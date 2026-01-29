package com.apm.DiaryManagement.web.action;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;


import com.apm.Accounts.eu.bi.AccountsDAO;
import com.apm.Accounts.eu.blogic.jdbc.JDBCAccountsDAO;
import com.apm.DiaryManagement.eu.bi.ClientDAO;
import com.apm.DiaryManagement.eu.bi.CompleteAptmDAO;
import com.apm.DiaryManagement.eu.bi.NotAvailableSlotDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCClientDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCCompleteAptmDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCNotAvailableSlotDAO;
import com.apm.DiaryManagement.eu.entity.Client;
import com.apm.DiaryManagement.eu.entity.CompleteAppointment;
import com.apm.DiaryManagement.eu.entity.DiaryManagement;
import com.apm.DiaryManagement.eu.entity.GpData;
import com.apm.DiaryManagement.eu.entity.NotAvailableSlot;
import com.apm.DiaryManagement.web.form.ClientForm;
import com.apm.Emr.eu.bi.EmrDAO;
import com.apm.Emr.eu.bi.InvestigationDAO;
import com.apm.Emr.eu.blogic.jdbc.JDBCEmrDAO;
import com.apm.Emr.eu.blogic.jdbc.JDBCInvestigationDAO;
import com.apm.Inventory.eu.bi.InventoryVendorDAO;
import com.apm.Inventory.eu.blogic.jdbc.JDBCInventoryVendorDAO;
import com.apm.Ipd.eu.bi.BedDao;
import com.apm.Ipd.eu.bi.IpdDAO;
import com.apm.Ipd.eu.blogic.jdbc.JDBCBedDao;
import com.apm.Ipd.eu.blogic.jdbc.JDBCIpdDAO;
import com.apm.Ipd.eu.entity.Bed;
import com.apm.Log.eu.entity.EmailLetterLog;
import com.apm.Master.eu.bi.MasterDAO;
import com.apm.Master.eu.blogic.jdbc.JDBCMasterDAO;
import com.apm.Master.eu.entity.Master;
import com.apm.Registration.eu.bi.ClinicDAO;
import com.apm.Registration.eu.bi.UserProfileDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCClinicDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCUserProfileDAO;
import com.apm.Registration.eu.entity.Clinic;
import com.apm.Registration.eu.entity.UserProfile;
import com.apm.ThirdParties.eu.bi.ThirdPartyDAO;
import com.apm.ThirdParties.eu.blogic.jdbc.JDBCThirdPartyDAO;
import com.apm.ThirdParties.eu.entity.ThirdParty;
import com.apm.TreatmentEpisode.eu.bi.TreatmentEpisodeDAO;
import com.apm.TreatmentEpisode.eu.blogic.jdbc.JDBCTreatmentEpisode;
import com.apm.TreatmentEpisode.eu.entity.TreatmentEpisode;
import com.apm.common.eu.blogic.jdbc.Connection_provider;
import com.apm.common.utils.DateTimeUtils;
import com.apm.common.utils.Pagination;
import com.apm.common.web.action.BaseAction;
import com.apm.common.web.common.helper.LoginHelper;
import com.apm.common.web.common.helper.LoginInfo;
import com.apm.common.web.utils.PopulateList;
import com.apm.main.common.constants.Constants;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;





public class ClientAction extends BaseAction implements Preparable, ModelDriven<ClientForm> {

	ClientForm clientForm = new ClientForm();
	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
	HttpServletResponse response = (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
	HttpSession session = request.getSession(true);
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	private Pagination pagination = new Pagination(25, 1);
	public Pagination getPagination() {
		return pagination;
	}


	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}
	
