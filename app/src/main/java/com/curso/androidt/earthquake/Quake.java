package com.curso.androidt.earthquake;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by androidt on 14/05/2015.
 */
public class Quake implements Serializable {

    private String id;
    private String title;
    private String link;
    private Float magnitude;
    private Date date;
    private Float longitude;
    private Float latitude;
    private Float elevation;
    private Float proximity; //Km
    private Integer bearingAngle; //Degrees

    public Quake() {
    }

    public Quake(String id, String title, String link, Date date, Float magnitude, Float latitude, Float longitude) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.magnitude = magnitude;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //Getters & Setters


    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Float getElevation() {
        return elevation;
    }

    public void setElevation(Float elevation) {
        this.elevation = elevation;
    }

    public Float getProximity() {
        return proximity;
    }

    public void setProximity(Float proximity) {
        this.proximity = proximity;
    }

    public Integer getBearingAngle() {
        return bearingAngle;
    }

    public void setBearingAngle(Integer bearingAngle) {
        this.bearingAngle = bearingAngle;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        return result.append("M ".concat(String.valueOf(magnitude))).append(" - ").append(title).append(" - ").append(String.valueOf(magnitude)).append(" - ").append(String.format("[%.6f, %.6f]",latitude, longitude)).append(" - ").append(link).toString();
    }
}
