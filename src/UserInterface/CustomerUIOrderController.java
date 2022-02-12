/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author SrishtiPC
 */
public class CustomerUIOrderController implements Initializable {

    @FXML
    private TableColumn<?, ?> tcOrderrID;
    @FXML
    private TableColumn<?, ?> tcProductID;
    @FXML
    private TableColumn<?, ?> tcQuantity;
    @FXML
    private TextField tfCustomerID;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
