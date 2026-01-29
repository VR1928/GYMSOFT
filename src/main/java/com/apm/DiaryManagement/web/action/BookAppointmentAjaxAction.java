
package com.apm.DiaryManagement.web.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.apm.Accounts.eu.bi.AccountsDAO;
import com.apm.Accounts.eu.bi.AdditionalDAO;
import com.apm.Accounts.eu.bi.ChargesAccountProcessingDAO;
import com.apm.Accounts.eu.blogic.jdbc.JDBCAccountsDAO;
import com.apm.Accounts.eu.blogic.jdbc.JDBCAdditionalDAO;
import com.apm.Accounts.eu.blogic.jdbc.JDBCChargeAccountProcesDAO;
import com.apm.Accounts.eu.entity.Accounts;
import com.apm.Appointment.eu.bi.AppointmentDAO;
import com.apm.Appointment.eu.bi.AppointmentTypeDAO;
import com.apm.Appointment.eu.blogic.jdbc.JDBCAppointmentDAO;
import com.apm.Appointment.eu.blogic.jdbc.JDBCAppointmentTypeDAO;
import com.apm.Appointment.eu.entity.AppointmentType;
import com.apm.Diagnosis.eu.bi.DiagnosisDAO;
import com.apm.Diagnosis.eu.blogic.jdbc.JDBCDiagnosisDAO;
import com.apm.Diagnosis.eu.entity.Diagnosis;
import com.apm.DiaryManagement.eu.bi.ClientDAO;
import com.apm.DiaryManagement.eu.bi.CompleteAptmDAO;
import com.apm.DiaryManagement.eu.bi.DiaryManagementDAO;
import com.apm.DiaryManagement.eu.bi.NotAvailableSlotDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCClientDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCCompleteAptmDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCDiaryManagentDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCNotAvailableSlotDAO;
import com.apm.DiaryManagement.eu.entity.Client;
import com.apm.DiaryManagement.eu.entity.CompleteAppointment;
import com.apm.DiaryManagement.eu.entity.NotAvailableSlot;
import com.apm.DiaryManagement.eu.entity.Priscription;
import com.apm.Dietary.eu.bi.DietaryDetailsDAO;
import com.apm.Dietary.eu.blogic.jdbc.JDBCDietaryDetailsDAO;
import com.apm.Dietary.eu.entity.DietaryDetails;
import com.apm.Emr.eu.bi.EmrDAO;
import com.apm.Emr.eu.bi.InvestigationDAO;
import com.apm.Emr.eu.bi.PrescriptionDAO;
import com.apm.Emr.eu.blogic.jdbc.JDBCEmrDAO;
import com.apm.Emr.eu.blogic.jdbc.JDBCInvestigationDAO;
import com.apm.Emr.eu.blogic.jdbc.JDBCPrescriptionDAO;
import com.apm.Emr.eu.entity.Investigation;
import com.apm.Inventory.eu.bi.InventoryProductDAO;
import com.apm.Inventory.eu.bi.ProcurementDAO;
import com.apm.Inventory.eu.blogic.jdbc.JDBCInventoryProductDAO;
import com.apm.Inventory.eu.blogic.jdbc.JDBCProcurementDAO;
import com.apm.Inventory.eu.entity.Product;
import com.apm.Ipd.eu.bi.BedDao;
import com.apm.Ipd.eu.bi.IpdDAO;
import com.apm.Ipd.eu.blogic.jdbc.JDBCBedDao;
import com.apm.Ipd.eu.blogic.jdbc.JDBCIpdDAO;
import com.apm.Ipd.eu.entity.Bed;
import com.apm.Ipd.eu.entity.Ipd;
import com.apm.Log.eu.bi.AccountLogDAO;
import com.apm.Log.eu.blogic.jdbc.JDBCAccountLogDAO;
import com.apm.Log.eu.entity.EmailLetterLog;
import com.apm.Master.eu.bi.MasterDAO;
import com.apm.Master.eu.bi.PackageMasterDAO;
import com.apm.Master.eu.bi.PrescriptionMasterDAO;
import com.apm.Master.eu.blogic.jdbc.JDBCMasterDAO;
import com.apm.Master.eu.blogic.jdbc.JDBCPackageMasterDAO;
import com.apm.Master.eu.blogic.jdbc.JDBCPrescriptionMasterDAO;
import com.apm.Master.eu.entity.Master;
import com.apm.Master.eu.entity.PackageMaster;
import com.apm.Pharmacy.eu.bi.PharmacyDAO;
import com.apm.Pharmacy.eu.blogic.jdbc.JDBCPharmacyDAO;
import com.apm.Registration.eu.bi.ClinicDAO;
import com.apm.Registration.eu.bi.UserProfileDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCClinicDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCUserProfileDAO;
import com.apm.Registration.eu.entity.Clinic;
import com.apm.Registration.eu.entity.UserProfile;
import com.apm.ThirdParties.eu.bi.ThirdPartyDAO;
import com.apm.ThirdParties.eu.blogic.jdbc.JDBCThirdPartyDAO;
import com.apm.ThirdParties.eu.entity.ThirdParty;
import com.apm.Tools.web.action.AllTemplateAction;
import com.apm.TreatmentEpisode.eu.bi.TreatmentEpisodeDAO;
import com.apm.TreatmentEpisode.eu.blogic.jdbc.JDBCTreatmentEpisode;
import com.apm.TreatmentEpisode.eu.entity.TreatmentEpisode;
import com.apm.common.eu.blogic.jdbc.Connection_provider;
import com.apm.common.utils.DateTimeUtils;
import com.apm.common.web.action.BaseAction;
import com.apm.common.web.common.helper.LoginHelper;
import com.apm.common.web.common.helper.LoginInfo;
import com.apm.main.common.constants.Constants;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.inject.util.Function;
import com.sun.javaws.jnl.UpdateDesc;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import atg.taglib.json.util.JSONObject;
import sun.awt.image.VolatileSurfaceManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.apm.Accounts.eu.bi.AccountsDAO;
import com.apm.Accounts.eu.bi.AdditionalDAO;
import com.apm.Accounts.eu.bi.ChargesAccountProcessingDAO;
import com.apm.Accounts.eu.blogic.jdbc.JDBCAccountsDAO;
import com.apm.Accounts.eu.blogic.jdbc.JDBCAdditionalDAO;
import com.apm.Accounts.eu.blogic.jdbc.JDBCChargeAccountProcesDAO;
import com.apm.Accounts.eu.entity.Accounts;
import com.apm.Appointment.eu.bi.AppointmentDAO;
import com.apm.Appointment.eu.bi.AppointmentTypeDAO;
import com.apm.Appointment.eu.blogic.jdbc.JDBCAppointmentDAO;
import com.apm.Appointment.eu.blogic.jdbc.JDBCAppointmentTypeDAO;
import com.apm.Appointment.eu.entity.AppointmentType;
import com.apm.Diagnosis.eu.bi.DiagnosisDAO;
import com.apm.Diagnosis.eu.blogic.jdbc.JDBCDiagnosisDAO;
import com.apm.Diagnosis.eu.entity.Diagnosis;
import com.apm.DiaryManagement.eu.bi.ClientDAO;
import com.apm.DiaryManagement.eu.bi.CompleteAptmDAO;
import com.apm.DiaryManagement.eu.bi.DiaryManagementDAO;
import com.apm.DiaryManagement.eu.bi.NotAvailableSlotDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCClientDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCCompleteAptmDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCDiaryManagentDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCNotAvailableSlotDAO;
import com.apm.DiaryManagement.eu.entity.Client;
import com.apm.DiaryManagement.eu.entity.CompleteAppointment;
import com.apm.DiaryManagement.eu.entity.NotAvailableSlot;
import com.apm.DiaryManagement.eu.entity.Priscription;
import com.apm.Dietary.eu.bi.DietaryDetailsDAO;
import com.apm.Dietary.eu.blogic.jdbc.JDBCDietaryDetailsDAO;
import com.apm.Dietary.eu.entity.DietaryDetails;
import com.apm.Emr.eu.bi.EmrDAO;
import com.apm.Emr.eu.bi.InvestigationDAO;
import com.apm.Emr.eu.bi.PrescriptionDAO;
import com.apm.Emr.eu.blogic.jdbc.JDBCEmrDAO;
import com.apm.Emr.eu.blogic.jdbc.JDBCInvestigationDAO;
import com.apm.Emr.eu.blogic.jdbc.JDBCPrescriptionDAO;
import com.apm.Emr.eu.entity.Investigation;
import com.apm.Inventory.eu.bi.InventoryProductDAO;
import com.apm.Inventory.eu.bi.ProcurementDAO;
import com.apm.Inventory.eu.blogic.jdbc.JDBCInventoryProductDAO;
import com.apm.Inventory.eu.blogic.jdbc.JDBCProcurementDAO;
import com.apm.Inventory.eu.entity.Product;
import com.apm.Ipd.eu.bi.BedDao;
import com.apm.Ipd.eu.bi.IpdDAO;
import com.apm.Ipd.eu.blogic.jdbc.JDBCBedDao;
import com.apm.Ipd.eu.blogic.jdbc.JDBCIpdDAO;
import com.apm.Ipd.eu.entity.Bed;
import com.apm.Ipd.eu.entity.Ipd;
import com.apm.Log.eu.bi.AccountLogDAO;
import com.apm.Log.eu.blogic.jdbc.JDBCAccountLogDAO;
import com.apm.Log.eu.entity.EmailLetterLog;
import com.apm.Master.eu.bi.MasterDAO;
import com.apm.Master.eu.bi.PackageMasterDAO;
import com.apm.Master.eu.bi.PrescriptionMasterDAO;
import com.apm.Master.eu.blogic.jdbc.JDBCMasterDAO;
import com.apm.Master.eu.blogic.jdbc.JDBCPackageMasterDAO;
import com.apm.Master.eu.blogic.jdbc.JDBCPrescriptionMasterDAO;
import com.apm.Master.eu.entity.Master;
import com.apm.Master.eu.entity.PackageMaster;
import com.apm.Pharmacy.eu.bi.PharmacyDAO;
import com.apm.Pharmacy.eu.blogic.jdbc.JDBCPharmacyDAO;
import com.apm.Registration.eu.bi.ClinicDAO;
import com.apm.Registration.eu.bi.UserProfileDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCClinicDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCUserProfileDAO;
import com.apm.Registration.eu.entity.Clinic;
import com.apm.Registration.eu.entity.UserProfile;
import com.apm.ThirdParties.eu.bi.ThirdPartyDAO;
import com.apm.ThirdParties.eu.blogic.jdbc.JDBCThirdPartyDAO;
import com.apm.ThirdParties.eu.entity.ThirdParty;
import com.apm.Tools.web.action.AllTemplateAction;
import com.apm.TreatmentEpisode.eu.bi.TreatmentEpisodeDAO;
import com.apm.TreatmentEpisode.eu.blogic.jdbc.JDBCTreatmentEpisode;
import com.apm.TreatmentEpisode.eu.entity.TreatmentEpisode;
import com.apm.common.eu.blogic.jdbc.Connection_provider;
import com.apm.common.utils.DateTimeUtils;
import com.apm.common.web.action.BaseAction;
import com.apm.common.web.common.helper.LoginHelper;
import com.apm.common.web.common.helper.LoginInfo;
import com.apm.main.common.constants.Constants;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.inject.util.Function;
import com.sun.javaws.jnl.UpdateDesc;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import atg.taglib.json.util.JSONObject;
import sun.awt.image.VolatileSurfaceManager;

public class BookAppointmentAjaxAction extends BaseAction {

	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
	HttpServletResponse response = (HttpServletResponse) ActionContext.getContext()
			.get(ServletActionContext.HTTP_RESPONSE);
	HttpSession session = request.getSession(true);
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	
	private static final Logger TestingLog = Logger.getLogger(BookAppointmentAjaxAction.class);
	public String rdd(){
		String val = request.getParameter("val");
		session.setAttribute("sessionrddval", val);
		return null;
	}
	
	
	public String media(){
		
		return "opdmedia";
	}
	
