package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.common.JDBCUtil;
import model.dto.ReviewDTO;

public class ReviewDAO {
	// 리뷰 작성 C, 리뷰 삭제 D, 리뷰 수정 U, 리뷰 목록 조회 R ALL, 리뷰 상세 조회 R ONE
	// [1] 리뷰 작성 (C)
	// REVIEW_NUMBER는 가장 큰 번호 + 1해서 매 데이터 생성마다 PK 하나씩 증가
	final String INSERTREVIEW = "INSERT INTO REVIEW (REVIEW_NUMBER, REVIEW_SCORE, REVIEW_REGISTER_DATE, REVIEW_CONTENT, MEMBER_NUMBER, PRODUCT_COMBO_NUMBER) "
			+ "SELECT IFNULL(MAX(REVIEW_NUMBER), 0) + 1, ?, NOW(), ?, ?, ? FROM REVIEW";
	
	// [2] 모든 리뷰 목록 조회 (R - ALL)  
	// 리뷰 전체 목록 최신순(글 작성일)으로 조회.
	// MEMBER 테이블 JOIN으로 회원 정보 함꼐 불러와서 출력
	final String SELECTALLREVIEWLIST ="SELECT REVIEW.REVIEW_NUMBER, REVIEW.REVIEW_SCORE, REVIEW.REVIEW_REGISTER_DATE, REVIEW.REVIEW_CONTENT, "
			+ "REVIEW.MEMBER_NUMBER, MEMBER.MEMBER_NAME, MEMBER.MEMBER_IS_WITHDRAW, REVIEW.PRODUCT_COMBO_NUMBER, "
			+ "COUNT(*) OVER() AS TOTAL_REVIEW_COUNT FROM REVIEW JOIN MEMBER ON REVIEW.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER "
			+ "ORDER BY REVIEW.REVIEW_REGISTER_DATE DESC LIMIT ?, ?";
	
	// [3] 리뷰 상세 조회 (R - ONE)
	// MEMBER_NAME, MEMBER_COMBO_NUMBER 2가지 조건을 통해 1개의 리뷰 상세 조회 
	final String SELECTONEREVIEW = "SELECT REVIEW.REVIEW_NUMBER, REVIEW.REVIEW_SCORE, REVIEW.REVIEW_REGISTER_DATE, REVIEW.REVIEW_CONTENT, MEMBER.MEMBER_NAME, "
			+ "REVIEW.MEMBER_NUMBER, REVIEW.PRODUCT_COMBO_NUMBER, MEMBER.MEMBER_IS_WITHDRAW FROM REVIEW JOIN MEMBER ON REVIEW.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER "
			+ "WHERE MEMBER.MEMBER_NUMBER = ? AND REVIEW.PRODUCT_COMBO_NUMBER = ?";	
	
	// [4] 리뷰 수정 (U)
	// 리뷰 내용, 별점만 수정 가능
	final String UPDATEREVIEW = "UPDATE REVIEW SET REVIEW_SCORE = ?, REVIEW_CONTENT = ? WHERE REVIEW_NUMBER = ?";
	
	// [5] 리뷰 삭제 (D)
	// 리뷰 번호를 조건으로 삭제
	final String DELETEREVIEW = "DELETE FROM REVIEW WHERE REVIEW_NUMBER = ?";
	
	// [6] 한 상품에 대한 리뷰 목록 출력 (R - ALL) 
	// 상품 번호를 조건으로 조회
	final String SELECTALLREVIEWONEPROUDCT ="SELECT REVIEW.REVIEW_NUMBER, REVIEW.REVIEW_SCORE, REVIEW.REVIEW_REGISTER_DATE, REVIEW.REVIEW_CONTENT, "
			+ "MEMBER.MEMBER_NUMBER, MEMBER.MEMBER_NAME, MEMBER.MEMBER_IS_WITHDRAW, REVIEW.PRODUCT_COMBO_NUMBER, COUNT(*) OVER() "
			+ "AS TOTAL_REVIEW_COUNT FROM REVIEW JOIN MEMBER ON REVIEW.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER "
			+ "WHERE REVIEW.PRODUCT_COMBO_NUMBER = ? ORDER BY REVIEW.REVIEW_REGISTER_DATE DESC LIMIT ?, ?";


