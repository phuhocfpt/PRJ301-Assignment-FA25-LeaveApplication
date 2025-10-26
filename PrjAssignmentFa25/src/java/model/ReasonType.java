/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author phuga
 */
public class ReasonType extends BaseModel{
    private String rcode;
    private String rname;
    private boolean reasonOthers;

    public ReasonType() {
    }

    public ReasonType(String rcode, String rname, boolean reasonOthers) {
        this.rcode = rcode;
        this.rname = rname;
        this.reasonOthers = reasonOthers;
    }

    public ReasonType(String rcode, String rname, boolean reasonOthers, int id) {
        super(id);
        this.rcode = rcode;
        this.rname = rname;
        this.reasonOthers = reasonOthers;
    }

    public String getRcode() {
        return rcode;
    }

    public void setRcode(String rcode) {
        this.rcode = rcode;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public boolean isReasonOthers() {
        return reasonOthers;
    }

    public void setReasonOthers(boolean reasonOthers) {
        this.reasonOthers = reasonOthers;
    }

    
    
    
}
