package br.com.geoskills.model;

public class User {
    private String name;
    private String email;
    private int points;

    private String uuid;
    private int profileSelected;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uiid) {
        this.uuid = uiid;
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

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    // Para o firebase
    public User() {

    }

    public User(String name, String email, int points) {
        this.name = name;
        this.email = email;
        this.points = points;
    }
    public User(String name, String email, int points,String uuid) {
        this.name = name;
        this.email = email;
        this.points = points;
        this.uuid = uuid;
    }
    public User(String name, String email, int points, String uuid, int profileSelected) {
        this.name = name;
        this.email = email;
        this.points = points;
        this.uuid = uuid;
        this.profileSelected = profileSelected;
    }
    public User(String name, String email, int points,int profileSelected) {
        this.name = name;
        this.email = email;
        this.points = points;
        this.uuid = uuid;
        this.profileSelected = profileSelected;
    }

    public int getProfileSelected() {
        return profileSelected;
    }
}
