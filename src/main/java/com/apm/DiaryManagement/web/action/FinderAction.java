package com.apm.DiaryManagement.web.action;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionMessages;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.components.ActionError;

import com.apm.Appointment.eu.entity.AppointmentType;
import com.apm.DiaryManagement.eu.bi.ClientDAO;
import com.apm.DiaryManagement.eu.bi.FinderDAO;
import com.apm.DiaryManagement.eu.bi.NotAvailableSlotDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCClientDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCFinderDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCNotAvailableSlotDAO;
import com.apm.DiaryManagement.eu.entity.Client;
import com.apm.DiaryManagement.eu.entity.DiaryManagement;
import com.apm.DiaryManagement.eu.entity.NotAvailableSlot;
import com.apm.DiaryManagement.web.form.NotAvailableSlotForm;
import com.apm.Log.eu.entity.EmailLetterLog;
import com.apm.Registration.eu.bi.UserProfileDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCUserProfileDAO;
import com.apm.Registration.eu.entity.Location;
import com.apm.Registration.eu.entity.UserProfile;
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
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class FinderAction extends BaseAction implements Preparable, ModelDriven<NotAvailableSlotForm>{
	
	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
	HttpServletResponse response = (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
	HttpSession session = request.getSession(true);
	NotAvailableSlotForm notAvailableSlotForm = new NotAvailableSlotForm();
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	
	public String execute() throws Exception {
		
		if(!verifyLogin(request)){
			return "login";
		}
		
		
		Connection connection = null;
		try{
			
			connection = Connection_provider.getconnection();
			FinderDAO finderDAO = new JDBCFinderDAO(connection);
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			
			//set selected clientid from session
			if(session.getAttribute("sessionselectedclientid")!=null){
				if(notAvailableSlotForm.getClientId().equals("")){
					String clientid = (String)session.getAttribute("sessionselectedclientid");
					notAvailableSlotForm.setClientId(clientid);
					/*Client client = clientDAO.getSelectedSessionClientDetails(clientid);
					notAvailableSlotForm.setClient(client.getClientName());*/
				}
				
			}
			
			//if(!notAvailableSlotForm.getClientId().equals("")){
				String  fromDate = notAvailableSlotForm.getFromDate();
				if(!fromDate.equals("")){
					String temp[]= fromDate.split("/");
					fromDate = temp[2]+"-"+temp[1]+"-"+temp[0];
				}
				
				ArrayList<NotAvailableSlot>finderList = finderDAO.getFinderList(notAvailableSlotForm.getClientId(),fromDate);
				notAvailableSlotForm.setFinderList(finderList);
				
				session.setAttribute("clientid", notAvailableSlotForm.getClientId());
				session.setAttribute("client", notAvailableSlotForm.getClient());
				
				Client client = clientDAO.getSelectedSessionClientDetails(notAvailableSlotForm.getClientId());
				notAvailableSlotForm.setClient(client.getClientName());
			//}
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			connection.close();
		}
		
		return SUCCESS;
	}

	
	public String input() throws Exception {
		if(!verifyLogin(request)){
			return "login";
		}
		
		return super.input();
	}
	
	


	public NotAvailableSlotForm getModel() {
		// TODO Auto-generated method stub
		return notAvailableSlotForm;
	}
	
	public String delete() throws Exception{
		
	/*	if(!verifyLogin(request)){
			return "login";
		}
		
		
		//int id = notAvailableSlotForm.getId();
		Connection connection = null;
		try{
			
			connection = Connection_provider.getconnection();
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			
			int del = notAvailableSlotDAO.DeleteBlockedSlot(Integer.toString(notAvailableSlotForm.getId()));
			
			FinderDAO finderDAO = new JDBCFinderDAO(connection);
			
			String clientid = (String)session.getAttribute("clientid");
			String client = (String)session.getAttribute("client");
			notAvailableSlotForm.setClient(client);
			notAvailableSlotForm.setClientId(clientid);
			
			ArrayList<NotAvailableSlot>finderList = finderDAO.getFinderList(clientid);
			notAvailableSlotForm.setFinderList(finderList);
			
			addActionMessage("Appointment Deleted Successfully!!");*/
		
		
		if(!verifyLogin(request)){
			return "login";
		}
		String selectedid = Integer.toString(notAvailableSlotForm.getId());
		String cancelApmtNote = notAvailableSlotForm.getCancelApmtNote();
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
			int status = notAvailableSlotDAO.getStatus(selectedid,"opd");
			if(status==0){
				int treatmeId = notAvailableSlotDAO.getSelecedTreatmentEpisodeId(Integer.parseInt(selectedid));
				int usedSession = notAvailableSlotDAO.getUsedSession(treatmeId,selectedid);
				ArrayList<NotAvailableSlot>usedSessionList = new ArrayList<NotAvailableSlot>();
				usedSessionList = notAvailableSlotDAO.getUsedSessionList(treatmeId,usedSession);
				
				
				for(NotAvailableSlot n:usedSessionList){
					int updateAllPrevious = notAvailableSlotDAO.updateAllPrevious(n.getId(),treatmeId);
				}
				int updatesession = notAvailableSlotDAO.updateSessions(treatmeId);
			}
			NotAvailableSlot n = notAvailableSlotDAO.getApmtDetailsForLog(Integer.parseInt(selectedid));
			
			String commencingTemp = n.getCommencing();
			
			String apmtstatus = "Cancelled";
			
			int logsave = notAvailableSlotDAO.saveCancelApmtInLog(Integer.parseInt(selectedid),DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),time,loginInfo.getUserId(),n.getClientId(),commencingTemp,n.getSTime(),apmtstatus,cancelApmtNote,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),n);
			
			  //send sms
		    ClientDAO clientDAO = new JDBCClientDAO(connection);
		    NotAvailableSlot notAvailableSlot = notAvailableSlotDAO.getApmtDetailsForLog(Integer.parseInt(selectedid));
		    UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
		    Client clients = clientDAO.getClientDetails(notAvailableSlot.getClientId());
		    UserProfile userProfile  = userProfileDAO.getUserprofileDetails(notAvailableSlot.getDiaryUserId());
		   
		    String message = AllTemplateAction.getDeletedAppointmentSMSText(notAvailableSlot.getClientId(), Integer.parseInt(selectedid), connection, loginInfo);
		    
		    if(loginInfo.getCountry().equals("India")){
		    	SmsService s = new SmsService();
		    	s.sendSms(message, clients.getMobNo(), loginInfo, new EmailLetterLog());
		    	s.sendSms(message, userProfile.getMobile(), loginInfo, new EmailLetterLog());
		    }
			 
			 
			int result = notAvailableSlotDAO.DeleteBlockedSlot(selectedid,"opd");
			
			notAvailableSlotForm.setMessage("Appointment Deleted Successfully!!");
			addActionMessage("Appointment Deleted Successfully!!");
			
			clientDAO = new JDBCClientDAO(connection);
			
			Client client = clientDAO.getSelectedSessionClientDetails(notAvailableSlotForm.getClientId());
			notAvailableSlotForm.setClient(client.getClientName());
			
			boolean isapmtexist = notAvailableSlotDAO.checkApmtExist(n.getClientId());
			if(isapmtexist){
				int sts = 1;
				int updp = notAvailableSlotDAO.updateNewPtsStatus(n.getClientId(),sts);
			}else{
				int sts = 0;
				int updp = notAvailableSlotDAO.updateNewPtsStatus(n.getClientId(),sts);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			connection.close();
		}
		
			
			
	
		
		
		
		return execute();
	}


	public void prepare() throws Exception {
		Connection connection = null;
		
		ArrayList<Client> thirdPartyTypeList = new ArrayList<Client>();
		ArrayList<Client> thirdPartyTypeNameList = new ArrayList<Client>();
		ArrayList<Client> clientOccupationList = new ArrayList<Client>();
		ArrayList<Client> refrenceList = new ArrayList<Client>();
		ArrayList<String> initialList = new ArrayList<String>();
		ArrayList<Client> sourceOfIntroList = new ArrayList<Client>();
		
		ArrayList<Client> condtitionList = new ArrayList<Client>();
		ArrayList<Client> gpList = new ArrayList<Client>();
		ArrayList<Client> surgeryList = new ArrayList<Client>();
		try{
			
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			
			gpList = clientDAO.getGpList();
			notAvailableSlotForm.setGpList(gpList);
			condtitionList = clientDAO.getTreatmentTypeList();
			notAvailableSlotForm.setCondtitionList(condtitionList);
			
			
			thirdPartyTypeList = clientDAO.getThirdPartyType();
			notAvailableSlotForm.setThirdPartyTypeList(thirdPartyTypeList);
			
			thirdPartyTypeNameList = clientDAO.getThirdPartyTypeName();
			notAvailableSlotForm.setThirdPartyTypeNameList(thirdPartyTypeNameList);
			
			clientOccupationList = clientDAO.getOccupationList();
			
			Client client = new Client();
			client.getOther();
			clientOccupationList.add(client);
			notAvailableSlotForm.setClientOccupationList(clientOccupationList);
			
			surgeryList = clientDAO.getSurgeryList();
			Client client3 = new Client();
			client3.getOther();
			surgeryList.add(client3);
			notAvailableSlotForm.setSurgeryList(surgeryList);

			
			refrenceList = clientDAO.getReferenceList();
			Client client2 = new Client();
			client2.getOther();
			refrenceList.add(client2);
			notAvailableSlotForm.setRefrenceList(refrenceList);
			
			initialList = clientDAO.getInitialList();
			notAvailableSlotForm.setInitialList(initialList);
			
			sourceOfIntroList = clientDAO.getSourceOfIntroList();
			notAvailableSlotForm.setSourceOfIntroList(sourceOfIntroList);
			
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			
			ArrayList<Location>locationList = notAvailableSlotDAO.getLocationList(loginInfo.getId());
			notAvailableSlotForm.setLocationList(locationList);
			
			ArrayList<AppointmentType>appointmentTypeList = notAvailableSlotDAO.getAppointmentTypeList();
			notAvailableSlotForm.setAppointmentTypeList(appointmentTypeList);
			
			TreatmentEpisodeDAO treatmentEpisodeDAO = new JDBCTreatmentEpisode(connection);
			ArrayList<TreatmentEpisode> sourceOfReferralList = treatmentEpisodeDAO.getSourceOfReferralList();
			notAvailableSlotForm.setSourceOfReferralList(sourceOfReferralList);
			
			notAvailableSlotForm.setCountry("United Kingdom");
			
			
			notAvailableSlotForm.setCountryList(PopulateList.countryList());
			
			
			notAvailableSlotForm.setStartTimeList(PopulateList.startTimeList());
			notAvailableSlotForm.setEndTimeList(PopulateList.endTimeList());
			notAvailableSlotForm.setApmtDurationList(PopulateList.apmtDurationList());
			notAvailableSlotForm.setApmtBlockDurationList(PopulateList.apmBlocktDurationList());
			
			ArrayList<DiaryManagement>userList = notAvailableSlotDAO.getUserList(loginInfo.getId());
			notAvailableSlotForm.setUserList(userList);
			
			ArrayList<String>weekNameList = new ArrayList<String>();
			weekNameList.add("Monday");
			weekNameList.add("Tuesday");
			weekNameList.add("Wednesday");
			weekNameList.add("Thursday");
			weekNameList.add("Friday");
			weekNameList.add("Saturday");
			weekNameList.add("Sunday");
			
			notAvailableSlotForm.setWeekNameList(weekNameList);
			
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			connection.close();
		}
		
		

		
	}
}
