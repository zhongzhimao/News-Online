<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="sidebar">
    <h1> <a href="${pageContext.request.contextPath}/NewsServlet?action=listNews&tid=1">
                 <img src="Images/title_1.gif" alt="国内新闻" /></a></h1>
    <div class="side_list">
      <ul>
        <!-- 国内新闻   标题超出10个字符长度，后面使用省略号.... 
             ${fn:length(shoppingCart.products)}
             ${fn:substring(zip, 6, -1)}
         -->
         <c:forEach var="news" items="${list1}">
     		<li><a href="${pageContext.request.contextPath}/NewsServlet?action=detail&nid=${news.nid}">
     		       <b>
     		       		<c:if test="${fn:length(news.ntitle)>12}">
     		       			${fn:substring(news.ntitle,0,12)}...
     		       		</c:if>
     		       		<c:if test="${fn:length(news.ntitle)<=12}">
     		       			${news.ntitle}
     		       		</c:if>
     		       </b>
     		     </a> 
     		</li>
     	 </c:forEach>
      	<!-- 国内新闻 -->
      </ul>
    </div>
    <h1> <a href="${pageContext.request.contextPath}/NewsServlet?action=listNews&tid=2">
                    <img src="Images/title_2.gif" alt="国际新闻" /></a></h1>
    <div class="side_list">
<ul>
        <!-- 国际新闻 -->
         <c:forEach var="news" items="${list2}">
     		<li> <a href="${pageContext.request.contextPath}/NewsServlet?action=detail&nid=${news.nid}">
     		       <b>
     		       		<c:if test="${fn:length(news.ntitle)>12}">
     		       			${fn:substring(news.ntitle,0,12)}...
     		       		</c:if>
     		       		<c:if test="${fn:length(news.ntitle)<=12}">
     		       			${news.ntitle}
     		       		</c:if>
     		       </b>
     		     </a> 
     		</li>
     	</c:forEach>
		<!-- 国际新闻 -->
		
      </ul>
    </div>
    <h1> <a href="${pageContext.request.contextPath}/NewsServlet?action=listNews&tid=5">
                    <img src="Images/title_3.gif" alt="娱乐新闻" /></a></h1>
    <div class="side_list">
      <ul>
         <!-- 娱乐新闻 -->
         <c:forEach var="news" items="${list5}">
     		<li> <a href="${pageContext.request.contextPath}/NewsServlet?action=detail&nid=${news.nid}">
     		       <b>
     		       		<c:if test="${fn:length(news.ntitle)>12}">
     		       			${fn:substring(news.ntitle,0,12)}...
     		       		</c:if>
     		       		<c:if test="${fn:length(news.ntitle)<=12}">
     		       			${news.ntitle}
     		       		</c:if>
     		       </b>
     		     </a> 
     		</li>
     	</c:forEach>
      	 <!-- 娱乐新闻 -->
      </ul>
    </div>
  </div>