	public String execute() throws Exception {
		
		if(!verifyLogin(request)){
			return "login";
		}
		
		Connection connection = null;
		Client client = new Client();
		ArrayList<Client> allPatientList = new ArrayList<Client>();
		try{
			connection = Connection_provider.getconnection();
			
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			allPatientList = clientDAO.getAllPatient(loginInfo.getId());
			
			clientForm.setAllPatientList(allPatientList);
			
			
			StringBuffer str = new StringBuffer();
			

			str.append("<table width = '50%' id = 'allPatient' cellpadding='0' cellspacing='0' class='my-table' > ");
			str.append("<tr>");
			str.append("<th>Id</th> ");
			str.append("<th>Adhar No</th> ");
			str.append("<th>Name</th> ");
			str.append("<th>Age</th> ");
			str.append("<th>Mobile</th> ");
			str.append("<th>Email</th> ");
			str.append("<th>PostCode</th> ");
			str.append("<th>LastModified</th> ");

			str.append("</tr>");
			
			for(Client client1:allPatientList){
			String name = client1.getTitle()+" "+client1.getFirstName()+" "+client1.getMiddlename()+" "+client1.getLastName(); 	
			String color = "";
			if(!client1.getCasualtyid().equals("0")){
				color = "#f5a0b4";
			}
			
			String whopay = "";
			if(client1.getWhopay().equals(Constants.PAY_BY_THIRD_PARTY)){
				whopay = "tp";
			}else{
				whopay = "Client";
			}
			String fullname = client1.getTitle() + "/" + client1.getFirstName() + "/" + client1.getLastName();
			
			str.append("<tr style='cursor: pointer;background-color:"+color+"' onclick = setClientName('"+client1.getId()+"','"+client1.getType()+"','"+client1.getTypeName()+"','"+whopay+"','"+client1.getGpname()+"')>");
			String firstName= client1.getFirstName();
			str.append("<input type='hidden' id='firstnameid"+client1.getId()+"' value='"+fullname+"'>");
			if(client1.getAbrivationid()==null){
				str.append("<td>0000"+client1.getId()+"</td>");
			}else{
				str.append("<td>"+client1.getAbrivationid()+"/"+client1.getId()+"</td>");
			}
			
			str.append("<td>"+client1.getAdhno()+"</td>");
				
			

			

			str.append("<td>"+name+"</td>");

			
			if(client1.getOldclientId()==null){
				client1.setOldclientId("");
			}
			try {
				String age=DateTimeUtils.getAge1(client1.getDob());
				str.append("<td>"+age+"</td>");
				
			} catch (Exception e) {
				str.append("<td></td>");
			}
			
			str.append("<td>"+client1.getMobNo()+"</td>");
			str.append("<td>"+client1.getEmail()+"</td>");
			str.append("<td>"+client1.getPostCode()+"</td>");
			if(client1.getLastModified()==null){
				client1.setLastModified("");
			}
			str.append("<td>"+client1.getLastModified()+"</td>");
			
			str.append("</tr>");
			}
			
			
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
	
	
	
	
	public String whopay() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		
		Connection connection = null;
		
		try{
			connection = Connection_provider.getconnection();
			
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			
			String patientId = request.getParameter("patientId");
			String whopay = request.getParameter("whopay");
			
			int updatewhopay = clientDAO.updateWhoPay(patientId,whopay);
			
			System.out.println(whopay);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			connection.close();
		}
		
		return null;
	}
	

	public String showList() throws SQLException{
		if(!verifyLogin(request)){
			return "login";
		}
		
		Connection connection = null;
		Client client = new Client();
		ArrayList<Client> allPatientList = new ArrayList<Client>();
		try{
			connection = Connection_provider.getconnection();
			
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			allPatientList = clientDAO.getAllPatient(loginInfo.getId());
			
			clientForm.setAllPatientList(allPatientList);
			
			
			StringBuffer str = new StringBuffer();
			

			
			str.append("<table class='table table-bordered' > ");
			str.append("<thead>");
			str.append("<tr class='bg-info'>");
			str.append("<th>Id</th> ");

			str.append("<th>Name</th> ");
			str.append("<th>Age</th> ");
			str.append("<th>Mobile</th> ");
			str.append("<th>Email</th> ");
			str.append("<th>PostCode</th> ");
			str.append("<th>LastModified</th> ");
			str.append("</tr>");
			str.append("</thead>");
			
			str.append("<tbody>");
			for(Client client1:allPatientList){
				String name = client1.getTitle()+" "+client1.getFirstName()+" "+client1.getMiddlename()+" "+client1.getLastName(); 	
				String color = "";
				if(!client1.getCasualtyid().equals("0")){
					color = "#f5a0b4";
				}
				String firstName= client1.getFirstName();
				
				int payee=0;
				if(client1.getWhopay()!=null){
					
					if(client1.getWhopay().equals("Client")){
						 payee=0;
					} else {
						payee=1;
					}
					
				}
			str.append("<tr style='background-color:"+color+";cursor: pointer;' onclick = setPatientName('"+client1.getId()+"','"+client1.getTypeName()+"','"+payee+"') >");
			if(client1.getAbrivationid()==null){
				str.append("<td>0000"+client1.getId()+"</td>");
			}else{
				str.append("<td>"+client1.getAbrivationid()+"/"+client1.getId()+"</td>");
			}
			
			str.append("<input type='hidden' id='firstnameid"+client1.getId()+"' value='"+client1.getFirstName()+"'>");
			
		//	String data=client1.getAddress()+"~"+client1.getDob()+"~"+client1.getEmergencyContName()+"~"+client1.getEmergencyContNo()+"~"+client1.getRelation();
			str.append("<td style='cursor: pointer;'; >"+name+"</td>");
			if(client1.getOldclientId()==null){
				client1.setOldclientId("");
			}
			try {
				String age= DateTimeUtils.getAge1(client1.getDob());
				str.append("<td>"+age+"</td>");
			} catch (Exception e) {
				str.append("<td></td>");
			} 
			
			str.append("<td>"+client1.getMobNo()+"</td>");
			str.append("<td>"+client1.getEmail()+"</td>");
			str.append("<td>"+client1.getPostCode()+"</td>");
			if(client1.getLastModified()==null){
				client1.setLastModified("");
			}
			str.append("<td>"+client1.getLastModified()+"</td>");

			str.append("</tr>");
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
	public String manage() throws SQLException{
		if(!verifyLogin(request)){
			return "login";
		}
		
		Connection connection = null;
		Client client = new Client();
		ArrayList<Client> allPatientList = new ArrayList<Client>();
		
		clientForm.setShowAll(true);
		try{
			connection = Connection_provider.getconnection();
			
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			int totalCount = clientDAO.getTotalClientCount(loginInfo.getId(),clientForm.isShowAll(),clientForm.getDiaryUser());
			pagination.setPreperties(totalCount);
			
			allPatientList = clientDAO.getAllPatient(pagination,loginInfo.getId(),clientForm.isShowAll(),clientForm.getDiaryUser());
			pagination.setPage_records(allPatientList.size());
			clientForm.setTotalRecords(totalCount);
			clientForm.setPagerecords(Integer.toString(pagination.getPage_records()));
			
			//set state and city list
			InventoryVendorDAO vendorDAO=new JDBCInventoryVendorDAO(connection);
			ArrayList<Master> stateList= vendorDAO.getAllStateList();
			ArrayList<Master> cityList= vendorDAO.getAllCityList();
			clientForm.setStatelist(stateList);
			clientForm.setCitylist(cityList);
			
			clientForm.setAllPatientList(allPatientList);
			//session.setAttribute("pagination", pagination);
			session.removeAttribute("searchClient");
						
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			connection.close();
		}
		return "manage";
	}
	public String back() throws Exception{
		setFromData();
		return "manage";
	}
	
	public String move() throws Exception{
		//clientForm.setShowAll(true);
		setFromData();
		return "manage";
	}
	
	public String save() throws SQLException{
		if(!verifyLogin(request)){
			return "login";
		}
 		Connection connection = null;
		try{
			
			Client client = new Client();
			connection = Connection_provider.getconnection();
			String gp = clientForm.getGpid();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			
			//save abrivation seq no
			String cdate = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
			boolean checkifseq = clientDAO.checkifSequenceExist(cdate);
			String abrivationid = "";
			String clinicabrivation = clientDAO.getClinicAbrivation(loginInfo.getClinicid());
			String tempd[] = cdate.split("-");
			String y = tempd[0];
			String m = tempd[1];
			String d = tempd[2];
			String newseq="";
//			String mothername=DateTimeUtils.isNull(request.getParameter("mothername"));
//			String fathername=DateTimeUtils.isNull(request.getParameter("fathername"));
//			String birthplace=DateTimeUtils.isNull(request.getParameter("birthplace"));
			if(checkifseq){
				
				int seqno = clientDAO.getSqeunceNumber(cdate);
				seqno++;
				int r = clientDAO.InserCdateSeq(cdate,seqno);
				//SNH170609001
				int yr = Integer.parseInt(y)%1000;
				if(String.valueOf(seqno).length()==1)
				{
					 newseq="00"+seqno;
				}else if(String.valueOf(seqno).length()==2){
					 newseq="0"+seqno;
				}else{
					newseq=""+seqno;
				}
				abrivationid = clinicabrivation + yr + m +d + "" +newseq ;
			}else{
				int seqno = 1;
				int r = clientDAO.InserCdateSeq(cdate,seqno);
				//String seqno = clientDAO.getSqeunceNumber(cdate);
				int yr = Integer.parseInt(y)%1000;
				if(String.valueOf(seqno).length()==1)
				{
					 newseq="00"+seqno;
				}else if(String.valueOf(seqno).length()==2){
					 newseq="0"+seqno;
				}else{
					newseq=""+seqno;
				}
				abrivationid = clinicabrivation + yr + m +d + "" + seqno;
			}
			
			client.setTitle(clientForm.getTitle());
			client.setFirstName(clientForm.getFirstName());
			client.setLastName(clientForm.getLastName());
			client.setMiddlename(clientForm.getMiddleName());
			client.setAddress(clientForm.getAddress());
			client.setCountry(clientForm.getCountry());
			client.setDob(clientForm.getDob());
			client.setEmail(clientForm.getEmail());
			client.setGender(clientForm.getGender());
			client.setMobNo(clientForm.getMobNo().trim());
			client.setPostCode(clientForm.getPostCode().trim());
			
			//get adhar and third party member number 
			String adhno = clientForm.getAdhno();
			String tmemb = clientForm.getTpmemb();
			client.setAdhno(clientForm.getAdhno());
			client.setTpmemb(clientForm.getTpmemb());
			
			ArrayList<Client> clientOccupationList = clientDAO.getOccupationList();
			ArrayList<Client> refrenceList = clientDAO.getReferenceList();
			ArrayList<Client> sourceOfIntroList = clientDAO.getSourceOfIntroList();
			
			
			client.setMothername(clientForm.getMothername());
			client.setFathername(clientForm.getFathername());
			client.setBirthplace(clientForm.getBirthplace());
			client.setTown(clientForm.getTown());
			client.setType(clientForm.getType());
			client.setTypeName(clientForm.getTypeName());
			if(clientForm.getOtherRef().equals(null) || clientForm.getOtherRef().equals(""))
			{
				client.setReference(clientForm.getReference());
				
			}else{
				String reference = clientForm.getOtherRef();
				int save = clientDAO.insertOtherReference(client, reference);
				int id = clientDAO.getMaxIdOfRefernce();
				client.setReference(Integer.toString(id));
			
			}
			if(clientForm.getOtherSourceOfIntro().equals(null) || clientForm.getOtherSourceOfIntro().equals(""))
			{
				client.setSourceOfIntro(clientForm.getSourceOfIntro());

			}
			else{
				String otherSourceOfIntro = clientForm.getOtherSourceOfIntro();
				int save = clientDAO.insertOtherSourceOfIntro(client, otherSourceOfIntro);
				int id = clientDAO.getMaxIdOfSourceOfIntro();
				client.setSourceOfIntro(Integer.toString(id));
			}
			if(clientForm.getOtherOccupation().equals(null) || clientForm.getOtherOccupation().equals(""))
			{
				client.setOccupation(clientForm.getOccupation());

			}
			else{
				String otherOccupation = clientForm.getOtherOccupation();
				int save = clientDAO.insertOtherOccupation(client, otherOccupation);
				int id = clientDAO.getMaxIdOfOccupation();
				client.setOccupation(Integer.toString(id));
			}
			
			if(clientForm.getOtherCondition().equals(null) || clientForm.getOtherCondition().equals(""))
			{
				client.setTreatmentType(clientForm.getTreatmentType());

			}
			else{
				String otherCondition = clientForm.getOtherCondition();
				int save = clientDAO.insertOtherCondition(client, otherCondition);
				int id = clientDAO.getMaxIdOfCondition();
				client.setTreatmentType(Integer.toString(id));
			}
			client.setExpiryDate(clientForm.getExpiryDate());
			client.setWhopay(clientForm.getWhopay());
			client.setPolicyAuthorzCode(clientForm.getPolicyAuthorzCode());
			client.setPolicyNo(clientForm.getPolicyNo());
			client.setPolicyExcess(clientForm.getPolicyExcess());
			
			
			client.setKnownAs(clientForm.getKnownAs());
			String County = clientForm.getCounty();
			client.setCounty(clientForm.getCounty());
			client.setHomeNo(clientForm.getHomeNo().trim());
			client.setWorkNo(clientForm.getWorkNo().trim());
			client.setEmailCc(clientForm.getEmailCc());
			client.setPrefContactMode(clientForm.getPrefContactMode());
			client.setEmergencyContName(clientForm.getEmergencyContName());
			client.setEmergencyContNo(clientForm.getEmergencyContNo().trim());
			client.setPatientType(clientForm.getPatientType());
			client.setReferedDate(clientForm.getReferedDate());
			client.setEmployerName(clientForm.getEmployerName());
			client.setGpname(clientForm.getGpid());
			String surgeryname = clientForm.getGptypeName();
			client.setDoctorsurgery(clientForm.getGptypeName());
			client.setSecondLineaddress(clientForm.getSecondLineaddress());
			client.setSourceOfIntroName(clientForm.getSourceOfIntroName());
			ArrayList<Client>condtitionList = clientDAO.getTreatmentTypeList();
			
			
			//set note
			client.setClientNote(clientForm.getClientNote());
			client.setAccountNote(clientForm.getAccountNote());
			client.setClinicalNote(clientForm.getClinicalNote());
			client.setLastModifiedDate(DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
			client.setNhsNumber(clientForm.getNhsNumber());
			client.setAbrivationid(abrivationid);
			//shubham 06/12/2018
			client.setCompname(clientForm.getCompname());
			client.setNeisno(clientForm.getNeisno());
			client.setDesignationbytp(clientForm.getDesignationbytp());
			client.setUnitstation(clientForm.getUnitstation());
			client.setRelationvbytpe(clientForm.getRelationvbytpe());
			client.setClaimbytp(clientForm.getClaimbytp());
			client.setColliery(clientForm.getColliery());
			client.setAreabytp(clientForm.getAreabytp());
			client.setPolicyholder(clientForm.getPolicyholder());
			if(clientForm.isHospitalborn()){
				client.setHospitalborn("1");
			}else{
				client.setHospitalborn("0");
			}
			if(clientForm.getMaritalsts().equals("0")){
				client.setMaritalsts("");
			}else{
				client.setMaritalsts(clientForm.getMaritalsts());
			}
			String birthtime=clientForm.getHourls()+":"+clientForm.getMinutels()+":00";
			client.setBirthtime(birthtime);
			client.setDocumentValue(clientForm.getDocumentValue());
			client.setDocumentID(clientForm.getDocument_name());
			
			int result = clientDAO.savePatient(client,loginInfo.getId());
			clientDAO.addToHISDocumentLog(clientForm.getDocument_name(), clientForm.getDocumentValue(), ""+result);
			
			File userImage = clientForm.getUserImage();
			String userImageFileName = clientForm.getUserImageFileName();
			if(userImageFileName!=null){			
				userImageFileName =loginInfo.getClinicUserid()+"_"+result+"_"+userImageFileName;
				//update image name
				int res=clientDAO.updateClientUserImage(userImageFileName,result);
			}else{
				userImageFileName = loginInfo.getClinicUserid()+"_"+result+"_profileimg.png";
				
				String filepath = request.getRealPath("/liveData/"+userImageFileName);
				String clientImageData = clientForm.getProfileimg();
				byte[] imagedata = DatatypeConverter.parseBase64Binary(clientImageData.substring(clientImageData.indexOf(",") + 1));
				BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagedata));
				if(!clientForm.getProfileimg().equals("")){
					ImageIO.write(bufferedImage, "png", new File(filepath));
				}
			}
			String userImageContentType = clientForm.getUserImageContentType();
			
			if(clientForm.getUserImageContentType()!=null){
			String filePath = request.getRealPath("/liveData/");			
			System.out.println("Server path:" + filePath);
			File fileToCreate = new File(filePath, userImageFileName);
			FileUtils.copyFile(userImage, fileToCreate);
			}
			
			
	
			session.setAttribute("clientid", Integer.toString(result));
			session.setAttribute("title", clientForm.getTitle());
			session.setAttribute("firstname", clientForm.getFirstName());
			session.setAttribute("lastname", clientForm.getLastName());
			session.setAttribute("treatmenttype", clientForm.getTreatmentType());
			session.setAttribute("newWhopay", clientForm.getWhopay());
			session.setAttribute("newPolicyNo", clientForm.getPolicyNo());
			
			CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(connection);
			CompleteAppointment completeAppointment =  completeAptmDAO.getInsuranceCompanyName(Integer.toString(result));
			String tpName = completeAppointment.getInsuranceCompanyName();
			session.setAttribute("newTpname", tpName);
			
			clientForm.setMessage("Patient Registered Suceessfully");
			addActionMessage("Patient Registered Suceessfully");
			setFromData();
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			connection.close();
		}
		return "save";
	}
	
	
	public String saveNewPatient() throws SQLException{
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{
			
			Client client = new Client();
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			client.setTitle(clientForm.getTitle());
			client.setFirstName(clientForm.getFirstName());
			client.setLastName(clientForm.getLastName());
			client.setMiddlename(clientForm.getMiddleName());
			client.setDob(clientForm.getDob());
			client.setGender(clientForm.getGender());
			client.setGpname(clientForm.getGpname());
			client.setAddress(clientForm.getAddress());
			client.setTown(clientForm.getTown());
			client.setPostCode(clientForm.getPostCode().trim());
			client.setCountry(clientForm.getCountry());
			
			client.setEmail(clientForm.getEmail());
			client.setMobNo(clientForm.getMobNo().trim());
			String other = "Other";
			if(clientForm.getReference().equalsIgnoreCase(other))
			{
				clientForm.setReference(clientForm.getOtherRef());
			}
			
			client.setReference(clientForm.getReference());
			
			
			client.setWhopay(clientForm.getWhopay());
			
			client.setType(clientForm.getType());
			client.setTypeName(clientForm.getTypeName());
			client.setPolicyNo(clientForm.getPolicyNo());
			client.setExpiryDate(clientForm.getExpiryDate());
			client.setPolicyExcess(clientForm.getPolicyExcess());
			
			client.setTreatmentType(clientForm.getTreatmentType());
			//lokesh 27/11*/2018
			String hospitalborn= request.getParameter("hospitalborn");
			client.setHospitalborn(hospitalborn);
			//shubham 07/12/18
			client.setCompname(clientForm.getCompname());
			client.setNeisno(clientForm.getNeisno());
			client.setDesignationbytp(clientForm.getDesignationbytp());
			client.setUnitstation(clientForm.getUnitstation());
			client.setRelationvbytpe(clientForm.getRelationvbytpe());
			client.setClaimbytp(clientForm.getClaimbytp());
			client.setColliery(clientForm.getColliery());
			client.setAreabytp(clientForm.getAreabytp());
			
			int result = clientDAO.saveNewPatient(client,loginInfo.getId());
			clientForm.setMessage("Patient Registered Suceessfully");
			addActionMessage("Patient Registered Suceessfully");
			setFromData();
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			connection.close();
		}
		return "save";
		
	}
	public void setFromData() throws Exception{
		Connection connection = null;
		int totalCount =0;
		try{
			String searchClient = clientForm.getSearchText();
			String status = clientForm.getStatus();
			if(searchClient.equals(""))
			{
				searchClient = (String) session.getAttribute("searchClient");
				
				if(searchClient == null){
					searchClient = "";
				}
			}
		connection = Connection_provider.getconnection();
		ClientDAO clientDAO = new JDBCClientDAO(connection);
		ArrayList<Client> allPatientList = new ArrayList<Client>();

		/*session.setAttribute("csdiaryuser", clientForm.getDiaryUser());
		session.setAttribute("showall", clientForm.isShowAll());*/
		
		String diaryUser = "0";
		
		if((String)session.getAttribute("csdiaryuser")!=null){
			diaryUser = (String)session.getAttribute("csdiaryuser");
		}
		boolean showAll = false;
				
		if(session.getAttribute("showall")!=null){
			showAll = (Boolean)session.getAttribute("showall");
		}
				
		
		clientForm.setDiaryUser(diaryUser);
	//	clientForm.setShowAll(showAll);;
		
		
		
		/*if(!searchClient.equals("")){
			 totalCount = clientDAO.getTotalClientCountOfSearch(loginInfo.getId(),searchClient,status,clientForm.isShowAll(),clientForm.getDiaryUser());
			 pagination.setPreperties(totalCount);
			allPatientList = clientDAO.getClientofSearch(pagination,searchClient,loginInfo.getId(),status,clientForm.isShowAll(),clientForm.getDiaryUser());
		}else{
			 totalCount = clientDAO.getTotalClientCount(loginInfo.getId(),clientForm.isShowAll(),clientForm.getDiaryUser());
			 pagination.setPreperties(totalCount);
			allPatientList = clientDAO.getAllPatient(pagination,loginInfo.getId(),clientForm.isShowAll(),clientForm.getDiaryUser());

		}*/
		
		 totalCount = clientDAO.getTotalClientCountOfSearch(loginInfo.getId(),searchClient,status,clientForm.isShowAll(),clientForm.getDiaryUser());
		 pagination.setPreperties(totalCount);
		allPatientList = clientDAO.getClientofSearch(pagination,searchClient,loginInfo.getId(),status,clientForm.isShowAll(),clientForm.getDiaryUser());
		
		
		clientForm.setSearchText(searchClient);
		
		pagination.setPage_records(allPatientList.size());
		clientForm.setTotalRecords(totalCount);
		clientForm.setPagerecords(Integer.toString(pagination.getPage_records()));
		clientForm.setAllPatientList(allPatientList);
		}
		catch(Exception e){
			e.printStackTrace();
			
		}finally{
			
			connection.close();
		}
	}
	
	public String add() throws SQLException{
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		Client client = new Client();
		ArrayList<Client> thirdPartyTypeList = new ArrayList<Client>();
		ArrayList<Client> thirdPartyTypeNameList = new ArrayList<Client>();
		ArrayList<Client> clientOccupationList = new ArrayList<Client>();
		ArrayList<Client> refrenceList = new ArrayList<Client>();
		ArrayList<String> initialList = new ArrayList<String>();
		ArrayList<Client> sourceOfIntroList = new ArrayList<Client>();
		try{
			
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			thirdPartyTypeList = clientDAO.getThirdPartyType();
			clientForm.setThirdPartyTypeList(thirdPartyTypeList);
			
			thirdPartyTypeNameList = clientDAO.getThirdPartyTypeName();
			clientForm.setThirdPartyTypeNameList(thirdPartyTypeNameList);
			
			clientOccupationList = clientDAO.getOccupationList();
			Client client1 = new Client();
			client1.getOther();
			clientOccupationList.add(client1);
			
			clientForm.setClientOccupationList(clientOccupationList);
			
			refrenceList = clientDAO.getReferenceList();
			Client client2 = new Client();
			client2.getOther();
			refrenceList.add(client2);
			clientForm.setRefrenceList(refrenceList);
			
			initialList = clientDAO.getInitialList();
			clientForm.setInitialList(initialList);
			
			sourceOfIntroList = clientDAO.getSourceOfIntroList();
			clientForm.setSourceOfIntroList(sourceOfIntroList);
			
			
			
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			connection.close();
		}
		return null;
	}
	
	public String addManage() throws SQLException{
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		Client client = new Client();
		ArrayList<Client> thirdPartyTypeList = new ArrayList<Client>();
		ArrayList<Client> thirdPartyTypeNameList = new ArrayList<Client>();
		ArrayList<Client> clientOccupationList = new ArrayList<Client>();
		ArrayList<Client> refrenceList = new ArrayList<Client>();
		ArrayList<String> initialList = new ArrayList<String>();
		ArrayList<Client> sourceOfIntroList = new ArrayList<Client>();
		 
		try{
			
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			thirdPartyTypeList = clientDAO.getThirdPartyType();
			clientForm.setThirdPartyTypeList(thirdPartyTypeList);
			
			thirdPartyTypeNameList = clientDAO.getThirdPartyTypeName();
			clientForm.setThirdPartyTypeNameList(thirdPartyTypeNameList);
			
			clientOccupationList = clientDAO.getOccupationList();
			Client client1 = new Client();
			client1.getOther();
			clientOccupationList.add(client1);
			
			clientForm.setClientOccupationList(clientOccupationList);
			
			refrenceList = clientDAO.getReferenceList();			
			Client client2 = new Client();
			client2.getOther();
			refrenceList.add(client2);
			clientForm.setRefrenceList(refrenceList);
			
			initialList = clientDAO.getInitialList();
			clientForm.setInitialList(initialList);
			
			sourceOfIntroList = clientDAO.getSourceOfIntroList();
			clientForm.setSourceOfIntroList(sourceOfIntroList);
			
			ArrayList<Client>surgeryList = clientDAO.getSurgeryList();
			clientForm.setSurgeryList(surgeryList);
			
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			connection.close();
		}
		return "addManage";
	}
	
	public String addCompleteInfo() throws SQLException{
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		Client client = new Client();
		ArrayList<Client> thirdPartyTypeList = new ArrayList<Client>();
		ArrayList<Client> thirdPartyTypeNameList = new ArrayList<Client>();
		ArrayList<Client> clientOccupationList = new ArrayList<Client>();
		ArrayList<Client> refrenceList = new ArrayList<Client>();
		ArrayList<String> initialList = new ArrayList<String>();
		ArrayList<Client> sourceOfIntroList = new ArrayList<Client>();
		try{
			
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			thirdPartyTypeList = clientDAO.getThirdPartyType();
			clientForm.setThirdPartyTypeList(thirdPartyTypeList);
			
			thirdPartyTypeNameList = clientDAO.getThirdPartyTypeName();
			clientForm.setThirdPartyTypeNameList(thirdPartyTypeNameList);
			
			clientOccupationList = clientDAO.getOccupationList();
			
			
			clientForm.setClientOccupationList(clientOccupationList);
			
			refrenceList = clientDAO.getReferenceList();
			
			clientForm.setRefrenceList(refrenceList);
			
			initialList = clientDAO.getInitialList();
			clientForm.setInitialList(initialList);
			
			sourceOfIntroList = clientDAO.getSourceOfIntroList();
			clientForm.setSourceOfIntroList(sourceOfIntroList);
			
			ArrayList<Client>surgeryList = clientDAO.getSurgeryList();
			clientForm.setSurgeryList(surgeryList);
			clientForm.setHourList(PopulateList.hourList());
			clientForm.setMinuteList(PopulateList.getMinuteList());
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			connection.close();
		}
		return "addCompleteInfo";
	}
	public String addPatient() throws SQLException{
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		Client client = new Client();
		ArrayList<Client> thirdPartyTypeList = new ArrayList<Client>();
		ArrayList<Client> thirdPartyTypeNameList = new ArrayList<Client>();
		ArrayList<Client> clientOccupationList = new ArrayList<Client>();
		ArrayList<Client> refrenceList = new ArrayList<Client>();
		ArrayList<String> initialList = new ArrayList<String>();
		ArrayList<Client> sourceOfIntroList = new ArrayList<Client>();
		try{
			
		
			
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			thirdPartyTypeList = clientDAO.getThirdPartyType();
			clientForm.setThirdPartyTypeList(thirdPartyTypeList);
			
			thirdPartyTypeNameList = clientDAO.getThirdPartyTypeName();
			clientForm.setThirdPartyTypeNameList(thirdPartyTypeNameList);
			
			clientOccupationList = clientDAO.getOccupationList();
			Client client1 = new Client();
			client1.getOther();
			clientOccupationList.add(client1);
			
			
			clientForm.setClientOccupationList(clientOccupationList);
			
			refrenceList = clientDAO.getReferenceList();
			Client client2 = new Client();
			client2.getOther();
			refrenceList.add(client2);
			clientForm.setRefrenceList(refrenceList);
			
			initialList = clientDAO.getInitialList();
			clientForm.setInitialList(initialList);
			
			sourceOfIntroList = clientDAO.getSourceOfIntroList();
			clientForm.setSourceOfIntroList(sourceOfIntroList);
			
			ArrayList<Client>surgeryList = clientDAO.getSurgeryList();
			clientForm.setSurgeryList(surgeryList);
			
			
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			connection.close();
		}
		return "addPatient";
	}
	
	
	public String edit() throws SQLException{
		if(!verifyLogin(request)){
			return "login";
		}
		ArrayList<Client> thirdPartyTypeList = new ArrayList<Client>();
		ArrayList<Client> thirdPartyTypeNameList = new ArrayList<Client>();
		ArrayList<Client> clientOccupationList = new ArrayList<Client>();
		ArrayList<Client> refrenceList = new ArrayList<Client>();
		ArrayList<String> initialList = new ArrayList<String>();
		Connection connection = null;
		try{
			int id = Integer.parseInt(request.getParameter("selectedid"));
			Client client = new Client();
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			thirdPartyTypeList = clientDAO.getThirdPartyType();
			clientForm.setThirdPartyTypeList(thirdPartyTypeList);
			
			
			clientOccupationList = clientDAO.getOccupationList();
			Client client1 = new Client();
			client1.getOther();
			clientOccupationList.add(client1);
			
			
			clientForm.setClientOccupationList(clientOccupationList);
			
			refrenceList = clientDAO.getReferenceList();
			Client client2 = new Client();
			client2.getOther();
			refrenceList.add(client2);
			clientForm.setRefrenceList(refrenceList);
			
			initialList = clientDAO.getInitialList();
			clientForm.setInitialList(initialList);
			
			ArrayList<Client>surgeryList = clientDAO.getSurgeryList();
			clientForm.setSurgeryList(surgeryList);
			
			client = clientDAO.getPatient(id);
			
			clientForm.setTitle(client.getTitle());
			clientForm.setFirstName(client.getFirstName());
			clientForm.setLastName(client.getLastName());
			clientForm.setMiddleName(client.getMiddlename());
			clientForm.setAddress(client.getAddress());
			clientForm.setCountry(client.getCountry());
			clientForm.setDob(client.getDob());
			clientForm.setEmail(client.getEmail());
			clientForm.setGender(client.getGender());
			clientForm.setMobNo(client.getMobNo());
			clientForm.setPostCode(client.getPostCode());
			clientForm.setReference(client.getReference());
			clientForm.setSourceOfIntro(client.getSourceOfIntro());
			
			//String city = clientDAO.getcityfromid(client.getTown());
			//city selection
			clientForm.setTown(client.getTown());
			clientForm.setId(client.getId());
			clientForm.setType(client.getType());
			
			clientForm.setAdhno(client.getAdhno());
			clientForm.setTpmemb(client.getTpmemb());
			
			if(client.getHospitalborn().equals("1")){
				clientForm.setHospitalborn(true);
			}else{
				clientForm.setHospitalborn(false);
			}
			
			clientForm.setUserImage(new File(request.getRealPath("/liveData/")+"/"+client.getImageName()));
			clientForm.setClientimg(client.getImageName());
			
			String typeid = client.getType();
			String null1 = null;
			if(typeid!= null1){
				thirdPartyTypeNameList = clientDAO.getThirdTypeNameList(Integer.parseInt(typeid));
			}
			clientForm.setDocument_name(client.getDocumentID());
			clientForm.setDocumentValue(client.getDocumentValue());	
			
			clientForm.setThirdPartyTypeNameList(thirdPartyTypeNameList);
			clientForm.setTypeName(client.getTypeName());
			clientForm.setExpiryDate(client.getExpiryDate());
			clientForm.setOccupation(client.getOccupation());
			
			clientForm.setExpiryDate(client.getExpiryDate());
			clientForm.setWhopay(client.getWhopay());
			clientForm.setPolicyAuthorzCode(client.getPolicyAuthorzCode());
			clientForm.setPolicyNo(client.getPolicyNo());
			clientForm.setKnownAs(client.getKnownAs());
			//String county = clientDAO.getstatefromid(client.getCounty());
			clientForm.setCounty(client.getCounty());
			clientForm.setHomeNo(client.getHomeNo());
			clientForm.setWorkNo(client.getWorkNo());
			clientForm.setEmailCc(client.getEmailCc());
			clientForm.setPrefContactMode(client.getPrefContactMode());
			clientForm.setEmergencyContName(client.getEmergencyContName());
			clientForm.setEmergencyContNo(client.getEmergencyContNo());
			clientForm.setPatientType(client.getPatientType());
			
			clientForm.setPolicyExcess(client.getPolicyExcess());
			String doctorsurgery = client.getGptypeName();
			ArrayList<GpData>gpList = clientDAO.getGPDataList(doctorsurgery);
			clientForm.setGpDataList(gpList);
			clientForm.setGpname(client.getGpname());
			clientForm.setGptypeName(client.getGptypeName());
			/*String tpiddata = clientDAO.getTpIdDetails(client.getGpname());
			if(!tpiddata.equals("")){
				String temp[] = tpiddata.split(",");
				
				ArrayList<GpData>gpList = clientDAO.getGPDataList(tpiddata);
				clientForm.setGpDataList(gpList);
				//clientForm.setGpname(client.getGpname());
				
				clientForm.setSurgeryName(tpiddata);
			}*/
			
			
			clientForm.setReferedDate(client.getReferedDate());
			clientForm.setEmployerName(client.getEmployerName());
			clientForm.setTreatmentType(client.getTreatmentType());
			clientForm.setId(id);
			clientForm.setSourceOfIntroName(client.getSourceOfIntroName());
			clientForm.setSecondLineaddress(client.getSecondLineaddress());
			//clientForm.setThirdPartyCompanyName(client.getThirdPartyCompanyName());
			
			//note
			clientForm.setClientNote(client.getClientNote());
			clientForm.setAccountNote(client.getAccountNote());
			clientForm.setClinicalNote(client.getClinicalNote());
			clientForm.setNhsNumber(client.getNhsNumber());
					//shubham 06/12/2018
			clientForm.setCompname(client.getCompname());
			clientForm.setNeisno(client.getNeisno());
			clientForm.setDesignationbytp(client.getDesignationbytp());
			clientForm.setRelationvbytpe(client.getRelationvbytpe());
			clientForm.setUnitstation(client.getUnitstation());
			clientForm.setClaimbytp(client.getClaimbytp());
			clientForm.setColliery(client.getColliery());
			clientForm.setAreabytp(client.getAreabytp());
			String companyname=clientDAO.gettptypenamebyid(client.getTypeName());
			
			if(companyname==null){
				companyname="";
			}
				
			if(companyname.equals("CGHS")){
				clientForm.setTptypenamestatus("CGHS");
			}else if(companyname.equals("WCL")){
				clientForm.setTptypenamestatus("WCL");
				
			}else if(companyname.equals("INSURANCE COMPANY")){
				clientForm.setTptypenamestatus("INSURANCE");
			}
			else{
				clientForm.setTptypenamestatus("");
			}
				clientForm.setPolicyholder(client.getPolicyholder());
				clientForm.setMaritalsts(client.getMaritalsts());
				
				clientForm.setMothername(client.getMothername());
				clientForm.setFathername(client.getFathername());
				clientForm.setBirthplace(client.getBirthplace());
				String btime=client.getBirthtime();
				String time[] = btime.split(":");
				String hh = time[0];
				String mm = time[1];
				clientForm.setHourls(hh);
				clientForm.setMinutels(mm);
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			connection.close();
		}
		return "editClientPage";
	}
	
	public String editPatient() throws SQLException{
		if(!verifyLogin(request)){
			return "login";
		}
		ArrayList<Client> thirdPartyTypeList = new ArrayList<Client>();
		ArrayList<Client> thirdPartyTypeNameList = new ArrayList<Client>();
		ArrayList<Client> clientOccupationList = new ArrayList<Client>();
		ArrayList<Client> refrenceList = new ArrayList<Client>();
		ArrayList<String> initialList = new ArrayList<String>();
		Connection connection = null;
		try{
			int id = Integer.parseInt(request.getParameter("selectedid"));
			Client client = new Client();
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			thirdPartyTypeList = clientDAO.getThirdPartyType();
			clientForm.setThirdPartyTypeList(thirdPartyTypeList);
			thirdPartyTypeNameList = clientDAO.getThirdPartyTypeName();
			clientForm.setThirdPartyTypeNameList(thirdPartyTypeNameList);
			
			clientOccupationList = clientDAO.getOccupationList();
			Client client1 = new Client();
			client1.getOther();
			clientOccupationList.add(client1);
			
			
			clientForm.setClientOccupationList(clientOccupationList);
			
			refrenceList = clientDAO.getReferenceList();
			Client client2 = new Client();
			client2.getOther();
			refrenceList.add(client2);
			clientForm.setRefrenceList(refrenceList);
			
			initialList = clientDAO.getInitialList();
			clientForm.setInitialList(initialList);
			
			ArrayList<Client>surgeryList = clientDAO.getSurgeryList();
			clientForm.setSurgeryList(surgeryList);
			
			client = clientDAO.getPatient(id);
			clientForm.setTitle(client.getTitle());
			clientForm.setFirstName(client.getFirstName());
			clientForm.setLastName(client.getLastName());
			clientForm.setMiddleName(client.getMiddlename());
			clientForm.setAddress(client.getAddress());
			clientForm.setCountry(client.getCountry());
			clientForm.setDob(client.getDob());
			clientForm.setEmail(client.getEmail());
			clientForm.setGender(client.getGender());
			clientForm.setMobNo(client.getMobNo());
			clientForm.setPostCode(client.getPostCode());
			clientForm.setReference(client.getReference());
			clientForm.setSourceOfIntro(client.getSourceOfIntro());
			clientForm.setTown(client.getTown());
			clientForm.setId(client.getId());
			clientForm.setType(client.getType());
			clientForm.setTypeName(client.getTypeName());
			clientForm.setExpiryDate(client.getExpiryDate());
			clientForm.setOccupation(client.getOccupation());
			
			clientForm.setExpiryDate(client.getExpiryDate());
			clientForm.setWhopay(client.getWhopay());
			clientForm.setPolicyAuthorzCode(client.getPolicyAuthorzCode());
			clientForm.setPolicyNo(client.getPolicyNo());
			clientForm.setKnownAs(client.getKnownAs());
			clientForm.setCounty(client.getCounty());
			clientForm.setHomeNo(client.getHomeNo());
			clientForm.setWorkNo(client.getWorkNo());
			clientForm.setEmailCc(client.getEmailCc());
			clientForm.setPrefContactMode(client.getPrefContactMode());
			clientForm.setEmergencyContName(client.getEmergencyContName());
			clientForm.setEmergencyContNo(client.getEmergencyContNo());
			clientForm.setPatientType(client.getPatientType());
			clientForm.setPolicyExcess(client.getPolicyExcess());
			clientForm.setUserImage(new File(client.getImageName()));
			
			
			
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			connection.close();
		}
		return "editPatientPage";
	}
	
	public String update() throws SQLException{
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{
			int id = clientForm.getId();
			Client client = new Client();
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			if(clientForm.getOtherRef().equals(null) || clientForm.getOtherRef().equals(""))
			{
				client.setReference(clientForm.getReference());
				
			}else{
				String reference = clientForm.getOtherRef();
				int save = clientDAO.insertOtherReference(client, reference);
				int id1 = clientDAO.getMaxIdOfRefernce();
				client.setReference(Integer.toString(id1));
			
			}
			if(clientForm.getOtherSourceOfIntro().equals(null) || clientForm.getOtherSourceOfIntro().equals(""))
			{
				client.setSourceOfIntro(clientForm.getSourceOfIntro());

			}
			else{
				String otherSourceOfIntro = clientForm.getOtherSourceOfIntro();
				int save = clientDAO.insertOtherSourceOfIntro(client, otherSourceOfIntro);
				int id1 = clientDAO.getMaxIdOfSourceOfIntro();
				client.setSourceOfIntro(Integer.toString(id1));
			}
			if(clientForm.getOtherOccupation().equals(null) || clientForm.getOtherOccupation().equals(""))
			{
				client.setOccupation(clientForm.getOccupation());

			}
			else{
				String otherOccupation = clientForm.getOtherOccupation();
				int save = clientDAO.insertOtherOccupation(client, otherOccupation);
				int id1 = clientDAO.getMaxIdOfOccupation();
				client.setOccupation(Integer.toString(id1));
			}
			
			if(clientForm.getOtherCondition().equals(null) || clientForm.getOtherCondition().equals(""))
			{
				client.setTreatmentType(clientForm.getTreatmentType());

			}
			else{
				String otherCondition = clientForm.getOtherCondition();
				int save = clientDAO.insertOtherCondition(client, otherCondition);
				int id1 = clientDAO.getMaxIdOfCondition();
				client.setTreatmentType(Integer.toString(id1));
			}
			client.setTitle(clientForm.getTitle());
			client.setFirstName(clientForm.getFirstName());
			client.setMiddlename(clientForm.getMiddleName());
			client.setLastName(clientForm.getLastName());
			client.setAddress(clientForm.getAddress());
			client.setCountry(clientForm.getCountry());
			client.setDob(clientForm.getDob());
			client.setEmail(clientForm.getEmail());
			client.setGender(clientForm.getGender());
			client.setMobNo(clientForm.getMobNo());
			client.setPostCode(clientForm.getPostCode());
			//client.setReference(clientForm.getReference());
			//client.setSourceOfIntro(clientForm.getSourceOfIntro());
			client.setTown(clientForm.getTown());
			client.setType(clientForm.getType());
			client.setTypeName(clientForm.getTypeName());
			//client.setOccupation(clientForm.getOccupation());
			client.setExpiryDate(clientForm.getExpiryDate());
			client.setWhopay(clientForm.getWhopay());
			client.setPolicyAuthorzCode(clientForm.getPolicyAuthorzCode());
			client.setPolicyNo(clientForm.getPolicyNo());
			client.setKnownAs(clientForm.getKnownAs());
			client.setCounty(clientForm.getCounty());
			client.setHomeNo(clientForm.getHomeNo());
			client.setWorkNo(clientForm.getWorkNo());
			client.setEmailCc(clientForm.getEmailCc());
			client.setPrefContactMode(clientForm.getPrefContactMode());
			client.setEmergencyContName(clientForm.getEmergencyContName());
			client.setEmergencyContNo(clientForm.getEmergencyContNo());
			client.setPatientType(clientForm.getPatientType());
			//client.setGpname(clientForm.getGpname());
			client.setEmployerName(clientForm.getEmployerName());
			client.setReferedDate(clientForm.getReferedDate());
			//client.setTreatmentType(clientForm.getTreatmentType());
			client.setPolicyExcess(clientForm.getPolicyExcess());
			client.setGpname(clientForm.getGpname());
			client.setDoctorsurgery(clientForm.getGptypeName());
			client.setSourceOfIntroName(clientForm.getSourceOfIntroName());
			client.setSecondLineaddress(clientForm.getSecondLineaddress());
			client.setDocumentID(clientForm.getDocument_name());
			client.setDocumentValue(clientForm.getDocumentValue());
			//update adhar no and tp membership no
			client.setAdhno(clientForm.getAdhno());
			client.setTpmemb(clientForm.getTpmemb());
			
			
			clientDAO.addToHISDocumentLog(clientForm.getDocument_name(), clientForm.getDocumentValue(), ""+id);
			
			//note
			client.setClientNote(clientForm.getClientNote());
			client.setAccountNote(clientForm.getAccountNote());
			client.setClinicalNote(clientForm.getClinicalNote());
			client.setLastModifiedDate(DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
			client.setNhsNumber(clientForm.getNhsNumber());
			if(clientForm.isHospitalborn()){
				client.setHospitalborn("1");
			}else{
				client.setHospitalborn("0");
			}
			File userImage = clientForm.getUserImage();
			String userImageFileName = clientForm.getUserImageFileName();
			if(userImageFileName!=null){			
				userImageFileName = loginInfo.getClinicUserid()+"_"+id+"_"+userImageFileName;
			}else{
				
				userImageFileName = loginInfo.getClinicUserid()+"_"+id+"_profileimg.png";
				
				String filepath = request.getRealPath("/liveData/"+userImageFileName);
				String clientImageData = clientForm.getProfileimg();
				byte[] imagedata = DatatypeConverter.parseBase64Binary(clientImageData.substring(clientImageData.indexOf(",") + 1));
				BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagedata));
				if(!clientForm.getProfileimg().equals("")){
					ImageIO.write(bufferedImage, "png", new File(filepath));
				}
			}
			
            String userImageContentType = clientForm.getUserImageContentType();
			
			if(clientForm.getUserImageContentType()!=null){
			String filePath = request.getRealPath("/liveData/");			
			System.out.println("Server path:" + filePath);
			File fileToCreate = new File(filePath, userImageFileName);
			FileUtils.copyFile(userImage, fileToCreate);
			

			//update image name
			int res=clientDAO.updateClientUserImage(userImageFileName,id);
			
			}
			
			//shubham 06/12/2018
			client.setCompname(clientForm.getCompname());
			client.setNeisno(clientForm.getNeisno());
			client.setDesignationbytp(clientForm.getDesignationbytp());
			client.setUnitstation(clientForm.getUnitstation());
			client.setRelationvbytpe(clientForm.getRelationvbytpe());
			client.setClaimbytp(clientForm.getClaimbytp());
			client.setColliery(clientForm.getColliery());
			client.setAreabytp(clientForm.getAreabytp());
			client.setPolicyholder(clientForm.getPolicyholder());
			if(clientForm.getMaritalsts().equals("0")){
				client.setMaritalsts("");
			}else{
			client.setMaritalsts(clientForm.getMaritalsts());
		}
			
			client.setMothername(clientForm.getMothername());
			client.setFathername(clientForm.getFathername());
			client.setBirthplace(clientForm.getBirthplace());
			String birthtime=clientForm.getHourls()+":"+clientForm.getMinutels()+":00";
			client.setBirthtime(birthtime);
			int result = clientDAO.updatePatient(client,id,loginInfo);
			clientForm.setMessage("Patient Updated Suceessfully");
			addActionMessage("");
			
			setFromData();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			connection.close();
		}
		return "updateClient";
	}
	
	public String delete() throws SQLException{
		if(!verifyLogin(request)){
			return "login";
		}
		int result = 0;
		int id =Integer.parseInt(request.getParameter("selectedid"));
		Connection connection = null;
		Client client = new Client();
		try{
			
			connection = Connection_provider.getconnection();
			
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			
			result = clientDAO.deleteClient(id,client);
			clientForm.setMessage("Patient Removed Suceessfully");
			addActionMessage("Patient Removed Suceessfully");
			
			setFromData();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			connection.close();
		}
		return "manage";
	}
	//Client Third Party Log // Unnati date : 19th march
	
	public String thirdPartyLog() throws SQLException{
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		Client client = new Client();
		ArrayList<Client> thirdPartyTypeList = new ArrayList<Client>();
		ArrayList<Client> thirdPartyTypeNameList = new ArrayList<Client>();
		try{
			
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			thirdPartyTypeList = clientDAO.getThirdPartyType();
			clientForm.setThirdPartyTypeList(thirdPartyTypeList);
			
			thirdPartyTypeNameList = clientDAO.getThirdPartyTypeName();
			clientForm.setThirdPartyTypeNameList(thirdPartyTypeNameList);
			
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			connection.close();
		}
		return "thirdPartyLog";
	}
	
	
	public String setTypeNameClntDropDown() throws Exception{
		
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			 
			
			int id = Integer.parseInt(request.getParameter("id"));
			
			
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			 ArrayList<Client>ajaxTypeNameList = clientDAO.getThirdTypeNameList(id);
			

			 StringBuffer dropDownAjax = new StringBuffer();
				dropDownAjax.append("<select id='typeName' name = 'typeName' class='form-control showToolTip chosen' data-toggle='tooltip'>");
					dropDownAjax.append("<option value = '0'>Select Third Party </option>");
					if(ajaxTypeNameList.size()!=0){
						for(Client client : ajaxTypeNameList){
							dropDownAjax.append("<option value = '"+client.getId()+"'>"+client.getThirdPartyCompanyName()+"</option>");
						}
						
					}
				dropDownAjax.append("</select>");
				response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				
				response.getWriter().write(""+dropDownAjax.toString()+""); 
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
		
		
	}
	
	public String setTypeNameDropDown() throws Exception{
		
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			 
			
			int id = Integer.parseInt(request.getParameter("id"));
			
			
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			 ArrayList<Client>ajaxTypeNameList = clientDAO.getThirdTypeNameList(id);
			

			 StringBuffer dropDownAjax = new StringBuffer();
				dropDownAjax.append("<select id='typeName' name = 'typeName' class='form-control showToolTip chosen' data-toggle='tooltip'>");
					dropDownAjax.append("<option value = '0'>Select Third Party </option>");
					if(ajaxTypeNameList.size()!=0){
						for(Client client : ajaxTypeNameList){
							dropDownAjax.append("<option value = '"+client.getId()+"'>"+client.getThirdPartyCompanyName()+"</option>");
						}
						
					}
				dropDownAjax.append("</select>");
				response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				
				response.getWriter().write(""+dropDownAjax.toString()+""); 
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
		
		
	}
	
	public String setCompanyNameDropDown() throws Exception{
		
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			 
			
			int id = Integer.parseInt(request.getParameter("id"));
			
			
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			 ArrayList<Client>ajaxTypeNameList = clientDAO.getThirdTypeNameList(id);
			

			 StringBuffer dropDownAjax = new StringBuffer();
				dropDownAjax.append("<select id='ctpName' name = 'ctpName' class='form-control showToolTip chosen' data-toggle='tooltip'>");
					dropDownAjax.append("<option value = '0'>Select Company Name </option>");
					if(ajaxTypeNameList.size()!=0){
						for(Client client : ajaxTypeNameList){
							dropDownAjax.append("<option value = '"+client.getId()+"'>"+client.getThirdPartyCompanyName()+"</option>");
						}
						
					}
				dropDownAjax.append("</select>");
				response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				
				response.getWriter().write(""+dropDownAjax.toString()+""); 
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
		
		
	}
	
	
	public String setPopupTypeNameDropDown() throws Exception{
		
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			 
			
			int id = Integer.parseInt(request.getParameter("id"));
			
			
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			 ArrayList<Client>ajaxTypeNameList = clientDAO.getThirdTypeNameList(id);
			

			 StringBuffer dropDownAjax = new StringBuffer();
				dropDownAjax.append("<select id='popuptypeName' name = 'typeName' class='form-control showToolTip chosen' data-toggle='tooltip'>");
					dropDownAjax.append("<option value = '0'>Select TP Name</option>");
					if(ajaxTypeNameList.size()!=0){
						for(Client client : ajaxTypeNameList){
							dropDownAjax.append("<option value = '"+client.getId()+"'>"+client.getThirdPartyCompanyName()+"</option>");
						}
						
					}
				dropDownAjax.append("</select>");
				response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				
				response.getWriter().write(""+dropDownAjax.toString()+""); 
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
		
		
	}
	
	/*public String setgpList(){
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			 
			
			String surgeryid = request.getParameter("id");
			
			
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			ArrayList<GpData>gpList = clientDAO.getGPDataList(surgeryid);;
			

			 StringBuffer dropDownAjax = new StringBuffer();
				dropDownAjax.append("<select id='gpname1' name = 'gpname' class='form-control showToolTip chosen' data-toggle='tooltip'>");
					dropDownAjax.append("<option value = '0'>Select GP</option>");
					if(gpList.size()!=0){
						for(Client client : gpList){
							dropDownAjax.append("<option value = '"+client.getId()+"'>"+client.getTypeName()+"</option>");
						}
						
					}
				dropDownAjax.append("</select>");
				response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				
				response.getWriter().write(""+dropDownAjax.toString()+""); 
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
		
	}*/
	
	public String updateThirdPartyDetails() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{
			String id = clientForm.getClientId();
			Client client = new Client();
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			
			client.setType(clientForm.getType());
			client.setTypeName(clientForm.getTypeName());
			int result = clientDAO.updateThirdPartyDetails(client,id);
		}
		catch (Exception e) {
			// TODO: handle exception
		}finally{
			
			connection.close();
		}
		
		return "updateThirdPartyDetails";
	}
	
	public String addOtherOccupation() throws SQLException{
		if(!verifyLogin(request)){
			return "login";
		}
		String otherOccupation = request.getParameter("otherOccupation");
		Connection connection = null;
		try{
			String id = clientForm.getClientId();
			Client client = new Client();
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			
			
			int result = clientDAO.insertOtherOccupation(client,otherOccupation);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			connection.close();
		}
		return null;
	}
	
	public String addOtherReference() throws SQLException{
		if(!verifyLogin(request)){
			return "login";
		}
		String otherReference = request.getParameter("otherReference");
		Connection connection = null;
		try{
			String id = clientForm.getClientId();
			Client client = new Client();
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			
			
			int result = clientDAO.insertOtherReference(client,otherReference);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			connection.close();
		}
		return null;
	}
	
	public String addOtherSourceOfIntro() throws SQLException{
		if(!verifyLogin(request)){
			return "login";
		}
		String otherSourceOfIntro = request.getParameter("otherSourceOfIntro");
		Connection connection = null;
		try{
			String id = clientForm.getClientId();
			Client client = new Client();
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			
			
			int result = clientDAO.insertOtherSourceOfIntro(client,otherSourceOfIntro);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			connection.close();
		}
		return null;
	}
	public String addOtherCondition() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		String otherCondition = request.getParameter("otherCondition");
		Connection connection = null;
		try{
			String id = clientForm.getClientId();
			Client client = new Client();
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			
			
			int result = clientDAO.insertOtherCondition(client,otherCondition);
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			connection.close();
		}
		return null;
	}
	public String search() throws SQLException{
		if(!verifyLogin(request)){
			return "login";
		}
		String ipdidsearch=DateTimeUtils.isNull(request.getParameter("manualipdseq"));
		request.setAttribute("manualipdseq", ipdidsearch);
		pagination.setManualIpdId(ipdidsearch);
		String searchClient = clientForm.getSearchText();
		String status = clientForm.getStatus();
		if(searchClient==null || searchClient ==""){
			searchClient = (String) session.getAttribute("searchClient");
		}
		if(status==null || status==""){
			
			status = (String) session.getAttribute("status");
		}
		String all = "All";
		
		if(status.equals(all)){
			status= "";
		}
		if(clientForm.getDiaryUser().equals("")){
			clientForm.setDiaryUser("0");
		}
		if(ipdidsearch==null){
			ipdidsearch="";
		}
		
		Connection connection = null;
		ArrayList<Client> allPatientList = new ArrayList<Client>();
		try{
			connection = Connection_provider.getconnection();
			
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			IpdDAO ipdDAO= new JDBCIpdDAO(connection);
			if(!ipdidsearch.equals("")){
				ipdidsearch= ipdDAO.getClientIdByIpdSeqno(ipdidsearch);
			}
			if(ipdidsearch!=null){
				if(!ipdidsearch.equals("")){
					searchClient=ipdidsearch;
				}
			}
			int totalCount = clientDAO.getTotalClientCountOfSearch(loginInfo.getId(),searchClient,status,clientForm.isShowAll(),clientForm.getDiaryUser());
			pagination.setPreperties(totalCount);
			

			allPatientList = clientDAO.getClientofSearch(pagination,searchClient,loginInfo.getId(),status,clientForm.isShowAll(),clientForm.getDiaryUser());
			pagination.setPage_records(allPatientList.size());
			clientForm.setTotalRecords(totalCount);
			clientForm.setPagerecords(Integer.toString(pagination.getPage_records()));
			clientForm.setAllPatientList(allPatientList);
			clientForm.setStatus(status);
			
			session.setAttribute("searchClient", searchClient);
			session.setAttribute("status", status);
			session.setAttribute("csdiaryuser", clientForm.getDiaryUser());
			session.setAttribute("showall", clientForm.isShowAll());
			//session.setAttribute("pagination", pagination);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			connection.close();
		}
		
		return "manage";
	}
	
	
	public String newsaved() throws SQLException{
		
		if(!verifyLogin(request)){
			return "login";
		}
		/*String searchClient = clientForm.getSearchText();
		String status = clientForm.getStatus();
		if(searchClient==null || searchClient ==""){
			searchClient = (String) session.getAttribute("searchClient");
		}
		if(status==null || status==""){
			
			status = (String) session.getAttribute("status");
		}
		String all = "All";
		
		if(status.equals(all)){
			status= "";
		}*/
		Connection connection = null;
		ArrayList<Client> allPatientList = new ArrayList<Client>();
		try{
			connection = Connection_provider.getconnection();
			
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			
			clientForm.setShowAll(true);
			
			
			int totalCount = clientDAO.getTotalClientCountOfSearch(loginInfo.getId(),"","",clientForm.isShowAll(),clientForm.getDiaryUser());
			pagination.setPreperties(totalCount);
			

			allPatientList = clientDAO.getClientofSearch(pagination,"",loginInfo.getId(),"",clientForm.isShowAll(),clientForm.getDiaryUser());
			pagination.setPage_records(allPatientList.size());
			clientForm.setTotalRecords(totalCount);
			clientForm.setPagerecords(Integer.toString(pagination.getPage_records()));
			clientForm.setAllPatientList(allPatientList);
			/*clientForm.setStatus(status);
			
			session.setAttribute("searchClient", searchClient);
			session.setAttribute("status", status);
			session.setAttribute("csdiaryuser", clientForm.getDiaryUser());
			session.setAttribute("showall", clientForm.isShowAll());*/
			//session.setAttribute("pagination", pagination);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			connection.close();
		}
		
		return "save";
	}
	
	
	public String selected() throws Exception{
		
		String selectedid = request.getParameter("selectedid");
		session.setAttribute("sessionselectedclientid", selectedid);
		session.removeAttribute("sessionadmissionid");
		
		Connection connection = null;
		try{
			
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			
			Client client = clientDAO.getSelectedSessionClientDetails(selectedid);
			
			session.setAttribute("sessionselectedclientName", client.getClientName());
			session.removeAttribute("diaryUserId");
			session.removeAttribute("conditionId");
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			connection.close();
		}
		
		System.out.println(selectedid);
		
		return null;
	}
	
