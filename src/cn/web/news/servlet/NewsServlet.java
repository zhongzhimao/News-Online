package cn.web.news.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;

import cn.web.news.dao.CommentDao;
import cn.web.news.dao.NewsDao;
import cn.web.news.dao.TopicDao;
import cn.web.news.entity.Comment;
import cn.web.news.entity.News;
import cn.web.news.entity.NewsDetail;
import cn.web.news.entity.Result;
import cn.web.news.entity.Topic;

/**
 * 新闻内容的控制器
 */
public class NewsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final int PAGE_SIZE = 15;
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");// post请求
		//请求的入口参数
		String action = request.getParameter("action");
		
		if("listNews".equals(action)) {
			doQueryNews(request,response);
		} else if("detail".equals(action)) {
			doNewsDetail(request,response);
		} else if("doComment".equals(action)) {
			doComment(request,response);
		} else if("doAjaxComment".equals(action)){
			doAjaxComment(request,response);
		}

	}

	private void doAjaxComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		int cnid = Integer.parseInt(request.getParameter("cnid"));
		String cauthor = request.getParameter("cauthor");
		String cip = request.getParameter("cip");
		String ccontent = request.getParameter("ccontent");
		
		Result result = new Result();
		
		CommentDao commentDao = new CommentDao();
		Comment comment = new Comment(cnid,cauthor,cip,ccontent);
		int row = commentDao.addComment(comment);
		result.setMsg(row+"");
		
		//查询最新的评论列表
		List<Comment> commentList = commentDao.findCommentsByNews(cnid);
		result.setDatas(commentList);
		
		//String json = JSON.toJSONString(commentList);
		//System.out.println(json);
		String jsonObject = JSON.toJSONString(result);
		out.print(jsonObject);
		System.out.println(jsonObject);
		out.flush();
		out.close();
	}

	private void doComment(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int cnid = Integer.parseInt(request.getParameter("cnid"));
		String cauthor = request.getParameter("cauthor");
		String cip = request.getParameter("cip");
		String ccontent = request.getParameter("ccontent");
		
		CommentDao commentDao = new CommentDao();
		Comment comment = new Comment(cnid,cauthor,cip,ccontent);
		commentDao.addComment(comment);
		
		//NewsServlet?action=detail&nid=48
		request.getRequestDispatcher("NewsServlet?action=detail&nid="+cnid).forward(request, response);
	}

	private void doNewsDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int nid = Integer.parseInt(request.getParameter("nid"));
		
		NewsDao newsDao = new NewsDao();
		CommentDao commentDao = new CommentDao();
		
		//左侧显示主要新闻类别：国内=1，国际=2，娱乐=5
		List<News> list1 = newsDao.findNewsByTopic(1);
		List<News> list2 = newsDao.findNewsByTopic(2);
		List<News> list5 = newsDao.findNewsByTopic(5);
		
		request.setAttribute("list1", list1);
		request.setAttribute("list2", list2);
		request.setAttribute("list5", list5);
		
		NewsDetail newsDetail = newsDao.findNewsById(nid);
		List<Comment> commentList = commentDao.findCommentsByNews(nid);
		String ip = request.getRemoteAddr(); 
		
		request.setAttribute("news", newsDetail);
		request.setAttribute("commentList", commentList);
		request.setAttribute("cip", ip);
		
		request.getRequestDispatcher("/news_ajax_read.jsp").forward(request, response);
	}

	private void doQueryNews(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		TopicDao topicDao = new TopicDao();
		NewsDao newsDao = new NewsDao();
		
		//顶部显示新闻主题
		List<Topic> topicList = topicDao.getAllTopics();
		
		//左侧显示主要新闻类别：国内=1，国际=2，娱乐=5
		List<News> list1 = newsDao.findNewsByTopic(1);
		List<News> list2 = newsDao.findNewsByTopic(2);
		List<News> list5 = newsDao.findNewsByTopic(5);
		
		request.setAttribute("list1", list1);
		request.setAttribute("list2", list2);
		request.setAttribute("list5", list5);
		request.setAttribute("topicList", topicList);
		
		//获取页码 , 默认是第1页
		int pageNo = 1;
		if (request.getParameter("pageNo") != null) {
			pageNo = Integer.parseInt(request.getParameter("pageNo"));
		}
		
		List<News> list = null;
		//计算总页数  , 先查询总记录数
		int allCounts = 0;
		//新闻主题的ID
		if (request.getParameter("tid") != null) {
			int tid = Integer.parseInt(request.getParameter("tid"));
			list = newsDao.findNewsByPage(pageNo, PAGE_SIZE,tid);
			allCounts = newsDao.getAllCounts(tid);
		} else {
			list = newsDao.findNewsByPage(pageNo, PAGE_SIZE);
			allCounts = newsDao.getAllCounts();
		}

		int allPages = (allCounts%PAGE_SIZE == 0)?(allCounts/PAGE_SIZE):(allCounts/PAGE_SIZE+1);
		
		//计算上一页和下一页  prev next
		int prev = pageNo;
		int next = pageNo;
		
		//判断页数边界
		if(pageNo<=1) { //首页
			prev = 1;
			next++;
		} else if(pageNo>=allPages) {  // 尾页
			next = allPages;
			prev--;
		} else {
			prev--;
			next++;
		}
		
		request.setAttribute("newsList", list);
		request.setAttribute("allPages", allPages);
		request.setAttribute("allCounts", allCounts);
		request.setAttribute("prev", prev);
		request.setAttribute("next", next);
		request.setAttribute("pageNo", pageNo);
		//  http://localhost:8080/news/index.jsp
		// 在转发的路径 中 ，建议加上 '/' , 表示WebRoot根目录 
		request.getRequestDispatcher("/index.jsp").forward(request, response);
		// 重定向路径中'/', 表示整个工程根目录, http://localhost:8080/index.jsp
		// request.getContextPath()相当于${pageContext.request.contextPath}
		//response.sendRedirect(request.getContextPath() +"/index.jsp"); 
	}

}
