package com.awrtechnologies.carbudgetsales.data;

import java.util.List;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "Inventory")
public class Inventory extends Model {

	@Column(name = "inventoryId")
	String inventoryId;

	@Column(name = "userId")
	String userId;

	@Column(name = "adid")
	String adid;

	@Column(name = "companyId")
	String companyId;

	@Column(name = "companyName")
	String companyName;

	@Column(name = "category")
	String category;

	@Column(name = "stockNumber")
	String stockNumber;

	@Column(name = "vin")
	String vin;

	@Column(name = "status")
	String status;

	@Column(name = "year")
	String year;

	@Column(name = "make")
	String make;

	@Column(name = "model")
	String model;

	@Column(name = "trim")
	String trim;

	@Column(name = "kms")
	String kms;

	@Column(name = "exteriorColor")
	String exteriorColor;

	@Column(name = "mfgExteriorColor")
	String mfgExteriorColor;

	@Column(name = "interiorColor")
	String interiorColor;

	@Column(name = "fuelType")
	String fuelType;

	@Column(name = "drive")
	String drive;

	@Column(name = "engineSize")
	String engineSize;

	@Column(name = "transmission")
	String transmission;

	@Column(name = "doors")
	String doors;

	@Column(name = "passenger")
	String passenger;

	@Column(name = "cylinder")
	String cylinder;

	@Column(name = "body")
	String body;

	@Column(name = "price")
	String price;

	@Column(name = "hidePrice")
	String hidePrice;

	@Column(name = "options")
	String options;

	@Column(name = "addescription")
	String addescription;

	@Column(name = "financingIsAvailable")
	String financingIsAvailable;

	@Column(name = "financingPayment")
	String financingPayment;

	@Column(name = "financingPaymenttype")
	String financingPaymenttype;

	@Column(name = "financingNumberOfPayment")
	String financingNumberOfPayment;

	@Column(name = "financingDownPayment")
	String financingDownPayment;

	@Column(name = "financingSource")
	String financingSource;

	@Column(name = "financingType")
	String financingType;

	@Column(name = "financingOdometer")
	String financingOdometer;

	@Column(name = "financingDescription")
	String financingDescription;

	@Column(name = "manufactureProgram")
	String manufactureProgram;

	@Column(name = "warranty")
	String warranty;

	@Column(name = "warrantyDescription")
	String warrantyDescription;

	@Column(name = "imagesMain")
	String imagesMain;

	@Column(name = "imagesThumb")
	String imagesThumb;

	@Column(name = "modifiedDate")
	String modifiedDate;

	@Column(name = "createdDate")
	String createdDate;

	public String getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAdid() {
		return adid;
	}

	public void setAdid(String adid) {
		this.adid = adid;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getStockNumber() {
		return stockNumber;
	}

	public void setStockNumber(String stockNumber) {
		this.stockNumber = stockNumber;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getTrim() {
		return trim;
	}

	public void setTrim(String trim) {
		this.trim = trim;
	}

	public String getKms() {
		return kms;
	}

	public void setKms(String kms) {
		this.kms = kms;
	}

	public String getExteriorColor() {
		return exteriorColor;
	}

	public void setExteriorColor(String exteriorColor) {
		this.exteriorColor = exteriorColor;
	}

	public String getMfgExteriorColor() {
		return mfgExteriorColor;
	}

	public void setMfgExteriorColor(String mfgExteriorColor) {
		this.mfgExteriorColor = mfgExteriorColor;
	}

	public String getInteriorColor() {
		return interiorColor;
	}

	public void setInteriorColor(String interiorColor) {
		this.interiorColor = interiorColor;
	}

	public String getFuelType() {
		return fuelType;
	}

	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}

	public String getDrive() {
		return drive;
	}

	public void setDrive(String drive) {
		this.drive = drive;
	}

	public String getEngineSize() {
		return engineSize;
	}

	public void setEngineSize(String engineSize) {
		this.engineSize = engineSize;
	}

	public String getTransmission() {
		return transmission;
	}

	public void setTransmission(String transmission) {
		this.transmission = transmission;
	}

	public String getDoors() {
		return doors;
	}

	public void setDoors(String doors) {
		this.doors = doors;
	}

	public String getPassenger() {
		return passenger;
	}

	public void setPassenger(String passenger) {
		this.passenger = passenger;
	}

	public String getCylinder() {
		return cylinder;
	}

	public void setCylinder(String cylinder) {
		this.cylinder = cylinder;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getHidePrice() {
		return hidePrice;
	}

	public void setHidePrice(String hidePrice) {
		this.hidePrice = hidePrice;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	public String getAddescription() {
		return addescription;
	}

	public void setAddescription(String addescription) {
		this.addescription = addescription;
	}

	public String getFinancingIsAvailable() {
		return financingIsAvailable;
	}

	public void setFinancingIsAvailable(String financingIsAvailable) {
		this.financingIsAvailable = financingIsAvailable;
	}

	public String getFinancingPayment() {
		return financingPayment;
	}

	public void setFinancingPayment(String financingPayment) {
		this.financingPayment = financingPayment;
	}

	public String getFinancingPaymenttype() {
		return financingPaymenttype;
	}

	public void setFinancingPaymenttype(String financingPaymenttype) {
		this.financingPaymenttype = financingPaymenttype;
	}

	public String getFinancingNumberOfPayment() {
		return financingNumberOfPayment;
	}

	public void setFinancingNumberOfPayment(String financingNumberOfPayment) {
		this.financingNumberOfPayment = financingNumberOfPayment;
	}

	public String getFinancingDownPayment() {
		return financingDownPayment;
	}

	public void setFinancingDownPayment(String financingDownPayment) {
		this.financingDownPayment = financingDownPayment;
	}

	public String getFinancingSource() {
		return financingSource;
	}

	public void setFinancingSource(String financingSource) {
		this.financingSource = financingSource;
	}

	public String getFinancingType() {
		return financingType;
	}

	public void setFinancingType(String financingType) {
		this.financingType = financingType;
	}

	public String getFinancingOdometer() {
		return financingOdometer;
	}

	public void setFinancingOdometer(String financingOdometer) {
		this.financingOdometer = financingOdometer;
	}

	public String getFinancingDescription() {
		return financingDescription;
	}

	public void setFinancingDescription(String financingDescription) {
		this.financingDescription = financingDescription;
	}

	public String getManufactureProgram() {
		return manufactureProgram;
	}

	public void setManufactureProgram(String manufactureProgram) {
		this.manufactureProgram = manufactureProgram;
	}

	public String getWarranty() {
		return warranty;
	}

	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}

	public String getWarrantyDescription() {
		return warrantyDescription;
	}

	public void setWarrantyDescription(String warrantyDescription) {
		this.warrantyDescription = warrantyDescription;
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

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public static List<Inventory> getAll() {
		return new Select().from(Inventory.class).execute();
	}


	public static Inventory getAllData() {
		return new Select().from(VehicleInfo.class).executeSingle();
	}

	public static Inventory getById(int inventoryId) {
		return new Select().from(Inventory.class)
				.where("inventoryId=?", inventoryId).executeSingle();
	}
}