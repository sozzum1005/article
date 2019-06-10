<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	boolean isNotLogined = session.getAttribute("loginId") == null || ((String)session.getAttribute("loginId")).length() == 0;
	boolean isLogined = !isNotLogined;
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시물 작성</title>
</head>
<body>
<h1>게시물 작성하기</h1>
<form action="doAdd.sbs">
	<div>
		<input type="text" name="title" placeholder="제목을 입력하세요.">
	</div>
	<div>
		<textarea name="body" placeholder="내용을 입력하세요."></textarea>
	</div>
	<% if(isNotLogined) { %>
	<div>
		<input type="text" name="passwd" placeholder="비밀번호를 입력하세요.">
	</div>
	<% } %>
	<div>
		<input type="submit" value="등록하기">
		<input type="button" value="취소하기" onclick="location.href='./list.sbs'">
	</div>
</form>
</body>
</html>