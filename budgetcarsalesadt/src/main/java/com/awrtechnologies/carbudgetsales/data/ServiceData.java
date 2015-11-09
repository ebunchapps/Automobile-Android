package com.awrtechnologies.carbudgetsales.data;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "ServiceData")
public class ServiceData extends Model {

	public ServiceData() {
		super();
	}

	@Column(name = "servicesid")
	String sid;

	@Column(name = "serviceyear")
	String syear;

	@Column(name = "servicemodel")
	String smodel;

	@Column(name = "servicemake")
	String smake;

	@Column(name = "serviceappointmentate")
	String sappointmentDate;

	@Column(name = "servicedescription")
	String servicedescription;

	@Column(name = "servicestatus")
	String servicestatus;

	@Column(name = "servicedealerUserId")
	String dealerUserId;

	@Column(name = "servicedealerId")
	String dealerId;

	@Column(name = "servicecreateDate")
	String createDate;

	@Column(name = "serviceuserEmail")
	String userEmail;

	@Column(name = "serviceuserPhone")
	String userPhone;

	@Column(name = "serviceuserName")
	String userName;

	@Column(name = "servicelogId")
	String servicelogId;

	@Column(name = "servicelogComment")
	String logComment;

	@Column(name = "servicelogDate")
	String servicelogDate;

	@Column(name = "servicelogStatus")
	String servicelogStatus;

	@Column(name = "servicelogString")
	String logString;

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getYear() {
		return syear;
	}

	public void setYear(String year) {
		this.syear = year;
	}

	public String getModel() {
		return smodel;
	}

	public void setModel(String model) {
		this.smodel = model;
	}

	public String getMake() {
		return smake;
	}

	public void setMake(String make) {
		this.smake = make;
	}

	public String getAppointmentDate() {
		return sappointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.sappointmentDate = appointmentDate;
	}

	public String getDescription() {
		return servicedescription;
	}

	public void setDescription(String description) {
		this.servicedescription = description;
	}

	public String getStatus() {
		return servicestatus;
	}

	public void setStatus(String status) {
		this.servicestatus = status;
	}

	public String getDealerUserId() {
		return dealerUserId;
	}

	public void setDealerUserId(String dealerUserId) {
		this.dealerUserId = dealerUserId;
	}

	public String getDealerId() {
		return dealerId;
	}

	public void setDealerId(String dealerId) {
		this.dealerId = dealerId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLogId() {
		return servicelogId;
	}

	public void setLogId(String logId) {
		this.servicelogId = logId;
	}

	public String getLogComment() {
		return logComment;
	}

	public void setLogComment(String logComment) {
		this.logComment = logComment;
	}

	public String getLogDate() {
		return servicelogDate;
	}

	public void setLogDate(String logDate) {
		this.servicelogDate = logDate;
	}

	public String getLogStatus() {
		return servicelogStatus;
	}

	public void setLogStatus(String logStatus) {
		this.servicelogStatus = logStatus;
	}

	public String getLogString() {
		return logString;
	}

	public void setLogString(String logString) {
		this.logString = logString;
	}

	public static List<ServiceData> getAll() {
		return new Select().from(ServiceData.class).execute();
	}
}
