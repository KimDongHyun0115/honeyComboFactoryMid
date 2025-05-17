package controller.move.action;
import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class ErrorMoveAction implements Action {
	@Override
	public ActionForward execute(HttpServletRequest request) {
		ActionForward forward = new ActionForward();
		HttpSession session = request.getSession();

		// 로그인 페이지 이동
		
		forward.setPath("error.jsp");
		forward.setRedirect(true);

		return forward;
	}
}


