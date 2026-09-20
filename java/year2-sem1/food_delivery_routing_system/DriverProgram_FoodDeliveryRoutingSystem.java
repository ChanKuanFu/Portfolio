package food_delivery_routing_system;

/**
 *3
 * 
 */

import java.util.ArrayList;
import java.util.List;
public class DriverProgram_FoodDeliveryRoutingSystem {
    public static void main(String[] args) {
        RoutingGraph graph = new RoutingGraph();
        OrderHistory orderHistory = new OrderHistory();
        List<Customer> customerList = new ArrayList<>();

        FoodDeliveryRoutingSystem system = new FoodDeliveryRoutingSystem(graph, orderHistory, customerList);
        system.showMainMenu();
    }
}
