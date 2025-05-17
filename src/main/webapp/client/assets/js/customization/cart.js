/**
 *  장바구니 js
 */
const changeCartProductCountUrl = "/honeyComboFactory/ChangeCartProductCountServlet"; // 장바구니 상품 개수 변경 서블릿 url
const deleteCartProductUrl = "/honeyComboFactory/DeleteCartProductServlet"; // 장바구니 상품 삭제 서블릿 url
const getCartProductInfoUrl = "/honeyComboFactory/GetCartProductInfoServlet"; // 장바구니 상품 정보 요청 서블릿 url

$(document).ready(function() {
	// 로그인 상태 확인
	const loginedMemberNumber = sessionStorage.getItem('loginedMemberNumber');
	console.log("장바구니 페이지 접근 시 로그인 정보 : [" + loginedMemberNumber + "]");

	if (!loginedMemberNumber) {  // 로그인되어 있지 않다면
		alert("로그인이 필요한 페이지입니다. 로그인 페이지로 이동합니다.");

		// 로그인 페이지로 이동
		location.href = "login.do";
	}
});

// 카카오페이 결제 준비 요청 기능
const purchaseCartProduct = () => {
	console.log("결제");
	alert("결제로직 미완성");
}

// 장바구니 각 상품 구매 가격 변경 기능
const calculationPrice = (cartProductNumber) => {
	console.log("장바구니 각 상품 구매 가격 변경 실행");
	const cartProductPrice = parseInt($("#cartProductPrice-" + cartProductNumber).text()); // 상품 가격
	const cartProductCount = parseInt($("#count-" + cartProductNumber).val()); // 상품 수량

	console.log("장바구니 각 상품 구매 가격 변경할 번호 : " + cartProductNumber + "]");
	console.log("상품 개수변경 가격 : [" + cartProductPrice + "]");
	console.log("상품 개수변경 수량 : [" + cartProductCount + "]");

	console.log("변경할 상품 가격 : [" + cartProductPrice * cartProductCount + "]");
	$("#cartProductSumPrice-" + cartProductNumber).text(cartProductPrice * cartProductCount);
}

// 장바구니 각 체크박스 클릭 기능
const clickOneCheckBox = (isChecked) => {
	console.log("체크박스 해제 여부 : [" + isChecked + "]");

	if (isChecked) { // 체크 실행이라면
		if (checkAllChecked()) { // 체크박스가 다 체크상태라면
			// productCheckBox 클래스의 모든 체크박스 상태 변경
			$("#cartAllCheckBox").prop('checked', true);
		}
	}
	else { // 체크 해제라면
		// productCheckBox 클래스의 모든 체크박스 상태 변경
		$("#cartAllCheckBox").prop('checked', false);
	}

	// 장바구니 상품 가격 총합 계산 함수 호출
	calculationTotalAmount();
}

// 전체 체크박스 체크 상태 여부 검사 기능
const checkAllChecked = () => {
	console.log("전체 체크박스 체크 상태 여부 검사 실행");
	const totalCheckboxes = $('.productCheckBox').length; // 전체 체크박스 개수
	const checkedCheckboxes = $('.productCheckBox:checked').length; // 체크된 체크박스 개수
	console.log("전체 체크박스 개수 : [" + totalCheckboxes + "]");
	console.log("체크된 체크박스 개수 : [" + checkedCheckboxes + "]");

	if (totalCheckboxes == checkedCheckboxes) { // 체크박스가 다 체크상태라면
		console.log("체크박스 전부 체크 됨")
		return true;
	}
	return false;
}

// 장바구니 상품 가격 총합 계산 기능
const calculationTotalAmount = () => {
	console.log("종바구니 상품 총합 계산 실행");
	let totalAmount = 0;

	// 체크된 상품들의 가격을 합산
	$('.productCheckBox:checked').each(function() {
		const productNumber = $(this).val();  // 개별 상품 번호
		const price = parseInt($("#cartProductPrice-" + productNumber).text());  // 개별 상품 가격
		const count = parseInt($("#count-" + productNumber).val()); // 상품 수량

		totalAmount += price * count; // 총 가격 계산
	});
	console.log("체크된 상품들의 합산 가격 : [" + totalAmount + "]");

	// 총합을 페이지에 표시
	$('#totalAmount').text(totalAmount);
}

