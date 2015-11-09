package com.awrtechnologies.carbudgetsales.data;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "DocumentMake")
public class DocumentMake extends Model {

	@Column(name = "makeId")
	String makeId;

	@Column(name = "name")
	String name;

	@Column(name = "image")
	String image;

	@Column(name = "userId")
	String userId;

	@Column(name = "createDate")
	String createDate;

	@Column(name = "appId")
	String appId;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMakeId() {
		return makeId;
	}

	public void setMakeId(String makeId) {
		this.makeId = makeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public static List<DocumentMake> getAll() {

		return new Select().from(DocumentMake.class).execute();
	}

}
