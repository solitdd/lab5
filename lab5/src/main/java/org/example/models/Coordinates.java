package org.example.models;

public class Coordinates {
    private Float x; //Значение поля должно быть больше -286, Поле не может быть null
    private double y; //Значение поля должно быть больше -422
    public Coordinates(Float x, double y) {

        if (x == null) {
            throw new IllegalArgumentException("Поле x не может быть null");
        }
        if (x <= -286) {
            throw new IllegalArgumentException("Поле x должно быть больше -286");
        }
        if (y <= -422) {
            throw new IllegalArgumentException("Поле y должно быть больше -422");
        }

        this.x = x;
        this.y = y;
    }
    public Coordinates(){}
    public double getY() {
        return y;
    }

    public Float getX() {
        return x;
    }
}
