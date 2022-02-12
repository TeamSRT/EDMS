/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Model.Product;
import Model.User;
import Utility.Database;
import Utility.SceneLoader;
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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author ktouf
 */
public class AdminPanelUIController implements Initializable {

    @FXML
    private TableView<User> tvUsers;
    @FXML
    private TableColumn<User, String> tcUsername;
    @FXML
    private TableColumn<User, String> tcPermission;
    @FXML
    private TextField tfUsername;
    @FXML
    private ComboBox<String> cbPermission;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblPermission;
    @FXML
    private Button btnUpdate;

    ObservableList<User> listUser = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbPermission.getItems().add("PENDING");
        cbPermission.getItems().add("APPROVE");
        try {
            // TODO
            loadUserTable();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AdminPanelUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadUserTable() throws ClassNotFoundException, SQLException {
        listUser.clear();
        Database db = new Database();
        db.connect();
        ResultSet rsUser = db.getResult("SELECT UserName, UserPermission FROM Users");
        while (rsUser.next()) {
            if (rsUser.getString(2).equalsIgnoreCase("ADMIN")) {
                continue;
            }
            listUser.add(new User(rsUser.getString(1), rsUser.getString(2).toUpperCase()));
        }
        tcUsername.setCellValueFactory(new PropertyValueFactory("username"));
        tcPermission.setCellValueFactory(new PropertyValueFactory("permission"));
        tvUsers.setItems(listUser);
    }

    @FXML
    private void btnDeleteOnClick(ActionEvent event) {
        User curr = tvUsers.getSelectionModel().getSelectedItem();
        if (curr == null) {
            new SceneLoader().showAlert(Alert.AlertType.ERROR, "Invalid Choice", "Select a user to delete!");
            return;
        }
        try {
            Database db = new Database();
            db.connect();
            db.updateTable("DELETE FROM USERS WHERE UserName =" + curr.getUsername());
            db.disconnect();
        } catch (SQLException | ClassNotFoundException ex) {
            new SceneLoader().showAlert(Alert.AlertType.ERROR, "Failed", "Failed to delete!");
        }
    }

    @FXML
    private void btnModifyOnClik(ActionEvent event) {
        User curr = tvUsers.getSelectionModel().getSelectedItem();
        if (curr == null) {
            new SceneLoader().showAlert(Alert.AlertType.ERROR, "Invalid Choice", "Select a user to delete!");
            return;
        }

        lblPermission.setVisible(true);
        lblUsername.setVisible(true);
        tfUsername.setVisible(true);
        cbPermission.setVisible(true);
        btnUpdate.setVisible(true);

        btnUpdate.setDisable(false);
        lblPermission.setDisable(false);
        lblUsername.setDisable(false);
        tfUsername.setDisable(false);
        cbPermission.setDisable(false);

        tfUsername.setText(curr.getUsername());
        cbPermission.setValue(curr.getPermission());
    }

    @FXML
    private void btnUpdateOnClick(ActionEvent event) {
        String query = "UPDATE USERS SET UserPermission='" + cbPermission.getValue() + "' WHERE UserName = '" + tfUsername.getText() + "'";
        try {
            Database db = new Database();
            db.connect();
            db.updateTable(query);
            db.disconnect();

            lblPermission.setVisible(false);
            lblUsername.setVisible(false);
            tfUsername.setVisible(false);
            cbPermission.setVisible(false);
            btnUpdate.setVisible(false);

            btnUpdate.setDisable(true);
            lblPermission.setDisable(true);
            lblUsername.setDisable(true);
            tfUsername.setDisable(true);
            cbPermission.setDisable(true);
            loadUserTable();
            new SceneLoader().showAlert(Alert.AlertType.INFORMATION, "Successful", "User Permission Updated!");
        } catch (SQLException | ClassNotFoundException ex) {
            new SceneLoader().showAlert(Alert.AlertType.ERROR, "Failed", "Failed to update!");
        }
    }

}
