package controller.common;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("*.do")
public class FrontControllerDo extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ActionFactory factory;   
	
	
    public FrontControllerDo() {
        super();
        factory = new ActionFactory();
    }
    
    private void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	String command = request.getRequestURI();
    	System.out.println("CTRL 로그 : action ["+command+"]");
    	command = command.replace("/honeyComboFactory/", "");
    	System.out.println("COMMAND ["+command+"]");

    	
    	Action action = factory.getAction(command);
    	System.out.println("ACTION ["+action+"]");
    	
    	ActionForward forward = action.execute(request);
       	
    	if(forward == null) {
    		System.out.println("에러 페이지");
    		response.sendRedirect("error.do");
    	}

    	if(forward.isRedirect()){
    		System.out.println("Redirect True");
    		response.sendRedirect(forward.getPath());
    	}
    	else{
    		System.out.println("Redirect False");
    		RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
    		dispatcher.forward(request, response);
    	}
    	
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("로그 [GET] 요청 호출됨");
		doAction(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("로그 [POST] 요청 호출됨");
		doAction(request,response);
	}

}
