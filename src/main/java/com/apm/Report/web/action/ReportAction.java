package com.apm.Report.web.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.apm.Accounts.eu.bi.AccountsDAO;
import com.apm.Accounts.eu.blogic.jdbc.JDBCAccountsDAO;
import com.apm.DiaryManagement.eu.bi.ClientDAO;
import com.apm.DiaryManagement.eu.bi.NotAvailableSlotDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCClientDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCNotAvailableSlotDAO;
import com.apm.DiaryManagement.eu.entity.Breadcrumbs;
import com.apm.DiaryManagement.eu.entity.Client;
import com.apm.DiaryManagement.eu.entity.DiaryManagement;
import com.apm.Emr.eu.bi.EmrDAO;
import com.apm.Emr.eu.bi.InvestigationDAO;
import com.apm.Emr.eu.blogic.jdbc.JDBCEmrDAO;
import com.apm.Emr.eu.blogic.jdbc.JDBCInvestigationDAO;
import com.apm.Emr.eu.entity.Investigation;
import com.apm.Inventory.eu.bi.InventoryProductDAO;
import com.apm.Inventory.eu.bi.ProcurementDAO;
import com.apm.Inventory.eu.blogic.jdbc.JDBCInventoryProductDAO;
import com.apm.Inventory.eu.blogic.jdbc.JDBCProcurementDAO;
import com.apm.Inventory.eu.entity.Procurement;
import com.apm.Inventory.eu.entity.Product;
import com.apm.Inventory.eu.entity.Vendor;
import com.apm.Master.eu.bi.InvestigationMasterDAO;
import com.apm.Master.eu.bi.MasterDAO;
import com.apm.Master.eu.blogic.jdbc.JDBCInvestigationMasterDAO;
import com.apm.Master.eu.blogic.jdbc.JDBCMasterDAO;
import com.apm.Master.eu.entity.Master;
import com.apm.Registration.eu.bi.ClinicDAO;
import com.apm.Registration.eu.bi.UserProfileDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCClinicDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCUserProfileDAO;
import com.apm.Registration.eu.entity.Clinic;
import com.apm.Registration.eu.entity.UserProfile;
import com.apm.Registration.web.form.UserProfileForm;
import com.apm.Report.eu.bi.ChargesReportDAO;
import com.apm.Report.eu.bi.ClientReportDAO;
import com.apm.Report.eu.bi.SummaryReportDAO;
import com.apm.Report.eu.blogic.jdbc.JDBCChargesReportDAO;
import com.apm.Report.eu.blogic.jdbc.JDBCClientReportDAO;
import com.apm.Report.eu.blogic.jdbc.JDBCSummaryReportDAO;
import com.apm.Report.eu.entity.Report;
import com.apm.Report.eu.entity.SummaryReport;
import com.apm.Report.web.form.ClientReportForm;
import com.apm.Report.web.form.ReportForm;
import com.apm.common.eu.blogic.jdbc.Connection_provider;
import com.apm.common.utils.DateTimeUtils;
import com.apm.common.utils.Pagination;
import com.apm.common.web.action.BaseAction;
import com.apm.common.web.common.helper.LoginHelper;
import com.apm.common.web.common.helper.LoginInfo;
import com.apm.common.web.utils.PopulateList;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class ReportAction extends BaseAction implements Preparable, ModelDriven<ClientReportForm>  {
	
	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
	HttpServletResponse response = (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
	HttpSession session = request.getSession(true);
	ClientReportForm clientReportForm = new ClientReportForm();
	ReportForm reportForm = new ReportForm();
	private Pagination pagination = new Pagination(15, 1);
		public Pagination getPagination() {
			return pagination;
		}


		public void setPagination(Pagination pagination) {
			this.pagination = pagination;
		}
	
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	
	
	public String execute() throws Exception {
		
		if(!verifyLogin(request)){
			return "login";
		}
		
		
	
		
	 
		return "reportsuccess";
	}

	public String setColumns()throws Exception{
		String from =reportForm.getFromDate();
		String todate = reportForm.getToDate();
		String r1 = reportForm.getReport();
		reportForm.setFromDate(from);
		reportForm.setToDate(todate);
		reportForm.setReport(r1);
		
		
	
		
		return "reportsuccess";
	}
	
	
	/*public String noActivityRecord(){
		
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			ClientReportDAO clientReportDAO = new JDBCClientReportDAO(connection);
			ArrayList<Client> noActivityRecordList = clientReportDAO.getNoActivityRecordOfClientList();
			clientReportForm.setNoActivityRecordList(noActivityRecordList);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	*/
	
	public ClientReportForm getModel() {
		return clientReportForm;
	}


	public void prepare() throws Exception {

		try{
			Connection connection=null;
			connection  = Connection_provider.getconnection();
		NotAvailableSlotDAO notAvailableSlotDAO=new JDBCNotAvailableSlotDAO(connection);
		ArrayList<DiaryManagement>userList = notAvailableSlotDAO.getUserList(loginInfo.getId());		
		clientReportForm.setUserList(userList);
		//reportForm.setReportList(PopulateList.reportList());
		}catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	//Akash 26 April 2018 stock valuation report
	public String stockvaluation() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			InventoryProductDAO inventoryProductDAO = new JDBCInventoryProductDAO(connection);
			MasterDAO masterDAO = new JDBCMasterDAO(connection);
			String location = clientReportForm.getLocation();
			String filteroforder = clientReportForm.getFilteroforder();
			if(location==null){
				location = "36";
			}else if(location.equals("")){
				location = "36";
			}
			
			if(filteroforder==null){
				filteroforder="0";
			}else if(filteroforder.equals("")){
				filteroforder="0";
			}
			
			ArrayList<Product> stockvaluationlist = inventoryProductDAO.getstockValuationReport(location,filteroforder);
			clientReportForm.setStockvaluationlist(stockvaluationlist);
			ArrayList<Master> locationlist = masterDAO.getAllLocation(null);
			clientReportForm.setLocationlist(locationlist);
			clientReportForm.setLocation(location);
			clientReportForm.setFilteroforder(filteroforder);
			
			Clinic clinic = new Clinic();
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
			clientReportForm.setClinicName(clinic.getClinicName());
			clientReportForm.setClinicOwner(clinic.getClinicOwner());
			clientReportForm.setOwner_qualification(clinic.getOwner_qualification());
			clientReportForm.setLocationAdressList(locationAdressList);
			clientReportForm.setAddress(clinic.getAddress());
			clientReportForm.setLandLine(clinic.getLandLine());
			clientReportForm.setClinicemail(clinic.getEmail());
			clientReportForm.setWebsiteUrl(clinic.getWebsiteUrl());
			clientReportForm.setClinicLogo(clinic.getUserImageFileName());
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			connection.close();
		}
		return "stockvaluation";
	}
	
	//Akash 28 April 2018 Department wise Revenue Monthwise compare and Yearly Compare//forwarded to lokesh
	public String departmentwiserevenue() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			String todate = clientReportForm.getToDate();
			SummaryReportDAO summaryReportDAO = new JDBCSummaryReportDAO(connection);
			String month = clientReportForm.getMonth();
			if(todate== null){
				DateFormat dateFormat = new SimpleDateFormat("yyyy");
				Calendar cal = Calendar.getInstance(); 
				todate = dateFormat.format(cal.getTime());
				
			}else{
				if(todate.equals("")){
				    DateFormat dateFormat = new SimpleDateFormat("yyyy");
				    Calendar cal = Calendar.getInstance(); 
				    todate = dateFormat.format(cal.getTime());
				    
				}
			}
			if(month==null){
				month="1";
			}else if(month.equals("0")){
				month="1";
			}
			clientReportForm.setMonth(month);
			String ipdopd=clientReportForm.getIpdopd();
			if(ipdopd==null){
				ipdopd="";
			}
			
			String newtodate = todate;
			String fromdate="";
			fromdate = "01-01-"+newtodate+"";
			todate = "31-12-"+newtodate+"";
			fromdate = DateTimeUtils.getCommencingDate1(fromdate);  
			todate = DateTimeUtils.getCommencingDate1(todate);
			
			ArrayList<SummaryReport> departmentwisereportlist = summaryReportDAO.getDepartmentWiseRevenue(fromdate,todate,newtodate,ipdopd);
			clientReportForm.setDepartmentwisereportlist(departmentwisereportlist);
			//lokesh
			SummaryReport sr1= departmentwisereportlist.get(departmentwisereportlist.size()-1);
			clientReportForm.setJan1(sr1.getJan1());
			clientReportForm.setFeb1(sr1.getFeb1());
			clientReportForm.setMarch1(sr1.getMarch1());
			clientReportForm.setApril1(sr1.getApril1());
			clientReportForm.setMay1(sr1.getMay1());
			clientReportForm.setJune1(sr1.getJune1());
			clientReportForm.setJully1(sr1.getJully1());
			clientReportForm.setAug1(sr1.getAug1());
			clientReportForm.setSept1(sr1.getSept1());
			clientReportForm.setOct1(sr1.getOct1());
			clientReportForm.setNov1(sr1.getNov1());
			clientReportForm.setDec1(sr1.getDec1());
			clientReportForm.setToDate(newtodate);
			clientReportForm.setTotalpatient(Integer.parseInt(sr1.getTotalpatient()));
			//lokesh
			Clinic clinic = new Clinic();
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
			clientReportForm.setClinicName(clinic.getClinicName());
			clientReportForm.setClinicOwner(clinic.getClinicOwner());
			clientReportForm.setOwner_qualification(clinic.getOwner_qualification());
			clientReportForm.setLocationAdressList(locationAdressList);
			clientReportForm.setAddress(clinic.getAddress());
			clientReportForm.setLandLine(clinic.getLandLine());
			clientReportForm.setClinicemail(clinic.getEmail());
			clientReportForm.setWebsiteUrl(clinic.getWebsiteUrl());
			clientReportForm.setClinicLogo(clinic.getUserImageFileName());
			
			session.setAttribute("deptwise_graphical_fromdate", fromdate);
			session.setAttribute("deptwise_graphical_todate", todate);
			session.setAttribute("deptwise_graphical_month", month);
			session.setAttribute("deptwise_graphical_ipdopd", ipdopd);
			session.setAttribute("deptwise_graphical_newtodate", newtodate);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			connection.close();
		}
		return "departmentwiserevenue";
	}
	//Akash 30 April 2018
	public String departmaterialissue() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{			
			connection = Connection_provider.getconnection();
			InventoryProductDAO inventoryProductDAO = new JDBCInventoryProductDAO(connection);
			String fromDate = clientReportForm.getFromDate();
			String toDate = clientReportForm.getToDate();	
			String productname= clientReportForm.getProduct();
			String department= clientReportForm.getDepartment();
			String warehouseid = clientReportForm.getWarehouse_id();
			String category = clientReportForm.getCat_filter();
			if(productname==null){
				productname= "";
			}if(productname.equals("0")){
				productname= "";
			}
			
			if(department==null){
				department= "";
			}if(department.equals("0")){
				department= "";
			}
			
			if(warehouseid==null){
				warehouseid= "36";
			}if(warehouseid.equals("0") || warehouseid.equals("")){
				warehouseid= "36";
			}
			
			if(category!=null){
				if(category.equals("")){
					category="0";
				}
			}else{
				category="0";
			}
			clientReportForm.setCat_filter(category);
			
			/*String filteroforder = clientReportForm.getFilteroforder();*/
			if (fromDate == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar cal = Calendar.getInstance();
				// cal.add(Calendar.DATE, -7);
				fromDate = dateFormat.format(cal.getTime());
				fromDate = DateTimeUtils.getCommencingDate1(fromDate);
			} else {

				if (fromDate.equals("")) {
					DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					Calendar cal = Calendar.getInstance();
					// cal.add(Calendar.DATE, -7);
					fromDate = dateFormat.format(cal.getTime());
					fromDate = DateTimeUtils.getCommencingDate1(fromDate);
					// fromdate = null;
				} else {
					fromDate = DateTimeUtils.getCommencingDate1(fromDate);
				}
			}

			if (toDate == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar cal = Calendar.getInstance();
				toDate = dateFormat.format(cal.getTime());
				toDate = DateTimeUtils.getCommencingDate1(toDate);
			} else {
				if (toDate.equals("")) {
					DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					Calendar cal = Calendar.getInstance();
					toDate = dateFormat.format(cal.getTime());
					toDate = DateTimeUtils.getCommencingDate1(toDate);
					// todate = null;
				} else {
					toDate = DateTimeUtils.getCommencingDate1(toDate);
				}
			}
	 		
			
			/*if (filteroforder==null) {
				filteroforder="0";
			}else if(filteroforder.equals("")){
				filteroforder="0";
			}*/
			MasterDAO masterdao= new JDBCMasterDAO(connection);
			//ArrayList<Product> departmaterialissuelist = inventoryProductDAO.getDeptMaterialIssueList(fromDate,toDate,productname,department,warehouseid);
			ArrayList<Product> departmaterialissuelist = inventoryProductDAO.getDeptMaterialIssueListNew(fromDate,toDate,productname,department,warehouseid,category);
			ArrayList<Master> departmentlist= masterdao.getAllLocation(null);
			/*ArrayList<Product> productlist= inventoryProductDAO.getMedicineListforVendor("0");*/
			ArrayList<Product> productlist= new ArrayList<Product>();
			clientReportForm.setProductlist(productlist);
			clientReportForm.setDepartmentlist(departmentlist);
	 		clientReportForm.setDepartmaterialissuelist(departmaterialissuelist);
			clientReportForm.setFromDate(fromDate = DateTimeUtils.getCommencingDate1(fromDate));
			clientReportForm.setToDate( DateTimeUtils.getCommencingDate1(toDate));
			clientReportForm.setProduct(productname);
			clientReportForm.setDepartment(department);
			clientReportForm.setWarehouse_id(warehouseid);
			Clinic clinic = new Clinic();
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
			ArrayList<Master> warehouseList= inventoryProductDAO.getWareHouseList();
			clientReportForm.setWarehouseList(warehouseList);
			clientReportForm.setClinicName(clinic.getClinicName());
			clientReportForm.setClinicOwner(clinic.getClinicOwner());
			clientReportForm.setOwner_qualification(clinic.getOwner_qualification());
			clientReportForm.setLocationAdressList(locationAdressList);
			clientReportForm.setAddress(clinic.getAddress());
			clientReportForm.setLandLine(clinic.getLandLine());
			clientReportForm.setClinicemail(clinic.getEmail());
			clientReportForm.setWebsiteUrl(clinic.getWebsiteUrl());
			clientReportForm.setClinicLogo(clinic.getUserImageFileName());
			
			ArrayList<Product> categoryList=inventoryProductDAO.getAllCategories(null);
			//ArrayList<Product> subcategoryList=inventoryProductDAO.getAllSubCategoryList(null);
			clientReportForm.setCategoryList(categoryList);
			//clientReportForm.setSubcategoryList(subcategoryList);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			connection.close();
		}
		
		return "departmaterialissue";
	}
	
	//Akash 02 May 2018 
	public String nonmovingitem() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{			
			connection = Connection_provider.getconnection();
			InventoryProductDAO inventoryProductDAO = new JDBCInventoryProductDAO(connection);
			MasterDAO masterdao= new JDBCMasterDAO(connection);
			String department= clientReportForm.getDepartment();
			String filteroforder= clientReportForm.getFilteroforder();
			if(department==null){
				department= "36";
			}if(department.equals("")){
				department= "36";
			}
			if(filteroforder==null){
				filteroforder="0";
			}else if(filteroforder.equals("")){
				filteroforder="0";
			}
			
			String toDate="";
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();
			toDate = dateFormat.format(cal.getTime());
			toDate = DateTimeUtils.getCommencingDate1(toDate);
			
			ArrayList<Product> nonmovingitemlist = inventoryProductDAO.getNonMovingItemList(department,toDate,filteroforder);
			clientReportForm.setNonmovingitemlist(nonmovingitemlist);
			
			ArrayList<Master> departmentlist= masterdao.getAllLocation(null);
			clientReportForm.setDepartmentlist(departmentlist);
			
			
			Clinic clinic = new Clinic();
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
			clientReportForm.setClinicName(clinic.getClinicName());
			clientReportForm.setClinicOwner(clinic.getClinicOwner());
			clientReportForm.setOwner_qualification(clinic.getOwner_qualification());
			clientReportForm.setLocationAdressList(locationAdressList);
			clientReportForm.setAddress(clinic.getAddress());
			clientReportForm.setLandLine(clinic.getLandLine());
			clientReportForm.setClinicemail(clinic.getEmail());
			clientReportForm.setWebsiteUrl(clinic.getWebsiteUrl());
			clientReportForm.setClinicLogo(clinic.getUserImageFileName());
			clientReportForm.setDepartment(department);
			clientReportForm.setFilteroforder(filteroforder);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			connection.close();
		}
		
		return "nonmovingitem";
	}
	//Akash 03 may 2018
	public String payableaging() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{			
			connection = Connection_provider.getconnection();
			ProcurementDAO procurementDAO = new JDBCProcurementDAO(connection);
			String toDate="";
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();
			toDate = dateFormat.format(cal.getTime());
			toDate = DateTimeUtils.getCommencingDate1(toDate);
			
			ArrayList<Procurement> paybleaginglist = procurementDAO.getPaybleAging(toDate);
			clientReportForm.setPaybleaginglist(paybleaginglist);
			
			Clinic clinic = new Clinic();
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
			clientReportForm.setClinicName(clinic.getClinicName());
			clientReportForm.setClinicOwner(clinic.getClinicOwner());
			clientReportForm.setOwner_qualification(clinic.getOwner_qualification());
			clientReportForm.setLocationAdressList(locationAdressList);
			clientReportForm.setAddress(clinic.getAddress());
			clientReportForm.setLandLine(clinic.getLandLine());
			clientReportForm.setClinicemail(clinic.getEmail());
			clientReportForm.setWebsiteUrl(clinic.getWebsiteUrl());
			clientReportForm.setClinicLogo(clinic.getUserImageFileName());
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			connection.close();
		}
		
		return "payableaging";
	}
	public String stockreport() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			InventoryProductDAO inventoryProductDAO = new JDBCInventoryProductDAO(connection);
			MasterDAO masterDAO = new JDBCMasterDAO(connection);
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			
			String location = clientReportForm.getLocation();
			
			 String isfromcathlab = request.getParameter("isfromcathlab");
				
				if(isfromcathlab!=null){
					if(isfromcathlab.equals("")){
						isfromcathlab = "0";
					}
				}else{
					isfromcathlab = clientReportForm.getIsfromcathlab();
					if(isfromcathlab!=null){
						if(isfromcathlab.equals("")){
							isfromcathlab="0";
						}
					}else{
						isfromcathlab="0";
					}
				}
				ArrayList<Master> locationlist =new ArrayList<Master>();
				if(isfromcathlab.equals("1")){
					location ="105";
					clientReportForm.setLocation(location);
					session.setAttribute("location", location);
					locationlist = masterDAO.getAllLocationNew(location);
				}else{
					locationlist = masterDAO.getAllLocation(null);
				}
			clientReportForm.setIsfromcathlab(isfromcathlab);
			String orderby = clientReportForm.getOrderby();
			String report_filter = clientReportForm.getReport_filter();
			String withstock_filter = clientReportForm.getWithstock_filter();
			//String subcat_filter = clientReportForm.getSubcat_filter();
			String cat_filter = clientReportForm.getCat_filter();
			String fromdate = clientReportForm.getFromDate();
			
			String searchtext = clientReportForm.getSearchtext();
			
			if(searchtext!=null){
				if(searchtext.equals("")){
					searchtext= null;
				}
			}
			
			if(location!=null){
				if(location.equals("") || location.equals("0")){
					location ="36";
				}
			}else{
				location = "36";
			}
			if(orderby!=null){
				if(orderby.equals("")){
					orderby ="1";
				}
			}else{
				orderby = "1";
			}
			if(report_filter!=null){
				if(report_filter.equals("")){
					report_filter ="3";
				}
			}else{
				report_filter = "3";
			}
			
			if(withstock_filter!=null){
				if(withstock_filter.equals("")){
					withstock_filter ="1";
				}
			}else{
				withstock_filter = "1";
			}
			/*if(subcat_filter!=null){
				if(subcat_filter.equals("")){
					subcat_filter ="0";
				}
			}else{
				subcat_filter = "0";
			}*/
			
			if(cat_filter!=null){
				if(cat_filter.equals("")){
					cat_filter ="0";
				}
			}else{
				cat_filter = "0";
			}
			String order_filter = clientReportForm.getOrder_filter();
			if (order_filter != null) {
				if (order_filter.equals("")) {
					order_filter = "asc";
				}
			} else {
				order_filter = "desc";
			}
			
			if(fromdate == null){
	 			   DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	 			   Calendar cal = Calendar.getInstance();
	 			   fromdate = dateFormat.format(cal.getTime());   
	 			   fromdate = DateTimeUtils.getCommencingDate1(fromdate);
	   		 }else {
	 		    if(fromdate.equals("")) {
	 		    	DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
	 		    	Calendar cal = Calendar.getInstance();
	 		    	fromdate = dateFormat.format(cal.getTime());   
	 		    	fromdate = DateTimeUtils.getCommencingDate1(fromdate);
	 		    } else {
	 		    	fromdate = DateTimeUtils.getCommencingDate1(fromdate);   
	 		    }  
	 		 }
			
			String toDate="";
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();
			toDate = dateFormat.format(cal.getTime());
			toDate = DateTimeUtils.getCommencingDate1(toDate);
			
			ArrayList<Product> newstocklist = new ArrayList<Product>();
			
			String prductlist ="0";
			String totalmrp ="0";
			String totalsaleprice ="0";
			double totalpurchaseprice =0;
			int totalqty =0;
			double totalfinal=0;
			double totalsalevaluation =0;
			if(report_filter.equals("3")){
				if(!toDate.equals(fromdate)){
					ArrayList<Product> stocklist = inventoryProductDAO.getALLStockReport(location,orderby,report_filter,withstock_filter,cat_filter,order_filter,fromdate,1,prductlist,searchtext);
					int size = stocklist.size();
					if (size > 0) {
						totalmrp = stocklist.get(size - 1).getTotalmrp();
						totalsaleprice = stocklist.get(size - 1).getTotalsaleprice();
						totalpurchaseprice = stocklist.get(size-1).getTotalpurchaseprice();
						totalqty = stocklist.get(size - 1).getTotalqty();
						totalfinal=stocklist.get(size-1).getTotalAmt();
						prductlist = stocklist.get(size-1).getPrductlistnew();
						totalsalevaluation =stocklist.get(size-1).getTotalsalevaluation();
					}
					newstocklist.addAll(stocklist);
				}
			}
			
			ArrayList<Product> stocklist = inventoryProductDAO.getALLStockReport(location,orderby,report_filter,withstock_filter,cat_filter,order_filter,fromdate,0,prductlist,searchtext);
			newstocklist.addAll(stocklist);
