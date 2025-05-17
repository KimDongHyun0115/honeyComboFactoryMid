package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.common.JDBCUtil;
import model.dto.PurchaseDTO;
public class PurchaseDAO {

	// 1. 모든 주문 목록 출력 (촤신순)
	final String SELECTALL ="SELECT PURCHASE_NUMBER, PURCHASE_TERMINAL_ID, PURCHASE_TOTAL_PRICE, MEMBER_NUMBER FROM PURCHASE "
			+ "ORDER BY PURCHASE_NUMBER DESC LIMIT ?, ?";
	// 2. 한 회원의 모든 주문 목록 출력
	final String SELECTALLONEPERSON = "SELECT P.PURCHASE_NUMBER, P.PURCHASE_TERMINAL_ID, P.PURCHASE_TOTAL_PRICE, P.MEMBER_NUMBER, C.TOTAL_COUNT_NUMBER"
			+ " FROM PURCHASE P"
			+ " JOIN (SELECT MEMBER_NUMBER, COUNT(*) AS TOTAL_COUNT_NUMBER FROM PURCHASE WHERE MEMBER_NUMBER = ?) C USING (MEMBER_NUMBER)"
			+ " WHERE P.MEMBER_NUMBER = ?"
			+ " ORDER BY P.PURCHASE_NUMBER DESC"
			+ " LIMIT ? OFFSET ?";
	// 3. 주문 추가
	final String INSERT = "INSERT INTO PURCHASE (PURCHASE_NUMBER, PURCHASE_TERMINAL_ID,  PURCHASE_TOTAL_PRICE, MEMBER_NUMBER) "
			+ "SELECT IFNULL(MAX(PURCHASE_NUMBER), 39999) + 1,  ?, ?, ?) FROM PURCHASE";
	// 4. 주문 취소
	final String DELETEPURCHASE ="DELETE FROM PURCHASE WHERE PURCHASE_NUMBER = ?";
	// 5. 탈퇴한 회원의 주문 목록 삭제
	final String DELETECANCELMEMBER ="DELETE FROM PURCHASE WHERE MEMBER_NUMBER = ?";
	// 6. 주문 정보 1개 조회
	final String SELECTONE = "SELECT PURCHASE_NUMBER, PURCHASE_TERMINAL_ID, PURCHASE_TOTAL_PRICE, MEMBER_NUMBER FROM PURCHASE WHERE PURCHASE_NUMBER = ?";


