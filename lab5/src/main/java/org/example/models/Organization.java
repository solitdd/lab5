package org.example.models;

public class Organization implements Comparable<Organization>{

    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private String fullName; //Значение этого поля должно быть уникальным, Поле может быть null
    private Long employeesCount; //Поле может быть null, Значение поля должно быть больше 0
    private OrganizationType type; //Поле может быть null

    public Organization( String name, String fullName, Long employeesCount, OrganizationType type){
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Поле name не может быть null или пустым");
        }

        if (employeesCount != null && employeesCount <= 0) {
            throw new IllegalArgumentException("employeesCount должен быть больше 0 или null");
        }
        this.name = name;
        this.fullName = fullName;
        this.employeesCount = employeesCount;
        this.type = type;

    }
    public Organization(){}

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public Long getEmployeesCount() {
        return employeesCount;
    }

    public OrganizationType getType() {
        return type;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Organization{id=" + id +
                ", name='" + name + '\'' +
                ", fullName='" + fullName + '\'' +
                ", employeesCount=" + employeesCount +
                ", type=" + type +
                '}';
    }

    @Override
    public int compareTo(Organization o) {
        return this.employeesCount.compareTo(o.employeesCount);
    }




}
