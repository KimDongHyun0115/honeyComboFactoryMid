package controller.common;

import java.util.HashMap;
import java.util.Map;

import controller.member.action.DeleteShoppingCartAction;
import controller.member.action.LogoutAction;
import controller.move.action.BoardComboMoveAction;
import controller.move.action.BoardDetailMoveAction;
import controller.move.action.ComboProductMoveAction;
import controller.move.action.CuProductMoveAction;
import controller.move.action.ErrorMoveAction;
import controller.move.action.FindAccountMoveAction;
import controller.move.action.GsProductMoveAction;
import controller.move.action.JoinMoveAction;
import controller.move.action.LoginMoveAction;
import controller.move.action.MainMoveAction;
import controller.move.action.MemberUpdateBoardMoveAction;
import controller.move.action.MyPageMoveAction;
import controller.move.action.ProductDetailMoveAction;
import controller.move.action.PurchaseDetailMoveAction;
import controller.move.action.ShoppingCartMoveAction;

public class ActionFactory {

	private Map<String, Action> factory;

	public ActionFactory() {
		factory = new HashMap<String, Action>();

		System.out.println("ACTION FACTORY 도착 여부");

//		LoginServlet la=new LoginServlet();

		// 단순 페이지 이동 ↓
		// 회원 페이지 이동
		factory.put("client/comboBoard.do", new BoardComboMoveAction()); // 꿀조합 게시판페이지 이동
		factory.put("client/boardDetail.do", new BoardDetailMoveAction()); // 글 상세페이지로 이동
		factory.put("client/comboProduct.do", new ComboProductMoveAction()); // 꿀조합 상품 페이지로 이동
		factory.put("client/CUProduct.do", new CuProductMoveAction()); // CU 상품 페이지로 이동
		factory.put("client/findAccount.do", new FindAccountMoveAction()); // 계정 찾기 페이지로 이동
		factory.put("client/GSProduct.do", new GsProductMoveAction()); // GS 상품 페이지로 이동
		factory.put("client/join.do", new JoinMoveAction()); // 회원가입 페이지로 이동
		factory.put("client/login.do", new LoginMoveAction()); // 로그인 페이지로 이동
		factory.put("client/main.do", new MainMoveAction()); // 메인페이지로 이동
		factory.put("client/myInfo.do", new MyPageMoveAction()); // 마이페이지로 이동
		factory.put("client/productDetail.do", new ProductDetailMoveAction()); // 상품 상세페이지로 이동
		factory.put("client/purchaseDetail.do", new PurchaseDetailMoveAction()); // 주문 상세페이지로 이동
		factory.put("client/cart.do", new ShoppingCartMoveAction()); // 장바구니로 이동
		factory.put("client/updateBoard.do", new MemberUpdateBoardMoveAction()); // 글 작성 페이지로 이동
		factory.put("client/error.do", new ErrorMoveAction());

//		factory.put("MEMBERUPDATEINFORMATIONMOVE", new MemberUpdateInformationMoveAction()); // 회원정보 수정 이동

//		// 관리자 페이지 이동
//		factory.put("ADDPRODUCTCOMBOMOVE", new AdminAddProductComboMoveAction()); // 관리자 상품추가 페이지 이동
//		factory.put("ADMINBOARDCOMBOMOVE", new AdminBoardComboMoveAction()); // 관리자 꿀조합게시판 페이지 이동
//		factory.put("ADMINBOARDNOTICEMOVE", new AdminBoardNoticeMoveAction()); // 관리자 공지사항게시판 페이지 이동
//		factory.put("ADMINCOMBOPRODUCTMOVE", new AdminComboProductMoveAction()); // 관리자 꿀조합상품 페이지 이동
//		factory.put("ADMINCUPRODUCTMOVE", new AdminCUProductMoveAction()); // 관리자 CU상품 페이지 이동
//		factory.put("ADMINGS25PRODUCTMOVE",new AdminGS25ProductMoveAction()); //관리자 GS상품 페이지 이동
//		factory.put("ADMINMAINMOVE", new AdminMainMoveAction()); // 관리자 메인페이지 이동
//		factory.put("ADMINWRITEBOARDNOTICEMOVE", new AdminWriteBoardNoticeMoveAction()); // 관리자 공지사항 글 작성 페이지 이동
//		// 관리자 회원관리 페이지 이동 누락

		// 단순 페이지 이동 끝 ↑

		// 기능 액션 ↓
		// 회원 액션
//		factory.put("client/boardComboPage.did", new BoardComboPageAction()); // 꿀조합 게시판
//		factory.put("client/detailBoard.did", new BoardDetailPageAction()); // 글 상세 페이지
//		factory.put("BOARDNOTICEPAGE", new BoardNoticePageAction()); // 공지사항 게시판
//		factory.put("client/addShoppingCart.did", new AddShoppingCartAction()); // 장바구니 상품 추가
		factory.put("client/deleteCartProduct.did", new DeleteShoppingCartAction()); // 장바구니 상품 삭제
//		factory.put("client/upDateNewPassowrd.did", new FindAccountAction()); // 아이디/비밀번호 찾기
//		factory.put("client/join.did", new JoinAction()); // 회원가입
//		factory.put("client/login.did", new LoginAction()); // 로그인
		factory.put("client/logout.did", new LogoutAction()); // 로그아웃
//		factory.put("client/main.did", new MainAction()); // 메인페이지
//		factory.put("client/memberMyPage.did", new MemberMyPageAction()); // 내 정보
//		factory.put("client/updateMyInfo.did", new MemberUpdateInformationAction()); // 회원정보 수정
//		factory.put("client/insertBoard.did", new MemberWriteBoardComboAction()); // 꿀조합 글 작성
//		factory.put("client/deleteBoard.did", new DeleteBoardComboAction()); // 꿀조합 글 삭제
//		factory.put("client/productcomboPage.did", new ProductComboPageAction()); // 꿀조합 페이지
//		factory.put("client/updateReviewAction.did", new UpdateReviewAction()); // 리뷰 등록
//		factory.put("client/cuProductSinglePage.did", new CuProductSinglePageAction()); // CU 개별상품 페이지
//		factory.put("client/gsProductSinglePage.did", new GsProductSinglePageAction()); // GS 개별상품 페이지
//		factory.put("client/detailPurchase.did", new PurchaseDetailAction()); // 주문상세 페이지
//		factory.put("client/liked.did", new BoardComboLikedAction()); // 좋아요
//		factory.put("client/unliked.did", new BoardComboUnlikedAction()); // 좋아요 취소
//		factory.put("purchaseCartProduct.did", null); //결제버튼 Action 만들어야함

//		// 관리자 액션
//		factory.put("ADMINBOARDCOMBO", new AdminBoardComboAction()); // 관리자 꿀조합게시판
//		factory.put("ADMINBOARDNOTICE", new AdminBoardNoticeAction()); // 관리자 공지사항 게시판
//		factory.put("ADMINDELETEBOARDCOMBO", new AdminDeleteBoardComboAction()); // 관리자 꿀조합 게시판 글 삭제
//		factory.put("ADMINDELETEBOARDNOTICE", new AdminDeleteBoardNoticeAction()); // 관리자 공지사항 게시판 글 삭제
//		factory.put("ADMINWRITEBOARDCOMBO", new AdminWriteBoardComboAction()); // 관리자 꿀조합 글 작성
//		factory.put("ADMINWRITEBOARDNOTICEACTION", new AdminWriteBoardNoticeAction()); // 관리자 공지사항 글 작성
//		factory.put("ADMINMAIN", new AdminMainAction()); // 관리자 메인페이지
//		factory.put("ADMINCLIENTMANAGEMENT", new AdminClientManagementAction()); // 관리자 회원가입 페이지
//		factory.put("ADMINADDPRODUCTCOMBOACTION", new AdminAddProductComboAction()); // 관리자 상품 추가
//		factory.put("ADMINCOMBOPRODUCT", new AdminComboProductAction()); // 관리자 꿀조합 상품 관리
//		factory.put("ADMINCUPRODUCT", new AdminCUProductAction()); // 관리자 CU상품 관리
//		factory.put("ADMINGS25PRODUCT", new AdminGS25ProductAction()); // 관리자 GS상품 관리

		// 기능 액션 끝 ↑

	}

	public Action getAction(String command) {
		return this.factory.get(command);
	}
}
