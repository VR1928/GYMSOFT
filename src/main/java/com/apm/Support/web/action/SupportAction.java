package com.apm.Support.web.action;

import java.io.BufferedReader;
import java.io.File;

import java.sql.Connection;
import java.sql.DriverManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;


import com.apm.DiaryManagement.web.action.SmsService;
import com.apm.Log.eu.entity.EmailLetterLog;

import com.apm.Registration.eu.bi.ClinicDAO;

import com.apm.Registration.eu.blogic.jdbc.JDBCClinicDAO;

import com.apm.Registration.eu.entity.Clinic;
import com.apm.Registration.eu.entity.UserProfile;
import com.apm.Support.eu.bi.UserAdministartionDAO;
import com.apm.Support.eu.blogic.jdbc.JDBCUserAdministration;
import com.apm.Support.eu.entity.Support;
import com.apm.Support.web.form.SupportForm;

import com.apm.common.utils.DateTimeUtils;
import com.apm.common.utils.Pagination;
import com.apm.common.web.action.BaseAction;
import com.apm.common.web.common.helper.LoginHelper;
import com.apm.common.web.common.helper.LoginInfo;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;

import atg.taglib.json.util.JSONObject;


public class SupportAction extends BaseAction implements ModelDriven<SupportForm> {

	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
	HttpServletResponse response = (HttpServletResponse) ActionContext.getContext()
			.get(ServletActionContext.HTTP_RESPONSE);
	HttpSession session = request.getSession(true);
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	SupportForm supportForm = new SupportForm();
	private Pagination pagination = new Pagination(20, 1);

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}

	public SupportForm getModel() {
		return supportForm;
	}

	public String execute() throws Exception {
		return super.execute();
	}

	public String linkup() {
		Connection connection = null;
		try {

			String ownclinicid = request.getParameter("clinicid");
			String ownuserid = request.getParameter("ownuserid");

			Class.forName("com.mysql.jdbc.Driver");

			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcsadmin", "pranams", "6qxi5x&)~XBZ");
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "linkup";
	}

	public String requestqueue() {
		String ownclinicid = request.getParameter("clinicid");
		String ownuserid = request.getParameter("ownuserid");
		Cookie cookie = new Cookie("clinicid", ownclinicid);
		Cookie cookie2 = new Cookie("ownuserid", ownuserid);
		response.addCookie(cookie);
		response.addCookie(cookie2);
		session.setAttribute("clinicid", ownclinicid);
		session.setAttribute("ownuserid", ownuserid);

		return "requestqueue1";
	}

	public String requestqueue1() {
		Connection connection = null;
		try {
			String ownclinicid = "", ownuserid = "";
			Cookie cookies[] = request.getCookies();
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("clinicid")) {
					ownclinicid = cookie.getValue();
				}
				if (cookie.getName().equals("ownuserid")) {
					ownuserid = cookie.getValue();
				}

			}

			connection = getDemoConnection();
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);

			UserAdministartionDAO userAdministartionDAO = new JDBCUserAdministration(connection);
			int count = userAdministartionDAO.countOfUserRequest(ownclinicid, ownuserid, "");
			pagination.setPreperties(count);
			ArrayList<UserProfile> list = new ArrayList<UserProfile>();
			list = userAdministartionDAO.getAllSupportRequest(ownclinicid, ownuserid, "", pagination);
			supportForm.setSupportrequestlist(list);
			supportForm.setTotalcount(count);
			supportForm.setUsername(ownuserid);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "requestqueue";
	}

	public String adminrequestqueue() {

		if(!verifyLogin(request)){
			return "login";
		}

		if(!loginInfo.isSupport_Access()){
			return "login";
		}
		
		
		Connection connection = null;
		
		String clinicid = supportForm.getClinicid();
		String status = supportForm.getStatus();
		String fromdate = supportForm.getFromdate();
		String todate = supportForm.getTodate();
		if (clinicid == null) {
			clinicid = "";
		}
		if (status == null) {
			status = "";
		}
		if (fromdate == null) {
			fromdate = "";
		}
		if (todate == null) {
			todate = "";
		}
		if (fromdate.equals("")) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -7);
			fromdate = dateFormat.format(cal.getTime());
			supportForm.setFromdate(fromdate);
		}
		if (todate.equals("")) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();
			todate = dateFormat.format(cal.getTime());
			supportForm.setTodate(todate);
		}

		try {
			/* fixed=2, WIP=1,requested=0 , Not Posible=3 */
			connection = getDemoConnection();
			UserAdministartionDAO userAdministartionDAO = new JDBCUserAdministration(connection);
			ArrayList<UserProfile> list = new ArrayList<UserProfile>();
			int total = userAdministartionDAO.totalSupportRequest(clinicid, "", status, fromdate, todate);
			pagination.setPreperties(total);
			supportForm.setTotalcount(total);
			list = userAdministartionDAO.getAllSupportRequestToAdmin(clinicid, "", status, fromdate, todate,
					pagination);
			supportForm.setSupportrequestlist(list);
			ArrayList<Support> executivelist = userAdministartionDAO.getExcutiveList();
			supportForm.setExecutivelist(executivelist);
			int fixed = 0, requseted = 0, notPossible = 0, wip = 0;
			fixed = userAdministartionDAO.totalSupportRequest(clinicid, "", "2", fromdate, todate);
			requseted = userAdministartionDAO.totalSupportRequest(clinicid, "", "0", fromdate, todate);
			notPossible = userAdministartionDAO.totalSupportRequest(clinicid, "", "3", fromdate, todate);
			wip = userAdministartionDAO.totalSupportRequest(clinicid, "", "1", fromdate, todate);
			supportForm.setWip(wip);
			supportForm.setNotPossible(notPossible);
			supportForm.setFixed(fixed);
			supportForm.setRequseted(requseted);
			ArrayList<Clinic> cliniclist = new ArrayList<Clinic>();
			cliniclist = userAdministartionDAO.getAllClinic();
			supportForm.setCliniclist(cliniclist);
			supportForm.setModuleList(userAdministartionDAO.getModuleMasterList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "adminrequestqueue";
	}

	public String setsupportreqstatus() {
		String ticketid = request.getParameter("ticketid");
		String status = request.getParameter("status");
		Connection connection = null;
		try {
			connection = getDemoConnection();
			UserAdministartionDAO userAdministartionDAO = new JDBCUserAdministration(connection);
			int res = userAdministartionDAO.setUserreqStatus(ticketid, status);
			if(status!=null){
				if(status.equals("2")){
					Support support= new Support();
					support=userAdministartionDAO.getSupportQueryData(ticketid);
					SmsService s = new SmsService();
					DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				      Calendar cal = Calendar.getInstance();
				      String date = dateFormat.format(cal.getTime());  
					String msg="Smartcare support ticket no: SC"+ticketid+" Fixed on "+date+".You can check status online from Support>View Tickets ";
			        s.sendSms(msg, support.getMobno(), loginInfo, new EmailLetterLog());	
				}
			}
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String setsupportreqexecutive() {
		String ticketid = request.getParameter("ticketid");
		String name = request.getParameter("name");
		Connection connection = null;
		try {
			connection = getDemoConnection();
			UserAdministartionDAO userAdministartionDAO = new JDBCUserAdministration(connection);
			int res = userAdministartionDAO.setexcutiveSupport(name, ticketid);
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String setsupportremark() {
		String ticketid = request.getParameter("ticketid");
		String remark = request.getParameter("remark");
		String prority = request.getParameter("priority");
		Connection connection = null;
		try {
			connection = getDemoConnection();
			UserAdministartionDAO userAdministartionDAO = new JDBCUserAdministration(connection);
			int res = userAdministartionDAO.saveSupportRemark(remark, ticketid, prority);
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(res);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String showdetailsreuest() {
		Connection connection = null;
		try {
			String ticketid = request.getParameter("ticketid");
			connection = getDemoConnection();
			UserAdministartionDAO userAdministartionDAO = new JDBCUserAdministration(connection);
			ArrayList<Support> executivelist = userAdministartionDAO.getExcutiveList();
			ArrayList<Support> messagelist = userAdministartionDAO.getMsgList(ticketid);
			Support support = userAdministartionDAO.getSupportQueryData(ticketid);
			supportForm.setClinicid(support.getClinicid());
			supportForm.setUserid(support.getUserid());
			supportForm.setQuery(support.getQuery());
			supportForm.setQuerytype(support.getQuerytype());
			supportForm.setMessagelist(messagelist);
			supportForm.setExecutivelist(executivelist);
			supportForm.setTicketid(ticketid);
			userAdministartionDAO.setReadAllMsg(ticketid, "readbyadmn");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "showdetailsreuest";
	}

	public String sendmsg() {
		Connection connection = null;
		try {
			String ticketid = request.getParameter("ticketid");
			String msg = request.getParameter("msg");
			String who = request.getParameter("who");
			connection = getDemoConnection();
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			String date = dateFormat.format(cal.getTime());

			UserAdministartionDAO userAdministartionDAO = new JDBCUserAdministration(connection);
			userAdministartionDAO.sendMsg(ticketid, who, msg, date,"");
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String requestdetails() {
		Connection connection = null;
		try {
			String ticketid = request.getParameter("ticketid");
			connection = getDemoConnection();
			UserAdministartionDAO userAdministartionDAO = new JDBCUserAdministration(connection);
			ArrayList<Support> messagelist = userAdministartionDAO.getMsgList(ticketid);
			Support support = userAdministartionDAO.getSupportQueryData(ticketid);
			supportForm.setClinicid(support.getClinicid());
			supportForm.setUserid(support.getUserid());
			supportForm.setQuery(support.getQuery());
			supportForm.setQuerytype(support.getQuerytype());
			supportForm.setMessagelist(messagelist);

			supportForm.setTicketid(ticketid);
			userAdministartionDAO.setReadAllMsg(ticketid, "readbyuser");
		} catch (Exception e) {

		}
		return "requestdetails";
	}

	public String getalluserlist() {
		String clinicid = request.getParameter("clinicid");
		try {
			Connection connection = getAdminConnection();
			UserAdministartionDAO userAdministartionDAO = new JDBCUserAdministration(connection);
			Clinic clinic = new Clinic();
			clinic = userAdministartionDAO.getClinicDetailsFromMaster(clinicid);
			String list = "";
			if (clinic != null) {
				list = userAdministartionDAO.userListFromThierIp(clinic);
			}

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("" + list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String craeteTicketforsuport() {
		try {
			String query = "", queryType = "", clinicid = "", userid = "", mobile = "", fullname = "", datetime = "",
					module = "";
			clinicid = request.getParameter("clinicids");
			userid = request.getParameter("useres");
			queryType = request.getParameter("query_type");
			query = request.getParameter("query");
			mobile = request.getParameter("mbl");
			module=request.getParameter("modulename");
			String issuetype=request.getParameter("issuetype");
			if (userid != null) {
				if (userid.contains("(")) {
					String str[] = userid.split("\\(");
					userid = str[0];
				}
			}

			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			datetime = dateFormat.format(cal.getTime());
			Connection connection = getDemoConnection();
			UserAdministartionDAO userAdministartionDAO = new JDBCUserAdministration(connection);
			Clinic clinic = new Clinic();
			clinic = userAdministartionDAO.getClinicDetailsFromMaster(clinicid);
			UserProfile userProfile = new UserProfile();
			userProfile = userAdministartionDAO.getUserDetailsForSupport(userid, clinicid, clinic);
			int x=userAdministartionDAO.sendToMainSupport(clinicid, userid, queryType, query, datetime,
					userProfile.getJobtitle(), userProfile.getFullname(),mobile,module,issuetype,"");
			if(x>0){
				SmsService s = new SmsService();
				String dd[]=datetime.split(" ");
				datetime=DateTimeUtils.getCommencingDate1(dd[0])+" "+dd[1];
				String msg="Smartcare support ticket no: SC"+x+" Created on "+datetime+".You can check status online from Support>View Tickets ";
		        s.sendSms(msg, mobile, loginInfo, new EmailLetterLog());	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "craeteTicketforsuport";
	}

	
	public  String loadsupportconversation(){
		try {
			Connection connection= getDemoConnection();
			
			
			BufferedReader br=request.getReader();
			String line="";
			String inputjson="";
			if((line=br.readLine())!=null){
				inputjson=inputjson+line;
			}
			JSONObject jsonObject= new JSONObject(inputjson);
			String ticketid=jsonObject.getString("ticketid");
			String type=DateTimeUtils.isNull(jsonObject.getString("type"));
			UserAdministartionDAO userAdministartionDAO = new JDBCUserAdministration(connection);
			if(type.equals("User")){
				userAdministartionDAO.setReadAllMsg(ticketid, "readbyuser");
			}else if(type.equals("Admin")){
				userAdministartionDAO.setReadAllMsg(ticketid, "readbyadmn");
			}
			
			
			ArrayList<Support> messagelist = userAdministartionDAO.getMsgList(ticketid);
			StringBuffer buffer= new  StringBuffer();
			
			buffer.append("<div class='col-lg-12 col-md-12 col-xs-12 col-sm-12' style='padding:10px;'>");
			for(Support msg:messagelist){
				buffer.append("<div style='min-height:30px;padding:5px;' class='col-lg-12 col-md-12 col-xs-12 col-sm-12'>");
				buffer.append("<b class='col-lg-1 col-md-1 col-xs-1 col-sm-1'>"+msg.getWho()+ "   </b>   ");
				buffer.append("<b class='col-lg-2 col-md-2 col-xs-2 col-sm-2'> [ "+msg.getDatetime()+" ] >>>> </b>");
				if(msg.getMsgtype().equals("File")){
					buffer.append(" <div  class='col-lg-9 col-md-9 col-xs-9 col-sm-9'> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a target='_blank' href='liveData/supportshot/"+msg.getMsgs()+"'><img alt='Not Found' src='liveData/supportshot/"+msg.getMsgs()+"' style='width:170px;height:110px;'></a></div>");
				}else{
					buffer.append("<div  class='col-lg-9 col-md-9 col-xs-9 col-sm-9'> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  "+msg.getMsgs()+ "</div>");
						
				}
				buffer.append("</div>");
			}
			
			buffer.append("<div>");
			buffer.append("<div class='col-lg-6 col-md-6 col-xs-6 col-sm-6'>");
			buffer.append("<input type='text' class='form-control' name='msg"+ticketid+"' id='msg"+ticketid+"'>");
			buffer.append("</div>");
			buffer.append("<div class='col-lg-2 col-md-2 col-xs-2 col-sm-2'>");
			buffer.append("<input type='button' class='btn btn-primary' name='snd"+ticketid+"' value='Send' onclick=sendsupportmsg('"+type+"',"+ticketid+")>");
			buffer.append("</div>");
			
			buffer.append("<div class='col-lg-2 col-md-2 col-xs-2 col-sm-2'>");
			
			buffer.append("</div>");
		
			buffer.append("</div>");
			buffer.append("</div>");
			
			jsonObject.put("datax",buffer.toString());
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+jsonObject.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public String savescreenshot(){
		try {
			Connection connection= getDemoConnection();
			File uploadedFile = supportForm.getSubuploadfiles();
			String filecontenttype = supportForm.getSubuploadfilesContentType();
			
			String ticketId=supportForm.getTicketid();
			String fileName=ticketId+""+supportForm.getSubuploadfilesFileName();
			String path = request.getRealPath("/liveData/supportshot/");
			File uploadFolder = new File(path);
			if (!uploadFolder.exists()) {
				uploadFolder.mkdirs();
			}
			File fileToCreate = new File(path, fileName);
            FileUtils.copyFile(uploadedFile, fileToCreate);
            //saving msg
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			String date = dateFormat.format(cal.getTime());

			UserAdministartionDAO userAdministartionDAO = new JDBCUserAdministration(connection);
			userAdministartionDAO.sendMsg(ticketId, "User", fileName, date,"File");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "savescreenshot";
	}
	
	
	private Connection getAdminConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");

			connection = DriverManager.getConnection("jdbc:mysql://139.162.51.34:3306/admin", "pranams",
					"6qxi5x&)~XBZ");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	private Connection getDemoConnection() {
		Connection connection = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");

			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/pcsadmin", "pranams", "6qxi5x&)~XBZ");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
	

	
}
