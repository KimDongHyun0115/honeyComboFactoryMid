package controller.move.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.dao.MemberDAO;
import model.dao.ProductComboDAO;
import model.dao.ProductSingleDAO;
import model.dto.MemberDTO;
import model.dto.ProductComboDTO;
import model.dto.ProductSingleDTO;

public class ShoppingCartMoveAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request) {
		ActionForward forward = new ActionForward();

		HttpSession session = request.getSession();
		ArrayList<Map<String, Object>> shoppingCart = (ArrayList<Map<String, Object>>) session.getAttribute("shoppingCart");

		ProductSingleDAO productSingleDAO = new ProductSingleDAO();
		ProductComboDAO productComboDAO = new ProductComboDAO();

		MemberDTO memberDTO = new MemberDTO();
		MemberDAO memberDAO = new MemberDAO();

		if (session.getAttribute("loginedMemberNumber") == null) {
	        forward.setPath("login.jsp");
	        forward.setRedirect(true);
	        return forward;
	    }
		
		System.out.println("형변환 전 멤버 번호 [" +session.getAttribute("loginedMemberNumber") +"]");
		
		long memberNumber = Long.parseLong(session.getAttribute("loginedMemberNumber").toString());
				
		System.out.println("형변환 후 멤버 번호 ["+memberNumber+"]");
		
		memberDTO.setCondition("SELECTONEMYINFORMATION");
		memberDTO.setMemberNumber(memberNumber);
		memberDTO = memberDAO.selectOne(memberDTO);

		if (memberDTO != null) {
			request.setAttribute("memberAddressDatas", memberDTO);
			
			System.out.println("보낼 주소 ["+memberDTO+"]");
		}

		if (shoppingCart == null) {
			forward.setPath("cart.jsp");
			forward.setRedirect(false);

			return forward;
		}

		ArrayList<Map<String, Object>> cartList = new ArrayList<>();

		for (Map<String, Object> cartItem : shoppingCart) {
			int productNumber = (int) cartItem.get("productNumber");
			int cartProductCount = (int) cartItem.get("cartProductCount");
			boolean isComboProduct = (boolean) cartItem.get("isComboProduct");
			
			Map<String, Object> productMap = new HashMap<>();

			if (isComboProduct) {
				ProductComboDTO productCombo = new ProductComboDTO();
				productCombo.setCondition("SELECTONE");
				productCombo.setProductComboNumber(productNumber);
				productCombo = productComboDAO.selectOne(productCombo);
				if (productCombo != null) {
					productMap.put("cartProductName", productCombo.getProductComboName());
					productMap.put("cartProductImage", productCombo.getProductComboImage());
					productMap.put("cartProductStock", productCombo.getProductComboStock());
					productMap.put("cartProductNumber", productCombo.getProductComboNumber());
					productMap.put("cartProductPrice", productCombo.getProductComboPrice());
					productMap.put("cartProductCount", cartProductCount);
				}
			} else {
				ProductSingleDTO productSingle = new ProductSingleDTO();
				productSingle.setCondition("SELECTONE_9");
				productSingle.setProductSingleNumber(productNumber);
				productSingle = productSingleDAO.selectOne(productSingle);
				if (productSingle != null) {
					productMap.put("cartProductName", productSingle.getProductSingleName());
					productMap.put("cartProductImage", productSingle.getProductSingleImage());
					productMap.put("cartProductStock", productSingle.getProductSingleStock());
					productMap.put("cartProductNumber", productSingle.getProductSingleNumber());
					productMap.put("cartProductPrice", productSingle.getProductSinglePrice());
					productMap.put("cartProductCount", cartProductCount);
				}
			}

			cartList.add(productMap);
		}

		request.setAttribute("cartProductDatas", cartList);
		forward.setPath("cart.jsp");
		forward.setRedirect(false);

		return forward;
	}

}
