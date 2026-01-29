package com.apm.Emr.web.action;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.apm.DiaryManagement.eu.bi.ClientDAO;
import com.apm.DiaryManagement.eu.bi.NotAvailableSlotDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCClientDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCNotAvailableSlotDAO;
import com.apm.DiaryManagement.eu.entity.Client;
import com.apm.DiaryManagement.eu.entity.DiaryManagement;
import com.apm.Emr.eu.bi.ConsultationNoteDAO;
import com.apm.Emr.eu.bi.EmrDAO;
import com.apm.Emr.eu.blogic.jdbc.JDBCConsultationNoteDAO;
import com.apm.Emr.eu.blogic.jdbc.JDBCEmrDAO;
import com.apm.Emr.web.form.EmrForm;
import com.apm.common.eu.blogic.jdbc.Connection_provider;
import com.apm.common.web.action.BaseAction;
import com.apm.common.web.common.helper.LoginHelper;
import com.apm.common.web.common.helper.LoginInfo;
import com.apm.main.common.constants.Constants;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import com.apm.Emr.eu.entity.Emr;

public class ConsultationNoteAction extends BaseAction implements  Preparable, ModelDriven<EmrForm>{
	
	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
	HttpServletResponse response = (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
	HttpSession session = request.getSession(true);
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	
	EmrForm emrForm = new EmrForm();
	public String execute(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{
			
			connection = Connection_provider.getconnection();
			ConsultationNoteDAO consultationNoteDAO = new JDBCConsultationNoteDAO(connection);
			ArrayList<Emr> consultationNoteList = new ArrayList<Emr>();
			emrForm.setConsultationList(consultationNoteList);
			session.setAttribute("consultationList", consultationNoteList);
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "consultationNoteSucess";
	}
	
	
	public String setClientsajax(){
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			 
			
			int practId = Integer.parseInt(request.getParameter("practId"));
			
			
			ConsultationNoteDAO consultationNoteDAO = new JDBCConsultationNoteDAO(connection);
			 ArrayList<Client>clientList = consultationNoteDAO.getClientList(practId);
			

			 StringBuffer dropDownAjax = new StringBuffer();
				dropDownAjax.append("<select id='clientname' name = 'clientname' class='form-control showToolTip chosen' data-toggle='tooltip' onchange = 'setConditionajax(this.value)'>");
					dropDownAjax.append("<option value = '0'>Select Client </option>");
					if(clientList.size()!=0){
						for(Client client : clientList){
							dropDownAjax.append("<option value = '"+client.getId()+"'>"+client.getClientName()+"</option>");
						}
						
					}
				dropDownAjax.append("</select>");
				response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				
				response.getWriter().write(""+dropDownAjax.toString()+""); 
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public String setClients(){
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			 
			
			int practId = Integer.parseInt(request.getParameter("practId"));
			
			
			ConsultationNoteDAO consultationNoteDAO = new JDBCConsultationNoteDAO(connection);
			 ArrayList<Client>clientList = consultationNoteDAO.getClientList(practId);
			

			 StringBuffer dropDownAjax = new StringBuffer();
				dropDownAjax.append("<select id='clientname' name = 'clientname' class='form-control showToolTip chosen' data-toggle='tooltip' onchange = 'setCondition(this.value)'>");
					dropDownAjax.append("<option value = '0'>Select Client </option>");
					if(clientList.size()!=0){
						for(Client client : clientList){
							dropDownAjax.append("<option value = '"+client.getId()+"'>"+client.getClientName()+"</option>");
						}
						
					}
				dropDownAjax.append("</select>");
				response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				
				response.getWriter().write(""+dropDownAjax.toString()+""); 
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	
	public String setClientConditionajax(){
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			 
			
			int clientId = Integer.parseInt(request.getParameter("clientId"));
			
			
			ConsultationNoteDAO consultationNoteDAO = new JDBCConsultationNoteDAO(connection);
			 ArrayList<Client>conditionList = consultationNoteDAO.getConditionList(clientId);
			

			 StringBuffer dropDownAjax = new StringBuffer();
				dropDownAjax.append("<select onchange='getPatientRecord()' id='condition'  name = 'condition' class='form-control showToolTip chosen' data-toggle='tooltip'>");
					dropDownAjax.append("<option value = '0'>Select Condtition </option>");
					if(conditionList.size()!=0){
						for(Client client : conditionList){
							dropDownAjax.append("<option value = '"+client.getId()+"'>"+client.getTreatmentType()+"</option>");
						}
						
					}
				dropDownAjax.append("</select>");
				response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				
				response.getWriter().write(""+dropDownAjax.toString()+""); 
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public String setClientCondition(){
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			 
			
			int clientId = Integer.parseInt(request.getParameter("clientId"));
			
			
			ConsultationNoteDAO consultationNoteDAO = new JDBCConsultationNoteDAO(connection);
			 ArrayList<Client>conditionList = consultationNoteDAO.getConditionList(clientId);
			

			 StringBuffer dropDownAjax = new StringBuffer();
				dropDownAjax.append("<select id='condition'  name = 'condition' class='form-control showToolTip chosen' data-toggle='tooltip'>");
					dropDownAjax.append("<option value = '0'>Select Condtition </option>");
					if(conditionList.size()!=0){
						for(Client client : conditionList){
							dropDownAjax.append("<option value = '"+client.getId()+"'>"+client.getTreatmentType()+"</option>");
						}
						
					}
				dropDownAjax.append("</select>");
				response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				
				response.getWriter().write(""+dropDownAjax.toString()+""); 
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public String showAll(){
		
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{
			
			connection = Connection_provider.getconnection();
			String diaryUser = request.getParameter("practId");
			String clientname = request.getParameter("clientId");
			String condition = request.getParameter("conditionId");
			
		//	ConsultationNoteDAO consultationNoteDAO = new JDBCConsultationNoteDAO(connection);
		//	ArrayList<Emr> consultationNoteList = consultationNoteDAO.getConsultationNoteList(diaryUser,clientname,condition);
			setConsFormData(diaryUser,clientname,condition);

			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public String redirect(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{
			
			connection = Connection_provider.getconnection();
			ConsultationNoteDAO consultationNoteDAO = new JDBCConsultationNoteDAO(connection);

			String diaryUser = request.getParameter("practitionerId");
			String clientname = request.getParameter("clientId");
			int apmtId = Integer.parseInt(request.getParameter("appointId"));
			String condition = consultationNoteDAO.getCondtion(apmtId);
			 ArrayList<Client>clientList = consultationNoteDAO.getClientList(Integer.parseInt(diaryUser));

			 ArrayList<Client>conditionList = consultationNoteDAO.getConditionList(Integer.parseInt(clientname));

			 emrForm.setClientList(clientList);
			 emrForm.setConditionList(conditionList);
			 
			emrForm.setDiaryUser(diaryUser);
			emrForm.setClientname(clientname);
			emrForm.setCondition(condition);
		//	ConsultationNoteDAO consultationNoteDAO = new JDBCConsultationNoteDAO(connection);
		//	ArrayList<Emr> consultationNoteList = consultationNoteDAO.getConsultationNoteList(diaryUser,clientname,clientname);
			//setConsFormData(diaryUser,clientname,condition);
			ArrayList<Emr> consultationNoteList = consultationNoteDAO.getConsultationNoteList(diaryUser,clientname,condition);
			emrForm.setConsultationList(consultationNoteList);
			session.setAttribute("consultationList", consultationNoteList);

			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return "consultationNoteSucess";
	}
	public String save(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{
			
			connection = Connection_provider.getconnection();
			String practId = request.getParameter("practId");
			String clientId = request.getParameter("clientId");
			String conditionId = request.getParameter("conditionId");
			String consNote = request.getParameter("consNote");
			ConsultationNoteDAO consultationNoteDAO = new JDBCConsultationNoteDAO(connection);
			int result = consultationNoteDAO.saveNote(practId,clientId,conditionId,consNote);
		
			setConsFormData(practId,clientId,conditionId);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public String edit(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{
			
			connection = Connection_provider.getconnection();
			connection=DriverManager.getConnection(""+Constants.DB_HOST+":3306/"+loginInfo.getClinicUserid()+"?useUnicode=true&characterEncoding=UTF-8","pranams","6qxi5x&)~XBZ");
			String id = request.getParameter("id");
			
			ConsultationNoteDAO consultationNoteDAO = new JDBCConsultationNoteDAO(connection);
			String note = consultationNoteDAO.getNote(id);
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+note+""); 
			
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	public String update(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{
			
			connection = Connection_provider.getconnection();
			String id = request.getParameter("id");
			String practId = request.getParameter("practId");
			String clientId = request.getParameter("clientId");
			String conditionId = request.getParameter("conditionId");
			String consNote = request.getParameter("consNote");
			ConsultationNoteDAO consultationNoteDAO = new JDBCConsultationNoteDAO(connection);
			int result = consultationNoteDAO.updateNote(practId,clientId,conditionId,consNote,id);
			setConsFormData(practId,clientId,conditionId);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public String delete(){
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{
			
			connection = Connection_provider.getconnection();
			String id = request.getParameter("id");
			String practId = request.getParameter("practId");
			String clientId = request.getParameter("clientId");
			String conditionId = request.getParameter("conditionId");
			ConsultationNoteDAO consultationNoteDAO = new JDBCConsultationNoteDAO(connection);
			int result = consultationNoteDAO.deleteNote(id);
			setConsFormData(practId,clientId,conditionId);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	private void setConsFormData(String practId, String clientId, String conditionId) {
		Connection connection = null;
		
		try{ 
			
			connection = Connection_provider.getconnection();
			
			StringBuffer str = new StringBuffer();
			
			
			str.append("<table style='margin-left: 20px;'>");
			/*str.append("<thead>");
			str.append("<tr>");
			str.append("<th>History</th>");
			
			str.append("</tr>");
			str.append("</thead>");*/
			ConsultationNoteDAO consultationNoteDAO = new JDBCConsultationNoteDAO(connection);
			ArrayList<Emr> consultationNoteList = consultationNoteDAO.getConsultationNoteList(practId,clientId,conditionId);
			
			for(Emr emr2 : consultationNoteList){
				
				str.append("<tr>");
				str.append("<td style='width: 20%;'>"+emr2.getLastModified()+"</td>");	
				str.append("<td style='width: 20%;'>"+emr2.getPractitionerName()+"</td>");

				
				str.append("</tr>");
				
				str.append("<tr>");
				str.append("<td style='width: 20%;'>Client given massage.</td>");	
				str.append("<td style='width: 100%;'>"+emr2.getDescription()+"</td>"); 
				//str.append("<td style='width: 20%;'></td>");
				str.append("<td><a href ='#' onclick = 'editConsultationNote("+emr2.getId()+")' title = 'Edit' style = 'padding-right: 20px;'> <i class='fa fa-edit'></i> </a></td>");
				str.append("<td><a href ='#' onclick = 'deleteConsultationNote("+emr2.getId()+")' title = 'Delete'> <i class='fa fa-trash-o'></i> </a></td>");
				
				str.append("</tr>");
				str.append("<tr>");
				str.append("<td colspan = '5'><hr size='1' width='100%' style = 'margin-top: 10px;margin-bottom: 10px;border: 0;border-top: 1px solid #60CFD3;;'/></td>");
				str.append("</tr>");
			}
			
			str.append("</table>");			
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+str.toString()+""); 
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	public EmrForm getModel() {
		// TODO Auto-generated method stub
		return emrForm;
	}
	

	public void prepare() throws Exception {
		Connection connection = null;
		try{
			
			connection = Connection_provider.getconnection();
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			
			ArrayList<DiaryManagement>userList = notAvailableSlotDAO.getUserList(loginInfo.getId());
			emrForm.setUserList(userList);
			ConsultationNoteDAO consultationNoteDAO = new JDBCConsultationNoteDAO(connection);

			 ArrayList<Client>clientList = new ArrayList<Client>();
			 ArrayList<Client>conditionList = new ArrayList<Client>();
			 emrForm.setClientList(clientList);
			 emrForm.setConditionList(conditionList);
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
