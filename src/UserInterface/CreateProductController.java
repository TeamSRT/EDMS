/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UserInterface;

import Utility.DataManager;
import Utility.Database;
import Utility.SceneLoader;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author ktouf
 */
public class CreateProductController implements Initializable {

    @FXML
    private TextField tfProductID;
    @FXML
    private TextField tfBrand;
    @FXML
    private TextField tfModel;
    @FXML
    private TextField tfWarranty;
    @FXML
    private TextField tfPrice;
    @FXML
    private TextArea tfDescription;
    @FXML
    private Label lblProductID;
    @FXML
    private ComboBox<String> cbType;
    @FXML
    private Pane paneMotherboard;
    @FXML
    private TextField tfChipset;
    @FXML
    private Label lblChipset;
    @FXML
    private TextField tfSocket;
    @FXML
    private Label lblChipset1;
    @FXML
    private Label lblChipset11;
    @FXML
    private TextField tfRamType;
    @FXML
    private Label lblChipset111;
    @FXML
    private TextField tfRamCapacity;
    @FXML
    private Label lblChipset1111;
    @FXML
    private Pane paneProcessor;
    @FXML
    private TextField tfCoreCount;
    @FXML
    private TextField tfCpuClock;
    @FXML
    private TextField tfCache;
    @FXML
    private Pane paneRAM;
    @FXML
    private TextField tfFreq;
    @FXML
    private TextField tfDDRType;
    @FXML
    private TextField tfCapacity;
    @FXML
    private Label lblChipset1112;
    @FXML
    private Pane panePSU;
    @FXML
    private TextField tfContPower;
    @FXML
    private TextField tfACInput;
    @FXML
    private TextField tfDCoutput;
    @FXML
    private Label lblChipset11121;
    @FXML
    private Pane paneGPU;
    @FXML
    private TextField tfClockSpeed;
    @FXML
    private TextField tfVRAM;
    @FXML
    private TextField tfMemoryClock;
    @FXML
    private Label lblChipset111211;
    @FXML
    private CheckBox cbAdditional;
    @FXML
    private TextField tfPCI;
    @FXML
    private AnchorPane anchorMain;
    public boolean isEdit = false;
    public boolean isView = false;
    public int eProductID = 0;
    public String eProductType = "";
    @FXML
    private TextField tfStock;
    @FXML
    private Label lblStock;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnCreate;
    @FXML
    private Label lblNoAdd;
    
