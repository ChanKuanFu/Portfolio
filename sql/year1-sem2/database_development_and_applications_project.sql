-- ============================================================
-- ZUS Coffee Beverage Database - Full Script
-- Task 4: Create database tables in Oracle
-- ============================================================

-- 4.1 Table Customer
CREATE TABLE CUSTOMER (
    Customer_ID    NUMBER(6) PRIMARY KEY,
    Customer_Name  VARCHAR(50) NOT NULL,
    Phone_No       VARCHAR(15),
    Email          VARCHAR(80) UNIQUE,
    Member_Tier    VARCHAR(20),
    Gender         VARCHAR(1)
        CHECK (Gender IN ('M','F'))
);

-- 4.2 Table Branch
CREATE TABLE BRANCH (
    Branch_ID       NUMBER(4) PRIMARY KEY,
    Branch_Name     VARCHAR(50) NOT NULL,
    Branch_Location VARCHAR(100),
    Contact_No      VARCHAR(15),
    Manager_Name    VARCHAR(50)
);

-- 4.3 Table Product
CREATE TABLE PRODUCT (
    Product_ID   NUMBER(5) PRIMARY KEY,
    Product_Name VARCHAR(50) NOT NULL,
    Category     VARCHAR(20)
        CHECK (Category IN ('Coffee','Tea','Pastries')),
    Unit_Price   NUMBER(6,2) NOT NULL
        CHECK (Unit_Price >= 0),
    Status       VARCHAR(15) DEFAULT 'Available'
        CHECK (Status IN ('Available','Not Available'))
);

-- 4.4 Table Orders
CREATE TABLE ORDERS (
    Order_ID    NUMBER(8) PRIMARY KEY,
    Order_Date  DATE NOT NULL,
    Order_Time  VARCHAR2(8),
    Total_Amt   NUMBER(6,2) NOT NULL
        CHECK (Total_Amt >= 0),
    Pay_Method  VARCHAR2(10) NOT NULL
        CHECK (Pay_Method IN ('Cash','Card','E-Wallet')),
    Pay_Status  VARCHAR2(10) NOT NULL
        CHECK (Pay_Status IN ('Paid','Pending','Cancelled')),
    Customer_ID NUMBER(6) NOT NULL,
    Branch_ID   NUMBER(4) NOT NULL,
    CONSTRAINT fk_order_customer
        FOREIGN KEY (Customer_ID)
        REFERENCES CUSTOMER(Customer_ID),
    CONSTRAINT fk_order_branch
        FOREIGN KEY (Branch_ID)
        REFERENCES BRANCH(Branch_ID)
);

-- 4.5 Table Order_Item
-- NOTE: original document wrote "ProductID" (no underscore) in the FK,
-- which does not match the actual column name "Product_ID" and would
-- raise ORA-00904 (invalid identifier). Fixed below.
CREATE TABLE ORDER_ITEM (
    Product_ID NUMBER(5) NOT NULL,
    Order_ID   NUMBER(8) NOT NULL,
    Quantity   NUMBER(4) NOT NULL
        CHECK (Quantity > 0),
    CONSTRAINT pk_order_item
        PRIMARY KEY (Product_ID, Order_ID),
    CONSTRAINT fk_oi_product
        FOREIGN KEY (Product_ID)
        REFERENCES PRODUCT(Product_ID),
    CONSTRAINT fk_oi_order
        FOREIGN KEY (Order_ID)
        REFERENCES ORDERS(Order_ID)
);

