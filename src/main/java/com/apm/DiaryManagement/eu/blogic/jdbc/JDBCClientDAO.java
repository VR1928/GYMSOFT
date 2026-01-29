package com.apm.DiaryManagement.eu.blogic.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.a.a.a.g.m.p;
import com.apm.DiaryManagement.eu.bi.ClientDAO;
import com.apm.DiaryManagement.eu.bi.CompleteAptmDAO;
import com.apm.DiaryManagement.eu.entity.Client;
import com.apm.DiaryManagement.eu.entity.CompleteAppointment;
import com.apm.DiaryManagement.eu.entity.GpData;
import com.apm.DiaryManagement.eu.entity.NotAvailableSlot;
import com.apm.DiaryManagement.web.action.SmsService;
import com.apm.Ipd.eu.bi.BedDao;
import com.apm.Ipd.eu.blogic.jdbc.JDBCBedDao;
import com.apm.Ipd.eu.entity.Bed;
import com.apm.Log.eu.entity.EmailLetterLog;
import com.apm.Master.eu.entity.Master;
import com.apm.Registration.eu.bi.UserProfileDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCUserProfileDAO;
import com.apm.Registration.eu.entity.UserProfile;
import com.apm.ThirdParties.eu.entity.ThirdParty;
import com.apm.common.eu.blogic.jdbc.JDBCBaseDAO;
import com.apm.common.utils.DateTimeUtils;
import com.apm.common.utils.Pagination;
import com.apm.common.web.common.helper.LoginInfo;

public class JDBCClientDAO extends JDBCBaseDAO implements ClientDAO {
	
	public JDBCClientDAO(Connection connection){
		this.connection = connection;
		
	}

