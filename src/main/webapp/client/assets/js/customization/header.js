/**
 *  헤더 js
 */
// 로그아웃 기능
const logout = () => {
	console.log("로그아웃 실행");
	// sessionStorage에 회원 번호 값 제거
	sessionStorage.removeItem("loginedMemberNumber");
	// 로그아웃 실행
	location.href = "logout.did";
}