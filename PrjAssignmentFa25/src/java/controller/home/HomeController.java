/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.home;

import controller.iam.BaseRequiredAuthorizationController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import model.iam.User;

/**
 *
 * @author phuga
 */
@WebServlet(urlPatterns = "/home")
public class HomeController extends BaseRequiredAuthorizationController{
    //tính năng home chỉ truy cập được khi mà đã đăng nhập => kế thừa authorization
    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        //xử lý dưới hàm get
        processGet(req, resp, user);
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        //gán đối tượng User vào req => jsp
        req.setAttribute("user", user);
        
        //forward tới trang jsp
        req.getRequestDispatcher("view/home/home.jsp").forward(req, resp);
    }

    
    
}
