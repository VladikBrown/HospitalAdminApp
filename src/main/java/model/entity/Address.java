package model.entity;

import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.Objects;

public class Address {
    private String street;
    @BsonProperty(value = "home_number")
    private Integer homeNumber;
    @BsonProperty(value = "building_number")
    private Integer buildingNumber;
    @BsonProperty(value = "apartments_number")
    private Integer apartmentsNumber;

    public Address(){
        street = "";
        homeNumber = 0;
        buildingNumber = 0;
        apartmentsNumber = 0;
    }

    public Address(String street, Integer homeNumber, Integer buildingNumber, Integer apartmentsNumber) {
        this.street = street;
        this.homeNumber = homeNumber;
        this.buildingNumber = buildingNumber;
        this.apartmentsNumber = apartmentsNumber;
    }


    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getHomeNumber() {
        return homeNumber;
    }

    public void setHomeNumber(Integer homeNumber) {
        this.homeNumber = homeNumber;
    }

    public Integer getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(Integer buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public Integer getApartmentsNumber() {
        return apartmentsNumber;
    }

    public void setApartmentsNumber(Integer apartmentsNumber) {
        this.apartmentsNumber = apartmentsNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(street, address.street) &&
                Objects.equals(homeNumber, address.homeNumber) &&
                Objects.equals(buildingNumber, address.buildingNumber) &&
                Objects.equals(apartmentsNumber, address.apartmentsNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, homeNumber, buildingNumber, apartmentsNumber);
    }

    @Override
    public String toString() {
        return "st. " + street +
                ", home: " + homeNumber +
                ", building: " + buildingNumber +
                ", ap: " + apartmentsNumber;
    }
}
