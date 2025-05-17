package controller.move.action;

import java.util.ArrayList;

import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import model.dao.BoardComboDAO;
import model.dao.ProductSingleDAO;
import model.dto.BoardComboDTO;
import model.dto.ProductSingleDTO;

public class BoardComboMoveAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request) {
		ActionForward forward = new ActionForward();

		System.out.println("BoardComboMove 도착 여부");

		// 꿀조합 게시판 정보 확인 후 생성 및 request에 저장
		BoardComboDTO boardComboDTO = new BoardComboDTO();
		BoardComboDAO boardComboDAO = new BoardComboDAO();

//		System.out.println("게시판 페이지 번호 [" +request.getParameter("boardPageNumber")+ "]");
//		System.out.println("게시판 데이터 수 [" +request.getParameter("boardContentCount")+"]");
		
		// 클라이언트에서 보내는 페이지 번호와 데이터 수
		int boardPageNumber = 1;
		int boardContentCount = 5;

		System.out.println("더보기 횟수 [" + boardPageNumber + "]");
		System.out.println("보여줄 데이터 수 [" + boardContentCount + "]");

		// 시작 인덱스
		int startIndex = (boardPageNumber - 1) * boardContentCount;
		System.out.println("시작 인덱스 번호 [" + startIndex + "]");

		// 페이지네이션에 필요한 값 전달
		// 최신순 정렬
		boardComboDTO.setBoardComboIndex(startIndex);
		boardComboDTO.setBoardComboContentCount(boardContentCount);

		boardComboDTO.setCondition("SELECTALLADMINCONTENT");
		ArrayList<BoardComboDTO> admindBoardComboList = boardComboDAO.selectAll(boardComboDTO);
		request.setAttribute("boardComboAdminDatas", admindBoardComboList);
		
		System.out.println("관리자 작성글 출력 로그");
		System.out.println(admindBoardComboList);

		// 꿀조합게시판 페이지 이동 Action
		forward.setPath("comboBoard.jsp");
		forward.setRedirect(false);

		return forward;
	}
}
