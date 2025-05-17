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

@WebServlet("/OrderProductServlet")
public class OrderProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public OrderProductServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// CU,GS용 store
		String store = null;
		String orderCondition = null;
		String category = null;
		int pageNumber = 0;
		int contentCount = 0;

		// JS에서 보내는 값에 따라 조건 확인
		
		// CU상품을 요청한다면
		if (request.getParameter("CUProductPageNumber") != null) {
			store = "CU";
			pageNumber = Integer.parseInt(request.getParameter("CUProductPageNumber"));
			contentCount = Integer.parseInt(request.getParameter("CUProductContentCount"));
			orderCondition = request.getParameter("CUProductOrderCondition");
			category = request.getParameter("CUProductCategory");
		} else if (request.getParameter("GSProductPageNumber") != null) {
			store = "GS25";
			pageNumber = Integer.parseInt(request.getParameter("GSProductPageNumber"));
			contentCount = Integer.parseInt(request.getParameter("GSProductContentCount"));
			orderCondition = request.getParameter("GSProductOrderCondition");
			category = request.getParameter("GSProductCategory");
		} else if (request.getParameter("comboProductPageNumber") != null) {
			pageNumber = Integer.parseInt(request.getParameter("comboProductPageNumber"));
			contentCount = Integer.parseInt(request.getParameter("comboProductContentCount"));
			orderCondition = request.getParameter("comboProductOrderCondition");
			category = request.getParameter("comboProductCategory");
		}

		System.out.println("페이지 번호 [" + pageNumber + "]");
		System.out.println("보여줄 데이터 수 [" + contentCount + "]");
		System.out.println("정렬 조건 [" + orderCondition + "]");
		System.out.println("카테고리 [" + category + "]");
		if (store != null) {
			System.out.println("스토어 [" + store + "]");
		}
		
		int startIndex = (pageNumber - 1) * contentCount;
		System.out.println("시작 인덱스 번호 [" + startIndex + "]");

		JSONArray jsonResponse = new JSONArray();

		// CU / GS 단일 상품
		if (store != null) {
			ProductSingleDTO productSingleDTO = new ProductSingleDTO();
			ProductSingleDAO productSingleDAO = new ProductSingleDAO();

			productSingleDTO.setProductSingleStore(store);
			productSingleDTO.setStartIndex(startIndex);
			productSingleDTO.setLimitNumber(contentCount);

			if ("ORDERPOPULAR".equals(orderCondition) && "ALLPRODUCT".equals(category)) {
				productSingleDTO.setCondition("SELECTALLPOPULAR");
			} else if ("ORDERHIGHPRICES".equals(orderCondition) && "ALLPRODUCT".equals(category)) {
				productSingleDTO.setCondition("SELECTALLPRICEDESC");
			} else if ("ORDERLOWPRICES".equals(orderCondition) && "ALLPRODUCT".equals(category)) {
				productSingleDTO.setCondition("SELECTALLPRICEASC");
			} else {
				productSingleDTO.setProductSingleCategory(category);
				if ("ORDERPOPULAR".equals(orderCondition)) {
					productSingleDTO.setCondition("SELECTALLCATEGORYPOPULAR");
				} else if ("ORDERHIGHPRICES".equals(orderCondition)) {
					productSingleDTO.setCondition("SELECTALLCATEGORYPRICEDESC");
				} else if ("ORDERLOWPRICES".equals(orderCondition)) {
					productSingleDTO.setCondition("SELECTALLCATEGORYPRICEASC");
				}
			}

			ArrayList<ProductSingleDTO> productList = productSingleDAO.selectAll(productSingleDTO);
			for (ProductSingleDTO product : productList) {
				JSONObject productObject = new JSONObject();
				productObject.put("productSingleNumber", product.getProductSingleNumber());
				productObject.put("productSingleImage", product.getProductSingleImage());
				productObject.put("productSingleName", product.getProductSingleName());
				productObject.put("productSinglePrice", product.getProductSinglePrice());
				productObject.put("totalCountNumber", product.getTotalCountNumber());
				jsonResponse.add(productObject);
			}
		}

		// 콤보 상품
		else {
			ProductComboDTO productComboDTO = new ProductComboDTO();
			ProductComboDAO productComboDAO = new ProductComboDAO();

			productComboDTO.setProductComboIndex(startIndex);
			productComboDTO.setProductComboContentCount(contentCount);

			if ("ORDERPOPULAR".equals(orderCondition) && "ALLPRODUCT".equals(category)) {
				productComboDTO.setCondition("SELECTALLPOPULAR");
			} else if ("ORDERHIGHPRICES".equals(orderCondition) && "ALLPRODUCT".equals(category)) {
				productComboDTO.setCondition("SELECTALLPRICEDESC");
			} else if ("ORDERLOWPRICES".equals(orderCondition) && "ALLPRODUCT".equals(category)) {
				productComboDTO.setCondition("SELECTALLPRICEASC");
			} else {
				if ("MDPRODUCT".equals(category)) {
					productComboDTO.setProductComboCategory("MD");
				} else if ("CELEBRITYPRODUCT".equals(category)) {
					productComboDTO.setProductComboCategory("INFLUENCER");
				} else if ("COSTEFFECTIVENESSPRODUCT".equals(category)) {
					productComboDTO.setProductComboCategory("CHEAP");
				}

				if ("ORDERPOPULAR".equals(orderCondition)) {
					productComboDTO.setCondition("SELECTALLCATEGORYPOPULAR");
				} else if ("ORDERHIGHPRICES".equals(orderCondition)) {
					productComboDTO.setCondition("SELECTALLCATEGORYDESC");
				} else if ("ORDERLOWPRICES".equals(orderCondition)) {
					productComboDTO.setCondition("SELECTALLCATEGORYASC");
				}
			}

			ArrayList<ProductComboDTO> productList = productComboDAO.selectAll(productComboDTO);
			for (ProductComboDTO product : productList) {
				JSONObject productObject = new JSONObject();
				productObject.put("productComboNumber", product.getProductComboNumber());
				productObject.put("productComboImage", product.getProductComboImage());
				productObject.put("productComboName", product.getProductComboName());
				productObject.put("productComboPrice", product.getProductComboPrice());
				productObject.put("totalCountNumber", product.getTotalCountNumber());
				jsonResponse.add(productObject);
			}
		}

		// 응답 전송
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
