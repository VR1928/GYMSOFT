package com.apm.Support.eu.bi;

import java.util.ArrayList;

import com.apm.Master.eu.entity.Master;
import com.apm.Registration.eu.entity.Clinic;
import com.apm.Registration.eu.entity.UserProfile;
import com.apm.Support.eu.entity.Support;
import com.apm.common.utils.Pagination;

public interface UserAdministartionDAO {
	
	
	ArrayList<Clinic> getAllClinic();
	
	int giveDeadLineToClient(String userid, String date,String type);
	
	int setClinicActiveDeactive(Clinic clinic, String active);
	
	int sendToMainSupport(String clinicid,String userid,String qtype,String query,String datetime,String dept,String fullname,String mobno,String module,String issuetype, String altmob);
	
	ArrayList<UserProfile> getAllSupportRequest(String clinicid,String userid,String status,Pagination pagination);
	ArrayList<UserProfile> getAllSupportRequestToAdmin(String clinicid,String userid,String status,String fromdate,String todate,Pagination pagination);
	int setUserreqStatus(String ticketid,String status);
	
	ArrayList<Support> getMsgList(String ticketid);
	ArrayList<Support> getExcutiveList();
	Support getSupportQueryData(String ticketid);
	int sendMsg(String ticketid,String who,String msg,String datetime , String type);
	int totalSupportRequest(String clinicid,String userid,String status,String fromdate,String todate);
	int setexcutiveSupport(String name,String ticketid);
	int countOfUserRequest(String clinicid,String userid,String status);
	int saveSupportRemark(String remark,String ticketid,String priority);
	int setReadAllMsg(String ticketid,String column);
	Clinic getClinicDetailsFromMaster(String clinicid);
	String userListFromThierIp(Clinic clinic);
	UserProfile getUserDetailsForSupport(String userid,String clinicid ,Clinic clinic);
	int setClinicActivDeactiveMaster(String clinicid, String status);
	ArrayList<Master> getModuleMasterList();
	ArrayList<UserProfile> relesedNotesList(String fromDate, String toDate);
	int saveReleasedNotes(UserProfile userProfile);
}
