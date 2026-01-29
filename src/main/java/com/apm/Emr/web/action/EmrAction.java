package com.apm.Emr.web.action;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.apm.Emr.eu.entity.FileName;
import com.apm.Emr.eu.entity.Investigation;
import com.apm.Emr.eu.entity.MedicalHistory;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts2.ServletActionContext;

import com.apm.Accounts.eu.bi.AccountsDAO;
import com.apm.Accounts.eu.blogic.jdbc.JDBCAccountsDAO;
import com.apm.Appointment.eu.bi.AppointmentDAO;
import com.apm.Appointment.eu.blogic.jdbc.JDBCAppointmentDAO;
import com.apm.Appointment.eu.entity.AppointmentType;
import com.apm.AssesmentForms.eu.bi.AssessmentFormDAO;
import com.apm.AssesmentForms.eu.bi.ImportImageForAssessmentDAO;
import com.apm.AssesmentForms.eu.bi.ListAssessmentDAO;
import com.apm.AssesmentForms.eu.blogic.jdbc.JDBCAssessmentFormDAO;
import com.apm.AssesmentForms.eu.blogic.jdbc.JDBCImportImageAssessmentDAO;
import com.apm.AssesmentForms.eu.blogic.jdbc.JDBCListAssessmentFormDAO;
import com.apm.AssesmentForms.eu.entity.Assessment;
import com.apm.AssesmentForms.web.action.Template;
import com.apm.Diagnosis.eu.bi.DiagnosisDAO;
import com.apm.Diagnosis.eu.blogic.jdbc.JDBCDiagnosisDAO;
import com.apm.Diagnosis.eu.entity.Diagnosis;
import com.apm.DiaryManagement.eu.bi.ClientDAO;
import com.apm.DiaryManagement.eu.bi.CompleteAptmDAO;
import com.apm.DiaryManagement.eu.bi.NotAvailableSlotDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCClientDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCCompleteAptmDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCNotAvailableSlotDAO;
import com.apm.DiaryManagement.eu.entity.Client;
import com.apm.DiaryManagement.eu.entity.CompleteAppointment;
import com.apm.DiaryManagement.eu.entity.DiaryManagement;
import com.apm.DiaryManagement.eu.entity.NotAvailableSlot;
import com.apm.DiaryManagement.eu.entity.Priscription;
import com.apm.DiaryManagement.web.action.EmbeddedImageEmailUtil;
import com.apm.DiaryManagement.web.action.SmsService;
import com.apm.Dietary.eu.bi.DietaryDAO;
import com.apm.Dietary.eu.bi.DietaryDetailsDAO;
import com.apm.Dietary.eu.blogic.jdbc.JDBCDietaryDAO;
import com.apm.Dietary.eu.blogic.jdbc.JDBCDietaryDetailsDAO;
import com.apm.Dietary.eu.entity.Dietary;
import com.apm.Dietary.eu.entity.DietaryDetails;
import com.apm.Emr.eu.bi.ConsultationNoteDAO;
import com.apm.Emr.eu.bi.EmrDAO;
import com.apm.Emr.eu.bi.InvestigationDAO;
import com.apm.Emr.eu.bi.PrescriptionDAO;
import com.apm.Emr.eu.blogic.jdbc.JDBCConsultationNoteDAO;
import com.apm.Emr.eu.blogic.jdbc.JDBCEmrDAO;
import com.apm.Emr.eu.blogic.jdbc.JDBCInvestigationDAO;
import com.apm.Emr.eu.blogic.jdbc.JDBCPrescriptionDAO;
import com.apm.Emr.eu.entity.Emr;
import com.apm.Emr.web.form.EmrForm;
import com.apm.InstantMessage.web.action.Email;
import com.apm.Inventory.eu.bi.InventoryProductDAO;
import com.apm.Inventory.eu.blogic.jdbc.JDBCInventoryProductDAO;
import com.apm.Inventory.eu.entity.Product;
import com.apm.Ipd.eu.bi.BedDao;
import com.apm.Ipd.eu.bi.IpdDAO;
import com.apm.Ipd.eu.blogic.jdbc.JDBCBedDao;
import com.apm.Ipd.eu.blogic.jdbc.JDBCIpdDAO;
import com.apm.Ipd.eu.entity.Bed;
import com.apm.Log.eu.entity.EmailLetterLog;
import com.apm.Master.eu.bi.MasterDAO;
import com.apm.Master.eu.bi.PrescriptionMasterDAO;
import com.apm.Master.eu.bi.TreatmentTypeDAO;
import com.apm.Master.eu.blogic.jdbc.JDBCMasterDAO;
import com.apm.Master.eu.blogic.jdbc.JDBCPrescriptionMasterDAO;
import com.apm.Master.eu.blogic.jdbc.JDBCTreatmentTypeDAO;
import com.apm.Master.eu.entity.Discharge;
import com.apm.Master.eu.entity.Master;
import com.apm.Master.eu.entity.TreatmentType;
import com.apm.Master.web.action.PrescriptionDetailsAction;
import com.apm.Pacs.eu.entity.Pacs;
import com.apm.Pharmacy.eu.bi.PharmacyDAO;
import com.apm.Pharmacy.eu.blogic.jdbc.JDBCPharmacyDAO;
import com.apm.Registration.eu.bi.ClinicDAO;
import com.apm.Registration.eu.bi.UserProfileDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCClinicDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCUserProfileDAO;
import com.apm.Registration.eu.entity.Clinic;
import com.apm.Registration.eu.entity.Location;
import com.apm.Registration.eu.entity.UserProfile;
import com.apm.Tools.eu.bi.EmailTemplateDAO;
import com.apm.Tools.eu.blogic.jdbc.JDBCEmailTemplateDAO;
import com.apm.Tools.web.action.AllTemplateAction;
import com.apm.TreatmentEpisode.eu.bi.TreatmentEpisodeDAO;
import com.apm.TreatmentEpisode.eu.blogic.jdbc.JDBCTreatmentEpisode;
import com.apm.TreatmentEpisode.eu.entity.TreatmentEpisode;
import com.apm.common.eu.blogic.jdbc.Connection_provider;
import com.apm.common.utils.DateTimeUtils;
import com.apm.common.web.action.BaseAction;
import com.apm.common.web.common.helper.LoginHelper;
import com.apm.common.web.common.helper.LoginInfo;
import com.apm.common.web.utils.PopulateList;
import com.apm.main.common.constants.Constants;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;


