package by.gsu.olaksen.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Equipment {
    private int id;
    private String model;
    private String status;
    /**
     * Время окончания аренды. null — оборудование свободно.
     */
    private LocalDateTime rentUntil;
    private String type;
    private BigDecimal pricePerHour;

    /**
     * Конструктор без id (используется до сохранения сущности в БД).
     */
    public Equipment(String model, String status, LocalDateTime rentUntil, String type) {
        this.model = model;
        this.status = status;
        this.rentUntil = rentUntil;
        this.type = type;
        this.pricePerHour = new BigDecimal(0);
    }

}