	public String searchPatient() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		String searchClient = request.getParameter("searchText");
		Connection connection = null;
		ArrayList<Client> allPatientList = new ArrayList<Client>();
		try{
			connection = Connection_provider.getconnection();
			
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			allPatientList = clientDAO.getClient(searchClient,loginInfo.getId());
			
			clientForm.setAllPatientList(allPatientList);
			StringBuffer str = new StringBuffer();
				
			str.append("<table width = '50%' id = 'allPatient' cellpadding='0' cellspacing='0' class='my-table' > ");
			str.append("<tr>");
			str.append("<th>Id</th> ");
			str.append("<th>Adhar_No</th> ");
			str.append("<th>Name</th> ");
			str.append("<th>Old Id</th> ");
			str.append("<th>Mobile</th> ");
			str.append("<th>Email</th> ");
			str.append("<th>Post Code</th> ");
			str.append("<th>DOB</th> ");
			str.append("<th>LastModified</th> ");
			str.append("</tr>");
			
			for(Client client1:allPatientList){
			str.append("<tr>");
			String name = client1.getTitle()+" "+client1.getFirstName()+" "+client1.getLastName(); 	
			String firstName= client1.getFirstName();
			if(client1.getAbrivationid()==null){
				str.append("<td>0000"+client1.getId()+"</td>");
			}else{
				str.append("<td>"+client1.getAbrivationid()+"/"+client1.getId()+"</td>");
			}
			
			str.append("<td>"+client1.getAdhno()+"</td>");

			String whopay = "";
			if(client1.getWhopay().equals(Constants.PAY_BY_THIRD_PARTY)){
				whopay = "tp";
			}else{
				whopay = "Client";
			}

			String fullname = client1.getTitle() + "/" + client1.getFirstName() + "/" + client1.getLastName();

			str.append("<td style='cursor: pointer;'; onclick = setClientName('"+client1.getId()+"','"+client1.getType()+"','"+client1.getTypeName()+"','"+whopay+"','"+client1.getGpname()+"')>"+name+"</td>");
			
			str.append("<input type='hidden' id='firstnameid"+client1.getId()+"' value='"+fullname+"'>");
			if(client1.getOldclientId()==null){
				client1.setOldclientId("");
			}
			str.append("<td>"+client1.getOldclientId()+"</td>");

			str.append("<td>"+client1.getMobNo()+"</td>");
			str.append("<td>"+client1.getEmail()+"</td>");
			str.append("<td>"+client1.getPostCode()+"</td>");
			str.append("<td>"+client1.getDob()+"</td>");
			if(client1.getLastModified()==null){
				client1.setLastModified("");
			}
			str.append("<td>"+client1.getLastModified()+"</td>");

			str.append("</tr>");
			}
			
			
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
	
	public String searchParticularPatient() throws SQLException{
 		if(!verifyLogin(request)){
			return "login";
		}
		String searchClient = request.getParameter("searchText");
		String ipdno= request.getParameter("ipdno");
		
		Connection connection = null;
		ArrayList<Client> allPatientList = new ArrayList<Client>();
		try{
			connection = Connection_provider.getconnection();
			if(ipdno!=null){
				if(!ipdno.equals("")){
					IpdDAO ipdDAO= new JDBCIpdDAO(connection);
					ipdno=ipdDAO.getClientIdByIpdSeqno(ipdno);
				}
			}
			if(ipdno!=null){
				if(!ipdno.equals("")){
					searchClient=ipdno;
					}
				}
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			allPatientList = clientDAO.getClient(searchClient,loginInfo.getId());
			
			clientForm.setAllPatientList(allPatientList);
			StringBuffer str = new StringBuffer();
				
			str.append("<table class = 'table table-bordered' > ");
			str.append("<thead>");
			str.append("<tr class = 'bg-info'>");
			str.append("<th>Id</th> ");

			str.append("<th>Name</th> ");
			str.append("<th>Old Id</th> ");
			str.append("<th>Mobile</th> ");
			str.append("<th>Email</th> ");
			str.append("<th>Post Code</th> ");
			str.append("<th>Dob</th> ");

			str.append("</tr>");
			str.append("</thead>");
			
			str.append("<tbody>");
			for(Client client1:allPatientList){
			String firstName= client1.getFirstName();
			str.append("<tr style='cursor: pointer;'; onclick = setPatientName('"+client1.getId()+"','"+client1.getTypeName()+"','"+client1.getType()+"')>");
			str.append("<input type='hidden' id='firstnameid"+client1.getId()+"' value='"+firstName+"'>");
			if(client1.getAbrivationid()==null){
				str.append("<td>0000"+client1.getId()+"</td>");
			}else{
				str.append("<td>"+client1.getAbrivationid()+"/"+client1.getId()+"</td>");
			}
			

			String name = client1.getTitle()+" "+client1.getFirstName()+" "+client1.getMiddlename()+" "+client1.getLastName(); 	
			str.append("<td>"+name+"</td>");
			if(client1.getOldclientId()==null){
				client1.setOldclientId("");
			}
			str.append("<td>"+client1.getOldclientId()+"</td>");
			str.append("<td>"+client1.getMobNo()+"</td>");
			str.append("<td>"+client1.getEmail()+"</td>");
			str.append("<td>"+client1.getPostCode()+"</td>");
			str.append("<td>"+client1.getDob()+"</td>");

			str.append("</tr>");
			}
			str.append("<tbody>");
			
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
	
	public String searchParticular() throws SQLException{
		if(!verifyLogin(request)){
			return "login";
		}
		String searchClient = clientForm.getSearchText();
		Connection connection = null;
		ArrayList<Client> allPatientList = new ArrayList<Client>();
		try{
			connection = Connection_provider.getconnection();
			
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			allPatientList = clientDAO.getClient(searchClient,loginInfo.getId());
			
			clientForm.setAllPatientList(allPatientList);
		}
		
		catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			connection.close();
		}
		return SUCCESS;
	}
	
	public String savePatient() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		String title = request.getParameter("title");
		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String middleName = request.getParameter("middleName");
				
		String gender = request.getParameter("gender");
		String dob = request.getParameter("dob");
		String address = request.getParameter("address");
		String town = request.getParameter("town");
		String state=request.getParameter("state");
		
		String country = request.getParameter("country");
		String postCode = request.getParameter("postCode");
		String mobNo = request.getParameter("mobNo");
		String email = request.getParameter("email");
		String reference = request.getParameter("reference");
		String type = request.getParameter("type");
		String company = request.getParameter("company");
		String occupation=request.getParameter("occupation");
		
		String policyNo = request.getParameter("policyNo");
		String expiryDate = request.getParameter("expiryDate");
		String whopay = request.getParameter("whopay");
		String gpname = request.getParameter("gpname");
		String tratmentType = request.getParameter("tratmentType");
		String policyExcess = request.getParameter("policyExcess");
		String secondLineaddress = request.getParameter("secondLineaddress");
		String doctorsurgery = request.getParameter("doctorsurgery");
		String age=request.getParameter("age");
		String adhno = request.getParameter("adhno");
		String relativename = request.getParameter("relativename");
		String relativeno = request.getParameter("relativeno");
		String hospitalborn = request.getParameter("hospitalborn");
		String compname = request.getParameter("compname");
		String neisno = request.getParameter("neisno");
		String designationbytp = request.getParameter("designationbytp");
		String unitstation = request.getParameter("unitstation");
		String claimbytp = request.getParameter("claimbytp");
		String relationvbytp= request.getParameter("relationvbytp");
		String colliery = request.getParameter("colliery");
		String areabytp = request.getParameter("areabytp");
		String policyholder=request.getParameter("policyholder");
		String maritalsts=request.getParameter("maritalsts");
		Connection connection = null;
		try{
			
			Client client = new Client();
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			
			//save abrivation seq no
			String cdate = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
			boolean checkifseq = clientDAO.checkifSequenceExist(cdate);
			String abrivationid = "";
			String clinicabrivation = clientDAO.getClinicAbrivation(loginInfo.getClinicid());
			String tempd[] = cdate.split("-");
			String y = tempd[0];
			String m = tempd[1];
			String d = tempd[2];
			String newseq="";
			if(checkifseq){
				int seqno = clientDAO.getSqeunceNumber(cdate);
				seqno++;
				int r = clientDAO.InserCdateSeq(cdate,seqno);
				//SNH170609001
				int yr = Integer.parseInt(y)%1000;
				
				 if(String.valueOf(seqno).length()==1)
				{
					 newseq="00"+seqno;
				}else if(String.valueOf(seqno).length()==2){
					 newseq="0"+seqno;
				}else{
					newseq=""+seqno;
				}
				/*abrivationid = clinicabrivation + yr + m +d + "00" + seqno;*/
				abrivationid = clinicabrivation + yr + m +d + newseq;
			}else{
				
				int seqno = 1;
				int r = clientDAO.InserCdateSeq(cdate,seqno);
				//String seqno = clientDAO.getSqeunceNumber(cdate);
				int yr = Integer.parseInt(y)%1000;
				if(String.valueOf(seqno).length()==1)
				{
					 newseq="00"+seqno;
				}else if(String.valueOf(seqno).length()==2){
					 newseq="0"+seqno;
				}else{
					newseq=""+seqno;
				}
				abrivationid = clinicabrivation + yr + m +d + newseq;
			}
			
			client.setTitle(title);
			client.setFirstName(firstName);
			client.setLastName(lastName);
			client.setAddress(address);
			client.setCountry(country);
			client.setDob(dob);
			client.setEmail(email.trim());
			client.setGender(gender);
			client.setMobNo(mobNo.trim());
			client.setPostCode(postCode.trim());
			client.setReference(reference);
			client.setTown(town);
			client.setGpname(gpname);
			client.setThirdPartyType(type);
			client.setThirdPartyCompanyName(company);
			client.setPolicyNo(policyNo.trim());
			client.setExpiryDate(expiryDate);
			client.setWhopay(whopay);
			client.setMiddlename(middleName);
			client.setTreatmentType(tratmentType);
			client.setPolicyExcess(policyExcess);
			client.setSecondLineaddress(secondLineaddress);
			client.setDoctorsurgery(doctorsurgery);
			client.setState(state);
			client.setOccupation(occupation);
			client.setAdhno(adhno);
			client.setRelativename(relativename);
			client.setRelativeno(relativeno);
			client.setAbrivationid(abrivationid);
			client.setHospitalborn(hospitalborn);
			
			client.setFathername(request.getParameter("fathername"));
			client.setMothername(request.getParameter("mothername"));
			client.setBirthplace(request.getParameter("birthplace"));
			client.setBirthtime(request.getParameter("fulltime"));
			if(compname==null)
			{
				client.setCompname("");
			}else{
				client.setCompname(compname);
			}
			if(neisno==null)
			{
				client.setNeisno("");
			}else{
				client.setNeisno(neisno);
			}
			if(designationbytp==null)
			{
				client.setDesignationbytp("");
			}else{
				client.setDesignationbytp(designationbytp);
			}if(unitstation==null)
			{
				client.setUnitstation("");
			}else{
				client.setUnitstation(unitstation);
			}if(claimbytp==null)
			{
				client.setClaimbytp("");
			}else{
				client.setClaimbytp(claimbytp);
			}
			
			
			
			if(relationvbytp==null)
			{
				client.setRelationvbytpe("");
			}else{
				client.setRelationvbytpe(relationvbytp);
			}
			if(colliery==null)
			{
				client.setColliery("");
			}else{
				client.setColliery(colliery);
			}if(areabytp==null)
			{
				client.setAreabytp("");
			}else{
				client.setAreabytp(areabytp);
			}
			if(policyholder==null)
			{
				client.setPolicyholder("");
			}else{
				client.setPolicyholder(policyholder);
			}
			
			
			
			
			
			if(age!=null){
				
				if(age.equals("")){
				
					age="0";
				}
				
			} else {
				age="0";
			}

			Calendar calendar=Calendar.getInstance();
			int year=calendar.get(Calendar.YEAR);
			int ageee=Integer.parseInt(age);
			
			int dobyear=year-ageee;
			
			SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
			String date=dateFormat.format(calendar.getTime());
			String splitdate[]=date.split("/");
			String dodd=splitdate[0]+"/"+splitdate[1]+"/"+dobyear;
			client.setAge(ageee);
			if(maritalsts==null){
				client.setMaritalsts("");
			}else if(maritalsts.equals("0")){
				client.setMaritalsts("");
			}
			else{
			client.setMaritalsts(maritalsts);
			}
		//	client.setDob(dodd);
		
			int result = clientDAO.savePatientDetails(client,loginInfo.getId());
			
			ArrayList<Client> allPatientList = new ArrayList<Client>();
			allPatientList = clientDAO.getAllPatient(loginInfo.getId());
			StringBuffer str = new StringBuffer();
			
			str.append("<table width = '50%' id = 'allPatient' cellpadding='0' cellspacing='0' class='my-table' > ");
			str.append("<tr>");
			str.append("<th>Id</th> ");
			str.append("<th>Adhar No</th> ");
			str.append("<th>Name</th> ");
			str.append("<th>Old Id</th> ");
			str.append("<th>Mobile</th> ");
			str.append("<th>Email</th> ");
			str.append("<th>Post Code</th> ");
			str.append("<th>Dob</th> ");

			str.append("</tr>");
			
			for(Client client1:allPatientList){
				String name = client1.getTitle()+" "+client1.getFirstName()+" "+client1.getMiddlename()+" "+client1.getLastName(); 		
			str.append("<tr>");
			String firstName1= client1.getFirstName();
			str.append("<td>0000"+client1.getAbrivationid()+"</td>");
			str.append("<td>"+client1.getAdhno()+"</td>");

			//str.append("<td onclick = setClientName('"+firstName1+"','"+client1.getId()+"','"+client1.getType()+"','"+client1.getTypeName()+"')>"+name+"</td>");
			
			//String whopay = "";
			if(client1.getWhopay().equals(Constants.PAY_BY_THIRD_PARTY)){
				whopay = "tp";
			}else{
				whopay = "Client";
			}

			String fullname = client1.getTitle() + "/" + client1.getFirstName() + "/" + client1.getLastName();

			str.append("<td style='cursor: pointer;'; onclick = setClientName('"+client1.getId()+"','"+client1.getType()+"','"+client1.getTypeName()+"','"+whopay+"','"+client1.getGpname()+"')>"+name+"</td>");
			str.append("<input type='hidden' id='firstnameid"+client1.getId()+"' value='"+fullname+"'>");
		

			
			if(client1.getOldclientId()==null){
				client1.setOldclientId("");
			}
			str.append("<td>"+client1.getOldclientId()+"</td>");

			str.append("<td>"+client1.getMobNo()+"</td>");
			str.append("<td>"+client1.getEmail()+"</td>");
			str.append("<td>"+client1.getPostCode()+"</td>");
			str.append("<td>"+client1.getDob()+"</td>");

			str.append("</tr>");
			}
			
			
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
	
	public String addThirdPartyDetails() throws Exception {
		if(!verifyLogin(request)){
			return "login";
		}
		String clientId = request.getParameter("clientId");
		String thirdPartyType = request.getParameter("thirdPartyType");
		String companyName = request.getParameter("companyName");
		String thirdPartyPolicyNo = request.getParameter("thirdPartyPolicyNo");
		String thirdPartyExpiryDate = request.getParameter("thirdPartyExpiryDate");
		String tppolicyExcess = request.getParameter("tppolicyExcess");
		Connection connection = null;
		try{
			
			Client client = new Client();
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			
			client.setThirdPartyType(thirdPartyType);
			client.setThirdPartyCompanyName(companyName);
			client.setPolicyNo(thirdPartyPolicyNo);
			client.setExpiryDate(thirdPartyExpiryDate);
			client.setPolicyExcess(tppolicyExcess);
			
			
		
			int result = clientDAO.updateThirdParty(client,clientId,loginInfo.getId());
			 companyName = clientDAO.getThirdPartyCompanyName(companyName);
			StringBuffer textAjax = new StringBuffer();
			textAjax.append("<input type = 'text' id = 'invoicee' name = 'invoicee' class = 'form-control' value = '"+companyName+"'>");
			textAjax.append("<label id = 'invoiceeError' class='text-danger'></label>");
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+textAjax.toString()+""); 
		}
		catch(Exception e){
			e.printStackTrace();
		}finally{
			
			connection.close();
		}
		return null;
	}
	public String getFullName() throws Exception{
		if(!verifyLogin(request)){
		return "login";
	}
	String id = request.getParameter("id");
	Connection connection = null;
	try{
		
		Client client = new Client();
		connection = Connection_provider.getconnection();
		ClientDAO clientDAO = new JDBCClientDAO(connection);
		String fullName = clientDAO.getClientFullName(id);
		StringBuffer str = new StringBuffer();
		str.append(fullName);
		
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+str.toString()+""); 
	}
	catch(Exception e){
		e.printStackTrace();
	}finally{
		
		connection.close();
	}
	return null;
}

	public String showPatientList() throws SQLException{
		if(!verifyLogin(request)){
			return "login";
		}
		
		Connection connection = null;
		Client client = new Client();
		ArrayList<Client> allPatientList = new ArrayList<Client>();
		try{
			connection = Connection_provider.getconnection();
			
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			allPatientList = clientDAO.getAllPatient(loginInfo.getId());
			
			clientForm.setAllPatientList(allPatientList);
			
			
			StringBuffer str = new StringBuffer();
			

			
			str.append("<table class='table table-bordered' > ");
			str.append("<thead>");
			str.append("<tr class='bg-info'>");
			str.append("<th>Id</th> ");

			str.append("<th>Name</th> ");
			str.append("<th>Old Patient Id</th> ");
			str.append("<th>Mobile</th> ");
			str.append("<th>Email</th> ");
			str.append("<th>PostCode</th> ");
			str.append("<th>Dob</th> ");

			str.append("</tr>");
			str.append("</thead>");
			
			str.append("<tbody>");
			for(Client client1:allPatientList){
				String name = client1.getTitle()+" "+client1.getFirstName()+" "+client1.getLastName(); 		
			str.append("<tr>");
			str.append("<td>0000"+client1.getId()+"</td>");

			String firstName= client1.getFirstName();
			str.append("<td style='cursor: pointer;'; onclick = setPatient('"+firstName+"','"+client1.getId()+"','"+client1.getType()+"','"+client1.getTypeName()+"')>"+name+"</td>");
			if(client1.getOldclientId()==null){
				client1.setOldclientId("");
			}
			str.append("<td>"+client1.getOldclientId()+"</td>");
			str.append("<td>"+client1.getMobNo()+"</td>");
			str.append("<td>"+client1.getEmail()+"</td>");
			str.append("<td>"+client1.getPostCode()+"</td>");
			str.append("<td>"+client1.getDob()+"</td>");

			str.append("</tr>");
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
	
	
	public String searchTemplatePatient() throws SQLException{
		if(!verifyLogin(request)){
			return "login";
		}
		String searchClient = request.getParameter("searchText");
		Connection connection = null;
		ArrayList<Client> allPatientList = new ArrayList<Client>();
		try{
			connection = Connection_provider.getconnection();
			
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			allPatientList = clientDAO.getClient(searchClient,loginInfo.getId());
			
			clientForm.setAllPatientList(allPatientList);
			StringBuffer str = new StringBuffer();
				
			str.append("<table class = 'table table-bordered' > ");
			str.append("<thead>");
			str.append("<tr class = 'bg-info'>");
			str.append("<th>Id</th> ");

			str.append("<th>Name</th> ");
			str.append("<th>Old Id</th> ");
			str.append("<th>Mobile</th> ");
			str.append("<th>Email</th> ");
			str.append("<th>Post Code</th> ");
			str.append("<th>Dob</th> ");

			str.append("</tr>");
			str.append("</thead>");
			
			str.append("<tbody>");
			for(Client client1:allPatientList){
			str.append("<tr>");
			String firstName= client1.getFirstName();
			str.append("<td>0000"+client1.getId()+"</td>");

			String name = client1.getTitle()+" "+client1.getFirstName()+" "+client1.getMiddlename()+" "+client1.getLastName(); 	
			str.append("<td style='cursor: pointer;'; onclick = setPatient('"+firstName+"','"+client1.getId()+"','"+client1.getType()+"','"+client1.getTypeName()+"')>"+name+"</td>");
			if(client1.getOldclientId()==null){
				client1.setOldclientId("");
			}
			str.append("<td>"+client1.getOldclientId()+"</td>");
			str.append("<td>"+client1.getMobNo()+"</td>");
			str.append("<td>"+client1.getEmail()+"</td>");
			str.append("<td>"+client1.getPostCode()+"</td>");
			str.append("<td>"+client1.getDob()+"</td>");

			str.append("</tr>");
			}
			str.append("<tbody>");
			
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
	
public String getPatientDetails() throws Exception{
	if(!verifyLogin(request)){
		return "login";
	}
	String id = request.getParameter("id");
	session.setAttribute("id", id);
	Connection connection = null;
	Client client = new Client();
	try{
		
		connection = Connection_provider.getconnection();
		ClientDAO clientDAO = new JDBCClientDAO(connection);
		String fullName = clientDAO.getClientFullName(id);
		StringBuffer str = new StringBuffer();
		str.append(fullName);
		
		String tpCompanyEmail = clientDAO.getThirdPartyCompanyEmail(id);
		
		String data = ""+str.toString()+"/"+tpCompanyEmail+"";
		System.out.println(str.toString());
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+data+""); 		
		
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		
		connection.close();
	}
	
	return null;
}


public String changeStatusToActive() throws Exception{
	if(!verifyLogin(request)){
		return "login";
	}
	String id =request.getParameter("id");
	Connection connection = null;
	try{
		
		Client client = new Client();
		connection = Connection_provider.getconnection();
		ClientDAO clientDAO = new JDBCClientDAO(connection);
		int result = clientDAO.changeStatusToActive(id);
	
	}
	catch(Exception e){
		e.printStackTrace();
	}finally{
		
		connection.close();
	}
	return null;
	
}

public String changeStatusToInActive() throws Exception{
	if(!verifyLogin(request)){
		return "login";
	}
	String id =request.getParameter("id");
	//boolean status =Boolean.parseBoolean(request.getParameter("status"));
	Connection connection = null;
	try{
		
		Client client = new Client();
		connection = Connection_provider.getconnection();
		ClientDAO clientDAO = new JDBCClientDAO(connection);
		
			int result = clientDAO.changeStatusToInactive(id);
			
		
		
	}
	catch(Exception e){
		e.printStackTrace();
	}finally{
		
		connection.close();
	}
	return null;
	
}
public String viewProfile(){
	return "viewProfileOfClient";
}

public String displayProfile() throws Exception{
	if(!verifyLogin(request)){
		return "login";
	}
	
	
	
	Connection connection = null;
	try{
		
		connection = Connection_provider.getconnection();
		ClientDAO clientDAO = new JDBCClientDAO(connection);
		IpdDAO ipdDAO = new JDBCIpdDAO(connection);
		//set selected clientid from session
		if(session.getAttribute("sessionselectedclientid")!=null){
			if(request.getParameter("selectedid")==null || request.getParameter("selectedid").equals("")){
				String clientid = (String)session.getAttribute("sessionselectedclientid");
				clientForm.setClientId(clientid);
				Client client = clientDAO.getSelectedSessionClientDetails(clientid);
				clientForm.setClient(client.getClientName());
			}else{
				String clientId = request.getParameter("selectedid");
				clientForm.setClientId(clientId);
				Client client = clientDAO.getSelectedSessionClientDetails(clientId);
				clientForm.setClient(client.getClientName());
			}
			
		}
		
		
		
		String clientId = clientForm.getClientId();
		String clientName = clientForm.getClient();
		if(clientId==null || clientId == ""){
			clientId = request.getParameter("selectedid");
			clientForm.setClientId(clientId);
		}
		
		
		Client client = new Client();
		
		client = clientDAO.getClientDetails(clientForm.getClientId());
		
		clientForm.setTitle(client.getTitle());
		clientForm.setFirstName(client.getFirstName());
		clientForm.setLastName(client.getLastName());
		clientForm.setClientaddress(client.getAddress()+ " " +client.getSecondLineaddress());
		clientForm.setCountry(client.getCountry());
		clientForm.setDob(client.getDob());
		//Akash 09 nov to set age instead of dob
		
		String agegender="";
		//String dob = client.getDob();
		String age = DateTimeUtils.getAge1(client.getDob());
		/*if(Integer.parseInt(age)<2){
			if(Integer.parseInt(age)<1){
				String monthdays= DateTimeUtils.getMonthandDays(client.getDob());
				agegender=monthdays;
			}else{
				String monthdays= DateTimeUtils.getMonthandDays(client.getDob());
				agegender= age + " Years" + " "+monthdays;
			}
		} else {
			agegender = age + "Years";	
		}*/
		
		agegender = age +"/"+client.getGender();
		clientForm.setAge(agegender);
		
		clientForm.setEmail(client.getEmail());
		clientForm.setGender(client.getGender());
		clientForm.setMobNo(client.getMobNo());
		clientForm.setPostCode(client.getPostCode());
		clientForm.setReference(client.getReference());
		//String sourceOfIntro = clientDAO.getSourceOfIntro(client.getSourceOfIntro());
		clientForm.setSourceOfIntro(client.getSourceOfIntro());
		clientForm.setTown(client.getTown());
		clientForm.setId(client.getId());
		clientForm.setType(client.getType());
		clientForm.setTypeName(client.getTypeName());
		clientForm.setExpiryDate(client.getExpiryDate());
		clientForm.setNote(client.getNote());
		//String occupation = clientDAO.getOccupation(client.getOccupation());
		clientForm.setOccupation(client.getOccupation());		
		clientForm.setExpiryDate(client.getExpiryDate());
		if(client.getWhopay().equals("Client")){
			client.setWhopay("Self");
		}
		clientForm.setWhopay(client.getWhopay());
		clientForm.setPolicyAuthorzCode(client.getPolicyAuthorzCode());
		clientForm.setPolicyNo(client.getPolicyNo());
		clientForm.setKnownAs(client.getKnownAs());
		clientForm.setCounty(client.getCounty());
		clientForm.setHomeNo(client.getHomeNo());
		clientForm.setWorkNo(client.getWorkNo());
		clientForm.setEmailCc(client.getEmailCc());
		clientForm.setPrefContactMode(client.getPrefContactMode());
		clientForm.setEmergencyContName(client.getEmergencyContName());
		clientForm.setEmergencyContNo(client.getEmergencyContNo());
		clientForm.setPatientType(client.getPatientType());		
		//clientForm.setClientId(clientForm.get);
		clientForm.setClient(client.getTitle()+" " + client.getFirstName()+" " +client.getLastName());
		session.setAttribute("clientId",clientForm.getClientId());
		
		String abbb = client.getAbrivationid();
		clientForm.setAbrivationid(client.getAbrivationid());
		clientForm.setPatientIdAbrivation(client.getPatientIdAbrivation());
		
		String declarationNotes = clientDAO.getDeclaration(loginInfo.getId());
		
		session.setAttribute("declarationNotes", declarationNotes);
		String declarationTitle = clientDAO.getTitleOfDeclaration(loginInfo.getId());
		clientForm.setDeclarationTitle(declarationTitle);
		session.setAttribute("declarationTile",declarationTitle);
		//Akash 09 nov 2017 to add speciality of dr
		String practtionername = clientDAO.getIpdPractionerName(clientForm.getClientId());
		String speciality = clientDAO.getIpdPractionerSpeciality(clientForm.getClientId());
		String practid = clientDAO.getIpdPractionerId(clientForm.getClientId());
		if(practtionername!=null){
			 if(practtionername.equals("")){
				 practtionername=clientDAO.getPractitionerName(clientForm.getClientId());
				 speciality = clientDAO.getPractitionerSpeciality(clientForm.getClientId());
				 practid = clientDAO.getIpdPractionerId(clientForm.getClientId());
			 }
		} else {
			practtionername=clientDAO.getPractitionerName(clientForm.getClientId());
			speciality = clientDAO.getPractitionerSpeciality(clientForm.getClientId());
			practid = clientDAO.getIpdPractionerId(clientForm.getClientId());
		}
		clientForm.setPractid(practid);
		clientForm.setQualification(speciality);
		clientForm.setPractitionerName(practtionername);
		String clinicName = clientDAO.getClinicName(loginInfo.getClinicid());
		clientForm.setClinicName(clinicName);
		clientForm.setGpname(client.getGpname());
		clientForm.setEmployerName(client.getEmployerName());
		//String treatmentType = clientDAO.getTreatmentType(client.getTreatmentType());
		clientForm.setTreatmentType(client.getTreatmentType());
		clientForm.setReferedDate(client.getReferedDate());
		String insuranceCo = clientDAO.getTPCompanyName(client.getTypeName());
		clientForm.setThirdPartyCompanyName(insuranceCo);
		clientForm.setPolicyExcess(client.getPolicyExcess());
		
		ThirdParty gp = client.getGpDetails();
		clientForm.setGpAddress(gp.getAddress());
		clientForm.setGpTown(gp.getTown());
		clientForm.setGpCounty(gp.getCounty());
		clientForm.setGpPostCode(gp.getPostcode());
		clientForm.setGpname(gp.getGpname());
		clientForm.setSurgeryName(gp.getCompanyName());
		
		
		ThirdParty tp = client.getTpDetails();
		clientForm.setTpAddress(tp.getAddress());
		clientForm.setTpTown(tp.getTown());
		clientForm.setTpCounty(tp.getCounty());
		clientForm.setTpPostCode(tp.getPostcode());
		
		
		
		ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
		AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
		ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
		Clinic clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
		clientForm.setClinicName(clinic.getClinicName());
		clientForm.setClinicOwner(clinic.getClinicOwner());
		clientForm.setOwner_qualification(clinic.getOwner_qualification());
		clientForm.setLocationAdressList(locationAdressList);
		clientForm.setAddress(clinic.getAddress());
		clientForm.setLandLine(clinic.getLandLine());
		clientForm.setClinicemail(clinic.getEmail());
		clientForm.setWebsiteUrl(clinic.getWebsiteUrl());
		clientForm.setClinicLogo(clinic.getUserImageFileName());
		
		int ipdid = ipdDAO.getLastIpdId(clientForm.getClientId());
		clientForm.setIpdid(String.valueOf(ipdid));
		Client client2=new Client();
		client2=accountsDAO.getDisandadmiss(clientId);
		clientForm.setAddmissiondate(client2.getAdmissiondate());
		clientForm.setDischargedate(client2.getDischargedate());
		clientForm.setStatus("0");
		String drqualif=accountsDAO.getDrNamebyClientId(clientId);
		clientForm.setDrqualification(drqualif);
	}catch(Exception e){
		
		e.printStackTrace();
	}finally{
		
		connection.close();
	}
	
	return "viewProfileOfClient";
}

public String printProfile() throws Exception{
	if(!verifyLogin(request)){
		return "login";
	}
	String clientId = clientForm.getClientId();
	String clientName = clientForm.getClient();
	if(clientId==null || clientId == ""){
		clientId = request.getParameter("selectedid");
	}
	if(clientId==null || clientId == ""){
		clientId = (String) session.getAttribute("clientId");
	}
	
	Connection connection = null;
	try{
		
		Client client = new Client();
		connection = Connection_provider.getconnection();
		ClientDAO clientDAO = new JDBCClientDAO(connection);
		client = clientDAO.getClientDetails(clientId);
		
		clientForm.setTitle(client.getTitle());
		clientForm.setFirstName(client.getFirstName());
		clientForm.setLastName(client.getLastName());
		clientForm.setAddress(client.getAddress()+ " " +client.getSecondLineaddress());
		clientForm.setCountry(client.getCountry());
		clientForm.setDob(client.getDob());
		clientForm.setEmail(client.getEmail());
		clientForm.setGender(client.getGender());
		clientForm.setMobNo(client.getMobNo());
		clientForm.setPostCode(client.getPostCode());
		clientForm.setReference(client.getReference());
		clientForm.setSourceOfIntro(client.getSourceOfIntro());
		clientForm.setTown(client.getTown());
		clientForm.setId(client.getId());
		clientForm.setType(client.getType());
		clientForm.setTypeName(client.getTypeName());
		clientForm.setExpiryDate(client.getExpiryDate());
		clientForm.setOccupation(client.getOccupation());		
		clientForm.setExpiryDate(client.getExpiryDate());
		clientForm.setWhopay(client.getWhopay());
		clientForm.setPolicyAuthorzCode(client.getPolicyAuthorzCode());
		clientForm.setPolicyNo(client.getPolicyNo());
		clientForm.setKnownAs(client.getKnownAs());
		clientForm.setCounty(client.getCounty());
		clientForm.setHomeNo(client.getHomeNo());
		clientForm.setWorkNo(client.getWorkNo());
		clientForm.setEmailCc(client.getEmailCc());
		clientForm.setPrefContactMode(client.getPrefContactMode());
		clientForm.setEmergencyContName(client.getEmergencyContName());
		clientForm.setEmergencyContNo(client.getEmergencyContNo());
		clientForm.setPatientType(client.getPatientType());		
		clientForm.setClientId(clientId);
		clientForm.setClient(client.getTitle()+" " + client.getFirstName()+" " +client.getLastName());
		String declarationNotes = clientDAO.getDeclaration(loginInfo.getId());
		session.setAttribute("declarationNotes", declarationNotes);
		String declarationTitle = clientDAO.getTitleOfDeclaration(loginInfo.getId());
		clientForm.setDeclarationTitle(declarationTitle);
		session.setAttribute("declarationTile",declarationTitle);
		String practtionername = clientDAO.getPractitionerName(clientId);
		clientForm.setPractitionerName(practtionername);
		String clinicName = clientDAO.getClinicName(loginInfo.getClinicid());
		clientForm.setClinicName(clinicName);
		clientForm.setGpname(client.getGpname());
		clientForm.setPolicyExcess(client.getPolicyExcess());
		
		clientForm.setEmployerName(client.getEmployerName());
		//String treatmentType = clientDAO.getTreatmentType(client.getTreatmentType());
		clientForm.setTreatmentType(client.getTreatmentType());
		clientForm.setReferedDate(client.getReferedDate());
		String insuranceCo = clientDAO.getTPCompanyName(client.getTypeName());
		clientForm.setThirdPartyCompanyName(insuranceCo);
		clientForm.setPolicyExcess(client.getPolicyExcess());

	}catch(Exception e){
		
		e.printStackTrace();
	}finally{
		
		connection.close();
	}
	return "viewProfileOfClient";
}
	
public String checkMobileExist() throws IOException, SQLException{
	if(!verifyLogin(request)){
		return "login";
	}
	Connection connection = null;
	String mob = (String)request.getParameter("mob").trim();
	
	connection = Connection_provider.getconnection();
	ClientDAO clientDAO = new JDBCClientDAO(connection);
	// check if user with given user id already exist
	boolean mobile = clientDAO.isMobExist(mob);

	// if user id already exist then set response 'false'
	if(mobile){
		response.getWriter().write("false");
	}else{	// else set response 'true'
		response.getWriter().write("true");
	}
	return null;
}

public String checkEmailExist() throws IOException, SQLException{
	if(!verifyLogin(request)){
		return "login";
	}
	Connection connection = null;
	String email = (String)request.getParameter("email").trim();
	
	connection = Connection_provider.getconnection();
	ClientDAO clientDAO = new JDBCClientDAO(connection);
	// check if user with given user id already exist
	boolean emailId = clientDAO.isEmailIdExist(email);

	// if user id already exist then set response 'false'
	if(emailId){
		response.getWriter().write("false");
	}else{	// else set response 'true'
		response.getWriter().write("true");
	}
	return null;
}
public String saveNote() throws Exception{
	if(!verifyLogin(request)){
		return "login";
	}
	String note = request.getParameter("note");
	String id = request.getParameter("id");
	Connection connection = null;
	try{
		
		Client client = new Client();
		connection = Connection_provider.getconnection();
		ClientDAO clientDAO = new JDBCClientDAO(connection);
		int result = clientDAO.saveNote(id,note);
		
		StringBuffer textAjax = new StringBuffer();

		textAjax.append("<a title = 'Edit' onclick='editNote("+id+")' href=#''><i class='fa fa-edit'></i></a>&nbsp;&nbsp;<a title = 'Delete' onclick='deleteNote("+id+")' href='#'><i class='fa fa-trash-o'></i></a><br>");
		textAjax.append(note);
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+textAjax.toString()+""); 
	}
	catch(Exception e){
		e.printStackTrace();
	}finally{
		
		connection.close();
	}
	
	
	return null;
}

public String getNoteAndSet() throws Exception{

	if(!verifyLogin(request)){
		return "login";
	}
	String id = request.getParameter("id");
	Connection connection = null;
	try{
		
		Client client = new Client();
		connection = Connection_provider.getconnection();
		ClientDAO clientDAO = new JDBCClientDAO(connection);
		String note = clientDAO.getNote(id);
		
		StringBuffer textAjax = new StringBuffer();

		textAjax.append("<a title = 'Edit' onclick = 'updateNote("+id+")' href='#'><i class='fa fa-edit'></i>Update</a>&nbsp;&nbsp;<br>");
		textAjax.append("<input type = 'text' maxlength = '50' id = 'note"+id+"' name = 'note' class = 'form-control' value = '"+note+"'>");

		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+textAjax.toString()+""); 
	}
	catch(Exception e){
		e.printStackTrace();
	}finally{
		
		connection.close();
	}
	
	
	return null;
}

public String updateNote() throws Exception{
	if(!verifyLogin(request)){
		return "login";
	}
	String note = request.getParameter("note");
	String id = request.getParameter("id");
	Connection connection = null;
	try{
		
		Client client = new Client();
		connection = Connection_provider.getconnection();
		ClientDAO clientDAO = new JDBCClientDAO(connection);
		int result = clientDAO.updateNote(id,note);
		
		StringBuffer textAjax = new StringBuffer();

		textAjax.append("<a title = 'Edit' onclick='editNote("+id+")' href=#''><i class='fa fa-edit'></i></a>&nbsp;&nbsp;<a title = 'Delete' onclick='deleteNote("+id+")' href='#'><i class='fa fa-trash-o'></i></a><br>");
		textAjax.append(note);
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+textAjax.toString()+""); 
	}
	catch(Exception e){
		e.printStackTrace();
	}finally{
		
		connection.close();
	}
	
	
	return null;
}

public String deleteNote() throws Exception{
	if(!verifyLogin(request)){
		return "login";
	}
	String id = request.getParameter("id");
	Connection connection = null;
	try{
		
		connection = Connection_provider.getconnection();
		ClientDAO clientDAO = new JDBCClientDAO(connection);
		int result = clientDAO.deleteNote(id);
		
		StringBuffer textAjax = new StringBuffer();

		textAjax.append("<a title = 'Save' onclick = 'addNote("+id+")' href='#'><i class='fa fa-plus'></i>Save</a>&nbsp;&nbsp;<br>");
		textAjax.append("<input type = 'text' maxlength = '50' id = 'note"+id+"' name = 'note' class = 'form-control' placeholder='Enter Note'>");

		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+textAjax.toString()+""); 
	}
	catch(Exception e){
		e.printStackTrace();
	}finally{
		
		connection.close();
	}
	
	
	return null;
}
public String sortByPractitioner() throws SQLException{
	if(!verifyLogin(request)){
		return "login";
	}
	String practionerId = clientForm.getDiaryUser();
	System.out.println(practionerId);
	
	if(!verifyLogin(request)){
		return "login";
	}
	
	Connection connection = null;
	Client client = new Client();
	ArrayList<Client> allPatientList = new ArrayList<Client>();
	try{
		connection = Connection_provider.getconnection();
		
		ClientDAO clientDAO = new JDBCClientDAO(connection);
		int totalCount = clientDAO.getTotalClientUnderPCount(practionerId);
		pagination.setPreperties(totalCount);
		
		allPatientList = clientDAO.getAllPatientUnderP(pagination,practionerId);
		pagination.setPage_records(allPatientList.size());
		clientForm.setTotalRecords(totalCount);
		clientForm.setPagerecords(Integer.toString(pagination.getPage_records()));
		
		
		clientForm.setAllPatientList(allPatientList);
		//session.setAttribute("pagination", pagination);
	}
	catch (Exception e) {
		// TODO: handle exception
	}
	finally{
		connection.close();
	}
	return "manage";
}

public String setgp() throws SQLException{
	if(!verifyLogin(request)){
		return "login";
	}
	Connection connection = null;
	String id = request.getParameter("id");
	try{
		
		connection = Connection_provider.getconnection();
		ClientDAO clientDAO = new JDBCClientDAO(connection);

		ArrayList<Client> gpList = new ArrayList<Client>();
		gpList = clientDAO.getGpList();
		Client gpdetail = clientDAO.getGPDeatils(id);
		StringBuffer str = new StringBuffer();
		str.append("<label>GP Name</label><label><span class='text-danger'>*</span></label>");
		str.append("<select name='gpname' id='gpname' class='form-control showToolTip chosen' data-toggle='tooltip'  title = 'Select GP'>");
		
		
		if(gpdetail.getGpname() == null || gpdetail.getGpname().equals("")){
			str.append("<option value='"+gpdetail.getId()+"'>Request to Client</option>");

		}
		else{
			str.append("<option value='"+gpdetail.getId()+"'>"+gpdetail.getGpname()+"</option>");

		}		
		for(Client client : gpList){
			if(client.getGpname() == null || client.getGpname()==""){
				str.append("<option value='"+client.getId()+"'>Request to Client</option>");

			}
			else{
				str.append("<option value='"+client.getId()+"'>"+client.getGpname()+"</option>");

			}
		}
		
		str.append("</select>");
		
		
		
		
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



	public void prepare() throws Exception {
		Connection connection = null;
		
		ArrayList<Client> thirdPartyTypeList = new ArrayList<Client>();
		//ArrayList<Client> thirdPartyTypeNameList = new ArrayList<Client>();
		ArrayList<Client> clientOccupationList = new ArrayList<Client>();
		ArrayList<Client> refrenceList = new ArrayList<Client>();
		ArrayList<String> initialList = new ArrayList<String>();
		ArrayList<Client> sourceOfIntroList = new ArrayList<Client>();
		ArrayList<Client> gpList = new ArrayList<Client>();
		ArrayList<Client> condtitionList = new ArrayList<Client>();

		try{
			
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			gpList = clientDAO.getGpList();
			clientForm.setGpList(gpList);

			condtitionList = clientDAO.getEmrTreatmentTypeList();
			clientForm.setCondtitionList(condtitionList);
			
			ArrayList<DiaryManagement>userList = notAvailableSlotDAO.getUserList(loginInfo.getId());
			clientForm.setUserList(userList);

			thirdPartyTypeList = clientDAO.getThirdPartyType();
			clientForm.setThirdPartyTypeList(thirdPartyTypeList);
			
			//thirdPartyTypeNameList = clientDAO.getThirdPartyTypeName();
			//clientForm.setThirdPartyTypeNameList(thirdPartyTypeNameList);
			
			clientOccupationList = clientDAO.getOccupationList();
			
			
			
			clientForm.setClientOccupationList(clientOccupationList);
			
			refrenceList = clientDAO.getReferenceList();
			
			clientForm.setRefrenceList(refrenceList);
			
			initialList = clientDAO.getInitialList();
			clientForm.setInitialList(initialList);
			
			sourceOfIntroList = clientDAO.getSourceOfIntroList();
			clientForm.setSourceOfIntroList(sourceOfIntroList);
			
			
			//set surgery list
			ArrayList<Client>surgeryList = clientDAO.getSurgeryList();
			clientForm.setSurgeryList(surgeryList);
			
			ArrayList<ThirdParty> tpnameList = new ArrayList<ThirdParty>();
			ThirdPartyDAO  thirdPartyDAO = new JDBCThirdPartyDAO(connection);

			tpnameList = thirdPartyDAO.getTPNameList();
			clientForm.setTpnameList(tpnameList);
			clientForm.setApmtDurationList(PopulateList.apmtDurationList());
			TreatmentEpisodeDAO treatmentEpisodeDAO = new JDBCTreatmentEpisode(connection);

			ArrayList<TreatmentEpisode> sourceOfReferralList = treatmentEpisodeDAO.getSourceOfReferralList();
			clientForm.setSourceOfReferralList(sourceOfReferralList);
			
			//set declearation list
			ArrayList<Master>declerationTitleList = clientDAO.getDeclerationTitleList();
			clientForm.setDeclerationTitleList(declerationTitleList);
			String selecteddecid = clientDAO.getSelectedDecId();
			clientForm.setDectitle(selecteddecid);
			
			//set state and city list
			InventoryVendorDAO vendorDAO=new JDBCInventoryVendorDAO(connection);
			ArrayList<Master> stateList= vendorDAO.getAllStateList();
			ArrayList<Master> cityList= vendorDAO.getAllCityList();
			clientForm.setStatelist(stateList);
			clientForm.setCitylist(cityList);
			//investigation
			EmrDAO emrDAO= new JDBCEmrDAO(connection);
			InvestigationDAO investigationDAO= new JDBCInvestigationDAO(connection);
			MasterDAO masterDAO= new JDBCMasterDAO(connection);
			ArrayList<Master>invsTypeList = emrDAO.getInvesigationTypeList();
			clientForm.setInvsTypeList(invsTypeList);
			
			ArrayList<Master>invstReportTypeList = emrDAO.getInvstReportTypeList();
			clientForm.setInvstReportTypeList(invstReportTypeList);
			
			ArrayList<Master>invstUnitList = emrDAO.getInvstUnitList();
			clientForm.setInvstUnitList(invstUnitList);
			
			ArrayList<Master>invSectionList = investigationDAO.getInvestigationSectionList();
			clientForm.setInvSectionList(invSectionList);
			
			ArrayList<Master>disciplineList =  masterDAO.getDisciplineDataList();
			clientForm.setDisciplineList(disciplineList);
			
			//investigation pkg list
			ArrayList<Master>pkgsList = investigationDAO.getInvPaksLists();
			clientForm.setPkgsList(pkgsList);
			
			UserProfileDAO userProfileDAO= new JDBCUserProfileDAO(connection);
			ArrayList<String> jobTitleList = userProfileDAO.getJobTitleList();
			clientForm.setJobTitleList(jobTitleList);
			clientForm.setDocuList(masterDAO.getHISDocumentList());
			//user define date time
			clientForm.setHourList(PopulateList.hourList());
			clientForm.setMinuteList(PopulateList.getMinuteList());
		}catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			connection.close();
		}
		clientForm.setCountryList(PopulateList.countryList());
		clientForm.setCountry("United Kingdom");
		if(loginInfo.getCountry().equals("India")){
			clientForm.setCountry("India");
		}
		clientForm.setOccupationList(PopulateList.occupationList());
	}
	
	
	public String savegp() throws Exception{
		
		Connection connection = null;
		String id = request.getParameter("id");
		try{
			
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			
			
			String popgptype = "1";
			String gptypeNamepopup = request.getParameter("gptypeNamepopup");
			String gpnameid = request.getParameter("gpnameid");
			String workphno = request.getParameter("workphno");
			String gpemailid = request.getParameter("gpemailid");
			String gpfax = request.getParameter("gpfax");
			String gpNote = request.getParameter("gpNote");
			
			
			int result = clientDAO.saveGPData(popgptype,gptypeNamepopup,gpnameid,workphno,gpemailid,gpfax,gpNote);
			
			
			
			//set gpdata list
			ArrayList<GpData>gpList = clientDAO.getGPDataList(gptypeNamepopup);
			
			
			
			 StringBuffer dropDownAjax = new StringBuffer();
				dropDownAjax.append("<select id='gpname' name = 'gpname' class = 'form-control chosen'>");
					dropDownAjax.append("<option value = '0'>Select GP</option>");
					if(gpList.size()!=0){
						for(GpData data : gpList){
							dropDownAjax.append("<option value='"+data.getId()+"'>"+data.getGpName()+"</option>");
						}
						
					}
				dropDownAjax.append("</select>");
				response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				
				response.getWriter().write(""+dropDownAjax.toString()+""); 
			
			
			
			
			
			
		}catch(Exception e){
			e.printStackTrace();
			
		}finally{
			
			connection.close();
		}
		
		return null;
	}
	
	
	public String gpdtails() throws Exception{
		
		String id = request.getParameter("id");
		
		Connection connection = null;
		
		try{
			connection = Connection_provider.getconnection();
			ThirdPartyDAO  thirdPartyDAO = new JDBCThirdPartyDAO(connection);
			ThirdParty thirdParty = new ThirdParty();
			
			
			thirdParty = thirdPartyDAO.getTPDetails(id);
			
			String str = thirdParty.getTelephoneLine() + "*" + thirdParty.getCompnyEmail() + "*" + thirdParty.getFax() + "*" + thirdParty.getTpAccountSettingNotes();
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			
			response.getWriter().write(str); 
		

			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			connection.close();
		}
		
		System.out.println(id);
		
		return null;
	}
	
	
	public String gpdata() throws Exception{
		
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		String id = request.getParameter("id");
		try{
			
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			
			String surgeryid = request.getParameter("surgeryid");
			
			
			
			ArrayList<GpData>gpList = clientDAO.getGPDataList(surgeryid);
			
			
			 StringBuffer dropDownAjax = new StringBuffer();
				dropDownAjax.append("<select id='gpname' name = 'gpid' class = 'form-control chosen'>");
					dropDownAjax.append("<option value = '0'>Select GP</option>");
					if(gpList.size()!=0){
						for(GpData data : gpList){
							dropDownAjax.append("<option value='"+data.getId()+"'>"+data.getGpName()+"</option>");
						}
						
					}
				dropDownAjax.append("</select>");
				response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				
				response.getWriter().write(""+dropDownAjax.toString()+""); 
			
			
			
			
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			connection.close();
		}
		
		return null;
	}
	
	public String setWhoWillPay() throws Exception{
		
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		String result = "";
		String clientId = request.getParameter("clientId");
		String clientName = request.getParameter("client");
		try{
			
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			String whopay = clientDAO.getWhoPayName(clientId);
			
			Client client = clientDAO.getClientDetails(clientId);
			
			CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(connection);
			CompleteAppointment completeAppointment =  completeAptmDAO.getInsuranceCompanyName(clientId);
			String tpName = completeAppointment.getInsuranceCompanyName();
			if(whopay == null){
				result = clientName +"/Self";
			}
			else if(whopay.equals("Third Party")){
				result = tpName+"/"+whopay+"" + "/" + client.getPolicyNo();
				
			}/*else {
				result = clientName +"/"+whopay+"";

			}*/
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+result+""); 
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			connection.close();
		}
		
		return null;
	}
	
	
	public String ciims() throws Exception{
		Connection connection = null; 
		try{
			connection = Connection_provider.getconnection();
			
			
			
			//http://localhost:8080/APM19/ciimsClient?title=&firstname=&lastname=&email=&clinicid=darun&mob=&date=&diaryuserid=&gender=&dob=
			String title = request.getParameter("title");
			String firstname = request.getParameter("firstname");
			String lastname = request.getParameter("lastname");
			String email = request.getParameter("email");
			String mob = request.getParameter("mob");
			String clinicid = request.getParameter("clinicid");
			//String date = request.getParameter("date");
			//String diaryuserid = request.getParameter("diaryuserid");
			String dob = request.getParameter("dob");
			String gender = request.getParameter("gender");
			String opdno = request.getParameter("opdno");
			String address = "";
			
			
			connection = DriverManager.getConnection("jdbc:mysql://94.249.188.73:3306/"+clinicid+"","pranams","6qxi5x&)~XBZ");
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			int save = clientDAO.saveCiimsClients(title,firstname,lastname,email,mob,dob,gender,address,opdno);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			connection.close();
		}
		
		return "manage";
	}
	
	public String otp(){
		String email = request.getParameter("email");
		String mob = request.getParameter("mob");
		try{
			String otp = DateTimeUtils.getOTP();
			String to = email;
			String cc = "";
			String subject = "One Time Password";
			String notes = "One Time Password for appointment booking is "+otp+". Please use this password to book appointment.";
			
			EmbeddedImageEmailUtil.sendOtpMail(to, cc, subject, notes);
			
			if(loginInfo.getCountry().equals("India")){
				SmsService s = new SmsService();
				s.sendSms(notes, mob, loginInfo, new EmailLetterLog());
			}
			
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(otp); 
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public String exist() throws Exception{
		Connection connection = null;
			try{
				connection = Connection_provider.getconnection();
				ClientDAO clientDAO = new JDBCClientDAO(connection);
				String email = request.getParameter("email");
				boolean checkemalidexist = clientDAO.checkEmailidExist(email);
				if(checkemalidexist){
					int clientid = clientDAO.getPureSevaClientid(email);
					Client client = clientDAO.getClientDetails(Integer.toString(clientid));
					
					String data = client.getFirstName() + "*" + client.getLastName() + "*" + client.getMobNo() + "*" + client.getDob();
					
					response.setContentType("text/html");
					response.setHeader("Cache-Control", "no-cache");
					response.getWriter().write(""+data+""); 
				}
				
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				
				connection.close();
			}
		return null;
	}
	
	public String pureseva() throws Exception{
		
	//	var url = "puresevaClient?email="+email+"&fname="+fname+"&lname="+lname+"&mob="+mob+"&dob="+dob+" ";
		String email = request.getParameter("email");
		String fname = request.getParameter("fname");
		String lname = request.getParameter("lname");
		String mob = request.getParameter("mob");
		String dob = request.getParameter("dob");
		String puruhid = request.getParameter("puruhid");
		
		loginInfo.setEmail(email);
		loginInfo.setFirstName(fname);
		loginInfo.setLastName(lname);
		loginInfo.setMob(mob);
		loginInfo.setDob(dob);
		loginInfo.setUserId(email);
		
		Connection connection = null;
		try{
			
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			
			boolean checkemalidexist = clientDAO.checkEmailidExist(loginInfo.getEmail());
			
			int res = 0;
		
		/*	if(!checkemalidexist){
				res = clientDAO.savePureSevaClient(loginInfo);
			}else{
				res = clientDAO.getPureSevaClientid(loginInfo.getEmail());
				
			}*/
			res = clientDAO.getPureSevaClientid(loginInfo.getUhid());
			Client client = clientDAO.getClientDetails(Integer.toString(res));
			
			loginInfo.setIsotpconfirmd(1);
			
			String name =  loginInfo.getTitle() + "/"+ loginInfo.getFirstName() + "/"+loginInfo.getLastName();
			
			String data = res + "*" + name + "*" + client.getWhopay() + "*" + client.getType() + "*" + client.getTypeName();
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+data+""); 
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			connection.close();
		}
		
		return null;
	}
	
	
	public String getage()throws Exception {
		
		try {
			String dob1= request.getParameter("dob");
			
//			SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
//		    Calendar dob = Calendar.getInstance();
//		    Calendar today = Calendar.getInstance();
//		    Date dateOfBirth= dateFormat.parse(dobstr);
//		    dob.setTime(dateOfBirth);  
//
//		    int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
//
//		    if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
//		        age--; 
//		    }
//
//		    dob.set(Calendar.YEAR, today.get(Calendar.YEAR));
//		    int days=0;
//		    if(today.get(Calendar.DAY_OF_YEAR)> dob.get(Calendar.DAY_OF_YEAR) ){
//		        days=	today.get(Calendar.DAY_OF_YEAR) - dob.get(Calendar.DAY_OF_YEAR);
//		    } else {
//		    	days= dob.get(Calendar.DAY_OF_YEAR)- today.get(Calendar.DAY_OF_YEAR) ;
//		    }
//		   
//		    
//		    Integer ageInt = new Integer(age);
//		    String ageS = ageInt.toString();
//		    
//		    String data=ageS+"~"+days;
			int years = 0;
			   int months = 0;
			   int days = 0;
			Date birthDate=new SimpleDateFormat("dd/MM/yyyy").parse(dob1);
			String dt[]= dob1.split("/");
			String n=dt[1];
		   int tr=Integer.parseInt(n);
		   //create calendar object for birth day
		   Calendar birthDay = Calendar.getInstance();
		   birthDay.setTimeInMillis(birthDate.getTime());
		   //create calendar object for current day
		   long currentTime = System.currentTimeMillis();
		   Calendar now = Calendar.getInstance();
		   now.setTimeInMillis(currentTime);
		   //Get difference between years
		   years = now.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
		   int currMonth = now.get(Calendar.MONTH) + 1;
		   int birthMonth = birthDay.get(Calendar.MONTH) + 1;
		   //Get difference between months
		   months = currMonth - birthMonth;
		   //if month difference is in negative then reduce years by one and calculate the number of months.
		   if (months < 0)
		   {
		      years--;
		      months = 12 - birthMonth + currMonth;
		      if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
		         months--;
		   } else if (months == 0 && now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
		   {
		      years--;
		      months = 11;
		   }
		   else if(months>0&& now.get(Calendar.DATE) < birthDay.get(Calendar.DATE)){
			   months--;
		   }
		   //Calculate the days
		   if (now.get(Calendar.DATE) > birthDay.get(Calendar.DATE))
		      days = now.get(Calendar.DATE) - birthDay.get(Calendar.DATE);
		   else if (now.get(Calendar.DATE) < birthDay.get(Calendar.DATE))
		   {
		      int today = now.get(Calendar.DAY_OF_MONTH);
		      now.add(Calendar.MONTH, -1);
		      days = now.getActualMaximum(Calendar.DAY_OF_MONTH) - birthDay.get(Calendar.DAY_OF_MONTH) + today;
		   } else
		   {
		      days = 0;
		      if (months == 12)
		      {
		         years++;
		         months = 0;
		      }
		   }
		   
		   

		  
			   
			   String age= String.valueOf(years)+" yr "+String.valueOf(months)+" months "+String.valueOf(days)+" days" ; 
			   String data=String.valueOf(years)+"~"+String.valueOf(months)+"~"+String.valueOf(days);
			   response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write(""+data+"");  
			

		   
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	public String getRecentlyAddedPatientDetails() throws Exception{
		
		String clientid = (String)session.getAttribute("clientid");
		String title = (String)session.getAttribute("title");
		String firstname = (String)session.getAttribute("firstname");
		String lastname = (String)session.getAttribute("lastname");
		String treatmenttype = (String)session.getAttribute("treatmenttype");
		String newWhopay = (String)session.getAttribute("newWhopay");
		String newPolicyNo = (String)session.getAttribute("newPolicyNo");
		String newTpname = (String)session.getAttribute("newTpname");
		
		
		String result = "";
		try{
			String name = title + " "+ firstname + " "+lastname;
			result= clientid+"#"+name+"#"+treatmenttype+"#"+newWhopay+"#"+newPolicyNo+"#"+newTpname;
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+result+"");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public String getInfo() throws Exception{
		Connection connection = null;
		try{
			
		
			session.removeAttribute("invstList");
			
			int id = Integer.parseInt(request.getParameter("clientId"));
			Client client = new Client();
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			
			//get paractioner data
			String diaryuser = request.getParameter("diaryuser");
			UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
			UserProfile userProfile = userProfileDAO.getUserprofileDetails(Integer.parseInt(diaryuser));
			String diaryusername = userProfile.getInitial() + " " + userProfile.getFirstname() + " " + userProfile.getLastname() ;
			
			client = clientDAO.getPatient(id);
			
			String age=DateTimeUtils.getAge(client.getDob());
			//Akash 05 dec 2017 age in months and year
			String agegender="";
			String age2 = DateTimeUtils.getAge(client.getDob());
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
			
			String typenameid = client.getTypeName();
			String tp = clientDAO.getTPCompanyName(typenameid);
			String name = client.getTitle()+" "+client.getFirstName()+" "+client.getMiddlename()+" "+client.getLastName();
			
			//Akash 06 Aug 2018 set pricription heading weight
			String weight = clientDAO.getClientWeight(""+id);
			
			String clientInfo = name +"*"+client.getAddress()+"*"+client.getSecondLineaddress()+"*"+client.getTown()+"*"+client.getPostCode()+"*"+agegender+"*"+tp+"*"+client.getMobNo()+"*"+client.getEmail() + "*" + client.getGender() + "*" +diaryusername+"*"+weight;
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(clientInfo);
		}
		catch(Exception e){
			e.printStackTrace();
		}finally{
			
			connection.close();
		}
		return null;
	}
	
	
	public String setpatientname() throws Exception{
		
		Connection connection=null;
		try {
			String id=request.getParameter("id");
			connection=Connection_provider.getconnection();
			ClientDAO clientDAO= new JDBCClientDAO(connection);
			String fullname= clientDAO.getClientFullName(id);
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(fullname);
		} catch (Exception e) {

			e.printStackTrace();
		}
		finally {
			connection.close();
		}
		
		return null;
	}
	
	
	public ClientForm getModel() {
		
		return clientForm;
	}
	
	public String savedeclaration() throws Exception{
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			String ipdid = clientForm.getIpdid();
			String practid = clientForm.getPractid();
			String clientId = clientForm.getClientId();
			String declarationNotes = clientForm.getDeclarationNotes();
			String declarationTitle = clientForm.getDeclarationTitle();
			Client client = new Client();
			client.setIpdid(ipdid);
			client.setClientId(clientId);
			client.setDeclarationNotes(declarationNotes);
			client.setDeclarationTitle(declarationTitle);
			client.setPractid(practid);
			String date= DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
			client.setLastModifiedDate(date);
			client.setUserid(loginInfo.getUserId());
			int result = clientDAO.saveDeclaration(client);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			connection.close();
		}
		return printDeclaration();
	}


	public String printDeclaration() throws Exception {
		if(!verifyLogin(request)){
			return "login";
		}
		
		
		
		Connection connection = null;
		try{
			
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			IpdDAO ipdDAO = new JDBCIpdDAO(connection);
			//set selected clientid from session
			if(session.getAttribute("sessionselectedclientid")!=null){
				if(request.getParameter("selectedid")==null || request.getParameter("selectedid").equals("")){
					String clientid = (String)session.getAttribute("sessionselectedclientid");
					clientForm.setClientId(clientid);
					Client client = clientDAO.getSelectedSessionClientDetails(clientid);
					clientForm.setClient(client.getClientName());
				}else{
					String clientId = request.getParameter("selectedid");
					clientForm.setClientId(clientId);
					Client client = clientDAO.getSelectedSessionClientDetails(clientId);
					clientForm.setClient(client.getClientName());
				}
				
			}
			
			
			
			String clientId = clientForm.getClientId();
			String clientName = clientForm.getClient();
			if(clientId==null || clientId == ""){
				clientId = request.getParameter("selectedid");
				clientForm.setClientId(clientId);
			}
			
			
			Client client = new Client();
			
			client = clientDAO.getClientDetails(clientForm.getClientId());
			
			clientForm.setTitle(client.getTitle());
			clientForm.setFirstName(client.getFirstName());
			clientForm.setLastName(client.getLastName());
			clientForm.setClientaddress(client.getAddress()+ " " +client.getSecondLineaddress());
			clientForm.setCountry(client.getCountry());
			clientForm.setDob(client.getDob());
			//Akash 09 nov to set age instead of dob
			
			String agegender=DateTimeUtils.getAge1(client.getDob());;
			//String dob = client.getDob();
//			String age = DateTimeUtils.getAge(client.getDob());
//			if(Integer.parseInt(age)<2){
//				if(Integer.parseInt(age)<1){
//					String monthdays= DateTimeUtils.getMonthandDays(client.getDob());
//					agegender=monthdays;
//				}else{
//					String monthdays= DateTimeUtils.getMonthandDays(client.getDob());
//					agegender= age + " Years" + " "+monthdays;
//				}
//			} else {
//				agegender = age + "Years";	
//			}
			
			clientForm.setAge(agegender);
			
			clientForm.setEmail(client.getEmail());
			clientForm.setGender(client.getGender());
			clientForm.setMobNo(client.getMobNo());
			clientForm.setPostCode(client.getPostCode());
			clientForm.setReference(client.getReference());
			//String sourceOfIntro = clientDAO.getSourceOfIntro(client.getSourceOfIntro());
			clientForm.setSourceOfIntro(client.getSourceOfIntro());
			clientForm.setTown(client.getTown());
			clientForm.setId(client.getId());
			clientForm.setType(client.getType());
			clientForm.setTypeName(client.getTypeName());
			clientForm.setExpiryDate(client.getExpiryDate());
			clientForm.setNote(client.getNote());
			//String occupation = clientDAO.getOccupation(client.getOccupation());
			clientForm.setOccupation(client.getOccupation());		
			clientForm.setExpiryDate(client.getExpiryDate());
			if(client.getWhopay().equals("Client")){
				client.setWhopay("Self");
			}
			clientForm.setWhopay(client.getWhopay());
			clientForm.setPolicyAuthorzCode(client.getPolicyAuthorzCode());
			clientForm.setPolicyNo(client.getPolicyNo());
			clientForm.setKnownAs(client.getKnownAs());
			clientForm.setCounty(client.getCounty());
			clientForm.setHomeNo(client.getHomeNo());
			clientForm.setWorkNo(client.getWorkNo());
			clientForm.setEmailCc(client.getEmailCc());
			clientForm.setPrefContactMode(client.getPrefContactMode());
			clientForm.setEmergencyContName(client.getEmergencyContName());
			clientForm.setEmergencyContNo(client.getEmergencyContNo());
			clientForm.setPatientType(client.getPatientType());		
			//clientForm.setClientId(clientForm.get);
			clientForm.setClient(client.getTitle()+" " + client.getFirstName()+" " +client.getLastName());
			session.setAttribute("clientId",clientForm.getClientId());
			
			String abbb = client.getAbrivationid();
			clientForm.setAbrivationid(client.getAbrivationid());
			clientForm.setPatientIdAbrivation(client.getPatientIdAbrivation());
			
			
			//Akash 09 nov 2017 to add speciality of dr
			String practtionername = clientDAO.getIpdPractionerName(clientForm.getClientId());
			String speciality = clientDAO.getIpdPractionerSpeciality(clientForm.getClientId());
			String practid = clientDAO.getIpdPractionerId(clientForm.getClientId());
			if(practtionername!=null){
				 if(practtionername.equals("")){
					 practtionername=clientDAO.getPractitionerName(clientForm.getClientId());
					 speciality = clientDAO.getPractitionerSpeciality(clientForm.getClientId());
					 practid = clientDAO.getIpdPractionerId(clientForm.getClientId());
				 }
			} else {
				practtionername=clientDAO.getPractitionerName(clientForm.getClientId());
				speciality = clientDAO.getPractitionerSpeciality(clientForm.getClientId());
				practid = clientDAO.getIpdPractionerId(clientForm.getClientId());
			}
			clientForm.setPractid(practid);
			clientForm.setQualification(speciality);
			clientForm.setPractitionerName(practtionername);
			String clinicName = clientDAO.getClinicName(loginInfo.getClinicid());
			clientForm.setClinicName(clinicName);
			clientForm.setGpname(client.getGpname());
			clientForm.setEmployerName(client.getEmployerName());
			//String treatmentType = clientDAO.getTreatmentType(client.getTreatmentType());
			clientForm.setTreatmentType(client.getTreatmentType());
			clientForm.setReferedDate(client.getReferedDate());
			String insuranceCo = clientDAO.getTPCompanyName(client.getTypeName());
			clientForm.setThirdPartyCompanyName(insuranceCo);
			clientForm.setPolicyExcess(client.getPolicyExcess());
			
			ThirdParty gp = client.getGpDetails();
			clientForm.setGpAddress(gp.getAddress());
			clientForm.setGpTown(gp.getTown());
			clientForm.setGpCounty(gp.getCounty());
			clientForm.setGpPostCode(gp.getPostcode());
			clientForm.setGpname(gp.getGpname());
			clientForm.setSurgeryName(gp.getCompanyName());
			
			
			ThirdParty tp = client.getTpDetails();
			clientForm.setTpAddress(tp.getAddress());
			clientForm.setTpTown(tp.getTown());
			clientForm.setTpCounty(tp.getCounty());
			clientForm.setTpPostCode(tp.getPostcode());
			
			
			
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
			Clinic clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
			clientForm.setClinicName(clinic.getClinicName());
			clientForm.setClinicOwner(clinic.getClinicOwner());
			clientForm.setOwner_qualification(clinic.getOwner_qualification());
			clientForm.setLocationAdressList(locationAdressList);
			clientForm.setAddress(clinic.getAddress());
			clientForm.setLandLine(clinic.getLandLine());
			clientForm.setClinicemail(clinic.getEmail());
			clientForm.setWebsiteUrl(clinic.getWebsiteUrl());
			clientForm.setClinicLogo(clinic.getUserImageFileName());
			
			int ipdid = ipdDAO.getLastIpdId(clientForm.getClientId());
			clientForm.setIpdid(String.valueOf(ipdid));
			
			Client client2 = clientDAO.getDeclarationData(ipdid);
			
			/*String declarationNotes = clientDAO.getDeclaration(loginInfo.getId());
			session.setAttribute("declarationNotes", declarationNotes);
			String declarationTitle = clientDAO.getTitleOfDeclaration(loginInfo.getId());
			clientForm.setDeclarationTitle(declarationTitle);
			session.setAttribute("declarationTile",declarationTitle);*/
			String declarationNotes = client2.getDeclarationNotes();
			session.setAttribute("declarationNotes", declarationNotes);
			String declarationTitle = client2.getDeclarationTitle();
			clientForm.setDeclarationTitle(declarationTitle);
			session.setAttribute("declarationTile",declarationTitle);
			
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			connection.close();
		}
		
		return "printdeclarationform";
	}
	public String editdeclarationformdetails() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		
		
		
		Connection connection = null;
		try{
			
			String clientId = request.getParameter("clientId");
			String id = request.getParameter("id");
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			IpdDAO ipdDAO = new JDBCIpdDAO(connection);
			//set selected clientid from session
			/*if(session.getAttribute("sessionselectedclientid")!=null){
				if(request.getParameter("selectedid")==null || request.getParameter("selectedid").equals("")){
					String clientid = (String)session.getAttribute("sessionselectedclientid");
					clientForm.setClientId(clientid);
					Client client = clientDAO.getSelectedSessionClientDetails(clientid);
					clientForm.setClient(client.getClientName());
				}else{
					String clientId = request.getParameter("selectedid");
					clientForm.setClientId(clientId);
					Client client = clientDAO.getSelectedSessionClientDetails(clientId);
					clientForm.setClient(client.getClientName());
				}
				
			}*/
			
			
			Client client2 = clientDAO.getSelectedSessionClientDetails(clientId);
			clientForm.setClient(client2.getClientName());
			//String clientId = clientId;
			String clientName = clientForm.getClient();
			if(clientId==null || clientId == ""){
				clientId = request.getParameter("selectedid");
				clientForm.setClientId(clientId);
			}
			
			
			Client client = new Client();
			
			client = clientDAO.getClientDetails(clientId);
			
			clientForm.setTitle(client.getTitle());
			clientForm.setFirstName(client.getFirstName());
			clientForm.setLastName(client.getLastName());
			clientForm.setClientaddress(client.getAddress()+ " " +client.getSecondLineaddress());
			clientForm.setCountry(client.getCountry());
			clientForm.setDob(client.getDob());
			//Akash 09 nov to set age instead of dob
			
			String agegender="";
			//String dob = client.getDob();
			String age = DateTimeUtils.getAge(client.getDob());
			if(Integer.parseInt(age)<2){
				if(Integer.parseInt(age)<1){
					String monthdays= DateTimeUtils.getMonthandDays(client.getDob());
					agegender=monthdays;
				}else{
					String monthdays= DateTimeUtils.getMonthandDays(client.getDob());
					agegender= age + " Years" + " "+monthdays;
				}
			} else {
				agegender = age + "Years";	
			}
			
			clientForm.setAge(agegender);
			
			clientForm.setEmail(client.getEmail());
			clientForm.setGender(client.getGender());
			clientForm.setMobNo(client.getMobNo());
			clientForm.setPostCode(client.getPostCode());
			clientForm.setReference(client.getReference());
			//String sourceOfIntro = clientDAO.getSourceOfIntro(client.getSourceOfIntro());
			clientForm.setSourceOfIntro(client.getSourceOfIntro());
			clientForm.setTown(client.getTown());
			clientForm.setId(client.getId());
			clientForm.setType(client.getType());
			clientForm.setTypeName(client.getTypeName());
			clientForm.setExpiryDate(client.getExpiryDate());
			clientForm.setNote(client.getNote());
			//String occupation = clientDAO.getOccupation(client.getOccupation());
			clientForm.setOccupation(client.getOccupation());		
			clientForm.setExpiryDate(client.getExpiryDate());
			if(client.getWhopay().equals("Client")){
				client.setWhopay("Self");
			}
			clientForm.setWhopay(client.getWhopay());
			clientForm.setPolicyAuthorzCode(client.getPolicyAuthorzCode());
			clientForm.setPolicyNo(client.getPolicyNo());
			clientForm.setKnownAs(client.getKnownAs());
			clientForm.setCounty(client.getCounty());
			clientForm.setHomeNo(client.getHomeNo());
			clientForm.setWorkNo(client.getWorkNo());
			clientForm.setEmailCc(client.getEmailCc());
			clientForm.setPrefContactMode(client.getPrefContactMode());
			clientForm.setEmergencyContName(client.getEmergencyContName());
			clientForm.setEmergencyContNo(client.getEmergencyContNo());
			clientForm.setPatientType(client.getPatientType());		
			//clientForm.setClientId(clientForm.get);
			clientForm.setClient(client.getTitle()+" " + client.getFirstName()+" " +client.getLastName());
			session.setAttribute("clientId",clientId);
			
			String abbb = client.getAbrivationid();
			clientForm.setAbrivationid(client.getAbrivationid());
			clientForm.setPatientIdAbrivation(client.getPatientIdAbrivation());
			
			
			//Akash 09 nov 2017 to add speciality of dr
			String practtionername = clientDAO.getIpdPractionerName(clientId);
			String speciality = clientDAO.getIpdPractionerSpeciality(clientId);
			String practid = clientDAO.getIpdPractionerId(clientId);
			if(practtionername!=null){
				 if(practtionername.equals("")){
					 practtionername=clientDAO.getPractitionerName(clientId);
					 speciality = clientDAO.getPractitionerSpeciality(clientId);
					 practid = clientDAO.getIpdPractionerId(clientId);
				 }
			} else {
				practtionername=clientDAO.getPractitionerName(clientId);
				speciality = clientDAO.getPractitionerSpeciality(clientId);
				practid = clientDAO.getIpdPractionerId(clientId);
			}
			clientForm.setPractid(practid);
			clientForm.setQualification(speciality);
			clientForm.setPractitionerName(practtionername);
			String clinicName = clientDAO.getClinicName(loginInfo.getClinicid());
			clientForm.setClinicName(clinicName);
			clientForm.setGpname(client.getGpname());
			clientForm.setEmployerName(client.getEmployerName());
			//String treatmentType = clientDAO.getTreatmentType(client.getTreatmentType());
			clientForm.setTreatmentType(client.getTreatmentType());
			clientForm.setReferedDate(client.getReferedDate());
			String insuranceCo = clientDAO.getTPCompanyName(client.getTypeName());
			clientForm.setThirdPartyCompanyName(insuranceCo);
			clientForm.setPolicyExcess(client.getPolicyExcess());
			
			ThirdParty gp = client.getGpDetails();
			clientForm.setGpAddress(gp.getAddress());
			clientForm.setGpTown(gp.getTown());
			clientForm.setGpCounty(gp.getCounty());
			clientForm.setGpPostCode(gp.getPostcode());
			clientForm.setGpname(gp.getGpname());
			clientForm.setSurgeryName(gp.getCompanyName());
			
			
			ThirdParty tp = client.getTpDetails();
			clientForm.setTpAddress(tp.getAddress());
			clientForm.setTpTown(tp.getTown());
			clientForm.setTpCounty(tp.getCounty());
			clientForm.setTpPostCode(tp.getPostcode());
			
			
			
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
			Clinic clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
			clientForm.setClinicName(clinic.getClinicName());
			clientForm.setClinicOwner(clinic.getClinicOwner());
			clientForm.setOwner_qualification(clinic.getOwner_qualification());
			clientForm.setLocationAdressList(locationAdressList);
			clientForm.setAddress(clinic.getAddress());
			clientForm.setLandLine(clinic.getLandLine());
			clientForm.setClinicemail(clinic.getEmail());
			clientForm.setWebsiteUrl(clinic.getWebsiteUrl());
			clientForm.setClinicLogo(clinic.getUserImageFileName());
		
			int ipdid = ipdDAO.getLastIpdId(clientId);
			clientForm.setIpdid(String.valueOf(ipdid));
			
			Client client3 = clientDAO.getDeclarationByID(id);
			session.setAttribute("declarationNotes", client3.getDeclarationNotes());
			//String declarationTitle = clientDAO.getTitleOfDeclaration(loginInfo.getId());
			clientForm.setDeclarationTitle(client3.getDeclarationTitle());
			session.setAttribute("declarationTile",client3.getDeclarationTitle());
			clientForm.setDeclarationid(id);
			clientForm.setStatus("1");

			clientForm.setClinicLogo(clinic.getUserImageFileName());
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			connection.close();
		}
		
		return "viewProfileOfClient";
	}
	public String printdeclarationformdetails() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		
		
		
		Connection connection = null;
		try{
			String clientId = request.getParameter("clientId");
			String id = request.getParameter("id");
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			IpdDAO ipdDAO = new JDBCIpdDAO(connection);
			//set selected clientid from session
			/*if(session.getAttribute("sessionselectedclientid")!=null){
				if(request.getParameter("selectedid")==null || request.getParameter("selectedid").equals("")){
					String clientid = (String)session.getAttribute("sessionselectedclientid");
					clientForm.setClientId(clientid);
					Client client = clientDAO.getSelectedSessionClientDetails(clientid);
					clientForm.setClient(client.getClientName());
				}else{
					String clientId = request.getParameter("selectedid");
					clientForm.setClientId(clientId);
					Client client = clientDAO.getSelectedSessionClientDetails(clientId);
					clientForm.setClient(client.getClientName());
				}
				
			}*/
			
			Client client2 = clientDAO.getSelectedSessionClientDetails(clientId);
			clientForm.setClient(client2.getClientName());
			
			//String clientId = clientId;
			clientForm.setClientId(clientId);
			String clientName = clientForm.getClient();
			if(clientId==null || clientId == ""){
				clientId = request.getParameter("selectedid");
				clientForm.setClientId(clientId);
			}
			
			
			Client client = new Client();
			
			client = clientDAO.getClientDetails(clientId);
			
			clientForm.setTitle(client.getTitle());
			clientForm.setFirstName(client.getFirstName());
			clientForm.setLastName(client.getLastName());
			clientForm.setClientaddress(client.getAddress()+ " " +client.getSecondLineaddress());
			clientForm.setCountry(client.getCountry());
			clientForm.setDob(client.getDob());
			//Akash 09 nov to set age instead of dob
			
			String agegender=DateTimeUtils.getAge1(client.getDob());;
			//String dob = client.getDob();
//			String age = DateTimeUtils.getAge(client.getDob());
//			if(Integer.parseInt(age)<2){
//				if(Integer.parseInt(age)<1){
//					String monthdays= DateTimeUtils.getMonthandDays(client.getDob());
//					agegender=monthdays;
//				}else{
//					String monthdays= DateTimeUtils.getMonthandDays(client.getDob());
//					agegender= age + " Years" + " "+monthdays;
//				}
//			} else {
//				agegender = age + "Years";	
//			}
			
			clientForm.setAge(agegender);
			
			clientForm.setEmail(client.getEmail());
			clientForm.setGender(client.getGender());
			clientForm.setMobNo(client.getMobNo());
			clientForm.setPostCode(client.getPostCode());
			clientForm.setReference(client.getReference());
			//String sourceOfIntro = clientDAO.getSourceOfIntro(client.getSourceOfIntro());
			clientForm.setSourceOfIntro(client.getSourceOfIntro());
			clientForm.setTown(client.getTown());
			clientForm.setId(client.getId());
			clientForm.setType(client.getType());
			clientForm.setTypeName(client.getTypeName());
			clientForm.setExpiryDate(client.getExpiryDate());
			clientForm.setNote(client.getNote());
			//String occupation = clientDAO.getOccupation(client.getOccupation());
			clientForm.setOccupation(client.getOccupation());		
			clientForm.setExpiryDate(client.getExpiryDate());
			if(client.getWhopay().equals("Client")){
				client.setWhopay("Self");
			}
			clientForm.setWhopay(client.getWhopay());
			clientForm.setPolicyAuthorzCode(client.getPolicyAuthorzCode());
			clientForm.setPolicyNo(client.getPolicyNo());
			clientForm.setKnownAs(client.getKnownAs());
			clientForm.setCounty(client.getCounty());
			clientForm.setHomeNo(client.getHomeNo());
			clientForm.setWorkNo(client.getWorkNo());
			clientForm.setEmailCc(client.getEmailCc());
			clientForm.setPrefContactMode(client.getPrefContactMode());
			clientForm.setEmergencyContName(client.getEmergencyContName());
			clientForm.setEmergencyContNo(client.getEmergencyContNo());
			clientForm.setPatientType(client.getPatientType());		
			//clientForm.setClientId(clientForm.get);
			clientForm.setClient(client.getTitle()+" " + client.getFirstName()+" " +client.getLastName());
			session.setAttribute("clientId",clientId);
			
			String abbb = client.getAbrivationid();
			clientForm.setAbrivationid(client.getAbrivationid());
			clientForm.setPatientIdAbrivation(client.getPatientIdAbrivation());
			
			
			//Akash 09 nov 2017 to add speciality of dr
			String practtionername = clientDAO.getIpdPractionerName(clientId);
			String speciality = clientDAO.getIpdPractionerSpeciality(clientId);
			String practid = clientDAO.getIpdPractionerId(clientId);
			if(practtionername!=null){
				 if(practtionername.equals("")){
					 practtionername=clientDAO.getPractitionerName(clientId);
					 speciality = clientDAO.getPractitionerSpeciality(clientId);
					 practid = clientDAO.getIpdPractionerId(clientId);
				 }
			} else {
				practtionername=clientDAO.getPractitionerName(clientId);
				speciality = clientDAO.getPractitionerSpeciality(clientId);
				practid = clientDAO.getIpdPractionerId(clientId);
			}
			clientForm.setPractid(practid);
			clientForm.setQualification(speciality);
			clientForm.setPractitionerName(practtionername);
			String clinicName = clientDAO.getClinicName(loginInfo.getClinicid());
			clientForm.setClinicName(clinicName);
			clientForm.setGpname(client.getGpname());
			clientForm.setEmployerName(client.getEmployerName());
			//String treatmentType = clientDAO.getTreatmentType(client.getTreatmentType());
			clientForm.setTreatmentType(client.getTreatmentType());
			clientForm.setReferedDate(client.getReferedDate());
			String insuranceCo = clientDAO.getTPCompanyName(client.getTypeName());
			clientForm.setThirdPartyCompanyName(insuranceCo);
			clientForm.setPolicyExcess(client.getPolicyExcess());
			
			ThirdParty gp = client.getGpDetails();
			clientForm.setGpAddress(gp.getAddress());
			clientForm.setGpTown(gp.getTown());
			clientForm.setGpCounty(gp.getCounty());
			clientForm.setGpPostCode(gp.getPostcode());
			clientForm.setGpname(gp.getGpname());
			clientForm.setSurgeryName(gp.getCompanyName());
			
			
			ThirdParty tp = client.getTpDetails();
			clientForm.setTpAddress(tp.getAddress());
			clientForm.setTpTown(tp.getTown());
			clientForm.setTpCounty(tp.getCounty());
			clientForm.setTpPostCode(tp.getPostcode());
			
			
			
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
			Clinic clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
			clientForm.setClinicName(clinic.getClinicName());
			clientForm.setClinicOwner(clinic.getClinicOwner());
			clientForm.setOwner_qualification(clinic.getOwner_qualification());
			clientForm.setLocationAdressList(locationAdressList);
			clientForm.setAddress(clinic.getAddress());
			clientForm.setLandLine(clinic.getLandLine());
			clientForm.setClinicemail(clinic.getEmail());
			clientForm.setWebsiteUrl(clinic.getWebsiteUrl());
			
			int ipdid = ipdDAO.getLastIpdId(clientId);
			clientForm.setIpdid(String.valueOf(ipdid));
			
			Client client3 = clientDAO.getDeclarationByID(id);
			session.setAttribute("declarationNotes", client3.getDeclarationNotes());
			//String declarationTitle = clientDAO.getTitleOfDeclaration(loginInfo.getId());
			clientForm.setDeclarationTitle(client3.getDeclarationTitle());
			session.setAttribute("declarationTile",client3.getDeclarationTitle());
			
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			
			connection.close();
		}
		
		return "printdeclarationform";
	}
	public String updatedeclaration() throws Exception{
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			String ipdid = clientForm.getIpdid();
			String practid1 = clientForm.getPractid();
			String clientId = clientForm.getClientId();
			String declarationNotes = clientForm.getDeclarationNotes();
			String declarationTitle = clientForm.getDeclarationTitle();
			String declarationid = clientForm.getDeclarationid();
			Client client1 = new Client();
			client1.setIpdid(ipdid);
			client1.setClientId(clientId);
			client1.setDeclarationNotes(declarationNotes);
			client1.setDeclarationTitle(declarationTitle);
			client1.setPractid(practid1);
			String date= DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
			client1.setLastModifiedDate(date);
			client1.setUserid(loginInfo.getUserId());
			client1.setId(Integer.parseInt(declarationid));
			int result = clientDAO.updateDeclarationData(client1);
			
			
			//lets try
			
			String id = declarationid;
			IpdDAO ipdDAO = new JDBCIpdDAO(connection);
			
			Client client2 = clientDAO.getSelectedSessionClientDetails(clientId);
			clientForm.setClient(client2.getClientName());
			
			clientForm.setClientId(clientId);
			String clientName = clientForm.getClient();
			if(clientId==null || clientId == ""){
				clientId = request.getParameter("selectedid");
				clientForm.setClientId(clientId);
			}
			
			
			Client client = new Client();
			
			client = clientDAO.getClientDetails(clientId);
			
			clientForm.setTitle(client.getTitle());
			clientForm.setFirstName(client.getFirstName());
			clientForm.setLastName(client.getLastName());
			clientForm.setClientaddress(client.getAddress()+ " " +client.getSecondLineaddress());
			clientForm.setCountry(client.getCountry());
			clientForm.setDob(client.getDob());
			//Akash 09 nov to set age instead of dob
			
			String agegender="";
			//String dob = client.getDob();
			String age = DateTimeUtils.getAge(client.getDob());
			if(Integer.parseInt(age)<2){
				if(Integer.parseInt(age)<1){
					String monthdays= DateTimeUtils.getMonthandDays(client.getDob());
					agegender=monthdays;
				}else{
					String monthdays= DateTimeUtils.getMonthandDays(client.getDob());
					agegender= age + " Years" + " "+monthdays;
				}
			} else {
				agegender = age + "Years";	
			}
			
			clientForm.setAge(agegender);
			
			clientForm.setEmail(client.getEmail());
			clientForm.setGender(client.getGender());
			clientForm.setMobNo(client.getMobNo());
			clientForm.setPostCode(client.getPostCode());
			clientForm.setReference(client.getReference());
			//String sourceOfIntro = clientDAO.getSourceOfIntro(client.getSourceOfIntro());
			clientForm.setSourceOfIntro(client.getSourceOfIntro());
			clientForm.setTown(client.getTown());
			clientForm.setId(client.getId());
			clientForm.setType(client.getType());
			clientForm.setTypeName(client.getTypeName());
			clientForm.setExpiryDate(client.getExpiryDate());
			clientForm.setNote(client.getNote());
			//String occupation = clientDAO.getOccupation(client.getOccupation());
			clientForm.setOccupation(client.getOccupation());		
			clientForm.setExpiryDate(client.getExpiryDate());
			if(client.getWhopay().equals("Client")){
				client.setWhopay("Self");
			}
			clientForm.setWhopay(client.getWhopay());
			clientForm.setPolicyAuthorzCode(client.getPolicyAuthorzCode());
			clientForm.setPolicyNo(client.getPolicyNo());
			clientForm.setKnownAs(client.getKnownAs());
			clientForm.setCounty(client.getCounty());
			clientForm.setHomeNo(client.getHomeNo());
			clientForm.setWorkNo(client.getWorkNo());
			clientForm.setEmailCc(client.getEmailCc());
			clientForm.setPrefContactMode(client.getPrefContactMode());
			clientForm.setEmergencyContName(client.getEmergencyContName());
			clientForm.setEmergencyContNo(client.getEmergencyContNo());
			clientForm.setPatientType(client.getPatientType());		
			//clientForm.setClientId(clientForm.get);
			clientForm.setClient(client.getTitle()+" " + client.getFirstName()+" " +client.getLastName());
			session.setAttribute("clientId",clientId);
			
			String abbb = client.getAbrivationid();
			clientForm.setAbrivationid(client.getAbrivationid());
			clientForm.setPatientIdAbrivation(client.getPatientIdAbrivation());
			
			
			//Akash 09 nov 2017 to add speciality of dr
			String practtionername = clientDAO.getIpdPractionerName(clientId);
			String speciality = clientDAO.getIpdPractionerSpeciality(clientId);
			String practid = clientDAO.getIpdPractionerId(clientId);
			if(practtionername!=null){
				 if(practtionername.equals("")){
					 practtionername=clientDAO.getPractitionerName(clientId);
					 speciality = clientDAO.getPractitionerSpeciality(clientId);
					 practid = clientDAO.getIpdPractionerId(clientId);
				 }
			} else {
				practtionername=clientDAO.getPractitionerName(clientId);
				speciality = clientDAO.getPractitionerSpeciality(clientId);
				practid = clientDAO.getIpdPractionerId(clientId);
			}
			clientForm.setPractid(practid);
			clientForm.setQualification(speciality);
			clientForm.setPractitionerName(practtionername);
			String clinicName = clientDAO.getClinicName(loginInfo.getClinicid());
			clientForm.setClinicName(clinicName);
			clientForm.setGpname(client.getGpname());
			clientForm.setEmployerName(client.getEmployerName());
			//String treatmentType = clientDAO.getTreatmentType(client.getTreatmentType());
			clientForm.setTreatmentType(client.getTreatmentType());
			clientForm.setReferedDate(client.getReferedDate());
			String insuranceCo = clientDAO.getTPCompanyName(client.getTypeName());
			clientForm.setThirdPartyCompanyName(insuranceCo);
			clientForm.setPolicyExcess(client.getPolicyExcess());
			
			ThirdParty gp = client.getGpDetails();
			clientForm.setGpAddress(gp.getAddress());
			clientForm.setGpTown(gp.getTown());
			clientForm.setGpCounty(gp.getCounty());
			clientForm.setGpPostCode(gp.getPostcode());
			clientForm.setGpname(gp.getGpname());
			clientForm.setSurgeryName(gp.getCompanyName());
			
			
			ThirdParty tp = client.getTpDetails();
			clientForm.setTpAddress(tp.getAddress());
			clientForm.setTpTown(tp.getTown());
			clientForm.setTpCounty(tp.getCounty());
			clientForm.setTpPostCode(tp.getPostcode());
			
			
			
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
			Clinic clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
			clientForm.setClinicName(clinic.getClinicName());
			clientForm.setClinicOwner(clinic.getClinicOwner());
			clientForm.setOwner_qualification(clinic.getOwner_qualification());
			clientForm.setLocationAdressList(locationAdressList);
			clientForm.setAddress(clinic.getAddress());
			clientForm.setLandLine(clinic.getLandLine());
			clientForm.setClinicemail(clinic.getEmail());
			clientForm.setWebsiteUrl(clinic.getWebsiteUrl());
			clientForm.setClinicLogo(clinic.getUserImageFileName());
			
			clientForm.setIpdid(String.valueOf(ipdid));
			
			Client client3 = clientDAO.getDeclarationByID(id);
			session.setAttribute("declarationNotes", client3.getDeclarationNotes());
			//String declarationTitle = clientDAO.getTitleOfDeclaration(loginInfo.getId());
			clientForm.setDeclarationTitle(client3.getDeclarationTitle());
			session.setAttribute("declarationTile",client3.getDeclarationTitle());
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			connection.close();
		}
		//return printDeclaration();
		return "printdeclarationform";
	}
	public String headcircumferencechart() throws Exception{
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			String clientId = request.getParameter("clientId");
			Client client = clientDAO.getPatient(Integer.parseInt(clientId));
			String age1="";
			age1=DateTimeUtils.getAge1(client.getDob());
			
			clientForm.setGender(client.getGender());
			clientForm.setClientId(clientId);
			clientForm.setAbrivationid(client.getAbrivationid());
			clientForm.setFullname(client.getFullname());
			clientForm.setAge(""+age1);
			int heightmastercount = clientDAO.getHeightMasterCount(client.getGender());
			int weightmastercount = clientDAO.getWeightMasterCount(client.getGender());
			int bmimastercount = clientDAO.getBmiMasterCount(client.getGender());
			int headmastercount = clientDAO.getHeadMasterCount(client.getGender());
			int lengthmastercount = clientDAO.getLengthHeightMasterCount(client.getGender());
			
			heightmastercount = heightmastercount+1;
			weightmastercount = weightmastercount+1;
			bmimastercount = bmimastercount+1;
			headmastercount = headmastercount+1;
			lengthmastercount = lengthmastercount+1;
			
			int heightdatacount = clientDAO.getHeightDataCount(clientId);
			int weightdatacount = clientDAO.getWeightDataCount(clientId);
			int bmidatacount = clientDAO.getBmiDataCount(clientId);
			int headdatacount = clientDAO.getHeadDataCount(clientId);
			int lengthdatacount = clientDAO.getLengthHeightDataCount(clientId);
			
			heightdatacount = heightdatacount+1;
			weightdatacount = weightdatacount+1;
			bmidatacount = bmidatacount+1;
			headdatacount = headdatacount+1;
			lengthdatacount = lengthdatacount+1;
			
			int heightcount =-1;
			int weightcount = -1;
			int bmicount = -1;
			int headcount = -1;
			int lengthcount = -1;
			
			if(heightmastercount>heightdatacount){
				heightcount = heightmastercount;
			}else{
				heightcount = heightdatacount;
			}
			
			if(weightmastercount>weightdatacount){
				weightcount=weightmastercount;
			}else{
				weightcount=weightdatacount;
			}
			
			if(bmimastercount>bmidatacount){
				bmicount = bmimastercount;
			}else{
				bmicount =bmidatacount;
			}
			
			if(headmastercount>headdatacount){
				headcount = headmastercount;
			}else{
				headcount =headdatacount;
			}
			
			if(lengthmastercount>lengthdatacount){
				lengthcount = lengthmastercount;
			}else{
				lengthcount = lengthdatacount;
			}

			
			ArrayList<Client> heightmasterlist = clientDAO.getHeightMasterList(client.getGender(),heightcount);
			ArrayList<Client> weightmasterlist = clientDAO.getWeightMasterList(client.getGender(),weightcount);
			ArrayList<Client> bmimasterlist = clientDAO.getBmiMasterList(client.getGender(),bmicount);
			ArrayList<Client> headcircumferncemasterlist = clientDAO.getHeadMasterList(client.getGender(),headcount);
			ArrayList<Client> lengthheightmasterlist = clientDAO.getLengthHeightMasterList(client.getGender(),lengthcount);
			
			//ArrayList<Client> clientgrowthlist = clientDAO.getClientGrowthList(clientId);
			
			/*ArrayList<Client> clientgrowthheightlist = clientDAO.getClientGrowthHeightList(clientId,heightcount);
			ArrayList<Client> clientgrowthweightlist = clientDAO.getClientGrowthWeightList(clientId,weightcount);
			ArrayList<Client> clientgrowthbmilist = clientDAO.getClientGrowthBmiList(clientId,bmicount);
			ArrayList<Client> clientgrowthheadlist = clientDAO.getClientGrowthHeadList(clientId,headcount);
			ArrayList<Client> clientgrowthlengthlist = clientDAO.getClientGrowthLengthList(clientId,lengthcount);*/
			
			ArrayList<Client> clientgrowthheightlist = new ArrayList<Client>();
			ArrayList<Client> clientgrowthweightlist = new ArrayList<Client>();
			ArrayList<Client> clientgrowthbmilist = new ArrayList<Client>();
			ArrayList<Client> clientgrowthheadlist =new ArrayList<Client>();
			ArrayList<Client> clientgrowthlengthlist = new ArrayList<Client>();
			
			if(heightdatacount>0){
				clientgrowthheightlist = clientDAO.getClientGrowthHeightList(clientId,heightcount,heightdatacount);
			}
			
			if(weightdatacount>0){
				clientgrowthweightlist = clientDAO.getClientGrowthHeightList(clientId,weightcount,weightdatacount);
			}
			
			if(bmidatacount>0){
				clientgrowthbmilist = clientDAO.getClientGrowthHeightList(clientId,bmicount,bmidatacount);
			}
			
			if(headdatacount>0){
				clientgrowthheadlist = clientDAO.getClientGrowthHeightList(clientId,headcount,headdatacount);
			}
			
			if(lengthdatacount>1){
				clientgrowthlengthlist = clientDAO.getClientGrowthHeightList(clientId,lengthcount,lengthdatacount);
			}
			
			//lokesh for setting blanks n 0's to null 
			for(Client client1:clientgrowthheightlist){
				if(client1.getHeightdata()!=null){
				if(client1.getHeightdata().equals("0")){
					client1.setHeightdata(null);
				}else if(client1.getHeightdata().equals("")){
					client1.setHeightdata(null);
				}
				}
				if(client1.getWeightdata()!=null){
					if(client1.getWeightdata().equals("0")){
						client1.setWeightdata(null);
					}else if(client1.getWeightdata().equals("")){
						client1.setWeightdata(null);
					}
					}
				if(client1.getBmidata()!=null){
					if(client1.getBmidata().equals("0")){
						client1.setBmidata(null);
					}else if(client1.getBmidata().equals("")){
						client1.setBmidata(null);
					}
					}
				if(client1.getHeadcircumferncedata()!=null){
					if(client1.getHeadcircumferncedata().equals("0")){
						client1.setHeadcircumferncedata(null);
					}else if(client1.getHeadcircumferncedata().equals("")){
						client1.setHeadcircumferncedata(null);
					}
					}
			}
			for(Client client1:clientgrowthbmilist){
				if(client1.getHeightdata()!=null){
				if(client1.getHeightdata().equals("0")){
					client1.setHeightdata(null);
				}else if(client1.getHeightdata().equals("")){
					client1.setHeightdata(null);
				}
				}
				if(client1.getWeightdata()!=null){
					if(client1.getWeightdata().equals("0")){
						client1.setWeightdata(null);
					}else if(client1.getWeightdata().equals("")){
						client1.setWeightdata(null);
					}
					}
				if(client1.getBmidata()!=null){
					if(client1.getBmidata().equals("0")){
						client1.setBmidata(null);
					}else if(client1.getBmidata().equals("")){
						client1.setBmidata(null);
					}
					}
				if(client1.getHeadcircumferncedata()!=null){
					if(client1.getHeadcircumferncedata().equals("0")){
						client1.setHeadcircumferncedata(null);
					}else if(client1.getHeadcircumferncedata().equals("")){
						client1.setHeadcircumferncedata(null);
					}
					}
			}
			for(Client client1:clientgrowthheadlist){
				if(client1.getHeightdata()!=null){
				if(client1.getHeightdata().equals("0")){
					client1.setHeightdata(null);
				}else if(client1.getHeightdata().equals("")){
					client1.setHeightdata(null);
				}
				}
				if(client1.getWeightdata()!=null){
					if(client1.getWeightdata().equals("0")){
						client1.setWeightdata(null);
					}else if(client1.getWeightdata().equals("")){
						client1.setWeightdata(null);
					}
					}
				if(client1.getBmidata()!=null){
					if(client1.getBmidata().equals("0")){
						client1.setBmidata(null);
					}else if(client1.getBmidata().equals("")){
						client1.setBmidata(null);
					}
					}
				if(client1.getHeadcircumferncedata()!=null){
					if(client1.getHeadcircumferncedata().equals("0")){
						client1.setHeadcircumferncedata(null);
					}else if(client1.getHeadcircumferncedata().equals("")){
						client1.setHeadcircumferncedata(null);
					}
					}
			}
			for(Client client1:clientgrowthweightlist){
				if(client1.getHeightdata()!=null){
				if(client1.getHeightdata().equals("0")){
					client1.setHeightdata(null);
				}else if(client1.getHeightdata().equals("")){
					client1.setHeightdata(null);
				}
				}
				if(client1.getWeightdata()!=null){
					if(client1.getWeightdata().equals("0")){
						client1.setWeightdata(null);
					}else if(client1.getWeightdata().equals("")){
						client1.setWeightdata(null);
					}
					}
				if(client1.getBmidata()!=null){
					if(client1.getBmidata().equals("0")){
						client1.setBmidata(null);
					}else if(client1.getBmidata().equals("")){
						client1.setBmidata(null);
					}
					}
				if(client1.getHeadcircumferncedata()!=null){
					if(client1.getHeadcircumferncedata().equals("0")){
						client1.setHeadcircumferncedata(null);
					}else if(client1.getHeadcircumferncedata().equals("")){
						client1.setHeadcircumferncedata(null);
					}
					}
			}
			session.setAttribute("heightmasterlist", heightmasterlist);
			session.setAttribute("weightmasterlist", weightmasterlist);
			session.setAttribute("bmimasterlist", bmimasterlist);
			session.setAttribute("headcircumferncemasterlist", headcircumferncemasterlist);
			//session.setAttribute("clientgrowthlist", clientgrowthlist);
			session.setAttribute("clientgender", client.getGender());
			session.setAttribute("lengthheightmasterlist", lengthheightmasterlist);
			
			session.setAttribute("clientgrowthheightlist", clientgrowthheightlist);
			session.setAttribute("clientgrowthweightlist", clientgrowthweightlist);
			session.setAttribute("clientgrowthbmilist", clientgrowthbmilist);
			session.setAttribute("clientgrowthheadlist", clientgrowthheadlist);
			session.setAttribute("clientgrowthlengthlist", clientgrowthlengthlist);
			clientForm.setStartTimeList(PopulateList.startTimeList());
			
			 Clinic clinic = new Clinic();
			   ClinicDAO clinicDAO= new JDBCClinicDAO(connection);
			   clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
			   clientForm.setClinicName(clinic.getClinicName());
			   clientForm.setClinicOwner(clinic.getClinicOwner());
			   clientForm.setOwner_qualification(clinic.getOwner_qualification());
			  
			   clientForm.setAddress(clinic.getAddress());
			   clientForm.setLandLine(clinic.getLandLine());
			   clientForm.setClinicemail(clinic.getEmail());
			   clientForm.setWebsiteUrl(clinic.getWebsiteUrl());
			   clientForm.setClinicLogo(clinic.getUserImageFileName());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "headcircumferencechart";
	}
	
	
	public String feedbackForm()throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		String treatmenttype="";
		Connection connection=null;
		try {
			String type="";
			type= request.getParameter("type");
			if(type!=null){
				treatmenttype= type;
			}
			
	if(type==null){
			treatmenttype= clientForm.getTreatmenttype();
			if(clientForm.getTreatmenttype()==null||clientForm.getTreatmenttype().equals("")){
				treatmenttype="opd";
			}
			}
			
			  DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			    Calendar cal = Calendar.getInstance();
			   
			    String date = dateFormat.format(cal.getTime());
			    date= DateTimeUtils.getCommencingDate1(date);
			    
			    
			
			connection=Connection_provider.getconnection();
			ClientDAO clientDAO= new JDBCClientDAO(connection);
			ArrayList<Client> patientlist= clientDAO.getpatientlistForFeedback(treatmenttype);
			ArrayList<Client> feedbacklist= clientDAO.getallFeedbacks(treatmenttype);
			clientForm.setPatientlist(patientlist);
			clientForm.setFeedbacklist(feedbacklist);
			StringBuffer feedbackidlist= new StringBuffer();
			feedbackidlist.append("0");
					for(Client client: feedbacklist){
				feedbackidlist.append(","+client.getFeedbackid());
				
			}
					String feedbackids= feedbackidlist.toString();
			clientForm.setFeedbackids(feedbackids);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	return "feedbackForm";
	}
	
	public String savefeedback()throws Exception{
		Connection connection= null;
		try {
			connection= Connection_provider.getconnection();
			ClientDAO clientDAO= new JDBCClientDAO(connection);

			
			 DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			    Calendar cal = Calendar.getInstance();
			   
			    String date = dateFormat.format(cal.getTime());
			    date= DateTimeUtils.getCommencingDate1(date);
			    String manualfeedback= request.getParameter("manualfeedback");
			
		String	treatmenttype= clientForm.getTreatmenttype();
			if(clientForm.getTreatmenttype()==null||clientForm.getTreatmenttype().equals("")){
				treatmenttype="opd";
			}
			Client client= new Client();
			client.setManualfeedback(manualfeedback);
			client.setDate(date);
			client.setTreatmentType(treatmenttype);
			if(treatmenttype.equals("hd")){
				client.setIshd(1);
			}else{
				client.setIshd(0);
			}
			
			String feedbackids= clientForm.getFeedbackids();
			String patientid= clientForm.getPatient();
			client.setPatientID(patientid);
			int i=0;
			for(String id: feedbackids.split(",")){
				if(id.equals("0")){
					continue;
				}
				i=i+1;
				client.setFeedbackid(id);
				String ratings= request.getParameter("rating"+id+"");
				String feedbackname= request.getParameter("feedback"+id);
				client.setRating(ratings);
				client.setFeedbackname(feedbackname);
				System.out.println(feedbackname);
				if(treatmenttype.equals("opd")||treatmenttype.equals("hd")){
					if(i==1){
					int saveparent= clientDAO.saveOPDFeedBackParent(client);
					String parentid= String.valueOf(saveparent);
					if(!parentid.equals("")){
						client.setFeedbackparentid(parentid);
					}
					}
					int savechild=clientDAO.saveChildFeedBack(client);
				}
				if(treatmenttype.equals("ipd")){
					if(i==1){
						int saveparent= clientDAO.saveIPDFeedBackParent(client);
						
						String parentid= String.valueOf(saveparent);
						if(!parentid.equals("")){
							client.setFeedbackparentid(parentid);
						}
						}
						int savechild=clientDAO.saveChildFeedBack(client);
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return "savefeedback";
	}
	
	public String seeallFeedbacks(){
		Connection connection= null;
		try {
			String type="";
			String treatmenttype="";
	
			
				type= request.getParameter("type");
				if(type!=null){
					treatmenttype= type;
				}
				
		if(type==null){
				treatmenttype= clientForm.getTreatmenttype();
				if(clientForm.getTreatmenttype()==null||clientForm.getTreatmenttype().equals("")){
					treatmenttype="opd";
				}
				}
		connection = Connection_provider.getconnection();
		ArrayList<Client> feedbackbypatient= new ArrayList<Client>();
		ClientDAO clientDAO= new JDBCClientDAO(connection);
		if(treatmenttype.equals("all")){
			feedbackbypatient= clientDAO.getallfeedbacksbyPatient("opd");
			ArrayList<Client> list=clientDAO.getallfeedbacksbyPatient("ipd");
			feedbackbypatient.addAll(list);
			
		}else{
		feedbackbypatient= clientDAO.getallfeedbacksbyPatient(treatmenttype);
		}clientForm.setFeedbackbypatient(feedbackbypatient);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "seeallFeedbacks";
	}
	
	public String printfeedbackOFPatient(){
		Connection connection= null;
		try {
			connection= Connection_provider.getconnection();
	
			String parentid= request.getParameter("id");
			String clientname= request.getParameter("clientname");
			String date= request.getParameter("date");
			String feedback= request.getParameter("feedback");
			
			ClientDAO clientDAO= new JDBCClientDAO(connection);
			ArrayList<Client> feedbacklist= new ArrayList<Client>();
			
			feedbacklist=clientDAO.printfeedbackForm(parentid);
			clientForm.setFeedbacklist(feedbacklist);
			clientForm.setClient(clientname);
			clientForm.setDate(date);
			clientForm.setManualfeedback(feedback);
			
			int clientId=0;
			IpdDAO ipdDAO= new JDBCIpdDAO(connection);
			BedDao bedDao = new JDBCBedDao(connection);
			Client client=clientDAO.feebackDetails(parentid);
			if(client.getIpdid().equals("0")){
				clientId=clientDAO.clientIdFromIPDOPD("OPD", client.getOpdid());
			}else{
				clientId=clientDAO.clientIdFromIPDOPD("IPD", client.getIpdid());
				Bed bed=bedDao.getEditIpdData(client.getIpdid());
				client.setIpdid(bed.getIpdseqno());
				client.setAdmissiondate(bed.getAdmissiondate());
				
			}
			Client client2=clientDAO.getPatient(clientId);
			request.setAttribute("FeedBack", client);
			request.setAttribute("clientData", client2);
			
			 Clinic clinic = new Clinic();
			   ClinicDAO clinicDAO= new JDBCClinicDAO(connection);
			   clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
			   clientForm.setClinicName(clinic.getClinicName());
			   clientForm.setClinicOwner(clinic.getClinicOwner());
			   clientForm.setOwner_qualification(clinic.getOwner_qualification());
			  
			   clientForm.setAddress(clinic.getAddress());
			   clientForm.setLandLine(clinic.getLandLine());
			   clientForm.setClinicemail(clinic.getEmail());
			   clientForm.setWebsiteUrl(clinic.getWebsiteUrl());
			   clientForm.setClinicLogo(clinic.getUserImageFileName());

			
		} catch (Exception e) {
		e.printStackTrace();
		}
		return "printfeedbackOFPatient";
	}
	
	public String getallpatientlistForfeedback(){
		Connection connection= null;
		try {
			connection=Connection_provider.getconnection();
			ClientDAO clientDAO= new JDBCClientDAO(connection);
			String type="";
			type= request.getParameter("type");
			String treatmenttype="";
			if(type!=null){
				treatmenttype= type;
			}
			
	if(type==null){
			treatmenttype= clientForm.getTreatmenttype();
			if(clientForm.getTreatmenttype()==null||clientForm.getTreatmenttype().equals("")){
				treatmenttype="opd";
			}
			}
	ArrayList<Client> patientlist1= new ArrayList<Client>();
			ArrayList<Client> patientlist= clientDAO.getpatientlistForFeedback(treatmenttype);
			int i=0;
			for(Client client: patientlist){
				if(i<10){
					patientlist1.add(client);
					++i;
				}
			}
			int j=0;
		StringBuffer str= new StringBuffer();
		str.append("<table class='table table-bordered' > ");
		str.append("<thead>");
		str.append("<tr class='bg-info'>");
		str.append("<th>Id</th> ");
		str.append("<th>Name</th> ");
		str.append("</tr>");
		
		str.append("</thead>");
		str.append("<tbody>");
		for(Client client : patientlist1){
		str.append("<tr id='"+client.getId()+"' onclick='getPatient(this.id)'>");
		str.append("<th>"+j+"</th> ");
		str.append("<th>"+client.getFullname()+"</th> ");
		str.append("</tr>");
		}
		str.append("</tbody>");
		
		str.append("</table>");
		String response1="";
		response1= str.toString();
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		
		response.getWriter().write(""+response1+""); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
public String savetofollowup(){
	String ipdid=request.getParameter("ipdid");
	String opdid= request.getParameter("opdid");
	String clientid=request.getParameter("clientid");
	String today="";
	String followupdate= request.getParameter("followdate");
	String drid=request.getParameter("drid");
	String reqtype= request.getParameter("reqtype");
	//1 for ipd, 2 for opd, 3 for emr 
	
	 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Calendar cal = Calendar.getInstance(); 
	    today = dateFormat.format(cal.getTime());
	    Connection connection= null;
	    Client client= new Client();
	   followupdate= DateTimeUtils.getCommencingDate1(followupdate);
	    try {
	    	connection= Connection_provider.getconnection();
	    	ClientDAO clientDAO= new  JDBCClientDAO(connection);
	    	if(reqtype.equals("1")){
	    		BedDao  bedDao = new JDBCBedDao(connection);
	    		Bed bed=bedDao.getEditIpdData(ipdid);
	    		drid=bed.getPractitionerid();
	    	}else if(reqtype.equals("2")){
	    		
	    		NotAvailableSlot notAvailableSlot = clientDAO.getLastAppointmentdetails(clientid);
	    		drid= String.valueOf(notAvailableSlot.getDiaryUserId());
	    		//opdid is  id of apm_available slot
	    		opdid= String.valueOf(notAvailableSlot.getId());
	    	}
	    	 client.setIpdid(ipdid);
	 	    client.setClientId(clientid);
	 	    client.setOpdid(opdid);
	 	    client.setDate(today);
	 	    client.setFollowupdate(followupdate);
	 	    client.setPractid(drid);
	 	    client.setType(reqtype);
			
			
			clientDAO.savefollowup(client);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	
	return "savetofollowup";
}	


public String followupdashboard(){
	
	String fromdate="";
	String todate="";
	fromdate= clientForm.getFromdate();
	todate= clientForm.getTodate();
	if(fromdate==null){
		fromdate="";
	}
	if(todate==null){
		todate="";
	}
	if(fromdate.equals("")){
		 DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		    Calendar cal = Calendar.getInstance(); 
		    fromdate = dateFormat.format(cal.getTime());
	}
	if(todate.equals("")){
		 DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		    Calendar cal = Calendar.getInstance(); 
		    todate = dateFormat.format(cal.getTime());
	}
	Connection connection= null;
	try {
		connection= Connection_provider.getconnection();
		ArrayList<Client> followuplist= new ArrayList<Client>();
		ClientDAO clientDAO= new JDBCClientDAO(connection);
		followuplist= clientDAO.getallfollowupsToDash("", "", fromdate, todate);
		clientForm.setCondtitionList(followuplist);
		clientForm.setFromdate(fromdate);
		clientForm.setTodate(todate);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return "followupdashboard";
}

public String sendfollowupsms(){
	Connection connection= null;
	try {
		String clientid=request.getParameter("clientid");
		String doctor= request.getParameter("doctor");
		String date= request.getParameter("followdate");
		String id= request.getParameter("id");
		String message="Your Follow up is Scheduled on "+date+" by "+doctor+"";
		connection = Connection_provider.getconnection();
		ClientDAO clientDAO= new JDBCClientDAO(connection);
		Client client= clientDAO.getPatient(Integer.parseInt(clientid));
		SmsService s= new SmsService();
		s.sendvaccineSms(message, client.getMobNo(), loginInfo, new EmailLetterLog(),connection);
		clientDAO.setfollowupsmsflag(id);
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		
		response.getWriter().write(""); 
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}

public String getuhidnew(){
	String clientid=request.getParameter("clientid");
	Connection connection= null;
	try {
		connection = Connection_provider.getconnection();
		ClientDAO clientDAO= new JDBCClientDAO(connection);
		Client client= clientDAO.getPatient(Integer.parseInt(clientid));
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		
		response.getWriter().write(client.getAbrivationid()); 
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	
	return null;
}

public String setFollowupsatus(){
	String id= request.getParameter("id");
	String status= request.getParameter("status");
	String fdate= request.getParameter("fdate");

	Connection connection= null;
	try {
		connection= Connection_provider.getconnection();
		ClientDAO clientDAO= new JDBCClientDAO(connection);
		clientDAO.setFollowupStatus(id, status,fdate);
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""); 
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}


public String sendfollowupsmsall(){
	Connection connection= null;
	try {
		connection= Connection_provider.getconnection();
		String fromdate= clientForm.getFromdate();
		String todate= clientForm.getTodate();
		ClientDAO  clientDAO= new JDBCClientDAO(connection);
		ArrayList<Client> followuplist= new ArrayList<Client>();
		followuplist= clientDAO.getallfollowupsToDash("", "", fromdate, todate);
		for(Client client:followuplist){
			String message="Your Follow up is Scheduled on "+client.getFollowupdate()+" by "+client.getDiaryUser()+"";
			connection = Connection_provider.getconnection();
			 clientDAO= new JDBCClientDAO(connection);
			Client client1= clientDAO.getPatient(Integer.parseInt(client.getClientId()));
			SmsService s= new SmsService();
			s.sendSms(message, client1.getMobNo(), loginInfo, new EmailLetterLog());
			clientDAO.setfollowupsmsflag(String.valueOf(client.getId()));
			
		}
		clientForm.setFromdate(fromdate);
		clientForm.setTodate(todate);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return "sendfollowupsmsall";
}
}