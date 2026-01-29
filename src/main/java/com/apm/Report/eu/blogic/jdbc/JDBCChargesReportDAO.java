package com.apm.Report.eu.blogic.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.struts2.convention.annotation.Results;

import com.apm.Accounts.eu.bi.AccountsDAO;
import com.apm.Accounts.eu.bi.AdditionalDAO;
import com.apm.Accounts.eu.bi.StatementDAO;
import com.apm.Accounts.eu.blogic.jdbc.JDBCAccountsDAO;
import com.apm.Accounts.eu.blogic.jdbc.JDBCAdditionalDAO;
import com.apm.Accounts.eu.blogic.jdbc.JDBCStatementDAO;
import com.apm.Accounts.eu.entity.Accounts;
import com.apm.DiaryManagement.eu.bi.ClientDAO;
import com.apm.DiaryManagement.eu.bi.CompleteAptmDAO;
import com.apm.DiaryManagement.eu.bi.DiaryManagementDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCClientDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCCompleteAptmDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCDiaryManagentDAO;
import com.apm.DiaryManagement.eu.entity.Client;
import com.apm.DiaryManagement.eu.entity.CompleteAppointment;
import com.apm.DiaryManagement.eu.entity.NotAvailableSlot;
import com.apm.Ipd.eu.bi.BedDao;
import com.apm.Ipd.eu.bi.IpdDAO;
import com.apm.Ipd.eu.blogic.jdbc.JDBCBedDao;
import com.apm.Ipd.eu.blogic.jdbc.JDBCIpdDAO;
import com.apm.Ipd.eu.entity.Bed;
import com.apm.Master.eu.entity.Master;
import com.apm.Registration.eu.bi.UserProfileDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCUserProfileDAO;
import com.apm.Registration.eu.entity.UserProfile;
import com.apm.Report.eu.bi.ChargesReportDAO;
import com.apm.Report.eu.entity.ChargesReport;
import com.apm.Report.eu.entity.MisReport;
import com.apm.ThirdParties.eu.bi.ThirdPartyDAO;
import com.apm.ThirdParties.eu.blogic.jdbc.JDBCThirdPartyDAO;
import com.apm.ThirdParties.eu.entity.ThirdParty;
import com.apm.common.eu.blogic.jdbc.JDBCBaseDAO;
import com.apm.common.utils.DateTimeUtils;
import com.apm.main.common.constants.Constants;


public class JDBCChargesReportDAO extends JDBCBaseDAO implements ChargesReportDAO{
	
	public JDBCChargesReportDAO(Connection connection){
		this.connection = connection;
	}

