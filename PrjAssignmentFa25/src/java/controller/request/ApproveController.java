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
import model.iam.User;
import model.RequestForLeave;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author phuga
 */
@WebServlet(urlPatterns = "/request/approve")
public class ApproveController extends BaseRequiredAuthorizationController {

    private static final int PAGE_SIZE = 5; // phân trang hộp thư chờ duyệt

    // [THÊM/H1] Gom logic kiểm tra quyền xử lý 1 đơn vào 1 hàm (giữ rule cũ: Manager chỉ duyệt đơn của cấp dưới trực tiếp; Admin duyệt tất cả)
    private boolean canProcessThis(User user, RequestForLeave r) {
        int rlevel = user.getRoles().get(0).getRlevel();
        boolean isManager = (rlevel < 3);
        boolean isAdmin = (rlevel == 0); // GIỮ ĐÚNG LOGIC CŨ CỦA BẠN Ở CONTROLLER NÀY

        if (isAdmin) {
            return true; // Admin thấy/duyệt tất cả (Hướng 1)
        }
        if (!isManager) {
            return false;
        }

        // Manager: chỉ được duyệt đơn của cấp dưới trực tiếp (giữ nguyên rule cũ)
        Integer creatorMgr = (r.getCreatedBy() != null) ? r.getCreatedBy().getManagerId() : null;
        Integer me = (user.getEmployee() != null) ? user.getEmployee().getId() : null;
        return (creatorMgr != null && me != null && creatorMgr.intValue() == me.intValue());
    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        String reqidStr = req.getParameter("reqid");
        String action = req.getParameter("action"); // approve | reject
        String note = req.getParameter("note");

        try {
            RequestForLeaveDBContext db = new RequestForLeaveDBContext();

            int rlevel = user.getRoles().get(0).getRlevel();
            boolean isManager = (rlevel < 3);
            boolean isAdmin = (rlevel == 0);
            if (!isManager && !isAdmin) {
                req.setAttribute("errorMessage", "Bạn không có quyền xét duyệt.");
                processGet(req, resp, user);
                return;
            }

            int reqid = Integer.parseInt(reqidStr);
            RequestForLeave r = db.getDetailByReqId(reqid);
            if (r == null) {
                req.setAttribute("errorMessage", "Không tìm thấy đơn.");
                processGet(req, resp, user);
                return;
            }
//            if (r.getStatus() != 0) {
//                req.setAttribute("errorMessage", "Đơn này đã được xử lý trước đó.");
//                processGet(req, resp, user);
//                return;
//            }  // vì đã cho override đơn nên bỏ hàm này
            int newStatus = "approve".equalsIgnoreCase(action) ? 1 : 2;

            // [SỬA/H1] Dùng hàm override trong DBContext (allowOverride = true) theo Hướng 1
            boolean ok = db.processRequest(
                    reqid,
                    user.getEmployee().getId(),
                    newStatus,
                    note,
                    true // Hướng 1: CHO PHÉP OVERRIDE bất kỳ trạng thái nào
            );

            if (!ok) {
                req.setAttribute("errorMessage", "Không thể cập nhật trạng thái (đơn có thể không tồn tại).");
                processGet(req, resp, user);
                return;
            }

            // quay lại form chi tiết sau khi xử lý
            resp.sendRedirect(req.getContextPath() + "/request/detail?reqid=" + reqid);
        } catch (Exception ex) {
            Logger.getLogger(ApproveController.class.getName()).log(Level.SEVERE, null, ex);
            req.setAttribute("errorMessage", "Lỗi xử lý: " + ex.getMessage());
            processGet(req, resp, user);
        }
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        try {
            RequestForLeaveDBContext db = new RequestForLeaveDBContext();

            // phân quyền theo rlevel
            int rlevel = user.getRoles().get(0).getRlevel();
            boolean isManager = (rlevel < 3);
            boolean isAdmin = (rlevel == 0);

            // nếu không phải manager/admin thì đá về list
            if (!isManager && !isAdmin) {
                req.setAttribute("errorMessage", "Bạn không có quyền xét duyệt.");
                req.getRequestDispatcher("/view/feature/request/listRFL.jsp").forward(req, resp);
                return;
            }

            String reqidStr = req.getParameter("reqid");
            if (reqidStr != null && !reqidStr.isEmpty()) {
                // Mở trang form approve 1 đơn
                int reqid = Integer.parseInt(reqidStr);
                RequestForLeave r = db.getDetailByReqId(reqid);
                if (r == null) {
                    req.setAttribute("errorMessage", "Không tìm thấy đơn.");
                } else {
                    // kiểm tra phạm vi duyệt (admin thấy hết; manager chỉ được duyệt đơn của cấp dưới trực tiếp)
                    if (!canProcessThis(user, r)) {
                        req.setAttribute("errorMessage", "Bạn không có quyền duyệt đơn này.");
                    }

                    req.setAttribute("r", r);
                }
                req.getRequestDispatcher("/view/feature/request/approveRFL.jsp").forward(req, resp);
                return;
            }

            // Không có reqid -> hiển thị inbox chờ duyệt
            int page = 1;
            String p = req.getParameter("page");
            if (p != null && !p.isEmpty()) {
                page = Integer.parseInt(p);
            }

            ArrayList<RequestForLeave> inbox;
            int total, totalPage;

            if (isAdmin) {
                inbox = db.listPendingAll(page, PAGE_SIZE);
                total = db.countPendingAll();
            } else {
                int managerEid = user.getEmployee().getId();
                inbox = db.listPendingByManager(managerEid, page, PAGE_SIZE);
                total = db.countPendingByManager(managerEid);
            }
            totalPage = (int) Math.ceil((double) total / PAGE_SIZE);

            req.setAttribute("inbox", inbox);
            req.setAttribute("pageIndex", page);
            req.setAttribute("totalPage", totalPage);
            req.getRequestDispatcher("/view/feature/request/approveInbox.jsp").forward(req, resp);

        } catch (SQLException ex) {
            Logger.getLogger(ApproveController.class.getName()).log(Level.SEVERE, null, ex);
            req.setAttribute("errorMessage", "Lỗi CSDL: " + ex.getMessage());
            req.getRequestDispatcher("/view/feature/request/approveInbox.jsp").forward(req, resp);
        }
    }

}
