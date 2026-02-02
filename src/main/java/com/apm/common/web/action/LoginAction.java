 package com.apm.common.web.action;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.RollingFileAppender;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.apm.Accounts.eu.bi.AccountsDAO;
import com.apm.Accounts.eu.blogic.jdbc.JDBCAccountsDAO;
import com.apm.Appointment.eu.bi.BranchDAO;
import com.apm.common.eu.blogic.jdbc.Connection_provider;
import com.apm.Appointment.eu.blogic.jdbc.JDBCBranchDAO;
import com.apm.Appointment.eu.entity.Branch;
import com.apm.common.web.common.helper.Access;
import com.apm.common.web.common.helper.LoginHelper;
import com.apm.common.web.common.helper.LoginInfo;
import com.apm.common.web.utils.PopulateList;
import com.apm.main.common.constants.Constants;
import com.apm.common.utils.DateTimeUtils;
import com.apm.common.utils.Encryption;
import com.apm.sendemail.java4s.DBScheduler;
import com.apm.Appointment.web.form.BranchForm;
import com.apm.AssesmentForms.eu.bi.AssessmentFormDAO;
import com.apm.AssesmentForms.eu.blogic.jdbc.JDBCAssessmentFormDAO;
import com.apm.AssesmentForms.eu.entity.Assessment;
import com.apm.DiaryManagement.eu.bi.ClientDAO;
import com.apm.DiaryManagement.eu.bi.DiaryManagementDAO;
import com.apm.DiaryManagement.eu.bi.NotAvailableSlotDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCClientDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCDiaryManagentDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCNotAvailableSlotDAO;
import com.apm.DiaryManagement.eu.entity.Priscription;
import com.apm.DiaryManagement.web.action.SmsService;
import com.apm.Log.eu.blogic.jdbc.JDBCAccountLogDAO;
import com.apm.Log.eu.entity.EmailLetterLog;
import com.apm.Pharmacy.eu.bi.PharmacyDAO;
import com.apm.Pharmacy.eu.blogic.jdbc.JDBCPharmacyDAO;
import com.apm.Registration.eu.bi.ClinicDAO;
import com.apm.Registration.eu.bi.UserProfileDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCClinicDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCUserProfileDAO;
import com.apm.Registration.eu.entity.Clinic;
import com.apm.Registration.eu.entity.UserProfile;
import com.apm.Tools.eu.bi.EmailTemplateDAO;
import com.apm.Tools.eu.blogic.jdbc.JDBCEmailTemplateDAO;
import com.apm.Tools.eu.entity.EmailTemplate;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;




