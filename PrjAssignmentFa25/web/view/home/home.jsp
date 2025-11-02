<%-- 
    Document   : home
    Created on : Oct 26, 2025, 11:30:55 PM
    Author     : phuga
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <nav class="nav-bar">
            <div class="nav-brand">
                <a href="${pageContext.request.contextPath}/home">LeaveApp</a>
            </div>

            <div class="nav-menu">
                <%-- Link luôn hiển thị --%>
                <a href="${pageContext.request.contextPath}/home" class="active">Trang Chủ</a>
                <a href="${pageContext.request.contextPath}/request/create">Tạo Đơn</a>
                <a href="${pageContext.request.contextPath}/request/list">Xem Đơn</a> <%-- Sửa: Gộp Xem Đơn --%>

                <%--
                    Logic hiển thị menu động dựa trên Role.
                    Giả định User CHỈ CÓ 1 ROLE để làm đơn giản.
                    Nếu User có nhiều Role, bạn cần dùng <c:forEach> và kiểm tra phức tạp hơn.
                    Lưu ý: user.roles có thể null nếu Lazy Loading chưa chạy, cần check.
                --%>
                <c:if test="${not empty user.roles}">
                    <c:set var="userRole" value="${user.roles[0]}" /> <%-- Lấy Role đầu tiên --%>

                    <%-- Link Xét duyệt: Dành cho cấp bậc < 3 (Admin, Dir, Man, HR) --%>
                    <c:if test="${userRole.rlevel < 3}">
                        <a href="${pageContext.request.contextPath}/request/approve" class="approve-link">Xét Duyệt</a>
                    </c:if>

                    <%-- Link Agenda: Dành cho cấp bậc < 3 (Admin, Dir, Man, HR) --%>
                    <c:if test="${userRole.rlevel < 3}">
                        <a href="${pageContext.request.contextPath}/reports/agenda">Agenda</a>
                    </c:if>

                    <%-- Link Admin: Chỉ dành cho Admin (rcode 'ADMIN') --%>
                    <c:if test="${userRole.rcode eq 'ADMIN'}">
                        <a href="${pageContext.request.contextPath}/admin/management" class="admin-link">Admin</a>
                    </c:if>
                </c:if> <%-- Kết thúc kiểm tra user.roles --%>

            </div>

            <div class="user-info">
                <%-- Hiển thị displayname (từ User) hoặc ename (từ Employee nếu có) --%>
                Chào, <strong>${not empty user.displayname ? user.displayname : user.employee.name}</strong> |
                <a href="${pageContext.request.contextPath}/logout">Logout</a>
            </div>
        </nav>

        <div class="content-area">
            <div class="welcome-section">
                <h2>Xin chào!</h2>
                <p>Bạn đã đăng nhập thành công vào Hệ thống Quản lý Nghỉ Phép.</p>
                <%-- Hiển thị tên Role nếu đã được tải --%>
                <c:if test="${not empty user.roles}">
                    <p>Vai trò của bạn:
                    <c:forEach var="role" items="${user.roles}" varStatus="loop">
                        <strong>${role.rname}</strong><c:if test="${!loop.last}"> / </c:if>
                    </c:forEach>
                    </p>
                </c:if>
            </div>

            <%-- Khu vực nội dung chính của trang chủ có thể thêm vào đây --%>
            <%-- Ví dụ: Dashboard, thông báo, v.v... --%>

        </div>


    </body>
</html>
