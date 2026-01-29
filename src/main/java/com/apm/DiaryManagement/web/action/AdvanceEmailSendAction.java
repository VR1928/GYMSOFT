package com.apm.DiaryManagement.web.action;

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
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.apm.Accounts.eu.bi.AccountsDAO;
import com.apm.Accounts.eu.bi.ChargesAccountProcessingDAO;
import com.apm.Accounts.eu.blogic.jdbc.JDBCAccountsDAO;
import com.apm.Accounts.eu.blogic.jdbc.JDBCChargeAccountProcesDAO;
import com.apm.Accounts.web.action.ChargeAccountProcessingAction;
import com.apm.Appointment.eu.bi.AppointmentTypeDAO;
import com.apm.Appointment.eu.blogic.jdbc.JDBCAppointmentTypeDAO;
import com.apm.Appointment.eu.entity.Appointment;
import com.apm.Appointment.eu.entity.AppointmentType;
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
import com.apm.Inventory.eu.bi.InventoryProductDAO;
import com.apm.Inventory.eu.blogic.jdbc.JDBCInventoryProductDAO;
import com.apm.Inventory.eu.entity.Product;
import com.apm.Ipd.eu.bi.BedDao;
import com.apm.Ipd.eu.bi.IpdDAO;
import com.apm.Ipd.eu.blogic.jdbc.JDBCBedDao;
import com.apm.Ipd.eu.blogic.jdbc.JDBCIpdDAO;
import com.apm.Ipd.eu.entity.Bed;
import com.apm.Log.eu.entity.EmailLetterLog;
import com.apm.Master.eu.entity.Master;
import com.apm.Mis.eu.bi.MisChartDAO;
import com.apm.Mis.eu.blogic.jdbc.JDBCMisChartDAO;
import com.apm.Registration.eu.bi.ClinicDAO;
import com.apm.Registration.eu.bi.UserProfileDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCClinicDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCUserProfileDAO;
import com.apm.Registration.eu.entity.Clinic;
import com.apm.Registration.eu.entity.Location;
import com.apm.Registration.eu.entity.UserProfile;
import com.apm.Tools.web.action.AllTemplateAction;
import com.apm.common.eu.blogic.jdbc.Connection_provider;
import com.apm.common.utils.DateTimeUtils;
import com.apm.common.web.action.BaseAction;
import com.apm.common.web.common.helper.LoginHelper;
import com.apm.common.web.common.helper.LoginInfo;
import com.apm.main.common.constants.Constants;

import com.opensymphony.xwork2.ActionContext;

public class AdvanceEmailSendAction  extends BaseAction{
	
	
	HttpServletRequest request = ServletActionContext.getRequest();
//	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
	HttpSession session = request.getSession(true);
	
	String result = "";
	
	
	