public class LoginAction extends BaseAction implements ModelDriven<BranchForm> {
	
	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
	HttpServletResponse response = (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
	HttpSession session = request.getSession(true);
	BranchForm branchForm = new BranchForm();
	
	private static final Logger log = Logger.getLogger(LoginAction.class);
	
	@SkipValidation
	public String doctmob() throws SQLException{
		
		String actionType = request.getParameter("actionType");
		if(actionType.equals("doctormob")){
			String uid = request.getParameter("uid");
			String pass = request.getParameter("pass");
			
			branchForm.setUserId(uid);
			branchForm.setPassword(pass);
			branchForm.setActiontype(actionType);
			branchForm.setDbType("0");
			
			//session.removeAttribute("dbTypes");
			LoginHelper.removeLoginInfo(request);
		}
		
		
		return execute();
	}
	
	public String execute() throws SQLException{
		Connection connection = null;
		String url = "";
		try{
			
			String opendb=(String)session.getAttribute("openedb");
			
			if(opendb==null){
				
				session.setAttribute("openedb", "opd");
			}
			
			String sessionid = session.getId();
			System.out.println(sessionid);
			
			
			session.setAttribute("dbTypes", Integer.parseInt(branchForm.getDbType()));
			
			connection = Connection_provider.getconnection();
			
			DatabaseMetaData dmd = connection.getMetaData();
			 url = dmd.getURL() + " username = " + dmd.getUserName();
			System.out.println(url);
			log.debug("@@@@@@@@@@"+ url );
		
		
			LoginInfo loginInfo = new LoginInfo();
			loginInfo.setLoginType("pc");
//			connection = Connection_provider.getconnection();
			
			ClinicDAO clinicDAO=new JDBCClinicDAO(connection);
			
			
		String existUserid = "";
		boolean checkuserexist = clinicDAO.checkLoginUserExist(branchForm.getUserId());
		if(checkuserexist){
			 existUserid = clinicDAO.getExistUserId(branchForm.getUserId());
		}else{
			existUserid = "";
		}
		branchForm.setUserId(existUserid);
		
		
		boolean loginstatus = clinicDAO.getLoginStatus(branchForm.getUserId());
		if(loginstatus){
			addActionError(getText("error.userid.loggedin"));
			return INPUT;
		}
		
		Clinic clinic = clinicDAO.getuser(branchForm.getUserId());
		loginInfo.setCountry(clinic.getCountry());
		loginInfo.setIslogo(clinic.getIslogo());
		//loginInfo.setAcaccess(clinic.getAcaccess());
		
	
		
		if(!branchForm.getUserId().equals(clinic.getUserId()) ){
			addActionError(getText("error.userid.notexist"));
			return INPUT;
		}
		String encPassword = Encryption.encryptSHA(branchForm.getPassword());
		if(!encPassword.equals(clinic.getPassword())){
			addActionError(getText("error.user.authfailed"));
			return INPUT;
		}
		
		
		
		if(!branchForm.getUserId().equals("admin")){
			
			// update login status
			//int res = clinicDAO.updateLoginStatus(branchForm.getUserId());
			
			if(clinic.getUserType()==4){
				//get country for practitoner
				String country = clinicDAO.getCountryForPractitoner(clinic.getClinicID());
				loginInfo.setCountry(country);
				connection = DriverManager.getConnection(""+Constants.DB_HOST+":3306/"+clinic.getClinicID()+"","pranams","6qxi5x&)~XBZ");
				loginInfo.setClinicUserid(clinic.getClinicID());
				
			}else{
				connection = DriverManager.getConnection(""+Constants.DB_HOST+":3306/"+branchForm.getUserId()+"","manasyuvi","M@n@S1928YUVI#$@%");
				//connection = DriverManager.getConnection(":3306/"+branchForm.getUserId()+"","root","mysql");
				loginInfo.setClinicUserid(branchForm.getUserId());
			}
			clinicDAO = new JDBCClinicDAO(connection);
			clinic = clinicDAO.getuser(branchForm.getUserId());
			loginInfo.setIslogo(clinic.getIslogo());
		}
		Clinic clinic5 = new Clinic();
		if(loginInfo.getWarningmsg()==null){
			loginInfo.setWarningmsg("");
		}
		clinic5=clinicDAO.getLoginExpiry();
		if(clinic5.getLe_date()!=null){
			if(!clinic5.getLe_date().equals("")){
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
	 			   String today = dateFormat.format(cal.getTime()); 
	 			  long diff=DateTimeUtils.getDifferenceOfTwoDateDBFormat(today, clinic5.getLe_date());
	 			 
	 			   if(diff<20 && diff>0){
	 				   if(clinic5.getLe_msg()!=null){
	 					   if(!clinic5.getLe_msg().equals("")){
	 						   loginInfo.setWarningmsg(clinic5.getLe_msg());
	 					   }
	 					  else{
		 					   loginInfo.setWarningmsg(" "+diff+""+" Day Remains To Expire Your Software");
		 				   }
	 				   }else{
	 					   loginInfo.setWarningmsg(" "+diff+""+" Day Remains To Expire Your Software");
	 				   }
	 				 
	 			   }else if(diff<=0 ){
	 				   return "expiryerror";
	 			   }
				
			}
		}
		
		//set time zone
		loginInfo.setTimeZone("Europe/London");
		if(loginInfo.getCountry()!=null){
			if(loginInfo.getCountry().equals("India")){
				loginInfo.setTimeZone("IST");
			}
			if(loginInfo.getCountry().equals("London")){
				loginInfo.setTimeZone("Europe/London");
			}
		}
		
		
		HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
		HttpSession session = request.getSession(true);
		
		loginInfo.setId(clinic.getId());
		loginInfo.setUserId(clinic.getUserId());
		loginInfo.setFirstName(clinic.getFirstName());
		loginInfo.setLastName(clinic.getLastName());
		loginInfo.setClinicOwner(clinic.getClinicOwner());
		loginInfo.setUserType(clinic.getUserType());
		
		
		loginInfo.setDbName(clinic.getUserId());
		if(loginInfo.getUserType()==4){
			String userid = clinicDAO.getOtherUserID(clinic.getClinicID());
			loginInfo.setDbName(userid);
		}
		
		
		
		//EmailConfigure Setting
		EmailTemplateDAO emailTemplateDAO = new JDBCEmailTemplateDAO(connection);
    	EmailTemplate emailTemplate = new EmailTemplate();
		/*
		 * emailTemplate = emailTemplateDAO.getEmailConfigureDetails(clinic.getId());
		 * loginInfo.setEmailConfigureId(emailTemplate.getEmailConfigureId());
		 * loginInfo.setEmailUserName(emailTemplate.getEmailUserName());
		 * loginInfo.setEmailPassword(emailTemplate.getEmailPassword());
		 * loginInfo.setEmailHostName(emailTemplate.getEmailHostName());
		 */
		
		//Menu Setting
		Clinic clinic2 = new Clinic();
		if(clinic.getUserType()==4){
			int clinicId = clinicDAO.getClinicId(clinic.getId());
			clinic2 = clinicDAO.getCliniclistDetails(clinicId);	
			loginInfo.setClinicid(clinicId);
			
		}
		else if(clinic.getUserType()==2){
			clinic2 = clinicDAO.getCliniclistDetails(clinic.getId());
			loginInfo.setClinicid(clinic.getId());
		}
		//Hospital wise access
		Clinic clinic3= new Clinic();
		clinic3= clinicDAO.getclinicNewHospitalAccess();
		if(!clinic3.isActive_clinic()){
			return "expiryerror";
			
		}
		loginInfo.setDisc_approve_sms(clinic3.isDisc_approve_sms());
		loginInfo.setHidecalinpoprint(clinic3.isHidecalinpoprint());
		NotAvailableSlotDAO notAvailableSlotDAO= new JDBCNotAvailableSlotDAO(connection);
		int vacinator=notAvailableSlotDAO.getVaccinator();
		loginInfo.setInvoice_default_note(clinic3.getInvoice_default_note());
		loginInfo.setDeleted_invst_charge(clinic3.isDeleted_invst_charge());
		loginInfo.setOpd_video_icon_show(clinic3.isOpd_video_icon_show());
		loginInfo.setEmr_vitals_show(clinic3.isEmr_vitals_show());
		loginInfo.setGrn_to_prisc_location(clinic3.getGrn_to_prisc_location());
		loginInfo.setNew_aureus_discard(clinic3.isNew_aureus_discard());
		loginInfo.setSms_senderid(clinic3.getSms_senderid());
		loginInfo.setRelease_notes_upload(clinic3.isRelease_notes_upload());
		loginInfo.setBarcode_productname_show(clinic3.isBarcode_productname_show());
		loginInfo.setDischarge_msg_hs(clinic3.isDischarge_msg_hs());
		loginInfo.setPhar_print_seq(clinic3.isPhar_print_seq());
		loginInfo.setOpening_closeing_on(clinic3.isOpening_closeing_on());
		loginInfo.setOpening_locations(clinic3.getOpening_locations());
		loginInfo.setPrachi_clinic(clinic3.isPrachi_clinic());
		loginInfo.setVacinator(vacinator);
		
		loginInfo.setDirect_prisc(clinic3.isDirect_prisc());
		loginInfo.setPrisc_location_list(clinic3.isPrisc_location_list());
		loginInfo.setPrisc_print(clinic3.isPrisc_print());
		loginInfo.setAuto_generic_name(clinic3.isAuto_generic_name());
		loginInfo.setPrisc_deliver_return(clinic3.isPrisc_deliver_return());
		loginInfo.setJson_diagnosis(clinic3.isJson_diagnosis());
		loginInfo.setAddress_manual(clinic3.getAddress_manual());
		loginInfo.setPatientname_field(clinic3.getPatientname_field());
		loginInfo.setPractitonername_field(clinic3.getPractitonername_field());
		/*loginInfo.setDirect_refund_disc(clinic3.isDirect_refund_disc());*/
		loginInfo.setIsledgerhosp(clinic3.isIsledgerhosp());
		loginInfo.setInvest_order(clinic3.isInvest_order());
		loginInfo.setDemo_access(clinic3.isDemo_access());
		loginInfo.setDischarge_validation(clinic3.getDischarge_validation());
		loginInfo.setOpd_payamnt_sms(clinic3.isOpd_payamnt_sms());
		loginInfo.setIpd_payamnt_sms(clinic3.isIpd_payamnt_sms());
		loginInfo.setRefund_payamnt_sms(clinic3.isRefund_payamnt_sms());
		loginInfo.setAdv_payamnt_sms(clinic3.isAdv_payamnt_sms());
		loginInfo.setOther_payamnt_sms(clinic3.isOther_payamnt_sms());
		loginInfo.setNewdischargecard(clinic3.isNewdischargecard());
		loginInfo.setInvoice_date_modify(clinic3.isInvoice_date_modify());
		loginInfo.setOt_patient_sms(clinic3.isOt_patient_sms());
		loginInfo.setOt_surgeon_sms(clinic3.isOt_surgeon_sms());
		UserProfileDAO userProfileDAOxml= new JDBCUserProfileDAO(connection);
		loginInfo.setSupport_Access(userProfileDAOxml.supportAccess(loginInfo.getUserId()));
		
		//xml access
		String x=request.getRealPath("/");
		System.out.println(x);
		Access xmlaccess=userProfileDAOxml.getXMLAccess(loginInfo.getClinicUserid(),x);
		loginInfo.setInvestigation_newaccess(xmlaccess.getInvestigation_newaccess());
		//Indivisual access
		UserProfile individualaccess=userProfileDAOxml.getIndivdualAccess(loginInfo.getUserId());
		loginInfo.setMax_phar_discount(individualaccess.isMax_phar_discount());
		loginInfo.setChange_indent_product(individualaccess.isChange_indent_product());
		loginInfo.setSuperadminid(individualaccess.getId());
		loginInfo.setProc_after_stock(individualaccess.isProc_after_stock());
		loginInfo.setCancel_invoice_new(individualaccess.isCancel_invoice_new());
		loginInfo.setUpdate_investigation_charge(individualaccess.isUpdate_investigation_charge());
		loginInfo.setPaymentReport(individualaccess.isPaymentReport());
		loginInfo.setRef_dis_pay(individualaccess.isRef_dis_pay());
		loginInfo.setAdd_manual_charge(individualaccess.isAdd_manual_charge());
		loginInfo.setInvoicemodify(individualaccess.isInvoicemodify());
		loginInfo.setAdditional_disc(individualaccess.isAdditional_disc());
		loginInfo.setIndv_discount(individualaccess.isIndv_discount());
		loginInfo.setPayrollaccess(individualaccess.isPayrollaccess());
		loginInfo.setShow_hospital_admin(clinic3.isShow_hospital_admin());
		loginInfo.setDirect_ipd(clinic3.isDirect_ipd());
		loginInfo.setDrwise_ipd(clinic3.isDrwise_ipd());
		loginInfo.setJobtitlewise_investigation(clinic3.isJobtitlewise_investigation());
//		loginInfo.setShow_wardname(clinic3.isShow_wardname());
		loginInfo.setCriticalvaluesms(clinic2.isCriticalvaluesms());
		loginInfo.setMedtreatgiven(clinic2.isMedtreatgiven());
		//for new req of discharge from in kunal Hospital
		loginInfo.setDischarge_new(clinic3.isDischarge_new());
	//set clinic start and end time
		if(clinic.getUserType()!=1){
			DiaryManagementDAO diaryManagementDAO = new JDBCDiaryManagentDAO(connection);
			Clinic csetting = diaryManagementDAO.getClinicStartAndEndTime(loginInfo.getClinicid());
			String temp[] = csetting.getStarttime().split(":");
			loginInfo.setClinicStartTime(Integer.parseInt(temp[0]));
			String temp1[] = csetting.getEndtime().split(":");
			loginInfo.setClinicEndTime(Integer.parseInt(temp1[0]));
			
			loginInfo.setDbsize(csetting.getDbsize());
		}
	//access for invoice group by and invoice seq no
		loginInfo.setInvoice_charge_seqno(clinic3.isInvoice_charge_seqno());
		loginInfo.setInvoice_groupby(clinic3.isInvoice_groupby());
		//access while take payment from opd of tp patient then received amount should 0
		loginInfo.setOpd_tp_zero_invoice(clinic3.isOpd_tp_zero_invoice());
		
			//set clinic registerd address
			loginInfo.setRegAddress(clinic2.getAddress());
			loginInfo.setRegLocation(clinic2.getLocationname());
			loginInfo.setRegPinCode(clinic2.getPinCode());
			loginInfo.setRegContactNo(clinic2.getContactNo());
		//shubham 14/12/2018 show option for posted invoice
			loginInfo.setShow_unpost(clinic3.isShow_unpost());
			//shubham 20/12/2018 sms on bed change and new admission for dr
			loginInfo.setSms_on_bedchange(clinic3.isSms_on_bedchange());
			loginInfo.setSms_on_newadm(clinic3.isSms_on_newadm());
			loginInfo.setIpd_abbr_access(clinic3.getIpd_abbr_access());
			loginInfo.setHidelogoinvst(clinic3.isHidelettinvst());
			loginInfo.setHidelogoemr(clinic3.isHidelettemr());
			loginInfo.setHidelogobillinv(clinic3.isHidelettbillinv());
			loginInfo.setInvst_inv_apr(clinic3.isInvst_inv_apr());
			loginInfo.setBalgopal(clinic3.isBalgopal());
			loginInfo.setPrisc_savenprint(clinic3.isPrisc_savenprint());
			loginInfo.setInvest_savenprint(clinic3.isInvest_savenprint());
			loginInfo.setPackage_access(clinic3.isPackage_access());
			//set balgopal address
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
			Clinic ccbg =  locationAdressList.get(0);
			session.setAttribute("balgopaladdress", ccbg.getAddress());
			session.setAttribute("balgopalcname", ccbg.getClinicName());
			
		/*	loginInfo.setDiaryManagement(clinic2.isDiaryManagement());
			loginInfo.setAppointmentBooking(clinic2.isAppointmentBooking());
			loginInfo.setBasicFinance(clinic2.isBasicFinance());
			loginInfo.setFullFinance(clinic2.isFullFinance());
			loginInfo.setMedicalRecord(clinic2.isMedicalRecord());
			loginInfo.setClinicResourceMngment(clinic2.isClinicResourceMngment());
			loginInfo.setClinicPayrollMngment(clinic2.isClinicPayrollMngment());
			loginInfo.setCommunication(clinic2.isCommunication());
			loginInfo.setReport(clinic2.isReport());
			loginInfo.setAssessmentForms(clinic2.isAssessmentForms());
			loginInfo.setDesktop(clinic2.isDesktop());
			loginInfo.setiOS(clinic2.isIOS());
			loginInfo.setMobile(clinic2.isMobile());
			loginInfo.setTablet(clinic2.isTablet());*/
		
			loginInfo.setClinicName(clinic2.getClinicName());
			loginInfo.setClinicAddress(clinic2.getAddress());
			loginInfo.setClinicLogo(clinic2.getUserImageFileName());
			PharmacyDAO pharmacyDAO= new JDBCPharmacyDAO(connection);
			
			Priscription priscription =pharmacyDAO.getPharacyUsrLInfo(loginInfo.getUserId());
			
		    loginInfo.setPharmacyUserType(priscription.getPharmacyUserType());
		    loginInfo.setPurchase_edit_pharmacy(priscription.isPurchase_edit());
		    loginInfo.setCancel_po(priscription.getCancel_po());
		    
		    
		    
		    UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
		    //MIS access
			
			  Access accessmis=userProfileDAO.getMisRoleaccess(loginInfo.getUserId());
			  
			  loginInfo.setPractioner_share(accessmis.isPractioner_share());
			  loginInfo.setOpd_practioner_share(accessmis.isOpd_practioner_share());
			  loginInfo.setCharges(accessmis.isCharges());
			  loginInfo.setInvoice(accessmis.isInvoice());
			  loginInfo.setPayment_report_detailed(accessmis.isPayment_report_detailed());
			  loginInfo.setPayment_report_small(accessmis.isPayment_report_small());
			  loginInfo.setAdd_debtors(accessmis.isAdd_debtors());
			  loginInfo.setCa(accessmis.isCa());
			  loginInfo.setAuditors(accessmis.isAuditors());
			  loginInfo.setUserwise_payment(accessmis.isUserwise_payment());
			  loginInfo.setDeptwise_analysis(accessmis.isDeptwise_analysis());
			  loginInfo.setCharges_share(accessmis.isCharges_share());
			  loginInfo.setBilling(accessmis.isBilling());
			  loginInfo.setDiscount(accessmis.isDiscount());
			  loginInfo.setCancel_invoice(accessmis.isCancel_invoice());
			  loginInfo.setPayment(accessmis.isPayment());
			  loginInfo.setKpi_dashboard(accessmis.isKpi_dashboard());
			  loginInfo.setTreatment_episode_list(accessmis.isTreatment_episode_list());
			  loginInfo.setPatient_condition_list(accessmis.isPatient_condition_list());
			  loginInfo.setDtr_report(accessmis.isDtr_report());
			  loginInfo.setPatientlist(accessmis.isPatientlist());
			  loginInfo.setCurrent_track_with_no_future_ampts(accessmis.
			  isCurrent_track_with_no_future_ampts());
			  loginInfo.setNo_appointment_activity_record(accessmis.
			  isNo_appointment_activity_record());
			  loginInfo.setDna_with_no_future_appointment(accessmis.
			  isDna_with_no_future_appointment());
			  loginInfo.setNo_activity_record(accessmis.isNo_activity_record());
			  loginInfo.setDna_analysiis(accessmis.isDna_analysiis());
			  loginInfo.setAppointment_kept_vs_dna(accessmis.isAppointment_kept_vs_dna());
			  loginInfo.setTreatment_state_by_refferal(accessmis.
			  isTreatment_state_by_refferal());
			  loginInfo.setReturning_patients(accessmis.isReturning_patients());
			  loginInfo.setOutcome_discharge(accessmis.isOutcome_discharge());
			  loginInfo.setDeathreport(accessmis.isDeathreport());
			  loginInfo.setCurrent_patient_report(accessmis.isCurrent_patient_report());
			  loginInfo.setIpd_daily_report(accessmis.isIpd_daily_report());
			  loginInfo.setIpd_monthly_report(accessmis.isIpd_monthly_report());
			  loginInfo.setBed_occupancy_report(accessmis.isBed_occupancy_report());
			  loginInfo.setReffered_by(accessmis.isReffered_by());
			  loginInfo.setMlc(accessmis.isMlc());
			  loginInfo.setReport_outstandng(accessmis.isReport_outstandng());
			  loginInfo.setNow_patients(accessmis.isNow_patients());
			  loginInfo.setTotal_patients_seen(accessmis.isTotal_patients_seen());
			  loginInfo.setDna_outstanding_action(accessmis.isDna_outstanding_action());
			  loginInfo.setSales_report(accessmis.isSales_report());
			  loginInfo.setPayment_recive(accessmis.isPayment_recive());
			  loginInfo.setInventory_opening(accessmis.isInventory_opening());
			  loginInfo.setItemwise_stock(accessmis.isItemwise_stock());
			  loginInfo.setPurchase_report(accessmis.isPurchase_report());
			  loginInfo.setExpiry_medicine_report(accessmis.isExpiry_medicine_report());
			  loginInfo.setGrn(accessmis.isGrn());
			  loginInfo.setIndent_statement(accessmis.isIndent_statement());
			  loginInfo.setIpd_daily_discharge(accessmis.isIpd_daily_discharge());
			  loginInfo.setOpd_ipd_cancel_refund(accessmis.isOpd_ipd_cancel_refund());
			  loginInfo.setIpd_bill_register(accessmis.isIpd_bill_register());
			  loginInfo.setService_register_details(accessmis.isService_register_details())
			  ; loginInfo.setCancel_invoice_report(accessmis.isCancel_invoice_report());
			  loginInfo.setKPI_report(accessmis.isKPI_report());
			 
			  
		    
		    
		    
		   
		    //lokesh
		    Priscription prs= new Priscription();
		    prs=userProfileDAO.getpharmaAdminaccess();
		    
		    loginInfo.setIsdotmatrix(prs.isIsdotmatrix());
		session.setAttribute("logininfo", loginInfo);
		LoginHelper.saveLoginInfo(request, loginInfo);		
		
		String checkValue = clinicDAO.IsMailSend(clinic.getId());
		branchForm.setCheckMailSend(checkValue);
		
		
		//show admin for all user other than practitioner
		
		int duserid  = userProfileDAO.getDiaryUserId(loginInfo.getUserId());
		UserProfile userProfile = userProfileDAO.getUserprofileDetails(duserid);
		
		  loginInfo.setPrisc_new_req_access(userProfile.isPrisc_new_req_access());
		  loginInfo.setDirect_refund_disc(userProfile.isDirect_refund_disc());
		  loginInfo.setTreatment_episode_acc(userProfile.isTreatmentacc());
		  loginInfo.setSupplier_add(userProfile.isSupplier_add());
		  loginInfo.setAdjustmentaccess(userProfile.isAdjustmentaccess());
		  loginInfo.setAcaccess(userProfile.getAcaccess());
		  loginInfo.setEdit_paypo(userProfile.isEdit_paypo());
		  loginInfo.setEdit_invst_charge(userProfile.isEdit_invst_charge());
		  loginInfo.setStock_log(clinic2.isStock_log()); //lokesh
		  loginInfo.setFullname(userProfile.getFullname());
		  loginInfo.setBdaysms(clinic2.isBdaysms());
		  loginInfo.setImmusms(clinic2.isImmusms());
		  loginInfo.setF_diagnosis_discharge(clinic2.isF_diagnosis_discharge());
		  loginInfo.setSeq_no_gen(clinic2.isSeq_no_gen());
		  loginInfo.setRemoveprocurement(clinic2.isRemoveprocurement());
		  loginInfo.setModify_disc(clinic2.isModify_disc());
		 
		loginInfo.setUserMobileNo(userProfile.getMobile());
		//shubham
		loginInfo.setSmsVisitingConslt(clinic2.isSmsVisitingConslt());
		loginInfo.setShow_wardname(clinic2.isShow_wardname());
		//connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/admin","pranams","6qxi5x&)~XBZ");
		userProfileDAO = new JDBCUserProfileDAO(connection);
		Access access = userProfileDAO.getRoleAccess(userProfile.getJobtitle());
		
		//Access access = userProfileDAO.getUserRoleAccess(loginInfo.getUserId());
		loginInfo.setMedicine_barcode(access.isMedicine_barcode());
		
		
		  loginInfo.setPharm_print_backdate(access.isPharm_print_backdate());
		  loginInfo.setDiaryManagement(access.isDiarymanagement());
		  loginInfo.setAppointmentBooking(access.isAppointmentbooking());
		 
		
		  loginInfo.setBasicFinance(access.isBasicfinance());
		  loginInfo.setFullFinance(access.isFullfinance());
		  loginInfo.setMedicalRecord(access.isMedicalrecord());
		  loginInfo.setCommunication(access.isCommunication());
		 
		  loginInfo.setReport(access.isReport());
		 loginInfo.setAssessmentForms(access.isAssessmentForm());
			
			  loginInfo.setManageclient(access.isManageclient());
			  loginInfo.setManageclinic(access.isManageclinic());
			  loginInfo.setManagemaster(access.isManagemaster());
				
				  loginInfo.setManageprisc(access.isManageprisc());
				  loginInfo.setManageinvst(access.isManageinvst());
				  loginInfo.setManageipd(access.isManageipd());
				 
				
			loginInfo.setJobTitle(userProfile.getJobtitle());
			loginInfo.setManageopd(access.isManageopd());
				 
			  loginInfo.setManageemr(access.isManageemr());
			  loginInfo.setExpences(access.isExpences());
			  loginInfo.setPayroll(access.isPayroll());
			  loginInfo.setBloodbak(access.isBloodbak());
			  loginInfo.setInventory(access.isInventory());
			  loginInfo.setDischarge(access.isDischarge());
			  loginInfo.setManagemis(access.isManagemis());
			  loginInfo.setApmtfinder(access.isApmtfinder());
			  loginInfo.setPacks(access.isPacks());
			 
		  loginInfo.setInvestigation_chart(access.isInvestigation_chart());
		  loginInfo.setSheduler(access.isSheduler());
		  loginInfo.setHousekeeping(access.isHousekeeping());
		  loginInfo.setDietery(access.isDietery());
		  loginInfo.setCafeteria(access.isCafeteria());
		  loginInfo.setPackages(access.isPackages());
		  loginInfo.setAmbulance(access.isAmbulance());
		  loginInfo.setBank_deposite(access.isBank_deposite());
		  loginInfo.setAccount_reconcilation(access.isAccount_reconcilation());
		  loginInfo.setCathlab(access.isCathlab()); //set ot, casualty, pharmacy, mrd,
		//  marketing, voice_recorder in main dashboard loginInfo.setOt(access.isOt());
		  loginInfo.setCasualty(access.isCasualty()); loginInfo.setMrd(access.isMrd());
		  loginInfo.setMarketing(access.isMarketing());
		  loginInfo.setVoice_recording(access.isVoice_recording());
		  loginInfo.setIndent(access.isIndent());
		  loginInfo.setPharmacy(access.isPharmacy());
		  loginInfo.setInvestigation_approve(access.isInvestigation_approve());
		  loginInfo.setDaily_opd(access.isDaily_opd());
		  loginInfo.setIndent_approve(access.isIndent_approve());
		  loginInfo.setTpa(access.isTpa());
		  loginInfo.setNabh_quality(access.isNabh_quality());
		  loginInfo.setDoctor_opd(access.isDoctor_opd());
		  loginInfo.setInvst_collect(access.isInvst_collect());
		  loginInfo.setToken_display(access.isToken_display());
		  loginInfo.setAdd_medicine(individualaccess.isAdd_medicine());
		  loginInfo.setMyhr(access.isMyhr()); loginInfo.setDaycare(access.isDaycare());
		  loginInfo.setEmergency_lbl(access.isEmergency_lbl()); //refund and discount
		 // dashboard loginInfo.setRefund_dashboard(userProfile.isRefund_dashboard());
		 
		loginInfo.setShowinvestigation(userProfile.isShowinvestigation());
		
		if(userProfile.getJobtitle().equals("Admin")){
			clinic.setUserType(2);
			loginInfo.setUserType(2);
		}
		
		
		//get pacs ip address
		
		  String pacsip = clinicDAO.getPacsIpAddress(loginInfo.getClinicUserid());
		  loginInfo.setPacsip(pacsip);
		 
		
		
		
		//get outo prisc
		int outoprisc = clinicDAO.getoutoprisc(loginInfo.getClinicUserid());
		loginInfo.setOutoprisc(outoprisc);
		
		int wardforcharge = clinicDAO.getwardforcharge(loginInfo.getClinicUserid());
		loginInfo.setWardforcharge(wardforcharge);
		
		int iskunal = clinicDAO.getIsKunal(loginInfo.getClinicUserid());
		int pharmacypagelimit=clinicDAO.getPharmacyPagelimit();
		loginInfo.setPagelimitpharmacy(pharmacypagelimit);
		loginInfo.setIskunal(iskunal);
		int gymsms = clinicDAO.getgymsms(loginInfo.getClinicUserid());
		loginInfo.setGymsms(gymsms);
		
		
		//get clinic ip address
		String ipadd = clinicDAO.getClinicIpAddress(loginInfo.getClinicUserid());
		Access globalaccess = clinicDAO.getGlobalAccessAccess(userProfile.getJobtitle());
		
		if(ipadd!=null){
			String remoteaddress = request.getRemoteAddr();
			if(!globalaccess.isGlobalaccess()){
				if(!remoteaddress.equals(ipadd)){
					LoginHelper.removeLoginInfo(request);
					return "noglobalaccess";
				}
			}
		}
		
		boolean isshowdiscount = userProfileDAO.checkisshowdiscount(loginInfo.getClinicid());
		
		loginInfo.setShowdiscount(isshowdiscount);
		
		loginInfo.setPharm_print_backdate(priscription.isPharm_print_backdate());
		loginInfo.setCreditlimit(priscription.getCreditlimit());
		loginInfo.setCreditlimitaccess(priscription.isCreditlimitaccess());
		//set indivusial access
		
		
		
		//set opdrollaccess
		/*
		 * Access opdaccess =
		 * userProfileDAO.getopdRollAccess(userProfile.getJobtitle());
		 * loginInfo.setOpd_modify(opdaccess.isOpd_modify());
		 * loginInfo.setOpd_cancel(opdaccess.isOpd_cancel());
		 * loginInfo.setOpd_prescription(opdaccess.isOpd_prescription());
		 * loginInfo.setOpd_investigation(opdaccess.isOpd_investigation());
		 * loginInfo.setOpd_ot(opdaccess.isOpd_ot());
		 * loginInfo.setOpd_viewaccount(opdaccess.isOpd_viewaccount());
		 * loginInfo.setOpd_apmtfinder(opdaccess.isOpd_apmtfinder());
		 * loginInfo.setOpd_advandref(opdaccess.isOpd_advandref());
		 * loginInfo.setOpd_modifydiagnosis(opdaccess.isOpd_modifydiagnosis());
		 * loginInfo.setOpd_editpatient(opdaccess.isOpd_editpatient());
		 * loginInfo.setOpd_log(opdaccess.isOpd_log());
		 * loginInfo.setOpd_emr(opdaccess.isOpd_emr());
		 * loginInfo.setOpd_assessmentform(opdaccess.isOpd_assessmentform());
		 * loginInfo.setOpd_treatment(opdaccess.isOpd_treatment());
		 * loginInfo.setOpd_addcharges(opdaccess.isOpd_addcharges());
		 * loginInfo.setOpd_createinvoice(opdaccess.isOpd_createinvoice());
		 * loginInfo.setOpd_recordpayment(opdaccess.isOpd_recordpayment());
		 */
		
		
		
		/*
		 * Access ipdaccess = userProfileDAO.getipdaccesss(userProfile.getJobtitle());
		 * 
		 * loginInfo.setIpd_admission(ipdaccess.isIpd_admission());
		 * loginInfo.setIpd_declaration(ipdaccess.isIpd_declaration());
		 * loginInfo.setIpd_log(ipdaccess.isIpd_log());
		 * loginInfo.setIpd_forms(ipdaccess.isIpd_forms());
		 * loginInfo.setIpd_discharge(ipdaccess.isIpd_discharge());
		 * loginInfo.setIpd_emr(ipdaccess.isIpd_emr());
		 * loginInfo.setIpd_prescription(ipdaccess.isIpd_prescription());
		 * loginInfo.setIpd_investigation(ipdaccess.isIpd_investigation());
		 * loginInfo.setIpd_reqconsultant(ipdaccess.isIpd_reqconsultant());
		 * loginInfo.setIpd_nursingcare(ipdaccess.isIpd_nursingcare());
		 * loginInfo.setIpd_reqblood(ipdaccess.isIpd_reqblood());
		 * loginInfo.setIpd_autocare(ipdaccess.isIpd_autocare());
		 * loginInfo.setIpd_treatmentlog(ipdaccess.isIpd_treatmentlog());
		 * loginInfo.setIpd_addcharges(ipdaccess.isIpd_addcharges());
		 * loginInfo.setIpd_createinvoice(ipdaccess.isIpd_createinvoice());
		 * loginInfo.setIpd_recordpayment(ipdaccess.isIpd_recordpayment());
		 * loginInfo.setIpd_advandref(ipdaccess.isIpd_advandref());
		 * loginInfo.setIpd_viewaccount(ipdaccess.isIpd_viewaccount());
		 * loginInfo.setIpd_packages(ipdaccess.isIpd_packages());
		 * loginInfo.setCancel_admsn(ipdaccess.isCancel_admsn());
		 */
		
		//account access
		Access accaess  = userProfileDAO.getAccountaccesss(userProfile.getJobtitle());
		
		loginInfo.setAcc_createinvoice(accaess.isAcc_createinvoice());
		loginInfo.setAcc_recordpayment(accaess.isAcc_recordpayment());
		loginInfo.setAcc_viewcreditaccount(accaess.isAcc_viewcreditaccount());
		loginInfo.setAcc_advandref(accaess.isAcc_advandref());
		loginInfo.setAcc_chargeinvoice(accaess.isAcc_chargeinvoice());
		loginInfo.setAcc_addcharges(accaess.isAcc_addcharges());
		loginInfo.setAcc_chargedetails(accaess.isAcc_chargedetails());
		loginInfo.setCash_desk(accaess.isCash_desk());
		loginInfo.setRefund(accaess.isRefund());
		
		/*
		 * Access emraccess = userProfileDAO.getEmraccesss(userProfile.getJobtitle());
		 * loginInfo.setEmr_docs(emraccess.isEmr_docs());
		 * loginInfo.setEmr_history(emraccess.isEmr_history());
		 * loginInfo.setEmr_medicine(emraccess.isEmr_medicine());
		 * loginInfo.setEmr_investigation(emraccess.isEmr_investigation());
		 * loginInfo.setEmr_pacs(emraccess.isEmr_pacs());
		 * loginInfo.setEmr_media(emraccess.isEmr_media());
		 * loginInfo.setEmr_update(emraccess.isEmr_update());
		 * loginInfo.setEmr_print(emraccess.isEmr_print());
		 * loginInfo.setEmr_edit(emraccess.isEmr_edit());
		 * loginInfo.setEmr_delete(emraccess.isEmr_delete());
		 */
		
		//client access
		Access clientaccaess  = userProfileDAO.getClientAccesss(userProfile.getJobtitle());
		
		loginInfo.setClient_add(clientaccaess.isClient_add());
		loginInfo.setClient_edit(clientaccaess.isClient_edit());
		loginInfo.setClient_delete(clientaccaess.isClient_delete());
		loginInfo.setClient_forms(clientaccaess.isClient_forms());
		loginInfo.setClient_discharge(clientaccaess.isClient_discharge());
		loginInfo.setClient_emai(clientaccaess.isClient_emai());
		loginInfo.setClient_print(clientaccaess.isClient_print());
		loginInfo.setClient_treatment(clientaccaess.isClient_treatment());
		loginInfo.setClient_log(clientaccaess.isClient_log());
		loginInfo.setClient_recordpayment(clientaccaess.isClient_recordpayment());
		loginInfo.setClient_cashdesk(clientaccaess.isClient_cashdesk());
		loginInfo.setClientadvandref(clientaccaess.isClientadvandref());
		loginInfo.setClient_addcharge(clientaccaess.isClient_addcharge());
		loginInfo.setClient_viewaccount(clientaccaess.isClient_viewaccount());
		loginInfo.setClient_emr(clientaccaess.isClient_emr());
		String loginsessionid = loginInfo.getUserId()+session.getId();
		loginInfo.setLoginsessionid(loginsessionid);
		loginInfo.setShow_master(clientaccaess.isShow_master());
		
		/*if(loginInfo.getJobTitle().equals("Pathlab")){
			
			return "pathlab";
		}*/
		
	/*	if(loginInfo.getJobTitle().equals("Medical Store")){
			
			return "medicalstore";
		}
		*/
		
		/*if(clinic.getUserType()==2){
			if(clinic2.isAppointmentBooking() == true){
			//return "gotodashboard";
			return "maindashboard";
		}
		}
		
		if(clinic.getUserType()==4){
			if(clinic2.isAppointmentBooking() == true){
			return "gotoweekdashboard";
				return "maindashboard";
		}
		}*/
		
		/*if(loginInfo.getJobTitle().equals("Medical Store")){
			return "phstaff";
		}*/
		

		//vaccination sms
		/*
		 * if(loginInfo.isImmusms()){ DateFormat dateFormat3 = new
		 * SimpleDateFormat("yyyy-MM-dd"); Calendar cal3 = Calendar.getInstance();
		 * cal3.add(Calendar.DATE, 1); String Date1= dateFormat3.format(cal3.getTime());
		 * notAvailableSlotDAO.getAllClientVaccinations(Date1, loginInfo); } SmsService
		 * s = new SmsService(); String
		 * msg="akash https://localhost:8080/HISLIVE/discountapproveMainDashBoard?userid=aureusamp;clinicuserid=aureusamp;discid=4170amp;invoiceid=32913"
		 * ; s.sendSms(msg, "9764434837", loginInfo, new EmailLetterLog());
		 */
		  
		  //edit charges access
		  UserProfile access2=userProfileDAO.getMainAccessByUserid(loginInfo.getUserId());
		  String editcharge=access2.getEditcharges();
		  boolean editchargesacs=false;
		  if(editcharge!=null){
		  if(editcharge.equals(""))
		  {
			  editchargesacs=true;
		  }
		  }if(Integer.parseInt(access2.getEditcharges())>0) {
			  editchargesacs=true;
		}
		  
		 loginInfo.setEditchargesacs(editchargesacs);
		  String fromtime=clinic2.getFromtime();
		  String totime=clinic2.getTotime();
		 
		 
		  
		  boolean misaccess=false;
		  if(fromtime!=null){
		  if(fromtime.equals(""))
		  {
			  misaccess=true;
		  }
		  }else {
			  misaccess=true;
		}
		  if (totime!=null) {
			if (totime.equals("")) {
				misaccess=true;
			}
		}else {
			misaccess=true;	
		}
		  if(!misaccess){
		  int ft=0,tt=0,ct=0;
		  ft=Integer.parseInt(fromtime);
		  tt=Integer.parseInt(totime);
		  DateFormat d = new SimpleDateFormat("HH");
		    Calendar cal2 = Calendar.getInstance();
		    cal2.add(Calendar.DATE, 1);
		    String currenttm = d.format(cal2.getTime());
		    ct=Integer.parseInt(currenttm);
		    if(ft>=ct && ct<=tt){
		    	misaccess=true;
		    }
		  }
		    loginInfo.setMisaccess(misaccess);
		  
			/*
			 * if(branchForm.getActiontype().equals("doctormob")){
			 * 
			 * return "doctormob"; }
			 */
		 
	
		}catch (Exception e) {
 			e.printStackTrace();
 			log.debug("##########################"+e.getMessage() + "-" + url);
 			/*DatabaseMetaData dmd = connection.getMetaData();
 			 url = dmd.getURL() + " username = " + dmd.getUserName();
 			log.debug("##########################"+url);
 			log.debug("@@@@@@@@@"+Constants.DB_HOST + "/" + e.getMessage());
 			System.out.println(url);*/
 			return "error";
		}
		
		finally{
			if(connection==null){
				return "error";
			}
			connection.close();
			
		}
		
		
		
		return "maindashboard";
	}
	
	
	public String input() {
		
		
		  if(session.getAttribute("logininfo")!=null){
			  
			  return SUCCESS;
		  } else {
			  return INPUT;
		  }
	}
	
	
/*	
	public String lockScreenPage(){
		
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			LoginInfo loginInfo = (LoginInfo) session.getAttribute("loginInfo");
			String userId = loginInfo.getUserId();
			String encPassword = Encryption.encryptSHA(branchForm.getPassword());
			Clinic clinic = clinicDAO.getuser(userId);
			
			if(!encPassword.equals(clinic.getPassword())){
				addActionError(getText("error.user.authfailed"));
				return "lockscreen";
			}
			
		}catch(Exception e){
			
		}
		
		return "gotodashboard";
	}
	*/

	public void validate() {
	    	
    	 /* Do not use 'else if' since it will cause to show only one error at a time */
    	 // If user is null or empty add error in field errors
		 if ( branchForm.getUserId() == null || branchForm.getUserId().length() == 0) {
	            addFieldError("userId", getText("error.userid.required") );	// set error message form property file
		 }
		 // If password is null or empty add error to field errors
	     if ( branchForm.getPassword() == null ||  branchForm.getPassword().length() == 0) {
	            addFieldError("password", getText("error.password.required")); 	// set error message form property file
	     }
    }

	public BranchForm getModel() {
		// TODO Auto-generated method stub
		return branchForm;
	}

	

	
	
	

}
