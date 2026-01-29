package com.apm.DiaryManagement.eu.entity;

import java.io.Serializable;
import java.util.ArrayList;

import com.apm.DiaryManagement.web.common.Month;
import com.apm.Registration.eu.entity.Location;

public class DiaryManagement implements Serializable{
	
	private int id;
	
	private boolean show_master;
	
	private String commencing;
	
	private String selectedDiaryUser;
	
	private String location;
	
	private String room;
	
	private String description;
	private int status=0;
	
	private boolean onlineBooking;
	
	private String sTime;
	
	private String endTime;
	
	private String apmtDuration;
	
	private int diarUserid;
	
	private String firstName;
	
	private String lastName;
	
	private String tdCode;
	
	private String weekName;
	
	private String year;
	
	private ArrayList<Tdcode>tdDataList;
	
	private ArrayList<Month>monthtdList;
	
	private String diaryColor;
	
	private String locationColor;
	
	private String weekFullName;
	
	private String diaryUser;
	
	private String locationName;
	
	private String locationString = "";
	
	private String usrPosition;
	
	private ArrayList<Location>multiLicationList;
	
	
	private String disciplineid;
	
	private boolean checked;
	private boolean opd;
	private boolean ipd;
	private boolean emr;
	private boolean pacs;
	private boolean discharge;
	private boolean medicine;
	private boolean investigation;
	private boolean bloodbank;
	private boolean accounts;
	private boolean payroll;
	private boolean expenses;
	private boolean inventory;
	private boolean mis;
	private boolean consultants;
	private boolean patient;
	private boolean appointmentfinder;
	private boolean setting;
	
	//new terms added for personal widget
	private boolean ot;
	private boolean casualty;
	private boolean pharmacy;
	private boolean mrd;
	private boolean marketing;
	private boolean voice_recording;
	
	
	 private boolean packs;
	 private boolean investigation_chart;
	 private boolean sheduler;
	 private boolean housekeeping;
	 private boolean dietery;
	 private boolean cafeteria;
	 private boolean packages;
	 private boolean ambulance;
	 private boolean bank_deposite;
	 private boolean account_reconcilation; 
	
	private String jobtitle;
	
	private String fromdate;
	private String todate;
	private String eventname;
	private String time;
	private String place;
	
	ArrayList<DiaryManagement>rotadataList;
	private String rotadate;
	private boolean indent;
	private boolean daily_opd;
	private boolean indent_approve;
	private boolean cash_desk;
	
	private boolean tpa;
	private boolean nabh_quality;
	
	private boolean doctor_opd;
	private boolean adjustmentaccess;
	private boolean supplier_add;
	private boolean direct_refund_disc;
	private boolean ref_dis_pay;
	private boolean prisc_new_req_access;
	private boolean medicine_barcode;
	private boolean additional_disc;
	private boolean cancel_invoice;
	private boolean payrollaccess;
	
	public boolean isCancel_invoice() {
		return cancel_invoice;
	}

	public void setCancel_invoice(boolean cancel_invoice) {
		this.cancel_invoice = cancel_invoice;
	}

	public boolean isAdditional_disc() {
		return additional_disc;
	}

	public void setAdditional_disc(boolean additional_disc) {
		this.additional_disc = additional_disc;
	}

	public boolean isMedicine_barcode() {
		return medicine_barcode;
	}

	public void setMedicine_barcode(boolean medicine_barcode) {
		this.medicine_barcode = medicine_barcode;
	}

	public boolean isPrisc_new_req_access() {
		return prisc_new_req_access;
	}

	public void setPrisc_new_req_access(boolean prisc_new_req_access) {
		this.prisc_new_req_access = prisc_new_req_access;
	}

	public boolean isRef_dis_pay() {
		return ref_dis_pay;
	}

	public void setRef_dis_pay(boolean ref_dis_pay) {
		this.ref_dis_pay = ref_dis_pay;
	}

	public boolean isDirect_refund_disc() {
		return direct_refund_disc;
	}

	public void setDirect_refund_disc(boolean direct_refund_disc) {
		this.direct_refund_disc = direct_refund_disc;
	}

	public boolean isSupplier_add() {
		return supplier_add;
	}

	public void setSupplier_add(boolean supplier_add) {
		this.supplier_add = supplier_add;
	}

