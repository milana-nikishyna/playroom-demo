package by.gsu.olaksen.model;

import java.util.Objects;

public final class TableTop {
    private final Long tabletopId;
    private final String tabletopName;

    public TableTop(Long tabletopId, String tabletopName) {
        this.tabletopId = tabletopId;
        this.tabletopName = tabletopName;
    }

    public Long getTabletopId() {
        return tabletopId;
    }

    public String getTabletopName() {
        return tabletopName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        TableTop that = (TableTop) obj;
        return Objects.equals(this.tabletopId, that.tabletopId) &&
                Objects.equals(this.tabletopName, that.tabletopName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tabletopId, tabletopName);
    }

    @Override
    public String toString() {
        return "TableTop[" +
                "tabletopId=" + tabletopId + ", " +
                "tabletopName=" + tabletopName + ']';
    }

}
