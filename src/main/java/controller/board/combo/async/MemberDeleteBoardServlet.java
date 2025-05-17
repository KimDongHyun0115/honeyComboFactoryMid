package controller.board.combo.async;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.BoardComboDAO;
import model.dto.BoardComboDTO;

@WebServlet("/MemberDeleteBoardServlet")
public class MemberDeleteBoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MemberDeleteBoardServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 게시글 번호
		long boardComboNumber = Long.parseLong(request.getParameter("boardComboNumber"));
		
		System.out.println("삭제할 게시글 번호 ["+boardComboNumber+"]");
		
		BoardComboDTO boardComboDTO = new BoardComboDTO();
		BoardComboDAO boardComboDAO = new BoardComboDAO();

		boardComboDTO.setBoardComboNumber(boardComboNumber);
		boolean flag = boardComboDAO.delete(boardComboDTO);

		System.out.println("삭제 여부 ["+flag+"]");
		
		if (flag) {
			System.out.println("게시글 삭제 성공");
			flag = true;
		} else {
			System.out.println("게시글 삭제 실패");
			flag = false;
		}

		// 응답으로 좋아요 여부(true/false) 반환
		response.setContentType("text/plain");
		response.getWriter().write(Boolean.toString(flag));
	}

}
