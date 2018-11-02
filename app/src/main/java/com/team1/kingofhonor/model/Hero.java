package com.team1.kingofhonor.model;

import com.team1.kingofhonor.R;

import java.io.Serializable;

//英雄的属性
//还没修改好

public class Hero implements Serializable{
    private int image;
    private String name;
    private String alias;
    private String category;//1.法师 2.刺客 3.射手 4.辅助 5.战士 6.坦克

    public Hero(){}

    public Hero(String name, int image, String alias, String category) {
        super();
        this.name = name;
        this.alias = alias;
        this.image = image;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}