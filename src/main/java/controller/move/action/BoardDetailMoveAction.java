package controller.move.action;

import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.dao.BoardComboDAO;
import model.dao.BoardComboLikedDAO;
import model.dto.BoardComboDTO;
import model.dto.BoardComboLikedDTO;

public class BoardDetailMoveAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request) {
		ActionForward forward = new ActionForward();
		HttpSession session = request.getSession();

		// 글 상세 페이지 이동 액션

		// 글 번호 받기
		System.out.println(request.getParameter("boardComboNumber"));
		
		long boardComboNumber = Long.parseLong(request.getParameter("boardComboNumber"));

		System.out.println("글 상세 번호 [" + boardComboNumber + "]");

		
		BoardComboDTO boardComboDTO = new BoardComboDTO();
		BoardComboDAO boardComboDAO = new BoardComboDAO();

		boardComboDTO.setCondition("SELECTONECOMBOBOARD");
		boardComboDTO.setBoardComboNumber(boardComboNumber);
		boardComboDTO = boardComboDAO.selectOne(boardComboDTO);
		
		request.setAttribute("boardComboDetailData", boardComboDTO);

		System.out.println("글 상세정보 출력 [" + boardComboDTO + "]");	
		
		forward.setPath("boardDetail.jsp");
		forward.setRedirect(false);
		
		System.out.println("이동 경로 ["+forward.getPath()+"]");

		return forward;

	}
}
