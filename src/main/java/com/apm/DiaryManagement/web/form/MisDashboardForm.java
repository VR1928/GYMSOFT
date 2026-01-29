package com.apm.DiaryManagement.web.form;

import java.util.ArrayList;

import com.apm.DiaryManagement.eu.entity.DiaryManagement;
import com.apm.Ipd.eu.entity.Bed;
import com.apm.Master.eu.entity.Master;
import com.apm.Registration.eu.entity.UserProfile;

public class MisDashboardForm {
	
	private String clinicname;
	
	public String getClinicname() {
		return clinicname;
	}

	public void setClinicname(String clinicname) {
		this.clinicname = clinicname;
	}

	private int id;
	private String date;
	
	private String time1;
	 private String ampm;
	
	 private String fullname;
	 private String userid;
	 private String mobile;
	 private String department;
	 private String state;
	 private String location;
	 private int refundstatus;
	 private int discountstatus;
	 private int indentstaus=0;
	 
	public int getIndentstaus() {
		return indentstaus;
	}

	public void setIndentstaus(int indentstaus) {
		this.indentstaus = indentstaus;
	}

	public int getDiscountstatus() {
		return discountstatus;
	}

	public void setDiscountstatus(int discountstatus) {
		this.discountstatus = discountstatus;
	}

	public int getRefundstatus() {
		return refundstatus;
	}

	public void setRefundstatus(int refundstatus) {
		this.refundstatus = refundstatus;
	}

	private String month;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}
	
	private String fromdate;
	private String todate;
	private String eventname;
	private String description;
	
    private String jobtitle;	
	private String time;
	private String place;
	
	private String pagerecords;
	private int totalRecords;
	private String mastername;
	private String userImageFileName;
	
	public String getUserImageFileName() {
		return userImageFileName;
	}

	public void setUserImageFileName(String userImageFileName) {
		this.userImageFileName = userImageFileName;
	}

	private ArrayList<Master> masterlist;
	private ArrayList<DiaryManagement> jobtitleList;
	
	private ArrayList<String> timelist;
		
	private ArrayList<DiaryManagement> eventList;
	

	public ArrayList<DiaryManagement> getEventList() {
		return eventList;
	}

	public void setEventList(ArrayList<DiaryManagement> eventList) {
		this.eventList = eventList;
	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getJobtitle() {
		return jobtitle;
	}

	public void setJobtitle(String jobtitle) {
		this.jobtitle = jobtitle;
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

	public void setPlace(String place) {
		this.place = place;
	}

	public String getPagerecords() {
		return pagerecords;
	}

	public void setPagerecords(String pagerecords) {
		this.pagerecords = pagerecords;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public String getMastername() {
		return mastername;
	}

	public void setMastername(String mastername) {
		this.mastername = mastername;
	}

	public ArrayList<Master> getMasterlist() {
		return masterlist;
	}

	public void setMasterlist(ArrayList<Master> masterlist) {
		this.masterlist = masterlist;
	}

	public ArrayList<DiaryManagement> getJobtitleList() {
		return jobtitleList;
	}

	public void setJobtitleList(ArrayList<DiaryManagement> jobtitleList) {
		this.jobtitleList = jobtitleList;
	}

	public ArrayList<String> getTimelist() {
		return timelist;
	}

	public void setTimelist(ArrayList<String> timelist) {
		this.timelist = timelist;
	}

	public String getTime1() {
		return time1;
	}

	public void setTime1(String time1) {
		this.time1 = time1;
	}

	public String getAmpm() {
		return ampm;
	}

	public void setAmpm(String ampm) {
		this.ampm = ampm;
	}
private String city;
	

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	

	public ArrayList<UserProfile> getBdaylist() {
		return bdaylist;
	}

	public void setBdaylist(ArrayList<UserProfile> bdaylist) {
		this.bdaylist = bdaylist;
	}

	public ArrayList<Bed> getWardlist() {
		return wardlist;
	}

	public void setWardlist(ArrayList<Bed> wardlist) {
		this.wardlist = wardlist;
	}

	public String getWardid() {
		return wardid;
	}

	public void setWardid(String wardid) {
		this.wardid = wardid;
	}

	public int getExpirytime() {
		return expirytime;
	}

	public void setExpirytime(int expirytime) {
		this.expirytime = expirytime;
	}

	private ArrayList<UserProfile> bdaylist;
	
	private ArrayList<Bed> wardlist;

	
	private String wardid;
	private int expirytime=16;
}
