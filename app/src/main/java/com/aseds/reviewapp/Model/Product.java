package com.aseds.reviewapp.Model;

import java.io.Serializable;

public class Product implements Serializable {

    private String imageurl;
    private String name;
    private Double price;
    private float rating;
    private String productid;
    private String storeName;
    private String category;
    private String storeLocation;
    private String categorie;

    public Product() {
    }

    public Product(String imageurl, String name, Double price, float rating, String productid, String storeName, String category, String storeLocation, String categorie) {
        this.imageurl = imageurl;
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.productid = productid;
        this.storeName = storeName;
        this.category = category;
        this.storeLocation = storeLocation;
        this.categorie = categorie;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public float getRating() {
        return rating;
    }

    public String getProductid() {
        return productid;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreLocation() {
        return storeLocation;
    }

}
