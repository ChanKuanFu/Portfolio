/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package food_delivery_routing_system;

/**
 *
 * @author lcc0i
 */
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;

public class Admin {
    private String staffID;
    private static int currentID = 1001;
    private String staffName;
    private String staffPassword;
    
    private Scanner scAdmin; // scanner input
    private StringBuilder dottedLine = new StringBuilder("-".repeat(60));
    private StringBuilder solidLine = new StringBuilder("_".repeat(60));
    private StringBuilder starLine = new StringBuilder("*".repeat(60));
    
    
    Admin(String staffName, String staffPassword, Scanner sc){
        this.staffID = "Adm" + String.format("%d",currentID);
        this.staffName = staffName;
        this.staffPassword = staffPassword;
        currentID++;
        
        // use scanner for main program
        this.scAdmin = sc;
    }

    public String getStaffName() {
        return staffName;
    }
    public String getStaffID(){
        return staffID;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }
   
    public boolean validPassword(String inputPassword){
        if(inputPassword==null){
            return false;
        }
        // regex check password
        Pattern p = Pattern.compile("[a-zA-Z0-9]{6,8}");
        Matcher m = p.matcher(inputPassword);
        if(!m.matches()){
            return false;
        }
        return true;
    }
    
    public boolean staffLogin(){
        StringBuffer loginSuccessMsg = new StringBuffer(String.format("LOGIN SUCCESSFUL : Welcome %s",getStaffName()));
        StringBuffer loginFailedMsg = new StringBuffer("LOGIN ERROR : Wrong password !!!");
        StringBuffer loginInvalidFormat = new StringBuffer("LOGIN ERROR : Must between 6 - 8 characters!!!");
        
        int spaceLoginSuccessMsg = (50-loginSuccessMsg.length())/2;
        int spaceLoginFailedMsg = (50-loginFailedMsg.length())/2;
        int spaceLoginInvalidFormat = (50-loginInvalidFormat.length())/2;
        
        String inputPassword;
        
        System.out.println("\n\t"+starLine);
        System.out.println("\t\t\t>=== Welcome to login page ===<");
        System.out.print("\n\t\t\tPassword : ");
        inputPassword = scAdmin.nextLine();
        
        if(!validPassword(inputPassword)){
            System.out.printf("\n\t%s\n\t%s%s%s%s%s\n\t%s\n\n",
                    starLine,
                    "*".repeat(5),
                    " ".repeat(spaceLoginInvalidFormat),
                    loginInvalidFormat,
                    " ".repeat(spaceLoginInvalidFormat),
                    "*".repeat(5),
                    starLine);
            return false;
        }
        
        if(!inputPassword.equals(staffPassword)){
            System.out.printf("\n\t%s\n\t%s%s%s%s%s\n\t%s\n\n",
                    starLine,
                    "*".repeat(5),
                    " ".repeat(spaceLoginFailedMsg),
                    loginFailedMsg,
                    " ".repeat(spaceLoginFailedMsg),
                    "*".repeat(5),
                    starLine);
            return false;
        }
        
        System.out.printf("\n\t%s\n\t%s%s%s%s%s\n\t%s\n\n",
                    starLine,
                    "*".repeat(5),
                    " ".repeat(spaceLoginSuccessMsg),
                    loginSuccessMsg,
                    " ".repeat(spaceLoginSuccessMsg),
                    "*".repeat(5),
                    starLine);
        return true;
    }
    
    public void resetPassword(){
        String loginSuccessMsg = "\t>=== PASSWORD CHANGED SUCCESSFUL ===<";
        String newPassword;
        String confirmPassword;
        
        
        System.out.println("\t>=== Welcome to Reset Password page ===<\n"
                +"\t** (Press '0' back to main menu) **\n");
        System.out.println("\tEnter a new password below to change your password.");
        System.out.println("\tYour password must contain:\n\t  (/) In about 6 - 8 characters in length\n");
        
        
        while(true){
            System.out.print("\tNew Password : ");
            newPassword = scAdmin.nextLine();
            if ("0".equals(newPassword)) {
                System.out.println("\n\tBack to Main Menu...\n");
                return; // jump back to Main Menu
            }
            if(!validPassword(newPassword)){
                System.out.println("\n\tPlease re-enter the 'New Password'...\n");
                continue;
            }
            
            System.out.print("\tConfirm Password : ");
            confirmPassword = scAdmin.nextLine();
            if ("0".equals(confirmPassword)) {
                System.out.println("\n\tBack to Main Menu...\n");
                return; // jump back to Main Menu
            }
            if(!validPassword(confirmPassword)){
                System.out.println("\n\tPlease re-enter the 'Confirm Password'...\n");
                continue;
            }
            
            if(!newPassword.equals(confirmPassword)){
                System.out.println("\nPASSWORD RESET ERROR : Both password must be same! Please try again.\n");
                continue;
            }
            
            // update new password to staffPassword
            this.staffPassword = newPassword;
            System.out.println("\n\t" + "*".repeat(56)+"\n\t\t"
                                +loginSuccessMsg
                                +"\t\n\t"+"*".repeat(56) + "\n");
            break;  // to stop the loop
        }
    }
    
    // list out orderHistory
    public void viewOrderHistory(OrderHistory history){
        System.out.println(solidLine);
        System.out.println("|#| >  Main Menu (Admin)  >  View Order History");
        System.out.println(starLine);
        
        StringBuffer orderNotFoundMsg = new StringBuffer("No order history found in the system.");
        int spaceOrderNotFoundMsg = (56-orderNotFoundMsg.length())/2;
        
        System.out.println("\n\t>=== System Order History ===<");
        
       
        
        
        if(history==null || history.getAllOrders()==null || history.getAllOrders().isEmpty()) {
            System.out.printf("\n\t%s\n\t%s%s%s%s%s\n\t%s\n\n",
                    starLine,
                    "*".repeat(2),
                    " ".repeat(spaceOrderNotFoundMsg),
                    orderNotFoundMsg,
                    " ".repeat(spaceOrderNotFoundMsg),
                    "*".repeat(2),
                    starLine);
            return; // jump back to Main Menu
        }
        
        List<Order> orderList = history.getAllOrders();
        for(Order ord:orderList){
            System.out.println(ord.toString());
            System.out.println(dottedLine);  
        }
        System.out.println("Total number of order(s): " + orderList.size());
        System.out.println(solidLine);
    }
    
    // main menu (Admin) - manage user account
    public void manageCustomerAccount(List<Customer> customerList, String custName){
        System.out.println(">=== Manage Customer Account ===<");
        
        if(customerList==null || customerList.isEmpty()){
            System.out.println("*".repeat(56)+"\n"+"*".repeat(15) 
                    +"No customer account record"
                    +"*".repeat(15)+"\n"+"*".repeat(56)+"\n\n");
            return; // jump back to Main Menu
        }
        
        // search customer name
        Customer targetCustomer = null;
        for (Customer c : customerList) {
            if (c.getName().equalsIgnoreCase(custName)) {
                targetCustomer = c;
                break;
            }
        }
        
        // if customer name found
        if (targetCustomer != null) {
            System.out.printf("Customer Account Found: %-20s", targetCustomer.toString());
            System.out.printf("\n\tCurrent Payable Amount\t: RM %-6.2f\n",targetCustomer.getPayAmount());
        } else {
            System.out.printf("\nCustomer '%s' was not found!!!\n",custName);
        }
    }
    
    @Override
    public String toString() {
        return String.format(">=== Admin Details ===<\nName: %-30s\nID: %-20s",getStaffName(),getStaffID());
    }

}



