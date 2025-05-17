package controller.product.combo.async;

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
import model.dto.ProductComboDTO;

@WebServlet("/SearchComboProductServlet")
public class SearchComboProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public SearchComboProductServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String comboProductOrderCondition = request.getParameter("comboProductOrderCondition");
		String searchKeyword = request.getParameter("searchKeyword");

		ProductComboDTO productComboDTO = new ProductComboDTO();
		ProductComboDAO productComboDAO = new ProductComboDAO();
		
		// 클라이언트에서 보내는 페이지 번호와 데이터 수
		int comboProductPageNumber = Integer.parseInt(request.getParameter("comboProductPageNumber"));
		int comboProductContentCount = Integer.parseInt(request.getParameter("comboProductContentCount"));

		System.out.println("검색 키워드 [" +searchKeyword+ "]");
		System.out.println("더보기 횟수 [" + comboProductPageNumber + "]");
		System.out.println("보여줄 데이터 수 [" + comboProductContentCount + "]");

		// 시작 인덱스
		int startIndex = (comboProductPageNumber - 1) * comboProductContentCount;
		System.out.println("시작 인덱스 번호 [" + startIndex + "]");

		
		if("ORDERPOPULAR".equals(comboProductOrderCondition)) {
			productComboDTO.setCondition("SELECTALLSEARCHPOPULAR");
		}
		else if("ORDERHIGHPRICES".equals(comboProductOrderCondition)){
			productComboDTO.setCondition("SELECTALLSEARCHDESC");
		}
		else if("ORDERLOWPRICES".equals(comboProductOrderCondition)){
			productComboDTO.setCondition("SELECTALLSEARCHASC");
		}
	
		productComboDTO.setSearchKeyword(searchKeyword);
		productComboDTO.setProductComboIndex(startIndex);
		productComboDTO.setProductComboContentCount(comboProductContentCount);

		ArrayList<ProductComboDTO> ComboProductDatas = productComboDAO.selectAll(productComboDTO);

		System.out.println("배열 저장 성공");

		// JSON 응답 생성
		JSONArray jsonResponse = new JSONArray();
		for (ProductComboDTO product : ComboProductDatas) {
			JSONObject productArray = new JSONObject();
			productArray.put("productComboNumber", product.getProductComboNumber());
			productArray.put("productComboImage", product.getProductComboImage());
			productArray.put("productComboName", product.getProductComboName());
			productArray.put("productComboPrice", product.getProductComboPrice());
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
