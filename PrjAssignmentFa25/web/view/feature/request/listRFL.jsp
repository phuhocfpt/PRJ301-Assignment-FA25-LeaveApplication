<%-- 
    Document   : listRFL
    Created on : Oct 26, 2025, 11:20:00 PM
    Author     : phuga
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Danh Sách Đơn Nghỉ Phép</title>
        <%-- Link đến file CSS chung --%>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>

        <%-- Nhúng thanh Nav Bar (Copy từ home.jsp) --%>
        <nav class="nav-bar">
            <div class="nav-brand">
                <a href="${pageContext.request.contextPath}/home">LeaveApp</a>
            </div>
            <div class="nav-menu">
                <a href="${pageContext.request.contextPath}/home">Trang Chủ</a>
                <a href="${pageContext.request.contextPath}/request/create">Tạo Đơn</a>
                <a href="${pageContext.request.contextPath}/request/list" class="active">Xem Đơn</a>

                <%-- Đọc Role từ Session (đã được lưu bởi LoginController) --%>
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

        <%-- Nội dung chính của trang List --%>
        <div class="content-area">
            <h1>Danh Sách Đơn Nghỉ Phép</h1>

            <div class="tab-container">
                <div id="tab_my" class="tab-link" onclick="openTab(event, 'myRequests')">Đơn của tôi</div>

                <c:if test="${requestScope.isManager}">
                    <div id="tab_team" class="tab-link" onclick="openTab(event, 'teamRequests')">Đơn của Team</div>
                </c:if>
            </div>

            <div id="myRequests" class="tab-content">
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
                        </tr>
                    </thead>
                    <tbody>
                        <%-- Lặp qua ${listRflEmp} (tên biến của bạn) --%>
                        <c:forEach var="r" items="${requestScope.listRflEmp}">
                            <tr>
                                <td>${r.id}</td>
                                <%-- Định dạng ngày tháng cho đẹp --%>
                                <td><fmt:formatDate value="${r.fromDate}" pattern="dd/MM/yyyy"/></td>
                        <td><fmt:formatDate value="${r.toDate}" pattern="dd/MM/yyyy"/></td>
                        <td>${r.reasonType.rname}</td>
                        <td>
                            <%-- Dùng c:choose để đổi số status ra chữ --%>
                            <c:choose>
                                <c:when test="${r.status == 0}">
                                    <span class="status status-pending">Chờ duyệt</span>
                                </c:when>
                                <c:when test="${r.status == 1}">
                                    <span class="status status-approved">Đã duyệt</span>
                                </c:when>
                                <c:when test="${r.status == 2}">
                                    <span class="status status-rejected">Đã từ chối</span>
                                </c:when>
                            </c:choose>
                        </td>
                        <td>
                            <%-- Hiển thị tên người duyệt (nếu có) --%>
                            ${not empty r.processedBy ? r.processedBy.ename : '---'}
                        </td>
                        <td>
                            <a href="#">Xem</a> <%-- Link đến trang chi tiết (nếu có) --%>
                        </td>
                        </tr>
                    </c:forEach>
                    <%-- Xử lý nếu danh sách rỗng --%>
                    <c:if test="${empty requestScope.listRflEmp}">
                        <tr>
                            <td colspan="7" style="text-align: center; color: #6c757d;">Bạn chưa tạo đơn nào.</td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>

                <c:if test="${requestScope.totalPageEmp > 1}">
                    <div class="pagination">
                        <%-- Lặp từ 1 đến tổng số trang của tab "my" --%>
                        <c:forEach begin="1" end="${requestScope.totalPageEmp}" var="i">
                            <%-- Thêm &tab=my để JS biết mở đúng tab --%>
                            <a href="list?page=${i}&tab=my" 
                               class="${(i == requestScope.pageIndex) ? 'active' : ''}">
                                ${i}
                            </a>
                        </c:forEach>
                    </div>
                </c:if>

            </div>

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
                                <th>Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            <%-- Lặp qua ${listRflDept} (tên biến của bạn) --%>
                            <c:forEach var="r" items="${requestScope.listRflDept}">
                                <tr>
                                    <td>${r.id}</td>
                                    <td>${r.createdBy.ename}</td> 
                                    <td><fmt:formatDate value="${r.fromDate}" pattern="dd/MM/yyyy"/></td>
                            <td><fmt:formatDate value="${r.toDate}" pattern="dd/MM/yyyy"/></td>
                            <td>${r.reasonType.rname}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${r.status == 0}">
                                        <span class="status status-pending">Chờ duyệt</span>
                                    </c:when>
                                    <c:when test="${r.status == 1}">
                                        <span class="status status-approved">Đã duyệt</span>
                                    </c:when>
                                    <c:when test="${r.status == 2}">
                                        <span class="status status-rejected">Đã từ chối</span>
                                    </c:when>
                                </c:choose>
                            </td>
                            <td>
                                <%-- Link đến trang /request/approve --%>
                                <a href="${pageContext.request.contextPath}/request/approve?reqid=${r.id}">Duyệt đơn</a>
                            </td>
                            </tr>
                        </c:forEach>
                        <%-- Xử lý nếu danh sách rỗng --%>
                        <c:if test="${empty requestScope.listRflDept}">
                            <tr>
                                <td colspan="7" style="text-align: center; color: #6c757d;">Không có đơn nào trong phòng ban của bạn.</td>
                            </tr>
                        </c:if>
                        </tbody>
                    </table>

                    <c:if test="${requestScope.totalPageDept > 1}">
                        <div class="pagination">
                            <%-- Lặp từ 1 đến tổng số trang của tab "team" --%>
                            <c:forEach begin="1" end="${requestScope.totalPageDept}" var="i">
                                <%-- Thêm &tab=team để JS biết mở đúng tab --%>
                                <a href="list?page=${i}&tab=team" 
                                   class="${(i == requestScope.pageIndex) ? 'active' : ''}">
                                    ${i}
                                </a>
                            </c:forEach>
                        </div>
                    </c:if>

                </div>
            </c:if>
        </div>

        <%-- JavaScript để xử lý chuyển Tab --%>
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

            // TỰ ĐỘNG MỞ ĐÚNG TAB KHI TẢI LẠI TRANG (VÍ DỤ KHI BẤM PHÂN TRANG)
            document.addEventListener("DOMContentLoaded", function () {
                // Đọc tab đang active từ Controller
                // ${activeTab} sẽ là 'my' hoặc 'team'
                const activeTab = "${requestScope.activeTab}";

                if (activeTab === 'team' && document.getElementById('tab_team')) {
                    // Tự động click vào tab "Đơn của Team"
                    document.getElementById('tab_team').click();
                } else {
                    // Mặc định, click vào tab "Đơn của tôi"
                    document.getElementById('tab_my').click();
                }
            });
        </script>
    </body>
</html>