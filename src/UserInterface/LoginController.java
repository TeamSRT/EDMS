/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Utility.DataManager;
import Utility.Database;
import Utility.SceneLoader;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ktouf
 */
public class LoginController implements Initializable {

    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Button btnLogin;
    @FXML
    private Label lblInvalid;
    @FXML
    private Button btnForgetPassword;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    

    @FXML
    private void btnLoginOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String username = tfUsername.getText();
        String password = pfPassword.getText();
        String query = "SELECT UserPermission FROM Users WHERE UserName = '" + username + "' AND UserPassword = '" + password + "'";
        Database db = new Database();
        db.connect();
        ResultSet rs = db.getResult(query);
        
        String permission = "INVALID";
        while (rs.next()) {
            permission = rs.getString(1);
        }
        DataManager.permission = permission;
        if (permission.equals("INVALID")) {
            
            lblInvalid.setOpacity(1);
        }
        else if(permission.equals("PENDING")) {
            new SceneLoader().LoadScene(event, "/UserInterface/PendingApprovalUI.fxml");
        }
        else {
            new SceneLoader().LoadScene(event, "/UserInterface/HomeUI.fxml");
        }
        db.disconnect();
    }

    @FXML
    private void tfUsernameOnClicked(MouseEvent event) {
        lblInvalid.setOpacity(0);
    }

    @FXML
    private void tfPasswordOnClicked(MouseEvent event) {
        lblInvalid.setOpacity(0);
    }

    @FXML
    private void btnCreateAccountOnClick(ActionEvent event) {
        new SceneLoader().LoadScene(event, "/UserInterface/SignUpUI.fxml");
    }

    @FXML
    private void btnForgetPasswordAction(ActionEvent event) throws IOException {
        
          Parent root = FXMLLoader.load(getClass().getResource("email.fxml"));
                Scene src = new Scene(root);
                Stage s = (Stage) ((Node) event.getSource()).getScene().getWindow();
                s.setScene(src);
                s.show();  
    }
    
}
