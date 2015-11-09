package com.awrtechnologies.carbudgetsales.data;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by awr001 on 13/07/15.
 */

@Table(name = "ServiceDetail")

public class ServiceDetail extends Model {


    @Column(name = "sId")

    String sId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getKms() {
        return kms;
    }

    public void setKms(String kms) {
        this.kms = kms;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    @Column(name = "type")


    String type;

    @Column(name = "month")


    String month;

    @Column(name = "kms")


    String kms;

    @Column(name = "vehicleId")


    String vehicleId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Column(name = "description")


    String description;

    @Column(name = "price")


    String price;





    @Column(name = "pdf")


    String pdf;


    public static List<ServiceDetail> getAll() {
        return new Select().from(ServiceDetail.class).execute();
    }

    public static ServiceDetail getServiceByType(String type)  {

        return new Select().from(ServiceDetail.class).where("type=?", type).executeSingle();
    }

    public static ServiceDetail getAllService() {
        return new Select().from(ServiceDetail.class).executeSingle();
    }

    public static List<ServiceDetail> getServicesbyVehicleId(String vehicleId)  {

        return new Select().from(ServiceDetail.class).where("vehicleId=?", vehicleId).execute();
    }
}