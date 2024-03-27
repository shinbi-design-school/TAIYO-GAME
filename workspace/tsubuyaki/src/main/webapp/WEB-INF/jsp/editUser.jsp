<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="com.design_shinbi.tsubuyaki.model.entity.User" %>

<%
    String error = (String)request.getAttribute("error");
    User user = (User)request.getAttribute("user");
    int id = 0;
    String email = "";
    String name = "";
    String adminOption = "";
    String userOption = "checked";
    String submitName = "登録";
    String operationName = "add";
    String emailOption = "";
    if(user != null) {
        id = user.getId();
        email = user.getEmail();
        name = user.getName();
        if(user.isAdmin()) {
            adminOption = "checked";
            userOption = "";
        }
        submitName = "更新";
        operationName = "update";
        emailOption = "readonly";
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
            <form method="post" action="user">
                <table id="user_table">
                    <tr>
                        <th>メールアドレス</th>
                        <td>
                          <input type="email" name="email" 
								value="<%= email %>" <%= emailOption %>>
                        </td>
                    </tr>
                    <tr>
                        <th>名前</th>
                        <td><input type="text" name="name" value="<%= name %>"></td>
                    </tr>
                    <tr>
                        <th>権限</th>
                        <td>
                            <input type="radio" name="is_admin" value="true"
                                <%= adminOption %>> 管理者
                            <input type="radio" name="is_admin" value="false"
                                <%= userOption %>> 一般
                        </td>
                    </tr>
                    <tr>
                        <th>パスワード</th>
                        <td><input type="password" name="password"></td>
                    </tr>
                    <tr>
                        <th>パスワード(確認)</th>
                        <td><input type="password" name="confirmed"></td>
                    </tr>
                    <tr>
                        <td>
                            <input id="submit" type="submit" value="<%= submitName %>">
                        </td>
                    </tr>
                </table>
                <input type="hidden" name="id" value="<%= id %>">
                <input type="hidden" name="operation" value="<%= operationName %>">
            </form>
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
