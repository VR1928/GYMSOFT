 package com.apm.Ipd.web.action;
import java.sql.Connection;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts2.ServletActionContext;

import com.apm.Accounts.eu.bi.AccountsDAO;
import com.apm.Accounts.eu.blogic.jdbc.JDBCAccountsDAO;
import com.apm.Accounts.eu.entity.Accounts;
import com.apm.AssesmentForms.eu.bi.AssessmentFormDAO;
import com.apm.AssesmentForms.eu.blogic.jdbc.JDBCAssessmentFormDAO;
import com.apm.DiaryManagement.eu.bi.ClientDAO;
import com.apm.DiaryManagement.eu.bi.DiaryManagementDAO;
import com.apm.DiaryManagement.eu.bi.NotAvailableSlotDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCClientDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCDiaryManagentDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCNotAvailableSlotDAO;
import com.apm.DiaryManagement.eu.entity.Client;
import com.apm.DiaryManagement.eu.entity.DiaryManagement;
import com.apm.DiaryManagement.eu.entity.NotAvailableSlot;
import com.apm.Inventory.eu.bi.InventoryProductDAO;
import com.apm.Inventory.eu.bi.InventoryVendorDAO;
import com.apm.Inventory.eu.blogic.jdbc.JDBCInventoryProductDAO;
import com.apm.Inventory.eu.blogic.jdbc.JDBCInventoryVendorDAO;
import com.apm.Inventory.eu.entity.Product;
import com.apm.Ipd.eu.bi.BedDao;
import com.apm.Ipd.eu.bi.IpdDAO;
import com.apm.Ipd.eu.blogic.jdbc.JDBCBedDao;
import com.apm.Ipd.eu.blogic.jdbc.JDBCIpdDAO;
import com.apm.Ipd.eu.entity.Bed;
import com.apm.Ipd.web.form.IpdForm;
import com.apm.Master.eu.bi.MasterDAO;
import com.apm.Master.eu.blogic.jdbc.JDBCMasterDAO;
import com.apm.Master.eu.entity.Master;
import com.apm.Pharmacy.eu.bi.PharmacyDAO;
import com.apm.Pharmacy.eu.blogic.jdbc.JDBCPharmacyDAO;
import com.apm.Registration.eu.bi.ClinicDAO;
import com.apm.Registration.eu.bi.UserProfileDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCClinicDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCUserProfileDAO;
import com.apm.Registration.eu.entity.Location;
import com.apm.Registration.eu.entity.UserProfile;
import com.apm.ThirdParties.eu.bi.ThirdPartyDAO;
import com.apm.ThirdParties.eu.blogic.jdbc.JDBCThirdPartyDAO;
import com.apm.ThirdParties.eu.entity.ThirdParty;
import com.apm.TreatmentEpisode.eu.bi.TreatmentEpisodeDAO;
import com.apm.TreatmentEpisode.eu.blogic.jdbc.JDBCTreatmentEpisode;
import com.apm.TreatmentEpisode.eu.entity.TreatmentEpisode;
import com.apm.common.eu.blogic.jdbc.Connection_provider;
import com.apm.common.utils.DateTimeUtils;
import com.apm.common.web.action.BaseAction;
import com.apm.common.web.common.helper.LoginHelper;
import com.apm.common.web.common.helper.LoginInfo;
import com.apm.common.web.utils.PopulateList;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
public class IpdAjaxAction extends BaseAction implements ModelDriven<IpdForm>{
	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
	HttpServletResponse response = (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
	HttpSession session = request.getSession(true);
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	IpdForm ipdForm = new IpdForm();
	public IpdForm getModel() {
		return ipdForm;
	}
	public String ipdformdata() throws Exception {
		if(!verifyLogin(request)) {
			return "login";
		}
		String wardid = request.getParameter("wardid");
		String bedid = request.getParameter("bedid");
		String action = request.getParameter("action");
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			IpdDAO ipdDAO = new JDBCIpdDAO(connection);
			UserProfileDAO userProfileDAO=new JDBCUserProfileDAO(connection);
			MasterDAO masterDAO=new JDBCMasterDAO(connection);
			ClientDAO clientDAO=new JDBCClientDAO(connection);
			BedDao bedDao = new JDBCBedDao(connection);
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			TreatmentEpisodeDAO treatmentEpisodeDAO = new JDBCTreatmentEpisode(connection);
			
			ArrayList<Bed> wardlist=bedDao.getAllWardList(action);
			ipdForm.setWardlist(wardlist);
			ArrayList<Bed>bedList = ipdDAO.getWardWiseBedList(wardid);
			DiaryManagementDAO diaryManagementDAO = new JDBCDiaryManagentDAO(connection);
			String clientid= ipdForm.getClientid();
			Client client=clientDAO.getClientDetails(clientid);
			String practitionerid= ipdForm.getPractitionerid();
			ipdForm.setBedlist(bedList);
			ipdForm.setClientid(clientid);
			ipdForm.setWardid(wardid);
			ipdForm.setBedid(bedid);
			ipdForm.setTpid(client.getTypeName());
			ipdForm.setPractitionerid(practitionerid);
			String chief_comlint_id=masterDAO.getIpdTemplateId("Chief Complaints");
			String present_ill_id=masterDAO.getIpdTemplateId("Present Illness");
			String past_history_id=masterDAO.getIpdTemplateId("Past History");
			String family_hist_id=masterDAO.getIpdTemplateId("Family History");
			String personal_hist_id=masterDAO.getIpdTemplateId("Personal History");
			String onexami_id=masterDAO.getIpdTemplateId("On Examination");
			String tretment_given_id=masterDAO.getIpdTemplateId("Treatment Given");
			UserProfile userProfile=  userProfileDAO.getUserprofileDetails(Integer.parseInt(practitionerid));
			String specializationId=  userProfile.getDiciplineName();
			ipdForm.setDepartment(userProfile.getSpecialization());
			if(loginInfo.getClinicUserid().equals("iconhospital")){
			if(specializationId.equals("20")||specializationId.equals("21")){
				ipdForm.setWeightsts("true");
			}else{
				ipdForm.setWeightsts("false");
			}
			}else{
				ipdForm.setWeightsts("false");
			}
			boolean issystemicreview= masterDAO.isIpdFormFieldActive(specializationId,"Systemic Review");
			boolean history= masterDAO.isIpdFormFieldActive(specializationId,"History");
			boolean obstretic_history = masterDAO.isIpdFormFieldActive(specializationId,"OBSTRETIC HISTORY");
			boolean menstrual_history = masterDAO.isIpdFormFieldActive(specializationId, "MENSTRUAL HISTORY"); 
			boolean substance_history = masterDAO.isIpdFormFieldActive(specializationId, "SUBSTANCE HISTORY");
			boolean peditric = masterDAO.isIpdFormFieldActive(specializationId, "Paediatric History");
			ipdForm.setIssystemicreview(issystemicreview);
			ipdForm.setHistory(history);
			ipdForm.setObstretic_history(obstretic_history);
			ipdForm.setMenstrual_history(menstrual_history);
			ipdForm.setSubstance_history(substance_history);
			ipdForm.setPaediatrichist(peditric);
			ArrayList<Master> chief_complaints_list=masterDAO.getIpdTemplateListNames(chief_comlint_id);
			ArrayList<Master> present_illness_list=masterDAO.getIpdTemplateListNames(present_ill_id);
			ArrayList<Master> past_history_list=masterDAO.getIpdTemplateListNames(past_history_id);
			ArrayList<Master> family_history_list=masterDAO.getIpdTemplateListNames(family_hist_id);
			ArrayList<Master> personal_hist_list=masterDAO.getIpdTemplateListNames(personal_hist_id);
			ArrayList<Master> on_exam_list=masterDAO.getIpdTemplateListNames(onexami_id);
			ArrayList<Master> treatment_given_list=masterDAO.getIpdTemplateListNames(tretment_given_id);
			ipdForm.setChief_complaints_list(chief_complaints_list);
			ipdForm.setPresent_illness_list(present_illness_list);
			ipdForm.setPast_history_list(past_history_list);
			ipdForm.setFamily_history_list(family_history_list);
			ipdForm.setPersonal_hist_list(personal_hist_list);
			ipdForm.setOn_exam_list(on_exam_list);
			ipdForm.setTreatment_given_list(treatment_given_list);
			int selectedid = loginInfo.getId();
			ClinicDAO clinicListDAO = new JDBCClinicDAO(connection);
			com.apm.Registration.eu.entity.Clinic cliniclist = clinicListDAO.getCliniclistDetails(selectedid);
			ipdForm.setClinicName(cliniclist.getClinicName());
			
			
			//prepapre 
			
			ArrayList<Client> clientOccupationList = clientDAO.getOccupationList();
			Client client1 = new Client();
			client1.getOther();
			clientOccupationList.add(client1);
			
			ipdForm.setClientOccupationList(clientOccupationList);
			ipdForm.setCountryList(PopulateList.countryList());
			
			//user define date time
			ipdForm.setHourList(PopulateList.hourList());
			ipdForm.setMinuteList(PopulateList.getMinuteList());
			
			ArrayList<Accounts>thirdPartyList = accountsDAO.getThirdPartyList(loginInfo.getId());
			ipdForm.setThirdPartyList(thirdPartyList);
			
			ArrayList<UserProfile> mlcdrlist = userProfileDAO.getAllPractitionerList(null,null,null,null,"1");
			ipdForm.setMlcdrlist(mlcdrlist);
			
			ArrayList<DiaryManagement> userList = notAvailableSlotDAO.getUserList(loginInfo.getId());
			ipdForm.setUserList(userList);
			
			//Akash 25 dec 2017 list get from referal table
			ArrayList<Client> refrenceList = clientDAO.getReferenceList();
			ipdForm.setRefrenceList(refrenceList);
			
			ArrayList<Location>locationList = diaryManagementDAO.getLocationList(loginInfo.getId());
			ipdForm.setLocationList(locationList);
			
			ArrayList<String> departmentList= diaryManagementDAO.getDepartmentList();
			ipdForm.setDepartmentList(departmentList);
			
			ArrayList<String> initialList = clientDAO.getInitialList();
			ipdForm.setInitialList(initialList);
			
			//set state and city list
			InventoryVendorDAO vendorDAO=new JDBCInventoryVendorDAO(connection);
			ArrayList<Master> statelist= vendorDAO.getAllStateList();
			ipdForm.setStatelist(statelist);
			
			ArrayList<Master> citylist= vendorDAO.getAllCityList();
			ipdForm.setCitylist(citylist);
			
			ArrayList<Client> diagnosisList=clientDAO.getEmrTreatmentTypeList();
			ipdForm.setDiagnosisList(diagnosisList);
			
			ArrayList<Client> surgeryList = clientDAO.getSurgeryList();
			if(surgeryList.size()==0){
				surgeryList = new ArrayList<Client>();
			}
			Client client3 = new Client();
			client3.getOther();
			surgeryList.add(client3);
			ipdForm.setSurgeryList(surgeryList);
			
			ArrayList<Client> thirdPartyTypeList = new ArrayList<Client>();
			thirdPartyTypeList = clientDAO.getThirdPartyType();
			ipdForm.setThirdPartyTypeList(thirdPartyTypeList);
			
			ArrayList<Client> condtitionList = clientDAO.getEmrTreatmentTypeList();
			ipdForm.setCondtitionList(condtitionList);
			
			ArrayList<TreatmentEpisode> sourceOfReferralList = treatmentEpisodeDAO.getSourceOfReferralList();
			ipdForm.setSourceOfReferralList(sourceOfReferralList);
			ipdForm.setActiontype(action);
			if(action.equals("1")||action.equals("2")){
				return "addipdcasualty";
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
			connection.close();
		}
		
		return INPUT;
		
	}
		
	public String admitted()throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		
		Connection connection = null;
		String clientid = request.getParameter("clientid");
		try{
			connection = Connection_provider.getconnection();
			AssessmentFormDAO assessmentFormDAO = new JDBCAssessmentFormDAO(connection);
			int bedid = assessmentFormDAO.getIpdBedno(clientid);
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+bedid+"");
			
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			connection.close();
		}
		
		
		return null;
	}
	
