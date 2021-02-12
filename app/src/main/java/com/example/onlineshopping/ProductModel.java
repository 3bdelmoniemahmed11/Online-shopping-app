package com.example.onlineshopping;

public class ProductModel {



    private String ProName;
    private Double ProPrice;
    private int Pro_ID,ProQuantity,CatID;
    private byte[] proImage;
    private String BarCode;
    public ProductModel(String Name,Double Price, int Quantity,byte [] image, int Cat_id,String BCInput)
    {
        this.ProName=Name;
        this.ProPrice=Price;
        this.ProQuantity=Quantity;
        this.proImage=image;
        this.CatID=Cat_id;
        this.BarCode=BCInput;
    }
    public String getProName() {
        return ProName;
    }

    public Double getProPrice() {
        return ProPrice;
    }

    public int getPro_ID() {
        return Pro_ID;
    }

    public int getProQuantity() {
        return ProQuantity;
    }

    public int getCatID() {
        return CatID;
    }

    public byte[] getProImage() {
        return proImage;
    }

    public void setProName(String proName) {
        ProName = proName;
    }

    public void setProPrice(Double proPrice) {
        ProPrice = proPrice;
    }

    public void setPro_ID(int pro_ID) {
        Pro_ID = pro_ID;
    }

    public void setProQuantity(int proQuantity) {
        ProQuantity = proQuantity;
    }

    public void setCatID(int catID) {
        CatID = catID;
    }

    public void setProImage(byte[] proImage) {
        this.proImage = proImage;
    }

    public void setBarCode(String barCode) {
        BarCode = barCode;
    }
    public String getBarCode() {
        return BarCode;
    }


}
