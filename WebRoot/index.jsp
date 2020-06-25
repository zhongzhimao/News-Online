<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="index-elements/index_top.jsp"></jsp:include>
<div id="container">
	<jsp:include page="index-elements/index_sidebar.jsp" />
	<div class="main">
		<div class="class_type">
			<img src="Images/class_type.gif" alt="新闻中心" />
		</div>
		<div class="content">
			<ul class="class_date">
				<li id='class_month'>
				<a href="${pageContext.request.contextPath}/NewsServlet?action=listNews">全部</a>
				<!--遍历 显示topic list -->
				<c:forEach var="topic" items="${topicList}">
					<a href="${pageContext.request.contextPath}/NewsServlet?action=listNews&tid=${topic.tid}"><b>${topic.tname}</b></a> 
				</c:forEach>
				<!--遍历 显示topic list -->			</ul>

			<ul class="classlist">
				<!-- 遍历显示 news list  -->
				<c:forEach var="news" items="${requestScope.newsList}">
					<li><a href="${pageContext.request.contextPath}/NewsServlet?action=detail&nid=${news.nid}">${news.ntitle}</a> <span>${news.ncreatedate}</span></li>
				</c:forEach>
				<!-- 遍历显示 news list  -->

				<!-- 分页显示   request.getParameter("tid")-->
				<c:if test="${param.tid!=null}">
					<p align="right">
					当前页数:[${pageNo}/${allPages}]&nbsp;&nbsp; 
					    <a href="${pageContext.request.contextPath}/NewsServlet?action=listNews&pageNo=1&tid=${param.tid}">首页</a>
					    <a href="${pageContext.request.contextPath}/NewsServlet?action=listNews&pageNo=${prev}&tid=${param.tid}">&nbsp;&nbsp;上一页</a>
						<a href="${pageContext.request.contextPath}/NewsServlet?action=listNews&pageNo=${next}&tid=${param.tid}">&nbsp;&nbsp;下一页</a> 
						<a href="${pageContext.request.contextPath}/NewsServlet?action=listNews&pageNo=${allPages}&tid=${param.tid}">&nbsp;&nbsp;末页</a>
				     </p>			
				</c:if>
				<c:if test="${param.tid==null}">
					<p align="right">
						当前页数:[${pageNo}/${allPages}]&nbsp;&nbsp; 
						    <a href="${pageContext.request.contextPath}/NewsServlet?action=listNews&pageNo=1">首页</a>
						    <a href="${pageContext.request.contextPath}/NewsServlet?action=listNews&pageNo=${prev}">&nbsp;&nbsp;上一页</a>
							<a href="${pageContext.request.contextPath}/NewsServlet?action=listNews&pageNo=${next}">&nbsp;&nbsp;下一页</a> 
							<a href="${pageContext.request.contextPath}/NewsServlet?action=listNews&pageNo=${allPages}">&nbsp;&nbsp;末页</a>
					</p>				
				</c:if>

			</ul>
		</div>
		<jsp:include page="index-elements/index_rightbar.html" />
	</div>
</div>

<jsp:include page="index-elements/index_bottom.html" />
