package by.gsu.olaksen.db;

import by.gsu.olaksen.model.TableTop;

import java.util.List;

public class TableTopRepository extends BaseRepository<TableTop> {

    @Override
    protected void initDb() {
        var sql = SQL_DIALECT.createTableTopsTable();
        executeStatement(sql);
    }

    public List<TableTop> getAllTableTops() {
        var sql = "SELECT id, inv_num, name FROM tabletops";
        return executeQuery(sql, rs ->
                new TableTop(rs.getInt("id"), rs.getInt("inv_num"), rs.getString("name"))
        );
    }

    public int addTableTop(TableTop tabletop) {
        var sql = "INSERT INTO tabletops (inv_num, name) VALUES (?, ?)";
        return executeInsert(sql, ps -> {
            ps.setInt(1, tabletop.getTabletopInvNum());
            ps.setString(2, tabletop.getTabletopName());
        });
    }

    public void updateTableTop(TableTop tabletop) {
        var sql = "UPDATE tabletops SET inv_num = ?, name = ? WHERE id = ?";
        executeUpdate(sql, ps -> {
            ps.setInt(1, tabletop.getTabletopInvNum());
            ps.setString(2, tabletop.getTabletopName());
            ps.setInt(3, tabletop.getTabletopId());
        });
    }

    public void deleteTableTop(int id) {
        var sql = "DELETE FROM tabletops WHERE id = ?";
        executeDelete(sql, id);
    }
}
