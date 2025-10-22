/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.sql.*;
/**
 *
 * @author phuga
 */
public class User extends BaseModel{
    private String username;
    private String password;
    public String displayname;
    public Date created_time;

    public User() {
    }

    public User(String username, String password, String displayname, Date created_time) {
        this.username = username;
        this.password = password;
        this.displayname = displayname;
        this.created_time = created_time;
    }

    public User(String username, String password, String displayname, Date created_time, int id) {
        super(id);
        this.username = username;
        this.password = password;
        this.displayname = displayname;
        this.created_time = created_time;
    }
    
    

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayname() {
        return displayname;
    }

    public void setDisplayname(String displayname) {
        this.displayname = displayname;
    }

    public Date getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Date created_time) {
        this.created_time = created_time;
    }
    
    
}
