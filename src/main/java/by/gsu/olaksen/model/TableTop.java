package by.gsu.olaksen.model;

public class TableTop {
    private int tabletopId;
    private int tabletopInvNum;
    private String tabletopName;

    public TableTop(int tabletopId, int tabletopInvNum, String tabletopName) {
        this.tabletopId = tabletopId;
        this.tabletopInvNum = tabletopInvNum;
        this.tabletopName = tabletopName;
    }
	
	public TableTop(int tabletopInvNum, String tabletopName) {
        this.tabletopInvNum = tabletopInvNum;
        this.tabletopName = tabletopName;
    }

    public int getTabletopId() {
        return tabletopId;
    }

    public String getTabletopName() {
        return tabletopName;
    }

    public int getTabletopInvNum() {
        return tabletopInvNum;
    }

    public void setTabletopId(int tabletopId) {
        this.tabletopId = tabletopId;
    }

    public void setTabletopName(String tabletopName) {
        this.tabletopName = tabletopName;
    }

    public void setTabletopInvNum(int tabletopInvNum) {
        this.tabletopInvNum = tabletopInvNum;
    }
}
