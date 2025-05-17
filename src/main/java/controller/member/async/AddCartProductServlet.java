package controller.member.async;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/AddCartProductServlet")
public class AddCartProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public AddCartProductServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/plain; charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        ArrayList<Map<String, Object>> shoppingCart = (ArrayList<Map<String, Object>>) session.getAttribute("shoppingCart");
        if (shoppingCart == null) {
            shoppingCart = new ArrayList<>();
            session.setAttribute("shoppingCart", shoppingCart);
        }

        int productNumber = Integer.parseInt(request.getParameter("productNumber"));
        int cartProductCount = Integer.parseInt(request.getParameter("cartProductCount"));
        boolean isComboProduct = Boolean.parseBoolean(request.getParameter("isComboProduct"));
        
        System.out.println("받은 상품 번호 [" +productNumber+ "]");

        boolean alreadyIn = false;

        for (Map<String, Object> cartItem : shoppingCart) {
            if ((int) cartItem.get("productNumber") == productNumber && (boolean) cartItem.get("isComboProduct") == isComboProduct) {
                int currentCount = (int) cartItem.get("cartProductCount");
                cartItem.put("cartProductCount", currentCount + cartProductCount);
                alreadyIn = true;
                break;
            }
        }

        if (!alreadyIn) {
            Map<String, Object> newCartItem = new HashMap<>();
            newCartItem.put("productNumber", productNumber);
            newCartItem.put("cartProductCount", cartProductCount);
            newCartItem.put("isComboProduct", isComboProduct);
            shoppingCart.add(newCartItem);
        }

        session.setAttribute("shoppingCart", shoppingCart);

       System.out.println(shoppingCart);
        
        out.print("true");
        out.flush();
	}

}
