/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.ArrayList;
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
    public int createRFL(RequestForLeave request) {
        String sql_create_rfl = """
                                                INSERT INTO [RequestForLeave]
                                                           ([created_by]
                                                           ,[created_time]
                                                           ,[processed_by]
                                                           ,[from]
                                                           ,[to]
                                                           ,[status]
                                                           ,[did]
                                                           ,[rtid]
                                                           ,[reason_others]
                                                           ,[processed_time]
                                                           ,[decision_note])
                                                     VALUES (
                                                ?,
                                                ?,
                                                ?,
                                                ?,
                                                ?,
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
            stm.setString(1, rfl.getCreatedBy().getId());
            
        } catch (SQLException ex) {
            Logger.getLogger(RequestForLeaveDBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Tính năng tạo đơn
}
