/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.request;

import controller.iam.BaseRequiredAuthorizationController;
import dal.ReasonTypeDBContext;
import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.ReasonType;
import model.RequestForLeave;
import model.iam.User;

/**
 *
 * @author phuga
 */
@WebServlet(urlPatterns = "/request/create")
public class CreateController extends BaseRequiredAuthorizationController {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        try {
            //Xử lí form đó
            //Take data of insert query
            // Extends Authorization => get createdBy, createdTime, did of class RequestForLeave
            //Just take fromDate, toDate, Reason - Orther reason

            String fromDateStr = req.getParameter("fromdate");
            String toDateStr = req.getParameter("todate");
            int reasonTypeId = Integer.parseInt(req.getParameter("reasontype"));
            String reasonOthers = req.getParameter("reasondetails");

            RequestForLeave rfl = new RequestForLeave();
            rfl.setFromDate(Date.valueOf(fromDateStr)); //Gán fromDate vào đơn xin nghỉ
            rfl.setToDate(Date.valueOf(toDateStr)); // gán toDate vào đơn xin nghỉ

            //get in4 user(empName, did, ...) on session
            rfl.setCreatedBy(user.getEmployee()); //empName, ...
            rfl.setDepartment(user.getEmployee().getDepartment()); // did

            //take id reasonType
            // 1. SICK, 2.ANNUAL, 3.OTHERS -> Need descripton
            ReasonType rt = new ReasonType();
            rt.setId(reasonTypeId);
            rfl.setReasonType(rt);

            rfl.setReasonOthers(reasonOthers);

            RequestForLeaveDBContext requestDBContext = new RequestForLeaveDBContext();
            int newReqId = requestDBContext.createRFL(rfl); //hàm createRFL trả về id của đơn đó

            if (newReqId != -1) { //done
                resp.sendRedirect(req.getContextPath() + "/home"); //trả về home
            } else { //fail
                //báo lỗi
                req.setAttribute("errorMessage", "Tạo đơn thất bại. Vui lòng thử lại.");

                //quay lại form tạo đơn xin nghỉ
                ReasonTypeDBContext reasonDAO = new ReasonTypeDBContext();
                req.setAttribute("reasonTypes", reasonDAO.listsReason()); //trả về list reason (dropdown) 
                //POST cần vi khi gửi đi lỗi thì processGet không run
                // => Tải lại ReasonType và setAtt vào request
                req.getRequestDispatcher("view/feature/request/createRFL.jsp").forward(req, resp);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CreateController.class.getName()).log(Level.SEVERE, null, ex);
            resp.getWriter().println("Lỗi DB. Lỗi: " + ex);
            
        } catch (Exception e){ //bắt thêm lỗi nhập date sai, parseInt lỗi thì đều trả về form tạo đơn
            Logger.getLogger(CreateController.class.getName()).log(Level.SEVERE, null, e);
            req.setAttribute("errorMessage", "Định dạng dữ liệu không hợp lệ.");
            ReasonTypeDBContext reasonDAO = new ReasonTypeDBContext();
            req.setAttribute("reasonTypes", reasonDAO.listsReason());
            req.getRequestDispatcher("/view/feature/request/createRFL.jsp").forward(req, resp);
        }

    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        //Hiển thị form để nhập

        //trả về reason để tạo đơn
        ReasonTypeDBContext reasons = new ReasonTypeDBContext();

        ArrayList<ReasonType> reasonTypes = reasons.listsReason();

        //truyền list về
        req.setAttribute("reasonTypes", reasonTypes);

        req.getRequestDispatcher("/view/feature/request/createRFL.jsp").forward(req, resp);
    }

}
