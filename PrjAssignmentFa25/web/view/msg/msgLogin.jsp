<%-- 
    Document   : accessDenied
    Created on : Oct 26, 2025, 8:46:35 PM
    Author     : phuga
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Access Denied</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body class="auth-page">
        <!-- Bên BaseRequiredAuthenticationController req.setAttribute("errorLoginMsg", "Access Denied! You must login first!") -->

        <div class="container-accessDenied">
            <h1 class="errorAccessDenied-msg">${requestScope.errorLoginMsg}</h1>

            <!-- Thẻ p để cho chạy js thông báo redirect về trang login.jsp trong 8 seconds -->
            <p class="redirectAccessDenied-msg">Redirecting to login page on <span id="timer">8</span> seconds...</p>
        </div>

        <!-- này để trả về trang login sau 8 seconds -->
        <div id="redirectInfo" 
             data-redirect-url="${pageContext.request.contextPath}/login" 
             data-countdown-start="8">
        </div>

        <!-- Gọi file JS -->
        <script src="${pageContext.request.contextPath}/js/script.js"></script>
    </body>
</html>
