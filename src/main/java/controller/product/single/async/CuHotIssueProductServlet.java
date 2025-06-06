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

@WebServlet("/CuHotIssueProductServlet")
public class CuHotIssueProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CuHotIssueProductServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 클라이언트에서 보내는 페이지 번호와 데이터 수
		int hotCUProductPageNumber = Integer.parseInt(request.getParameter("hotCUProductPageNumber"));
		int hotCUProductContentCount = Integer.parseInt(request.getParameter("hotCUProductContentCount"));

		System.out.println("더보기 횟수 [" + hotCUProductPageNumber + "]");
		System.out.println("보여줄 데이터 수 [" + hotCUProductContentCount + "]");

		// 시작 인덱스
		int startIndex = (hotCUProductPageNumber - 1) * hotCUProductContentCount;
		System.out.println("시작 인덱스 번호 [" + startIndex + "]");

		ProductSingleDTO productSingleDTO = new ProductSingleDTO();
		ProductSingleDAO productSingleDAO = new ProductSingleDAO();

		productSingleDTO.setCondition("SELECTALL_1");
		productSingleDTO.setProductSingleStore("CU");
		productSingleDTO.setProductSingleCategory("HOTISSUE");
		productSingleDTO.setStartIndex(startIndex);
		productSingleDTO.setLimitNumber(hotCUProductContentCount);

		ArrayList<ProductSingleDTO> CUProductDatas = productSingleDAO.selectAll(productSingleDTO);

		System.out.println("핫이슈 배열 저장 성공");
		System.out.println(CUProductDatas);

		// JSON 응답 생성
		JSONArray jsonResponse = new JSONArray();
		for (ProductSingleDTO product : CUProductDatas) {
			JSONObject productArray = new JSONObject();
			productArray.put("productSingleNumber", product.getProductSingleNumber());
			productArray.put("productSingleImage", product.getProductSingleImage());
			productArray.put("productSingleName", product.getProductSingleName());
			productArray.put("productSinglePrice", product.getProductSinglePrice());
			productArray.put("totalCountNumber", product.getTotalCountNumber());
			
			System.out.println("데이터 수 [" +product.getTotalCountNumber()+ "]");
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
