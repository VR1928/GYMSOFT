package com.apm.Ipd.eu.bi;

import java.util.ArrayList;
import java.util.Collection;

import com.apm.DiaryManagement.eu.entity.NotAvailableSlot;
import com.apm.DiaryManagement.eu.entity.Priscription;
import com.apm.Emr.eu.entity.Investigation;
import com.apm.Ipd.eu.entity.Bed;
import com.apm.Master.eu.entity.Master;
import com.apm.TreatmentEpisode.eu.entity.TreatmentEpisode;
import com.apm.common.utils.Pagination;
import com.apm.common.web.common.helper.LoginInfo;

public interface BedDao {

	public ArrayList<Bed> getAllWardList(String action);
	public int addWard(String wardname,String plusminus,String procedure);
	public int addSection(String sectionname,String wardid);
	public ArrayList<Bed> getAllSectionList();
	public int addBed(Bed bed);
	public ArrayList<Bed> getAllBedList();
	public int addEquipment(Bed bed);
	public Bed getBed(int id);
	
	// admission data
	public int addIpdAdmissionForm(Bed bed,String date,String action,int maxid);
	public Bed getEditIpdData(String selectedid);
	public Bed getIpdDetails(String clientid); 
	public int updateIpdDetails(Bed bed, int id,String date);
	public NotAvailableSlot getClientLastOpdRecord(String clientid);
	public int updateBedStatus(String sessionadmissionid);
	
	
	public ArrayList<Bed> getAllWardList(Pagination pagination);

	public ArrayList<Bed> getAllSectionList(Pagination pagination);
	
	
	//public ArrayList<Bed> getAllBedList(Pagination pagination);
	public ArrayList<Bed> getEquipmentList();
	public ArrayList<Bed> getEquipmentList(Pagination pagination);

	public int deleteBed(String selectedid);
	public int updateBedEntries(Bed bed);
	public ArrayList<Bed> getSectionList(String wardid);
	public ArrayList<Bed> getBedList(String sectionid);
	public ArrayList<Bed> getEquipmentList(String bedid);
	public int getTotalWardCount();
	public Bed getWard(String selectedid);
	public int updateWardMaster(Bed bed);
	public int deleteWardMaster(String selectedid);
	public int getTotalSectionCount();
	public Bed getSection(String selectedid);
	public int updateSectionMaster(Bed bed);
	public int deleteSectionMaster(String selectedid);
	//public int getTotalBedCount();
	public int updateBedMaster(Bed bed);
	public int deleteBedMaster(String selectedid);
	public int getTotalEquipmentCount();
	public Bed getEquipment(String selectedid);
	public int updateEquipment(Bed bed);
	public int deleteEquipment(String selectedid);
	public int addCondition(int admissionid, Bed bed);
	public int updateIpdCondition(int admissionid, String conditionid);
	public ArrayList<Bed> getIpdConditionList(String selectedid);
	public int deleteIpdCondition(int id);
	public int saveAdmissionLog(Bed bed, String date, int admissionid);
	public int saveDischargeLog(String sessionadmissionid, String patientid,
			String ukCurrentDataTime,String bedid, LoginInfo loginInfo);
	public String getTreatmentName(String treatmentepisodeid);
	public Collection<Bed> getConditionList(String selectedid);
	public int saveBedChangeLog(Bed bed, String date, int admissionid,String logcommencing,String selectedshiftdata,int autosetcharge);
	public int changeBedStatus(String selectedid, String status);
	public ArrayList<String> getConditionNameList(ArrayList<Bed> ipdConditionids);
	public String getIpdConditionName(String conditionid);
	public ArrayList<Bed> getAllIpdActivePatients();
	public boolean getIsWardChange(int id, String wardid);
	public int deleteIpdConditionifExist(int admissionid,
			String treatmentepisodeid);
	public int addIpdConditionReport(int admissionid, Bed bed);
	public boolean isIpdExistCondition(String sessionadmissionid,
			String treatmentEpisodeid, String conditionid);
	public int updateCasualtyid(int id);
	public ArrayList<Bed> getAllIpdStdPatientList();
	public int updateStdChargeSetup(String ipdid);
	public int saveShiftBedLog(String admissiondate, int id, String wardid,
			String wardid2);
	public int saveGynicObsData(int admissionid, Bed bed2);
	public ArrayList<Bed> getGynicObsListByGynicId(String gynicid);
	public ArrayList<Bed> getGynicObsList(String ipdid);
	public int updateGynicObsData(Bed bed2);

	public ArrayList<Bed> getAllBedList(Pagination pagination, String searchText);


	public int getTotalBedCount(String searchText);
		
	public String getWardName(String wardid);
	public String getBedName(String wardid);
	public String getThirdPartyName(String tpid);
	public int updateCasualtybedEmpty(int id);
	public int getMaxIpdId();
	public int getMaxCasualtyID();
	public int leftCasualtyPatient(String selectedid);
	public ArrayList<Bed> getAllMasterWardList();
	public boolean checkBedidExixtst(String bedid);
	public String getHospitalCutoffTime(int clinicid);
	ArrayList<Priscription> gettreatmentlist(String selectedid);
	ArrayList<Investigation> getinvestigationlist(String selectedid);
	public Bed getRecentWardid(int id, String wardid);
	int updateCasualtybedEmptyByClient(String clientid);
	int getMaxDayCareId();
	boolean isDayCare(String ipdid);
	public boolean checkAlreadyAutoChargeApplied(String dt, String clientid, int id, String chargename);
	public int updateAdmissionDate(int id, String admissiondate);
	public ArrayList<Bed> getAllWardList(String action, String wardid, int usertype);
	public ArrayList<Master> getAllWardListNew(String action, String wardid, int userType);
	int updateUseridInTable(String userid, String treatmentepisodeId, String colname);
	String getLastDischargeUserId(String treatmentepisodeId, String colname);
	Bed newDischargeCardDetails(String ipdid);
	int makeEntryToNewDischargeFields(String clientId,String ipdid, String treatmentEpiId);
	boolean entryExistsInNewDischargeFields(String ipdid);
	int saveNewFieldsData(String ipdid, String column, String value);
	int bedCountOfWard(String wardid); 
	String dischargeEnDateOfPatient(String treatmentepisode);
}
