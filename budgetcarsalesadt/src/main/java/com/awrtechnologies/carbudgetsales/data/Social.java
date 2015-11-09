package com.awrtechnologies.carbudgetsales.data;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "DocumentSocial")
public class Social extends Model {

	@Column(name = "facebook")
	String facebook;

	@Column(name = "gPlus")
	String gPlus;

	@Column(name = "com/awrtechnologies/carbudgetsales/twitter")
	String twitter;

	@Column(name = "diggUrl")
	String diggUrl;

	@Column(name = "youtube")
	String youtube;

	public static List<Social> getAll() {

		return new Select().from(Social.class).execute();
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getgPlus() {
		return gPlus;
	}

	public void setgPlus(String gPlus) {
		this.gPlus = gPlus;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getDiggUrl() {
		return diggUrl;
	}

	public void setDiggUrl(String diggUrl) {
		this.diggUrl = diggUrl;
	}

	public String getYoutube() {
		return youtube;
	}

	public void setYoutube(String youtube) {
		this.youtube = youtube;
	}

	public static Social getSocial() {
		return new Select().from(Social.class).executeSingle();
	}
	
}
