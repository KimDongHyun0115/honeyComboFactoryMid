/**
 *  메인 페이지 js
 */
const insertCartUrl = "/honeyComboFactory/AddCartProductServlet"; // 장바구니 상품 담기 서블릿 url
let loginedMemberNumber; // 로그인한 회원 번호

$(document).ready(function() {
	// 로그인 상태 확인
	loginedMemberNumber = sessionStorage.getItem('loginedMemberNumber');
	console.log("꿀조합 게시글 상세 페이지 접근 시 로그인 정보 : [" + loginedMemberNumber + "]");
});

// 장바구니 상품 담기 기능
const insertCart = (productNumber, cartProductCount, isComboProduct) => {
	console.log("메인 장바구니 상품 담기 실행");
	if (!loginedMemberNumber) { // 로그인하지 않은 경우
		console.log("로그인 없이 장바구니 담기 요청");
		alert("로그인이 필요한 기능입니다!");
		return;
	}
	console.log("장바구니 담을 상품 번호 : [" + productNumber + "]");
	console.log("장바구니 담을 상품 개수 : [" + cartProductCount + "]");
	console.log("장바구니 담을 꿀조합 상품 여부 : [" + isComboProduct + "]");

	$.ajax({
		type: "POST", // 방식
		url: insertCartUrl, // 찾아갈 주소
		data: { // 보낼 값
			productNumber: productNumber,
			cartProductCount: cartProductCount,
			isComboProduct: isComboProduct
		},
		dataType: "text", // 받을 타입
		success: (response) => { // 성공 시 처리
			if (response === "true") { // 잘 담겼다면
				console.log("장바구니 상품 담기 성공");
				alert("선택하신 상품이 장바구니에 담겼습니다.");
			} else { // 안 담겼다면
				consoel.log("장바구니 상품 담기 실패");
				alert("선택하신 상품을 장바구니에 담기 실패했습니다.");
				location.href = "error.do";
			}
		},
		error: (xhr, status, error) => { // 에러 처리
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			alert("서버에 문제가 발생했습니다. 지속될 시 관리자에게 문의하세요.");
			location.href = "error.do";
		}
	});
};