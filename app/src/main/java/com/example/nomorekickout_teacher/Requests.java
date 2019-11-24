package com.example.nomorekickout_teacher;

public class Requests {
    private String RID, ID, building, room, name, noAlart, requestType;

    public String getRID() {
        return RID;
    }

    public void setRID() {
        this.RID=RID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoAlart() {
        return noAlart;
    }

    public void setNoAlart(String noAlart) {
        this.noAlart = noAlart;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    @Override
    public String toString() {
        return "Requests{" +
                "ID='" + ID + '\'' +
                ", building='" + building + '\'' +
                ", room='" + room + '\'' +
                ", name='" + name + '\'' +
                ", noAlart='" + noAlart + '\'' +
                ", requestType='" + requestType + '\'' +
                '}';
    }
}
