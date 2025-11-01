/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal.iam;

import dal.DBContext;
import java.sql.SQLException;
import model.iam.User;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Employee;

/**
 *
 * @author phuga
 */
public class UserDBContext extends DBContext {

    public User get(String username, String password) throws SQLException {
        try {
            String sql = """
                     SELECT
                         u.uid,
                         u.username,
                         u.displayname,
                         u.created_time, 
                         e.eid,
                         e.ename,
                         e.email 
                     FROM [User] u 
                         INNER JOIN [Enrollment] en ON u.[uid] = en.[uid]
                         INNER JOIN [Employee] e ON e.eid = en.eid
                     WHERE
                         u.username = ? AND u.[password] = ?
                         AND en.active = 1""";

            PreparedStatement stm = connection.prepareStatement(sql);
            stm.setString(1, username);
            stm.setString(2, password);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                User u = new User();
                Employee e = new Employee();
                e.setId(rs.getInt("eid"));
                e.setName(rs.getNString("ename"));
                e.setEmail(rs.getString("email"));
                u.setEmployee(e);
                
                
                u.setId(rs.getInt("uid"));
                u.setUsername(rs.getString("username"));
                u.setDisplayname(rs.getNString("displayname"));
                u.setCreated_time(rs.getTimestamp("created_time"));

                return u;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }
        return null; //nếu không đúng thông tin hoặc không thỏa mãn đăng nhập(active = 0, ..) thì sẽ return về null
    }
}
