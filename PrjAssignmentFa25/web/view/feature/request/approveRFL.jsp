<%-- 
    Document   : approveRFL
    Created on : Nov 8, 2025, 5:25:07 PM
    Author     : phuga
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Xét duyệt đơn</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css"> 
    </head>
    <body>
        <nav class="nav-bar">
            <div class="nav-brand"><a href="${pageContext.request.contextPath}/home">LeaveApp</a></div>
            <div class="nav-menu">
                <a href="${pageContext.request.contextPath}/request/list">Xem Đơn</a>
                <a href="${pageContext.request.contextPath}/request/approve" class="approve-link">Xét Duyệt</a>
            </div>
            <div class="user-info">
                Chào, <strong>${sessionScope.acc.displayname}</strong> |
                <a href="${pageContext.request.contextPath}/logout">Logout</a>
            </div>
        </nav>

        <div class="content-area">
            <h1>Xét duyệt đơn #${r.id}</h1>

            <c:if test="${not empty requestScope.errorMessage}">
                <div class="login-message error">${requestScope.errorMessage}</div>
            </c:if>

            <c:if test="${not empty requestScope.successMessage}">
                <div class="login-message" style="background:#d4edda;color:#155724;border:1px solid #c3e6cb;">
                    ${requestScope.successMessage}
                </div>
            </c:if>

            <div class="form-container">
                <div class="form-grid">
                    <div class="form-group"><label>ID đơn:</label><span>${r.id}</span></div>
                    <div class="form-group"><label>Người tạo:</label><span>${r.createdBy.name}</span></div>
                    <div class="form-group"><label>Phòng ban:</label><span>${r.department.dname}</span></div>
                    <div class="form-group"><label>Từ ngày:</label><fmt:formatDate value="${r.fromDate}" pattern="dd/MM/yyyy"/></div>
                    <div class="form-group"><label>Đến ngày:</label><fmt:formatDate value="${r.toDate}" pattern="dd/MM/yyyy"/></div>
                    <div class="form-group"><label>Loại lý do:</label><span>${r.reasonType.rname}</span></div>
                    <div class="form-group"><label>Thời gian tạo đơn:</label>
                        <fmt:formatDate value="${r.createdTime}" pattern="dd/MM/yyyy HH:mm:ss"/></div>
                    <div class="form-group-full">


                        <div class="form-group">
                            <label>Trạng thái hiện tại:</label>
                            <c:choose>
                                <c:when test="${r.status == 0}"><span class="status status-pending">Chờ duyệt</span></c:when>
                                <c:when test="${r.status == 1}"><span class="status status-approved">Đã duyệt</span></c:when>
                                <c:when test="${r.status == 2}"><span class="status status-rejected">Từ chối</span></c:when>
                            </c:choose>
                        </div>

                        <c:if test="${not empty r.processedBy}">
                            <div class="form-group"><label>Người xử lý gần nhất:</label><span>${r.processedBy.name}</span></div>
                            <div class="form-group"><label>Ghi chú hiện tại:</label><span>${empty r.decisionNote ? '-' : r.decisionNote}</span></div>
                        </c:if>

                        <label>Lý do khác:</label>
                        <p>${empty r.reasonOthers ? '-' : r.reasonOthers}</p>
                    </div>
                </div>

                <hr style="margin: 1rem 0;border:none;border-top:1px solid #eee;"/>

                <form method="post" action="${pageContext.request.contextPath}/request/approve">
                    <input type="hidden" name="reqid" value="${r.id}"/>

                    <div class="form-group-full">
                        <label>Ghi chú quyết định</label>
                        <textarea name="note" placeholder="Nhập ghi chú (tuỳ chọn)"></textarea>
                    </div>

                    <button class="form-submit-button" type="submit" name="action" value="approve">Phê duyệt</button>
                    <button class="form-submit-button" type="submit" name="action" value="reject" style="background-color:#dc3545;">Từ chối</button>
                    <a class="form-submit-button" href="${pageContext.request.contextPath}/request/detail?reqid=${r.id}" style="background-color:#6c757d;">Xem chi tiết</a>
                    <a class="form-submit-button" href="${pageContext.request.contextPath}/request/history?reqid=${r.id}" style="background-color:#17a2b8;">Lịch sử</a>
                    <a class="form-submit-button" href="${pageContext.request.contextPath}/request/approve" style="background-color:#6c757d;">Quay lại</a>
                </form>
            </div>
        </div>
    </body>
</html>
