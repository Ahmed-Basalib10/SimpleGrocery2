package com.e.simplegrocery.model;

public class Items {
    public String fname;
    public String fprice;
    public String fdesc;
    public String fimage;

    public Items() {
    }

    public Items(String fname, String fprice, String fdesc,String fimage) {
        this.fname = fname;
        this.fprice = fprice;
        this.fdesc = fdesc;
        this.fimage = fimage;
    }

    public String getFdesc() {
        return fdesc;
    }

    public void setFdesc(String fdesc) {
        this.fdesc = fdesc;
    }

    public String getFimage() {
        return fimage;
    }

    public void setFimage(String fimage) {
        this.fimage = fimage;
    }

    public String getFname() {
        return fname;
    }

    public String getFprice() {
        return fprice;
    }


    public void setFname(String fname) {
        this.fname = fname;
    }

    public void setFprice(String fprice) {
        this.fprice = fprice;
    }

}
