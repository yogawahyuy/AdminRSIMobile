package com.rsip.adminrsimoblie.Model;

public class UserModel {

    private String username;
    private String id;
    private String ImageURL;
    private String email;
    private String alamat;
    private String jk;
    private String numberPhone;

    public UserModel(String username, String id, String imageURL, String email, String alamat, String jk, String numberPhone) {
        this.username = username;
        this.id = id;
        ImageURL = imageURL;
        this.email = email;
        this.alamat = alamat;
        this.jk = jk;
        this.numberPhone = numberPhone;
    }

    public UserModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getJk() {
        return jk;
    }

    public void setJk(String jk) {
        this.jk = jk;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }
}
