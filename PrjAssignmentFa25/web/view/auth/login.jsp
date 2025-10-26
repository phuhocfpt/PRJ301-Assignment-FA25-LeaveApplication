<%-- 
    Document   : login
    Created on : Oct 26, 2025, 3:20:55 PM
    Author     : phuga
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Page</title>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">

    </head>
    <body class="auth-page">

        <div class="container">

            <form class="auth-form" action="${pageContext.request.contextPath}/login" method="POST">
                <h1>Login</h1>

                <!-- 
                Khối này sẽ đảm bảo <div> chỉ được 
                tạo ra KHI CÓ lỗi (khi "errorloginMsg" không rỗng).
                -->

                <c:if test="${not empty requestScope.errorloginMsg}">
                    <div class="login-message error">
                        ${requestScope.errorloginMsg}
                    </div>
                </c:if>

                <!-- Nhóm Username -->
                <div class="form-group">
                    <label for="username">Username:</label>
                    <input type="text" id="username" name="username" required>
                </div>

                <!-- Nhóm Password -->
                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" required>
                </div>

                <!-- Nút Login -->
                <button type="submit" class="auth-button">Login</button>
            </form>

        </div>

    </body>
</html>
