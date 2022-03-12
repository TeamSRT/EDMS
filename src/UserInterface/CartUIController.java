/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Model.CartItem;
import Model.Product;
import Utility.DataManager;
import Utility.Database;
import Utility.SceneLoader;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ktouf
 */
public class CartUIController implements Initializable {

    @FXML
    private TextField tfSearchPhone;
    @FXML
    private TextField tfSearchID;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfPhone;
    @FXML
    private TextField tfAddress;
    @FXML
    private TableColumn<CartItem, String> tcProductID;
    @FXML
    private TableColumn<CartItem, String> tcQuantity;
    @FXML
    private TableColumn<CartItem, String> tcPaidAmount;
    ObservableList<CartItem> cartList = FXCollections.observableArrayList();
    public List<CartItem> orderList = new ArrayList<CartItem>();
    @FXML
    private TableView<CartItem> tvOrders;
    @FXML
    private Button btnInsert;
    @FXML
    private Button btnClearFields;
    private int customerID = -1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        orderList = DataManager.orderList;
        loadTables();
    }

    public void loadTables() {
        cartList.clear();
        for (int i = 0; i < orderList.size(); ++i) {
            cartList.add(orderList.get(i));
        }
        tcProductID.setCellValueFactory(new PropertyValueFactory<>("productID"));
        tcQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tcPaidAmount.setCellValueFactory(new PropertyValueFactory<>("payment"));

        tvOrders.setItems(cartList);
    }

    @FXML
    private void btnSearchPhoneOnClick(ActionEvent event) throws ClassNotFoundException, SQLException {
        if (tfSearchPhone.getText().trim().length() <= 0) {
            new SceneLoader().showAlert(Alert.AlertType.ERROR, "Invalid Input", "Insert a phone number!");
        }
        String query = "SELECT * FROM CUSTOMER WHERE customerPhone = " + tfSearchPhone.getText().trim();
        Database db = new Database();
        db.connect();
        ResultSet rs = db.getResult(query);
        if (rs.next()) {
            customerID = rs.getInt("customerID");
            tfName.setText(rs.getString("customerName"));
            tfPhone.setText(rs.getString("customerPhone"));
            tfAddress.setText(rs.getString("addresses"));
            btnInsert.setDisable(true);
        }

    }

    @FXML
    private void btnSearchIDOnClick(ActionEvent event) throws ClassNotFoundException, SQLException {
        if (tfSearchID.getText().trim().length() <= 0) {
            new SceneLoader().showAlert(Alert.AlertType.ERROR, "Invalid Input", "Insert a ID!");
        }
        String query = "SELECT * FROM CUSTOMER WHERE customerID = " + tfSearchID.getText().trim();
        Database db = new Database();
        db.connect();
        ResultSet rs = db.getResult(query);
        if (rs.next()) {
            customerID = rs.getInt("customerID");
            tfName.setText(rs.getString("customerName"));
            tfPhone.setText(rs.getString("customerPhone"));
            tfAddress.setText(rs.getString("addresses"));
            btnInsert.setDisable(true);
        }

    }

    @FXML
    private void btnInsertNewOnAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        String name = tfName.getText().trim();
        String phone = tfPhone.getText().trim();
        String address = tfAddress.getText().trim();
        if (name.equals("")) {
            new SceneLoader().showAlert(Alert.AlertType.ERROR, "Invalid Input", "You can not keep the name field empty!");
            return;
        }
        if (phone.equals("")) {
            new SceneLoader().showAlert(Alert.AlertType.ERROR, "Invalid Input", "You can not keep the phone field empty!");
            return;
        }

        String query = "INSERT INTO CUSTOMER(customerName,customerPhone,addresses) VALUES ('" + name + "','" + phone + "','" + address + "')";
        Database db = new Database();
        db.connect();
        try {
            ResultSet rs = db.updateTableWithKeys(query);
            if (rs.next()) {
                customerID = rs.getInt(1);
            }
            new SceneLoader().showAlert(Alert.AlertType.INFORMATION, "Customer Inserted", "New customer added!");
        } catch (SQLException ex) {
            new SceneLoader().showAlert(Alert.AlertType.ERROR, "Action failed", "New customer Insertion failed!");
            return;
        }
        db.disconnect();
    }

    @FXML
    private void btnPlaceOrderOnClick(ActionEvent event) throws ClassNotFoundException, SQLException {
        if (customerID == -1) {
            new SceneLoader().showAlert(Alert.AlertType.ERROR, "Invalid Input", "Search or Insert a Customer!");
            return;
        }
        for (int i = 0; i < orderList.size(); ++i) {
            String query = "INSERT INTO ORDERS (customerID, productID, quantity) VALUES "
                    + "(" + customerID + "," + orderList.get(i).getProductID() + "," + orderList.get(i).getQuantity() + ")";

            Database db = new Database();
            db.connect();
            db.updateTable(query);
            query = "UPDATE PRODUCT SET Stock = Stock - " + orderList.get(i).getQuantity() + " WHERE ProductID = " + orderList.get(i).getProductID();
            db.updateTable(query);
            db.disconnect();
        }
        new SceneLoader().showAlert(Alert.AlertType.INFORMATION, "Order Placed", "All the orders have been placed!");
        orderList.clear();
        ((Stage) tfAddress.getScene().getWindow()).close();
    }

    @FXML
    private void btnDeleteSelectedOnAction(ActionEvent event) {
        CartItem curr = tvOrders.getSelectionModel().getSelectedItem();
        if (curr == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Action");
            alert.setContentText("Select a item from table to delete!");
            alert.show();
            return;
        }
        int index = tvOrders.getSelectionModel().getSelectedIndex();
        orderList.remove(index);
        loadTables();

    }

    @FXML
    private void btnClearAllOnAction(ActionEvent event) {
        orderList.clear();
        loadTables();
    }

    @FXML
    private void btnClearFieldsOnClick(ActionEvent event) {
        tfName.setText("");
        tfPhone.setText("");
        tfAddress.setText("");
        btnInsert.setDisable(false);
    }

}
