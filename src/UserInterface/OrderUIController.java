/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Model.Order;
import Utility.Database;
import Utility.SceneLoader;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author ktouf
 */
public class OrderUIController implements Initializable {

    @FXML
    private Button btnInsert;
    @FXML
    private Button btnDelete;
    @FXML
    private TableView<Order> tvOrder;
    @FXML
    private TableColumn<Order, String> tcOrderID;
    @FXML
    private TableColumn<Order, String> tcCustomerID;
    @FXML
    private TableColumn<Order, String> tcProductID;
    @FXML
    private TableColumn<Order, String> tcQuantity;
    @FXML
    private TableColumn<Order, String> tcTime;

    ObservableList<Order> listOrder = FXCollections.observableArrayList();
    @FXML
    private TextField tfOrderID;
    @FXML
    private TextField tfCustomerID;
    @FXML
    private TextField tfProductID;
    @FXML
    private TextField tfQuantity;
    @FXML
    private Button btnModify;
    private String searchBy = "";
    private boolean isEdit = false;
    @FXML
    private Label lblOrderID;
    @FXML
    private TableColumn<Order, String> tcCost;
    @FXML
    private TextField tfCost;
    @FXML
    private TextField tfSearch;
    @FXML
    private TextField tfSearch2;
    @FXML
    private MenuButton menuBtnSearch;
    @FXML
    private MenuItem mitemOrderID;
    @FXML
    private MenuItem mitemCustomerID;
    @FXML
    private MenuItem mitemProductID;
    @FXML
    private MenuItem mitemQuantity;
    @FXML
    private MenuItem mitemCost;
    @FXML
    private Label lblMaxAlert;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        searchBy = "orderID";
        loadOrderTable("SELECT * FROM ORDERS");

        tfQuantity.setText("0");
        tfQuantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    tfQuantity.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    private void loadOrderTable(String query) {
        listOrder.clear();
        ResultSet rsOrder = null;
        try {
            Database db = new Database();
            db.connect();
            rsOrder = db.getResult(query);
            while (rsOrder.next()) {
                listOrder.add(new Order(rsOrder.getInt("orderID"), rsOrder.getInt("customerID"), rsOrder.getInt("productID"),
                        rsOrder.getInt("quantity"), rsOrder.getTimestamp("orderTime"), rsOrder.getInt("Cost")));
            }
            db.disconnect();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductUIController.class.getName()).log(Level.SEVERE, null, ex);
        }

