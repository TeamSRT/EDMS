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
public class Transaction {
    
    int transaction_id;
    int order_id;
    String type;
    String date;
    float amount;

    public Transaction(int transaction_id, int order_id, String type, String date, float amount) {
        this.transaction_id = transaction_id;
        this.order_id = order_id;
        this.type = type;
        this.date = date;
        this.amount = amount;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
    
    
    
    
    
    
    
    
    
}