public String clientinfo()throws Exception{
		
		if(!verifyLogin(request)) {
    		
    		return "login";
    	}
    	
    	Connection connection=null;
    	
    	String clientid = request.getParameter("clientid");
    	
    	try {
	
    		connection=Connection_provider.getconnection();
    		BedDao bedDao=new JDBCBedDao(connection);
    		ThirdPartyDAO thirdPartyDAO = new JDBCThirdPartyDAO(connection);
    		
    		NotAvailableSlot notAvailableSlot = bedDao.getClientLastOpdRecord(clientid);
    		IpdDAO ipdDAO=new JDBCIpdDAO(connection); 
    		
    		ClientDAO clientDAO = new JDBCClientDAO(connection);
    		Client client = clientDAO.getPatient(Integer.parseInt(clientid));
    		
    		String fullname = client.getTitle() + " " + client.getFirstName() + " "+client.getMiddlename()+" " + client.getLastName();
    		String tp = clientDAO.getTPCompanyName(client.getTypeName());
    		
    		ThirdParty thirdParty = thirdPartyDAO.getThirdPartyDetails(client.getTypeName());
    		
    		String age = DateTimeUtils.getAge(client.getDob());
    		String agegender="";
    		if(Integer.parseInt(age)<2){

				String monthdays= DateTimeUtils.getMonthandDays(client.getDob());
				agegender=monthdays+" / "+client.getGender();
			} else {
				agegender = age + "Years" + " / " + client.getGender();	
			}
    		age=DateTimeUtils.getAge1(client.getDob());
    		agegender= age +" / "+ client.getGender();	
    	    String num_admission=ipdDAO.getNumofAdmissionCount(clientid);
    	    String abrivationid= client.getAbrivationid();
    	    
    	    String reference=clientDAO.getReferenceName(client.getReference());
    		if(client.getEmergencyContName()==null){
    			client.setEmergencyContName("");
    		}
    		
    		if(client.getEmergencyContNo()==null){
    			client.setEmergencyContNo("");
    		}
    		if(client.getReference()==null){
    			client.setReference("");
    		}
    		if(client.getRelation()==null){
    			
    			client.setRelation("");
    		}
    		if(client.getAbrivationid()==null){
    			client.setAbrivationid("");
    		}
    		int lastipdid=ipdDAO.getLastIpdId(clientid);
    		Bed bed= bedDao.getEditIpdData(""+lastipdid);
    		bed.setPractitionerid(DateTimeUtils.numberCheck(bed.getPractitionerid()));
    		
    		UserProfileDAO ueUserProfileDAO= new JDBCUserProfileDAO(connection);
    		UserProfile userProfile= ueUserProfileDAO.getUserprofileDetails(Integer.parseInt(bed.getPractitionerid()));
    		String dept=userProfile.getSpecialization();
    		
    		String clientpay="0";
    		if(client.getWhopay()==null||client.getWhopay().equals("")||client.getWhopay().equals("Client")||client.getWhopay().equals("0")){
    			clientpay="0";	
    		}else{
    			clientpay="1";
    		}
    		
    		String clientInfo = fullname + "~" + notAvailableSlot.getDiaryUser() + "~" + notAvailableSlot.getCondition() + 
    				"~" + notAvailableSlot.getTreatmentEpisodeId() + "~" + tp + "~" + client.getWhopay() + "~" + clientid +
    				"~" + client.getPolicyNo() + "~" +agegender+"~"+client.getAddress()+"~"+client.getDob()+"~"+client.getEmergencyContName()+"~"+client.getEmergencyContNo()+"~"+client.getRelation()+"~"+num_admission+"~"+reference+"~"+abrivationid+"~"+bed.getPractitionerid()+"~"+dept+"~"+clientpay;
    		
    		response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+clientInfo+"");
    		
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	finally{
			connection.close();
		}
    	return null;
	}

