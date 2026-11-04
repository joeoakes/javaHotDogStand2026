package program;

import entity.Customer;
import entity.CustomerDAO;
import entity.Order;
import entity.OrderDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Native IntelliJ Swing UI Designer form.
 *
 * The matching Main.form creates and binds the components declared below.
 * This class contains application behavior, not generated layout code.
 *
 * IMPORTANT: Compile using IntelliJ's Swing UI Designer support.
 */
public class Main extends JFrame {
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    // These names must match the "binding" attributes in Main.form.
    private JPanel rootPanel;
    private JTabbedPane jTabbedPaneHotDogStand;
    private JPanel jPanelCustomer;
    private JPanel jPanelOrder;

    private JTextField jTextFieldCustomerID;
    private JTextField jTextFieldCustomerFirstName;
    private JTextField jTextFieldCustomerLastName;
    private JTextField jTextFieldCustomerFavoriteMeal;
    private JButton jButtonInsertCustomer;
    private JButton jButtonUpdateCustomer;
    private JButton jButtonDeleteCustomer;
    private JTable jTableCustomer;

    private JTextField jTextFieldOrderID;
    private JTextField jTextFieldOrderCustomerID;
    private JTextField jTextFieldOrderPrice;
    private JTextField jTextFieldOrderDateTime;
    private JTextField jTextFieldItemName;
    private JButton jButtonInsertOrder;
    private JButton jButtonUpdateOrder;
    private JButton jButtonDeleteOrder;
    private JTable jTableOrder;

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Main() {
        // rootPanel is instantiated by IntelliJ's GUI Designer instrumentation.
        setContentPane(rootPanel);
        setTitle("Hot Dog Stand - JDBC");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 430);
        setLocationRelativeTo(null);

        // The tables display database records but do not allow inline editing.
        jTableCustomer.setModel(readOnlyModel(
                "ID", "First Name", "Last Name", "Favorite Meal"));
        jTableOrder.setModel(readOnlyModel(
                "Order ID", "Customer ID", "Name", "Order Price",
                "Order Date Time", "Item Name"));

        // Register handlers for CRUD buttons.
        jButtonInsertCustomer.addActionListener(e -> insertCustomer());
        jButtonUpdateCustomer.addActionListener(e -> updateCustomer());
        jButtonDeleteCustomer.addActionListener(e -> deleteCustomer());
        jButtonInsertOrder.addActionListener(e -> insertOrder());
        jButtonUpdateOrder.addActionListener(e -> updateOrder());
        jButtonDeleteOrder.addActionListener(e -> deleteOrder());

