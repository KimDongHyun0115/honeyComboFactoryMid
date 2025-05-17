package controller.product.common.asyn;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.ReviewDAO;
import model.dto.ReviewDTO;

@WebServlet("/ProductDetailUpdateReviewServlet")
public class ProductDetailUpdateReviewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ProductDetailUpdateReviewServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		long reviewNumber = Long.parseLong(request.getParameter("reviewNumber"));
		int reviewScore = Integer.parseInt(request.getParameter("reviewScore"));
		String reviewContent = request.getParameter("reviewContent");

		System.out.println("수정할 리뷰 번호 : [" + reviewNumber + "]");
		System.out.println("수정할 리뷰 별점 수 : [" + reviewScore + "]");
		System.out.println("수정할 리뷰 내용 : [" + reviewContent + "]");

		ReviewDTO reviewDTO = new ReviewDTO();
		ReviewDAO reviewDAO = new ReviewDAO();

		// 리뷰 수정용 파서 세팅
		reviewDTO.setReviewNumber(reviewNumber);
		reviewDTO.setReviewScore(reviewScore);
		reviewDTO.setReviewContent(reviewContent);

		// 리뷰 수정 성공 여부
		boolean flag = reviewDAO.update(reviewDTO);
		System.out.println("리뷰 수정 성공 여부 : [" + flag + "]");

		// 응답으로 JSON 반환
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		if (flag) { // 리뷰 수정 성공이라면
			System.out.println("리뷰 수정 성공");
			out.print("true");
		} else { // 리뷰 수정 실패라면
			System.out.println("리뷰 수정 실패");
			out.print("false");
		}
	}

}
