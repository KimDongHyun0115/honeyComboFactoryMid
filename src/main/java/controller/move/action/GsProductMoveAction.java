package controller.move.action;

import java.util.ArrayList;

import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import model.dao.ProductSingleDAO;
import model.dto.ProductSingleDTO;

public class GsProductMoveAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request) {
		ActionForward forward = new ActionForward();

		// GS상품 개별판매 페이지 이동 Action
		// GS상품 SingleDAO 오면 컨디션 수정

		// GS개별 상품 전체 목록 페이지로 보내기
		ProductSingleDTO productSingleDTO = new ProductSingleDTO();
		ProductSingleDAO productSingleDAO = new ProductSingleDAO();

		productSingleDTO.setProductSingleStore("GS25");

		// Hot Issues
		// 페이지 값 받기
		int hotPage = 1;
		if (request.getParameter("hotGSProductPageNumber") != null) {
			hotPage = Integer.parseInt(request.getParameter("hotGSProductPageNumber"));
		}
		System.out.println("페이지 번호 로그 [" + hotPage + "]");

		// 보여줄 내역 수
		int hotPerPage = 3;
		if(request.getParameter("hotGSProductContentCount")!=null) {
			hotPerPage = Integer.parseInt(request.getParameter("hotGSProductContentCount"));
		}
		
		int hotStartIndex = (hotPage - 1) * hotPerPage;
		System.out.println("시작 인덱스 번호 [" + hotStartIndex + "]");

		productSingleDTO.setCondition("SELECTALL_1");
		productSingleDTO.setProductSingleCategory("HOTISSUE");
		productSingleDTO.setStartIndex(hotStartIndex);
		productSingleDTO.setLimitNumber(hotPerPage);

		ArrayList<ProductSingleDTO> gsHotIssueList = productSingleDAO.selectAll(productSingleDTO);
		request.setAttribute("hotGSProductDatas", gsHotIssueList);

		// +1 증정상품
		
		// 페이지 값 받기
		int plusPage = 1;
		if(request.getParameter("plusGSProductPageNumber") != null) {
			plusPage = Integer.parseInt(request.getParameter("plusGSProductPageNumber"));
		}
		// 보여줄 내역 수
		int plusPerPage = 3;
		if(request.getParameter("plusGSProductContentCount") != null) {
			plusPerPage =Integer.parseInt(request.getParameter("plusGSProductContentCount"));
		}
		int plusStartIndex = (plusPage - 1) * plusPerPage;
		
		productSingleDTO.setCondition("SELECTALL_1");
		productSingleDTO.setProductSingleCategory("PLUSPRODUCT");
		productSingleDTO.setStartIndex(plusStartIndex);
		productSingleDTO.setLimitNumber(plusPerPage);

		ArrayList<ProductSingleDTO> gsPlusOneList = productSingleDAO.selectAll(productSingleDTO);
		request.setAttribute("plusGSProductDatas", gsPlusOneList);

		// GPS API
		// 추가할 예정

		// 인기순 정렬

		// 페이지 수 받기
		int page = 1;
		if (request.getParameter("GSProductPageNumber") != null) {
			page = Integer.parseInt(request.getParameter("GSProductPageNumber"));
		}
		System.out.println("페이지 번호 로그 [" + page + "]");

		// 페이지당 보여줄 데이터 수 설정
		int productsPerPage = 6;
		if(request.getParameter("GSProductContentCount") != null) {
			productsPerPage = Integer.parseInt(request.getParameter("GSProductContentCount"));
		}
		System.out.println("보여줄 상품 수 [" + productsPerPage + "]");

		// 시작 인덱스
		int startIndex = (page - 1) * productsPerPage;
		System.out.println("시작 인덱스 번호 [" + startIndex + "]");

		// 페이지네이션에 필요한 값 전달
		productSingleDTO.setCondition("SELECTALL_3");
		productSingleDTO.setStartIndex(startIndex);
		productSingleDTO.setLimitNumber(productsPerPage);

		ArrayList<ProductSingleDTO> productSingleGsList = productSingleDAO.selectAll(productSingleDTO);
		request.setAttribute("GSProductDatas", productSingleGsList);

		System.out.println("GS 상품 출력 로그");
		System.out.println(productSingleGsList);

		forward.setPath("GSProduct.jsp");
		forward.setRedirect(false);

		return forward;
	}
}
