package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.common.JDBCUtil;
import model.dto.BoardComboLikedDTO;

public class BoardComboLikedDAO {

	// 1. 좋아요 추가
	final String INSERTBOARDCOMBOLIKED = "INSERT INTO BOARD_COMBO_LIKED (BOARD_COMBO_LIKED_NUMBER, MEMBER_NUMBER, BOARD_COMBO_NUMBER) SELECT IFNULL(MAX(BOARD_COMBO_LIKED_NUMBER), 0) + 1, ?, ? FROM BOARD_COMBO_LIKED";
	// 2. 좋아요 삭제
	final String DELETEBOARDCOMBOLIKED = "DELETE FROM BOARD_COMBO_LIKED WHERE MEMBER_NUMBER = ? AND BOARD_COMBO_NUMBER = ?";
	// 3. 좋아요 누른 총 개수, 좋아요 누른 게시물 목록(글 번호, 글 제목, 글 작성자) 출력 - 최신순
	final String SELETEALL = "SELECT BOARD_COMBO_LIKED.BOARD_COMBO_LIKED_NUMBER, BOARD_COMBO_LIKED.BOARD_COMBO_NUMBER, BOARD_COMBO.BOARD_COMBO_TITLE, MEMBER.MEMBER_NAME, BOARD_COMBO_LIKED.MEMBER_NUMBER, "
			+ "COUNT(BOARD_COMBO_LIKED.BOARD_COMBO_NUMBER) OVER() AS TOTAL_COUNT_NUMBER FROM BOARD_COMBO_LIKED JOIN BOARD_COMBO "
			+ "ON BOARD_COMBO_LIKED.BOARD_COMBO_NUMBER = BOARD_COMBO.BOARD_COMBO_NUMBER JOIN MEMBER ON BOARD_COMBO.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER "
			+ "WHERE BOARD_COMBO_LIKED.MEMBER_NUMBER = ? ORDER BY BOARD_COMBO_LIKED.BOARD_COMBO_NUMBER DESC LIMIT ?, ?"; 
	// 4. 글 번호와 회원 번호가 둘 다 있다면 정보를 가져옴
	final String SELECTONE = "SELECT BOARD_COMBO_LIKED_NUMBER, MEMBER_NUMBER, BOARD_COMBO_NUMBER FROM BOARD_COMBO_LIKED WHERE MEMBER_NUMBER = ? AND BOARD_COMBO_NUMBER = ?";
	public ArrayList<BoardComboLikedDTO> selectAll(BoardComboLikedDTO boardComboLikedDTO){
		ArrayList<BoardComboLikedDTO> datas=new ArrayList<BoardComboLikedDTO>();
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			conn=JDBCUtil.connect();
			pstmt=conn.prepareStatement(SELETEALL);				
			pstmt.setLong(1, boardComboLikedDTO.getMemberNumber());
			pstmt.setInt(2, boardComboLikedDTO.getBoardComboLikedIndex());
			pstmt.setInt(3, boardComboLikedDTO.getBoardComboLikedContentCount());
			ResultSet rs=pstmt.executeQuery();
			while(rs.next()) {
				BoardComboLikedDTO data = new BoardComboLikedDTO();
				data.setBoardComboLikedNumber(rs.getLong("BOARD_COMBO_LIKED_NUMBER"));
				data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
				data.setMemberName(rs.getString("MEMBER_NAME"));
				data.setBoardComboNumber(rs.getLong("BOARD_COMBO_NUMBER"));
				data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));
				datas.add(data);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			JDBCUtil.disconnect(conn, pstmt);
		}
		return datas;
	}
	
	public BoardComboLikedDTO selectOne(BoardComboLikedDTO boardComboLikedDTO){
		BoardComboLikedDTO data=null;
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			conn=JDBCUtil.connect();
			pstmt=conn.prepareStatement(SELECTONE);
			pstmt.setLong(1, boardComboLikedDTO.getMemberNumber());
			pstmt.setLong(2, boardComboLikedDTO.getBoardComboNumber());
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()) {
				data = new BoardComboLikedDTO();
				data.setBoardComboLikedNumber(rs.getLong("BOARD_COMBO_LIKED_NUMBER"));
				data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
				data.setBoardComboNumber(rs.getLong("BOARD_COMBO_NUMBER"));
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			JDBCUtil.disconnect(conn, pstmt);
		}
		return data;
	}
	
	public boolean insert(BoardComboLikedDTO boardComboLikedDTO){
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			conn=JDBCUtil.connect();
			pstmt=conn.prepareStatement(INSERTBOARDCOMBOLIKED);
			pstmt.setLong(1, boardComboLikedDTO.getMemberNumber());
			pstmt.setLong(2, boardComboLikedDTO.getBoardComboNumber());
			int rs=pstmt.executeUpdate();
			if(rs<=0) {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			JDBCUtil.disconnect(conn, pstmt);
		}
		return true;
	}
	
	private boolean update(BoardComboLikedDTO boardComboLikedDTO){
		return false;
	}
	
	public boolean delete(BoardComboLikedDTO boardComboLikedDTO){
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			conn=JDBCUtil.connect();
			pstmt=conn.prepareStatement(DELETEBOARDCOMBOLIKED);
			pstmt.setLong(1, boardComboLikedDTO.getMemberNumber());
			pstmt.setLong(2, boardComboLikedDTO.getBoardComboNumber());
			int rs=pstmt.executeUpdate();
			if(rs<=0) {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}finally {
			JDBCUtil.disconnect(conn, pstmt);
		}
		return true;
	}
}