// 장바구니 전체 선택/비선택 기능
const selectAllProduct = (isChecked) => {
	console.log("장바구니 상품 전체 체크 여부 : [" + isChecked + "]");

	// productCheckBox 클래스의 모든 체크박스를 선택하여 상태 변경
	$(".productCheckBox").prop('checked', isChecked);

	// 장바구니 상품 가격 총합 계산 함수 호출
	calculationTotalAmount();
}

// 장바구니 상품 수량 표시 변경 기능
const changeInputCartProductCount = (cartProductNumber, cartProductCondition, productCount) => {
	console.log("장바구니 상품 수량 표시 변경할 상품 번호 : [" + cartProductNumber + "]");
	console.log("장바구니 상품 수량 표시 변경할 상품 증감 조건 : [" + cartProductCondition + "]");
	console.log("장바구니 상품 수량 표시 변경할 상품 변경 수량 : [" + productCount + "]");

	const beforeCount = parseInt($("#count-" + cartProductNumber).val(), 10);;
	console.log("장바구니 상품 수량 표시 변경 전 수량 : [" + beforeCount + "]");
	const check = checkMaxMinProductCount(cartProductNumber, cartProductCondition, productCount);
	if (cartProductCondition === "upCartProductCount") { // 상품 수량 증가라면
		// 장바구니 최소/최대 개수 검사 함수 호출
		if (!check) {
			$("#count-" + cartProductNumber).val(beforeCount + productCount);
		}
	}
	else { // 상품 수량 감소라면
		// 장바구니 최소/최대 개수 검사 함수 호출
		if (!check) {
			$("#count-" + cartProductNumber).val(beforeCount - productCount);
		}
	}
}

// 장바구니 최소/최대 개수 검사 기능
const checkMaxMinProductCount = (cartProductNumber, cartProductCondition, productCount) => {
	let flag = false;
	const nowCount = parseInt($("#count-" + cartProductNumber).val());

	if (cartProductCondition === "upCartProductCount") { // 상품 수량 증가라면
		const maxValue = parseInt($("#count-" + cartProductNumber).prop("max"));
		if (nowCount + parseInt(productCount) > maxValue) { // 재고보다 많아진다면
			console.log("재고보다 많을 수 없음");
			flag = true;
		}
	}
	else { // 상품 수량 감소라면
		if (nowCount - parseInt(productCount) < 1) { // 1보다 작아진다면
			console.log("1보다 작을 수 없음");
			flag = true;
		}
	}
	return flag;
}

// 장바구니 상품 개수 증가/감소 기능
const changeCartProductCount = (cartProductNumber, cartProductCondition) => {
	// 장바구니 증감할 개수
	const productCount = 1;

	console.log("개수 변경 장바구니 상품 실행");
	console.log("개수 변경 장바구니 상품 번호 : [" + cartProductNumber + "]");
	console.log("개수 변경 장바구니 상품 조건 : [" + cartProductCondition + "]");

	// 장바구니 최소/최대 개수 검사 함수 호출
	if (checkMaxMinProductCount(cartProductNumber, cartProductCondition, productCount)) {
		return;
	}

	$.ajax({
		type: "POST", // 방식
		url: changeCartProductCountUrl, // 찾아갈 주소
		data: { // 보낼 값
			cartProductNumber: cartProductNumber,
			cartProductCondition: cartProductCondition,
			productCount: productCount
		},
		dataType: "text", // 받을 타입
		success: (response) => { // 성공적이라면
			if (response === "true") { // 상품 개수 변경 성공 시
				console.log("장바구니 상품 개수 변경 성공");

				// 장바구니 상품 수량 표시 변경 함수 호출
				changeInputCartProductCount(cartProductNumber, cartProductCondition, productCount);

				// 장바구니 각 상품 구매 가격 변경 함수 호출
				calculationPrice(cartProductNumber);

				console.log("증감 상품 장바구니 선택 여부 : [" +
					$("#productCheckBox-" + cartProductNumber).prop("checked") + "]");
				if ($("#productCheckBox-" + cartProductNumber).prop("checked")) { // 선택된 상품이었다면
					// 장바구니 상품 가격 총합 계산 함수 호출
					calculationTotalAmount();
				}
			}
			else { // 상품 개수 변경 실패 시
				console.log("장바구니 상품 개수 변경 실패");
				alert("장바구니 개수 변경에 실패했습니다.");
				location.href = "error.do"; // 에러 페이지 이동
			}
		},
		error: (xhr, status, error) => { // 에러 처리
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			alert("서버에 문제가 발생했습니다. 지속될 시 관리자에게 문의하세요.");
			location.href = "error.do";
		}
	});
}

