#include <iostream>
#include <iomanip>
#include <string>
#include <cstdlib>
#include <ctime>
#include<cctype>
using namespace std;

string name;

int displayMenu()
{
    int choice;
    cout << "================ MENU ===============" << endl;
    cout << "1. Book a Ticket" << endl;
    cout << "2. Staff: View Income Report" << endl;
    cout << "=====================================" << endl;
    cout << "Enter your choice (1-2): ";
    cin >> choice;
    cin.ignore();
    return choice;
}

int admin(string train[], int price[][3], int seat[][3], string classes[]) {
    int countaccount = 0, countpassword = 0;
    string account, password;

    do {
        cout << "Enter your account: ";
        getline(cin, account);
        if (account == "RAY EUGUNE JACKSON")
        {
            do {
                cout << "Enter your password: ";
                getline(cin, password);
                if (password == "WMD2007")
                {
                    int adminChoice;
                    do {
                        cout << endl << "============================================== ADMIN DASHBOARD ==============================================" << endl;
                        cout << "1. View Income Report" << endl;
                        cout << "2. Close Business (Exit Program)" << endl;
                        cout << "3. Return to Main Menu" << endl;
                        cout << "=============================================================================================================" << endl;
                        cout << "Enter your choice: ";
                        cin >> adminChoice;
                        cin.ignore();

                        if (adminChoice == 1)
                        {
                            double totalIncome = 0;

                            cout << endl << "================================================= INCOME REPORT ==================================================" << endl;
                            cout << left << setw(20) << "Destination" << setw(32) << "Economy" << setw(32) << "Luxury" << setw(30) << "Business" << endl;

                            cout << left << setw(20) << " " << setw(7) << "Price" << setw(6) << "Sold" << setw(8) << "Remain" << setw(11) << "Income" << setw(7) << "Price" << setw(6) << "Sold" << setw(8) << "Remain" << setw(11) << "Income" << setw(7) << "Price" << setw(6) << "Sold" << setw(8) << "Remain" << setw(10) << "Income" << endl;

                            cout << "==================================================================================================================" << endl;
                            for (int i = 0; i < 11; i++) {
                                cout << left << setw(2) << i + 1 << "." << setw(17) << train[i];

                                for (int j = 0; j < 3; j++) {
                                    int totalSeats[3] = { 100,10,30 };
                                    int sold = totalSeats[j] - seat[i][j];
                                    double income = (sold * price[i][j]) * 1.06;
                                    int remain = seat[i][j];
                                    totalIncome += income;

                                    cout << left << "RM" << setw(5) << price[i][j] << setw(6) << sold << setw(8) << remain << "RM" << setw(9) << fixed << setprecision(2) << income;
                                }
                                cout << endl;
                            }
                            cout << "==================================================================================================================" << endl;
                            cout << "Today's Total Income: RM " << fixed << setprecision(2) << totalIncome << endl << endl;
                        }

                        else if (adminChoice == 2)
                        {
                            cout << endl << "Business Closed. Exiting program.";
                            return 0;
                        }
                        else if (adminChoice == 3)
                        {
                            cout << "Returning to main menu" << endl;
                            return 1;
                        }
                        else
                        {
                            cout << "Invalid choice. Please try again.\n";
                        }
                    } while (adminChoice != 3);
                }
                else
                {
                    cout << "Invalid password. Please try again." << endl;
                    countpassword++;
                }
            } while (password != "WMD2007" && countpassword < 5);
            if (countpassword == 5)
            {
                cout << endl << "SECURITY ALERT !!! Too many wrong password attempts !!!" << endl;
                return 1;
            }
        }
        else
        {
            cout << "Invalid account. Please try again." << endl;
            countaccount++;
        }
    } while (account != "RAY EUGUNE JACKSON" && countaccount < 5);
    if (countaccount == 5)
    {
        cout << "SECURITY ALERT !!! Too many wrong account attempts !!!" << endl;
    }
    return 1;
}

// ======= Name input =======
string namee(string name)
{
    cout << "Enter your name: ";
    getline(cin, name);
    return name;
}

