/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Model.Customer;
import Utility.Database;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private boolean modify = false;
    private Integer id = -1; 
    ObservableList <Customer> listCustomer = FXCollections.observableArrayList();   
  
    
    
  
    
    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
            showTable();      
    }

    private void showTable(){
        try {
            listCustomer.clear();
            modify = false;
            Database db = new Database();
            db.connect();
            String Query = "SELECT * FROM CUSTOMER";        
            ResultSet rs = db.getResult(Query);                     
            int count = 0;
            //need to work           
            while(rs.next())
            {
               count++; 
               listCustomer.add(new Customer(rs.getInt("customerID"),rs.getString("customerName"),rs.getString("customerPhone"),rs.getString("addresses")));               
            }
            //System.out.println("count = "+count);
            if(count == 0)
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
        }
       
    }

    @FXML
    private void btnInsertCustomerOnClicked(ActionEvent event) {
       
            String name = tfName.getText();
            String phone = tfPhone.getText();
            String address = tfAddress.getText();
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
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Success");
                    if(!modify)
                    {
                       alert.setContentText("Data inserted successfully");
                    }
                    else
                    {
                        alert.setContentText("Data updated successfully");                        
                    }
                    alert.show();
                    btnInsertCustomer.setText("Insert Customer");
                    showTable();
                    resetFields();                    
                    db.disconnect();
                }catch (ClassNotFoundException| SQLException ex) {
                    System.out.println("Exception in insert customer:"+ex);
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
      id = selected.getCustomerID();
      tfName.setText(selected.getName());
      tfPhone.setText(selected.getPhone());
      tfAddress.setText(selected.getAddress());
      btnInsertCustomer.setText("Update Customer");
    }

    @FXML
    private void btnClearOnClicked(ActionEvent event) {
      resetFields();
    }
    
    @FXML
    private void btnDeleteOnClicked(ActionEvent event) {
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
            String query = "DELETE FROM CUSTOMER WHERE customerID = "+selected.getCustomerID();
            db.updateTable(query);
            db.disconnect();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Successful");
            alert.setContentText("Data deleted successfully");
            alert.show();
            showTable();
        }catch(ClassNotFoundException | SQLException ex) {
            System.out.println("Exception in btnDelete: "+ ex);
        }
    }

    private void resetFields() {
        tfName.setText("");
        tfPhone.setText("");
        tfAddress.setText("");
        modify = false;
        id = -1;
    }

    
    
}
