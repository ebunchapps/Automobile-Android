package com.awrtechnologies.carbudgetsales.data;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by awr001 on 13/07/15.
 */

@Table(name = "VehiclesDetail")

public class VehiclesDetail extends Model {


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "name")

    String name;

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    @Column(name = "mainId")


    String mainId;

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    @Column(name = "pdf")


    String pdf;




    public static List<VehiclesDetail> getAll() {
        return new Select().from(VehiclesDetail.class).execute();
    }

    public static List<VehiclesDetail> getVehicleByModelId(String mainId)  {

        return new Select().from(VehiclesDetail.class).where("mainId=?", mainId).execute();
    }
}



