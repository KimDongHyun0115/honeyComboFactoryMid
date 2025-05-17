package controller.product.common.asyn;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.ReviewDAO;
import model.dto.ReviewDTO;

@WebServlet("/ProductDetailReviewServlet")
public class ProductDetailReviewServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ProductDetailReviewServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		
		System.out.println("상품 상세 리뷰 불러오기 서블릿");
		
		// 클라이언트로부터 전달받은 상품 번호
		System.out.println("조합 상품 번호 [" + request.getParameter("productNumber") + "]");
		long productNumber = Long.parseLong(request.getParameter("productNumber").toString());

		// 클라이언트에서 보내는 페이지 번호와 데이터 수
		int reviewPageNumber = Integer.parseInt(request.getParameter("reviewPageNumber"));
		int reviewContentCount = Integer.parseInt(request.getParameter("reviewContentCount"));

		System.out.println("더보기 횟수 [" + reviewPageNumber + "]");
		System.out.println("보여줄 데이터 수 [" + reviewContentCount + "]");

		// 시작 인덱스 계산
		int startIndex = (reviewPageNumber - 1) * reviewContentCount;
		System.out.println("시작 인덱스 번호 [" + startIndex + "]");

		// 리뷰 조회를 위한 DTO 설정
		ReviewDTO reviewDTO = new ReviewDTO();
		reviewDTO.setCondition("SELECTALLREVIEWONEPROUDCT");
		reviewDTO.setProductComboNumber(productNumber);
		reviewDTO.setReviewIndex(startIndex);
		reviewDTO.setReviewContentCount(reviewContentCount);

		// 리뷰 목록 가져오기
		ReviewDAO reviewDAO = new ReviewDAO();
		ArrayList<ReviewDTO> reviewList = reviewDAO.selectAll(reviewDTO);
		
		System.out.println("볼 리뷰 내용 ["+reviewList+"]");
		JSONObject responseJson = new JSONObject();

		// 리뷰 데이터를 JSON 배열로 변환
		JSONArray reviewArray = new JSONArray();
		if (reviewList != null) {
			for (ReviewDTO review : reviewList) {
				JSONObject reviewObject = new JSONObject();
				reviewObject.put("reviewNumber", review.getReviewNumber());
				reviewObject.put("reviewScore", review.getReviewScore());
				reviewObject.put("reviewRegisterDate", review.getReviewRegisterDate().toString());
				reviewObject.put("reviewContent", review.getReviewContent());
				reviewObject.put("memberNumber", review.getMemberNumber());
				reviewObject.put("memberName", review.getMemberName());
				reviewObject.put("memberIsWithdraw", review.isMemberIsWithdraw());
				reviewObject.put("totalCountNumber", review.getTotalReviewCount());
				reviewArray.add(reviewObject);
			}
		}

		// 최종 응답 JSON 생성
		responseJson.put("reviewDatas", reviewArray);

		System.out.println("리뷰 출력 로그");
		System.out.println(reviewArray);

		// 응답 설정 및 전송
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(responseJson.toJSONString());

	}

}
