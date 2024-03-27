package com.design_shinbi.tsubuyaki.servlet;

import java.sql.Connection;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.design_shinbi.tsubuyaki.model.Const;
import com.design_shinbi.tsubuyaki.model.PostInfo;
import com.design_shinbi.tsubuyaki.model.dao.MessageDAO;
import com.design_shinbi.tsubuyaki.model.dao.UserDAO;
import com.design_shinbi.tsubuyaki.model.entity.User;
import com.design_shinbi.tsubuyaki.util.DbUtil;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException {
        try {
            Connection connection = DbUtil.connect();
            String jsp = null;
            String error = "";
            User user = null;

            UserDAO userDao = new UserDAO(connection);

            String email = request.getParameter("email");
            if (email == null || email.isEmpty()) {
                error = "メールアドレスを入力してください。";
            }

            String password = request.getParameter("password");
            if (password == null || password.isEmpty()) {
                error = error + "パスワードを入力してください。";
            }

            if (error.isEmpty()) {
                user = userDao.login(email, password);
            }

            if (user == null) {
                if (error.isEmpty()) {
                    error = "ユーザー名もしくはパスワードが違います。";
                }
                request.setAttribute("error", error);
                jsp = "/WEB-INF/jsp/login.jsp";
            } 
            else {
                HttpSession session = request.getSession();
                session.setAttribute(Const.LOGIN_USER_KEY, user);

                try {
                    MessageDAO dao = new MessageDAO(connection);
                    List<PostInfo> posts = dao.getPosts();
                    request.setAttribute("posts", posts);

                    jsp = "/WEB-INF/jsp/top.jsp";

                    connection.close();
                }
                catch (Exception e) {
                    throw new ServletException(e);
                }
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher(jsp);
            dispatcher.forward(request, response);
        } 
        catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
