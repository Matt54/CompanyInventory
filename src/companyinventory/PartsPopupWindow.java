/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package companyinventory;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author matthewp
 */
public class PartsPopupWindow{
    PartsPopupWindow(Stage _primaryStage, Inventory _inventory)
    {
        CreateComponents(_primaryStage, _inventory);
        SwitchWindows();
        tfID.setText("Auto Gen - Disabled");
        rbInHouse.setSelected(true);
    }

    PartsPopupWindow(Stage _primaryStage, Inventory _inventory, Part part)
    {
        CreateComponents(_primaryStage, _inventory);
        SwitchWindows();
        LoadPart(part);
        if (part instanceof InHouse) {
            rbInHouse.setSelected(true);
            startHelp = true;
        }
        else{
            rbOutsourced.setSelected(true);
            startHelp = true;
        }
    }

    void CreateComponents(Stage _primaryStage, Inventory _inventory){
        primaryStage = _primaryStage;
        inventory = _inventory;

        rbInHouse.setToggleGroup(tgPopup2);
        rbOutsourced.setToggleGroup(tgPopup2);
        tfID.setDisable(true);
        btnSave.setText("Save");
        btnCancel.setText("Cancel");

        windowPopup.setScene(scenePopup);
        windowPopup.setTitle("Add Part");

        rbInHouse.selectedProperty().addListener(new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
            labelMachineID.setText("Machine ID");
                if(startHelp)tfMachineID.clear();
                
            }
        });
        rbOutsourced.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> obs, Boolean wasPreviouslySelected, Boolean isNowSelected) {
               labelMachineID.setText("Company Name");
                if(startHelp)tfMachineID.clear();
            }
        });

        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Boolean didSave = SavePart();
                if(didSave){
                    primaryStage.show();
                    windowPopup.close();
                }
            }
        });
        btnCancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.show();
                windowPopup.close();
            }
        });

        tfID.setPrefWidth(180);
        tfName.setPrefWidth(180);
        tfMin.setPrefWidth(80);
        tfMax.setPrefWidth(80);
        tfCost.setPrefWidth(180);
        tfInv.setPrefWidth(80);
        tfMachineID.setPrefWidth(180);
        scenePopup.getStylesheets().add("companyinventory/StyleSheet.css");
    }

    Boolean ValidateInputs(){
        try{

            Boolean emptyInput = false;
            if(tfID.getText().equals(""))emptyInput = true;
            if(tfName.getText().equals(""))emptyInput = true;
            if(tfCost.getText().equals(""))emptyInput = true;
            if(tfInv.getText().equals(""))emptyInput = true;
            if(tfMax.getText().equals(""))emptyInput = true;
            if(tfMin.getText().equals(""))emptyInput = true;
            if(tfMachineID.getText().equals(""))emptyInput = true;
            if(emptyInput){
                Alert alert = new Alert(Alert.AlertType.ERROR, "You can't have an empty input.");
                alert.showAndWait();
                return false;
            }

            Double.parseDouble(tfCost.getText());

            Boolean badStockValue = false;
            int stock = Integer.parseInt(tfInv.getText());
            int max = Integer.parseInt(tfMax.getText());
            int min = Integer.parseInt(tfMin.getText());
            if((stock < min) || stock > max){
                badStockValue = true;
            }
            if(badStockValue){
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inventory Stock for the part must be at least the minimum and no more than the maximum.");
                alert.showAndWait();
                return false;
            }

            if(rbInHouse.isSelected())
            {
                Integer.parseInt(tfMachineID.getText());
            }
            else{
                tfMachineID.getText();
            }
        }
        catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
            return false;
        }
        return true;
    }

    Boolean SavePart(){
        if(ValidateInputs()){
            int id;
            if(tfID.getText().equals("Auto Gen - Disabled")){
                id = inventory.getAllParts().size()+1;
            }
            else{
                id = Integer.parseInt(tfID.getText());
            }

            String name = tfName.getText();
            double price = Double.parseDouble(tfCost.getText());
            int stock = Integer.parseInt(tfInv.getText());
            int max = Integer.parseInt(tfMax.getText());
            int min = Integer.parseInt(tfMin.getText());
            if(rbInHouse.isSelected())
            {
                int machineID = Integer.parseInt(tfMachineID.getText());
                InHouse part = new InHouse(id,name,price,stock,min,max,machineID);
                if(isExistingPart){
                    inventory.updatePart(inventory.getAllParts().indexOf(inputPart), part);
                }
                else{
                    inventory.addPart(part);
                }
            }
            else{
                String companyName = tfMachineID.getText();
                Outsourced part = new Outsourced(id,name,price,stock,min,max,companyName);
                if(isExistingPart){
                    inventory.updatePart(id-1, part);
                }
                else{
                    inventory.addPart(part);
                }
            }
            return true;
        }
        return false;
    }

    void SwitchWindows(){
        windowPopup.show();
        primaryStage.close();
    }

    void LoadPart(Part part){
        isExistingPart=true;
        labelPopup.setText("Modify Part");
        windowPopup.setTitle("Modify Part");
        inputPart = part;

        tfID.setText(Integer.toString(part.getId()));
        tfName.setText(part.getName());
        tfInv.setText(Integer.toString(part.getStock()));
        tfCost.setText(Double.toString(part.getPrice()));
        tfMax.setText(Integer.toString(part.getMax()));
        tfMin.setText(Integer.toString(part.getMin()));
        if (part instanceof InHouse) {
            tfMachineID.setText(Integer.toString(((InHouse) part).getMachineId()));
            rbInHouse.setSelected(true);
        }
        else{
            tfMachineID.setText(((Outsourced) part).getCompanyName());
            rbOutsourced.setSelected(true);
        }
    }

    Stage primaryStage;
    Inventory inventory;
    Boolean isExistingPart = false;
    Boolean startHelp = false;
    Part inputPart;

    Label labelPopup = new Label("Add Part");
    RadioButton rbInHouse = new RadioButton("In-House");
    RadioButton rbOutsourced = new RadioButton("Outsourced");
    HBox hboxPopupTop = new HBox(labelPopup,rbInHouse,rbOutsourced);
    final ToggleGroup tgPopup2 = new ToggleGroup();

    Label labelID = new Label("ID");
    TextField tfID = new TextField();
    HBox hboxID = new HBox(labelID,tfID);

    Label labelName = new Label("Name");
    TextField tfName = new TextField();
    HBox hboxName = new HBox(labelName,tfName);

    Label labelInv = new Label("Inv");
    TextField tfInv = new TextField();
    HBox hboxInv = new HBox(labelInv,tfInv);

    Label labelCost = new Label("Price/Cost");
    TextField tfCost = new TextField();
    HBox hboxCost = new HBox(labelCost,tfCost);

    Label labelMax = new Label("Max");
    TextField tfMax = new TextField();
    Label labelMin = new Label("Min");
    TextField tfMin = new TextField();
    HBox hboxMaxMin = new HBox(labelMax,tfMax,labelMin,tfMin);

    Label labelMachineID = new Label("Machine ID");
    TextField tfMachineID = new TextField();
    HBox hboxMachineID = new HBox(labelMachineID,tfMachineID);

    Button btnSave = new Button();
    Button btnCancel = new Button();
    HBox hboxPopupBtns = new HBox(btnSave,btnCancel);

    VBox vboxPopup = new VBox(hboxPopupTop,hboxID,hboxName,hboxInv,hboxCost,hboxMaxMin,hboxMachineID,hboxPopupBtns);
    FlowPane rootPopup = new FlowPane(vboxPopup);
    Scene scenePopup = new Scene(rootPopup, 300, 300);
    Stage windowPopup = new Stage();
}
