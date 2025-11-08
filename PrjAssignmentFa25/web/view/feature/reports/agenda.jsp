<%-- 
    Document   : agenda
    Created on : Nov 9, 2025, 4:43:47 AM
    Author     : phuga
--%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Agenda - Lịch làm việc và nghỉ phép</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
        <style>
            /* ======= Bổ sung style riêng cho trang Agenda ======= */
            .agenda-table {
                width: 100%;
                border-collapse: collapse;
                background: #fff;
            }

            .agenda-table th, .agenda-table td {
                border: 1px solid #ccc;
                padding: 8px;
                text-align: center;
                min-width: 40px;
            }

            .agenda-table th.sticky {
                position: sticky;
                left: 0;
                background: #f8f9fa;
                z-index: 1;
                text-align: left;
            }

            .agenda-present {
                background: #a5d6a7; /* xanh nhạt - đi làm */
            }

            .agenda-absent {
                background: #ef9a9a; /* đỏ nhạt - nghỉ phép */
            }

            .agenda-legend {
                margin: 10px 0;
                font-size: 0.95rem;
                color: #555;
            }

            .chip {
                display: inline-block;
                width: 14px;
                height: 14px;
                border-radius: 3px;
                margin-right: 6px;
                vertical-align: middle;
            }

            .form-inline {
                margin-bottom: 1rem;
            }

            .form-inline input[type="date"] {
                margin: 0 5px;
            }
        </style>
    </head>
    <body>

        <nav class="nav-bar">
            <div class="nav-brand"><a href="${pageContext.request.contextPath}/home">LeaveApp</a></div>
            <div class="nav-menu">
                <a href="${pageContext.request.contextPath}/request/list">Xem Đơn</a>
                <a href="${pageContext.request.contextPath}/reports/agenda" class="approve-link">Agenda</a>
            </div>
            <div class="user-info">
                Chào, <strong>${sessionScope.acc.displayname}</strong> |
                <a href="${pageContext.request.contextPath}/logout">Logout</a>
            </div>
        </nav>

        <div class="content-area">
            <h1>Lịch làm việc của cấp dưới</h1>

            <c:if test="${not empty errorMessage}">
                <div class="login-message error">${errorMessage}</div>
            </c:if>

            <!-- Bộ lọc ngày -->
            <form method="get" action="${pageContext.request.contextPath}/reports/agenda" class="form-inline">
                Từ ngày:
                <input type="date" name="from" value="${start}">
                Đến ngày:
                <input type="date" name="to" value="${end}">
                <button class="form-submit-button" type="submit">Xem</button>
            </form>

            <!-- Chú thích -->
            <div class="agenda-legend">
                <span class="chip" style="background:#a5d6a7"></span> Đi làm
                &nbsp;&nbsp;
                <span class="chip" style="background:#ef9a9a"></span> Nghỉ phép (đã duyệt)
            </div>

            <!-- Bảng Agenda -->
            <table class="agenda-table">
                <thead>
                    <tr>
                        <th class="sticky">Nhân sự</th>
                            <c:forEach var="d" items="${days}">
                            <th>${d.getDayOfMonth()}/${d.getMonthValue()}</th>
                            </c:forEach>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="e" items="${team}">
                        <tr>
                            <th class="sticky">${e.name}</th>
                                <c:set var="row" value="${leaveMatrix[e.id]}"/>
                                <c:forEach var="idx" begin="0" end="${fn:length(days)-1}">
                                    <c:choose>
                                        <c:when test="${row[idx]}">
                                        <td class="agenda-absent"></td>
                                    </c:when>
                                    <c:otherwise>
                                        <td class="agenda-present"></td>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </tr>
                    </c:forEach>

                    <c:if test="${empty team}">
                        <tr>
                            <td colspan="${fn:length(days)+1}" style="color:#6c757d; text-align:center;">
                                Không có cấp dưới để hiển thị.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

    </body>
</html>
