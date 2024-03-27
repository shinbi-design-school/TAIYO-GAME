package com.design_shinbi.tsubuyaki.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.design_shinbi.tsubuyaki.model.Const;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException {
        HttpSession session = request.getSession();
        session.removeAttribute(Const.LOGIN_USER_KEY);

        String jsp = "/WEB-INF/jsp/login.jsp";

        try {
            RequestDispatcher dispatcher = request.getRequestDispatcher(jsp);
            dispatcher.forward(request, response);
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
