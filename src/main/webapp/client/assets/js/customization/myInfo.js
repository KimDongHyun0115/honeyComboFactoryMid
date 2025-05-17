/**
 *  내정보 js
 */
const withdrawUrl = "/honeyComboFactory/WithdrawServlet"; // 회원탈퇴 서블릿 url
const loadMoreMyBoardUrl = "/honeyComboFactory/MyInfoMyBoardServlet"; // 본인 작성 글 불러오기 서블릿 url
const loadMorePurchaseUrl = "/honeyComboFactory/MyInfoMyPurchaseServlet"; // 주문 내역 불러오기 서블릿 url
const loadMoreLikedBoardUrl = "/honeyComboFactory/MyInfoMyLikedBoardServlet"; // 좋아요 글 불러오기 서블릿 url
const checkLoginedMemberUrl = "/honeyComboFactory/ConfirmPasswordServlet"; // 본인확인 서블릿 url
const updateMemberInfoUrl = "/honeyComboFactory/UpdateMyInfoServlet"; // 회원 정보수정 서블릿 url
const updateMemberPasswordUrl = "/honeyComboFactory/ChangePasswordServlet"; // 비밀번호 변경 서블릿 url
let confirmNumber = 0; // 발급 받은 핸드폰 인증 번호
let timer; // 핸드폰 인증번호 타이머 ID 저장용 변수
const pageGroupSize = 5; // 한 번에 보여줄 페이지네이션 개수
// 본인 작성 글 페이지네이션 변수
let myBoardPageNumber = 1; // 초기 페이지는 1로 설정
const myBoardContentCount = 3; // 한 번에 가져올 데이터 개수
// 좋아요 글 페이지네이션 변수
let likedBoardPageNumber = 1; // 초기 페이지는 1로 설정
const likedBoardContentCount = 3; // 한 번에 가져올 데이터 개수
// 주문내역 페이지네이션 변수
let purchasePageNumber = 1; // 초기 페이지는 1로 설정
const purchaseContentCount = 3; // 한 번에 가져올 데이터 개수

