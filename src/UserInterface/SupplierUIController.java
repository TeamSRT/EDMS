/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Model.Customer;
import Model.Supplier;
import Utility.Database;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author SrishtiPC
 */
public class SupplierUIController implements Initializable {

    @FXML
    private TableView<Supplier> tvSupplier;
    @FXML
    private TableColumn<Supplier, Integer> tcSupplierID;
    @FXML
    private TableColumn<Supplier, String> tcName;
    @FXML
    private TableColumn<Supplier, String> tcPhone;
    @FXML
    private TableColumn<Supplier, String> tcMail;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfPhone;
    @FXML
    private TextField tfMail;
    @FXML
    private Label lblAlert;
    @FXML
    private Button btnInsertSupplier;
    @FXML
    private Button btnModify;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnDelete;
    @FXML
    private TextField tfSearch;
    @FXML
    private MenuButton menuBtnSearch;
    @FXML
    private MenuItem mitemID;
    @FXML
    private MenuItem mitemName;
    @FXML
    private MenuItem mitemPhone;
    @FXML
    private MenuItem mitemKeyword;
    private boolean modify = false;
    private Integer id = -1;
    private String searchBy = "";
    ObservableList<Supplier> listSupplier = FXCollections.observableArrayList();
    @FXML
    private MenuItem mitemMail;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showTable("SELECT * FROM SUPPLIER");
        searchBy = "Keyword";
    }

    private void showTable(String query) {

        try {
            listSupplier.clear();
            modify = false;
            Database db = new Database();
            db.connect();
            ResultSet rs = db.getResult(query);
            boolean check = false;
            while (rs.next()) {
                check = true;
                listSupplier.add(new Supplier(rs.getInt("supplierID"), rs.getString("supplierPhone"), rs.getString("supplierName"), rs.getString("supplierMail")));
            }
            if (check == false) {
                btnModify.setDisable(true);
                btnDelete.setDisable(true);
            } else {
                btnModify.setDisable(false);
                btnDelete.setDisable(false);
            }
            tcSupplierID.setCellValueFactory(new PropertyValueFactory("supplierID"));
            tcName.setCellValueFactory(new PropertyValueFactory("name"));
            tcPhone.setCellValueFactory(new PropertyValueFactory("phone"));
            tcMail.setCellValueFactory(new PropertyValueFactory("mail"));
            tvSupplier.setItems(listSupplier);
            db.disconnect();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Exception in show table:" + ex);
        }

    }

    @FXML
    private void btnInsertSupplierOnClicked(ActionEvent event) {
        String name = tfName.getText();
        String phone = tfPhone.getText();
        String mail = tfMail.getText();
        if (name.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Name field required");
            alert.setContentText("You can not keep the name field empty!");
            alert.show();
        } else if (phone.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Phone field required");
            alert.setContentText("You can not keep the phone field empty!");
            alert.show();
        } else {
            try {
                Database db = new Database();
                db.connect();
                String query;
                if (!modify) {
                    query = "INSERT INTO SUPPLIER(supplierName,supplierPhone,supplierMail) VALUES ('" + name + "','" + phone + "','" + mail + "')";
                } else {
                    query = "UPDATE SUPPLIER SET supplierName = '" + name + "', supplierPhone = '" + phone + "',supplierMail = '" + mail + "'" + "WHERE supplierID = " + id + "";
                }
                db.updateTable(query);
                if (!modify) {
                    lblAlert.setText("Data inserted successfully!");
                } else {
                    lblAlert.setText("Data updated successfully!");
                }
                lblAlert.setOpacity(1);
                btnInsertSupplier.setText("Insert Supplier");

                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(e -> resetFields());
                pause.play();
                showTable("SELECT * FROM SUPPLIER");
                db.disconnect();
            } catch (ClassNotFoundException | SQLException ex) {
                System.out.println("Exception in insert supplier:" + ex);
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Failed operation");
                if (!modify) {
                    alert.setContentText("Unable to insert data!Please check your inputs");
                } else {
                    alert.setContentText("Unable to update data!Please check your inputs");
                }
                alert.show();
            }
        }
    }

    @FXML
    private void btnModifyOnCliked(ActionEvent event) {
        modify = true;
        Supplier selected = tvSupplier.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Can't modify");
            alert.setContentText("You need to select a row to modify!");
            alert.show();
        } else {
            id = selected.getSupplierID();
            tfName.setText(selected.getName());
            tfPhone.setText(selected.getPhone());
            tfMail.setText(selected.getMail());
            btnInsertSupplier.setText("Update Supplier");
        }
    }

    @FXML
    private void btnClearOnClicked(ActionEvent event) {
        resetFields();
    }

    private void resetFields() {
        tfName.setText("");
        tfPhone.setText("");
        tfMail.setText("");
        modify = false;
        id = -1;
        lblAlert.setOpacity(0);
        tfSearch.setText("");
        btnInsertSupplier.setText("Insert Supplier");
    }

    @FXML
    private void btnDeleteOnClicked(ActionEvent event) {
        try {
            Database db = new Database();
            db.connect();
            Supplier selected = tvSupplier.getSelectionModel().getSelectedItem();
            if (selected == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Can't Delete");
                alert.setContentText("You need to select a row to delete!");
                alert.show();
            } else {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setHeaderText("Are you sure?");
                alert.setContentText("Do you really want to delete?This process can't be undone!");
                ButtonType yes = new ButtonType("Yes");
                ButtonType no = new ButtonType("No");
                alert.getButtonTypes().setAll(yes, no);
                Optional<ButtonType> choice = alert.showAndWait();
                if (choice.get() == yes) {
                    String query = "DELETE FROM SUPPLIER WHERE supplierID = " + selected.getSupplierID();
                    db.updateTable(query);
                    db.disconnect();
                    lblAlert.setText("Data deleted successfully!");
                    lblAlert.setOpacity(1);
                    PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    pause.setOnFinished(e -> lblAlert.setOpacity(0));
                    pause.play();
                }
            }
            showTable("SELECT * FROM SUPPLIER");

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Exception in btnDelete: " + ex);
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Can't Delete");
            alert.setContentText("Unable to delete data!");
            alert.show();
        }
    }

    @FXML
    private void searchOnKeyRelease(KeyEvent event) {
        search();
    }

    @FXML
    private void searchByID(ActionEvent event) {
        searchBy = "Id";
        menuBtnSearch.setText("Id");
        tfSearch.setPromptText("Search by ID");
        search();
    }

    @FXML
    private void searchByName(ActionEvent event) {
        searchBy = "Name";
        menuBtnSearch.setText("Name");
        tfSearch.setPromptText("Search by Name");
        search();
    }

    @FXML
    private void searchByPhone(ActionEvent event) {
        searchBy = "Phone";
        menuBtnSearch.setText("Phone");
        tfSearch.setPromptText("Search by Phone");
        search();
    }

    @FXML
    private void searchByMail(ActionEvent event) {
        searchBy = "Mail";
        menuBtnSearch.setText("Mail");
        tfSearch.setPromptText("Search by Mail");
        search();
    }

    @FXML
    private void searchByKeyword(ActionEvent event) {
        searchBy = "Keyword";
        menuBtnSearch.setText("Keyword");
        tfSearch.setPromptText("Search by Keyword");
        search();
    }

    private void search() {
        if (tfSearch.getText().equals("")) {
            System.out.println("No text in the search field");
            showTable("SELECT * FROM SUPPLIER");
        } else {
            String query;
            switch (searchBy) {
                case "Id":
                    query = "SELECT * FROM SUPPLIER WHERE supplierID LIKE '%" + tfSearch.getText() + "%'";
                    System.out.println("ID chosen");
                    break;
                case "Name":
                    query = "SELECT * FROM SUPPLIER WHERE supplierName LIKE '%" + tfSearch.getText() + "%'";
                    System.out.println("Name chosen");
                    break;
                case "Phone":
                    query = "SELECT * FROM SUPPLIER WHERE supplierPhone LIKE '%" + tfSearch.getText() + "%'";
                    System.out.println("Phone Chosen");
                    break;
                case "Mail":
                    query = "SELECT * FROM SUPPLIER WHERE supplierMail LIKE '%" + tfSearch.getText() + "%'";
                    System.out.println("Mail Chosen");
                    break;
                default:
                    query = "SELECT * FROM SUPPLIER WHERE supplierID LIKE '%" + tfSearch.getText() + "%' OR "
                            + "supplierName LIKE '%" + tfSearch.getText() + "%' OR "
                            + "supplierMail LIKE '%" + tfSearch.getText() + "%' OR "
                            + "supplierPhone LIKE '%" + tfSearch.getText() + "%'";
                    System.out.println("Keyword chosen");
                    break;
            }
            showTable(query);
        }
    }

}
