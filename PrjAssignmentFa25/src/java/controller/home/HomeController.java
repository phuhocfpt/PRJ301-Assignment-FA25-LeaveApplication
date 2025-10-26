/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.home;

import controller.iam.BaseRequiredAuthenticationController;
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
public class HomeController extends BaseRequiredAuthenticationController{

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        //gán giá trị cho user bên BaseRequiredAuthenticationController sang đây set attribute để chuyển về home.jsp
        req.setAttribute("user", user);
        
        //chuyển tới home.jsp
        req.getRequestDispatcher("view/home/home.jsp").forward(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        
        doGet(req, resp, user);
    }
    
}
