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
<title>회원 - 회원정보 수정</title>
<style>
.member-modify-form>table {
	border-collapse: collapse;
}

.member-modify-form td, .member-modify-form th {
	border: 1px solid black;
	padding:10px;
}
.modify-button {
	padding-top:10px;
	padding-left:120px;
}
</style>
</head>
<body>
	<h1>회원정보 수정</h1>
	<form class="member-modify-form" action="doModify.sbs">
		<div>
			<input type="hidden" name="id" value="<%=loginedMember.get("id")%>">
		</div>
		<table>
			<tr>
				<th>이 름</th>
				<td><input type="text" name="name"
					value="<%=loginedMember.get("name")%>"></td>
			</tr>
			<tr>
				<th>아이디</th>
				<td><input type="text" name="loginId"
					value="<%=loginedMember.get("loginId")%>"></td>
			</tr>
			<tr>
				<th>기존 비밀번호</th>
				<td><input type="password" name="loginPw"
					placeholder="기존 비밀번호 입력"></td>
			</tr>
			<tr>
				<th>새로운 비밀번호</th>
				<td><input type="password" name="newloginPw"
					placeholder="새로운 비밀번호 입력"></td>
			</tr>
			<tr>
				<th>비밀번호 확인</th>
				<td><input type="password" name="newloginPw2"
					placeholder="비밀번호 확인"></td>
			</tr>
		</table>
		<div class="modify-button">
			<input type="submit" value="수정완료">
		</div>
	</form>
</body>
</html>