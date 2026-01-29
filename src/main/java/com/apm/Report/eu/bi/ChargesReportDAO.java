package com.apm.Report.eu.bi;

import java.util.ArrayList;

import com.apm.Accounts.eu.entity.Accounts;
import com.apm.DiaryManagement.eu.entity.Client;
import com.apm.DiaryManagement.eu.entity.NotAvailableSlot;
import com.apm.Master.eu.entity.Master;
import com.apm.Report.eu.entity.ChargesReport;

public interface ChargesReportDAO {

	ArrayList<Accounts> getChargesReportList(String fromDate, String toDate,String payby,String tpid,String invoiceStatus,String oredrby,String order,String condition);

	ArrayList<Accounts> getInvoiceReportList(String fromDate, String toDate,String payby,String paymentStatus,String thirdparty,String orderby,String order,String invcategory,String invoicetype);

	ArrayList<Accounts> getPaymentReportList(String fromDate, String toDate,String payby,String howpaid,String orderby,String order,String invcategory,String userid,String selectedInvctype, String sortfilter);

	int moveInvoiceToScecondary(String hdnprimaryinvoice);

	ArrayList<Accounts> getAuditorsInvoiceReportList(String fromDate,
			String toDate, String payby, String paymentStatus,
			String thirdParty, String orderby, String order,
			String invoicecategory,String invcetype);

	ArrayList<Accounts> getAdvanceInvoiceReportList(String fromDate,
			String toDate, String payby, String paymentStatus,
			String thirdParty, String orderby, String order,
			String invoicecategory, String invcetype,String userid);

	ArrayList<Accounts> getDoctorShareReport(String fromdate, String todate,String practitionerId,String jobtitle);

	NotAvailableSlot getOtAppointmentDetails(int parseInt);

	
	ArrayList<Accounts> getPaymentModeWiseList(String fromDate, String toDate,
			int i, String payby, String howpaid, String invoicecategory,
			String userid);

	ArrayList<Accounts> getAdvPaymentModeWiseList(String fromDate,
			String toDate, int i, String payby, String howpaid,
			String invoicecategory, String userid);

	ArrayList<Accounts> getSmallPaymentReportList(String fromDate,
			String toDate, String payby, String howpaid, String orderby,
			String order, String invoicecategory,String userid, String selectedInvctype);

	double getRefForInvoicet(String fromDate, String toDate, int i,
			String payby, String howpaid, String invoicecategory, String userid);

	double getRefOnly(String fromDate, String toDate, int i, String payby,
			String howpaid, String invoicecategory, String userid);

	ArrayList<Accounts> getAdvancePaymentReportList(String fromDate,
			String toDate, String payby, String paymentStatus,
			String thirdParty, String orderby, String order,
			String invoicecategory, String string, String userid, String paymode,String shiftedFromCancel);

	ArrayList<Accounts> getRefundPaymentReportList(String fromDate,
			String toDate, String payby, String paymentStatus,
			String thirdParty, String orderby, String order,
			String invoicecategory, String string, String userid, String selectedInvctype, String paymode);

	ArrayList<Master> getAccountUserList();

	int saveMisReportLog(String string, String userId, String fromDate, String toDate,String date,String string1);

	ArrayList<Accounts> getChargesReportDeatiled(String fromDate, String toDate, String payby, String tpid,
			   String invoiceStatus, String orderby, String order, String jobtitle, String apmttype,String ChargesType,String user,String opdipd ,String clientid,String searchinv, String dept);


	public ArrayList<Accounts> getpaymentreciptlist(String fromdate, String todate, String paymenttype, String userid,String invoicetype);
	
	int  getcountofinvoice(String fromdate, String todate, String paymenttype, String userid,String invoicetype);

	
	ArrayList<Client> getallInvoicedListOfClient(String fromdate, String todate);

	ArrayList<Accounts> getCreditBalanceReportList(String fromDate, String toDate, String payby, String paymentStatus,
			String thirdParty, String orderby, String order, String invoicecategory, String string, String userid);

	int dropAssesmentView();

	int createAssesmentView(String fromDate, String toDate);

	double getRequestedDiscountAmt(String fromDate, String toDate, String userid, int i);

	ArrayList<NotAvailableSlot> getDistlevelopdcount(String fromDate, String toDate);


	ArrayList<Accounts> getRefundPaymentReportListIpdOpd(String fromDate,
			String toDate, String payby, String paymentStatus,
			String thirdParty, String orderby, String order,
			String invoicecategory, String string, String userid,int isipdopd, String selectedInvctype,String paymode);

	String getPhysicalpaymentIdbyinvoiceid(int payid);

	double getPaymentReportInvoiceAmt(String invoiceids);

	ArrayList<Accounts> getInvoiceReportListForPaymentReport(String fromDate, String toDate, String payby,
			String howpaid, String orderby, String order, String invoicecategory, String userid,
			String selectedInvctype, String sortfilter);

	String getTpname(int int1);

	String getClientFullName(String string);

	int getNoOfCharges(int int1);

	double getCreditAmount(double double1);
	public ArrayList<Accounts> creditInvoiceReportList(String fromDate, String toDate, String payby,
			String paymentStatus, String thirdParty, String orderby, String order, String invoicecategory,
			String string, String userid); 
}
