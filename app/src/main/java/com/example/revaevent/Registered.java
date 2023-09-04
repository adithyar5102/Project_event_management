package com.example.revaevent;

public class Registered {
    public Registered() {
    }

    String uid;
    String events;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEvents() {
        return events;
    }

    public void setEvents(String events) {
        this.events = events;
    }

    public Registered(String uid, String events) {
        this.uid = uid;
        this.events = events;
    }
}