// ======= Booking =======
void bookTicket(string train[], int price[][3], int seat[][3], string classes[]) {
    cout << setfill('=') << setw(37) << "=" << setfill(' ') << endl;
    cout << "Welcome to ABC Ticketing Service!" << endl;
    cout << setfill('=') << setw(37) << "=" << setfill(' ') << endl;
    name = namee(name);
    int day, month, year;
    cout << endl << "======== Select Travel Date =========" << endl;
    cout << "Enter year (2025-2027): ";
    cin >> year;
    while (year !=2025 && year !=2026 && year!=2027 )
    {
        cout << "Invalid year,please enter between 2025-2027: ";
        cin >> year;
    }
    cout << "Enter month (1-12): ";
    cin >> month;
    while (month < 1 || month > 12)
    {
        cout << "Invalid month,please enter between 1-12: ";
        cin >> month;
    }
    cout << "Enter day (1-31): ";
    cin >> day;
    while (day < 1 || day > 31)
    {
        cout << "Invalid day,please enter between 1-31: ";
        cin >> day;
    }

    // input time
    int hour;
    cout << endl << "======= Select Departure Time =======" << endl;
    cout << "Enter hour (0-23): ";
    cin >> hour;
    while (hour < 0 || hour > 23)
    {
        cout << "Invalid hour! Please enter between 0-23: ";
        cin >> hour;
    }

    // display departures 
    const int NUM_WIDTH = 2;
    const int NAME_WIDTH = 20;
    const int SEATS_WIDTH = 6;
    cout << endl << "Available Departures:" << endl;
    cout << endl << "============================= AVAILABLE DEPARTURES =============================" << endl;
    cout << left << setw(23) << "Destination" << setw(20) << "Economy" << setw(20) << "Luxury" << setw(20) << "Business" << endl;

    cout << left << setw(23) << " " << setw(10) << "Price" << setw(10) << "Seats" << setw(10) << "Price" << setw(10) << "Seats" << setw(10) << "Price" << setw(10) << "Seats" << endl;

    cout << "================================================================================" << endl;

    for (int i = 0; i < 11; i++) {
        cout << left << setw(2) << i + 1 << "." << setw(20) << train[i] << "RM" << setw(8) << price[i][0] << setw(10) << seat[i][0] << "RM" << setw(8) << price[i][1] << setw(10) << seat[i][1] << "RM" << setw(8) << price[i][2] << setw(10) << seat[i][2] << endl;
    }
    cout << "================================================================================" << endl;
    // input departures 
    int departure;
    int validDeparture = 0;
    do {
        cout << "\nChoose your departure (1-11): ";
        cin >> departure;
        if (departure < 1 || departure > 11)
        {
            cout << "Invalid choice! Please select a number between 1 and 11." << endl;
        }
        else
        {
            validDeparture = 1;
        }
    } while (validDeparture == 0);

    cout << endl << "Available Destinations:" << endl;
    cout << left << setw(NUM_WIDTH) << " " << setw(NAME_WIDTH) << "Destination" << endl;
    cout << "=====================================" << endl;

    for (int i = 0; i < 11; i++)
        if (departure != 1 + i)
            cout << left << setw(NUM_WIDTH) << i + 1 << "." << setw(NAME_WIDTH) << train[i] << endl;

    // input destination
    int destination;
    int validDestination = 0;
    do {
        cout << "\nChoose your destination (1-11, excluding " << departure << "): ";
        cin >> destination;

        if (destination < 1 || destination > 11)
        {
            cout << "Invalid choice! Please select a number between 1 and 11." << endl;
        }
        else if (destination == departure)
        {
            cout << "Destination cannot be the same as departure! Please choose a different destination." << endl;
        }
        else
        {
            validDestination = 1;
        }
    } while (validDestination == 0);


    cout << endl << "========= Select Seat Class =========" << endl;
    for (int i = 0; i < 3; i++) {
        cout << left << setw(1) << i + 1 << "." << setw(8) << classes[i] << setw(10) << " | Price: RM " << price[departure - 1][i] << " | Seats: " << seat[departure - 1][i] << endl;
    }

    int classChoice;
    do {
        cout << "Choose your class (1-3): ";
        cin >> classChoice;
        if (classChoice < 1 || classChoice > 3) {
            cout << "Invalid choice! Please select a number between 1 and 3." << endl;
        }
    } while (classChoice < 1 || classChoice > 3);

    int chosenPrice = price[departure - 1][classChoice - 1];
    int availableSeats = seat[departure - 1][classChoice - 1];

    int ticketCount;
    do {
        cout << "Enter number of tickets: ";
        cin >> ticketCount;
        if (ticketCount <= 0) {
            cout << "Invalid number! Please enter at least 1." << endl;
        }
        else if (ticketCount > availableSeats) {
            cout << "Not enough seats! Only " << availableSeats << " seats available." << endl;
        }
    } while (ticketCount <= 0 || ticketCount > availableSeats);

    double serviceFee = chosenPrice * 0.06 * ticketCount;
    double totalAmount = serviceFee + (chosenPrice * ticketCount);

    // output confirmation
    cout << endl << "========= Route Information =========" << endl;
    cout << left << setw(17) << "Departure: " << train[departure - 1] << endl;
    cout << left << setw(17) << "Destination: " << train[destination - 1] << endl;
    cout << left << setw(17) << "Available Seats: " << availableSeats << endl;
    cout << left << setw(17) << "Tickets: " << ticketCount << endl;
    cout << left << setw(17) << "Ticket Price: " << "RM " << chosenPrice << endl;
    cout << setfill('=') << setw(37) << "=" << setfill(' ') << endl;

    // confirmation on booking 
    char confirm;
    int validConfirm = 0;
    do {
        cout << "\nConfirm booking? (Y/N): ";
        cin >> confirm;
        if (toupper(confirm) == 'Y')
        {
            if (availableSeats > 0)
            {
                seat[departure - 1][classChoice - 1] -= ticketCount;
                int paymentChoice;
                cout << endl << "========== Payment Options ==========" << endl;
                cout << "1.Cash" << endl;
                cout << "2.E-Wallet" << endl;
                cout << "3.Credit/Debit Card" << endl;
                cout << "=====================================" << endl;
                do {
                    cout << "Choose payment method (1-3): ";
                    cin >> paymentChoice;
                    if (paymentChoice < 1 || paymentChoice > 3) {
                        cout << "Invalid choice! Please select 1-3." << endl;
                    }
                } while (paymentChoice < 1 || paymentChoice > 3);

                int otp = 100000 + rand() % 900000;

                string paymentway;{
                switch (paymentChoice) {
                case 1:
                    paymentway = "Cash";
                    break;
                case 2: {
                    int walletChoice;
                    cout << endl;
                    cout << "========== E-Wallet Options =========" << endl;
                    cout << "Choose E-Wallet:" << endl << "1.Touch 'n Go" << endl << "2.Alipay" << endl << "3.WeChat Pay" << endl << "Choice: ";
                    cin >> walletChoice;
                    if (walletChoice == 1) paymentway = "E-Wallet (Touch 'n Go)";
                    else if (walletChoice == 2) paymentway = "E-Wallet (Alipay)";
                    else paymentway = "E-Wallet (WeChat Pay)";

                    string phone;
                    cout << "Enter your mobile number (XXX-XXXXXXXX): ";
                    getline(cin, phone);

                    cout << endl << "OTP sent to your phone" << phone << ": " << otp << endl;
                    int userOTP;
                    do {
                        cout << "Enter OTP: ";
                        cin >> userOTP;
                        if (userOTP != otp) cout << "Incorrect OTP! Try again.\n";
                    } while (userOTP != otp);
                    break;
                }
                case 3: {
                    string cardNumber,pin;
                    cout << "\n[ CARD PAYMENT ]" << endl;

                    cout << "Enter card number (last 4 digits): ";
                    cin >> cardNumber;

                    do {
                        cout << "Enter PIN (6 digit): ";
                        cin >> pin;

                        if (pin.length() != 6)
                            cout << "PIN must be exactly 6 digits!\n";
                    } while (pin.length() != 6);
                }
                    break;
                }
                }
                cout << endl << "Processing payment via " << paymentway << endl;
                double payment;
                do {
                    cout << "Total to Pay: RM " << fixed << setprecision(2) << totalAmount << endl;
                    cout << "Enter payment amount: RM ";
                    cin >> payment;

                    if (payment < totalAmount) {
                        cout << "Payment not enough! You need at least RM "
                            << fixed << setprecision(2) << totalAmount << "." << endl;
                    }
                } while (payment < totalAmount);

                double change = payment - totalAmount;
                cout << "Payment Accepted: RM " << fixed << setprecision(2) << payment << endl;
                cout << "Change: RM " << fixed << setprecision(2) << change << endl;

                cout << "Payment successful!" << endl;
                cout << endl << "========= TICKET CONFIRMED ==========" << endl;
                cout << "Name : " << name << endl;
                cout << "Route: " << train[departure - 1] << " to " << train[destination - 1] << endl;
                cout << "Date : " << month << "/" << day << "/" << year << endl;
                cout << "Time : " << hour << ":00" << endl;
                cout << "Class: " << classes[classChoice - 1] << endl;
                cout << "Tickets: " << ticketCount << endl;
                cout << "Price: RM " << chosenPrice * ticketCount << endl;
                cout << "Service Fee (6%): RM " << fixed << setprecision(2) << serviceFee << endl;
                cout << "Total Amount: RM " << fixed << setprecision(2) << totalAmount << endl;
                cout << "Customer Paid: RM " << fixed << setprecision(2) << payment << endl;
                cout << "Change: RM " << fixed << setprecision(2) << change << endl;
                cout << "Payment Method: " << paymentway << endl;
                cout << setfill('=') << setw(37) << "=" << setfill(' ') << endl;
                cout << "Thank you for your booking!" << endl;
                validConfirm = 1;
            }
            else
            {
                cout << endl << "Sorry, no seats available for " << train[departure - 1] << " to " << train[destination - 1] << " (" << classes[classChoice - 1] << " class)" << endl;
                char retry;
                cout << "Do you want to choose another class? (Y/N): ";
                cin >> retry;

                if (toupper(retry) == 'Y') {
                    cout << endl << "======= Select Seat Class =======" << endl;
                    for (int i = 0; i < 3; i++) {
                        cout << left << setw(1) << i + 1 << "." << setw(10)
                            << classes[i] << " | Price: RM " << price[departure - 1][i]
                            << " | Seats: " << seat[departure - 1][i] << endl;
                    }

                    do {
                        cout << "Choose your class (1-3): ";
                        cin >> classChoice;
                        if (classChoice < 1 || classChoice > 3) {
                            cout << "Invalid choice! Please select a number between 1 and 3." << endl;
                        }
                    } while (classChoice < 1 || classChoice > 3);

                    chosenPrice = price[departure - 1][classChoice - 1];
                    availableSeats = seat[departure - 1][classChoice - 1];
                    validConfirm = 0;
                }
                else {
                    cout << "Booking canceled. Returning to menu..." << endl;
                    return;
                }
            }
        }
        else if (toupper(confirm) == 'N')
        {
            cout << "Booking canceled. Goodbye!" << endl;
            return;
        }
        else
        {
            cout << "Invalid input! Please enter 'Y' or 'N'." << endl;
        }
    } while (validConfirm == 0);
}

int main() {
    string train[11] = { "PERLIS", "PENANG" ,"PERAK","PAHANG","KEDAH","KELANTAN","JOHOR","SELANGOR", "NEGERI SEMBILAN" ,"MELAKA" ,"TERENGGANU" };
    string classes[3] = { "Economy", "Luxury", "Business" };
    int price[11][3] = { {12, 20, 35}, {7, 15, 25}, {10, 18, 30}, {5, 12, 20}, {9, 17, 28},
    {12, 22, 36}, {11, 19, 32}, {6, 14, 24}, {13, 21, 34}, {10, 18, 30}, {15, 25, 40}
    };

    srand(time(0));
    int seat[11][3];
    for (int i = 0; i < 11; i++) {
        seat[i][0] = rand() % 101;
        seat[i][1] = rand() % 11;
        seat[i][2] = rand() % 31;
    }
    int choice = 0;
    while (1) {
        int choice = displayMenu();
        if (choice == 1) {
            bookTicket(train, price, seat, classes);
        }
        else if (choice == 2) {
            int result;
            result = admin(train, price, seat, classes);
            if (result == 0) return 0;
        }
        else {
            cout << "Invalid Choice. Please choose again." << endl;
        }
    }
    return 0;
}