	@Override
	public String execute() throws Exception {
		/*for(int i=0;i<50;i++)

		{
			
			//EmbeddedImageEmailUtil.sendMailForForgotPassword("manojmishra443@gmail.com", "", "hello world", "hello world");

		System.out.println(i);

		}*/
		
		
		if(session.getAttribute("logininfo")!=null){
				
			
			/*for(int i=0;i<100;i++)

			{
				
				EmbeddedImageEmailUtil.sendMailForForgotPassword("manojmishra443@gmail.com", "", "hello world", "hello world");

			System.out.println(i);

			}*/
			
			
			Connection connection = null;
			LoginInfo loginInfo =  (LoginInfo)session.getAttribute("logininfo");
			try{
				
						
				
				AllTemplateAction allTemplateAction = new AllTemplateAction();
				
				connection = getconnection(loginInfo.getDbName());
				NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
				
				int duration = notAvailableSlotDAO.getAdDurationData(loginInfo.getClinicid());
				
				if(duration!=0){
					DiaryManagementDAO diaryManagementDAO = new JDBCDiaryManagentDAO(connection);
					//get remainder date
					
					String date = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
					
					String temp[] = date.split(" ");
					
					String dt = temp[0];
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Calendar c = Calendar.getInstance();
					c.setTime(sdf.parse(dt));
					
					
					
					c.add(Calendar.DATE, duration);  // number of days to add
					dt = sdf.format(c.getTime());  // dt is now the new date
					
					c.add(Calendar.DATE, -duration);
					String emailsenddate = sdf.format(c.getTime());
					System.out.println(emailsenddate);
					
					ArrayList<String>apmtidList = notAvailableSlotDAO.getRemainderAppointmentList(dt);
					
					if(apmtidList.size()>0){
						for(String appointmentid : apmtidList ){
							ArrayList<NotAvailableSlot>appointmentList = diaryManagementDAO.getAppintmentSendMailDetails(Integer.parseInt(appointmentid));
							
							for(NotAvailableSlot notAvailableSlot : appointmentList){
								UserProfile userProfile = notAvailableSlot.getUserDEtails();
								Client client = notAvailableSlot.getClientDetails();
								Location location = notAvailableSlot.getLocationDetails();
								
								
								
								
								if(emailsenddate.equals(temp[0]) && notAvailableSlot.getAd()==0)	{
									allTemplateAction.emailSend(userProfile.getFullname(), client.getFirstName(), notAvailableSlot.getSTime(), notAvailableSlot.getDuration(), userProfile.getEmail(), client.getEmail(), notAvailableSlot.getCommencing(), location.getLocationname(),location.getContactNo(),location.getAddress(),location.getLocationid(),notAvailableSlot.getClientId(),userProfile.getQualification(),Integer.parseInt(appointmentid),userProfile.getDiciplineName(),connection,loginInfo);
									
									int upd = notAvailableSlotDAO.updateAD(appointmentid);
									
									  //send sms
									
								    ClientDAO clientDAO = new JDBCClientDAO(connection);
								  //  NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
								    NotAvailableSlot notAvailableSlot1 = notAvailableSlotDAO.getApmtDetailsForLog(Integer.parseInt(appointmentid));
								    UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
								    Client clients = clientDAO.getClientDetails(notAvailableSlot1.getClientId());
								    UserProfile userProfiles  = userProfileDAO.getUserprofileDetails(notAvailableSlot1.getDiaryUserId());
								   
								    String message = AllTemplateAction.getAppointmentSMSText(notAvailableSlot1.getClientId(), Integer.parseInt(appointmentid), connection, loginInfo);
								    
								    if(loginInfo.getCountry().equals("India")){
								    	SmsService s = new SmsService();
								    	s.sendSms(message, clients.getMobNo(), loginInfo, new EmailLetterLog());
//								    	s.sendSms(message, userProfiles.getMobile(), loginInfo, new EmailLetterLog());
								    }
								    
								}
								
							}
						}
						
					}
				}
				
			
				
				BedDao bedDao=new JDBCBedDao(connection);
				ClientDAO clientDAO= new JDBCClientDAO(connection);
				CompleteAptmDAO completeAptmDAO= new JDBCCompleteAptmDAO(connection);
				AppointmentTypeDAO appointmentTypeDAO= new JDBCAppointmentTypeDAO(connection);
				// for auto std charge setup 
				
				String date1= DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
				String datetime = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
			   
		/*		try {
					
					ArrayList<Bed> ipdPatientStdSetupList= bedDao.getAllIpdStdPatientList();
					for(Bed bed :ipdPatientStdSetupList){
						
						
						CompleteAppointment appointment=new CompleteAppointment();
		    			appointment.setClientId(bed.getClientid());
		    			appointment.setPractitionerId(bed.getPractitionerid());
		    			appointment.setChargeType("Charge");
		    			appointment.setLocation("1");
		    		    appointment.setAdditionalcharge_id("001");
		    		    appointment.setIpdid(bed.getId());
		    		    appointment.setInvoiceDate(date1);
		    		    appointment.setIpd("1");
		    		    appointment.setAppointmentid("0");
		    		    appointment.setGinvstid("0");
		    		    appointment.setWardid(bed.getWardid());
		    		    
		    		    Client client= clientDAO.getClientDetails(bed.getClientid());
		    		    
		    		    if(client.getWhopay()!=null){
		    		    	
		    		    	if(client.getWhopay().equals("Self") || client.getWhopay().equals("Client")){
		    		    	       
		    		    		appointment.setPolicyExcess("0");
		    		    		appointment.setPayBuy("0");
		    		    	} else {
		    		    		appointment.setPolicyExcess("1");
		    		    		appointment.setPayBuy("1");
		    		    	}
		    		    }
		    		    
		    		    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    		    Calendar calendar=Calendar.getInstance();
		    		    Date date= f.parse(bed.getAdmissiondate());
		    		    calendar.setTime(date);
		    		    calendar.add(Calendar.DATE, 1);
		    		    
		    		    String nowdateString = f.format(calendar.getTime());
		    		    int res= datetime.compareTo(nowdateString);
		    		    int invoiceid=0;
		    		    if(res==0 || res>0) {   //nowdate is greater or equal to another
		    		    	
		    		    	ArrayList<Master> chargeList=appointmentTypeDAO.getStandardChargeList(bed.getWardid(), bed.getTpid(), client.getWhopay());
		    		    	
		    		    	if(chargeList.size()!=0){
		    		    		invoiceid=completeAptmDAO.saveStndCharge(appointment.getClientId(),bed.getIpdid(), "0");
			        		    invoiceid=completeAptmDAO.saveAmpmInvoice(appointment,loginInfo.getId());
			        		    res =bedDao.updateStdChargeSetup(bed.getIpdid());
		    		    	}
		        		    
		        		    String nowDate=DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
		        			String stddate= bed.getAdmissiondate().split(" ")[0];
		        			long diff= DateTimeUtils.getDifferenceOfTwoDateDBFormat(stddate, nowDate);
		        			int qty =(int) diff;
		        			if(qty<0){
		        				 qty=0;
		        			}
		        			if(qty==0){
		        				qty=1;
		        			} else {
		        				//qty++;
		        			}
		        		    
		        		    String fullname=client.getTitle()+" "+client.getFirstName()+" "+client.getLastName();
		        		    appointment.setUser(fullname);
		        		    appointment.setCommencing(date1);    
		        		    
		        		    for(Master master:chargeList){
		      		    	  
		     		    	   String chargeId=String.valueOf(master.getId());
		     		    	   appointment.setApmtType(master.getName());
		     		    	   appointment.setCharges(master.getCharge());
		     		    	   appointment.setAdditionalcharge_id(chargeId);
		     		    	   appointment.setMasterchargetype("Bed Charge");
		     		    	   //appointment.setMasterchargetype("Accommodation Charges");
		     		    	   appointment.setQuantity(qty);
		     		    	   appointment.setStdflag("1");
		     		    	   res=completeAptmDAO.saveInvoiceAssesment(appointment, invoiceid);
		         		       int result= appointmentTypeDAO.saveStdCharge(bed.getIpdid(),chargeId,res,"1",datetime,"");
		     		    	   
		     		       }
		        		    
		    		    }
		    		    
						
					}
					
					
					
				} catch (Exception e) {

				} */
			
				
				
				
				
				
				
				
				
				
				
				
				
			
				

				//for auto charge
			//	ClinicDAO clinicDAO=new JDBCClinicDAO(connection);
				
				ArrayList<Bed> bedlist=bedDao.getAllIpdActivePatients();
				String date = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
				String stemp[] = date.split(" ");
				
				
				String stime[]=stemp[1].split(":");
				String time=stemp[1];
				                  				
				date =stemp[0];
				
				String nowdate= DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
				for(Bed bed:bedlist) {
					
					 /*if(bed.getClientid().equals("16036")){
							System.out.println("hello"); 
						 }*/
					    // if autocharges is on
					    boolean isAutochargeOn= completeAptmDAO.isAutoChargedOn(bed.getClientid());
					   // isAutochargeOn = false;
							if(isAutochargeOn){
						
								
							// All Bed charges and Standard changes will be fetched of active ipd patients

							ArrayList<CompleteAppointment> invoicelist=completeAptmDAO.getListofActiveCharges(bed.getId(),bed.getClientid(),loginInfo);

							//ArrayList<CompleteAppointment> invoicelist=completeAptmDAO.getListofActiveCharges(bed.getId(),bed.getClientid());

							//ArrayList<CompleteAppointment> invoicelist =completeAptmDAO.getActiveStandardCharges(bed.getId()); 
								
								if(invoicelist.size()!=0){
								 
								 	 for(CompleteAppointment appointment:invoicelist) {
								 		
								 			   
								 		  		int res=0;
								 			  // String autoChargeTIme=clinicDAO.getAutoChargeTime(String.valueOf(loginInfo.getClinicid()));
								 			   //int res=autoChargeTIme.compareTo(time);
								 			   
								 			   AppointmentType appointmentType= appointmentTypeDAO.getMasterCharge(appointment.getAdditionalcharge_id());
								 			   
								 			   try {
												   //parse and compare the time 
								 				   String stdDate= appointment.getCommencing();
								 				   String stdTime= appointment.getStartTime();
								 				   if(stdTime!=null){
								 					   if(stdTime.equals("")){
								 						   stdTime="00:00:00"; 
								 					   }
								 				   } else {
								 					   stdTime="00:00:00";
								 				   }
								 				   String dateTime= stdDate+" "+stdTime;
								 				   
								 				   SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								 				   Date date2 =f.parse(dateTime);
								 				   Calendar calendar=Calendar.getInstance();
								 				   calendar.setTime(date2);
								 				   int addedHours= Integer.parseInt(appointmentType.getHours());
								 				   if(appointment.getStdflag().equals("1")){
								 					   
								 					    if(addedHours>=24){
								 					    	
								 					    }else {
								 					    	calendar.add(Calendar.DATE, 1);
								 					    }
								 					    
								 				   }
								 				    
								 				   calendar.add(Calendar.HOUR, addedHours);
								 				   
								 				   String stdDateTime= f.format(calendar.getTime());
								 				   
								 				   res =nowdate.compareTo(stdDateTime);
								 				   
								 				 
								 				  
									 			   if(res==0 || res>0) { //if date is equal or greater than
		
									 				   	 if(appointmentType.getRatio()==null){
									 				   		 appointmentType.setRatio("0");
									 				   	 }
									 				     int ratio= Integer.parseInt(appointmentType.getRatio());
									 				     
									 				     // if ratio is zero means full charge
									 				     
									 				    String logcommencing = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
								 						
								 						String templ[] = bed.getAdmissiondate().split(" ");
								 						String ldate1 = DateTimeUtils.getCommencingDate1(templ[0]);
								 						String times[] = templ[1].split(":");
								 						String hh = times[0];
								 						String mm = times[1];
								 						
								 						//String loglastmodified = logcommencing + " " + hh + ":" + mm + ":20" ;
								 						String loglastmodified = DateTimeUtils.getCommencingDate1(bed.getAdmissiondate()) + " " + hh + ":" + mm + ":20" ;
								 						String cutofftime = bedDao.getHospitalCutoffTime(loginInfo.getClinicid());
								 			            if(!cutofftime.equals("0")){
								 			            	loglastmodified = logcommencing + " " + cutofftime + ":20" ;
								 			            }
								 			            
								 			            
								 			            
								 			            
								 			            //compare cutoff time
								 			            
								 			        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
								 			  		String aDateString = logcommencing + " "+hh+":"+mm+":00.0";
								 			  		if(!cutofftime.equals("0")){
								 			  			aDateString = logcommencing + " "+cutofftime+":00.0";
								 			  		}
								 			  		Date dateee = sdf.parse(aDateString);
								 			  		System.out.println("reference date:"+dateee);

								 			  		Calendar cal = Calendar.getInstance();
								 			  		cal.setTime(dateee);
								 			  		//cal.add(Calendar.HOUR, 36);
								 			  		System.out.println("added one and half days to reference date: "+cal.getTime());
								 			  		
								 			  		SimpleDateFormat fs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
								 					fs.setTimeZone(TimeZone.getTimeZone(loginInfo.getTimeZone()));
								 					System.out.println(fs.format(GregorianCalendar.getInstance().getTime()));

								 					String newDateString = fs.format(GregorianCalendar.getInstance().getTime());
								 			  		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
								 			  		Date newDate = sdf.parse(newDateString);
								 			  		System.out.println("new date to compare with reference date : "+newDate);

								 			  		Calendar newCal = Calendar.getInstance();
								 			  		newCal.setTime(newDate);
								 			  		
								 			  		if(!cal.after(newCal)){
								 			  		    System.out.println("date is valid");
								 			  		}
								 			  		//Akash 07 Aug 2018 auto charge check for already charge apply or not
								 			  		boolean flag = completeAptmDAO.checkAutoChargeAppliedStatus(bed.getWardid(),bed.getBedid(),bed.getId(),logcommencing);
								 			  		
								 			  		if(!flag){
								 			  			//if false then not available so apply auto charge
								 			  			
								 			  			if(ratio==0){   
									 				    	/* String sdate = completeAptmDAO.getIpdLogLastCommencing(bed.getId());
									 				    	String stempp[] = sdate.split(" ");
									 						sdate = DateTimeUtils.getCommencingDate1(stempp[0]) + " " + stempp[1];*/
									 						
									 						String edate = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
									 						String temp[] = edate.split(" ");
									 						edate = DateTimeUtils.getCommencingDate1(temp[0]) + " " + temp[1];
									 				    	 
									 				    	/*int qty1 =(int)DateTimeUtils.getDifferanceofDateWithTime(sdate, edate, Integer.parseInt(appointmentType.getHours()));
									 				    	int result=completeAptmDAO.updateAutocharge(appointment.getId(),stempp[0],bed.getWardid(),time,qty1);*/
									 						
									 						//save new charge for new day
									 						
									 															 			  		
									 						if(!cal.after(newCal)){
									 			            
									 						appointment.setAppointmentid("0");
									 						appointment.setChargeType("Charge");
											    			appointment.setLocation("1");
											    			appointment.setIpd("1");
									 						 int invoiceid=completeAptmDAO.saveAmpmInvoice(appointment,loginInfo.getId(),loginInfo.getUserId());
									 						 
									 						
									 						 String logshifteddate = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
								 					            int log1 = bedDao.saveBedChangeLog(bed,loglastmodified,bed.getId(),logcommencing,logshifteddate,1);
									 						 
									 						 Client client = clientDAO.getClientDetails(bed.getClientid());
									 						
									 						 
									 						ArrayList<Master> chargeList=appointmentTypeDAO.getBedShiftingStandardChargeList(bed.getWardid(), client.getTypeName(), client.getWhopay(),loginInfo);
									 						 
									 						 for(Master m1:chargeList){
									 					    	  
									 					    	  appointment.setCommencing(temp[0]); 
									 					    	   appointment.setApmtType(m1.getName());
									 					    	   appointment.setCharges(m1.getCharge());
									 					    	   appointment.setAdditionalcharge_id(String.valueOf(m1.getId()));
									 					    	   appointment.setMasterchargetype("Bed Charge");
									 					    	   if(loginInfo.getIskunal()==1){
									 					    		   appointment.setMasterchargetype(m1.getMasterchargetype());
									 					    	   }
									 					    	  //appointment.setMasterchargetype("Accommodation Charges");
									 					    	  
									 					          // Bed bed2 = appointmentTypeDAO.getLogwardId(m1.getDate(),bed.getId());
									 					    	   
									 					    	   appointment.setWardid(bed.getWardid());
									 					    	   appointment.setBedid(bed.getBedid());
									 					    	   appointment.setIpdid(bed.getId());
									 					    	   appointment.setLogid(Integer.toString(log1));
									 					    	   
									 					    	 
									 					    	

									 					    	   
									 				    	   		appointment.setQuantity(1);

									 					    	    res=completeAptmDAO.saveInvoiceAssesment(appointment, invoiceid);
									 						 }  
									 					    }
									 				     }
									 				     else { 
									 				    	  // else half charges
									 				    	 
									 				    	if(!cal.after(newCal)){
									 				    	 CompleteAppointment completeAppointment=completeAptmDAO.getInvoiceAsessmentDetails(appointment.getId());
									 				    	 completeAppointment.setStartTime(time);
									 				    	 completeAppointment.setCommencing(date);
									 				    	 int chargeAmt= Integer.parseInt(appointmentType.getCharges());
									 				    	 //update half qty
									 				    	 int amt= chargeAmt/2;
									 				    	 int result= completeAptmDAO.getHalfInvoiceAssesmentIfExist(appointment.getAdditionalcharge_id(),bed.getIpdid(),loginInfo);
									 				    	 if(result==0){
									 				    		 result=completeAptmDAO.saveInvoiceAssementWithHalf(completeAppointment,amt);
									 				    		 int temp=completeAptmDAO.updateAutochargeDate(appointment.getId(),date,bed.getWardid(),time);
									 				    	 } else {
									 				    		 
									 				    		 String sdate = completeAptmDAO.getIpdLogLastCommencing(bed.getId());
											 				    	String stempp[] = sdate.split(" ");
											 						sdate = DateTimeUtils.getCommencingDate1(stempp[0]) + " " + stempp[1];
											 						
											 						String edate = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
											 						String temp[] = edate.split(" ");
											 						edate = DateTimeUtils.getCommencingDate1(temp[0]) + " " + temp[1];
											 				    	 
											 				    	int qty1 =(int)DateTimeUtils.getDifferanceofDateWithTime(sdate, edate, Integer.parseInt(appointmentType.getHours()));
									 				    		result=completeAptmDAO.updateAutocharge(result,stempp[0],bed.getWardid(),time,qty1);
									 				    	 }
									 				    	 
									 				    	}
									 				    	 
									 				     }
								 			  		}
								 			  	   }
								 				   
								 			   } catch (Exception e) {
		
								 				   //e.printStackTrace();
								 			   }
								 			  
								 		 
								 	 }
							 }
						
							} 
				}
				
				
				
				
				
				
				
				
			/*	if(loginInfo.getUserType()==2){
					return "gotodashboard";
				}
				
				
				
				if(loginInfo.getUserType()==4){
					return "gotoweekdashboard";
				}
				*/
				
				//Ipd bed log
				IpdDAO ipdDAO = new JDBCIpdDAO(connection);
				MisChartDAO misChartDAO = new JDBCMisChartDAO(connection);
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar cal = Calendar.getInstance();
				String todate = dateFormat.format(cal.getTime());
				todate = DateTimeUtils.getCommencingDate1(todate);
					
				Calendar cal1 = Calendar.getInstance();
				cal1.add(Calendar.DATE, -1);
				String newdate = dateFormat.format(cal1.getTime());
				newdate = DateTimeUtils.getCommencingDate1(newdate);
				Boolean flag = ipdDAO.checkInhousePatientExists(newdate);
				String ipdids="";
				if(!flag){
					int ipdOldPatient = misChartDAO.getInHousePatients(todate, todate);
					int ipdnewadmission = misChartDAO.getIpdNewAdmission(todate, todate);
					int dischargepatient = misChartDAO.getDischargePatients(newdate, newdate);
					int total = dischargepatient+ipdOldPatient-ipdnewadmission;
					int totalbed1 = misChartDAO.getAvailableBed(date,date);
					
					ArrayList<String> ipdOldPatientlist =  misChartDAO.getInHousePatientList(todate, todate);
					ArrayList<String> ipdnewadmissionlist = misChartDAO.getIpdNewAdmissionList(todate, todate);
					//ipdids =misChartDAO.getDischargePatientList(newdate, newdate);
					ArrayList<String> ipddischargelist = misChartDAO.getDischargePatientList1(newdate, newdate);
					/*for (String string : ipdOldPatientlist) {
						boolean flag1 = false;
						for (String string1 : ipdnewadmissionlist) {
							if(string.equals(string1)){
								flag1=true;
								break;
							}
						}
						if(!flag1){
							if(ipdids!=null){
								if(ipdids.equals("")){
									ipdids = string;
								}else{
									ipdids = ipdids+","+string;
								}
							}else{
								ipdids = string;
							}
						}
					}*/
					ipdOldPatientlist.removeAll(ipdnewadmissionlist);
					ipdOldPatientlist.addAll(ipddischargelist);
					for (String string : ipdOldPatientlist) {
						if(ipdids!=null){
							if(ipdids.equals("")){
								ipdids = string;
							}else{
								ipdids =ipdids+","+string;
							}
						}
					}
					int res = ipdDAO.saveInhousePatient(total,newdate,totalbed1,ipdids);
				}	
				//Akash save department wise and ward wise count 05 Oct 2018
				boolean iswardwisedatastore =  ipdDAO.checkWardWiseDataStored(newdate);
				if(!iswardwisedatastore){
					if(ipdids!=null){
						if(ipdids.equalsIgnoreCase("")){
							ArrayList<String> ipdOldPatientlist =  misChartDAO.getInHousePatientList(todate, todate);
							ArrayList<String> ipdnewadmissionlist = misChartDAO.getIpdNewAdmissionList(todate, todate);
							ArrayList<String> ipddischargelist = misChartDAO.getDischargePatientList1(newdate, newdate);
							ipdOldPatientlist.removeAll(ipdnewadmissionlist);
							ipdOldPatientlist.addAll(ipddischargelist);
							for (String string : ipdOldPatientlist) {
								if(ipdids!=null){
									if(ipdids.equals("")){
										ipdids = string;
									}else{
										ipdids =ipdids+","+string;
									}
								}
							}
						}
					}else{
						ArrayList<String> ipdOldPatientlist =  misChartDAO.getInHousePatientList(todate, todate);
						ArrayList<String> ipdnewadmissionlist = misChartDAO.getIpdNewAdmissionList(todate, todate);
						ArrayList<String> ipddischargelist = misChartDAO.getDischargePatientList1(newdate, newdate);
						ipdOldPatientlist.removeAll(ipdnewadmissionlist);
						ipdOldPatientlist.addAll(ipddischargelist);
						for (String string : ipdOldPatientlist) {
							if(ipdids!=null){
								if(ipdids.equals("")){
									ipdids = string;
								}else{
									ipdids =ipdids+","+string;
								}
							}
						}
					}
					ArrayList<String> wardlist = ipdDAO.getWardListFromIpdids(ipdids);
					for (String wardid: wardlist) {
						int total = misChartDAO.getInHousePatientsWardWise(todate, todate,ipdids,wardid);
						int totalbed1 = misChartDAO.getAvailableBedWardWise(date,date,ipdids,wardid);
						String ipdwardwiseids = misChartDAO.getInHousePatientsWardWiseIds(todate, todate,ipdids,wardid);
						int res = ipdDAO.saveInhousePatientWardWise(total,newdate,totalbed1,ipdwardwiseids,wardid);
					}
				}
				boolean ispractwisedatastore =  ipdDAO.checkPractWiseDataStored(newdate);
				
				if(!ispractwisedatastore){
					if(ipdids!=null){
						if(ipdids.equalsIgnoreCase("")){
							ArrayList<String> ipdOldPatientlist =  misChartDAO.getInHousePatientList(todate, todate);
							ArrayList<String> ipdnewadmissionlist = misChartDAO.getIpdNewAdmissionList(todate, todate);
							ArrayList<String> ipddischargelist = misChartDAO.getDischargePatientList1(newdate, newdate);
							ipdOldPatientlist.removeAll(ipdnewadmissionlist);
							ipdOldPatientlist.addAll(ipddischargelist);
							for (String string : ipdOldPatientlist) {
								if(ipdids!=null){
									if(ipdids.equals("")){
										ipdids = string;
									}else{
										ipdids =ipdids+","+string;
									}
								}
							}
						}
					}else{
						ArrayList<String> ipdOldPatientlist =  misChartDAO.getInHousePatientList(todate, todate);
						ArrayList<String> ipdnewadmissionlist = misChartDAO.getIpdNewAdmissionList(todate, todate);
						ArrayList<String> ipddischargelist = misChartDAO.getDischargePatientList1(newdate, newdate);
						ipdOldPatientlist.removeAll(ipdnewadmissionlist);
						ipdOldPatientlist.addAll(ipddischargelist);
						for (String string : ipdOldPatientlist) {
							if(ipdids!=null){
								if(ipdids.equals("")){
									ipdids = string;
								}else{
									ipdids =ipdids+","+string;
								}
							}
						}
					}
					ArrayList<String> practlist = ipdDAO.getPractListFromIpdids(ipdids);
					for (String practid: practlist) {
						int total = misChartDAO.getInHousePatientsPractWise(todate, todate,ipdids,practid);
						String ipdwardwiseids = misChartDAO.getInHousePatientsPractWiseIds(todate, todate,ipdids,practid);
						int res = ipdDAO.saveInhousePatientPractWise(total,newdate,ipdwardwiseids);
					}
					
				}
				
			if(loginInfo.isShow_unpost()==true){
				ChargesAccountProcessingDAO chargesAccountProcessingDAO = new JDBCChargeAccountProcesDAO(connection);
				DateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal3 = Calendar.getInstance();
				cal3.add(Calendar.DATE, -2); 
				String date4= dateFormat3.format(cal3.getTime());
				String podate = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
				int res = chargesAccountProcessingDAO.updateInvoicePost(date4,loginInfo.getUserId(),podate);
			}
				
				
			if(loginInfo.isBdaysms()){	
			//lokesh bdaysms
				DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
				Calendar cal2 = Calendar.getInstance();
				
				String Date1 = dateFormat2.format(cal2.getTime());
				String dob1[]= Date1.split("/");
				String dob= dob1[0]+"/"+dob1[1]+"/";		
				ArrayList<Client> bdaylist= clientDAO.getbdaylistPatients(dob, loginInfo);
				}	
		
			
			if(true){
				DateFormat dateFormat3 = new SimpleDateFormat("dd-MM-yyyy");
				Calendar cal3 = Calendar.getInstance();
				cal3.add(Calendar.DATE, 1); 
				 SmsService s= new SmsService();
				String Date1= dateFormat3.format(cal3.getTime());
				 ArrayList<Client> followuplist=clientDAO.getallfollowupsToDash("", "", Date1, Date1);
				 for(Client client:followuplist){
					 if(client.getState().equals("0")){
						 String message="Your Follow up is Scheduled on "+Date1+" by "+client.getDiaryUser()+"";
							s.sendvaccineSms(message, client.getMobNo(), loginInfo, new EmailLetterLog(),connection);
							clientDAO.setfollowupsmsflag(""+client.getId());
					 }
				 }
			}
			
			UserProfileDAO userProfileDAO= new JDBCUserProfileDAO(connection);
			if(userProfileDAO.getWeekVaccineAccess()){
				DateFormat dateFormat3 = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal3 = Calendar.getInstance();
				cal3.add(Calendar.DATE, 7); 
				String Date1= dateFormat3.format(cal3.getTime());
				notAvailableSlotDAO.getAllClientVaccinations(Date1, loginInfo);
			}
				
			//17 August 2018 Save inventory 
			InventoryProductDAO inventoryProductDAO = new JDBCInventoryProductDAO(connection);
			String todaydate=DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
			//10-08-2019 Picachu
			if(loginInfo.isStock_log()){
				if(loginInfo.getOpening_locations()!=null){
					for (String string : loginInfo.getOpening_locations().split(",")) {
						if(string.equals("0")){
							continue;
						}
						boolean flags = inventoryProductDAO.checkInventoryProductLog(todaydate,string);
						if(!flags){
							int res =inventoryProductDAO.saveInventoryStockLog(todaydate,string);
						}
					}
				}
			}
			
			//Akash 31-01-2019
			
			DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cal11 = Calendar.getInstance();
			cal11.add(Calendar.DATE, -1);
			String todates = dateFormat1.format(cal11.getTime());
			
			Calendar cal12 = Calendar.getInstance();
			cal12.add(Calendar.DATE, -2);
			String previousdate = dateFormat1.format(cal12.getTime());
			/*previousdate = DateTimeUtils.getCommencingDate1(previousdate);*/
		/*	boolean catdatastorestatus =  checkInventoryCatalogueStatus(previousdate,connection);*/
			if(loginInfo.isOpening_closeing_on()){
				for (String string : loginInfo.getOpening_locations().split(",")) {
					if(string.equals("0")){
						continue;
					}
					boolean flags = inventoryProductDAO.checkInventoryOpeningStockLog(todates,string);
					if(!flags){
						String location_filter = string;
						
						StringBuffer buffer = new StringBuffer();
						buffer.append("select inventory_catalogue.id from inventory_catalogue ");
						buffer.append("inner join inventory_product on inventory_product.catalogueid = inventory_catalogue.id  ");
						buffer.append("where procurement=0 and product_name is not null and inventory_product.location='"+location_filter+"'  ");
						buffer.append("group by inventory_catalogue.id order by inventory_catalogue.id  ");
						PreparedStatement ps=connection.prepareStatement(buffer.toString());
						ResultSet rs =ps.executeQuery();
						while(rs.next()){
							String catalogueid = rs.getString(1);
							double opeingstockvalue =0;
							int openingstock=0;
							double openingvalue =0;
							int qtyin=0;
							double qtyinvalue =0;
							int qtyout=0;
							double qtyoutvalue =0;
							double salevalue = 0;
							int closingstock=0;
							double closingvalue =0;
							Product product2 = inventoryProductDAO.getYesterdayClosingData(previousdate,catalogueid,location_filter);
							opeingstockvalue = product2.getTotalclosingvalue();
							openingstock = product2.getTotalclosingstock();
							openingvalue = product2.getClosing();
							
							boolean flagss = inventoryProductDAO.checkProductInProductLog(todates,catalogueid,todates);
							if(flagss){
								Product qtyin_product = inventoryProductDAO.getCatalogueProductIn(todates,catalogueid,string,todates);
								Product qtyin_product_update_pro = inventoryProductDAO.getCatalogueProductUpdateProIn(todates,catalogueid,string,todates); 
								
								qtyin = qtyin_product.getTotalqty() + qtyin_product_update_pro.getTotalqty();
								qtyinvalue = qtyin_product.getTotal_amount() + qtyin_product_update_pro.getTotal_amount();
								
								Product qtyout_product = inventoryProductDAO.getCatalogueProductOut(todates,catalogueid,string,todates,1);
								Product qtyout_product_update_pro = inventoryProductDAO.getCatalogueProductUpdateProOut(todates,catalogueid,string,todates);
								Product qtyout_product_sale = inventoryProductDAO.getCatalogueProductSaleOut(todates,catalogueid,string,todates);
								
								qtyout = qtyout_product.getTotalqty() + qtyout_product_update_pro.getTotalqty()+qtyout_product_sale.getTotalqty() ;
								qtyoutvalue = qtyout_product.getTotal_amount() + qtyout_product_update_pro.getTotal_amount()+qtyout_product_sale.getTotal_amount(); 
								salevalue = qtyout_product.getSalevalue() + qtyout_product_update_pro.getSalevalue()+qtyout_product_sale.getSalevalue(); 
							}
							closingstock=openingstock+qtyin-qtyout;
							closingvalue=openingvalue+qtyinvalue-qtyoutvalue;
							int unknownqty = 0;
							if((openingstock +qtyin)<qtyout){
								closingstock =0;
								closingvalue =0;
								unknownqty = qtyout - (openingstock +qtyin);
							}
							
							String sql1="insert into inventory_catalogue_log (lastmodfied, date, location, opening_stock, opeing_value, qty_in, qty_in_value, qty_out, qty_out_value, stock_value, Unknown_qty, closing_stock, closing_value, catalogueid) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
						 	PreparedStatement ps1=connection.prepareStatement(sql1);
						 	ps1.setString(1, todates);
						 	ps1.setString(2, todates);
						 	ps1.setString(3, location_filter);
						 	ps1.setString(4, ""+openingstock);
						 	ps1.setString(5, DateTimeUtils.changeFormat(Math.round(opeingstockvalue * 100.0)/100.0));
						 	ps1.setString(6, ""+qtyin);
						 	ps1.setString(7, DateTimeUtils.changeFormat(qtyinvalue));
						 	ps1.setString(8, ""+qtyout);
						 	ps1.setString(9, DateTimeUtils.changeFormat(Math.round(qtyoutvalue * 100.0)/100.0));
						 	ps1.setString(10, DateTimeUtils.changeFormat(Math.round(salevalue * 100.0)/100.0));
						 	ps1.setString(11, ""+unknownqty);
						 	ps1.setString(12, ""+closingstock);
						 	ps1.setString(13, DateTimeUtils.changeFormat(Math.round(closingvalue * 100.0)/100.0));
						 	ps1.setString(14, catalogueid);
						 	int res= ps1.executeUpdate();
							
						}
					}
				}
			}
			boolean catdatastorestatus =  true;
			if(!catdatastorestatus){/*
				try {
					String fromDate = previousdate;
					String toDate = previousdate;
					
					ArrayList<String> arrayList = new ArrayList<String>();
					arrayList.add("32");
					arrayList.add("33");
					arrayList.add("34");
					arrayList.add("35");
					arrayList.add("36");
				for (String string : arrayList) {
					String location_filter = string;
					StringBuffer buffer = new StringBuffer();
					buffer.append("select distinct inventory_catalogue.id from inventory_catalogue ");
					buffer.append("inner join inventory_product on inventory_product.catalogueid = inventory_catalogue.id  ");
					buffer.append("where procurement=0 and product_name is not null and inventory_product.location='"+location_filter+"' ");
					buffer.append("order by inventory_product.prodname ");
					PreparedStatement ps=connection.prepareStatement(buffer.toString());
					ResultSet rs =ps.executeQuery();
					while(rs.next()){
						Product product = new Product();
						
						String catalogueid = rs.getString(1);
						double opeingstockvalue =0;
						int openingstock=0;
						boolean checklastday = inventoryProductDAO.checkPreviousDataCatAvailable(previousdate,catalogueid,location_filter);
						if(checklastday){
							Product product2 = inventoryProductDAO.getYesterdayClosingData(previousdate,catalogueid,location_filter);
							opeingstockvalue = product2.getTotalclosingvalue();
							openingstock = product2.getTotalclosingstock();
						}else{
							//Opening- Purchase qty + return from patient + return to store - Patient sale - Return to supplier
							//- Consume -Direct transfer -Request transfer =before fromdate
							//Purchase qty =before fromdate this plus
							Product purbeforeproduct = inventoryProductDAO.getPuchaseProductData(catalogueid,fromDate,location_filter);  
							
							//Return from patient before fromdate this plus
							Product retpatientbeforeproduct = inventoryProductDAO.getReturnPatientProductData(catalogueid,fromDate,location_filter);
							
							//Patient Sale Before Fromdate this minus
							Product salebefore = inventoryProductDAO.getPatientSaleBeforeDate(catalogueid,fromDate,location_filter);
							
							//Return to supplier before fromdate this minus
							Product returntosuppplierbefore = inventoryProductDAO.getReturnToSupplierBeforeDate(catalogueid,fromDate,location_filter); 
							
							//Consume to user or patient this minus
							Product consumebefore = inventoryProductDAO.getConsumeBeforeDate(catalogueid,fromDate,location_filter);
							
							int directtransferout =0; 
							int requesttransferout =0;
							int retruntransferin =0;
							double returntransfervaluein =0;
							double directtransfervalueout =0;
							double requesttransfervalueout =0;
							
							int directtransferin =0;
							int requesttransferin =0;
							int returntransferout =0;
							double directtransfervaluein=0;
							double requesttransfervaluein=0;
							double returntransfervalueout=0;
							if(!location_filter.equals("0")){
								//Direct transfer out before fromdate
								Product directtransferproductout = inventoryProductDAO.getDirectTransferBefore(catalogueid,fromDate,location_filter);
								
								//Request Transfer out before fromdate
								Product requesttransferproductout = inventoryProductDAO.getRequestTransferBefore(catalogueid,fromDate,location_filter);
								
								//Return transfer in before fromdate
								Product returntransferproductin = inventoryProductDAO.getReturnTransferBefore(catalogueid,fromDate,location_filter);
								
								directtransferout = directtransferproductout.getTotalqty();
								requesttransferout = requesttransferproductout.getTotalqty();
								retruntransferin = returntransferproductin.getTotalqty();
								returntransfervaluein = returntransferproductin.getTotal_amount();
								directtransfervalueout = directtransferproductout.getTotal_amount();
								requesttransfervalueout = requesttransferproductout.getTotal_amount();
								//Direct transfer in before  fromdate
								Product directtransferproductin = inventoryProductDAO.getDirectTransferBeforeIn(catalogueid,fromDate,location_filter);
								
								//Request Transfer in before  fromdate
								Product requesttransferproductin = inventoryProductDAO.getRequestTransferBeforeIn(catalogueid,fromDate,location_filter);
								
								//Return transfer out before  fromdate
								Product returntransferproductout = inventoryProductDAO.getReturnTransferBeforeOut(catalogueid,fromDate,location_filter);
							
								directtransferin =directtransferproductin.getTotalqty();
								requesttransferin =requesttransferproductin.getTotalqty();
								returntransferout =returntransferproductout.getTotalqty();
								directtransfervaluein=directtransferproductin.getTotal_amount();
								requesttransfervaluein=requesttransferproductin.getTotal_amount();
								returntransfervalueout  = returntransferproductout.getTotal_amount();
							}
							
							Product adjustmentbeforout = inventoryProductDAO.getAdjustmentDataBeforeOut(catalogueid,fromDate,location_filter);
							
							Product adjustmentbeforin = inventoryProductDAO.getAdjustmentDataBeforeIn(catalogueid,fromDate,location_filter);
							
							//opening stock calculation
							//Opening= Purchase qty + return from patient + return to store - Patient sale - Return to supplier
							//- Consume -Direct transfer -Request transfer =before fromdate
							openingstock = purbeforeproduct.getTotalqty() + retpatientbeforeproduct.getTotalqty() 
									-salebefore.getTotalqty() - returntosuppplierbefore.getTotalqty() - consumebefore.getTotalqty() 
									- directtransferout - requesttransferout + retruntransferin 
									+ directtransferin + requesttransferin- returntransferout  - adjustmentbeforout.getTotalqty() + adjustmentbeforin.getTotalqty();
							
							if(openingstock<0){
								openingstock=0;
							}
							
							//Opening Stock Value - purchase qty pur value + return from patient sale price = Before Fromdate;
							opeingstockvalue = purbeforeproduct.getTotal_amount() + retpatientbeforeproduct.getTotal_amount() 
													- salebefore.getTotal_amount() - returntosuppplierbefore.getTotal_amount() - consumebefore.getTotal_amount()
													- directtransfervalueout - requesttransfervalueout + returntransfervaluein
													+ directtransfervaluein +requesttransfervaluein - returntransfervalueout - adjustmentbeforout.getTotal_amount() + adjustmentbeforin.getTotal_amount();
							
							if(opeingstockvalue<0){
								opeingstockvalue=0;
							}
						}
						
						
						
						//Qty in-  Purchase qty +  return from patient + retruntransfer  = in fromdate and todate
						
						//Purchase qty =  in fromdate and todate
						Product purproduct = inventoryProductDAO.getPuchaseProductDataBetween(catalogueid,fromDate,toDate,location_filter);  
						
						//return from patient =  in fromdate and todate
						Product returnpatientqty = inventoryProductDAO.getReturnPatientProductDataBetween(catalogueid,fromDate,toDate,location_filter);
						
						// retruntransfer = in fromdate and todate 
						int retruntransferqtyin =0;
						double retruntransferqtyinvalue=0;
						int directtransferqtyin=0;
						int requesttransferqtyin =0;
						double directtransferqtyinvalue=0;
						double requesttransferqtyinvalue=0;
						if(!location_filter.equals("0")){
							//Return transfer between date 
							Product returntransferproductin = inventoryProductDAO.getReturnTransferBetweenDate(catalogueid,fromDate,location_filter,toDate);
							retruntransferqtyin = returntransferproductin.getTotalqty();
							retruntransferqtyinvalue = returntransferproductin.getTotal_amount();
							
							//Direct transfer between 
							Product directtransferproductin = inventoryProductDAO.getDirectTransferBetweenDateIn(catalogueid,fromDate,location_filter,toDate);
							
							//Request Transfer between
							Product requesttransferproductin = inventoryProductDAO.getRequestTransferBetweenDateIn(catalogueid,fromDate,location_filter,toDate);
							
							directtransferqtyin = directtransferproductin.getTotalqty();
							requesttransferqtyin = requesttransferproductin.getTotalqty();
							directtransferqtyinvalue = directtransferproductin.getTotal_amount();
							requesttransferqtyinvalue = requesttransferproductin.getTotal_amount();
						}
						
						Product adjustmentbetweenin = inventoryProductDAO.getAdjustmentDataBetweenIn(catalogueid,fromDate,location_filter,toDate);
						
						int qtyin = purproduct.getTotalqty() + returnpatientqty.getTotalqty() 
									+retruntransferqtyin + directtransferqtyin +requesttransferqtyin + adjustmentbetweenin.getTotalqty() ;
						
						double qtyinvalue = purproduct.getTotal_amount() + returnpatientqty.getTotal_amount() 
									+retruntransferqtyinvalue + directtransferqtyinvalue +requesttransferqtyinvalue + adjustmentbetweenin.getTotal_amount() ;
						
						//Qty out - Patient sale + Return to Supplier + Consume + direct transfer + request return  = in Fromdate and Todate
						
						// retruntransfer = in fromdate and todate 
						int directtransferqtyout =0;
						double directtransferqtyoutvalue=0;
						int requesttransferqtyout =0;
						double requesttransferqtyoutvalue=0;
						
						int returntransferqtyout =0;
						double returntransferqtyoutvalue=0;
						
						double directsaleprice=0;
						double returnsaleprice=0;
						double requestsaleprice=0;
						
						if(!location_filter.equals("0")){
							//Direct transfer between 
							Product directtransferproductout = inventoryProductDAO.getDirectTransferBetweenDate(catalogueid,fromDate,location_filter,toDate);
							
							//Request Transfer between
							Product requesttransferproductout = inventoryProductDAO.getRequestTransferBetweenDate(catalogueid,fromDate,location_filter,toDate);
							
							directtransferqtyout = directtransferproductout.getTotalqty();
							requesttransferqtyout = requesttransferproductout.getTotalqty();
							directtransferqtyoutvalue = directtransferproductout.getTotal_amount();
							requesttransferqtyoutvalue = requesttransferproductout.getTotal_amount();
							
							//Return to store between
							Product returntransferproductin = inventoryProductDAO.getReturnTransferBetweenDateOut(catalogueid,fromDate,location_filter,toDate);
							returntransferqtyout = returntransferproductin.getTotalqty();
							returntransferqtyoutvalue = returntransferproductin.getTotal_amount();
							
							directsaleprice = directtransferproductout.getSalepricetotal();
							returnsaleprice = returntransferproductin.getSalepricetotal();
							requestsaleprice = requesttransferproductout.getSalepricetotal();
						}
						
						//Patient Sale between Fromdate and Todate
						Product salepatient = inventoryProductDAO.getPatientSaleBetweenDate(catalogueid,fromDate,toDate,location_filter);
						
						//Return to supplier in between fromdate and todate
						Product returntosuppplier = inventoryProductDAO.getReturnToSupplierBetwwenDate(catalogueid,fromDate,toDate,location_filter); 
						
						//Consume to user or patient between Fromdate and Todate
						Product consume = inventoryProductDAO.getConsumeBetweenDate(catalogueid,fromDate,toDate,location_filter);
						
						Product adjustmentbetweenOut = inventoryProductDAO.getAdjustmentDatabetweenOut(catalogueid,fromDate,location_filter,toDate);
						
						int qtyout = salepatient.getTotalqty()+returntosuppplier.getTotalqty()
									+consume.getTotalqty() + directtransferqtyout +requesttransferqtyout 
									+returntransferqtyout + adjustmentbetweenOut.getTotalqty();
						
						//Stock value =  Patient sale unit price + Return to Supplier purchase price + Consume purchase price
						double stockvalue =  salepatient.getTotal_amount()+ returntosuppplier.getTotal_amount()
												+consume.getTotal_amount() +directtransferqtyoutvalue +requesttransferqtyoutvalue 
												+returntransferqtyoutvalue + adjustmentbetweenOut.getTotal_amount();
						
						//sale price total 
						double saleprice = salepatient.getSalepricetotal()+ returntosuppplier.getSalepricetotal()
												+consume.getSalepricetotal() +directsaleprice +returnsaleprice 
												+requestsaleprice + adjustmentbetweenOut.getSalepricetotal();
						
						
						
						//Closing - opening + qtyin - qtyout
						int closingstock = openingstock +qtyin - qtyout;
						
						//double closingvalue = unitprice * closingstock;
						double closingvalue = opeingstockvalue + qtyinvalue - stockvalue;
						int unknownqty = 0;
						if((openingstock +qtyin)<qtyout){
							closingstock =0;
							closingvalue =0;
							unknownqty = qtyout - (openingstock +qtyin);
						}
						
						//double unitprice = getUnitPriceFromCatalogueid(catalogueid);
						
						product.setUnknownqty(unknownqty);
						product.setOpeningstock(""+openingstock);
						product.setOpeningstockvalue(Math.round(opeingstockvalue * 100.0)/100.0);
						product.setPurchaseqty(qtyin);
						product.setSale(""+qtyout);
						product.setSalevalue(Math.round(stockvalue * 100.0)/100.0);
						product.setClosingstock(""+closingstock);
						product.setSv(DateTimeUtils.changeFormat(Math.round(closingvalue * 100.0)/100.0));
						product.setQtyinvalue(DateTimeUtils.changeFormat(qtyinvalue));
						product.setSale_price(DateTimeUtils.changeFormat(Math.round(saleprice * 100.0)/100.0));
					
					 	String sql1="insert into inventory_catalogue_log (lastmodfied, date, location, opening_stock, opeing_value, qty_in, qty_in_value, qty_out, qty_out_value, stock_value, Unknown_qty, closing_stock, closing_value, catalogueid) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
					 	PreparedStatement ps1=connection.prepareStatement(sql1);
					 	ps1.setString(1, todates);
					 	ps1.setString(2, previousdate);
					 	ps1.setString(3, location_filter);
					 	
					 	ps1.setString(4, ""+openingstock);
					 	ps1.setString(5, DateTimeUtils.changeFormat(Math.round(opeingstockvalue * 100.0)/100.0));
					 	ps1.setString(6, ""+qtyin);
					 	
					 	ps1.setString(7, DateTimeUtils.changeFormat(qtyinvalue));
					 	ps1.setString(8, ""+qtyout);
					 	ps1.setString(9, DateTimeUtils.changeFormat(Math.round(stockvalue * 100.0)/100.0));
					 	
					 	ps1.setString(10, DateTimeUtils.changeFormat(Math.round(saleprice * 100.0)/100.0));
					 	ps1.setString(11, ""+unknownqty);
					 	ps1.setString(12, ""+closingstock);
					 	
					 	ps1.setString(13, DateTimeUtils.changeFormat(Math.round(closingvalue * 100.0)/100.0));
					 	ps1.setString(14, rs.getString(1));
					 	int res= ps1.executeUpdate();
					}
				}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			*/}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				connection.close();
			}
			
				
		}
		
		
		return "wait";
	}
	
	
	

	
	
