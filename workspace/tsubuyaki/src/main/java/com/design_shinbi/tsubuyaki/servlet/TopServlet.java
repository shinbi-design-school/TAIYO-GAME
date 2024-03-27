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
import com.design_shinbi.tsubuyaki.model.entity.User;
import com.design_shinbi.tsubuyaki.util.DbUtil;

@WebServlet("/top")
public class TopServlet extends HttpServlet {
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response) throws ServletException {
        HttpSession session = request.getSession();
        
        User user = (User)session.getAttribute(Const.LOGIN_USER_KEY);
        
        String jsp = null;
        if(user == null) {
            jsp = "/WEB-INF/jsp/login.jsp";
        }
        else {
            try {
                Connection connection = DbUtil.connect();
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
        
        try {
            RequestDispatcher dispatcher = request.getRequestDispatcher(jsp);
            dispatcher.forward(request, response);
        }
        catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
