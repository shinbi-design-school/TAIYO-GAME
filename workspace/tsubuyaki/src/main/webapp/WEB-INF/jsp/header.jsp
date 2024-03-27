<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="com.design_shinbi.tsubuyaki.model.entity.User" %>
<%@ page import="com.design_shinbi.tsubuyaki.model.Const" %>

<%
    User user = (User)session.getAttribute(Const.LOGIN_USER_KEY);
%>

<header>
<%
    if(user != null) {
%>
        <jsp:include page="menu.jsp" />
<%
    }
%>
    <div id="title">
        <h1 id="subtitle">つぶやきシステム</h1>
        <h1 id="systemName">つぶやいた～</h1>
    </div>
<%
    if(user != null) {
%>
        <div id="welcome">
            <%= user.getName() %> (<%= user.getEmail() %>)さん。ようこそ。
        </div>
<%
    }
%>
</header>
