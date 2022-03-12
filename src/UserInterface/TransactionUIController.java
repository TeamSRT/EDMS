/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Model.Customer;
import Model.Order;
import Model.Service;
import Model.Supply;
import Utility.Database;
import java.io.IOException;
import Model.Transaction;
import Utility.SceneLoader;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author SaMiR
 */
public class TransactionUIController implements Initializable {

    @FXML
    private ScrollPane btn_modify;
    private Label lblAlert;

    private boolean modify = false;

    int id;
    private Label lblTransactionID;
    @FXML
    private DatePicker dpFrom;
    @FXML
    private DatePicker dpTo;
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
    @FXML
    private TableColumn<Order, String> tcCost;
    @FXML
    private TableColumn<Supply, String> col_SupplierID;
    @FXML
    private TableColumn<Supply, String> col_ProductID;
    @FXML
    private TableColumn<Supply, String> col_date;
    @FXML
    private TableColumn<Supply, String> col_Quantity;
    @FXML
    private TableColumn<Supply, String> col_cost;
    @FXML
    private TableView<Supply> tvSupplies;
    @FXML
    private TableView<Service> tvService;
    @FXML
    private TableColumn<Service, String> tcServiceID;
    @FXML
    private TableColumn<Service, String> tcServicesProductID;
    @FXML
    private TableColumn<Service, String> tcCustomerID1;
    @FXML
    private TableColumn<Service, String> tcDetails;
    @FXML
    private TableColumn<Service, String> tcServiceCharge;
    @FXML
    private TableColumn<Service, String> tcServiceStatus;
    @FXML
    private TableColumn<Service, String> tcGivenDate;
    ObservableList<Service> listService = FXCollections.observableArrayList();
    ObservableList<Supply> supplies_list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showServiceTable("SELECT * FROM SERVICE_");
        updateTable("SELECT * FROM SUPPLIES");

    }

    private void showServiceTable(String query) {
        try {
            listService.clear();
            modify = false;
            Database db = new Database();
            db.connect();
            ResultSet rs = db.getResult(query);
            boolean check = false;
            while (rs.next()) {
                listService.add(new Service(rs.getInt("serviceID"), rs.getInt("productID"), rs.getInt("customerID"), rs.getString("details"), rs.getInt("serviceCharge"), rs.getString("serviceStatus"), rs.getString("givenDate")));
            }
            tcServiceID.setCellValueFactory(new PropertyValueFactory("serviceID"));
            tcServicesProductID.setCellValueFactory(new PropertyValueFactory("productID"));
            tcCustomerID.setCellValueFactory(new PropertyValueFactory("customerID"));
            tcDetails.setCellValueFactory(new PropertyValueFactory("details"));
            tcServiceStatus.setCellValueFactory(new PropertyValueFactory("serviceStatus"));
            tcServiceCharge.setCellValueFactory(new PropertyValueFactory("serviceCharge"));
            tcGivenDate.setCellValueFactory(new PropertyValueFactory("givenDate"));
            tvService.setItems(listService);
            db.disconnect();
        } catch (ClassNotFoundException | SQLException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Can't Show");
            alert.setContentText("Unable to show table!");
            alert.show();
        }

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
        tvSupplies.setItems(supplies_list);
    }

    @FXML
    private void btnSearchOnClick(ActionEvent event) {
    }

    @FXML
    private void btnModifyOnCliked(MouseEvent event) {
    }

}
