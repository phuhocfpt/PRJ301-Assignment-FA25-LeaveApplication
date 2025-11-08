<%-- 
    Document   : historyRFL
    Created on : Nov 8, 2025, 11:42:12 AM
    Author     : phuga
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Lịch sử trạng thái đơn nghỉ phép</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>

        <nav class="nav-bar">
            <div class="nav-brand"><a href="${pageContext.request.contextPath}/home">LeaveApp</a></div>
            <div class="nav-menu">
                <a href="${pageContext.request.contextPath}/request/list">Xem Đơn</a>
            </div>
            <div class="user-info">
                Chào, <strong>${sessionScope.acc.displayname}</strong> |
                <a href="${pageContext.request.contextPath}/logout">Logout</a>
            </div>
        </nav>

        <div class="content-area">
            <h1>Lịch sử trạng thái đơn nghỉ phép</h1>

            <div class="form-container">
                <p><strong>Mã đơn:</strong> ${reqid}</p>
                <table class="request-table">
                    <thead>
                        <tr>
                            <th>Thời gian thay đổi</th>
                            <th>Người thay đổi</th>
                            <th>Trạng thái mới</th>
                            <th>Ghi chú</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="h" items="${requestScope.historyLists}">
                            <tr>
                                <td><fmt:formatDate value="${h.changedAt}" pattern="dd/MM/yyyy HH:mm:ss"/></td>
                                <td>${h.changedByName}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${h.newStatus == 0}"><span class="status status-pending">Chờ duyệt</span></c:when>
                                        <c:when test="${h.newStatus == 1}"><span class="status status-approved">Đã duyệt</span></c:when>
                                        <c:when test="${h.newStatus == 2}"><span class="status status-rejected">Đã từ chối</span></c:when>
                                    </c:choose>
                                </td>
                                <td>${empty h.note ? '-' : h.note}</td>
                            </tr>
                        </c:forEach>

                        <c:if test="${empty requestScope.historyLists}">
                            <tr><td colspan="4" style="text-align:center;color:#6c757d;">Chưa có thay đổi nào trong lịch sử duyệt.</td></tr>
                        </c:if>
                    </tbody>
                </table>

                <div style="margin-top:1.5rem;">
                    <a href="${pageContext.request.contextPath}/request/detail?reqid=${requestScope.reqid}" class="form-submit-button">Quay lại chi tiết</a>
                    <a href="${pageContext.request.contextPath}/request/list" class="form-submit-button" style="background-color:#6c757d;">Về danh sách</a>
                </div>
            </div>
        </div>

    </body>
</html>

