package controller.member.async;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.MemberDAO;
import model.dto.MemberDTO;

@WebServlet("/FindPasswordServlet")
public class FindPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public FindPasswordServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 요청 인코딩 및 응답 타입 설정
		request.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=UTF-8");

		// 파라미터 받기
		String authMethod = request.getParameter("authMethod"); // "phoneNum" 또는 "email"
		String memberBirth = request.getParameter("memberBirth");
		String authValue = request.getParameter("authValue");
		String memberId = request.getParameter("memberId");

		// 생년월일, 인증 입력값이 없을 경우
		if (memberId == null || memberId.trim().isEmpty() || memberBirth == null || memberBirth.trim().isEmpty()
				|| authValue == null || authValue.trim().isEmpty()) {
			response.getWriter().print("false");
			return;
		}

		MemberDTO memberDTO = new MemberDTO();
		MemberDAO memberDAO = new MemberDAO();

		// 생년월일 날짜 형변환
		try {
			memberDTO.setMemberBirth(java.sql.Date.valueOf(memberBirth)); // yyyy-mm-dd 형식
		} catch (IllegalArgumentException e) {
			response.getWriter().print("false");
			return;
		}

		// 비밀번호 찾기 조건 설정
		memberDTO.setCondition("SELECTONEFINDPASSWORD");
		memberDTO.setMemberId(memberId);

		if ("phoneNum".equals(authMethod)) {
			memberDTO.setMemberPhoneNumber(authValue);
		} else if ("email".equals(authMethod)) {
			// 이메일을 '@' 기준으로 분리
			String[] emailParts = authValue.split("@");
			if (emailParts.length != 2) {
				response.getWriter().print("false");
				return;
			}
			memberDTO.setMemberEmailId(emailParts[0]);
			memberDTO.setMemberEmailDomain(emailParts[1]);
		} else {
			response.getWriter().print("false");
			return;
		}

		// 회원 정보 확인
		memberDTO = memberDAO.selectOne(memberDTO);

		if (memberDTO != null) {
			response.getWriter().print("true");
		} else {
			response.getWriter().print("false");
		}
	}

}