        // Clicking a table row copies the values into the edit fields.
        jTableCustomer.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = jTableCustomer.getSelectedRow();
                if (row < 0) return;
                int modelRow = jTableCustomer.convertRowIndexToModel(row);
                jTextFieldCustomerID.setText(cell(jTableCustomer, modelRow, 0));
                jTextFieldCustomerFirstName.setText(cell(jTableCustomer, modelRow, 1));
                jTextFieldCustomerLastName.setText(cell(jTableCustomer, modelRow, 2));
                jTextFieldCustomerFavoriteMeal.setText(cell(jTableCustomer, modelRow, 3));
            }
        });
        jTableOrder.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                int row = jTableOrder.getSelectedRow();
                if (row < 0) return;
                int modelRow = jTableOrder.convertRowIndexToModel(row);
                jTextFieldOrderID.setText(cell(jTableOrder, modelRow, 0));
                jTextFieldOrderCustomerID.setText(cell(jTableOrder, modelRow, 1));
                jTextFieldOrderPrice.setText(cell(jTableOrder, modelRow, 3));
                jTextFieldOrderDateTime.setText(cell(jTableOrder, modelRow, 4));
                jTextFieldItemName.setText(cell(jTableOrder, modelRow, 5));
            }
        });

        refreshCustomersTable();
        refreshOrdersTable();
    }

    private static DefaultTableModel readOnlyModel(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private static String cell(JTable table, int row, int column) {
        Object value = table.getModel().getValueAt(row, column);
        return value == null ? "" : value.toString();
    }

    private static int number(JTextField field, String label) {
        try {
            return Integer.parseInt(field.getText().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(label + " must be a whole number.");
        }
    }

    private static String required(JTextField field, String label) {
        String value = field.getText().trim();
        if (value.isEmpty()) throw new IllegalArgumentException(label + " is required.");
        return value;
    }

    private static String orderDateTime(JTextField field) {
        String text = required(field, "Order date/time");
        try {
            // Accept both the simple input format and Derby timestamp strings.
            LocalDateTime value;
            if (text.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$")) {
                value = LocalDateTime.parse(text + ":00", DATE_FORMAT);
            } else {
                value = LocalDateTime.parse(text.replace(' ', 'T'));
            }
            return value.format(DATE_FORMAT) + ".0";
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(
                    "Use date/time format yyyy-MM-dd HH:mm:ss (example: 2026-11-04 14:30:00).");
        }
    }

    private Customer customerFromForm() {
        return new Customer(
                number(jTextFieldCustomerID, "Customer ID"),
                required(jTextFieldCustomerFirstName, "First name"),
                required(jTextFieldCustomerLastName, "Last name"),
                required(jTextFieldCustomerFavoriteMeal, "Favorite meal"));
    }

    private Order orderFromForm() {
        return new Order(
                number(jTextFieldOrderID, "Order ID"),
                number(jTextFieldOrderPrice, "Order price"),
                orderDateTime(jTextFieldOrderDateTime),
                required(jTextFieldItemName, "Item name"),
                number(jTextFieldOrderCustomerID, "Customer ID"));
    }

    private void execute(String action, Runnable work) {
        try {
            work.run();
        } catch (IllegalArgumentException | IllegalStateException ex) {
            showError(action + ": " + ex.getMessage());
        } catch (RuntimeException ex) {
            showError(action + " failed: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Hot Dog Stand",
                JOptionPane.ERROR_MESSAGE);
    }

    private boolean confirm(String type) {
        return JOptionPane.showConfirmDialog(this,
                "Delete this " + type + "?", "Confirm deletion",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private void insertCustomer() {
        execute("Insert customer", () -> {
            Customer c = customerFromForm();
            if (customerDAO.get(c.getID()).isPresent())
                throw new IllegalArgumentException("Customer ID already exists.");
            customerDAO.insert(c);
            refreshCustomersTable();
            clearCustomer();
        });
    }

    private void updateCustomer() {
        execute("Update customer", () -> {
            Customer c = customerFromForm();
            if (!customerDAO.get(c.getID()).isPresent())
                throw new IllegalArgumentException("Customer does not exist.");
            customerDAO.update(c);
            refreshCustomersTable();
        });
    }

    private void deleteCustomer() {
        execute("Delete customer", () -> {
            int id = number(jTextFieldCustomerID, "Customer ID");
            Customer c = customerDAO.get(id).orElseThrow(
                    () -> new IllegalArgumentException("Customer does not exist."));
            if (!confirm("customer")) return;
            customerDAO.delete(c);
            refreshCustomersTable();
            refreshOrdersTable();
            clearCustomer();
        });
    }

    private void insertOrder() {
        execute("Insert order", () -> {
            Order o = orderFromForm();
            if (orderDAO.get(o.getID()).isPresent())
                throw new IllegalArgumentException("Order ID already exists.");
            validateOrderCustomer(o);
            orderDAO.insert(o);
            refreshOrdersTable();
            clearOrder();
        });
    }

    private void updateOrder() {
        execute("Update order", () -> {
            Order o = orderFromForm();
            if (!orderDAO.get(o.getID()).isPresent())
                throw new IllegalArgumentException("Order does not exist.");
            validateOrderCustomer(o);
            orderDAO.update(o);
            refreshOrdersTable();
        });
    }

    private void deleteOrder() {
        execute("Delete order", () -> {
            int id = number(jTextFieldOrderID, "Order ID");
            Order o = orderDAO.get(id).orElseThrow(
                    () -> new IllegalArgumentException("Order does not exist."));
            if (!confirm("order")) return;
            orderDAO.delete(o);
            refreshOrdersTable();
            clearOrder();
        });
    }

    private void validateOrderCustomer(Order order) {
        if (!customerDAO.get(order.getCustomerID()).isPresent())
            throw new IllegalArgumentException("Customer ID does not exist.");
    }

    private void refreshCustomersTable() {
        DefaultTableModel model = (DefaultTableModel) jTableCustomer.getModel();
        model.setRowCount(0);
        for (Customer c : customerDAO.getAll()) {
            model.addRow(new Object[]{
                    c.getID(), c.getFirstName(), c.getLastName(), c.getFavoriteMeal()
            });
        }
    }

    private void refreshOrdersTable() {
        DefaultTableModel model = (DefaultTableModel) jTableOrder.getModel();
        model.setRowCount(0);
        for (Order o : orderDAO.getAll()) {
            String name = customerDAO.get(o.getCustomerID())
                    .map(c -> c.getFirstName() + " " + c.getLastName())
                    .orElse("(missing customer)");
            model.addRow(new Object[]{
                    o.getID(), o.getCustomerID(), name, o.getPrice(),
                    o.getDateTime(), o.getItemName()
            });
        }
    }

    private void clearCustomer() {
        jTextFieldCustomerID.setText("");
        jTextFieldCustomerFirstName.setText("");
        jTextFieldCustomerLastName.setText("");
        jTextFieldCustomerFavoriteMeal.setText("");
    }

    private void clearOrder() {
        jTextFieldOrderID.setText("");
        jTextFieldOrderCustomerID.setText("");
        jTextFieldOrderPrice.setText("");
        jTextFieldOrderDateTime.setText("");
        jTextFieldItemName.setText("");
    }

    public static void main(String[] args) {
        // Swing windows must be created on the Event Dispatch Thread.
        SwingUtilities.invokeLater(() -> {
            try {
                new Main().setVisible(true);
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(null, ex.toString(),
                        "Startup error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
    }
}
