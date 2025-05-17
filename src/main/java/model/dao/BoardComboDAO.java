package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.common.JDBCUtil;
import model.dto.BoardComboDTO;
public class BoardComboDAO {

	/*[1] 꿀조합 게시판 글 작성 (C)
	 * PK값인 BOARD_COMBO_NUMBER는 가장 큰 번호 + 1 해서 매 데이터 생성마다 PK 하나씩 증가
	 */
	final String INSERTCOMBOBOARD = "INSERT INTO BOARD_COMBO (BOARD_COMBO_NUMBER, BOARD_COMBO_TITLE, BOARD_COMBO_CONTENT, MEMBER_NUMBER, MEMBER_NAME) "
			+ "SELECT IFNULL(MAX(BOARD_COMBO_NUMBER), 0) + 1, ?, ?, ?, ? FROM BOARD_COMBO";
	
	// [2] 꿀조합 게시판 글 목록 조회(최신순) (R - ALL)
	final String SELECTALLCOMBOBOARDDESC = "SELECT BOARD_COMBO.BOARD_COMBO_NUMBER, MEMBER.MEMBER_IS_ADMIN, MEMBER.MEMBER_NUMBER, BOARD_COMBO.BOARD_COMBO_TITLE, MEMBER.MEMBER_NAME, BOARD_COMBO.BOARD_COMBO_CONTENT, "
			+ "BOARD_COMBO.BOARD_COMBO_REGISTER_DATE, BOARD_COMBO.BOARD_COMBO_VIEW_COUNT, COALESCE(BOARD_COMBO_LIKED_COUNT.LIKE_COUNT, 0) AS BOARD_COMBO_LIKED_COUNT, "
			+ "(SELECT COUNT(*) FROM BOARD_COMBO) AS TOTAL_COUNT_NUMBER FROM BOARD_COMBO JOIN MEMBER ON BOARD_COMBO.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER "
			+ "LEFT JOIN (SELECT BOARD_COMBO_NUMBER, COUNT(*) AS LIKE_COUNT FROM BOARD_COMBO_LIKED GROUP BY BOARD_COMBO_NUMBER) BOARD_COMBO_LIKED_COUNT "
			+ "ON BOARD_COMBO.BOARD_COMBO_NUMBER = BOARD_COMBO_LIKED_COUNT.BOARD_COMBO_NUMBER ORDER BY BOARD_COMBO.BOARD_COMBO_NUMBER DESC LIMIT ?, ?";
	
	// 3. 꿀조합 게시판 글 목록 조회 R ALL (오래된순)
	final String SELECTALLCOMBOBOARDASC = "SELECT BOARD_COMBO.BOARD_COMBO_NUMBER, MEMBER.MEMBER_IS_ADMIN, BOARD_COMBO.BOARD_COMBO_TITLE, MEMBER.MEMBER_NUMBER, BOARD_COMBO.BOARD_COMBO_CONTENT, "
			+ "BOARD_COMBO.BOARD_COMBO_REGISTER_DATE, BOARD_COMBO.BOARD_COMBO_VIEW_COUNT, MEMBER.MEMBER_NAME, COALESCE(BOARD_COMBO_LIKED_COUNT.LIKE_COUNT, 0) AS BOARD_COMBO_LIKED_COUNT, "
			+ "(SELECT COUNT(*) FROM BOARD_COMBO) AS TOTAL_COUNT_NUMBER FROM BOARD_COMBO JOIN MEMBER ON BOARD_COMBO.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER "
			+ "LEFT JOIN (SELECT BOARD_COMBO_NUMBER, COUNT(*) AS LIKE_COUNT FROM BOARD_COMBO_LIKED GROUP BY BOARD_COMBO_NUMBER) BOARD_COMBO_LIKED_COUNT "
			+ "ON BOARD_COMBO.BOARD_COMBO_NUMBER = BOARD_COMBO_LIKED_COUNT.BOARD_COMBO_NUMBER ORDER BY BOARD_COMBO.BOARD_COMBO_NUMBER ASC LIMIT ?, ?";
	
