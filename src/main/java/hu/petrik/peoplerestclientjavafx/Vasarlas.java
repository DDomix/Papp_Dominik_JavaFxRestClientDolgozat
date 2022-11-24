package hu.petrik.peoplerestclientjavafx;

import com.google.gson.annotations.Expose;

public class Vasarlas {
    private int id;
    @Expose
    private String name;
    @Expose
    private String email;
    @Expose
    private int value;
    @Expose
    private int points;

    public Vasarlas(int id, String name, String email, int ar, int points) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.value = ar;
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int ar) {
        this.value = ar;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
