/**
 *  로그인 js
 */

// 중요 값 상수화
const loginUrl = "/honeyComboFactory/LoginServlet"; // 로그인 서블릿 url
const kakaoLoginUrl = "/honeyComboFactory/KakaoLoginServlet"; // 카카오 로그인 서블릿 url
const javaScriptKey = "api키"; // SDK 카카오 로그인 키
const kakaoServerUrl = "/v2/user/me"; // 카카오 로그인용 서버 url

// 로그인 중이라면 페이지 접근 막기
$(document).ready(function() {
	const loginedMemberNumber = sessionStorage.getItem('loginedMemberNumber');

	console.log("로그인 페이지 접근 시 로그인 정보 : [" + loginedMemberNumber + "]");

	// ❗ 'null', '', undefined 아닌 정확한 숫자형 or 문자열 체크
	if (loginedMemberNumber && loginedMemberNumber !== "null") {
		alert("올바르지 않은 접근입니다. 메인페이지로 이동합니다.");
		location.href = "main.do";
	}
});


// 로그인 기능
// 폼 제출을 AJAX로 처리
document.getElementById('loginForm').addEventListener('submit', function(event) {
	console.log("로그인 실행");
	event.preventDefault(); // 기본 폼 제출 동작을 방지

	const memberId = document.getElementById('memberId').value;
	const memberPassword = document.getElementById('memberPassword').value;
	console.log("입력받은 로그인 아이디 : [" + memberId + "]");
	console.log("입력받은 로그인 비밀번호 : [" + memberPassword + "]");

	// AJAX 요청
	$.ajax({
		type: "POST", // 방식
		url: loginUrl, // 서버로 데이터를 보낼 URL
		data: { // 보낼 값
			memberId: memberId,
			memberPassword: memberPassword
		},
		dataType: "text", // 받을 타입
		success: function(response) {
			console.log("받은 로그인 데이터 : [" + response + "]");
			if (response === "false") { // 로그인 실패 시
				alert("로그인 실패! 아이디나 비밀번호를 확인하세요.");
			} else { // 로그인 성공 시
				sessionStorage.setItem("loginedMemberNumber", response);
				alert("방문을 환영합니다.");
				location.href = "main.do"; // 로그인 후 메인 페이지로 이동
			}
		},
		error: (xhr, status, error) => { // 에러 처리
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			alert("서버에 문제가 발생했습니다. 지속될 시 관리자에게 문의하세요.");
			location.href = "error.do";
		}
	});
});



// 카카오 로그인 초기화
Kakao.init(javaScriptKey);

// 카카오 로그인 기능
const kakaoLogin = () => {
	console.log("카카오 로그인 실행");

	Kakao.Auth.login({
		success: () => {
			// 로그인 성공 후 이메일 정보 요청
			Kakao.API.request({
				url: kakaoServerUrl,
				success: (response) => {
					console.log("카카오 로그인 성공");
					// AJAX 요청
					$.ajax({
						type: "POST", // 방식
						url: kakaoLoginUrl, // 서버로 데이터를 보낼 URL
						data: JSON.stringify(response), // 보낼 값
						dataType: "text", // 받을 타입
						success: function(response) {
							console.log("받은 로그인 데이터 : [" + response + "]");
							if (response === "false") { // 로그인 실패 시
								alert("정보 조회에 실패했습니다. 지속될 시 관리자에게 문의하세요.");
							} else { // 로그인 성공 시
								sessionStorage.setItem("loginedMemberNumber", response);
								alert("방문을 환영합니다.");
								location.href = "main.do"; // 로그인 후 메인 페이지로 이동
							}
						},
						error: (xhr, status, error) => { // 에러 처리
							console.error("AJAX 요청 에러 발생", xhr.status, status, error);
							alert("서버에 문제가 발생했습니다. 지속될 시 관리자에게 문의하세요.");
							location.href = "error.do";
						}
					});
				},
				fail: (xhr, status, error) => {
					console.error("AJAX 요청 에러 발생", xhr.status, status, error);
					alert("카카오 서버에 문제가 발생했습니다. 지속될 시 관리자에게 문의하세요.");
					location.href = "error.do";
				}
			});
		},
		fail: (xhr, status, error) => {
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			alert("카카오 서버에 문제가 발생했습니다. 지속될 시 관리자에게 문의하세요.");
			location.href = "error.do";
		}
	});
}