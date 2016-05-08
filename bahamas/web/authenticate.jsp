<%-- 
    Document   : authenticate
    Created on : 7 May, 2016, 1:20:07 PM
    Author     : Marcus
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
    String username = request.getParameter("username");
    String password = request.getParameter("password");

    if (username.equals("zuerst")) {
        if (password.equals("test123")) {
            session.setAttribute("user", "admin");
            response.sendRedirect("index.jsp");
        } else {
            String errorMsg = "Invalid username/password.";
            session.setAttribute("errorMsg", errorMsg);
            response.sendRedirect("login.jsp");
        }
    } else {
        String errorMsg = "Invalid username/password.";
        session.setAttribute("errorMsg", errorMsg);
        response.sendRedirect("login.jsp");
    }
%>