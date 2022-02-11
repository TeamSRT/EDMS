/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Model.Product;
import Utility.DataManager;
import Utility.Database;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ktouf
 */
public class ProductUIOrderController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField tfProductID;
    @FXML
    private TextField tfQuantity;
    @FXML
    private TextField tfCustomerPhone;
    @FXML
    private TextField tfCustomerID;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfAddress;
    private boolean isCustomer = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfProductID.setText(DataManager.product.getProductID() + "");
        tfProductID.setEditable(false);
    }

    @FXML
    private void btnSearchOnClick(ActionEvent event) throws ClassNotFoundException, SQLException {
        if (tfCustomerPhone.getText().trim().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Action");
            alert.setContentText("Insert a Phone Number to search!");
            alert.show();
            return;
        }
        String query = "SELECT * FROM CUSTOMER WHERE phoneNumber = " + tfCustomerPhone.getText().trim();
        Database db = new Database();
        db.connect();
        ResultSet rs = db.getResult(query);

        while (rs.next()) {
            isCustomer = true;
            tfCustomerID.setText(rs.getInt("customerID") + "");
            tfName.setText(rs.getString("customerName"));
            tfAddress.setText("customerAddress");
            tfCustomerID.setEditable(false);
            tfName.setEditable(false);
            tfAddress.setEditable(false);
        }
        db.disconnect();
        if (!isCustomer) {
            tfCustomerID.setEditable(false);
            tfCustomerID.setVisible(false);
            tfAddress.setLayoutX(tfName.getLayoutX());
            tfAddress.setLayoutY(tfName.getLayoutY());
            tfName.setLayoutX(tfCustomerID.getLayoutX());
            tfName.setLayoutY(tfCustomerID.getLayoutY());
        }
    }

    @FXML
    private void btnOrderOnClick(ActionEvent event) throws ClassNotFoundException, SQLException {
        Database db = new Database();
        db.connect();
        if (!isCustomer) {
            String queryCustomer = "INSERT INTO CUSTOMER(customerName, phoneNumber, Address) VALUES "
                    + "(" + tfName.getText()
                    + "," + tfCustomerPhone.getText()
                    + "," + tfAddress.getText() + ")";
            db.updateTable(queryCustomer);
        }
        String queryOrder = "INSERT INTO ORDERS(customerID, ProductID, Quantity) VALUES "
                + "(" + tfCustomerID.getText()
                + "," + tfProductID.getText()
                + "," + tfQuantity.getText() + ")";
        db.updateTable(queryOrder);
        db.disconnect();
        ((Stage) tfAddress.getScene().getWindow()).close();
    }

    @FXML
    private void btnCancelOnClick(ActionEvent event) {
        ((Stage) tfAddress.getScene().getWindow()).close();
    }

}
