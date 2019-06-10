package sbs.demo.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sbs.demo.util.DBUtil.DBLink;

public class ArticleController {
	
	public DBLink dbLink;
	
	private static void forwardJsp(HttpServletRequest request, HttpServletResponse response, String jspPath)
			throws ServletException, IOException {
		ServletContext context = request.getServletContext();
		RequestDispatcher dispatcher = context.getRequestDispatcher(jspPath); // 데이터 넘길 페이지 주소
		dispatcher.forward(request, response);
	}

	public String _list(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.getWriter().append("list 실행됨");

		String sql = "SELECT article.id, article.regDate, article.title, article.memberId, SUM(IF(articleReply.id IS NULL, 0, 1)) AS repliesCount ";
		sql += "FROM article ";
		sql	+= "LEFT JOIN articleReply ";
		sql	+= "ON article.id = articleReply.articleId ";
		sql += "GROUP BY article.id ";
		sql	+= "ORDER BY article.id";
		
		List<Map<String, Object>> articles = dbLink.getRows(sql);
		
		request.setAttribute("articles", articles);
		
		String loginId = (String)request.getSession().getAttribute("loginId");
		
		Map<String, Object> member = dbLink.getRow("SELECT * FROM member WHERE loginId = '" +loginId+ "'");
		
		request.setAttribute("member", member);
		
		ServletContext context = request.getServletContext();
		RequestDispatcher dispatcher = context.getRequestDispatcher("/article/list.jsp"); // 넘길 페이지 주소
		dispatcher.forward(request, response);

		return "";
	}

	public String _doAdd(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().append("doAdd 실행됨");
		
		String loginId = (String)request.getSession().getAttribute("loginId");
		
		Map<String, Object> member = dbLink.getRow("SELECT * FROM member WHERE loginId = '" + loginId +"'");
		
		String title = request.getParameter("title");
		
		if (title == null) {
			response.getWriter().append("<script> alert('제목을 입력해주세요.'); history.back(); </script>");
		}

		title = title.trim();

		if (title.length() == 0) {
			response.getWriter().append("<script> alert('제목을 입력해주세요.'); history.back(); </script>");

		}

		String body = request.getParameter("body");

		if (body == null) {
			response.getWriter().append("<script> alert('내용을 입력해주세요.'); history.back(); </script>");
		}

		body = body.trim();

		if (body.length() == 0) {
			response.getWriter().append("<script> alert('내용을 입력해주세요.'); history.back(); </script>");

		}
		String passwd = request.getParameter("passwd");
		
		
		if(loginId == null || loginId.length() == 0){
			passwd = passwd.replaceAll("\'", "\\\\'");

		if (passwd == null) {
			response.getWriter().append("<script> alert('비밀번호를 입력해주세요.'); history.back(); </script>");
		}

		passwd = passwd.trim();

		if (passwd.length() == 0) {
			response.getWriter().append("<script> alert('비밀번호를 입력해주세요.'); history.back(); </script>");

		}

		if (passwd.length() > 4 || passwd.length() < 4) {
			response.getWriter().append("<script> alert('비밀번호를 4자리로 입력해주세요.'); history.back(); </script>");

		}

		if (passwd.length() == 4) {
			
			dbLink.executeQuery("INSERT INTO article SET regDate = NOW(), title = '" +title+ "', body = '" +body+ "', passwd = '" +passwd+ "', memberId = 0");

			int id = dbLink.getLastInsertId();
			
			response.getWriter().append("<script> alert('"+id+"번 게시물이 등록되었습니다.'); </script>");
			response.getWriter().append("<script> location.replace('./detail.sbs?id="+ id +"'); </script>");

			
			}
		}
		else {
			dbLink.executeQuery("INSERT INTO article SET regDate = NOW(), title = '" +title+ "', body = '" +body+ "', passwd = '', memberId = '" +member.get("id")+ "'" );
			
			int id = dbLink.getLastInsertId();
			
			response.getWriter().append("<script> alert('"+id+"번 게시물이 등록되었습니다.'); </script>");
			response.getWriter().append("<script> location.replace('./detail.sbs?id="+id+"'); </script>");
		}
		
		
				
		return "";
	}

	public String _doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().append("doDelete 실행됨");