-- ============================================================
-- Task 5: Insert records
-- Order matters: CUSTOMER & BRANCH & PRODUCT first (no dependencies),
-- then ORDERS (depends on CUSTOMER, BRANCH),
-- then ORDER_ITEM (depends on PRODUCT, ORDERS).
-- ============================================================
-- ============================================================
-- 5.1 Record of CUSTOMER (47 rows)
-- ============================================================
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100001,'Aina Rahman','0123456789','aina.rahman@gmail.com','Silver','F');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100002,'Daniel Lee','0112345678','daniel.lee@gmail.com','Gold','M');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100003,'Nur Aisyah','0198765432','nur.aisyah@gmail.com','Regular','F');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100004,'Amir Hakim','0187654321','amir.hakim@gmail.com','Silver','M');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100005,'Siti Khadijah','0176543210','siti.khadijah@gmail.com','Gold','F');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100006,'Muhammad Irfan','0165432109','m.irfan@gmail.com','Regular','M');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100007,'Nur Izzati','0149876543','nur.izzati@gmail.com','Silver','F');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100008,'Adam Wong','0138765432','adam.wong@gmail.com','Platinum','M');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100009,'Aisyah Sofea','0129876543','aisyah.sofea@gmail.com','Gold','F');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100010,'Jason Lim','0118765432','jason.lim@gmail.com','Regular','M');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100011,'Farah Nabila','0192345678','farah.nabila@gmail.com','Silver','F');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100012,'Hafiz Zainal','0182345678','hafiz.zainal@gmail.com','Gold','M');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100013,'Nur Shafiqa','0172345678','nur.shafiqa@gmail.com','Regular','F');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100014,'Aiman Syafiq','0162345678','aiman.syafiq@gmail.com','Silver','M');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100015,'Nabila Huda','0142345678','nabila.huda@gmail.com','Gold','F');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100016,'Bryan Tan','0132345678','bryan.tan@gmail.com','Platinum','M');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100017,'Sofia Aina','0122345678','sofia.aina@gmail.com','Regular','F');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100018,'Irfan Zulkifli','0113345678','irfan.z@gmail.com','Silver','M');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100019,'Nur Amirah','0193345678','nur.amirah@gmail.com','Gold','F');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100020,'Kevin Chong','0183345678','kevin.chong@gmail.com','Regular','M');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100021,'Haziq Fikri','0173456789','haziq.fikri@gmail.com','Silver','M');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100022,'Nur Alya','0128765432','nur.alya@gmail.com','Regular','F');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100023,'Syafiq Roslan','0191122334','syafiq.roslan@gmail.com','Gold','M');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100024,'Izzah Farhana','0189988776','izzah.farhana@gmail.com','Silver','F');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100025,'Wan Aqil','0165566778','wan.aqil@gmail.com','Regular','M');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100026,'Nur Aina Syuhada','0134455667','aina.syuhada@gmail.com','Gold','F');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100027,'Hakimi Azlan','0112233445','hakimi.azlan@gmail.com','Silver','M');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100028,'Yong Wei Jian','0123344556','yong.weijian@gmail.com','Platinum','M');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100029,'Tan Jia En','0177788990','tan.jiaen@gmail.com','Regular','F');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100030,'Lim Zi Xuan','0198877665','lim.zixuan@gmail.com','Gold','F');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100031,'Nurul Iman','0181239876','nurul.iman@gmail.com','Silver','F');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100032,'Ariff Haziq','0169988123','ariff.haziq@gmail.com','Regular','M');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100033,'Siti Nur Aina','0129988112','siti.nuraina@gmail.com','Gold','F');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100034,'Firdaus Ismail','0118899001','firdaus.ismail@gmail.com','Silver','M');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100035,'Puteri Amani','0136677889','puteri.amani@gmail.com','Regular','F');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100036,'Chong Wei Ming','0172233445','chong.weiming@gmail.com','Gold','M');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100037,'Teoh Xin Yi','0193344556','teoh.xinyi@gmail.com','Silver','F');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100038,'Nur Maisarah','0184455667','nur.maisarah@gmail.com','Platinum','F');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100039,'Aiman Hakim','0162233445','aiman.hakim@gmail.com','Regular','M');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100040,'Soo Jun Kai','0126677889','soo.junkai@gmail.com','Gold','M');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100041,'Nur Sabrina','0114567890','nur.sabrina@gmail.com','Silver','F');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100042,'Haziq Danial','0195566770','haziq.danial@gmail.com','Regular','M');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100043,'Alya Batrisyia','0187788991','alya.batrisyia@gmail.com','Gold','F');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100044,'Kumar Rajesh','0164455778','kumar.rajesh@gmail.com','Silver','M');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100045,'Shanthi Devi','0121112233','shanthi.devi@gmail.com','Regular','F');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100046,'Prakash Anand','0171112233','prakash.anand@gmail.com','Gold','M');
INSERT INTO CUSTOMER (Customer_ID, Customer_Name, Phone_No, Email, Member_Tier, Gender) VALUES (100047,'Nur Hana','0192233110','nur.hana@gmail.com','Silver','F');

-- ============================================================
-- 5.2 Record of BRANCH (20 rows)
-- ============================================================
INSERT INTO BRANCH (Branch_ID, Branch_Name, Branch_Location, Contact_No, Manager_Name) VALUES (1001,'ZUS Coffee Mid Valley','Mid Valley Megamall, Kuala Lumpur','03-22011234','Nur Aisyah');
INSERT INTO BRANCH (Branch_ID, Branch_Name, Branch_Location, Contact_No, Manager_Name) VALUES (1002,'ZUS Coffee Sunway Pyramid','Sunway Pyramid, Selangor','03-56122345','Daniel Lee');
INSERT INTO BRANCH (Branch_ID, Branch_Name, Branch_Location, Contact_No, Manager_Name) VALUES (1003,'ZUS Coffee Pavilion KL','Pavilion Kuala Lumpur, Kuala Lumpur','03-21106789','Siti Khadijah');
INSERT INTO BRANCH (Branch_ID, Branch_Name, Branch_Location, Contact_No, Manager_Name) VALUES (1004,'ZUS Coffee IOI City Mall','IOI City Mall, Putrajaya','03-83288900','Amir Hakim');
INSERT INTO BRANCH (Branch_ID, Branch_Name, Branch_Location, Contact_No, Manager_Name) VALUES (1005,'ZUS Coffee Queensbay','Queensbay Mall, Penang','04-6112233','Tan Jia En');
INSERT INTO BRANCH (Branch_ID, Branch_Name, Branch_Location, Contact_No, Manager_Name) VALUES (1006,'ZUS Coffee Gurney Plaza','Gurney Plaza, Penang','04-2297788','Lim Zi Xuan');
INSERT INTO BRANCH (Branch_ID, Branch_Name, Branch_Location, Contact_No, Manager_Name) VALUES (1007,'ZUS Coffee AEON Tebrau','AEON Tebrau City, Johor Bahru','07-3584455','Hafiz Zainal');
INSERT INTO BRANCH (Branch_ID, Branch_Name, Branch_Location, Contact_No, Manager_Name) VALUES (1008,'ZUS Coffee Paradigm JB','Paradigm Mall JB, Johor Bahru','07-5603322','Nur Izzati');
INSERT INTO BRANCH (Branch_ID, Branch_Name, Branch_Location, Contact_No, Manager_Name) VALUES (1009,'ZUS Coffee i-City','i-City, Shah Alam, Selangor','03-55219988','Bryan Tan');
INSERT INTO BRANCH (Branch_ID, Branch_Name, Branch_Location, Contact_No, Manager_Name) VALUES (1010,'ZUS Coffee KL Sentral','KL Sentral, Kuala Lumpur','03-22762211','Aina Rahman');
INSERT INTO BRANCH (Branch_ID, Branch_Name, Branch_Location, Contact_No, Manager_Name) VALUES (1011,'ZUS Coffee Setia City Mall','Setia City Mall, Shah Alam, Selangor','03-33621100','Aiman Syafiq');
INSERT INTO BRANCH (Branch_ID, Branch_Name, Branch_Location, Contact_No, Manager_Name) VALUES (1012,'ZUS Coffee MyTown','MyTown Shopping Centre, Kuala Lumpur','03-27132288','Nur Sabrina');
INSERT INTO BRANCH (Branch_ID, Branch_Name, Branch_Location, Contact_No, Manager_Name) VALUES (1013,'ZUS Coffee NU Sentral','NU Sentral, Kuala Lumpur','03-22748899','Haziq Danial');
INSERT INTO BRANCH (Branch_ID, Branch_Name, Branch_Location, Contact_No, Manager_Name) VALUES (1014,'ZUS Coffee One Utama','1 Utama Shopping Centre, Petaling Jaya','03-76213344','Alya Batrisyia');
INSERT INTO BRANCH (Branch_ID, Branch_Name, Branch_Location, Contact_No, Manager_Name) VALUES (1015,'ZUS Coffee Subang Parade','Subang Parade, Subang Jaya','03-56115566','Firdaus Ismail');
INSERT INTO BRANCH (Branch_ID, Branch_Name, Branch_Location, Contact_No, Manager_Name) VALUES (1016,'ZUS Coffee Central i-City','Central i-City, Shah Alam','03-55226677','Nur Maisarah');
INSERT INTO BRANCH (Branch_ID, Branch_Name, Branch_Location, Contact_No, Manager_Name) VALUES (1017,'ZUS Coffee AEON Bukit Tinggi','AEON Bukit Tinggi, Klang','03-33224455','Azhar Firman');
INSERT INTO BRANCH (Branch_ID, Branch_Name, Branch_Location, Contact_No, Manager_Name) VALUES (1018,'ZUS Coffee Mid Valley Southkey','Mid Valley Southkey, Johor Bahru','07-2667788','Harith Imran');
INSERT INTO BRANCH (Branch_ID, Branch_Name, Branch_Location, Contact_No, Manager_Name) VALUES (1019,'ZUS Coffee The Spring','The Spring Shopping Mall, Kuching','082-556677','Nur Adibah');
INSERT INTO BRANCH (Branch_ID, Branch_Name, Branch_Location, Contact_No, Manager_Name) VALUES (1020,'ZUS Coffee Suria Sabah','Suria Sabah, Kota Kinabalu','088-334455','Muhammad Danish');

