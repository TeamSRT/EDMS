/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Utility.Database;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    public static int check=0;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //lblOtpCheck.setVisible(false);
    }    

    @FXML
    private void OTPenterAction(ActionEvent event) throws ClassNotFoundException, SQLException, IOException {
       int OTP = EmailController.sent_otp;
        
       String sentOtp =String.valueOf(OTP);
       if(tfOTP.getText().equals(sentOtp))
       {
            check=1;
       }
       
           System.out.println(OTP);
           String Password=EmailController.Password;
           String Email=EmailController.email;
           //String query="UPDATE Users SET password= '" + Password +"' WHERE Email = '" + Email+ "'";
               
                Parent root = FXMLLoader.load(getClass().getResource("Confirm.fxml"));
                Scene src = new Scene(root);
                Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
                s.setScene(src);
                s.show();  
               
       
     
       
    }
    
}
