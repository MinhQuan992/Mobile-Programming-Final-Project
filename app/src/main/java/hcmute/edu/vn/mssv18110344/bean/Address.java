package hcmute.edu.vn.mssv18110344.bean;

import java.io.Serializable;

public class Address implements Serializable {
    private int id;
    private String name;
    private String phone;
    private String provinceId;
    private String districtId;
    private String wardId;
    private String street;
    private int userId;

    public Address(int id, String name, String phone, String provinceId, String districtId, String wardId, String street, int userId) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.provinceId = provinceId;
        this.districtId = districtId;
        this.wardId = wardId;
        this.street = street;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getWardId() {
        return wardId;
    }

    public void setWardId(String wardId) {
        this.wardId = wardId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
