/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author ktouf
 */
public class ProductUIController implements Initializable {

    @FXML
    private TableView<Product> tvProduct;
    @FXML
    private TableColumn<Product, String> tcBrand;
    @FXML
    private TableColumn<Product, String> tcModel;
    @FXML
    private TableColumn<Product, String> tcWarranty;
    @FXML
    private TableColumn<Product, String> tcPrice;
    @FXML
    private TableColumn<Product, String> tcDescription;
    @FXML
    private TableColumn<Product, String> tcStock;
    @FXML
    private TableColumn<Product, String> tcProductID;

    ObservableList<Product> listProduct = FXCollections.observableArrayList();
    @FXML
    private TextField tfProductID;
    @FXML
    private TextField tfBrand;
    @FXML
    private TextField tfModel;
    @FXML
    private TextField tfWarranty;
    @FXML
    private TextField tfPrice;
    @FXML
    private TextField tfStock;
    @FXML
    private TextArea tfDescription;
    @FXML
    private Button btnInsertProduct;

    private boolean isEdit = false;
    @FXML
    private Button btnModify;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnDelete;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateTable();
    }

    private void updateTable() {
        listProduct.clear();
        ResultSet rsProduct = null;
        try {
            String query = "SELECT * FROM PRODUCT";
            Database db = new Database();
            db.connect();
            rsProduct = db.getResult(query);
            while (rsProduct.next()) {
                listProduct.add(new Product(rsProduct.getInt(1), rsProduct.getString(2), rsProduct.getString(3),
                        rsProduct.getInt(4), rsProduct.getInt(5), rsProduct.getString(6), rsProduct.getString(7)));
            }
            db.disconnect();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tcProductID.setCellValueFactory(new PropertyValueFactory<>("productID"));
        tcBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        tcModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        tcWarranty.setCellValueFactory(new PropertyValueFactory<>("warranty"));
        tcPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tcStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        tvProduct.setItems(listProduct);
    }

    @FXML
    private void btnInsertProductOnClicked(ActionEvent event) throws ClassNotFoundException, SQLException {
        String productID = tfProductID.getText();
        String brand = tfBrand.getText();
        String model = tfModel.getText();
        String warranty = tfWarranty.getText();
        String price = tfPrice.getText();
        String description = tfDescription.getText();
        String stock = tfStock.getText();
        String query;
        if (isEdit) {
            query = "UPDATE PRODUCT SET Brand = '" + brand + "', Model = '" + model + "', Warranty = " + warranty + ", Price = " + price + ", Details = '" + description + "', Stock = " + stock + " WHERE ProductID = " + productID;
        } else {
            query = "INSERT INTO PRODUCT VALUES (" + productID + ",'" + brand + "','" + model + "'," + warranty + "," + price + ",'" + description + "'," + stock + ")";
        }
        Database db = new Database();
        db.connect();
        db.updateTable(query);
        db.disconnect();
        updateTable();
        resetFields();
    }

    @FXML
    private void btnModifyOnCliked(ActionEvent event) {
        Product curr = tvProduct.getSelectionModel().getSelectedItem();
        if (curr == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Action");
            alert.setContentText("Select a product from table to modify!");
            alert.show();
            return;
        }
        tfProductID.setText(curr.getProductID() + "");
        tfBrand.setText(curr.getBrand());
        tfModel.setText(curr.getModel());
        tfWarranty.setText(curr.getWarranty() + "");
        tfPrice.setText(curr.getPrice() + "");
        tfDescription.setText(curr.getDescription());
        tfStock.setText(curr.getStock() + "");
        isEdit = true;
        tfProductID.setEditable(false);
        btnInsertProduct.setText("Update Product");
    }

    private void resetFields() {
        tfProductID.setText("");
        tfBrand.setText("");
        tfModel.setText("");
        tfWarranty.setText("");
        tfPrice.setText("");
        tfDescription.setText("");
        tfStock.setText("");
        isEdit = false;
        tfProductID.setEditable(true);
        btnInsertProduct.setText("Insert Product");
    }

    @FXML
    private void btnClearOnClicked(ActionEvent event) {
        resetFields();
    }

    @FXML
    private void btnDeleteOnClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        Product curr = tvProduct.getSelectionModel().getSelectedItem();
        if (curr == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Action");
            alert.setContentText("Select a product from table to delete!");
            alert.show();
            return;
        }
        String query = "DELETE FROM PRODUCT WHERE ProductID = " + curr.getProductID();
        Database db = new Database();
        db.connect();
        db.updateTable(query);
        db.disconnect();
        updateTable();
    }

}
