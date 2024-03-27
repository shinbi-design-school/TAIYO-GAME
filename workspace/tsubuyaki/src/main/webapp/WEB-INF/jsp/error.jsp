<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
    String error = (String)request.getAttribute("error");
%>

<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="head.jsp" />
        <title>エラー</title>
    </head>
    <body>
        <jsp:include page="header.jsp" />

        <main>
            <div id="error"><%= error %></div>
        </main>
        <jsp:include page="footer.jsp" />
    </body>
</html>
