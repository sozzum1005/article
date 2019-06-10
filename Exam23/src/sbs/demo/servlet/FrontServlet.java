package sbs.demo.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sbs.demo.controller.ArticleController;
import sbs.demo.controller.MemberController;
import sbs.demo.util.DBUtil;

@WebServlet("*.sbs")
public class FrontServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] uriBits = request.getRequestURI().split("/");

		String controllerName = uriBits[2];
		String funcName = uriBits[3];

		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");

		if (controllerName.equals("member")) {
			MemberController controller = new MemberController();
			controller.dbLink = DBUtil.getNewDBLink();

			if (funcName.equals("login.sbs")) {
				controller._login(request, response);
			} else if (funcName.equals("doLogin.sbs")) {
				controller._doLogin(request, response);
			} else if (funcName.equals("myPage.sbs")) {
				controller._myPage(request, response);
			} else if (funcName.equals("join.sbs")) {
				controller._join(request, response);
			} else if (funcName.equals("doJoin.sbs")) {
				controller._doJoin(request, response);
			} else if (funcName.equals("modify.sbs")) {
				controller._modify(request, response);
			} else if (funcName.equals("doModify.sbs")) {
				controller._doModify(request, response);
			} else if (funcName.equals("logout.sbs")) {
				controller._logout(request, response);
			}

			controller.dbLink.close();
		}
		
		if (controllerName.equals("article")) {
			ArticleController controller = new ArticleController();
			controller.dbLink = DBUtil.getNewDBLink();
			
			if (funcName.equals("list.sbs")) {
				controller._list(request, response);
			} else if (funcName.equals("doAdd.sbs")) {
				controller._doAdd(request, response);
			} else if (funcName.equals("doDelete.sbs")) {
				controller._doDelete(request, response);
			} else if (funcName.equals("detail.sbs")) {
				controller._detail(request, response);
			} else if (funcName.equals("doModify.sbs")) {
				controller._doModify(request, response);
			} else if (funcName.equals("modify.sbs")) {
				controller._modify(request, response);
			} else if (funcName.equals("doAddReply.sbs")) {
				controller._doAddReply(request, response);
			} else if (funcName.equals("doDeleteReply.sbs")) {
				controller._doDeleteReply(request, response);
			} else if (funcName.equals("doModifyReply.sbs")) {
				controller._doModifyReply(request, response);
			} else if (funcName.equals("add.sbs")) {
				controller._add(request, response);
			} 
			
			controller.dbLink.close();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
