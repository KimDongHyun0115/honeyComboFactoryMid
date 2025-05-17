package controller.move.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.dao.ProductComboComponentDAO;
import model.dao.ProductComboDAO;
import model.dao.ProductSingleDAO;
import model.dao.ReviewDAO;
import model.dto.ProductComboComponentDTO;
import model.dto.ProductComboDTO;
import model.dto.ProductSingleDTO;
import model.dto.ReviewDTO;

public class ProductDetailMoveAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request) {
		ActionForward forward = new ActionForward();
		HttpSession session = request.getSession();

		// JSON 데이터 생성
		JSONObject jsonResponse = new JSONObject();

		System.out.println("상품상세정보 도착 번호 [" + request.getParameter("productComboNumber") + "]");
		System.out.println("상품상세정보 도착 번호 [" + request.getParameter("productSingleNumber") + "]");

		long productComboNumber = 0;
		long productSingleNumber = 0;

		if (request.getParameter("productComboNumber") != null) {
			productComboNumber = Long.parseLong(request.getParameter("productComboNumber"));
			System.out.println("정보 보낼 조합 상품 번호 [" + productComboNumber + "]");

		} else if (request.getParameter("productSingleNumber") != null) {
			productSingleNumber = Long.parseLong(request.getParameter("productSingleNumber"));
			System.out.println("정보 보낼 개별 상품 번호 [" + productSingleNumber + "]");
		} else {
			System.out.println("상품 번호 받아오지 못함");
		}

		// 조합상품
		ProductComboDTO productComboDTO = new ProductComboDTO();
		ProductComboDAO productComboDAO = new ProductComboDAO();

		// 개별상품
		ProductSingleDTO productSingleDTO = new ProductSingleDTO();
		ProductSingleDAO productSingleDAO = new ProductSingleDAO();

		// 구성품
		ProductComboComponentDTO productComboComponentDTO = new ProductComboComponentDTO();
		ProductComboComponentDAO productComboComponentDAO = new ProductComboComponentDAO();

		// 조합상품 데이터 가져오기
		if (productComboNumber != 0) {
			productComboDTO.setProductComboNumber(productComboNumber);
			productComboDTO.setCondition("SELECTONE");
			productComboDTO = productComboDAO.selectOne(productComboDTO);

			if (productComboDTO != null) {
				Map<String, Object> productComboData = new HashMap<>();
				productComboData.put("productNumber", productComboDTO.getProductComboNumber());
				productComboData.put("productName", productComboDTO.getProductComboName());
				productComboData.put("productStock", productComboDTO.getProductComboStock());
				productComboData.put("productPrice", productComboDTO.getProductComboPrice());
				productComboData.put("productImg", productComboDTO.getProductComboImage());
				productComboData.put("productInformation", productComboDTO.getProductComboInformation());
				productComboData.put("productCategory", productComboDTO.getProductComboCategory());
				productComboData.put("isComboProduct", true);

				// request에 직접 저장
				request.setAttribute("productData", productComboData);
				System.out.println("출력될 조합 상품: " + productComboNumber);
				System.out.println("JSP로 보낼 상품 정보 [" + productComboData + "]");
			}

			// 조합상품 구성품 출력
			productComboComponentDTO.setProductComboNumber(productComboNumber);
			productComboComponentDTO.setCondition("SELECTALLCOMPONENT");
			ArrayList<ProductComboComponentDTO> productComboComponent = productComboComponentDAO
					.selectAll(productComboComponentDTO);
			request.setAttribute("productComboComponentDatas", productComboComponent);

			System.out.println("조합 상품 구성품 출력 로그");
			System.out.println(productComboComponent);

		}
		// 개별상품 데이터 가져오기
		else if (productSingleNumber != 0) {
			productSingleDTO.setCondition("SELECTONE_9");
			productSingleDTO.setProductSingleNumber(productSingleNumber);
			productSingleDTO = productSingleDAO.selectOne(productSingleDTO);

			System.out.println("개별 상품 반환: " + productSingleDTO);

			if (productSingleDTO != null) {
				Map<String, Object> productSingleData = new HashMap<>();
				productSingleData.put("productNumber", productSingleDTO.getProductSingleNumber());
				productSingleData.put("productName", productSingleDTO.getProductSingleName());
				productSingleData.put("productPrice", productSingleDTO.getProductSinglePrice());
				productSingleData.put("productImg", productSingleDTO.getProductSingleImage());
				productSingleData.put("productStock", productSingleDTO.getProductSingleStock());
				productSingleData.put("productInformation", productSingleDTO.getProductSingleInformation());
				productSingleData.put("productCategory", productSingleDTO.getProductSingleCategory());
				productSingleData.put("isComboProduct", false);

				// request에 직접 저장
				request.setAttribute("productData", productSingleData);
				System.out.println("출력될 개별 상품: " + productSingleNumber);
				System.out.println("JSP로 보낼 상품 정보 [" + productSingleData + "]");
			}
		}

		// 리뷰 목록 하단 개별상품 판매 목록
		// 개별 상품 DTO가 null이면 새 객체 생성
		if (productSingleDTO == null) {
			productSingleDTO = new ProductSingleDTO();
		}

		productSingleDTO.setCondition("SELECTALLSTOCKDESC");
		productSingleDTO.setStartIndex(0);
		productSingleDTO.setLimitNumber(3);

		ArrayList<ProductSingleDTO> productSingleList = productSingleDAO.selectAll(productSingleDTO);
		session.setAttribute("recommendProductDatas", productSingleList);

		System.out.println("추천 상품 출력 로그");
		System.out.println(productSingleList);

		// 상품 상세정보 페이지 이동 Action
		forward.setPath("productDetail.jsp");
		forward.setRedirect(false);

		return forward;
	}
}