	// 4. 꿀조합 게시판 글 목록 조회 R ALL (인기순)
	final String SELECTALLCOMBOBOARDPOPULAR = "SELECT BOARD_COMBO.BOARD_COMBO_NUMBER, MEMBER.MEMBER_IS_ADMIN, BOARD_COMBO.BOARD_COMBO_TITLE, BOARD_COMBO.MEMBER_NUMBER, BOARD_COMBO.BOARD_COMBO_CONTENT, BOARD_COMBO.BOARD_COMBO_REGISTER_DATE, "
			+ "BOARD_COMBO.BOARD_COMBO_VIEW_COUNT, MEMBER.MEMBER_NAME, COALESCE(BOARD_COMBO_LIKED_COUNT.LIKE_COUNT, 0) AS BOARD_COMBO_LIKED_COUNT, (SELECT COUNT(*) FROM BOARD_COMBO) AS TOTAL_COUNT_NUMBER "
			+ "FROM BOARD_COMBO JOIN MEMBER ON BOARD_COMBO.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER LEFT JOIN (SELECT BOARD_COMBO_NUMBER, COUNT(*) AS LIKE_COUNT "
			+ "FROM BOARD_COMBO_LIKED GROUP BY BOARD_COMBO_NUMBER) BOARD_COMBO_LIKED_COUNT ON BOARD_COMBO.BOARD_COMBO_NUMBER = BOARD_COMBO_LIKED_COUNT.BOARD_COMBO_NUMBER "
			+ "ORDER BY BOARD_COMBO_LIKED_COUNT.LIKE_COUNT DESC LIMIT ?, ?";
	
	// 5. 꿀조합 게시판 상세글 출력 R ONE
	final String SELECTONECOMBOBOARD = "SELECT BOARD_COMBO.BOARD_COMBO_NUMBER, BOARD_COMBO.MEMBER_NUMBER, MEMBER.MEMBER_IS_ADMIN, MEMBER.MEMBER_NAME, BOARD_COMBO.BOARD_COMBO_TITLE, "
			+ "BOARD_COMBO.BOARD_COMBO_VIEW_COUNT, BOARD_COMBO.BOARD_COMBO_CONTENT, BOARD_COMBO.BOARD_COMBO_REGISTER_DATE, (SELECT COUNT(*) FROM BOARD_COMBO_LIKED "
			+ "WHERE BOARD_COMBO_LIKED.BOARD_COMBO_NUMBER = BOARD_COMBO.BOARD_COMBO_NUMBER) AS BOARD_COMBO_LIKED_COUNT FROM BOARD_COMBO LEFT JOIN MEMBER ON "
			+ "BOARD_COMBO.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER WHERE BOARD_COMBO.BOARD_COMBO_NUMBER = ?";
	
	// 6. 꿀조합글 게시판 검색 R ALL (최신순)
	final String SELECTALLCOMBOBOARDSEARCHDESC = "SELECT BOARD_COMBO.BOARD_COMBO_NUMBER, MEMBER.MEMBER_NAME, MEMBER.MEMBER_IS_ADMIN, MEMBER.MEMBER_NUMBER, BOARD_COMBO.BOARD_COMBO_TITLE, BOARD_COMBO.BOARD_COMBO_CONTENT, "
			+ "BOARD_COMBO.BOARD_COMBO_VIEW_COUNT, (SELECT COUNT(*) FROM BOARD_COMBO_LIKED WHERE BOARD_COMBO_LIKED.BOARD_COMBO_NUMBER = BOARD_COMBO.BOARD_COMBO_NUMBER) AS BOARD_COMBO_LIKED_COUNT, "
			+ "BOARD_COMBO.BOARD_COMBO_REGISTER_DATE, COUNT(*) OVER() AS TOTAL_COUNT_NUMBER FROM BOARD_COMBO LEFT JOIN MEMBER ON BOARD_COMBO.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER "
			+ "WHERE BOARD_COMBO.BOARD_COMBO_TITLE LIKE CONCAT('%', ?, '%') ORDER BY BOARD_COMBO.BOARD_COMBO_REGISTER_DATE DESC LIMIT ?, ?";
	
