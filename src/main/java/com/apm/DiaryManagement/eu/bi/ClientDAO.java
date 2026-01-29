package com.apm.DiaryManagement.eu.bi;

import java.sql.Connection;
import java.util.ArrayList;

import com.apm.DiaryManagement.eu.entity.Client;
import com.apm.DiaryManagement.eu.entity.GpData;
import com.apm.DiaryManagement.eu.entity.NotAvailableSlot;
import com.apm.Ipd.eu.entity.Bed;
import com.apm.Master.eu.entity.Master;
import com.apm.common.utils.Pagination;
import com.apm.common.web.common.helper.LoginInfo;

public interface ClientDAO {
	
	String  getcityfromid(String id);
	String  getstatefromid(String id);

	ArrayList<Client> getAllPatient(int id);

	int savePatient(Client client, int i);

	Client getPatient(int id);

	int updatePatient(Client client, int id,LoginInfo loginInfo);

	int deleteClient(int id, Client client);

	ArrayList<Client> getThirdPartyType();

	ArrayList<Client> getThirdPartyTypeName();

	ArrayList<Client> getThirdTypeNameList(int id);

	int updateThirdPartyDetails(Client client, String id);

	ArrayList<Client> getOccupationList();
	ArrayList<Client> getOccupaList();

	ArrayList<Client> getReferenceList();

	int insertOtherOccupation(Client client, String otherOccupation);

	int insertOtherReference(Client client, String otherReference);

	ArrayList<String> getInitialList();

	ArrayList<Client> getClient(String searchClient,int id);

	ArrayList<Client> getSourceOfIntroList();

	int getTotalClientCount(int id,boolean isShowAll,String diaryuser);

	ArrayList<Client> getAllPatient(Pagination pagination,int id,boolean isShowAll,String diaryUser);

	int savePatientDetails(Client client,int id);

	ArrayList<Client> getReferList();

	ArrayList<Client> getInitailList();

	int updateThirdParty(Client client, String clientId, int id);

	String getThirdPartyCompanyName(String companyName);

	String getClientFullName(String id);

	int getTotalClientCountOfSearch(int id, String searchClient,String status,boolean isShowAll,String diaryuser);

	ArrayList<Client> getClientofSearch(Pagination pagination,
			String searchClient, int id,String status,boolean isShowAll,String diaryuser);

	int changeStatusToActive(String id);

	int changeStatusToInactive(String id);

	

	Client getClientDetails(String clientId);

	ArrayList<Client> getAllClientOfThirdParty(String id);

	String getDeclaration(int id);

	String getTitleOfDeclaration(int clientId);

	String getPractitionerName(String clientId);

	String getClinicName(int id);

	int saveNewPatient(Client client, int id);

	boolean isMobExist(String mob);

	boolean isEmailIdExist(String email);

	int deleteNote(String id);

	int updateNote(String id, String note);

	String getNote(String id);

	int saveNote(String id, String note);

	String getThirdPartyCompanyEmail(String id);

	int getTotalClientUnderPCount(String practionerId);

	ArrayList<Client> getAllPatientUnderP(Pagination pagination,
			String practionerId);

	ArrayList<Client> getGpList();

	Client getGPDeatils(String id);

	String getTPCompanyName(String typeName);

	ArrayList<Client> getTreatmentTypeList();

	String getSourceOfIntro(String sourceOfIntro);

	ArrayList<GpData> getGPDataList(String surgeryid);

	String getTpIdDetails(String gpname);

	int saveGPData(String popgptype, String gptypeNamepopup, String gpnameid,
			String workphno, String gpemailid, String gpfax, String gpNote);

	ArrayList<Client> getSurgeryList();

	String getWhoPayName(String clientId);

	int insertOtherSourceOfIntro(Client client, String otherSourceOfIntro);

	int insertOtherCondition(Client client, String otherCondition);

	String getClientEmail(String id);

	int getMaxIdOfRefernce();

	int getMaxIdOfOccupation();

	int getMaxIdOfSourceOfIntro();

	int getMaxIdOfCondition();

	int updateWhoPay(String patientId, String whopay);

	String getClientID(String userid);

	String getGPClientID(String userid);

	Client getSelectedSessionClientDetails(String clientid);

	NotAvailableSlot getLastAppointmentdetails(String clientId);

	int savePureSevaClient(LoginInfo loginInfo);

	boolean checkEmailidExist(String email);

	int getPureSevaClientid(String email);

	int saveCiimsClients(String title, String firstname, String lastname,
			String email, String mob, String dob, String gender,String address,String opdno);

	ArrayList<Master> getDeclerationTitleList();

	int updateAllDec();

	int updateDec(String id);

	String getSelectedDecId();

	String getIpdDischargeDate(String addmissionid);

	String getGPname(String gpid);

