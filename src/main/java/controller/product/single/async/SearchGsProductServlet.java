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

@WebServlet("/SearchGsProductServlet")
public class SearchGsProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SearchGsProductServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("검색 서블릿 도착 여부");
		System.out.println("서블릿으로 넘어온 검색어 ["+request.getParameter("searchKeyword")+"]");
		System.out.println("서블릿으로 넘어온 페이지 번호 ["+request.getParameter("GSProductPageNumber")+"]");
		System.out.println("서블릿으로 넘어온 데이터 수 [" +request.getParameter("GSProductContentCount")+ "]");
		
		String GSProductOrderCondition = request.getParameter("GSProductOrderCondition");
		System.out.println("정렬 조건[" + GSProductOrderCondition + "]");
		
		String searchKeyword = request.getParameter("searchKeyword");
		int GSProductPageNumber = Integer.parseInt(request.getParameter("GSProductPageNumber"));
		int GSProductContentCount = Integer.parseInt(request.getParameter("GSProductContentCount"));

		System.out.println("검색 키워드: [" + searchKeyword + "]");
		System.out.println("페이지 번호: [" + GSProductPageNumber + "]");
		System.out.println("보여줄 데이터 수: [" + GSProductContentCount + "]");

		int startIndex = (GSProductPageNumber - 1) * GSProductContentCount;
		System.out.println("시작 인덱스 번호: [" + startIndex + "]");

		ProductSingleDTO productSingleDTO = new ProductSingleDTO();
		ProductSingleDAO productSingleDAO = new ProductSingleDAO();

		if("ORDERPOPULAR".equals(GSProductOrderCondition)) {
			productSingleDTO.setCondition("SELECTALL_2");
		}
		else if("ORDERHIGHPRICES".equals(GSProductOrderCondition)){
			productSingleDTO.setCondition("SELECTALL_10");
		}
		else if("ORDERLOWPRICES".equals(GSProductOrderCondition)){
			productSingleDTO.setCondition("SELECTALL_11");
		}
		
		productSingleDTO.setProductSingleStore("GS25");
		productSingleDTO.setSearchKeyword(searchKeyword);
		productSingleDTO.setStartIndex(startIndex);
		productSingleDTO.setLimitNumber(GSProductContentCount);

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
		response.getWriter().flush();
		response.getWriter().close();

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
