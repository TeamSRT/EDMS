/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Utility.DataManager;
import Utility.SceneLoader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author ktouf
 */
public class HomeUIController implements Initializable {

    @FXML
    private BorderPane bpHome;
    @FXML
    private Button btnProduct;
    @FXML
    private Button btnCustomer;
    @FXML
    private Button btnSupplier;
    @FXML
    private Button btnSupply;
    @FXML
    private Button btnTransaction;
    @FXML
    private Button btnOrder;
    @FXML
    private Button btnAdmin;
    @FXML
    private Button btnSignOut;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if (!DataManager.permission.equalsIgnoreCase("ADMIN")) {
            btnAdmin.setDisable(true);
            btnAdmin.setVisible(false);
        }
    }

    public void loadScene(String filePath) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(filePath));
        bpHome.getChildren().remove(bpHome.getCenter()); //remove existing fxml from center.
        try {
            bpHome.setCenter(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void btnProductOnClick(ActionEvent event) {
        loadScene("/UserInterface/ProductUI.fxml");
    }

    @FXML
    private void btnCustomerOnClick(ActionEvent event) {
        loadScene("/UserInterface/CustomerUI.fxml");
    }

    @FXML
    private void btnSupplierOnClick(ActionEvent event) {
        loadScene("/UserInterface/SupplierUI.fxml");
    }

    @FXML
    private void btnSupplyOnClick(ActionEvent event) {
        loadScene("/UserInterface/SupplyUI.fxml");
    }

    @FXML
    private void btnTransactionOnClick(ActionEvent event) {
        loadScene("/UserInterface/TransactionUI.fxml");
    }

    @FXML
    private void btnOrderOnClick(ActionEvent event) {
        loadScene("/UserInterface/OrderUI.fxml");
    }

    @FXML
    private void btnAdminOnClick(ActionEvent event) {
        loadScene("/UserInterface/AdminPanelUI.fxml");
    }

    @FXML
    private void btnSignOutOnClick(ActionEvent event) {
        new SceneLoader().LoadScene(event, "/UserInterface/Login.fxml");
        DataManager.product = null;
        DataManager.selected = null;
        DataManager.permission = "";
    }
}