	public String vdyo(){
		
		try {
			response.sendRedirect("./VidyoClient-WebSDK/samples/VidyoConnector/js/VidyoConnector.jsp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
		
	
	public String dsplay() {

		if (!verifyLogin(request)) {
			return "login";
		}

		session.setAttribute("openedb", "dsplay");

		return "dsplay";
	}


	public String opd() {

		if (!verifyLogin(request)) {
			return "login";
		}

		session.setAttribute("openedb", "opd");

		return "opd";
	}

	public String otdb() {

		if (!verifyLogin(request)) {
			return "login";
		}

		session.setAttribute("openedb", "otdb");
		session.removeAttribute("sessionrddval");
		return "ot";
	}
	
	public String updmveapmt(){
		String duserid = request.getParameter("duserid");
		String commencing = request.getParameter("commencing");
		String stime = request.getParameter("stime");
		String endtime = request.getParameter("endtime");
		String duration = request.getParameter("duration");
		String editAppointId = request.getParameter("editAppointId");
		String apmttype = request.getParameter("apmttype");
		System.out.println("hello");
		
		if (!verifyLogin(request)) {
			return "login";
		}
		Connection connection = null;
		
		try{
			connection = Connection_provider.getconnection();
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			
			NotAvailableSlot n = new NotAvailableSlot();
			AdditionalDAO additionalDAO=new JDBCAdditionalDAO(connection);
			n.setDiaryUserId(Integer.parseInt(duserid));
			commencing = DateTimeUtils.getCommencingDate(commencing);
			n.setCommencing(commencing);
			n.setSTime(stime);
			n.setEndTime(endtime);
			n.setDuration(duration);
			n.setAppointmentid(Integer.parseInt(editAppointId));
			n.setApmtType(apmttype);
			UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
			UserProfile up = userProfileDAO.getUserprofileDetails(Integer.parseInt(duserid));
			AppointmentType appointmentType=additionalDAO.getAppointmentTypeDetails(apmttype);
			n.setApmttypetext(appointmentType.getName());
			n.setCharge(Double.parseDouble(appointmentType.getCharges()));
			String dname = up.getInitial() + " " + up.getFirstname() + " " + up.getLastname();
			n.setDiaryUser(dname);
			
						NotAvailableSlot t = notAvailableSlotDAO.getMveDiaryUserDetails(duserid,commencing);
			n.setId(t.getId());
			
			int r = notAvailableSlotDAO.updatemveappointment(n);
			
			//update apm_invoice
			int u = notAvailableSlotDAO.updatemveapminvoice(editAppointId,n);
			
			Accounts a = notAvailableSlotDAO.getmveapmtchargeinfo(editAppointId);
			
			// update invoice assesment

			u = notAvailableSlotDAO.updatemveapminvoiceassesmnt(a.getId(),n);
			
			// update charges invoice
			u = notAvailableSlotDAO.updatemveapmtchargesinvoice(a.getInvoiceid(),n);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	public String mveapmt(){
		
		String duserid = request.getParameter("duserid");
		String commencing = request.getParameter("commencing");
		
		if (!verifyLogin(request)) {
			return "login";
		}
		Connection connection = null;
		
		try{
			connection = Connection_provider.getconnection();
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			
			commencing = DateTimeUtils.getCommencingDate(commencing);
			
			
			
			NotAvailableSlot n = notAvailableSlotDAO.getMveDiaryUserDetails(duserid,commencing);
			
			String stime = n.getSTime();
			int slotid = n.getId();
			String duration = n.getDuration();
			boolean chkapmtexsist = notAvailableSlotDAO.chkmveapmtaxsist(duserid,commencing);
			if(chkapmtexsist){
				stime = notAvailableSlotDAO.getmveapmtendtime(duserid,commencing);
			}
			
		
			String str = slotid + "~" + stime + "~" + duration;
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(str.toString());

			
			
		}catch (Exception e) {
			// TODO: handle exception
	
			}

		
		return null;
	}
	
	public String newdisplay(){
		
		if (!verifyLogin(request)) {
			return "login";
		}
		Connection connection = null;
		
		
		String ndate = request.getParameter("ndate");
		String nduserid = request.getParameter("nduserid");
		
	
		int t = 1;
		try{
			connection = Connection_provider.getconnection();
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			//Opd dashboard display data
			
			ndate = DateTimeUtils.getCommencingDate(ndate);	
			
			ArrayList<NotAvailableSlot>opdlist = notAvailableSlotDAO.getNewOpdList(ndate,nduserid);
			
			StringBuffer str = new StringBuffer();
			int i = 1;
			for(NotAvailableSlot n : opdlist){
				String color = "";
				String textcolor="";
				int popupstatus = 0;
				if(n.getStatus().equals("0")){
					color = "rgb(252, 186, 99)";
					popupstatus = 1;
				}
				if(n.isAppointmentCompleted()){
					color = "rgb(129, 171, 109)";
					popupstatus = 2;
				}
				if(!n.getInvoiceid().equals("0")){
					color = "rgb(204, 204, 204)";
					
					popupstatus = 3;
					
				}
				if(n.getOpdpmnt()>0){
					color = "rgb(204, 204, 204)";
				}
				if(n.getDrcompleted()==1){
					
					color = "rgb(92, 241, 213)";
					
					popupstatus = 3;
				}
				
				if(n.getArrivedStatus()==1){
					textcolor="color: green !important;";
				}
				 if(n.getArrivedStatus()==2){
					textcolor="color: white !important;";
				}
				 if(n.isDna()){
					 color = "rgb(255,0,0)";
					 popupstatus = 4;
					 }
				 if(n.getApmttypetext().equals("OPD FREE") && Integer.parseInt(n.getInvoiceid())>0){
					 color = "rgb(204, 204, 204)";
						
						popupstatus = 3;
						if(n.getDrcompleted()==1){
							
							color = "rgb(92, 241, 213)";
							
							popupstatus = 3;
						}
				 }
				str.append("<tr style='background-color:"+color+";cursor:pointer;"+textcolor+";font-size:13px' onclick='shownewdto("+n.getId()+","+nduserid+","+n.getCondition()+","+n.getClientId()+","+i+","+popupstatus+","+n.getPbodytemplate()+","+n.getPbodyeditedtmplate()+")'>");
				str.append("<td style='text-align: left;'>"+i+"</td>");
				str.append("<td style='text-align: left;'>"+DateTimeUtils.getCommencingDate1(n.getCommencing())+"</td>");
				str.append("<td style='text-align: left;'>"+n.getOpdbooktime()+"</td>");
				str.append("<td style='text-align: left;'>"+n.getsTime()+"</td>");
				str.append("<td style='text-align: left;'>"+n.getDuration()+"</td>");
				str.append("<td style='text-align: left;'>"+n.getAbrivationid()+"</td>");
				if(loginInfo.isOpd_video_icon_show()){
					str.append("<td style='text-align: left;'>"+n.getClientName()+" (<a title='Vidio Call' href='vdyoBookAppointmentAjax' target='blank'><span class='glyphicon glyphicon-facetime-video'></span></a>)</td>");
				}else{
					str.append("<td style='text-align: left;'>"+n.getClientName()+"</td>");
				}
				str.append("<td style='text-align: left;'>"+n.getAgegender()+"</td>");
				
				
				str.append("<td style='text-align: left;'>"+n.getApmttypetext()+"</td>");
				
				/*str.append("<td style='text-align: left;'><a href='#'>Day_To_Day</a></td>");*/
				str.append("<input type = 'hidden' name='ncname' id='ncname"+i+"' value='"+n.getClientName()+"'>");
				str.append("<input type = 'hidden' name='nwhopay' id='nwhopay"+i+"' value='"+n.getWhopay()+"'>");
				
				i++;
				
				str.append("</tr>");
			}
			
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(str.toString());

			
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
		
	}
	
	public String newopd(){
		if (!verifyLogin(request)) {
			return "login";
		}
		Connection connection = null;
		
		
		String ndate = request.getParameter("ndate");
		String nduserid = request.getParameter("nduserid");
		
	
		int t = 1;
		try{
			connection = Connection_provider.getconnection();
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			
			
			ndate = DateTimeUtils.getCommencingDate(ndate);	
			
			
			
			System.out.println(ndate);
			
			
			
			NotAvailableSlot n = notAvailableSlotDAO.getNewOpdDiaryUserData(ndate,nduserid);
			Date date = new Date();
			DateFormat format = new SimpleDateFormat("HH:mm");
			String currentTime=format.format(date);
			String mm=currentTime.split(":")[1];
			int rem=Integer.parseInt(mm)%5;
			if(rem==0){
				n.setsTime(currentTime);
				n.setSlotstime(currentTime);
			}else{
				int mmm=Integer.parseInt(mm);
				while(mmm%5!=0){
					mmm--;
				}
				if(mmm<=9){
					String singlemm=String.valueOf(mmm);
					singlemm="0"+singlemm;
					mmm=Integer.parseInt(singlemm);
				}
				String updatetime=currentTime.split(":")[0]+":"+mmm;
				n.setsTime(updatetime);
				n.setSlotstime(updatetime);
			}
			// id,starttime,endtime,apmtduration,location,diaryuser,diaryuserid 
			String str = n.getId() + "~" + n.getsTime() + "~" + n.getEndTime() 
			+ "~" + n.getDuration() + "~" + n.getLocation() + "~" + n.getDiaryUser()
			+ "~" + n.getDiaryUserId() + "~" + n.getSlotstime() + "~" +ndate 
			+ "~" +  n.getDiaryUserId();
			
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(str);
			
			
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}

	public String saveAppoinment() throws SQLException {
		if (!verifyLogin(request)) {
			return "login";
		}
		Connection connection = null;
		int result = 0;

		String slotId = request.getParameter("slotId");
		String commencing = request.getParameter("commencing");
		/*
		 * String temp[] = commencing.split("/"); commencing =
		 * temp[2]+"-"+temp[1]+"-"+temp[0];
		 */
		String location = request.getParameter("location");
		String room = request.getParameter("room");
		String sTime = request.getParameter("sTime");
		
		//setting radio button value
		String rdstme[] = sTime.split(":");
		int rrr = Integer.parseInt(rdstme[0]) * 1;
		session.setAttribute("sessionrddval", ""+rrr+"");
		
		String endTime = request.getParameter("endTime");
		String apmtDuration = request.getParameter("apmtDuration");
		String diaryuserId = request.getParameter("diaryuserId");
		String client = request.getParameter("client");
		String dept = request.getParameter("dept");
		String apmtType = request.getParameter("apmtType");
		String notes = request.getParameter("notes");
		String diaryUser = request.getParameter("diaryUser");
		String clientId = request.getParameter("clientId");
		String treatmentEpisodeId = request.getParameter("treatmentEpisodeId");
		String condition = request.getParameter("condition");
		String whopay = request.getParameter("whopay");
		String stafflistid = request.getParameter("stafflistid");
		boolean isot = Boolean.parseBoolean(request.getParameter("isot"));
		String bookwithpayment = request.getParameter("bookwithpayment");

		// ot variables
		String otplaned = request.getParameter("otplaned");
		String otprocedureplaned1 = request.getParameter("otprocedureplaned");

		String otsurgeonname = request.getParameter("otsurgeonname");
		String otanesthesia = request.getParameter("otanesthesia");
		String otipdnos = request.getParameter("otipdnos");

		String psurcharge = request.getParameter("psurcharge");
		String panetcharge = request.getParameter("panetcharge");
		String anifees = request.getParameter("anifees");
		String sic = request.getParameter("sic");
		String assistaffcharge = request.getParameter("assistaffcharge");
		// take payment data
		String invcetype = request.getParameter("invcetype");
		String howpaid = request.getParameter("howpaid");
		String totalamount = request.getParameter("totalamount");
		String discount = request.getParameter("discount");
		String payAmount = request.getParameter("payAmount");
		String disctype = request.getParameter("disctype");
		String bnkname = request.getParameter("bnkname");
		// String paymentNote = document.getElementById('paymentNote').value;

		String status = null;
		int selectedid = Integer.parseInt(request.getParameter("selectedid"));
		String selectedot = request.getParameter("selectedot");
		
		String opdotcharge = request.getParameter("opdotcharge");
		String opdotregcharge = request.getParameter("opdotregcharge");
		//shubham 22/05/2019
		String height = request.getParameter("height");
		String weight = request.getParameter("weight");
		String bmi = request.getParameter("bmi");
		String headcir = request.getParameter("headcir");
		String tempr = request.getParameter("tempr");
		int seqno = 1;
		if(assistaffcharge.equals("")){
			assistaffcharge="0";
     	}
     	
     	if(psurcharge.equals("")){
     		psurcharge="0";
     	}
     	if(panetcharge.equals("")){
     		panetcharge="0";
     	}
     	if(sic.equals("")){
     		sic="0";
     	}
     	if(assistaffcharge.equals("")){
     		assistaffcharge="0";
     	}
     	
     	if(anifees.equals("")){
     		anifees="0";
     	}
		/*
		 * int slotId1 = 0; int diaryuserId1 = 0;
		 * 
		 * if(selectedid == 0){ slotId1 = Integer.parseInt(slotId); diaryuserId1
		 * = Integer.parseInt(diaryuserId); }
		 */

		NotAvailableSlot beforeUpdateData = new NotAvailableSlot();

		try {
			connection = Connection_provider.getconnection();
			MasterDAO masterDAO = new JDBCMasterDAO(connection);
			NotAvailableSlot notAvailableSlot = new NotAvailableSlot();
			notAvailableSlot.setApmtSlotId(Integer.parseInt(slotId));
			notAvailableSlot.setDiaryUserId(Integer.parseInt(diaryuserId));
			IpdDAO ipdDAO = new JDBCIpdDAO(connection);
			UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
			UserProfile userProfile = userProfileDAO.getUserprofileDetails(notAvailableSlot.getDiaryUserId());
			String dusername = userProfile.getInitial() + " " + userProfile.getFirstname() + " "
					+ userProfile.getLastname();
			notAvailableSlot.setDiaryUser(dusername);
			notAvailableSlot.setBnkname(bnkname);

			// Akash 25 Jan 2018
			String otprocedureplaned = masterDAO.getMasterChargeType(otprocedureplaned1);

			// Akash set ipdno while booking ot appointment
			if (otprocedureplaned1 != null) {
				if (!otprocedureplaned1.equals("0")) {
					int lastipdid = ipdDAO.getLastIpdId(clientId);
					if (otipdnos == null) {
						otipdnos = String.valueOf(lastipdid);
					} else if (otipdnos.equals("")) {
						otipdnos = String.valueOf(lastipdid);
					}
				}
			}

			notAvailableSlot.setDept(dept);
			notAvailableSlot.setLocation(location);
			notAvailableSlot.setRoom(room);
			notAvailableSlot.setCommencing(commencing);
			notAvailableSlot.setSTime(sTime);
			notAvailableSlot.setEndTime(endTime);
			notAvailableSlot.setApmtDuration(apmtDuration);
			notAvailableSlot.setClient(client);
			notAvailableSlot.setApmtType(apmtType);
			notAvailableSlot.setNotes(notes);
			notAvailableSlot.setClientId(clientId);
			notAvailableSlot.setTreatmentEpisodeId(treatmentEpisodeId);
			notAvailableSlot.setAddedBy(loginInfo.getUserId());
			notAvailableSlot.setModifiedBy(loginInfo.getUserId());
			notAvailableSlot.setCondition(condition);
			notAvailableSlot.setPayBy(whopay);
			notAvailableSlot.setStafflistid(stafflistid);

			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			DiaryManagementDAO diaryManagementDAO = new JDBCDiaryManagentDAO(connection);
			BedDao bedDao = new JDBCBedDao(connection);
			String zero = "0";
			int appointmentid = 0;
			// 2014-08-08
			// current date and time in dd/mm/yyyy
			String currentDate = DateTimeUtils.getDateinSimpleFormate(new Date());
			String datetemp[] = currentDate.split(" ");
			String temp1[] = datetemp[0].split("-");
			String date = temp1[0] + "/" + temp1[1] + "/" + temp1[2];
			String commencingtemp[] = commencing.split(" ");
			String tempC[] = commencingtemp[0].split("-");
			String commencingTemp = commencing;
			String time = (datetemp[2] + " " + datetemp[3]);

			// ot set get
			notAvailableSlot.setOtplan(otplaned);
			notAvailableSlot.setProcedure(otprocedureplaned);
			notAvailableSlot.setSurgeon(otsurgeonname);
			notAvailableSlot.setAnesthesia(otanesthesia);
			notAvailableSlot.setIpdno(otipdnos);

			notAvailableSlot.setPsurcharge(psurcharge);
			notAvailableSlot.setPanetcharge(panetcharge);
			notAvailableSlot.setAnifees(anifees);
			notAvailableSlot.setSic(sic);
			notAvailableSlot.setAssistaffcharge(assistaffcharge);
			if (otplaned.equals("Planed")) {
				Bed bed = bedDao.getEditIpdData(otipdnos);
				notAvailableSlot.setWardid(bed.getWardid());
			}

			// update client bpayby

			ClientDAO clientDAO = new JDBCClientDAO(connection);
			Client client2 = clientDAO.getClientDetails(notAvailableSlot.getClientId());
			int updatewhopay = clientDAO.updateWhoPay(notAvailableSlot.getClientId(), notAvailableSlot.getPayBy());

			if (selectedid == 0) {

				if (!isot) {
					notAvailableSlot.setOtplan("0");
					notAvailableSlot.setProcedure("0");
					notAvailableSlot.setSurgeon("0");
					notAvailableSlot.setAnesthesia("0");
					notAvailableSlot.setIpdno("0");
					notAvailableSlot.setWardid("0");

					notAvailableSlot.setPsurcharge("0");
					notAvailableSlot.setPanetcharge("0");
					notAvailableSlot.setAnifees("0");
					notAvailableSlot.setSic("0");

				}

				int usedsession = diaryManagementDAO.getTempUsedSession(notAvailableSlot.getTreatmentEpisodeId());
				usedsession = usedsession + 1;

				if (treatmentEpisodeId.equals(zero)) {
					usedsession = 0;
				}
				notAvailableSlot.setUsedsession(Integer.toString(usedsession));

				boolean checkEventExist = notAvailableSlotDAO.checkEventAllreadyExist(notAvailableSlot.getCommencing(),
						location, diaryuserId, notAvailableSlot.getSTime(), notAvailableSlot.getEndTime());
				if (checkEventExist) {
					checkEventExist = cheForWeekRepeatAfterEventExist(notAvailableSlot.getCommencing(), location,
							diaryuserId, notAvailableSlot.getSTime(), notAvailableSlot.getEndTime());
				}
				if (!checkEventExist) {

					// save abrivation seq no
					String cdate = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
					boolean checkifseq = notAvailableSlotDAO.checkifSequenceExist(cdate,
							notAvailableSlot.getDiaryUserId());
					String abrivationid = "";
					// String clinicabrivation =
					// clientDAO.getClinicAbrivation(loginInfo.getClinicid());
					String tempd[] = cdate.split("-");
					String y = tempd[0];
					String m = tempd[1];
					String d = tempd[2];
					SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

				    Date now = new Date();

				    String opdbooktime = sdfTime.format(now);
					notAvailableSlot.setOpdbooktime(opdbooktime);
					// boolean ch =
					// notAvailableSlotDAO.checkEventAllreadyExist(notAvailableSlot.getCommencing(),location,
					// Integer.toString(notAvailableSlot.getDiaryUserId()),notAvailableSlot.getSTime(),notAvailableSlot.getEndTime());
					// if(!ch){
					appointmentid = notAvailableSlotDAO.saveAppointment(notAvailableSlot);
					// }

					if (checkifseq) {
						seqno = notAvailableSlotDAO.getSqeunceNumber(cdate, notAvailableSlot.getDiaryUserId());
						seqno++;
						int r = notAvailableSlotDAO.InserCdateSeq(cdate, seqno, appointmentid,
								notAvailableSlot.getDiaryUserId());
						// SNH170609001
						/*
						 * int yr = Integer.parseInt(y)%1000; abrivationid =
						 * clinicabrivation + yr + m +d + "00" + seqno;
						 */
					} else {

						seqno = 1;
						int r = notAvailableSlotDAO.InserCdateSeq(cdate, seqno, appointmentid,
								notAvailableSlot.getDiaryUserId());
						// String seqno = clientDAO.getSqeunceNumber(cdate);
						/*
						 * int yr = Integer.parseInt(y)%1000; abrivationid =
						 * clinicabrivation + yr + m +d + "00" + seqno;
						 */
					}

					
					if (!client2.getCasualtyid().equals("0")) {

						int updc = bedDao.updateCasualtyid(Integer.parseInt(notAvailableSlot.getClientId()));
					}
					if (isot) {
						String temp[] = stafflistid.split(",");
						for (int i = 0; i < temp.length; i++) {
							if (i > 0) {
								int otid = notAvailableSlotDAO.saveParenrotData(notAvailableSlot.getCommencing(),
										selectedot, temp[i], appointmentid);
							}

							// block slot for ot
							stafflistid = stafflistid + "," + notAvailableSlot.getAnesthesia() + ","
									+ notAvailableSlot.getSurgeon();
							/*
							 * String temps[] = stafflistid.split(","); for(int
							 * b=0;b<temps.length;b++){ if(b>0){ String
							 * selectedpractid = temps[b];
							 * 
							 * UserProfile userProfile2 =
							 * userProfileDAO.getUserprofileDetails(Integer.
							 * parseInt(selectedpractid)); String fullname =
							 * userProfile2.getInitial() + " " +
							 * userProfile2.getFirstname() + " " +
							 * userProfile2.getLastname();
							 * notAvailableSlot.setDiaryUser(fullname); int
							 * apmtSlotid =
							 * notAvailableSlotDAO.getOtAppointmentSlotID(
							 * notAvailableSlot.getCommencing(),Integer.parseInt
							 * (selectedpractid),notAvailableSlot.getSTime(),
							 * notAvailableSlot.getEndTime(),notAvailableSlot.
							 * getLocation());
							 * notAvailableSlot.setApmtSlotId(apmtSlotid);
							 * checkEventExist =
							 * notAvailableSlotDAO.checkEventAllreadyExist(
							 * notAvailableSlot.getCommencing(),location,
							 * selectedpractid,notAvailableSlot.getSTime(),
							 * notAvailableSlot.getEndTime());
							 * if(!checkEventExist){
							 * notAvailableSlot.setStatus("1");
							 * notAvailableSlot.setDiaryUserId(Integer.parseInt(
							 * selectedpractid));
							 * notAvailableSlot.setBlockot(appointmentid);
							 * notAvailableSlot.setReasonforblock("OT Booked");
							 * int block = notAvailableSlotDAO.saveBlockSlot(
							 * notAvailableSlot, "opd"); } } }
							 */

						}
						TestingLog.debug("saveAppoinment()  728");
						// send sms to accountants
						double charge = notAvailableSlotDAO.getCharge(notAvailableSlot.getApmtType());
						ArrayList<Master> accUserList = notAvailableSlotDAO.getAccountUserList();
						for (Master mstr : accUserList) {
							UserProfile u = userProfileDAO.getUserprofileDetails(mstr.getId());
							String accountant = u.getInitial() + " " + u.getFirstname() + " " + u.getLastname();

							String otmsg = accountant + "it has been informed you that " + notAvailableSlot.getClient()
									+ " has OT " + notAvailableSlot.getProcedure() + " scheduled on date "
									+ notAvailableSlot.getCommencing() + " of " + Constants.getCurrency(loginInfo)
									+ DateTimeUtils.changeFormat(charge) + " kindly check payment";

							SendSms os = new SendSms();
							//os.send(otmsg, u.getMobile(), loginInfo, new EmailLetterLog());
						}
						SmsService s = new SmsService();
						if(loginInfo.isOt_patient_sms()){
							UserProfile userpf = userProfileDAO.getUserprofileDetails(Integer.parseInt(otsurgeonname));
						String patientot=notAvailableSlot.getClient()+" It has been informed you that OT " + notAvailableSlot.getProcedure() + " scheduled on date "+ DateTimeUtils.getCommencingDate1(notAvailableSlot.getCommencing())+"";
						s.sendSms(patientot, client2.getMobNo(), loginInfo, new EmailLetterLog());
						}
						if(loginInfo.isOt_surgeon_sms()){
						UserProfile userpf = userProfileDAO.getUserprofileDetails(Integer.parseInt(otsurgeonname));
						String drot=userpf.getFullname()+" It has been informed you that OT " + notAvailableSlot.getProcedure() + " scheduled on date "+ DateTimeUtils.getCommencingDate1(notAvailableSlot.getCommencing())+" for patient "+notAvailableSlot.getClient()+"";
						s.sendSms(drot, userpf.getMobile(), loginInfo, new EmailLetterLog());
						}
					}

					if (bookwithpayment.equals("1")) {
						saveOpdCharge(appointmentid, notAvailableSlot, invcetype, howpaid, totalamount, discount,
								payAmount, connection, disctype,opdotcharge,opdotregcharge);
					}
					status = "Booked";
					int logsave = notAvailableSlotDAO.saveApmtInLog(appointmentid, date, time, loginInfo.getUserId(),
							clientId, commencingTemp, sTime, status,
							DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
					// }

					/*
					 * else{ //book for ot String temp[] =
					 * stafflistid.split(","); int otid =
					 * notAvailableSlotDAO.saveParenrotData(notAvailableSlot.
					 * getCommencing(),selectedot); for(int
					 * i=0;i<temp.length;i++){ if(i>0){ String selectedpractid =
					 * temp[i];
					 * notAvailableSlot.setDiaryUserId(Integer.parseInt(
					 * selectedpractid)); UserProfile userProfile2 =
					 * userProfileDAO.getUserprofileDetails(Integer.parseInt(
					 * selectedpractid)); String fullname =
					 * userProfile2.getInitial() + " " +
					 * userProfile2.getFirstname() + " " +
					 * userProfile2.getLastname();
					 * notAvailableSlot.setDiaryUser(fullname); int apmtSlotid =
					 * notAvailableSlotDAO.getAppointmentSlotID(notAvailableSlot
					 * .getCommencing(),Integer.parseInt(selectedpractid),
					 * notAvailableSlot.getSTime(),notAvailableSlot.getEndTime()
					 * ,notAvailableSlot.getLocation());
					 * notAvailableSlot.setApmtSlotId(apmtSlotid);
					 * checkEventExist =
					 * notAvailableSlotDAO.checkEventAllreadyExist(
					 * notAvailableSlot.getCommencing(),location,
					 * selectedpractid,notAvailableSlot.getSTime(),
					 * notAvailableSlot.getEndTime()); if(!checkEventExist){
					 * 
					 * notAvailableSlot.setOtid(otid); appointmentid =
					 * notAvailableSlotDAO.saveAppointment(notAvailableSlot); }
					 * }
					 * 
					 * }
					 * 
					 * System.out.println("ot code"); }
					 */

				}

				// check apmtexist
				boolean isapmtexist = notAvailableSlotDAO.checkApmtExist(notAvailableSlot.getClientId());
				if (isapmtexist) {
					int sts = 1;
					int updp = notAvailableSlotDAO.updateNewPtsStatus(notAvailableSlot.getClientId(), sts);
				} else {
					int sts = 0;
					int updp = notAvailableSlotDAO.updateNewPtsStatus(notAvailableSlot.getClientId(), sts);
				}

				/*
				 * if(appointmentid!=0){
				 * 
				 * }
				 */
				int updatusessession = diaryManagementDAO.updateTreatmentSession(usedsession,
						notAvailableSlot.getTreatmentEpisodeId());

				// log.debug("*****************appointment2");
				session.setAttribute("appointmentid", appointmentid);

				/*
				 * Clinic clinic = new Clinic(); ClinicDAO clinicDAO = new
				 * JDBCClinicDAO(connection); clinic =
				 * clinicDAO.getCliniclistDetails(loginInfo.getId()); String
				 * logo = clinic.getUserImageFileName(); String filePath =
				 * request.getRealPath("/liveData/clinicLogo/"+logo+"/");
				 * session.setAttribute("logopath", filePath);
				 */
				/*
				 * String twitter =
				 * request.getRealPath("/img/Entypo_f309(0)_32.png/");
				 * session.setAttribute("twitter", twitter);
				 * 
				 * String fb =
				 * request.getRealPath("/img/Entypo_f30c(0)_32.png/");
				 * session.setAttribute("fb", fb);
				 * 
				 * String gml =
				 * request.getRealPath("/img/Entypo_f30f(0)_32.png/");
				 * session.setAttribute("gml", gml);
				 */

				/*
				 * DBScheduler dbScheduler = new DBScheduler();
				 * dbScheduler.callScheduler(connection,loginInfo,request,
				 * appointmentid);
				 */
				// result =
				// notAvailableSlotDAO.saveCharge(notAvailableSlot,apmtType,result);
			} else {
				int selectedTreatmentEpisodeId = notAvailableSlotDAO.getSelecedTreatmentEpisodeId(selectedid);

				if (selectedTreatmentEpisodeId > 0) {

					if (selectedTreatmentEpisodeId != Integer.parseInt(notAvailableSlot.getTreatmentEpisodeId())) {
						int usedsession = diaryManagementDAO
								.getTempUsedSession(notAvailableSlot.getTreatmentEpisodeId());
						usedsession = usedsession + 1;
						if (treatmentEpisodeId.equals(zero)) {
							usedsession = 0;
						}
						int updatusessession = diaryManagementDAO.updateTreatmentSession(usedsession,
								notAvailableSlot.getTreatmentEpisodeId());

						int prevTreatmentSession = notAvailableSlotDAO
								.getPreviousTreatmentUsedSession(selectedTreatmentEpisodeId);
						prevTreatmentSession = prevTreatmentSession - 1;
						int updatePreviousTreatmentEpisode = notAvailableSlotDAO
								.updatePreviousTreatmentEpisode(prevTreatmentSession, selectedTreatmentEpisodeId);

						notAvailableSlot.setUsedsession(Integer.toString(usedsession));

					}

				}

				int usedsession = diaryManagementDAO.getTempUsedSession(notAvailableSlot.getTreatmentEpisodeId());
				// usedsession = usedsession+1;
				if (treatmentEpisodeId.equals(zero)) {
					usedsession = 0;
				}
				notAvailableSlot.setUsedsession(Integer.toString(usedsession));
				beforeUpdateData = notAvailableSlotDAO.getApmtDetailsForLog(selectedid);
				int update = notAvailableSlotDAO.updateAppointment(notAvailableSlot, selectedid);
				// update oT data
				if (!notAvailableSlot.getOtplan().equals("0")) {
					int upot = notAvailableSlotDAO.updateOTdata(notAvailableSlot, selectedid);

					// delete asistant doctor
					int delasisdoctor = notAvailableSlotDAO.deleteAsistantDoctor(selectedid);

					String temp[] = stafflistid.split(",");
					for (int i = 0; i < temp.length; i++) {
						if (i > 0) {

							int otid = notAvailableSlotDAO.saveParenrotData(notAvailableSlot.getCommencing(),
									selectedot, temp[i], selectedid);
						}
					}

					// block slot for ot
					stafflistid = stafflistid + "," + notAvailableSlot.getAnesthesia() + ","
							+ notAvailableSlot.getSurgeon();
					int delblockot = notAvailableSlotDAO.deleteBlockOt(Integer.toString(selectedid));
					String temps[] = stafflistid.split(",");
					/*
					 * for(int b=0;b<temps.length;b++){ if(b>0){ String
					 * selectedpractid = temps[b];
					 * 
					 * UserProfile userProfile2 =
					 * userProfileDAO.getUserprofileDetails(Integer.parseInt(
					 * selectedpractid)); String fullname =
					 * userProfile2.getInitial() + " " +
					 * userProfile2.getFirstname() + " " +
					 * userProfile2.getLastname();
					 * notAvailableSlot.setDiaryUser(fullname); int apmtSlotid =
					 * notAvailableSlotDAO.getOtAppointmentSlotID(
					 * notAvailableSlot.getCommencing(),Integer.parseInt(
					 * selectedpractid),notAvailableSlot.getSTime(),
					 * notAvailableSlot.getEndTime(),notAvailableSlot.
					 * getLocation());
					 * notAvailableSlot.setApmtSlotId(apmtSlotid); boolean
					 * checkEventExist =
					 * notAvailableSlotDAO.checkEventAllreadyExist(
					 * notAvailableSlot.getCommencing(),location,
					 * selectedpractid,notAvailableSlot.getSTime(),
					 * notAvailableSlot.getEndTime()); if(!checkEventExist){
					 * notAvailableSlot.setStatus("1");
					 * notAvailableSlot.setDiaryUserId(Integer.parseInt(
					 * selectedpractid));
					 * notAvailableSlot.setBlockot(selectedid);
					 * notAvailableSlot.setReasonforblock("OT Booked"); int
					 * block =
					 * notAvailableSlotDAO.saveBlockSlot(notAvailableSlot,
					 * "opd"); } } }
					 */
					TestingLog.debug("saveAppoinment()  930");
					// send sms to accountants
					double charge = notAvailableSlotDAO.getCharge(notAvailableSlot.getApmtType());
					ArrayList<Master> accUserList = notAvailableSlotDAO.getAccountUserList();
					for (Master mstr : accUserList) {
						UserProfile u = userProfileDAO.getUserprofileDetails(mstr.getId());
						String accountant = u.getInitial() + " " + u.getFirstname() + " " + u.getLastname();

						String otmsg = accountant + "it has been informed you that " + notAvailableSlot.getClient()
								+ " has OT " + notAvailableSlot.getProcedure() + " scheduled on date "
								+ notAvailableSlot.getCommencing() + " of " + Constants.getCurrency(loginInfo)
								+ DateTimeUtils.changeFormat(charge) + " kindly check payment";

						SendSms os = new SendSms();
						os.send(otmsg, u.getMobile(), loginInfo, new EmailLetterLog());
					}
					SmsService s = new SmsService();
					if(loginInfo.isOt_patient_sms()){
						UserProfile userpf = userProfileDAO.getUserprofileDetails(Integer.parseInt(otsurgeonname));
					String patientot=notAvailableSlot.getClient()+" It has been informed you that OT " + notAvailableSlot.getProcedure() + " scheduled on date "+ DateTimeUtils.getCommencingDate1(notAvailableSlot.getCommencing())+"";
					s.sendSms(patientot, client2.getMobNo(), loginInfo, new EmailLetterLog());
					}
					if(loginInfo.isOt_surgeon_sms()){
					UserProfile userpf = userProfileDAO.getUserprofileDetails(Integer.parseInt(otsurgeonname));
					String drot=userpf.getFullname()+" It has been informed you that OT " + notAvailableSlot.getProcedure() + " scheduled on date "+ DateTimeUtils.getCommencingDate1(notAvailableSlot.getCommencing())+" for patient "+notAvailableSlot.getClient()+"";
					s.sendSms(drot, userpf.getMobile(), loginInfo, new EmailLetterLog());
					}
				}

				appointmentid = selectedid;
				status = "Modified";
				int logsave = notAvailableSlotDAO.saveApmtInLog(appointmentid, date, time, loginInfo.getUserId(),
						clientId, commencingTemp, sTime, status,
						DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));

				System.out.println("hello");
				CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(connection);
				int results = completeAptmDAO.deleteComplteApmt(loginInfo.getId());

				if (selectedid > 0) {
					// set all treatment episode sessions

					TreatmentEpisodeDAO treatmentEpisodeDAO = new JDBCTreatmentEpisode(connection);
					ArrayList<TreatmentEpisode> treatmentEpisodeList = treatmentEpisodeDAO
							.getTreatmentEpisodeList(clientId);
					for (TreatmentEpisode treatmentEpisode : treatmentEpisodeList) {

						ArrayList<NotAvailableSlot> dnaoffsetList = notAvailableSlotDAO
								.getDnaOffsetList(Integer.toString(treatmentEpisode.getId()));
						int i = 1;
						for (NotAvailableSlot nt : dnaoffsetList) {

							int updateusedsession = notAvailableSlotDAO.updateAppointmentUsedSession(nt.getId(), i);

							i++;

						}

						int updatetepisodeusedsession = notAvailableSlotDAO.updateupdateTpEpisodeusedsession(
								Integer.toString(treatmentEpisode.getId()), dnaoffsetList.size());
					}

				}

				// log.debug("*****************appointment3");
				session.setAttribute("appointmentid", selectedid);

				/*
				 * String filePath =
				 * request.getRealPath("/Design/images/logo.png/");
				 * session.setAttribute("logopath", filePath);
				 */
				/*
				 * DBScheduler dbScheduler = new DBScheduler();
				 * dbScheduler.callScheduler(connection,loginInfo,request,
				 * appointmentid);
				 */

			}

			// update client condition
			int updatecondition = notAvailableSlotDAO.updateClientCondition(notAvailableSlot.getClientId(), condition);

			boolean wholeweek = Boolean.parseBoolean(request.getParameter("wholeweek"));
			int weekNumber = Integer.parseInt(request.getParameter("weekNumber"));

			// repeat code

			if (weekNumber > 0) {
				boolean monday = Boolean.parseBoolean(request.getParameter("monday"));
				boolean tuesday = Boolean.parseBoolean(request.getParameter("tuesday"));
				boolean wednesday = Boolean.parseBoolean(request.getParameter("wednesday"));
				boolean thursday = Boolean.parseBoolean(request.getParameter("thursday"));
				boolean friday = Boolean.parseBoolean(request.getParameter("friday"));
				boolean saturday = Boolean.parseBoolean(request.getParameter("saturday"));
				boolean sunday = Boolean.parseBoolean(request.getParameter("sunday"));

				weekNumber = Integer.parseInt(request.getParameter("weekNumber"));

				String dt = notAvailableSlot.getCommencing();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar c = Calendar.getInstance();
				c.setTime(sdf.parse(dt));

				for (int i = 0; i < weekNumber; i++) {

					for (int j = 0; j <= 6; j++) {

						c.add(Calendar.DATE, 1); // number of days to add
						dt = sdf.format(c.getTime()); // dt is now the new date

						notAvailableSlot.setCommencing(dt);
						commencingTemp = dt;

						String wcdate[] = dt.split("-");

						// set weekname
						int wyear = Integer.parseInt(wcdate[0]);
						int month = Integer.parseInt(wcdate[1]);
						int day = Integer.parseInt(wcdate[2]);

						String cweekName = DateTimeUtils.getWeekName(wyear, month, day);

						System.out.println(dt);
						System.out.println(cweekName);

						if (monday && cweekName.equals(Constants.MONDAY)) {

							// common for all
							int apmtSlotid = notAvailableSlotDAO.getAppointmentSlotID(notAvailableSlot.getCommencing(),
									notAvailableSlot.getDiaryUserId(), notAvailableSlot.getSTime(),
									notAvailableSlot.getEndTime(), notAvailableSlot.getLocation());
							if (apmtSlotid != 0) {
								notAvailableSlot.setApmtSlotId(apmtSlotid);
								boolean checkEventExist = notAvailableSlotDAO.checkEventAllreadyExist(
										notAvailableSlot.getCommencing(), location, diaryuserId,
										notAvailableSlot.getSTime(), notAvailableSlot.getEndTime());
								if (checkEventExist) {
									checkEventExist = cheForWeekRepeatAfterEventExist(notAvailableSlot.getCommencing(),
											location, diaryuserId, notAvailableSlot.getSTime(),
											notAvailableSlot.getEndTime());
								}
								if (!checkEventExist) {
									int usedsession = diaryManagementDAO
											.getTempUsedSession(notAvailableSlot.getTreatmentEpisodeId());
									int sessions = diaryManagementDAO
											.getTempSessions(notAvailableSlot.getTreatmentEpisodeId());
									if (usedsession == sessions) {
										if (sessions > 0) {
											break;
										}
									}
									usedsession = usedsession + 1;
									if (treatmentEpisodeId.equals(zero)) {
										usedsession = 0;
									}
									notAvailableSlot.setUsedsession(Integer.toString(usedsession));
									appointmentid = notAvailableSlotDAO.saveAppointment(notAvailableSlot);
									int updatusessession = diaryManagementDAO.updateTreatmentSession(usedsession,
											notAvailableSlot.getTreatmentEpisodeId());
									// Log Save
									status = "Booked";
									int logsave = notAvailableSlotDAO.saveApmtInLog(appointmentid, date, time,
											loginInfo.getUserId(), clientId, commencingTemp, sTime, status,
											DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
								} else {
									break;
								}

							} else {
								// break;
							}

						} else if (tuesday && cweekName.equals(Constants.TUESDAY)) {
							// common for all
							int apmtSlotid = notAvailableSlotDAO.getAppointmentSlotID(notAvailableSlot.getCommencing(),
									notAvailableSlot.getDiaryUserId(), notAvailableSlot.getSTime(),
									notAvailableSlot.getEndTime(), notAvailableSlot.getLocation());
							if (apmtSlotid != 0) {
								notAvailableSlot.setApmtSlotId(apmtSlotid);
								boolean checkEventExist = notAvailableSlotDAO.checkEventAllreadyExist(
										notAvailableSlot.getCommencing(), location, diaryuserId,
										notAvailableSlot.getSTime(), notAvailableSlot.getEndTime());
								if (checkEventExist) {
									checkEventExist = cheForWeekRepeatAfterEventExist(notAvailableSlot.getCommencing(),
											location, diaryuserId, notAvailableSlot.getSTime(),
											notAvailableSlot.getEndTime());
								}
								if (!checkEventExist) {
									int usedsession = diaryManagementDAO
											.getTempUsedSession(notAvailableSlot.getTreatmentEpisodeId());
									int sessions = diaryManagementDAO
											.getTempSessions(notAvailableSlot.getTreatmentEpisodeId());
									if (usedsession == sessions) {
										if (sessions > 0) {
											break;
										}
									}
									usedsession = usedsession + 1;
									if (treatmentEpisodeId.equals(zero)) {
										usedsession = 0;
									}
									notAvailableSlot.setUsedsession(Integer.toString(usedsession));
									appointmentid = notAvailableSlotDAO.saveAppointment(notAvailableSlot);
									int updatusessession = diaryManagementDAO.updateTreatmentSession(usedsession,
											notAvailableSlot.getTreatmentEpisodeId());
									// Log Save
									status = "Booked";
									int logsave = notAvailableSlotDAO.saveApmtInLog(appointmentid, date, time,
											loginInfo.getUserId(), clientId, commencingTemp, sTime, status,
											DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
								} else {
									break;
								}

							} else {
								// break;
							}
						} else if (wednesday && cweekName.equals(Constants.WEDNEDAY)) {
							// common for all
							int apmtSlotid = notAvailableSlotDAO.getAppointmentSlotID(notAvailableSlot.getCommencing(),
									notAvailableSlot.getDiaryUserId(), notAvailableSlot.getSTime(),
									notAvailableSlot.getEndTime(), notAvailableSlot.getLocation());
							if (apmtSlotid != 0) {
								notAvailableSlot.setApmtSlotId(apmtSlotid);
								boolean checkEventExist = notAvailableSlotDAO.checkEventAllreadyExist(
										notAvailableSlot.getCommencing(), location, diaryuserId,
										notAvailableSlot.getSTime(), notAvailableSlot.getEndTime());
								if (checkEventExist) {
									checkEventExist = cheForWeekRepeatAfterEventExist(notAvailableSlot.getCommencing(),
											location, diaryuserId, notAvailableSlot.getSTime(),
											notAvailableSlot.getEndTime());
								}
								if (!checkEventExist) {
									int usedsession = diaryManagementDAO
											.getTempUsedSession(notAvailableSlot.getTreatmentEpisodeId());
									int sessions = diaryManagementDAO
											.getTempSessions(notAvailableSlot.getTreatmentEpisodeId());
									if (usedsession == sessions) {
										if (sessions > 0) {
											break;
										}
									}
									usedsession = usedsession + 1;
									if (treatmentEpisodeId.equals(zero)) {
										usedsession = 0;
									}
									notAvailableSlot.setUsedsession(Integer.toString(usedsession));
									appointmentid = notAvailableSlotDAO.saveAppointment(notAvailableSlot);
									int updatusessession = diaryManagementDAO.updateTreatmentSession(usedsession,
											notAvailableSlot.getTreatmentEpisodeId());
									// Log Save
									status = "Booked";
									int logsave = notAvailableSlotDAO.saveApmtInLog(appointmentid, date, time,
											loginInfo.getUserId(), clientId, commencingTemp, sTime, status,
											DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
								} else {
									break;
								}

							} else {
								// break;
							}
						} else if (thursday && cweekName.equals(Constants.THUSRDAY)) {
							// common for all
							int apmtSlotid = notAvailableSlotDAO.getAppointmentSlotID(notAvailableSlot.getCommencing(),
									notAvailableSlot.getDiaryUserId(), notAvailableSlot.getSTime(),
									notAvailableSlot.getEndTime(), notAvailableSlot.getLocation());
							if (apmtSlotid != 0) {
								notAvailableSlot.setApmtSlotId(apmtSlotid);
								boolean checkEventExist = notAvailableSlotDAO.checkEventAllreadyExist(
										notAvailableSlot.getCommencing(), location, diaryuserId,
										notAvailableSlot.getSTime(), notAvailableSlot.getEndTime());
								if (checkEventExist) {
									checkEventExist = cheForWeekRepeatAfterEventExist(notAvailableSlot.getCommencing(),
											location, diaryuserId, notAvailableSlot.getSTime(),
											notAvailableSlot.getEndTime());
								}
								if (!checkEventExist) {
									int usedsession = diaryManagementDAO
											.getTempUsedSession(notAvailableSlot.getTreatmentEpisodeId());
									int sessions = diaryManagementDAO
											.getTempSessions(notAvailableSlot.getTreatmentEpisodeId());
									if (usedsession == sessions) {
										if (sessions > 0) {
											break;
										}
									}
									usedsession = usedsession + 1;
									if (treatmentEpisodeId.equals(zero)) {
										usedsession = 0;
									}
									notAvailableSlot.setUsedsession(Integer.toString(usedsession));
									appointmentid = notAvailableSlotDAO.saveAppointment(notAvailableSlot);
									int updatusessession = diaryManagementDAO.updateTreatmentSession(usedsession,
											notAvailableSlot.getTreatmentEpisodeId());
									// Log Save
									status = "Booked";
									int logsave = notAvailableSlotDAO.saveApmtInLog(appointmentid, date, time,
											loginInfo.getUserId(), clientId, commencingTemp, sTime, status,
											DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
								} else {
									break;
								}

							} else {
								// break;
							}
						} else if (friday && cweekName.equals(Constants.FRIDAY)) {
							// common for all
							int apmtSlotid = notAvailableSlotDAO.getAppointmentSlotID(notAvailableSlot.getCommencing(),
									notAvailableSlot.getDiaryUserId(), notAvailableSlot.getSTime(),
									notAvailableSlot.getEndTime(), notAvailableSlot.getLocation());
							if (apmtSlotid != 0) {
								notAvailableSlot.setApmtSlotId(apmtSlotid);
								boolean checkEventExist = notAvailableSlotDAO.checkEventAllreadyExist(
										notAvailableSlot.getCommencing(), location, diaryuserId,
										notAvailableSlot.getSTime(), notAvailableSlot.getEndTime());
								if (checkEventExist) {
									checkEventExist = cheForWeekRepeatAfterEventExist(notAvailableSlot.getCommencing(),
											location, diaryuserId, notAvailableSlot.getSTime(),
											notAvailableSlot.getEndTime());
								}
								if (!checkEventExist) {
									int usedsession = diaryManagementDAO
											.getTempUsedSession(notAvailableSlot.getTreatmentEpisodeId());
									int sessions = diaryManagementDAO
											.getTempSessions(notAvailableSlot.getTreatmentEpisodeId());
									if (usedsession == sessions) {
										if (sessions > 0) {
											break;
										}
									}
									usedsession = usedsession + 1;
									if (treatmentEpisodeId.equals(zero)) {
										usedsession = 0;
									}
									notAvailableSlot.setUsedsession(Integer.toString(usedsession));
									appointmentid = notAvailableSlotDAO.saveAppointment(notAvailableSlot);
									int updatusessession = diaryManagementDAO.updateTreatmentSession(usedsession,
											notAvailableSlot.getTreatmentEpisodeId());
									// Log Save
									status = "Booked";
									int logsave = notAvailableSlotDAO.saveApmtInLog(appointmentid, date, time,
											loginInfo.getUserId(), clientId, commencingTemp, sTime, status,
											DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
								} else {
									break;
								}

							} else {
								// break;
							}
						} else if (saturday && cweekName.equals(Constants.SATURDAY)) {
							// common for all
							int apmtSlotid = notAvailableSlotDAO.getAppointmentSlotID(notAvailableSlot.getCommencing(),
									notAvailableSlot.getDiaryUserId(), notAvailableSlot.getSTime(),
									notAvailableSlot.getEndTime(), notAvailableSlot.getLocation());
							if (apmtSlotid != 0) {
								notAvailableSlot.setApmtSlotId(apmtSlotid);
								boolean checkEventExist = notAvailableSlotDAO.checkEventAllreadyExist(
										notAvailableSlot.getCommencing(), location, diaryuserId,
										notAvailableSlot.getSTime(), notAvailableSlot.getEndTime());
								if (checkEventExist) {
									checkEventExist = cheForWeekRepeatAfterEventExist(notAvailableSlot.getCommencing(),
											location, diaryuserId, notAvailableSlot.getSTime(),
											notAvailableSlot.getEndTime());
								}
								if (!checkEventExist) {
									int usedsession = diaryManagementDAO
											.getTempUsedSession(notAvailableSlot.getTreatmentEpisodeId());
									int sessions = diaryManagementDAO
											.getTempSessions(notAvailableSlot.getTreatmentEpisodeId());
									if (usedsession == sessions) {
										if (sessions > 0) {
											break;
										}
									}
									usedsession = usedsession + 1;
									if (treatmentEpisodeId.equals(zero)) {
										usedsession = 0;
									}
									notAvailableSlot.setUsedsession(Integer.toString(usedsession));
									appointmentid = notAvailableSlotDAO.saveAppointment(notAvailableSlot);
									int updatusessession = diaryManagementDAO.updateTreatmentSession(usedsession,
											notAvailableSlot.getTreatmentEpisodeId());
									// Log Save
									status = "Booked";
									int logsave = notAvailableSlotDAO.saveApmtInLog(appointmentid, date, time,
											loginInfo.getUserId(), clientId, commencingTemp, sTime, status,
											DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
								} else {
									break;
								}

							} else {
								// break;
							}
						} else if (sunday && cweekName.equals(Constants.SUNDAY)) {
							// common for all
							int apmtSlotid = notAvailableSlotDAO.getAppointmentSlotID(notAvailableSlot.getCommencing(),
									notAvailableSlot.getDiaryUserId(), notAvailableSlot.getSTime(),
									notAvailableSlot.getEndTime(), notAvailableSlot.getLocation());
							if (apmtSlotid != 0) {
								notAvailableSlot.setApmtSlotId(apmtSlotid);
								boolean checkEventExist = notAvailableSlotDAO.checkEventAllreadyExist(
										notAvailableSlot.getCommencing(), location, diaryuserId,
										notAvailableSlot.getSTime(), notAvailableSlot.getEndTime());
								if (checkEventExist) {
									checkEventExist = cheForWeekRepeatAfterEventExist(notAvailableSlot.getCommencing(),
											location, diaryuserId, notAvailableSlot.getSTime(),
											notAvailableSlot.getEndTime());
								}
								if (!checkEventExist) {
									int usedsession = diaryManagementDAO
											.getTempUsedSession(notAvailableSlot.getTreatmentEpisodeId());
									int sessions = diaryManagementDAO
											.getTempSessions(notAvailableSlot.getTreatmentEpisodeId());
									if (usedsession == sessions) {
										if (sessions > 0) {
											break;
										}
									}
									usedsession = usedsession + 1;
									if (treatmentEpisodeId.equals(zero)) {
										usedsession = 0;
									}
									notAvailableSlot.setUsedsession(Integer.toString(usedsession));
									appointmentid = notAvailableSlotDAO.saveAppointment(notAvailableSlot);
									int updatusessession = diaryManagementDAO.updateTreatmentSession(usedsession,
											notAvailableSlot.getTreatmentEpisodeId());
									// Log Save
									status = "Booked";
									int logsave = notAvailableSlotDAO.saveApmtInLog(appointmentid, date, time,
											loginInfo.getUserId(), clientId, commencingTemp, sTime, status,
											DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
								} else {
									break;
								}

							} else {
								// break;
							}
						} else if (!monday && !tuesday && !wednesday && !thursday && !friday && !saturday && !sunday) {
							// common for all
							int apmtSlotid = notAvailableSlotDAO.getAppointmentSlotID(notAvailableSlot.getCommencing(),
									notAvailableSlot.getDiaryUserId(), notAvailableSlot.getSTime(),
									notAvailableSlot.getEndTime(), notAvailableSlot.getLocation());
							if (apmtSlotid != 0) {
								notAvailableSlot.setApmtSlotId(apmtSlotid);
								boolean checkEventExist = notAvailableSlotDAO.checkEventAllreadyExist(
										notAvailableSlot.getCommencing(), location, diaryuserId,
										notAvailableSlot.getSTime(), notAvailableSlot.getEndTime());
								if (checkEventExist) {
									checkEventExist = cheForWeekRepeatAfterEventExist(notAvailableSlot.getCommencing(),
											location, diaryuserId, notAvailableSlot.getSTime(),
											notAvailableSlot.getEndTime());
								}
								if (!checkEventExist) {
									int usedsession = diaryManagementDAO
											.getTempUsedSession(notAvailableSlot.getTreatmentEpisodeId());
									int sessions = diaryManagementDAO
											.getTempSessions(notAvailableSlot.getTreatmentEpisodeId());
									if (usedsession == sessions) {
										if (sessions > 0) {
											break;
										}
									}
									usedsession = usedsession + 1;
									if (treatmentEpisodeId.equals(zero)) {
										usedsession = 0;
									}
									notAvailableSlot.setUsedsession(Integer.toString(usedsession));
									appointmentid = notAvailableSlotDAO.saveAppointment(notAvailableSlot);
									int updatusessession = diaryManagementDAO.updateTreatmentSession(usedsession,
											notAvailableSlot.getTreatmentEpisodeId());
									// Log Save
									status = "Booked";
									int logsave = notAvailableSlotDAO.saveApmtInLog(appointmentid, date, time,
											loginInfo.getUserId(), clientId, commencingTemp, sTime, status,
											DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
								} else {
									break;
								}

							} else {
								// break;
							}
						}

					}
				}
			}

			session.setAttribute("connection", connection);

			notAvailableSlot = notAvailableSlotDAO.getAvailableSlotdata(appointmentid);

			String diaryuseridname = notAvailableSlotDAO.getDiaryUserIdName(notAvailableSlot.getDiaryUserId());

			String updatedAppointmentData = notAvailableSlot.getCommencing() + "/" + notAvailableSlot.getsTime() + "/"
					+ notAvailableSlot.getEndTime() + "/" + notAvailableSlot.getDiaryUser() + "/"
					+ notAvailableSlot.getClientName() + "/" + notAvailableSlot.getDiaryUserId() + "/"
					+ notAvailableSlot.getId() + "/" + notAvailableSlot.getClientId() + "/" + loginInfo.getClinicid()
					+ "/" + loginInfo.getUserType() + "/" + loginInfo.getClinicUserid() + "/" + diaryuseridname;
			TestingLog.debug("saveAppoinment()  728");
			// send sms
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			boolean isSMSActive = clinicDAO.isSmsActive(loginInfo.getId());
			if (isSMSActive) {
				TestingLog.debug("saveAppoinment()  728"+isSMSActive);
			}
			// autosms(Integer.toString(appointmentid), status
			// ,beforeUpdateData.getSTime() ,beforeUpdateData.getCommencing() );

			// autosms(Integer.toString(appointmentid), status
			// ,beforeUpdateData.getSTime()
			// ,beforeUpdateData.getCommencing(),seqno );
			
			Client client3=new Client();
			client3.setHeight(height);
			client3.setWeight(weight);
			client3.setBmi(bmi);
			client3.setHead_cir(headcir);
			client3.setTemprature(tempr);
			client3.setClientid(clientId);
			client3.setAppointmentid(String.valueOf(appointmentid));
			int res=clientDAO.saveBMIPatient(client3);
			
			//Akash 27-05-2019
			int ageinmonth = DateTimeUtils.getmonthsfromdob(client2.getDob());
			if(ageinmonth<=60 && ageinmonth>=0){
				if(height!=null){
					if(!height.equals("") && !height.equals("0")){
						Client client4 = new Client();
						client4.setHeightdata(height);
						client4.setWeightdata(weight);
						client4.setBmidata(bmi);
						client4.setHeadcircumferncedata(headcir);
						client4.setClientid(clientId);
						client4.setMonth(""+ageinmonth);
						client4.setUserid(loginInfo.getUserId());
						String datetime = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
						client4.setDate(datetime);
						int id = clientDAO.check_child_growth_data(clientId, ""+ageinmonth);
						int res1 = 0;
						if (id > 0) {
							client4.setId(id);
							// update
							res1 = clientDAO.updateChildGrowthData(client4, "height");
						} else {
							// insert
							res1 = clientDAO.saveChildGrowthData(client4, "height");
						}
					}
				}
				if(weight!=null){
					if(!weight.equals("") && !weight.equals("0")){
						Client client4 = new Client();
						client4.setHeightdata(height);
						client4.setWeightdata(weight);
						client4.setBmidata(bmi);
						client4.setHeadcircumferncedata(headcir);
						client4.setClientid(clientId);
						client4.setMonth(""+ageinmonth);
						client4.setUserid(loginInfo.getUserId());
						String datetime = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
						client4.setDate(datetime);
						int id = clientDAO.check_child_growth_data(clientId, ""+ageinmonth);
						int res1 = 0;
						if (id > 0) {
							client4.setId(id);
							// update
							res1 = clientDAO.updateChildGrowthData(client4, "weight");
						} else {
							// insert
							res1 = clientDAO.saveChildGrowthData(client4, "weight");
						}
					}
				}
				if(bmi!=null){
					if(!bmi.equals("") && !bmi.equals("0")){
						Client client4 = new Client();
						client4.setHeightdata(height);
						client4.setWeightdata(weight);
						client4.setBmidata(bmi);
						client4.setHeadcircumferncedata(headcir);
						client4.setClientid(clientId);
						client4.setMonth(""+ageinmonth);
						client4.setUserid(loginInfo.getUserId());
						String datetime = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
						client4.setDate(datetime);
						int id = clientDAO.check_child_growth_data(clientId, ""+ageinmonth);
						int res1 = 0;
						if (id > 0) {
							client4.setId(id);
							// update
							res1 = clientDAO.updateChildGrowthData(client4, "bmi");
						} else {
							// insert
							res1 = clientDAO.saveChildGrowthData(client4, "bmi");
						}
					}
				}
				if(headcir!=null){
					if(!headcir.equals("") && !headcir.equals("0")){
						Client client4 = new Client();
						client4.setHeightdata(height);
						client4.setWeightdata(weight);
						client4.setBmidata(bmi);
						client4.setHeadcircumferncedata(headcir);
						client4.setClientid(clientId);
						client4.setMonth(""+ageinmonth);
						client4.setUserid(loginInfo.getUserId());
						String datetime = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
						client4.setDate(datetime);
						int id = clientDAO.check_child_growth_data(clientId, ""+ageinmonth);
						int res1 = 0;
						if (id > 0) {
							client4.setId(id);
							// update
							res1 = clientDAO.updateChildGrowthData(client4, "headcircumfernce");
						} else {
							// insert
							res1 = clientDAO.saveChildGrowthData(client4, "headcircumfernce");
						}
					}
				}

			}
			TestingLog.debug("saveAppoinment()  1548"+isSMSActive);
			// @ruchi check smscheck then send msg
			autosmsCheckSms(Integer.toString(appointmentid), status, beforeUpdateData.getSTime(),
					beforeUpdateData.getCommencing(), seqno);
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("" + updatedAppointmentData + "");

		} catch (Exception e) {
			e.printStackTrace();
			// log.debug("***************"+e.getMessage());
			TestingLog.debug("saveAppoinment()  "+e.getMessage()+"");
		}

		finally {
			connection.close();
		}
		TestingLog.debug("saveAppoinment()  1566");
		return null;
	}

	public boolean cheForWeekRepeatAfterEventExist(String commencing, String location, String diaryuserId,
			String starttime, String endtime) throws Exception {
		boolean checkEventExist = true;
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			int coutnEsistingSlot = notAvailableSlotDAO.coutnEsistingSlot(commencing, location, diaryuserId, starttime,
					endtime, 0);

			if (coutnEsistingSlot == 1) {
				String existStartTime = notAvailableSlotDAO.getExistStartTime(commencing, location, diaryuserId,
						starttime, endtime, 0);

				String duration = DateTimeUtils.getDuration(starttime, endtime);

				System.out.println(duration);

				String sumoftime = DateTimeUtils.getSumofTime(starttime, duration);

				if (sumoftime.equals(existStartTime)) {
					checkEventExist = false;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			connection.close();
		}
		return checkEventExist;

	}

	private void saveOpdCharge(int appointmentid, NotAvailableSlot notAvailableSlot, String invcetype, String howpaid,
			String totalamount, String discounts, String payAmount, Connection connection, String disctype, String opdotcharge, String opdotregcharge)
			throws Exception {

		try {

			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			AccountLogDAO accountLogDAO = new JDBCAccountLogDAO(connection);
			CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(connection);
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);

			String location = notAvailableSlot.getLocation();
			String chargeType = "Submit";

			String temp[] = notAvailableSlot.getCommencing().split("-");
			String commencing = temp[2] + "-" + temp[1] + "-" + temp[0];
			double debit = Double.parseDouble(totalamount);
			if(discounts==null){
				discounts="0";
			}
			if(discounts.equals("")){
				discounts="0";
			}
			double discount = Double.parseDouble(discounts);

			ArrayList<Accounts> invoiceList = new ArrayList<Accounts>();
			int thirdPartyID = accountsDAO.getThirdPartyID(notAvailableSlot.getClientId());

			String payBuy = "0";
			if (notAvailableSlot.getPayBy().equals(Constants.PAY_BY_THIRD_PARTY)) {
				payBuy = "1";
			}

			String date = DateTimeUtils.getDateinSimpleFormate(new Date());
			String stemp[] = date.split(" ");

			String temps[] = stemp[0].split("-");
			date = temps[2] + "-" + temps[1] + "-" + temps[0];

			ClientDAO clientDAO = new JDBCClientDAO(connection);
			Client client = clientDAO.getClientDetails(notAvailableSlot.getClientId());
			String clientname = client.getTitle() + " " + client.getFirstName() + " " + client.getLastName();

			CompleteAppointment completeAppointment = new CompleteAppointment();
			completeAppointment.setClientId(notAvailableSlot.getClientId());
			completeAppointment.setPractitionerId(Integer.toString(notAvailableSlot.getDiaryUserId()));
			completeAppointment.setUser(clientname);
			completeAppointment.setInvoiceDate(date);
			completeAppointment.setChargeId(notAvailableSlot.getApmtType());
			completeAppointment.setChargeType("Charge");
			completeAppointment.setAppointmentid(Integer.toString(appointmentid));
			completeAppointment.setPractitionerName(notAvailableSlot.getDiaryUser());
			completeAppointment.setLocation(location);
			completeAppointment.setIpd("0");
			completeAppointment.setGpriscid("0");
			completeAppointment.setGinvstid("0");
			completeAppointment.setAdditionalcharge_id(null);
			completeAppointment.setWardid(null);
			completeAppointment.setPayBuy(payBuy);
			String apmtTYpeText = notAvailableSlotDAO.getAppointmentTypeText(notAvailableSlot.getApmtType());
			completeAppointment.setApmtType(apmtTYpeText);
			completeAppointment.setQuantity(1);
			completeAppointment.setCommencing(notAvailableSlot.getCommencing());
			completeAppointment.setMasterchargetype("Appointment Charge");

			// insert into apm_invoice
			int selfInvoice = completeAptmDAO.saveAmpmInvoice(completeAppointment, loginInfo.getId(),loginInfo.getUserId());

			// insert in apm_invoice_assessment
			//completeAppointment.setCharges(totalamount);
			completeAppointment.setCharges(opdotcharge);
			NotAvailableSlot not = notAvailableSlotDAO.getOTData(Integer.toString(appointmentid));
			if (!notAvailableSlot.getProcedure().equals("0")) {
				completeAppointment.setCharges(not.getChargeamout());
				completeAppointment.setMasterchargetype(notAvailableSlot.getProcedure());
			}
			int result = completeAptmDAO.saveInvoiceAssesment(completeAppointment, selfInvoice);

			// save ot more charges

			if (!notAvailableSlot.getProcedure().equals("0")) {

				//completeAppointment.setMasterchargetype(notAvailableSlot.getProcedure());
				completeAppointment.setMasterchargetype("IP / OP PROCEDURE");

				completeAppointment.setApmtType(Constants.SURGEON_CHARGE);
				completeAppointment.setCharges(notAvailableSlot.getPsurcharge());
				result = completeAptmDAO.saveInvoiceAssesment(completeAppointment, selfInvoice);

				completeAppointment.setApmtType(Constants.ANISTHESIA_CHARGE);
				completeAppointment.setCharges(notAvailableSlot.getPanetcharge());
				result = completeAptmDAO.saveInvoiceAssesment(completeAppointment, selfInvoice);

				completeAppointment.setApmtType(Constants.SIC_CHARGE);
				completeAppointment.setCharges(notAvailableSlot.getSic());
				result = completeAptmDAO.saveInvoiceAssesment(completeAppointment, selfInvoice);
				
				//Akash 11 July 2018 assiting staff charges
				completeAppointment.setApmtType(Constants.ASSISTING_STAFF_CHARGE);
				if(notAvailableSlot.getAssistaffcharge()!=null){
					if(notAvailableSlot.getAssistaffcharge().equals("")){
						notAvailableSlot.setAssistaffcharge("0");
					}
				}else{
					notAvailableSlot.setAssistaffcharge("0");
				}
				completeAppointment.setCharges(notAvailableSlot.getAssistaffcharge());
				result = completeAptmDAO.saveInvoiceAssesment(completeAppointment, selfInvoice);
				
			}else{
				//Akash 12 July 2018 /to set registration charge
				boolean flag = completeAptmDAO.isclientIdInApmt(notAvailableSlot.getClientId());
				boolean isfirsttimereg=completeAptmDAO.isOPDFirstCharge(notAvailableSlot.getClientId(), "");
				boolean issetchargenew= false;
				if(!flag&&isfirsttimereg){
					issetchargenew=true;
				}
				if(!flag||issetchargenew){
					if(opdotregcharge!=null){
						if(!opdotregcharge.equals("")){
							if(Double.parseDouble(opdotregcharge)>0){
								//double opdregcharge =  completeAptmDAO.getOpdRegCharge();
								completeAppointment.setApmtType("OPD Registration Charge");
								completeAppointment.setCharges(opdotregcharge);
								completeAppointment.setMasterchargetype("Registration Charge");
								result = completeAptmDAO.saveInvoiceAssesment(completeAppointment, selfInvoice);
							}
						}
					}
				}
			}

			ChargesAccountProcessingDAO chargesAccountProcessingDAO = new JDBCChargeAccountProcesDAO(connection);
			// logdata
			String status = "Created";
			int log = accountLogDAO.saveAmpmInvoice(completeAppointment, selfInvoice, status,
					DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));

			// reset invoice
			int resetinv = accountsDAO.getMaxResetInv();
			int resetcreditinv = accountsDAO.getMaxResetCreditInv();
			int rinv = 0;
			if (resetinv > resetcreditinv) {
				rinv = resetinv + 1;
			} else {
				rinv = resetcreditinv + 1;
			}
			IpdDAO ipdDAO = new JDBCIpdDAO(connection);
			int ipdid = 0;
			if (invcetype != null) {
				if (invcetype.equals("2")) {
					ipdid = ipdDAO.getLastIpdId(notAvailableSlot.getClientId());
				} else {
					ipdid = 0;
				}
			} else {
				ipdid = 0;
			}
			int invoiceid = accountsDAO.saveChargesInvoice(notAvailableSlot.getPayBy(),
					DateTimeUtils.getCommencingDate1(commencing), Integer.parseInt(notAvailableSlot.getClientId()),
					debit, discount, "", thirdPartyID, location,
					DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()), loginInfo.getId(),
					"0", invcetype, rinv, null, null, String.valueOf(notAvailableSlot.getDiaryUserId()), ipdid);
			DateFormat dateFormat1 = new SimpleDateFormat("yyyy");
			Calendar cal1 = Calendar.getInstance();
			String a_year = dateFormat1.format(cal1.getTime());
			//lokeh for generating seqno
			int res1 = accountsDAO.getMaxOpdseqNo(a_year);
			res1 =  res1+1;
			int ress = accountsDAO.updateInvoiceSeqNo("1",res1,invoiceid,a_year);

		
			// posting opd invoice
			String podate = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());

			int res = chargesAccountProcessingDAO.updateIclosed(Integer.toString(invoiceid), podate);
			if(loginInfo.isShow_unpost()!=true){
				res = chargesAccountProcessingDAO.updateIpost(Integer.toString(invoiceid), podate);
			}
			// ledger for credit invoice
			if (invoiceid > 0) {
				String itype = accountsDAO.getInvoiceTypeId(invoiceid);
				// String serviceid =
				// chargesAccountProcessingDAO.getLedgrServiceIds(itype);
				String serviceid = itype;
				String ledgerid = chargesAccountProcessingDAO.getledgerID(serviceid, "0", "0");

				double lbal = chargesAccountProcessingDAO.getLedgerBalance(ledgerid);
				lbal = lbal + debit;
				String credit = "" + debit + "";
				String ldebit = "0";
				String product = "xxxxx";
				String partyid = notAvailableSlot.getClientId();
				String lcommencing = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
				int saveledger = chargesAccountProcessingDAO.saveLedger(partyid, product, ldebit, credit, lbal,
						ledgerid, lcommencing, "" + invoiceid + "", 0,"0","0","0","0","0",0,0,"0");
				
				//second effect
				 lbal = 0;
				 credit = "0";
				 ldebit = "" + debit + "";
				 product = "xxxxx";
				 partyid = notAvailableSlot.getClientId();
				 lcommencing = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
				 saveledger = chargesAccountProcessingDAO.saveLedger(partyid, product, ldebit, credit, lbal,
						ledgerid, lcommencing, "" + invoiceid + "", 0,"0","0","0","0","0",0,0,"0");
			}
			// update discount
			String userid = loginInfo.getUserId();
			String datetime = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
			int p = accountsDAO.updatePercentageAmount(Integer.toString(invoiceid), discounts, disctype, userid,
					datetime);

			double discdebit = Double.parseDouble(discounts);
			if (disctype.equals("0")) {
				discdebit = (debit * Double.parseDouble(discounts)) / 100;
			}

			// discount ledger
			if (discdebit > 0) {

				String serviceid = chargesAccountProcessingDAO.getLedgrServiceIds("Discount");
				String ledgerid = chargesAccountProcessingDAO.getledgerID(serviceid, "0", "0");

				double lbal = chargesAccountProcessingDAO.getLedgerBalance(ledgerid);
				lbal = lbal + discdebit;
				String credit = "" + discdebit + "";
				String ldebit = "0";
				String product = "" + invoiceid + "";
				String partyid = notAvailableSlot.getClientId();
				String lcommencing = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
				int saveledger = chargesAccountProcessingDAO.saveLedger(partyid, product, ldebit, credit, lbal,
						ledgerid, lcommencing, "" + invoiceid + "", 0,"0","0","0","0","0",0,0,"0");
				
				//second effect
				 lbal = 0;
				 credit = "0";
				 ldebit = "" + discdebit + "";
				 product = "xxxxx";
				 partyid = notAvailableSlot.getClientId();
				 lcommencing = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
				 saveledger = chargesAccountProcessingDAO.saveLedger(partyid, product, ldebit, credit, lbal,
						ledgerid, lcommencing, "" + invoiceid + "", 0,"0","0","0","0","0",0,0,"0");

			}

			// save log
			int invoiceid1 = accountLogDAO.saveChargesInvoice(notAvailableSlot.getPayBy(),
					DateTimeUtils.getCommencingDate1(commencing), Integer.parseInt(notAvailableSlot.getClientId()),
					debit, discount, "", thirdPartyID, location, invoiceid,
					DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));

			String chargeInvoiceid = accountsDAO.getAppointmentInvoiceid(Integer.toString(appointmentid));
			int save = accountsDAO.saveChargesAssesment(Integer.parseInt(chargeInvoiceid), invoiceid);
			int update = accountsDAO.updateChargeType(chargeInvoiceid, "Submit");

			String chequeno = request.getParameter("chequeno");
			String bankname = request.getParameter("bankname");

			//update charge invoiced
			ArrayList<Master>chargeidList = accountsDAO.getInvoicedChargeidList(invoiceid);
			for(Master m : chargeidList){
				int upc = accountsDAO.updateChargeInvoideid(m.getId(),invoiceid);
			}
			
			// save payment
			if(!payAmount.equals("0")){
			int re = accountsDAO.saveChargesPayment(notAvailableSlot.getClientId(), invoiceid, payAmount, howpaid,
					thirdPartyID, "", DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()), 0,
					loginInfo.getUserId(), chequeno, bankname);
			
			int upstatus = accountsDAO.updateOpdPaymentStatus(appointmentid,invoiceid);
			
			//update invoice type payment autono
			int maxno = accountsDAO.getMaxInvoiceTypePaymentNo(re,invcetype);
			int u = accountsDAO.updateInvoicetypePaymentNo(re,maxno,invcetype);
			// Sms to Patient
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			Clinic clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
			boolean isPaymentSms = clinicDAO.isPaymentSMSActive(loginInfo.getClinicid());
			if (loginInfo.isOpd_payamnt_sms()) {
				String msg = "Thanks " + clientname + " for payment of Rupees " + payAmount + " from- "
						+ clinic.getClinicName() + "";
				SmsService service = new SmsService();
				service.sendSms(msg, client.getMobNo(), loginInfo, new EmailLetterLog());

			}

			int result1 = accountLogDAO.saveChargesPayment(notAvailableSlot.getClientId(), invoiceid, payAmount,
					howpaid, thirdPartyID, "", DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()), "");

			// ledger services
			ArrayList<Master> ledgerservicesList = accountsDAO.getLedgerServicesList(invoiceid);

			int l = 1;
			// for(Master master : ledgerservicesList){

			String itype = accountsDAO.getInvoiceTypeId(invoiceid1);
			// String serviceid =
			// chargesAccountProcessingDAO.getLedgrServiceIds(itype);
			String serviceid = itype;
			String ledgerid = chargesAccountProcessingDAO.getledgerID(serviceid, howpaid,
					notAvailableSlot.getBnkname());

			double lbal = chargesAccountProcessingDAO.getLedgerBalance(ledgerid);
			lbal = lbal + Double.parseDouble(payAmount);
			String credit = "0";
			String ldebit = payAmount;
			String product = "xxxxx";
			String partyid = notAvailableSlot.getClientId();
			String lcommencing = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
			int saveledger = chargesAccountProcessingDAO.saveLedger(partyid, product, ldebit, credit, lbal, ledgerid,
					lcommencing, "" + invoiceid + "", re,"0","0","0","0","0",0,0,"0");
			
			//second effect
			 lbal = 0;
			 credit = payAmount;
			 ldebit = "0";
			 product = "xxxxx";
			 partyid = notAvailableSlot.getClientId();
			 lcommencing = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
			 saveledger = chargesAccountProcessingDAO.saveLedger(partyid, product, ldebit, credit, lbal,
					ledgerid, lcommencing, "" + invoiceid + "", 0,"0","0","0","0","0",0,0,"0");
			 }else{
				 //if payamount not zero
				 int upstatus = accountsDAO.updateOpdPaymentStatus(appointmentid,invoiceid);
			 }

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void autosmsCheckSms(String appointmentid, String status, String starttime, String apmtdate, int seqNO)
			throws Exception {

		// String appointmentid = request.getParameter("appointmentid");

		Connection connection = null;

		try {
			TestingLog.debug("autosmsCheckSms()  1944");
			connection = Connection_provider.getconnection();
			// send sms
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			NotAvailableSlot notAvailableSlot = notAvailableSlotDAO
					.getApmtDetailsForLog(Integer.parseInt(appointmentid));
			UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			boolean isSMSActive = clinicDAO.isSmsActive(loginInfo.getId());
			Client client = clientDAO.getClientDetails(notAvailableSlot.getClientId());
			UserProfile userProfile = userProfileDAO.getUserprofileDetails(notAvailableSlot.getDiaryUserId());

			String message = AllTemplateAction.getAppointmentSMSTextNew(notAvailableSlot.getClientId(),
					Integer.parseInt(appointmentid), connection, loginInfo, seqNO);

			String messageapmuser = AllTemplateAction.getAppointmentSMSTextToapmUser(notAvailableSlot.getClientId(),
					Integer.parseInt(appointmentid), connection, loginInfo, seqNO);

			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			Clinic clinic = emrDAO.getsmsCheckConditionList(loginInfo.getClinicid());
			TestingLog.debug("autosmsCheckSms()  1966");
			if (status.equals("Modified")) {
				boolean rsult = false;
				if (!starttime.equals(notAvailableSlot.getSTime())) {
					rsult = true;
				}

				if (!apmtdate.equals(notAvailableSlot.getCommencing())) {
					rsult = true;
				}

				if (rsult) {
					message = AllTemplateAction.getAppointmentSMSTextNew(notAvailableSlot.getClientId(),
							Integer.parseInt(appointmentid), connection, loginInfo, seqNO);
					if (loginInfo.getCountry().equals("India")) {
						SmsService s = new SmsService();
						if (clinic.isSmscheck()) {
							s.sendSms(message, client.getMobNo(), loginInfo, new EmailLetterLog());
						}

						/*if (isSMSActive) {*/
							if (clinic.isSmscheckdoctor()) {
								s.sendSms(messageapmuser, userProfile.getMobile(), loginInfo, new EmailLetterLog());
							}

						/*}*/
					}
				}

			} else {
				if (loginInfo.getCountry().equals("India")) {
					SmsService s = new SmsService();
					if (clinic.isSmscheck()) {
						s.sendSms(message, client.getMobNo(), loginInfo, new EmailLetterLog());
						// s.sendSms(message, client.getMobNo(), loginInfo, new
						// EmailLetterLog());
					}
					/*if (isSMSActive) {*/
						if (clinic.isSmscheckdoctor()) {
							s.sendSms(messageapmuser, userProfile.getMobile(), loginInfo, new EmailLetterLog());
						}
					/*}*/
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			TestingLog.debug("autosmschksms()  "+e.getMessage()+"");
		} finally {

			connection.close();
		}

	}

	public String getConditionPatient() throws Exception {
		if (!verifyLogin(request)) {
			return "login";
		}
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);

			String clientId = request.getParameter("clientId");
			String id = notAvailableSlotDAO.getConditionPatient(clientId);

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("" + id + "");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			connection.close();
		}

		return null;
	}

	public String duration() throws Exception {
		if (!verifyLogin(request)) {
			return "login";
		}
		String selectedid = request.getParameter("selectedid");
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);

			String durationStr = notAvailableSlotDAO.getDuration(selectedid);

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("" + durationStr + "");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			connection.close();
		}

		return null;
	}

	public String diaryduration() throws SQLException {

		String slotid = request.getParameter("diaryslotid");
		System.out.println(slotid);

		if (!verifyLogin(request)) {
			return "login";
		}
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);

			String duration = notAvailableSlotDAO.getDiaryDuration(slotid);

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("" + duration + "");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}

		return null;
	}

	public String eventExist() throws Exception {
		if (!verifyLogin(request)) {
			return "login";
		}
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);

			String commencing = request.getParameter("commencing");
			String location = request.getParameter("location");
			String diaryuserId = request.getParameter("diaryuserId");
			String starttime = request.getParameter("starttime");
			String endtime = request.getParameter("endtime");
			int editAppointId = Integer.parseInt(request.getParameter("editAppointId"));

			boolean checkEventExist = notAvailableSlotDAO.checkEventAllreadyExist(commencing, location, diaryuserId,
					starttime, endtime);
			if (editAppointId != 0) {
				NotAvailableSlot notAvailableSlot = notAvailableSlotDAO.getAvailableSlotdata(editAppointId);

				if (starttime.equals(notAvailableSlot.getsTime()) && endtime.equals(notAvailableSlot.getEndTime())) {
					checkEventExist = false;
				} else {

					checkEventExist = notAvailableSlotDAO.checkEventAllreadyExist(commencing, location, diaryuserId,
							starttime, endtime, editAppointId);
				}
			}

			if (checkEventExist) {
				int coutnEsistingSlot = notAvailableSlotDAO.coutnEsistingSlot(commencing, location, diaryuserId,
						starttime, endtime, editAppointId);

				if (coutnEsistingSlot == 1) {
					String existStartTime = notAvailableSlotDAO.getExistStartTime(commencing, location, diaryuserId,
							starttime, endtime, editAppointId);

					String duration = DateTimeUtils.getDuration(starttime, endtime);

					System.out.println(duration);

					String sumoftime = DateTimeUtils.getSumofTime(starttime, duration);

					if (sumoftime.equals(existStartTime)) {
						checkEventExist = false;
					}

					// boolean checkDurationExist =
					// notAvailableSlotDAO.checkDurationExist(duration);

					/*
					 * if(checkDurationExist){ checkEventExist = false; }
					 */
				}
				if (editAppointId != 0) {
					if (coutnEsistingSlot == 1 || coutnEsistingSlot == 2) {
						String duration = DateTimeUtils.getDuration(starttime, endtime);

						String existStartTime = notAvailableSlotDAO.getEditExistStartTime(commencing, location,
								diaryuserId, starttime, endtime, editAppointId);

						System.out.println(duration);

						String sumoftime = DateTimeUtils.getSumofTime(starttime, duration);

						if (sumoftime.equals(existStartTime)) {
							checkEventExist = false;
						}

					}
				}

			}

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("" + checkEventExist + "");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			connection.close();
		}

		return null;
	}

	public String setPendingAmountOfClient() throws Exception {
		if (!verifyLogin(request)) {
			return "login";
		}
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			DiaryManagementDAO diaryManagementDAO = new JDBCDiaryManagentDAO(connection);

			String clientId = request.getParameter("clientId");

			/*
			 * double balanceTotal =
			 * notAvailableSlotDAO.getPendingBalanceTotal(clientId); String
			 * btotal = DateTimeUtils.changeFormat(balanceTotal);
			 */

			double debit = diaryManagementDAO.getClientDebitTotal(clientId);
			double payment = diaryManagementDAO.getClientPayment(clientId);

			double balance = debit - payment;

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("" + DateTimeUtils.changeFormat(balance) + "");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			connection.close();
		}

		return null;
	}

	public String loggedinuser() throws Exception {

		if (!verifyLogin(request)) {
			return "login";
		}

		Connection connection = null;
		try {
			connection = DriverManager.getConnection(""+Constants.DB_HOST+":3306/admin", "pranams", "6qxi5x&)~XBZ");
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			String apmuserlist = notAvailableSlotDAO.getApmLoggedUserList(loginInfo.getDbName());
			session.setAttribute("apmuserlist", apmuserlist);

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(apmuserlist);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			connection.close();
		}

		return null;
	}

	public String multiloc() throws SQLException {

		if (!verifyLogin(request)) {
			return "login";
		}

		// var url =
		// "multilocNotAvailableSlot?title="+title+"&endtime="+endtime+"&userid="+userid+"&commencing="+commencing+"
		// ";
		String title = request.getParameter("title");
		String temp[] = title.split(" ");
		String selectedStarttime = temp[0];

		String diaryuserid = request.getParameter("userid");
		String commencing = request.getParameter("commencing");
		String prevEndTime = request.getParameter("endtime");

		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);

			NotAvailableSlot notAvailableSlot = notAvailableSlotDAO.getInitilizedElementData(diaryuserid, commencing,
					selectedStarttime, prevEndTime);

			String data = notAvailableSlot.getApmtSlotId() + "/" + notAvailableSlot.getSTime() + "/"
					+ notAvailableSlot.getEndTime() + "/" + notAvailableSlot.getLocation() + "/"
					+ notAvailableSlot.getDisciplineid();

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("" + data + "");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}

		return null;
	}

	public String rewraped() throws Exception {

		if (!verifyLogin(request)) {
			return "login";
		}
		Connection connection = null;
		try {

			String eventid = request.getParameter("id");

			connection = Connection_provider.getconnection();
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			DiaryManagementDAO diaryManagementDAO = new JDBCDiaryManagentDAO(connection);
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			String opendb = (String) session.getAttribute("openedb");
			ArrayList<NotAvailableSlot> wrapedLiest = diaryManagementDAO.getWrapedEventData(eventid, opendb);

			String str = "";

			for (NotAvailableSlot notAvailableSlot : wrapedLiest) {

				if (notAvailableSlot.getImagename() == null) {
					notAvailableSlot.setImagename("");
				}

				Client client = clientDAO.getPatientBMIData(notAvailableSlot.getClientId(),Integer.parseInt(eventid));

				str = notAvailableSlot.getId() + "#" + notAvailableSlot.getClientName() + "#"
						+ notAvailableSlot.getApmtType() + "#" + notAvailableSlot.getSTime() + "#"
						+ notAvailableSlot.getEndTime() + "#" + notAvailableSlot.getDuration() + "#"
						+ notAvailableSlot.getStatus() + "#" + notAvailableSlot.getNotes() + "#"
						+ notAvailableSlot.getArrivedStatus() + "#" + notAvailableSlot.isDna() + "#"
						+ notAvailableSlot.getClientId() + "#" + notAvailableSlot.getCommencing() + "#"
						+ notAvailableSlot.getDiaryUserId() + "#" + notAvailableSlot.getCharge() + "#"
						+ notAvailableSlot.getReasonforblock() + "#" + notAvailableSlot.getTreatmentEpisodeId() + "#"
						+ notAvailableSlot.getApmttypetext() + "#" + notAvailableSlot.getClientName() + "#"
						+ notAvailableSlot.getUsedsession() + "#" + notAvailableSlot.getTptypeid() + "#"
						+ notAvailableSlot.getTpnameid() + "#" + notAvailableSlot.getPractitionerEmail() + "#"
						+ notAvailableSlot.getClientEmail() + "#" + notAvailableSlot.getLocid() + "#"
						+ notAvailableSlot.getLocation() + "#" + notAvailableSlot.getCondition() + "#"
						+ notAvailableSlot.getWhopay() + "#" + notAvailableSlot.isAppointmentCompleted() + "#"
						+ notAvailableSlot.isChargeCompleted() + "#" + notAvailableSlot.getDiaryUser() + "#"
						+ notAvailableSlot.getApmtSlotId() + "#" + notAvailableSlot.getTpName() + "#"
						+ loginInfo.getEmail() + "#" + notAvailableSlot.getOtid() + "#"
						+ notAvailableSlot.getImagename() + "#" + client.getHeight() + "#" + client.getWeight() + "#"
						+ client.getBmi() + "#" + client.getPulse() + "#" + client.getSysbp() + "#" + client.getDiabp()
						+ "#" + notAvailableSlot.getOtplan() + "#" + notAvailableSlot.getProcedure() + "#"
						+ notAvailableSlot.getSurgeon() + "#" + notAvailableSlot.getAnesthesia() + "#"
						+ notAvailableSlot.getIpdno() + "#" + notAvailableSlot.getWardid() + "#"
						+ notAvailableSlot.getAsistantdoclist() + "#" + notAvailableSlot.getToken() + "#"
						+ client.getSugarfasting() + "#" + client.getPostmeal() + "#" + loginInfo.getUhid() + "#"
						+ notAvailableSlot.getUhid()+ "#" + client.getTemprature()+ "#" + client.getSpo()+ "#" + client.getHead_cir();

				response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write(str);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			connection.close();
		}

		return null;
	}

	// IPD

	public String getbellcolor() throws Exception {

		if (!verifyLogin(request)) {
			return "login";
		}

		Connection connection = null;
		try {

			StringBuffer buffer = new StringBuffer();
			connection = Connection_provider.getconnection();
			IpdDAO ipdDAO = new JDBCIpdDAO(connection);

			String date = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
			date = date.substring(0, 10);
			String nextday = date + " 23:59:59";
			ArrayList<Bed> doseremainders = ipdDAO.getAllDosageRemainders(date, nextday, loginInfo);
			// ipdForm.setDoseremainders(doseremainders);

			HashSet<String> colorcount = new HashSet<String>();
			for (Bed bed : doseremainders) {

				String rowid = "d" + "" + bed.getConditionname() + "" + bed.getId();
				buffer.append(bed.getId() + "-" + bed.getColor() + "-" + rowid + "/");
				if (bed.getColor().equals("Red")) {

					String data = bed.getId() + "/" + bed.getColor();
					colorcount.add(data);
				}

			}

			buffer.append(colorcount.size());

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(buffer.toString());

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			connection.close();
		}

		return null;
	}

	// Akash 16 April 2018 diagnosis
	public String getdiagnosis() throws Exception {

		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			IpdDAO ipdDAO = new JDBCIpdDAO(connection);
			String search = request.getParameter("search");
			String selected = request.getParameter("selected");
			ArrayList<Client> ipdCondtitionList = ipdDAO.getAllDiagnosis(search);
			StringBuilder builder = new StringBuilder();
			for (Client client : ipdCondtitionList) {

				boolean flag = false;

				for (String s : selected.split(",")) {

					if (s == null) {
						continue;
					}

					int d = Integer.parseInt(s);
					if (d == 0) {
						continue;
					}
					if (d == client.getId()) {
						flag = true;
						break;
					}
				}

				if (flag) {

					builder.append("<tr>");
					builder.append("<td><input type='checkbox' checked='checked' onclick='setCheckedData(this)' value='"
							+ client.getId() + "'  /></td>");
					builder.append("<td>" + client.getTreatmentType() + "</td>");
					builder.append("</tr>");

				} else {

					builder.append("<tr>");
					builder.append("<td><input type='checkbox' onclick='setCheckedData(this)' value='" + client.getId()
							+ "'  /></td>");
					builder.append("<td>" + client.getTreatmentType() + "</td>");
					builder.append("</tr>");

				}

			}
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(builder.toString());
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			connection.close();
		}
		return null;
	}

	
	public String prodstock() throws Exception {
		String masterchargetype = request.getParameter("masterchargetype");
		String prodid = request.getParameter("prodid");

		Connection connection = null;

		try {

			connection = Connection_provider.getconnection();
			IpdDAO ipdDAO = new JDBCIpdDAO(connection);
			CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(connection);

			boolean checkInventoryProduct = ipdDAO.checkInventoryChargeType(masterchargetype);
			Product product = completeAptmDAO.getInventoryProductDetails(prodid);

			String str = "";
			if (!checkInventoryProduct) {
				int sumqty = ipdDAO.getSumOfProdQty(prodid);
				str = "1/" + product.getStock() + "/" + sumqty;
			} else {
				str = "0/0/0";
			}

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("" + str + "");

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			connection.close();
		}

		return null;
	}

	
	public String savecharge() throws Exception {

		Double total = 0.00;
		String totalx = "";

		String user = request.getParameter("ipdclientname");
		String apmtType = request.getParameter("chargetype");
		String quantity = request.getParameter("quantity");

		String charge = request.getParameter("charge");

		String practitionerId = request.getParameter("ipdpractitionerid");
		String clientId = request.getParameter("ipdclientid");
		String practitionerName = request.getParameter("ipdpractitionername");
		String date = request.getParameter("date");

		String payBuy = request.getParameter("payby");
		String markAppointment = request.getParameter("markappointment");
		String apppointmentid = "0";

		String masterchargetype = request.getParameter("masterchargetype");
		String mannualcharge = request.getParameter("mannualcharge");
		String packageid = request.getParameter("packageid");

		String visitingconsulatntdr = request.getParameter("visitingconsulatntdr");
		String isindisharecharge = request.getParameter("isindisharecharge");
		
		String taxtypids=request.getParameter("taxtypes");
		String chargedescription=request.getParameter("chargedescriptnew");
		
		Connection connection = null;
		try {
			if (date == null) {
				date = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
			} else {
				date = DateTimeUtils.getCommencingDate1(date);
			}
			connection = Connection_provider.getconnection();
			UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
			ChargesAccountProcessingDAO chargesAccountProcessingDAO=new  JDBCChargeAccountProcesDAO(connection);
			int compulsary_consultant=chargesAccountProcessingDAO.isCompulasryConsultant(masterchargetype);
			if (masterchargetype != null) {
				if (masterchargetype.equals("IPD Visiting Charge") || masterchargetype.equals("Consultation Charge")||compulsary_consultant==1) {
					practitionerId = visitingconsulatntdr;
					//practitionerName = userProfileDAO.getReferalDrName(visitingconsulatntdr);
					//Akash 16 May 2018
					practitionerName = userProfileDAO.getFullName(practitionerId);
				}
			}
			
			
			

			CompleteAppointment completeAppointment = new CompleteAppointment();

			//lokesh
			if(taxtypids!=null){
				for(String id:taxtypids.split(",")){
					if(id.equals("0")){
						continue;
					}
					String per[]= id.split("~");
					if(per[0].equals("1")){
						completeAppointment.setTax1(per[1]);
					}else if(per[0].equals("2")){
						completeAppointment.setTax2(per[1]);
					}else if(per[0].equals("3")){
						completeAppointment.setTax3(per[1]);
					}
				}
			}
			completeAppointment.setChargedescription(chargedescription);
			
			PackageMasterDAO packageMasterDAO = new JDBCPackageMasterDAO(connection);

			CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(connection);
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);

			completeAppointment.setUser(user);
			completeAppointment.setIsindisharecharge(isindisharecharge);
			completeAppointment.setVisitingconsulatntdrid(visitingconsulatntdr);
			
			String appointmentTypeName = completeAptmDAO.getAppointmentTypeName(apmtType);
			completeAppointment.setApmtType(apmtType);
			completeAppointment.setCharges(charge);
			//
			completeAppointment.setStartTime("");
			completeAppointment.setDuration("");
			completeAppointment.setPractitionerId(practitionerId);
			completeAppointment.setPractitionerName(practitionerName);
			completeAppointment.setClientId(clientId);
			completeAppointment.setCommencing(date);
			completeAppointment.setPayBuy(payBuy);
			completeAppointment.setMarkAppointment(markAppointment);
			completeAppointment.setAppointmentid(apppointmentid);
			completeAppointment.setQuantity(Integer.parseInt(quantity));
			completeAppointment.setMasterchargetype(masterchargetype);
			completeAppointment.setManualcharge(mannualcharge);
			//
			completeAppointment.setAdditionalcharge_id(apmtType);
			completeAppointment.setManuallyadded("1");
			int ipdid=0;
			String appointmentTYpeText = notAvailableSlotDAO.getAppointmentTypeText(apmtType);
			if(session.getAttribute("sessionadmissionid")!=null){
			 ipdid = (Integer) session.getAttribute("sessionadmissionid");
			}
			if (ipdid != 0) {
				completeAppointment.setIpdid(ipdid);
			}
			if (packageid == null) {
				int result = completeAptmDAO.saveCharge(completeAppointment, apppointmentid, loginInfo.getId());
			} else if (packageid.equals("")) {
				int result = completeAptmDAO.saveCharge(completeAppointment, apppointmentid, loginInfo.getId());
			} else if (packageid.equals("0")) {
				int result = completeAptmDAO.saveCharge(completeAppointment, apppointmentid, loginInfo.getId());
			} else {
				ArrayList<PackageMaster> arrayList = packageMasterDAO.getPackageFromChild(Integer.parseInt(packageid));
				for (PackageMaster packageMaster : arrayList) {
					completeAppointment.setManualcharge(packageMaster.getChargename());
					if(completeAppointment.getCharges().equals(packageMaster.getCal_amount())){
						completeAppointment.setCharges(packageMaster.getCal_amount());
					}
					//completeAppointment.setCharges(packageMaster.getCal_amount());
					completeAppointment.setMasterchargetype(mannualcharge);
					completeAppointment.setTpkg(packageMaster.getId());
					int result = completeAptmDAO.saveCharge(completeAppointment, apppointmentid, loginInfo.getId());
				}
			}

			ArrayList<CompleteAppointment> clientChargeListDetail = new ArrayList<CompleteAppointment>();

			clientChargeListDetail = completeAptmDAO.getPatientChrageDetails(clientId, date, apppointmentid,
					loginInfo.getId());

			for (CompleteAppointment completeAppointment2 : clientChargeListDetail) {
				total = completeAppointment2.getChargeTotal();
				totalx = completeAppointment2.getChargeTotalx();
			}
			// completeAppointmentForm.setChargeTotal(total);

			String textAjax = new String();

			textAjax = ("<input class = 'form-control' type = 'text' id = 'chargeTotal' name = 'chargeTotal' disabled = 'disabled' value = '"
					+ Constants.getCurrency(loginInfo) + totalx + " '>");

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("" + textAjax.toString() + "");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			connection.close();
		}

		return null;
	}

	public String cashDesk() throws SQLException {
		if (!verifyLogin(request)) {
			return "login";
		}
		String id = request.getParameter("selectedUser");
		String date = request.getParameter("date");
		if (date == null) {
			date = "";
		}
		if (date.equals("")) {
			date = DateTimeUtils.getDateinSimpleFormate(new Date());
			String stemp[] = date.split(" ");

			String temp[] = stemp[0].split("-");
			date = temp[2] + "-" + temp[1] + "-" + temp[0];
		}

		String apmtSlotId = request.getParameter("apmtSlotId");
		System.out.println(id);
		double total = 0;
		String totalx = "";
		Connection connection = null;

		try {
			CompleteAppointment completeAppointment = new CompleteAppointment();
			ArrayList<CompleteAppointment> clientChargeListDetail = new ArrayList<CompleteAppointment>();
			connection = Connection_provider.getconnection();
			CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(connection);
			clientChargeListDetail = completeAptmDAO.getPatientChrageDetails(id, date, apmtSlotId, loginInfo.getId());
			// completeAppointmentForm.setClientChargeListDetail(clientChargeListDetail);

			// total = completeAppointment2.getChargeTotal();

			StringBuffer str = new StringBuffer();

			// Practitioner Mail
			/*
			 * str.
			 * append("<table  id = 'cashDesk' cellpadding='0' cellspacing='0' class='my-table' > "
			 * );
			 * 
			 * 
			 * 
			 * str.append("<tr>"); str.append("<th>Id</th> ");
			 * str.append("<th>PayBy</th> ");
			 * str.append("<th>Appointment Type</th> ");
			 * str.append("<th>Quantity</th> "); str.append("<th>Charge</th> ");
			 * str.append("<th>Delete</th> "); str.append("</tr>");
			 */
			int count = 0;
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			Client client = clientDAO.getClientDetails(id);
			AppointmentDAO appointmentDAO=new JDBCAppointmentDAO(connection);

			for (CompleteAppointment completeAppointment1 : clientChargeListDetail) {

				str.append("<tr>");
				str.append("<td>"+DateTimeUtils.getCommencingDate1(completeAppointment1.getCommencing())+"</td>");
				// str.append("<td>"+completeAppointment1.getId()+"</td>");
				if (client.getWhopay().equals(Constants.PAY_BY_CLIENT)) {
					str.append("<td class='hidden'>Self</td>");
				} else {
					str.append("<td class='hidden'>Third party</td>");
				}
				str.append("<td>" + completeAppointment1.getMasterchargetype() + "</td>");
				str.append("<td id='invchargenameid" + count + "'>" + completeAppointment1.getApmtType() + "</td>");
				str.append("<td style='text-align:center'>" + completeAppointment1.getQuantity() + "</td>");

				double taxcharge=Math.round((Double.parseDouble(completeAppointment1.getCharges())*Double.parseDouble(completeAppointment1.getTotaltax()))/100.0);
				double chargeamt=Double.parseDouble(completeAppointment1.getCharges())+taxcharge;
				if (completeAppointment1.getMasterchargetype().equals("INVESTIGATION")) {
					if (loginInfo.isEdit_invst_charge()) {
						str.append(
								"<td  style='text-align:right'><input  class='invunitchargecase' type='number' onchange='updatetptypetempcharge("
										+ completeAppointment1.getId() + ",this.id,this.value)' id='invchargeid" + count
										+ "' name='invchargeid" + count + "' value='" + /*completeAppointment1.getCharges()*/chargeamt
										+ "' /></td>");
					} else {
						int noneditamt = appointmentDAO.getNonEditAmtSts(completeAppointment1.getAdditionalcharge_id());
						if(noneditamt==1){
							str.append(
									"<td  style='text-align:right'><input  class='invunitchargecase' type='number' readonly='readonly' onchange='updatetptypetempcharge("
											+ completeAppointment1.getId() + ",this.id,this.value)' id='invchargeid" + count
											+ "' name='invchargeid" + count + "' value='" + /*completeAppointment1.getCharges()*/chargeamt
											+ "' /></td>");
						}else{
						str.append(
								"<td  style='text-align:right'><input  class='invunitchargecase' type='number' onchange='updatetptypetempcharge("
										+ completeAppointment1.getId() + ",this.id,this.value)' id='invchargeid" + count
										+ "' name='invchargeid" + count + "' value='" + /*completeAppointment1.getCharges()*/chargeamt
										+ "' /></td>");
						}
						
					}

				} else {
					if (client.getWhopay().equals(Constants.PAY_BY_THIRD_PARTY)) {
						int noneditamt = appointmentDAO.getNonEditAmtSts(completeAppointment1.getAdditionalcharge_id());
						if(noneditamt==1){
						str.append(
								"<td  style='text-align:right'><input style='width:112%' class='invunitchargecase' type='number' readonly='readonly' onchange='updatetptypetempcharge("
										+ completeAppointment1.getId() + ",this.id,this.value)' id='invchargeid" + count
										+ "' name='invchargeid" + count + "' value='" + /*completeAppointment1.getCharges()*/chargeamt
										+ "' /></td>");
					}else{
						str.append(
								"<td  style='text-align:right'><input style='width:112%' class='invunitchargecase' type='number' min='0' onchange='updatetptypetempcharge("
										+ completeAppointment1.getId() + ",this.id,this.value)' id='invchargeid" + count
										+ "' name='invchargeid" + count + "' value='" + /*completeAppointment1.getCharges()*/chargeamt
										+ "' /></td>");
					}
						
					}	else{
						int noneditamt = appointmentDAO.getNonEditAmtSts(completeAppointment1.getAdditionalcharge_id());
						if(noneditamt==1){
							str.append(
									"<td  style='text-align:right'><input style='width:112%' class='invunitchargecase' type='number' readonly='readonly' onchange='updatetptypetempcharge("
											+ completeAppointment1.getId() + ",this.id,this.value)' id='invchargeid" + count
											+ "' name='invchargeid" + count + "' value='" + /*completeAppointment1.getCharges()*/chargeamt
											+ "' /></td>");
					}else{
						str.append(
								"<td  style='text-align:right'><input style='width:112%' class='invunitchargecase' type='number' min='0' onchange='updatetptypetempcharge("
										+ completeAppointment1.getId() + ",this.id,this.value)' id='invchargeid" + count
										+ "' name='invchargeid" + count + "' value='" + /*completeAppointment1.getCharges()*/chargeamt
										+ "' /></td>");
					}
				}

				double charge = Double.parseDouble(completeAppointment1.getCharges())
						* completeAppointment1.getQuantity();
				
				str.append("<td style='text-align:right'>" + Constants.getCurrency(loginInfo)
						+ "<span id='invamtchargeid'>" + DateTimeUtils.changeFormat(charge) + "</span></td>");
				/*if (count == 0) {
					str.append("<td><img src='common/images/delete.gif'></img></td>");

				} else {*/
					str.append("<td onclick = 'confirmedDelete1(" + completeAppointment1.getId()
							+ ")'><img src='common/images/delete.gif'></img></td>");

				/*}*/

				str.append("</tr>");
				count = count + 1;
			}
			}
			// str.append("</table>");
			for (CompleteAppointment completeAppointment2 : clientChargeListDetail) {
				total = completeAppointment2.getChargeTotal();
				totalx = ""+(Double.parseDouble(completeAppointment2.getChargeTotalx())+completeAppointment2.getTotaltaxamt());
				
			}

			str.append("<tr style='background-color: #efefef;'>");
			str.append("<th colspan='5' style='font-size: 13px;font-weight: bold;'>Total</th> ");

			str.append("<th style='font-size: 13px;font-weight: bold;' colspan='5'>" + Constants.getCurrency(loginInfo)
					+ totalx + "</th> ");
			str.append("</tr>");

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("" + str.toString() + "");

			String textAjax = new String();

			textAjax = ("<input class='form-control' type = 'hidden' id = 'hiddenTotal' name = 'hiddenTotal' value = '"
					+ Constants.getCurrency(loginInfo) + totalx + " '>");

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");

			response.getWriter().write("" + textAjax.toString() + "");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return null;
	}

	// This method now called from IpdAction 17 April 2018 Akash
	public String bed() throws Exception {
		if (!verifyLogin(request)) {

			return "login";
		}

		String wardid = request.getParameter("wid");
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			IpdDAO ipdDAO = new JDBCIpdDAO(connection);
			ArrayList<Bed> bedList = ipdDAO.getWardWiseBedList(wardid);

			StringBuffer str = new StringBuffer();
			str.append("<select id='bedid' name='bedid' class='form-control'>");
			str.append("<option value='0'>Select Bed</option>");

			for (Bed bed : bedList) {
				str.append("<option value='" + bed.getId() + "'>" + bed.getBedname() + "</option>");
			}

			str.append("</select>");

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("" + str.toString() + "");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return null;
	}

	public String setalldiagnosis() throws Exception {

		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			DiagnosisDAO diagnosisDAO = new JDBCDiagnosisDAO(connection);
			StringBuffer buffer = new StringBuffer();
			String selected = request.getParameter("selected");

			int i = 0;
			for (String s : selected.split(",")) {

				int d = Integer.parseInt(s);
				if (d == 0) {
					continue;
				} else {

					Diagnosis diagnosis = diagnosisDAO.getDiagnosisName(s);
					buffer.append("<tr>");
					buffer.append("<td><input type='checkbox' checked='checked' onclick=reoveThisRow(this,'"
							+ diagnosis.getId() + "') value='" + diagnosis.getId()
							+ "' class='classD' /> <input type='hidden' value=" + diagnosis.getId()
							+ " name='conditions[" + i + "].conditionid'  /> </td>");
					buffer.append("<td>" + diagnosis.getName() + "</td>");
					buffer.append("<td class='hidden'><a onclick=reoveThisRow('" + diagnosis.getId()
							+ "')><i class='fa fa-trash-o'></i></a></td>");
					buffer.append("</tr>");
					i++;
				}

			}

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(buffer.toString());
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			connection.close();
		}

		return null;
	}

	public String saveinvestoutsource() throws Exception {
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			InvestigationDAO investigationDAO = new JDBCInvestigationDAO(connection);
			StringBuffer buffer = new StringBuffer();
			String invid = request.getParameter("invid");
			String val = request.getParameter("val");
			String userid= loginInfo.getUserId();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			String date = dateFormat.format(cal.getTime());
			int res = investigationDAO.updateInvestigationOutsource(invid, val,userid,date);
			String invreq=investigationDAO.getInveReqId(Integer.parseInt(invid));
			Investigation investigation=investigationDAO.getInvestigationTyeID(invid);
			double charge=0;
			charge=investigationDAO.getoutsourceAmount(invid,val,investigation.getInvsttypeid());
			if(charge>0){
			int chargeup=investigationDAO.updateOutsourceChargeAmount(invreq,charge);
			}
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(buffer.toString());
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			connection.close();
		}

		return null;
	}

