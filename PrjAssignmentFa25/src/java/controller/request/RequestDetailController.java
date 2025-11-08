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
import java.util.logging.Level;
import java.util.logging.Logger;
import model.RequestForLeave;
import model.iam.User;

/**
 *
 * @author phuga
 *
 *
 * Servlet này dùng để cho tính năng Xem ở phần xem đơn(Detail)
 */
@WebServlet(urlPatterns = "/request/detail")
public class RequestDetailController extends BaseRequiredAuthorizationController {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {

    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        String sid = req.getParameter("reqid");
        if (sid == null) {
            resp.sendRedirect(req.getContextPath() + "/request/list");
            return;
        }
        int reqid;
        try {
            reqid = Integer.parseInt(sid);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/request/list");
            return;
        }

        try {

            RequestForLeaveDBContext dao = new RequestForLeaveDBContext();
            RequestForLeave r = dao.getDetailBasic(reqid);
            try {
                r = dao.getDetailByReqId(reqid); // viết hàm này ở DBContext (mục 3)
            } catch (SQLException ex) {
                Logger.getLogger(RequestDetailController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (r == null) {
                req.setAttribute("errorMessage", "Không tìm thấy đơn.");
                req.getRequestDispatcher("/view/feature/request/detailRFL.jsp").forward(req, resp);
                return;
            }
            req.setAttribute("r", r);
            req.getRequestDispatcher("/view/feature/request/detailRFL.jsp").forward(req, resp);
        } catch (SQLException ex) {
            req.setAttribute("errorMessage", "Lỗi CSDL: " + ex.getMessage()); //send lỗi
            req.getRequestDispatcher("/view/feature/request/detailRFL.jsp").forward(req, resp);
        }
    }

}
