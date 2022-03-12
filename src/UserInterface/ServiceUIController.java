/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Model.Service;
import Utility.Database;
import Utility.SceneLoader;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author SrishtiPC
 */
public class ServiceUIController implements Initializable {

    @FXML
    private Label lblAlert;
    @FXML
    private Button btnModify;
    @FXML
    private Button btnDelete;
    @FXML
    private TextField tfSearch;
    @FXML
    private MenuButton menuBtnSearch;

    private boolean modify = false;
    private int count = 0;
    private Integer id = -1;
    private String searchBy = "";
    ObservableList<Service> listService = FXCollections.observableArrayList();
    @FXML
    private TableView<Service> tvService;
    @FXML
    private TableColumn<Service, Integer> tcServiceID;
    @FXML
    private TableColumn<Service, Integer> tcProductID;
    @FXML
    private TableColumn<Service, Integer> tcCustomerID;
    @FXML
    private TableColumn<Service, String> tcDetails;
    @FXML
    private TableColumn<Service, Integer> tcServiceCharge;
      @FXML
    private TableColumn<Service, String> tcServiceStatus;
    @FXML
    private TableColumn<Service, String> tcGivenDate;
    @FXML
    private Button btnInsert;
    @FXML
    private Button btnReset;
    @FXML
    private TextField tfProductID;
    @FXML
    private TextField tfCustomerID;
    @FXML
    private TextField tfCharge;
    @FXML
    private TextArea tfDetails;
    private AnchorPane tfServiceCharge;
    @FXML
    private TextField tfSearch2;
    @FXML
    private MenuItem mitemServiceID;
    @FXML
    private MenuItem mitemProductID;
    @FXML
    private MenuItem mitemCustomerID;
    @FXML
    private MenuItem mitemDetails;
    @FXML
    private MenuItem mitemServiceCharge;
  

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showTable("SELECT * FROM SERVICE_");
        searchBy = "serviceID";
    }

    private void showTable(String query) {
        try {
            listService.clear();
            modify = false;
            Database db = new Database();
            db.connect();
            ResultSet rs = db.getResult(query);
            boolean check = false;
            while (rs.next()) {
                check = true;
                listService.add(new Service(rs.getInt("serviceID"), rs.getInt("productID"), rs.getInt("customerID"), rs.getString("details"), rs.getInt("serviceCharge"), rs.getString("serviceStatus"),rs.getString("givenDate")));
            }
            if (check == false) {
                btnModify.setDisable(true);
                btnDelete.setDisable(true);
            } else {
                btnModify.setDisable(false);
                btnDelete.setDisable(false);
            }
            tcServiceID.setCellValueFactory(new PropertyValueFactory("serviceID"));
            tcProductID.setCellValueFactory(new PropertyValueFactory("productID"));
            tcCustomerID.setCellValueFactory(new PropertyValueFactory("customerID"));
            tcDetails.setCellValueFactory(new PropertyValueFactory("details"));           
            tcServiceStatus.setCellValueFactory(new PropertyValueFactory("serviceStatus"));
            tcServiceCharge.setCellValueFactory(new PropertyValueFactory("serviceCharge"));
            tcGivenDate.setCellValueFactory(new PropertyValueFactory("givenDate"));
            tvService.setItems(listService);
            db.disconnect();
        } catch (ClassNotFoundException | SQLException ex) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Can't Show");
            alert.setContentText("Unable to show table!");
            alert.show();
        }

    }

    @FXML
    private void btnInsertOnClick(ActionEvent event) throws ClassNotFoundException {
        String productID = tfProductID.getText().trim();
        String customerID = tfCustomerID.getText().trim();
        String details = tfDetails.getText().trim();
        String serviceCharge = tfCharge.getText().trim();
        Alert alert = new Alert(AlertType.ERROR);
        if (productID.length() == 0) {
            new SceneLoader().showAlert(AlertType.ERROR, "Product ID required!", "You can not keep the productID field empty!");
        } else if (customerID.length() == 0) {
            new SceneLoader().showAlert(AlertType.ERROR, "Customer ID required!", "You can not keep the customerID field empty!");
        } else if (details.length() == 0) {
            new SceneLoader().showAlert(AlertType.ERROR, "Details required!", "You can not keep the details field empty!");
        } else if (serviceCharge.length() == 0) {
            new SceneLoader().showAlert(AlertType.ERROR, "Service charge required!", "You can not keep the Service Charge field empty!");
        } else {
            try {
                Database db = new Database();
                db.connect();
                String query;
                if (!modify) {
                    query = "INSERT INTO SERVICE_(productID, customerID, details, serviceCharge) VALUES"
                            + "(" + productID + "," + customerID + ",'" + details + "'," + serviceCharge + ")";
                    //System.out.println("query = "+query);
                } else {
                    query = "UPDATE SERVICE_ SET productID = " + productID + ", customerID = " + customerID + ", details = '" + details + "', serviceCharge = " + serviceCharge + " where serviceID = " + id + "";
                    System.out.println("update query" + query);
                }
                db.updateTable(query);
                if (!modify) {
                    lblAlert.setText("Data inserted successfully!");
                } else {
                    lblAlert.setText("Data updated successfully!");
                }
                lblAlert.setOpacity(1);
                btnInsert.setText("Insert Service");
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(e -> resetFields());
                pause.play();
                showTable("SELECT * FROM SERVICE_");
                db.disconnect();
            } catch (SQLException ex) {
                System.out.println("Exception in insert service:" + ex.getErrorCode());
                alert.setTitle("Failed operation");
                if (ex.toString().contains("chk_charge")) {
                    alert.setContentText("Service charge can not be less than 0!");
                } else if (ex.toString().contains("fk_service_customerID")) {
                    alert.setContentText("Customer doesn't exist!");
                } else if (ex.toString().contains("fk_service_productID")) {
                    alert.setContentText("Product doesn't exist!");
                } else {
                    if (!modify) {
                        alert.setContentText("Unable to insert data!");
                    } else {
                        alert.setContentText("Unable to modify data!");
                    }
                }
                alert.show();
            }
        }
        
    }

    @FXML
    private void btnModifyOnCliked(ActionEvent event) {
        modify = true;
        Service selected = tvService.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Can't modify");
            alert.setContentText("You need to select a row to modify!");
            alert.show();
        } else {
            id = selected.getServiceID();
            tfProductID.setText(selected.getProductID() + "");
            tfCustomerID.setText(selected.getCustomerID() + "");
            tfDetails.setText(selected.getDetails());
            tfCharge.setText(selected.getServiceCharge() + "");
            btnInsert.setText("Update Service");
        }
    }

    private void resetFields() {
        tfProductID.setText("");
        tfCustomerID.setText("");
        tfCharge.setText("");
        tfDetails.setText("");
        modify = false;
        id = -1;
        lblAlert.setOpacity(0);
        tfSearch.setText("");
        btnInsert.setText("Insert Service");
        showTable("SELECT * FROM SERVICE_");
    }

    @FXML
    private void btnDeleteOnClicked(ActionEvent event) throws ClassNotFoundException {
        resetFields();
        try {
            Database db = new Database();
            db.connect();
            Service selected = tvService.getSelectionModel().getSelectedItem();
            if (selected == null) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Can't Delete");
                alert.setContentText("You need to select a row to delete!");
                alert.show();
            } else {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setHeaderText("Are you sure?");
                alert.setContentText("Do you really want to delete? This process can't be undone!");
                ButtonType yes = new ButtonType("Yes");
                ButtonType no = new ButtonType("No");
                alert.getButtonTypes().setAll(yes, no);
                Optional<ButtonType> choice = alert.showAndWait();
                if (choice.get() == yes) {
                    String query = "DELETE FROM SERVICE_ WHERE serviceID = " + selected.getServiceID();
                    db.updateTable(query);
                    db.disconnect();
                    lblAlert.setText("Data deleted successfully!");
                    lblAlert.setOpacity(1);
                    PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    pause.setOnFinished(e -> lblAlert.setOpacity(0));
                    pause.play();
                }
            }            
            showTable("SELECT * FROM SERVICE_");
        } catch (SQLException ex) {
            System.out.println("Exception in btnDelete FOR SERVICING: " + ex);
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Can't Delete");
            alert.setContentText("Unable to delete data!");
            alert.show();
        }

    }

    @FXML
    private void searchOnKeyRelease(KeyEvent event) {
        search();
    }


    @FXML
    private void btnResetOnClick(ActionEvent event) {
        resetFields();
    }

    @FXML
    private void searchByServiceID(ActionEvent event) {
        tfSearch2.setDisable(true);
        tfSearch2.setOpacity(0);
        searchBy = "serviceID";
        menuBtnSearch.setText("Service ID");       
        tfSearch.setPromptText("Search by Service ID");
        search();
    }

    @FXML
    private void searchByProductID(ActionEvent event) {
        tfSearch2.setDisable(true);
        tfSearch2.setOpacity(0);
        searchBy = "productID";
        menuBtnSearch.setText("Product ID");       
        tfSearch.setPromptText("Search by Product ID");
        search();
    }

    @FXML
    private void searchByCustomerID(ActionEvent event) {
        tfSearch2.setDisable(true);
        tfSearch2.setOpacity(0);
        searchBy = "customerID";
        menuBtnSearch.setText("Customer ID");       
        tfSearch.setPromptText("Search by Customer ID");
        search();
    }

    @FXML
    private void searchByDetails(ActionEvent event) {
        tfSearch2.setDisable(true);
        tfSearch2.setOpacity(0);
        searchBy = "details";
        menuBtnSearch.setText("Details");       
        tfSearch.setPromptText("Search by details");
        search();
    }

    @FXML
    private void searchByServiceCharge(ActionEvent event){
        searchBy = "serviceCharge";
        menuBtnSearch.setText("Service Charge");       
        tfSearch.setPromptText("Search by minimum service charge");
        tfSearch2.setOpacity(1);
        tfSearch2.setPromptText("Search by maximum service charge");
        tfSearch2.setDisable(false);
        search();
    }
    private void search() {
        if(tfSearch.getText().equals(""))
        {
            System.out.println("No text in the search field");
            showTable("SELECT * FROM SERVICE_");  
        }       
        else
        {
            String query;
            switch (searchBy) {               
                case "productID":
                    query = "SELECT * FROM SERVICE_ WHERE productID LIKE '%"+tfSearch.getText()+"%'";
                    System.out.println("ProductID chosen");
                    break;
                case "customerID":
                    query = "SELECT * FROM SERVICE_ WHERE customerID LIKE '%"+tfSearch.getText()+"%'";
                    System.out.println("CustomerID Chosen");
                    break;
                case "details":
                    query = "SELECT * FROM SERVICE_ WHERE details LIKE '%"+tfSearch.getText()+"%'";
                    System.out.println("Details Chosen");
                    break;
                case "serviceCharge":
                    int chargeMin = Integer.parseInt(tfSearch.getText().equals("") ? "0" : tfSearch.getText());
                     int chargeMax = Integer.parseInt(tfSearch2.getText().equals("") ? "999999" : tfSearch2.getText());
                     if (chargeMin > chargeMax) {
                          new SceneLoader().showAlert(AlertType.ERROR, "Invalid Input", "Minimum price cannot be larger than Max");
                         return;
                    }
                    query = "SELECT * FROM SERVICE_ WHERE serviceCharge >= "+chargeMin+" AND serviceCharge <= "+chargeMax;        
                    System.out.println("Service charge query = "+query);
                    break;
                default:   
                    query = "SELECT * FROM SERVICE_ WHERE serviceID LIKE '%"+tfSearch.getText()+"%'";                          
                    System.out.println("Service ID chosen");
                    break;            
            }
            showTable(query);            
        }
       
    }

}
