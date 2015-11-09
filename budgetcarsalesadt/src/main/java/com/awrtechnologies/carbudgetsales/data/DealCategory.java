package com.awrtechnologies.carbudgetsales.data;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "DealCategory")
public class DealCategory extends Model {

	@Column(name = "dealId")
	String dealId;

	@Column(name = "name")
	String name;

	@Column(name = "createdate")
	String createdate;

	@Column(name = "userid")
	String userid;

	@Column(name = "image")
	String image;

	@Column(name = "mainImage")
	String mainImage;

	@Column(name = "thumbImage")
	String thumbImage;

	@Column(name = "dealCount")
	String dealCount;

	@Column(name = "appid")
	String appid;

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
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

	public String getCreateDate() {
		return createdate;
	}

	public void setCreateDate(String createdate) {
		this.createdate = createdate;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getMainImage() {
		return mainImage;
	}

	public void setMainImage(String mainImage) {
		this.mainImage = mainImage;
	}

	public String getThumbImage() {
		return thumbImage;
	}

	public void setThumbImage(String thumbImage) {
		this.thumbImage = thumbImage;
	}

	public String getDealCount() {
		return dealCount;
	}

	public void setDealCount(String dealCount) {
		this.dealCount = dealCount;
	}

	public static List<DealCategory> getAll() {
		return new Select().from(DealCategory.class).execute();
	}

}
