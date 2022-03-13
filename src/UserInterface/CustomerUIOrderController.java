/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Model.Customer;
import Model.CustomerOrder;
import Model.CustomerService;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
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
    private TableColumn<CustomerOrder, String> tcOrderID;
    @FXML
    private TableColumn<CustomerOrder, String> tcProductID;
    @FXML
    private TableColumn<CustomerOrder, String> tcBrand;
    @FXML
    private TableColumn<CustomerOrder, String> tcDate;
    @FXML
    private TableColumn<CustomerOrder, String> tcWarrantyLeft;
    @FXML
    private TableColumn<CustomerOrder, String> tcDue;
    @FXML
    private TextField tfCustomerID;
    ObservableList<CustomerOrder> listCustomerOrder = FXCollections.observableArrayList();
    ObservableList<CustomerService> listCustomerService = FXCollections.observableArrayList();
    @FXML
    private TableView<CustomerService> tvCustomerService;
    @FXML
    private TableColumn<CustomerService, String> tcServiceProductID;
    @FXML
    private TableColumn<CustomerService, String> tcServiceBrand;
    @FXML
    private TableColumn<CustomerService, String> tcServiceModel;
    @FXML
    private TableColumn<CustomerService, String> tcServiceCharge;
    @FXML
    private TableColumn<CustomerService, String> tcServiceStatus;
    @FXML
    private TableColumn<CustomerService, String> tcServiceReceive;
    @FXML
    private TextField tfTotalOrder;
    @FXML
    private TextField tfTotalServices;
    @FXML
    private MenuButton menuBtnSearch;
    @FXML
    private MenuItem mitemBrand;
    @FXML
    private MenuItem mItemModel;
    @FXML
    private TextField tfSearch;
     private String searchBy = "";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        searchBy = "brand";
        showTotalOrder("SELECT Orders.orderID,Convert(DATE,Orders.orderTime) as orderDate,Product.productID,Product.Brand,"
                    + "Product.Model,PRODUCT.Warranty, DATEDIFF(day, Convert(DATE,Orders.orderTime), Convert(DATE, getDate())) as dayDiff,(Product.Price * Orders.quantity - Orders.cost) as due "
                    + "from ORDERS INNER JOIN PRODUCT ON ORDERS.productID = PRODUCT.ProductID where ORDERS.customerID = " + DataManager.selected.getCustomerID());
        showTotalServices("SELECT PRODUCT.productID,PRODUCT.Brand,PRODUCT.Model,SERVICE_.serviceCharge, SERVICE_.serviceStatus, Convert(DATE,SERVICE_.givenDate) as receiveDate "
                    + "FROM SERVICE_ "
                    + "LEFT JOIN PRODUCT ON Service_.productID = PRODUCT.productID "
                    + "where Service_.customerID = " + DataManager.selected.getCustomerID());
        tfCustomerID.setText(DataManager.selected.getCustomerID() + "");

    }

    private void showTotalOrder(String query) {
        listCustomerOrder.clear();
        try {                       
            System.out.println("Button Show Order = " + query);            
            Database db = new Database();
            db.connect();
            ResultSet rs = db.getResult(query);
            while (rs.next()) {                
                int warranty = Integer.parseInt(rs.getString("Warranty"));
                int count = warranty * 365 - Integer.parseInt(rs.getString("dayDiff"));
                if (count > 0) {
                    listCustomerOrder.add(new CustomerOrder(rs.getInt("orderID"), rs.getString("orderDate"),
                            rs.getInt("productID"), rs.getString("Brand"), rs.getString("Model"), (count + " days"),
                            rs.getInt("due")));

                } else {
                    listCustomerOrder.add(new CustomerOrder(rs.getInt("orderID"), rs.getString("orderDate"),
                            rs.getInt("productID"), rs.getString("Brand"), rs.getString("Model"), "Expired",
                            rs.getInt("due")));
                }
            }

        } catch (ClassNotFoundException | SQLException ex) {            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Action");
            alert.setContentText("Unable to show total orders by a customer");
            alert.show();
            ((Stage) tfCustomerID.getScene().getWindow()).close();
        }
        tcOrderID.setCellValueFactory(new PropertyValueFactory("orderID"));
        tcDate.setCellValueFactory(new PropertyValueFactory("date"));
        tcProductID.setCellValueFactory(new PropertyValueFactory("productID"));
        tcBrand.setCellValueFactory(new PropertyValueFactory("Brand"));
        tcModel.setCellValueFactory(new PropertyValueFactory("Model"));
        tcDue.setCellValueFactory(new PropertyValueFactory("due"));
        tcWarrantyLeft.setCellValueFactory(new PropertyValueFactory("warrantyRemain"));
        tfTotalOrder.setText(listCustomerOrder.size() + "");
        tvCustomerOrder.setItems(listCustomerOrder);
    }

    private void showTotalServices(String query) {
        listCustomerService.clear();
        try {                       
            System.out.println("Button Show Total Service = " + query);
            Database db = new Database();
            db.connect();
            ResultSet rs = db.getResult(query);
            while (rs.next()) {
                listCustomerService.add(new CustomerService(rs.getInt("productID"), rs.getString("Brand"), rs.getString("Model"),
                        rs.getInt("serviceCharge"), rs.getString("serviceStatus"), rs.getString("receiveDate")));
                //System.out.println("Query Service= " + query);
            }
            tcServiceProductID.setCellValueFactory(new PropertyValueFactory("productID"));
            tcServiceBrand.setCellValueFactory(new PropertyValueFactory("Brand"));
            tcServiceModel.setCellValueFactory(new PropertyValueFactory("Model"));
            tcServiceCharge.setCellValueFactory(new PropertyValueFactory("serviceCharge"));
            tcServiceStatus.setCellValueFactory(new PropertyValueFactory("status"));
            tcServiceReceive.setCellValueFactory(new PropertyValueFactory("givenDate"));
            tfTotalServices.setText(listCustomerService.size()+"");
            tvCustomerService.setItems(listCustomerService);

        } catch (ClassNotFoundException | SQLException ex) {           
//System.out.println("exception in showing total order:" + ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Action");
            alert.setContentText("Unable to show total services by a customer");
            alert.show();
            ((Stage) tfCustomerID.getScene().getWindow()).close();
        }
    }

    @FXML
    private void searchOnKeyRelease(KeyEvent event) {
        search();
    }

    @FXML
    private void searchByBrand(ActionEvent event) {
        searchBy = "brand";
        menuBtnSearch.setText("Brand");       
        tfSearch.setPromptText("Search by Brand");
        search();
    }

    @FXML
    private void searchByModel(ActionEvent event){
        searchBy = "model";
        menuBtnSearch.setText("model");       
        tfSearch.setPromptText("Search by Model");
        search();
    }

    private void search() {
        if(tfSearch.getText().equals(""))
        {
            System.out.println("No text in the search field");
            showTotalServices("SELECT PRODUCT.productID,PRODUCT.Brand,PRODUCT.Model,SERVICE_.serviceCharge, SERVICE_.serviceStatus, Convert(DATE,SERVICE_.givenDate) as receiveDate "
                    + "FROM SERVICE_ "
                    + "LEFT JOIN PRODUCT ON Service_.productID = PRODUCT.productID "
                    + "where Service_.customerID = " + DataManager.selected.getCustomerID());
            showTotalOrder("SELECT Orders.orderID,Convert(DATE,Orders.orderTime) as orderDate,Product.productID,Product.Brand,"
                    + "Product.Model,PRODUCT.Warranty, DATEDIFF(day, Convert(DATE,Orders.orderTime), Convert(DATE, getDate())) as dayDiff,(Product.Price * Orders.quantity - Orders.cost) as due "
                    + "from ORDERS INNER JOIN PRODUCT ON ORDERS.productID = PRODUCT.ProductID where ORDERS.customerID = " + DataManager.selected.getCustomerID());
        }
        else
        {
            String query;
            switch (searchBy) {               
                case "model":
                   query = "SELECT PRODUCT.productID,PRODUCT.Brand,PRODUCT.Model,SERVICE_.serviceCharge, SERVICE_.serviceStatus, Convert(DATE,SERVICE_.givenDate) as receiveDate "
                    + "FROM SERVICE_ "
                    + "LEFT JOIN PRODUCT ON Service_.productID = PRODUCT.productID "
                    + "where Product.model = " + tfSearch.getText();
                   
                   query = "SELECT Orders.orderID,Convert(DATE,Orders.orderTime) as orderDate,Product.productID,Product.Brand,"
                    + "Product.Model,PRODUCT.Warranty, DATEDIFF(day, Convert(DATE,Orders.orderTime), Convert(DATE, getDate())) as dayDiff,(Product.Price * Orders.quantity - Orders.cost) as due "
                    + "from ORDERS INNER JOIN PRODUCT ON ORDERS.productID = PRODUCT.ProductID where PRODUCT.model = " + tfSearch.getText();
                   
                    break;                
                default:   
                    query = "SELECT PRODUCT.productID,PRODUCT.Brand,PRODUCT.Model,SERVICE_.serviceCharge, SERVICE_.serviceStatus, Convert(DATE,SERVICE_.givenDate) as receiveDate "
                    + "FROM SERVICE_ "
                    + "LEFT JOIN PRODUCT ON Service_.productID = PRODUCT.productID "
                    + "where Product.Brand = " + tfSearch.getText();
                   
                   query = "SELECT Orders.orderID,Convert(DATE,Orders.orderTime) as orderDate,Product.productID,Product.Brand,"
                    + "Product.Model,PRODUCT.Warranty, DATEDIFF(day, Convert(DATE,Orders.orderTime), Convert(DATE, getDate())) as dayDiff,(Product.Price * Orders.quantity - Orders.cost) as due "
                    + "from ORDERS INNER JOIN PRODUCT ON ORDERS.productID = PRODUCT.ProductID where PRODUCT.Brand = " + tfSearch.getText();
                   
                    break;            
            }
            
            showTotalServices(query);
            showTotalOrder(query);
        }
        
    }

}
