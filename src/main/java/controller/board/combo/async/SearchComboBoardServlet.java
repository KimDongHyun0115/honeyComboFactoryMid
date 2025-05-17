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
import model.dto.ProductSingleDTO;

@WebServlet("/SearchComboBoardServlet")
public class SearchComboBoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SearchComboBoardServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("게시글 검색 서블릿 도착 여부");
		
		String searchKeyword = request.getParameter("searchKeyword");
		String boardOrderCondition = request.getParameter("boardOrderCondition");
		
		// 클라이언트에서 보내는 페이지 번호와 데이터 수
		int boardPageNumber = Integer.parseInt(request.getParameter("boardPageNumber"));
		int boardContentCount = Integer.parseInt(request.getParameter("boardContentCount"));

		System.out.println("검색 키워드 [" + searchKeyword + "]");
		System.out.println("더보기 횟수 [" + boardPageNumber + "]");
		System.out.println("보여줄 데이터 수 [" + boardContentCount + "]");

		// 시작 인덱스
		int startIndex = (boardPageNumber - 1) * boardContentCount;
		System.out.println("시작 인덱스 번호 [" + startIndex + "]");

		BoardComboDTO boardComboDTO = new BoardComboDTO();
		BoardComboDAO boardComboDAO = new BoardComboDAO();

		if("ORDERUPTODATE".equals(boardOrderCondition)) {
			boardComboDTO.setCondition("SELECTALLCOMBOBOARDSEARCHDESC");
		}
		else if("ORDEROLD".equals(boardOrderCondition)) {
			boardComboDTO.setCondition("SELECTALLCOMBOBOARDSEARCHASC");

		}
		else if("ORDERPOPULAR".equals(boardOrderCondition)) {
			boardComboDTO.setCondition("SELECTALLCOMBOBOARDSEARCHPOPULAR");
		}
		
		boardComboDTO.setSearchKeyword(searchKeyword);
		boardComboDTO.setBoardComboIndex(startIndex);
		boardComboDTO.setBoardComboContentCount(boardContentCount);
		
		ArrayList<BoardComboDTO> boardComboDatas = boardComboDAO.selectAll(boardComboDTO);
		if (boardComboDatas == null) {
			boardComboDatas = new ArrayList<>();
        }
		
		
		System.out.println("게시글 검색 로그 [" + boardComboDatas + "]");

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
			
			System.out.println("내보내는 데이터 [" +boardComboObject+ "]");
			
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
