package controller.member.async;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.MemberDAO;
import model.dto.MemberDTO;

@WebServlet("/CheckJoinIdServlet")
public class CheckJoinIdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CheckJoinIdServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");

		System.out.println("아이디 중복검사 서블릿 도착");
		
		String joinMemberId = request.getParameter("memberId");

		System.out.println("입력한 아이디 [" +joinMemberId+ "]");
		
		// 입력값이 없을 경우 예외 처리
		boolean isAvailable = false;

		if (joinMemberId != null && !joinMemberId.trim().isEmpty()) {
			MemberDAO memberDAO = new MemberDAO();
			MemberDTO memberDTO = new MemberDTO();
			memberDTO.setCondition("SELECTONEMEMBER");
			memberDTO.setMemberId(joinMemberId);

			memberDTO = memberDAO.selectOne(memberDTO);
			
			System.out.println("입력한 아이디 존재하면 정보 [" +memberDTO+"]");
			
			if(memberDTO == null) {
				isAvailable = true;
			}
			else if(memberDTO != null) {
				isAvailable = false;
			}
		}
		
		System.out.println("중복 여부 ["+isAvailable+"]");
		
		
		PrintWriter out = response.getWriter();
		out.print(isAvailable);
		out.flush();

	}

}
