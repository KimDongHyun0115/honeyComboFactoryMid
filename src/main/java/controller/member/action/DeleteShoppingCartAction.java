package controller.member.action;

import java.util.ArrayList;

import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.dao.ProductComboDAO;
import model.dao.ProductSingleDAO;
import model.dto.ProductComboDTO;
import model.dto.ProductSingleDTO;

public class DeleteShoppingCartAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request) {

		ActionForward forward = new ActionForward();
		HttpSession session = request.getSession();

		// 장바구니 배열 생성
		ArrayList<Object> shoppingCart = (ArrayList<Object>) session.getAttribute("shoppingCart");
		
		// 조합상품 또는 개별상품 번호 받아오기
		int productSingleNumber = Integer.parseInt(request.getParameter("productSingleNumber"));
		int productComboNumber = Integer.parseInt(request.getParameter("productComboNumber"));

		System.out.println("장바구니에 담을 개별 상품 번호 [" + productSingleNumber + "]");
		System.out.println("장바구니에 담을 조합 상품 번호 [" + productComboNumber + "]");

		// 개별상품
		ProductSingleDTO productSingleDTO = new ProductSingleDTO();
		ProductSingleDAO productSingleDAO = new ProductSingleDAO();

		// 조합상품
		ProductComboDTO productComboDTO = new ProductComboDTO();
		ProductComboDAO productComboDAO = new ProductComboDAO();

		// 삭제 여부 확인
		boolean removedProduct = false;

		// 장바구니에서 삭제
		for (Object cartProduct : shoppingCart) {
			// 개별 상품인 경우
			if (cartProduct instanceof ProductSingleDTO && productSingleNumber != 0) {
				ProductSingleDTO productSingle = (ProductSingleDTO) cartProduct;
				if (productSingle.getProductSingleNumber() == productSingleNumber) {
					shoppingCart.remove(cartProduct);
					removedProduct = true;
					break;
				}
			}

			// 조합 상품인 경우
			else if (cartProduct instanceof ProductComboDTO && productComboNumber != 0) {
				ProductComboDTO productCombo = (ProductComboDTO) cartProduct;
				if (productCombo.getProductComboNumber() == productComboNumber) {
					shoppingCart.remove(cartProduct);
					removedProduct = true;
					break;
				}
			}
		}
		
		// 장바구니 데이터 다시 저장
		session.setAttribute("shoppingCart", shoppingCart);

		System.out.println("장바구니 출력 로그");
		System.out.println(shoppingCart);

		// 삭제되면 장바구니로 이동
		if(removedProduct) {
			forward.setPath("cart.jsp");
			forward.setRedirect(false);
		}
		else {
			forward.setPath("cart.jsp");
			forward.setRedirect(false);
		}
		
		
		return forward;
		

	}

}
