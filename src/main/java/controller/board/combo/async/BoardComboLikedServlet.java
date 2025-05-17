package controller.board.combo.async;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.BoardComboLikedDAO;
import model.dto.BoardComboLikedDTO;

@WebServlet("/BoardComboLikedServlet")
public class BoardComboLikedServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public BoardComboLikedServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		System.out.println("좋아요 여부 판별 서블릿 도착함");
		
		
		HttpSession session = request.getSession();
		
		// 게시글 번호
		long boardComboNumber = Long.parseLong(request.getParameter("boardComboNumber"));
		// 로그인된 회원번호
		long memberNumber = (long)session.getAttribute("loginedMemberNumber");

		BoardComboLikedDTO boardComboLikedDTO = new BoardComboLikedDTO();
		BoardComboLikedDAO boardComboLikedDAO = new BoardComboLikedDAO();

		boolean isLiked = false;

		boardComboLikedDTO.setBoardComboNumber(boardComboNumber);
		boardComboLikedDTO.setMemberNumber(memberNumber);
		boardComboLikedDTO = boardComboLikedDAO.selectOne(boardComboLikedDTO);

		if (boardComboLikedDTO != null) {
			isLiked = true;
		} else {
			isLiked = false;
		}
		
		// 응답으로 좋아요 여부(true/false) 반환
		response.setContentType("text/plain");
		response.getWriter().write(Boolean.toString(isLiked));
	}

}
