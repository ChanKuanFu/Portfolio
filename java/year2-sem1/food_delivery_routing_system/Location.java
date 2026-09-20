package food_delivery_routing_system;

import java.util.Objects;
public abstract class Location {
    private String name;
    private String address;
    
    Location(String name,String address){
        this.name = name;
        this.address = address;
    }
    
    public String getName(){
        return name;
    }
    public String getAddress(){
        return address;
    }
    
    @Override
    public boolean equals(Object o){
        if(o == null){
            return false;
        }
        if(o instanceof Location){
            Location loc = (Location) o;
            if(this.name != null && this.name.equals(loc.name)){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
