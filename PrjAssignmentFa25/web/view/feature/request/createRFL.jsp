<%-- 
    Document   : createRFL
    Created on : Nov 3, 2025, 8:55:48 AM
    Author     : phuga
--%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Request For Leave</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    </head>
    <body>
        <nav class="nav-bar">
            <div class="nav-brand">
                <a href="${pageContext.request.contextPath}/home">LeaveApp</a>
            </div>
            <div class="nav-menu">
                <a href="${pageContext.request.contextPath}/home">Trang Chủ</a>
                <a href="${pageContext.request.contextPath}/request/create" class="active">Tạo Đơn</a>
                <a href="${pageContext.request.contextPath}/request/list">Xem Đơn</a>

                <%-- 
                    Lưu ý: Chúng ta đang ở trang /request/create, đối tượng 'user'
                    (với 'roles') có thể không được HomeController gửi sang.
                    Để an toàn, chúng ta nên đọc trực tiếp từ Session.
                --%>
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

        <%-- Nội dung chính của trang --%>
        <div class="content-area">
            <div class="form-container">
                <h1>Tạo Đơn Xin Nghỉ Phép Mới</h1>

                <%-- Form gửi dữ liệu đến CreateRequestController (POST) --%>
                <form id="createRequestForm" action="${pageContext.request.contextPath}/request/create" method="POST">

                    <%-- Hiển thị thông báo lỗi nếu POST thất bại --%>
                    <c:if test="${not empty requestScope.errorMessage}">
                        <div class="login-message error">
                            ${requestScope.errorMessage}
                        </div>
                    </c:if>

                    <%-- Chia form làm 2 cột --%>
                    <div class="form-grid">

                        <%-- Cột 1 --%>
                    <div class="form-group">
                        <label for="fromdate">Từ Ngày:</label>
                        <%-- SỬA LỖI: Đổi type="date" thành type="text" và thêm placeholder --%>
                        <input type="text" id="fromdate" name="fromdate" placeholder="dd/MM/yyyy" required>
                    </div>
                    
                    <%-- Cột 2 --%>
                    <div class="form-group">
                        <label for="todate">Đến Ngày:</label>
                        <%-- SỬA LỖI: Đổi type="date" thành type="text" và thêm placeholder --%>
                        <input type="text" id="todate" name="todate" placeholder="dd/MM/yyyy" required>
                    </div>

                        <%-- Hàng 2 (Full cột) --%>
                        <div class="form-group form-group-full">
                            <label for="reasontype">Loại Lý Do:</label>
                            <select id="reasontype" name="reasontype" required>
                                <option value="">-- Vui lòng chọn --</option>
                                <%-- 
                                    Dùng JSTL để lặp qua ArrayList 'reasonTypes' 
                                    mà Controller (processGet) đã gửi sang.
                                --%>
                                <c:forEach var="reason" items="${requestScope.reasonTypes}">
                                    <%-- 
                                        Quan trọng: Thêm 'data-requires-details'
                                        để JavaScript biết khi nào cần hiện ô chi tiết
                                    --%>
                                    <option value="${reason.id}" data-requires-details="${reason.reasonOthers}">
                                        ${reason.rname}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <%-- Hàng 3 (Full cột) - Ẩn ban đầu --%>
                        <div class="form-group form-group-full" id="reasonDetailsGroup">
                            <label for="reasondetails">Lý Do Chi Tiết (Bắt buộc):</label>
                            <textarea id="reasondetails" name="reasondetails"></textarea>
                        </div>

                    </div> <%-- Hết form-grid --%>

                    <button type="submit" class="form-submit-button">Gửi Đơn</button>

                </form>
            </div>
        </div>

        <%-- JavaScript để xử lý ẩn/hiện ô "Lý do chi tiết" --%>
        <script type="text/javascript">
            // Chờ tài liệu tải xong
            document.addEventListener("DOMContentLoaded", function () {

                // Lấy các element
                const reasonSelect = document.getElementById("reasontype");
                const detailsGroup = document.getElementById("reasonDetailsGroup");
                const detailsText = document.getElementById("reasondetails");

                // Lắng nghe sự kiện 'change' (thay đổi) trên ô dropdown
                reasonSelect.addEventListener("change", function () {
                    // Lấy <option> đang được chọn
                    const selectedOption = this.options[this.selectedIndex];

                    // Lấy giá trị 'data-requires-details' (true/false)
                    const requiresDetails = selectedOption.getAttribute('data-requires-details');

                    if (requiresDetails === 'true') {
                        // Nếu là "Lý do khác", hiển thị ô chi tiết và đặt là bắt buộc
                        detailsGroup.style.display = 'block';
                        detailsText.required = true;
                    } else {
                        // Nếu là lý do thường, ẩn đi và bỏ bắt buộc
                        detailsGroup.style.display = 'none';
                        detailsText.required = false;
                        detailsText.value = ''; // Xóa nội dung cũ
                    }
                });
            });
        </script>
    </body>
</html>
