/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.iam;

import java.sql.Timestamp;
import java.util.ArrayList;
import model.BaseModel;
import model.Employee;

/**
 *
 * @author phuga
 */
public class User extends BaseModel {

    private String username;
    private String password;
    private String displayname;
    private Timestamp created_time;
    private Employee employee;
    private ArrayList<Role> roles = new ArrayList<>();

    public User() {
    }

    public User(String username, String password, String displayname, Timestamp created_time, Employee employee) {
        this.username = username;
        this.password = password;
        this.displayname = displayname;
        this.created_time = created_time;
        this.employee = employee;
    }

    public User(String username, String password, String displayname, Timestamp created_time, Employee employee, int id) {
        super(id);
        this.username = username;
        this.password = password;
        this.displayname = displayname;
        this.created_time = created_time;
        this.employee = employee;
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }

}
