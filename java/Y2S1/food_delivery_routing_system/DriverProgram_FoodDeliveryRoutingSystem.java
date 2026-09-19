/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package food_delivery_routing_system;

/**
 *3
 * 
 * @author lcc0i
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
