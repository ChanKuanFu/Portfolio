package food_delivery_routing_system;
/**
 *
 * @author lcc0i
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Scanner;

public class FoodDeliveryRoutingSystem {
    private RoutingGraph graph;
    private OrderHistory orderHistory;
    private List<Customer> customerList;
    
    // vertices list -- array
    private final String[] V = {
        "Supplier", "Intersection A", "Intersection B", "Intersection C",
        "Intersection D", "Intersection E", "Delivery Hub A", "Delivery Hub B",
        "Customer A", "Customer B"
    };
    
    // edges list -- 2d array
    private final String[][] E = {
        {"Supplier", "Intersection A"},
        {"Supplier", "Intersection C"},
        {"Intersection A", "Delivery Hub A"},
        {"Intersection A", "Delivery Hub B"},
        {"Intersection C", "Delivery Hub A"},
        {"Intersection C", "Delivery Hub B"},
        {"Delivery Hub A", "Intersection B"},
        {"Delivery Hub A", "Intersection E"},
        {"Delivery Hub B", "Intersection D"},
        {"Delivery Hub B", "Intersection E"},
        {"Intersection B", "Customer A"},
        {"Intersection B", "Intersection D"},
        {"Intersection E", "Customer A"},
        {"Intersection E", "Customer B"},
        {"Customer A", "Intersection D"},
        {"Customer B", "Intersection D"}
    };
    
    
    public Scanner sc = new Scanner(System.in);
    
    // element for cli_UI
    private StringBuilder dottedLine = new StringBuilder("-".repeat(100));
    private StringBuilder solidLine = new StringBuilder("_".repeat(100));
    private StringBuilder starLine = new StringBuilder("*".repeat(100));
    private StringBuilder logo = new StringBuilder("*".repeat(100)+"\n"+"*".repeat(100)
                + "\n\t ________ \t\t   _____   \t\t   _____   \t\t _______       "
                + "\n\t|@@@@@@@@|\t\t |@@@@@@@| \t\t |@@@@@@@| \t\t|@@@@@@@|      "
                + "\n\t|@@|      \t\t|@@@| |@@@|\t\t|@@@| |@@@|\t\t|@@|  |@@|     "
                + "\n\t|@@|_____ \t\t|@@|   |@@|\t\t|@@|   |@@|\t\t|@@|  |@@@|    "
                + "\n\t|@@@@@@@@|\t\t|@@|   |@@|\t\t|@@|   |@@|\t\t|@@|   |@@|    "
                + "\n\t|@@|      \t\t|@@|   |@@|\t\t|@@|   |@@|\t\t|@@|   |@@|    "
                + "\n\t|@@|      \t\t|@@|   |@@|\t\t|@@|   |@@|\t\t|@@|  |@@@|    "
                + "\n\t|@@|      \t\t|@@@| |@@@|\t\t|@@@| |@@@|\t\t|@@|__|@@|     "
                + "\n\t|@@|      \t\t |@@@@@@@| \t\t |@@@@@@@| \t\t|@@@@@@@|      "
                +"\n\n"+"*".repeat(100)+"\n"+"*".repeat(100)+"\n"
        );
    private StringBuilder invalidOptionMsg = new StringBuilder("\n\t"+"*".repeat(70)
                                                              +"\n\t\t\tInvalid selection, please try again."
                                                              +"\n\t"+"*".repeat(70)+"\n\n\n\n\n");
    // admin accounts initialize
    // pass sc object into admin constructor
    private Admin admin = new Admin("Lee", "abc123", sc);
                                                            
    // if null then Guest Mode
    //else if not null then Admin Mode
    private Admin currentAdmin;
    
    FoodDeliveryRoutingSystem(RoutingGraph graph, OrderHistory orderHistory, List<Customer> customer){
        this.graph = graph;
        this.orderHistory = orderHistory;
        this.customerList = customer;
        this.currentAdmin = null; // initialize with guest mode
        initializeGraphNetwork(); // initial routing graph
    }
    
    // convert V and E to Location and Route, become a graph
    private void initializeGraphNetwork() {

        // Supplier location info
        graph.addLocation(new Supplier("Supplier", "Setapak Central", "012-987 6543", "Apple", "P01", 6.99));

        // 4 Intersection locations info
        graph.addLocation(new Intersection("Intersection A", "Kuala Lumpur"));
        graph.addLocation(new Intersection("Intersection B", "Shah Alam"));
        graph.addLocation(new Intersection("Intersection C", "Danau Kota"));
        graph.addLocation(new Intersection("Intersection D", "Kota Damansara"));
        graph.addLocation(new Intersection("Intersection E", "Ampang Park"));

        // 2 Delivery Hub locations info
        graph.addLocation(new DeliveryHub("Delivery Hub A", "Ampang Delivery Hub", "DHAP", "6", "June", "2026", 13.99));
        graph.addLocation(new DeliveryHub("Delivery Hub B", "KL Delivery Hub", "DHKL", "5", "May", "2026", 26.99));

        // 2 Customer locations info
        graph.addLocation(new Customer("Customer A", "Taman Melawati", "011-1010 1011"));
        graph.addLocation(new Customer("Customer B", "Gombak", "012-1213 1415"));


        // for-each loop to add on all E
        for (String[] edge : E) {
            Location a = graph.getLocationByName(edge[0]);
            Location b = graph.getLocationByName(edge[1]);

            graph.addRoute(a, b);
        }
    }
    
    public void showMainMenu(){
        
        while(true){
            System.out.println(solidLine);
            System.out.println(logo);
            System.out.println(solidLine);
            System.out.println(dottedLine
                    +"\nWelcome to Food Delivery Routing System\n"
                    +dottedLine);
            if(currentAdmin == null){
                System.out.printf("%100s\n","< Guest Mode >\n\n");
            } else{
                System.out.printf("%100s\n","< Admin ID: " + currentAdmin.getStaffID() + " >\n\n");
            }
            
            System.out.printf("%25s%50s%25s\n"," ".repeat(25),"*".repeat(50)," ".repeat(25));
            System.out.printf("%43s%14s%43s\n"," ".repeat(43),"Main Menu Page"," ".repeat(43));
            System.out.printf("%25s%50s%25s\n"," ".repeat(25),"*".repeat(50)," ".repeat(25));
            
            System.out.print(
                    "\n"+dottedLine
                    +"\n"+"|#| >  Main Menu : Enter ");
            if(currentAdmin == null){
                // Guest mode
                System.out.print("'0' to '5'"); 
            } else {
                // Admin mode
                System.out.print("'0' to '8'"); 
            }
            System.out.print(" for computation option.\n\n");
            
            System.out.print("\t1\t-\tCreate Routing Graph\n"
                            +"\t2\t-\tSearch location\n"
                            +"\t3\t-\tView the FOOD delivery network\n"
                            +"\t4\t-\tPlace an order & compute route\n"
            );
            if(currentAdmin == null){
                // Guest mode
                System.out.print("\t5\t-\tAdmin Login\n");
            } else {
                // Admin mode
                System.out.print(  "\t5\t-\tView Order History\n"
                                  +"\t6\t-\tManage User Account\n"
                                  +"\t7\t-\tReset Password\n"
                                  +"\t8\t-\tLogout\n"
                );
            }
            System.out.println("\t0\t-\tExit\n");
            System.out.println(dottedLine);
            System.out.print("\n  Selection: ");
            String option;
            option = sc.nextLine();
            
            switch (option) {
                case "1":{optionCreateGraph(); break;}
                case "2":{optionSearchGraph(); break;}
                case "3":{optionViewRoutingGraph(); break;}
                case "4":{optionOrderComputeRoute(); break;}
                case "5":{
                    if(currentAdmin == null){
                        optionAdminLogin();
                    }else{
                        currentAdmin.viewOrderHistory(orderHistory);
                    }
                    break;
                }
                case "6":{
                    if(currentAdmin != null) {
                        optionManageUserAccount();
                    }
                    break;
                }
                case "7":{
                    if(currentAdmin != null) {
                        currentAdmin.resetPassword();
                    }
                    break;
                }
                case "8":{
                    if (currentAdmin != null) {
                        // switch  Admin mode  back to  Guest mode
                        currentAdmin = null;
                        System.out.println("\n\t"+"*".repeat(70));
                        System.out.println("\t\tLogged out successfully.");
                        System.out.println("\n\t"+"*".repeat(70));
                    }
                    break;
                }
                case "0": {
                    System.out.println();
                    System.out.printf("%15s%70s%15s\n","","*".repeat(70),"");
                    System.out.printf("%15s%2s%66s%2s%15s\n","","*".repeat(2),"  "+"-".repeat(62)+"  ","*".repeat(2),"");
                    System.out.printf("%15s%2s%21s%24s%21s%2s%15s\n","","*".repeat(2),"","> === Exit  System === <","","*".repeat(2),"");
                    System.out.printf("%15s%2s%66s%2s%15s\n","","*".repeat(2),"","*".repeat(2),"");
                    System.out.printf("%15s%2s%6s%54s%6s%2s%15s\n","","*".repeat(2),"","** Thank you for using Food Delivery Routing System **","","*".repeat(2),"");
                    System.out.printf("%15s%2s%20s%26s%20s%2s%15s\n","","*".repeat(2),"","** See you next time !! **","","*".repeat(2),"");
                    System.out.printf("%15s%2s%66s%2s%15s\n","","*".repeat(2),"  "+"-".repeat(62)+"  ","*".repeat(2),"");
                    System.out.printf("%15s%70s%15s\n","","*".repeat(70),"");
                    System.out.println();
                    System.out.println(solidLine);
                    return;
                }
                default:{
                    System.out.println(invalidOptionMsg);
                }
            }
        }
    }
    
    
    // module 1 - Create Routing Graph
    public void optionCreateGraph(){
        while(true){
            System.out.println(solidLine);
            System.out.println("|#| >  Main Menu  >  Create Routing Graph");
            System.out.println("=========================================\n");
            
            System.out.println(dottedLine);
            System.out.println(">=== Create Routing Graph ===<");
            System.out.println(dottedLine);
            System.out.println("\n"
                    +"\t1\t-\tAdd a Location\n"
                    +"\t2\t-\tRemove a Location\n"
                    +"\t3\t-\tAdd a Route\n"
                    +"\t4\t-\tRemove a Route\n"
                    +"\t0\t-\tReturn to Main Menu\n");
            System.out.println(dottedLine);
            System.out.print("\n  Selection: ");
            String choice = sc.nextLine();
            
            switch (choice) {
                case "1": {
                    addLocationFlow(); 
                    break;
                }
                case "2": {
                    removeLocationFlow(); 
                    break;
                }
                case "3": {
                    addRouteFlow(); 
                    break;
                }
                case "4": {
                    removeRouteFlow(); 
                    break;
                }
                case "0": return; // jump back to main menu
                default: System.out.println(invalidOptionMsg);
            }
        }
    }
    // Exception handling for special character
    private boolean exceptionHandlingSpecialCharacter(String selectedDataField,String scannerData){
        Pattern characterInput = Pattern.compile("[a-z A-Z 0-9]+"); // at least 1 char
        Matcher match = characterInput.matcher(scannerData);
        if(scannerData == null){
            System.out.println("\n\t"+"*".repeat(70));
            System.out.printf("\n\tINPUT ERROR : '%s' must not be empty !!!\n",selectedDataField);
            System.out.println("\n\t"+"*".repeat(70));
            return false;
        } else if(!match.matches()){
            System.out.println("\n\t"+"*".repeat(70));
            System.out.printf("\n\tINPUT ERROR : '%s' must not contain special character !!!\n",selectedDataField);
            System.out.println("\n\t"+"*".repeat(70));
            return false;
        }
        return true;
    }
    private boolean exceptionHandlingOptionInt(String selectedDataField,String scannerData){
        Pattern characterInput = Pattern.compile("[0-9]{1}"); // pnly accept with one digit
        Matcher match = characterInput.matcher(scannerData);
        if(scannerData == null){
            System.out.println("\n\t"+"*".repeat(70));
            System.out.printf("\t\tINPUT ERROR : '%s' cannot be empty !!!\n",selectedDataField);
            System.out.println("\n\t"+"*".repeat(70));
            return false;
        } else if(!match.matches()){
            System.out.println("\n\t"+"*".repeat(70));
            System.out.printf("\t\tINPUT ERROR : '%s' must be a single digit !!!",selectedDataField);
            System.out.println("\n\t"+"*".repeat(70));
            return false;
        }
        return true;
    }
    private boolean exceptionHandlingString(String selectedDataField,String scannerData){
        Pattern characterInput = Pattern.compile("[a-z A-Z \\,]+"); // at least 1 char & ','
        Matcher match = characterInput.matcher(scannerData);
        if(scannerData == null){
            System.out.println("\n\t"+"*".repeat(70));
            System.out.printf("\t\tINPUT ERROR : '%s' must not be empty !!!",selectedDataField);
            System.out.println("\n\t"+"*".repeat(70));
            return false;
        } else if(!match.matches()){
            System.out.println("\n\t"+"*".repeat(70));
            System.out.printf("\t\tINPUT ERROR : '%s' must contain only 'a'-'z' & 'A'-'Z' !!!",selectedDataField);
            System.out.println("\n\t"+"*".repeat(70));
            return false;
        }
        return true;
    }
    private boolean exceptionHandlingDuplicateName(String selectedDataField,String scannerData){
        Pattern characterInput = Pattern.compile("[a-z A-Z]+"); // at least 1 char
        Matcher match = characterInput.matcher(scannerData);
        
        if(scannerData == null){
            System.out.println("\n\t"+"*".repeat(70));
            System.out.printf("\t\tINPUT ERROR : '%s' must not be empty !!!",selectedDataField);
            System.out.println("\n\t"+"*".repeat(70));
            return false;
        } else if(!match.matches()){
            System.out.println("\n\t"+"*".repeat(70));
            System.out.printf("\t\tINPUT ERROR : '%s' must contain only 'a'-'z' & 'A'-'Z' !!!",selectedDataField);
            System.out.println("\n\t"+"*".repeat(70));
            return false;
        }
        
        // check duplicated name in current vertices list
        boolean isDuplicateName = false;

        // check if the name is exist in current vertices list
        for (String currentName : V) {
            if (scannerData.equalsIgnoreCase(currentName)) {
                isDuplicateName = true;
                break;
            }
        }
        // check if the name is exist in current vertices list including user input
        if (graph.getLocationByName(scannerData) != null) {
            isDuplicateName = true;
        }
        if (isDuplicateName) {
            System.out.println("\n\t" + "*".repeat(70));
            System.out.printf("\t\tINPUT ERROR : '%s' is currently exist in vertices list !!!\n\t\tPlease try another name !!!", scannerData);
            System.out.println("\n\t" + "*".repeat(70));
            return false;
        }
        return true;
    }
    private boolean exceptionHandlingMatchVertexListName(String selectedDataField,String scannerData){
        Pattern characterInput = Pattern.compile("[a-z A-Z]+"); // at least 1 char
        Matcher match = characterInput.matcher(scannerData);
        
        if(scannerData == null){
            System.out.println("\n\t"+"*".repeat(70));
            System.out.printf("\t\tINPUT ERROR : '%s' must not be empty !!!",selectedDataField);
            System.out.println("\n\t"+"*".repeat(70));
            return false;
        } else if(!match.matches()){
            System.out.println("\n\t"+"*".repeat(70));
            System.out.printf("\t\tINPUT ERROR : '%s' must contain only 'a'-'z' & 'A'-'Z' !!!",selectedDataField);
            System.out.println("\n\t"+"*".repeat(70));
            return false;
        }
        
        // check matching name in current vertices list
        boolean isMatchVertexName = false;

        // check if the name is exist in current vertices list
        for (String currentName : V) {
            if (scannerData.equalsIgnoreCase(currentName)) {
                isMatchVertexName = true;
                break;
            }
        }
        // check if the name is exist in current vertices list including user input
        if (graph.getLocationByName(scannerData) != null) {
            isMatchVertexName = true; // if selected vertex is exist in current vertices list
        }
        if (!isMatchVertexName) {
            System.out.println("\n\t" + "*".repeat(70));
            System.out.printf("\t\tINPUT ERROR : '%s' does not exist in vertices list !!!\n\t\tPlease select the vertex from current list !!!", scannerData);
            System.out.println("\n\t" + "*".repeat(70));
            return false;
        }
        return true;
    }
    private boolean exceptionHandlingDigit(String selectedDataField,String scannerData){
        Pattern characterInput = Pattern.compile("[0-9 ]+"); // at least 1 char
        Matcher match = characterInput.matcher(scannerData);
        if(scannerData == null){
            System.out.println("\n\t"+"*".repeat(70));
            System.out.printf("\t\tINPUT ERROR : '%s' must not be empty !!!",selectedDataField);
            System.out.println("\n\t"+"*".repeat(70));
            return false;
        } else if(!match.matches()){
            System.out.println("\n\t"+"*".repeat(70));
            System.out.printf("\t\tINPUT ERROR : '%s' must contain only '0'-'9' !!!",selectedDataField);
            System.out.println("\n\t"+"*".repeat(70));
            return false;
        }
        return true;
    }
    private boolean exceptionHandlingPhone(String selectedDataField,String scannerData){
        Pattern phoneFormatInputA = Pattern.compile("[0-9]{9,10}");  // 9 to 10 digit for phone
        Pattern phoneFormatInputB = Pattern.compile("[0-9]{3}\\-{1}[0-9]{3,4} [0-9]{4}");   // 01x-xxxx xxxx (accepted format)
        Matcher matchA = phoneFormatInputA.matcher(scannerData);
        Matcher matchB = phoneFormatInputB.matcher(scannerData);
        if(scannerData == null){
            System.out.println("\n\t"+"*".repeat(70));
            System.out.printf("\t\tINPUT ERROR : '%s' must contain 9 to 10 digits !!!\n",selectedDataField);
            System.out.println("\n\t"+"*".repeat(70));
            return false;
        } else if(!(matchA.matches() || matchB.matches())){
            System.out.println("\n\t"+"*".repeat(70));
            System.out.printf("\t\tINPUT ERROR : '%s' no follow the valid format !!! \n\n\t\t**Phone format must : \n\t\t\t(/) contain only '0'-'9'\n\t\t\t\tOR\n\t\t\t(/) in 01x-xxxx xxxx\n",selectedDataField);
            System.out.println("\n\t"+"*".repeat(70));
            return false;
        }
        return true;
    }
    private boolean exceptionHandlingPrice(String selectedDataField,double scannerData){
        if(scannerData == 0.00){
            System.out.println("\n\t"+"*".repeat(70));
            System.out.printf("\t\tINPUT ERROR : '%s' cannot be RM0.00 !!!",selectedDataField);
            System.out.println("\n\t"+"*".repeat(70));
            return false;
        } else if(scannerData < 0.00){
            System.out.println("\n\t"+"*".repeat(70));
            System.out.printf("\t\tINPUT ERROR : '%s' cannot be negative value !!!",selectedDataField);
            System.out.println("\n\t"+"*".repeat(70));
            return false;
        }
        return true;
    }
    
    // submodule 1.1 - add new location vertex 
    private void addLocationFlow() {
        System.out.println(solidLine);
        System.out.println("|#| >  Main Menu  >  Create Routing Graph  >  Add a Location");
        System.out.println("============================================================\n");
        
        System.out.println(dottedLine);
        System.out.println(">=== Add a Location ===<");
        System.out.println(dottedLine);
        System.out.println("\n\tEnter '1' to '4' for location type.\n\tAnd '0' to previous page.\n");
        System.out.println(  "\t\t1 - Supplier\n"
                            +"\t\t2 - Intersection\n"
                            +"\t\t3 - Delivery Hub\n"
                            +"\t\t4 - Customer\n"
                            +"\t\t0 - Back");
        System.out.println(dottedLine);
        
        String type;
        while(true){
            System.out.print("\n  Selection: ");
            type = sc.nextLine();
            if(exceptionHandlingOptionInt("Location type",type)){
                if((type.equals("1") || type.equals("2") || type.equals("3") || type.equals("4"))){
                    break; // if return true then stop looping
                }else if(type.equals("0")){
                    return; // back to previous menu
                }else {
                    System.out.println("\n\t"+"*".repeat(70));
                    System.out.println("\t\tINPUT ERROR : 'Location type' must be a valid type in above list !!!");
                    System.out.println("\n\t"+"*".repeat(70));
                    continue; // keep looping if Selection not as 1,2,3,4
                }
            }
            
            
        }
        
        System.out.println(dottedLine);
        StringBuilder sbCurrentLocationType = new StringBuilder();
        switch(type){
            case "1": {
                sbCurrentLocationType.append("Supplier"); 
                break;
            }
            case "2": {
                sbCurrentLocationType.append("Intersection");
                break;
            }
            case "3": {
                sbCurrentLocationType.append("Delivery Hub"); 
                break;
            }
            case "4": {
                sbCurrentLocationType.append("Customer");
            }
        }
        
        System.out.println(solidLine);
        System.out.printf("|#| >  Main Menu  >  Create Routing Graph  >  Add a Location  >  %s\n",sbCurrentLocationType);
        System.out.println("================================================================="+"=".repeat(sbCurrentLocationType.length())+"\n");
        
        System.out.println(dottedLine);
        System.out.printf(">=== Location infomations : %s ===<\n",sbCurrentLocationType);
        System.out.println(dottedLine);
        
        String name;
        while(true){
            System.out.print("\n\tEnter name\t\t: ");
            name = sc.nextLine().trim();
            
            // if (return) true then break
            if(exceptionHandlingDuplicateName("Location name",name)){
                break;
            } 
        } 
        
        String address; 
        while(true){
            System.out.print("\n\tEnter address\t\t: ");
            address = sc.nextLine().trim();

            if(exceptionHandlingString("address",address)){
                break;
            }
        }
        
        Location loc;
        switch (type) {
            case "1":{
                String phone;
                while(true){
                    System.out.print("\n\tEnter supplier phone\t: ");
                    phone = sc.nextLine().trim();
                    if(exceptionHandlingPhone("supplier phone",phone)){
                        break;
                    }
                }
                String prodName;
                while(true){
                    System.out.print("\n\tEnter product name\t: ");
                    prodName = sc.nextLine().trim();
                    if(exceptionHandlingString("product name",prodName)){
                        break;
                    }
                }
                String prodID;
                while (true) {
                    System.out.print("\n\tEnter product ID\t: ");
                    prodID= sc.nextLine().trim();
                    if(exceptionHandlingSpecialCharacter("product id",prodID)){
                        break;
                    }
                }
                double prodPrice;
                while (true) {
                    System.out.print("\n\tEnter product price\t: RM ");

                    prodPrice = Double.parseDouble(sc.nextLine().trim()); // to avoid Scanner buffer
                    if(exceptionHandlingPrice("product price",prodPrice)){
                        break;
                    }
                }
                
                loc = new Supplier(name,address,phone,prodName,prodID,prodPrice);
                break;
            }
            case "2":{
                loc = new Intersection(name,address);
                break;
            }
            case "3":{
                String deliveryID;
                while (true) {
                    System.out.print("\n\tEnter delivery ID\t: ");

                    deliveryID = sc.nextLine().trim();
                    if(exceptionHandlingString("delivery ID",deliveryID)){
                        break;
                    }
                }
                String deliveryDay;
                while (true) {
                    System.out.print("\n\tEnter delivery day\t: ");
                    deliveryDay = sc.nextLine().trim();
                    if(exceptionHandlingDigit("delivery day",deliveryDay)){
                        break;
                    }
                }
                String deliveryMonth;
                while (true) {
                    System.out.print("\n\tEnter delivery month\t: ");
                    deliveryMonth = sc.nextLine().trim();
                    if(exceptionHandlingString("delivery month",deliveryMonth)){
                        break;
                    } 
                }
                String deliveryYear;
                while (true) {
                    System.out.print("\n\tEnter delivery year\t: ");
                    deliveryYear = sc.nextLine().trim();
                    if(exceptionHandlingDigit("delivery year",deliveryYear)){
                        break;
                    }
                }
                double deliveryCost;
                while (true) {
                    System.out.print("\n\tEnter delivery cost\t: RM ");
                    deliveryCost = Double.parseDouble(sc.nextLine().trim()); // to avoid Scanner buffer
                    if(exceptionHandlingPrice("delivery cost",deliveryCost)){
                        break;
                    }
                }
                
                loc = new DeliveryHub(name, address, deliveryID, deliveryDay, deliveryMonth, deliveryYear, deliveryCost);
                break;
            }
            case "4":{
                String custPhone;
                while (true) {
                    System.out.print("\n\tEnter customer phone\t: ");

                    custPhone = sc.nextLine().trim();
                    if(exceptionHandlingPhone("customer phone",custPhone)){
                        break;
                    }
                }
                loc = new Customer(name,address,custPhone);
                break;
            }
            default:
                System.out.println("\n\t"+"*".repeat(70));
                System.out.println("\n\t\tInvalid type location !!!\n\t\tCancelling process......\n\n");
                System.out.println("\n\t"+"*".repeat(70));
                return; // jump back to main menu
        }
        
        graph.addLocation(loc);
        System.out.println("\n\t"+"*".repeat(70));
        System.out.printf("\t\tLocation %s has been added successfully.\n",name);
        System.out.println("\t"+"*".repeat(70));
        
        System.out.println("\n\n\t\t<< Press enter key to return main menu page.... >>\n");
        sc.nextLine(); // receive enter key
    }
    // submodule 1.2 - remove a location vertex
    private void removeLocationFlow() {
        System.out.println(solidLine);
        System.out.println("|#| >  Main Menu  >  Create Routing Graph  >  Remove a Location");
        System.out.println("===============================================================\n");
        
        System.out.print("\n\tEnter the location name to remove: ");
        String name = sc.nextLine().trim();
        
        // get selected location vertex
        Location loc = graph.getLocationByName(name);
        
        // if selected location vetrex not found
        if (loc == null) {
            System.out.println("\n\t"+"*".repeat(70));
            System.out.printf("\t\t\tLocation %s not found.\n",name);
            System.out.println("\n\t"+"*".repeat(70));
            return;
        }
        // remove selected location vertex
        graph.removeLocation(loc);
        
        System.out.println("\n\t"+"*".repeat(70));
        System.out.printf("\t\t\tLocation %s has been removed.",name);
        System.out.println("\n\t"+"*".repeat(70));
    }
    // submodule 1.3 - add a new route (edge)
    private void addRouteFlow() {
        System.out.println(solidLine);
        System.out.println("|#| >  Main Menu  >  Create Routing Graph  >  Add a Route");
        System.out.println("=========================================================\n");
        
        // get starting vertex
        System.out.print("\tEnter the 1st location: ");
        String name1 = sc.nextLine().trim();
        // get adjacency vertex
        System.out.print("\tEnter the 2nd location: ");
        String name2 = sc.nextLine().trim();
        
        Location a = graph.getLocationByName(name1);
        Location b = graph.getLocationByName(name2);
        
        // if missing any vertex then return back to menu
        if (a == null || b == null) {
            System.out.println("\n\t"+"*".repeat(70));
            System.out.println("\n\tOne or both locations do not exist......\n\tPlease add them first.....");
            System.out.println("\n\t"+"*".repeat(70));
            return;
        }
        
        // check if a and b is same vertex
        // to avoid self-looping
        if (a.equals(b)) {
            System.out.println("\n\t"+"*".repeat(70));
            System.out.println("\n\tCannot create a route from a location to itself !!!");
            System.out.println("\n\t"+"*".repeat(70));
            return;
        }
        
        // check if route already exists between a and b
        boolean isDuplicateRoute = false;
        for (Route rt:graph.getAdjacent(a)) {
            if (rt.getOther(a).equals(b)) {
                isDuplicateRoute = true;
                break;
            }
        }
        if (isDuplicateRoute) {
            System.out.println("\n\t"+"*".repeat(70));
            System.out.printf("\tA route between %s and %s already exists !!!\n\tPlease try another pair or remove the existing route first !!!", name1, name2);
            System.out.println("\n\t"+"*".repeat(70));
            return;
        }
        
        // add path into graph list
        graph.addRoute(a, b);
        System.out.println("\n\t"+"*".repeat(70));
        System.out.printf("\tThere is now a route between %s and %s",name1,name2);
        System.out.println("\n\t"+"*".repeat(70));
    }
    // submodule 1.4 - remove a route
    private void removeRouteFlow() {
        System.out.println(solidLine);
        System.out.println("|#| >  Main Menu  >  Create Routing Graph  >  Remove a Route");
        System.out.println("============================================================\n");
        
        // get starting vertex
        System.out.print("\nEnter the 1st location: ");
        String name1 = sc.nextLine().trim();
        // get adjacency vertex
        System.out.print("Enter the 2nd location: ");
        String name2 = sc.nextLine().trim();

        Location a = graph.getLocationByName(name1);
        Location b = graph.getLocationByName(name2);
        
        // if missing any vertex then return back to menu
        if (a == null || b == null) {
            System.out.println("\nOne or both locations do not exist.\n");
            return;
        }
        
        // remove path from graph list
        boolean isRemoved = graph.removeRoute(a,b);
        StringBuffer removedValid = new StringBuffer("\n\t"+"*".repeat(70));
        
        String removedSuccessMsg = String.format("\n\tThere is now a route between %s and %s has been removed.\n",name1,name2);
        String removedFailedMsg = String.format("\n\tNo existing route was found between %s and %s. Nothing to remove !!!\n",name1,name2);
        if(isRemoved){
            removedValid.append(removedSuccessMsg);
        }else{
            removedValid.append(removedFailedMsg);
        }
        removedValid.append("\n\t"+"*".repeat(70));
        System.out.println(removedValid);
    }
    
    
    // module 2 - search any vertex in graph
    public void optionSearchGraph(){
        System.out.println(solidLine);
        System.out.println("|#| >  Main Menu  >  Search location");
        System.out.println(starLine);
        
        System.out.println(dottedLine);
        System.out.println(">=== Search Location ===<");
        System.out.println(dottedLine);
        System.out.print("\n\tEnter the name of the location to search: ");
        String name = sc.nextLine().trim();
        
        // get location by searching name
        Location loc = graph.getLocationByName(name);
        if (loc == null) {
            System.out.printf("\n\tLocation '%s' was not found !!!\n",name);
            return; // jump back to menu
        }
        System.out.println("\n\tLocation Found: "+loc.toString());
        System.out.println(dottedLine);
    }
    
    // module 3 - list overall route step-by-step
    public void optionViewRoutingGraph(){
        System.out.println(solidLine);
        System.out.println("|#| >  Main Menu  >  View the FOOD delivery network");
        System.out.println(starLine);
        
        System.out.println(dottedLine);
        System.out.println(">=== Routing Graph Overview ===<");
        System.out.println(dottedLine);
        
        // display routing graph step-by-step
        // a -> b [n minute(s)]
        System.out.println(graph.displayRoutingGraph());
        
        System.out.println(solidLine);
    }
    
    // module 4 - place a new order then compute route
    public void optionOrderComputeRoute(){
        System.out.println(solidLine);
        System.out.println("|#| >  Main Menu  >  Place an order & compute route");
        System.out.println(starLine);
        
        System.out.println(dottedLine);
        System.out.println(">=== Place an Order & Compute Route ===<");
        System.out.println(dottedLine);
        
        
        System.out.println("\n\t > Customer info ");
        
        String custName;
        while(true){
            System.out.print("\n\t\tEnter your name\t\t: ");
            custName = sc.nextLine().trim();
            
            // if format invalid then re-enter
            if (!exceptionHandlingString("Customer name", custName)) {
                continue;
            }
            
            // check if the custName same as existing current vertex name (location)
            boolean isVertexName = false;
            // traversal all elements in current vertex list
            for(Location currentLoc:graph.getAllLocations()){
                if(custName.equalsIgnoreCase(currentLoc.getName())){
                    isVertexName = true;
                    break;
                }
            }
            
            StringBuffer isVertexNameMsg = new StringBuffer(
                    String.format("INPUT ERROR : '%s' is currently exist in vertices list !!!", custName));
            String tryAnotherName = " Please use another name !!!";
            int stringMsgWidth = 90;
            int spaceIsVertexNameMsg = (stringMsgWidth - 10 - isVertexNameMsg.length()) / 2;
            int spaceTryAnotherName = (stringMsgWidth - 10 - tryAnotherName.length()) / 2;
            
            if(isVertexName){
                System.out.printf("\n\t%s\n\t%s%s%s%s%s\n\t%s%s%s%s%s\n\t%s\n\n",
                        "*".repeat(stringMsgWidth),
                        "*".repeat(5),
                        " ".repeat(spaceIsVertexNameMsg),
                        isVertexNameMsg,
                        " ".repeat(spaceIsVertexNameMsg),
                        "*".repeat(5),
                        "*".repeat(5),
                        " ".repeat(spaceTryAnotherName),
                        tryAnotherName,
                        " ".repeat(spaceTryAnotherName),
                        "*".repeat(5),
                        "*".repeat(stringMsgWidth));
                continue;
            }
          
            
            // if a valid name then break, next input
            break; 
        }
        
        String custPhone;
        while(true){
            System.out.print("\n\t\tEnter phone number\t: ");
            custPhone = sc.nextLine().trim();
            
            // if (return) true then break
            if(exceptionHandlingPhone("Customer phone",custPhone)){
                break;
            } 
        }
        
        String address;
        while(true){
            System.out.print("\n\t\tEnter address\t\t: ");
            address = sc.nextLine().trim();
            
            // if (return) true then break
            if(exceptionHandlingString("Customer address",address)){
                break;
            } 
        }
        
        // check if current user has placed order before
        // reuse same Customer obj
        Customer currentUser = null;
        for(Customer c:customerList){
            if(c.getName().equalsIgnoreCase(custName) && c.getCustPhone().equalsIgnoreCase(custPhone)){
                    currentUser = c;
                    break; // same name & custPhone, same obj
            }
        }
        
        if (currentUser == null) {
            currentUser = new Customer(custName, address, custPhone);
            customerList.add(currentUser); // add new obj into list
        }
       
        System.out.println("\n\t > Route info \n\t   **All vertex must exits in current routing system**");
        
        String supplierName;
        while(true){
            System.out.print("\n\t\tEnter Supplier (Start vertex) name\t\t: ");
            supplierName = sc.nextLine().trim();
            
            if (exceptionHandlingMatchVertexListName("Supplier vertex", supplierName)) {
                break;
            }
        }
        
        String customerName;
        while(true){
            System.out.print("\n\t\tEnter any target customer (End vertex) name\t: ");
            customerName = sc.nextLine().trim();
            
            // if (return) true then break
            if (exceptionHandlingMatchVertexListName("Customer vertex", customerName)) {
                break;
            }
        }
        
        // finding location vertex
        Location supplierLoc = graph.getLocationByName(supplierName);
        Location customerLoc = graph.getLocationByName(customerName);

        // if location vertex not found
        if (supplierLoc == null) {
            System.out.println("\n\t"+"*".repeat(70));
            System.out.println("\t\tSupplier location not found !!!\n");
            System.out.println("\n\t"+"*".repeat(70));
            return;
        }
        if (customerLoc == null) {
            System.out.println("\n\t"+"*".repeat(70));
            System.out.println("\t\tCustomer location not found !!!\n");
            System.out.println("\n\t"+"*".repeat(70));
            return;
        }
        if (supplierLoc == null || customerLoc == null) {
            System.out.println("\n\t"+"*".repeat(70));
            System.out.println("\t\tSupplier or Customer location not found !!!");
            System.out.println("\n\t"+"*".repeat(70));
            return;
        }
        
        // if not a supplier vertex
        if (!(supplierLoc instanceof Supplier)){
            System.out.println("\n\t"+"*".repeat(70));
            System.out.println("\t\tINVALID LOCATION TYPE : check supplier name !!!");
            System.out.println("\n\t"+"*".repeat(70));
            return;
        }
        // if not a customer vertex
        if (!(customerLoc instanceof Customer)) {
            System.out.println("\n\t"+"*".repeat(70));
            System.out.println("\t\tINVALID LOCATION TYPE : check customer name !!!");
            System.out.println("\n\t"+"*".repeat(70));
            return;
        }
        /*
        Order(Location supplier,Location customer){
        */
        Order order = new Order(supplierLoc, customerLoc);

        if (!order.computeRoute(graph)) {
            System.out.println("\n\t"+"*".repeat(70));
            System.out.println("\t\tRESULT : No path found between supplier and customer !!!");
            System.out.println("\n\t"+"*".repeat(70));
            return;
        }
        // prodPrice in sp can be passed into calculatePayAmount()
        Supplier sp = (Supplier) supplierLoc;
        
        // store DeliveryHub if found in path
        // else is null
        DeliveryHub dh = null;
        
        for (Location loc : order.getPath()) {
            if (loc instanceof DeliveryHub) {
                dh = (DeliveryHub) loc;
                break;
            }
        }
        // pay amount for order
        double total = 0.0;
        
        if (dh != null) {
            total = currentUser.calculatePayAmount(sp, dh);
        } else {
            System.out.println("\n\t"+"*".repeat(70));
            System.out.println("\t\tERROR: No delivery hub on this route !!! \n\t\tTotal cost cannnot be calculated !!!");
            System.out.println("\t"+"*".repeat(70));
        }        
        System.out.printf("\n\t>=== Order Details ===<"
               +"\n\tOrder ID\t:\t%s"
               +"\n\tOrdered by\t:\t%s"
               +"\n\tSupplier\t:\t%s"
               +"\n\tCustomer\t:\t%s"
               +"\n\tRoute\t\t:\t%s"
               +"\n\tTotal Paid\t:\tRM%6.2f\n\n"
        ,order.getOrderID(),custName,supplierName,customerName,order.getPathToString(),total);
        
        // store user name & pay amount into Order obj
        order.setCustName(custName);
        order.setTotalPaid(total);
        
        orderHistory.addOrder(order);
    }
    
    // module 5 - login page for admin
    public void optionAdminLogin(){
        System.out.println(solidLine);
        System.out.println("|#| >  Main Menu  >  Admin Login");
        System.out.println("================================\n");
        
        if (admin.staffLogin()) {
            this.currentAdmin = admin;
        }   
    }
    
    // module 6 (only for admin) - managing user (customer) account
    public void optionManageUserAccount(){
        System.out.println(solidLine);
        System.out.println("|#| >  Main Menu (Admin)  >  Manage User Account");
        System.out.println(starLine);
        
        System.out.print("\nEnter the user name to manage: ");
        String name = sc.nextLine().trim();
        currentAdmin.manageCustomerAccount(customerList, name);
    }
}
