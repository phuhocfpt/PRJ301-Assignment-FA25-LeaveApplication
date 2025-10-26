/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.iam;

import java.sql.Timestamp;
import model.BaseModel;

/**
 *
 * @author phuga
 */
public class User extends BaseModel {

    private String username;
    private String password;
    private String displayname;
    private Timestamp created_time;

    private Role role;

    public User() {
    }

    public User(String username, String password, String displayname, Timestamp created_time, Role role) {
        this.username = username;
        this.password = password;
        this.displayname = displayname;
        this.created_time = created_time;
        this.role = role;
    }

    public User(String username, String password, String displayname, Timestamp created_time, Role role, int id) {
        super(id);
        this.username = username;
        this.password = password;
        this.displayname = displayname;
        this.created_time = created_time;
        this.role = role;
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

    public Timestamp getCreated_time() {
        return created_time;
    }

    public void setCreated_time(Timestamp created_time) {
        this.created_time = created_time;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
