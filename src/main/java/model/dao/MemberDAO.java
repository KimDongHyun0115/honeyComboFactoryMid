package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;

import model.common.JDBCUtil;
import model.dto.MemberDTO;

public class MemberDAO {
	// 1. 전체 회원 목록 조회 
	final String SELECTALL = "SELECT MEMBER_NUMBER, MEMBER_ID, MEMBER_NAME, MEMBER_BIRTH, MEMBER_PHONE_NUMBER, "
			+ "MEMBER_ADDRESS_MAIN, MEMBER_ADDRESS_DETAIL, MEMBER_EMAIL_ID, MEMBER_EMAIL_DOMAIN, MEMBER_REGISTER_DATE, "
			+ "MEMBER_IS_ADMIN, MEMBER_IS_WITHDRAW, (SELECT COUNT(MEMBER_NUMBER) FROM MEMBER) AS TOTAL_COUNT_NUMBER FROM MEMBER "
			+ "ORDER BY MEMBER_NUMBER DESC LIMIT ?, ?";
	// 2. 로그인 
	final String SELECTONELOGIN = "SELECT MEMBER_NUMBER, MEMBER_ID, MEMBER_NAME, MEMBER_IS_ADMIN FROM MEMBER WHERE MEMBER_ID = ? AND MEMBER_PASSWORD = ?";

	// 3. ID로 회원조회 (관리자 기능)  
	final String SELECTONEMEMBER = "SELECT MEMBER_NUMBER, MEMBER_ID, MEMBER_NAME, MEMBER_BIRTH, MEMBER_PHONE_NUMBER, MEMBER_ADDRESS_MAIN, "
			+ "MEMBER_ADDRESS_DETAIL, MEMBER_EMAIL_ID, MEMBER_EMAIL_DOMAIN, MEMBER_REGISTER_DATE, MEMBER_IS_ADMIN, MEMBER_IS_WITHDRAW FROM MEMBER "
			+ "WHERE MEMBER_ID = ?";

	// 4. 내 정보 상세 보기 
	final String SELECTONEMYINFORMATION = "SELECT MEMBER_NUMBER, MEMBER_ID, MEMBER_NAME, MEMBER_BIRTH, MEMBER_PHONE_NUMBER, "
			+ "MEMBER_ADDRESS_MAIN, MEMBER_ADDRESS_DETAIL, MEMBER_EMAIL_ID, MEMBER_EMAIL_DOMAIN FROM MEMBER WHERE MEMBER_NUMBER = ?";

	// 5. 회원가입 
	final String INSERTJOIN= "INSERT INTO MEMBER (MEMBER_NUMBER, MEMBER_ID, MEMBER_PASSWORD, MEMBER_NAME, MEMBER_BIRTH, "
			+ "MEMBER_PHONE_NUMBER, MEMBER_ADDRESS_MAIN, MEMBER_ADDRESS_DETAIL, MEMBER_EMAIL_ID, MEMBER_EMAIL_DOMAIN, "
			+ "MEMBER_REGISTER_DATE, MEMBER_IS_ADMIN, MEMBER_IS_WITHDRAW) "
			+ "SELECT IFNULL(MAX(MEMBER_NUMBER), 0) + 1, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), 0, 0 FROM MEMBER";

	// 6. 비밀번호 변경 
	final String UPDATEPASSWORD = "UPDATE MEMBER SET MEMBER_PASSWORD=? WHERE MEMBER_NUMBER =?";

	// 7. 아이디 찾기  ONE 
	final String SELECTONEFINDID = "SELECT MEMBER_ID FROM MEMBER "
			+ "WHERE (MEMBER_BIRTH = ? AND MEMBER_PHONE_NUMBER = ?) "
			+ "OR (MEMBER_BIRTH = ? AND MEMBER_EMAIL_ID = ? AND MEMBER_EMAIL_DOMAIN = ?)";

	// 8. 비밀번호 찾기 ONE 
	final String SELECTONEFINDPASSWORD ="SELECT MEMBER_NUMBER FROM MEMBER "
			+ "WHERE (MEMBER_BIRTH = ? AND MEMBER_ID = ? AND MEMBER_PHONE_NUMBER = ?) "
			+ "OR (MEMBER_BIRTH = ? AND MEMBER_ID = ? AND MEMBER_EMAIL_ID = ? AND MEMBER_EMAIL_DOMAIN = ?)";

	// 9. 내 정보 수정 U
	final String UPDATEMYINFORMATION = "UPDATE MEMBER SET MEMBER_PHONE_NUMBER = ?, MEMBER_ADDRESS_MAIN = ?, "
			+ "MEMBER_ADDRESS_DETAIL = ?, MEMBER_EMAIL_ID = ?, MEMBER_EMAIL_DOMAIN = ? WHERE MEMBER_NUMBER = ?";