	// 7. 꿀조합글 게시판 검색 R ALL (오래된순)
	final String SELECTALLCOMBOBOARDSEARCHASC = "SELECT BOARD_COMBO.BOARD_COMBO_NUMBER, MEMBER.MEMBER_NAME, MEMBER.MEMBER_IS_ADMIN, MEMBER.MEMBER_NUMBER, BOARD_COMBO.BOARD_COMBO_TITLE, BOARD_COMBO.BOARD_COMBO_CONTENT, "
			+ "BOARD_COMBO.BOARD_COMBO_VIEW_COUNT, (SELECT COUNT(*) FROM BOARD_COMBO_LIKED WHERE BOARD_COMBO_LIKED.BOARD_COMBO_NUMBER = BOARD_COMBO.BOARD_COMBO_NUMBER) AS BOARD_COMBO_LIKED_COUNT, "
			+ "BOARD_COMBO.BOARD_COMBO_REGISTER_DATE, COUNT(*) OVER() AS TOTAL_COUNT_NUMBER FROM BOARD_COMBO LEFT JOIN MEMBER ON BOARD_COMBO.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER "
			+ "WHERE BOARD_COMBO.BOARD_COMBO_TITLE LIKE CONCAT('%', ?, '%') ORDER BY BOARD_COMBO.BOARD_COMBO_REGISTER_DATE ASC LIMIT ?, ?";
	
	// 8. 꿀조합글 게시판 검색 R ALL (인기순)
	final String SELECTALLCOMBOBOARDSEARCHPOPULAR = "SELECT BOARD_COMBO.BOARD_COMBO_NUMBER, MEMBER.MEMBER_NAME, MEMBER.MEMBER_IS_ADMIN, MEMBER.MEMBER_NUMBER, BOARD_COMBO.BOARD_COMBO_TITLE, BOARD_COMBO.BOARD_COMBO_CONTENT, "
			+ "BOARD_COMBO.BOARD_COMBO_VIEW_COUNT, BOARD_COMBO.BOARD_COMBO_REGISTER_DATE, COALESCE(LIKE_COUNT.LIKE_COUNT, 0) AS BOARD_COMBO_LIKED_COUNT, COUNT(*) OVER() AS TOTAL_COUNT_NUMBER "
			+ "FROM BOARD_COMBO JOIN MEMBER ON BOARD_COMBO.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER LEFT JOIN (SELECT BOARD_COMBO_NUMBER, COUNT(*) AS LIKE_COUNT FROM BOARD_COMBO_LIKED "
			+ "GROUP BY BOARD_COMBO_NUMBER) LIKE_COUNT ON BOARD_COMBO.BOARD_COMBO_NUMBER = LIKE_COUNT.BOARD_COMBO_NUMBER WHERE BOARD_COMBO.BOARD_COMBO_TITLE LIKE CONCAT('%', ?, '%') "
			+ "ORDER BY LIKE_COUNT.LIKE_COUNT DESC LIMIT ?, ?";
	
	// 9. 꿀조합 게시판 글 수정 U
	final String UPDATECOMBOBOARD = "UPDATE BOARD_COMBO SET BOARD_COMBO_TITLE = ?, BOARD_COMBO_CONTENT = ?  WHERE BOARD_COMBO_NUMBER = ?";	
	
	// 10. 꿀조합 게시판 글 삭제 D
	final String DELETECOMBOBOARD = "DELETE FROM BOARD_COMBO WHERE BOARD_COMBO_NUMBER = ?";
	
