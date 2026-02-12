package by.gsu.olaksen.db;

import by.gsu.olaksen.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base repository providing common database operations.
 * Reduces code duplication across repository classes.
 *
 * @param <T> The entity type this repository manages
 */
public abstract class BaseRepository<T> {
    private static final Logger logger = LoggerFactory.getLogger(BaseRepository.class);
    protected static final String URL = AppConfig.getInstance().getDbProperty("url");
    protected static final String USER = AppConfig.getInstance().getDbProperty("user");
    protected static final String PASSWORD = AppConfig.getInstance().getDbProperty("password");

    public BaseRepository() {
        initDb();
    }

    /**
     * Initialize database schema for this repository.
     * Each repository must implement its own table creation logic.
     */
    protected abstract void initDb();

    /**
     * Get a database connection.
     */
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Execute an INSERT statement and return the generated key.
     *
     * @param sql    The SQL INSERT statement
     * @param setter Functional interface to set PreparedStatement parameters
     * @return The generated key (ID), or 0 if failed
     */
    protected int executeInsert(String sql, PreparedStatementSetter setter) {
        try (var conn = getConnection();
             var ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setter.setValues(ps);
            ps.executeUpdate();
            try (var rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            logger.error("DB insert failed", e);
        }
        return 0;
    }

    /**
     * Execute an UPDATE statement.
     *
     * @param sql    The SQL UPDATE statement
     * @param setter Functional interface to set PreparedStatement parameters
     */
    protected void executeUpdate(String sql, PreparedStatementSetter setter) {
        try (var conn = getConnection();
             var ps = conn.prepareStatement(sql)) {
            setter.setValues(ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("DB update failed", e);
        }
    }

    /**
     * Execute a DELETE statement with a single ID parameter.
     *
     * @param sql The SQL DELETE statement
     * @param id  The ID to delete
     */
    protected void executeDelete(String sql, int id) {
        try (var conn = getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("DB delete failed", e);
        }
    }

    /**
     * Execute a DELETE statement with custom parameters.
     *
     * @param sql    The SQL DELETE statement
     * @param setter Functional interface to set PreparedStatement parameters
     */
    protected void executeDelete(String sql, PreparedStatementSetter setter) {
        try (var conn = getConnection();
             var ps = conn.prepareStatement(sql)) {
            setter.setValues(ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("DB delete failed", e);
        }
    }

    /**
     * Execute a SELECT query without parameters.
     *
     * @param sql    The SQL SELECT statement
     * @param mapper Functional interface to map ResultSet to entity
     * @return List of entities
     */
    protected List<T> executeQuery(String sql, ResultSetMapper<T> mapper) {
        var result = new ArrayList<T>();
        try (var conn = getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                result.add(mapper.map(rs));
            }
        } catch (SQLException e) {
            logger.error("DB query failed", e);
        }
        return result;
    }

    /**
     * Execute a SELECT query with parameters.
     *
     * @param sql    The SQL SELECT statement
     * @param setter Functional interface to set PreparedStatement parameters
     * @param mapper Functional interface to map ResultSet to entity
     * @return List of entities
     */
    protected List<T> executeQuery(String sql, PreparedStatementSetter setter, ResultSetMapper<T> mapper) {
        var result = new ArrayList<T>();
        try (var conn = getConnection();
             var ps = conn.prepareStatement(sql)) {
            setter.setValues(ps);
            try (var rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapper.map(rs));
                }
            }
        } catch (SQLException e) {
            logger.error("DB query failed", e);
        }
        return result;
    }

    /**
     * Execute a custom SQL statement (for schema alterations, etc.).
     *
     * @param sql The SQL statement to execute
     */
    protected void executeStatement(String sql) {
        try (var conn = getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            logger.error("DB statement failed", e);
        }
    }

    /**
     * Functional interface for setting PreparedStatement parameters.
     */
    @FunctionalInterface
    protected interface PreparedStatementSetter {
        void setValues(PreparedStatement ps) throws SQLException;
    }

    /**
     * Functional interface for mapping ResultSet to entity.
     */
    @FunctionalInterface
    protected interface ResultSetMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }
}
