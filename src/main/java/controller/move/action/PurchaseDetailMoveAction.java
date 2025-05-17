package controller.move.action;

import java.util.ArrayList;

import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.dao.MemberDAO;
import model.dao.PurchaseDAO;
import model.dao.PurchaseDetailDAO;
import model.dto.MemberDTO;
import model.dto.PurchaseDTO;
import model.dto.PurchaseDetailDTO;

public class PurchaseDetailMoveAction implements Action {
	
	@Override
	public ActionForward execute(HttpServletRequest request) {
		ActionForward forward = new ActionForward();
		HttpSession session = request.getSession();
		
		// 주문상세 페이지 이동 Action
		
		System.out.println("주문 상세 페이지 이동 Action 도착");
		
		// 회원 번호 받아오기
		long memberNumber = (long)session.getAttribute("loginedMemberNumber");
		
		System.out.println("주문 상세 정보를 볼 회원 번호 [" +memberNumber+ "]");
		
		// 회원 주소 정보 가져오기
		MemberDAO memberDAO = new MemberDAO();
		MemberDTO memberDTO = new MemberDTO();
			
		memberDTO.setCondition("SELECTONEMYINFORMATION");
		memberDTO.setMemberNumber(memberNumber); 
		memberDTO = memberDAO.selectOne(memberDTO);

		session.setAttribute("loginedMemberNumber", memberDTO.getMemberNumber()); 
		if (memberDTO != null) {
			request.setAttribute("memberAddressDatas", memberDTO); 
			
			System.out.println("보낼 주소 ["+memberDTO+"]");
		}
		
		long purchaseNumber = Long.parseLong(request.getParameter("purchaseNumber"));

		// 주문 정보 출력
		PurchaseDAO purchaseDAO = new PurchaseDAO();
		PurchaseDTO purchaseDTO = new PurchaseDTO();
		purchaseDTO.setPurchaseNumber(purchaseNumber);
		purchaseDTO = purchaseDAO.selectOne(purchaseDTO);
		
		request.setAttribute("purchaseData", purchaseDTO);
		
		System.out.println("주문 정보 출력 [" +purchaseDTO+"]");
		
		// 주문 상세정보 출력
		
		PurchaseDetailDAO purchaseDetailDAO = new PurchaseDetailDAO();
		PurchaseDetailDTO purchaseDetailDTO = new PurchaseDetailDTO();
		
		purchaseDetailDTO.setPurchaseNumber(purchaseNumber);
		purchaseDetailDTO.setMemberNumber(memberNumber); 
		ArrayList<PurchaseDetailDTO> purchaseProductDetailDatas = purchaseDetailDAO.selectAll(purchaseDetailDTO);
		
		request.setAttribute("purchaseProductDetailDatas", purchaseProductDetailDatas);
		
		System.out.println("주문내역 상세정보 출력 ["+purchaseProductDetailDatas+"]");
		
		forward.setPath("purchaseDetail.jsp");
		forward.setRedirect(false);
		
		System.out.println("이동 경로 ["+forward.getPath()+"]");
		
		return forward;
	}
}
