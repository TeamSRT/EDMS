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
public class SupplyDetails {
    String Brand;
    String Model;
    int quantity;
    int cost;
    String supplyDate;

    public SupplyDetails(String Brand, String Model, int quantity, int cost, String supplyDate) {
        this.Brand = Brand;
        this.Model = Model;
        this.quantity = quantity;
        this.cost = cost;
        this.supplyDate = supplyDate;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getSupplyDate() {
        return supplyDate;
    }

    public void setSupplyDate(String supplyDate) {
        this.supplyDate = supplyDate;
    }
    
}
