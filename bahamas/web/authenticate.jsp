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

    if (username.equals("admin")) {
        if (password.equals("123")) {
            session.setAttribute("user", "admin");
            if (request.getParameter("rememberme") == null) {
                //do nothing
            } else {
                Cookie c = new Cookie("username", username);
                c.setMaxAge(24 * 60 * 60);
                response.addCookie(c);
            }
            response.sendRedirect("admin.jsp");
        } else{
            String errorMsg = "Invalid username/password.";
            session.setAttribute("errorMsg", errorMsg);
            response.sendRedirect("login.jsp");
        }
    } else if(username.equals("novice")){
        if(password.equals("123")){
            session.setAttribute("user", "novice");
            if (request.getParameter("rememberme") == null) {
                //do nothing
            } else {
                Cookie c = new Cookie("username", username);
                c.setMaxAge(24 * 60 * 60);
                response.addCookie(c);
            }
            response.sendRedirect("novice.jsp");
        } else{
            String errorMsg = "Invalid username/password.";
            session.setAttribute("errorMsg", errorMsg);
            response.sendRedirect("login.jsp");
        }
    } else if(username.equals("user")){
        if(password.equals("123")){
            session.setAttribute("user", "user");
            if (request.getParameter("rememberme") == null) {
                //do nothing
            } else {
                Cookie c = new Cookie("username", username);
                c.setMaxAge(24 * 60 * 60);
                response.addCookie(c);
            }
            response.sendRedirect("user.jsp");
        } else{
            String errorMsg = "Invalid username/password.";
            session.setAttribute("errorMsg", errorMsg);
            response.sendRedirect("login.jsp");
        } 
        
    }else {
        String errorMsg = "Invalid username/password.";
        session.setAttribute("errorMsg", errorMsg);
        response.sendRedirect("login.jsp");
    }
%>