	// 10. 회원 탈퇴 U
	final String UPDATECANCEL ="UPDATE MEMBER SET MEMBER_IS_WITHDRAW = 1 WHERE MEMBER_NUMBER = ?";

	// 11. 이메일 중복검사 ONE
	final String SELECTONEEMAIL = "SELECT MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, MEMBER_ID FROM MEMBER "
			+ "WHERE MEMBER_EMAIL_ID = ? AND MEMBER_EMAIL_DOMAIN = ?";

	// 12. 핸드폰중복검사 ONE 
	final String SELECTONEPHONE ="SELECT MEMBER_NUMBER FROM MEMBER WHERE MEMBER_PHONE_NUMBER = ?";

	// 13. 내정보 본인확인 ONE
	final String SELECTONEMYPAGE = "SELECT MEMBER_NUMBER, MEMBER_NAME, MEMBER_IS_ADMIN, MEMBER_ID "
			+ "FROM MEMBER "
			+ "WHERE MEMBER_NUMBER = ? AND MEMBER_PASSWORD = ?";
	
	// 14. 비밀번호 찾기 - 변경
	final String UPDATEFINDPASSWORD = "UPDATE MEMBER SET MEMBER_PASSWORD = ? WHERE MEMBER_ID = ?";


	// 전체 회원 조회
	public ArrayList<MemberDTO> selectAll(MemberDTO memberDTO) {
		ArrayList<MemberDTO> datas = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		MemberDTO data=null;

		try {
			conn = JDBCUtil.connect();
			pstmt = conn.prepareStatement(SELECTALL);
			pstmt.setLong(1, memberDTO.getMemberIndex());
			pstmt.setLong(2, memberDTO.getMemberContentCount());
			rs = pstmt.executeQuery();

			while (rs.next()) {
				data = new MemberDTO();
				data.setMemberNumber(rs.getLong("MEMBER_NUMBER"));
				data.setMemberId(rs.getString("MEMBER_ID"));
				data.setMemberName(rs.getString("MEMBER_NAME"));
				data.setMemberBirth(rs.getDate("MEMBER_BIRTH"));
				data.setMemberPhoneNumber(rs.getString("MEMBER_PHONE_NUMBER"));
				data.setMemberAddressMain(rs.getString("MEMBER_ADDRESS_MAIN"));
				data.setMemberAddressDetail(rs.getString("MEMBER_ADDRESS_DETAIL"));
				data.setMemberEmailId(rs.getString("MEMBER_EMAIL_ID"));
				data.setMemberEmailDomain(rs.getString("MEMBER_EMAIL_DOMAIN"));
				data.setMemberRegisterDate(rs.getDate("MEMBER_REGISTER_DATE"));
				data.setMemberIsAdmin(rs.getBoolean("MEMBER_IS_ADMIN"));
				data.setMemberIsWithdraw(rs.getBoolean("MEMBER_IS_ADMIN"));
				data.setTotalConutNumber(rs.getLong("TOTAL_COUNT_NUMBER"));
				datas.add(data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCUtil.disconnect(conn, pstmt);
		}
		return datas;
	}

	public MemberDTO selectOne(MemberDTO memberDTO) {
		MemberDTO data = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = JDBCUtil.connect();
			String condition = memberDTO.getCondition();

			if (condition.equals("SELECTONELOGIN")) {
				pstmt = conn.prepareStatement(SELECTONELOGIN);
				pstmt.setString(1, memberDTO.getMemberId());
				pstmt.setString(2, memberDTO.getMemberPassword());
			}
			else if(condition.equals("SELECTONEMEMBER")) {
				pstmt = conn.prepareStatement(SELECTONEMEMBER);
				pstmt.setString(1, memberDTO.getMemberId());
			}
			else if (condition.equals("SELECTONEMYINFORMATION")) {
				pstmt = conn.prepareStatement(SELECTONEMYINFORMATION);
				pstmt.setLong(1, memberDTO.getMemberNumber());
			}
			else if (condition.equals("SELECTONEFINDID")) {
				pstmt = conn.prepareStatement(SELECTONEFINDID);
				pstmt.setDate(1, memberDTO.getMemberBirth());
				pstmt.setString(2, memberDTO.getMemberPhoneNumber());
				pstmt.setDate(3, memberDTO.getMemberBirth());
				pstmt.setString(4, memberDTO.getMemberEmailId());
				pstmt.setString(5, memberDTO.getMemberEmailDomain());
			}
			else if (condition.equals("SELECTONEFINDPASSWORD")) {
				pstmt = conn.prepareStatement(SELECTONEFINDPASSWORD); 
				pstmt.setDate(1, memberDTO.getMemberBirth());
				pstmt.setString(2, memberDTO.getMemberId());
				pstmt.setString(3, memberDTO.getMemberPhoneNumber());
				pstmt.setDate(4, memberDTO.getMemberBirth());
				pstmt.setString(5, memberDTO.getMemberId());
				pstmt.setString(6, memberDTO.getMemberEmailId());
				pstmt.setString(7, memberDTO.getMemberEmailDomain());
			}
			else if (condition.equals("SELECTONEEMAIL")) {
				pstmt = conn.prepareStatement(SELECTONEEMAIL);
				pstmt.setString(1, memberDTO.getMemberEmailId());
				pstmt.setString(2, memberDTO.getMemberEmailDomain());
			}
			else if (condition.equals("SELECTONEPHONE")) {
				pstmt = conn.prepareStatement(SELECTONEPHONE);
				pstmt.setString(1, memberDTO.getMemberPhoneNumber());
			}
			else if(condition.equals("SELECTONEMYPAGE")) {
				pstmt = conn.prepareStatement(SELECTONEMYPAGE);
				pstmt.setLong(1, memberDTO.getMemberNumber());
				pstmt.setString(2, memberDTO.getMemberPassword());
			}
			else {
				return null;
			}
			rs = pstmt.executeQuery();

			if (rs.next()) {

				System.out.println("쿼리 실행 조건: " + condition);
//				System.out.println("결과: MEMBER_NUMBER = " + rs.getLong("MEMBER_NUMBER"));
			//	System.out.println("결과: MEMBER_ID = " + rs.getString("MEMBER_ID"));

				data = new MemberDTO();
				//data.setMemberNumber(rs.getLong("MEMBER_NUMBER")); 

				condition = memberDTO.getCondition();
				
				if(condition.equals("SELECTONELOGIN")) {
					data.setMemberNumber(rs.getLong("MEMBER_NUMBER")); 
				    data.setMemberName(rs.getString("MEMBER_NAME"));
				    data.setMemberIsAdmin(rs.getBoolean("MEMBER_IS_ADMIN"));
				    data.setMemberId(rs.getString("MEMBER_ID"));
				}
				else if (condition.equals("SELECTONEMYPAGE")) {
					data.setMemberNumber(rs.getLong("MEMBER_NUMBER")); 
				    data.setMemberName(rs.getString("MEMBER_NAME"));
				    data.setMemberIsAdmin(rs.getBoolean("MEMBER_IS_ADMIN"));
				    data.setMemberId(rs.getString("MEMBER_ID"));
				}
				else if (condition.equals("SELECTONE")) {
					  data.setMemberName(rs.getString("MEMBER_NAME"));
					   data.setMemberIsAdmin(rs.getBoolean("MEMBER_IS_ADMIN")); 
				    }
				else if(condition.equals("SELECTONEMEMBER")) {
					 data.setMemberNumber(rs.getLong("MEMBER_NUMBER")); 
					 data.setMemberId(rs.getString("MEMBER_ID"));
					 data.setMemberBirth(rs.getDate("MEMBER_BIRTH"));
					 data.setMemberName(rs.getString("MEMBER_NAME"));
					 data.setMemberPhoneNumber(rs.getString("MEMBER_PHONE_NUMBER"));
					 data.setMemberAddressMain(rs.getString("MEMBER_ADDRESS_MAIN"));
					 data.setMemberAddressDetail(rs.getString("MEMBER_ADDRESS_DETAIL"));
					 data.setMemberEmailId(rs.getString("MEMBER_EMAIL_ID"));
					 data.setMemberEmailDomain(rs.getString("MEMBER_EMAIL_DOMAIN"));
					 data.setMemberRegisterDate(rs.getDate("MEMBER_REGISTER_DATE"));
					 data.setMemberIsAdmin(rs.getBoolean("MEMBER_IS_ADMIN"));
					 data.setMemberIsWithdraw(rs.getBoolean("MEMBER_IS_WITHDRAW"));
				}
				else if(condition.equals("SELECTONEMYINFORMATION")) {
					data.setMemberNumber(rs.getLong("MEMBER_NUMBER")); 
					data.setMemberId(rs.getString("MEMBER_ID"));
					data.setMemberName(rs.getString("MEMBER_NAME"));
					data.setMemberBirth(rs.getDate("MEMBER_BIRTH"));
					data.setMemberPhoneNumber(rs.getString("MEMBER_PHONE_NUMBER"));
					data.setMemberAddressMain(rs.getString("MEMBER_ADDRESS_MAIN"));
					data.setMemberAddressDetail(rs.getString("MEMBER_ADDRESS_DETAIL"));
					data.setMemberEmailId(rs.getString("MEMBER_EMAIL_ID"));
					data.setMemberEmailDomain(rs.getString("MEMBER_EMAIL_DOMAIN"));
				}
				else if (condition.equals("SELECTONEFINDID")) {
					data.setMemberId(rs.getString("MEMBER_ID"));
				}
				else if (condition.equals("SELECTONEFINDPASSWORD")) {
					data.setMemberNumber(rs.getLong("MEMBER_NUMBER")); 
				}
				else if (condition.equals("SELECTONEEMAIL")) {
					data.setMemberNumber(rs.getLong("MEMBER_NUMBER")); 
					data.setMemberName(rs.getString("MEMBER_NAME"));
					data.setMemberIsAdmin(rs.getBoolean("MEMBER_IS_ADMIN"));
					data.setMemberId(rs.getString("MEMBER_ID"));
					
				}
				else if (condition.equals(" SELECTONEPHONE")) {
					data.setMemberNumber(rs.getLong("MEMBER_NUMBER")); 
				}
			}
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		JDBCUtil.disconnect(conn, pstmt);
	}

	return data;
}

// 회원 가입
public boolean insert(MemberDTO memberDTO) {
	Connection conn = null;
	PreparedStatement pstmt = null;

	try {
		conn = JDBCUtil.connect();
		pstmt = conn.prepareStatement(INSERTJOIN);

		pstmt.setString(1, memberDTO.getMemberId());
		pstmt.setString(2, memberDTO.getMemberPassword());
		pstmt.setString(3, memberDTO.getMemberName());
		pstmt.setDate(4, memberDTO.getMemberBirth());
		pstmt.setString(5, memberDTO.getMemberPhoneNumber());
		//   pstmt.setString(6, memberDTO.getMemberAddressMain());
		//   pstmt.setString(7, memberDTO.getMemberAddressDetail());
		//   pstmt.setString(8, memberDTO.getMemberEmailId());
		//   pstmt.setString(9, memberDTO.getMemberEmailDomain());

		if (memberDTO.getMemberAddressMain() != null && !memberDTO.getMemberAddressMain().isEmpty()) {
			pstmt.setString(6, memberDTO.getMemberAddressMain());
		} else {
			pstmt.setNull(6, Types.VARCHAR);
		}

		if (memberDTO.getMemberAddressDetail() != null && !memberDTO.getMemberAddressDetail().isEmpty()) {
			pstmt.setString(7, memberDTO.getMemberAddressDetail());
		} else {
			pstmt.setNull(7, Types.VARCHAR);
		}

		pstmt.setString(8, memberDTO.getMemberEmailId());
		pstmt.setString(9, memberDTO.getMemberEmailDomain());

		int result = pstmt.executeUpdate();
		return result > 0;

	} catch (Exception e) {
		e.printStackTrace();
		return false;
	} finally {
		JDBCUtil.disconnect(conn, pstmt);
	}
}

// 회원 정보 수정, 비밀번호 변경, 회원 탈퇴
public boolean update(MemberDTO memberDTO) {
	Connection conn = null;
	PreparedStatement pstmt = null;

	try {
		conn = JDBCUtil.connect();
		String condition = memberDTO.getCondition();

		if (condition.equals("UPDATEPASSWORD")) { // 비밀번호 변경
			pstmt = conn.prepareStatement(UPDATEPASSWORD);
			pstmt.setString(1, memberDTO.getMemberPassword());
			pstmt.setLong(2, memberDTO.getMemberNumber());
		} else if (condition.equals("UPDATEMYINFORMATION")) { // 회원 정보 수정
			pstmt = conn.prepareStatement(UPDATEMYINFORMATION);
			pstmt.setString(1, memberDTO.getMemberPhoneNumber());
			pstmt.setString(2, memberDTO.getMemberAddressMain());
			pstmt.setString(3, memberDTO.getMemberAddressDetail());
			pstmt.setString(4, memberDTO.getMemberEmailId());
			pstmt.setString(5, memberDTO.getMemberEmailDomain());    
			pstmt.setLong(6, memberDTO.getMemberNumber());
		} else if(condition.equals("UPDATECANCEL")) { // 회원 탈퇴
			pstmt = conn.prepareStatement(UPDATECANCEL);
			pstmt.setLong(1, memberDTO.getMemberNumber());
		} else if(condition.equals("UPDATEFINDPASSWORD")) { // 비밀번호 찾기로 비밀번호 변경
			pstmt = conn.prepareStatement(UPDATEFINDPASSWORD);
			pstmt.setString(1, memberDTO.getMemberPassword());
			pstmt.setString(2, memberDTO.getMemberId());
		}
		else {
			return false;
		}

		int result = pstmt.executeUpdate();
		return result > 0;

	} catch (Exception e) {
		System.out.println("쿼리 실행 중 오류: " + e.getMessage());
		return false;
	} finally {
		JDBCUtil.disconnect(conn, pstmt);
	}
}


private boolean delete(MemberDTO memberDTO) {
	return false;
}
}
