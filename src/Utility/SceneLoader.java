/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utility;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

/**
 *
 * @author ktoufiquee
 */
public class SceneLoader {

    public void LoadScene(ActionEvent ev, String fileLocation) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fileLocation));
            Scene dashboard = new Scene(root);
            Stage window = (Stage) ((Node) ev.getSource()).getScene().getWindow();
            window.setScene(dashboard);
            window.setResizable(false);
            window.show();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    
    public void showAlert(AlertType errorType, String title, String text) {
        Alert alert = new Alert(errorType);
        alert.setTitle(title);
        alert.setContentText(text);
        alert.show();
    }
}