-- ============================================================
-- 5.3 Record of PRODUCT (40 rows)
-- ============================================================
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20001,'Americano (Hot)','Coffee',7.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20002,'Americano (Iced)','Coffee',8.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20003,'Cafe Latte (Hot)','Coffee',9.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20004,'Cafe Latte (Iced)','Coffee',10.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20005,'Cappuccino (Hot)','Coffee',9.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20006,'Cappuccino (Iced)','Coffee',10.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20007,'Mocha (Hot)','Coffee',11.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20008,'Mocha (Iced)','Coffee',12.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20009,'Caramel Macchiato (Hot)','Coffee',12.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20010,'Caramel Macchiato (Iced)','Coffee',13.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20011,'Spanish Latte (Hot)','Coffee',12.50,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20012,'Spanish Latte (Iced)','Coffee',13.50,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20013,'Flat White (Hot)','Coffee',10.50,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20014,'Flat White (Iced)','Coffee',11.50,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20015,'Vanilla Latte (Hot)','Coffee',11.50,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20016,'Vanilla Latte (Iced)','Coffee',12.50,'Not Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20017,'Matcha Latte (Hot)','Tea',11.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20018,'Matcha Latte (Iced)','Tea',12.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20019,'Jasmine Green Tea (Hot)','Tea',6.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20020,'Jasmine Green Tea (Iced)','Tea',7.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20021,'Earl Grey Tea (Hot)','Tea',6.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20022,'Earl Grey Tea (Iced)','Tea',7.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20023,'Peach Tea (Iced)','Tea',8.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20024,'Lemon Tea (Iced)','Tea',7.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20025,'Honey Lemon Tea (Hot)','Tea',8.50,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20026,'Milk Tea (Iced)','Tea',9.50,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20027,'Brown Sugar Milk Tea','Tea',10.50,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20028,'Chamomile Tea (Hot)','Tea',7.50,'Not Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20029,'Croissant Butter','Pastries',6.50,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20030,'Chocolate Croissant','Pastries',7.50,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20031,'Almond Croissant','Pastries',8.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20032,'Cinnamon Roll','Pastries',8.50,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20033,'Blueberry Muffin','Pastries',7.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20034,'Chocolate Muffin','Pastries',7.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20035,'Banana Bread Slice','Pastries',6.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20036,'Cheese Cake Slice','Pastries',10.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20037,'Tiramisu Cup','Pastries',11.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20038,'Red Velvet Slice','Pastries',10.90,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20039,'Portuguese Egg Tart','Pastries',5.50,'Available');
INSERT INTO PRODUCT (Product_ID, Product_Name, Category, Unit_Price, Status) VALUES (20040,'Chicken Puff','Pastries',5.90,'Not Available');

