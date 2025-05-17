/**
 *  게시글 상세 js
 */
const checkLikedUrl = "/honeyComboFactory/BoardComboLikedServlet"; // 좋아요 여부 받아오기 서블릿 url
const clickLikedUrl = "/honeyComboFactory/MemberLikedBoardServlet"; // 좋아요 여부 변경 서블릿 url
const deleteComboBoardUrl = "/honeyComboFactory/MemberDeleteBoardServlet"; // 게시글 삭제 서블릿 url
const boardComboNumber = document.body.dataset.boardNumber; // jsp 파일의 게시글 번호 가져오기
let boardLikedCount = document.body.dataset.boardLikedCount; // jsp 파일의 게시글 좋아요 수 가져오기
let loginedMemberNumber; // 로그인한 회원 번호
let isLiked = false; // 게시글 좋아요 여부

$(document).ready(function() {
	$(document).on("click", "#likeBtn", function(event) { // 좋아요 클릭 시
		event.preventDefault(); // 기본 이벤트 방지

		clickLiked(); // 좋아요 여부 변경 함수 호출
	});

	// 로그인 상태 확인
	loginedMemberNumber = sessionStorage.getItem('loginedMemberNumber');
	console.log("꿀조합 게시글 상세 페이지 접근 시 로그인 정보 : [" + loginedMemberNumber + "]");

	// 좋아요 여부 불러오기 함수 호출
	checkLiked();
});

// 좋아요 여부 받아오기 기능
const checkLiked = () => {
	console.log("좋아요 여부 받아올 게시글 번호 : [" + boardComboNumber + "]");

	if (!loginedMemberNumber) { // 비회원이라면
		createLikedBtn();
		return;
	}

	$.ajax({ // 비동기
		url: checkLikedUrl, // 보낼 주소
		type: 'POST', // 방식
		data: { boardComboNumber: boardComboNumber }, // 보낼 값
		dataType: 'text', // 받을 타입
		success: (response) => { // 성공적이라면
			console.log("받은 글 좋아요 여부 정보 : [" + response + "]"); // 로그 찍기
			if (response === "true") {
				isLiked = true;
			}
			// 좋아요 버튼 생성 함수 호출
			createLikedBtn();
		},
		error: (xhr, status, error) => { // 에러 처리
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			alert("서버에 문제가 발생했습니다. 지속될 시 관리자에게 문의하세요.");
			location.href = "error.do";
		}
	});
}

// 좋아요 버튼 생성 기능
const createLikedBtn = () => {
	console.log("좋아요 버튼 생성 시 좋아요 여부 : [" + isLiked + "]");
	console.log("좋아요 버튼 생성 시 좋아요 수 : [" + boardLikedCount + "]");
	// 좋아요 버튼 내용 비우기
	$("#likedBtnWrapper").empty();
	let contentText = "";

	if (isLiked) { // 좋아요 상태라면
		contentText = "좋아요 취소";
	}
	else { // 좋아요 상태가 아니라면
		contentText = `좋아요`;
	}

	// 화면에 생성
	$("#likedBtnWrapper").append(`
		<a href="javascript:void(0);" 
		id="likeBtn" class="genric-btn primary-border e-large danger-border">
			<i class="fa fa-heart" aria-hidden="true"></i>
			<span>
			`+ contentText + `&nbsp;` + boardLikedCount + `
			</span>
		</a>
	`);
}

// 좋아요 여부 변경 기능
const clickLiked = () => {
	let likedCondition = "";
	console.log("좋아요 버튼 클릭");
	console.log("좋아요 버튼에 요청받은 글 번호 : [" + boardComboNumber + "]");
	console.log("좋아요 버튼에 요청받은 회원 번호 : [" + loginedMemberNumber + "]");
	console.log("좋아요 버튼에 요청받은 좋아요 여부 : [" + isLiked + "]");

	if (!loginedMemberNumber) { // 비회원이라면
		alert("로그인이 필요한 기능입니다.");
		return;
	}

	if (isLiked) { // 좋아요 글이라면
		likedCondition = "DELETELIKED";
	}
	else { // 좋아요 글이 아니라면
		likedCondition = "INSERTLIKED";
	}
	console.log("좋아요 등록/취소 조건 : [" + likedCondition + "]");

	$.ajax({ // 비동기
		url: clickLikedUrl, // 보낼 주소
		type: 'POST', // 방식
		data: { // 보낼 값
			boardComboNumber: boardComboNumber,
			likedCondition: likedCondition
		},
		dataType: 'text', // 받을 타입
		success: (response) => { // 성공적이라면
			console.log("받은 좋아요 여부 : [" + response + "]"); // 로그 찍기

			if (response === "false") { // 좋아요 여부 변경 실패라면
				console.log("좋아요 여부 변경 실패");
				alert("좋아요 변경이 실패했습니다.");
				location.href = "error.do"; // 에러 페이지 이동
				return;
			}

			isLiked = !isLiked; // 회원 좋아요 여부 값 변경
			boardLikedCount = response // 글 좋아요 수 값 변경
			// 좋아요 버튼 생성 함수 호출
			createLikedBtn();
		},
		error: (xhr, status, error) => { // 에러 처리
			console.error("AJAX 요청 에러 발생", xhr.status, status, error);
			alert("서버에 문제가 발생했습니다. 지속될 시 관리자에게 문의하세요.");
			location.href = "error.do";
		}
	});
}

// 꿀조합 게시글 삭제 기능
const deleteComboBoard = (boardComboNumber) => {
	console.log("삭제할 게시글 번호 : [" + boardComboNumber + "]");

	const answer = confirm("정말로 작성하신 글을 삭제하시겠습니까?\n삭제된 글은 복구하실 수 없습니다.");
	console.log("삭제 확인 여부 : [" + answer + "]")
	if (!answer) {
		return;
	}

	$.ajax({ // 비동기
		url: deleteComboBoardUrl, // 보낼 주소
		type: 'POST', // 방식
		data: { boardComboNumber: boardComboNumber }, // 보낼 값
		dataType: 'text', // 받을 타입
		success: (response) => { // 성공적이라면
			console.log("받은 글 삭제 성공 정보 : [" + response + "]"); // 로그 찍기
			if (response === "true") { // 게시글 삭제 성공 시
				console.log("게시글 삭제 성공");
				alert(boardComboNumber + "번 꿀조합 게시글이 삭제되었습니다.");
				location.href = "comboBoard.do"; // 꿀조합 게시판 이동
			}
			else { // 게시글 삭제 실패 시
				console.log("게시글 삭제 실패");
				alert(boardComboNumber + "번 꿀조합 게시글 삭제가 실패했습니다.");
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

// 꿀조합 게시글 내용 수정 기능
const updateComboBoard = (boardComboNumber, boardComboContent) => {
	console.log("수정할 게시글 번호 : [" + boardComboNumber + "]");
	console.log("수정할 게시글 내용 : [" + boardComboContent + "]");
}