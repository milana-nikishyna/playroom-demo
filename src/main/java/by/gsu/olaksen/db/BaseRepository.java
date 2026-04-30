package by.gsu.olaksen.db;

import by.gsu.olaksen.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Абстрактный базовый репозиторий, предоставляющий общие операции с базой данных.
 * Уменьшает дублирование кода в классах репозиториев.
 *
 * @param <T> Тип сущности, которой управляет данный репозиторий
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
     * Инициализация схемы базы данных для данного репозитория.
     * Каждый репозиторий должен реализовать свою логику создания таблиц.
     */
    protected abstract void initDb();

    /**
     * Получает подключение к базе данных.
     */
    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Выполняет операцию INSERT и возвращает сгенерированный ключ.
     *
     * @param sql    SQL запрос INSERT
     * @param setter Функциональный интерфейс для установки параметров PreparedStatement
     * @return Сгенерированный ключ (ID), или 0 в случае ошибки
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
            logger.error("Ошибка при выполнении INSERT в БД", e);
        }
        return 0;
    }

    /**
     * Выполняет операцию UPDATE.
     *
     * @param sql    SQL запрос UPDATE
     * @param setter Функциональный интерфейс для установки параметров PreparedStatement
     */
    protected void executeUpdate(String sql, PreparedStatementSetter setter) {
        try (var conn = getConnection();
             var ps = conn.prepareStatement(sql)) {
            setter.setValues(ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Ошибка при выполнении UPDATE в БД", e);
        }
    }

    /**
     * Выполняет операцию DELETE с одним параметром ID.
     *
     * @param sql SQL запрос DELETE
     * @param id  ID для удаления
     */
    protected void executeDelete(String sql, int id) {
        try (var conn = getConnection();
             var ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Ошибка при выполнении DELETE в БД", e);
        }
    }

    /**
     * Выполняет операцию DELETE с пользовательскими параметрами.
     *
     * @param sql    SQL запрос DELETE
     * @param setter Функциональный интерфейс для установки параметров PreparedStatement
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
     * Выполняет SELECT запрос без параметров.
     *
     * @param sql    SQL запрос SELECT
     * @param mapper Функциональный интерфейс для преобразования ResultSet в сущность
     * @return Список сущностей
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
            logger.error("Ошибка при выполнении SELECT запроса в БД", e);
        }
        return result;
    }

    /**
     * Выполняет SELECT запрос с параметрами.
     *
     * @param sql    SQL запрос SELECT
     * @param setter Функциональный интерфейс для установки параметров PreparedStatement
     * @param mapper Функциональный интерфейс для преобразования ResultSet в сущность
     * @return Список сущностей
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
     * Выполняет произвольную SQL-команду (для изменения схемы и т.д.).
     *
     * @param sql SQL-команда для выполнения
     */
    protected void executeStatement(String sql) {
        try (var conn = getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            logger.error("Ошибка при выполнении SQL-команды в БД", e);
        }
    }

    /**
     * Функциональный интерфейс для установки параметров PreparedStatement.
     */
    @FunctionalInterface
    protected interface PreparedStatementSetter {
        void setValues(PreparedStatement ps) throws SQLException;
    }

    /**
     * Функциональный интерфейс для преобразования ResultSet в сущность.
     */
    @FunctionalInterface
    protected interface ResultSetMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }
}