-- ============================================================
-- 5.5 Record of ORDERS (48 rows: batch 50000001-020, batch 52000001-028)
-- ============================================================
INSERT INTO ORDERS VALUES (50000001, TO_DATE('01-SEP-25','DD-MON-YY'), '09:10', 18.90, 'Card', 'Paid', 100001, 1001);
INSERT INTO ORDERS VALUES (50000002, TO_DATE('01-SEP-25','DD-MON-YY'), '09:25', 12.50, 'Cash', 'Paid', 100002, 1002);
INSERT INTO ORDERS VALUES (50000003, TO_DATE('01-SEP-25','DD-MON-YY'), '10:05', 25.70, 'E-Wallet', 'Pending', 100003, 1003);
INSERT INTO ORDERS VALUES (50000004, TO_DATE('01-SEP-25','DD-MON-YY'), '10:40', 9.90, 'Card', 'Paid', 100004, 1004);
INSERT INTO ORDERS VALUES (50000005, TO_DATE('01-SEP-25','DD-MON-YY'), '11:15', 33.80, 'E-Wallet', 'Paid', 100005, 1005);
INSERT INTO ORDERS VALUES (50000006, TO_DATE('02-SEP-25','DD-MON-YY'), '09:05', 16.80, 'Cash', 'Paid', 100006, 1006);
INSERT INTO ORDERS VALUES (50000007, TO_DATE('02-SEP-25','DD-MON-YY'), '09:50', 22.40, 'Card', 'Paid', 100007, 1007);
INSERT INTO ORDERS VALUES (50000008, TO_DATE('02-SEP-25','DD-MON-YY'), '10:20', 8.90, 'E-Wallet', 'Paid', 100008, 1008);
INSERT INTO ORDERS VALUES (50000009, TO_DATE('02-SEP-25','DD-MON-YY'), '11:05', 19.90, 'Card', 'Pending', 100009, 1009);
INSERT INTO ORDERS VALUES (50000010, TO_DATE('02-SEP-25','DD-MON-YY'), '11:40', 27.50, 'Cash', 'Paid', 100010, 1010);
INSERT INTO ORDERS VALUES (50000011, TO_DATE('03-SEP-25','DD-MON-YY'), '09:30', 14.90, 'E-Wallet', 'Paid', 100011, 1011);
INSERT INTO ORDERS VALUES (50000012, TO_DATE('03-SEP-25','DD-MON-YY'), '10:10', 21.80, 'Card', 'Paid', 100012, 1012);
INSERT INTO ORDERS VALUES (50000013, TO_DATE('03-SEP-25','DD-MON-YY'), '10:55', 11.90, 'Cash', 'Paid', 100013, 1013);
INSERT INTO ORDERS VALUES (50000014, TO_DATE('03-SEP-25','DD-MON-YY'), '11:25', 29.60, 'E-Wallet', 'Pending', 100014, 1014);
INSERT INTO ORDERS VALUES (50000015, TO_DATE('03-SEP-25','DD-MON-YY'), '12:05', 17.40, 'Card', 'Paid', 100015, 1015);
INSERT INTO ORDERS VALUES (50000016, TO_DATE('04-SEP-25','DD-MON-YY'), '09:15', 24.30, 'Cash', 'Paid', 100016, 1016);
INSERT INTO ORDERS VALUES (50000017, TO_DATE('04-SEP-25','DD-MON-YY'), '10:00', 13.50, 'E-Wallet', 'Paid', 100017, 1017);
INSERT INTO ORDERS VALUES (50000018, TO_DATE('04-SEP-25','DD-MON-YY'), '10:35', 31.20, 'Card', 'Paid', 100018, 1018);
INSERT INTO ORDERS VALUES (50000019, TO_DATE('04-SEP-25','DD-MON-YY'), '11:10', 9.50, 'Cash', 'Cancelled', 100019, 1019);
INSERT INTO ORDERS VALUES (50000020, TO_DATE('04-SEP-25','DD-MON-YY'), '11:55', 20.90, 'E-Wallet', 'Paid', 100020, 1020);
INSERT INTO ORDERS VALUES (52000001, TO_DATE('01-SEP-25','DD-MON-YY'), '09:05', 12.90, 'Card', 'Paid', 100001, 1001);
INSERT INTO ORDERS VALUES (52000002, TO_DATE('01-SEP-25','DD-MON-YY'), '09:15', 18.90, 'Cash', 'Paid', 100002, 1002);
INSERT INTO ORDERS VALUES (52000003, TO_DATE('01-SEP-25','DD-MON-YY'), '09:25', 9.90, 'E-Wallet', 'Paid', 100003, 1003);
INSERT INTO ORDERS VALUES (52000004, TO_DATE('01-SEP-25','DD-MON-YY'), '09:40', 25.90, 'Card', 'Pending', 100004, 1004);
INSERT INTO ORDERS VALUES (52000005, TO_DATE('01-SEP-25','DD-MON-YY'), '09:55', 14.90, 'Cash', 'Paid', 100005, 1005);
INSERT INTO ORDERS VALUES (52000006, TO_DATE('02-SEP-25','DD-MON-YY'), '10:05', 16.90, 'Cash', 'Paid', 100006, 1006);
INSERT INTO ORDERS VALUES (52000007, TO_DATE('02-SEP-25','DD-MON-YY'), '10:20', 22.90, 'Card', 'Paid', 100007, 1007);
INSERT INTO ORDERS VALUES (52000008, TO_DATE('02-SEP-25','DD-MON-YY'), '10:35', 8.90, 'E-Wallet', 'Paid', 100008, 1008);
INSERT INTO ORDERS VALUES (52000009, TO_DATE('02-SEP-25','DD-MON-YY'), '10:50', 19.90, 'Cash', 'Pending', 100009, 1009);
INSERT INTO ORDERS VALUES (52000010, TO_DATE('02-SEP-25','DD-MON-YY'), '11:05', 27.90, 'Card', 'Paid', 100010, 1010);
INSERT INTO ORDERS VALUES (52000011, TO_DATE('03-SEP-25','DD-MON-YY'), '09:30', 14.90, 'E-Wallet', 'Paid', 100011, 1011);
INSERT INTO ORDERS VALUES (52000012, TO_DATE('03-SEP-25','DD-MON-YY'), '09:50', 21.80, 'Card', 'Paid', 100012, 1012);
INSERT INTO ORDERS VALUES (52000013, TO_DATE('03-SEP-25','DD-MON-YY'), '10:10', 11.90, 'Cash', 'Paid', 100013, 1013);
INSERT INTO ORDERS VALUES (52000014, TO_DATE('03-SEP-25','DD-MON-YY'), '10:30', 29.60, 'E-Wallet', 'Pending', 100014, 1014);
INSERT INTO ORDERS VALUES (52000015, TO_DATE('03-SEP-25','DD-MON-YY'), '10:55', 17.40, 'Card', 'Paid', 100015, 1015);
INSERT INTO ORDERS VALUES (52000016, TO_DATE('04-SEP-25','DD-MON-YY'), '09:15', 24.30, 'Cash', 'Paid', 100016, 1016);
INSERT INTO ORDERS VALUES (52000017, TO_DATE('04-SEP-25','DD-MON-YY'), '09:40', 13.50, 'E-Wallet', 'Paid', 100017, 1017);
INSERT INTO ORDERS VALUES (52000018, TO_DATE('04-SEP-25','DD-MON-YY'), '10:00', 31.20, 'Card', 'Paid', 100018, 1018);
INSERT INTO ORDERS VALUES (52000019, TO_DATE('04-SEP-25','DD-MON-YY'), '10:35', 9.50, 'Cash', 'Cancelled', 100019, 1019);
INSERT INTO ORDERS VALUES (52000020, TO_DATE('04-SEP-25','DD-MON-YY'), '10:55', 20.90, 'E-Wallet', 'Paid', 100020, 1020);
INSERT INTO ORDERS VALUES (52000021, TO_DATE('05-SEP-25','DD-MON-YY'), '09:05', 18.90, 'Card', 'Paid', 100021, 1001);
INSERT INTO ORDERS VALUES (52000022, TO_DATE('05-SEP-25','DD-MON-YY'), '09:25', 12.50, 'Cash', 'Paid', 100022, 1002);
INSERT INTO ORDERS VALUES (52000023, TO_DATE('05-SEP-25','DD-MON-YY'), '09:45', 25.70, 'E-Wallet', 'Pending', 100023, 1003);
INSERT INTO ORDERS VALUES (52000024, TO_DATE('05-SEP-25','DD-MON-YY'), '10:05', 9.90, 'Card', 'Paid', 100024, 1004);
INSERT INTO ORDERS VALUES (52000025, TO_DATE('05-SEP-25','DD-MON-YY'), '10:25', 33.80, 'Cash', 'Paid', 100025, 1005);
INSERT INTO ORDERS VALUES (52000026, TO_DATE('06-SEP-25','DD-MON-YY'), '09:10', 16.80, 'E-Wallet', 'Paid', 100026, 1006);
INSERT INTO ORDERS VALUES (52000027, TO_DATE('06-SEP-25','DD-MON-YY'), '09:35', 22.40, 'Card', 'Paid', 100027, 1007);
INSERT INTO ORDERS VALUES (52000028, TO_DATE('06-SEP-25','DD-MON-YY'), '10:00', 8.90, 'Cash', 'Paid', 100028, 1008);

