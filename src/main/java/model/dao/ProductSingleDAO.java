package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import model.common.JDBCUtil;
import model.dto.ProductSingleDTO;

public class ProductSingleDAO { // 개별상품 DAO
	// 상품 카테고리(핫이슈, +1 증정상품)
	final String SELECTALL_1 = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER"
			+ " FROM PRODUCT_SINGLE WHERE PRODUCT_SINGLE_STORE = ? AND PRODUCT_SINGLE_CATEGORY = ? LIMIT ? OFFSET ?";
	// 상품이름 검색(인기순)
	final String SELECTALL_2 = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER"
			+ " FROM PRODUCT_SINGLE WHERE PRODUCT_SINGLE_NAME LIKE CONCAT('%', ?, '%') LIMIT ? OFFSET ?";
	// 상품 전체+인기순
	final String SELECTALL_3 = "SELECT PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME,PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER, COUNT(PURCHASE_DETAIL.PURCHASE_DETAIL_NUMBER) AS PURCHASE_COUNT"
			+ " FROM PRODUCT_SINGLE LEFT JOIN PURCHASE_DETAIL ON PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER = PURCHASE_DETAIL.PRODUCT_SINGLE_NUMBER WHERE PRODUCT_SINGLE_STORE = ?"
			+ " GROUP BY PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_IMAGE ORDER BY PURCHASE_COUNT DESC LIMIT ? OFFSET ?";
	// 상품 전체+가격 높은순
	final String SELECTALL_4 = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER"
			+ " FROM PRODUCT_SINGLE WHERE PRODUCT_SINGLE_STORE = ? ORDER BY PRODUCT_SINGLE_PRICE DESC LIMIT ? OFFSET ?";
	// 상품 전체+가격 낮은순
	final String SELECTALL_5 = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER"
			+ " FROM PRODUCT_SINGLE WHERE PRODUCT_SINGLE_STORE = ? ORDER BY PRODUCT_SINGLE_PRICE ASC LIMIT ? OFFSET ?";
	// 상품 카테고리+인기순
	final String SELECTALL_6 = "SELECT PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME,PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER, COUNT(PURCHASE_DETAIL.PURCHASE_DETAIL_NUMBER) AS PURCHASE_COUNT"
			+ " FROM PRODUCT_SINGLE LEFT JOIN PURCHASE_DETAIL ON PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER = PURCHASE_DETAIL.PRODUCT_SINGLE_NUMBER WHERE PRODUCT_SINGLE_STORE = ? AND PRODUCT_SINGLE_CATEGORY = ?"
			+ " GROUP BY PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_IMAGE ORDER BY PURCHASE_COUNT DESC LIMIT ? OFFSET ?";
	// 상품 카테고리+가격 높은순
	final String SELECTALL_7 = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER"
			+ " FROM PRODUCT_SINGLE WHERE PRODUCT_SINGLE_STORE = ? AND PRODUCT_SINGLE_CATEGORY = ? ORDER BY PRODUCT_SINGLE_PRICE DESC LIMIT ? OFFSET ?";
	// 상품 카테고리+가격 낮은순
	final String SELECTALL_8 = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER"
			+ " FROM PRODUCT_SINGLE WHERE PRODUCT_SINGLE_STORE = ? AND PRODUCT_SINGLE_CATEGORY = ? ORDER BY PRODUCT_SINGLE_PRICE ASC LIMIT ? OFFSET ?";

	// 상품 상세정보
	final String SELECTONE_9 = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_CATEGORY, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_IMAGE, PRODUCT_SINGLE_STOCK, PRODUCT_SINGLE_INFORMATION"
			+ " FROM PRODUCT_SINGLE WHERE PRODUCT_SINGLE_NUMBER = ?";

	// 상품이름 검색(가격 높은 순)
	final String SELECTALL_10 = "SELECT PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE.PRODUCT_SINGLE_NAME, PRODUCT_SINGLE.PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE.PRODUCT_SINGLE_IMAGE, "
			+ "COUNT(PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER FROM PRODUCT_SINGLE WHERE PRODUCT_SINGLE.PRODUCT_SINGLE_NAME "
			+ "LIKE CONCAT('%', ?, '%') ORDER BY PRODUCT_SINGLE.PRODUCT_SINGLE_PRICE DESC LIMIT ? OFFSET ?";

