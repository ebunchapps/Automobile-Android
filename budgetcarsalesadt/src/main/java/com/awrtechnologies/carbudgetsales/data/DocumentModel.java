package com.awrtechnologies.carbudgetsales.data;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "DocumentModel")
public class DocumentModel extends Model {

	@Column(name = "modelId")
	String modelId;

	@Column(name = "title")
	String title;

	@Column(name = "makeId")
	String makeId;

	@Column(name = "userId")
	String userId;

	@Column(name = "createDate")
	String createDate;

	@Column(name = "parentid")
	String parentId;

	@Column(name = "details")
	private String details;

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMakeId() {
		return makeId;
	}

	public void setMakeId(String makeId) {
		this.makeId = makeId;
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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public static List<DocumentModel> getAllByMakeId(int makeid) {
		return new Select().from(DocumentModel.class).where("makeId=?", makeid)
				.execute();
	}

	public static List<DocumentModel> getAll() {
		return new Select().from(DocumentModel.class)
				.execute();
	}
}