-- ============================================================
-- 5.4 Record of ORDER_ITEM (48 rows)
-- ============================================================
INSERT INTO ORDER_ITEM VALUES (20001, 52000001, 1);
INSERT INTO ORDER_ITEM VALUES (20005, 52000001, 2);
INSERT INTO ORDER_ITEM VALUES (20009, 52000001, 1);
INSERT INTO ORDER_ITEM VALUES (20002, 52000002, 1);
INSERT INTO ORDER_ITEM VALUES (20006, 52000002, 2);
INSERT INTO ORDER_ITEM VALUES (20010, 52000002, 1);
INSERT INTO ORDER_ITEM VALUES (20003, 52000003, 1);
INSERT INTO ORDER_ITEM VALUES (20007, 52000003, 2);
INSERT INTO ORDER_ITEM VALUES (20011, 52000003, 1);
INSERT INTO ORDER_ITEM VALUES (20004, 52000004, 1);
INSERT INTO ORDER_ITEM VALUES (20008, 52000004, 2);
INSERT INTO ORDER_ITEM VALUES (20012, 52000004, 1);
INSERT INTO ORDER_ITEM VALUES (20005, 52000005, 1);
INSERT INTO ORDER_ITEM VALUES (20009, 52000005, 2);
INSERT INTO ORDER_ITEM VALUES (20013, 52000005, 1);
INSERT INTO ORDER_ITEM VALUES (20006, 52000006, 1);
INSERT INTO ORDER_ITEM VALUES (20010, 52000006, 2);
INSERT INTO ORDER_ITEM VALUES (20014, 52000006, 1);
INSERT INTO ORDER_ITEM VALUES (20007, 52000007, 1);
INSERT INTO ORDER_ITEM VALUES (20011, 52000007, 2);
INSERT INTO ORDER_ITEM VALUES (20015, 52000007, 1);
INSERT INTO ORDER_ITEM VALUES (20008, 52000008, 1);
INSERT INTO ORDER_ITEM VALUES (20012, 52000008, 2);
INSERT INTO ORDER_ITEM VALUES (20016, 52000008, 1);
INSERT INTO ORDER_ITEM VALUES (20009, 52000009, 1);
INSERT INTO ORDER_ITEM VALUES (20013, 52000009, 2);
INSERT INTO ORDER_ITEM VALUES (20017, 52000009, 1);
INSERT INTO ORDER_ITEM VALUES (20010, 52000010, 1);
INSERT INTO ORDER_ITEM VALUES (20014, 52000010, 2);
INSERT INTO ORDER_ITEM VALUES (20018, 52000010, 1);
INSERT INTO ORDER_ITEM VALUES (20011, 52000011, 1);
INSERT INTO ORDER_ITEM VALUES (20015, 52000011, 2);
INSERT INTO ORDER_ITEM VALUES (20019, 52000011, 1);
INSERT INTO ORDER_ITEM VALUES (20012, 52000012, 1);
INSERT INTO ORDER_ITEM VALUES (20016, 52000012, 2);
INSERT INTO ORDER_ITEM VALUES (20020, 52000012, 1);
INSERT INTO ORDER_ITEM VALUES (20013, 52000013, 1);
INSERT INTO ORDER_ITEM VALUES (20017, 52000013, 2);
INSERT INTO ORDER_ITEM VALUES (20021, 52000013, 1);
INSERT INTO ORDER_ITEM VALUES (20014, 52000014, 1);
INSERT INTO ORDER_ITEM VALUES (20018, 52000014, 2);
INSERT INTO ORDER_ITEM VALUES (20022, 52000014, 1);
INSERT INTO ORDER_ITEM VALUES (20015, 52000015, 1);
INSERT INTO ORDER_ITEM VALUES (20019, 52000015, 2);
INSERT INTO ORDER_ITEM VALUES (20023, 52000015, 1);
INSERT INTO ORDER_ITEM VALUES (20016, 52000016, 1);
INSERT INTO ORDER_ITEM VALUES (20020, 52000016, 2);
INSERT INTO ORDER_ITEM VALUES (20024, 52000016, 1);
-- ============================================================
-- Task 6: Reports (SQL*Plus scripts)
-- ============================================================

