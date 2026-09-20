package food_delivery_routing_system;

import java.util.List;

public class Order {
    private int orderID;
    private static int currentID = 1001;
    private Location supplier;
    private Location customer;
    private List<Location> path;
    private String custName;
    private double totalPaid;
    
    Order(Location supplier,Location customer){
        this.orderID = currentID;
        this.supplier = supplier;
        this.customer = customer;
        this.path = null; // route result will get from computeRoute()
        
        currentID++;
    }

    public int getOrderID() {
        return orderID;
    }
    public static int getCurrentID() {
        return currentID;
    }
    public Location getSupplier() {
        return supplier;
    }
    public Location getCustomer() {
        return customer;
    }
    public List<Location> getPath() {
        return path;
    }
    public String getCustName() {
        return custName;
    }
    public double getTotalPaid() {
        return totalPaid;
    }
    

    public void setCustName(String custName) {
        this.custName = custName;
    }
    public void setTotalPaid(double totalPaid) {
        this.totalPaid = totalPaid;
    }
    
    // to link loc Supplier and Customer,
    // use RoutingGraph to compute shortest path for each order
    public boolean computeRoute(RoutingGraph graph){
        // call findShortestRoute for BFS algo
        List<Location> result = graph.findShortestRoute(this.supplier, this.customer);
        
        // if no result or no connection btw two loc vertex
        // then no compute
        if(result==null || result.isEmpty()){
            return false;
        }
        
        // store path if found
        path = result;
        return true;
    }
    
   
    // convert path list to string
    public String getPathToString(){
        if(path==null || path.isEmpty()){
            return "No found route";
        }
        
        StringBuilder sb = new StringBuilder();
        
        for(int i = 0; i<path.size(); i++){
            sb.append(path.get(i).getName());
            if(i<path.size() - 1){
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }
    
    @Override                              
    public String toString() {
        return String.format(">=== Order Details ===<"
                            +"\nOrder ID\t: %d"
                            +"\nOrdered by\t: %s"
                            +"\nSupplier\t: %s"
                            +"\nCustomer\t: %s"
                            +"\nRoute\t\t: %s"
                            +"\nTotal Paid\t: RM%.2f",
                            getOrderID(), getCustName(), supplier.getName(), customer.getName(), getPathToString(), getTotalPaid());
    }
}
