<%@ page import="java.net.URLEncoder" %> 
<%@ page import="java.security.SecureRandom" %> <!-- 보안성이 높은 랜덤 숫자를 생성  --> 
<%@ page import="java.math.BigInteger" %> 
<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<%
	String scope = "name,email,birthday,birthyear,mobile"; // 네이버에게 받을 값
    String clientId = "클라이언트 ID"; 
    String redirectURI = URLEncoder.encode("http://localhost:8088/honeyComboFactory/client/callback.jsp", "UTF-8"); // 로그인 성공 후 리디렉션될 콜백 주소(네이버에 등록한 것과 동일)
    SecureRandom random = new SecureRandom(); 
    String state = new BigInteger(130, random).toString();
    String apiURL = "https://nid.naver.com/oauth2.0/authorize?response_type=code" // 네이버 인증 요청 URL
    // 필수 파라미터
         + "&client_id=" + clientId 
         + "&redirect_uri=" + redirectURI // 네이버 개발자 센터에 등록한 것과 일치
         + "&state=" + state 
   		 + "&scope=" + scope; 
    session.setAttribute("state", state); 
%>

</body>
</html>


