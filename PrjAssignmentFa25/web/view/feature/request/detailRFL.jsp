<%-- 
    Document   : detailRFL
    Created on : Nov 8, 2025, 8:33:26 AM
    Author     : phuga
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Chi tiết đơn nghỉ phép</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <nav class="nav-bar">
            <div class="nav-brand"><a href="${pageContext.request.contextPath}/home">LeaveApp</a></div>
            <div class="nav-menu">
                <a href="${pageContext.request.contextPath}/home">Trang Chủ</a>
                <a href="${pageContext.request.contextPath}/request/list">Xem Đơn</a>
            </div>
            <div class="user-info">
                Chào, <strong>${sessionScope.acc.displayname}</strong> |
                <a href="${pageContext.request.contextPath}/logout">Logout</a>
            </div>
        </nav>

        <div class="content-area">
            <h1>Chi tiết đơn nghỉ phép</h1>

            <div class="form-container">
                <div class="form-grid">
                    <div class="form-group">
                        <label>ID đơn:</label>
                        <span>${r.id}</span>
                    </div>
                    <div class="form-group">
                        <label>Người tạo:</label>
                        <span>${r.createdBy.name}</span>
                    </div>
                    <div class="form-group">
                        <label>Phòng ban:</label>
                        <span>${r.department.dname}</span>
                    </div>
                    <div class="form-group">
                        <label>Từ ngày:</label>
                        <span><fmt:formatDate value="${r.fromDate}" pattern="dd/MM/yyyy"/></span>
                    </div>
                    <div class="form-group">
                        <label>Đến ngày:</label>
                        <span><fmt:formatDate value="${r.toDate}" pattern="dd/MM/yyyy"/></span>
                    </div>
                    <div class="form-group">
                        <label>Loại lý do:</label>
                        <span>${r.reasonType.rname}</span>
                    </div>
                    <div class="form-group-full">
                        <label>Lý do khác:</label>
                        <p>${empty r.reasonOthers ? '-' : r.reasonOthers}</p>
                    </div>
                    <div class="form-group">
                        <label>Trạng thái:</label>
                        <c:choose>
                            <c:when test="${r.status == 0}"><span class="status status-pending">Chờ duyệt</span></c:when>
                            <c:when test="${r.status == 1}"><span class="status status-approved">Đã duyệt</span></c:when>
                            <c:when test="${r.status == 2}"><span class="status status-rejected">Đã từ chối</span></c:when>
                        </c:choose>
                    </div>

                    <div class="form-group">
                        <label>Thời gian tạo đơn:</label>
                        <span><fmt:formatDate value="${r.createdTime}" pattern="dd/MM/yyyy HH:mm:ss"/></span>
                    </div>
                    
                    <div class="form-group">
                        <label>Người duyệt:</label>
                        <span>${empty r.processedBy ? '---' : r.processedBy.name}</span>
                    </div>
                </div>

                <div style="margin-top:2rem;">
                    <a href="${pageContext.request.contextPath}/request/list" class="form-submit-button">Quay lại danh sách</a>
                    <a href="${pageContext.request.contextPath}/request/history?reqid=${r.id}" class="form-submit-button" style="background-color:#17a2b8;">Xem lịch sử</a>
                </div>
            </div>
        </div>
    </body>
</html>