	public String savechildgrowthdata() throws Exception {
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			String heightdata = request.getParameter("heightdata");
			String heightmonth = request.getParameter("heightmonth");
			String weightdata = request.getParameter("weightdata");
			String weightmonth = request.getParameter("weightmonth");
			String bmidata = request.getParameter("bmidata");
			String bmimonth = request.getParameter("bmimonth");
			String headcircumferncedata = request.getParameter("headcircumferncedata");
			String headcircumferncemonth = request.getParameter("headcircumferncemonth");
			String clientId = request.getParameter("clientId");
			String val = request.getParameter("val");
			String month = request.getParameter("month");
			String lengthdata = ""; //request.getParameter("lengthdata");
			String lengthmonth = ""; //request.getParameter("lengthmonth");
			Client client = new Client();
			client.setHeightdata(heightdata);
			client.setWeightdata(weightdata);
			client.setBmidata(bmidata);
			client.setHeadcircumferncedata(headcircumferncedata);
			client.setLength(lengthdata);
			client.setClientid(clientId);
			client.setMonth(month);
			client.setUserid(loginInfo.getUserId());
			String datetime = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
			client.setDate(datetime);
			int id = clientDAO.check_child_growth_data(clientId, month);
			int res = 0;
			if (id > 0) {
				client.setId(id);
				// update
				res = clientDAO.updateChildGrowthData(client, val);
			} else {
				// insert
				res = clientDAO.saveChildGrowthData(client, val);
			}

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return null;
	}

