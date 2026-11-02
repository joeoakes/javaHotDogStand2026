CREATE TABLE HD_Order (
    Order_ID int NOT NULL PRIMARY KEY,
    Order_Price int NOT NULL,
    Order_Date_TIME TIMESTAMP NOT NULL,
    Item_Name VARCHAR(20) NOT NULL,
    Customer_ID int NOT NULL,
    FOREIGN KEY (Customer_ID) REFERENCES HD_Customer(Customer_ID)
);