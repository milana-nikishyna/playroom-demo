package by.gsu.olaksen.db;

/**
 * Абстракция для различных SQL диалектов (H2, SQLite, PostgreSQL).
 * Позволяет использовать одну кодовую базу для разных БД.
 */
public interface SqlDialect {

    /**
     * Фабрика для получения нужного диалекта на основе URL БД
     */
    static SqlDialect forUrl(String url) {
        if (url.contains("sqlite")) {
            return new SqliteSqlDialect();
        } else if (url.contains("postgresql")) {
            return new PostgresSqlDialect();
        } else {
            // H2 по умолчанию
            return new H2SqlDialect();
        }
    }

    /**
     * Возвращает SQL для создания таблицы equipment
     */
    String createEquipmentTable();

    /**
     * Возвращает SQL для создания таблицы room_booking
     */
    String createRoomBookingTable();

    /**
     * Возвращает SQL для создания таблицы tabletops
     */
    String createTableTopsTable();
}

/**
 * SQL диалект для H2
 */
class H2SqlDialect implements SqlDialect {
    @Override
    public String createEquipmentTable() {
        return """
                CREATE TABLE IF NOT EXISTS equipment (
                    id INTEGER PRIMARY KEY AUTO_INCREMENT,
                    model VARCHAR(255) NOT NULL,
                    available BOOLEAN NOT NULL,
                    rent_until TEXT,
                    type VARCHAR(50) NOT NULL,
                    price_per_hour VARCHAR(50) DEFAULT '0'
                )
                """;
    }

    @Override
    public String createRoomBookingTable() {
        return """
                CREATE TABLE IF NOT EXISTS room_booking (
                    id INTEGER PRIMARY KEY AUTO_INCREMENT,
                    room_number INTEGER NOT NULL,
                    booking_date TEXT NOT NULL,
                    hour VARCHAR(10) NOT NULL,
                    status VARCHAR(20) NOT NULL
                )
                """;
    }

    @Override
    public String createTableTopsTable() {
        return """
                CREATE TABLE IF NOT EXISTS tabletops (
                    id INTEGER PRIMARY KEY AUTO_INCREMENT,
                    inv_num INTEGER NOT NULL,
                    name VARCHAR(255) NOT NULL
                )
                """;
    }
}

/**
 * SQL диалект для SQLite
 */
class SqliteSqlDialect implements SqlDialect {
    @Override
    public String createEquipmentTable() {
        return """
                CREATE TABLE IF NOT EXISTS equipment (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    model VARCHAR(255) NOT NULL,
                    available BOOLEAN NOT NULL,
                    rent_until TEXT,
                    type VARCHAR(50) NOT NULL,
                    price_per_hour TEXT DEFAULT '0'
                )
                """;
    }

    @Override
    public String createRoomBookingTable() {
        return """
                CREATE TABLE IF NOT EXISTS room_booking (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    room_number INTEGER NOT NULL,
                    booking_date TEXT NOT NULL,
                    hour VARCHAR(10) NOT NULL,
                    status VARCHAR(20) NOT NULL
                )
                """;
    }

    @Override
    public String createTableTopsTable() {
        return """
                CREATE TABLE IF NOT EXISTS tabletops (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    inv_num INTEGER NOT NULL,
                    name VARCHAR(255) NOT NULL
                )
                """;
    }
}

/**
 * SQL диалект для PostgreSQL
 */
class PostgresSqlDialect implements SqlDialect {
    @Override
    public String createEquipmentTable() {
        return """
                CREATE TABLE IF NOT EXISTS equipment (
                    id SERIAL PRIMARY KEY,
                    model VARCHAR(255) NOT NULL,
                    available BOOLEAN NOT NULL,
                    rent_until TEXT,
                    type VARCHAR(50) NOT NULL,
                    price_per_hour VARCHAR(50) DEFAULT '0'
                )
                """;
    }

    @Override
    public String createRoomBookingTable() {
        return """
                CREATE TABLE IF NOT EXISTS room_booking (
                    id SERIAL PRIMARY KEY,
                    room_number INTEGER NOT NULL,
                    booking_date TEXT NOT NULL,
                    hour VARCHAR(10) NOT NULL,
                    status VARCHAR(20) NOT NULL
                )
                """;
    }

    @Override
    public String createTableTopsTable() {
        return """
                CREATE TABLE IF NOT EXISTS tabletops (
                    id SERIAL PRIMARY KEY,
                    inv_num INTEGER NOT NULL,
                    name VARCHAR(255) NOT NULL
                )
                """;
    }
}