	// 상품이름 검색(가격 낮은 순)
	final String SELECTALL_11 = "SELECT PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE.PRODUCT_SINGLE_NAME, PRODUCT_SINGLE.PRODUCT_SINGLE_PRICE,"
			+ " PRODUCT_SINGLE.PRODUCT_SINGLE_IMAGE, COUNT(PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER FROM PRODUCT_SINGLE "
			+ "WHERE PRODUCT_SINGLE.PRODUCT_SINGLE_NAME LIKE CONCAT('%', ?, '%') ORDER BY PRODUCT_SINGLE.PRODUCT_SINGLE_PRICE ASC LIMIT ? OFFSET ?"; 

	// 개별상품 추가
	final String INSERT = "INSERT INTO PRODUCT_SINGLE (PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_STOCK,"
			+ " PRODUCT_SINGLE_IMAGE, PRODUCT_SINGLE_STORE, PRODUCT_SINGLE_CATEGORY, PRODUCT_SINGLE_INFORMATION) "
			+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	// 개별상품 재고순 정렬
	final String SELECTALLSTOCKDESC = "SELECT PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE_NAME, PRODUCT_SINGLE_PRICE, PRODUCT_SINGLE_STOCK, PRODUCT_SINGLE_IMAGE, PRODUCT_SINGLE_STORE, PRODUCT_SINGLE_CATEGORY, PRODUCT_SINGLE_INFORMATION, "
			+ "COUNT(PRODUCT_SINGLE_NUMBER) OVER() AS TOTAL_COUNT_NUMBER FROM PRODUCT_SINGLE ORDER BY PRODUCT_SINGLE_STOCK DESC LIMIT ? OFFSET ?";