	// from ipd action 21 april 2018
	public String otinfo() throws Exception {
		String clientid = request.getParameter("clientid");
		Connection connection = null;

		try {

			connection = Connection_provider.getconnection();
			IpdDAO ipdDAO = new JDBCIpdDAO(connection);

			int lastipdid = ipdDAO.getLastIpdId(clientid);

			BedDao bedDao = new JDBCBedDao(connection);

			Bed bed = bedDao.getEditIpdData(Integer.toString(lastipdid));
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			Client client = clientDAO.getClientDetails(clientid);

			String age = DateTimeUtils.getAge(client.getDob());
			String agegender = age + "Years" + " / " + client.getGender();

			String wardname = ipdDAO.getIpdWardName(bed.getWardid());
			String bedname = ipdDAO.getIpdBedName(bed.getBedid());

			String result = agegender + "~" + lastipdid + "~" + wardname + "~" + bedname;

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("" + result + "");

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			connection.close();
		}

		return null;
	}

	public String setappointmentprocedure() throws Exception {
		Connection connection = null;
		try {

			connection = Connection_provider.getconnection();
			IpdDAO ipdDAO = new JDBCIpdDAO(connection);
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			MasterDAO masterDAO = new JDBCMasterDAO(connection);
			String editAppointId = request.getParameter("editAppointId");
			NotAvailableSlot notAvailableSlot = notAvailableSlotDAO.getProcedureDepartment(editAppointId);
			ArrayList<Master> procedureList = notAvailableSlotDAO.getProcedureList(notAvailableSlot.getDept());

			StringBuffer str = new StringBuffer();

			str.append(
					"<select onchange='filterOtCharges(this.value)' name='otprocedureplaned' id='otprocedureplaned' class='form-control showToolTip chosen' >");
			str.append("<option value='0'>Select Procedure</option>");
			for (Master master : procedureList) {
				// Akash 30-March-2018
				if (notAvailableSlot.getId() == master.getId()) {
					str.append("<option selected value='" + master.getId() + "'>" + master.getName() + "</option>");
				} else {
					str.append("<option value='" + master.getId() + "'>" + master.getName() + "</option>");
				}
			}
			str.append("</select>");

			str.append("~");

			ArrayList<Master> list = ipdDAO.getOtFilteredChargeList(notAvailableSlot.getProcedure(),
					notAvailableSlot.getClientId());

			str.append(
					"<select onchange='setAppointmentTypeTimeAjax(this.value)' name='duration' id='apmtType' class='form-control showToolTip chosen' >");
			str.append("<option value='0'>Select Charge</option>");
			for (Master master : list) {
				/*
				 * str.append("<option value='"+master.getId()+"'>"+master.
				 * getName()+"</option>");
				 */
				if (notAvailableSlot.getAppointmentTypeid() == master.getId()) {
					str.append(
							"<option selected value='" + master.getId() + "'>" + master.getChargetype() + "</option>");
				} else {
					str.append("<option value='" + master.getId() + "'>" + master.getChargetype() + "</option>");
				}
			}
			str.append("</select>");

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("" + str.toString() + "");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public String chageotemplate() throws Exception {
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			JDBCMasterDAO masterDAO = new JDBCMasterDAO(connection);
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			String surgeonid = request.getParameter("srgeonid");
			String tempsrno = request.getParameter("tempsrno");
			StringBuffer stringBuffer = new StringBuffer();

			int specialityid = notAvailableSlotDAO.getSpeciSurgonFromRefernce(Integer.parseInt(surgeonid));
			Master master1 = masterDAO.getDisciplineData((String.valueOf(specialityid)));
			String surgical_template = masterDAO.getIpdTemplateId("OT Template");
			ArrayList<Master> otherTemplateList = masterDAO.getIpdTemplateBySpeciality(surgical_template,
					master1.getDiscipline());

			stringBuffer
					.append("<label for='exampleInputEmail1'>Operation Notes <a href='#' onclick='setOTemplateBySpeciality("
							+ tempsrno + "," + surgeonid + ")'><i class='fa fa-refresh'></i></a></label>");
			stringBuffer.append("<select name='template" + tempsrno + "' id='template" + tempsrno
					+ "' onchange='getNewOTtemplate(this.value," + tempsrno
					+ ")' class='form-control showToolTip chosen-select'>");
			stringBuffer.append("<option value='0'>Select Template</option>");
			for (Master master : otherTemplateList) {
				stringBuffer.append("<option value='" + master.getId() + "'>" + master.getName() + "</option>");
			}
			stringBuffer.append("</select>");
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(stringBuffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			connection.close();
		}

		return null;
	}

	// Akash 26 April 2018 called from addcharge its from ipddashobardaction
	public String getajaxnursingdata() throws Exception {
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal = Calendar.getInstance();
			// cal.add(Calendar.DATE, -7);
			String currDate = dateFormat.format(cal.getTime());

			connection = Connection_provider.getconnection();
			IpdDAO ipdDAO = new JDBCIpdDAO(connection);
			String ipdid = request.getParameter("ipdid");

			// for priscription

			ArrayList<Priscription> parentPriscList = ipdDAO.getParentPriscList(ipdid);

			for (Priscription priscription : parentPriscList) {
				String temp[] = priscription.getDate().split(" ");
				String mdicinestartdate = temp[0];

				long mdicinedays = DateTimeUtils.getDiffofTwoDates(mdicinestartdate, currDate);
				mdicinedays++;

				ArrayList<Priscription> clientPriscList = ipdDAO.getClientPriscList(priscription.getParentid());
				for (Priscription pr : clientPriscList) {
					boolean checkpriscexist = ipdDAO.checkPrescExist(mdicinedays, pr.getId());
					String dosecolumn = "";
					String doseqmark = "";

					String dosage = pr.getDosage();
					if (!checkpriscexist) {
						if (dosage != null) {
							String dosetemp[] = dosage.split("-");
							if (dosetemp.length < 3) {
								dosage = ipdDAO.getAlterNateDose(dosage);
								dosetemp = dosage.split("-");
							}
							int c = 0;
							for (int i = 1; i <= dosetemp.length; i++) {

								doseqmark = doseqmark + 0 + ",";
								dosecolumn = dosecolumn + "dos" + i + ",";
								c++;
							}
							dosecolumn = dosecolumn.substring(0, dosecolumn.length() - 1);
							doseqmark = doseqmark.substring(0, doseqmark.length() - 1);

							if (mdicinedays <= pr.getDays()) {
								int result = ipdDAO.savePrescReminder(dosecolumn, doseqmark, mdicinedays, pr.getId(),
										ipdid);
							}

						}

					}
				}
			}

			// for Nursing

			ArrayList<Master> nursingParentList = ipdDAO.getParentNursingList(ipdid);

			for (Master master : nursingParentList) {
				String temp[] = master.getDate().split(" ");
				String mdicinestartdate = temp[0];

				long mdicinedays = DateTimeUtils.getDiffofTwoDates(mdicinestartdate, currDate);
				mdicinedays++;

				ArrayList<Master> clientNursingList = ipdDAO.getClientNursingList(master.getParentid());
				for (Master pr : clientNursingList) {
					boolean checkpriscexist = ipdDAO.checkNursingExist(mdicinedays, pr.getId());
					String dosecolumn = "";
					String doseqmark = "";

					if (!checkpriscexist) {
						if (pr.getFrequency() != null) {
							String dosetemp[] = pr.getFrequency().split("-");
							int c = 0;
							for (int i = 1; i <= dosetemp.length; i++) {

								doseqmark = doseqmark + 0 + ",";
								dosecolumn = dosecolumn + "dos" + i + ",";
								c++;
							}
							dosecolumn = dosecolumn.substring(0, dosecolumn.length() - 1);
							doseqmark = doseqmark.substring(0, doseqmark.length() - 1);

							if (mdicinedays <= pr.getDays()) {
								int result = ipdDAO.saveNursingReminder(dosecolumn, doseqmark, mdicinedays, pr.getId(),
										ipdid);
							}

						}
					}
				}

			}

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			connection.close();
		}
		return null;
	}

	// Akash 26 April 2018 called from addcharge its from ipddashobardaction
	public String getallpriscajax() throws Exception {

		Connection connection = null;

		try {
			String ipdid = request.getParameter("ipdid");
			connection = Connection_provider.getconnection();
			IpdDAO ipdDAO = new JDBCIpdDAO(connection);
			ArrayList<Priscription> ipdPriscList = ipdDAO.getAJaxPriscription(ipdid);

			StringBuffer buffer = new StringBuffer();
			for (Priscription priscription : ipdPriscList) {

				if (priscription.getMdicinenametxt() != null) {

					buffer.append("<tr class='prstotip'>");
					buffer.append("<td>" + priscription.getMdicinenametxt() + "("+priscription.getCurrdays()+"/"+priscription.getTotaldays()+" days)</td>");
					buffer.append("<td>" + priscription.getDosage() + "</td>");
					buffer.append("<td>");

					if (priscription.getDosesize() == 3) {
						String priscdose = priscription.getDosage();
						String doselabletime = priscription.getDosegiventime();
						String[] dosetime= new String[4];
						if(doselabletime!=null){
							if(!doselabletime.equals("")){
								dosetime = doselabletime.split("-");
								if(dosetime[0]!=null){
									if(dosetime[0].equals("")){
										dosetime[0]="0";
									}
								}
								if(dosetime[1]!=null){
									if(dosetime[1].equals("")){
										dosetime[1]="0";
									}
								}
								
								if(dosetime[2]!=null){
									if(dosetime[2].equals("")){
										dosetime[2]="0";
									}
								}
							}else{
								dosetime[0]="0";
								dosetime[1]="0";
								dosetime[2]="0";
							}
						}else{
							dosetime[0]="0";
							dosetime[1]="0";
							dosetime[2]="0";
						}
						
						if (!priscription.getDosevalue1().equals("0")) {
							if (priscription.isDos1()) {
								buffer.append(
										"<input type='checkbox' name='dos1' id='dos1' checked='checked' onclick=toggleConfirmation("
												+ priscription.getId() + ",'dos1'," + priscription.isDos1() + ")>");
								if(doselabletime!=null){
									if(!doselabletime.equals("")){
										if(!dosetime[0].equals("0")){
											buffer.append(""+dosetime[0]+"("+priscription.getUserid()+")<br>");
										}else{
											buffer.append("("+priscription.getUserid()+")<br>");
										}
									}else{
										buffer.append("("+priscription.getUserid()+")<br>");
									}
								}else{
									buffer.append("("+priscription.getUserid()+")<br>");
								}
								
							} else {
								buffer.append("<input type='checkbox' name='dos1' id='dos1' onclick=toggleConfirmation("
										+ priscription.getId() + ",'dos1'," + priscription.isDos1() + ")>");
								if(doselabletime!=null){
									if(!doselabletime.equals("")){
										if(!dosetime[0].equals("0")){
											buffer.append(""+dosetime[0]+"<br>");
										}else{
											buffer.append("<br>");
										}
									}else{
										buffer.append("<br>");
									}
								}else{
									buffer.append("<br>");
								}
							}
						} else {
							buffer.append("<input type='checkbox' name='dos1' id='dos1' onclick=toggleConfirmation("
									+ priscription.getId() + ",'dos1'," + priscription.isDos1() + ") disabled='true'>");
							if(doselabletime!=null){
								if(!doselabletime.equals("")){
									if(!dosetime[0].equals("0")){
										buffer.append(""+dosetime[0]+"<br>");
									}else{
										buffer.append("<br>");
									}
								}else{
									buffer.append("<br>");
								}
							}else{
								buffer.append("<br>");
							}
						}
						if (!priscription.getDosevalue2().equals("0")) {
							if (priscription.isDos2()) {
								buffer.append(
										"<input type='checkbox' name='dos2' id='dos2' checked='checked' onclick=toggleConfirmation("
												+ priscription.getId() + ",'dos2'," + priscription.isDos2() + ")>");
								if(doselabletime!=null){
									if(!doselabletime.equals("")){
										if(!dosetime[1].equals("0")){
											buffer.append(""+dosetime[1]+"("+priscription.getUserid()+")<br>");
										}else{
											buffer.append("("+priscription.getUserid()+")<br>");
										}
									}else{
										buffer.append("("+priscription.getUserid()+")<br>");
									}
								}else{
									buffer.append("("+priscription.getUserid()+")<br>");
								}
							} else {
								buffer.append("<input type='checkbox' name='dos2' id='dos2' onclick=toggleConfirmation("
										+ priscription.getId() + ",'dos2'," + priscription.isDos2() + ")>");
								if(doselabletime!=null){
									if(!doselabletime.equals("")){
										if(!dosetime[1].equals("0")){
											buffer.append(""+dosetime[1]+"<br>");
										}else{
											buffer.append("<br>");
										}
									}else{
										buffer.append("<br>");
									}
								}else{
									buffer.append("<br>");
								}
							}
						} else {
							buffer.append("<input type='checkbox' name='dos2' id='dos2' onclick=toggleConfirmation("
									+ priscription.getId() + ",'dos2'," + priscription.isDos2() + ") disabled='true'>");
								if(doselabletime!=null){
									if(!doselabletime.equals("")){
										if(!dosetime[1].equals("0")){
											buffer.append(""+dosetime[1]+"<br>");
										}else{
											buffer.append("<br>");
										}
									}else{
										buffer.append("<br>");
									}
								}else{
									buffer.append("<br>");
								}
						}
						if (!priscription.getDosevalue3().equals("0")) {
							if (priscription.isDos3()) {
								buffer.append(
										"<input type='checkbox' name='dos3' id='dos3' checked='checked' onclick=toggleConfirmation("
												+ priscription.getId() + ",'dos3'," + priscription.isDos3() + ")>");
								if(doselabletime!=null){
									if(!doselabletime.equals("")){
										if(!dosetime[2].equals("0")){
											buffer.append(""+dosetime[2]+"("+priscription.getUserid()+")<br>");
										}else{
											buffer.append("("+priscription.getUserid()+")<br>");
										}
									}else{
										buffer.append("("+priscription.getUserid()+")<br>");
									}
								}else{
									buffer.append("("+priscription.getUserid()+")<br>");
								}
							} else {
								buffer.append("<input type='checkbox' name='dos3' id='dos3' onclick=toggleConfirmation("
										+ priscription.getId() + ",'dos3'," + priscription.isDos3() + ")>");
								if(doselabletime!=null){
									if(!doselabletime.equals("")){
										if(!dosetime[2].equals("0")){
											buffer.append(""+dosetime[2]+"<br>");
										}else{
											buffer.append("<br>");
										}
									}else{
										buffer.append("<br>");
									}
								}else{
									buffer.append("<br>");
								}
							}
						} else {
							buffer.append("<input type='checkbox' name='dos3' id='dos3' onclick=toggleConfirmation("
									+ priscription.getId() + ",'dos3'," + priscription.isDos3() + ") disabled='true'>");
								if(doselabletime!=null){
									if(!doselabletime.equals("")){
										if(!dosetime[2].equals("0")){
											buffer.append(""+dosetime[2]+"<br>");
										}else{
											buffer.append("<br>");
										}
									}else{
										buffer.append("<br>");
									}
								}else{
									buffer.append("<br>");
								}
						}

					}
					if (priscription.getDosesize() == 4) {

						if (!priscription.getDosevalue1().equals("0")) {
							if (priscription.isDos1()) {
								buffer.append(
										"<input type='checkbox' name='dos1' id='dos1' checked='checked' onclick=toggleConfirmation("
												+ priscription.getId() + ",'dos1'," + priscription.isDos1() + ")>");
								buffer.append("("+priscription.getUserid()+")<br>");
							} else {
								buffer.append(
										"<input type='checkbox' name='dos1' id='dos1' onclick='toggleConfirmation("
												+ priscription.getId() + ",'dos1'," + priscription.isDos1() + ")'>");
								buffer.append("<br>");
							}
						} else {
							buffer.append("<input type='checkbox' name='dos1' id='dos1' onclick=toggleConfirmation("
									+ priscription.getId() + ",'dos1'," + priscription.isDos1() + ") disabled='true'>");
							buffer.append("<br>");
						}
						if (!priscription.getDosevalue2().equals("0")) {
							if (priscription.isDos2()) {
								buffer.append(
										"<input type='checkbox' name='dos2' id='dos2' checked='checked' onclick=toggleConfirmation("
												+ priscription.getId() + ",'dos2'," + priscription.isDos2() + ")>");
								buffer.append("("+priscription.getUserid()+")<br>");
							} else {
								buffer.append("<input type='checkbox' name='dos2' id='dos2' onclick=toggleConfirmation("
										+ priscription.getId() + ",'dos2'," + priscription.isDos2() + ")>");
								buffer.append("<br>");
							}
						} else {
							buffer.append("<input type='checkbox' name='dos2' id='dos2' onclick=toggleConfirmation("
									+ priscription.getId() + ",'dos2'," + priscription.isDos2() + ") disabled='true'>");
							buffer.append("<br>");
						}
						if (!priscription.getDosevalue3().equals("0")) {
							if (priscription.isDos3()) {
								buffer.append(
										"<input type='checkbox' name='dos3' id='dos3' checked='checked' onclick=toggleConfirmation("
												+ priscription.getId() + ",'dos3'," + priscription.isDos3() + ")>");
								buffer.append("("+priscription.getUserid()+")<br>");
							} else {
								buffer.append("<input type='checkbox' name='dos3' id='dos3' onclick=toggleConfirmation("
										+ priscription.getId() + ",'dos3'," + priscription.isDos3() + ")>");
								buffer.append("<br>");
							}
						} else {
							buffer.append("<input type='checkbox' name='dos3' id='dos3' onclick=toggleConfirmation("
									+ priscription.getId() + ",'dos3'," + priscription.isDos3() + ") disabled='true'>");
							buffer.append("<br>");
						}
						if (!priscription.getDosevalue4().equals("0")) {
							if (priscription.isDos4()) {
								buffer.append(
										"<input type='checkbox' name='dos4' id='dos4' checked='checked' onclick=toggleConfirmation("
												+ priscription.getId() + ",'dos4'," + priscription.isDos4() + ")>");
								buffer.append("("+priscription.getUserid()+")<br>");
							} else {
								buffer.append("<input type='checkbox' name='dos4' id='dos4' onclick=toggleConfirmation("
										+ priscription.getId() + ",'dos4'," + priscription.isDos4() + ")>");
								buffer.append("<br>");
							}
						} else {
							buffer.append("<input type='checkbox' name='dos4' id='dos4' onclick=toggleConfirmation("
									+ priscription.getId() + ",'dos4'," + priscription.isDos4() + ") disabled='true'>");
							buffer.append("<br>");
						}
					}
					buffer.append("</td>");
					buffer.append("<td>" + priscription.getDosenotes() + "</td>");
					buffer.append("<td>" + priscription.getPrisctimename() + "</td>");
					buffer.append("<td>" + priscription.getPriscindivisualremark() + "</td>");
					buffer.append("<td><a href='#' onclick=deleteChildPriscIPD('" + priscription.getPrischildid()
							+ "','" + ipdid + "') ><i class='fa fa-times fa-2x text-danger' ></i></a></td>");
					buffer.append("</tr>");
				}
			}

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(buffer.toString());

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			connection.close();
		}
		return null;
	}

	// Akash 26 April 2018 called from addcharge its from ipddashobardaction
	public String getallnursingajax() throws Exception {

		Connection connection = null;

		try {
			String ipdid = request.getParameter("ipdid");
			connection = Connection_provider.getconnection();
			IpdDAO ipdDAO = new JDBCIpdDAO(connection);
			ArrayList<Master> ipdNursingList = ipdDAO.getAJaxNursing(ipdid);

			StringBuffer buffer = new StringBuffer();
			for (Master master : ipdNursingList) {

				if (master.getTaskname() != null) {

					buffer.append("<tr class='prstotip'>");
					buffer.append("<td>" + master.getTaskname() + "</td>");
					buffer.append("<td>" + master.getFrequency() + "</td>");
					buffer.append("<td>");
					if (master.getDosesize() == 3) {

						if (!master.getDosevalue1().equals("0")) {
							if (master.isDos1()) {
								buffer.append(
										"<input type='checkbox' name='dos1' id='dos1' checked='checked' onclick=togglenursingConfirm("
												+ master.getId() + ",'dos1'," + master.isDos1() + ")>");
							} else {
								buffer.append(
										"<input type='checkbox' name='dos1' id='dos1' onclick=togglenursingConfirm("
												+ master.getId() + ",'dos1'," + master.isDos1() + ")>");
							}
						} else {
							buffer.append("<input type='checkbox' name='dos1' id='dos1' onclick=togglenursingConfirm("
									+ master.getId() + ",'dos1'," + master.isDos1() + ") disabled='true'>");
						}
						if (!master.getDosevalue2().equals("0")) {
							if (master.isDos2()) {
								buffer.append(
										"<input type='checkbox' name='dos2' id='dos2' checked='checked' onclick=togglenursingConfirm("
												+ master.getId() + ",'dos2'," + master.isDos2() + ")>");
							} else {
								buffer.append(
										"<input type='checkbox' name='dos2' id='dos2' onclick=togglenursingConfirm("
												+ master.getId() + ",'dos2'," + master.isDos2() + ")>");
							}
						} else {
							buffer.append("<input type='checkbox' name='dos2' id='dos2' onclick=togglenursingConfirm("
									+ master.getId() + ",'dos2'," + master.isDos2() + ") disabled='true'>");
						}
						if (!master.getDosevalue3().equals("0")) {
							if (master.isDos3()) {
								buffer.append(
										"<input type='checkbox' name='dos3' id='dos3' checked='checked' onclick=togglenursingConfirm("
												+ master.getId() + ",'dos3'," + master.isDos3() + ")>");
							} else {
								buffer.append(
										"<input type='checkbox' name='dos3' id='dos3' onclick=togglenursingConfirm("
												+ master.getId() + ",'dos3'," + master.isDos3() + ")>");
							}
						} else {
							buffer.append("<input type='checkbox' name='dos3' id='dos3' onclick=togglenursingConfirm("
									+ master.getId() + ",'dos3'," + master.isDos3() + ") disabled='true'>");
						}

					}
					if (master.getDosesize() == 4) {

						if (!master.getDosevalue1().equals("0")) {
							if (master.isDos1()) {
								buffer.append(
										"<input type='checkbox' name='dos1' id='dos1' checked='checked' onclick=togglenursingConfirm("
												+ master.getId() + ",'dos1'," + master.isDos1() + ")>");
							} else {
								buffer.append(
										"<input type='checkbox' name='dos1' id='dos1' onclick='togglenursingConfirm("
												+ master.getId() + ",'dos1'," + master.isDos1() + ")'>");
							}
						} else {
							buffer.append("<input type='checkbox' name='dos1' id='dos1' onclick=togglenursingConfirm("
									+ master.getId() + ",'dos1'," + master.isDos1() + ") disabled='true'>");
						}
						if (!master.getDosevalue2().equals("0")) {
							if (master.isDos2()) {
								buffer.append(
										"<input type='checkbox' name='dos2' id='dos2' checked='checked' onclick=togglenursingConfirm("
												+ master.getId() + ",'dos2'," + master.isDos2() + ")>");
							} else {
								buffer.append(
										"<input type='checkbox' name='dos2' id='dos2' onclick=togglenursingConfirm("
												+ master.getId() + ",'dos2'," + master.isDos2() + ")>");
							}
						} else {
							buffer.append("<input type='checkbox' name='dos2' id='dos2' onclick=togglenursingConfirm("
									+ master.getId() + ",'dos2'," + master.isDos2() + ") disabled='true'>");
						}
						if (!master.getDosevalue3().equals("0")) {
							if (master.isDos3()) {
								buffer.append(
										"<input type='checkbox' name='dos3' id='dos3' checked='checked' onclick=togglenursingConfirm("
												+ master.getId() + ",'dos3'," + master.isDos3() + ")>");
							} else {
								buffer.append(
										"<input type='checkbox' name='dos3' id='dos3' onclick=togglenursingConfirm("
												+ master.getId() + ",'dos3'," + master.isDos3() + ")>");
							}
						} else {
							buffer.append("<input type='checkbox' name='dos3' id='dos3' onclick=togglenursingConfirm("
									+ master.getId() + ",'dos3'," + master.isDos3() + ") disabled='true'>");
						}
						if (!master.getDosevalue4().equals("0")) {
							if (master.isDos4()) {
								buffer.append(
										"<input type='checkbox' name='dos4' id='dos4' checked='checked' onclick=togglenursingConfirm("
												+ master.getId() + ",'dos4'," + master.isDos4() + ")>");
							} else {
								buffer.append(
										"<input type='checkbox' name='dos4' id='dos4' onclick=togglenursingConfirm("
												+ master.getId() + ",'dos4'," + master.isDos4() + ")>");
							}
						} else {
							buffer.append("<input type='checkbox' name='dos4' id='dos4' onclick=togglenursingConfirm("
									+ master.getId() + ",'dos4'," + master.isDos4() + ") disabled='true'>");
						}
					}
					buffer.append("</td>");
					// buffer.append("<td>"+master.getNotes()+"</td>");

					buffer.append("</tr>");
				}
			}

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(buffer.toString());

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			connection.close();
		}

		return null;
	}

	// Akash 26 April 2018 called from addcharge its from ipddashobardaction
	public String getajaxperpatientinvestigation() throws Exception {
		String ipdid = request.getParameter("ipdid");

		// Akash 03 oct 2017 investigation details in ipd dashborad
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			InvestigationDAO investigationDAO = new JDBCInvestigationDAO(connection);

			ArrayList<Investigation> invstigationList = investigationDAO.getIPDInvestList(ipdid);

			StringBuffer str = new StringBuffer();
			int i = 0;

			for (Investigation investigation : invstigationList) {

				str.append("<tr>");
				str.append("<td>" + (++i) + "</td>");
				str.append("<td>" + investigation.getInvsttype() + "</td>");
				str.append("<td>" + investigation.getDate() + "</td>");
				if (investigation.getInvstreporttype().equals("Numerical")) {
					str.append("<td><a href='#' onclick='printinvstigationrecord(" + investigation.getId()
							+ ",0)' title='Print Investigation' class='editpricon'><i class='fa fa-print'></i></a></td>");

				} else {
					str.append("<td><a href='#' onclick='printinvstigationrecord(" + investigation.getId()
							+ ",1)' title='Print Investigation' class='editpricon'><i class='fa fa-print'></i></a></td>");
				}

				if (investigation.getPacs() != 0) {
					str.append("<td><a href='#' onclick='viewpacsreport(" + investigation.getId()
							+ ")' title='Pacs Report' class='editpricon'><i class='fa fa-object-ungroup aria-hidden='true''></i></a></td>");
				} else {
					str.append("<td></td>");
				}

				str.append("</tr>");

			}

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("" + str.toString() + "");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return null;
	}

	// Akash 26 April 2018 called from addcharge its from ipddashobardaction
	public String getAjaxPerpatientdietary() throws Exception {

		Connection connection = null;

		try {
			String ipdid = request.getParameter("ipdid");
			connection = Connection_provider.getconnection();
			StringBuffer buffer = new StringBuffer();

			DietaryDetailsDAO detailsDAO = new JDBCDietaryDetailsDAO(connection);
			// ArrayList<DietaryDetails> dietarylist =
			// detailsDAO.getAllIpdDietplan();
			DietaryDetails dietaryDetails = detailsDAO.getIpdPerPatientDiet(ipdid);
			// Akash 10 nov 2017 dietary plan
			
			String mutliParents=detailsDAO.mutiParentIds(ipdid);
			if(!DateTimeUtils.isNull(mutliParents).equals("")){
				for(String parentId:mutliParents.split(",")){
					dietaryDetails.setParentid(parentId);
					buffer.append("<tr>");
					buffer.append("<td><span id='c" + dietaryDetails.getBedid() + "'><i class='fa fa-bell'></i></span> / "
							+ dietaryDetails.getWardname() + " <br> " + dietaryDetails.getClientname() + " <br>"
							+ dietaryDetails.getAge() + "</td>");
					for (int i = 1; i <= 9; i++) {
						ArrayList<DietaryDetails> arrayList = detailsDAO.getSingleDietplanList(dietaryDetails.getParentid(),
								"" + i);
						buffer.append("<td>");
						int x = 0;
						for (DietaryDetails dietaryDetails2 : arrayList) {
							if (x == 0) {
								if (dietaryDetails2.getExecuted().equals("0")) {
									buffer.append("<input type='checkbox' onclick=updateDietaryGivenStatus('"
											+ dietaryDetails.getParentid() + "','" + i + "',this.checked)>"
											+ dietaryDetails2.getSubcategory() + "<br>");
								} else {
									buffer.append("<input type='checkbox' checked='checked' onclick=updateDietaryGivenStatus('"
											+ dietaryDetails.getParentid() + "','" + i + "',this.checked)>"
											+ dietaryDetails2.getSubcategory() + "<br>");
								}

							} else {
								buffer.append("" + dietaryDetails2.getSubcategory() + "<br>");
							}
							x++;

						}
						buffer.append("</td>");
						/*
						 * if(i==0){ DietaryDetails details =
						 * detailsDAO.getSingleDietplan(dietaryDetails.getParentid(),
						 * "Breakfast"); if(details.getSubcategory()!=null){
						 * buffer.append("<td><center>"+details.getSubcategory()+
						 * "</center></td>"); }else{ buffer.append("<td></td>"); } }else
						 * if(i==1){ DietaryDetails details =
						 * detailsDAO.getSingleDietplan(dietaryDetails.getParentid()
						 * ,"Midmorning Snack"); if(details.getSubcategory()!=null){
						 * buffer.append("<td><center>"+details.getSubcategory()+
						 * "</center></td>"); }else{ buffer.append("<td></td>"); } }else
						 * if(i==2){ DietaryDetails details =
						 * detailsDAO.getSingleDietplan(dietaryDetails.getParentid(),
						 * "Lunch"); if(details.getSubcategory()!=null){
						 * buffer.append("<td><center>"+details.getSubcategory()+
						 * "</center></td>"); }else{ buffer.append("<td></td>"); } }else
						 * if(i==3){ DietaryDetails details =
						 * detailsDAO.getSingleDietplan(dietaryDetails.getParentid()
						 * ,"Midafternoon Snack"); if(details.getSubcategory()!=null){
						 * buffer.append("<td><center>"+details.getSubcategory()+
						 * "</center></td>"); }else{ buffer.append("<td></td>"); } }else
						 * if(i==4){ DietaryDetails details =
						 * detailsDAO.getSingleDietplan(dietaryDetails.getParentid()
						 * ,"Midevening Snack"); if(details.getSubcategory()!=null){
						 * buffer.append("<td><center>"+details.getSubcategory()+
						 * "</center></td>"); }else{ buffer.append("<td></td>"); } }else
						 * if(i==5){ DietaryDetails details =
						 * detailsDAO.getSingleDietplan(dietaryDetails.getParentid(),
						 * "Dinner"); if(details.getSubcategory()!=null){
						 * buffer.append("<td><center>"+details.getSubcategory()+
						 * "</center></td>"); }else{ buffer.append("<td></td>"); } }
						 */

					}
					buffer.append("</tr>");

				}
			}
			
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(buffer.toString());

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			connection.close();
		}
		return null;
	}

	public String deletechildprisc() throws Exception {
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			IpdDAO ipdDAO = new JDBCIpdDAO(connection);
			String prischildid = request.getParameter("prischildid");
			StringBuffer buffer = new StringBuffer();
			String date = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
			String userid = loginInfo.getUserId();
			int result = ipdDAO.updatePriscChildFromIpd(prischildid, date, userid);
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(buffer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String saveimmurtizationdata() throws Exception {
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			String clientId = request.getParameter("clientId");
			String colname = request.getParameter("colname");
			String val = request.getParameter("val");

			/*
			 * StringBuffer buffer=new StringBuffer(); String date =
			 * DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
			 * String userid = loginInfo.getUserId();
			 */
			int result = notAvailableSlotDAO.updateimmurtizationchart(clientId, colname, val);
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return null;

	}

	public String saveTempIPDData() throws Exception {
		Connection connection = null;
		IpdDAO ipddao = null;
		try {
			connection = Connection_provider.getconnection();
			StringBuffer strbuffer = new StringBuffer();

			StringBuilder buffer = new StringBuilder();
			BufferedReader reader = request.getReader();
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			String data = buffer.toString();
			JSONObject jsonObject = new JSONObject(data);
			String addmissionfor = jsonObject.getString("addmissionfor");
			String alergies = jsonObject.getString("alergies");
			String packagename = jsonObject.getString("packagename");
			String chiefcomplains = jsonObject.getString("chiefcomplains");
			String presentillness = jsonObject.getString("presentillness");
			String onexamination = jsonObject.getString("onexamination");
			String pasthistory = jsonObject.getString("pasthistory");
			String familyhist = jsonObject.getString("familyhist");
			String personalhist = jsonObject.getString("personalhist");
			String surgicalnotes = jsonObject.getString("surgicalnotes");
			String suggestedtrtment = jsonObject.getString("suggestedtrtment");
			String earlierinvest = jsonObject.getString("earlierinvest");
		
		String birthhist = jsonObject.getString("birthhist");
		String emmunizationhist = jsonObject.getString("emmunizationhist");
		String diethist = jsonObject.getString("diethist");
		String developmenthist = jsonObject.getString("developmenthist");
		
			String ipdid = jsonObject.getString("ipdid");
			String clientid = jsonObject.getString("clientid");
			String coloumn = "";

			ipddao = new JDBCIpdDAO(connection);
			boolean flag = false;
			flag = ipddao.checkOfTempData(ipdid, clientid, coloumn);
			if (flag) {
				int b = ipddao.updateTempIPDData(ipdid, clientid, addmissionfor, alergies, packagename, chiefcomplains,
						presentillness, onexamination, pasthistory, familyhist, personalhist, surgicalnotes,
						suggestedtrtment, earlierinvest,diethist, birthhist, developmenthist, emmunizationhist);

			} else {
				int a = ipddao.insertTempIPDData(ipdid, clientid, addmissionfor, alergies, packagename, chiefcomplains,
						presentillness, onexamination, pasthistory, familyhist, personalhist, surgicalnotes,
						suggestedtrtment, earlierinvest,diethist, birthhist, developmenthist, emmunizationhist);
			}

			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("1");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getIPDTempData() throws Exception {
		try {

			if (!verifyLogin(request)) {
				return "login";
			}
			Connection connection = null;
			IpdDAO ipddao = null;
			Bed bed = new Bed();

			connection = Connection_provider.getconnection();
			StringBuffer strbuffer = new StringBuffer();

			StringBuilder buffer = new StringBuilder();
			BufferedReader reader = request.getReader();
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			String data = buffer.toString();
			JSONObject jsonObject = new JSONObject(data);

			String ipdid = jsonObject.getString("ipdid");
			String clientid = jsonObject.getString("clientid");

			ipddao = new JDBCIpdDAO(connection);

			bed = ipddao.getAllIPDData(ipdid, clientid);

			String addmissionfor = bed.getAddmissionfor();
			String alergies = bed.getAlergies();
			String packagename = bed.getPackagename();
			String chiefcomplains = bed.getChiefcomplains();
			String presentillness = bed.getPresentillness();

			String onexamination = bed.getOnexamination();
			String pasthistory = bed.getPasthistory();
			String familyhist = bed.getFamily_history();
			String personalhist = bed.getPersonalhist();
			String surgicalnotes = bed.getSurgicalnotes();
			String suggestedtrtment = bed.getSuggestedtrtment();
			String earlierinvest = bed.getEarlierinvest();
           String birthhist = bed.getBirthhist();
           String diethist = bed.getDiethist();
           String emmunizationhist = bed.getEmmunizationhist();
           String developmenthist = bed.getDevelopmenthist();
			
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("addmissionfor", addmissionfor);
			jsonobj.put("alergies", alergies);
			jsonobj.put("packagename", packagename);
			jsonobj.put("chiefcomplains", chiefcomplains);
			jsonobj.put("presentillness", presentillness);
			jsonobj.put("onexamination", onexamination);
			jsonobj.put("pasthistory", pasthistory);
			jsonobj.put("familyhist", familyhist);
			jsonobj.put("personalhist", personalhist);
			jsonobj.put("surgicalnotes", surgicalnotes);
			jsonobj.put("suggestedtrtment", suggestedtrtment);
			jsonobj.put("earlierinvest", earlierinvest);

			jsonobj.put("birthhist", birthhist);
			jsonobj.put("diethist", diethist);
			jsonobj.put("emmunizationhist", emmunizationhist);
			jsonobj.put("developmenthist", developmenthist);
			
			
			String response1 = jsonobj.toString();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(response1);

		} catch (Exception e) {
			e.printStackTrace();

		}

		return null;
	}
	
	public String savediagnosisfastJSON()throws Exception{

		if (!verifyLogin(request)) {
			return "login";
		}
		try{ 
		Connection connection= Connection_provider.getconnection();
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		String data = buffer.toString();
		JSONObject jsonObject = new JSONObject(data);

		String condition = jsonObject.getString("condition");
		String icdcode= "0";
		DiagnosisDAO  diagnosisDAO =new JDBCDiagnosisDAO(connection);
		Diagnosis diagnosis=new Diagnosis();
		diagnosis.setName(condition);
		diagnosis.setIcdcode(icdcode);
		int checkid=diagnosisDAO.checkExixstDiagnosis(diagnosis.getName());
		int id=0;
		if (checkid>0) {
			id=0;
		}else{
		 id=diagnosisDAO.addDiagnosisName(diagnosis);
		}
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("condition", id);
		
		String response1 = jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(response1);

		
		
		}catch(Exception e){
			e.printStackTrace();
		}
		 
		 return null;
	 }
	
public String setAllDiagosisJSON(){

	if (!verifyLogin(request)) {
		return "login";
	}
	
	try{
		Connection connection= Connection_provider.getconnection();
		DiagnosisDAO diagnosisDAO= new JDBCDiagnosisDAO(connection);
		StringBuilder buffer1 = new StringBuilder();
		StringBuffer buffer= new StringBuffer();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer1.append(line);
		}
		String data = buffer1.toString();
		JSONObject jsonObject = new JSONObject(data);

		String selected = jsonObject.getString("selected");
		int i = 0;
		for (String s : selected.split(",")) {

			int d = Integer.parseInt(s);
			if (d == 0) {
				continue;
			} else {

				Diagnosis diagnosis = diagnosisDAO.getDiagnosisName(s);
				buffer.append("<tr>");
				buffer.append("<td><input type='checkbox' checked='checked' onclick=reoveThisRow(this,'"
						+ diagnosis.getId() + "') value='" + diagnosis.getId()
						+ "' class='classD' /> <input type='hidden' value=" + diagnosis.getId()
						+ " name='conditions[" + i + "].conditionid'  /> </td>");
				buffer.append("<td>" + diagnosis.getName() + "</td>");
				buffer.append("<td class='hidden'><a onclick=reoveThisRow('" + diagnosis.getId()
						+ "')><i class='fa fa-trash-o'></i></a></td>");
				buffer.append("</tr>");
				i++;
			}

		}
String responsej= buffer.toString();
JSONObject jsonobj = new JSONObject();
jsonobj.put("responsej", responsej);
		

String response1 = jsonobj.toString();
response.setContentType("application/json");
response.setHeader("Cache-Control", "no-cache");
response.getWriter().write(response1);


	}catch(Exception e){
		e.printStackTrace();
	}
	
	return null;
}

public String getdiagnosisJSON()throws Exception{

	if (!verifyLogin(request)) {
		return "login";
	}
	try{
		Connection connection= Connection_provider.getconnection();
		IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		StringBuilder buffer1 = new StringBuilder();
		
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer1.append(line);
		}
		String data = buffer1.toString();
		JSONObject jsonObject = new JSONObject(data);
		
		String selected= jsonObject.getString("selected");
		
		String search= jsonObject.getString("search");
		
		ArrayList<Client> ipdCondtitionList = ipdDAO.getAllDiagnosis(search);
		StringBuilder builder = new StringBuilder();
		for (Client client : ipdCondtitionList) {

			boolean flag = false;

			for (String s : selected.split(",")) {

				if (s == null) {
					continue;
				}

				int d = Integer.parseInt(s);
				if (d == 0) {
					continue;
				}
				if (d == client.getId()) {
					flag = true;
					break;
				}
			}
			if(loginInfo.isF_diagnosis_discharge()){
				if (flag) {

					builder.append("<tr>");
					builder.append("<td><input type='checkbox' checked='checked' onclick='setfreedischargedata(this)'  value='"
							+ client.getId() + "'  /></td>");
					builder.append("<td>" + client.getTreatmentType() + "</td>");
					builder.append("</tr>");

				} else {

					builder.append("<tr>");
					builder.append("<td><input type='checkbox' onclick='setfreedischargedata(this)'  value='" + client.getId()
							+ "'  /></td>");
					builder.append("<td>" + client.getTreatmentType() + "</td>");
					builder.append("</tr>");

			}
			}	else{
				
				if (flag) {

					builder.append("<tr>");
					builder.append("<td><input type='checkbox' checked='checked' onclick='setCheckedData(this)' value='"
							+ client.getId() + "'  /></td>");
					builder.append("<td>" + client.getTreatmentType() + "</td>");
					builder.append("</tr>");

				} else {

					builder.append("<tr>");
					builder.append("<td><input type='checkbox' onclick='setCheckedData(this)' value='" + client.getId()
							+ "'  /></td>");
					builder.append("<td>" + client.getTreatmentType() + "</td>");
					builder.append("</tr>");

			}
		}


		}
	String responsej=	builder.toString();
	JSONObject jsonobj = new JSONObject();
	jsonobj.put("responsej", responsej);
		

String response1 = jsonobj.toString();
response.setContentType("application/json");
response.setHeader("Cache-Control", "no-cache");
response.getWriter().write(response1);


		
	}catch(Exception e){
		e.printStackTrace();
	}
	
	return null;
}

public String getprodiagnosisJSON()throws Exception{

	if (!verifyLogin(request)) {
		return "login";
	}
	try{
		Connection connection= Connection_provider.getconnection();
		IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		StringBuilder buffer1 = new StringBuilder();
		
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer1.append(line);
		}
		String data = buffer1.toString();
		JSONObject jsonObject = new JSONObject(data);
		
		String selected= jsonObject.getString("selected");
		
		String search= jsonObject.getString("search");
		
		ArrayList<Client> ipdCondtitionList = ipdDAO.getAllDiagnosis(search);
		StringBuilder builder = new StringBuilder();
		for (Client client : ipdCondtitionList) {

			boolean flag = false;

			for (String s : selected.split(",")) {

				if (s == null) {
					continue;
				}

				int d = Integer.parseInt(s);
				if (d == 0) {
					continue;
				}
				if (d == client.getId()) {
					flag = true;
					break;
				}
			}
			
				
				if (flag) {

					builder.append("<tr>");
					builder.append("<td><input type='checkbox' checked='checked' onclick='setCheckedData(this)' value='"
							+ client.getId() + "'  /></td>");
					builder.append("<td>" + client.getTreatmentType() + "</td>");
					builder.append("</tr>");

				} else {

					builder.append("<tr>");
					builder.append("<td><input type='checkbox' onclick='setCheckedData(this)' value='" + client.getId()
							+ "'  /></td>");
					builder.append("<td>" + client.getTreatmentType() + "</td>");
					builder.append("</tr>");

			}
		


		}
	String responsej=	builder.toString();
	JSONObject jsonobj = new JSONObject();
	jsonobj.put("responsej", responsej);
		

String response1 = jsonobj.toString();
response.setContentType("application/json");
response.setHeader("Cache-Control", "no-cache");
response.getWriter().write(response1);


		
	}catch(Exception e){
		e.printStackTrace();
	}
	
	return null;
}




public String setalldiagnosisemrJSON(){

	if (!verifyLogin(request)) {
		return "login";
	}
	Connection connection= null;
	try{
		connection= Connection_provider.getconnection();
		StringBuilder buffer1 = new StringBuilder();
		DiagnosisDAO diagnosisDAO=new JDBCDiagnosisDAO(connection);
		StringBuffer builder=new StringBuffer();
	
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer1.append(line);
		}
		String data = buffer1.toString();
		JSONObject jsonObject = new JSONObject(data);
		
		String selected= jsonObject.getString("selected");
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
		String responsej=	builder.toString();
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("responsej", responsej);
			
		
		String response1 = jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(response1);


	}catch(Exception e){
		e.printStackTrace();
	}
	
	
	return null;
}


public String searchdiagnosisemrJSON(){

	if (!verifyLogin(request)) {
		return "login";
	}
	Connection connection= null;
	try{
	connection= Connection_provider.getconnection();
	StringBuilder buffer1 = new StringBuilder();
	DiagnosisDAO diagnosisDAO=new JDBCDiagnosisDAO(connection);
	StringBuilder builder=new StringBuilder();

	BufferedReader reader = request.getReader();
	String line;
	while ((line = reader.readLine()) != null) {
		buffer1.append(line);
	}
	String data = buffer1.toString();
	JSONObject jsonObject = new JSONObject(data);
	
	String selected= jsonObject.getString("selected");
	String search= jsonObject.getString("search");
	IpdDAO ipdDAO = new JDBCIpdDAO(connection);
	ArrayList<Client> ipdCondtitionList = ipdDAO.getAllDiagnosis(search);
	
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
	
	
	String responsej=	builder.toString();
	JSONObject jsonobj = new JSONObject();
	jsonobj.put("responsej", responsej);
		
	
	String response1 = jsonobj.toString();
	response.setContentType("application/json");
	response.setHeader("Cache-Control", "no-cache");
	response.getWriter().write(response1);

		
	}catch(Exception e){
e.printStackTrace();		
	}
	
	return null;
}


public String searchdiagnosiseditemrJSON(){

	if (!verifyLogin(request)) {
		return "login";
	}
	Connection connection= null;
	try{
		connection= Connection_provider.getconnection();
		StringBuilder buffer1 = new StringBuilder();
		DiagnosisDAO diagnosisDAO=new JDBCDiagnosisDAO(connection);
		IpdDAO ipdDAO = new JDBCIpdDAO(connection);

		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer1.append(line);
		}
		String data = buffer1.toString();
		JSONObject jsonObject = new JSONObject(data);
		
		String selected= jsonObject.getString("selected");
		String search= jsonObject.getString("search");
		
		
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
		
		

		String responsej=	builder.toString();
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("responsej", responsej);
			
		
		String response1 = jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(response1);

		
		
	}catch(Exception e){
		e.printStackTrace();
	}
	return null;
}

public String newmedicineJSON(){

	if (!verifyLogin(request)) {
		return "login";
	}
	Connection connection= null;
	try{
		connection =Connection_provider.getconnection();
		StringBuilder buffer1 = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer1.append(line);
		}
		String data = buffer1.toString();
		JSONObject jsonObject = new JSONObject(data);
		
		MasterDAO masterDAO= new JDBCMasterDAO(connection);
		InventoryProductDAO inventoryProductDAO= new JDBCInventoryProductDAO(connection);
		PrescriptionMasterDAO prescriptionMasterDAO= new JDBCPrescriptionMasterDAO(connection);
		
		
		String medicinename= jsonObject.getString("medicinename");
		String genericname= jsonObject.getString("genericname");
		String istemp = jsonObject.getString("istemp");
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
		
		product.setUserid(loginInfo.getUserId());
		product.setTodate(DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0]);
		
