package com.awrtechnologies.carbudgetsales.data;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by awr001 on 13/07/15.
 */

@Table(name = "YearDetail")
public class YearDetail extends Model {

    @Column(name = "name")
    String name;

    @Column(name = "pdf")
    String pdf;
    @Column(name = "mainId")
    String mainId;

    @Column(name = "modelId")
    String modelId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }
    

    public static List<YearDetail> getAll() {
        return new Select().from(YearDetail.class).execute();
    }

    public static List<YearDetail> getYearByModelId(String modelId) {

        return new Select().from(YearDetail.class).where("modelId=?", modelId).execute();
    }

}


