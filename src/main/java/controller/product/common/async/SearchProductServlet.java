package controller.product.common.async;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.ProductComboDAO;
import model.dao.ProductSingleDAO;
import model.dto.ProductComboDTO;
import model.dto.ProductSingleDTO;

@WebServlet("/SearchProductServlet")
public class SearchProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public SearchProductServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String store = null; // CU, GS25 (콤보 NULL)
		String orderCondition = null;
		String searchKeyword = null;
		int pageNumber = 0;
		int contentCount = 0;

		// CU 검색
		if (request.getParameter("CUProductPageNumber") != null) {
			store = "CU";
			pageNumber = Integer.parseInt(request.getParameter("CUProductPageNumber"));
			contentCount = Integer.parseInt(request.getParameter("CUProductContentCount"));
			orderCondition = request.getParameter("CUProductOrderCondition");
			searchKeyword = request.getParameter("searchKeyword");
		}
		// GS 검색
		else if (request.getParameter("GSProductPageNumber") != null) {
			store = "GS25";
			pageNumber = Integer.parseInt(request.getParameter("GSProductPageNumber"));
			contentCount = Integer.parseInt(request.getParameter("GSProductContentCount"));
			orderCondition = request.getParameter("GSProductOrderCondition");
			searchKeyword = request.getParameter("searchKeyword");
		}
		// 콤보 검색
		else if (request.getParameter("comboProductPageNumber") != null) {
			pageNumber = Integer.parseInt(request.getParameter("comboProductPageNumber"));
			contentCount = Integer.parseInt(request.getParameter("comboProductContentCount"));
			orderCondition = request.getParameter("comboProductOrderCondition");
			searchKeyword = request.getParameter("searchKeyword");
		}
		
		System.out.println("페이지 번호 [" + pageNumber + "]");
		System.out.println("보여줄 데이터 수 [" + contentCount + "]");
		System.out.println("정렬 조건 [" + orderCondition + "]");
		if (store != null) {
			System.out.println("스토어 [" + store + "]");
		}
		
		int startIndex = (pageNumber - 1) * contentCount;

		JSONArray jsonResponse = new JSONArray();

		if (store != null) {
			// CU / GS 단일 상품 검색 처리
			ProductSingleDTO productSingleDTO = new ProductSingleDTO();
			ProductSingleDAO productSingleDAO = new ProductSingleDAO();
			
			if("ORDERPOPULAR".equals(orderCondition)) {
				productSingleDTO.setCondition("SELECTALLSEARCHPOPULAR");
			}
			else if("ORDERHIGHPRICES".equals(orderCondition)){
				productSingleDTO.setCondition("SELECTALLSEARCHPRICEDESC");
			}
			else if("ORDERLOWPRICES".equals(orderCondition)){
				productSingleDTO.setCondition("SELECTALLSEARCHPRICEASC");
			}

			productSingleDTO.setProductSingleStore(store);
			productSingleDTO.setStartIndex(startIndex);
			productSingleDTO.setLimitNumber(contentCount);
			productSingleDTO.setSearchKeyword(searchKeyword);

			ArrayList<ProductSingleDTO> productList = productSingleDAO.selectAll(productSingleDTO);

			for (ProductSingleDTO product : productList) {
				JSONObject productArray = new JSONObject();
				productArray.put("productSingleNumber", product.getProductSingleNumber());
				productArray.put("productSingleImage", product.getProductSingleImage());
				productArray.put("productSingleName", product.getProductSingleName());
				productArray.put("productSinglePrice", product.getProductSinglePrice());
				productArray.put("totalCountNumber", product.getTotalCountNumber());
				jsonResponse.add(productArray);
			}
		}else {
			// 콤보 상품 검색 처리
			ProductComboDTO productComboDTO = new ProductComboDTO();
			ProductComboDAO productComboDAO = new ProductComboDAO();
			
			if("ORDERPOPULAR".equals(orderCondition)) {
				productComboDTO.setCondition("SELECTALLSEARCHPOPULAR");
			}
			else if("ORDERHIGHPRICES".equals(orderCondition)){
				productComboDTO.setCondition("SELECTALLSEARCHDESC");
			}
			else if("ORDERLOWPRICES".equals(orderCondition)){
				productComboDTO.setCondition("SELECTALLSEARCHASC");
			}

			productComboDTO.setProductComboIndex(startIndex);
			productComboDTO.setProductComboContentCount(contentCount);
			productComboDTO.setSearchKeyword(searchKeyword);

			ArrayList<ProductComboDTO> productList = productComboDAO.selectAll(productComboDTO);

			for (ProductComboDTO product : productList) {
				JSONObject productArray = new JSONObject();
				productArray.put("productComboNumber", product.getProductComboNumber());
				productArray.put("productComboImage", product.getProductComboImage());
				productArray.put("productComboName", product.getProductComboName());
				productArray.put("productComboPrice", product.getProductComboPrice());
				productArray.put("totalCountNumber", product.getTotalCountNumber());
				jsonResponse.add(productArray);
			}
		}
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(jsonResponse.toJSONString());
		response.getWriter().flush();
		response.getWriter().close();
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
