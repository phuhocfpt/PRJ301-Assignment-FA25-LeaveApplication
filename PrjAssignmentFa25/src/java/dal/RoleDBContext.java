/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.util.ArrayList;
import model.iam.Role;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.iam.Feature;

/**
 *
 * @author phuga
 */
public class RoleDBContext extends DBContext<Role> {

    public ArrayList<Role> getByUserId(int id) {
        ArrayList<Role> roles = new ArrayList<>();
        PreparedStatement stm_role = null;
        PreparedStatement stm_feature = null;
        ResultSet rs_role = null;
        ResultSet rs_feature = null;
        try {

            // Lấy tất cả role
            String sql_role = """
                            SELECT r.rid, r.rname, r.rcode, r.rlevel
                            FROM Role r
                            INNER JOIN UserRole ur ON r.rid = ur.rid
                            WHERE ur.uid = ?""";
            
            //stm_role ở trên đang null
            stm_role = connection.prepareStatement(sql_role);
            stm_role.setInt(1, id);
            rs_role = stm_role.executeQuery();

            // Với mỗi Role tìm được...
            while (rs_role.next()) {
                Role r = new Role();
                r.setId(rs_role.getInt("rid"));
                r.setRname(rs_role.getString("rname"));
                r.setRcode(rs_role.getString("rcode"));
                r.setRlevel(rs_role.getInt("rlevel"));

                // Lấy all feature của role đó
                String sql_feature = """
                                     SELECT f.fid, f.url
                                     FROM Feature f
                                     INNER JOIN RoleFeature rf ON f.fid = rf.fid
                                     WHERE rf.rid = ?""";

                ArrayList<Feature> features = new ArrayList<>();
                
                //stm_feature trên kia khai báo null
                stm_feature = connection.prepareStatement(sql_feature);
                stm_feature.setInt(1, r.getId()); // Dùng ID của Role vừa lấy
                rs_feature = stm_feature.executeQuery();

                // Nạp các Feature vào trong Role
                while (rs_feature.next()) {
                    Feature f = new Feature();
                    f.setId(rs_feature.getInt("fid"));
                    f.setUrl(rs_feature.getString("url"));
                    features.add(f);
                }

                r.setFeatures(features); // Gán danh sách Feature vào Role
                roles.add(r); // Add role có feature vào ArrayList roles    
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoleDBContext.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeConnection();
        }

        return roles;

    }

}
