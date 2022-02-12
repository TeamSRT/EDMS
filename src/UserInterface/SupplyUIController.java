/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Supply;

import Utility.Database;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import table_model.Supply;
import table_model.Transaction;
import transaction.TransactionUIController;

/**
 * FXML Controller class
 *
 * @author SaMiR
 */
public class SupplyUIController implements Initializable {

    @FXML
    private TableView<Supply> supplies_table;
    @FXML
    private TableColumn<Supply, String> col_SupplierID;
    @FXML
    private TableColumn<Supply, String> col_ProductID;
    @FXML
    private TableColumn<Supply, String> col_date;
    @FXML
    private TableColumn<Supply, String> col_Quantity;
    @FXML
    private TextField tf_supplierID;
    @FXML
    private TextField tf_productID;
    @FXML
    private TextField tf_quantity;
    @FXML
    private Label lbl_SupplierID;
    @FXML
    private Label lbl_ProductID;
    @FXML
    private Label lbl_Quantity;
    @FXML
    private Button btn_insert;
    @FXML
    private Button btn_refresh_;
    @FXML
    private DatePicker date_picker;

    ObservableList<Supply> supplies_list = FXCollections.observableArrayList();
    @FXML
    private MenuItem mi_product;
    @FXML
    private MenuItem mi_productDate;

    private int searchOption;
    @FXML
    private MenuButton btn_searchBy;
    @FXML
    private MenuItem mi_searchByDate;
    @FXML
    private TextField tf_productID_search;
    @FXML
    private Button btn_search;

    private boolean pressed = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        searchOption = 1;
        btn_searchBy.setText("ProductID");
        updateTable("SELECT * FROM SUPPLIES");

    }

    @FXML
    private void btn_insert_action(ActionEvent event) {

        try {
            String supplierID = tf_supplierID.getText();
            String productID = tf_productID.getText();
            String quantity = tf_quantity.getText();

            String query;
            query = "INSERT INTO  SUPPLIES(SupplierID,ProductID,Quantity) VALUES('" + supplierID + "', '" + productID + "' ,'" + quantity + "')";
            Database dbc = new Database();
            dbc.connect();

            dbc.updateTable(query);

            dbc.disconnect();
            updateTable("SELECT * FROM SUPPLIES");
            clearTextFields();

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex);
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("");
            alert.setContentText("Unable to insert");
            alert.show();
        }

    }

    @FXML
    private void btn_refresh_action(ActionEvent event) {

        //pressed = false;
        searchOption = 1;
        btn_searchBy.setText("ProductID");

        tf_productID_search.clear();
        date_picker.getEditor().clear();
        updateTable("SELECT * FROM SUPPLIES");

    }

    private void updateTable(String query) {

        supplies_list.clear();
        ResultSet rsProduct = null;

        try {

            Database dbc = new Database();
            dbc.connect();
            rsProduct = dbc.getResult(query);
            while (rsProduct.next()) {
                supplies_list.add(new Supply(rsProduct.getInt(1), rsProduct.getInt(2), rsProduct.getString(3),
                        rsProduct.getInt(4)));

            }
            dbc.disconnect();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(TransactionUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        col_SupplierID.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
        col_ProductID.setCellValueFactory(new PropertyValueFactory<>("productID"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        col_Quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        supplies_table.setItems(supplies_list);

    }

    public void searchBy() {
        String query = "SELECT * FROM SUPPLIES";

        if (searchOption == 1) {
            query = "SELECT * FROM SUPPLIES WHERE ProductID LIKE '%" + tf_productID_search.getText() + "%'";
            System.out.println(query);
        }

        if (searchOption == 2) {

            LocalDate date = date_picker.getValue();

            query = "SELECT * FROM SUPPLIES WHERE ProductID  LIKE '%" + tf_productID_search.getText() + "%' AND CONVERT (DATE,Date_)='" + date + "'";

        }
        if (searchOption == 3) {
            LocalDate date = date_picker.getValue();
            query = "SELECT * FROM SUPPLIES  WHERE CONVERT (DATE,Date_)='" + date + "'";

        }

        updateTable(query);

    }

    @FXML
    private void searchByProduct_action(ActionEvent event) {

        searchOption = 1;
        // pressed = true;
        btn_searchBy.setText("ProductID");

    }

    @FXML
    private void searchByProductAndDate_action(ActionEvent event) {

        searchOption = 2;
        //pressed = true;
        btn_searchBy.setText("ProductID And Date");
    }

    @FXML
    private void searchByDate_action(ActionEvent event) {

        searchOption = 3;
        // pressed = true;
        btn_searchBy.setText("Date");
    }

    @FXML
    private void search_action(ActionEvent event) {

        //if (pressed == true) {
        if (searchOption == 1) {

            if (tf_productID_search.getText().equals("")) {

                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("");
                alert.setContentText("Enter ProductID");
                alert.show();
            } else {

                searchBy();
            }
        } else if (searchOption == 2) {

            if (tf_productID_search.getText().equals("") || date_picker.getEditor() == null) {

                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("");
                alert.setContentText("Enter Date And ProductID");
                alert.show();
            } else {

                searchBy();

            }
        } else if (searchOption == 3) {

            if (date_picker.getValue() == null) {

                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("");
                alert.setContentText("Enter Date");
                alert.show();
            } else {

                searchBy();

            }
        } else {
            searchBy();
        }

        /* else {

            searchBy();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setContentText("Select what to search");
            alert.show();
            searchBy();
            // searchOption = 0;
        }*/
    }

    public void clearTextFields() {
        tf_supplierID.clear();
        tf_productID.clear();
        tf_quantity.clear();
    }

}
