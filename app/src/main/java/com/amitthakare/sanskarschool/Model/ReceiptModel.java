package com.amitthakare.sanskarschool.Model;

public class ReceiptModel {

    String Name,Img;

    public ReceiptModel() {
    }

    public ReceiptModel(String name, String img) {
        Name = name;
        Img = img;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImg() {
        return Img;
    }

    public void setImg(String img) {
        Img = img;
    }
}
