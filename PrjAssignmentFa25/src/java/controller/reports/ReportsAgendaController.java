/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.reports;

import controller.iam.BaseRequiredAuthorizationController;
import dal.RequestForLeaveDBContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import model.Employee;
import model.iam.User;

/**
 *
 * @author phuga
 */
@WebServlet(urlPatterns = "/reports/agenda")
public class ReportsAgendaController extends BaseRequiredAuthorizationController {

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        try {
            // --- Lấy khoảng ngày (có thể chọn bằng query param)
            LocalDate today = LocalDate.now();
            LocalDate start = Optional.ofNullable(req.getParameter("from"))
                    .map(LocalDate::parse)
                    .orElse(today);
            LocalDate end = Optional.ofNullable(req.getParameter("to"))
                    .map(LocalDate::parse)
                    .orElse(start.plusDays(8)); // mặc định 9 ngày

            // Tạo danh sách ngày để hiển thị cột
            ArrayList<LocalDate> days = new ArrayList<>();
            for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
                days.add(d);
            }

            RequestForLeaveDBContext db = new RequestForLeaveDBContext();

            ArrayList<Employee> team;
            ArrayList<RequestForLeaveDBContext.LeaveSpan> spans;

            
            String userRole = user.getRoles().get(0).getRname(); // Giả định hàm này trả về "ADMIN" hoặc "MANAGER"

            if (userRole != null && userRole.equalsIgnoreCase("ADMIN")) {
                //admin take all leave
                team = db.listAllEmployees();
                spans = db.listApprovedSpansOfAll(Date.valueOf(start), Date.valueOf(end));

            } else {
                // Các role < admin và > emp thì xem
                int managerEid = user.getEmployee().getId();
                team = db.listTeamByManager(managerEid);
                spans = db.listApprovedSpansOfTeam(managerEid, Date.valueOf(start), Date.valueOf(end));
            }


            // --- Xây dựng ma trận nghỉ phép: eid -> boolean[days]
            // (Phần này giữ nguyên, không thay đổi)
            Map<Integer, boolean[]> leaveMatrix = new HashMap<>();
            for (Employee e : team) {
                leaveMatrix.put(e.getId(), new boolean[days.size()]);
            }

            for (RequestForLeaveDBContext.LeaveSpan s : spans) {
                LocalDate f = s.from.toLocalDate();
                LocalDate t = s.to.toLocalDate();
                // Cắt trong phạm vi
                if (f.isBefore(start)) {
                    f = start;
                }
                if (t.isAfter(end)) {
                    t = end;
                }

                boolean[] row = leaveMatrix.get(s.eid);
                if (row == null) {
                    continue; // Bỏ qua nếu nhân viên này không có trong danh sách (ví dụ: Admin xem all nhưng đơn của chính Admin)
                }

                for (LocalDate d = f; !d.isAfter(t); d = d.plusDays(1)) {
                    int idx = (int) (d.toEpochDay() - start.toEpochDay());
                    if (0 <= idx && idx < row.length) {
                        row[idx] = true;
                    }
                }
            }

            // set attribute to jsp
            req.setAttribute("days", days);
            req.setAttribute("team", team);
            req.setAttribute("leaveMatrix", leaveMatrix);
            req.setAttribute("start", start);
            req.setAttribute("end", end);

            req.getRequestDispatcher("/view/feature/reports/agenda.jsp").forward(req, resp);

        } catch (Exception ex) {
            req.setAttribute("errorMessage", "Lỗi khi tải Agenda: " + ex.getMessage());
            req.getRequestDispatcher("/view/feature/reports/agenda.jsp").forward(req, resp);
        }
    }

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        processGet(req, resp, user);
    }

}
