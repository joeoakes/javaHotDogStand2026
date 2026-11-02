package core;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/** Embedded Apache Derby database and one-time schema initialization. */
public class DB {
    private static final String DB_URL =
            "jdbc:derby:hotdogstanddb;create=true;user=hotdogstanddb;password=hotdogstanddb";
    private static DB instance;
    private final Connection mConnection;

    private DB() throws SQLException {
        mConnection = DriverManager.getConnection(DB_URL);
        try {
            initializeSchema();
        } catch (SQLException ex) {
            try { mConnection.close(); } catch (SQLException closeEx) { ex.addSuppressed(closeEx); }
            throw ex;
        }
    }

    public static synchronized DB getInstance() {
        if (instance == null) {
            try {
                instance = new DB();
            } catch (SQLException ex) {
                throw new IllegalStateException("Unable to initialize Apache Derby database", ex);
            }
        }
        return instance;
    }

    private boolean tableExists(String table) throws SQLException {
        // Derby stores unquoted identifiers in uppercase.
        try (ResultSet rs = mConnection.getMetaData().getTables(
                null, null, table, new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    private void initializeSchema() throws SQLException {
        // Run in dependency order; preserve all existing data on later launches.
        if (!tableExists("HD_CUSTOMER")) {
            try (Statement st = mConnection.createStatement()) {
                st.executeUpdate("CREATE TABLE HD_Customer (" +
                        "Customer_ID INT NOT NULL PRIMARY KEY, " +
                        "Customer_First_Name VARCHAR(20) NOT NULL, " +
                        "Customer_Last_Name VARCHAR(20) NOT NULL, " +
                        "Customer_FAVORITE_MEAL VARCHAR(20) NOT NULL)");
            }
        }
        if (!tableExists("HD_ORDER")) {
            try (Statement st = mConnection.createStatement()) {
                st.executeUpdate("CREATE TABLE HD_Order (" +
                        "Order_ID INT NOT NULL PRIMARY KEY, " +
                        "Order_Price INT NOT NULL, " +
                        "Order_Date_TIME TIMESTAMP NOT NULL, " +
                        "Item_Name VARCHAR(20) NOT NULL, " +
                        "Customer_ID INT NOT NULL, " +
                        "FOREIGN KEY (Customer_ID) REFERENCES HD_Customer(Customer_ID))");
            }
        }
    }

    public int executeUpdate(String sql) throws SQLException {
        try (Statement st = mConnection.createStatement()) {
            return st.executeUpdate(sql);
        }
    }

    // Caller must close the returned ResultSet and its associated Statement.
    public ResultSet executeQuery(String sql) throws SQLException {
        return mConnection.createStatement().executeQuery(sql);
    }

    public PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return mConnection.prepareStatement(sql);
    }
}
