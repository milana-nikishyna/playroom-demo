package by.gsu.olaksen.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class Equipment {
    private int id;
    private String model;
    private String status;
    private String term;
    private String type;
    private BigDecimal pricePerHour;

    /**
     * Конструктор без id (используется до сохранения сущности в БД).
     * Тип должен всегда указываться явно вызывающим кодом (например, вкладкой).
     */
    public Equipment(String model, String status, String term, String type) {
        this.model = model;
        this.status = status;
        this.term = term;
        this.type = type;
        this.pricePerHour = new BigDecimal(0);
    }

}