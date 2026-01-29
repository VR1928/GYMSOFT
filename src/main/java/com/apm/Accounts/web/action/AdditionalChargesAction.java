package com.apm.Accounts.web.action;

import java.io.BufferedReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.apm.Accounts.eu.bi.AccountsDAO;
import com.apm.Accounts.eu.bi.AdditionalDAO;
import com.apm.Accounts.eu.blogic.jdbc.JDBCAccountsDAO;
import com.apm.Accounts.eu.blogic.jdbc.JDBCAdditionalDAO;
import com.apm.Accounts.eu.entity.Accounts;
import com.apm.Accounts.eu.entity.Invoice;
import com.apm.Accounts.web.form.AccountsForm;
import com.apm.Appointment.eu.bi.AppointmentDAO;
import com.apm.Appointment.eu.blogic.jdbc.JDBCAppointmentDAO;
import com.apm.Appointment.eu.entity.AppointmentType;
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
import com.apm.Emr.eu.bi.EmrDAO;
import com.apm.Emr.eu.blogic.jdbc.JDBCEmrDAO;
import com.apm.Inventory.eu.bi.InventoryProductDAO;
import com.apm.Inventory.eu.blogic.jdbc.JDBCInventoryProductDAO;
import com.apm.Inventory.eu.entity.Product;
import com.apm.Ipd.eu.bi.BedDao;
import com.apm.Ipd.eu.bi.IpdDAO;
import com.apm.Ipd.eu.blogic.jdbc.JDBCBedDao;
import com.apm.Ipd.eu.blogic.jdbc.JDBCIpdDAO;
import com.apm.Ipd.eu.entity.Bed;
import com.apm.Master.eu.bi.MasterDAO;
import com.apm.Master.eu.blogic.jdbc.JDBCMasterDAO;
import com.apm.Master.eu.entity.Master;
import com.apm.Registration.eu.blogic.jdbc.JDBCClinicDAO;
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

import atg.taglib.json.util.JSONObject;

