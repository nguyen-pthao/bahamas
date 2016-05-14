<%-- 
    Document   : logout
    Created on : 8 May, 2016, 3:14:13 PM
    Author     : Marcus
--%>

<% if (session != null) {
        session.invalidate();
        response.sendRedirect("login.jsp");
    }
%>   
