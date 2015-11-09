package com.awrtechnologies.carbudgetsales.data;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "DocumentModelDetails")
public class DocumentModelDetails extends Model {

	@Column(name = "modelDetailId")
	String modelDetailId;

	@Column(name = "title")
	String title;

	@Column(name = "imageMain")
	String imageMain;

	@Column(name = "imageThumb")
	String imageThumb;

	@Column(name = "modelId")
	String modelid;

	@Column(name = "type")
	private String type;


	@Column(name = "attachment_name")
	String attachment_name;

	@Column(name = "link")
	private String link;


	public String getAttachment_name() {
		return attachment_name;
	}

	public void setAttachment_name(String attachment_name) {
		this.attachment_name = attachment_name;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getModelDetailId() {
		return modelDetailId;
	}

	public void setModelDetailId(String modelDetailId) {
		this.modelDetailId = modelDetailId;
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

	public String getModelid() {
		return modelid;
	}

	public void setModelid(String modelid) {
		this.modelid = modelid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public static List<DocumentModelDetails> getAllByModelId(String id) {
		return new Select().from(DocumentModelDetails.class)
				.where("modelId=?", id).execute();
	}

	public static DocumentModelDetails getByModelId(String modelId) {
		return new Select().from(DocumentModelDetails.class)
				.where("modelId=?", modelId).executeSingle();
	}
}
