package com.apm.Ipd.eu.blogic.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.apm.DiaryManagement.eu.bi.ClientDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCClientDAO;
import com.apm.Ipd.eu.bi.IpdDAO;
import com.apm.Ipd.eu.bi.IpdLogDAO;
import com.apm.Ipd.eu.entity.Bed;
import com.apm.common.eu.blogic.jdbc.JDBCBaseDAO;
import com.apm.common.utils.DateTimeUtils;

public class JDBCIpdLogDAO  extends JDBCBaseDAO implements IpdLogDAO {
	
	public JDBCIpdLogDAO(Connection connection){
		this.connection = connection;
	}

	public ArrayList<Bed> getAdmissionlogDAO(String clientId) {
		PreparedStatement preparedStatement = null;
		ArrayList<Bed>list = new ArrayList<Bed>();
		String sql = "select admissionid, lastmodified from ipd_admission_log where clientid="+clientId+" ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Bed bed = new Bed();
				bed.setAddmissionid(rs.getString(1));
				if(rs.getString(2)!=null){
					bed.setAdmissiondate(DateTimeUtils.getDBDate(rs.getString(2)));
				}else{
					bed.setAdmissiondate("");
				}
			
				ClientDAO clientDAO = new JDBCClientDAO(connection);
				String dischargedate = clientDAO.getIpdDischargeDate(rs.getString(1));
				if(dischargedate!=null){
					
					bed.setDischargeDate(dischargedate);
				}else{
					bed.setDischargeDate("");
				}
				
				
				list.add(bed);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return list;
	}

	public ArrayList<Bed> getDischargeLog(String clientId) {
		PreparedStatement preparedStatement = null;
		ArrayList<Bed>list = new ArrayList<Bed>();
		String sql = "select admissionid, lastmodified from ipd_discharge_log where clientid="+clientId+" ";
		
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Bed bed = new Bed();
				bed.setAddmissionid(rs.getString(1));
				//bed.setAdmissiondate(rs.getString(2));
				if(rs.getString(2)!=null){
					bed.setAdmissiondate(DateTimeUtils.getDBDate(rs.getString(2)));
				}else{
					bed.setAdmissiondate("");
				}
				
				list.add(bed);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return list;
	}

	public ArrayList<Bed> getBedChangeLogList(String clientId,String admissionid) {
		PreparedStatement preparedStatement = null;
		ArrayList<Bed> list = new ArrayList<Bed>();
		String sql = "SELECT admissionid, wardid, bedid, selectedshiftdata,id FROM ipd_bed_change_log where clientid = "+clientId+" and admissionid ="+admissionid+" and autosetcharge=0 order by id desc";
		
		try{
			
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				Bed bed = new Bed();
				bed.setAddmissionid(rs.getString(1));
				
				IpdDAO ipdDAO = new JDBCIpdDAO(connection);
				String wardname = ipdDAO.getIpdWardName(rs.getString(2));
				bed.setWardname(wardname);
				
				String bedname = ipdDAO.getIpdBedName(rs.getString(3));
				bed.setBedname(bedname);
				
				if(rs.getString(4)!=null){
					bed.setDate(DateTimeUtils.getDBDate(rs.getString(4)));
				}else{
					bed.setDate("");
				}
				
				bed.setId(rs.getInt(5));
				
				list.add(bed);
				
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return list;
	}

	public String getDischargeBedId(String admissionid) {

		String bedid="";
		
		try {
			String sql="SELECT bedid from ipd_discharge_log where admissionid="+admissionid+" and bedid>0";
			PreparedStatement ps=connection.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				 bedid=rs.getString(1);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return bedid;
	}


	public boolean isBedChanged(String admissionid,String clientid) {

		try {
			String sql="SELECT id from ipd_bed_change_log where admissionid="+admissionid+" and clientid="+clientid+"";
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
	
	public String getDischargeDate(int admissionid) {

		String dischargedate="";
		
		try {
			String sql="SELECT lastmodified from ipd_discharge_log where admissionid="+admissionid+"";
			PreparedStatement ps=connection.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				dischargedate=rs.getString(1);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return dischargedate;
	}

}
