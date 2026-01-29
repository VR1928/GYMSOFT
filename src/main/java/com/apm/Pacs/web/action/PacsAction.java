package com.apm.Pacs.web.action;

import ij.Executer;
import ij.IJ;
import ij.ImagePlus;
import ij.Menus;
import ij.WindowManager;
import ij.gui.ImageCanvas;
import ij.io.FileInfo;
import ij.io.FileOpener;
import ij.io.Opener;
import ij.plugin.DicomDecoder;
import ij.plugin.JpegWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import com.apm.DiaryManagement.eu.bi.ClientDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCClientDAO;
import com.apm.DiaryManagement.eu.entity.Client;
import com.apm.Master.eu.entity.Master;
import com.apm.Pacs.eu.bi.PacsDAO;
import com.apm.Pacs.eu.blogic.JDBCPacsDAO;
import com.apm.Pacs.eu.entity.Pacs;
import com.apm.Pacs.web.form.PacsForm;
import com.apm.common.eu.blogic.jdbc.Connection_provider;
import com.apm.common.utils.DateTimeUtils;
import com.apm.common.web.action.BaseAction;
import com.apm.common.web.common.helper.LoginHelper;
import com.apm.common.web.common.helper.LoginInfo;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

public class PacsAction extends BaseAction implements ModelDriven<PacsForm>,Preparable {

	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
	HttpServletResponse response = (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
	HttpSession session = request.getSession(true);
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	PacsForm pacsform=new PacsForm();
	
	public PacsForm getModel() {
		// TODO Auto-generated method stub
		return pacsform;
	}


	@Override
	public String execute() throws Exception {

		if(!verifyLogin(request)){
			return "login";
		}
		
		String clientid = request.getParameter("clientid");
		
		String fromDate = pacsform.getFromDate();
		String toDate = pacsform.getToDate();	
		
		if(fromDate.equals("")){
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -7); 
			fromDate = dateFormat.format(cal.getTime());
			pacsform.setFromDate(fromDate);
		}
		if(toDate.equals("")){
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.DATE, -7); 
			toDate = dateFormat.format(cal.getTime());
			pacsform.setToDate(toDate);
		}
		
		
		if(!fromDate.equals("")){
			String temp[]= fromDate.split("/");
			fromDate = temp[2]+"-"+temp[1]+"-"+temp[0];
		}
		if(!toDate.equals("")){
			String temp1[]= toDate.split("/");
			toDate = temp1[2]+"-"+temp1[1]+"-"+temp1[0];
		}
		
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			PacsDAO pacsDAO = new JDBCPacsDAO(connection);
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			
			ArrayList<Pacs>pacsdataList = pacsDAO.getwebpacsList(connection,pacsform.getSearchText(),fromDate,toDate,pacsform.getSelectedmodality(),pacsform.getSearchInvstid(),clientid,loginInfo.getPacsip());
			pacsform.setPacsdataList(pacsdataList);
			
