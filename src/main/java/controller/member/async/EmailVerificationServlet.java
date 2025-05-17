package controller.member.async;

import java.io.IOException;
import java.sql.Date;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.json.simple.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.MemberDAO;
import model.dto.MemberDTO;
// AJAX 요청 받아서 처리
@WebServlet("/verifyEmailCode.do") //  서블릿에서 요청한 URL
public class EmailVerificationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=UTF-8"); // 응답형식 JSON 선언
        HttpSession session = request.getSession(); 

        JSONObject result = new JSONObject(); // 응답 메시지 담을 변수 선언

        try { // AJAX에서 전달받은 데이터 받음
            String emailId = request.getParameter("memberEmailId"); 
            String emailDomain = request.getParameter("memberEmailDomain");
            String birth = request.getParameter("memberBirth");

            // DTO에 값 세팅
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setMemberEmailId(emailId);
            memberDTO.setMemberEmailDomain(emailDomain);
            memberDTO.setMemberBirth(Date.valueOf(birth));
            memberDTO.setCondition("SELECTONEFINDID");

            // DB에서 일치하는 회원이 있는지 확인
            MemberDAO memberDAO = new MemberDAO();
            memberDTO = memberDAO.selectOne(memberDTO);

            if (memberDTO == null) { // 일치하는 회원이 없으면
                result.put("message", "일치하는 회원 정보가 없습니다.");
                response.getWriter().write(result.toJSONString());
                return; // 실패 
            }
            // 일치하는 회원이 있다면
            // 인증번호 생성해서 세션에 저장
            // 해당 회원의 아이디도 함께 세션에 저장해두었다가 화면에 표시해줄 때 사용
            String verificationCode = String.format("%06d", new Random().nextInt(1000000));
            session.setAttribute("verificationCode", verificationCode);
            session.setAttribute("findId", memberDTO.getMemberId());

            // 이메일 전송
            // 구글 계정으로 로그인 설정 
            String to = emailId + "@" + emailDomain;
            String from = "이메일"; // 보낼 이메일
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            Session mailSession = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("이메일", "앱 비밀번호"); // 앱 비밀번호
                }
            });

            Message message = new MimeMessage(mailSession); // 메일 제목 및 내용 설정
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("[꿀조합팩토리] 인증번호 안내"); // 제목
            message.setText("인증번호는 [" + verificationCode + "] 입니다."); // 내용

            Transport.send(message); // 메일 전송

            result.put("message", "인증번호가 이메일로 전송되었습니다.");
            result.put("code", verificationCode); // 추가


        } catch (Exception e) {
            e.printStackTrace();
            result.put("message", "서버 오류 또는 이메일 전송 실패");
        }

        response.getWriter().write(result.toJSONString());
    }
}
