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

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LoginServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doPost(request,response);
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		System.out.println("로그인 서블릿 도착 여부");
		
		String memberId = request.getParameter("memberId");
		String memberPassword = request.getParameter("memberPassword");
		
		System.out.println("로그인 할 회원 아이디 [" +memberId+ "]");
		System.out.println("로그인 할 회원 비밀번호 ["+memberPassword+ "]");
		
		MemberDTO memberDTO = new MemberDTO();
		MemberDAO memberDAO = new MemberDAO();
		
		boolean isLogined = false;
		
		if(memberId != null && memberPassword != null) {
			memberDTO.setCondition("SELECTONELOGIN");
			memberDTO.setMemberId(memberId);
			memberDTO.setMemberPassword(memberPassword);
			memberDTO = memberDAO.selectOne(memberDTO);
			
			System.out.println("로그인 할 회원 정보 ["+memberDTO+"]");
			if(memberDTO != null) {
				if(!memberDTO.isMemberIsWithdraw()) {
					isLogined = true;
				}
			}
		}
		
		System.out.println("로그인 성공 여부 ["+isLogined+"]");
		
		// 응답 처리: 성공/실패 여부 반환
        PrintWriter out = response.getWriter();
        if (isLogined) {
            session.setAttribute("loginedMemberName", memberDTO.getMemberName());
			session.setAttribute("memberIsAdmin", memberDTO.isMemberIsAdmin());
			session.setAttribute("loginedMemberNumber", memberDTO.getMemberNumber());
            
			out.print(memberDTO.getMemberNumber()); // 로그인 성공
        } else {
            out.print("false"); // 로그인 실패
        }
        out.flush();
		
		
		
	}

}
