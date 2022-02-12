/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Utility.Database;
import java.io.IOException;
import Model.Transaction;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
        query = "INSERT INTO TRANSACTION_(TransactionID ,OrderID,Type_ ,Amount) VALUES('" + transaction_id + "', '" + order_id + "' ,'" + typee + "' ,'" + amount + "')";

        Database dbc = new Database();
        dbc.connect();

        try {
            dbc.updateTable(query);
            
              clear_fields();
            //label_data_insert.setOpacity(1);
           // PauseTransition pause = new PauseTransition(Duration.seconds(1));
           // pause.setOnFinished(e -> label_data_insert.setOpacity(0));
            //pause.play();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Unable to Insert Data");

        }

        dbc.disconnect();

        show_sum_amount = false;
        updateTable("SELECT * FROM TRANSACTION_");
      

    }

    @FXML
    private void btn_show_onclick(ActionEvent event) throws IOException {

        show_sum_amount = false;
        updateTable("SELECT * FROM TRANSACTION_");
        datePicker.getEditor().clear();
    }

    private void updateTable(String query) {

        transaction_list.clear();
        ResultSet rsProduct = null;

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
        String username = "ktoufiquee";
        String password = "p@ssw0rd16";

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
            }
        else {
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

}
