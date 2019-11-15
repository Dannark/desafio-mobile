package br.com.dannark.desafio_mobile_daniel.Entity;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

public class Product {

    //PRODUCT DETAILS
    String name;
    String nameSeller;
    Double price;
    Double listPrice;

    //BEST INSTALLMENT
    String count;
    String value;
    String total;
    String rate;

    Bitmap img;

    public ArrayList<String> imagesURLs;

    public Product(String name, String nameSeller, Double price, Double listPrice, String count,
                   String value, String total, String rate, ArrayList<String> imagesURLs){

        this.name = name;
        this.nameSeller = nameSeller;
        this.price = price;
        this.listPrice = listPrice;
        this.count = count;
        this.value = value;
        this.total = total;
        this.rate = rate;

        this.imagesURLs = imagesURLs;
    }

    public ArrayList<String> getImagesURLs(){
        return imagesURLs;
    }


    public String getName() {
        return name;
    }

    public String getNameSeller() {
        return nameSeller;
    }

    public Double getPrice() {
        return price;
    }

    public Double getListPrice() {
        return listPrice;
    }

    public String getCount() {
        return count;
    }

    public String getValue() {
        return value;
    }

    public String getTotal() {
        return total;
    }

    public String getRate() {
        return rate;
    }


    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
}
