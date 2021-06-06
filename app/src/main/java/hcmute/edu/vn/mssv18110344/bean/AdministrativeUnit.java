package hcmute.edu.vn.mssv18110344.bean;

import java.io.Serializable;

public class AdministrativeUnit implements Serializable {
    private String id;
    private String name;
    private String type;
    private String dependentId;

    public AdministrativeUnit(String id, String name, String type, String dependentId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.dependentId = dependentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDependentId() {
        return dependentId;
    }

    public void setDependentId(String dependentId) {
        this.dependentId = dependentId;
    }
}
