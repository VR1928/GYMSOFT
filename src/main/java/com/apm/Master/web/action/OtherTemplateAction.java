package com.apm.Master.web.action;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.apm.Master.eu.bi.DischargeOutcomeDAO;
import com.apm.Master.eu.bi.MasterDAO;
import com.apm.Master.eu.blogic.jdbc.JDBCDischargeOutcomeDAO;
import com.apm.Master.eu.blogic.jdbc.JDBCMasterDAO;
import com.apm.Master.eu.entity.Master;
import com.apm.Master.web.form.MasterForm;
import com.apm.common.eu.blogic.jdbc.Connection_provider;
import com.apm.common.utils.Pagination;
import com.apm.common.web.action.BaseAction;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class OtherTemplateAction extends BaseAction implements
		ModelDriven<MasterForm>, Preparable {

	MasterForm masterForm = new MasterForm();
	HttpServletRequest request = ServletActionContext.getRequest();
	HttpServletResponse response = ServletActionContext.getResponse();
	HttpSession session=request.getSession(false);
    private String mastername;
    Pagination pagination=new Pagination(25,1);
	
	
	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub

		Connection connection = null;
		try {

			connection = Connection_provider.getconnection();
			MasterDAO masterDAO = new JDBCMasterDAO(connection);
			int count=masterDAO.getOtherTemplateCount();
			pagination.setPreperties(count);
			String searchText = masterForm.getSearchText();
			
			if(searchText!=null){
				if(searchText.equals("")){
					searchText=null;
				}
			}
			
			ArrayList<Master> otherTemplateList = masterDAO
					.getAllOtherTemplateList(searchText,pagination);
			masterForm.setOtherTemplateList(otherTemplateList);
			mastername = request.getParameter("selectedid");
			pagination.setPage_records(otherTemplateList.size());
			masterForm.setPagerecords(String.valueOf(pagination.getPage_records()));
			masterForm.setTotalRecords(count);
			if (mastername != null) {

				session.setAttribute("mastername", mastername);
			} else {
				mastername = (String) session.getAttribute("mastername");
			}
			masterForm.setMastername(mastername);
			
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			connection.close();
		}

		return SUCCESS;
	}

	public String add() {

		if (!verifyLogin(request)) {
			return "login";
		}
		return "add";

	}

	public String save() throws Exception {
		if (!verifyLogin(request)) {
			return "login";
		}
		Connection connection = null;
		try {

			connection=Connection_provider.getconnection();
            Master master=new Master();
            master.setTitle(masterForm.getTitle());
            master.setOther_template_text(masterForm.getOther_template_text());
            master.setDiscipline_id(masterForm.getDiscipline_id());
            
            MasterDAO masterDAO=new JDBCMasterDAO(connection);
            masterDAO.addOtherTemplate(master);         
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			connection.close();
		}

		return "save";
	}

	
	public String edit() throws Exception{
		
		if(!verifyLogin(request)){
			return "login";
		}
	 	
		Connection connection=null;
		
        try {
			
        	connection=Connection_provider.getconnection();
        	String selectedid=request.getParameter("selectedid");
        	MasterDAO masterDAO=new JDBCMasterDAO(connection);
        	Master master=masterDAO.getOtherTemplate(selectedid);
        	masterForm.setId(master.getId());
        	masterForm.setOther_template_text(master.getOther_template_text());
        	masterForm.setTitle(master.getTitle());
        	masterForm.setDiscipline_id(master.getDiscipline_id());
        	
		} catch (Exception e) {

		  e.printStackTrace();
		}		
        finally {
        	connection.close();
        }
		return "edit";
	}
	
	public String update() throws Exception{
		
		Connection connection=null;
		
		try {
		
			connection=Connection_provider.getconnection();
		    Master master=new Master();
		    master.setId(masterForm.getId());
			master.setTitle(masterForm.getTitle());
			master.setOther_template_text(masterForm.getOther_template_text());
			master.setDiscipline_id(masterForm.getDiscipline_id());
			MasterDAO masterDAO=new JDBCMasterDAO(connection);
			int result=masterDAO.updateOtherTemplate(master);			
			
		} catch (Exception e) {

		  e.printStackTrace();
		}
		finally{
			
			connection.close();
		}
	
	return "save";
	} 
	
	public String delete() throws Exception {
		
		Connection connection=null;
		
		try {
			connection=Connection_provider.getconnection();
			String selectedid=request.getParameter("selectedid");
			MasterDAO dao=new JDBCMasterDAO(connection);
			int i=dao.deleteOtherTemplate(selectedid);
			
			
		} catch (Exception e) {

		  e.printStackTrace();
		}
	    finally{
	    	connection.close();
	    }
		return "save";
	}
	

	
	public void prepare() throws Exception {

		Connection con = null;

		try {
			con = Connection_provider.getconnection();
			MasterDAO masterDAO=new JDBCMasterDAO(con);
			DischargeOutcomeDAO outcomeDAO = new JDBCDischargeOutcomeDAO(con);
			ArrayList<Master> masterlist = outcomeDAO.getMasterList();
			masterForm.setMasterlist(masterlist);
			
			
			ArrayList<Master> disciplineList=masterDAO.getDisciplineDataList();
			masterForm.setDisciplineList(disciplineList);
			mastername = (String) session.getAttribute("mastername");
			masterForm.setMastername(mastername);
			
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			con.close();
		}

	}

	public MasterForm getModel() {
		// TODO Auto-generated method stub
		return masterForm;
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

}
