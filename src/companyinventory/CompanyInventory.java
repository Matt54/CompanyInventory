/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package companyinventory;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author matthewp
 */
public class CompanyInventory extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        FlowPane root = new FlowPane();
        Scene sceneMain = new Scene(root, 875, 510);
        
        Inventory inventory = new Inventory();
        CreateInitialInventory(inventory);
        
        InventoryPartsView inventoryPartsView = new InventoryPartsView(primaryStage,inventory);
        InventoryProductsView inventoryProductsView = new InventoryProductsView(primaryStage,inventory);

        Label mainScreenLabel = new Label("Matt Pfeiffer - Inventory Management System");
        mainScreenLabel.setId("main-screen-label");
        
        HBox inventoryViews = new HBox(inventoryPartsView.CombinedView,inventoryProductsView.CombinedView);
        
        Button btnExit = new Button();
        btnExit.setText("Exit The Program");
        btnExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                primaryStage.close();
            }
        });
        btnExit.setId("exit-button");
        
        BorderPane topPane = new BorderPane();
        topPane.setLeft(mainScreenLabel);
        topPane.setRight(btnExit);
        
        VBox mainScreen = new VBox(topPane,inventoryViews);
        
        root.getChildren().addAll(mainScreen);
        
        primaryStage.setTitle("Company Inventory");
        primaryStage.setScene(sceneMain);
        primaryStage.show();
        sceneMain.getStylesheets().add("companyinventory/StyleSheet.css");
    }
    
    public void CreateInitialInventory(Inventory inventory)
    {
        Product product1 = new Product(1,"Product 1",50.0,1,1,1);
        Product product2 = new Product(2,"Product 2",100.0,1,1,2);
        Product product3 = new Product(3,"Product 3",150.0,1,1,3);
        
        Outsourced part1 = new Outsourced(1,"Part 1",5.0,1,1,1,"Matt's Company");
        InHouse part2 = new InHouse(2,"Part 2",10.0,1,1,1,2);
        InHouse part3 = new InHouse(3,"Part 3",15.0,1,1,1,3);
        
        product1.addAssociatedPart(part1);
        product1.addAssociatedPart(part2);
        product2.addAssociatedPart(part2);
        product2.addAssociatedPart(part3);
        product3.addAssociatedPart(part1);
        product3.addAssociatedPart(part3);
        
        inventory.addProduct(product1);
        inventory.addProduct(product2);
        inventory.addProduct(product3);
        inventory.addPart(part1);
        inventory.addPart(part2);
        inventory.addPart(part3);
    }
    
    public class InventoryPartsView
    {
        InventoryPartsView(Stage _primaryStage, Inventory _inventory)
        {
            primaryStage = _primaryStage;
            inventory = _inventory;
            tvParts = new TableView<Part>(inventory.getAllParts());
            
            TableColumn<Part, String> id = new TableColumn<>("Part ID");
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            tvParts.getColumns().add(id);

            TableColumn<Part, String> name = new TableColumn<>("Part Name");
            name.setCellValueFactory(new PropertyValueFactory<>("name"));
            tvParts.getColumns().add(name);

            TableColumn<Part, String> stock = new TableColumn<>("Inventory Level");
            stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
            tvParts.getColumns().add(stock);

            TableColumn<Part, String> price = new TableColumn<>("Price/Cost per Unit");
            price.setCellValueFactory(new PropertyValueFactory<>("price"));
            tvParts.getColumns().add(price);

            tvParts.setPrefWidth(355);
            tvParts.setPrefHeight(300);


            btnSearchParts.setText("Search");
            btnSearchParts.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    //Search for a part
                    String searchTerm = tfSearchParts.getText();
                    Part foundPart = inventory.lookupPart(searchTerm);
                    if(foundPart != null)
                    {
                        tvParts.getSelectionModel().select(foundPart);
                    }
                    else{
                        Alert alert = new Alert(AlertType.ERROR, "Part Name not found");
                        alert.showAndWait();
                    }
                }
            });

            TableView.TableViewSelectionModel<Part> tvSelPart = tvParts.getSelectionModel();

            btnAddPart.setText("Add");
            btnAddPart.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    PartsPopupWindow partsPopupWindow = new PartsPopupWindow(primaryStage,inventory);
                }
            });
        
            btnModifyPart.setText("Modify");
            btnModifyPart.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if(tvSelPart.getSelectedItem()!=null){
                        PartsPopupWindow partsPopupWindow = new PartsPopupWindow(primaryStage,inventory,tvSelPart.getSelectedItem());
                    }
                    else{
                        Alert alert = new Alert(AlertType.ERROR, "No Part Selected");
                        alert.showAndWait();
                    }
                }
            });

            btnDeletePart.setText("Delete");
            btnDeletePart.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if(tvSelPart.getSelectedItem()!= null){
                        inventory.deletePart(tvSelPart.getSelectedItem());
                    }
                    else{
                        Alert alert = new Alert(AlertType.ERROR, "No Part Selected");
                        alert.showAndWait();
                    }
                }
            });
            
            hboxTop = new HBox(labelParts,btnSearchParts,tfSearchParts);
            hboxBottom = new HBox(btnAddPart,btnModifyPart,btnDeletePart);
            CombinedView = new VBox(hboxTop,tvParts,hboxBottom);
            CombinedView.setId("inventory-view");
            labelParts.setId("secondary-label");
        }
        TableView<Part> tvParts;
        Label labelParts = new Label("Parts");
        
        TextField tfSearchParts = new TextField();
        Button btnSearchParts = new Button();
        Button btnModifyPart = new Button();
        Button btnDeletePart = new Button();
        Button btnAddPart = new Button();
        
        Stage primaryStage;
        Inventory inventory;
        
        HBox hboxTop;
        HBox hboxBottom;
        public VBox CombinedView;
    }
    
    public class InventoryProductsView
    {
        InventoryProductsView(Stage _primaryStage, Inventory _inventory)
        {
            primaryStage = _primaryStage;
            inventory = _inventory;
            tvProducts = new TableView<Product>(inventory.getAllProducts());
            
            TableColumn<Product, String> id = new TableColumn<>("Product ID");
            id.setCellValueFactory(new PropertyValueFactory<>("id"));
            tvProducts.getColumns().add(id);

            TableColumn<Product, String> name = new TableColumn<>("Product Name");
            name.setCellValueFactory(new PropertyValueFactory<>("name"));
            tvProducts.getColumns().add(name);

            TableColumn<Product, String> stock = new TableColumn<>("Inventory Level");
            stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
            tvProducts.getColumns().add(stock);

            TableColumn<Product, String> price = new TableColumn<>("Price/Cost per Unit");
            price.setCellValueFactory(new PropertyValueFactory<>("price"));
            tvProducts.getColumns().add(price);

            tvProducts.setPrefWidth(395);
            tvProducts.setPrefHeight(300);

            btnSearchProducts.setText("Search");
            btnSearchProducts.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String searchTerm = tfSearchProducts.getText();
                    Product foundProduct = inventory.lookupProduct(searchTerm);
                    if(foundProduct != null)
                    {
                        tvProducts.getSelectionModel().select(foundProduct);
                    }
                    else{
                        Alert alert = new Alert(AlertType.ERROR, "Product Name not found");
                        alert.showAndWait();
                    }
                }
            });

            TableView.TableViewSelectionModel<Product> tvSelProduct = tvProducts.getSelectionModel();

            btnAddProduct.setText("Add");
            btnAddProduct.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    ProductsPopupWindow ProductsPopupWindow = new ProductsPopupWindow(primaryStage,inventory);
                }
            });
        
            btnModifyProduct.setText("Modify");
            btnModifyProduct.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if(tvSelProduct.getSelectedItem()!= null){
                        ProductsPopupWindow ProductsPopupWindow = new ProductsPopupWindow(primaryStage,inventory,tvSelProduct.getSelectedItem());
                    }
                    else{
                        Alert alert = new Alert(AlertType.ERROR, "No Product Selected");
                        alert.showAndWait();
                    }
                }
            });

            btnDeleteProduct.setText("Delete");
            btnDeleteProduct.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if(tvSelProduct.getSelectedItem()!= null){
                        inventory.deleteProduct(tvSelProduct.getSelectedItem());
                    }
                    else{
                        Alert alert = new Alert(AlertType.ERROR, "No Product Selected");
                        alert.showAndWait();
                    }
                }
            });
            
            hboxTop = new HBox(labelProducts,btnSearchProducts,tfSearchProducts);
            hboxBottom = new HBox(btnAddProduct,btnModifyProduct,btnDeleteProduct);
            CombinedView = new VBox(hboxTop,tvProducts,hboxBottom);
            CombinedView.setId("inventory-view");
            labelProducts.setId("secondary-label");
        }
        TableView<Product> tvProducts;
        Label labelProducts= new Label("Products");
        TextField tfSearchProducts = new TextField();
        Button btnSearchProducts = new Button();
        Button btnModifyProduct = new Button();
        Button btnDeleteProduct = new Button();
        Button btnAddProduct = new Button();
        
        Stage primaryStage;
        Inventory inventory;
        
        HBox hboxTop;
        HBox hboxBottom;
        public VBox CombinedView;
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
