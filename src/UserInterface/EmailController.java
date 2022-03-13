/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Password.Email;
import Utility.Database;
import Utility.SceneLoader;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
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
public class EmailController implements Initializable {

    private TextField tf_OTP;
    @FXML
    private TextField UpdateEmail_tf;
    @FXML
    private Button valid_btn;

    public static int sent_otp;
    Database d = new Database();

    public static String Name;
    public static String Username;
    public static String email;
    public static String Password;
    public static String Confirmpassword;
    public static String Occupation;
    ResultSet rsProduct = null;
    @FXML
    private TextField tf_newpass;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void valid_btn(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {

        //String tf_get_otp = tf_OTP.getText();
        email = UpdateEmail_tf.getText();
        String user_query = "SELECT COUNT(Email) FROM Users WHERE Email = '" + email + "'";

        try {

            Database dbc = new Database();
            dbc.connect();

            rsProduct = dbc.getResult(user_query);

            int count = 1;

            while (rsProduct.next()) {
                count = rsProduct.getInt(1);
            }
            if (count == 1) {

                Password = tf_newpass.getText();
                email = UpdateEmail_tf.getText();
                Parent root = FXMLLoader.load(getClass().getResource("OTPcheck.fxml"));
                Scene src = new Scene(root);
                Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
                s.setScene(src);
                s.show();
                //String final_sent_otp = String.valueOf(sent_otp); //int to string conversion
                email = UpdateEmail_tf.getText();

                int rand = (int) (Math.random() * 4000);
                EmailController.sent_otp = rand;
                //Storing OTP
                Email.send("samirsarker150@gmail.com", "Samir1234", email, "EDMS Password Recovery", rand);

            } else {
                Alert a = new Alert(Alert.AlertType.NONE);
                a.setAlertType(Alert.AlertType.INFORMATION);

                a.setContentText("Emial doesn't exist");

                a.show();

            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(TransactionUIController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void btnBackOnAction(ActionEvent event) {
        new SceneLoader().LoadScene(event, "/UserInterface/Login.fxml");
    }
}
