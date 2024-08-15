package cadastrobd.model.util;

/**
 *
 * @author mari-
 */

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;


public class ConectorBD {
    
    private Connection con;
    private PreparedStatement stmt;
    private ResultSet rs;
    private CredentialsLoader loader;
    
    private final String HOSTNAME;
    private final String DBNAME;
    private final String LOGIN;
    private final String PASSWORD;
    
    public ConectorBD() {
        loader = new CredentialsLoader();
        HOSTNAME = loader.getHostname();
        DBNAME = loader.getDbname();
        LOGIN = loader.getLogin();
        PASSWORD = loader.getPassword();
    }
    
    
    public Connection getConnection() throws SQLException {
        String URL = String.format("jdbc:sqlserver://%s:1433;databaseName=%s;",
         HOSTNAME, DBNAME).concat("encrypt=true;trustServerCertificate=true");
        con = DriverManager.getConnection(URL, LOGIN, PASSWORD);
        return con;
    }
    
    public PreparedStatement getPrepared(String sql) throws SQLException {
        stmt = getConnection().prepareStatement(sql);
        return stmt;
    }

    public ResultSet getSelect(String sql) throws SQLException {
        stmt = getPrepared(sql);
        rs = stmt.executeQuery();
        return rs;
    }

    public int insert(String sql) throws SQLException {
        stmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.executeUpdate();
        rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        } else {
            throw new SQLException("Data insert failed.");
        }
    }

    public boolean update(String sql) throws SQLException {
        return getPrepared(sql).executeUpdate() > 0;
    }

    public void close() throws SQLException {
        if (stmt != null && !stmt.isClosed()) {
            stmt.close();
        }
        if (rs != null && !rs.isClosed()) {
            rs.close();
        }
        if (con != null && !con.isClosed()) {
            con.close();
        }
    }
    
}
