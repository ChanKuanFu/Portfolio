/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package food_delivery_routing_system;

/**
 *
 * @author lcc0i
 */
import java.util.List;
import java.util.ArrayList;
public class OrderHistory {
    private List<Order> orders;
    
    // Constructors
    OrderHistory() {
        this.orders = new ArrayList<>();
    }
    
    public void addOrder(Order o){
        if(o!=null){
            orders.add(o);
        }
    }
    
    public List<Order> getAllOrders(){
        // retrieve data for all orders to MENU
        return orders;
    }
}
