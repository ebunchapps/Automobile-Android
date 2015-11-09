package com.awrtechnologies.carbudgetsales.data;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by m004 on 20/08/15.
 */
@Table(name = "Appointments")
public class Appointments extends Model{

    @Column(name = "appointmentid")

    String appointmentid;

    @Column(name = "user_id")

    String user_id;

    @Column(name = "message")

    String message;

    @Column(name = "make")

    String make;

    @Column(name = "model")

    String model;

    @Column(name = "year")

    String year;

    @Column(name = "car_id")

    String car_id;

    @Column(name = "request_timestamp")

    String request_timestamp;

    @Column(name = "completed_timestamp")

    String completed_timestamp;

    @Column(name = "appid")

    String appid;

    @Column(name = "status")

    String status;


    @Column(name = "result")

    String result;


    public String getAppointmentid() {
        return appointmentid;
    }

    public void setAppointmentid(String appointmentid) {
        this.appointmentid = appointmentid;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getCompleted_timestamp() {
        return completed_timestamp;
    }

    public void setCompleted_timestamp(String completed_timestamp) {
        this.completed_timestamp = completed_timestamp;
    }

    public String getRequest_timestamp() {
        return request_timestamp;
    }

    public void setRequest_timestamp(String request_timestamp) {
        this.request_timestamp = request_timestamp;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public static List<Appointments> getAllAppointments() {
        return new Select().from(Appointments.class).execute();
    }

    public static Appointments getAppointmentsById(String appointmentid) {
        return new Select().from(Appointments.class)
                .where("appointmentid=?", appointmentid).executeSingle();
    }
}
