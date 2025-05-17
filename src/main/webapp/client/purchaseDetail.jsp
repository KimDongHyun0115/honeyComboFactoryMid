<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" errorPage="error.do"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html lang="zxx">
<head>
<meta charset="utf-8">
<meta http-equiv="x-ua-compatible" content="ie=edge">
<title>주문 상세정보 - 꿀조합팩토리</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="manifest" href="site.webmanifest">
<link rel="shortcut icon" type="image/x-icon"
	href="assets/img/logo/iconLogo.jpg">
</head>

<body>
	<!-- 헤더 영역 -->
	<%@ include file="header.jsp"%>

	<main>
		<br> <br>
		<!--================ confirmation part start =================-->
		<section class="confirmation_part">
			<div class="container">
				<div class="row">
					<div class="col-lg-6 col-lx-4">
						<div class="single_confirmation_details">
							<h4>주문 정보</h4>
							<ul>
								<li>
									<p>주문 번호</p> <span>: ${purchaseData.purchaseNumber}</span>
								</li>
								<li>
									<p>결제번호</p> <span>: ${purchaseData.purchaseTerminalId}</span>
								</li>
								<li>
									<p>결제 가격</p> <span>: ${purchaseData.purchaseTotalPrice}원</span>
								</li>
							</ul>
						</div>
					</div>
					<div class="col-lg-6 col-lx-4">
						<div class="single_confirmation_details">
							<h4>배송지</h4>
							<ul>
								<li>
									<p>도로명 주소</p> <span>:
										${memberAddressDatas.memberAddressMain}</span>
								</li>
								<li>
									<p>상세 주소</p> <span>:
										${memberAddressDatas.memberAddressDetail}</span>
								</li>
								<li>&nbsp;</li>
							</ul>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="col-lg-12">
						<div class="order_details_iner">
							<h3>주문 상세정보</h3>
							<table class="table table-borderless">
								<thead>
									<tr>
										<th scope="col" colspan="2">상품</th>
										<th scope="col">수량</th>
										<th scope="col">총 가격</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="purchaseProductDetailData"
										items="${purchaseProductDetailDatas}">
										<tr>
											<c:choose>
												<c:when test="${not empty purchaseProductDetailData.productComboName}">
													<th colspan="2">
														<span>${purchaseProductDetailData.productComboName}</span>
													</th>
													<th>${purchaseProductDetailData.purchaseProductCount}</th>
													<th>
														<span>
															${purchaseProductDetailData.purchaseProductCount * 
															(purchaseProductDetailData.productComboPrice != null ? 
															purchaseProductDetailData.productComboPrice : 0)}원
														</span>
													</th>
												</c:when>
												<c:when test="${not empty purchaseProductDetailData.productSingleName}">
													<th colspan="2">
														<span>${purchaseProductDetailData.productSingleName}</span>
													</th>
													<th>${purchaseProductDetailData.purchaseProductCount}</th>
													<th>
														<span>
															${purchaseProductDetailData.purchaseProductCount * 
															(purchaseProductDetailData.productSinglePrice != null ? 
															purchaseProductDetailData.productSinglePrice : 0)}원
														</span>
													</th>
												</c:when>
												<c:otherwise>
													<th colspan="4">알 수 없는 상품 데이터입니다. 관리자에게 문의해주세요.</th>
												</c:otherwise>
											</c:choose>
										</tr>
									</c:forEach>
								</tbody>

								<tfoot>
									<tr>
										<th scope="col" colspan="3">총합</th>
										<th scope="col">${purchaseData.purchaseTotalPrice}원</th>
									</tr>
								</tfoot>
							</table>
						</div>
					</div>
				</div>
			</div>
		</section>
	</main>
	<br><br><br><br><br><br>
	<%@ include file="footer.jsp"%>
	<script type="text/javascript" src="assets/js/customization/purchaseDetail.js"></script>
</body>
</html>
