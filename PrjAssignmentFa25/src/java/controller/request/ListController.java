/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.request;

import controller.iam.BaseRequiredAuthorizationController;
import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.RequestForLeave;
import model.iam.Role;
import model.iam.User;

/**
 *
 * @author phuga
 */
@WebServlet(urlPatterns = "/request/list")
public class ListController extends BaseRequiredAuthorizationController {

    private static final int PAGE_SIZE = 10;

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        processGet(req, resp, user);
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        try {
            String pageStr = req.getParameter("page");
            int pageIndex;

            try {
                pageIndex = (pageStr != null) ? Integer.parseInt(pageStr) : 1;
            } catch (NumberFormatException e) {
                pageIndex = 1; //ng dùng nhập sai định dạng trang => về trang 1
            }

            //Lấy current tab
            String activeTab = req.getParameter("tab");
            if (activeTab == null || activeTab.isEmpty()) {
                activeTab = "my";
            }

            RequestForLeaveDBContext rflContext = new RequestForLeaveDBContext();

            //lấy id emp và id did từ session của User
            int empId = user.getEmployee().getId();
            int deptId = user.getEmployee().getDepartment().getId();

            //check manager(cấp trên)
            boolean isManager = false;
            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                Role userRole = user.getRoles().get(0); //mỗi người dùng có 1 role chính => get(0) là lấy ra role

                if (userRole.getRlevel() < 3) { //0, 1, 2 là quản lý đổ lên
                    isManager = true;
                }
            }

            int totalPageEmp = 1;
            int totalPageDept = 1;

            //List đơn nvien đó tạo
            int totalRequestsEmp = rflContext.countListRflByEmpId(empId);
            //tổng trang
            totalPageEmp = (int) Math.ceil((double) totalRequestsEmp / PAGE_SIZE);

            if (activeTab.equals("my")) { //validate pageIndex nếu đang ở tab "my"
                if (pageIndex > totalPageEmp && totalPageEmp > 0) {
                    pageIndex = totalPageEmp;
                }
                if (pageIndex < 1) {
                    pageIndex = 1;
                }
            }

            ArrayList<RequestForLeave> listRflEmp = rflContext.listByUserId(empId, pageIndex, PAGE_SIZE);

            //Check role có phải cấp trên không
            if (isManager) {
                int totalRequestsDept = rflContext.countByDepartmentId(deptId);
                totalPageDept = (int) Math.ceil((double) totalRequestsDept / PAGE_SIZE);

                if (activeTab.equals("team")) { //validate pageIndex nếu đang ở tab "team"
                    if (pageIndex > totalPageDept && totalPageDept > 0) {
                        pageIndex = totalPageDept;
                    }
                    if (pageIndex < 1) {
                        pageIndex = 1;
                    }
                }

                ArrayList<RequestForLeave> listRflDept = rflContext.listByDeptsId(deptId, pageIndex, PAGE_SIZE);

                req.setAttribute("listRflDept", listRflDept); //list rfl
                req.setAttribute("totalPageDept", totalPageDept); //tổng số trang
            }

            //gửi dlieu của xem đơn bản thân nvien đó sang jsp
            req.setAttribute("listRflEmp", listRflEmp); //list rfl
            req.setAttribute("totalPageEmp", totalPageEmp); //tổng số trang

            //gửi các dt còn lại
            req.setAttribute("isManager", isManager);
            req.setAttribute("pageIndex", pageIndex);
            req.setAttribute("activeTab", activeTab);

            req.getRequestDispatcher("/view/feature/request/listRFL.jsp").forward(req, resp);
        } catch (SQLException ex) {
            Logger.getLogger(ListController.class.getName()).log(Level.SEVERE, "Lỗi khi tạo list đơn", ex);
            req.setAttribute("errorMessage", "Lỗi CSDL: " + ex.getMessage());
            req.getRequestDispatcher("/view/msg/msgLogin.jsp").forward(req, resp);
        }

    }

}
