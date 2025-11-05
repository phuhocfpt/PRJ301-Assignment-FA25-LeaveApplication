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
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
        //DateTimeFormatter mình định dạng là dd/MM/yyyy nhưng khi mà tạo đơn
        //và gửi về bên sql thì nó nhận dạng yyyy/MM/dd nên cần tại đối tượng này
        //với LocalDate và khi set gtri vào rfl cần phải dùng java.sql để nó về định
        //dạng đúng như sql yyyy/MM/dd (dòng 59,60)
        DateTimeFormatter formatdate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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

            //Chuyển từ String -> obj localdate 
            LocalDate localFromDate = LocalDate.parse(fromDateStr, formatdate);
            LocalDate localToDate = LocalDate.parse(toDateStr, formatdate);

            //add local date đó vào sql 
            rfl.setFromDate(java.sql.Date.valueOf(localFromDate)); //Gán fromDate vào đơn xin nghỉ
            rfl.setToDate(java.sql.Date.valueOf(localToDate)); // gán toDate vào đơn xin nghỉ

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

        } catch (DateTimeParseException | NumberFormatException ex) { try {
            //bắt thêm lỗi nhập date sai, parseInt lỗi thì đều trả về form tạo đơn
            Logger.getLogger(CreateController.class.getName()).log(Level.WARNING, null, ex);
            req.setAttribute("errorMessage", "Định dạng dữ liệu không hợp lệ.");
            loadReasonsAndForward(req, resp); // Gọi hàm helper
            } catch (SQLException ex1) {
                Logger.getLogger(CreateController.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }

    }

    //hàm helper này khắc phục ở trên khi không catch Exception nói chung nó giúp tải lại reasonTypes(downlist) về form
    //POST cần vi khi gửi đi lỗi thì processGet không run
    // => Tải lại ReasonType và setAtt vào request và trả về trang nhập form
    private void loadReasonsAndForward(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        ReasonTypeDBContext reasonDAO = new ReasonTypeDBContext();
        req.setAttribute("reasonTypes", reasonDAO.listsReason());
        req.getRequestDispatcher("/view/feature/request/createRFL.jsp").forward(req, resp);
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user) throws ServletException, IOException {
        try {
            //Hiển thị form để nhập
            
            //trả về reason để tạo đơn
            ReasonTypeDBContext reasons = new ReasonTypeDBContext();
            
            ArrayList<ReasonType> reasonTypes = reasons.listsReason();
            
            //truyền list về
            req.setAttribute("reasonTypes", reasonTypes);
            
            req.getRequestDispatcher("/view/feature/request/createRFL.jsp").forward(req, resp);
        } catch (SQLException ex) {
            Logger.getLogger(CreateController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
