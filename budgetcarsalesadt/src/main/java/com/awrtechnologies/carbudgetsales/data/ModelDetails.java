package com.awrtechnologies.carbudgetsales.data;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "ModelDetails")
public class ModelDetails extends Model {

	@Column(name = "detailId")
	String detailId;

	@Column(name = "title")
	String title;

	@Column(name = "imageMain")
	String imageMain;

	@Column(name = "imageThumb")
	String imageThumb;

	@Column(name = "imageType")
	String imageType;

	@Column(name = "modelId")
	String modelId;

	@Column(name = "parentid")
	private
	String parentId;
	
	
	public String getDetailId() {
		return detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImageMain() {
		return imageMain;
	}

	public void setImageMain(String imageMain) {
		this.imageMain = imageMain;
	}

	public String getImageThumb() {
		return imageThumb;
	}

	public void setImageThumb(String imageThumb) {
		this.imageThumb = imageThumb;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

}
