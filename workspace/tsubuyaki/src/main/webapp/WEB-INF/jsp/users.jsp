<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="com.design_shinbi.tsubuyaki.model.entity.User" %>

<%
    List<User> users = (List<User>)request.getAttribute("users");
    String error = (String)request.getAttribute("error");
%>

<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="head.jsp" />
        <title>ユーザー管理</title>
    </head>
    <body>
        <jsp:include page="header.jsp" />

        <main>
<%
    if(error != null) {
%>
            <div id="error"><%= error %></div>
<%
    }
%>
            <form id="user_form" action="user" method="post">
                <input type="hidden" id="user_id_input" name="id" value="">
                <input type="hidden" id="operation_input" name="operation" value="">
            </form>
            <div>
                <a href="javascript:newUser()">
                    <span class="icon fas fa-user-plus"></span>
                </a>
            </div>
            <table id="users">
                <tr>
                    <th>メールアドレス</th>
                    <th>名前</th>
                    <th>権限</th>
                    <th>操作</th>
                </tr>
<%
    for(User user : users) {
        String authority = "一般";
        if(user.isAdmin()) {
            authority = "管理者";
        }
%>
                <tr>
                    <td><%= user.getEmail() %></td>
                    <td><%= user.getName() %></td>
                    <td><%= authority %></td>
                    <td>
                        <a href="javascript:editUser(<%= user.getId() %>)">
                            <span class="icon fas fa-pencil-alt"></span>
                        </a>
                        <a href="javascript:deleteUser(<%= user.getId() %>)">
                            <span class="icon far fa-trash-alt"></span>
                        </a>
                    </td>
                </tr>
<%
    }
%>
            </table>
        </main>
        <jsp:include page="footer.jsp" />
    </body>
</html>
