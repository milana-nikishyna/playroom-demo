package by.gsu.olaksen.model;

public class TableTop {
    private int tabletopId;
    private String tabletopName;

    public TableTop(int tabletopId, String tabletopName) {
        this.tabletopId = tabletopId;
        this.tabletopName = tabletopName;
    }
	
	public TableTop(String tabletopName) {
        this.tabletopName = tabletopName;
    }

    public int getTabletopId() {
        return tabletopId;
    }

    public String getTabletopName() {
        return tabletopName;
    }

    public void setTabletopId(int tabletopId) {
        this.tabletopId = tabletopId;
    }

    public void setTabletopName(String tabletopName) {
        this.tabletopName = tabletopName;
    }
}
