/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package food_delivery_routing_system;

/**
 *
 * @author lcc0i
 */
public class Supplier extends Location{
    private String supplierPhone;
    private String productName;
    private String productID;
    private double productPrice;
    
    Supplier(String name, String address, String supplierPhone, String productName, String productID, double productPrice){
        super(name,address);
        this.supplierPhone = supplierPhone;
        this.productName = productName;
        this.productID = productID;
        this.productPrice = productPrice;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }
    public String getProductName() {
        return productName;
    }
    public String getProductID() {
        return productID;
    }
    public double getProductPrice() {
        return productPrice;
    }

    public void setSupplierPhone(String supplierPhone) {
        this.supplierPhone = supplierPhone;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public void setProductID(String productID) {
        this.productID = productID;
    }
    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    @Override
    public String toString() {
        return String.format("\n\n\t>===  Vertex: Supplier Details ===<\n\tName\t\t: %-30s\n\tPhone\t\t: %-15s\n\tAddress\t\t: %-40s\n\tProduct name\t: %-30s\n\tProduct ID\t: %-10s\n\tProduct Price\t: RM%-6.2f\n",super.getName(),getSupplierPhone(),super.getAddress(),getProductName(),getProductID(),getProductPrice());
    }
}