public String returnipdmedicine() throws Exception{
	 Connection connection=null;  
	 try {
		 connection=Connection_provider.getconnection();
		 PharmacyDAO pharmacyDAO=new JDBCPharmacyDAO(connection);
		 BedDao bedDao = new JDBCBedDao(connection);
		 ClientDAO clientDAO = new JDBCClientDAO(connection);
		 String clientid = request.getParameter("ipdclientid");
		 String ispharmacy = "0";
		 String ipdid = request.getParameter("ipdid");
		 boolean flag =false;
		 if(clientid==null){
			 clientid = (String)session.getAttribute("nursebyreturnclientid");
			 ipdid = (String) session.getAttribute("nursebyreturnipd");
			 flag =true;
		 }
		 
		 if(ipdid!=null){
			 if(ipdid.equals("")){
				 ipdid ="0";
			 }
		 }else{
			 ipdid="0";
		 }
		 
		 
		 String pract_name ="";
		 if(ipdid.equals("0")){
			pract_name = pharmacyDAO.getappointmentinfo(Integer.parseInt(clientid));
		 }else{
			Bed  bed = bedDao.getEditIpdData(ipdid);
			UserProfileDAO profileDAO = new JDBCUserProfileDAO(connection);
			pract_name = profileDAO.getName(bed.getPractitionerid());
		 }
		 Client client = clientDAO.getClientDetails(clientid);
		 String mob= client.getMobNo();
		 String address=client.getAddress();
		 String clientname = client.getTitle()+" "+client.getFirstName()+" "+client.getLastName();
		 
		 ipdForm.setFullname(clientname);
		 ipdForm.setMobile(mob);
		 ipdForm.setAddress(address);
		 ipdForm.setPractitionerName(pract_name);
		 ArrayList<Product> allMedicieneList = pharmacyDAO.getAllMedicinesForIPDReturn(clientid,ispharmacy,"0",1);
		 ipdForm.setAllMedicieneList(allMedicieneList);
		 ipdForm.setIpdid(ipdid);
		 ipdForm.setClientid(clientid);
		 ipdForm.setIspharmacy(ispharmacy);
		 if(flag){
			 
		 }
		 
		 if(flag){
			 	ipdForm.setSucessmsg("Return request send sucessfully in pharmacy");
				addActionMessage("Return request send sucessfully in pharmacy");
		 }else{
			 	ipdForm.setSucessmsg("0");
		 }
	} catch (Exception e) {
		e.printStackTrace();
	}finally{
		connection.close();
	}
	 return "returnmedicinenurse";
}
	
}
