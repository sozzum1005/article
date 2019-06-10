<%@ page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	Map<String, Object> article = (Map)request.getAttribute("article");
	List<Map<String, Object>> articleReplies = (List)request.getAttribute("articleReplies");
	
	boolean isNotLogined = session.getAttribute("loginId") == null || ((String)session.getAttribute("loginId")).length() == 0;
	boolean isLogined = !isNotLogined;
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시물 상세페이지</title>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.0/jquery.min.js"></script>

<style>
.article-replies-list tr .edit-mode-visible {
    display:none;
}

.article-replies-list tr.edit-mode .edit-mode-visible {
    display:block;
}

.article-replies-list tr.edit-mode .read-mode-visible {
    display:none;
}

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

</style>

<script>

function deleteArticle(id) {
	$('.article-delete-popup,.popup-bg').css('display', 'block');
	var $popup = $('.article-delete-popup');
	$popup.find('input[name="id"]').val(id);
	$popup.find('input[name="passwd"]').val('');
	$popup.find('input[name="passwd"]').focus();
}

function logineddeleteArticle(id){
	if ( confirm(id + '번 게시물을 삭제합니다.') == false ) {
        return;
    }

    location.href = './doDelete.sbs?id=' + id;
}

function deleteArticleReply(id) {
	$('.article-reply-delete-popup,.popup-bg').css('display', 'block');
	var $popup = $('.article-reply-delete-popup');
	$popup.find('input[name="id"]').val(id);
	$popup.find('input[name="passwd"]').val('');
	$popup.find('input[name="passwd"]').focus();
}

$(function() {
	$('.popup-bg, .popup > .head > .btn-close, .popup input[type="reset"]')
			.click(function() {
				$('.popup, .popup-bg').css('display', 'none');
			});
});

function logineddeleteArticleReply(id) {
	if( confirm(id + '번 댓글을 삭제합니다.') == false ){
		return;
	}
	location.href = './doDeleteReply.sbs?id=' + id;
}

function enableEditMode(el) {

	var $el = $(el);

	var $tr = $el.closest('tr');

	$tr.addClass('edit-mode');

}

function disableEditMode(el) {

	var $el = $(el);

	var $tr = $el.closest('tr');

	$tr.removeClass('edit-mode');

}

function addFormSubmited(form) {

	form.body.value = form.body.value.trim();

	if (form.body.value.length == 0) {
		alert('내용을 입력해주세요.');
		form.body.focus();

		return;
	}

	form.passwd.value = form.passwd.value.trim();

	if (form.passwd.value.length < 4) {

		alert('비밀번호를 4자 이상 입력해주세요.');

		form.passwd.focus();

		return;

	}

	form.submit();

}

function modifyFormSubmited(form) {

	form.body.value = form.body.value.trim();

	if (form.body.value.length == 0) {
		alert('내용을 입력해주세요.');
		form.body.focus();

		return;
	}

	form.passwd.value = form.passwd.value.trim();

	if (form.passwd.value.length < 4) {

		alert('비밀번호를 4자 이상 입력해주세요.');

		form.passwd.focus();

		return;

	}

	form.submit();
}

function passwdFormSubmited(form) {

	form.passwd.value = form.passwd.value.trim();

	if (form.passwd.value.length == 0) {
		alert('비밀번호를 입력해주세요.');
		form.passwd.focus();

		return;
	}

	else if (form.passwd.value.length < 4) {

		alert('비밀번호를 4자 이상 입력해주세요.');

		form.passwd.focus();

		return;

	}

	form.submit();
}

</script>

</head>
<body>
<h1>게시물 상세페이지</h1>

