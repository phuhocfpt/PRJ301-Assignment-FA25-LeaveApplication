/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import model.iam.User;

/**
 *
 * @author phuga
 */
public class Enrollment {

    private Employee employee;
    private User user;
    private boolean active;

    public Enrollment() {
    }

    public Enrollment(Employee employee, User user, boolean active) {
        this.employee = employee;
        this.user = user;
        this.active = active;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
