package com.example.keepfitapp;

public class aModel {
    private String age, weight, height, bmi, id, date;

    public aModel(){
    }

    public aModel(String age, String weight, String height, String bmi, String id, String date) {
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.bmi = bmi;
        this.id = id;
        this.date = date;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
