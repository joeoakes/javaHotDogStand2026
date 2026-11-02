/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package program;
import entity.*;
import java.util.List;
import java.util.Optional;
/**
 *
 * @author Gokhan
 */
public class MainOld {

    /**
     * @param args the command line arguments
     */
    
    private static DAO customerDAO;
    private static DAO orderDAO;
    
//    public static void main(String[] args) {
//        customerDAO = new CustomerDAO();
//        orderDAO = new OrderDAO();
//        printCustomers();
//        Customer customer;
//        addCustomer(1, "John", "Smith", "Hot Dog");
//        customer = new Customer(1, "John", "Doe", "Hot Dog");
//        customerDAO.update(customer);//Update John
//        customer = new Customer(2, "Alice", "Mira", "Drink");
//        customerDAO.insert(customer);//Insert Alice
//        customer = new Customer(2, "Alice", "Smith", "Drink");
//        customerDAO.update(customer);
//        customer = new Customer(3, "Sezen", "Aksu", "Combo");
//        customerDAO.insert(customer);//Insert Sezen
//        printCustomers();
//        customerDAO.delete(customer);//Delete Sezen
//        customer = new Customer(4, "Mike", "Smith", "Combo");
//        customerDAO.delete(customer);
//        printCustomers();
//        customer = getCustomer(0);//This customer does not exist
//        System.out.println(customer.getID() + "-" + customer.getFirstName() + "-" + customer.getLastName() + "-" + customer.getFavoriteMeal());//This customer does not exist, it will print non exist
//        printOrders();
//        Order order;
//        addOrder(1,2,"2021-02-23 08:48:11.556", "Hot Dog", 1);
//        order = new Order(2, 4, "2021-02-23 08:49:11.556", "Combo", 2);
//        orderDAO.insert(order);//insert order 2
//        order = new Order(2, 2, "2021-02-23 08:49:11.556", "Drink", 2);
//        orderDAO.update(order);//Update order 2 to drink and update its price to 2 dollars
//        order = new Order(3, 2, "2021-02-23 08:50:11.556", "Hot Dog", 2);
//        orderDAO.insert(order);//insert order 3
//        printOrders();
//        orderDAO.delete(order);//delete order 3
//        printOrders();
//    }
//    
//    static void addCustomer(int id, String firstName, String lastName, String favoriteMeal) {
//        Customer customer;
//        customer = new Customer(id, firstName, lastName, favoriteMeal);
//        customerDAO.insert(customer);
//    }
//    
//    static void addOrder(int id, int price, String dateTime, String itemName, int customerID) {
//        Order order;
//        order = new Order(id, price, dateTime, itemName, customerID);
//        orderDAO.insert(order);
//    }
//    
//    static Customer getCustomer(int id) {
//        Optional<Customer> customer = customerDAO.get(id);
//        return customer.orElseGet(() -> new Customer(-1, "Non-exist", "Non-exist", "Non-exist"));
//    }
//    
//    static Order getOrder(int id) {
//        Optional<Order> order = orderDAO.get(id);
//        return order.orElseGet(() -> new Order(-1, -1, "Non-exist", "Non-exist", -1));
//    }
//    
//    
//    static void printCustomers() {
//        List<String> headers = customerDAO.getColumnNames();
//        int numberCols = headers.size();
//        //Print column names as header
//        for (int i = 0; i < numberCols; i++) {
//            String header = headers.get(i);
//            System.out.printf("%25s", header);
//        }
//        System.out.println();
//        //Print the results
//        List<Customer> customers = customerDAO.getAll();
//        int numberRows = customers.size();
//        for (int i = 0; i < numberRows; i++) {
//            System.out.printf("%25s%25s%25s%25s", customers.get(i).getID(), customers.get(i).getFirstName(), customers.get(i).getLastName(), customers.get(i).getFavoriteMeal());
//            System.out.println();
//        }
//    }
//    
//    static void printOrders() {
//        List<String> headers = orderDAO.getColumnNames();
//        int numberCols = headers.size();
//        //Print column names as header
//        for (int i = 0; i < numberCols; i++) {
//            String header = headers.get(i);
//            System.out.printf("%25s", header);
//        }
//        System.out.println();
//        //Print the results
//        List<Order> orders = orderDAO.getAll();
//        int numberRows = orders.size();
//        for (int i = 0; i < numberRows; i++) {
//            System.out.printf("%25s%25s%25s%25s%25s", orders.get(i).getID(), orders.get(i).getPrice(), orders.get(i).getDateTime(), orders.get(i).getItemName(), orders.get(i).getCustomerID());
//            System.out.println();
//        }
//    }
}
