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
*/
public class Department extends BaseModel{
    private String name;
    private int superior;

    public Department() {
    }

    public Department(String name, int superior) {
        this.name = name;
        this.superior = superior;
    }

    public Department(String name, int superior, int id) {
        super(id);
        this.name = name;
        this.superior = superior;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSuperior() {
        return superior;
    }

    public void setSuperior(int superior) {
        this.superior = superior;
    }
    
    
}