	public ArrayList<PurchaseDTO> selectAll(PurchaseDTO purchaseDTO) {
		ArrayList<PurchaseDTO> datas = new ArrayList<PurchaseDTO>();
		System.out.println("[selectAll] 전체 주문 목록 조회 시작");

		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			conn=JDBCUtil.connect();
			System.out.println("[selectAll] DB 연결 완료");

			if(purchaseDTO.getCondition().equals("SELECTALL")) {
				System.out.println("[selectAll] SELECTALL 조건 실행");
				pstmt=conn.prepareStatement(SELECTALL);
				System.out.println(">> 쿼리 준비 완료: " + pstmt);
				pstmt.setInt(1, purchaseDTO.getPurchaseIndex());
				pstmt.setInt(2, purchaseDTO.getPurchaseContentCount());
				System.out.println(">> selectall = " + purchaseDTO.getPurchaseIndex() + ", " + purchaseDTO.getPurchaseContentCount());

			}
			else if(purchaseDTO.getCondition().equals("SELECTALLONEPERSON")) {
				System.out.println("[selectAll] SELECTALLONEPERSON 조건 실행");
				pstmt=conn.prepareStatement(SELECTALLONEPERSON);
				System.out.println(">> 쿼리 준비 완료: " + pstmt);
				pstmt.setLong(1, purchaseDTO.getMemberNumber());
				pstmt.setLong(2, purchaseDTO.getMemberNumber());
				pstmt.setInt(3, purchaseDTO.getPurchaseContentCount());
				pstmt.setInt(4, purchaseDTO.getPurchaseIndex());
				System.out.println(">> selectallperson= " + purchaseDTO.getMemberNumber());

			}
			ResultSet rs=pstmt.executeQuery();
			System.out.println("[selectAll] 쿼리 실행 완료");

			while(rs.next()) {
				PurchaseDTO data = new PurchaseDTO ();
				data.setPurchaseNumber(rs.getLong("PURCHASE_NUMBER"));
				data.setPurchaseTerminalId(rs.getString("PURCHASE_TERMINAL_ID"));
				data.setPurchaseTotalPrice(rs.getLong("PURCHASE_TOTAL_PRICE"));
				data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
				data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));
				datas.add(data);
				System.out.println(">> purchasenumber " + data.getPurchaseNumber());


			}
		}catch(Exception e) {
			System.out.println("[selectAll] 예외 발생: " + e.getMessage());

			e.printStackTrace();
		}finally {
			JDBCUtil.disconnect(conn, pstmt);
			System.out.println("[selectAll] DB 연결 해제");

		}
		return datas;
	}

	public PurchaseDTO selectOne(PurchaseDTO purchaseDTO){
		System.out.println("[selectOne] 주문 단건 조회 시작");
		PurchaseDTO data=null;
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			conn=JDBCUtil.connect();
			System.out.println("[selectOne] DB 연결 완료");

			pstmt=conn.prepareStatement(SELECTONE);
			pstmt.setLong(1, purchaseDTO.getPurchaseNumber());
			System.out.println(">> 조회할 PURCHASE_NUMBER: " + purchaseDTO.getPurchaseNumber());

			ResultSet rs=pstmt.executeQuery();
			if(rs.next()) {
				data = new PurchaseDTO();
				data.setPurchaseNumber(rs.getLong("PURCHASE_NUMBER"));
				data.setPurchaseTerminalId(rs.getString("PURCHASE_TERMINAL_ID"));
				data.setPurchaseTotalPrice(rs.getLong("PURCHASE_TOTAL_PRICE"));
				data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
				System.out.println(">> 주문 정보 조회 성공: " + data.getPurchaseNumber());
			}
		}catch(Exception e) {
            System.err.println("[selectOne] 예외 발생: " + e.getMessage());

			e.printStackTrace();
		}finally {
			JDBCUtil.disconnect(conn, pstmt);
            System.out.println("[selectOne] DB 연결 해제");

		}
		return data;
	}

	public boolean insert(PurchaseDTO purchaseDTO){
		 System.out.println("[insert] 주문 등록 시작");
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			conn=JDBCUtil.connect();
            System.out.println("[insert] DB 연결 완료");

			pstmt=conn.prepareStatement(INSERT);
			pstmt.setLong(1, purchaseDTO.getPurchaseNumber());
			pstmt.setString(2, purchaseDTO.getPurchaseTerminalId());
			pstmt.setLong(3, purchaseDTO.getPurchaseTotalPrice());
			pstmt.setLong(4, purchaseDTO.getMemberNumber());
			int rs=pstmt.executeUpdate();
            System.out.println("[insert] 쿼리 실행 완료, 결과: " + rs);

			if(rs<=0) {
				return false;
			}
		}catch(Exception e) {
            System.out.println("[insert] 예외 발생: " + e.getMessage());

			e.printStackTrace();
			return false;
		}finally {
			JDBCUtil.disconnect(conn, pstmt);
            System.out.println("[insert] DB 연결 해제");

		}
		return true;
	}

	private boolean update(PurchaseDTO purchaseDTO){
		  System.out.println("[update] 현재 미구현");
		return false;

	}
	public boolean delete(PurchaseDTO purchaseDTO){
        System.out.println("[delete] DB 연결 완료");

		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			conn=JDBCUtil.connect();
			if(purchaseDTO.getCondition().equals("DELETEPURCHASE")) {
				pstmt=conn.prepareStatement(DELETEPURCHASE);
				pstmt.setLong(1, purchaseDTO.getPurchaseNumber());
                System.out.println(">> 삭제할 주문번호: " + purchaseDTO.getPurchaseNumber());


			}
			else if(purchaseDTO.getCondition().equals("DELETECANCELMEMBER")) {
				pstmt=conn.prepareStatement(DELETECANCELMEMBER);
				pstmt.setLong(1, purchaseDTO.getMemberNumber());
                System.out.println(">> 탈퇴 회원의 주문 전체 삭제 -회원번호: " + purchaseDTO.getMemberNumber());

			}
			int rs=pstmt.executeUpdate();
            System.out.println("[delete] 쿼리 실행 완료, 결과: " + rs);

			if(rs<=0) {
				return false;
			}
		}catch(Exception e) {
            System.out.println("[delete] 예외 발생: " + e.getMessage());

			e.printStackTrace();
			return false;
		}finally {
			JDBCUtil.disconnect(conn, pstmt);
            System.out.println("[delete] DB 연결 해제");

		}
		return true;
	}
}

