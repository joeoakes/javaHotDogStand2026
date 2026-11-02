/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import core.*;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
/**
 *
 * @author Gokhan
 */
public class OrderDAO implements DAO<Order>
{   
    public OrderDAO() {
        
    }
    List<Order> orders;
    /**
     * Get a single order entity as an order object
     * @param id
     * @return 
     */
    @Override
    public Optional<Order> get(int id) {
        DB db = DB.getInstance();
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM HD_Order WHERE Order_ID = ?";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            Order order = null;
            while (rs.next()) {
                order = new Order(rs.getInt("Order_ID"), rs.getInt("Order_Price"), rs.getString("Order_Date_Time"), rs.getString("Item_Name"), rs.getInt("Customer_ID"));
            }
            return Optional.ofNullable(order);
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            return null;
        }
    }
    
    /**
     * Get all order entities as a List
     * @return 
     */
    @Override
    public List<Order> getAll() {
        DB db = DB.getInstance();
        ResultSet rs = null;
        orders = new ArrayList<>();
        try {
            String sql = "SELECT * FROM HD_Order";
            rs = db.executeQuery(sql);
            Order order = null;
            while (rs.next()) {
                order = new Order(rs.getInt("Order_ID"), rs.getInt("Order_Price"), rs.getString("Order_Date_Time"), rs.getString("Item_Name"), rs.getInt("Customer_ID"));
                orders.add(order);
            }
            return orders;
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            return null;
        }
    }
    
    /**
     * Insert an order object into order table
     * @param order 
     */
    @Override
    public void insert(Order order)
    {
        DB db = DB.getInstance();
        try {
            String sql = "INSERT INTO HD_Order(Order_ID, Order_Price, Order_Date_Time, Item_Name, Customer_ID) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setInt(1, order.getID());
            stmt.setInt(2, order.getPrice());
            stmt.setString(3, order.getDateTime());
            stmt.setString(4, order.getItemName());
            stmt.setInt(5, order.getCustomerID());
            int rowInserted = stmt.executeUpdate();
            if (rowInserted > 0) {
                System.out.println("A new order was inserted successfully!");
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }
    
    /**
     * Update an order entity in database if it exists using an order object
     * @param order
     */
    @Override
    public void update(Order order) {
        DB db = DB.getInstance();
        try {
            String sql = "UPDATE HD_Order SET Order_Price=?, Order_Date_Time=?, Item_Name=?, Customer_ID=? WHERE Order_ID=?";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setInt(1, order.getPrice());
            stmt.setString(2, order.getDateTime());
            stmt.setString(3, order.getItemName());
            stmt.setInt(4, order.getCustomerID());
            stmt.setInt(5, order.getID());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("An existing order was updated successfully!");
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }
    
    /**
     * Delete an order from order table if the entity exists
     * @param order 
     */
    @Override
    public void delete(Order order) {
        DB db = DB.getInstance();
        try {
            String sql = "DELETE FROM HD_Order WHERE Order_ID = ?";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setInt(1, order.getID());
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("An order was deleted successfully!");
            }
        } catch (SQLException ex) {
            System.err.println(ex.toString());
        }
    }
    
    /**
     * Get all column names in a list array
     * @return 
     */
    @Override
    public List<String> getColumnNames() {
        DB db = DB.getInstance();
        ResultSet rs = null;
        List<String> headers = new ArrayList<>();
        try {
            String sql = "SELECT * FROM HD_Order WHERE Order_ID = -1";//We just need this sql query to get the column headers
            rs = db.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            //Get number of columns in the result set
            int numberCols = rsmd.getColumnCount();
            for (int i = 1; i <= numberCols; i++) {
                headers.add(rsmd.getColumnLabel(i));//Add column headers to the list
            }
            return headers;
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            return null;
        } 
    }
}
