package com.awrtechnologies.carbudgetsales.data;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by m004 on 20/08/15.
 */

@Table(name = "Lanes")
public class Lanes extends Model {

    @Column(name = "laneid")

    String laneid;

    @Column(name = "appid")

    String appid;

    @Column(name = "lanename")

    String lanename;

    @Column(name = "booked_until")

    String booked_until;

    @Column(name = "service_request_id")

    String service_request_id;

    public String getLaneid() {
        return laneid;
    }

    public void setLaneid(String laneid) {
        this.laneid = laneid;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getLanename() {
        return lanename;
    }

    public void setLanename(String lanename) {
        this.lanename = lanename;
    }

    public String getBooked_until() {
        return booked_until;
    }

    public void setBooked_until(String booked_until) {
        this.booked_until = booked_until;
    }

    public String getService_request_id() {
        return service_request_id;
    }

    public void setService_request_id(String service_request_id) {
        this.service_request_id = service_request_id;
    }

    public static List<Lanes> getAllLanes() {
        return new Select().from(Lanes.class).execute();
    }

}
