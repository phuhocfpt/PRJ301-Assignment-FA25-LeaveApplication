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
public class LeaveStatusHistory extends BaseModel{
    private RequestForLeave requestForLeave;
    private int newStatus;
    private Employee changedBy;
    private Timestamp changedAt;
    private String note;

    public LeaveStatusHistory() {
    }

    public LeaveStatusHistory(RequestForLeave requestForLeave, int newStatus, Employee changedBy, Timestamp changedAt, String note) {
        this.requestForLeave = requestForLeave;
        this.newStatus = newStatus;
        this.changedBy = changedBy;
        this.changedAt = changedAt;
        this.note = note;
    }

    public LeaveStatusHistory(RequestForLeave requestForLeave, int newStatus, Employee changedBy, Timestamp changedAt, String note, int id) {
        super(id);
        this.requestForLeave = requestForLeave;
        this.newStatus = newStatus;
        this.changedBy = changedBy;
        this.changedAt = changedAt;
        this.note = note;
    }

    public RequestForLeave getRequestForLeave() {
        return requestForLeave;
    }

    public void setRequestForLeave(RequestForLeave requestForLeave) {
        this.requestForLeave = requestForLeave;
    }

    public int getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(int newStatus) {
        this.newStatus = newStatus;
    }

    public Employee getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(Employee changedBy) {
        this.changedBy = changedBy;
    }

    public Timestamp getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Timestamp changedAt) {
        this.changedAt = changedAt;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
    
    
}
