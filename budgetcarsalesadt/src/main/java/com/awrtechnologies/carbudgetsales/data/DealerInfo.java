package com.awrtechnologies.carbudgetsales.data;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "DocumentDealerInfo")
public class DealerInfo extends Model {

	@Column(name = "email")
	String email;

	@Column(name = "firstName")
	String firstName;

	@Column(name = "dealershipname")
	String dealershipname;

	@Column(name = "phone")
	String phone;

	@Column(name = "address")
	String address;

	@Column(name = "fbUrl")
	String fbUrl;

	@Column(name = "twitterurl")
	String twitterurl;

	@Column(name = "gplUsUrl")
	String gplUsUrl;

	@Column(name = "diggUrl")
	String diggUrl;

	@Column(name = "youtubeUrl")
	String youtubeUrl;

	@Column(name = "appId")
	String appId;

	@Column(name = "image")
	String image;

	@Column(name = "signUpSource")
	String signUpSource;

	@Column(name = "zipcode")
	String zipcode;

	@Column(name = "createdate")
	String createdate;

	@Column(name = "hashId")
	String hashId;


	public String getHashId() {
		return hashId;
	}

	public void setHashId(String hashId) {
		this.hashId = hashId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getDealershipname() {
		return dealershipname;
	}

	public void setDealershipname(String dealershipname) {
		this.dealershipname = dealershipname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFbUrl() {
		return fbUrl;
	}

	public void setFbUrl(String fbUrl) {
		this.fbUrl = fbUrl;
	}

	public String getTwitterurl() {
		return twitterurl;
	}

	public void setTwitterurl(String twitterurl) {
		this.twitterurl = twitterurl;
	}

	public String getGplUsUrl() {
		return gplUsUrl;
	}

	public void setGplUsUrl(String gplUsUrl) {
		this.gplUsUrl = gplUsUrl;
	}

	public String getDiggUrl() {
		return diggUrl;
	}

	public void setDiggUrl(String diggUrl) {
		this.diggUrl = diggUrl;
	}

	public String getYoutubeUrl() {
		return youtubeUrl;
	}

	public void setYoutubeUrl(String youtubeUrl) {
		this.youtubeUrl = youtubeUrl;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getSignUpSource() {
		return signUpSource;
	}

	public void setSignUpSource(String signUpSource) {
		this.signUpSource = signUpSource;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public static DealerInfo getDealer() {
		return new Select().from(DealerInfo.class).executeSingle();
	}
}
