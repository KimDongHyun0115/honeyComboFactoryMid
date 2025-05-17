/**
 *  주문 상세 js
 */

$(document).ready(function() {
	// 로그인 상태 확인
	const loginedMemberNumber = sessionStorage.getItem('loginedMemberNumber');
	console.log("내정보 페이지 접근 시 로그인 정보 : [" + loginedMemberNumber + "]");

	if (!loginedMemberNumber) {  // 로그인되어 있지 않다면
		alert("로그인이 필요한 페이지입니다. 로그인 페이지로 이동합니다.");

		// 로그인 페이지로 이동
		location.href = "login.do";
	}
});