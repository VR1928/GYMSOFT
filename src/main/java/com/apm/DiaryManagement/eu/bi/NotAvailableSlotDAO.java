package com.apm.DiaryManagement.eu.bi;

import java.sql.Connection;
import java.util.ArrayList;

import com.apm.Accounts.eu.entity.Accounts;
import com.apm.Appointment.eu.entity.AppointmentType;
import com.apm.Diagnosis.eu.entity.Diagnosis;
import com.apm.DiaryManagement.eu.entity.Client;
import com.apm.DiaryManagement.eu.entity.DiaryManagement;
import com.apm.DiaryManagement.eu.entity.NotAvailableSlot;
import com.apm.DiaryManagement.eu.entity.Priscription;
import com.apm.Master.eu.entity.Master;
import com.apm.Registration.eu.entity.Location;
import com.apm.Registration.eu.entity.UserProfile;
import com.apm.ThirdParties.eu.entity.ThirdParty;
import com.apm.common.utils.Pagination;
import com.apm.common.web.common.helper.LoginInfo;

public interface NotAvailableSlotDAO  {
	
	String getDiaryUserName(String practitionerid) ;
	
	public ArrayList<DiaryManagement> getUserList(int clinicid,String commencing);
	ArrayList<Location> getLocationList(int id);
	ArrayList<NotAvailableSlot> getList(int id, String date);
	ArrayList<DiaryManagement> getUserList(int id);
	int saveAppointment(NotAvailableSlot notAvailableSlot);
	int saveBlockSlot(NotAvailableSlot notAvailableSlot,String opendb);
	ArrayList<AppointmentType> getAppointmentTypeList();
	int updateAppointment(NotAvailableSlot notAvailableSlot,int selectedid);
	int updateBlockSlot(NotAvailableSlot notAvailableSlot, int selectedid,String opendb);
	int updateClientHasArrived(int selectedid, int status);
	int updateClientIsBeingSeen(int selectedid, int status);
	int updateResetNotArrived(int selectedid, int status);
	int updateDNA(int selectedid, String notes, boolean dna,
			String dnaReason,String dnacharge);
	int saveCharge(NotAvailableSlot notAvailableSlot, String apmtType, int result);
	int updateCharge(NotAvailableSlot notAvailableSlot, String apmtType,
			int selectedid);
	
	ArrayList<NotAvailableSlot> getPrintDataOfWeek(String practionerId,
			String fromDate, String toDate);
	ArrayList<NotAvailableSlot> getPractitionerPrintData(String practionerId,
			String date);
	ArrayList<NotAvailableSlot> getAllPractitionerPrintData(String date,String enddate,String location,String diaryuser,String action,String opendb,String diaryuserid, String string, String toDate);
	String getAppointmentDuration(String connection);
	
	int UpdateDragAndDropData(String availableSlotID, String practitionerid,
			String startTime, String endTime,String slotid,String location,String commencing);
	int getSelecedTreatmentEpisodeId(int selectedid);
	ArrayList<NotAvailableSlot> getAvailableSlotList(String avlbltyDate,
			String chdiaryUser, String chlocation);
	boolean checkEventAllreadyExist(String commencing, String location,
			String diaryuserId, String starttime, String endtime);
	NotAvailableSlot getAvailableSlotdata(int editAppointId);
	boolean checkEventAllreadyExist(String commencing, String location,
			String diaryuserId, String starttime, String endtime,
			int editAppointId);
	ArrayList<NotAvailableSlot> getAllAvailableSlotList(String date,
			String diaryuserId, String location);
	String getDuration(String selectedid);
	
	 String getAppointmentTypeText(String id);
	int DeleteBlockedSlot(String selectedid,String opendb);
	int getStatus(String availableSlotID,String opendb);
	int saveDnaCharge(NotAvailableSlot notAvailableSlot, int selectedid);
	int saveApmInvoiceAssesment(NotAvailableSlot notAvailableSlot,
			int invoiceid, int appointmentid);
	int deleteDnaInvoiceAssesment(int selectedid);
	int deleteDnaInvoice(int selectedid);
	NotAvailableSlot getAvailableSlotDnadata(int selectedid);
	int updateSessions(int treatmeId);
	int getUserTotalCount(String opendb,String jobgroup);
	ArrayList<DiaryManagement> getDashBoardUserList(int id,
			Pagination pagination,String commencing,String opendb,String jobgroup);
	int saveApmtInLog(int appointmentid, String date, String time, String userId, String clientId, String commencingTemp, String sTime, String status,String lastModifiedDate);
	String getClientId(String availableSlotID);
	int saveDnaLog(String clientId, String date, String time, String userId,
			int selectedid);
	int getAppointmentSlotID(String commencing, int diaryUserId,String starttime,String endtime,String location);
	double getPendingBalanceTotal(String clientId);
	int getUsedSession(int treatmeId, String selectedid);
	ArrayList<NotAvailableSlot> getUsedSessionList(int treatmeId,
			int usedSession);
	int updateAllPrevious(int id, int treatmeId);
	String getConditionPatient(String clientId);
	int updateCondition(String clientId, String conditionID);
	int getUserListToalCount();
	int saveActivity(int clientId, int activity);
	int coutnEsistingSlot(String commencing, String location,
			String diaryuserId, String starttime, String endtime,
			int editAppointId);
	String getExistStartTime(String commencing, String location,
			String diaryuserId, String starttime, String endtime,
			int editAppointId);
	boolean checkDurationExist(String duration);
	String getEditExistStartTime(String commencing, String location,
			String diaryuserId, String starttime, String endtime,
			int editAppointId);
	double getDNAAercentage();
	ThirdParty getTPDNALimit(String clientId);
	NotAvailableSlot getApmtDetailsForLog(int selectedid);
	double getInvoiceCharge(int selectedid);
	int getDNAInvoicePayBy(int selectedid);
	int updateDNAInvoiceAssesment(String editdnaCharge, String dnapayby,int invoiceid);
	int getDNAInvoiceid(int selectedid);
	int saveCancelApmtInLog(int parseInt, String date, String time, String userId,
			String clientId, String commencingTemp, String sTime,
			String apmtstatus, String cancelApmtNote,String lastModifiedDate,NotAvailableSlot notAvailableSlot);
	
