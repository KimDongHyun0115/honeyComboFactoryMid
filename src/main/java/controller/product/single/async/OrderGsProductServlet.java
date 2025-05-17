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

@WebServlet("/OrderGsProductServlet")
public class OrderGsProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public OrderGsProductServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String GSProductOrderCondition = request.getParameter("GSProductOrderCondition");
		String GSProductCategory = request.getParameter("GSProductCategory");

		System.out.println("정렬 조건[" + GSProductOrderCondition + "]");
		System.out.println("카테고리 조건 [" + GSProductCategory + "]");

		ProductSingleDTO productSingleDTO = new ProductSingleDTO();
		ProductSingleDAO productSingleDAO = new ProductSingleDAO();
		
		// 모든 상품
		// 클라이언트에서 보내는 페이지 번호와 데이터 수
		int GSProductPageNumber = Integer.parseInt(request.getParameter("GSProductPageNumber"));
		int GSProductContentCount = Integer.parseInt(request.getParameter("GSProductContentCount"));

		System.out.println("더보기 횟수 [" + GSProductPageNumber + "]");
		System.out.println("보여줄 데이터 수 [" + GSProductContentCount + "]");

		// 시작 인덱스
		int startIndex = (GSProductPageNumber - 1) * GSProductContentCount;
		System.out.println("시작 인덱스 번호 [" + startIndex + "]");

		productSingleDTO.setProductSingleStore("GS25");
		productSingleDTO.setStartIndex(startIndex);
		productSingleDTO.setLimitNumber(GSProductContentCount);

		// 모든 상품 목록 정렬
		if ("ORDERPOPULAR".equals(GSProductOrderCondition) && "ALLPRODUCT".equals(GSProductCategory)) {
		    productSingleDTO.setCondition("SELECTALL_3");
		} else if ("ORDERHIGHPRICES".equals(GSProductOrderCondition) && "ALLPRODUCT".equals(GSProductCategory)) {
		    productSingleDTO.setCondition("SELECTALL_4");
		} else if ("ORDERLOWPRICES".equals(GSProductOrderCondition) && "ALLPRODUCT".equals(GSProductCategory)) {
		    productSingleDTO.setCondition("SELECTALL_5");
		} else if ("DAILYSUPPLIESPRODUCT".equals(GSProductCategory)) {
		    productSingleDTO.setProductSingleCategory("DAILYSUPPLIESPRODUCT");
		    if ("ORDERPOPULAR".equals(GSProductOrderCondition)) {
		        productSingleDTO.setCondition("SELECTALL_6");
		    } else if ("ORDERHIGHPRICES".equals(GSProductOrderCondition)) {
		        productSingleDTO.setCondition("SELECTALL_7");
		    } else if ("ORDERLOWPRICES".equals(GSProductOrderCondition)) {
		        productSingleDTO.setCondition("SELECTALL_8");
		    }
		} else if ("BEVERAGEPRODUCT".equals(GSProductCategory)) {
		    productSingleDTO.setProductSingleCategory("BEVERAGEPRODUCT");
		    if ("ORDERPOPULAR".equals(GSProductOrderCondition)) {
		        productSingleDTO.setCondition("SELECTALL_6");
		    } else if ("ORDERHIGHPRICES".equals(GSProductOrderCondition)) {
		        productSingleDTO.setCondition("SELECTALL_7");
		    } else if ("ORDERLOWPRICES".equals(GSProductOrderCondition)) {
		        productSingleDTO.setCondition("SELECTALL_8");
		    }
		} else if ("FOODPRODUCT".equals(GSProductCategory)) {
		    productSingleDTO.setProductSingleCategory("FOODPRODUCT");
		    if ("ORDERPOPULAR".equals(GSProductOrderCondition)) {
		        productSingleDTO.setCondition("SELECTALL_6");
		    } else if ("ORDERHIGHPRICES".equals(GSProductOrderCondition)) {
		        productSingleDTO.setCondition("SELECTALL_7");
		    } else if ("ORDERLOWPRICES".equals(GSProductOrderCondition)) {
		        productSingleDTO.setCondition("SELECTALL_8");
		    }
		}
		
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
			
			System.out.println("총 데이터 수 ["+product.getTotalCountNumber()+"]");
			
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
		doGet(request,response);
	}

}