//			int size1 = stocklist.size();
//			String totalpurprice = stocklist.get(size1 - 1).getTotalcountpurprice();
//			clientReportForm.setTotalcountpurprice(totalpurprice);
			clientReportForm.setStocklist(newstocklist);
			
			//ArrayList<Product> subcategoryList=inventoryProductDAO.getAllSubCategoryList(null);
			//clientReportForm.setSubcategoryList(subcategoryList);
			ArrayList<Product> categoryList=inventoryProductDAO.getAllCategories(null);
			
			int size = stocklist.size();
			if (size > 0) {
				double totalmrpnew = Double.parseDouble(totalmrp) +   Double.parseDouble(stocklist.get(size - 1).getTotalmrp());
				double totalsalepricenew = Double.parseDouble(totalsaleprice) +  Double.parseDouble(stocklist.get(size - 1).getTotalsaleprice());
				totalpurchaseprice = totalpurchaseprice + stocklist.get(size-1).getTotalpurchaseprice();
				totalqty = totalqty + stocklist.get(size - 1).getTotalqty();
				totalfinal= totalfinal + stocklist.get(size-1).getTotalAmt();
				totalfinal=(Math.round(totalfinal * 100.0) / 100.0);
				totalmrp = DateTimeUtils.changeFormat(totalmrpnew);
				totalsaleprice = DateTimeUtils.changeFormat(totalsalepricenew);
				totalsalevaluation = totalsalevaluation + stocklist.get(size-1).getTotalsalevaluation();
				String t= DateTimeUtils.changeFormat(totalfinal);
				clientReportForm.setTotalmrp(totalmrp);
				clientReportForm.setTotalsaleprice(totalsaleprice);
				clientReportForm.setTotalqty(totalqty);
				clientReportForm.setTotalpurchaseprice(String.valueOf(Math.round(totalpurchaseprice * 100.0) / 100.0));
				clientReportForm.setTotalofstock(totalfinal);
				clientReportForm.setTtl(t);
				clientReportForm.setTotalsalevaluation(Math.round(totalsalevaluation*100.0)/100.0);
			} else {
				String t= DateTimeUtils.changeFormat(totalfinal);
				clientReportForm.setTotalmrp(totalmrp);
				clientReportForm.setTotalsaleprice(totalsaleprice);
				clientReportForm.setTotalqty(totalqty);
				clientReportForm.setTotalpurchaseprice(String.valueOf(Math.round(totalpurchaseprice * 100.0) / 100.0));
				clientReportForm.setTotalofstock(totalfinal);
				clientReportForm.setTtl(t);
				clientReportForm.setTotalsalevaluation(Math.round(totalsalevaluation*100.0)/100.0);
			}
			
			clientReportForm.setCategoryList(categoryList);
			
			clientReportForm.setLocationlist(locationlist);
			clientReportForm.setOrderby(orderby);
			clientReportForm.setLocation(location);
			clientReportForm.setReport_filter(report_filter);
			clientReportForm.setWithstock_filter(withstock_filter);
			//clientReportForm.setSubcat_filter(subcat_filter);
			clientReportForm.setCat_filter(cat_filter);
			Clinic clinic = new Clinic();
			
			clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
			ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
			clientReportForm.setClinicName(clinic.getClinicName());
			clientReportForm.setClinicOwner(clinic.getClinicOwner());
			clientReportForm.setOwner_qualification(clinic.getOwner_qualification());
			clientReportForm.setLocationAdressList(locationAdressList);
			clientReportForm.setAddress(clinic.getAddress());
			clientReportForm.setLandLine(clinic.getLandLine());
			clientReportForm.setClinicemail(clinic.getEmail());
			clientReportForm.setWebsiteUrl(clinic.getWebsiteUrl());
			clientReportForm.setClinicLogo(clinic.getUserImageFileName());
			fromdate = DateTimeUtils.getCommencingDate1(fromdate);   
			clientReportForm.setFromDate(fromdate);
			if(searchtext==null){
				clientReportForm.setSearchtext("");
			}else{
				clientReportForm.setSearchtext(searchtext);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			connection.close();
		}
		return "stockreport";
	}
	
	//Akash 08 June 2018
	public String userwisematerialissue() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{			
			connection = Connection_provider.getconnection();
			InventoryProductDAO inventoryProductDAO = new JDBCInventoryProductDAO(connection);
			String fromDate = clientReportForm.getFromDate();
			String toDate = clientReportForm.getToDate();	
			String product= clientReportForm.getProduct();
			String department= clientReportForm.getDepartment();
			String fromwherefilter = clientReportForm.getFromwherefilter();
			String warehousefilter=clientReportForm.getWarehousefilter();
			if(product==null){
				product= "";
			}if(product.equals("0")){
				product= "";
			}
			clientReportForm.setProduct(product);
			if(department==null){
				department= "0";
			}if(department.equals("")){
				department= "0";
			}
			
			if(fromwherefilter==null){
				fromwherefilter= "0";
			}if(fromwherefilter.equals("")){
				fromwherefilter= "0";
			}
			clientReportForm.setFromwherefilter(fromwherefilter);
			
			if (fromDate == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar cal = Calendar.getInstance();
				// cal.add(Calendar.DATE, -7);
				fromDate = dateFormat.format(cal.getTime());
				fromDate = DateTimeUtils.getCommencingDate1(fromDate);
			} else {

				if (fromDate.equals("")) {
					DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					Calendar cal = Calendar.getInstance();
					// cal.add(Calendar.DATE, -7);
					fromDate = dateFormat.format(cal.getTime());
					fromDate = DateTimeUtils.getCommencingDate1(fromDate);
					// fromdate = null;
				} else {
					fromDate = DateTimeUtils.getCommencingDate1(fromDate);
				}
			}

			if (toDate == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar cal = Calendar.getInstance();
				toDate = dateFormat.format(cal.getTime());
				toDate = DateTimeUtils.getCommencingDate1(toDate);
			} else {
				if (toDate.equals("")) {
					DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					Calendar cal = Calendar.getInstance();
					toDate = dateFormat.format(cal.getTime());
					toDate = DateTimeUtils.getCommencingDate1(toDate);
					// todate = null;
				} else {
					toDate = DateTimeUtils.getCommencingDate1(toDate);
				}
			}
			ArrayList<Master> warehouseList = inventoryProductDAO.getWareHouseList();
			clientReportForm.setWarehouseList(warehouseList);
			String warehousefilter1=clientReportForm.getWarehousefilter();
			if(warehousefilter1==null){
				warehousefilter1="36";
			}
			if(warehousefilter1.equals(""))
			{
				warehousefilter1="36";
			}
			clientReportForm.setWarehousefilter(warehousefilter1);
			UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
			ArrayList<Product> usermaterialissuelist = inventoryProductDAO.getUserMaterialIssueList(fromDate,toDate,product,department,fromwherefilter,warehousefilter1);
			ArrayList<Product> productlist= inventoryProductDAO.getMedicineListforVendor("0");
			ArrayList<UserProfile> userlist = userProfileDAO.getAllUserList();
			clientReportForm.setUserlist(userlist);
			clientReportForm.setProductlist(productlist);
			clientReportForm.setDepartmaterialissuelist(usermaterialissuelist);
			clientReportForm.setFromDate(fromDate = DateTimeUtils.getCommencingDate1(fromDate));
			clientReportForm.setToDate( DateTimeUtils.getCommencingDate1(toDate));
			Clinic clinic = new Clinic();
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
			
			clientReportForm.setClinicName(clinic.getClinicName());
			clientReportForm.setClinicOwner(clinic.getClinicOwner());
			clientReportForm.setOwner_qualification(clinic.getOwner_qualification());
			clientReportForm.setLocationAdressList(locationAdressList);
			clientReportForm.setAddress(clinic.getAddress());
			clientReportForm.setLandLine(clinic.getLandLine());
			clientReportForm.setClinicemail(clinic.getEmail());
			clientReportForm.setWebsiteUrl(clinic.getWebsiteUrl());
			clientReportForm.setClinicLogo(clinic.getUserImageFileName());
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			connection.close();
		}
		
		return "usermaterialissue";
	}
	
	public String investoutsource() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		
		String dept= clientReportForm.getDepartment();
		if(dept==null){
			dept="0";
		}
		if(dept.equals("")){
			dept="0";
		}
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			InvestigationDAO investigationDAO = new JDBCInvestigationDAO(connection);
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			String fromDate = clientReportForm.getFromDate();
			String toDate = clientReportForm.getToDate();
			
			if (fromDate == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar cal = Calendar.getInstance();
				// cal.add(Calendar.DATE, -7);
				fromDate = dateFormat.format(cal.getTime());
				fromDate = DateTimeUtils.getCommencingDate1(fromDate);
			} else {
				if (fromDate.equals("")) {
					DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					Calendar cal = Calendar.getInstance();
					// cal.add(Calendar.DATE, -7);
					fromDate = dateFormat.format(cal.getTime());
					fromDate = DateTimeUtils.getCommencingDate1(fromDate);
					// fromdate = null;
				} else {
					fromDate = DateTimeUtils.getCommencingDate1(fromDate);
				}
			}

			if (toDate == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar cal = Calendar.getInstance();
				toDate = dateFormat.format(cal.getTime());
				toDate = DateTimeUtils.getCommencingDate1(toDate);
			} else {
				if (toDate.equals("")) {
					DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					Calendar cal = Calendar.getInstance();
					toDate = dateFormat.format(cal.getTime());
					toDate = DateTimeUtils.getCommencingDate1(toDate);
					// todate = null;
				} else {
					toDate = DateTimeUtils.getCommencingDate1(toDate);
				}
			}
			String outsrclabname=clientReportForm.getOutsourcelab();
			if(outsrclabname==null){
				outsrclabname="";
			}
			
			String clientId, invstnameId;
			clientId= DateTimeUtils.isNull(clientReportForm.getClientId());
			invstnameId=DateTimeUtils.isNull(clientReportForm.getInvstName());
			MasterDAO masterDAO= new JDBCMasterDAO(connection);
			ArrayList<Master> outsourcelabs= masterDAO.getallOutsource(null, null);
			ArrayList<Investigation> outsourcelist = investigationDAO.getOutsourceReport(fromDate,toDate,outsrclabname,dept,clientId,invstnameId);
			clientReportForm.setOutsourcelist(outsourcelist);
			clientReportForm.setFromDate(DateTimeUtils.getCommencingDate1(fromDate));
			clientReportForm.setToDate(DateTimeUtils.getCommencingDate1(toDate));
			
			ArrayList<Client> clientList=new ArrayList<Client>();
			ClientDAO clientDAO= new JDBCClientDAO(connection);
			clientList= clientDAO.getAllPatient();
			clientReportForm.setClientList(clientList);
			ArrayList<Master> invsttypelist= new ArrayList<Master>();
			InvestigationMasterDAO investigationMasterDAO= new JDBCInvestigationMasterDAO(connection);
			invsttypelist= investigationMasterDAO.getAllInvestigationTypes();
			clientReportForm.setInvsTypeList(invsttypelist);
			
			Clinic clinic = new Clinic();
			clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
			ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
			clientReportForm.setClinicName(clinic.getClinicName());
			clientReportForm.setClinicOwner(clinic.getClinicOwner());
			clientReportForm.setOwner_qualification(clinic.getOwner_qualification());
			clientReportForm.setLocationAdressList(locationAdressList);
			clientReportForm.setAddress(clinic.getAddress());
			clientReportForm.setLandLine(clinic.getLandLine());
			clientReportForm.setClinicemail(clinic.getEmail());
			clientReportForm.setWebsiteUrl(clinic.getWebsiteUrl());
			clientReportForm.setClinicLogo(clinic.getUserImageFileName());
			clientReportForm.setOutsrclablist(outsourcelabs);
			clientReportForm.setOutsourcelab(outsrclabname);
			clientReportForm.setTotalcharge(outsourcelist.get(outsourcelist.size()-1).getTotalcharge());
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			connection.close();
		}
		return "investoutsource";
	}
	
	public String itemwisepurchase() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{			
			connection = Connection_provider.getconnection();
			ProcurementDAO procurementDAO = new JDBCProcurementDAO(connection);
			InventoryProductDAO inventoryProductDAO = new JDBCInventoryProductDAO(connection);
			String fromDate = clientReportForm.getFromDate();
			String toDate = clientReportForm.getToDate();	
			String product= clientReportForm.getProduct();
			
			String warehouseid = clientReportForm.getWarehouse_id();
			if(warehouseid==null){
				warehouseid= "0";
			}if(warehouseid.equals("")){
				warehouseid= "0";
			}
			
			if(product!=null){
				if(product.equals("")){
					product= null;
				}
			}
			
			
			if (fromDate == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar cal = Calendar.getInstance();
				fromDate = dateFormat.format(cal.getTime());
				fromDate = DateTimeUtils.getCommencingDate1(fromDate);
			} else {

				if (fromDate.equals("")) {
					DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					Calendar cal = Calendar.getInstance();
					fromDate = dateFormat.format(cal.getTime());
					fromDate = DateTimeUtils.getCommencingDate1(fromDate);
				} else {
					fromDate = DateTimeUtils.getCommencingDate1(fromDate);
				}
			}

			if (toDate == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar cal = Calendar.getInstance();
				toDate = dateFormat.format(cal.getTime());
				toDate = DateTimeUtils.getCommencingDate1(toDate);
			} else {
				if (toDate.equals("")) {
					DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					Calendar cal = Calendar.getInstance();
					toDate = dateFormat.format(cal.getTime());
					toDate = DateTimeUtils.getCommencingDate1(toDate);
				} else {
					toDate = DateTimeUtils.getCommencingDate1(toDate);
				}
			}
			//lokesh
			int  vendorid= clientReportForm.getVendorid();
			
			String categoryid = clientReportForm.getCategoryid();
			String subcategoryid = clientReportForm.getSubcategoryid();
			
			if(categoryid!=null){
				if(categoryid.equals("")){
					categoryid="0";
				}
			}else{
				categoryid="0";
			}
			
			if(subcategoryid!=null){
				if(subcategoryid.equals("")){
					subcategoryid="0";
				}
			}else{
				subcategoryid="0";
			}
			
	 		ArrayList<Product> usermaterialissuelist = procurementDAO.getItemWisePurchase(fromDate,toDate,product,warehouseid,vendorid,categoryid,subcategoryid);
	 		
	 		ArrayList<Vendor> vendorlist= new ArrayList<Vendor>();
	 		vendorlist= procurementDAO.getVendorList();
	 		
			clientReportForm.setVendorlist(vendorlist);
			
			/*ArrayList<Product> productlist= inventoryProductDAO.getMedicineListforVendor("0");*/
			ArrayList<Product> productlist= new ArrayList<Product>();
			clientReportForm.setProductlist(productlist);
			clientReportForm.setDepartmaterialissuelist(usermaterialissuelist);
			clientReportForm.setFromDate(fromDate = DateTimeUtils.getCommencingDate1(fromDate));
			clientReportForm.setToDate( DateTimeUtils.getCommencingDate1(toDate));
			
			Clinic clinic = new Clinic();
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
			
			clientReportForm.setWarehouse_id(warehouseid);
			ArrayList<Master> warehouseList= inventoryProductDAO.getWareHouseList();
			clientReportForm.setWarehouseList(warehouseList);
			clientReportForm.setClinicName(clinic.getClinicName());
			clientReportForm.setClinicOwner(clinic.getClinicOwner());
			clientReportForm.setOwner_qualification(clinic.getOwner_qualification());
			clientReportForm.setLocationAdressList(locationAdressList);
			clientReportForm.setAddress(clinic.getAddress());
			clientReportForm.setLandLine(clinic.getLandLine());
			clientReportForm.setClinicemail(clinic.getEmail());
			clientReportForm.setWebsiteUrl(clinic.getWebsiteUrl());
			clientReportForm.setClinicLogo(clinic.getUserImageFileName());
			ArrayList<Product> categoryList=inventoryProductDAO.getAllCategories(null);
			//ArrayList<Product> subcategoryList=inventoryProductDAO.getAllSubCategoryList(null);
			ArrayList<Product> subcategoryList = new ArrayList<Product>();
			if(!categoryid.equals("0")){
				subcategoryList = inventoryProductDAO.getSubCategoryList(categoryid);
			}
			clientReportForm.setCategoryList(categoryList);
			clientReportForm.setSubcategoryList(subcategoryList);
			clientReportForm.setCategoryid(categoryid);
			clientReportForm.setSubcategoryid(subcategoryid);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			connection.close();
		}
		return "itemwisepurchase";
	}
	public String pharmacycreditrpt() {
		if(!verifyLogin(request)){
			return "login";
		}
		try{
			
			ArrayList<Breadcrumbs> indentflowlist = new ArrayList<Breadcrumbs>();
			ArrayList<Breadcrumbs> indentflowlisttemp = new ArrayList<Breadcrumbs>();
			if (session.getAttribute("indentflowlist") != null) {
				indentflowlisttemp = (ArrayList<Breadcrumbs>) session.getAttribute("indentflowlist");
			}
			boolean isavilablemodule= false;
			int modulecount =0;
			for (Breadcrumbs breadcrumbs : indentflowlisttemp) {
				breadcrumbs.setIscurrent(false);
				breadcrumbs.setSqno(modulecount);
				modulecount++;
				if(breadcrumbs.getName().equals("Pharmacy Credit Report")){
					isavilablemodule =true;
					breadcrumbs.setIscurrent(true);
					indentflowlist.add(breadcrumbs);
					break;
				}else{
					indentflowlist.add(breadcrumbs);
				}
			}
			if(!isavilablemodule){
				Breadcrumbs breadcrumbs = new Breadcrumbs();
				breadcrumbs.setName("Pharmacy Credit Report");
				breadcrumbs.setOn(true);
				breadcrumbs.setSqno(modulecount);
				breadcrumbs.setUrllink("pharmacycreditrptReport");
				breadcrumbs.setIscurrent(true);
				breadcrumbs.setShowingname("Pharmacy Credit Report");
				indentflowlist.add(breadcrumbs);
			}
			session.setAttribute("indentflowlist",indentflowlist); 
			
			Connection connection = Connection_provider.getconnection();
			Clinic clinic = new Clinic();
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
			clientReportForm.setClinicName(clinic.getClinicName());
			clientReportForm.setClinicOwner(clinic.getClinicOwner());
			clientReportForm.setOwner_qualification(clinic.getOwner_qualification());
			clientReportForm.setLocationAdressList(locationAdressList);
			clientReportForm.setAddress(clinic.getAddress());
			clientReportForm.setLandLine(clinic.getLandLine());
			clientReportForm.setClinicemail(clinic.getEmail());
			clientReportForm.setWebsiteUrl(clinic.getWebsiteUrl());
			clientReportForm.setClinicLogo(clinic.getUserImageFileName());
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "pharmacycreditrpt";
	}
	public String adjustmentreport() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{			
			
			
			String fromDate = clientReportForm.getFromDate();
			String toDate = clientReportForm.getToDate();	
			
			if (fromDate == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar cal = Calendar.getInstance();
				fromDate = dateFormat.format(cal.getTime());
				fromDate = DateTimeUtils.getCommencingDate1(fromDate);
			} else {

				if (fromDate.equals("")) {
					DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					Calendar cal = Calendar.getInstance();
					fromDate = dateFormat.format(cal.getTime());
					fromDate = DateTimeUtils.getCommencingDate1(fromDate);
				} else {
					fromDate = DateTimeUtils.getCommencingDate1(fromDate);
				}
			}

			if (toDate == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar cal = Calendar.getInstance();
				toDate = dateFormat.format(cal.getTime());
				toDate = DateTimeUtils.getCommencingDate1(toDate);
			} else {
				if (toDate.equals("")) {
					DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					Calendar cal = Calendar.getInstance();
					toDate = dateFormat.format(cal.getTime());
					toDate = DateTimeUtils.getCommencingDate1(toDate);
				} else {
					toDate = DateTimeUtils.getCommencingDate1(toDate);
				}
			}
			connection = Connection_provider.getconnection();
		InventoryProductDAO inventoryProductDAO=new JDBCInventoryProductDAO(connection);
		int count= inventoryProductDAO.getadjusmentlistcount(fromDate,toDate);
		pagination.setPreperties(count);
			ArrayList<Product> adjusmentlist = inventoryProductDAO.getadjusmentlist(fromDate,toDate,pagination);
			pagination.setTotal_records(adjusmentlist.size());
			clientReportForm.setAdjustmentlist(adjusmentlist);
			clientReportForm.setFromDate(fromDate = DateTimeUtils.getCommencingDate1(fromDate));
			clientReportForm.setToDate( DateTimeUtils.getCommencingDate1(toDate));
			clientReportForm.setPagerecords(String.valueOf(pagination.getTotal_records()));
			clientReportForm.setTotalRecords(count);
			
			int size = adjusmentlist.size();
			if (size > 0) {
				double ttlprice = adjusmentlist.get(size - 1).getTotalpurchaseprice();
				ttlprice = Math.round(ttlprice * 100.0) / 100.0;
				clientReportForm.setTotalpurchaseprice(DateTimeUtils.changeFormat(ttlprice));
			} else {
				clientReportForm.setTotalpurchaseprice("0.0");
			}
			
			Clinic clinic = new Clinic();
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
			clientReportForm.setClinicName(clinic.getClinicName());
			clientReportForm.setClinicOwner(clinic.getClinicOwner());
			clientReportForm.setOwner_qualification(clinic.getOwner_qualification());
			clientReportForm.setLocationAdressList(locationAdressList);
			clientReportForm.setAddress(clinic.getAddress());
			clientReportForm.setLandLine(clinic.getLandLine());
			clientReportForm.setClinicemail(clinic.getEmail());
			clientReportForm.setWebsiteUrl(clinic.getWebsiteUrl());
			clientReportForm.setClinicLogo(clinic.getUserImageFileName());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			connection.close();
		}
		
		return "adjustmentreport";
	}
