package com.apm.DiaryManagement.eu.entity;

import java.io.File;
import java.util.ArrayList;

import com.apm.Accounts.eu.entity.Accounts;
import com.apm.Ipd.eu.entity.Bed;
import com.apm.Master.eu.entity.Master;
import com.apm.ThirdParties.eu.entity.ThirdParty;

public class Client{
	private String patientIdAbrivation;
	public String getPatientIdAbrivation() {
		return patientIdAbrivation;
	}
	public void setPatientIdAbrivation(String patientIdAbrivation) {
		this.patientIdAbrivation = patientIdAbrivation;
	}
	private String patientID;
	private String ispharmacy;
	public String getIspharmacy() {
		return ispharmacy;
	}
	public String getPatientID() {
		return patientID;
	}
	public void setPatientID(String patientID) {
		this.patientID = patientID;
	}
	public void setIspharmacy(String ispharmacy) {
		this.ispharmacy = ispharmacy;
	}
	private String agegender;
	private String birthtime;
	private String relativename;
	private String charges;
	private String relativeno;
	private String address1;
	
	public String getAddress1() {
		return address1;
	}
	public void setAddress1(String address1) {
		this.address1 = address1;
	}
	private String adhno;
	private String tpmemb;
	
	private String city;
	private int id;
	private String firstName="";
	private String lastName="";
	private String middlename="";
	private String title="";
	private String mobNo;
	private String email;
	private String gender;
	private String dob;
	private String town;
	private String country;
	private String address;
	private String sourceOfIntro;
	private String balance="0";
	private String reference;
	private String postCode;
	private String employerName;
	private String conditionid;
	private String treatmentType;
	private String referedDate;
	private String other = "Other";
	private String doctorsurgery; 
	private String gptypeName;
	private String secondLineaddress = "";
	private String lastApmtDate;
	private String state;
	private int total;
	private String invstypeid;
	private String department;
	private String relation="";
	
	private String clientName;
	private String height="";
	private String weight="";
	private String bmi="";
	private String pulse="";
	private String sysbp="";
	private String diabp="";
	private String casualtyid;
	private String sugarfasting="";
	private String postmeal="";
	
	private String tpName = "";
	
	private String clientNote = "";
	
	ArrayList<Accounts>invoiceList;
	
	ArrayList<Accounts>selecttedInvoiceList;
	
	private String creditTotalx;
	private String debitTotalx;
	private String balanceTotalx;
	
	private String nhsNumber;
	
	private String debitSum;
	
	private String creditSum;
	
	private String balanceSum;
	
	private int age;
	
	ArrayList<Bed>ipdList;
	private String clientid;
	private String appointmentid;
	private String apmtStatus;
	private String dnaid;
	private String apmttypetext;
	private String abrivationid;
private String fullname;
private String wardname="";
private String bedname ="";
private String admissiondate ="";
private String ipdid;
private String practid;
private String clientId;
private String declarationNotes;
private String declarationTitle;
private String userid;


private String heightdata;
private String heightmonth;
private String weightdata;
private String weightmonth;
private String bmidata; 
private String bmimonth;
private String headcircumferncedata;
private String headcircumferncemonth;
private String month;
private String date;

private String obesity;
private String owerweight;
private String normal;
private String thinness;
private String severthinness; 

private int opdcomjan=0,opdcomfeb=0,opdcommar=0,opdcomapr=0,opdcommay=0,opdcomjune=0,opdcomjuly=0,opdcomaug=0,opdcomsep=0,opdcomoct=0,opdcomnov=0,opdcomdec=0;
private int opddnajan=0,opddnafeb=0,opddnamar=0,opddnaapr=0,opddnamay=0,opddnajune=0,opddnajuly=0,opddnaaug=0,opddnasep=0,opddnaoct=0,opddnanov=0,opddnadec=0;
private int isautocharge;
private String cgddata;
private String monthyear;

