/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author SrishtiPC
 */
public class Supplier {
    int SupplierID;
    String phone;
    String name;
    String mail;
    public Supplier()
    {
        
    }
            
    public Supplier(int SupplierID, String phone, String name, String mail) {
        this.SupplierID = SupplierID;
        this.phone = phone;
        this.name = name;
        this.mail = mail;
    }

    public int getSupplierID() {
        return SupplierID;
    }

    public void setSupplierID(int SupplierID) {
        this.SupplierID = SupplierID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String email) {
        this.mail = email;
    }
    
}
