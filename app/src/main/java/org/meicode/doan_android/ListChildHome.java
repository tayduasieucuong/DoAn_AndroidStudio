package org.meicode.doan_android;

public class ListChildHome {
    String idParent;
    String id;
    String CreateTime;
    String Admin;
    String Name;
    String isDone;
    public ListChildHome(String idParent, String id, String Name, String CreateTime, String Admin, String isDone){
        this.idParent = idParent;
        this.Name = Name;
        this.id = id;
        this.CreateTime = CreateTime;
        this.Admin = Admin;
        this.isDone = isDone;
    }

    public void setIsDone(String isDone) {
        isDone = isDone;
    }

    public String getIsDone() {
        return isDone;
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