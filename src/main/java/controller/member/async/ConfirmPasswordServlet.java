package controller.member.async;

import java.io.IOException;

import org.json.simple.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.MemberDAO;
import model.dto.MemberDTO;

@WebServlet("/ConfirmPasswordServlet")
public class ConfirmPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ConfirmPasswordServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("비밀번호 인증 서블릿 도착");

		HttpSession session = request.getSession();

		long memberNumber = (long) session.getAttribute("loginedMemberNumber");
		String memberPassword = request.getParameter("loginedMemberPassword");

		System.out.println("본인인증할 회원 번호 [" + memberNumber + "]");
		System.out.println("인증 요청한 비밀번호 [" + memberPassword + "]");

		MemberDTO memberDTO = new MemberDTO();
		MemberDAO memberDAO = new MemberDAO();

		memberDTO.setCondition("SELECTONEMYPAGE");
		memberDTO.setMemberNumber(memberNumber);
		memberDTO.setMemberPassword(memberPassword);
		memberDTO = memberDAO.selectOne(memberDTO);
		
		System.out.println("인증 요청한 회원 정보 ["+memberDTO+"]");

		// 클라이언트에게 응답하는 콘텐츠 유형을 plain text로 설정
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");

		if (memberDTO != null) {
			System.out.println("인증 확인");
			response.getWriter().write("true");
			response.getWriter().flush();
			response.getWriter().close();
		} else {
			System.out.println("인증 실패");
			response.getWriter().write("false");
			response.getWriter().flush();
			response.getWriter().close();
		}
		
	}

}
