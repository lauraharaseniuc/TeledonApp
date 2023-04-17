package model;

import java.io.Serializable;

public class Case implements Entity<Integer>, Serializable {
    private Integer id;
    private String description;
    private String city;
    private String country;
    private String personInNeed;
    private double necessarySum;

    public Case(Integer id) {
        this.id = id;
        this.description=null;
        this.city=null;
        this.country=null;
        this.personInNeed=null;
        this.necessarySum= 0;
    }

    public Case(String description, String city, String country, String personInNeed, double necessarySum) {
        this.description = description;
        this.city = city;
        this.country = country;
        this.personInNeed = personInNeed;
        this.necessarySum = necessarySum;
    }

    public Case(Integer id, String description, String city, String country, String personInNeed, double necessarySum) {
        this.id = id;
        this.description = description;
        this.city = city;
        this.country = country;
        this.personInNeed = personInNeed;
        this.necessarySum = necessarySum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPersonInNeed() {
        return personInNeed;
    }

    public void setPersonInNeed(String personInNeed) {
        this.personInNeed = personInNeed;
    }

    public double getNecessarySum() {
        return necessarySum;
    }

    public void setNecessarySum(double necessarySum) {
        this.necessarySum = necessarySum;
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id=id;
    }

    @Override
    public String toString() {
        return "Case{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", personInNeed='" + personInNeed + '\'' +
                ", necessarySum=" + necessarySum +
                '}';
    }
}
