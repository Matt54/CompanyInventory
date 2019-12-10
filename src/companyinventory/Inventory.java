/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package companyinventory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author matthewp
 */
public class Inventory {
    ObservableList<Part> allParts;
    ObservableList<Product> allProducts;
    
    Inventory(){
        allParts = FXCollections.observableArrayList();
        allProducts = FXCollections.observableArrayList();
    }
    
    public void addPart(Part part){
        allParts.add(part);
    }
    
    public void addProduct(Product product){
        allProducts.add(product);
    }
    
    public Part lookupPart(int partId){
        for(Part part : allParts){
            if(part.getId() == partId) return part;
        }
        return null;
    }
    
    public Product lookupProduct(int productId){
        for(Product product : allProducts){
            if(product.id == productId) return product;
        }
        return null;
    }
    
    public Part lookupPart(String partName){
        for(Part part : allParts){
            if(part.getName().equals(partName)) return part;
        }
        return null;
    }
    
    public Product lookupProduct(String productName){
        for(Product product : allProducts){
            if(product.getName().equals(productName)) return product;
        }
        return null;
    }
    
    public void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }
    
    public void updateProduct(int index, Product selectedProduct){
        allProducts.set(index, selectedProduct);
    }
    
    public boolean deletePart(Part selectedPart){
        if(allParts.contains(selectedPart)){
            allParts.remove(selectedPart);
            return true;
        }
        else{
            return false;
        }
    }
    
    public boolean deleteProduct(Product selectedProduct){
        if(allProducts.contains(selectedProduct)){
            allProducts.remove(selectedProduct);
            return true;
        }
        else{
            return false;
        }
    }
    
    public ObservableList<Part> getAllParts(){
        
        return allParts;
    }
    
    public ObservableList<Product> getAllProducts(){
        
        return allProducts;
    }
}
