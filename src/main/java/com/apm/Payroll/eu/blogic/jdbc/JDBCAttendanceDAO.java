package com.apm.Payroll.eu.blogic.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Locale;

import com.apm.Payroll.eu.bi.AttendanceDAO;
import com.apm.Payroll.eu.bi.PayrollEmployeeDAO;
import com.apm.Payroll.eu.bi.PayrollMasterDAO;
import com.apm.Payroll.eu.entity.Attendance;
import com.apm.Payroll.eu.entity.Employee;
import com.apm.Payroll.eu.entity.Salary;
import com.apm.common.utils.DateTimeUtils;
import com.apm.common.utils.Pagination;
import com.apm.common.web.common.helper.LoginInfo;

public class JDBCAttendanceDAO implements AttendanceDAO{

	Connection connection;
	
	public JDBCAttendanceDAO(Connection connection) {
          
		 this.connection=connection;
	}

	public int addOrUpdateWorkSheet(Attendance attendance) {
		PayrollEmployeeDAO employeeDAO=new JDBCPayrollEmployeeDAO(connection);
		PayrollMasterDAO payrollMasterDAO=new JDBCPayrollMasterDAO(connection);
		int result=0;
		int id=0;
		boolean flag=false;
		String userid="";
	     try {
	    	 String[] temp = attendance.getMonth().split("-");
	    	 int iyear = Integer.parseInt(temp[1]);
				int imonth = Integer.parseInt(attendance.getNummonth());
				int idays = 1;
				String res=payrollMasterDAO.getweekdays();
				int sunday=0;
				if(res!=null){
				String temp1[]=res.split(",");
				sunday=getSundays(imonth, iyear,temp1);
				}
	    	 String sql="select id,emp_id,month,weekno from apm_payroll_weeksheet";
	    	 PreparedStatement ps=connection.prepareStatement(sql);
	    	 
	    	 ResultSet  rs=ps.executeQuery();
	    	 while(rs.next()){
	    		 
	    		 id=rs.getInt(1);
	    		 String emp_id=rs.getString(2);
	    		 userid=employeeDAO.getEmployeeUserId(emp_id);
	    		 String month=rs.getString(3);
	    		 String weekno=rs.getString(4);
	    		 
	    		 if(emp_id!=null && month!=null){
	    			 
	    			  if(emp_id.equals(attendance.getEmp_id()) && month.equals(attendance.getMonth()) ) {
	    				  
	    				   flag=true;
	    				   break;
	    			  }
	    		
	    		 }
	    	 }
	    	
	    	
	    	 /*if(flag){
	    		 
	    		 //update
	    		 
	    		 String query1="update apm_payroll_weeksheet set emp_id=?, branch_id=?, reason=?, month=?, weekno=?, monday=?, tuesday=?, wednesday=?, thursday=?, friday=?, saturday=?, sunday=?, total_hrs=?, notes=?, date=? where id="+id+"";
	    		 PreparedStatement ps1=connection.prepareStatement(query1);
	    		 ps1.setString(1, attendance.getEmp_id());
	    		 ps1.setString(2, attendance.getBranch_id());
	    		 ps1.setString(3, attendance.getReason());
	    		 ps1.setString(4, attendance.getMonth());
	    		 ps1.setString(5, attendance.getWeekno());
	    		 ps1.setString(6, attendance.getMonday());
	    		 ps1.setString(7, attendance.getTuesday());
	    		 ps1.setString(8, attendance.getWednesday());
	    		 ps1.setString(9, attendance.getThursday()); 
	    		 ps1.setString(10, attendance.getFriday());
	    		 ps1.setString(11, attendance.getSaturday());
	    		 ps1.setString(12, attendance.getSunday());
	    		 ps1.setString(13, attendance.getTotal_hour());
	    		 ps1.setString(14, attendance.getNotes());
	    		 ps1.setString(15, attendance.getDate());
	    		
	    		 result=ps1.executeUpdate();
	    	 }*/
if(flag){
	    		 
	    		 //update
	    		 
	    		 String query1="update apm_payroll_weeksheet set emp_id=?, branch_id=?, reason=?, month=?, notes=?, date=?, days=?, totalsalary=?,userid=?,leaves=? where id="+id+"";
	    		 PreparedStatement ps1=connection.prepareStatement(query1);
	    		 ps1.setString(1, attendance.getEmp_id());
	    		 ps1.setString(2, attendance.getBranch_id());
	    		 ps1.setString(3, attendance.getReason());
	    		  ps1.setString(4, attendance.getMonth());
	    		  /*ps1.setString(5, attendance.getWeekno());
	    		 ps1.setString(6, attendance.getMonday());
	    		 ps1.setString(7, attendance.getTuesday());
	    		 ps1.setString(8, attendance.getWednesday());
	    		 ps1.setString(9, attendance.getThursday()); 
	    		 ps1.setString(10, attendance.getFriday());
	    		 ps1.setString(11, attendance.getSaturday());
	    		 ps1.setString(12, attendance.getSunday());
	    		 ps1.setString(13, attendance.getTotal_hour());*/
//	    		  int days=0;
//	    		  if(attendance.getDays()!=null){
//			    		 if(!attendance.getDays().equals("")){
//			    			  days=Integer.parseInt(attendance.getDays())+sunday;
//			    	    	 attendance.setDays(String.valueOf(days));
//			    		 }
//			    	 }
	    		 ps1.setString(5, attendance.getNotes());
	    		 ps1.setString(6, attendance.getDate());
	    		 ps1.setString(7, attendance.getDays());
	    		 ps1.setString(8,attendance.getSalaryTotal());
	    		 ps1.setString(9,userid);
	    		 ps1.setString(10,attendance.getLeaveday());
	    		
	    		 result=ps1.executeUpdate();
	    	 }
	    	 /*else {
	    		 //add
	    		 String query2="insert into apm_payroll_weeksheet (emp_id, branch_id, reason, month, weekno, monday, tuesday, wednesday, thursday, friday, saturday, sunday, total_hrs, notes, date) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	    		 PreparedStatement ps1=connection.prepareStatement(query2);
	    		 ps1.setString(1, attendance.getEmp_id());
	    		 ps1.setString(2, attendance.getBranch_id());
	    		 ps1.setString(3, attendance.getReason());
	    		 ps1.setString(4, attendance.getMonth());
	    		 ps1.setString(5, attendance.getWeekno());
	    		 ps1.setString(6, attendance.getMonday());
	    		 ps1.setString(7, attendance.getTuesday());
	    		 ps1.setString(8, attendance.getWednesday());
	    		 ps1.setString(9, attendance.getThursday()); 
	    		 ps1.setString(10, attendance.getFriday());
	    		 ps1.setString(11, attendance.getSaturday());
	    		 ps1.setString(12, attendance.getSunday());
	    		 ps1.setString(13, attendance.getTotal_hour());
	    		 ps1.setString(14, attendance.getNotes());
	    		 ps1.setString(15, attendance.getDate());
	    		 
	    		 result=ps1.executeUpdate();
	    	 }*/
	    	 
else {
	 //add
	 String query2="insert into apm_payroll_weeksheet (emp_id, branch_id, reason, month, notes, date, days, totalsalary,userid,leaves) values (?,?,?,?,?,?,?,?,?,?)";
	 PreparedStatement ps1=connection.prepareStatement(query2);
	 ps1.setString(1, attendance.getEmp_id());
	 ps1.setString(2, attendance.getBranch_id());
	 ps1.setString(3, attendance.getReason());
	 ps1.setString(4, attendance.getMonth());
	 /*ps1.setString(5, attendance.getWeekno());
	 ps1.setString(6, attendance.getMonday());
	 ps1.setString(7, attendance.getTuesday());
	 ps1.setString(8, attendance.getWednesday());
	 ps1.setString(9, attendance.getThursday()); 
	 ps1.setString(10, attendance.getFriday());
	 ps1.setString(11, attendance.getSaturday());
	 ps1.setString(12, attendance.getSunday());
	 ps1.setString(13, attendance.getTotal_hour());*/
//	 int days=0;
//	  if(attendance.getDays()!=null){
//   		 if(!attendance.getDays().equals("")){
//   			  days=Integer.parseInt(attendance.getDays())+sunday;
//   	    	 attendance.setDays(String.valueOf(days));
//   		 }
//   	 }
	 ps1.setString(5, attendance.getNotes());
	 ps1.setString(6, attendance.getDate());
	 ps1.setString(7, attendance.getDays());
	 ps1.setString(8, attendance.getSalaryTotal());
	 ps1.setString(9, userid);
	 ps1.setString(10,attendance.getLeaveday());
	 result=ps1.executeUpdate();
}
	    	 
		} catch (Exception e) {

		   e.printStackTrace();
		}	 
		
		
		return result;
	}

