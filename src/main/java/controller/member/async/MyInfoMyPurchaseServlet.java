package controller.member.async;

import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.PurchaseDAO;
import model.dto.PurchaseDTO;

@WebServlet("/MyInfoMyPurchaseServlet")
public class MyInfoMyPurchaseServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MyInfoMyPurchaseServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("내 구매내역 목록 서블릿 도착");
		
		HttpSession session = request.getSession();
		
		// 주문 내역
		PurchaseDTO purchaseDTO = new PurchaseDTO();
		PurchaseDAO purchaseDAO = new PurchaseDAO();

		long memberNumber = (long) session.getAttribute("loginedMemberNumber");

		System.out.println("주문상세 회원번호 [" + memberNumber + "]");

		// 페이지 값 받기
		int purchasePageNumber = 1;
		if (request.getParameter("purchasePageNumber") != null) {
			purchasePageNumber = Integer.parseInt(request.getParameter("purchasePageNumber"));
		}
		System.out.println("페이지 번호 로그 [" + purchasePageNumber + "]");

		// 보여줄 내역 수
		int purchaseContentCount = 3;
		if (request.getParameter("purchaseContentCount") != null) {
			purchaseContentCount = Integer.parseInt(request.getParameter("purchaseContentCount"));
		}

		System.out.println("내역 수 로그 [" + purchaseContentCount + "]");

		// 시작 인덱스
		int purchaseStartIndex = (purchasePageNumber - 1) * purchaseContentCount;
		System.out.println("주문내역 시작 인덱스 번호 [" + purchaseStartIndex + "]");

		// 주문 페이지네이션에 필요한 값 전달
		purchaseDTO.setCondition("SELECTALLONEPERSON");
		purchaseDTO.setMemberNumber(memberNumber);
		purchaseDTO.setPurchaseIndex(purchaseStartIndex);
		purchaseDTO.setPurchaseContentCount(purchaseContentCount);
		ArrayList<PurchaseDTO> purchaseList = purchaseDAO.selectAll(purchaseDTO);
		if (purchaseList == null) {
			purchaseList = new ArrayList<>();
        }

		System.out.println("주문내역 로그 [" + purchaseList + "]");

		// JSON 변환
		JSONArray purchaseArray = new JSONArray();
		for (PurchaseDTO purchase : purchaseList) {
			JSONObject purchaseObject = new JSONObject();

			// JSONObject는 Map이므로 타입을 명시적으로 설정
			purchaseObject.put("purchaseNumber", purchase.getPurchaseNumber());
			purchaseObject.put("purchaseTotalPrice", purchase.getPurchaseTotalPrice());
			purchaseObject.put("totalCountNumber", purchase.getTotalCountNumber());
			purchaseObject.put("purchaseTerminalId", purchase.getPurchaseTerminalId());

			purchaseArray.add(purchaseObject);
		}

		// 응답 설정 및 전송
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(purchaseArray.toJSONString());
	}

}