public class EmrAction extends BaseAction implements Preparable, ModelDriven<EmrForm>{
	
	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
	HttpServletResponse response = (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
	HttpSession session = request.getSession(true);
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	
	EmrForm emrForm = new EmrForm();
	
	int practitionerID = loginInfo.getId();
	
	
	
	public String execute() throws Exception {
		if(!verifyLogin(request)){
			return "login";
		}
		//set admin emr
		if(loginInfo.getUserType()==2){
			setAdminEmr();
		}

		
		Connection connection = null;
		//int practitionerID = loginInfo.getId();
		ArrayList<Emr>emrList = new ArrayList<Emr>();
		try{
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			int apmtId = emrForm.getAppointmentid();
			int conditionId = emrDAO.getConditionOfAmpt(apmtId);
			//String conditionName = emrDAO.getConditionName(conditionId);
			
			emrList = emrDAO.getEmrList(emrForm.getCommencing(),practitionerID);
			
			emrForm.setEmrList(emrList);
		
			emrForm.setSelectedid(emrForm.getAppointmentid());
			emrForm.setSelectedPatientId(emrForm.getId());
			
			//Client details
			clientProfileDataView(apmtId,emrForm.getSelectedPatientId());
			
			//set default list
			ArrayList<Emr>medicalHistoryList = emrDAO.getMedicalHistoryList(emrForm.getId(), practitionerID, loginInfo.getId());
			emrForm.setMedicalHistoryList(medicalHistoryList);
			
			//consultation Note
			
			ArrayList<Emr>consultationList = emrDAO.getConsultationList(emrForm.getId(), practitionerID,loginInfo.getId(),loginInfo.getFirstName(),loginInfo.getLastName(),conditionId);
			/*emrForm.setConsultationList(consultationList);*/
			session.setAttribute("consultationList", consultationList);
			
			//Documentation
			ArrayList<Emr>docList = emrDAO.getDocList(emrForm.getId(), practitionerID,loginInfo.getId(),conditionId,emrForm.getFilterdoctType());
			emrForm.setDocList(docList);
			
			//Social history
			ArrayList<Emr>socialHistoryList = emrDAO.getSocialHistoryList(emrForm.getId(),practitionerID, loginInfo.getId(),conditionId);				
			emrForm.setSocialHistoryList(socialHistoryList);
			
			//Reminder history
			ArrayList<Emr>reminderList = emrDAO.getReminderList(emrForm.getId(), practitionerID, loginInfo.getId());				
			emrForm.setReminderList(reminderList);
			
			//prescription history
			ArrayList<Emr>prescriptionList = emrDAO.getPrescriptionList(emrForm.getId(), practitionerID, loginInfo.getId(),conditionId);
			emrForm.setPrescriptionList(prescriptionList);
			
			//Allergy
			ArrayList<Emr>allergyList = emrDAO.getAllergyList(emrForm.getId(), practitionerID, loginInfo.getId());
			emrForm.setAllergyList(allergyList);			
				
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		return SUCCESS;
	}
	
	
	/*public String updateTemplate(){
		if(!verifyLogin(request)){
			return "login";
		}
		
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			int id = Integer.parseInt(request.getParameter("id"));
			String clientId = request.getParameter("clientName");
			String diaryUserId = request.getParameter("diaryUser");
			String conditionId = request.getParameter("condition");
			ListAssessmentDAO listAssessmentDAO = new JDBCListAssessmentFormDAO(connection);
			
			int result1 = listAssessmentDAO.updateAssessmentClientNameImage(id,clientId,diaryUserId,conditionId);
			
			ArrayList<Assessment> fieldNameList = (ArrayList<Assessment>) session.getAttribute("fieldNameList");
			for(Assessment assessment : fieldNameList){
				
				String lableName = assessment.getFiledname();
			
			
				String fieldValue = lableName.replace(" ", "_");
				String temp1 = fieldValue.replace("(", "_");
				String temp2 = temp1.replace(")", "_");
				String temp3 = temp2.replace("-", "_");
				String temp4 = temp3.replace("/", "_");
				String temp5 = temp4.replace("?", "_");
				String temp6 = temp5.replace(",", "_");
				String temp7 = temp6.replace("&", "_");
				String temp8 = temp7.replace("+", "_");
				String temp9 = temp8.replace(".", "_");
				String temp10 = temp9.replace("'", "_");
				
				
				String temp10 = DateTimeUtils.removeAllSpecialChar(lableName);
				temp10 = temp10.replace(" ", "");
				
				String textName = request.getParameter(lableName);
				
				String note = "";
				if(assessment.getType().equals("5")){
					note = request.getParameter("note_"+lableName);
					textName = textName + ":" + note;
				}
				
				if(assessment.getType().equals("6")){
					note = request.getParameter("note_"+lableName);
					note = request.getParameter("note_"+lableName);
					textName = textName + ":" + note;
				}
				
				if(emrForm.isRepeat()==false){
					int result = listAssessmentDAO.updateAssessmentFormClient(id,temp10,lableName,textName,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
				}else{
					AssessmentFormDAO assessmentFormDAO = new JDBCAssessmentFormDAO(connection);
					Template template = (Template)session.getAttribute("assmnttemplatedetails");
					String clientassesmentfieldid = (String)session.getAttribute("clientassesmentfieldid");
					ArrayList<Assessment>dataList = assessmentFormDAO.getRepeatFormData(template,assessment.getFiledname(),clientassesmentfieldid);
					int r=0;
					for(Assessment data : dataList){
						String strtxt = request.getParameter(assessment.getFiledname() + r);
						int updte = assessmentFormDAO.updateRepeatFormData(strtxt,data.getId(),temp10,clientId,diaryUserId,conditionId);
						r++;
					}
				}
				
			}
			
			String templateId = listAssessmentDAO.getTemplateId(id);
			
			session.setAttribute("clientId", clientId);
			session.setAttribute("diaryUserId", diaryUserId);
			session.setAttribute("conditionId", conditionId);
			session.setAttribute("templateId", templateId);
			
			
			
		//	setFormData(clientId, diaryUserId, conditionId);
			
			//emrForm.setMessage("Field Updated Sucessfully!!");
			addActionMessage("Field Updated Sucessfully!!");
			
			//assessmentForm.setClientName("");
			
		}catch(Exception e){
			
		}
		
		
		
		return getPatientRecord();
	}
	*/
	
	
	private void clientProfileDataView(int apmtId, int selectedPatientId) {

		Connection connection = null;
		String selectedPatientId1 = Integer.toString(selectedPatientId);
		Client client = new Client();
		
		try{
			
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			client = clientDAO.getClientDetails(selectedPatientId1);
			
			emrForm.setTitle(client.getTitle());
			emrForm.setFirstName(client.getFirstName());
			emrForm.setLastName(client.getLastName());
			emrForm.setAddress(client.getAddress());
			emrForm.setCountry(client.getCountry());
			emrForm.setDob(client.getDob());
			emrForm.setEmail(client.getEmail());
			emrForm.setGender(client.getGender());
			emrForm.setMobNo(client.getMobNo());
			emrForm.setPostCode(client.getPostCode());
			emrForm.setReference(client.getReference());
			//String sourceOfIntro = clientDAO.getSourceOfIntro(client.getSourceOfIntro());
			emrForm.setSourceOfIntro(client.getSourceOfIntro());
			emrForm.setTown(client.getTown());
			emrForm.setId(client.getId());
			/*emrForm.setType(client.getType());
			emrForm.setTypeName(client.getTypeName());
			emrForm.setExpiryDate(client.getExpiryDate());
			emrForm.setNote(client.getNote());*/
			//String occupation = clientDAO.getOccupation(client.getOccupation());
			emrForm.setOccupation(client.getOccupation());		
			//emrForm.setExpiryDate(client.getExpiryDate());
			emrForm.setWhopay(client.getWhopay());
			/*emrForm.setPolicyAuthorzCode(client.getPolicyAuthorzCode());
			emrForm.setPolicyNo(client.getPolicyNo());
			emrForm.setKnownAs(client.getKnownAs());*/
			emrForm.setCounty(client.getCounty());
			emrForm.setHomeNo(client.getHomeNo());
			emrForm.setWorkNo(client.getWorkNo());
			/*emrForm.setEmailCc(client.getEmailCc());
			emrForm.setPrefContactMode(client.getPrefContactMode());
			emrForm.setEmergencyContName(client.getEmergencyContName());
			emrForm.setEmergencyContNo(client.getEmergencyContNo());
			emrForm.setPatientType(client.getPatientType());		*/
			emrForm.setClientId(selectedPatientId1);
			emrForm.setClient(client.getTitle()+" " + client.getFirstName()+" " +client.getLastName());
			session.setAttribute("clientId",selectedPatientId1);
			//String declarationNotes = clientDAO.getDeclaration(loginInfo.getId());
			//session.setAttribute("declarationNotes", declarationNotes);
			//String declarationTitle = clientDAO.getTitleOfDeclaration(loginInfo.getId());
			//emrForm.setDeclarationTitle(declarationTitle);
		//	session.setAttribute("declarationTile",declarationTitle);
			String practtionername = emrDAO.getPractitionerName(selectedPatientId1,apmtId);
			emrForm.setPractitionerName(practtionername);
			//String clinicName = clientDAO.getClinicName(loginInfo.getId());
			//emrForm.setClinicName(clinicName);
			emrForm.setGpname(client.getGpname());
			emrForm.setEmployerName(client.getEmployerName());
			//String treatmentType = clientDAO.getTreatmentType(client.getTreatmentType());
			emrForm.setTreatmentType(client.getTreatmentType());
			//emrForm.setReferedDate(client.getReferedDate());
			String insuranceCo = clientDAO.getTPCompanyName(client.getTypeName());
			emrForm.setThirdPartyCompanyName(insuranceCo);
			//emrForm.setPolicyExcess(client.getPolicyExcess());
			
			
		}catch(Exception e){
			
		}
		
	}


	public String show(){
		if(!verifyLogin(request)){
			return "login";
		}
		//set admin emr
		if(loginInfo.getUserType()==2){
			setAdminEmr();
		}

		
		Connection connection = null;
		//int practitionerID = loginInfo.getId();
		ArrayList<Emr>emrList = new ArrayList<Emr>();
		try{
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
		
				String temp[] = emrForm.getCommencing().split("/");
				String date = temp[2] + "-" + temp[1] + "-" + temp[0];
				emrList = emrDAO.getEmrList(date,practitionerID);
			
				emrForm.setEmrList(emrList);
				
				//set first client record
				setFormData(emrList);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return SUCCESS;
	}
	
	
	
	
	public void setAdminEmr(){
		
		Connection connection = null;
		try{
			
			
			practitionerID = (Integer)session.getAttribute("selectedPractitioner");
			
			connection = Connection_provider.getconnection();
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			
			ArrayList<DiaryManagement>userList = notAvailableSlotDAO.getUserList(loginInfo.getId());
			
			emrForm.setUserList(userList);
			
			emrForm.setDiaryUser(Integer.toString(practitionerID));
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*private void setSelectedFormData(Emr emr) {	
			
			emrForm.setSelectedid(emr.getAppointmentid());
			emrForm.setSelectedPatientId(emr.getId());
			emrForm.setAge(emr.getAge());
			emrForm.setDob(emr.getDob());
			emrForm.setGender(emr.getGender());
			emrForm.setPatientName(emr.getPatientName());
			
			
			Connection connection = null;
			try{
				connection = Connection_provider.getconnection();
				EmrDAO emrDAO = new JDBCEmrDAO(connection);
				
				//medical history
				ArrayList<Emr>medicalHistoryList = emrDAO.getSelectedMedicalHistoryList(emr.getAppointmentid(),emr.getId(), practitionerID,loginInfo.getId());
				emrForm.setMedicalHistoryList(medicalHistoryList);
				
				//consultation Note
				ArrayList<Emr>consultationList = emrDAO.getSelectedConsultationList(emr.getAppointmentid(),emr.getId(), practitionerID,loginInfo.getId(),loginInfo.getFirstName(),loginInfo.getLastName());
				emrForm.setConsultationList(consultationList);
				
				//Documentation
				ArrayList<Emr>docList = emrDAO.getSelectedDocList(emr.getAppointmentid(),emr.getId(), practitionerID, loginInfo.getId());
				emrForm.setDocList(docList);
				
				//Social history
				ArrayList<Emr>socialHistoryList = emrDAO.getSelectedSocialHistoryList(emr.getAppointmentid(),emr.getId(),practitionerID, loginInfo.getId());				
				emrForm.setSocialHistoryList(socialHistoryList);
				
				//Reminder history
				ArrayList<Emr>reminderList = emrDAO.getSelectedReminderList(emr.getAppointmentid(),emr.getId(), practitionerID, loginInfo.getId());				
				emrForm.setReminderList(reminderList);
				
				//prescription history
				ArrayList<Emr>prescriptionList = emrDAO.getSelectedPrescriptionList(emr.getAppointmentid(),emr.getId(), practitionerID,loginInfo.getId());
				emrForm.setPrescriptionList(prescriptionList);
				
				//Allergy
				ArrayList<Emr>allergyList = emrDAO.getSelectedAllergyList(emr.getAppointmentid(),emr.getId(), practitionerID, loginInfo.getId());
				emrForm.setAllergyList(allergyList);
				
			}catch(Exception e){
				e.printStackTrace();
			}
		
	}*/
	
	public void setFormData(ArrayList<Emr>emrList){
		
		if(emrList.size()>0){
			Emr emr = (Emr)emrList.get(0);
			
			emrForm.setSelectedid(emr.getAppointmentid());
			emrForm.setSelectedPatientId(emr.getId());
			emrForm.setAge(emr.getAge());
			emrForm.setDob(emr.getDob());
			emrForm.setGender(emr.getGender());
			emrForm.setPatientName(emr.getPatientName());
			emrForm.setConditionName(emr.getConditionName());
			emrForm.setTreatmentEpisodeName(emr.getTreatmentEpisodeName());
			
			int apmtId = emr.getAppointmentid();
			Connection connection = null;
			try{
				connection = Connection_provider.getconnection();
				EmrDAO emrDAO = new JDBCEmrDAO(connection);
				
				//Client details
				clientProfileDataView(apmtId,emrForm.getSelectedPatientId());
				
				//medical history
				ArrayList<Emr>medicalHistoryList = emrDAO.getMedicalHistoryList(emr.getId(), practitionerID,loginInfo.getId());
				emrForm.setMedicalHistoryList(medicalHistoryList);
				
				int conditionId = emrDAO.getConditionOfAmpt(apmtId);
				//consultation Note
				ArrayList<Emr>consultationList = emrDAO.getConsultationList(emr.getId(), practitionerID,loginInfo.getId(),loginInfo.getFirstName(),loginInfo.getLastName(),conditionId);
				//emrForm.setConsultationList(consultationList);
				session.setAttribute("consultationList", consultationList);
				
				//Documentation
				ArrayList<Emr>docList = emrDAO.getDocList(emrForm.getId(), practitionerID,loginInfo.getId(),conditionId,emrForm.getFilterdoctType());
				emrForm.setDocList(docList);
				
				//Social history
				ArrayList<Emr>socialHistoryList = emrDAO.getSocialHistoryList(emr.getId(),practitionerID, loginInfo.getId(),conditionId);				
				emrForm.setSocialHistoryList(socialHistoryList);
				
				//Reminder history
				ArrayList<Emr>reminderList = emrDAO.getReminderList(emr.getId(), practitionerID, loginInfo.getId());				
				emrForm.setReminderList(reminderList);
				
				//prescription history
				ArrayList<Emr>prescriptionList = emrDAO.getPrescriptionList(emr.getId(), practitionerID,loginInfo.getId(),conditionId);
				emrForm.setPrescriptionList(prescriptionList);
				
				//Allergy
				ArrayList<Emr>allergyList = emrDAO.getAllergyList(emr.getId(), practitionerID, loginInfo.getId());
				emrForm.setAllergyList(allergyList);
				
				emrForm.setAction("emr");
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	public String view(){
		if(!verifyLogin(request)){
			return "login";
		}
	Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			int selectedid = Integer.parseInt(request.getParameter("selectedid"));
			
			
			
			String result = emrDAO.getEmrData(selectedid);
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+result+""); 
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	public void setAjaxData(int patientid,int pid){
		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			StringBuffer str = new StringBuffer();
			
			str.append("<thead>");
			str.append("<tr>");
			str.append("<th>#</th>");
			str.append("<th class='text-center'>Use Name</>");
			str.append("<th class='text-center' style='width: 50%;'>Description</>");
			str.append("<th class='text-center' style='width: 5%;'>View</th>");
			str.append("<th class='text-center' style='width: 5%;'>Edit</th>");
			str.append("<th class='text-center' style='width: 5%;'>Delete</th>");
			str.append("</tr>");
			str.append("</thead>");
			
			ArrayList<Emr>list = emrDAO.getMedicalHistoryList(patientid,pid,practitionerID);
			
			int i = 1;
			for(Emr emr2 : list){
				
				str.append("<tr>");
				str.append("<td>"+i+"</td>");
				str.append("<td>"+emr2.getPractitionerName()+"</td>");
				str.append("<td class='text-center' style='width: 50%;'>"+emr2.getDescription()+"</td>");
				str.append("<td class='text-center' style='width: 5%;'><a href='javascript:void(0)' onclick='viewMedicalHistory("+emr2.getId()+",\"viewmh\")'>View</a></td>"
						+ "<td class='text-center' style='width: 5%;'><a href='javascript:void(0)' onclick='editMedicalHistory("+emr2.getId()+",\"editmh\")'><i class='fa fa-edit'></i></a></td>"
						+ "<td class='text-center' style='width: 5%;'><a href='javascript:void(0)' onclick='deleteMedicalHistory("+emr2.getId()+")'><i class='fa fa-trash-o'></i></a></td>");
				
				str.append("</tr>");
				
				i++;
			}
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+str.toString()+""); 
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String delete(){
		if(!verifyLogin(request)){
			return "login";
		}
		
	Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			int selectedid = Integer.parseInt(request.getParameter("selectedid"));
			
			int patientid = Integer.parseInt(request.getParameter("selectedPatientId"));
			
			int pid = Integer.parseInt(request.getParameter("pid"));
			
			int result = emrDAO.deleteMedicalHistory(selectedid);
			
			setAjaxData(patientid,pid);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	
	public String editsave(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			int selectedid = Integer.parseInt(request.getParameter("selectedid"));
			String medicalhistory = request.getParameter("medicalhistory");
			int patientid = Integer.parseInt(request.getParameter("selectedPatientId"));
			int pid = Integer.parseInt(request.getParameter("pid"));
			
			int result = emrDAO.updateMedicalHistory(selectedid,medicalhistory);
			
			setAjaxData(patientid,pid);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String save(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			//var url = "saveEmr?selectedPatientId="+selectedPatientId+"&medicalhistory="+medicalhistory+"&apmtid="+apmtid+" ";
			int patientid = Integer.parseInt(request.getParameter("selectedPatientId"));
			//int practionerId = loginInfo.getId();
			int apmtid = Integer.parseInt(request.getParameter("apmtid"));
			String medicalhistory = request.getParameter("medicalhistory");
			int pid = Integer.parseInt(request.getParameter("pid"));
			
			Emr emr = new Emr();
			emr.setPatientId(patientid);
			emr.setPractitionerId(practitionerID);
			emr.setAppointmentid(apmtid);
			emr.setDescription(medicalhistory);
			
			int result = emrDAO.saveMedicalHistory(emr);
			
			setAjaxData(patientid,pid);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	// consultation note
	
	public String showConsList() throws SQLException{
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		int patientid = Integer.parseInt(request.getParameter("selectedPatientId"));
		int apmntid = Integer.parseInt(request.getParameter("apmtid"));
		
		try{
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			int conditionId = emrDAO.getConditionOfAmpt(apmntid);
			ArrayList<Emr>consultationList = emrDAO.getConsultationList(patientid, practitionerID,loginInfo.getId(),loginInfo.getFirstName(),loginInfo.getLastName(),conditionId);
			emrForm.setConsultationList(consultationList);
			
			emrForm.setConsultationList(consultationList);
			
			StringBuffer str = new StringBuffer();
			
			str.append("<table class='table table-hover table-condensed table-bordered table-striped ' > ");
			str.append("<thead>");
			str.append("<tr class='bg-info'>");
			str.append("<th>#</th>");
			str.append("<th class='text-center'>Use Name</>");
			str.append("<th class='text-center' >Date/Time</>");			
			str.append("<th class='text-center'>Note</>");
			str.append("<th class='text-center'>View</th>");
			str.append("<th class='text-center'>Edit</th>");
			str.append("<th class='text-center'>Delete</th>");
			
			str.append("</tr>");
			str.append("</thead>");
			
			str.append("<tbody>");
			int i = 1;
			for(Emr emr1:consultationList){
				str.append("<tr>");
				str.append("<td>"+i+"</td>");
				str.append("<td>"+emr1.getPractitionerName()+"</td>");
				str.append("<td>"+emr1.getLastModified()+"</td>");				
				str.append("<td>"+emr1.getDescription()+"</td>");
				str.append("<td><a href='javascript:void(0)' onclick='viewConsultation("+emr1.getId()+",\"viewcons\")'>View</a></td>"
						+ "<td><a href='javascript:void(0)' onclick='editConsultation("+emr1.getId()+",\"editcons\")'><i class='fa fa-edit'></i></a></td>"
						+ "<td><a href='javascript:void(0)' onclick='deleteConsultationNoteAjax("+emr1.getId()+")'><i class='fa fa-trash-o'></i></a></td>");
				
				str.append("</tr>");
				
				i++;
			}
			str.append("</tbody>");
			str.append("</table>");
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			
			response.getWriter().write(""+str.toString()+""); 
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			connection.close();
		}
		
		return null;
	}
	
	public String deletecons(){
		if(!verifyLogin(request)){
			return "login";
		}
		
		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			int selectedid = Integer.parseInt(request.getParameter("selectedid"));
			
			int patientid = Integer.parseInt(request.getParameter("selectedPatientId"));
			
			int pid = Integer.parseInt(request.getParameter("pid"));
			int apmtid = Integer.parseInt(request.getParameter("apmtid"));
			int result = emrDAO.deleteConsultation(selectedid);
			int conditionId = emrDAO.getConditionOfAmpt(apmtid);
			
			//setConsultationFormData(patientid,pid,apmtid);
			setConsFormData(patientid,pid,conditionId);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	public String editsavecons(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			int selectedid = Integer.parseInt(request.getParameter("selectedid"));
			String editconstext = request.getParameter("editconstext");
			int patientid = Integer.parseInt(request.getParameter("selectedPatientId"));
			int pid = Integer.parseInt(request.getParameter("pid"));
			int apmtid = Integer.parseInt(request.getParameter("apmtid"));
			int conditionId = emrDAO.getConditionOfAmpt(apmtid);
			
			int result = emrDAO.updateConsultationNote(selectedid,editconstext);
			
			//setConsultationFormData(patientid,pid,apmtid);
			setConsFormData(patientid,pid,conditionId);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public String savecons(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			//var url = "saveEmr?selectedPatientId="+selectedPatientId+"&medicalhistory="+medicalhistory+"&apmtid="+apmtid+" ";
			int patientid = Integer.parseInt(request.getParameter("selectedPatientId"));
			//int practionerId = loginInfo.getId();
			int apmtid = Integer.parseInt(request.getParameter("apmtid"));
			String consNoteText = request.getParameter("consNoteText");
			int pid = Integer.parseInt(request.getParameter("pid"));
			int conditionId = emrDAO.getConditionOfAmpt(apmtid);
			
			Emr emr = new Emr();
			emr.setPatientId(patientid);
			emr.setPractitionerId(practitionerID);
			emr.setAppointmentid(apmtid);
			emr.setDescription(consNoteText);
		//	String condition = emrDAO.getConditionOfAmpt(apmtid);
			int result = emrDAO.saveConsultationNote(emr);
			
			//setConsultationFormData(patientid,pid,apmtid);
			setConsFormData(patientid,pid,conditionId);
			
		}catch(Exception e){
			
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void setConsFormData( int patientid,int pid, int conditionId) {
		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			
			StringBuffer str = new StringBuffer();
			
			
			str.append("<table style='margin-left: 20px;'>");
			/*str.append("<thead>");
			str.append("<tr>");
			str.append("<th>History</th>");
			
			str.append("</tr>");
			str.append("</thead>");*/
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			ArrayList<Emr>consultationNoteList = emrDAO.getConsultationList(patientid,pid,loginInfo.getId(),loginInfo.getFirstName(),loginInfo.getLastName(),conditionId);
			
			for(Emr emr2 : consultationNoteList){
				
				str.append("<tr>");
				str.append("<td style='width: 20%;'><b>"+emr2.getLastModified()+"</b></td>");	
				str.append("<td style='width: 20%;'><b>"+emr2.getPractitionerName()+"</b></td>");				
				str.append("</tr>");
				
				str.append("<tr>");
				str.append("<td style='width: 20%'>Client given massage.</td>");	
				str.append("<td style='width: 100%'>"+emr2.getDescription()+"</td>");
				str.append("<td style='width: 20%'></td>");
				str.append("<td style='width: 5%'><a style = 'padding-right: 20px;' href='javascript:void(0)' onclick='editConsultation("+emr2.getId()+",\"editconsult\")'><i class='fa fa-edit'></i></td>");
				str.append("<td style='width: 5%'><a href='javascript:void(0)' onclick='deleteConsultationNoteAjax("+emr2.getId()+")'><i class='fa fa-trash-o'></i></a></td>");

				str.append("</tr>");
				str.append("<tr>");
				str.append("<td colspan = '5'><hr size='1' width='100%' style = 'margin-top: 10px;margin-bottom: 10px;border: 0;border-top: 1px solid #60CFD3;;'/></td>");
				str.append("</tr>");
				
			}
			
			str.append("</table>");
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+str.toString()+""); 
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	
	public String viewcons(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		
		try{ 
			
			String selectedid = request.getParameter("selectedid");
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			String result = emrDAO.getConsultationNoteText(selectedid);
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+result+""); 
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void setConsultationFormData(int patientid,int pid,int apmtid){
		
	Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			StringBuffer str = new StringBuffer();
			
			str.append("<thead>");
			str.append("<tr>");
			str.append("<th>#</th>");
			str.append("<th class='text-center' style='width: 20%;'>Use Name</>");
			str.append("<th class='text-center' style='width: 20%;'>Date/Time</>");			
			str.append("<th class='text-center' style='width: 50%;'>Note</>");
			str.append("<th class='text-center' style='width: 5%;'>View</th>");
			str.append("<th class='text-center' style='width: 5%;'>Edit</th>");
			str.append("<th class='text-center' style='width: 5%;'>Delete</th>");
			str.append("</tr>");
			str.append("</thead>");
			int conditionId = emrDAO.getConditionOfAmpt(apmtid);
			ArrayList<Emr>list = emrDAO.getConsultationList(patientid,pid,loginInfo.getId(),loginInfo.getFirstName(),loginInfo.getLastName(),conditionId);
			
			int i = 1;
			for(Emr emr2 : list){
				
				str.append("<tr>");
				str.append("<td>"+i+"</td>");
				str.append("<td class='text-center' style='width: 20%;'>"+emr2.getPractitionerName()+"</td>");
				str.append("<td class='text-center' style='width: 20%;'>"+emr2.getLastModified()+"</td>");				
				str.append("<td class='text-center' style='width: 50%;'>"+emr2.getDescription()+"</td>");
				str.append("<td class='text-center' style='width: 5%;'><a href='javascript:void(0)' onclick='viewConsultation("+emr2.getId()+",\"viewcons\")'>View</a></td>"
						+ "<td class='text-center' style='width: 5%;'><a href='javascript:void(0)' onclick='editConsultation("+emr2.getId()+",\"editcons\")'><i class='fa fa-edit'></i></a></td>"
						+ "<td class='text-center' style='width: 5%;'><a href='javascript:void(0)' onclick='deleteConsultationNoteAjax("+emr2.getId()+")'><i class='fa fa-trash-o'></i></a></td>");
				
				str.append("</tr>");
				
				i++;
			}
			
			
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+str.toString()+""); 
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	//Social History
	
	public String saveSocial(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			int patientid = Integer.parseInt(request.getParameter("selectedPatientId"));
			//int practionerId = loginInfo.getId();
			int apmtid = Integer.parseInt(request.getParameter("apmtid"));
			String socialhistory = request.getParameter("socialhistory");
			int pid = Integer.parseInt(request.getParameter("pid"));

			
			Emr emr = new Emr();
			emr.setPatientId(patientid);
			emr.setPractitionerId(practitionerID);
			emr.setAppointmentid(apmtid);
			emr.setDescription(socialhistory);
			
			int result = emrDAO.saveSocialHistory(emr);
			
			setSocialAjaxData(patientid,pid,apmtid);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	private void setSocialAjaxData(int patientid,int pid,int apmtid) {

		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			//practitionerID = (Integer) session.getAttribute("practitionerID");

			StringBuffer str = new StringBuffer();
			
			str.append("<thead>");
			str.append("<tr>");
			str.append("<th>#</th>");
			str.append("<th class='text-center' style='width: 20%;'>User Name</>");
			str.append("<th class='text-center' style='width: 50%;'>Description</>");
			str.append("<th class='text-center' style='width: 5%;'>View</th>");
			str.append("<th class='text-center' style='width: 5%;'>Edit</th>");
			str.append("<th class='text-center' style='width: 5%;'>Delete</th>");
			str.append("</tr>");
			str.append("</thead>");
			
			int conditionId = emrDAO.getConditionOfAmpt(apmtid);
			ArrayList<Emr>list = emrDAO.getSocialHistoryList(patientid,pid, loginInfo.getId(),conditionId);
			
			int i = 1;
			for(Emr emr2 : list){
				
				str.append("<tr>");
				str.append("<td>"+i+"</td>");
				str.append("<td class='text-center' style='width: 20%;'>"+emr2.getPractitionerName()+"</td>");
				str.append("<td class='text-center' style='width: 50%;'>"+emr2.getDescription()+"</td>");
				str.append("<td class='text-center' style='width: 5%;'><a href='javascript:void(0)' onclick='viewSocialHistory("+emr2.getId()+",\"viewsh\")'>View</a></td>"
						+ "<td class='text-center' style='width: 5%;'><a href='javascript:void(0)' onclick='editSocialHistory("+emr2.getId()+",\"editsh\")'><i class='fa fa-edit'></i></a></td>"
						+ "<td class='text-center' style='width: 5%;'><a href='javascript:void(0)' onclick='deleteSocialHistory("+emr2.getId()+")'><i class='fa fa-trash-o'></i></a></td>");
				
				str.append("</tr>");
				
				i++;
			}
			
			
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+str.toString()+""); 
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public String viewSocial(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
			
			try{ 
				
				connection = Connection_provider.getconnection();
				EmrDAO emrDAO = new JDBCEmrDAO(connection);
				
				int selectedid = Integer.parseInt(request.getParameter("selectedid"));
				
				
				
				String result = emrDAO.getSocialData(selectedid);
				
				response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write(""+result+""); 
				
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
		

	public String editsaveSocial(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			int selectedid = Integer.parseInt(request.getParameter("selectedid"));
			String socialhistory = request.getParameter("socialhistory");
			int patientid = Integer.parseInt(request.getParameter("selectedPatientId"));
			int pid = Integer.parseInt(request.getParameter("pid"));
			int apmtid = Integer.parseInt(request.getParameter("apmtid"));
			
			int result = emrDAO.updateSocialHistory(selectedid,socialhistory);
			
			setSocialAjaxData(patientid,pid,apmtid);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

	public String deleteSocial(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			int selectedid = Integer.parseInt(request.getParameter("selectedid"));
			
			int patientid = Integer.parseInt(request.getParameter("selectedPatientId"));
			
			int pid = Integer.parseInt(request.getParameter("pid"));
			int apmtid = Integer.parseInt(request.getParameter("apmtid"));
			
			int result = emrDAO.deleteSocialHistory(selectedid);
			
			setSocialAjaxData(patientid,pid,apmtid);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}		
		
		return null;		
	}

	// Reminder

	public String saveReminderDetails(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		
		try{			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			//practitionerID = (Integer) session.getAttribute("practitionerID");

			int patientId = Integer.parseInt(request.getParameter("selectedPatientId"));
			int apmtid = Integer.parseInt(request.getParameter("apmtid"));
			String reminder = request.getParameter("reminder"); 
			String alertdate = request.getParameter("alert");
			String hh = request.getParameter("hh");
			String min = request.getParameter("min");
			int pid = Integer.parseInt(request.getParameter("pid"));
			
			Emr emr = new Emr();
			
			emr.setPatientId(patientId);
			emr.setAppointmentid(apmtid);
			emr.setPractitionerId(practitionerID);
			emr.setDescription(reminder);
			emr.setAlertDate(alertdate);
			emr.setHour(hh);
			emr.setMin(min);
			
			int result = emrDAO.saveReminder(emr);
			
			setReminderAjaxData(patientId,pid);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	private void setReminderAjaxData(int patientId,int pid) {
		
		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			StringBuffer str = new StringBuffer();
			//practitionerID = (Integer) session.getAttribute("practitionerID");
			str.append("<thead>");
			str.append("<tr>");
			str.append("<th>#</th>");
			str.append("<th class='text-center' style='width: 20%;'>User Name</>");
			str.append("<th class='text-center' style='width: 50%;'>Note</>");			
			str.append("<th class='text-center' style='width: 20%;'>Date/Time</>");
			str.append("<th class='text-center' style='width: 20%;'>Alert Date</>");
			str.append("<th class='text-center' style='width: 5%;'>View</th>");
			str.append("<th class='text-center' style='width: 5%;'>Edit</th>");
			str.append("<th class='text-center' style='width: 5%;'>Delete</th>");
			str.append("</tr>");
			str.append("</thead>");
			
			ArrayList<Emr>list = emrDAO.getReminderList(patientId,pid, loginInfo.getId());
			
			int i = 1;
			for(Emr emr2 : list){
				
				str.append("<tr>");
				str.append("<td>"+i+"</td>");
				str.append("<td class='text-center' style='width: 20%;'>"+emr2.getPractitionerName()+"</td>");
				str.append("<td class='text-center' style='width: 50%;'>"+emr2.getDescription()+"</td>");
				str.append("<td class='text-center' style='width: 20%;'>"+emr2.getLastModified()+"</td>");
				str.append("<td class='text-center' style='width: 20%;'>"+emr2.getAlertDate()+"</td>");				
				str.append("<td class='text-center' style='width: 5%;'><a href='javascript:void(0)' onclick='viewReminder("+emr2.getId()+",\"viewrem\")'>View</a></td>"
						+ "<td class='text-center' style='width: 5%;'><a href='javascript:void(0)' onclick='editReminder("+emr2.getId()+",\"editrem\")'><i class='fa fa-edit'></i></a></td>"
						+ "<td class='text-center' style='width: 5%;'><a href='javascript:void(0)' onclick='deleteReminderAjax("+emr2.getId()+")'><i class='fa fa-trash-o'></i></a></td>");
				
				str.append("</tr>");
				
				i++;
			}
						
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+str.toString()+""); 
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	public String viewReminder(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		Emr emr = new Emr();
		
		try{ 
			
			String selectedid = request.getParameter("selectedid");
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			//String result = emrDAO.getReminderText(selectedid);
			emr = emrDAO.getReminderText(selectedid);
			String data = ""+emr.getAlertDate()+"//"+emr.getDescription()+"";
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+data+""); 
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String editsaveReminder(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			int selectedid = Integer.parseInt(request.getParameter("selectedid"));
			String reminder = request.getParameter("reminder");
			String editalert = request.getParameter("editalert");
			String edithh = request.getParameter("edithh");
			String editmin = request.getParameter("editmin");
			int pid = Integer.parseInt(request.getParameter("pid"));
			
			int patientid = Integer.parseInt(request.getParameter("selectedPatientId"));
			
			int result = emrDAO.updateReminder(selectedid,reminder,editalert,edithh,editmin);
			
			setReminderAjaxData(patientid,pid);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

	public String deleteReminder(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			int selectedid = Integer.parseInt(request.getParameter("selectedid"));
			
			int patientid = Integer.parseInt(request.getParameter("selectedPatientId"));
			
			int pid = Integer.parseInt(request.getParameter("pid"));
			
			int result = emrDAO.deleteReminder(selectedid);
			
			setReminderAjaxData(patientid,pid);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}		
		
		return null;		
	}
	
	//Prescription
	
	public String savePrescription(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		
		try{			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			//practitionerID = (Integer) session.getAttribute("practitionerID");

			int patientId = Integer.parseInt(request.getParameter("selectedPatientId"));
			int apmtid = Integer.parseInt(request.getParameter("apmtid"));
			String prescription = request.getParameter("prescription"); 
			int pid = Integer.parseInt(request.getParameter("pid"));
						
			Emr emr = new Emr();
			
			emr.setPatientId(patientId);
			emr.setAppointmentid(apmtid);
			emr.setPractitionerId(practitionerID);
			emr.setDescription(prescription);			
			
			int result = emrDAO.savePrescription(emr);
			
			setPrescriptionAjaxData(patientId,pid,apmtid);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	

	
	private void setPrescriptionAjaxData(int patientId,int pid,int apmtid) {

		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			StringBuffer str = new StringBuffer();
			//practitionerID = (Integer) session.getAttribute("practitionerID");
			str.append("<thead>");
			str.append("<tr>");
			str.append("<th>#</th>");
			str.append("<th class='text-center' style='width: 20%;'>User Name</>");		
			str.append("<th class='text-center' style='width: 50%;'>Treatment</>");			
			str.append("<th class='text-center' style='width: 20%;'>Date/Time</>");
			str.append("<th class='text-center' style='width: 5%;'>View</th>");
			str.append("<th class='text-center' style='width: 5%;'>Edit</th>");
			str.append("<th class='text-center' style='width: 5%;'>Delete</th>");
			str.append("</tr>");
			str.append("</thead>");
			
			int conditionId = emrDAO.getConditionOfAmpt(apmtid);
			ArrayList<Emr>list = emrDAO.getPrescriptionList(patientId,pid,loginInfo.getId(),conditionId);
			
			int i = 1;
			for(Emr emr2 : list){
				
				str.append("<tr>");
				str.append("<td>"+i+"</td>");
				str.append("<td class='text-center' style='width: 20%;'>"+emr2.getPractitionerName()+"</td>");
				str.append("<td class='text-center' style='width: 50%;'>"+emr2.getDescription()+"</td>");
				str.append("<td class='text-center' style='width: 20%;'>"+emr2.getLastModified()+"</td>");
				str.append("<td class='text-center' style='width: 5%;'><a href='javascript:void(0)' onclick='viewPrescription("+emr2.getId()+",\"viewpre\")'>View</a></td>"
						+ "<td class='text-center' style='width: 5%;'><a href='javascript:void(0)' onclick='editPrescription("+emr2.getId()+",\"editpre\")'><i class='fa fa-edit'></i></a></td>"
						+ "<td class='text-center' style='width: 5%;'><a href='javascript:void(0)' onclick='deletePrescriptionAjax("+emr2.getId()+")'><i class='fa fa-trash-o'></i></a></td>");
				str.append("</tr>");
				
				i++;
			}
						
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+str.toString()+""); 
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
	}
	
	public String viewPrescription(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		
		try{ 
			
			String selectedid = request.getParameter("selectedid");
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			String result = emrDAO.getPrescriptionText(selectedid);
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+result+""); 
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

	public String editsavePrescription(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			int selectedid = Integer.parseInt(request.getParameter("selectedid"));
			String prescription = request.getParameter("prescription");
			int patientid = Integer.parseInt(request.getParameter("selectedPatientId"));
			int pid = Integer.parseInt(request.getParameter("pid"));
			int apmtid = Integer.parseInt(request.getParameter("apmtid"));
			
			int result = emrDAO.updatePrescription(selectedid,prescription);
			
			setPrescriptionAjaxData(patientid,pid,apmtid);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String deletePrescription(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			int selectedid = Integer.parseInt(request.getParameter("selectedid"));
			
			int patientid = Integer.parseInt(request.getParameter("selectedPatientId"));
			
			int pid = Integer.parseInt(request.getParameter("pid"));
			int apmtid = Integer.parseInt(request.getParameter("apmtid"));
			
			int result = emrDAO.deletePrescription(selectedid);
			
			setPrescriptionAjaxData(patientid,pid,apmtid);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}		
		
		return null;		
	}

	// Allergy
	
	public String saveAllergy(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		
		try{
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			String allergy = request.getParameter("allergy"); 
			int apmtid = Integer.parseInt(request.getParameter("apmtid"));
			int patientId = Integer.parseInt(request.getParameter("selectedPatientId"));
			//int selectedID = Integer.parseInt(request.getParameter("selectedid"));
			int pid = Integer.parseInt(request.getParameter("pid"));
						
			Emr emr = new Emr();
			emr.setPatientId(patientId);
			emr.setAppointmentid(apmtid);
			emr.setPractitionerId(practitionerID);
			emr.setDescription(allergy);
			
			int result = emrDAO.saveAllergy(emr);
			
			setAllergyAjaxData(patientId,pid);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private void setAllergyAjaxData(int patientId,int pid) {

		Connection connection = null;
		
		try{			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			StringBuffer str = new StringBuffer();
			//practitionerID = (Integer) session.getAttribute("practitionerID");
			
			
			str.append("<thead>");
			str.append("<tr>");
			str.append("<th>#</th>");
			str.append("<th class='text-center' style='width: 20%;'>User Name</>");			
			str.append("<th class='text-center' style='width: 20%;'>Date/time</>");			
			str.append("<th class='text-center' style='width: 50%;'>Note</>");
			str.append("<th class='text-center' style='width: 5%;'>View</th>");
			str.append("<th class='text-center' style='width: 5%;'>Edit</th>");
			str.append("<th class='text-center' style='width: 5%;'>Delete</th>");
			str.append("</tr>");
			str.append("</thead>");
			
			ArrayList<Emr>list = emrDAO.getAllergyList(patientId,pid,loginInfo.getId());
			
			int i = 1;
			for(Emr emr2 : list){
				
				str.append("<tr>");
				str.append("<td>"+i+"</td>");
				str.append("<td class='text-center' style='width: 20%;'>"+emr2.getPractitionerName()+"</td>");
				str.append("<td class='text-center' style='width: 20%;'>"+emr2.getLastModified()+"</td>");
				str.append("<td class='text-center' style='width: 50%;'>"+emr2.getDescription()+"</td>");				
				str.append("<td class='text-center' style='width: 5%;'><a href='javascript:void(0)' onclick='viewAllergy("+emr2.getId()+",\"viewall\")'>View</a></td>"
						+ "<td class='text-center' style='width: 5%;'><a href='javascript:void(0)' onclick='editAllergy("+emr2.getId()+",\"editall\")'><i class='fa fa-edit'></i></a></td>"
						+ "<td class='text-center' style='width: 5%;'><a href='javascript:void(0)' onclick='deleteAllergyAjax("+emr2.getId()+")'><i class='fa fa-trash-o'></i></a></td>");
				str.append("</tr>");
				
				i++;
			}
						
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+str.toString()+""); 
			
		}catch(Exception e){
			e.printStackTrace();
		}	
		
	}
	
	public String  viewAllergy(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		
		try{ 
			
			String selectedid = request.getParameter("selectedid");
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			String result = emrDAO.getAllergyText(selectedid);
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+result+""); 
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public String editsaveAllergy(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			int selectedid = Integer.parseInt(request.getParameter("selectedid"));
			String allergy = request.getParameter("allergy");
			int patientid = Integer.parseInt(request.getParameter("selectedPatientId"));
			int pid = Integer.parseInt(request.getParameter("pid"));
			
			int result = emrDAO.updateAllergy(selectedid,allergy);
			
			setAllergyAjaxData(patientid,pid);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public String deleteAllergy(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			int selectedid = Integer.parseInt(request.getParameter("selectedid"));
			
			int patientid = Integer.parseInt(request.getParameter("selectedPatientId"));
			
			int pid = Integer.parseInt(request.getParameter("pid"));
			
			int result = emrDAO.deleteAllergy(selectedid);
			
			setAllergyAjaxData(patientid,pid);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}		
		
		return null;		
	}


	//Documentation
	
	
	public String uploadAjaxFiles(){
		if(!verifyLogin(request)){
			return "login";
		}
		 for (int i = 0; i < emrForm.getFileUpload().length; i++) {
	            File uploadedFile = emrForm.getFileUpload()[i];
	            String fileName = emrForm.getFileUploadFileName()[i];
	            String filePath = request.getRealPath("/liveData/document/");
	            File destFile = new File(filePath,fileName);
	            System.out.println("Server path:" + filePath);
	            session.setAttribute("fileName", fileName);
	            try {
	                FileUtils.copyFile(uploadedFile, destFile);
	                emrForm.setMsg("File Uploaded successfully !!");
	       		 addActionMessage("File Uploaded successfully !!");
	            } catch (IOException ex) {
	                System.out.println("Could not copy file " + fileName);
	                ex.printStackTrace();
	            }
	        }
		 
		 return null;
	}
	
	

	public String edituploadAjaxFiles(){
		if(!verifyLogin(request)){
			return "login";
		}
		 for (int i = 0; i < emrForm.getEditfUpload().length; i++) {
	            File uploadedFile = emrForm.getEditfUpload()[i];
	            String fileName = emrForm.getEditfUploadFileName()[i];
	            String filePath = request.getRealPath("/liveData/document/");
	            File destFile = new File(filePath,fileName);
	            System.out.println("Server path:" + filePath);
	            session.setAttribute("fileName", fileName);
	            try {
	                FileUtils.copyFile(uploadedFile, destFile);
	            } catch (IOException ex) {
	                System.out.println("Could not copy file " + fileName);
	                ex.printStackTrace();
	            }
	        }
		 return null;
	}
		      
		public String saveDoc(){
			if(!verifyLogin(request)){
				return "login";
			}
			Connection connection = null;
				try {
				connection = Connection_provider.getconnection();
				EmrDAO emrDAO = new JDBCEmrDAO(connection);
				
				int apmtid = Integer.parseInt(request.getParameter("apmtid"));
				int patientId = Integer.parseInt(request.getParameter("selectedPatientId"));
				String document = request.getParameter("doc");
				int pid = Integer.parseInt(request.getParameter("pid"));
				String fileName = (String) session.getAttribute("fileName");
			            
			            Emr emr = new Emr();
			    		emr.setPatientId(patientId);
			    		emr.setAppointmentid(apmtid);
			    		emr.setPractitionerId(practitionerID);
			    		emr.setFileName(fileName);
			    		emr.setDescription(document);
			            
			            int result = emrDAO.saveDocument(emr); 
			            
			            setDocumentAjaxData(patientId,pid,apmtid);
			        
		         } catch (SQLException e) {
					
					e.printStackTrace();
				}
			return null;
	}
	
	
	
	private void setDocumentAjaxData(int patientId, int pid,int apmtid) {

		Connection connection = null;
		
		try{			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			StringBuffer str = new StringBuffer();
			//practitionerID = (Integer) session.getAttribute("practitionerID");			
			
			str.append("<thead>");
			str.append("<tr>");
			str.append("<th>#</th>");
			str.append("<th>User Name</>");
			str.append("<th>File Name</>");
			str.append("<th>Date/time</>");				
			str.append("<th>Note</>");
			str.append("<th>Download</>");
			str.append("<th>View</th>");
			str.append("<th>Edit</th>");
			str.append("<th>Delete</th>");
			str.append("</tr>");
			str.append("</thead>");
			
			int conditionId = emrDAO.getConditionOfAmpt(apmtid);
			ArrayList<Emr>list = emrDAO.getDocList(emrForm.getId(), practitionerID,loginInfo.getId(),conditionId,emrForm.getFilterdoctType());
			
			int i = 1;
			for(Emr emr2 : list){
				
				str.append("<tr>");
				str.append("<td>"+i+"</td>");
				str.append("<td>"+emr2.getPractitionerName()+"</td>");
				str.append("<td>"+emr2.getFileName()+"</td>");
				str.append("<td>"+emr2.getLastModified()+"</td>");				
				str.append("<td>"+emr2.getDescription()+"</td>");	
				//str.append("<td><a href='#' onclick='downloadDocument("+emr2.getId()+","+emr2.getFileName()+")'>Download</a></td>");
				str.append("<td><a href='downloadDocEmr?filename="+emr2.getFileName()+"'>Download</a></td>");
				str.append("<td><a href='#' onclick='viewDocument("+emr2.getId()+",\"viewdoc\")'>View</a></td>"
						+ "<td><a href='#' onclick='editDocument("+emr2.getId()+",\"editdoc\")'>Edit</a></td>"
								+ "<td><a href='#' onclick='deleteDocumentAjax("+emr2.getId()+")'>Delete</a></td>");
				str.append("</tr>");
				
				i++;
			}
						
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+str.toString()+""); 
			
		}catch(Exception e){
			e.printStackTrace();
		}	
			
		}
	
	
	public String  viewDocument(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		Emr emr = new Emr();
		
		try{ 
			
			String selectedid = request.getParameter("selectedid");
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			//String result = emrDAO.getReminderText(selectedid);
			emr = emrDAO.getDocumentText(selectedid);
			String data = ""+emr.getFileName()+"/"+emr.getDescription()+"";
			//String filename = emr.getFileName();
			//String desc = emr.getDescription();
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+data+""); 
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

	
	
		public String editsaveDocument(){
			if(!verifyLogin(request)){
				return "login";
			}
		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			int selectedid = Integer.parseInt(request.getParameter("selectedid"));
			String document = request.getParameter("doc");
			int apmtid = Integer.parseInt(request.getParameter("apmtid"));
			//String fileName = (String) session.getAttribute("fileName");
			String fileName = request.getParameter("name");
			int pid = Integer.parseInt(request.getParameter("pid"));
			String nullFile = "null";
			if(fileName.equals(nullFile) || fileName == null){
				fileName = (String) session.getAttribute("fileName");
			}
			
			int patientid = Integer.parseInt(request.getParameter("selectedPatientId"));
			
			int result = emrDAO.updateDocument(selectedid,document,fileName);
			
			setDocumentAjaxData(patientid,pid,apmtid);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}

		
		
		public String deleteDocument(){
			if(!verifyLogin(request)){
				return "login";
			}
			Connection connection = null;
			
			try{ 
				
				connection = Connection_provider.getconnection();
				EmrDAO emrDAO = new JDBCEmrDAO(connection);
				
				int selectedid = Integer.parseInt(request.getParameter("selectedid"));
				
				int patientid = Integer.parseInt(request.getParameter("selectedPatientId"));
				
				int pid = Integer.parseInt(request.getParameter("pid"));
				int apmtid = Integer.parseInt(request.getParameter("apmtid"));
				
				int result = emrDAO.deleteDocument(selectedid);
				
				setDocumentAjaxData(patientid,pid,apmtid);
				
				
			}catch(Exception e){
				e.printStackTrace();
			}		
			
			return null;		
		}
		
		public String downloadDoc() throws IOException{
			if(!verifyLogin(request)){
				return "login";
			}
			String fileName = request.getParameter("filename");
			
			FileInputStream in = null;
			ServletOutputStream out = null;
			HttpServletRequest servletRequest=(HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
			HttpServletResponse response = (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
			
			String id = request.getParameter("id");
			Connection connection = null;
			try{
				connection = Connection_provider.getconnection();
				EmrDAO emrDAO = new JDBCEmrDAO(connection);
				int dicom = emrDAO.checkifdicom(id);
				if(dicom==1){
					 String sql = "select dicomimg,filename from apm_document where id = "+id+" ";
					  PreparedStatement preparedStatement = connection.prepareStatement(sql);
					  ResultSet rs = preparedStatement.executeQuery();
					  while(rs.next()){
						  InputStream ins = rs.getBinaryStream(1);
						  fileName = rs.getString(2);
						  
						  
						  
						 // File file = new File(request.getRealPath("/pacsdata/"+multipacsid+""));
							//if(!file.exists()){
								System.out.println("file not exist");
								OutputStream f = new FileOutputStream(new File(request.getRealPath("/liveData/document/"+fileName+"")));
								
								int c = 0;
								while ((c = ins.read()) > -1) {
									f.write(c);
								}
								f.close();
								ins.close();
							}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
			String filepath = request.getRealPath("/liveData/document/"+fileName);
			filepath= filepath.replace("HISLIVE", "HISDATA");
			filepath= filepath.replace("HISTEST", "HISDATA");
			filepath= filepath.replace("HISTEST1", "HISDATA");
			 String workingDirectory = System.getProperty("user.dir");
			 //String absoluteFilePath = servletRequest.getRealPath("/liveData/document/");
			
			 File file=new File(filepath);
			 if(file.exists()){
			//return an application file instead of html page
			//response.setContentType("application/octet-stream");
	        response.setHeader("Content-Disposition","attachment;filename="+fileName+"");
		
	        	 try {
					in = new FileInputStream(file);
					 out = response.getOutputStream();
		        	 
			        	byte[] outputByte = new byte[4096];
			        	//copy binary contect to output stream
			        	while(in.read(outputByte, 0, 4096) != -1)
			        	{
			        		out.write(outputByte, 0, 4096);
			        	}
			        	
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	        
			 }
	        	
	        	in.close();
	        	out.flush();
	        	out.close();
	        	
	        	//setDocumentAjaxData(patientid,pid);
			return null;
		}
		
		/*public String showSelectedUser(){
			if(loginInfo.getUserType()==2){
				setAdminEmr();
			}
			Connection connection = null;
			ArrayList<Emr>emrList = new ArrayList<Emr>();
			try{
				
				connection = Connection_provider.getconnection();
				EmrDAO emrDAO = new JDBCEmrDAO(connection);
				//String apmtid =(String) session.getAttribute("apmtid1");
				String apmtid = request.getParameter("appointmentid");
				int apmtid1 = Integer.parseInt(apmtid);
				emrList = emrDAO.getSelectedPatientEmrList(apmtid1,practitionerID);
				emrForm.setEmrList(emrList);
				setSelectedFormData(emrList);
				
			}catch(Exception e){
				e.printStackTrace();
			}	
				
			return SUCCESS;
		}
		*/
		public String checkReportField(){
			if(!verifyLogin(request)){
				return "login";
			}
			/*if(loginInfo.getUserType()==2){
				setAdminEmr();
			}*/
			String apmtid1 = request.getParameter("apmtid");
			int apmtid = Integer.parseInt(request.getParameter("apmtid"));
			System.out.println(apmtid);
			
			int clientid = Integer.parseInt(request.getParameter("clientid"));
			
			Connection connection = null;
			ArrayList<Emr>emrList = new ArrayList<Emr>();
			try{
				connection = Connection_provider.getconnection();
				EmrDAO emrDAO = new JDBCEmrDAO(connection);
				
				//emrList = emrDAO.getSelectedPatientEmrList(apmtid,practitionerID);
				//emrForm.setEmrList(emrList);
				/*Emr emr = emrDAO.getSelectedPatientEmr(apmtid,practitionerID);
				emrForm.setId(emr.getId());
				emrForm.setPatientName(emr.getPatientName());
				emrForm.setGender(emr.getGender());
				emrForm.setAppointmentid(emr.getAppointmentid());
				emrForm.setDob(emr.getDob());
				emrForm.setAge(emr.getAge());
				
				setSelectedFormData(emr);*/
				
				String reportFieldExist = emrDAO.getReportFieldExist(apmtid,clientid);
							
				response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write(reportFieldExist); 
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return null;
		}
		
		public String emailReport(){
			if(!verifyLogin(request)){
				return "login";
			}
			Connection connection = null;
			
			try{
				connection = Connection_provider.getconnection();
				EmailTemplateDAO emailTemplateDAO = new JDBCEmailTemplateDAO(connection);			
				//String id = request.getParameter("id");
				String to = request.getParameter("to");
				String cc = request.getParameter("cc");
				String subject = request.getParameter("subject");
				String notes = request.getParameter("notes");
				System.out.println(notes);
				//String filename = (String)session.getAttribute("pdfFileName");
				//String[] temp1 = filename.split("/");
				//String filename1 = temp1[1];
				//int invoiceid = (Integer)session.getAttribute("chargesInvoiceid");
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				String d1 = dateFormat.format(date);
				String[] temp = d1.split("\\s+");
				String date1 = temp[0];
				String time = temp[1];
				
				//int result = emailTemplateDAO.saveEmailLogDetails(to, cc, subject, notes, date1,time);
				//EmbeddedImageEmailUtil.sendMail(connection,loginInfo.getId(),to, cc, subject, notes,loginInfo);
				
			}catch(Exception e){
				e.printStackTrace();
			}
			return SUCCESS;
		}
		
		public String getEmailPractioner(){
			if(!verifyLogin(request)){
				return "login";
			}
			Connection connection = null;
			int pid = Integer.parseInt(request.getParameter("pid"));
			try{
				connection = Connection_provider.getconnection();
				EmrDAO emrDAO = new JDBCEmrDAO(connection);
				 
				String practionerEmail = emrDAO.getPractitionerEmail(pid);
				
				response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write(practionerEmail); 
				
			}catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}
		
		public String showDetailsByClient(){
			if(!verifyLogin(request)){
				return "login";
			}
						
			Connection connection = null;
			ArrayList<Emr>emrList = new ArrayList<Emr>();
			
		try {
			connection = Connection_provider.getconnection();
			
			String currentDate = DateTimeUtils.getDateinSimpleFormate(new Date());
			String temp[] = currentDate.split(" ");
			String temp1[] = temp[0].split("-");
			String date = temp1[0] + "/" + temp1[1] + "/" + temp1[2];
			emrForm.setCommencing(date);
			
			date = temp1[2] + "-" + temp1[1] + "-" + temp1[0];
			
				EmrDAO emrDAO = new JDBCEmrDAO(connection);
								
				String clientId = emrForm.getClientId();
				int apmtId = emrDAO.getLatestAppointmentId(clientId);
				int conditionId = emrDAO.getConditionOfAmpt(apmtId);
				int practitionerID = emrDAO.getPractitionerId(clientId, apmtId);				
				
				if(loginInfo.getUserType()==2){				
					
					NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
					
					ArrayList<DiaryManagement>userList = notAvailableSlotDAO.getUserList(loginInfo.getId());
					
					emrForm.setUserList(userList);
					
					if(Integer.toString(practitionerID).equals("")){
						session.setAttribute("selectedPractitioner", Integer.parseInt(emrForm.getDiaryUser()));
						
						practitionerID = Integer.parseInt(emrForm.getDiaryUser());
						session.setAttribute("practitionerID", practitionerID);
					}
				}				
				
				emrList = emrDAO.getEmrList(date,practitionerID);				
				emrForm.setEmrList(emrList);				
				
				emrForm.setSelectedid(apmtId);
				emrForm.setSelectedPatientId(Integer.parseInt(clientId));
				emrForm.setDiaryUser(Integer.toString(practitionerID));
				
				if(emrList.size()>0){
					Emr emr = (Emr)emrList.get(0);
					
					emrForm.setSelectedid(emr.getAppointmentid());
					emrForm.setSelectedPatientId(emr.getId());
					emrForm.setAge(emr.getAge());
					emrForm.setDob(emr.getDob());
					emrForm.setGender(emr.getGender());
					emrForm.setPatientName(emr.getPatientName());
					emrForm.setConditionName(emr.getConditionName());
					emrForm.setTreatmentEpisodeName(emr.getTreatmentEpisodeName());
				}
				
				//Client details
				int clientId1 = Integer.parseInt(clientId);
				clientProfileDataView(apmtId,clientId1);
				
				//set default list
				ArrayList<Emr>medicalHistoryList = emrDAO.getMedicalHistoryList(emrForm.getId(), practitionerID, loginInfo.getId());
				emrForm.setMedicalHistoryList(medicalHistoryList);
				
				//consultation Note
				
				ArrayList<Emr>consultationList = emrDAO.getConsultationList(emrForm.getId(), practitionerID,loginInfo.getId(),loginInfo.getFirstName(),loginInfo.getLastName(),conditionId);
				/*emrForm.setConsultationList(consultationList);*/
				session.setAttribute("consultationList", consultationList);
				
				//Documentation
				ArrayList<Emr>docList = emrDAO.getDocList(emrForm.getId(), practitionerID,loginInfo.getId(),conditionId,emrForm.getFilterdoctType());
				emrForm.setDocList(docList);
				
				//Social history
				ArrayList<Emr>socialHistoryList = emrDAO.getSocialHistoryList(emrForm.getId(),practitionerID, loginInfo.getId(),conditionId);				
				emrForm.setSocialHistoryList(socialHistoryList);
				
				//Reminder history
				ArrayList<Emr>reminderList = emrDAO.getReminderList(emrForm.getId(), practitionerID, loginInfo.getId());				
				emrForm.setReminderList(reminderList);
				
				//prescription history
				ArrayList<Emr>prescriptionList = emrDAO.getPrescriptionList(emrForm.getId(), practitionerID, loginInfo.getId(),conditionId);
				emrForm.setPrescriptionList(prescriptionList);
				
				//Allergy
				ArrayList<Emr>allergyList = emrDAO.getAllergyList(emrForm.getId(), practitionerID, loginInfo.getId());
				emrForm.setAllergyList(allergyList);			
					
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
			
			return SUCCESS;
		}

		
	public String input() throws Exception {
		if(!verifyLogin(request)){
			return "login";
		}
		
		Connection connection = null;
		//set admin emr
		if(loginInfo.getUserType()==2){
			//setAdminEmr();
			
			connection = Connection_provider.getconnection();
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			
			ArrayList<DiaryManagement>userList = notAvailableSlotDAO.getUserList(loginInfo.getId());
			
			emrForm.setUserList(userList);
			
			if(!emrForm.getDiaryUser().equals("")){
				session.setAttribute("selectedPractitioner", Integer.parseInt(emrForm.getDiaryUser()));
				
				practitionerID = Integer.parseInt(emrForm.getDiaryUser());
				session.setAttribute("practitionerID", practitionerID);
			}
		}
		
		//int practitionerID = loginInfo.getId();
		
		String currentDate = DateTimeUtils.getDateinSimpleFormate(new Date());
		String temp[] = currentDate.split(" ");
		String temp1[] = temp[0].split("-");
		String date = temp1[0] + "/" + temp1[1] + "/" + temp1[2];
		emrForm.setCommencing(date);
		
		date = temp1[2] + "-" + temp1[1] + "-" + temp1[0];
		
		
		try{
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			ArrayList<Emr>emrList = emrDAO.getEmrList(date,practitionerID);
			
			emrForm.setEmrList(emrList);
			
			//set first client record
			setFormData(emrList);
		
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		
		return INPUT;
		
	}
	
	
	public String redirect(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{
			
			connection = Connection_provider.getconnection();
			String practitionerID1 = request.getParameter("practitionerId");
			int practitionerID = Integer.parseInt(practitionerID1);
			String clientId = request.getParameter("clientId");
			int apmtId = Integer.parseInt(request.getParameter("appointId"));			
				
			ArrayList<Emr>emrList = new ArrayList<Emr>();
			
			if(loginInfo.getUserType()==2){
				//setAdminEmr();
			
				NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
				
				ArrayList<DiaryManagement>userList = notAvailableSlotDAO.getUserList(loginInfo.getId());
				
				emrForm.setUserList(userList);
				
				if(practitionerID1.equals("")){
					session.setAttribute("selectedPractitioner", Integer.parseInt(emrForm.getDiaryUser()));
					
					practitionerID = Integer.parseInt(emrForm.getDiaryUser());
					session.setAttribute("practitionerID", practitionerID);
				}			
				
			}
			
			String currentDate = DateTimeUtils.getDateinSimpleFormate(new Date());
			String temp[] = currentDate.split(" ");
			String temp1[] = temp[0].split("-");
			String date = temp1[0] + "/" + temp1[1] + "/" + temp1[2];
			emrForm.setCommencing(date);
			
			date = temp1[2] + "-" + temp1[1] + "-" + temp1[0];
		
				connection = Connection_provider.getconnection();
				EmrDAO emrDAO = new JDBCEmrDAO(connection);
				emrList = emrDAO.getEmrList(date,practitionerID);
				
				emrForm.setEmrList(emrList);				
			
				int conditionId = emrDAO.getConditionOfAmpt(apmtId);
				
				emrForm.setSelectedid(apmtId);
				emrForm.setSelectedPatientId(Integer.parseInt(clientId));
				emrForm.setDiaryUser(practitionerID1);
				
				if(emrList.size()>0){
					Emr emr = (Emr)emrList.get(0);
					
					emrForm.setSelectedid(emr.getAppointmentid());
					emrForm.setSelectedPatientId(emr.getId());
					emrForm.setAge(emr.getAge());
					emrForm.setDob(emr.getDob());
					emrForm.setGender(emr.getGender());
					emrForm.setPatientName(emr.getPatientName());
					emrForm.setConditionName(emr.getConditionName());
					emrForm.setTreatmentEpisodeName(emr.getTreatmentEpisodeName());
				}
				//Client details
				int clientId1 = Integer.parseInt(clientId);
				clientProfileDataView(apmtId,clientId1);
				
				
				
				//consultation Note
				
				ArrayList<Emr>consultationList = emrDAO.getConsultationList(emrForm.getId(), practitionerID,loginInfo.getId(),loginInfo.getFirstName(),loginInfo.getLastName(),conditionId);
				/*emrForm.setConsultationList(consultationList);*/
				session.setAttribute("consultationList", consultationList);
				
				//set default list
				ArrayList<Emr>medicalHistoryList = emrDAO.getMedicalHistoryList(emrForm.getId(), practitionerID, loginInfo.getId());
				emrForm.setMedicalHistoryList(medicalHistoryList);
				
				//Social history
				ArrayList<Emr>socialHistoryList = emrDAO.getSocialHistoryList(emrForm.getId(),practitionerID, loginInfo.getId(),conditionId);				
				emrForm.setSocialHistoryList(socialHistoryList);
				
				//prescription history
				ArrayList<Emr>prescriptionList = emrDAO.getPrescriptionList(emrForm.getId(), practitionerID, loginInfo.getId(),conditionId);
				emrForm.setPrescriptionList(prescriptionList);
				
				//Allergy
				ArrayList<Emr>allergyList = emrDAO.getAllergyList(emrForm.getId(), practitionerID, loginInfo.getId());
				emrForm.setAllergyList(allergyList);			
					
				//Documentation
				ArrayList<Emr>docList = emrDAO.getDocList(emrForm.getId(), practitionerID,loginInfo.getId(),conditionId,emrForm.getFilterdoctType());
				emrForm.setDocList(docList);
				
				//Reminder history
				ArrayList<Emr>reminderList = emrDAO.getReminderList(emrForm.getId(), practitionerID, loginInfo.getId());				
				emrForm.setReminderList(reminderList);
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			
			
			return SUCCESS;
			
		
	}
	
	public String viewPage(){
		
		emrForm.setAction("ad");
		String currentDate = DateTimeUtils.getDateinSimpleFormate(new Date());
		String temp[] = currentDate.split(" ");
	
		String temp1[] = temp[0].split("-");
		String date = temp1[0] + "/" + temp1[1] + "/" + temp1[2];
		emrForm.setCommencing(date);
		
		return "emrDetailsPage";
	}
	
	public String getClientDetails(){
		String id = request.getParameter("clientId");
		try{
		Connection connection = null;
		Client client = new Client();
		connection = Connection_provider.getconnection();
		ClientDAO clientDAO = new JDBCClientDAO(connection);
		client = clientDAO.getPatient(Integer.parseInt(id));
		StringBuffer str = new StringBuffer();
		//practitionerID = (Integer) session.getAttribute("practitionerID");			
		
		str.append("<span>");
		str.append("<b> Client Details : "+client.getTitle()+" "+client.getFirstName()+" "+client.getLastName()+"</b> " +client.getDob()+", "+client.getAddress()+"  "+client.getSecondLineaddress()+", "+client.getTown()+", "+client.getPostCode()+","+client.getMobNo()+"");
		str.append("</span>");
		
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+str+""); 
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String delassesment(){
		if(!verifyLogin(request)){
			return "login";
		}
		
		Connection connection = null;
		
		try{
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			String id = request.getParameter("selectedid");
			String diaryUser = request.getParameter("diaryUser");
			String clientname = request.getParameter("clientname");
			String condition = request.getParameter("condition");
			
			int del = emrDAO.deleteAssesmentForm(id);
			
			/*emrForm.setClientname(clientname);
			emrForm.setDiaryUser(diaryUser);
			emrForm.setCondition(condition);*/
			
			
			
			//setFormData(clientname,diaryUser,condition);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return getPatientRecord();
				
		
	}
	
	public String link(){
		
		String clientid = (String) session.getAttribute("sharedclientid");
		String diaryuser = (String) session.getAttribute("shareddiaryuser");
		String condition = (String) session.getAttribute("sharedcondition");
		
		emrForm.setClientname(clientid);
		emrForm.setDiaryUser(diaryuser);
		emrForm.setCondition(condition);
		
	
		System.out.println(clientid);
		
		return getPatientRecord();
	}
	
	
	public String pacsreport(){
		if(!verifyLogin(request)){
			return "login";
		}
		
		try{
			Connection connection = null;
			Client client = new Client();
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			
			String imgid = request.getParameter("imgid");
			String action = request.getParameter("action");
			if(imgid.equals("0")){
				imgid = (String)session.getAttribute("selectedinvstid");
			}
			System.out.println(imgid);
			
			//get clinic details
			Clinic clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
			
			ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
			//String address = accountsDAO.getLocationAddress(locationid,loginInfo.getId());
			
			emrForm.setClinicName(clinic.getClinicName());
			emrForm.setClinicOwner(clinic.getClinicOwner());
			emrForm.setOwner_qualification(clinic.getOwner_qualification());
			//accountsForm.setClinicaddress(address);
			emrForm.setLandLine(clinic.getLandLine());
			emrForm.setWebsiteUrl(clinic.getWebsiteUrl());
			emrForm.setClinicemail(clinic.getEmail());
			emrForm.setLocationAdressList(locationAdressList);
			
			
			PreparedStatement preparedStatement = null;
			ArrayList<Pacs>pacsimgList = new ArrayList<Pacs>();
			String sql = "SELECT id,imgname,finding  FROM his_pacs_data where invstid = "+imgid+"  order by id desc ";
			if(action.equals("m")){
				sql = "select his_pacs_data.id,imgname,his_pacs_data.finding from his_pacs_data inner join dicom_list " +
						" on his_pacs_data.imgid = dicom_list.id where multpacsid = "+imgid+" order by id desc ";
			}
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Pacs pacs = new Pacs();
				pacs.setId(rs.getInt(1));
				pacs.setFinding(rs.getString(3));
				String filepath = request.getRealPath("/liveData/pacsimg/"+rs.getInt(1)+".png");
				
				InputStream in = rs.getBinaryStream(2);
				OutputStream f = new FileOutputStream(new File(filepath));
				int c = 0;
				while ((c = in.read()) > -1) {
					f.write(c);
				}
				
				pacsimgList.add(pacs);
				f.close();
				in.close();
			}
			
			session.setAttribute("pacsimgList", pacsimgList);
			//emrForm.setPacsimgList(pacsimgList);
			
		}catch (Exception e) {
			e.printStackTrace();
			
		}
		
		return "pacsreport";
	}
	
	
	public String ehislink(){
		  
		  String clientid =(String)session.getAttribute("clientId"); 
		  String diaryuser =(String)session.getAttribute("diaryUserId");
		  String condition =(String)session.getAttribute("conditionId");
		  
		  emrForm.setClientname(clientid);
		  emrForm.setDiaryUser(diaryuser);
		  emrForm.setCondition(condition);
		   
		  System.out.println(clientid);
		  
		  return getPatientRecord();
		 }
	
	public String getPatientRecord(){
		
		if(!verifyLogin(request)){
			return "login";
		}
		
		if(!emrForm.getApmtId().equals("0")){
			session.removeAttribute("sessionadmissionid");
			
		}else{
			session.removeAttribute("sessionemrapmtid");
		}
		
		if(!emrForm.getApmtId().equals("")){
			session.setAttribute("sessionemrapmtid", emrForm.getApmtId());
		}
		String fromdate = emrForm.getFromDate();
		String todate = emrForm.getToDate();
		if(fromdate == null){
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();
			fromdate = dateFormat.format(cal.getTime());   
			fromdate = DateTimeUtils.getCommencingDate1(fromdate);
		}else{
			if(fromdate.equals("")) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			    Calendar cal = Calendar.getInstance();
			    fromdate = dateFormat.format(cal.getTime());   
			    fromdate = DateTimeUtils.getCommencingDate1(fromdate);
			} else {
			    fromdate = DateTimeUtils.getCommencingDate1(fromdate);   
			}  
		}
		if(todate== null){
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance(); 
			todate = dateFormat.format(cal.getTime());
			todate = DateTimeUtils.getCommencingDate1(todate);
		}else{
			if(todate.equals("")){
			    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			    Calendar cal = Calendar.getInstance(); 
			    todate = dateFormat.format(cal.getTime());
			    todate = DateTimeUtils.getCommencingDate1(todate);
			} else {
				todate = DateTimeUtils.getCommencingDate1(todate);
			}
		}
		
		
		if(emrForm.getCaldate().equals("")){
			String currentDate = DateTimeUtils.getDateinSimpleFormate(new Date());
			String temp[] = currentDate.split(" ");
		
			String temp1[] = temp[0].split("-");
			String date = temp1[0] + "/" + temp1[1] + "/" + temp1[2];
			emrForm.setCommencing(date);
		}else{
			emrForm.setCommencing(emrForm.getCaldate());
		}
		
		String action = request.getParameter("action");
		System.out.println(action);
		
		
		String clientId = emrForm.getClientname();
		String practionerId = emrForm.getDiaryUser();
		String condition = emrForm.getCondition();
		if(practionerId!=null){
			if(practionerId.equals("undefined")){
				practionerId=null;
			}
		}
		if(condition!=null){
			if(condition.equals("undefined")){
				condition=null;
			}
		}
		if(emrForm.getClientname().equals("")){
			/*Id = (String)session.getAttribute("clientId");
			practionerId = (String)session.getAttribute("diaryUserId");
			condition = (String)session.getAttribute("conditionId");*/
			if(clientId==null){
				clientId =  "";
			}
			emrForm.setApmtId("");
		}
		
		
		if(!emrForm.getConditionsApmt().equals("")){
			condition = emrForm.getConditionsApmt();
			emrForm.setConditionsApmt("");
		}
		
		
		
		try{
			Connection connection = null;
			Client client = new Client();
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
			AssessmentFormDAO assessmentFormDAO = new JDBCAssessmentFormDAO(connection);
			MasterDAO masterDAO = new JDBCMasterDAO(connection);
			
		
			int checkconfidential = emrDAO.checkConfidentialEmr(clientId,practionerId,condition);
			emrForm.setCheckconfidential(checkconfidential);
		/*	if(!clientId.equals("")){
				client = clientDAO.getPatient(Integer.parseInt(clientId));
				StringBuffer str = new StringBuffer();
				
				
				emrForm.setClient(client.getTitle()+" " + client.getFirstName()+" "+client.getLastName());
				emrForm.setClientData(client.getDob()+", "+client.getAddress()+"  "+client.getSecondLineaddress()+ " "+client.getTown()+ " "+client.getPostCode()+" , "+client.getMobNo());
				emrForm.setClientname(clientId);
			}*/
		if(practionerId==null&&clientId!=null){
			NotAvailableSlot notAvailableSlot = clientDAO.getLastAppointmentdetails(clientId);
			practionerId = Integer.toString(notAvailableSlot.getDiaryUserId());
			condition = notAvailableSlot.getCondition();
		}
			
			//set selected clientid from session
			if(session.getAttribute("sessionselectedclientid")!=null && clientId.equals("")){
				clientId = (String)session.getAttribute("sessionselectedclientid");
				
				NotAvailableSlot notAvailableSlot = clientDAO.getLastAppointmentdetails(clientId);
				practionerId = Integer.toString(notAvailableSlot.getDiaryUserId());
				condition = notAvailableSlot.getCondition();
				
				if(session.getAttribute("docpractionerid")!=null){
					practionerId = (String) session.getAttribute("docpractionerid");
					condition = (String) session.getAttribute("doccondition");
				}
				
				if(session.getAttribute("diaryUserId")!=null){
					practionerId = (String)session.getAttribute("diaryUserId");
					condition = (String)session.getAttribute("conditionId");
				}
				
				
				if(condition!=null){
				     
				     if(condition.equals("")){
				      condition=null; 
				     } 
				    }
				    
				    if(condition==null){
				     
				     IpdDAO ipdDAO=new JDBCIpdDAO(connection);
				     BedDao bedDao=new JDBCBedDao(connection); 
				     String ipdid=ipdDAO.getIpdId(clientId);
				     
				     Bed bed=bedDao.getEditIpdData(ipdid);
				     condition=bed.getConditionid(); 
				     practionerId=bed.getPractitionerid();
				    }
				    
				
				   
				Client client1 = clientDAO.getSelectedSessionClientDetails(clientId);
				emrForm.setClient(client1.getClientName());
				
				/*client = clientDAO.getPatient(Integer.parseInt(clientId));
				StringBuffer str = new StringBuffer();
				
				emrForm.setClient(client.getTitle()+" " + client.getFirstName()+" "+client.getLastName());
				emrForm.setClientData(client.getDob()+", "+client.getAddress()+"  "+client.getSecondLineaddress()+", "+client.getTown()+", "+client.getPostCode()+" , "+client.getMobNo());
				emrForm.setClientname(clientId);*/
			}
			
			emrForm.setDiaryUser(practionerId);
			//common condition
			UserProfile up=userProfileDAO.getUserprofileDetails(Integer.parseInt(practionerId));
			condition = up.getDiciplineName();
			
			if(emrForm.getApmtId().equals("")){
				String apmtid = emrDAO.getLastAppointmentid(clientId,practionerId,condition); 
				emrForm.setApmtId(apmtid);
			}
			
			String oldpractid = practionerId;
			//check if doctor placed with machine
			
			UserProfile userProfile = userProfileDAO.getUserprofileDetails(Integer.parseInt(practionerId));
			
			if(userProfile.getJobgroup().equals("4")){
				practionerId = userProfile.getDoctor();
				//common condition
				 up=userProfileDAO.getUserprofileDetails(Integer.parseInt(practionerId));
				condition = up.getDiciplineName();
			}
			
			client = clientDAO.getPatient(Integer.parseInt(clientId));
			
			//for assesment form list @ jitu
			ArrayList<Assessment> assesmenttemplateNameList= assessmentFormDAO.getTemplateList();
			emrForm.setAssesmenttemplateNameList(assesmenttemplateNameList);
			
			StringBuffer str = new StringBuffer();
			
			emrForm.setClient(client.getTitle()+" " + client.getFirstName()+" "+client.getLastName());
			emrForm.setClientData(client.getDob()+", "+client.getAddress()+"  "+client.getSecondLineaddress()+", "+client.getTown()+", "+client.getPostCode()+" , "+client.getMobNo());
			
			if(client.getMobNo()==null){
				client.setMobNo("");
			}
			
			if(client.getMobNo().equals("")){
				emrForm.setClientData(client.getDob()+", "+client.getAddress()+"  "+client.getSecondLineaddress()+", "+client.getTown()+", "+client.getPostCode()+" ");
			}
			emrForm.setClientname(clientId);
			
			ConsultationNoteDAO consultationNoteDAO = new JDBCConsultationNoteDAO(connection);
			ArrayList<Emr> consultationNoteList = consultationNoteDAO.getConsultationNoteListwithDate(practionerId,clientId,condition,fromdate,todate);
			emrForm.setConsultationList(consultationNoteList);
			session.setAttribute("consultationNoteList", consultationNoteList);
			String emrClientName = client.getTitle() + " " + client.getFirstName() + " " + client.getLastName();
			session.setAttribute("emrClientName", emrClientName);
			emrForm.setDiaryUser(practionerId);
			
			ArrayList<Client>clientList = consultationNoteDAO.getClientListEmr(Integer.parseInt(practionerId),clientId,oldpractid,emrForm.getApmtId());
			emrForm.setClientList(clientList);
		
			ArrayList<Client>conditionList = consultationNoteDAO.getConditionList(Integer.parseInt(clientId));
			emrForm.setConditionList(conditionList);
			emrForm.setCondition(up.getDiciplineName());
			

			ArrayList<TreatmentEpisode>	treatmentEpisodeList = new ArrayList<TreatmentEpisode>();
			treatmentEpisodeList = emrDAO.getTreatmentEpisodeList(clientId,practionerId,condition);
			emrForm.setTreatmentEpisodeList(treatmentEpisodeList);
			
			String treatmentEpisodeid = consultationNoteDAO.getTreatmentEpisodeid(emrForm.getApmtId());
			if(emrForm.getApmtId().equals("0")){
				String sessionadmissionid = (String)session.getAttribute("sessionadmissionid");
				BedDao bedDao=new JDBCBedDao(connection);
				Bed bed = bedDao.getEditIpdData(sessionadmissionid);
				treatmentEpisodeid = bed.getTreatmentepisodeid();
			}
			emrForm.setTreatmentEpisodeid(treatmentEpisodeid);
			
			/*
			for(TreatmentEpisode t : treatmentEpisodeList){
				for(NotAvailableSlot n : t.getAppointmnetList())
				{
					if(t.getTreatmentApmtCount() == 1 && n.getApmtCount() == 1){
						emrForm.setApmtId(Integer.toString(n.getId()));
					}
				}
			}*/
			
			//Documentation
			ArrayList<Emr>docList = emrDAO.getDocList(Integer.parseInt(clientId), Integer.parseInt(practionerId),loginInfo.getId(),Integer.parseInt(condition),emrForm.getFilterdoctType());
			emrForm.setDocList(docList);
			
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			
			ArrayList<Bed>addmissionList = new ArrayList<Bed>();
			ArrayList<Bed> dischargeList = new ArrayList<Bed>();
			ArrayList<Bed> gynicFormList=  new ArrayList<Bed>();
			ArrayList<Assessment>assessmentFormsList = new ArrayList<Assessment>();
			ArrayList<NotAvailableSlot> optionformlist = new ArrayList<NotAvailableSlot>();
			
			ArrayList<Client> declarationformlist = new ArrayList<Client>();
			//Akash 14 mar 2018 added ot notes list in docs
			ArrayList<NotAvailableSlot> otformlist = new ArrayList<NotAvailableSlot>();
			
			if(emrForm.getFilterdoctType().equals("0")){
				addmissionList = emrDAO.getIpdAdmissionList(clientId);
				dischargeList = emrDAO.getDischargeList(clientId);
				assessmentFormsList = emrDAO.getAssessmentList(clientId,practionerId,condition);
				optionformlist = notAvailableSlotDAO.getAllOptionFormList(clientId);
				gynicFormList =  emrDAO.getGynicFormList(clientId);
				declarationformlist = clientDAO.getDeclarationDataList(clientId);
				otformlist = notAvailableSlotDAO.getOTNotesFormList(clientId);
			}else{
				if(emrForm.getFilterdoctType().equals("Admission Form")){
					addmissionList = emrDAO.getIpdAdmissionList(clientId);
					dischargeList = new ArrayList<Bed>();
					assessmentFormsList = new ArrayList<Assessment>();
					optionformlist = new ArrayList<NotAvailableSlot>();
					declarationformlist = new ArrayList<Client>();
					otformlist= new ArrayList<NotAvailableSlot>();
				}
				if(emrForm.getFilterdoctType().equals("Discharge Form")){
					dischargeList = emrDAO.getDischargeList(clientId);
					addmissionList = new ArrayList<Bed>();
					assessmentFormsList = new ArrayList<Assessment>();
					optionformlist = new ArrayList<NotAvailableSlot>();
					declarationformlist = new ArrayList<Client>();
					otformlist= new ArrayList<NotAvailableSlot>();
				}
				if(emrForm.getFilterdoctType().equals("Assessment Report")){
					assessmentFormsList = emrDAO.getAssessmentList(clientId,practionerId,condition);
					dischargeList = new ArrayList<Bed>();
					addmissionList = new ArrayList<Bed>();
					optionformlist = new ArrayList<NotAvailableSlot>();
					declarationformlist = new ArrayList<Client>();
					otformlist= new ArrayList<NotAvailableSlot>();
				}
				if(emrForm.getFilterdoctType().equals("Optional Form")){
					assessmentFormsList = new ArrayList<Assessment>();
					dischargeList = new ArrayList<Bed>();
					addmissionList = new ArrayList<Bed>();
					optionformlist = notAvailableSlotDAO.getAllOptionFormList(clientId);
					declarationformlist = new ArrayList<Client>();
					otformlist= new ArrayList<NotAvailableSlot>();
				}
				if(emrForm.getFilterdoctType().equals("Declaration Form")){
					assessmentFormsList = new ArrayList<Assessment>();
					dischargeList = new ArrayList<Bed>();
					addmissionList = new ArrayList<Bed>();
					optionformlist = new ArrayList<NotAvailableSlot>();
					declarationformlist = clientDAO.getDeclarationDataList(clientId);
					otformlist= new ArrayList<NotAvailableSlot>();
					
				}
				if(emrForm.getFilterdoctType().equals("OT Form")){
					assessmentFormsList = new ArrayList<Assessment>();
					dischargeList = new ArrayList<Bed>();
					addmissionList = new ArrayList<Bed>();
					optionformlist = new ArrayList<NotAvailableSlot>();
					declarationformlist = new ArrayList<Client>();
					otformlist = notAvailableSlotDAO.getOTNotesFormList(clientId);
					
				}
				
				
			}
			
			emrForm.setAddmissionList(addmissionList);
			emrForm.setIpdsdischargeList(dischargeList);
			emrForm.setAssessmentFormsList(assessmentFormsList);
			emrForm.setOptionformlist(optionformlist);
			emrForm.setGynicFormList(gynicFormList);
			emrForm.setDeclarationformlist(declarationformlist);
			emrForm.setOtformlist(otformlist);
			//createPackImage();
			
			
			ArrayList<Emr>medicalRecordsTypeList = emrDAO.getMedicalRecordTypeList(Integer.parseInt(clientId), Integer.parseInt(practionerId), loginInfo.getId(),condition);
			emrForm.setMedicalRecordsTypeList(medicalRecordsTypeList);
			
			ArrayList<Assessment>imageDataList = emrDAO.getImageDataList(clientId,practionerId,condition);
			emrForm.setImageDataList(imageDataList);
			
			ArrayList<Emr>videoList = emrDAO.getVideoList(clientId,practionerId,condition);
			emrForm.setVideoList(videoList);
			
			//get repeat prescription list
			
			ArrayList<Priscription>parentPriscList = emrDAO.getParentPriscList(clientId,practionerId,condition);
			emrForm.setParentPriscList(parentPriscList);
			
			userProfile = userProfileDAO.getUserprofileDetails(Integer.parseInt(practionerId));
			emrForm.setEmrClientName(emrClientName);
			String emrDoctorName = userProfile.getInitial() + " " + userProfile.getFirstname() + " " + userProfile.getLastname();
			emrForm.setEmrDoctorName(emrDoctorName);
			String emrConditionName = emrDAO.getEmrConditionName(condition);
			emrForm.setEmrConditionName(emrConditionName);
			
			
			//set opd condiiton list
			if(session.getAttribute("sessionemrapmtid")!=null){
				String apmtid = (String)session.getAttribute("sessionemrapmtid");
				emrForm.setApmtId(apmtid);
				String opdcondstr = emrDAO.getopdconditionList(apmtid,emrForm.getClientname());
				emrForm.setOpdchkcondition(opdcondstr);
				emrForm.setOdconditionstr(opdcondstr);
			}
			
			//Akash 12 jan 2018
				if(practionerId==null) {
				   ArrayList<Master> otherTemplateList = masterDAO.getEmrTemplateList(null);
				   emrForm.setOtherTemplateList(otherTemplateList);
				}else {
					ArrayList<Master> otherTemplateList = masterDAO.getEmrTemplateList(userProfile.getDiciplineName());
					emrForm.setOtherTemplateList(otherTemplateList);
				}
			
				  emrForm.setFromDate( DateTimeUtils.getCommencingDate1(fromdate));
				  emrForm.setToDate( DateTimeUtils.getCommencingDate1(todate));
				//Akash 11 April 2018
				ArrayList<Priscription> medicinetimelist = emrDAO.getMedicineTimeList(); 
				emrForm.setMedicinetimelist(medicinetimelist);
				
			}catch(Exception e){
				e.printStackTrace();
			}
		 
		 session.setAttribute("sessionselectedclientid", clientId);
		
		return "emrDetailsPage";
	}
	
	public String getMedicalDropdown(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		
		try{ 
			String count = request.getParameter("count");
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			ArrayList<Emr>medicalRecordTypeList = emrDAO.getMedicalRecordTypeList();
			emrForm.setMedicalRecordTypeList(medicalRecordTypeList);
			
			 StringBuffer dropDownAjax = new StringBuffer();
				dropDownAjax.append("<select name = 'medicalHistory["+count+"].medicalRecordType' id = 'medicalHistory["+count+"].medicalRecordType' class='form-control showToolTip chosen' data-toggle='tooltip' onchange='setNew(this.value)'>");
					dropDownAjax.append("<option value = '0'>Select Type </option>");
					if(medicalRecordTypeList.size()!=0){
						for(Emr emr : medicalRecordTypeList){
							dropDownAjax.append("<option value = '"+emr.getMedicalRecordType()+"'>"+emr.getMedicalRecordType()+"</option>");
						}
						
					}
				dropDownAjax.append("</select>");
				
			     dropDownAjax.append("<input type='text'  class='form-control showToolTip medicalRecordTypeOther' name = 'medicalHistory["+count+"].medicalRecordTypeOther' id = 'medicalHistory["+count+"].medicalRecordTypeOther' placeholder='Enter Document Note' style = 'display:none' onchange='insertMedicalRecordType(this.value)'>");

				response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				
				response.getWriter().write(""+dropDownAjax.toString()+""); 
				
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public String addMedicalRecord(){
		
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			Emr emr = new Emr();
			
			String practionerId = emrForm.getDiaryUser();
			String patientid = emrForm.getClientname();
			String condition = emrForm.getCondition();
			
			
			emr.setPatientId(Integer.parseInt(patientid));
			emr.setPractitionerId(Integer.parseInt(practionerId));
			emr.setCondition_id(condition);
			
			

			
			for(MedicalHistory medicalHistory2 : emrForm.getMedicalHistory()){
				emr.setMedicalRecordType(medicalHistory2.getMedicalRecordType());
				emr.setMedicalHistoryNotes(medicalHistory2.getMedicalHistoryNotes());
				int result = emrDAO.saveMedicalHistoryRecords(emr,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
				
			}
			
			//setFormData(patientid,practionerId,condition);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return getPatientRecord();
		
	}
	
	public String editMedicalRecord(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			Emr emr = new Emr();
			
			String practionerId = emrForm.getDiaryUser();
			String patientid = emrForm.getClientname();
			String condition = emrForm.getCondition();
			String id = emrForm.getMedicalRecordId();
			
			emr.setPatientId(Integer.parseInt(patientid));
			emr.setPractitionerId(Integer.parseInt(practionerId));
			emr.setCondition_id(condition);
			
				emr.setMedicalRecordType(emrForm.getMedicalRecordType());
				emr.setMedicalHistoryNotes(emrForm.getMedicalHistoryNotes());
				int result = emrDAO.updateMedicalHistoryRecords(emr,id,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
				
			
			
			//setFormData(patientid,practionerId,condition);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return getPatientRecord();
	}
	public String deleteMedicalRecord(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			Emr emr = new Emr();
			
			String practionerId = emrForm.getDiaryUser();
			String patientid = emrForm.getClientname();
			String condition = emrForm.getCondition();
			String id = emrForm.getDeleteMedicalId();
			
			
				int result = emrDAO.deleteMedicalHistoryRecords(id);
				
			
			
			//setFormData(patientid,practionerId,condition);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return getPatientRecord();
	}
	public String saveNewMedicalRecordType(){
		String newtype = request.getParameter("newtype");
		String count = request.getParameter("c1");

		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			int save = 	emrDAO.saveNewMedicalRecordType(newtype);
			
			connection = Connection_provider.getconnection();
			ArrayList<Emr>medicalRecordTypeList = emrDAO.getMedicalRecordTypeList();
			emrForm.setMedicalRecordTypeList(medicalRecordTypeList);
			String lastMedicalRecordType = emrDAO.getLastMedicalRecordType();
			 StringBuffer dropDownAjax = new StringBuffer();
				dropDownAjax.append("<select name = 'medicalHistory["+count+"].medicalRecordType' id = 'medicalHistory["+count+"].medicalRecordType' class='form-control showToolTip chosen' data-toggle='tooltip' onchange='setNew(this.value)'>");
					dropDownAjax.append("<option value = '"+lastMedicalRecordType+"'>"+lastMedicalRecordType+" </option>");
					if(medicalRecordTypeList.size()!=0){
						for(Emr emr : medicalRecordTypeList){
							dropDownAjax.append("<option value = '"+emr.getMedicalRecordType()+"'>"+emr.getMedicalRecordType()+"</option>");
						}
						
					}
				dropDownAjax.append("</select>");
				
				response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				
				response.getWriter().write(""+dropDownAjax.toString()+""); 
				
				
		}
	catch(Exception e){
		e.printStackTrace();
	}
		return null;
	}
	
	public String set(){
		//var url = "setEmr?data="+data+"&type="+type+"";
		//disc,doctype
		String type = request.getParameter("type");
		String data = request.getParameter("data");
		/* session.setAttribute("doctType", emrForm.getDoctType());
         session.setAttribute("documentDesc", emrForm.getDocumentDesc());*/
		if(type.equals("doctype")){
			session.setAttribute("doctType",data);
		}
		if(type.equals("disc")){
			  session.setAttribute("documentDesc", data);
		}
		 session.setAttribute("isVideo", 0);
		return null;
	}
	
	public String uploadDocuments(){

		int isVideo = emrForm.getIsvideo();
		String clientid= emrForm.getClientname();

		
		for (int i = 0; i < emrForm.getFiles().length; i++) {
            File uploadedFile = emrForm.getFiles()[i];
            String fileName ="";
            if(clientid==null){
                fileName=loginInfo.getClinicUserid()+"_"+emrForm.getFilesFileName()[i];
            } else {
            	fileName=loginInfo.getClinicUserid() + "_"+clientid+"_"+emrForm.getFilesFileName()[i];
            }
            
            String filePath = "";
            
          //  int patientId = Integer.parseInt(request.getParameter("selectedPatientId"));
            
            if(isVideo == 1){
    			//videoClips
            	 filePath = request.getRealPath("/");
            	 
    		}
            else{
            	filePath = request.getRealPath("/liveData/document/");
            	filePath= filePath.replace("HISLIVE", "HISDATA");
            	filePath= filePath.replace("HISTEST", "HISDATA");
            	filePath= filePath.replace("HISTEST1", "HISDATA");
            	File uploadFolder = new File(filePath);
        		if (!uploadFolder.exists()) {
        			uploadFolder.mkdirs();
        		}
            	//filePath = "http://myapm.co.uk:8080/apmpdf/";
                 session.setAttribute("doctType", emrForm.getDoctType());
                 session.setAttribute("documentDesc", emrForm.getDocumentDesc());
            }
          //  File destFile = new File(filePath + File.separator + fileName);
           
           // String workingDirectory = System.getProperty("user.dir");
			//String absoluteFilePath = workingDirectory + File.separator + loginInfo.getClinicUserid() + File.separator +  fileName;
            
            System.out.println("Server path:" + filePath);
            session.setAttribute("fileName", fileName);
            session.setAttribute("isVideo", isVideo);
            try {
            	File fileToCreate = new File(filePath, fileName);
                FileUtils.copyFile(uploadedFile, fileToCreate);
                emrForm.setMsg("File Uploaded successfully !!");
       		 addActionMessage("File Uploaded successfully !!");
            } catch (IOException ex) {
                System.out.println("Could not copy file " + fileName);
                ex.printStackTrace();
            }
        }
		
		
		return "";
	}
	public String addDocuments(){
		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			Emr emr = new Emr();
			
			String practionerId = emrForm.getDiaryUser();
			String patientid = emrForm.getClientname();
			String condition = emrForm.getCondition();
			String editDoctId = emrForm.getEditDoctId(); 
			String ipdopdemr= request.getParameter("ipdopdemr");
			String fromDocDashboard= request.getParameter("fromdashboard");
			
			session.setAttribute("sessionselectedclientid", patientid);
			session.setAttribute("docpractionerid", practionerId);
			session.setAttribute("doccondition", condition);
			
			if(ipdopdemr==null){
				ipdopdemr="0";
			}
			emr.setPatientId(Integer.parseInt(patientid));
			emr.setPractitionerId(Integer.parseInt(practionerId));
			emr.setCondition_id(condition);
			String filename = (String)session.getAttribute("fileName");
			emr.setFileName(filename);
			emr.setIpdopdemr(ipdopdemr);
			
			int isVideo = (Integer)session.getAttribute("isVideo");
			if(isVideo == 1){
				
		        	   int result = emrDAO.saveVideoClip(emr,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone())); 
		         
				
			}else{
			emr.setDescription((String)session.getAttribute("documentDesc"));
            emr.setDoctType((String)session.getAttribute("doctType"));
			
	           
	           if(editDoctId == null || editDoctId.equalsIgnoreCase("")){
	        	   int result = emrDAO.savePatientDocument(emr,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),"0",loginInfo.getUserId()); 
	           }
				else{
					int result = emrDAO.updatePatientDocument(editDoctId,emr,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
				}
			} 
			//setFormData(patientid,practionerId,condition);
			
			if(fromDocDashboard!=null){
				return "viewDocs";
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "adddoc";
	}
	
	public String addConsultationNote(){
		Connection connection = null;
		
		try{ 
			
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			connection = Connection_provider.getconnection();
			connection=DriverManager.getConnection(""+Constants.DB_HOST+":3306/"+loginInfo.getClinicUserid()+"?useUnicode=true&characterEncoding=UTF-8","pranams","6qxi5x&)~XBZ");
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			Emr emr = new Emr();
			emr.setFinaldiagnosis(emrForm.getFinaldiagnosis());
			String practionerId = emrForm.getDiaryUser();
			String patientid = emrForm.getClientname();
			String condition = emrForm.getCondition();
			/*String consNote = request.getParameter("hindiconsnote");*/
			String str = new String (request.getParameter("consNote").getBytes ("iso-8859-1"), "UTF-8");
			System.out.println(str);
					
			
			session.setAttribute("sessionselectedclientid", patientid);
			session.setAttribute("docpractionerid", practionerId);
			session.setAttribute("doccondition", condition);
			
			emr.setPatientId(Integer.parseInt(patientid));
			emr.setPractitionerId(Integer.parseInt(practionerId));
			emr.setCondition_id(condition);
			if(!emrForm.getApmtId().equals("")){
				emr.setAppointmentid(Integer.parseInt(emrForm.getApmtId()));
			}else{
				emr.setAppointmentid(0);
			}
			
			emr.setConsNote(str);
			
	        int result = emrDAO.savePatientConsultationNote(emr,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
	        
	        //update treatment episode discharge status
	        int status = 0;
	        String dischargedate = DateTimeUtils.getCommencingDate1(emrForm.getDischargedate()) + " " + emrForm.getHour() + ":" + emrForm.getMinute() + ":20" ;
	        
	        if(emrForm.isChkDischarge()){
	        	status = 1;
	        }
	        ConsultationNoteDAO consultationNoteDAO = new JDBCConsultationNoteDAO(connection);
	        String treatmentEpisodeid = consultationNoteDAO.getTreatmentEpisodeid(emrForm.getApmtId());
	       String ipdid="";
	       String type="2";
	        if(emrForm.getApmtId().equals("0")){
	        	BedDao bedDao=new JDBCBedDao(connection);
	        	String sessionadmissionid = (String)session.getAttribute("sessionadmissionid");
				Bed bed = bedDao.getEditIpdData(sessionadmissionid);
				ipdid=sessionadmissionid;
				type="1";
				treatmentEpisodeid = bed.getTreatmentepisodeid();
				
				if(status==1){
				 int res = bedDao.updateBedStatus(sessionadmissionid);
				 int log = bedDao.saveDischargeLog(sessionadmissionid,patientid,dischargedate,bed.getBedid(),loginInfo);
				}
	        	//treatmentEpisodeid = consultationNoteDAO.getIpdTreatmentEpisodeid(emrForm.getClientname());
	        }
	        
	        
	        if(status==1){
	        	/*int upd = emrDAO.updateTreatmentEpisodeSischargeStatus(emrForm.getDischrgeOutcomes(),
	        			emrForm.getDischargeStatus(),status,treatmentEpisodeid,dischargedate,
	        			"","");*/
	        	
	        	AllTemplateAction allTemplateAction = new AllTemplateAction();
	        	allTemplateAction.sendDischargeEmail(patientid,condition,practionerId,emrForm.getApmtId(),loginInfo,connection,treatmentEpisodeid);
	        }else{
	        	/*emrDAO.updateTreatmentEpisodeSischargeStatus(emrForm.getDischrgeOutcomes(),
	        			emrForm.getDischargeStatus(),status,treatmentEpisodeid,"","","");*/
	        }
			
	        //save opd multicondition
	        if(!emrForm.getApmtId().equals("0")){
	        	NotAvailableSlotDAO notAvailableSlotDAO =  new JDBCNotAvailableSlotDAO(connection);	
	        	String arr[] = emrForm.getOdconditionstr().split(",");
	        	String lastmodified = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
	        	String tempdate[] = lastmodified.split(" ");
	        	//delete condition
	        	int del = emrDAO.deleteAppointmentCondition(emrForm.getApmtId());
	        	for(int i=0;i<arr.length;i++){
	        		if(!arr[i].equals("0")){
	        			int save = notAvailableSlotDAO.addOpdConditionReport(Integer.parseInt(DateTimeUtils.numberCheck(emrForm.getApmtId())), emrForm.getClientname(), arr[i], tempdate[0]);
	        		}
	        		
	        	}
	        	
	        }
	        
	        if(str!=null){
				Pattern p = Pattern.compile("(Follow Up Date:</b> \\w{1,2}/\\w{1,2}/\\w{4}</p>)"); 
				Matcher m = p.matcher(str);
				if (m.find()){
					String theGroup = m.group(1);
					/*System.out.println(theGroup);*/
					Pattern p1 = Pattern.compile("(\\w{1,2}/\\w{1,2}/\\w{4})"); 
					Matcher m1 = p1.matcher(theGroup);
					if (m1.find()){
						String tempdatefollow = m1.group(1);
						
						String monthchnage=tempdatefollow.split("/")[1];
						if(monthchnage.length()==1){
							monthchnage="0"+monthchnage;
						}
						
						String datechnge=tempdatefollow.split("/")[0];
						if(datechnge.length()==1){
							datechnge="0"+datechnge;
						}
						
						String newfollwupdate=datechnge+"-"+monthchnage+"-"+tempdatefollow.split("/")[2];
						ClientDAO clientDAO= new JDBCClientDAO(connection);
						Client client= new Client();
						client.setClientId(patientid);
						client.setOpdid(emrForm.getApmtId());
						client.setIpdid(ipdid);
						client.setType(type);
						client.setPractid(practionerId);
						client.setLocation("EMR");
						client.setFollowupdate(DateTimeUtils.getCommencingDate1(newfollwupdate));
						DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					     Calendar cal = Calendar.getInstance();
					     String toDaysDate = dateFormat.format(cal.getTime());
					     client.setDate(toDaysDate);
					     
						int followup=clientDAO.savefollowup(client);
						boolean flag=false;
						if(followup>0&&emrForm.getIsbookapmtfollowup()==1){
							newfollwupdate=DateTimeUtils.getCommencingDate1(newfollwupdate);
							AccountsDAO accountsDAO= new JDBCAccountsDAO(connection);
							String whopay=clientDAO.getWhoPayName(patientid);
							AppointmentType appointmentType= accountsDAO.getFollowUpIdCharge(whopay);
							if(appointmentType.getCharges()!=null){
								NotAvailableSlotDAO notAvailableSlotDAO =  new JDBCNotAvailableSlotDAO(connection);	
								NotAvailableSlot n =notAvailableSlotDAO.getMveDiaryUserDetails(""+practionerId,newfollwupdate);
								
								
								String stime = n.getSTime();
								int slotid = n.getId();
								String duration = n.getDuration();
								boolean chkapmtexsist = notAvailableSlotDAO.chkmveapmtaxsist(""+practionerId,newfollwupdate);
								if(chkapmtexsist){
									stime = notAvailableSlotDAO.getmveapmtendtime(""+practionerId,newfollwupdate);
								}
								SimpleDateFormat df = new SimpleDateFormat("HH:mm");
								 Date d = df.parse(stime); 
								 Calendar cal1 = Calendar.getInstance();
								 cal1.setTime(d);
								 cal1.add(Calendar.MINUTE, 5);
								 String endtime = df.format(cal1.getTime());
								 
								 Client client2= clientDAO.getClientDetails(patientid);
								 
								 UserProfileDAO userProfileDAO= new JDBCUserProfileDAO(connection);
								 UserProfile user= new UserProfile();
									user=userProfileDAO.getUserprofileDetails(DateTimeUtils.convertToInteger(practionerId));
								 NotAvailableSlot notAvailableSlot= new NotAvailableSlot();
								 notAvailableSlot.setApmtSlotId(slotid);
								 notAvailableSlot.setCommencing(newfollwupdate);
								 notAvailableSlot.setSTime(stime);
								 notAvailableSlot.setEndTime(endtime);
								 notAvailableSlot.setDiaryUserId((DateTimeUtils.convertToInteger(practionerId)));
								 notAvailableSlot.setDiaryUser(user.getFullname());
								 notAvailableSlot.setLocation("1");
								 notAvailableSlot.setDept(user.getDiciplineName());
								 notAvailableSlot.setClient(client2.getFullname());
								 notAvailableSlot.setClientId(patientid);
								 notAvailableSlot.setApmtDuration("00:05");
								 notAvailableSlot.setApmtType(""+appointmentType.getId());
								 notAvailableSlot.setRoom("Room1");
								 notAvailableSlot.setPayBy("Client");
								 notAvailableSlot.setAddedBy(""+loginInfo.getUserId());
								 notAvailableSlot.setCondition(user.getDiciplineName());
								 notAvailableSlot.setVaccineApmt(true);
								 notAvailableSlot.setNotes("");
								 notAvailableSlot.setTreatmentEpisodeId("0");
								 notAvailableSlot.setUsedsession("0");
								 notAvailableSlot.setOtid(0);
								 notAvailableSlot.setOtplan(""+0);
								 notAvailableSlot.setProcedure(""+0);
								 notAvailableSlot.setSurgeon(""+0);
								 notAvailableSlot.setAnesthesia(""+0);
								 notAvailableSlot.setIpdno(""+0);
								 notAvailableSlot.setWardid(""+0);
								 notAvailableSlot.setAssistaffcharge(""+0);
								 notAvailableSlot.setSic(""+0);
								 notAvailableSlot.setPsurcharge(""+0);
								 notAvailableSlot.setPanetcharge(""+0);
									
								 
								 
								int x= notAvailableSlotDAO.saveAppointment(notAvailableSlot);
								 
								String message="Your Follow up is Scheduled on "+DateTimeUtils.getCommencingDate1(client.getDate())+" by "+user.getFullname()+"";
								
								SmsService s= new SmsService();
								/*s.sendSms(message, client2.getMobNo(), loginInfo, new EmailLetterLog());*/
							}
						}
					}
				}
				
			  }
			//setFormData(patientid,practionerId,condition);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return "adddoc";
	}
	
	public String dicom(){
		Connection connection = null;
		try{ 
			String id = request.getParameter("id");
			String action = request.getParameter("action");
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			if(!action.equals("undefined")){
				int result = emrDAO.saveRemoteDicomId(id,loginInfo.getUserId(),action);
			}
			
			/*int multipacsid = emrDAO.getMultiPacsid(id);
			if(multipacsid>0){
				int updall = emrDAO.resetAllcgh(loginInfo.getUserId());
				int upd = emrDAO.updatePacsViewer(id,loginInfo.getUserId());
			}*/
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+loginInfo.getClinicUserid()+"");
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
		
		
	}
	
	
	
	
	public String editConsultationNote(){
		Connection connection = null;
		
		try{ 
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			
			
			connection = Connection_provider.getconnection();
			connection=DriverManager.getConnection(""+Constants.DB_HOST+":3306/"+loginInfo.getClinicUserid()+"?useUnicode=true&characterEncoding=UTF-8","pranams","6qxi5x&)~XBZ");
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			Emr emr = new Emr();
			
			String practionerId = emrForm.getDiaryUser();
			String patientid = emrForm.getClientname();
			String condition = emrForm.getCondition();
			
			int consulatation_note_id = emrForm.getConsulatation_note_id();
			emr.setPatientId(Integer.parseInt(patientid));
			emr.setPractitionerId(Integer.parseInt(practionerId));
			emr.setCondition_id(condition);
			String str = new String (request.getParameter("consNote").getBytes ("iso-8859-1"), "UTF-8");
			emr.setConsNote(str);
			
			if(!emrForm.getApmtId().equals("")){
				emr.setAppointmentid(Integer.parseInt(emrForm.getApmtId()));
			}else{
				emr.setAppointmentid(0);
			}
			
	        int result = emrDAO.updatePatientConsultationNote(emr,consulatation_note_id,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone())); 
			
	        //update treatment episode discharge status
	        int status = 0;
	        String dischargedate = DateTimeUtils.getCommencingDate1(emrForm.getDischargedate()) + " " + emrForm.getHour() + ":" + emrForm.getMinute() + ":20" ;
	        if(emrForm.isChkDischarge()){
	        	status = 1;
	        }
	        ConsultationNoteDAO consultationNoteDAO = new JDBCConsultationNoteDAO(connection);
	        String treatmentEpisodeid = consultationNoteDAO.getTreatmentEpisodeid(emrForm.getApmtId());
	       
	        if(emrForm.getApmtId().equals("0")){
	        	/*treatmentEpisodeid = consultationNoteDAO.getIpdTreatmentEpisodeid(emrForm.getClientname());*/
	        	BedDao bedDao=new JDBCBedDao(connection);
	        	String sessionadmissionid = (String)session.getAttribute("sessionadmissionid");
				Bed bed = bedDao.getEditIpdData(sessionadmissionid);
				treatmentEpisodeid = bed.getTreatmentepisodeid();
				
				if(status==1){
					int res = bedDao.updateBedStatus(sessionadmissionid);
					int log = bedDao.saveDischargeLog(sessionadmissionid,patientid,dischargedate,bed.getBedid(),loginInfo);
				}
				
	        }
	        
	        
	        if(status==1){
	        	
	        	/*int upd = emrDAO.updateTreatmentEpisodeSischargeStatus(emrForm.getDischrgeOutcomes(),
	        			emrForm.getDischargeStatus(),status,treatmentEpisodeid,dischargedate,"","");*/
	        	
	        	AllTemplateAction allTemplateAction = new AllTemplateAction();
	        	allTemplateAction.sendDischargeEmail(patientid,condition,practionerId,emrForm.getApmtId(),loginInfo,connection,treatmentEpisodeid);
	        }else{
	        	/*emrDAO.updateTreatmentEpisodeSischargeStatus(emrForm.getDischrgeOutcomes(),emrForm.getDischargeStatus(),
	        			status,treatmentEpisodeid,"","","");*/
	        }
	        
	       
	        //save opd multicondition
	        if(!emrForm.getApmtId().equals("0")){
	        	NotAvailableSlotDAO notAvailableSlotDAO =  new JDBCNotAvailableSlotDAO(connection);	
	        	String arr[] = emrForm.getOdconditionstr().split(",");
	        	String lastmodified = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
	        	String tempdate[] = lastmodified.split(" ");
	        	//delete condition
	        	int del = emrDAO.deleteAppointmentCondition(emrForm.getApmtId());
	        	for(int i=0;i<arr.length;i++){
	        		if(!arr[i].equals("0")){
	        			int save = notAvailableSlotDAO.addOpdConditionReport(Integer.parseInt(emrForm.getApmtId()), emrForm.getClientname(), arr[i], tempdate[0]);
	        		}
	        		
	        	}
	        	
	        }
	            
			//setFormData(patientid,practionerId,condition);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return getPatientRecord();
	}
	
	
	public String deleteConsultationNote(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{
			
			connection = Connection_provider.getconnection();
			int id = emrForm.getConsulatation_note_id();
			String practionerId = emrForm.getDiaryUser();
			String patientid = emrForm.getClientname();
			String condition = emrForm.getCondition();
			
			
			ConsultationNoteDAO consultationNoteDAO = new JDBCConsultationNoteDAO(connection);
			int result = consultationNoteDAO.deleteNote(Integer.toString(id));
			//setFormData(patientid,practionerId,condition);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return getPatientRecord();
	}
	
	
public String deloteq(){
	
	Connection connection = null;
	try{
		
		String clientId = request.getParameter("clientId");
		String prectionerid = request.getParameter("prectionerid");
		String conditionid = request.getParameter("conditionid");
		String templatename = request.getParameter("templatename");
		
		String selectedid = request.getParameter("selectedid");
		
		ArrayList<Priscription>oteqlist = (ArrayList<Priscription>)session.getAttribute("oteqlist");
		Priscription p = (Priscription)oteqlist.get(Integer.parseInt(selectedid));
		oteqlist.remove(Integer.parseInt(selectedid));
		
		ArrayList<String>deloteqList = new ArrayList<String>();
		if(session.getAttribute("deloteqList")!=null){
			deloteqList = (ArrayList<String>) session.getAttribute("deloteqList");
		}
		
		deloteqList.add(p.getMdicinenameid());
		session.setAttribute("deloteqList", deloteqList);
		
		if(templatename.equals("0")){
			getOTEquipmentData();
		}else{
			getoteqTemplateData();
		}
		
		
	/*	connection = Connection_provider.getconnection();
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		
		
		int del = emrDAO.deletePriscriptionData(selectedid);*/
				
				//getPriscriptionData(clientId, prectionerid, conditionid, "", "");
		
	}catch(Exception e){
		e.printStackTrace();
	}
	
	
	return null;
}

//priscription code start
	
public String delprisc(){
	
	Connection connection = null;
	try{
		
		String clientId = request.getParameter("clientId");
		String prectionerid = request.getParameter("prectionerid");
		String conditionid = request.getParameter("conditionid");
		String templatename = request.getParameter("templatename");
		
		String selectedid = request.getParameter("selectedid");
		
		ArrayList<Priscription>priscList = (ArrayList<Priscription>)session.getAttribute("priscList");
		Priscription p =new Priscription();
		if(priscList!=null){
			p= (Priscription)priscList.get(Integer.parseInt(selectedid));
			priscList.remove(Integer.parseInt(selectedid));
		}
		
		ArrayList<String>delPriscList = new ArrayList<String>();
		if(session.getAttribute("delPriscList")!=null){
			delPriscList = (ArrayList<String>) session.getAttribute("delPriscList");
		}
		
		delPriscList.add(p.getMdicinenameid());
		session.setAttribute("delPriscList", delPriscList);
		
		if(templatename.equals("0")){
			if(loginInfo.getOutoprisc()==1){
				getNimaiPriscriptionTemplateData();
			}else{
				getPriscriptionData();
			}
		}else{
			getPriscriptionTemplateData();
		}
		
		
	/*	connection = Connection_provider.getconnection();
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		
		
		int del = emrDAO.deletePriscriptionData(selectedid);*/
				
				//getPriscriptionData(clientId, prectionerid, conditionid, "", "");
		
	}catch(Exception e){
		e.printStackTrace();
	}
	
	return null;
}


public String updatemdcine(){
	
	//var url="updatemdcineEmr?medicinename="+medicinename+"&ghdnmdnameid="+ghdnmdnameid+"&gemdindex="+gemdindex+" ";
	Connection connection = null;
	try{
		String templatename = request.getParameter("templatename");
		String medicinename = request.getParameter("medicinename");
		String ghdnmdnameid = request.getParameter("ghdnmdnameid");
		String gemdindex = request.getParameter("gemdindex");
		
		connection = Connection_provider.getconnection();
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		
		ArrayList<Priscription>priscList = (ArrayList<Priscription>)session.getAttribute("priscList");
		Priscription p =new Priscription();
		if(priscList!=null){
			p= (Priscription)priscList.get(Integer.parseInt(gemdindex));
			p.setMdicinenametxt(medicinename);
			priscList.set(Integer.parseInt(gemdindex), p);
			session.setAttribute("priscList", priscList);
			
			//update medcine name
			int upd = emrDAO.updateMedcinename(ghdnmdnameid,medicinename);
			
			if(templatename.equals("0")){
				getPriscriptionData();
			}else{
				getPriscriptionTemplateData();
			}
		}
		
	}catch (Exception e) {
		// TODO: handle exception
	}
	
	return null;
	
	
}

public String editsaveprisc(){
	String categoryid = request.getParameter("categoryid");
	String mdicinenameid = request.getParameter("mdicinenameid");
	String mdicinenametxt = request.getParameter("mdicinenametxt");
	String priscdose = request.getParameter("priscdose");
	String priscfreq = request.getParameter("priscfreq");
	String priscdays = request.getParameter("priscdays");
	
	String clientId = request.getParameter("clientId");
	String prectionerid = request.getParameter("prectionerid");
	String conditionid = request.getParameter("conditionid");
	
	
	Connection connection = null;
	try{
		
		
		connection = Connection_provider.getconnection();
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		
		Priscription priscription = new Priscription();
		priscription.setCategoryid(categoryid);
		priscription.setMdicinenameid(mdicinenameid);
		priscription.setMdicinenametxt(mdicinenametxt);
		priscription.setPriscdose(priscdose);
		priscription.setPriscfreq(priscfreq);
		priscription.setPriscdays(priscdays);
		priscription.setClientId(clientId);
		priscription.setPrectionerid(prectionerid);
		priscription.setConditionid(conditionid);
		
		String selectedid = request.getParameter("selectedid");
		
		int result  = emrDAO.editsavePriscription(priscription,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),selectedid);
		
		//getPriscriptionData(clientId, prectionerid, conditionid, categoryid, mdicinenameid);
		
	}catch(Exception e){
		e.printStackTrace();
	}
	
	return  null;
}

public String deleteparentprisc(){
	
	String selectedid = request.getParameter("selectedid");
	Connection connection = null;
	try{
		
		
		connection = Connection_provider.getconnection();
		PrescriptionDAO prescriptionDAO = new JDBCPrescriptionDAO(connection);
		//Akash 19-10-2019 Commented because its delete full data. isntead ofit set flag
		//int del = emrDAO.deleteParentPrisc(selectedid);
		//int dl = emrDAO.deleteParet(selectedid);
		
		String delete_reason= request.getParameter("delete_reason");
		String userid = loginInfo.getUserId();
		String date = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
		int del=prescriptionDAO.cancelPrescription(selectedid,"From Emr Deleted",userid,date);
		
		viewallclientprisc();
		
	}catch(Exception e){
		e.printStackTrace();
	}
	
	return null;
}

public void getoteqTemplateData(){
	String selectedid = request.getParameter("selectedid");
	Connection connection = null;
	StringBuffer str = new StringBuffer();
	try{
		
		
		connection = Connection_provider.getconnection();
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		
		
		
		ArrayList<Priscription>selectedPriscList =(ArrayList<Priscription>)session.getAttribute("oteqlist");
		session.setAttribute("oteqlist", selectedPriscList);
		
		int i = 0;
		//str.append("<input type='hidden' name='parenteditdataid' id='eotparenteditdataid' value='"+parentdata+"'>");
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		//cal.add(Calendar.DATE, -7); 
		String curdate = dateFormat.format(cal.getTime());
		
		ArrayList<Master>dosageList = emrDAO.getDosageList();
		
		
		str.append("<input type='hidden' name='listsizeid' id='eotlistsizeid' value='"+selectedPriscList.size()+"'>");
		for(Priscription priscription : selectedPriscList){
		
			
			str.append("<tr>");
			/*str.append("<td>"+priscription.getDate()+"</td>");*/
			
			/*str.append("<td>"+priscription.getPrisctype()+"</td>");*/
			str.append("<td id='eotmdcinenametxt"+i+"'>"+priscription.getMdicinenametxt()+"</td>");
			
			str.append("<td><input type='number' class='form-control' id='eotdays"+i+"' name='days"+i+"' value='"+priscription.getPriscdays()+"' ></td>");
			
			str.append("<td><input type='text' name='eotdosage"+i+"' id='eotdosage"+i+"' value='"+priscription.getDosenotes()+"'></td>");
			
			
			
			
			/*str.append("<td>"+priscription.getPrisctotal()+"</td>");*/
			
			/*str.append("<td><i onclick='showedit("+i+")' class='fa fa-edit' ></i></td>");*/
			str.append("<td><i onclick='deleteeqdata("+i+")' class='fa fa-trash-o' ></i></td>");
		/*	String temp[] = priscription.getLastmodified().split(" ");
			
			if(temp[0].equals(curdate)){
				str.append("<td><i onclick='deletepriscdata("+i+")' class='fa fa-trash-o' ></i></td>");
			}else{
				str.append("<td><i  class='fa fa-trash-o' ></i></td>");
			}*/
			
			str.append("</tr>");
			
			i++;
		}
		
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+str.toString()+"");
		
	}catch(Exception e){
		e.printStackTrace();
	}
	
	
}
	


public String tenplateoteq(){
	
	String selectedid = request.getParameter("selectedid");
	Connection connection = null;
	try{
		
		
		connection = Connection_provider.getconnection();
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		
		Priscription pr = emrDAO.getTemplateoteqParentData(Integer.parseInt(selectedid));
		
		StringBuffer str = new StringBuffer();
		StringBuffer strdoage = new StringBuffer();
		StringBuffer strdays = new StringBuffer();
		
		String parentdata = pr.getPrepay() + "~" + pr.getPostpay() + "~" + pr.getOtherpay() + "~" + pr.getPriscdosenotes() + "~"
				+ pr.getFollowupsqty() + "~" + pr.getFollowupstype() + "~" + pr.getEnglish() + "~" + pr.getRegional() + "~" 
				+ pr.getHindi() + "~" + pr.getPriscadvoice();
		
		
		
		ArrayList<Priscription>selectedPriscList = emrDAO.getSelectedTemplateoteqList(selectedid);
		session.setAttribute("oteqlist", selectedPriscList);
		
		int i = 0;
		str.append("<input type='hidden' name='parenteditdataid' id='eotparenteditdataid' value='"+parentdata+"'>");
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		//cal.add(Calendar.DATE, -7); 
		String curdate = dateFormat.format(cal.getTime());
		
		ArrayList<Master>dosageList = emrDAO.getDosageList();
		
		
		str.append("<input type='hidden' name='listsizeid' id='eotlistsizeid' value='"+selectedPriscList.size()+"'>");
		for(Priscription priscription : selectedPriscList){
			strdoage = new StringBuffer();
			strdays = new StringBuffer();
			
			
			str.append("<tr>");
			/*str.append("<td>"+priscription.getDate()+"</td>");*/
			
			/*str.append("<td>"+priscription.getPrisctype()+"</td>");*/
			str.append("<td id='eotmdcinenametxt"+i+"'>"+priscription.getMdicinenametxt()+"</td>");
			
			str.append("<td><input type='number' class='form-control' id='eotdays"+i+"' name='days"+i+"' value='"+priscription.getPriscdays()+"' ></td>");
			
			str.append("<td><input type='text' name='eotdosage"+i+"' id='eotdosage"+i+"' value='"+priscription.getDosenotes()+"'></td>");
			
			
			
			
			/*str.append("<td>"+priscription.getPrisctotal()+"</td>");*/
			
			/*str.append("<td><i onclick='showedit("+i+")' class='fa fa-edit' ></i></td>");*/
			str.append("<td><i onclick='deleteeqdata("+i+")' class='fa fa-trash-o' ></i></td>");
		/*	String temp[] = priscription.getLastmodified().split(" ");
			
			if(temp[0].equals(curdate)){
				str.append("<td><i onclick='deletepriscdata("+i+")' class='fa fa-trash-o' ></i></td>");
			}else{
				str.append("<td><i  class='fa fa-trash-o' ></i></td>");
			}*/
			
			str.append("</tr>");
			
			i++;
		}
		
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+str.toString()+"");
		
	}catch(Exception e){
		e.printStackTrace();
	}
	
	return null;
}


public String tenplateprisc(){
	
	String selectedid = request.getParameter("selectedid");
	Connection connection = null;
	try{
		
		
		connection = Connection_provider.getconnection();
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		
		Priscription pr = emrDAO.getTemplatePriscriptionParentData(Integer.parseInt(selectedid));
		
		StringBuffer str = new StringBuffer();
		StringBuffer strdoage = new StringBuffer();
		StringBuffer strdays = new StringBuffer();
		
		String parentdata = pr.getPrepay() + "~" + pr.getPostpay() + "~" + pr.getOtherpay() + "~" + pr.getPriscdosenotes() + "~"
				+ pr.getFollowupsqty() + "~" + pr.getFollowupstype() + "~" + pr.getEnglish() + "~" + pr.getRegional() + "~" 
				+ pr.getHindi() + "~" + pr.getPriscadvoice();
		
		
		
		ArrayList<Priscription>selectedPriscList = emrDAO.getSelectedTemplatePriscList(selectedid);
		session.setAttribute("priscList", selectedPriscList);
		
		int i = 0;
		str.append("<input type='hidden' name='parenteditdataid' id='parenteditdataid' value='"+parentdata+"'>");
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		//cal.add(Calendar.DATE, -7); 
		String curdate = dateFormat.format(cal.getTime());
		
		ArrayList<Master>dosageList = emrDAO.getDosageList();
		
		
		str.append("<input type='hidden' name='listsizeid' id='listsizeid' value='"+selectedPriscList.size()+"'>");
		for(Priscription priscription : selectedPriscList){
			strdoage = new StringBuffer();
			strdays = new StringBuffer();
			
			
			str.append("<tr>");
			/*str.append("<td>"+priscription.getDate()+"</td>");*/
			str.append("<input type='hidden' id='dddose"+i+"' value='"+priscription.getDddose()+"'  class='form-control'>");
			str.append("<td><input type='text' id='srno"+i+"' onchange='changepriscgivensrno("+i+",this.value)' value='"+priscription.getSrno()+"' class='form-control'></td>");
			str.append("<input type='hidden' class='akash' value='" + priscription.getMdicinenameid() + "' class='form-control' />");
			str.append("<input type='hidden' class='akashclasss' value='" + i + "' class='form-control' />");
			str.append("<input type='hidden' id='uom"+i+"' value='"+priscription.getUom()+"'  class='form-control'>");
			/*str.append("<td>"+priscription.getPrisctype()+"</td>");*/
			str.append("<td style='cursor:pointer' onclick='showprisceditmdcinenameindb("+i+","+priscription.getMdicinenameid()+")' id='mdcinenametxt"+i+"'>"+priscription.getMdicinenametxt()+"</td>");
			str.append("<td class='hidden' id='priscfreq"+i+"'>"+priscription.getPriscfreq()+"</td>");
			str.append("<td class='hidden' id='mdicinenameid"+i+"'>"+priscription.getMdicinenameid()+"</td>");
			
			if(priscription.getUom()==null){
				priscription.setUom("");
			}
			if(loginInfo.getOutoprisc()==1){
				//str.append("<td><input type='number' id='dddose"+i+"' value='"+priscription.getDddose()+"'  class='form-control'>"+priscription.getUom()+"</td>");
				str.append("<td>"+priscription.getDddose()+" "+priscription.getUom()+"</td>");
			}else{
				if(priscription.getUnit()==null){
					str.append("<td>");
				}else{
					String unit = DateTimeUtils.isNull(priscription.getUnit());
					str.append("<td>"+unit+"");
				}
				if(priscription.getUnitextension()!=null){
					if(!priscription.getUnitextension().equals("0")){
						str.append(""+priscription.getUnitextension()+"");
					}
				}
				str.append("</td>");
			}
			
			/*str.append("<td>"+priscription.getPriscdose()+"</td>");*/
			strdoage.append("<select class='form-control' onchange='changepriscgivendose("+i+",this.value)' name='dosage"+i+"' id='dosage"+i+"' style='height:18px !important;'>");
			strdoage.append("<option value='"+priscription.getPriscdose()+"'>"+priscription.getPriscdose()+"</option>");
				for(Master master : dosageList){
					strdoage.append("<option value='"+master.getName()+"'>"+master.getName()+"</option>");
				}
			strdoage.append("</select>");
			str.append("<td><s>"+strdoage.toString()+"</td>");
			
			
			//str.append("<td>"+priscription.getPriscdays()+" "+priscription.getPriscdurationtype()+"</td>");
			/*strdays.append("<select class='form-control' onchange='changepriscgivendays("+i+",this.value)' name='days"+i+"' id='days"+i+"' style='height:18px !important;'>");
			strdays.append("<option value='"+priscription.getPriscdays()+"'>"+priscription.getPriscdays()+"</option>");
			for(int p=1;p<=90;p++){
				strdays.append("<option value='"+p+"'>"+p+"</option>");
				}
			strdays.append("</select>");
			str.append("<td><s>"+strdays.toString()+"</td>");*/

			str.append("<td><input placeholder='days' type='number' class='form-control' onchange='changepriscgivendays("+i+",this.value)' id='priscday"+i+"' name='priscdays"+i+"' size='5' maxlength='5' value='"+priscription.getPriscdays()+"' ></td>");
			
			str.append("<td id='prisctimename"+i+"'>"+priscription.getPrisctimename()+"</td>");
			/*str.append("<td>"+priscription.getUnit()+""+priscription.getUnitextension()+"</td>");*/
			/*str.append("<td>"+priscription.getUnit()+"");
			if(priscription.getUnitextension()!=null){
				if(!priscription.getUnitextension().equals("0")){
					str.append(""+priscription.getUnitextension()+"");
				}
			}
			str.append("</td>");*/
			/*str.append("<td>"+priscription.getPrisctotal()+"</td>");*/
			
			str.append("<td><input type='number' id='drgivenqty"+i+"' onchange='changepriscgivenqty("+i+",this.value)' value='"+priscription.getPriscqty()+"'  class='form-control'></td>");
			str.append("<td id='dosenotes"+i+"'>"+priscription.getDosenotes()+"</td>");
			/*str.append("<td><i onclick='showedit("+i+")' class='fa fa-edit' ></i></td>");*/
			
			/*str.append("<td>"+priscription.getPriscindivisualremark()+"</td>");*/
			str.append("<td><input placeholder='Remark' type='textarea' rows='2' class='form-control' id='priscindivisualremark"+i+"' onchange='changepriscgivenremark("+i+",this.value)' name='remark"+i+"' value='"+priscription.getPriscindivisualremark()+"' ></td>");
			
			str.append("<td><i onclick='deletepriscdata("+i+")' class='fa fa-trash-o' style='color:#d9534f;cursor:pointer;'></i></td>");
		/*	String temp[] = priscription.getLastmodified().split(" ");
			
			if(temp[0].equals(curdate)){
				str.append("<td><i onclick='deletepriscdata("+i+")' class='fa fa-trash-o' ></i></td>");
			}else{
				str.append("<td><i  class='fa fa-trash-o' ></i></td>");
			}*/
			
			str.append("</tr>");
			
			i++;
		}
		
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+str.toString()+"");
		
	}catch(Exception e){
		e.printStackTrace();
	}
	
	return null;
}

public String editparentprisc(){
	
	String selectedid = request.getParameter("selectedid");
	Connection connection = null;
	try{
		
		
		connection = Connection_provider.getconnection();
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		
		Priscription pr = emrDAO.getPriscriptionParentData(Integer.parseInt(selectedid));
		
		StringBuffer str = new StringBuffer();
		
		String parentdata = pr.getPrepay() + "~" + pr.getPostpay() + "~" + pr.getOtherpay() + "~" + pr.getPriscdosenotes() + "~"
				+ pr.getFollowupsqty() + "~" + pr.getFollowupstype() + "~" + pr.getEnglish() + "~" + pr.getRegional() + "~" 
				+ pr.getHindi() + "~" + pr.getPriscadvoice();
		
		
		ArrayList<Master>dosageList = emrDAO.getDosageList();
		ArrayList<Priscription>selectedPriscList = emrDAO.getSelectedPriscList(selectedid);
		session.setAttribute("priscList", selectedPriscList);
		
		int i = 0;
		str.append("<input type='hidden' name='parenteditdataid' id='parenteditdataid' value='"+parentdata+"'>");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		//cal.add(Calendar.DATE, -7); 
		String curdate = dateFormat.format(cal.getTime());
		str.append("<input type='hidden' name='listsizeid' id='listsizeid' value='0'>");
		for(Priscription priscription : selectedPriscList){
			str.append("<tr>");
			str.append("<td><input type='text' id='srno"+i+"' value='"+priscription.getSrno()+"' onclick='changepriscgivensrno("+i+",this.value)' class='form-control'></td>");
			str.append("<input type='hidden' class='akash' value='" + priscription.getMdicinenameid() + "' class='form-control' />");
			str.append("<input type='hidden' class='akashclasss' value='" + i + "' class='form-control' />");
			str.append("<input type='hidden' id='dddose"+i+"' value='"+priscription.getDddose()+"'  class='form-control'>");
			str.append("<input type='hidden' id='uom"+i+"' value='"+priscription.getUom()+"'  class='form-control'>");
			str.append("<td>"+priscription.getMdicinenametxt()+"</td>");
			//str.append("<td>"+priscription.getUnit()+"</td>");
			if(loginInfo.getOutoprisc()==1){
				str.append("<td>"+priscription.getDddose()+" "+priscription.getUom()+"</td>");
			}else{
				if(priscription.getUnit()==null){
					str.append("<td>");
				}else{
					String unit = DateTimeUtils.isNull(priscription.getUnit());
					str.append("<td>"+unit+"");
				}
				if(priscription.getUnitextension()!=null){
					if(!priscription.getUnitextension().equals("0")){
						str.append(""+priscription.getUnitextension()+"");
					}
				}
				str.append("</td>");
			}
			str.append("<td>");
			str.append("<select class='form-control' onchange='changepriscgivendose("+i+",this.value)' name='dosage"+i+"' id='dosage"+i+"'>");
			str.append("<option value='"+priscription.getPriscdose()+"'>"+priscription.getPriscdose()+"</option>");
				for(Master master : dosageList){
					str.append("<option value='"+master.getName()+"'>"+master.getName()+"</option>");
				}
			str.append("</select>");
			str.append("</td>");
			str.append("<td><input placeholder='days' type='number' class='form-control' onchange='changepriscgivendays("+i+",this.value)' id='priscday"+i+"' name='priscdays"+i+"' size='5' maxlength='5' value='"+priscription.getPriscdays()+"' ></td>");
			str.append("<td>"+priscription.getPrisctimename()+"</td>");
			str.append("<td><input type='number' id='drgivenqty"+i+"' value='"+priscription.getPriscqty()+"' onchange='changepriscgivenqty("+i+",this.value)' class='form-control'></td>");
			str.append("<td>"+priscription.getDosenotes()+"</td>");
			str.append("<td><input placeholder='Remark' type='textarea' rows='2' class='form-control' id='priscindivisualremark"+i+"' onchange='changepriscgivenremark("+i+",this.value)' name='remark"+i+"' value='"+priscription.getPriscindivisualremark()+"' ></td>");
			str.append("<td><i onclick='deletepriscdata("+i+")' class='fa fa-trash-o' ></i></td>");
			str.append("</tr>");
			i++;
		}
		
		
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+str.toString()+"");
		
	}catch(Exception e){
		e.printStackTrace();
	}
	
	return null;
}

public String editdischargemedicine(){
	
	String selectedid = request.getParameter("selectedid");
	String ipdid = request.getParameter("ipdid");
	String clientid= request.getParameter("clientid");
	Connection connection = null;
	try{
		
		
		connection = Connection_provider.getconnection();
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		
		Priscription pr = emrDAO.getPriscriptionParentData(Integer.parseInt(selectedid));
		
		StringBuffer str = new StringBuffer();
		
		String parentdata = pr.getPrepay() + "~" + pr.getPostpay() + "~" + pr.getOtherpay() + "~" + pr.getPriscdosenotes() + "~"
				+ pr.getFollowupsqty() + "~" + pr.getFollowupstype() + "~" + pr.getEnglish() + "~" + pr.getRegional() + "~" 
				+ pr.getHindi() + "~" + pr.getPriscadvoice();
		
		
		ArrayList<Priscription>selectedPriscList = emrDAO.getIpdSelectedPriscList(ipdid,clientid);
		session.setAttribute("priscList", selectedPriscList);
		
		int i = 0;
		str.append("<input type='hidden' name='parenteditdataid' id='parenteditdataid' value='"+parentdata+"'>");
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		//cal.add(Calendar.DATE, -7); 
		String curdate = dateFormat.format(cal.getTime());
		str.append("<input type='hidden' name='listsizeid' id='listsizeid' value='0'>");
		for(Priscription priscription : selectedPriscList){
			str.append("<tr>");
			/*str.append("<td>"+priscription.getDate()+"</td>");*/
			
			/*str.append("<td>"+priscription.getPrisctype()+"</td>");*/
			str.append("<td>"+priscription.getMdicinenametxt()+"</td>");
			/*str.append("<td>"+priscription.getPriscfreq()+"</td>");*/
			str.append("<td>"+priscription.getPriscdose()+"</td>");
			str.append("<td>"+priscription.getPriscdays()+" "+priscription.getPriscdurationtype()+"</td>");
			/*str.append("<td>"+priscription.getPrisctotal()+"</td>");*/
			str.append("<td>"+priscription.getDosenotes()+"</td>");
			/*str.append("<td><i onclick='showedit("+i+")' class='fa fa-edit' ></i></td>");*/
			str.append("<td><i onclick='deletepriscdata("+i+")' class='fa fa-trash-o' ></i></td>");
		/*	String temp[] = priscription.getLastmodified().split(" ");
			
			if(temp[0].equals(curdate)){
				str.append("<td><i onclick='deletepriscdata("+i+")' class='fa fa-trash-o' ></i></td>");
			}else{
				str.append("<td><i  class='fa fa-trash-o' ></i></td>");
			}*/
			
			str.append("</tr>");
			
			i++;
		}
		
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+str.toString()+"");
		
	}catch(Exception e){
		e.printStackTrace();
	}
	
	return null;
}


public String addtempeq(){
	
	String categoryid = request.getParameter("categoryid");
	String mdicinenameid = request.getParameter("mdicinenameid");
	String mdicinenametxt = request.getParameter("mdicinenametxt");
	String priscdose = request.getParameter("priscdose");
	String priscfreq = request.getParameter("priscfreq");
	String priscdays = request.getParameter("priscdays");
	
	String prisctype =  request.getParameter("prisctype");
	String prisccode = request.getParameter("prisccode");
	String prisctotal = request.getParameter("prisctotal");
	String priscdurationtype = request.getParameter("priscdurationtype");
	String dosenotes=request.getParameter("dosenotes");
	
	String clientId = request.getParameter("clientId");
	String prectionerid = request.getParameter("prectionerid");
	String conditionid = request.getParameter("conditionid");
	
	String templatename = request.getParameter("templatename");
	
	
	Priscription priscription = new Priscription();
	
	ArrayList<Priscription>oteqlist = new ArrayList<Priscription>();
	if(session.getAttribute("oteqlist")!=null){
		oteqlist = (ArrayList<Priscription>)session.getAttribute("oteqlist");
	}
	
	Connection connection = null;
	
	
	try{
		connection = Connection_provider.getconnection();
		EmrDAO  emrDAO = new JDBCEmrDAO(connection);
		priscfreq = emrDAO.getMedicineStrength(mdicinenameid);
		
		priscription.setClientId(clientId);
		priscription.setPrectionerid(prectionerid);
		priscription.setConditionid(conditionid);
		
		priscription.setCategoryid(categoryid);
		priscription.setMdicinenameid(mdicinenameid);
		priscription.setMdicinenametxt(mdicinenametxt);
		priscription.setPriscdose(priscdose);
		priscription.setPriscfreq(priscfreq);
		priscription.setPriscdays(priscdays);
		
		priscription.setPrisctype(prisctype);
		priscription.setPrisccode(prisccode);
		priscription.setPrisctotal(prisctotal);
		priscription.setPriscdurationtype(priscdurationtype);
		priscription.setDosenotes(dosenotes);
		priscription.setLastmodified(DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
		
		oteqlist.add(priscription);
		
		session.setAttribute("oteqlist", oteqlist);
		
		if(templatename.equals("0")){
			getOTEquipmentData();
		}else{
			getoteqTemplateData();
		}
		
		
	}catch(Exception e){
		e.printStackTrace();
	}
	
	return null;
	
	
	
}

public String addtempprisc(){
	 
	 String categoryid = request.getParameter("categoryid");
	 String mdicinenameid = request.getParameter("mdicinenameid");
	 String mdicinenametxt = request.getParameter("mdicinenametxt");
	 String priscdose = request.getParameter("priscdose");
	 String priscfreq = request.getParameter("priscfreq");
	 String priscdays = request.getParameter("priscdays");
	 String dddose = request.getParameter("dddose");
	 double tttqty = 0;
	 
	 String prisctype =  request.getParameter("prisctype");
	 String prisccode = request.getParameter("prisccode");
	 String prisctotal = request.getParameter("prisctotal");
	 String priscdurationtype = request.getParameter("priscdurationtype");
	 String dosenotes=request.getParameter("dosenotes");
	 //
	 String clientId = request.getParameter("clientId");
	 String prectionerid = request.getParameter("prectionerid");
	 String conditionid = request.getParameter("conditionid");
	 
	 String templatename = request.getParameter("templatename");
	 String genericName =request.getParameter("genericName");
	 String medicineName =request.getParameter("medicineName");
	 String unit =request.getParameter("unit");
	 String priscindivisualremark = request.getParameter("priscindivisualremark");
	 if(priscindivisualremark==null){
		 priscindivisualremark="";
	 }
	 if(priscindivisualremark.equals("null")){
		 priscindivisualremark=""; 
	 }
	 
	 String prisctime = request.getParameter("prisctime");
	 String prisctimename = request.getParameter("prisctimename");
	 
	 String unitextension = request.getParameter("unitextension");
	 String dosegiventime1 = request.getParameter("dosegiventime1");
	 String dosegiventime2 = request.getParameter("dosegiventime2");
	 String dosegiventime3 = request.getParameter("dosegiventime3");
	 
	 String priscqty =request.getParameter("priscqty");
	 String masterdose = request.getParameter("masterdose");
	 Priscription priscription = new Priscription();
	 
	 ArrayList<Priscription>priscList = new ArrayList<Priscription>();
	 if(session.getAttribute("priscList")!=null){
	  priscList = (ArrayList<Priscription>)session.getAttribute("priscList");
	 }
	 
	 Connection connection = null;
	 
	 
	 try{
	  connection = Connection_provider.getconnection();
	  EmrDAO  emrDAO = new JDBCEmrDAO(connection);
	 
	 
	  	String med= emrDAO.getMedicicneName(mdicinenameid);
	  	mdicinenametxt = med;
	  	
	  if(mdicinenameid.equals("0")){
	    
	    int res = emrDAO.saveNewMedicine(genericName,medicineName);
	    mdicinenameid = String.valueOf(res);
	    mdicinenametxt= medicineName;
	   
	  }

	  
	  
	  
	 priscfreq = emrDAO.getMedicineStrength(mdicinenameid);
	  String medicine_shedule = emrDAO.getMedicineShedule(mdicinenameid);
	  String uom = emrDAO.getMedicineUom(mdicinenameid);
	  priscription.setUom(uom);
	  
	  if(uom==null){
		  priscription.setUom("");
	  }
	  
	  priscription.setClientId(clientId);
	  priscription.setPrectionerid(prectionerid);
	  priscription.setConditionid(conditionid);
	  
	  priscription.setCategoryid(categoryid);
	  priscription.setMdicinenameid(mdicinenameid);
	  priscription.setMdicinenametxt(mdicinenametxt);
	  priscription.setPriscdose(priscdose);
	  priscription.setPriscfreq(priscfreq);
	  priscription.setPriscdays(priscdays);
	 
	  if(loginInfo.getOutoprisc()==1){
	  //dose  farmula
	  //Dose (DailyDose) = ((Wt x M_Dosage) / Frequency) / Strength
	  ClientDAO clientDAO = new JDBCClientDAO(connection);
	 
	  PrescriptionMasterDAO masterDAO= new JDBCPrescriptionMasterDAO(connection);
	  Priscription pr= masterDAO.getPrescriptionDetails(mdicinenameid);
	  
	  String weight = clientDAO.getClientWeight(clientId);
	 /* double dddose = (Double.parseDouble(weight) * 
			  Double.parseDouble(pr.getCaldose())) / 
			  Double.parseDouble(pr.getFrequency()) /
			  Double.parseDouble(pr.getStrength());*/
	  
	  priscription.setDddose(dddose);
	  
	  //total qty farmula
	  //Total Qty = (Dose (1.1) X Frequency X Days)/ QTY
	 /* if(priscqty==null){
		  priscqty = pr.getDr_qty();
	  }
	  tttqty = dddose *1.1 * Double.parseDouble(pr.getFrequency())
			  * Double.parseDouble(priscdays)/ Double.parseDouble(priscqty);*/
	  }else{
		  // Akash 14 sep 2018 hidding null
		  priscription.setDddose("");
	  }
	  
	  
	  priscription.setMedicine_shedule(medicine_shedule);
	  
	  priscription.setPrisctype(prisctype);
	  priscription.setPrisccode(prisccode);
	  priscription.setPrisctotal(prisctotal);
	  priscription.setPriscdurationtype(priscdurationtype);
	  priscription.setDosenotes(dosenotes);
	  unit = DateTimeUtils.isNull(unit);
	  priscription.setUnit(unit);
	  priscription.setMasterdose(masterdose);
	  priscription.setLastmodified(DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
	  
	  //Akash 30 June 2018
	  priscription.setPriscqty(priscqty);
	  
	  //Akadh 11 April 2018
	  priscription.setPriscindivisualremark(priscindivisualremark);
	  priscription.setPriscriptiontime(prisctime);
	  priscription.setPrisctimename(prisctimename);
	  
	  //Akash 13 April 2018
	  priscription.setUnitextension(unitextension);
	  
	  //Akash 07 feb 2018 to set sr no
	  int size = priscList.size();
	  if (size > 0) {
	   size = size+1;
	  } else {
	   size =1;
	  }
	  
	  priscription.setSrno(String.valueOf(size));
	  
	  //Akash 10 MaY 2018
	  if(priscdose!=null){
		  boolean flag = true;
		  if(dosegiventime1!=null && dosegiventime2!=null && dosegiventime3!=null){
			if(dosegiventime1.equals("") || (dosegiventime2.equals("")) || (dosegiventime2.equals(""))){
				flag = false;
			}
		  }else{
			  flag = false;
		  }
		  if(flag){
			  if(priscdose.equals("1-0-1") || priscdose.equals("2-0-2")){
				  priscription.setDosegiventime(dosegiventime1+"-"+dosegiventime2+"-"+dosegiventime3);
			  }else if(priscdose.equals("1-1-1") || priscdose.equals("2-2-2")){
				  priscription.setDosegiventime(dosegiventime1+"-"+dosegiventime2+"-"+dosegiventime3);
			  }else if(priscdose.equals("1-0-0") || priscdose.equals("2-0-0")){
				  priscription.setDosegiventime(dosegiventime1+"-"+dosegiventime2+"-"+dosegiventime3);
			  }else if(priscdose.equals("1-1-0") || priscdose.equals("2-2-0")){
				  priscription.setDosegiventime(dosegiventime1+"-"+dosegiventime2+"-"+dosegiventime3);
			  }else if(priscdose.equals("0-1-0") || priscdose.equals("0-2-0")){
				  priscription.setDosegiventime(dosegiventime1+"-"+dosegiventime2+"-"+dosegiventime3);
			  }else if(priscdose.equals("0-1-1") || priscdose.equals("0-2-2")){
				  priscription.setDosegiventime(dosegiventime1+"-"+dosegiventime2+"-"+dosegiventime3);
			  }else if(priscdose.equals("0-0-1") || priscdose.equals("0-0-2")){
				  priscription.setDosegiventime(dosegiventime1+"-"+dosegiventime2+"-"+dosegiventime3);
			  }else{
				  priscription.setDosegiventime(null);
			  }
		  }else{
			  priscription.setDosegiventime(null);
		  }
	  }else{
		  priscription.setDosegiventime(null);
	  }
	  EmrDAO emrDAO2= new JDBCEmrDAO(connection); 
	  String medtype="";
	  medtype= emrDAO2.getMedtype(priscription.getMdicinenameid());
	  if(medtype!=null){
		  priscription.setMdicinenametxt(priscription.getMdicinenametxt()+"("+medtype+")");
	  }
	  
	  if(loginInfo.getOutoprisc()==1){
		  String gen= emrDAO2.getGenerictype(priscription.getMdicinenameid());
		  if(!"".equals(gen)){
		  priscription.setMdicinenametxt(gen+"-"+priscription.getMdicinenametxt());
		  }
	  }
		  
	  
	 /* if(loginInfo.getOutoprisc()==1){
		  double valueRounded = Math.ceil(tttqty);
		  priscription.setPriscqty(""+valueRounded+"");
	  }*/
	  
	  priscList.add(priscription);
	 
	  session.setAttribute("priscList", priscList);
	  int x= emrDAO.updatePrisc(priscription);
	  
	  if(templatename.equals("0")){
			if(loginInfo.getOutoprisc()==1){
				getNimaiPriscriptionTemplateData();
			}else{
				getPriscriptionData();
			}
	  }else{
		  getPriscriptionTemplateData();
	  }
	  
	  
	 }catch(Exception e){
	  e.printStackTrace();
	 }
	 
	 return null;
	}

public String listot(){
	Connection connection = null;
	try{
		connection = Connection_provider.getconnection();
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		
		String apmtid = request.getParameter("apmtid");
		ArrayList<Emr>listot = emrDAO.getOtEqList(apmtid);
		emrForm.setListot(listot);
		//letter head
		Clinic clinic = new Clinic();
		ClinicDAO clinicDAO  = new JDBCClinicDAO(connection);
		AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
		ArrayList<Clinic> locationAdressList = accountsDAO.getLetterHeadDetails(loginInfo.getClinicid());
		
		clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
		emrForm.setClinicName(clinic.getClinicName());
		emrForm.setClinicOwner(clinic.getClinicOwner());
		emrForm.setOwner_qualification(clinic.getOwner_qualification());
	//	emrForm.setLocationAdressList(locationAdressList);
		//accountsForm.setAddress(clinic.getAddress());
		emrForm.setLandLine(clinic.getLandLine());
		emrForm.setClinicemail(clinic.getEmail());
		emrForm.setWebsiteUrl(clinic.getWebsiteUrl());
		
		emrForm.setLocationAdressList(locationAdressList);
		
	}catch (Exception e) {
		// TODO: handle exception
	}
	
	return "listot";
}


public String printprisc(){
	
	
	Connection connection = null;
	try{
		
		request.setCharacterEncoding("UTF-8");
		
		connection = Connection_provider.getconnection();
		connection=DriverManager.getConnection(""+Constants.DB_HOST+":3306/"+loginInfo.getClinicUserid()+"?useUnicode=true&characterEncoding=UTF-8","pranams","6qxi5x&)~XBZ");
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		ClientDAO clientDAO = new JDBCClientDAO(connection);
		AccountsDAO accountsDAO =new JDBCAccountsDAO(connection);
		int selectedid=0;
		String halfprint =request.getParameter("halfprint");
		if(request.getParameter("selectedid")==null){
			selectedid=0;
		}else{
			selectedid = Integer.parseInt(request.getParameter("selectedid"));
		}
		int sessiid = 0;
		if(selectedid==0){
			sessiid = (Integer)session.getAttribute("saveparent");
			selectedid = sessiid;
			/*if(sessiid!=0){
				session.removeAttribute("saveparent");
			}*/
		}
		/*if(selectedid==0){
			selectedid = (Integer)session.getAttribute("saveparent");
		}*/
		String newparentid = (String) session.getAttribute("prisc_newparentid");
		boolean flag=false;
		int newsessiid =0;
		if(newparentid!=null){
			newsessiid = Integer.parseInt(newparentid);
			emrForm.setHidecolumn(true);
			flag=true;
			session.removeAttribute("prisc_newparentid");
		}else{
			newparentid=request.getParameter("newparentid");
			if(newparentid!=null)
			{
				newsessiid = Integer.parseInt(newparentid);
				flag=true;
				emrForm.setHidecolumn(true);
			}
			
			
		}
		if(halfprint!=null){
			if(halfprint.equals("1")){
				if(!emrForm.isHidecolumn()){
					emrForm.setHidecolumn(true);
				}
			}
		}
		Priscription priscription = emrDAO.getPriscriptionParentData(selectedid);
		session.setAttribute("parentpriscdata", priscription);
		System.out.println(priscription.getClientId());
		String lastmodified="";
		String lastmodified1="";
		if(priscription.getLastmodified()!=null){
			
			lastmodified=priscription.getLastmodified().split(" ")[0];
			lastmodified1=priscription.getLastmodified();
			priscription.setLastmodified(lastmodified);
			
		}
	
		if(sessiid!=0){
				emrForm.setHidecolumn(true);
		}
		
		PrescriptionDAO prescriptionDAO=new JDBCPrescriptionDAO(connection);
		Client client = clientDAO.getPatient(Integer.parseInt(priscription.getClientId()));
		
		String name = client.getTitle()+" "+client.getFirstName()+" "+client.getLastName();
		
		String date  = DateTimeUtils.getDashboardTodayDate(loginInfo.getCountry());
		
		String dobyear[] = client.getDob().split("/");
		String curryear[] = date.split("/");
		
		int  age = Integer.parseInt(curryear[2]) - Integer.parseInt(dobyear[2]);
		
		String agegender="";
		String dob = client.getDob();
		String age2 = DateTimeUtils.getAge(client.getDob());
		String age3= DateTimeUtils.getAge1(client.getDob());
		if(Integer.parseInt(age2)<2){
			if(Integer.parseInt(age2)<1){
				String monthdays= DateTimeUtils.getMonthandDays(client.getDob());
				agegender=monthdays;
			}else{
				String monthdays= DateTimeUtils.getMonthandDays(client.getDob());
				agegender= age2 + " Years" + " "+monthdays;
			}
		} else {
			agegender = age2;	
		}
		
		client.setAgegender(agegender);
		client.setAge(age);
		client.setAge1(age3);
		session.setAttribute("clientinfo", client);
		
		// get clinic info
		
		Clinic clinic = new Clinic();
		ClinicDAO clinicDAO  = new JDBCClinicDAO(connection);
		clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
		
		Location location = clinicDAO.getRegisterdLication();
		clinic.setLocationname(location.getAddress());
		
		/*String curdatetime = DateTimeUtils.getPriscDatetime(loginInfo.getTimeZone());
		clinic.setCurDateTime(curdatetime);*/
		clinic.setCurDateTime(lastmodified1);
		ArrayList<Priscription> priscList =new ArrayList<Priscription>();
		session.setAttribute("clinicinfo", clinic);
		if(flag){
			priscList = prescriptionDAO.getChildPriscData(newsessiid);
			int size1 = priscList.size();
			emrForm.setPrisctimestatus(false);
			emrForm.setPriscreamrkstatus(false);
			emrForm.setPriscunitstatus(false);
			emrForm.setIsipdtimeshow("0");
			emrForm.setMasterdosestatus(false);
			emrForm.setUserid(priscList.get(size1-1).getUserid());
			emrForm.setDosenotesstauss(false);
		}else{
			priscList = emrDAO.getPrintPriscList(Integer.toString(selectedid),loginInfo);
			int size1 = priscList.size();
			if(size1>0){
				boolean prisctimestatus = priscList.get(size1-1).isPrisctimestatus();
				boolean priscreamrkstatus = priscList.get(size1-1).isPriscreamrkstatus();
				boolean priscunitstatus = priscList.get(size1-1).isPriscunitstatus();
				String isipdtimeshow = priscList.get(size1-1).getIsipdtimeshow();
				boolean remarkstatus=priscList.get(size1-1).isPriscremark();
				emrForm.setPrisctimestatus(prisctimestatus);
				emrForm.setPriscreamrkstatus(remarkstatus);
				emrForm.setPriscunitstatus(priscunitstatus);
				emrForm.setIsipdtimeshow(isipdtimeshow);
				
				//Akash 01 aug 2018
				boolean masterdosestatus = priscList.get(size1-1).isMasterdosestatus();
				emrForm.setMasterdosestatus(masterdosestatus);
				emrForm.setDosenotesstauss(priscList.get(size1-1).isDosenotesstaus());
			}else{
				emrForm.setPrisctimestatus(false);
				emrForm.setPriscreamrkstatus(false);
				emrForm.setPriscunitstatus(false);
				emrForm.setIsipdtimeshow("0");
				emrForm.setMasterdosestatus(false);
				emrForm.setDosenotesstauss(false);
			}
		}
		//Akash 04 May 2018 hiding header
	
		if(priscription.getIpdid()!=null){
			if(!priscription.getIpdid().equals("") || (!priscription.getIpdid().equals("0"))){
				emrForm.setIsfromipd("1");
			}
		}
		session.setAttribute("authorisedPrisc", priscList);
		
		
		
		String ipdid = emrDAO.getpriscIpdId(selectedid);
		BedDao bedDao = new JDBCBedDao(connection);
		UserProfileDAO profileDAO= new JDBCUserProfileDAO(connection);
		IpdDAO  ipdDAO = new JDBCIpdDAO(connection);
		Bed bed = bedDao.getEditIpdData(ipdid);
		
		
		
		emrForm.setIpdid(ipdid);
		if(ipdid==null || ipdid.equals("0")){
			priscription.setIpdid("0");
		}else{
			priscription.setIpdid(ipdid);
		}
		
		String wardname=ipdDAO.getIpdWardName(bed.getWardid());
		priscription.setWardname(wardname);
		String bedname = ipdDAO.getIpdBedName(bed.getBedid());
		priscription.setBedname(bedname);
		
		emrForm.setWardname(wardname);
		emrForm.setBedname(bedname);
	    emrForm.setId(selectedid);
		emrForm.setLocation(priscription.getLocation());
	    
	    ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
		
		clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
		emrForm.setClinicName(clinic.getClinicName());
		emrForm.setClinicOwner(clinic.getClinicOwner());
		emrForm.setOwner_qualification(clinic.getOwner_qualification());
		emrForm.setLocationAdressList(locationAdressList);
		emrForm.setAddress(clinic.getAddress());
		emrForm.setLandLine(clinic.getLandLine());
		emrForm.setClinicemail(clinic.getEmail());
		emrForm.setWebsiteUrl(clinic.getWebsiteUrl());
		emrForm.setClinicLogo(clinic.getUserImageFileName());
		emrForm.setAbrivationid(client.getAbrivationid());
		
		if(Integer.parseInt(priscription.getIpdid())>0){
			String practitionerid= bed.getPractitionerid();
			UserProfile userProfile= profileDAO.getUserprofileDetails(Integer.parseInt(practitionerid));
			emrForm.setPractitionerName(userProfile.getFullname());
			emrForm.setQualification(userProfile.getQualification());
			emrForm.setDrregno(userProfile.getRegisterno());
		}else{
			String practitionerid= priscription.getPrectionerid();
			UserProfile userProfile= profileDAO.getUserprofileDetails(Integer.parseInt(practitionerid));
			emrForm.setPractitionerName(userProfile.getFullname());
			emrForm.setQualification(userProfile.getQualification());
			emrForm.setDrregno(userProfile.getRegisterno());
		}
		
	    if(loginInfo.getOutoprisc()==1){
	    	return "newprintprisc";
	    }
	    
	}catch(Exception e){
		e.printStackTrace();
	}
	
	return "printprisc";
}



public String savetemplateoteq(){
	
	String mdcinenametxt = request.getParameter("mdcinenametxt");
	String priscfreq = request.getParameter("priscfreq");
	String dosage = request.getParameter("dosage");
	String days = request.getParameter("days");
	String dosenotes = request.getParameter("dosenotes");
	
	String prepay = request.getParameter("prepay");
	String postpay = request.getParameter("postpay");
	String otherpay = request.getParameter("otherpay");
	String priscautoid = request.getParameter("priscautoid");
	String priscdosenotes = request.getParameter("priscdosenotes");
	String followupsqty = request.getParameter("followupsqty");
	String followupstype = request.getParameter("followupstype");
	String priscadvoice = request.getParameter("priscadvoice");
	String english = request.getParameter("english");
	String regional  = request.getParameter("regional");
	String hindi = request.getParameter("hindi");
	String discharge = request.getParameter("discharge");
	
	String clientId = request.getParameter("clientId");
	String prectionerid = request.getParameter("prectionerid");
	String conditionid = request.getParameter("conditionid");
	String editAppointId = request.getParameter("editAppointId");
	
	Priscription priscription = new Priscription();
	priscription.setMdicinenametxt(mdcinenametxt);
	priscription.setPriscdose(dosage);
	priscription.setPriscfreq(priscfreq);
	priscription.setPriscdurationtype("Days");
	priscription.setDosenotes(dosenotes);
	priscription.setPriscdays(days);
	
	Connection connection = null;
	try{
		connection = Connection_provider.getconnection();
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		
		int saveparent = (Integer)session.getAttribute("savetemplateparentoteq");
		int result  = emrDAO.saveOtEquipment(priscription,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),saveparent,clientId,prectionerid,conditionid,editAppointId);
		
	}catch (Exception e) {
		// TODO: handle exception
	}
	
	
	return null;
}


public String savetemplateprisc(){
	
	
	String mdcinenametxt = request.getParameter("mdcinenametxt");
	String priscfreq = request.getParameter("priscfreq");
	String dosage = request.getParameter("dosage");
	String dddose = request.getParameter("dddose");
	String days = request.getParameter("days");
	String dosenotes = request.getParameter("dosenotes");
	String mdicinenameid= request.getParameter("mdicinenameid");
	String prepay = request.getParameter("prepay");
	String postpay = request.getParameter("postpay");
	String otherpay = request.getParameter("otherpay");
	String priscautoid = request.getParameter("priscautoid");
	String priscdosenotes = request.getParameter("priscdosenotes");
	String followupsqty = request.getParameter("followupsqty");
	String followupstype = request.getParameter("followupstype");
	String priscadvoice = request.getParameter("priscadvoice");
	String english = request.getParameter("english");
	String regional  = request.getParameter("regional");
	String hindi = request.getParameter("hindi");
	String discharge = request.getParameter("discharge");
	String priscindivisualremark = request.getParameter("priscindivisualremark");
	String unitextension = request.getParameter("unitextension");
	String srno = request.getParameter("srno");
	
	String clientId = request.getParameter("clientId");
	String prectionerid = request.getParameter("prectionerid");
	String conditionid = request.getParameter("conditionid");
	String priscqty = request.getParameter("priscqty");
	
	String parentsuperlistsizeid = request.getParameter("parentsuperlistsizeid");
	String current_size_data = request.getParameter("current_size_data");
	
	String prisctimename = request.getParameter("prisctimename");
	Priscription priscription = new Priscription();
	priscription.setMdicinenametxt(mdcinenametxt);
	priscription.setMdicinenameid(mdicinenameid);
	priscription.setPriscdose(dosage);
	priscription.setPriscfreq(priscfreq);
	priscription.setPriscdurationtype("Days");
	priscription.setDosenotes(dosenotes);
	priscription.setPriscdays(days);
	priscription.setDddose(dddose);
	priscription.setPrisctimename(prisctimename);
	priscription.setRemark(priscindivisualremark);
	priscription.setPriscindivisualremark(priscindivisualremark);
	priscription.setSrno(srno);
	priscription.setUnitextension(unitextension);
	
	
	if(priscqty!=null){
		if(priscqty.equals("")){
			priscqty="0";
		}
	}else{
		priscqty="0";
	}
	priscription.setPriscqty(priscqty);
	Connection connection = null;
	try{
		connection = Connection_provider.getconnection();
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		
		int saveparent = (Integer)session.getAttribute("savetemplateparent");
		int result  = emrDAO.savePriscription(priscription,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),saveparent,clientId,prectionerid,conditionid);
		
		int parentlastsize = Integer.parseInt(DateTimeUtils.numberCheck(parentsuperlistsizeid));
		int parentcurrentsize = Integer.parseInt(DateTimeUtils.numberCheck(current_size_data));
		parentlastsize = parentlastsize-1;
		if(parentcurrentsize==parentlastsize){
			Priscription priscriptionnew = emrDAO.getPriscriptionParentData(saveparent);
			String date = "";
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			date = dateFormat.format(cal.getTime());
			priscriptionnew.setDate(date);
			priscriptionnew.setUserid(loginInfo.getUserId());
			int directtransfer =1;
			if(!loginInfo.isDirect_prisc()){
				directtransfer = 0;
			}
			int newparentid = emrDAO.saveParentPriscNew(priscriptionnew,"",""+saveparent,priscriptionnew.getDefault_location(),directtransfer);
			ArrayList<Priscription> arrayList= emrDAO.getPriscriptionChildData(""+saveparent,0);
			for(Priscription priscription2 : arrayList){
				String mdrequestqty = priscription2.getPriscqty();
				String mdname = priscription2.getMdicinenametxt();
				String mdid = priscription2.getMdicinenameid();
				String childid =""+priscription2.getId();
				String newchild = childid;
				int res = emrDAO.saveChildPriscNew(priscription2,mdrequestqty,""+saveparent,childid,newparentid);
			}
		}
		
	}catch (Exception e) {
		// TODO: handle exception
	}
	
	
	
	
	return null;
}

public String oteqtemplate(){
	Connection connection = null;
	try{
		
		
		connection = Connection_provider.getconnection();
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		
		//set template name list
		
		ArrayList<Priscription>templateNameList = emrDAO.getoteqTemplateNameList();
		
		StringBuffer str = new StringBuffer();
		str.append("<select onchange='showTemplateoteq(this.value)' name='templatename' id='eottemplatename' class='form-control chosen-select'>");
		str.append("<option value='0'>Select Template</option>");
		for(Priscription priscription : templateNameList){
			str.append("<option value='"+priscription.getId()+"'>"+priscription.getTemplatename()+"</option>");
		}
		str.append("</select>");
		
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+str.toString()+""); 
		
		
	}catch (Exception e) {
		// TODO: handle exception
	}
	
	return null;
}


public String prisctemplate(){
	Connection connection = null;
	try{
		
		
		connection = Connection_provider.getconnection();
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		
		//set template name list
		
		ArrayList<Priscription>templateNameList = emrDAO.getTemplateNameList(loginInfo);
		
		StringBuffer str = new StringBuffer();
		str.append("<select onchange='showTemplatePrisc(this.value)' name='templatename' id='templatename' class='form-control chosen-select'>");
		str.append("<option value='0'>Select Template</option>");
		for(Priscription priscription : templateNameList){
			str.append("<option value='"+priscription.getId()+"'>"+priscription.getTemplatename()+"</option>");
		}
		str.append("</select>");
		
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+str.toString()+""); 
		
		
	}catch (Exception e) {
		// TODO: handle exception
	}
	
	return null;
}

public String saveparentoteq(){
	
	String prepay = request.getParameter("prepay");
	String postpay = request.getParameter("postpay");
	String otherpay = request.getParameter("otherpay");
	String priscautoid = request.getParameter("priscautoid");
	String priscdosenotes = request.getParameter("priscdosenotes");
	String followupsqty = request.getParameter("followupsqty");
	String followupstype = request.getParameter("followupstype");
	String priscadvoice = request.getParameter("priscadvoice");
	String english = request.getParameter("english");
	String regional  = request.getParameter("regional");
	String hindi = request.getParameter("hindi");
	String discharge = request.getParameter("discharge");
	
	String clientId = request.getParameter("clientId");
	String prectionerid = request.getParameter("prectionerid");
	String conditionid = request.getParameter("conditionid");
	
	Priscription priscription = new Priscription();
	
	priscription.setPrepay(prepay);
	priscription.setPostpay(postpay);
	priscription.setOtherpay(otherpay);
	priscription.setPriscdose(priscdosenotes);
	priscription.setFollowupsqty(followupsqty);
	priscription.setFollowupstype(followupstype);
	priscription.setPriscadvoice(priscadvoice);
	priscription.setEnglish(english);
	priscription.setRegional(regional);
	priscription.setHindi(hindi);
	priscription.setPriscdosenotes(priscdosenotes);
	
	Connection connection = null;
	try{
		
		
		connection = Connection_provider.getconnection();
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		
		String oldpractid = prectionerid;
		//check if doctor placed with machine
		UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
		UserProfile userProfile = userProfileDAO.getUserprofileDetails(Integer.parseInt(oldpractid));
		
		if(userProfile.getJobgroup().equals("4")){
			prectionerid = userProfile.getDoctor();
		}
	
		priscription.setClientId(clientId);
		priscription.setPrectionerid(prectionerid);
		
		
		priscription.setConditionid(conditionid);
		
		String sessionadmissionid = (String)session.getAttribute("sessionadmissionid");
		
		 int saveparent =  emrDAO.saveParentoteq(priscription,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),sessionadmissionid,discharge);
			session.setAttribute("savetemplateparentoteq", saveparent);
			
			
	}catch (Exception e) {
		// TODO: handle exception
	}
	
	
	
	return null;
}

public String saveparentprisc(){
	String locations= request.getParameter("locations");
	String prepay = request.getParameter("prepay");
	String postpay = request.getParameter("postpay");
	String otherpay = request.getParameter("otherpay");
	String priscautoid = request.getParameter("priscautoid");
	String priscdosenotes = request.getParameter("priscdosenotes");
	String followupsqty = request.getParameter("followupsqty");
	String followupstype = request.getParameter("followupstype");
	String priscadvoice = request.getParameter("priscadvoice");
	String english = request.getParameter("english");
	String regional  = request.getParameter("regional");
	String hindi = request.getParameter("hindi");
	String discharge = request.getParameter("discharge");
	String admission= request.getParameter("admission");
	String clientId = request.getParameter("clientId");
	String prectionerid = request.getParameter("prectionerid");
	String conditionid = request.getParameter("conditionid");
	
	Priscription priscription = new Priscription();
	
	priscription.setPrepay(prepay);
	priscription.setPostpay(postpay);
	priscription.setOtherpay(otherpay);
	priscription.setPriscdose(priscdosenotes);
	priscription.setFollowupsqty(followupsqty);
	priscription.setFollowupstype(followupstype);
	priscription.setPriscadvoice(priscadvoice);
	priscription.setEnglish(english);
	priscription.setRegional(regional);
	priscription.setHindi(hindi);
	priscription.setPriscdosenotes(priscdosenotes);
	
	Connection connection = null;
	try{
		
		
		connection = Connection_provider.getconnection();
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
		String oldpractid = prectionerid;
		//check if doctor placed with machine
		UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
		UserProfile userProfile = userProfileDAO.getUserprofileDetails(Integer.parseInt(oldpractid));
		
		if(userProfile.getJobgroup().equals("4")){
			prectionerid = userProfile.getDoctor();
		}
	
		priscription.setClientId(clientId);
		priscription.setPrectionerid(prectionerid);
		
		
		//Akash 24-10-2019
		String isfromemrdashb = request.getParameter("isfromemrdashb");
		isfromemrdashb = DateTimeUtils.isNull(isfromemrdashb);
		if(isfromemrdashb.equals("1")){
			boolean isinipd = emrDAO.checkIsAdmitedPatient(clientId);
			if(isinipd){
				locations ="ipd";
			}
		}
		
		
		priscription.setConditionid(conditionid);
		
		String sessionadmissionid = (String)session.getAttribute("sessionadmissionid");
		//int default_location = pharmacyDAO.getByDefaultPharmacyLocation();
		String requestlocationid = request.getParameter("requestlocationid");
		int default_location = 0;
		if(requestlocationid!=null){
			if(requestlocationid.equals("") || requestlocationid.equals("0")){
				default_location = pharmacyDAO.getByDefaultPharmacyLocation();
			}else{
				default_location = Integer.parseInt(requestlocationid);
			}
		}else{
			default_location = pharmacyDAO.getByDefaultPharmacyLocation();
		}
		int saveparent =  emrDAO.saveParentPriscription(priscription,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),sessionadmissionid,discharge,"0",loginInfo.getUserId(),locations,default_location);
		session.setAttribute("savetemplateparent", saveparent);
			
		
	}catch (Exception e) {
		// TODO: handle exception
	}
	
	
	
	return null;
}


public String addoteq() throws SQLException{
	Priscription priscription = new Priscription();
	
	String prepay = request.getParameter("prepay");
	String postpay = request.getParameter("postpay");
	String otherpay = request.getParameter("otherpay");
	String priscautoid = request.getParameter("priscautoid");
	String priscdosenotes = request.getParameter("priscdosenotes");
	String followupsqty = request.getParameter("followupsqty");
	String followupstype = request.getParameter("followupstype");
	String priscadvoice = request.getParameter("priscadvoice");
	String english = request.getParameter("english");
	String regional  = request.getParameter("regional");
	String hindi = request.getParameter("hindi");
	String discharge = request.getParameter("discharge");
	
	String clientId = request.getParameter("clientId");
	String prectionerid = request.getParameter("prectionerid");
	String conditionid = request.getParameter("conditionid");
	
	String editparentpriscid = request.getParameter("editparentpriscid");
	String repeatdate=request.getParameter("repeatdate");
	
	String editAppointId = request.getParameter("editAppointId");

	
	
	priscription.setPrepay(prepay);
	priscription.setPostpay(postpay);
	priscription.setOtherpay(otherpay);
	priscription.setPriscdose(priscdosenotes);
	priscription.setFollowupsqty(followupsqty);
	priscription.setFollowupstype(followupstype);
	priscription.setPriscadvoice(priscadvoice);
	priscription.setEnglish(english);
	priscription.setRegional(regional);
	priscription.setHindi(hindi);
	priscription.setPriscdosenotes(priscdosenotes);
	
	
	
	
	Connection connection = null;
	try{
		
		
		connection = Connection_provider.getconnection();
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		
		String oldpractid = prectionerid;
		//check if doctor placed with machine
		UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
		UserProfile userProfile = userProfileDAO.getUserprofileDetails(Integer.parseInt(oldpractid));
		
		if(userProfile.getJobgroup().equals("4")){
			prectionerid = userProfile.getDoctor();
		}
	
		priscription.setClientId(clientId);
		priscription.setPrectionerid(prectionerid);
		priscription.setConditionid(conditionid);
		
		String sessionadmissionid = (String)session.getAttribute("sessionadmissionid");
		System.out.println(editparentpriscid);
		
		
		String istemplate = request.getParameter("istemplate");
		if(istemplate==null){
			istemplate="0";
		}
		
		String mdtemplatename = request.getParameter("mdtemplatename");
		priscription.setTemplatename(mdtemplatename);
		
		if(istemplate.equals("0")){
			  int saveparent =  emrDAO.saveParentotequipment(priscription,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),sessionadmissionid,discharge);
				session.setAttribute("saveparent", saveparent);
				ArrayList<Priscription>priscList = (ArrayList<Priscription>)session.getAttribute("oteqlist");
				for(Priscription priscription2 : priscList){
					int result  = emrDAO.saveOtEquipment(priscription2,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),saveparent,clientId,prectionerid,conditionid,editAppointId);
				}
			 
		}else{
			//save template
			  int saveparent =  emrDAO.saveParentOtEqTemplate(priscription,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),sessionadmissionid,discharge);
				session.setAttribute("saveparent", saveparent);
				ArrayList<Priscription>priscList = (ArrayList<Priscription>)session.getAttribute("oteqlist");
				for(Priscription priscription2 : priscList){
					int result  = emrDAO.saveoteqTemplate(priscription2,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),saveparent,clientId,prectionerid,conditionid);
				}
			
		}
		
		
	
		//delete priscription
		if(!editparentpriscid.equals("0")){
			
			if(session.getAttribute("deloteqList")!=null){
				ArrayList<String>delPriscList = (ArrayList<String>) session.getAttribute("deloteqList");
				for(String str : delPriscList){
					int del = emrDAO.delSelectedPriscription(str,editparentpriscid);
				}
			}
		}
		
		
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+priscadvoice+""); 
		
		
		//getPriscriptionData(clientId, prectionerid, conditionid, categoryid, mdicinenameid,saveparent);
		
	}catch(Exception e){
		e.printStackTrace();
	}
	finally {
		connection.close();
	}
	
	
	return null;
	
	
}

	
public String addprisc() throws Exception{
		Priscription priscription = new Priscription();
		
	/*	String categoryid = request.getParameter("categoryid");
		String mdicinenameid = request.getParameter("mdicinenameid");
		String mdicinenametxt = request.getParameter("mdicinenametxt");
		String priscdose = request.getParameter("priscdose");
		String priscfreq = request.getParameter("priscfreq");
		String priscdays = request.getParameter("priscdays");
		
		String prisctype =  request.getParameter("prisctype");
		String prisccode = request.getParameter("prisccode");
		String prisctotal = request.getParameter("prisctotal");

		priscription.setPrisctype(prisctype);
		priscription.setPrisccode(prisccode);
		priscription.setPrisctotal(prisctotal);*/
		//single value
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String locations= request.getParameter("locations");
		String prepay = request.getParameter("prepay");
		String postpay = request.getParameter("postpay");
		String otherpay = request.getParameter("otherpay");
		String priscautoid = request.getParameter("priscautoid");
		String priscdosenotes = request.getParameter("priscdosenotes");
		String followupsqty = request.getParameter("followupsqty");
		String followupstype = request.getParameter("followupstype");
		String priscadvoice = DateTimeUtils.isNull(request.getParameter("priscadvoice"));
		
		priscadvoice= new String(request.getParameter("priscadvoice").getBytes ("iso-8859-1"), "UTF-8");
		
		String english = request.getParameter("english");
		String regional  = request.getParameter("regional");
		String hindi = request.getParameter("hindi");
		String priscdays = request.getParameter("priscdays");
		String discharge = request.getParameter("discharge");
		String admission = request.getParameter("admission");
		String clientId = request.getParameter("clientId");
		String prectionerid = request.getParameter("prectionerid");
		String conditionid = request.getParameter("conditionid");
		
		String editparentpriscid = request.getParameter("editparentpriscid");
		String repeatdate=request.getParameter("repeatdate");
		
		String uptemplate = request.getParameter("uptemplate");
		String uptemplateid = request.getParameter("uptemplateid");
		String requestlocationid = request.getParameter("requestlocationid");
		String fromtreatmentgivenstatus=DateTimeUtils.numberCheck(request.getParameter("treatmentgivensatus"));
		priscription.setFromtreatmentgivenstatus(fromtreatmentgivenstatus);
		
		if(repeatdate!=null){
			
			if(repeatdate.equals("")){
				
				repeatdate="0";
			}
		} else {
			repeatdate="0";
		}
		
		
		priscription.setPrepay(prepay);
		priscription.setPostpay(postpay);
		priscription.setOtherpay(otherpay);
		priscription.setPriscdose(priscdosenotes);
		priscription.setFollowupsqty(followupsqty);
		priscription.setFollowupstype(followupstype);
		priscription.setPriscadvoice(priscadvoice);
		priscription.setEnglish(english);
		priscription.setRegional(regional);
		priscription.setHindi(hindi);
		priscription.setPriscdosenotes(priscdosenotes);
		
		
		
		
		Connection connection = null;
		try{
			
			
			connection = Connection_provider.getconnection();
			connection=DriverManager.getConnection(""+Constants.DB_HOST+":3306/"+loginInfo.getClinicUserid()+"?useUnicode=true&characterEncoding=UTF-8","pranams","6qxi5x&)~XBZ");
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
			String oldpractid = prectionerid;
			//check if doctor placed with machine
			UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
			UserProfile userProfile = userProfileDAO.getUserprofileDetails(Integer.parseInt(oldpractid));
			
			if(userProfile.getJobgroup().equals("4")){
				prectionerid = userProfile.getDoctor();
			}
			
			//Akash 24-10-2019
			String isfromemrdashb = request.getParameter("isfromemrdashb");
			isfromemrdashb = DateTimeUtils.isNull(isfromemrdashb);
			if(isfromemrdashb.equals("1")){
				boolean isinipd = emrDAO.checkIsAdmitedPatient(clientId);
				if(isinipd){
					locations ="ipd";
				}
			}
		
			priscription.setClientId(clientId);
			priscription.setPrectionerid(prectionerid);
			priscription.setConditionid(conditionid);
			
			String sessionadmissionid = (String)session.getAttribute("sessionadmissionid");
			System.out.println(editparentpriscid);
			
			
			String istemplate = request.getParameter("istemplate");
			if(istemplate==null){
				istemplate="0";
			}
			
			
			
			
			String mdtemplatename = request.getParameter("mdtemplatename");
			priscription.setTemplatename(mdtemplatename);
			int default_location = 0;
			if(requestlocationid!=null){
				if(requestlocationid.equals("") || requestlocationid.equals("0")){
					default_location = pharmacyDAO.getByDefaultPharmacyLocation();
				}else{
					default_location = Integer.parseInt(requestlocationid);
				}
			}else{
				default_location = pharmacyDAO.getByDefaultPharmacyLocation();
			}
					
			if(istemplate.equals("0")){
			if(editparentpriscid.equals("0")){
				int saveparent =  emrDAO.saveParentPriscription(priscription,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),sessionadmissionid,discharge,admission,loginInfo.getUserId(),locations,default_location);
				session.setAttribute("saveparent", saveparent);
				ArrayList<Priscription>priscList = (ArrayList<Priscription>)session.getAttribute("priscList");
				for(Priscription priscription2 : priscList){
					
					//30 June 2018 
					double qty=0;
					if(priscription2.getPriscqty()!=null){
						if(priscription2.getPriscqty().equals("")){
							if(priscription2.getPriscdose()!=null){
								if(!priscription2.getPriscdose().equals("") || (!priscription2.getPriscdose().equals("0"))){
									
									boolean found = false;
								    for(char c : priscription2.getPriscdose().toCharArray()){
								        if(Character.isDigit(c)){
								            found = true;
								        } else if(found){
								            // If we already found a digit before and this char is not a digit, stop looping
								            break;                
								        }
								    }
								    String s = priscription2.getPriscdays();
								    boolean checkalpha = s.matches(".*[a-zA-Z].*");
								    if(!checkalpha){
									    if(found){
									    	if(priscription2.getPriscdays()!=null){
									    		if(!priscription2.getPriscdays().equals("") || (!priscription2.getPriscdays().equals("0"))){
									    			String[] priscdoselist = priscription2.getPriscdose().split("-");
									    			for (String string : priscdoselist) {
														if(string!=null){
															if(!string.equals("")){
																boolean flag = false;
																if(string.matches("[0-9]+")){
																	flag = true;
																}
																if(!flag){
																	continue;
																}
																if(string.equals("0")){
																	continue;
																}
																if(string.equals("1/2")){
																	continue;
																}
																
																qty = qty + Integer.parseInt(priscription2.getPriscdays()) * Integer.parseInt(string);
															}
														}
													}
									    		}
									    	}
									    }
								    }
								}
							}
						}else{
							qty = Double.parseDouble(priscription2.getPriscqty());
						}
					}else{
						if(priscription2.getPriscdose()!=null){
							if(!priscription2.getPriscdose().equals("") || (!priscription2.getPriscdose().equals("0"))){
								
								boolean found = false;
							    for(char c : priscription2.getPriscdose().toCharArray()){
							        if(Character.isDigit(c)){
							            found = true;
							        } else if(found){
							            // If we already found a digit before and this char is not a digit, stop looping
							            break;                
							        }
							    }
							    
							    String s = priscription2.getPriscdays();
							    boolean checkalpha = s.matches(".*[a-zA-Z].*");
							    if(!checkalpha){
							        if(found){
								    	if(priscription2.getPriscdays()!=null){
								    		if(!priscription2.getPriscdays().equals("") || (!priscription2.getPriscdays().equals("0"))){
								    			String[] priscdoselist = priscription2.getPriscdose().split("-");
								    			for (String string : priscdoselist) {
													if(string!=null){
														if(!string.equals("")){
															boolean flag = false;
															if(string.matches("[0-9]+")){
																flag = true;
															}
															if(!flag){
																continue;
															}
															if(string.equals("0")){
																continue;
															}
															if(string.equals("1/2")){
																continue;
															}
															
															qty = qty + Integer.parseInt(priscription2.getPriscdays()) * Integer.parseInt(string);
														}
													}
												}
								    		}
								    	}
								    }
							    }
							}
						}
					}
					priscription2.setPriscqty(""+qty);
					
					int result  = emrDAO.savePriscription(priscription2,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),saveparent,clientId,prectionerid,conditionid);
				}
				
				Priscription priscriptionnew = emrDAO.getPriscriptionParentData(saveparent);
				String date = "";
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				date = dateFormat.format(cal.getTime());
				priscriptionnew.setDate(date);
				priscriptionnew.setUserid(loginInfo.getUserId());
				int directtransfer =1;
				if(!loginInfo.isDirect_prisc()){
					directtransfer = 0;
				}
				int newparentid = emrDAO.saveParentPriscNew(priscriptionnew,"",""+saveparent,""+default_location,directtransfer);
				ArrayList<Priscription> arrayList= emrDAO.getPriscriptionChildData(""+saveparent,0);
				for(Priscription priscription2 : arrayList){
					 priscription.setClientId(priscription2.getClientId());
					String mdrequestqty = priscription2.getPriscqty();
					String mdname = priscription2.getMdicinenametxt();
					String mdid = priscription2.getMdicinenameid();
					String childid =""+priscription2.getId();
					String newchild = childid;
					priscription.setMdicinenameid(mdid);
					priscription.setMdicinenametxt(mdname);
					int res = emrDAO.saveChildPriscNew(priscription2,mdrequestqty,""+saveparent,childid,newparentid);
				}
				/*if(priscriptionnew.getIpdid()!=null){
					if(priscriptionnew.getIpdid().equals("0")){
						String date = "";
						DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Calendar cal = Calendar.getInstance();
						date = dateFormat.format(cal.getTime());
						priscriptionnew.setDate(date);
						priscriptionnew.setUserid(loginInfo.getUserId());
						
						int newparentid = emrDAO.saveParentPriscNew(priscriptionnew,"",""+saveparent);
						ArrayList<Priscription> arrayList= emrDAO.getPriscriptionChildData(""+saveparent);
						for(Priscription priscription2 : arrayList){
							 priscription.setClientId(priscription2.getClientId());
							String mdrequestqty = priscription2.getPriscqty();
							String mdname = priscription2.getMdicinenametxt();
							String mdid = priscription2.getMdicinenameid();
							String childid =""+priscription2.getId();
							String newchild = childid;
							priscription.setMdicinenameid(mdid);
							priscription.setMdicinenametxt(mdname);
							int res = emrDAO.saveChildPriscNew(priscription2,mdrequestqty,""+saveparent,childid,newparentid);
						}
					}
				}else{
					String date = "";
					DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Calendar cal = Calendar.getInstance();
					date = dateFormat.format(cal.getTime());
					priscriptionnew.setDate(date);
					priscriptionnew.setUserid(loginInfo.getUserId());
					
					int newparentid = emrDAO.saveParentPriscNew(priscription,"",""+saveparent);
					ArrayList<Priscription> arrayList= emrDAO.getPriscriptionChildData(""+saveparent);
					for(Priscription priscription2 : arrayList){
						
						String mdrequestqty = priscription.getPriscqty();
						String mdname = priscription2.getMdicinenametxt();
						String mdid = priscription2.getMdicinenameid();
						String childid =""+priscription2.getId();
						String newchild = childid;
						priscription.setMdicinenameid(mdid);
						priscription.setMdicinenametxt(mdname);
						int res = emrDAO.saveChildPriscNew(priscription,mdrequestqty,""+saveparent,childid,newparentid);
					}
				}*/
			}else{
				
				 if(!repeatdate.equals("0")){
					 
					   int saveparent =  emrDAO.saveParentPriscription(priscription,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),sessionadmissionid,discharge,admission,loginInfo.getUserId(),locations,default_location);
						session.setAttribute("saveparent", saveparent);
						ArrayList<Priscription>priscList = (ArrayList<Priscription>)session.getAttribute("priscList");
						for(Priscription priscription2 : priscList){
							//30 June 2018 
							double qty=0;
							if(priscription2.getPriscqty()!=null){
								if(priscription2.getPriscqty().equals("")){
									if(priscription2.getPriscdose()!=null){
										if(!priscription2.getPriscdose().equals("") || (!priscription2.getPriscdose().equals("0"))){
											
											boolean found = false;
										    for(char c : priscription2.getPriscdose().toCharArray()){
										        if(Character.isDigit(c)){
										            found = true;
										        } else if(found){
										            // If we already found a digit before and this char is not a digit, stop looping
										            break;                
										        }
										    }
										    String s = priscription2.getPriscdays();
										    boolean checkalpha = s.matches(".*[a-zA-Z].*");
										    if(!checkalpha){
											    if(found){
											    	if(priscription2.getPriscdays()!=null){
											    		if(!priscription2.getPriscdays().equals("") || (!priscription2.getPriscdays().equals("0"))){
											    			String[] priscdoselist = priscription2.getPriscdose().split("-");
											    			for (String string : priscdoselist) {
																if(string!=null){
																	if(!string.equals("")){
																		boolean flag = false;
																		if(string.matches("[0-9]+")){
																			flag = true;
																		}
																		if(!flag){
																			continue;
																		}
																		if(string.equals("0")){
																			continue;
																		}
																		if(string.equals("1/2")){
																			continue;
																		}
																		
																		qty = qty + Integer.parseInt(priscription2.getPriscdays()) * Integer.parseInt(string);
																	}
																}
															}
											    		}
											    	}
											    }
										    }
										}
									}
								}else{
									qty = Double.parseDouble(priscription2.getPriscqty());
								}
							}else{
								if(priscription2.getPriscdose()!=null){
									if(!priscription2.getPriscdose().equals("") || (!priscription2.getPriscdose().equals("0"))){
										
										boolean found = false;
									    for(char c : priscription2.getPriscdose().toCharArray()){
									        if(Character.isDigit(c)){
									            found = true;
									        } else if(found){
									            // If we already found a digit before and this char is not a digit, stop looping
									            break;                
									        }
									    }
									    if(found){
									    	if(priscription2.getPriscdays()!=null){
									    		if(!priscription2.getPriscdays().equals("") || (!priscription2.getPriscdays().equals("0"))){
									    			String[] priscdoselist = priscription2.getPriscdose().split("-");
									    			for (String string : priscdoselist) {
														if(string!=null){
															if(!string.equals("")){
																boolean flag = false;
																if(string.matches("[0-9]+")){
																	flag = true;
																}
																if(!flag){
																	continue;
																}
																if(string.equals("0")){
																	continue;
																}
																if(string.equals("1/2")){
																	continue;
																}
																
																qty = qty + Integer.parseInt(priscription2.getPriscdays()) * Integer.parseInt(string);
															}
														}
													}
									    		}
									    	}
									    }
									}
								}
							}
							priscription2.setPriscqty(""+qty);
							int result  = emrDAO.savePriscription(priscription2,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),saveparent,clientId,prectionerid,conditionid);
						}
						
						//Akash 10-10-2019 to send direct request to pharmacy
						Priscription priscriptionnew = emrDAO.getPriscriptionParentData(saveparent);
						String date = "";
						DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Calendar cal = Calendar.getInstance();
						date = dateFormat.format(cal.getTime());
						priscriptionnew.setDate(date);
						priscriptionnew.setUserid(loginInfo.getUserId());
						int directtransfer =1;
						if(!loginInfo.isDirect_prisc()){
							directtransfer = 0;
						}
						int newparentid = emrDAO.saveParentPriscNew(priscriptionnew,"",""+saveparent,""+default_location,directtransfer);
						ArrayList<Priscription> arrayList= emrDAO.getPriscriptionChildData(""+saveparent,0);
						for(Priscription priscription2 : arrayList){
							 priscription.setClientId(priscription2.getClientId());
							String mdrequestqty = priscription2.getPriscqty();
							String mdname = priscription2.getMdicinenametxt();
							String mdid = priscription2.getMdicinenameid();
							String childid =""+priscription2.getId();
							String newchild = childid;
							priscription.setMdicinenameid(mdid);
							priscription.setMdicinenametxt(mdname);
							int res = emrDAO.saveChildPriscNew(priscription2,mdrequestqty,""+saveparent,childid,newparentid);
						}
					 
				 } else {
					 ArrayList<Priscription>priscList = (ArrayList<Priscription>)session.getAttribute("priscList");
					 session.setAttribute("saveparent", Integer.parseInt(editparentpriscid));	
					 for(Priscription priscription2 : priscList){
							boolean mdicineExixst = emrDAO.checkMdicineExist(priscription2.getMdicinenameid(),editparentpriscid);
							if(mdicineExixst){
								int priscid = emrDAO.getSelectedPriscId(priscription2.getMdicinenameid(),editparentpriscid);
								int update = emrDAO.updateExixstMedicine(priscription2,priscid);
							}else{
								//30 June 2018 
								double qty=0;
								if(priscription2.getPriscqty()!=null){
									if(priscription2.getPriscqty().equals("")){
										if(priscription2.getPriscdose()!=null){
											if(!priscription2.getPriscdose().equals("") || (!priscription2.getPriscdose().equals("0"))){
												
												boolean found = false;
											    for(char c : priscription2.getPriscdose().toCharArray()){
											        if(Character.isDigit(c)){
											            found = true;
											        } else if(found){
											            // If we already found a digit before and this char is not a digit, stop looping
											            break;                
											        }
											    }
											    if(found){
											    	if(priscription2.getPriscdays()!=null){
											    		if(!priscription2.getPriscdays().equals("") || (!priscription2.getPriscdays().equals("0"))){
											    			String[] priscdoselist = priscription2.getPriscdose().split("-");
											    			for (String string : priscdoselist) {
																if(string!=null){
																	if(!string.equals("")){
																		boolean flag = false;
																		if(string.matches("[0-9]+")){
																			flag = true;
																		}
																		if(!flag){
																			continue;
																		}
																		if(string.equals("0")){
																			continue;
																		}
																		if(string.equals("1/2")){
																			continue;
																		}
																		
																		qty = qty + Integer.parseInt(priscription2.getPriscdays()) * Integer.parseInt(string);
																	}
																}
															}
											    		}
											    	}
											    }
											}
										}
									}else{
										qty = Double.parseDouble(priscription2.getPriscqty());
									}
								}else{
									if(priscription2.getPriscdose()!=null){
										if(!priscription2.getPriscdose().equals("") || (!priscription2.getPriscdose().equals("0"))){
											
											boolean found = false;
										    for(char c : priscription2.getPriscdose().toCharArray()){
										        if(Character.isDigit(c)){
										            found = true;
										        } else if(found){
										            // If we already found a digit before and this char is not a digit, stop looping
										            break;                
										        }
										    }
										    if(found){
										    	if(priscription2.getPriscdays()!=null){
										    		if(!priscription2.getPriscdays().equals("") || (!priscription2.getPriscdays().equals("0"))){
										    			String[] priscdoselist = priscription2.getPriscdose().split("-");
										    			for (String string : priscdoselist) {
															if(string!=null){
																if(!string.equals("")){
																	boolean flag = false;
																	if(string.matches("[0-9]+")){
																		flag = true;
																	}
																	if(!flag){
																		continue;
																	}
																	if(string.equals("0")){
																		continue;
																	}
																	if(string.equals("1/2")){
																		continue;
																	}
																	
																	qty = qty + Integer.parseInt(priscription2.getPriscdays()) * Integer.parseInt(string);
																}
															}
														}
										    		}
										    	}
										    }
										}
									}
								}
								priscription2.setPriscqty(""+qty);
								
								int result  = emrDAO.savePriscription(priscription2,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),Integer.parseInt(editparentpriscid),clientId,prectionerid,conditionid);
							}
							
						}
				 }
				
				
			}
			}else{
				
				if(uptemplate==null){
					uptemplate="0";
				}
				if(uptemplate.equals("1")){
					//28 April 2018 Akash 
					//update template 
					int saveparent = Integer.parseInt(uptemplateid);
					int res = emrDAO.deleteChildPriscTempData(saveparent);
					ArrayList<Priscription>priscList = (ArrayList<Priscription>)session.getAttribute("priscList");
					for(Priscription priscription2 : priscList){
						//30 June 2018 
						double qty=0;
						if(priscription2.getPriscqty()!=null){
							if(priscription2.getPriscqty().equals("")){
								if(priscription2.getPriscdose()!=null){
									if(!priscription2.getPriscdose().equals("") || (!priscription2.getPriscdose().equals("0"))){
										
										boolean found = false;
									    for(char c : priscription2.getPriscdose().toCharArray()){
									        if(Character.isDigit(c)){
									            found = true;
									        } else if(found){
									            // If we already found a digit before and this char is not a digit, stop looping
									            break;                
									        }
									    }
									    if(found){
									    	if(priscription2.getPriscdays()!=null){
									    		if(!priscription2.getPriscdays().equals("") || (!priscription2.getPriscdays().equals("0"))){
									    			String[] priscdoselist = priscription2.getPriscdose().split("-");
									    			for (String string : priscdoselist) {
														if(string!=null){
															if(!string.equals("")){
																boolean flag = false;
																if(string.matches("[0-9]+")){
																	flag = true;
																}
																if(!flag){
																	continue;
																}
																if(string.equals("0")){
																	continue;
																}
																if(string.equals("1/2")){
																	continue;
																}
																
																qty = qty + Integer.parseInt(priscription2.getPriscdays()) * Integer.parseInt(string);
															}
														}
													}
									    		}
									    	}
									    }
									}
								}
							}else{
								qty = Double.parseDouble(priscription2.getPriscqty());
							}
						}else{
							if(priscription2.getPriscdose()!=null){
								if(!priscription2.getPriscdose().equals("") || (!priscription2.getPriscdose().equals("0"))){
									
									boolean found = false;
								    for(char c : priscription2.getPriscdose().toCharArray()){
								        if(Character.isDigit(c)){
								            found = true;
								        } else if(found){
								            // If we already found a digit before and this char is not a digit, stop looping
								            break;                
								        }
								    }
								    if(found){
								    	if(priscription2.getPriscdays()!=null){
								    		if(!priscription2.getPriscdays().equals("") || (!priscription2.getPriscdays().equals("0"))){
								    			String[] priscdoselist = priscription2.getPriscdose().split("-");
								    			for (String string : priscdoselist) {
													if(string!=null){
														if(!string.equals("")){
															boolean flag = false;
															if(string.matches("[0-9]+")){
																flag = true;
															}
															if(!flag){
																continue;
															}
															if(string.equals("0")){
																continue;
															}
															if(string.equals("1/2")){
																continue;
															}
															
															qty = qty + Integer.parseInt(priscription2.getPriscdays()) * Integer.parseInt(string);
														}
													}
												}
								    		}
								    	}
								    }
								}
							}
						}
						priscription2.setPriscqty(""+qty);
						
						int result  = emrDAO.savePriscriptionTemplate(priscription2,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),saveparent,clientId,prectionerid,conditionid);
					}
				}else{
					//save template
					  int saveparent =  emrDAO.saveParentPriscriptionTemplate(priscription,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),sessionadmissionid,discharge);
						session.setAttribute("saveparent", saveparent);
						ArrayList<Priscription>priscList = (ArrayList<Priscription>)session.getAttribute("priscList");
						for(Priscription priscription2 : priscList){
							
							//17 July 2018 
							double qty=0;
							if(priscription2.getPriscqty()!=null){
								if(priscription2.getPriscqty().equals("")){
									if(priscription2.getPriscdose()!=null){
										if(!priscription2.getPriscdose().equals("") || (!priscription2.getPriscdose().equals("0"))){
											
											boolean found = false;
										    for(char c : priscription2.getPriscdose().toCharArray()){
										        if(Character.isDigit(c)){
										            found = true;
										        } else if(found){
										            // If we already found a digit before and this char is not a digit, stop looping
										            break;                
										        }
										    }
										    if(found){
										    	if(priscription2.getPriscdays()!=null){
										    		if(!priscription2.getPriscdays().equals("") || (!priscription2.getPriscdays().equals("0"))){
										    			String[] priscdoselist = priscription2.getPriscdose().split("-");
										    			for (String string : priscdoselist) {
															if(string!=null){
																if(!string.equals("")){
																	boolean flag = false;
																	if(string.matches("[0-9]+")){
																		flag = true;
																	}
																	if(!flag){
																		continue;
																	}
																	if(string.equals("0")){
																		continue;
																	}
																	if(string.equals("1/2")){
																		continue;
																	}
																	
																	qty = qty + Integer.parseInt(priscription2.getPriscdays()) * Integer.parseInt(string);
																}
															}
														}
										    		}
										    	}
										    }
										}
									}
								}else{
									qty = Double.parseDouble(priscription2.getPriscqty());
								}
							}else{
								if(priscription2.getPriscdose()!=null){
									if(!priscription2.getPriscdose().equals("") || (!priscription2.getPriscdose().equals("0"))){
										
										boolean found = false;
									    for(char c : priscription2.getPriscdose().toCharArray()){
									        if(Character.isDigit(c)){
									            found = true;
									        } else if(found){
									            // If we already found a digit before and this char is not a digit, stop looping
									            break;                
									        }
									    }
									    if(found){
									    	if(priscription2.getPriscdays()!=null){
									    		if(!priscription2.getPriscdays().equals("") || (!priscription2.getPriscdays().equals("0"))){
									    			String[] priscdoselist = priscription2.getPriscdose().split("-");
									    			for (String string : priscdoselist) {
														if(string!=null){
															if(!string.equals("")){
																boolean flag = false;
																if(string.matches("[0-9]+")){
																	flag = true;
																}
																if(!flag){
																	continue;
																}
																if(string.equals("0")){
																	continue;
																}
																if(string.equals("1/2")){
																	continue;
																}
																
																qty = qty + Integer.parseInt(priscription2.getPriscdays()) * Integer.parseInt(string);
															}
														}
													}
									    		}
									    	}
									    }
									}
								}
							}
							priscription2.setPriscqty(""+qty);
							
							
							int result  = emrDAO.savePriscriptionTemplate(priscription2,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),saveparent,clientId,prectionerid,conditionid);
						}
				}
			}
			
			
		
			//delete priscription
			if(!editparentpriscid.equals("0")){
				
				if(session.getAttribute("delPriscList")!=null){
					ArrayList<String>delPriscList = (ArrayList<String>) session.getAttribute("delPriscList");
					for(String str : delPriscList){
						int del = emrDAO.delSelectedPriscription(str,editparentpriscid);
					}
				}
			}
			//session.setAttribute("priscList", null);
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+priscadvoice+""); 
			
			
			//getPriscriptionData(clientId, prectionerid, conditionid, categoryid, mdicinenameid,saveparent);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		finally {
			connection.close();
		}
		
		
		return null;
	}


	public String editprisc(){
		
		Connection connection = null;
		try{
			
			session.removeAttribute("delPriscList");
			
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			String selectedid = request.getParameter("selectedid");
			
			Priscription priscription = emrDAO.getEditPriscription(selectedid);
			
			String str = priscription.getCategoryid() + "~" + priscription.getMdicinenameid() +  "~" + priscription.getPriscdose() +  "~" + priscription.getPriscfreq() +  "~" + priscription.getPriscdays() + "~" + priscription.getPriscdurationtype()+"~"+priscription.getDosenotes();
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+str+""); 
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}


	public String showprisc(){
		
		String clientId = request.getParameter("clientId");
		String prectionerid = request.getParameter("prectionerid");
		String conditionid = request.getParameter("conditionid");
		
		//getPriscriptionData(clientId, prectionerid, conditionid, "", "");
		
	
		
		return  null;
	}
	
	
	public String viewallclientprisc(){
		
		String clientId = request.getParameter("clientId");
		String prectionerid = request.getParameter("prectionerid");
		String conditionid = request.getParameter("conditionid");
		
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			ArrayList<Priscription>parentPriscList = emrDAO.getParentPriscList(clientId,prectionerid,conditionid);
			
			StringBuffer str = new StringBuffer();
			
			for(Priscription priscription : parentPriscList){
				str.append("<div class='row'>");
				str.append("<span><p class='pdatpree'>"+priscription.getDate()+" <a href='#' onclick='editparentprisc("+priscription.getId()+")' title='Edit Prescription' class='editpricon'><i class='fa fa-edit'></i></a> ");
				if(priscription.getDeliverystatus()>0){
					str.append("<a href='#'  title='Can't delete' class='editpricon'><i class='fa fa-ban'></i></a>");
				}else{
					str.append("<a href='#' onclick='delparentprisc("+priscription.getId()+")' title='Delete Prescription' class='editpricon'><i class='fa fa-trash-o'></i></a>");
				}
				str.append("<a href='#' onclick='printParentPrisc("+priscription.getId()+")' title='Print Prescription' class='editpricon'><i class='fa fa-print'></i></a></p> </span>");
				str.append("</div>");
				
			}
			
			/*ArrayList<Priscription>priscriptionList = emrDAO.getAllPriscList(clientId,practitionerID,conditionid);
			
			
			for(Priscription priscription : priscriptionList){
				str.append("<tr>");
				str.append("<td>"+priscription.getDate()+"</td>");
				str.append("<td>"+priscription.getMdicinenametxt()+"</td>");
				str.append("<td>"+priscription.getPriscdose()+"</td>");
				str.append("<td>"+priscription.getPriscfreq()+"</td>");
				str.append("<td>"+priscription.getPriscdays()+"</td>");
				
				str.append("</tr>");
				
				
			}*/
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+str.toString()+""); 
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	
	public String vieemrwallclientprisc(){
		
		String clientId = request.getParameter("clientId");
		String prectionerid = request.getParameter("prectionerid");
		String conditionid = request.getParameter("conditionid");
		
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			ArrayList<Priscription>parentPriscList = emrDAO.getParentPriscList(clientId,prectionerid,conditionid);
			
			StringBuffer str = new StringBuffer();
			str.append("<table class='table'>");
			str.append("<thead class='thead-dark'>");
			str.append("<tr>");
			str.append("<th class='emrinvestigationfont'>Sr.No</th><th class='emrinvestigationfont'>Date</th><th class='emrinvestigationfont'>Print</th>");
			str.append("</tr>");
			str.append("</thead>");
			str.append("<tbody>");
			int i=0;
			for(Priscription priscription : parentPriscList){
				str.append("<tr>");
				str.append("<td>"+(++i)+"</td>");			
				str.append("<td>"+priscription.getDate()+"</td>");
				str.append("<td><a href='#' onclick='printParentPrisc("+priscription.getId()+")' title='Print Prescription'><i class='fa fa-print emrinvestigationfontprint'></i></a></td>");
				str.append("</tr>");
				
			}
			str.append("</tbody>");
			str.append("</table>");
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+str.toString()+""); 
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	
	public String viewallprisc(){
		String clientId = request.getParameter("clientId");
		String prectionerid = request.getParameter("prectionerid");
		String conditionid = request.getParameter("conditionid");
		
		Connection connection = null;
		try{
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			connection = Connection_provider.getconnection();
			connection=DriverManager.getConnection(""+Constants.DB_HOST+":3306/"+loginInfo.getClinicUserid()+"?useUnicode=true&characterEncoding=UTF-8","pranams","6qxi5x&)~XBZ");
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			String date = DateTimeUtils.getDashboardTodayDate(loginInfo.getTimeZone());
			//ArrayList<Priscription>priscriptionList = emrDAO.getPractitionerList(clientId,prectionerid,conditionid,"","",date);
			
			
			StringBuffer str = new StringBuffer();
			if(loginInfo.getOutoprisc()==0){
			ArrayList<Priscription>priscList = (ArrayList<Priscription>)session.getAttribute("priscList");
			
			
			for(Priscription priscription : priscList){
				str.append("<tr>");
				/*str.append("<td>"+priscription.getPrisctype()+"</td>");*/
				str.append("<td>"+priscription.getMdicinenametxt()+"</td>");
				/*String dose = emrDAO.getregionalText(priscription.getPriscdose());
				str.append("<td>"+dose+"</td>");*/

				String regional = emrDAO.getregionalText(priscription.getPriscdose());
				str.append("<td>"+regional.toString()+"</td>");
				//str.append("<td>"+priscription.getPriscfreq()+"</td>");
				if(!priscription.getPriscdays().equals("")){
				str.append("<td>"+priscription.getPriscdays()+" days "+priscription.getPriscdurationtype()+"</td>");
				}else{
					str.append("<td>"+priscription.getPriscdays()+"  "+priscription.getPriscdurationtype()+"</td>");
				}
				/*str.append("<td>"+priscription.getPrisctotal()+"</td>");*/
				str.append("<td>"+priscription.getDosenotes().toString()+"</td>");
				str.append("<td>"+priscription.getPrisctimename()+"</td>");
				str.append("<td>"+priscription.getPriscindivisualremark()+"</td>");
				str.append("</tr>");
				
				
			}
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+str.toString()+""); 
			}else{
				int saveparent = (Integer)session.getAttribute("savetemplateparent");
				ArrayList<Priscription>priscList = emrDAO.getPrintPriscList(Integer.toString(saveparent),loginInfo);
			
				int t = 0;
				for(Priscription priscription : priscList) {
					t++;
					str.append("<tr>");
					str.append("<td>"+t+"</td>");
					str.append("<td>"+priscription.getMdicinenametxt()+"</td>");
					
					str.append("<td>"+priscription.getDddose()+" "+priscription.getUnitextension()+"</td>");
					str.append("<td>"+priscription.getRegional()+"</td>");
					
					str.append("<td>"+priscription.getPriscdays()+"</td>");
					str.append("<td>"+priscription.getPriscqty()+"</td>");
					str.append("<td>"+priscription.getDosenotes()+"</td>");
					str.append("<td>"+priscription.getPrisctimename()+"</td>");
					str.append("<td>"+priscription.getPriscindivisualremark()+"</td>");

					str.append("</tr>");
				}

				
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write(""+str.toString()+""); 
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	
public void getNimaiPriscriptionTemplateData(){
		
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			MasterDAO masterDAO = new JDBCMasterDAO(connection);
			
			String date = DateTimeUtils.getDashboardTodayDate(loginInfo.getTimeZone());
			
			ArrayList<Master>dosageList = emrDAO.getDosageList();
			ArrayList<Priscription> medicinetimelist = emrDAO.getMedicineTimeList(); 

			ArrayList<Master>dosagenoteList = masterDAO.getDosageNoteList();
			
			ArrayList<Master>nimaidoselist = masterDAO.getnimaidoselistt();
			ArrayList<Master>nimaiqtylist = masterDAO.getnimaiqtylist();
			ArrayList<Master>nimairemarklist = masterDAO.getnimairemarlist();
			ArrayList<Master> priscUnitList= masterDAO.getPriscUnitList();
			
		/*	ArrayList<Priscription>priscriptionList = emrDAO.getPractitionerList(clientid,practionerid,conditionid,categoryid,medicineid,date);
			
			Priscription parentdata = emrDAO.getPriscriptionParentData(parentid);*/
			
			StringBuffer str = new StringBuffer();
			StringBuffer strdoage = new StringBuffer();
			StringBuffer strdays = new StringBuffer();
			
			StringBuffer strreqnote = new StringBuffer();
			StringBuffer strroute = new StringBuffer();
			StringBuffer strremark = new StringBuffer();
			StringBuffer struom = new StringBuffer();
			
			
			StringBuffer strdddose = new StringBuffer();
			StringBuffer strqty = new StringBuffer();
			
			ArrayList<Priscription>priscriptionList = (ArrayList<Priscription>)session.getAttribute("priscList");
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.DATE, -7); 
			String curdate = dateFormat.format(cal.getTime());
			
			int i = 0;
			 str.append("<input type='hidden' name='listsizeid' id='listsizeid' value='"+priscriptionList.size()+"'>");
			for(Priscription priscription : priscriptionList){
				strdoage = new StringBuffer();
			    strdays = new StringBuffer();
			    
			    strdddose = new StringBuffer();
			    strreqnote = new StringBuffer();
			    strqty = new StringBuffer();
			    strroute = new StringBuffer();
			    strremark = new StringBuffer();
			    struom = new StringBuffer();
				
			   
			    str.append("<tr class='nimaicase' id='"+i+"'>");
				/*str.append("<td>"+priscription.getDate()+"</td>");*/
			    str.append("<td><input type='text' readonly='true' id='srno"+i+"' value='"+priscription.getSrno()+"' onchange='' class='form-control casenn'></td>");
			   // str.append("<input id='mdicinenameid"+i+"' type='hidden' class='akash' value='" + priscription.getMdicinenameid() + "' class='form-control' />");
			    
			    str.append("<td class='hidden' id='mdicinenameid"+i+"'>"+priscription.getMdicinenameid()+"</td>");
			    str.append("<input type='hidden' class='akashclasss' value='" + i + "' class='form-control' />");

				/*str.append("<td>"+priscription.getPrisctype()+"</td>");*/
				str.append("<td style='cursor:pointer' onclick='showprisceditmdcinenameindb("+i+","+priscription.getMdicinenameid()+")' id='mdcinenametxt"+i+"'>"+priscription.getMdicinenametxt()+"</td>");
				
				str.append("<td class='hidden' id='priscfreq"+i+"'>"+priscription.getPriscfreq()+"</td>");
				//str.append("<td>"+priscription.getMasterdose()+"</td>");
				String uom = emrDAO.getMedicineUom(priscription.getMdicinenameid());
				
				if(uom==null){
					uom="";
				}
				//str.append("<td><input type='number' id='dddose"+i+"' value='"+priscription.getDddose()+"'  class='form-control'>"+uom+"</td>");
				
				if(loginInfo.getOutoprisc()==1){
					strdddose.append("<select class='form-control' onchange='' name='dddose"+i+"' id='dddose"+i+"'>");
					strdddose.append("<option value='"+priscription.getDddose()+"'>"+priscription.getDddose()+"</option>");
						for(Master master : nimaidoselist){
							strdddose.append("<option value='"+master.getName()+"'>"+master.getName()+"</option>");
						}
						strdddose.append("</select>");
						
						//uom list
						strdddose.append("<select class='form-control' onchange='' name='uom"+i+"' id='uom"+i+"'>");
						strdddose.append("<option value='"+priscription.getUnitextension()+"'>"+priscription.getUnitextension()+"</option>");
							for(Master master : priscUnitList){
								strdddose.append("<option value='"+master.getName()+"'>"+master.getName()+"</option>");
							}
							strdddose.append("</select>");
						
					str.append("<td>"+strdddose.toString()+"</td>");
					//str.append("<td><input type='number' id='dddose"+i+"' value='"+priscription.getDddose()+"'  class='form-control'>"+priscription.getUom()+"</td>");
				}else{
					str.append("<td><input type='hidden' id='dddose"+i+"' value='"+priscription.getDddose()+"'  class='form-control'>"+priscription.getUom()+"</td>");
					str.append("<input type='hidden' id='uom"+i+"' value='"+priscription.getUom()+"'  class='form-control'>");
					if(priscription.getUnit()==null){
						str.append("<td>");
					}else{
						str.append("<td>"+priscription.getUnit()+"");
					}
					
					if(priscription.getUnitextension()!=null){
						if(!priscription.getUnitextension().equals("0")){
							str.append(""+priscription.getUnitextension()+"");
						}
					}
					str.append("</td>");
				}
				
				
				strdoage.append("<select class='form-control' onchange='' name='dosage"+i+"' id='dosage"+i+"'>");
				strdoage.append("<option value='"+priscription.getPriscdose()+"'>"+priscription.getPriscdose()+"</option>");
					for(Master master : dosageList){
						strdoage.append("<option value='"+master.getName()+"'>"+master.getName()+"</option>");
					}
				strdoage.append("</select>");
				str.append("<td>"+strdoage.toString()+"</td>");
				
				
				//days
				strdays.append("<select class='form-control' onchange='' name='days"+i+"' id='days"+i+"'>");
				strdays.append("<option value='"+priscription.getPriscdays()+"'>"+priscription.getPriscdays()+"</option>");
				for(int p=1;p<=90;p++){
					strdays.append("<option value='"+p+"'>"+p+"</option>");
					}
				strdays.append("</select>");
				str.append("<td><s>"+strdays.toString()+"</td>");
				
				//frequency note
				//str.append("<td id='prisctimename"+i+"'>"+priscription.getPrisctimename()+"</td>");
				
				strreqnote.append("<select class='form-control' onchange='changepriscgivendddose("+i+",this.value)' name='prisctimename"+i+"' id='prisctimename"+i+"'>");
				strreqnote.append("<option value='"+priscription.getPrisctimename()+"'>"+priscription.getPrisctimename()+"</option>");
					for(Priscription master : medicinetimelist){
						strreqnote.append("<option value='"+master.getPriscriptiontime()+"'>"+master.getPriscriptiontime()+"</option>");
					}
					strreqnote.append("</select>");
				str.append("<td>"+strreqnote.toString()+"</td>");

				
				
				//QTY
				//str.append("<td><input type='number' id='drgivenqty"+i+"' value='"+priscription.getPriscqty()+"'  class='form-control'></td>");
				strqty.append("<select class='form-control' onchange='' name='drgivenqty"+i+"' id='drgivenqty"+i+"'>");
				strqty.append("<option value='"+priscription.getPriscqty()+"'>"+priscription.getPriscqty()+"</option>");
					for(Master master : nimaiqtylist){
						strqty.append("<option value='"+master.getName()+"'>"+master.getName()+"</option>");
					}
					strqty.append("</select>");
				str.append("<td>"+strqty.toString()+"</td>");
				
				//route
				//str.append("<td id='dosenotes"+i+"'>"+priscription.getDosenotes()+"</td>");
				strroute.append("<select class='form-control' onchange='' name='dosenotes"+i+"' id='dosenotes"+i+"'>");
				strroute.append("<option value='"+priscription.getDosenotes()+"'>"+priscription.getDosenotes()+"</option>");
					for(Master master : dosagenoteList){
						strroute.append("<option value='"+master.getName()+"'>"+master.getName()+"</option>");
					}
					strroute.append("</select>");
				str.append("<td>"+strroute.toString()+"</td>");

				
				//remark
				String rname = emrDAO.getrname(priscription.getPriscindivisualremark());
				//str.append("<td>"+priscription.getPriscindivisualremark()+"</td>");
				strremark.append("<select class='form-control' onchange='' name='priscindivisualremark"+i+"' id='priscindivisualremark"+i+"'>");
				strremark.append("<option value='"+priscription.getPriscindivisualremark()+"'>"+rname+"</option>");
					for(Master master : nimairemarklist){
						strremark.append("<option value='"+master.getId()+"'>"+master.getName()+"</option>");
					}
					strremark.append("</select>");
				str.append("<td>"+strremark.toString()+"</td>");

				
				//delete
				str.append("<td><a><i onclick='deletepriscdata("+i+")' class='fa fa-trash-o' ></i></a></td>");
			
								
				str.append("</tr>");
				
				
				i++;
			}
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+str.toString()+""); 
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
public void getPriscriptionTemplateData(){
		
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			String date = DateTimeUtils.getDashboardTodayDate(loginInfo.getTimeZone());
			
			ArrayList<Master>dosageList = emrDAO.getDosageList();
			
		/*	ArrayList<Priscription>priscriptionList = emrDAO.getPractitionerList(clientid,practionerid,conditionid,categoryid,medicineid,date);
			
			Priscription parentdata = emrDAO.getPriscriptionParentData(parentid);*/
			
			StringBuffer str = new StringBuffer();
			StringBuffer strdoage = new StringBuffer();
			StringBuffer strdays = new StringBuffer();
			
			ArrayList<Priscription>priscriptionList = (ArrayList<Priscription>)session.getAttribute("priscList");
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.DATE, -7); 
			String curdate = dateFormat.format(cal.getTime());
			
			int i = 0;
			 str.append("<input type='hidden' name='listsizeid' id='listsizeid' value='"+priscriptionList.size()+"'>");
			for(Priscription priscription : priscriptionList){
				strdoage = new StringBuffer();
			    strdays = new StringBuffer();
				
			   
			    str.append("<tr>");
				/*str.append("<td>"+priscription.getDate()+"</td>");*/
			    str.append("<td><input type='text' id='srno"+i+"' value='"+priscription.getSrno()+"' onchange='changepriscgivensrno("+i+",this.value)' class='form-control'></td>");
			   // str.append("<input id='mdicinenameid"+i+"' type='hidden' class='akash' value='" + priscription.getMdicinenameid() + "' class='form-control' />");
			    
			    str.append("<td class='hidden' id='mdicinenameid"+i+"'>"+priscription.getMdicinenameid()+"</td>");
			    str.append("<input type='hidden' class='akashclasss' value='" + i + "' class='form-control' />");
				/*str.append("<td>"+priscription.getPrisctype()+"</td>");*/
				str.append("<td style='cursor:pointer' onclick='showprisceditmdcinenameindb("+i+","+priscription.getMdicinenameid()+")' id='mdcinenametxt"+i+"'>"+priscription.getMdicinenametxt()+"</td>");
				str.append("<td class='hidden' id='priscfreq"+i+"'>"+priscription.getPriscfreq()+"</td>");
				//str.append("<td>"+priscription.getMasterdose()+"</td>");
				String uom = emrDAO.getMedicineUom(priscription.getMdicinenameid());
				
				if(uom==null){
					uom="";
				}
				//str.append("<td><input type='number' id='dddose"+i+"' value='"+priscription.getDddose()+"'  class='form-control'>"+uom+"</td>");
				
				if(loginInfo.getOutoprisc()==1){
					str.append("<td><input type='number' id='dddose"+i+"' value='"+priscription.getDddose()+"'  class='form-control'>"+priscription.getUom()+"</td>");
				}else{
					str.append("<td class='hidden'><input type='hidden' id='dddose"+i+"' value='"+priscription.getDddose()+"'  class='form-control'>"+priscription.getUom()+"</td>");
					if(priscription.getUnit()==null){
						str.append("<td>");
					}else{
						String unit = DateTimeUtils.isNull(priscription.getUnit());
						str.append("<td>"+unit+"");
						//str.append("<td>"+priscription.getUnit()+"");
					}
					
					if(priscription.getUnitextension()!=null){
						if(!priscription.getUnitextension().equals("0")){
							str.append(""+priscription.getUnitextension()+"");
						}
					}
					str.append("</td>");
				}
				str.append("<input type='hidden' id='uom"+i+"' value='"+priscription.getUom()+"'  class='form-control'>");
				/*str.append("<td>"+priscription.getPriscdose()+"</td>");*/
				strdoage.append("<select class='form-control' onchange='changepriscgivendose("+i+",this.value)' name='dosage"+i+"' id='dosage"+i+"'>");
				strdoage.append("<option value='"+priscription.getPriscdose()+"'>"+priscription.getPriscdose()+"</option>");
					for(Master master : dosageList){
						strdoage.append("<option value='"+master.getName()+"'>"+master.getName()+"</option>");
					}
				strdoage.append("</select>");
				str.append("<td><s>"+strdoage.toString()+"</td>");
				
				
				//str.append("<td>"+priscription.getPriscdays()+" "+priscription.getPriscdurationtype()+"</td>");
				/*strdays.append("<select class='form-control' onchange='changepriscgivendays("+i+",this.value)' name='days"+i+"' id='days"+i+"'>");
				strdays.append("<option value='"+priscription.getPriscdays()+"'>"+priscription.getPriscdays()+"</option>");
				for(int p=1;p<=90;p++){
					strdays.append("<option value='"+p+"'>"+p+"</option>");
					}
				strdays.append("</select>");
				str.append("<td><s>"+strdays.toString()+"</td>");*/
				
				str.append("<td><input placeholder='days' type='number' class='form-control' onchange='changepriscgivendays("+i+",this.value)' id='priscday"+i+"' name='priscdays"+i+"' size='5' maxlength='5' value='"+priscription.getPriscdays()+"' ></td>");
				
				str.append("<td id='prisctimename"+i+"'>"+priscription.getPrisctimename()+"</td>");
				/*str.append("<td>"+priscription.getUnit()+""+priscription.getUnitextension()+"</td>");*/
				
				/*str.append("<td>"+priscription.getUnit()+"");
				if(priscription.getUnitextension()!=null){
					if(!priscription.getUnitextension().equals("0")){
						str.append(""+priscription.getUnitextension()+"");
					}
				}
				str.append("</td>");*/
				
				/*str.append("<td>"+priscription.getPrisctotal()+"</td>");*/
				str.append("<td><input type='number' id='drgivenqty"+i+"' value='"+priscription.getPriscqty()+"'  class='form-control'></td>");
				str.append("<td id='dosenotes"+i+"'>"+priscription.getDosenotes()+"</td>");
				/*str.append("<td><i onclick='showedit("+i+")' class='fa fa-edit' ></i></td>");*/
				
				/*str.append("<td>"+priscription.getPriscindivisualremark()+"</td>");*/
				str.append("<td><input placeholder='Remark' type='textarea' rows='2' class='form-control' id='priscindivisualremark"+i+"' onchange='changepriscgivenremark("+i+",this.value)' name='remark"+i+"' value='"+priscription.getPriscindivisualremark()+"' ></td>");
				
				str.append("<td><a><i onclick='deletepriscdata("+i+")' class='fa fa-trash-o' ></i></a></td>");
			/*	String temp[] = priscription.getLastmodified().split(" ");
				
				if(temp[0].equals(curdate)){
					str.append("<td><i onclick='deletepriscdata("+i+")' class='fa fa-trash-o' ></i></td>");
				}else{
					str.append("<td><i  class='fa fa-trash-o' ></i></td>");
				}*/
				
				str.append("</tr>");
				
				
				i++;
			}
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+str.toString()+""); 
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void getOTEquipmentData(){
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			String date = DateTimeUtils.getDashboardTodayDate(loginInfo.getTimeZone());
			
			
			
		/*	ArrayList<Priscription>priscriptionList = emrDAO.getPractitionerList(clientid,practionerid,conditionid,categoryid,medicineid,date);
			
			Priscription parentdata = emrDAO.getPriscriptionParentData(parentid);*/
			
			StringBuffer str = new StringBuffer();
			
			ArrayList<Priscription>priscriptionList = (ArrayList<Priscription>)session.getAttribute("oteqlist");
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.DATE, -7); 
			String curdate = dateFormat.format(cal.getTime());
			
			int i = 0;
			str.append("<input type='hidden' name='listsizeid' id='listsizeid' value='0'>");
			for(Priscription priscription : priscriptionList){
				str.append("<tr>");
				/*str.append("<td>"+priscription.getDate()+"</td>");*/
				
				/*str.append("<td>"+priscription.getPrisctype()+"</td>");*/
				str.append("<td >"+priscription.getMdicinenametxt()+"</td>");
				/*str.append("<td>"+priscription.getPriscfreq()+"</td>");
				str.append("<td>"+priscription.getPriscdose()+"</td>");*/
				str.append("<td>"+priscription.getPriscdays()+"</td>");
				/*str.append("<td>"+priscription.getPrisctotal()+"</td>");*/
				str.append("<td>"+priscription.getDosenotes()+"</td>");
				/*str.append("<td><i onclick='showedit("+i+")' class='fa fa-edit' ></i></td>");*/
				
				str.append("<td><i onclick='deleteeqdata("+i+")' class='fa fa-trash-o' ></i></td>");
				/*String temp[] = priscription.getLastmodified().split(" ");
				
				if(temp[0].equals(curdate)){
					str.append("<td><i onclick='deleteeqdata("+i+")' class='fa fa-trash-o' ></i></td>");
				}else{
					str.append("<td><i  class='fa fa-trash-o' ></i></td>");
				}*/
				str.append("</tr>");
				
				i++;
			}
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+str.toString()+""); 
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}


	public void getPriscriptionData(){
		
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			String date = DateTimeUtils.getDashboardTodayDate(loginInfo.getTimeZone());
			
			
			
		/*	ArrayList<Priscription>priscriptionList = emrDAO.getPractitionerList(clientid,practionerid,conditionid,categoryid,medicineid,date);
			
			Priscription parentdata = emrDAO.getPriscriptionParentData(parentid);*/
			
			StringBuffer str = new StringBuffer();
			
			ArrayList<Priscription>priscriptionList = (ArrayList<Priscription>)session.getAttribute("priscList");

			ArrayList<Master>dosageList = emrDAO.getDosageList();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.DATE, -7); 
			String curdate = dateFormat.format(cal.getTime());
			
			int i = 0;
			str.append("<input type='hidden' name='listsizeid' id='listsizeid' value='0'>");
			for(Priscription priscription : priscriptionList){
				str.append("<tr>");
				str.append("<td><input type='text' id='srno"+i+"' value='"+priscription.getSrno()+"' onchange='changepriscgivensrno("+i+",this.value)' class='form-control'></td>");
				str.append("<input type='hidden' class='akash' value='" + priscription.getMdicinenameid() + "' class='form-control' />");
				str.append("<input type='hidden' class='akashclasss' value='" + i + "' class='form-control' />");
				str.append("<input type='hidden' id='dddose"+i+"' value='"+priscription.getDddose()+"'  class='form-control'>");
				str.append("<input type='hidden' id='uom"+i+"' value='"+priscription.getUom()+"'  class='form-control'>");
				/*str.append("<td>"+priscription.getDate()+"</td>");*/
				
				/*str.append("<td>"+priscription.getPrisctype()+"</td>");*/
				if(priscription.getMedicine_shedule().equals("0")) {
				    //color="#777";
				    str.append("<td id='mdcinenametxt"+i+"' onclick='showprisceditmdcinenameindb("+i+","+priscription.getMdicinenameid()+")' style='color:#777;cursor:pointer'>"+priscription.getMdicinenametxt()+"</td>");
				} else if(priscription.getMedicine_shedule().equals("1")){
				    //color="#e05d6f";
				    str.append("<td id='mdcinenametxt"+i+"'  onclick='showprisceditmdcinenameindb("+i+","+priscription.getMdicinenameid()+")' style='color: #e05d6f;cursor:pointer'>"+priscription.getMdicinenametxt()+"</td>");
				} else if(priscription.getMedicine_shedule().equals("2")){
				    //color="#e69522";
				    str.append("<td id='mdcinenametxt"+i+"'  onclick='showprisceditmdcinenameindb("+i+","+priscription.getMdicinenameid()+")' style='color: #e69522;cursor:pointer'>"+priscription.getMdicinenametxt()+"</td>");
				}
				//str.append("<td>"+priscription.getPriscfreq()+"</td>");
				if(loginInfo.getOutoprisc()==1){
					str.append("<td>"+priscription.getDddose()+" "+priscription.getUom()+"</td>");
				}else{
					if(priscription.getUnit()==null){
						str.append("<td>");
					}else{
						String unit = DateTimeUtils.isNull(priscription.getUnit());
						str.append("<td>"+unit+"");
					}
					if(priscription.getUnitextension()!=null){
						if(!priscription.getUnitextension().equals("0")){
							str.append(""+priscription.getUnitextension()+"");
						}
					}
					str.append("</td>");
				}
				//str.append("<td>"+priscription.getDddose()+" "+priscription.getUom()+"</td>");
				
				//27-06-2019 AKash 
				//str.append("<td>"+priscription.getPriscdose()+"</td>");
				str.append("<td>");
					str.append("<select class='form-control' onchange='changepriscgivendose("+i+",this.value)' name='dosage"+i+"' id='dosage"+i+"'>");
					str.append("<option value='"+priscription.getPriscdose()+"'>"+priscription.getPriscdose()+"</option>");
						for(Master master : dosageList){
							str.append("<option value='"+master.getName()+"'>"+master.getName()+"</option>");
						}
					str.append("</select>");
				str.append("</td>");
				/*str.append("<td>"+priscription.getPriscdays()+"</td>");*/
				str.append("<td><input placeholder='days' type='number' class='form-control' onchange='changepriscgivendays("+i+",this.value)' id='priscday"+i+"' name='priscdays"+i+"' size='5' maxlength='5' value='"+priscription.getPriscdays()+"' ></td>");
				
				str.append("<td>"+priscription.getPrisctimename()+"</td>");
				/*str.append("<td>"+priscription.getUnit()+""+priscription.getUnitextension()+"</td>");*/
				//str.append("<td>"+priscription.getUnit()+"");
				/*if(priscription.getUnitextension()!=null){
					if(!priscription.getUnitextension().equals("0")){
						str.append(""+priscription.getUnitextension()+"");
					}
				}*/
				str.append("</td>");
				/*str.append("<td>"+priscription.getPrisctotal()+"</td>");*/
				str.append("<td><input type='number' id='drgivenqty"+i+"' value='"+priscription.getPriscqty()+"' onchange='changepriscgivenqty("+i+",this.value)' class='form-control'></td>");
				str.append("<td>"+priscription.getDosenotes()+"</td>");
				/*str.append("<td><i onclick='showedit("+i+")' class='fa fa-edit' ></i></td>");*/
				
				/*str.append("<td>"+priscription.getPriscindivisualremark()+"</td>");*/
				str.append("<td><input placeholder='Remark' type='textarea' rows='2' class='form-control' id='priscindivisualremark"+i+"' onchange='changepriscgivenremark("+i+",this.value)' name='remark"+i+"' value='"+priscription.getPriscindivisualremark()+"' ></td>");
				str.append("<td><a><i onclick='deletepriscdata("+i+")' class='fa fa-trash-o' ></i></a></td>");
				/*String temp[] = priscription.getLastmodified().split(" ");
				
				if(temp[0].equals(curdate)){
					str.append("<td><i onclick='deletepriscdata("+i+")' class='fa fa-trash-o' ></i></td>");
				}else{
					str.append("<td><i  class='fa fa-trash-o' ></i></td>");
				}*/
				str.append("</tr>");
				
				i++;
			}
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+str.toString()+""); 
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
	
	
	public String deleteDocuments(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{
			
			connection = Connection_provider.getconnection();
			String id = emrForm.getDeleteDoctId(); 
			String practionerId = emrForm.getDiaryUser();
			String patientid = emrForm.getClientname();
			String condition = emrForm.getCondition();
			
			
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			int result = emrDAO.deleteDocuments(id);
			//setFormData(patientid,practionerId,condition);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return getPatientRecord();	
	}
	public String deleteAllConsultationNote(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{
			
			request.setCharacterEncoding("UTF-8");
			
			connection = Connection_provider.getconnection();
			connection=DriverManager.getConnection(""+Constants.DB_HOST+":3306/"+loginInfo.getClinicUserid()+"?useUnicode=true&characterEncoding=UTF-8","pranams","6qxi5x&)~XBZ");
			int apmtId = emrForm.getAppointmentid();
			String practionerId = emrForm.getDiaryUser();
			String patientid = emrForm.getClientname();
			String condition = emrForm.getCondition();
			ConsultationNoteDAO consultationNoteDAO = new JDBCConsultationNoteDAO(connection);
			int result = consultationNoteDAO.deleteAllConsultationNote(Integer.toString(apmtId));
			//setFormData(patientid,practionerId,condition);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return getPatientRecord();
	}
	
	public String updateImageDateOfClient(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{
			
			connection = Connection_provider.getconnection();
			String clientImageDataId = emrForm.getClientImageDataId();
			String imagedata = emrForm.getClientImageData();
			String practionerId = emrForm.getDiaryUser();
			String patientid = emrForm.getClientname();
			String condition = emrForm.getCondition();
		
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			if(clientImageDataId == null || clientImageDataId.equalsIgnoreCase("")){
				int result = emrDAO.saveImageDateOfClient(imagedata,practionerId,patientid,condition);

			}
			else{
			int result = emrDAO.updateImageDateOfClient(clientImageDataId,imagedata);
			}
			//setFormData(patientid,practionerId,condition);

		}
		catch(Exception e){
			e.printStackTrace();
		}
		return getPatientRecord();
	}
	
	public String deleteClientImage(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{
			
			connection = Connection_provider.getconnection();
			String clientImageDataId = emrForm.getClientDataId();
			String practionerId = emrForm.getDiaryUser();
			String patientid = emrForm.getClientname();
			String condition = emrForm.getCondition();
		
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			int result = emrDAO.deleteImageDateOfClient(clientImageDataId);
			
			//setFormData(patientid,practionerId,condition);

		}
		catch(Exception e){
			e.printStackTrace();
		}
		return getPatientRecord();
	}
	
	public String filterConsultation(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{
			
			connection = Connection_provider.getconnection();
			int id = emrForm.getAppointmentid();
			String practionerId = emrForm.getDiaryUser();
			String patientid = emrForm.getClientname();
			String condition = emrForm.getCondition();
		
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			
			
			//setFormData(patientid,practionerId,condition);
			ConsultationNoteDAO consultationNoteDAO = new JDBCConsultationNoteDAO(connection);
			ArrayList<Emr> consultationNoteList = consultationNoteDAO.getConsultationNoteList(practionerId,patientid,condition,id);
			emrForm.setConsultationList(consultationNoteList);
			session.setAttribute("consultationNoteList", consultationNoteList);
			session.setAttribute("radioId", Integer.toString(id));
			emrForm.setApmtChk(true);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return getPatientRecord();
	}
	public void setFormData(String clientId,String practionerId,String condition){
		
		
		try{
			Connection connection = null;
			Client client = new Client();
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			EmrDAO emrDAO = new JDBCEmrDAO(connection);

			client = clientDAO.getPatient(Integer.parseInt(clientId));
			StringBuffer str = new StringBuffer();
			
			emrForm.setClient(client.getTitle()+" " + client.getFirstName()+" "+client.getLastName());
			emrForm.setClientData(client.getDob()+", "+client.getAddress()+"  "+client.getSecondLineaddress()+", "+client.getTown()+", "+client.getPostCode()+" , "+client.getMobNo());
			emrForm.setClientname(clientId);
			ConsultationNoteDAO consultationNoteDAO = new JDBCConsultationNoteDAO(connection);
			ArrayList<Emr> consultationNoteList = consultationNoteDAO.getConsultationNoteList(practionerId,clientId,condition);
			emrForm.setConsultationList(consultationNoteList);
			session.setAttribute("consultationNoteList", consultationNoteList);
			emrForm.setDiaryUser(practionerId);
			ArrayList<Client>clientList = consultationNoteDAO.getClientList(Integer.parseInt(practionerId));
			emrForm.setClientList(clientList);
		
			ArrayList<Client>conditionList = consultationNoteDAO.getConditionList(Integer.parseInt(clientId));
			
			emrForm.setConditionList(conditionList);
			emrForm.setCondition(condition);
			

			ArrayList<TreatmentEpisode>	treatmentEpisodeList = new ArrayList<TreatmentEpisode>();
			treatmentEpisodeList = emrDAO.getTreatmentEpisodeList(clientId,practionerId,condition);
			emrForm.setTreatmentEpisodeList(treatmentEpisodeList);
			String treatmentEpisodeid = consultationNoteDAO.getTreatmentEpisodeid(emrForm.getApmtId());
			emrForm.setTreatmentEpisodeid(treatmentEpisodeid);
			
			/*for(TreatmentEpisode t : treatmentEpisodeList){
				for(NotAvailableSlot n : t.getAppointmnetList())
				{
					if(t.getTreatmentApmtCount() == 1 && n.getApmtCount() == 1){
						emrForm.setApmtId(Integer.toString(n.getId()));
					}
				}
			}*/
			
			//Documentation
			ArrayList<Emr>docList = emrDAO.getDocList(Integer.parseInt(clientId), Integer.parseInt(practionerId),loginInfo.getId(),Integer.parseInt(condition),emrForm.getFilterdoctType());
			emrForm.setDocList(docList);
			ArrayList<Assessment>assessmentFormsList = emrDAO.getAssessmentList(clientId,practionerId,condition);
			emrForm.setAssessmentFormsList(assessmentFormsList);
			
			ArrayList<Emr>medicalRecordsTypeList = emrDAO.getMedicalRecordTypeList(Integer.parseInt(clientId), Integer.parseInt(practionerId), loginInfo.getId(),condition);
			emrForm.setMedicalRecordsTypeList(medicalRecordsTypeList);
			ArrayList<Assessment>imageDataList = emrDAO.getImageDataList(clientId,practionerId,condition);
			emrForm.setImageDataList(imageDataList);
			emrForm.setAction("emr");
			emrForm.setImageDataList(imageDataList);
			
			ArrayList<Emr>videoList = emrDAO.getVideoList(clientId,practionerId,condition);
			emrForm.setVideoList(videoList);
			}
		
			
			
			catch(Exception e){
				e.printStackTrace();
			}
		 
		
	}
	
	
	
	

	public EmrForm getModel() {
		
		return emrForm;
	}
	
	
	
public void createPackImage() throws Exception{
		
		Connection connection=null;
		try {
			connection=Connection_provider.getconnection();
			EmrDAO emrDAO=new JDBCEmrDAO(connection);
			InvestigationDAO investigationDAO=new JDBCInvestigationDAO(connection);
			ArrayList<Emr> dicomList= emrDAO.getDicomImageData();
			int i=0;
			for(Emr dicom : dicomList){
			    InputStream in =dicom.getDicomimageData();
	           		   
			    String filepath = request.getRealPath("/liveData/document/"+dicom.getInvstid()+"_dimage.png");
			    File fc = new File(filepath);
			    OutputStream f = new FileOutputStream(new File(filepath));
			    i++;
			    int c = 0;
			    //if(!fc.exists()){
			     while ((c = in.read()) > -1) {
			      f.write(c);
			     }
			    //}
			    f.close();
			    in.close();
			    
			    boolean isimgexist=emrDAO.ispackImageExists(dicom.getImgid());
			    if(!isimgexist){
			    	
			    	
			    	Investigation investigation=investigationDAO.getEditInvestigation(String.valueOf(dicom.getInvstid()));
			    	
                    String fileName=""+dicom.getInvstid()+"_dimage.png";
			    	dicom.setFileName(fileName);
			    	int pratitionerid=Integer.parseInt(investigation.getPrectionerid());
			    	dicom.setPractitionerId(pratitionerid);
			    	dicom.setCondition_id(investigation.getConditionid());
			    	dicom.setDoctType("Investigation");
			        int result = emrDAO.savePatientDocument(dicom,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),String.valueOf(dicom.getInvstid()),loginInfo.getUserId());  
			    }
			 }
				
		} catch (Exception e) {

		    e.printStackTrace();
		}
		finally{
			connection.close();
		}
	
	}
	
	

	
	public String printconsnote(){
		Connection connection = null;
		try{
			
			connection = Connection_provider.getconnection();
		Clinic clinic = new Clinic();
		ClinicDAO clinicDAO  = new JDBCClinicDAO(connection);
		AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
		ConsultationNoteDAO consultationNoteDAO = new JDBCConsultationNoteDAO(connection);
		
		String clientId = request.getParameter("clientid");
		String practionerId = request.getParameter("diaryuserid");
		String condition = request.getParameter("conditionid");
		String action = request.getParameter("action");
		String apmtid = request.getParameter("amptid");
		
		String date = DateTimeUtils.getDashboardTodayDate(loginInfo.getTimeZone());
		
		ArrayList<Emr> consultationNoteList = new ArrayList<Emr>();
		
		//ArrayList<Emr> consultationNoteList = consultationNoteDAO.getPrintConsultationNoteList(practionerId,clientId,condition,date);
		
		if(action.equals("1")){
			consultationNoteList = consultationNoteDAO.getConsultationNoteList(practionerId,clientId,condition);
		}else if(action.equals("3")){
			String editid = request.getParameter("editid");
			consultationNoteList = consultationNoteDAO.printEditConsultationNote(editid);
		}
		else{
			consultationNoteList = consultationNoteDAO.getPrintConsultationNoteList(practionerId,clientId,condition,date);
		}
		
		//01-12-2017 Lastmodified date Akash
		
		int size = consultationNoteList.size();
		String lastmodified ="";
		if (size > 0) {
			lastmodified = consultationNoteList.get(size - 1).getLastModified();
		}
		
		emrForm.setDateTime(lastmodified);
		
		emrForm.setConsultationList(consultationNoteList);
		session.setAttribute("consultationNoteList", consultationNoteList);
		
		int index = consultationNoteList.size() - 1;
		if(consultationNoteList.size()>0){
		emrForm.setOpdid(""+consultationNoteList.get(index).getAppointmentid());
		}
		//get client details
		ClientDAO clientDAO = new JDBCClientDAO(connection);
		Client client = clientDAO.getClientDetails(clientId);
		String fullname = client.getTitle() + "." + client.getFirstName() + " " + client.getLastName();
		
		Client bmidata= clientDAO.getPatientBMIData(clientId,DateTimeUtils.convertToInteger(apmtid));
		
		emrForm.setFullname(fullname);
		String age = DateTimeUtils.getAge(client.getDob());
		String age1=  DateTimeUtils.getAge1(client.getDob());
		
		age1= age1+"/"+client.getGender();
		emrForm.setAgegender(age1);
		emrForm.setAge(Integer.parseInt(age));
		emrForm.setGender(client.getGender());
		emrForm.setClientname(clientId);
		emrForm.setTown(client.getTown());
		emrForm.setMobno(client.getMobNo());
		
		emrForm.setBmi(bmidata.getBmi());	
		emrForm.setWeight(bmidata.getWeight());
		emrForm.setHeight(bmidata.getHeight());
		emrForm.setPulse(bmidata.getPulse());
		emrForm.setSysbp(bmidata.getSysbp());
		emrForm.setDiabp(bmidata.getDiabp());
		emrForm.setAbrivationid(client.getAbrivationid());
		emrForm.setSuagarfasting(bmidata.getSugarfasting());
		emrForm.setPostmeal(bmidata.getPostmeal());
		emrForm.setTemprature(bmidata.getTemprature());
		emrForm.setSpo2(bmidata.getSpo());
		emrForm.setBsa(bmidata.getHead_cir());
		//get clinic details
		clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
		
		ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
		//String address = accountsDAO.getLocationAddress(locationid,loginInfo.getId());
		
		emrForm.setClinicName(clinic.getClinicName());
		emrForm.setClinicOwner(clinic.getClinicOwner());
		emrForm.setOwner_qualification(clinic.getOwner_qualification());
		//accountsForm.setClinicaddress(address);
		emrForm.setLandLine(clinic.getLandLine());
		emrForm.setWebsiteUrl(clinic.getWebsiteUrl());
		emrForm.setClinicemail(clinic.getEmail());
		emrForm.setLocationAdressList(locationAdressList);
		emrForm.setClinicLogo(clinic.getUserImageFileName());
		
		UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
		
		UserProfile userProfile=userProfileDAO.getUserprofileDetails(Integer.parseInt(practionerId));
		emrForm.setPractitionerName(userProfile.getFullname());
		/*String date2= DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0] +" "+DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[1];
		emrForm.setDateTime(date2);*/
		emrForm.setQualification(userProfile.getQualification());
		session.setAttribute("drqualification", userProfile.getQualification());
		String[] drqualification = userProfile.getQualification().split("`");
		for (int i = 0; i < drqualification.length; i++) {
			System.out.println(drqualification[i]);
		}
		emrForm.setUseregno(userProfile.getRegisterno());
		}catch(Exception e){
			e.printStackTrace();
		}
		return "printconsnote";
	}

	public void prepare() throws Exception {
		Connection connection = null;
		try{
			
			connection = Connection_provider.getconnection();
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			ConsultationNoteDAO consultationNoteDAO = new JDBCConsultationNoteDAO(connection);
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			InvestigationDAO investigationDAO=new JDBCInvestigationDAO(connection);
			UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
			PharmacyDAO pharmacyDAO =new JDBCPharmacyDAO(connection);
			ArrayList<DiaryManagement>userList = notAvailableSlotDAO.getUserList(loginInfo.getId());
			emrForm.setUserList(userList);
			
			if(loginInfo.getUserType() == 4){
				emrForm.setDiaryUser(Integer.toString(loginInfo.getId()));
				//ArrayList<Client>clientList = consultationNoteDAO.getClientList(loginInfo.getId());
				ArrayList<Client>clientList = new ArrayList<Client>();
				emrForm.setClientList(clientList);
			
				/*ArrayList<Client>conditionList = consultationNoteDAO.getConditionList(Integer.parseInt(clientId));
				emrForm.setConditionList(conditionList);*/
			}
			else{
			ArrayList<Client>clientList = new ArrayList<Client>();
			emrForm.setClientList(clientList);
			}
			ArrayList<Client>conditionList = new ArrayList<Client>();
			emrForm.setConditionList(conditionList);

			ArrayList<Emr> consultationNoteList = new ArrayList<Emr>();
			emrForm.setConsultationList(consultationNoteList);
			session.setAttribute("consultationNoteList", consultationNoteList);
			
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			ArrayList<Emr>medicalRecordTypeList = emrDAO.getMedicalRecordTypeList();
			emrForm.setMedicalRecordTypeList(medicalRecordTypeList);
			
			ImportImageForAssessmentDAO imageForAssessmentDAO = new JDBCImportImageAssessmentDAO(connection);
			ArrayList<Assessment> importImageList = imageForAssessmentDAO.getImportImageList(); 
			emrForm.setImportImageList(importImageList);
			
			ArrayList<Discharge>dischargeOutcomeList = emrDAO.getDischrageOutcomeList();
			emrForm.setDischargeOutcomeList(dischargeOutcomeList);
			
			//investigation section master
			ArrayList<Master>invSectionList = investigationDAO.getInvestigationSectionList();
			emrForm.setInvSectionList(invSectionList);
			
			
			ArrayList<Discharge>dischargeStatusList = emrDAO.getDischrageStatusList();
			emrForm.setDischargeStatusList(dischargeStatusList);

			String date = DateTimeUtils.getDashboardTodayDate(loginInfo.getTimeZone());
			emrForm.setPriscdate(date);
			emrForm.setPriscdateandtime(DateTimeUtils.getPriscDatetime(loginInfo.getTimeZone()));
			
			ArrayList<Master>mdicinecategoryList = emrDAO.getmedicineCategoryList();
			emrForm.setMdicinecategoryList(mdicinecategoryList);
			
			ArrayList<Master>dosageList = emrDAO.getDosageList();
			emrForm.setDosageList(dosageList);
			
			ArrayList<Master>mdicneTypeList = emrDAO.getMedicineTypeList();
			emrForm.setMdicneTypeList(mdicneTypeList);
			
			ArrayList<Client> condtitionList = new ArrayList<Client>();//clientDAO.getEmrTreatmentTypeList();
			emrForm.setTreatmentTypeList(condtitionList);
			session.setAttribute("opdconditionlist", condtitionList);
			
			ArrayList<Master>invsTypeList = emrDAO.getInvesigationTypeList();
			emrForm.setInvsTypeList(invsTypeList);
			
			ArrayList<Master>invstReportTypeList = emrDAO.getInvstReportTypeList();
			emrForm.setInvstReportTypeList(invstReportTypeList);
			
			ArrayList<Master>invstUnitList = emrDAO.getInvstUnitList();
			emrForm.setInvstUnitList(invstUnitList);
			
			//get other template details
			MasterDAO masterDAO = new JDBCMasterDAO(connection);
			
			String practionerId = emrForm.getDiaryUser();
			if(practionerId==null){
				practionerId = (String)session.getAttribute("diaryUserId");
			}
			
			if(practionerId!=null){
				if(practionerId.equals("")){
					practionerId=null;
				}
			}
			
			if(practionerId==null) {
			
			   ArrayList<Master> otherTemplateList = masterDAO.getEmrTemplateList(null);
			   emrForm.setOtherTemplateList(otherTemplateList);
			}else {
				
				UserProfile userProfile=userProfileDAO.getUserprofileDetails(Integer.parseInt(practionerId));
				ArrayList<Master> otherTemplateList = masterDAO.getEmrTemplateList(userProfile.getDiciplineName());
				emrForm.setOtherTemplateList(otherTemplateList);
			}
			ArrayList<Master>medicineList = masterDAO.getMedicineList();
			emrForm.setMedicineList(medicineList);
			
			ArrayList<Master>dosagenoteList = masterDAO.getDosageNoteList();
			emrForm.setDosagenoteList(dosagenoteList);
			
			//user define date time
			emrForm.setHourList(PopulateList.hourList());
			emrForm.setMinuteList(PopulateList.getMinuteList());
			
			String datetime = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
			
			String temp[] = datetime.split(" ");
			String dates = DateTimeUtils.getCommencingDate1(temp[0]);
			emrForm.setDischargedate(dates);;
			String time[] = temp[1].split(":");
			String hh = time[0];
			String mm = time[1];
			emrForm.setHour(hh);
			emrForm.setMinute(mm);
			
			
			
			ArrayList<Priscription>parentPriscList = new ArrayList<Priscription>();
			emrForm.setParentPriscList(parentPriscList);
			
			
			ArrayList<String> jobTitleList = userProfileDAO.getJobTitleList();
			emrForm.setJobTitleList(jobTitleList);
			UserProfile userProfile = userProfileDAO.getUserprofileDetails(loginInfo.getId());
			emrForm.setJobtitle("Pathlab");
			
			
			//set template name list
			
			ArrayList<Priscription>templateNameList = emrDAO.getTemplateNameList(loginInfo);
			emrForm.setTemplateNameList(templateNameList);
			
			ArrayList<Priscription>oteqtemplateNameList = emrDAO.getoteqTemplateNameList();
			emrForm.setOteqtemplateNameList(oteqtemplateNameList);
			
			//set ot asset list
			ArrayList<Master>assetList = emrDAO.getOtAssetList();
			emrForm.setAssetList(assetList);
			
			//investigation pkg list
			ArrayList<Master>pkgsList = investigationDAO.getInvPaksLists();
			emrForm.setPkgsList(pkgsList);
		
			//jitu
			ArrayList<Master> specializationTemplateList= masterDAO.getMasterSpecializationList();
			emrForm.setSpecializationTemplateList(specializationTemplateList);
			
			ArrayList<Master> priscUnitList= masterDAO.getPriscUnitList();
			emrForm.setPriscUnitList(priscUnitList);
			
			//Akash 11 April 2018
			ArrayList<Priscription> medicinetimelist = emrDAO.getMedicineTimeList(); 
			emrForm.setMedicinetimelist(medicinetimelist);
			
			ArrayList<Master>nimaidoselist = masterDAO.getnimaidoselistt();
			ArrayList<Master>nimaiqtylist = masterDAO.getnimaiqtylist();
			ArrayList<Master>nimairemarklist = masterDAO.getnimairemarlist();
			
			emrForm.setNimaidoselist(nimaidoselist);
			emrForm.setNimaiqtylist(nimaiqtylist);
			emrForm.setNimairemarklist(nimairemarklist);
			ArrayList<Master> requestlocationlist= pharmacyDAO.getAllLocationNew();
			emrForm.setRequestlocationlist(requestlocationlist);
			if(loginInfo.isPrisc_location_list()){
				int default_location = pharmacyDAO.getByDefaultPharmacyLocation();
				emrForm.setRequestlocationid(""+default_location);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			connection.close();
		}
		
	}
	
	
	public String getothertemplate() throws Exception {
		  
		  Connection connection=null;
		  
		  try {
		    connection=Connection_provider.getconnection();
		      MasterDAO masterDAO=new JDBCMasterDAO(connection); 
		   String tempid=request.getParameter("id");
		   
		   Master master=masterDAO.getOtherTemplate(tempid);
		    
		   response.setContentType("text/html");
		   response.setHeader("Cache-Control", "no-cache");
		   response.getWriter().write(master.getOther_template_text()); 
		   
		   
		  } catch (Exception e) {

		     e.printStackTrace();
		  }
		  finally {
		   connection.close();
		  }
		  
		   return null; 
		 }
	
	
	public String share(){
		if(!verifyLogin(request)){
			return "login";
		}
		
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			
			// var url="shareEmr?email="+email+"&fname="+fname+"&lname="+lname+"&mob="+mob+"&diaryUser="+diaryUser+"&clientname="+clientname+"&condition="+condition+" ";
			String email = request.getParameter("email");
			String fname = request.getParameter("fname");
			String lname = request.getParameter("lname");
			String mob = request.getParameter("mob");
			String diaryUser = request.getParameter("diaryUser");
			String clientname = request.getParameter("clientname");
			String condition = request.getParameter("condition");
			
			Client client = new Client();
			client.setEmail(email);
			client.setFirstName(fname);
			client.setLastName(lname);
			client.setMobNo(mob);
			client.setDiaryUser(diaryUser);
			client.setClientName(clientname);
			client.setTreatmentType(condition);
			
			int result = emrDAO.saveShareEmr(client);
			
			Client c = clientDAO.getClientDetails(clientname);
			String fullname = c.getTitle() + " " + c.getFirstName() + " " + c.getLastName();
			
			String emailId = email;
			String subject = "Shared Emr";
			StringBuffer str1 = new StringBuffer();
			str1.append("<table width = '50%'> ");
			str1.append("<tr>");
			str1.append("<td colspan ='2'>Hi,</td>");
			str1.append("</tr>");
			str1.append("<tr>");
			str1.append("<td>Please click on the link to see shared emr</td> ");
			str1.append("</tr>");
			
			
			str1.append("<tr>");
			//str1.append("<td><a href='http://myohost:8080/APM1.1/changeResetPassword' target = 'null'>Reset Password</a></td>");		
			/*str1.append("<td><a href='http://saas.myohost.com:8080/histesting/Share?clientid="+clientname+"&diaryuser="+diaryUser+"&condition="+condition+"&clinicid="+loginInfo.getClinicUserid()+"&email="+email+"&mob="+mob+"' " +
					"target = ''>Emr of "+fullname+"</a></td>");*/
			
			str1.append("<td><a href='http://154.70.153.191:8080/HIS/Share?clientid="+clientname+"&diaryuser="+diaryUser+"&condition="+condition+"&clinicid="+loginInfo.getClinicUserid()+"&email="+email+"&mob="+mob+"' " +
					"target = ''>Emr of "+fullname+"</a></td>");
			//str1.append("<td><a href='http://myapm.co.uk:8080/APM/changeResetPassword' target = ''>Reset Password</a></td>");
			str1.append("</tr>");
			str1.append("</br>");
			str1.append("<tr>");
			
			str1.append("<td>Thank you for your HIS.</td> ");
			str1.append("</tr>");
			/*str1.append("<tr>");
			str1.append("<td>care@myapm.co.uk</td>");
			str1.append("</tr>");*/
			str1.append("</table>");
			
			EmailLetterLog emailLetterLog = new EmailLetterLog();
			emailLetterLog.setClientId(clientname);
			emailLetterLog.setType("Email");
			
			EmbeddedImageEmailUtil.sendEmrLinkMail(emailId, "", subject, str1.toString(),loginInfo,emailLetterLog,connection);
			
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}
	

	
	
	public String chshuser(){
		String mob = request.getParameter("mob");
		
		try{
			
			   String sharedmob = (String) session.getAttribute("sharedmob");
			   String str = "0";
			   
			   if(sharedmob.equals(mob)){
				   str = "1";
				   
				   String otp = DateTimeUtils.getOTP();
					//String to = email;
					String cc = "";
					String subject = "One Time Password";
					String notes = "One Time Password for shared EMR is "+otp+". Please use this password to see EMR.";
					
					//EmbeddedImageEmailUtil.sendOtpMail(to, cc, subject, notes);
					
					if(loginInfo.getCountry().equals("India")){
						SmsService s = new SmsService();
						s.sendSms(notes, mob, loginInfo, new EmailLetterLog());
					}
					str = str + "~" + otp;
			   }
			   
			   response.setContentType("text/html");
			   response.setHeader("Cache-Control", "no-cache");
			   response.getWriter().write(str); 
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}
	
	
	public String conf(){
		String diaryUser = request.getParameter("diaryUser");
		String clientname = request.getParameter("clientname");
		String condition = request.getParameter("condition");
		String confpassd = request.getParameter("confpassd");
		
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			
			boolean chkConfidentialExist = emrDAO.chkConfidentialExist(diaryUser,clientname,condition);
			if(!chkConfidentialExist){
				int res = emrDAO.saveConfidentialEmr(diaryUser,clientname,condition,confpassd);
			}
			
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}
	
	public String confsvd(){
		
		return getPatientRecord();
	}
	



	
	
	
public String allconditions() throws Exception{
		
		Connection connection=null;
		try {
	        		
			connection=Connection_provider.getconnection();
			EmrDAO emrDAO=new JDBCEmrDAO(connection);
			DiagnosisDAO diagnosisDAO=new JDBCDiagnosisDAO(connection);
            
			String apmtId=request.getParameter("apmtId");
			String condition=request.getParameter("condition");			
			String allconditions=emrDAO.getAllConditionsFromApmt(apmtId);
			StringBuffer buffer=new StringBuffer();
			
			Diagnosis diagno=diagnosisDAO.getDiagnosisName(condition);
			buffer.append("<b>Condition:</b> <br>");
			buffer.append("<label>"+diagno.getName()+":</label><br>");
			
			int i=-1;
            for(String str:allconditions.split(",")){
            	   i++;
            	    if(i==0){
            	    	 continue;
            	    }
            	    Diagnosis diagnosis=diagnosisDAO.getDiagnosisName(str);        
            	    buffer.append("<label>"+diagnosis.getName()+":</label> <br>");
            }
			  
               response.setContentType("text/html");
			   response.setHeader("Cache-Control", "no-cache");
			   response.getWriter().write(buffer.toString());   
          			
		} catch (Exception e) {

		   e.printStackTrace();
		}
	    finally {
	    	connection.close();
	    	
	    	
	    }
		return null;
	}
	
	
	
	public String chconf(){
		String diaryUser = request.getParameter("diaryUser");
		String clientname = request.getParameter("clientname");
		String condition = request.getParameter("condition");
		String confpassd = request.getParameter("confpassd");
		
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			
			String str = "";
			String pass = emrDAO.getConfidentialPassword(diaryUser,clientname,condition);
			if(pass.equals(confpassd)){
				str = "1";
			}
			
			   response.setContentType("text/html");
			   response.setHeader("Cache-Control", "no-cache");
			   response.getWriter().write(str); 
			
		}catch (Exception e) {

			e.printStackTrace();
		}
		
		
		return null;
	}
	
	
	//@ jitu
	public String newmedicine() throws Exception{
		
		Connection connection=null;
		try {
			
			connection=Connection_provider.getconnection();
			MasterDAO masterDAO= new JDBCMasterDAO(connection);
			InventoryProductDAO inventoryProductDAO= new JDBCInventoryProductDAO(connection);
			PrescriptionMasterDAO prescriptionMasterDAO= new JDBCPrescriptionMasterDAO(connection);
			String medicinename= request.getParameter("medicinename");
			String genericname= request.getParameter("genericname");
			String istemp= request.getParameter("istemp");
			String date = "";
			Product product= new Product();
			product.setProduct_name(medicinename);
			product.setGeneric_name(genericname);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			date = dateFormat.format(cal.getTime());
			product.setDate(date);
			String loc = (String) session.getAttribute("location");
			if (loc == null) {
				loc = "0";
			}
			product.setLocation(loc);
			if (product.getGeneric_name() == null) {
				product.setGeneric_name("");
			} else if (product.getGeneric_name().equals("")) {
				product.setGeneric_name("");
			}
			
			product.setCategory_id("2");
			product.setSubcategory_id("1");
			
			if(istemp!=null){
				if(istemp.equals("")){
					istemp="0";
				}
			}else{
				istemp="0";
			}
			
			int res = inventoryProductDAO.addNewProduct(product);
			//int result = inventoryProductDAO.addNewProductToVendor(loc, res);
			int result=prescriptionMasterDAO.addToMedicineMaster(product,res,istemp);
			
			setmedicinedrufform();
			
		
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		finally {
			connection.close();
		}
				
		return null;
	}
	
	public String showmd(){
		
		setmedicinedrufform();
		
		return null;
	}
	
	
	public void setmedicinedrufform(){
		Connection connection=null;
	try {
			
			connection=Connection_provider.getconnection();
			MasterDAO masterDAO= new JDBCMasterDAO(connection);
			
			//add to medicine master
			
			ArrayList<Master>medicineList = masterDAO.getMedicineList();
			StringBuffer buffer =new StringBuffer();
			buffer.append("<select class='form-control chosen' id='mdicinename' name='mdicinename' onchange='getDoseNote(this.value)' > ");
			buffer.append("<option value='0'>Select Medicine</option>");
			for(Master master:medicineList){
				
				buffer.append("<option value='"+master.getId()+"'> "+master.getGenericname()+" </option>");
			}
			buffer.append("</select>");
			
			buffer.append("<a href='#' type='button' class='btn btn-sm btn-primary' onclick=openhiddendiv('hiddendiv') style='margin-left: 5px;'><i  class='fa fa-plus'></i></a>");
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(buffer.toString());   
			
	}catch (Exception e) {
		// TODO: handle exception
	}
	
	}
	
	
	public String updateTemplate(){
		if(!verifyLogin(request)){
			return "login";
		}
		String mrdid = request.getParameter("mrdid");
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			int id = Integer.parseInt(request.getParameter("id"));
			String clientId = request.getParameter("clientName");
			String diaryUserId = request.getParameter("diaryUser");
			String conditionId = request.getParameter("condition");
			
			if (clientId!=null) {
				if(clientId.equals("0") || clientId == "0"){
					clientId = request.getParameter("mrd_clientid");
				}
			}
			
			ListAssessmentDAO listAssessmentDAO = new JDBCListAssessmentFormDAO(connection);
			
			int result1 = listAssessmentDAO.updateAssessmentClientNameImage(id,clientId,diaryUserId,conditionId);
			
			ArrayList<Assessment> fieldNameList = (ArrayList<Assessment>) session.getAttribute("fieldNameList");
			for(Assessment assessment : fieldNameList){
				
				String lableName = assessment.getFiledname();
			
			
			/*	String fieldValue = lableName.replace(" ", "_");
				String temp1 = fieldValue.replace("(", "_");
				String temp2 = temp1.replace(")", "_");
				String temp3 = temp2.replace("-", "_");
				String temp4 = temp3.replace("/", "_");
				String temp5 = temp4.replace("?", "_");
				String temp6 = temp5.replace(",", "_");
				String temp7 = temp6.replace("&", "_");
				String temp8 = temp7.replace("+", "_");
				String temp9 = temp8.replace(".", "_");
				String temp10 = temp9.replace("'", "_");
				*/
				
				String temp10 = DateTimeUtils.removeAllSpecialChar(lableName);
				temp10 = temp10.replace(" ", "");
				
				String textName = request.getParameter(lableName);
				
				String note = "";
				if(assessment.getType().equals("5")){
					note = request.getParameter("note_"+lableName);
					textName = textName + ":" + note;
				}
				
				if(assessment.getType().equals("6")){
					note = request.getParameter("note_"+lableName);
					note = request.getParameter("note_"+lableName);
					textName = textName + ":" + note;
				}
				
				if(emrForm.isRepeat()==false){
					int result = listAssessmentDAO.updateAssessmentFormClient(id,temp10,lableName,textName,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
				}else{
					AssessmentFormDAO assessmentFormDAO = new JDBCAssessmentFormDAO(connection);
					Template template = (Template)session.getAttribute("assmnttemplatedetails");
					String clientassesmentfieldid = (String)session.getAttribute("clientassesmentfieldid");
					ArrayList<Assessment>dataList = assessmentFormDAO.getRepeatFormData(template,assessment.getFiledname(),clientassesmentfieldid);
					int r=0;
					for(Assessment data : dataList){
						String strtxt = request.getParameter(assessment.getFiledname() + r);
						int updte = assessmentFormDAO.updateRepeatFormData(strtxt,data.getId(),temp10,clientId,diaryUserId,conditionId);
						r++;
					}
				}
				
			}
			
			String templateId = listAssessmentDAO.getTemplateId(id);
			
			session.setAttribute("clientId", clientId);
			session.setAttribute("diaryUserId", diaryUserId);
			session.setAttribute("conditionId", conditionId);
			session.setAttribute("templateId", templateId);
			
			
			
		//	setFormData(clientId, diaryUserId, conditionId);
			
			//emrForm.setMessage("Field Updated Sucessfully!!");
			addActionMessage("Field Updated Sucessfully!!");
			
			//assessmentForm.setClientName("");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		if(mrdid!=null)
			return "mrd";
		else
			return getPatientRecord();
	}
	
public String emrnew() throws Exception{
	return "EMRNEW";
}
public String emrdocs() throws Exception{
	
	Connection connection=null;
	try {
		connection=Connection_provider.getconnection();
		String clientId= request.getParameter("clientId");
		String diaryUser= request.getParameter("diaryUser");
		String condition= request.getParameter("condition");
		
		if(clientId==null){
			
		     clientId= (String) session.getAttribute("clientId");
		     diaryUser= (String) session.getAttribute("diaryUserId");
		     condition= (String) session.getAttribute("condition");
		}
		
		String fromdate=emrForm.getFromdate();
		String todate= emrForm.getTodate();
		
		if(fromdate==null){
			Calendar calendar=Calendar.getInstance();
			SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
			fromdate=dateFormat.format(calendar.getTime());
		} else {
			fromdate= DateTimeUtils.getCommencingDate1(fromdate);
		}
		if(todate==null){
			
			Calendar calendar=Calendar.getInstance();
			SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
			todate=dateFormat.format(calendar.getTime());
		} else {
			todate= DateTimeUtils.getCommencingDate1(todate);
		}
			
		
		
		ClientDAO clientDAO=new JDBCClientDAO(connection);
		EmrDAO emrDAO= new JDBCEmrDAO(connection);
		
		String doctype= request.getParameter("doctype");
		if(doctype==null){
			doctype="2";	
		}
		if(doctype.equals("")){
			doctype="2";
		}
		
		ArrayList<Emr> documentDetailedList= new ArrayList<Emr>();
		
		
		ArrayList<Emr> emrDocumentList= emrDAO.getDocumentList(clientId, "0",fromdate,todate);
		ArrayList<Emr> ipdDocumentList= emrDAO.getDocumentList(clientId, "1",fromdate,todate);
		ArrayList<Emr> opdDocumentList= emrDAO.getDocumentList(clientId, "2",fromdate,todate);
		if(doctype.equals("2")){
			documentDetailedList=opdDocumentList;
		} else if(doctype.equals("1")){
			documentDetailedList= ipdDocumentList;
		} else if(doctype.equals("0")){
			documentDetailedList= emrDocumentList;
		}
		
		
		
		Client client=clientDAO.getClientDetails(clientId);
		String fullname= client.getTitle()+" "+client.getFirstName()+" "+client.getLastName();
		emrForm.setAbrivationid(client.getAbrivationid());
		emrForm.setFullname(fullname);
		String agegender="";
		String age="";
		try {
			age= DateTimeUtils.getAge(client.getDob());
		} catch (Exception e) {
			
		}
		emrForm.setClientId(clientId);
		emrForm.setAgeandgender(client.getGender()+"/"+age);
		emrForm.setDob(client.getDob());
		emrForm.setMobNo(client.getMobNo());
		emrForm.setTown(client.getCity());
		emrForm.setDiaryUser(diaryUser);
		emrForm.setCondition(condition);
		emrForm.setClientname(clientId);
		
		session.setAttribute("clientId", clientId);
		session.setAttribute("diaryUserId", diaryUser);
		session.setAttribute("conditionId", condition);
		
		emrForm.setOpdDocumentList(opdDocumentList);
		emrForm.setIpdDocumentList(ipdDocumentList);
		emrForm.setEmrDocumentList(emrDocumentList);
		emrForm.setDocumentDetailedList(documentDetailedList);
		fromdate= DateTimeUtils.getCommencingDate1(fromdate);
		todate= DateTimeUtils.getCommencingDate1(todate);
		
		emrForm.setFromdate(fromdate);
		emrForm.setTodate(todate);
		
		
	} catch (Exception e) {

		e.printStackTrace();
	}
	finally {
		
		connection.close();
	}
	return "emrdocs";
}

public String savecondition() throws Exception {
	
	Connection connection=null;
	try {
		connection=Connection_provider.getconnection();
		String condition= request.getParameter("condition");
		String icdcode= request.getParameter("icdcode");
		String practitionerid= request.getParameter("diaryUser");
		DiagnosisDAO  diagnosisDAO =new JDBCDiagnosisDAO(connection);
		UserProfileDAO userProfileDAO= new JDBCUserProfileDAO(connection); 
		ClientDAO clientDAO=new JDBCClientDAO(connection);
		UserProfile userProfile= userProfileDAO.getUserprofileDetails(Integer.parseInt(practitionerid));
		Diagnosis diagnosis=new Diagnosis();
		diagnosis.setName(condition);
		diagnosis.setDepartment(userProfile.getDiciplineName());
		diagnosis.setIcdcode(icdcode);
		int d=diagnosisDAO.addDiagnosisName(diagnosis);
		ArrayList<Client> list= clientDAO.getEmrTreatmentTypeList();

		StringBuffer buffer= new StringBuffer();
		/*buffer.append("<tr id='dispid' class='hidden'>");
		buffer.append("<td>");
		buffer.append("<input type='text' class='form-control' placeholder='Enter New Diagnosis' id='newcondition' style='border: 1px solid #ddd;margin-top: 8px;width: 49%;'>");
		buffer.append("<input type='text' class='form-control' placeholder='Enter ICD Code' id='icdcode' style='border: 1px solid #ddd;margin-top: 8px;width: 50%;'>");
		buffer.append("<input type='button' onclick='addnewCOndition1()' class='btn btn-sm btn-info' style='margin-top: 8px;' value='Add New' />");
		buffer.append("</td>");
		buffer.append("</tr>");*/
		for(Client client : list) {
			buffer.append("<tr>");
            buffer.append("<td>");
            buffer.append(" <input class='concase' type='checkbox' onclick='showopdcontxtoneditornew("+client.getId()+")'  id='chh"+client.getId()+"' ");
            buffer.append("name='ch"+client.getId()+"' value='"+client.getId()+"'>");
            buffer.append("<span id='ccck"+client.getId()+"' >"+client.getTreatmentType()+"</span><br>");
            buffer.append("<td>");
            buffer.append("</tr>");
			/*buffer.append("<li style='cursor: pointer;' onclick='showopdcontxtoneditornew('"+client.getTreatmentType()+"')'>"+client.getTreatmentType()+"</li>");*/
			/*buffer.append("<span style='cursor: pointer;' onclick='showopdcontxtoneditornew('"+client.getTreatmentType()+"')'>"+client.getTreatmentType()+"</span><br>");*/
        }
		response.setContentType("text/html");
	    response.setHeader("Cache-Control", "no-cache");
	    response.getWriter().write(buffer.toString());
	} catch (Exception e) {
		e.printStackTrace();
	}
	finally {
		connection.close();
	}
	return null;
}
public String refreshtemplatelist() throws Exception {
	
	Connection connection=null;
	try {
		connection=Connection_provider.getconnection();
		MasterDAO masterDAO = new JDBCMasterDAO(connection);
		UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
		String diciplineid = request.getParameter("diciplineid");
		String practionerId = request.getParameter("drid");
		if(diciplineid!=null){
			if(diciplineid.equals("")){
				diciplineid =null;
			}
		}
		
		if(practionerId!=null) {
			if(!practionerId.equals("")){
				UserProfile userProfile=userProfileDAO.getUserprofileDetails(Integer.parseInt(practionerId));
				diciplineid = userProfile.getDiciplineName();
			}
		}
		ArrayList<Master> otherTemplateList = masterDAO.getEmrTemplateList(diciplineid);
		StringBuffer buffer= new StringBuffer();
		int i=1;
		for(Master master : otherTemplateList) {
			buffer.append("<tr>");
			buffer.append("<td>"+i+"</td>");
			buffer.append("<td><a href='#' onclick='setselectedtemplatedataNew("+master.getId()+")'>"+master.getTitle()+"</a></td>");
			buffer.append("</tr>");
			i++;
		}
		response.setContentType("text/html");
	    response.setHeader("Cache-Control", "no-cache");
	    response.getWriter().write(buffer.toString());
	} catch (Exception e) {
		e.printStackTrace();
	}
	finally {
		connection.close();
	}
	return null;
}

public String getdiagnosis() throws Exception {
	
	Connection connection=null;
	try {
		connection = Connection_provider.getconnection();
		IpdDAO ipdDAO = new JDBCIpdDAO(connection);
		String search = request.getParameter("search");
		String selected= request.getParameter("selected");
		ArrayList<Client> ipdCondtitionList = ipdDAO.getAllDiagnosis(search);
		StringBuilder builder = new StringBuilder();
		for (Client client : ipdCondtitionList) {
			
			boolean flag=false;
			
			for(String s:selected.split(",")){

				     if(s==null){
				    	 continue;
				     }
				
				    int d=Integer.parseInt(s);
				    if(d==0){
				    	continue;
				    }
				    if(d==client.getId()){
				    	flag=true;
				    	break;
				    }
			}
			
		   
			if(flag){
				
				  builder.append("<tr><td>");
					builder.append("<input class='concase' checked='checked' type='checkbox' onclick='setCheckedData(this)'  id='chh"+client.getId()+" name='ch"+client.getId()+"' value='"+client.getId()+"' /> ");
					builder.append("<span id='ccck"+client.getId()+"' >"+client.getTreatmentType()+"</span><br>");
					builder.append("</td></tr>");
			} else {
				  builder.append("<tr><td>");
					builder.append("<input class='concase' type='checkbox' onclick='setCheckedData(this)'  id='chh"+client.getId()+" name='ch"+client.getId()+"' value='"+client.getId()+"' /> ");
					builder.append("<span id='ccck"+client.getId()+"' >"+client.getTreatmentType()+"</span><br>");
					builder.append("</td></tr>");
			}
			  
			
		}
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(builder.toString());
	} catch (Exception e) {

		e.printStackTrace();
	}
	finally {
		connection.close();
	}
	
	return null;
	
}

public String getdiagnosisedit() throws Exception {
	
	Connection connection=null;
	try {
		connection = Connection_provider.getconnection();
		IpdDAO ipdDAO = new JDBCIpdDAO(connection);
		String search = request.getParameter("search");
		String selected= request.getParameter("selected");
		ArrayList<Client> ipdCondtitionList = ipdDAO.getAllDiagnosis(search);
		StringBuilder builder = new StringBuilder();
		for (Client client : ipdCondtitionList) {
			
			boolean flag=false;
			
			for(String s:selected.split(",")){

				     if(s==null){
				    	 continue;
				     }
				
				    int d=Integer.parseInt(s);
				    if(d==0){
				    	continue;
				    }
				    if(d==client.getId()){
				    	flag=true;
				    	break;
				    }
			}
			
		   
			if(flag){
				
				  builder.append("<tr><td>");
					builder.append("<input class='econcase' checked='checked' type='checkbox' onclick='setCheckedDataEdit(this)'  id='echh"+client.getId()+" name='ech"+client.getId()+"' value='"+client.getId()+"' /> ");
					builder.append("<span id='econtxt"+client.getId()+"' >"+client.getTreatmentType()+"</span><br>");
					builder.append("</td></tr>");
			} else {
				  builder.append("<tr><td>");
					builder.append("<input class='econcase' type='checkbox' onclick='setCheckedDataEdit(this)'  id='echh"+client.getId()+" name='ech"+client.getId()+"' value='"+client.getId()+"' /> ");
					builder.append("<span id='econtxt"+client.getId()+"' >"+client.getTreatmentType()+"</span><br>");
					builder.append("</td></tr>");
			}
			  
			
		}
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(builder.toString());
	} catch (Exception e) {

		e.printStackTrace();
	}
	finally {
		connection.close();
	}
	
	return null;
	
}


public String setalldiagnosis() throws Exception {
	
	Connection connection=null;
	try {
		connection=Connection_provider.getconnection();
		DiagnosisDAO diagnosisDAO=new JDBCDiagnosisDAO(connection);
		StringBuffer builder=new StringBuffer();
		String selected= request.getParameter("selected");
		
		int i=0;
		for(String s:selected.split(",")){
			
			   int d=Integer.parseInt(s);
			   if(d==0){
				    continue;
			   } else {
				   	
				    Diagnosis diagnosis=diagnosisDAO.getDiagnosisName(s);
				    builder.append("<tr><td>");
					builder.append("<input class='concase' checked='checked'  type='checkbox' onclick='showopdcontxtoneditornew("+d+")'  id='chh"+d+" name='ch"+d+"' value='"+d+"' /> ");
					builder.append("<span id='ccck"+d+"' >"+diagnosis.getName()+"</span><br>");
					builder.append("</td></tr>");
				   i++;
			   }
			    
		}
		
		
    	response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(builder.toString());
	} catch (Exception e) {

		e.printStackTrace();
	}
	finally {
		connection.close();
	}
	
	return null;
}

public String changepriscgivensrno() throws Exception{
	Connection connection = null;
	try{
		String templatename = request.getParameter("templatename");
		String srno = request.getParameter("srno");
		String gemdindex = request.getParameter("index");
		
		connection = Connection_provider.getconnection();
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		
		ArrayList<Priscription>priscList = (ArrayList<Priscription>)session.getAttribute("priscList");
		Priscription p =new Priscription();
		if(priscList!=null){
			p= (Priscription)priscList.get(Integer.parseInt(gemdindex));
			p.setSrno(srno);
			priscList.set(Integer.parseInt(gemdindex), p);
			session.setAttribute("priscList", priscList);
			
			if(templatename.equals("0")){
				getPriscriptionData();
			}else{
				getPriscriptionTemplateData();
			}
		}
		
	}catch (Exception e) {
		e.printStackTrace();
	}
	
	return null;
}

public String changepriscgivendays() throws Exception{
	Connection connection = null;
	try{
		String templatename = request.getParameter("templatename");
		String srno = request.getParameter("srno");
		String gemdindex = request.getParameter("index");
		
		connection = Connection_provider.getconnection();
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		
		ArrayList<Priscription>priscList = (ArrayList<Priscription>)session.getAttribute("priscList");
		Priscription p =new Priscription();
		if(priscList!=null){
			p= (Priscription)priscList.get(Integer.parseInt(gemdindex));
			p.setPriscdays(srno);
			priscList.set(Integer.parseInt(gemdindex), p);
			session.setAttribute("priscList", priscList);
			
			if(templatename.equals("0")){
				getPriscriptionData();
			}else{
				getPriscriptionTemplateData();
			}
		}
		
	}catch (Exception e) {
		e.printStackTrace();
	}
	
	return null;
}

public String changepriscgivendose() throws Exception{
	Connection connection = null;
	try{
		String templatename = request.getParameter("templatename");
		String srno = request.getParameter("srno");
		String gemdindex = request.getParameter("index");
		
		connection = Connection_provider.getconnection();
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		
		ArrayList<Priscription>priscList = (ArrayList<Priscription>)session.getAttribute("priscList");
		Priscription p =new Priscription();
		if(priscList!=null){
			p= (Priscription)priscList.get(Integer.parseInt(gemdindex));
			p.setPriscdose(srno);
			priscList.set(Integer.parseInt(gemdindex), p);
			session.setAttribute("priscList", priscList);
			
			if(templatename.equals("0")){
				getPriscriptionData();
			}else{
				getPriscriptionTemplateData();
			}
		}
		
	}catch (Exception e) {
		e.printStackTrace();
	}
	
	return null;
}


public String changepriscgivenqty() throws Exception{
	Connection connection = null;
	try{
		String templatename = request.getParameter("templatename");
		String qty = request.getParameter("qty");
		String gemdindex = request.getParameter("index");
		
		connection = Connection_provider.getconnection();
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		
		ArrayList<Priscription>priscList = (ArrayList<Priscription>)session.getAttribute("priscList");
		Priscription p =new Priscription();
		if(priscList!=null){
			p= (Priscription)priscList.get(Integer.parseInt(gemdindex));
			p.setPriscqty(qty);
			priscList.set(Integer.parseInt(gemdindex), p);
			session.setAttribute("priscList", priscList);
			
			if(templatename.equals("0")){
				getPriscriptionData();
			}else{
				getPriscriptionTemplateData();
			}
		}
		
	}catch (Exception e) {
		e.printStackTrace();
	}
	
	return null;
}

public String clinicalnotes() throws Exception {
	Connection connection=null;
	try {
		
		String ipdid=request.getParameter("ipdid");
		String clientid=request.getParameter("clientid");
		emrForm.setDiaryUser(DateTimeUtils.isNull(request.getParameter("diaryuser")));
		if(ipdid==null){
			ipdid="0";
		}else if(ipdid.equals("")){
			ipdid="0";
		}
		connection=Connection_provider.getconnection();
		DiagnosisDAO diagnosisDAO=new JDBCDiagnosisDAO(connection);
		IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		//int result=diagnosisDAO.addPatientIllness(ipdid,clientid);
		session.setAttribute("ipdid", ipdid);
		session.setAttribute("clientid",clientid);
	    //session.setAttribute("illnessid", result);
		ClientDAO clientDAO= new JDBCClientDAO(connection);
		Client client=clientDAO.getClientDetails(clientid);
		request.setAttribute("clientDetails", client);
		
	    ArrayList<Diagnosis> diagnosislist=diagnosisDAO.getAllClinicalDiagnosisList();
	    emrForm.setDiagnosislist(diagnosislist);
	    emrForm.setLastclinicalnotesid(diagnosislist.get(diagnosislist.size()-1).getId());
	    emrForm.setIpdid(ipdid);
	    BedDao bedDao = new JDBCBedDao(connection);
	    Bed bed=new  Bed();
	   
	    bed= bedDao.getEditIpdData(ipdid);
	    request.setAttribute("bed", bed);
	    
	    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	    Calendar cal = Calendar.getInstance();
	    String date = dateFormat.format(cal.getTime());  
	    request.setAttribute("date", date);
	    //Dietray
	    
	    DietaryDetailsDAO dietaryDetailsDAO = new JDBCDietaryDetailsDAO(connection);
	   /* ArrayList<Bed> bedList = ipdDAO.getAllBedListForDietary("0", loginInfo.getClinicid(),"");
	    emrForm.setBedlist(bedList);*/
	    //for Dietry
	    ArrayList<DietaryDetails> templatelist = dietaryDetailsDAO.getTemplateList(null);
	    emrForm.setTemplatelist(templatelist);
	    ArrayList<DietaryDetails> dietfeedlist = dietaryDetailsDAO.getDietFeedList();
	    emrForm.setDietfeedlist(dietfeedlist);
	    DietaryDAO dietaryDAO = new JDBCDietaryDAO(connection);
		ArrayList<Dietary> categorylist = dietaryDAO.getctaegory();
		emrForm.setDietarycategoryList(categorylist);
		ArrayList<DietaryDetails> dietplanlist = dietaryDetailsDAO.getDietPlanList();
		emrForm.setDietplanlist(dietplanlist);
		//ArrayList<Master> vitalMasterandValueList= ipdDAO.getDailyCareDataListandValues(ipdclientid, ipdid, "", "1");
		
		//For Vitals
		MasterDAO masterDAO= new JDBCMasterDAO(connection);
		ArrayList<Master> vitalList=masterDAO.getallVitalMasterdata("1");
		emrForm.setVitalList(vitalList);
		
		ArrayList<Master> nursingcategorylist=masterDAO.getAllNursingCategory(null);
		emrForm.setNursingcategorylist(nursingcategorylist);
		
		
		
		
	} catch (Exception e) {

	   e.printStackTrace();
	}
	finally {
		connection.close();
	}
	
	return "clinicalnotes";
}


public String changepriscgivenremark() throws Exception{
	Connection connection = null;
	try{
		String templatename = request.getParameter("templatename");
		String reamrk = request.getParameter("reamrk");
		String gemdindex = request.getParameter("index");
		
		connection = Connection_provider.getconnection();
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		
		ArrayList<Priscription>priscList = (ArrayList<Priscription>)session.getAttribute("priscList");
		Priscription p =new Priscription();
		if(priscList!=null){
			p= (Priscription)priscList.get(Integer.parseInt(gemdindex));
			p.setPriscindivisualremark(reamrk);
			priscList.set(Integer.parseInt(gemdindex), p);
			session.setAttribute("priscList", priscList);
			
			if(templatename.equals("0")){
				getPriscriptionData();
			}else{
				getPriscriptionTemplateData();
			}
		}
		
	}catch (Exception e) {
		e.printStackTrace();
	}
	
	return null;
}

public String savechildparentpriscdata(){

	Connection connection = null;
	try{
		
		connection = Connection_provider.getconnection();
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
		int saveparent = (Integer)session.getAttribute("savetemplateparent");
		//int saveparent = Integer.parseInt(DateTimeUtils.numberCheck(savetemplateparent));
		if(saveparent!=0){
			Priscription priscriptionnew = emrDAO.getPriscriptionParentData(saveparent);
			String date = "";
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			date = dateFormat.format(cal.getTime());
			priscriptionnew.setDate(date);
			priscriptionnew.setUserid(loginInfo.getUserId());
			int directtransfer =1;
			if(!loginInfo.isDirect_prisc()){
				directtransfer = 0;
			}
			int newparentid = emrDAO.saveParentPriscNew(priscriptionnew,"",""+saveparent,priscriptionnew.getDefault_location(),directtransfer);
			ArrayList<Priscription> arrayList= emrDAO.getPriscriptionChildData(""+saveparent,0);
			for(Priscription priscription2 : arrayList){
				String mdrequestqty = priscription2.getPriscqty();
				String mdname = priscription2.getMdicinenametxt();
				String mdid = priscription2.getMdicinenameid();
				String childid =""+priscription2.getId();
				String newchild = childid;
				int res = emrDAO.saveChildPriscNew(priscription2,mdrequestqty,""+saveparent,childid,newparentid);
			}
		}
	}catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}

public String deleteprisctempall(){
	
	Connection connection = null;
	try{
		
		String clientId = request.getParameter("clientId");
		String prectionerid = request.getParameter("prectionerid");
		String conditionid = request.getParameter("conditionid");
		String templatename = request.getParameter("templatename");
		
		ArrayList<Priscription>priscList = (ArrayList<Priscription>)session.getAttribute("priscList");
		
		if(priscList!=null){
			priscList.removeAll(priscList);
		}
	
		if(templatename.equals("0")){
			if(loginInfo.getOutoprisc()==1){
				getNimaiPriscriptionTemplateData();
			}else{
				getPriscriptionData();
			}
		}else{
			getPriscriptionTemplateData();
		}		
	}catch(Exception e){
		e.printStackTrace();
	}
	return null;
}



}


