package by.gsu.olaksen.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Equipment {
    private int id;
    private String model;
    private String status;
    private String term;
    private String type;
    private double pricePerHour;

    /**
     * Constructor without id (used before entity is stored in DB).
     * Type must always be specified explicitly by the caller (e.g. tab).
     */
    public Equipment(String model, String status, String term, String type) {
        this.model = model;
        this.status = status;
        this.term = term;
        this.type = type;
    }

}