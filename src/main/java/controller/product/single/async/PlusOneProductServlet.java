package controller.product.single.async;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.ProductSingleDAO;
import model.dto.ProductSingleDTO;

@WebServlet("/PlusOneProductServlet")
public class PlusOneProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public PlusOneProductServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String store = null;
		int pageNumber = 0;
		int contentCount = 0;

		if (request.getParameter("plusCUProductPageNumber") != null) {
			store = "CU";
			// 클라이언트에서 보내는 페이지 번호와 데이터 수
			pageNumber = Integer.parseInt(request.getParameter("plusCUProductPageNumber"));
			contentCount = Integer.parseInt(request.getParameter("plusCUProductContentCount"));
		} else if (request.getParameter("plusGSProductPageNumber") != null) {
			store = "GS25";
			// 클라이언트에서 보내는 페이지 번호와 데이터 수
			pageNumber = Integer.parseInt(request.getParameter("plusGSProductPageNumber"));
			contentCount = Integer.parseInt(request.getParameter("plusGSProductContentCount"));
		}
		System.out.println("브랜드 [" + store + "]");
		System.out.println("더보기 횟수 [" + pageNumber + "]");
		System.out.println("보여줄 데이터 수 [" + contentCount + "]");

		int startIndex = (pageNumber - 1) * contentCount;
		System.out.println("시작 인덱스 번호 [" + startIndex + "]");

		ProductSingleDTO productSingleDTO = new ProductSingleDTO();
		ProductSingleDAO productSingleDAO = new ProductSingleDAO();

		productSingleDTO.setCondition("SELECTALLCATEGORY");
		productSingleDTO.setProductSingleStore(store);
		productSingleDTO.setProductSingleCategory("PLUSPRODUCT");
		productSingleDTO.setStartIndex(startIndex);
		productSingleDTO.setLimitNumber(contentCount);

		ArrayList<ProductSingleDTO> productList = productSingleDAO.selectAll(productSingleDTO);

		System.out.println("+1 증정 배열 저장 성공");
		System.out.println(productList);

		JSONArray jsonResponse = new JSONArray();
		for (ProductSingleDTO product : productList) {
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
		response.getWriter().flush();
		response.getWriter().close();

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