	// 11. 한 사용자가 작성한 꿀조합 게시판 글 개수 포함 출력 R ALL
	final String SELECTALLMEMBERWRITE = "SELECT BOARD_COMBO.BOARD_COMBO_NUMBER, MEMBER.MEMBER_NAME, MEMBER.MEMBER_IS_ADMIN, MEMBER.MEMBER_NUMBER, BOARD_COMBO.BOARD_COMBO_TITLE, "
			+ "BOARD_COMBO.BOARD_COMBO_CONTENT, BOARD_COMBO.BOARD_COMBO_VIEW_COUNT, COALESCE(BOARD_COMBO_LIKED_COUNT.LIKE_COUNT, 0) AS BOARD_COMBO_LIKED_COUNT, "
			+ "BOARD_COMBO.BOARD_COMBO_REGISTER_DATE, (SELECT COUNT(*) FROM BOARD_COMBO WHERE MEMBER_NUMBER = MEMBER.MEMBER_NUMBER) AS TOTAL_COUNT_NUMBER, (SELECT COUNT(*) FROM BOARD_COMBO WHERE MEMBER_NUMBER = ?) "
			+ "FROM BOARD_COMBO JOIN MEMBER ON BOARD_COMBO.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER LEFT JOIN (SELECT BOARD_COMBO_NUMBER, COUNT(*) AS LIKE_COUNT "
			+ "FROM BOARD_COMBO_LIKED GROUP BY BOARD_COMBO_NUMBER) BOARD_COMBO_LIKED_COUNT ON BOARD_COMBO.BOARD_COMBO_NUMBER = BOARD_COMBO_LIKED_COUNT.BOARD_COMBO_NUMBER "
			+ "WHERE BOARD_COMBO.MEMBER_NUMBER = ? ORDER BY BOARD_COMBO.BOARD_COMBO_REGISTER_DATE DESC LIMIT ?, ?";
	
	// 12. 관리자가 작성한 글 목록 출력 // 관리자 글 좋아요 수 안줘도 됨.
	final String SELECTALLADMINCONTENT = "SELECT BOARD_COMBO.BOARD_COMBO_NUMBER, MEMBER.MEMBER_NUMBER, BOARD_COMBO.BOARD_COMBO_TITLE, BOARD_COMBO.BOARD_COMBO_CONTENT, BOARD_COMBO.BOARD_COMBO_REGISTER_DATE, "
			+ "BOARD_COMBO.BOARD_COMBO_VIEW_COUNT, MEMBER.MEMBER_NAME, MEMBER.MEMBER_IS_ADMIN, COALESCE(BOARD_COMBO_LIKED_COUNT.LIKE_COUNT, 0) AS BOARD_COMBO_LIKED_COUNT, "
			+ "COUNT(*) OVER() AS TOTAL_COUNT_NUMBER FROM BOARD_COMBO JOIN MEMBER ON BOARD_COMBO.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER LEFT JOIN (SELECT BOARD_COMBO_NUMBER, "
			+ "COUNT(*) AS LIKE_COUNT FROM BOARD_COMBO_LIKED GROUP BY BOARD_COMBO_NUMBER) BOARD_COMBO_LIKED_COUNT ON BOARD_COMBO.BOARD_COMBO_NUMBER = BOARD_COMBO_LIKED_COUNT.BOARD_COMBO_NUMBER "
			+ "WHERE MEMBER.MEMBER_IS_ADMIN = 1 ORDER BY BOARD_COMBO.BOARD_COMBO_REGISTER_DATE DESC LIMIT ?, ?";
	
