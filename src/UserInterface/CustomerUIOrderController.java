/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Model.Customer;
import Model.CustomerOrder;
import Utility.DataManager;
import Utility.Database;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author SrishtiPC
 */
public class CustomerUIOrderController implements Initializable {
    @FXML
    private TableView<CustomerOrder> tvCustomerOrder;
    @FXML
    private TableColumn<CustomerOrder, String> tcModel;
    @FXML
    private TableColumn<CustomerOrder, Integer> tcOrderID;   
    @FXML
    private TableColumn<CustomerOrder, Integer> tcProductID;
    @FXML
    private TableColumn<CustomerOrder, String> tcBrand;  
    @FXML
    private TextField tfCustomerID;
    ObservableList<CustomerOrder> listCustomerOrder = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showTotalOrder();
    }

    private void showTotalOrder() {
        listCustomerOrder.clear();
        tfCustomerID.setText(DataManager.selected.getCustomerID()+"");
        try {
            String query = "SELECT Orders.orderID,Product.productID,Product.Brand,Product.Model from ORDERS INNER JOIN PRODUCT ON ORDERS.orderID = PRODUCT.ProductID where ORDERS.customerID = " + DataManager.selected.getCustomerID();
            System.out.println("Button Show Order = " + query);
            Database db = new Database();
            db.connect();
            ResultSet rs = db.getResult(query);
            while (rs.next()) {
                System.out.println("Print in show Total Order:");
                listCustomerOrder.add(new CustomerOrder(rs.getInt("orderID"), rs.getInt("productID"),rs.getString("Brand"),rs.getString("Model")));
                System.out.println("OrderId = " +rs.getInt("orderID") + "ProductID = "+rs.getInt("productID"));    
            }
            
            tcOrderID.setCellValueFactory(new PropertyValueFactory("orderID"));
            tcProductID.setCellValueFactory(new PropertyValueFactory("productID"));
            tcBrand.setCellValueFactory(new PropertyValueFactory("Brand"));
            tcModel.setCellValueFactory(new PropertyValueFactory("Model"));
            tvCustomerOrder.setItems(listCustomerOrder);
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Exception in show total order = "+ex);
        }
    }

}