	public ArrayList<ProductSingleDTO> selectAll(ProductSingleDTO productSingleDTO) { // R
		ArrayList<ProductSingleDTO> datas = new ArrayList<ProductSingleDTO>(); // 배열 객체화
		// scope문제로 위에 선언 및 try~catch문으로 인한 변수 초기화
		Connection conn = null;
		PreparedStatement pstmt = null;
		try { // 에러 가능성 있는 코드
			// 드라이버 로드 및 연결
			conn = JDBCUtil.connect();
			// 데이터 read
			System.out.println("ProductSingleDAO 로그-받은 selectAll 조건 : ["+productSingleDTO.getCondition()+"]");
			// 상품 카테고리(핫이슈, +1 증정상품)
			if (productSingleDTO.getCondition().equals("SELECTALL_1")) {
				pstmt = conn.prepareStatement(SELECTALL_1); // 구문 저장
				pstmt.setString(1, productSingleDTO.getProductSingleStore()); // 파서 값 저장
				pstmt.setString(2, productSingleDTO.getProductSingleCategory());
				pstmt.setInt(3, productSingleDTO.getLimitNumber());
				pstmt.setLong(4, productSingleDTO.getStartIndex());
			}
			// 상품이름 검색
			else if (productSingleDTO.getCondition().equals("SELECTALL_2")) {
				pstmt = conn.prepareStatement(SELECTALL_2); // 구문 저장
				pstmt.setString(1, productSingleDTO.getSearchKeyword());
				pstmt.setInt(2, productSingleDTO.getLimitNumber());
				pstmt.setLong(3, productSingleDTO.getStartIndex());
				//pstmt.setString(1, productSingleDTO.getProductSingleStore()); // 파서 값 저장
			}
			// 상품 전체+인기순
			else if (productSingleDTO.getCondition().equals("SELECTALL_3")) {
				pstmt = conn.prepareStatement(SELECTALL_3); // 구문 저장
				pstmt.setString(1, productSingleDTO.getProductSingleStore()); // 파서 값 저장
				pstmt.setInt(2, productSingleDTO.getLimitNumber());
				pstmt.setLong(3, productSingleDTO.getStartIndex());
			}
			// 상품 전체+가격 높은순
			else if (productSingleDTO.getCondition().equals("SELECTALL_4")) {
				pstmt = conn.prepareStatement(SELECTALL_4); // 구문 저장
				pstmt.setString(1, productSingleDTO.getProductSingleStore()); // 파서 값 저장
				pstmt.setInt(2, productSingleDTO.getLimitNumber());
				pstmt.setLong(3, productSingleDTO.getStartIndex());
			}
			// 상품 전체+가격 낮은순
			else if (productSingleDTO.getCondition().equals("SELECTALL_5")) {
				pstmt = conn.prepareStatement(SELECTALL_5); // 구문 저장
				pstmt.setString(1, productSingleDTO.getProductSingleStore()); // 파서 값 저장
				pstmt.setInt(2, productSingleDTO.getLimitNumber());
				pstmt.setLong(3, productSingleDTO.getStartIndex());
			}
			// 상품 카테고리+인기순
			else if (productSingleDTO.getCondition().equals("SELECTALL_6")) {
				pstmt = conn.prepareStatement(SELECTALL_6); // 구문 저장
				pstmt.setString(1, productSingleDTO.getProductSingleStore()); // 파서 값 저장
				pstmt.setString(2, productSingleDTO.getProductSingleCategory());
				pstmt.setInt(3, productSingleDTO.getLimitNumber());
				pstmt.setLong(4, productSingleDTO.getStartIndex());
			}
			// 상품 카테고리+가격 높은순
			else if (productSingleDTO.getCondition().equals("SELECTALL_7")) {
				pstmt = conn.prepareStatement(SELECTALL_7); // 구문 저장
				pstmt.setString(1, productSingleDTO.getProductSingleStore()); // 파서 값 저장
				pstmt.setString(2, productSingleDTO.getProductSingleCategory());
				pstmt.setInt(3, productSingleDTO.getLimitNumber());
				pstmt.setLong(4, productSingleDTO.getStartIndex());
			}
			// 상품 카테고리+가격 낮은순
			else if (productSingleDTO.getCondition().equals("SELECTALL_8")) {
				pstmt = conn.prepareStatement(SELECTALL_8); // 구문 저장
				pstmt.setString(1, productSingleDTO.getProductSingleStore()); // 파서 값 저장
				pstmt.setString(2, productSingleDTO.getProductSingleCategory());
				pstmt.setInt(3, productSingleDTO.getLimitNumber());
				pstmt.setLong(4, productSingleDTO.getStartIndex());
			}
			// 상품 상세 정보
			else if (productSingleDTO.getCondition().equals("SELECTONE_9")) {
				pstmt = conn.prepareStatement(SELECTONE_9); // 구문 저장
				pstmt.setLong(1, productSingleDTO.getProductSingleNumber());
			}
			// 개별상품 재고순 정렬
			else if (productSingleDTO.getCondition().equals("SELECTALLSTOCKDESC")) {
				pstmt = conn.prepareStatement(SELECTALLSTOCKDESC); // 구문 저장
				pstmt.setInt(1, productSingleDTO.getLimitNumber());
				pstmt.setLong(2, productSingleDTO.getStartIndex());
			}
			// 검색 가격높은순
			else if (productSingleDTO.getCondition().equals("SELECTALL_10")) {
				pstmt = conn.prepareStatement(SELECTALL_10); // 구문 저장
				pstmt.setString(1, productSingleDTO.getSearchKeyword());
				pstmt.setInt(2, productSingleDTO.getLimitNumber());
				pstmt.setLong(3, productSingleDTO.getStartIndex());
			}
			// 검색 가격 낮은순
			else if (productSingleDTO.getCondition().equals("SELECTALL_11")) {
				pstmt = conn.prepareStatement(SELECTALL_11); // 구문 저장
				pstmt.setString(1, productSingleDTO.getSearchKeyword());
				pstmt.setInt(2, productSingleDTO.getLimitNumber());
				pstmt.setLong(3, productSingleDTO.getStartIndex());
			}
			// 결과 rs에 저장
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) { // 다음 데이터가 있다면
				ProductSingleDTO data = new ProductSingleDTO(); // 객체화
				data.setProductSingleNumber(rs.getLong("PRODUCT_SINGLE_NUMBER")); // 값들 저장
				data.setProductSingleName(rs.getString("PRODUCT_SINGLE_NAME"));
				data.setProductSinglePrice(rs.getInt("PRODUCT_SINGLE_PRICE"));
				data.setProductSingleImage(rs.getString("PRODUCT_SINGLE_IMAGE"));
				data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));

