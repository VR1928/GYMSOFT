package com.apm.Report.web.action;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.allcolor.yahp.converter.CYaHPConverter;
import org.allcolor.yahp.converter.IHtmlToPdfTransformer;
import org.apache.struts2.ServletActionContext;

import com.apm.Accounts.eu.bi.AccountsDAO;
import com.apm.Accounts.eu.blogic.jdbc.JDBCAccountsDAO;
import com.apm.Accounts.eu.entity.Accounts;
import com.apm.Accounts.web.form.AccountsForm;
import com.apm.Appointment.eu.bi.AppointmentTypeDAO;
import com.apm.Appointment.eu.blogic.jdbc.JDBCAppointmentTypeDAO;
import com.apm.Appointment.eu.entity.AppointmentType;
import com.apm.DiaryManagement.eu.bi.NotAvailableSlotDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCNotAvailableSlotDAO;
import com.apm.DiaryManagement.eu.entity.Client;
import com.apm.DiaryManagement.eu.entity.DiaryManagement;
import com.apm.DiaryManagement.eu.entity.NotAvailableSlot;
import com.apm.DiaryManagement.web.action.EmbeddedImageEmailUtil;
import com.apm.Emr.eu.bi.ConsultationNoteDAO;
import com.apm.Emr.eu.blogic.jdbc.JDBCConsultationNoteDAO;
import com.apm.Log.eu.entity.EmailLetterLog;
import com.apm.Master.eu.entity.Master;
import com.apm.Registration.eu.bi.ClinicDAO;
import com.apm.Registration.eu.bi.UserProfileDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCClinicDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCUserProfileDAO;
import com.apm.Registration.eu.entity.Clinic;
import com.apm.Registration.eu.entity.UserProfile;
import com.apm.Report.eu.bi.ChargesReportDAO;
import com.apm.Report.eu.bi.SummaryReportDAO;
import com.apm.Report.eu.blogic.jdbc.JDBCChargesReportDAO;
import com.apm.Report.eu.blogic.jdbc.JDBCSummaryReportDAO;
import com.apm.Report.web.form.ChargesReportForm;
import com.apm.ThirdParties.eu.bi.ThirdPartyDAO;
import com.apm.ThirdParties.eu.blogic.jdbc.JDBCThirdPartyDAO;
import com.apm.ThirdParties.eu.entity.ThirdParty;
import com.apm.common.eu.blogic.jdbc.Connection_provider;
import com.apm.common.utils.DateTimeUtils;
import com.apm.common.web.action.BaseAction;
import com.apm.common.web.common.helper.LoginHelper;
import com.apm.common.web.common.helper.LoginInfo;
import com.apm.main.common.constants.Constants;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class ChargesReportAction extends BaseAction implements Preparable, ModelDriven<ChargesReportForm> {
	ChargesReportForm chargesReportForm = new ChargesReportForm();
	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
	HttpServletResponse response = (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
	HttpSession session = request.getSession(true);
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	public String execute() throws Exception{
		Connection connection = null;
		if(!verifyLogin(request)){
			return "login";
		}
		String fromDate = chargesReportForm.getFromDate();
		String toDate = chargesReportForm.getToDate();	
		if(chargesReportForm.getOrderby().equals("")){
			chargesReportForm.setOrderby("commencing");
		}
		
		if(fromDate.equals("")){
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -7); 
			fromDate = dateFormat.format(cal.getTime());
			chargesReportForm.setFromDate(fromDate);
		}
		if(toDate.equals("")){
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.DATE, -7); 
			toDate = dateFormat.format(cal.getTime());
			chargesReportForm.setToDate(toDate);
		}
		
		
		if(!fromDate.equals("")){
			String temp[]= fromDate.split("/");
			fromDate = temp[2]+"-"+temp[1]+"-"+temp[0];
		}
		if(!toDate.equals("")){
			String temp1[]= toDate.split("/");
			toDate = temp1[2]+"-"+temp1[1]+"-"+temp1[0];
		}
		try{
		/*Connection connection = null;*/
		connection = Connection_provider.getconnection();
		ChargesReportDAO chargesReportDAO = new JDBCChargesReportDAO(connection);
	// Adarsh
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	      Calendar cal = Calendar.getInstance();
	      String date = dateFormat.format(cal.getTime());  
		
		int result = chargesReportDAO.saveMisReportLog("Charges Report",loginInfo.getUserId(),fromDate,toDate,date,"execute");
		ArrayList<Accounts> list = new ArrayList<Accounts>();
		
		list = chargesReportDAO.getChargesReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getThirdParty(),chargesReportForm.getInvoiceStatus(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getCondition());
		
		chargesReportForm.setList(list);
	
		double debittotal = 0.0;
		double credit = 0.0;
		for(Accounts accounts :list ){
			debittotal = debittotal + accounts.getDebitTotal();
			/*chargesReportForm.setBalanceTotal(accounts.getBalanceTotal());
			chargesReportForm.setDebitTotal(accounts.getDebitTotal());
			chargesReportForm.setCreditTotal(accounts.getCreditTotal());*/
			
			chargesReportForm.setBalanceTotalx(DateTimeUtils.changeFormat(debittotal));
			chargesReportForm.setDebitTotalx(DateTimeUtils.changeFormat(debittotal));
			chargesReportForm.setCreditTotalx(DateTimeUtils.changeFormat(credit));
		
		}
		
		session.setAttribute("chargereportlist", list);
		session.setAttribute("chargedebittotal", chargesReportForm.getDebitTotalx());
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			connection.close();
		}
		return "chargeReportSucess";
	}
	
	public String district(){
		
		Connection connection = null;
		if(!verifyLogin(request)){
			return "login";
		}
		String fromDate = chargesReportForm.getFromDate();
		String toDate = chargesReportForm.getToDate();	
		if(chargesReportForm.getOrderby().equals("")){
			chargesReportForm.setOrderby("commencing");
		}
		
		if(fromDate.equals("")){
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -7); 
			fromDate = dateFormat.format(cal.getTime());
			chargesReportForm.setFromDate(fromDate);
		}
		if(toDate.equals("")){
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.DATE, -7); 
			toDate = dateFormat.format(cal.getTime());
			chargesReportForm.setToDate(toDate);
		}
		
		
		if(!fromDate.equals("")){
			String temp[]= fromDate.split("/");
			fromDate = temp[2]+"-"+temp[1]+"-"+temp[0];
		}
		if(!toDate.equals("")){
			String temp1[]= toDate.split("/");
			toDate = temp1[2]+"-"+temp1[1]+"-"+temp1[0];
		}
		try{
		/*Connection connection = null;*/
		connection = Connection_provider.getconnection();
		ChargesReportDAO chargesReportDAO = new JDBCChargesReportDAO(connection);

		ArrayList<NotAvailableSlot>distlist = chargesReportDAO.getDistlevelopdcount(fromDate,toDate);
		chargesReportForm.setDistlist(distlist);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return "district";
	}
	
	public String saveAsPdfChargeReport() throws Exception{
		Connection connection = null;
		try{
		
		connection = Connection_provider.getconnection();
		ChargesReportDAO chargesReportDAO = new JDBCChargesReportDAO(connection);
		
		
	
		String currentDate = DateTimeUtils.getDateinSimpleFormate(new Date());
		String temp[] = currentDate.split(" ");
		String temptime[] = currentDate.split("at");
		String temp1[] = temp[0].split("-");
		String date = temp1[0] + "/" + temp1[1] + "/" + temp1[2];
		String time = temptime[1];
		StringBuffer str = new StringBuffer();
		str.append("<div style='font-size: 20px; font-weight: bold;'> Charges Report </div>");
		str.append("<div style='font-size: 10px; font-weight: bold;'> Date: "+date+" </div>");
		str.append("<div style='font-size: 10px; font-weight: bold;'> Time: "+time+" </div>");
		str.append("<br>");
		str.append("<table cellpadding='0' cellspacing='0' style='width: 100%;font-size: 10px;'>");
		str.append("<tr>");
		str.append("<th>Date</th>");
		str.append("<th>Transaction</th>");
		str.append("<th>Client</th>");
		str.append("<th>Status</th>");
		str.append("<th>Payee</th>");
		str.append("<th>Location</th>");
		str.append("<th>Description</th>");
		str.append("<th>Debit</th>");
		str.append("<th>Credit</th>");
		str.append("<th>Balance</th>");
		str.append("</tr>");
		
		ArrayList<Accounts>list = (ArrayList<Accounts>)session.getAttribute("chargereportlist");
		String debittotal = (String)session.getAttribute("chargedebittotal");
		
		for(Accounts accounts : list){
			
			str.append("<tr>");
			str.append("<td>"+accounts.getCommencing()+"</td>");
			str.append("<td>Charge (0000"+accounts.getInvoiceid()+")</td>");
			str.append("<td>"+accounts.getClientName()+"</td>");
			if(accounts.isChargesInvoiced()){
				str.append("<td>Invoiced</td>");
			}else{
				str.append("<td>Not Invoiced</td>");
			}
			if(accounts.getWhoPay().equals("Self")){
				str.append("<td>"+accounts.getWhoPay()+" ("+accounts.getClientName()+")</td>");
			}else{
				str.append("<td>"+accounts.getWhoPay()+" ("+accounts.getTpName()+")</td>");
			}
			
			str.append("<td>"+accounts.getLocationName()+"</td>");
			str.append("<td>"+accounts.getAppointmentType()+"<br>");
			str.append(" Treatment:"+accounts.getTreatmentEpisodeName()+"<br>");
			str.append(" Practitioner:"+accounts.getPractitionerName()+"<br>");
			str.append("</td>");
			str.append("<td>"+Constants.getCurrency(loginInfo)+ accounts.getDebitTotalx()+"</td>");
			str.append("<td>"+Constants.getCurrency(loginInfo)+ accounts.getCreditTotalx()+"</td>");
			str.append("<td class = 'text-danger'>"+Constants.getCurrency(loginInfo)+ accounts.getBalanceTotalx()+"</td>");
			str.append("</tr>");
			
			
		
			
		}
		double balanceTotal = 0,creditTotal = 0;;
	
		str.append("<tr>");
		
		str.append("<td colspan='6'>");
		str.append("<td style='text-align: center;'><b>Total</b></td>");
		str.append("<td>"+Constants.getCurrency(loginInfo)+ debittotal+"</td>");
		str.append("<td>"+Constants.getCurrency(loginInfo)+ DateTimeUtils.changeFormat(creditTotal)+"</td>");
		str.append("<td class = 'text-danger'>"+Constants.getCurrency(loginInfo)+ debittotal+"</td>");
		str.append("</tr>");
			str.append("</table>");
			String filePath = request.getRealPath("//liveData//chargesReport//");
			String filename = "ChargeReport.pdf";

			htmlToPdfFile(str.toString(), filePath, filename);
			
			session.setAttribute("chargesReportFileName", filePath+"/"+filename);
			System.out.println("pdf done");
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+filename+"");	
		

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			connection.close();
		}
		
		return null;
		
	}
	public String sendMailChargeReport() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{
			
		connection = Connection_provider.getconnection();
		
		String to = request.getParameter("to");
		String cc = request.getParameter("cc");
		String subject = request.getParameter("subject");
		String notes = request.getParameter("emailBody");
		String file = request.getParameter("file");	
		String filename = (String)session.getAttribute("chargesReportFileName");		
		
		//int result = emailTemplateDAO.saveEmailLogDetails(to, cc, subject, notes, date1,time);
		
		String type = "Email";
		int appointmentid = 0;
		int invoiceid = 0;
		EmailLetterLog emailLetterLog = new EmailLetterLog();
		emailLetterLog.setAppointmentid(appointmentid);
		emailLetterLog.setInvoiceid(invoiceid);
		emailLetterLog.setType(type);
		
		EmbeddedImageEmailUtil.sendMailAttachment(connection,loginInfo.getId(),to, cc, subject, notes,filename,loginInfo,emailLetterLog,"0");
		
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			connection.close();
		}
		return null;
	}
	public String saveAsPdfInvoiceReport() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		if(!fromDate.equals("")){
			String temp[]= fromDate.split("/");
			fromDate = temp[2]+"-"+temp[1]+"-"+temp[0];
		}
		if(!toDate.equals("")){
			String temp1[]= toDate.split("/");
			toDate = temp1[2]+"-"+temp1[1]+"-"+temp1[0];
		}
		Connection connection = null;
		try{
			
			
			
			
			connection = Connection_provider.getconnection();
			ChargesReportDAO chargesReportDAO = new JDBCChargesReportDAO(connection);
			ArrayList<Accounts> invoiceList = new ArrayList<Accounts>();
			invoiceList = (ArrayList<Accounts>)session.getAttribute("invoicereportlist");
			chargesReportForm.setInvoiceList(invoiceList);
			
			String currentDate = DateTimeUtils.getDateinSimpleFormate(new Date());
			String temp[] = currentDate.split(" ");
			String temptime[] = currentDate.split("at");
			String temp1[] = temp[0].split("-");
			String date = temp1[0] + "/" + temp1[1] + "/" + temp1[2];
			String time = temptime[1];
			StringBuffer str = new StringBuffer();
			str.append("<div style='font-size: 20px; font-weight: bold;'> Invoice Report </div>");
			str.append("<div style='font-size: 10px; font-weight: bold;'> Date: "+date+" </div>");
			str.append("<div style='font-size: 10px; font-weight: bold;'> Time: "+time+" </div>");
			str.append("<br>");
			
			
			
			String invpayby = (String)session.getAttribute("invpayby");
			String invthirdparty = (String)session.getAttribute("invthirdparty");
			String invpaymentstatus = (String)session.getAttribute("invpaymentstatus");
			String invfromdate = (String)session.getAttribute("invfromdate");
			String invtodate = (String)session.getAttribute("invtodate");
			
			if(invpayby.equals("0")){
				invpayby = "All";
			}
			if(invpaymentstatus.equals("0")){
				invpaymentstatus = "All";
			}
			if(invthirdparty==null){
				invthirdparty = "All";
			}
			
			double dtotal = 0;
			double ctotal = 0;
			double btotal = 0;
			
			for(Accounts accounts : invoiceList){
				dtotal = dtotal + accounts.getDebitAmount();
				ctotal = ctotal + accounts.getCreditAmount();
				btotal = btotal + accounts.getBalance();
			}
			
			str.append("<table align='right' cellpadding='0' cellspacing='0' style='width: 70%;font-size: 10px;'>");
			str.append("<tr>");
			str.append("<th>Payed By</th>");
			str.append("<th>Show TP</th>");
			str.append("<th>Show Payment</th>");
			str.append("<th>From Date</th>");
			str.append("<th>To Date</th>");
			str.append("<th>Debit</th>");
			str.append("<th>Credit</th>");
			str.append("<th>Balance</th>");
			str.append("</tr>");
			str.append("<tr>");
			str.append("<td>"+invpayby+"</td>");
			str.append("<td>"+invthirdparty+"</td>");
			str.append("<td>"+invpaymentstatus+"</td>");
			str.append("<td>"+invfromdate+"</td>");
			str.append("<td>"+invtodate+"</td>");
			str.append("<td style='font-weight: bold;'>"+Constants.getCurrency(loginInfo)+DateTimeUtils.changeFormat(dtotal)+"</td>");
			str.append("<td style='font-weight: bold;'>"+Constants.getCurrency(loginInfo)+DateTimeUtils.changeFormat(ctotal)+"</td>");
			str.append("<td style='font-weight: bold;'>"+Constants.getCurrency(loginInfo)+DateTimeUtils.changeFormat(btotal)+"</td>");
			str.append("</tr>");
			
			str.append("</table>");
			
			str.append("<br>");
			
			str.append("<table cellpadding='0' cellspacing='0' style='width: 100%;font-size: 10px;'>");
			str.append("<tr><td><hr><td></tr>");
			str.append("</table>");
			
			str.append("<br>");
			str.append("<table cellpadding='0' cellspacing='0' style='width: 100%;font-size: 10px;'>");
			str.append("<tr>");
			str.append("<th>Invoice Date</th>");
			str.append("<th>Invoice</th>");
			str.append("<th>Client Name</th>");
			str.append("<th>Payee</th>");
			str.append("<th>Status</th>");
			str.append("<th>Debit</th>");
			str.append("<th>Credit</th>");
			str.append("<th>Discount</th>");
			str.append("<th>Balance</th>");
		
			str.append("</tr>");
			
			
			
			for(Accounts accounts : invoiceList){
				
				str.append("<tr>");
				str.append("<td>"+accounts.getDate()+"</td>");
				
				str.append("<td>0000"+accounts.getId()+"</td>");
				str.append("<td>"+accounts.getClientName()+"</td>");
				str.append("<td>"+accounts.getPayby()+"("+accounts.getPayeeName()+")</td>");
				
				if(accounts.getBalance()==0){
					str.append("<td>Paid</td>");
				}
				else{
					str.append("<td>Not Paid</td>");
				}
				
				str.append("<td>"+Constants.getCurrency(loginInfo)+accounts.getDebitAmountx()+"</td>");
				str.append("<td>"+Constants.getCurrency(loginInfo)+accounts.getCreditTotalx()+"</td>");
				str.append("<td>("+Constants.getCurrency(loginInfo)+accounts.getDiscAmmount()+")"+accounts.getDiscount()+"%</td>");
				str.append("<td>"+Constants.getCurrency(loginInfo)+accounts.getBalancex()+"<td>");
				
				str.append("</tr>");
				
			}
				str.append("</table>");
				String filePath = request.getRealPath("//liveData//chargesReport//");
				String filename = "InvoiceReport.pdf";

				htmlToPdfFile(str.toString(), filePath, filename);
				
				
				session.setAttribute("invoiceReportFileName", filePath+"/"+filename);
				System.out.println("pdf done");
				response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write(""+filename+"");
			
			
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				connection.close();
			}
		return null;
		
	}
	public String sendMailInvoiceReport() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{
			
		connection = Connection_provider.getconnection();
		
		String to = request.getParameter("to");
		String cc = request.getParameter("cc");
		String subject = request.getParameter("subject");
		String notes = request.getParameter("emailBody");
		String file = request.getParameter("file");	
		String filename = (String)session.getAttribute("invoiceReportFileName");		
		
		//int result = emailTemplateDAO.saveEmailLogDetails(to, cc, subject, notes, date1,time);
		
		String type = "Email";
		int appointmentid = 0;
		int invoiceid = 0;
		EmailLetterLog emailLetterLog = new EmailLetterLog();
		emailLetterLog.setAppointmentid(appointmentid);
		emailLetterLog.setInvoiceid(invoiceid);
		emailLetterLog.setType(type);
		
		EmbeddedImageEmailUtil.sendMailAttachment(connection,loginInfo.getId(),to, cc, subject, notes,filename,loginInfo,emailLetterLog,"0");
		
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			connection.close();
		}
		return null;
	}
	public String saveAsPdfPaymentReport() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		String fromDate = chargesReportForm.getFromDate();
		String toDate = chargesReportForm.getToDate();
		if(!fromDate.equals("")){
			String temp[]= fromDate.split("/");
			fromDate = temp[2]+"-"+temp[1]+"-"+temp[0];
		}
		if(!toDate.equals("")){
			String temp1[]= toDate.split("/");
			toDate = temp1[2]+"-"+temp1[1]+"-"+temp1[0];
		}
		Connection connection = null;
		try{
		
			connection = Connection_provider.getconnection();
			ChargesReportDAO chargesReportDAO = new JDBCChargesReportDAO(connection);
			ArrayList<Accounts> paymentList = new ArrayList<Accounts>();
			paymentList = (ArrayList<Accounts>)session.getAttribute("paymentreportList");
			chargesReportForm.setPaymentList(paymentList);
			
			
			double totalAmt = (Double)session.getAttribute("paymentTotal");
			
			String currentDate = DateTimeUtils.getDateinSimpleFormate(new Date());
			String temp[] = currentDate.split(" ");
			String temptime[] = currentDate.split("at");
			String temp1[] = temp[0].split("-");
			String date = temp1[0] + "/" + temp1[1] + "/" + temp1[2];
			String time = temptime[1];
			StringBuffer str = new StringBuffer();
			str.append("<div style='font-size: 20px; font-weight: bold;'> Payment Report </div>");
			str.append("<div style='font-size: 10px; font-weight: bold;'> Date: "+date+" </div>");
			str.append("<div style='font-size: 10px; font-weight: bold;'> Time: "+time+" </div>");
			str.append("<br>");
			str.append("<table cellpadding='0' cellspacing='0' style='width: 100%;font-size: 10px;'>");
			str.append("<tr>");
			str.append("<th>Invoice</th>");
			str.append("<th>Client No.</th>");
			str.append("<th>Date</th>");
			str.append("<th>Payment Mode</th>");
			str.append("<th>Amount Paid</th>");
			str.append("</tr>");
			
			str.append("<tr><td colspan='5'><hr></td></tr>");
			
			for(Accounts accounts : paymentList){
				str.append("<tr>");
				if(accounts.getWhoPay().equals("Client")){
					str.append("<td>"+accounts.getInvoicee()+" (Self)</td>");
				}else{
					str.append("<td>"+accounts.getInvoicee()+" (Third Party)</td>");
				}
				
				
				
				str.append("<td>0000"+accounts.getClientid()+"</td>");
				str.append("<td>"+accounts.getDate()+"</td>");
				str.append("<td>"+accounts.getPaymentmode()+"</td>");
				str.append("<td>"+Constants.getCurrency(loginInfo)+DateTimeUtils.changeFormat(accounts.getAmount())+"</td>");
				
				str.append("</tr>");
				
			}
			
				str.append("<tr><td colspan='5'><hr></td></tr>");
				
				str.append("<tr>");
				str.append("<td colspan='4'>Total</td>");
				str.append("<td style='font-weight:bold'>"+Constants.getCurrency(loginInfo)+DateTimeUtils.changeFormat(totalAmt)+"</td>");
				str.append("</tr>");
			
				str.append("</table>");
				String filePath = request.getRealPath("//liveData//chargesReport//");
				String filename = "PaymentReport.pdf";

				htmlToPdfFile(str.toString(), filePath, filename);
				
				
				session.setAttribute("paymentReportFileName", filePath+"/"+filename);
				System.out.println("pdf done");
				response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write(""+filename+"");
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				connection.close();
			}
		return null;
	}
	
	public String sendMailPaymentReport() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{
			
		connection = Connection_provider.getconnection();
		
		String to = request.getParameter("to");
		String cc = request.getParameter("cc");
		String subject = request.getParameter("subject");
		String notes = request.getParameter("emailBody");
		String file = request.getParameter("file");	
		String filename = (String)session.getAttribute("paymentReportFileName");		
		
		//int result = emailTemplateDAO.saveEmailLogDetails(to, cc, subject, notes, date1,time);
		String type = "Email";
		int appointmentid = 0;
		int invoiceid = 0;
		EmailLetterLog emailLetterLog = new EmailLetterLog();
		emailLetterLog.setAppointmentid(appointmentid);
		emailLetterLog.setInvoiceid(invoiceid);
		emailLetterLog.setType(type);
		
		EmbeddedImageEmailUtil.sendMailAttachment(connection,loginInfo.getId(),to, cc, subject, notes,filename,loginInfo,emailLetterLog,"0");
		
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			connection.close();
		}
		return null;
		
	}
	public void htmlToPdfFile(String htmlString, String filepath,
			String fileaName) throws Exception {	

		CYaHPConverter converter = new CYaHPConverter();
		File fout = new File(filepath + "/" + fileaName);
		FileOutputStream out = new FileOutputStream(fout);
		Map properties = new HashMap();
		ArrayList headerFooterList = new ArrayList();

		properties.put(IHtmlToPdfTransformer.PDF_RENDERER_CLASS,IHtmlToPdfTransformer.FLYINGSAUCER_PDF_RENDERER);
		// properties.put(IHtmlToPdfTransformer.FOP_TTF_FONT_PATH, fontPath);
		converter.convertToPdf(htmlString, IHtmlToPdfTransformer.A4P,(java.util.List) headerFooterList, "file:///temp/", out, properties);
		out.flush();
		out.close();
	}
	
	public String move() throws Exception{
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			ChargesReportDAO chargesReportDAO = new JDBCChargesReportDAO(connection);
			
			int result = chargesReportDAO.moveInvoiceToScecondary(chargesReportForm.getHdnprimaryinvoice());
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			connection.close();
		}
		
	return invoiceReport();
	}
	
	
	public String auditor() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		
		if(chargesReportForm.getOrderby().equals("")){
			chargesReportForm.setOrderby("date");
		}
		
		String fromDate = chargesReportForm.getFromDate();
		String toDate = chargesReportForm.getToDate();
		
		if(fromDate.equals("")){
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.DATE, -7); 
			fromDate = dateFormat.format(cal.getTime());
			chargesReportForm.setFromDate(fromDate);
		}
		if(toDate.equals("")){
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.DATE, -7); 
			toDate = dateFormat.format(cal.getTime());
			chargesReportForm.setToDate(toDate);
		}
		
		
		if(!fromDate.equals("")){
			String temp[]= fromDate.split("/");
			fromDate = temp[2]+"-"+temp[1]+"-"+temp[0];
		}
		if(!toDate.equals("")){
			String temp1[]= toDate.split("/");
			toDate = temp1[2]+"-"+temp1[1]+"-"+temp1[0];
		}
		
	
		Connection connection = null;
		try{
			
			connection = Connection_provider.getconnection();
			ChargesReportDAO chargesReportDAO = new JDBCChargesReportDAO(connection);
			
			
			ArrayList<Accounts> opdInvoiceList = new ArrayList<Accounts>();
			ArrayList<Accounts> ipdInvoiceList = new ArrayList<Accounts>();
			ArrayList<Accounts> hdInvoiceList = new ArrayList<Accounts>();
			ArrayList<Accounts> invInvoiceList = new ArrayList<Accounts>();
			ArrayList<Accounts> advanceInvoiceList = new ArrayList<Accounts>();
			ArrayList<Accounts> vaccinelist = new ArrayList<Accounts>();
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		      Calendar cal = Calendar.getInstance();
		      String date = dateFormat.format(cal.getTime());  
			
			int result = chargesReportDAO.saveMisReportLog("Auditors Report",loginInfo.getUserId(),fromDate,toDate,date,"auditor");
			
			String invcetype = "";
			if(chargesReportForm.getInvcetype().equals("0")){
				invcetype = "1";
				 opdInvoiceList= chargesReportDAO.getAuditorsInvoiceReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),invcetype);
				chargesReportForm.setOpdInvoiceList(opdInvoiceList);
				
				invcetype = "2";
				ipdInvoiceList = chargesReportDAO.getAuditorsInvoiceReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),invcetype);
				chargesReportForm.setIpdInvoiceList(ipdInvoiceList);
				
				invcetype = "3";
				hdInvoiceList = chargesReportDAO.getAuditorsInvoiceReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),invcetype);
				chargesReportForm.setHdInvoiceList(hdInvoiceList);
				
				invcetype = "4";
				invInvoiceList = chargesReportDAO.getAuditorsInvoiceReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),invcetype);
				chargesReportForm.setInvInvoiceList(invInvoiceList);
				
				invcetype = "5";
				advanceInvoiceList = chargesReportDAO.getAdvanceInvoiceReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),invcetype,null);
				chargesReportForm.setAdvanceInvoiceList(advanceInvoiceList);
				
				invcetype = "6";
				vaccinelist = chargesReportDAO.getAuditorsInvoiceReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),invcetype);
				chargesReportForm.setVaccinelist(vaccinelist);
				
			}else if(chargesReportForm.getInvcetype().equals("1")){
				invcetype = "1";
				 opdInvoiceList= chargesReportDAO.getAuditorsInvoiceReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),invcetype);
				chargesReportForm.setOpdInvoiceList(opdInvoiceList);
			}else if(chargesReportForm.getInvcetype().equals("2")){
				invcetype = "2";
				ipdInvoiceList = chargesReportDAO.getAuditorsInvoiceReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),invcetype);
				chargesReportForm.setIpdInvoiceList(ipdInvoiceList);
			}else if(chargesReportForm.getInvcetype().equals("3")){
				invcetype = "3";
				hdInvoiceList = chargesReportDAO.getAuditorsInvoiceReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),invcetype);
				chargesReportForm.setHdInvoiceList(hdInvoiceList);
			}else if(chargesReportForm.getInvcetype().equals("4")){
				invcetype = "4";
				invInvoiceList = chargesReportDAO.getAuditorsInvoiceReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),invcetype);
				chargesReportForm.setInvInvoiceList(invInvoiceList);
			}
			
			else if(chargesReportForm.getInvcetype().equals("5")){
				
				invcetype = "5";
				advanceInvoiceList = chargesReportDAO.getAdvanceInvoiceReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),invcetype,null);
				chargesReportForm.setAdvanceInvoiceList(advanceInvoiceList);
			}
			else if(chargesReportForm.getInvcetype().equals("6")){
				
				invcetype = "6";
				vaccinelist = chargesReportDAO.getAuditorsInvoiceReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),invcetype);
				chargesReportForm.setVaccinelist(vaccinelist);
			}
			if(chargesReportForm.getPayby()!=null) {
				if(chargesReportForm.getPayby().equals(Constants.PAY_BY_THIRD_PARTY)){
					advanceInvoiceList = new ArrayList<Accounts>();
				}
				if(!chargesReportForm.getThirdParty().equals("0")){
					advanceInvoiceList = new ArrayList<Accounts>();
				}
				if(chargesReportForm.getPaymentStatus().equals("Not Paid")){
					advanceInvoiceList = new ArrayList<Accounts>();
				}
			}
			
			double dtotal = 0;
			double ctotal = 0;
			double btotal = 0;
			
			//ipd
			for(Accounts accounts : ipdInvoiceList){
				dtotal = dtotal + accounts.getDebitAmount();
				ctotal = ctotal + accounts.getCreditAmount();
				btotal = btotal + accounts.getBalance();
			}
			
			//opd
			for(Accounts accounts : opdInvoiceList){
				dtotal = dtotal + accounts.getDebitAmount();
				ctotal = ctotal + accounts.getCreditAmount();
				btotal = btotal + accounts.getBalance();
			}
			
			//hd
			for(Accounts accounts : hdInvoiceList){
				dtotal = dtotal + accounts.getDebitAmount();
				ctotal = ctotal + accounts.getCreditAmount();
				btotal = btotal + accounts.getBalance();
			}
			
			//invst
			for(Accounts accounts : invInvoiceList){
				dtotal = dtotal + accounts.getDebitAmount();
				ctotal = ctotal + accounts.getCreditAmount();
				btotal = btotal + accounts.getBalance();
			}
			
			//advance
			
			for(Accounts accounts : advanceInvoiceList){
				//dtotal = dtotal + accounts.getDebitAmount();
				ctotal = ctotal + accounts.getCreditAmount();
				//btotal = btotal + accounts.getBalance();
			}
			
			for(Accounts accounts : vaccinelist){
				dtotal = dtotal + accounts.getDebitAmount();
				ctotal = ctotal + accounts.getCreditAmount();
				btotal = btotal + accounts.getBalance();
			}
			
			chargesReportForm.setDtotal(DateTimeUtils.changeFormat(dtotal));
			chargesReportForm.setCtotal(DateTimeUtils.changeFormat(ctotal));
			chargesReportForm.setBtotal(DateTimeUtils.changeFormat(btotal));
			
			//session.setAttribute("invoicereportlist", ipdinvoiceList);
			
			if(chargesReportForm.getPayby()==null){
				chargesReportForm.setPayby("0");
				
			}
			if(chargesReportForm.getThirdParty()==null){
				chargesReportForm.setThirdParty("0");
				chargesReportForm.setTpName("All");
			}
			if(chargesReportForm.getPaymentStatus()==null){
				chargesReportForm.setPaymentStatus("0");
			}
			
			ThirdPartyDAO thirdPartyDAO = new JDBCThirdPartyDAO(connection);
			if(!chargesReportForm.getThirdParty().equals("0")){
				ThirdParty thirdParty = thirdPartyDAO.getThirdPartyDetails(chargesReportForm.getThirdParty());
				chargesReportForm.setTpName(thirdParty.getCompanyName());
				
			}
			
			session.setAttribute("invpayby", chargesReportForm.getPayby());
			session.setAttribute("invthirdparty", chargesReportForm.getTpName());
			session.setAttribute("invpaymentstatus", chargesReportForm.getPaymentStatus());
			session.setAttribute("invfromdate", chargesReportForm.getFromDate());
			session.setAttribute("invtodate", chargesReportForm.getToDate());
			
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			connection.close();
		}
		
		return "auditor";
	}
	
	public String invoiceReport() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		
		if(chargesReportForm.getOrderby().equals("")){
			chargesReportForm.setOrderby("date");
		}
		
		String fromDate = chargesReportForm.getFromDate();
		String toDate = chargesReportForm.getToDate();
		
		if(fromDate.equals("")){
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.DATE, -7); 
			fromDate = dateFormat.format(cal.getTime());
			chargesReportForm.setFromDate(fromDate);
		}
		if(toDate.equals("")){
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.DATE, -7); 
			toDate = dateFormat.format(cal.getTime());
			chargesReportForm.setToDate(toDate);
		}
		
		
		if(!fromDate.equals("")){
			String temp[]= fromDate.split("/");
			fromDate = temp[2]+"-"+temp[1]+"-"+temp[0];
		}
		if(!toDate.equals("")){
			String temp1[]= toDate.split("/");
			toDate = temp1[2]+"-"+temp1[1]+"-"+temp1[0];
		}
		
	/*	String oredr = request.getParameter("order");
		
		if(oredr!=null){
			if(oredr.equals("asc")){
				chargesReportForm.setOrder("desc");
			}else{
				chargesReportForm.setOrder("asc");
			}
		}
		
		session.setAttribute("order", chargesReportForm.getOrder());*/
		Connection connection = null;
		try{
			
			connection = Connection_provider.getconnection();
			ChargesReportDAO chargesReportDAO = new JDBCChargesReportDAO(connection);
			
			int result = chargesReportDAO.moveInvoiceToScecondary(chargesReportForm.getHdnprimaryinvoice());
			
			if(loginInfo.getJobTitle().equals(Constants.SUPER_ADMIN)){
				chargesReportForm.setInvoicecategory("1");
			} String invoicetype= chargesReportForm.getItype();
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		      Calendar cal = Calendar.getInstance();
		      String date = dateFormat.format(cal.getTime());  
			
			 result = chargesReportDAO.saveMisReportLog("Invoice Report",loginInfo.getUserId(),fromDate,toDate,date,"invoiceReport");
			
			
			
			ArrayList<Accounts> invoiceList = new ArrayList<Accounts>();
			invoiceList = chargesReportDAO.getInvoiceReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),invoicetype);
			chargesReportForm.setInvoiceList(invoiceList);
			
			double dtotal = 0;
			double ctotal = 0;
			double btotal = 0;
			double ptotal = 0;
			for(Accounts accounts : invoiceList){
				dtotal = dtotal + accounts.getDebitAmount();
				ctotal = ctotal + accounts.getCreditAmount();
				btotal = btotal + accounts.getBalance();
				ptotal = ptotal + accounts.getTotalAmount();
			}
			
			chargesReportForm.setDtotal(DateTimeUtils.changeFormat(dtotal));
			chargesReportForm.setCtotal(DateTimeUtils.changeFormat(ctotal));
			chargesReportForm.setBtotal(DateTimeUtils.changeFormat(btotal));
			chargesReportForm.setTotalamount(DateTimeUtils.changeFormat(ptotal));
			session.setAttribute("invoicereportlist", invoiceList);
			
			if(chargesReportForm.getPayby()==null){
				chargesReportForm.setPayby("0");
				
			}
			if(chargesReportForm.getThirdParty()==null){
				chargesReportForm.setThirdParty("0");
				chargesReportForm.setTpName("All");
			}
			if(chargesReportForm.getPaymentStatus()==null){
				chargesReportForm.setPaymentStatus("0");
			}
			
			ThirdPartyDAO thirdPartyDAO = new JDBCThirdPartyDAO(connection);
			if(!chargesReportForm.getThirdParty().equals("0")){
				ThirdParty thirdParty = thirdPartyDAO.getThirdPartyDetails(chargesReportForm.getThirdParty());
				chargesReportForm.setTpName(thirdParty.getCompanyName());
				
			}
			
			session.setAttribute("invpayby", chargesReportForm.getPayby());
			session.setAttribute("invthirdparty", chargesReportForm.getTpName());
			session.setAttribute("invpaymentstatus", chargesReportForm.getPaymentStatus());
			session.setAttribute("invfromdate", chargesReportForm.getFromDate());
			session.setAttribute("invtodate", chargesReportForm.getToDate());
			
			
			AccountsDAO accountsDAO= new JDBCAccountsDAO(connection);
			   ArrayList<Master> invoicetypelist = accountsDAO.getInvoiceTypeList();
			   
			   chargesReportForm.setInvoicetypelist(invoicetypelist);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				connection.close();
			}
		return "invoiceReportSucess";
		
	}
	public String paymentReport() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		String fromDate = chargesReportForm.getFromDate();
		String toDate = chargesReportForm.getToDate();
		String userid= chargesReportForm.getUserid();
		String sortfilter = chargesReportForm.getSortfilter();
		
		if(sortfilter!=null){
			if(sortfilter.equals("")){
				sortfilter = "2";
			}
		}else{
			sortfilter = "2";
		}
		
		chargesReportForm.setSortfilter(sortfilter);
		if(userid!=null){
			if(userid.equals("0")){
				 
			} 
		}else{
			userid=loginInfo.getUserId();
		}
		String usr=DateTimeUtils.isNull(userid);
		if(usr==null){
			userid=loginInfo.getUserId();
		}
		if(chargesReportForm.getOrderby().equals("")){
			chargesReportForm.setOrderby("date");
		}
		
		
		
		if(fromDate.equals("")){
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.DATE, -7); 
			fromDate = dateFormat.format(cal.getTime());
			chargesReportForm.setFromDate(fromDate);
		}
		if(toDate.equals("")){
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.DATE, -7); 
			toDate = dateFormat.format(cal.getTime());
			chargesReportForm.setToDate(toDate);
		}
		
		if(!fromDate.equals("")){
			String temp[]= fromDate.split("/");
			fromDate = temp[2]+"-"+temp[1]+"-"+temp[0];
		}
		if(!toDate.equals("")){
			String temp1[]= toDate.split("/");
			toDate = temp1[2]+"-"+temp1[1]+"-"+temp1[0];
		}
		Connection connection = null;
		try{
			
			connection = Connection_provider.getconnection();
			ChargesReportDAO chargesReportDAO = new JDBCChargesReportDAO(connection);
			ArrayList<Accounts> paymentList = new ArrayList<Accounts>();
			
			
			{
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar cal = Calendar.getInstance();
				String date = dateFormat.format(cal.getTime());
				UserProfileDAO userProfileDAO= new JDBCUserProfileDAO(connection);
				int id=userProfileDAO.getIdOfPaymentReportNotes(loginInfo.getUserId(), date);
				UserProfile user= userProfileDAO.getPaymentReportNotesData(""+id);
				chargesReportForm.setPaymentReportNote(DateTimeUtils.isNull(user.getPaymentReportNote()));
			}
			
			if(loginInfo.getJobTitle().equals(Constants.SUPER_ADMIN)){
				chargesReportForm.setInvoicecategory("1");
			}
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		      Calendar cal = Calendar.getInstance();
		      String date = dateFormat.format(cal.getTime());  
			
			int result = chargesReportDAO.saveMisReportLog("Payment Report Detailed",loginInfo.getUserId(),fromDate,toDate,date,"paymentReport");
			
		/*	String viewname = "asmnt_view" + userid + "_view";
			int cr = createView(viewname);
*/						
			String selectedInvctype = chargesReportForm.getSelectedmodality();
			paymentList = chargesReportDAO.getPaymentReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),userid,selectedInvctype,sortfilter);
			chargesReportForm.setPaymentList(paymentList);
			
			ArrayList<Accounts> invoiceList = new ArrayList<Accounts>();
			invoiceList = chargesReportDAO.getInvoiceReportListForPaymentReport(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),userid,selectedInvctype,sortfilter);
			chargesReportForm.setInvoiceList(invoiceList);
			
			double totalAmt = 0;
			for(Accounts accounts : paymentList){
				totalAmt = totalAmt+accounts.getAmount();
			}
			double invoicettotal =0;
			if(paymentList.size()!=0){
				Accounts a = paymentList.get(paymentList.size()-1);
				chargesReportForm.setOpdtotal(a.getOpdtotal());
				chargesReportForm.setIpdtotal(a.getIpdtotal());
				chargesReportForm.setPathlabtotal(a.getPathlabtotal());
				chargesReportForm.setMdcinetotal(a.getMdcinetotal());
				chargesReportForm.setDaycaretotal(a.getDaycaretotal());
				chargesReportForm.setBalanceTotalx(DateTimeUtils.changeFormat(a.getBalanceTotal()));
				invoicettotal = chargesReportDAO.getPaymentReportInvoiceAmt(a.getInvoiceids());
			}
			chargesReportForm.setInvoicettotal(invoicettotal);
			boolean flag= false;
			if(!selectedInvctype.equals("0")){
				String temp[] = selectedInvctype.split(",");
				if(temp.length>1){
					for (int i = 0; i < temp.length; i++) {
						if(Integer.parseInt(temp[i])==5){
							flag = true;
							break;
						}
					}
				}else{
					flag = true;
				}
			}else{
				flag = true;
			}
			
			//refun payment list
			ArrayList<Accounts>refundPaymentList = new ArrayList<Accounts>();
			ArrayList<Accounts>advanceInvoiceList = new ArrayList<Accounts>();
			ArrayList<Accounts> cancelledInvoiceShifts= new ArrayList<Accounts>();
			if(flag){
				refundPaymentList = chargesReportDAO.getRefundPaymentReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),"",userid,selectedInvctype,"");
				advanceInvoiceList = chargesReportDAO.getAdvancePaymentReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),"",userid,"","0");
				cancelledInvoiceShifts = chargesReportDAO.getAdvancePaymentReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),"",userid,"","1");
			}
			
			chargesReportForm.setRefundPaymentList(refundPaymentList);
			chargesReportForm.setAdvanceInvoiceList(advanceInvoiceList);
			chargesReportForm.setCancelledInvoiceShifts(cancelledInvoiceShifts);
			
			ArrayList<Accounts> ipdRefundList= new ArrayList<Accounts>();
			ArrayList<Accounts> opdRefundList= new ArrayList<Accounts>();
			boolean flag1= false;
			boolean flag2= false;
			if(loginInfo.isBalgopal()){
				if(!selectedInvctype.equals("0")){
					String temp[] = selectedInvctype.split(",");
					if(temp.length>1){
						for (int i = 0; i < temp.length; i++) {
							if(Integer.parseInt(temp[i])==9){
								flag1 = true;
								break;
							}
						}
						for (int i = 0; i < temp.length; i++) {
							if(Integer.parseInt(temp[i])==8){
								flag2 = true;
								break;
							}
						}
					}else{
						flag1 = true;
						flag2 = true;
					}
				}else{
					flag1 = true;
					flag2 = true;
				}
				if(flag1){
					ipdRefundList= chargesReportDAO.getRefundPaymentReportListIpdOpd(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),"",userid,1,selectedInvctype,"");
				}
				if(flag2){
					opdRefundList= chargesReportDAO.getRefundPaymentReportListIpdOpd(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),"",userid,0,selectedInvctype,"");
				}
			}
			double ipdref=0,opdref=0;
			for(Accounts acc:ipdRefundList){
				ipdref=ipdref+Double.parseDouble(acc.getDebitTotalx());
			}

			for(Accounts acc:opdRefundList){
				opdref=opdref+Double.parseDouble(acc.getDebitTotalx());
			}
			chargesReportForm.setIpdref(ipdref);
			chargesReportForm.setOpdref(opdref);
			chargesReportForm.setIpdRefundList(ipdRefundList);
			chargesReportForm.setOpdRefundList(opdRefundList);
			
			for(Accounts accounts : advanceInvoiceList){
				totalAmt = totalAmt+accounts.getCreditAmount();
			}
			for(Accounts accounts : cancelledInvoiceShifts){
				totalAmt = totalAmt+accounts.getCreditAmount();
			}
			if(advanceInvoiceList.size()!=0){
				Accounts ad = advanceInvoiceList.get(advanceInvoiceList.size()-1);
				chargesReportForm.setAdvreftotal(ad.getAdvreftotal());
			}
			
			if(cancelledInvoiceShifts.size()!=0){
				Accounts ad = cancelledInvoiceShifts.get(cancelledInvoiceShifts.size()-1);
				chargesReportForm.setCancelInvShiftTotal(ad.getAdvreftotal());
			}
			
			AccountsDAO accountsDAO= new JDBCAccountsDAO(connection);
			if(flag){
				chargesReportForm.setRefundtotal(accountsDAO.getAdvanceAndRefundAmt(fromDate, toDate, userid, "Refund", chargesReportForm.getHowpaid()));
				chargesReportForm.setAdvtotal(accountsDAO.getAdvanceAndRefundAmt(fromDate, toDate, userid, "Advance", chargesReportForm.getHowpaid()));
			}else{
				chargesReportForm.setRefundtotal(0);
				chargesReportForm.setAdvtotal(0);
			}
			
			
			session.setAttribute("paymentreportList", paymentList);
			session.setAttribute("paymentTotal", totalAmt);
			
			chargesReportForm.setDebitTotalx(DateTimeUtils.changeFormat(totalAmt));
			
			double cashtotal = 0;
			double cheqtotal = 0;
			double crtotal = 0;
			double drtotal = 0;
			double nefttotal = 0;
			double othertotal = 0;
			
			ArrayList<Accounts>opdpaywiselist = chargesReportDAO.getPaymentModeWiseList(fromDate,toDate,1,
					chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getInvoicecategory(),userid);
			chargesReportForm.setOpdpaywiselist(opdpaywiselist);
			for(Accounts accounts : opdpaywiselist){
				if(accounts.getPaymentmode().equals("Cash")){
					cashtotal = cashtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("Cheque")){
					cheqtotal = cheqtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("C/Card")){
					crtotal = crtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("D/Card")){
					drtotal = drtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("NEFT")){
					nefttotal = nefttotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("Other")){
					othertotal = othertotal + accounts.getAmount();
				}
				
			}
			
			
			ArrayList<Accounts>ipdpaywiselist = chargesReportDAO.getPaymentModeWiseList(fromDate,toDate,2,
					chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getInvoicecategory(),userid);
			chargesReportForm.setIpdpaywiselist(ipdpaywiselist);
			
			for(Accounts accounts : ipdpaywiselist){
				if(accounts.getPaymentmode().equals("Cash")){
					cashtotal = cashtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("Cheque")){
					cheqtotal = cheqtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("C/Card")){
					crtotal = crtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("D/Card")){
					drtotal = drtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("NEFT")){
					nefttotal = nefttotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("Other")){
					othertotal = othertotal + accounts.getAmount();
				}
				
			}
			
			ArrayList<Accounts>pathlabpaywiselist = chargesReportDAO.getPaymentModeWiseList(fromDate,toDate,3,
					chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getInvoicecategory(),userid);
			chargesReportForm.setPathlabpaywiselist(pathlabpaywiselist);
			
			for(Accounts accounts : pathlabpaywiselist){
				if(accounts.getPaymentmode().equals("Cash")){
					cashtotal = cashtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("Cheque")){
					cheqtotal = cheqtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("C/Card")){
					crtotal = crtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("D/Card")){
					drtotal = drtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("NEFT")){
					nefttotal = nefttotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("Other")){
					othertotal = othertotal + accounts.getAmount();
				}
				
			}
			
			
			
			ArrayList<Accounts>mdcinepaywiselist = chargesReportDAO.getPaymentModeWiseList(fromDate,toDate,4,
					chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getInvoicecategory(),userid);
			ArrayList<Accounts> vaccinelist= new ArrayList<Accounts>();
			vaccinelist=chargesReportDAO.getPaymentModeWiseList(fromDate,toDate,6,
					chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getInvoicecategory(),userid);
			mdcinepaywiselist.addAll(vaccinelist);
			ArrayList<Accounts> daycarelist= new ArrayList<Accounts>();
			daycarelist=chargesReportDAO.getPaymentModeWiseList(fromDate,toDate,8,
					chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getInvoicecategory(),userid);
			chargesReportForm.setMdcinepaywiselist(mdcinepaywiselist);
			
			mdcinepaywiselist.addAll(daycarelist);
			
			for(Accounts accounts : mdcinepaywiselist){
				if(accounts.getPaymentmode().equals("Cash")){
					cashtotal = cashtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("Cheque")){
					cheqtotal = cheqtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("C/Card")){
					crtotal = crtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("D/Card")){
					drtotal = drtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("NEFT")){
					nefttotal = nefttotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("Other")){
					othertotal = othertotal + accounts.getAmount();
				}
				
			}
			
			ArrayList<Accounts>advrefpaywiselist = chargesReportDAO.getAdvPaymentModeWiseList(fromDate,toDate,4,
					chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getInvoicecategory(),userid);
			chargesReportForm.setAdvrefpaywiselist(advrefpaywiselist);
			for(Accounts accounts : advrefpaywiselist){
				if(accounts.getPaymentmode().equals("Cash")){
					cashtotal = cashtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("Cheque")){
					cheqtotal = cheqtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("C/Card")){
					crtotal = crtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("D/Card")){
					drtotal = drtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("NEFT")){
					nefttotal = nefttotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("Other")){
					othertotal = othertotal + accounts.getAmount();
				}
				
			}
			double refforinvoice=0.0;
			/*double refforinvoice = chargesReportDAO.getRefForInvoicet(fromDate,toDate,4,
					chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getInvoicecategory(),userid);*/
			
			/*double refOnly=0.0;*/
			
			double refOnly = chargesReportDAO.getRefOnly(fromDate,toDate,4,
					chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getInvoicecategory(),userid);
			double totalref = refforinvoice + refOnly;
			if(!flag){
				totalref =0;
				if(flag1 && flag2){
					totalref=ipdref +opdref;
				}else if(flag1){
					totalref=ipdref;
				}else if(flag2){
					totalref=opdref;
				}
				
			}
			double calcdebittotal = totalAmt - totalref;
			chargesReportForm.setTotalcashcollect(DateTimeUtils.changeFormat(cashtotal-totalref));
			chargesReportForm.setTotalref(DateTimeUtils.changeFormat(totalref));
			chargesReportForm.setCalcdebittotal(DateTimeUtils.changeFormat(calcdebittotal));
			
			chargesReportForm.setCashtotal(DateTimeUtils.changeFormat(cashtotal));
			chargesReportForm.setCheqtotal(DateTimeUtils.changeFormat(cheqtotal));
			chargesReportForm.setCrtotal(DateTimeUtils.changeFormat(crtotal));
			chargesReportForm.setDrtotal(DateTimeUtils.changeFormat(drtotal));
			chargesReportForm.setNefttotal(DateTimeUtils.changeFormat(nefttotal));
			chargesReportForm.setOthertotal(DateTimeUtils.changeFormat(othertotal));
			
			//Akash 26-06-2019
			double requestdiscamt =0;
			double approvediscamt =0;
			requestdiscamt = chargesReportDAO.getRequestedDiscountAmt(fromDate,toDate,userid,1);
			approvediscamt = chargesReportDAO.getRequestedDiscountAmt(fromDate,toDate,userid,2);
			double appliedDisc= chargesReportDAO.getRequestedDiscountAmt(fromDate,toDate,userid,3);
			chargesReportForm.setRequestdiscamt(Math.round(requestdiscamt *100.0)/100.0);
			chargesReportForm.setApprovediscamt(Math.round(approvediscamt*100.0)/100.0);
			chargesReportForm.setAppliedDisc(Math.round(appliedDisc*100.0)/100.0);
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				connection.close();
			}
		
		
		if(chargesReportForm.getActiontype().equals("1")){
			getAllUserWiseList(fromDate,toDate,1,
					chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getInvoicecategory(),userid);
			
			return "paymentforallusers";

		}
		
		
		
		return "paymentReportSucess";
	}
	
	
	private void getAllUserWiseList(String fromDate, String toDate, int i, String payby, String howpaid,
			String invoicecategory, String userid) throws Exception {
		
		
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			ChargesReportDAO chargesReportDAO = new JDBCChargesReportDAO(connection);
			ArrayList<Master>accountUserList = chargesReportDAO.getAccountUserList();
			
			ArrayList<Accounts>list1 = new ArrayList<Accounts>();
			
		for(Master master : accountUserList){
			Accounts ac = new Accounts();
			userid = master.getUserid();
			ac.setUserid(userid);
			
			double cashtotal = 0;
			double cheqtotal = 0;
			double crtotal = 0;
			double drtotal = 0;
			double nefttotal = 0;
			double othertotal = 0;
			double totalAmt = 0;
			
			
			
			ArrayList<Accounts> paymentList = new ArrayList<Accounts>();
			
			if(loginInfo.getJobTitle().equals(Constants.SUPER_ADMIN)){
				chargesReportForm.setInvoicecategory("1");
			}
			String selectedInvctype = chargesReportForm.getSelectedmodality();
			paymentList = chargesReportDAO.getPaymentReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),userid,selectedInvctype,"0");
			chargesReportForm.setPaymentList(paymentList);
			
			
			if(paymentList.size()!=0){
				Accounts a = paymentList.get(paymentList.size()-1);
				ac.setOpdtotal(a.getOpdtotal());
				ac.setIpdtotal(a.getIpdtotal());
				ac.setPathlabtotal(a.getPathlabtotal());
				ac.setMdcinetotal(a.getMdcinetotal());
			}
			
			
			
			for(Accounts accounts : paymentList){
				totalAmt = totalAmt+accounts.getAmount();
			}
			
			
			
		    ArrayList<Accounts>advanceInvoiceList = chargesReportDAO.getAdvancePaymentReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),"",userid,"","0");
			chargesReportForm.setAdvanceInvoiceList(advanceInvoiceList);
			
			for(Accounts accounts : advanceInvoiceList){
				totalAmt = totalAmt+accounts.getCreditAmount();
			}
			
			if(advanceInvoiceList.size()!=0){
				Accounts ad = advanceInvoiceList.get(advanceInvoiceList.size()-1);
				chargesReportForm.setAdvreftotal(ad.getAdvreftotal());
			}
			
			
			
			ac.setDebitTotalx(DateTimeUtils.changeFormat(totalAmt));
			
		
		
			ArrayList<Accounts>opdpaywiselist = chargesReportDAO.getPaymentModeWiseList(fromDate,toDate,1,
					chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getInvoicecategory(),userid);
			ac.setOpdpaywiselist(opdpaywiselist);
			for(Accounts accounts : opdpaywiselist){
				if(accounts.getPaymentmode().equals("Cash")){
					cashtotal = cashtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("Cheque")){
					cheqtotal = cheqtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("C/Card")){
					crtotal = crtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("D/Card")){
					drtotal = drtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("NEFT")){
					nefttotal = nefttotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("Other")){
					othertotal = othertotal + accounts.getAmount();
				}
				
			}
			
			
			ArrayList<Accounts>ipdpaywiselist = chargesReportDAO.getPaymentModeWiseList(fromDate,toDate,2,
					chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getInvoicecategory(),userid);
			ac.setIpdpaywiselist(ipdpaywiselist);
			
			for(Accounts accounts : ipdpaywiselist){
				if(accounts.getPaymentmode().equals("Cash")){
					cashtotal = cashtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("Cheque")){
					cheqtotal = cheqtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("C/Card")){
					crtotal = crtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("D/Card")){
					drtotal = drtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("NEFT")){
					nefttotal = nefttotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("Other")){
					othertotal = othertotal + accounts.getAmount();
				}
				
			}
			
			ArrayList<Accounts>pathlabpaywiselist = chargesReportDAO.getPaymentModeWiseList(fromDate,toDate,3,
					chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getInvoicecategory(),userid);
			ac.setPathlabpaywiselist(pathlabpaywiselist);
			
			for(Accounts accounts : pathlabpaywiselist){
				if(accounts.getPaymentmode().equals("Cash")){
					cashtotal = cashtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("Cheque")){
					cheqtotal = cheqtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("C/Card")){
					crtotal = crtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("D/Card")){
					drtotal = drtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("NEFT")){
					nefttotal = nefttotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("Other")){
					othertotal = othertotal + accounts.getAmount();
				}
				
			}
			
			ArrayList<Accounts>mdcinepaywiselist = chargesReportDAO.getPaymentModeWiseList(fromDate,toDate,4,
					chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getInvoicecategory(),userid);
			ac.setMdcinepaywiselist(mdcinepaywiselist);
			
			for(Accounts accounts : mdcinepaywiselist){
				if(accounts.getPaymentmode().equals("Cash")){
					cashtotal = cashtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("Cheque")){
					cheqtotal = cheqtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("C/Card")){
					crtotal = crtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("D/Card")){
					drtotal = drtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("NEFT")){
					nefttotal = nefttotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("Other")){
					othertotal = othertotal + accounts.getAmount();
				}
				
			}
			
			ArrayList<Accounts>advrefpaywiselist = chargesReportDAO.getAdvPaymentModeWiseList(fromDate,toDate,4,
					chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getInvoicecategory(),userid);
			ac.setAdvrefpaywiselist(advrefpaywiselist);
			for(Accounts accounts : advrefpaywiselist){
				if(accounts.getPaymentmode().equals("Cash")){
					cashtotal = cashtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("Cheque")){
					cheqtotal = cheqtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("C/Card")){
					crtotal = crtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("D/Card")){
					drtotal = drtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("NEFT")){
					nefttotal = nefttotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("Other")){
					othertotal = othertotal + accounts.getAmount();
				}
				
			}
			
			double refforinvoice = chargesReportDAO.getRefForInvoicet(fromDate,toDate,4,
					chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getInvoicecategory(),userid);
			
			double refOnly = chargesReportDAO.getRefOnly(fromDate,toDate,4,
					chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getInvoicecategory(),userid);
			
			double totalref = refforinvoice + refOnly;
			double calcdebittotal = totalAmt - totalref;
			
			ac.setTotalref(DateTimeUtils.changeFormat(totalref));
			ac.setCalcdebittotal(DateTimeUtils.changeFormat(calcdebittotal));
			
			ac.setCashtotal(DateTimeUtils.changeFormat(cashtotal));
			ac.setCheqtotal(DateTimeUtils.changeFormat(cheqtotal));
			ac.setCrtotal(DateTimeUtils.changeFormat(crtotal));
			ac.setDrtotal(DateTimeUtils.changeFormat(drtotal));
			ac.setNefttotal(DateTimeUtils.changeFormat(nefttotal));
			chargesReportForm.setOthertotal(DateTimeUtils.changeFormat(othertotal));
			
			list1.add(ac);
				
			}
			
		
		chargesReportForm.setMaserUserList(list1);
		}catch (Exception e) {
			// TODO: handle exception
		}finally {
			connection.close();
		}
		
	}

	public String smallpaymentReport() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		String fromDate = chargesReportForm.getFromDate();
		String toDate = chargesReportForm.getToDate();
		
		if(chargesReportForm.getOrderby().equals("")){
			chargesReportForm.setOrderby("date");
		}
		
		if(fromDate.equals("")){
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.DATE, -7); 
			fromDate = dateFormat.format(cal.getTime());
			chargesReportForm.setFromDate(fromDate);
		}
		if(toDate.equals("")){
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.DATE, -7); 
			toDate = dateFormat.format(cal.getTime());
			chargesReportForm.setToDate(toDate);
		}
		
		if(!fromDate.equals("")){
			String temp[]= fromDate.split("/");
			fromDate = temp[2]+"-"+temp[1]+"-"+temp[0];
		}
		if(!toDate.equals("")){
			String temp1[]= toDate.split("/");
			toDate = temp1[2]+"-"+temp1[1]+"-"+temp1[0];
		}
		
		String userid= chargesReportForm.getUserid();
		String searchuserid = chargesReportForm.getSearchuserid();
		/*if(loginInfo.getUserType()==2){
			userid=null;
		}
		if(userid!=null){
			if(userid.equals("") || userid.equals("0")){
				userid=null;
			}
		}*/
		
		if(searchuserid!=null){
			if(searchuserid.equals("") || searchuserid.equals("0")){
				userid=null;
			}else{
				userid = userid;
			}
		}else{
			userid = userid;
		}
		
		if(userid!=null){
			if(userid.equals("") || userid.equals("0")){
				userid=null;
			}
		}
		chargesReportForm.setSearchuserid(userid);
		Connection connection = null;
		try{
			
			int[] itypeArray= new int[]{1,2,3,4,6,7,11};
			
			connection = Connection_provider.getconnection();
			ChargesReportDAO chargesReportDAO = new JDBCChargesReportDAO(connection);
			ArrayList<Accounts> paymentList = new ArrayList<Accounts>();
			
			if(loginInfo.getJobTitle().equals(Constants.SUPER_ADMIN)){
				chargesReportForm.setInvoicecategory("1");
			}
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		      Calendar cal = Calendar.getInstance();
		      String date = dateFormat.format(cal.getTime());  
			AccountsDAO accountsDAO= new JDBCAccountsDAO(connection);
			int result = chargesReportDAO.saveMisReportLog("Payment Report Summary",loginInfo.getUserId(),fromDate,toDate,date,"smallpaymentReport");
			String selectedInvctype = DateTimeUtils.isNull(chargesReportForm.getSelectedmodality());
		
			ArrayList<Master> smallPaymentReportListCollection= new ArrayList<Master>();
			if(DateTimeUtils.isNull(selectedInvctype).equals("0")){
				for (int ityp:itypeArray) {
					
					String invName=accountsDAO.getInvoiceName(""+ityp);
					Master payListMaster=new Master();
					ArrayList<Accounts> innerList= chargesReportDAO.getSmallPaymentReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),userid,"0,"+ityp);
					if(innerList.size()==0){
						continue;
					}
					payListMaster.setAmmount(""+innerList.get(innerList.size()-1).getFinalTotal());
					payListMaster.setInnerPaymentList(innerList);
					payListMaster.setName(invName);
					smallPaymentReportListCollection.add(payListMaster);
				}
			}else{
				for(String ityp:selectedInvctype.split(",")){
					String invName=accountsDAO.getInvoiceName(""+ityp);
					Master payListMaster=new Master();
					ArrayList<Accounts> innerList= chargesReportDAO.getSmallPaymentReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),userid,"0,"+ityp);
					if(innerList.size()==0){
						continue;
					}
					payListMaster.setAmmount(""+innerList.get(innerList.size()-1).getFinalTotal());
					payListMaster.setInnerPaymentList(innerList);
					payListMaster.setName(invName);
					smallPaymentReportListCollection.add(payListMaster);
				}
			}
			chargesReportForm.setSmallPaymentReportListCollection(smallPaymentReportListCollection);
			paymentList = chargesReportDAO.getSmallPaymentReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),userid,selectedInvctype);
			chargesReportForm.setPaymentList(paymentList);
			chargesReportForm.setSelectedmodality(selectedInvctype);
			double totalAmt = 0;
			for(Accounts accounts : paymentList){
				totalAmt = totalAmt+accounts.getAmount();
			}
			String selectedInvctypenew = selectedInvctype;
			if(selectedInvctype!=null){
				if(!selectedInvctype.equals("0")){
					selectedInvctypenew = "0,"+selectedInvctypenew;
				}
			}
			ArrayList<Accounts> invoiceList = new ArrayList<Accounts>();
			invoiceList = chargesReportDAO.getInvoiceReportListForPaymentReport(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),userid,selectedInvctypenew,"");
			chargesReportForm.setInvoiceList(invoiceList);
			
			double cashtotal = 0;
			double cheqtotal = 0;
			double crtotal = 0;
			double drtotal = 0;
			double nefttotal = 0;
			double othertotal = 0;
			for(Accounts accounts : paymentList){
			if(accounts.getPaymentmode().equals("Cash")){
					cashtotal = cashtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("Cheque")){
					cheqtotal = cheqtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("C/Card")){
					crtotal = crtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("D/Card")){
					drtotal = drtotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("NEFT")){
					nefttotal = nefttotal + accounts.getAmount();
				}
				if(accounts.getPaymentmode().equals("Other")){
					othertotal = othertotal + accounts.getAmount();
				}
			}
			
			ArrayList<Accounts>refundPaymentList = new ArrayList<Accounts>();
			ArrayList<Accounts>advanceInvoiceList = new ArrayList<Accounts>();
			ArrayList<Accounts>shiftedadvanceList = new ArrayList<Accounts>();
			boolean flag = false;
			if(!selectedInvctype.equals("0")){
				if(Integer.parseInt(selectedInvctype)==5){
					flag = true;
				}
			}else{
				flag = true;
			}
			if(flag){
				refundPaymentList = chargesReportDAO.getRefundPaymentReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),"",userid,selectedInvctype,chargesReportForm.getHowpaid());
				advanceInvoiceList = chargesReportDAO.getAdvancePaymentReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),"",userid,chargesReportForm.getHowpaid(),"0");
				shiftedadvanceList = chargesReportDAO.getAdvancePaymentReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),"",userid,chargesReportForm.getHowpaid(),"1");
			}
			
		    //advanceInvoiceList = chargesReportDAO.getAdvanceInvoiceReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),"",userid);
			
			for(Accounts accounts : advanceInvoiceList){
				totalAmt = totalAmt+accounts.getCreditAmount();
			}
			for(Accounts accounts : shiftedadvanceList){
				totalAmt = totalAmt+accounts.getCreditAmount();
			}
			
			for(Accounts accounts : advanceInvoiceList){
				if(accounts.getPaymentmode().equals("Cash")){
						cashtotal = cashtotal + accounts.getCreditAmount();
					}
					if(accounts.getPaymentmode().equals("Cheque")){
						cheqtotal = cheqtotal + accounts.getCreditAmount();
					}
					if(accounts.getPaymentmode().equals("C/Card")){
						crtotal = crtotal + accounts.getCreditAmount();
					}
					if(accounts.getPaymentmode().equals("D/Card")){
						drtotal = drtotal + accounts.getCreditAmount();
					}
					if(accounts.getPaymentmode().equals("NEFT")){
						nefttotal = nefttotal + accounts.getCreditAmount();
					}
					if(accounts.getPaymentmode().equals("Other")){
						othertotal = othertotal + accounts.getCreditAmount();
					}
				}
			
			
			
			for(Accounts accounts : shiftedadvanceList){
				if(accounts.getPaymentmode().equals("Cash")){
						cashtotal = cashtotal + accounts.getCreditAmount();
					}
					if(accounts.getPaymentmode().equals("Cheque")){
						cheqtotal = cheqtotal + accounts.getCreditAmount();
					}
					if(accounts.getPaymentmode().equals("C/Card")){
						crtotal = crtotal + accounts.getCreditAmount();
					}
					if(accounts.getPaymentmode().equals("D/Card")){
						drtotal = drtotal + accounts.getCreditAmount();
					}
					if(accounts.getPaymentmode().equals("NEFT")){
						nefttotal = nefttotal + accounts.getCreditAmount();
					}
					if(accounts.getPaymentmode().equals("Other")){
						othertotal = othertotal + accounts.getCreditAmount();
					}
				}
			
			
			chargesReportForm.setRefundPaymentList(refundPaymentList);
			chargesReportForm.setAdvanceInvoiceList(advanceInvoiceList);
			chargesReportForm.setCancelledInvoiceShifts(shiftedadvanceList);
			/*ArrayList<Accounts>advanceInvoiceList1 = chargesReportDAO.getAdvancePaymentReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),"",userid);
			chargesReportForm.setAdvanceInvoiceList(advanceInvoiceList);*/
			
			/*if(advanceInvoiceList.size()!=0){
				Accounts ad = advanceInvoiceList.get(advanceInvoiceList.size()-1);
				chargesReportForm.setAdvreftotal(ad.getAdvreftotal());
			}*/
			
			ArrayList<Accounts> ipdRefundList= new ArrayList<Accounts>();
			ArrayList<Accounts> opdRefundList= new ArrayList<Accounts>();
			boolean flag1= false;
			boolean flag2= false;
			if(loginInfo.isBalgopal()){
				if(Integer.parseInt(selectedInvctype)==9){
					flag1 = true;
				}else if(Integer.parseInt(selectedInvctype)==8){
					flag2 = true;
				}
				if(flag1){
					ipdRefundList= chargesReportDAO.getRefundPaymentReportListIpdOpd(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),"",userid,1,selectedInvctype,chargesReportForm.getHowpaid());
				}else if(flag2){
					opdRefundList= chargesReportDAO.getRefundPaymentReportListIpdOpd(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),"",userid,0,selectedInvctype,chargesReportForm.getHowpaid());
				}else if(flag1==false && flag2==false){
					ipdRefundList= chargesReportDAO.getRefundPaymentReportListIpdOpd(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),"",userid,1,selectedInvctype,chargesReportForm.getHowpaid());
					opdRefundList= chargesReportDAO.getRefundPaymentReportListIpdOpd(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),"",userid,0,selectedInvctype,chargesReportForm.getHowpaid());
				}
			}
			double ipdref=0,opdref=0;
			for(Accounts acc:ipdRefundList){
				ipdref=ipdref+Double.parseDouble(acc.getDebitTotalx());
			}

			for(Accounts acc:opdRefundList){
				opdref=opdref+Double.parseDouble(acc.getDebitTotalx());
			}
			chargesReportForm.setIpdref(ipdref);
			chargesReportForm.setOpdref(opdref);
			chargesReportForm.setIpdRefundList(ipdRefundList);
			chargesReportForm.setOpdRefundList(opdRefundList);
			
			/*for(Accounts accounts : advanceInvoiceList){
				totalAmt = totalAmt+accounts.getCreditAmount();
			}*/
			
			if(advanceInvoiceList.size()!=0){
				Accounts ad = advanceInvoiceList.get(advanceInvoiceList.size()-1);
				chargesReportForm.setAdvreftotal(ad.getAdvreftotal());
			}
			if(shiftedadvanceList.size()!=0){
				Accounts ad = shiftedadvanceList.get(shiftedadvanceList.size()-1);
				chargesReportForm.setCancelInvShiftTotal(ad.getAdvreftotal());
			}
			//AccountsDAO accountsDAO= new JDBCAccountsDAO(connection);
			if(flag){
				chargesReportForm.setRefundtotal(accountsDAO.getAdvanceAndRefundAmt(fromDate, toDate, userid, "Refund", chargesReportForm.getHowpaid()));
				chargesReportForm.setAdvtotal(accountsDAO.getAdvanceAndRefundAmt(fromDate, toDate, userid, "Advance", chargesReportForm.getHowpaid()));
			}else{
				chargesReportForm.setRefundtotal(0);
				chargesReportForm.setAdvtotal(0);
			}
			
			session.setAttribute("paymentreportList", paymentList);
			session.setAttribute("paymentTotal", totalAmt);
			
			chargesReportForm.setDebitTotalx(DateTimeUtils.changeFormat(totalAmt));
			
			
			/*double refforinvoice = chargesReportDAO.getRefForInvoicet(fromDate,toDate,4,
					chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getInvoicecategory(),userid);*/
			
			//refun payment list
			/*ArrayList<Accounts>refundPaymentList = chargesReportDAO.getRefundPaymentReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),"",userid,"");
			chargesReportForm.setRefundPaymentList(refundPaymentList);*/
			
			double refforinvoice=0.0;
			double refOnly = chargesReportDAO.getRefOnly(fromDate,toDate,4,
					chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getInvoicecategory(),userid);
			
			double totalref = refforinvoice + refOnly;
			if(!flag){
				totalref =0;
				if(flag1 && flag2){
					totalref=ipdref +opdref;
					chargesReportForm.setIpdref(ipdref);
					chargesReportForm.setOpdref(opdref);
				}else if(flag1){
					totalref=ipdref;
				}else if(flag2){
					totalref=opdref;
				}
				
			}
			double calcdebittotal = totalAmt - totalref;
			
			chargesReportForm.setTotal(DateTimeUtils.changeFormat(totalAmt-totalref));
			chargesReportForm.setTotalcashcollect(DateTimeUtils.changeFormat(cashtotal-totalref));
			chargesReportForm.setTotalref(DateTimeUtils.changeFormat(totalref));
			chargesReportForm.setCalcdebittotal(DateTimeUtils.changeFormat(calcdebittotal));
			
			
		    chargesReportForm.setCashtotal(DateTimeUtils.changeFormat(cashtotal));
			chargesReportForm.setCheqtotal(DateTimeUtils.changeFormat(cheqtotal));
			chargesReportForm.setCrtotal(DateTimeUtils.changeFormat(crtotal));
			chargesReportForm.setDrtotal(DateTimeUtils.changeFormat(drtotal));
			chargesReportForm.setNefttotal(DateTimeUtils.changeFormat(nefttotal));
			chargesReportForm.setOthertotal(DateTimeUtils.changeFormat(othertotal));
			
			ArrayList<Accounts> creditBalanceReportList=chargesReportDAO.getCreditBalanceReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),"",userid);
			chargesReportForm.setCreditBalanceReportList(creditBalanceReportList);
			double totalcredit=0;
			/*for (Accounts accounts2 : creditBalanceReportList) {
				 totalcredit=totalcredit+accounts2.getBalance();
			}*/
			
			
			ArrayList<Accounts> creditInvoiceReportList=new ArrayList<Accounts>();
			creditInvoiceReportList=chargesReportDAO.creditInvoiceReportList(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),"",userid);
			chargesReportForm.setCreditInvoiceReportList(creditInvoiceReportList);
			
			double totalcr=0;
			if(creditInvoiceReportList.size()>0){
				totalcr= creditInvoiceReportList.get(creditInvoiceReportList.size() - 1).getBalanceTotal();
			}
			chargesReportForm.setTotalcr(totalcr);
			
			int size = creditBalanceReportList.size();
			if (size > 0) {
				totalcredit = creditBalanceReportList.get(size - 1).getBalanceTotal();
			} 
			chargesReportForm.setTotalcredit(totalcredit);
			Clinic clinic = new Clinic();
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			
			ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
			clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
			chargesReportForm.setClinicName(clinic.getClinicName());
			chargesReportForm.setClinicOwner(clinic.getClinicOwner());
			chargesReportForm.setOwner_qualification(clinic.getOwner_qualification());
			chargesReportForm.setLocationAdressList(locationAdressList);
			chargesReportForm.setAddress(clinic.getAddress());
			chargesReportForm.setLandLine(clinic.getLandLine());
			chargesReportForm.setClinicemail(clinic.getEmail());
			chargesReportForm.setWebsiteUrl(clinic.getWebsiteUrl());
			chargesReportForm.setClinicLogo(clinic.getUserImageFileName());

			chargesReportForm.setUserid(userid);
			
			//Akash 26-06-2019
			double requestdiscamt =0;
			double approvediscamt =0;
			requestdiscamt = chargesReportDAO.getRequestedDiscountAmt(fromDate,toDate,userid,1);
			approvediscamt = chargesReportDAO.getRequestedDiscountAmt(fromDate,toDate,userid,2);
			chargesReportForm.setRequestdiscamt(Math.round(requestdiscamt *100.0)/100.0);
			chargesReportForm.setApprovediscamt(Math.round(approvediscamt*100.0)/100.0);
			
			}catch(Exception e){
				e.printStackTrace();
			}finally {
				connection.close();
			}
		return "paymentReportSmall";
	}
	
	public String opdreport() throws Exception{
		Connection connection = null;
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "opdreport";
		
	}
	
	public ChargesReportForm getModel() {
		// TODO Auto-generated method stub
		return chargesReportForm;
	}

	public void prepare() throws Exception {
		Connection connection  = null;
		
		try{
			
			connection  = Connection_provider.getconnection();
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			UserProfileDAO userProfileDAO=new JDBCUserProfileDAO(connection);
			
			//ArrayList<Accounts>locationList = accountsDAO.getLocationList(loginInfo.getClinicid());
			ArrayList<Accounts>thirdPartyList = accountsDAO.getThirdPartyList(loginInfo.getId());
		//	accountsForm.setLocationList(locationList);
			chargesReportForm.setThirdPartyList(thirdPartyList);
			
			ArrayList<Master>invoiceTypeList = accountsDAO.getInvoiceTypeList();
			chargesReportForm.setInvoiceTypeLis(invoiceTypeList);
			
			ArrayList<UserProfile> userProfileList= userProfileDAO.getAllUserProfileList();
			chargesReportForm.setUserProfileList(userProfileList);
			chargesReportForm.setUserid(loginInfo.getUserId());
			
			ConsultationNoteDAO consultationNoteDAO = new JDBCConsultationNoteDAO(connection);
			 ArrayList<Client>conditionList = consultationNoteDAO.getConditionList(0);
			 chargesReportForm.setConditionList(conditionList);
			 
			 
			 ArrayList<String> jobTitleList = userProfileDAO.getJobTitleList();
			 chargesReportForm.setJobTitleList(jobTitleList);
			
			   Clinic clinic = new Clinic();
			   ClinicDAO clinicDAO= new JDBCClinicDAO(connection);
			   clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
			   chargesReportForm.setClinicName(clinic.getClinicName());
			   chargesReportForm.setClinicOwner(clinic.getClinicOwner());
			   chargesReportForm.setOwner_qualification(clinic.getOwner_qualification());
			  
			   chargesReportForm.setAddress(clinic.getAddress());
			   chargesReportForm.setLandLine(clinic.getLandLine());
			   chargesReportForm.setClinicemail(clinic.getEmail());
			   chargesReportForm.setWebsiteUrl(clinic.getWebsiteUrl());
			   chargesReportForm.setClinicLogo(clinic.getUserImageFileName());
			
		}
		catch(Exception e){
			e.printStackTrace();
		}finally {
			connection.close();
		}
		
	}
	
	public String newDetailed(){
		  Connection connection = null;
		  String x;
		/*x =request.getParameter("appmmt");*/
		  if(!verifyLogin(request)){
		   return "login";
		  }
		  String fromDate = chargesReportForm.getFromDate();
		  String toDate = chargesReportForm.getToDate(); 
		  if(fromDate!=null&&fromDate.contains("/")){
			   String temp[]= fromDate.split("/");
			   fromDate = temp[0]+"-"+temp[1]+"-"+temp[2];
			  
		  }
		  if(toDate!=null&&toDate.contains("/")){
			   String temp[]= toDate.split("/");
			   toDate = temp[0]+"-"+temp[1]+"-"+temp[2];
		  }
		  if(chargesReportForm.getOrderby().equals("")){
		   chargesReportForm.setOrderby("commencing");
		  }
		  

		  if(fromDate==null){
		   fromDate="";
		  }
		  if(toDate==null){
		   toDate="";
		  }
		  if(fromDate.equals("")){
		   DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		   Calendar cal = Calendar.getInstance();
		   cal.add(Calendar.DATE, -1); 
		   fromDate = dateFormat.format(cal.getTime());
		   chargesReportForm.setFromDate(fromDate);
		  }
		  if(toDate.equals("")){
		   DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		   Calendar cal = Calendar.getInstance();
		   //cal.add(Calendar.DATE, -7); 
		   toDate = dateFormat.format(cal.getTime());
		   chargesReportForm.setToDate(toDate);
		  }
		  
		  if(!fromDate.equals("")){
		   String temp[]= fromDate.split("-");
		   fromDate = temp[2]+"-"+temp[1]+"-"+temp[0];
		  }
		  if(!toDate.equals("")){
		   String temp1[]= toDate.split("-");
		   toDate = temp1[2]+"-"+temp1[1]+"-"+temp1[0];
		  }

		String apmttype="", ChargesType=""; 
		apmttype= chargesReportForm.getApmtType();
		ChargesType= chargesReportForm.getChargeType();
		if(apmttype==null){
			 apmttype="";
		}
		if(ChargesType==null){
			ChargesType="";
		}
		String doctorselected=chargesReportForm.getDoctor();
		chargesReportForm.setDoctor(doctorselected);
		 String aptname=request.getParameter("hideselect");
		 String invsearch= request.getParameter("searchinv");
		 
		 if(invsearch==null){
			 invsearch="";
		 }
		  
		  try {
		   connection = Connection_provider.getconnection();
		   ArrayList<Accounts> reportlist= new ArrayList<Accounts>();
		   AppointmentTypeDAO appointmentTypeDAO= new JDBCAppointmentTypeDAO(connection);
		   ArrayList<AppointmentType> apmtlist= new ArrayList<AppointmentType>();
		     apmtlist=appointmentTypeDAO.getallApmtList1();
		     
		 /*    StringBuffer buffer = new StringBuffer();
		     buffer.append("<select name='apmt' class='selectpicker' multiple>");
		   for(AppointmentType apmt:apmtlist){
			   buffer.append("    <option value='"+apmt.getName()+"'>"+apmt.getName()+"</option>");
		   }
		     buffer.append("</select>");
		    session.setAttribute("apmttype", apmtlist); */
		    String dr=chargesReportForm.getDoctor();
		    if(dr==null){
		    	dr="";
		    }
		    String opdipd= chargesReportForm.getIpdopd();
		    if(opdipd==null){
		    	opdipd="";
		    }
		    String clientid=chargesReportForm.getClientid();
		    if(clientid==null){
		    	clientid="";
		    }
		    apmttype=		aptname;
		    if(apmttype==null){
		    	apmttype="";
		    }
		    if(apmttype.equals("0")){
		    	apmttype="";
		    }
		   ArrayList<Client> clintlist=new ArrayList<Client>(); 
		   ChargesReportDAO chargesReportDAO = new JDBCChargesReportDAO(connection);
		   reportlist= chargesReportDAO.getChargesReportDeatiled(fromDate, toDate, "", "", "", "", "", "",apmttype,ChargesType,dr,opdipd,clientid,invsearch,chargesReportForm.getCondition());
		   clintlist= chargesReportDAO.getallInvoicedListOfClient(fromDate, toDate);
		   chargesReportForm.setInvoicedclients(clintlist);
		   chargesReportForm.setList(reportlist);
		      chargesReportForm.setApmtlist(apmtlist);
		   chargesReportForm.setApmtType(chargesReportForm.getApmtType());
		   fromDate= DateTimeUtils.getCommencingDate1(fromDate);
		   toDate= DateTimeUtils.getCommencingDate1(toDate);
		   chargesReportForm.setFromDate(fromDate);
		   chargesReportForm.setToDate(toDate);
		   chargesReportForm.setSearchinv(invsearch);
		   NotAvailableSlotDAO notAvailableSlotDAO= new  JDBCNotAvailableSlotDAO(connection);
			ArrayList<DiaryManagement>userList = notAvailableSlotDAO.getUserList(loginInfo.getId());
			chargesReportForm.setDoctorList(userList);
			double totalamt=0;
			if(reportlist.size()>0){
		    totalamt=reportlist.get(reportlist.size()-1).getTotalrecammount();
			}
		   chargesReportForm.setDebitTotal(Math.round(totalamt));
		   //letterhead
		   ClinicDAO clinicDAO= new JDBCClinicDAO(connection);
		   Clinic clinic = new Clinic();
		   clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
		   
		   chargesReportForm.setClinicName(clinic.getClinicName());
		   chargesReportForm.setClinicOwner(clinic.getClinicOwner());
		   chargesReportForm.setOwner_qualification(clinic.getOwner_qualification());
		  
		   chargesReportForm.setAddress(clinic.getAddress());
		   chargesReportForm.setLandLine(clinic.getLandLine());
		   chargesReportForm.setClinicemail(clinic.getEmail());
		   chargesReportForm.setWebsiteUrl(clinic.getWebsiteUrl());
		   chargesReportForm.setClinicLogo(clinic.getUserImageFileName());

		   
		   
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
		  
		  return "newDetailed";
		 }
	
//	lokesh
public String paymentreceivedreport(){
	Connection connection= null;
	try{
		String fromDate = chargesReportForm.getFromDate();
		String toDate = chargesReportForm.getToDate();
	
		if(fromDate==null){
			fromDate="";
		}
		if(toDate==null){
			toDate="";
		}	
		
	
		if(fromDate.equals("")){
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.DATE, -7); 
			fromDate = dateFormat.format(cal.getTime());
			chargesReportForm.setFromDate(fromDate);
		}
		if(toDate.equals("")){
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.DATE, -7); 
			toDate = dateFormat.format(cal.getTime());
			chargesReportForm.setToDate(toDate);
		}
		
		String howpaid= chargesReportForm.getHowpaid();
		if(howpaid== null){
			howpaid="";
		}
		if(howpaid.equals("0")){
			howpaid="";
		}
		
		String user= chargesReportForm.getUserid();
		if(user==null){
			user="";
		}
		if(user.equals("0")){
			user="";
		}
		
		 String invoicetype= chargesReportForm.getInvcetype();
		 if(invoicetype==null){
			 invoicetype="";
			}
			if(invoicetype.equals("0")){
				invoicetype="";
			}
		 
		connection= Connection_provider.getconnection();
		ChargesReportDAO chargesReportDAO= new JDBCChargesReportDAO(connection);
		ArrayList<Accounts> paymentrecivedList= new ArrayList<Accounts>();
		paymentrecivedList= chargesReportDAO.getpaymentreciptlist(fromDate, toDate, howpaid, user, invoicetype);
		chargesReportForm.setPaymentrecivedList(paymentrecivedList);
		UserProfileDAO userProfileDAO=new JDBCUserProfileDAO(connection);
		ArrayList<UserProfile> userProfileList= userProfileDAO.getAllUserProfileList();
		chargesReportForm.setUserProfileList(userProfileList);
		int refund=0;
		int advnce=0;
		int ipd=0;
//		refund= chargesReportDAO.getcountofinvoice(fromDate, toDate, howpaid, user, "Refund");
		advnce=chargesReportDAO.getcountofinvoice(fromDate, toDate, howpaid, user, "Advance");
		ipd=chargesReportDAO.getcountofinvoice(fromDate, toDate, howpaid, user, "IPD");
//		if(invoicetype.equals("Refund")){
//			chargesReportForm.setTotalamount("Total :"+refund);
//		}
//		else
		if(invoicetype.equals("Advance")){
			chargesReportForm.setTotalamount("Total :"+advnce);
		}
		else if(invoicetype.equals("IPD")){
			chargesReportForm.setTotalamount("Total :"+ipd);
		}
		else{
			chargesReportForm.setTotalamount("Total :"+((advnce+ipd)));
		}
		chargesReportForm.setTotaladvnce(advnce);
		chargesReportForm.setTotalipd(ipd);
		chargesReportForm.setTotalrefund(refund);
		
		   //letterhead
		   ClinicDAO clinicDAO= new JDBCClinicDAO(connection);
		   Clinic clinic = new Clinic();
		   clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
		   
		   chargesReportForm.setClinicName(clinic.getClinicName());
		   chargesReportForm.setClinicOwner(clinic.getClinicOwner());
		   chargesReportForm.setOwner_qualification(clinic.getOwner_qualification());
		  
		   chargesReportForm.setAddress(clinic.getAddress());
		   chargesReportForm.setLandLine(clinic.getLandLine());
		   chargesReportForm.setClinicemail(clinic.getEmail());
		   chargesReportForm.setWebsiteUrl(clinic.getWebsiteUrl());
		   chargesReportForm.setClinicLogo(clinic.getUserImageFileName());
	}catch (Exception e) {
		e.printStackTrace();
	}
	
	return"paymentreceivedreport";
}