		/*int res = inventoryProductDAO.addNewProduct(product);*/
		//int result = inventoryProductDAO.addNewProductToVendor(loc, res);
		int res=0;
		int result=prescriptionMasterDAO.addToMedicineMaster(product,res,istemp);
		
		
		
		//add to medicine master
		
		ArrayList<Master>medicineList = masterDAO.getMedicineList();
		StringBuffer buffer =new StringBuffer();
		/*buffer.append("<select class='form-control chosen' id='mdicinename' name='mdicinename' onchange='getDoseNote(this.value)' > ");
		buffer.append("<option value='0'>Select Medicine</option>");
		if(istemp.equals("1")){
			Master master1 = masterDAO.getMedicineDetails(result);
			buffer.append("<option value='"+master1.getId()+"'> "+master1.getGenericname()+" </option>");
		}
		
		for(Master master:medicineList){
			
			buffer.append("<option value='"+master.getId()+"'> "+master.getGenericname()+" </option>");
		}
		buffer.append("</select>");
		
		buffer.append("<a href='#' type='button' class='btn btn-sm btn-primary' onclick=openhiddendiv('hiddendiv') style='margin-left: 5px;'><i  class='fa fa-plus'></i></a>");
		*/
		
		if(loginInfo.isAdd_medicine() || loginInfo.getUserType()==2){
			buffer.append("<a title='Add New Medicine' href='#' type='button' class='btn btn-sm btn-primary' onclick=openhiddendiv('hiddendiv') style='margin-right: 5px;'><i class='fa fa-pencil' aria-hidden='true'></i></a>");
		} else{
			buffer.append("<a title='Add New Medicine' href='#' type='button' class='btn btn-sm btn-primary'  style='margin-right: 5px;'><i class='fa fa-pencil' aria-hidden='true'></i></a>");
		} 
		buffer.append("<select class='form-control chosen' id='mdicinename' name='mdicinename' onchange='getDoseNote(this.value)' > ");
		buffer.append("<option value='0'>Select Medicine</option>");
		if(istemp.equals("1")){
			Master master1 = masterDAO.getMedicineDetails(result);
			buffer.append("<option value='"+master1.getId()+"'> "+master1.getGenericname()+" </option>");
		}
		for(Master master:medicineList){
			buffer.append("<option value='"+master.getId()+"'> "+master.getGenericname()+" </option>");
		}
		buffer.append("</select>");
		//buffer.append("<a href='#' type='button' class='btn btn-sm btn-primary' onclick=openhiddendiv('hiddendiv') style='margin-left: 5px;'><i  class='fa fa-plus'></i></a>");
		
		if(loginInfo.isAdd_medicine() || loginInfo.getUserType()==2){
			buffer.append("&nbsp;<div><a href='#'><i class='fa fa-plus' style='width:10px;' onclick=openBlankPopup('addPrescriptiondetails')></i></a></div>");
		} else{
			buffer.append("&nbsp;<div><a href='#'><i class='fa fa-plus' style='width:10px;' ></i></a></div>");
		} 

		String responsej=	buffer.toString();
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("responsej", responsej);
			
		
		String response1 = jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(response1);

		
		
	}
	catch(Exception e){
		e.printStackTrace();
	}
	return null;
}
public String getvisitingconsultantlist() throws Exception {
	Connection connection = null;
	try {
		connection = Connection_provider.getconnection();
		UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
		StringBuffer str = new StringBuffer();
		/*ArrayList<UserProfile> visitingConsultDoctors=userProfileDAO.getVisitingPractitinerList();*/
		ArrayList<UserProfile> visitingConsultDoctors=userProfileDAO.getVisitingPractitinerListFromUser();
		str.append("<select name='consultantdr' id='consultantdr' class='form-control showToolTip chosen' >");
		str.append("<option value='0'>Select Consultant</option>");
		for (UserProfile userProfile : visitingConsultDoctors) {
			str.append("<option value='" + userProfile.getId() + "'>" + userProfile.getFullname() + "</option>");
		}
		str.append("</select>");

		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write("" + str.toString() + "");

	} catch (Exception e) {
		e.printStackTrace();
	}finally{
		connection.close();
	}
	return null;
}