// 장바구니 상품 삭제 기능
const deleteCartProduct = () => {
	console.log("장바구니 상품 삭제 실행");

	let cartProductNumberDatas = ""; // 선택된 상품 번호들을 저장할 문자열

	// 체크된 상품들의 번호 저장
	$('.productCheckBox:checked').each(function() {
		const productNumber = $(this).val(); // 상품 번호

		// 상품 번호를 저장 (처음이 아니면 + 추가)
		if (cartProductNumberDatas.length > 0) {
			cartProductNumberDatas += "+";
		}

		cartProductNumberDatas += productNumber;
	});
	console.log("체크된 상품들의 번호 정보 : [" + cartProductNumberDatas + "]");

	$.ajax({
		type: "POST", // 방식
		url: deleteCartProductUrl, // 찾아갈 주소
		data: { cartProductNumberDatas: cartProductNumberDatas }, // 보낼 값
		dataType: "text", // 받을 타입
		success: (response) => { // 성공적이라면
			if (response === "true") { // 상품 삭제 성공 시
				console.log("장바구니 상품 삭제 성공");

				// "+"를 기준으로 모든 값을 배열로 분리
				let deleteParts = cartProductNumberDatas.split("+");
				console.log("총 개수:", deleteParts.length); // 몇 개인지 확인

				// 배열 반복문으로 행 삭제 처리
				deleteParts.forEach((deletePart) => {
					// 해당 상품 행 삭제
					$("#cartRow-" + deletePart).remove();
				});

				// 장바구니 상품 가격 총합 계산 함수 호출
				calculationTotalAmount();

				if ($('.productCheckBox').length == 0) { // 장바구니에 상품이 없다면
					$("table.table tbody").prepend(`
						<tr>
							<td>장바구니에 담긴 상품이 없습니다.</td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
					`);
				}
			}
			else { // 상품 삭제 실패 시
				alert("장바구니 상품 삭제에 실패했습니다.");
				location.href = "error.do";
			}
		},
		error: (xhr, status, error) => { // 에러 처리
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			alert("서버에 문제가 발생했습니다. 지속될 시 관리자에게 문의하세요.");
			location.href = "error.do";
		}
	});
}

// 카카오 주소 검색 API js
const kakaoPostAPIcode = () => {
	console.log("주소 API 실행");
	// 카카오 주소 검색 팝업을 생성하고 실행 
	new daum.Postcode({
		// 사용자가 주소를 선택했을 때 실행되는 콜백 함수 
		oncomplete: (data) => {
			// data = 선택한 주소 정보
			console.log(data);

			let addr = ''; // 주소 변수(도로명, 지번)
			let extraAddr = ''; // 참고항목 변수(건물병, 법정동 등 )

			//사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져오기
			// 사용자가 도로명 주소를 선택했을 경우
			if (data.userSelectedType === 'R') {
				addr = data.roadAddress;
			} else {
				// 사용자가 지번 주소를 선택했을 경우
				addr = data.jibunAddress;
			}

			// Geocoder 객체 생성
			let geocoder = new kakao.maps.services.Geocoder();

			geocoder.addressSearch(addr, function(result, status) {
				if (status === kakao.maps.services.Status.OK) {
					let lat = result[0].y; // 위도
					let lng = result[0].x; // 경도
					console.log("📍 위도:", lat, "경도:", lng);
				} else {
					console.error("주소 → 좌표 변환 실패");
				}
			});

			// 참고항목 조합(도로명 주소일 때만)
			// 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
			//Kakao가 도로명 주소를 선택하면 userSelectedType에 'R'을 넣기로 약속
			if (data.userSelectedType === 'R') {
				// 법정동명이 있을 경우 추가한다. (법정리는 제외)
				// 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
				if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
					extraAddr += data.bname;
				}
				// 건물명이 있고, 공동주택일 경우 추가한다.
				if (data.buildingName !== '' && data.apartment === 'Y') {
					extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
				}
				// 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
				if (extraAddr !== '') {
					extraAddr = ' (' + extraAddr + ')';
				}
			}
			// 사용자가 주소를 선택했을 때, 그 주소를 input에 넣는 코드
			// 우편번호와 주소 정보를 해당 필드에 넣는다. 
			$("#memberAddressMain").val(addr);
			// 상세주소 입력란 readonly 속성 제거
			$("#memberAddressDetail").prop("readonly", false);
			// 커서를 상세주소 입력란으로 자동 이동
			$("#memberAddressDetail").focus();
		}
	}).open(); //팝업 창 실행(사용자 주소 검색창)
}