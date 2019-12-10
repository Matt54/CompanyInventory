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
public class InHouse extends Part {
    
    private int machineId;
    
    InHouse(int _id, String _name, double _price, int _stock, int _min, int _max,int _machineId)
    {
        id = _id;
        name = _name;
        price = _price;
        stock = _stock;
        min = _min;
        max = _max;
        machineId = _machineId;
    }
    
    public void setMachineId(int _machineId){
        machineId = _machineId;
    }
    
    public int getMachineId(){
        return machineId;
    }
    
}
