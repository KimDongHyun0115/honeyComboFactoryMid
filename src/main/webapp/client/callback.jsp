<%@page import="io.opentelemetry.exporter.logging.SystemOutLogRecordExporter"%>
<%@ page import="java.io.*, java.net.*, java.util.*" %>
<%@ page import="org.json.simple.*, org.json.simple.parser.*" %>
<%@ page import="model.dao.MemberDAO, model.dto.MemberDTO" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>

<%
    String code = request.getParameter("code");
    String state = request.getParameter("state");
    String redirectURI = URLEncoder.encode("http://localhost:8088/honeyComboFactory/client/callback.jsp", "UTF-8");
    String clientId = "클라이언트ID";
    String clientSecret = "시크릿ID"; 

    String apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code"
        + "&client_id=" + clientId
        + "&client_secret=" + clientSecret
        + "&redirect_uri=" + redirectURI
        + "&code=" + code
        + "&state=" + state;

    String accessToken = "";

    try {
        URL url = new URL(apiURL);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuilder responseBuffer = new StringBuilder();
        while ((inputLine = br.readLine()) != null) {
            responseBuffer.append(inputLine);
        }
        br.close();

        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(responseBuffer.toString());
        accessToken = (String) json.get("access_token");

        if (accessToken == null || accessToken.isEmpty()) {
            out.println("<script>alert('토큰 발급 실패'); location.href='" + request.getContextPath() + "/client/login.do';</script>");
            return;
        }

    } catch (Exception e) {
        e.printStackTrace();
        out.println("<script>alert('토큰 요청 중 오류 발생'); location.href='" + request.getContextPath() + "/client/login.do';</script>");
        return;
    }

    // 사용자 정보 요청
    try {
        String token = "Bearer " + accessToken;
        URL profileUrl = new URL("https://openapi.naver.com/v1/nid/me");
        HttpURLConnection profileCon = (HttpURLConnection) profileUrl.openConnection();
        profileCon.setRequestMethod("GET");
        profileCon.setRequestProperty("Authorization", token);

        BufferedReader profileBr = new BufferedReader(new InputStreamReader(profileCon.getInputStream(), "UTF-8"));
        StringBuilder profileResponse = new StringBuilder();
        String profileLine;
        while ((profileLine = profileBr.readLine()) != null) {
            profileResponse.append(profileLine);
        }
        profileBr.close();

        JSONParser parser = new JSONParser();
        JSONObject profileJson = (JSONObject) parser.parse(profileResponse.toString());
        JSONObject responseObj = (JSONObject) profileJson.get("response");

        if (responseObj == null) {
            out.println("<script>alert('네이버 로그인 실패: 사용자 정보 없음'); location.href='" + request.getContextPath() + "/client/login.do';</script>");
            return;
        }

        String email = (String) responseObj.get("email");
        String name = (String) responseObj.get("name");
        String birthYear = (String) responseObj.get("birthyear");
        String birthday = (String) responseObj.get("birthday");
        String mobile = (String) responseObj.get("mobile");

        if (email == null || !email.contains("@")) {
            out.println("<script>alert('유효한 이메일이 없습니다.'); location.href='" + request.getContextPath() + "/client/login.do';</script>");
            return;
        }

        // 이메일 분리
        String[] emailParts = email.split("@");
        String emailId = emailParts[0];
        String emailDomain = emailParts[1];

        MemberDAO memberDAO = new MemberDAO();
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberEmailId(emailId);
        memberDTO.setMemberEmailDomain(emailDomain);
        memberDTO.setCondition("SELECTONEEMAIL");

        MemberDTO existingUser = memberDAO.selectOne(memberDTO);

        String memberId;
        String memberPassword = "NAVER"; // 네이버는 고정 비번

        if (existingUser == null) {
            // 신규 회원 가입
            memberId = emailId;
         // 1. 생년월일 문자열 생성
            String cleanBirthday = birthday.replace("-", ""); // "08-14" → "0814"
            String birthStr = birthYear + "-" + cleanBirthday.substring(0, 2) + "-" + cleanBirthday.substring(2); // "2000-08-14"

            // 2. 문자열을 java.util.Date로 파싱
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date birthDate = sdf.parse(birthStr); // java.util.Date

            // 3. java.sql.Date로 변환해서 setter에 전달
            memberDTO.setMemberBirth(new java.sql.Date(birthDate.getTime()));

            memberDTO.setMemberId(memberId);
            memberDTO.setMemberId(memberId);
            memberDTO.setMemberPassword(memberPassword);
            memberDTO.setMemberName(name);
          //  memberDTO.setMemberBirth(Date.valueOf(birth));
            memberDTO.setMemberPhoneNumber(mobile);
            memberDTO.setCondition("INSERT");

            memberDAO.insert(memberDTO);

            // 다시 조회해서 memberNumber 가져오기
            memberDTO.setCondition("SELECTONEEMAIL");
            memberDTO = memberDAO.selectOne(memberDTO);
        } else {
            // 기존 회원 로그인
            memberDTO = existingUser;
            memberId = memberDTO.getMemberId();
        }

      
        if (memberId == null || memberPassword == null) {
            out.println("<script>alert('로그인 정보 생성 실패'); location.href='" + request.getContextPath() + "/client/login.do';</script>");
            return;
        }

        // 네이버 로그인 후 세션 저장
        session.setAttribute("loginedMemberNumber", memberDTO.getMemberNumber());
        session.setAttribute("loginedMemberName", memberDTO.getMemberName());
        session.setAttribute("loginedmemberIsAdmin", memberDTO.isMemberIsAdmin());

        

    } catch (Exception e) {
        e.printStackTrace();
        out.println("<script>alert('사용자 정보 요청 중 오류 발생'); location.href='" + request.getContextPath() + "/client/login.do';</script>");
    }
    %>
    
    <script>
        sessionStorage.setItem("loginedMemberNumber", "<%= session.getAttribute("loginedMemberNumber") %>");
        sessionStorage.setItem("loginedMemberName", "<%= session.getAttribute("loginedMemberName") %>");
        sessionStorage.setItem("loginedmemberIsAdmin", "<%= session.getAttribute("loginedmemberIsAdmin") %>");

        location.href = "main.do"; // 리디렉션을 JS로 옮김
      </script>
