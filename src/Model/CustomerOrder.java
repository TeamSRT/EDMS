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
public class CustomerOrder {
    int orderID;
    String date;
    int productID;
    String Brand;
    String Model;
    String warrantyRemain;
    int due;
    int serviceCharge;
    String status;
    String givenDate;
    public CustomerOrder()
    {
        
    }
    public CustomerOrder(int orderID, String date, int productID, String Brand, String Model, String warrantyRemain, int due) {
        this.orderID = orderID;
        this.date = date;
        this.productID = productID;
        this.Brand = Brand;
        this.Model = Model;
        this.warrantyRemain = warrantyRemain;
        this.due = due;
    }
    public CustomerOrder(int productID, String Brand, String Model, int serviceCharge, String status, String givenDate)
    {
        this.productID = productID;
        this.Brand = Brand;
        this.Model = Model;
        this.serviceCharge = serviceCharge;
        this.status = status;
        this.givenDate = givenDate;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String Brand) {
        this.Brand = Brand;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String Model) {
        this.Model = Model;
    }

    public String getWarrantyRemain() {
        return warrantyRemain;
    }

    public void setWarrantyRemain(String warrantyRemain) {
        this.warrantyRemain = warrantyRemain;
    }

    public int getDue() {
        return due;
    }

    public void setDue(int due) {
        this.due = due;
    }
    
   
    
    
}