	int getPreviousTreatmentUsedSession(int selectedTreatmentEpisodeId);
	int updatePreviousTreatmentEpisode(int prevTreatmentSession,
			int selectedTreatmentEpisodeId);
	String getSpecificDnaCharge(int selectedid);
	int saveDnaApmInvoiceAssesment(NotAvailableSlot notAvailableSlot,
			int invoiceid,int appointmentid, String dnapercentage);
	int IncraeseDnaOffset(int selectedid, int usedsession);
	int getTpEpisodeUsedSession(String tepisodeid);
	int updateupdateTpEpisodeusedsession(String tepisodeid, int usedsession);
	int updateDnaOffset(boolean dnaOffset, int appointmentTypeid);
	int updateAppointmentDnaoffset(int selectedid, boolean dnaOffset);
	ArrayList<NotAvailableSlot> getDnaOffsetList(String treatmentEpisodeId);
	int updateAppointmentUsedSession(int id, int usedsession);
	int updateClientCondition(String clientId, String condition);
	NotAvailableSlot getInitilizedElementData(String diaryuserid,
			String commencing, String selectedStarttime, String prevEndTime);
	ArrayList<String> getRemainderAppointmentList(String dt);
	int getAdDurationData(int clinicid);
	String getDiaryUserFullname(String diaryuser);
	String getLocationName(String location);
	int saveLoggedInUserID(String userid);
	String getApmLoggedUserList(String clinicid);
	boolean checLoggedkUseridExist(String userid);
	String getSlotCommencingDate(String slotid);
	NotAvailableSlot getTreatmentDetails(int apptid);
	NotAvailableSlot getApmtConsNoteData(String apmtid);
	DiaryManagement getApmtSlotData(String date, String diaryuser,
			String location);
	String getDiaryUserIdName(int diaryUserId);
	boolean checkApmtExist(String clientId);
	int updateNewPtsStatus(String clientId, int sts);
	String getDiaryDuration(String slotid);
	ArrayList<Master> getMedicineList(String selectedid);
	ArrayList<Priscription> getFollowupApmtList(String followupdate);
	int updateAD(String appointmentid);
	ArrayList<UserProfile> getOTstaffList();
	int saveParenrotData(String commencing,String selectedot,String asistantdoctor,int apmtid);
	int checkotAppointment(String selectedid);
	int deleteOtApmt(int otid);
	ArrayList<UserProfile> getOTDoctorList();
	String getSelectedDiagnosisID(String editAppointId);
	int updateApmtDiagnosis(String editAppointId, String conid);
	boolean checkOtChargeExist(String editAppointId);
	int updateConsultationEmr(String editAppointId, String conid);
	int addMultiConditionstoEmr(String editAppointId, String moreconditions);
	String getDiagnosisNameFromID(String condition);
	ArrayList<Diagnosis> getMultipleConditions(String text);
	String getMultipleDiagnosis(String editAppointId);
	int updateWorkCompleted(int selectedid, String status,String datetime,String cancelnotes);
	double getCharge(String apmtType);
    int addOpdConditionReport(int apmtid,String clientid,String conditionid,String lastmodified);
	int deleteOpdConditionifExistsReport(String editAppointId);
	String getDiaryUserId(String appointmentid);
	ArrayList<Master> getProcedureList(String department);
	int updateOTdata(NotAvailableSlot notAvailableSlot, int selectedid);
	int deleteAsistantDoctor(int selectedid);
	int getOtAppointmentSlotID(String commencing, int parseInt, String time,
			String endTime, String location);
	int deleteBlockOt(String stafflistid);
	String getTemplateText(String id);
	NotAvailableSlot getOTData(String apmtid);
	int updateOtnotes(NotAvailableSlot notAvailableSlot, int id,String imageData);
	String getOtProcedure(String appointemntid);
	NotAvailableSlot getOTDataByIpd(String selectedid);
	ArrayList<DiaryManagement> getEditUserList(int id, String selectedid);
	int updateDischargeOtData(String sessionadmissionid,NotAvailableSlot notAvailableSlot);

