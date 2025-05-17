package controller.board.combo.async;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.BoardComboDAO;
import model.dto.BoardComboDTO;

@WebServlet("/MemberInsertBoardServlet")
public class MemberInsertBoardServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MemberInsertBoardServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		String memberName = session.getAttribute("memberName").toString();
		String boardComboTitle = request.getParameter("boardComboTitle");
		String boardComboContent = request.getParameter("boardComboContent");

		System.out.println("글 작성 회원 이름 [" + memberName + "]");
		System.out.println("글 제목 [" + boardComboTitle + "]");
		System.out.println("글 내용 [" + boardComboContent + "]");

		BoardComboDTO boardComboDTO = new BoardComboDTO();
		BoardComboDAO boardComboDAO = new BoardComboDAO();

		boardComboDTO.setBoardComboTitle(boardComboTitle);
		boardComboDTO.setBoardComboContent(boardComboContent);
		boardComboDTO.setMemberName(memberName);
		boolean flag = boardComboDAO.insert(boardComboDTO);

		if (flag) {
			System.out.println("글 작성 성공");
			flag = true;
		} else {
			System.out.println("글 작성 실패");
			flag = false;
		}

		// 응답으로 좋아요 여부(true/false) 반환
		response.setContentType("text/plain");
		response.getWriter().write(Boolean.toString(flag));

	}

}
