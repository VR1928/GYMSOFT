package com.apm.common.web.action;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.apm.Registration.eu.bi.ClinicDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCClinicDAO;
import com.apm.common.eu.blogic.jdbc.Connection_provider;
import com.apm.common.web.common.helper.LoginHelper;
import com.apm.common.web.common.helper.LoginInfo;
import com.apm.main.common.constants.Constants;
import com.opensymphony.xwork2.ActionContext;

public class LogoutAction extends BaseAction{
	
	
	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
	
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	
	public String execute() throws Exception {
		

		
		
		/*HttpSession session = request.getSession(true);
		
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		
		String apmuserlist = (String)session.getAttribute("apmuserlist");
		
		String temp[] = apmuserlist.split(",");
		
		StringBuffer str = new StringBuffer();
		
		for(int i=0;i<temp.length;i++){
			if(!temp[i].equals(loginInfo.getUserId())){
				str.append(temp[i] + ",");
			}
		}
		
		  if(str.length()!=0){
	        	String result = str.substring(0, str.length()-1);
	        	session.setAttribute("apmuserlist", result);
	        }*/
		
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			
			//connection = DriverManager.getConnection(""+Constants.DB_HOST+":3306/admin","root","mysql");
			//connection = DriverManager.getConnection(""+Constants.DB_HOST+":3306/admin","pranams","6qxi5x&)~XBZ");
			
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			
		
			int update = clinicDAO.updateLogoutStatus(loginInfo.getUserId());
			
			String userid = loginInfo.getUserId();
			//connection = DriverManager.getConnection(""+Constants.DB_HOST+":3306/admin","pranams","6qxi5x&)~XBZ");
			clinicDAO = new JDBCClinicDAO(connection);
			int del = clinicDAO.deleteApmloggedUser(userid);
			
			if(loginInfo.getUserType()==5){
				LoginHelper.removeLoginInfo(request);
				
				return "esclogout";
			}else{
				LoginHelper.removeLoginInfo(request);
			}
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
			connection.close();
		}
		
		
		
		return SUCCESS;
	}
	
	public String mob(){
		try{
			
			LoginHelper.removeLoginInfo(request);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "esclogout";
	}

}
