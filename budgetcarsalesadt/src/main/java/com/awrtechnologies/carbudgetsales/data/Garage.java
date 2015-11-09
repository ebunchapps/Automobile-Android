package com.awrtechnologies.carbudgetsales.data;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Garage")
public class Garage extends Model {

	@Column(name = "dealId")
	String dealId;

	@Column(name = "name")
	String name;

	@Column(name = "price")
	String price;

	@Column(name = "description")
	String description;

	@Column(name = "createDate")
	String createDate;

	@Column(name = "userId")
	String userId;

	@Column(name = "validFrom")
	String validFrom;

	@Column(name = "validTo")
	String validTo;

	@Column(name = "categoryId")
	String categoryId;

	@Column(name = "vehicleDriven")
	String vehicleDriven;

	@Column(name = "categoryName")
	String categoryName;

	@Column(name = "imagesMain")
	String imagesMain;

	@Column(name = "imagesThumb")
	String imagesThumb;

	@Column(name = "imagesType")
	String imagesType;

	@Column(name = "kms")
	String kms;


	public String getKms() {
		return kms;
	}

	public void setKms(String kms) {
		this.kms = kms;
	}

	public String getDealId() {
		return dealId;
	}

	public void setDealId(String dealId) {
		this.dealId = dealId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}

	public String getValidTo() {
		return validTo;
	}

	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getVehicleDriven() {
		return vehicleDriven;
	}

	public void setVehicleDriven(String vehicleDriven) {
		this.vehicleDriven = vehicleDriven;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getImagesMain() {
		return imagesMain;
	}

	public void setImagesMain(String imagesMain) {
		this.imagesMain = imagesMain;
	}

	public String getImagesThumb() {
		return imagesThumb;
	}

	public void setImagesThumb(String imagesThumb) {
		this.imagesThumb = imagesThumb;
	}

	public String getImagesType() {
		return imagesType;
	}

	public void setImagesType(String imagesType) {
		this.imagesType = imagesType;
	}

	public static List<Garage> getAll() {

		return new Select().from(Garage.class).execute();
	}

	public static Garage getAllByDealId(String dealId) {
		return new Select().from(Garage.class).where("dealId=?", dealId)
				.executeSingle();
	}

}
