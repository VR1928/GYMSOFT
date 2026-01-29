package com.apm.Master.web.action;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import com.apm.Master.eu.bi.MasterDAO;

import com.apm.Master.eu.blogic.jdbc.JDBCMasterDAO;
import com.apm.Master.eu.entity.Master;
import com.apm.Master.web.form.LocationMasterForm;
import com.apm.common.eu.blogic.jdbc.Connection_provider;
import com.apm.common.utils.Pagination;
import com.apm.common.web.action.BaseAction;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class LocationMasterAction extends BaseAction implements ModelDriven<LocationMasterForm>,Preparable{

	LocationMasterForm locationMasterForm = new LocationMasterForm();
	String mastername;
	
	HttpServletRequest request=ServletActionContext.getRequest();
	HttpServletResponse response=ServletActionContext.getResponse();
	HttpSession session=request.getSession();
	Pagination pagination=new Pagination(10,1);
	
		public LocationMasterForm getModel() {
			return locationMasterForm;
		}	
		
		
		
	
	public Pagination getPagination() {
			return pagination;
		}
		public void setPagination(Pagination pagination) {
			this.pagination = pagination;
		}




	public String execute() throws Exception {
		Connection connection=null;
		try {
			connection=Connection_provider.getconnection();
			
			MasterDAO masterDAO = new JDBCMasterDAO(connection);
			int count=masterDAO.getTotalLocationCount();
			pagination.setPreperties(count);
			
			String searchText = locationMasterForm.getSearchText();
			
			if(searchText!=null){
				if(searchText.equals("")){
					searchText=null;
				}
			}
			
			ArrayList<Master> locationList = masterDAO.getAllLocation(searchText,pagination);
			locationMasterForm.setLocationList(locationList);
			mastername=request.getParameter("selectedid");
			pagination.setPage_records(locationList.size());
			locationMasterForm.setPagerecords(String.valueOf(pagination.getPage_records()));
			locationMasterForm.setTotalRecords(String.valueOf(count));
			if(mastername!=null){
				 session.setAttribute("mastername", mastername);
			} else {
				mastername=(String)session.getAttribute("mastername");
			}
			locationMasterForm.setMastername(mastername);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			connection.close();
		}
		return "locationmaster";
	}
	
	public String add(){
		return "addlocationMaster";	
	}
	
	public String save() throws Exception{
		Connection connection=null;
		try {
			connection = Connection_provider.getconnection();
			MasterDAO masterDAO = new JDBCMasterDAO(connection);
			Master locationMaster = new Master();
			locationMaster.setName(locationMasterForm.getName());
			String a=locationMasterForm.getPharmacycheck();
			locationMaster.setPharmacycheck(locationMasterForm.getPharmacycheck());
			int result = masterDAO.addLocation(locationMaster);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		connection.close();
		}
		return "savelocationMaster";
	}
	

	
	public String edit(){
		Connection connection;
		try {
			connection = Connection_provider.getconnection();
			MasterDAO masterDAO = new JDBCMasterDAO(connection);
			Master master = new Master();
			master.setId(locationMasterForm.getId());
			Master master2 = masterDAO.getlocationinfo(master);
			
			locationMasterForm.setId(master2.getId());
			locationMasterForm.setName(master2.getName());
			
			if(master2.getPharmacycheck()!=null){
				if(master2.getPharmacycheck().equals("0"))
				{
					locationMasterForm.setChecked1("0");
				}
				else
				{
					locationMasterForm.setChecked1("1");
				}
			}
			else
			{
				locationMasterForm.setChecked1("0");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "editlocationMaster";
	}
	
	public String update() throws Exception{
		Connection connection=null;
		try {
			connection = Connection_provider.getconnection();
			MasterDAO masterDAO = new JDBCMasterDAO(connection);
			Master master = new Master();
			master.setId(locationMasterForm.getId());
			master.setName(locationMasterForm.getName());
			String a=locationMasterForm.getPharmacycheck();
			master.setPharmacycheck(locationMasterForm.getPharmacycheck());
			int result = masterDAO.updateLocation(master);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
		connection.close();
		}
		return "updatelocationMaster";
	}

	
	public String delete(){
		Connection connection;
		try {
			connection = Connection_provider.getconnection();
			MasterDAO masterDAO = new JDBCMasterDAO(connection);
			Master master = new Master();
			master.setId(locationMasterForm.getId());
			int i = master.getId();
			int result = masterDAO.deletelocation(master);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "deletedlocationMaster";
	}
	


	public void prepare() throws Exception {
		Connection connection = null;
		
		try { 
			connection = Connection_provider.getconnection();
			MasterDAO masterDAO = new JDBCMasterDAO(connection);
			ArrayList<Master> masterlist = masterDAO.getMasterList();
			locationMasterForm.setMasterlist(masterlist);
			mastername=(String)session.getAttribute("mastername");
			locationMasterForm.setMastername(mastername);
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			connection.close();
		}
	}
	
}