        tcOrderID.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        tcCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        tcProductID.setCellValueFactory(new PropertyValueFactory<>("productID"));
        tcQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tcTime.setCellValueFactory(new PropertyValueFactory<>("orderTime"));
        tcCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        tvOrder.setItems(listOrder);

    }

    @FXML
    private void btnInsertOnClick(ActionEvent event) throws ClassNotFoundException, SQLException {
        String orderID = tfOrderID.getText();
        String productID = tfProductID.getText();
        String customerID = tfCustomerID.getText();
        String quantity = tfQuantity.getText();
        String cost = tfCost.getText();
        int stock = 0;
        System.out.println(orderID);
        String query;
        Database db = new Database();
        db.connect();
        try {
            query = "SELECT Stock FROM PRODUCT WHERE ProductID = " + productID;
            ResultSet rs = db.getResult(query);
            if (rs.next()) {
                stock = rs.getInt("Stock");
            }
            if (stock < Integer.parseInt(quantity) || Integer.parseInt(quantity) == 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Input");
                alert.setContentText("Invalid quantity amount!");
                alert.show();
                return;
            }
            if (cost.equals("")) {
                new SceneLoader().showAlert(Alert.AlertType.ERROR, "Invalid Input", "Insert cost!");
                return;
            }
            if (Integer.parseInt(cost) <= 0) {
                new SceneLoader().showAlert(Alert.AlertType.ERROR, "Invalid Input", "Cost must be higher than 0!");
                return;
            }
            if (isEdit) {
                query = "UPDATE ORDERS SET customerID = " + customerID + ", productID = " + productID + ", quantity = " + quantity + ", Cost = " + cost + " WHERE orderID = " + tfOrderID.getText();
            } else {
                query = "INSERT INTO ORDERS (customerID, productID, quantity, Cost) VALUES (" + customerID + "," + productID + "," + quantity + "," +cost+")";
            }
            System.out.println(query);

            db.updateTable(query);
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            if (isEdit) {
                alert.setContentText("Modification failed. Insert valid data!");
            } else {
                alert.setContentText("Insertion failed. Insert valid data!");
            }
            alert.show();
            return;
        }
        try {
            stock -= Integer.parseInt(quantity);
            query = "UPDATE PRODUCT SET Stock = " + stock + " WHERE ProductID = " + productID;
            db.updateTable(query);
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Stock Update Error");
            alert.setContentText("There was a problem updating stock in Product table!");
            alert.show();
            return;
        }
        db.disconnect();
        loadOrderTable("SELECT * FROM ORDERS");
        resetFields();
    }

    @FXML
    private void btnDeleteOnClick(ActionEvent event) throws ClassNotFoundException, SQLException {
        Order curr = tvOrder.getSelectionModel().getSelectedItem();
        if (curr == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Action");
            alert.setContentText("Select a product from table to delete!");
            alert.show();
            return;
        }
        String query = "DELETE FROM ORDERS WHERE orderID = " + curr.getOrderID();
        Database db = new Database();
        db.connect();
        db.updateTable(query);
        db.disconnect();
        loadOrderTable("SELECT * FROM ORDERS");
    }

    @FXML
    private void btnModifyOnClick(ActionEvent event) {
        Order curr = tvOrder.getSelectionModel().getSelectedItem();
        if (curr == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Action");
            alert.setContentText("Select a product from table to modify!");
            alert.show();
            return;
        }
        
        tfProductID.setText(curr.getProductID() + "");
        tfCustomerID.setText(curr.getCustomerID() + "");
        tfQuantity.setText(curr.getQuantity() + "");
        tfCost.setText(curr.getCost()+"");
        tfOrderID.setDisable(false);
        tfOrderID.setOpacity(1);
        tfOrderID.setEditable(false);
        tfOrderID.setText(curr.getOrderID() + "");
        lblOrderID.setDisable(false);
        lblOrderID.setOpacity(1);

        isEdit = true;
        btnInsert.setText("Modify");

    }

    private void resetFields() {
        tfProductID.setText("");
        tfCustomerID.setText("");
        tfQuantity.setText("");
        tfCost.setText("");
        isEdit = false;
        tfOrderID.setDisable(true);
        tfOrderID.setOpacity(0);
        lblOrderID.setDisable(true);
        lblOrderID.setOpacity(0);
        lblMaxAlert.setOpacity(0);
        btnInsert.setText("Insert");
    }

    @FXML
    private void btnShowOrderOnClicked(ActionEvent event) {
        Order curr = tvOrder.getSelectionModel().getSelectedItem();
        String query;
        if (curr == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Action");
            alert.setContentText("Select a customer from table to see his order!");
            alert.show();
            query = "SELECT * FROM ORDERS";
        } else {
            query = "SELECT * FROM ORDERS WHERE customerID = " + curr.getCustomerID();
        }
        loadOrderTable(query);
    }

    @FXML
    private void btnClearOnClick(ActionEvent event) {
        resetFields();
    }

    @FXML
    private void searchOnKeyRelease(KeyEvent event) {
        search();
    }

    @FXML
    private void searchByOrderID(ActionEvent event) {
        tfSearch2.setDisable(true);
        tfSearch2.setOpacity(0);
        searchBy = "orderID";
        menuBtnSearch.setText("Order ID");       
        tfSearch.setPromptText("Search by Order ID");
        search();
    }

    @FXML
    private void searchByCustomerID(ActionEvent event) {
        tfSearch2.setDisable(true);
        tfSearch2.setOpacity(0);
        searchBy = "customerID";
        menuBtnSearch.setText("Customer ID");       
        tfSearch.setPromptText("Search by Customer ID");
        search(); 
    }

    @FXML
    private void searchByProductID(ActionEvent event){
        tfSearch2.setDisable(true);
        tfSearch2.setOpacity(0);
        searchBy = "productID";
        menuBtnSearch.setText("Product ID");       
        tfSearch.setPromptText("Search by Product ID");
        search();  
    }

    @FXML
    private void searchByQuantity(ActionEvent event){
        searchBy = "quantity";
        menuBtnSearch.setText("Quantity");       
        tfSearch.setPromptText("Search by minimum quantity");
        tfSearch2.setOpacity(1);
        tfSearch2.setPromptText("Search by maximum quantity");
        tfSearch2.setDisable(false);
        search();
    }

    @FXML
    private void searchByCost(ActionEvent event) {
        searchBy = "cost";
        menuBtnSearch.setText("Cost");       
        tfSearch.setPromptText("Search by minimum cost");
        tfSearch2.setOpacity(1);
        tfSearch2.setPromptText("Search by maximum cost");
        tfSearch2.setDisable(false);
        search();
    }

    private void search() {
         if(tfSearch.getText().equals(""))
        {
            System.out.println("No text in the search field");
            loadOrderTable("SELECT * FROM ORDERS");  
        }       
        else
        {
            String query;
            switch (searchBy) {               
                case "productID":
                    query = "SELECT * FROM ORDERS WHERE productID LIKE '%"+tfSearch.getText()+"%'";
                    System.out.println("ProductID chosen");
                    break;
                case "customerID":
                    query = "SELECT * FROM ORDERS WHERE customerID LIKE '%"+tfSearch.getText()+"%'";
                    System.out.println("CustomerID Chosen");
                    break;               
                case "quantity":
                     int quantityMin = Integer.parseInt(tfSearch.getText().equals("") ? "0" : tfSearch.getText());
                     int quantityMax = Integer.parseInt(tfSearch2.getText().equals("") ? "999999" : tfSearch2.getText());
                     lblMaxAlert.setOpacity(0);
                     if (quantityMin > quantityMax) {
                        lblMaxAlert.setOpacity(1);
                    }
                    query = "SELECT * FROM ORDERS WHERE quantity >= "+quantityMin+" AND quantity <= "+quantityMax;        
                    System.out.println("Quantity query = "+query);
                    break;
                 case "cost":
                     int costMin = Integer.parseInt(tfSearch.getText().equals("") ? "0" : tfSearch.getText());
                     int costMax = Integer.parseInt(tfSearch2.getText().equals("") ? "999999" : tfSearch2.getText());
                     if (costMin > costMax) {
                          new SceneLoader().showAlert(Alert.AlertType.ERROR, "Invalid Input", "Minimum price cannot be larger than Max");
                         return;
                    }
                    query = "SELECT * FROM ORDERS WHERE Cost >= "+costMin+" AND Cost <= "+costMax;        
                    System.out.println("Cost query = "+query);
                    break;    
                default:   
                    query = "SELECT * FROM ORDERS WHERE orderID LIKE '%"+tfSearch.getText()+"%'";                          
                    System.out.println("Order ID chosen");
                    break;            
            }
            loadOrderTable(query);            
        }
    }
}
