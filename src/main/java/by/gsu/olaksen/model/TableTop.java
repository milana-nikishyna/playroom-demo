package by.gsu.olaksen.model;

public class TableTop {
    private Long tabletopId;
    private String tabletopName;

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

    public void setTabletopId(Long tabletopId) {
        this.tabletopId = tabletopId;
    }

    public void setTabletopName(String tabletopName) {
        this.tabletopName = tabletopName;
    }
}
