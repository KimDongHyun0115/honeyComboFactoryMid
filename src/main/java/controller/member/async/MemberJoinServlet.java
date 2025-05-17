package controller.member.async;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.MemberDAO;
import model.dto.MemberDTO;

@WebServlet("/MemberJoinServlet")
public class MemberJoinServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MemberJoinServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		System.out.println("회원가입 서블릿 도착");
		
		// 폼 데이터 받아오기
		String memberId = request.getParameter("memberId");
        String memberPassword = request.getParameter("memberPassword");
        String memberName = request.getParameter("memberName");
        String memberBirth = request.getParameter("memberBirth");
        String memberPhoneNumber = request.getParameter("memberPhoneNumber");
        String memberAddressMain = request.getParameter("memberAddressMain");
        String memberAddressDetail = request.getParameter("memberAddressDetail");
        String memberEmailId = request.getParameter("memberEmailId");
        String memberEmailDomain = request.getParameter("memberEmailDomain");

        System.out.println("회원 아이디 ["+memberId+"]");
        System.out.println("회원 아이디 ["+memberPassword+"]");
        System.out.println("회원 아이디 ["+memberName+"]");
        System.out.println("회원 아이디 ["+memberBirth+"]");
        System.out.println("회원 아이디 ["+memberPhoneNumber+"]");
        System.out.println("회원 아이디 ["+memberAddressMain+"]");
        System.out.println("회원 아이디 ["+memberAddressDetail+"]");
        System.out.println("회원 아이디 ["+memberEmailId+"]");
        System.out.println("회원 아이디 ["+memberEmailDomain+"]");
        
     // MemberDTO 객체 생성
        MemberDTO memberDTO = new MemberDTO();
        MemberDAO memberDAO = new MemberDAO();
        
        memberDTO.setMemberId(memberId);
        memberDTO.setMemberPassword(memberPassword);
        memberDTO.setMemberName(memberName);
        memberDTO.setMemberBirth(java.sql.Date.valueOf(memberBirth)); // YYYY-MM-DD 형식으로 변환
        memberDTO.setMemberPhoneNumber(memberPhoneNumber);
        memberDTO.setMemberAddressMain(memberAddressMain);
        memberDTO.setMemberAddressDetail(memberAddressDetail);
        memberDTO.setMemberEmailId(memberEmailId);
        memberDTO.setMemberEmailDomain(memberEmailDomain);
        
        boolean isJoined = memberDAO.insert(memberDTO);
        
        System.out.println("회원가입 성공 여부 [" +isJoined+ "]");
        
     // 결과 응답
        if (isJoined) {
            response.getWriter().write("true"); // 회원가입 성공 시 "true" 반환
        } else {
            response.getWriter().write("false"); // 회원가입 실패 시 "false" 반환
        }
        
	}

}
