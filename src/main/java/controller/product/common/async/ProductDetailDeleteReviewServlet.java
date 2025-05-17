package controller.product.common.async;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.ReviewDAO;
import model.dto.ReviewDTO;

@WebServlet("/ProductDetailDeleteReviewServlet")
public class ProductDetailDeleteReviewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ProductDetailDeleteReviewServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		long reviewNumber = Long.parseLong(request.getParameter("reviewNumber"));
		System.out.println("받은 삭제할 리뷰 번호 : ["+reviewNumber+"]");
		
		ReviewDTO reviewDTO = new ReviewDTO();
		ReviewDAO reviewDAO = new ReviewDAO();
		
		// 리뷰 삭제용 파서 세팅
		reviewDTO.setReviewNumber(reviewNumber);

		// 리뷰 삭제 성공 여부
		boolean flag = reviewDAO.delete(reviewDTO);
		System.out.println("리뷰 삭제 성공 여부 : [" + flag + "]");

		// 응답으로 JSON 반환
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		if (flag) { // 삭제 성공 시
			System.out.println("리뷰 삭제 성공");
			out.print("true");
		} else { // 삭제 실패 시
			System.out.println("리뷰 삭제 실패");
			out.print("false");
		}

	}

}
