/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.iam;

import dal.iam.RoleDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.iam.Feature;
import model.iam.Role;
import model.iam.User;

/**
 *
 * @author phuga
 */
public abstract class BaseRequiredAuthorizationController extends BaseRequiredAuthenticationController {

    private boolean isAuthorized(HttpServletRequest req, User user) throws SQLException {
        //Check người dùng (user) có role hay role rỗng không
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            RoleDBContext rdc = new RoleDBContext();

            //Lấy list role + feature dựa trên user id
            user.setRoles(rdc.getByUserId(user.getId()));

            //Update role của user trong session đó
            req.getSession().setAttribute("acc", user);
        }

        //lấy url ng dùng đang truy cập
        String url = req.getServletPath();

        //check url xem có trong feature được làm của user đó không
        //lặp role của user để check
        for (Role role : user.getRoles()) {
            //với mỗi role => lặp qua các feature của role đó
            if (role.getFeatures() != null) {
                for (Feature feature : role.getFeatures()) {
                    // so sánh url từ contextPath kia với các url trong feature của role đó
                    if (feature.getUrl() != null && feature.getUrl().equals(url)) {
                        //tìm thấy => true => xác thực
                        return true;
                    }
                }

            }
        }

        return false; //không tìm thấy url đó có trong feature của role của user đó
    }

    //tương tự như bên class BaseRequiredAuthenticationController có những abstract method
    // để xử lý các tính năng riêng của trang đó
    protected abstract void processPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException;

    protected abstract void processGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException;

    //override 2 method abstract của lớp cha
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        try {
            if (isAuthorized(req, user)) { // Kiểm tra Vòng 2
                // Nếu có quyền, gọi hàm processPost của lớp Con để xử lý nghiệp vụ
                processPost(req, resp, user);
            } else {
                // Nếu không có quyền, báo lỗi và chuyển đến trang báo lỗi (8 giây)
                req.setAttribute("errorLoginMsg", "Access Denied! You do not have permission to access this page.");
                req.getRequestDispatcher("view/msg/msgLogin.jsp").forward(req, resp);
            }
        } catch (SQLException ex) {
            //RoleDBContext.getByUserID() fail
            Logger.getLogger(BaseRequiredAuthorizationController.class.getName()).log(Level.SEVERE, null, ex);
            req.setAttribute("errorLoginMsg", "Lỗi CSDL khi kiểm tra quyền truy cập.");
            req.getRequestDispatcher("view/msg/msgLogin.jsp").forward(req, resp);
        }
    }

    /**
     * @param req
     * @param resp
     * @param user
     * @throws jakarta.servlet.ServletException
     * @throws java.io.IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        try {
            if (isAuthorized(req, user)) { // Kiểm tra Vòng 2
                // Nếu có quyền, gọi hàm processGet của lớp Con để xử lý nghiệp vụ
                processGet(req, resp, user);
            } else {
                // Nếu không có quyền, báo lỗi và chuyển đến trang báo lỗi (8 giây)
                req.setAttribute("errorLoginMsg", "Access Denied! You do not have permission to access this page.");
                req.getRequestDispatcher("view/msg/msgLogin.jsp").forward(req, resp);
            }
        } catch (SQLException ex) {
            //RoleDBContext.getByUserId() fail
            Logger.getLogger(BaseRequiredAuthorizationController.class.getName()).log(Level.SEVERE, null, ex);
            req.setAttribute("errorLoginMsg", "Lỗi DB khi kiểm tra quyền truy cập.");
            req.getRequestDispatcher("view/msg/msgLogin.jsp").forward(req, resp);
        }
    }

}
