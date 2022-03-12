/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author ktoufiquee
 */
public class CustomerService {

    int productID;
    String Brand;
    String Model;
    int serviceCharge;
    String status;
    String givenDate;

    public CustomerService(int productID, String Brand, String Model, int serviceCharge, String status, String givenDate) {
        this.productID = productID;
        this.Brand = Brand;
        this.Model = Model;
        this.serviceCharge = serviceCharge;
        this.status = status;
        this.givenDate = givenDate;
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

    public int getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(int serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGivenDate() {
        return givenDate;
    }

    public void setGivenDate(String givenDate) {
        this.givenDate = givenDate;
    }
    
    
}