-- ----------------------------------------------------------
-- 6.1 Branch Sales Performance Analysis
-- ----------------------------------------------------------
SET VERIFY OFF
SET LINESIZE 2000
SET PAGESIZE 2000
SET TRIMSPOOL ON
PROMPT =========================================
PROMPT Q1: Branch Sales Performance Report
PROMPT Date format example: 01-SEP-25
PROMPT Payment example: Cash / Card / E-Wallet / ALL
PROMPT =========================================
ACCEPT v_start_date CHAR PROMPT 'Enter start date (DD-MON-YY) : '
ACCEPT v_end_date CHAR PROMPT 'Enter end date (DD-MON-YY) : '
ACCEPT v_pay_method CHAR PROMPT 'Payment method (Cash/Card/E-Wallet/ALL): '

COLUMN branch_id FORMAT 9999 HEADING 'Branch ID'
COLUMN branch_name FORMAT A22 TRUNCATE HEADING 'Branch Name'
COLUMN orders_cnt FORMAT 999,999 HEADING 'Orders'
COLUMN items_sold FORMAT 999,999 HEADING 'Items Sold'
COLUMN revenue FORMAT 999,999.99 HEADING 'Revenue(RM)'
COLUMN avg_order FORMAT 999,999.99 HEADING 'Avg Order(RM)'

TTITLE LEFT 'ZUS Coffee - Branch Sales Performance Report' SKIP 1 -
       LEFT 'Period: &v_start_date to &v_end_date  Payment: &v_pay_method' SKIP 1

BREAK ON REPORT
COMPUTE SUM LABEL 'TOTAL' OF orders_cnt items_sold revenue ON REPORT
COMPUTE AVG LABEL 'OVERALL AVG' OF avg_order ON REPORT

SELECT
    b.branch_id,
    b.branch_name,
    COUNT(DISTINCT o.order_id) AS orders_cnt,
    SUM(oi.quantity) AS items_sold,
    SUM(oi.quantity * p.unit_price) AS revenue,
    ROUND(
        SUM(oi.quantity * p.unit_price)
        / NULLIF(COUNT(DISTINCT o.order_id),0), 2
    ) AS avg_order
FROM branch b
JOIN orders o ON o.branch_id = b.branch_id
JOIN order_item oi ON oi.order_id = o.order_id
JOIN product p ON p.product_id = oi.product_id
WHERE o.order_date BETWEEN TO_DATE('&v_start_date','DD-MON-YY')
                        AND TO_DATE('&v_end_date','DD-MON-YY')
  AND (UPPER('&v_pay_method')='ALL'
       OR UPPER(o.pay_method)=UPPER('&v_pay_method'))
GROUP BY b.branch_id, b.branch_name
ORDER BY revenue DESC;

TTITLE OFF
CLEAR BREAKS
CLEAR COMPUTES


-- ----------------------------------------------------------
-- 6.2 Top-Selling Products by Revenue
-- ----------------------------------------------------------
SET VERIFY OFF
SET LINESIZE 2000
SET PAGESIZE 2000
SET TRIMSPOOL ON
PROMPT =========================================
PROMPT Q2: Top N Products by Revenue Report
PROMPT =========================================
ACCEPT v_start_date CHAR PROMPT 'Enter start date (DD-MON-YY) : '
ACCEPT v_end_date CHAR PROMPT 'Enter end date (DD-MON-YY) : '
ACCEPT v_branch_id CHAR PROMPT 'Enter Branch_ID (e.g. 1001) or ALL: '
ACCEPT v_top_n CHAR PROMPT 'Show Top N products (e.g. 10): '

COLUMN product_id FORMAT 99999 HEADING 'Product ID'
COLUMN product_name FORMAT A28 TRUNCATE HEADING 'Product Name'
COLUMN category FORMAT A12 TRUNCATE HEADING 'Category'
COLUMN qty_sold FORMAT 999,999 HEADING 'Qty Sold'
COLUMN revenue FORMAT 999,999.99 HEADING 'Revenue(RM)'