public String paymentreportNotes(){
	try {
		String fromdate=DateTimeUtils.isNull(chargesReportForm.getFromDate());
		String todate=DateTimeUtils.isNull(chargesReportForm.getToDate());
		String status=chargesReportForm.getInvoiceStatus();
		if(fromdate.equals("")){
			  DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			    Calendar cal = Calendar.getInstance();
			   
			    fromdate = dateFormat.format(cal.getTime());
			    todate=fromdate;
		}
		ArrayList<Accounts> noteList=new ArrayList<Accounts>();
		Connection connection= Connection_provider.getconnection();
		SummaryReportDAO summaryReportDAO= new JDBCSummaryReportDAO(connection);
		noteList=summaryReportDAO.getPaymentRepportNoteList(fromdate, todate, status, loginInfo);
		chargesReportForm.setList(noteList);
		chargesReportForm.setFromDate(fromdate);
		chargesReportForm.setToDate(todate);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return "paymentreportNotesdash";
}

public String setpaymentreportnotesstatus(){
	try {
		Connection connection= Connection_provider.getconnection();
		String id=DateTimeUtils.isNull(request.getParameter("id"));
		String status=DateTimeUtils.isNull(request.getParameter("status"));
		UserProfileDAO userProfileDAO= new JDBCUserProfileDAO(connection);
		
		 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Calendar cal = Calendar.getInstance();
		 String   date = dateFormat.format(cal.getTime());
		    
		int x= userProfileDAO.updateStatusUserPaymentReportNotes(loginInfo, status, date, id,null);
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write("");
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}

public String payment_report_combinned(){
	try {
		
		String fromDate=DateTimeUtils.isNull(chargesReportForm.getFromDate());
		String toDate= DateTimeUtils.isNull(chargesReportForm.getToDate());
		if(fromDate.equals("")){
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();
			fromDate = dateFormat.format(cal.getTime());
			toDate=dateFormat.format(cal.getTime());
		}
		chargesReportForm.setFromDate(fromDate);
		chargesReportForm.setToDate(toDate);
		fromDate=DateTimeUtils.getCommencingDate1(fromDate);
		toDate=DateTimeUtils.getCommencingDate1(toDate);
		
		Connection connection= Connection_provider.getconnection();
		SummaryReportDAO summaryReportDAO= new JDBCSummaryReportDAO(connection);
		ChargesReportDAO chargesReportDAO= new JDBCChargesReportDAO(connection);
		double totalcashtotal=0,totalcheqtotal=0,totaldcrdtotal=0,totalnefttotal=0,totalothertotal=0,totalstotal=0;
		//int itype, String payby, String howpaid
		//how paid= Cash,D/Card,NEFT,Cheque
		ArrayList<Accounts> mainlist= new ArrayList<Accounts>();
		ArrayList<Accounts>opdpaywiselist = chargesReportDAO.getPaymentModeWiseList(fromDate,toDate,1,
				chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),"","0");
		double cashtotal=0,cheqtotal=0,dcrdtotal=0,nefttotal=0,othertotal=0;
		for(Accounts accounts : opdpaywiselist){
			if(accounts.getPaymentmode().equals("Cash")){
				cashtotal = cashtotal + accounts.getAmount();
			}
			if(accounts.getPaymentmode().equals("Cheque")){
				cheqtotal = cheqtotal + accounts.getAmount();
			}
			
			if(accounts.getPaymentmode().equals("D/Card")){
				dcrdtotal = dcrdtotal + accounts.getAmount();
			}
			if(accounts.getPaymentmode().equals("NEFT")){
				nefttotal = nefttotal + accounts.getAmount();
			}
			if(accounts.getPaymentmode().equals("Other")){
				othertotal = othertotal + accounts.getAmount();
			}
		}
		totalcashtotal=cashtotal+totalcashtotal;totalcheqtotal=cheqtotal+totalcheqtotal;totaldcrdtotal=dcrdtotal+totaldcrdtotal;totalnefttotal=totalnefttotal+nefttotal;totalothertotal=othertotal+totalothertotal;totalstotal=(cashtotal+cheqtotal+dcrdtotal+nefttotal+othertotal)+totalstotal;
		Accounts opdData= new Accounts();
		opdData.setDepartment("OPD");
		opdData.setCashtotal(""+cashtotal);
		opdData.setCheqtotal(""+cheqtotal);
		opdData.setDrtotal(""+dcrdtotal);
		opdData.setNefttotal(""+nefttotal);
		opdData.setOthertotal(""+othertotal);
		opdData.setTotalamt(""+(cashtotal+cheqtotal+dcrdtotal+nefttotal+othertotal));
		mainlist.add(opdData);
		
		//IPD
		ArrayList<Accounts>ipdpaywiselist = chargesReportDAO.getPaymentModeWiseList(fromDate,toDate,2,
				chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),"","0");
		cashtotal=0;cheqtotal=0;dcrdtotal=0;nefttotal=0;othertotal=0;
		for(Accounts accounts : ipdpaywiselist){
			if(accounts.getPaymentmode().equals("Cash")){
				cashtotal = cashtotal + accounts.getAmount();
			}
			if(accounts.getPaymentmode().equals("Cheque")){
				cheqtotal = cheqtotal + accounts.getAmount();
			}
			
			if(accounts.getPaymentmode().equals("D/Card")){
				dcrdtotal = dcrdtotal + accounts.getAmount();
			}
			if(accounts.getPaymentmode().equals("NEFT")){
				nefttotal = nefttotal + accounts.getAmount();
			}
			if(accounts.getPaymentmode().equals("Other")){
				othertotal = othertotal + accounts.getAmount();
			}
		}
		totalcashtotal=cashtotal+totalcashtotal;totalcheqtotal=cheqtotal+totalcheqtotal;totaldcrdtotal=dcrdtotal+totaldcrdtotal;totalnefttotal=totalnefttotal+nefttotal;totalothertotal=othertotal+totalothertotal;totalstotal=(cashtotal+cheqtotal+dcrdtotal+nefttotal+othertotal)+totalstotal;
		
		Accounts ipdData= new Accounts();
		ipdData.setDepartment("IPD");
		ipdData.setCashtotal(""+cashtotal);
		ipdData.setCheqtotal(""+cheqtotal);
		ipdData.setDrtotal(""+dcrdtotal);
		ipdData.setNefttotal(""+nefttotal);
		ipdData.setOthertotal(""+othertotal);
		ipdData.setTotalamt(""+(cashtotal+cheqtotal+dcrdtotal+nefttotal+othertotal));
		mainlist.add(ipdData);
		
		
		//Investigation
		ArrayList<Accounts>invstpaywiselist = chargesReportDAO.getPaymentModeWiseList(fromDate,toDate,3,
				chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),"","0");
		cashtotal=0;cheqtotal=0;dcrdtotal=0;nefttotal=0;othertotal=0;
		for(Accounts accounts : invstpaywiselist){
			if(accounts.getPaymentmode().equals("Cash")){
				cashtotal = cashtotal + accounts.getAmount();
			}
			if(accounts.getPaymentmode().equals("Cheque")){
				cheqtotal = cheqtotal + accounts.getAmount();
			}
			
			if(accounts.getPaymentmode().equals("D/Card")){
				dcrdtotal = dcrdtotal + accounts.getAmount();
			}
			if(accounts.getPaymentmode().equals("NEFT")){
				nefttotal = nefttotal + accounts.getAmount();
			}
			if(accounts.getPaymentmode().equals("Other")){
				othertotal = othertotal + accounts.getAmount();
			}
		}
		totalcashtotal=cashtotal+totalcashtotal;totalcheqtotal=cheqtotal+totalcheqtotal;totaldcrdtotal=dcrdtotal+totaldcrdtotal;totalnefttotal=totalnefttotal+nefttotal;totalothertotal=othertotal+totalothertotal;totalstotal=(cashtotal+cheqtotal+dcrdtotal+nefttotal+othertotal)+totalstotal;
		
		Accounts invstData= new Accounts();
		invstData.setDepartment("Investigation");
		invstData.setCashtotal(""+cashtotal);
		invstData.setCheqtotal(""+cheqtotal);
		invstData.setDrtotal(""+dcrdtotal);
		invstData.setNefttotal(""+nefttotal);
		invstData.setOthertotal(""+othertotal);
		invstData.setTotalamt(""+(cashtotal+cheqtotal+dcrdtotal+nefttotal+othertotal));
		mainlist.add(invstData);
		
		
		//Advance
		
		ArrayList<Accounts>advrefpaywiselist = chargesReportDAO.getAdvPaymentModeWiseList(fromDate,toDate,4,
				chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),chargesReportForm.getInvoicecategory(),"0");
		cashtotal=0;cheqtotal=0;dcrdtotal=0;nefttotal=0;othertotal=0;
		for(Accounts accounts : advrefpaywiselist){
			if(accounts.getPaymentmode().equals("Cash")){
				cashtotal = cashtotal + accounts.getAmount();
			}
			if(accounts.getPaymentmode().equals("Cheque")){
				cheqtotal = cheqtotal + accounts.getAmount();
			}
			
			if(accounts.getPaymentmode().equals("D/Card")){
				dcrdtotal = dcrdtotal + accounts.getAmount();
			}
			if(accounts.getPaymentmode().equals("NEFT")){
				nefttotal = nefttotal + accounts.getAmount();
			}
			if(accounts.getPaymentmode().equals("Other")){
				othertotal = othertotal + accounts.getAmount();
			}
		}
		totalcashtotal=cashtotal+totalcashtotal;totalcheqtotal=cheqtotal+totalcheqtotal;totaldcrdtotal=dcrdtotal+totaldcrdtotal;totalnefttotal=totalnefttotal+nefttotal;totalothertotal=othertotal+totalothertotal;totalstotal=(cashtotal+cheqtotal+dcrdtotal+nefttotal+othertotal)+totalstotal;
		
		Accounts advData= new Accounts();
		advData.setDepartment("Advance");
		advData.setCashtotal(""+cashtotal);
		advData.setCheqtotal(""+cheqtotal);
		advData.setDrtotal(""+dcrdtotal);
		advData.setNefttotal(""+nefttotal);
		advData.setOthertotal(""+othertotal);
		advData.setTotalamt(""+(cashtotal+cheqtotal+dcrdtotal+nefttotal+othertotal));
		mainlist.add(advData);
		
		
		//Medicine
				ArrayList<Accounts>medpaywiselist = chargesReportDAO.getPaymentModeWiseList(fromDate,toDate,4,
						chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),"","0");
				cashtotal=0;cheqtotal=0;dcrdtotal=0;nefttotal=0;othertotal=0;
				for(Accounts accounts : medpaywiselist){
					if(accounts.getPaymentmode().equals("Cash")){
						cashtotal = cashtotal + accounts.getAmount();
					}
					if(accounts.getPaymentmode().equals("Cheque")){
						cheqtotal = cheqtotal + accounts.getAmount();
					}
					
					if(accounts.getPaymentmode().equals("D/Card")){
						dcrdtotal = dcrdtotal + accounts.getAmount();
					}
					if(accounts.getPaymentmode().equals("NEFT")){
						nefttotal = nefttotal + accounts.getAmount();
					}
					if(accounts.getPaymentmode().equals("Other")){
						othertotal = othertotal + accounts.getAmount();
					}
				}
				totalcashtotal=cashtotal+totalcashtotal;totalcheqtotal=cheqtotal+totalcheqtotal;totaldcrdtotal=dcrdtotal+totaldcrdtotal;totalnefttotal=totalnefttotal+nefttotal;totalothertotal=othertotal+totalothertotal;totalstotal=(cashtotal+cheqtotal+dcrdtotal+nefttotal+othertotal)+totalstotal;
				
				Accounts medData= new Accounts();
				medData.setDepartment("Medicine");
				medData.setCashtotal(""+cashtotal);
				medData.setCheqtotal(""+cheqtotal);
				medData.setDrtotal(""+dcrdtotal);
				medData.setNefttotal(""+nefttotal);
				medData.setOthertotal(""+othertotal);
				medData.setTotalamt(""+(cashtotal+cheqtotal+dcrdtotal+nefttotal+othertotal));
				mainlist.add(medData);
				
				
				
				//Vaccination
				ArrayList<Accounts>vaccinationList = chargesReportDAO.getPaymentModeWiseList(fromDate,toDate,6,
						chargesReportForm.getPayby(),chargesReportForm.getHowpaid(),"","0");
				cashtotal=0;cheqtotal=0;dcrdtotal=0;nefttotal=0;othertotal=0;
				for(Accounts accounts : vaccinationList){
					if(accounts.getPaymentmode().equals("Cash")){
						cashtotal = cashtotal + accounts.getAmount();
					}
					if(accounts.getPaymentmode().equals("Cheque")){
						cheqtotal = cheqtotal + accounts.getAmount();
					}
					
					if(accounts.getPaymentmode().equals("D/Card")){
						dcrdtotal = dcrdtotal + accounts.getAmount();
					}
					if(accounts.getPaymentmode().equals("NEFT")){
						nefttotal = nefttotal + accounts.getAmount();
					}
					if(accounts.getPaymentmode().equals("Other")){
						othertotal = othertotal + accounts.getAmount();
					}
				}
				totalcashtotal=cashtotal+totalcashtotal;totalcheqtotal=cheqtotal+totalcheqtotal;totaldcrdtotal=dcrdtotal+totaldcrdtotal;totalnefttotal=totalnefttotal+nefttotal;totalothertotal=othertotal+totalothertotal;totalstotal=(cashtotal+cheqtotal+dcrdtotal+nefttotal+othertotal)+totalstotal;
				
				Accounts vaccData= new Accounts();
				vaccData.setDepartment("Vaccination");
				vaccData.setCashtotal(""+cashtotal);
				vaccData.setCheqtotal(""+cheqtotal);
				vaccData.setDrtotal(""+dcrdtotal);
				vaccData.setNefttotal(""+nefttotal);
				vaccData.setOthertotal(""+othertotal);
				vaccData.setTotalamt(""+(cashtotal+cheqtotal+dcrdtotal+nefttotal+othertotal));
				mainlist.add(vaccData);
				
			
			
				
				//Total of all
				Accounts totalData= new Accounts();
				totalData.setDepartment("Total");
				totalData.setCashtotal(""+totalcashtotal);
				totalData.setCheqtotal(""+totalcheqtotal);
				totalData.setDrtotal(""+totaldcrdtotal);
				totalData.setNefttotal(""+totalnefttotal);
				totalData.setOthertotal(""+totalothertotal);
				totalData.setTotalamt(""+(totalstotal));
				mainlist.add(totalData);
				
		
		
		
		//ipd Refund
		ArrayList<Accounts>	ipdRefundList= chargesReportDAO.getRefundPaymentReportListIpdOpd(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),"","0",1,"0","");
		double ipdref=0,opdref=0;
		cashtotal=0;cheqtotal=0;dcrdtotal=0;nefttotal=0;othertotal=0;
		for(Accounts accounts:ipdRefundList){
			ipdref=ipdref+Double.parseDouble(accounts.getDebitTotalx());
			
		
				if(accounts.getPaymentmode().equals("Cash")){
					cashtotal = cashtotal + Double.parseDouble(accounts.getDebitTotalx());;
				}
				if(accounts.getPaymentmode().equals("Cheque")){
					cheqtotal = cheqtotal + Double.parseDouble(accounts.getDebitTotalx());;
				}
				
				if(accounts.getPaymentmode().equals("D/Card")){
					dcrdtotal = dcrdtotal + Double.parseDouble(accounts.getDebitTotalx());;
				}
				if(accounts.getPaymentmode().equals("NEFT")){
					nefttotal = nefttotal + Double.parseDouble(accounts.getDebitTotalx());
				}
				if(accounts.getPaymentmode().equals("Other")){
					othertotal = othertotal + Double.parseDouble(accounts.getDebitTotalx());
				}
			
		}
		Accounts ipdrefData= new Accounts();
		ipdrefData.setDepartment("IPD Refund");
		ipdrefData.setCashtotal(""+cashtotal);
		ipdrefData.setCheqtotal(""+cheqtotal);
		ipdrefData.setDrtotal(""+dcrdtotal);
		ipdrefData.setNefttotal(""+nefttotal);
		ipdrefData.setOthertotal(""+othertotal);
		ipdrefData.setTotalamt(""+(cashtotal+cheqtotal+dcrdtotal+nefttotal+othertotal));
		mainlist.add(ipdrefData);
		
		//opd Refund
		ArrayList<Accounts>	opdRefundList= chargesReportDAO.getRefundPaymentReportListIpdOpd(fromDate,toDate,chargesReportForm.getPayby(),chargesReportForm.getPaymentStatus(),chargesReportForm.getThirdParty(),chargesReportForm.getOrderby(),chargesReportForm.getOrder(),chargesReportForm.getInvoicecategory(),"","0",0,"0","");
		cashtotal=0;cheqtotal=0;dcrdtotal=0;nefttotal=0;othertotal=0;
		for(Accounts accounts:opdRefundList){
			//opdref=opdref+Double.parseDouble(acc.getDebitTotalx());
			if(accounts.getPaymentmode().equals("Cash")){
				cashtotal = cashtotal + Double.parseDouble(accounts.getDebitTotalx());;
			}
			if(accounts.getPaymentmode().equals("Cheque")){
				cheqtotal = cheqtotal + Double.parseDouble(accounts.getDebitTotalx());;
			}
			
			if(accounts.getPaymentmode().equals("D/Card")){
				dcrdtotal = dcrdtotal + Double.parseDouble(accounts.getDebitTotalx());;
			}
			if(accounts.getPaymentmode().equals("NEFT")){
				nefttotal = nefttotal + Double.parseDouble(accounts.getDebitTotalx());
			}
			if(accounts.getPaymentmode().equals("Other")){
				othertotal = othertotal + Double.parseDouble(accounts.getDebitTotalx());
			}
		}
		
		Accounts opdrefData= new Accounts();
		opdrefData.setDepartment("OPD Refund");
		opdrefData.setCashtotal(""+cashtotal);
		opdrefData.setCheqtotal(""+cheqtotal);
		opdrefData.setDrtotal(""+dcrdtotal);
		opdrefData.setNefttotal(""+nefttotal);
		opdrefData.setOthertotal(""+othertotal);
		opdrefData.setTotalamt(""+(cashtotal+cheqtotal+dcrdtotal+nefttotal+othertotal));
		
		chargesReportForm.setTotalref(""+((Double.parseDouble(opdrefData.getTotalamt()))+Double.parseDouble(ipdrefData.getTotalamt())));
		
		mainlist.add(opdrefData);
		
		
		Accounts totalRefData= new Accounts();
		totalRefData.setDepartment("Refund Total");
		totalRefData.setCashtotal(""+((Double.parseDouble(opdrefData.getCashtotal()))+Double.parseDouble(ipdrefData.getCashtotal())));
		totalRefData.setCheqtotal(""+((Double.parseDouble(opdrefData.getCheqtotal()))+Double.parseDouble(ipdrefData.getCheqtotal())));
		totalRefData.setDrtotal(""+((Double.parseDouble(opdrefData.getDrtotal()))+Double.parseDouble(ipdrefData.getDrtotal())));
		totalRefData.setNefttotal(""+((Double.parseDouble(opdrefData.getNefttotal()))+Double.parseDouble(ipdrefData.getNefttotal())));
		totalRefData.setOthertotal(""+((Double.parseDouble(opdrefData.getOthertotal()))+Double.parseDouble(ipdrefData.getOthertotal())));
		totalRefData.setTotalamt(chargesReportForm.getTotalref());
		
		mainlist.add(totalRefData);
		
		
		Accounts finalData= new Accounts();
		finalData.setDepartment("Final Total");
		finalData.setCashtotal(""+((Double.parseDouble(totalData.getCashtotal()))-Double.parseDouble(totalRefData.getCashtotal())));
		finalData.setCheqtotal(""+((Double.parseDouble(totalData.getCheqtotal()))-Double.parseDouble(totalRefData.getCheqtotal())));
		finalData.setDrtotal(""+((Double.parseDouble(totalData.getDrtotal()))-Double.parseDouble(totalRefData.getDrtotal())));
		finalData.setNefttotal(""+((Double.parseDouble(totalData.getNefttotal()))-Double.parseDouble(totalRefData.getNefttotal())));
		finalData.setOthertotal(""+((Double.parseDouble(totalData.getOthertotal()))-Double.parseDouble(totalRefData.getOthertotal())));
		finalData.setTotalamt(""+(Double.parseDouble(totalData.getTotalamt())-(Double.parseDouble(totalRefData.getTotalamt()))));
		
		mainlist.add(finalData);
		
		chargesReportForm.setList(mainlist);
		
		

		   //letterhead
		   ClinicDAO clinicDAO= new JDBCClinicDAO(connection);
		   Clinic clinic = new Clinic();
		   clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
		   
		   chargesReportForm.setClinicName(clinic.getClinicName());
		   chargesReportForm.setClinicOwner(clinic.getClinicOwner());
		   chargesReportForm.setOwner_qualification(clinic.getOwner_qualification());
		  
		   chargesReportForm.setAddress(clinic.getAddress());
		   chargesReportForm.setLandLine(clinic.getLandLine());
		   chargesReportForm.setClinicemail(clinic.getEmail());
		   chargesReportForm.setWebsiteUrl(clinic.getWebsiteUrl());
		   chargesReportForm.setClinicLogo(clinic.getUserImageFileName());
	} catch (Exception e) {
		e.printStackTrace();
	}
	return "payment_report_combinned";
}


}
