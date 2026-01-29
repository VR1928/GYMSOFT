package com.apm.Payroll.eu.blogic.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.apm.DiaryManagement.eu.bi.CompleteAptmDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCCompleteAptmDAO;
import com.apm.Master.eu.entity.Master;
import com.apm.Payroll.eu.bi.PayrollEmployeeDAO;
import com.apm.Payroll.eu.entity.Employee;
import com.apm.common.utils.DateTimeUtils;
import com.apm.common.utils.Pagination;
import com.apm.common.web.common.helper.LoginInfo;

public class JDBCPayrollEmployeeDAO implements PayrollEmployeeDAO {
  
	   Connection connection;
	   public JDBCPayrollEmployeeDAO(Connection connection) {
	
	       this.connection=connection;
	   }
	   
	public ArrayList<Employee> getAllEmployees(String searchText,Pagination pagination,String dept,String searchname,LoginInfo loginInfo,String activestatus) {
		if(dept==null){
			dept="0";
		}if(dept.equals("")){
			dept="0";
		}
		CompleteAptmDAO completeAptmDAO=new JDBCCompleteAptmDAO(connection);
		ArrayList<Employee> list=new ArrayList<Employee>();
		
		try {
		      String sql="";
		      StringBuffer buffer=new StringBuffer();
			   buffer.append("select id,firstname, surname, email,lastModified, mobno,height, weight, gender, fullname, address, package_status,dob,image from apm_patient ");
				boolean flag=false;
		      if(searchText!=null){
		    	  if(!searchText.equals("")){
		    		  if(flag){
		    			  buffer.append(" and id='"+searchText+"' ");
		    		  }else{
		    			  flag=true;
		    		  buffer.append(" where id='"+searchText+"' ");
		    		  }  		
		    	  }
		      }
		      if(searchname!=null){
		    	  if(!searchname.equals("")){
		    		  if(flag){
		    			  buffer.append(" and fullname like '%"+searchname+"%' or id like '%"+searchname+"%' or mobno like '%"+searchname+"%' ");
		    		  }else {
		    			  flag=true;
		    			  buffer.append(" where fullname like '%"+searchname+"%' or id like '%"+searchname+"%' or mobno like '%"+searchname+"%' ");
					}
		    		  
		    	  }
		      }
		      
	    		  
	    	  /*if(!activestatus.equals("")){
	    		  if(flag){
	    			  buffer.append(" and isautocharge="+activestatus+" ");
	    		  }else {
	    			  
	    			  buffer.append(" where  isautocharge="+activestatus+" ");
				}
	    		  
	    	  }*/
		      buffer.append("group by id desc");
			   sql=buffer.toString();			  
			   if(pagination!=null)
				{
					sql=pagination.getSQLQuery(sql);
				}
			   PreparedStatement ps=connection.prepareStatement(sql);
			  ResultSet rs=ps.executeQuery();
			  
			  while(rs.next()) {
				     Employee employee=new Employee();
				     employee.setId(rs.getInt(1));
				     String name= rs.getString(10);
				     employee.setName(name);
//				     email, joining_date, mobile, height, weight, gende
				    employee.setEmail(rs.getString(4));
				     employee.setDate_join(DateTimeUtils.getCommencingDatePayroll(rs.getString(5)));
				     int package_status=rs.getInt(12);
				     String expirydate="";
				     String fromdate="";
				     /*if(package_status==1){
				    	 String packagetodate=getPackageendDate(rs.getInt(1));
				    	 String packagefromdate=getPackagefromDate(rs.getInt(1));
					     String tmp[]=packagetodate.split(" ");
					     String tmp1[]=packagefromdate.split(" ");
				    	 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						 Calendar cal = Calendar.getInstance();
				 		 String today = dateFormat.format(cal.getTime());
			 			 long diff=DateTimeUtils.getDifferenceOfTwoDateDBFormat(today, tmp[0]);
			 			 
			 			   if( diff>0 && diff<0){
			 				   employee.setStatus("1");
			 			   }

				     fromdate=DateTimeUtils.changeDateFormattoPicker(tmp1[0]);
				     expirydate=DateTimeUtils.changeDateFormattoPicker(tmp[0]);
				     }else{
				     String joindate = employee.getDate_join();
				     String tmp[] = joindate.split("/");
				     int dd = Integer.parseInt(tmp[0]);
				     int mm = Integer.parseInt(tmp[1]);
				     int yy = Integer.parseInt(tmp[2]);
				     
				     DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					 Calendar cal = Calendar.getInstance();
					 cal.set(yy, mm, dd);;
					 cal.add(Calendar.MONTH, 0);
					  expirydate = dateFormat.format(cal.getTime());
				     }*/
				     
				     String joindate = employee.getDate_join();
				     String tmp[] = joindate.split("/");
				     int dd = Integer.parseInt(tmp[0]);
				     int mm = Integer.parseInt(tmp[1]);
				     int yy = Integer.parseInt(tmp[2]);
				     
				     DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					 Calendar cal = Calendar.getInstance();
					 cal.set(yy, mm, dd);;
					 cal.add(Calendar.MONTH, 0);
					  expirydate = dateFormat.format(cal.getTime());
					  employee.setExp_date(expirydate);
				    
				    if(package_status==1){
				    	Master master = getpkgdata(employee.getId());
				    	if(!master.getTpkg().equals("0")){
				    		String tmp1[]=master.getFdate().split(" ");
				    		String tmp2[]=master.getTodate().split(" ");
				    		String exp = DateTimeUtils.getCommencingDate1(tmp1[0])+" to "+DateTimeUtils.getCommencingDate1(tmp2[0]);
				    		employee.setExp_date(exp);
				    	}
				    }
				    
				     
				     
				    employee.setFromdate(fromdate); 	
				    employee.setMobNo(rs.getString(6));
				    employee.setHeight(rs.getString(7));
				    employee.setWeight(rs.getString(8));
				    employee.setGender(rs.getString(9));
				    employee.setDob(rs.getString(13));
				    boolean isautochargeOn= completeAptmDAO.isAutoChargedOn(rs.getString(1));
					if(isautochargeOn){
						employee.setAutocharge("1");
					} else {
						employee.setAutocharge("0");
					}
				    /*employee.setInitial(rs.getString(10));*/
					employee.setImage(rs.getString(14));
				     list.add(employee);  
			  }
			  			
		} catch (Exception e) {

		  e.printStackTrace();
		}
		
		return list;
	}

