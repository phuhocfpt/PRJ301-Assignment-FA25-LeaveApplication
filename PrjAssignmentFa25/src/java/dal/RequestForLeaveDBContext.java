/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.RequestForLeave;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                                                ?""";

        PreparedStatement stm = null;
        ResultSet rs = null;
        try {
            //Return generate ... => const variable => return về id của cái vừa được tạo, dùng ở dưới
            stm = connection.prepareStatement(sql_create_rfl, Statement.RETURN_GENERATED_KEYS);

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

    //Tính năng tạo đơn
}
