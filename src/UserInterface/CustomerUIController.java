/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Model.Customer;
import Utility.DataManager;
import Utility.Database;
import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.PauseTransition;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author SrishtiPC
 */
public class CustomerUIController implements Initializable {
    
    @FXML
    private TableView<Customer> tvCustomer;
    @FXML
    private TableColumn<Customer, Integer> tcCustomerID;
    @FXML
    private TableColumn<Customer, String> tcName;
    @FXML
    private TableColumn<Customer, String> tcPhone;
    @FXML
    private TableColumn<Customer, String> tcAddress;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfAddress;
    @FXML
    private TextField tfPhone;
    @FXML
    private Button btnInsertCustomer;
    @FXML
    private Button btnModify;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnDelete;
    @FXML
    private Label lblAlert;
    @FXML
    private TextField tfSearch;
    @FXML
    private MenuButton menuBtnSearch;
    @FXML
    private MenuItem mitemID;
    @FXML
    private MenuItem mitemName;
    @FXML
    private MenuItem mitemPhone;
    @FXML
    private MenuItem mitemAddress;
    @FXML
    private MenuItem mitemKeyword;
  
    private boolean modify = false;
    private int count = 0;
    private Integer id = -1;
    private String searchBy = "";
    ObservableList <Customer> listCustomer = FXCollections.observableArrayList();   
    @FXML
    private Button btnShowOrder;
    
  
    
    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
            showTable("SELECT * FROM CUSTOMER");
            searchBy = "Keyword";
    }

    private void showTable(String query){
        try {
            listCustomer.clear();
            modify = false;
            Database db = new Database();
            db.connect();               
            ResultSet rs = db.getResult(query);                     
            boolean check = false;
            while(rs.next())
            {
               check = true; 
               listCustomer.add(new Customer(rs.getInt("customerID"),rs.getString("customerName"),rs.getString("customerPhone"),rs.getString("addresses")));               
            }
            //System.out.println("count = "+count);
            if(check == false)
            {
                btnModify.setDisable(true);
                btnDelete.setDisable(true);
            }
            else
            {
                btnModify.setDisable(false);
                btnDelete.setDisable(false);
            }
            tcCustomerID.setCellValueFactory(new PropertyValueFactory("customerID"));
            tcName.setCellValueFactory(new PropertyValueFactory("name"));
            tcPhone.setCellValueFactory(new PropertyValueFactory("phone"));
            tcAddress.setCellValueFactory(new PropertyValueFactory("address"));
            tvCustomer.setItems(listCustomer);
            db.disconnect();            
        }catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Exception in show table:"+ex);
             Alert alert = new Alert(AlertType.ERROR);
             alert.setTitle("Can't Show");
             alert.setContentText("Unable to show table!");
             alert.show();
        }
       
    }

    @FXML
    private void btnInsertCustomerOnClicked(ActionEvent event) throws ClassNotFoundException {
       
            String name = tfName.getText().trim();
            String phone = tfPhone.getText().trim();
            String address = tfAddress.getText().trim();
            if(name.equals(""))
            {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Name field required");
                alert.setContentText("You can not keep the name field empty!");
                alert.show();
            }
            else if(phone.equals(""))
            {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Phone field required");
                alert.setContentText("You can not keep the phone field empty!");
                alert.show();
            }
            else
            {                
                try {
                    Database db = new Database();
                    db.connect();
                    String query;
                    if(!modify)
                    {   
                       query = "INSERT INTO CUSTOMER(customerName,customerPhone,addresses) VALUES ('"+name+"','"+phone+"','"+address+"')";
                    }
                    else
                    {                       
                        query = "UPDATE CUSTOMER SET customerName = '"+name+"', customerPhone = '"+phone+"',addresses = '"+address+"'"+"WHERE customerID = "+id+"";
                    } 
                    db.updateTable(query);                                 
                    if(!modify)
                    {
                       lblAlert.setText("Data inserted successfully!");                       
                    }
                    else
                    {
                        lblAlert.setText("Data updated successfully!");                                                
                    }
                    lblAlert.setOpacity(1);
                    btnInsertCustomer.setText("Insert Customer");
                    
                    PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    pause.setOnFinished(e -> resetFields());
                    pause.play();
                    showTable("SELECT * FROM CUSTOMER");  
                    db.disconnect();
                }catch (SQLException ex) {
                    System.out.println("Exception in insert customer:"+ex.getErrorCode());
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Failed operation");
                    if(ex.toString().contains("check_cusName"))
                    {
                        alert.setContentText("Please match the requested format in name field");                   
                    }
                    else if(ex.toString().contains("check_phone"))
                    {
                        alert.setContentText("Please match the requested format and length in phone field"); 
                    }
                    else if(ex.toString().contains("uc_phone"))
                    {
                        alert.setContentText("Phone number needs to be unique!!");
                    }
                    else
                    {
                        if(!modify)
                        {
                            alert.setContentText("Unable to insert data!");
                        }
                        else
                        {
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
      Customer selected = tvCustomer.getSelectionModel().getSelectedItem();
      if(selected == null)
      {
          Alert alert = new Alert(AlertType.ERROR);
          alert.setTitle("Can't modify");
          alert.setContentText("You need to select a row to modify!");
          alert.show();
      }
      else
      {    
        id = selected.getCustomerID();
        tfName.setText(selected.getName());
        tfPhone.setText(selected.getPhone());
        tfAddress.setText(selected.getAddress());
        btnInsertCustomer.setText("Update Customer");
      }
    }

    @FXML
    private void btnClearOnClicked(ActionEvent event) {
      resetFields();
    }
    
    @FXML
    private void btnDeleteOnClicked(ActionEvent event) {
        resetFields();
        try {
            Database db = new Database();
            db.connect();
            Customer selected = tvCustomer.getSelectionModel().getSelectedItem();
            if(selected == null)
            {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Can't Delete");
                alert.setContentText("You need to select a row to delete!");
                alert.show();
            }
            else
            {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setHeaderText("Are you sure?");
                alert.setContentText("Do you really want to delete? This process can't be undone!");
                ButtonType yes = new ButtonType("Yes");
                ButtonType no = new ButtonType("No");
                alert.getButtonTypes().setAll(yes,no);
                Optional<ButtonType> choice = alert.showAndWait();
                if(choice.get() == yes)
                {
                    String query = "DELETE FROM CUSTOMER WHERE customerID = "+selected.getCustomerID();
                    db.updateTable(query);
                    db.disconnect();            
                    lblAlert.setText("Data deleted successfully!");
                    lblAlert.setOpacity(1);
                    PauseTransition pause = new PauseTransition(Duration.seconds(1));
                    pause.setOnFinished(e -> lblAlert.setOpacity(0));
                    pause.play();
                }
            }
            showTable("SELECT * FROM CUSTOMER");  
           
        }catch(ClassNotFoundException | SQLException ex) {
             System.out.println("Exception in btnDelete: "+ ex);
             Alert alert = new Alert(AlertType.ERROR);
             alert.setTitle("Can't Delete");
             alert.setContentText("Unable to delete data!");
             alert.show();
        }
        
    }

    private void resetFields() {
        tfName.setText("");
        tfPhone.setText("");
        tfAddress.setText("");
        modify = false;
        id = -1;
        lblAlert.setOpacity(0);
        tfSearch.setText(""); 
        btnInsertCustomer.setText("Insert Customer");
        showTable("SELECT * FROM CUSTOMER");  
    }

   
    private void search() {
        if(tfSearch.getText().equals(""))
        {
            System.out.println("No text in the search field");
            showTable("SELECT * FROM CUSTOMER");  
        }
        else
        {
            String query;
            switch (searchBy) {
                case "Id":
                    query = "SELECT * FROM CUSTOMER WHERE customerID LIKE '%"+tfSearch.getText()+"%'";
                    System.out.println("ID chosen");
                    break;
                case "Name":
                    query = "SELECT * FROM CUSTOMER WHERE customerName LIKE '%"+tfSearch.getText()+"%'";
                    System.out.println("Name chosen");
                    break;
                case "Phone":
                    query = "SELECT * FROM CUSTOMER WHERE customerPhone LIKE '%"+tfSearch.getText()+"%'";
                    System.out.println("Phone Chosen");
                    break;
                case "Address":
                    query = "SELECT * FROM CUSTOMER WHERE addresses LIKE '%"+tfSearch.getText()+"%'";
                    System.out.println("Address Chosen");
                    break;
                default:   
                    query = "SELECT * FROM CUSTOMER WHERE customerID LIKE '%"+tfSearch.getText()+"%' OR "
                            + "customerName LIKE '%"+tfSearch.getText()+"%' OR "
                            + "addresses LIKE '%"+tfSearch.getText()+"%' OR "
                            + "customerPhone LIKE '%"+tfSearch.getText()+"%'";
                    System.out.println("Keyword chosen");
                    break;            
            }
            showTable(query);            
        }
    }

    @FXML
    private void searchByID(ActionEvent event) {
        searchBy = "Id";
        menuBtnSearch.setText("Id");       
        tfSearch.setPromptText("Search by ID");
        search();
    }

    @FXML
    private void searchByName(ActionEvent event) {
        searchBy = "Name";
        menuBtnSearch.setText("Name");           
        tfSearch.setPromptText("Search by Name");
        search();
    }

    @FXML
    private void searchByPhone(ActionEvent event) {
        searchBy = "Phone";
        menuBtnSearch.setText("Phone");     
        tfSearch.setPromptText("Search by Phone");
        search();
    }

    @FXML
    private void searchByAddress(ActionEvent event) {
        searchBy = "Address";
        menuBtnSearch.setText("Address");    
        tfSearch.setPromptText("Search by Address");
        search();
    }

    @FXML
    private void searchByKeyword(ActionEvent event) {
        searchBy = "Keyword";
        menuBtnSearch.setText("Keyword");     
        tfSearch.setPromptText("Search by Keyword");
        search();
    }
    
     //by keywords (on key release)  
    @FXML
    private void searchOnKeyRelease(KeyEvent event) {
        search();
    }

    @FXML
    private void btnShowOrderOnClicked(ActionEvent event) throws IOException {
        DataManager.selected = tvCustomer.getSelectionModel().getSelectedItem();
        if (DataManager.selected == null) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Action");
            alert.setContentText("You need to select a customer to show his order!");
            alert.show();
        } else {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
            Parent rootPane = FXMLLoader.load(getClass().getResource("/UserInterface/CustomerUIOrder.fxml"));
            Scene dialogScene = new Scene(rootPane);
            dialog.setScene(dialogScene);
            dialog.show();
        }
    }

 
    
    
}
