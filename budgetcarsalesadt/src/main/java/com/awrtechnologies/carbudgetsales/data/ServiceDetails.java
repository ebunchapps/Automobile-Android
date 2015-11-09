package com.awrtechnologies.carbudgetsales.data;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by awr001 on 04/07/15.
 */

@Table(name = "ServiceDetails")
public class ServiceDetails  extends Model {

    @Column(name = "make")
    String make;

    @Column(name = "model")
    String model;

    @Column(name = "year")
    String year;

    @Column(name = "kms")
    String kms;

    @Column(name = "status")
    String status;

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Column(name = "rating")
    String rating;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    @Column(name = "ids")
    String ids;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "description")
    String description;

    public String getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    @Column(name = "estimatedTime")
    String estimatedTime;


    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getKms() {
        return kms;
    }

    public void setKms(String kms) {
        this.kms = kms;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static List<ServiceDetails> getServiceDetails() {

        return new Select().from(ServiceDetails.class).execute();
    }
    public static List<ServiceDetails> getServiceDetailsByPending(String status)  {

        return new Select().from(ServiceDetails.class).where("status=?", status).execute();
    }
    public static List<ServiceDetails> getServiceDetailsByInProgress(String status)  {

        return new Select().from(ServiceDetails.class).where("status=?", status).execute();
    }
    public static List<ServiceDetails> getServiceDetailsByComplted(String status)  {

        return new Select().from(ServiceDetails.class).where("status=?", status).execute();
    }

}
