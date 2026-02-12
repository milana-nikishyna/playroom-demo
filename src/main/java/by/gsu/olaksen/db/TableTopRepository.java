package by.gsu.olaksen.db;

import by.gsu.olaksen.model.TableTop;

import java.util.List;

public class TableTopRepository extends BaseRepository<TableTop> {

    @Override
    protected void initDb() {
        var sql = """ 
                CREATE TABLE IF NOT EXISTS tabletops (
                    id INTEGER PRIMARY KEY NOT NULL,
                    name VARCHAR(255) NOT NULL
                )
                """;
        executeStatement(sql);
    }

    public List<TableTop> getAllTableTops() {
        var sql = "SELECT id, name FROM tabletops";
        return executeQuery(sql, rs ->
                new TableTop(rs.getInt("id"), rs.getString("name"))
        );
    }

    public int addTableTop(TableTop tabletop) {
        var sql = "INSERT INTO tabletops (name) VALUES (?)";
        return executeInsert(sql, ps -> ps.setString(1, tabletop.getTabletopName()));
    }

    public void updateTableTop(TableTop tabletop) {
        var sql = "UPDATE tabletops SET name = ? WHERE id = ?";
        executeUpdate(sql, ps -> {
            ps.setString(1, tabletop.getTabletopName());
            ps.setInt(2, tabletop.getTabletopId());
        });
    }

    public void deleteTableTop(int id) {
        var sql = "DELETE FROM tabletops WHERE id = ?";
        executeDelete(sql, id);
    }
}
