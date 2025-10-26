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
public class RequestForLeave extends BaseModel {

    private Employee createdBy;
    private Timestamp createdTime;

    private Employee processedBy;
    private Timestamp processedTime;

    private Date fromDate;
    private Date toDate;

    private int status;

    private Department department;

    private ReasonType reasonType;

    private String reasonOthers;

    private String decisionNote;

    public RequestForLeave() {
    }

    public RequestForLeave(Employee createdBy, Timestamp createdTime, Employee processedBy, Timestamp processedTime, Date fromDate, Date toDate, int status, Department department, ReasonType reasonType, String reasonOthers, String decisionNote) {
        this.createdBy = createdBy;
        this.createdTime = createdTime;
        this.processedBy = processedBy;
        this.processedTime = processedTime;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.status = status;
        this.department = department;
        this.reasonType = reasonType;
        this.reasonOthers = reasonOthers;
        this.decisionNote = decisionNote;
    }

    public RequestForLeave(Employee createdBy, Timestamp createdTime, Employee processedBy, Timestamp processedTime, Date fromDate, Date toDate, int status, Department department, ReasonType reasonType, String reasonOthers, String decisionNote, int id) {
        super(id);
        this.createdBy = createdBy;
        this.createdTime = createdTime;
        this.processedBy = processedBy;
        this.processedTime = processedTime;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.status = status;
        this.department = department;
        this.reasonType = reasonType;
        this.reasonOthers = reasonOthers;
        this.decisionNote = decisionNote;
    }

    public Employee getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Employee createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Employee getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(Employee processedBy) {
        this.processedBy = processedBy;
    }

    public Timestamp getProcessedTime() {
        return processedTime;
    }

    public void setProcessedTime(Timestamp processedTime) {
        this.processedTime = processedTime;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public ReasonType getReasonType() {
        return reasonType;
    }

    public void setReasonType(ReasonType reasonType) {
        this.reasonType = reasonType;
    }

    public String getReasonOthers() {
        return reasonOthers;
    }

    public void setReasonOthers(String reasonOthers) {
        this.reasonOthers = reasonOthers;
    }

    public String getDecisionNote() {
        return decisionNote;
    }

    public void setDecisionNote(String decisionNote) {
        this.decisionNote = decisionNote;
    }

}
