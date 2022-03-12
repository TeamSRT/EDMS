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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
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
    @FXML
    private CheckBox cbMakeTransaction;
    @FXML
    private TextField tfTransactionID;
    @FXML
    private TextField tfTransactionType;
    @FXML
    private TextField tfTransactionAmount;
    @FXML
    private Text lblCustomerID;
    @FXML
    private Text lblCustomerName;
    @FXML
    private Text lblAddress;    
    private ProductUIController productTableView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfProductID.setText(DataManager.product.getProductID() + "");
        tfProductID.setEditable(false);

        tfQuantity.setText("0");
        tfQuantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    tfQuantity.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        tfTransactionAmount.setText("0");
        tfTransactionAmount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    tfTransactionAmount.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        
        tfTransactionType.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!newValue.matches("[A-Za-z]")) {
                    tfTransactionType.setText(newValue.replaceAll("[^A-Za-z]", ""));
                }
            }
        });

    }

    @FXML
    private void btnSearchOnClick(ActionEvent event) throws ClassNotFoundException, SQLException {
        tfName.setText("");
        tfAddress.setText("");
        if (tfCustomerPhone.getText().trim().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Action");
            alert.setContentText("Insert a Phone Number to search!");
            alert.show();
            return;
        }
        isCustomer = false;
        String query = "SELECT * FROM CUSTOMER WHERE customerPhone = " + tfCustomerPhone.getText().trim();
        Database db = new Database();
        db.connect();
        ResultSet rs = db.getResult(query);
        while (rs.next()) {
            isCustomer = true;
            tfCustomerID.setText(rs.getInt("customerID") + "");
            tfName.setText(rs.getString("customerName"));
            tfAddress.setText(rs.getString("addresses"));
            tfName.setEditable(false);
            tfAddress.setEditable(false);
            tfCustomerID.setEditable(false);
            tfCustomerID.setDisable(false);
            tfCustomerID.setVisible(true);
            lblCustomerID.setVisible(true);
        }
        db.disconnect();
        if (!isCustomer) {
            lblCustomerID.setVisible(false);
            tfCustomerID.setVisible(false);
            tfCustomerID.setDisable(true);
            tfName.setEditable(true);
            tfAddress.setEditable(true);
            tfName.setDisable(false);
            tfAddress.setDisable(false);
        }
    }

    @FXML
    private void btnOrderOnClick(ActionEvent event) throws ClassNotFoundException, SQLException {
        Database db = new Database();
        db.connect();
        String check = checkFields();
        if (!check.equals("Okay!")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Action");
            alert.setContentText(check);
            alert.show();
            return;
        }
        String customerID = tfCustomerID.getText();
        if (!isCustomer) {
            String queryCustomer = "INSERT INTO CUSTOMER(customerName, customerPhone, addresses) VALUES "
                    + "('" + tfName.getText()
                    + "','" + tfCustomerPhone.getText()
                    + "','" + tfAddress.getText() + "')";
            ResultSet rs = db.updateTableWithKeys(queryCustomer);
            if (rs.next()) {
                customerID = rs.getInt(1) + "";
                System.out.println("CustomerID = " + customerID);
            }
        }
        String queryOrder = "INSERT INTO ORDERS(customerID, ProductID, quantity) VALUES "
                + "(" + customerID
                + "," + tfProductID.getText()
                + "," + tfQuantity.getText() + ")";
        
        ResultSet rsOrderID = db.updateTableWithKeys(queryOrder);
        int orderID = 0;
        if (rsOrderID.next()) {
            orderID = rsOrderID.getInt(1);
            System.out.println("orderID = " + orderID);
        }
        if (cbMakeTransaction.isSelected()) {
            String queryTrx = "INSERT INTO TRANSACTION_(TransactionID, OrderID, Type_, Amount) VALUES"
                    + "(" + tfTransactionID.getText()
                    + "," + orderID
                    + ",'" + tfTransactionType.getText()
                    + "'," + tfTransactionAmount.getText() + ")";
            db.updateTable(queryTrx);
        }
        int stock = Integer.parseInt(DataManager.product.getStock());
        try {
            stock -= Integer.parseInt(tfQuantity.getText());
            System.out.println("CHECK STOCK: " + stock);
            String query = "UPDATE PRODUCT SET Stock = " + stock + " WHERE ProductID = " + tfProductID.getText();
            db.updateTable(query);
        } catch (SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Stock Update Error");
            alert.setContentText("There was a problem updating stock in Product table!");
            alert.show();
            return;
        }
        db.disconnect();
        ((Stage) tfAddress.getScene().getWindow()).close();
        productTableView.loadProductTable("SELECT * FROM PRODUCT");
    }

    @FXML
    private void btnCancelOnClick(ActionEvent event) {
        ((Stage) tfAddress.getScene().getWindow()).close();
    }

    @FXML
    private void cbTransactionOnClicked(ActionEvent event) {
        if (cbMakeTransaction.isSelected()) {
            tfTransactionAmount.setDisable(false);
            tfTransactionID.setDisable(false);
            tfTransactionType.setDisable(false);
        } else {
            tfTransactionAmount.setDisable(true);
            tfTransactionID.setDisable(true);
            tfTransactionType.setDisable(true);
        }
    }

    private String checkFields() {
        int quantity = Integer.parseInt(tfQuantity.getText());
        if (quantity <= 0) {
            return "Quantity cannot be less than 1!";
        }
        if (quantity > Integer.parseInt(DataManager.product.getStock())) {
            return "Quantity cannot exceed stock!";
        }
        if (tfCustomerPhone.getText().trim().length() == 0) {
            return "Phone Number cannot be empty!";
        }
        if (tfName.getText().trim().length() == 0) {
            return "Customer Name cannot be empty!";
        }
        if (cbMakeTransaction.isSelected()) {
            if (Integer.parseInt(tfTransactionAmount.getText()) <= 0) {
                return "Amount cannot be less than 1!";
            }
            if (tfTransactionType.getText().trim().length() == 0) {
                return "Transaction Type cannot be empty!";
            }
            if (tfTransactionID.getText().trim().length() == 0) {
                return "Transaction ID cannot be empty!";
            }
        }
        return "Okay!";
    }

    public void setProductTableView(ProductUIController productTableView) {
        this.productTableView = productTableView;
    }
    
    

}
