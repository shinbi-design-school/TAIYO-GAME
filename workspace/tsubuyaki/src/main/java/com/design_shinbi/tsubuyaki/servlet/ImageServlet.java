package com.design_shinbi.tsubuyaki.servlet;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.design_shinbi.tsubuyaki.model.dao.MessageDAO;
import com.design_shinbi.tsubuyaki.model.entity.Image;
import com.design_shinbi.tsubuyaki.util.DbUtil;


@WebServlet("/image")
public class ImageServlet extends HttpServlet {
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
            ) throws ServletException, IOException {
        try {
            Connection connection = DbUtil.connect();
            String id = request.getParameter("id");


            MessageDAO dao = new MessageDAO(connection);
            Image image = dao.getImage(Integer.parseInt(id));
            if(image != null && image.getStream() != null) {
                String fileName = image.getFileName();
                InputStream stream = image.getStream();


                if(fileName.toLowerCase().endsWith(".png")) {
                    response.setContentType("image/png");
                }
                else {
                    response.setContentType("image/jpeg");
                }


                byte[] buffer = new byte[1024];
                int length = 0;
                while((length = stream.read(buffer)) > 0) { //レスポンスのOutputStreamに画像のInputStreamを書き込む
                    response.getOutputStream().write(buffer, 0, length);
                }
            }
            connection.close();
        }
        catch(Exception e) {
            throw new ServletException(e);
        }
    }
}