$(document).ready(function() {
	// 로그인 상태 확인
	const loginedMemberNumber = sessionStorage.getItem('loginedMemberNumber');
	console.log("내정보 페이지 접근 시 로그인 정보 : [" + loginedMemberNumber + "]");

	if (!loginedMemberNumber) {  // 로그인되어 있지 않다면
		alert("로그인이 필요한 페이지입니다. 로그인 페이지로 이동합니다.");

		// 로그인 페이지로 이동
		location.href = "login.do";
		return;
	}

	// 본인 인증 확인
	checkLoginedMember().then((isAuthenticated) => {
		if (!isAuthenticated) { // 본인 인증 실패 시
			location.href = "main.do";
			return;
		}

		// 본인 작성글 페이지네이션 이벤트 등록
		$(document).on("click", ".boardPage", function(event) { // 페이지네이션 클릭 시
			event.preventDefault(); // 기본 이벤트 방지

			changemyBoardPage($(this)); // 페이지 이동
		});

		// 좋아요 글 페이지네이션 이벤트 등록
		$(document).on("click", ".likedBoardPage", function(event) { // 페이지네이션 클릭 시
			event.preventDefault(); // 기본 이벤트 방지

			changeLikedBoardPage($(this)); // 페이지 이동
		});

		// 주문 내역 페이지네이션 이벤트 등록
		$(document).on("click", ".purchasePage", function(event) { // 페이지네이션 클릭 시
			event.preventDefault(); // 기본 이벤트 방지

			changePurchasePage($(this)); // 페이지 이동
		});

		// 본인 작성 글 불러오기 함수 호출
		loadMyBoardDatas();
		// 좋아요 글 불러오기 함수 호출
		loadLikedBoardDatas();
		// 주문내역 불러오기 함수 호출
		loadPurchaseDatas();

		// 폼 제출 시
		$('#memberInfoForm').on('submit', function(event) {
			event.preventDefault(); // 기본 제출 동작 방지

			if ($("#updateMyInfoBtn").text() === "정보수정") { // 정보수정 버튼 클릭 시
				// 본인 인증 확인
				checkLoginedMember().then((isAuthenticated) => {
					if (!isAuthenticated) { // 본인 인증 실패 시
						return;
					}

					// 버튼 내용 변경
					$("#updateMyInfoBtn").text("수정 완료");
					console.log("회원정보 수정 가능 상태로 변경");

					// 정보수정 입력 form 잠금 해제 함수 호출
					unlockMemberInfoForm();
				});
				return;
			}

			// 표준 메서드 checkValidity()를 사용하여 폼의 입력값이 유효한지 확인
			if (!this.checkValidity()) {
				event.stopPropagation(); // 이벤트가 부모 요소로 전파되는 것을 방지
			}
			else {
				// 핸드폰번호 인증 검사
				if ($("#confirmPhoneNumberBtn").val() !== "인증 완료") {
					alert("휴대폰번호 인증을 진행해주세요.");
					return;
				}

				// 입력받는 값들 공백 제거 후 값을 다시 설정
				$("#memberEmailId").val($("#memberEmailId").val().replace(/\s+/g, ""));
				$("#memberEmailDomain").val($("#memberEmailDomain").val().replace(/\s+/g, ""));
				$("#memberName").val($("#memberName").val().replace(/\s+/g, ""));
				$("#memberPhoneNumber").val($("#memberPhoneNumber").val().replace(/\s+/g, ""));

				// 수정된 정보 저장
				$.ajax({
					type: "POST", // 방식
					url: updateMemberInfoUrl, // 찾아갈 주소
					data: $(this).serialize(), // 보낼 값
					dataType: "text", // 받을 타입
					success: (response) => { // 성공적이라면
						if (response === "true") { // 정보수정 성공 시
							console.log("정보 수정 성공");
							alert("회원님의 정보가 수정되었습니다. 다시 로그인해주세요!");
							sessionStorage.removeItem("loginedMemberNumber");
							location.href = "logout.did";
						}
						else { // 정보수정 실패 시
							console.log("정보 수정 실패");
							alert("정보 수정에 실패했습니다.");
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
			// 폼에 'was-validated' 클래스를 추가하여 유효성 검사 상태를 시각적으로 표시
			$(this).addClass("was-validated");
		});
	});
});

// 정보수정 입력 form 잠금 해제 기능
const unlockMemberInfoForm = () => {
	console.log("정보수정 form 잠금 해제 실행");
	// disabled 속성 제거 및 정보 *표시 제거
	$("#memberId").prop("type", "text");
	$("#memberName").prop("disabled", false).prop("type", "text");
	$("#memberPhoneNumber").prop("disabled", false).prop("type", "tel");
	$("#confirmPhoneNumberBtn").prop("type", "button");
	$("#memberAddressBtn").prop("type", "button");
	$("#memberAddressMain").prop("disabled", false).prop("readonly", true).prop("type", "text");
	$("#memberAddressDetail").prop("disabled", false).prop("readonly", true).prop("type", "text");
	$("#memberEmailId").prop("disabled", false).prop("type", "text");
	$("#memberEmailDomain").prop("disabled", false).prop("type", "text");
	$("#memberBirth").prop("type", "date");
}

// 비밀번호 변경 기능
const changeMemberPassword = () => {
	console.log("비밀번호 변경 실행");

	// 본인 인증 확인
	checkLoginedMember().then((isAuthenticated) => {
		if (!isAuthenticated) { // 본인 인증 실패 시
			return;
		}

		// 비밀번호 패턴
		const passwordPattern = /^(?!([\d])\1{5,})(?=.*[\W_])\S{6,15}$/;
		let memberPassword; // 변경할 비밀번호
		const maxAttempts = 5; // 최대 가능 시도 횟수
		let attempts = 0; // 시도 횟수

		// 변경할 비밀번호 입력받기
		while (attempts < maxAttempts) {
			memberPassword = prompt("비밀번호를 입력하세요:\n(6~15자, 같은 숫자 연속 6개 이상 X, 특수문자 1개 이상 포함)");

			// 사용자가 취소(ESC) 버튼을 눌렀다면 종료
			if (memberPassword === null) {
				alert("비밀번호 입력이 취소되었습니다.");
				return;
			}

			// 패턴 검사 통과하면 루프 종료
			if (passwordPattern.test(memberPassword)) {
				break;
			}

			attempts++;
			alert("비밀번호 형식이 올바르지 않습니다. ( " + attempts + "/" + maxAttempts + " )\n다시 입력해주세요.");
		}

		// 최대 시도 횟루를 초과했다면
		if (attempts >= maxAttempts) {
			alert("최대 입력 횟수를 초과했습니다. 다시 시도해 주세요.");
			return;
		}

		console.log("입력받은 변경할 비밀번호 : [" + memberPassword + "]")
		let answer = confirm("입력하신 비밀번호로 변경하시겠습니까?");
		console.log("최종 비밀번호 변경 의사 : [" + answer + "]");
		if (!answer) {
			alert("비밀번호 변경이 취소되었습니다.");
			return;
		}

		$.ajax({
			type: "POST", // 방식
			url: updateMemberPasswordUrl, // 찾아갈 주소
			data: { memberPassword: memberPassword }, // 보낼 값
			dataType: "text", // 받을 타입
			success: (response) => { // 성공적이라면
				if (response === "true") { // 비밀번호 변경 성공 시
					console.log("비밀번호 변경 성공");
					alert("성공적으로 비밀번호 변경이 실행됐습니다. 다시 로그인해주세요!");
					sessionStorage.removeItem("loginedMemberNumber");
					location.href = "logout.did";
				}
				else { // 비밀번호 변경 실패 시
					console.log("비밀번호 변경 실패");
					alert("비밀번호 변경에 실패했습니다.");
					location.href = "error.do";
				}
			},
			error: (xhr, status, error) => { // 에러 처리
				console.error("AJAX 요청 에러 발생", xhr.status, status, error);
				alert("서버에 문제가 발생했습니다. 지속될 시 관리자에게 문의하세요.");
				location.href = "error.do";
			}
		});
	});
}

// 휴대폰번호 변경 검사 기능
const checkMemberPhoneNumber = (event) => {
	console.log("핸드폰번호 변경 검사 실행");
	// 행동이 일어난 태그의 값 받아오기
	let memberPhoneNumber = $(event.target).val().trim();
	console.log("핸드폰번호 변경 검사를 위해 받아온 핸드폰 번호 : [" + memberPhoneNumber + "]");

	if (memberPhoneNumber === $(event.target).data('original')) {
		console.log("핸드폰번호 변경 없음");
		$("#confirmPhoneNumberBtn").val("인증 완료");
		$("#phoneNumberConfirmNumber").prop("disabled", true);
		return;
	};

	console.log("핸드폰번호 변경 있음");
	$("#confirmPhoneNumberBtn").val("인증");
}

// 휴대폰번호 인증 기능
const confirmPhoneNumber = () => {
	console.log("휴대폰번호 인증");
	if ($("#confirmPhoneNumberBtn").val() === "인증 완료") { // 인증 완료 상태라면
		alert("현재 번호는 인증이 완료된 번호입니다.");
	}
	else if ($("#confirmPhoneNumberBtn").val() === "인증") { // 인증 시작이라면
		// 버튼명 변경
		$("#confirmPhoneNumberBtn").val("취소");

		// 핸드폰번호 공백들 제거값 설정
		$("#memberPhoneNumber").val($("#memberPhoneNumber").val().replace(/\s+/g, ""));

		// 숫자만 포함되어 있는지 확인 (정규식 사용)
		const phoneNumberPattern = /^(?!^(\d)\1{10}$)\d{11}$/;
		if (!(phoneNumberPattern.test($("#memberPhoneNumber").val()))) {
			console.log("핸드폰번호 숫자 이외의 것 포함.");
			alert("숫자로만 이루어진 동일하지 않은 11자리여야합니다.");
			return;
		}

		// 핸드폰번호 입력칸 disabled 속성 추가
		$("#memberPhoneNumber").prop("disabled", true);
		// 핸드폰 인증번호 입력칸 disabled 속성 제거
		$("#phoneNumberConfirmNumber").prop("disabled", false);
		// 인증번호 확인 버튼 hidden 속성 변경
		$("#checkConfirmPhoneNumberBtn").prop("type", "button");
		// 발급 받은 핸드폰 인증 번호
		confirmNumber = 1234;
		console.log("발급 받은 핸드폰 인증 번호 : [" + confirmNumber + "]");
		// 타이머 시작
		startPhoneNumberConfirmTimer();
		// 타이머 표시
		$("#confirmTimer").show();
	}
	else if ($("#confirmPhoneNumberBtn").val() === "취소") { // 인증 취소라면
		// 핸드폰번호 입력칸 disabled 속성 제거
		$("#memberPhoneNumber").prop("disabled", false);
		// 핸드폰 인증번호 입력칸 disabled 속성 추가
		$("#phoneNumberConfirmNumber").prop("disabled", true);
		// 인증번호 확인 버튼 button 속성 변경
		$("#checkConfirmPhoneNumberBtn").prop("type", "hidden");

		// 타이머 초기화
		clearInterval(timer);
		// 타이머 숨기기
		$("#confirmTimer").hide();
		// 버튼명 변경
		$("#confirmPhoneNumberBtn").val("인증");
	}
}

// 인증번호 확인 기능
const checkConfirmPhoneNumber = () => {
	console.log("인증번호 확인 실행");
	console.log("인증번호 확인할 현재 인증번호 : [" + confirmNumber + "]");
	console.log("인증번호 확인할 입력받은 인증번호 : [" + $("#phoneNumberConfirmNumber").val() + "]");
	if (Number($("#phoneNumberConfirmNumber").val()) === confirmNumber && confirmNumber !== "0") { // 인증번호가 맞다면
		// 타이머 초기화
		clearInterval(timer);

		alert("인증이 완료되었습니다.");

		// 핸드폰번호 입력칸 disabled 속성 제거
		$("#memberPhoneNumber").prop("disabled", false);
		// 핸드폰 인증번호 입력칸 disabled 속성 추가
		$("#phoneNumberConfirmNumber").val("").prop("disabled", true);
		// 인증번호 확인 버튼 button 속성 변경
		$("#checkConfirmPhoneNumberBtn").prop("type", "hidden");

		// 버튼명 변경
		$("#confirmPhoneNumberBtn").val("인증 완료");
		// 타이머 숨기기
		$("#confirmTimer").hide();
	}
	else { // 인증번호가 다르다면
		alert("잘못된 인증번호입니다.");
		// 인증번호 입력칸 값 비우기
		$("#phoneNumberConfirmNumber").val("");
		// 인증 취소 처리
		$("#confirmPhoneNumberBtn").val("취소");
		confirmPhoneNumber();
	}
}

// 타이머 시작 기능
const startPhoneNumberConfirmTimer = () => {
	clearInterval(timer);

	let timeLeft = 300; // 5분 (300초)로 설정

	timer = setInterval(() => {
		// 남은 시간 초단위로 표시
		let minutes = Math.floor(timeLeft / 60); // 분 계산
		let seconds = timeLeft % 60; // 초 계산

		// "4:56" 형식으로 표시
		$("#confirmTimer").text(`${minutes}:${seconds < 10 ? '0' : ''}${seconds}`);

		// 시간이 다 되면 타이머 종료
		if (timeLeft <= 0) {
			console.log("핸드폰 인증 타이머 종료");
			clearInterval(timer);
			alert("인증 시간이 지났습니다.");
			// 인증 취소 처리
			$("#confirmPhoneNumberBtn").val("취소");
			confirmPhoneNumber();
		}

		timeLeft--;
	}, 1000); // 1초마다 업데이트
}

// 본인확인 기능
const checkLoginedMember = () => {
	return new Promise((resolve) => {
		let loginedMemberPassword = prompt("본인인증을 위해 비밀번호를 입력하세요");

		// 사용자가 취소 버튼을 눌렀을 경우
		if (loginedMemberPassword === null) {
			console.log("본인인증 입력 취소됨");
			alert("본인 인증이 취소되었습니다.");
			resolve(false);
			return;
		}

		console.log("입력받은 본인확인 비밀번호 : [" + loginedMemberPassword + "]");

		$.ajax({
			type: "POST", // 방식
			url: checkLoginedMemberUrl, // 찾아갈 주소
			data: { loginedMemberPassword: loginedMemberPassword }, // 보낼 값
			dataType: "text", // 받을 타입
			success: (response) => { // 성공적이라면
				if (response === "true") { // 인증 성공 시
					console.log("인증 성공");
					resolve(true);
				} else { // 인증 실패 시
					console.log("인증 실패");
					alert("본인 인증에 실패하셨습니다. 비밀번호를 확인해주세요.");
					resolve(false);
				}
			},
			error: (xhr, status, error) => { // 에러 처리
				console.error("AJAX 요청 에러 발생", xhr.status, status, error);
				alert("서버에 문제가 발생했습니다. 지속될 시 관리자에게 문의하세요.");
				// 에러 상황에서도 흐름을 멈추지 않도록 false 반환
				resolve(false);
				location.href = "error.do";
			}
		});
	});
};

// 폼 데이터와 원본 데이터 비교 함수
const isFormModified = () => {
	let isModified = false;

	// 각 form 필드를 가져와서 비교
	$('#memberInfoForm input').each(function() {
		const originalValue = $(this).data('original'); // data-original에서 원본 값 가져오기
		const currentValue = $(this).val(); // 현재 form 입력값

		if (currentValue !== originalValue) { // 값이 하나라도 다르면
			isModified = true; // 수정된 것으로 판단
		}
	});

	return isModified;
};

// 주문 내역 페이지네이션 페이지 이동 기능
const changePurchasePage = (element) => {
	console.log("주문 내역 페이지네이션 클릭 번호 : [" + element.data('page') + "]");
	console.log("주문 내역 페이지네이션 클릭 아이디 : [" + element.attr('id') + "]");

	if (element.attr('id') === 'purchasePrevious') { // "<" 버튼 클릭 시
		purchasePageNumber--;
	} else if (element.attr('id') === 'purchaseNext') { // ">" 버튼 클릭 시
		purchasePageNumber++;
	} else { // 페이지 번호 클릭 시
		purchasePageNumber = element.data('page');
	}

	// 주문 내역 불러오기 함수 호출
	loadPurchaseDatas();
}

// 주문 내역 불러오기 기능
const loadPurchaseDatas = () => {
	$.ajax({ // 비동기
		url: loadMorePurchaseUrl, // 보낼 주소
		type: 'POST', // 방식
		data: { // 보낼 값
			purchasePageNumber: purchasePageNumber,
			purchaseContentCount: purchaseContentCount
		},
		dataType: 'json', // 받을 타입
		success: (response) => { // 성공적이라면
			console.log("받은 본인 작성 글 데이터 : [" + response + "]"); // 로그 찍기

			// 주문 내역 생성 함수 호출
			insertPurchase(response);
		},
		error: (xhr, status, error) => { // 에러 처리
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			alert("서버에 문제가 발생했습니다. 지속될 시 관리자에게 문의하세요.");
			location.href = "error.do";
		}
	});
}

// 반복하며 주문 내역 생성 기능
const insertPurchase = (response) => {
	console.log("받은 주문 내역 생성 정보 : [" + response + "]");
	const purchaseWrapper = $("#purchaseWrapper");

	// 주문 내역 초기화
	purchaseWrapper.empty();

	if (response.length === 0) { // 응답이 비어있다면
		console.log("주문 내역 존재하지 않음");

		// 주문 내역 비우고 생성
		purchaseWrapper.append(`
			<li>
				<a href="javascript:void(0);">
					주문 내역이 없습니다.
				</a>
			</li>
		`);

		// 주문 내역 페이지네이션 생성 함수 호출
		makePurchasePageNation(1);
		return;
	}

	// 반복하며 주문 내역 생성
	response.forEach(purchaseData => {
		purchaseWrapper.append(`
			<li>
				<a href="purchaseDetail.do?purchaseNumber=${purchaseData.purchaseNumber}">
				${purchaseData.purchaseTerminalId}
				<span class="last">${purchaseData.purchaseTotalPrice}</span>
				</a>
			</li>
		`);
	});

	// 주문 내역 페이지네이션 생성 함수 호출
	makePurchasePageNation(response[0].totalCountNumber);
}

// 주문 내역 페이지네이션 생성 기능
function makePurchasePageNation(totalCountNumber) {
	console.log("주문 내역 총 데이터 수 : [" + totalCountNumber + "]");
	const totalPageNumber = Math.ceil(totalCountNumber / purchaseContentCount); // 총 페이지 수
	const purchasePageNation = $("#purchasePageNation");
	let content = "";
	console.log("주문 내역 총 페이지 수 : [" + totalPageNumber + "]");

	let group = Math.ceil(purchasePageNumber / pageGroupSize); // 현재 그룹
	let startPage = (group - 1) * pageGroupSize + 1;
	let endPage = Math.min(group * pageGroupSize, totalPageNumber);

	let prevClass = purchasePageNumber <= 1 ? 'disabled-link' : '';
	let nextClass = purchasePageNumber >= totalPageNumber ? 'disabled-link' : '';

	console.log("현재 페이지 수 : [" + purchasePageNumber + "]");
	console.log("이전 버튼 값 : [" + prevClass + "]");
	console.log("다음 버튼 값 : [" + nextClass + "]");

	// 이전 페이지 버튼 추가
	content += `
	    <li class="page-item">
	        <a class="page-link purchasePage `+ prevClass + `" href="javascript:void(0);" aria-label="Previous" id="purchasePrevious">
	            <i class="ti-angle-left"></i>
	        </a>
	    </li>
	`;

	// 페이지 숫자 버튼 추가
	for (let i = startPage; i <= endPage; i++) {
		console.log("i 값: " + i);
		let activeClass = purchasePageNumber == i ? 'active' : '';
		content += `
	    	<li class="page-item `+ activeClass + `">
	    		<a class="page-link purchasePage" href="javascript:void(0);" data-page=` + i + `>` + i + `</a>
	    	</li>
	    `;
	}

	// 다음 페이지 버튼 추가
	content += `
	    <li class="page-item">
	        <a class="page-link purchasePage `+ nextClass + `" href="javascript:void(0);" aria-label="Next" id="purchaseNext" >
	            <i class="ti-angle-right"></i>
	        </a>
	    </li>
	`;

	// 주문 내역 페이지네이션 비우고 새로 생성
	purchasePageNation.empty().append(content);
}

// 좋아요 글 페이지네이션 페이지 이동 기능
const changeLikedBoardPage = (element) => {
	console.log("좋아요 글 페이지네이션 클릭 번호 : [" + element.data('page') + "]");
	console.log("좋아요 글 페이지네이션 클릭 아이디 : [" + element.attr('id') + "]");

	if (element.attr('id') === 'likedBoardPrevious') { // "<" 버튼 클릭 시
		likedBoardPageNumber--;
	} else if (element.attr('id') === 'likedBoardNext') { // ">" 버튼 클릭 시
		likedBoardPageNumber++;
	} else { // 페이지 번호 클릭 시
		likedBoardPageNumber = element.data('page');
	}

	// 좋아요 글 불러오기 함수 호출
	loadLikedBoardDatas();
}

// 좋아요 글 불러오기 기능
const loadLikedBoardDatas = () => {
	$.ajax({ // 비동기
		url: loadMoreLikedBoardUrl, // 보낼 주소
		type: 'POST', // 방식
		data: { // 보낼 값
			likedBoardPageNumber: likedBoardPageNumber,
			likedBoardContentCount: likedBoardContentCount
		},
		dataType: 'json', // 받을 타입
		success: (response) => { // 성공적이라면
			console.log("받은 본인 작성 글 데이터 : [" + response + "]"); // 로그 찍기

			// 좋아요 글 생성 함수 호출
			insertLikedBoard(response);
		},
		error: (xhr, status, error) => { // 에러 처리
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			alert("서버에 문제가 발생했습니다. 지속될 시 관리자에게 문의하세요.");
			location.href = "error.do";
		}
	});
}

// 반복하며 좋아요 글 생성 기능
const insertLikedBoard = (response) => {
	console.log("받은 본인 작성 글 생성 정보 : [" + response + "]");
	const likedBoardWrapper = $("#likedBoardWrapper");

	// 좋아요 글 초기화
	likedBoardWrapper.empty();

	if (response.length === 0) { // 응답이 비어있다면
		console.log("좋아요 글 존재하지 않음");

		// 좋아요 글 비우고 생성
		likedBoardWrapper.append(`
			<li>
				<a href="javascript:void(0);">
					좋아요 글이 없습니다.
				</a>
			</li>
		`);

		// 좋아요 글 페이지네이션 생성 함수 호출
		makelikedBoardPageNation(1);
		return;
	}

	// 반복하며 좋아요 글 생성
	response.forEach(likedBoardData => {
		likedBoardWrapper.append(`
			<li>
				<a href="boardDetail.do?boardComboNumber=${likedBoardData.boardComboNumber}">${likedBoardData.boardComboNumber}
					<span class="last">${likedBoardData.memberName}</span>
				</a>
			</li>
		`);
	});

	// 좋아요 글 페이지네이션 생성 함수 호출
	makelikedBoardPageNation(response[0].totalCountNumber);
}

// 좋아요 글 페이지네이션 생성 기능
function makelikedBoardPageNation(totalCountNumber) {
	console.log("좋아요 글 총 데이터 수 : [" + totalCountNumber + "]");
	const totalPageNumber = Math.ceil(totalCountNumber / likedBoardContentCount); // 총 페이지 수
	const likedBoardPageNation = $("#likedBoardPageNation");
	let content = "";
	console.log("좋아요 글 총 페이지 수 : [" + totalPageNumber + "]");

	let group = Math.ceil(likedBoardPageNumber / pageGroupSize); // 현재 그룹
	let startPage = (group - 1) * pageGroupSize + 1;
	let endPage = Math.min(group * pageGroupSize, totalPageNumber);

	let prevClass = likedBoardPageNumber <= 1 ? 'disabled-link' : '';
	let nextClass = likedBoardPageNumber >= totalPageNumber ? 'disabled-link' : '';

	console.log("현재 페이지 수 : [" + likedBoardPageNumber + "]");
	console.log("이전 버튼 값 : [" + prevClass + "]");
	console.log("다음 버튼 값 : [" + nextClass + "]");

	// 이전 페이지 버튼 추가
	content += `
	    <li class="page-item">
	        <a class="page-link likedBoardPage `+ prevClass + `" href="javascript:void(0);" aria-label="Previous" id="likedBoardPrevious">
	            <i class="ti-angle-left"></i>
	        </a>
	    </li>
	`;

	// 페이지 숫자 버튼 추가
	for (let i = startPage; i <= endPage; i++) {
		console.log("i 값: " + i);
		let activeClass = likedBoardPageNumber == i ? 'active' : '';
		content += `
	    	<li class="page-item `+ activeClass + `">
	    		<a class="page-link likedBoardPage" href="javascript:void(0);" data-page=` + i + `>` + i + `</a>
	    	</li>
	    `;
	}

	// 다음 페이지 버튼 추가
	content += `
	    <li class="page-item">
	        <a class="page-link likedBoardPage `+ nextClass + `" href="javascript:void(0);" aria-label="Next" id="likedBoardNext" >
	            <i class="ti-angle-right"></i>
	        </a>
	    </li>
	`;

	// 좋아요 글 페이지네이션 비우고 새로 생성
	likedBoardPageNation.empty().append(content);
}

// 본인 작성 글 페이지네이션 페이지 이동 기능
const changemyBoardPage = (element) => {
	console.log("본인 작성 글 페이지네이션 클릭 번호 : [" + element.data('page') + "]");
	console.log("본인 작성 글 페이지네이션 클릭 아이디 : [" + element.attr('id') + "]");

	if (element.attr('id') === 'myBoardPrevious') { // "<" 버튼 클릭 시
		myBoardPageNumber--;
	} else if (element.attr('id') === 'myBoardNext') { // ">" 버튼 클릭 시
		myBoardPageNumber++;
	} else { // 페이지 번호 클릭 시
		myBoardPageNumber = element.data('page');
	}

	// 본인 작성 글 불러오기 함수 호출
	loadMyBoardDatas();
}

// 본인 작성 글 불러오기 기능
const loadMyBoardDatas = () => {
	$.ajax({ // 비동기
		url: loadMoreMyBoardUrl, // 보낼 주소
		type: 'POST', // 방식
		data: { // 보낼 값
			myBoardPageNumber: myBoardPageNumber,
			myBoardContentCount: myBoardContentCount
		},
		dataType: 'json', // 받을 타입
		success: (response) => { // 성공적이라면
			console.log("받은 본인 작성 글 데이터 : [" + response + "]"); // 로그 찍기
			// 본인 작성 글 생성 함수 호출
			insertMyBoard(response);
		},
		error: (xhr, status, error) => { // 에러 처리
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			alert("서버에 문제가 발생했습니다. 지속될 시 관리자에게 문의하세요.");
			location.href = "error.do";
		}
	});
}

// 반복하며 본인 작성 글 생성 기능
const insertMyBoard = (response) => {
	console.log("받은 본인 작성 글 생성 정보 : [" + response + "]");
	const myBoardWrapper = $("#boardTable tbody");
	// 본인 작성 글 초기화
	myBoardWrapper.empty();

	if (response.length === 0) { // 응답이 비어있다면
		console.log("본인 작성 글 존재하지 않음");

		// 본인 작성 글 생성
		myBoardWrapper.append(`
			<tr>
	        <td colspan="5">작성하신 글이 없습니다.</td>
			</tr>
		`);

		// 본인 작성 글 페이지네이션 생성 함수 호출
		makemyBoardPageNation(1);
		return;
	}

	// 반복하며 본인 작성 글 생성
	response.forEach(myBoardData => {
		myBoardWrapper.append(`
			<tr onclick="location.href='boardDetail.do?boardComboNumber=`+ myBoardData.boardComboNumber + `'">
		        <td>`+ myBoardData.boardComboNumber + `</td>
		        <td>`+ myBoardData.boardComboTitle + `</td>
		        <td>`+ myBoardData.boardComboViewCount + `</td>
		        <td>`+ myBoardData.boardComboLikedCount + `</td>
		        <td>`+ myBoardData.boardComboRegisterDate + `</td>
			</tr>
		`);
	});

	// 본인 작성 글 페이지네이션 생성 함수 호출
	makemyBoardPageNation(response[0].totalCountNumber);
}

// 본인 작성 글 페이지네이션 생성 기능
function makemyBoardPageNation(totalCountNumber) {
	console.log("본인 작성 글 총 데이터 수 : [" + totalCountNumber + "]");
	const totalPageNumber = Math.ceil(totalCountNumber / myBoardContentCount); // 총 페이지 수
	const myBoardPageNation = $("#myBoardPageNation");
	let content = "";
	console.log("본인 작성 글 총 페이지 수 : [" + totalPageNumber + "]");

	let group = Math.ceil(myBoardPageNumber / pageGroupSize); // 현재 그룹
	let startPage = (group - 1) * pageGroupSize + 1;
	let endPage = Math.min(group * pageGroupSize, totalPageNumber);

	let prevClass = myBoardPageNumber <= 1 ? 'disabled-link' : '';
	let nextClass = myBoardPageNumber >= totalPageNumber ? 'disabled-link' : '';

	console.log("현재 페이지 수 : [" + myBoardPageNumber + "]");
	console.log("이전 버튼 값 : [" + prevClass + "]");
	console.log("다음 버튼 값 : [" + nextClass + "]");

	// 이전 페이지 버튼 추가
	content += `
	    <li class="page-item">
	        <a class="page-link boardPage `+ prevClass + `" href="javascript:void(0);" aria-label="Previous" id="myBoardPrevious">
	            <i class="ti-angle-left"></i>
	        </a>
	    </li>
	`;

	// 페이지 숫자 버튼 추가
	for (let i = startPage; i <= endPage; i++) {
		console.log("i 값: " + i);
		let activeClass = myBoardPageNumber == i ? 'active' : '';
		content += `
	    	<li class="page-item `+ activeClass + `">
	    		<a class="page-link boardPage" href="javascript:void(0);" data-page=` + i + `>` + i + `</a>
	    	</li>
	    `;
	}

	// 다음 페이지 버튼 추가
	content += `
	    <li class="page-item">
	        <a class="page-link boardPage `+ nextClass + `" href="javascript:void(0);" aria-label="Next" id="myBoardNext" >
	            <i class="ti-angle-right"></i>
	        </a>
	    </li>
	`;

	// 본인 작성 글 페이지네이션 비우고 새로 생성
	myBoardPageNation.empty().append(content);
}

// 회원 탈퇴 기능
const withdraw = () => {
	console.log("회원 탈퇴 실행");

	const answer = confirm("정말로 계정을 삭제하시겠습니까?\n삭제된 계정 정보는 복구하실 수 없습니다.");
	console.log("탈퇴 확인 여부 : [" + answer + "]")
	if (!answer) {
		return;
	}

	// 본인 인증 확인
	checkLoginedMember().then((isAuthenticated) => {
		if (!isAuthenticated) { // 본인 인증 실패 시
			return;
		}
		$.ajax({
			type: "POST", // 방식
			url: withdrawUrl, // 찾아갈 주소
			data: {}, // 보낼 값
			dataType: "text", // 받을 타입
			success: (response) => { // 성공적이라면
				if (response === "true") { // 탈퇴 성공 시
					console.log("탈퇴 성공");
					alert("다시 뵙기를 고대하겠습니다. 감사합니다.");
					sessionStorage.removeItem("loginedMemberNumber");
					location.href = "logout.did";
				}
				else { // 탈퇴 실패 시
					console.log("탈퇴 실패");
					alert("회원 탈퇴에 실패하였습니다.");
					location.href = "error.do";
				}
			},
			error: (xhr, status, error) => { // 에러 처리
				console.error("AJAX 요청 에러 발생", xhr.status, status, error);
				alert("서버에 문제가 발생했습니다. 지속될 시 관리자에게 문의하세요.");
				location.href = "error.do";
			}
		});
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