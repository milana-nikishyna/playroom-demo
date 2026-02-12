package by.gsu.olaksen.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TableTop {
    private int tabletopId;
    private int tabletopInvNum;
    private String tabletopName;

	public TableTop(int tabletopInvNum, String tabletopName) {
        this.tabletopInvNum = tabletopInvNum;
        this.tabletopName = tabletopName;
    }
}
