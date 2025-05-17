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

@WebServlet("/FindIdServlet")
public class FindIdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public FindIdServlet() {
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

		String authMethod = request.getParameter("authMethod"); // "phoneNum" 또는 "email"
        String memberBirth = request.getParameter("memberBirth");
        String authValue = request.getParameter("authValue");
		
        // 생년월일, 인증 입력값이 없을 경우
        if (memberBirth == null || memberBirth.trim().isEmpty() || authValue == null || authValue.trim().isEmpty()) {
            response.getWriter().print("");
            return;
        }
        
        MemberDTO memberDTO = new MemberDTO();
        MemberDAO memberDAO = new MemberDAO();
        
        // 아이디 검색조건
        memberDTO.setCondition("SELECTONEFINDID");
        
        // 생년월일 날짜 형변환
        try {
            memberDTO.setMemberBirth(java.sql.Date.valueOf(memberBirth));
            System.out.println("아이디 찾을 생년월일 [" + memberBirth + "]");
        } catch (IllegalArgumentException e) {
            response.getWriter().print("");
            return;
        }
        
        if ("phoneNum".equals(authMethod)) {
            memberDTO.setCondition("SELECTONEFINDID");
            // 핸드폰 번호 집어 넣기
            memberDTO.setMemberPhoneNumber(authValue);
        } else if ("email".equals(authMethod)) {
        	// 이메일 쪼개기
            String[] emailParts = authValue.split("@");
            if (emailParts.length != 2) {
                response.getWriter().print("");
                return;
            }
            // 쪼갠 이메일 넣기
            memberDTO.setMemberEmailId(emailParts[0]);
            memberDTO.setMemberEmailDomain(emailParts[1]);
        } else {
            response.getWriter().print("");
            return;
        }
        
        memberDTO = memberDAO.selectOne(memberDTO);
        if (memberDTO != null) {
            response.getWriter().print(memberDTO.getMemberId());
        } else {
            response.getWriter().print("");
        }

	}

}