public class AdditionalChargesAction extends BaseAction implements Preparable,
		ModelDriven<AccountsForm> {

	HttpServletRequest request = (HttpServletRequest) ActionContext
			.getContext().get(ServletActionContext.HTTP_REQUEST);
	HttpServletResponse response = (HttpServletResponse) ActionContext
			.getContext().get(ServletActionContext.HTTP_RESPONSE);
	HttpSession session = request.getSession(true);
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);

	AccountsForm accountsForm = new AccountsForm();
	private Pagination pagination = new Pagination(100, 1);

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public String execute() throws Exception {

		if (!verifyLogin(request)) {
			return "login";
		}

		Connection connection = null;
		try {

			connection = Connection_provider.getconnection();
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public String cr() throws Exception {

		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(
					connection);

			String clientid = request.getParameter("clientid");
			session.setAttribute("sessionselectedclientid", clientid);

			ClientDAO clientDAO = new JDBCClientDAO(connection);
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);

			// set selected clientid from session
			String clientId = request.getParameter("clientId");
			if (clientId == null) {
				clientId = (String) session
						.getAttribute("sessionselectedclientid");
				accountsForm.setClientId(clientId);
				Client clients = clientDAO
						.getSelectedSessionClientDetails(clientId);
				accountsForm.setClient(clients.getClientName());

			} else {

				accountsForm.setClientId(clientId);
				Client clients = clientDAO
						.getSelectedSessionClientDetails(clientId);
				accountsForm.setClient(clients.getClientName());
			}
			int result = completeAptmDAO.deleteComplteApmt(loginInfo.getId());

			String date= DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
			accountsForm.setDate(DateTimeUtils.getCommencingDate1(date));
			//remove invoice id condition means readonl condition 
			//boolean checkinvoice = completeAptmDAO.checkInvoiceBalanace(clientid);
			boolean checkinvoice = true;
			accountsForm.setCheckinvoice(String.valueOf(checkinvoice));
			accountsForm.setApprovedrefund("0");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			connection.close();
		}

		return "creditcharge";
	}

	public String credit() throws Exception{
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(
					connection);

			ClientDAO clientDAO = new JDBCClientDAO(connection);
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);

			// set selected clientid from session
			String clientId = request.getParameter("clientId");
			String invoiceid = request.getParameter("invoiceid");
			
			if(clientId!=null){
				if(clientId.equals("")){
					clientId=null;
				}
			}
			
			if(clientId==null){
				clientId=request.getParameter("clientid");
			}
			if (clientId == null) {
				clientId = (String) session
						.getAttribute("sessionselectedclientid");
				accountsForm.setClientId(clientId);
				Client clients = clientDAO
						.getSelectedSessionClientDetails(clientId);
				accountsForm.setClient(clients.getClientName());

			} else {

				accountsForm.setClientId(clientId);
				Client clients = clientDAO
						.getSelectedSessionClientDetails(clientId);
				accountsForm.setClient(clients.getClientName());
			}
			
			int result = completeAptmDAO.deleteComplteApmt(loginInfo.getId());
			session.removeAttribute("sessionadmissionid");
			String date= DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
			accountsForm.setDate(DateTimeUtils.getCommencingDate1(date));

			//boolean checkinvoice = completeAptmDAO.checkInvoiceBalanace(clientId);
			boolean checkinvoice = true;
			accountsForm.setCheckinvoice(String.valueOf(checkinvoice));
			accountsForm.setApprovedrefund("0");
			
			if(invoiceid!=null){
				if(!invoiceid.equals("")){
					accountsForm.setRefundinvoiceid(invoiceid);
				}else{
					accountsForm.setRefundinvoiceid("0");
				}
			}else{
				accountsForm.setRefundinvoiceid("0");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			connection.close();
		}

		return "creditcharge";
	}



	public String dbt() throws Exception{
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(
					connection);

			ClientDAO clientDAO = new JDBCClientDAO(connection);
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);

			// set selected clientid from session
			String clientId = request.getParameter("clientid");
			if (clientId == null) {
				clientId = (String) session
						.getAttribute("sessionselectedclientid");
				accountsForm.setClientId(clientId);
				Client clients = clientDAO
						.getSelectedSessionClientDetails(clientId);
				accountsForm.setClient(clients.getClientName());

			} else {

				accountsForm.setClientId(clientId);
				Client clients = clientDAO
						.getSelectedSessionClientDetails(clientId);
				accountsForm.setClient(clients.getClientName());
			}

			int result = completeAptmDAO.deleteComplteApmt(loginInfo.getId());

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			connection.close();
		}

		return "debitcharge";
	}

	public String opddebit() throws Exception {
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(
					connection);

			ClientDAO clientDAO = new JDBCClientDAO(connection);
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);

			// set selected clientid from session
			String clientId = request.getParameter("clientid");
			String apmtid = request.getParameter("apmtid");
			session.setAttribute("otsessionapmtid", apmtid);
			if (clientId == null) {
				clientId = (String) session
						.getAttribute("sessionselectedclientid");
				accountsForm.setClientId(clientId);
				Client clients = clientDAO
						.getSelectedSessionClientDetails(clientId);
				accountsForm.setClient(clients.getClientName());
				accountsForm.setTpid(clientDAO.getTpIDformPatient(clientId));
				
				String payby=clients.getWhopay();
				if(payby!=null){
					 if(payby.equals("Client")){
						 accountsForm.setPayee("0");
					 }else {
						 accountsForm.setPayee("1");
					 }
				}
				
				
			} else {

				accountsForm.setClientId(clientId);
				Client clients = clientDAO
						.getSelectedSessionClientDetails(clientId);
				accountsForm.setClient(clients.getClientName());
				accountsForm.setTpid(clientDAO.getTpIDformPatient(clientId));
				String payby=clients.getWhopay();
				if(payby!=null){
					 if(payby.equals("Client")){
						 accountsForm.setPayee("0");
					 }else {
						 accountsForm.setPayee("1");
					 }
				}
			}

			Calendar calendar=Calendar.getInstance();
			SimpleDateFormat dateFormat= new SimpleDateFormat("dd-MM-yyyy");
			String date =dateFormat.format(calendar.getTime());
			accountsForm.setDate(date);
			int result = completeAptmDAO.deleteComplteApmt(loginInfo.getId());
			session.removeAttribute("sessionadmissionid");

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			connection.close();
		}

		return "otdebitcharge";
	}

	public String debit()  throws Exception{
		Connection connection = null;

		try {
			connection = Connection_provider.getconnection();
			CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(
					connection);

			ClientDAO clientDAO = new JDBCClientDAO(connection);
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);

			// set selected clientid from session
			String clientId = accountsForm.getClientId();

			String ipdid=request.getParameter("ipdid");
			if(ipdid==null){
				ipdid="0";
			}
			if(ipdid.equals("")){
				ipdid="0";
			}
			
			
			accountsForm.setIpdadmissionid(ipdid);
			
			session.removeAttribute("otsessionapmtid");
			if (clientId == null) {
				clientId = (String) session
						.getAttribute("sessionselectedclientid");
				accountsForm.setClientId(clientId);
				Client clients = clientDAO
						.getSelectedSessionClientDetails(clientId);
				accountsForm.setClient(clients.getClientName());

			}

			if (clientId != null) {

				if (clientId.equals("")) {
					clientId = null;
				}
				
			}

			if (clientId == null) {
				clientId = request.getParameter("clientId");
				accountsForm.setClientId(clientId);
				Client clients = clientDAO
						.getSelectedSessionClientDetails(clientId);
				accountsForm.setClient(clients.getClientName());
				
			}
			
			String payby=(String)session.getAttribute("payby");
			if(payby!=null){
				if(payby.equals("")){
					payby = null;
				}
			}
			if(payby!=null){
				 if(payby.equals("Client")){
					 accountsForm.setPayee("0");
				 }else {
					 accountsForm.setPayee("1");
				 }
			}
			else {
				Client clients = clientDAO.getSelectedSessionClientDetails(clientId);
				payby=clients.getWhopay();
				if(payby!=null){
					 if(payby.equals("Client")){
						 accountsForm.setPayee("0");
					 }else {
						 accountsForm.setPayee("1");
					 }
				}
			}
			
			
			
			Client client=clientDAO.getClientDetails(clientId);
			String tpid=client.getTypeName();
			accountsForm.setTpid(tpid);
			accountsForm.setAbrivationid(client.getAbrivationid());
			Calendar calendar=Calendar.getInstance();
			SimpleDateFormat dateFormat= new SimpleDateFormat("dd-MM-yyyy");
			String date =dateFormat.format(calendar.getTime());
			accountsForm.setDate(date);
			IpdDAO ipdDAO=new JDBCIpdDAO(connection);
			
			ArrayList<Master>patientPackageList = ipdDAO.getPatientPackageListByClientid(clientId);
			accountsForm.setPatientPackageList(patientPackageList);
			int result = completeAptmDAO.deleteComplteApmt(loginInfo.getId());
			session.removeAttribute("sessionadmissionid");
			int lastipdid=ipdDAO.getLastIpdId(clientId);
			accountsForm.setLastipdid(String.valueOf(lastipdid));
			BedDao bedDao=new JDBCBedDao(connection);
			 Bed bed = bedDao.getEditIpdData(String.valueOf(lastipdid));
		String admissiondate=bed.getAdmissiondate();
		if(admissiondate==null)
		{
			admissiondate="";
		}
		if(admissiondate.equals("0")){
			admissiondate="";
		}
		String[]temp=admissiondate.split(" ");
		if(!admissiondate.equals("")){
			String admissiondate1=temp[0];
			String admissiontime=DateTimeUtils.getCommencingDate1(admissiondate1);
			accountsForm.setAdmissionDate(admissiontime+" "+temp[1]);	
		}
		
		int res=completeAptmDAO.checkInvoiceCreated(String.valueOf(lastipdid));
		if(accountsForm.getIpdadmissionid().equals("0")){
			accountsForm.setIpdadmissionid(String.valueOf(lastipdid));
		}
		session.setAttribute("sessionadmissionid", lastipdid);
		if(res>0){
			accountsForm.setInvoicecreated(true);
		}else{
			accountsForm.setInvoicecreated(false);
		}
		
		MasterDAO masterDAO= new JDBCMasterDAO(connection);
		ArrayList<Master> taxlist= new ArrayList<Master>();
		taxlist= masterDAO.getMasterList("his_tax_master");
		accountsForm.setTaxlist(taxlist);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			connection.close();
		}

		return "debitcharge";
	}

	public String dashb() throws Exception {
		if (!verifyLogin(request)) {
			return "login";
		}

		String clientid = request.getParameter("clientid");
		session.setAttribute("sessionselectedclientid", clientid);

		return input();
	}

	public String input() throws Exception {
		if (!verifyLogin(request)) {
			return "login";
		}

		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);

			// set selected clientid from session
			if (session.getAttribute("sessionselectedclientid") != null) {
				String clientId = (String) session
						.getAttribute("sessionselectedclientid");
				accountsForm.setClientId(clientId);
				Client clients = clientDAO
						.getSelectedSessionClientDetails(clientId);
				accountsForm.setClient(clients.getClientName());

			}

			// truncate tempcharge table
			int del = accountsDAO.deleteTempChagesInvoiceStringData(loginInfo.getLoginsessionid());

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			connection.close();
		}

		return "inputSucess";
	}

	public String save() throws Exception{
		if (!verifyLogin(request)) {
			return "login";
		}

		Connection connection = null;
		try {

			connection = Connection_provider.getconnection();
			AdditionalDAO additionalDAO = new JDBCAdditionalDAO(connection);
			CompleteAptmDAO aptmDAO=new JDBCCompleteAptmDAO(connection);
			String clientId = accountsForm.getClientId();
			String clientname = accountsForm.getClient();
			String type = accountsForm.getApmtType();
			String date = DateTimeUtils.getDateinSimpleFormate(new Date());
			String location = accountsForm.getLocation();
			// String creditDebitCharge = accountsForm.getCreditDebitCharge();
			String payby = accountsForm.getPayby();	
			String stemp[] = date.split(" ");

			String temp[] = stemp[0].split("-");
			date = temp[2] + "-" + temp[1] + "-" + temp[0];
			
			date = DateTimeUtils.getCommencingDate1(accountsForm.getDate());

			if (payby.equalsIgnoreCase("1")) {
				payby = Constants.PAY_BY_THIRD_PARTY;
			} else {
				payby = Constants.PAY_BY_CLIENT;
			}

			String apmtid = "0";
			if (session.getAttribute("otsessionapmtid") != null) {
				apmtid = (String) session.getAttribute("otsessionapmtid");
			}
			
			int invoiceid = additionalDAO.saveInvoce(clientId, clientname,
					type, date, location, apmtid,loginInfo.getUserId());
			ArrayList<CompleteAppointment> assesmentList = additionalDAO
					.getCompleteApmtList(clientId,loginInfo.getId());
						for (CompleteAppointment appointment : assesmentList) {
				//Akash 27 Jan 2018 pass date1 instead of date
				String date1= appointment.getCommencing();
				
//				int result = additionalDAO.saveAssessment(clientId, clientname,
//						type, date1, invoiceid, appointment);
				if(loginInfo.getIskunal()==1){
					appointment.setIskunal(true);
				}else{
					appointment.setIskunal(false);
				}
				appointment.setDate(DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
				appointment.setUserid(loginInfo.getUserId());
				
				int result=aptmDAO.saveInvoiceAssesment(appointment, invoiceid);
				int log=aptmDAO.saveManualChargeLog(appointment,invoiceid);
				boolean ispkg = aptmDAO.checkifpkg(appointment.getMasterchargetype());
				if(ispkg){
					int res= aptmDAO.updatePackageStatus(clientId);
				}
			}
			String mastername=assesmentList.get(assesmentList.size()-1).getMasterchargetype();
			boolean ispkg = aptmDAO.checkifpkg(mastername);
			if(ispkg){
				int res= aptmDAO.updatePackageStatus(clientId);
			}
			CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(
					connection);
			
			int result = completeAptmDAO.deleteComplteApmt(loginInfo.getId());
			String raiseChargeType = "1";
			
			accountsForm.setClientId(clientId);
			accountsForm.setClient(clientname);
			accountsForm.setPayby("Client");
			session.setAttribute("clientId", clientId);
			session.setAttribute("clientname", clientname);
			session.setAttribute("payby", payby);
			session.setAttribute("raiseChargeType", raiseChargeType);
			session.setAttribute("location", location);

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			connection.close();
		}
		return "raiseChargePage";
	}

	public String saveCharge() throws Exception{
		if (!verifyLogin(request)) {
			return "login";
		}
		Double total = 0.00;
		String totalx = "";
		Connection connection = null;
		try {

			connection = Connection_provider.getconnection();
			AdditionalDAO additionalDAO = new JDBCAdditionalDAO(connection);
			String clientId = request.getParameter("clientid");
			String clientname = request.getParameter("clientname");
			String type = request.getParameter("type");
			String manualTypeName = request.getParameter("manualTypeName");
			String manualCharge = request.getParameter("manualCharge");
			String creditDebitCharge = request
					.getParameter("creditDebitCharge");
			String payBuy = request.getParameter("payBuy");

			String date = DateTimeUtils.getDateinSimpleFormate(new Date());
			String stemp[] = date.split(" ");

			String temp[] = stemp[0].split("-");
			date = temp[2] + "-" + temp[1] + "-" + temp[0];

			if (type.equalsIgnoreCase("0")) {
				int result = additionalDAO.saveManualCharge(clientId,
						clientname, manualTypeName, manualCharge, date,
						creditDebitCharge, payBuy,loginInfo.getId());

			} else {
				int result = additionalDAO.saveCharge(clientId, clientname,
						type, date, creditDebitCharge, payBuy,loginInfo.getId());
			}
			ArrayList<CompleteAppointment> clientChargeListDetail = new ArrayList<CompleteAppointment>();

			clientChargeListDetail = additionalDAO
					.getPatientChrageDetails(clientId,loginInfo.getId());

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
		}finally{
			
			connection.close();
		}
		return null;
	}

	public String cashDesk() throws SQLException {
		if (!verifyLogin(request)) {
			return "login";
		}
		String id = request.getParameter("clientid");
		System.out.println(id);
		double total = 0;
		String totalx = "";
		Connection connection = null;

		try {

			CompleteAppointment completeAppointment = new CompleteAppointment();
			ArrayList<CompleteAppointment> clientChargeListDetail = new ArrayList<CompleteAppointment>();
			connection = Connection_provider.getconnection();
			AdditionalDAO additionalDAO = new JDBCAdditionalDAO(connection);
			clientChargeListDetail = additionalDAO.getPatientChrageDetails(id,loginInfo.getId());

			// total = completeAppointment2.getChargeTotal();

			StringBuffer str = new StringBuffer();

			// Practitioner Mail
			str
					.append("<table  id = 'cashDesk' cellpadding='0' cellspacing='0' class='my-table' > ");

			str.append("<tr>");
			str.append("<th>No</th> ");
			str.append("<th>Charge Details</th> ");
			str.append("<th>Charge Type</th> ");
			str.append("<th>Amount</th> ");
			str.append("<th>To</th> ");
			str.append("<th>Delete</th> ");
			str.append("</tr>");
			int count = 0;
			for (CompleteAppointment completeAppointment1 : clientChargeListDetail) {

				str.append("<tr>");
				str.append("<td>" + completeAppointment1.getId() + "</td>");
				str.append("<td>" + completeAppointment1.getApmtType()
						+ "</td>");

				if (completeAppointment1.getChargeType().equals("1")) {
					str.append("<td>CREDIT</td>");
				} else {
					str.append("<td>DEBIT</td>");
				}

				str.append("<td>" + Constants.getCurrency(loginInfo)
						+ completeAppointment1.getCharges() + "</td>");

				if (completeAppointment1.getPayBuy().equals("0")) {
					str.append("<td>Self</td>");
				} else {
					str.append("<td>Third party</td>");
				}

				/*
				 * if(count==0){ str.append("<td><img
				 * src='common/images/delete.gif'></img></td>");
				 * 
				 * }else{ str.append("<td onclick = 'confirmedDelete1("+completeAppointment1.getId()+")'><img
				 * src='common/images/delete.gif'></img></td>");
				 *  }
				 */

				str.append("<td onclick = 'confirmedDelete1("
						+ completeAppointment1.getId()
						+ ")'><img src='common/images/delete.gif'></img></td>");

				str.append("</tr>");
				count = count + 1;
			}
			str.append("</table>");
			for (CompleteAppointment completeAppointment2 : clientChargeListDetail) {
				total = completeAppointment2.getChargeTotal();
				totalx = completeAppointment2.getChargeTotalx();
			}

			str.append("<tr style='background-color: #efefef;'>");
			str.append("<th colspan='3' style='font-size: 13px;font-weight: bold;'>Total</th> ");

			str.append("<th colspan='3' style='font-size: 13px;font-weight: bold;'>" + Constants.getCurrency(loginInfo)
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

	public String instantcash() throws Exception{

		if (!verifyLogin(request)) {
			return "login";
		}
		Connection connection = null;
		try {

			connection = Connection_provider.getconnection();
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			AdditionalDAO additionalDAO = new JDBCAdditionalDAO(connection);
			String clientId = accountsForm.getClientId();
			String clientname = accountsForm.getClient();
			String type = accountsForm.getApmtType();
			String date = accountsForm.getDate();
			String location = accountsForm.getLocation();
			String creditDebitCharge = accountsForm.getCreditDebitCharge();
			
			if(date==null){
				date = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
			} else {
				if(date.equals("")){
					date = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
				} else {
					date = DateTimeUtils.getCommencingDate1(date);
				}
			}
			
			// get details from appoinymentid
			CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(connection);
			boolean isinvoiced = completeAptmDAO.checkAppointmentInvoiced(accountsForm.getAppointmentid());
			
			
			
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			ArrayList<DiaryManagement> userList = notAvailableSlotDAO.getUserList(loginInfo.getId());
			accountsForm.setUserList(userList);

			if (!isinvoiced) {
				NotAvailableSlot availableSlot = accountsDAO.getClientsAppointmentData(accountsForm.getAppointmentid());
				accountsForm.setClientId(availableSlot.getClientId());
				accountsForm.setPayby(availableSlot.getPayBy());
				
				
				accountsForm.setDoctorid(availableSlot.getDiaryUser());
				

				ClientDAO clientDAO = new JDBCClientDAO(connection);
				Client client = clientDAO.getClientDetails(accountsForm
						.getClientId());
				accountsForm.setClient(client.getTitle() + " "
						+ client.getFirstName() + " "+client.getMiddlename()+" "+ client.getLastName());


				if (!accountsForm.getClient().equals("")) {
					
					String ctemp[] = clientname.split(" ");
					accountsForm.setInitial(ctemp[0]);
					accountsForm.setFirstname(ctemp[1]);
					accountsForm.setLastname(ctemp[2]);
				}

				String invoiceid = accountsDAO.getAppointmentInvoiceid(accountsForm.getAppointmentid());
				String locationid = accountsDAO.getCashDeskLocation(invoiceid);
				accountsForm.setLocation(locationid);

				ArrayList<Invoice> list = accountsDAO.getTotalInvoice(accountsForm.getAppointmentid());
				if (list.size() > 1) {
					String payby = "Client";
					accountsForm.setPayby(payby);
				}

				ArrayList<Accounts> accountList = accountsDAO.getCashdeskChargeList(invoiceid,clientId);
				
				ArrayList<Master>ledgerservicesList = accountsDAO.getcashdeskLedgerServicesList(invoiceid,clientId);
				accountsForm.setLedgerservicesList(ledgerservicesList);
				session.setAttribute("cashledgerservicesList", ledgerservicesList);

				double debitTotal = 0.0;
				double balanceTotal = 0.0;

				for (Accounts accounts : accountList) {
					debitTotal = debitTotal + accounts.getDebitAmount();

				}

				accountsForm.setAccountList(accountList);
				accountsForm.setDebitTotal(debitTotal);
				accountsForm.setBalanceTotal(debitTotal);
				accountsForm.setNumberOfChages(accountList.size());

				accountsForm.setDebitTotalx(DateTimeUtils.changeFormat(debitTotal));
				accountsForm.setBalanceTotalx(DateTimeUtils.changeFormat(debitTotal));

				// CompleteAptmDAO completeAptmDAO = new
				// JDBCCompleteAptmDAO(connection);

				int result = completeAptmDAO.deleteComplteApmt(loginInfo.getId());

				accountsForm.setCreditDebitCharge(creditDebitCharge);

				// set pre-payment mode
				
				
				int res=accountsDAO.getidforlist("OPD");
				
					accountsForm.setInvcetype(String.valueOf(res));
				boolean checkCreditAmount = accountsDAO.checkCreditAmount(accountsForm.getClientId());
				accountsForm.setBalanceAmt(checkCreditAmount);
				if (checkCreditAmount == true) {
					double balanceAmmount = accountsDAO.getBlanceAmount(accountsForm.getClientId());
					String amount = DateTimeUtils.changeFormat(balanceAmmount);
					accountsForm.setBalanceAmount(amount);
				} else {
					accountsForm.setBalanceAmount("0.00");
				}

			} else {
				addActionError("Invoice already created, Can't cash desk again!!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			connection.close();
		}

		return "cashdesk";
	}
	
	public String invstcash() throws Exception{
		
		if (!verifyLogin(request)) {
			return "login";
		}
		Connection connection = null;
		try {

			connection = Connection_provider.getconnection();
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			AdditionalDAO additionalDAO = new JDBCAdditionalDAO(connection);
			String clientId = accountsForm.getClientId();
			String clientname = accountsForm.getClient();
			String type = accountsForm.getApmtType();
			String date = DateTimeUtils.getDateinSimpleFormate(new Date());
			String location = accountsForm.getLocation();
			String creditDebitCharge = accountsForm.getCreditDebitCharge();
			
			
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			ArrayList<DiaryManagement> userList = notAvailableSlotDAO.getUserList(loginInfo.getId());
			accountsForm.setUserList(userList);
			
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			Client client = clientDAO.getClientDetails(clientId);
			
			accountsForm.setClientId(clientId);
			accountsForm.setPayby(client.getWhopay());
			
			if(accountsForm.getInvoiceid()==0){
				accountsForm.setInvoiceid(Integer.parseInt(accountsForm.getCashinvoiceid()));
			}
			
			
			accountsForm.setClient(client.getTitle() + " "+ client.getFirstName() + " "+client.getMiddlename()+" "+ client.getLastName());

			String stemp[] = date.split(" ");

			String temp[] = stemp[0].split("-");
			date = temp[2] + "-" + temp[1] + "-" + temp[0];
			
			String locationid = accountsDAO.getCashDeskLocation(Integer.toString(accountsForm.getInvoiceid()));
			accountsForm.setLocation(locationid);
			
			String payby = "Client";
			accountsForm.setPayby(payby);

			ArrayList<Accounts> accountList = accountsDAO.getCashdeskChargeList(Integer.toString(accountsForm.getInvoiceid()),clientId);
			
			ArrayList<Master>ledgerservicesList = accountsDAO.getcashdeskLedgerServicesList(Integer.toString(accountsForm.getInvoiceid()),accountsForm.getClientId());
			accountsForm.setLedgerservicesList(ledgerservicesList);
			session.setAttribute("cashledgerservicesList", ledgerservicesList);
			
			
			double debitTotal = 0.0;
			double balanceTotal = 0.0;
	
			for (Accounts accounts : accountList) {
				debitTotal = debitTotal + accounts.getDebitAmount();
				String totalassesment = "0,"+accounts.getInvoiceid();
				accountsForm.setTotalassesment(totalassesment);
					
			}
	
			accountsForm.setAccountList(accountList);
			accountsForm.setDebitTotal(debitTotal);
			accountsForm.setBalanceTotal(debitTotal);
			accountsForm.setNumberOfChages(accountList.size());
			
			
			accountsForm.setDebitTotalx(DateTimeUtils.changeFormat(debitTotal));
			accountsForm.setBalanceTotalx(DateTimeUtils.changeFormat(debitTotal));

			// CompleteAptmDAO completeAptmDAO = new
			// JDBCCompleteAptmDAO(connection);


			accountsForm.setCreditDebitCharge(creditDebitCharge);

			// set pre-payment mode

			boolean checkCreditAmount = accountsDAO.checkCreditAmount(accountsForm.getClientId());
			accountsForm.setBalanceAmt(checkCreditAmount);
			if (checkCreditAmount == true) {
				double balanceAmmount = accountsDAO.getBlanceAmount(accountsForm.getClientId());
				String amount = DateTimeUtils.changeFormat(balanceAmmount);
				accountsForm.setBalanceAmount(amount);
			} else {
				accountsForm.setBalanceAmount("0.00");
			}


			accountsForm.setAppointmentid("");
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			connection.close();
		}
		
		return "cashdesk";
	}

	public String casdesk() throws Exception{
		if (!verifyLogin(request)) {
			return "login";
		}
		Connection connection = null;
		try {

			connection = Connection_provider.getconnection();
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			AdditionalDAO additionalDAO = new JDBCAdditionalDAO(connection);
			String clientId = accountsForm.getClientId();
			String clientname = accountsForm.getClient();
			String type = accountsForm.getApmtType();
			String date = accountsForm.getDate();
			String location = accountsForm.getLocation();
			String creditDebitCharge = accountsForm.getCreditDebitCharge();
			String manualinvoiceid = accountsForm.getManualinvoiceid();
			String refundnote = accountsForm.getRefundnote();
			String refundrequestid = accountsForm.getRefundrequestid();
			String invoicetype=accountsForm.getInvoicetype();
			if(invoicetype==null){
				invoicetype="0";
			}
			session.setAttribute("manualinvoiceid", manualinvoiceid);
			session.setAttribute("refundnote", refundnote);
			session.setAttribute("refundrequestid", refundrequestid);
			if(date==null){
				date = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
			} else {
				if(date.equals("")){
					date = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
				} else {
					date = DateTimeUtils.getCommencingDate1(date);
				}
			}
			
			date = DateTimeUtils.getCommencingDate1(accountsForm.getDate());

			if (!accountsForm.getClient().equals("")) {
				String ctemp[] = clientname.split(" ");
				accountsForm.setInitial(ctemp[0]);
				accountsForm.setFirstname(ctemp[1]);
				accountsForm.setLastname(ctemp[2]);
			}

			if (creditDebitCharge.equalsIgnoreCase("1")) {
				String tempTime= DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[1];
				String dateTime= date+" "+tempTime;

				ArrayList<CompleteAppointment> assesmentList = additionalDAO.getCompleteApmtList(clientId,loginInfo.getId());

				if (assesmentList.size() > 0) {
					int invoiceid = additionalDAO.saveCreditInvoice(clientId,clientname, type, dateTime, location);
					/*for (CompleteAppointment appointment : assesmentList) {
						int result = additionalDAO.saveCreditAssessment(clientId, clientname, type, dateTime, invoiceid,appointment);
					}*/
					accountsForm.setCreditChargeId(Integer.toString(invoiceid));

					ArrayList<Accounts> accountList = accountsDAO.getCreditChargeAccountList(clientId, invoiceid,loginInfo.getId());

					double debitTotal = 0.0;
					double balanceTotal = 0.0;

					for (Accounts accounts : accountList) {
						debitTotal = debitTotal+ Double.parseDouble(accounts.getCharges());

					}

					accountsForm.setAccountList(accountList);
					accountsForm.setDebitTotal(debitTotal);
					accountsForm.setBalanceTotal(debitTotal);
					accountsForm.setNumberOfChages(accountList.size());

					accountsForm.setDebitTotalx(DateTimeUtils.changeFormat(debitTotal));
					accountsForm.setBalanceTotalx(DateTimeUtils.changeFormat(debitTotal));

				}

				CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(connection);

				//int result = completeAptmDAO.deleteComplteApmt(loginInfo.getId());

				/*
				 * int save = additionalDAO.saveTempCreditAccounts(invoiceid);
				 * 
				 * String allChargeList =
				 * accountsDAO.getTempChagesInvoiceStringData();
				 * 
				 * 
				 * ArrayList<Accounts>accountList =
				 * accountsDAO.getCashdeskChargeList(allChargeList);
				 */

				if (!accountsForm.getLocation().equals("")) {
					String locationName = accountsDAO
							.getLocationName(accountsForm.getLocation());
					accountsForm.setLocationName(locationName);
				}

			} else {
				
				accountsDAO.deleteTempChagesInvoiceStringData(loginInfo.getLoginsessionid());

				ArrayList<CompleteAppointment> assesmentList = additionalDAO
						.getCompleteApmtList(clientId,loginInfo.getId());
				int invoiceid = 0;

				if (assesmentList.size() > 0) {
					String apmtid = "0";
					if (session.getAttribute("otsessionapmtid") != null) {
						apmtid = (String) session
								.getAttribute("otsessionapmtid");
					}

					invoiceid = additionalDAO.saveInvoce(clientId, clientname,
							type, date, location, apmtid,loginInfo.getUserId());

					for (CompleteAppointment appointment : assesmentList) {
						//Akash 27 Jan 2018 pass date1 instead of date
						String date1= appointment.getCommencing();
						int result = additionalDAO.saveAssessment(clientId,
								clientname, type, date1, invoiceid, appointment);
					}
					
					

					int save = accountsDAO.saveTempChargeAccounts(invoiceid,loginInfo.getLoginsessionid());

				}

				String allChargeList = accountsDAO
						.getTempChagesInvoiceStringData(loginInfo.getLoginsessionid());

				ArrayList<Accounts> accountList = accountsDAO
						.getCashdeskChargeList(allChargeList,clientId);

				double debitTotal = 0.0;
				double balanceTotal = 0.0;

				for (Accounts accounts : accountList) {
					debitTotal = debitTotal + accounts.getDebitAmount();

				}

				accountsForm.setAccountList(accountList);
				accountsForm.setDebitTotal(debitTotal);
				accountsForm.setBalanceTotal(debitTotal);
				accountsForm.setNumberOfChages(accountList.size());

				accountsForm.setDebitTotalx(DateTimeUtils
						.changeFormat(debitTotal));
				accountsForm.setBalanceTotalx(DateTimeUtils
						.changeFormat(debitTotal));

				CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(
						connection);

				int result = completeAptmDAO.deleteComplteApmt(loginInfo.getId());

				if (!accountsForm.getLocation().equals("")) {
					String locationName = accountsDAO
							.getLocationName(accountsForm.getLocation());
					accountsForm.setLocationName(locationName);
				}
			}

			accountsForm.setCreditDebitCharge(creditDebitCharge);

			// set pre-payment mode

			boolean checkCreditAmount = accountsDAO
					.checkCreditAmount(accountsForm.getClientId());
			accountsForm.setBalanceAmt(checkCreditAmount);
			if (checkCreditAmount == true) {
				double balanceAmmount = accountsDAO
						.getBlanceAmount(accountsForm.getClientId());
				String amount = DateTimeUtils.changeFormat(balanceAmmount);
				accountsForm.setBalanceAmount(amount);
			} else {
				accountsForm.setBalanceAmount("0.00");
			}
			
			//Akash 06 dec 2017 set dr name list 
			Bed bed = new Bed();
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			BedDao bedDao = new JDBCBedDao(connection);
			IpdDAO ipdDAO = new JDBCIpdDAO(connection);
			int apptid=0;
			String practid = "0";
			int ipdid = ipdDAO.getLastIpdId(accountsForm.getClientId());
			if(ipdid>0){
				bed = bedDao.getEditIpdData(String.valueOf(ipdid));
				practid = bed.getPractitionerid();
			}else{
				apptid = notAvailableSlotDAO.getLastAppointmentId(accountsForm.getClientId());
				if(apptid>0){
					practid=notAvailableSlotDAO.getDrApptId(apptid);
				}
			}
			ArrayList<DiaryManagement> userList = notAvailableSlotDAO.getUserAccountList(loginInfo.getClinicid());
			accountsForm.setUserList(userList);
			accountsForm.setDoctorid(practid);
			accountsForm.setInvcetype(invoicetype);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			connection.close();
		}

		return "cashdesk";
	}

	public String createInvoice() throws Exception{
		if (!verifyLogin(request)) {
			return "login";
		}
		Connection connection = null;
		try {

			connection = Connection_provider.getconnection();
			AdditionalDAO additionalDAO = new JDBCAdditionalDAO(connection);
			String clientId = accountsForm.getClientId();
			String clientname = accountsForm.getClient();
			String type = accountsForm.getApmtType();
			String date = DateTimeUtils.getDateinSimpleFormate(new Date());
			String location = accountsForm.getLocation();
			String payby = accountsForm.getPayby();
			String stemp[] = date.split(" ");

			String temp[] = stemp[0].split("-");
			date = temp[2] + "-" + temp[1] + "-" + temp[0];
			
			date = DateTimeUtils.getCommencingDate1(accountsForm.getDate());
			
			

			String apmtid = "0";
			if (session.getAttribute("otsessionapmtid") != null) {
				apmtid = (String) session.getAttribute("otsessionapmtid");
			}

			int invoiceid = additionalDAO.saveInvoce(clientId, clientname,
					type, date, location, apmtid,loginInfo.getUserId());
			ArrayList<CompleteAppointment> assesmentList = additionalDAO
					.getCompleteApmtList(clientId,loginInfo.getId());
			for (CompleteAppointment appointment : assesmentList) {
				//Akash 27 Jan 2018 pass date1 instead of date
				String date1= appointment.getCommencing();
				int result = additionalDAO.saveAssessment(clientId, clientname,
						type, date1, invoiceid, appointment);
			}
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			int save = accountsDAO.saveTempChargeAccounts(invoiceid,loginInfo.getLoginsessionid());

			CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(
					connection);

			int result = completeAptmDAO.deleteComplteApmt(loginInfo.getId());
			accountsForm.setClientId(clientId);
			accountsForm.setLocation(location);
			accountsForm.setPayby(payby);

			session.setAttribute("clientId", clientId);
			session.setAttribute("location", location);
			session.setAttribute("payby", payby);

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			connection.close();
		}
		return "createInvoice";
	}

	public AccountsForm getModel() {

		return accountsForm;
	}

	public String showList() throws SQLException {
		if (!verifyLogin(request)) {
			return "login";
		}

		Connection connection = null;
		Client client = new Client();
		ArrayList<Client> allPatientList = new ArrayList<Client>();
		try {
			connection = Connection_provider.getconnection();

			ClientDAO clientDAO = new JDBCClientDAO(connection);
			allPatientList = clientDAO.getAllPatient(loginInfo.getId());

			// clientForm.setAllPatientList(allPatientList);

			StringBuffer str = new StringBuffer();

			str
					.append("<table class='table table-hover table-condensed table-bordered table-striped ' > ");
			str.append("<thead>");
			str.append("<tr class='bg-info'>");
			str.append("<th>UHID</th> ");

			str.append("<th>Name</th> ");
			str.append("<th>Old Client Id</th> ");
			str.append("<th>Mobile</th> ");
			str.append("<th>Email</th> ");
			str.append("<th>PostCode</th> ");
			str.append("<th>Dob</th> ");
			str.append("<th>LastModified</th> ");

			str.append("</tr>");
			str.append("</thead>");

			str.append("<tbody>");
			for (Client client1 : allPatientList) {
				String name = client1.getTitle() + " " + client1.getFirstName() 
						+ " "+client1.getMiddlename()+" " + client1.getLastName();
				str.append("<tr>");
				str.append("<td>" + client1.getAbrivationid()+ "</td>");

				String firstName = client1.getFirstName();
				str
						.append("<td style='cursor: pointer;'; onclick = setClientDetails('"
								+ firstName
								+ "','"
								+ client1.getId()
								+ "','"
								+ client1.getType()
								+ "','"
								+ client1.getTypeName()
								+ "')>"
								+ name
								+ "</td>");
				if (client1.getOldclientId() == null) {
					client1.setOldclientId("");
				}
				str.append("<td>" + client1.getOldclientId() + "</td>");
				str.append("<td>" + client1.getMobNo() + "</td>");
				str.append("<td>" + client1.getEmail() + "</td>");
				str.append("<td>" + client1.getPostCode() + "</td>");
				str.append("<td>" + client1.getDob() + "</td>");
				if (client1.getLastModified() == null) {
					client1.setLastModified("");
				}
				str.append("<td>" + client1.getLastModified() + "</td>");

				str.append("</tr>");
			}
			str.append("</tbody>");
			str.append("</table>");
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");

			response.getWriter().write("" + str.toString() + "");

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			connection.close();
		}

		return null;

	}

	public String searchParticularPatient() throws SQLException {
		if (!verifyLogin(request)) {
			return "login";
		}
		String searchClient = request.getParameter("searchText");
		Connection connection = null;
		ArrayList<Client> allPatientList = new ArrayList<Client>();
		try {
			connection = Connection_provider.getconnection();

			ClientDAO clientDAO = new JDBCClientDAO(connection);
			allPatientList = clientDAO.getClient(searchClient, loginInfo
					.getId());

			// clientForm.setAllPatientList(allPatientList);
			StringBuffer str = new StringBuffer();

			str
					.append("<table class = 'table table-hover table-condensed table-bordered table-striped ' > ");
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
			for (Client client1 : allPatientList) {
				str.append("<tr>");
				String firstName = client1.getFirstName();
				str.append("<td>0000" + client1.getId() + "</td>");

				String name = client1.getTitle() + " " + client1.getFirstName()
						+ " " + client1.getLastName();
				str
						.append("<td style='cursor: pointer;'; onclick = setClientDetails('"
								+ firstName
								+ "','"
								+ client1.getId()
								+ "','"
								+ client1.getType()
								+ "','"
								+ client1.getTypeName()
								+ "')>"
								+ name
								+ "</td>");
				if (client1.getOldclientId() == null) {
					client1.setOldclientId("");
				}
				str.append("<td>" + client1.getOldclientId() + "</td>");
				str.append("<td>" + client1.getMobNo() + "</td>");
				str.append("<td>" + client1.getEmail() + "</td>");
				str.append("<td>" + client1.getPostCode() + "</td>");
				str.append("<td>" + client1.getDob() + "</td>");

				str.append("</tr>");
			}
			str.append("<tbody>");

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");

			response.getWriter().write("" + str.toString() + "");

		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			connection.close();
		}

		return null;
	}

	public String getClientDetails() throws Exception {
		if (!verifyLogin(request)) {
			return "login";
		}

		Connection connection = null;

		String clientId = request.getParameter("id");
		try {
			connection = Connection_provider.getconnection();

			CompleteAppointment completeAppointment = new CompleteAppointment();
			CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(
					connection);
			completeAppointment = completeAptmDAO
					.getInsuranceCompanyName(clientId);

			String tpDetails = completeAppointment.getInsuranceCompanyName()
					+ " " + completeAppointment.getInsuranceCompanyAddress()
					+ " " + completeAppointment.getThirdPartyContacttno() + " "
					+ completeAppointment.getThirdPartyPostcode() + " "
					+ completeAppointment.getThirdPartyemail();

			Client client = new Client();
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			client = accountsDAO.getClientData(clientId);

			String clientDetails = "UHID : " + client.getAbrivationid() + " "
					+ client.getTitle() + " " + client.getFirstName() + " "
					+ client.getLastName() + " " + "," + client.getAddress()
					+ " " + client.getTown() + " " + client.getPostCode() + " "
					+ client.getEmail() + " " + client.getMobNo();

			ClientDAO clientDAO = new JDBCClientDAO(connection);
			String fullName = clientDAO.getClientFullName(clientId);

			String data = fullName + "~" + tpDetails + "~" + clientDetails;

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");

			response.getWriter().write("" + data + "");

		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			
			connection.close();
		}
		return null;
	}

	public String advance() throws Exception {
		Connection connection = null;
		try {

			String clientid = request.getParameter("clientid");

			connection = Connection_provider.getconnection();
			AdditionalDAO additionalDAO = new JDBCAdditionalDAO(connection);

			//double balance = additionalDAO.getAdvanceBalance(clientid);
			double balance = additionalDAO.getAdvanceBalanceRemain(clientid);
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");

			response.getWriter().write("" + balance + "");

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			connection.close();
		}

		return null;
	}

	public String getChargeAmount() throws Exception{
		if (!verifyLogin(request)) {
			return "login";
		}

		Connection connection = null;
		String type = request.getParameter("type");
		try {
			connection = Connection_provider.getconnection();
			AdditionalDAO additionalDAO = new JDBCAdditionalDAO(connection);
			String amount = additionalDAO.getAdditionalCharge(type);

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");

			response.getWriter().write("" + amount + "");
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			
			connection.close();
		}

		return null;
	}

	public String creditList() throws Exception {
		Connection connection = null;
		try {

			connection = Connection_provider.getconnection();

			AdditionalDAO additionalDAO = new JDBCAdditionalDAO(connection);
			ClientDAO clientDAO = new JDBCClientDAO(connection);

			String clientId = null;

			clientId = request.getParameter("clientId");

			if (clientId == null) {

				// set selected clientid from session
				if (session.getAttribute("sessionselectedclientid") != null) {
					clientId = (String) session
							.getAttribute("sessionselectedclientid");
					accountsForm.setClientId(clientId);
					Client clients = clientDAO
							.getSelectedSessionClientDetails(clientId);
					accountsForm.setClient(clients.getClientName());
					accountsForm.setClientId(clientId);

				}
			}

			clientId = accountsForm.getClientId();

			if (clientId != null) {

				if (clientId.equals("")) {
					clientId = null;
				}
			} else {
				clientId = request.getParameter("clientId");
			}

			if (clientId != null) {

				ArrayList<Accounts> creditList = additionalDAO
						.getCreditAccountList(clientId,"");
				Client clients = clientDAO
						.getSelectedSessionClientDetails(clientId);
				accountsForm.setClient(clients.getClientName());
				accountsForm.setClientId(clientId);
				accountsForm.setCreditList(creditList);

				double credit = 0;

				for (Accounts accounts : creditList) {
					credit = credit + Double.parseDouble(accounts.getCharges());
				}

				accountsForm
						.setCreditTotalx(DateTimeUtils.changeFormat(credit));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			connection.close();
		}

		return "creditlist";
	}

	public String creditsave() throws Exception {

		// var url =
		// "creditsaveAdditional?paymode="+paymode+"&clientId="+clientId+"&type="+type+"&payBuy="+payBuy+"&charge="+remain+"
		// ";
		String paymode = request.getParameter("paymode");
		String clientId = request.getParameter("clientId");
		String type = request.getParameter("type");
		String payBuy = request.getParameter("payBuy");
		String charge = request.getParameter("charge");
		String creditnote = request.getParameter("creditnote");

		String date = DateTimeUtils.getUKCurrentDataTime(loginInfo
				.getTimeZone());

		Connection connection = null;
		try {

			connection = Connection_provider.getconnection();
			AdditionalDAO additionalDAO = new JDBCAdditionalDAO(connection);

			double balance = additionalDAO.getCreditTotal(clientId);
			balance = balance + Double.parseDouble(charge);

			// reset invoice
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			int resetinv = accountsDAO.getMaxResetInv();
			int resetcreditinv = accountsDAO.getMaxResetCreditInv();
			int rinv = 0;
			if (resetinv > resetcreditinv) {
				rinv = resetinv + 1;
			} else {
				rinv = resetcreditinv + 1;
			}
			int invoiceid = additionalDAO.saveCreditRecord(clientId, type,
					date, creditnote, payBuy, charge, paymode, balance, rinv,loginInfo.getUserId(),0);

			int save = additionalDAO.saveCreditAssessmentRecord(clientId, "",
					type, date, invoiceid, charge, accountsForm.getAdvref());
			additionalDAO.createSeqnogenProccessForAdvAndRef(accountsForm.getInvcetype(), ""+invoiceid, paymode,"");

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			connection.close();
		}

		System.out.println("hello");

		return null;
	}

	public void prepare() throws Exception {
		Connection connection = null;
		try {

			connection = Connection_provider.getconnection();

			AdditionalDAO additionalDAO = new JDBCAdditionalDAO(connection);
			AppointmentDAO appointmentDAO = new JDBCAppointmentDAO(connection);
			MasterDAO masterDAO = new JDBCMasterDAO(connection);
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			ArrayList<AppointmentType> additionalChargesList = new ArrayList<AppointmentType>();
			//additionalChargesList = additionalDAO.getAdditionalChargesList();
			accountsForm.setAdditionalChargesList(additionalChargesList);

			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			ArrayList<Accounts> locationList = accountsDAO
					.getLocationList(loginInfo.getClinicid());
			accountsForm.setLocationList(locationList);
			String regLocationID = emrDAO.getRegisteredLocationId();
			accountsForm.setLocation(regLocationID);

			ArrayList<Master> masterChageTypeList = appointmentDAO
					.getmasterChageTypeList(loginInfo);
			accountsForm.setMasterChageTypeList(masterChageTypeList);
			accountsForm.setMasterchargetype("Additional Charge");

			ArrayList<Master> invoiceTypeList = accountsDAO
					.getInvoiceTypeList();
			accountsForm.setInvoiceTypeLis(invoiceTypeList);
			
			
			 ArrayList<Master>bankNameList = masterDAO.getBankNameList();
			 accountsForm.setBankNameList(bankNameList);
			 accountsForm.setHourList(PopulateList.hourList());
			accountsForm.setMinuteList(PopulateList.getMinuteList());

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			connection.close();
		}

	}
	
	public String checkinvoiceidofclient() throws Exception{
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			AdditionalDAO additionalDAO = new JDBCAdditionalDAO(connection);
			String invoiceid = request.getParameter("newinvoiceid");
			String clientid = request.getParameter("clientid");
			
			int res = additionalDAO.checkInvoiceIdofClient(invoiceid,clientid);
			String amount ="0";
			if(res==1){
				 amount = additionalDAO.getDebitFromInvoiceId(invoiceid);
				 boolean flag = additionalDAO.checkPendingRefund(invoiceid);
				 if(flag){
					 res =2;
				 }
			}
			
			String data = res +"~"+ amount;
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+data+"");
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			connection.close();
		}
		return null;
	}
	
	public String refundrequest() throws Exception{
		if (!verifyLogin(request)) {
			return "login";
		}
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			AdditionalDAO additionalDAO = new JDBCAdditionalDAO(connection);
			String clientId = accountsForm.getClientId();
			String clientname = accountsForm.getClient();
			String type = accountsForm.getApmtType();
			String date = accountsForm.getDate();
			String location = accountsForm.getLocation();
			String creditDebitCharge = accountsForm.getCreditDebitCharge();
			String manualinvoiceid = accountsForm.getManualinvoiceid();
			String refundnote = accountsForm.getRefundnote();
			session.setAttribute("manualinvoiceid", manualinvoiceid);
			session.setAttribute("refundnote", refundnote);
			if(date==null){
				date = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
			} else {
				if(date.equals("")){
					date = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
				} else {
					date = DateTimeUtils.getCommencingDate1(date);
				}
			}
			
			date = DateTimeUtils.getCommencingDate1(accountsForm.getDate());

			if (!accountsForm.getClient().equals("")) {
				String ctemp[] = clientname.split(" ");
				accountsForm.setInitial(ctemp[0]);
				accountsForm.setFirstname(ctemp[1]);
				accountsForm.setLastname(ctemp[2]);
			}
			String requestedate = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
			int parentid = additionalDAO.saveRequestRefund(clientId,clientname,type,date,location,creditDebitCharge,manualinvoiceid,refundnote,loginInfo.getUserId(),requestedate);
			int result = additionalDAO.saveRequestRefundChild(parentid,clientId,loginInfo.getId());
			
			String ids = additionalDAO.getRequestedRefundDeleteIDs(clientId,loginInfo.getId());
			
			if(ids!=null){
				int reess = additionalDAO.updateRefundParentIds(ids,parentid);
				for (String string : ids.split(",")) {
					if (string.equals("0")) {
						continue;
					}
					int rres = additionalDAO.updateRefundRequestDeleteInvoice(string);
				}
			}
			
			ArrayList<String>  arrayList = additionalDAO.getRefundRequestId(String.valueOf(parentid));
			for (String string : arrayList) {
				int rres = additionalDAO.updateRefundRequestDeleteInvoice(string);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			connection.close();
		}

		return "refundactiondash";
	}
	
	public String refundrequestdashboard() throws Exception {
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			AdditionalDAO additionalDAO = new JDBCAdditionalDAO(connection);
			String filter_status =(String)session.getAttribute("refund_dashbord_filter_status");
			String fromdate = (String) session.getAttribute("refund_dashbord_fromdate");
			String todate = (String) session.getAttribute("refund_dashbord_todate");
			String searchtext = (String) session.getAttribute("refund_dashbord_searchtext");
			String countdata = (String) session.getAttribute("refund_dashbord_countdata");
			
			if(fromdate==null){
				fromdate = accountsForm.getFromdate();
			}else{
				session.removeAttribute("refund_dashbord_fromdate");
			}
			if(todate==null){
				todate = accountsForm.getTodate();
			}else{
				session.removeAttribute("refund_dashbord_todate");
			}
			if(searchtext==null){
				searchtext = accountsForm.getSearchtext();
			}else{
				session.removeAttribute("refund_dashbord_searchtext");
			}
			if(filter_status==null){
				filter_status = accountsForm.getFilter_status();
			}else{
				session.removeAttribute("refund_dashbord_filter_status");
			}
			
			if(countdata==null){
				countdata = request.getParameter("countdata");
			}else{
				session.removeAttribute("refund_dashbord_countdata");
			}
			
			if(filter_status==null){
				filter_status ="";
			}
			if (fromdate == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar cal = Calendar.getInstance();
				fromdate = dateFormat.format(cal.getTime());
				fromdate = DateTimeUtils.getCommencingDate1(fromdate);
			} else {
				if (fromdate.equals("")) {
					DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					Calendar cal = Calendar.getInstance();
					fromdate = dateFormat.format(cal.getTime());
					fromdate = DateTimeUtils.getCommencingDate1(fromdate);
					// fromdate = null;
				} else {
					fromdate = DateTimeUtils.getCommencingDate1(fromdate);
				}
			}

			if (todate == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar cal = Calendar.getInstance();
				todate = dateFormat.format(cal.getTime());
				todate = DateTimeUtils.getCommencingDate1(todate);
			} else {
				if (todate.equals("")) {
					DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					Calendar cal = Calendar.getInstance();
					todate = dateFormat.format(cal.getTime());
					todate = DateTimeUtils.getCommencingDate1(todate);
					// todate = null;
				} else {
					todate = DateTimeUtils.getCommencingDate1(todate);
				}

			}
			/*String searchtext1 = request.getParameter("searchtext1");
			if(searchtext1!=null){
				if(searchtext1.equals("")){
					searchtext1 =null;
				}
			}
			if(searchtext1!=null){
				if(searchtext!=null){
					if(searchtext.equals("")){
						searchtext =null;
					}
				}
			}else{
				searchtext = searchtext1;
			}*/
			if(searchtext!=null){
				if(searchtext.equals("")){
					searchtext =null;
				}
			}
			boolean accessofapprove= false;
			if(loginInfo.isRefund_dashboard() || loginInfo.getUserType()==2 || loginInfo.isRef_dis_pay()){
				accessofapprove =true;
			}
			
			
			if(countdata==null){
				countdata = "";
			}
			ArrayList<CompleteAppointment> parentrefundrequestlist = additionalDAO.getRefundRequestList(fromdate,todate,"",searchtext,accessofapprove,loginInfo.getId(),filter_status,loginInfo.getUserId(),countdata); 
			
			int approvecount = additionalDAO.getRefundApproveAppliedCount(fromdate,todate,"",searchtext,accessofapprove,loginInfo.getId(),filter_status,loginInfo.getUserId(),"0");
			int nonpaiedcount = additionalDAO.getRefundApproveAppliedCount(fromdate,todate,"",searchtext,accessofapprove,loginInfo.getId(),filter_status,loginInfo.getUserId(),"1");
			
			accountsForm.setParentrefundrequestlist(parentrefundrequestlist);
			accountsForm.setFromdate(DateTimeUtils.getCommencingDate1(fromdate));
			accountsForm.setTodate(DateTimeUtils.getCommencingDate1(todate));
			accountsForm.setFilter_status(filter_status);
			accountsForm.setSearchtext(searchtext);
			accountsForm.setNon_approve_count(approvecount);
			accountsForm.setNon_applied_count(nonpaiedcount);
			accountsForm.setCountdata(countdata);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return "refundrequestdashboard";
	}
	
	public String deleterefundrequest() throws Exception {
		
		Connection connection=null;
		try {
			connection=Connection_provider.getconnection();
			AdditionalDAO additionalDAO = new JDBCAdditionalDAO(connection);
			String parentid= request.getParameter("parentid");
			String delete_reason= request.getParameter("delete_reason");
			String userid = loginInfo.getUserId();
			String date = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
			int del=additionalDAO.cancelRefundrequest(parentid,userid,date,delete_reason);
			
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			connection.close();
		}
		
		return null;
	}
	
	/*public String aproverefund() throws Exception{
		Connection  connection = null;
		try {
			connection = Connection_provider.getconnection();
			AdditionalDAO additionalDAO = new JDBCAdditionalDAO(connection);
			String id =request.getParameter("id");
			String approve_reason= request.getParameter("approve_reason");
			String userid = loginInfo.getUserId();
			String date = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
			int del=additionalDAO.approveRefundrequest(id,userid,date,approve_reason);
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			connection.close();
		}
		return null;
	}*/
	
	public String aproverefund() throws Exception {
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			AdditionalDAO additionalDAO = new JDBCAdditionalDAO(connection);
			StringBuilder buffer = new StringBuilder();
			BufferedReader reader = request.getReader();
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			String data = buffer.toString();
			JSONObject jsonObject = new JSONObject(data);
			
			String id = jsonObject.getString("id");
			String approve_reason = jsonObject.getString("approve_reason");
			
			String searchtext = jsonObject.getString("searchtext");
			String fromdate = jsonObject.getString("fromdate");
			String todate = jsonObject.getString("todate");
			String filter_status = jsonObject.getString("filter_status");
			String countdata = jsonObject.getString("countdata");
			String userid = loginInfo.getUserId();
			String date = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
			int del=additionalDAO.approveRefundrequest(id,userid,date,approve_reason);
			
			session.setAttribute("refund_dashbord_searchtext", searchtext);
			session.setAttribute("refund_dashbord_fromdate", fromdate);
			session.setAttribute("refund_dashbord_todate", todate);
			session.setAttribute("refund_dashbord_filter_status", filter_status);
			session.setAttribute("refund_dashbord_countdata", countdata);
			JSONObject jsonobj = new JSONObject();
			String response1 = jsonobj.toString();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(response1);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return null;
	}
	
	public String refundinvoice() throws Exception{
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(
					connection);

			ClientDAO clientDAO = new JDBCClientDAO(connection);
			AdditionalDAO additionalDAO = new JDBCAdditionalDAO(connection);
			// set selected clientid from session
			String clientId = request.getParameter("clientId");
			
			
			if(clientId!=null){
				if(clientId.equals("")){
					clientId=null;
				}
			}
			
			if(clientId==null){
				clientId=request.getParameter("clientid");
			}
			if (clientId == null) {
				clientId = (String) session
						.getAttribute("sessionselectedclientid");
				accountsForm.setClientId(clientId);
				Client clients = clientDAO
						.getSelectedSessionClientDetails(clientId);
				accountsForm.setClient(clients.getClientName());

			} else {

				accountsForm.setClientId(clientId);
				Client clients = clientDAO
						.getSelectedSessionClientDetails(clientId);
				accountsForm.setClient(clients.getClientName());
			}

			int result = completeAptmDAO.deleteComplteApmt(loginInfo.getId());
			session.removeAttribute("sessionadmissionid");
			String date= DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
			accountsForm.setDate(DateTimeUtils.getCommencingDate1(date));

			//boolean checkinvoice = completeAptmDAO.checkInvoiceBalanace(clientId);
			boolean checkinvoice = true;
			accountsForm.setCheckinvoice(String.valueOf(checkinvoice));
			accountsForm.setApprovedrefund("1");
			
			String id = request.getParameter("id");
			CompleteAppointment completeAppointment = additionalDAO.getRefundRequestData(id);
			int res = additionalDAO.saveRefundTemp(id,loginInfo.getId());
			ArrayList<CompleteAppointment> clientChargeListDetail = new ArrayList<CompleteAppointment>();
			clientChargeListDetail = completeAptmDAO.getPatientChrageDetails(clientId,date,completeAppointment.getAppointmentid(),loginInfo.getId());
			
			accountsForm.setClientChargeListDetail(clientChargeListDetail);
			
			int size = clientChargeListDetail.size();
			if (size > 0) {
				String total = clientChargeListDetail.get(size - 1).getChargeTotalx();
				accountsForm.setChargeTotalx(total);
			} else {
				accountsForm.setChargeTotalx("0");
			}
			
			accountsForm.setManualinvoiceid(completeAppointment.getManualinvoiceid());
			accountsForm.setRefundnote(completeAppointment.getRefundnote());
			accountsForm.setRefundrequestid(id);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			
			connection.close();
		}

		return "creditcharge";
	}
	
	public String openclientrequestedrefund() throws Exception{
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			AdditionalDAO additionalDAO = new JDBCAdditionalDAO(connection);
			String clientid = request.getParameter("clientId");
			ArrayList<CompleteAppointment> parentrefundrequestlist = additionalDAO.getClientRefundRequestList(clientid); 
			accountsForm.setParentrefundrequestlist(parentrefundrequestlist);
			accountsForm.setClientId(clientid);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return "clientrefundrequest";
	}
	
	public String getrefundinvoicedata() throws Exception{
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(
					connection);
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			AdditionalDAO additionalDAO = new JDBCAdditionalDAO(connection);
			// set selected clientid from session
			String clientId = request.getParameter("clientId");
			
			
			if(clientId!=null){
				if(clientId.equals("")){
					clientId=null;
				}
			}
			
			if(clientId==null){
				clientId=request.getParameter("clientid");
			}
			if (clientId == null) {
				clientId = (String) session
						.getAttribute("sessionselectedclientid");
				accountsForm.setClientId(clientId);
				Client clients = clientDAO
						.getSelectedSessionClientDetails(clientId);
				accountsForm.setClient(clients.getClientName());

			} else {

				accountsForm.setClientId(clientId);
				Client clients = clientDAO
						.getSelectedSessionClientDetails(clientId);
				accountsForm.setClient(clients.getClientName());
			}

			int result = completeAptmDAO.deleteComplteApmt(loginInfo.getId());
			session.removeAttribute("sessionadmissionid");
			String date= DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
			accountsForm.setDate(DateTimeUtils.getCommencingDate1(date));

			
			String id = request.getParameter("newinvoiceid");
			//int res = additionalDAO.saveRefundTempFromDeleteInvoice(id,loginInfo.getId());
			//Akash 28-06-2019
			//check not any charges available
			boolean flag = additionalDAO.checkRefundRequest(id); 
			if(flag){
				/*boolean statusrequestdiscamt = accountsDAO.getRequestedDiscountStatus(Integer.parseInt(id));
				double discountamt = 0;
				if(statusrequestdiscamt){
					discountamt = accountsDAO.getRequestedDiscountAmount(Integer.parseInt(id));
				}*/
				double balanceamt = accountsDAO.getInvoiceBalanceAmount(id);
				if(balanceamt<0){
					//double inrequestbalance = accountsDAO.getInRequestRefundBalance(id);
					double newbalane = Math.abs(balanceamt);
					String deletechargeid = additionalDAO.getDeletedChargeIDforRefund(id);
					int res = additionalDAO.saveRefundTempFromDeleteInvoiceNew(id,loginInfo.getId(),deletechargeid,newbalane);
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
	
	public String changepaymodeadv(){
		String id= request.getParameter("id");
		String paymode= request.getParameter("paymode");
		Connection connection= null;
		if(paymode==null){
			paymode="0";
		}
		if(paymode.equals("0")||paymode.equals("")||id.equals("")){
			return null;
		}
		try {
			connection= Connection_provider.getconnection();
			AdditionalDAO additionalDAO= new JDBCAdditionalDAO(connection);
			additionalDAO.updateAdvRefPaymodeCreditAcc(id, paymode);
			additionalDAO.updateAdvrefLedger(id, paymode);
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""); 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}