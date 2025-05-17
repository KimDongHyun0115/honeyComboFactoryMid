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
import model.dao.BoardComboDAO;
import model.dto.BoardComboDTO;

@WebServlet("/MyInfoMyBoardServlet")
public class MyInfoMyBoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MyInfoMyBoardServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		System.out.println("내 게시글 목록 서블릿 도착");
		
		HttpSession session = request.getSession();

		// 본인 작성 글 페이지네이션
		BoardComboDTO boardComboDTO = new BoardComboDTO();
		BoardComboDAO boardComboDAO = new BoardComboDAO();
		
		long memberNumber = (long) session.getAttribute("loginedMemberNumber");

		System.out.println("꿀조합 게시판 회원번호 [" + memberNumber + "]");

		// 페이지 값 받기
		int myBoardPageNumber = 1;
		if(request.getParameter("myBoardPageNumber") != null) {
			myBoardPageNumber = Integer.parseInt(request.getParameter("myBoardPageNumber"));
		}
		System.out.println("페이지 번호 로그 [" +myBoardPageNumber+ "]");
		
		// 보여줄 내역 수
		int myBoardContentCount = 3;
		if (request.getParameter("myBoardContentCount") != null) {
			myBoardContentCount = Integer.parseInt(request.getParameter("myBoardContentCount"));
		}
		System.out.println("작성글 보여줄 내역 수 [" + myBoardContentCount + "]");

		int boardStartIndex = (myBoardPageNumber - 1) * myBoardContentCount;
		System.out.println("작성글 시작 인덱스 번호 [" + boardStartIndex + "]");

		boardComboDTO.setCondition("SELECTALLMEMBERWRITE");
		boardComboDTO.setMemberNumber(memberNumber);
		boardComboDTO.setBoardComboIndex(boardStartIndex);
		boardComboDTO.setBoardComboContentCount(myBoardContentCount);
		
		ArrayList<BoardComboDTO> boardComboList = boardComboDAO.selectAll(boardComboDTO);
		if (boardComboList == null) {
            boardComboList = new ArrayList<>();
        }
		
		System.out.println("본인 작성글 로그 [" + boardComboList + "]");
		
		// JSON 변환
		JSONArray boardArray = new JSONArray();
		for (BoardComboDTO board : boardComboList) {
		    JSONObject boardObject = new JSONObject();
		    
		    // JSONObject는 Map이므로 타입을 명시적으로 설정
		    boardObject.put("boardComboNumber", board.getBoardComboNumber());
		    boardObject.put("boardComboTitle", board.getBoardComboTitle());
		    boardObject.put("boardComboViewCount", board.getBoardComboViewCount());
		    boardObject.put("boardComboLikedCount", board.getBoardComboLikedCount());
		    boardObject.put("boardComboRegisterDate", board.getBoardComboRegisterDate().toString());
		    boardObject.put("totalCountNumber", board.getTotalCountNumber());
		    boardArray.add(boardObject);
		}

		// 응답 설정 및 전송
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(boardArray.toJSONString());
	}

}
