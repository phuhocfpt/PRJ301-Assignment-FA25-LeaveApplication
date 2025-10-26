/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author phuga
 */
/*
    File thầy có trong bảng Employee có thuộc tính 
        private Employee superior
    
    Đang thắc mắc là có phải tạo superior ở bảng này với kiểu dữ liệu 
    là Employee superior hay không?

Trả lời: Không vì superior ở đây chỉ tới superior của Department đó không phải của Employee id
 */
public class Department extends BaseModel {

    private String dname;
    private Department superior;

    public Department() {
    }

    public Department(String dname, Department superior) {
        this.dname = dname;
        this.superior = superior;
    }

    public Department(String dname, Department superior, int id) {
        super(id);
        this.dname = dname;
        this.superior = superior;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public Department getSuperior() {
        return superior;
    }

    public void setSuperior(Department superior) {
        this.superior = superior;
    }

}