	public ArrayList<Accounts> getChargesReportList(String fromDate,String toDate,String payby,String tpid,String invoiceStatus,String orderby,String order,String jobtitle) {
		
		double creditTotal = 0.0;
		double balanceTotal = 0.0;
		DateTimeUtils dateTimeUtils = new DateTimeUtils();
		PreparedStatement preparedStatement = null;
		ArrayList<Accounts> list = new ArrayList<Accounts>();
		
		StringBuffer sql = new StringBuffer();
		
		
		
		if(!fromDate.equals("") && !toDate.equals("")){
			
			sql.append("SELECT commencing,user,apmtType,sum(charge*quantity),invoiceid,paid,count(apm_invoice_assesments.id),apm_invoice.practitionerid,apm_invoice_assesments.paybuy,apm_invoice_assesments.clientId,chargetype,howpaid,apm_invoice.appointmentid,department,apm_invoice_assesments.thirdPartyId,wardid,abrivationid  FROM apm_invoice_assesments ");
			sql.append("inner join apm_invoice on apm_invoice.id = apm_invoice_assesments.invoiceid ");
			//sql.append("inner join apm_third_party_details on apm_third_party_details.id = apm_invoice_assesments.thirdPartyId ");
			sql.append("inner join apm_patient on apm_patient.id = apm_invoice_assesments.clientId ");
			sql.append("where  commencing between '"+fromDate+"' AND '"+toDate+"' ");
			sql.append("and apm_invoice_assesments.active=1  ");
		}else{
			sql.append("SELECT commencing,user,apmtType,sum(charge*quantity),invoiceid,paid,count(apm_invoice_assesments.id),apm_invoice.practitionerid,apm_invoice_assesments.paybuy,apm_invoice_assesments.clientId,chargetype,howpaid,apm_invoice.appointmentid,department,apm_invoice_assesments.thirdPartyId,wardid,abrivationid  FROM apm_invoice_assesments ");
			sql.append("inner join apm_invoice on apm_invoice.id = apm_invoice_assesments.invoiceid ");
			sql.append("where apm_invoice_assesments.active=1  ");		
		}
		
		
		
		if(payby!=null){
			if(!payby.equals("All")){
				
			if(!payby.equals("0")){
				sql = new StringBuffer();
				if(payby.equals(Constants.PAY_BY_THIRD_PARTY)){
					payby = "1";
				}else if(payby.equals(Constants.PAY_BY_CLIENT)){
					payby = "0";
				}else{
					payby="";
				}
			}
				
				if(!fromDate.equals("") && !toDate.equals("")){
					sql = new StringBuffer();
					sql.append("SELECT commencing,user,apmtType,sum(charge*quantity),invoiceid,paid,count(apm_invoice_assesments.id),apm_invoice.practitionerid,apm_invoice_assesments.paybuy,apm_invoice_assesments.clientId,chargetype,howpaid,apm_invoice.appointmentid,department,apm_invoice_assesments.thirdPartyId,wardid,abrivationid  FROM apm_invoice_assesments ");
					sql.append("inner join apm_invoice on apm_invoice.id = apm_invoice_assesments.invoiceid ");
					//sql.append("inner join apm_third_party_details on apm_third_party_details.id = apm_invoice_assesments.thirdPartyId ");
					sql.append("inner join apm_patient on apm_patient.id = apm_invoice_assesments.clientId ");
					sql.append("where commencing between '"+fromDate+"' AND '"+toDate+"' ");
					
					if(payby.equals("1") && !tpid.equals("0")){
						
						sql.append("and apm_invoice_assesments.active=1 and apm_invoice_assesments.paybuy='"+payby+"' and apm_invoice_assesments.thirdPartyId="+tpid+"   ");
					}else{
						sql.append("and apm_invoice_assesments.active=1 and apm_invoice_assesments.paybuy='"+payby+"'  ");
					}
					
				}else{
					sql = new StringBuffer();
					sql.append("SELECT commencing,user,apmtType,sum(charge*quantity),invoiceid,paid,count(apm_invoice_assesments.id),apm_invoice.practitionerid,apm_invoice_assesments.paybuy,apm_invoice_assesments.clientId,chargetype,howpaid,apm_invoice.appointmentid,department,apm_invoice_assesments.thirdPartyId,wardid,abrivationid  FROM apm_invoice_assesments ");
					sql.append("inner join apm_invoice on apm_invoice.id = apm_invoice_assesments.invoiceid ");
					
					if(payby.equals("1") && !tpid.equals("0")){
						
						sql.append("where apm_invoice_assesments.active=1 and apm_invoice_assesments.paybuy='"+payby+"' and apm_invoice_assesments.thirdPartyId="+tpid+"   ");
					}else{
						sql.append("where apm_invoice_assesments.active=1 and apm_invoice_assesments.paybuy='"+payby+"'  ");
					}
				}
				
			
				
				
			}
	}
		if(jobtitle!=null){
			
			if(!jobtitle.equals("0")){
				sql.append(" and department='"+jobtitle+"' group by invoiceid ");
			}else{
				sql.append(" group by invoiceid ");
			}
		}else{
			
			sql.append(" group by invoiceid ");
		}
			
		
		
		sql.append("order by "+orderby+" "+order+" ");
		
		String sql1 = sql.toString();
		
		try{
			preparedStatement = connection.prepareStatement(sql1);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				double debitTotal=0.0;
				Accounts accounts = new Accounts();
				accounts.setCommencing(DateTimeUtils.getCommencingDate1(rs.getString(1)));
				accounts.setClientName(rs.getString(2));
				accounts.setAppointmentType(rs.getString(3));
				accounts.setCreditCharge(rs.getString(4));
				accounts.setInvoiceid(rs.getInt(5));
				accounts.setPaid(rs.getBoolean(6));
				
				accounts.setNumberOfChages(rs.getInt(7));
				String practitionerFullName = getPractitionerFullName(rs.getString(8));
				accounts.setPractitionerName(practitionerFullName);
				accounts.setPayby(rs.getString(9));
				if(rs.getString(9).equals("1")){
					accounts.setWhoPay("Third Party");
					
					CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(connection);
					CompleteAppointment completeAppointment =  completeAptmDAO.getInsuranceCompanyName(rs.getString(10));
					String tpName = completeAppointment.getInsuranceCompanyName();
					accounts.setTpName(tpName);
					
				}
				else{
					accounts.setWhoPay("Self");
				}
				accounts.setClientid(rs.getInt(10));
				accounts.setChargeType(rs.getString(11));
				accounts.setHowPaid(rs.getString(12));
				accounts.setApptId(rs.getString(13));
				
				String locationName = getChargeDepartmentName(rs.getInt(8));
				accounts.setLocationName(locationName);
				
				String treamentEpisodeName = getTreatmentEpisodeName(rs.getString(13));
				accounts.setTreatmentEpisodeName(treamentEpisodeName);
				accounts.setLocationName(locationName);
				
				
				
				
				
				boolean chargesInvoiced = checkChargeInvoiced(accounts.getInvoiceid());
				accounts.setChargesInvoiced(chargesInvoiced);
				
				
				//if(chargesInvoiced==false){
					double credit = 0.0;
					debitTotal =  rs.getDouble(4); 
			/*		int credit = getCreditAmount(rs.getString(5),rs.getString(9));
					int totalPayment = getPaymentAmount(rs.getInt(5),rs.getString(9));
					int debit = totalPayment - credit;
					accounts.setPayAmount(credit);
					accounts.setTotalAmount(credit+debit);
					double total = credit+debit;
					
					accounts.setDebitAmount(debit);
					
					debitTotal = total + debitTotal;
					creditTotal = creditTotal + credit;
					balanceTotal = balanceTotal + debit;
					accounts.setDebitTotal(debitTotal);
					accounts.setCreditTotal(creditTotal);
					accounts.setBalanceTotal(balanceTotal);*/
					
					accounts.setDebitTotal(debitTotal);
					
					accounts.setDebitTotalx(dateTimeUtils.changeFormat(debitTotal));
					accounts.setCreditTotalx(dateTimeUtils.changeFormat(credit));
					accounts.setBalanceTotalx(dateTimeUtils.changeFormat(debitTotal));
					BedDao bedDao=new JDBCBedDao(connection);
				//}
				String wardname=bedDao.getWardName(rs.getString(16));
				accounts.setWard(wardname);
				accounts.setAbrivationid(rs.getString(17));
				if(invoiceStatus.equals("Invoiced")){
					if(chargesInvoiced){
						list.add(accounts);
					}
				}else if(invoiceStatus.equals("Not Invoiced")){
					if(!chargesInvoiced){
						list.add(accounts);
					}
				}else{
					list.add(accounts);
				}
				
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private String getChargeDepartmentName(int id) {
		PreparedStatement preparedStatement = null;
		String result = "";
		//String sql = "SELECT discipline FROM apm_discipline where id = "+id+" ";
		String sql = "select discipline from apm_user inner join  apm_discipline "
				+ " on apm_discipline.id = apm_user.discription where apm_user.id = "+id+" ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				result = rs.getString(1);
			}
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	private int getPaymentAmount(int invoiceid, String paybuy) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "SELECT sum(charge) FROM apm_invoice_assesments where invoiceid = "+invoiceid+" and paybuy = "+paybuy+" and active=1";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				result = rs.getInt(1);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	private int getCreditAmount(String invoiceid, String payBuy) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "SELECT sum(amount) FROM apm_invoice_payment where invoice_id = "+invoiceid+" and payBuy = "+payBuy+" ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				result = rs.getInt(1);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	private boolean checkChargeInvoiced(int invoiceid) {
		PreparedStatement preparedStatement = null;
		boolean result = false;
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT apm_invoice.id FROM apm_invoice inner join apm_charges_assesment on ");
		sql.append("apm_invoice.id =  apm_charges_assesment.invoiceid where apm_invoice.id= "+invoiceid+" ");
		
		
		
		try{
			
			preparedStatement = connection.prepareStatement(sql.toString());
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				result = true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	private String getTreatmentEpisodeName(String id) {
		String treatmentEpsodeId = null;
		String treatmentEpsodeName = null;
		PreparedStatement preparedStatement = null;
		String zero = "0";
		String sql = "select treatmentEpisodeId from apm_available_slot where id = "+id+"";
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				
				treatmentEpsodeId = rs.getString(1);
				if(treatmentEpsodeId.equals(zero)){
					treatmentEpsodeName = "No Treatment Episode";
				}
				else{
				treatmentEpsodeName = getTreatmentEpisode(treatmentEpsodeId);
				}
				}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return treatmentEpsodeName;
	}

	private String getTreatmentEpisode(String treatmentEpsodeId) {
		String treatmentEpsodeName = null;
		PreparedStatement preparedStatement = null;
		
		String sql = "select name from apm_treatment_episode where id = "+treatmentEpsodeId+"";
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				
				treatmentEpsodeName = rs.getString(1);
				
			
			}	
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return treatmentEpsodeName;
	}

	private String getLocationName(int location) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT location FROM apm_clinic_location where id = "+location+" ";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				result = rs.getString(1);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	private String getPractitionerFullName(String id) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT initial,firstname,lastname FROM apm_user where id= "+id+" ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				result = rs.getString(1) + " " + rs.getString(2) + " " +rs.getString(3);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	
	public ArrayList<Accounts> getAuditorsInvoiceReportList(String fromDate,String toDate,
			String payby,String paymentStatus,String thirdparty,String orderby,String order,String invcategory,String invcetype) {
		PreparedStatement preparedStatement = null;
		ArrayList<Accounts>list = new ArrayList<Accounts>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT apm_charges_invoice.id,payby,date,apm_charges_invoice.clientid,debit,deliverstatus,discount,tpid,firstname,resetinv,disctype,discamt,apm_charges_invoice.ipdid ");
		sql.append("FROM apm_charges_invoice ");
		sql.append("inner join apm_patient on apm_patient.id = apm_charges_invoice.clientid ");
		sql.append("where itype="+invcetype+" and date between '"+fromDate+"' AND '"+toDate+"' ");
		

		if(!invcategory.equals("2")){
			sql.append("and apm_charges_invoice.invpstype="+invcategory+" ");
		}
		
		
		if(payby!=null){
			if(payby.equals("0")){
				orderby = "resetinv";
			}
			if(!payby.equals("0")){
				
				sql = new StringBuffer();
				if(payby.equals(Constants.PAY_BY_CLIENT)){
					
						
					sql.append("SELECT apm_charges_invoice.id,payby,date,apm_charges_invoice.clientid,debit,deliverstatus,discount,tpid,firstname,resetinv,disctype,discamt ,apm_charges_invoice.ipdid ");
					sql.append("FROM apm_charges_invoice ");
					sql.append("inner join apm_patient on apm_patient.id = apm_charges_invoice.clientid ");
					sql.append("where itype="+invcetype+" and payby='Client' and date between '"+fromDate+"' AND '"+toDate+"' ");
					
						
						if(!invcategory.equals("2")){
							sql.append("and apm_charges_invoice.invpstype="+invcategory+" ");
						}
					
				}else{
					if(payby.equals(Constants.PAY_BY_THIRD_PARTY)){
						sql = new StringBuffer();
						
						sql.append("SELECT apm_charges_invoice.id,payby,date,apm_charges_invoice.clientid,debit,deliverstatus,discount,tpid,firstname,resetinv,disctype,discamt,apm_charges_invoice.ipdid ");
						sql.append("FROM apm_charges_invoice ");
						sql.append("inner join apm_patient on apm_patient.id = apm_charges_invoice.clientid ");
						sql.append("where itype="+invcetype+" and payby='Third Party' and date between '"+fromDate+"' AND '"+toDate+"'  ");
							
							if(!invcategory.equals("2")){
								sql.append("and apm_charges_invoice.invpstype="+invcategory+" ");
							}
					}
				
			}
			
		}
			
			
			if(!thirdparty.equals("0")){
				sql = new StringBuffer();
				
				sql = new StringBuffer();
				
				sql.append("SELECT apm_charges_invoice.id,payby,date,apm_charges_invoice.clientid,debit,deliverstatus,discount,tpid,firstname,resetinv,disctype,discamt,apm_charges_invoice.ipdid ");
				sql.append("FROM apm_charges_invoice ");
				sql.append("inner join apm_patient on apm_patient.id = apm_charges_invoice.clientid ");
				sql.append("inner join apm_third_party_details on apm_third_party_details.id = apm_charges_invoice.tpid ");
				sql.append("where itype="+invcetype+" and payby='Third Party' and apm_third_party_details.id="+thirdparty+" and date between '"+fromDate+"' AND '"+toDate+"'  ");
					
					if(!invcategory.equals("2")){
						sql.append("and apm_charges_invoice.invpstype="+invcategory+" ");
					}
				
			}
				
			
	}
		
		
		
		
		
		sql.append("order by "+orderby+" "+order+" ");
		
		 
		try{
			preparedStatement = connection.prepareStatement(sql.toString());
			ResultSet rs = preparedStatement.executeQuery();
			IpdDAO ipdDAO= new JDBCIpdDAO(connection);
			while(rs.next()){
				double balance = 0;
				double result = 0.0;
				double creditAmount = 0;
				
				Accounts accounts = new Accounts();
				accounts.setId(rs.getInt(1));
				accounts.setPayby(rs.getString(2));
				accounts.setDate(DateTimeUtils.getCommencingDate1(rs.getString(3)));
				accounts.setClientid(rs.getInt(4));
				
				double totaldebitamount = 0;
				
				double prepaymentAmount = getPrePaymentAmount(rs.getInt(1));
				double debitamount = rs.getDouble(5);
				
				if(accounts.getPayby().equals(Constants.PAY_BY_CLIENT)){
					totaldebitamount = debitamount - prepaymentAmount;
				}else{
					totaldebitamount = rs.getDouble(5);
				}
				
				accounts.setIpdid(ipdDAO.getipdseqno(rs.getString(13)));
				accounts.setDebitAmount(totaldebitamount);
				accounts.setDebitAmountx(DateTimeUtils.changeFormat(totaldebitamount));
				
				accounts.setDeliverstatus(rs.getString(6));
				
				double debit = rs.getDouble(5);
				double total = totaldebitamount;
				double discount = rs.getDouble(7);
				int disctype=rs.getInt(11);
				double discamt= rs.getDouble(12);
				
				
				double r1 = (total*discount)/100;
				
				if(disctype==0){
					discamt= r1;
				}else{
					r1= discamt;
				}
				
				accounts.setDiscount(rs.getDouble(7));
				accounts.setDiscAmmount(DateTimeUtils.changeFormat(discamt));
				total = total-r1;
				result = result + total;
				
				if(accounts.getPayby().equals(Constants.PAY_BY_CLIENT)){
					creditAmount = getAuditCreditAmount(rs.getDouble(1));
				}else{
					creditAmount = getCreditAmount(rs.getDouble(1));
				}
				
				accounts.setCreditAmount(creditAmount);
				accounts.setCreditCharge(Double.toString(creditAmount));
				accounts.setCreditTotalx(DateTimeUtils.changeFormat(creditAmount));
				
				balance = result - creditAmount;
				if(balance<0){
					balance=0;
				}
				accounts.setBalance(balance);
				accounts.setBalancex(DateTimeUtils.changeFormat(balance));
				
				int noOfCharges = getNoOfCharges(rs.getInt(1));
				accounts.setNumberOfChages(noOfCharges);
				
				String clientname = getClientFullName(rs.getString(4));
				accounts.setClientName(clientname);
				String payee = rs.getString(2);
				String payeename = null;
				if(payee.equalsIgnoreCase("Third Party")){
					payeename = getTpname(rs.getInt(8));
					
				}
				else{
					payeename = clientname;
				}
				accounts.setPayeeName(payeename);
				accounts.setResetinv(rs.getString(10));
				//showing seqno instead of invoice no. Shubham
				AccountsDAO accountsDAO=new JDBCAccountsDAO(connection);
			    String ipdopdseq=accountsDAO.getIpdOpdSeqNoWithType(rs.getInt(1));
			    accounts.setIpdopdseq((ipdopdseq));				
				
				if(paymentStatus!=null){
					if(paymentStatus.equals("Paid")){
						if(balance==0){
							list.add(accounts);
						}
					}else if(paymentStatus.equals("Not Paid")){
						if(balance!=0){
							list.add(accounts);
						}
					}else{
						list.add(accounts);
					}
				}else{
					list.add(accounts);
				}
				
				
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return list;
	}
	
	
	
	
	
	

	private double getPrePaymentAmount(int id) {
		PreparedStatement preparedStatement = null;
		double result = 0;
		String sql = "SELECT sum(payment) FROM apm_charges_payment where chargeinvoiceid = "+id+" and paymode ='prepayment' ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				result = rs.getDouble(1);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	private double getAuditCreditAmount(double id) {
		PreparedStatement preparedStatement = null;
		double result = 0;
		String sql = "SELECT sum(payment) FROM apm_charges_payment where chargeinvoiceid = "+id+" and paymode!='prepayment' ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				result = rs.getDouble(1);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public ArrayList<Accounts> getInvoiceReportList(String fromDate,String toDate,String payby,String paymentStatus,String thirdparty,String orderby,String order,String invcategory,String invoicetype) {
		PreparedStatement preparedStatement = null;
		ArrayList<Accounts>list = new ArrayList<Accounts>();
		StringBuffer sql = new StringBuffer();
		StatementDAO statementDAO = new JDBCStatementDAO(connection);
//		if(!fromDate.equals("") && !toDate.equals("")){
//			sql.append("select apm_charges_invoice.id,payby,date,clientid,debit,deliverstatus,discount,tpid,firstname,apm_charges_invoice.disctype,discamt,apm_charges_invoice.itype,apm_patient.abrivationid from apm_charges_invoice inner join ");
//			sql.append("apm_patient on apm_patient.id = apm_charges_invoice.clientid ");
//			sql.append("where date between '"+fromDate+"' AND '"+toDate+"' ");
//			if(!invcategory.equals("2")){
//				sql.append("and apm_charges_invoice.invpstype="+invcategory+" ");
//			}
//			//sql.append("select id,payby,date,clientid,debit,deliverstatus,discount,tpid from apm_charges_invoice where date between '"+fromDate+"' AND '"+toDate+"'  ") ;
//		}else{
//			sql.append("select id,payby,date,clientid,debit,deliverstatus,discount,tpid,apm_charges_invoice.itype from apm_charges_invoice ") ;
//			if(!invcategory.equals("2")){
//				sql.append("where apm_charges_invoice.invpstype="+invcategory+" ");
//			}
//		}
		if(payby==null){
			payby="0";
		}
		if(thirdparty==null){
			thirdparty="0";
		}
		if(payby!=null){
			if(payby.equals("0")){
				orderby = "firstname";
			}
		}
			
//			if(!payby.equals("0")){
				
				sql = new StringBuffer();
//				if(payby.equals(Constants.PAY_BY_CLIENT)){
//					if(!fromDate.equals("") && !toDate.equals("")){
//						
//						//sql.append("select id,payby,date,clientid,debit,deliverstatus,discount,tpid from apm_charges_invoice where payby='"+payby+"' and  date between '"+fromDate+"' AND '"+toDate+"' order by id desc  ") ;
//						sql.append("select apm_charges_invoice.id,payby,date,clientid,debit,deliverstatus,discount,tpid,firstname,apm_charges_invoice.disctype,discamt,apm_charges_invoice.itype,apm_patient.abrivationid from apm_charges_invoice inner join ");
//						sql.append("apm_patient on apm_patient.id = apm_charges_invoice.clientid ");
//						sql.append("where payby='Client' and  date between '"+fromDate+"' AND '"+toDate+"' ");
//						if(!invcategory.equals("2")){
//							sql.append("and apm_charges_invoice.invpstype="+invcategory+" ");
//						}
//					}else{
//						sql.append("select apm_charges_invoice.id,payby,date,clientid,debit,deliverstatus,discount,tpid,firstname,apm_charges_invoice.disctype,discamt,apm_charges_invoice.itype,apm_patient.abrivationid from apm_charges_invoice inner join ");
//						sql.append("apm_patient on apm_patient.id = apm_charges_invoice.clientid ");
//						sql.append("where payby='Client' ");
//						if(!invcategory.equals("2")){
//							sql.append("and apm_charges_invoice.invpstype="+invcategory+" ");
//						}
//					}
//				}else{
//					
//					if(!fromDate.equals("") && !toDate.equals("")){
//						
//						//sql = "select id,payby,date,clientid,debit,deliverstatus,discount,tpid from apm_charges_invoice where payby='"+payby+"' and  date between '"+fromDate+"' AND '"+toDate+"' order by id desc " ;
						/*sql.append("select apm_charges_invoice.id,apm_charges_invoice.payby,apm_charges_invoice.date,");
						sql.append("apm_charges_invoice.clientid,debit,deliverstatus,discount,apm_charges_invoice.tpid,");
						sql.append("company_name,apm_charges_invoice.disctype,discamt,apm_charges_invoice.itype,");
						sql.append("apm_patient.abrivationid,sum(payment),concat(apm_user.initial,' ',apm_user.firstname,' ',apm_user.lastname)  ");
						sql.append("from apm_charges_invoice ");
						sql.append("inner join apm_patient on apm_patient.id = apm_charges_invoice.clientid ");
						sql.append("left join apm_user on apm_user.id =apm_charges_invoice.practid ");
						sql.append("left join apm_third_party_details on apm_third_party_details.id = apm_charges_invoice.tpid ");
						sql.append("left join apm_charges_payment on apm_charges_payment.chargeinvoiceid = apm_charges_invoice.id ");
						sql.append("where apm_charges_invoice.date between '"+fromDate+"' AND '"+toDate+"' ");
						if(payby.equals(Constants.PAY_BY_CLIENT)){
							sql.append("and payby='Client' ");
						}else if(payby.equals(Constants.PAY_BY_THIRD_PARTY)){
							sql.append("and payby='Third Party' ");
						}
						if(!invcategory.equals("2")){
							sql.append("and apm_charges_invoice.invpstype="+invcategory+" ");
						}
						if(!thirdparty.equals("0")){
							sql.append(" and apm_third_party_details.id="+thirdparty+" ");	
						}*/
				sql.append("select apm_charges_invoice.id,apm_charges_invoice.payby,apm_charges_invoice.date,apm_charges_invoice.clientid,apm_charges_invoice.debit,deliverstatus, ");
				sql.append("apm_charges_invoice.discount,apm_charges_invoice.tpid,company_name,apm_charges_invoice.disctype,discamt,apm_charges_invoice.itype, ");
				sql.append("apm_patient.abrivationid,sum(payment),concat(apm_user.initial,' ',apm_user.firstname,' ',apm_user.lastname), ");
				sql.append("apm_charges_invoice.ipdid,appointmentid ");
				sql.append("from apm_charges_invoice ");
				sql.append("inner join apm_patient on apm_patient.id = apm_charges_invoice.clientid ");
				sql.append("left join apm_user on apm_user.id =apm_charges_invoice.practid ");
				sql.append("left join apm_third_party_details on apm_third_party_details.id = apm_charges_invoice.tpid ");
				sql.append("left join apm_charges_payment on apm_charges_payment.chargeinvoiceid = apm_charges_invoice.id ");
				sql.append("left join apm_charges_assesment on apm_charges_assesment.chargeinvoiceid = apm_charges_invoice.id ");
				sql.append("left join apm_invoice on apm_invoice.id = apm_charges_assesment.invoiceid ");
				sql.append("where apm_charges_invoice.date between '"+fromDate+"' AND '"+toDate+"' ");
//					}else{
//						sql.append("select apm_charges_invoice.id,payby,date,clientid,debit,deliverstatus,discount,tpid,company_name,apm_charges_invoice.disctype,discamt,apm_charges_invoice.itype,apm_patient.abrivationid from apm_charges_invoice inner join ");
//						sql.append("apm_third_party_details on apm_third_party_details.id = apm_charges_invoice.tpid ");
//						sql.append("where payby='Third Party' ");
//						if(!invcategory.equals("2")){
//							sql.append("and apm_charges_invoice.invpstype="+invcategory+" ");
//						}
//					}
//				}
//				
//				
//			}
			
//			if(!thirdparty.equals("0")){
//				sql = new StringBuffer();
//				
//				if(!fromDate.equals("") && !toDate.equals("")){
//					
//					//sql.append("select id,payby,date,clientid,debit,deliverstatus,discount,tpid,company_name from apm_charges_invoice where payby='Third Party' and tpid="+thirdparty+" and  date between '"+fromDate+"' AND '"+toDate+"'  ") ;
//					sql.append("select apm_charges_invoice.id,payby,date,clientid,debit,deliverstatus,discount,tpid,company_name,apm_charges_invoice.disctype,discamt,apm_charges_invoice.itype,apm_patient.abrivationid from apm_charges_invoice inner join ");
//					sql.append("apm_patient on apm_patient.id = apm_charges_invoice.clientid ");
//					sql.append("inner join apm_third_party_details on apm_third_party_details.id = apm_charges_invoice.tpid ");
//					sql.append("where payby='Third Party' and  date between '"+fromDate+"' AND '"+toDate+"' and apm_third_party_details.id="+thirdparty+"  ");
//					if(!invcategory.equals("2")){
//						sql.append("and apm_charges_invoice.invpstype="+invcategory+" ");
//					}
//				}else{
//					sql.append("select id,payby,date,clientid,debit,deliverstatus,discount,tpid,company_name,apm_charges_invoice.disctype,discamt,apm_charges_invoice.itype,apm_patient.abrivationid from apm_charges_invoice where payby='Third Party' and tpid="+thirdparty+"  ") ;
//					if(!invcategory.equals("2")){
//						sql.append("and apm_charges_invoice.invpstype="+invcategory+" ");
//					}
//				}
//				
//			}
			
			
//		}
		if(invoicetype!=null){
		   if(!invoicetype.equals("0")){
			   sql.append(" and  apm_charges_invoice.itype="+invoicetype+" ");
		   }
		}
		sql.append(" group by apm_charges_invoice.id  ");
		/*sql.append("order by "+orderby+" "+order+" ");*/
		sql.append("order by apm_charges_invoice.id desc ");
		
		 
		try{
			preparedStatement = connection.prepareStatement(sql.toString());
			ResultSet rs = preparedStatement.executeQuery();
			double payment=0;
			while(rs.next()){
				double balance = 0;
				double result = 0.0;
				double creditAmount = 0;
				
				Accounts accounts = new Accounts();
				accounts.setId(rs.getInt(1));
				accounts.setPayby(rs.getString(2));
				accounts.setDate(DateTimeUtils.getCommencingDate1(rs.getString(3)));
				accounts.setClientid(rs.getInt(4));
				
				accounts.setDebitAmount(rs.getDouble(5));
				accounts.setDebitAmountx(DateTimeUtils.changeFormat(rs.getDouble(5)));
				
				accounts.setDeliverstatus(rs.getString(6));
				
				double debit = rs.getDouble(5);
				double total = rs.getDouble(5);
				int disctype = rs.getInt(10);
				double discamt = rs.getDouble(11);
				double discount = rs.getDouble(7);
				double r1 = (total*discount)/100;
				if(disctype==1){
					r1 = discamt;
				}
				accounts.setDiscAmmount(DateTimeUtils.changeFormat(r1));
				
				total = total-r1;
				result = result + total;
				
				creditAmount = getCreditAmount(rs.getDouble(1));
				
				//if refund against invoice
				double refundAmt = statementDAO.getRefundAmtAgainsInvoice(rs.getInt(1));
				accounts.setRefundAmountx(DateTimeUtils.changeFormat(refundAmt));
				creditAmount = creditAmount - refundAmt;
				
				accounts.setCreditAmount(creditAmount);
				accounts.setCreditCharge(Double.toString(creditAmount));
				accounts.setCreditTotalx(DateTimeUtils.changeFormat(creditAmount));
				
				
				balance = result - creditAmount;
				accounts.setBalance(balance);
				accounts.setBalancex(DateTimeUtils.changeFormat(balance));
				
				int noOfCharges = getNoOfCharges(rs.getInt(1));
				accounts.setNumberOfChages(noOfCharges);
				accounts.setDiscount(rs.getDouble(7));
				String clientname = getClientFullName(rs.getString(4));
				accounts.setClientName(clientname);
				String payee = rs.getString(2);
				String payeename = null;
				if(payee.equalsIgnoreCase("Third Party")){
					payeename = getTpname(rs.getInt(8));
					
				}
				else{
					payeename = clientname;
				}
				accounts.setPayeeName(payeename);
				//showing seq no instead of invoice id 21/09/2018
				AccountsDAO accountsDAO=new JDBCAccountsDAO(connection);
			    String ipdopdseq=accountsDAO.getIpdOpdSeqNoWithType(rs.getInt(1));
			    String inName=accountsDAO.getInvoiceName(rs.getString(12));
			    accounts.setInvoicetype(inName);
			    accounts.setIpdopdseq(ipdopdseq);
			    accounts.setAbrivationid(rs.getString(13));
			    accounts.setTotalAmount(rs.getDouble(14));
			    accounts.setPractitionerName(rs.getString(15));
			    if(rs.getInt(16)>0){
			    	accounts.setIpdid(rs.getString(16));
			    }else{
			    	accounts.setIpdid(rs.getString(17));
			    }
				if(paymentStatus!=null){
					if(paymentStatus.equals("Paid")){
						if(balance==0){
							list.add(accounts);
						}
					}else if(paymentStatus.equals("Not Paid")){
						if(balance!=0){
							list.add(accounts);
						}
					}else{
						list.add(accounts);
					}
				}else{
					list.add(accounts);
				}
				
				
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return list;
	}
	public String getTpname(int id) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT company_name FROM apm_third_party_details where id = "+id+"";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				result = rs.getString(1);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String getClientFullName(String id) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT title,firstname,surname FROM apm_patient where  id = "+id+"";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				result = rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public String getClientUHID(String id) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT abrivationid FROM apm_patient where  id = "+id+"";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				result = rs.getString(1);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int getNoOfCharges(int id) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "SELECT count(id) FROM apm_charges_payment where chargeinvoiceid = "+id+" ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				result = rs.getInt(1);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public double getCreditAmount(double id) {
		PreparedStatement preparedStatement = null;
		double result = 0;
		String sql = "SELECT sum(payment) FROM apm_charges_payment where chargeinvoiceid = "+id+" ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				result = rs.getDouble(1);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	
	public ArrayList<Accounts> getAdvPaymentModeWiseList(String fromDate, String toDate,
			int itype, String payby, String howpaid, String invoicecategory,
			String userid){
		
		PreparedStatement preparedStatement = null;
		ArrayList<Accounts> list = new ArrayList<Accounts>();
		StringBuffer str = new StringBuffer();
		
		toDate = toDate + " 23:59:59";
		
		str.append("SELECT sum(charge),payment_mode ");
		str.append("FROM apm_credit_account inner join apm_patient on apm_patient.id = apm_credit_account.client_id ");
		str.append("where payment_mode IS NOT NULL and date between '"+fromDate+"' and '"+toDate+"' and charge>0 ");
		
		if(payby!=null){
			
		
			
			if(!howpaid.equals("0")){
				str.append("and apm_credit_account.payment_mode='"+howpaid+"' ");
			}
			
			/*if(!userid.equals("0")){
				str.append("and apm_credit_account.userid='"+userid+"' ");
			}*/
			
			if(userid!=null){
				if(userid.equals("")||userid.equals("0") ){
					
				}else{
					str.append("and apm_credit_account.userid='"+userid+"' ");
				}
				
				
				
			}
			
			
		}
		
		str.append("group by payment_mode ");
		
		try{
			preparedStatement = connection.prepareStatement(str.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Accounts accounts = new Accounts();
				accounts.setAmount(rs.getDouble(1));
				accounts.setPayAmountx(DateTimeUtils.changeFormat(rs.getDouble(1)));
				accounts.setPaymentmode(rs.getString(2));
				
				list.add(accounts);
			}
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return list;
	}
	
	public ArrayList<Accounts> getPaymentModeWiseList(String fromDate, String toDate,
			int itype, String payby, String howpaid, String invoicecategory,
			String userid){
		
		PreparedStatement preparedStatement = null;
		ArrayList<Accounts> list = new ArrayList<Accounts>();
		StringBuffer str = new StringBuffer();
		
		toDate = toDate + " 23:59:59";
		
		str.append("select sum(payment),paymode FROM apm_charges_payment ");
		str.append("inner join apm_charges_invoice on apm_charges_invoice.id = apm_charges_payment.chargeinvoiceid ");
		str.append("where apm_charges_payment.date between '"+fromDate+"' AND '"+toDate+"' and paymode!='prepayment' ");
		str.append("and itype = "+itype+" ");
		
		if(payby!=null){
			if(!payby.equals("0")){
				if(!payby.equals("Client")){
					str.append("and apm_charges_payment.tpid=0");
				}else{
					str.append("and apm_charges_payment.tpid!=0");
				}
			}
		
			
			if(!howpaid.equals("0")){
				str.append("and apm_charges_payment.paymode='"+howpaid+"' ");
			}
			
			/*if(!userid.equals("0")){
				str.append("and apm_charges_payment.userid='"+userid+"' ");
			}*/
			
			if(userid!=null){
				if(userid.equals("")||userid.equals("0") ){
					
				}else{
					str.append("and apm_charges_payment.userid='"+userid+"' ");
				}
				
			}
			
			
		}
		
		str.append("group by paymode ");
		
		try{
			preparedStatement = connection.prepareStatement(str.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Accounts accounts = new Accounts();
				accounts.setAmount(rs.getDouble(1));
				accounts.setPayAmountx(DateTimeUtils.changeFormat(rs.getDouble(1)));
				accounts.setPaymentmode(rs.getString(2));
				
				list.add(accounts);
			}
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return list;
	}

	public ArrayList<Accounts> getPaymentReportList(String fromDate,
			String toDate,String payby,String howpaid,String orderby,String order,String invcategory,String userid,String selectedInvctype, String sortfilter) {
		String tp = "Third Party";String self = "Client";
		PreparedStatement preparedStatement = null;
		ArrayList<Accounts>list = new ArrayList<Accounts>();
		StatementDAO statementDAO = new JDBCStatementDAO(connection);
		StringBuffer sql = new StringBuffer();
		
		if(payby!=null){
			if(!payby.equals("0")){
				if(payby.equals(Constants.PAY_BY_CLIENT)){
					sql.append("select apm_charges_payment.id,apm_charges_payment.clientid,chargeinvoiceid,payment,paymode,apm_charges_payment.date,apm_charges_payment.tpid,payby,firstname,apm_charges_payment.userid,apm_charges_invoice.id, concat(apm_user.initial,' ',apm_user.firstname,' ',apm_user.lastname),apm_charges_invoice.itype,apm_charges_payment.ipdno,apm_charges_payment.opdno,apm_charges_payment.invno,apm_charges_payment.cyear,concat(his_invoice_payment_sno.cyear,'/',his_invoice_payment_sno.sno),paymentnote,chequeno,cancelinv, ");
					sql.append("debit,discount,apm_charges_invoice.disctype,discamt,apm_charges_invoice.date,apm_charges_invoice.time ");
					sql.append("FROM apm_charges_payment ");
					sql.append("inner join apm_charges_invoice on apm_charges_invoice.id = apm_charges_payment.chargeinvoiceid ");
					sql.append(" inner join apm_user on apm_user.id =apm_charges_invoice.practid ");
					sql.append("inner join apm_patient on apm_patient.id = apm_charges_payment.clientid ");
					sql.append(" left join his_invoice_payment_sno on his_invoice_payment_sno.paymentid= apm_charges_payment.id ");
					
				}else{
					sql.append("select apm_charges_payment.id,apm_charges_payment.clientid,chargeinvoiceid,payment,paymode,apm_charges_payment.date,apm_charges_payment.tpid,payby,company_name,apm_charges_payment.userid,apm_charges_invoice.id, concat(apm_user.initial,' ',apm_user.firstname,' ',apm_user.lastname),apm_charges_invoice.itype,apm_charges_payment.ipdno,apm_charges_payment.opdno,apm_charges_payment.invno,apm_charges_payment.cyear,concat(his_invoice_payment_sno.cyear,'/',his_invoice_payment_sno.sno),paymentnote,chequeno,cancelinv, ");
					sql.append("debit,discount,apm_charges_invoice.disctype,discamt,apm_charges_invoice.date,apm_charges_invoice.time ");
					sql.append("FROM apm_charges_payment ");
					sql.append("inner join apm_charges_invoice on apm_charges_invoice.id = apm_charges_payment.chargeinvoiceid ");
					sql.append(" inner join apm_user on apm_user.id =apm_charges_invoice.practid ");
					sql.append("inner join apm_third_party_details on apm_third_party_details.id = apm_charges_payment.tpid ");
					sql.append(" left join his_invoice_payment_sno on his_invoice_payment_sno.paymentid= apm_charges_payment.id ");
				}
			}else{
				sql.append("select apm_charges_payment.id,apm_charges_payment.clientid,chargeinvoiceid,payment,paymode,apm_charges_payment.date,apm_charges_payment.tpid,payby,apm_charges_payment.clientid,apm_charges_payment.userid,apm_charges_invoice.id, concat(apm_user.initial,' ',apm_user.firstname,' ',apm_user.lastname),apm_charges_invoice.itype,apm_charges_payment.ipdno,apm_charges_payment.opdno,apm_charges_payment.invno,apm_charges_payment.cyear,concat(his_invoice_payment_sno.cyear,'/',his_invoice_payment_sno.sno),paymentnote,chequeno,cancelinv, ");
				sql.append("debit,discount,apm_charges_invoice.disctype,discamt,apm_charges_invoice.date,apm_charges_invoice.time ");
				sql.append("FROM apm_charges_payment ");
				sql.append("inner join apm_charges_invoice on apm_charges_invoice.id = apm_charges_payment.chargeinvoiceid ");
				sql.append(" inner join apm_user on apm_user.id =apm_charges_invoice.practid ");
				sql.append(" left join his_invoice_payment_sno on his_invoice_payment_sno.paymentid= apm_charges_payment.id ");
			}
		}else{
			sql.append("select apm_charges_payment.id,apm_charges_payment.clientid,chargeinvoiceid,payment,paymode,apm_charges_payment.date,apm_charges_payment.tpid,payby,apm_charges_payment.clientid,apm_charges_payment.userid,apm_charges_invoice.id, concat(apm_user.initial,' ',apm_user.firstname,' ',apm_user.lastname),apm_charges_invoice.itype,apm_charges_payment.ipdno,apm_charges_payment.opdno,apm_charges_payment.invno,apm_charges_payment.cyear,concat(his_invoice_payment_sno.cyear,'/',his_invoice_payment_sno.sno),paymentnote,chequeno,cancelinv, ");
			sql.append("debit,discount,apm_charges_invoice.disctype,discamt,apm_charges_invoice.date,apm_charges_invoice.time ");
			sql.append("FROM apm_charges_payment ");
			sql.append("inner join apm_charges_invoice on apm_charges_invoice.id = apm_charges_payment.chargeinvoiceid ");
			sql.append(" inner join apm_user on apm_user.id =apm_charges_invoice.practid ");
			sql.append(" left join his_invoice_payment_sno on his_invoice_payment_sno.paymentid= apm_charges_payment.id ");
			
		}
	
	
		
		
		if(!fromDate.equals("") && !toDate.equals("")){
			
			/*if(fromDate.equals(toDate)){
				toDate = toDate + " " + "23:59:59";
			}else{
				if(!toDate.equals("")){
					String dt = toDate;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Calendar c = Calendar.getInstance();
					try {
						c.setTime(sdf.parse(dt));
						c.add(Calendar.DATE, 1);  // number of days to add
						dt = sdf.format(c.getTime());  // dt is now the new date
						toDate = dt + " " + "23:59:59";
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
			}*/
			toDate = toDate + " " + "23:59:59";
			
			//sql.append("select id,clientid,chargeinvoiceid,payment,paymode,date,tpid from apm_charges_payment where date between '"+fromDate+"' AND '"+toDate+"' order by id desc ") ;
			
			sql.append("where apm_charges_payment.date between '"+fromDate+"' AND '"+toDate+"' ");
			
			
			if(payby!=null){
				if(!payby.equals("0") && !howpaid.equals("0") ){
					sql.append("and payby='"+payby+"' and paymode='"+howpaid+"' ");
				}else if(!payby.equals("0")){
					sql.append("and payby='"+payby+"' " );
				}else if(!howpaid.equals("0")){
					sql.append("and paymode='"+howpaid+"' " );
				}
			}
		}else{
			
			if(payby!=null){
				if(!payby.equals("0") && !howpaid.equals("0") ){
					sql.append("where payby='"+payby+"' and paymode='"+howpaid+"' ");
				}else if(!payby.equals("0")){
					sql.append("where payby='"+payby+"' " );
				}else if(!howpaid.equals("0")){
					sql.append("where paymode='"+howpaid+"' " );
				}
			}
			
		}
		
		if(!invcategory.equals("2")){
			
			sql.append("and apm_charges_invoice.invpstype="+invcategory+" ");
		}
		
		if(!userid.equals("0")){	 
			sql.append(" and apm_charges_payment.userid='"+userid+"' ");
		}
		if(payby!=null){
		/*if(!userid.equals("0")){*/
		
		
			
		if(!selectedInvctype.equals("0")){
			String temp[] = selectedInvctype.split(",");
			if(temp.length>1){
				//selectedInvctype = selectedInvctype.substring(selectedInvctype.length()-1);
				sql.append(" and itype in("+selectedInvctype+") ");
			}
		}
		}
		
		sql.append("and paymode!='prepayment' ");
		if(sortfilter.equals("0")){
			sql.append(" order by itype ");
		}else if(sortfilter.equals("0")){
			sql.append(" order by chargeinvoiceid ");
		}else{
			sql.append(" order by apm_charges_payment.id ");
		}
		//sql.append("and paymode!='prepayment' order by chargeinvoiceid ");
		try{
			preparedStatement = connection.prepareStatement(sql.toString());
			ResultSet rs = preparedStatement.executeQuery();
			
			double opdtotal = 0;
			double ipdtotal = 0;
			double pathlabtotal = 0;
			double mdcinetotal = 0;
			double advreftotal = 0;
			double daycaretotal=0; 
			double balancetotal=0;
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			String invoiceids="0";
			while(rs.next()){
				double result = 0.0;
				Accounts accounts = new Accounts();
				accounts.setId(rs.getInt(1));
				accounts.setClientid(rs.getInt(2));
				String clientName = getClientFullName(rs.getInt(2));
				String abrivationid =getClientAbrivationId(rs.getInt(2));
				accounts.setAbrivationid(abrivationid);
				accounts.setClientName(clientName);
				accounts.setInvoiceid(rs.getInt(3));
				accounts.setAmount(rs.getDouble(4));
				accounts.setAmountx(DateTimeUtils.changeFormat(rs.getDouble(4)));
				accounts.setPaymentmode(rs.getString(5));
				accounts.setDate(DateTimeUtils.getIndianDateTimeFormat(rs.getString(6)));
				accounts.setInvoicedate(DateTimeUtils.getCommencingDate1(rs.getString(26))+" "+rs.getString(27));
				String whopay = getWhoPay(rs.getInt(3));
				accounts.setWhoPay(whopay);
				if(whopay.equals(tp)){
					String invoicee = getThirdPartyCompanyName(rs.getString(7));
					accounts.setInvoicee(invoicee);
				}else{
					accounts.setInvoicee(clientName);
				}
				accounts.setUserid(rs.getString(10));
				
				int invoiceid = rs.getInt(11);
				invoiceids = invoiceids +","+invoiceid;
				accounts.setInvoiceids(invoiceids);
				accounts.setInvoiceid(invoiceid);
				
				ArrayList<Master>masterAssessmentList = accountsDAO.getMasterAssessmentPmntRptList(invoiceid,fromDate,toDate,userid);
				accounts.setMasterAssessmentList(masterAssessmentList);
				
				String invoicenameid=accountsDAO.getInvoiceTypeId(invoiceid);
				if(invoicenameid.equals("1")){
					opdtotal = opdtotal + accounts.getAmount();
				}
				
				if(invoicenameid.equals("2")){
					ipdtotal = ipdtotal + accounts.getAmount();
				}
				
				if(invoicenameid.equals("3")){
					pathlabtotal = pathlabtotal + accounts.getAmount();
				}
				
				if(invoicenameid.equals("4")||invoicenameid.equals("6")||invoicenameid.equals("7")){
					mdcinetotal = mdcinetotal + accounts.getAmount();
				}

				if(invoicenameid.equals("8")){
					daycaretotal = daycaretotal + accounts.getAmount();
				}
				
				String itype=rs.getString(13);
				String ipdid=rs.getString(14);
				String opdid=rs.getString(15);
				String invstid=rs.getString(16);
				String cyear=rs.getString(17);
				String type1="";
				/*if(itype!=null){
					if(itype.equals("2")){
						type1=cyear+"/"+ipdid;
					}else if(itype.equals("1")){
						type1=cyear+"/"+opdid;
					}else if(itype.equals("3")){
						type1=cyear+"/"+invstid;
					}else{
						type1=String.valueOf(accounts.getId());
					}*/
				if(rs.getString(18)!=null){
				type1= rs.getString(18);
				accounts.setNewsr(type1);	
				}else{
					accounts.setNewsr(String.valueOf(accounts.getId()));	
				}
				accounts.setOpdtotal(DateTimeUtils.changeFormat(opdtotal));
				accounts.setIpdtotal(DateTimeUtils.changeFormat(ipdtotal));
				accounts.setPathlabtotal(DateTimeUtils.changeFormat(pathlabtotal));
				accounts.setMdcinetotal(DateTimeUtils.changeFormat(mdcinetotal));
				accounts.setDaycaretotal(DateTimeUtils.changeFormat(daycaretotal));
				accounts.setPractitionerName(rs.getString(12));
				
				String invoicename=accountsDAO.getInvoiceName(invoicenameid);
				accounts.setInvoicenameid(invoicename);
				//showing seqno instead of invoice no. Shubham
				//AccountsDAO accountsDAO2=new JDBCAccountsDAO(connection);
			    int ipdopdseq=accountsDAO.getIpdOpdSeqNo((rs.getInt(3)));
			    accounts.setIpdopdseq(String.valueOf(ipdopdseq));
			    String paymentnote=rs.getString(19);
			    String chequeno=rs.getString(20);
			    if(rs.getString(5).equals("Cheque")){
					   accounts.setPaymentNote(chequeno);
				   }else {
					accounts.setPaymentNote(paymentnote);
				}
			    accounts.setCancelsts(rs.getString(21));
			    accounts.setPhysical_payment_id(getPhysicalpaymentId(rs.getString(1)));
			    
			    
			    double total = rs.getDouble(22);
				int disctype = rs.getInt(24);
				double discamt = rs.getDouble(25);
				double discount = rs.getDouble(23);
				double r1 = (total*discount)/100;
				if(disctype==1){
					r1 = discamt;
				}
				accounts.setDiscAmmount(DateTimeUtils.changeFormat(r1));
				accounts.setDebitAmountx(DateTimeUtils.changeFormat(total));
				total = total-r1;
				result = result + total;
				
				double creditAmount = getCreditAmount(rs.getDouble(11));
				
				//if refund against invoice
				double refundAmt = statementDAO.getRefundAmtAgainsInvoice(invoiceid);
				accounts.setRefundAmountx(DateTimeUtils.changeFormat(refundAmt));
				creditAmount = creditAmount - refundAmt;
				
				accounts.setCreditAmount(creditAmount);
				accounts.setCreditCharge(Double.toString(creditAmount));
				accounts.setCreditTotalx(DateTimeUtils.changeFormat(creditAmount));
				
				
				double balance = result - creditAmount;
				accounts.setBalance(balance);
				accounts.setBalancex(DateTimeUtils.changeFormat(balance));
			    accounts.setBalanceTotal(balancetotal+balance);
			    balancetotal=accounts.getBalanceTotal();
				list.add(accounts);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return list;
	}
	private String getClientAbrivationId(int id) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT abrivationid FROM apm_patient where  id = "+id+"";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				result = rs.getString(1);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private String getWhoPay(int id) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT payby from apm_charges_invoice where  id = "+id+"";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				result = rs.getString(1);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public String getThirdPartyCompanyName(String companyName) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT company_name FROM apm_third_party_details where  id = "+companyName+"";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				result = rs.getString(1);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public String getClientFullName(int id) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT title,firstname,surname FROM apm_patient where  id = "+id+"";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				result = rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int moveInvoiceToScecondary(String hdnprimaryinvoice) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		
		int invpstype = getInvpstype(hdnprimaryinvoice);
		
		String sql = "update apm_charges_invoice  set invpstype="+invpstype+" where id in("+hdnprimaryinvoice+") ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			result = preparedStatement.executeUpdate();
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	private int getInvpstype(String invoicesid) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "select invpstype from apm_charges_invoice where id in("+invoicesid+") group by invpstype ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				result = rs.getInt(1);
			}
			
			if(result==0){
				result = 1;
				return result;
			}
			
			if(result==1){
				result = 0;
				return result;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<Accounts> getAdvanceInvoiceReportList(String fromDate,
			String toDate, String payby, String paymentStatus,
			String thirdParty, String orderby, String order,
			String invoicecategory, String invcetype,String userid) {
		PreparedStatement preparedStatement = null;
		ArrayList<Accounts>list = new ArrayList<Accounts>();
		//String sql = "SELECT id,date,payby,payment_mode,charge,debit,apm_credit_accountbalance FROM apm_credit_account where client_id = "+clientId+" and payment_mode IS NOT NULL";
		
		toDate = toDate + " 23:59:59";
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT apm_credit_account.id,date,payby,payment_mode,charge,debit,balance,third_party_name_id,concat(title,' ',firstname,' ',surname),advref,resetinv,payment_mode,userid,apm_credit_account.cyear,apm_credit_account.advno,apm_credit_account.credit_note,apm_credit_account.invoiceid,apm_credit_account.cancelpay FROM apm_credit_account inner join ");
		sql.append("apm_patient on apm_patient.id = apm_credit_account.client_id where payment_mode IS NOT NULL and date between '"+fromDate+"' and '"+toDate+"' and charge>0 ");
		if(userid!=null){
			sql.append(" and userid='"+userid+"' ");
		}
		
		sql.append("order by resetinv ");
		
		
		try{
			
			preparedStatement = connection.prepareStatement(sql.toString());
			ResultSet rs = preparedStatement.executeQuery();
			
			double advtotal = 0;
			while(rs.next()){
				Accounts accounts = new Accounts();
				accounts.setId(rs.getInt(1));
				String date = DateTimeUtils.getIndianDateTimeFormat(rs.getString(2));
				accounts.setDate(date);
				String temp[] = date.split(" ");		
				accounts.setCommencing(temp[0]);
				accounts.setPayby(rs.getString(3));
				accounts.setPaymentmode(rs.getString(4));
				accounts.setCharges(DateTimeUtils.changeFormat(Double.parseDouble(rs.getString(5))));
				advtotal = advtotal + rs.getDouble(5);
				accounts.setDebitTotalx(DateTimeUtils.changeFormat(Double.parseDouble(rs.getString(6))));
				accounts.setAdvreftotal(DateTimeUtils.changeFormat(advtotal));
				
				double credit = rs.getDouble(5);
				double debit = rs.getDouble(6);
				double balance = 0;
				
				accounts.setDebitAmount(debit);
				accounts.setCreditAmount(credit);
				accounts.setBalance(balance);
				
				if(rs.getString(3).equals(Constants.PAY_BY_THIRD_PARTY)){
					
					CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(connection);
				    CompleteAppointment	completeAppointment = completeAptmDAO.getInsuranceCompanyName(rs.getString(8));
				    accounts.setClientName(completeAppointment.getInsuranceCompanyName());
				}
				else{
					
					accounts.setClientName(rs.getString(9));
				}
				
				
				accounts.setBalancex(DateTimeUtils.changeFormat(balance));
				accounts.setAdvref(rs.getInt(10));
				accounts.setResetinv(rs.getString(11));
				accounts.setPaymentmode(rs.getString(12));
				accounts.setUserid(rs.getString(13));
				accounts.setAdvno(rs.getString(14)+"/"+rs.getString(15));
				accounts.setRemark(rs.getString(16));
				String receipt=getpaymentreceipt(rs.getInt(17));
				accounts.setReceiptid(receipt);
				accounts.setCancelsts(rs.getString(18));
				list.add(accounts);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	private String getpaymentreceipt(int int1) {
		PreparedStatement preparedStatement = null;
		String sql = "select id from apm_charges_payment where chargeinvoiceid="+int1+" ";
		
		String result = "";
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				result = rs.getString(1);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
	}

	public ArrayList<Accounts> getDoctorShareReport(String fromdate,
			String todate,String pracId,String jobtitle) {

		ArrayList<Accounts> superInvoiceList=new ArrayList<Accounts>();
		UserProfileDAO userProfileDAO=new JDBCUserProfileDAO(connection);
		StringBuffer buffer=new StringBuffer();
		try {
			
			buffer.append("select id from apm_charges_invoice where date between '"+fromdate+"' and '"+todate+"' order by id desc ");
			 /*buffer.append("select apm_charges_invoice.id,apm_invoice_assesments.appointmentid from apm_invoice_assesments ");
			   buffer.append("inner join apm_charges_assesment on apm_invoice_assesments.invoiceid=apm_charges_assesment.invoiceid ");
			   buffer.append("inner join apm_charges_invoice on apm_charges_assesment.chargeinvoiceid = apm_charges_invoice.id and ");
			   buffer.append("date between '"+fromdate+"' and '"+todate+"' order by id desc ");*/

			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()){
				
				Accounts accounts=new Accounts();
				int chargeinvoiceid=rs.getInt(1);
				String apmtid="0";
				accounts.setId(chargeinvoiceid);
				
				ArrayList<Accounts> practionerList=  getPractionerListofInvoice(chargeinvoiceid,pracId,jobtitle,apmtid);
			    for(Accounts acc:practionerList){
			    	acc.setId(chargeinvoiceid);
			    	
			    	String fullname=userProfileDAO.getFullName(String.valueOf(acc.getPractitionerId()));
			    	acc.setPractitionerName(fullname);
			    	
			    	ArrayList<Accounts> assesmentListCharges= getAllChargesByPracIdandInvoiceid(acc.getInvoiceid(),acc.getPractitionerId(),acc.getDiscount(),acc.getAppointmentid());
			    	ArrayList<Accounts> sharedChargeList=new ArrayList<Accounts>();
			    	boolean flag=false;
			    	double totalSub=0.0;
			    	double totalDisc=0.0;
			    	double shareTotal=0.0;
			    	int totalQty=0;
			    	if(assesmentListCharges.size()>0){
			    	for(Accounts acc1:assesmentListCharges) {
			    		acc1.setId(chargeinvoiceid);
			    		
			    		 boolean isshared= isshared(acc1.getChargeType(), acc.getPractitionerId());
			    		 if(isshared){
			    				totalQty=totalQty+acc1.getQuantity();
					    		totalSub=totalSub+acc1.getTotalAmount();
					    		totalDisc=totalDisc+acc1.getDiscount();
					    		shareTotal=shareTotal+acc1.getShareAmt();
			    			 
				    		   sharedChargeList.add(acc1);
				    		   flag=true;
				    	 }
			    		
			    	}
			    	}
			    	if(flag){
			    		
			    		String tsub=DateTimeUtils.changeFormat(totalSub);
			    		String tshar=DateTimeUtils.changeFormat(shareTotal);
			    		
			    		acc.setTotalSub(Double.parseDouble(tsub));
			    		acc.setTotalDisc(totalDisc);
			    		acc.setShareTotal(Double.parseDouble(tshar));
			    		acc.setTotalQty(totalQty);
			    		acc.setSharedChargeList(sharedChargeList);
			    		superInvoiceList.add(acc);
			    	}
			    }
			   
					
			}
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		return superInvoiceList;
	}

	private ArrayList<Accounts> getPractionerListofInvoice(int chargeinvoiceid,String pracId,String jobtitle,String appointmentid) {
		
	   ArrayList<Accounts> list=new ArrayList<Accounts>();
	   StringBuffer buffer=new StringBuffer();
	   try {
		   buffer.append("select apm_invoice_assesments.invoiceid,apm_invoice_assesments.practitionerid,apm_charges_invoice.discount,apm_invoice_assesments.appointmentid from apm_invoice_assesments ");
		   buffer.append("inner join apm_charges_assesment on apm_invoice_assesments.invoiceid=apm_charges_assesment.invoiceid ");
		   buffer.append("inner join apm_charges_invoice on apm_charges_assesment.chargeinvoiceid = apm_charges_invoice.id ");
		   buffer.append("where apm_charges_invoice.id="+chargeinvoiceid+" ");
		   /*if(appointmentid!=null){
   	    	if(!appointmentid.equals("0")){
   	    		   NotAvailableSlot otobj = getOtAppointmentDetails(Integer.parseInt(appointmentid));
  		    	    
  					if(otobj.getSurgeon().equals("0")){
  						 buffer.append("inner join apm_user on apm_invoice_assesments.practitionerid=apm_user.id ");
  						 buffer.append("where apm_charges_invoice.id="+chargeinvoiceid+" ");
  						 
  						 if(jobtitle.equals("0")){
  						    buffer.append("and apm_user.jobtitle='Practitioner' ");
  					   } else {
  						   buffer.append("and apm_user.jobtitle!='Practitioner' ");
  					   }
  				  } else {
  					  
  					  buffer.append("where apm_charges_invoice.id="+chargeinvoiceid+" ");
  				  }
   	    	  }
     	   }*/
		  
		  
		   
		  
		   
		   if(!pracId.equals("0")){
			   
			    buffer.append("and apm_invoice_assesments.practitionerid="+pracId+" ");
		   }
		   
		   buffer.append("group by apm_invoice_assesments.invoiceid;");
		   PreparedStatement ps=connection.prepareStatement(buffer.toString());
		    ResultSet rs=ps.executeQuery();
		    
		    while(rs.next()){
		    	  
		    	    Accounts accounts=new Accounts();
		    	    accounts.setInvoiceid(rs.getInt(1));
		    	    accounts.setPractitionerId(rs.getInt(2));
		    	    accounts.setDiscount(rs.getDouble(3));
		    	    accounts.setAppointmentid(rs.getInt(4));
		    	    
		    	    String apmtid = rs.getString(4);
		    	    ArrayList<Accounts>otdoctorlist = new ArrayList<Accounts>();
		    	    if(apmtid!=null){
		    	    	if(!apmtid.equals("0")){
		    	    		   NotAvailableSlot otobj = getOtAppointmentDetails(Integer.parseInt(apmtid));
		   		    	    
		   					if(!otobj.getSurgeon().equals("0")){
		   						otdoctorlist = getOtDoctorList(accounts.getInvoiceid(),accounts.getDiscount(),Integer.parseInt(apmtid));
		   					}
		    	    	}
		    	    }
		    	 
		    	    
					
		    	   
		    	    list.add(accounts);
		    	    if(otdoctorlist.size()!=0){
		    	    	list.addAll(otdoctorlist);
		    	    }
 
		    }
		    
		    
	   } catch (Exception e) {

		   e.printStackTrace();
	   }	
	  return list;	
	}
	
	
	
	private ArrayList<Accounts> getOtDoctorList(int invoiceid, double discount,int apmtid) {
		
		ArrayList<Accounts>list = new ArrayList<Accounts>();
		 NotAvailableSlot otobj = getOtAppointmentDetails(apmtid);
		 DiaryManagementDAO diaryManagementDAO = new JDBCDiaryManagentDAO(connection);
		 String stafflistid = diaryManagementDAO.getAsistantDoctorList(apmtid);
		 stafflistid = stafflistid + "," + otobj.getAnesthesia() + "," + otobj.getSurgeon();
		 
		 String temps[] = stafflistid.split(",");
			for(int b=0;b<temps.length;b++){
				//if(b>0){
					String selectedpractid = temps[b];
					
					Accounts accounts = new Accounts();
					accounts.setInvoiceid(invoiceid);
					accounts.setDiscount(discount);
					accounts.setPractitionerId(Integer.parseInt(selectedpractid));
					accounts.setAppointmentid(apmtid);
					
					list.add(accounts);
				//}
			}
		 
		return list;
	}

	private boolean isshared(String chargeType,int doctorid) {
		
		try {
			String sql="select id from apm_shared_charges where chargetype='"+chargeType+"' and userid like('%"+doctorid+"%') ";
			PreparedStatement ps=connection.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()){
				
				return true;
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return false;
	}
	
	
	public Accounts getSharedAssesmentAccount(int id) {
		
		Accounts accounts=new Accounts();
		try {
			
			String sql="select invoiceid,user,apmtType,charge,clientId,quantity,ipdid from apm_invoice_assesments where id="+id+"";
			PreparedStatement ps=connection.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				
				   accounts.setInvoiceid(rs.getInt(1));
				   accounts.setClientName(rs.getString(2));
				   accounts.setAppointmentType(rs.getString(3));
				   accounts.setCharges(rs.getString(4));
				   accounts.setClientid(rs.getInt(5));
				   accounts.setQuantity(rs.getInt(6));
				   accounts.setIpdid(rs.getString(7));
				   
				   double total=Integer.parseInt(accounts.getCharges())*accounts.getQuantity();
				   accounts.setTotalAmount(total);
				   accounts.setDiscount(0.0);
				   accounts.setShareAmt(0.0);
			}
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return accounts;
	}
	
	
	public ArrayList<Accounts> getAllChargesByPracIdandInvoiceid(int invoiceid,int practitionerid,double discount,int apmtid){
		
		ArrayList<Accounts> list=new ArrayList<Accounts>();
		UserProfileDAO userProfileDAO=new JDBCUserProfileDAO(connection);
		ThirdPartyDAO thirdPartyDAO=new JDBCThirdPartyDAO(connection);
		try {
			
			String sql="select id,user,apmtType,charge,practitionerId,clientId,quantity,ipdid,masterchargetype,commencing,paybuy,thirdPartyId,appointmentid from apm_invoice_assesments where invoiceid="+invoiceid+" and  practitionerId="+practitionerid+"";
			ChargesReportDAO chargesReportDAO = new JDBCChargesReportDAO(connection);
			if(apmtid!=0){
			NotAvailableSlot otobj = chargesReportDAO.getOtAppointmentDetails(apmtid);
			if(!otobj.getSurgeon().equals("0")){
				 sql="select id,user,apmtType,charge,practitionerId,clientId,quantity,ipdid,masterchargetype,commencing,paybuy,thirdPartyId,appointmentid from apm_invoice_assesments where invoiceid="+invoiceid+" and appointmentid="+apmtid+"";
			}
			}
			
			PreparedStatement ps=connection.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat format=new SimpleDateFormat("dd-MMM");
			while(rs.next()){
				
				    Accounts accounts=new Accounts();
				    accounts.setId(rs.getInt(1));
				    accounts.setClientName(rs.getString(2));
				    accounts.setAppointmentType(rs.getString(3));
				    accounts.setCharges(rs.getString(4));
				    accounts.setPractitionerId(rs.getInt(5));
				    accounts.setClientid(rs.getInt(6));
				    accounts.setQuantity(rs.getInt(7));
				    accounts.setIpdid(rs.getString(8));
				    accounts.setChargeType(rs.getString(9));
				    accounts.setCommencing(rs.getString(10));
				    accounts.setPayBy(rs.getInt(11));
				    accounts.setTpid(rs.getInt(12));
				    accounts.setAppointmentid(rs.getInt(13));
				    Calendar calendar=Calendar.getInstance();
				    if(accounts.getCommencing()!=null){
				    
				    Date date=dateFormat.parse(accounts.getCommencing()); 
				    calendar.setTime(date);
				    }
				    String month=format.format(calendar.getTime());
				    accounts.setMonth(month);
				    String wardname=getWardFromIpd(accounts.getIpdid());
				    accounts.setWard(wardname);
				    
				    
				    int ipdid=rs.getInt(8);
				    int userper=0;
				    
				 /*  boolean isprocdures=isProcedureChargeType(accounts.getChargeType()); 
				   if(isprocdures && accounts.getAppointmentid()>0){
							 //for ot   
							    NotAvailableSlot notAvailableSlot=getOtAppointmentDetails(accounts.getAppointmentid());
							    
							    if(accounts.getPayBy()==0){
							    	  //Self
							    	UserProfile userProfile=userProfileDAO.getUserprofileDetails(Integer.parseInt(notAvailableSlot.getSurgeon()));
							    	userper=userProfile.getSurgeonCharge();
							    		
							    } else {
							    	ThirdParty thirdParty=thirdPartyDAO.getThirdPartyDetails(String.valueOf(accounts.getTpid()));  
							    	   //tp
							    	userper=thirdParty.getSurgeonShare();
							    	
						       }
							   
					} else {*/
						
						if(accounts.getPayBy()==0){
					    	//self
					    	UserProfile userProfile=userProfileDAO.getUserprofileDetails(practitionerid);
					    	if(ipdid>1){
					    		//ipd
					    		userper=userProfile.getIpdCharge();
					    	} else {
					    		// opd
					    		userper=userProfile.getCompAppCharge();
					    	}
					    } else {
					    	ThirdParty thirdParty=thirdPartyDAO.getThirdPartyDetails(String.valueOf(accounts.getTpid()));
					    	//thirparty
					    	if(ipdid>1){
					    		//ipd
					    		userper=thirdParty.getIpdShare();
					    	} else {
					    		// opd
					    		userper=thirdParty.getOpdShare();
					    	}
					    }
						
					//}
						double totalAmt=0;
						if(accounts.getCharges()!=null){
						    if(!accounts.getCharges().equals("")){
						    	  totalAmt=Double.parseDouble(accounts.getCharges());
						    }
						} 
				    double perval=totalAmt*userper/100;
				    accounts.setCharges(DateTimeUtils.changeFormat(perval));
				   
				    totalAmt=perval*accounts.getQuantity();
				    accounts.setTotalAmount(totalAmt);
				    double discTotal=totalAmt*discount/100;
				    double shareamt=totalAmt-discTotal;
				    
				    accounts.setShareAmt(shareamt);
				    accounts.setDiscount(discTotal);
				    
				    
				    list.add(accounts);
			}
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}
	
	

	public int getUserSharePer(int pracId) {
		
		int result=0;
		try {
			String sql="select completeAppCharge from apm_user where id="+pracId+"";
			PreparedStatement ps=connection.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()){
				  result=rs.getInt(1);
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		return result;
	}
	
	
	
	
	private String getWardFromIpd(String ipdid) {
		
		String wardname="";
		BedDao bedDao=new JDBCBedDao(connection);
		try {
			
		     	Bed bed=bedDao.getEditIpdData(ipdid);
		     	wardname=bedDao.getWard(bed.getWardid()).getWardname();
		 
		} catch (Exception e) {

			e.printStackTrace();
		}
		return wardname;
	}
	
	
	public NotAvailableSlot getOtAppointmentDetails(int appointemntid){
		
		NotAvailableSlot notAvailableSlot=new NotAvailableSlot();
		
		try {
			
			String sql="select commencing,status,diaryuserid,diaryusername,reasonforblock,clientId,charge,otid,category,procedures,surgeon,anesthesia from apm_available_slot where id="+appointemntid+"";
			PreparedStatement ps=connection.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()){
				 
				 notAvailableSlot.setCommencing(rs.getString(1));
				 notAvailableSlot.setStatus(rs.getString(2));
				 notAvailableSlot.setDiaryUserId(rs.getInt(3));
				 notAvailableSlot.setDiaryUser(rs.getString(4));
				 notAvailableSlot.setReasonforblock(rs.getString(5));
				 notAvailableSlot.setClientId(rs.getString(6));
				 notAvailableSlot.setCharge(rs.getDouble(7));
				 notAvailableSlot.setOtid(rs.getInt(8));
				 notAvailableSlot.setCategory(rs.getString(9));
				 notAvailableSlot.setProcedure(rs.getString(10));
				 notAvailableSlot.setSurgeon(rs.getString(11));
				 notAvailableSlot.setAnesthesia(rs.getString(12));
				
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		
		return notAvailableSlot;
	}
	
	
	public int getAssistantDoctorId(int id){
		
		int result=0;
		try {
			
			String sql="select assistantdoctor from apm_ot_parent where apmtid="+id+"";
			PreparedStatement ps=connection.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()){
				  
				result=rs.getInt(1);
			}
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		
		return result;
	}
	
	
	
    public boolean isProcedureChargeType(String chargeType){
    	
    	boolean isresult=false;
    	try {
		
    		String sql="select procedures from apm_newchargetype where name='"+chargeType+"'";
    		PreparedStatement ps=connection.prepareStatement(sql);
    		ResultSet rs=ps.executeQuery();
    		while(rs.next()){
    			 
    			 isresult=rs.getBoolean(1);
    		}
    		
		} catch (Exception e) {

			e.printStackTrace();
			
		}
    	
    	return isresult;
    }
	
    public ArrayList<Accounts> getSmallPaymentReportList(String fromDate,
			String toDate,String payby,String howpaid,String orderby,String order,String invcategory,String userid, String selectedInvctype) {
		String tp = "Third Party";String self = "Client";
		PreparedStatement preparedStatement = null;
		ArrayList<Accounts>list = new ArrayList<Accounts>();
		StringBuffer sql = new StringBuffer();
		
		if(payby!=null){
			if(!payby.equals("0")){
				if(payby.equals(Constants.PAY_BY_CLIENT)){
					sql.append("select apm_charges_payment.id,apm_charges_payment.clientid,chargeinvoiceid,payment,paymode,apm_charges_payment.date,apm_charges_payment.tpid,payby,firstname,apm_charges_payment.userid,apm_charges_invoice.itype,apm_charges_payment.ipdno,apm_charges_payment.opdno,apm_charges_payment.invno,apm_charges_payment.cyear,paymentnote,chequeno,cancelinv,cancelNotes,concat(his_invoice_payment_sno.cyear,'/',his_invoice_payment_sno.sno)  ");
					sql.append("FROM apm_charges_payment ");
					sql.append("inner join apm_charges_invoice on apm_charges_invoice.id = apm_charges_payment.chargeinvoiceid ");
					sql.append("inner join apm_patient on apm_patient.id = apm_charges_payment.clientid ");
					sql.append(" left join his_invoice_payment_sno on his_invoice_payment_sno.paymentid= apm_charges_payment.id ");
				}else{
					sql.append("select apm_charges_payment.id,apm_charges_payment.clientid,chargeinvoiceid,payment,paymode,apm_charges_payment.date,apm_charges_payment.tpid,payby,company_name,apm_charges_payment.userid,apm_charges_invoice.itype,apm_charges_payment.ipdno,apm_charges_payment.opdno,apm_charges_payment.invno,apm_charges_payment.cyear,paymentnote,chequeno,cancelinv,cancelNotes,concat(his_invoice_payment_sno.cyear,'/',his_invoice_payment_sno.sno)  ");
					sql.append("FROM apm_charges_payment ");
					sql.append("inner join apm_charges_invoice on apm_charges_invoice.id = apm_charges_payment.chargeinvoiceid ");
					sql.append("inner join apm_third_party_details on apm_third_party_details.id = apm_charges_payment.tpid ");
					sql.append(" left join his_invoice_payment_sno on his_invoice_payment_sno.paymentid= apm_charges_payment.id ");
				}
			}else{
				sql.append("select apm_charges_payment.id,apm_charges_payment.clientid,chargeinvoiceid,payment,paymode,apm_charges_payment.date,apm_charges_payment.tpid,payby,apm_charges_payment.userid,apm_charges_invoice.itype,apm_charges_payment.ipdno,apm_charges_payment.opdno,apm_charges_payment.invno,apm_charges_payment.cyear,paymentnote,chequeno,cancelinv,cancelNotes,concat(his_invoice_payment_sno.cyear,'/',his_invoice_payment_sno.sno)  ");
				sql.append("FROM apm_charges_payment ");
				sql.append("inner join apm_charges_invoice on apm_charges_invoice.id = apm_charges_payment.chargeinvoiceid ");
				sql.append(" left join his_invoice_payment_sno on his_invoice_payment_sno.paymentid= apm_charges_payment.id ");
			}
		}else{
			sql.append("select apm_charges_payment.id,apm_charges_payment.clientid,chargeinvoiceid,payment,paymode,apm_charges_payment.date,apm_charges_payment.tpid,payby,apm_charges_payment.userid,apm_charges_invoice.itype,apm_charges_payment.ipdno,apm_charges_payment.opdno,apm_charges_payment.invno,apm_charges_payment.cyear,paymentnote,chequeno,cancelinv,cancelNotes,concat(his_invoice_payment_sno.cyear,'/',his_invoice_payment_sno.sno) ");
			sql.append("FROM apm_charges_payment ");
			sql.append("inner join apm_charges_invoice on apm_charges_invoice.id = apm_charges_payment.chargeinvoiceid ");
			sql.append(" left join his_invoice_payment_sno on his_invoice_payment_sno.paymentid= apm_charges_payment.id ");
		}
	
	
		
		
		if(!fromDate.equals("") && !toDate.equals("")){
			
			/*if(fromDate.equals(toDate)){
				toDate = toDate + " " + "23:59:59";
			}else{
				if(!toDate.equals("")){
					String dt = toDate;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Calendar c = Calendar.getInstance();
					try {
						c.setTime(sdf.parse(dt));
						c.add(Calendar.DATE, 1);  // number of days to add
						dt = sdf.format(c.getTime());  // dt is now the new date
						toDate = dt + " " + "23:59:59";
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
			}*/
			toDate = toDate + " " + "23:59:59";
			
			//sql.append("select id,clientid,chargeinvoiceid,payment,paymode,date,tpid from apm_charges_payment where date between '"+fromDate+"' AND '"+toDate+"' order by id desc ") ;
			
			sql.append("where apm_charges_payment.date between '"+fromDate+"' AND '"+toDate+"' ");
			
			
			if(payby!=null){
				if(!payby.equals("0") && !howpaid.equals("0") ){
					sql.append("and payby='"+payby+"' and paymode='"+howpaid+"' ");
				}else if(!payby.equals("0")){
					sql.append("and payby='"+payby+"' " );
				}else if(!howpaid.equals("0")){
					sql.append("and paymode='"+howpaid+"' " );
				}
			}
		}else{
			
			if(payby!=null){
				if(!payby.equals("0") && !howpaid.equals("0") ){
					sql.append("where payby='"+payby+"' and paymode='"+howpaid+"' ");
				}else if(!payby.equals("0")){
					sql.append("where payby='"+payby+"' " );
				}else if(!howpaid.equals("0")){
					sql.append("where paymode='"+howpaid+"' " );
				}
			}
			
		}
		
		if(!invcategory.equals("2")){
			
			sql.append("and apm_charges_invoice.invpstype="+invcategory+" ");
		}
		
		if(userid!=null){
			if(!userid.equals("0")){
				sql.append("and apm_charges_payment.userid='"+userid+"' ");
			}
		}
		if(!selectedInvctype.equals("0")){
			sql.append(" and itype in("+selectedInvctype+") ");
		}
		sql.append("and paymode!='prepayment' order by "+orderby+" "+order+" ");
		 
		try{
			double totalOfTotal=0;
			preparedStatement = connection.prepareStatement(sql.toString());
			ResultSet rs = preparedStatement.executeQuery();
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			while(rs.next()){
				Accounts accounts = new Accounts();
				accounts.setId(rs.getInt(1));
				accounts.setClientid(rs.getInt(2));
				String clientName = getClientFullName(rs.getInt(2));
				accounts.setClientName(clientName);
				accounts.setInvoiceid(rs.getInt(3));
				
				String invoicenameid=accountsDAO.getInvoiceTypeId(rs.getInt(3));
				int ipdopdseq=accountsDAO.getIpdOpdSeqNo((rs.getInt(3)));
				String invoicename=accountsDAO.getInvoiceName(invoicenameid);
				accounts.setInvoicenameid(invoicename);
				accounts.setIpdopdseq(""+ipdopdseq);
				String abrivationid =getClientAbrivationId(rs.getInt(2));
				accounts.setAbrivationid(abrivationid);
				
				accounts.setAmount(rs.getDouble(4));
				accounts.setFinalTotal(totalOfTotal+accounts.getAmount());
				totalOfTotal=accounts.getFinalTotal();
				accounts.setAmountx(DateTimeUtils.changeFormat(rs.getDouble(4)));
				accounts.setPaymentmode(rs.getString(5));
				accounts.setDate(DateTimeUtils.getIndianDateTimeFormat(rs.getString(6)));
				String whopay = getWhoPay(rs.getInt(3));
				accounts.setWhoPay(whopay);
				if(whopay.equals(tp)){
					String invoicee = getThirdPartyCompanyName(rs.getString(7));
					accounts.setInvoicee(invoicee);
				}
				else{
					accounts.setInvoicee(clientName);
				}
				accounts.setUserid(rs.getString(9));
				String itype=rs.getString(10);
				String ipdid=rs.getString(11);
				String opdid=rs.getString(12);
				String invstid=rs.getString(13);
				String cyear=rs.getString(14);
				
//				if(itype!=null){
//					if(itype.equals("2")){
//						type1=cyear+"/"+ipdid;
//					}else if(itype.equals("1")){
//						type1=cyear+"/"+opdid;
//					}else if(itype.equals("3")){
//						type1=cyear+"/"+invstid;
//					}else{
//						type1=String.valueOf(accounts.getId());
//					}
//				accounts.setNewsr(type1);	
//				}else{
					accounts.setNewsr(String.valueOf(accounts.getId()));	
//				}
				String paymentnote=rs.getString(15);
				String chequeno=rs.getString(16);
				if(rs.getString(5).equals("Cheque"))
				{
					accounts.setPaymentNote(chequeno);
				}else{
					accounts.setPaymentNote(paymentnote);
				}
				accounts.setCancelsts(rs.getString(17));
				accounts.setCancelNotes(DateTimeUtils.isNull(rs.getString(18)));
				String type1="";
				if(rs.getString(19)!=null){
					type1= rs.getString(19);
					accounts.setNewsr(type1);	
				}else{
					accounts.setNewsr(String.valueOf(accounts.getId()));	
				}
				accounts.setPhysical_payment_id(getPhysicalpaymentId(rs.getString(1)));
				list.add(accounts);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return list;
	}

	public double getRefForInvoicet(String fromDate, String toDate, int i,
			String payby, String howpaid, String invoicecategory, String userid) {
		PreparedStatement preparedStatement = null;
		double result  = 0;
		toDate = toDate + " 23:59:59";
		String sql = "select sum(debit) from apm_credit_account where date between '"+fromDate+"' and '"+toDate+"' " +
				" and refinvoiceid !=0 and apm_credit_account.userid='"+userid+"' ";
		StringBuffer buffer = new StringBuffer();
		buffer.append("select sum(debit) from apm_credit_account where date between '"+fromDate+"' and '"+toDate+"' ");
		buffer.append(" and refinvoiceid !=0 ");
		if(userid!=null){
			buffer.append("and apm_credit_account.userid='"+userid+"' ");
		}
		
		try{
			preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				result = rs.getDouble(1);
			}
			
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public double getRefOnly(String fromDate, String toDate, int i,
			String payby, String howpaid, String invoicecategory, String userid) {
		PreparedStatement preparedStatement = null;
		double result  = 0;
		toDate = toDate + " 23:59:59";
		String sql = "select sum(debit) from apm_credit_account where date between '"+fromDate+"' and '"+toDate+"' " +
				" and advref=1 and apm_credit_account.userid='"+userid+"' ";
		StringBuffer buffer = new StringBuffer();
		buffer.append("select sum(debit) from apm_credit_account where date between '"+fromDate+"' and '"+toDate+"' ");
		buffer.append(" and advref=1 ");
		if(userid!=null){
			if(userid.equals("")||userid.equals("0")){
				
			}else{
				buffer.append("and apm_credit_account.userid='"+userid+"' ");
			}
			
		}
		
		try{
			preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				result = rs.getDouble(1);
			}
			
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	
	public ArrayList<Accounts> getAdvancePaymentReportList(String fromDate,
			String toDate, String payby, String paymentStatus,
			String thirdParty, String orderby, String order,
			String invoicecategory, String invcetype,String userid,String paymode, String shiftedFromCancel) {
		PreparedStatement preparedStatement = null;
		ArrayList<Accounts>list = new ArrayList<Accounts>();
		ClientDAO  clientDAO=new JDBCClientDAO(connection);
		//String sql = "SELECT id,date,payby,payment_mode,charge,debit,apm_credit_accountbalance FROM apm_credit_account where client_id = "+clientId+" and payment_mode IS NOT NULL";
		paymode=DateTimeUtils.isNull(paymode);
		toDate = toDate + " 23:59:59";
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT apm_credit_account.id,apm_credit_account.date,apm_credit_account.payby,payment_mode,charge,apm_credit_account.debit,balance,third_party_name_id,concat(title,' ',firstname,' ',surname),advref,apm_credit_account.resetinv,payment_mode,apm_credit_account.userid,apm_credit_account.cyear,apm_credit_account.advno,concat(his_invoice_payment_sno.cyear,'/',his_invoice_payment_sno.sno),apm_credit_account.credit_note,apm_credit_account.invoiceid, apm_credit_account.cancelpay,itype,apm_charges_payment.id,apm_credit_account.client_id FROM apm_credit_account  ");
		sql.append(" left join apm_charges_invoice on apm_charges_invoice.id = apm_credit_account.invoiceid ");
		sql.append(" left join apm_charges_payment on apm_charges_payment.chargeinvoiceid=apm_credit_account.invoiceid ");
		sql.append(" left join apm_patient on apm_patient.id = apm_credit_account.client_id ");
		sql.append(" left join his_invoice_payment_sno on his_invoice_payment_sno.invoiceid=apm_credit_account.id ");
		sql.append(" where payment_mode IS NOT NULL and apm_credit_account.date between '"+fromDate+"' and '"+toDate+"' and charge>0 and his_invoice_payment_sno.paymentid=0  and payment_mode!='prepayment' ");
		
		if(userid!=null){
			if(userid.equals("0")||userid.equals("")){
				
			}else{
				sql.append("and apm_credit_account.userid='"+userid+"' ");	
			}
			
		}
		if(!(paymode.equals("")||paymode.equals("0"))){
			
			sql.append("  and payment_mode='"+paymode+"' ");	
		}
		sql.append("  and cancelpay='"+shiftedFromCancel+"' ");
		sql.append(" group by apm_credit_account.id order by resetinv ");
		
		try{
			double tot=0;
			preparedStatement = connection.prepareStatement(sql.toString());
			ResultSet rs = preparedStatement.executeQuery();
			AdditionalDAO additionalDAO= new JDBCAdditionalDAO(connection);
			double advtotal = 0;
			while(rs.next()){
				Accounts accounts = new Accounts();
				accounts.setId(rs.getInt(1));
				accounts.setBghseqId(additionalDAO.getBalgopalSeqNum(""+accounts.getId()));
				String date = DateTimeUtils.getIndianDateTimeFormat(rs.getString(2));
				accounts.setDate(date);
				String temp[] = date.split(" ");		
				accounts.setCommencing(temp[0]);
				accounts.setPayby(rs.getString(3));
				accounts.setPaymentmode(rs.getString(4));
				accounts.setCharges(DateTimeUtils.changeFormat(Double.parseDouble(rs.getString(5))));
				advtotal = advtotal + rs.getDouble(5);
				accounts.setDebitTotalx(DateTimeUtils.changeFormat(Double.parseDouble(rs.getString(6))));
				accounts.setAdvreftotal(DateTimeUtils.changeFormat(advtotal));
				
				double credit = rs.getDouble(5);
				double debit = rs.getDouble(6);
				double balance = 0;
				
				accounts.setDebitAmount(debit);
				accounts.setCreditAmount(credit);
				accounts.setBalance(balance);
				
				if(rs.getString(3).equals(Constants.PAY_BY_THIRD_PARTY)){
					
					CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(connection);
				    CompleteAppointment	completeAppointment = completeAptmDAO.getInsuranceCompanyName(rs.getString(8));
				    accounts.setClientName(completeAppointment.getInsuranceCompanyName());
				}
				else{
					
					accounts.setClientName(rs.getString(9));
				}
				
				
				accounts.setBalancex(DateTimeUtils.changeFormat(balance));
				accounts.setAdvref(rs.getInt(10));
				accounts.setResetinv(rs.getString(11));
				accounts.setPaymentmode(rs.getString(12));
				accounts.setUserid(rs.getString(13));
				if(rs.getString(16)!=null){
					accounts.setAdvno(rs.getString(16));
				}else{
				accounts.setAdvno(rs.getString(14)+"/"+rs.getString(15));
				}
				accounts.setRemark(rs.getString(17));
				accounts.setPhysical_payment_id(getPhysicalpaymentIdAdvRef(rs.getString(1)));
				accounts.setCancelsts(rs.getString(19));
				String receipt=getnewpaymentreceipt(rs.getInt(18));
				String newreceipt=getPhysicalpaymentIdbyinvoiceid(rs.getInt(18));
				accounts.setNewshftcharge(newreceipt);
				if(rs.getInt(20)==1){
					accounts.setReceiptid(rs.getString(21));
				}else{
				accounts.setReceiptid(receipt);
				
				}
				//for payment report
				accounts.setItype("5");
				accounts.setAmountx(rs.getString(5));
				double payAmt = rs.getDouble(5);
				tot = tot + payAmt;
				accounts.setTotalamt(String.valueOf(tot));
				Client client = clientDAO.getClientDetails(rs.getString(22));
				accounts.setAbrivationid(client.getAbrivationid());
				list.add(accounts);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	public String getPhysicalpaymentIdbyinvoiceid(int payid){
		String res="0";
		try {
			PreparedStatement ps= connection.prepareStatement(" select id from his_payment_record_physical where invoiceid='"+payid+"'   ");
			ResultSet rs= ps.executeQuery();
			while(rs.next()){
				res=""+rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	private String getnewpaymentreceipt(int int1) {
		PreparedStatement preparedStatement = null;
		String sql = "select concat(his_invoice_payment_sno.cyear,'/',his_invoice_payment_sno.sno) from his_invoice_payment_sno where invoiceid="+int1+" ";
		
		String result = "";
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				result = rs.getString(1);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
	}

	public ArrayList<Accounts> getRefundPaymentReportList(String fromDate,
			String toDate, String payby, String paymentStatus,
			String thirdParty, String orderby, String order,
			String invoicecategory, String string, String userid,String selectedInvctype, String paymode) {
		PreparedStatement preparedStatement = null;
		ArrayList<Accounts>list = new ArrayList<Accounts>();
		//String sql = "SELECT id,date,payby,payment_mode,charge,debit,apm_credit_accountbalance FROM apm_credit_account where client_id = "+clientId+" and payment_mode IS NOT NULL";
		paymode=DateTimeUtils.isNull(paymode);
		toDate = toDate + " 23:59:59";
		
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT apm_credit_account.id,date,payby,payment_mode, ");
		sql.append("charge,debit,balance,third_party_name_id,concat(title,' ',firstname,' ',surname),advref,resetinv,payment_mode,userid,concat(his_invoice_payment_sno.cyear,'/',his_invoice_payment_sno.sno),apm_credit_account.manualinvoiceid,client_id  ");
		sql.append("FROM apm_credit_account inner join apm_patient on apm_patient.id = apm_credit_account.client_id ");
		sql.append(" left join his_invoice_payment_sno on his_invoice_payment_sno.invoiceid=apm_credit_account.id ");
		sql.append("where payment_mode IS NOT NULL and date between '"+fromDate+"' and '"+toDate+"' and ");
		sql.append("advref=1 and his_invoice_payment_sno.paymentid=0  ");
		if(userid!=null){
			if(userid.equals("0")||userid.equals("")){
				
			}else{
				sql.append(" and userid = '"+userid+"'"); 
			}
			
		}
		
		if(!selectedInvctype.equals("0")){
			if(selectedInvctype.contains("0,")){
				String val="";
				for (String str : selectedInvctype.split(",")) {
					if(str.equals("0")){
						continue;
					}
					if(val.equals("")){
						val=str;
					}else{
						val=val+","+str;
					}
					selectedInvctype=val;
				}
			}
		}
//			String temp1[] = selectedInvctype.split(",");
//			if(temp1.length>1){
		if(!selectedInvctype.equals("0")){
				//selectedInvctype = selectedInvctype.substring(selectedInvctype.length()-1);
				sql.append(" and invtypenew in("+selectedInvctype+") ");
		}
//		if(selectedInvctype!=null){
//			if(selectedInvctype.equals("0") || selectedInvctype.equals("")){
//				
//			}else{
//				sql.append(" and invoice_type = '"+selectedInvctype+"'"); 
//			}
//		}
		
		if(!(paymode.equals("")||paymode.equals("0"))){
			sql.append("  and payment_mode='"+paymode+"' ");	
		}
		
		sql.append(" order by id desc ");
		try{
			
			preparedStatement = connection.prepareStatement(sql.toString());
			ResultSet rs = preparedStatement.executeQuery();
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			ClientDAO clientDAO= new JDBCClientDAO(connection);
			double advtotal = 0;
			while(rs.next()){
				Accounts accounts = new Accounts();
				accounts.setId(rs.getInt(1));
				String date = DateTimeUtils.getIndianDateTimeFormat(rs.getString(2));
				accounts.setDate(date);
				String temp[] = date.split(" ");		
				accounts.setCommencing(temp[0]);
				accounts.setPayby(rs.getString(3));
				accounts.setPaymentmode(rs.getString(4));
				accounts.setCharges(DateTimeUtils.changeFormat(Double.parseDouble(rs.getString(5))));
				advtotal = advtotal + rs.getDouble(5);
				accounts.setDebitTotalx(DateTimeUtils.changeFormat(Double.parseDouble(rs.getString(6))));
				accounts.setAdvreftotal(DateTimeUtils.changeFormat(advtotal));
				Client client= clientDAO.getClientDetails(rs.getString(16));
				accounts.setAbrivationid(client.getAbrivationid());
				double credit = rs.getDouble(5);
				double debit = rs.getDouble(6);
				double balance = 0;
				
				
				
				accounts.setDebitAmount(debit);
				accounts.setCreditAmount(credit);
				accounts.setBalance(balance);
				
				if(rs.getString(3).equals(Constants.PAY_BY_THIRD_PARTY)){
					
					CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(connection);
				    CompleteAppointment	completeAppointment = completeAptmDAO.getInsuranceCompanyName(rs.getString(8));
				    accounts.setClientName(completeAppointment.getInsuranceCompanyName());
				}
				else{
					
					accounts.setClientName(rs.getString(9));
				}
				
				
				accounts.setBalancex(DateTimeUtils.changeFormat(balance));
				accounts.setAdvref(rs.getInt(10));
				accounts.setResetinv(rs.getString(11));
				accounts.setPaymentmode(rs.getString(12));
				accounts.setUserid(rs.getString(13));
				accounts.setRefid(rs.getString(14));
				if(rs.getString(15)==null){
					accounts.setManualinvoiceid("0");
				}else{
					if(rs.getString(15).equals("")){
						accounts.setManualinvoiceid("0");
					}else{
						 int ipdopdseq=accountsDAO.getIpdOpdSeqNo((rs.getInt(15)));
						 accounts.setIpdopdseq(String.valueOf(ipdopdseq));
						 accounts.setManualinvoiceid(rs.getString(15));
						 String invoicenameid=accountsDAO.getInvoiceTypeId(rs.getInt(15));
						 String invoicename=accountsDAO.getInvoiceName(invoicenameid);
						 accounts.setInvoicenameid(invoicename);
					}
				}
				accounts.setPhysical_payment_id(getPhysicalpaymentIdAdvRef(rs.getString(1)));
				list.add(accounts);
			}
			
			//Akash 28 May 2018 for test
			/*ArrayList<Accounts>reuinvoiceList = getRefInvoiceList( fromDate,
					 toDate,  payby,  paymentStatus,
					 thirdParty,  orderby,  order,
					 invoicecategory,  string,  userid);
			
			list.addAll(reuinvoiceList);*/
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	private ArrayList<Accounts> getRefInvoiceList(String fromDate,
			String toDate, String payby, String paymentStatus,
			String thirdParty, String orderby, String order,
			String invoicecategory, String string, String userid) {
		// TODO Auto-generated method stub
		PreparedStatement preparedStatement = null;
		ArrayList<Accounts>list = new ArrayList<Accounts>();
		//String sql = "SELECT id,date,payby,payment_mode,charge,debit,apm_credit_accountbalance FROM apm_credit_account where client_id = "+clientId+" and payment_mode IS NOT NULL";
		
		toDate = toDate + " 23:59:59";
		
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT apm_credit_account.id,date,payby,payment_mode, ");
		sql.append("charge,debit,balance,third_party_name_id,concat(title,' ',firstname,' ',surname),advref,resetinv,payment_mode,userid ");
		sql.append("FROM apm_credit_account inner join apm_patient on apm_patient.id = apm_credit_account.client_id ");
		sql.append("where payment_mode IS NOT NULL and date between '"+fromDate+"' and '"+toDate+"' and ");
		sql.append("refinvoiceid>0  and userid = '"+userid+"' order by id desc ");
		try{
			
			preparedStatement = connection.prepareStatement(sql.toString());
			ResultSet rs = preparedStatement.executeQuery();
			
			double advtotal = 0;
			while(rs.next()){
				Accounts accounts = new Accounts();
				accounts.setId(rs.getInt(1));
				String date = DateTimeUtils.getIndianDateTimeFormat(rs.getString(2));
				accounts.setDate(date);
				String temp[] = date.split(" ");		
				accounts.setCommencing(temp[0]);
				accounts.setPayby(rs.getString(3));
				accounts.setPaymentmode(rs.getString(4));
				accounts.setCharges(DateTimeUtils.changeFormat(Double.parseDouble(rs.getString(5))));
				advtotal = advtotal + rs.getDouble(5);
				accounts.setDebitTotalx(DateTimeUtils.changeFormat(Double.parseDouble(rs.getString(6))));
				accounts.setAdvreftotal(DateTimeUtils.changeFormat(advtotal));
				
				double credit = rs.getDouble(5);
				double debit = rs.getDouble(6);
				double balance = 0;
				
				accounts.setDebitAmount(debit);
				accounts.setCreditAmount(credit);
				accounts.setBalance(balance);
				
				if(rs.getString(3).equals(Constants.PAY_BY_THIRD_PARTY)){
					
					CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(connection);
				    CompleteAppointment	completeAppointment = completeAptmDAO.getInsuranceCompanyName(rs.getString(8));
				    accounts.setClientName(completeAppointment.getInsuranceCompanyName());
				}
				else{
					
					accounts.setClientName(rs.getString(9));
				}
				
				
				accounts.setBalancex(DateTimeUtils.changeFormat(balance));
				accounts.setAdvref(rs.getInt(10));
				accounts.setResetinv(rs.getString(11));
				accounts.setPaymentmode(rs.getString(12));
				accounts.setUserid(rs.getString(13));
				list.add(accounts);
			}
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<Master> getAccountUserList() {
		PreparedStatement preparedStatement = null;
		ArrayList<Master>list = new ArrayList<Master>();
		String sql = "select userid,jobtitle from apm_user where jobtitle = 'Accounts' ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Master master = new Master();
				master.setUserid(rs.getString(1));
				list.add(master);
			}
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}
	// Adarsh
	public int saveMisReportLog(String string, String userId, String fromDate, String toDate,String date, String string1
			) {
		int result=0;
		try {
			String sql = "insert into mis_report_log(report_name, userid, from_date, to_date, date, method_name) values(?,?,?,?,?,?)";
			PreparedStatement ps = connection.prepareStatement(sql);
			MisReport misReport = new MisReport();
			ps.setString(1, string);
			ps.setString(2, userId);
			ps.setString(3, fromDate);
			ps.setString(4, toDate);
			ps.setString(5, date);
			ps.setString(6, string1);
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public ArrayList<Accounts> getChargesReportDeatiled(String fromDate, String toDate, String payby, String tpid,
			   String invoiceStatus, String orderby, String order, String jobtitle, String apmttype,String ChargesType,String user,String opdipd,String clientid,String searchinv,String dept) {
			  ArrayList<Accounts> list= new ArrayList<Accounts>();
			  PreparedStatement ps= null;
			  try {
			   StringBuffer buffer= new StringBuffer();
			 /*  buffer.append(" SELECT commencing,apm_invoice.practitionername,apmtType,charge,apm_invoice_assesments.invoiceid,paid,apm_invoice_assesments.id,apm_invoice.practitionerid,apm_invoice_assesments.paybuy,apm_invoice_assesments.clientId,apm_invoice.chargetype,howpaid,apm_invoice.appointmentid,department,apm_invoice_assesments.thirdPartyId,apm_invoice.date,apm_invoice_assesments.masterchargetype,apm_invoice_assesments.quantity,apm_charges_assesment.chargeinvoiceid ");
			   buffer.append(" ,apm_charges_invoice.debit,apm_charges_invoice.discamt,apm_charges_invoice.date,apm_charges_invoice.id , apm_charges_invoice.itype,apm_invoice_assesments.practitionerId,apm_invoice_assesments.wardid,ipdseqno ");
			   buffer.append(" FROM apm_invoice_assesments ");
			   buffer.append(" inner join apm_invoice on apm_invoice.id = apm_invoice_assesments.invoiceid ");
			   buffer.append(" inner join apm_patient on apm_patient.id = apm_invoice_assesments.clientId ");
			   buffer.append(" inner join apm_charges_assesment on apm_charges_assesment.invoiceid= apm_invoice_assesments.invoiceid ");
			   buffer.append(" inner join apm_charges_invoice on apm_charges_invoice.id=apm_charges_assesment.chargeinvoiceid   ");
			   buffer.append(" where ");*/
			   
			   buffer.append("SELECT commencing,concat(initial,' ',apm_user.firstname,' ',lastname),apmtType,charge,apm_invoice_assesments.invoiceid, ");
			   buffer.append("paid,apm_invoice_assesments.id,apm_charges_invoice.practid,apm_invoice_assesments.paybuy, ");
			   buffer.append("apm_invoice_assesments.clientId,apm_invoice.chargetype,howpaid,apm_invoice.appointmentid,apm_user.discription, ");
			   buffer.append("apm_invoice_assesments.thirdPartyId,apm_invoice.date,apm_invoice_assesments.masterchargetype, ");
			   buffer.append("apm_invoice_assesments.quantity,apm_charges_assesment.chargeinvoiceid  ,apm_charges_invoice.debit, ");
			   buffer.append("apm_charges_invoice.discamt,apm_charges_invoice.date,apm_charges_invoice.id , apm_charges_invoice.itype, ");
			   buffer.append("apm_invoice_assesments.practitionerId,apm_invoice_assesments.wardid,ipdseqno,apm_invoice_assesments.chargedisc ");
			   buffer.append("FROM apm_invoice_assesments  inner join apm_invoice on apm_invoice.id = apm_invoice_assesments.invoiceid ");
			   buffer.append("inner join apm_patient on apm_patient.id = apm_invoice_assesments.clientId ");
			   buffer.append("inner join apm_charges_assesment on apm_charges_assesment.invoiceid= apm_invoice_assesments.invoiceid ");
			   buffer.append("inner join apm_charges_invoice on apm_charges_invoice.id=apm_charges_assesment.chargeinvoiceid ");
			   buffer.append("inner join apm_user on apm_charges_invoice.practid = apm_user.id ");
			   buffer.append("where apm_charges_invoice.date between '"+fromDate+"' AND '"+toDate+"'  ");
			  
			   
			   
			   
			   if(!searchinv.equals("")){
				   buffer.append(" and apm_charges_invoice.id='"+searchinv+"' ");
			   }
			// if(searchinv.equals("")){
			  
			   buffer.append(" and  apm_invoice_assesments.active=1 ");
			   if(!apmttype.equals("")){
			    buffer.append(" and apmtType in( "+apmttype+" )");
			   }
			   if(!ChargesType.equals("")){
			    buffer.append(" and chargetype='"+ChargesType+"'");
			   }
			   if(!user.equals("")){
				   buffer.append(" and apm_charges_invoice.practid='"+user+"' ");
			   }
			   if(!opdipd.equals("")){
				   if(opdipd.equals("0")){
					   buffer.append("  and apm_charges_invoice.itype ='2' ");
				   }else if(opdipd.equals("2")){
					   buffer.append("  and apm_charges_invoice.itype ='3' ");
				   }else if(opdipd.equals("3")){
					   buffer.append("  and apm_charges_invoice.itype ='8' ");
				   }
				   else{
					   buffer.append(" and apm_charges_invoice.itype ='1' ");
				   }
				   
			   }
			   buffer.append("  and apm_charges_invoice.chargetype='Submit' ");
			   if(!clientid.equals("")){
				   buffer.append(" and apm_invoice.clientid='"+clientid+"'");
			   }
			   if(dept==null){
				   dept="0";   
			   }
			   if(dept.equals("")){
				   dept="0";
			   }
				if(!dept.equals("0")){
						buffer.append(" and apm_user.discription='"+dept+"' ");
					}
			   
			   
			   CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(connection);
			   String sql="";
			   sql= buffer.toString();
			   ps=connection.prepareStatement(sql);
			   ResultSet rs= ps.executeQuery();
			   double totaloftotal=0;
			   while(rs.next()){
			    Accounts accounts = new Accounts();
			    accounts.setCommencing(DateTimeUtils.getCommencingDate1(rs.getString(1)));
			    
			    int invcid=rs.getInt(23);
			    if(rs.getString(2)==null){
			    	
			    	String usernamebyid=getUserNameById(invcid);
			    	accounts.setUsername(usernamebyid);
			    	}else if(rs.getString(2).equals("")){
			    		String usernamebyid=getUserNameById(invcid);
				    	accounts.setUsername(usernamebyid);
				    	
			    }else{
			    	 accounts.setUsername(rs.getString(2));
			    }
			    accounts.setAppointmentType(rs.getString(3));
			    accounts.setCreditCharge(rs.getString(4));
			    accounts.setInvoiceid(rs.getInt(5));
			    accounts.setPaid(rs.getBoolean(6));
			    accounts.setNumberOfChages(rs.getInt(7));
			  
			   
			    accounts.setPractitionerName(rs.getString(8));
			    if(accounts.getPractitionerName()==null){
			    	 accounts.setPractitionerName("");
			    }
			    if(rs.getString(9)!=null){
				    if(rs.getString(9).equals("1")){
				     accounts.setWhoPay("Third Party");
				     
				     CompleteAppointment completeAppointment =  completeAptmDAO.getInsuranceCompanyName(rs.getString(10));
				     String tpName = completeAppointment.getInsuranceCompanyName();
				     accounts.setTpName(tpName);
				     
				    }
			    }
			    else{
			     accounts.setWhoPay("Self");
			    }
			    accounts.setClientid(rs.getInt(10));
			
			    ClientDAO clientDAO= new JDBCClientDAO(connection);
			   String clientname= clientDAO.getClientFullName(String.valueOf(accounts.getClientid()));
			   accounts.setClientName(clientname);
			    accounts.setChargeType(rs.getString(11));
			    accounts.setHowPaid(rs.getString(12));
			    accounts.setApptId(rs.getString(13));
			    
			    String locationName = getChargeDepartmentName(rs.getInt(8));
			    accounts.setLocationName(locationName);
			    
			    String treamentEpisodeName = getTreatmentEpisodeName(rs.getString(13));
			    accounts.setTreatmentEpisodeName(treamentEpisodeName);
			    
			    
			    
			    
			    
			    boolean chargesInvoiced = checkChargeInvoiced(accounts.getInvoiceid());
			    accounts.setChargesInvoiced(chargesInvoiced);
			    accounts.setInvdate((DateTimeUtils.getCommencingDate1(rs.getString(16))));
			    accounts.setChargeType(rs.getString(17));
			    accounts.setQuantity(rs.getInt(18));
			    accounts.setTotalAmount(rs.getDouble(18)*rs.getDouble(4));
			    accounts.setTotalrecammount(accounts.getTotalAmount()+totaloftotal);
			    totaloftotal= accounts.getTotalrecammount();
			    accounts.setNewsr(rs.getString(19));
			    if(rs.getDouble(28)>0){
			    	 accounts.setDiscount(rs.getDouble(28));
			    }else{
			    accounts.setDiscount(rs.getDouble(21));
			    }
			    accounts.setDebitAmount(rs.getDouble(20));
			    accounts.setInvdate(DateTimeUtils.getCommencingDate1(rs.getString(22)));
			    if(rs.getInt(24)==1){
			    	accounts.setItype("Opd");	
			    }else if(rs.getInt(24)==2){
			    	accounts.setItype("Ipd");	
			    }else if(rs.getInt(24)==8){
			    	accounts.setItype("DC");	
			    }
			    else{
			    	accounts.setItype("INVST");	
			    }
			    
			    UserProfileDAO userProfileDAO= new JDBCUserProfileDAO(connection);
			    String chgreuser= userProfileDAO.getFullName(rs.getString(25));
			    accounts.setFirstname(chgreuser);
			    String wardname=completeAptmDAO.getwardnamebywardid(rs.getInt(26));
			    if(wardname==null){
			    	wardname="";
			    }
			    accounts.setWard(wardname);
			    accounts.setIpdopdseq(rs.getString(27));
			   
			    list.add(accounts);
			   
			   
			    }
			   //}
			  } catch (Exception e) {
			   e.printStackTrace();
			  }
			  
			  return list;
			 }
	
	//lokesh 27-7-2018
	public ArrayList<Accounts> getpaymentreciptlist(String fromdate, String todate, String paymenttype, String userid,String invoicetype) {
		ArrayList<Accounts> paymentrecieptlist= new ArrayList<Accounts>();
		PreparedStatement ps= null;
		try {
			fromdate= DateTimeUtils.getCommencingDate1(fromdate);
			todate= DateTimeUtils.getCommencingDate1(todate)+" 23.59.59";
			
			StringBuffer buffer= new StringBuffer();
			buffer.append("   select a.id, a.invoiceid, a.paymentid, a.invtype, a.sno, a.cyear,    ");
			buffer.append("    b.date, b.client_id, b.payment_mode,b.charge,   ");
			buffer.append("    c.date, c.clientid,  c.paymode, c.payment,  ");
			buffer.append("    concat(e.firstname,' ',e.surname), concat('',f.firstname,' ',f.lastname) ,b.debit  ");
			
			buffer.append("    from his_invoice_payment_sno a   ");
			
			buffer.append("    left join apm_credit_account b on b.id= a.invoiceid   ");
			buffer.append("    left join apm_charges_payment  c on c.id = a.paymentid   ");
			
			buffer.append("    left join apm_patient e on e.id =b.client_id or e.id =c.clientid   ");
			buffer.append("    left join apm_user f on f.userid= b.userid or f.userid=c.userid   ");
			
			buffer.append("    where a.sno is not null   ");
			
			if(fromdate!=null&&todate!=null){
				buffer.append("    and (( b.date between '"+fromdate+"' and '"+todate+"')  or (   c.date between '"+fromdate+"' and '"+todate+"'))  ");
			}
			if(!paymenttype.equals("")){
				buffer.append("   and (( b.payment_mode='"+paymenttype+"') or (c.paymode='"+paymenttype+"'))    ");
			}
			if(!userid.equals("")){
				buffer.append("   and ((b.userid='"+userid+"') or (c.userid='"+userid+"'))   ");
			}
			if(!invoicetype.equals("")){
				buffer.append("   and  a.invtype='"+invoicetype+"' ");
			}
			
			ps= connection.prepareStatement(buffer.toString());
			ResultSet rs=  ps.executeQuery();
			while(rs.next()){
				
				if(rs.getString(4).equals("Advance")){
					Accounts accounts= new Accounts();
					String date[]= rs.getString(7).split(" ");
					accounts.setDate(DateTimeUtils.getCommencingDate1(date[1])+" "+date[0]);
					accounts.setId(rs.getInt(5));
					accounts.setAmount(rs.getDouble(10));
					accounts.setPaymentmode(rs.getString(9));
					accounts.setClientName(rs.getString(15));
					accounts.setUsername(rs.getString(16));
					accounts.setInvoicetype("Advance");
					paymentrecieptlist.add(accounts);
					
				}/*else if(rs.getString(4).equals("Refund")){
					Accounts accounts= new Accounts();
					String date[]= rs.getString(7).split(" ");
					accounts.setDate(DateTimeUtils.getCommencingDate1(date[1])+" "+date[0]);
					accounts.setId(rs.getInt(5));
					accounts.setAmount(rs.getDouble(17));
					accounts.setPaymentmode(rs.getString(9));
					accounts.setClientName(rs.getString(15));
					accounts.setUsername(rs.getString(16));
					accounts.setInvoicetype("Refund");
					paymentrecieptlist.add(accounts);
					
				}*/else if(rs.getString(4).equals("IPD")){
					Accounts accounts= new Accounts();
					String date[]= rs.getString(11).split(" ");
					accounts.setDate(DateTimeUtils.getCommencingDate1(date[1])+" "+date[0]);
					accounts.setId(rs.getInt(5));
					accounts.setAmount(rs.getDouble(14));
					accounts.setPaymentmode(rs.getString(13));
					accounts.setClientName(rs.getString(15));
					accounts.setUsername(rs.getString(16));
					accounts.setInvoicetype("IPD");
					paymentrecieptlist.add(accounts);
					
				}else{
					
				}	
				
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	return paymentrecieptlist;
}

	public int getcountofinvoice(String fromdate, String todate, String paymenttype, String userid,
			String invoicetype) {
		ArrayList<Accounts> paymentrecieptlist= new ArrayList<Accounts>();
		int count=0;
		PreparedStatement ps= null;
		try {
			fromdate= DateTimeUtils.getCommencingDate1(fromdate);
			todate= DateTimeUtils.getCommencingDate1(todate)+" 23.59.59";
			
			StringBuffer buffer= new StringBuffer();
			
			if(invoicetype.equals("Advance")){
			buffer.append("   select sum(b.charge)  ");
			}
			if(invoicetype.equals("Refund")){
			buffer.append("   select sum(b.debit)  ");	
			}
			if(invoicetype.equals("IPD")){
				buffer.append("   select sum(c.payment)  ");	
			}
			buffer.append("    from his_invoice_payment_sno a   ");
			
			buffer.append("    left join apm_credit_account b on b.id= a.invoiceid   ");
			buffer.append("    left join apm_charges_payment  c on c.id = a.paymentid   ");
			
			buffer.append("    left join apm_patient e on e.id =b.client_id or e.id =c.clientid   ");
			buffer.append("    left join apm_user f on f.userid= b.userid or f.userid=c.userid   ");
			
			buffer.append("    where a.sno is not null   ");
			
			if(fromdate!=null&&todate!=null){
				buffer.append("    and (( b.date between '"+fromdate+"' and '"+todate+"')  or (   c.date between '"+fromdate+"' and '"+todate+"'))  ");
			}
			if(!paymenttype.equals("")){
				buffer.append("   and (( b.payment_mode='"+paymenttype+"') or (c.paymode='"+paymenttype+"'))    ");
			}
			if(!userid.equals("")){
				buffer.append("   and ((b.userid='"+userid+"') or (c.userid='"+userid+"'))   ");
			}
			if(!invoicetype.equals("")){
				buffer.append("   and  a.invtype='"+invoicetype+"' ");
			}
			
			ps= connection.prepareStatement(buffer.toString());
			ResultSet rs=  ps.executeQuery();
			while(rs.next()){
				
				count= rs.getInt(1);
					
				}	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public ArrayList<Client> getallInvoicedListOfClient(String fromdate, String todate){
		ArrayList<Client> list=new ArrayList<Client>();
		StringBuffer buffer=new StringBuffer();
		buffer.append("select apm_patient.id,concat(title,' ',firstname,' ',surname) from apm_charges_invoice ");
		buffer.append(" inner join apm_patient on apm_patient.id=apm_charges_invoice.clientid  ");
		buffer.append("  where apm_charges_invoice.date between '"+fromdate+"' and '"+todate+"' group by clientid ");
		PreparedStatement ps= null;
		try {
			ps= connection.prepareStatement(buffer.toString());
			ResultSet rs= ps.executeQuery();
			while(rs.next()){
				Client client= new  Client();
				client.setId(rs.getInt(1));
				client.setClientName(rs.getString(2));
				list.add(client);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public String getUserNameById(int id){
		PreparedStatement ps= null;
		String username="";
		try {
			String sql="select concat(initial,' ',firstname,' ',lastname) from apm_charges_invoice inner join apm_user on apm_charges_invoice.practid=apm_user.id where apm_charges_invoice.id="+id+" ";
			ps= connection.prepareStatement(sql);
			ResultSet rs= ps.executeQuery();
			while(rs.next()){
				username=rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return username;
		
	}

	public ArrayList<Accounts> getCreditBalanceReportList(String fromDate, String toDate, String payby,
			String paymentStatus, String thirdParty, String orderby, String order, String invoicecategory,
			String string, String userid) {
		PreparedStatement preparedStatement = null;
		ArrayList<Accounts>list = new ArrayList<Accounts>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select apm_charges_invoice.id,apm_charges_invoice.debit,sum(apm_charges_payment.payment),discount,discamt,fullname,userid,apm_charges_invoice.date,(debit-sum(payment)-discamt-(debit/discount)) ");
		sql.append(" ,payby,itype from apm_charges_invoice ");
		sql.append("inner join apm_patient on apm_patient.id = apm_charges_invoice.clientid ");
		sql.append("inner join apm_charges_payment on apm_charges_payment.chargeinvoiceid=apm_charges_invoice.id ");
		sql.append(" where apm_charges_invoice.date between '"+fromDate+"' and '"+toDate+"'  ");
		//sql.append("and (debit-payment-(discamt+(discount/100)*debit))>0 ");
		if(userid!=null){
			sql.append("and userid='"+userid+"' ");
		}
		sql.append("group by apm_charges_invoice.id having (debit-sum(payment)-(discamt+(discount/100)*debit))>0 ");
		try{
			preparedStatement = connection.prepareStatement(sql.toString());
			ResultSet rs = preparedStatement.executeQuery();
			double balanceTotal =0;
			AccountsDAO accountsDAO= new JDBCAccountsDAO(connection);
			while(rs.next()){
				Accounts accounts=new Accounts();
				accounts.setClientName(rs.getString(6));
				accounts.setInvoiceid(rs.getInt(1));
				accounts.setInvdate(DateTimeUtils.getCommencingDate1(rs.getString(8)));
				accounts.setDebitAmount(rs.getLong(2));
				accounts.setCreditAmount(rs.getDouble(3));
				if(rs.getDouble(4)!=0){
					accounts.setDiscount(rs.getDouble(4));
				}else if(rs.getDouble(5)!=0){
					accounts.setDiscount(rs.getDouble(5));
				}else{
					accounts.setDiscount(0);
				}
				accounts.setBalance(rs.getDouble(9));
				if(rs.getDouble(9)==0){
					double discount = accounts.getDiscount();
					double bal =0;
					if(discount==0){
						bal = rs.getDouble(2) - rs.getDouble(3) ;
					}else{
						bal = rs.getDouble(2) - rs.getDouble(3) -discount;
					}
					accounts.setBalance(bal);
				}
				balanceTotal = balanceTotal + accounts.getBalance();
				accounts.setBalanceTotal(balanceTotal);
				accounts.setUserid(rs.getString(7));
				accounts.setPayby(rs.getString(10));
				accounts.setInvoicetype(accountsDAO.getInvoiceName(rs.getString(11)));
					list.add(accounts);
				}
	
	}catch (Exception e) {
	e.printStackTrace();
}
		return list;
	}

	public int dropAssesmentView() {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "drop view asmnt_view ";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			result = preparedStatement.executeUpdate();
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public int createAssesmentView(String fromDate, String toDate) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "create view  asmnt_view as select * from apm_invoice_assesments where commencing between '"+fromDate+"' and '"+toDate+"' ";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			result = preparedStatement.executeUpdate();
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	private String getPhysicalpaymentId(String payid){
		String res="0";
		try {
			PreparedStatement ps= connection.prepareStatement(" select id from his_payment_record_physical where payment_id='"+payid+"'   ");
			ResultSet rs= ps.executeQuery();
			while(rs.next()){
				res=""+rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	private String getPhysicalpaymentIdAdvRef(String invoiceid){
		String res="0";
		try {
			PreparedStatement ps= connection.prepareStatement(" select id from his_payment_record_physical where adv_ref_id='"+invoiceid+"'   ");
			ResultSet rs= ps.executeQuery();
			while(rs.next()){
				res=""+rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public double getRequestedDiscountAmt(String fromDate, String toDate, String userid, int i) {
		double amt =0;
		try {
			toDate =toDate+" "+"59:59:59";
			StringBuffer buffer = new StringBuffer();
			buffer.append("select disc_type, disc_amount, invoiceamount from discount_request ");
			buffer.append("inner join apm_charges_invoice on apm_charges_invoice.id = invoiceid ");
			buffer.append("where disc_request='"+i+"' and discount_request.deleted=0 ");
			buffer.append("and requested_date between '"+fromDate+"' and '"+toDate+"' ");
			if(userid!=null){
				if(userid.equals("")||userid.equals("0")){
					
				}else{
					buffer.append("and requested_userid='"+userid+"' ");
				}
			}
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			
			while (rs.next()) {
				double d =0;
				if(rs.getString(1).equals("0")){
					d = (rs.getDouble(3)*rs.getDouble(2))/100;
				}else{
					d = rs.getDouble(2);
				}
				amt = amt + d;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return amt;
	}

	public ArrayList<NotAvailableSlot> getDistlevelopdcount(String fromDate, String toDate) {
		PreparedStatement preparedStatement = null;
		ArrayList<NotAvailableSlot>list = new ArrayList<NotAvailableSlot>();
		String sql = "select name from admin.all_district ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				NotAvailableSlot n = new NotAvailableSlot();
	
				String dbname = rs.getString(1);
				
				int opdcount = getdistopdcnt(dbname,fromDate,toDate);
				int opdcompletedcount = getopdcompletedcnt(dbname,fromDate,toDate);
				int opdinvoiced = getopdinvoicedcnt(dbname,fromDate,toDate);
				
				n.setDistrict(dbname);
				n.setOpdnt(""+opdcount+"");
				n.setCompletedcnt(""+opdcompletedcount+"");
				n.setInvoicedcnt(""+opdinvoiced+"");
				
				list.add(n);
				
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

	private int getopdinvoicedcnt(String dbname, String fromDate, Object toDate) {
		PreparedStatement preparedStatement = null;
		String sql = "select count(*) from "+dbname+".apm_invoice_assesments where commencing between "
				+ " '"+fromDate+"' and '"+toDate+"' and invoiced !=0 ";
		
		int result = 0;
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				result = rs.getInt(1);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
	}

	private int getopdcompletedcnt(String dbname, String fromDate, Object toDate) {
		PreparedStatement preparedStatement = null;
		String sql = "select count(*) from "+dbname+".apm_invoice where commencing between "
				+ " '"+fromDate+"' and '"+toDate+"' and chargeType='Submit' ";
		
		int result = 0;
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				result = rs.getInt(1);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
	}

	private int getdistopdcnt(String dbname, String fromDate, Object toDate) {
		PreparedStatement preparedStatement = null;
		String sql = "select count(*) from "+dbname+".apm_available_slot where commencing between "
				+ " '"+fromDate+"' and '"+toDate+"' ";
		
		int result = 0;
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				result = rs.getInt(1);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return result;
	}

	public ArrayList<Accounts> getRefundPaymentReportListIpdOpd(String fromDate, String toDate, String payby,
			String paymentStatus, String thirdParty, String orderby, String order, String invoicecategory,
			String string, String userid, int isipdopd,String selectedInvctype, String paymode) {
	/*	isipdopd zero for opd and 1 for ipd*/
	toDate = toDate + " 23:59:59";
	fromDate=fromDate+" 00:00:00";
	PreparedStatement preparedStatement = null;
	ArrayList<Accounts>list = new ArrayList<Accounts>();
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT apm_credit_account.id,date,payby,payment_mode, ");
		sql.append("charge,debit,balance,third_party_name_id,concat(title,' ',firstname,' ',surname),advref,resetinv,payment_mode,userid,concat(his_invoice_payment_sno.cyear,'/',his_invoice_payment_sno.sno) ,client_id ");
		sql.append("FROM apm_credit_account inner join apm_patient on apm_patient.id = apm_credit_account.client_id ");
		sql.append(" left join his_invoice_payment_sno on his_invoice_payment_sno.invoiceid=apm_credit_account.id ");
		sql.append("where payment_mode IS NOT NULL and date between '"+fromDate+"' and '"+toDate+"' and ");
		sql.append("advref=1 and his_invoice_payment_sno.paymentid=0  ");
		if(userid!=null){
			if(userid.equals("0")||userid.equals("")){
				
			}else{
				sql.append(" and userid = '"+userid+"'"); 
			}
			
		}
		if(isipdopd>0){
			sql.append(" and   advance_ipdid!='0'  and debit!='0' ");
		}else{
			sql.append(" and   advance_ipdid='0'  and debit!='0' ");
		}
//		if(selectedInvctype!=null){
//			if(selectedInvctype.equals("0") || selectedInvctype.equals("")){
//				
//			}else{
//				sql.append(" and invoice_type = '"+selectedInvctype+"'"); 
//			}
//		}
		if(!selectedInvctype.equals("0")){
			if(selectedInvctype.contains("0,")){
				String val="";
				for (String str : selectedInvctype.split(",")) {
					if(str.equals("0")){
						continue;
					}
					if(val.equals("")){
						val=str;
					}else{
						val=val+","+str;
					}
					selectedInvctype=val;
				}
			}
		}
		if(!selectedInvctype.equals("0")){
//			String temp[] = selectedInvctype.split(",");
//			if(temp.length>1){
				//selectedInvctype = selectedInvctype.substring(selectedInvctype.length()-1);
				sql.append(" and invtypenew in("+selectedInvctype+") ");
//			}
		}
		
		paymode=DateTimeUtils.isNull(paymode);
		if(!(paymode.equals("")||paymode.equals("0"))){
			sql.append("  and payment_mode='"+paymode+"' ");	
		}
		
		sql.append(" order by id desc ");
		try{
			
			ClientDAO clientDAO= new JDBCClientDAO(connection);
			preparedStatement = connection.prepareStatement(sql.toString());
			ResultSet rs = preparedStatement.executeQuery();
			AdditionalDAO additionalDAO= new JDBCAdditionalDAO(connection);
			double advtotal = 0;
			while(rs.next()){
				Accounts accounts = new Accounts();
				accounts.setId(rs.getInt(1));
				accounts.setBghseqId(additionalDAO.getBalgopalSeqNum(""+accounts.getId()));
				String date = DateTimeUtils.getIndianDateTimeFormat(rs.getString(2));
				accounts.setDate(date);
				String temp[] = date.split(" ");		
				accounts.setCommencing(temp[0]);
				accounts.setPayby(rs.getString(3));
				accounts.setPaymentmode(rs.getString(4));
				accounts.setCharges(DateTimeUtils.changeFormat(Double.parseDouble(rs.getString(5))));
				advtotal = advtotal + rs.getDouble(5);
				accounts.setDebitTotalx(DateTimeUtils.changeFormat(Double.parseDouble(rs.getString(6))));
				accounts.setAdvreftotal(DateTimeUtils.changeFormat(advtotal));
				
				
				Client client= clientDAO.getClientDetails(rs.getString(15));
				accounts.setAbrivationid(client.getAbrivationid());
				
				double credit = rs.getDouble(5);
				double debit = rs.getDouble(6);
				double balance = 0;
				
				accounts.setDebitAmount(debit);
				accounts.setCreditAmount(credit);
				accounts.setBalance(balance);
				
				if(rs.getString(3).equals(Constants.PAY_BY_THIRD_PARTY)){
					
					CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(connection);
				    CompleteAppointment	completeAppointment = completeAptmDAO.getInsuranceCompanyName(rs.getString(8));
				    accounts.setClientName(completeAppointment.getInsuranceCompanyName());
				}
				else{
					
					accounts.setClientName(rs.getString(9));
				}
				
				
				accounts.setBalancex(DateTimeUtils.changeFormat(balance));
				accounts.setAdvref(rs.getInt(10));
				accounts.setResetinv(rs.getString(11));
				accounts.setPaymentmode(rs.getString(12));
				accounts.setUserid(rs.getString(13));
				accounts.setRefid(rs.getString(14));
				accounts.setPhysical_payment_id(getPhysicalpaymentIdAdvRef(rs.getString(1)));
				list.add(accounts);
			}
			
			//Akash 28 May 2018 for test
			/*ArrayList<Accounts>reuinvoiceList = getRefInvoiceList( fromDate,
					 toDate,  payby,  paymentStatus,
					 thirdParty,  orderby,  order,
					 invoicecategory,  string,  userid);
			
			list.addAll(reuinvoiceList);*/
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	public double getPaymentReportInvoiceAmt(String invoiceids) {
		double amt =0;
		try {
			String sql ="select sum(debit) from apm_charges_invoice where id in("+invoiceids+") ";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				amt = Math.round(rs.getDouble(1)*100.0)/100.0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return amt;
	}

	public ArrayList<Accounts> getInvoiceReportListForPaymentReport(String fromDate, String toDate, String payby,
			String howpaid, String orderby, String order, String invcategory, String userid,
			String selectedInvctype, String sortfilter) {
		PreparedStatement preparedStatement = null;
		ArrayList<Accounts>list = new ArrayList<Accounts>();
		StringBuffer sql = new StringBuffer();
		StatementDAO statementDAO = new JDBCStatementDAO(connection);
		sql.append("select apm_charges_invoice.id,payby,date,clientid,debit,deliverstatus,discount,tpid,apm_patient.firstname,apm_charges_invoice.disctype,discamt,apm_charges_invoice.itype,userid from apm_charges_invoice inner join ");
		sql.append("apm_patient on apm_patient.id = apm_charges_invoice.clientid  inner join apm_user on apm_user.id=apm_charges_invoice.updatedby   ");
		sql.append("where date between '"+fromDate+"' AND '"+toDate+"' and (debit-(discount/100)*debit+discamt)=0 ");
		if(payby!=null){
			if(!payby.equals("0")){
				if(payby.equals(Constants.PAY_BY_CLIENT)){
					sql.append("and payby='Client' ");
				}else{
					sql.append("and payby='Third Party' ");
				}
			}
		}
		if(!invcategory.equals("2")){
			sql.append("and apm_charges_invoice.invpstype="+invcategory+" ");
		}
		if(!selectedInvctype.equals("0")){
			String temp[] = selectedInvctype.split(",");
			if(temp.length>1){
				sql.append(" and itype in("+selectedInvctype+") ");
			}
		}
		sql.append("order by apm_charges_invoice.id desc ");
		try{
			AccountsDAO accountsDAO=new JDBCAccountsDAO(connection);
			preparedStatement = connection.prepareStatement(sql.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				double balance = 0;
				double result = 0.0;
				double creditAmount = 0;
				
				Accounts accounts = new Accounts();
				accounts.setId(rs.getInt(1));
				accounts.setPayby(rs.getString(2));
				accounts.setDate(DateTimeUtils.getCommencingDate1(rs.getString(3)));
				accounts.setClientid(rs.getInt(4));
				
				accounts.setDebitAmount(rs.getDouble(5));
				accounts.setDebitAmountx(DateTimeUtils.changeFormat(rs.getDouble(5)));
				
				accounts.setDeliverstatus(rs.getString(6));
				
				double debit = rs.getDouble(5);
				double total = rs.getDouble(5);
				int disctype = rs.getInt(10);
				double discamt = rs.getDouble(11);
				double discount = rs.getDouble(7);
				double r1 = (total*discount)/100;
				if(disctype==1){
					r1 = discamt;
				}
				accounts.setUserid(rs.getString(13));
				accounts.setDiscAmmount(DateTimeUtils.changeFormat(r1));
				
				total = total-r1;
				result = result + total;
				
				creditAmount = getCreditAmount(rs.getDouble(1));
				
				//if refund against invoice
				double refundAmt = statementDAO.getRefundAmtAgainsInvoice(rs.getInt(1));
				accounts.setRefundAmountx(DateTimeUtils.changeFormat(refundAmt));
				creditAmount = creditAmount - refundAmt;
				
				accounts.setCreditAmount(creditAmount);
				accounts.setCreditCharge(Double.toString(creditAmount));
				accounts.setCreditTotalx(DateTimeUtils.changeFormat(creditAmount));
				
				
				balance = result - creditAmount;
				accounts.setBalance(balance);
				accounts.setBalancex(DateTimeUtils.changeFormat(balance));
				
				int noOfCharges = getNoOfCharges(rs.getInt(1));
				accounts.setNumberOfChages(noOfCharges);
				accounts.setDiscount(rs.getDouble(7));
				String clientname = getClientFullName(rs.getString(4));
				String clientabr= getClientUHID(rs.getString(4));
				accounts.setAbrivationid(clientabr);
				accounts.setClientName(clientname);
				String payee = rs.getString(2);
				String payeename = null;
				if(payee.equalsIgnoreCase("Third Party")){
					payeename = getTpname(rs.getInt(8));
					
				}
				else{
					payeename = clientname;
				}
				accounts.setPayeeName(payeename);
				//showing seq no instead of invoice id 21/09/2018
				
			    String ipdopdseq=accountsDAO.getIpdOpdSeqNoWithType(rs.getInt(1));
			    accounts.setIpdopdseq(ipdopdseq);
			    list.add(accounts);
				
				
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return list;
	}
	
	
	public ArrayList<Accounts> creditInvoiceReportList(String fromDate, String toDate, String payby,
			String paymentStatus, String thirdParty, String orderby, String order, String invoicecategory,
			String string, String userid) {
		PreparedStatement preparedStatement = null;
		ArrayList<Accounts>list = new ArrayList<Accounts>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select apm_charges_invoice.id,apm_charges_invoice.debit,sum(apm_charges_payment.payment),discount,discamt,fullname,userid,apm_charges_invoice.date,(debit-sum(payment)-discamt-(debit/discount)) ");
		sql.append(" ,payby,itype,(discamt+(discount/100)*debit) from apm_charges_invoice ");
		sql.append("inner join apm_patient on apm_patient.id = apm_charges_invoice.clientid ");
		sql.append("left join apm_charges_payment on apm_charges_payment.chargeinvoiceid=apm_charges_invoice.id ");
		sql.append(" where apm_charges_invoice.date between '"+fromDate+"' and '"+toDate+"'  ");
		//sql.append("and (debit-payment-(discamt+(discount/100)*debit))>0 ");
		/*if(userid!=null){
			sql.append("and userid='"+userid+"' ");
		}*/
		sql.append("group by apm_charges_invoice.id  ");
		try{
			preparedStatement = connection.prepareStatement(sql.toString());
			ResultSet rs = preparedStatement.executeQuery();
			double balanceTotal =0;
			AccountsDAO accountsDAO= new JDBCAccountsDAO(connection);
			while(rs.next()){
				Accounts accounts=new Accounts();
				accounts.setClientName(rs.getString(6));
				accounts.setInvoiceid(rs.getInt(1));
				accounts.setInvdate(DateTimeUtils.getCommencingDate1(rs.getString(8)));
				accounts.setDebitAmount(rs.getLong(2));
				accounts.setCreditAmount(rs.getDouble(3));
				
				
				int payment=rs.getInt(3);
				int debit=rs.getInt(2);
				int discountAmt=rs.getInt(12);
				if(((debit-payment-discountAmt))<=1){
					continue;
				}
				
				if(rs.getDouble(4)!=0){
					accounts.setDiscount(rs.getDouble(4));
				}else if(rs.getDouble(5)!=0){
					accounts.setDiscount(rs.getDouble(5));
				}else{
					accounts.setDiscount(0);
				}
				accounts.setBalance(rs.getDouble(9));
				if(rs.getDouble(9)==0){
					double discount = accounts.getDiscount();
					double bal =0;
					if(discount==0){
						bal = rs.getDouble(2) - rs.getDouble(3) ;
					}else{
						bal = rs.getDouble(2) - rs.getDouble(3) -discount;
					}
					accounts.setBalance(bal);
				}
				balanceTotal = balanceTotal + accounts.getBalance();
				accounts.setBalanceTotal(balanceTotal);
				accounts.setUserid(rs.getString(7));
				accounts.setPayby(rs.getString(10));
				accounts.setInvoicetype(accountsDAO.getInvoiceName(rs.getString(11)));
					list.add(accounts);
				}
	
	}catch (Exception e) {
	e.printStackTrace();
}
		return list;
	}
	
	
	
}

