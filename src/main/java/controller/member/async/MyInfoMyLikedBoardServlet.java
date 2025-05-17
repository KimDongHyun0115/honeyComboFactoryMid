package controller.member.async;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.BoardComboLikedDAO;
import model.dto.BoardComboLikedDTO;

@WebServlet("/MyInfoMyLikedBoardServlet")
public class MyInfoMyLikedBoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MyInfoMyLikedBoardServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("내 좋아요 목록 서블릿 도착");
		
		HttpSession session = request.getSession();

		
		// 좋아요 글 내역
		BoardComboLikedDTO boardComboLikedDTO = new BoardComboLikedDTO();
		BoardComboLikedDAO boardComboLikedDAO = new BoardComboLikedDAO();

		long memberNumber = (long) session.getAttribute("loginedMemberNumber");
		
		// 페이지 값 받기
		int likedBoardPageNumber = 1;
		if (request.getParameter("likedBoardPageNumber") != null) {
			likedBoardPageNumber = Integer.parseInt(request.getParameter("likedBoardPageNumber"));
		}
		System.out.println("페이지 번호 로그 [" + likedBoardPageNumber + "]");

		// 보여줄 내역 수
		int likedBoardContentCount = 3;
		if (request.getParameter("likedBoardContentCount") != null) {
			likedBoardContentCount = Integer.parseInt(request.getParameter("likedBoardContentCount"));
		}
		System.out.println("좋아요 보여줄 내역 수 [" + likedBoardContentCount + "]");

		int likedBoardStartIndex = (likedBoardPageNumber - 1) * likedBoardContentCount;
		System.out.println("작성글 시작 인덱스 번호 [" + likedBoardStartIndex + "]");

		boardComboLikedDTO.setMemberNumber(memberNumber);
		boardComboLikedDTO.setBoardComboLikedIndex(likedBoardStartIndex);
		boardComboLikedDTO.setBoardComboLikedContentCount(likedBoardContentCount);

		ArrayList<BoardComboLikedDTO> boardComboLikedList = boardComboLikedDAO.selectAll(boardComboLikedDTO);
		if (boardComboLikedList == null) {
			boardComboLikedList = new ArrayList<>();
        }

		System.out.println("좋아요한 글 로그 [" + boardComboLikedList + "]");

		// JSON 변환
		JSONArray boardComboLikedArray = new JSONArray();
		for (BoardComboLikedDTO boardComboLiked : boardComboLikedList) {
			JSONObject boardComboLikedObject = new JSONObject();

			// JSONObject는 Map이므로 타입을 명시적으로 설정
			boardComboLikedObject.put("boardComboNumber", boardComboLiked.getBoardComboNumber());
			boardComboLikedObject.put("memberName", boardComboLiked.getMemberName());
			boardComboLikedObject.put("totalCountNumber", boardComboLiked.getTotalCountNumber());

			boardComboLikedArray.add(boardComboLikedObject);
		}

		// 응답 설정 및 전송
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(boardComboLikedArray.toJSONString());
	}

}