	boolean checkifSequenceExist(String cdate,int diaryuserid);

	int getSqeunceNumber(String cdate,int diaryuserid);

	int InserCdateSeq(String cdate, int seqno,int appointmentid,int diaryuserid);

	ArrayList<Master> getAccountUserList();

	int getLastAppointmentId(String valueOf);

	String getDrNameFromApptId(int apptid);

	String getDrApptId(int apptid);
	ArrayList<DiaryManagement> getUserAccountList(int id);

	int saveOptionalForm(NotAvailableSlot notAvailableSlot);

	NotAvailableSlot getOptionalFormDetails(int id);

	ArrayList<NotAvailableSlot> getAllOptionFormList(String clientId);

	int updateOptionalForm(NotAvailableSlot notAvailableSlot);

	ArrayList<Master> getotDepartmenrList();

	
	int saveMultiImgData(String otnotesapmtid, String data22, String savemoreid, String otnotes, String trainer);

	boolean checkMultiOtDataExist(String otnotesapmtid, String savemoreid);

	int updateMultimgotData(String otnotesapmtid, String data22, String savemoreid, String otnotes);

	ArrayList<Master> getMultiOtImgList(String otnotesapmtid, String savemoreid);

	int getSpeciFromRefernce(int surgeonid);

	ArrayList<NotAvailableSlot> getOTNotesFormList(String clientId);

	NotAvailableSlot getProcedureDepartment(String editAppointId);

	int getSpeciSurgonFromRefernce(int parseInt);

	int updateimmurtizationchart(String clientId, String colname, String val);

	Master getImmurtizeData(String clientid);

	Boolean checkimmurtizationdata(String clientid);

	int saveimmurtizationdata(String clientid);

	ArrayList<DiaryManagement> getDisplayDashBoardUserList(int id, Pagination pagination, String commencing,
			String opendb, String selectedjobgroup,String resultstr);

	ArrayList<DiaryManagement> getIdDashBoardUserList(int id, Pagination pagination, String commencing, String opendb,
			String selectedjobgroup);
//lokesh 
	

	ArrayList<Master> getVaccinationdata(Client client);
	int getvacinedependacyvale(String dependid);
	int savevacinationimmunizationajax(String mastername,String masterid, String clientid,String givendate,String duedate);
	int checkvacinationimmunizationajaxData(String masterid, String clientid);
	int updatevacinationimmunizationajaxdate(String id, String givendate,String duedate);
	int updatevacinationimmunizationajaxRemark(String client,String masterid,String remark);
	Master getremarkNdate(String clientid,String masterid);
	int setdatatoVacinationInfo(Master master,String clientid);
	boolean checksmsflag(int masterid,String clientid);
	int setsmsflag(int masterid,String clientid);
	
	ArrayList<Master> getAllClientVaccinations(String date, LoginInfo loginInfo);
	String lmpDAte(String clientid);
	Master getVacinationInfo(String masterid,String clientid);
	ArrayList<Master> getVacinationForShow(String clientid);

	NotAvailableSlot getNewOpdDiaryUserData(String ndate, String nduserid);

	ArrayList<NotAvailableSlot> getNewOpdList(String ndate, String nduserid);

	int updateChargeStatus(String appointmentid);
	int saveDateOfOPDEvents(String appointmentid , String columnname);
	int saveDiagnosisOpd(String opdid,String diagnosisid);
	
	String getAllDiagnosisofOpd(String opdid);

	boolean chkmveapmtaxsist(String duserid, String commencing);

	NotAvailableSlot getMveDiaryUserDetails(String duserid, String commencing);

	String getmveapmtendtime(String duserid, String commencing);

	int updatemveappointment(NotAvailableSlot n);

	int updatemveapminvoice(String editAppointId,NotAvailableSlot n);

	Accounts getmveapmtchargeinfo(String editAppointId);

	int updatemveapminvoiceassesmnt(int id, NotAvailableSlot n);

	int updatemveapmtchargesinvoice(int invoiceid, NotAvailableSlot n);
	String getGivenDate(int dependson,String clientid);
	int updatedueDateOfVaccine(String clientid, String masterid, String date);
	
	void startImmnunizationAppTProccess(String date, LoginInfo loginInfo);
	int updateDueDate(String value, String masterid, String clientid);
	String getDueDate(String masterid, String clientid);
	int getVaccinator();
	
	boolean bookededStatsu(String clientid);
	String getDOBChangeLogDate(String clientId);
	int insertDobChangeLog(String clientId, String dob);
	int updateScheduledDateAfterDOBChange(String masterid, String date, String clientId);

	int insertTrainerData(NotAvailableSlot notAvailableSlot);

	ArrayList<NotAvailableSlot> getTrainerNotes(String fromDate, String toDate);
}
