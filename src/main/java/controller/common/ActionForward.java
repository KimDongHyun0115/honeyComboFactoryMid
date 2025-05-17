package controller.common;

public class ActionForward {
	private String path; // 어디로 가야하는지 ==> String
	private boolean redirect; // 어떻게 가야하는지 ==> boolean
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public boolean isRedirect() {
		return redirect;
	}
	public void setRedirect(boolean redirect) {
		this.redirect = redirect;
	}
}
