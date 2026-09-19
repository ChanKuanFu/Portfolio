/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package food_delivery_routing_system;

/**
 *
 * @author lcc0i
 */
public class DeliveryHub extends Location{
    private String deliveryID;
    private String deliveryDay;
    private String deliveryMonth;
    private String deliveryYear;
    private double deliveryCost;
    
    DeliveryHub(String name, String address, String deliveryID, String deliveryDay, String deliveryMonth, String deliveryYear, double deliveryCost){
        super(name,address);
        this.deliveryID = deliveryID;
        this.deliveryDay = deliveryDay;
        this.deliveryMonth = deliveryMonth;
        this.deliveryYear = deliveryYear;
        this.deliveryCost = deliveryCost;
    }

    public String getDeliveryID() {
        return deliveryID;
    }
    public String getDeliveryDay() {
        return deliveryDay;
    }
    public String getDeliveryMonth() {
        return deliveryMonth;
    }
    public String getDeliveryYear() {
        return deliveryYear;
    }
    public double getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryID(String deliveryID) {
        this.deliveryID = deliveryID;
    }
    public void setDeliveryDay(String deliveryDay) {
        this.deliveryDay = deliveryDay;
    }
    public void setDeliveryMonth(String deliveryMonth) {
        this.deliveryMonth = deliveryMonth;
    }
    public void setDeliveryYear(String deliveryYear) {
        this.deliveryYear = deliveryYear;
    }
    public void setDeliveryCost(double deliveryCost) {
        this.deliveryCost = deliveryCost;
    }
    
    @Override
    public String toString() {
        return String.format("\n\n\t>=== Vertex: Delivery Hub Details ===<\n\tDelivery Hub name\t: %-30s\n\tAddress\t\t\t: %-40s\n\tDelivery ID\t\t: %-15s\n\tDelivery Date\t\t: %s/%s/%s\n\tDelivery cost\t\t: RM%-6.2f",super.getName(),super.getAddress(),getDeliveryID(),getDeliveryDay(),getDeliveryMonth(),getDeliveryYear(),getDeliveryCost());
    }
    
}
