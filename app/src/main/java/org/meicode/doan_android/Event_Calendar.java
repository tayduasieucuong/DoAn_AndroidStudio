package org.meicode.doan_android;

import java.util.Date;

public class Event_Calendar {
    Date date;
    String name;
    String mess;

    public Event_Calendar(Date date, String name, String mess) {
        this.date = date;
        this.name = name;
        this.mess = mess;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    @Override
    public String toString() {
        return "Event_Calendar{" +
                "date='" + date + '\'' +
                ", name='" + name + '\'' +
                ", mess='" + mess + '\'' +
                '}';
    }
}