	// [SELECTALL] - 모든 리뷰 목록 조회, 한 상품에 대한 리뷰 목록 출력
	public ArrayList<ReviewDTO> selectAll(ReviewDTO reviewDTO) {
		ArrayList<ReviewDTO> datas = new ArrayList<>();
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			conn=JDBCUtil.connect();
			System.out.println("[M 로그] SELECTALL() 호출, condition: " + reviewDTO.getCondition());

			// 전체 리뷰 목록
			if(reviewDTO.getCondition().equals("SELECTALLREVIEWLIST")) {
				pstmt=conn.prepareStatement(SELECTALLREVIEWLIST);	
				pstmt.setInt(1, reviewDTO.getReviewIndex());
				pstmt.setInt(2, reviewDTO.getReviewContentCount());
				System.out.println("[M 로그] SELECTALLREVIEWLIST 실행: index=["+reviewDTO.getReviewIndex()+"], count=["+reviewDTO.getReviewContentCount()+"]");
			}
			// 한 상품에 대한 리뷰 목록 
			else if(reviewDTO.getCondition().equals("SELECTALLREVIEWONEPROUDCT")) {
				pstmt=conn.prepareStatement(SELECTALLREVIEWONEPROUDCT);
				pstmt.setLong(1, reviewDTO.getProductComboNumber());
				pstmt.setInt(2, reviewDTO.getReviewIndex());
				pstmt.setInt(3, reviewDTO.getReviewContentCount());
				System.out.println("[M 로그] SELECTALLREVIEWONEPROUDCT 실행: comboNumber=["+reviewDTO.getProductComboNumber()+"], index=["+reviewDTO.getReviewIndex()+"], count=["+reviewDTO.getReviewContentCount()+"]");
			}
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()) {
				ReviewDTO data = new ReviewDTO();
				data.setReviewNumber(rs.getLong("REVIEW_NUMBER"));
				data.setReviewScore(rs.getInt("REVIEW_SCORE"));
				data.setReviewRegisterDate(rs.getDate("REVIEW_REGISTER_DATE"));
				data.setReviewContent(rs.getString("REVIEW_CONTENT"));
				data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
				data.setMemberName(rs.getString("MEMBER_NAME"));
				data.setMemberIsWithdraw(rs.getBoolean("MEMBER_IS_WITHDRAW"));
				data.setTotalReviewCount(rs.getLong("TOTAL_REVIEW_COUNT"));
				data.setProductComboNumber(rs.getLong("PRODUCT_COMBO_NUMBER"));
				datas.add(data);
			}
			System.out.println("[M 로그] 조회된 리뷰 개수: " + datas.size());
		}catch(Exception e) {
			System.out.println("[M 로그] SELECTALL에서 발생한 에러 ["+reviewDTO.getCondition()+"]");
			e.printStackTrace();
		}finally {
			JDBCUtil.disconnect(conn, pstmt);
		}
		return datas;
	}
	// [SELECTONE] 특정 리뷰 상세 조회
	public ReviewDTO selectOne(ReviewDTO reviewDTO) {
		ReviewDTO data=null;
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			conn=JDBCUtil.connect();
			System.out.println("[M 로그] SELECTONE 호출, condition: " + reviewDTO.getCondition());
			if(reviewDTO.getCondition().equals("SELECTONEREVIEW")) {
				pstmt=conn.prepareStatement(SELECTONEREVIEW);
				pstmt.setLong(1, reviewDTO.getMemberNumber());
				pstmt.setLong(2, reviewDTO.getProductComboNumber());
				System.out.println("[M 로그] SELECTONEREVIEW 조건 실행 : memberNumber=["+reviewDTO.getMemberNumber()+"], productComboNumber=["+reviewDTO.getProductComboNumber()+"]");
			}
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()) {
				data = new ReviewDTO();
				data.setReviewNumber(rs.getLong("REVIEW_NUMBER"));
				data.setReviewScore(rs.getInt("REVIEW_SCORE"));
				data.setReviewRegisterDate(rs.getDate("REVIEW_REGISTER_DATE"));
				data.setReviewContent(rs.getString("REVIEW_CONTENT"));
				data.setMemberName(rs.getString("MEMBER_NAME"));
				data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
				data.setProductComboNumber(rs.getLong("PRODUCT_COMBO_NUMBER"));	
				data.setMemberIsWithdraw(rs.getBoolean("MEMBER_IS_WITHDRAW"));
			}
		}catch(Exception e) {
			System.out.println("[M 로그] SELECTONE 에러 발생 ("+reviewDTO.getCondition()+")");
			e.printStackTrace();
		}finally {
			JDBCUtil.disconnect(conn, pstmt);
		}
		return data;
	}
	// [INSERT] 리뷰 작성
	public boolean insert(ReviewDTO reviewDTO) {
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			System.out.println("[M 로그] INSERT 호출");
			conn=JDBCUtil.connect();
			// 리뷰 작성 
			pstmt = conn.prepareStatement(INSERTREVIEW);
			pstmt.setInt(1, reviewDTO.getReviewScore());
			pstmt.setString(2, reviewDTO.getReviewContent());
			pstmt.setLong(3, reviewDTO.getMemberNumber());
			pstmt.setLong(4, reviewDTO.getProductComboNumber());
			System.out.println("[M 로그] INSERTREVIEW 실행 : reviewScore ="+reviewDTO.getReviewScore()+"], memberName=["+reviewDTO.getMemberName()+"], memberNumber=["+reviewDTO.getMemberNumber()+"], productComboNumber=["+reviewDTO.getProductComboNumber()+"]");
			int rs=pstmt.executeUpdate();
			System.out.println("[M 로그] INSERT 결과: ["+rs+"]");
			if(rs<=0) {
				return false;
			}
		}catch(Exception e) {
			System.out.println("[M 로그] INSERT 에러 발생 ("+reviewDTO.getCondition()+")");
			e.printStackTrace();
			return false;
		}finally {
			JDBCUtil.disconnect(conn, pstmt);
		}
		return true;
	}
	// [UPDATE] 리뷰 수정
	public boolean update(ReviewDTO reviewDTO) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = JDBCUtil.connect();
			System.out.println("[M 로그] UPDATE 호출");
			// 리뷰 수정
			pstmt = conn.prepareStatement(UPDATEREVIEW);
			pstmt.setLong(1, reviewDTO.getReviewScore());
			pstmt.setString(2, reviewDTO.getReviewContent());
			pstmt.setLong(3, reviewDTO.getReviewNumber());
			System.out.println("[M 로그] UPDATEREVIEW 실행 : reviewScore =["+reviewDTO.getReviewScore()+"], reviewContent =["+reviewDTO.getReviewContent()+"], reviewNumber =["+reviewDTO.getReviewNumber()+"]");
			int rs = pstmt.executeUpdate();
			System.out.println("[M 로그] UPDATE 결과:["+rs+"]");
			return rs > 0;
		} catch (Exception e) {
			System.out.println("[M 로그] UPDATE 에러 발생 ("+reviewDTO.getCondition()+")");
			e.printStackTrace();
			return false;
		} finally {
			JDBCUtil.disconnect(conn, pstmt);
		}
	}

	// [DELETE] 리뷰 삭제
	public boolean delete(ReviewDTO reviewDTO) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = JDBCUtil.connect();
			// 리뷰 삭제
			pstmt = conn.prepareStatement(DELETEREVIEW);
			pstmt.setLong(1, reviewDTO.getReviewNumber());
			System.out.println("[M 로그] DELETEREVIEW 실행 : reviewNumber =["+reviewDTO.getReviewNumber()+"]");
			int rs = pstmt.executeUpdate();
			System.out.println("[M 로그] DELETE 결과:["+rs+"]");
			return rs > 0;
		} catch (Exception e) {
			System.out.println("[M 로그] DELETE 에러 발생 ("+reviewDTO.getCondition()+")");
			e.printStackTrace();
			return false;
		} finally {
			JDBCUtil.disconnect(conn, pstmt);
		}
	}
}

