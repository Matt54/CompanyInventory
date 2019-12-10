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
public class Product {
    
    ObservableList<Part> associatedParts;
    protected int id;
    protected String name;
    protected double price;
    protected int stock;
    protected int min;
    protected int max;
    
    Product(int _id, String _name, double _price, int _stock, int _min, int _max)
    {
        id = _id;
        name = _name;
        price = _price;
        stock = _stock;
        min = _min;
        max = _max;
        associatedParts = FXCollections.observableArrayList();
    }
    
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }
    
    public boolean deleteAssociatedPart(Part selectedAspart){
        if(associatedParts.contains(selectedAspart)){
            associatedParts.remove(selectedAspart);
            return true;
        }
        else{
            return false;
        }
    }
    
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }
    
    public void setID(int _id){
        id = _id;
    }
    
    public void setName(String _name){
        name = _name;
    }
    
    public void setPrice(double _price){
        price = _price;
    }
    
    public void setStock(int _stock){
        stock = _stock;
    }
    
    public void setMin(int _min){
        min = _min;
    }
    
    public void setMax(int _max){
        max = _max;
    }
    
    public int getId(){
        return id;
    }
    
    public String getName(){
        return name;
    }
    
    public double getPrice(){
        return price;
    }
    
    public int getStock(){
        return stock;
    }
    
    public int getMin(){
        return min;
    }
    
    public int getMax(){
        return max;
    }
}
