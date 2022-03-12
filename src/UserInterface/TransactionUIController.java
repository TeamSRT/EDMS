/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Model.Customer;
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
    private TableView<Transaction> Transaction_table;

    @FXML
    private TableColumn<Transaction, String> TransactionID_heading;
    @FXML
    private TableColumn<Transaction, String> OrderID_heading;
    @FXML
    private TableColumn<Transaction, String> Type_heading;
    @FXML
    private TableColumn<Transaction, String> Date_heading;
    @FXML
    private TableColumn<Transaction, String> Amount_heading;

    @FXML
    private TextField tf_transactionID;
    @FXML
    private TextField tf_orderID;
    @FXML
    private TextField tf_type;

    @FXML
    private TextField tf_amount;
    @FXML
    private Button btn_insert;

    ObservableList<Transaction> transaction_list = FXCollections.observableArrayList();
    @FXML
    private Button btn_Show;
    @FXML
    private Button btn_search;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField total_show;
    public Label label_datainert;
    private ResultSet rsProduct = null;
    boolean show_sum_amount = false;
    String total_amount_query;
    @FXML
    private Label date_label;
    @FXML
    private Label lbl_total;
    @FXML
    public Label label_data_insert;
    @FXML
    private AnchorPane btn_modify;
    @FXML
    private Button btn_delete;
    @FXML
    private Label lblAlert;

    private boolean modify = false;
    @FXML
    private Button btnModify;

    int id;
    @FXML
    private Label lblTransactionID;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        updateTable("SELECT * FROM TRANSACTION_");
        show_sum_amount = false;

    }

    @FXML
    private void btn_insert_onclick(ActionEvent event) throws ClassNotFoundException, SQLException {

        datePicker.getEditor().clear();
        String transaction_id = tf_transactionID.getText();
        String order_id = tf_orderID.getText();
        String typee = tf_type.getText();
        String amount = tf_amount.getText();

        String query;
        if (!modify) {
            query = "INSERT INTO TRANSACTION_(TransactionID ,OrderID,Type_ ,Amount) VALUES('" + transaction_id + "', '" + order_id + "' ,'" + typee + "' ,'" + amount + "')";
        } else {

            query = "UPDATE TRANSACTION_ SET OrderID='" + order_id + "', Type_='" + typee + "', Amount='" + amount + "'" + "WHERE TransactionID = " + id + "";

        }
        Database dbc = new Database();
        dbc.connect();

        try {
            dbc.updateTable(query);
            lblTransactionID.setVisible(true);
            tf_transactionID.setVisible(true);
            btn_insert.setText("Insert");
            clear_fields();
            label_data_insert.setOpacity(1);
             PauseTransition pause = new PauseTransition(Duration.seconds(1));
             pause.setOnFinished(e -> label_data_insert.setOpacity(0));
            pause.play();
        }catch (SQLException ex) {
            
            if (ex.toString().contains("pk_transactionID")) {
                new SceneLoader().showAlert(Alert.AlertType.ERROR, "Failed", "TransactionID is already exist");
            } else if (ex.toString().contains("ck_type")) {
                new SceneLoader().showAlert(Alert.AlertType.ERROR, "Failed", "Type must be between 'A-z' and 'a-z'");
            } else if (ex.toString().contains("ck_amount")) {
                new SceneLoader().showAlert(Alert.AlertType.ERROR, "Failed", "Amount must be more than Zero");
            } 
            System.out.println(ex);
        }
        dbc.disconnect();

        show_sum_amount = false;
        updateTable("SELECT * FROM TRANSACTION_");
        total_show.setVisible(true);
        lbl_total.setVisible(true);

    }

    private void updateTable(String query) {

        transaction_list.clear();
        ResultSet rsProduct = null;
        modify = false;
        try {

            Database dbc = new Database();
            dbc.connect();
            rsProduct = dbc.getResult(query);
            while (rsProduct.next()) {
                transaction_list.add(new Transaction(rsProduct.getInt(1), rsProduct.getInt(2), rsProduct.getString(3),
                        rsProduct.getString(4), rsProduct.getFloat(5)));
                // dbc.disconnect();

            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(TransactionUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        TransactionID_heading.setCellValueFactory(new PropertyValueFactory<>("transaction_id"));
        OrderID_heading.setCellValueFactory(new PropertyValueFactory<>("order_id"));
        Type_heading.setCellValueFactory(new PropertyValueFactory<>("type"));
        Date_heading.setCellValueFactory(new PropertyValueFactory<>("date"));
        Amount_heading.setCellValueFactory(new PropertyValueFactory<>("amount"));

        Transaction_table.setItems(transaction_list);

//String total_amount_query = "SELECT SUM(amount) AS Total FROM TRANSACTIONN WHERE CONVERT (DATE,datee)='" + now + "'";
        String username = "samir";
        String password = "1234567";

        String user_query = "SELECT COUNT(UserName) FROM Users WHERE UserName = '" + username + "' AND UserPassword = '" + password + "'";

        try {

            Database dbc = new Database();
            dbc.connect();

            rsProduct = dbc.getResult(user_query);

            int count = 1;

            while (rsProduct.next()) {
                count = rsProduct.getInt(1);
            }
            if (count == 1) {

                if (show_sum_amount == false) {

                    LocalDateTime now = LocalDateTime.now();
                    total_amount_query = "SELECT SUM(Amount) AS Total FROM TRANSACTION_ WHERE CONVERT (DATE,Date_)='" + now + "'";
                    date_label.setText("( " + now.format(DateTimeFormatter.ISO_DATE).toString() + " )");

                    rsProduct = dbc.getResult(total_amount_query);

                    while (rsProduct.next()) {

                        total_show.setText(rsProduct.getString("Total"));

                    }

                } else if (show_sum_amount == true) {

                    LocalDate date = datePicker.getValue();
                    total_amount_query = "SELECT SUM(Amount) AS Total FROM TRANSACTION_ WHERE CONVERT (DATE,Date_)='" + date + "'";
                    date_label.setText("( " + date.format(DateTimeFormatter.ISO_DATE).toString() + " )");
                    rsProduct = dbc.getResult(total_amount_query);

                    while (rsProduct.next()) {

                        total_show.setText(rsProduct.getString("Total"));

                    }

                }
            } else {
                //total_show.setVisible(false);
                //lbl_total.setVisible(false);
            }

            dbc.disconnect();

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(TransactionUIController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void btn_search_onclick(ActionEvent event) {
        total_show.setVisible(true);
        lbl_total.setVisible(true);
        if (datePicker.getEditor() == null) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setContentText("Please Enter Date");
            alert.show();
        }

        show_sum_amount = true;
        LocalDate date = datePicker.getValue();

        String query = "SELECT * FROM TRANSACTION_ WHERE CONVERT (DATE,Date_)='" + date + "'";

        updateTable(query);

    }

    public void clear_fields() {

        label_data_insert.setOpacity(0);
        tf_transactionID.clear();

        tf_orderID.clear();

        tf_type.clear();

        tf_amount.clear();
    }

    @FXML
    private void btnDeleteOnClicked(ActionEvent event) {
        total_show.setVisible(true);
        lbl_total.setVisible(true);
        try {
            Database db = new Database();
            db.connect();
            Transaction selected = Transaction_table.getSelectionModel().getSelectedItem();
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
                    String query = "DELETE FROM TRANSACTION_ WHERE TransactionID = " + selected.getTransaction_id();
                    db.updateTable(query);
                    db.disconnect();
                    lblAlert.setText("Data deleted successfully!");
                    lblAlert.setOpacity(1);
                    PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    pause.setOnFinished(e -> lblAlert.setOpacity(0));
                    pause.play();
                }
            }
            updateTable("SELECT * FROM TRANSACTION_");

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
        Transaction selected = Transaction_table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Can't modify");
            alert.setContentText("You need to select a row to modify!");
            alert.show();
        } else {
            lblTransactionID.setVisible(false);
            tf_transactionID.setVisible(false);
            id = selected.getTransaction_id();
            System.out.println(id);
            tf_orderID.setText(Integer.toString(selected.getOrder_id()));
            tf_type.setText(selected.getType());
            tf_amount.setText(Float.toString(selected.getOrder_id()));
            btn_insert.setText("Update");
        }
    }

    @FXML
    private void btnModifyOnCliked(MouseEvent event) {
    }

    @FXML
    private void btnRefreshonclick(ActionEvent event) {
        total_show.setVisible(true);
        lbl_total.setVisible(true);
        show_sum_amount = false;
        updateTable("SELECT * FROM TRANSACTION_");
        datePicker.getEditor().clear();
        clear_fields();
        
    }

}
