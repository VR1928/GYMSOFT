package com.apm.Payroll.web.action;

import java.sql.Connection;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.apm.Accounts.eu.bi.AccountsDAO;
import com.apm.Accounts.eu.blogic.jdbc.JDBCAccountsDAO;
import com.apm.DiaryManagement.eu.bi.CompleteAptmDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCCompleteAptmDAO;
import com.apm.DiaryManagement.eu.entity.CompleteAppointment;
import com.apm.Inventory.eu.bi.InventoryProductDAO;
import com.apm.Inventory.eu.blogic.jdbc.JDBCInventoryProductDAO;
import com.apm.Payroll.eu.bi.AttendanceDAO;
import com.apm.Payroll.eu.bi.PayrollEmployeeDAO;
import com.apm.Payroll.eu.bi.PayrollMasterDAO;
import com.apm.Payroll.eu.blogic.jdbc.JDBCAttendanceDAO;
import com.apm.Payroll.eu.blogic.jdbc.JDBCPayrollEmployeeDAO;
import com.apm.Payroll.eu.blogic.jdbc.JDBCPayrollMasterDAO;
import com.apm.Payroll.eu.entity.Attendance;
import com.apm.Payroll.eu.entity.Employee;
import com.apm.Payroll.web.form.AttendanceForm;
import com.apm.Registration.eu.entity.UserProfile;
import com.apm.common.eu.blogic.jdbc.Connection_provider;
import com.apm.common.utils.DateTimeUtils;
import com.apm.common.utils.Pagination;
import com.apm.common.web.action.BaseAction;
import com.apm.common.web.common.helper.LoginHelper;
import com.apm.common.web.common.helper.LoginInfo;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class AttendanceAction extends BaseAction implements ModelDriven<AttendanceForm>,Preparable {

	AttendanceForm attendanceForm=new AttendanceForm();
	
	HttpServletRequest request=ServletActionContext.getRequest();
	HttpServletResponse response=ServletActionContext.getResponse();
	HttpSession session=request.getSession(true);
	LoginInfo loginInfo=LoginHelper.getLoginInfo(request); 
	Pagination pagination = new Pagination(25, 1);

	
	
	public Pagination getPagination() {
		return pagination;
	}


	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}


	public AttendanceForm getModel() {
		return attendanceForm;
	}

	
	@Override
	public String execute() throws Exception {

		if(!verifyLogin(request)){
	
			 return "login";
		}
		
		Connection connection=null;
		try {
			connection=Connection_provider.getconnection();
			AttendanceDAO attendanceDAO=new JDBCAttendanceDAO(connection);
			PayrollMasterDAO payrollMasterDAO = new JDBCPayrollMasterDAO(connection);
            String branchid=attendanceForm.getBranch();
            String searchemp=attendanceForm.getEmpnamesearch();
			Calendar calendar=Calendar.getInstance(Locale.getDefault());
			SimpleDateFormat sdf=new SimpleDateFormat("MMM-yyyy");
			Date date=calendar.getTime();
			String month="";
			
			Calendar cal=Calendar.getInstance(Locale.getDefault());
			SimpleDateFormat sdf1=new SimpleDateFormat("yyyy");
			SimpleDateFormat sdf2=new SimpleDateFormat("MM");
			Date date1=cal.getTime();
			String year=sdf1.format(date1);
			String todaymonth=sdf2.format(date1);
			String newmonth=request.getParameter("month");
			String year1=request.getParameter("year");
			if(year1==null){
				 year=sdf1.format(date1);
			}else{
				year=year1;
			}
			if(newmonth == null){
				month= DateTimeUtils.getMonthName(Integer.parseInt(todaymonth));
				month=month+"-"+year;
				newmonth=String.valueOf(Integer.parseInt(todaymonth)-1);
			}else{
				month=newmonth;
				
				if (newmonth.equals("0")) {
					month="Jan"+ "-" + year;
				} else if (newmonth.equals("1")) {
					month="Feb"+ "-" + year;
				} else if (newmonth.equals("2")) {
					month="March"+ "-" + year;
				} else if (newmonth.equals("3")) {
					month="April"+ "-" + year;
				} else if (newmonth.equals("4")) {
					month="May"+ "-" + year;
				} else if (newmonth.equals("5")) {
					month="June"+ "-" + year;
				} else if (newmonth.equals("6")) {
					month="July"+ "-" + year;
				} else if (newmonth.equals("7")) {
					month="August"+ "-" + year;
				} else if (newmonth.equals("8")) {
					month="September"+ "-" + year;
				} else if (newmonth.equals("9")) {
					month="October"+ "-" + year;
				} else if (newmonth.equals("10")) {
					month="November"+ "-" + year;
				} else if (newmonth.equals("11")) {
					month="December"+ "-" + year;
				}
			}
			String numdate=year+"/"+(Integer.parseInt(newmonth)+1);
			int weekno=calendar.get(Calendar.WEEK_OF_MONTH);
			SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
			String fromdate=dateFormat.format(date);
			String emp_id=request.getParameter("selectedid");
		/*	String month = payrollMasterDAO.monthforSalarySlip(emp_id);*/
			String filter_status = request.getParameter("filter_status");
			
			if(searchemp==null){
				searchemp="";
			}
			String[] temp = month.split("-");

			int iyear = Integer.parseInt(temp[1]);
			int imonth = Integer.parseInt(newmonth);
			int idays = 1;
			if (temp[0].equals("Jan")) {
				filter_status="0";
			} else if (temp[0].equals("Feb")) {
				filter_status="1";
			} else if (temp[0].equals("March")) {
				filter_status="2";
			} else if (temp[0].equals("April")) {
				filter_status="3";
			} else if (temp[0].equals("May")) {
				filter_status="4";
			} else if (temp[0].equals("June")) {
				filter_status="5";
			} else if (temp[0].equals("July")) {
				filter_status="6";
			} else if (temp[0].equals("August")) {
				filter_status="7";
			} else if (temp[0].equals("September")) {
				filter_status="8";
			} else if (temp[0].equals("October")) {
				filter_status="9";
			} else if (temp[0].equals("November")) {
				filter_status="10";
			} else if (temp[0].equals("December")) {
				filter_status="11";
			}
			String result=payrollMasterDAO.getweekdays();
			int sunday=0;
			if(result!=null){
			String temp1[]=result.split(",");
			sunday=getSundays(imonth, iyear,temp1);
			}
			Calendar mycal = new GregorianCalendar(iyear, imonth, idays);
			// Get the number of days in that month
			int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
			int count=attendanceDAO.getAllAttendanceListcount(month,weekno,branchid,searchemp,loginInfo,numdate,daysInMonth);
			pagination.setPreperties(count);
			
			attendanceForm.setTotalmonthday(String.valueOf(daysInMonth));
			daysInMonth=daysInMonth-sunday;
			ArrayList<Attendance> attendanceList=attendanceDAO.getAllAttendanceList(month,weekno,branchid,pagination,searchemp,loginInfo,numdate,daysInMonth); 
			
		    attendanceForm.setAttendanceList(attendanceList);
		    attendanceForm.setTotalRecords(count);
			pagination.setTotal_records(attendanceList.size());
			attendanceForm.setTotalRecords(count);
			attendanceForm.setPagerecords(String.valueOf(pagination.getTotal_records()));
		    

			

//			String[] temp = month.split("-");
//
//			int iyear = Integer.parseInt(temp[1]);
//			int imonth = Integer.parseInt(newmonth);
//			int idays = 1;
//			if (temp[0].equals("Jan")) {
//				filter_status="0";
//			} else if (temp[0].equals("Feb")) {
//				filter_status="1";
//			} else if (temp[0].equals("March")) {
//				filter_status="2";
//			} else if (temp[0].equals("April")) {
//				filter_status="3";
//			} else if (temp[0].equals("May")) {
//				filter_status="4";
//			} else if (temp[0].equals("June")) {
//				filter_status="5";
//			} else if (temp[0].equals("July")) {
//				filter_status="6";
//			} else if (temp[0].equals("August")) {
//				filter_status="7";
//			} else if (temp[0].equals("September")) {
//				filter_status="8";
//			} else if (temp[0].equals("October")) {
//				filter_status="9";
//			} else if (temp[0].equals("November")) {
//				filter_status="10";
//			} else if (temp[0].equals("December")) {
//				filter_status="11";
//			}
//			String result=payrollMasterDAO.getweekdays();
//			int sunday=0;
//			if(result!=null){
//			String temp1[]=result.split(",");
//			sunday=getSundays(imonth, iyear,temp1);
//			}
//			Calendar mycal = new GregorianCalendar(iyear, imonth, idays);
//			// Get the number of days in that month
//			int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
//			attendanceForm.setTotalmonthday(String.valueOf(daysInMonth));
//			daysInMonth=daysInMonth-sunday;
			attendanceForm.setDaysmonth(daysInMonth);
			attendanceForm.setWeekoff(String.valueOf(sunday));
		    attendanceForm.setFilter_status(filter_status);
			calendar.add(Calendar.DAY_OF_MONTH, 6);
			
			date=calendar.getTime();
		
			String todate=dateFormat.format(date);
			
			attendanceForm.setFromdate(fromdate);
			attendanceForm.setTodate(todate);
			attendanceForm.setMonth(newmonth);
			attendanceForm.setWeekno(String.valueOf(weekno));
			attendanceForm.setYear(year);
		
			
			PayrollEmployeeDAO employeeDAO=new JDBCPayrollEmployeeDAO(connection);
			ArrayList<Employee> branchlist=employeeDAO.getAllBranches();
			attendanceForm.setBranchlist(branchlist);
			
			if(branchid!="" && branchid!=null){
				attendanceForm.setBranch(branchid);
			}
			
			
		} catch (Exception e) {
				e.printStackTrace();
		}
		finally{
			connection.close();
		}
	
		return super.execute();
	}
	
	
	public String sort() throws Exception{ 
		
		Connection connection=null;
		
		try {
			connection=Connection_provider.getconnection();
			AttendanceDAO attendanceDAO=new JDBCAttendanceDAO(connection);
			String fromdate=attendanceForm.getFromdate();
			String todate=attendanceForm.getTodate();
			
			SimpleDateFormat sdf=new SimpleDateFormat("dd-MM-yyyy");
			Date date=sdf.parse(fromdate);
			
			Calendar calendar=Calendar.getInstance();
			calendar.setTime(date);
			SimpleDateFormat format=new SimpleDateFormat("MMM-yyyy");
			Date nowdate=calendar.getTime();
		 //	int monthindex= Integer.parseInt(request.getParameter("month"));
		   // String monThArr[]={"Jan","Feb","March","April"};
			
			String month= format.format(nowdate); 
			int weekno=calendar.get(Calendar.WEEK_OF_MONTH);
			
            String branchid=attendanceForm.getBranch_id();
			ArrayList<Attendance> attendanceList=attendanceDAO.getAllAttendanceList(month,weekno,branchid,pagination,"",loginInfo,"",0); 
		    attendanceForm.setAttendanceList(attendanceList);
			
			PayrollEmployeeDAO employeeDAO=new JDBCPayrollEmployeeDAO(connection);
			ArrayList<Employee> branchlist=employeeDAO.getAllBranches();
			attendanceForm.setBranchlist(branchlist);
			
			attendanceForm.setFromdate(fromdate);
			attendanceForm.setTodate(todate);
			attendanceForm.setMonth(month);
			attendanceForm.setWeekno(String.valueOf(weekno));
			
			
			if(branchid!="" && branchid!=null){
				attendanceForm.setBranch(branchid);
			}
			
		} catch (Exception e) {

		   e.printStackTrace();
		}
		finally {
			connection.close();
		}
		
		return super.execute();
	}
	
	
	
	
	
	
	
	public String update() throws Exception{
    	
		if(!verifyLogin(request)){
			return "login";
		}
		int monthindex= 0;
		Connection connection=null;
		try {
			 connection=Connection_provider.getconnection();
			 AttendanceDAO attendanceDAO=new JDBCAttendanceDAO(connection);
			  monthindex= Integer.parseInt(request.getParameter("month"));
			    String monThArr[]={"Jan","Feb","March","April","May","June","July","August","September","October","November","December"};
			    
				String month= monThArr[monthindex]; //format.format(nowdate); 
				
				Calendar calendar=Calendar.getInstance(Locale.getDefault());
			     SimpleDateFormat format=new SimpleDateFormat("yyyy");
			     String year=format.format(calendar.getTime());
			 for(Attendance attendance:attendanceForm.getAttendance()){
				 attendance.setNummonth(String.valueOf(monthindex));
				    attendance.setMonth(month+"-"+year);
				    if(attendance.getDays()!=null){
				    	if(!attendance.getDays().equals("")){
				    		attendanceDAO.addOrUpdateWorkSheet(attendance); 	  
				    	}
				    }
                    
			 }
			 
			 
		} catch (Exception e) {

		   e.printStackTrace();
		   
		}
		
    	return "save";
    }
	
 	public String caldate() {
 		
  		
 	    try {
 	    	
 	    	String fromdate=request.getParameter("fromdate");
 	    	SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-yyyy");
 	    	Date date=dateFormat.parse(fromdate);

 	    	Calendar calendar=Calendar.getInstance();
 	    	calendar.setTime(date);
 	    
 	    	calendar.add(Calendar.DAY_OF_MONTH, 6);
 	    	
 	    	String data=dateFormat.format(calendar.getTime());
 	   
 	    	response.setContentType("text/html");
		    response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(data);
			
		} catch (Exception e) {

		   e.printStackTrace();
		}
 	    finally {
 	    	
 	    }
 	
 		return null;
 	}
	
	
	
	
	public void prepare() throws Exception {
		Connection connection=null;
		try {
			 connection=Connection_provider.getconnection();
		PayrollEmployeeDAO employeeDAO=new JDBCPayrollEmployeeDAO(connection);
		ArrayList<Employee> branchlist=employeeDAO.getAllBranches();
		attendanceForm.setBranchlist(branchlist);
		}catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	public int getSundays(int month,int year, String[] temp1) 
	{ 
	int count=0; 
	Calendar calendar=Calendar.getInstance(); 
	calendar.set(year, month,1); 
	int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); 

	for(int i=1;i<=days;i++) 
	{ 

	calendar.set(year, month, i); 
	int day=calendar.get(Calendar.DAY_OF_WEEK); 
	for (int j = 0; j < temp1.length; j++) {
		int  string = Integer.parseInt(temp1[j]);
		if(day==string) {
			count++ ; 
		}
	}
	

	} 
	return count; 
	} 
