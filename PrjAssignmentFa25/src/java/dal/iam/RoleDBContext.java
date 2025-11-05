/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal.iam;

import dal.DBContext;
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
public class RoleDBContext extends DBContext {

    public ArrayList<Role> getByUserId(int id) throws SQLException {
        ArrayList<Role> roles = new ArrayList<>();
        PreparedStatement stm = null;
        ResultSet rs = null;
        try {

            // Lấy tất cả Role và Feature của 1 User
            String sql_roleAndFeatureOfAUser = """
                         SELECT r.rid, r.rname, r.rcode, r.rlevel, f.fid, f.url
                         FROM [User] u 
                             INNER JOIN [UserRole] ur ON u.uid = ur.uid
                             INNER JOIN [Role] r ON r.rid = ur.rid
                             INNER JOIN [RoleFeature] rf ON rf.rid = r.rid
                             INNER JOIN [Feature] f ON f.fid = rf.fid
                         WHERE u.uid = ?
                         ORDER BY r.rid ASC"""; //id ascending

            //stm_role ở trên đang null
            stm = connection.prepareStatement(sql_roleAndFeatureOfAUser);
            stm.setInt(1, id);
            rs = stm.executeQuery();
            //current Role để check
            Role r = null; //để trong vòng lặp thì mỗi lần while sẽ resert role => null

            // Với mỗi Role tìm được...
            while (rs.next()) {
                int rid = rs.getInt("rid");

                //check xem role đang check có phải new Role không
                if (r == null || rid != r.getId()) {
                    //if new role
                    r = new Role();

                    r.setId(rid); //gán id cho
//                    r.setId(rs.getInt("rid"));
/*
không dùng lệnh cmt để set id => lỗi gán id
vd: đầu vào a2, uid = 1, rid = 5(EMP)
                // So sánh rid (mới, là 5) với rid của Role (cũ, đang ghi nhớ).
                // - Sẽ TRUE ở lần lặp đầu tiên (vì currentRole == null)
                // - Sẽ FALSE ở lần lặp 2, 3 (vì 5 == 5)
                // - Sẽ TRUE khi bắt đầu một Role mới (ví dụ: rid = 4)
                     */

                    r.setRname(rs.getString("rname"));
                    r.setRcode(rs.getString("rcode"));
                    r.setRlevel(rs.getInt("rlevel"));

                    //add vào arrayList
                    roles.add(r);
                }

                // Lấy all feature của role đó
                //vì trong role khi tạo constructer(tạo đối tượng sẽ tạo ra)
                //1 list như: private ArrayList<Feature> features = new ArrayList<>();
                //nên không cần tạo arrayList ở đây để lưu feature của role đó mà gán vào list của từng user luôn
                Feature f = new Feature();
                f.setId(rs.getInt("fid"));
                f.setUrl(rs.getString("url"));
                
                //check getFeature trog class Role(return về 1 arrayList)
                if(r.getFeatures() != null){
                    r.getFeatures().add(f); //thêm feature đó vào list
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(RoleDBContext.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        } finally {
            if(rs != null){
                rs.close();
            }
            if(stm != null){
                stm.close();
            }
            closeConnection();
        }

        return roles;

    }

}
