package com.apm.Accounts.eu.bi;

import java.util.ArrayList;

import com.apm.Accounts.eu.entity.Accounts;
import com.apm.Appointment.eu.entity.AppointmentType;
import com.apm.DiaryManagement.eu.entity.CompleteAppointment;
import com.apm.common.web.common.helper.LoginInfo;

public interface AdditionalDAO {

	ArrayList<AppointmentType> getAdditionalChargesList();

	int saveInvoce(String clientId, String clientname, String type, String date, String locatio,String apmtid, String userid);

	int saveAssessment(String clientId, String clientname, String type,
			String date, int invoiceid, CompleteAppointment appointment);

	int saveCharge(String clientId, String clientname, String type, String date, String creditDebitCharge, String payBuy,int loginid);

	ArrayList<CompleteAppointment> getPatientChrageDetails(String id,int loginid);

	ArrayList<CompleteAppointment> getCompleteApmtList(String clientId,int loginid);

	int saveManualCharge(String clientId, String clientname,
			String manualTypeName, String manualCharge, String date, String creditDebitCharge, String payBuy,int loginid);

	String getAdditionalCharge(String type);

	int saveCreditInvoice(String clientId, String clientname, String type,
			String date, String location);

	int saveCreditAssessment(String clientId, String clientname, String type,
			String date, int invoiceid, CompleteAppointment appointment);

	int saveTempCreditAccounts(int invoiceid,String loginsessionid);

	int updateCredit(String payby, double debitTotal,
			String submitInvoiceNotes, String howpaid, String creditId,double balance);

	ArrayList<Accounts> getCreditAccountList(String clientId,String ipdid);

	double getCreditTotal(String clientId);

	int saveCreditRecord(String clientId, String type, String date,
			String creditnote, String payBuy, String charge,String paymode,double balance,int rinv,String userid, int invoiceid);

	int saveCreditAssessmentRecord(String clientId, String apmttype, String type,
			String date, int invoiceid,String charge,int advref);

	int saveDebitRecord(String clientId, String type, String date,
			String creditnote, String payBuy, String charge, String paymode,
			double balance,int advref,int rinv,String userid, String manualinvoiceid, String refundnote,String invoiceid, String invtype, LoginInfo loginInfo);

	double getAdvanceBalance(String clientid);

	int updateRefundInvoice(String invoiceid,int crinvoiceid);

	int checkInvoiceIdofClient(String invoiceid, String clientid);

	String getDebitFromInvoiceId(String invoiceid);

	int saveRequestRefund(String clientId, String clientname, String type, String date, String location,
			String creditDebitCharge, String manualinvoiceid, String refundnote, String userId, String requestedate);

	int saveRequestRefundChild(int parentid, String clientId, int id);

	ArrayList<CompleteAppointment> getRefundRequestList(String fromdate, String todate, String ipdopdwise, String searchtext, boolean accessofapprove, int id, String filter_status, String userid, String countdata);

	int cancelRefundrequest(String parentid, String userid, String date, String delete_reason);

	int approveRefundrequest(String id, String userid, String date,String approve_reason);

	int saveRefundTemp(String id, int id2);

	CompleteAppointment getRefundRequestData(String id);

	int updateRefundRequest(String refundrequestid, String userId, String datetime);

	ArrayList<CompleteAppointment> getClientRefundRequestList(String clientid);

	int saveRefundTempFromDeleteInvoice(String id, int id2);

	ArrayList<String> getRefundRequestId(String refundrequestid);

	int updateRefundDeleteInvoice(String string, String datetime, String userId);

	int updateDebitRecordRefundId(int crinvoiceid, String refundrequestid);

	int updateRefundRequestDeleteInvoice(String string);

	int saveRequestRefundFromDiscount(String invoiceId, Accounts accounts2, String datetime2, String date,
			String refund_reason, String refundamount, String userid, String discaproveuserid);

	int saveRequestRefundFromDiscuntChild(String invoiceId, Accounts accounts2, String datetime2, String date,
			String userid, int result, int id, String refundamount);

	double getDiscRefundAmount(String clientId);

	int checkrefundStatus(String id, String clientid);

	int checkNewRefund(boolean accessofapprove, int id, String currentdate, String userid);

	int checkNewDiscount(boolean accessofapprove, int id, String currentdate, String userid);

	int getMaxAdvno(int crinvoiceid);

	int updateAdvMaxno(int crinvoiceid, int maxno,String invtype,int paymentid);
	
	int setPractionerinCreditacc(int crinvoiceid,String practid);
	int updateAdvRefPaymodeCreditAcc(String recieptid,String paymode);
	int updateAdvrefLedger(String recieptid,String paymode);
ArrayList<CompleteAppointment> 	getRefundReportList(String fromdate, String todate, String ipdopdwise);

boolean checkRefundRequest(String id);

String getDeletedChargeIDforRefund(String id);

int saveRefundTempFromDeleteInvoiceNew(String id, int id2, String deletechargeid, double newbalane);

String getRequestedRefundDeleteIDs(String clientId, int id);

int updateRefundParentIds(String ids, int parentid);

boolean checkPendingRefund(String invoiceid);

int getRefundApproveAppliedCount(String fromdate, String todate, String string, String searchtext,
		boolean accessofapprove, int id, String filter_status, String userId, String countdata);

int createSeqnogenProccessForAdvAndRef(String itype,String creditaccId,String paymode, String refinvoiceid);
int getBalgopalSeqNum(String creditaccId);

ArrayList<CompleteAppointment> getAutoRefundList(String fromdate, String todate, String ipdopdwise);

ArrayList<Accounts> getAutoRefundListNew(String fromdate, String todate, String ipdopdwise);
AppointmentType getAppointmentTypeDetails(String type);

double getAdvanceBalanceRemain(String clientid);

String getpaymentUserId(int invoiceid);
}
