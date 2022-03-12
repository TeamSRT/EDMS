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
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author SaMiR
 */
public class ConfirmController implements Initializable {

    @FXML
    private Button btnEnter;
    @FXML
    private Button btnTryagain;
    @FXML
    private Label lblOtpMatch;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        if(OTPcheckController.check==1)
        {
               
                    try{
                        btnEnter.setVisible(true);
                        lblOtpMatch.setVisible(false);
                        btnTryagain.setVisible(false);
                        //System.out.println(OTP);
                        String Password=EmailController.Password;
                        String Email=EmailController.email;
                        String query="UPDATE Users SET UserPassword= '" + Password +"' WHERE Email = '" + Email+ "'";
                        
                        Database dbc = new Database();
                        dbc.connect();
                        try{
                            
                            dbc.updateTable(query);
                        }catch (SQLException ex) {
                            System.out.println(ex);
                        }
                        
                        
                    }catch (ClassNotFoundException ex) {
                        Logger.getLogger(ConfirmController.class.getName()).log(Level.SEVERE, null,ex);
                    } catch (SQLException ex) {
                Logger.getLogger(ConfirmController.class.getName()).log(Level.SEVERE, null, ex);
            }
                    
                    
                }
                
           
        
        
        else
        {
            btnEnter.setVisible(false);
            lblOtpMatch.setVisible(true);
            btnTryagain.setVisible(true); 
        }
                
    }    

    @FXML
    private void btnEnterAction(ActionEvent event) throws IOException {
                Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
                Scene src = new Scene(root);
                Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
                s.setScene(src);
                s.show();  
    }

    @FXML
    private void btnTryagainAction(ActionEvent event) throws IOException {
         Parent root = FXMLLoader.load(getClass().getResource("email.fxml"));
                Scene src = new Scene(root);
                Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
                s.setScene(src);
                s.show(); 
    }
    
}
