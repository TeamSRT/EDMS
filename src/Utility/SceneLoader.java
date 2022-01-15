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
            window.show();
        } catch (IOException ex) {
            System.out.println(ex);
        }

    }

}
