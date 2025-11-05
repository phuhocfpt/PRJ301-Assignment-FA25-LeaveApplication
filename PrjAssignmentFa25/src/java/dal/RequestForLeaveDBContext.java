/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.RequestForLeave;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Department;
import model.Employee;
import model.ReasonType;

/**
 *
 * @author phuga
 */
public class RequestForLeaveDBContext extends DBContext {
// Dùng int cho hàm này để lấy reqid (id của đơn thứ ..) được tạo ra, nhằm dùng cho sau này

    public int createRFL(RequestForLeave rfl) throws SQLException {
        //vì là đơn nghỉ nên chỉ cần những thông tin này còn những thông tin như created time, hoặc những thứ khác thì là của bên xử lí
        String sql_create_rfl = """
                                                INSERT INTO [RequestForLeave]
                                                           ([created_by]
                                                           ,[from]
                                                           ,[to]
                                                           ,[did]
                                                           ,[rtid]
                                                           ,[reason_others])
                                                     VALUES (
                                                ?,
                                                ?,
                                                ?,
                                                ?,
                                                ?,
                                                ?)""";

        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            //Return generate ... => const variable => return về id của cái vừa được tạo, dùng ở dưới
            stm = connection.prepareStatement(sql_create_rfl, Statement.RETURN_GENERATED_KEYS); //(1)

            //setString (?)
            //biến request kiểu dl class RFL truyền vào
            //quá trình như sau:
            /*
                - Class này được tạo đối tượng bên Controller
                - biến rlf ở dưới được tạo bên Controller để LẤY giá trị của người dùng
                - Sau đó gọi các rlf đó để lấy các attribute của câu lệnh INSERT
                - Dữ liệu được lấy và truyền vào câu lệnh insert(các ?) ở trên
            
                - Khác so với ĐỌC dữ liệu:(tạo đối tượng class đó bên đây rồi mang đi so sánh với
                - Còn GHI dữ liệu thì như này (ngược lại so với đọc) (lấy dữ liệu từ controller và adapt vào sql statement trên kia)
             */
            stm.setInt(1, rfl.getCreatedBy().getId()); // -> created_by
            stm.setDate(2, rfl.getFromDate()); // -> from
            stm.setDate(3, rfl.getToDate()); // -> to

            stm.setInt(4, rfl.getDepartment().getId()); // -> did
            stm.setInt(5, rfl.getReasonType().getId()); // -> rtid

            //Check reason_others (may NULL)
            if (rfl.getReasonOthers() != null && !rfl.getReasonOthers().isEmpty()) {
                //nếu người dùng có nhập nghỉ lý do khác
                stm.setString(6, rfl.getReasonOthers());
            } else {
                //nếu người dùng k nhập các lí do có sẵn thì => Chèn NULL vào db
                stm.setNull(6, java.sql.Types.NVARCHAR); // lệnh chèn null (cột có nhận NULL)
            }

            //có thông tin => chạy lệnh insert
            stm.executeUpdate();

            //Lấy id(reqid) vừa tạo
            rs = stm.getGeneratedKeys(); //(2)
            if (rs.next()) {
                /*
                Câu lệnh (1) sẽ lấy id vừa đc tạo ra sau khi câu lệnh insert thực hiện
                Câu lệnh (2) ở trên thì nó sẽ nhận giá trị int int ở cột đầu (reqid)
                 */
                return rs.getInt(1); //1 này là cột thứ 1 (reqid)
            }

        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName()).log(Level.SEVERE, null, ex);
            throw ex; //ném lỗi cho controller 
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            closeConnection();
        }
        return -1; //fail
    }

    // ====== HÀM SHOW LIST THEO ROLE ======
    //hàm phụ đóng gói dữ liệu
    private RequestForLeave enDataToRFL(ResultSet rs) throws SQLException {
        RequestForLeave rfl = new RequestForLeave();

        //i4 của 1 đơn rfl
        rfl.setId(rs.getInt("reqid"));
        rfl.setFromDate(rs.getDate("from"));
        rfl.setToDate(rs.getDate("to"));
        rfl.setStatus(rs.getInt("status"));
        rfl.setCreatedTime(rs.getTimestamp("created_time"));
        rfl.setReasonOthers(rs.getString("reason_others"));
        rfl.setProcessedTime(rs.getTimestamp("processed_time"));
        rfl.setDecisionNote(rs.getString("decision_note"));

        //i4 emp create
        Employee creator = new Employee();
        creator.setId(rs.getInt("created_by_eid"));
        creator.setName(rs.getNString("created_by_name"));
        rfl.setCreatedBy(creator);

        //i4 người duyệt (emp) - may null
        int processedByEId = rs.getInt("processed_by_eid");
        if (!rs.wasNull()) { //check null, không null thì gán id đó luôn
            Employee processor = new Employee();
            processor.setId(processedByEId);
            processor.setName(rs.getNString("processed_by_name"));
            rfl.setProcessedBy(processor);
        }

        //i4 reasonType
        ReasonType rt = new ReasonType();
        rt.setId(rs.getInt("rtid"));
        rt.setRname(rs.getNString("reason_name"));
        rfl.setReasonType(rt);

        //i4 department
        Department depts = new Department();
        depts.setId(rs.getInt("did"));
        depts.setDname(rs.getString("dept_name"));
        rfl.setDepartment(depts);

        return rfl;
    }

    //hàm 1: lấy list đơn của cá nhân
    public ArrayList<RequestForLeave> listByUserId(int eid, int pageindex, int pagesize) throws SQLException {
        ArrayList<RequestForLeave> listRFL = new ArrayList<>();

        PreparedStatement stm = null;
        ResultSet rs = null;

        String sql_list_Emp = """
                              SELECT r.reqid
                                        ,r.created_by
                                        ,r.created_time
                                        ,r.processed_by
                                        ,r.[from]
                                        ,r.[to]
                                        ,r.[status]
                                        ,r.did
                                        ,r.rtid
                                        ,r.reason_others
                                        ,r.processed_time
                                        ,r.decision_note
                                  	  ,e_creator.eid AS created_by_eid
                                  	  ,e_creator.ename AS created_by_name
                                  	  ,e_processor.eid AS processed_by_eid
                                  	  ,e_processor.ename AS processed_by_name
                                  	  ,rt.rname AS reason_name
                                  	  ,d.dname AS dept_name
                                  
                                    FROM RequestForLeave r
                                    INNER JOIN Employee e_creator ON r.created_by = e_creator.eid
                                    LEFT JOIN Employee e_processor ON r.processed_by = e_processor.eid
                                    INNER JOIN ReasonType rt ON r.rtid = rt.rtid
                                    INNER JOIN Department d ON r.did = d.did
                                    WHERE r.created_by = ?
                                    ORDER BY r.reqid DESC
                                        OFFSET (?-1)*? ROWS
                                        FETCH NEXT ? ROWS ONLY;
                              """;

        try {
            stm = connection.prepareStatement(sql_list_Emp);
            stm.setInt(1, eid);
            stm.setInt(2, pageindex);
            stm.setInt(3, pagesize);
            stm.setInt(4, pagesize);
            rs = stm.executeQuery();

            while (rs.next()) {
                //use hàm phụ => đóng gói đối tượng với dữ liệu 
                listRFL.add(enDataToRFL(rs));
                //truyền rs vào đóng gói dữ liệu với hàm bên dưới
            }
        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName()).log(Level.SEVERE, "Lỗi khi lấy đơn theo Employee ID", ex);
            throw ex;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            closeConnection();
        }
        return listRFL;
    }

    //hàm 2: lấy list đơn của department(cấp trên xem đc cấp dưới)
    // code tương tự hàm list emp nên copy xuống
    public ArrayList<RequestForLeave> listByDeptsId(int did, int pageindex, int pagesize) throws SQLException {
        ArrayList<RequestForLeave> listRFL = new ArrayList<>();

        PreparedStatement stm = null;
        ResultSet rs = null;

        String sql_list_depts = """
                              SELECT r.reqid
                                        ,r.created_by
                                        ,r.created_time
                                        ,r.processed_by
                                        ,r.[from]
                                        ,r.[to]
                                        ,r.[status]
                                        ,r.did
                                        ,r.rtid
                                        ,r.reason_others
                                        ,r.processed_time
                                        ,r.decision_note
                                  	  ,e_creator.eid AS created_by_eid
                                  	  ,e_creator.ename AS created_by_name
                                  	  ,e_processor.eid AS processed_by_eid
                                  	  ,e_processor.ename AS processed_by_name
                                  	  ,rt.rname AS reason_name
                                  	  ,d.dname AS dept_name
                                  
                                    FROM RequestForLeave r
                                    INNER JOIN Employee e_creator ON r.created_by = e_creator.eid
                                    LEFT JOIN Employee e_processor ON r.processed_by = e_processor.eid
                                    INNER JOIN ReasonType rt ON r.rtid = rt.rtid
                                    INNER JOIN Department d ON r.did = d.did
                                    WHERE r.did = ?
                                    ORDER BY r.reqid DESC
                                      OFFSET (?-1)*? ROWS
                                      FETCH NEXT ? ROWS ONLY;
                                """;

        try {
            stm = connection.prepareStatement(sql_list_depts);
            stm.setInt(1, did);
            stm.setInt(2, pageindex);
            stm.setInt(3, pagesize);
            stm.setInt(4, pagesize);
            rs = stm.executeQuery();

            while (rs.next()) {
                //use hàm phụ => đóng gói đối tượng với dữ liệu 
                listRFL.add(enDataToRFL(rs));
                //truyền rs vào đóng gói dữ liệu với hàm bên dưới
            }
        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName()).log(Level.SEVERE, "Lỗi khi lấy đơn theo Department ID", ex);
            throw ex;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            closeConnection();
        }
        return listRFL;
    }

    //hàm 3: Đếm số lượng đơn của list request for leave khi xem = employee id
    public int countListRflByEmpId(int eid) throws SQLException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT COUNT(*) as Total FROM [RequestForLeave] WHERE created_by = ?";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, eid);
            rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt("Total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            closeConnection();
        }
        return 0;
    }

    //hàm 4: Đếm số lượng đơn của list request for leave khi xem = department id
    public int countByDepartmentId(int did) throws SQLException {
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT COUNT(*) as Total FROM [RequestForLeave] WHERE did = ?";
            stm = connection.prepareStatement(sql);
            stm.setInt(1, did);
            rs = stm.executeQuery();
            if (rs.next()) {
                return rs.getInt("Total");
            }
        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }
            closeConnection();
        }
        return 0;
    }

}
