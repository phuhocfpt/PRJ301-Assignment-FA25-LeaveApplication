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
import model.RequestForLeave;
import model.iam.User;

/**
 *
 * @author phuga
 */
@WebServlet(urlPatterns = "/request/history")
public class RequestHistoryController extends BaseRequiredAuthorizationController {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        processGet(req, resp, user);
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        String sid = req.getParameter("reqid");
        if (sid == null || sid.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/request/list");
            return;
        }
        int reqid = Integer.parseInt(sid);

        try {
            RequestForLeaveDBContext dao = new RequestForLeaveDBContext();
            RequestForLeave r = dao.getDetailBasic(reqid); 
            ArrayList<java.util.Map<String, Object>> historyLists = dao.getStatusHistoryMap(reqid); // <— đổi sang Map


            req.setAttribute("r", r);
            req.setAttribute("historyLists", historyLists);
            req.setAttribute("reqid", reqid);

            req.getRequestDispatcher("/view/feature/request/historyRFL.jsp").forward(req, resp);

        } catch (SQLException ex) {
            req.setAttribute("errorMessage", "Lỗi CSDL: " + ex.getMessage());
            req.getRequestDispatcher("/view/feature/request/historyRFL.jsp").forward(req, resp);
        }
    }

}