//Akash 16 May 2018
//To speed up
public String charge()throws Exception{
	Connection connection=null;
    try {
    	connection=Connection_provider.getconnection();
    	IpdDAO ipdDAO = new JDBCIpdDAO(connection);
    	ChargesAccountProcessingDAO chargesAccountProcessingDAO= new JDBCChargeAccountProcesDAO(connection);
    	
    	String chargetype = request.getParameter("chargetype");
    	
    	String ipdwhopay = request.getParameter("ipdwhopay");
    	String ipdtpid = request.getParameter("ipdtpid");
    	String ipdaddmissionid = request.getParameter("ipdaddmissionid");
    	String clientId = request.getParameter("clientId");
    	
    	if(ipdwhopay.equals("")){
    		ClientDAO clientDAO = new JDBCClientDAO(connection);
    		Client client = clientDAO.getClientDetails(clientId);
    		ipdwhopay = client.getWhopay();
    	}
    	
    	if(ipdwhopay.equals(Constants.PAY_BY_CLIENT)){
    		ipdtpid = "0";
    	}
    	
    	if(ipdaddmissionid==null){
    		
    		ipdaddmissionid="0";
    	}
    	if(ipdaddmissionid.equals("")){
    		ipdaddmissionid="0";
    	}
    	
    	BedDao bedDao=new JDBCBedDao(connection);
		Bed bed = bedDao.getEditIpdData(ipdaddmissionid);
		
		String wardid = bed.getWardid();
		
		if(wardid==null){
			wardid="0";
		}
		if(wardid.equals("")){
			wardid="0";
		}
		
		int compulsary_consultant=chargesAccountProcessingDAO.isCompulasryConsultant(chargetype);
    	
   	ArrayList<Master>list = ipdDAO.getFilteredChargeList(chargetype,ipdtpid,wardid,clientId,loginInfo.isShow_wardname());
    	StringBuffer str = new StringBuffer();
   
    		str.append("<select onchange='setAdditionalChargeAjax1(this.value)' name='chargeTYpe' id='chargeTYpe' class='form-control showToolTip chosen' >");
    		str.append("<option value='0'>Select Charge</option>");
    		for(Master master : list){
    			str.append("<option value='"+master.getId()+"'>"+master.getName()+"</option>");
    		}
    		str.append("</select>");
    		
    		str.append("!@");
    		str.append(compulsary_consultant);
    		response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+str.toString()+""); 
    	
    }catch(Exception e){
 	   e.printStackTrace();
    }
    finally{
		connection.close();
	}
	return null;
}
//Akash 16 May 2018
public String apmtcharge()throws Exception{
	
	   Connection connection=null;
     
     try {
     	
     	connection=Connection_provider.getconnection();
     	IpdDAO ipdDAO = new JDBCIpdDAO(connection);
     	String chargetype = request.getParameter("chargetype");
     	String tpid=request.getParameter("tpid");
     	String whopay = request.getParameter("whopay");
     	
     	if(whopay.equals(Constants.PAY_BY_CLIENT)){
     		tpid = "0";
     	}
     	
     	
     	ArrayList<Master>list = ipdDAO.getFilteredOpdChargeList(chargetype,tpid,whopay);
     	StringBuffer str = new StringBuffer();
     	
    
     		str.append("<select onchange='setAdditionalChargeAjax(this.value)' name='chargeTYpe' id='addChargeType' class='form-control showToolTip chosen' >");
     		str.append("<option value='0'>Select Charge</option>");
     		for(Master master : list){
     			str.append("<option value='"+master.getId()+"'>"+master.getName()+"</option>");
     		}
     		str.append("</select>");
     		
     		response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write(""+str.toString()+""); 
     	
     }catch(Exception e){
  	   e.printStackTrace();
     }finally{
			connection.close();
		}
	
	return null;
}
public String saveTempIPDDischargeData() throws Exception {
	Connection connection = null;
	IpdDAO ipddao = null;
	try {
		connection = Connection_provider.getconnection();
		StringBuffer strbuffer = new StringBuffer();

		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		String data = buffer.toString();
		JSONObject jsonObject = new JSONObject(data);
		

		   	String history = jsonObject.getString("history");
		String surgicalnotes = jsonObject.getString("surgicalnotes");
		String hospitalcourse = jsonObject.getString("hospitalcourse");
		String treatmentgiven = jsonObject.getString("treatmentgiven");
		String investigation = jsonObject.getString("investigation");
		String findondis = jsonObject.getString("findondis");
		String dischargeadvice = jsonObject.getString("dischargeadvice");
	
	String birthhist = jsonObject.getString("birthhist");
	String emmunizationhist = jsonObject.getString("emmunizationhist");
	String diethist = jsonObject.getString("diethist");
	String developmenthist = jsonObject.getString("developmenthist");
	
		String ipdid = jsonObject.getString("ipdid");
		String clientid = jsonObject.getString("clientid");
		String coloumn = "";

		ipddao = new JDBCIpdDAO(connection);
		boolean flag = false;
		flag = ipddao.checkOfTempData(ipdid, clientid, coloumn);
		if (flag) {
			int b = ipddao.updateTempIPDDischargeData(ipdid, clientid, history, surgicalnotes, hospitalcourse, treatmentgiven, investigation, findondis, dischargeadvice, diethist, birthhist, developmenthist, emmunizationhist);
					

		} else {
			int a = ipddao.insertTempIPDDischargeData(ipdid, clientid, history, surgicalnotes, hospitalcourse, treatmentgiven, investigation, findondis, dischargeadvice, diethist, birthhist, developmenthist, emmunizationhist);
		}

		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write("1");

	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}



public String getIPDTempDischargeData() throws Exception {
	try {

		if (!verifyLogin(request)) {
			return "login";
		}
		Connection connection = null;
		IpdDAO ipddao = null;
		Bed bed = new Bed();

		connection = Connection_provider.getconnection();
		StringBuffer strbuffer = new StringBuffer();

		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		String data = buffer.toString();
		JSONObject jsonObject = new JSONObject(data);

		String ipdid = jsonObject.getString("ipdid");
		String clientid = jsonObject.getString("clientid");

		ipddao = new JDBCIpdDAO(connection);

		bed = ipddao.getAllIPDDischargeData(ipdid, clientid);
		
		
       String birthhist = bed.getBirthhist();
       String diethist = bed.getDiethist();
       String emmunizationhist = bed.getEmmunizationhist();
       String developmenthist = bed.getDevelopmenthist();
		
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("surgicalnotes", bed.getSurgicalnotes());
		jsonobj.put("hospitalcourse",bed.getHospitalcourse());
		jsonobj.put("treatmentgiven", bed.getTreatmentgiven());
		jsonobj.put("investigation", bed.getInvestigation());
		jsonobj.put("findondis", bed.getFindondis());
		jsonobj.put("dischargeadvice", bed.getDischargeadvice());
	
		jsonobj.put("birthhist", birthhist);
		jsonobj.put("diethist", diethist);
		jsonobj.put("emmunizationhist", emmunizationhist);
		jsonobj.put("developmenthist", developmenthist);
		
		
		String response1 = jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(response1);

	} catch (Exception e) {
		e.printStackTrace();

	}

	return null;
}

//Akash 21 May 2018 

public String getipdrmodata() throws Exception {
	Connection connection = null;
	try {

		if (!verifyLogin(request)) {
			return "login";
		}
		
		connection = Connection_provider.getconnection();
		IpdDAO ipddao = new JDBCIpdDAO(connection);
		
		StringBuffer strbuffer = new StringBuffer();
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		
		String data = buffer.toString();
		JSONObject jsonObject = new JSONObject(data);

		String ipdid = jsonObject.getString("ipdid");
		String clientid = jsonObject.getString("clientid");
		
		String addmissiondate = ipddao.getIPDAdmissionDate(ipdid);
		long day=0;
		if (!addmissiondate.equals("")) {
			String[] addate = addmissiondate.split(" ");
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();
			String toDate = dateFormat.format(cal.getTime());
			toDate = DateTimeUtils.getCommencingDate1(toDate);
			
			String date1[] = addate[0].split("-");
			String date= date1[2]+"/"+date1[1]+"/"+date1[0];
			
			Date d1=new SimpleDateFormat("dd/MM/yyyy").parse(date);
			Date d2= new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
			long diff=d2.getTime()- d1.getTime();
			long difference= (Math.abs((diff / (1000*60*60*24))));
			day = difference++;
		}
		
		ArrayList<Ipd> arrayList = ipddao.getAllRMONotes(ipdid,day);
		
		Ipd ipd = ipddao.getRmoNoteDay(ipdid,day);
		
		JSONObject jsonobj = new JSONObject();
		strbuffer.append("<tr>");
		strbuffer.append("<input type='hidden' id='rmocurdateid' value='"+ipd.getId()+"'>");
		strbuffer.append("<input type='hidden' id='rmocurday' value='"+day+"'>");
		strbuffer.append("<td>Day: "+day+"</td>");
		strbuffer.append("</tr>");
		strbuffer.append("<tr>");
		strbuffer.append("<td><textarea rows='6'  class='form-control' id='rmonewdaynote' placeholder='Day to Day Note' style='background-color: beige;'>"+ipd.getNotes()+"</textarea></td>");
		strbuffer.append("</tr>");
		
		StringBuffer buffer2 = new StringBuffer();
		for (Ipd ipd2 : arrayList) {
			buffer2.append("<tr>");
			buffer2.append("<td>Day: "+ipd2.getDay()+"</td>");
			buffer2.append("</tr>");
			buffer2.append("<tr>");
			buffer2.append("<td><p>"+ipd2.getNotes()+"</p></td>");
			buffer2.append("</tr>");
		}
		
		jsonobj.put("prenoteslist", buffer2.toString());
		jsonobj.put("curentdaynotes", strbuffer.toString());
		
		String response1 = jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(response1);

	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}

//24 May 2018

public String savermonotes() throws Exception {
	Connection connection = null;
	try {

		if (!verifyLogin(request)) {
			return "login";
		}
		
		connection = Connection_provider.getconnection();
		IpdDAO ipddao = new JDBCIpdDAO(connection);
		
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		
		String data = buffer.toString();
		JSONObject jsonObject = new JSONObject(data);

		String ipdid = jsonObject.getString("ipdid");
		String clientid = jsonObject.getString("clientid");
		String rmonotes = jsonObject.getString("rmonotes");
		String rmocurdateid = jsonObject.getString("rmocurdateid");
		String rmocurday = jsonObject.getString("rmocurday");
		
		Ipd ipd = new Ipd();
		ipd.setIpdid(ipdid);
		ipd.setClientid(clientid);
		ipd.setNotes(rmonotes);
		ipd.setDay(rmocurday);
		ipd.setId(Integer.parseInt(rmocurdateid));
		int res =0;
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Calendar cal = Calendar.getInstance();
		String toDate = dateFormat.format(cal.getTime());
		toDate = DateTimeUtils.getCommencingDate1(toDate);
		ipd.setDate(toDate);
		if(ipd.getId()>0){
			res = ipddao.updateRMONotes(ipd);
		}else{
			res = ipddao.saveRMONotes(ipd);
		}
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("response1", res);
		String response1 = jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(response1);

	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}

//28 May 2018

public String checkrefundpaidstatus() throws Exception{
	Connection connection = null;
	try {
		connection = Connection_provider.getconnection();
		AdditionalDAO additionalDAO = new JDBCAdditionalDAO(connection);
		String id = request.getParameter("id");
		String clientid = request.getParameter("clientid");
		
		int res = additionalDAO.checkrefundStatus(id,clientid);
		StringBuffer buffer =new StringBuffer();
		buffer.append(""+res+"");
		buffer.append("~");
		buffer.append(""+id+"");
		buffer.append("~");
		buffer.append(""+clientid+"");
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+buffer.toString()+""); 
	
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}

//Akash 02 Jun 2018
public String getinvoicechargeslist() throws Exception {
	Connection connection = null;
	try {
		if (!verifyLogin(request)) {
			return "login";
		}
		connection = Connection_provider.getconnection();
		AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
		Clinic clinic = new Clinic();
		ClinicDAO clinicDAO  = new JDBCClinicDAO(connection);
		clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
		
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		
		String data = buffer.toString();
		JSONObject jsonObject = new JSONObject(data);
	
		String billsummary = jsonObject.getString("billsummary");
		String invoiceid = jsonObject.getString("invoiceid");
		String totalAmountx = jsonObject.getString("totalAmountx");
		String seq = jsonObject.getString("seq");
		
		String filterbydate= seq;
		ArrayList<Master>masterAssessmentList = new ArrayList<Master>();
		if(billsummary.equals("1")){
			masterAssessmentList = accountsDAO.getBillMasterAssessmentList(Integer.parseInt(invoiceid),filterbydate);
		}else{
			masterAssessmentList = accountsDAO.getMasterAssessmentList(Integer.parseInt(invoiceid),filterbydate);
		}
		
		StringBuffer stringBuffer = new StringBuffer();
		
		for (Master master : masterAssessmentList) {
			if(master.getName().equals("Ipd Registration Charge")){
				master.setName("Admission Charge");
			}
			stringBuffer.append("<tr class='totalbor'>");
			stringBuffer.append("<td><b>"+master.getName()+"</b></td>");
			stringBuffer.append("<td></td>");
			stringBuffer.append("<td></td>");
			stringBuffer.append("<td></td>");
			stringBuffer.append("<td></td>");
			stringBuffer.append("<td></td>");
			stringBuffer.append("<td style='text-align:right;color: #5cb85c;'>Sub Total "+Constants.getCurrency(loginInfo)+""+master.getCharge()+"</td>");
			stringBuffer.append("</tr>");
			for (Accounts accounts : master.getAssesmentList()) {
				stringBuffer.append("<tr>");
				stringBuffer.append("<td>");
					if(clinic.getInvoice_date()!=null){
						if(clinic.getInvoice_date().equals("0")){
							stringBuffer.append(""+accounts.getCommencing()+"");
						}	
					}
				stringBuffer.append("</td>");
				stringBuffer.append("<td>"+accounts.getTpcode()+"</td> ");
				stringBuffer.append("<td class='padletab'>");
					if(accounts.isDna()){
						stringBuffer.append(""+accounts.getAppointmentType()+"<span style='color:red'>(DNA)</span>");
					}else{
						stringBuffer.append(""+accounts.getAppointmentType()+"");
					}
				stringBuffer.append("</td>");
				stringBuffer.append("<td class='text-center'>");
				if(accounts.getPkginvissid()!=null){
					if(accounts.getPkginvissid().equals("0")){
						stringBuffer.append(""+accounts.getQuantity()+"");
					}
				}
				stringBuffer.append("</td>");
				stringBuffer.append("<td class=''>");
				if(accounts.getPkginvissid()!=null){
					if(accounts.getPkginvissid().equals("0")){
						stringBuffer.append(""+Constants.getCurrency(loginInfo)+""+accounts.getUnitcharge()+"");
					}
				}
				stringBuffer.append("</td>");
				
				stringBuffer.append("<td class=''>");
				if(accounts.getPkginvissid()!=null){
					if(accounts.getPkginvissid().equals("0")){
						stringBuffer.append(""+Constants.getCurrency(loginInfo)+""+accounts.getChargedisc()+"");
					}
				}
				stringBuffer.append("</td>");
				//stringBuffer.append("<%-- <td>"+accounts.getAppointmentType()+"</td> --%>");
				
				stringBuffer.append("<td class='text-right hidden'>");
				if(accounts.getPkginvissid()!=null){
					if(accounts.getPkginvissid().equals("0")){
						stringBuffer.append(""+Constants.getCurrency(loginInfo)+""+accounts.getCharges()+"");
					}
				}
				stringBuffer.append("</td>");
				stringBuffer.append("<td class='text-right'>");
				if(accounts.getPkginvissid()!=null){
					if(accounts.getPkginvissid().equals("0")){
						stringBuffer.append(""+Constants.getCurrency(loginInfo)+""+accounts.getChargeTotal()+"");
					}
				}
				stringBuffer.append("</td>");
				stringBuffer.append("</tr>");
			}
			
			
		}
		stringBuffer.append("<tr class='settopbac'>");
		stringBuffer.append("<td>Total</td>");
		stringBuffer.append("<td></td>");
		stringBuffer.append("<td></td>");
		stringBuffer.append("<td></td>");
		stringBuffer.append("<td></td>");
		stringBuffer.append("<td></td>");
		stringBuffer.append("<td class='text-right'>"+Constants.getCurrency(loginInfo)+""+totalAmountx+"</td>");
		stringBuffer.append("</tr>");
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("response1", stringBuffer.toString());
		String response1 = jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(response1);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}

//Akash 11 June 2018
public String getoutsourcedetails() throws Exception {
	Connection connection = null;
	try {
		if (!verifyLogin(request)) {
			return "login";
		}
		connection = Connection_provider.getconnection();
		InvestigationDAO investigationDAO = new JDBCInvestigationDAO(connection);
		Clinic clinic = new Clinic();
		ClinicDAO clinicDAO  = new JDBCClinicDAO(connection);
		clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
		
		StringBuilder buffer = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
		}
		
		String data = buffer.toString();
		JSONObject jsonObject = new JSONObject(data);
	
		String outsourceid = jsonObject.getString("outsourceid");
		String inparentid = jsonObject.getString("inparentid");
		
		Investigation investigation = investigationDAO.getInvestigationDetails(inparentid);
		String outsourcename = investigationDAO.getOutSourceName(outsourceid);
		StringBuffer buffer2 = new StringBuffer();
		StringBuffer buffer3 = new StringBuffer();
		StringBuffer buffer4 = new StringBuffer();
		StringBuffer buffer5 = new StringBuffer();
		StringBuffer buffer6 = new StringBuffer();
		
		buffer2.append("<label>OutSourced to :  </label>");
		buffer2.append("<label> "+outsourcename+"</label>");
		
		buffer3.append("<label>Sample Handover to :  </label>");
		if(investigation.getHandoverto()!=null){
			if(investigation.getHandoverto().equals("")){
				buffer5.append("<input type='hidden' id='oshandovertostatus' value='0'>");
				buffer3.append("<input type='text' id='oshandoverto' value='"+investigation.getHandoverto()+"' class='form-control' placeholder='Sample Handover To'>");
			}else{
				buffer5.append("<input type='hidden' id='oshandovertostatus' value='1'>");
				buffer3.append("<input type='text' readonly='readonly' value='"+investigation.getHandoverto()+"' class='form-control' id='oshandoverto' placeholder='Sample Handover To'>");
			}
		}else{
			buffer5.append("<input type='hidden' id='oshandovertostatus' value='0'>");
			buffer3.append("<input type='text' id='oshandoverto' class='form-control' placeholder='Sample Handover To'>");
		}
		
		buffer4.append("<label>Report Received</label>");
			if(investigation.getIsreturnOS()!=null){
				if(investigation.getIsreturnOS().equals("1")){
					buffer4.append("<select id='isreturnOS' disabled='disabled' class='form-control chosen'>");
					buffer4.append("<option value='0'>No</option> ");
					buffer4.append("<option value='1' selected='selected'>Yes</option>");
				}else{
					buffer4.append("<select id='isreturnOS'  class='form-control' >");
					buffer4.append("<option value='0'>No</option> ");
					buffer4.append("<option value='1' >Yes</option>");
				}
			}else{
				buffer4.append("<select id='isreturnOS' class='form-control chosen'>");
				buffer4.append("<option value='0'>No</option> ");
				buffer4.append("<option value='1'>Yes</option>");
			}
		buffer4.append("</select>");
		
		buffer5.append("<label>HandOver From:</label>");
		if(investigation.getIsreturnOS()!=null){
			if(investigation.getIsreturnOS().equals("1")){
				buffer5.append("<input type='hidden' id='oshandoverfromstatus' value='1'>");
				buffer5.append("<input type='text' readonly='readonly' value='"+investigation.getHandoverfrom()+"' class='form-control' id='oshandoverfrom' placeholder='Handover From'>");
			}else{
				if(investigation.getHandoverfrom()!=null){
						buffer5.append("<input type='hidden' id='oshandoverfromstatus' value='0'>");
						buffer5.append("<input type='text' id='oshandoverfrom' value='"+investigation.getHandoverfrom()+"' class='form-control' placeholder='Handover From'>");
				}else{
					buffer5.append("<input type='hidden' id='oshandoverfromstatus' value='0'>");
					buffer5.append("<input type='text' id='oshandoverfrom' class='form-control' placeholder='Handover From'>");
				}
			}
		}else{
			if(investigation.getHandoverfrom()!=null){
				buffer5.append("<input type='hidden' id='oshandoverfromstatus' value='0'>");
				buffer5.append("<input type='text' id='oshandoverfrom' class='form-control' placeholder='Handover From'>");
		}else{
			buffer5.append("<input type='hidden' id='oshandoverfromstatus' value='0'>");
			buffer5.append("<input type='text' id='oshandoverfrom' class='form-control' placeholder='Handover From'>");
		}
	}
		
		if(investigation.getIsreturnOS()!=null){
			if(investigation.getIsreturnOS().equals("1")){
				buffer6.append("<a href='#' class='btn btn-danger'>OK</a>");
			}else{
				buffer6.append("<a href='#' class='btn btn-danger' onclick='saveOutsourceDetails()'>Save</a>");
				
			}
		}else{
			buffer6.append("<a href='#' class='btn btn-danger' onclick='saveOutsourceDetails()'>Save</a>");
		}
		
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("outsourcediv", buffer2.toString());
		jsonobj.put("outsourcehandoverdiv", buffer3.toString());
		jsonobj.put("outsourceisretunddiv", buffer4.toString());
		jsonobj.put("outsourcereturnuserdiv", buffer5.toString());
		jsonobj.put("outsourcebtndiv", buffer6.toString());
		String response1 = jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(response1);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}

public String updateinvestoutsource() throws Exception{
	Connection connection = null;
	try {
		connection = Connection_provider.getconnection();
		InvestigationDAO investigationDAO = new JDBCInvestigationDAO(connection);
		StringBuffer buffer = new StringBuffer();
		String invid = request.getParameter("invid");
		String oshandoverto = request.getParameter("oshandoverto");
		String isreturnOS = request.getParameter("isreturnOS");
		String oshandoverfrom = request.getParameter("oshandoverfrom");
		String oshandovertostatus = request.getParameter("oshandovertostatus");
		if(isreturnOS==null){
			isreturnOS="1";
		}else if(isreturnOS.equals("")){
			isreturnOS="1";
		}else if(isreturnOS.equals("null")){
			isreturnOS="1";
		}
		
		String userid= loginInfo.getUserId();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String date = dateFormat.format(cal.getTime());
		int res = investigationDAO.updateInvesOutsourceNew(invid,userid,date,oshandoverto,oshandoverfrom,isreturnOS,oshandovertostatus);
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(buffer.toString());
	} catch (Exception e) {

		e.printStackTrace();
	} finally {
		connection.close();
	}

	return null;
}


public String priscnurseJson() throws Exception{
	if (!verifyLogin(request)) {
		return "login";
	}
	Connection connection= null;
	try{
		connection =Connection_provider.getconnection();
		StringBuilder buffer1 = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer1.append(line);
		}
		String data = buffer1.toString();
		JSONObject jsonObject = new JSONObject(data);
		
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		//MasterDAO masterDAO = new JDBCMasterDAO(connection);
		String parentid= jsonObject.getString("parentid");
		
		String date = "";
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		date = dateFormat.format(cal.getTime());
		
		//ArrayList<Master>medicineList = masterDAO.getMedicineList();
		StringBuffer buffer =new StringBuffer();
		
		//Priscription priscription = emrDAO.getPriscriptionParentData(Integer.parseInt(parentid));
		ArrayList<Priscription> arrayList= emrDAO.getPriscriptionChildData(parentid,0);
		int size = arrayList.size();
		if (size > 0){
			String totalid = arrayList.get(size - 1).getTotalid();
			String medid = arrayList.get(size - 1).getMedid();
			buffer.append("<input type='hidden' id='totalid' name='totalid' value='"+totalid+"'>");
			buffer.append("<input type='hidden' id='medid' name='medid' value='"+medid+"'>");
		}else{
			buffer.append("<input type='hidden' id='totalid' name='totalid' value='"+0+"'>");
			buffer.append("<input type='hidden' id='medid' name='medid' value='"+0+"'>");
		}
		int i=0;
		buffer.append("<input type='hidden' id='priscstatusparentid' name='parentid' value='"+parentid+"'>");
		for (Priscription priscription2 : arrayList) {
			buffer.append("<tr>");
			buffer.append("<input type='hidden' class='akashpriscrequest' value='"+priscription2.getId()+"'>");
			buffer.append("<input type='hidden' name='mdname"+priscription2.getId()+"' value='"+priscription2.getMdicinenametxt()+"'>");
			buffer.append("<input type='hidden' name='mdid"+priscription2.getId()+"' value='"+priscription2.getMdicinenameid()+"'>");
			buffer.append("<input type='hidden' name='childid"+priscription2.getId()+"' value='"+priscription2.getId()+"'>");
			buffer.append("<td><input type='checkbox' class='akashcase' value='"+priscription2.getId()+"' class='form-control' /></td>");
			buffer.append("<td>"+(++i)+"</td>");
			buffer.append("<td>"+priscription2.getMdicinenametxt()+"</td>");
			if(priscription2.getDosage()!=null){
				buffer.append("<td>"+priscription2.getDosage()+"</td>");
			}else{
				buffer.append("<td></td>");
			}
			buffer.append("<td>"+priscription2.getPriscdays()+"</td>");
			buffer.append("<td>"+priscription2.getPriscqty()+"</td>");
			buffer.append("<td><input type='number' tabindex="+i+" class='form-control' id='mdrequestqty"+priscription2.getId()+"' name='mdrequestqty"+priscription2.getId()+"'></td>");
			buffer.append("</tr>");
		}
		
		String responsej=	buffer.toString();
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("responsej", responsej);
		String response1 = jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(response1);
	}
	catch(Exception e){
		e.printStackTrace();
	}
	return null;
}


public String addnewpriscbynurse() throws Exception{
	if (!verifyLogin(request)) {
		return "login";
	}
	Connection connection= null;
	try{
		connection =Connection_provider.getconnection();
		StringBuilder buffer1 = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer1.append(line);
		}
		String data = buffer1.toString();
		JSONObject jsonObject = new JSONObject(data);
		
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		String parentid= jsonObject.getString("parentid");
		String newmedicineid = jsonObject.getString("newmedicineid");
		String date = "";
		
		String med= emrDAO.getMedicicneName(newmedicineid);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		date = dateFormat.format(cal.getTime());
		
		StringBuffer buffer =new StringBuffer();
		Priscription priscription = emrDAO.getPriscriptionParentData(Integer.parseInt(parentid));
		priscription.setDate(date);
		priscription.setUserid(loginInfo.getUserId());
		priscription.setMdicinenameid(newmedicineid);
		priscription.setMdicinenametxt(med);
		priscription.setParentid(parentid);
		int res = emrDAO.saveNewMedBynurse(priscription);
		
		String responsej=	buffer.toString();
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("responsej", responsej);
		String response1 = jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(response1);
	}
	catch(Exception e){
		e.printStackTrace();
	}
	return null;
}


public String admissinpriscdata()throws Exception{
	 
	 Connection connection = null;
	 
	 try{
		 connection = Connection_provider.getconnection();
		 IpdDAO ipdDAO = new JDBCIpdDAO(connection);
		 EmrDAO emrDAO = new JDBCEmrDAO(connection);
		 String sessionadmissionid = (String)session.getAttribute("sessionadmissionid");
		 
		 ArrayList<Priscription>dischargePriscList = ipdDAO.getAdmissionPrescList(sessionadmissionid);
		
		ArrayList<Master> dosageList = emrDAO.getDosageList();
		
		int size = dischargePriscList.size();
		String totalchildmedids ="0";
		if (size > 0) {
			totalchildmedids = dischargePriscList.get(size - 1).getTotalchildmedids();
		}
		
		int i = 0;
		StringBuffer str = new StringBuffer();
		for(Priscription priscription : dischargePriscList){
			str.append("<tr>");
			str.append("<td><input type='number' class='form-control' name='dicpriscmed"+priscription.getId()+"' value='"+priscription.getDispriscsrno()+"'></td>");
			str.append("<td>"+priscription.getMdicinenametxt()+"</td/>");
			//Akash 05 June 2018
			str.append("<td>");
			str.append("<select id='discpriscdose"+priscription.getId()+"' name='discpriscdose"+priscription.getId()+"' class='form-control chosen-select'>");
			for (Master master : dosageList) {
				if(priscription.getPriscdose()!=null){
					if(master.getName()!=null){
						if(master.getName().equals(priscription.getPriscdose())){
							str.append("<option value='"+master.getName()+"' selected='selected'>"+master.getName()+"</option>");
						}else{
							str.append("<option value='"+master.getName()+"'>"+master.getName()+"</option>");
						}
					}else{
						str.append("<option value='"+master.getName()+"'>"+master.getName()+"</option>");
					}
				}else{
					str.append("<option value='"+master.getName()+"'>"+master.getName()+"</option>");
				}
			}
			str.append("</select>");
			str.append("</td>");
			//str.append("<td>"+priscription.getPriscdose()+"</td/>");
			/*str.append("<td>"+priscription.getPriscdays()+" "+priscription.getPriscdurationtype()+"</td/>");*/
			str.append("<td><input type='number' class='form-control' name='dicpriscdays"+priscription.getId()+"' value='"+priscription.getPriscdays()+"'></td>");
			str.append("<td>"+priscription.getDosenotes()+"</td/>");
			str.append("<td>"+priscription.getStrengthnew()+"</td/>");
			str.append("<td>"+priscription.getPriscindivisualremark()+"</td/>");
			str.append("<td><a onclick='removeMedicineDisc(this,"+priscription.getId()+")' ><i class='fa fa-trash'></i></a></td>");
			str.append("</tr>");
			i++;
		}
		str.append("<input type='hidden' name='totalchildmedids' value='"+totalchildmedids+"'>");
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+str.toString()+"");
		 
	 }catch (Exception e) {
		e.printStackTrace();
	}
	 finally{
			connection.close();
		}
	 return null;
}

public String discdata()throws Exception{
	 
	 Connection connection = null;
	 
	 try{
		 connection = Connection_provider.getconnection();
		 IpdDAO ipdDAO = new JDBCIpdDAO(connection);
		 EmrDAO emrDAO = new JDBCEmrDAO(connection);
		 String sessionadmissionid = (String)session.getAttribute("sessionadmissionid");
		 String treatmentgivensatus=DateTimeUtils.isNull(request.getParameter("treatmentgivensatus"));
		 ArrayList<Priscription>dischargePriscList = new ArrayList<Priscription>();
		if(treatmentgivensatus.equals("1")){
			dischargePriscList=ipdDAO.getTreatmentGivenDischargePrescList(sessionadmissionid);
		}else {
			dischargePriscList=ipdDAO.getDischargePrescList(sessionadmissionid);
		}
		
		ArrayList<Master> dosageList = emrDAO.getDosageList();
		
		int size = dischargePriscList.size();
		String totalchildmedids ="0";
		if (size > 0) {
			totalchildmedids = dischargePriscList.get(size - 1).getTotalchildmedids();
		}
		
		int i = 0;
		StringBuffer str = new StringBuffer();
		for(Priscription priscription : dischargePriscList){
			str.append("<tr>");
			str.append("<td><input type='number' class='form-control' name='dicpriscmed"+priscription.getId()+"' value='"+priscription.getDispriscsrno()+"'></td>");
			str.append("<td>"+priscription.getMdicinenametxt()+"</td/>");
			//Akash 05 June 2018
			str.append("<td>");
			str.append("<select id='discpriscdose"+priscription.getId()+"' name='discpriscdose"+priscription.getId()+"' class='form-control chosen-select'>");
			for (Master master : dosageList) {
				if(priscription.getPriscdose()!=null){
					if(master.getName()!=null){
						if(master.getName().equals(priscription.getPriscdose())){
							str.append("<option value='"+master.getName()+"' selected='selected'>"+master.getName()+"</option>");
						}else{
							str.append("<option value='"+master.getName()+"'>"+master.getName()+"</option>");
						}
					}else{
						str.append("<option value='"+master.getName()+"'>"+master.getName()+"</option>");
					}
				}else{
					str.append("<option value='"+master.getName()+"'>"+master.getName()+"</option>");
				}
			}
			str.append("</select>");
			str.append("</td>");
			//str.append("<td>"+priscription.getPriscdose()+"</td/>");
			/*str.append("<td>"+priscription.getPriscdays()+" "+priscription.getPriscdurationtype()+"</td/>");*/
			str.append("<td><input type='number' class='form-control' name='dicpriscdays"+priscription.getId()+"' value='"+priscription.getPriscdays()+"'></td>");
			
			str.append("<td>"+priscription.getDosenotes()+"</td/>");
			str.append("<td>"+priscription.getStrength()+"</td/>");
			if(!treatmentgivensatus.equals("1")){
				str.append("<td><a onclick='removeMedicineDisc(this,"+priscription.getId()+")' ><i class='fa fa-trash'></i></a></td>");
			}else{
				str.append("<td><a onclick='removeMedicineDisc1(this,"+priscription.getId()+")' ><i class='fa fa-trash'></i></a></td>");
			}
			
			str.append("</tr>");
			i++;
		}
		str.append("<input type='hidden' name='totalchildmedids' value='"+totalchildmedids+"'>");
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+str.toString()+"");
		 
	 }catch (Exception e) {
		e.printStackTrace();
	}
	 finally{
			connection.close();
		}
	 return null;
}

//Akash 12 July 2018

public String chargeopdot() throws Exception{
	
	String apmtType = request.getParameter("apmtType");
	String clientId = request.getParameter("clientId");
	Connection connection = null;
	try{
		connection = Connection_provider.getconnection();
		NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
		CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(connection);
		double charge = notAvailableSlotDAO.getCharge(apmtType);
		double opdregcharge =0;
		//Akash 10 July 2018 /to set registration charge
		boolean isopd = completeAptmDAO.isclientIdInApmtBefore(clientId);
		boolean isipd=completeAptmDAO.isOPDFirstCharge(clientId, "");
		//new condition for opd reg charge
		/*boolean isundernewcond=false;
		if(todayspatient&&!flag){
			isundernewcond=true;
		}
		if(!flag||todayspatient){
			opdregcharge =  completeAptmDAO.getOpdRegCharge();
		}*/
		if(isopd){
			opdregcharge =  completeAptmDAO.getOpdRegCharge();
		}
		if(isipd){
			opdregcharge = 0;
		}
		
		String data = charge +"~"+opdregcharge+"~"+loginInfo.isOpd_tp_zero_invoice();
		
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+data+""); 
		
	}catch (Exception e) {
		// TODO: handle exception
	}finally{
		
		connection.close();
	}
	
	return null;
}

//Akash 21 Julyn2018
public String getvendorstateforlongpo() throws Exception{
	  Connection connection= null;
		 try {
			String procurementid= request.getParameter("procurementid");
			connection= Connection_provider.getconnection();
			ProcurementDAO procurementDAO = new JDBCProcurementDAO(connection);
			int result= procurementDAO.getvendorstateforlongpo(procurementid);
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+result+"");

		} catch (Exception e) {
			e.printStackTrace();
		}
		 return null;
}

public String getstate() throws Exception{

	Connection connection = null;
	try {
		connection = Connection_provider.getconnection();
		PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
		Priscription priscription = pharmacyDAO.getPharacyUsrLInfo(loginInfo.getUserId());
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(priscription.getState());
	} catch (Exception e) {

		e.printStackTrace();
	}
	finally {
		connection.close();
	}

	return null;
}

public String getclientweight() throws Exception{

	Connection connection = null;
	try {
		connection = Connection_provider.getconnection();
		ClientDAO clientDAO = new JDBCClientDAO(connection);
		String clientid = request.getParameter("clientid");
		
		String weight = clientDAO.getClientWeight(clientid);
		
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+weight+"");
	} catch (Exception e) {

		e.printStackTrace();
	}
	finally {
		connection.close();
	}

	return null;
}

public String addtonursereturn() throws Exception{
    
    Connection connection=null;
    try {
     connection=Connection_provider.getconnection();
     InventoryProductDAO inventoryProductDAO=new JDBCInventoryProductDAO(connection);
     PharmacyDAO pharmacyDAO= new JDBCPharmacyDAO(connection);
     String chargeid=request.getParameter("chargeid");
     String tcount=request.getParameter("count");
     String qty=request.getParameter("qty");
     Priscription priscription= pharmacyDAO.getMedicineChargesbyid(Integer.parseInt(chargeid),0);
     Product product=inventoryProductDAO.getProduct(priscription.getProduct_id());
     String medicine_shedule= inventoryProductDAO.getMedicineShedule(product.getCatalogueid());
     chargeid = DateTimeUtils.numberCheck(chargeid);
     int returnqty= pharmacyDAO.getReturnQtyofCharge(chargeid); 
     int returnqty0 = pharmacyDAO.getNurseReturnQty(Integer.parseInt(chargeid));
     returnqty = returnqty + returnqty0;
     if(product.getId()>0){
      
     String ex=product.getExpiry_date();
     String expiry=DateTimeUtils.converToMMMYYYYforExp(ex);
     int count=Integer.parseInt(tcount);
     String color="#777";
     if(medicine_shedule.equals("Regular")) {
         color="#777";
     } else if(medicine_shedule.equals("Narcotics")){
         color="#e05d6f";
     } else if(medicine_shedule.equals("H1")){
         color="#e69522";
     }else if(medicine_shedule.equals("High Risk Medicine")){
    	 color="#9381cc";
     }else if(medicine_shedule.equals("Vaccination")){
    	 color="#e0acdafc";
     }
     returnqty =priscription.getSaleqty()- returnqty; 
     int sr=count+1;
     StringBuffer buffer=new StringBuffer();
     buffer.append("<td>"+sr+"</td>");
     buffer.append("<td style=color:"+color+" '>"+product.getProduct_name()+" <input type='hidden' class='pclass' value='"+count+"' /> <input type='hidden' name='medicines["+count+"].id' id='id"+count+"'  value='"+chargeid+"' /> <input type='hidden' name='medicines["+count+"].product_id' id='product_id"+count+"' value='"+product.getId()+"' /> </td>");
     buffer.append("<td id='labelreq"+count+"'>"+priscription.getSaleqty()+"</td> <input type='hidden' name='medicines["+count+"].tempsale' value='"+returnqty+"' id='tempsale"+count+"' /> <input type='hidden' name='medicines["+count+"].productname' value='"+product.getProduct_name()+"' />");
    /* buffer.append("<td> "+product.getHsnno()+" </td>");*/
     /*buffer.append("<td>"+returnqty+"/"+product.getBatch_no()+"/"+expiry+" <input type='hidden' id='pur_price"+count+"' value='"+product.getPurchase_price()+"' /> <input type='hidden' value='"+product.getPack()+"' id='pack"+count+"'/> <input type='hidden' name='medicines["+count+"].billno' id='billno"+count+"' value='"+priscription.getBillno()+"' /> </td>");*/
     buffer.append("<input type='hidden' id='pur_price"+count+"' value='"+product.getPurchase_price()+"' /> <input type='hidden' value='"+product.getPack()+"' id='pack"+count+"'/> <input type='hidden' name='medicines["+count+"].billno' id='billno"+count+"' value='"+priscription.getBillno()+"' /> ");
     
     buffer.append("<td>"+returnqty+"</td>");
     buffer.append("<td>"+product.getBatch_no()+"</td>");
     buffer.append("<td>"+expiry+"</td>");
     buffer.append("<td><input type='number' name='medicines["+count+"].returnqty' onchange='validatePharmacyNurseBill()'  id='returnqty"+count+"' value='"+qty+"' class='form-control' style='height: 20px !important;background-color: rgba(255, 225, 225, 0.59);'></td>");
     if(loginInfo.isPurchase_edit_pharmacy()){
   	  	buffer.append("<td class='hidden'><input type='number' value='"+product.getSale_price()+"' name='medicines["+count+"].sale_price'  id='mrp"+count+"' class='form-control' style='height: 20px !important;text-align: right;background-color: rgba(255, 225, 225, 0.59);' readonly/> <input type='hidden' value='"+product.getSale_price()+"' id='sale_price"+count+"' /> </td>");
     }else{
   	  	buffer.append("<td class='hidden'><input type='number' value='"+product.getSale_price()+"' name='medicines["+count+"].sale_price'  id='mrp"+count+"' class='form-control' style='height: 20px !important;text-align: right;background-color: rgba(255, 225, 225, 0.59);' readonly/> <input type='hidden' value='"+product.getSale_price()+"' id='sale_price"+count+"' /> </td>");  
     }
     buffer.append("<td style='text-align: center;text-transform: uppercase;' class='hidden'>"+product.getShelf()+" <input type='hidden' name='medicines["+count+"].vat' id='vat"+count+"' value='"+product.getVat()+"'> </td>");
     buffer.append("<td  class='text-right hidden'>Rs.<label id='totalmrp"+count+"'>00.00</label> ");
     buffer.append("<input type='hidden' id='prodd"+count+"' value='"+product.getStock()+"'/>");
     buffer.append("<input type='hidden' id='discper"+count+"' value='"+priscription.getDiscount()+"'/>");
     buffer.append("</td>");
     int stock=Integer.parseInt(product.getStock());
     if(stock>100){
      buffer.append("<td class='text-center' id='tdbutton"+count+"'><a href='#' onclick=deleteRowtempNurse(this)><i class='fa fa-minus-circle' style='color:#c50404;font-size: 17px;padding-top: 2px;' aria-hidden='true'></i></a></td>");
     } else {
      buffer.append("<td class='text-center'><a href='#' onclick=deleteRowtempNurse(this)><i class='fa fa-minus-circle' style='color:#c50404;font-size: 17px;padding-top: 2px;' aria-hidden='true'></i></a></td><td class='text-center hidden'><a href='#' onclick='requestStock("+product.getId()+","+count+")' title='Request Stock'><i class='fa fa-cart-plus' aria-hidden='true' style='font-size: 17px;padding-top: 2px;'></i></a></td>");
     }
     buffer.append("<input type='hidden' id='totalrefundrs"+count+"' name='medicines["+count+"].totalrefundrs'  />");
     buffer.append("<input type='hidden' id='tmedicineid"+count+"' name='medicines["+count+"].mdicinenameid' value='"+product.getMedicinenameid()+"' />");
     
     response.setContentType("text/html");
     response.setHeader("Cache-Control", "no-cache");
     response.getWriter().write(""+buffer.toString()+""); 
     
     } else {
      response.setContentType("text/html");
      response.setHeader("Cache-Control", "no-cache");
      response.getWriter().write("0"); 
     }
     
    } catch (Exception e) {

     e.printStackTrace();
    }
    finally {
     connection.close();
    }
    
    return null;
   }
