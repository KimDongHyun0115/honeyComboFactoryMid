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

@WebServlet("/ChangePasswordServlet")
public class ChangePasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ChangePasswordServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		String memberId = request.getParameter("memberId");
		String newPassword = request.getParameter("memberPassword");

		System.out.println("비밀번호 변경할 회원 번호 ["+memberId+"]");
		System.out.println("변경할 비밀번호 ["+newPassword+"");
		
		MemberDTO memberDTO = new MemberDTO();
		MemberDAO memberDAO = new MemberDAO();

		memberDTO.setCondition("UPDATEFINDPASSWORD");
		memberDTO.setMemberPassword(newPassword);
		memberDTO.setMemberId(memberId);

		boolean flag = memberDAO.update(memberDTO);

		// 응답 처리
		PrintWriter out = response.getWriter();
		if (flag) {
			System.out.println("비밀번호 변경 성공");
			out.print("true"); // 비밀번호 변경 성공 시 true 반환
		} else {
			System.out.println("비밀번호 변경 실패");
			out.print("false"); // 비밀번호 변경 실패 시 false 반환
		}
		out.flush(); // 응답 버퍼 비우기

	}

}
