package com.awrtechnologies.carbudgetsales.data;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "VehicleInfo")
public class VehicleInfo extends Model{

	
	@Column(name = "vehicleid")
	String vehicleid;
	
	@Column(name = "license")
	String license;
	
	@Column(name = "make")
	String make;
	
	@Column(name = "model")
	String model;
	
	@Column(name = "year")
	String year;
	
	@Column(name = "hashId")
	String hashId;
	
	@Column(name = "userId")
	String userId;
	
	@Column(name = "main")
	String main;
	
	@Column(name = "thumb")
	String thumb;
	
	@Column(name = "type")
	String type;
	
	@Column(name = "usertype")
	String usertype;

	public String getVehicleid() {
		return vehicleid;
	}

	public void setVehicleid(String vehicleid) {
		this.vehicleid = vehicleid;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

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

	public String getHashId() {
		return hashId;
	}

	public void setHashId(String hashId) {
		this.hashId = hashId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMain() {
		return main;
	}

	public void setMain(String main) {
		this.main = main;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getUsertype() {
		return usertype;
	}

	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}

	public static List<VehicleInfo> getAll() {
		return new Select().from(VehicleInfo.class).execute();
	}
	public static VehicleInfo getById(String vehicleid) {
		return new Select().from(VehicleInfo.class)
				.where("vehicleid=?", vehicleid).executeSingle();
	}
	public static VehicleInfo getimages() {
		return new Select().from(VehicleInfo.class).executeSingle();
	}
	
	public static List<VehicleInfo> getAllByUsertype(String usertype) {
		return new Select().from(VehicleInfo.class).where("usertype=?", usertype).execute();
	}

}
