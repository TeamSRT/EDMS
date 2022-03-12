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
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

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
    private TableColumn<CustomerOrder, String> tcDate;
    @FXML
    private TableColumn<CustomerOrder, String> tcWarrantyLeft;
    @FXML
    private TableColumn<CustomerOrder, Integer> tcDue;
    @FXML    
    private TextField tfCustomerID;
    ObservableList<CustomerOrder> listCustomerOrder = FXCollections.observableArrayList();
    ObservableList<CustomerOrder> listCustomerService = FXCollections.observableArrayList();
    @FXML
    private TableView<CustomerOrder> tvCustomerService;
    @FXML
    private TableColumn<CustomerOrder, Integer> tcServiceProductID;
    @FXML
    private TableColumn<CustomerOrder, String> tcServiceBrand;
    @FXML
    private TableColumn<CustomerOrder, String> tcServiceModel;
    @FXML
    private TableColumn<CustomerOrder, Integer> tcServiceCharge;
    @FXML
    private TableColumn<CustomerOrder, String> tcServiceStatus;
    @FXML
    private TableColumn<CustomerOrder, String> tcServiceReceive;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfCustomerID.setText(DataManager.selected.getCustomerID()+"");
        showTotalOrder();
        showTotalServices();
    }

    private void showTotalOrder() {
        listCustomerOrder.clear();        
        try {
            String query;
            query = "SELECT Orders.orderID,Convert(DATE,Orders.orderTime) as orderDate,Product.productID,Product.Brand,"
                    + "Product.Model,,Product.Warranty, DATEDIFF(day, Convert(DATE,Orders.orderTime), Convert(DATE, getDate())) as dayDiff,(Product.Price - Orders.cost) as due "
                    + "from ORDERS INNER JOIN PRODUCT ON ORDERS.productID = PRODUCT.ProductID where ORDERS.customerID = " + DataManager.selected.getCustomerID();
            System.out.println("Button Show Order = " + query);
            Database db = new Database();
            db.connect();
            ResultSet rs = db.getResult(query);
            while (rs.next()) {
                int warranty = Integer.parseInt(rs.getString("Product.Warranty"));
                int count = Integer.parseInt(rs.getString("dayDiff"));
                System.out.println("count = "+count);
                if(warranty * 365 > count)
                {
                   listCustomerOrder.add(new CustomerOrder(rs.getInt("orderID"),rs.getString("orderDate"), rs.getInt("productID"),rs.getString("Brand"),rs.getString("Model"),rs.getString(count+" days"),rs.getInt("due")));
                   System.out.println("OrderId = " +rs.getInt("orderID") + "ProductID = "+rs.getInt("productID"));
                   System.out.println("Query = "+query);                   
                }
                else
                {
                    listCustomerOrder.add(new CustomerOrder(rs.getInt("orderID"),rs.getString("orderDate"), rs.getInt("productID"),rs.getString("Brand"),rs.getString("Model"),"Expired",rs.getInt("due")));
                    System.out.println("OrderId = " +rs.getInt("orderID") + "ProductID = "+rs.getInt("productID"));
                    System.out.println("Query = "+query); 
                }               
              
            }            
            tcOrderID.setCellValueFactory(new PropertyValueFactory("orderID"));
            tcDate.setCellValueFactory(new PropertyValueFactory("date"));
            tcProductID.setCellValueFactory(new PropertyValueFactory("productID"));
            tcBrand.setCellValueFactory(new PropertyValueFactory("Brand"));
            tcModel.setCellValueFactory(new PropertyValueFactory("Model"));
            tcDue.setCellValueFactory(new PropertyValueFactory("due"));
            tcModel.setCellValueFactory(new PropertyValueFactory("Model"));
            tvCustomerOrder.setItems(listCustomerOrder);                      
            
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("exception in showing total order:"+ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Action");
            alert.setContentText("Unable to show total orders by a customer");
            alert.show();
            //((Stage) tfCustomerID.getScene().getWindow()).close();
        }
    }

    private void showTotalServices() {
        listCustomerService.clear();        
        try {
            String query;
            query = "SELECT PRODUCT.productID,PRODUCT.Brand,PRODUCT.Model,SERVICE_.serviceCharge, SERVICE_.serviceStatus, Convert(DATE,SERVICE_.givenDate) as receiveDate FROM (SELECT * FROM ORDERS where customerID = "+DataManager.selected.getCustomerID()+") as ORDERS"+
                    "INNER JOIN PRODUCT ON ORDERS.productID = PRODUCT.productID"+
                    "INNER JOIN Service_ ON SERVICE_.productID = ORDERS.productID";
            System.out.println("Button Show Total Service = " + query);
            Database db = new Database();
            db.connect();
            ResultSet rs = db.getResult(query);
            while (rs.next()) {              
               listCustomerService.add(new CustomerOrder(rs.getInt("productID"),rs.getString("Brand"), rs.getString("Model"),rs.getInt("serviceCharge"),rs.getString("serviceStatus"),rs.getString("givenDate")));              
               System.out.println("Query Service= "+query);                              
            }            
            tcServiceProductID.setCellValueFactory(new PropertyValueFactory("productID"));
            tcServiceBrand.setCellValueFactory(new PropertyValueFactory("Brand"));                      
            tcServiceModel.setCellValueFactory(new PropertyValueFactory("Model"));
            tcServiceCharge.setCellValueFactory(new PropertyValueFactory("serviceCharge"));
            tcServiceStatus.setCellValueFactory(new PropertyValueFactory("status"));
            tcServiceReceive.setCellValueFactory(new PropertyValueFactory("givenDate"));
            tvCustomerOrder.setItems(listCustomerService);                     
            
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("exception in showing total order:"+ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Action");
            alert.setContentText("Unable to show total services by a customer");
            alert.show();
            //((Stage) tfCustomerID.getScene().getWindow()).close();
        }
    }

}
