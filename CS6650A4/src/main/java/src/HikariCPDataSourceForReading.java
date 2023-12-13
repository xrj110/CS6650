package src;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariCPDataSourceForReading {
    private static HikariDataSource dataSourceForReading;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://slave2.cjrx3xkafq5a.us-west-2.rds.amazonaws.com:3306/6650a2?serverTimezone=America/Los_Angeles");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername("admin");
        config.setPassword("xrj20011010");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.setMaximumPoolSize(70);

        dataSourceForReading = new HikariDataSource(config);
    }

    public static HikariDataSource getDataSource() {
        return dataSourceForReading;
    }

    public static void main(String[] args) throws SQLException {
        HikariDataSource dataSource = HikariCPDataSource.getDataSource();
        Connection connection = dataSource.getConnection();
        System.out.println(connection.isValid(100));

    }
}