	public ArrayList<Attendance> getAllAttendanceList(String month, int weekno,String branchid,Pagination pagination, String searchemp,LoginInfo loginInfo,String numdate,int daysInMonth) {

		ArrayList<Attendance> list=new ArrayList<Attendance>();
		PayrollEmployeeDAO employeeDAO=new JDBCPayrollEmployeeDAO(connection);
        String sql="";		
		try {
			
			if(branchid!="" && branchid!=null){
				sql="select empid,name,branchid from apm_payroll_employee where branchid="+branchid+" ";
			}
			else {
			   sql="select empid,name,branchid from apm_payroll_employee ";
		    }
			sql=sql+" "+"where name like('%"+searchemp+"%')";
			if(!loginInfo.isPayrollaccess()){
				sql=sql+" "+" and userid='"+loginInfo.getUserId()+"'"; 
			}
			 if(pagination!=null)
				{
					sql=pagination.getSQLQuery(sql);
				}
			PreparedStatement ps=connection.prepareStatement(sql);
	        ResultSet rs=ps.executeQuery();
	        
	        while(rs.next()){
	        	
	            Attendance attendance=new Attendance();      	
	        	attendance.setEmp_id(rs.getString(1));
	        	attendance.setEmp_name(rs.getString(2));
	        	attendance.setBranch_id(rs.getString(3));
	        	String userid=employeeDAO.getEmployeeUserId(rs.getString(1));
	        	int leavecount=getleavecount(userid,numdate);
	        	attendance=getAttendanceOfMonth(attendance, month, weekno);
	        	attendance.setMonth(month);
	        	attendance.setWeekno(String.valueOf(weekno));
	        	attendance.setLeaveday(String.valueOf(leavecount));
	        	attendance.setWorkwithleave(daysInMonth-leavecount);
	        	String basicsalary= getSalaryForAttendence(attendance.getEmp_id());
	        	attendance.setBasicsalary(basicsalary);
	        	String salaryTotal =totalSalaryForAttendence(attendance.getEmp_id());
	        	attendance.setSalaryTotal(salaryTotal);
	            list.add(attendance);	 
	        		
	        }
			
		
		} catch (Exception e) {

		   e.printStackTrace();
		}
		
		
		
		return list;
	}
	
