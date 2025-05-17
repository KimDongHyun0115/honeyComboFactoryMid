<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" errorPage="error.do"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html lang="zxx">
<head>
<meta charset="utf-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<title>장바구니 - 꿀조합팩토리</title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="manifest" href="site.webmanifest">
<link rel="shortcut icon" type="image/x-icon"
	href="assets/img/logo/iconLogo.jpg">
<!-- 사용자 정의 css -->
<link href="assets/css/customization/cart.css" type="text/css"
	rel="stylesheet">
</head>
<body>
	<!-- 헤더 영역 -->
	<%@ include file="header.jsp"%>

	<br>
	<br>
	<br>
	<main>
		<!--================Cart Area =================-->
		<section class="cart_area">
			<div class="container">
				<div class="cart_inner">
					<div class="table-responsive">
						<table class="table">
							<thead>
								<tr>
									<th scope="col">장바구니 상품</th>
									<th scope="col">가격</th>
									<th scope="col">수량</th>
									<th scope="col">총 가격</th>
									<th scope="col"><input type="checkbox"
										id="cartAllCheckBox" onchange="selectAllProduct(this.checked)"></th>
								</tr>
							</thead>
							<tbody>
								<c:if test="${empty cartProductDatas}">
									<tr>
										<td>장바구니에 담긴 상품이 없습니다.</td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
									</tr>
								</c:if>
								<c:if test="${not empty cartProductDatas}">
									<c:forEach var="cartProductData" items="${cartProductDatas}">
										<tr id="cartRow-${cartProductData.cartProductNumber}">
											<td>
												<div class="media">
													<div class="d-flex">
														<img src="${cartProductData.cartProductImage}"
															alt="장바구니 상품 이미지" />
													</div>
													<div class="media-body">
														<p>${cartProductData.cartProductName}</p>
													</div>
												</div>
											</td>
											<td>
												<h5
													id="cartProductPrice-${cartProductData.cartProductNumber}">${cartProductData.cartProductPrice}</h5>
											</td>
											<td>
												<div class="product_count">
													<span class="input-number-decrement"
														onclick="changeCartProductCount(${cartProductData.cartProductNumber}, 'downCartProductCount')">
														<i class="ti-minus"></i>
													</span> <input class="input-number" type="text"
														id="count-${cartProductData.cartProductNumber}"
														value="${cartProductData.cartProductCount}" min="1"
														max="${cartProductData.cartProductStock}" readonly>
													<span class="input-number-increment"
														onclick="changeCartProductCount(${cartProductData.cartProductNumber}, 'upCartProductCount')">
														<i class="ti-plus"></i>
													</span>
												</div>
											</td>
											<td>
												<h5>
													<span
														id="cartProductSumPrice-${cartProductData.cartProductNumber}">${cartProductData.cartProductPrice * cartProductData.cartProductCount}
													</span>원
												</h5>
											</td>
											<td><input type="checkbox" class="productCheckBox"
												id="productCheckBox-${cartProductData.cartProductNumber}"
												onclick="clickOneCheckBox(this.checked)"
												value="${cartProductData.cartProductNumber}"></td>
										</tr>
									</c:forEach>
								</c:if>
								<tr>
									<td></td>
									<td></td>
									<td>
										<h5>총 구매 가격</h5>
									</td>
									<td>
										<h5>
											<span id="totalAmount">0</span>&nbsp;원
										</h5>
									</td>
									<td></td>
								</tr>
								<tr class="shipping_area">
									<td></td>
									<td></td>
									<td></td>
									<td>
										<div class="shipping_box">
											<h6>배송지</h6>
											<input class="post_code" type="text" id="memberAddressMain"
												value="${memberAddressDatas.memberAddressMain}" required readonly /><input
												id="memberAddressDetail" class="post_code" type="text"
												pattern="^(?=.*[가-힣])(?=.*[0-9])[가-힣a-zA-Z0-9\s\-.,#]{2,50}$"
												title="상세 주소는 한글과 숫자를 포함한 2~50자의 값만 입력 가능합니다."
												value="${memberAddressDatas.memberAddressDetail}" required readonly />
											<a href="javascript:void(0);" onclick="kakaoPostAPIcode()" class="btn_1 genric-btn warning radius">변경</a>
										</div>
									</td>
									<td></td>
								</tr>
							</tbody>
						</table>
						<div class="checkout_btn_inner float-right">
							<a class="btn_1" href="javascript:void(0);"
								onclick="deleteCartProduct()">선택 상품 삭제</a> <a
								class="btn_1 checkout_btn_1" href="javascript:void(0);"
								onclick="purchaseCartProduct()">결제하기</a>
						</div>
					</div>
				</div>
			</div>
		</section>
		<!--================End Cart Area =================-->
	</main>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<!-- 푸터 영역 -->
	<%@ include file="footer.jsp"%>

	<!-- 사용자 정의 js -->
	<script type="text/javascript" src="assets/js/customization/cart.js"></script>
	<!-- 주소 검색 js -->
	<script
		src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
	<script type="text/javascript"
		src="//dapi.kakao.com/v2/maps/sdk.js?appkey=69df466ae7cc967290bff3279669a70a&libraries=services"></script>
</body>
</html>