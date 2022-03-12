/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;


public class Service {
    int serviceID;
    int productID;
    int customerID;
    String details;    
    int serviceCharge;
    String serviceStatus;
    String givenDate;

    public Service(int serviceID, int productID, int customerID, String details, int serviceCharge, String serviceStatus, String givenDate) {
        this.serviceID = serviceID;
        this.productID = productID;
        this.customerID = customerID;
        this.details = details;
        this.serviceCharge = serviceCharge;
        this.serviceStatus = serviceStatus;
        this.givenDate = givenDate;
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

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(int serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }

    public void setServiceStatus(String serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    public String getGivenDate() {
        return givenDate;
    }

    public void setGivenDate(String givenDate) {
        this.givenDate = givenDate;
    }
    
   
}
