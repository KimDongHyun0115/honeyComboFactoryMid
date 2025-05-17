package controller.product.single.async;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.ProductSingleDAO;
import model.dto.ProductSingleDTO;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@WebServlet("/GsPlusOneProductServlet")
public class GsPlusOneProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public GsPlusOneProductServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 클라이언트에서 보내는 페이지 번호와 데이터 수
		int plusGSProductPageNumber = Integer.parseInt(request.getParameter("plusGSProductPageNumber"));
		int plusGSProductContentCount = Integer.parseInt(request.getParameter("plusGSProductContentCount"));

		System.out.println("더보기 횟수 [" + plusGSProductPageNumber + "]");
		System.out.println("보여줄 데이터 수 [" + plusGSProductContentCount + "]");

		// 시작 인덱스
		int startIndex = (plusGSProductPageNumber - 1) * plusGSProductContentCount;
		System.out.println("시작 인덱스 번호 [" + startIndex + "]");

		ProductSingleDTO productSingleDTO = new ProductSingleDTO();
		ProductSingleDAO productSingleDAO = new ProductSingleDAO();

		productSingleDTO.setCondition("SELECTALL_1");
		productSingleDTO.setProductSingleStore("GS25");
		productSingleDTO.setProductSingleCategory("PLUSPRODUCT");
		productSingleDTO.setStartIndex(startIndex);
		productSingleDTO.setLimitNumber(plusGSProductContentCount);

		ArrayList<ProductSingleDTO> GSProductDatas = productSingleDAO.selectAll(productSingleDTO);

		System.out.println("배열 저장 성공");

		// JSON 응답 생성
		JSONArray jsonResponse = new JSONArray();
		for (ProductSingleDTO product : GSProductDatas) {
			JSONObject productArray = new JSONObject();
			productArray.put("productSingleNumber", product.getProductSingleNumber());
			productArray.put("productSingleImage", product.getProductSingleImage());
			productArray.put("productSingleName", product.getProductSingleName());
			productArray.put("productSinglePrice", product.getProductSinglePrice());
			productArray.put("totalCountNumber", product.getTotalCountNumber());
			jsonResponse.add(productArray);
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		response.getWriter().print(jsonResponse.toJSONString());
		response.getWriter().flush(); // 출력 버퍼 비우기
		response.getWriter().close(); // 스트림 닫기

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
