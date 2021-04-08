package com.e.simplegrocery.model;

public class Cart {
    public String foodName;
    public String foodPrice;
    public String foodDesc;
    public String foodImage;
    public String foodQuantity;

    public Cart() {
    }

    public Cart(String foodName, String foodPrice, String foodDesc, String foodImage, String foodQuantity) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodDesc = foodDesc;
        this.foodImage = foodImage;
        this.foodQuantity = foodQuantity;
    }

    public String getFoodName() {
        return foodName;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public String getFoodDesc() {
        return foodDesc;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public String getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

    public void setFoodDesc(String foodDesc) {
        this.foodDesc = foodDesc;
    }

    public void setFoodImage(String foodImage) {
        this.foodImage = foodImage;
    }

    public void setFoodQuantity(String foodQuantity) {
        this.foodQuantity = foodQuantity;
    }
}
