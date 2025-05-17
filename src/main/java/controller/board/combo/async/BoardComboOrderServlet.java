package controller.board.combo.async;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.BoardComboDAO;
import model.dto.BoardComboDTO;

@WebServlet("/BoardComboOrderServlet")
public class BoardComboOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public BoardComboOrderServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 정렬 조건을 요청 파라미터에서 받아옴
		String orderCondition = request.getParameter("boardOrderCondition");
		System.out.println("백단 도착 여부: " + orderCondition);

		int boardPageNumber = Integer.parseInt(request.getParameter("boardPageNumber"));
		int boardContentCount = Integer.parseInt(request.getParameter("boardContentCount"));

		System.out.println("더보기 횟수: [" + boardPageNumber + "]");
		System.out.println("보여줄 데이터 수: [" + boardContentCount + "]");

		int startIndex = (boardPageNumber - 1) * boardContentCount;
		System.out.println("시작 인덱스 번호: [" + startIndex + "]");

		BoardComboDTO boardComboDTO = new BoardComboDTO();
		if ("ORDERUPTODATE".equals(orderCondition)) {
			boardComboDTO.setCondition("SELECTALLMEMBERCONTENTDESC");
		} else if ("ORDEROLD".equals(orderCondition)) {
			boardComboDTO.setCondition("SELECTALLMEMBERCONTENTASC");
		} else if ("ORDERPOPULAR".equals(orderCondition)) {
			boardComboDTO.setCondition("SELECTALLMEMBERCONTENTPOPULAR");
		}

		boardComboDTO.setBoardComboIndex(startIndex);
		boardComboDTO.setBoardComboContentCount(boardContentCount);

		BoardComboDAO boardComboDAO = new BoardComboDAO();
		ArrayList<BoardComboDTO> boardComboDatas = boardComboDAO.selectAll(boardComboDTO);
		if (boardComboDatas == null) {
			boardComboDatas = new ArrayList<>();
		}

		System.out.println("게시글 내역 로그: " + boardComboDatas);
		// JSON 배열로 상품 정보를 변환
		JSONArray boardComboArray = new JSONArray();
		for (BoardComboDTO boardCombo : boardComboDatas) {
			JSONObject boardComboObject = new JSONObject();
			boardComboObject.put("boardComboNumber", boardCombo.getBoardComboNumber());
			boardComboObject.put("memberName", boardCombo.getMemberName());
			boardComboObject.put("boardComboTitle", boardCombo.getBoardComboTitle());
			boardComboObject.put("boardComboViewCount", boardCombo.getBoardComboViewCount());
			boardComboObject.put("boardComboLikedCount", boardCombo.getBoardComboLikedCount());
			boardComboObject.put("boardComboRegisterDate", boardCombo.getBoardComboRegisterDate().toString());
			boardComboObject.put("totalCountNumber", boardCombo.getTotalCountNumber());
			boardComboArray.add(boardComboObject);

			System.out.println("내보내는 데이터 [" + boardComboObject + "]");

		}
		JSONObject boardComboData = new JSONObject();

		boardComboData.put("boardComboDatas", boardComboArray);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().print(boardComboData.toJSONString());
		response.getWriter().flush();
		response.getWriter().close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
