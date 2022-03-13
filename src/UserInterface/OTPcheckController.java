/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Utility.Database;
import Utility.SceneLoader;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author SaMiR
 */
public class OTPcheckController implements Initializable {

    @FXML
    private TextField tfOTP;
    @FXML
    private Button OTPenter;
    public static int check = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //lblOtpCheck.setVisible(false);
    }

    @FXML
    private void OTPenterAction(ActionEvent event) {
        try {
            int OTP = EmailController.sent_otp;

            String sentOtp = String.valueOf(OTP);
            if (tfOTP.getText().equals(sentOtp)) {
                check = 1;
            }

            System.out.println(OTP);
            if (check == 1) {
                String Password = EmailController.Password;
                String Email = EmailController.email;
                String query = "UPDATE Users SET UserPassword= '" + Password + "' WHERE Email = '" + Email + "'";
                System.out.println(query);
                Database db = new Database();
                db.connect();
                db.updateTable(query);
            } else {
                new SceneLoader().showAlert(Alert.AlertType.ERROR, "Error!", "OTP doesn't match!");
                return;
            }
        } catch (ClassNotFoundException | SQLException ex) {
            new SceneLoader().showAlert(Alert.AlertType.ERROR, "Error!", "Something went wrong!");
            new SceneLoader().LoadScene(event, "/UserInterface/Login.fxml");
            ex.printStackTrace();
        }

        new SceneLoader().showAlert(Alert.AlertType.CONFIRMATION, "Successfull!", "Password updated successfully!");
        new SceneLoader().LoadScene(event, "/UserInterface/Login.fxml");

    }

}
