/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package companyinventory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author matthewp
 */
public class ProductsPopupWindow {
    
    ProductsPopupWindow(Stage _primaryStage, Inventory _inventory)
    {
        CreateComponents(_primaryStage, _inventory);
        SwitchWindows();
        tfID.setText("Auto Gen - Disabled");
        tfInv.setText("0");
    }

    ProductsPopupWindow(Stage _primaryStage, Inventory _inventory, Product product)
    {
        isExistingProduct = true;
        myProduct = product;
        CreateComponents(_primaryStage, _inventory);
        SwitchWindows();
        LoadProduct(product);
    }
    
    void LoadProduct(Product product)
    {
        labelPopup.setText("Modify Product");
        windowPopup.setTitle("Modify Product");
        tfID.setText(Integer.toString(product.getId()));
        tfName.setText(product.getName());
        tfInv.setText(Integer.toString(product.getStock()));
        tfCost.setText(Double.toString(product.getPrice()));
        tfMax.setText(Integer.toString(product.getMax()));
        tfMin.setText(Integer.toString(product.getMin()));
    }
    
    void SwitchWindows(){
        windowPopup.show();
        primaryStage.close();
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
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inventory Stock for the product must be at least the minimum and no more than the maximum.");
                alert.showAndWait();
                return false;
            }
        }
        catch(Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
            return false;
        }
        return true;
    }    
    
    Boolean SaveProduct(){
        if(ValidateInputs())
        {
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
            
            
            Product newProduct = new Product(id,name,price,stock,min,max);
            for (Part prodPart: associatedParts) {
                newProduct.addAssociatedPart(prodPart);
            }
            
            if(isExistingProduct)
            {
                inventory.updateProduct(inventory.getAllProducts().indexOf(myProduct),newProduct);
            }
            else{
                inventory.addProduct(newProduct);
            }
            
            return true;
        }
        return false;
    }

    void CreateComponents(Stage _primaryStage, Inventory _inventory){
        primaryStage = _primaryStage;
        inventory = _inventory;
        associatedParts = FXCollections.observableArrayList();
        unassociatedParts = FXCollections.observableArrayList();
        if(isExistingProduct){
            associatedParts = myProduct.getAllAssociatedParts();
            for (Part invPart: inventory.getAllParts()) {
                Boolean found = false;
                for (Part prodPart: associatedParts) {
                    if(invPart.getId() == prodPart.getId()) found = true;
                }
                if(!found) unassociatedParts.add(invPart);
            }
        }
        else{
            for (Part invPart: inventory.getAllParts()) {
                unassociatedParts.add(invPart);
            }
        }

        tfID.setDisable(true);
        btnSave.setText("Save");
        btnCancel.setText("Cancel");

        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Boolean didSave = SaveProduct();
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
        
        tvInventory = new TableView<Part>(unassociatedParts);

        TableColumn<Part, String> id = new TableColumn<>("Part ID");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tvInventory.getColumns().add(id);

        TableColumn<Part, String> name = new TableColumn<>("Part Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        tvInventory.getColumns().add(name);

        TableColumn<Part, String> stock = new TableColumn<>("Inventory Level");
        stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tvInventory.getColumns().add(stock);

        TableColumn<Part, String> price = new TableColumn<>("Price/Cost per Unit");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        tvInventory.getColumns().add(price);

        tvInventory.setPrefWidth(355);
        tvInventory.setPrefHeight(100);
        
        tvSelInventoryPart = tvInventory.getSelectionModel();

        btnSearchParts.setText("Search");
        btnSearchParts.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //Search for a part
                String searchTerm = tfSearchParts.getText();
                Part foundPart = inventory.lookupPart(searchTerm);
                if(foundPart != null)
                {
                    tvInventory.getSelectionModel().select(foundPart);
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Part Name not found");
                    alert.showAndWait();
                }
            }
        });

        btnAddPart.setText("Add");
        btnAddPart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(tvSelInventoryPart.getSelectedItem()!=null){
                    associatedParts.add(tvSelInventoryPart.getSelectedItem());
                    unassociatedParts.remove(tvSelInventoryPart.getSelectedItem());
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR, "No Part Selected");
                    alert.showAndWait();
                }
            }
        });
        
        tvIncluded = new TableView<Part>(associatedParts);

        TableColumn<Part, String> idIncluded = new TableColumn<>("Part ID");
        idIncluded.setCellValueFactory(new PropertyValueFactory<>("id"));
        tvIncluded.getColumns().add(idIncluded);

        TableColumn<Part, String> nameIncluded = new TableColumn<>("Part Name");
        nameIncluded.setCellValueFactory(new PropertyValueFactory<>("name"));
        tvIncluded.getColumns().add(nameIncluded);

        TableColumn<Part, String> stockIncluded = new TableColumn<>("Inventory Level");
        stockIncluded.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tvIncluded.getColumns().add(stockIncluded);

        TableColumn<Part, String> priceIncluded = new TableColumn<>("Price/Cost per Unit");
        priceIncluded.setCellValueFactory(new PropertyValueFactory<>("price"));
        tvIncluded.getColumns().add(priceIncluded);

        tvIncluded.setPrefWidth(355);
        tvIncluded.setPrefHeight(100);
        
        tvSelIncludedPart = tvIncluded.getSelectionModel();
        
        btnDeletePart.setText("Delete");
        btnDeletePart.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(tvSelIncludedPart.getSelectedItem()!=null){
                    unassociatedParts.add(tvSelIncludedPart.getSelectedItem());
                    associatedParts.remove(tvSelIncludedPart.getSelectedItem());
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR, "No Part Selected");
                    alert.showAndWait();
                }
            }
        });
        
        vboxRight = new VBox(hboxInventoryTop,tvInventory,btnAddPart,tvIncluded,btnDeletePart);
        
        HBox hboxCombinedView = new HBox(vboxLeft,vboxRight);
    
        rootPopup = new FlowPane(hboxCombinedView);
        scenePopup = new Scene(rootPopup, 620, 320);
        windowPopup = new Stage();
        windowPopup.setScene(scenePopup);
        windowPopup.setTitle("Add Product");
        
        tfID.setPrefWidth(180);
        tfName.setPrefWidth(180);
        tfMin.setPrefWidth(80);
        tfMax.setPrefWidth(80);
        tfCost.setPrefWidth(180);
        tfInv.setPrefWidth(80);
        scenePopup.getStylesheets().add("companyinventory/StyleSheet.css");
    }
    
    Stage primaryStage;
    Inventory inventory;
    Boolean isExistingProduct=false;
    Product myProduct;
    ObservableList<Part> associatedParts;
    ObservableList<Part> unassociatedParts;
    
    Label labelPopup = new Label("Add Product");
    HBox hboxPopupTop = new HBox(labelPopup);
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

    Button btnSave = new Button();
    Button btnCancel = new Button();
    HBox hboxPopupBtns = new HBox(btnSave,btnCancel);

    VBox vboxLeft = new VBox(hboxPopupTop,hboxID,hboxName,hboxInv,hboxCost,hboxMaxMin,hboxPopupBtns);

    TableView<Part> tvInventory;
    TableView.TableViewSelectionModel<Part> tvSelInventoryPart;
    
    TextField tfSearchParts = new TextField();
    Button btnSearchParts = new Button();
    HBox hboxInventoryTop = new HBox(btnSearchParts,tfSearchParts);
    
    Button btnAddPart = new Button();
    
    TableView<Part> tvIncluded;
    TableView.TableViewSelectionModel<Part> tvSelIncludedPart;
    
    Button btnDeletePart = new Button();

    VBox vboxRight;
    
    FlowPane rootPopup;
    Scene scenePopup;
    Stage windowPopup;
}
