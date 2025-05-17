package controller.move.action;
import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

// 아이디/비밀번호 찾기로 이동
public class FindAccountMoveAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request) {
			ActionForward forward = new ActionForward();
			HttpSession session = request.getSession();

			// 계정 찾기 페이지 이동 Action
			
			forward.setPath("findAccount.jsp");
			forward.setRedirect(true);

			return forward;
		}
	}
