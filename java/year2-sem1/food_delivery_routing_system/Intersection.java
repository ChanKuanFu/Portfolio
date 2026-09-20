package food_delivery_routing_system;

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