public String deletediscount(){
	Connection connection= null;
	String id= request.getParameter("id");
	String reason= request.getParameter("reason");
	String invid=request.getParameter("invid");
	String userid= loginInfo.getUserId();
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar cal = Calendar.getInstance();
    String date = dateFormat.format(cal.getTime());  
	
	try {
		connection = Connection_provider.getconnection();
		AppointmentDAO appointmentDAO= new JDBCAppointmentDAO(connection);
		appointmentDAO.deletediscountfromdashboard(id, userid, date, reason,invid);
		
		 response.setContentType("text/html");
	      response.setHeader("Cache-Control", "no-cache");
	      response.getWriter().write("0"); 
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}

public String truncate() throws Exception{
	
	Connection connection = null;
	try {

		connection = Connection_provider.getconnection();
		IpdDAO ipdDAO = new JDBCIpdDAO(connection);
		
		CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(connection);

		int result = completeAptmDAO.deleteComplteApmt(loginInfo.getId());
	}
	catch(Exception e){
		e.printStackTrace();
	}finally{
		
		connection.close();
	}
	return null;
}
public String getpriscmedicinelist(){

	if (!verifyLogin(request)) {
		return "login";
	}
	Connection connection= null;
	try{
		connection =Connection_provider.getconnection();
		StringBuilder buffer1 = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer1.append(line);
		}
		String data = buffer1.toString();
		JSONObject jsonObject = new JSONObject(data);
		
		MasterDAO masterDAO= new JDBCMasterDAO(connection);
		InventoryProductDAO inventoryProductDAO= new JDBCInventoryProductDAO(connection);
		PrescriptionMasterDAO prescriptionMasterDAO= new JDBCPrescriptionMasterDAO(connection);
		
		//add to medicine master
		ArrayList<Master>medicineList = masterDAO.getMedicineList();
		StringBuffer buffer =new StringBuffer();
		
		if(loginInfo.isAdd_medicine() || loginInfo.getUserType()==2){
			buffer.append("<a title='Add New Medicine' href='#' type='button' class='btn btn-sm btn-primary' onclick=openhiddendiv('hiddendiv') style='margin-right: 5px;'><i class='fa fa-pencil' aria-hidden='true'></i></a>");
		} else{
			buffer.append("<a title='Add New Medicine' href='#' type='button' class='btn btn-sm btn-primary'  style='margin-right: 5px;'><i class='fa fa-pencil' aria-hidden='true'></i></a>");
		} 
		buffer.append("<select class='form-control chosen' id='mdicinename' name='mdicinename' onchange='getDoseNote(this.value)' > ");
		buffer.append("<option value='0'>Select Medicine</option>");
		for(Master master:medicineList){
			buffer.append("<option value='"+master.getId()+"'> "+master.getGenericname()+" </option>");
		}
		buffer.append("</select>");
		//buffer.append("<a href='#' type='button' class='btn btn-sm btn-primary' onclick=openhiddendiv('hiddendiv') style='margin-left: 5px;'><i  class='fa fa-plus'></i></a>");
		
		if(loginInfo.isAdd_medicine() || loginInfo.getUserType()==2){
			buffer.append("&nbsp;<div><a href='#'><i class='fa fa-plus' style='width:10px;' onclick=openBlankPopup('addPrescriptiondetails')></i></a></div>");
		} else{
			buffer.append("&nbsp;<div><a href='#'><i class='fa fa-plus' style='width:10px;' ></i></a></div>");
		} 
		
		
		String responsej=	buffer.toString();
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("responsej", responsej);
			
		
		String response1 = jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(response1);

		
		
	}
	catch(Exception e){
		e.printStackTrace();
	}
	return null;
}


public String delete() throws Exception{
	if(!verifyLogin(request)){
		return "login";
	}
	String selectedid = request.getParameter("selectedid");
	String cancelApmtNote = request.getParameter("cancelApmtNote");
	Connection connection = null;
	//current date and time in dd/mm/yyyy
			String currentDate = DateTimeUtils.getDateinSimpleFormate(new Date());
			String datetemp[] = currentDate.split(" ");
			String temp1[] = datetemp[0].split("-");
			String date = temp1[0] + "/" + temp1[1] + "/" + temp1[2];
			String time = (datetemp[2]+" "+datetemp[3]);
			
			

	try{
		connection = Connection_provider.getconnection();
		NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
		ClinicDAO clinicDAO=new JDBCClinicDAO(connection);
		
		String opendb = (String)session.getAttribute("openedb");
		int status = notAvailableSlotDAO.getStatus(selectedid,opendb);
/*		if(status==0){
			int treatmeId = notAvailableSlotDAO.getSelecedTreatmentEpisodeId(Integer.parseInt(selectedid));
			int usedSession = notAvailableSlotDAO.getUsedSession(treatmeId,selectedid);
			ArrayList<NotAvailableSlot>usedSessionList = new ArrayList<NotAvailableSlot>();
			usedSessionList = notAvailableSlotDAO.getUsedSessionList(treatmeId,usedSession);
			
			
			for(NotAvailableSlot n:usedSessionList){
				int updateAllPrevious = notAvailableSlotDAO.updateAllPrevious(n.getId(),treatmeId);
			}
			int updatesession = notAvailableSlotDAO.updateSessions(treatmeId);
		}*/
		NotAvailableSlot n = notAvailableSlotDAO.getApmtDetailsForLog(Integer.parseInt(selectedid));
		
		String commencingTemp = n.getCommencing();
		
		String apmtstatus = "Cancelled";
		
		
		
		  //send sms
	    ClientDAO clientDAO = new JDBCClientDAO(connection);
	    NotAvailableSlot notAvailableSlot = notAvailableSlotDAO.getApmtDetailsForLog(Integer.parseInt(selectedid));
	    UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
	    Client client = clientDAO.getClientDetails(notAvailableSlot.getClientId());
	    UserProfile userProfile  = userProfileDAO.getUserprofileDetails(notAvailableSlot.getDiaryUserId());
	   
	   
		 
	    int otid = notAvailableSlotDAO.checkotAppointment(selectedid);
	    if(otid>0){
	    	int result = notAvailableSlotDAO.deleteOtApmt(otid);
	    }else{
	    	//if(opendb.equals("opd")){
	    		int logsave = notAvailableSlotDAO.saveCancelApmtInLog(Integer.parseInt(selectedid),DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),time,loginInfo.getUserId(),n.getClientId(),commencingTemp,n.getSTime(),apmtstatus,cancelApmtNote,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),n);
		    	
		    	int result = notAvailableSlotDAO.DeleteBlockedSlot(selectedid,opendb);
		    	 //String message = AllTemplateAction.getDeletedAppointmentSMSText(notAvailableSlot.getClientId(), Integer.parseInt(selectedid), connection, loginInfo);
		 	    String message = "Appointment has cancelled";
		 	    if(loginInfo.getCountry().equals("India")){
		 	    	SmsService s = new SmsService();
		 	    	if(status==0){
		 	    	 s.sendSms(message, client.getMobNo(), loginInfo, new EmailLetterLog());
		 	    	boolean isSMSActive=clinicDAO.isSmsActive(loginInfo.getId());
		 			if(isSMSActive){
		 				s.sendSms(message, userProfile.getMobile(), loginInfo, new EmailLetterLog());
		 			}
		 	    	  
		 	    	}
		 	    }
	    	//}
	    	
	    }
		
		
		boolean isapmtexist = notAvailableSlotDAO.checkApmtExist(n.getClientId());
		if(isapmtexist){
			int sts = 1;
			int updp = notAvailableSlotDAO.updateNewPtsStatus(n.getClientId(),sts);
		}else{
			int sts = 0;
			int updp = notAvailableSlotDAO.updateNewPtsStatus(n.getClientId(),sts);
		}
		
	}catch(Exception e){
		//log.debug("sendsms"+e.getMessage());
		//e.printStackTrace();
	}finally{
		
		connection.close();
	}
	
	
	return null;

}

public String saveopddiagnosis(){
	try {
	Connection connection=Connection_provider.getconnection();	
	String opdid=request.getParameter("opdid");
	String diagnosisid=request.getParameter("diagnosisid");
	NotAvailableSlotDAO notAvailableSlotDAO= new JDBCNotAvailableSlotDAO(connection);
	int result=notAvailableSlotDAO.saveDiagnosisOpd(opdid, diagnosisid);
	response.setContentType("text/html");
    response.setHeader("Cache-Control", "no-cache");
    response.getWriter().write("0"); 
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}


public String getbmidata(){
	try {
		Connection connection=Connection_provider.getconnection();
		String clientid=request.getParameter("clientid");
		String appointmentid=request.getParameter("apmtid");
		ClientDAO clientDAO= new JDBCClientDAO(connection);
		Client client=clientDAO.getBMIData(clientid,appointmentid);
		NotAvailableSlotDAO notAvailableSlotDAO=new JDBCNotAvailableSlotDAO(connection);
		NotAvailableSlot notAvailableSlot=notAvailableSlotDAO.getAvailableSlotdata(Integer.parseInt(appointmentid));
		Client client1=clientDAO.getClientDetails(clientid);
		DiaryManagementDAO diaryManagementDAO=new JDBCDiaryManagentDAO(connection);
		UserProfile userProfile=diaryManagementDAO.getUserProfile(notAvailableSlot.getDiaryUserId());
		String responnse="";
		String spliter="$$~$$";
		responnse=client.getHeight()+spliter+client.getWeight()+spliter+client.getBmi()+spliter+client.getPulse()+spliter+client.getSysbp();
		responnse=responnse+spliter+client.getDiabp()+spliter+client.getSugarfasting()+spliter+client.getPostmeal()+spliter+client.getTemprature()+spliter+client.getSpo()+spliter+client.getHead_cir();
		responnse=responnse+spliter+client1.getFullname();
		responnse=responnse+spliter+notAvailableSlot.getsTime();
		responnse=responnse+spliter+notAvailableSlot.getDuration();
		responnse=responnse+spliter+client1.getWhopay();
		responnse=responnse+spliter+userProfile.getFullname();
		responnse=responnse+spliter+userProfile.getDiciplineName();
		responnse=responnse+spliter+notAvailableSlot.getApmttypetext();
		responnse=responnse+spliter+notAvailableSlot.getCommencing();
		responnse=responnse+spliter+notAvailableSlot.isDna();
		responnse=responnse+spliter+notAvailableSlot.getOpdpmnt();
		responnse=responnse+spliter+notAvailableSlot.getDiaryUserId();
		responnse=responnse+spliter+notAvailableSlot.getProcedure();
		response.setContentType("text/html");
	    response.setHeader("Cache-Control", "no-cache");
	    response.getWriter().write(""+responnse); 
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return null;
}

public String getchildgrowthdataofmonth() throws Exception {
	Connection connection = null;
	try {
		connection = Connection_provider.getconnection();
		ClientDAO clientDAO = new JDBCClientDAO(connection);
		
		String clientId = request.getParameter("clientId");
		String val = request.getParameter("val");
		String month = request.getParameter("month");
		Client client = clientDAO.getChildGrowthData(clientId,val,month);
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+client.getCgddata()+"~~~"+val);
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		connection.close();
	}
	return null;
}

public String savebmi() throws Exception {
	
	Connection connection=null; 
	try {
		connection=Connection_provider.getconnection();
//		/ var url="savebmiNotAvailableSlot?height="+height+"&weight="+weight+"&bmi="+bmi+"&pulse="+pulse+"&sysbp="+sysbp+"&diabp="+diabp+"";
		ClientDAO clientDAO=new JDBCClientDAO(connection);
		String height=request.getParameter("height");
		String weight=request.getParameter("weight");
		String bmi=request.getParameter("bmi");
		String pulse=request.getParameter("pulse");
		String sysbp=request.getParameter("sysbp");
		String diabp=request.getParameter("diabp");
		String patientId=request.getParameter("patientId");		
		String sugarfasting=request.getParameter("sugarfasting");
		String postmeal=request.getParameter("postmeal");	
		String temprature=request.getParameter("temprature");
		String spo=request.getParameter("spo");
		String head_cir=request.getParameter("bsa");
		String appointmentid=request.getParameter("apmtid");
		Client client=new Client();
		client.setHeight(height);
		client.setWeight(weight);
		client.setBmi(bmi);
		client.setPulse(pulse);
		client.setSysbp(sysbp);
		client.setDiabp(diabp);
		client.setClientid(patientId);
		client.setSugarfasting(sugarfasting);
		client.setPostmeal(postmeal);
		client.setTemprature(temprature);
		client.setSpo(spo);
		client.setHead_cir(head_cir);
		client.setAppointmentid(appointmentid);
		int result=clientDAO.saveBMIPatient(client);
		
		Client client2 = clientDAO.getClientDetails(patientId);
		int ageinmonth = DateTimeUtils.getmonthsfromdob(client2.getDob());
		if(ageinmonth<=60 && ageinmonth>=0){
			if(height!=null){
				if(!height.equals("") && !height.equals("0")){
					Client client4 = new Client();
					client4.setHeightdata(height);
					client4.setWeightdata(weight);
					client4.setBmidata(bmi);
					client4.setHeadcircumferncedata(head_cir);
					client4.setClientid(patientId);
					client4.setMonth(""+ageinmonth);
					client4.setUserid(loginInfo.getUserId());
					String datetime = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
					client4.setDate(datetime);
					int id = clientDAO.check_child_growth_data(patientId, ""+ageinmonth);
					int res1 = 0;
					if (id > 0) {
						client4.setId(id);
						// update
						res1 = clientDAO.updateChildGrowthData(client4, "height");
					} else {
						// insert
						res1 = clientDAO.saveChildGrowthData(client4, "height");
					}
				}
			}
			if(weight!=null){
				if(!weight.equals("") && !weight.equals("0")){
					Client client4 = new Client();
					client4.setHeightdata(height);
					client4.setWeightdata(weight);
					client4.setBmidata(bmi);
					client4.setHeadcircumferncedata(head_cir);
					client4.setClientid(patientId);
					client4.setMonth(""+ageinmonth);
					client4.setUserid(loginInfo.getUserId());
					String datetime = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
					client4.setDate(datetime);
					int id = clientDAO.check_child_growth_data(patientId, ""+ageinmonth);
					int res1 = 0;
					if (id > 0) {
						client4.setId(id);
						// update
						res1 = clientDAO.updateChildGrowthData(client4, "weight");
					} else {
						// insert
						res1 = clientDAO.saveChildGrowthData(client4, "weight");
					}
				}
			}
			if(bmi!=null){
				if(!bmi.equals("") && !bmi.equals("0")){
					Client client4 = new Client();
					client4.setHeightdata(height);
					client4.setWeightdata(weight);
					client4.setBmidata(bmi);
					client4.setHeadcircumferncedata(head_cir);
					client4.setClientid(patientId);
					client4.setMonth(""+ageinmonth);
					client4.setUserid(loginInfo.getUserId());
					String datetime = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
					client4.setDate(datetime);
					int id = clientDAO.check_child_growth_data(patientId, ""+ageinmonth);
					int res1 = 0;
					if (id > 0) {
						client4.setId(id);
						// update
						res1 = clientDAO.updateChildGrowthData(client4, "bmi");
					} else {
						// insert
						res1 = clientDAO.saveChildGrowthData(client4, "bmi");
					}
				}
			}
			if(head_cir!=null){
				if(!head_cir.equals("") && !head_cir.equals("0")){
					Client client4 = new Client();
					client4.setHeightdata(height);
					client4.setWeightdata(weight);
					client4.setBmidata(bmi);
					client4.setHeadcircumferncedata(head_cir);
					client4.setClientid(patientId);
					client4.setMonth(""+ageinmonth);
					client4.setUserid(loginInfo.getUserId());
					String datetime = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
					client4.setDate(datetime);
					int id = clientDAO.check_child_growth_data(patientId, ""+ageinmonth);
					int res1 = 0;
					if (id > 0) {
						client4.setId(id);
						// update
						res1 = clientDAO.updateChildGrowthData(client4, "headcircumfernce");
					} else {
						// insert
						res1 = clientDAO.saveChildGrowthData(client4, "headcircumfernce");
					}
				}
			}
		}
		
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""); 
		
	} catch (Exception e) {

		e.printStackTrace();
	}finally{
		
		connection.close();
	}
	 
	 return null;
 }


public String priscrequest() throws Exception{
	if (!verifyLogin(request)) {
		return "login";
	}
	Connection connection= null;
	try{
		connection =Connection_provider.getconnection();
		StringBuilder buffer1 = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer1.append(line);
		}
		String data = buffer1.toString();
		JSONObject jsonObject = new JSONObject(data);
		
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		//MasterDAO masterDAO = new JDBCMasterDAO(connection);
		String parentid= jsonObject.getString("parentid");
		
		String date = "";
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		date = dateFormat.format(cal.getTime());
		
		//ArrayList<Master>medicineList = masterDAO.getMedicineList();
		StringBuffer buffer =new StringBuffer();
		
		//Priscription priscription = emrDAO.getPriscriptionParentData(Integer.parseInt(parentid));
		ArrayList<Priscription> arrayList= emrDAO.getPriscriptionChildData(parentid,0);
		int size = arrayList.size();
		if (size > 0){
			String totalid = arrayList.get(size - 1).getTotalid();
			String medid = arrayList.get(size - 1).getMedid();
			buffer.append("<input type='hidden' id='totalid_request' name='totalid' value='"+totalid+"'>");
			buffer.append("<input type='hidden' id='medid_request' name='medid' value='"+medid+"'>");
		}else{
			buffer.append("<input type='hidden' id='totalid_request' name='totalid' value='"+0+"'>");
			buffer.append("<input type='hidden' id='medid_request' name='medid' value='"+0+"'>");
		}
		int i=0;
		buffer.append("<input type='hidden' id='priscstatusparentid_request' name='parentid' value='"+parentid+"'>");
		for (Priscription priscription2 : arrayList) {
			buffer.append("<tr>");
			buffer.append("<input type='hidden' class='akashpriscrequest_request' value='"+priscription2.getId()+"'>");
			buffer.append("<input type='hidden' name='mdname"+priscription2.getId()+"' value='"+priscription2.getMdicinenametxt()+"'>");
			buffer.append("<input type='hidden' name='mdid"+priscription2.getId()+"' value='"+priscription2.getMdicinenameid()+"'>");
			buffer.append("<input type='hidden' name='childid"+priscription2.getId()+"' value='"+priscription2.getId()+"'>");
			if(priscription2.getDeliver_statuss()==0){
				buffer.append("<td><input type='checkbox' class='akashcase_request' value='"+priscription2.getId()+"'  /></td>");
			}else{
				buffer.append("<td><input type='checkbox' disabled='disabled' checked='true' value='"+priscription2.getId()+"'  /></td>");
			}
			
			buffer.append("<td>"+(++i)+"</td>");
			buffer.append("<td>"+priscription2.getMdicinenametxt()+"</td>");
			if(priscription2.getDosage()!=null){
				buffer.append("<td>"+priscription2.getDosage()+"</td>");
			}else{
				buffer.append("<td></td>");
			}
			buffer.append("<td>"+priscription2.getPriscdays()+"</td>");
			buffer.append("<td>"+priscription2.getReq_qtys()+"</td>");
			/*buffer.append("<td><input type='number' class='form-control' id='mdrequestqty"+priscription2.getId()+"_request' name='mdrequestqty"+priscription2.getId()+"'></td>");*/
			buffer.append("</tr>");
		}
		
		String responsej=	buffer.toString();
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("responsej", responsej);
		String response1 = jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(response1);
	}
	catch(Exception e){
		e.printStackTrace();
	}
	return null;
}

public String priscrequestforreturn() throws Exception{
	if (!verifyLogin(request)) {
		return "login";
	}
	Connection connection= null;
	try{
		connection =Connection_provider.getconnection();
		StringBuilder buffer1 = new StringBuilder();
		BufferedReader reader = request.getReader();
		String line;
		while ((line = reader.readLine()) != null) {
			buffer1.append(line);
		}
		String data = buffer1.toString();
		JSONObject jsonObject = new JSONObject(data);
		
		EmrDAO emrDAO = new JDBCEmrDAO(connection);
		PrescriptionDAO prescriptionDAO = new JDBCPrescriptionDAO(connection);
		//MasterDAO masterDAO = new JDBCMasterDAO(connection);
		String parentid= jsonObject.getString("parentid");
		
		String date = "";
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		date = dateFormat.format(cal.getTime());
		
		StringBuffer buffer =new StringBuffer();
		int isforreturn =1;
		ArrayList<Priscription> arrayList= emrDAO.getPriscriptionChildData(parentid,isforreturn);
		int size = arrayList.size();
		if (size > 0){
			String totalid = arrayList.get(size - 1).getTotalid();
			String medid = arrayList.get(size - 1).getMedid();
			buffer.append("<input type='hidden' id='totalid_return' name='totalid' value='"+totalid+"'>");
			buffer.append("<input type='hidden' id='medid_return' name='medid' value='"+medid+"'>");
		}else{
			buffer.append("<input type='hidden' id='totalid_return' name='totalid' value='"+0+"'>");
			buffer.append("<input type='hidden' id='medid_return' name='medid' value='"+0+"'>");
		}
		int i=0;
		buffer.append("<input type='hidden' id='priscstatusparentid_return' name='parentid' value='"+parentid+"'>");
		for (Priscription priscription2 : arrayList) {
			int returnqty =  prescriptionDAO.getReturnQtyAgainstPrisc(priscription2.getId());
			int reqqty = priscription2.getReq_qtys();
			int availableqty = reqqty - returnqty;
			buffer.append("<tr>");
			buffer.append("<input type='hidden' class='akashpriscrequest_return' value='"+priscription2.getId()+"'>");
			buffer.append("<input type='hidden' name='mdname"+priscription2.getId()+"' value='"+priscription2.getMdicinenametxt()+"'>");
			buffer.append("<input type='hidden' name='mdid"+priscription2.getId()+"' value='"+priscription2.getMdicinenameid()+"'>");
			buffer.append("<input type='hidden' name='childid"+priscription2.getId()+"' value='"+priscription2.getId()+"'>");
			buffer.append("<input type='hidden' id='availaberetrunqty"+priscription2.getId()+"' value='"+availableqty+"'>");
			buffer.append("<td><input type='checkbox' class='akashcase_return' value='"+priscription2.getId()+"'  /></td>");
			buffer.append("<td>"+(++i)+"</td>");
			buffer.append("<td>"+priscription2.getMdicinenametxt()+"</td>");
			if(priscription2.getDosage()!=null){
				buffer.append("<td>"+priscription2.getDosage()+"</td>");
			}else{
				buffer.append("<td></td>");
			}
			buffer.append("<td>"+priscription2.getPriscdays()+"</td>");
			buffer.append("<td>"+reqqty+"</td>");
			buffer.append("<td>"+availableqty+"</td>");
			buffer.append("<td><input type='number' class='form-control' id='mdrequestqty_return"+priscription2.getId()+"' name='mdrequestqty"+priscription2.getId()+"'></td>");
			buffer.append("</tr>");
		}
		
		String responsej=	buffer.toString();
		JSONObject jsonobj = new JSONObject();
		jsonobj.put("responsej", responsej);
		String response1 = jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(response1);
	}
	catch(Exception e){
		e.printStackTrace();
	}
	return null;
}
public String getstdcharge() throws Exception {
	
	Connection connection=null;
     try {
      connection=Connection_provider.getconnection();
      ClientDAO clientDAO=new JDBCClientDAO(connection);
      AppointmentTypeDAO appointmentTypeDAO= new JDBCAppointmentTypeDAO(connection);
      String wardid= request.getParameter("wardid");
      String tpid= request.getParameter("tpid");
      String clientId= request.getParameter("clientId");
      String ipdid= request.getParameter("ipdid");
      String payee= request.getParameter("payee");
      
      if(payee==null){
    	  payee="Client";
      }
      if(payee.equals("")){
    	  payee="Client";
      }
      Client client=clientDAO.getClientDetails(clientId);
      String fullname= client.getTitle()+" "+client.getFirstName()+" "+client.getLastName();
      AccountsDAO accountsDAO=new JDBCAccountsDAO(connection);
      StringBuffer buffer=new StringBuffer();
      buffer.append(fullname);
      buffer.append("~");
  		if(payee.equals("Client") || payee.equals("Self")){
		 tpid="0";
  		}
  		tpid=DateTimeUtils.isNull(tpid);
  		
      ArrayList<Master> chargeList= appointmentTypeDAO.getStdOnoffChargeList(wardid, tpid,payee);
      IpdDAO ipdDAO = new JDBCIpdDAO(connection);
      ArrayList<String> wardlist=ipdDAO.getAllWardIdsOFPerson(ipdid);
      ArrayList<Master> otherwardList=new ArrayList<Master>();
      for(String ward:wardlist){
    	  if(!(wardid.equals(ward))){
    		  otherwardList.addAll(appointmentTypeDAO.getStdOnoffChargeListDiffWard(ward, tpid, payee, ipdid));
    	  }
      }
      
      
      chargeList.addAll(otherwardList);
      for(Master master: chargeList){
    	  
    	 boolean isSelected= appointmentTypeDAO.isStdChargeSelected(ipdid, master.getId());
    	 boolean isexist=appointmentTypeDAO.isStdChargeExist(ipdid, master.getId());
    			 
    	 if(isexist||master.getTpid().equals(tpid)){
    		 
    	 }else{
    		 continue;
    	 }
    	 if(!(master.getTpid().equals("0")||master.getTpid().equals(""))){
    		 master.setPayby("(TP)");
    	 }
    	
    	 
    	 Accounts accounts= accountsDAO.showonofftime(master.getId(), ipdid);
    	 Accounts accounts2= new Accounts();
    	 if(accounts.getId()>0){
    		 accounts2 = accountsDAO.getStdChargeChildData(accounts.getId());
    	 }
    	
    	 String wardname="";
    	 String color="";
    	 if(master.isFromOldWard()){
    		 wardname="W :"+ipdDAO.getIpdWardName(master.getWardid());
    		 color="color:green;";
    	 }
    	
    	 String ondate=DateTimeUtils.isNull(accounts2.getOndatetime());
    	 /*if(ondate.contains(",")){
    		 for(String on:ondate.split(",")){
    			 ondate=on;
    		 }
    	 }*/
    	 
    	 String offdate=DateTimeUtils.isNull(accounts2.getOffdatetime());
    	 /*if(offdate.contains(",")){
    		 for(String off:offdate.split(",")){
    			 offdate=off;
    		 }
    	 }*/
    	  /*if(isSelected){
    		  buffer.append("<input type='checkbox' checked='checked'  class='scase' id='ch"+master.getId()+"' name='ch"+master.getId()+"' value='"+master.getId()+"'  />");
    	  } else {
    		  buffer.append("<input type='checkbox' class='scase' id='ch"+master.getId()+"' name='ch"+master.getId()+"' value='"+master.getId()+"'  />");
    	  }
    	  buffer.append(master.getName()+" <input type='button' class='btn updatebtn' value='Update' onclick='updateOnOffStd("+ipdid+","+master.getId()+")' /> <br>  ");
    	*/
    	buffer.append("<div class='form-group col-xs-12' style='"+color+"'>");
    	buffer.append("<label class='col-xs-4 control-label'>"+master.getName()+"   : "+DateTimeUtils.isNull(master.getPayby())+"  "+wardname+" "+"</label>");
    	buffer.append("<div class='col-xs-1 control-label'>");
    	buffer.append("<div class='onoffswitch greensea'>");
		if(isSelected){
    		  buffer.append("<input type='checkbox' checked='checked' id='ch"+master.getId()+"' onclick='updateOnOffStd("+ipdid+","+master.getId()+","+accounts2.getId()+",1,"+accounts.getId()+")' name='ch"+master.getId()+"' value='"+master.getId()+"' class='onoffswitch-checkbox scase'>");
    		  buffer.append("<label class='onoffswitch-label' for='ch"+master.getId()+"'>");
			  buffer.append("<span class='onoffswitch-inner'></span>");
			  buffer.append("<span class='onoffswitch-switch'></span>");
			  buffer.append("</label>");
		}else{
			  buffer.append("<input type='checkbox' id='ch"+master.getId()+"' onclick='updateOnOffStd("+ipdid+","+master.getId()+","+accounts2.getId()+",0,"+accounts.getId()+")' name='ch"+master.getId()+"' value='"+master.getId()+"' class='onoffswitch-checkbox scase'>");
    		  buffer.append("<label class='onoffswitch-label' for='ch"+master.getId()+"'>");
			  buffer.append("<span class='onoffswitch-inner'></span>");
			  buffer.append("<span class='onoffswitch-switch'></span>");
			  buffer.append("</label>");
		}
		
		buffer.append("</div>");
		buffer.append("</div>");
		boolean flag = true;
		if(ondate.equals("")){
			flag=false;
		}
		
		boolean flag1 = true;
		if(offdate.equals("")){
			flag1=false;
		}
		
		if(!flag){
			buffer.append("<span class='col-xs-3 control-label'>"+ondate+"</span>");
		}else{
			/*buffer.append("<span class='col-xs-3 control-label'>"+ondate+"</span>");*/
			if(!flag1){
				buffer.append("<span class='col-xs-3 control-label'>"+ondate+"<a href='#' style='color: #d9534f;' onclick='editdatetime("+ipdid+","+master.getId()+","+0+","+clientId+","+accounts2.getId()+")' > <i class='fa fa-edit' aria-hidden='true' style='color: #d9534f;'></i></a></span>");
			}else{
				 
				buffer.append("<span class='col-xs-3 control-label'>"+ondate+"<a href='#' style='color: #d9534f;' onclick='editdatetime("+ipdid+","+master.getId()+","+0+","+clientId+","+accounts2.getId()+")' > <i class='fa fa-edit' aria-hidden='true' style='color: #d9534f;'></i></a></span>");
			}
			
		}
		
		if(isSelected){
			buffer.append("<span class='col-xs-3 control-label'></span>");
		}else{
			if(!flag){
				buffer.append("<span class='col-xs-3 control-label'>"+offdate+"</span>");
			}else{
				
				/*buffer.append("<span class='col-xs-3 control-label'>"+offdate+"</span>");*/
				buffer.append("<span class='col-xs-3 control-label'>"+offdate+"<a href='#' style='color: #d9534f;' onclick='editdatetime("+ipdid+","+master.getId()+","+1+","+clientId+","+accounts2.getId()+")' > <i class='fa fa-edit' aria-hidden='true' style='color: #d9534f;'></i></a></span>");
			}
			
		}
		
		
		
		buffer.append("<input type='button' class='btn updatebtn col-xs-1' value='Info' style='margin-top: 0px;' onclick='getOnoffTime("+ipdid+","+master.getId()+")'/>  ");
		buffer.append("</div>");
		
		
		/*buffer.append(master.getName()+" <input type='button' class='btn updatebtn' value='Update' onclick='updateOnOffStd("+ipdid+","+master.getId()+")' /> <br>  ");*/
		
      }
      
    
      response.setContentType("text/html");
      response.setHeader("Cache-Control", "no-cache");
      response.getWriter().write(buffer.toString());
      
  } catch (Exception e) {
   e.printStackTrace();
  }finally{
		connection.close();
	}
     
     return null;
	
}

