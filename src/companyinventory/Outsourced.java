/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package companyinventory;

/**
 *
 * @author matthewp
 */
public class Outsourced extends Part {
    private String companyName;
    
    Outsourced(int _id, String _name, double _price, int _stock, int _min, int _max,String _companyName)
    {
        id = _id;
        name = _name;
        price = _price;
        stock = _stock;
        min = _min;
        max = _max;
        companyName = _companyName;
    }
    
    public void setCompanyName(String _companyName){
        companyName = _companyName;
    }
    
    public String getCompanyName(){
        return companyName;
    }
}
