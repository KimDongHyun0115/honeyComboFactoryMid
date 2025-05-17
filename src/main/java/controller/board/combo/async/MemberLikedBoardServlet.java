package controller.board.combo.async;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.BoardComboDAO;
import model.dao.BoardComboLikedDAO;
import model.dto.BoardComboDTO;
import model.dto.BoardComboLikedDTO;

@WebServlet("/MemberLikedBoardServlet")
public class MemberLikedBoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MemberLikedBoardServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		
		System.out.println("게시글 좋아요 서블릿 도착 여부");
		
		// 게시글 번호
		long boardComboNumber = Long.parseLong(request.getParameter("boardComboNumber"));
		// 로그인된 회원번호
		long memberNumber = Long.parseLong(session.getAttribute("loginedMemberNumber").toString());
		// 좋아요 여부
		String orderCondition = request.getParameter("likedCondition");

		System.out.println("받은 게시글 번호"+boardComboNumber+"]");
		System.out.println("받은 회원 번호 [" +memberNumber+"]");
		System.out.println("정렬 조건 ["+orderCondition+"]");
		
		BoardComboLikedDTO boardComboLikedDTO = new BoardComboLikedDTO();
		BoardComboLikedDAO boardComboLikedDAO = new BoardComboLikedDAO();

		boardComboLikedDTO.setMemberNumber(memberNumber);
		boardComboLikedDTO.setBoardComboNumber(boardComboNumber);
			   
		BoardComboDTO boardComboDTO = new BoardComboDTO();
		BoardComboDAO boardComboDAO = new BoardComboDAO();
		
		boardComboDTO.setCondition("SELECTONECOMBOBOARD");
		boardComboDTO.setBoardComboNumber(boardComboNumber);

		boardComboDTO = boardComboDAO.selectOne(boardComboDTO);
		
		boolean isLiked = false;
		
		int boardComboLikedCount = (int)boardComboDTO.getBoardComboLikedCount();

		System.out.println("받은 좋아요 수 ["+boardComboLikedCount+"]");
		
		if("INSERTLIKED".equals(orderCondition)) {
			isLiked = boardComboLikedDAO.insert(boardComboLikedDTO);
			boardComboLikedCount++;
			System.out.println("수정된 좋아요 수 [" +boardComboLikedCount+ "]");
			
			System.out.println(isLiked ? "좋아요 등록 성공" : "좋아요 등록 실패");
			
		}
		else if(("DELETELIKED").equals(orderCondition)){
			isLiked = boardComboLikedDAO.delete(boardComboLikedDTO);
			boardComboLikedCount--;
			System.out.println("수정된 좋아요 수 [" +boardComboLikedCount+ "]");


			System.out.println(isLiked ? "좋아요 취소 성공" : "좋아요 취소 실패");
		}
		
		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		
		if(isLiked) {
		response.getWriter().write(String.valueOf(boardComboLikedCount));
		}
		else {
			response.getWriter().write("false");
		}
	}

}
