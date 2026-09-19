/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package food_delivery_routing_system;

/**
 *
 * @author lcc0i
 */
public class Customer extends Location{
    private String custPhone;
    private int custID;
    private double payAmount;
    private static int currentCustID = 1;
    
    Customer(String name, String address, String custPhone){
        super(name,address);
        this.custPhone = custPhone;
        this.custID = currentCustID;
        
        currentCustID++;
    }

    public String getCustPhone() {
        return custPhone;
    }
    public int getCustID() {
        return custID;
    }
    public double getPayAmount() {
        return payAmount;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    public double calculatePayAmount(Supplier sp, DeliveryHub dh){
        return payAmount = sp.getProductPrice() + dh.getDeliveryCost();
    }

    @Override
    public String toString() {
        return String.format("\n\n\t>=== Vertex: Customer Details ===<\n\tCustomer name\t: %-30s\n\tCustomer ID\t: C%03d\n\tAddress\t\t: %-40s\n\tPhone\t\t: %-15s\n\tPay Amount\t: RM%-6.2f",super.getName(),getCustID(),super.getAddress(),getCustPhone(),getPayAmount());
    }
}
