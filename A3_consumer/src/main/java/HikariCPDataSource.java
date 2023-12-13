import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariCPDataSource {
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://master.cjrx3xkafq5a.us-west-2.rds.amazonaws.com/6650a2?serverTimezone=America/Los_Angeles");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername("admin");
        config.setPassword("xrj20011010");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.setMaximumPoolSize(16);

        dataSource = new HikariDataSource(config);
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }

    public static void main(String[] args) throws SQLException {
        HikariDataSource dataSource = HikariCPDataSource.getDataSource();
        Connection connection = dataSource.getConnection();
        System.out.println(connection.isValid(100));

    }
}
