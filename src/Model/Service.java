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
public class Service {
    int serviceID;
    int productID;
    String details;
    int customerID;
    int serviceCharge;
    
    public Service()
    {
        
    }
    
    public Service(int serviceID, int productID, int customerID, String details, int serviceCharge) {
        this.serviceID = serviceID;
        this.productID = productID;       
        this.customerID = customerID;
        this.details = details;
        this.serviceCharge = serviceCharge;
    }

    public int getServiceID() {
        return serviceID;
    }

    public void setServiceID(int serviceID) {
        this.serviceID = serviceID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(int serviceCharge) {
        this.serviceCharge = serviceCharge;
    }
    
}
