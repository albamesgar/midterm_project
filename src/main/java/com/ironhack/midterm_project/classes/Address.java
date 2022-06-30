package com.ironhack.midterm_project.classes;

import javax.persistence.Embeddable;

@Embeddable
public class Address {
    private String street;
    private int homeNumber;
    private String city;
    private int postalCode;
    private String country;

    // GETTERS AND SETTERS

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHomeNumber() {
        return homeNumber;
    }

    public void setHomeNumber(int homeNumber) {
        this.homeNumber = homeNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", homeNumber=" + homeNumber +
                ", city='" + city + '\'' +
                ", postalCode=" + postalCode +
                ", country='" + country + '\'' +
                '}';
    }
}
