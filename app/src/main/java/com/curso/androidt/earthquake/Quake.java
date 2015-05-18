package com.curso.androidt.earthquake;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by androidt on 14/05/2015.
 */
public class Quake implements Serializable {

    private Long id;
    private String title;
    private String link;
    private Float magnitude;
    private Date date;
    private Float longitude;
    private Float latitude;

    public Quake() {
    }

    public Quake(String title, String link, Float magnitude, Date date, Float longitude, Float latitude) {
        this.title = title;
        this.link = link;
        this.magnitude = magnitude;
        this.date = date;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    //Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Float getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(Float magnitude) {
        this.magnitude = magnitude;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        return result.append(String.valueOf(id)).append(" - ").append(title).append(" - ").append(String.valueOf(magnitude)).append(" - ").append(link).toString();
    }
}
