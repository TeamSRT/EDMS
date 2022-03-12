/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Model.Supply;
import Model.Transaction;
import static Utility.DataManager.selected;
import Utility.Database;
import Utility.SceneLoader;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;

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
    private boolean modify = false;
    private int id;
    private String date;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnModify;
    @FXML
    private Label lblAlert;
    @FXML
    private TableColumn<?, ?> col_cost;
    @FXML
    private TextField tf_cost;
    @FXML
    private Label lbl_Quantity1;

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
    private void btn_insert_action(ActionEvent event) throws ClassNotFoundException {
        String supplierID = tf_supplierID.getText();
        String productID = tf_productID.getText();
        String quantity = tf_quantity.getText();
        String cost = tf_cost.getText();
        String query;

        if (!modify) {
            query = "INSERT INTO  SUPPLIES(SupplierID,ProductID,Quantity,Cost) VALUES('" + supplierID + "', '" + productID + "' ,'" + quantity + "', " + cost + ")";

        } else {
            query = "UPDATE SUPPLIES SET ProductID='" + productID + "', Quantity ='" + quantity + "', Cost = " + cost + " WHERE SupplierID = '" + id + "' AND Date_='" + date + "' ";

        }
        try {

            Database dbc = new Database();
            dbc.connect();

            dbc.updateTable(query);
            lbl_SupplierID.setVisible(true);
            tf_supplierID.setVisible(true);
            btn_insert.setText("Insert");
            dbc.disconnect();

            updateTable("SELECT * FROM SUPPLIES");
            clearTextFields();

        } catch (SQLException ex) {

            if (ex.toString().contains("supplies_fk_supplierID")) {
                new SceneLoader().showAlert(Alert.AlertType.ERROR, "Failed", "Invalid SupplierID!");
            } else if (ex.toString().contains("supplies_fk_productID")) {
                new SceneLoader().showAlert(Alert.AlertType.ERROR, "Failed", "Invalid ProductID");
            } else if (ex.toString().contains("chk_quantity_supplies")) {
                new SceneLoader().showAlert(Alert.AlertType.ERROR, "Failed", "Quantity must be more than Zero");
            } else if (ex.toString().contains("supp_chk_cost")) {
                new SceneLoader().showAlert(Alert.AlertType.ERROR, "Failed", "Cost must be more than Zero");
            } else {
                new SceneLoader().showAlert(Alert.AlertType.ERROR, "Failed", "Unknown error. Try again later.");
            }
            System.out.println(ex);
            /*
            System.out.println(ex);
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("");
            alert.setContentText("Unable to insert");
            alert.show();*/
        }
        //Updating stock value in PRODUCT Table
        try {
            query = "SELECT Stock FROM PRODUCT WHERE ProductID = " + productID;
            Database db = new Database();
            db.connect();
            ResultSet rs = db.getResult(query);
            int stock = 0;
            if (rs.next()) {
                stock = rs.getInt("Stock");
            }
            stock += Integer.parseInt(quantity);
            query = "UPDATE PRODUCT SET Stock = " + stock + " WHERE ProductID = " + productID;
            db.updateTable(query);
            db.disconnect();
        } catch (SQLException ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Stock Update Error");
            alert.setContentText("There was a problem updating stock in Product table!");
            alert.show();
        }

    }

    @FXML
    private void btn_refresh_action(ActionEvent event) {

        //pressed = false;
        lbl_SupplierID.setVisible(true);
        tf_supplierID.setVisible(true);
        searchOption = 1;
        btn_searchBy.setText("ProductID");

        tf_productID_search.clear();
        date_picker.getEditor().clear();
        updateTable("SELECT * FROM SUPPLIES");
        clearTextFields();

    }

    private void updateTable(String query) {

        supplies_list.clear();
        modify = false;
        ResultSet rsProduct = null;

        try {

            Database dbc = new Database();
            dbc.connect();
            rsProduct = dbc.getResult(query);
            while (rsProduct.next()) {
                supplies_list.add(new Supply(rsProduct.getInt(1), rsProduct.getInt(2), rsProduct.getString(3),
                        rsProduct.getInt(4), rsProduct.getInt(5)));

            }
            dbc.disconnect();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(TransactionUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        col_SupplierID.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
        col_ProductID.setCellValueFactory(new PropertyValueFactory<>("productID"));
        col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
        col_Quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        col_cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
        supplies_table.setItems(supplies_list);
    }

    public void searchBy() {
        lbl_SupplierID.setVisible(true);
        tf_supplierID.setVisible(true);
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
    private void search_action(ActionEvent event) {
        lbl_SupplierID.setVisible(true);
        tf_supplierID.setVisible(true);
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

    @FXML
    private void searchByProduct_action(ActionEvent event) {
        lbl_SupplierID.setVisible(true);
        tf_supplierID.setVisible(true);
        searchOption = 1;
        // pressed = true;
        btn_searchBy.setText("ProductID");
    }

    @FXML
    private void searchByDate_action(ActionEvent event) {
        lbl_SupplierID.setVisible(true);
        tf_supplierID.setVisible(true);
        searchOption = 3;
        // pressed = true;
        btn_searchBy.setText("Date");
    }

    @FXML
    private void searchByProductAndDate_action(ActionEvent event) {
        lbl_SupplierID.setVisible(true);
        tf_supplierID.setVisible(true);
        searchOption = 2;
        //pressed = true;
        btn_searchBy.setText("ProductID And Date");
    }

    public void clearTextFields() {
        //lbl_SupplierID.setVisible(true);
        //tf_supplierID.setVisible(true);
        tf_supplierID.clear();
        tf_productID.clear();
        tf_quantity.clear();
        tf_cost.clear();
    }

    @FXML
    private void btnDeleteOnClicked(ActionEvent event) {
        lbl_SupplierID.setVisible(true);
        tf_supplierID.setVisible(true);
        try {
            Database db = new Database();
            db.connect();
            Supply selected = supplies_table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Can't Delete");
                alert.setContentText("You need to select a row to delete!");
                alert.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("Are you sure?");
                alert.setContentText("Do you really want to delete? This process can't be undone!");
                ButtonType yes = new ButtonType("Yes");
                ButtonType no = new ButtonType("No");
                alert.getButtonTypes().setAll(yes, no);
                Optional<ButtonType> choice = alert.showAndWait();
                if (choice.get() == yes) {
                    String query = "DELETE FROM SUPPLIES WHERE SupplierID = '" + selected.getSupplierID() + "' AND Date_= '" + selected.getDate() + "'AND Quantity='" + selected.getQuantity() + "'";
                    System.out.println(query);
                    db.updateTable(query);
                    db.disconnect();
                    //lblAlert.setText("Data deleted successfully!");
                    //lblAlert.setOpacity(1);
                    //PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    //pause.setOnFinished(e -> lblAlert.setOpacity(0));
                    //pause.play();
                }
            }
            updateTable("SELECT * FROM SUPPLIES");

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Exception in btnDelete: " + ex);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Can't Delete");
            alert.setContentText("Unable to delete data!");
            alert.show();
        }
    }

    @FXML
    private void btnModifyOnCliked(ActionEvent event) {
        modify = true;
        Supply selected = supplies_table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Can't modify");
            alert.setContentText("You need to select a row to modify!");
            alert.show();
        } else {
            lbl_SupplierID.setVisible(false);
            tf_supplierID.setVisible(false);
            id = selected.getSupplierID();
            date = selected.getDate();
            tf_productID.setText(Integer.toString(selected.getProductID()));
            tf_quantity.setText(Integer.toString(selected.getQuantity()));

            btn_insert.setText("Update");
        }
    }

}
