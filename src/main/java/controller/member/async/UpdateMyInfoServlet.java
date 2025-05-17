package controller.member.async;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.MemberDAO;
import model.dto.MemberDTO;

@WebServlet("/UpdateMyInfoServlet")
public class UpdateMyInfoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public UpdateMyInfoServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		MemberDTO memberDTO = new MemberDTO();
		MemberDAO memberDAO = new MemberDAO();
		
		long memberNumber = (long)session.getAttribute("loginedMemberNumber");
		String memberName = request.getParameter("memberName");
		String memberPhoneNumber = request.getParameter("memberPhoneNumber");
		String memberAddressMain = request.getParameter("memberAddressMain");
		String memberAddressDetail = request.getParameter("memberAddressDetail");
		String memberEmailId = request.getParameter("memberEmailId");
		String memberEmailDomain = request.getParameter("memberEmailDomain");
		
		memberDTO.setCondition("UPDATEMYINFORMATION");
		memberDTO.setMemberNumber(memberNumber);
		memberDTO.setMemberName(memberName);
		memberDTO.setMemberPhoneNumber(memberPhoneNumber);
		memberDTO.setMemberAddressMain(memberAddressMain);
		memberDTO.setMemberAddressDetail(memberAddressDetail);
		memberDTO.setMemberEmailId(memberEmailId);
		memberDTO.setMemberEmailDomain(memberEmailDomain);
		
		boolean flag = memberDAO.update(memberDTO);
		
		if(flag) {
			System.out.println("회원정보 업데이트 성공");
			response.getWriter().write("true");
		}
		else {
			System.out.println("회원정보 업데이트 실패");
			response.getWriter().write("false");

		}
		
	}

}
