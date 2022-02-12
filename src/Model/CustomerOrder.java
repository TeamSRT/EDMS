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
    int productID;
    String Model;
    String Brand;
    public CustomerOrder()
    {
        
    }
    public CustomerOrder(int orderID, int productID, String Model, String Brand) {
        this.orderID = orderID;
        this.productID = productID;
        this.Model = Model;
        this.Brand = Brand;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String Model) {
        this.Model = Model;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String Brand) {
        this.Brand = Brand;
    }
        
    
    
}