			if(!clientid.equals("0")){
				Client client = clientDAO.getClientDetails(clientid);
				String fiullname = client.getTitle() + " " + client.getFirstName() + " " + client.getLastName();
				pacsform.setClientname(fiullname);
				
			}
			pacsform.setClientid(clientid);
			
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			
			connection.close();
		}
		
		return super.execute();
	}
	
	public String download() throws IOException{
		FileInputStream in = null;
		ServletOutputStream out = null;
		/*HttpServletRequest servletRequest=(HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
		HttpServletResponse response = (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);*/
		
		String filename = request.getParameter("fname");
		String filePath = "/liveData/emailAttachments/";
		 File file=new File(request.getRealPath(filePath+filename+""));
		 if(file.exists()){
		//return an application file instead of html page
		//response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition","attachment;filename="+filename+"");
	
        	 try {
				in = new FileInputStream(file);
				 out = response.getOutputStream();
	        	 
		        	byte[] outputByte = new byte[4096];
		        	//copy binary contect to output stream
		        	while(in.read(outputByte, 0, 4096) != -1)
		        	{
		        		out.write(outputByte, 0, 4096);
		        	}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        
		 }
        	
        	in.close();
        	out.flush();
        	out.close();
		 
		return null;
	}

	
	public String wordfile() throws Exception{
		String fileName = "";
		for (int i = 0; i < pacsform.getFileUpload().length; i++) {
            File uploadedFile = pacsform.getFileUpload()[i];
            fileName = pacsform.getFileUploadFileName()[i];
            
            //save dicom info 
           
	 				/*FileOpener opener = new FileOpener(fi);
	 				opener.open();*/
	 			
	 			
	 			
	 		// save dicom image
	 			String filePath = request.getRealPath("/liveData/emailAttachments/");
	 			 fileName = pacsform.getSelectedinvstid() + "finding_" + fileName;
	 			 
		 			File fileToCreate = new File(filePath, fileName);
	            	try {
						FileUtils.copyFile(uploadedFile, fileToCreate);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	
		} 
		
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			PacsDAO pacsDAO = new JDBCPacsDAO(connection);
			
			int upd = pacsDAO.updatewordfilename(pacsform.getSelectedinvstid(),fileName);
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			
			connection.close();
		}
		
		
		
		
		return folder();
	}
	
	
	public String folder() throws Exception{
		if(!verifyLogin(request)){
			return "login";
		}
		
		String clientid = request.getParameter("clientid");
		
		String fromDate = pacsform.getFromDate();
		String toDate = pacsform.getToDate();	
		
		if(fromDate.equals("")){
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -7); 
			fromDate = dateFormat.format(cal.getTime());
			pacsform.setFromDate(fromDate);
		}
		if(toDate.equals("")){
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Calendar cal = Calendar.getInstance();
			//cal.add(Calendar.DATE, -7); 
			toDate = dateFormat.format(cal.getTime());
			pacsform.setToDate(toDate);
		}
		
		
		if(!fromDate.equals("")){
			String temp[]= fromDate.split("/");
			fromDate = temp[2]+"-"+temp[1]+"-"+temp[0];
		}
		if(!toDate.equals("")){
			String temp1[]= toDate.split("/");
			toDate = temp1[2]+"-"+temp1[1]+"-"+temp1[0];
		}
		
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			PacsDAO pacsDAO = new JDBCPacsDAO(connection);
			
			ArrayList<Pacs>folderList = pacsDAO.getFolderList(fromDate,toDate);
			pacsform.setFolderList(folderList);
			
			
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			
			connection.close();
		}
		
		
		return "folder";
	}
	
	public String multi(){
		 String fileName = "";
		 Connection connection = null;
		 String id = request.getParameter("id");
		try{
			connection = Connection_provider.getconnection();
			PacsDAO pacsDAO = new JDBCPacsDAO(connection);
			
			int t = pacsDAO.truncatemultipacs(loginInfo.getUserId());
			
			  String sql = "select dicomimg,filename,id from dicom_list where multpacsid = "+id+"   ";
			  PreparedStatement preparedStatement = connection.prepareStatement(sql);
			  ResultSet rs = preparedStatement.executeQuery();
			  while(rs.next()){
				  int dicomid = rs.getInt(3);
				  int r = pacsDAO.insertDicomid(dicomid,loginInfo.getUserId());
				  InputStream in = rs.getBinaryStream(1);
				  fileName = id + "_" + rs.getString(2);
				  String path = request.getRealPath("/liveData/dicom/"+fileName);
				  File file = new File(path);
					if(!file.exists()){
						System.out.println("file not exist");
						OutputStream f = new FileOutputStream(new File(path));
						
						int c = 0;
						while ((c = in.read()) > -1) {
							f.write(c);
						}
						f.close();
						in.close();
					}
					
					
					String directory = request.getRealPath("/liveData/dicom/");
					String fname = directory+"/" +fileName+".jpeg";
					file = new File(fname);
					if(!file.exists()){
						DicomDecoder dd = new DicomDecoder(directory+"/",  fileName);
						dd.insertid = 0;
						FileInfo fi = null;
						
						fi = dd.getFileInfo();
						
						//String info = dd.getDicomInfo();
			 			//System.out.println(info);
		  		 
					
							String inputURL=directory + fileName;
				 			FileOpener opener = new FileOpener(fi);
							//opener.open();
				 			ImagePlus imp = opener.openImage();
				 			 path = directory+"/";
				 			String err = JpegWriter.save(imp, path +fileName+".jpeg", 85);
						}
		  		       
					
			  }
			  
			  
			  int pacscount = pacsDAO.getPacsCount(id);
			  ArrayList<Pacs>pimglist = pacsDAO.getPacsImgList(id);
			  session.setAttribute("pacscount", pacscount);
			  session.setAttribute("multipacsid", id);
			  session.setAttribute("pimglist", pimglist);
			  
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return "multiviewer";
	}
	
	public String edit() throws Exception{
		String id = request.getParameter("id");
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			PacsDAO pacsDAO = new JDBCPacsDAO(connection);
			
			String finding = pacsDAO.getSelectedFinding(id);
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""+finding+""); 
			
			
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			
			connection.close();
		}
		
		return null;
	}
	public String savedicom() throws Exception{
		Connection connection = null;
		 String directory = "C:/dicom/";
		 String fileName = "";
		
		 // copy the uploaded files into pre-configured location
        for (int i = 0; i < pacsform.getFileUpload().length; i++) {
            File uploadedFile = pacsform.getFileUpload()[i];
            fileName = pacsform.getFileUploadFileName()[i];
            
            //save dicom info 
           
	 				/*FileOpener opener = new FileOpener(fi);
	 				opener.open();*/
	 			
	 			
	 			
	 		// save dicom image
	 			String filePath = request.getRealPath("/liveData/emailAttachments/");
	 			 fileName = pacsform.getSelectedinvstid() + "_" + fileName;
	 			 
		 			File fileToCreate = new File(filePath, fileName);
	            	FileUtils.copyFile(uploadedFile, fileToCreate);
	            	
	            	
	            	 DicomDecoder dd = new DicomDecoder(filePath, fileName);
	            	dd.directory = filePath + "/";
	            	dd.fileName = fileName;
	 	 			dd.insertid = 0;
	 	 			dd.invstid = pacsform.getSelectedinvstid();
	 	 			dd.lastmodified = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
	 	 			
	 	 			FileInfo fi = null;
	 	 			try {
	 	 				 
	 	 				fi = dd.getFileInfo();
	 	 			String info = dd.getDicomInfo();
	 	 			System.out.println(info);
	 	 			
	 	 			
	            	
	            	PreparedStatement preparedStatement = null;
					String sql = "update dicom_list set dicomimg=? where id = "+dd.insertid+"";
					int re = 0;
					
					 File imgfile = new File(filePath + "/" + fileName);
					 FileInputStream fin = new FileInputStream(imgfile);
					 
					connection = Connection_provider.getconnection();
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setBinaryStream(1,(InputStream)fin,(int)imgfile.length());
					re = preparedStatement.executeUpdate();
					
	 			
	 			
	 		 
	 			
        
           
            	
            } catch (IOException ex) {
                System.out.println("Could not copy file " + fileName);
                ex.printStackTrace();
            }finally{
        		
        		connection.close();
        	}
        }
		
		return "savedicom";
	}
	
	
	public String finding() throws Exception{
		Connection connection = null;
		try{
			
			String txt = pacsform.getOtnotes();
			;
			
			connection = Connection_provider.getconnection();
			PacsDAO pacsDAO = new JDBCPacsDAO(connection);
			int selectedid = pacsform.getId();
			
			int r = pacsDAO.updatefinding(selectedid,txt);
			
			String invstid = pacsDAO.getSelectedInvstid(selectedid);
			
			session.setAttribute("selectedinvstid", invstid);
			
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			
			connection.close();
		}
		
		return "editfinding";
	}
	
	public void prepare() throws Exception {
		Connection connection = null;
		try{
			connection = Connection_provider.getconnection();
			PacsDAO pacsDAO = new JDBCPacsDAO(connection);
			String curdate = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
			String temp[] = curdate.split(" ");
			curdate = temp[0];
			ArrayList<Master>invstList = pacsDAO.getInvstList(curdate);
			pacsform.setInvstList(invstList);
			
			//modality list
			ArrayList<Master>modalityList = pacsDAO.getModalityList();
			pacsform.setModalityList(modalityList);
			
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			
			connection.close();
		}
		
	}

	
	
	
	
}