	int updateClientUserImage(String userImageFileName,int clientid);

	int updatePayeeofPatient(String clientid, String payee, String tpid);
	ArrayList<Client> getDepartmentTreatmentTypeList(String location);

	int updateEmergencyDetails(Client clientcont);

	String getReferenceName(String reference);

	int saveBMIPatient(Client client);

	Client getPatientBMIData(String clientid, int opdid);

	ArrayList<Client> getEmrTreatmentTypeList();
	public String getTpIDformPatient(String patientid);
	boolean checkifSequenceExist(String cdate);
	int getSqeunceNumber(String cdate);
	int InserCdateSeq(String cdate,int seqno);
	String getClinicAbrivation(int clinicid);
	ArrayList<Client> getAllPatient();
	String getIpdPractionerName(String clientId);
	String getIpdPractionerSpeciality(String clientId);
	String getPractitionerSpeciality(String clientId);
	ArrayList<Client> getAnesthesiaList();
	String getIpdPractionerId(String clientId);
	int saveDeclaration(Client client);
	Client getDeclarationData(int ipdid);
	ArrayList<Client> getDeclarationDataList(String clientId);
	Client getDeclarationByID(String id);
	int updateDeclarationData(Client client);
	Bed getdischargedata(String string);
	int check_child_growth_data(String clientId, String month);
	int updateChildGrowthData(Client client, String val);
	int saveChildGrowthData(Client client, String val);
	ArrayList<Client> getHeightMasterList(String gender, int heightcount);
	ArrayList<Client> getWeightMasterList(String gender, int weightcount);
	ArrayList<Client> getBmiMasterList(String gender, int bmicount);
	ArrayList<Client> getHeadMasterList(String gender, int headcount);
	ArrayList<Client> getClientGrowthList(String clientId);
	ArrayList<Client> getLengthHeightMasterList(String gender, int lengthcount);
	int getHeightMasterCount(String gender);
	int getWeightMasterCount(String gender);
	int getBmiMasterCount(String gender);
	int getHeadMasterCount(String gender);
	int getLengthHeightMasterCount(String gender);
	int getHeightDataCount(String gender);
	int getWeightDataCount(String gender);
	int getBmiDataCount(String gender);
	int getHeadDataCount(String gender);
	int getLengthHeightDataCount(String gender);
	ArrayList<Client> getClientGrowthHeightList(String clientId, int heightcount, int heightdatacount);
	ArrayList<Client> getClientGrowthWeightList(String clientId, int heightcount);
	ArrayList<Client> getClientGrowthBmiList(String clientId, int heightcount);
	ArrayList<Client> getClientGrowthHeadList(String clientId, int heightcount);
	ArrayList<Client> getClientGrowthLengthList(String clientId, int heightcount);
	String getClinicAbrivationFromUserid(String databasename); 

	ArrayList<Client> getallFeedbacks(String treatmenttype);
	int saveOPDFeedBackParent(Client client);
	String getparentId(Client client);
	int saveChildFeedBack(Client client);
	int saveIPDFeedBackParent(Client client);
	String getparentIdIPD(Client client);
	ArrayList<Client> getpatientlistForFeedback(String type);
	ArrayList<Client> getallfeedbacksbyPatient(String dept);
	ArrayList<Client> printfeedbackForm(String parentid);
	String getProceFromClientId(String clientid);
	ArrayList<Client> getAllPatientSorted(int id);
	String getClientWeight(String clientid);
	ArrayList<Client> getbdaylistPatients(String dob,LoginInfo loginInfo);
	int savefollowup(Client client);
	ArrayList<Client> getallfollowupsToDash(String practid, String type, String fromdate, String todate );
	int setfollowupsmsflag(String id);
	String gettptypenamebyid(String typeName);
	String getwardnamebyid(String wardid);
//	String drmobno(int practitionerid);
	int setFollowupStatus(String id, String status,String followdate);
	ArrayList<Client> getApmtNameList(String mastername, String chargecolumnname, String tpid);
	Client getAppointmentType(String masterid, String chargecolumnname);
	int updateAssessment(Client client2, String assesmentid, String ipdid);
	String getopdnotes(String clientid); 
	Client getBMIData(String clientid, String appointmentid);
	Client getChildGrowthData(String clientId, String val, String month);
	ArrayList<Client> getVaccinListdataOfAll(String fromdate, String todate, String type);
	void startFollowUpSMSProcess(Connection connection, String date);
	ArrayList<Client> getAllPatientByApmtid(int id);
	int documentId(String docName);
	String documetData(String docId);
	void addToHISDocumentLog(String docId,String docValue, String clientId);
	String documentValueFromLog(String docName, String clientId);
	Client feebackDetails(String parentId); 
	int clientIdFromIPDOPD(String Type, String id);
	Client avgOfAllFeedBack(String feedBackId);
}
