package org.example.models;

import java.util.Date;
import java.util.Objects;

public class Product implements Comparable<Product> {


    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private float price; //Значение поля должно быть больше 0
    private String partNumber; //Значение этого поля должно быть уникальным, Строка не может быть пустой, Поле не может быть null
    private int manufactureCost;
    private UnitOfMeasure unitOfMeasure; //Поле не может быть null
    private Organization manufacturer; //Поле не может быть null

    public Product(
            int id,
            String name,
            Coordinates coordinates,
            float price,String partNumber,
            int manufactureCost,
            UnitOfMeasure unitOfMeasure,
            Organization manufacturer){

        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Поле name не может быть null или пустым");
        }
        if (coordinates == null) {
            throw new IllegalArgumentException("Поле coordinates не может быть null");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Поле price должно быть больше 0");
        }
        if (partNumber == null || partNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Поле partNumber не может быть null или пустым");
        }
        if (unitOfMeasure == null) {
            throw new IllegalArgumentException("Поле unitOfMeasure не может быть null");
        }
        if (manufacturer == null) {
            throw new IllegalArgumentException("Поле manufacturer не может быть null");
        }
        this.id=id;
        this.name = name;
        this.coordinates = coordinates;
        this.price = price;
        this.partNumber = partNumber;
        this.manufactureCost = manufactureCost;
        this.unitOfMeasure = unitOfMeasure;
        this.manufacturer = manufacturer;
        this.creationDate = new Date();

    }
    public Product(){}

    public int getManufactureCost() {
        return manufactureCost;
    }

    public void setManufactureCost(int manufactureCost) {
        this.manufactureCost = manufactureCost;
    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public UnitOfMeasure getUnitOfMeasure() {
        return unitOfMeasure;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public float getPrice() {
        return price;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Organization getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Organization manufacturer) {
        this.manufacturer = manufacturer;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", partNumber='" + partNumber + '\'' +
                ", unitOfMeasure=" + unitOfMeasure +
                ", manufacturer=" + manufacturer +
                '}';
    }

    @Override
    public boolean equals(Object obj){
        if(this==obj){
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) return false;
        Product product=(Product) obj;
        return Objects.equals(product.partNumber, this.partNumber);
    }

    @Override
    public int compareTo(Product o) {
        return this.name.compareTo(o.name);
    }






}
