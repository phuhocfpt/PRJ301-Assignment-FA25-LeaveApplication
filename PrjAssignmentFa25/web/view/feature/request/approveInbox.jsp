<%-- 
    Document   : approveInbox
    Created on : Nov 8, 2025, 5:24:09 PM
    Author     : phuga
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Xét Duyệt Đơn</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <nav class="nav-bar">
            <div class="nav-brand"><a href="${pageContext.request.contextPath}/home">LeaveApp</a></div>
            <div class="nav-menu">
                <a href="${pageContext.request.contextPath}/home">Trang Chủ</a>
                <a href="${pageContext.request.contextPath}/request/list">Xem Đơn</a>
                <a href="${pageContext.request.contextPath}/request/approve" class="approve-link">Xét Duyệt</a>
            </div>
            <div class="user-info">
                Chào, <strong>${sessionScope.acc.displayname}</strong> |
                <a href="${pageContext.request.contextPath}/logout">Logout</a>
            </div>
        </nav>

        <div class="content-area">
            <h1>Đơn chờ duyệt</h1>

            <c:if test="${not empty requestScope.errorMessage}">
                <div class="login-message error">${requestScope.errorMessage}</div>
            </c:if>

            <table class="request-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Người tạo</th>
                        <th>Ngày tạo</th>
                        <th>Từ ngày</th>
                        <th>Đến ngày</th>
                        <th>Loại lý do</th>
                        <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="r" items="${requestScope.inbox}">
                        <tr>
                            <td>${r.id}</td>
                            <td>${r.createdBy.name}</td>
                            <td><fmt:formatDate value="${r.createdTime}" pattern="dd/MM/yyyy HH:mm"/></td>
                            <td><fmt:formatDate value="${r.fromDate}" pattern="dd/MM/yyyy"/></td>
                            <td><fmt:formatDate value="${r.toDate}" pattern="dd/MM/yyyy"/></td>
                            <td>${r.reasonType.rname}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/request/detail?reqid=${r.id}">Xem</a> |
                                <a href="${pageContext.request.contextPath}/request/approve?reqid=${r.id}">Duyệt</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty requestScope.inbox}">
                        <tr><td colspan="6" style="text-align:center;color:#6c757d;">Không có đơn chờ duyệt.</td></tr>
                    </c:if>
                </tbody>
            </table>

            <c:if test="${requestScope.totalPage > 1}">
                <div class="pagination">
                    <c:forEach begin="1" end="${requestScope.totalPage}" var="i">
                        <a href="${pageContext.request.contextPath}/request/approve?page=${i}"
                           class="${i == requestScope.pageIndex ? 'active' : ''}">${i}</a>
                    </c:forEach>
                </div>
            </c:if>
        </div>
    </body>
</html>
