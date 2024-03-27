<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="com.design_shinbi.tsubuyaki.model.Const" %>
<%@ page import="com.design_shinbi.tsubuyaki.model.entity.User" %>
<%@ page import="com.design_shinbi.tsubuyaki.model.entity.Message" %>
<%@ page import="com.design_shinbi.tsubuyaki.model.entity.Image" %>

<%
    User loginUser = (User)session.getAttribute(Const.LOGIN_USER_KEY);
    Message message = (Message)request.getAttribute("message");
    Image image = (Image)request.getAttribute("image");
    String error = (String)request.getAttribute("error");
    String imageFileName = "";
    if(image != null) {
        imageFileName = image.getFileName();
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="head.jsp" />
        <title>つぶやき編集</title>
    </head>
    <body>
        <jsp:include page="header.jsp" />

        <main>
            <div id="edit_message_area">
                <form id="new_message_form" method="post" action="postMessage"
                      enctype="multipart/form-data">
                    <div>
                        <textarea id="message_text" name="text"></textarea>
                    </div>
                    <div>
                        <span id="image_file_name"><%= imageFileName %></span>
                        <input id="message_image_file" type="file" name="image_file">
                        <a href="javascript:removeImage()">
                            <span class="icon red  fas fa-times-circle"></span>
                        </a>
                    </div>
                    <div>
                        <input id="submit" type="submit" name="submit" value="更新">
                    </div>
                    <input type="hidden" name="id" value="<%= message.getId() %>">
                    <input id="delete_image_input" type="hidden" name="delete_image_flag" value="false">
                </form>
                <script>
<%
    if(imageFileName == null || imageFileName.isEmpty()) {
%>
                    $('#image_file_name').css('display', 'none');
<%
    }
    else {
%>
                    $('#message_image_file').css('display', 'none');
<%
    }
%>
                    $('#message_text').val('<%= message.getText() %>');
                </script>
            </div>
<%
    if(error != null) {
%>
            <div id="error"><%= error %></div>
<%
    }
%>
        </main>
        <jsp:include page="footer.jsp" />
    </body>
</html>
