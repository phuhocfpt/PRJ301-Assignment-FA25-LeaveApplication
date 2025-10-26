/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.iam;

import model.BaseModel;

/**
 *
 * @author phuga
 */
public class Feature extends BaseModel{
    private String url;

    public Feature() {
    }

    public Feature(String url) {
        this.url = url;
    }

    public Feature(String url, int id) {
        super(id);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    
}
