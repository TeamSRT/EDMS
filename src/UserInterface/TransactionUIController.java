/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Model.Customer;
import Model.Order;
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
    private TableColumn<?, ?> tcCustomerID;
    @FXML
    private TableColumn<?, ?> tcProductID;
    @FXML
    private TableColumn<?, ?> tcQuantity;
    @FXML
    private TableColumn<?, ?> tcTime;
    @FXML
    private TableColumn<?, ?> tcCost;
    @FXML
    private TableView<?> supplies_table;
    @FXML
    private TableColumn<?, ?> col_SupplierID;
    @FXML
    private TableColumn<?, ?> col_ProductID;
    @FXML
    private TableColumn<?, ?> col_date;
    @FXML
    private TableColumn<?, ?> col_Quantity;
    @FXML
    private TableColumn<?, ?> col_cost;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODOE

    }

    @FXML
    private void btnSearchOnClick(ActionEvent event) {
    }

    @FXML
    private void btnModifyOnCliked(MouseEvent event) {
    }


}
