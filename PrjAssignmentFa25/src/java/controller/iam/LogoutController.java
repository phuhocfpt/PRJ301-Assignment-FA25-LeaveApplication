/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.iam;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.iam.User;

/**
 *
 * @author phuga
 */
@WebServlet(urlPatterns = "/logout")
public class LogoutController extends BaseRequiredAuthenticationController{
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        // 1. Lấy session hiện tại (đã có từ đăng nhập)
        HttpSession session = req.getSession();
        
        // 2. Hủy bỏ session (xóa attribute "acc" và các attribute khác)
        session.invalidate();
        
        // 3. Chuyển hướng người dùng về trang đăng nhập
        resp.sendRedirect(req.getContextPath() + "/login");
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        doGet(req, resp, user); // Gọi lại hàm doGet ở trên để xử lý
    }
    

    
}