	private int getleavecount(String userid, String month) {
		int result=0;
		String startdate=month+"/01";
		String enddate=month+"/31";
		try {
			
			String sql="select days from payroll_leave where userid='"+userid+"' and fromdate between '"+startdate+"' and '"+enddate+"' and todate between '"+startdate+"' and '"+enddate+"' and status=1";
			PreparedStatement ps=connection.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()){
				
				 result=rs.getInt(1);   
				
			}
			
		} catch (Exception e) {

		   e.printStackTrace();
		}
	
		return result;

	}

	public Attendance getAttendanceOfMonth(Attendance attendance,String month,int weekno) {
		
		try {
			
	         
			 /*String sql="select id, branch_id, reason, month, weekno, monday, tuesday, wednesday, thursday, friday, saturday, sunday, total_hrs, notes, date from apm_payroll_weeksheet where month='"+month+"' and weekno="+weekno+" and emp_id="+attendance.getEmp_id()+"";*/	
			 StringBuffer buffer = new StringBuffer();
			
			/* String sql="select id, branch_id, reason, month, notes, date, days, totalsalary from apm_payroll_weeksheet where  emp_id="+attendance.getEmp_id()+"";*/
			 buffer.append("select id, branch_id, reason, month, notes, date, days, totalsalary from apm_payroll_weeksheet where  emp_id="+attendance.getEmp_id()+" ");
			 buffer.append("and month='"+month+"' ");
			 PreparedStatement ps=connection.prepareStatement(buffer.toString());
	         ResultSet set=ps.executeQuery();
	         
			 while(set.next()){
				 
				  attendance.setReason(set.getString(3));
				 /* attendance.setMonth(month);*/
				  attendance.setNotes(set.getString(5));
				  attendance.setDate(set.getString(6));
				  attendance.setDays(set.getString(7));
				  attendance.setSalaryTotal(set.getString(8));
				 if(attendance.getMonth()=="" && attendance.getMonth()==null){
					   
					  attendance.setMonth(month);
				  }
				  
				 // attendance.setDate(set.getString(15));
				  
			 }
	          
			
		} catch (Exception e) {

		   e.printStackTrace();
		} 
		
	
		return attendance;
	}

	public int getTotalHours(String month, String emp_id) {

		int result=0;
		
		try {
			
			String sql="select sum(total_hrs) from apm_payroll_weeksheet where emp_id="+emp_id+" and month='"+month+"'";
			PreparedStatement ps=connection.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()){
				
				 result=rs.getInt(1);   
				
			}
			
		} catch (Exception e) {

		   e.printStackTrace();
		}
	
		return result;
	}
	
	public String getSalaryForAttendence(String emp_id) {

		String result="";

		try {
			String sql = "select basic from apm_payroll_salary_master where emp_id='" + emp_id + "'";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

			result = rs.getString(1);
				

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	public String totalSalaryForAttendence(String emp_id){
		
		String result="";
		try {
			String sql="select totalsalary from apm_payroll_weeksheet where emp_id='" + emp_id + "'";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

			result = rs.getString(1);
				

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
		
	}

	public int getAllAttendanceListcount(String month, int weekno, String branchid,String searchemp, LoginInfo loginInfo, String numdate, int daysInMonth) {
		  String sql="";		
		  int res=0;
			try {
				
				if(branchid!="" && branchid!=null){
					sql="select empid,name,branchid from apm_payroll_employee where branchid="+branchid+" ";
				}
				else {
				   sql="select empid,name,branchid from apm_payroll_employee ";
			    }
				sql=sql+" "+"where name like('%"+searchemp+"%')";
				if(!loginInfo.isPayrollaccess()){
					sql=sql+" "+" and userid='"+loginInfo.getUserId()+"'"; 
				}
				PreparedStatement ps=connection.prepareStatement(sql);
		        ResultSet rs=ps.executeQuery();
		        
		        while(rs.next()){
		        	res=rs.getInt(1);
	}
	
}catch (Exception e) {
	e.printStackTrace();
}
			return res;
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

	public Attendance getListOfAttendance(String today,String userid, String empid) {
		Attendance attendance=new Attendance();
		try {
			
			String sql="select emp_id, userid, date, indatetime, outdatetime, totalhour, status from apm_payroll_daily_attendance where date='"+today+"' and userid='"+userid+"' and emp_id='"+empid+"'";
			PreparedStatement ps=connection.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while (rs.next()) {
				attendance.setEmp_id(rs.getString(1));
				attendance.setUserid(rs.getString(2));
				attendance.setDate(rs.getString(3));
				if(rs.getString(4)!=null){
					if(!rs.getString(4).equals("")){
						String temp[]=rs.getString(4).split(" ");
						attendance.setIndatetime(DateTimeUtils.getCommencingDatePayroll(temp[0])+" "+temp[1]);
					}
				}
				if(rs.getString(5)!=null){
					if(!rs.getString(5).equals("")){
						String temp[]=rs.getString(5).split(" ");
						attendance.setOutdatetime(DateTimeUtils.getCommencingDatePayroll(temp[0])+" "+temp[1]);
					}
				}
				attendance.setTotalhour(rs.getString(6));
				attendance.setStatus(rs.getString(7));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attendance;
	}

	public int insertPunchIn(String empid, String userid, String intime,String date,int status,String remark) {
		int res=0;
		try {
			String query="insert into apm_payroll_daily_attendance (emp_id, userid,date, indatetime,status,remarkintime) values (?,?,?,?,?,?)";
			 PreparedStatement ps1=connection.prepareStatement(query);
			 ps1.setString(1, empid);
			 ps1.setString(2, userid);
			 ps1.setString(3, date);
			 ps1.setString(4, intime);
			 ps1.setInt(5, status);
			 ps1.setString(6, remark);
			 res=ps1.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public boolean checktodaysentry(String today,LoginInfo loginInfo) {
		boolean flag = false;
		try {

			String sql = "select * from apm_payroll_daily_attendance where date='" + today + "' and userid='"+loginInfo.getUserId()+"'";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
	}

	public int updatePunching(String empid, String userid, String intime, String date1, int status,String columnname,String remarkcol, String remark) {
int result=0;
		
		try {
			
			String sql="update apm_payroll_daily_attendance set status=?,"+columnname+"=?,"+remarkcol+"=? where date='"+date1+"' and emp_id='"+empid+"' and userid='"+userid+"'";
			PreparedStatement ps=connection.prepareStatement(sql);
			ps.setInt(1, status);
			ps.setString(2, intime);
			ps.setString(3, remark);
			result=ps.executeUpdate();
		} catch (Exception e) {

		   e.printStackTrace();
		}
	
		return result;
	}
	public String differenceOftwoTimes(String dateStart,String dateStop) {
		String result="";
		//HH converts hour in 24 hours format (0-23), day calculation
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

		Date d1 = null;
		Date d2 = null;

		try {
			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);

			//in milliseconds
			long diff = d2.getTime() - d1.getTime();

			long diffSeconds = diff / 1000 % 60;
			long diffMinutes = diff / (60 * 1000) % 60;
			long diffHours = diff / (60 * 60 * 1000) % 24;
			long diffDays = diff / (24 * 60 * 60 * 1000);
			String diffhh="";
			String diffmm="";
			if(diffHours<10){
				diffhh="0"+diffHours;
			}else{
				diffhh=""+diffHours;
			}
			if(diffMinutes<10){
				diffmm="0"+diffMinutes;
			}else{
				diffmm=""+diffMinutes;
			}
			result=diffhh+":"+diffmm;
			System.out.print(diffDays + " days, ");
			System.out.print(diffHours + " hours, ");
			System.out.print(diffMinutes + " minutes, ");
			System.out.print(diffSeconds + " seconds.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
		
	}

	public ArrayList<Attendance> getAllAttendanceListOfSelf(String attdate, String newmonth, String year,LoginInfo loginInfo,String empname) {
		ArrayList<Attendance> list=new ArrayList<Attendance>();
		newmonth=String.valueOf((Integer.parseInt(newmonth)+1));
		if(Integer.parseInt(newmonth)<10){
			newmonth="0"+newmonth;
		}
		String fromdate=year+"/"+newmonth+"/"+"01";
		String todate=year+"/"+newmonth+"/"+"31";
        StringBuffer buffer  = new StringBuffer();		
		try {
			buffer.append("select apm_payroll_daily_attendance.date, indatetime, outdatetime,remarkintime,remarkouttime,apm_payroll_daily_attendance.id,apm_payroll_employee.name from apm_payroll_daily_attendance"
					+ " left join apm_payroll_employee on apm_payroll_employee.empid=apm_payroll_daily_attendance.emp_id where date between '"+fromdate+"' and '"+todate+"' ");
			if(!loginInfo.isPayrollaccess()){
				buffer.append(" and apm_payroll_daily_attendance.userid='"+loginInfo.getUserId()+"'");
			}
			
				if(!attdate.equals("")){
					buffer.append("and apm_payroll_daily_attendance.date='"+DateTimeUtils.getCommencingDatePayroll(attdate)+"' ");
				}
				if(!empname.equals("")){
					buffer.append(" and apm_payroll_employee.name like '%"+empname+"%'");
				}
			PreparedStatement ps = connection.prepareStatement(buffer.toString());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				  Attendance attendance=new Attendance();
				  attendance.setDate(DateTimeUtils.getCommencingDatePayroll(rs.getString(1)));
				  if(rs.getString(2)!=null){
				  String tmp[]=rs.getString(2).split(" ");
				  attendance.setIndatetime(tmp[1]);
				  }
				  if(rs.getString(3)!=null){
				  String tmp1[]=rs.getString(3).split(" ");
				 
				  attendance.setOutdatetime(tmp1[1]);
				  }
				  attendance.setRemarkintime(rs.getString(4));
				  attendance.setRemarkouttime(rs.getString(5));
				  attendance.setId(rs.getInt(6));
				  attendance.setEmp_name(rs.getString(7));
				  list.add(attendance);	 
	        		
	        }
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public ArrayList<Attendance> getMonthWiseReport(String empid, String empname, String month, String year,int daysinmonth,int daysofMonth,Pagination pagination) {
		ArrayList<Attendance> list=new ArrayList<Attendance>();
		PayrollEmployeeDAO employeeDAO=new JDBCPayrollEmployeeDAO(connection);
		String fromdate=year+"/"+month+"/"+"01";
		String todate=year+"/"+month+"/"+"31";
		String sql1="";
		GregorianCalendar date = new GregorianCalendar();      
        int currentmonth = date.get(Calendar.MONTH);
        currentmonth = currentmonth+1;
        int currentyear = Calendar.getInstance().get(Calendar.YEAR);
        StringBuffer sql=new StringBuffer();	
		try {
			
			   sql.append("select empid,name from apm_payroll_employee ");
			   sql.append("inner join apm_payroll_daily_attendance on apm_payroll_daily_attendance.emp_id=apm_payroll_employee.empid ");
			   sql.append("where apm_payroll_daily_attendance.date between '"+fromdate+"' and '"+todate+"' ");
			   if(empid!=null){
				   if(!empid.equals("")){
					   sql.append(" and apm_payroll_employee.empid='"+empid+"'");
				   }
			   }
			   if(empname!=null){
				   if(!empname.equals("")){
					   sql.append(" and apm_payroll_employee.name like '%"+empname+"%'" );
				   }
			   }
			   sql.append("group by empid ");
			   sql1=sql.toString();
			   if(pagination!=null)
				{
					sql1=pagination.getSQLQuery(sql1);
				}
			   PreparedStatement ps = connection.prepareStatement(sql1);

				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					Attendance attendance=new Attendance();
					
					ArrayList<Attendance> subattendancelist=new ArrayList<Attendance>();
					attendance.setEmp_name(rs.getString(2));
					attendance.setEmp_id(rs.getString(1));
					for (int i = 1; i <= daysinmonth; i++) {
						Attendance attendance2=new Attendance();
						int res=0;
						if(month.equals(String.valueOf(currentmonth)) && year.equals(String.valueOf(currentyear))){
						if(i<=daysofMonth){
							res=getSubAttendanceList(i,month,year,rs.getString(1));
							attendance2.setStatus(String.valueOf(res));
							
						}
						}else{
							res=getSubAttendanceList(i,month,year,rs.getString(1));
							attendance2.setStatus(String.valueOf(res));
						}
						subattendancelist.add(attendance2);
					}
					attendance.setSubattendancelist(subattendancelist);
					list.add(attendance);
				}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private int getSubAttendanceList(int i, String month, String year, String empid) {
		ArrayList<Attendance> list=new ArrayList<Attendance>();
		int res=1;
		String temp="";
		if(i<10){
			temp="0"+i;
		}else{
			temp=String.valueOf(i);
		}
		String date=year+"/"+month+"/"+temp;
		try{
			String sql="select * from apm_payroll_daily_attendance where date='"+date+"' and emp_id="+empid+"";
			PreparedStatement ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				res=2;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public int getMonthWiseReportCount(String empid, String empname, String tmp, String year, int daysInMonth,
			int dayOfMonth) {
		String fromdate=year+"/"+tmp+"/"+"01";
		String todate=year+"/"+tmp+"/"+"31";
        StringBuffer sql=new StringBuffer();
        int res=0;
		try {
			
			   sql.append("select empid from apm_payroll_employee ");
			   sql.append("inner join apm_payroll_daily_attendance on apm_payroll_daily_attendance.emp_id=apm_payroll_employee.empid ");
			   sql.append("where apm_payroll_daily_attendance.date between '"+fromdate+"' and '"+todate+"' ");
			   if(empid!=null){
				   if(!empid.equals("")){
					   sql.append(" and apm_payroll_employee.empid='"+empid+"'");
				   }
			   }
			   if(empname!=null){
				   if(!empname.equals("")){
					   sql.append(" and apm_payroll_employee.name like '%"+empname+"%'" );
				   }
			   }
			   sql.append("group by empid ");
			   
			   PreparedStatement ps = connection.prepareStatement(sql.toString());

				ResultSet rs = ps.executeQuery();
				if (rs.last()) {
					res = rs.getRow();
				}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public int changepunchtime(String id, String val,String colname,Attendance attendance) {
		int res=0;
		PreparedStatement ps=null;
		try {
			val=attendance.getDate()+" "+val;
			String sql="update apm_payroll_daily_attendance set "+colname+"='"+val+"' where id="+id+"";
			ps=connection.prepareStatement(sql);
			res=ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public Attendance getDayDetails(String id) {
		Attendance attendance=new Attendance();
		try{
			String sql="select date from apm_payroll_daily_attendance where id="+id+"";
			PreparedStatement ps = connection.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				
				attendance.setDate(rs.getString(1));
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return attendance;
	}
}	
