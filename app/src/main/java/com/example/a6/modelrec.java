package com.example.a6;

public class modelrec {
    String name, surname, phone, email, pimage;

    modelrec(){


    }

    public modelrec(String name, String surname, String phone, String email, String pimage) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.email = email;
        this.pimage = pimage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }
}