	// 13. 회원 글 목록 출력 (최신순) // 여기부터 추가
	final String SELECTALLMEMBERCONTENTDESC = "SELECT BOARD_COMBO.BOARD_COMBO_NUMBER, MEMBER.MEMBER_NUMBER, BOARD_COMBO.BOARD_COMBO_TITLE, BOARD_COMBO.BOARD_COMBO_CONTENT, "
			+ "BOARD_COMBO.BOARD_COMBO_REGISTER_DATE, BOARD_COMBO.BOARD_COMBO_VIEW_COUNT, MEMBER.MEMBER_NAME, MEMBER.MEMBER_IS_ADMIN, COALESCE(BOARD_COMBO_LIKED_COUNT.LIKE_COUNT, 0) AS BOARD_COMBO_LIKED_COUNT, "
			+ "COUNT(*) OVER() AS TOTAL_COUNT_NUMBER FROM BOARD_COMBO JOIN MEMBER ON BOARD_COMBO.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER LEFT JOIN (SELECT BOARD_COMBO_NUMBER, COUNT(*) AS LIKE_COUNT "
			+ "FROM BOARD_COMBO_LIKED GROUP BY BOARD_COMBO_NUMBER) BOARD_COMBO_LIKED_COUNT ON BOARD_COMBO.BOARD_COMBO_NUMBER = BOARD_COMBO_LIKED_COUNT.BOARD_COMBO_NUMBER WHERE MEMBER.MEMBER_IS_ADMIN = 0 "
			+ "ORDER BY BOARD_COMBO.BOARD_COMBO_REGISTER_DATE DESC LIMIT ?, ?";
	// 14. 회원 글 목록 출력 (오래된순)
	final String SELECTALLMEMBERCONTENTASC = "SELECT BOARD_COMBO.BOARD_COMBO_NUMBER, MEMBER.MEMBER_NUMBER, BOARD_COMBO.BOARD_COMBO_TITLE, BOARD_COMBO.BOARD_COMBO_CONTENT, BOARD_COMBO.BOARD_COMBO_REGISTER_DATE, "
			+ "BOARD_COMBO.BOARD_COMBO_VIEW_COUNT, MEMBER.MEMBER_NAME, MEMBER.MEMBER_IS_ADMIN, COALESCE(BOARD_COMBO_LIKED_COUNT.LIKE_COUNT, 0) AS BOARD_COMBO_LIKED_COUNT, COUNT(*) OVER() AS TOTAL_COUNT_NUMBER "
			+ "FROM BOARD_COMBO JOIN MEMBER ON BOARD_COMBO.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER LEFT JOIN (SELECT BOARD_COMBO_NUMBER, COUNT(*) AS LIKE_COUNT FROM BOARD_COMBO_LIKED "
			+ "GROUP BY BOARD_COMBO_NUMBER) BOARD_COMBO_LIKED_COUNT ON BOARD_COMBO.BOARD_COMBO_NUMBER = BOARD_COMBO_LIKED_COUNT.BOARD_COMBO_NUMBER WHERE MEMBER.MEMBER_IS_ADMIN = 0 "
			+ "ORDER BY BOARD_COMBO.BOARD_COMBO_REGISTER_DATE ASC LIMIT ?, ?";
	
	// 15. 회원 글 목록 출력 (인기순)
	final String SELECTALLMEMBERCONTENTPOPULAR = "SELECT BOARD_COMBO.BOARD_COMBO_NUMBER, MEMBER.MEMBER_NUMBER, MEMBER.MEMBER_IS_ADMIN, BOARD_COMBO.BOARD_COMBO_TITLE, BOARD_COMBO.BOARD_COMBO_CONTENT, "
			+ "BOARD_COMBO.BOARD_COMBO_REGISTER_DATE, BOARD_COMBO.BOARD_COMBO_VIEW_COUNT, MEMBER.MEMBER_NAME, MEMBER.MEMBER_IS_ADMIN, "
			+ "COALESCE(BOARD_COMBO_LIKED_COUNT.LIKE_COUNT, 0) AS BOARD_COMBO_LIKED_COUNT, COUNT(*) OVER() AS TOTAL_COUNT_NUMBER FROM BOARD_COMBO "
			+ "JOIN MEMBER ON BOARD_COMBO.MEMBER_NUMBER = MEMBER.MEMBER_NUMBER LEFT JOIN (SELECT BOARD_COMBO_NUMBER, COUNT(*) AS LIKE_COUNT FROM BOARD_COMBO_LIKED "
			+ "GROUP BY BOARD_COMBO_NUMBER) BOARD_COMBO_LIKED_COUNT ON BOARD_COMBO.BOARD_COMBO_NUMBER = BOARD_COMBO_LIKED_COUNT.BOARD_COMBO_NUMBER "
			+ "WHERE MEMBER.MEMBER_IS_ADMIN = 0 ORDER BY BOARD_COMBO_LIKED_COUNT.LIKE_COUNT DESC LIMIT ?, ?";


