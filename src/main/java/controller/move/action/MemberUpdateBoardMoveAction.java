package controller.move.action;

import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class MemberUpdateBoardMoveAction implements Action{
	
	@Override
	public ActionForward execute(HttpServletRequest request) {
		ActionForward forward = new ActionForward();
		HttpSession session = request.getSession();
		
		
		// 회원 글 작성 페이지로 이동 Action
		
		forward.setPath("updateBoard.jsp");
		forward.setRedirect(true);
		
		return forward;
	}

}
