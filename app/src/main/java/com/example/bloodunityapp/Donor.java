package com.example.bloodunityapp;

public class Donor {
    public String name;
    public String address;
    public String bloodGroup;
    public String age;
    public String weight;

    public Donor() {
        // Default constructor required for calls to DataSnapshot.getValue(Donor.class)
    }

    public Donor(String name, String address, String age, String weight, String bloodGroup) {
        this.name = name;
        this.address = address;
        this.age = age;
        this.weight = weight;
        this.bloodGroup = bloodGroup;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
}