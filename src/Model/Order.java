/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import java.sql.Timestamp;

/**
 *
 * @author ktoufiquee
 */
public class Order {
    int orderID;
    int customerID;
    int productID;
    int quantity;
    Timestamp orderTime;
    int cost;

    public Order(int orderID, int customerID, int productID, int quantity, Timestamp orderTime, int cost) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.productID = productID;
        this.quantity = quantity;
        this.orderTime = orderTime;
        this.cost = cost;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
    
    
    
    
}