    ProductUIController prodControl;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //prodControl = DataManager.prodControl;
        isEdit = DataManager.createProdIsEdit;
        eProductID = DataManager.createProductId;
        eProductType = DataManager.createProdType;
        isView = DataManager.createProdIsView;
        cbType.getItems().add("Processor");
        cbType.getItems().add("Motherboard");
        cbType.getItems().add("RAM");
        cbType.getItems().add("GPU");
        cbType.getItems().add("PSU");
        cbType.getItems().add("Other");
        cbType.getSelectionModel().selectFirst();
        hideAllPane();
        cbType.setDisable(false);
        cbType.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                hideAllPane();
                if (cbAdditional.isSelected()) {
                    showSelectedPane();
                }
            }
        });

        cbAdditional.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    showSelectedPane();
                } else {
                    hideAllPane();
                }
            }
        });

        if (isEdit) {
            hideAllPane();
            lblStock.setVisible(true);
            tfStock.setEditable(true);
            tfStock.setVisible(true);
            tfStock.setDisable(false);
            tfProductID.setVisible(true);
            lblProductID.setVisible(true);
            String query = "SELECT * "
                    + "FROM PRODUCT "
                    + "LEFT JOIN " + eProductType
                    + " ON Product.productID = " + eProductType + ".productID "
                    + "WHERE Product.productID = " + eProductID;
            System.out.println(query);
            try {
                Database db = new Database();
                db.connect();
                ResultSet rs = db.getResult(query);
                if (rs.next()) {
                    tfProductID.setText(rs.getString("ProductID"));
                    tfProductID.setEditable(false);
                    tfBrand.setText(rs.getString("Brand"));
                    tfModel.setText(rs.getString("Model"));
                    tfWarranty.setText(rs.getInt("Warranty") + "");
                    tfPrice.setText(rs.getInt("Price") + "");
                    tfDescription.setText(rs.getString("Details"));
                    tfStock.setText(rs.getInt("Stock") + "");
                    cbType.getSelectionModel().select(rs.getString("productType"));
                    showSelectedPane();

                    if (eProductType.equals("Motherboard")) {
                        tfChipset.setText(rs.getString("Chipset"));
                        tfSocket.setText(rs.getString("Socket"));
                        tfPCI.setText(rs.getInt("pcieSlots") + "");
                        tfRamType.setText(rs.getString("ramType"));
                        tfRamCapacity.setText(rs.getInt("ramCapacity") + "");

                    }
                    if (eProductType.equals("Processor")) {
                        tfCoreCount.setText(rs.getInt("coreCount") + "");
                        tfCpuClock.setText(rs.getInt("clockSpeed") + "");
                        tfCache.setText(rs.getInt("Cache") + "");
                    }
                    if (eProductType.equals("RAM")) {
                        tfFreq.setText(rs.getFloat("Frequency") + "");
                        tfDDRType.setText(rs.getString("ddrType"));
                        tfCapacity.setText(rs.getString("Capacity"));
                    }
                    if (eProductType.equals("PSU")) {
                        tfContPower.setText(rs.getInt("contPower") + "");
                        tfACInput.setText(rs.getFloat("ACInput") + "");
                        tfDCoutput.setText(rs.getString("DCOutput") + "");
                    }
                    if (eProductType.equals("GPU")) {
                        tfClockSpeed.setText(rs.getInt("clockSpeed") + "");
                        tfVRAM.setText(rs.getFloat("vRam") + "");
                        tfMemoryClock.setText(rs.getInt("memClock") + "");
                    }
                    if (!rs.wasNull()) {
                        cbAdditional.setSelected(true);
                        cbAdditional.setDisable(true);
                    }
                    btnCreate.setText("Update Product");
                    if (isView) {
                        if (rs.wasNull()) {
                            lblNoAdd.setVisible(true);
                        }
                        btnCreate.setVisible(false);
                        btnCreate.setDisable(false);
                        setAllEditable(false);
                        cbType.setEditable(false);
                        cbType.setDisable(true);
                        cbType.getEditor().setEditable(false);
                        cbAdditional.setDisable(true);
                        cbAdditional.setVisible(false);
                    }
                }
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(CreateProductController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    private void hideAllPane() {
        paneMotherboard.setDisable(true);
        paneMotherboard.setVisible(false);
        paneGPU.setDisable(true);
        paneGPU.setVisible(false);
        panePSU.setDisable(true);
        panePSU.setVisible(false);
        paneProcessor.setDisable(true);
        paneProcessor.setVisible(false);
        paneRAM.setDisable(true);
        paneRAM.setVisible(false);
    }

    private void showSelectedPane() {
        String newValue = cbType.getSelectionModel().getSelectedItem();
        cbAdditional.setVisible(true);
        if (newValue == "Processor") {
            paneProcessor.setDisable(false);
            paneProcessor.setVisible(true);
            paneProcessor.setLayoutX(21);
            paneProcessor.setLayoutY(319);
        } else if (newValue == "Motherboard") {
            paneMotherboard.setDisable(false);
            paneMotherboard.setVisible(true);
            paneMotherboard.setLayoutX(21);
            paneMotherboard.setLayoutY(319);
        } else if (newValue == "RAM") {
            paneRAM.setDisable(false);
            paneRAM.setVisible(true);
            paneRAM.setLayoutX(21);
            paneRAM.setLayoutY(319);
        } else if (newValue == "GPU") {
            paneGPU.setDisable(false);
            paneGPU.setVisible(true);
            paneGPU.setLayoutX(21);
            paneGPU.setLayoutY(319);
        } else if (newValue == "PSU") {
            panePSU.setDisable(false);
            panePSU.setVisible(true);
            panePSU.setLayoutX(21);
            panePSU.setLayoutY(319);
        } else {
            hideAllPane();
            cbAdditional.setVisible(false);
            cbAdditional.setSelected(false);
        }
    }

    @FXML
    private void btnCreateProductOnClick(ActionEvent event) {
        if (tfBrand.getText().trim().length() <= 0) {
            new SceneLoader().showAlert(Alert.AlertType.ERROR, "Blank Field", "Brand cannot be empty!");
        } else if (tfModel.getText().trim().length() <= 0) {
            new SceneLoader().showAlert(Alert.AlertType.ERROR, "Blank Field", "Model cannot be empty!");
        } else if (tfWarranty.getText().trim().length() <= 0) {
            new SceneLoader().showAlert(Alert.AlertType.ERROR, "Blank Field", "Warranty cannot be empty!");
        } else if (tfPrice.getText().trim().length() <= 0) {
            new SceneLoader().showAlert(Alert.AlertType.ERROR, "Blank Field", "Price cannot be empty!");
        } else if (tfDescription.getText().trim().length() <= 0) {
            new SceneLoader().showAlert(Alert.AlertType.ERROR, "Blank Field", "Description cannot be empty!");
        } else if (cbAdditional.isSelected()) {
            if (cbType.getSelectionModel().getSelectedItem() == "Motherboard") {
                if (tfChipset.getText().trim().length() <= 0) {
                    new SceneLoader().showAlert(Alert.AlertType.ERROR, "Blank Field", "Chipset cannot be empty!");
                } else if (tfSocket.getText().trim().length() <= 0) {
                    new SceneLoader().showAlert(Alert.AlertType.ERROR, "Blank Field", "Socket cannot be empty!");
                } else if (tfPCI.getText().trim().length() <= 0) {
                    new SceneLoader().showAlert(Alert.AlertType.ERROR, "Blank Field", "PCIe Slots cannot be empty!");
                } else if (tfRamType.getText().trim().length() <= 0) {
                    new SceneLoader().showAlert(Alert.AlertType.ERROR, "Blank Field", "Ram type cannot be empty!");
                } else if (tfRamCapacity.getText().trim().length() <= 0) {
                    new SceneLoader().showAlert(Alert.AlertType.ERROR, "Blank Field", "Ram Capacity cannot be empty!");
                } else {

                }
            }
            if (cbType.getSelectionModel().getSelectedItem() == "Processor") {
                System.out.println("HERE");
                if (tfCoreCount.getText().trim().length() <= 0) {
                    new SceneLoader().showAlert(Alert.AlertType.ERROR, "Blank Field", "Core count cannot be empty!");
                } else if (tfCpuClock.getText().trim().length() <= 0) {
                    new SceneLoader().showAlert(Alert.AlertType.ERROR, "Blank Field", "Cpu clock cannot be empty!");
                } else if (tfCache.getText().trim().length() <= 0) {
                    new SceneLoader().showAlert(Alert.AlertType.ERROR, "Blank Field", "Cache Slots cannot be empty!");
                } else {

                }
            }
            if (cbType.getSelectionModel().getSelectedItem() == "RAM") {
                if (tfFreq.getText().trim().length() <= 0) {
                    new SceneLoader().showAlert(Alert.AlertType.ERROR, "Blank Field", "Frequency cannot be empty!");
                } else if (tfDDRType.getText().trim().length() <= 0) {
                    new SceneLoader().showAlert(Alert.AlertType.ERROR, "Blank Field", "DDR Type cannot be empty!");
                } else if (tfCapacity.getText().trim().length() <= 0) {
                    new SceneLoader().showAlert(Alert.AlertType.ERROR, "Blank Field", "Capacity cannot be empty!");
                } else {

                }
            }
            if (cbType.getSelectionModel().getSelectedItem() == "PSU") {
                if (tfContPower.getText().trim().length() <= 0) {
                    new SceneLoader().showAlert(Alert.AlertType.ERROR, "Blank Field", "Continuous power cannot be empty!");
                } else if (tfACInput.getText().trim().length() <= 0) {
                    new SceneLoader().showAlert(Alert.AlertType.ERROR, "Blank Field", "AC Input cannot be empty!");
                } else if (tfDCoutput.getText().trim().length() <= 0) {
                    new SceneLoader().showAlert(Alert.AlertType.ERROR, "Blank Field", "DC Output cannot be empty!");
                } else {

                }
            }
            if (cbType.getSelectionModel().getSelectedItem() == "GPU") {
                if (tfClockSpeed.getText().trim().length() <= 0) {
                    new SceneLoader().showAlert(Alert.AlertType.ERROR, "Blank Field", "Clock speed cannot be empty!");
                } else if (tfVRAM.getText().trim().length() <= 0) {
                    new SceneLoader().showAlert(Alert.AlertType.ERROR, "Blank Field", "VRAM cannot be empty!");
                } else if (tfMemoryClock.getText().trim().length() <= 0) {
                    new SceneLoader().showAlert(Alert.AlertType.ERROR, "Blank Field", "Memory clock cannot be empty!");
                } else {

                }
            }
        }
        try {
            String brand = tfBrand.getText();
            String model = tfModel.getText();
            String warranty = tfWarranty.getText();
            String price = tfPrice.getText();
            String description = tfDescription.getText();
            String type = cbType.getSelectionModel().getSelectedItem();
            String stock = tfStock.getText();
            String query = "INSERT INTO PRODUCT(productType, Brand, Model, Warranty, Price, Details) VALUES('" + type + "','" + brand + "','" + model + "'," + warranty + "," + price + ",'" + description + "')";
            if (isEdit) {
                query = "UPDATE PRODUCT SET productType = '" + type + "', "
                        + "Brand = '" + brand + "', "
                        + "Model = '" + model + "', "
                        + "Warranty = '" + warranty + "', "
                        + "Price = '" + price + "', "
                        + "Details = '" + description + "', "
                        + "Stock = " + stock + " "
                        + "WHERE ProductID = '" + eProductID + "'";
            }
            Database db = new Database();
            db.connect();
            System.out.println(query);
            int productID = 0;
            if (!isEdit) {
                ResultSet rsPID = db.updateTableWithKeys(query);
                if (rsPID.next()) {
                    productID = rsPID.getInt(1);
                }
            } else {
                productID = eProductID;
                db.updateTable(query);
            }

            if (cbAdditional.isSelected()) {
                String queryAdd = "";
                if (cbType.getSelectionModel().getSelectedItem() == "Motherboard") {
                    queryAdd = "INSERT INTO MOTHERBOARD(Chipset, Socekt, pcieSlots, ramType, ramCapacity, productID) VALUES(?, ?, ?, ?, ?, ?)";
                    if (isEdit) {
                        queryAdd = "UPDATE MOTHERBOARD SET Chipset = ?, Socket = ?, pcieSlots = ?, ramType = ?,"
                                + "ramCapacity = ? WHERE productID = ?";
                    }
                    PreparedStatement ps = db.createStatement(queryAdd);
                    ps.setString(1, tfChipset.getText());
                    ps.setString(2, tfSocket.getText());
                    ps.setInt(3, Integer.parseInt(tfPCI.getText()));
                    ps.setString(4, tfRamType.getText());
                    ps.setInt(5, Integer.parseInt(tfRamCapacity.getText()));
                    ps.setInt(6, productID);
                    int rows = ps.executeUpdate();
                    if (isEdit && rows == 0) {
                        ps = db.createStatement("INSERT INTO MOTHERBOARD(Chipset, Socekt, pcieSlots, ramType, ramCapacity, productID) VALUES(?, ?, ?, ?, ?, ?)");
                        ps.setString(1, tfChipset.getText());
                        ps.setString(2, tfSocket.getText());
                        ps.setInt(3, Integer.parseInt(tfPCI.getText()));
                        ps.setString(4, tfRamType.getText());
                        ps.setInt(5, Integer.parseInt(tfRamCapacity.getText()));
                        ps.setInt(6, productID);
                        ps.executeUpdate();
                    }
                }
                if (cbType.getSelectionModel().getSelectedItem() == "Processor") {
                    queryAdd = "INSERT INTO Processor(coreCount, clockSpeed, Cache, productID) VALUES(?, ?, ?, ?)";
                    if (isEdit) {
                        queryAdd = "UPDATE Processor SET coreCount = ?, clockSpeed = ?, Cache = ?"
                                + " WHERE productID = ?";
                    }
                    PreparedStatement ps = db.createStatement(queryAdd);

                    ps.setInt(1, Integer.parseInt(tfCoreCount.getText()));
                    ps.setFloat(2, Float.parseFloat(tfCpuClock.getText()));
                    ps.setInt(3, Integer.parseInt(tfCache.getText()));
                    ps.setInt(4, productID);
                    int rows = ps.executeUpdate();
                    if (isEdit && rows == 0) {
                        ps = db.createStatement("INSERT INTO Processor(coreCount, clockSpeed, Cache, productID) VALUES(?, ?, ?, ?)");
                        ps.setInt(1, Integer.parseInt(tfCoreCount.getText()));
                        ps.setFloat(2, Float.parseFloat(tfCpuClock.getText()));
                        ps.setInt(3, Integer.parseInt(tfCache.getText()));
                        ps.setInt(4, productID);
                        ps.executeUpdate();
                    }
                }
                if (cbType.getSelectionModel().getSelectedItem() == "RAM") {
                    queryAdd = "INSERT INTO RAM(Frequency, ddrType, Capacity, productID) VALUES(?, ?, ?, ?)";
                    if (isEdit) {
                        queryAdd = "UPDATE RAM SET Frequency = ?, ddrType = ?, Capacity = ?"
                                + " WHERE productID = ?";
                    }
                    PreparedStatement ps = db.createStatement(queryAdd);
                    ps.setFloat(1, (float) Double.parseDouble(tfFreq.getText()));
                    ps.setString(2, tfDDRType.getText());
                    ps.setString(3, tfCapacity.getText());
                    ps.setInt(4, productID);
                    int rows = ps.executeUpdate();
                    if (isEdit && rows == 0) {
                        ps = db.createStatement("INSERT INTO RAM(Frequency, ddrType, Capacity, productID) VALUES(?, ?, ?, ?)");
                        ps.setFloat(1, (float) Double.parseDouble(tfFreq.getText()));
                        ps.setString(2, tfDDRType.getText());
                        ps.setString(3, tfCapacity.getText());
                        ps.setInt(4, productID);
                        ps.executeUpdate();
                    }
                }
                if (cbType.getSelectionModel().getSelectedItem() == "PSU") {
                    queryAdd = "INSERT INTO PSU(contPower, ACInput, DCOutput, productID) VALUES(?, ?, ?, ?)";
                    if (isEdit) {
                        queryAdd = "UPDATE PSU SET contPower = ?, ACInput = ?, DCOutput = ?"
                                + " WHERE productID = ?";
                    }
                    PreparedStatement ps = db.createStatement(queryAdd);
                    ps.setInt(1, Integer.parseInt(tfContPower.getText()));
                    ps.setFloat(2, Float.parseFloat(tfACInput.getText()));
                    ps.setFloat(3, Float.parseFloat(tfDCoutput.getText()));
                    ps.setInt(4, productID);
                    int rows = ps.executeUpdate();
                    if (isEdit && rows == 0) {
                        ps = db.createStatement("INSERT INTO PSU(contPower, ACInput, DCOutput, productID) VALUES(?, ?, ?, ?)");
                        ps.setInt(1, Integer.parseInt(tfContPower.getText()));
                        ps.setFloat(2, Float.parseFloat(tfACInput.getText()));
                        ps.setFloat(3, Float.parseFloat(tfDCoutput.getText()));
                        ps.setInt(4, productID);
                        ps.executeUpdate();
                    }
                }
                if (cbType.getSelectionModel().getSelectedItem() == "GPU") {
                    queryAdd = "INSERT INTO GPU(clockSpeed, vRam, memClock, productID) VALUES(?, ?, ?, ?)";
                    if (isEdit) {
                        queryAdd = "UPDATE GPU SET clockSpeed = ?, vRam = ?, memClock = ?"
                                + " WHERE productID = ?";
                    }
                    PreparedStatement ps = db.createStatement(queryAdd);
                    ps.setFloat(1, Float.parseFloat(tfClockSpeed.getText()));
                    ps.setFloat(2, Float.parseFloat(tfVRAM.getText()));
                    ps.setInt(3, Integer.parseInt(tfMemoryClock.getText()));
                    ps.setInt(4, productID);
                    int rows = ps.executeUpdate();
                    if (isEdit && rows == 0) {
                        db.createStatement("INSERT INTO GPU(productID, clockSpeed, vRam, memClock) VALUES(?, ?, ?, ?)");
                        ps.setFloat(1, Float.parseFloat(tfClockSpeed.getText()));
                        ps.setFloat(2, Float.parseFloat(tfVRAM.getText()));
                        ps.setInt(3, Integer.parseInt(tfMemoryClock.getText()));
                        ps.setInt(4, productID);
                        ps.executeUpdate();
                    }
                }
            }
        } catch (SQLException ex) {
            new SceneLoader().showAlert(Alert.AlertType.ERROR, "Update failed!", "Operation failed!");
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            new SceneLoader().showAlert(Alert.AlertType.ERROR, "Update failed!", "Could not connect to Database!");
        }
        clearAllFields();
        ((Stage) tfDescription.getScene().getWindow()).close();
    }

    private void clearAllFields() {
        paneGPU.getChildren()
                .filtered(node -> node instanceof TextField)
                .forEach(node -> ((TextField) node).setText(""));
        paneMotherboard.getChildren()
                .filtered(node -> node instanceof TextField)
                .forEach(node -> ((TextField) node).setText(""));
        panePSU.getChildren()
                .filtered(node -> node instanceof TextField)
                .forEach(node -> ((TextField) node).setText(""));
        paneProcessor.getChildren()
                .filtered(node -> node instanceof TextField)
                .forEach(node -> ((TextField) node).setText(""));
        paneRAM.getChildren()
                .filtered(node -> node instanceof TextField)
                .forEach(node -> ((TextField) node).setText(""));
        anchorMain.getChildren()
                .filtered(node -> node instanceof TextField)
                .forEach(node -> ((TextField) node).setText(""));
        tfDescription.setText("");
    }

    private void setAllEditable(boolean val) {
        paneGPU.getChildren()
                .filtered(node -> node instanceof TextField)
                .forEach(node -> ((TextField) node).setEditable(val));
        paneMotherboard.getChildren()
                .filtered(node -> node instanceof TextField)
                .forEach(node -> ((TextField) node).setEditable(val));
        panePSU.getChildren()
                .filtered(node -> node instanceof TextField)
                .forEach(node -> ((TextField) node).setEditable(val));
        paneProcessor.getChildren()
                .filtered(node -> node instanceof TextField)
                .forEach(node -> ((TextField) node).setEditable(val));
        paneRAM.getChildren()
                .filtered(node -> node instanceof TextField)
                .forEach(node -> ((TextField) node).setEditable(val));
        anchorMain.getChildren()
                .filtered(node -> node instanceof TextField)
                .forEach(node -> ((TextField) node).setEditable(val));
        tfDescription.setEditable(false);
    }

    @FXML
    private void btnCancelOnClick(ActionEvent event) {
        ((Stage) tfDescription.getScene().getWindow()).close();
    }

}