		String id = request.getParameter("id");
		String passwd = request.getParameter("passwd");
		String articleId = request.getParameter("articleId");
		String url = request.getHeader("referer");
		
		String loginId = (String)request.getSession().getAttribute("loginId");
		
		Map<String, Object> member = dbLink.getRow("SELECT * FROM member WHERE loginId = '" +loginId+ "'");
		
		Map<String, Object> article = dbLink.getRow("SELECT * FROM article WHERE id = '" +id+ "'");
		
		if(loginId == null || loginId.length() == 0) {
		
		if (passwd == null) {
			response.getWriter().append("<script> alert('비밀번호를 입력해주세요.'); history.back(); </script>");
		}

		passwd = passwd.trim();

		if (passwd.length() == 0) {
			response.getWriter().append("<script> alert('비밀번호를 입력해주세요.'); history.back(); </script>");

		}
		
		passwd = passwd.replaceAll("\'", "\\\\'");
		
		
		if(article.get("passwd").equals(passwd)) {
			
			dbLink.executeQuery("DELETE FROM article WHERE id = '" +id+ "'");
			dbLink.executeQuery("DELETE FROM articleReply WHERE articleId = '" +id+ "'");
			
			response.getWriter().append("<script> alert('"+id+"번 게시물이 삭제되었습니다.'); </script>");
			response.getWriter().append("<script> location.replace('./list.sbs'); </script>");

		} 

		else {
			response.getWriter().append("<script> alert('"+id+"번 게시물의 비밀번호가 틀렸습니다.'); </script>");
			response.getWriter().append("<script> location.replace('"+url+"'); </script>");
			}
		} 
		else {
			if((long)article.get("memberId") == (long)member.get("id") ) {
			
				dbLink.executeQuery("DELETE FROM article WHERE memberId = '" +article.get("memberId")+ "' AND id = '" +id+ "'");
			
			response.getWriter().append("<script> alert('"+id+"번 게시물이 삭제되었습니다.'); </script>");
			response.getWriter().append("<script> location.replace('./list.sbs'); </script>");
			
			}
			else {
								
				response.getWriter().append("<script> alert('삭제할 권한이 없습니다.'); </script>");
				response.getWriter().append("<script> location.replace('./list.sbs'); </script>");
				
			}
		}
		
