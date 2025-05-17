package controller.member.async;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dto.ProductComboDTO;
import model.dto.ProductSingleDTO;

@WebServlet("/DeleteCartProductServlet")
public class DeleteCartProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public DeleteCartProductServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doPost(request,response);
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/plain; charset=UTF-8");
		PrintWriter out = response.getWriter();

		HttpSession session = request.getSession();
		ArrayList<Map<String, Object>> shoppingCart = (ArrayList<Map<String, Object>>) session
				.getAttribute("shoppingCart");
		if (shoppingCart == null) {
			shoppingCart = new ArrayList<>();
			session.setAttribute("shoppingCart", shoppingCart);
		}

		System.out.println(request.getParameter("cartProductNumberDatas"));
		
		// 배열로 보낸 값을 String 으로 저장
		String cartProductNumberDatas = request.getParameter("cartProductNumberDatas");
		// 만약 보낸 값이 null 이거나 isEmpty가 아니라면
		if (cartProductNumberDatas != null && !cartProductNumberDatas.isEmpty()) {
			// + 기준으로 상품 번호 분리
		    String[] productNumbers = cartProductNumberDatas.split("\\+");
		    // 번호를 저장할 Integer List 생성
		    ArrayList<Integer> productNumberList = new ArrayList<>();
		    // 번호를 하나씩 돌리면서
		    for (String productNumber : productNumbers) {
		        try {
		        	// 생성한 List 에 저장
		            productNumberList.add(Integer.parseInt(productNumber.trim())); // 문자열 → 정수 변환
		        } catch (NumberFormatException e) {
		            System.err.println("잘못된 상품 번호: " + productNumber);
		        }
		    }

		    System.out.println("삭제할 상품 번호 리스트: " + productNumberList);

		    // 상품 삭제 로직 추가 (Iterator 사용)
		    Iterator<Map<String, Object>> iterator = shoppingCart.iterator();
		    while (iterator.hasNext()) {
		        Map<String, Object> cartItem = iterator.next();
		        int cartProductNumber = (int) cartItem.get("productNumber"); // 장바구니 상품 번호

		        if (productNumberList.contains(cartProductNumber)) { // 리스트에 포함된 경우 삭제
		            iterator.remove();
		            System.out.println("삭제된 상품 번호: " + cartProductNumber);
		        }
		    }
		}

		session.setAttribute("shoppingCart", shoppingCart);

		System.out.println(shoppingCart);

		out.print("true");
		out.flush();

	}
}
