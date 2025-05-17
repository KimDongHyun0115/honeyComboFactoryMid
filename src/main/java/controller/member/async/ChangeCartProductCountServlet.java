package controller.member.async;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import org.json.simple.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dto.ProductComboDTO;
import model.dto.ProductSingleDTO;

@WebServlet("/ChangeCartProductCountServlet")
public class ChangeCartProductCountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ChangeCartProductCountServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();

		// 장바구니 가져오기
		ArrayList<Map<String, Object>> shoppingCart = (ArrayList<Map<String, Object>>) session.getAttribute("shoppingCart");
		if (shoppingCart == null) {
			shoppingCart = new ArrayList<>();
		}

		int cartProductNumber = Integer.parseInt(request.getParameter("cartProductNumber"));
		int newProductCount = Integer.parseInt(request.getParameter("productCount"));
		String cartProductCondition = request.getParameter("cartProductCondition");
		
		if ("upCartProductCount".equals(cartProductCondition)) {

			// 장바구니에서 해당 상품 찾기
			for (Map<String, Object> cartItem : shoppingCart) {
				System.out.println("돌릴 상품 >> " +(int) cartItem.get("productNumber"));
				if ((int) cartItem.get("productNumber") == cartProductNumber) {
					int currentCount = (int) cartItem.get("cartProductCount");
					cartItem.put("cartProductCount", currentCount + newProductCount);
					System.out.println("기존 수량 [" + currentCount + "]");
					System.out.println("바뀐 수량 [" + currentCount + newProductCount + "]");
					out.print("true");
					out.flush();
					break;
				}
			}

		} else if ("downCartProductCount".equals(cartProductCondition)) {
			// 장바구니에서 해당 상품 찾기
			for (Map<String, Object> cartItem : shoppingCart) {
				if ((int) cartItem.get("productNumber") == cartProductNumber) {
					int currentCount = (int) cartItem.get("cartProductCount");
					cartItem.put("cartProductCount", currentCount - newProductCount);
					out.print("true");
					out.flush();
					break;
				}
			}
		}
		
	}
}
