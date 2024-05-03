package com.example.bloodunityapp;

public class Donor {
    public String name;
    public String address;
    public String age;
    public String weight;
    public String bloodgroup;

    public Donor() {
        // Default constructor required for calls to DataSnapshot.getValue(Donor.class)
    }

    public Donor(String name, String address, String age, String weight, String bloodgroup) {
        this.name = name;
        this.address = address;
        this.age = age;
        this.weight = weight;
        this.bloodgroup = bloodgroup;
    }
}
