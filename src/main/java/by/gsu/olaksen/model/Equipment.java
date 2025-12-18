package by.gsu.olaksen.model;

public class Equipment {
    private int id;
    private String model;
    private String status;
    private String term;
    private String type;

    public Equipment(String model, String status, String term, String type) {
        this.model = model;
        this.status = status;
        this.term = term;
        this.type = type;
    }

        public Equipment(int id, String model, String status, String term, String type) {
        this.id = id;
        this.model = model;
        this.status = status;
        this.term = term;
        this.type = type;
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

}