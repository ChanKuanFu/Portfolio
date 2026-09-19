/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package food_delivery_routing_system;

/**
 *
 * @author lcc0i
 */
public class Intersection extends Location{
    // no any variable
    
    Intersection(String name, String address){
        super(name,address);
    } 
    
    @Override
    public String toString() {
        return String.format("\n\n\t>=== Vertex: Intersection Details ===<\n\tIntersection name\t: %-30s\n\tAddress\t\t\t: %-40s\n",super.getName(),super.getAddress());
    }
}
