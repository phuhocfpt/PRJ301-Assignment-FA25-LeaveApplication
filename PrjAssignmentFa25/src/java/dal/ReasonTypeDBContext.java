/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.ArrayList;
import model.ReasonType;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author phuga
 */
public class ReasonTypeDBContext extends DBContext {
    //lấy all lí do từ sql

    public ArrayList<ReasonType> listsReason() throws SQLException {
        ArrayList<ReasonType> reasons = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {

            String sql_reasons = """
                                         SELECT rtid
                                               ,rcode
                                               ,rname
                                               ,reason_others
                                           FROM ReasonType""";

            stm = connection.prepareStatement(sql_reasons);
            rs = stm.executeQuery();

            while (rs.next()) {
                ReasonType rt = new ReasonType();

                rt.setId(rs.getInt("rtid"));
                rt.setRcode(rs.getString("rcode"));
                rt.setRname(rs.getNString("rname"));
                rt.setReasonOthers(rs.getBoolean("reason_others"));

                reasons.add(rt);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ReasonTypeDBContext.class.getName()).log(Level.SEVERE, null, ex);
            throw ex; //ném lỗi xử lysowr controller
        } finally {

            if (rs != null) {
                rs.close();
            }
            if (stm != null) {
                stm.close();
            }

            closeConnection();
        }

        return reasons;
    }
}
