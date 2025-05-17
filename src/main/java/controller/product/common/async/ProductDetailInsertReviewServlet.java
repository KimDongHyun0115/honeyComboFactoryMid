package controller.product.common.async;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.json.simple.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.ReviewDAO;
import model.dto.ReviewDTO;

@WebServlet("/ProductDetailInsertReviewServlet")
public class ProductDetailInsertReviewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ProductDetailInsertReviewServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();

		long productComboNumber = Long.parseLong(request.getParameter("productNumber"));
		long memberNumber = Long.parseLong(session.getAttribute("loginedMemberNumber").toString());
		int reviewScore = Integer.parseInt(request.getParameter("reviewScore"));
		String reviewContent = request.getParameter("reviewContent");

		System.out.println("리뷰 저장할 상품 번호 : [" + productComboNumber + "]");
		System.out.println("리뷰 저장할 회원 번호 : [" + memberNumber + "]");
		System.out.println("리뷰 저장할 별점 수 : [" + reviewScore + "]");
		System.out.println("리뷰 저장할 내용 : [" + reviewContent + "]");

		ReviewDAO reviewDAO = new ReviewDAO();
		ReviewDTO reviewDTO = new ReviewDTO();

		// 중복 검사용 파서 세팅
		reviewDTO.setCondition("SELECTONEREVIEW");
		reviewDTO.setMemberNumber(memberNumber);
		reviewDTO.setProductComboNumber(productComboNumber);

		// 중복 리뷰 작성 검사
		ReviewDTO isReviewDTO = reviewDAO.selectOne(reviewDTO);
		System.out.println("받은 중복 리뷰 작성 검사 정보 : [" + isReviewDTO + "]");

		// 응답으로 JSON 반환
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		
		// jsonObj 인스턴스화
		JSONObject rivewData = new JSONObject();
		// 중복 리뷰 작성이라면
		if (isReviewDTO != null) {
			System.out.println("리뷰 중복 작성 - '0' 반환");
			rivewData.put("rivewData", 0);
			out.println(rivewData);
			return;
		}

		// 리뷰 저장용 파서 세팅
		// 회원번호, 상품 번호는 위에서 세팅해둠
		reviewDTO.setReviewScore(reviewScore);
		reviewDTO.setReviewContent(reviewContent);

		// 리뷰 저장 성공 여부
		boolean flag = reviewDAO.insert(reviewDTO);
		System.out.println("리뷰 저장 성공 여부 [" + flag + "]");

		if (!flag) { // 리뷰 저장 실패라면
			System.out.println("리뷰 저장 실패 - '1' 반환");
			rivewData.put("rivewData", 1);
			out.println(rivewData);
			return;
		}

		// 리뷰 저장 정보 받아오기
		reviewDTO = reviewDAO.selectOne(reviewDTO);
		System.out.println("받은 리뷰 저장 정보 : [" + reviewDTO + "]");

		rivewData.put("reviewNumber", reviewDTO.getReviewNumber());
		rivewData.put("reviewScore", reviewDTO.getReviewScore());
		rivewData.put("reviewRegisterDate", reviewDTO.getReviewRegisterDate().toString());
		rivewData.put("reviewContent", reviewDTO.getReviewContent());
		rivewData.put("memberName", reviewDTO.getMemberName());
		System.out.println("반환할 jsonObj 리뷰 데이터 : [" + rivewData + "]");

		// jsonObj 리뷰 데이터 반환
		out.print(rivewData);

	}

}
