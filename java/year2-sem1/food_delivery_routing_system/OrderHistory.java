package food_delivery_routing_system;

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
