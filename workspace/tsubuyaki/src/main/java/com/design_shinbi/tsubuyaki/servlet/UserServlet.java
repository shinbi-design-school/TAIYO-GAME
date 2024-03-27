package com.design_shinbi.tsubuyaki.servlet;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.design_shinbi.tsubuyaki.model.Const;
import com.design_shinbi.tsubuyaki.model.dao.UserDAO;
import com.design_shinbi.tsubuyaki.model.entity.User;
import com.design_shinbi.tsubuyaki.util.DbUtil;

@WebServlet("/user")
public class UserServlet extends HttpServlet {
	@Override
	protected void service(
			HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession();
		User loginUser = (User) session.getAttribute(Const.LOGIN_USER_KEY);

		String jsp = null;
		if (loginUser == null) {
			jsp = "/WEB-INF/jsp/login.jsp";
		} else if (!loginUser.isAdmin()) {
			request.setAttribute("error", "権限がありません。");
			jsp = "/WEB-INF/jsp/error.jsp";
		} else {
			try {
				jsp = operate(request, loginUser);
			} catch (Exception e) {
				throw new ServletException(e);
			}
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher(jsp);
		dispatcher.forward(request, response);
	}

	private String operate(HttpServletRequest request, User loginUser)
			throws Exception {
		String jsp = null;
		Connection connection = DbUtil.connect();
		String operation = request.getParameter("operation");
		UserDAO dao = new UserDAO(connection);
		if (operation != null) {
			if (operation.equals("new")) {
				jsp = newUser(request, dao);
			} else if (operation.equals("edit")) {
				jsp = editUser(request, dao);
			} else if (operation.equals("add")) {
				jsp = addUser(request, dao);
			} else if (operation.equals("update")) {
				jsp = updateUser(request, dao);
			} else if (operation.equals("delete")) {
				jsp = deleteUser(request, dao, loginUser);
			}
		}
		if (jsp == null) {
			jsp = getList(request, dao);
		}

		return jsp;
	}

	private String getList(HttpServletRequest request, UserDAO dao)
			throws SQLException {
		List<User> users = dao.findAll();
		request.setAttribute("users", users);

		String jsp = "/WEB-INF/jsp/users.jsp";
		return jsp;
	}

	private String newUser(HttpServletRequest request, UserDAO dao) {
		String jsp = "/WEB-INF/jsp/editUser.jsp";
		return jsp;
	}

	private String addUser(HttpServletRequest request, UserDAO dao)
			throws SQLException, NoSuchAlgorithmException {
		String jsp = null;
		String error = "";

		String email = request.getParameter("email");
		if (email == null || email.isEmpty()) {
			error = "メールアドレスを入力してください。";
		} else {
			User user = dao.findByEmail(email);
			if (user != null) {
				error = "そのメールアドレスは既に使われています。";
			}
		}

		String name = request.getParameter("name");
		if (name == null || name.isEmpty()) {
			error += "名前を入力してください。";
		}

		String isAdmin = request.getParameter("is_admin");
		String password = request.getParameter("password");
		if (password == null || password.isEmpty()) {
			error += "パスワードを入力してください。";
		}

		String confirmed = request.getParameter("confirmed");
		if (!password.equals(confirmed)) {
			error += "パスワードが一致しません。";
		}

		if (error.isEmpty()) {
			dao.add(name, email, password, Boolean.parseBoolean(isAdmin));
			jsp = this.getList(request, dao);
		} else {
			request.setAttribute("error", error);
			jsp = "/WEB-INF/jsp/editUser.jsp";
		}

		return jsp;
	}

	private String editUser(HttpServletRequest request, UserDAO dao)
			throws NumberFormatException, SQLException {
		String id = request.getParameter("id");
		User user = dao.find(Integer.parseInt(id));
		request.setAttribute("user", user);
		String jsp = "/WEB-INF/jsp/editUser.jsp";

		return jsp;
	}

	private String updateUser(HttpServletRequest request, UserDAO dao)
			throws SQLException, NoSuchAlgorithmException {
		String jsp = null;
		String error = "";

		int id = Integer.parseInt(request.getParameter("id"));
		User user = dao.find(id);

		String name = request.getParameter("name");
		if (name == null || name.isEmpty()) {
			error += "名前を入力してください。";
		}

		String isAdmin = request.getParameter("is_admin");

		String password = request.getParameter("password");
		String confirmed = request.getParameter("confirmed");
		if (!password.equals(confirmed)) {
			error += "パスワードが一致しません。";
		}

		if (error.isEmpty()) {
			user.setName(name);
			user.setAdmin(Boolean.parseBoolean(isAdmin));
			dao.update(user);

			if (!password.isEmpty()) {
				dao.updatePassword(id, password);
			}
			jsp = this.getList(request, dao);
		} else {
			request.setAttribute("user", user);
			request.setAttribute("error", error);
			jsp = "/WEB-INF/jsp/editUser.jsp";
		}

		return jsp;
	}

	private String deleteUser(HttpServletRequest request, UserDAO dao, User loginUser)
			throws SQLException {
		String error = "";

		int id = Integer.parseInt(request.getParameter("id"));

		User user = dao.find(id);
		if (user.getId() == loginUser.getId()) {
			error = "現在ログイン中のユーザーを消すことはできません。";
			request.setAttribute("error", error);
		} else {
			dao.delete(id);
		}

		String jsp = this.getList(request, dao);

		return jsp;
	}
}
