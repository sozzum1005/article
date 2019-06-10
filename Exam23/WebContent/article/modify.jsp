<%@ page import="java.util.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	int id = Integer.parseInt(request.getParameter("id"));
	Map<String, Object> article = (Map)request.getAttribute("article");
%>
<%
	boolean isNotLogined = session.getAttribute("loginId") == null || ((String)session.getAttribute("loginId")).length() == 0;
	boolean isLogined = !isNotLogined;
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시물 수정하기</title>
</head>
<body>
<form action="./doModify.sbs">
<input type="hidden" name="id" value="<%=id%>">
<div>
	<input value="<%=article.get("title")%>" type="text" name="title" placeholder="제목">
</div>
<div>
	<textarea name="body" placeholder="내용"><%=article.get("body")%></textarea>
</div>
<% if(isNotLogined) { %>
<div>
	<input type="text" name="passwd" placeholder="비밀번호를 입력하세요.">
</div>
<% } %>
<div>
	<input type="submit" value="수정하기">
	<input type="button" value="취소하기" onclick="location.href='./detail.sbs?id=<%=article.get("id")%>';">
</div>
</form>
</body>
</html>