/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Model.CartItem;
import Model.Product;
import Utility.DataManager;
import Utility.SceneLoader;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ktouf
 */
public class CartItemUIController implements Initializable {

    @FXML
    private Label lblBrand;
    @FXML
    private Label lblModel;
    @FXML
    private Label lblStock;
    @FXML
    private TextField tfQuantity;
    ProductUIController prodController;
    Product curr;
    @FXML
    private TextField tfMoney;
    @FXML
    private Label lblTotalCost;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prodController = DataManager.prodController;
        curr = DataManager.orderSelect;
        lblBrand.setText(curr.getBrand());
        lblModel.setText(curr.getModel());
        lblStock.setText(curr.getStock());
        lblTotalCost.setText(curr.getPrice() + "");

        tfQuantity.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    tfQuantity.setText(newValue.replaceAll("[^\\d]", ""));
                } else {
                    if (newValue.trim().length() > 0) {
                        int q = Integer.parseInt(newValue);
                        tfMoney.setText(q * curr.getPrice() + "");
                    }
                }
            }
        });
        tfMoney.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    tfQuantity.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    @FXML
    private void btnAddOnClick(ActionEvent event) {
        if (tfQuantity.getText().trim().length() <= 0) {
            new SceneLoader().showAlert(Alert.AlertType.ERROR, "Invalid Input", "Insert quantity!");
            return;
        }
        if (tfMoney.getText().trim().length() <= 0) {
            new SceneLoader().showAlert(Alert.AlertType.ERROR, "Invalid Input", "Insert price!");
            return;
        }
        if (Integer.parseInt(tfQuantity.getText()) > Integer.parseInt(curr.getStock())) {
            new SceneLoader().showAlert(Alert.AlertType.ERROR, "Invalid Input", "Quantity cannot exceed stock!");
            return;
        }
        if (Integer.parseInt(tfMoney.getText()) <= 0) {
            new SceneLoader().showAlert(Alert.AlertType.ERROR, "Invalid Input", "Cost cannot be 0!");
            return;
        }
        prodController.orderList.add(new CartItem(curr.getProductID(), Integer.parseInt(tfQuantity.getText()), Integer.parseInt(tfMoney.getText())));
        ((Stage) tfMoney.getScene().getWindow()).close();
    }

    @FXML
    private void btnCancelOnClick(ActionEvent event) {
        ((Stage) tfMoney.getScene().getWindow()).close();

    }

}
