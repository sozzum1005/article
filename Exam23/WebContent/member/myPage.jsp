<%@ page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	Map<String, Object> loginedMember = (Map<String, Object>) request.getAttribute("loginedMember");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 - 마이페이지</title>
<style>

.mypage-form>table{
	border-collapse:collapse;
}

.mypage-form td, .mypage-form th{
	border: 1px solid black;
	padding:10px;
}

</style>
</head>
<body>
	<h1>회원 - 마이페이지</h1>
	<div>
	<a href="./modify.sbs">수정하기</a>
	<a href="./logout.sbs">로그아웃</a>
	<a href="../article/list.sbs">글 리스트</a>
	</div>
	<br>
	<div class="mypage-form">
	<table border="1">
		<tbody>
			<tr>
				<th>아이디</th>
				<td><%=loginedMember.get("loginId")%></td>
			</tr>
			<tr>
				<th>이름</th>
				<td><%=loginedMember.get("name")%></td>
			</tr>
		</tbody>
	</table>
	</div>
</body>
</html>