	public String getCgddata() {
	return cgddata;
}
public String getMonthyear() {
		return monthyear;
	}
	public void setMonthyear(String monthyear) {
		this.monthyear = monthyear;
	}
public void setCgddata(String cgddata) {
	this.cgddata = cgddata;
}
	public int getIsautocharge() {
	return isautocharge;
}
public void setIsautocharge(int isautocharge) {
	this.isautocharge = isautocharge;
}
	public int getOpdcomjan() {
	return opdcomjan;
}
public void setOpdcomjan(int opdcomjan) {
	this.opdcomjan = opdcomjan;
}
public int getOpdcomfeb() {
	return opdcomfeb;
}
public void setOpdcomfeb(int opdcomfeb) {
	this.opdcomfeb = opdcomfeb;
}
public int getOpdcommar() {
	return opdcommar;
}
public void setOpdcommar(int opdcommar) {
	this.opdcommar = opdcommar;
}
public int getOpdcomapr() {
	return opdcomapr;
}
public void setOpdcomapr(int opdcomapr) {
	this.opdcomapr = opdcomapr;
}
public int getOpdcommay() {
	return opdcommay;
}
public void setOpdcommay(int opdcommay) {
	this.opdcommay = opdcommay;
}
public int getOpdcomjune() {
	return opdcomjune;
}
public void setOpdcomjune(int opdcomjune) {
	this.opdcomjune = opdcomjune;
}
public int getOpdcomjuly() {
	return opdcomjuly;
}
public void setOpdcomjuly(int opdcomjuly) {
	this.opdcomjuly = opdcomjuly;
}
public int getOpdcomaug() {
	return opdcomaug;
}
public void setOpdcomaug(int opdcomaug) {
	this.opdcomaug = opdcomaug;
}
public int getOpdcomsep() {
	return opdcomsep;
}
public void setOpdcomsep(int opdcomsep) {
	this.opdcomsep = opdcomsep;
}
public int getOpdcomoct() {
	return opdcomoct;
}
public void setOpdcomoct(int opdcomoct) {
	this.opdcomoct = opdcomoct;
}
public int getOpdcomnov() {
	return opdcomnov;
}
public void setOpdcomnov(int opdcomnov) {
	this.opdcomnov = opdcomnov;
}
public int getOpdcomdec() {
	return opdcomdec;
}
public void setOpdcomdec(int opdcomdec) {
	this.opdcomdec = opdcomdec;
}
public int getOpddnajan() {
	return opddnajan;
}
public void setOpddnajan(int opddnajan) {
	this.opddnajan = opddnajan;
}
public int getOpddnafeb() {
	return opddnafeb;
}
public void setOpddnafeb(int opddnafeb) {
	this.opddnafeb = opddnafeb;
}
public int getOpddnamar() {
	return opddnamar;
}
public void setOpddnamar(int opddnamar) {
	this.opddnamar = opddnamar;
}
public int getOpddnaapr() {
	return opddnaapr;
}
public void setOpddnaapr(int opddnaapr) {
	this.opddnaapr = opddnaapr;
}
public int getOpddnamay() {
	return opddnamay;
}
public void setOpddnamay(int opddnamay) {
	this.opddnamay = opddnamay;
}
public int getOpddnajune() {
	return opddnajune;
}
public void setOpddnajune(int opddnajune) {
	this.opddnajune = opddnajune;
}
public int getOpddnajuly() {
	return opddnajuly;
}
public void setOpddnajuly(int opddnajuly) {
	this.opddnajuly = opddnajuly;
}
public int getOpddnaaug() {
	return opddnaaug;
}
public void setOpddnaaug(int opddnaaug) {
	this.opddnaaug = opddnaaug;
}
public int getOpddnasep() {
	return opddnasep;
}
public void setOpddnasep(int opddnasep) {
	this.opddnasep = opddnasep;
}
public int getOpddnaoct() {
	return opddnaoct;
}
public void setOpddnaoct(int opddnaoct) {
	this.opddnaoct = opddnaoct;
}
public int getOpddnanov() {
	return opddnanov;
}
public void setOpddnanov(int opddnanov) {
	this.opddnanov = opddnanov;
}
public int getOpddnadec() {
	return opddnadec;
}
public void setOpddnadec(int opddnadec) {
	this.opddnadec = opddnadec;
}
	public String getObesity() {
	return obesity;
}
public void setObesity(String obesity) {
	this.obesity = obesity;
}
public String getOwerweight() {
	return owerweight;
}
public void setOwerweight(String owerweight) {
	this.owerweight = owerweight;
}
public String getNormal() {
	return normal;
}
public void setNormal(String normal) {
	this.normal = normal;
}
public String getThinness() {
	return thinness;
}
public void setThinness(String thinness) {
	this.thinness = thinness;
}
public String getSeverthinness() {
	return severthinness;
}
public void setSeverthinness(String severthinness) {
	this.severthinness = severthinness;
}
	public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
}
	public String getHeightdata() {
	return heightdata;
}
public void setHeightdata(String heightdata) {
	this.heightdata = heightdata;
}
public String getHeightmonth() {
	return heightmonth;
}
public void setHeightmonth(String heightmonth) {
	this.heightmonth = heightmonth;
}
public String getWeightdata() {
	return weightdata;
}
public void setWeightdata(String weightdata) {
	this.weightdata = weightdata;
}
public String getWeightmonth() {
	return weightmonth;
}
public void setWeightmonth(String weightmonth) {
	this.weightmonth = weightmonth;
}
public String getBmidata() {
	return bmidata;
}
public void setBmidata(String bmidata) {
	this.bmidata = bmidata;
}
public String getBmimonth() {
	return bmimonth;
}
public void setBmimonth(String bmimonth) {
	this.bmimonth = bmimonth;
}
public String getHeadcircumferncedata() {
	return headcircumferncedata;
}
public void setHeadcircumferncedata(String headcircumferncedata) {
	this.headcircumferncedata = headcircumferncedata;
}
public String getHeadcircumferncemonth() {
	return headcircumferncemonth;
}
public void setHeadcircumferncemonth(String headcircumferncemonth) {
	this.headcircumferncemonth = headcircumferncemonth;
}
public String getMonth() {
	return month;
}
public void setMonth(String month) {
	this.month = month;
}
	public String getUserid() {
	return userid;
}
public void setUserid(String userid) {
	this.userid = userid;
}
	public String getPractid() {
	return practid;
}
public void setPractid(String practid) {
	this.practid = practid;
}
public String getClientId() {
	return clientId;
}
public void setClientId(String clientId) {
	this.clientId = clientId;
}
public String getDeclarationNotes() {
	return declarationNotes;
}
public void setDeclarationNotes(String declarationNotes) {
	this.declarationNotes = declarationNotes;
}
public String getDeclarationTitle() {
	return declarationTitle;
}
public void setDeclarationTitle(String declarationTitle) {
	this.declarationTitle = declarationTitle;
}
	public String getIpdid() {
	return ipdid;
}
public void setIpdid(String ipdid) {
	this.ipdid = ipdid;
}
	public String getFullname() {
	return fullname;
}
public void setFullname(String fullname) {
	this.fullname = fullname;
}
	int otjan=0,otfeb=0,otmar=0,otapr=0,otmay=0,otjune=0,otjuly=0,otaug=0,otsep=0,otoct=0,otnov=0,otdec=0;
	int opdjan=0,opdfeb=0,opdmar=0,opdapr=0,opdmay=0,opdjune=0,opdjuly=0,opdaug=0,opdsep=0,opdoct=0,opdnov=0,opddec=0;
	int ipdjan=0,ipdfeb=0,ipdmar=0,ipdapr=0,ipdmay=0,ipdjune=0,ipdjuly=0,ipdaug=0,ipdsep=0,ipdoct=0,ipdnov=0,ipddec=0;
	
	private String imageName;
	private String companyName;
	
	
	private File userImage;
	private String userImageFileName;
	private String userImageContentType;
	
	private String invstid;
	
	ArrayList<Master>invetigationList;
	

	public String getWardname() {
		return wardname;
	}
	public void setWardname(String wardname) {
		this.wardname = wardname;
	}
	public String getBedname() {
		return bedname;
	}
	public void setBedname(String bedname) {
		this.bedname = bedname;
	}
	public String getAdmissiondate() {
		return admissiondate;
	}
	public void setAdmissiondate(String admissiondate) {
		this.admissiondate = admissiondate;
	}
	public String getSugarfasting() {
		return sugarfasting;
	}
	public void setSugarfasting(String sugarfasting) {
		this.sugarfasting = sugarfasting;
	}
	public String getPostmeal() {
		return postmeal;
	}
	public void setPostmeal(String postmeal) {
		this.postmeal = postmeal;
	}
	public ArrayList<Master> getInvetigationList() {
		return invetigationList;
	}
	public void setInvetigationList(ArrayList<Master> invetigationList) {
		this.invetigationList = invetigationList;
	}
	public String getInvstid() {
		return invstid;
	}
	public void setInvstid(String invstid) {
		this.invstid = invstid;
	}
	public File getUserImage() {
		return userImage;
	}
	public void setUserImage(File userImage) {
		this.userImage = userImage;
	}
	public String getUserImageFileName() {
		return userImageFileName;
	}
	public void setUserImageFileName(String userImageFileName) {
		this.userImageFileName = userImageFileName;
	}
	public String getUserImageContentType() {
		return userImageContentType;
	}
	public void setUserImageContentType(String userImageContentType) {
		this.userImageContentType = userImageContentType;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getOtjan() {
		return otjan;
	}
	public void setOtjan(int otjan) {
		this.otjan = otjan;
	}
	public int getOtfeb() {
		return otfeb;
	}
	public void setOtfeb(int otfeb) {
		this.otfeb = otfeb;
	}
	public int getOtmar() {
		return otmar;
	}
	public void setOtmar(int otmar) {
		this.otmar = otmar;
	}
	public int getOtapr() {
		return otapr;
	}
	public void setOtapr(int otapr) {
		this.otapr = otapr;
	}
	public int getOtmay() {
		return otmay;
	}
	public void setOtmay(int otmay) {
		this.otmay = otmay;
	}
	public int getOtjune() {
		return otjune;
	}
	public void setOtjune(int otjune) {
		this.otjune = otjune;
	}
	public int getOtjuly() {
		return otjuly;
	}
	public void setOtjuly(int otjuly) {
		this.otjuly = otjuly;
	}
	public int getOtaug() {
		return otaug;
	}
	public void setOtaug(int otaug) {
		this.otaug = otaug;
	}
	public int getOtsep() {
		return otsep;
	}
	public void setOtsep(int otsep) {
		this.otsep = otsep;
	}
	public int getOtoct() {
		return otoct;
	}
	public void setOtoct(int otoct) {
		this.otoct = otoct;
	}
	public int getOtnov() {
		return otnov;
	}
	public void setOtnov(int otnov) {
		this.otnov = otnov;
	}
	public int getOtdec() {
		return otdec;
	}
	public void setOtdec(int otdec) {
		this.otdec = otdec;
	}
	public int getOpdjan() {
		return opdjan;
	}
	public void setOpdjan(int opdjan) {
		this.opdjan = opdjan;
	}
	public int getOpdfeb() {
		return opdfeb;
	}
	public void setOpdfeb(int opdfeb) {
		this.opdfeb = opdfeb;
	}
	public int getOpdmar() {
		return opdmar;
	}
	public void setOpdmar(int opdmar) {
		this.opdmar = opdmar;
	}
	public int getOpdapr() {
		return opdapr;
	}
	public void setOpdapr(int opdapr) {
		this.opdapr = opdapr;
	}
	public int getOpdmay() {
		return opdmay;
	}
	public void setOpdmay(int opdmay) {
		this.opdmay = opdmay;
	}
	public int getOpdjune() {
		return opdjune;
	}
	public void setOpdjune(int opdjune) {
		this.opdjune = opdjune;
	}
	public int getOpdjuly() {
		return opdjuly;
	}
	public void setOpdjuly(int opdjuly) {
		this.opdjuly = opdjuly;
	}
	public int getOpdaug() {
		return opdaug;
	}
	public void setOpdaug(int opdaug) {
		this.opdaug = opdaug;
	}
	public int getOpdsep() {
		return opdsep;
	}
	public void setOpdsep(int opdsep) {
		this.opdsep = opdsep;
	}
	public int getOpdoct() {
		return opdoct;
	}
	public void setOpdoct(int opdoct) {
		this.opdoct = opdoct;
	}
	public int getOpdnov() {
		return opdnov;
	}
	public void setOpdnov(int opdnov) {
		this.opdnov = opdnov;
	}
	public int getOpddec() {
		return opddec;
	}
	public void setOpddec(int opddec) {
		this.opddec = opddec;
	}
	public int getIpdjan() {
		return ipdjan;
	}
	public void setIpdjan(int ipdjan) {
		this.ipdjan = ipdjan;
	}
	public int getIpdfeb() {
		return ipdfeb;
	}
	public void setIpdfeb(int ipdfeb) {
		this.ipdfeb = ipdfeb;
	}
	public int getIpdmar() {
		return ipdmar;
	}
	public void setIpdmar(int ipdmar) {
		this.ipdmar = ipdmar;
	}
	public int getIpdapr() {
		return ipdapr;
	}
	public void setIpdapr(int ipdapr) {
		this.ipdapr = ipdapr;
	}
	public int getIpdmay() {
		return ipdmay;
	}
	public void setIpdmay(int ipdmay) {
		this.ipdmay = ipdmay;
	}
	public int getIpdjune() {
		return ipdjune;
	}
	public void setIpdjune(int ipdjune) {
		this.ipdjune = ipdjune;
	}
	public int getIpdjuly() {
		return ipdjuly;
	}
	public void setIpdjuly(int ipdjuly) {
		this.ipdjuly = ipdjuly;
	}
	public int getIpdaug() {
		return ipdaug;
	}
	public void setIpdaug(int ipdaug) {
		this.ipdaug = ipdaug;
	}
	public int getIpdsep() {
		return ipdsep;
	}
	public void setIpdsep(int ipdsep) {
		this.ipdsep = ipdsep;
	}
	public int getIpdoct() {
		return ipdoct;
	}
	public void setIpdoct(int ipdoct) {
		this.ipdoct = ipdoct;
	}
	public int getIpdnov() {
		return ipdnov;
	}
	public void setIpdnov(int ipdnov) {
		this.ipdnov = ipdnov;
	}
	public int getIpddec() {
		return ipddec;
	}
	public void setIpddec(int ipddec) {
		this.ipddec = ipddec;
	}
	public String getAppointmentid() {
		return appointmentid;
	}
	public void setAppointmentid(String appointmentid) {
		this.appointmentid = appointmentid;
	}
	public String getApmtStatus() {
		return apmtStatus;
	}
	public void setApmtStatus(String apmtStatus) {
		this.apmtStatus = apmtStatus;
	}
	public String getDnaid() {
		return dnaid;
	}
	public void setDnaid(String dnaid) {
		this.dnaid = dnaid;
	}
	public String getApmttypetext() {
		return apmttypetext;
	}
	public void setApmttypetext(String apmttypetext) {
		this.apmttypetext = apmttypetext;
	}
	public ArrayList<Bed> getIpdList() {
		return ipdList;
	}
	public void setIpdList(ArrayList<Bed> ipdList) {
		this.ipdList = ipdList;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getDebitSum() {
		return debitSum;
	}
	public void setDebitSum(String debitSum) {
		this.debitSum = debitSum;
	}
	public String getCreditSum() {
		return creditSum;
	}
	public void setCreditSum(String creditSum) {
		this.creditSum = creditSum;
	}
	public String getBalanceSum() {
		return balanceSum;
	}
	public void setBalanceSum(String balanceSum) {
		this.balanceSum = balanceSum;
	}
	public String getNhsNumber() {
		return nhsNumber;
	}
	public void setNhsNumber(String nhsNumber) {
		this.nhsNumber = nhsNumber;
	}
	public String getCreditTotalx() {
		return creditTotalx;
	}
	public void setCreditTotalx(String creditTotalx) {
		this.creditTotalx = creditTotalx;
	}
	public String getDebitTotalx() {
		return debitTotalx;
	}
	public void setDebitTotalx(String debitTotalx) {
		this.debitTotalx = debitTotalx;
	}
	public String getBalanceTotalx() {
		return balanceTotalx;
	}
	public void setBalanceTotalx(String balanceTotalx) {
		this.balanceTotalx = balanceTotalx;
	}
	public ArrayList<Accounts> getSelecttedInvoiceList() {
		return selecttedInvoiceList;
	}
	public void setSelecttedInvoiceList(ArrayList<Accounts> selecttedInvoiceList) {
		this.selecttedInvoiceList = selecttedInvoiceList;
	}
	public ArrayList<Accounts> getInvoiceList() {
		return invoiceList;
	}
	public void setInvoiceList(ArrayList<Accounts> invoiceList) {
		this.invoiceList = invoiceList;
	}
	public String getGpid() {
		return gpid;
	}
	public void setGpid(String gpid) {
		this.gpid = gpid;
	}
	private String accountNote = "";
	
	private String clinicalNote = "";
	
	private String lastModifiedDate = "";
	
	private ThirdParty gpDetails;
	
	private ThirdParty tpDetails;
	
	private String gpid;
	
	
	
	
	
	
	
	
	public ThirdParty getTpDetails() {
		return tpDetails;
	}
	public void setTpDetails(ThirdParty tpDetails) {
		this.tpDetails = tpDetails;
	}
	public ThirdParty getGpDetails() {
		return gpDetails;
	}
	public void setGpDetails(ThirdParty gpDetails) {
		this.gpDetails = gpDetails;
	}
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getClientNote() {
		return clientNote;
	}
	public void setClientNote(String clientNote) {
		this.clientNote = clientNote;
	}
	public String getAccountNote() {
		return accountNote;
	}
	public void setAccountNote(String accountNote) {
		this.accountNote = accountNote;
	}
	public String getClinicalNote() {
		return clinicalNote;
	}
	public void setClinicalNote(String clinicalNote) {
		this.clinicalNote = clinicalNote;
	}
	public String getTpName() {
		return tpName;
	}
	public void setTpName(String tpName) {
		this.tpName = tpName;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getLastApmtDate() {
		return lastApmtDate;
	}
	public void setLastApmtDate(String lastApmtDate) {
		this.lastApmtDate = lastApmtDate;
	}
	public String getSecondLineaddress() {
		return secondLineaddress;
	}
	public void setSecondLineaddress(String secondLineaddress) {
		this.secondLineaddress = secondLineaddress;
	}
	public String getSourceOfIntroName() {
		return sourceOfIntroName;
	}
	public void setSourceOfIntroName(String sourceOfIntroName) {
		this.sourceOfIntroName = sourceOfIntroName;
	}
	private String sourceOfIntroName;
	
	
	public String getGptypeName() {
		return gptypeName;
	}
	public void setGptypeName(String gptypeName) {
		this.gptypeName = gptypeName;
	}
	public String getDoctorsurgery() {
		return doctorsurgery;
	}
	public void setDoctorsurgery(String doctorsurgery) {
		this.doctorsurgery = doctorsurgery;
	}
	private String lastModified;
	//Report fields
	private int noApmts;
	private double totalTPCharges;
	private double totalSelfCharges;
	private double totalTPPaidAmt;
	private double totalSelfPaidAmt;
	private double totalTPBalance;
	private double totalSelfBalnace;
	
	
	//Third Party attribute
	
	public int getNoApmts() {
		return noApmts;
	}
	public void setNoApmts(int noApmts) {
		this.noApmts = noApmts;
	}
	public double getTotalTPCharges() {
		return totalTPCharges;
	}
	public void setTotalTPCharges(double totalTPCharges) {
		this.totalTPCharges = totalTPCharges;
	}
	public double getTotalSelfCharges() {
		return totalSelfCharges;
	}
	public void setTotalSelfCharges(double totalSelfCharges) {
		this.totalSelfCharges = totalSelfCharges;
	}
	public double getTotalTPPaidAmt() {
		return totalTPPaidAmt;
	}
	public void setTotalTPPaidAmt(double totalTPPaidAmt) {
		this.totalTPPaidAmt = totalTPPaidAmt;
	}
	public double getTotalSelfPaidAmt() {
		return totalSelfPaidAmt;
	}
	public void setTotalSelfPaidAmt(double totalSelfPaidAmt) {
		this.totalSelfPaidAmt = totalSelfPaidAmt;
	}
	public double getTotalTPBalance() {
		return totalTPBalance;
	}
	public void setTotalTPBalance(double totalTPBalance) {
		this.totalTPBalance = totalTPBalance;
	}
	public double getTotalSelfBalnace() {
		return totalSelfBalnace;
	}
	public void setTotalSelfBalnace(double totalSelfBalnace) {
		this.totalSelfBalnace = totalSelfBalnace;
	}
	public String getOther() {
		return other;
	}
	public void setOther(String other) {
		this.other = other;
	}
	public String getLastModified() {
		return lastModified;
	}
	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}
	public String getReferedDate() {
		return referedDate;
	}
	public void setReferedDate(String referedDate) {
		this.referedDate = referedDate;
	}
	public String getTreatmentType() {
		return treatmentType;
	}
	public void setTreatmentType(String treatmentType) {
		this.treatmentType = treatmentType;
	}
	public String getEmployerName() {
		return employerName;
	}
	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}
	private String type;
	private String typeName;
	private String thirdPartyType;
	private String thirdPartyTypeName;
	private String thirdPartyCompanyName;
	//21st march
	private String occupation;
	private String expiryDate;
	private String defaultNotes;
	private String accountsNotes;
	private String criticalInfo;
	
	//28th march
	private String whopay;
	public String getPolicyExcess() {
		return policyExcess;
	}
	public void setPolicyExcess(String policyExcess) {
		this.policyExcess = policyExcess;
	}
	private String policyAuthorzCode;
	private String policyNo;
	private String policyExcess;
	private ArrayList<Client> otherOption ;
	
	private String knownAs;
	private String county;
	private String homeNo;
	private String workNo;
	private String emailCc;
	private String prefContactMode;
	private String emergencyContName;
	private String emergencyContNo;
	private String patientType;
	private String note = "";
	private String oldclientId;
	private String diaryUser;
	private String gpname;
	private String length;
	
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getGpname() {
		return gpname;
	}
	public void setGpname(String gpname) {
		this.gpname = gpname;
	}
	public String getDiaryUser() {
		return diaryUser;
	}
	public void setDiaryUser(String diaryUser) {
		this.diaryUser = diaryUser;
	}
	public String getOldclientId() {
		return oldclientId;
	}
	public void setOldclientId(String oldclientId) {
		this.oldclientId = oldclientId;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	private boolean status;
	
	public boolean isStatus() {
		return status;
	}
	public String getMiddlename() {
		return middlename;
	}
	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getExpiryDate() {
		return expiryDate;
	}
	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}
	public String getDefaultNotes() {
		return defaultNotes;
	}
	public void setDefaultNotes(String defaultNotes) {
		this.defaultNotes = defaultNotes;
	}
	public String getAccountsNotes() {
		return accountsNotes;
	}
	public void setAccountsNotes(String accountsNotes) {
		this.accountsNotes = accountsNotes;
	}
	public String getCriticalInfo() {
		return criticalInfo;
	}
	public void setCriticalInfo(String criticalInfo) {
		this.criticalInfo = criticalInfo;
	}
	public String getThirdPartyType() {
		return thirdPartyType;
	}
	public void setThirdPartyType(String thirdPartyType) {
		this.thirdPartyType = thirdPartyType;
	}
	public String getThirdPartyTypeName() {
		return thirdPartyTypeName;
	}
	public void setThirdPartyTypeName(String thirdPartyTypeName) {
		this.thirdPartyTypeName = thirdPartyTypeName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMobNo() {
		return mobNo;
	}
	public void setMobNo(String mobNo) {
		this.mobNo = mobNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getSourceOfIntro() {
		return sourceOfIntro;
	}
	public void setSourceOfIntro(String sourceOfIntro) {
		this.sourceOfIntro = sourceOfIntro;
	}
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getThirdPartyCompanyName() {
		return thirdPartyCompanyName;
	}
	public void setThirdPartyCompanyName(String thirdPartyCompanyName) {
		this.thirdPartyCompanyName = thirdPartyCompanyName;
	}
	public String getPolicyAuthorzCode() {
		return policyAuthorzCode;
	}
	public void setPolicyAuthorzCode(String policyAuthorzCode) {
		this.policyAuthorzCode = policyAuthorzCode;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getWhopay() {
		return whopay;
	}
	public void setWhopay(String whopay) {
		this.whopay = whopay;
	}
	public ArrayList<Client> getOtherOption() {
		return otherOption;
	}
	public void setOtherOption(ArrayList<Client> otherOption) {
		this.otherOption = otherOption;
	}
	public String getKnownAs() {
		return knownAs;
	}
	public void setKnownAs(String knownAs) {
		this.knownAs = knownAs;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getHomeNo() {
		return homeNo;
	}
	public void setHomeNo(String homeNo) {
		this.homeNo = homeNo;
	}
	public String getWorkNo() {
		return workNo;
	}
	public void setWorkNo(String workNo) {
		this.workNo = workNo;
	}
	public String getEmailCc() {
		return emailCc;
	}
	public void setEmailCc(String emailCc) {
		this.emailCc = emailCc;
	}
	public String getPrefContactMode() {
		return prefContactMode;
	}
	public void setPrefContactMode(String prefContactMode) {
		this.prefContactMode = prefContactMode;
	}
	public String getEmergencyContName() {
		return emergencyContName;
	}
	public void setEmergencyContName(String emergencyContName) {
		this.emergencyContName = emergencyContName;
	}
	public String getEmergencyContNo() {
		return emergencyContNo;
	}
	public void setEmergencyContNo(String emergencyContNo) {
		this.emergencyContNo = emergencyContNo;
	}
	public String getPatientType() {
		return patientType;
	}
	public void setPatientType(String patientType) {
		this.patientType = patientType;
	}
	public String getConditionid() {
		return conditionid;
	}
	public void setConditionid(String conditionid) {
		this.conditionid = conditionid;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getBmi() {
		return bmi;
	}
	public void setBmi(String bmi) {
		this.bmi = bmi;
	}
	public String getPulse() {
		return pulse;
	}
	public void setPulse(String pulse) {
		this.pulse = pulse;
	}
	public String getSysbp() {
		return sysbp;
	}
	public void setSysbp(String sysbp) {
		this.sysbp = sysbp;
	}
	public String getDiabp() {
		return diabp;
	}
	public void setDiabp(String diabp) {
		this.diabp = diabp;
	}
	public String getClientid() {
		return clientid;
	}
	public void setClientid(String clientid) {
		this.clientid = clientid;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public String getInvstypeid() {
		return invstypeid;
	}
	public void setInvstypeid(String invstypeid) {
		this.invstypeid = invstypeid;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getCasualtyid() {
		return casualtyid;
	}
	public void setCasualtyid(String casualtyid) {
		this.casualtyid = casualtyid;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getAdhno() {
		return adhno;
	}
	public void setAdhno(String adhno) {
		this.adhno = adhno;
	}
	public String getTpmemb() {
		return tpmemb;
	}
	public void setTpmemb(String tpmemb) {
		this.tpmemb = tpmemb;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getRelativename() {
		return relativename;
	}
	public void setRelativename(String relativename) {
		this.relativename = relativename;
	}
	public String getRelativeno() {
		return relativeno;
	}
	public void setRelativeno(String relativeno) {
		this.relativeno = relativeno;
	}
	public String getAbrivationid() {
		return abrivationid;
	}
	public void setAbrivationid(String abrivationid) {
		this.abrivationid = abrivationid;
	}
	public String getAgegender() {
		return agegender;
	}
	public void setAgegender(String agegender) {
		this.agegender = agegender;
	}
	
	public String getFeedbackname() {
		return feedbackname;
	}
	public void setFeedbackname(String feedbackname) {
		this.feedbackname = feedbackname;
	}
	public String getFeedbackid() {
		return feedbackid;
	}
	public void setFeedbackid(String feedbackid) {
		this.feedbackid = feedbackid;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getFeedbackparentid() {
		return feedbackparentid;
	}
	public void setFeedbackparentid(String feedbackparentid) {
		this.feedbackparentid = feedbackparentid;
	}
	public String getManualfeedback() {
		return manualfeedback;
	}
	public void setManualfeedback(String manualfeedback) {
		this.manualfeedback = manualfeedback;
	}
	public String getAge1() {
		return age1;
	}
	public void setAge1(String age1) {
		this.age1 = age1;
	}
	public int getVacine_type() {
		return vacine_type;
	}
	public void setVacine_type(int vacine_type) {
		this.vacine_type = vacine_type;
	}
	public String getOpdid() {
		return opdid;
	}
	public void setOpdid(String opdid) {
		this.opdid = opdid;
	}
	public String getFollowupdate() {
		return followupdate;
	}
	public void setFollowupdate(String followupdate) {
		this.followupdate = followupdate;
	}
	public String getHospitalborn() {
		return hospitalborn;
	}
	public void setHospitalborn(String hospitalborn) {
		this.hospitalborn = hospitalborn;
	}
	private String feedbackname;
	private String feedbackid;
	private String rating;
	private String feedbackparentid;
	private String manualfeedback;
	private String age1;
	private int vacine_type;
	private String opdid;
	private String followupdate;
	private String hospitalborn;
	
	
	
	public String getCompname() {
		return compname;
	}
	public void setCompname(String compname) {
		this.compname = compname;
	}
	public String getNeisno() {
		return neisno;
	}
	public void setNeisno(String neisno) {
		this.neisno = neisno;
	}
	public String getDesignationbytp() {
		return designationbytp;
	}
	public void setDesignationbytp(String designationbytp) {
		this.designationbytp = designationbytp;
	}
	public String getRelationvbytpe() {
		return relationvbytpe;
	}
	public void setRelationvbytpe(String relationvbytpe) {
		this.relationvbytpe = relationvbytpe;
	}
	public String getClaimbytp() {
		return claimbytp;
	}
	public void setClaimbytp(String claimbytp) {
		this.claimbytp = claimbytp;
	}
	public String getUnitstation() {
		return unitstation;
	}
	public void setUnitstation(String unitstation) {
		this.unitstation = unitstation;
	}
	public String getColliery() {
		return colliery;
	}
	public void setColliery(String colliery) {
		this.colliery = colliery;
	}
	public String getAreabytp() {
		return areabytp;
	}
	public void setAreabytp(String areabytp) {
		this.areabytp = areabytp;
	}
	public int getNewtpid() {
		return newtpid;
	}
	public void setNewtpid(int newtpid) {
		this.newtpid = newtpid;
	}
	
	
	
	
	
	public String getDischargedate() {
		return dischargedate;
	}
	public void setDischargedate(String dischargedate) {
		this.dischargedate = dischargedate;
	}
	public String getTemprature() {
		return temprature;
	}
	public void setTemprature(String temprature) {
		this.temprature = temprature;
	}
	public String getSpo() {
		return spo;
	}
	public void setSpo(String spo) {
		this.spo = spo;
	}
	private String compname;
	private String neisno;
	private String designationbytp;
	private String relationvbytpe;
	private String claimbytp;
	private String unitstation;
	private String colliery;
	private String areabytp;
	private int newtpid; 
	private String temprature;
	private String spo;
	
	public String getPolicyholder() {
		return policyholder;
	}
	public void setPolicyholder(String policyholder) {
		this.policyholder = policyholder;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getFstatus() {
		return fstatus;
	}
	public void setFstatus(String fstatus) {
		this.fstatus = fstatus;
	}
	public int getIshd() {
		return ishd;
	}
	public void setIshd(int ishd) {
		this.ishd = ishd;
	}
	
	public String getMastername() {
		return mastername;
	}
	public void setMastername(String mastername) {
		this.mastername = mastername;
	}
	public String getMasterid() {
		return masterid;
	}
	public void setMasterid(String masterid) {
		this.masterid = masterid;
	}
	
	public String getApmtname() {
		return apmtname;
	}
	public void setApmtname(String apmtname) {
		this.apmtname = apmtname;
	}
	public String getApmtcharges() {
		return apmtcharges;
	}
	public void setApmtcharges(String apmtcharges) {
		this.apmtcharges = apmtcharges;
	}
	public String getApmtcode() {
		return apmtcode;
	}
	public void setApmtcode(String apmtcode) {
		this.apmtcode = apmtcode;
	}
	
	public int getWardid() {
		return wardid;
	}
	public void setWardid(int wardid) {
		this.wardid = wardid;
	}
	public int getStdcharge() {
		return stdcharge;
	}
	public void setStdcharge(int stdcharge) {
		this.stdcharge = stdcharge;
	}
	public String getMaritalsts() {
		return maritalsts;
	}
	public void setMaritalsts(String maritalsts) {
		this.maritalsts = maritalsts;
	}
	public String getAdmissionwithtime() {
		return admissionwithtime;
	}
	public void setAdmissionwithtime(String admissionwithtime) {
		this.admissionwithtime = admissionwithtime;
	}
	public String getDischargewithtime() {
		return dischargewithtime;
	}
	public void setDischargewithtime(String dischargewithtime) {
		this.dischargewithtime = dischargewithtime;
	}
	public String getHead_cir() {
		return head_cir;
	}
	public void setHead_cir(String head_cir) {
		this.head_cir = head_cir;
	}
	private String policyholder;
	private String head_cir;
	private String location;  
	private String fstatus;
	private String dischargedate;
	private int ishd;
	private String mastername;
	private String masterid;
	private String apmtname;
	private String apmtcharges;
	private String apmtcode;
	private int wardid;
	private int stdcharge;
	private String maritalsts;
	private String admissionwithtime;
	private String dischargewithtime;
	private String mothername,fathername,birthplace;
	public String getMothername() {
		return mothername;
	}
	public void setMothername(String mothername) {
		this.mothername = mothername;
	}
	public String getFathername() {
		return fathername;
	}
	public void setFathername(String fathername) {
		this.fathername = fathername;
	}
	public String getBirthplace() {
		return birthplace;
	}
	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}
	public String getBirthtime() {
		return birthtime;
	}
	public void setBirthtime(String birthtime) {
		this.birthtime = birthtime;
	}
	public String getDocumentID() {
		return documentID;
	}
	public void setDocumentID(String documentID) {
		this.documentID = documentID;
	}
	public String getDocumentValue() {
		return documentValue;
	}
	public void setDocumentValue(String documentValue) {
		this.documentValue = documentValue;
	}
	public String getCharges() {
		return charges;
	}
	public void setCharges(String charges) {
		this.charges = charges;
	}
	private String documentID,documentValue;
}