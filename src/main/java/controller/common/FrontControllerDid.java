package controller.common;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("*.did")
public class FrontControllerDid extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ActionFactory factory;   
	
	
    public FrontControllerDid() {
        super();
        factory = new ActionFactory();
    }
    
    private void didAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	String command = request.getRequestURI();
    	System.out.println("CTRL 로그 : action ["+command+"]");
    	command = command.replace("/honeyComboFactory/", "");
    	System.out.println("COMMAND ["+command+"]");
    	
    	Action action = factory.getAction(command);
    	ActionForward forward = action.execute(request);
    	
    	if(forward == null) {
    		response.sendRedirect("error.do");
    	}

    	if(forward.isRedirect()){
    		response.sendRedirect(forward.getPath());
    	}
    	else{
    		RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
    		dispatcher.forward(request, response);
    	}
    	
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("로그 [GET] 요청 호출됨");
		didAction(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("로그 [POST] 요청 호출됨");
		didAction(request,response);
	}

}
