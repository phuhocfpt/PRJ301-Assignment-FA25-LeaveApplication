/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
import model.iam.User;

/**
 *
 * @author phuga
 */
@WebServlet(urlPatterns = "/request/list")
public class ListController extends BaseRequiredAuthorizationController {

    private static final int PAGE_SIZE = 5; // Adjust as needed

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        //Post không dùng cho list
        //gọi get
        processGet(req, resp, user);
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        try {
            RequestForLeaveDBContext rflDB = new RequestForLeaveDBContext();
            int pageSize = 5;

            // Lấy page và tab từ request
            String pageStr = req.getParameter("page");
            String tab = req.getParameter("tab"); // "my" hoặc "team"
            int page;
            if (pageStr != null && !pageStr.isEmpty()) {
                page = Integer.parseInt(pageStr);
            } else {
                page = 1;
            }
            
            boolean isAdmin = user.ha
            int rlevel = user.getRoles().get(0).getRlevel();
            boolean isManager = rlevel < 3;

            // List của Emplyee (default)
            int eid = user.getEmployee().getId();
            ArrayList<RequestForLeave> listRflEmp = rflDB.listByUserId(eid, page, pageSize);
            int totalEmp = rflDB.countListRflByEmpId(eid);
            int totalPageEmp = (int) Math.ceil((double) totalEmp / pageSize);

            // List của manager (role id < 3)
            ArrayList<RequestForLeave> listRflDept = null;
            int totalPageDept = 0;
            int pageIndexDept = 1;
            if (isManager) {
                int did = user.getEmployee().getDepartment().getId();
                if ("team".equals(tab)) {
                    pageIndexDept = page;
                    listRflDept = rflDB.listByDeptsId(did, page, pageSize);
                } else {
                    listRflDept = rflDB.listByDeptsId(did, 1, pageSize); // trang 1 mặc định
                }
                int totalDept = rflDB.countByDepartmentId(did);
                totalPageDept = (int) Math.ceil((double) totalDept / pageSize);
            }

            // Set attribute
            req.setAttribute("listRflEmp", listRflEmp);
            req.setAttribute("listRflDept", listRflDept);
            req.setAttribute("isManager", isManager);

            req.setAttribute("pageIndexEmp", "team".equals(tab) ? 1 : page); // nếu đang ở team → my về trang 1
            req.setAttribute("pageIndexDept", pageIndexDept);

            req.setAttribute("totalPageEmp", totalPageEmp);
            req.setAttribute("totalPageDept", totalPageDept);

            req.setAttribute("activeTab", tab != null ? tab : "my");

            req.getRequestDispatcher("/view/feature/request/listRFL.jsp").forward(req, resp);

        } catch (SQLException ex) {
            Logger.getLogger(ListController.class.getName()).log(Level.SEVERE, null, ex);
            req.setAttribute("errorMessage", "Lỗi kết nối CSDL: " + ex.getMessage());
            req.getRequestDispatcher("/view/feature/request/listRFL.jsp").forward(req, resp);
        }
    }
}