TTITLE LEFT 'ZUS Coffee - Top Products by Revenue' SKIP 1 -
       LEFT 'Period: &v_start_date to &v_end_date  Branch: &v_branch_id  Top: &v_top_n' SKIP 1

BREAK ON REPORT
COMPUTE SUM LABEL 'TOTAL' OF qty_sold revenue ON REPORT

SELECT *
FROM (
    SELECT
        p.product_id,
        p.product_name,
        p.category,
        SUM(oi.quantity) AS qty_sold,
        SUM(oi.quantity * p.unit_price) AS revenue
    FROM product p
    JOIN order_item oi ON oi.product_id = p.product_id
    JOIN orders o ON o.order_id = oi.order_id
    WHERE o.order_date BETWEEN TO_DATE('&v_start_date','DD-MON-YY')
                            AND TO_DATE('&v_end_date','DD-MON-YY')
      AND (UPPER('&v_branch_id')='ALL'
           OR TO_CHAR(o.branch_id)='&v_branch_id')
    GROUP BY p.product_id, p.product_name, p.category
    ORDER BY revenue DESC
)
WHERE ROWNUM <= TO_NUMBER('&v_top_n');

TTITLE OFF
CLEAR BREAKS
CLEAR COMPUTES


-- ----------------------------------------------------------
-- 6.3 Sales Contribution by Member Tier
-- ----------------------------------------------------------
SET VERIFY OFF
SET LINESIZE 2000
SET PAGESIZE 2000
SET TRIMSPOOL ON
PROMPT =========================================
PROMPT Q3: Member Tier Contribution Report
PROMPT =========================================
ACCEPT v_start_date CHAR PROMPT 'Enter start date (DD-MON-YY) : '
ACCEPT v_end_date CHAR PROMPT 'Enter end date (DD-MON-YY) : '

COLUMN member_tier FORMAT A12 TRUNCATE HEADING 'Member Tier'
COLUMN cust_cnt FORMAT 999,999 HEADING 'Customers'
COLUMN orders_cnt FORMAT 999,999 HEADING 'Orders'
COLUMN items_sold FORMAT 999,999 HEADING 'Items Sold'
COLUMN revenue FORMAT 999,999.99 HEADING 'Revenue(RM)'
COLUMN avg_order FORMAT 999,999.99 HEADING 'Avg Order(RM)'

TTITLE LEFT 'ZUS Coffee - Member Tier Contribution Report' SKIP 1 -
       LEFT 'Period: &v_start_date to &v_end_date' SKIP 1

BREAK ON REPORT
COMPUTE SUM LABEL 'TOTAL' OF cust_cnt orders_cnt items_sold revenue ON REPORT
COMPUTE AVG LABEL 'OVERALL AVG' OF avg_order ON REPORT

SELECT
    c.member_tier,
    COUNT(DISTINCT c.customer_id) AS cust_cnt,
    COUNT(DISTINCT o.order_id) AS orders_cnt,
    SUM(oi.quantity) AS items_sold,
    SUM(oi.quantity * p.unit_price) AS revenue,
    ROUND(
        SUM(oi.quantity * p.unit_price)
        / NULLIF(COUNT(DISTINCT o.order_id),0), 2
    ) AS avg_order
FROM customer c
JOIN orders o ON o.customer_id = c.customer_id
JOIN order_item oi ON oi.order_id = o.order_id
JOIN product p ON p.product_id = oi.product_id
WHERE o.order_date BETWEEN TO_DATE('&v_start_date','DD-MON-YY')
                        AND TO_DATE('&v_end_date','DD-MON-YY')
GROUP BY c.member_tier
ORDER BY revenue DESC;

TTITLE OFF
CLEAR BREAKS
CLEAR COMPUTES


-- ----------------------------------------------------------
-- 6.4 Payment Status Monitoring by Branch
-- ----------------------------------------------------------
SET VERIFY OFF
SET LINESIZE 200
SET PAGESIZE 100
SET TRIMSPOOL ON
SET COLSEP ' '
ACCEPT v_start_date CHAR PROMPT 'Enter start date (DD-MON-YY) : '
ACCEPT v_end_date CHAR PROMPT 'Enter end date (DD-MON-YY) : '

COLUMN branch_id FORMAT 9999 HEADING 'Branch ID'
COLUMN branch_name FORMAT A30 TRUNCATE HEADING 'Branch Name'
COLUMN pay_status FORMAT A10 TRUNCATE HEADING 'Pay Status'
COLUMN orders_cnt FORMAT 999,999 HEADING 'Orders'
COLUMN revenue FORMAT 999,999.99 HEADING 'Revenue(RM)'

TTITLE LEFT 'ZUS Coffee - Payment Status Monitoring by Branch' SKIP 1 -
       LEFT 'Period: &v_start_date to &v_end_date' SKIP 1

BREAK ON branch_name SKIP 1 ON REPORT
COMPUTE SUM LABEL 'BRANCH TOTAL' OF orders_cnt revenue ON branch_name
COMPUTE SUM LABEL 'GRAND TOTAL' OF orders_cnt revenue ON REPORT

SELECT
    b.branch_id,
    b.branch_name,
    o.pay_status,
    COUNT(DISTINCT o.order_id) AS orders_cnt,
    SUM(oi.quantity * p.unit_price) AS revenue
FROM branch b
JOIN orders o ON o.branch_id = b.branch_id
JOIN order_item oi ON oi.order_id = o.order_id
JOIN product p ON p.product_id = oi.product_id
WHERE o.order_date BETWEEN TO_DATE('&v_start_date','DD-MON-YY')
                        AND TO_DATE('&v_end_date','DD-MON-YY')
