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

public class AddShoppingCartAction implements Action {
	
	@Override
	public ActionForward execute(HttpServletRequest request) {
		
		ActionForward forward = new ActionForward();
		HttpSession session = request.getSession();
		
		// Object로 선언하여 장바구니 배열 생성
		ArrayList<Object> shoppingCart = (ArrayList<Object>)session.getAttribute("shoppingCart");
		
		if(shoppingCart == null) {
			shoppingCart = new ArrayList<Object>();
			session.setAttribute("shoppingCart", shoppingCart);
		}
		
		int productSingleNumber = Integer.parseInt(request.getParameter("productSingleNumber"));
		int productComboNumber = Integer.parseInt(request.getParameter("productComboNumber"));
		
		System.out.println("장바구니에 담을 개별 상품 번호 [" + productSingleNumber + "]" );
		System.out.println("장바구니에 담을 조합 상품 번호 [" + productComboNumber + "]");
		
		// 개별상품
		ProductSingleDTO productSingleDTO = new ProductSingleDTO();
		ProductSingleDAO productSingleDAO = new ProductSingleDAO();
		
		// 조합상품
		ProductComboDTO productComboDTO = new ProductComboDTO();
		ProductComboDAO productComboDAO = new ProductComboDAO();
		
		// 개별상품 선택
		productSingleDTO.setProductSingleNumber(productSingleNumber);
		productSingleDTO = productSingleDAO.selectOne(productSingleDTO);
		
		System.out.println("장바구니에 담긴 개별 상품 정보 [" + productSingleDTO + "]");		

		// 조합상품 선택
		productComboDTO.setProductComboNumber(productComboNumber);
		productComboDTO = productComboDAO.selectOne(productComboDTO);
		
		System.out.println("장바구니에 담긴 조합 상품 정보 [" + productComboDTO + "]");
		
		// 이미 담긴 상품인지 확인 여부
		boolean alreadyIn = false;
		
		for(Object cartProduct : shoppingCart) {
			// 개별상품이 이미 장바구니에 담겨 있다면
			if(cartProduct instanceof ProductSingleDTO && productSingleDTO != null) {
				ProductSingleDTO alreadyInProductSingle = (ProductSingleDTO) cartProduct;
				if(alreadyInProductSingle.getProductSingleNumber() == productSingleDTO.getProductSingleNumber()) {
					alreadyInProductSingle.setProductSingleCount(alreadyInProductSingle.getProductSingleCount() + 1);
					alreadyIn = true;
					
					System.out.println("저장 여부 [" +alreadyIn + "]");
					
					break;
				}
			}
			
			// 조합상품이 이미 장바구니에 담겨 있다면 
			else if(cartProduct instanceof ProductComboDTO && productComboDTO != null) {
				ProductComboDTO alreadyInProductCombo = (ProductComboDTO) cartProduct;
				if(alreadyInProductCombo.getProductComboNumber() == productComboDTO.getProductComboNumber()) {
					alreadyInProductCombo.setProductComboCount(alreadyInProductCombo.getProductComboCount() + 1);
					alreadyIn = true;
					
					System.out.println("저장 여부 [" +alreadyIn + "]");

					break;
				}
			}	
		}
		
		// 만약 장바구니에 추가되지 않은 상품이라면
		if(!alreadyIn) {
			if(productSingleDTO != null && productComboDTO == null) {
				shoppingCart.add(productSingleDTO);
				
				System.out.println("저장 여부 [" +alreadyIn + "]");
				
			}
			else if(productSingleDTO == null && productComboDTO != null) {
				shoppingCart.add(productComboDTO);
				
				System.out.println("저장 여부 [" +alreadyIn + "]");
				
			}
		}
		
		session.setAttribute("cartProductDatas", shoppingCart);

		System.out.println("장바구니 출력 로그");
		System.out.println(shoppingCart);
		
		forward.setPath("alert.jsp");
		forward.setRedirect(false);
		
		return forward;
	}
}	
	/*	장바구니에 상품 담기
	 	>> 장바구니 세션에 저장
	 	
	 	상품 DTO
	 	상품 DAO
	 	
	 	꿀조합 DTO
	 	꿀조합 DAO
	 	
	 	개별상품 장바구니 배열 생성
	 	꿀조합상품 장바구니 배열 생성
	 	
	 	만약 장바구니가 null 이라면
	 		장바구니는 새 배열
	 		세션에 장바구니명, 장바구니로 저장
	 	
	 	해당 상품 번호 파라미터로 받아오기
	 
	 	DTO에 해당 상품 번호 넣기
	 	DAO selectOne으로 집어 넣기
	 	
	 	이미 담겨있는지 기본 false로 생성
	 	
	 	반복문을 사용해서
	 		만약 장바구니에 있는 상품의 번호 추가하려는 상품과 비교해서 일치하면
	 			장바구니의 수량에 +1을 하고
	 			이미 담겨있는지 true로 변경
	 			탈출
	 		
	 	만약 이미 들어가있다면
	 		이미 들어가있다고 출력
	 		다시 해당 상품 페이지로 돌아감
	 	만약 안들어가있다면
	 		장바구니에 해당 상품 추
	 		
	 		
	 		
	 	제네릭을 object로 써서
	 	인스턴스 오브로
	 	
	 	
	 	
	 	
	 
	 
	 */

