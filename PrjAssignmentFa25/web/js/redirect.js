/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */


/*
 * File JS này nhận dữ liệu từ `accessDenied.jsp` (thông qua data attributes)
 * để thực hiện đếm ngược và chuyển hướng người dùng về trang login.
 */

// Đặt code trong một hàm tự gọi (IIFE) để tránh làm ô nhiễm global scope
(function() {
    
    // Lấy element <div id="redirectInfo"> từ JSP để đọc dữ liệu
    const redirectInfo = document.getElementById('redirectInfo');
    // Lấy element <span id="timer"> từ JSP để cập nhật số giây
    const timerElement = document.getElementById('timer');

    // Kiểm tra xem các element có tồn tại không (phòng lỗi)
    if (!redirectInfo || !timerElement) {
        console.error("Countdown elements not found on the page.");
        return;
    }

    // Đọc URL chuyển hướng (data-redirect-url) từ JSP
    const loginUrl = redirectInfo.getAttribute('data-redirect-url');
    // Đọc số giây bắt đầu (data-countdown-start) từ JSP
    let countdown = parseInt(redirectInfo.getAttribute('data-countdown-start'), 10);

    // Cập nhật số giây ban đầu lên màn hình
    timerElement.innerText = countdown;

    // Bắt đầu vòng lặp đếm ngược (chạy mỗi 1 giây)
    const interval = setInterval(() => {
        countdown--; // Giảm 1 giây
        timerElement.innerText = countdown; // Cập nhật số giây lên màn hình

        // Nếu đếm về 0
        if (countdown <= 0) {
            clearInterval(interval); // Dừng đếm
            window.location.href = loginUrl; // Chuyển hướng về trang login
        }
    }, 1000); // 1000 ms = 1 giây

})();
