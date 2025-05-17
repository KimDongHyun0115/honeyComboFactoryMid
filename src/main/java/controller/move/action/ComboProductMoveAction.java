package controller.move.action;

import java.util.ArrayList;

import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import model.dao.ProductComboDAO;
import model.dto.ProductComboDTO;

public class ComboProductMoveAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request) {
		ActionForward forward = new ActionForward();

		// 조합상품 판매 페이지 이동 Action

		// 조합상품 전체 목록 페이지로 보내기
		ProductComboDTO productComboDTO = new ProductComboDTO();
		ProductComboDAO productComboDAO = new ProductComboDAO();

		// MD Pick
		int mdPerPage = 3;
		int mdStartIndex = 0;
		

		productComboDTO.setCondition("SELECTALLMD");
		System.out.println("정렬 조건 (MD) [" +productComboDTO.getCondition()+"]");
		
		productComboDTO.setProductComboIndex(mdStartIndex);
		productComboDTO.setProductComboContentCount(mdPerPage);
		ArrayList<ProductComboDTO> mdPickList = productComboDAO.selectAll(productComboDTO);
		request.setAttribute("MDproductComboTop", mdPickList);

		// BEST ISSUES
		
		int hotPerPage = 3;
		int hotStartIndex = 0;		
		
		productComboDTO.setCondition("SELECTALLHOTISSUE");
		System.out.println("정렬 조건 (HOT) [" +productComboDTO.getCondition()+"]");

		productComboDTO.setProductComboIndex(hotStartIndex);
		productComboDTO.setProductComboContentCount(hotPerPage);
		ArrayList<ProductComboDTO> hotIssueList = productComboDAO.selectAll(productComboDTO);
		request.setAttribute("hotProductComboTop", hotIssueList);

		// GPS API
		// 추가할 예정

		forward.setPath("comboProduct.jsp");
		forward.setRedirect(false);

		return forward;
	}
}
