package com.softeem.pojo;

import java.io.Serializable;

public class Address implements Serializable {

    private Integer id;
    private String addressName;   //体检机构名
    private String telephone; //联系电话
    private String lngAndLat; //经纬度
//    private String lat; //纬度

    private String img;
    private String map;     //地图
    private String detailaddress; //详细地址

    public Address() {
    }

    public String getLngAndLat() {
        return lngAndLat;
    }

    public void setLngAndLat(String lngAndLat) {
        this.lngAndLat = lngAndLat;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getDetailaddress() {
        return detailaddress;
    }

    public void setDetailaddress(String detailaddress) {
        this.detailaddress = detailaddress;
    }
}

