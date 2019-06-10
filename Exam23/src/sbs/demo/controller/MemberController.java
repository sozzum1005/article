package sbs.demo.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sbs.demo.util.CookieUtil;
import sbs.demo.util.DBUtil;

public class MemberController {

	public DBUtil.DBLink dbLink;

	private static void forwardJsp(HttpServletRequest request, HttpServletResponse response, String jspPath)
			throws ServletException, IOException {
		ServletContext context = request.getServletContext();
		RequestDispatcher dispatcher = context.getRequestDispatcher(jspPath); // 데이터 넘길 페이지 주소
		dispatcher.forward(request, response);
	}

	public void _login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		forwardJsp(request, response, "/member/login.jsp");
	}

	public void _doLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String loginId = request.getParameter("loginId");
		String loginPw = request.getParameter("loginPw");

		String sql = "SELECT COUNT(*) AS cnt FROM member WHERE loginId = '" + loginId + "' AND loginPw = '" + loginPw
				+ "'";
		int cnt = dbLink.getRowIntValue(sql);

		if (cnt == 0) {
			String msg = "일치하는 회원이 없습니다.";
			response.getWriter().append("<script> alert('" + msg + "'); history.back(); </script>");

			return;
		}

		//CookieUtil.setAttribute(response, "loginId", loginId);
		request.getSession().setAttribute("loginId", loginId);

		String msg = "로그인 되었습니다.";
		response.getWriter().append("<script> alert('" + msg + "'); location.replace('./myPage.sbs'); </script>");
	}

	public void _myPage(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		//String loginId = CookieUtil.getAttribute(request, "loginId");
		String loginId = (String) request.getSession().getAttribute("loginId");

		if (loginId == null || loginId.length() == 0) {
			String msg = "로그인 후 접근해주세요.";
			response.getWriter().append("<script> alert('" + msg + "'); location.replace('./login.sbs'); </script>");

			return;
		}

		Map<String, Object> loginedMember = dbLink.getRow("SELECT * FROM member WHERE loginId = '" + loginId + "' LIMIT 1");

		request.setAttribute("loginedMember", loginedMember);

		forwardJsp(request, response, "/member/myPage.jsp");
	}
	
	public void _join(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		forwardJsp(request, response, "/member/join.jsp");
	}
	
	public void _doJoin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String loginId = request.getParameter("loginId");
		String loginPw = request.getParameter("loginPw");
		String name = request.getParameter("name");
		
		String sql = "SELECT COUNT(*) AS cnt FROM member WHERE loginId = '" + loginId + "'";
		int cnt = dbLink.getRowIntValue(sql);

		if (cnt == 0) {
			String msg = "가입되었습니다! 로그인 후 이용해주세요!";
			
			dbLink.executeQuery("INSERT INTO member SET  regDate = NOW(), loginId = '" +loginId+ "', loginPw = '" +loginPw+ "', name = '" +name+ "'");
			
			response.getWriter().append("<script> alert('" + msg + "');</script>");
			response.getWriter().append("<script> location.replace('./login.sbs'); </script>");

			return;
		}
		else {
			String msg = "이미 사용중인 아이디 입니다. 다른 아이디를 사용해주세요.";
			
			response.getWriter().append("<script> alert('"+msg+"'); history.back(); </script>");
		}

		//CookieUtil.setAttribute(response, "loginId", loginId);
		request.getSession().setAttribute("loginId", loginId);

	}
	
	
	public void _modify(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		//String loginId = CookieUtil.getAttribute(request, "loginId");
		String loginId = (String) request.getSession().getAttribute("loginId");

		Map<String, Object> loginedMember = dbLink.getRow("SELECT * FROM member WHERE loginId = '" + loginId + "' LIMIT 1");

		request.setAttribute("loginedMember", loginedMember);

		forwardJsp(request, response, "/member/modify.jsp");
	}
	
	public void _doModify(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = request.getParameter("id");
		String loginId = request.getParameter("loginId");
		String loginPw = request.getParameter("loginPw");
		String name = request.getParameter("name");
		String newloginPw = request.getParameter("newloginPw");
		String newlonginPw2 = request.getParameter("newloginPw2");
		
		String sql = "SELECT loginPw FROM member WHERE id = '" + id + "'";
		String passwd = dbLink.getRowStringValue(sql);
		
		
		if (passwd.equals(loginPw) && newloginPw.equals(newlonginPw2) ) {
			String msg = "개인정보가 수정되었습니다!";
			
			dbLink.executeQuery("UPDATE member SET name = '" + name + "', loginId = '" + loginId + "', loginPw = '" + newloginPw + "' WHERE id = '" + id + "'");
			
			response.getWriter().append("<script> alert('" + msg + "');</script>");
			response.getWriter().append("<script> location.replace('./myPage.sbs'); </script>");

			return;
		}
		else {
			String msg = "비밀번호를 제대로 입력해주세요.";
			
			response.getWriter().append("<script> alert('"+msg+"'); history.back(); </script>");
		}

		//CookieUtil.setAttribute(response, "loginId", loginId);
		request.getSession().setAttribute("loginId", loginId);

	}

	public void _logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		String loginId = (String)request.getSession().getAttribute("loginId");
		
		if(loginId != null || loginId.length() != 0 ) {
			
			request.getSession().invalidate();
			response.getWriter().append("<script> alert('로그아웃 되었습니다!'); </script>");
			response.getWriter().append("<script> location.replace('../article/list.sbs'); </script>");
			
		}
		
	}


}
