/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Utility.Database;
import Utility.SceneLoader;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ktouf
 */
public class SignUpUIController implements Initializable {

    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private PasswordField tfConfrimPassword;
    @FXML
    private TextField tfMail;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void btnCreateAccountOnAction(ActionEvent event) throws ClassNotFoundException, SQLException {
        if (!tfPassword.getText().equals(tfConfrimPassword.getText())) {
            new SceneLoader().showAlert(Alert.AlertType.ERROR, "Password Mismatch", "Password and Confirm Password doesn't match!");
            return;
        }
        if (tfPassword.getText().trim().length() == 0 || tfConfrimPassword.getText().trim().length() == 0 || tfUsername.getText().trim().length() == 0 || tfMail.getText().trim().length() == 0) {
            new SceneLoader().showAlert(Alert.AlertType.ERROR, "Empty Fields", "Please fill up all the text fields!");
            return;
        }

        Database db = new Database();
        db.connect();
        try {
            String query = "INSERT INTO Users(UserName, UserPassword, Email) VALUES"
                    + "('" + tfUsername.getText() + "'"
                    + ",'" + tfPassword.getText() + "','"
                    + tfMail.getText() +"')";
            System.out.println(query);
            db.updateTable(query);
            new SceneLoader().LoadScene(event, "/UserInterface/PendingApprovalUI.fxml");

        } catch (SQLException ex) {
            if (ex.toString().contains("pk_username")) {
                new SceneLoader().showAlert(Alert.AlertType.ERROR, "Failed", "Username already exists!");
            } else if (ex.toString().contains("chk_username")) {
                new SceneLoader().showAlert(Alert.AlertType.ERROR, "Failed", "Username contains invalid character. Only A-Z, a-z, 0-9 allowed!");
            } else if (ex.toString().contains("chk_password")) {
                new SceneLoader().showAlert(Alert.AlertType.ERROR, "Failed", "Password length needs to at least 6!");
            } else if (ex.toString().contains("chk_email")) {
                new SceneLoader().showAlert(Alert.AlertType.ERROR, "Failed", "Invalid Email format!");
            } else if (ex.toString().contains("uq_key")) {
                new SceneLoader().showAlert(Alert.AlertType.ERROR, "Failed", "Email already used in another ID!");
            } else {
                new SceneLoader().showAlert(Alert.AlertType.ERROR, "Failed", "Unknown error. Try again later.");
            }
            System.out.println(ex);
        }
        db.disconnect();
    }

    @FXML
    private void btnBackOnAction(ActionEvent event) {
        new SceneLoader().LoadScene(event, "/UserInterface/Login.fxml");
    }

}