		return "";
	}

	public String _detail(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.getWriter().append("detail 실행됨");

		String id = request.getParameter("id");
	
		
		Map<String, Object> article = dbLink.getRow("SELECT * FROM article WHERE id = '" +id+ "'");
		List<Map<String, Object>> articleReplies = dbLink.getRows("SELECT * FROM articleReply WHERE articleId = '" +id+ "'");
		request.setAttribute("article", article);
		request.setAttribute("articleReplies", articleReplies);
		
		ServletContext context = request.getServletContext();
		RequestDispatcher dispatcher = context.getRequestDispatcher("/article/detail.jsp"); // 넘길 페이지 주소
		dispatcher.forward(request, response);
	
		return "";
	}
	
	public String _modify(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		response.getWriter().append("modify 실행됨");

		String id = request.getParameter("id");
		
		Map<String, Object> article = dbLink.getRow("SELECT * FROM article WHERE id = '" +id+ "'");
		
		request.setAttribute("article", article);
		
		ServletContext context = request.getServletContext();
		RequestDispatcher dispatcher = context.getRequestDispatcher("/article/modify.jsp"); // 넘길 페이지 주소
		dispatcher.forward(request, response);
	
		return "";
	}

	public String _doModify(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().append("doModify 실행됨");

		String id = request.getParameter("id");
		String title = request.getParameter("title");
		String body = request.getParameter("body");
		String passwd = request.getParameter("passwd");
		String url = request.getHeader("referer");
		String loginId = (String)request.getSession().getAttribute("loginId");
		
		Map<String, Object> member = dbLink.getRow("SELECT * FROM member WHERE id = '" +loginId+ "'");
		
		Map<String, Object> article = dbLink.getRow("SELECT * FROM article WHERE id = '" +id+ "'");
		
		if( loginId == null || loginId.length() == 0 ) {
		
		if (passwd == null) {
			response.getWriter().append("<script> alert('비밀번호를 입력해주세요.'); history.back(); </script>");
		}
		
		passwd = passwd.trim();

		if (passwd.length() == 0) {
			response.getWriter().append("<script> alert('비밀번호를 입력해주세요.'); history.back(); </script>");
		}
		
		passwd = passwd.replaceAll("\'", "\\\\'");
		
		
		
		if(article.get("passwd").equals(passwd)) {
			dbLink.executeQuery("UPDATE article SET regDate = NOW(), title = '" +title+ "', body = '" +body+ "' WHERE id = '" +id+ "'");
			response.getWriter().append("<script> alert('"+id+"번 게시물이 수정되었습니다.'); </script>");
			response.getWriter().append("<script> location.replace('./detail.sbs?id="+id+"'); </script>");

		} 
		else {
			response.getWriter().append("<script> alert('"+id+"번 게시물의 비밀번호가 틀렸습니다.'); </script>");
			response.getWriter().append("<script> location.replace('"+url+"'); </script>");
			}	
		}
		
		else {
			
			if((long)article.get("memberId") == (long)member.get("id")) {
			
				dbLink.executeQuery("UPDATE article SET title = '" +title+ "', body = '" +body+ "' WHERE id = '" +id+ "'");
				response.getWriter().append("<script> alert('게시물이 수정되었습니다.'); </script>");
				response.getWriter().append("<script> location.replace('./detail.sbs?id="+id+"'); </script>");
			
			}
			
			else {
				
				response.getWriter().append("<script> alert('수정할 권한이 없습니다.'); </script>");
				response.getWriter().append("<script> location.replace('./detail.sbs?id="+id+"'); </script>");
				
			}
			
		}
		
		
		return "";
	}
	
	public String _doDeleteReply(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().append("doDeleteReply 실행됨");

		String id = request.getParameter("id");
		String passwd = request.getParameter("passwd");
		String url = request.getHeader("referer");
		String loginId = (String)request.getSession().getAttribute("loginId");
		
		Map<String, Object> articleReply = dbLink.getRow("SELECT * FROM articleReply WHERE id = '" +id+ "'");
		
		Map<String, Object> member = dbLink.getRow("SELECT * FROM member WHERE loginId = '" +loginId+ "'");
		
		if(loginId == null || loginId.length() == 0) {
			
		passwd = passwd.replaceAll("\'", "\\\\'");
		
		if (passwd == null) {
			response.getWriter().append("<script> alert('비밀번호를 입력해주세요.'); history.back(); </script>");
		}

		passwd = passwd.trim();

		if (passwd.length() == 0) {
			response.getWriter().append("<script> alert('비밀번호를 입력해주세요.'); history.back(); </script>");

		}
		
		if(articleReply.get("passwd").equals(passwd)) {
			
			dbLink.executeQuery("DELETE FROM articleReply WHERE id = '" +id+ "'");

			response.getWriter().append("<script> alert('"+id+"번 댓글이 삭제되었습니다.'); </script>");
			response.getWriter().append("<script> location.replace('"+url+"'); </script>");

		} 

		else {
			response.getWriter().append("<script> alert('"+id+"번 게시물의 비밀번호가 틀렸습니다.'); </script>");
			response.getWriter().append("<script> location.replace('"+url+"'); </script>");
			}	
		}
		
		else {
			
			if(member.get("id") == articleReply.get("memberId")) {
				
			dbLink.executeQuery("DELETE FROM articleReply WHERE id = '" +id+ "'");
			
			response.getWriter().append("<script> alert('"+id+"번 댓글이 삭제되었습니다.'); </script>");
			response.getWriter().append("<script> location.replace('"+url+"'); </script>");
			
			}
			else {
				
				response.getWriter().append("<script> alert('게시물을 삭제할 권한이 없습니다.'); </script>");
				response.getWriter().append("<script> location.replace('"+url+"'); </script>");
				
			}
		}

		
		return "";
	}

	
	public String _doAddReply(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().append("doAddReply 실행됨");

		String articleId = request.getParameter("articleId");
		String url = request.getHeader("referer");
		String body = request.getParameter("body");
		String loginId = (String)request.getSession().getAttribute("loginId");
		
		Map<String, Object> member = dbLink.getRow("SELECT * FROM member WHERE loginId = '" +loginId+ "'");
		
		if (body == null) {
			response.getWriter().append("<script> alert('내용을 입력해주세요.'); history.back(); </script>");
		}

		body = body.trim();

		if (body.length() == 0) {
			response.getWriter().append("<script> alert('내용을 입력해주세요.'); history.back(); </script>");

		}
		
		String passwd = request.getParameter("passwd");
		
		if(loginId == null || loginId.length() == 0) {
		
		if (passwd == null) {
			response.getWriter().append("<script> alert('비밀번호를 입력해주세요.'); history.back(); </script>");
		}

		passwd = passwd.trim();

		if (passwd.length() == 0) {
			response.getWriter().append("<script> alert('비밀번호를 입력해주세요.'); history.back(); </script>");

		}

		if (passwd.length() > 4 || passwd.length() < 4) {
			response.getWriter().append("<script> alert('비밀번호를 4자리로 입력해주세요.'); history.back(); </script>");

		}
		
		if (passwd.length() == 4) {
			dbLink.executeQuery("INSERT INTO articleReply SET regDate = NOW(), body = '" +body+ "', articleId = '" +articleId+ "', passwd = '" +passwd+ "', memberId = '" +0+ "'");

			response.getWriter().append("<script> alert('댓글이 등록되었습니다.'); </script>");
			response.getWriter().append("<script> location.replace('"+url+"'); </script>");

			}
		}
		else {
			
			dbLink.executeQuery("INSERT INTO articleReply SET regDate = NOW(), body = '" +body+ "', articleId = '" +articleId+ "', passwd = '', memberId = '" +member.get("id")+ "'" );
			response.getWriter().append("<script> alert('댓글이 등록되었습니다.'); </script>");
			response.getWriter().append("<script> location.replace('"+url+"'); </script>");
			
		}
		
		return "";
	}
	
	public String _doModifyReply(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.getWriter().append("domodifyReply 실행됨");
		
		String id = request.getParameter("id");
		String articleId = request.getParameter("articleId");
		String body = request.getParameter("body");
		String passwd = request.getParameter("passwd");
		String url = request.getHeader("referer");
		
		String loginId = (String)request.getSession().getAttribute("loginId");
		
		Map<String, Object> articleReply = dbLink.getRow("SELECT * FROM articleReply WHERE id = '" +id+ "'");
		
		Map<String, Object> member = dbLink.getRow("SELECT * FROM member WHERE loginId = '" +loginId+ "'");
		
		if(loginId == null || loginId.length() == 0) {
		
		
		if (passwd == null) {
			response.getWriter().append("<script> alert('비밀번호를 입력해주세요.'); history.back(); </script>");
		}
		
		passwd = passwd.trim();

		if (passwd.length() == 0) {
			response.getWriter().append("<script> alert('비밀번호를 입력해주세요.'); history.back(); </script>");
		}
		
		passwd = passwd.replaceAll("\'", "\\\\'");
		
		if(articleReply.get("passwd").equals(passwd)) {
			dbLink.executeQuery("UPDATE articleReply SET regDate = NOW(), body = '" +body+ "', articleId = '" +articleId+ "' WHERE id = '" +id+ "'");

			response.getWriter().append("<script> alert('댓글이 수정되었습니다.'); </script>");
			response.getWriter().append("<script> location.replace('"+url+"'); </script>");


		} 
		else {
			response.getWriter().append("<script> alert('"+id+"번 게시물의 비밀번호가 틀렸습니다.'); </script>");
			response.getWriter().append("<script> location.replace('"+url+"'); </script>");
			}
		}
		
		else {
			
			if(member.get("id") == articleReply.get("memberId")) {
			
			dbLink.executeQuery("UPDATE articleReply SET body = '" +body+ "' WHERE id = '" +id+ "'");
			response.getWriter().append("<script> alert('댓글이 수정되었습니다.'); </script>");
			response.getWriter().append("<script> location.replace('"+url+"'); </script>");
			}
			
			else {
				
				response.getWriter().append("<script> alert('댓글 수정 권한이 없습니다.'); </script>");
				response.getWriter().append("<script> location.replace('"+url+"'); </script>");
				
			}
		}
		
		return "";
	}

	public void _add(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		forwardJsp(request, response, "/article/add.jsp");
	}
	
	
}