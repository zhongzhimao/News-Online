package cn.web.news.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.alibaba.fastjson.JSON;

import cn.web.news.dao.CommentDao;
import cn.web.news.dao.NewsDao;
import cn.web.news.dao.TopicDao;
import cn.web.news.entity.Comment;
import cn.web.news.entity.News;
import cn.web.news.entity.NewsDetail;
import cn.web.news.entity.Result;
import cn.web.news.entity.Topic;
import cn.web.news.utils.PageBean;

/**
 * Servlet implementation class AdminServlet
 */
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		
		String action = request.getParameter("action");
		if("adminIndex".equals(action)) {
			doAdminQuery(request,response);
		} else if("toNewsAdd".equals(action)) {
			toNewsAdd(request,response);
		} else if("doNewsAdd".equals(action)) {
			doNewsAdd(request,response);
		} else if("toNewsUpdate".equals(action)) {
			toNewsUpdate(request,response);
		} else if("doUpdateSubmit".equals(action)) {
			doUpdateSubmit(request,response);
		} else if("doDelComment".equals(action)) {
			doDelComment(request,response);
		}
		
	}

	private void doDelComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
		int cid = Integer.parseInt(request.getParameter("cid"));
		int nid = Integer.parseInt(request.getParameter("nid"));
		
		CommentDao commentDao = new CommentDao();
		int row = commentDao.deleteComment(cid);
		
		List<Comment> commentList = commentDao.findCommentsByNews(nid);
		
		Result result = new Result();
		result.setMsg(row+"");
		result.setDatas(commentList);
		
		String json = JSON.toJSONString(result);
		
		System.out.println(json);
		
		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();
		out.close();
	}

	private void doUpdateSubmit(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			//设置文件上传的参数
			upload.setHeaderEncoding("UTF-8");
			//设置上传文件的最大容量
			upload.setFileSizeMax(1024*1024*3);
			
			List list = upload.parseRequest(request);
			//创建保存数据库的News对象
			News news = new News();
			Iterator it = list.iterator();
			while(it.hasNext()) {
				FileItem item = (FileItem) it.next();//表示表单中的每一个元素的对象 （文本或文件）
				
				if (item.isFormField()) { //判断当前FileItem的类别 ： 1. form的普通输入框   2. file文件域 
					// request.getParameter("nid");这种 已经失效
					String value = item.getString("UTF-8");
					
					if (item.getFieldName().equals("ntid")) {
						news.setNtid(Integer.parseInt(value));
					} else if(item.getFieldName().equals("nid")){
						news.setNid(Integer.parseInt(value));
					} else if(item.getFieldName().equals("ntitle")) {
						news.setNtitle(value);
					} else if(item.getFieldName().equals("nauthor")) {
						news.setNauthor(value);
					} else if(item.getFieldName().equals("nsummary")) {
						news.setNsummary(value);
					} else if(item.getFieldName().equals("ncontent")) {
						news.setNcontent(value);
					} else if(item.getFieldName().equals("npicpath")) {
						news.setNpicpath(value);
					}
				} else {
					//判断是否有文件重新上传
					if (!"".equals(item.getName())) {
						String path = request.getServletContext().getRealPath("upload");
						System.out.println(path);
						
						//删除原来的图片
						String oldFileName = news.getNpicpath();
						if (oldFileName != null) {
							File oldFile = new File(path, news.getNpicpath());
							if (oldFile.exists()) {
								oldFile.delete();
							}
						}
						
						File targetFile = new File(path, item.getName());
						item.write(targetFile );  // 把item的二进制的文件数据另存到一个目标文件中。
						news.setNpicpath(item.getName());
						
						System.out.println("上传完成");
					}

				}
			}
			
			//保存数据库
			NewsDao newDao = new NewsDao();
			newDao.updateNews(news);
			
		}  catch (Exception e) {
			e.printStackTrace();
		}
		
		response.sendRedirect(request.getContextPath() + "/AdminServlet?action=adminIndex");
		
	}

	private void toNewsUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		int nid= Integer.parseInt(request.getParameter("nid"));
		
		TopicDao topicDao = new TopicDao();
		NewsDao newsDao = new NewsDao();
		CommentDao commentDao = new CommentDao();
		
		List<Topic> topicList = topicDao.getAllTopics();
		NewsDetail newsDetail = newsDao.findNewsById(nid);
		List<Comment> commentList = commentDao.findCommentsByNews(nid);
		
		request.setAttribute("topicList", topicList);
		request.setAttribute("news", newsDetail);
		request.setAttribute("commentList", commentList);
		
		request.getRequestDispatcher("/newspages/news_modify.jsp").forward(request, response);
	}

	private void doNewsAdd(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			//设置文件上传的参数
			upload.setHeaderEncoding("UTF-8");
			//设置上传文件的最大容量
			upload.setFileSizeMax(1024*1024*3);
			
			List list = upload.parseRequest(request);
			//创建保存数据库的News对象
			News news = new News();
			Iterator it = list.iterator();
			while(it.hasNext()) {
				FileItem item = (FileItem) it.next();//表示表单中的每一个元素的对象 （文本或文件）
				
				if (item.isFormField()) { //判断当前FileItem的类别 ： 1. form的普通输入框   2. file文件域 
					// request.getParameter("nid");这种 已经失效
					String value = item.getString("UTF-8");
					
					if (item.getFieldName().equals("ntid")) {
						news.setNtid(Integer.parseInt(value));
					} else if(item.getFieldName().equals("ntitle")) {
						news.setNtitle(value);
					} else if(item.getFieldName().equals("nauthor")) {
						news.setNauthor(value);
					} else if(item.getFieldName().equals("nsummary")) {
						news.setNsummary(value);
					} else if(item.getFieldName().equals("ncontent")) {
						news.setNcontent(value);
					}
				} else {
					//D:\\Apache\\apache-tomcat-8.0.50\\webapps\\emp-web\\upload
					//获取upload目录在tomcat的绝对路径     application=ServletContext
					String path = request.getServletContext().getRealPath("upload");
					System.out.println(path);
					
					File targetFile = new File(path, item.getName());
					item.write(targetFile );  // 把item的二进制的文件数据另存到一个目标文件中。
					news.setNpicpath(item.getName());
					
					System.out.println("上传完成");
				}
			}
			
			//保存数据库
			NewsDao newDao = new NewsDao();
			newDao.saveNews(news);
			
		}  catch (Exception e) {
			e.printStackTrace();
		}
		
		response.sendRedirect(request.getContextPath() + "/AdminServlet?action=adminIndex");
	}

	private void toNewsAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<Topic> topicList = new TopicDao().getAllTopics();
		request.setAttribute("topicList", topicList);
		request.getRequestDispatcher("/newspages/news_add.jsp").forward(request, response);
	}

	private void doAdminQuery(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		PageBean pageBean = new PageBean((request.getParameter("pageNo")==null)?1:Integer.parseInt(request.getParameter("pageNo")));
		//pageBean.setPageSize(15);
		
		TopicDao topicDao = new TopicDao();
		List<Topic> list = topicDao.findTopicAndNewsList(pageBean.getPageNo(), pageBean.getPageSize());
		
		int allCount = topicDao.getTopicCount(); //计算左外连后的总记录数
		pageBean.setAllCount(allCount);
		
		request.setAttribute("topicList", list);
		request.setAttribute("page", pageBean);
		
		//response.sendRedirect(request.getContextPath() + "/newspages/admin.jsp");
		request.getRequestDispatcher("/newspages/admin.jsp").forward(request, response);
	}

}