	private boolean checkInventoryCatalogueStatus(String newdate, Connection connection) {
		boolean flag = false;
		try {
			String sql ="select * from inventory_catalogue_log where date='"+newdate+"'";
			java.sql.PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}






	/*private void saveInventoryStockLog(Connection connection, String todaydate) {
		try {
			//Akash 17 Aug 2018
			InventoryProductDAO inventoryProductDAO = new JDBCInventoryProductDAO(connection);
			int res = inventoryProductDAO.saveInventoryStockLog(todaydate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}*/






	public  Connection getconnection(String dbname) throws SQLException
	{
		/*LoginInfo loginInfo = (LoginInfo)session.getAttribute("logininfo");
		String dbname = "";
		if(loginInfo!=null){
			dbname = loginInfo.getUserId();
		}
		*/
		
		Connection con=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			
			//con=DriverManager.getConnection("jdbc:mysql://localhost:3306/apm","root","mysql");
			//con=DriverManager.getConnection("jdbc:mysql://localhost:3306/apmCbstec","Balvinder001","Deepak001");
			con=DriverManager.getConnection(""+Constants.DB_HOST+":3306/"+dbname+"","pranams","6qxi5x&)~XBZ");
			//con=DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dbname+"","root","mysql");
			System.out.println("done");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return con;
	}
	

}
