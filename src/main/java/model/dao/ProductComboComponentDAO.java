package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import model.common.JDBCUtil;
import model.dto.MemberDTO;
import model.dto.ProductComboComponentDTO;
import model.dto.PurchaseDTO;

public class ProductComboComponentDAO {
	// 1. 꿀조합 구성품 전체 조회
	final String SELECTALL = "SELECT PRODUCT_COMBO_COMPONENT_NUMBER, PRODUCT_COMBO_COMPONENT_ONE, PRODUCT_COMBO_COMPONENT_TWO, PRODUCT_COMBO_COMPONENT_THREE, PRODUCT_COMBO_NUMBER, PRODUCT_SINGLE_NUMBER, "
			+ "(SELECT COUNT(*) FROM PRODUCT_COMBO_COMPONENT) AS TOTAL_COUNT_NUMBER FROM  PRODUCT_COMBO_COMPONENT LIMIT ?, ?";
	// 2. 꿀조합 구성품 추가
	final String INSERT = "INSERT INTO PRODUCT_COMBO_COMPONENT (PRODUCT_COMBO_COMPONENT_NUMBER, PRODUCT_COMBO_COMPONENT_ONE, PRODUCT_COMBO_COMPONENT_TWO, PRODUCT_COMBO_COMPONENT_THREE, PRODUCT_COMBO_NUMBER, PRODUCT_SINGLE_NUMBER) VALUES (?, ?, ?, ?, ?, ?)"; 
	// 3. 꿀조합 구성품 삭제
	final String DELETE = "DELETE FROM PRODUCT_COMBO_COMPONENT WHERE PRODUCT_COMBO_COMPONENT_NUMBER = ?";

	// 4. 조합 구성품 출력
	final String SELECTALLCOMPONENT = "SELECT PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER, PRODUCT_SINGLE.PRODUCT_SINGLE_NAME, PRODUCT_SINGLE.PRODUCT_SINGLE_IMAGE, "
			+ "PRODUCT_SINGLE.PRODUCT_SINGLE_PRICE FROM PRODUCT_COMBO_COMPONENT LEFT JOIN PRODUCT_SINGLE ON "
			+ "PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER = PRODUCT_COMBO_COMPONENT.PRODUCT_COMBO_COMPONENT_ONE OR "
			+ "PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER = PRODUCT_COMBO_COMPONENT.PRODUCT_COMBO_COMPONENT_TWO OR "
			+ "PRODUCT_SINGLE.PRODUCT_SINGLE_NUMBER = PRODUCT_COMBO_COMPONENT.PRODUCT_COMBO_COMPONENT_THREE "
			+ "WHERE PRODUCT_COMBO_COMPONENT.PRODUCT_COMBO_NUMBER = ?";

	public ArrayList<ProductComboComponentDTO> selectAll(ProductComboComponentDTO productComboComponentDTO){
		ArrayList<ProductComboComponentDTO> datas=new ArrayList<ProductComboComponentDTO>();
		Connection conn=null;
		PreparedStatement pstmt=null;

		try {
			conn=JDBCUtil.connect();
			if(productComboComponentDTO.getCondition().equals("SELECTALL")) {
				productComboComponentDTO.setCondition("SELECTALL");
				System.out.println("[selectAll] 조건: " +SELECTALL);
				pstmt=conn.prepareStatement(SELECTALL);
				System.out.println(">> 쿼리 준비 완료: " + pstmt);
				pstmt.setInt(1, productComboComponentDTO.getProductComboComponentIndex());
				pstmt.setInt(2, productComboComponentDTO.getProductComboComponentContentCount());
			}
			else if(productComboComponentDTO.getCondition().equals("SELECTALLCOMPONENT")) {
				productComboComponentDTO.setCondition("SELECTALLCOMPONENT");
				System.out.println("[selectAll] 조건: " +SELECTALLCOMPONENT);
				pstmt=conn.prepareStatement(SELECTALLCOMPONENT);
				System.out.println(">> 쿼리 준비 완료: " + pstmt);
				pstmt.setLong(1, productComboComponentDTO.getProductComboNumber());
			}
			else {
				System.out.println("잘못된 condition: " + productComboComponentDTO.getCondition());
			}
			ResultSet rs=pstmt.executeQuery();
			System.out.println("[selectAll] 쿼리 실행 완료");
			while(rs.next()) {
				ProductComboComponentDTO data = new ProductComboComponentDTO();
				if(productComboComponentDTO.getCondition().equals("SELECTALL")) {
					data.setProductComboComponentNumber(rs.getLong("PRODUCT_COMBO_COMPONENT_NUMBER"));
					data.setProductComboComponentOne(rs.getLong("PRODUCT_COMBO_COMPONENT_ONE"));
					data.setProductComboComponentTwo(rs.getLong("PRODUCT_COMBO_COMPONENT_TWO"));
					data.setProductComboComponentThree(rs.getLong("PRODUCT_COMBO_COMPONENT_THREE"));
					data.setProductComboNumber(rs.getLong("PRODUCT_COMBO_NUMBER"));
					data.setProductSingleNumber(rs.getLong("PRODUCT_SINGLE_NUMBER"));
					data.setTotalCountNumber(rs.getLong("TOTAL_COUNT_NUMBER"));
				} else if(productComboComponentDTO.getCondition().equals("SELECTALLCOMPONENT")) {
					data.setProductSingleNumber(rs.getLong("PRODUCT_SINGLE_NUMBER"));
					data.setProductSingleName(rs.getString("PRODUCT_SINGLE_NAME"));
					data.setProductSingleImage(rs.getString("PRODUCT_SINGLE_IMAGE"));
					data.setProductSinglePrice(rs.getLong("PRODUCT_SINGLE_PRICE"));

					datas.add(data);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			JDBCUtil.disconnect(conn, pstmt);
		}
		return datas;
	}

	private ProductComboComponentDTO selectOne(ProductComboComponentDTO productComboComponentDTO) {
		return null;
	}


	public boolean insert(ProductComboComponentDTO productComboComponentDTO){
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			conn=JDBCUtil.connect();
			pstmt=conn.prepareStatement(INSERT);
			pstmt.setLong(1, productComboComponentDTO.getProductComboComponentNumber());
			pstmt.setLong(2, productComboComponentDTO.getProductComboComponentOne());
			pstmt.setLong(3, productComboComponentDTO.getProductComboComponentTwo());
			pstmt.setLong(4, productComboComponentDTO.getProductComboComponentThree());
			pstmt.setLong(5, productComboComponentDTO.getProductComboNumber());
			pstmt.setLong(6, productComboComponentDTO.getProductSingleNumber());
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
	private boolean update(ProductComboComponentDTO productComboComponentDTO){
		return false;
	}

	public boolean delete(ProductComboComponentDTO productComboComponentDTO){
		Connection conn=null;
		PreparedStatement pstmt=null;
		try {
			conn=JDBCUtil.connect();
			pstmt=conn.prepareStatement(DELETE);
			pstmt.setLong(1, productComboComponentDTO.getProductComboComponentNumber());
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
