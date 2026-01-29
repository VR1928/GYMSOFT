package com.apm.DiaryManagement.web.action;


import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.apm.DiaryManagement.eu.bi.ClientDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCClientDAO;
import com.apm.DiaryManagement.eu.entity.Client;
import com.apm.DiaryManagement.web.form.NotAvailableSlotForm;
import com.apm.Master.eu.entity.Master;
import com.apm.common.eu.blogic.jdbc.Connection_provider;
import com.apm.common.utils.DateTimeUtils;
import com.apm.common.web.action.BaseAction;
import com.apm.common.web.common.helper.LoginHelper;
import com.apm.common.web.common.helper.LoginInfo;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

public class VaccinationAction extends BaseAction implements ModelDriven<NotAvailableSlotForm> {
	NotAvailableSlotForm notAvailableSlotForm= new NotAvailableSlotForm();

HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
HttpServletResponse response = (HttpServletResponse) ActionContext.getContext()
		.get(ServletActionContext.HTTP_RESPONSE);
HttpSession session = request.getSession(true);
LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	public NotAvailableSlotForm getModel() {
		return notAvailableSlotForm;
	}
	
public String dashboard(){
	String fromdate=DateTimeUtils.isNull(notAvailableSlotForm.getFromDate());
	String todate= DateTimeUtils.isNull(notAvailableSlotForm.getToDate());
	if(fromdate.equals("")){
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	    Calendar cal = Calendar.getInstance();
	    todate=dateFormat.format(cal.getTime());
	    cal.add(Calendar.DATE, -7);
	    fromdate = dateFormat.format(cal.getTime());
	}
	try {
		Connection connection= Connection_provider.getconnection();
		ClientDAO clientDAO= new JDBCClientDAO(connection);
		
		ArrayList<Client> vaccinlist= new ArrayList<Client>();
		
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return "dashboard";
}	
}