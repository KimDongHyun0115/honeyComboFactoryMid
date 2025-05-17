/**
 *  사용자 정의 글 등록 js
 */

const updateBoardUrl = "/honeyComboFactory/MemberInserBoardServlet"; // 게시글 등록 서블릿 url

$(document).ready(function() {
	// 로그인 상태 확인
	const loginedMemberNumber = sessionStorage.getItem('loginedMemberNumber');
	console.log("게시글 등록 페이지 접근 시 로그인 정보 : [" + loginedMemberNumber + "]");

	if (!loginedMemberNumber) {  // 로그인되어 있지 않다면
		alert("로그인이 필요한 페이지입니다. 로그인 페이지로 이동합니다.");

		// 로그인 페이지로 이동
		location.href = "login.do";
	}

	// 글 등록 버튼 이벤트 위임
	$("#updateBoardBtn").on("click", function() {
		updateBoard();
	});
});

// 게시글 등록 기능
const updateBoard = () => {
	console.log("게시글 등록 실행");
	let updateBoardTitle = $("#updateBoardTitle").val();  // 제목 입력값을 가져옴
	let updateBoardContent = editorInstance.getData();  // CKEditor5에서 입력된 HTML 형식의 콘텐츠를 가져옴

	console.log("등록할 제목 : [" + updateBoardTitle + "]");
	console.log("등록할 내용 : [" + updateBoardContent + "]");

	if (!updateBoardTitle || !updateBoardContent.trim()) { // 제목이나 내용이 비어있다면
		alert("제목과 내용을 모두 입력해주세요.");
		return;
	}

	// Ajax를 사용하여 데이터 서버로 전송
	$.ajax({
		url: updateBoardUrl,  // 서버로 데이터를 전송할 API 경로
		type: "POST",  // HTTP 메서드: POST
		contentType: "application/x-www-form-urlencoded; charset=UTF-8",  // 콘텐츠 타입 설정
		data: {
			updateBoardTitle: updateBoardTitle,  // 제목 데이터 전송
			updateBoardContent: updateBoardContent,  // 내용 데이터 전송
		},
		dataType: "text",  // 서버 응답 형식 (여기서는 텍스트로 응답)
		success: function(response) {  // 서버로부터의 응답 처리
			if (response === "true") {  // 서버 응답이 "true"일 경우
				console.log("게시글 등록 성공");
				alert("게시글이 등록되었습니다.");
				location.href = "comboBoard.do";
			} else {
				console.log("게시글 등록 실패");  // 콘솔에 저장 실패 로그 출력
				alert("게시글 등록이 실패했습니다.");
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