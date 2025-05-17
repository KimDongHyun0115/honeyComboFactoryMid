package controller.member.async;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.MemberDAO;
import model.dto.MemberDTO;

import java.io.IOException;

@WebServlet("/WithdrawServlet")
public class WithdrawServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public WithdrawServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		System.out.println("회원탈퇴 서블릿 도착 여부");
		
		System.out.println(session.getAttribute("loginedMemberNumber"));
		long memberNumber = (long)session.getAttribute("loginedMemberNumber");
		System.out.println("회원탈퇴할 회원 번호 ["+memberNumber+"]");
		
		MemberDTO memberDTO = new MemberDTO();
		MemberDAO memberDAO = new MemberDAO();
		
		memberDTO.setCondition("UPDATECANCEL");
		memberDTO.setMemberNumber(memberNumber);
		boolean withdraw = memberDAO.update(memberDTO);
		
		if(withdraw) {
			System.out.println("회원탈퇴 성공");
			session.invalidate();
		}
		else {
			System.out.println("회원탈퇴 실패");
			
		}
		// 텍스트 응답 설정
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(Boolean.toString(withdraw)); // "true" 또는 "false" 반환

	}

}
