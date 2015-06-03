package com.curso.androidt.earthquake;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by davasens on 5/22/2015.
 */
public class QuakeDto implements Serializable {

    private Float magnitude;
    private Date date;

    private String sort;
    private String dir;

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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
