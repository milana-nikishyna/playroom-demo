package by.gsu.olaksen.model;

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

    /**
     * Constructor with id (used when reading from DB).
     */
    public Equipment(int id, String model, String status, String term, String type, double pricePerHour) {
        this.id = id;
        this.model = model;
        this.status = status;
        this.term = term;
        this.type = type;
        this.pricePerHour = pricePerHour;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getId() {
        return id;
    }

    /**
     * ID is assigned by the DB; normally you only read it.
     * Kept public so repository/controller code can store the generated key.
     */
    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

}