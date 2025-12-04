package by.gsu.olaksen.model;

public class RentItem {
    private String model;
    private String status;
    private String term;

    public RentItem(String model, String status, String term) {
        this.model = model;
        this.status = status;
        this.term = term;
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
}