	public ArrayList<BoardComboDTO> selectAll(BoardComboDTO boardComboDTO){
		ArrayList<BoardComboDTO> datas=new ArrayList<BoardComboDTO>();
		BoardComboDTO data=null;
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			conn=JDBCUtil.connect();
			if(boardComboDTO.getCondition().equals("SELECTALLCOMBOBOARDDESC")) {
				pstmt=conn.prepareStatement(SELECTALLCOMBOBOARDDESC);
				pstmt.setInt(1, boardComboDTO.getBoardComboIndex());
				pstmt.setInt(2, boardComboDTO.getBoardComboContentCount());
			} 
			else if(boardComboDTO.getCondition().equals("SELECTALLCOMBOBOARDASC")) {
				pstmt=conn.prepareStatement(SELECTALLCOMBOBOARDASC );
				pstmt.setInt(1, boardComboDTO.getBoardComboIndex());
				pstmt.setInt(2, boardComboDTO.getBoardComboContentCount());
			} 
			else if(boardComboDTO.getCondition().equals("SELECTALLCOMBOBOARDPOPULAR")) {
				pstmt=conn.prepareStatement(SELECTALLCOMBOBOARDPOPULAR);
				pstmt.setInt(1, boardComboDTO.getBoardComboIndex());
				pstmt.setInt(2, boardComboDTO.getBoardComboContentCount());
			}
			else if(boardComboDTO.getCondition().equals("SELECTALLCOMBOBOARDSEARCHDESC")) {
				pstmt=conn.prepareStatement(SELECTALLCOMBOBOARDSEARCHDESC);
				pstmt.setString(1, boardComboDTO.getSearchKeyword());
				pstmt.setInt(2, boardComboDTO.getBoardComboIndex());
				pstmt.setInt(3, boardComboDTO.getBoardComboContentCount());
			}
			else if(boardComboDTO.getCondition().equals("SELECTALLCOMBOBOARDSEARCHASC")) {
				pstmt=conn.prepareStatement(SELECTALLCOMBOBOARDSEARCHASC);
				pstmt.setString(1, boardComboDTO.getSearchKeyword());
				pstmt.setInt(2, boardComboDTO.getBoardComboIndex());
				pstmt.setInt(3, boardComboDTO.getBoardComboContentCount());
			}
			else if(boardComboDTO.getCondition().equals("SELECTALLCOMBOBOARDSEARCHPOPULAR")) {
				pstmt=conn.prepareStatement(SELECTALLCOMBOBOARDSEARCHPOPULAR);
				pstmt.setString(1, boardComboDTO.getSearchKeyword());
				pstmt.setInt(2, boardComboDTO.getBoardComboIndex());
				pstmt.setInt(3, boardComboDTO.getBoardComboContentCount());
			}
			else if(boardComboDTO.getCondition().equals("SELECTALLMEMBERWRITE")) {
				pstmt=conn.prepareStatement(SELECTALLMEMBERWRITE);
				pstmt.setLong(1, boardComboDTO.getMemberNumber());
				pstmt.setLong(2, boardComboDTO.getMemberNumber());
				pstmt.setInt(3, boardComboDTO.getBoardComboIndex());
				pstmt.setInt(4, boardComboDTO.getBoardComboContentCount());
			}
			else if(boardComboDTO.getCondition().equals("SELECTALLADMINCONTENT")) {
				pstmt=conn.prepareStatement(SELECTALLADMINCONTENT);
				pstmt.setInt(1, boardComboDTO.getBoardComboIndex());
				pstmt.setInt(2, boardComboDTO.getBoardComboContentCount());
			}
			else if(boardComboDTO.getCondition().equals("SELECTALLMEMBERCONTENTDESC")) {
				pstmt=conn.prepareStatement(SELECTALLMEMBERCONTENTDESC);
				pstmt.setInt(1, boardComboDTO.getBoardComboIndex());
				pstmt.setInt(2, boardComboDTO.getBoardComboContentCount());
			}
			else if(boardComboDTO.getCondition().equals("SELECTALLMEMBERCONTENTASC")) {
				pstmt=conn.prepareStatement(SELECTALLMEMBERCONTENTASC);
				pstmt.setInt(1, boardComboDTO.getBoardComboIndex());
				pstmt.setInt(2, boardComboDTO.getBoardComboContentCount());
			}
			else if(boardComboDTO.getCondition().equals("SELECTALLMEMBERCONTENTPOPULAR")) {
				pstmt=conn.prepareStatement(SELECTALLMEMBERCONTENTPOPULAR);
				pstmt.setInt(1, boardComboDTO.getBoardComboIndex());
				pstmt.setInt(2, boardComboDTO.getBoardComboContentCount());
			}
			else {
				System.out.println("[ERROR] 알 수 없는 condition입니다: " + boardComboDTO.getCondition());
				return datas; // 빈 리스트 반환
			}
			ResultSet rs=pstmt.executeQuery();
			while (rs.next()) {
				data = new BoardComboDTO();

					data.setBoardComboNumber(rs.getLong("BOARD_COMBO_NUMBER"));
					data.setBoardComboTitle(rs.getString("BOARD_COMBO_TITLE"));
					data.setBoardComboContent(rs.getString("BOARD_COMBO_CONTENT"));
					data.setBoardComboViewCount(rs.getLong("BOARD_COMBO_VIEW_COUNT"));
					data.setBoardComboRegisterDate(rs.getDate("BOARD_COMBO_REGISTER_DATE"));
					data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
					data.setMemberName(rs.getString("MEMBER_NAME"));
					data.setBoardComboLikedCount(rs.getLong("BOARD_COMBO_LIKED_COUNT"));
					data.setMemberIsAdmin(rs.getBoolean("MEMBER_IS_ADMIN"));
					data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));
					
