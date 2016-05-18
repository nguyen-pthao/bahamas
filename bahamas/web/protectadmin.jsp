<%-- 
    Document   : protectadmin
    Created on : 8 May, 2016, 3:22:13 PM
    Author     : Marcus
--%>
<%
    String userLevel = (String)session.getAttribute("userLevel");
    if(!userLevel.equals("admin")){
        response.sendRedirect("user.jsp");
    }
    
%>
