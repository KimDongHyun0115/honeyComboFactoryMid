package controller.move.action;
import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
//3. 회원가입으로 이동
public class JoinMoveAction implements Action {
	@Override
	public ActionForward execute(HttpServletRequest request) {
		ActionForward forward = new ActionForward();
		HttpSession session = request.getSession();

		// 회원가입 페이지 이동
		
		forward.setPath("join.jsp");
		forward.setRedirect(true);

		return forward;
	}
}
