/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.iam;

import dal.iam.UserDBContext;
import jakarta.servlet.ServletException;
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
                session.setAttribute("acc", u);
                
                //print successfully if match account
                req.setAttribute("msgLogin", "Login Successful!");
            } else {
                req.setAttribute("msgLogin", "Login Failed!");
            }
            
            //sau khi xác nhận đăng nhập in ra thông báo => Chuyển về trang jsp thông báo
            req.getRequestDispatcher("/view/auth/mesageLogin.jsp").forward(req, resp);
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