public String updatestdonoff()throws Exception{
	
	Connection connection=null;
	
	try {
		connection=Connection_provider.getconnection();
		
		AppointmentTypeDAO appointmentTypeDAO= new JDBCAppointmentTypeDAO(connection);
		AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
		CompleteAptmDAO completeAptmDAO= new JDBCCompleteAptmDAO(connection);
		BedDao bedDao= new JDBCBedDao(connection);
		ClientDAO clientDAO= new JDBCClientDAO(connection);
		
		String chargeid= request.getParameter("chargeid");
		String ipdid= request.getParameter("ipdid");
		String status= request.getParameter("status");
		String stddate= request.getParameter("stddate");
		String starthour= request.getParameter("starthour");
		String startminute= request.getParameter("startminute");
		String enddate =request.getParameter("enddate");
		String endminute =request.getParameter("endminute");
		String endhour= request.getParameter("endhour");
		String childid =request.getParameter("childid");
		String previousstatus= request.getParameter("previousstatus");
		String parentid= request.getParameter("parentid");
		boolean stdbackdatechk = Boolean.parseBoolean(request.getParameter("stdbackdatechk"));
		
		if(stdbackdatechk){
			int r = appointmentTypeDAO.getStdChargeIdIdExists(ipdid,chargeid);
			if(r!=0){
				String curstatus = appointmentTypeDAO.getstddbcurstatus(r);
				if(curstatus.equals("1")){
					 response.setContentType("text/html");
				     response.setHeader("Cache-Control", "no-cache");
				     response.getWriter().write("0");
					return null;
				}
			}
		}
		/*String startTime = "";
		String endTime= "";
		String ondatetime= "";
		String offdatetime= "";
		if(!stdbackdatechk){
			startTime = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[1];
			endTime= DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[1];
			stddate = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
			stddate= DateTimeUtils.getCommencingDate1(stddate);
			enddate = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
			enddate= DateTimeUtils.getCommencingDate1(enddate);
			ondatetime= stddate+" "+startTime;
			offdatetime= enddate+" "+endTime;
		}else{
			startTime = starthour+":"+startminute+":"+"00";
			endTime= endhour+":"+endminute+":"+"00";
			ondatetime= stddate+" "+startTime;
			offdatetime= enddate+" "+endTime;
		}*/
		String startTime = starthour+":"+startminute+":"+"00";
		String endTime= endhour+":"+endminute+":"+"00";
		String ondatetime= stddate+" "+startTime;
		String offdatetime= enddate+" "+endTime;
		if(!stdbackdatechk){
			enddate = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
			endTime =DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[1];
			enddate= DateTimeUtils.getCommencingDate1(enddate);
			offdatetime= enddate+" "+endTime;
		}
		
		if(stddate!=null){
			
			if(stddate.equals("")){
				 stddate= DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
			} else {
				stddate= DateTimeUtils.getCommencingDate1(stddate);
			}
		} else {
			stddate= DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
		}
		
		if(enddate!=null){
			
			if(enddate.equals("")){
				enddate= DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
			} else {
				enddate= DateTimeUtils.getCommencingDate1(enddate);
			}
		} else {
			enddate= DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
		}
		
		String nowDate=DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
		
		long diff= DateTimeUtils.getDifferenceOfTwoDateDBFormat(stddate, nowDate);
		int qty =(int) diff;
		if(qty<0){
			 qty=0;
		}
		
		if(qty==0){
			qty=1;
		} else {
			qty++;
		}
		
		String stdcharges="001";
		int invoiceid=0;
		
		String time= DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[1];
		
		String date1 = DateTimeUtils.getDateinSimpleFormate(new Date());
		String stemp[] = date1.split(" ");
		
		String temp[] = stemp[0].split("-");
		date1 = temp[2] + "-" + temp[1] + "-" + temp[0];
		int result=0;
		
		Bed bed= bedDao.getEditIpdData(ipdid);
		String clientid = bed.getClientid();
		Client client= clientDAO.getClientDetails(clientid);

		//creating invoice
			
			CompleteAppointment appointment=new CompleteAppointment();
			appointment.setClientId(clientid);
			appointment.setPractitionerId(bed.getPractitionerid());
			appointment.setChargeType("Charge");
			appointment.setLocation("1");
		    appointment.setAdditionalcharge_id(stdcharges);
		    appointment.setIpdid(Integer.parseInt(ipdid));
		    appointment.setInvoiceDate(date1);
		    appointment.setIpd("1");
		    appointment.setAppointmentid("0");
		    appointment.setWardid(bed.getWardid());
		    if(client.getWhopay()!=null){
		    	
		    	if(client.getWhopay().equals("Self") || client.getWhopay().equals("Client")){
		    	       
		    		appointment.setPolicyExcess("0");
		    		appointment.setPayBuy("0");
		    	} else {
		    		appointment.setPolicyExcess("1");
		    		appointment.setPayBuy("1");
		    	}
		    }
		    invoiceid= completeAptmDAO.getInvoiceforStandardCharges(ipdid,stdcharges);
		    if(invoiceid==0){
		    	invoiceid=completeAptmDAO.saveAmpmInvoice(appointment,loginInfo.getId(),loginInfo.getUserId());
		    }
		    
		    String fullname=client.getTitle()+" "+client.getFirstName()+" "+client.getLastName();
		    appointment.setUser(fullname);
		    appointment.setCommencing(date1);     
		    
		    Master master= appointmentTypeDAO.getMasterCharges(chargeid);
	    	   appointment.setApmtType(master.getName());
	    	   appointment.setCharges(master.getCharge());
	    	   appointment.setAdditionalcharge_id(chargeid);
	    	   appointment.setMasterchargetype(master.getMasterchargetype());
	    	   appointment.setStartTime(time);
	    	   
	    	   qty = DateTimeUtils.getDifferanceofDateWithTime(ondatetime, offdatetime, master.getChargehours());
	    	   if(qty==0){
	   			qty=1;
	    	   }
	    	   appointment.setQuantity(qty);
	    	   appointment.setBackDate(stddate);
	    	   appointment.setStdflag("1");
	    	   
	    	   result = appointmentTypeDAO.getStdChargeIdIdExists(ipdid,chargeid);
	    	   if(status.equals("0")){
	    		   if(result==0){
	    			   
		    	   } else {
		    		   if(!stdbackdatechk){
		    			   
		    			 /*  Accounts accounts = appointmentTypeDAO.getStdChargeDetails(result);
		    			   String temps[] = accounts.getOndatetime().split(",");
		    			   String lastdate = temps[temps.length-1];
		    			   String temp1[] = lastdate.split(" ");
		    			   diff= DateTimeUtils.getDifferenceOfTwoDateDBFormat(DateTimeUtils.getCommencingDate1(temp1[0]), enddate);
		    			   qty =(int) diff;
		    			   
		    				int assesmentid = appointmentTypeDAO.getstdAssesmentid(result);
		    				int dbqty = appointmentTypeDAO.getDbQuantity(assesmentid);
		    				 qty = qty + dbqty;
		    			     int upd = accountsDAO.updateChargeqty(Integer.toString(assesmentid),Integer.toString(qty));*/
		    			     
		    			     result= appointmentTypeDAO.updateStdCharge(result,"0","",offdatetime);
		    			   
		    		   }else{
		    			   String curstatus = appointmentTypeDAO.getstddbcurstatus(result);
		    			   if(curstatus.equals("0")){
		    				   Accounts accounts = appointmentTypeDAO.getStdChargeDetails(result);
		    				   offdatetime = accounts.getOffdatetime() + " , " + offdatetime;
		    				   ondatetime = accounts.getOndatetime() + " , " + ondatetime;
		    				   int upd = appointmentTypeDAO.updateStdChargeDateTime(result, ondatetime, offdatetime);
		    			   }
		    		   }
		    		  
		    	   }
	    		   
	    	   }else {
	    		   
	    		   int assesmentid= completeAptmDAO.getAssesmentIdforStdChargeIfExits1(ipdid,chargeid,invoiceid,master.getMasterchargetype());
		    	   if(assesmentid==0){
		    		   if(stdbackdatechk){
		    			     /*diff= DateTimeUtils.getDifferenceOfTwoDateDBFormat(stddate, enddate);
		    				 qty =(int) diff;*/
		    			     qty = DateTimeUtils.getDifferanceofDateWithTime(ondatetime, offdatetime, master.getChargehours());
		    				 appointment.setQuantity(qty);
		    				 
		    		   }
		    		   assesmentid=completeAptmDAO.saveInvoiceAssesment(appointment, invoiceid);
		    	   }
		    	   if(result==0){
		    		   if(!stdbackdatechk){
		    			   result= appointmentTypeDAO.saveStdCharge(ipdid,chargeid,assesmentid,"1",ondatetime,"");
		    		   }else{
		    			   result= appointmentTypeDAO.saveStdCharge(ipdid,chargeid,assesmentid,"0",ondatetime,offdatetime);
		    			   
		    			  /* diff= DateTimeUtils.getDifferenceOfTwoDateDBFormat(stddate, enddate);
		    				 qty =(int) diff;*/
		    			   qty = DateTimeUtils.getDifferanceofDateWithTime(ondatetime, offdatetime, master.getChargehours());
		    				 assesmentid = appointmentTypeDAO.getstdAssesmentid(result);
		    			     int upd = accountsDAO.updateChargeqty(Integer.toString(assesmentid),Integer.toString(qty));
		    		   }
		    		   
		    	   } else {
		    		   
		    		   if(stdbackdatechk){
		    			   String curstatus = appointmentTypeDAO.getstddbcurstatus(result);
		    			   if(curstatus.equals("0")){
		    				     /*diff= DateTimeUtils.getDifferenceOfTwoDateDBFormat(stddate, enddate);
			    				 qty =(int) diff;*/
		    				   qty = DateTimeUtils.getDifferanceofDateWithTime(ondatetime, offdatetime, master.getChargehours());
			    				 assesmentid = appointmentTypeDAO.getstdAssesmentid(result);
			    				 if(qty==0){
			    					 qty=1;
			    				 }
			    				 int dbqty = appointmentTypeDAO.getDbQuantity(assesmentid);
			    				 qty = qty + dbqty;
			    				 int upd = accountsDAO.updateChargeqty(Integer.toString(assesmentid),Integer.toString(qty));
		    			   }
		    			    
		    				 
		    		   }
		    		   if(!stdbackdatechk){
		    			   String curstatus = appointmentTypeDAO.getstddbcurstatus(result);
		    			   if(curstatus.equals("0")){
		    				   qty = DateTimeUtils.getDifferanceofDateWithTime(ondatetime, offdatetime, master.getChargehours());
			    			   int dbqty = appointmentTypeDAO.getDbQuantity(assesmentid);
			    			   if(qty==0){
			    					 qty=1;
			    				 }
			    				 qty = qty + dbqty;
			    			   int upd = accountsDAO.updateChargeqty(Integer.toString(assesmentid),Integer.toString(qty));
			    			   result= appointmentTypeDAO.updateStdCharge(result,"1",ondatetime,"");
		    			   }
		    		   }else{
		    			   String curstatus = appointmentTypeDAO.getstddbcurstatus(result);
		    			   if(curstatus.equals("0")){
		    				   Accounts accounts = appointmentTypeDAO.getStdChargeDetails(result);
		    				   offdatetime = accounts.getOffdatetime() + " , " + offdatetime;
		    				   ondatetime = accounts.getOndatetime() + " , " + ondatetime;
		    				   int upd = appointmentTypeDAO.updateStdChargeDateTime(result, ondatetime, offdatetime);
		    			   }
		    		   }
		    		   
		    	   }
	    		   
	    	   }
		
	    	 //akash 06-08-2019 below if else only
	    	Accounts accounts= accountsDAO.showonofftime(Integer.parseInt(chargeid), ipdid);
	   		if(previousstatus.equals("0")){
	   			int res = accountsDAO.insertStdChargeChild(chargeid,""+accounts.getId(),loginInfo.getUserId(),DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),ondatetime);
	   		}else{
	   			int res = accountsDAO.updateStdChargeChild(loginInfo.getUserId(),DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),childid,offdatetime);
	   		}
		
    	 response.setContentType("text/html");
	     response.setHeader("Cache-Control", "no-cache");
	     response.getWriter().write("1");
		
		
	} catch (Exception e) {

		e.printStackTrace();
	}finally{
		connection.close();
	}
	return null;
}

public String editchargedate() throws Exception {
	
	Connection connection=null;
     try {
      connection=Connection_provider.getconnection();
      ClientDAO clientDAO=new JDBCClientDAO(connection);
      AppointmentTypeDAO appointmentTypeDAO= new JDBCAppointmentTypeDAO(connection);
      String ipdid= request.getParameter("ipdid");
      String chargeid= request.getParameter("chargeid");
      String startend= request.getParameter("startend");
      String clientId= request.getParameter("clientid");
      String childid= request.getParameter("childid");
      Client client=clientDAO.getClientDetails(clientId);
      String fullname= client.getTitle()+" "+client.getFirstName()+" "+client.getLastName();
      AccountsDAO accountsDAO=new JDBCAccountsDAO(connection);
      StringBuffer buffer=new StringBuffer();
      buffer.append(fullname);
      buffer.append("~~");
      String chargename = appointmentTypeDAO.getChargeName(chargeid);
      buffer.append(""+chargename+"");
      Accounts accounts= accountsDAO.showonofftime(Integer.parseInt(chargeid), ipdid);
      String date ="";
      String hour ="00";
      String min ="00";
      if(startend.equals("0")){
    	  String ondate=DateTimeUtils.isNull(accounts.getOndatetime());
    	  if(ondate.contains(",")){
    		 for(String on:ondate.split(",")){
    			 ondate=on;
    		 }
    	  }
    	  if(!ondate.equals("")){
    		  String datetime[] = ondate.split(" ");
    		  if(datetime.length==3){
    			  date = datetime[1];
    			  hour = datetime[2].split(":")[0];
    			  min = datetime[2].split(":")[1];
    		  }else{
    			  date = datetime[0];
    			  hour = datetime[1].split(":")[0];
    			  min = datetime[1].split(":")[1];
    		  }
    	  }
      }else{
    	  String offdate=DateTimeUtils.isNull(accounts.getOffdatetime());
    	  if(offdate.contains(",")){
    		 for(String off:offdate.split(",")){
    			 offdate=off;
    		 }
    	  }
    	  if(!offdate.equals("")){
    		  String datetime[] = offdate.split(" ");
    		  if(datetime.length==3){
    			  date = datetime[1];
    			  hour = datetime[2].split(":")[0];
    			  min = datetime[2].split(":")[1];
    		  }else{
    			  date = datetime[0];
    			  hour = datetime[1].split(":")[0];
    			  min = datetime[1].split(":")[1];
    		  }
    	  }
      }
      buffer.append("~~");
      buffer.append(""+date+"");
      buffer.append("~~");
      buffer.append(""+hour+"");
      buffer.append("~~");
      buffer.append(""+min+"");
      buffer.append("~~");
      buffer.append(""+ipdid+"");
      buffer.append("~~");
      buffer.append(""+chargeid+"");
      buffer.append("~~");
      buffer.append(""+startend+"");
      buffer.append("~~");
      buffer.append(""+clientId+"");
      buffer.append("~~");
      buffer.append(""+childid+"");
      response.setContentType("text/html");
      response.setHeader("Cache-Control", "no-cache");
      response.getWriter().write(buffer.toString());
      
  } catch (Exception e) {
   e.printStackTrace();
  }finally{
		connection.close();
	}
     
     return null;
	
}

public String updatestdonofftimedate()throws Exception{
	try {
		Connection connection= Connection_provider.getconnection();
		String date=DateTimeUtils.isNull(request.getParameter("stddate"));
		String time=DateTimeUtils.isNull(request.getParameter("starthour"))+":"+DateTimeUtils.isNull(request.getParameter("startminute"))+":00";
		//dd-mm-yy hh:mm:ss
		String datetime=date+" "+time;
		String ipdid=request.getParameter("ipdid");
		String stdchargeId=request.getParameter("chargeid");
		String childId=request.getParameter("editstdxhildid");
		String clientId=request.getParameter("clientid");
		String startorend=DateTimeUtils.isNull(request.getParameter("editstdstartend"));
		
		AccountsDAO accountsDAO= new JDBCAccountsDAO(connection);
		Accounts accounts=accountsDAO.showonofftime(Integer.parseInt(stdchargeId), ipdid);
		Accounts childaccount=accountsDAO.getOnOffChargesChildDates(""+accounts.getId());
		String appenddate="";
		if(startorend.equals("0")){
			appenddate=childaccount.getOndatetime();
		}else{
			appenddate=childaccount.getOffdatetime();
		}		
		appenddate=DateTimeUtils.isNull(appenddate);
		String temp[]=appenddate.split(",");
		
		String correcttime="";
		temp[temp.length-1]=datetime;
		for (int i = 0; i < temp.length; i++) {
			if(i==0){
				correcttime=temp[i];
			}else{
				correcttime=correcttime+","+temp[i];
			}
			
		}
		if(appenddate.contains(",")){
			appenddate=appenddate+","+datetime;
		}else{
			appenddate=datetime;
		}
		int x=accountsDAO.updatestdOnFFDateTime(ipdid, stdchargeId, correcttime, startorend);
		x=accountsDAO.updateChildOnOffDatetime(datetime, childId, startorend);
		
		
		//Charge qty Logic 
		accounts=accountsDAO.showonofftime(Integer.parseInt(stdchargeId), ipdid);
		 childaccount=accountsDAO.getOnOffChargesChildDates(""+accounts.getId());
		int totalqty=0;
		AppointmentTypeDAO appointmentTypeDAO=new JDBCAppointmentTypeDAO(connection);
		 Master master= appointmentTypeDAO.getMasterCharges(stdchargeId);
		{
			String ontm[]=childaccount.getOndatetime().split(",");
			String offtm[]=childaccount.getOffdatetime().split(",");
			for (int i = 0; i < offtm.length; i++) {
				String ondatetime = DateTimeUtils.isNull(ontm[i]);
				String offdatetime= DateTimeUtils.isNull(offtm[i]);
				if(ondatetime.contains(":")&&offdatetime.contains(":")){
					  int qty = 1+DateTimeUtils.getDifferanceofDateWithTime(ondatetime, offdatetime, master.getChargehours());	
					  totalqty=totalqty+qty;
				}
			}
		}
		
			System.out.println(totalqty);
			CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(connection);
			int invoiceid = completeAptmDAO.getInvoiceforStandardCharges(ipdid, "001");
			int assesmentid = completeAptmDAO.getAssesmentIdforStdChargeIfExits1(ipdid, stdchargeId, invoiceid,
					master.getMasterchargetype());
			int upd = accountsDAO.updateChargeqty(Integer.toString(assesmentid), Integer.toString(totalqty));
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("" );
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}


public String getonoffcharges(){
	  try {
			Connection connection= Connection_provider.getconnection();
	  String ipdid= request.getParameter("ipdid");
		String id =request.getParameter("id");
		 Accounts accounts=new Accounts();
		 AccountsDAO accountsDAO=new JDBCAccountsDAO(connection);
			accounts=accountsDAO.showonofftime(Integer.parseInt(id),ipdid);
			accounts.setOndatetime(DateTimeUtils.isNull(accounts.getOndatetime()));
			accounts.setOffdatetime(DateTimeUtils.isNull(accounts.getOffdatetime()));
			String str=accounts.getOndatetime()+"~"+accounts.getOffdatetime();
			
			
			ArrayList<Accounts> childList= accountsDAO.getOnOffChildList(""+accounts.getId());
			StringBuffer buffer=new StringBuffer();
			if(childList.size()>0){
				buffer.append("<table class='my-table lkclass' style='width:100%'>");
				buffer.append("<tr>");
				buffer.append("<th>On Date/Time</th>");
				buffer.append("<th>Off Date/Time</th>");
				buffer.append("<th>Delete</th>");
				buffer.append("</tr>");
			
			
			for(Accounts child:childList){
				buffer.append("<tr>");
				buffer.append("<td>"+child.getOndatetime()+"</td>");
			
				buffer.append("<td>"+child.getOffdatetime()+"</td>");
				if(child.getOffdatetime().equals("")){
					buffer.append("<td><a></a></td>");
				}else{
					buffer.append("<td><a onclick='delonnoffstd("+child.getChildid()+")'><i class='fa fa-trash'></a></td>");
				}
				
						
				
				buffer.append("</tr>");
			}
		
				buffer.append("</table>");
			}
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+buffer.toString()+"");
	  }catch (Exception e) {
		e.printStackTrace();
	}
	return null;
	  
}
public String editcharge() throws Exception{
	//var url = "editchargeStatement?ipdid="+ipdid+"&chargeid="+chargeid+"&assesmentid="+assesmentid+" ";
	StringBuilder buffer1 = new StringBuilder();
	BufferedReader reader = request.getReader();
	String line;
	while ((line = reader.readLine()) != null) {
		buffer1.append(line);
	}
	String data = buffer1.toString();
	JSONObject jsonObject = new JSONObject(data);
	//MasterDAO masterDAO = new JDBCMasterDAO(connection);
	String ipdid = jsonObject.getString("ipdid");
	String chargeid = jsonObject.getString("chargeid");
	String assesmentid = jsonObject.getString("assesmentid");
	String stdcharge =  jsonObject.getString("stdcharge");
	String newchargename=jsonObject.getString("chargename");
	String updchargeid=jsonObject.getString("updchargeid");
	String mastername=jsonObject.getString("mastername");
//	String ipdid = request.getParameter("ipdid");
//	String chargeid = request.getParameter("chargeid");
//	String assesmentid = request.getParameter("assesmentid");
//	String stdcharge =  request.getParameter("stdcharge");
//	String newchargename=request.getParameter("chargename");
//	String updchargeid=request.getParameter("updchargeid");
//	String mastername=request.getParameter("mastername");
	session.setAttribute("chargeipdid", ipdid);
	session.setAttribute("chargechargeid", chargeid);
	session.setAttribute("chargeassesmentid", assesmentid);
	session.setAttribute("stdcharge", stdcharge);
	session.setAttribute("updchargeid", updchargeid);
	
	Connection connection  = null;
	String id = request.getParameter("id");
	
	try{
		
		connection  = Connection_provider.getconnection();
		AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
		IpdDAO ipdDAO=new JDBCIpdDAO(connection);
		String clientid=ipdDAO.getClientIDFromIPDID(ipdid);
		Accounts accounts = accountsDAO.getAssesmentDetails(assesmentid);
		ArrayList<Accounts> chargelist=accountsDAO.getchargelist(mastername);
		StringBuffer str = new StringBuffer();
//		str.append("<tr>");
//		str.append("<td>Qty : <input type='number' class='form-control'  name='qty"+accounts.getId()+"' id='qty"+accounts.getId()+"' value='"+accounts.getQuantity()+"'></td>");
//		str.append("</tr>");
		ClientDAO clientDAO=new JDBCClientDAO(connection);
		Client client = clientDAO.getClientDetails(clientid);
		String tpid = client.getTypeName();
			 
			String temptpid= ipdDAO.getFollowerTp(tpid); 
			if(temptpid!=null){
				
				if(!temptpid.equals("0")){
					 tpid=temptpid;  
				}
			}
				ThirdPartyDAO thirdPartyDAO = new JDBCThirdPartyDAO(connection);
				ThirdParty thirdParty = thirdPartyDAO.getThirdPartyDetails(tpid);
		ArrayList<Client> apmtmentlist=clientDAO.getApmtNameList(mastername,thirdParty.getChargecolumnname(),tpid);	
		
		boolean isSTdCharge = accountsDAO.getIfSTdCharge(chargeid);
		
		if(isSTdCharge && stdcharge.equals("1")){
			String startEndTime= accountsDAO.getStdChargeStartEndTime(Integer.parseInt(assesmentid),ipdid); 
			session.setAttribute("startEndTime", startEndTime);
			
			
			if(startEndTime!=null){
				String tempc[] = startEndTime.split("~");
				String t1[] = tempc[0].split(",");
				String t2[] = tempc[1].split(",");
				String resultdatetime = "";
				for(int i=0;i<t2.length;i++){
					//resultdatetime = resultdatetime + t1[i] + " - " + t2[i] + " , "; 
					str.append("<tr>");
					str.append("<td>From Date : <input type='text' class='form-control' readonly='readonly' name='fdate"+i+"' id='fdate"+i+"' value='"+t1[i]+"' autocomplete='off'></td>");
					str.append("<td>ToDate : <input type='text' class='form-control' readonly='readonly' name='tdate"+i+"' id='tdate"+i+"' value='"+t2[i]+"'></td>");
					str.append("</tr>");
					str.append("<tr>");
					str.append("<td>Charge : <input type='text' class='form-control'  name='newchargename' id='newchargename' value='"+newchargename+"'></td>");
					str.append("</tr><br>");
					str.append("<tr>");
					
					str.append("<br><select id='apmttype' name='apmttype' class='form-control chosen'>");
					str.append("<option value='0'>Replace Existing Charge</option>");

					for (Client apmt : apmtmentlist) {
						str.append("<option value='" + apmt.getMasterid() + "'>" + apmt.getMastername() + "</option>");
					}

					str.append("</select>");
					str.append("</tr>");
					str.append("<br><br>");
					str.append("<tr>");
					str.append("<br><button type='button' class='btn btn-info' onclick=openPopup('debitAdditional?clientId="+clientid+"')>Add New Charge</button></td>");
					str.append("</tr><br>");
				}
				int curstatus = accountsDAO.getstdonoffcurstatus(Integer.parseInt(assesmentid),ipdid);
				if(curstatus==1){
					//resultdatetime = resultdatetime + " , " + t1[t1.length-1];
					int j = t1.length-1;
					str.append("<tr>");
					str.append("<td>Charge Date : <input type='text' class='form-control' readonly='readonly' name='fdate"+j+"' id='fdate"+j+"' value='"+t1[t1.length-1]+"' autocomplete='off'></td>");
					str.append("</tr>");
				}
				
			}
		}else{
			str.append("<tr>");
			str.append("<td>Charge Date : <input type='text' class='form-control' readonly='readonly' name='fdate' id='fdate' value='"+DateTimeUtils.getCommencingDate1(accounts.getCommencing())+"' autocomplete='off'></td>");
			if(loginInfo.getClinicUserid().equals("ngppadole"))
			{
			str.append("<td>Showing Date : <input type='text' class='form-control' name='sdate' id='sdate' value='"+DateTimeUtils.isNull(accounts.getShowdate())+"' autocomplete='off'></td>");
			}
			str.append("</tr><br>");
			str.append("<tr>");
			str.append("<td>Charge : <input type='text' class='form-control'  name='newchargename' id='newchargename' value='"+newchargename+"'></td>");
			str.append("</tr><br>");
			str.append("<tr>");
			
			str.append("<br><select id='apmttype' name='apmttype' class='form-control chosen'>");
			str.append("<option value='0'>Replace Existing Charge</option>");

			for (Client apmt : apmtmentlist) {
				str.append("<option value='" + apmt.getMasterid() + "'>" + apmt.getMastername() + "</option>");
			}

			str.append("</select>");
			str.append("</tr>");
			
			str.append("<br><br>");
			str.append("<tr>");
			str.append("<br><button type='button' class='btn btn-info' onclick=openPopup('debitAdditional?clientId="+clientid+"')>Add New Charge</button>");
			str.append("</tr><br>");
			/*String curdate = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
			String tempa[] = curdate.split(" ");
			
			str.append("<tr>");
			str.append("<td>FromDate : <input type='text' class='form-control'  name='tdate' id='tdate' value='"+tempa[0]+"'></td>");
			str.append("</tr>");*/
		}
		String ngppadole="";
		/*if(loginInfo.getClinicUserid().equals("ngppadole"))
		{
			ngppadole="1";
		}else{
			ngppadole="0";
		}*/
		
		JSONObject jsonobj = new JSONObject();
	
		jsonobj.put("chargedtailsbody", str.toString());
		jsonobj.put("ngppadole", ngppadole);
		String response1 = jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(response1);
		
		
	}catch (Exception e) {
	e.printStackTrace();
	}finally{
		
		connection.close();
	}
	
	return null;
}
public String deleteinddiscount(){
	Connection connection= null;
	String id= request.getParameter("id");
	String reason= request.getParameter("reason");
	String userid= loginInfo.getUserId();
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar cal = Calendar.getInstance();
    String date = dateFormat.format(cal.getTime());  
	
	try {
		connection = Connection_provider.getconnection();
		AppointmentDAO appointmentDAO= new JDBCAppointmentDAO(connection);
		int res=appointmentDAO.deletediscountindfromdashboard(id, userid, date, reason);
		CompleteAppointment appointment = appointmentDAO.getDeleteDiscountReqData(id);
		if(appointment.getInvoiced()>0){
			res = appointmentDAO.setUpdateDiscRequestStatusTo0(appointment.getInvoiced());
		}
		 response.setContentType("text/html");
	      response.setHeader("Cache-Control", "no-cache");
	      response.getWriter().write("0"); 
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}


public String delonnoffstd(){
	try {
		Connection connection= Connection_provider.getconnection();
		String childid=DateTimeUtils.isNull(request.getParameter("child"));
		
		AccountsDAO accountsDAO= new JDBCAccountsDAO(connection);
		Accounts accounts= accountsDAO.getOnOffChargesChildDateNEW(childid);
		Accounts parent= accountsDAO.getOnOffChargesParent(accounts.getParentId());
		int res=accountsDAO.delChildOnOffCharge(childid);
		AppointmentTypeDAO appointmentTypeDAO= new JDBCAppointmentTypeDAO(connection);
		Master master= appointmentTypeDAO.getMasterCharges(parent.getChargeid());
		if(res==1){
			Accounts childaccount=accountsDAO.getOnOffChargesChildDates(""+accounts.getParentId());
				int totalqty=0;
				
				{
					String ontm[]=childaccount.getOndatetime().split(",");
					String offtm[]=childaccount.getOffdatetime().split(",");
					for (int i = 0; i < offtm.length; i++) {
						String ondatetime = DateTimeUtils.isNull(ontm[i]);
						String offdatetime= DateTimeUtils.isNull(offtm[i]);
						if(ondatetime.contains(":")&&offdatetime.contains(":")){
							  int qty = 1+DateTimeUtils.getDifferanceofDateWithTime(ondatetime, offdatetime, master.getChargehours());	
							  totalqty=totalqty+qty;
						}
					}
				}
			  int upd = accountsDAO.updateChargeqty(parent.getAssesmentid(),Integer.toString(totalqty));
		}
		String rest=parent.getIpdid()+"-"+parent.getChargeid();
		 response.setContentType("text/html");
	      response.setHeader("Cache-Control", "no-cache");
	      response.getWriter().write(""+rest); 
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}
public String getallnotoccupiedbeds(){
	Connection connection= null;
	String wardid=request.getParameter("wardid");
	try {
		connection= Connection_provider.getconnection();
		IpdDAO ipdDAO = new JDBCIpdDAO(connection);
		ArrayList<Bed> bedList = ipdDAO.getWardWiseBedList(wardid);

		StringBuffer str = new StringBuffer();
		str.append("<select  name='bedid' id='bedname' class='form-control chosen-select'>");
		str.append("<option value='0'>Select Bed</option>");

		for (Bed bed : bedList) {
			str.append("<option value='" + bed.getId() + "'>" + bed.getBedname() + "</option>");
		}

		str.append("</select>");
		str.append("&nbsp; &nbsp; &nbsp;<p><button class='btn btn-primary' onclick='savepatienttoipd()' >Save To IPD</button></p>");
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write("" + str.toString() + "");

		
	} catch (Exception e) {
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

public String viewemr(){
	
	String clientId = request.getParameter("clientId");
	String prectionerid = request.getParameter("prectionerid");
	String conditionid = request.getParameter("conditionid");
	
	Connection connection = null;
	try{
		
		
		connection = Connection_provider.getconnection();
		InvestigationDAO investigationDAO = new JDBCInvestigationDAO(connection);
		
		ArrayList<Investigation>invstigationList = investigationDAO.getParentPriscList(clientId,prectionerid,conditionid);
		
		StringBuffer str = new StringBuffer();
		
		   str.append("<table class='table'>");
		   str.append("<thead class='thead-dark'>");
		   str.append("<tr >");
		   str.append("<th class='emrinvestigationfont'>Sr.No</th><th class='emrinvestigationfont'>Investigation Type</th><th class='emrinvestigationfont'>Date</th><th class='emrinvestigationfont'>Print</th><th class='emrinvestigationfont'>Pacs Report</th>");
		   str.append("</tr>");
		   str.append("</thead>");
		   str.append("<tbody>");
		   int i=0;
		
		for(Investigation investigation : invstigationList){
			
			str.append("<tr>");
			str.append("<td>"+(++i)+"</td>");	
			str.append("<td>"+investigation.getInvsttype()+"</td>");	
			str.append("<td>"+investigation.getDate()+"</td>");
			if(investigation.getInvstreporttype().equals("Numerical")){
				str.append("<td><a href='#' onclick='printinvstigationrecord("+investigation.getId()+",0)' title='Print Investigation' class='editpricon'><i class='fa fa-print emrinvestigationfontprint'></i></a></td>");
				
			}else{
				str.append("<td><a href='#' onclick='printinvstigationrecord("+investigation.getId()+",1)' title='Print Investigation' class='editpricon'><i class='fa fa-print emrinvestigationfontprint'></i></a></td>");
			}
			
			if(investigation.getPacs()!=0){
				str.append("<td><a href='#' onclick='viewpacsreport("+investigation.getId()+")' title='Pacs Report' class='editpricon'><i class='fa fa-object-ungroup aria-hidden='true''></i></a></td>");
			}
			str.append("<td></td>"); 
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

public String checkdisccancelorapplied() throws Exception{
	  Connection connection =null;
	  try {
		   connection = Connection_provider.getconnection();
		   AppointmentDAO appointmentDAO = new JDBCAppointmentDAO(connection);
		   String discountid = request.getParameter("discountid");
		   String invoiceid = request.getParameter("invoiceid");
		   String isaproveordisc = request.getParameter("isaproveordisc");
		   boolean flag = appointmentDAO.checkDiscRequestDeleted(discountid);
		   int res =0;
		   if(!flag){
			   flag = appointmentDAO.checkInvoiceCreatedAgainstDiscReq(invoiceid);
			   if(flag){
				   res =2;
			   }
		   }else{
			   res=1;
		   }
		   response.setContentType("text/html");
		   response.setHeader("Cache-Control", "no-cache");
		   response.getWriter().write(""+res+"~~"+isaproveordisc+""); 
		  
	  } catch (Exception e) {
	  		e.printStackTrace();
	  }finally{
		  connection.close();
	  }
	  return null;
	 }
public String getappointmentdata(){
	 Connection connection =null;
	  try {
		   connection = Connection_provider.getconnection();
		   String editAppointId = request.getParameter("apmtid");
		   NotAvailableSlotDAO notAvailableSlotDAO=new JDBCNotAvailableSlotDAO(connection);
		   NotAvailableSlot notAvailableSlot=notAvailableSlotDAO.getAvailableSlotdata(Integer.parseInt(editAppointId));
		   AppointmentTypeDAO appointmentTypeDAO=new JDBCAppointmentTypeDAO(connection);
		   ClientDAO clientDAO=new JDBCClientDAO(connection);
		   StringBuffer str=new StringBuffer();
		   str.append(""+notAvailableSlot.getsTime()+"~~"+notAvailableSlot.getEndTime()+"~~"+DateTimeUtils.changeDateFormattoPicker(notAvailableSlot.getCommencing())+"~~"+notAvailableSlot.getDiaryUserId()+"~~"+notAvailableSlot.getDuration()+"~~"+notAvailableSlot.getAppointmentid()+"~~"+notAvailableSlot.getOpdpmnt()+"~~"+notAvailableSlot.getChrgstatus());
		   ArrayList<Client> acc=clientDAO.getAllPatientByApmtid(Integer.parseInt(notAvailableSlot.getClientId()));
		   int size=acc.size();
		   
		   ArrayList<AppointmentType> appointmentTypeList = appointmentTypeDAO.gettpAppointmentTypeList(acc.get(size-1).getTypeName(), acc.get(size-1).getWhopay(),false,"0");

			StringBuffer str1 = new StringBuffer();

			str1.append("<select onchange='setAppointmentTypeTimeAjax(this.value)' name='duration' id='apmtType12' class='form-control showToolTip chosen' >");
			str1.append("<option value='0'>Select Appointment Type</option>");
			for (AppointmentType appointmentType : appointmentTypeList) {
				str1.append("<option value='" + appointmentType.getId() + "'>"
						+ appointmentType.getName() + "</option>");
			}
			str1.append("</select>");
		   
		   response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+str.toString()+"~~"+str1.toString()+""); 
	  }catch (Exception e) {
		e.printStackTrace();
	}
	return null;
	
}
public String alreadybooked(){
		 Connection connection =null;
		  try {
			  int res=0;
			   connection = Connection_provider.getconnection();
			   AppointmentDAO appointmentDAO=new JDBCAppointmentDAO(connection);
			   String bedid = request.getParameter("bedid");
			   boolean flag = appointmentDAO.checkalredyadmitted(bedid);
			   if(flag){
				   res=1;
			   }
			   response.setContentType("text/html");
			   response.setHeader("Cache-Control", "no-cache");
			   response.getWriter().write(""+res+""); 
			  
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return null;
	
}

public String removemedicine() throws Exception {
	
	Connection connection=null;
	try {
		connection=Connection_provider.getconnection();
		String id= request.getParameter("id");
		IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		int res= ipdDAO.removeMedicineDischarge(id); 
		
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write("");
	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		connection.close();
	}
	
	return null;
}
public String selfcharge()throws Exception{
	Connection connection=null;
    try {
    	connection=Connection_provider.getconnection();
    	IpdDAO ipdDAO = new JDBCIpdDAO(connection);
    	ChargesAccountProcessingDAO chargesAccountProcessingDAO= new JDBCChargeAccountProcesDAO(connection);
    	
    	String chargetype = request.getParameter("chargetype");
    	
    	String ipdwhopay = request.getParameter("ipdwhopay");
    	String ipdtpid = request.getParameter("ipdtpid");
    	String ipdaddmissionid = request.getParameter("ipdaddmissionid");
    	String clientId = request.getParameter("clientId");
    	
    	if(ipdwhopay.equals("")){
    		ClientDAO clientDAO = new JDBCClientDAO(connection);
    		Client client = clientDAO.getClientDetails(clientId);
    		ipdwhopay = client.getWhopay();
    	}
    	
    	if(ipdwhopay.equals(Constants.PAY_BY_CLIENT)){
    		ipdtpid = "0";
    	}
    	
    	if(ipdaddmissionid==null){
    		
    		ipdaddmissionid="0";
    	}
    	if(ipdaddmissionid.equals("")){
    		ipdaddmissionid="0";
    	}
    	
    	BedDao bedDao=new JDBCBedDao(connection);
		Bed bed = bedDao.getEditIpdData(ipdaddmissionid);
		
		String wardid = bed.getWardid();
		
		if(wardid==null){
			wardid="0";
		}
		if(wardid.equals("")){
			wardid="0";
		}
		
		int compulsary_consultant=chargesAccountProcessingDAO.isCompulasryConsultant(chargetype);
    	
    	ArrayList<Master>list = ipdDAO.getFilteredChargeList(chargetype,ipdtpid,wardid,clientId,loginInfo.isShow_wardname());
    	StringBuffer str = new StringBuffer();
   
    		str.append("<select  name='chargeTYpe' id='chargeTYpe' class='form-control showToolTip chosen' >");
    		str.append("<option value='0'>Select Charge</option>");
    		for(Master master : list){
    			str.append("<option value='"+master.getId()+"'>"+master.getName()+"</option>");
    		}
    		str.append("</select>");
    		
    		str.append("!@");
    		str.append(compulsary_consultant);
    		response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+str.toString()+""); 
    	
    }catch(Exception e){
 	   e.printStackTrace();
    }
    finally{
		connection.close();
	}
	return null;
}

public String getdisadvoice() throws Exception {
    
    Connection connection=null;
    try {
     connection=Connection_provider.getconnection();
     TreatmentEpisodeDAO treatmentEpisodeDAO=new JDBCTreatmentEpisode(connection);
     String treatmentid=request.getParameter("treatmentepisode");
     String advoice=treatmentEpisodeDAO.getDischargeAdvoice(treatmentid);
     
     response.setContentType("text/html");
     response.setHeader("Cache-Control", "no-cache");
     response.getWriter().write(""+advoice+"");
     
 } catch (Exception e) {
  e.printStackTrace();
 }finally{
		connection.close();
	}
    
    return null;
}
}
