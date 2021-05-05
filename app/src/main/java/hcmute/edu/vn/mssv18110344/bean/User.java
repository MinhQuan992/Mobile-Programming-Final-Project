package hcmute.edu.vn.mssv18110344.bean;

import java.io.Serializable;

public class User implements Serializable {
    int id;
    String fullName;
    String sex;
    String dateOfBirth;
    String phone;
    String email;
    String password;
    byte[] avatar;

    public User(int id, String fullName, String sex, String dateOfBirth, String phone, String email, String password, byte[] avatar) {
        super();
        this.id = id;
        this.fullName = fullName;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
    }

    public User(String fullName, String sex, String dateOfBirth, String phone, String email, String password, byte[] avatar) {
        super();
        this.fullName = fullName;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
}
