<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 - 로그인</title>

<script>
	function submitLoginForm(form) {
		form.loginId.value = form.loginId.value.trim();

		if (form.loginId.value.length == 0) {
			form.loginId.focus();

			alert('로그인 아이디를 입력해주세요.');

			return false;
		}

		form.loginPw.value = form.loginPw.value.trim();

		if (form.loginPw.value.length == 0) {
			form.loginPw.focus();

			alert('로그인 비번을 입력해주세요.');

			return false;
		}

		form.submit();
	}
</script>
</head>
<body>
	<h1>회원 - 로그인</h1>

	<form action="./doLogin.sbs" method="POST"
		onsubmit="submitLoginForm(this); return false;">
		<table>
			<tbody>
				<tr>
					<th>아이디</th>
					<td><input maxlength="30" type="text" name="loginId"
						placeholder="로그인 아이디" />
				</tr>
				<tr>
					<th>비번</th>
					<td><input maxlength="30" type="password" name="loginPw"
						placeholder="로그인 비번" />
				</tr>

				<tr>
					<th>로그인</th>
					<td><input type="submit" value="로그인" />
				</tr>
				<tr>
					<th>회원가입</th>
					<td><input type="button" value="회원가입" onclick="location.href='./join.sbs';"/>
				</tr>
			</tbody>
		</table>
	</form>
</body>
</html>