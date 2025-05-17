package controller.member.async;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.MemberDAO;
import model.dto.MemberDTO;

@WebServlet("/UpdatePasswordServlet")
public class UpdatePasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public UpdatePasswordServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		
		long memberNumber = 0;
		String memberId = null;
		
		if(session.getAttribute("loginedMemberNumber")!=null && request.getParameter("memberId") == null) {
			memberNumber = (long)session.getAttribute("loginedMemberNumber");
		}
		else if(request.getParameter("memberId")!= null && session.getAttribute("loginedMemberNumber") == null) {
			memberId = request.getParameter("memberId");
		}
		
		String newPassword = request.getParameter("memberPassword");
		
		System.out.println("비밀번호 변경할 회원 번호 [" +memberNumber+ "]");
		System.out.println("비밀번호 변경할 회원 아이디 ["+memberId+"]");
		System.out.println("변경할 비밀번호 ["+newPassword+"]");
		
		MemberDTO memberDTO = new MemberDTO();
		MemberDAO memberDAO = new MemberDAO();
		
		memberDTO.setCondition("UPDATEPASSWORD");
		memberDTO.setMemberPassword(newPassword);
		
		if(memberNumber != 0 && (memberId==null || memberId.isEmpty())) {
			memberDTO.setMemberNumber(memberNumber);
		}else if(memberId != null && memberNumber == 0) {
			memberDTO.setMemberId(memberId);
		}

		// 응답 처리
		PrintWriter out = response.getWriter();
		
		boolean flag = memberDAO.update(memberDTO);
		if(flag) {
			System.out.println("내 정보 페이지 비밀번호 변경 성공");
			out.print("true");
		}else {
			System.out.println("내 정보 페이지 비밀번호 변경 실패");
			out.print("false");
		}
		
	}

}
