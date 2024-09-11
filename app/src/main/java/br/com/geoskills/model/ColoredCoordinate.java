package br.com.geoskills.model;

public class ColoredCoordinate extends Coordinate{
    private String color;
    public ColoredCoordinate(String latitude, String longitude, String color) {
        super(latitude, longitude);
        this.color = color;
    }

    public String getColor(){
        return color;
    }

    public void setColor(String color){
        this.color = color;
    }

}