<div class="popup-bg"></div>

	<div class="popup article-delete-popup">
		<div class="head">
			<div class="btn-close"></div>
		</div>
		<div>삭제하시려면 게시물의 비밀번호를 입력해주세요.</div>
		<div class="body">
			<form action="./doDelete.sbs" method="POST"
				onsubmit="passwdFormSubmited(this); return false;">
				<input type="password" name="passwd" placeholder="비밀번호"> 
				<input type="hidden" name="id" value=""> <input type="submit" value="삭제"> 
				<input type="reset" value="취소">
			</form>
		</div>
	</div>

	<div class="popup article-reply-delete-popup">
		<div class="head">
			<div class="btn-close"></div>
		</div>
		<div>삭제하려면 댓글의 비밀번호를 입력해주세요.</div>
		<div class="body">
			<form action="./doDeleteReply.sbs" method="POST"
				onsubmit="passwdFormSubmited(this); return false;">
				<input type="password" name="passwd" placeholder="비밀번호"> <input
					type="hidden" name="id" value=""> <input type="submit"
					value="삭제"> <input type="reset" value="취소">
			</form>
		</div>
	</div>

<div>
	<a href="./list.sbs">글 리스트</a>
	<a href="./add.jsp">글 작성하기</a>
	<a href="./modify.sbs?id=<%=article.get("id")%>">글 수정하기</a>
	<%if( isNotLogined ) {%>
	<a href="javascript:deleteArticle(<%=article.get("id")%>);">글 삭제하기</a>
	<% } else {%>
	<a href="javascript:logineddeleteArticle(<%=article.get("id")%>);">글 삭제하기</a>
	<% } %>
</div>
<h2>글 상세보기</h2>
<table border="1">
<tr>
	<th>번호</th>
	<td><%=article.get("id")%></td>
</tr>
<tr>
	<th>날짜</th>
	<td><%=article.get("regDate")%></td>
</tr>
<tr>
	<th>제목</th>
	<td><%=article.get("title")%></td>
</tr>
<tr>
	<th>내용</th>
	<td><%=article.get("body")%></td>
</tr>
</table>

<h2>댓글 작성</h2>
<form action="./doAddReply.sbs" method="POST" onsubmit="addFormSubmited(this); return false;">
	<input type="hidden" name="articleId" value="<%=article.get("id")%>">
	<div>
		<textarea name="body" placeholder="내용"></textarea>
	</div>
	<% if(isNotLogined) { %>
	<div>
		<input type="password" name="passwd" placeholder="비밀번호를 입력하세요.">
	</div>
	<% } %>
	<div>
		<input type="submit" value="댓글작성">
		<input type="reset" value="취소">
	</div>
</form>

<h2>댓글 리스트</h2>
<% if(articleReplies.size() > 0) { %>
<table border="1" class="article-replies-list">
<thead>
<tr>
	<th>번호</th>
	<th>날짜</th>
	<th>내용</th>
	<th>비고</th>
</tr>
</thead>
<tbody>
<%
	for( Map<String, Object> articleReply : articleReplies) {
%>
<tr>
	<td><%=articleReply.get("id") %></td>
	<td><%=articleReply.get("regDate") %></td>
	<td>
		<div class="read-mode-visible">
			<%=articleReply.get("body") %>
		</div>
		<div class="edit-mode-visible">
			<form action="./doModifyReply.sbs" method="POST" onsubmit="modifyFormSubmited(this); return false;">
				<input type="hidden" name="id" value="<%=articleReply.get("id")%>">
				<input type="hidden" name="articleId" value="<%=article.get("id")%>">
				<div>
					<textarea name="body" placeholder="댓글내용"><%=articleReply.get("body") %></textarea>
				</div>
				<% if( isNotLogined ) { %>
				<div>
					<input type="password" name="passwd" placeholder="비밀번호를 입력하세요.">
				</div>
				<% } %>
				<div>
					<input type="submit" value="댓글수정">
					<input onclick="disableEditMode(this);" type="reset" value="수정취소">
				</div>
			</form>
		</div>
	</td>
	<td>
		<% if( isNotLogined ) { %>
		<a href="javascript:deleteArticleReply(<%=articleReply.get("id")%>);">삭제</a>
		<% } else {%>
		<a href="javascript:logineddeleteArticleReply(<%=articleReply.get("id")%>);">삭제</a>
		<% } %>
		<a class="read-mode-visible" href="javascript:;" onclick="enableEditMode(this);">수정</a>
	</td>
</tr>
<% } %>
</tbody>
</table>
<% } else { %>
<div>댓글이 없습니다!</div>
<% } %>
</body>
</html>