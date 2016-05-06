package bahamas.dao;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class that manages connections to the database. It also has a utility
 * method that close connections, statements and resultsets
 */
public class ConnectionManager {

    private static final String PROPS_FILENAME = "/bahamas/properties/connection.properties";
    private static String dbUser;
    private static String dbPassword;
    private static String dbURL;

    static {
        if (!readOpenshiftDatabaseProperties()) {
            readLocalDatabaseProperties();
        }
        initDBDriver();
    }

    private static boolean readOpenshiftDatabaseProperties() {
        // grab environment variable
        String host = System.getenv("OPENSHIFT_MYSQL_DB_HOST");

        if (host == null) {
            return false;
        }
        // this is production environment
        // obtain database connection properties from environment variables
        String port = System.getenv("OPENSHIFT_MYSQL_DB_PORT");
        String dbName = System.getenv("OPENSHIFT_APP_NAME");
        dbUser = System.getenv("OPENSHIFT_MYSQL_DB_USERNAME");
        dbPassword = System.getenv("OPENSHIFT_MYSQL_DB_PASSWORD");

        dbURL = "jdbc:mysql://" + host + ":" + port + "/" + dbName+"?useUnicode=yes&characterEncoding=UTF-8&useServerPrepStmts=false&rewriteBatchedStatements=true";
        return true;
    }

    private static void readLocalDatabaseProperties() {
        try {
            // Retrieve properties from connection.properties via the CLASSPATH
            // WEB-INF/classes is on the CLASSPATH
            InputStream is = ConnectionManager.class.getResourceAsStream(PROPS_FILENAME);
            Properties props = new Properties();
            props.load(is);

            // load database connection details
            String host = props.getProperty("db.host");
            String port = props.getProperty("db.port");
            String dbName = props.getProperty("db.name");
            dbUser = props.getProperty("db.user");
            dbPassword = props.getProperty("db.password");

            dbURL = "jdbc:mysql://" + host + ":" + port + "/" + dbName+"?useUnicode=yes&characterEncoding=UTF-8&useServerPrepStmts=false&rewriteBatchedStatements=true";
        } catch (Exception ex) {
            // unable to load properties file
            String message = "Unable to load '" + PROPS_FILENAME + "'.";

            System.out.println(message);
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, message, ex);
            throw new RuntimeException(message, ex);
        }
    }

    private static void initDBDriver() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // unable to load properties file
            String message = "Unable to find JDBC driver for MySQL.";

            System.out.println(message);
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.SEVERE, message, ex);
            throw new RuntimeException(message, ex);
        }
    }

    /**
     * Gets a connection to the database
     *
     * @return the connection
     * @throws SQLException if an error occurs when connecting
     */
    public static Connection getConnection() throws SQLException {
        String message = "dbURL: " + dbURL
                + "  , dbUser: " + dbUser
                + "  , dbPassword: " + dbPassword;
        Logger.getLogger(ConnectionManager.class.getName()).log(Level.INFO, message);

        return DriverManager.getConnection(dbURL, dbUser, dbPassword);

    }

    /**
     * close the given connection, statement and resultset
     *
     * @param conn the connection object to be closed
     * @param stmt the statement object to be closed
     * @param rs the resultset object to be closed
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.WARNING,
                    "Unable to close ResultSet", ex);
        }
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.WARNING,
                    "Unable to close Statement", ex);
        }
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ConnectionManager.class.getName()).log(Level.WARNING,
                    "Unable to close Connection", ex);
        }
    }
    /**
     * Closes the given connection, statement.
     *
     * @param conn the connection object to be closed
     * @param stmt the statement object to be closed
     */
    public static void close(Connection conn, Statement stmt) {
        close(conn, stmt, null);
    }
    
    /**
     * Closes the given connection.
     * @param conn 
     */
    public static void close(Connection conn) {
        close(conn, null, null);
    }
    
    /**
     * Set auto commit to false.
     *
     * @param conn the connection object to be closed
     */

    public static void disableCommit(Connection conn) {
        try {
            if (conn != null) {
                conn.setAutoCommit(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Set auto commit to true.
     *
     * @param conn the connection object to be closed
     */
    public static void enableCommit(Connection conn) {
        try {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
