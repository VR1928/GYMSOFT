package com.apm.DiaryManagement.eu.blogic.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.apm.DiaryManagement.eu.bi.ClientDAO;
import com.apm.DiaryManagement.eu.bi.FinderDAO;
import com.apm.DiaryManagement.eu.entity.Client;
import com.apm.DiaryManagement.eu.entity.NotAvailableSlot;
import com.apm.DiaryManagement.web.form.NotAvailableSlotForm;
import com.apm.common.eu.blogic.jdbc.JDBCBaseDAO;
import com.apm.common.utils.DateTimeUtils;

public class JDBCFinderDAO extends JDBCBaseDAO implements FinderDAO{
	
	public JDBCFinderDAO(Connection connection){
		this.connection = connection;
		
	}

	public ArrayList<NotAvailableSlot> getFinderList(String clientId,String commencing) {
		PreparedStatement preparedStatement = null;
		ArrayList<NotAvailableSlot>list = new ArrayList<NotAvailableSlot>();
		//String sql = "select id,commencing, diaryuserid,starttime,duration,aptmtype,notes,endtime,location,treatmentEpisodeId,apmslotid,condition_id from apm_available_slot where clientid= "+clientId+" ";
		String sql = "";
		if(!clientId.equals("") && !commencing.equals("")){
			sql = "select id,commencing, diaryuserid,starttime,duration,aptmtype,notes,endtime,location,treatmentEpisodeId,apmslotid,condition_id,status,clientid from apm_available_slot where clientid= "+clientId+" and commencing='"+commencing+"' ";
		}
		else if(!clientId.equals("")){
			sql = "select id,commencing, diaryuserid,starttime,duration,aptmtype,notes,endtime,location,treatmentEpisodeId,apmslotid,condition_id,status,clientid from apm_available_slot where clientid= "+clientId+" ";
		}
		else if(!commencing.equals("")){
			sql = "select id,commencing, diaryuserid,starttime,duration,aptmtype,notes,endtime,location,treatmentEpisodeId,apmslotid,condition_id,status,clientid from apm_available_slot where commencing= '"+commencing+"' ";
		}
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				NotAvailableSlot availableSlot = new NotAvailableSlot();
				availableSlot.setId(rs.getInt(1));
				availableSlot.setCommencing(rs.getString(2));
				
				String date = DateTimeUtils.getCommencingDate1(rs.getString(2));
				availableSlot.setCommencing(date);
				
				String diaryUserName = getDiaryUserName(rs.getInt(3));
				availableSlot.setDiaryUser(diaryUserName);
				availableSlot.setSTime(rs.getString(4));
				availableSlot.setDuration(rs.getString(5));
				String appointmentType = getAppointmentType(rs.getInt(6));
				availableSlot.setApmttypetext(appointmentType);
				availableSlot.setNotes(rs.getString(7));
				availableSlot.setEndTime(rs.getString(8));
				availableSlot.setLocation(rs.getString(9));
				availableSlot.setTreatmentEpisodeId(rs.getString(10));
				availableSlot.setApmtSlotId(rs.getInt(11));
				availableSlot.setCondition(rs.getString(12));
				availableSlot.setStatus(rs.getString(13));
				availableSlot.setApmtType(rs.getString(6));
				availableSlot.setDiaryUserId(rs.getInt(3));
				availableSlot.setClientId(clientId);
				
				
				
				String te = rs.getString(10);
				if(rs.getString(10)!=null ){
					if(!te.equals("0")){
						String treatmentSessions = getTreatmentEpisodeName(availableSlot.getId());
						availableSlot.setTreatmentSessions(treatmentSessions);
					}else{
						availableSlot.setTreatmentSessions("");
					}
					
				}
				
				
				
				boolean iscompleted = getIsCompleted(availableSlot.getId());
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date1 = sdf.parse(availableSlot.getCommencing());
				Date date2 = sdf.parse(DateTimeUtils.getSimpleDateFormat(new Date()));
				
				if(date1.before(date2)){
					availableSlot.setOldata(true);
				}else{
					availableSlot.setOldata(false);
					
				}
				
				if(availableSlot.getStatus().equals("1")){
					availableSlot.setClientName("N/A");
					list.add(availableSlot);
				}else{
					if(!iscompleted){
						ClientDAO clientDAO = new JDBCClientDAO(connection);
						Client client = clientDAO.getClientDetails(rs.getString(14));
						String clientname = client.getTitle() + " " + client.getFirstName() + " " + client.getLastName();
						availableSlot.setClientName(clientname);
						
						list.add(availableSlot);
					}
				}
				
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return list;
	}

	private String getTreatmentEpisodeName(int id) {
		PreparedStatement preparedStatement = null;
		String result = "";
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT apm_available_slot.usedsession,sessions,name FROM apm_available_slot inner join apm_treatment_episode on ");
		sql.append("apm_available_slot.treatmentEpisodeId = apm_treatment_episode.id where apm_available_slot.id = "+id+"");
		
		try{
			preparedStatement = connection.prepareStatement(sql.toString());
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				//2 Session for Back Pain / 1-10
				String usedsessions = rs.getString(1);
				String sessions = rs.getString(2);
				String name = rs.getString(3);
				
				result = sessions + " " + "for" + " " + name + " " + "/" + usedsessions + "-" + sessions;
			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return result;
	}

	private boolean getIsCompleted(int id) {
		PreparedStatement preparedStatement = null;
		boolean result = false;
		String sql = "SELECT * FROM apm_invoice where appointmentid= "+id+" ";
		
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

	private String getAppointmentType(int id) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT name FROM apm_appointment_type where id= "+id+" ";
		
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

	private String getDiaryUserName(int id) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT firstname,lastname FROM apm_user where id= "+id+" ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				result = rs.getString(1) + " " + rs.getString(2);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return result;
	}

}