GROUP BY b.branch_id, b.branch_name, o.pay_status
ORDER BY b.branch_id, o.pay_status;

TTITLE OFF
CLEAR BREAKS
CLEAR COMPUTES


-- ----------------------------------------------------------
-- 6.5 Peak Hour Sales Analysis
-- ----------------------------------------------------------
SET VERIFY OFF
SET LINESIZE 2000
SET PAGESIZE 2000
SET TRIMSPOOL ON
PROMPT =========================================
PROMPT Q5: Peak Hour Sales Report
PROMPT Date format example: 01-SEP-25
PROMPT Branch example: 1001 or ALL
PROMPT =========================================
ACCEPT v_start_date CHAR PROMPT 'Enter start date (DD-MON-YY) : '
ACCEPT v_end_date CHAR PROMPT 'Enter end date (DD-MON-YY) : '
ACCEPT v_branch_id CHAR PROMPT 'Enter Branch_ID (e.g. 1001) or ALL: '

COLUMN branch_id FORMAT 9999 HEADING 'Branch ID'
COLUMN branch_name FORMAT A22 TRUNCATE HEADING 'Branch Name'
COLUMN hour_slot FORMAT A5 HEADING 'Hour'
COLUMN orders_cnt FORMAT 999,999 HEADING 'Orders'
COLUMN revenue FORMAT 999,999.99 HEADING 'Revenue(RM)'

TTITLE LEFT 'ZUS Coffee - Peak Hour Sales Report' SKIP 1 -
       LEFT 'Period: &v_start_date to &v_end_date  Branch: &v_branch_id' SKIP 1

BREAK ON branch_id SKIP 1 ON REPORT
COMPUTE SUM LABEL 'BRANCH TOTAL' OF orders_cnt revenue ON branch_id
COMPUTE SUM LABEL 'GRAND TOTAL' OF orders_cnt revenue ON REPORT

SELECT
    b.branch_id,
    b.branch_name,
    SUBSTR(o.order_time,1,2) || ':00' AS hour_slot,
    COUNT(DISTINCT o.order_id) AS orders_cnt,
    SUM(oi.quantity * p.unit_price) AS revenue
FROM branch b
JOIN orders o ON o.branch_id = b.branch_id
JOIN order_item oi ON oi.order_id = o.order_id
JOIN product p ON p.product_id = oi.product_id
WHERE o.order_date BETWEEN TO_DATE('&v_start_date','DD-MON-YY')
                        AND TO_DATE('&v_end_date','DD-MON-YY')
  AND (UPPER('&v_branch_id') = 'ALL'
       OR TO_CHAR(b.branch_id) = '&v_branch_id')
GROUP BY b.branch_id, b.branch_name, SUBSTR(o.order_time,1,2)
ORDER BY b.branch_id, hour_slot;

TTITLE OFF
CLEAR BREAKS
CLEAR COMPUTES


-- ----------------------------------------------------------
-- 6.6 Low and No Sales Products Exception Report
-- ----------------------------------------------------------
SET VERIFY OFF
SET LINESIZE 2000
SET PAGESIZE 2000
SET TRIMSPOOL ON
SET COLSEP ' '
PROMPT =========================================
PROMPT Q6: Low/No Sales Products Exception Report
PROMPT Date format example: 01-SEP-25
PROMPT Threshold example: 5
PROMPT =========================================
ACCEPT v_start_date CHAR PROMPT 'Enter start date (DD-MON-YY) : '
ACCEPT v_end_date CHAR PROMPT 'Enter end date (DD-MON-YY) : '
ACCEPT v_min_qty CHAR PROMPT 'Enter minimum quantity threshold (e.g. 5): '

COLUMN product_id FORMAT 99999 HEADING 'Product ID'
COLUMN product_name FORMAT A30 TRUNCATE HEADING 'Product Name'
COLUMN category FORMAT A12 TRUNCATE HEADING 'Category'
COLUMN qty_sold FORMAT 999,999 HEADING 'Qty Sold'
COLUMN revenue FORMAT 999,999.99 HEADING 'Revenue(RM)'
COLUMN remark FORMAT A16 HEADING 'Remark'

TTITLE LEFT 'ZUS Coffee - Low/No Sales Products (Exception Report)' SKIP 1 -
       LEFT 'Period: &v_start_date to &v_end_date  Threshold: < &v_min_qty units' SKIP 1

BREAK ON REPORT
COMPUTE SUM LABEL 'TOTAL' OF qty_sold revenue ON REPORT

SELECT
    p.product_id,
    p.product_name,
    p.category,
    NVL(s.qty_sold, 0) AS qty_sold,
    NVL(s.revenue, 0) AS revenue,
    CASE
        WHEN NVL(s.qty_sold, 0) = 0 THEN 'NO SALES'
        ELSE 'LOW SALES'
    END AS remark
FROM product p
LEFT JOIN (
    SELECT
        oi.product_id,
        SUM(oi.quantity) AS qty_sold,
        SUM(oi.quantity * pr.unit_price) AS revenue
    FROM order_item oi
    JOIN orders o ON o.order_id = oi.order_id
    JOIN product pr ON pr.product_id = oi.product_id
    WHERE o.order_date BETWEEN TO_DATE('&v_start_date','DD-MON-YY')
                            AND TO_DATE('&v_end_date','DD-MON-YY')
    GROUP BY oi.product_id
) s ON s.product_id = p.product_id
WHERE NVL(s.qty_sold, 0) < TO_NUMBER('&v_min_qty')
ORDER BY qty_sold ASC, revenue ASC, p.product_id;

TTITLE OFF
CLEAR BREAKS
CLEAR COMPUTES