	private Master getpkgdata(int clientid) {
		PreparedStatement preparedStatement = null;
		Master master = new Master();
		String sql = "SELECT tpkg, tpcommencing, tptodate  FROM apm_invoice_assesments where clientid ="+clientid+"  and tpkg > 0 order by id desc limit 0,1 ;";
		try {
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				master.setTpkg(rs.getString(1));
				master.setFdate(rs.getString(2));
				master.setTodate(rs.getString(3));
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return master;
	}

	private String getPackagefromDate(int int1) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT tpfdate from apm_invoice_assesments where "
				+ " clientid = "+int1+" and tpkg !=0 order by id desc limit 0,1 ";
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				result = rs.getString(1);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	private String getPackageendDate(int int1) {
		PreparedStatement preparedStatement = null;
		String result = "";
		String sql = "SELECT tptodate from apm_invoice_assesments where "
				+ " clientid = "+int1+" and tpkg !=0 order by id desc limit 0,1 ";
		try{
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			if(rs.next()){
				result = rs.getString(1);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	private String getPackageId(int clientId) {
		
		String result="";
		PreparedStatement preparedStatement=null;
		StringBuffer sql = new StringBuffer();
		sql.append(
				"SELECT  apm_parent_patient_package.todate FROM apm_parent_patient_package inner join his_parent_package on ");
		sql.append("his_parent_package.id = apm_parent_patient_package.packageid ");
		sql.append("where clientid = " + clientId + " order by apm_parent_patient_package.id desc");

		try {
			preparedStatement = connection.prepareStatement(sql.toString());
			ResultSet rs = preparedStatement.executeQuery();
			while (rs.next()) {
				result=rs.getString(1);
			}
	}catch (Exception e) {
		e.printStackTrace();
	}
		return result;
	}

	public static void main(String[] args) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		cal.set(2020, 02, 12);;
		cal.add(Calendar.MONTH, 0);
		
		System.out.println(dateFormat.format(cal.getTime()));
	}
	/*
	public ArrayList<Employee> getAllEmployees(String branch_id) {

		ArrayList<Employee> list=new ArrayList<Employee>();
		String sql="";
		
		try {
			
		      if(branch_id!=null && branch_id!=""){
		    	  sql="select empid, name, company_id, branchid, dept_id, designation, date_join, qualification, password, status from apm_payroll_employee where branchid="+branch_id+"";
		      }
		      else {
			     sql="select empid, name, company_id, branchid, dept_id, designation, date_join, qualification, password, status from apm_payroll_employee";
		      }
			  PreparedStatement ps=connection.prepareStatement(sql);
			  ResultSet rs=ps.executeQuery();
			  
			  while(rs.next()) {
				     Employee employee=new Employee();
				     employee.setEmp_id(rs.getString(1));
				     employee.setName(rs.getString(2));
				     
				     String comp_id=rs.getString(3);
				     employee.setCompany(getCompanyName(comp_id));
				     String branchid=rs.getString(4);
				     employee.setBranch(getBranchName(branchid));
				     String dept_id=rs.getString(5);
				     employee.setDepartment(getDepartmentName(dept_id));
				     employee.setDesignation(rs.getString(6));
				     employee.setDate_join(rs.getString(7));
				     employee.setQualification(rs.getString(8));
				     employee.setPassword(rs.getString(9));
				     list.add(employee);  
			  }
			  			
		} catch (Exception e) {

		  e.printStackTrace();
		}
		
		return list;
	}
*/
	
	
	public String getDepartmentName(String dept_id) {

		String department="";
		try {
			
			String sql="select dept_name from apm_payroll_department where id="+dept_id+"";
			PreparedStatement ps=connection.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				
				 department=rs.getString(1);
			}
			
			
		} catch (Exception e) {

		   e.printStackTrace();
		}
	
		return department;
	}

	public String getBranchName(String branchid) {

		String branch="";
		
		try {

			String sql="select name from apm_payroll_branch where id="+branchid+"";
			PreparedStatement ps=connection.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()){
				
				branch=rs.getString(1);
			}
			
		} catch (Exception e) {

		  e.printStackTrace();
		}
	
		return branch;
	}

	public String getCompanyName(String comp_id) {

		String company="";
		try {
			
			String sql="select company_name from apm_payroll_company where id="+comp_id+"";
			PreparedStatement ps=connection.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				
				company=rs.getString(1);				
			}
			
			
		} catch (Exception e) {

		   e.printStackTrace();
		}
		
		return company;
	}