	public ArrayList<Client> getAllPatient(int id) {
		PreparedStatement preparedStatement = null;
		ArrayList<Client>list = new ArrayList<Client>();
		String sql = "select id,title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,refrence,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,whopay,policyauthorzcode,policyno,knownAs,county,homeNo,workNo,emailCc,prefContactMode,emergencyContName,emergencyContNo,patientType,status,note,oldClientId,lastModified,gp_id,imgname,casualtyid,adhno,middlename,mbalance,abrivationid from apm_patient where status=1 order by id desc limit 20";
		
		/*StringBuffer sql = new StringBuffer();
		sql.append("select distinct apm_patient.id,title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,refrence,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,apm_patient.whopay,policyauthorzcode,policyno,knownAs,county,homeNo,workNo,emailCc,prefContactMode,emergencyContName,emergencyContNo,patientType,apm_patient.status,note,oldClientId,lastModified ");
		sql.append("from apm_patient inner join apm_available_slot on  apm_available_slot.clientid = apm_patient.id ");
		sql.append("order by apm_available_slot.id desc limit 10");*/
		
		try{
			
			preparedStatement = connection.prepareStatement(sql.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Client client = new Client();
				client.setId(rs.getInt(1));
				client.setTitle(rs.getString(2));
				client.setFirstName(rs.getString(3));
				client.setLastName(rs.getString(4));
				client.setMobNo(rs.getString(5));
				client.setEmail(rs.getString(6));
				client.setGender(rs.getString(7));
				client.setDob(rs.getString(8));
				client.setAddress(rs.getString(9));
				/*String city = rs.getString(10);
				client.setCity(city);
				client.setTown(getcityfromid(city));*/
				client.setTown(rs.getString(10));
				
				String age =DateTimeUtils.getAge(client.getDob());
				client.setAge(Integer.parseInt(age));
				
				client.setCountry(rs.getString(11));
				client.setPostCode(rs.getString(12));
				client.setReference(rs.getString(13));
				client.setSourceOfIntro(rs.getString(14));
				client.setType(rs.getString(15));
				client.setTypeName(rs.getString(16));
				
				
				String thirdPartyType = getType(rs.getString(15));
				String thirdPartyTypeName = getTypeName(rs.getString(16));
				
				
				
				
				client.setThirdPartyType(thirdPartyType);
				client.setThirdPartyTypeName(thirdPartyTypeName);
				client.setOccupation(rs.getString(17));
				client.setExpiryDate(rs.getString(18));
				client.setWhopay(rs.getString(19));
				client.setPolicyAuthorzCode(rs.getString(20));
				client.setPolicyNo(rs.getString(21));
				client.setKnownAs(rs.getString(22));
				client.setCounty(rs.getString(23));
				client.setHomeNo(rs.getString(24));
				client.setWorkNo(rs.getString(25));
				client.setEmailCc(rs.getString(26));
				client.setPrefContactMode(rs.getString(27));
				client.setEmergencyContName(rs.getString(28));
				client.setEmergencyContNo(rs.getString(29));
				client.setPatientType(rs.getString(30));
				client.setStatus(rs.getBoolean(31));
				client.setNote(rs.getString(32));
				client.setOldclientId(rs.getString(33));
				client.setLastModified(rs.getString(34));
				client.setGpid(rs.getString(35));
				client.setImageName(rs.getString(36));
				client.setCasualtyid(rs.getString(37));
				client.setAdhno(rs.getString(38));
				client.setMiddlename(rs.getString(39));
				client.setBalance(rs.getString(40));
				client.setIspharmacy("0");
				client.setAbrivationid(rs.getString(41));
				list.add(client);
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
		
		
	}

	private String getTypeName(String type) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT name FROM apm_third_party_details where  id = "+type+"";
		
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

	private String getType(String typeName) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT name FROM apm_third_party where  id = "+typeName+"";
		
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

	public int savePatient(Client client,int id) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "insert into apm_patient(title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,whopay,policyauthorzcode,policyno,knownAs,county,homeNo,workNo,emailCc,prefContactMode,emergencyContName,emergencyContNo,patientType,clinicId,status,middlename,refered_date,employer_name,gp_id,treatment_type,policyExcess,lastModified,refrence,doctor_surgery_id,second_line_address,source_of_intro_name,note,accountnote,clinicalnote,nhs,adhno,tpmemb,fullname,abrivationid,hospitalborn,regdate,companyname, neiscardno, designation, relationofuser, unit_station, claimid, colliery, areatp,policyholder,maritalsts,mothername,fathername,birthplace,birthtime,document_name,document_data)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		
		
		try{
			preparedStatement = connection.prepareStatement(sql);
		
			preparedStatement.setString(1, client.getTitle());
			preparedStatement.setString(2, client.getFirstName());
			preparedStatement.setString(3, client.getLastName());
			preparedStatement.setString(4, client.getMobNo());
			preparedStatement.setString(5, client.getEmail());
			preparedStatement.setString(6, client.getGender());
			preparedStatement.setString(7, client.getDob());
			preparedStatement.setString(8, client.getAddress());
			preparedStatement.setString(9, client.getTown());
			preparedStatement.setString(10, client.getCountry());
			preparedStatement.setString(11, client.getPostCode());
			preparedStatement.setString(12, client.getSourceOfIntro());
			preparedStatement.setString(13, client.getType());
			preparedStatement.setString(14, client.getTypeName());
			preparedStatement.setString(15, client.getOccupation());
			preparedStatement.setString(16, client.getExpiryDate());
			preparedStatement.setString(17, client.getWhopay());
			preparedStatement.setString(18, client.getPolicyAuthorzCode());
			preparedStatement.setString(19, client.getPolicyNo());
			preparedStatement.setString(20, client.getKnownAs());
			preparedStatement.setString(21, client.getCounty());
			preparedStatement.setString(22, client.getHomeNo());
			preparedStatement.setString(23, client.getWorkNo());
			preparedStatement.setString(24, client.getEmailCc());
			preparedStatement.setString(25, client.getPrefContactMode());
			preparedStatement.setString(26, client.getEmergencyContName());
			preparedStatement.setString(27, client.getEmergencyContNo());
			preparedStatement.setString(28, client.getPatientType());
			preparedStatement.setInt(29, id);
			preparedStatement.setInt(30, 1);
			preparedStatement.setString(31, client.getMiddlename());
			preparedStatement.setString(32, client.getReferedDate());
			preparedStatement.setString(33, client.getEmployerName());
			preparedStatement.setString(34, client.getGpname());
			preparedStatement.setString(35, client.getTreatmentType());
			preparedStatement.setString(36, client.getPolicyExcess());
			preparedStatement.setString(37, client.getLastModifiedDate());
			preparedStatement.setString(38, client.getReference());
			preparedStatement.setString(39, client.getDoctorsurgery());
			preparedStatement.setString(40, client.getSecondLineaddress());
			preparedStatement.setString(41, client.getSourceOfIntroName());
			preparedStatement.setString(42, client.getClientNote());
			preparedStatement.setString(43, client.getAccountNote());
			preparedStatement.setString(44, client.getClinicalNote());
			preparedStatement.setString(45, client.getNhsNumber());
			//add adhar and third party member number
			preparedStatement.setString(46, client.getAdhno());
			preparedStatement.setString(47, client.getTpmemb());
			String fullname = client.getFirstName()+" "+ client.getLastName();
			preparedStatement.setString(48, fullname);
			preparedStatement.setString(49, client.getAbrivationid());
			preparedStatement.setString(50, client.getHospitalborn());
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar=Calendar.getInstance();
			String date=dateFormat.format(calendar.getTime());
			preparedStatement.setString(51, date);
			//shubham 06/12/2018
			preparedStatement.setString(52, client.getCompname());
			preparedStatement.setString(53, client.getNeisno());
			preparedStatement.setString(54, client.getDesignationbytp());
			preparedStatement.setString(55, client.getRelationvbytpe());
			preparedStatement.setString(56, client.getUnitstation());
			preparedStatement.setString(57, client.getClaimbytp());
			preparedStatement.setString(58, client.getColliery());
			preparedStatement.setString(59, client.getAreabytp());
			preparedStatement.setString(60, client.getPolicyholder());
			preparedStatement.setString(61, client.getMaritalsts());
			preparedStatement.setString(62, client.getMothername());
			preparedStatement.setString(63, client.getFathername());
			preparedStatement.setString(64, client.getBirthplace());
			preparedStatement.setString(65, client.getBirthtime());
			preparedStatement.setString(66, client.getDocumentID());
			preparedStatement.setString(67, client.getDocumentValue());
			result = preparedStatement.executeUpdate();
			if(result == 1){
				ResultSet resultSet = preparedStatement.getGeneratedKeys();
				if(resultSet.next()){
					result = resultSet.getInt(1);  
				}
			}

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Client getPatient(int id) {
		PreparedStatement preparedStatement = null;
		String sql = "select id,title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,refrence,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,whopay,policyauthorzcode,policyno,knownAs,county,homeNo,workNo,emailCc,prefContactMode,emergencyContName,emergencyContNo,patientType,middlename,oldClientId,gp_id,employer_name,treatment_type,refered_date,policyExcess,lastModified,doctor_surgery_id,second_line_address,source_of_intro_name,note,accountnote,clinicalnote,nhs,imgname,relation,adhno,tpmemb,middlename,mbalance,abrivationid,fullname,hospitalborn,companyname, neiscardno, designation, relationofuser, unit_station, claimid, colliery, areatp,maritalsts,policyholder,mothername,fathername,birthplace,birthtime,document_name,document_data from apm_patient where id = "+id+"";
		Client client = new Client();
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				
				client.setId(rs.getInt(1));
				client.setTitle(rs.getString(2));
				client.setFirstName(rs.getString(3));
				client.setLastName(rs.getString(4));
				client.setMobNo(rs.getString(5));
				client.setEmail(rs.getString(6));
				client.setGender(rs.getString(7));
				client.setDob(rs.getString(8));
				client.setAddress(rs.getString(9));
				
				//String city = rs.getString(10);
				//client.setCity(city);
				client.setTown(rs.getString(10));
				
				
				client.setCountry(rs.getString(11));
				client.setPostCode(rs.getString(12));
				client.setReference(rs.getString(13));
				client.setSourceOfIntro(rs.getString(14));
				client.setType(rs.getString(15));
				client.setTypeName(rs.getString(16));
				client.setOccupation(rs.getString(17));
				client.setExpiryDate(rs.getString(18));
				client.setWhopay(rs.getString(19));
				client.setPolicyAuthorzCode(rs.getString(20));
				client.setPolicyNo(rs.getString(21));
				client.setKnownAs(rs.getString(22));
				client.setCounty(rs.getString(23));
				client.setHomeNo(rs.getString(24));
				client.setWorkNo(rs.getString(25));
				client.setEmailCc(rs.getString(26));
				client.setPrefContactMode(rs.getString(27));
				client.setEmergencyContName(rs.getString(28));
				client.setEmergencyContNo(rs.getString(29));
				client.setPatientType(rs.getString(30));
				client.setMiddlename(rs.getString(31));
				client.setOldclientId(rs.getString(32));
				String gpname = rs.getString(33);
				client.setGpname(gpname);
				client.setEmployerName(rs.getString(34));
				client.setTreatmentType(rs.getString(35));
				client.setReferedDate(rs.getString(36));
				client.setPolicyExcess(rs.getString(37));
				client.setLastModified(rs.getString(38));
				client.setGptypeName(rs.getString(39));
				
				if(rs.getString(40)==null){
					client.setSecondLineaddress("");
				}else{
					client.setSecondLineaddress(rs.getString(40));
				}
				
				client.setSourceOfIntroName(rs.getString(41));
				client.setClientNote(rs.getString(42));
				client.setAccountNote(rs.getString(43));
				client.setClinicalNote(rs.getString(44));
				client.setNhsNumber(rs.getString(45));
				client.setImageName(rs.getString(46));
				client.setRelation(rs.getString(47));
				client.setAdhno(rs.getString(48));
				client.setTpmemb(rs.getString(49));
				client.setMiddlename(rs.getString(50));
				client.setBalance(rs.getString(51));
				client.setAbrivationid(rs.getString(52));
				client.setFullname(rs.getString(53));
				client.setHospitalborn(String.valueOf(rs.getInt(54)));
				//shubham 06/12/2018
				client.setCompname(rs.getString(55));
				client.setNeisno(rs.getString(56));
				client.setDesignationbytp(rs.getString(57));
				client.setRelationvbytpe(rs.getString(58));
				client.setUnitstation(rs.getString(59));
				client.setClaimbytp(rs.getString(60));
				client.setColliery(rs.getString(61));
				client.setAreabytp(rs.getString(62));
				client.setMaritalsts(rs.getString(63));
				client.setPolicyholder(rs.getString(64));
				client.setMothername(rs.getString(65));
				client.setFathername(rs.getString(66));
				client.setBirthplace(rs.getString(67));
				client.setBirthtime(rs.getString(68));
				client.setDocumentID(rs.getString(69));
				client.setDocumentValue(rs.getString(70));
				client.setAge1(DateTimeUtils.getAge1(rs.getString(8)));
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return client;
	}

	public int updatePatient(Client client, int id ,LoginInfo loginInfo) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "update apm_patient set title = ?,firstname = ?,surname = ?,mobno = ?,email = ?,gender = ?,dob = ?,address = ?,town = ?,country = ?,postcode = ?,sourceofintro = ?,third_party_id = ?,third_party_name_id = ?,occupation = ?,expiryDate = ?,whopay = ?,policyauthorzcode = ?,policyno = ?,knownAs = ?,county = ?,homeNo = ?,workNo = ?,emailCc = ?,prefContactMode = ?,emergencyContName = ?,emergencyContNo = ?,patientType = ?,middlename = ?, gp_id = ?,employer_name = ?,treatment_type = ?,policyExcess = ?,lastModified = ?,refrence = ?,refered_date = ?,doctor_surgery_id = ?,second_line_address = ?,source_of_intro_name = ?,note=?,accountnote=?,clinicalnote=?, nhs=?, adhno=?, tpmemb=?, fullname=? ,hospitalborn=?,companyname=?, neiscardno=?, designation=?, relationofuser=?, unit_station=?, claimid=?, colliery=?, areatp=?, policyholder=? , maritalsts=?,mothername=?,fathername=?,birthplace=?,birthtime=?,modified_userid=?,document_name=?,document_data=?  where id = "+id+"";
		
		
		
		try{
			preparedStatement = connection.prepareStatement(sql);
		
			preparedStatement.setString(1, client.getTitle());
			preparedStatement.setString(2, client.getFirstName());
			preparedStatement.setString(3, client.getLastName());
			preparedStatement.setString(4, client.getMobNo());
			preparedStatement.setString(5, client.getEmail());
			preparedStatement.setString(6, client.getGender());
			preparedStatement.setString(7, client.getDob());
			preparedStatement.setString(8, client.getAddress());
			preparedStatement.setString(9, client.getTown());
			preparedStatement.setString(10, client.getCountry());
			preparedStatement.setString(11, client.getPostCode());
			preparedStatement.setString(12, client.getSourceOfIntro());
			preparedStatement.setString(13, client.getType());
			preparedStatement.setString(14, client.getTypeName());
			preparedStatement.setString(15, client.getOccupation());
			preparedStatement.setString(16, client.getExpiryDate());
			preparedStatement.setString(17, client.getWhopay());
			preparedStatement.setString(18,client.getPolicyAuthorzCode());
			preparedStatement.setString(19, client.getPolicyNo());
			preparedStatement.setString(20, client.getKnownAs());
			preparedStatement.setString(21, client.getCounty());
			preparedStatement.setString(22, client.getHomeNo());
			preparedStatement.setString(23, client.getWorkNo());
			preparedStatement.setString(24, client.getEmailCc());
			preparedStatement.setString(25, client.getPrefContactMode());
			preparedStatement.setString(26, client.getEmergencyContName());
			preparedStatement.setString(27, client.getEmergencyContNo());
			preparedStatement.setString(28, client.getPatientType());
			preparedStatement.setString(29, client.getMiddlename());
			preparedStatement.setString(30, client.getGpname());
			preparedStatement.setString(31, client.getEmployerName());
			preparedStatement.setString(32, client.getTreatmentType());
			preparedStatement.setString(33, client.getPolicyExcess());
			preparedStatement.setString(34, client.getLastModifiedDate());
			preparedStatement.setString(35, client.getReference());
			preparedStatement.setString(36, client.getReferedDate());
			preparedStatement.setString(37, client.getDoctorsurgery());
			preparedStatement.setString(38, client.getSecondLineaddress());
			preparedStatement.setString(39, client.getSourceOfIntroName());
			preparedStatement.setString(40, client.getClientNote());
			preparedStatement.setString(41, client.getAccountNote());
			preparedStatement.setString(42, client.getClinicalNote());
			preparedStatement.setString(43, client.getNhsNumber());
			preparedStatement.setString(44, client.getAdhno());
			preparedStatement.setString(45, client.getTpmemb());
			String fullname = client.getFirstName()+" "+client.getLastName();
			preparedStatement.setString(46, fullname);
			preparedStatement.setString(47, client.getHospitalborn());
			//shubham 06/12/2018
			preparedStatement.setString(48, client.getCompname());
			preparedStatement.setString(49, client.getNeisno());
			preparedStatement.setString(50, client.getDesignationbytp());
			preparedStatement.setString(51, client.getRelationvbytpe());
			preparedStatement.setString(52, client.getUnitstation());
			preparedStatement.setString(53, client.getClaimbytp());
			preparedStatement.setString(54, client.getColliery());
			preparedStatement.setString(55, client.getAreabytp());
			preparedStatement.setString(56, client.getPolicyholder());
			preparedStatement.setString(57, client.getMaritalsts());
			preparedStatement.setString(58, client.getMothername());
			preparedStatement.setString(59, client.getFathername());
			preparedStatement.setString(60, client.getBirthplace());
			preparedStatement.setString(61, client.getBirthtime());
			preparedStatement.setString(62, loginInfo.getUserId());
			preparedStatement.setString(63, client.getDocumentID());
			preparedStatement.setString(64, client.getDocumentValue());
			result = preparedStatement.executeUpdate();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int deleteClient(int id, Client client) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "delete from apm_patient where id = "+id+" ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			
			result = preparedStatement.executeUpdate();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
	public ArrayList<Client> getThirdPartyType() {
		PreparedStatement preparedStatement = null;
		ArrayList<Client>list = new ArrayList<Client>();
		String sql = "select id,name from apm_third_party";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Client client = new Client();
				client.setId(rs.getInt(1));
				client.setType(rs.getString(2));
				
				list.add(client);
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	
	public ArrayList<Client> getThirdPartyTypeName() {
		PreparedStatement preparedStatement = null;
		ArrayList<Client>list = new ArrayList<Client>();
		String sql = "select id,name,company_name from apm_third_party_details order by company_name";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Client client = new Client();
				client.setId(rs.getInt(1));
				client.setTypeName(rs.getString(2));
				client.setThirdPartyCompanyName(rs.getString(3));
				list.add(client);
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	
	public ArrayList<Client> getThirdTypeNameList(int id) {
		PreparedStatement preparedStatement = null;
		ArrayList<Client>list = new ArrayList<Client>();
		String sql = "select id,name,company_name from apm_third_party_details where third_party_id = '"+id+"' and company_name!='' order by company_name";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Client client = new Client();
				client.setId(rs.getInt(1));
				client.setTypeName(rs.getString(2));
				client.setThirdPartyCompanyName(rs.getString(3));
				list.add(client);
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	
	public int updateThirdPartyDetails(Client client, String id) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "update apm_patient set third_party_id = ?,third_party_name_id = ? where id = "+id+"";
		try{
			preparedStatement = connection.prepareStatement(sql);
		
			preparedStatement.setString(1, client.getType());
			preparedStatement.setString(2, client.getTypeName());
			
			result = preparedStatement.executeUpdate();

		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public ArrayList<Client> getOccupationList() {
		PreparedStatement preparedStatement = null;
		ArrayList<Client>list = new ArrayList<Client>();
		String sql = "select id,occupation from occupation";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				
				Client client = new Client();
				client.setId(rs.getInt(1));
				client.setOccupation(rs.getString(2));
			
				list.add(client);
			}
			int maxId = getMaxIdOfOccupation();
			Client c1 = new Client();
			c1.setId(maxId+1);
			c1.setOccupation("ADD NEW");
			list.add(c1);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public int getMaxIdOfOccupation() {
		PreparedStatement preparedStatement = null;
		int id = 0;
		String sql = "select max(id) from occupation";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				
				id = rs.getInt(1);
			
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	
	
	public ArrayList<Client> getAnesthesiaList() {
		PreparedStatement preparedStatement = null;
		ArrayList<Client>list = new ArrayList<Client>();
		String sql = "select id, name from reference";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				
				Client client = new Client();
				client.setId(rs.getInt(1));
				client.setReference(rs.getString(2));
				
				list.add(client);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<Client> getReferenceList() {
		PreparedStatement preparedStatement = null;
		ArrayList<Client>list = new ArrayList<Client>();
		String sql = "select id, name from reference where isrefered='1'";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				
				Client client = new Client();
				client.setId(rs.getInt(1));
				client.setReference(rs.getString(2));
				
				list.add(client);
			}
			int maxId = getMaxIdOfRefernce();
			Client c1 = new Client();
			c1.setId(maxId+1);
			c1.setReference("ADD NEW");
			list.add(c1);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public int getMaxIdOfRefernce() {
		PreparedStatement preparedStatement = null;
		int id = 0;
		String sql = "select max(id) from reference";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				
				id = rs.getInt(1);
			
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	public int insertOtherOccupation(Client client, String otherOccupation) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "insert into occupation(occupation)values(?)";
		try{
			preparedStatement = connection.prepareStatement(sql);
		
			preparedStatement.setString(1, otherOccupation);
			
			
			result = preparedStatement.executeUpdate();

		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public int insertOtherReference(Client client, String otherReference) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "insert into reference(name,isrefered)values(?,?)";
		try{
			preparedStatement = connection.prepareStatement(sql);
		
			preparedStatement.setString(1, otherReference);
			
			preparedStatement.setString(2, "1");
			result = preparedStatement.executeUpdate();

		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	
	public ArrayList<String> getInitialList() {
		PreparedStatement preparedStatement = null;
		ArrayList<String>list = new ArrayList<String>();
		String sql = "select initial from initial";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				
				
				String initial = rs.getString(1);
			
				list.add(initial);
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	

	public ArrayList<Client> getClient(String searchClient,int id) {
		PreparedStatement preparedStatement = null;
		String temp[] = searchClient.split(" ");
		ArrayList<Client>list = new ArrayList<Client>();
		StringBuffer sql = new StringBuffer();
		sql.append("select id,title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,refrence,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,whopay,policyauthorzcode,policyno,knownAs,county,homeNo,workNo,emailCc,prefContactMode,emergencyContName,emergencyContNo,patientType,status,oldClientId,lastModified,adhno,fullname,abrivationid from apm_patient where status = 1 and (");
		
		if(temp.length==1){
			sql.append(" firstname like('"+temp[0]+"%') or surname like('"+temp[0]+"%') or id like ('%"+temp[0]+"%') or adhno like('"+temp[0]+"%') or fullname like('"+temp[0]+"%') or oldClientId like('"+temp[0]+"%') or mobno like('"+temp[0]+"%') or abrivationid like('%"+temp[0]+"%') ");
			
		}else if(temp.length>1){
			sql.append(" (firstname like('"+temp[0]+"%') and surname like('"+temp[1]+"%')) or fullname like('"+searchClient+"%') or abrivationid like('%"+temp[0]+"%') ");
		} 
		sql.append(") order by lastmodified");
		
		try{
			
			preparedStatement = connection.prepareStatement(sql.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Client client = new Client();
				client.setId(rs.getInt(1));
				client.setTitle(rs.getString(2));
				client.setFirstName(rs.getString(3));
				client.setLastName(rs.getString(4));
				client.setMobNo(rs.getString(5));
				client.setEmail(rs.getString(6));
				client.setGender(rs.getString(7));
				client.setDob(rs.getString(8));
				client.setAddress(rs.getString(9));

//				String city = rs.getString(10);
//				client.setCity(city);
//				client.setTown(getcityfromid(city));
			
				client.setTown(rs.getString(10));
				client.setCountry(rs.getString(11));
				client.setPostCode(rs.getString(12));
				client.setReference(rs.getString(13));
				client.setSourceOfIntro(rs.getString(14));
				client.setType(rs.getString(15));
				client.setTypeName(rs.getString(16));
				
				
				String thirdPartyType = getType(rs.getString(15));
				String thirdPartyTypeName = getTypeName(rs.getString(16));
				
				client.setThirdPartyType(thirdPartyType);
				client.setThirdPartyTypeName(thirdPartyTypeName);
				client.setOccupation(rs.getString(17));
				client.setExpiryDate(rs.getString(18));
				client.setWhopay(rs.getString(19));
				client.setPolicyAuthorzCode(rs.getString(20));
				client.setPolicyNo(rs.getString(21));
				client.setKnownAs(rs.getString(22));
				client.setCounty(rs.getString(23));
				client.setHomeNo(rs.getString(24));
				client.setWorkNo(rs.getString(25));
				client.setEmailCc(rs.getString(26));
				client.setPrefContactMode(rs.getString(27));
				client.setEmergencyContName(rs.getString(28));
				client.setEmergencyContNo(rs.getString(29));
				client.setPatientType(rs.getString(30));
				client.setStatus(rs.getBoolean(31));
				client.setOldclientId(rs.getString(32));
				client.setLastModified(rs.getString(33));
				client.setAdhno(rs.getString(34));
				String fullname = rs.getString(35);
				client.setIspharmacy("0");
				client.setAbrivationid(rs.getString(36));
				list.add(client);
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<Client> getSourceOfIntroList() {
		PreparedStatement preparedStatement = null;
		ArrayList<Client>list = new ArrayList<Client>();
		String sql = "select id,sourceOfIntro from apm_sourceofintro";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				
				Client client = new Client();
				client.setId(rs.getInt(1));
				client.setSourceOfIntro(rs.getString(2));
			
				list.add(client);
			}
			int maxId = getMaxIdOfSourceOfIntro();
			Client c2 = new Client();
			c2.setId(maxId+1);
			c2.setSourceOfIntro("ADD NEW");
			list.add(c2);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public int getMaxIdOfSourceOfIntro() {
		PreparedStatement preparedStatement = null;
		int id = 0;
		String sql = "select max(id) from apm_sourceofintro";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				
				id = rs.getInt(1);
			
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	public ArrayList<Client> getAllPatient(Pagination pagination,int id,boolean isShowAll,String diaryuser) {
		PreparedStatement preparedStatement = null;
		ArrayList<Client>list = new ArrayList<Client>();
		StringBuffer sql = new StringBuffer();
		if(pagination.sortColumn==null){
			
			//sql = "select id,title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,refrence,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,whopay,policyauthorzcode,policyno,status,note,oldClientId,lastModified,treatment_type from apm_patient order by id desc";
			if(diaryuser.equals("") && isShowAll){
				sql.append("select id,title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,refrence,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,whopay,policyauthorzcode,policyno,status,note,oldClientId,lastModified,treatment_type,relation,adhno,middlename,abrivationid,isautocharge from apm_patient order by lastModified desc ");
			}else if(!isShowAll){
				if(diaryuser.equals("")){
					sql.append("select distinct apm_patient.id,title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,refrence,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,apm_patient.whopay,policyauthorzcode,policyno,apm_patient.status,note,oldClientId,lastModified,treatment_type,relation,,adhno,middlename,abrivationid,isautocharge from apm_patient ");
					sql.append("inner join apm_available_slot on apm_patient.id = apm_available_slot.clientid ");
					sql.append("order by apm_available_slot.id desc ");
				}else{
					sql.append("select distinct apm_patient.id,title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,refrence,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,apm_patient.whopay,policyauthorzcode,policyno,apm_patient.status,note,oldClientId,lastModified,treatment_type,relation,,adhno,middlename,abrivationid,isautocharge from apm_patient ");
					sql.append("inner join apm_available_slot on apm_patient.id = apm_available_slot.clientid and diaryuserid = "+diaryuser+" ");
					sql.append("order by apm_available_slot.id desc ");
				}
				
			}
		}else{
			//sql = "select id,title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,refrence,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,whopay,policyauthorzcode,policyno,status,note,oldClientId,lastModified,treatment_type from apm_patient ";
			if(diaryuser.equals("") && isShowAll){
				sql.append("select id,title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,refrence,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,whopay,policyauthorzcode,policyno,status,note,oldClientId,lastModified,treatment_type,relation,adhno,middlename,abrivationid,isautocharge from apm_patient ");
			}else if(!isShowAll){
				if(diaryuser.equals("")){
					sql.append("select distinct apm_patient.id,title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,refrence,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,apm_patient.whopay,policyauthorzcode,policyno,apm_patient.status,note,oldClientId,lastModified,treatment_type,relation,adhno,middlename,abrivationid,isautocharge from apm_patient ");
					sql.append("inner join apm_available_slot on apm_patient.id = apm_available_slot.clientid ");
					sql.append("order by apm_available_slot.id desc ");
				}else{
					sql.append("select distinct apm_patient.id,title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,refrence,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,apm_patient.whopay,policyauthorzcode,policyno,apm_patient.status,note,oldClientId,lastModified,treatment_type,relation,adhno,middlename,abrivationid,isautocharge from apm_patient ");
					sql.append("inner join apm_available_slot on apm_patient.id = apm_available_slot.clientid and diaryuserid = "+diaryuser+" ");
					sql.append("order by apm_available_slot.id desc ");
				}
			}
			
		}
		//String sql = "select id,title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,refrence,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,whopay,policyauthorzcode,policyno,status,note,oldClientId,lastModified from apm_patient order by surname";
		
		
		String query = pagination.getSQLQuery(sql.toString());
		try{
			
			preparedStatement = connection.prepareStatement(query);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Client client = new Client();
				client.setId(rs.getInt(1));
				client.setTitle(rs.getString(2));
				client.setFirstName(rs.getString(3));
				client.setLastName(rs.getString(4));
				client.setMobNo(rs.getString(5));
				client.setEmail(rs.getString(6));
				client.setGender(rs.getString(7));
				client.setDob(rs.getString(8));
				client.setAddress(rs.getString(9));
				
				/*String city = rs.getString(10);
				client.setCity(city);
				client.setTown(getcityfromid(city));*/
				
				client.setTown(rs.getString(10));
				
				client.setCountry(rs.getString(11));
				client.setPostCode(rs.getString(12));
				client.setReference(rs.getString(13));
				client.setSourceOfIntro(rs.getString(14));
				client.setType(rs.getString(15));
				client.setTypeName(rs.getString(16));
				
				try {
					String age= DateTimeUtils.getAge(client.getDob());
					client.setAge(Integer.parseInt(age));
					String age1= DateTimeUtils.getAge1(client.getDob());
					client.setAge1(age1);
				} catch (Exception e) {
					 e.printStackTrace();
				}
				
				
				String thirdPartyType = getType(rs.getString(15));
				String thirdPartyTypeName = getTypeName(rs.getString(16));
				
				client.setThirdPartyType(thirdPartyType);
				client.setThirdPartyTypeName(thirdPartyTypeName);
				client.setOccupation(rs.getString(17));
				client.setExpiryDate(rs.getString(18));
				client.setWhopay(rs.getString(19));
				client.setPolicyAuthorzCode(rs.getString(20));
				client.setPolicyNo(rs.getString(21));
				client.setStatus(rs.getBoolean(22));
				
				if(rs.getString(23)!=null){
					String str = rs.getString(23);
					
					boolean ch = str.contains("\n");
					if(ch==true){
						str = str.replace("\n", "").replace("\r", "");
					}
					client.setClientNote(str);
					
					if(str!=null){
						if(str.length()>40){
							str = str.substring(0,40);
						}
						client.setNote(str);
					}
				}else{
					client.setNote("");
					client.setClientNote("");
				}
				
				
				
				client.setOldclientId(rs.getString(24));
				client.setLastModified(DateTimeUtils.getIndianDateTimeFormat(rs.getString(25)));
				client.setTreatmentType(rs.getString(26));
				client.setRelation(rs.getString(27));	
				client.setAdhno(rs.getString(28));
				client.setMiddlename(rs.getString(29));
				client.setAbrivationid(rs.getString(30));
				client.setIsautocharge(rs.getInt(31));
				CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(connection);
				CompleteAppointment completeAppointment =  completeAptmDAO.getInsuranceCompanyName(rs.getString(1));
				String tpName = completeAppointment.getInsuranceCompanyName();
				client.setThirdPartyCompanyName(tpName);
				
				ArrayList<Bed>ipdList = getPatientIpdList(client.getId());
				client.setIpdList(ipdList);
				
				list.add(client);
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
		
	}

	private ArrayList<Bed> getPatientIpdList(int clientid) {
		PreparedStatement preparedStatement = null;
		BedDao bedDao=new JDBCBedDao(connection);
		ArrayList<Bed>list = new ArrayList<Bed>();
		String sql = "SELECT id,admissiondsate,cancel,cancelnote,canceluser,ipdseqno,treatmentepisodeid FROM ipd_addmission_form where clientid = "+clientid+" order by id desc";
		
		try{
				
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Bed bed = new Bed();
				bed.setAddmissionid(rs.getString(1));
				//Akash 27/07/2019 commented because its use un-wantedly
				//Bed bedmaster = bedDao.getEditIpdData(bed.getAddmissionid());
				if(rs.getString(2)!=null){
					bed.setAdmissiondate(DateTimeUtils.getDBDate(rs.getString(2)));
				}else{
					bed.setAdmissiondate("");
				}
				
				int cancel= rs.getInt(3);
				bed.setCancel(cancel);
				if(cancel>0){
					
					bed.setCancelnote(rs.getString(4));
					bed.setCancelUser(rs.getString(5));
				} 
				bed.setIpdseqno(rs.getString(6));
				//Akash 27/07/2019 commented because its use un-wantedly
				//String dischargeDate = getIpdDischargeDate(bed.getAddmissionid());     
				String dischargeDate = getIpdDischargeDateNew(rs.getString(7));  
				bed.setDischargeDate(dischargeDate);
				if(dischargeDate.equals("")){
					bed.setTreatmentepisodeid("");
				}else {
				   bed.setTreatmentepisodeid(rs.getString(7));
				}
				bed.setClientid(String.valueOf(clientid));
				list.add(bed);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	private String getIpdDischargeDateNew(String treatmentid) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT dischargedate FROM apm_treatment_episode where id = '"+treatmentid+"' ";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				if(rs.getString(1)!=null){
					result = DateTimeUtils.getDBDate(rs.getString(1));
				}else{
					result = "";
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return result;
	}

	public String getIpdDischargeDate(String addmissionid) {
		PreparedStatement preparedStatement = null;
		String result = "";
		
		BedDao bedDao=new JDBCBedDao(connection);
		Bed bed = bedDao.getEditIpdData(addmissionid);
		
		String sql = "SELECT dischargedate FROM apm_treatment_episode where id = "+bed.getTreatmentepisodeid()+" ";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				if(rs.getString(1)!=null){
					result = DateTimeUtils.getDBDate(rs.getString(1));
				}else{
					result = "";
				}
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return result;
	}

	public int getTotalClientCount(int id,boolean isShowAll,String diaryuser) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		
		StringBuffer sql = new StringBuffer(); 
		
		if(isShowAll){
			 sql.append("select count(*) from apm_patient ");
		}else if(!isShowAll){
			if(diaryuser.equals("")){
				sql.append("select  count(distinct apm_available_slot.clientid) from apm_available_slot ");
				
			}else{
				sql.append("select  count(distinct apm_available_slot.clientid) from apm_available_slot where diaryuserid ");
				
			}
		}
		
		
		try{
			preparedStatement = connection.prepareStatement(sql.toString());
			
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				result = rs.getInt(1);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int savePatientDetails(Client client,int id) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "insert into apm_patient(title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,clinicId,third_party_id,third_party_name_id,expiryDate,whopay,policyno,middlename,status,gp_id,treatment_type,policyExcess,lastModified,refrence,second_line_address,doctor_surgery_id,county,occupation,adhno,fullname,emergencyContName,emergencyContNo,abrivationid,hospitalborn,regdate,companyname, neiscardno, designation, relationofuser, unit_station, claimid, colliery, areatp, policyholder,maritalsts,mothername,fathername,birthplace,birthtime)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try{
			preparedStatement = connection.prepareStatement(sql);
		
			preparedStatement.setString(1, client.getTitle());
			preparedStatement.setString(2, client.getFirstName());
			preparedStatement.setString(3, client.getLastName());
			preparedStatement.setString(4, client.getMobNo());
			preparedStatement.setString(5, client.getEmail());
			preparedStatement.setString(6, client.getGender());
			preparedStatement.setString(7, client.getDob());
			preparedStatement.setString(8, client.getAddress());
			preparedStatement.setString(9, client.getTown());
			preparedStatement.setString(10, client.getCountry());
			preparedStatement.setString(11, client.getPostCode());
			
			//preparedStatement.setString(12, client.getCounty());
			
			preparedStatement.setInt(12, id);
			preparedStatement.setString(13, client.getThirdPartyType());
			preparedStatement.setString(14, client.getThirdPartyCompanyName());
			preparedStatement.setString(15, client.getExpiryDate());
			preparedStatement.setString(16, client.getWhopay());
			preparedStatement.setString(17, client.getPolicyNo());
			preparedStatement.setString(18, client.getMiddlename());
/*			preparedStatement.setString(19, client.getReference());
*/			preparedStatement.setInt(19, 1);
			preparedStatement.setString(20, client.getGpname());
			preparedStatement.setString(21, client.getTreatmentType());
			preparedStatement.setString(22, client.getPolicyExcess());
			preparedStatement.setTimestamp(23, DateTimeUtils.getCurrentDateInSQLCasting());
			preparedStatement.setString(24, client.getReference());
			preparedStatement.setString(25, client.getSecondLineaddress());
			preparedStatement.setString(26, client.getDoctorsurgery());
			preparedStatement.setString(27, client.getState());
			preparedStatement.setString(28, client.getOccupation());
			preparedStatement.setString(29, client.getAdhno());
			String fullname = client.getFirstName() +" "+ client.getLastName();
			preparedStatement.setString(30, fullname);
			preparedStatement.setString(31, client.getRelativename());
			preparedStatement.setString(32, client.getRelativeno());
			preparedStatement.setString(33, client.getAbrivationid());
			preparedStatement.setString(34, client.getHospitalborn());
			
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar=Calendar.getInstance();
			String date=dateFormat.format(calendar.getTime());
			preparedStatement.setString(35, date);
			//shubham 
			preparedStatement.setString(36, client.getCompname());
			preparedStatement.setString(37, client.getNeisno());
			preparedStatement.setString(38, client.getDesignationbytp());
			preparedStatement.setString(39, client.getRelationvbytpe());
			preparedStatement.setString(40, client.getUnitstation());
			preparedStatement.setString(41, client.getClaimbytp());
			preparedStatement.setString(42, client.getColliery());
			preparedStatement.setString(43, client.getAreabytp());
			preparedStatement.setString(44, client.getPolicyholder());
			preparedStatement.setString(45, client.getMaritalsts());
			preparedStatement.setString(46, client.getMothername());
			preparedStatement.setString(47, client.getFathername());
			preparedStatement.setString(48, client.getBirthplace());
			preparedStatement.setString(49, client.getBirthtime());
			result = preparedStatement.executeUpdate();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<Client> getOccupaList() {
		PreparedStatement preparedStatement = null;
		ArrayList<Client>list = new ArrayList<Client>();
		String sql = "select occupation from occupation";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Client client = new Client();
				 client.setOccupation(rs.getString(1));
				 list.add(client);
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<Client> getReferList() {
		PreparedStatement preparedStatement = null;
		ArrayList<Client>list = new ArrayList<Client>();
		String sql = "select name from reference";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Client client = new Client();
				
				client.setReference(rs.getString(1));
			
				list.add(client);
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<Client> getInitailList() {
		PreparedStatement preparedStatement = null;
		ArrayList<Client>list = new ArrayList<Client>();
		String sql = "select initial from initial";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Client client = new Client();
				client.setTitle(rs.getString(1));
				list.add(client);
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public int updateThirdParty(Client client, String clientId, int id) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "update apm_patient set third_party_id = ?,third_party_name_id = ?,expiryDate = ?,policyno = ?,whopay = ?,policyExcess = ? where id = "+clientId+" ";
		try{
			preparedStatement = connection.prepareStatement(sql);
			
			preparedStatement.setString(1, client.getThirdPartyType());
			preparedStatement.setString(2, client.getThirdPartyCompanyName());
			preparedStatement.setString(3, client.getExpiryDate());
			preparedStatement.setString(4, client.getPolicyNo());
			preparedStatement.setString(5, "Third Party");
			preparedStatement.setString(6, client.getPolicyExcess());
			result = preparedStatement.executeUpdate();

		}
		catch (Exception e) {
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

	public String getClientFullName(String id) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT title,firstname,middlename,surname FROM apm_patient where  id = "+id+"";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				result = rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int getTotalClientCountOfSearch(int id, String searchClient,String status,boolean isShowAll,String diaryuser) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String temp[] = searchClient.split(" ");
		StringBuffer sql = new StringBuffer();
		 //sql.append("select count(*) from apm_patient where status like('%"+status+"%') and (mobno like('%"+searchClient+"%') or postcode like('%"+searchClient+"%') or dob like('%"+searchClient+"%') or");
		if(!searchClient.equals("") && status.equals("") && diaryuser.equals("0")){
			sql.append("select count(*) from apm_patient where status like('%"+status+"%') and (mobno like('%"+searchClient+"%') or postcode like('%"+searchClient+"%') or dob like('%"+searchClient+"%') or apm_patient.id='"+searchClient+"' or adhno like('"+searchClient+"%') or ");
		}else{
			 if(isShowAll){
				 sql.append("select count(*) from apm_patient where status like('%"+status+"%') and (mobno like('%"+searchClient+"%') or postcode like('%"+searchClient+"%') or dob like('%"+searchClient+"%') or apm_patient.id='"+searchClient+"' or adhno like('"+searchClient+"%') or fullname like('"+searchClient+"%') or ");
			}else if(!isShowAll){
				
					 sql.append("select count(distinct apm_available_slot.clientid) from apm_patient ");
					 sql.append("inner join apm_available_slot on apm_patient.id = apm_available_slot.clientid ");
					 sql.append("where apm_patient.status like('%"+status+"%') and (mobno like('%"+searchClient+"%') or postcode like('%"+searchClient+"%') or dob like('%"+searchClient+"%') or oldClientId like('%"+searchClient+"%') or apm_patient.id='"+searchClient+"' or adhno like('"+searchClient+"%')  or fullname like('"+searchClient+"%') or ");
					
				
			}
		}
		
		sql.append("  abrivationid like('"+searchClient+"%') or ");
		 if(temp.length>=2){
			 sql.append("fullname like '"+searchClient+"%') ");
		 }else{
		for(int i=0;i<temp.length;i++){
			
			
			sql.append(" firstname like('"+temp[i]+"%') or ");
			if(i==(temp.length-1)){
			sql.append("surname like('"+temp[i]+"%'))");
			}
			else{
				sql.append("surname like('"+temp[i]+"%') or ");
			}
			
		}
		 }
	
		if(!isShowAll){
			if(!diaryuser.equals("0") && !diaryuser.equals("")){
				sql.append("and diaryuserid = "+diaryuser+" ");
			}
		}
		
		
		try{
			preparedStatement = connection.prepareStatement(sql.toString());
			
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				result = rs.getInt(1);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<Client> getClientofSearch(Pagination pagination,
			String searchClient, int id,String status,boolean isShowAll,String diaryuser) {
		PreparedStatement preparedStatement = null;
		String temp[] = searchClient.split(" ");
		ArrayList<Client>list = new ArrayList<Client>();
		StringBuffer sql = new StringBuffer();
		
		if(!searchClient.equals("") && status.equals("") && diaryuser.equals("0")){
			sql.append("select apm_patient.id,title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,refrence,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,whopay,policyauthorzcode,policyno,knownAs,county,homeNo,workNo,emailCc,prefContactMode,emergencyContName,emergencyContNo,patientType,status,note,oldClientId,lastModified,adhno,fullname,middlename,abrivationid,isautocharge from apm_patient where status like('%"+status+"%') and (mobno like('%"+searchClient+"%') or postcode like('%"+searchClient+"%') or dob like('%"+searchClient+"%') or oldClientId like('%"+searchClient+"%') or apm_patient.id='"+searchClient+"' or adhno like('"+searchClient+"%') or ");
		}else{
			if(isShowAll){
				sql.append("select apm_patient.id,title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,refrence,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,whopay,policyauthorzcode,policyno,knownAs,county,homeNo,workNo,emailCc,prefContactMode,emergencyContName,emergencyContNo,patientType,status,note,oldClientId,lastModified,adhno,fullname,middlename,abrivationid,isautocharge from apm_patient where status like('%"+status+"%') and (mobno like('%"+searchClient+"%') or postcode like('%"+searchClient+"%') or dob like('%"+searchClient+"%') or oldClientId like('%"+searchClient+"%') or apm_patient.id='"+searchClient+"' or adhno like('"+searchClient+"%') or fullname like('"+searchClient+"%') or");
			}else if(!isShowAll){
				
					sql.append("select distinct apm_patient.id,title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,refrence,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,apm_patient.whopay,policyauthorzcode,policyno,knownAs,county,homeNo,workNo,emailCc,prefContactMode,emergencyContName,emergencyContNo,patientType,apm_patient.status,note,oldClientId,lastModified,adhno,fullname,middlename,abrivationid,isautocharge from apm_patient ");
					sql.append("inner join apm_available_slot on apm_patient.id = apm_available_slot.clientid ");
					sql.append("where apm_patient.status like('%"+status+"%') and (mobno like('%"+searchClient+"%') or postcode like('%"+searchClient+"%') or dob like('%"+searchClient+"%') or oldClientId like('%"+searchClient+"%') or id='"+searchClient+"' or adhno like('"+searchClient+"%')  or fullname like('"+searchClient+"%') or");
				
			}
		}
		
	
		 sql.append("  abrivationid like('"+searchClient+"%') or ");
		 if(temp.length>=2){
			 sql.append("fullname like '"+searchClient+"%') ");
		 }else{
		for(int i=0;i<temp.length;i++){
			
			
			sql.append(" firstname like('"+temp[i]+"%') or ");
			
			if(i==(temp.length-1)){
			sql.append("surname like('"+temp[i]+"%')) ");
			}
			else{
				sql.append("surname like('"+temp[i]+"%') or ");
			}
			
			
			
		}
		 }
		
		if(!searchClient.equals("") && status.equals("") && diaryuser.equals("0")){
			sql.append(" order by firstname");
		}else{
			if(!isShowAll){
				if(!diaryuser.equals("0") && !diaryuser.equals("")){
					sql.append("and diaryuserid = "+diaryuser+" order by apm_available_slot.id desc ");
				}else{
					sql.append("order by firstname ");
				}
			}else{
				sql.append(" order by firstname");
			}
		}
		
		
		String sqlnew="";
		if(!DateTimeUtils.isNull(pagination.getManualIpdId()).equals("")){
			sqlnew="select apm_patient.id,title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,refrence,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,whopay,policyauthorzcode,policyno,knownAs,county,homeNo,workNo,emailCc,prefContactMode,emergencyContName,emergencyContNo,patientType,status,note,oldClientId,lastModified,adhno,fullname,middlename,abrivationid,isautocharge from apm_patient where id='"+searchClient+"'";
		}
		
		
		String sql1 = sql.toString();
		sql1 = pagination.getSQLQuery(sql.toString());
		if(!sqlnew.equals("")){
			sql1=pagination.getSQLQuery(sqlnew);
		}
		try{
			
			preparedStatement = connection.prepareStatement(sql1);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Client client = new Client();
				client.setId(rs.getInt(1));
				client.setTitle(rs.getString(2));
				client.setFirstName(rs.getString(3));
				client.setLastName(rs.getString(4));
				client.setMobNo(rs.getString(5));
				client.setEmail(rs.getString(6));
				client.setGender(rs.getString(7));
				client.setDob(rs.getString(8));
				client.setAddress(rs.getString(9));
				try {
					String age=DateTimeUtils.getAge(client.getDob());
					client.setAge(Integer.parseInt(age));
					client.setAge1(DateTimeUtils.getAge1(client.getDob()));
				} catch (Exception e) {
				
				}
				
//				String city = rs.getString(10);
//				client.setCity(city);
//				client.setTown(getcityfromid(city));
				
				client.setTown(rs.getString(10));
				
				client.setCountry(rs.getString(11));
				client.setPostCode(rs.getString(12));
				client.setReference(rs.getString(13));
				client.setSourceOfIntro(rs.getString(14));
				client.setType(rs.getString(15));
				client.setTypeName(rs.getString(16));
				
				
				String thirdPartyType = getType(rs.getString(15));
				String thirdPartyTypeName = getTypeName(rs.getString(16));
				
				client.setThirdPartyType(thirdPartyType);
				client.setThirdPartyTypeName(thirdPartyTypeName);
				client.setOccupation(rs.getString(17));
				client.setExpiryDate(rs.getString(18));
				client.setWhopay(rs.getString(19));
				client.setPolicyAuthorzCode(rs.getString(20));
				client.setPolicyNo(rs.getString(21));
				client.setKnownAs(rs.getString(22));
				client.setCounty(rs.getString(23));
				client.setHomeNo(rs.getString(24));
				client.setWorkNo(rs.getString(25));
				client.setEmailCc(rs.getString(26));
				client.setPrefContactMode(rs.getString(27));
				client.setEmergencyContName(rs.getString(28));
				client.setEmergencyContNo(rs.getString(29));
				client.setPatientType(rs.getString(30));
				client.setStatus(rs.getBoolean(31));
				
				if(rs.getString(32)!=null){
					String str = rs.getString(32);
					boolean ch = str.contains("\n");
					if(ch==true){
						str = str.replace("\n", "").replace("\r", "");
					}
					client.setClientNote(str);
					if(str!=null){
						if(str.length()>40){
							str = str.substring(0,40);
						}
						client.setNote(str);
					}
				}else{
					client.setClientNote("");
					client.setNote("");
				}
				
				
				client.setOldclientId(rs.getString(33));
				client.setLastModified(rs.getString(34));
				client.setAdhno(rs.getString(35));
				String fullname = rs.getString(36);
				client.setMiddlename(rs.getString(37));
				client.setAbrivationid(rs.getString(38));
				client.setIsautocharge(rs.getInt(39));
				CompleteAptmDAO completeAptmDAO = new JDBCCompleteAptmDAO(connection);
				CompleteAppointment completeAppointment =  completeAptmDAO.getInsuranceCompanyName(rs.getString(1));
				String tpName = completeAppointment.getInsuranceCompanyName();
				client.setThirdPartyCompanyName(tpName);
				
				ArrayList<Bed>ipdList = getPatientIpdList(client.getId());
				client.setIpdList(ipdList);
				
				list.add(client);
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public int changeStatusToActive(String id) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "update apm_patient set status = 1 where id = "+id+"";
		try{
			preparedStatement = connection.prepareStatement(sql);
			result = preparedStatement.executeUpdate();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int changeStatusToInactive(String id) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "update apm_patient set status = 0 where id = "+id+"";
		try{
			preparedStatement = connection.prepareStatement(sql);
			result = preparedStatement.executeUpdate();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Client getClientDetails(String clientId) {
		PreparedStatement preparedStatement = null;
		String sql = "select * from apm_patient where id = "+clientId;
		Client client = new Client();
		try{
			
			int t = 0;
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			
			while(rs.next()){
			
				
				client.setId(rs.getInt(1));
				client.setTitle(rs.getString(2));
				client.setFirstName(rs.getString(3));
				client.setLastName(rs.getString(4));
				client.setMobNo(rs.getString(5));
				client.setEmail(rs.getString(6));
				client.setGender(rs.getString(7));
				client.setDob(rs.getString(8));
				client.setAddress(rs.getString(9));
				client.setMiddlename(rs.getString("middlename"));				
				/*String city = rs.getString(10);
				client.setCity(city);
				client.setTown(getcityfromid(city));*/
				
				client.setTown(rs.getString(10));
				client.setCountry(rs.getString(11));
				client.setPostCode(rs.getString(12));
				String reference = getReferenceName(rs.getString(13));
				client.setReference(reference);
				String sourceOfIntro = getSourceOfIntro(rs.getString(14));
				client.setSourceOfIntro(sourceOfIntro);
				client.setType(rs.getString(15));
				client.setTypeName(rs.getString(16));
				String occupation = getOccupation(rs.getString(17));
				client.setOccupation(occupation);
				client.setExpiryDate(rs.getString(18));
				client.setWhopay(rs.getString(19));
				client.setPolicyAuthorzCode(rs.getString(20));
				client.setPolicyNo(rs.getString(21));
				client.setKnownAs(rs.getString(22));
				client.setCounty(rs.getString(23));
				client.setHomeNo(rs.getString(24));
				client.setWorkNo(rs.getString(25));
				client.setEmailCc(rs.getString(26));
				client.setPrefContactMode(rs.getString(27));
				client.setEmergencyContName(rs.getString(28));
				client.setEmergencyContNo(rs.getString(29));
				client.setPatientType(rs.getString(30));
				client.setMiddlename(rs.getString("middlename"));
				client.setNote(rs.getString(33));
				String gpname = getGPname(rs.getString(36));
				client.setGpid(rs.getString(36));
				client.setEmployerName(rs.getString(37));
				client.setConditionid(rs.getString(38));
				String treatmentType = getTreatmentType(rs.getString(38));
				client.setTreatmentType(treatmentType);
				client.setReferedDate(rs.getString(39));
				client.setPolicyExcess(rs.getString(40));
				if(rs.getString(43)!=null){
					client.setSecondLineaddress(rs.getString(43));
				}
				client.setNhsNumber(rs.getString(48));
				
				client.setImageName(rs.getString(50));
				client.setRelation(rs.getString(51));
				client.setCasualtyid(rs.getString(52));
				
				if(rs.getString(58)!=null){
					client.setAbrivationid(rs.getString(58));
					client.setPatientIdAbrivation(rs.getString(58)+"/"+rs.getString(1));
			
				}else{
					client.setAbrivationid(rs.getString(1));
					client.setPatientIdAbrivation(rs.getString(1));
				}
				
				/*if(gpname.equals("")||gpname.equals(null)){
					client.setGpname("Request to Client");
				}
				else{
				client.setGpname(gpname);
				}
				*/
//				
//				int age=Integer.parseInt(DateTimeUtils.getAge(client.getDob()));
//				client.setAge(age);
//				String age1="";
//				 age1=DateTimeUtils.getAge1(client.getDob());
//				 client.setAge1(age1);
				
				ThirdParty gp = getGPDetails(rs.getString(36));
				client.setGpDetails(gp);
				
				ThirdParty tp = getThirdPartyDetails(rs.getString(16));
				client.setTpDetails(tp);
				client.setBalance(rs.getString("mbalance"));
				client.setFullname(rs.getString("fullname"));
				client.setNewtpid(rs.getInt("third_party_id"));
				client.setMothername(DateTimeUtils.isNull(rs.getString("mothername")));
				client.setFathername(DateTimeUtils.isNull(rs.getString("fathername")));
				client.setBirthplace(DateTimeUtils.isNull(rs.getString("birthplace")));
				client.setBirthtime(rs.getString("birthtime"));
				
			}
		}catch (Exception e) {
		
			e.printStackTrace();
		}
		
		return client;

	}

	private ThirdParty getThirdPartyDetails(String tpid) {
		
		PreparedStatement preparedStatement = null;
		ThirdParty thirdParty = new ThirdParty();
		String sql = "SELECT name,company_name,address,postcode,town,county,shortname from apm_third_party_details where id = "+tpid+" ";
		
		try{
			preparedStatement = connection.prepareStatement(sql.toString());
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				thirdParty.setThirdPartyName(rs.getString(1));
				thirdParty.setCompanyName(rs.getString(2));
				thirdParty.setAddress(rs.getString(3));
				thirdParty.setPostcode(rs.getString(4));
				thirdParty.setTown(rs.getString(5));
				//String city = rs.getString(5);
				//client.setCity(city);
				//thirdParty.setTown(getcityfromid(city));
				
				thirdParty.setCounty(rs.getString(6));
				thirdParty.setShortname(rs.getString(7));

			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return thirdParty;
	}

	private ThirdParty getGPDetails(String gpid) {
		PreparedStatement preparedStatement = null;
		ThirdParty thirdParty = new ThirdParty();
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT gpname,company_name,address,postcode,town,county FROM apm_gp_details  inner join apm_third_party_details on ");
		sql.append("apm_third_party_details.id =  apm_gp_details.tpid where apm_gp_details.id="+gpid+" ");
		
		
		try{
			preparedStatement = connection.prepareStatement(sql.toString());
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				thirdParty.setGpname(rs.getString(1));
				thirdParty.setCompanyName(rs.getString(2));
				thirdParty.setAddress(rs.getString(3));
				thirdParty.setPostcode(rs.getString(4));
				thirdParty.setTown(rs.getString(5));
				//String city = rs.getString(5);
				//client.setCity(city);
				//thirdParty.setTown(getcityfromid(city));
			
				thirdParty.setCounty(rs.getString(6));
				
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return thirdParty;
	}

	private String getOccupation(String id) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT occupation FROM occupation where  id = '"+id+"' ";
		
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
	
	public String  getcityfromid(String id){
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT city FROM apm_city where  id = "+id+" ";
		
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
	
	public String  getstatefromid(String id){
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT name FROM apm_state where  id = '"+id+"' ";
		
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
	

	private String getTreatmentType(String id) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT name FROM apm_condition where  id = '"+id+"' ";
		
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

	public String getReferenceName(String id) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT name FROM reference where  id = '"+id+"' ";
		
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

	public String getGPname(String id) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT gpname FROM apm_gp_details where  id = '"+id+"' ";
		
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

	public ArrayList<Client> getAllClientOfThirdParty(String tpid) {
		PreparedStatement preparedStatement = null;
		ArrayList<Client>list = new ArrayList<Client>();
		
		String sql = "select id,title,firstname,surname,mobno,email,third_party_name_id from apm_patient where third_party_name_id = '"+tpid+"' order by lastModified";
		
		
		
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Client client = new Client();
				client.setId(rs.getInt(1));
				client.setTitle(rs.getString(2));
				client.setFirstName(rs.getString(3));
				client.setLastName(rs.getString(4));
				client.setMobNo(rs.getString(5));
				client.setEmail(rs.getString(6));
				client.setThirdPartyTypeName(rs.getString(7));
				boolean chkInvoiceIstheir = checkInvoiceIsPendingOrNot(rs.getString(1));
				 if(chkInvoiceIstheir == true){
					 list.add(client);
				 }
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private boolean checkInvoiceIsPendingOrNot(String id) {
		PreparedStatement preparedStatement = null;
		boolean result = false;
		String sql = "select * from apm_charges_invoice where clientid = "+id+"";
		
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

	public String getDeclaration(int id) {
		PreparedStatement preparedStatement = null;
		String notes = " ";
		String sql = "select declaration_text from apm_declaration where selected = 1 ";
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				
				notes = rs.getString(1);
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return notes;
	}

	public String getTitleOfDeclaration(int id) {
		PreparedStatement preparedStatement = null;
		String title = " ";
		String sql = "select title from apm_declaration where selected = 1 ";
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				
				title = rs.getString(1);
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return title;
	}

	public String getPractitionerName(String clientId) {
		PreparedStatement preparedStatement = null;
		String pname = " ";
		String sql = "SELECT diaryuserid FROM apm_available_slot where clientid = "+clientId+" LIMIT 1";
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				
				
				pname = getPractitionerFullname(rs.getString(1));
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return pname;
	}

	private String getPractitionerFullname(String id) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT initial,firstname,lastname FROM apm_user where  id = "+id+"";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				result = rs.getString(1)+" " +rs.getString(2)+" " +rs.getString(3);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
	public String getClinicName(int id) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT clinicname FROM apm_user where  id = "+id+"";
		
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

	
	public int saveNewPatient(Client client, int id) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "insert into apm_patient(title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,"
				+ "third_party_id,third_party_name_id,expiryDate,whopay,policyno,clinicId,status,middlename,refrence,gp_id,treatment_type,policyExcess,lastModified,county,abrivationid,fullname,hospitalborn,maritalsts)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try{
			preparedStatement = connection.prepareStatement(sql);
		
			preparedStatement.setString(1, client.getTitle());
			preparedStatement.setString(2, client.getFirstName());
			preparedStatement.setString(3, client.getLastName());
			preparedStatement.setString(4, client.getMobNo());
			preparedStatement.setString(5, client.getEmail());
			preparedStatement.setString(6, client.getGender());
			preparedStatement.setString(7, client.getDob());
			preparedStatement.setString(8, client.getAddress1());
			preparedStatement.setString(9, client.getTown());
			preparedStatement.setString(10, client.getCountry());
			preparedStatement.setString(11, client.getPostCode());
			preparedStatement.setString(12, client.getType());
			preparedStatement.setString(13, client.getTypeName());
			preparedStatement.setString(14, client.getExpiryDate());
			preparedStatement.setString(15, client.getWhopay());
			preparedStatement.setString(16, client.getPolicyNo());
			preparedStatement.setInt(17, id);
			preparedStatement.setInt(18, 1);
			preparedStatement.setString(19, client.getMiddlename());
			preparedStatement.setString(20, client.getReference());
			preparedStatement.setString(21, client.getGpname());
			preparedStatement.setString(22, client.getTreatmentType());
			preparedStatement.setString(23, client.getPolicyExcess());
			preparedStatement.setTimestamp(24, DateTimeUtils.getCurrentDateInSQLCasting());
			preparedStatement.setString(25, client.getCounty());
			preparedStatement.setString(26, client.getAbrivationid());
			preparedStatement.setString(27, client.getFullname());
			preparedStatement.setString(28, client.getHospitalborn());
			preparedStatement.setString(29, client.getMaritalsts());
			result = preparedStatement.executeUpdate();

			ResultSet set=preparedStatement.getGeneratedKeys();
			
			while(set.next()){
				
				result=set.getInt(1);
				
			}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
	public boolean isMobExist(String mob) {
		PreparedStatement preparedStatement = null;
		boolean result = false;
		String sql = "select * from apm_patient where mobno = '"+mob+"' and mobno!= ''";
		
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

	
	public boolean isEmailIdExist(String email) {
		PreparedStatement preparedStatement = null;
		boolean result = false;
		String sql = "select * from apm_patient where email = '"+email+"' and email!= ''";
		
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
	public int saveNote(String id, String note) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "update apm_patient set note = ? where id = "+id+"";
		try{
			preparedStatement = connection.prepareStatement(sql);
		
			preparedStatement.setString(1, note);
			
			result = preparedStatement.executeUpdate();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	public String getNote(String id) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT note FROM apm_patient where  id = "+id+"";
		
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

	
	public int updateNote(String id, String note) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "update apm_patient set note = ? where id = "+id+"";
		try{
			preparedStatement = connection.prepareStatement(sql);
		
			preparedStatement.setString(1,note);
			
			result = preparedStatement.executeUpdate();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
	public int deleteNote(String id) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "update apm_patient set note = ? where id = "+id+"";
		try{
			preparedStatement = connection.prepareStatement(sql);
		
			preparedStatement.setString(1,"");
			
			result = preparedStatement.executeUpdate();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	
	public String getThirdPartyCompanyEmail(String id) {
		PreparedStatement preparedStatement = null;
		String result = "";
		Client client = new Client();
		String sql = "select third_party_name_id from apm_patient where id = "+id+" ";
		try{
			preparedStatement = connection.prepareStatement(sql);
			
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				 result  =  getCompanyEmailOfThirdParty(rs.getString(1));				
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private String getCompanyEmailOfThirdParty(String id) {
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

	public int getTotalClientUnderPCount(String practionerId) {
		// TODO Auto-generated method stub
		return 0;
	}

	public ArrayList<Client> getAllPatientUnderP(Pagination pagination,
			String practionerId) {
		PreparedStatement preparedStatement = null;
		ArrayList<Client>list = new ArrayList<Client>();
/*		String sql = "select id,title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,refrence,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,whopay,policyauthorzcode,policyno,knownAs,county,homeNo,workNo,emailCc,prefContactMode,emergencyContName,emergencyContNo,patientType,status,note,oldClientId from apm_patient order by lastModified";
*/		
		StringBuffer sql = new StringBuffer();
		sql.append("select  apm_patient.id,title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,refrence,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,whopay,policyauthorzcode,policyno,knownAs,county,homeNo,workNo,emailCc,prefContactMode,emergencyContName,emergencyContNo,patientType,apm_patient.status,note,oldClientId,lastModified ");
		sql.append("from apm_patient inner join apm_available_slot on ");
		sql.append("apm_available_slot.clientId = apm_patient.id where diaryuserid = "+practionerId+" ");
		try{
			
			preparedStatement = connection.prepareStatement(sql.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Client client = new Client();
				client.setId(rs.getInt(1));
				client.setTitle(rs.getString(2));
				client.setFirstName(rs.getString(3));
				client.setLastName(rs.getString(4));
				client.setMobNo(rs.getString(5));
				client.setEmail(rs.getString(6));
				client.setGender(rs.getString(7));
				client.setDob(rs.getString(8));
				client.setAddress(rs.getString(9));
				
				/*String city = rs.getString(10);
				client.setCity(city);
				client.setTown(getcityfromid(city));*/
				client.setTown(rs.getString(10));
				
				client.setCountry(rs.getString(11));
				client.setPostCode(rs.getString(12));
				client.setReference(rs.getString(13));
				client.setSourceOfIntro(rs.getString(14));
				client.setType(rs.getString(15));
				client.setTypeName(rs.getString(16));
				String thirdPartyType = getType(rs.getString(15));
				String thirdPartyTypeName = getTypeName(rs.getString(16));
				client.setThirdPartyType(thirdPartyType);
				client.setThirdPartyTypeName(thirdPartyTypeName);
				client.setOccupation(rs.getString(17));
				client.setExpiryDate(rs.getString(18));
				client.setWhopay(rs.getString(19));
				client.setPolicyAuthorzCode(rs.getString(20));
				client.setPolicyNo(rs.getString(21));
				client.setKnownAs(rs.getString(22));
				client.setCounty(rs.getString(23));
				client.setHomeNo(rs.getString(24));
				client.setWorkNo(rs.getString(25));
				client.setEmailCc(rs.getString(26));
				client.setPrefContactMode(rs.getString(27));
				client.setEmergencyContName(rs.getString(28));
				client.setEmergencyContNo(rs.getString(29));
				client.setPatientType(rs.getString(30));
				client.setStatus(rs.getBoolean(31));
				client.setNote(rs.getString(32));
				client.setOldclientId(rs.getString(33));
				client.setLastModified(rs.getString(34));
				list.add(client);

				/*String pid = getPIdOfClient(rs.getInt(1));
				if(pid.equals(practionerId)){
					list.add(client);
				}*/
				
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private String getPIdOfClient(int id) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT diaryuserid FROM apm_available_slot where clientId = "+id+"";
		
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

	public ArrayList<Client> getGpList() {
		PreparedStatement preparedStatement = null;
		ArrayList<Client>list = new ArrayList<Client>();
		String sql = "select id,name from apm_third_party_details where name!= '' or name!= null order by name";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Client client = new Client();
				client.setId(rs.getInt(1));
				client.setGpname(rs.getString(2));
				list.add(client);
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
		
	}

	public Client getGPDeatils(String id) {
		Client client = new Client();
		PreparedStatement preparedStatement = null;
		String sql = "select id,name from apm_third_party_details where id = "+id+"";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				
				client.setId(rs.getInt(1));
				client.setGpname(rs.getString(2));
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return client;
	}

	public String getTPCompanyName(String typeName) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT company_name FROM apm_third_party_details where  id = "+typeName+"";
		
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
	
	
	public ArrayList<Client> getEmrTreatmentTypeList() {
		PreparedStatement preparedStatement = null;
		ArrayList<Client>list = new ArrayList<Client>();
		String sql = "select id,name,diseasecode,icdcode from apm_diagnosis where name!= '' or name!= null order by name";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Client client = new Client();
				client.setId(rs.getInt(1));
				String condition = rs.getString(2);
				
				if(rs.getString(3)!=null && rs.getString(4)!=null){
					condition =  condition  + " " + rs.getString(3) + " / " + rs.getString(4);
				}
				
				else if(rs.getString(4)!=null){
					condition = condition + " / " + rs.getString(4);
				}
				
				
				
				client.setTreatmentType(condition);
				list.add(client);
			}
			int maxId = getMaxIdOfCondition();
			Client c1= new Client();
			c1.setId(maxId+1);
			c1.setTreatmentType("ADD NEW");
			list.add(c1);
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	

	public ArrayList<Client> getTreatmentTypeList() {
		PreparedStatement preparedStatement = null;
		ArrayList<Client>list = new ArrayList<Client>();
		String sql = "select id,name,diseasecode,icdcode from apm_condition where name!= '' or name!= null order by name";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Client client = new Client();
				client.setId(rs.getInt(1));
				String condition = rs.getString(2);
				
				if(rs.getString(3)!=null && rs.getString(4)!=null){
					condition =  condition  + " " + rs.getString(3) + " / " + rs.getString(4);
				}
				
				else if(rs.getString(4)!=null){
					condition = condition + " / " + rs.getString(4);
				}
				
				
				
				client.setTreatmentType(condition);
				list.add(client);
			}
			int maxId = getMaxIdOfCondition();
			Client c1= new Client();
			c1.setId(maxId+1);
			c1.setTreatmentType("ADD NEW");
			list.add(c1);
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	public int getMaxIdOfCondition() {
		PreparedStatement preparedStatement = null;
	int id = 0;
	String sql = "select max(id) from apm_condition";
	
	try{
		
		preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		while(rs.next()){
			
			id = rs.getInt(1);
		
		}
		
	}catch (Exception e) {
		e.printStackTrace();
	}
	return id;
	}

	public String getSourceOfIntro(String sourceOfIntro) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT sourceOfIntro FROM apm_sourceofintro where  id='"+sourceOfIntro+"'";
		
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

	public ArrayList<GpData> getGPDataList( String surgeryid) {
		PreparedStatement preparedStatement = null;
		ArrayList<GpData>list = new ArrayList<GpData>();
		String sql = "SELECT id,gpname FROM apm_gp_details where tpid = "+surgeryid+"  ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				
				GpData data = new GpData();
				data.setId(rs.getInt(1));
				data.setGpName(rs.getString(2));
				
				list.add(data);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	public String getTpIdDetails(String gpname) {
		PreparedStatement preparedStatement = null;
		String str = "";
		String sql = "SELECT tpid  FROM apm_gp_details where id = "+gpname+" ";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				str = rs.getString(1);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return str;
	}

	public int saveGPData(String popgptype, String gptypeNamepopup,
			String gpnameid, String workphno, String gpemailid, String gpfax,
			String gpNote) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		//String sql = "insert into apm_gp_details(tptypeid,tpid,gpname,note,fax,email,workno) values(?,?,?,?,?,?,?)";
		
	/*	String sql = "insert into apm_gp_details(tptypeid,tpid,gpname,note,fax,email,workno) values(?,?,?,?,?,?,?)";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, popgptype);
			preparedStatement.setString(2, gptypeNamepopup);
			preparedStatement.setString(3, gpnameid);
			preparedStatement.setString(4, gpNote);
			preparedStatement.setString(5, gpfax);
			preparedStatement.setString(6, gpemailid);
			preparedStatement.setString(7, workphno);
			
			result = preparedStatement.executeUpdate(sql);
					
			
		}catch(Exception e){
			e.printStackTrace();
		}*/
		
		
		String sql = "insert into apm_gp_details(tptypeid,tpid,gpname,note,fax,email,workno) values(?,?,?,?,?,?,?) ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, popgptype);
			preparedStatement.setString(2, gptypeNamepopup);
			preparedStatement.setString(3, gpnameid);
			preparedStatement.setString(4, gpNote);
			preparedStatement.setString(5, gpfax);
			preparedStatement.setString(6, gpemailid);
			preparedStatement.setString(7, workphno);
			result = preparedStatement.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}

	public ArrayList<Client> getSurgeryList() {
		
		PreparedStatement preparedStatement = null;
		ArrayList<Client>list = new ArrayList<Client>();
		String sql = "SELECT id,company_name,town,postcode FROM apm_third_party_details where third_party_id = 1 and company_name!= '' or company_name!= null group by company_name";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Client client = new Client();
				client.setId(rs.getInt(1));
				client.setGptypeName(rs.getString(2) + " " + "("+rs.getString(3)+" - "+rs.getString(4)+")");
				
				list.add(client);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return list;
	}

	public String getWhoPayName(String clientId) {
		int id = Integer.parseInt(clientId);
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "select whopay from apm_patient where id = "+id+"";
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				result = rs.getString(1);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	public int insertOtherSourceOfIntro(Client client, String otherSourceOfIntro) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "insert into apm_sourceofintro(sourceOfIntro)values(?)";
		try{
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, otherSourceOfIntro);
			result = preparedStatement.executeUpdate();

		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int insertOtherCondition(Client client, String otherCondition) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "insert into apm_condition(name)values(?)";
		try{
			preparedStatement = connection.prepareStatement(sql);
		
			preparedStatement.setString(1, otherCondition);
			
			
			result = preparedStatement.executeUpdate();

		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public String getClientEmail(String id) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT email FROM apm_patient where  id = "+id+"";
		
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

	public ArrayList<Client> getGPDataList1(String surgeryid) {
		PreparedStatement preparedStatement = null;
		ArrayList<Client>list = new ArrayList<Client>();
		String sql = "SELECT id,gpname  FROM apm_gp_details where tpid = "+surgeryid+"  ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				
				Client data = new Client();
				data.setId(rs.getInt(1));
				data.setGpname(rs.getString(2));
				
				list.add(data);
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	public int updateWhoPay(String patientId, String whopay) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "update apm_patient set whopay=? where id=? ";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, whopay);
			preparedStatement.setInt(2, Integer.parseInt(patientId));
			
			result = preparedStatement.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	public String getClientID(String userid) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT max(id) FROM apm_patient where  third_party_name_id = "+userid+"";
		
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

	public String getGPClientID(String userid) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT max(id) FROM apm_patient where  gp_id = "+userid+"";
		
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

	public Client getSelectedSessionClientDetails(String clientid) {
		PreparedStatement preparedStatement = null;
		Client client = new Client();
		String sql = "SELECT concat(title,' ',firstname,' ',middlename,' ',surname),whopay FROM apm_patient where id = "+clientid+" ";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				client.setClientName(rs.getString(1));
				client.setWhopay(rs.getString(2));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return client;
	}

	public NotAvailableSlot getLastAppointmentdetails(String clientId) {
		PreparedStatement preparedStatement = null;
		NotAvailableSlot noAvailableSlot = new NotAvailableSlot();
		String sql = "SELECT diaryuserid,condition_id,id FROM apm_available_slot where clientid = "+clientId+" order by id desc limit 1";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				noAvailableSlot.setDiaryUserId(rs.getInt(1));
				noAvailableSlot.setCondition(rs.getString(2));
				noAvailableSlot.setId(rs.getInt(3));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return noAvailableSlot;
	}
	
	

	public int savePureSevaClient(LoginInfo loginInfo) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "insert into apm_patient(title,firstname,surname,mobno,email,gender,dob,whopay,lastModified) values(?,?,?,?,?,?,?,?,?)";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, loginInfo.getTitle());
			preparedStatement.setString(2, loginInfo.getFirstName());
			preparedStatement.setString(3, loginInfo.getLastName());
			preparedStatement.setString(4, loginInfo.getMob());
			preparedStatement.setString(5, loginInfo.getEmail());
			preparedStatement.setString(6, loginInfo.getGender());
			preparedStatement.setString(7, loginInfo.getDob());
			preparedStatement.setString(8, "Client");
			preparedStatement.setString(9, DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
			
			result = preparedStatement.executeUpdate();
			

			if(result == 1){
				ResultSet resultSet = preparedStatement.getGeneratedKeys();
				if(resultSet.next()){
					result = resultSet.getInt(1);  
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	public boolean checkEmailidExist(String email) {
		PreparedStatement preparedStatement = null;
		boolean result = false;
		String sql = "SELECT * FROM apm_patient where email = '"+email+"'";
		
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

	public int getPureSevaClientid(String uhid) {
		PreparedStatement preparedStatement = null;
		int resule = 0;
		String sql = "SELECT id FROM apm_patient where abrivationid = '"+uhid+"'";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				resule  = rs.getInt(1);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return resule;
	}

	
	public int saveCiimsClients(String title, String firstname,
			String lastname, String email, String mob, String dob, String gender,String address,String opdno) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "insert into apm_patient(title,firstname,surname,mobno,email,gender,dob,address,oldClientId) values(?,?,?,?,?,?,?,?,?)";
		
		
		
		try{
			preparedStatement = connection.prepareStatement(sql);
		
			preparedStatement.setString(1, title);
			preparedStatement.setString(2, firstname);
			preparedStatement.setString(3, lastname);
			preparedStatement.setString(4, mob);
			preparedStatement.setString(5, email);
			preparedStatement.setString(6, gender);
			preparedStatement.setString(7, dob);
			preparedStatement.setString(8, address);
			preparedStatement.setString(9, opdno);
			
			result = preparedStatement.executeUpdate();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<Master> getDeclerationTitleList() {
		PreparedStatement preparedStatement = null;
		ArrayList<Master>list = new ArrayList<Master>();
		String sql = "select id,title from apm_declaration order by title ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Master master = new Master();
				master.setId(rs.getInt(1));
				master.setName(rs.getString(2));
				
				list.add(master);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}

	public int updateAllDec() {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "update apm_declaration set selected=0 where id>=1";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			result = preparedStatement.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	public int updateDec(String id) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "update apm_declaration set selected=1 where id="+id+" ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			result = preparedStatement.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		return result;
	}

	public String getSelectedDecId() {
		PreparedStatement preparedStatement = null;
		String  result = "";
		String sql = "select id from apm_declaration where selected=1 ";
		
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

	
	public int updateClientUserImage(String userImageFileName,int clientid) {

	    int result=0;		
		try {
			String sql="update apm_patient set imgname='"+userImageFileName+"' where id="+clientid+"";
			PreparedStatement ps=connection.prepareStatement(sql);
			result=ps.executeUpdate();
			
		} catch (Exception e) {

			e.printStackTrace();
		}
	
		return result;
	}

	public int updatePayeeofPatient(String clientid, String payee, String tpid) {

		int result=0;
		
		try {
			
			String sql="update apm_patient set whopay='"+payee+"',third_party_name_id="+tpid+" where id="+clientid+"";
			PreparedStatement ps=connection.prepareStatement(sql);
			result =ps.executeUpdate();
			
		} catch (Exception e) {

		   e.printStackTrace();
		}
		
		return result;
	}

	public ArrayList<Client> getDepartmentTreatmentTypeList(String location) {
		PreparedStatement preparedStatement = null;
		ArrayList<Client>list = new ArrayList<Client>();
		String sql = "select id,name,diseasecode,icdcode from apm_condition where department="+location+" and name!= '' or name!= null  order by name";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Client client = new Client();
				client.setId(rs.getInt(1));
				String condition = rs.getString(2);
				
				if(rs.getString(3)!=null && rs.getString(4)!=null){
					condition =  condition  + " " + rs.getString(3) + " / " + rs.getString(4);
				}
				
				else if(rs.getString(4)!=null){
					condition = condition + " / " + rs.getString(4);
				}
				client.setTreatmentType(condition);
				list.add(client);
			}
			int maxId = getMaxIdOfCondition();
			Client c1= new Client();
			c1.setId(maxId+1);
			c1.setTreatmentType("ADD NEW");
			list.add(c1);
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public int updateEmergencyDetails(Client clientcont) {

		int result=0;
		
		try {
			
			String sql="update apm_patient set emergencyContName=?,emergencyContNo=?,relation=?,refrence=? where id="+clientcont.getId()+"";
			PreparedStatement ps=connection.prepareStatement(sql);
			ps.setString(1, clientcont.getEmergencyContName());
			ps.setString(2, clientcont.getEmergencyContNo());
			ps.setString(3, clientcont.getRelation());
			ps.setString(4, clientcont.getReference());
			
			result=ps.executeUpdate();
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return result;
	}

	public int saveBMIPatient(Client client) {

		int result=0;
		try {
			//Akash 17 jan 2018 added sugarposting and postmeal
			String sql="insert into his_bmi (height, weight, bmi, pulse, sysbp, diabp, clientid,sugarfasting,postmeal,tempr,spo,head_cir,apmtid) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
			//String sql="insert into his_bmi (height, weight, bmi, pulse, sysbp, diabp, clientid) values (?,?,?,?,?,?,?)";
			PreparedStatement ps=connection.prepareStatement(sql);
			ps.setString(1, client.getHeight());
			ps.setString(2, client.getWeight());
			ps.setString(3, client.getBmi());
			ps.setString(4, client.getPulse());
			ps.setString(5, client.getSysbp());
			ps.setString(6, client.getDiabp());
			ps.setString(7, client.getClientid());
			ps.setString(8, client.getSugarfasting());
			ps.setString(9, client.getPostmeal());
			ps.setString(10, client.getTemprature());
			ps.setString(11, client.getSpo());
			ps.setString(12, client.getHead_cir());
			ps.setString(13, client.getAppointmentid());
			result=ps.executeUpdate();
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return result;
	}

	public Client getPatientBMIData(String clientid,int opdid) {

		Client client=new Client();
		try {
			
			String sql="select id, height, weight, bmi, pulse, sysbp, diabp,sugarfasting,postmeal,tempr,spo,head_cir from his_bmi where clientid="+clientid+" and apmtid="+opdid+"";
			PreparedStatement ps=connection.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()) {
				  
				client.setId(rs.getInt(1));
				client.setHeight(rs.getString(2));
				client.setWeight(rs.getString(3));
				client.setBmi(rs.getString(4));
				client.setPulse(rs.getString(5));
				client.setSysbp(rs.getString(6));
				client.setDiabp(rs.getString(7));
				client.setSugarfasting(rs.getString(8));
				client.setPostmeal(rs.getString(9));
				if(rs.getString(10)==null){
					client.setTemprature("");
				}else{
				client.setTemprature(rs.getString(10));
				}
				String spo=rs.getString(11);
				if(spo==null){
					client.setSpo("");
				}else{
				client.setSpo(spo);
			}
				if(rs.getString(12)==null){
					client.setHead_cir("");
				}else{
				client.setHead_cir(rs.getString(12));
			}
				client.setBmi(DateTimeUtils.isNull(client.getBmi()));
				if(client.getBmi().equals("Infinity")){
					client.setBmi("");
				}				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return client;
	}


	  public String getTpIDformPatient(String patientid) {
		  
		  String tpid="0";
		  try {
			
			  String sql="select third_party_name_id from apm_patient where id="+patientid+"";
			  PreparedStatement ps=connection.prepareStatement(sql);
			  ResultSet rs=ps.executeQuery();
			  
			  while(rs.next()){
				  
				   tpid=rs.getString(1);
			  } 
			  
		} catch (Exception e) {

			e.printStackTrace();
		}
		  
		  return tpid;
	  }

	public boolean checkifSequenceExist(String cdate) {
		PreparedStatement preparedStatement = null;
		boolean result = false;
		String sql = "select id from apm_patient_seqno where commencing = '"+cdate+"' ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				result = true;
			}
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		
		return result;
	}

	public int getSqeunceNumber(String cdate) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "select max(seqno) from apm_patient_seqno where commencing = '"+cdate+"' ";
		
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

	public int InserCdateSeq(String cdate,int seqno) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "insert into apm_patient_seqno(commencing,seqno) values(?,?) ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, cdate);
			preparedStatement.setInt(2, seqno);
			result = preparedStatement.executeUpdate();
			
			if(result == 1){
				ResultSet resultSet = preparedStatement.getGeneratedKeys();
				if(resultSet.next()){
					result = resultSet.getInt(1);  
				}
			}
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	public String getClinicAbrivation(int clinicid) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "select abrivation from apm_user where id = "+clinicid+" ";
		
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
	
	
	public ArrayList<Client> getAllPatient() {
		PreparedStatement preparedStatement = null;
		ArrayList<Client>list = new ArrayList<Client>();
		String sql = "select id,title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,refrence,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,whopay,policyauthorzcode,policyno,knownAs,county,homeNo,workNo,emailCc,prefContactMode,emergencyContName,emergencyContNo,patientType,status,note,oldClientId,lastModified,gp_id,imgname,casualtyid,adhno,middlename,mbalance,abrivationid from apm_patient order by id desc ";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Client client = new Client();
				
				client.setId(rs.getInt(1));
				client.setTitle(rs.getString(2));
				client.setFirstName(rs.getString(3));
				client.setLastName(rs.getString(4));
				client.setMobNo(rs.getString(5));
				client.setEmail(rs.getString(6));
				client.setGender(rs.getString(7));
				client.setDob(rs.getString(8));
				client.setAddress(rs.getString(9));
				/*String city = rs.getString(10);
				client.setCity(city);
				client.setTown(getcityfromid(city));*/
				client.setTown(rs.getString(10));
				
				String age =DateTimeUtils.getAge(client.getDob());
				client.setAge(Integer.parseInt(age));
				
				client.setCountry(rs.getString(11));
				client.setPostCode(rs.getString(12));
				client.setReference(rs.getString(13));
				client.setSourceOfIntro(rs.getString(14));
				client.setType(rs.getString(15));
				client.setTypeName(rs.getString(16));
				
				String thirdPartyType = getType(rs.getString(15));
				String thirdPartyTypeName = getTypeName(rs.getString(16));
				
				client.setThirdPartyType(thirdPartyType);
				client.setThirdPartyTypeName(thirdPartyTypeName);
				client.setOccupation(rs.getString(17));
				client.setExpiryDate(rs.getString(18));
				client.setWhopay(rs.getString(19));
				client.setPolicyAuthorzCode(rs.getString(20));
				client.setPolicyNo(rs.getString(21));
				client.setKnownAs(rs.getString(22));
				client.setCounty(rs.getString(23));
				client.setHomeNo(rs.getString(24));
				client.setWorkNo(rs.getString(25));
				client.setEmailCc(rs.getString(26));
				client.setPrefContactMode(rs.getString(27));
				client.setEmergencyContName(rs.getString(28));
				client.setEmergencyContNo(rs.getString(29));
				client.setPatientType(rs.getString(30));
				client.setStatus(rs.getBoolean(31));
				client.setNote(rs.getString(32));
				client.setOldclientId(rs.getString(33));
				client.setLastModified(rs.getString(34));
				client.setGpid(rs.getString(35));
				client.setImageName(rs.getString(36));
				client.setCasualtyid(rs.getString(37));
				client.setAdhno(rs.getString(38));
				client.setMiddlename(rs.getString(39));
				client.setBalance(rs.getString(40));
				client.setIspharmacy("0");
				client.setAbrivationid(rs.getString(41));
				String fullname = rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4);
				client.setFullname(fullname);
				list.add(client);
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
		
		
	}

	public String getIpdPractionerName(String clientId) {

		String res="";
		try {
			String sql="SELECT practitionerid from ipd_addmission_form where clientid="+clientId+" and bedid!=0 order by id desc limit 0,1  ";
			PreparedStatement ps= connection.prepareStatement(sql);
			ResultSet rs =ps.executeQuery();
			while(rs.next()){
				  
				res = getPractitionerFullname(rs.getString(1));
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		return res;
	}
	public String getIpdPractionerSpeciality(String clientId) {

		String res="";
		try {
			UserProfileDAO userProfileDAO =  new JDBCUserProfileDAO(connection);
			String sql="SELECT practitionerid from ipd_addmission_form where clientid="+clientId+" and bedid!=0 order by id desc limit 0,1  ";
			PreparedStatement ps= connection.prepareStatement(sql);
			ResultSet rs =ps.executeQuery();
			while(rs.next()){
				UserProfile userProfile = userProfileDAO.getUserprofileDetails(rs.getInt(1));
				res = userProfile.getSpecialization();
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		return res;
	}
	public String getPractitionerSpeciality(String clientId) {
		PreparedStatement preparedStatement = null;
		String pname = " ";
		String sql = "SELECT diaryuserid FROM apm_available_slot where clientid = "+clientId+" LIMIT 1";
		try{
			UserProfileDAO userProfileDAO =  new JDBCUserProfileDAO(connection);
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				UserProfile userProfile = userProfileDAO.getUserprofileDetails(rs.getInt(1));
				pname = userProfile.getSpecialization();
				
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return pname;
	}
	public String getIpdPractionerId(String clientId) {

		String res="";
		try {
			String sql="SELECT practitionerid from ipd_addmission_form where clientid="+clientId+" and bedid!=0 order by id desc limit 0,1  ";
			PreparedStatement ps= connection.prepareStatement(sql);
			ResultSet rs =ps.executeQuery();
			while(rs.next()){
				  
				res = rs.getString(1);
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		return res;
	}

	public int saveDeclaration(Client client) {
		PreparedStatement preparedStatement = null;
		int result = 0;
		String sql = "insert into apm_document(patientid, practitionerid, lastmodified,  uploadby,  ipdopd, declaration_data, status, declration_title) values(?,?,?,?,?,?,?,?) ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, client.getClientId());
			preparedStatement.setString(2, client.getPractid());
			preparedStatement.setString(3, client.getLastModifiedDate());
			preparedStatement.setString(4, client.getUserid());
			preparedStatement.setString(5, client.getIpdid());
			preparedStatement.setString(6, client.getDeclarationNotes());
			preparedStatement.setString(7, "1");
			preparedStatement.setString(8, client.getDeclarationTitle());
			result = preparedStatement.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public Client getDeclarationData(int ipdid) {
		PreparedStatement preparedStatement = null;
		Client client = new Client();
		try {
			String sql ="select id,patientid, practitionerid, lastmodified,  uploadby,  ipdopd, declaration_data, declration_title from apm_document where ipdopd='"+ipdid+"' and status='1' ORDER BY id DESC LIMIT 1";
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				client.setId(rs.getInt(1));
				client.setClientId(rs.getString(2));
				client.setPractid(rs.getString(3));
				client.setLastModified(rs.getString(4));
				client.setUserid(rs.getString(5));
				client.setIpdid(rs.getString(6));
				client.setDeclarationNotes(rs.getString(7));
				client.setDeclarationTitle(rs.getString(8));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return client;
	}

	public ArrayList<Client> getDeclarationDataList(String clientId) {
		PreparedStatement preparedStatement = null;
		ArrayList<Client> arrayList = new ArrayList<Client>();
		try {
			String sql ="select id,patientid, practitionerid, lastmodified,  uploadby,  ipdopd, declaration_data, declration_title from apm_document where patientid='"+clientId+"' and status='1'";
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Client client = new Client();
				client.setId(rs.getInt(1));
				client.setClientId(rs.getString(2));
				client.setPractid(rs.getString(3));
				String[] date = rs.getString(4).split(" ");
				String datetime = DateTimeUtils.getCommencingDate1(date[0])+" "+date[1];
				client.setLastModified(datetime);
				client.setUserid(rs.getString(5));
				client.setIpdid(rs.getString(6));
				client.setDeclarationNotes(rs.getString(7));
				client.setDeclarationTitle(rs.getString(8));
				arrayList.add(client);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public Client getDeclarationByID(String id) {
		PreparedStatement preparedStatement = null;
		Client client = new Client();
		try {
			String sql ="select id,patientid, practitionerid, lastmodified,  uploadby,  ipdopd, declaration_data, declration_title from apm_document where id='"+id+"'";
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				client.setId(rs.getInt(1));
				client.setClientId(rs.getString(2));
				client.setPractid(rs.getString(3));
				client.setLastModified(rs.getString(4));
				client.setUserid(rs.getString(5));
				client.setIpdid(rs.getString(6));
				client.setDeclarationNotes(rs.getString(7));
				client.setDeclarationTitle(rs.getString(8));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return client;
	}

	public int updateDeclarationData(Client client) {
int result=0;
		
		try {
			
			String sql="update apm_document set declaration_data=?,declration_title=? where id="+client.getId()+"";
			PreparedStatement ps=connection.prepareStatement(sql);
			ps.setString(1, client.getDeclarationNotes());
			ps.setString(2, client.getDeclarationTitle());
			result=ps.executeUpdate();
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return result;

	}

	public Bed getdischargedata(String addmissionid) {
		PreparedStatement preparedStatement = null;
		
		
		BedDao bedDao=new JDBCBedDao(connection);
		Bed bed = bedDao.getEditIpdData(addmissionid);
		Bed bed2 = new Bed();
		String sql = "SELECT dischargedate,dschargestatus FROM apm_treatment_episode where id = "+bed.getTreatmentepisodeid()+" ";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			bed2.setDischargeDate("");
			if(rs.next()){
				if(rs.getString(1)!=null){
					bed2.setDischargeDate(DateTimeUtils.getDBDate(rs.getString(1)));
				}else{
					bed2.setDischargeDate("");
				}
				bed2.setDischargeStatus(rs.getString(2));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return bed2;
	}

	public int check_child_growth_data(String clientId, String month) {
		int res =0;
		try {
			String sql ="select id from child_growth_data where clientid='"+clientId+"' and month='"+month+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				res = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public int updateChildGrowthData(Client client, String val) {
		int result=0;
		try {
			String sql="";
			if(val.equals("height")){
				sql="update child_growth_data set height='"+client.getHeightdata()+"',height_dt='"+client.getDate()+"',height_userid='"+client.getUserid()+"' where id="+client.getId()+"";
			}else if(val.equals("weight")){
				sql="update child_growth_data set weight='"+client.getWeightdata()+"',weight_dt='"+client.getDate()+"',weight_userid='"+client.getUserid()+"' where id="+client.getId()+"";
			}else if(val.equals("bmi")){
				sql="update child_growth_data set bmi='"+client.getBmidata()+"',bmi_dt='"+client.getDate()+"',bmi_userid='"+client.getUserid()+"' where id="+client.getId()+"";
			}else if(val.equals("headcircumfernce")){
				sql="update child_growth_data set head_circumfernce='"+client.getHeadcircumferncedata()+"',head_circumfernce_dt='"+client.getDate()+"',head_circumfernce_userid='"+client.getUserid()+"' where id="+client.getId()+"";
			}else if(val.equals("length")){//length, length_dt, length_userid
				sql="update child_growth_data set length='"+client.getHeadcircumferncedata()+"',length_dt='"+client.getDate()+"',length_userid='"+client.getUserid()+"' where id="+client.getId()+"";
			}
			PreparedStatement ps=connection.prepareStatement(sql);
			result=ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int saveChildGrowthData(Client client, String val) {
		int result=0;
		try {
			String sql="";
			if(val.equals("height")){
				sql = "insert into child_growth_data(month,clientid,height,height_dt,height_userid) values(?,?,?,?,?) ";
			}else if(val.equals("weight")){
				sql = "insert into child_growth_data(month,clientid,weight,weight_dt,weight_userid) values(?,?,?,?,?) ";
			}else if(val.equals("bmi")){
				sql = "insert into child_growth_data(month,clientid,bmi,bmi_dt,bmi_userid) values(?,?,?,?,?) ";
			}else if(val.equals("headcircumfernce")){
				sql = "insert into child_growth_data(month,clientid,head_circumfernce,head_circumfernce_dt,head_circumfernce_userid) values(?,?,?,?,?) ";
			}else if(val.equals("length")){//length, length_dt, length_userid
				sql = "insert into child_growth_data(month,clientid,length,length_dt,length_userid) values(?,?,?,?,?) ";
			}
			PreparedStatement ps=connection.prepareStatement(sql);
			ps.setString(1, client.getMonth());
			ps.setString(2, client.getClientid());
			if(val.equals("height")){
				ps.setString(3, client.getHeightdata());
			}else if(val.equals("weight")){
				ps.setString(3, client.getWeightdata());
			}else if(val.equals("bmi")){
				ps.setString(3, client.getBmidata());
			}else if(val.equals("headcircumfernce")){
				ps.setString(3, client.getHeadcircumferncedata());
			}else if(val.equals("length")){
				ps.setString(3, client.getLength());
			}
			ps.setString(4, client.getDate());
			ps.setString(5, client.getUserid());
			result=ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<Client> getHeightMasterList(String gender , int heightcount) {
		ArrayList<Client> arrayList = new ArrayList<Client>();
		try {
			String sql ="";
			StringBuffer buffer = new StringBuffer();
			if(gender.equals("Male")){
				//sql = "select id, month, ob_height, owb_height, nb_height, tb_height, stb_height from child_growth_master order by (month+0) asc";
				buffer.append("select id, month, ob_height, owb_height, nb_height, tb_height, stb_height from child_growth_master ");
				buffer.append("where (ob_height!='' or owb_height!='' or nb_height!='' or tb_height!='' or stb_height!='') and month is not null and month!='' group by month ");
				buffer.append("order by (month+0) asc ");
			}else{
				//sql = "select id, month, og_height, owg_height, ng_height, tg_height, stg_height from child_growth_master order by (month+0) asc";
				buffer.append("select id, month, og_height, owg_height, ng_height, tg_height, stg_height from child_growth_master ");
				buffer.append("where (og_height!='' or owg_height!='' or ng_height!='' or tg_height!='' or stg_height!='') and month is not null and month!='' group by month ");
				buffer.append("order by (month+0) asc ");
			}
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			int count =0;
			int month =0;
			while (rs.next()) {
				Client client = new Client();
				client.setId(rs.getInt(1));
				month = rs.getInt(2);
				client.setMonth(rs.getString(2));
				if(rs.getString(3)!=null){
					if(!rs.getString(3).equals("")){
						client.setObesity(rs.getString(3));
					}else{
						client.setObesity("0");
					}
				}else{
					client.setObesity("0");
				}
				
				if(rs.getString(4)!=null){
					if(!rs.getString(4).equals("")){
						client.setOwerweight(rs.getString(4));
					}else{
						client.setOwerweight("0");
					}
				}else{
					client.setOwerweight("0");
				}
				
				if(rs.getString(5)!=null){
					if(!rs.getString(5).equals("")){
						client.setNormal(rs.getString(5));
					}else{
						client.setNormal("0");
					}
				}else{
					client.setNormal("0");
				}
				
				if(rs.getString(6)!=null){
					if(!rs.getString(6).equals("")){
						client.setThinness(rs.getString(6));
					}else{
						client.setThinness("0");
					}
				}else{
					client.setThinness("0");
				}
				
				if(rs.getString(7)!=null){
					if(!rs.getString(7).equals("")){
						client.setSeverthinness(rs.getString(7));
					}else{
						client.setSeverthinness("0");
					}
				}else{
					client.setSeverthinness("0");
				}
				
				//client.setOwerweight(rs.getString(4));
				//client.setNormal(rs.getString(5));
				//client.setThinness(rs.getString(6));
				//client.setSeverthinness(rs.getString(7));
				arrayList.add(client);
				count++;
			}
			for (; count < heightcount; count++) {
				Client client = new Client();
				client.setObesity("0");
				client.setOwerweight("0");
				client.setNormal("0");
				client.setThinness("0");
				client.setSeverthinness("0");
				client.setMonth(""+(++month));
				arrayList.add(client);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public ArrayList<Client> getWeightMasterList(String gender, int weightcount) {
		ArrayList<Client> arrayList = new ArrayList<Client>();
		try {
			String sql ="";
			StringBuffer buffer = new StringBuffer();
			if(gender.equals("Male")){
				//sql = "select id, month, ob_weight, owb_weight, nb_weight, tb_weight, stb_weight from child_growth_master order by (month+0) asc";
				buffer.append("select id, month, ob_weight, owb_weight, nb_weight, tb_weight, stb_weight from child_growth_master ");
				buffer.append("where (ob_weight!='' or owb_weight!='' or nb_weight!='' or tb_weight!='' or stb_weight!='') and month is not null and month!='' group by month ");
				buffer.append("order by (month+0) asc ");
			}else{
				//sql = "select id, month, og_weight, owg_weight, ng_weight, tg_weight, stg_weight from child_growth_master order by (month+0) asc";
				buffer.append("select id, month, og_weight, owg_weight, ng_weight, tg_weight, stg_weight from child_growth_master ");
				buffer.append("where (og_weight!='' or owg_weight!='' or ng_weight!='' or tg_weight!='' or stg_weight!='') and month is not null and month!='' group by month ");
				buffer.append("order by (month+0) asc ");
			}
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			int count=0;
			int month =0;
			while (rs.next()) {
				Client client = new Client();
				client.setId(rs.getInt(1));
				client.setMonth(rs.getString(2));
				month = rs.getInt(2);
				if(rs.getString(3)!=null){
					if(!rs.getString(3).equals("")){
						client.setObesity(rs.getString(3));
					}else{
						client.setObesity("0");
					}
				}else{
					client.setObesity("0");
				}
				
				if(rs.getString(4)!=null){
					if(!rs.getString(4).equals("")){
						client.setOwerweight(rs.getString(4));
					}else{
						client.setOwerweight("0");
					}
				}else{
					client.setOwerweight("0");
				}
				
				if(rs.getString(5)!=null){
					if(!rs.getString(5).equals("")){
						client.setNormal(rs.getString(5));
					}else{
						client.setNormal("0");
					}
				}else{
					client.setNormal("0");
				}
				
				if(rs.getString(6)!=null){
					if(!rs.getString(6).equals("")){
						client.setThinness(rs.getString(6));
					}else{
						client.setThinness("0");
					}
				}else{
					client.setThinness("0");
				}
				
				if(rs.getString(7)!=null){
					if(!rs.getString(7).equals("")){
						client.setSeverthinness(rs.getString(7));
					}else{
						client.setSeverthinness("0");
					}
				}else{
					client.setSeverthinness("0");
				}
				
				//client.setOwerweight(rs.getString(4));
				//client.setNormal(rs.getString(5));
				//client.setThinness(rs.getString(6));
				//client.setSeverthinness(rs.getString(7));
				
				/*client.setObesity(rs.getString(3));
				client.setOwerweight(rs.getString(4));
				client.setNormal(rs.getString(5));
				client.setThinness(rs.getString(6));
				client.setSeverthinness(rs.getString(7));*/
				arrayList.add(client);
				count++;
			}
			for (; count < weightcount; count++) {
				Client client = new Client();
				client.setObesity("0");
				client.setOwerweight("0");
				client.setNormal("0");
				client.setThinness("0");
				client.setSeverthinness("0");
				client.setMonth(""+(++month));
				arrayList.add(client);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public ArrayList<Client> getBmiMasterList(String gender, int bmicount) {
		ArrayList<Client> arrayList = new ArrayList<Client>();
		try {
			String sql ="";
			StringBuffer buffer = new StringBuffer();
			if(gender.equals("Male")){
				sql = "select id, month, ob_bmi, owb_bmi, nb_bmi, tb_bmi, stb_bmi from child_growth_master order by (month+0) asc";
				buffer.append("select id, month, ob_bmi, owb_bmi, nb_bmi, tb_bmi, stb_bmi from child_growth_master ");
				buffer.append("where (ob_bmi!='' or owb_bmi!='' or nb_bmi!='' or tb_bmi!='' or stb_bmi!='') and month is not null and month!='' group by month ");
				buffer.append("order by (month+0) asc ");
			}else{
				sql = "select id, month, og_bmi, owg_bmi, ng_bmi, tg_bmi, stg_bmi from child_growth_master order by (month+0) asc";
				buffer.append("select id, month, og_bmi, owg_bmi, ng_bmi, tg_bmi, stg_bmi from child_growth_master ");
				buffer.append("where (og_bmi!='' or owg_bmi!='' or ng_bmi!='' or tg_bmi!='' or stg_bmi!='') and month is not null and month!='' group by month ");
				buffer.append("order by (month+0) asc ");
			}
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			int count=0;
			int month =0;
			while (rs.next()) {
				Client client = new Client();
				client.setId(rs.getInt(1));
				client.setMonth(rs.getString(2));
				month = rs.getInt(2);
				if(rs.getString(3)!=null){
					if(!rs.getString(3).equals("")){
						client.setObesity(rs.getString(3));
					}else{
						client.setObesity("0");
					}
				}else{
					client.setObesity("0");
				}
				
				if(rs.getString(4)!=null){
					if(!rs.getString(4).equals("")){
						client.setOwerweight(rs.getString(4));
					}else{
						client.setOwerweight("0");
					}
				}else{
					client.setOwerweight("0");
				}
				
				if(rs.getString(5)!=null){
					if(!rs.getString(5).equals("")){
						client.setNormal(rs.getString(5));
					}else{
						client.setNormal("0");
					}
				}else{
					client.setNormal("0");
				}
				
				if(rs.getString(6)!=null){
					if(!rs.getString(6).equals("")){
						client.setThinness(rs.getString(6));
					}else{
						client.setThinness("0");
					}
				}else{
					client.setThinness("0");
				}
				
				if(rs.getString(7)!=null){
					if(!rs.getString(7).equals("")){
						client.setSeverthinness(rs.getString(7));
					}else{
						client.setSeverthinness("0");
					}
				}else{
					client.setSeverthinness("0");
				}
				
				//client.setOwerweight(rs.getString(4));
				//client.setNormal(rs.getString(5));
				//client.setThinness(rs.getString(6));
				//client.setSeverthinness(rs.getString(7));
				
				/*client.setObesity(rs.getString(3));
				client.setOwerweight(rs.getString(4));
				client.setNormal(rs.getString(5));
				client.setThinness(rs.getString(6));
				client.setSeverthinness(rs.getString(7));*/
				arrayList.add(client);
				count++;
			}
			for (; count < bmicount; count++) {
				Client client = new Client();
				client.setObesity("0");
				client.setOwerweight("0");
				client.setNormal("0");
				client.setThinness("0");
				client.setSeverthinness("0");
				client.setMonth(""+(++month));
				arrayList.add(client);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public ArrayList<Client> getHeadMasterList(String gender, int headcount) {
		ArrayList<Client> arrayList = new ArrayList<Client>();
		try {
			String sql ="";
			StringBuffer buffer = new StringBuffer();
			if(gender.equals("Male")){
				//sql = "select id, month, ob_head, owb_head, nb_head, tb_head, stb_head from child_growth_master order by (month+0) asc";
				buffer.append("select id, month, ob_head, owb_head, nb_head, tb_head, stb_head from child_growth_master ");
				buffer.append("where (ob_head!='' or owb_head!='' or nb_head!='' or tb_head!='' or stb_head!='') and month is not null and month!='' group by month ");
				buffer.append("order by (month+0) asc ");
			}else{
				//sql = "select id, month, og_head, owg_head, ng_head, tg_head, stg_head from child_growth_master order by (month+0) asc";
				buffer.append("select id, month, og_head, owg_head, ng_head, tg_head, stg_head from child_growth_master ");
				buffer.append("where (og_head!='' or owg_head!='' or ng_head!='' or tg_head!='' or stg_head!='') and month is not null and month!='' group by month ");
				buffer.append("order by (month+0) asc ");
			}
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			int count=0;
			int month =0;
			while (rs.next()) {
				Client client = new Client();
				client.setId(rs.getInt(1));
				client.setMonth(rs.getString(2));
				month = rs.getInt(2);
				if(rs.getString(3)!=null){
					if(!rs.getString(3).equals("")){
						client.setObesity(rs.getString(3));
					}else{
						client.setObesity("0");
					}
				}else{
					client.setObesity("0");
				}
				
				if(rs.getString(4)!=null){
					if(!rs.getString(4).equals("")){
						client.setOwerweight(rs.getString(4));
					}else{
						client.setOwerweight("0");
					}
				}else{
					client.setOwerweight("0");
				}
				
				if(rs.getString(5)!=null){
					if(!rs.getString(5).equals("")){
						client.setNormal(rs.getString(5));
					}else{
						client.setNormal("0");
					}
				}else{
					client.setNormal("0");
				}
				
				if(rs.getString(6)!=null){
					if(!rs.getString(6).equals("")){
						client.setThinness(rs.getString(6));
					}else{
						client.setThinness("0");
					}
				}else{
					client.setThinness("0");
				}
				
				if(rs.getString(7)!=null){
					if(!rs.getString(7).equals("")){
						client.setSeverthinness(rs.getString(7));
					}else{
						client.setSeverthinness("0");
					}
				}else{
					client.setSeverthinness("0");
				}
				
				//client.setOwerweight(rs.getString(4));
				//client.setNormal(rs.getString(5));
				//client.setThinness(rs.getString(6));
				//client.setSeverthinness(rs.getString(7));
				
				/*client.setObesity(rs.getString(3));
				client.setOwerweight(rs.getString(4));
				client.setNormal(rs.getString(5));
				client.setThinness(rs.getString(6));
				client.setSeverthinness(rs.getString(7));*/
				arrayList.add(client);
				count++;
			}
			for (; count < headcount; count++) {
				Client client = new Client();
				client.setObesity("0");
				client.setOwerweight("0");
				client.setNormal("0");
				client.setThinness("0");
				client.setSeverthinness("0");
				client.setMonth(""+(++month));
				arrayList.add(client);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public ArrayList<Client> getClientGrowthList(String clientId) {
		ArrayList<Client> arrayList = new ArrayList<Client>();
		try {
			String sql ="select month from child_growth_master order by (month+0) asc";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Client client = new Client();
				Client client2 = getClientGrowthData(rs.getString(1),clientId);
				client.setMonth(rs.getString(1));
				if(client2.getHeightdata()==null){
					client2.setHeightdata("0");
				}else if(client2.getHeightdata().equals("")){
					client2.setHeightdata("0");
				}
				if(client2.getWeightdata()==null){
					client2.setWeightdata("0");
				}else if(client2.getWeightdata().equals("")){
					client2.setWeightdata("0");
				}
				if(client2.getBmidata()==null){
					client2.setBmidata("0");
				}else if(client2.getBmidata().equals("")){
					client2.setBmidata("0");
				}
				if(client2.getHeadcircumferncedata()==null){
					client2.setHeadcircumferncedata("0");
				}else if(client2.getHeadcircumferncedata().equals("")){
					client2.setHeadcircumferncedata("0");;
				}
				
				if(client2.getLength()==null){
					client2.setLength("0");
				}else if(client2.getLength().equals("")){
					client2.setLength("0");;
				}
				
				client.setHeightdata(client2.getHeightdata());
				client.setWeightdata(client2.getWeightdata());
				client.setBmidata(client2.getBmidata());
				client.setHeadcircumferncedata(client2.getHeadcircumferncedata());
				client.setLength(client2.getLength());
				arrayList.add(client);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	private Client getClientGrowthData(String month, String clientId) {
		Client client = new Client();
		try {
			String sql ="select height, weight, bmi, head_circumfernce,length from child_growth_data where clientid='"+clientId+"' and month='"+month+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				client.setHeightdata(rs.getString(1));
				client.setWeightdata(rs.getString(2));
				client.setBmidata(rs.getString(3));
				client.setHeadcircumferncedata(rs.getString(4));
				client.setLength(rs.getString(5));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return client;
	}

	public ArrayList<Client> getLengthHeightMasterList(String gender , int lengthcount) {
		ArrayList<Client> arrayList = new ArrayList<Client>();
		try {
			StringBuffer buffer = new StringBuffer();
			
			String sql ="";
			if(gender.equals("Male")){
				//sql = "select id, month, ob_length, owb_length, nb_length, tb_length, stb_length from child_growth_master order by (month+0) asc";
				buffer.append("select id, month, ob_length, owb_length, nb_length, tb_length, stb_length from child_growth_master ");
				buffer.append("where (ob_length!='' or owb_length!='' or nb_length!='' or tb_length!='' or stb_length!='') and month is not null and month!='' group by month ");
				buffer.append("order by (month+0) asc ");
			}else{
				//sql = "select id, month, og_length, owg_length, ng_length, tg_length, stg_length from child_growth_master order by (month+0) asc";
				buffer.append("select id, month, og_length, owg_length, ng_length, tg_length, stg_length from child_growth_master ");
				buffer.append("where (og_length!='' or owg_length!='' or ng_length!='' or tg_length!='' or stg_length!='') and month is not null and month!='' group by month ");
				buffer.append("order by (month+0) asc ");
			}
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			int count =0;
			int month =0;
			while (rs.next()) {
				Client client = new Client();
				client.setId(rs.getInt(1));
				client.setMonth(rs.getString(2));
				month = rs.getInt(2);
				if(rs.getString(3)!=null){
					if(!rs.getString(3).equals("")){
						client.setObesity(rs.getString(3));
					}else{
						client.setObesity("0");
					}
				}else{
					client.setObesity("0");
				}
				
				if(rs.getString(4)!=null){
					if(!rs.getString(4).equals("")){
						client.setOwerweight(rs.getString(4));
					}else{
						client.setOwerweight("0");
					}
				}else{
					client.setOwerweight("0");
				}
				
				if(rs.getString(5)!=null){
					if(!rs.getString(5).equals("")){
						client.setNormal(rs.getString(5));
					}else{
						client.setNormal("0");
					}
				}else{
					client.setNormal("0");
				}
				
				if(rs.getString(6)!=null){
					if(!rs.getString(6).equals("")){
						client.setThinness(rs.getString(6));
					}else{
						client.setThinness("0");
					}
				}else{
					client.setThinness("0");
				}
				
				if(rs.getString(7)!=null){
					if(!rs.getString(7).equals("")){
						client.setSeverthinness(rs.getString(7));
					}else{
						client.setSeverthinness("0");
					}
				}else{
					client.setSeverthinness("0");
				}
				
				//client.setOwerweight(rs.getString(4));
				//client.setNormal(rs.getString(5));
				//client.setThinness(rs.getString(6));
				//client.setSeverthinness(rs.getString(7));
				/*client.setObesity(rs.getString(3));
				client.setOwerweight(rs.getString(4));
				client.setNormal(rs.getString(5));
				client.setThinness(rs.getString(6));
				client.setSeverthinness(rs.getString(7));*/
				arrayList.add(client);
				count++;
			}
			for (; count < lengthcount; count++) {
				Client client = new Client();
				client.setObesity("0");
				client.setOwerweight("0");
				client.setNormal("0");
				client.setThinness("0");
				client.setSeverthinness("0");
				client.setMonth(""+(++month));
				arrayList.add(client);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public int getHeightMasterCount(String gender) {
		int count=0;
		try {
			String sql ="";
			StringBuffer buffer = new StringBuffer();
			if(gender.equals("Male")){
				//sql = "select id, month, ob_height, owb_height, nb_height, tb_height, stb_height from child_growth_master order by (month+0) asc";
				buffer.append("select id, month, ob_height, owb_height, nb_height, tb_height, stb_height from child_growth_master ");
				buffer.append("where ob_height!='' or owb_height!='' or nb_height!='' or tb_height!='' or stb_height!='' ");
				buffer.append("order by (month+0) desc limit 1 ");
			}else{
				//sql = "select id, month, og_height, owg_height, ng_height, tg_height, stg_height from child_growth_master order by (month+0) asc";
				buffer.append("select id, month, og_height, owg_height, ng_height, tg_height, stg_height from child_growth_master ");
				buffer.append("where og_height!='' or owg_height!='' or ng_height!='' or tg_height!='' or stg_height!='' ");
				buffer.append("order by (month+0) desc limit 1 ");
			}
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				count = rs.getInt(2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public int getWeightMasterCount(String gender) {
		int count=0;
		try {
			String sql ="";
			StringBuffer buffer = new StringBuffer();
			if(gender.equals("Male")){
				//sql = "select id, month, ob_weight, owb_weight, nb_weight, tb_weight, stb_weight from child_growth_master order by (month+0) asc";
				buffer.append("select id, month, ob_weight, owb_weight, nb_weight, tb_weight, stb_weight from child_growth_master ");
				buffer.append("where ob_weight!='' or owb_weight!='' or nb_weight!='' or tb_weight!='' or stb_weight!='' ");
				buffer.append("order by (month+0) desc limit 1 ");
			}else{
				//sql = "select id, month, og_weight, owg_weight, ng_weight, tg_weight, stg_weight from child_growth_master order by (month+0) asc";
				buffer.append("select id, month, og_weight, owg_weight, ng_weight, tg_weight, stg_weight from child_growth_master ");
				buffer.append("where og_weight!='' or owg_weight!='' or ng_weight!='' or tg_weight!='' or stg_weight!='' ");
				buffer.append("order by (month+0) desc limit 1 ");
			}
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				count = rs.getInt(2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public int getBmiMasterCount(String gender) {
		int count=0;
		try {
			String sql ="";
			StringBuffer buffer = new StringBuffer();
			if(gender.equals("Male")){
				sql = "select id, month, ob_bmi, owb_bmi, nb_bmi, tb_bmi, stb_bmi from child_growth_master order by (month+0) asc";
				buffer.append("select id, month, ob_bmi, owb_bmi, nb_bmi, tb_bmi, stb_bmi from child_growth_master ");
				buffer.append("where ob_bmi!='' or owb_bmi!='' or nb_bmi!='' or tb_bmi!='' or stb_bmi!='' ");
				buffer.append("order by (month+0) desc limit 1 ");
			}else{
				sql = "select id, month, og_bmi, owg_bmi, ng_bmi, tg_bmi, stg_bmi from child_growth_master order by (month+0) asc";
				buffer.append("select id, month, og_bmi, owg_bmi, ng_bmi, tg_bmi, stg_bmi from child_growth_master ");
				buffer.append("where og_bmi!='' or owg_bmi!='' or ng_bmi!='' or tg_bmi!='' or stg_bmi!='' ");
				buffer.append("order by (month+0) desc limit 1 ");
			}
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				count = rs.getInt(2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public int getHeadMasterCount(String gender) {
		int count=0;
		try {
			String sql ="";
			StringBuffer buffer = new StringBuffer();
			if(gender.equals("Male")){
				//sql = "select id, month, ob_head, owb_head, nb_head, tb_head, stb_head from child_growth_master order by (month+0) asc";
				buffer.append("select id, month, ob_head, owb_head, nb_head, tb_head, stb_head from child_growth_master ");
				buffer.append("where ob_head!='' or owb_head!='' or nb_head!='' or tb_head!='' or stb_head!='' ");
				buffer.append("order by (month+0) desc limit 1 ");
			}else{
				//sql = "select id, month, og_head, owg_head, ng_head, tg_head, stg_head from child_growth_master order by (month+0) asc";
				buffer.append("select id, month, og_head, owg_head, ng_head, tg_head, stg_head from child_growth_master ");
				buffer.append("where og_head!='' or owg_head!='' or ng_head!='' or tg_head!='' or stg_head!='' ");
				buffer.append("order by (month+0) desc limit 1 ");
			}
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				count = rs.getInt(2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public int getLengthHeightMasterCount(String gender) {
		int count=0;
		try {
			StringBuffer buffer = new StringBuffer();
			String sql ="";
			if(gender.equals("Male")){
				//sql = "select id, month, ob_length, owb_length, nb_length, tb_length, stb_length from child_growth_master order by (month+0) asc";
				buffer.append("select id, month, ob_length, owb_length, nb_length, tb_length, stb_length from child_growth_master ");
				buffer.append("where ob_length!='' or owb_length!='' or nb_length!='' or tb_length!='' or stb_length!='' ");
				buffer.append("order by (month+0) desc limit 1 ");
			}else{
				//sql = "select id, month, og_length, owg_length, ng_length, tg_length, stg_length from child_growth_master order by (month+0) asc";
				buffer.append("select id, month, og_length, owg_length, ng_length, tg_length, stg_length from child_growth_master ");
				buffer.append("where og_length!='' or owg_length!='' or ng_length!='' or tg_length!='' or stg_length!='' ");
				buffer.append("order by (month+0) desc limit 1 ");
			}
			PreparedStatement preparedStatement = connection.prepareStatement(buffer.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				count = rs.getInt(2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public int getHeightDataCount(String clientId) {
		int count =0;
		try {
			String sql ="select month from child_growth_data where clientid='"+clientId+"' and height is not null  order by (month+0) desc limit 1 ";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				count= rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public int getWeightDataCount(String clientId) {
		int count =0;
		try {
			String sql ="select month from child_growth_data where clientid='"+clientId+"' and weight is not null  order by (month+0) desc limit 1 ";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				count= rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public int getBmiDataCount(String clientId) {
		int count =0;
		try {
			String sql ="select  month from child_growth_data where clientid='"+clientId+"' and bmi is not null order by (month+0) desc limit 1 ";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				count= rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public int getHeadDataCount(String clientId) {
		int count =0;
		try {
			String sql ="select month from child_growth_data where clientid='"+clientId+"' and head_circumfernce is not null order by (month+0) desc limit 1 ";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				count= rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public int getLengthHeightDataCount(String clientId) {
		int count =0;
		try {
			String sql ="select month from child_growth_data where clientid='"+clientId+"' and length is not null order by (month+0) desc limit 1 ";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				count= rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public ArrayList<Client> getClientGrowthHeightList(String clientId, int heightcount, int heightdatacount) {
		ArrayList<Client> arrayList = new ArrayList<Client>();
		try {
			
			for(int i=0; i<heightcount;i++){
				
				if(heightdatacount==i){
					break;
				}
				
				String sql ="select height, weight, bmi, head_circumfernce,length,month from child_growth_data where clientid='"+clientId+"' and month='"+i+"' ";
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet rs = preparedStatement.executeQuery();
				if(rs.next()) {
					Client client = new Client();
					client.setHeightdata(rs.getString(1));
					client.setWeightdata(rs.getString(2));
					client.setBmidata(rs.getString(3));
					client.setHeadcircumferncedata(rs.getString(4));
					client.setLength(rs.getString(5));
					client.setMonth(rs.getString(6));
					String monthyear = getMonthYearofPatient(clientId,i);
					client.setMonthyear(monthyear);
					arrayList.add(client);
				}else{
					Client client = new Client();
					client.setHeightdata("0");
					client.setWeightdata("0");
					client.setBmidata("0");
					client.setHeadcircumferncedata("0");
					client.setLength("0");
					client.setMonth(""+i);
					client.setMonthyear("");
					arrayList.add(client);
				}
			}
			
			/*String sql ="select height, weight, bmi, head_circumfernce,length,month from child_growth_data where clientid='"+clientId+"' order by (month+0) asc ";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			int count =0;
			while (rs.next()) {
				Client client = new Client();
				client.setHeightdata(rs.getString(1));
				client.setWeightdata(rs.getString(2));
				client.setBmidata(rs.getString(3));
				client.setHeadcircumferncedata(rs.getString(4));
				client.setLength(rs.getString(5));
				client.setMonth(rs.getString(6));
				arrayList.add(client);
				count= rs.getInt(6);
			}
			for (; count < heightcount; count++) {
				Client client = new Client();
				client.setHeightdata("0");
				client.setWeightdata("0");
				client.setBmidata("0");
				client.setHeadcircumferncedata("0");
				client.setLength("0");
				client.setMonth(""+count);
				arrayList.add(client);
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	private String getMonthYearofPatient(String clientId, int i) {
		String monthyear =  "";
		try {
			String sql ="select dob from apm_patient where id='"+clientId+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				String str = rs.getString(1);
				if(str!=null){
					String dt = DateTimeUtils.changeDateFormat(str);
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
					Date d=sdf1.parse(dt);
					Calendar cal = Calendar.getInstance();
					cal.setTime(d);
					cal.add(Calendar.MONTH, i);
					monthyear=sdf1.format(cal.getTime());
					String dddt[] = monthyear.split("-");
					monthyear = dddt[1]+"-"+dddt[0];
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return monthyear;
	}

	public ArrayList<Client> getClientGrowthWeightList(String clientId, int heightcount) {
		ArrayList<Client> arrayList = new ArrayList<Client>();
		try {
			
			for(int i =0; i<heightcount;i++){
				String sql ="select height, weight, bmi, head_circumfernce,length,month from child_growth_data where clientid='"+clientId+"' and month='"+i+"' ";
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet rs = preparedStatement.executeQuery();
				if(rs.next()) {
					Client client = new Client();
					client.setHeightdata(rs.getString(1));
					client.setWeightdata(rs.getString(2));
					client.setBmidata(rs.getString(3));
					client.setHeadcircumferncedata(rs.getString(4));
					client.setLength(rs.getString(5));
					client.setMonth(rs.getString(6));
					arrayList.add(client);
				}else{
					Client client = new Client();
					client.setHeightdata("0");
					client.setWeightdata("0");
					client.setBmidata("0");
					client.setHeadcircumferncedata("0");
					client.setLength("0");
					client.setMonth(""+i);
					arrayList.add(client);
				}
			}
			
			
			/*String sql ="select height, weight, bmi, head_circumfernce,length,month from child_growth_data where clientid='"+clientId+"' order by (month+0) asc ";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			int count =0;
			while (rs.next()) {
				Client client = new Client();
				client.setHeightdata(rs.getString(1));
				client.setWeightdata(rs.getString(2));
				client.setBmidata(rs.getString(3));
				client.setHeadcircumferncedata(rs.getString(4));
				client.setLength(rs.getString(5));
				arrayList.add(client);
				count= rs.getInt(6);
			}
			for (; count < heightcount; count++) {
				Client client = new Client();
				client.setHeightdata("0");
				client.setWeightdata("0");
				client.setBmidata("0");
				client.setHeadcircumferncedata("0");
				client.setLength("0");
				client.setMonth(""+count);
				arrayList.add(client);
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public ArrayList<Client> getClientGrowthBmiList(String clientId, int heightcount) {
		ArrayList<Client> arrayList = new ArrayList<Client>();
		try {
			
			for(int i =0; i<heightcount;i++){
				String sql ="select height, weight, bmi, head_circumfernce,length,month from child_growth_data where clientid='"+clientId+"' and month='"+i+"' ";
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet rs = preparedStatement.executeQuery();
				if(rs.next()) {
					Client client = new Client();
					client.setHeightdata(rs.getString(1));
					client.setWeightdata(rs.getString(2));
					client.setBmidata(rs.getString(3));
					client.setHeadcircumferncedata(rs.getString(4));
					client.setLength(rs.getString(5));
					client.setMonth(rs.getString(6));
					arrayList.add(client);
				}else{
					Client client = new Client();
					client.setHeightdata("0");
					client.setWeightdata("0");
					client.setBmidata("0");
					client.setHeadcircumferncedata("0");
					client.setLength("0");
					client.setMonth(""+i);
					arrayList.add(client);
				}
			}
			
			/*String sql ="select height, weight, bmi, head_circumfernce,length,month from child_growth_data where clientid='"+clientId+"' order by (month+0) asc ";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			int count =0;
			while (rs.next()) {
				Client client = new Client();
				client.setHeightdata(rs.getString(1));
				client.setWeightdata(rs.getString(2));
				client.setBmidata(rs.getString(3));
				client.setHeadcircumferncedata(rs.getString(4));
				client.setLength(rs.getString(5));
				client.setMonth(rs.getString(6));
				arrayList.add(client);
				count= rs.getInt(6);
			}
			for (; count < heightcount; count++) {
				Client client = new Client();
				client.setHeightdata("0");
				client.setWeightdata("0");
				client.setBmidata("0");
				client.setHeadcircumferncedata("0");
				client.setLength("0");
				client.setMonth(""+count);
				arrayList.add(client);
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public ArrayList<Client> getClientGrowthHeadList(String clientId, int heightcount) {
		ArrayList<Client> arrayList = new ArrayList<Client>();
		try {
			
			for(int i =0; i<heightcount;i++){
				String sql ="select height, weight, bmi, head_circumfernce,length,month from child_growth_data where clientid='"+clientId+"' and month='"+i+"' ";
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet rs = preparedStatement.executeQuery();
				if(rs.next()) {
					Client client = new Client();
					client.setHeightdata(rs.getString(1));
					client.setWeightdata(rs.getString(2));
					client.setBmidata(rs.getString(3));
					client.setHeadcircumferncedata(rs.getString(4));
					client.setLength(rs.getString(5));
					client.setMonth(rs.getString(6));
					arrayList.add(client);
				}else{
					Client client = new Client();
					client.setHeightdata("0");
					client.setWeightdata("0");
					client.setBmidata("0");
					client.setHeadcircumferncedata("0");
					client.setLength("0");
					client.setMonth(""+i);
					arrayList.add(client);
				}
			}
			
			/*String sql ="select height, weight, bmi, head_circumfernce,length,month from child_growth_data where clientid='"+clientId+"' order by (month+0) asc ";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			int count =0;
			while (rs.next()) {
				Client client = new Client();
				client.setHeightdata(rs.getString(1));
				client.setWeightdata(rs.getString(2));
				client.setBmidata(rs.getString(3));
				client.setHeadcircumferncedata(rs.getString(4));
				client.setLength(rs.getString(5));
				client.setMonth(rs.getString(6));
				arrayList.add(client);
				count= rs.getInt(6);
			}
			for (; count < heightcount; count++) {
				Client client = new Client();
				client.setHeightdata("0");
				client.setWeightdata("0");
				client.setBmidata("0");
				client.setHeadcircumferncedata("0");
				client.setLength("0");
				client.setMonth(""+count);
				arrayList.add(client);
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}

	public ArrayList<Client> getClientGrowthLengthList(String clientId, int heightcount) {
		ArrayList<Client> arrayList = new ArrayList<Client>();
		try {
			
			for(int i =0; i<heightcount;i++){
				String sql ="select height, weight, bmi, head_circumfernce,length,month from child_growth_data where clientid='"+clientId+"' and month='"+i+"' ";
				PreparedStatement preparedStatement = connection.prepareStatement(sql);
				ResultSet rs = preparedStatement.executeQuery();
				if(rs.next()) {
					Client client = new Client();
					client.setHeightdata(rs.getString(1));
					client.setWeightdata(rs.getString(2));
					client.setBmidata(rs.getString(3));
					client.setHeadcircumferncedata(rs.getString(4));
					client.setLength(rs.getString(5));
					client.setMonth(rs.getString(6));
					arrayList.add(client);
				}else{
					Client client = new Client();
					client.setHeightdata("0");
					client.setWeightdata("0");
					client.setBmidata("0");
					client.setHeadcircumferncedata("0");
					client.setLength("0");
					client.setMonth(""+i);
					arrayList.add(client);
				}
			}
			
			/*String sql ="select height, weight, bmi, head_circumfernce,length,month from child_growth_data where clientid='"+clientId+"' order by (month+0) asc ";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			int count =0;
			while (rs.next()) {
				Client client = new Client();
				client.setHeightdata(rs.getString(1));
				client.setWeightdata(rs.getString(2));
				client.setBmidata(rs.getString(3));
				client.setHeadcircumferncedata(rs.getString(4));
				client.setLength(rs.getString(5));
				client.setMonth(rs.getString(6));
				arrayList.add(client);
				count= rs.getInt(6);
			}
			for (; count < heightcount; count++) {
				Client client = new Client();
				client.setHeightdata("0");
				client.setWeightdata("0");
				client.setBmidata("0");
				client.setHeadcircumferncedata("0");
				client.setLength("0");
				client.setMonth(""+count);
				arrayList.add(client);
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrayList;
	}
	public String getClinicAbrivationFromUserid(String databasename) {
		  PreparedStatement preparedStatement = null;
		  String result = "";
		  String sql = "select abrivation from apm_user where userid = '"+databasename+"' ";
		  
		  try{
		   preparedStatement = connection.prepareStatement(sql);
		   ResultSet rs = preparedStatement.executeQuery();
		   if(rs.next()){
		    result = rs.getString(1);
		   }
		   
		  }catch (Exception e) {
		   e.printStackTrace();
		  }
		  return result;
		 }

	public ArrayList<Client> getallFeedbacks(String treatmenttype) {
		ArrayList<Client> list= new  ArrayList<Client>();
	PreparedStatement ps= null;
	String sql="";
	if(treatmenttype.equals("ipd"))
	{
		sql="select id, name from feedback_child_master where isipd='1'";
	}
	if(treatmenttype.equals("opd")||treatmenttype.equals("hd")){
		sql="select id, name from feedback_child_master where isopd='1'";
	}
		try {
		ps= connection.prepareStatement(sql);
		ResultSet rs= ps.executeQuery();
		while(rs.next()){
			Client client= new Client();
			client.setFeedbackid(rs.getString(1));
			client.setFeedbackname(rs.getString(2));
			list.add(client);
		}
	} catch (Exception e) {
	e.printStackTrace();
	}
		return list;
	}

	public int saveOPDFeedBackParent(Client client) {
	int result=0;
	PreparedStatement ps= null;
	try {
		String sql="insert into feedback_form(opdid,date,feedback,ishd)values(?,?,?,?)";
		ps= connection.prepareStatement(sql);
		ps.setString(1, client.getPatientID());
		ps.setString(2, client.getDate());
		ps.setString(3, client.getManualfeedback());
		ps.setInt(4, client.getIshd());
		result= ps.executeUpdate();
		
		ResultSet rs1=ps.getGeneratedKeys();
		if(rs1.next()){
			result= rs1.getInt(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	
		return result;
	}

	public String getparentId(Client client) {
		String result="";
		PreparedStatement ps= null;
		try {
		 String sql="select id from feedback_form where opdid ='"+client.getPatientID()+"'  and date='"+client.getDate()+"' ";
		 ps= connection.prepareStatement(sql);
		 ResultSet rs = ps.executeQuery();
		 while(rs.next()){
			 result=rs.getString(1);
		 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public int saveChildFeedBack(Client client) {
		// TODO Auto-generated method stub
		int result=0;
		PreparedStatement ps= null;
		try {
			if(client.getRating()==null){
				client.setRating("0");
			}
			String sql="insert into feedback_child_form(parentid,masterid,name, rating)values(?,?,?,?)";
			ps= connection.prepareStatement(sql);
		ps.setString(1, client.getFeedbackparentid());
		ps.setString(2, client.getFeedbackid());
		ps.setString(3, client.getFeedbackname());
		ps.setString(4, client.getRating());
			result= ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
			return result;
		}

	public int saveIPDFeedBackParent(Client client) {
		int result=0;
		PreparedStatement ps= null;
		try {
			String sql="insert into feedback_form(ipdid,date,feedback)values(?,?,?)";
			ps= connection.prepareStatement(sql);
			ps.setString(1, client.getPatientID());
			ps.setString(2, client.getDate());
			ps.setString(3, client.getManualfeedback());
			result= ps.executeUpdate();
			
			ResultSet rs1=ps.getGeneratedKeys();
			while(rs1.next()){
				result= rs1.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
			return result;
	}

	public String getparentIdIPD(Client client) {
		String result="";
		PreparedStatement ps= null;
		try {
		 String sql="select id from feedback_form where ipdid ='"+client.getPatientID()+"'  and date='"+client.getDate()+"' ";
		 ps= connection.prepareStatement(sql);
		 ResultSet rs = ps.executeQuery();
		 while(rs.next()){
			 result=rs.getString(1);
		 }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public ArrayList<Client> getpatientlistForFeedback(String type){
		ArrayList<Client> list= new ArrayList<Client>();
		PreparedStatement ps = null;
		StringBuffer buffer = new StringBuffer();
		String sql= "";
		try {
			if(type.equals("opd")||type.equals("hd")){
				buffer.append("select concat(b.firstname, ' ', b.surname)name , a.id ");
				buffer.append(" from apm_available_slot a ");
				buffer.append(" inner join apm_patient b on b.id= a.clientid ");
				buffer.append(" where a.procedures =0 ");
				buffer.append(" group by name ");
				sql=buffer.toString();
			}
			if(type.equals("ipd")){
				buffer.append(" select concat(b.firstname, ' ', b.surname)name, a.id from ipd_addmission_form a ");
				buffer.append(" inner join apm_patient b on b.id= a.clientid ");
				buffer.append(" group by name ");

			
				sql=buffer.toString();
			}
			
			ps= connection.prepareStatement(sql);
			ResultSet rs= ps.executeQuery();
			while(rs.next()){
			Client client= new Client();
			client.setId(rs.getInt(2));
			client.setFullname(rs.getString(1));
			list.add(client);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

	public ArrayList<Client> getallfeedbacksbyPatient(String dept) {
		PreparedStatement ps= null;
		ArrayList<Client> list= new ArrayList<Client>();
		try {
			StringBuffer buffer = new StringBuffer();
			if(dept.equals("ipd")){
			buffer.append(" SELECT a.id, c.firstname, c.surname,a.ipdid, a.date, a.feedback  FROM feedback_form a ");
			buffer.append(" inner join ipd_addmission_form b on b.id = a.ipdid ");
			buffer.append(" inner join apm_patient c on c.id= b.clientid ");
			}
			
			if(dept.equals("opd")||dept.equals("hd")){
				buffer.append(" SELECT a.id, c.firstname, c.surname,a.opdid ,a.date, a.feedback  FROM feedback_form a ");
				buffer.append(" inner join apm_available_slot b on b.id = a.opdid ");
				buffer.append(" inner join apm_patient c on c.id= b.clientid ");
				if(dept.equals("hd")){
					buffer.append(" where ishd='1' ");
				}else{
					buffer.append(" where ishd='0' ");
				}
				
			}
			
			String sql="";
			sql= buffer.toString();
			ps= connection.prepareStatement(sql);
			ResultSet rs= ps.executeQuery();
			while(rs.next()){
				Client client= new Client();
				client.setFeedbackparentid(rs.getString(1));
				String n1= rs.getString(2);
				String n2= rs.getString(3);
				client.setClientName(n1+" "+n2);
				//ipd id is taken for opd id and ipd id both
				client.setIpdid(rs.getString(4));
				String date= DateTimeUtils.getCommencingDate1(rs.getString(5));
				client.setDate(date);
				client.setFeedbackname(rs.getString(6));
				client.setRating(avgOfAllFeedBack(client.getFeedbackparentid()).getRating());
				client.setTotal(avgOfAllFeedBack(client.getFeedbackparentid()).getTotal());
				list.add(client);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<Client> printfeedbackForm(String parentid) {
		ArrayList<Client> list= new  ArrayList<Client>();
		PreparedStatement ps = null;
		try {
			StringBuffer buffer = new StringBuffer();
			buffer.append(" select name, rating from feedback_child_form where parentid='"+parentid+"' ");
		String sql="";
		sql= buffer.toString();
		ps=connection.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			Client client= new Client();
			client.setRating(rs.getString(2));
			client.setFeedbackname(rs.getString(1));
			list.add(client);
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public String getProceFromClientId(String clientid) {
		String procedure ="";
		try {
			String sql ="select procedures from apm_available_slot where procedures!='0' and clientId='"+clientid+"' order by id desc limit 1 ";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				procedure = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return procedure;
	}
	
	
	public ArrayList<Client> getAllPatientSorted(int id) {
		PreparedStatement preparedStatement = null;
		ArrayList<Client>list = new ArrayList<Client>();
		String sql="";
		StringBuffer buf= new StringBuffer();
		buf.append( "select apm_patient.id,title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,refrence,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,whopay,policyauthorzcode,policyno,knownAs,county,homeNo,workNo,emailCc,prefContactMode,emergencyContName,emergencyContNo,patientType,apm_patient.status,note,oldClientId,apm_patient.lastModified,gp_id,imgname,casualtyid,adhno,middlename,mbalance,abrivationid,ipd_addmission_form.id from apm_patient");

		buf.append(" inner join ipd_addmission_form on  ipd_addmission_form.clientid= apm_patient.id ");
		buf.append(" where apm_patient.status=1 order by ipd_addmission_form.id desc limit 20 ");
		sql= buf.toString();
		/*StringBuffer sql = new StringBuffer();
		sql.append("select distinct apm_patient.id,title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,refrence,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,apm_patient.whopay,policyauthorzcode,policyno,knownAs,county,homeNo,workNo,emailCc,prefContactMode,emergencyContName,emergencyContNo,patientType,apm_patient.status,note,oldClientId,lastModified ");
		sql.append("from apm_patient inner join apm_available_slot on  apm_available_slot.clientid = apm_patient.id ");
		sql.append("order by apm_available_slot.id desc limit 10");*/
		
		try{
			
			preparedStatement = connection.prepareStatement(sql.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Client client = new Client();
				client.setId(rs.getInt(1));
				client.setTitle(rs.getString(2));
				client.setFirstName(rs.getString(3));
				client.setLastName(rs.getString(4));
				client.setMobNo(rs.getString(5));
				client.setEmail(rs.getString(6));
				client.setGender(rs.getString(7));
				client.setDob(rs.getString(8));
				client.setAddress(rs.getString(9));
				/*String city = rs.getString(10);
				client.setCity(city);
				client.setTown(getcityfromid(city));*/
				client.setTown(rs.getString(10));
				
				String age =DateTimeUtils.getAge(client.getDob());
				client.setAge(Integer.parseInt(age));
				
				client.setCountry(rs.getString(11));
				client.setPostCode(rs.getString(12));
				client.setReference(rs.getString(13));
				client.setSourceOfIntro(rs.getString(14));
				client.setType(rs.getString(15));
				client.setTypeName(rs.getString(16));
				
				
				String thirdPartyType = getType(rs.getString(15));
				String thirdPartyTypeName = getTypeName(rs.getString(16));
				
				
				
				
				client.setThirdPartyType(thirdPartyType);
				client.setThirdPartyTypeName(thirdPartyTypeName);
				client.setOccupation(rs.getString(17));
				client.setExpiryDate(rs.getString(18));
				client.setWhopay(rs.getString(19));
				client.setPolicyAuthorzCode(rs.getString(20));
				client.setPolicyNo(rs.getString(21));
				client.setKnownAs(rs.getString(22));
				client.setCounty(rs.getString(23));
				client.setHomeNo(rs.getString(24));
				client.setWorkNo(rs.getString(25));
				client.setEmailCc(rs.getString(26));
				client.setPrefContactMode(rs.getString(27));
				client.setEmergencyContName(rs.getString(28));
				client.setEmergencyContNo(rs.getString(29));
				client.setPatientType(rs.getString(30));
				client.setStatus(rs.getBoolean(31));
				client.setNote(rs.getString(32));
				client.setOldclientId(rs.getString(33));
				client.setLastModified(rs.getString(34));
				client.setGpid(rs.getString(35));
				client.setImageName(rs.getString(36));
				client.setCasualtyid(rs.getString(37));
				client.setAdhno(rs.getString(38));
				client.setMiddlename(rs.getString(39));
				client.setBalance(rs.getString(40));
				client.setIspharmacy("0");
				client.setAbrivationid(rs.getString(41));
				client.setIpdid(rs.getString(42));
				list.add(client);
			}
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
		
		
	}

	public String getClientWeight(String clientid) {
		String weight ="0";
		try {
			String sql ="select weight from his_bmi where clientid='"+clientid+"'";
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				weight = ""+rs.getDouble(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return weight;
	}

	public ArrayList<Client> getbdaylistPatients(String dob,LoginInfo logininfo) {
		//dob has to be in dd/mm format
		ArrayList<Client> list= new ArrayList<Client>();
		PreparedStatement ps= null;
		try {
		String sql="select concat(title,' ',firstname,' ',surname),mobno,id from apm_patient where dob like '%"+dob+"%'  ";
		ps= connection.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		 SmsService s = new SmsService();
		while(rs.next()){
			Client client = new Client();
			client.setClientName(rs.getString(1));
			client.setMobNo(rs.getString(2));
			client.setId(rs.getInt(3));
			boolean flag= issmssent(client.getId());
			if(!flag){
				int issent= updatesmssent(client.getId());
				String message= logininfo.getClinicName() +"  wishing you a very Happy Birthday "+client.getClientName();
				s.sendSms(message, client.getMobNo(), logininfo, new EmailLetterLog());
				
			}
			list.add(client);
		}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

private boolean issmssent(int clientid){
	boolean res= false;
	PreparedStatement ps= null;
	try {
		String sql="select smsdate from apm_patient where id='"+clientid+"' ";
		ps = connection.prepareStatement(sql);
		String x="";
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		
		String Date = dateFormat.format(cal.getTime());
		
		ResultSet rs= ps.executeQuery();
		while(rs.next()){
			x=rs.getString(1);
			if(x!=null){
				if(x.equals(Date)){
					res= true;
				}
			}
		}
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}	

private int updatesmssent(int id ){
	PreparedStatement ps= null;
	int res=0;
	try {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		
		String Date = dateFormat.format(cal.getTime());
		
		String sql="update apm_patient set smsdate='"+Date+"' where id='"+id+"' ";
		ps= connection.prepareStatement(sql);
		res= ps.executeUpdate();
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}

public int savefollowup(Client client) {
	int res=0;
	PreparedStatement ps= null;
	try {
		String sql="";
		if(client.getType().equals("1")){
			sql=" insert into followup_data(ipdid,clientid,practitionerid,followupdate,givendate,location_name) value(?,?,?,?,?,?) ";
		}
		if(client.getType().equals("2")){
			sql=" insert into followup_data(opdid,clientid,practitionerid,followupdate,givendate,location_name) value(?,?,?,?,?,?) ";
		}
		
		
		 ps= connection.prepareStatement(sql);
		 if(client.getType().equals("1")){
			 ps.setString(1, client.getIpdid());
		 }
		 if(client.getType().equals("2")){
			 ps.setString(1, client.getOpdid());
		 }
		 ps.setString(2, client.getClientId());
		 ps.setString(3, client.getPractid());
		 ps.setString(5,client.getDate());
		 ps.setString(4, client.getFollowupdate());
		 ps.setString(6, client.getLocation());
		 res= ps.executeUpdate();
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}

public ArrayList<Client> getallfollowupsToDash(String practid, String type, String fromdate, String todate) {
	ArrayList<Client> list= new ArrayList<Client>();
	PreparedStatement ps= null;
	fromdate= DateTimeUtils.getCommencingDate1(fromdate);
	todate= DateTimeUtils.getCommencingDate1(todate);
	try {
		StringBuffer buffer= new StringBuffer();
		buffer.append(" select a.id,a.clientid,a.followupdate, a.givendate,concat(b.title,' ',b.firstname,' ',b.surname),concat('Dr. ',c.firstname,' ',c.lastname),a.sms,a.status,b.mobno");
		buffer.append(" from (SELECT * FROM followup_data ORDER BY id DESC) as a  ");	
		buffer.append("  inner join apm_patient b on b.id= a.clientid  ");
		buffer.append(" inner join apm_user c on c.id = a.practitionerid  ");
		buffer.append("    where a.followupdate between '"+fromdate+"' and '"+todate+"'  group by a.clientid ORDER BY a.id DESC");
		ps= connection.prepareStatement(buffer.toString());
		ResultSet rs= ps.executeQuery();
		
		while(rs.next()){
			Client  client= new  Client();
			client.setId(rs.getInt(1));
			client.setClientId(rs.getString(2));
			client.setFollowupdate(DateTimeUtils.getCommencingDate1(rs.getString(3)));
			client.setDate(DateTimeUtils.getCommencingDate1(rs.getString(4)));
			client.setClientName(rs.getString(5));
			client.setDiaryUser(rs.getString(6));
			client.setState(""+rs.getInt(7));
			if(rs.getInt(8)==0){
				client.setFstatus("New");
			}else if(rs.getInt(8)==1){
				client.setFstatus("DND");
			}else{
				client.setFstatus("Done");
			} 
			client.setMobNo(rs.getString(9));
			list.add(client);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return list;
}

public int setfollowupsmsflag(String id) {
	PreparedStatement ps = null;
	int res=0;
	try {
		String sql="update followup_data set sms=1 where id='"+id+"' and sms='0'" ;
		ps= connection.prepareStatement(sql);
		res=ps.executeUpdate();
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}

public String gettptypenamebyid(String typeName) {
	String res="";
	PreparedStatement ps= null;
	try {
		String sql="select type from apm_third_party_details where id="+typeName+" ";
		ps = connection.prepareStatement(sql);
		
		ResultSet rs= ps.executeQuery();
		while(rs.next()){
			res=rs.getString(1);
			}
	
}catch (Exception e) {
	e.printStackTrace();
}
	return res;
}

public String getwardnamebyid(String wardid) {
	String res="";
	PreparedStatement ps= null;
	try {
		String sql="select wardname from apm_ipd_ward where id="+wardid+" ";
		ps = connection.prepareStatement(sql);
		
		ResultSet rs= ps.executeQuery();
		while(rs.next()){
			res=rs.getString(1);
			}
	
}catch (Exception e) {
	e.printStackTrace();
}
	return res;
}

public int setFollowupStatus(String id, String status,String followupdate) {
	if(followupdate==null){
		followupdate="";
	}
	if(!status.equals("2")){
		followupdate="";
	}
	 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Calendar cal = Calendar.getInstance();
	   
	    String toDate = dateFormat.format(cal.getTime());
	int res=0;
	try {
		
		StringBuffer buffr= new StringBuffer();
		buffr.append(" update followup_data set status='"+status+"'  ");
		if(!followupdate.equals("")){
			buffr.append(" ,followupdate='"+DateTimeUtils.getCommencingDate1(followupdate)+"' , givendate='"+toDate+"' ");
		}
		buffr.append(" where id='"+id+"' ");
		PreparedStatement ps= connection.prepareStatement(buffr.toString());
		res= ps.executeUpdate();
	} catch (Exception e) {
		e.printStackTrace();
	}
	return res;
}

public ArrayList<Client> getApmtNameList(String mastername, String chargecolumnname, String tpid) {
	ArrayList<Client> list= new ArrayList<Client>();
	PreparedStatement ps= null;
	try {
		String sql="select name,id,charges from apm_appointment_type where chargeType='"+mastername+"' and tpid='"+tpid+"'";
		ps=connection.prepareStatement(sql);
		ResultSet rs=ps.executeQuery();
		while(rs.next()){
			Client  client= new  Client();
			client.setMastername(rs.getString(1)+" ("+rs.getString(3)+")");
			client.setMasterid(rs.getString(2));
			list.add(client);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return list;
}

public Client getAppointmentType(String masterid, String chargecolumnname) {
	PreparedStatement ps= null;
	Client client=new Client();
	try {
		
		String sql="select id,name,"+chargecolumnname+",code,wardid,stdcharge from apm_appointment_type where id='"+masterid+"' ";
		ps = connection.prepareStatement(sql);
		
		ResultSet rs= ps.executeQuery();
		while(rs.next()){
			client.setAppointmentid(rs.getString(1));
			client.setApmtname(rs.getString(2));
			client.setApmtcharges(rs.getString(3));
			client.setApmtcode(rs.getString(4));
			client.setWardid(rs.getInt(5));
			client.setStdcharge(rs.getInt(6));
			}
	
}catch (Exception e) {
	e.printStackTrace();
}
	return client;
}

public int updateAssessment(Client client2, String assesmentid,String ipdid) {

	PreparedStatement preparedStatement = null;
	int result = 0;
	StringBuffer buffer=new StringBuffer();
	
	buffer.append("update apm_invoice_assesments set apmtType=?,charge=?,ipdid=? ");
	if(client2.getStdcharge()==1){
		buffer.append(",std_charge_Id=? ");
		}else{
			buffer.append(",chargeId=? ");
		}
	if(client2.getWardid()!=0){
	buffer.append(",wardid=? ");
	}
	buffer.append("where id="+assesmentid+"");
	try{
		preparedStatement = connection.prepareStatement(buffer.toString());
	
		preparedStatement.setString(1, client2.getApmtname());
		
		preparedStatement.setString(2, client2.getApmtcharges());
		preparedStatement.setString(3, client2.getAppointmentid());
		preparedStatement.setString(4, ipdid);
		if(client2.getWardid()!=0){
		preparedStatement.setInt(5, client2.getWardid());
		}
		result = preparedStatement.executeUpdate();

	}
	catch (Exception e) {
		e.printStackTrace();
	}
	return result;

}

public String getopdnotes(String opdid) {
	String notes="";
	try {
		String sql="select notes from apm_available_slot where id='"+opdid+"' ";
		PreparedStatement ps= connection.prepareStatement(sql);
		ResultSet rs= ps.executeQuery();
		while(rs.next()){
			notes=rs.getString(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return notes;
}

public Client getBMIData(String clientid,String appointmentid) {
	Client client= new  Client();
	try {
		String sql="select   height, weight, bmi, pulse, sysbp, diabp ,sugarfasting, postmeal, tempr, spo,head_cir from his_bmi where clientid='"+clientid+"' and apmtid='"+appointmentid+"'";
		PreparedStatement ps= connection.prepareStatement(sql);
		ResultSet rs= ps.executeQuery();
		while (rs.next()) {
			client.setHeight(rs.getString(1));
			client.setWeight(rs.getString(2));
			client.setBmi(rs.getString(3));
			client.setPulse(rs.getString(4));
			client.setSysbp(rs.getString(5)); 
			client.setDiabp(rs.getString(6));
			client.setSugarfasting(rs.getString(7));
			client.setPostmeal(rs.getString(8));
			
			client.setTemprature(DateTimeUtils.isNull(rs.getString(9)));
			client.setSpo(DateTimeUtils.isNull(rs.getString(10)));
			client.setHead_cir(DateTimeUtils.isNull(rs.getString(11)));
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return client;
}

public Client getChildGrowthData(String clientId, String val, String month) {
	
	Client client = new Client();
		try {
			String sql="";
			if(val.equals("height")){
				sql = "select month,clientid,height,height_dt,height_userid from child_growth_data where month='"+month+"' and clientid='"+clientId+"'";
			}else if(val.equals("weight")){
				sql = "select month,clientid,weight,weight_dt,weight_userid from  child_growth_data where month='"+month+"' and clientid='"+clientId+"'";
			}else if(val.equals("bmi")){
				sql = "select month,clientid,bmi,bmi_dt,bmi_userid from child_growth_data where month='"+month+"' and clientid='"+clientId+"'";
			}else if(val.equals("headcircumfernce")){
				sql = "select month,clientid,head_circumfernce,head_circumfernce_dt,head_circumfernce_userid from  child_growth_data where month='"+month+"' and clientid='"+clientId+"'";
			}else if(val.equals("length")){//length, length_dt, length_userid
				sql = "select month,clientid,length,length_dt,length_userid from child_growth_data where month='"+month+"' and clientid='"+clientId+"'";
			}
			PreparedStatement ps=connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			client.setCgddata("");
			while (rs.next()) {
				client.setMonth(""+rs.getInt(1));
				client.setClientid(rs.getString(2));
				if(val.equals("height")){
					client.setHeightdata(rs.getString(3));
				}else if(val.equals("weight")){
					client.setWeightdata(rs.getString(3));
				}else if(val.equals("bmi")){
					client.setBmidata(rs.getString(3));
				}else if(val.equals("headcircumfernce")){
					client.setHeadcircumferncedata(rs.getString(3));
				}else if(val.equals("length")){
					client.setLength(rs.getString(3));
				}
				client.setCgddata(rs.getString(3));
				client.setDate(rs.getString(4));
				client.setUserid(rs.getString(5));
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return client;
	
}

public ArrayList<Client> getVaccinListdataOfAll(String fromdate, String todate, String type) {
	ArrayList<Client> list= new  ArrayList<Client>();
	try {
		StringBuffer buffer= new StringBuffer();
		buffer.append("     ");
		buffer.append("     ");
		buffer.append("     ");
	} catch (Exception e) {
		e.printStackTrace();
	}
	return list;
}

public void startFollowUpSMSProcess(Connection connection, String date) {
	try {
		PreparedStatement ps=connection.prepareStatement("");
	} catch (Exception e) {
		e.printStackTrace();
	}
	
}



public ArrayList<Client> getAllPatientByApmtid(int id) {
	PreparedStatement preparedStatement = null;
	ArrayList<Client>list = new ArrayList<Client>();
	String sql = "select id,title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,refrence,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,whopay,policyauthorzcode,policyno,knownAs,county,homeNo,workNo,emailCc,prefContactMode,emergencyContName,emergencyContNo,patientType,status,note,oldClientId,lastModified,gp_id,imgname,casualtyid,adhno,middlename,mbalance,abrivationid from apm_patient where status=1 and id="+id+"";
	
	/*StringBuffer sql = new StringBuffer();
	sql.append("select distinct apm_patient.id,title,firstname,surname,mobno,email,gender,dob,address,town,country,postcode,refrence,sourceofintro,third_party_id,third_party_name_id,occupation,expiryDate,apm_patient.whopay,policyauthorzcode,policyno,knownAs,county,homeNo,workNo,emailCc,prefContactMode,emergencyContName,emergencyContNo,patientType,apm_patient.status,note,oldClientId,lastModified ");
	sql.append("from apm_patient inner join apm_available_slot on  apm_available_slot.clientid = apm_patient.id ");
	sql.append("order by apm_available_slot.id desc limit 10");*/
	
	try{
		
		preparedStatement = connection.prepareStatement(sql.toString());
		ResultSet rs = preparedStatement.executeQuery();
		while(rs.next()){
			Client client = new Client();
			client.setId(rs.getInt(1));
			client.setTitle(rs.getString(2));
			client.setFirstName(rs.getString(3));
			client.setLastName(rs.getString(4));
			client.setMobNo(rs.getString(5));
			client.setEmail(rs.getString(6));
			client.setGender(rs.getString(7));
			client.setDob(rs.getString(8));
			client.setAddress(rs.getString(9));
			/*String city = rs.getString(10);
			client.setCity(city);
			client.setTown(getcityfromid(city));*/
			client.setTown(rs.getString(10));
			
			String age =DateTimeUtils.getAge(client.getDob());
			client.setAge(Integer.parseInt(age));
			
			client.setCountry(rs.getString(11));
			client.setPostCode(rs.getString(12));
			client.setReference(rs.getString(13));
			client.setSourceOfIntro(rs.getString(14));
			client.setType(rs.getString(15));
			client.setTypeName(rs.getString(16));
			
			
			String thirdPartyType = getType(rs.getString(15));
			String thirdPartyTypeName = getTypeName(rs.getString(16));
			
			
			
			
			client.setThirdPartyType(thirdPartyType);
			client.setThirdPartyTypeName(thirdPartyTypeName);
			client.setOccupation(rs.getString(17));
			client.setExpiryDate(rs.getString(18));
			client.setWhopay(rs.getString(19));
			client.setPolicyAuthorzCode(rs.getString(20));
			client.setPolicyNo(rs.getString(21));
			client.setKnownAs(rs.getString(22));
			client.setCounty(rs.getString(23));
			client.setHomeNo(rs.getString(24));
			client.setWorkNo(rs.getString(25));
			client.setEmailCc(rs.getString(26));
			client.setPrefContactMode(rs.getString(27));
			client.setEmergencyContName(rs.getString(28));
			client.setEmergencyContNo(rs.getString(29));
			client.setPatientType(rs.getString(30));
			client.setStatus(rs.getBoolean(31));
			client.setNote(rs.getString(32));
			client.setOldclientId(rs.getString(33));
			client.setLastModified(rs.getString(34));
			client.setGpid(rs.getString(35));
			client.setImageName(rs.getString(36));
			client.setCasualtyid(rs.getString(37));
			client.setAdhno(rs.getString(38));
			client.setMiddlename(rs.getString(39));
			client.setBalance(rs.getString(40));
			client.setIspharmacy("0");
			client.setAbrivationid(rs.getString(41));
			list.add(client);
		}
		
		
	}catch (Exception e) {
		e.printStackTrace();
	}
	return list;
	
	
}

public int documentId(String docName) {
	int result=0;
	try {
		String sql=" select id from his_document_names where name=?";
		PreparedStatement ps= connection.prepareStatement(sql);
		ps.setString(1, docName);
		ResultSet rs=ps.executeQuery();
		while (rs.next()) {
			result=rs.getInt(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return result;
}

public String documetData(String docId) {
	String result="";
	try {
		String sql=" select name from his_document_names where id=?";
		PreparedStatement ps= connection.prepareStatement(sql);
		ps.setString(1, docId);
		ResultSet rs=ps.executeQuery();
		while (rs.next()) {
			result=rs.getString(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return result;
}

public void addToHISDocumentLog(String docId, String docValue, String clientId) {
	try {
		if(DateTimeUtils.convertToInteger(docId)>0&&!DateTimeUtils.isNull(docValue).equals("")){
			String sql="insert into his_document_log(docid,docname,docdata,clientid) values(?,?,?,?)";
			PreparedStatement ps=connection.prepareStatement(sql);
			ps.setString(1, docId);
			ps.setString(2, documetData(docId));
			ps.setString(3, docValue);
			ps.setString(4, clientId);
			int res=ps.executeUpdate();
		}
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}

public String documentValueFromLog(String docName, String clientId) {
	String result="";
	try {
		String sql=" select docdata from his_document_log where docname=? and clientid=? order by id desc limit 1";
		PreparedStatement ps= connection.prepareStatement(sql);
		ps.setString(1, docName);
		ps.setString(2, clientId);
		ResultSet rs=ps.executeQuery();
		while (rs.next()) {
			result=rs.getString(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return result;
}

public Client feebackDetails(String parentId) {
	Client client= new Client();
	try {
		String sql="select * from feedback_form where id="+parentId;
		PreparedStatement ps= connection.prepareStatement(sql);
		ResultSet rs=ps.executeQuery();
		while (rs.next()) {
			/*id, clientid, ipdid, opdid, feedback, rating, date, ishd*/
			client.setId(rs.getInt("id"));
			client.setClientId(""+rs.getInt("clientid"));
			client.setIpdid(""+rs.getInt("ipdid"));
			client.setOpdid(""+rs.getInt("opdid"));
			client.setDate(DateTimeUtils.getCommencingDate1(rs.getString("date")));
			client.setIshd(rs.getInt("ishd"));
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return client;
}

public int clientIdFromIPDOPD(String Type, String id) {
	int clientId=0;
	try {
		String sql="";
		if(Type.equals("IPD")){
			sql="select clientid from ipd_addmission_form where id='"+id+"'";
		}else{
			sql="select clientId from apm_available_slot where id='"+id+"'";
		}
		PreparedStatement ps= connection.prepareStatement(sql);
		ResultSet rs=ps.executeQuery();
		while (rs.next()) {
			clientId=rs.getInt(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return clientId;
}

public Client avgOfAllFeedBack(String feedBackId) {
	Client client= new Client();
	String sql="select sum(rating),count(*)*5 from feedback_child_form where rating !=0 and parentid ='"+feedBackId+"'";
	try {
		PreparedStatement ps=connection.prepareStatement(sql);
		ResultSet rs=ps.executeQuery();
		while (rs.next()) {
			client.setRating(""+rs.getInt(1));
			client.setTotal(rs.getInt(2));
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return client;
}

//public String drmobno(int practitionerid) {
//	PreparedStatement preparedStatement = null;
//	String result = "";
//	String sql = "SELECT mobile FROM apm_user where id="+practitionerid+" ";
//	
//	try{
//		
//		preparedStatement = connection.prepareStatement(sql);
//		ResultSet rs = preparedStatement.executeQuery();
//		if(rs.next()){
//			result = rs.getString(1);
//		}
//	}catch (Exception e) {
//		e.printStackTrace();
//	}
//	return result;
//}

}




