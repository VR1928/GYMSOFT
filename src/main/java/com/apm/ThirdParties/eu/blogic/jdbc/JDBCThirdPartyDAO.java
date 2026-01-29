package com.apm.ThirdParties.eu.blogic.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.apm.DiaryManagement.eu.bi.AppointmentDiaryReportDAO;
import com.apm.DiaryManagement.eu.entity.AppointmentDiaryReport;
import com.apm.DiaryManagement.eu.entity.Client;
import com.apm.ThirdParties.eu.bi.ThirdPartyDAO;
import com.apm.ThirdParties.eu.entity.GP;
import com.apm.ThirdParties.eu.entity.ThirdParty;
import com.apm.ThirdParties.web.form.DynamicAppointment;
import com.apm.Tools.web.action.TPReferalAction;
import com.apm.common.eu.blogic.jdbc.JDBCBaseDAO;
import com.apm.common.utils.DateTimeUtils;
import com.apm.common.utils.Pagination;
import com.apm.main.common.constants.Constants;

public class JDBCThirdPartyDAO extends JDBCBaseDAO implements ThirdPartyDAO {
	
	public JDBCThirdPartyDAO(Connection connection){
			
			this.connection = connection;
		}

	public ArrayList<ThirdParty> getThirdPartyList() {
		PreparedStatement preparedStatement = null;
		ArrayList<ThirdParty>list = new ArrayList<ThirdParty>();
		String sql = "select id,name,description from apm_third_party";
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				ThirdParty thirdParty = new ThirdParty();
				thirdParty.setId(rs.getInt(1));
				thirdParty.setName(rs.getString(2));
				thirdParty.setType(rs.getString(2));
				thirdParty.setDescription(rs.getString(3));
				list.add(thirdParty);
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	public int saveDetails(String type,ThirdParty thirdParty) {
		int result = 0;
		
		PreparedStatement pstm = null;
		
		
		String sql = "insert into apm_third_party_details(third_party_id,type,name,salutation,title,department,search_name,telephone_line,email,email_cc,notes,company_details,company_name,address,town,country,postcode,telephone,fax,web,reference_no,company_email,warning_msg,account_notes,acc_must_be_in_credit,acc_warning_limit,default_apmt_booking_confim,confm_contact_email,default_apmt_dna,dna_contact_email,dnaLimit,outInvoiceLimit,clinicId,creditDuration,creditReminderDuration) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			pstm = connection.prepareStatement(sql);
			pstm.setString(1, thirdParty.getType());;
			String typeName = getTypeName(thirdParty.getType());
			pstm.setString(2, typeName);
			pstm.setString(3, thirdParty.getName());
			pstm.setString(4, thirdParty.getSalutation());
			pstm.setString(5, thirdParty.getTitle());
			pstm.setString(6, thirdParty.getDepartment());
			pstm.setString(7, thirdParty.getSearchName());
			pstm.setString(8, thirdParty.getTelephoneLine());
			pstm.setString(9, thirdParty.getEmail());
			pstm.setString(10, thirdParty.getEmailCc());
			pstm.setString(11, thirdParty.getNotes());
			pstm.setString(12, thirdParty.getCompanyDetails());
			
			pstm.setString(13, thirdParty.getCompanyName());
			pstm.setString(14, thirdParty.getAddress());
			pstm.setString(15, thirdParty.getTown());
			pstm.setString(16, thirdParty.getCountry());
			pstm.setString(17, thirdParty.getPostcode());
			pstm.setString(18, thirdParty.getCompnyTelephone());
			pstm.setString(19, thirdParty.getFax());
			pstm.setString(20, thirdParty.getWeb());
			pstm.setString(21, thirdParty.getReferenceNo());
			pstm.setString(22, thirdParty.getCompnyEmail());
			
			pstm.setString(23, thirdParty.getWarningMsg());
			pstm.setString(24, thirdParty.getAccountsNotes());
			pstm.setBoolean(25, thirdParty.isAccountMustBeInCredit());
			pstm.setString(26, thirdParty.getAccountWarningLimit());
			pstm.setString(27, thirdParty.getDefaultApmtBookingConfmTemp());
			pstm.setString(28, thirdParty.getConfContactEmail());
			pstm.setString(29, thirdParty.getDefaultApmtDnaTemp());
			pstm.setString(30, thirdParty.getDnaContactEmail());
			pstm.setString(31, thirdParty.getDnaLimit());
			pstm.setString(32, thirdParty.getOutInvoiceLimit());
			pstm.setInt(33, thirdParty.getClinicId());
			pstm.setString(34, thirdParty.getCreditDuration());
			pstm.setString(35, thirdParty.getCreditReminderDuration());
			result = pstm.executeUpdate();
			
		}catch (Exception e) {
			
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return result;
	}

	private String getTypeName(String type) {
		String typeName = null;
		PreparedStatement preparedStatement = null;
		
		String sql = "select name from apm_third_party where id = "+type+"";
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				
				
				typeName = rs.getString(1);
				}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return typeName;
	}

