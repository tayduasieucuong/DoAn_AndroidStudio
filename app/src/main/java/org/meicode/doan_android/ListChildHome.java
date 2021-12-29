package org.meicode.doan_android;

public class ListChildHome {
    String idParent;
    String id;
    String CreateTime;
    String Admin;
    String Name;
    Boolean isDropDown;
    public ListChildHome(String idParent, String id, String Name, String CreateTime, String Admin, Boolean isDropDown){
        this.idParent = idParent;
        this.Name = Name;
        this.id = id;
        this.CreateTime = CreateTime;
        this.Admin = Admin;
        this.isDropDown = isDropDown;
    }

    public void setDropDown(Boolean dropDown) {
        isDropDown = dropDown;
    }

    public Boolean getDropDown() {
        return isDropDown;
    }

    public String getName() {
        return Name;
    }

    public String getIdParent() {
        return idParent;
    }

    public String getId() {
        return id;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public String getAdmin() {
        return Admin;
    }
}