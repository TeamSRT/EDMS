/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Model.SupplyDetails;
import Utility.DataManager;
import Utility.Database;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author SrishtiPC
 */
public class SupplierUIDetailsController implements Initializable {

    @FXML
    private TableColumn<SupplyDetails, String> tcBrand;
    @FXML
    private TableColumn<SupplyDetails, String> tcModel;
    @FXML
    private TableColumn<SupplyDetails, Integer> tcQuantity;
    @FXML
    private TableColumn<SupplyDetails, Integer> tcCost;
    @FXML
    private TableColumn<SupplyDetails, String> tcSupplyDate;
    @FXML
    private TableView<SupplyDetails> tvSupplies;
    @FXML
    private TextField tfSupplierID;
    ObservableList<SupplyDetails> listSupplies = FXCollections.observableArrayList();
    int supplierID = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        supplierID = DataManager.selectedSupplier.getSupplierID() ;
        showSupplies();
        tfSupplierID.setText(supplierID + "");
    }

    private void showSupplies() {
        listSupplies.clear();
        try {
            
            Database db = new Database();
            db.connect();
            String query = "SELECT PRODUCT.Brand, PRODUCT.Model, SUPPLIES.Quantity, SUPPLIES.Cost, Convert(DATE,SUPPLIES.Date_) as supplyrDate FROM"
                    + " PRODUCT INNER JOIN SUPPLIES ON SUPPLIES.ProductID = PRODUCT.ProductID where SUPPLIES.supplierID = " + DataManager.selectedSupplier.getSupplierID();
            System.out.println("Query = " + query);
            ResultSet rs = db.getResult(query);
            while (rs.next()) {
                listSupplies.add(new SupplyDetails(rs.getString("Brand"), rs.getString("Model"), rs.getInt("quantity"), rs.getInt("Cost"), rs.getString("supplyrDate")));
            }
            db.disconnect();

        } catch (ClassNotFoundException | SQLException ex) {
            //ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Action");
            alert.setContentText("Unable to show details of the supplier!!!");
            alert.show();
            ((Stage) tfSupplierID.getScene().getWindow()).close();
        }
        tcBrand.setCellValueFactory(new PropertyValueFactory("Brand"));
        tcModel.setCellValueFactory(new PropertyValueFactory("Model"));
        tcQuantity.setCellValueFactory(new PropertyValueFactory("quantity"));
        tcCost.setCellValueFactory(new PropertyValueFactory("cost"));
        tcSupplyDate.setCellValueFactory(new PropertyValueFactory("supplyDate"));
        tvSupplies.setItems(listSupplies);

    }

}
