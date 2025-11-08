/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.iam;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.iam.User;

/**
 *
 * @author phuga
 */
public abstract class BaseRequiredAuthenticationController extends HttpServlet {
    //check xem user đăng nhập chưa
    //acc được setAttribute bên LoginController

    private boolean isAuthenticated(HttpServletRequest req) {
        User user = (User) req.getSession().getAttribute("acc");
        return user != null;
    }

    //để các sub servlet phải overrides vào các method này
    protected abstract void doPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException;

    protected abstract void doGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException;

    //2 hàm doPost và doGet này sẽ chạy first trước 2 override bên trên
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (isAuthenticated(req)) {
            //FIX lỗi return về sau khi logout 
            // => không lưu cache
            resp.setHeader("Cache-Control", "no-cache, no-store, must-relogin");
            resp.setHeader("Pragma", "no-cache");
            resp.setDateHeader("Expires", 0);

            //exec , autheticate -->user
            //nếu đăng nhập done => Lấy user => Thực hiện hàm doPost ở trên override
            User user = (User) req.getSession().getAttribute("acc");
            doPost(req, resp, user);
        } else {
            //chưa đăng nhập => set attribute 1 errorLoginMsg và trả về 1 trang display lỗi
            req.setAttribute("errorLoginMsg", "Access Denied! You must login first!");

            // Forward đến trang JSP báo lỗi
            req.getRequestDispatcher("/view/msg/msgLogin.jsp").forward(req, resp);

        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (isAuthenticated(req)) {
            //FIX lỗi return về sau khi logout 
            // => không lưu cache
            resp.setHeader("Cache-Control", "no-cache, no-store, must-relogin");
            resp.setHeader("Pragma", "no-cache");
            resp.setDateHeader("Expires", 0);

            
            //Nếu đăng nhập => Thực hiện hàm doGet của sub servlet
            User user = (User) req.getSession().getAttribute("acc");
            doGet(req, resp, user);
        } else {
            //chưa đăng nhập => set attribute 1 errorLoginMsg và trả về 1 trang display lỗi
            req.setAttribute("errorLoginMsg", "Access Denied! You must login first!");
            // Forward đến trang JSP báo lỗi
            req.getRequestDispatcher("/view/msg/msgLogin.jsp").forward(req, resp);
        }
    }
}
