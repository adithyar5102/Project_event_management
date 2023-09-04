package com.example.revaevent;

public class Event_details {

    Event_details(){}
    public Event_details(String category, String date, String description, String name, String url, String venue,String limit) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.venue = venue;
        this.date = date;
        this.url = url;
        this.limit=limit;
    }
    String limit;

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    String name;
    String description;
    String venue;
    String date;
    String url;

}
