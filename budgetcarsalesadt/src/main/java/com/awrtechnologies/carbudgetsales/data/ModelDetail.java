package com.awrtechnologies.carbudgetsales.data;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by awr001 on 13/07/15.
 */

@Table(name = "ModelDetail")

public class ModelDetail extends Model {


    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    @Column(name = "categoryid")

    String categoryid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "name")

    String name;


    public String getVname() {
        return vname;
    }

    public void setVname(String vname) {
        this.vname = vname;
    }

    @Column(name = "vname")

    String vname;

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    @Column(name = "mainId")


    String mainId;


    public static List<ModelDetail> getAll() {
        return new Select().from(ModelDetail.class).execute();
    }

    public static List<ModelDetail> getDetailsbyTitle(String vname) {

        return new Select().from(ModelDetail.class).where("vname=?", vname).execute();
    }
}

