package controller.move.action;

import java.util.ArrayList;

import controller.common.Action;
import controller.common.ActionForward;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import model.dao.BoardComboDAO;
import model.dao.ProductComboDAO;
import model.dao.ProductSingleDAO;
import model.dto.BoardComboDTO;
import model.dto.ProductComboDTO;
import model.dto.ProductSingleDTO;


public class MainMoveAction implements Action {

		@Override
		public ActionForward execute(HttpServletRequest request) {
			ActionForward forward = new ActionForward();
			HttpSession session = request.getSession();

			System.out.println("COMMAND 도착 여부");
			
			
			// 메인화면 이동 페이지
			// 꿀조합 TOP 각 편의점 꺼 수정하기
			// 정렬 조건 SingleDAO 오면 수정하기
			
			ProductComboDAO productComboDAO = new ProductComboDAO();
			ProductComboDTO productComboDTO = new ProductComboDTO();
			ProductSingleDAO productSingleDAO = new ProductSingleDAO();
			ProductSingleDTO productSingleDTO = new ProductSingleDTO();
			BoardComboDAO boardComboDAO = new BoardComboDAO();
			BoardComboDTO boardComboDTO = new BoardComboDTO();
			
			// 꿀조합 Top3 출력
			productComboDTO.setCondition("SELECTALLPOPULAR");
			productComboDTO.setProductComboIndex(1);
			productComboDTO.setProductComboContentCount(3);

			ArrayList<ProductComboDTO> comboList = productComboDAO.selectAll(productComboDTO);
			request.setAttribute("allStoreProductComboTop", comboList);

			System.out.println(comboList);
			System.out.println("메인 꿀조합 top3 로그");
			
			// CU 꿀조합 Top3 출력
			productComboDTO.setCondition("SELECTALLPRICEDESC");
			productComboDTO.setProductComboIndex(1);
			productComboDTO.setProductComboContentCount(3);
			ArrayList<ProductComboDTO> cuList = productComboDAO.selectAll(productComboDTO);
			request.setAttribute("CUStoreProductComboTop", cuList);

			System.out.println(cuList);
			System.out.println("메인 cu 꿀조합 top3 로그");
			
			// GS 꿀조합 Top3 출력
			productComboDTO.setCondition("SELECTALLPRICEDESC");
			productComboDTO.setProductComboIndex(1);
			productComboDTO.setProductComboContentCount(3);
			ArrayList<ProductComboDTO> gsList = productComboDAO.selectAll(productComboDTO);
			request.setAttribute("GSStoreProductComboTop", gsList);

			System.out.println(gsList);
			System.out.println("메인 gs25 꿀조합 top3 로그");
			
			// 꿀조합 게시판 글 출력
			boardComboDTO.setCondition("SELECTALLMEMBERCONTENTPOPULAR");
			boardComboDTO.setBoardComboIndex(0);
			boardComboDTO.setBoardComboContentCount(3);
			ArrayList<BoardComboDTO> boardComboList = boardComboDAO.selectAll(boardComboDTO);
			request.setAttribute("boardComboPopularTop", boardComboList);
			
			// MDPICK 상품 출력 (1개, 광고용)
			productComboDTO.setCondition("SELECTONEADVERTISEMENT");
			productComboDTO.setProductComboADNumber(34001);
			ProductComboDTO mdPick = productComboDAO.selectOne(productComboDTO);
			request.setAttribute("MDRecommendProductData", mdPick);
			
			
			forward.setPath("main.jsp");
			forward.setRedirect(false);

			return forward;
		}
	}

