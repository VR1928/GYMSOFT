package com.apm.Payroll.web.action;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import com.apm.Accounts.eu.bi.AccountsDAO;
import com.apm.Accounts.eu.blogic.jdbc.JDBCAccountsDAO;
import com.apm.Appointment.eu.entity.AppointmentType;
import com.apm.DiaryManagement.eu.bi.ClientDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCClientDAO;
import com.apm.DiaryManagement.web.action.SmsService;
import com.apm.Inventory.eu.bi.InventoryVendorDAO;
import com.apm.Inventory.eu.blogic.jdbc.JDBCInventoryVendorDAO;
import com.apm.Log.eu.entity.EmailLetterLog;
import com.apm.Master.eu.bi.MasterDAO;
import com.apm.Master.eu.blogic.jdbc.JDBCMasterDAO;
import com.apm.Master.eu.entity.Master;
import com.apm.Payroll.eu.bi.PayrollDepartmentDAO;
import com.apm.Payroll.eu.bi.PayrollEmployeeDAO;
import com.apm.Payroll.eu.bi.PayrollMasterDAO;
import com.apm.Payroll.eu.blogic.jdbc.JDBCPayrollDepartment;
import com.apm.Payroll.eu.blogic.jdbc.JDBCPayrollEmployeeDAO;
import com.apm.Payroll.eu.blogic.jdbc.JDBCPayrollMasterDAO;
import com.apm.Payroll.eu.entity.Employee;
import com.apm.Payroll.eu.entity.Payroll;
import com.apm.Payroll.eu.entity.Salary;
import com.apm.Payroll.web.form.EmployeeForm;
import com.apm.common.eu.blogic.jdbc.Connection_provider;
import com.apm.common.utils.DateTimeUtils;
import com.apm.common.utils.Pagination;
import com.apm.common.web.action.BaseAction;
import com.apm.common.web.common.helper.LoginHelper;
import com.apm.common.web.common.helper.LoginInfo;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class PayrollEmployeeAction extends BaseAction implements ModelDriven<EmployeeForm>, Preparable {

	EmployeeForm employeeForm = new EmployeeForm();

	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
	HttpServletResponse response = (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
	HttpSession session = request.getSession(true);
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	Pagination pagination = new Pagination(25, 1);

	public EmployeeForm getModel() {
		return employeeForm;
	}

	@Override
	public String execute() throws Exception {

		if (!verifyLogin(request)) {
			return "login";
		}

		Connection connection = null;
		String status = request.getParameter("status");
		try {

			String dept = employeeForm.getDepartment();
			connection = Connection_provider.getconnection();
			PayrollEmployeeDAO payrollEmployeeDAO = new JDBCPayrollEmployeeDAO(connection);
			
			
			String searchText = employeeForm.getSearchText();
			if (searchText != null) {
				if (searchText.equals("")) {
					searchText = null;
				}
			}
			String searchname = employeeForm.getEmpnamesearch();
			if (searchText != null) {
				if (searchText.equals("")) {
					searchText = null;
				}
			}
			String activestatus=employeeForm.getActivestatus();
			if(activestatus==null){
				activestatus="0";
			}
			if(activestatus.equals("")){
				activestatus="0";
			}
			int count = payrollEmployeeDAO.gettotalempcount(searchText,  dept,
					searchname);
			pagination.setPreperties(count);
			ArrayList<Employee> employeelist = payrollEmployeeDAO.getAllEmployees(searchText, pagination, dept,
					searchname,loginInfo,activestatus);
			
			employeeForm.setEmployeelist(employeelist);
			employeeForm.setTotalRecords(count);
			pagination.setTotal_records(employeelist.size());
			employeeForm.setTotalRecords(count);
			employeeForm.setPagerecords(String.valueOf(pagination.getTotal_records()));
			employeeForm.setSearchText(searchText);
			employeeForm.setEmpnamesearch(searchname);
			employeeForm.setDepartment(dept);
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			connection.close();
		}
		if (status == null) {
			status = "1";
		}
		if (status.equals("0")) {
			return super.execute();
		} else {
			return "listwiseemp";
		}
	}

	public Pagination getPagination() {
		return pagination;
	}

	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}
	
	public String gymsms() throws Exception{
		if (!verifyLogin(request)) {

			return "login";
		}
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			PayrollEmployeeDAO employeeDAO = new JDBCPayrollEmployeeDAO(connection);
			
			Master master =  employeeDAO.getsmsdata();
			String data = master.getUsedsms()+"/"+master.getTotalsms()+"/"+master.getRemainsms();
			 response.setContentType("text/html");
			 response.setHeader("Cache-Control", "no-cache");
			 response.getWriter().write(data);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			connection.close();
		}
		return null;	
	}

	public String add() throws Exception {

		if (!verifyLogin(request)) {

			return "login";
		}

		Connection connection = null;

		try {
			connection = Connection_provider.getconnection();
			PayrollEmployeeDAO employeeDAO = new JDBCPayrollEmployeeDAO(connection);

			Employee employee = new Employee();
			employee.setFirstname(employeeForm.getFirstname());
			employee.setLastname(employeeForm.getLastname());
			String name = employeeForm.getFirstname() + " " + employeeForm.getLastname();
			employee.setName(name);
			employee.setDate_join(DateTimeUtils.getCommencingDatePayroll(employeeForm.getDate_join()));
			employee.setDob(employeeForm.getDob());
			employee.setEmpcode(employeeForm.getEmpcode());
			employee.setMobNo(employeeForm.getMobNo());
			employee.setEmail(employeeForm.getEmail());
			employee.setHeight(employeeForm.getHeight());
			employee.setWeight(employeeForm.getWeight());
			employee.setGender(employeeForm.getGender());
			employee.setPermanentaddress(employeeForm.getPermanentaddress());
			int result = employeeDAO.addEmployee(employee);
		
		
			String msg = "Your're registerd in Fitfull Gym and your customerid is "+result+" ";
			String mob = employeeForm.getMobNo();
			if(loginInfo.getGymsms()==1){
				
				SmsService sms = new SmsService();
				sms.sendSms(msg, mob, loginInfo, new EmailLetterLog());
			}
			

			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}

		return "savepayrollemp";
	}

	public String edit() throws Exception {

		if (!verifyLogin(request)) {
			return "login";
		}

		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			PayrollEmployeeDAO employeeDAO = new JDBCPayrollEmployeeDAO(connection);
			String id = request.getParameter("selectedid");
			Employee employee = employeeDAO.getEmployee(id);
			employeeForm.setEmp_id(id);
			 String data=employee.getId()+"~"+employee.getFirstname()+"~"+employee.getLastname()+"~"+employee.getEmail()+"~"+DateTimeUtils.getCommencingDatePayroll(employee.getDate_join())+"~"+employee.getMobNo()+"~"+employee.getHeight()+"~"+employee.getWeight()+"~"+employee.getGender()+"~"+employee.getPermanentaddress()+"~"+employee.getDob();
			 response.setContentType("text/html");
				response.setHeader("Cache-Control", "no-cache");
				response.getWriter().write(data);

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			connection.close();

		}
		return null;
	}

	public String update() throws Exception {

		if (!verifyLogin(request)) {

			return "login";
		}

		Connection connection = null;

		try {
			connection = Connection_provider.getconnection();
			PayrollEmployeeDAO payrollEmployeeDAO = new JDBCPayrollEmployeeDAO(connection);

			Employee employee = new Employee();
			
			employee.setId(employeeForm.getCust_id());
//			if(employeeForm.getId()==0){
//			employee.setId(DateTimeUtils.convertToInteger(request.getParameter("emp_id")));
//			}
			employee.setName(employeeForm.getFirstname() + " " + employeeForm.getLastname());
			employee.setFirstname(employeeForm.getFirstname());
			employee.setLastname(employeeForm.getLastname());
			 employee.setDate_join(DateTimeUtils.getCommencingDatePayroll(employeeForm.getDate_join()));
			 employee.setPermanentaddress(employeeForm.getPermanentaddress());
			employee.setGender(employeeForm.getGender());
			employee.setHeight(employeeForm.getHeight());
			employee.setWeight(employeeForm.getWeight());
			employee.setMobNo(employeeForm.getMobNo());
			employee.setEmail(employeeForm.getEmail());
			employee.setDob(employeeForm.getDob());
			int result = payrollEmployeeDAO.updateEmployee(employee);

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			connection.close();
		}

		return "updateprofile";
	}

	public void prepare() throws Exception {
		Connection connection = null;

		try {
			connection = Connection_provider.getconnection();
			PayrollEmployeeDAO payrollEmployeeDAO = new JDBCPayrollEmployeeDAO(connection);
			InventoryVendorDAO vendorDAO = new JDBCInventoryVendorDAO(connection);
			ArrayList<Master> stateList = vendorDAO.getAllStateList();
			ArrayList<Master> cityList = vendorDAO.getAllCityList();
			employeeForm.setStatelist(stateList);
			employeeForm.setCitylist(cityList);
			ArrayList<Employee> companylist = payrollEmployeeDAO.getAllCompanies();
			employeeForm.setCompanylist(companylist);

			ArrayList<Employee> branchlist = payrollEmployeeDAO.getAllBranches();
			employeeForm.setBranchlist(branchlist);

			ArrayList<Employee> departmentlist = payrollEmployeeDAO.getAllDepartments();
			employeeForm.setDepartmentlist(departmentlist);
			PayrollDepartmentDAO departmentDAO = new JDBCPayrollDepartment(connection);
			ArrayList<Payroll> designationlist = departmentDAO.getAllDesignation();
			employeeForm.setDesignationlist(designationlist);
			employeeForm.setDepartmentlist(departmentlist);
			ArrayList<String> initialList = new ArrayList<String>();
			ClientDAO clientDAO = new JDBCClientDAO(connection);

			initialList = clientDAO.getInitialList();
			employeeForm.setInitialList(initialList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String addemployee() {
		if (!verifyLogin(request)) {
			return "login";
		}

		Connection connection = null;

		try {
			connection = Connection_provider.getconnection();
			PayrollEmployeeDAO payrollEmployeeDAO = new JDBCPayrollEmployeeDAO(connection);
			InventoryVendorDAO vendorDAO = new JDBCInventoryVendorDAO(connection);
			ArrayList<Master> stateList = vendorDAO.getAllStateList();
			ArrayList<Master> cityList = vendorDAO.getAllCityList();
			employeeForm.setStatelist(stateList);
			employeeForm.setCitylist(cityList);
			ArrayList<Employee> companylist = payrollEmployeeDAO.getAllCompanies();
			employeeForm.setCompanylist(companylist);

			ArrayList<Employee> branchlist = payrollEmployeeDAO.getAllBranches();
			employeeForm.setBranchlist(branchlist);

			ArrayList<Employee> departmentlist = payrollEmployeeDAO.getAllDepartments();
			employeeForm.setDepartmentlist(departmentlist);
			ArrayList<String> initialList = new ArrayList<String>();
			ClientDAO clientDAO = new JDBCClientDAO(connection);

			initialList = clientDAO.getInitialList();
			employeeForm.setInitialList(initialList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "addemployee";

	}

	public String searchempname() {
		Connection connection = null;
		String searchtxt = request.getParameter("searchtext");

		try {
			connection = Connection_provider.getconnection();
			PayrollEmployeeDAO employeeDAO = new JDBCPayrollEmployeeDAO(connection);
			ArrayList<Employee> list = new ArrayList<Employee>();
			list = employeeDAO.getseachedname(searchtxt);
			StringBuffer buffer = new StringBuffer();

			for (Employee p : list) {
				buffer.append("<tr>");
				buffer.append("<td>" + p.getEmp_id() + "</td>");
				buffer.append("<td>" + p.getEmpcode() + "</td>");
				buffer.append("<td>" + p.getName() + "</td>");
				buffer.append("<td>" + p.getBranch() + "</td>");
				buffer.append("<td>" + p.getDepartment() + "</td>");

				buffer.append("<td>" + p.getDesignation() + "</td>");
				buffer.append("<td>" + p.getDate_join() + "</td>");

				buffer.append("<td class='text-center hidden-print'><s:a href='editPayrollEmployee?selectedid='"
						+ p.getEmp_id() + "'><i class='fa fa-edit'></i></s:a></td>");
				buffer.append("</tr>");
			}
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("" + buffer.toString() + "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String payrollprofile() {
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			String emp_id="";
			if(session.getAttribute("updateempid")!=null){
				emp_id = (String) session.getAttribute("updateempid");	
			}
			if (emp_id == null || emp_id.equals("")) {
				emp_id = request.getParameter("empid");
			} else {
				session.removeAttribute("updateempid");
			}
			PayrollEmployeeDAO payrollEmployeeDAO = new JDBCPayrollEmployeeDAO(connection);
			Employee employee = payrollEmployeeDAO.getAllDetailsEmployee(emp_id);
			//firstname, surname, email,lastModified, mobno,height, weight, gender, fullname, address
			employeeForm.setFirstname(employee.getFirstname());
			employeeForm.setLastname(employee.getLastname());
			employeeForm.setName(employee.getName());
			employeeForm.setDate_join(DateTimeUtils.getCommencingDatePayroll(employee.getDate_join()));
			employeeForm.setEmail(employee.getEmail());
			employeeForm.setMobNo(employee.getMobNo());
			employeeForm.setHeight(employee.getHeight());
			employeeForm.setWeight(employee.getWeight());
			employeeForm.setGender(employee.getGender());
			employeeForm.setPermanentaddress(employee.getPermanentaddress());
			employeeForm.setImage(employee.getImage());
			employeeForm.setDob(employee.getDob());
//			employeeForm.setCompany(employee.getCompany());
//			employeeForm.setBranch(employee.getBranch());
//			employeeForm.setDepartment(employee.getDepartment());
//			employeeForm.setDesignation(employee.getDesignation());
//			employeeForm.setQualification(employee.getQualification());
//			employeeForm.setDob(employee.getDob());
//			employeeForm.setCurrentaddress(employee.getCurrentaddress());
//			employeeForm.setPanno(employee.getPanno());
//			employeeForm.setInitial(employee.getInitial());
//			employeeForm.setEmpcode(employee.getEmpcode());
//			employeeForm.setAdhno(employee.getAdhno());
//			
//			employeeForm.setPostcode(employee.getPostcode());
//			employeeForm.setMaritalsts(employee.getMaritalsts());
//			employeeForm.setCounty(employee.getCounty());
//			employeeForm.setTown(employee.getTown());
			employeeForm.setEmp_id(emp_id);
//			employeeForm.setBasicsal(employee.getBasicsal());
//			employeeForm.setDa(employee.getDa());
//			employeeForm.setHra(employee.getHra());
//			employeeForm.setConveyance(employee.getConveyance());
//			employeeForm.setPerdevallow(employee.getPerdevallow());
//			employeeForm.setMedical_allownces(employee.getMedicalallow());
//			employeeForm.setTds(employee.getTds());
//			employeeForm.setEmp_esi(employee.getEmp_esi());
//			employeeForm.setEmp_pf(employee.getEmp_pf());
//			employeeForm.setLeaves(employee.getLeaves());
//			employeeForm.setProftax(employee.getProftax());
//			employeeForm.setNetsal(employee.getNetsal());
//			employeeForm.setNationality(employee.getNationality());
//			employeeForm.setAccount_no(employee.getAccount_no());
//			employeeForm.setBank_name(employee.getBank_name());
//			employeeForm.setBank_branch(employee.getBank_branch());
//			employeeForm.setIfsc_code(employee.getIfsc_code());
//			employeeForm.setDaper(employee.getDaper());
//			employeeForm.setHraper(employee.getHraper());
//			employeeForm.setTdsper(employee.getTdsper());
//			employeeForm.setWorkhour(employee.getWorkhour());
//			employeeForm.setEmployid(employee.getEmpcode());
//			employeeForm.setFixedsal(employee.getFixedsal());
//			employeeForm.setBasicsalper(employee.getBasicsalper());
//			employeeForm.setUserid(employee.getUserid());
//			employeeForm.setPfno(employee.getPfno());
//			employeeForm.setUanno(employee.getUanno());
//			employeeForm.setEsicno(employee.getEsicno());
//			employeeForm.setCasualleave(employee.getCasualleave());
//			employeeForm.setRemain_leave(employee.getRemain_leave());
//			employeeForm.setAgency(employee.getAgency());
			/*session.setAttribute("updateempid", emp_id);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "payrollprofile";
	}

	public String payrollbasicsal() {
		
		Connection connection = null;
		
		try {
			connection = Connection_provider.getconnection();
			PayrollEmployeeDAO payrollEmployeeDAO = new JDBCPayrollEmployeeDAO(connection);
			String emp_id = employeeForm.getEmp_id();
			String userid=payrollEmployeeDAO.getEmployeeUserId(emp_id);
			Employee employee = new Employee();
			employee.setEmp_id(emp_id);
			employee.setBasicsal(employeeForm.getBasicsal());
			employee.setDa(employeeForm.getDa());
			employee.setHra(employeeForm.getHra());
			employee.setConveyance(employeeForm.getConveyance());
			employee.setPerdevallow(employeeForm.getPerdevallow());
			employee.setMedicalallow(employeeForm.getMedical_allownces());
			employee.setTds(employeeForm.getTds());
			employee.setEmp_esi(employeeForm.getEmp_esi());
			employee.setEmp_pf(employeeForm.getEmp_pf());
			employee.setProftax(employeeForm.getProftax());
			employee.setNetsal(employeeForm.getNetsal());
			employee.setDaper(employeeForm.getDaper());
			employee.setHraper(employeeForm.getHraper());
			employee.setTdsper(employeeForm.getTdsper());
			employee.setFixedsal(employeeForm.getFixedsal());
			employee.setBasicsalper(employeeForm.getBasicsalper());
			employee.setUserid(userid);
			
			int res = payrollEmployeeDAO.updatebasicsal(employee, "apm_payroll_salary_master");
			int res1 = payrollEmployeeDAO.updatebasicsal(employee, "apm_payroll_salary");
			session.setAttribute("updateempid", employee.getEmp_id());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "payrollbasicsal";
	}

	public String employeename(String empid) {
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			PayrollEmployeeDAO employeeDAO = new JDBCPayrollEmployeeDAO(connection);
			String empname = employeeDAO.getEmployeeName(empid);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String updatepersonal() {
		if (!verifyLogin(request)) {

			return "login";
		}

		Connection connection = null;

		try {
			connection = Connection_provider.getconnection();
			PayrollEmployeeDAO payrollEmployeeDAO = new JDBCPayrollEmployeeDAO(connection);

			Employee employee = new Employee();
			employee.setEmp_id(employeeForm.getEmp_id());
			employee.setPanno(employeeForm.getPanno().toUpperCase());
			employee.setAdhno(employeeForm.getAdhno());
			employee.setMaritalsts(employeeForm.getMaritalsts());

			session.setAttribute("updateempid", employee.getEmp_id());
			int result = payrollEmployeeDAO.updatePersonalEmployee(employee);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return "updateprofile";
	}

	public String getdesignation() throws Exception {

		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			String deptid = request.getParameter("deptid");
			PayrollEmployeeDAO payrollEmployeeDAO = new JDBCPayrollEmployeeDAO(connection);
			ArrayList<Master> listdesign = payrollEmployeeDAO.getAllDesignation(deptid);

			StringBuffer buffer = new StringBuffer();
			buffer.append("<label>Designation</label><span class='text-danger'>*</span>");
			buffer.append("<select class='form-control showToolTip chosen' name='designation' id='designation'  >");
			buffer.append("<option value='0'>Select Designation </option>");
			for (Master master : listdesign) {

				buffer.append("<option value='" + master.getName() + "'>" + master.getName() + "</option>");

			}
			buffer.append("</select>");
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(buffer.toString());

		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			connection.close();
		}

		return null;
	}

	public String updatebank() {
		if (!verifyLogin(request)) {

			return "login";
		}

		Connection connection = null;

		try {
			connection = Connection_provider.getconnection();
			PayrollEmployeeDAO payrollEmployeeDAO = new JDBCPayrollEmployeeDAO(connection);

			Employee employee = new Employee();
			employee.setEmp_id(employeeForm.getEmp_id());
			employee.setAccount_no(employeeForm.getAccount_no());
			employee.setBank_name(employeeForm.getBank_name());
			employee.setBank_branch(employeeForm.getBank_branch());
			employee.setIfsc_code(employeeForm.getIfsc_code());
			session.setAttribute("updateempid", employee.getEmp_id());
			int result = payrollEmployeeDAO.updateBankEmployee(employee);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return "updateprofile";
	}
	
	public String uanpfupdate() {
		if (!verifyLogin(request)) {

			return "login";
		}

		Connection connection = null;

		try {
			connection = Connection_provider.getconnection();
			PayrollEmployeeDAO payrollEmployeeDAO = new JDBCPayrollEmployeeDAO(connection);

			Employee employee = new Employee();
			employee.setEmp_id(employeeForm.getEmp_id());
			employee.setUanno(employeeForm.getUanno());
			employee.setPfno(employeeForm.getPfno());
			employee.setEsicno(employeeForm.getEsicno());
			session.setAttribute("updateempid", employee.getEmp_id());
			int result = payrollEmployeeDAO.updateUanPfEmployee(employee);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return "updateprofile";
	}
	
	public String leavegiven() {
		if (!verifyLogin(request)) {

			return "login";
		}

		Connection connection = null;

		try {
			connection = Connection_provider.getconnection();
			PayrollEmployeeDAO payrollEmployeeDAO = new JDBCPayrollEmployeeDAO(connection);

			Employee employee = new Employee();
			employee.setEmp_id(employeeForm.getEmp_id());
			employee.setCasualleave(employeeForm.getCasualleave());
			session.setAttribute("updateempid", employee.getEmp_id());
			int result = payrollEmployeeDAO.updateleaveEmployee(employee);

		} catch (Exception e) {

			e.printStackTrace();
		}
		return "updateprofile";
	}
public String delete() {
	
	Connection connection = null;

	try {
		connection = Connection_provider.getconnection();
		PayrollEmployeeDAO payrollEmployeeDAO = new JDBCPayrollEmployeeDAO(connection);
		String id=request.getParameter("id");
		int result = payrollEmployeeDAO.deleteEmployee(id);
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write("0");

	} catch (Exception e) {

		e.printStackTrace();
	}
	return null; 
	}
public String switchautocharge() throws Exception{
	
	Connection connection=null;
	try {
		connection=Connection_provider.getconnection();
		PayrollEmployeeDAO employeeDAO=new JDBCPayrollEmployeeDAO(connection);
		String clientid= request.getParameter("clientid");
		String flag=request.getParameter("flag");
		if(flag==null){
			flag="0";
		}
		if(flag.equals("")){
			flag="0";
		}
		if (flag.equals("true")) {
			flag="1";
		}
		if (flag.equals("false")) {
			flag="0";
		}	
		
		int res=employeeDAO.updateAutochargeFlagClient(clientid,flag);
		
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		
		response.getWriter().write(""); 
		
	} catch (Exception e) {

		e.printStackTrace();
	}finally{
		
		connection.close();
	}
	
	return null;
}
public String uploaddocument() throws SQLException{
	Connection connection=null;
	try {
		connection = Connection_provider.getconnection();
		PayrollEmployeeDAO employeeDAO=new JDBCPayrollEmployeeDAO(connection);
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		String toDate = dateFormat.format(cal.getTime());
		String empid=employeeForm.getEmp_id();
		String userid=employeeDAO.getEmployeeUserId(""+empid);
		String filesubmission_category = employeeForm.getFilesubmission_category();
		String filesubremark = employeeForm.getFilesubremark();
		File uploadedFile = employeeForm.getSubuploadfiles();
		String filecontenttype = employeeForm.getSubuploadfilesContentType();
        String fileName ="";

        try {
        	fileName=loginInfo.getClinicUserid()+"_"+toDate+"_"+employeeForm.getSubuploadfilesFileName();
	        String filePath = "";
	        filePath = request.getRealPath("/liveData/document/");
	        	/*File fileToCreate = new File(filePath, fileName);*/
	        	File fileToCreate = new File(filePath, fileName);
	            FileUtils.copyFile(uploadedFile, fileToCreate);
	            String datetime = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
	            String uploaded_userid = loginInfo.getUserId();
            int res = employeeDAO.savePayrollFileSubmissionData(datetime,uploaded_userid,filesubmission_category,filesubremark,fileName,filecontenttype,""+empid,userid);
            session.setAttribute("updateempid", empid);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			connection.close();
		}
	return "updateprofile";
}
public String updateprofile() throws Exception {

	if (!verifyLogin(request)) {

		return "login";
	}

	Connection connection = null;

	try {
		connection = Connection_provider.getconnection();
		PayrollEmployeeDAO payrollEmployeeDAO = new JDBCPayrollEmployeeDAO(connection);

		Employee employee = new Employee();
		
		employee.setId(employeeForm.getCust_id());
		if(employeeForm.getId()==0){
		employee.setId(DateTimeUtils.convertToInteger(request.getParameter("emp_id")));
		}
		employee.setName(employeeForm.getFirstname() + " " + employeeForm.getLastname());
		employee.setFirstname(employeeForm.getFirstname());
		employee.setLastname(employeeForm.getLastname());
		 employee.setDate_join(DateTimeUtils.getCommencingDatePayroll(employeeForm.getDate_join()));
		 employee.setPermanentaddress(employeeForm.getPermanentaddress());
		employee.setGender(employeeForm.getGender());
		employee.setHeight(employeeForm.getHeight());
		employee.setWeight(employeeForm.getWeight());
		employee.setMobNo(employeeForm.getMobNo());
		employee.setEmail(employeeForm.getEmail());
		employee.setDob(employeeForm.getDob());
		session.setAttribute("updateempid", employee.getId());
		int result = payrollEmployeeDAO.updateEmployee(employee);

	} catch (Exception e) {

		e.printStackTrace();
	} finally {
		connection.close();
	}

	return "updateprofile";
}

}
