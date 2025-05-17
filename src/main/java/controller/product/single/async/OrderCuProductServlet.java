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

@WebServlet("/OrderCuProductServlet")
public class OrderCuProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public OrderCuProductServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String CUProductOrderCondition = request.getParameter("CUProductOrderCondition");
		String CUProductCategory = request.getParameter("CUProductCategory");

		System.out.println("정렬 조건[" + CUProductOrderCondition + "]");
		System.out.println("카테고리 조건 [" + CUProductCategory + "]");

		ProductSingleDTO productSingleDTO = new ProductSingleDTO();
		ProductSingleDAO productSingleDAO = new ProductSingleDAO();
		
		// 모든 상품
		// 클라이언트에서 보내는 페이지 번호와 데이터 수
		int CUProductPageNumber = Integer.parseInt(request.getParameter("CUProductPageNumber"));
		int CUProductContentCount = Integer.parseInt(request.getParameter("CUProductContentCount"));

		System.out.println("더보기 횟수 [" + CUProductPageNumber + "]");
		System.out.println("보여줄 데이터 수 [" + CUProductContentCount + "]");

		// 시작 인덱스
		int startIndex = (CUProductPageNumber - 1) * CUProductContentCount;
		System.out.println("시작 인덱스 번호 [" + startIndex + "]");

		productSingleDTO.setProductSingleStore("CU");
		productSingleDTO.setStartIndex(startIndex);
		productSingleDTO.setLimitNumber(CUProductContentCount);

		// 모든 상품 목록 정렬
		if ("ORDERPOPULAR".equals(CUProductOrderCondition) && "ALLPRODUCT".equals(CUProductCategory)) {
		    productSingleDTO.setCondition("SELECTALL_3");
		} else if ("ORDERHIGHPRICES".equals(CUProductOrderCondition) && "ALLPRODUCT".equals(CUProductCategory)) {
		    productSingleDTO.setCondition("SELECTALL_4");
		} else if ("ORDERLOWPRICES".equals(CUProductOrderCondition) && "ALLPRODUCT".equals(CUProductCategory)) {
		    productSingleDTO.setCondition("SELECTALL_5");
		} else if ("DAILYSUPPLIESPRODUCT".equals(CUProductCategory)) {
		    productSingleDTO.setProductSingleCategory("DAILYSUPPLIESPRODUCT");
		    if ("ORDERPOPULAR".equals(CUProductOrderCondition)) {
		        productSingleDTO.setCondition("SELECTALL_6");
		    } else if ("ORDERHIGHPRICES".equals(CUProductOrderCondition)) {
		        productSingleDTO.setCondition("SELECTALL_7");
		    } else if ("ORDERLOWPRICES".equals(CUProductOrderCondition)) {
		        productSingleDTO.setCondition("SELECTALL_8");
		    }
		} else if ("BEVERAGEPRODUCT".equals(CUProductCategory)) {
		    productSingleDTO.setProductSingleCategory("BEVERAGEPRODUCT");
		    if ("ORDERPOPULAR".equals(CUProductOrderCondition)) {
		        productSingleDTO.setCondition("SELECTALL_6");
		    } else if ("ORDERHIGHPRICES".equals(CUProductOrderCondition)) {
		        productSingleDTO.setCondition("SELECTALL_7");
		    } else if ("ORDERLOWPRICES".equals(CUProductOrderCondition)) {
		        productSingleDTO.setCondition("SELECTALL_8");
		    }
		} else if ("FOODPRODUCT".equals(CUProductCategory)) {
		    productSingleDTO.setProductSingleCategory("FOODPRODUCT");
		    if ("ORDERPOPULAR".equals(CUProductOrderCondition)) {
		        productSingleDTO.setCondition("SELECTALL_6");
		    } else if ("ORDERHIGHPRICES".equals(CUProductOrderCondition)) {
		        productSingleDTO.setCondition("SELECTALL_7");
		    } else if ("ORDERLOWPRICES".equals(CUProductOrderCondition)) {
		        productSingleDTO.setCondition("SELECTALL_8");
		    }
		}
		
		ArrayList<ProductSingleDTO> CUProductDatas = productSingleDAO.selectAll(productSingleDTO);

		System.out.println("전체 상품 배열 저장 성공");
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
	}

}
