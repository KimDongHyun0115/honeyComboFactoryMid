const findMemberIdUrl = "/honeyComboFactory/FindIdServlet";
const findMemberPasswordUrl = "/honeyComboFactory/FindPasswordServlet";
const updateMemberPasswordUrl = "/honeyComboFactory/ChangePasswordServlet";
let timer;
let sendingConfirmNumber = 0;

$(document).ready(function () {
	const loginedMemberNumber = sessionStorage.getItem('loginedMemberNumber');
	console.log("회원가입 페이지 접근 시 로그인 정보 : [" + loginedMemberNumber + "]");

	if (loginedMemberNumber) {
		alert("올바르지 않은 접근입니다. 메인페이지로 이동합니다.");
		location.href = "main.do";
	}

	limitInputBirth();
	updateAuthMethodVisibility(); 

	$('input[name="idAuthMethod"]').on('change', updateAuthMethodVisibility);
	$('input[name="passwordAuthMethod"]').on('change', updateAuthMethodVisibility); 

	$('#password-form .submit-btn').on('click', function (event) {
		event.preventDefault();
		if (!this.checkValidity()) {
			event.stopPropagation();
		} else {
			const authMethod = $("input[name='passwordAuthMethod']:checked").val();
			let confirmBtn = authMethod === "phoneNum" ? $("#confirmPhoneNumberBtnByPassword") : $("#confirmEmailBtnByPassword");
			if (confirmBtn.text() !== "재인증") {
				alert("본인 인증을 진행해주세요!");
				return;
			}
			findMemberPassword();
		}
	});

	$('#id-form .submit-btn').on('click', function (event) {
		event.preventDefault();
		if (!this.checkValidity()) {
			event.stopPropagation();
		} else {
			const authMethod = $("input[name='idAuthMethod']:checked").val();
			let confirmBtn = authMethod === "phoneNum" ? $("#confirmPhoneNumberBtnById") : $("#confirmEmailBtnById");
			if (confirmBtn.text() !== "재인증") {
				alert("본인 인증을 진행해주세요!");
				return;
			}
			findMemberId();
		}
	});
});

// ✅ 인증 방식 선택 시 input 표시 제어 함수
const updateAuthMethodVisibility = () => {
	const idAuth = $("input[name='idAuthMethod']:checked").val();
	const pwAuth = $("input[name='passwordAuthMethod']:checked").val();

	if (idAuth === "phoneNum") {
		$('#memberPhoneNumberById').closest('.form-group').removeClass('hidden');
		$('#memberEmailById').closest('.form-group').addClass('hidden');
	} else {
		$('#memberPhoneNumberById').closest('.form-group').addClass('hidden');
		$('#memberEmailById').closest('.form-group').removeClass('hidden');
	}

	if (pwAuth === "phoneNum") {
		$('#memberPhoneNumberByPassword').closest('.form-group').removeClass('hidden');
		$('#memberEmailByPassword').closest('.form-group').addClass('hidden');
	} else {
		$('#memberPhoneNumberByPassword').closest('.form-group').addClass('hidden');
		$('#memberEmailByPassword').closest('.form-group').removeClass('hidden');
	}
};

const stopBtn = (isSendingConfirmNumber) => {
	$("#tab-id").css("pointer-events", isSendingConfirmNumber ? "none" : "auto");
	$("#tab-password").css("pointer-events", isSendingConfirmNumber ? "none" : "auto");
	$('input[type="radio"]').prop('disabled', isSendingConfirmNumber);
	$('button.submit-btn').prop('disabled', isSendingConfirmNumber);
};

const checkConfirmNumber = (findType) => {
	console.log("인증번호 확인 종류 : [" + findType + "]");
	if (sendingConfirmNumber == 0) {
		alert("인증번호 전송 버튼을 눌러주세요.");
		return;
	}

	let inputConfirm = findType === "id" ? $("#confirmById") : $("#confirmByPassword");
	if (sendingConfirmNumber != inputConfirm.val()) {
		alert("옳지 않은 인증번호입니다.");
		return;
	}

	const authMethod = $("input[name='" + findType + "AuthMethod']:checked").val();
	let timerElement = findType === "id" ? $("#timerById") : $("#timerByPassword");
	let sendingconfirm = findType === "id" ? $("#sendingconfirmById") : $("#sendingconfirmByPassword");
	let confirmBtn = findType === "id" ?
		(authMethod === "phoneNum" ? $("#confirmPhoneNumberBtnById") : $("#confirmEmailBtnById"))
		: (authMethod === "phoneNum" ? $("#confirmPhoneNumberBtnByPassword") : $("#confirmEmailBtnByPassword"));

	alert("성공적으로 인증됐습니다.");
	clearInterval(timer);
	timerElement.addClass("hidden");
	sendingconfirm.addClass("hidden");
	inputConfirm.val("").prop("readonly", true);
	sendingConfirmNumber = 0;
	$('button.submit-btn').prop('disabled', false);
	confirmBtn.text("재인증");
};

