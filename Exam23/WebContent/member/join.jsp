<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 - 회원가입</title>

<script>

	function submitJoinForm(form) {

		form.name.value = form.name.value.trim();

		if (form.name.value.length == 0) {
			form.name.focus();

			alert('이름을 입력해주세요.');

			return false;
		}
		
		form.loginId.value = form.loginId.value.trim();

		if (form.loginId.value.length == 0) {
			form.loginId.focus();

			alert('아이디를 입력해주세요.');

			return false;
		}
		
		form.loginPw.value = form.loginPw.value.trim();

		if (form.loginPw.value.length == 0) {
			form.loginPw.focus();

			alert('비밀번호를 입력해주세요.');

			return false;
		}

		form.submit();
	}
	
</script>

</head>
<body>
<h1>회원가입</h1>
	<form action="./doJoin.sbs" method="POST"
		onsubmit="submitJoinForm(this); return false;">
	<table>
		<tr>
			<th>이름 :</th> 
			<td><input type="text" name="name" placeholder="이름을 입력하세요."></td>
		</tr>
		<tr>
			<th>아이디 :</th> 
			<td><input type="text" name="loginId" placeholder="ID를 입력하세요."></td>
			<td>
		</tr>
		<tr>
			<th>비밀번호 :</th> 
			<td><input type="password" name="loginPw" placeholder="비밀번호를 입력하세요."></td>
		</tr>
		<tr>
			<th>회원가입 :</th>
			<td><input type="submit" value="회원가입"></td>
		</tr>
	</table>
</form>
</body>
</html>