public String opdtatreport() throws Exception{
	
	if(!verifyLogin(request)){
		return "login";
	}
	Connection connection = null;
	try{			
		
		String fromDate = clientReportForm.getFromDate();
		String toDate = clientReportForm.getToDate();	
		
		if (fromDate == null) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();
			fromDate = dateFormat.format(cal.getTime());
			fromDate = DateTimeUtils.getCommencingDate1(fromDate);
		} else {

			if (fromDate.equals("")) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar cal = Calendar.getInstance();
				fromDate = dateFormat.format(cal.getTime());
				fromDate = DateTimeUtils.getCommencingDate1(fromDate);
			} else {
				fromDate = DateTimeUtils.getCommencingDate1(fromDate);
			}
		}

		if (toDate == null) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();
			toDate = dateFormat.format(cal.getTime());
			toDate = DateTimeUtils.getCommencingDate1(toDate);
		} else {
			if (toDate.equals("")) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar cal = Calendar.getInstance();
				toDate = dateFormat.format(cal.getTime());
				toDate = DateTimeUtils.getCommencingDate1(toDate);
			} else {
				toDate = DateTimeUtils.getCommencingDate1(toDate);
			}
		}
		connection = Connection_provider.getconnection();
	InventoryProductDAO inventoryProductDAO=new JDBCInventoryProductDAO(connection);
	int count= inventoryProductDAO.getopdtatteportcount(fromDate,toDate,clientReportForm.getDiaryUser());
	pagination.setPreperties(count);
		ArrayList<Product> opdtatreportlist = inventoryProductDAO.getopdtatreportlist(fromDate,toDate,pagination,clientReportForm.getDiaryUser());
		pagination.setTotal_records(opdtatreportlist.size());
		clientReportForm.setOpdtatreportlist(opdtatreportlist);
		clientReportForm.setFromDate( DateTimeUtils.getCommencingDate1(fromDate));
		clientReportForm.setToDate( DateTimeUtils.getCommencingDate1(toDate));
		clientReportForm.setPagerecords(String.valueOf(pagination.getTotal_records()));
		clientReportForm.setTotalRecords(count);
		Clinic clinic = new Clinic();
		ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
		clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
		AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
		ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
		clientReportForm.setClinicName(clinic.getClinicName());
		clientReportForm.setClinicOwner(clinic.getClinicOwner());
		clientReportForm.setOwner_qualification(clinic.getOwner_qualification());
		clientReportForm.setLocationAdressList(locationAdressList);
		clientReportForm.setAddress(clinic.getAddress());
		clientReportForm.setLandLine(clinic.getLandLine());
		clientReportForm.setClinicemail(clinic.getEmail());
		clientReportForm.setWebsiteUrl(clinic.getWebsiteUrl());
		clientReportForm.setClinicLogo(clinic.getUserImageFileName());
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		connection.close();
	}
	
	return "opdtatreport";
}
public String investigationtat() throws Exception{
	
	if(!verifyLogin(request)){
		return "login";
	}
	try{		
		
		
		
	Connection connection = null;
	connection = Connection_provider.getconnection();
	String fromDate = clientReportForm.getFromDate();
	String toDate = clientReportForm.getToDate();	
	String invsttype=clientReportForm.getInvsttype();
	String filter_status = clientReportForm.getFilter_status();
	String filter_ward = clientReportForm.getFilter_ward();
	String isdeleted= clientReportForm.getIsdeleted();
	EmrDAO emrDAO=new JDBCEmrDAO(connection);
	ArrayList<Master>invsTypeList = emrDAO.getInvesigationTypeList();
	clientReportForm.setInvsTypeList(invsTypeList);
	
	
	if(isdeleted==null){
		isdeleted="";
	}
	if(invsttype==null){
		invsttype="";
	}
		if(invsttype.equals("0")){
			invsttype="";
		}
		if(filter_status==null){
			filter_status="0";
		}else if(filter_status.equals("")){
			filter_status="0";
		}
		
		if(filter_ward==null){
			filter_ward="0";
		}else if(filter_ward.equals("")){
			filter_ward="0";
		}
	if (fromDate == null) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Calendar cal = Calendar.getInstance();
		fromDate = dateFormat.format(cal.getTime());
		fromDate = DateTimeUtils.getCommencingDate1(fromDate);
	} else {

		if (fromDate.equals("")) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();
			fromDate = dateFormat.format(cal.getTime());
			fromDate = DateTimeUtils.getCommencingDate1(fromDate);
		} else {
			fromDate = DateTimeUtils.getCommencingDate1(fromDate);
		}
	}

	if (toDate == null) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Calendar cal = Calendar.getInstance();
		toDate = dateFormat.format(cal.getTime());
		toDate = DateTimeUtils.getCommencingDate1(toDate);
	} else {
		if (toDate.equals("")) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();
			toDate = dateFormat.format(cal.getTime());
			toDate = DateTimeUtils.getCommencingDate1(toDate);
		} else {
			toDate = DateTimeUtils.getCommencingDate1(toDate);
		}
	}
	InvestigationDAO investigationDAO=new JDBCInvestigationDAO(connection);
	int count= investigationDAO.getInvestigationTatcount(fromDate,toDate,invsttype,filter_status,filter_ward,isdeleted,loginInfo);
	pagination.setPreperties(count);
	
	     
		ArrayList<Investigation> investigationtatlist = investigationDAO.getInvestigationTat(fromDate,toDate,invsttype,filter_status,filter_ward,isdeleted,pagination,loginInfo);
		
		pagination.setTotal_records(investigationtatlist.size());
		clientReportForm.setInvestigationtatlist(investigationtatlist);
		clientReportForm.setFromDate(fromDate = DateTimeUtils.getCommencingDate1(fromDate));
		clientReportForm.setToDate( DateTimeUtils.getCommencingDate1(toDate));
		clientReportForm.setPagerecords(String.valueOf(pagination.getTotal_records()));
		clientReportForm.setTotalRecords(count);
		Clinic clinic = new Clinic();
		ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
		clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
		AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
		ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
		clientReportForm.setClinicName(clinic.getClinicName());
		clientReportForm.setClinicOwner(clinic.getClinicOwner());
		clientReportForm.setOwner_qualification(clinic.getOwner_qualification());
		clientReportForm.setLocationAdressList(locationAdressList);
		clientReportForm.setAddress(clinic.getAddress());
		clientReportForm.setLandLine(clinic.getLandLine());
		clientReportForm.setClinicemail(clinic.getEmail());
		clientReportForm.setWebsiteUrl(clinic.getWebsiteUrl());
		clientReportForm.setClinicLogo(clinic.getUserImageFileName());
	} catch (Exception e) {
		e.printStackTrace();
	}
	return "investigationtat";
	
}
public String otreport(){

	if(!verifyLogin(request)){
		return "login";
	}
	try{		
		
		
		
	Connection connection = null;
	connection = Connection_provider.getconnection();
	String fromDate = clientReportForm.getFromDate();
	String toDate = clientReportForm.getToDate();
	String otroom=clientReportForm.getOtroom();
	String otuser=clientReportForm.getOtuser1();
	String anesthesia=clientReportForm.getAnesthesia();
	EmrDAO emrDAO=new JDBCEmrDAO(connection);
	UserProfileDAO userProfileDAO=new JDBCUserProfileDAO(connection);
	ArrayList<UserProfile>surgeonlist = userProfileDAO.getSurgenList("1");
	ArrayList<Master>otroomlist = emrDAO.getOtroomList();
	ArrayList<Client> anesthesiaList =  userProfileDAO.getAllAnethesiaList();
	clientReportForm.setOtroomlist(otroomlist);
	clientReportForm.setSurgeonlist(surgeonlist);
	clientReportForm.setAnesthesiaList(anesthesiaList);
	if (fromDate == null) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Calendar cal = Calendar.getInstance();
		fromDate = dateFormat.format(cal.getTime());
		fromDate = DateTimeUtils.getCommencingDate1(fromDate);
	} else {

		if (fromDate.equals("")) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();
			fromDate = dateFormat.format(cal.getTime());
			fromDate = DateTimeUtils.getCommencingDate1(fromDate);
		} else {
			fromDate = DateTimeUtils.getCommencingDate1(fromDate);
		}
	}

	if (toDate == null) {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Calendar cal = Calendar.getInstance();
		toDate = dateFormat.format(cal.getTime());
		toDate = DateTimeUtils.getCommencingDate1(toDate);
	} else {
		if (toDate.equals("")) {
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();
			toDate = dateFormat.format(cal.getTime());
			toDate = DateTimeUtils.getCommencingDate1(toDate);
		} else {
			toDate = DateTimeUtils.getCommencingDate1(toDate);
		}
	}
	if(otroom==null){
		otroom="0";
	}
	if(otuser==null){
		otuser="0";
	}
	if(anesthesia==null){
		anesthesia="";
	}
	SummaryReportDAO summaryReportDAO=new JDBCSummaryReportDAO(connection);
	int count= summaryReportDAO.getOtReportCount(fromDate,toDate,otroom,otuser);
	pagination.setPreperties(count);
	
	     
		ArrayList<SummaryReport> otreportlist = summaryReportDAO.getOtReport(fromDate, toDate, pagination,otroom,otuser,anesthesia);
		
		pagination.setTotal_records(otreportlist.size());
		clientReportForm.setOtreportlist(otreportlist);
		clientReportForm.setFromDate(fromDate = DateTimeUtils.getCommencingDate1(fromDate));
		clientReportForm.setToDate( DateTimeUtils.getCommencingDate1(toDate));
		clientReportForm.setPagerecords(String.valueOf(pagination.getTotal_records()));
		clientReportForm.setTotalRecords(count);
		Clinic clinic = new Clinic();
		ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
		clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
		AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
		ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
		clientReportForm.setClinicName(clinic.getClinicName());
		clientReportForm.setClinicOwner(clinic.getClinicOwner());
		clientReportForm.setOwner_qualification(clinic.getOwner_qualification());
		clientReportForm.setLocationAdressList(locationAdressList);
		clientReportForm.setAddress(clinic.getAddress());
		clientReportForm.setLandLine(clinic.getLandLine());
		clientReportForm.setClinicemail(clinic.getEmail());
		clientReportForm.setWebsiteUrl(clinic.getWebsiteUrl());
		clientReportForm.setClinicLogo(clinic.getUserImageFileName());
		clientReportForm.setOtroom(otroom);
		clientReportForm.setOtuser(otuser);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return "otreport";
	
}

