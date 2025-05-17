package controller.board.combo.async;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.BoardComboDAO;
import model.dto.BoardComboDTO;

import java.io.IOException;

@WebServlet("/MemberUpdateBoardServlet")
public class MemberUpdateBoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MemberUpdateBoardServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 게시글 번호
		long boardComboNumber = Long.parseLong(request.getParameter("boardComboNumber"));

		// 게시글 제목
		String boardComboTitle = request.getParameter("boardComboTitle");
		
		// 게시글 내용
		String boardComboContent = request.getParameter("boardComboContent");		
		
		System.out.println("수정할 게시글 번호 [" + boardComboNumber + "]");
		System.out.println("수정할 게시글 제목 [" + boardComboTitle + "]");
		System.out.println("수정할 게시글 내용 [" + boardComboContent + "]");
		
		BoardComboDTO boardComboDTO = new BoardComboDTO();
		BoardComboDAO boardComboDAO = new BoardComboDAO();

		boardComboDTO.setBoardComboNumber(boardComboNumber);
		boardComboDTO.setBoardComboTitle(boardComboTitle);
		boardComboDTO.setBoardComboContent(boardComboContent);
		
		boolean flag = boardComboDAO.update(boardComboDTO);

		System.out.println("수정 여부 [" + flag + "]");

		if (flag) {
			System.out.println("게시글 수정 성공");
			flag = true;
		} else {
			System.out.println("게시글 수정 실패");
			flag = false;
		}

		// 응답으로 좋아요 여부(true/false) 반환
		response.setContentType("text/plain");
		response.getWriter().write(Boolean.toString(flag));

	}

}