				// 좋아요 수가 쿼리 결과에 포함된 경우만 세팅
				try {
					long liked = rs.getLong("BOARD_COMBO_LIKED_COUNT");
					data.setBoardComboLikedCount(liked);
				} catch (SQLException e) {
					// 컬럼 없으면 무시
				}
				datas.add(data);
				System.out.println("글번호: " + data.getBoardComboNumber()
			    + ", 좋아요 수: " + data.getBoardComboLikedCount());
			}


		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			JDBCUtil.disconnect(conn, pstmt);
		}
		return datas;

	}

	public BoardComboDTO selectOne(BoardComboDTO boardComboDTO){ 
		BoardComboDTO data=null;
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			conn=JDBCUtil.connect();
				pstmt=conn.prepareStatement(SELECTONECOMBOBOARD);
				pstmt.setLong(1, boardComboDTO.getBoardComboNumber());
			ResultSet rs=pstmt.executeQuery();
			if(rs.next()) {
					data = new BoardComboDTO();
					data.setBoardComboNumber(rs.getLong("BOARD_COMBO_NUMBER"));
					data.setBoardComboTitle(rs.getString("BOARD_COMBO_TITLE"));
					data.setBoardComboContent(rs.getString("BOARD_COMBO_CONTENT"));
					data.setBoardComboViewCount(rs.getLong("BOARD_COMBO_VIEW_COUNT"));
					data.setBoardComboRegisterDate(rs.getDate("BOARD_COMBO_REGISTER_DATE"));
					data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
					data.setMemberName(rs.getString("MEMBER_NAME"));
					data.setBoardComboLikedCount(rs.getLong("BOARD_COMBO_LIKED_COUNT"));
				}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			JDBCUtil.disconnect(conn, pstmt);
		}
		return data;
	}
	public boolean insert(BoardComboDTO boardComboDTO) { 
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = JDBCUtil.connect();
			pstmt = conn.prepareStatement(INSERTCOMBOBOARD);  
			pstmt.setString(1, boardComboDTO.getBoardComboTitle());  
			pstmt.setString(2, boardComboDTO.getBoardComboContent());    
			pstmt.setLong(3, boardComboDTO.getMemberNumber());  
			pstmt.setString(4, boardComboDTO.getMemberName());
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

	public boolean update(BoardComboDTO boardComboDTO) { 
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			conn=JDBCUtil.connect();
			pstmt=conn.prepareStatement(UPDATECOMBOBOARD);
			pstmt.setString(1, boardComboDTO.getBoardComboTitle());
			pstmt.setString(2, boardComboDTO.getBoardComboContent());
			pstmt.setLong(3, boardComboDTO.getBoardComboNumber());
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

	public boolean delete(BoardComboDTO boardComboDTO) { 
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			conn=JDBCUtil.connect();
			pstmt=conn.prepareStatement(DELETECOMBOBOARD);
			pstmt.setLong(1, boardComboDTO.getBoardComboNumber());
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