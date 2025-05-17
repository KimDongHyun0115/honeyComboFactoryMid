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

@WebServlet("/OrderProductComboServlet")
public class OrderProductComboServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public OrderProductComboServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String comboProductOrderCondition = request.getParameter("comboProductOrderCondition");
		String comboProductCategory = request.getParameter("comboProductCategory");

		System.out.println("꿀조합 정렬 조건 [" + comboProductOrderCondition + "]");
		System.out.println("꿀조합 카테고리 조건 [" + comboProductCategory + "]");

		ProductComboDTO productComboDTO = new ProductComboDTO();
		ProductComboDAO productComboDAO = new ProductComboDAO();

		// 클라이언트에서 보내는 페이지 번호와 데이터 수
		int comboProductPageNumber = Integer.parseInt(request.getParameter("comboProductPageNumber"));
		int comboProductContentCount = Integer.parseInt(request.getParameter("comboProductContentCount"));

		System.out.println("더보기 횟수 [" + comboProductPageNumber + "]");
		System.out.println("보여줄 데이터 수 [" + comboProductContentCount + "]");

		// 시작 인덱스
		int startIndex = (comboProductPageNumber - 1) * comboProductContentCount;
		System.out.println("시작 인덱스 번호 [" + startIndex + "]");

		productComboDTO.setProductComboIndex(startIndex);
		productComboDTO.setProductComboContentCount(comboProductContentCount);
		
		
		// 모든 상품 목록 정렬
		if ("ORDERPOPULAR".equals(comboProductOrderCondition) && "ALLPRODUCT".equals(comboProductCategory)) {
			productComboDTO.setCondition("SELECTALLPOPULAR");
		} else if ("ORDERHIGHPRICES".equals(comboProductOrderCondition) && "ALLPRODUCT".equals(comboProductCategory)) {
			productComboDTO.setCondition("SELECTALLPRICEDESC");
		} else if ("ORDERLOWPRICES".equals(comboProductOrderCondition) && "ALLPRODUCT".equals(comboProductCategory)) {
			productComboDTO.setCondition("SELECTALLPRICEASC");
		} else if ("MDPRODUCT".equals(comboProductCategory)) {
			productComboDTO.setProductComboCategory("MD");
			if ("ORDERPOPULAR".equals(comboProductOrderCondition)) {
				productComboDTO.setCondition("SELECTALLCATEGORYPOPULAR");
			} else if ("ORDERHIGHPRICES".equals(comboProductOrderCondition)) {
				productComboDTO.setCondition("SELECTALLCATEGORYDESC");
			} else if ("ORDERLOWPRICES".equals(comboProductOrderCondition)) {
				productComboDTO.setCondition("SELECTALLCATEGORYASC");
			}
		} else if ("CELEBRITYPRODUCT".equals(comboProductCategory)) {
			productComboDTO.setProductComboCategory("INFLUENCER");
			if ("ORDERPOPULAR".equals(comboProductOrderCondition)) {
				productComboDTO.setCondition("SELECTALLCATEGORYPOPULAR");
			} else if ("ORDERHIGHPRICES".equals(comboProductOrderCondition)) {
				productComboDTO.setCondition("SELECTALLCATEGORYDESC");
			} else if ("ORDERLOWPRICES".equals(comboProductOrderCondition)) {
				productComboDTO.setCondition("SELECTALLCATEGORYASC");
			}
		} else if ("COSTEFFECTIVENESSPRODUCT".equals(comboProductCategory)) {
			productComboDTO.setProductComboCategory("CHEAP");
			if ("ORDERPOPULAR".equals(comboProductOrderCondition)) {
				productComboDTO.setCondition("SELECTALLCATEGORYPOPULAR");
			} else if ("ORDERHIGHPRICES".equals(comboProductOrderCondition)) {
				productComboDTO.setCondition("SELECTALLCATEGORYDESC");
			} else if ("ORDERLOWPRICES".equals(comboProductOrderCondition)) {
				productComboDTO.setCondition("SELECTALLCATEGORYASC");
			}
		}
		
		ArrayList<ProductComboDTO> comboProductDatas = productComboDAO.selectAll(productComboDTO);

		System.out.println("꿀조합 저장된 배열");
		System.out.println(comboProductDatas);
		
		// JSON 응답 생성
        JSONArray jsonResponse = new JSONArray();
        for (ProductComboDTO product : comboProductDatas) {
            JSONObject productArray = new JSONObject();
            productArray.put("productComboNumber", product.getProductComboNumber());
            productArray.put("productComboImage", product.getProductComboImage());
            productArray.put("productComboName", product.getProductComboName());
            productArray.put("productComboPrice", product.getProductComboPrice());
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
