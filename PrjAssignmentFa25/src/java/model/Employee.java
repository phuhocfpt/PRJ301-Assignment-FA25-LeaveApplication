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
// Sang bên class Department đọc
public class Employee extends BaseModel {

    private String name;
    private Date dob;
    private String address;
    private String phone;
    private Department department;
    private String email;
    private boolean egender;
    private Integer managerId;

    public Employee() {
    }

    public Employee(String name, Date dob, String address, String phone, Department department, String email, boolean egender, Integer managerId) {
        this.name = name;
        this.dob = dob;
        this.address = address;
        this.phone = phone;
        this.department = department;
        this.email = email;
        this.egender = egender;
        this.managerId = managerId;
    }

    public Employee(String name, Date dob, String address, String phone, Department department, String email, boolean egender, Integer managerId, int id) {
        super(id);
        this.name = name;
        this.dob = dob;
        this.address = address;
        this.phone = phone;
        this.department = department;
        this.email = email;
        this.egender = egender;
        this.managerId = managerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEgender() {
        return egender;
    }

    public void setEgender(boolean egender) {
        this.egender = egender;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

}
