/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Model.CartItem;
import Model.Product;
import Utility.DataManager;
import Utility.Database;
import Utility.SceneLoader;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ktouf
 */
public class ProductUIController implements Initializable {

    @FXML
    private TableView<Product> tvProduct;
    @FXML
    private TableColumn<Product, String> tcBrand;
    @FXML
    private TableColumn<Product, String> tcModel;
    @FXML
    private TableColumn<Product, String> tcWarranty;
    @FXML
    private TableColumn<Product, String> tcPrice;
    @FXML
    private TableColumn<Product, String> tcDescription;
    @FXML
    private TableColumn<Product, String> tcStock;
    @FXML
    private TableColumn<Product, String> tcProductID;
    @FXML
    private TableColumn<Product, String> tcType;

    ObservableList<Product> listProduct = FXCollections.observableArrayList();
    private TextField tfProductID;
    @FXML
    private TextField tfBrand;
    @FXML
    private TextField tfModel;

    @FXML
    private Button btnModify;
    @FXML
    private Button btnDelete;
    @FXML
    private ComboBox<String> cbType;
    private Label lblProductID;
    @FXML
    private CheckBox cbInStock;
    @FXML
    private Button btnCreateProduct;
    @FXML
    private Button btnViewProduct;
    @FXML
    private TextField tfPriceMin;
    @FXML
    private TextField tfpriceMax;
    @FXML
    private Button btnAddToOrder;
    public List<CartItem> orderList = new ArrayList<CartItem>();

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbType.getItems().add("All");
        cbType.getItems().add("Processor");
        cbType.getItems().add("Motherboard");
        cbType.getItems().add("RAM");
        cbType.getItems().add("GPU");
        cbType.getItems().add("PSU");
        cbType.getItems().add("Other");
        cbType.getSelectionModel().selectFirst();
        loadProductTable("SELECT * FROM PRODUCT");
    }

    public void loadProductTable(String query) {
        listProduct.clear();
        ResultSet rsProduct = null;
        try {
            //String query = "SELECT * FROM PRODUCT";
            Database db = new Database();
            db.connect();
            rsProduct = db.getResult(query);
            while (rsProduct.next()) {
                listProduct.add(new Product(rsProduct.getInt(1), rsProduct.getString(2), rsProduct.getString(3),
                        rsProduct.getInt(4), rsProduct.getInt(5), rsProduct.getString(6), rsProduct.getString(7),
                        rsProduct.getString(8)));
            }
            db.disconnect();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductUIController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        tcProductID.setCellValueFactory(new PropertyValueFactory<>("productID"));
        tcBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        tcModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        tcWarranty.setCellValueFactory(new PropertyValueFactory<>("warranty"));
        tcType.setCellValueFactory(new PropertyValueFactory<>("type"));
        tcPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        tcDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        tcStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        tvProduct.setItems(listProduct);
    }

    @FXML
    private void btnModifyOnCliked(ActionEvent event) throws IOException {
        Product curr = tvProduct.getSelectionModel().getSelectedItem();
        if (curr == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Action");
            alert.setContentText("Select a product from table to modify!");
            alert.show();
            return;
        }
        DataManager.createProdIsEdit = true;
        DataManager.createProductId = curr.getProductID();
        DataManager.createProdType = curr.getType();
        DataManager.createProdIsView = false;

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/CreateProduct.fxml"));

        Parent rootPane = (Parent) loader.load();
        Scene dialogScene = new Scene(rootPane);
        dialog.setScene(dialogScene);
        dialog.show();

    }

    private void resetFields() {
        tfPriceMin.setText("");
        tfpriceMax.setText("");
        tfBrand.setText("");
        tfModel.setText("");
        cbInStock.setSelected(false);
    }

    @FXML
    private void btnDeleteOnClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        Product curr = tvProduct.getSelectionModel().getSelectedItem();
        if (curr == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Action");
            alert.setContentText("Select a product from table to delete!");
            alert.show();
            return;
        }
        String query = "DELETE FROM PRODUCT WHERE ProductID = " + curr.getProductID();
        Database db = new Database();
        db.connect();
        db.updateTable(query);
        db.disconnect();
        loadProductTable("SELECT * FROM PRODUCT");
    }

    private void btnOrderOnClicked(ActionEvent event) throws IOException {

        DataManager.product = tvProduct.getSelectionModel().getSelectedItem();
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/ProductUIOrder.fxml"));

        Parent rootPane = (Parent) loader.load();
        ProductUIOrderController controller = loader.getController();
        controller.setProductTableView(this);
        Scene dialogScene = new Scene(rootPane);
        dialog.setScene(dialogScene);
        dialog.show();

    }

    @FXML
    private void btnCreateProductOnClick(ActionEvent event) throws IOException {
        DataManager.createProdIsEdit = false;
        DataManager.createProdIsView = false;

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/CreateProduct.fxml"));

        Parent rootPane = (Parent) loader.load();
        Scene dialogScene = new Scene(rootPane);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    @FXML
    private void btnViewProductOnClick(ActionEvent event) throws IOException {
        Product curr = tvProduct.getSelectionModel().getSelectedItem();
        if (curr == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Action");
            alert.setContentText("Select a product from table to view!");
            alert.show();
            return;
        }
        DataManager.createProdIsEdit = true;
        DataManager.createProductId = curr.getProductID();
        DataManager.createProdType = curr.getType();
        DataManager.createProdIsView = true;

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/CreateProduct.fxml"));

        Parent rootPane = (Parent) loader.load();
        Scene dialogScene = new Scene(rootPane);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    @FXML
    private void btnFilterOnAction(ActionEvent event) {
        int priceMin = Integer.parseInt(tfPriceMin.getText().equals("") ? "0" : tfPriceMin.getText());
        int priceMax = Integer.parseInt(tfpriceMax.getText().equals("") ? "999999" : tfpriceMax.getText());
        if (priceMin > priceMax) {
            new SceneLoader().showAlert(AlertType.ERROR, "Invalid Input", "Minimum price cannot be larger than Max");
            return;
        }
        String brand = tfBrand.getText().equals("") ? "%%" : tfBrand.getText();
        String model = tfModel.getText().equals("") ? "%%" : tfModel.getText();
        String type = cbType.getSelectionModel().getSelectedItem().equals("All") ? "%%" : cbType.getSelectionModel().getSelectedItem();
        String query = "SELECT * FROM PRODUCT WHERE "
                + "Brand LIKE '" + brand + "' "
                + "AND Model LIKE '" + model + "' "
                + "AND productType LIKE '" + type + "' "
                + "AND Price >= " + priceMin + " "
                + "AND Price <= " + priceMax;
        if (cbInStock.isSelected()) {
            query += " AND Stock > 0";
        }
        loadProductTable(query);
    }

    @FXML
    private void btnClearFieldsOnAction(ActionEvent event) {
        resetFields();
        loadProductTable("SELECT * FROM PRODUCT");
    }

    @FXML
    private void btnRefreshOnClick(ActionEvent event) {
        loadProductTable("SELECT * FROM PRODUCT");
    }

    @FXML
    private void btnAddToOrderOnAction(ActionEvent event) throws IOException {
        Product curr = tvProduct.getSelectionModel().getSelectedItem();
        if (curr == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Action");
            alert.setContentText("Select a product from table to add!");
            alert.show();
            return;
        }
        DataManager.prodController = this;
        DataManager.orderSelect = curr;
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/CartItemUI.fxml"));

        Parent rootPane = (Parent) loader.load();
        Scene dialogScene = new Scene(rootPane);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    @FXML
    private void btnShowOrderOnAction(ActionEvent event) throws IOException {
        if (orderList.size() <= 0) {
            new SceneLoader().showAlert(AlertType.ERROR, "Empty!", "Order cart is empty!");
        } else {
            DataManager.orderList = orderList;
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/CartUI.fxml"));

            Parent rootPane = (Parent) loader.load();
            Scene dialogScene = new Scene(rootPane);
            dialog.setScene(dialogScene);
            dialog.show();
        }
    }
}
