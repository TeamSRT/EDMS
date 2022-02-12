/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package table_model;

/**
 *
 * @author SaMiR
 */
public class Supply {
    
    int supplierID;
    int productID;
    String date;
    int quantity;

    public Supply(int supplierID, int productID, String date, int quantity) {
        this.supplierID = supplierID;
        this.productID = productID;
        this.date = date;
        this.quantity = quantity;
    }
    
    
    

    public int getSupplierID() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID = supplierID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    
}
