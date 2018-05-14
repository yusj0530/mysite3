package com.ssub.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.ssub.mysite.vo.UserVo;

public class UserDao {
	
	public int update( UserVo userVo ) {
		
		int count = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = getConnection();
			
			if( "".equals( userVo.getPassword() ) ) {
				String sql = 
					" update member" + 
					"    set name=?," + 
					"        gender=?" + 
					"  where no=?";
				pstmt = conn.prepareStatement(sql);
	
				pstmt.setString(1, userVo.getName() );
				pstmt.setString(2, userVo.getGender() );
				pstmt.setLong(3, userVo.getNo() );
			} else {
				String sql = 
						" update member" + 
						"    set name=?," + 
						"        gender=?," + 
						"        password=?" + 
						"  where no=?";
				pstmt = conn.prepareStatement(sql);
		
				pstmt.setString(1, userVo.getName() );
				pstmt.setString(2, userVo.getGender() );
				pstmt.setString(3, userVo.getPassword() );
				pstmt.setLong(4, userVo.getNo() );				
			}

			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error :" + e);
		} finally {
			// 자원 정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return count;	
	}
	
	public UserVo get( String email ) {
		UserVo vo = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();

			String sql = 
				" select no, name" + 
				"   from member" + 
				"  where email=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString( 1, email );
			
			rs = pstmt.executeQuery();
			if( rs.next() ) {
				Long no = rs.getLong( 1 );
				String name = rs.getString( 2 );
				
				vo = new UserVo();
				vo.setNo(no);
				vo.setName(name);
			}

		} catch (SQLException e) {
			System.out.println("error :" + e);
		} finally {
			// 자원 정리
			try {
				if(rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		
		return vo;
	}
	
	public UserVo get( Long userNo ) {
		UserVo vo = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();

			String sql = 
				" select no, name, email, gender" + 
				"   from member" + 
				"  where no=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setLong( 1, userNo );
			
			rs = pstmt.executeQuery();
			if( rs.next() ) {
				Long no = rs.getLong( 1 );
				String name = rs.getString( 2 );
				String email = rs.getString( 3 );
				String gender = rs.getString( 4 );
				
				vo = new UserVo();
				vo.setNo(no);
				vo.setName(name);
				vo.setEmail(email);
				vo.setGender(gender);
			}

		} catch (SQLException e) {
			System.out.println("error :" + e);
		} finally {
			// 자원 정리
			try {
				if(rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}		
		
		return vo;
	}
	
	public UserVo get( String email, String password ) {
		UserVo vo = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();

			String sql = 
				" select no, name" + 
				"   from member" + 
				"  where email=?" + 
				"    and password=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, email );
			pstmt.setString(2, password );

			rs = pstmt.executeQuery();
			if( rs.next() ) {
				Long no = rs.getLong( 1 );
				String name = rs.getString( 2 );
				
				vo = new UserVo();
				vo.setNo(no);
				vo.setName(name);
			}

		} catch (SQLException e) {
			System.out.println("error :" + e);
		} finally {
			// 자원 정리
			try {
				if(rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return vo;
	}
	
	public int insert( UserVo vo ) {
		int count = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = getConnection();

			String sql = 
				" insert" + 
				"   into member" + 
				" values ( seq_member.nextval, ?, ?, ?, ?, sysdate)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, vo.getName() );
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getEmail());
			pstmt.setString(4, vo.getGender());

			count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error :" + e);
		} finally {
			// 자원 정리
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return count;
	}	
	
	
	private Connection getConnection() throws SQLException {

		Connection conn = null;

		try {
			// JDBC 드라이버 로딩(JDBC 클래스 로딩)
			Class.forName("oracle.jdbc.driver.OracleDriver");

			// Connection 가져오기
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			conn = DriverManager.getConnection(url, "webdb", "webdb");

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패:" + e);
		}

		return conn;
	}		
}