public String revenuematrix() throws Exception{
	if(!verifyLogin(request)){
		return "login";
	}
	Connection connection = null;
	try {
		connection = Connection_provider.getconnection();
		String todate = clientReportForm.getToDate();
		SummaryReportDAO summaryReportDAO = new JDBCSummaryReportDAO(connection);
		if(todate== null){
			DateFormat dateFormat = new SimpleDateFormat("yyyy");
			Calendar cal = Calendar.getInstance(); 
			todate = dateFormat.format(cal.getTime());
			
		}else{
			if(todate.equals("")){
			    DateFormat dateFormat = new SimpleDateFormat("yyyy");
			    Calendar cal = Calendar.getInstance(); 
			    todate = dateFormat.format(cal.getTime());
			    
			}
		}
		
		String ipdopd=clientReportForm.getIpdopd();
		if(ipdopd==null){
			ipdopd="";
		}
		
		String newtodate = todate;
		String fromdate="";
		fromdate = "01-01-"+newtodate+"";
		todate = "31-12-"+newtodate+"";
		fromdate = DateTimeUtils.getCommencingDate1(fromdate);  
		todate = DateTimeUtils.getCommencingDate1(todate);
		
		ArrayList<SummaryReport> ipdrevenuematrix = summaryReportDAO.getipdrevenueRevenue(fromdate,todate,newtodate);
		clientReportForm.setIpdrevenuematrix(ipdrevenuematrix);
		//lokesh
		SummaryReport sr1= ipdrevenuematrix.get(ipdrevenuematrix.size()-1);
		double jan,feb,mar,apr,may,jun,jul,aug,sep,oct,nov,dec;
		jan=Double.parseDouble(sr1.getJancash())+Double.parseDouble(sr1.getJancheque())+Double.parseDouble(sr1.getJandcard())+Double.parseDouble(sr1.getJanneft())+Double.parseDouble(sr1.getJanprepay());
		feb=Double.parseDouble(sr1.getFebcash())+Double.parseDouble(sr1.getFebcheque())+Double.parseDouble(sr1.getFebdcard())+Double.parseDouble(sr1.getFebneft())+Double.parseDouble(sr1.getFebprepay());
		mar=Double.parseDouble(sr1.getMarcash())+Double.parseDouble(sr1.getMarcheque())+Double.parseDouble(sr1.getMardcard())+Double.parseDouble(sr1.getMarneft())+Double.parseDouble(sr1.getMarprepay());
		apr=Double.parseDouble(sr1.getAprcash())+Double.parseDouble(sr1.getAprcheque())+Double.parseDouble(sr1.getAprdcard())+Double.parseDouble(sr1.getAprneft())+Double.parseDouble(sr1.getAprprepay());
		may=Double.parseDouble(sr1.getMaycash())+Double.parseDouble(sr1.getMaycheque())+Double.parseDouble(sr1.getMaydcard())+Double.parseDouble(sr1.getMayneft())+Double.parseDouble(sr1.getMayprepay());
		jun=Double.parseDouble(sr1.getJuncash())+Double.parseDouble(sr1.getJuncheque())+Double.parseDouble(sr1.getJundcard())+Double.parseDouble(sr1.getJunneft())+Double.parseDouble(sr1.getJunprepay());
		jul=Double.parseDouble(sr1.getJulcash())+Double.parseDouble(sr1.getJulcheque())+Double.parseDouble(sr1.getJuldcard())+Double.parseDouble(sr1.getJulneft())+Double.parseDouble(sr1.getJulprepay());
		aug=Double.parseDouble(sr1.getAugcash())+Double.parseDouble(sr1.getAugcheque())+Double.parseDouble(sr1.getAugdcard())+Double.parseDouble(sr1.getAugneft())+Double.parseDouble(sr1.getAugprepay());
		sep=Double.parseDouble(sr1.getSepcash())+Double.parseDouble(sr1.getSepcheque())+Double.parseDouble(sr1.getSepdcard())+Double.parseDouble(sr1.getSepneft())+Double.parseDouble(sr1.getSepprepay());
		oct=Double.parseDouble(sr1.getOctcash())+Double.parseDouble(sr1.getOctcheque())+Double.parseDouble(sr1.getOctdcard())+Double.parseDouble(sr1.getOctneft())+Double.parseDouble(sr1.getOctprepay());
		nov=Double.parseDouble(sr1.getNovcash())+Double.parseDouble(sr1.getNovcheque())+Double.parseDouble(sr1.getNovdcard())+Double.parseDouble(sr1.getNovneft())+Double.parseDouble(sr1.getNovprepay());
		dec=Double.parseDouble(sr1.getDeccash())+Double.parseDouble(sr1.getDeccheque())+Double.parseDouble(sr1.getDecdcard())+Double.parseDouble(sr1.getDecneft())+Double.parseDouble(sr1.getDecprepay());
		clientReportForm.setJan(DateTimeUtils.changeFormat(jan));
		clientReportForm.setFeb(DateTimeUtils.changeFormat(feb));
		clientReportForm.setMar(DateTimeUtils.changeFormat(mar));
		clientReportForm.setApr(DateTimeUtils.changeFormat(apr));
		clientReportForm.setMay(DateTimeUtils.changeFormat(may));
		clientReportForm.setJun(DateTimeUtils.changeFormat(jun));
		clientReportForm.setJul(DateTimeUtils.changeFormat(jul));
		clientReportForm.setAug(DateTimeUtils.changeFormat(aug));
		clientReportForm.setSep(DateTimeUtils.changeFormat(sep));
		clientReportForm.setOct(DateTimeUtils.changeFormat(oct));
		clientReportForm.setNov(DateTimeUtils.changeFormat(nov));
		clientReportForm.setDec(DateTimeUtils.changeFormat(dec));
		clientReportForm.setToDate(newtodate);
		//lokesh
		Clinic clinic = new Clinic();
		ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
		clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
		AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
		ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
		clientReportForm.setClinicName(clinic.getClinicName());
		clientReportForm.setClinicOwner(clinic.getClinicOwner());
		clientReportForm.setOwner_qualification(clinic.getOwner_qualification());
		clientReportForm.setLocationAdressList(locationAdressList);
		clientReportForm.setAddress(clinic.getAddress());
		clientReportForm.setLandLine(clinic.getLandLine());
		clientReportForm.setClinicemail(clinic.getEmail());
		clientReportForm.setWebsiteUrl(clinic.getWebsiteUrl());
		clientReportForm.setClinicLogo(clinic.getUserImageFileName());
	} catch (Exception e) {
		e.printStackTrace();
	}finally {
		connection.close();
	}
	return "revenuematrix";
}

