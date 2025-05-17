package controller.move.action;

import java.util.ArrayList;

import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.dao.BoardComboDAO;
import model.dao.BoardComboLikedDAO;
import model.dao.MemberDAO;
import model.dao.PurchaseDAO;
import model.dto.BoardComboDTO;
import model.dto.BoardComboLikedDTO;
import model.dto.MemberDTO;
import model.dto.PurchaseDTO;

public class MyPageMoveAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request) {
		ActionForward forward = new ActionForward();
		HttpSession session = request.getSession();

		// 마이페이지 페이지 이동 Action

		MemberDTO memberDTO = new MemberDTO();
		MemberDAO memberDAO = new MemberDAO();
		long memberNumber = (long) session.getAttribute("loginedMemberNumber");

		System.out.println("마이페이지 이동할 회원 번호 [" + memberNumber + "]");

		// 회원번호 저장 후 일치하는 값 찾아서 request로 보내기
		memberDTO.setMemberNumber(memberNumber);
		memberDTO.setCondition("SELECTONEMYINFORMATION");
		memberDTO = memberDAO.selectOne(memberDTO);

		request.setAttribute("myInfoData", memberDTO);

		System.out.println("회원 정보 로그 [" + memberDTO + "]");

		// 주문 내역
		PurchaseDTO purchaseDTO = new PurchaseDTO();
		PurchaseDAO purchaseDAO = new PurchaseDAO();

		System.out.println("주문상세 회원번호 [" + memberNumber + "]");

		// 페이지 값 받기
		int purchasePage = 1;
		if (request.getParameter("purchasePageNumber") != null) {
			purchasePage = Integer.parseInt(request.getParameter("purchasePageNumber"));
		}
		System.out.println("페이지 번호 로그 [" + purchasePage + "]");

		// 보여줄 내역 수
		int purchasePerPage = 3;
		if (request.getParameter("purchaseContentCount") != null) {
			purchasePerPage = Integer.parseInt(request.getParameter("purchaseContentCount"));
		}

		System.out.println("내역 수 로그 [" + purchasePerPage + "]");

		// 시작 인덱스
		int purchaseStartIndex = (purchasePage - 1) * purchasePerPage;
		System.out.println("주문내역 시작 인덱스 번호 [" + purchaseStartIndex + "]");

		// 주문 페이지네이션에 필요한 값 전달
		purchaseDTO.setCondition("SELECTALLONEPERSON");
		purchaseDTO.setMemberNumber(memberNumber);
		purchaseDTO.setPurchaseIndex(purchaseStartIndex);
		purchaseDTO.setPurchaseContentCount(purchasePerPage);
		ArrayList<PurchaseDTO> purchaseList = purchaseDAO.selectAll(purchaseDTO);
		request.setAttribute("purchaseData", purchaseList);

		System.out.println("주문내역 로그 [" + purchaseList + "]");

		// 본인 작성글
		BoardComboDTO boardComboDTO = new BoardComboDTO();
		BoardComboDAO boardComboDAO = new BoardComboDAO();

		// 페이지 값 받기
		int boardPage = 1;
		if (request.getParameter("myBoardPageNumber") != null) {
			boardPage = Integer.parseInt(request.getParameter("myBoardPageNumber"));
		}
		System.out.println("페이지 번호 로그 [" + boardPage + "]");

		// 보여줄 내역 수
		int boardPerPage = 1;
		if (request.getParameter("myBoardContentCount") != null) {
			boardPerPage = Integer.parseInt(request.getParameter("myBoardContentCount"));
		}
		System.out.println("작성글 보여줄 내역 수 [" + boardPerPage + "]");

		int boardStartIndex = (boardPage - 1) * boardPerPage;
		System.out.println("작성글 시작 인덱스 번호 [" + boardStartIndex + "]");

		boardComboDTO.setCondition("SELECTALLMEMBERWRITE");
		boardComboDTO.setMemberNumber(memberNumber);
		boardComboDTO.setBoardComboIndex(boardStartIndex);
		boardComboDTO.setBoardComboContentCount(boardPerPage);
		ArrayList<BoardComboDTO> boardComboList = boardComboDAO.selectAll(boardComboDTO);
		request.setAttribute("myBoardDatas", boardComboList);

		System.out.println("본인 작성글 로그 [" + boardComboList + "]");

		// 좋아요 글 내역
		BoardComboLikedDTO boardComboLikedDTO = new BoardComboLikedDTO();
		BoardComboLikedDAO boardComboLikedDAO = new BoardComboLikedDAO();

		// 페이지 값 받기
		int likedBoardPage = 1;
		if (request.getParameter("likedBoardPageNumber") != null) {
			likedBoardPage = Integer.parseInt(request.getParameter("likedBoardPageNumber"));
		}
		System.out.println("페이지 번호 로그 [" + likedBoardPage + "]");

		// 보여줄 내역 수
		int likedBoardPerPage = 1;
		if (request.getParameter("likedBoardContentCount") != null) {
			likedBoardPerPage = Integer.parseInt(request.getParameter("likedBoardContentCount"));
		}
		System.out.println("좋아요 보여줄 내역 수 [" + likedBoardPerPage + "]");

		int likedBoardStartIndex = (likedBoardPage - 1) * likedBoardPerPage;
		System.out.println("작성글 시작 인덱스 번호 [" + likedBoardStartIndex + "]");

		boardComboLikedDTO.setMemberNumber(memberNumber);
		boardComboLikedDTO.setBoardComboLikedIndex(likedBoardStartIndex);
		boardComboLikedDTO.setBoardComboLikedContentCount(likedBoardPerPage);

		ArrayList<BoardComboLikedDTO> boardComboLikedList = boardComboLikedDAO.selectAll(boardComboLikedDTO);
		request.setAttribute("likedBoardDatas", boardComboLikedList);

		System.out.println("좋아요한 글 로그 [" + boardComboLikedList + "]");

		forward.setPath("myInfo.jsp");
		forward.setRedirect(false);

		return forward;
	}
}
