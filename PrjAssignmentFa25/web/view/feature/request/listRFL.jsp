<%--
    Document : listRFL
    Created on : Oct 26, 2025
    Author : phuga
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Danh Sách Đơn Nghỉ Phép</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>

        <%-- NAV BAR --%>
        <nav class="nav-bar">
            <div class="nav-brand"><a href="${pageContext.request.contextPath}/home">LeaveApp</a></div>
            <div class="nav-menu">
                <a href="${pageContext.request.contextPath}/home">Trang Chủ</a>
                <a href="${pageContext.request.contextPath}/request/create">Tạo Đơn</a>
                <a href="${pageContext.request.contextPath}/request/list" class="active">Xem Đơn</a>
                <c:if test="${not empty sessionScope.acc.roles}">
                    <c:set var="userRole" value="${sessionScope.acc.roles[0]}" />
                    <c:if test="${userRole.rlevel < 3}">
                        <a href="${pageContext.request.contextPath}/request/approve" class="approve-link">Xét Duyệt</a>
                        <a href="${pageContext.request.contextPath}/reports/agenda">Agenda</a>
                    </c:if>
                    <c:if test="${userRole.rcode eq 'ADMIN'}">
                        <a href="${pageContext.request.contextPath}/admin/management" class="admin-link">Admin</a>
                    </c:if>
                </c:if>
            </div>
            <div class="user-info">
                Chào, <strong>${sessionScope.acc.displayname}</strong> |
                <a href="${pageContext.request.contextPath}/logout">Logout</a>
            </div>
        </nav>

        <div class="content-area">
            <h1>Danh Sách Đơn Nghỉ Phép</h1>

            <c:if test="${not empty requestScope.errorMessage}">
                <div class="login-message error">${requestScope.errorMessage}</div>
            </c:if>

            <!-- TAB HEADER -->
            <div class="tab-container">
                <div id="tab_my" class="tab-link active" onclick="openTab(event, 'myRequests')">Đơn của tôi</div>
                <c:if test="${requestScope.isManager}">
                    <div id="tab_team" class="tab-link" onclick="openTab(event, 'teamRequests')">Đơn của Team</div>
                </c:if>
            </div>

            <!-- TAB: ĐƠN CỦA TÔI -->
            <div id="myRequests" class="tab-content active">
                <table class="request-table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Từ Ngày</th>
                            <th>Đến Ngày</th>
                            <th>Loại Lý Do</th>
                            <th>Trạng Thái</th>
                            <th>Người Duyệt</th>
                            <th>Chi tiết</th>
                            <th>Lịch sử</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="r" items="${requestScope.listRflEmp}">
                            <tr>
                                <td>${r.id}</td>
                                <td><fmt:formatDate value="${r.fromDate}" pattern="dd/MM/yyyy"/></td>
                                <td><fmt:formatDate value="${r.toDate}" pattern="dd/MM/yyyy"/></td>
                                <td>${r.reasonType.rname}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${r.status == 0}"><span class="status status-pending">Chờ duyệt</span></c:when>
                                        <c:when test="${r.status == 1}"><span class="status status-approved">Đã duyệt</span></c:when>
                                        <c:when test="${r.status == 2}"><span class="status status-rejected">Đã từ chối</span></c:when>
                                    </c:choose>
                                </td>
                                <td>${not empty r.processedBy ? r.processedBy.name : '---'}</td>
                                <td><a href="${pageContext.request.contextPath}/request/detail?reqid=${r.id}">Xem</a></td>

                                <td>
                                    <a href="${pageContext.request.contextPath}/request/history?reqid=${r.id}">Lịch sử</a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty requestScope.listRflEmp}">
                            <tr><td colspan="8" style="text-align:center;color:#6c757d;">Bạn chưa tạo đơn nào.</td></tr>
                        </c:if>
                    </tbody>
                </table>

                <!-- PHÂN TRANG - TAB MY -->
                <c:if test="${requestScope.totalPageEmp > 1}">
                    <div class="pagination">
                        <c:forEach begin="1" end="${requestScope.totalPageEmp}" var="i">
                            <a href="${pageContext.request.contextPath}/request/list?page=${i}&tab=my"
                               class="${i == requestScope.pageIndexEmp ? 'active' : ''}">${i}</a>
                        </c:forEach>
                    </div>
                </c:if>
            </div>

            <!-- TAB: ĐƠN CỦA TEAM -->
            <c:if test="${requestScope.isManager}">
                <div id="teamRequests" class="tab-content">
                    <table class="request-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Người Tạo</th>
                                <th>Từ Ngày</th>
                                <th>Đến Ngày</th>
                                <th>Loại Lý Do</th>
                                <th>Trạng Thái</th>
                                <th>Xem</th>
                                <th>Hành động</th>
                                <th>Lịch sử</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="r" items="${requestScope.listRflDept}">
                                <tr>
                                    <td>${r.id}</td>
                                    <td>${r.createdBy.name}</td>
                                    <td><fmt:formatDate value="${r.fromDate}" pattern="dd/MM/yyyy"/></td>
                                    <td><fmt:formatDate value="${r.toDate}" pattern="dd/MM/yyyy"/></td>
                                    <td>${r.reasonType.rname}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${r.status == 0}"><span class="status status-pending">Chờ duyệt</span></c:when>
                                            <c:when test="${r.status == 1}"><span class="status status-approved">Đã duyệt</span></c:when>
                                            <c:when test="${r.status == 2}"><span class="status status-rejected">Đã từ chối</span></c:when>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/request/detail?reqid=${r.id}">Xem</a>
                                    </td>
                                    <td><a href="${pageContext.request.contextPath}/request/approve?reqid=${r.id}">Duyệt đơn</a></td>
                                    <td><a href="${pageContext.request.contextPath}/request/history?reqid=${r.id}">Lịch sử</a></td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty requestScope.listRflDept}">
                                <tr><td colspan="8" style="text-align:center;color:#6c757d;">Không có đơn nào trong phòng ban.</td></tr>
                            </c:if>
                        </tbody>
                    </table>

                    <!-- PHÂN TRANG - TAB TEAM -->
                    <c:if test="${requestScope.totalPageDept > 1}">
                        <div class="pagination">
                            <c:forEach begin="1" end="${requestScope.totalPageDept}" var="i">
                                <a href="${pageContext.request.contextPath}/request/list?page=${i}&tab=team"
                                   class="${i == requestScope.pageIndexDept ? 'active' : ''}">${i}</a>
                            </c:forEach>
                        </div>
                    </c:if>
                </div>
            </c:if>
        </div>

        <%-- SCRIPT XỬ LÝ TAB --%>
        <script>
            function openTab(evt, tabName) {
                var i, tabcontent, tablinks;
                tabcontent = document.getElementsByClassName("tab-content");
                for (i = 0; i < tabcontent.length; i++) {
                    tabcontent[i].style.display = "none";
                    tabcontent[i].classList.remove("active");
                }
                tablinks = document.getElementsByClassName("tab-link");
                for (i = 0; i < tablinks.length; i++) {
                    tablinks[i].classList.remove("active");
                }
                document.getElementById(tabName).style.display = "block";
                document.getElementById(tabName).classList.add("active");
                evt.currentTarget.classList.add("active");
            }

            document.addEventListener("DOMContentLoaded", function () {
                const activeTab = "${requestScope.activeTab}";
                if (activeTab === 'team' && document.getElementById('tab_team')) {
                    document.getElementById('tab_team').click();
                } else {
                    document.getElementById('tab_my').click();
                }
            });
        </script>

    </body>
</html>