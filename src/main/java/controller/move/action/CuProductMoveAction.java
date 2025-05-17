package controller.move.action;

import java.util.ArrayList;

import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import model.dao.ProductSingleDAO;
import model.dto.ProductSingleDTO;

public class CuProductMoveAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request) {
		ActionForward forward = new ActionForward();

		// 개별상품 판매 페이지 이동 Action
		// CU상품 SingleDAO 오면 컨디션 수정

		// CU개별 상품 전체 목록 페이지로 보내기
		ProductSingleDTO productSingleDTO = new ProductSingleDTO();
		ProductSingleDAO productSingleDAO = new ProductSingleDAO();
		
		productSingleDTO.setProductSingleStore("CU");
		
		// Hot Issues
		// 페이지 값 받기
		int hotPage = 1;
		if(request.getParameter("hotCUProductPageNumber") != null) {
			hotPage = Integer.parseInt(request.getParameter("hotCUProductPageNumber"));
		}
		System.out.println("페이지 번호 로그 [" +hotPage+ "]");
		
		// 보여줄 내역 수
		int hotPerPage = 3;
		if(request.getParameter("hotCUProductContentCount") != null) {
			hotPerPage = Integer.parseInt(request.getParameter("hotCUProductContentCount"));
		}
		
		int hotStartIndex = (hotPage -1 ) * hotPerPage;
		System.out.println("시작 인덱스 번호 [" +hotStartIndex+ "]");
		
		productSingleDTO.setCondition("SELECTALL_1");
		productSingleDTO.setProductSingleCategory("HOTISSUE");
		productSingleDTO.setStartIndex(hotStartIndex);
		productSingleDTO.setLimitNumber(hotPerPage);
		
		ArrayList<ProductSingleDTO> cuHotIssueList = productSingleDAO.selectAll(productSingleDTO);
		request.setAttribute("hotCUProductDatas", cuHotIssueList);
		
		System.out.println("핫이슈 상품 출력");
		System.out.println(cuHotIssueList);

		// +1 증정상품
		
		// 페이지 번호 받기
		int plusPage = 1;
		if(request.getParameter("plusCUProductPageNumber") != null) {
			plusPage = Integer.parseInt(request.getParameter("plusCUProductPageNumber"));
		}
		// 보여줄 내역 수
		int plusPerPage = 3;
		if(request.getParameter("plusCUProductContentCount") != null) {
			plusPerPage = Integer.parseInt(request.getParameter("plusCUProductContentCount"));
		}
		
		int plusStartIndex = (plusPage - 1) * plusPerPage;
		
		productSingleDTO.setCondition("SELECTALL_1");
		productSingleDTO.setProductSingleCategory("PLUSPRODUCT");
		productSingleDTO.setStartIndex(plusStartIndex);
		productSingleDTO.setLimitNumber(plusPerPage);
		ArrayList<ProductSingleDTO> cuPlusOneList = productSingleDAO.selectAll(productSingleDTO);
		request.setAttribute("plusCUProductDatas", cuPlusOneList);
		
		System.out.println("PLUS 상품 출력");
		System.out.println(cuPlusOneList);

		// GPS API
		// 추가할 예정

		// 인기순 정렬
		
		// 페이지 수 받기
		int page = 1;
		if(request.getParameter("CUProductPageNumber") != null) {
			page = Integer.parseInt(request.getParameter("CUProductPageNumber"));
		}
		System.out.println("페이지 번호 로그 [" +page+ "]");
		
		// 페이지당 보여줄 데이터 수 설정
		int productsPerPage = 6;
		if(request.getParameter("CUProductContentCount")!= null) {
			productsPerPage = Integer.parseInt(request.getParameter("CUProductContentCount"));
		}
		
		System.out.println("보여줄 상품 수 [" +productsPerPage+ "]");
		
		// 시작 인덱스
		int startIndex = (page - 1) * productsPerPage;
		System.out.println("시작 인덱스 번호 [" +startIndex+ "]");
		
		// 페이지네이션에 필요한 값 전달
		productSingleDTO.setCondition("SELECTALL_3");
		productSingleDTO.setStartIndex(startIndex);
		productSingleDTO.setLimitNumber(productsPerPage);
		
		ArrayList<ProductSingleDTO> productSingleCuList = productSingleDAO.selectAll(productSingleDTO);
		request.setAttribute("CUProductDatas", productSingleCuList);

		System.out.println("CU 상품 출력 로그");
		System.out.println(productSingleCuList);

		forward.setPath("CUProduct.jsp");
		forward.setRedirect(false);

		return forward;
	}
}
