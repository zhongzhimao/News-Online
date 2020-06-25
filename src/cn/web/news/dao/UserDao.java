package cn.web.news.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.web.news.entity.User;

public class UserDao extends BaseDao {

	/**
	 * 登录功能
	 * @param username 帐号
	 * @param password 密码
	 * @return         当前登录的用户信息，根据用户的非空判断是否登录成功
	 */
	public User login(String username,String password) {
		//假设用户帐号有唯一约束
		String sql = "SELECT * FROM news_users WHERE uname=? AND upwd=?";
		
		Connection conn = this.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		User user = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			
			rs = pstmt.executeQuery();
			if(rs.next()) {
				user = new User();
				user.setUid(rs.getInt("usid"));
				user.setUname(rs.getString("uname"));
				user.setUpwd(rs.getString("upwd"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.closeDB(conn, pstmt, rs);
		}
		return user;
		
	}
}
