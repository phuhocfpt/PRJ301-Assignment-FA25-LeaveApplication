/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author phuga
 * @param <T>
 */

//Không để là DBContext<T extends BaseModel> vì những DAO khác mà không
//extends với BaseModel sẽ không hoạt động được và chỉ hoạt động với
//những class model extends BaseModel
//Vì vậy những method list, delete ... sẽ được khai báo ở những class cần dùng
public abstract class DBContext{
    protected Connection connection;

    public DBContext() {
        try {
            String user = "sa";
            String pass = "123";
            String url = "jdbc:sqlserver://localhost:1433;databaseName=PRJ301Assignment;encrypt=true;trustServerCertificate=true;";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException ex) { //lỗi không inport file jar jdbc
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
        //nổ lỗi
        
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    //Update thêm
    
    protected void beginTransaction() throws SQLException // (1)
    {
        connection.setAutoCommit(false);
    }
    
    protected void commitTransaction() throws SQLException // (2)
    {
        connection.commit();
    }
    
    protected void rollbackTransaction() // (3)
    {
        try {
            connection.rollback();
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    protected void resetTransactionMode() // (4)
    {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException ex) {
            Logger.getLogger(DBContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Các tính năng như sau:
    /*
    (1): Giai đoạn đầu mình sẽ cho commit = true để đưa dữ liệu qua
    (2): Mình sẽ để nó commit
    (3): Nếu như commit không thành công thì sẽ rollback lại
    (4): Ở đoạn (1) có chỉnh commit thành true thì ở dưới đây để reset về default
    
    **** NOTE: MÌNH SẼ GỌI CÁI NÀY TRONG CÁC CLASS EXTENDS DBContext này thay vì phải viết
                dài dòng và try catch CRUD
    */
   
}
