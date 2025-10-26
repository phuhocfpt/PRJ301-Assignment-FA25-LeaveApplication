package model.iam;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import model.BaseModel;

/**
 *
 * @author phuga
 */
public class Role extends BaseModel {
    private String rname;
    private String rcode;
    private int rlevel;

    public Role() {
    }

    public Role(String rname, String rcode, int rlevel) {
        this.rname = rname;
        this.rcode = rcode;
        this.rlevel = rlevel;
    }

    public Role(String rname, String rcode, int rlevel, int id) {
        super(id);
        this.rname = rname;
        this.rcode = rcode;
        this.rlevel = rlevel;
    }

    public String getRname() {
        return rname;
    }

    public void setRname(String rname) {
        this.rname = rname;
    }

    public String getRcode() {
        return rcode;
    }

    public void setRcode(String rcode) {
        this.rcode = rcode;
    }

    public int getRlevel() {
        return rlevel;
    }

    public void setRlevel(int rlevel) {
        this.rlevel = rlevel;
    }
    
    
    
    
}