const sendConfirmNumber = (findType, event) => {
	console.log("인증할 찾기 종류 : [" + findType + "]");
	let authMethod = $("input[name='" + findType + "AuthMethod']:checked").val();
	let authValue = authMethod === 'phoneNum'
		? $('#' + (findType === "id" ? 'memberPhoneNumberById' : 'memberPhoneNumberByPassword')).val().trim()
		: $('#' + (findType === "id" ? 'memberEmailById' : 'memberEmailByPassword')).val().trim();
	let inputConfirm = $("#" + (findType === "id" ? "confirmById" : "confirmByPassword"));

	if ($(event).text() === "재인증") {
		$(event).text("인증번호 전송");
		stopBtn(false);
		return;
	}

	if (sendingConfirmNumber != 0) {
		$("#sendingconfirmBy" + (findType === "id" ? "Id" : "Password")).removeClass("hidden");
		return;
	}

	if (!checkAuthValue(authMethod, authValue)) return;

	if (authMethod === 'email') {
	const emailParts = authValue.split('@');
	const emailId = emailParts[0];
	const emailDomain = emailParts[1];
	const birth = findType === "id" ? $('#memberBirthById').val().trim() : $('#memberBirthByPassword').val().trim();

	$.ajax({
		type: "POST",
		url: "/honeyComboFactory/verifyEmailCode.do",
		data: {
			memberEmailId: emailId,
			memberEmailDomain: emailDomain,
			memberBirth: birth
		},
		dataType: "json",
		success: (res) => {
			alert(res.message); // 인증번호가 이메일로 전송되었습니다
			if (res.code) {
				sendingConfirmNumber = res.code; // 서버에서 받은 인증번호 저장
				console.log("서버로부터 받은 인증번호 : " + sendingConfirmNumber);
			}
			stopBtn(true);
			inputConfirm.prop("readonly", false);
			startPhoneNumberConfirmTimer(findType);
		},
		error: (xhr, status, error) => {
			console.error("이메일 인증 AJAX 에러 발생", xhr.status, status, error);
			alert("이메일 인증번호 전송 중 오류가 발생했습니다.");
			stopBtn(false);
		}
	});

	return; // 아래 코드 실행하지 않도록 막기
}

	stopBtn(true);
	inputConfirm.prop("readonly", false);
	startPhoneNumberConfirmTimer(findType);
};

const startPhoneNumberConfirmTimer = (findType) => {
	clearInterval(timer);
	let timeLeft = 300;
	timer = setInterval(() => {
		let minutes = Math.floor(timeLeft / 60);
		let seconds = timeLeft % 60;
		let timerElement = $("#" + (findType === "id" ? "timerById" : "timerByPassword"));
		let sendingconfirm = $("#" + (findType === "id" ? "sendingconfirmById" : "sendingconfirmByPassword"));
		let inputConfirm = $("#" + (findType === "id" ? "confirmById" : "confirmByPassword"));

		timerElement.text(`${minutes}:${seconds < 10 ? '0' : ''}${seconds}`).removeClass("hidden");

		if (timeLeft <= 0) {
			clearInterval(timer);
			alert("인증 시간이 지났습니다.");
			timerElement.addClass("hidden");
			sendingconfirm.addClass("hidden");
			inputConfirm.prop("readonly", true);
			sendingConfirmNumber = 0;
			stopBtn(false);
		}
		timeLeft--;
	}, 1000);
};

const checkAuthValue = (authMethod, authValue) => {
	const phoneNumPattern = /^(?!(\d)\1{10})\d{11}$/;
	const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

	if (!authValue) {
		alert("휴대폰번호 혹은 이메일 주소를 입력해주세요.");
		return false;
	}

	if (authMethod === "phoneNum" && !phoneNumPattern.test(authValue)) {
		alert("휴대폰번호를 모두 같지 않은 숫자로만 11자리 입력해주세요.");
		return false;
	}
	if (authMethod === "email" && !emailPattern.test(authValue)) {
		alert("옳은 이메일 주소 형태를 입력해주세요.");
		return false;
	}

	return true;
};