	public ArrayList<ThirdParty> getThirdPartyDetailsList(String type,int clinicId,Pagination pagination,String searchText) {
		PreparedStatement preparedStatement = null;
		ArrayList<ThirdParty>list = new ArrayList<ThirdParty>();
		String sql = "";
		if(pagination.sortColumn==null){
			sql = "select id,third_party_id,type,name,salutation,title,department,search_name,telephone_line,email,email_cc,notes,company_details,company_name,address,town,country,postcode,telephone,fax,web,reference_no,company_email,warning_msg,account_notes,acc_must_be_in_credit,acc_credit_limit,acc_warning_limit,default_apmt_booking_confim,confm_contact_email,default_apmt_dna,dna_contact_email,dnaLimit,outInvoiceLimit from apm_third_party_details where third_party_id like ('%"+type+"%') and company_name like ('%"+searchText+"%') order by id desc";
		}else{
			sql = "select id,third_party_id,type,name,salutation,title,department,search_name,telephone_line,email,email_cc,notes,company_details,company_name,address,town,country,postcode,telephone,fax,web,reference_no,company_email,warning_msg,account_notes,acc_must_be_in_credit,acc_credit_limit,acc_warning_limit,default_apmt_booking_confim,confm_contact_email,default_apmt_dna,dna_contact_email,dnaLimit,outInvoiceLimit from apm_third_party_details where third_party_id like ('%"+type+"%') and company_name like ('%"+searchText+"%')";
		}
			sql = pagination.getSQLQuery(sql);
			
			
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				ThirdParty thirdParty = new ThirdParty();
				thirdParty.setId(rs.getInt(1));
				thirdParty.setThirdPartyId(rs.getInt(2));
				thirdParty.setType(rs.getString(3));
				thirdParty.setName(rs.getString(4));
				
				
				thirdParty.setSalutation(rs.getString(5));
				thirdParty.setTitle(rs.getString(6));
				thirdParty.setDepartment(rs.getString(7));
				thirdParty.setSearchName(rs.getString(8));
				thirdParty.setTelephoneLine(rs.getString(9));
				thirdParty.setEmail(rs.getString(10));
				thirdParty.setEmailCc(rs.getString(11));
				thirdParty.setNotes(rs.getString(12));
				thirdParty.setCompanyDetails(rs.getString(13));
				
				thirdParty.setCompanyName(rs.getString(14));
				thirdParty.setAddress(rs.getString(15));
				thirdParty.setTown(rs.getString(16));
				thirdParty.setCountry(rs.getString(17));
				thirdParty.setPostcode(rs.getString(18));
				thirdParty.setCompnyTelephone(rs.getString(19));
				thirdParty.setFax(rs.getString(20));
				thirdParty.setWeb(rs.getString(21));
				thirdParty.setReferenceNo(rs.getString(22));
				thirdParty.setCompnyEmail(rs.getString(23));
				
				thirdParty.setWarningMsg(rs.getString(24));
				thirdParty.setAccountsNotes(rs.getString(25));
				thirdParty.setAccountMustBeInCredit(rs.getBoolean(26));
				thirdParty.setAccountCreditLimit(rs.getString(27));
				thirdParty.setAccountWarningLimit(rs.getString(28));
				thirdParty.setDefaultApmtBookingConfmTemp(rs.getString(29));
				thirdParty.setConfContactEmail(rs.getString(30));
				thirdParty.setDefaultApmtDnaTemp(rs.getString(31));
				thirdParty.setDnaContactEmail(rs.getString(32));
				thirdParty.setDnaLimit(rs.getString(33));
				thirdParty.setOutInvoiceLimit(rs.getString(34));
				
				
				list.add(thirdParty);
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	public int editDetails(String type, int id, ThirdParty thirdParty) {
int result = 0;
		
		PreparedStatement pstm = null;
		
		
		String sql = "update apm_third_party_details set name=?,salutation=?,title=?,department=?,search_name=?,telephone_line=?,email=?,email_cc=?,notes=?,company_details=?,company_name=?,address=?,town=?,country=?,postcode=?,telephone=?,fax=?,web=?,reference_no=?,company_email=?,warning_msg=?,account_notes=?,acc_must_be_in_credit=?,acc_warning_limit=?,default_apmt_booking_confim=?,confm_contact_email=?,default_apmt_dna=?,dna_contact_email=?,dnaLimit=?,outInvoiceLimit=?,creditDuration=?,creditReminderDuration=?,gptown=?,gpaddress=? where id = "+id+""; 
		try {
			pstm = connection.prepareStatement(sql);
			
			pstm.setString(1, thirdParty.getName());
			pstm.setString(2, thirdParty.getSalutation());
			pstm.setString(3, thirdParty.getTitle());
			pstm.setString(4, thirdParty.getDepartment());
			pstm.setString(5, thirdParty.getSearchName());
			pstm.setString(6, thirdParty.getTelephoneLine());
			pstm.setString(7, thirdParty.getEmail());
			pstm.setString(8, thirdParty.getEmailCc());
			pstm.setString(9, thirdParty.getNotes());
			pstm.setString(10, thirdParty.getCompanyDetails());
			
			pstm.setString(11, thirdParty.getCompanyName());
			pstm.setString(12, thirdParty.getAddress());
			pstm.setString(13, thirdParty.getTown());
			pstm.setString(14, thirdParty.getCountry());
			pstm.setString(15, thirdParty.getPostcode());
			pstm.setString(16, thirdParty.getCompnyTelephone());
			pstm.setString(17, thirdParty.getFax());
			pstm.setString(18, thirdParty.getWeb());
			pstm.setString(19, thirdParty.getReferenceNo());
			pstm.setString(20, thirdParty.getCompnyEmail());
			
			pstm.setString(21, thirdParty.getWarningMsg());
			pstm.setString(22, thirdParty.getAccountsNotes());
			pstm.setBoolean(23, thirdParty.isAccountMustBeInCredit());
			
			pstm.setString(24, thirdParty.getAccountWarningLimit());
			pstm.setString(25, thirdParty.getDefaultApmtBookingConfmTemp());
			pstm.setString(26, thirdParty.getConfContactEmail());
			pstm.setString(27, thirdParty.getDefaultApmtDnaTemp());
			pstm.setString(28, thirdParty.getDnaContactEmail());
			pstm.setString(29, thirdParty.getDnaLimit());
			pstm.setString(30, thirdParty.getOutInvoiceLimit());
			pstm.setString(31, thirdParty.getCreditDuration());
			pstm.setString(32, thirdParty.getCreditReminderDuration());
			pstm.setString(33, thirdParty.getGptown());
			pstm.setString(34, thirdParty.getGpaddress());
			
			
			result = pstm.executeUpdate();
			
		}catch (Exception e) {
			
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return result;
	}

	public int deleteDetail(int id) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "delete from apm_third_party_details where id = "+id+" ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			
			result = preparedStatement.executeUpdate();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int saveType(ThirdParty thirdParty) {
		int result = 0;
		
		PreparedStatement pstm = null;
		
		
		String sql = "insert into apm_third_party(name,description) values (?,?)";
		try {
			pstm = connection.prepareStatement(sql);
			pstm.setString(1, thirdParty.getType());
			pstm.setString(2, thirdParty.getDescription());
			
			
			result = pstm.executeUpdate();
			
		}catch (Exception e) {
			
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return result;
	}

	public ThirdParty getTypeDetail(int id) {
		PreparedStatement preparedStatement = null;
		ThirdParty thirdParty = new ThirdParty();
		String sql = "select id,name,description from apm_third_party where id = "+id+"";
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				
				thirdParty.setId(rs.getInt(1));
				thirdParty.setType(rs.getString(2));
				thirdParty.setDescription(rs.getString(3));
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return thirdParty;
	}

	public int updateType(int id, ThirdParty thirdParty) {
		int result = 0;
		
		PreparedStatement pstm = null;
		
		
		String sql = "update apm_third_party set name= ?,description=? where id = "+id+"";
		try {
			pstm = connection.prepareStatement(sql);
			pstm.setString(1, thirdParty.getType());
			pstm.setString(2, thirdParty.getDescription());
			
			
			result = pstm.executeUpdate();
			
		}catch (Exception e) {
			
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return result;
	}

	public int deleteType(int id) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "delete from apm_third_party where id = "+id+" ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			
			result = preparedStatement.executeUpdate();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<ThirdParty> getThirdPartyDetailsList(int clinicId,Pagination pagination) {
		PreparedStatement preparedStatement = null;
		ArrayList<ThirdParty>list = new ArrayList<ThirdParty>();
		String sql = "";
		if(pagination.sortColumn==null){
			 sql = "select id,third_party_id,type,name,salutation,title,department,search_name,telephone_line,email,email_cc,notes,company_details,company_name,address,town,country,postcode,telephone,fax,web,reference_no,company_email,warning_msg,account_notes,acc_must_be_in_credit,acc_credit_limit,acc_warning_limit,default_apmt_booking_confim,confm_contact_email,default_apmt_dna,dna_contact_email,dnaLimit,outInvoiceLimit from apm_third_party_details order by id desc";
		}else{
			 sql = "select id,third_party_id,type,name,salutation,title,department,search_name,telephone_line,email,email_cc,notes,company_details,company_name,address,town,country,postcode,telephone,fax,web,reference_no,company_email,warning_msg,account_notes,acc_must_be_in_credit,acc_credit_limit,acc_warning_limit,default_apmt_booking_confim,confm_contact_email,default_apmt_dna,dna_contact_email,dnaLimit,outInvoiceLimit from apm_third_party_details";
		}
			sql = pagination.getSQLQuery(sql);
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				ThirdParty thirdParty = new ThirdParty();
				thirdParty.setId(rs.getInt(1));
				thirdParty.setThirdPartyId(rs.getInt(2));
				thirdParty.setType(rs.getString(3));
				thirdParty.setName(rs.getString(4));
				
				
				thirdParty.setSalutation(rs.getString(5));
				thirdParty.setTitle(rs.getString(6));
				thirdParty.setDepartment(rs.getString(7));
				thirdParty.setSearchName(rs.getString(8));
				thirdParty.setTelephoneLine(rs.getString(9));
				thirdParty.setEmail(rs.getString(10));
				thirdParty.setEmailCc(rs.getString(11));
				thirdParty.setNotes(rs.getString(12));
				thirdParty.setCompanyDetails(rs.getString(13));
				
				thirdParty.setCompanyName(rs.getString(14));
				thirdParty.setAddress(rs.getString(15));
				thirdParty.setTown(rs.getString(16));
				thirdParty.setCountry(rs.getString(17));
				thirdParty.setPostcode(rs.getString(18));
				thirdParty.setCompnyTelephone(rs.getString(19));
				thirdParty.setFax(rs.getString(20));
				thirdParty.setWeb(rs.getString(21));
				thirdParty.setReferenceNo(rs.getString(22));
				thirdParty.setCompnyEmail(rs.getString(23));
				
				thirdParty.setWarningMsg(rs.getString(24));
				thirdParty.setAccountsNotes(rs.getString(25));
				thirdParty.setAccountMustBeInCredit(rs.getBoolean(26));
				thirdParty.setAccountCreditLimit(rs.getString(27));
				thirdParty.setAccountWarningLimit(rs.getString(28));
				thirdParty.setDefaultApmtBookingConfmTemp(rs.getString(29));
				thirdParty.setConfContactEmail(rs.getString(30));
				thirdParty.setDefaultApmtDnaTemp(rs.getString(31));
				thirdParty.setDnaContactEmail(rs.getString(32));
				thirdParty.setDnaLimit(rs.getString(33));
				thirdParty.setOutInvoiceLimit(rs.getString(34));
				
				
				list.add(thirdParty);
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	public int getThirdPartyCount(String type) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		
		String sql = "select count(*) from apm_third_party_details where third_party_id = "+type+"";
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

	public ArrayList<ThirdParty> getThirdPartyDetailsList(String type,
			Pagination pagination) {
		PreparedStatement preparedStatement = null;
		ArrayList<ThirdParty>list = new ArrayList<ThirdParty>();
		String sql = "select id,third_party_id,type,name,salutation,title,department,search_name,telephone_line,email,email_cc,notes,company_details,company_name,address,town,country,postcode,telephone,fax,web,reference_no,company_email,warning_msg,account_notes,acc_must_be_in_credit,acc_credit_limit,acc_warning_limit,default_apmt_booking_confim,confm_contact_email,default_apmt_dna,dna_contact_email,dnaLimit,outInvoiceLimit from apm_third_party_details where third_party_id = "+type+" order by company_name";
	//	sql = pagination.getSQLQuery(sql);
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				ThirdParty thirdParty = new ThirdParty();
				thirdParty.setId(rs.getInt(1));
				thirdParty.setThirdPartyId(rs.getInt(2));
				thirdParty.setType(rs.getString(3));
				thirdParty.setName(rs.getString(4));
				
				
				thirdParty.setSalutation(rs.getString(5));
				thirdParty.setTitle(rs.getString(6));
				thirdParty.setDepartment(rs.getString(7));
				thirdParty.setSearchName(rs.getString(8));
				thirdParty.setTelephoneLine(rs.getString(9));
				thirdParty.setEmail(rs.getString(10));
				thirdParty.setEmailCc(rs.getString(11));
				thirdParty.setNotes(rs.getString(12));
				thirdParty.setCompanyDetails(rs.getString(13));
				
				thirdParty.setCompanyName(rs.getString(14));
				thirdParty.setAddress(rs.getString(15));
				thirdParty.setTown(rs.getString(16));
				thirdParty.setCountry(rs.getString(17));
				thirdParty.setPostcode(rs.getString(18));
				thirdParty.setCompnyTelephone(rs.getString(19));
				thirdParty.setFax(rs.getString(20));
				thirdParty.setWeb(rs.getString(21));
				thirdParty.setReferenceNo(rs.getString(22));
				thirdParty.setCompnyEmail(rs.getString(23));
				
				thirdParty.setWarningMsg(rs.getString(24));
				thirdParty.setAccountsNotes(rs.getString(25));
				thirdParty.setAccountMustBeInCredit(rs.getBoolean(26));
				thirdParty.setAccountCreditLimit(rs.getString(27));
				thirdParty.setAccountWarningLimit(rs.getString(28));
				thirdParty.setDefaultApmtBookingConfmTemp(rs.getString(29));
				thirdParty.setConfContactEmail(rs.getString(30));
				thirdParty.setDefaultApmtDnaTemp(rs.getString(31));
				thirdParty.setDnaContactEmail(rs.getString(32));
				thirdParty.setDnaLimit(rs.getString(33));
				thirdParty.setOutInvoiceLimit(rs.getString(34));
				
				list.add(thirdParty);
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	public int getThirdPartyTypeCount() {
		PreparedStatement preparedStatement = null;
		int result = 0;
		
		String sql = "select count(*) from apm_third_party";
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

	

	public ArrayList<ThirdParty> getThirdPartyList(Pagination pagination) {
		PreparedStatement preparedStatement = null;
		ArrayList<ThirdParty>list = new ArrayList<ThirdParty>();
		String sql = "";
		if(pagination.sortColumn==null){
		 sql = "select id,name,description from apm_third_party order by id desc";
		}
		else{
		sql = "select id,name,description from apm_third_party";

		}
		sql = pagination.getSQLQuery(sql);
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				ThirdParty thirdParty = new ThirdParty();
				thirdParty.setId(rs.getInt(1));
				thirdParty.setName(rs.getString(2));
				thirdParty.setDescription(rs.getString(3));
				list.add(thirdParty);
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ThirdParty getThirdPartyDetails(String id) {
		PreparedStatement preparedStatement = null;
		ThirdParty thirdParty = new ThirdParty();
		String sql = "select id,third_party_id,type,name,salutation,title,department,search_name,telephone_line,email,email_cc,notes,"
				+ "company_details,company_name,address,town,country,postcode,telephone,fax,web,reference_no,company_email,warning_msg,"
				+ "account_notes,acc_must_be_in_credit,acc_warning_limit,default_apmt_booking_confim,confm_contact_email,default_apmt_dna,"
				+ "dna_contact_email,dnaLimit,outInvoiceLimit,creditDuration,creditReminderDuration,gptown,county,ipdshare,surgeonshare,unit,area,shortname,opdshare,maintp,chargecolumnname from apm_third_party_details where id = "+id+" order by company_name";
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				
				thirdParty.setId(rs.getInt(1));
				thirdParty.setThirdPartyId(rs.getInt(2));
				thirdParty.setType(rs.getString(3));
				thirdParty.setName(rs.getString(4));
				
				
				thirdParty.setSalutation(rs.getString(5));
				thirdParty.setTitle(rs.getString(6));
				thirdParty.setDepartment(rs.getString(7));
				thirdParty.setSearchName(rs.getString(8));
				thirdParty.setTelephoneLine(rs.getString(9));
				thirdParty.setEmail(rs.getString(10));
				thirdParty.setEmailCc(rs.getString(11));
				thirdParty.setNotes(rs.getString(12));
				thirdParty.setCompanyDetails(rs.getString(13));
				
				thirdParty.setCompanyName(rs.getString(14));
				thirdParty.setAddress(rs.getString(15));
				thirdParty.setTown(rs.getString(16));
				thirdParty.setCountry(rs.getString(17));
				thirdParty.setPostcode(rs.getString(18));
				thirdParty.setCompnyTelephone(rs.getString(19));
				thirdParty.setFax(rs.getString(20));
				thirdParty.setWeb(rs.getString(21));
				thirdParty.setReferenceNo(rs.getString(22));
				thirdParty.setCompnyEmail(rs.getString(23));
				
				thirdParty.setWarningMsg(rs.getString(24));
				thirdParty.setAccountsNotes(rs.getString(25));
				thirdParty.setAccountMustBeInCredit(rs.getBoolean(26));
				
				thirdParty.setAccountWarningLimit(rs.getString(27));
				thirdParty.setDefaultApmtBookingConfmTemp(rs.getString(28));
				thirdParty.setConfContactEmail(rs.getString(29));
				thirdParty.setDefaultApmtDnaTemp(rs.getString(30));
				thirdParty.setDnaContactEmail(rs.getString(31));
				thirdParty.setDnaLimit(rs.getString(32));
				thirdParty.setOutInvoiceLimit(rs.getString(33));
				thirdParty.setCreditDuration(rs.getString(34));
				thirdParty.setCreditReminderDuration(rs.getString(35));
				thirdParty.setGptown(rs.getString(36));
				thirdParty.setCounty(rs.getString(37));
				thirdParty.setIpdShare(rs.getInt(38));
				thirdParty.setSurgeonShare(rs.getInt(39));
				thirdParty.setUnit(rs.getString(40));
				thirdParty.setArea(rs.getString(41));
				thirdParty.setShortname(rs.getString(42));
				thirdParty.setOpdShare(rs.getInt(43));
				thirdParty.setMaintp(rs.getBoolean(44));
				thirdParty.setChargecolumnname(rs.getString(45));
		}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return thirdParty;
	}

	public int saveNewDetails(String thirdPartyType, ThirdParty thirdParty) {
		int result = 0;
		
		PreparedStatement pstm = null;
		
		
		String sql = "insert into apm_third_party_details(third_party_id,type,company_name,telephone,company_email,acc_warning_limit,dnaLimit,outInvoiceLimit,clinicId) values (?,?,?,?,?,?,?,?,?)";
		try {
			pstm = connection.prepareStatement(sql);
			pstm.setString(1, thirdParty.getType());;
			String typeName = getTypeName(thirdParty.getType());
			pstm.setString(2, typeName);
			pstm.setString(3, thirdParty.getCompanyName());
			pstm.setString(4, thirdParty.getCompnyTelephone());
			pstm.setString(5, thirdParty.getCompnyEmail());
			pstm.setString(6, thirdParty.getAccountWarningLimit());
			
			pstm.setString(7, thirdParty.getDnaLimit());
			pstm.setString(8, thirdParty.getOutInvoiceLimit());
			pstm.setInt(9, thirdParty.getClinicId());
			result = pstm.executeUpdate();
			
		}catch (Exception e) {
			
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return result;
	}

	public int getTotalThirdPartiesCount(int id) {
		// TODO Auto-generated method stub
		PreparedStatement preparedStatement = null;
		int result = 0;
		
		String sql = "select count(*) from apm_third_party_details";
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

	public ArrayList<ThirdParty> getThirdPartyDetailsList(int id) {
		PreparedStatement preparedStatement = null;
		ArrayList<ThirdParty>list = new ArrayList<ThirdParty>();
		String sql = "select id,third_party_id,type,name,salutation,title,department,search_name,telephone_line,email,email_cc,"
				+ "notes,company_details,company_name,address,town,country,postcode,telephone,fax,web,reference_no,company_email,"
				+ "warning_msg,account_notes,acc_must_be_in_credit,acc_credit_limit,acc_warning_limit,default_apmt_booking_confim,"
				+ "confm_contact_email,default_apmt_dna,dna_contact_email,dnaLimit,outInvoiceLimit from apm_third_party_details where "
				+ "company_name!='' order by company_name limit 10";
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				ThirdParty thirdParty = new ThirdParty();
				thirdParty.setId(rs.getInt(1));
				thirdParty.setThirdPartyId(rs.getInt(2));
				thirdParty.setType(rs.getString(3));
				thirdParty.setName(rs.getString(4));
				
				
				thirdParty.setSalutation(rs.getString(5));
				thirdParty.setTitle(rs.getString(6));
				thirdParty.setDepartment(rs.getString(7));
				thirdParty.setSearchName(rs.getString(8));
				thirdParty.setTelephoneLine(rs.getString(9));
				thirdParty.setEmail(rs.getString(10));
				thirdParty.setEmailCc(rs.getString(11));
				thirdParty.setNotes(rs.getString(12));
				thirdParty.setCompanyDetails(rs.getString(13));
				
				thirdParty.setCompanyName(rs.getString(14));
				thirdParty.setAddress(rs.getString(15));
				thirdParty.setTown(rs.getString(16));
				thirdParty.setCountry(rs.getString(17));
				thirdParty.setPostcode(rs.getString(18));
				thirdParty.setCompnyTelephone(rs.getString(19));
				thirdParty.setFax(rs.getString(20));
				thirdParty.setWeb(rs.getString(21));
				thirdParty.setReferenceNo(rs.getString(22));
				thirdParty.setCompnyEmail(rs.getString(23));
				
				thirdParty.setWarningMsg(rs.getString(24));
				thirdParty.setAccountsNotes(rs.getString(25));
				thirdParty.setAccountMustBeInCredit(rs.getBoolean(26));
				thirdParty.setAccountCreditLimit(rs.getString(27));
				thirdParty.setAccountWarningLimit(rs.getString(28));
				thirdParty.setDefaultApmtBookingConfmTemp(rs.getString(29));
				thirdParty.setConfContactEmail(rs.getString(30));
				thirdParty.setDefaultApmtDnaTemp(rs.getString(31));
				thirdParty.setDnaContactEmail(rs.getString(32));
				thirdParty.setDnaLimit(rs.getString(33));
				thirdParty.setOutInvoiceLimit(rs.getString(34));
				
				
				list.add(thirdParty);
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	public int getTotalThirdPartiesCount(int id, String type,String searchText) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		
		String sql = "select count(*) from apm_third_party_details where third_party_id like ('%"+type+"%') and company_name like ('%"+searchText+"%')";

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

	public int saveData(String type, ThirdParty thirdParty) {

		int result = 0;		
		PreparedStatement pstm = null;		
		String sql = "insert into apm_third_party_details(third_party_id,type,name,telephone_line,email,company_name,"
				+ "address,town,country,telephone,company_email,acc_warning_limit,dnaLimit,outInvoiceLimit,clinicId,creditDuration,"
				+ "creditReminderDuration,gptown,gpaddress) "
				+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			pstm = connection.prepareStatement(sql);
			pstm.setString(1, thirdParty.getType());;
			String typeName = getTypeName(thirdParty.getType());
			pstm.setString(2, typeName);
			pstm.setString(3, thirdParty.getName());
			pstm.setString(4, thirdParty.getTelephoneLine());
			pstm.setString(5, thirdParty.getEmail());
			
			pstm.setString(6, thirdParty.getCompanyName());
			pstm.setString(7, thirdParty.getAddress());
			pstm.setString(8, thirdParty.getTown());
			pstm.setString(9, thirdParty.getCountry());
			pstm.setString(10, thirdParty.getCompnyTelephone());
			pstm.setString(11, thirdParty.getCompnyEmail());
			
			pstm.setString(12, thirdParty.getAccountWarningLimit());
			pstm.setString(13, thirdParty.getDnaLimit());
			pstm.setString(14, thirdParty.getOutInvoiceLimit());
			pstm.setInt(15, thirdParty.getClinicId());
			pstm.setString(16, thirdParty.getCreditDuration());
			pstm.setString(17, thirdParty.getCreditReminderDuration());
			pstm.setString(18, thirdParty.getGptown());
			pstm.setString(19, thirdParty.getGpaddress());

			
		
			
			pstm.executeUpdate();			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	
	public boolean isEmailIdExist(String email) {
		PreparedStatement preparedStatement = null;
		boolean result = false;
		String sql = "select * from apm_third_party_details where email = '"+email+"' or email_cc = '"+email+"' or company_email = '"+email+"' or confm_contact_email = '"+email+"' or dna_contact_email = '"+email+"' ";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				result = true;
			}
		}catch (Exception e) {
			
		}
		
		
		return result;
	}

	
	public boolean isTelePhoneExist(String telephone) {
		PreparedStatement preparedStatement = null;
		boolean result = false;
		String sql = "select * from apm_third_party_details where telephone_line = '"+telephone+"' or telephone = '"+telephone+"'  ";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				result = true;
			}
		}catch (Exception e) {
			
		}
		
		
		return result;
	}

	public int saveGp(String type, ThirdParty thirdParty,String tpid) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "insert into apm_gp_details(tptypeid,tpid,gpname,note,fax,email,workno) values(?,?,?,?,?,?,?)";
		try{
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, "1");
			preparedStatement.setString(2, tpid);
			preparedStatement.setString(3, thirdParty.getGpname());
			preparedStatement.setString(4, thirdParty.getNotes());
			preparedStatement.setString(5, thirdParty.getGpfax());
			preparedStatement.setString(6, thirdParty.getGpemailid());
			preparedStatement.setString(7, thirdParty.getWorkphno());
			result = preparedStatement.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}
	
	
	public int updateThirpartyAppointmentType(String name,
			String charges, String duration,String dnaCharge,boolean dnaoffset,int aptypeid){
		
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "update apm_appointment_type set duration=?,charges=?,dnacharge=?,dnaoffset=?,name=? where id=? ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, duration);
			preparedStatement.setString(2, charges);
			preparedStatement.setString(3, dnaCharge);
			preparedStatement.setBoolean(4, dnaoffset);
			preparedStatement.setString(5, name);
			preparedStatement.setInt(6, aptypeid);
			
			result = preparedStatement.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return result;
	}
	
	
	public int updateDynamicAppointmentTypeData(int id, ThirdParty thirdParty,String tpid){
		PreparedStatement preparedStatement = null;
		int result = 0;
		
		if(thirdParty.getApmtCharge().equals("")){
			thirdParty.setApmtCharge("0.00");
		}
		if(thirdParty.getDnaCharge().equals("")){
			thirdParty.setDnaCharge("0.00");
		}
		
		String sql = "update apm_appointment_type set name=?,duration=?,charges=?,tpid=?,dnacharge=?,dnaoffset=? where id="+id+" ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, thirdParty.getDnaName());
			preparedStatement.setString(2, thirdParty.getApmtDuaration());
			preparedStatement.setString(3, thirdParty.getApmtCharge());
			preparedStatement.setString(4, tpid);
			preparedStatement.setString(5, thirdParty.getDnaCharge());
			preparedStatement.setBoolean(6, thirdParty.isOffset());
			
			result = preparedStatement.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int saveDynamicAppointmentTypeData(int tpid, ThirdParty thirdParty){
		PreparedStatement preparedStatement = null;
		int result = 0;
		
		if(thirdParty.getApmtCharge().equals("")){
			thirdParty.setApmtCharge("0.00");
		}
		if(thirdParty.getDnaCharge().equals("")){
			thirdParty.setDnaCharge("0.00");
		}
		
		String sql = "insert into apm_appointment_type(name,duration,charges,chargeType,tpid,dnacharge,dnaoffset,aptype) values(?,?,?,?,?,?,?,?)";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, thirdParty.getDnaName());
			preparedStatement.setString(2, thirdParty.getApmtDuaration());
			preparedStatement.setString(3, thirdParty.getApmtCharge());
			preparedStatement.setString(4, "Appointment Charge");
			preparedStatement.setInt(5, tpid);
			preparedStatement.setString(6, thirdParty.getDnaCharge());
			preparedStatement.setBoolean(7, thirdParty.isOffset());
			preparedStatement.setInt(8, Constants.DYNAMIC_APPOINTMENT_TYPE);
			
			result = preparedStatement.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int saveInitialAppointmentTYpe(ThirdParty thirdParty,int tpid){
		PreparedStatement preparedStatement = null;
		int result = 0;
		if(thirdParty.getCompletedInitialCharge().equals("")){
			thirdParty.setCompletedInitialCharge("0.00");
		}
		if(thirdParty.getDnaInitialCharge().equals("")){
			thirdParty.setDnaInitialCharge("0.00");
		}
		String sql = "insert into apm_appointment_type(name,duration,charges,chargeType,tpid,dnacharge,dnaoffset,aptype) values(?,?,?,?,?,?,?,?)";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, thirdParty.getInitialAppointmentName());
			preparedStatement.setString(2, thirdParty.getCompltInitialApmtDuration());
			preparedStatement.setString(3, thirdParty.getCompletedInitialCharge());
			preparedStatement.setString(4, "Appointment Charge");
			preparedStatement.setInt(5, tpid);
			preparedStatement.setString(6, thirdParty.getDnaInitialCharge());
			preparedStatement.setBoolean(7, thirdParty.isInitialOffsetdna());
			preparedStatement.setInt(8, Constants.INITIAL_APPOINTMENT_TYPE);
			
			result = preparedStatement.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int saveFollowupAppointmentTYpe(ThirdParty thirdParty,int tpid){
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "insert into apm_appointment_type(name,duration,charges,chargeType,tpid,dnacharge,dnaoffset,aptype) values(?,?,?,?,?,?,?,?)";
		
	
		if(thirdParty.getCompletedFollowupCharge().equals("")){
			thirdParty.setCompletedFollowupCharge("0.00");
		}
		if(thirdParty.getDnaFollowupCharge().equals("")){
			thirdParty.setDnaFollowupCharge("0.00");
		}
		
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, thirdParty.getFollowupAppointmentName());
			preparedStatement.setString(2, thirdParty.getCompltfollowupApmtDuration());
			preparedStatement.setString(3, thirdParty.getCompletedFollowupCharge());
			preparedStatement.setString(4, "Appointment Charge");
			preparedStatement.setInt(5, tpid);
			preparedStatement.setString(6, thirdParty.getDnaFollowupCharge());
			preparedStatement.setBoolean(7, thirdParty.isFollowupOffsetdna());
			preparedStatement.setInt(8, Constants.FOLLOWUP_APPOINTMENT_TYPE);
			
			result = preparedStatement.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int saveFinalAppointmentTYpe(ThirdParty thirdParty,int tpid){
		PreparedStatement preparedStatement = null;
		int result = 0;
		
		if(thirdParty.getCompletedFinalCharge().equals("")){
			thirdParty.setCompletedFinalCharge("0.00");
		}
		
		if(thirdParty.getDnaFinalCharge().equals("")){
			thirdParty.setDnaFinalCharge("0.00");
		}
		
		String sql = "insert into apm_appointment_type(name,duration,charges,chargeType,tpid,dnacharge,dnaoffset,aptype) values(?,?,?,?,?,?,?,?)";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, thirdParty.getFinalAppointmentName());
			preparedStatement.setString(2, thirdParty.getCompltfinalApmtDuration());
			preparedStatement.setString(3, thirdParty.getCompletedFinalCharge());
			preparedStatement.setString(4, "Appointment Charge");
			preparedStatement.setInt(5, tpid);
			preparedStatement.setString(6, thirdParty.getDnaFinalCharge());
			preparedStatement.setBoolean(7, thirdParty.isFinalOffsetdna());
			preparedStatement.setInt(8, Constants.FINAL_APPOINTMENT_TYPE);
			
			
			result = preparedStatement.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	public int saveMaintnanceAppointmentTYpe(ThirdParty thirdParty,int tpid){
		PreparedStatement preparedStatement = null;
		int result = 0;
		
		if(thirdParty.getCompletedMaintnanceCharge().equals("")){
			thirdParty.setCompletedMaintnanceCharge("0.00");
		}
		
		if(thirdParty.getDnaMaintnanceCharge().equals("")){
			thirdParty.setDnaMaintnanceCharge("0.00");
		}
		
		String sql = "insert into apm_appointment_type(name,duration,charges,chargeType,tpid,dnacharge,dnaoffset,aptype) values(?,?,?,?,?,?,?,?)";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, thirdParty.getMaintnanceAppointmentName());
			preparedStatement.setString(2, thirdParty.getCompltmaintenanceApmtDuration());
			preparedStatement.setString(3, thirdParty.getCompletedMaintnanceCharge());
			preparedStatement.setString(4, "Appointment Charge");
			preparedStatement.setInt(5, tpid);
			preparedStatement.setString(6, thirdParty.getDnaMaintnanceCharge());
			preparedStatement.setBoolean(7, thirdParty.isMaintnanceOffsetdna());
			preparedStatement.setInt(8, Constants.MAINTNANCE_APPOINTMENT_TYPE);
			
			result = preparedStatement.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}
	
	

	public int saveTp(String type, ThirdParty thirdParty,int id) {
		int result = 0;
		int tpid = 0;
		PreparedStatement pstm = null;
		String sql = "insert into apm_third_party_details(third_party_id,type,telephone_line,company_name,address,town,country,postcode,"
				+ "company_email,acc_warning_limit,dnaLimit,outInvoiceLimit,clinicId,creditDuration,creditReminderDuration,dnaInitialApmt,"
				+ "dnafollowupApmt,dnafinalApmt,dnamaintenanceApmt,compltInitialApmt,compltfollowupApmt,compltfinalApmt,compltmaintenanceApmt,"
				+ "complt_inital_duration,complt_followup_duration,complt_final_duration,complt_maintence_duration,acct_setting_notes,"
				+ "email_cc,fax,web,second_line_address,dnaforall,ipdshare,surgeonshare,opdshare,unit,area,shortname,maintp) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			pstm = connection.prepareStatement(sql);
			pstm.setString(1, thirdParty.getType());;
			String typeName = getTypeName(thirdParty.getType());
			pstm.setString(2, typeName);
			pstm.setString(3, thirdParty.getTelephoneLine());
			pstm.setString(4, thirdParty.getCompanyName());
			pstm.setString(5, thirdParty.getAddress());
			pstm.setString(6, thirdParty.getTown());
			pstm.setString(7, thirdParty.getCountry());
			pstm.setString(8, thirdParty.getPostcode());
			pstm.setString(9, thirdParty.getCompnyEmail());
			pstm.setString(10, thirdParty.getAccountWarningLimit());
			pstm.setString(11, thirdParty.getDnaLimit());
			pstm.setString(12, thirdParty.getOutInvoiceLimit());
			pstm.setInt(13, id);
			pstm.setString(14, thirdParty.getCreditDuration());
			pstm.setString(15, thirdParty.getCreditReminderDuration());
			pstm.setString(16, thirdParty.getDnaInitialApmt());
			pstm.setString(17, thirdParty.getDnafollowupApmt());
			pstm.setString(18, thirdParty.getDnafinalApmt());
			pstm.setString(19, thirdParty.getDnamaintenanceApmt());
			pstm.setString(20, thirdParty.getCompltInitialApmt());
			pstm.setString(21, thirdParty.getCompltfollowupApmt());
			pstm.setString(22, thirdParty.getCompltfinalApmt());
			pstm.setString(23, thirdParty.getCompltmaintenanceApmt());
			pstm.setString(24, thirdParty.getCompltInitialApmtDuration());
			pstm.setString(25, thirdParty.getCompltfollowupApmtDuration());
			pstm.setString(26, thirdParty.getCompltfinalApmtDuration());
			pstm.setString(27, thirdParty.getCompltmaintenanceApmtDuration());
			pstm.setString(28, thirdParty.getTpAccountSettingNotes());
			pstm.setString(29, thirdParty.getEmailCc());
			pstm.setString(30, thirdParty.getFax());
			pstm.setString(31, thirdParty.getWeb());
			pstm.setString(32, thirdParty.getSecondLineAddress());
			pstm.setBoolean(33, thirdParty.isDnaForAll());
			pstm.setInt(34, thirdParty.getIpdShare());
			pstm.setInt(35, thirdParty.getSurgeonShare());
			pstm.setInt(36, thirdParty.getOpdShare());
			pstm.setString(37, thirdParty.getUnit());
			pstm.setString(38, thirdParty.getArea());
			pstm.setString(39, thirdParty.getShortname());
			pstm.setBoolean(40, thirdParty.isMaintp());
			result = pstm.executeUpdate();
			if(result == 1){
				ResultSet resultSet = pstm.getGeneratedKeys();
				if(resultSet.next()){
					tpid = resultSet.getInt(1);  
				}
			}
			
		}catch (Exception e) {
			
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return tpid;
	}

	public ArrayList<ThirdParty> getThirdPartyDetailsList(int id, String type) {
		PreparedStatement preparedStatement = null;
		ArrayList<ThirdParty>list = new ArrayList<ThirdParty>();
		String sql = "select id,third_party_id,type,name,salutation,title,department,search_name,telephone_line,email,email_cc,notes,company_details,company_name,address,town,country,postcode,telephone,fax,web,reference_no,company_email,warning_msg,account_notes,acc_must_be_in_credit,acc_credit_limit,acc_warning_limit,default_apmt_booking_confim,confm_contact_email,default_apmt_dna,dna_contact_email,dnaLimit,outInvoiceLimit from apm_third_party_details where company_name!='' and third_party_id = "+type+"  order by company_name";
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				ThirdParty thirdParty = new ThirdParty();
				thirdParty.setId(rs.getInt(1));
				thirdParty.setThirdPartyId(rs.getInt(2));
				thirdParty.setType(rs.getString(3));
				thirdParty.setName(rs.getString(4));
				
				
				thirdParty.setSalutation(rs.getString(5));
				thirdParty.setTitle(rs.getString(6));
				thirdParty.setDepartment(rs.getString(7));
				thirdParty.setSearchName(rs.getString(8));
				thirdParty.setTelephoneLine(rs.getString(9));
				thirdParty.setEmail(rs.getString(10));
				thirdParty.setEmailCc(rs.getString(11));
				thirdParty.setNotes(rs.getString(12));
				thirdParty.setCompanyDetails(rs.getString(13));
				
				thirdParty.setCompanyName(rs.getString(14));
				thirdParty.setAddress(rs.getString(15));
				thirdParty.setTown(rs.getString(16));
				thirdParty.setCountry(rs.getString(17));
				thirdParty.setPostcode(rs.getString(18));
				thirdParty.setCompnyTelephone(rs.getString(19));
				thirdParty.setFax(rs.getString(20));
				thirdParty.setWeb(rs.getString(21));
				thirdParty.setReferenceNo(rs.getString(22));
				thirdParty.setCompnyEmail(rs.getString(23));
				
				thirdParty.setWarningMsg(rs.getString(24));
				thirdParty.setAccountsNotes(rs.getString(25));
				thirdParty.setAccountMustBeInCredit(rs.getBoolean(26));
				thirdParty.setAccountCreditLimit(rs.getString(27));
				thirdParty.setAccountWarningLimit(rs.getString(28));
				thirdParty.setDefaultApmtBookingConfmTemp(rs.getString(29));
				thirdParty.setConfContactEmail(rs.getString(30));
				thirdParty.setDefaultApmtDnaTemp(rs.getString(31));
				thirdParty.setDnaContactEmail(rs.getString(32));
				thirdParty.setDnaLimit(rs.getString(33));
				thirdParty.setOutInvoiceLimit(rs.getString(34));
				
				
				list.add(thirdParty);
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	public String getFullName(String id) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT company_name FROM apm_third_party_details where  id = "+id+"";
		
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

	public String getThirdPartyEmail(String id) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT company_email FROM apm_third_party_details where  id = "+id+"";
		
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

	public ArrayList<ThirdParty> getThirdPartyDetailsList(String searchTP,
			int id) {
		PreparedStatement preparedStatement = null;
		ArrayList<ThirdParty>list = new ArrayList<ThirdParty>();
		String sql = "";
		
			sql = "select id,third_party_id,type,name,salutation,title,department,search_name,telephone_line,email,email_cc,"
					+ "notes,company_details,company_name,address,town,country,postcode,telephone,fax,web,reference_no,"
					+ "company_email,warning_msg,account_notes,acc_must_be_in_credit,acc_credit_limit,acc_warning_limit,"
					+ "default_apmt_booking_confim,confm_contact_email,default_apmt_dna,dna_contact_email,dnaLimit,"
					+ "outInvoiceLimit from apm_third_party_details where company_email like('%"+searchTP+"%') or "
					+ "telephone like('%"+searchTP+"%') or postcode like ('%"+searchTP+"%') or company_name like "
					+ "('%"+searchTP+"%') order by id desc";
				
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				ThirdParty thirdParty = new ThirdParty();
				thirdParty.setId(rs.getInt(1));
				thirdParty.setThirdPartyId(rs.getInt(2));
				thirdParty.setType(rs.getString(3));
				thirdParty.setName(rs.getString(4));
				
				
				thirdParty.setSalutation(rs.getString(5));
				thirdParty.setTitle(rs.getString(6));
				thirdParty.setDepartment(rs.getString(7));
				thirdParty.setSearchName(rs.getString(8));
				thirdParty.setTelephoneLine(rs.getString(9));
				thirdParty.setEmail(rs.getString(10));
				thirdParty.setEmailCc(rs.getString(11));
				thirdParty.setNotes(rs.getString(12));
				thirdParty.setCompanyDetails(rs.getString(13));
				
				thirdParty.setCompanyName(rs.getString(14));
				thirdParty.setAddress(rs.getString(15));
				thirdParty.setTown(rs.getString(16));
				thirdParty.setCountry(rs.getString(17));
				thirdParty.setPostcode(rs.getString(18));
				thirdParty.setCompnyTelephone(rs.getString(19));
				thirdParty.setFax(rs.getString(20));
				thirdParty.setWeb(rs.getString(21));
				thirdParty.setReferenceNo(rs.getString(22));
				thirdParty.setCompnyEmail(rs.getString(23));
				
				thirdParty.setWarningMsg(rs.getString(24));
				thirdParty.setAccountsNotes(rs.getString(25));
				thirdParty.setAccountMustBeInCredit(rs.getBoolean(26));
				thirdParty.setAccountCreditLimit(rs.getString(27));
				thirdParty.setAccountWarningLimit(rs.getString(28));
				thirdParty.setDefaultApmtBookingConfmTemp(rs.getString(29));
				thirdParty.setConfContactEmail(rs.getString(30));
				thirdParty.setDefaultApmtDnaTemp(rs.getString(31));
				thirdParty.setDnaContactEmail(rs.getString(32));
				thirdParty.setDnaLimit(rs.getString(33));
				thirdParty.setOutInvoiceLimit(rs.getString(34));
				
				
				list.add(thirdParty);
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	public ThirdParty getTPDetails(String tpname) {
		
				PreparedStatement preparedStatement = null;
				ThirdParty thirdParty = new ThirdParty();
				String sql = "";
				sql = "select id,third_party_id,telephone_line,company_name,address,town,country,postcode,"
						+ "company_email,acc_warning_limit,dnaLimit,outInvoiceLimit,creditDuration,creditReminderDuration,dnaInitialApmt,"
						+ "dnafollowupApmt,dnafinalApmt,dnamaintenanceApmt,compltInitialApmt,compltfollowupApmt,compltfinalApmt,compltmaintenanceApmt,"
						+ "complt_inital_duration,complt_followup_duration,complt_final_duration,complt_maintence_duration,inital_apmt_name,followup_apmt_name,"
						+ "final_apmtname,maintnance_apmtname,acct_setting_notes,email_cc,fax,web,second_line_address,dnaforall,ipdshare,surgeonshare,opdshare,unit,area,shortname,maintp from apm_third_party_details where id = "+tpname+"" ;
						
				try{
					preparedStatement = connection.prepareStatement(sql);
					ResultSet rs = preparedStatement.executeQuery();
					while(rs.next()){
						thirdParty.setTypeName(rs.getString(1));
						thirdParty.setType(rs.getString(2));
						thirdParty.setTelephoneLine(rs.getString(3));
						thirdParty.setCompanyName(rs.getString(4));
						thirdParty.setAddress(rs.getString(5));
						thirdParty.setTown(rs.getString(6));
						thirdParty.setCountry(rs.getString(7));
						thirdParty.setPostcode(rs.getString(8));
						thirdParty.setCompnyEmail(rs.getString(9));
						thirdParty.setAccountWarningLimit(rs.getString(10));
						thirdParty.setDnaLimit(rs.getString(11));
						thirdParty.setOutInvoiceLimit(rs.getString(12));
						thirdParty.setCreditDuration(rs.getString(13));
						thirdParty.setCreditReminderDuration(rs.getString(14));
						thirdParty.setDnaInitialApmt(rs.getString(15));
						thirdParty.setDnafollowupApmt(rs.getString(16));
						thirdParty.setDnafinalApmt(rs.getString(17));
						thirdParty.setDnamaintenanceApmt(rs.getString(18));
						thirdParty.setCompltInitialApmt(rs.getString(19));
						thirdParty.setCompltfollowupApmt(rs.getString(20));
						thirdParty.setCompltfinalApmt(rs.getString(21));
						thirdParty.setCompltmaintenanceApmt(rs.getString(22));
						thirdParty.setCompltInitialApmtDuration(rs.getString(23));
						thirdParty.setCompltfollowupApmtDuration(rs.getString(24));
						thirdParty.setCompltfinalApmtDuration(rs.getString(25));
						thirdParty.setCompltmaintenanceApmtDuration(rs.getString(26));
						
						//name
						thirdParty.setInitialAppointmentName(rs.getString(27));
						thirdParty.setFollowupAppointmentName(rs.getString(28));
						thirdParty.setFinalAppointmentName(rs.getString(29));
						thirdParty.setMaintnanceAppointmentName(rs.getString(30));
						thirdParty.setTpAccountSettingNotes(rs.getString(31));
						thirdParty.setEmailCc(rs.getString(32));
						thirdParty.setFax(rs.getString(33));
						thirdParty.setWeb(rs.getString(34));
						thirdParty.setSecondLineAddress(rs.getString(35));
						thirdParty.setDnaForAll(rs.getBoolean(36));
						thirdParty.setIpdShare(rs.getInt(37));
						thirdParty.setSurgeonShare(rs.getInt(38));
						thirdParty.setOpdShare(rs.getInt(39));
						thirdParty.setUnit(rs.getString(40));
						thirdParty.setArea(rs.getString(41));
						thirdParty.setShortname(rs.getString(42));
						thirdParty.setMaintp(rs.getBoolean(43));
					}  
					
					
				}catch (Exception e) {
					e.printStackTrace();
				}
				
				return thirdParty;		
	}

	public int updateTp(String type, ThirdParty thirdParty,int id,String typename) {
		int result = 0;
		int tpid = 0;
		PreparedStatement pstm = null;
		String sql = "update apm_third_party_details set telephone_line = ?,address = ?,town = ?,country = ?,postcode = ?,"
				+ "company_email = ?,acc_warning_limit = ?,dnaLimit = ?,outInvoiceLimit = ?,clinicId = ?,creditDuration = ?,creditReminderDuration = ?,dnaInitialApmt = ?,"
				+ "dnafollowupApmt = ?,dnafinalApmt = ?,dnamaintenanceApmt = ?,compltInitialApmt = ?,compltfollowupApmt = ?,compltfinalApmt = ?,compltmaintenanceApmt = ?,"
				+ "complt_inital_duration = ?,complt_followup_duration = ?,complt_final_duration = ?,complt_maintence_duration = ?,acct_setting_notes=?,email_cc = ?,fax = ?,"
				+ "web = ?,second_line_address = ?,dnaforall=?,ipdshare=?,surgeonshare=?,opdshare=?,unit=?,area=?,shortname=?,maintp=?  where id = "+typename+"";
		try {
			pstm = connection.prepareStatement(sql);
			
			pstm.setString(1, thirdParty.getTelephoneLine());
			pstm.setString(2, thirdParty.getAddress());
			pstm.setString(3, thirdParty.getTown());
			pstm.setString(4, thirdParty.getCountry());
			pstm.setString(5, thirdParty.getPostcode());
			pstm.setString(6, thirdParty.getCompnyEmail());
			pstm.setString(7, thirdParty.getAccountWarningLimit());
			pstm.setString(8, thirdParty.getDnaLimit());
			pstm.setString(9, thirdParty.getOutInvoiceLimit());
			pstm.setInt(10, id);
			pstm.setString(11, thirdParty.getCreditDuration());
			pstm.setString(12, thirdParty.getCreditReminderDuration());
			pstm.setString(13, thirdParty.getDnaInitialApmt());
			pstm.setString(14, thirdParty.getDnafollowupApmt());
			pstm.setString(15, thirdParty.getDnafinalApmt());
			pstm.setString(16, thirdParty.getDnamaintenanceApmt());
			pstm.setString(17, thirdParty.getCompltInitialApmt());
			pstm.setString(18, thirdParty.getCompltfollowupApmt());
			pstm.setString(19, thirdParty.getCompltfinalApmt());
			pstm.setString(20, thirdParty.getCompltmaintenanceApmt());
			pstm.setString(21, thirdParty.getCompltInitialApmtDuration());
			pstm.setString(22, thirdParty.getCompltfollowupApmtDuration());
			pstm.setString(23, thirdParty.getCompltfinalApmtDuration());
			pstm.setString(24, thirdParty.getCompltmaintenanceApmtDuration());
			pstm.setString(25, thirdParty.getTpAccountSettingNotes());
			pstm.setString(26, thirdParty.getEmailCc());
			pstm.setString(27, thirdParty.getFax());
			pstm.setString(28, thirdParty.getWeb());
			pstm.setString(29, thirdParty.getSecondLineAddress());
			pstm.setBoolean(30, thirdParty.isDnaForAll());
			pstm.setInt(31, thirdParty.getIpdShare());
			pstm.setInt(32, thirdParty.getSurgeonShare());
			pstm.setInt(33, thirdParty.getOpdShare());
			pstm.setString(34, thirdParty.getUnit());
			pstm.setString(35, thirdParty.getArea());
			pstm.setString(36, thirdParty.getShortname());
			pstm.setBoolean(37, thirdParty.isMaintp());
			result = pstm.executeUpdate();
			if(result == 1){
				ResultSet resultSet = pstm.getGeneratedKeys();
				if(resultSet.next()){
					tpid = resultSet.getInt(1);  
				}
			}
			
		}catch (Exception e) {
			
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return tpid;
	}

	public ArrayList<Client> getTPNameList(String id) {
		PreparedStatement preparedStatement = null;
		ArrayList<Client>list = new ArrayList<Client>();
		String sql = "select id,company_name from apm_third_party_details where third_party_id = "+id+" and company_name!='' order by company_name";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Client thirdParty = new Client();
				thirdParty.setId(rs.getInt(1));
				thirdParty.setTypeName(rs.getString(2));
				
				list.add(thirdParty);
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<ThirdParty> getTPNameList() {
		PreparedStatement preparedStatement = null;
		ArrayList<ThirdParty>list = new ArrayList<ThirdParty>();
		String sql = "select id,company_name from apm_third_party_details where company_name!='' order by company_name";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				ThirdParty thirdParty = new ThirdParty();
				thirdParty.setId(rs.getInt(1));
				thirdParty.setTypeName(rs.getString(2));
				
				list.add(thirdParty);
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	

	
	

	public ArrayList<ThirdParty> getSurjeryCompanyDetailsList(int id) {
		PreparedStatement preparedStatement = null;
		ArrayList<ThirdParty> list = new ArrayList<ThirdParty>();
		String sql = "select id,third_party_id,type,name,company_name,company_email,telephone,address,town,country,postcode "
					 + "from apm_third_party_details where third_party_id = 1 limit 10";
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				ThirdParty thirdParty = new ThirdParty();
				thirdParty.setId(rs.getInt(1));
				thirdParty.setThirdPartyId(rs.getInt(2));
				thirdParty.setType(rs.getString(3));
				thirdParty.setName(rs.getString(4));
				thirdParty.setCompanyName(rs.getString(5));
				thirdParty.setCompnyEmail(rs.getString(6));
				thirdParty.setCompnyTelephone(rs.getString(7));
				thirdParty.setAddress(rs.getString(8));
				thirdParty.setTown(rs.getString(9));
				thirdParty.setCountry(rs.getString(10));
				thirdParty.setPostcode(rs.getString(11));
				
				list.add(thirdParty);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<ThirdParty> getTPCompanyNameList(int id) {
		PreparedStatement preparedStatement = null;
		ArrayList<ThirdParty> list = new ArrayList<ThirdParty>();
		String sql = "select id,third_party_id,type,name,company_name,company_email,telephone,address,town,country,postcode "
					 + "from apm_third_party_details where third_party_id!=1 limit 10";
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				ThirdParty thirdParty = new ThirdParty();
				thirdParty.setId(rs.getInt(1));
				thirdParty.setThirdPartyId(rs.getInt(2));
				thirdParty.setType(rs.getString(3));
				thirdParty.setName(rs.getString(4));
				thirdParty.setCompanyName(rs.getString(5));
				thirdParty.setCompnyEmail(rs.getString(6));
				thirdParty.setCompnyTelephone(rs.getString(7));
				thirdParty.setAddress(rs.getString(8));
				thirdParty.setTown(rs.getString(9));
				thirdParty.setCountry(rs.getString(10));
				thirdParty.setPostcode(rs.getString(11));
				
				list.add(thirdParty);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<ThirdParty> getSearchTPCompanyNameList(String searchTP, int id) {
		PreparedStatement preparedStatement = null;
		ArrayList<ThirdParty> list = new ArrayList<ThirdParty>();
		String sql = "select id,third_party_id,type,name,company_name,company_email,telephone,address,town,country,postcode "
					 + "from apm_third_party_details where company_email like('%"+searchTP+"%') or "
					+ "telephone like('%"+searchTP+"%') or postcode like ('%"+searchTP+"%') or company_name like "
					+ "('%"+searchTP+"%') and third_party_id!=1  order by id desc limit 10";
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				ThirdParty thirdParty = new ThirdParty();
				thirdParty.setId(rs.getInt(1));
				thirdParty.setThirdPartyId(rs.getInt(2));
				thirdParty.setType(rs.getString(3));
				thirdParty.setName(rs.getString(4));
				thirdParty.setCompanyName(rs.getString(5));
				thirdParty.setCompnyEmail(rs.getString(6));
				thirdParty.setCompnyTelephone(rs.getString(7));
				thirdParty.setAddress(rs.getString(8));
				thirdParty.setTown(rs.getString(9));
				thirdParty.setCountry(rs.getString(10));
				thirdParty.setPostcode(rs.getString(11));
				
				list.add(thirdParty);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<ThirdParty> getSearchSurgeryNameList(String searchTP,
			int id) {
		PreparedStatement preparedStatement = null;
		ArrayList<ThirdParty> list = new ArrayList<ThirdParty>();
		String sql = "select id,third_party_id,type,name,company_name,company_email,telephone,address,town,country,postcode "
					 + "from apm_third_party_details where company_email like('%"+searchTP+"%') or "
					+ "telephone like('%"+searchTP+"%') or postcode like ('%"+searchTP+"%') or company_name like "
					+ "('%"+searchTP+"%') and third_party_id = 1  order by id desc limit 10";
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				ThirdParty thirdParty = new ThirdParty();
				thirdParty.setId(rs.getInt(1));
				thirdParty.setThirdPartyId(rs.getInt(2));
				thirdParty.setType(rs.getString(3));
				thirdParty.setName(rs.getString(4));
				thirdParty.setCompanyName(rs.getString(5));
				thirdParty.setCompnyEmail(rs.getString(6));
				thirdParty.setCompnyTelephone(rs.getString(7));
				thirdParty.setAddress(rs.getString(8));
				thirdParty.setTown(rs.getString(9));
				thirdParty.setCountry(rs.getString(10));
				thirdParty.setPostcode(rs.getString(11));
				
				list.add(thirdParty);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	

	public ThirdParty getGPDetailLetter(int tpId) {
		PreparedStatement preparedStatement =  null;
		ThirdParty thirdParty = new ThirdParty();
		String sql = "select id,company_name,company_email from apm_third_party_details where id = "+tpId+" ";
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				
				thirdParty.setId(rs.getInt(1));
				thirdParty.setCompanyName(rs.getString(2));
				thirdParty.setCompnyEmail(rs.getString(3));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return thirdParty;
	}

	public int getThirdPartyId(int patientId) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "select gp_id from apm_patient where id = "+patientId+" ";
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				result = getTPId(rs.getInt(1));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	public int getTPId(int gpId) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "select tpid from apm_gp_details where id = "+gpId+" ";
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				result = rs.getInt(1);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<ThirdParty> getDoctorSurgeryList(String type) {
		PreparedStatement preparedStatement = null;
		ArrayList<ThirdParty>list = new ArrayList<ThirdParty>();
		String sql = "select id,company_name from apm_third_party_details order by id desc";
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				ThirdParty thirdParty = new ThirdParty();
				thirdParty.setId(rs.getInt(1));
				thirdParty.setCompanyName(rs.getString(2));
				list.add(thirdParty);
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	public ArrayList<ThirdParty> getGPList(int r1) {
		PreparedStatement preparedStatement = null;
		ArrayList<ThirdParty>list = new ArrayList<ThirdParty>();
		String sql = "select id,gpname from apm_gp_details where tpid = "+r1+" and tptypeid = 1 order by id desc";
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				ThirdParty thirdParty = new ThirdParty();
				thirdParty.setId(rs.getInt(1));
				thirdParty.setGpname(rs.getString(2));
				list.add(thirdParty);
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	
	public String getCompanyNameForChargeType(String typename) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT company_name FROM apm_third_party_details where id = "+typename+"";
		
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
	
	
	public int getExistingAppointmentTypeid(String typename, int aptype) {
		
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "SELECT id FROM apm_appointment_type where aptype = "+aptype+" and tpid="+typename+" ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			
			if(rs.next()){
				result = rs.getInt(1);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	
	public boolean checkAppointmentTypeExist(String typename, int aptype) {
		PreparedStatement preparedStatement = null;
		boolean result = false;
		String sql = "SELECT * FROM apm_appointment_type where aptype = "+aptype+" and tpid="+typename+" ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			
			if(rs.next()){
				result = true;
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	public int getThirdPartyTempId(int selectedid) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "select third_party_name_id from apm_patient where id = "+selectedid+" ";
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				result = rs.getInt(1);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}


	public int updateAppointmentTypeName(String typeName,
			String initialAppointmentName,String constantaname) {
	
		PreparedStatement preparedStatement = null;
		int result = 0;
		
		String sql ="";
		if(constantaname.equals(Constants.INITIAL_APPOINTMENT)){
			 sql = "update apm_third_party_details set inital_apmt_name=? where id=? ";
		}
		if(constantaname.equals(Constants.FOLLOWUP_APPOINTMENT)){
			 sql = "update apm_third_party_details set followup_apmt_name=? where id=? ";
		}
		if(constantaname.equals(Constants.FINAL_APPOINTMENT)){
			 sql = "update apm_third_party_details set final_apmtname=? where id=? ";
		}
		if(constantaname.equals(Constants.MAINTNANCE_APPOINTMENT)){
			 sql = "update apm_third_party_details set maintnance_apmtname=? where id=? ";
		}
		
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, initialAppointmentName);
			preparedStatement.setString(2, typeName);
			
			result = preparedStatement.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	public int updateDoctorSurgery(String typename, ThirdParty thirdParty) {
		int result = 0;
		
		PreparedStatement pstm = null;
		String sql = "update apm_third_party_details set telephone_line = ?,address = ?,town = ?,country = ?,postcode = ?,"
				+ "company_email = ?,company_name = ?, fax = ?, acct_setting_notes = ?  where id = "+typename+"";
		try {
			pstm = connection.prepareStatement(sql);
			
			pstm.setString(1, thirdParty.getTelephoneLine());
			pstm.setString(2, thirdParty.getAddress());
			pstm.setString(3, thirdParty.getTown());
			pstm.setString(4, thirdParty.getCountry());
			pstm.setString(5, thirdParty.getPostcode());
			pstm.setString(6, thirdParty.getCompnyEmail());
			pstm.setString(7, thirdParty.getCompanyName());
			pstm.setString(8, thirdParty.getFax());
			pstm.setString(9, thirdParty.getTpAccountSettingNotes());

			
			result = pstm.executeUpdate();
			
			
		}catch (Exception e) {
			
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return result;
	}

	public int saveDynamicApmts(int r1, ThirdParty thirdParty) {
		int result = 0;
		
		PreparedStatement pstm = null;
		
		
		String sql = "insert into apm_tp_acct_setting_apmt_type(tpid,dna_charge,apmt_name,apmt_charge,apmt_duration,dna_name) values (?,?,?,?,?,?)";
		try {
			pstm = connection.prepareStatement(sql);
			pstm.setInt(1, r1);
			pstm.setString(2, thirdParty.getDnaCharge());
			pstm.setString(3, thirdParty.getApmtName());
			pstm.setString(4, thirdParty.getApmtCharge());
			pstm.setString(5, thirdParty.getApmtDuaration());
			pstm.setString(6, thirdParty.getDnaName());
			
			result = pstm.executeUpdate();
			
		}catch (Exception e) {
			
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return result;
	}

	public ArrayList<DynamicAppointment> getDynmicApmtList(String id) {
		PreparedStatement preparedStatement = null;
		ArrayList<DynamicAppointment>list = new ArrayList<DynamicAppointment>();
		//String sql = "select id,dna_charge,apmt_name,apmt_charge,apmt_duration,dna_name from apm_tp_acct_setting_apmt_type where tpid = "+id+"";
		
		String sql = "select id,name,duration,charges,dnacharge,dnaoffset from apm_appointment_type where tpid="+id+" and aptype="+Constants.DYNAMIC_APPOINTMENT_TYPE+" ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				
				DynamicAppointment dynamicAppointment = new DynamicAppointment();
				dynamicAppointment.setId(rs.getInt(1));
				dynamicAppointment.setDnaName(rs.getString(2));
				dynamicAppointment.setApmtDuaration(rs.getString(3));
				dynamicAppointment.setApmtCharge(rs.getString(4));
				dynamicAppointment.setDnaCharge(rs.getString(5));
				dynamicAppointment.setOffset(rs.getBoolean(6));
				
				
				/*dynamicAppointment.setDnaCharge(rs.getString(2));
				dynamicAppointment.setApmtName(rs.getString(3));
				dynamicAppointment.setApmtCharge(rs.getString(4));
				dynamicAppointment.setApmtDuaration(rs.getString(5));
				dynamicAppointment.setDnaName(rs.getString(6));*/
				list.add(dynamicAppointment);
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	public int updateDynamicApmts(int r1, ThirdParty thirdParty, int iddnymic) {
		int result = 0;
		
		PreparedStatement pstm = null;
		
		
		String sql = "update apm_tp_acct_setting_apmt_type set dna_charge = ?,apmt_name = ?,apmt_charge = ?,apmt_duration = ?,dna_name = ? where id = "+iddnymic+"";
		try {
			pstm = connection.prepareStatement(sql);
			pstm.setString(1, thirdParty.getDnaCharge());
			pstm.setString(2, thirdParty.getApmtName());
			pstm.setString(3, thirdParty.getApmtCharge());
			pstm.setString(4, thirdParty.getApmtDuaration());
			pstm.setString(5, thirdParty.getDnaName());
			
			
			result = pstm.executeUpdate();
			
		}catch (Exception e) {
			
			e.printStackTrace();
			// TODO: handle exception
		}
		
		return result;
	}

	public ThirdParty getAppointmentChargesData(String id,int aptype) {
		PreparedStatement preparedStatement = null;
		ThirdParty thirdParty = new ThirdParty();
		String sql = "select id,name,duration,charges,dnacharge,dnaoffset from apm_appointment_type where tpid="+id+" and aptype="+aptype+" ";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			
			if(rs.next()){
				thirdParty.setId(rs.getInt(1));
				thirdParty.setDnaName(rs.getString(2));
				thirdParty.setApmtDuaration(rs.getString(3));
				thirdParty.setApmtCharge(rs.getString(4));
				thirdParty.setDnaCharge(rs.getString(5));
				thirdParty.setOffset(rs.getBoolean(6));
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return thirdParty;
	}

	public GP getGPDetails(String id) {
		PreparedStatement preparedStatement = null;
		GP gp = new GP();
		String sql = "SELECT id,gpname FROM apm_gp_details where id = "+id+" ";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				gp.setId(rs.getInt(1));
				gp.setName(rs.getString(2));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return gp;
	}


	public ArrayList<ThirdParty> getThirdPartyDetailsList() {
		PreparedStatement preparedStatement = null;
		ArrayList<ThirdParty>list = new ArrayList<ThirdParty>();
		String sql = "select id,third_party_id,type,name,salutation,title,department,search_name,telephone_line,email,email_cc,notes,company_details,company_name,address,town,country,postcode,telephone,fax,web,reference_no,company_email,warning_msg,account_notes,acc_must_be_in_credit,acc_credit_limit,acc_warning_limit,default_apmt_booking_confim,confm_contact_email,default_apmt_dna,dna_contact_email,dnaLimit,outInvoiceLimit from apm_third_party_details order by company_name";
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				ThirdParty thirdParty = new ThirdParty();
				thirdParty.setId(rs.getInt(1));
				thirdParty.setThirdPartyId(rs.getInt(2));
				thirdParty.setType(rs.getString(3));
				thirdParty.setName(rs.getString(4));
				
				
				thirdParty.setSalutation(rs.getString(5));
				thirdParty.setTitle(rs.getString(6));
				thirdParty.setDepartment(rs.getString(7));
				thirdParty.setSearchName(rs.getString(8));
				thirdParty.setTelephoneLine(rs.getString(9));
				thirdParty.setEmail(rs.getString(10));
				thirdParty.setEmailCc(rs.getString(11));
				thirdParty.setNotes(rs.getString(12));
				thirdParty.setCompanyDetails(rs.getString(13));
				
				thirdParty.setCompanyName(rs.getString(14));
				thirdParty.setAddress(rs.getString(15));
				thirdParty.setTown(rs.getString(16));
				thirdParty.setCountry(rs.getString(17));
				thirdParty.setPostcode(rs.getString(18));
				thirdParty.setCompnyTelephone(rs.getString(19));
				thirdParty.setFax(rs.getString(20));
				thirdParty.setWeb(rs.getString(21));
				thirdParty.setReferenceNo(rs.getString(22));
				thirdParty.setCompnyEmail(rs.getString(23));
				
				thirdParty.setWarningMsg(rs.getString(24));
				thirdParty.setAccountsNotes(rs.getString(25));
				thirdParty.setAccountMustBeInCredit(rs.getBoolean(26));
				thirdParty.setAccountCreditLimit(rs.getString(27));
				thirdParty.setAccountWarningLimit(rs.getString(28));
				thirdParty.setDefaultApmtBookingConfmTemp(rs.getString(29));
				thirdParty.setConfContactEmail(rs.getString(30));
				thirdParty.setDefaultApmtDnaTemp(rs.getString(31));
				thirdParty.setDnaContactEmail(rs.getString(32));
				thirdParty.setDnaLimit(rs.getString(33));
				thirdParty.setOutInvoiceLimit(rs.getString(34));
				
				list.add(thirdParty);
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	public ArrayList<ThirdParty> getMainTPList() {
		
		ArrayList<ThirdParty> list=new ArrayList<ThirdParty>();
		try {
			
			String sql="select id,company_name from apm_third_party_details where maintp=1";
			PreparedStatement ps=connection.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()){
				 
				    ThirdParty party=new ThirdParty();
				    party.setId(rs.getInt(1));
				    party.setCompanyName(rs.getString(2));
				    list.add(party);
			}
			
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<ThirdParty> getSubTpList() {

		ArrayList<ThirdParty> list=new ArrayList<ThirdParty>();
		try {
			
			String sql="SELECT id,company_name from apm_third_party_details where maintp=0";
			PreparedStatement ps=connection.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()){
				 
				    ThirdParty party=new ThirdParty();
				    party.setId(rs.getInt(1));
				    party.setCompanyName(rs.getString(2));
				    list.add(party);
				 
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return list;
	}

	
	
	public ArrayList<ThirdParty> getSelectedSubTPList(String maintp) {

		ArrayList<ThirdParty> list=new ArrayList<ThirdParty>();
		try {
	
			String sql="SELECT id,company_name from apm_third_party_details where followtp="+maintp+"";
			PreparedStatement ps=connection.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();

			while(rs.next()){
				 
				ThirdParty thirdParty=new ThirdParty();
				thirdParty.setId(rs.getInt(1));
				thirdParty.setCompanyName(rs.getString(2));
				list.add(thirdParty);
				
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return list;
	}

	public int updateFollowerTp(int str, int tpid) {

		int result=0;
		try {
			
			String sql="update apm_third_party_details set followtp="+tpid+" where id="+str+" ";
			PreparedStatement ps=connection.prepareStatement(sql);
			
			result=ps.executeUpdate();
		} catch (Exception e) {

			e.printStackTrace(); 
		}
		
		return result;
	}
	

	

	
}
