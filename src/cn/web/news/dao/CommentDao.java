package cn.web.news.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import cn.web.news.entity.Comment;

public class CommentDao extends BaseDao {

	
	/**
	 * 根据新闻查询评论列表
	 * @param newsId
	 * @return
	 */
	public List<Comment> findCommentsByNews(int newsId) {
		String sql = "SELECT * FROM comments WHERE cnid=?";
		
		Connection con = super.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		List<Comment> list = new ArrayList<>();
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, newsId);
			
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Comment comment = new Comment();
				comment.setCid(rs.getInt("cid"));
				comment.setCnid(rs.getInt("cnid"));  //新闻的外键
				comment.setCcontent(rs.getString("ccontent"));
				comment.setCdate(rs.getString("cdate"));
				comment.setCip(rs.getString("cip"));
				comment.setCauthor(rs.getString("cauthor"));
				
				list.add(comment);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.closeDB(con,pstmt, rs);
		}
		return list;
	}
	
	/**
	 * 发表评论
	 * @param comment
	 */
	public int addComment(Comment comment) {
		String sql = "INSERT INTO comments(cnid,ccontent,cdate,cip,cauthor) VALUES(?,?,NOW(),?,?)";
		
		Connection con = super.getConnection();
		PreparedStatement pstmt = null;
		
		int row = 0;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, comment.getCnid());
			pstmt.setString(2, comment.getCcontent());
			pstmt.setString(3, comment.getCip());
			pstmt.setString(4, comment.getCauthor());
			
			row = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.closeDB(con, pstmt, null);
		}
		return row;
	}
	/**
	 * 删除评论
	 * @param cid
	 * @return
	 */
	public int deleteComment(int cid) {
		String sql = "delete from comments where cid=?";
		Connection con = super.getConnection();
		PreparedStatement pstmt = null;
		int row = 0;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, cid);
			
			row = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.closeDB(con, pstmt, null);
		}
		return row;
	}
	
	/**
	 *  删除某条新闻所有的评论
	 * @param cid
	 * @return
	 */
	public int deleteCommentByNews(int nid) {
		String sql = "delete from comments where cnid=?";
		Connection con = super.getConnection();
		PreparedStatement pstmt = null;
		int row = 0;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, nid);
			
			row = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.closeDB(con, pstmt, null);
		}
		return row;
	}
	
}
