/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.iam;

import dal.iam.UserDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.iam.User;
import jakarta.servlet.http.HttpSession;
/**
 *
 * @author phuga
 */
@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            
            UserDBContext ud = new UserDBContext();
            User u = ud.get(username, password);
            
            if(u!=null){
                HttpSession session = req.getSession();
                
                // "acc" này phải khớp với "acc"
                // trong BaseRequiredAuthenticationController
                session.setAttribute("acc", u);
                
                // Đăng nhập thành công, chuyển hướng về trang chủ
                resp.sendRedirect("home");
            } else {
                // Đăng nhập thất bại, đặt thông báo lỗi
                // "errorloginMsg" này phải khớp với "errorloginMsg"
                // trong login.jsp
                req.setAttribute("errorloginMsg", "Login Failed! Wrong username or password.");
                
                // Forward (trả về) lại trang login.jsp
                req.getRequestDispatcher("view/auth/login.jsp").forward(req, resp);
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //default là doGet chuyển hướng tới trang login.jsp để đăng nhập
        //trong quá trình đăng nhập xử lí bằng doPost bên trên
        req.getRequestDispatcher("/view/auth/login.jsp").forward(req, resp);
    }
    
}
