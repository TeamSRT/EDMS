/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Model.Order;
import Model.Product;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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
    
    private boolean isEdit = false;
    @FXML
    private Label lblOrderID;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadOrderTable();
        // TODO
    }

    private void loadOrderTable() {
        listOrder.clear();
        ResultSet rsOrder = null;
        try {
            String query = "SELECT * FROM ORDERS";
            Database db = new Database();
            db.connect();
            rsOrder = db.getResult(query);
            while (rsOrder.next()) {
                listOrder.add(new Order(rsOrder.getInt("orderID"), rsOrder.getInt("customerID"), rsOrder.getInt("productID"),
                        rsOrder.getInt("quantity"), rsOrder.getTimestamp("orderTime")));
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
        
        tvOrder.setItems(listOrder);
        
    }

    @FXML
    private void btnInsertOnClick(ActionEvent event) throws ClassNotFoundException, SQLException {
        String orderID = tfOrderID.getText();
        String productID = tfProductID.getText();
        String customerID = tfCustomerID.getText();
        String quantity = tfQuantity.getText();
        System.out.println(orderID);
        String query;
        if (isEdit) {
            query = "UPDATE ORDERS SET customerID = " + customerID + ", productID = " + productID + ", quantity = " + quantity + " WHERE orderID = " + tfOrderID.getText();
        } else {
            query = "INSERT INTO ORDERS (customerID, productID, quantity) VALUES (" + customerID + "," + productID + "," + quantity + ")";
        }
        System.out.println(query);
        Database db = new Database();
        db.connect();
        db.updateTable(query);
        db.disconnect();
        loadOrderTable();
        resetFields();
    }

    @FXML
    private void btnDeleteOnClick(ActionEvent event) {
    }

    @FXML
    private void btnModifyOnClick(ActionEvent event) {
        Order curr = tvOrder.getSelectionModel().getSelectedItem();
        if (curr == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Action");
            alert.setContentText("Select a product from table to delete!");
            alert.show();
            return;
        }
        tfProductID.setText(curr.getProductID() + "");
        tfCustomerID.setText(curr.getCustomerID() + "");
        tfQuantity.setText(curr.getQuantity() + "");
        
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
        isEdit = false;
        tfOrderID.setDisable(true);
        tfOrderID.setOpacity(0);
        lblOrderID.setDisable(true);
        lblOrderID.setOpacity(0);
        btnInsert.setText("Insert");
    }
}