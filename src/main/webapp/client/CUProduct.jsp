<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" errorPage="error.do"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>CU 상품 - 꿀조합팩토리</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="manifest" href="site.webmanifest">
<link rel="shortcut icon" type="image/x-icon"
	href="assets/img/logo/iconLogo.jpg">

<!-- 사용자 정의 css -->
<link rel="stylesheet" href="assets/css/customization/storeProduct.css">

<style type="text/css">
.slider-area .single-slider {
	background-image: url('assets/img/hero/CUheader.jpg');
	background-color: purple;
}
/* "내 위치 찾기" 버튼 스타일 */
#locationButton {
	display: block;
	width: 200px; /* 버튼 너비 */
	margin: 20px auto; /* 중앙 정렬 */
	padding: 10px;
	background-color: #007bff; /* 파란색 배경 */
	color: white; /* 텍스트 색상 */
	text-align: center;
	border-radius: 5px;
	border: none;
	cursor: pointer;
	transition: background-color 0.3s;
}

#locationButton:hover {
	background-color: #0056b3; /* 호버 시 색상 변경 */
}
/* InfoWindow 스타일 */
.gm-style-iw {
	z-index: 99999 !important; /* InfoWindow를 최상위로 표시 */
	visibility: visible !important;
	opacity: 1 !important;
	display: block !important;
}
</style>
</head>
<body>
	<!-- 헤더 영역 -->
	<%@ include file="header.jsp"%>

	<main>
		<!-- Hero Area Start-->
		<div class="slider-area ">
			<div class="single-slider slider-height2 d-flex align-items-center">
			</div>
		</div>
		<!-- Hero Area End-->
		<br> <br> <br>
		<section class="popular-items">
			<div class="container">
				<!-- Section tittle -->
				<div class="row justify-content-center">
					<div class="col-xl-7 col-lg-8 col-md-10">
						<div class="section-tittle mb-70 text-center">
							<h2>핫이슈</h2>
						</div>
					</div>
				</div>

				<!-- Nav Card -->
				<div class="tab-content" id="nav-tabContent">
					<!-- card one -->
					<div class="tab-pane fade show active" id="nav-home"
						role="tabpanel" aria-labelledby="nav-home-tab">
						<div class="row" id="hotIssueWrapper"></div>
					</div>
				</div>
				<!-- 페이지네이션 Start -->
				<nav class="blog-pagination justify-content-center d-flex">
					<ul class="pagination" id="hotPageNation">
					</ul>
				</nav>
				<!-- 페이지네이션 End -->
				<br>
				<hr>
				<br> <br>
				<!-- Section tittle -->
				<div class="row justify-content-center">

					<div class="col-xl-7 col-lg-8 col-md-10">
						<div class="section-tittle mb-70 text-center">
							<h2>+1 증정 상품</h2>
						</div>
					</div>
				</div>
				<!-- Nav Card -->
				<div class="tab-content" id="nav-tabContent">
					<!-- card one -->
					<div class="tab-pane fade show active" id="nav-home"
						role="tabpanel" aria-labelledby="nav-home-tab">
						<div class="row" id="plusWrapper"></div>
					</div>
				</div>
				<!-- 페이지네이션 Start -->
				<nav class="blog-pagination justify-content-center d-flex">
					<ul class="pagination" id="plusPageNation">
					</ul>
				</nav>
				<!-- 페이지네이션 End -->
				<br>
				<hr>
				<br> <br>
				<!-- Section tittle -->
				<div class="row justify-content-center">
					<div class="col-xl-7 col-lg-8 col-md-10">
						<div class="section-tittle mb-70 text-center">
							<h2>지금 나에게 필요한건?</h2>
							<p>CU의 다양한 상품들을 소개합니다!</p>
							<div class="search-box">
								<input id="searchKeyword" type="text"
									placeholder="연세우유 크림빵을 검색해보세요!"> <i
									onclick="searchCUProductName()">🔍</i>
							</div>
						</div>
					</div>
				</div>
				<div class="row product-btn justify-content-between mb-40">
					<div class="properties__button">
						<!--Nav Button  -->
						<nav>
							<div class="nav nav-tabs" id="nav-tab" role="tablist">
								<a class="nav-item nav-link active" id="nav-home-tab"
									data-toggle="tab" href="javascript:void(0);" role="tab"
									aria-controls="nav-home" aria-selected="true"
									onclick="setCUProductOrderCondition('ORDERPOPULAR')">인기순</a> <a
									class="nav-item nav-link" id="nav-profile-tab"
									data-toggle="tab" href="javascript:void(0);" role="tab"
									aria-controls="nav-profile" aria-selected="false"
									onclick="setCUProductOrderCondition('ORDERHIGHPRICES')">가격
									높은순</a> <a class="nav-item nav-link" id="nav-contact-tab"
									data-toggle="tab" href="javascript:void(0);" role="tab"
									aria-controls="nav-contact" aria-selected="false"
									onclick="setCUProductOrderCondition('ORDERLOWPRICES')">가격
									낮은순</a>
							</div>
						</nav>
						<!--End Nav Button  -->
					</div>
					<!-- Grid and List view -->
					<div class="grid-list-view"></div>
					<!-- Select items -->
					<div class="select-this">
						<div class="category-container">
							<button class="category dark"
								onclick="setCUProductCategory('ALLPRODUCT')">전체</button>
							<button class="category purple"
								onclick="setCUProductCategory('DAILYSUPPLIESPRODUCT')">생활용품</button>
							<button class="category green"
								onclick="setCUProductCategory('FOODPRODUCT')">식품</button>
							<button class="category blue"
								onclick="setCUProductCategory('BEVERAGEPRODUCT')">음료</button>
						</div>
					</div>
				</div>
				<!-- Nav Card -->
				<div class="tab-content" id="nav-tabContent">
					<!-- card one -->
					<div class="tab-pane fade show active" id="nav-home"
						role="tabpanel" aria-labelledby="nav-home-tab">
						<div class="row" id="CUWrapper"></div>
					</div>
				</div>
				<!-- 페이지네이션 Start -->
				<nav class="blog-pagination justify-content-center d-flex">
					<ul class="pagination" id="CUPageNation">
					</ul>
				</nav>
				<!-- 페이지네이션 End -->
				<br>
				<hr>
				<br> <br>
				<!-- Section tittle -->
				<div class="row justify-content-center">
					<div class="col-xl-7 col-lg-8 col-md-10">
						<div class="section-tittle mb-70 text-center">
							<h2>우리 주변 편의점</h2>
							<br>
							<div class="search-box">
								<input id="searchStoreKeyword" type="text"
									placeholder="편의점 이름을 검색해보세요!"> <i
									onclick="searchStore()">🔍</i>
							</div>
						</div>
						<div id="mapWrapper" style="width: 100%; height: 500px;"></div>
						<!-- 위치 찾기 버튼 추가 -->
						<button id="locationButton" onclick="getLocation()">현재
							위치로 이동</button>
						<p id="location"></p>
					</div>
				</div>
			</div>
			</div>
		</section>
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
	<script src="assets/js/customization/CUProduct.js"></script>

	<!-- Map js -->
	<script type="text/javascript"
		src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=KakaoMapAPI_AppKey&libraries=services,clusterer,drawing"></script>
	<script>
    let map;
    let userMarker;
    let currentInfowindow = null; // 현재 열린 정보창을 추적하기 위한 변수
    let currentUserPosition = null; // 사용자의 현재 위치

    // 지도 초기화 함수
    function initMap() {
        map = new kakao.maps.Map(document.getElementById('mapWrapper'), {
            center: new kakao.maps.LatLng(37.5665, 126.9780), // 초기 지도 중심 좌표 설정
            level: 3 // 초기 지도 확대 레벨 설정
        });
    }

    // 현재 위치 찾기 함수
    function getLocation() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                const pos = new kakao.maps.LatLng(position.coords.latitude, position.coords.longitude);
                map.setCenter(pos); // 지도 중심을 현재 위치로 설정

                if (userMarker) userMarker.setMap(null); // 기존 마커 삭제

                // 새로운 마커 생성
                userMarker = new kakao.maps.Marker({
                    position: pos,
                    map: map,
                    title: '내 위치'
                });

                searchNearbyCU(pos); // 주변 300m 반경 내 편의점 검색
            }, function() {
                alert('Geolocation 서비스 실패');
            });
        } else {
            alert('브라우저가 Geolocation을 지원하지 않습니다.');
        }
    }

    // 사용자가 입력한 검색어로 장소 검색
    function searchStore() {
        const keyword = document.getElementById('searchStoreKeyword').value.trim();
        console.log("편의점 검색어 [",keyword,"]");
        if (!keyword) {
            alert('편의점 이름을 입력해 주세요.');
            return;
        }

        const ps = new kakao.maps.services.Places();

        ps.keywordSearch(keyword, function(data, status, pagination) {
            if (status === kakao.maps.services.Status.OK) {
                // 검색 결과 중 첫 번째 장소를 기준으로 지도 이동
                const firstPlace = data[0];
                map.setCenter(new kakao.maps.LatLng(firstPlace.y, firstPlace.x));

                // 이전에 열린 정보창이 있다면 닫기
                if (currentInfowindow) {
                    currentInfowindow.close();
                }

                // 새로운 마커 생성
                const marker = new kakao.maps.Marker({
                    map: map,
                    position: new kakao.maps.LatLng(firstPlace.y, firstPlace.x)
                });

                // 마커에 클릭 이벤트 추가: 클릭 시 정보창 표시
                const infowindow = new kakao.maps.InfoWindow({
                    content: '<div style="padding:5px;font-size:12px;">' + firstPlace.place_name + '<br>' + (firstPlace.road_address_name || firstPlace.address_name) + '</div>'
                });
                infowindow.open(map, marker);
                currentInfowindow = infowindow;

                // 마커 클릭 시 이벤트: 이전 정보창 닫고 새로운 정보창 열기
                kakao.maps.event.addListener(marker, 'click', function() {
                    if (currentInfowindow) {
                        currentInfowindow.close();
                    }
                    infowindow.open(map, marker);
                    currentInfowindow = infowindow;
                });
            } else {
                alert('해당 편의점을 찾을 수 없습니다.');
            }
        });
    }

 // 주변 300m 반경 내 CU 편의점 검색
    function searchNearbyCU(location) {
        const ps = new kakao.maps.services.Places();
        ps.categorySearch('CS2', function(result, status) { // 'CS2'는 편의점 카테고리
            if (status === kakao.maps.services.Status.OK) {
                result.forEach(function(place) {
                    if (place.place_name.includes("CU")) { // 이름에 "CU"가 포함된 편의점만 처리
                        displayMarker(place);
                    }
                });

                if (!result.some(place => place.place_name.includes("CU"))) {
                    alert('주변에 CU 편의점을 찾을 수 없습니다.');
                }
            } else {
                alert('주변에 편의점을 찾을 수 없습니다.');
            }
        }, {
            location: location,
            radius: 300 // 검색 반경 (미터)
        });
    }

    // 마커를 생성하고 정보창을 표시하는 함수
    function displayMarker(place) {
        const coords = new kakao.maps.LatLng(place.y, place.x);

        const marker = new kakao.maps.Marker({
            map: map,
            position: coords
        });

        const infowindow = new kakao.maps.InfoWindow({
            content: '<div style="padding:5px;font-size:12px;">' + place.place_name + '<br>' + (place.road_address_name || place.address_name) + '</div>'
        });

        kakao.maps.event.addListener(marker, 'click', function() {
            if (currentInfowindow) {
                currentInfowindow.close();
            }
            infowindow.open(map, marker);
            currentInfowindow = infowindow;
        });
    }

    kakao.maps.load(initMap);
</script>

</body>
</html>