/*	public static int getNumberofSundays(String d1,String d2) throws Exception{ //object in Date form
		Date date1=getDate(d1);
		Date date2=getDate(d2);
		Date date3=

		Calendar c1=Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2=Calendar.getInstance();
		c2.setTime(date2);
		int sundays=0;
		while(c1.after(c2)){
		if(c2.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
		sundays++;
		c2.add(Calendar.DATE,1);
		}
		}

		System.out.println("number of days between 2 dates"+sundays);


		return sundays;
		}
*/
	public String dayemployee() {
			Connection connection=null;
			try {
			connection=Connection_provider.getconnection();
			Date date = new Date();  
			SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy"); 
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			SimpleDateFormat formatter1 = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");  
			SimpleDateFormat formatter2 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");  
		    Calendar cal = Calendar.getInstance();
		    String strDate = formatter.format(date);
		    String today=dateFormat.format(cal.getTime());
		    String datetime=formatter2.format(date);
		    AttendanceDAO attendanceDAO=new JDBCAttendanceDAO(connection);
		    PayrollEmployeeDAO employeeDAO=new JDBCPayrollEmployeeDAO(connection);
		    boolean chkentry=attendanceDAO.checktodaysentry(today,loginInfo);
		    String userid=loginInfo.getUserId();
		    String empid=employeeDAO.getEmployeeEmpid(userid);
		    Calendar cal1=Calendar.getInstance(Locale.getDefault());
			SimpleDateFormat sdf1=new SimpleDateFormat("yyyy");
			SimpleDateFormat sdf2=new SimpleDateFormat("MM");
			Date date1=cal1.getTime();
			String year=sdf1.format(date1);
			String todaymonth=sdf2.format(date1);
			String newmonth=request.getParameter("month");
			String year1=request.getParameter("year");
			String attdate=request.getParameter("date");
			String empname=attendanceForm.getEmpnamesearch();
			if(empname==null){
				empname="";
			}
			if(attdate==null){
				attdate="";
			}
			String month="";
			if(year1==null){
				 year=sdf1.format(date1);
			}else{
				year=year1;
			}
			if(newmonth == null){
				month= DateTimeUtils.getMonthName(Integer.parseInt(todaymonth));
				month=month+"-"+year;
				newmonth=String.valueOf(Integer.parseInt(todaymonth)-1);
			}else{
				month=newmonth;
			}
			
			ArrayList<Attendance> attendanceList=attendanceDAO.getAllAttendanceListOfSelf(attdate,newmonth,year,loginInfo,empname);
			attendanceForm.setAttendanceList(attendanceList);
			attendanceForm.setMonth(newmonth);
			attendanceForm.setYear(year);
			if(!chkentry){
	 	    	
			 	
			 	
			 	
			 	String intime = formatter1.format(date);
			 	int res=attendanceDAO.insertPunchIn(empid,userid,"",today,0,"");
			}
			Attendance attendance=attendanceDAO.getListOfAttendance(today,userid,empid);
			attendanceForm.setDate(strDate);
			attendanceForm.setStatus(attendance.getStatus());
			attendanceForm.setIndatetime(attendance.getIndatetime());
			attendanceForm.setOutdatetime(attendance.getOutdatetime());
			if(attendance.getStatus().equals("1")){
			   String tmp[]=attendance.getIndatetime().split(" ");
			   String onlydate=DateTimeUtils.getCommencingDatemmddyyy(tmp[0]);
			   String datewithtime=onlydate+" "+tmp[1];
			   String difftime=attendanceDAO.differenceOftwoTimes(datewithtime, datetime);
			   attendance.setTotalhour(difftime);
			}else if(attendance.getStatus().equals("2")){
			   String tmp[]=attendance.getIndatetime().split(" ");
			   String onlydate=DateTimeUtils.getCommencingDatemmddyyy(tmp[0]);
			   String datewithtime=onlydate+" "+tmp[1];
			   String tmp1[]=attendance.getOutdatetime().split(" ");
			   String onlydate1=DateTimeUtils.getCommencingDatemmddyyy(tmp1[0]);
			   String datewithtime1=onlydate1+" "+tmp1[1];
			   String difftime=attendanceDAO.differenceOftwoTimes(datewithtime, datewithtime1);
			   attendance.setTotalhour(difftime);
			}else{
			   attendance.setTotalhour("0");
		   }
		   attendanceForm.setTotalhour(attendance.getTotalhour());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "dayemployee";
	}
	public String setpunchin(){
		Connection connection=null;
		try {
			 connection=Connection_provider.getconnection();
			 	Date date = new Date();  
	 	    	AttendanceDAO attendanceDAO=new JDBCAttendanceDAO(connection);
	 	    	PayrollEmployeeDAO employeeDAO=new JDBCPayrollEmployeeDAO(connection);
	 	    	DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			 	String status=request.getParameter("status");
			 	String remark=request.getParameter("remark");
			 	String userid=loginInfo.getUserId();
			 	String empid=employeeDAO.getEmployeeEmpid(userid);
			    Calendar cal = Calendar.getInstance();
			    String date1 = dateFormat.format(cal.getTime());
			 	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");  
			 	String intime = formatter.format(date);
			 	if(!status.equals("2")){
			 	boolean chkentry=attendanceDAO.checktodaysentry(date1,loginInfo);
				   if(!chkentry){
					   int res=attendanceDAO.insertPunchIn(empid,userid,intime,date1,1,remark);
				   }else{
					   int res=attendanceDAO.updatePunching(empid,userid,intime,date1,1,"indatetime","remarkintime",remark);
				   }
			 	}else{
			 		int res=attendanceDAO.updatePunching(empid,userid,intime,date1,2,"outdatetime","remarkouttime",remark);
			 	}
	 	    	response.setContentType("text/html");
			    response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write("");
				
			} catch (Exception e) {

			   e.printStackTrace();
			}
		return null;
	}
	public String changepunchtime() throws Exception{
		Connection connection=Connection_provider.getconnection();;
		int res1=0;
		try {
			String id=request.getParameter("id");
			String val=request.getParameter("val");
			String colname=request.getParameter("colname");
			AttendanceDAO attendanceDAO=new JDBCAttendanceDAO(connection);
			Attendance attendance=attendanceDAO.getDayDetails(id);
			res1=attendanceDAO.changepunchtime(id, val,colname,attendance);
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""); 
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			connection.close();
		}
		
		return null;
		
	}
}