const findMemberPassword = () => {
	const memberId = $("#memberIdByPassword").val().trim();
	const memberBirth = $("#memberBirthByPassword").val().trim();
	const authMethod = $("input[name='passwordAuthMethod']:checked").val();
	const authValue = authMethod === 'phoneNum'
		? $('#memberPhoneNumberByPassword').val().trim()
		: $('#memberEmailByPassword').val().trim();

	if (!memberBirth || !authValue || !memberId) {
		alert("모든 정보를 입력해주세요.");
		return;
	}

	
	
	$.ajax({
		type: "POST",
		url: findMemberPasswordUrl,
		data: { memberId, memberBirth, authMethod, authValue },
		dataType: "text",
		success: (response) => {
			if (response === "true") {
				console.log("비밀번호 찾기 성공");
				changeMemberPassword();
			} else {
				alert("존재하지 않는 계정입니다.");
			}
		},
		error: () => {
			alert("서버 오류가 발생했습니다.");
			location.href = "error.do";
		}
	});
};

const findMemberId = () => {
	const memberBirth = $('#memberBirthById').val().trim();
	const authMethod = $("input[name='idAuthMethod']:checked").val();
	const authValue = authMethod === 'phoneNum'
		? $('#memberPhoneNumberById').val().trim()
		: $('#memberEmailById').val().trim();

	if (!memberBirth || !authValue) {
		alert("모든 정보를 입력해주세요.");
		return;
	}

	$.ajax({
		type: "POST",
		url: findMemberIdUrl,
		data: { memberBirth, authMethod, authValue },
		dataType: "text",
		success: (response) => {
			if (response) {
				alert("귀하의 아이디 : " + response);
				location.href = "main.do";
			} else {
				alert("존재하지 않는 계정입니다.");
			}
		},
		error: () => {
			alert("서버 오류가 발생했습니다.");
			location.href = "error.do";
		}
	});
};

const showTab = (tab) => {
	if (tab === 'id') {
		$('#id-form').removeClass('hidden');
		$('#password-form').addClass('hidden');
		$('#tab-id').addClass('active');
		$('#tab-password').removeClass('active');
	} else {
		$('#id-form').addClass('hidden');
		$('#password-form').removeClass('hidden');
		$('#tab-id').removeClass('active');
		$('#tab-password').addClass('active');
	}
};

const limitInputBirth = () => {
	const today = new Date();
	const todayDate = today.toISOString().split('T')[0];
	$("#memberBirthById").attr("max", todayDate);
	$("#memberBirthByPassword").attr("max", todayDate);
};

const changeMemberPassword = () => {
	const passwordPattern = /^(?!([\d])\1{5,})(?=.*[\W_])\S{6,15}$/;
	let attempts = 0;
	const maxAttempts = 5;
	let memberPassword;

	while (attempts < maxAttempts) {
		memberPassword = prompt("비밀번호를 입력하세요:\n(6~15자, 같은 숫자 연속 6개 이상 X, 특수문자 1개 이상 포함)");
		if (memberPassword === null) {
			alert("비밀번호 입력이 취소되었습니다.");
			return;
		}
		if (passwordPattern.test(memberPassword)) break;
		attempts++;
		alert(`비밀번호 형식이 올바르지 않습니다. (${attempts}/${maxAttempts})`);
	}

	if (attempts >= maxAttempts) {
		alert("최대 입력 횟수를 초과했습니다.");
		return;
	}

	if (!confirm("입력하신 비밀번호로 재설정하시겠습니까?")) {
		alert("비밀번호 재설정이 취소되었습니다.");
		return;
	}

	const memberId = $("#memberIdByPassword").val().trim();

	$.ajax({
		type: "POST",
		url: updateMemberPasswordUrl,
		data: {
			memberId: memberId,
			memberPassword: memberPassword
		},
		dataType: "text",
		success: (response) => {
			if (response === "true") {
				alert("비밀번호가 성공적으로 변경되었습니다.");
				location.href = "main.do";
			} else {
				alert("비밀번호 변경에 실패했습니다.");
				location.href = "error.do";
			}
		},
		error: () => {
			alert("서버 오류가 발생했습니다.");
			location.href = "error.do";
		}
	});
};
