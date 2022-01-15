/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Utility.Database;
import Utility.SceneLoader;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    

    @FXML
    private void btnLoginOnAction(ActionEvent event) throws SQLException, ClassNotFoundException {
        String username = tfUsername.getText();
        String password = pfPassword.getText();
        String query = "SELECT COUNT(UserName) FROM Users WHERE UserName = '" + username + "' AND UserPassword = '" + password + "'";
        Database db = new Database();
        db.connect();
        ResultSet rs = db.getResult(query);
        
        int count = 0;
        while (rs.next()) {
            count = rs.getInt(1);
        }
        if (count == 1) {
            System.out.println("LOGIN SUCCESSFULL");
            new SceneLoader().LoadScene(event, "/UserInterface/ProductUI.fxml");
        }
        else {
            lblInvalid.setOpacity(1);
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
    
}
