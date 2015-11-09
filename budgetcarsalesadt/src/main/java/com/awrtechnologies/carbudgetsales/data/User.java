package com.awrtechnologies.carbudgetsales.data;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "User")
public class User extends Model {
	@Column(name = "userid")
	String userid;

	@Column(name = "name")
	String name;

	@Column(name = "email")
	String email;

	@Column(name = "phone")
	String phone;

	@Column(name = "zipcode")
	String zipcode;

	@Column(name = "address")
	String address;

	@Column(name = "date")
	String date;
	
	@Column(name = "hashid")
	String hashid;

	
	
	public String getHashid() {
		return hashid;
	}

	public void setHashid(String hashid) {
		this.hashid = hashid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public static User getUser() {
		return new Select().from(User.class).executeSingle();
	}
	public List<User> getAll() {
		return new Select().from(User.class).execute();
	}
}
