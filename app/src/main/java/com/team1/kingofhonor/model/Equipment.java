package com.team1.kingofhonor.model;

import java.io.Serializable;

public class Equipment implements Serializable {
    private int image;
    private String name;
    private String property;
    private String process;

    public Equipment(){}

    public Equipment(String name, String property, String process, int image){
        this.image = image;
        this.name = name;
        this.property = property;
        this.process = process;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getProcess() {
        return process;
    }

    public String getProperty() {
        return property;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}
