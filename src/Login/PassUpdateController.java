/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author SrishtiPC
 */
public class PassUpdateController implements Initializable {

    @FXML
    private JFXButton back_btn;
    @FXML
    private JFXButton changePass;
    @FXML
    private JFXTextField tf_username;
    @FXML
    private JFXTextField tf_email;
    @FXML
    private JFXPasswordField tf_updatedPass;
    @FXML
    private JFXPasswordField tf_confUpdatedpass;
    @FXML
    private Label pass_matching_lebel;
    @FXML
    private Label Passwordlength_lbl;
    @FXML
    private Label weak_lbl;
    @FXML
    private Label mid_lbl;
    @FXML
    private Label strong_lbl;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void back_btn(ActionEvent event) {
    }

    @FXML
    private void changePass(ActionEvent event) {
    }

    @FXML
    private void tfPasswordOnTyped(KeyEvent event) {
    }
    
}
