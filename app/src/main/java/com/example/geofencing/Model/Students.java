package com.example.geofencing.Model;

public class Students {

    private String matric, password;

    public Students() {
    }

    public Students(String matric, String password) {
        this.matric = matric;
        this.password = password;
    }

    public String getMatric() {
        return matric;
    }

    public void setMatric(String matric) {
        this.matric = matric;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