	public boolean isAdjustmentaccess() {
		return adjustmentaccess;
	}

	public void setAdjustmentaccess(boolean adjustmentaccess) {
		this.adjustmentaccess = adjustmentaccess;
	}

	private String emrgency_title,emrgency_data;
	private boolean edit_paypo;
	
	public boolean isEdit_paypo() {
		return edit_paypo;
	}

	public void setEdit_paypo(boolean edit_paypo) {
		this.edit_paypo = edit_paypo;
	}

	public String getEmrgency_title() {
		return emrgency_title;
	}

	public void setEmrgency_title(String emrgency_title) {
		this.emrgency_title = emrgency_title;
	}

	public String getEmrgency_data() {
		return emrgency_data;
	}

	public void setEmrgency_data(String emrgency_data) {
		this.emrgency_data = emrgency_data;
	}

	public boolean isDoctor_opd() {
		return doctor_opd;
	}

	public void setDoctor_opd(boolean doctor_opd) {
		this.doctor_opd = doctor_opd;
	}

	public boolean isTpa() {
		return tpa;
	}

	public void setTpa(boolean tpa) {
		this.tpa = tpa;
	}

	public boolean isNabh_quality() {
		return nabh_quality;
	}

	public void setNabh_quality(boolean nabh_quality) {
		this.nabh_quality = nabh_quality;
	}

	public boolean isIndent_approve() {
		return indent_approve;
	}

	public void setIndent_approve(boolean indent_approve) {
		this.indent_approve = indent_approve;
	}

	public boolean isCash_desk() {
		return cash_desk;
	}

	public void setCash_desk(boolean cash_desk) {
		this.cash_desk = cash_desk;
	}

	public boolean isDaily_opd() {
		return daily_opd;
	}

	public void setDaily_opd(boolean daily_opd) {
		this.daily_opd = daily_opd;
	}

	public boolean isIndent() {
		return indent;
	}

	public void setIndent(boolean indent) {
		this.indent = indent;
	}

	public String getRotadate() {
		return rotadate;
	}

	public void setRotadate(String rotadate) {
		this.rotadate = rotadate;
	}

	public ArrayList<DiaryManagement> getRotadataList() {
		return rotadataList;
	}

