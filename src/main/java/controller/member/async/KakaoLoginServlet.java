package controller.member.async;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.MemberDAO;
import model.dto.MemberDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/KakaoLoginServlet") // 찾아올 주소
public class KakaoLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// 기본 생성자
	public KakaoLoginServlet() {
		super();
	}

	// GET 요청을 처리하는 메서드
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response); // GET 요청도 POST 요청 처리 메서드를 호출
	}

	// POST 요청은 GET 요청과 동일하게 처리하도록 함
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");

		// JSON 데이터 파싱
		StringBuilder kakaoLoginResponse = new StringBuilder();
		try (BufferedReader reader = request.getReader()) {
			String line;
			while ((line = reader.readLine()) != null) {
				kakaoLoginResponse.append(line);
			}
		}

		System.out.println("받은 카카오로그인 반환 정보 : [" + kakaoLoginResponse.toString() + "]");

		try {
			// JSONParser를 사용하여 카카오 로그인 응답을 JSON 객체로 변환
			JSONParser parser = new JSONParser();
			JSONObject jsonResponse = (JSONObject) parser.parse(kakaoLoginResponse.toString());

			// JSON 응답에서 'kakao_account' 객체를 추출하여 필요 정보 가져오기
			JSONObject kakaoAccount = (JSONObject) jsonResponse.get("kakao_account");
			long number = Long.parseLong(jsonResponse.get("id").toString());
			String name = kakaoAccount.get("name").toString();
			// 카카오에게 받은 이메일 주소에서 @ 앞부분만 추출
			String emailId = kakaoAccount.get("email").toString().split("@")[0];
			// 카카오에게 받은 이메일 주소에서 @ 뒤부분만 추출
			String emailDomain = kakaoAccount.get("email").toString().split("@")[1];
			// 카카오에게 받은 핸드폰번호에서 +82는 0으로 변경, "-"는 없애기
			String phoneNumber = kakaoAccount.get("phone_number").toString().replace("+82 ", "0").replaceAll("-", "")
					.trim();
			// 생년월일 하나로 만들기 작업
			String birthyear = kakaoAccount.get("birthyear").toString(); // 생년
			String birthday = kakaoAccount.get("birthday").toString(); // 월일 (MMDD 형식)
			String birth = birthyear + "-" + birthday.substring(0, 2) + "-" + birthday.substring(2); // 생년월일 (YYYY-MM-DD
																										// 형식)
			// 로그 찍기
			System.out.println("카카오 로그인 번호 : [" + number + "]");
			System.out.println("카카오 로그인 이름 : [" + name + "]");
			System.out.println("카카오 로그인 이메일아이디 : [" + emailId + "]");
			System.out.println("카카오 로그인 이메일도메인 : [" + emailDomain + "]");
			System.out.println("카카오 로그인 핸드폰번호 : [" + phoneNumber + "]");
			System.out.println("카카오 로그인 생년월일 : [" + birth + "]");

			MemberDAO memberDAO = new MemberDAO();
			MemberDTO memberDTO = new MemberDTO();
			memberDTO.setCondition("SELECTONEFINDPASSWORD");
			memberDTO.setMemberNumber(number);
			memberDTO.setMemberId(emailId);
			memberDTO.setMemberPassword("KAKAO");
			memberDTO.setMemberName(name);
			memberDTO.setMemberEmailId(emailId);
			memberDTO.setMemberEmailDomain(emailDomain);
			memberDTO.setMemberPhoneNumber(phoneNumber);
			memberDTO.setMemberBirth(java.sql.Date.valueOf(birth));

			MemberDTO isJoinedMember = memberDAO.selectOne(memberDTO);
			System.out.println("카카오 로그인-DB 저장 기록 확인 : [" + isJoinedMember + "]");

			boolean flag = true;
			if (isJoinedMember != null) { // DB에 회원번호가 있다면
				System.out.println("카카오 로그인-저장 기록 있음");

				// 두 객체의 값이 모두 같지 않다면
				if (!(memberDTO.equals(isJoinedMember))) {
					System.out.println("두 객체의 값이 하나라도 다름");
					flag = memberDAO.update(memberDTO);
				}
			} else { // DB에 회원번호가 없다면
				System.out.println("카카오 로그인-저장 기록 없음");

				flag = memberDAO.insert(memberDTO);
			}

			PrintWriter out = response.getWriter();
			if (!flag) { // 저장/업뎃에 실패했다면
				System.out.println("카카오 로그인-DB 정보 저장/업뎃 실패");
				out.print("false");
				out.flush();
			}
			
			// 로그인 정보 받아오기용 파서 세팅
			memberDTO.setCondition("SELECTONELOGIN");
			memberDTO.setMemberId(emailId);
			memberDTO.setMemberPassword("KAKAO");
			memberDTO = memberDAO.selectOne(memberDTO);

			// 로그인 성공 시의 필요 세션 세팅
			HttpSession session = request.getSession();
			session.setAttribute("loginedMemberNumber", memberDTO.getMemberNumber());
			session.setAttribute("loginedMemberName", name);
			session.setAttribute("memberIsAdmin", false);

			out.print(memberDTO.getMemberNumber());
			out.flush();
		} catch (ParseException e) {
			// JSON 파싱 에러 발생 시 예외 처리
			e.printStackTrace();
			// 클라이언트에게 에러 메시지 반환 (잘못된 카카오 로그인 응답)
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 카카오 로그인 응답입니다.");
		}
	}
}