public String nosofadmdisc() throws Exception{
	if(!verifyLogin(request)){
		return "login";
	}
	Connection connection = null;
	try {
		connection = Connection_provider.getconnection();
		String todate = clientReportForm.getToDate();
		SummaryReportDAO summaryReportDAO = new JDBCSummaryReportDAO(connection);
		if(todate== null){
			DateFormat dateFormat = new SimpleDateFormat("yyyy");
			Calendar cal = Calendar.getInstance(); 
			todate = dateFormat.format(cal.getTime());
			
		}else{
			if(todate.equals("")){
			    DateFormat dateFormat = new SimpleDateFormat("yyyy");
			    Calendar cal = Calendar.getInstance(); 
			    todate = dateFormat.format(cal.getTime());
			    
			}
		}
		
		
		String newtodate = todate;
		String fromdate="";
		fromdate = "01-01-"+newtodate+"";
		todate = "31-12-"+newtodate+"";
		fromdate = DateTimeUtils.getCommencingDate1(fromdate);  
		todate = DateTimeUtils.getCommencingDate1(todate);
		
		ArrayList<SummaryReport> nosofadmissiondischarge = summaryReportDAO.getNosOfadmdischarge(fromdate,todate,newtodate);
		clientReportForm.setNosofadmissiondischarge(nosofadmissiondischarge);
		clientReportForm.setToDate(newtodate);
		Clinic clinic = new Clinic();
		ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
		clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
		AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
		ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
		clientReportForm.setClinicName(clinic.getClinicName());
		clientReportForm.setClinicOwner(clinic.getClinicOwner());
		clientReportForm.setOwner_qualification(clinic.getOwner_qualification());
		clientReportForm.setLocationAdressList(locationAdressList);
		clientReportForm.setAddress(clinic.getAddress());
		clientReportForm.setLandLine(clinic.getLandLine());
		clientReportForm.setClinicemail(clinic.getEmail());
		clientReportForm.setWebsiteUrl(clinic.getWebsiteUrl());
		clientReportForm.setClinicLogo(clinic.getUserImageFileName());
	} catch (Exception e) {
		e.printStackTrace();
	}finally {
		connection.close();
	}
	return "nosofadmdisc";
}
	//Akash 12 Nov 2019
	public String departmaterialsummary() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		Connection connection = null;
		try{			
			connection = Connection_provider.getconnection();
			InventoryProductDAO inventoryProductDAO = new JDBCInventoryProductDAO(connection);
			String fromDate = clientReportForm.getFromDate();
			String toDate = clientReportForm.getToDate();	
			String department= clientReportForm.getDepartment();
			String warehouseid = clientReportForm.getWarehouse_id();
			String category = clientReportForm.getCat_filter();
			if(department==null){
				department= "";
			}if(department.equals("0")){
				department= "";
			}
			
			if(warehouseid==null){
				warehouseid= "36";
			}if(warehouseid.equals("0") || warehouseid.equals("")){
				warehouseid= "36";
			}
			
			if(category!=null){
				if(category.equals("")){
					category="0";
				}
			}else{
				category="0";
			}
			clientReportForm.setCat_filter(category);
			if (fromDate == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar cal = Calendar.getInstance();
				fromDate = dateFormat.format(cal.getTime());
				fromDate = DateTimeUtils.getCommencingDate1(fromDate);
			} else {

				if (fromDate.equals("")) {
					DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					Calendar cal = Calendar.getInstance();
					fromDate = dateFormat.format(cal.getTime());
					fromDate = DateTimeUtils.getCommencingDate1(fromDate);
				} else {
					fromDate = DateTimeUtils.getCommencingDate1(fromDate);
				}
			}

			if (toDate == null) {
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
				Calendar cal = Calendar.getInstance();
				toDate = dateFormat.format(cal.getTime());
				toDate = DateTimeUtils.getCommencingDate1(toDate);
			} else {
				if (toDate.equals("")) {
					DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
					Calendar cal = Calendar.getInstance();
					toDate = dateFormat.format(cal.getTime());
					toDate = DateTimeUtils.getCommencingDate1(toDate);
					// todate = null;
				} else {
					toDate = DateTimeUtils.getCommencingDate1(toDate);
				}
			}
	 		
			MasterDAO masterdao= new JDBCMasterDAO(connection);
			ArrayList<Product> departmaterialissuelist = inventoryProductDAO.getDeptMaterialSummaryList(fromDate,toDate,department,warehouseid,category);
			ArrayList<Master> departmentlist= masterdao.getAllLocation(null);
			ArrayList<Product> productlist= new ArrayList<Product>();
			clientReportForm.setProductlist(productlist);
			clientReportForm.setDepartmentlist(departmentlist);
	 		clientReportForm.setDepartmaterialissuelist(departmaterialissuelist);
			clientReportForm.setFromDate(fromDate = DateTimeUtils.getCommencingDate1(fromDate));
			clientReportForm.setToDate( DateTimeUtils.getCommencingDate1(toDate));
			clientReportForm.setDepartment(department);
			clientReportForm.setWarehouse_id(warehouseid);
			Clinic clinic = new Clinic();
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
			ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());
			ArrayList<Master> warehouseList= inventoryProductDAO.getWareHouseList();
			clientReportForm.setWarehouseList(warehouseList);
			clientReportForm.setClinicName(clinic.getClinicName());
			clientReportForm.setClinicOwner(clinic.getClinicOwner());
			clientReportForm.setOwner_qualification(clinic.getOwner_qualification());
			clientReportForm.setLocationAdressList(locationAdressList);
			clientReportForm.setAddress(clinic.getAddress());
			clientReportForm.setLandLine(clinic.getLandLine());
			clientReportForm.setClinicemail(clinic.getEmail());
			clientReportForm.setWebsiteUrl(clinic.getWebsiteUrl());
			clientReportForm.setClinicLogo(clinic.getUserImageFileName());
			
			ArrayList<Product> categoryList=inventoryProductDAO.getAllCategories(null);
			clientReportForm.setCategoryList(categoryList);
			
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			connection.close();
		}
		
		return "departmaterialsummary";
	}

}
