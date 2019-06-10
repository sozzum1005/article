<%@ page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	List<Map<String, Object>> articles = (List)request.getAttribute("articles");
	Map<String, Object> member = (Map)request.getAttribute("member");

	boolean isNotLogined = session.getAttribute("loginId") == null || ((String)session.getAttribute("loginId")).length() == 0;
	boolean isLogined = !isNotLogined;
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시물 리스트</title>

<script
	src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>

<style>
.popup {
	position: fixed;
	width: 500px;
	height: 200px;
	border: 10px solid black;
	top: 50%;
	left: 50%;
	transform: translateX(-50%) translateY(-50%);
	background-color: white;
	display: none;
}

.popup>.head::after {
	content: "";
	display: block;
	clear: both;
}

.popup>.head>.btn-close {
	width: 40px;
	height: 40px;
	float: right;
	position: relative;
	margin: 10px;
	cursor: pointer;
}

.popup>.head>.btn-close:hover {
	transform: rotate(10deg);
}

.popup>.head>.btn-close::before, .popup>.head>.btn-close::after {
	content: "";
	display: block;
	position: absolute;
	width: 10%;
	height: 100%;
	background-color: black;
	top: 50%;
	left: 50%;
	transform: translateX(-50%) translateY(-50%) rotate(45deg);
}

.popup>.head>.btn-close::after {
	transform: translateX(-50%) translateY(-50%) rotate(-45deg);
}

.popup-bg {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background-color: rgba(0, 0, 0, 0.5);
	display: none;
}

.article-list-form>table{
	border-collapse: collapse;
}

.article-list-form td, .article-list-form th {
	border: 1px solid black;
	padding:10px;
}

</style>
<script>
	function deleteArticle(id) {
		$('.popup,.popup-bg').css('display', 'block');
		$('.article-delete-popup form input[name="id"]').val(id);
		$('.article-delete-popup form input[name="passwd"]').val('');
		$('.article-delete-popup form input[name="passwd"]').focus();
	}

	$(function() {
		$('.popup > .head > .btn-close, .popup input[type="reset"], .popup-bg')
				.click(function() {
					$('.popup,.popup-bg').css('display', 'none');
				});
	});

	function logineddeleteArticle(id){
		if ( confirm(id + '번 게시물을 삭제합니다.') == false ) {
	        return;
	    }

	    location.href = './doDelete.sbs?id=' + id;
	}
	
	function passwdFormSubmited(form) {

		form.passwd.value = form.passwd.value.trim();

		if (form.passwd.value.length < 4) {

			alert('비밀번호를 4자 이상 입력해주세요.');

			form.passwd.focus();

			return;

		}

		form.submit();
	}
</script>

</head>

<body>

<div class="popup-bg"></div>
	<div class="popup article-delete-popup">
		<div class="head">
			<div class="btn-close"></div>
		</div>
		<div>삭제하려면 게시물의 비밀번호를 입력해주세요.</div>
		<div class="body">
			<form action="./doDelete.sbs" method="POST"
				onsubmit="passwdFormSubmited(this); return false;">
				<input maxlength="4" type="password" name="passwd" placeholder="비밀번호"> <input type="hidden" name="id" value="">
				<input type="submit" value="삭제"> <input type="reset" value="취소">
			</form>
		</div>
	</div>

<h1>게시물 리스트 </h1>

<div>
	<a href="./add.sbs">글 작성하기</a>
	<a href="../member/myPage.sbs">마이페이지</a>
	<% if(isLogined) { %>
	<a href="../member/logout.sbs">로그아웃</a>
	<% } else { %>
	<a href="../member/login.sbs">로그인</a>
	<% } %>
</div>

<br>

<div class="article-list-form">

<table border="1">
<thead>
<tr>
	<th>번호</th>
	<th>날짜</th>
	<th>제목</th>
	<th>댓글개수</th>
	<th>비고</th>
</tr>
</thead>
<tbody>
<%
	for( Map<String, Object> article : articles ) {
%>
<tr>
	<td><%=article.get("id")%></td>
	<td><%=article.get("regDate")%></td>
	<td><a href="./detail.sbs?id=<%=article.get("id")%>"><%=article.get("title")%></a></td>
	<td><%=article.get("repliesCount")%></td>
	<td>
	<%if(isNotLogined) { %>
	<a href="javascript:deleteArticle(<%=article.get("id")%>);">삭제</a>
	<% } else if((long)member.get("id") == (long)article.get("memberId")) { %>
	<a href="javascript:logineddeleteArticle(<%=article.get("id")%>);">삭제</a>
	<% } %>
	</td>
	
</tr>
<%
	}
%>
</tbody>
</table>

</div>

</body>
</html>