package by.gsu.olaksen.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class TableTop {
    private int tabletopId;
    private final int tabletopInvNum;
    private final String tabletopName;
}
