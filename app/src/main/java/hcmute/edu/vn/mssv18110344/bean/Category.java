package hcmute.edu.vn.mssv18110344.bean;

import java.io.Serializable;

public class Category implements Serializable {
    int id;
    String name;

    public Category(int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public Category(String name) {
        this.name = name;
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
}