				System.out.println("ProductSingleDAO 로그-반환 selectALL : ["+productSingleDTO.getCondition()+"]");

				if (productSingleDTO.getCondition().equals("SELECTALLSTOCKDESC")) {
					data.setProductSingleStock(rs.getInt("PRODUCT_SINGLE_STOCK"));
					data.setProductSingleCategory(rs.getString("PRODUCT_SINGLE_CATEGORY"));
					data.setProductSingleStore(rs.getString("PRODUCT_SINGLE_STORE"));
					data.setProductSingleInformation(rs.getString("PRODUCT_SINGLE_INFORMATION"));
				}
				if(productSingleDTO.getCondition().equals("SELECTALL_9")) {
					data.setProductSingleCategory(rs.getString("PRODUCT_SINGLE_CATEGORY"));
			}
				datas.add(data); // 배열에 추가
			}
		} catch (Exception e) { // 에러가 났다면
			e.printStackTrace(); // 에러 내용 출력
		} finally { // 반드시 실행할 코드
			// DB 연결 해제
			JDBCUtil.disconnect(conn, pstmt);
		}

		System.out.println("ProductSingleDAO 로그-반환 selectALL : ["+datas+"]");
		return datas; // 배열 반환
	}

	public ProductSingleDTO selectOne(ProductSingleDTO productSingleDTO) { // R
		ProductSingleDTO data = null; // 기본 null로 설정
		// scope문제로 위에 선언 및 try~catch문으로 인한 변수 초기화
		Connection conn = null;
		PreparedStatement pstmt = null;
		System.out.println("ProductSingleDAO 로그-받은 selectOne 조건 : ["+productSingleDTO.getCondition()+"]");
		// CU 상품 상세정보
		if (productSingleDTO.getCondition().equals("SELECTONE_9")) {
			try { // 에러 가능성 있는 코드
				// 드라이버 로드 및 연결
				conn = JDBCUtil.connect();
				// 데이터 read
				pstmt = conn.prepareStatement(SELECTONE_9); // 구문 저장
				pstmt.setLong(1, productSingleDTO.getProductSingleNumber()); // 파서 값 저장	
				System.out.println("SELECT_9 실행할 개별 상품 번호 ["+productSingleDTO.getProductSingleNumber()+"]");

				// 결과 rs에 저장
				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) { // 다음 데이터가 있다면
					data = new ProductSingleDTO(); // 객체화
					data.setProductSingleNumber(rs.getLong("PRODUCT_SINGLE_NUMBER")); // 값들 저장
					data.setProductSingleName(rs.getString("PRODUCT_SINGLE_NAME"));
					data.setProductSinglePrice(rs.getInt("PRODUCT_SINGLE_PRICE"));
					data.setProductSingleImage(rs.getString("PRODUCT_SINGLE_IMAGE"));
					data.setProductSingleStock(rs.getInt("PRODUCT_SINGLE_STOCK"));
					data.setProductSingleInformation(rs.getString("PRODUCT_SINGLE_INFORMATION"));
					data.setProductSingleCategory(rs.getString("PRODUCT_SINGLE_CATEGORY"));
				}
			} catch (Exception e) { // 에러가 났다면
				e.printStackTrace(); // 에러 내용 출력
			} finally { // 반드시 실행할 코드
				// DB 연결 해제
				JDBCUtil.disconnect(conn, pstmt);
			}
		}
		System.out.println("ProductSingleDAO 로그-반환 selectOne : ["+data+"]");
		return data; // 데이터 반환
	}


	public boolean insert(ProductSingleDTO dto) {
		if (existsByName(dto.getProductSingleName())) {//중복 상품 확인 메서드
			System.out.println("[DAO/insert] 중복 상품 등록 생략: " + dto.getProductSingleName());
			return false;
		}
		if (dto.getProductSingleNumber() == 0) {//상품번호 자동 생성
			dto.setProductSingleNumber(getNextProductSingleNumberByBrand(dto.getProductSingleStore()));
		}


		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = JDBCUtil.connect();
			pstmt = conn.prepareStatement(INSERT);

			pstmt.setLong(1, dto.getProductSingleNumber());
			pstmt.setString(2, dto.getProductSingleName());
			pstmt.setInt(3, dto.getProductSinglePrice());
			pstmt.setInt(4, dto.getProductSingleStock());
			pstmt.setString(5, dto.getProductSingleImage());
			pstmt.setString(6, dto.getProductSingleStore());
			pstmt.setString(7, dto.getProductSingleCategory());

			if (dto.getProductSingleInformation() != null && !dto.getProductSingleInformation().isEmpty()) {
				pstmt.setString(8, dto.getProductSingleInformation());
			} else {
				pstmt.setNull(8, Types.VARCHAR);
			}

			int result = pstmt.executeUpdate();
			System.out.println("[DAO/insert] 상품 등록 완료: " + dto.getProductSingleName());
			return result > 0;
		} catch (Exception e) {
			System.out.println("[DAO/insert] 상품 등록 중 예외 발생: " + dto.getProductSingleName());
			e.printStackTrace();
			return false;
		} finally {
			JDBCUtil.disconnect(conn, pstmt);
		}
	}

	private boolean existsByName(String productName) {//데이터가 있는지 확인 insert문 때문에
		String sql = "SELECT COUNT(*) FROM PRODUCT_SINGLE WHERE PRODUCT_SINGLE_NAME = ?";
		//한번만 사용하기 때문에 내부에 사용
		try (
				Connection conn = JDBCUtil.connect();
				PreparedStatement pstmt = conn.prepareStatement(sql)
				) {
			pstmt.setString(1, productName);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0; // 이미 존재함
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false; // 예외가 생기거나 없으면 insert 하게끔 처리
	}   

	private static final Map<String, Long> storeMap = new HashMap<>();
	static {
		storeMap.put("CU", 10000L);//cu는 10000~
		storeMap.put("GS25", 20000L);//gs25는 20000~
		//확장성을 위해 다른 store도 추가 쉽게 할 수 있음
	}
	private long getNextProductSingleNumberByBrand(String store) {//번호생성
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;


		long base = storeMap.getOrDefault(store, 30000L);
		//cu나 gs25 가 아니면 기본값 30000부터 시작
		long maxRange = base + 9999;
		//해당브랜드가 사용할 최대범위
		long nextNumber = base;
		//기본으로 시작번호로 초기화
		//이후 db에 따라 갱신


		try {
			conn = JDBCUtil.connect();
			String sql = "SELECT IFNULL(MAX(PRODUCT_SINGLE_NUMBER), ?) AS MAXNUMBER FROM PRODUCT_SINGLE WHERE PRODUCT_SINGLE_NUMBER BETWEEN ? AND ?";
			//현재 db에서 해당구간내에서 가장 큰 번호를 조회
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, base - 1);//데이터가 없을 때는 base-1>>+1하면서 시작
			pstmt.setLong(2, base);//범위시작
			pstmt.setLong(3, maxRange);//범위끝

			rs = pstmt.executeQuery();
			if (rs.next()) {
				long max = rs.getLong("MAXNUMBER");
				nextNumber = (max == 0) ? base : max + 1;
				//max가 0이면 base부터, 아니면 +1부터
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.disconnect(conn, pstmt);
		}

		return nextNumber;
	}


	private boolean update(ProductSingleDTO productSingleDTO) { // U
		return false;
	}

	private boolean delete(ProductSingleDTO productSingleDTO) { // D
		return false;
	}
}
