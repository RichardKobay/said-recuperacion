package org.example;

public class Professor {
    private String name;
    private int hours;

    public Professor() {
        this.name = "";
        this.hours = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }
}