	public void setRotadataList(ArrayList<DiaryManagement> rotadataList) {
		this.rotadataList = rotadataList;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getPlace() {
		return place;
	}

	
	
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getFromdate() {
		return fromdate;
	}

	public void setFromdate(String fromdate) {
		this.fromdate = fromdate;
	}

	public String getTodate() {
		return todate;
	}

	public void setTodate(String todate) {
		this.todate = todate;
	}

	public String getEventname() {
		return eventname;
	}

	public void setEventname(String eventname) {
		this.eventname = eventname;
	}

	public String getJobtitle() {
		return jobtitle;
	}

	public void setJobtitle(String jobtitle) {
		this.jobtitle = jobtitle;
	}

	public boolean isOpd() {
		return opd;
	}

	public void setOpd(boolean opd) {
		this.opd = opd;
	}

	public boolean isIpd() {
		return ipd;
	}

	public void setIpd(boolean ipd) {
		this.ipd = ipd;
	}

	public boolean isEmr() {
		return emr;
	}

	public void setEmr(boolean emr) {
		this.emr = emr;
	}

	public boolean isPacs() {
		return pacs;
	}

	public void setPacs(boolean pacs) {
		this.pacs = pacs;
	}

	public boolean isDischarge() {
		return discharge;
	}

	public void setDischarge(boolean discharge) {
		this.discharge = discharge;
	}

	public boolean isMedicine() {
		return medicine;
	}

	public void setMedicine(boolean medicine) {
		this.medicine = medicine;
	}

	public boolean isInvestigation() {
		return investigation;
	}

	public void setInvestigation(boolean investigation) {
		this.investigation = investigation;
	}

	public boolean isBloodbank() {
		return bloodbank;
	}

	public void setBloodbank(boolean bloodbank) {
		this.bloodbank = bloodbank;
	}

	public boolean isAccounts() {
		return accounts;
	}

	public void setAccounts(boolean accounts) {
		this.accounts = accounts;
	}

	public boolean isPayroll() {
		return payroll;
	}

	public void setPayroll(boolean payroll) {
		this.payroll = payroll;
	}

	public boolean isExpenses() {
		return expenses;
	}

	public void setExpenses(boolean expenses) {
		this.expenses = expenses;
	}

	public boolean isInventory() {
		return inventory;
	}

	public void setInventory(boolean inventory) {
		this.inventory = inventory;
	}

	public boolean isMis() {
		return mis;
	}

	public void setMis(boolean mis) {
		this.mis = mis;
	}

	public boolean isConsultants() {
		return consultants;
	}

	public void setConsultants(boolean consultants) {
		this.consultants = consultants;
	}

	public boolean isPatient() {
		return patient;
	}

	public void setPatient(boolean patient) {
		this.patient = patient;
	}

	public boolean isAppointmentfinder() {
		return appointmentfinder;
	}

	public void setAppointmentfinder(boolean appointmentfinder) {
		this.appointmentfinder = appointmentfinder;
	}

	public boolean isSetting() {
		return setting;
	}

	public void setSetting(boolean setting) {
		this.setting = setting;
	}

	public String getDisciplineid() {
		return disciplineid;
	}

	public void setDisciplineid(String disciplineid) {
		this.disciplineid = disciplineid;
	}

	public ArrayList<Location> getMultiLicationList() {
		return multiLicationList;
	}

	public void setMultiLicationList(ArrayList<Location> multiLicationList) {
		this.multiLicationList = multiLicationList;
	}

	public String getUsrPosition() {
		return usrPosition;
	}

	public void setUsrPosition(String usrPosition) {
		this.usrPosition = usrPosition;
	}

	public String getLocationString() {
		return locationString;
	}

	public void setLocationString(String locationString) {
		this.locationString = locationString;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getDiaryUser() {
		return diaryUser;
	}

	public void setDiaryUser(String diaryUser) {
		this.diaryUser = diaryUser;
	}

	public String getWeekFullName() {
		return weekFullName;
	}

	public void setWeekFullName(String weekFullName) {
		this.weekFullName = weekFullName;
	}

	public String getDiaryColor() {
		return diaryColor;
	}

	public void setDiaryColor(String diaryColor) {
		this.diaryColor = diaryColor;
	}

	public ArrayList<Month> getMonthtdList() {
		return monthtdList;
	}

	public void setMonthtdList(ArrayList<Month> monthtdList) {
		this.monthtdList = monthtdList;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getWeekName() {
		return weekName;
	}

	public void setWeekName(String weekName) {
		this.weekName = weekName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCommencing() {
		return commencing;
	}

	public void setCommencing(String commencing) {
		this.commencing = commencing;
	}

	public String getSelectedDiaryUser() {
		return selectedDiaryUser;
	}

	public void setSelectedDiaryUser(String selectedDiaryUser) {
		this.selectedDiaryUser = selectedDiaryUser;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isOnlineBooking() {
		return onlineBooking;
	}

	public void setOnlineBooking(boolean onlineBooking) {
		this.onlineBooking = onlineBooking;
	}

	public String getSTime() {
		return sTime;
	}

	public void setSTime(String time) {
		sTime = time;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public int getDiarUserid() {
		return diarUserid;
	}

	public void setDiarUserid(int diarUserid) {
		this.diarUserid = diarUserid;
	}

	public String getApmtDuration() {
		return apmtDuration;
	}

	public void setApmtDuration(String apmtDuration) {
		this.apmtDuration = apmtDuration;
	}

	public String getTdCode() {
		return tdCode;
	}

	public void setTdCode(String tdCode) {
		this.tdCode = tdCode;
	}

	public ArrayList<Tdcode> getTdDataList() {
		return tdDataList;
	}

	public void setTdDataList(ArrayList<Tdcode> tdDataList) {
		this.tdDataList = tdDataList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLocationColor() {
		return locationColor;
	}

	public void setLocationColor(String locationColor) {
		this.locationColor = locationColor;
	}

	public boolean isPacks() {
		return packs;
	}

	public void setPacks(boolean packs) {
		this.packs = packs;
	}

	public boolean isInvestigation_chart() {
		return investigation_chart;
	}

	public void setInvestigation_chart(boolean investigation_chart) {
		this.investigation_chart = investigation_chart;
	}

	public boolean isSheduler() {
		return sheduler;
	}

	public void setSheduler(boolean sheduler) {
		this.sheduler = sheduler;
	}

	public boolean isHousekeeping() {
		return housekeeping;
	}

	public void setHousekeeping(boolean housekeeping) {
		this.housekeeping = housekeeping;
	}

	public boolean isDietery() {
		return dietery;
	}

	public void setDietery(boolean dietery) {
		this.dietery = dietery;
	}

	public boolean isCafeteria() {
		return cafeteria;
	}

	public void setCafeteria(boolean cafeteria) {
		this.cafeteria = cafeteria;
	}

	public boolean isPackages() {
		return packages;
	}

	public void setPackages(boolean packages) {
		this.packages = packages;
	}

	public boolean isAmbulance() {
		return ambulance;
	}

	public void setAmbulance(boolean ambulance) {
		this.ambulance = ambulance;
	}

	public boolean isBank_deposite() {
		return bank_deposite;
	}

	public void setBank_deposite(boolean bank_deposite) {
		this.bank_deposite = bank_deposite;
	}

	public boolean isAccount_reconcilation() {
		return account_reconcilation;
	}

	public void setAccount_reconcilation(boolean account_reconcilation) {
		this.account_reconcilation = account_reconcilation;
	}

	public boolean isOt() {
		return ot;
	}

	public void setOt(boolean ot) {
		this.ot = ot;
	}

	public boolean isCasualty() {
		return casualty;
	}

	public void setCasualty(boolean casualty) {
		this.casualty = casualty;
	}

	public boolean isPharmacy() {
		return pharmacy;
	}

	public void setPharmacy(boolean pharmacy) {
		this.pharmacy = pharmacy;
	}

	public boolean isMrd() {
		return mrd;
	}

	public void setMrd(boolean mrd) {
		this.mrd = mrd;
	}

	public boolean isMarketing() {
		return marketing;
	}

	public void setMarketing(boolean marketing) {
		this.marketing = marketing;
	}

	public boolean isVoice_recording() {
		return voice_recording;
	}

	public void setVoice_recording(boolean voice_recording) {
		this.voice_recording = voice_recording;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isShow_master() {
		return show_master;
	}

	public void setShow_master(boolean show_master) {
		this.show_master = show_master;
	}

	public boolean isToken_display() {
		return token_display;
	}

	public void setToken_display(boolean token_display) {
		this.token_display = token_display;
	}

	public boolean isAdd_medicine() {
		return add_medicine;
	}

	public void setAdd_medicine(boolean add_medicine) {
		this.add_medicine = add_medicine;
	}

	public boolean isStock_log() {
		return stock_log;
	}

	public void setStock_log(boolean stock_log) {
		this.stock_log = stock_log;
	}

	public boolean isPharm_print_backdate() {
		return pharm_print_backdate;
	}

	public void setPharm_print_backdate(boolean pharm_print_backdate) {
		this.pharm_print_backdate = pharm_print_backdate;
	}

	public boolean isModify_disc() {
		return modify_disc;
	}

	public void setModify_disc(boolean modify_disc) {
		this.modify_disc = modify_disc;
	}

	public boolean isCathlab() {
		return cathlab;
	}

	public void setCathlab(boolean cathlab) {
		this.cathlab = cathlab;
	}

	private boolean token_display;
	
	
	private boolean add_medicine;
	private boolean stock_log,pharm_print_backdate;
	private boolean modify_disc;
	private boolean cathlab;
	private boolean myhr;
	private boolean daycare;
	private boolean emergency_lbl;


	public String getsTime() {
		return sTime;
	}

	public void setsTime(String sTime) {
		this.sTime = sTime;
	}

	public boolean isMyhr() {
		return myhr;
	}

	public void setMyhr(boolean myhr) {
		this.myhr = myhr;
	}

	public boolean isDaycare() {
		return daycare;
	}

	public void setDaycare(boolean daycare) {
		this.daycare = daycare;
	}

	public boolean isEmergency_lbl() {
		return emergency_lbl;
	}

	public void setEmergency_lbl(boolean emergency_lbl) {
		this.emergency_lbl = emergency_lbl;
	}
}
