package cn.web.news.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import cn.web.news.entity.News;
import cn.web.news.entity.NewsDetail;

public class NewsDao extends BaseDao {

	/**
	 * 分页查询
	 * @param pageNo   页码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public List<News> findNewsByPage(int pageNo,int pageSize,Integer... tids){
		String sql = "select * from news ORDER BY ncreatedate DESC LIMIT ?,?";
		if(tids != null && tids.length>0){
			sql = "select * from news where ntid=? ORDER BY ncreatedate DESC LIMIT ?,?";
		}
		
		Connection con = super.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<News> list = new ArrayList<>();
		try {
			pstmt = con.prepareStatement(sql);
			if (tids != null && tids.length>0) {
				pstmt.setInt(1, tids[0]);
				pstmt.setInt(2, (pageNo-1)*pageSize);
				pstmt.setInt(3, pageSize);			
			} else {
				pstmt.setInt(1, (pageNo-1)*pageSize);
				pstmt.setInt(2, pageSize);
			}
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				News news = new News();
				news.setNid(rs.getInt("nid"));
				news.setNtid(rs.getInt("ntid"));
				news.setNtitle(rs.getString("ntitle"));
				news.setNauthor(rs.getString("nauthor"));
				news.setNcreatedate(rs.getString("ncreatedate"));
				news.setNcontent(rs.getString("ncontent"));
				news.setNsummary(rs.getString("nsummary"));
				news.setNpicpath(rs.getString("npicpath"));
				list.add(news);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.closeDB(con, pstmt, rs);
		}
		return list;
	}
	
	/**
	 * 查询新闻总记录数
	 * @return
	 */
	public int getAllCounts(Integer... tids){
		String sql = "SELECT COUNT(*) FROM news"; 
		if (tids != null && tids.length>0) {
			sql = "SELECT COUNT(*) FROM news where ntid=?";
		}
		
		Connection con = super.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int allCounts = 0;
		try {
			pstmt = con.prepareStatement(sql);
			if (tids != null && tids.length>0) {
				pstmt.setInt(1, tids[0]);
			}
			
			rs = pstmt.executeQuery(); // 游标是在第一行数据之前
			rs.next();
			allCounts = rs.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.closeDB(con, pstmt, rs);
		}
		return allCounts;
	}
	
	/**
	 * 根据新闻主键查询新闻详情
	 * @return
	 */
	public NewsDetail findNewsById(int nid){
		String sql = "SELECT news.*,topic.tname FROM news,topic WHERE news.ntid=topic.tid AND news.nid=?";
		
		Connection con = super.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		NewsDetail news = null;
		try {
			pstmt = con.prepareStatement(sql);
			//设置分页的参数
			pstmt.setInt(1, nid);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				news = new NewsDetail();
				news.setNid(rs.getInt("nid"));
				news.setNtid(rs.getInt("ntid"));
				news.setNtitle(rs.getString("ntitle"));
				news.setNauthor(rs.getString("nauthor"));
				news.setNcreatedate(rs.getString("ncreatedate"));
				news.setNcontent(rs.getString("ncontent"));
				news.setNsummary(rs.getString("nsummary"));
				news.setNpicpath(rs.getString("npicpath"));
				news.setTname(rs.getString("tname"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.closeDB(con, pstmt, rs);
		}
		return news;
	}
	
	public int saveNews(News news){
		String sql = "INSERT INTO news(ntid,ntitle,nauthor,ncreatedate,npicpath,ncontent,nsummary) " +
				"VALUES(?,?,?,NOW(),?,?,?)";
		
		Connection con = super.getConnection();
		PreparedStatement pstmt = null;
		int row = 0;
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, news.getNtid());
			pstmt.setString(2, news.getNtitle());
			pstmt.setString(3, news.getNauthor());
			pstmt.setString(4, news.getNpicpath());
			pstmt.setString(5, news.getNcontent());
			pstmt.setString(6, news.getNsummary());
			
			row = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.closeDB(con, pstmt, null);
		}
		return row;
	}
	
	public int updateNews(News news){
		String sql = "update news set ntid=?,ntitle=?,nauthor=?,npicpath=?," +
					 "ncontent=?,nmodifydate=NOW(),nsummary=? where nid=?";
		
		Connection con = super.getConnection();
		PreparedStatement pstmt = null;
		int row = 0;
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, news.getNtid());
			pstmt.setString(2, news.getNtitle());
			pstmt.setString(3, news.getNauthor());
			pstmt.setString(4, news.getNpicpath());
			pstmt.setString(5, news.getNcontent());
			pstmt.setString(6, news.getNsummary());
			pstmt.setInt(7, news.getNid());
			
			row = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.closeDB(con, pstmt, null);
		}
		return row;
	}
	
	/**
	 * 根据主键删除新闻
	 * @param newsId
	 * @return
	 */
	public int deleteNews(int newsId){
		String sql = "delete from news where nid=?";
		
		Connection con = super.getConnection();
		PreparedStatement pstmt = null;
		int row = 0;
		try {
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, newsId);
			
			row = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			super.closeDB(con, pstmt, null);
		}
		return row;
	}
	
	/**
	 * 根据主题查询新闻
	 * @param tid
	 * @return
	 */
	public List<News> findNewsByTopic(int tid){
		String sql = "select * from news where ntid=? order by ncreatedate desc limit 10";
		Connection con = super.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<News> list = new ArrayList<News>();
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, tid);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				News news = new News();
				news.setNid(rs.getInt("nid"));
				news.setNtid(rs.getInt("ntid"));
				news.setNtitle(rs.getString("ntitle"));
				news.setNauthor(rs.getString("nauthor"));
				news.setNcreatedate(rs.getString("ncreatedate"));
				news.setNcontent(rs.getString("ncontent"));
				news.setNsummary(rs.getString("nsummary"));
				list.add(news);
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			super.closeDB(con, pstmt, rs);
		}
		return list;
	}
}
