package controller.member.action;

import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

// 로그아웃
public class LogoutAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request) {
		ActionForward forward = new ActionForward();
		HttpSession session = request.getSession();

		// 회원 이름/번호/관리자 여부/장바구니 세션 삭제
		session.removeAttribute("loginedMemberName");
		session.removeAttribute("loginedMemberNumber");
		session.removeAttribute("memberIsAdmin");
		session.removeAttribute("shoppingCart");

		System.out.println("로그아웃 성공");
		
//		request.setAttribute("msg", "로그아웃 성공!");
//		request.setAttribute("flag", true);
//		request.setAttribute("url", "controller.jsp?action=MAIN");

		forward.setPath("main.do");
		forward.setRedirect(false);
		return forward;
	}
}
