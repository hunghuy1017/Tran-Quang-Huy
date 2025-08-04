/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Hi
 */
public class GoogleForm {

    private String sub;
private String email;
private String name;
private String given_name;
private String family_name;
private String picture;
private boolean email_verified;


    public GoogleForm() {
    }

    public GoogleForm(String sub, String email, String name, String given_name, String family_name, String picture, boolean email_verified) {
        this.sub = sub;
        this.email = email;
        this.name = name;
        this.given_name = given_name;
        this.family_name = family_name;
        this.picture = picture;
        this.email_verified = email_verified;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGiven_name() {
        return given_name;
    }

    public void setGiven_name(String given_name) {
        this.given_name = given_name;
    }

    public String getFamily_name() {
        return family_name;
    }

    public void setFamily_name(String family_name) {
        this.family_name = family_name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isEmail_verified() {
        return email_verified;
    }

    public void setEmail_verified(boolean email_verified) {
        this.email_verified = email_verified;
    }

    @Override
    public String toString() {
        return "GoogleForm{" + "sub=" + sub + ", email=" + email + ", name=" + name + ", given_name=" + given_name + ", family_name=" + family_name + ", picture=" + picture + ", email_verified=" + email_verified + '}';
    }

    
}
