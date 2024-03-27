package com.design_shinbi.tsubuyaki.servlet;


import java.io.IOException;
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
import com.design_shinbi.tsubuyaki.model.entity.Message;
import com.design_shinbi.tsubuyaki.model.entity.User;
import com.design_shinbi.tsubuyaki.util.DbUtil;



@WebServlet("/message")
public class MessageServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String jsp = null;
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute(Const.LOGIN_USER_KEY);
        if (loginUser == null) {
            jsp = "/WEB-INF/jsp/login.jsp";
        }
        else {
            try {
                Connection connection = DbUtil.connect();
                MessageDAO dao = new MessageDAO(connection);


                String operation = request.getParameter("operation");


                if (operation.equals("edit")) {
                    jsp = this.editMessage(request, loginUser, dao);
                } else if (operation.equals("delete")) {
                    jsp = this.deleteMessage(request, loginUser, dao);
                }
                connection.close();
            }
            catch (Exception e) {
                throw new ServletException(e);
            }
        }


        RequestDispatcher dispatcher = request.getRequestDispatcher(jsp);
        dispatcher.forward(request, response);
    }


    private String editMessage(HttpServletRequest request, User user, MessageDAO dao) 
            throws Exception {
        String id = request.getParameter("id");
        Message message = dao.find(Integer.parseInt(id));


        String error = null;
        if (message.getUserId() != user.getId()) {
            error = "編集の権限がありません。";
        }


        String jsp = null;
        if (error == null) {
            request.setAttribute("message", message);
            jsp = "/WEB-INF/jsp/message.jsp";
        } 
        else {
            request.setAttribute("error", error);
            List<PostInfo> posts = dao.getPosts();
            request.setAttribute("posts", posts);


            jsp = "/WEB-INF/jsp/top.jsp";
        }


        return jsp;
    }


    private String deleteMessage(HttpServletRequest request, User user, MessageDAO dao) 
            throws Exception {
        String id = request.getParameter("id");
        Message message = dao.find(Integer.parseInt(id));


        if (message.getUserId() == user.getId() || user.isAdmin()) {
            dao.delete(message.getId());
        } else {
            String error = "削除する権限がありません。";
            request.setAttribute("error", error);
        }


        List<PostInfo> posts = dao.getPosts();
        request.setAttribute("posts", posts);


        String jsp = "/WEB-INF/jsp/top.jsp";
        return jsp;
    }
}