	public ArrayList<Employee> getAllCompanies() {

		ArrayList<Employee> list=new ArrayList<Employee>();
		
		try {
			
			String sql="select id,company_name from apm_payroll_company";
			PreparedStatement ps=connection.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				
				  Employee employee=new Employee();
				  employee.setComp_id(rs.getString(1));
				  employee.setCompany(rs.getString(2));
				  list.add(employee);
				
			}
			
		} catch (Exception e) {

		   e.printStackTrace();
		}
		
		return list;
	}

	public ArrayList<Employee> getAllBranches() {

		ArrayList<Employee> list=new ArrayList<Employee>();
		
		try {
			
			 String sql="select id,name from apm_payroll_branch";
			 PreparedStatement ps=connection.prepareStatement(sql);
			 ResultSet rs=ps.executeQuery();
			 while(rs.next()) {
				   
				  Employee employee=new Employee();
				  employee.setBranch_id(rs.getString(1));
				  employee.setBranch(rs.getString(2));
				  list.add(employee);
			 }
			 
		} catch (Exception e) {

		   e.printStackTrace();
		   
		}
		
		return list;
	}

	public ArrayList<Employee> getAllDepartments() {

		ArrayList<Employee> list=new ArrayList<Employee>();
		
		try {
			
			String sql="select id,dept_name from apm_payroll_department";
			PreparedStatement ps=connection.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				
				  Employee employee=new Employee();
				  employee.setDept_id(rs.getString(1));
				  employee.setDepartment(rs.getString(2));
				  list.add(employee);
			}
			
		} catch (Exception e) {

		   e.printStackTrace();
		}
		
		
		
		return list;
	}

	public int addEmployee(Employee employee) {

		int result=0;
		
		try {
			
			String sql="insert into apm_patient ( firstname, surname, email,lastModified, mobno,height, weight, gender, fullname, address,whopay,title,middlename,dob) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement ps=connection.prepareStatement(sql);
			ps.setString(1, employee.getFirstname());
			ps.setString(2, employee.getLastname());
			ps.setString(3, employee.getEmail());
			ps.setString(4, employee.getDate_join());
			ps.setString(5, employee.getMobNo());
			ps.setString(6, employee.getHeight());
			ps.setString(7, employee.getWeight());
			ps.setString(8, employee.getGender());
			ps.setString(9, employee.getName());
			ps.setString(10, employee.getPermanentaddress());
			ps.setString(11,"Client");
			ps.setString(12,"");
			ps.setString(13,"");
			ps.setString(14, employee.getDob());
			result=ps.executeUpdate();
			
			if (result == 1) {
				ResultSet resultSet = ps.getGeneratedKeys();
				if (resultSet.next()) {
					result = resultSet.getInt(1);
				}
			}
			
		} catch (Exception e) {

		   e.printStackTrace();
		}
	
		return result;
	}

	public Employee getEmployee(String id) {

		Employee employee=new Employee();
		
		try {
			StringBuffer buffer=new StringBuffer();
			buffer.append("select id,firstname, surname, email,lastModified, mobno,height, weight, gender, fullname, address,dob ");
			buffer.append("from apm_patient where id="+id+"");
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				employee.setId(rs.getInt(1));
				employee.setFirstname(rs.getString(2));
				employee.setLastname(rs.getString(3));
				employee.setEmail(rs.getString(4));
				employee.setDate_join(rs.getString(5));
				employee.setMobNo(rs.getString(6));
				employee.setHeight(rs.getString(7));
				employee.setWeight(rs.getString(8));
				employee.setGender(rs.getString(9));
				employee.setPermanentaddress(rs.getString(11));
				employee.setDob(rs.getString(12));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return employee;
	}

	public int updateEmployee(Employee employee) {

		int result=0;
		
		try {
			
//			String sql="update apm_payroll_employee set name=?, company_id=?, branchid=?, dept_id=?, designation=?, date_join=?, qualification=?, dob=?, currentaddress=?, contact=?, panno=?, permanentadd=?,initial=?,empcode=?, adharno=?, gender=?, postcode=?, maritalsts=?, country=?, town=?, mobno=?, email=? where empid="+employee.getEmp_id()+"";
			String sql="update apm_patient set firstname=?, surname=?, email=?,lastModified=?, mobno=?,height=?, weight=?, gender=?, fullname=?, address=?, dob=? where id="+employee.getId()+"";
			PreparedStatement ps=connection.prepareStatement(sql);
			ps.setString(1, employee.getFirstname());
			ps.setString(2, employee.getLastname());
			ps.setString(3, employee.getEmail());
			ps.setString(4, employee.getDate_join());
			ps.setString(5, employee.getMobNo());
			ps.setString(6, employee.getHeight());
			ps.setString(7, employee.getWeight());
			ps.setString(8, employee.getGender());
			ps.setString(9, employee.getName());
			ps.setString(10, employee.getPermanentaddress());
			ps.setString(11, employee.getDob());
			result=ps.executeUpdate();
		} catch (Exception e) {

		   e.printStackTrace();
		}
	
		return result;
	}

	public int addToSalary(int result) {

		int i=0;

		
		try {
			
		   Date date=Calendar.getInstance(Locale.getDefault()).getTime();	
           SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
           String time=format.format(date);
           
           String sql="insert into apm_payroll_salary_master (emp_id,basic,date) values ("+result+",'0','"+time+"')";
           PreparedStatement ps=connection.prepareStatement(sql);
           i=ps.executeUpdate();
           
           String sql1="insert into apm_payroll_salary (emp_id,basic,date) values ("+result+",'0','"+time+"')";
           PreparedStatement ps1=connection.prepareStatement(sql1);
           i=ps1.executeUpdate();
	 		
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	
		return i;
	}
	   
      	public int gettotalempcount(String searchText, String dept, String searchname) {
    	  int res=0;
    	  try {
    		  if(dept==null){
    				dept="0";
    			}if(dept.equals("")){
    				dept="0";
    			}
    			ArrayList<Employee> list=new ArrayList<Employee>();
    			
    			      String sql="";
    			      StringBuffer buffer=new StringBuffer();
    				   buffer.append("select count(*) from apm_patient ");
    					boolean flag=false;
    			      if(searchText!=null){
    			    	  if(!searchText.equals("")){
    			    		  if(flag){
    			    			  buffer.append(" and id='"+searchText+"' ");
    			    		  }else{
    			    			  flag=true;
    			    		  buffer.append(" where id='"+searchText+"' ");
    			    		  }  		
    			    	  }
    			      }
    			      if(searchname!=null){
    			    	  if(!searchname.equals("")){
    			    		  if(flag){
    			    			  buffer.append(" and fullname like '%"+searchname+"%' ");
    			    		  }else {
    			    			  buffer.append(" where fullname like '%"+searchname+"%' ");
    						}
    			    		  
    			    	  }
    			      }
    				   sql=buffer.toString();			  
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				res=rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
      		return res;
      	}	
      public ArrayList<Employee> getAllAttendence(String emp_id,String filter_status) {
    	  
    	  ArrayList<Employee>list = new ArrayList<Employee>();
    	  try { 
			String sql = "";
			StringBuffer buffer = new StringBuffer();
			buffer.append("select emp_id, month, days, totalsalary from apm_payroll_weeksheet ");
			if(!filter_status.equals("")){
				buffer.append("where month='"+filter_status+"'");
			}
			PreparedStatement ps = connection.prepareStatement(buffer.toString());
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				String empname = getempname(rs.getString(1));
			Employee employee = new Employee();
			employee.setEmp_id(rs.getString(1));
			employee.setMonth(rs.getString(2));
			employee.setDays(rs.getString(3));
			employee.setTotalsalary(rs.getString(4));
			employee.setName(empname);
			list.add(employee);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return list;
    }
      public String getempname(String emp_id){
    	  String name="";
    	  try {
			String sql = "select name from apm_payroll_employee where empid="+emp_id+"";
			PreparedStatement ps = connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				name = rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;
    	  
      }

	public ArrayList<Employee> getseachedname(String searchtxt) {
		ArrayList<Employee> list=new ArrayList<Employee>();
		String sql="";
	    	  sql="select empid,name, company_id, branchid, dept_id, designation, date_join, qualification, password, status,initial,empcode from apm_payroll_employee where name like('%"+searchtxt+"%')";
	      
	    
		  try{
		   PreparedStatement ps=connection.prepareStatement(sql);
		  ResultSet rs=ps.executeQuery();
		  
		  while(rs.next()) {
			     Employee employee=new Employee();
			     employee.setEmp_id(rs.getString(1));
			     String name= rs.getString(11)+" "+rs.getString(2);
			     
			     employee.setName(name);
			     
			     String comp_id=rs.getString(3);
			     employee.setCompany(getCompanyName(comp_id));
			     String branchid=rs.getString(4);
			     employee.setBranch(getBranchName(branchid));
			     String dept_id=rs.getString(5);
			     employee.setDepartment(getDepartmentName(dept_id));
			     employee.setDesignation(rs.getString(6));
			     employee.setDate_join(rs.getString(7));
			     employee.setQualification(rs.getString(8));
			     employee.setPassword(rs.getString(9));
			    employee.setEmpcode(rs.getString(12));
			     list.add(employee);  
		  }
		  			
	} catch (Exception e) {

	  e.printStackTrace();
	}
	
	return list;
	}

	public int updatebasicsal(String empid, String basicsal) {
		int res=0;
		try {
			String sql="update apm_payroll_salary_master set basic='"+basicsal+"' where emp_id='"+empid+"' ";
			PreparedStatement ps=connection.prepareStatement(sql);
			
			res=ps.executeUpdate();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public Employee getAllDetailsEmployee(String emp_id) {
Employee employee=new Employee();
		
		try {
			StringBuffer buffer=new StringBuffer();
			 buffer.append("select firstname, surname, email,lastModified, mobno,height, weight, gender, fullname, address,image,dob from apm_patient where id="+emp_id+"");
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				     employee.setFirstname(rs.getString(1));
				     employee.setLastname(DateTimeUtils.isNull(rs.getString(2)));
				     employee.setEmail(DateTimeUtils.isNull(rs.getString(3)));
				     employee.setDate_join(DateTimeUtils.isNull(rs.getString(4)));
				     employee.setMobNo(DateTimeUtils.isNull(rs.getString(5)));
				     employee.setHeight(DateTimeUtils.isNull(rs.getString(6)));
				     employee.setWeight(DateTimeUtils.isNull(rs.getString(7)));
				     employee.setGender(DateTimeUtils.isNull(rs.getString(8)));
				     employee.setName(DateTimeUtils.isNull(rs.getString(9)));
				     employee.setPermanentaddress(DateTimeUtils.isNull(rs.getString(10)));
				     employee.setImage(DateTimeUtils.isNull(rs.getString(11)));
//				     employee.setEmpcode(DateTimeUtils.isNull(rs.getString(14)));
				     employee.setId(DateTimeUtils.convertToInteger(emp_id));
				     employee.setDob(DateTimeUtils.isNull(rs.getString(12)));
//				     adharno, gender, postcode, maritalsts, country, town, mobno, email
//				     employee.setAdhno(DateTimeUtils.isNull(rs.getString(15)));
//				     employee.setGender(DateTimeUtils.isNull(rs.getString(16)));
//				     employee.setPostcode(DateTimeUtils.isNull(rs.getString(17)));
//				     employee.setMaritalsts(DateTimeUtils.isNull(rs.getString(18)));
//				     employee.setCounty(DateTimeUtils.isNull(rs.getString(19)));
//				     employee.setTown(DateTimeUtils.isNull(rs.getString(20)));
//				     employee.setMobNo(DateTimeUtils.isNull(rs.getString(21)));
//				     employee.setEmail(DateTimeUtils.isNull(rs.getString(22)));
//				     employee.setInitial(DateTimeUtils.isNull(rs.getString(23)));
//				     employee.setBasicsal(DateTimeUtils.numberCheck(rs.getString(24)));
//				     employee.setDa(DateTimeUtils.numberCheck(rs.getString(25)));
//				     employee.setHra(DateTimeUtils.numberCheck(rs.getString(26)));
//				     employee.setConveyance(DateTimeUtils.numberCheck(rs.getString(27)));
//				     employee.setPerdevallow(DateTimeUtils.numberCheck(rs.getString(28)));
//				     employee.setMedicalallow(DateTimeUtils.numberCheck(rs.getString(29)));
//				     employee.setTds(DateTimeUtils.numberCheck(rs.getString(30)));
//				     employee.setEmp_esi(DateTimeUtils.numberCheck(rs.getString(31)));
//				     employee.setEmp_pf(DateTimeUtils.numberCheck(rs.getString(32)));
//				     employee.setProftax(DateTimeUtils.numberCheck(rs.getString(33)));
//				     employee.setNetsal(DateTimeUtils.numberCheck(rs.getString(34)));
//				     employee.setNationality(rs.getString(35));
//				     employee.setAccount_no(DateTimeUtils.numberCheck(rs.getString(36)));
//				     employee.setBank_name(DateTimeUtils.isNull(rs.getString(37)));
//				     employee.setIfsc_code(DateTimeUtils.isNull(rs.getString(38)));
//				     employee.setBank_branch(DateTimeUtils.isNull(rs.getString(39)));
//				     employee.setDaper(DateTimeUtils.numberCheck(rs.getString(40)));
//				     employee.setHraper(DateTimeUtils.numberCheck(rs.getString(41)));
//				     employee.setTdsper(DateTimeUtils.numberCheck(rs.getString(42)));
//				     employee.setWorkhour(DateTimeUtils.numberCheck(rs.getString(43)));
//				     employee.setFixedsal(DateTimeUtils.numberCheck(rs.getString(44)));
//				     employee.setBasicsalper(DateTimeUtils.numberCheck(rs.getString(45)));
//				     employee.setUserid(DateTimeUtils.isNull(rs.getString(46)));
//				     employee.setUanno(DateTimeUtils.isNull(rs.getString(47)));
//				     employee.setPfno(DateTimeUtils.isNull(rs.getString(48)));
//				     employee.setEsicno(DateTimeUtils.isNull(rs.getString(49)));
//				     employee.setCasualleave(DateTimeUtils.isNull(rs.getString(50)));
//				     employee.setRemain_leave(DateTimeUtils.isNull(rs.getString(51)));
//				     employee.setAgency(DateTimeUtils.isNull(rs.getString(52)));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return employee;
	}

	public int updatebasicsal(Employee employee,String table) {
int result=0;
		try {
			
			String sql="update "+table+" set basic=?, da=?, hra=?, conveyance=?, perdevallow=?, medical_allowance=?, tds=?, emp_pf=?, emp_esi=?, professional_tax=?, net_pay=?, daperc=?, hraperc=?, tdsperc=?, userid=?, fixedsalary=?, basicsalper=? where emp_id="+employee.getEmp_id()+"";
			PreparedStatement ps=connection.prepareStatement(sql);
			ps.setString(1, employee.getBasicsal());
			ps.setString(2, employee.getDa());
			ps.setString(3, employee.getHra());
			ps.setString(4, employee.getConveyance());
			ps.setString(5, employee.getPerdevallow());
			ps.setString(6, employee.getMedicalallow());
			ps.setString(7, employee.getTds());
			ps.setString(8, employee.getEmp_pf());
			ps.setString(9, employee.getEmp_esi());
			ps.setString(10, employee.getProftax());
			ps.setString(11, employee.getNetsal());
			ps.setString(12, employee.getDaper());
			ps.setString(13, employee.getHraper());
			ps.setString(14, employee.getTdsper());
			ps.setString(15, employee.getUserid());
			ps.setString(16, employee.getFixedsal());
			ps.setString(17, employee.getBasicsalper());
			
			result=ps.executeUpdate();
		} catch (Exception e) {

		   e.printStackTrace();
		}
	
		return result;
	}

	public String getEmployeeName(String empid) {
		String empname="";
		try {
			String sql="select name from apm_payroll_employee where empid='"+empid+"'";
			PreparedStatement preparedStatement=connection.prepareStatement(sql);
			ResultSet rs=preparedStatement.executeQuery();
			while(rs.next()) {
				empname=rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return empname;
	}

	public int updateBankEmployee(Employee employee) {

		int result=0;
		
		try {
			
//			String sql="update apm_payroll_employee set name=?, company_id=?, branchid=?, dept_id=?, designation=?, date_join=?, qualification=?, dob=?, currentaddress=?, contact=?, panno=?, permanentadd=?,initial=?,empcode=?, adharno=?, gender=?, postcode=?, maritalsts=?, country=?, town=?, mobno=?, email=? where empid="+employee.getEmp_id()+"";
			String sql="update apm_payroll_employee set account_no=?,bank_name=?, ifsc_code=?, bank_branch=? where empid="+employee.getEmp_id()+"";
			PreparedStatement ps=connection.prepareStatement(sql);
			ps.setString(1, employee.getAccount_no());
			ps.setString(2, employee.getBank_name());
			ps.setString(3, employee.getIfsc_code());
			ps.setString(4, employee.getBank_branch());
			
			result=ps.executeUpdate();
		} catch (Exception e) {

		   e.printStackTrace();
		}
	
		return result;
	}

	public ArrayList<Master> getAllDesignation(String deptid) {
		ArrayList<Master> list=new ArrayList<Master>();
		try {
			String sql="SELECT id,name from apm_payroll_designation where department='"+deptid+"' ";
			PreparedStatement ps=connection.prepareStatement(sql);
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()){
				  
				Master master=new Master();
				master.setId(rs.getInt(1));
				master.setName(rs.getString(2));
				list.add(master);
			}
			
		} catch (Exception e) {

			e.printStackTrace();
		}
		
		return list;
	}
	public int updatePersonalEmployee(Employee employee) {

		int result=0;
	try {
			
					String sql="update apm_payroll_employee set panno=?, adharno=?, maritalsts=? where empid="+employee.getEmp_id()+"";
		 			PreparedStatement ps=connection.prepareStatement(sql);
					ps.setString(1, employee.getPanno());
					ps.setString(2, employee.getAdhno());
					ps.setString(3, employee.getMaritalsts());
		 			
		 			result=ps.executeUpdate();
		 		} catch (Exception e) {
		 			e.printStackTrace();
}
	return result;
	}

	public String getEmployeeUserId(String emp_id) {
		String result="";
	try {
		String sql="select userid from apm_payroll_employee where empid="+emp_id+" ";
		PreparedStatement preparedStatement=connection.prepareStatement(sql);
		ResultSet rs=preparedStatement.executeQuery();
		while(rs.next()){
			result=rs.getString(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return result;
	}

	public int updateUanPfEmployee(Employee employee) {

		int result=0;
		
		try {
			
//			String sql="update apm_payroll_employee set name=?, company_id=?, branchid=?, dept_id=?, designation=?, date_join=?, qualification=?, dob=?, currentaddress=?, contact=?, panno=?, permanentadd=?,initial=?,empcode=?, adharno=?, gender=?, postcode=?, maritalsts=?, country=?, town=?, mobno=?, email=? where empid="+employee.getEmp_id()+"";
			String sql="update apm_payroll_employee set uan=?, pfno=?, esicno=? where empid="+employee.getEmp_id()+"";
			PreparedStatement ps=connection.prepareStatement(sql);
			ps.setString(1, employee.getUanno());
			ps.setString(2, employee.getPfno());
			ps.setString(3, employee.getEsicno());
			
			result=ps.executeUpdate();
		} catch (Exception e) {

		   e.printStackTrace();
		}
	
		return result;
	}

	public int updateleaveEmployee(Employee employee) {
		int result=0;
		
		try {
			
//			String sql="update apm_payroll_employee set name=?, company_id=?, branchid=?, dept_id=?, designation=?, date_join=?, qualification=?, dob=?, currentaddress=?, contact=?, panno=?, permanentadd=?,initial=?,empcode=?, adharno=?, gender=?, postcode=?, maritalsts=?, country=?, town=?, mobno=?, email=? where empid="+employee.getEmp_id()+"";
			String sql="update apm_payroll_employee set totalleaves=?, remain_leave=? where empid="+employee.getEmp_id()+"";
			PreparedStatement ps=connection.prepareStatement(sql);
			ps.setString(1, employee.getCasualleave());
			ps.setString(2, employee.getCasualleave());
			result=ps.executeUpdate();
		} catch (Exception e) {

		   e.printStackTrace();
		}
	
		return result;
	}

	public Employee getallDetailsEmployeeByUserId(String userid) {

Employee employee=new Employee();
		
		try {
			StringBuffer buffer=new StringBuffer();
			buffer.append("select name, company_id, branchid, dept_id, designation, date_join, qualification,status,dob, currentaddress, contact, panno, permanentadd, ");
			buffer.append("empcode,adharno, gender, postcode, maritalsts, country, town, mobno, email,initial,apm_payroll_salary_master.basic,apm_payroll_salary_master.da, ");
			buffer.append("apm_payroll_salary_master.hra,apm_payroll_salary_master.conveyance,apm_payroll_salary_master.perdevallow,medical_allowance, ");
			buffer.append("apm_payroll_salary_master.tds,apm_payroll_salary_master.emp_esi,apm_payroll_salary_master.emp_pf,apm_payroll_salary_master.professional_tax,apm_payroll_salary_master.net_pay,nationality, ");
			buffer.append("account_no, bank_name, ifsc_code, bank_branch,apm_payroll_salary_master.daperc,apm_payroll_salary_master.hraperc,apm_payroll_salary_master.tdsperc,apm_payroll_employee.workhour,apm_payroll_salary_master.fixedsalary,apm_payroll_salary_master.basicsalper,apm_payroll_employee.userid, ");
			buffer.append("uan, pfno, esicno, totalleaves,remain_leave,empid ");
			buffer.append("from apm_payroll_employee ");
			buffer.append("left join apm_payroll_salary_master on apm_payroll_salary_master.emp_id=apm_payroll_employee.empid where apm_payroll_employee.userid='"+userid+"' ");
			PreparedStatement ps=connection.prepareStatement(buffer.toString());
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
				     employee.setName(rs.getString(1));
				     employee.setCompany(DateTimeUtils.isNull(rs.getString(2)));
				     employee.setBranch(DateTimeUtils.isNull(rs.getString(3)));
				     employee.setDepartment(DateTimeUtils.isNull(rs.getString(4)));
				     employee.setDesignation(DateTimeUtils.isNull(rs.getString(5)));
				     employee.setDate_join(DateTimeUtils.isNull(rs.getString(6)));
				     employee.setQualification(DateTimeUtils.isNull(rs.getString(7)));
				     /*employee.setPassword(rs.getString(8));*/
				     employee.setDob(DateTimeUtils.isNull(rs.getString(9)));
				     employee.setCurrentaddress(DateTimeUtils.isNull(rs.getString(10)));
				     employee.setContact(DateTimeUtils.isNull(rs.getString(11)));
				     employee.setPanno(DateTimeUtils.isNull(rs.getString(12)));
				     employee.setPermanentaddress(DateTimeUtils.isNull(rs.getString(13)));
				     employee.setEmpcode(DateTimeUtils.isNull(rs.getString(14)));
//				     adharno, gender, postcode, maritalsts, country, town, mobno, email
				     employee.setAdhno(DateTimeUtils.isNull(rs.getString(15)));
				     employee.setGender(DateTimeUtils.isNull(rs.getString(16)));
				     employee.setPostcode(DateTimeUtils.isNull(rs.getString(17)));
				     employee.setMaritalsts(DateTimeUtils.isNull(rs.getString(18)));
				     employee.setCounty(DateTimeUtils.isNull(rs.getString(19)));
				     employee.setTown(DateTimeUtils.isNull(rs.getString(20)));
				     employee.setMobNo(DateTimeUtils.isNull(rs.getString(21)));
				     employee.setEmail(DateTimeUtils.isNull(rs.getString(22)));
				     employee.setInitial(DateTimeUtils.isNull(rs.getString(23)));
				     employee.setBasicsal(DateTimeUtils.numberCheck(rs.getString(24)));
				     employee.setDa(DateTimeUtils.numberCheck(rs.getString(25)));
				     employee.setHra(DateTimeUtils.numberCheck(rs.getString(26)));
				     employee.setConveyance(DateTimeUtils.numberCheck(rs.getString(27)));
				     employee.setPerdevallow(DateTimeUtils.numberCheck(rs.getString(28)));
				     employee.setMedicalallow(DateTimeUtils.numberCheck(rs.getString(29)));
				     employee.setTds(DateTimeUtils.numberCheck(rs.getString(30)));
				     employee.setEmp_esi(DateTimeUtils.numberCheck(rs.getString(31)));
				     employee.setEmp_pf(DateTimeUtils.numberCheck(rs.getString(32)));
				     employee.setProftax(DateTimeUtils.numberCheck(rs.getString(33)));
				     employee.setNetsal(DateTimeUtils.numberCheck(rs.getString(34)));
				     employee.setNationality(rs.getString(35));
				     employee.setAccount_no(DateTimeUtils.numberCheck(rs.getString(36)));
				     employee.setBank_name(DateTimeUtils.numberCheck(rs.getString(37)));
				     employee.setIfsc_code(DateTimeUtils.numberCheck(rs.getString(38)));
				     employee.setBank_branch(DateTimeUtils.numberCheck(rs.getString(39)));
				     employee.setDaper(DateTimeUtils.numberCheck(rs.getString(40)));
				     employee.setHraper(DateTimeUtils.numberCheck(rs.getString(41)));
				     employee.setTdsper(DateTimeUtils.numberCheck(rs.getString(42)));
				     employee.setWorkhour(DateTimeUtils.numberCheck(rs.getString(43)));
				     employee.setFixedsal(DateTimeUtils.numberCheck(rs.getString(44)));
				     employee.setBasicsalper(DateTimeUtils.numberCheck(rs.getString(45)));
				     employee.setUserid(DateTimeUtils.isNull(rs.getString(46)));
				     employee.setUanno(DateTimeUtils.isNull(rs.getString(47)));
				     employee.setPfno(DateTimeUtils.isNull(rs.getString(48)));
				     employee.setEsicno(DateTimeUtils.isNull(rs.getString(49)));
				     employee.setCasualleave(DateTimeUtils.isNull(rs.getString(50)));
				     employee.setRemain_leave(DateTimeUtils.isNull(rs.getString(51)));
				     employee.setEmp_id(rs.getString(52));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return employee;
	}
	public String getEmployeeEmpid(String userid) {
		String result="";
	try {
		String sql="select empid from apm_payroll_employee where userid='"+userid+"' ";
		PreparedStatement preparedStatement=connection.prepareStatement(sql);
		ResultSet rs=preparedStatement.executeQuery();
		while(rs.next()){
			result=rs.getString(1);
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	return result;
	}

	public int deleteEmployee(String id) {
		int result=0;
		try {
			PreparedStatement ps=connection.prepareStatement("delete from apm_patient where id='"+id+"'");
			result= ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public int updateAutochargeFlagClient(String clientid, String flag) {
		int res=0;
		try {
			
			String sql="update apm_patient set isautocharge="+flag+" where id="+clientid+" ";
			PreparedStatement ps=connection.prepareStatement(sql);
			res =ps.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return res;
	}

	@Override
	public Master getsmsdata() {
		PreparedStatement preparedStatement  = null;
		Master master = new Master();
		String sql = "select count, totalsms from apm_sms_counter";
		try {
			preparedStatement = connection.prepareStatement(sql);
			ResultSet rs = preparedStatement.executeQuery();
			while(rs.next()){
				
				master.setUsedsms(rs.getString(1));
				master.setTotalsms(rs.getString(2));
				
				int remainsms = Integer.parseInt(master.getTotalsms()) - Integer.parseInt(master.getUsedsms());
				master.setRemainsms(Integer.toString(remainsms));
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return master;
	}

	@Override
	public int savePayrollFileSubmissionData(String datetime, String uploaded_userid, String filesubmission_category,
			String filesubremark, String fileName, String filecontenttype, String empid, String userid) {
			int result=0;
			try {
				String sql="update apm_patient set image='"+fileName+"' where id="+empid+"";
				PreparedStatement ps=connection.prepareStatement(sql);
				result =ps.executeUpdate();
			} catch (Exception e) {

				e.printStackTrace();
			}
			return result;
	}
}
