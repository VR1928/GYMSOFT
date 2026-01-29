package com.apm.Pacs.web.form;

import java.io.File;
import java.util.ArrayList;

import com.apm.Master.eu.entity.Master;
import com.apm.Pacs.eu.entity.Pacs;

public class PacsForm {
    
	private int id;
	
	private File[] fileUpload;

	private File[] files;
	
	private String[] fileUploadFileName;

	private String[] filesFileName;
	
	ArrayList<Master>invstList;
	
	private String invstid;
	
	private String selectedinvstid;
	
	private String fromDate = "";
	private String toDate = "";
	
	ArrayList<Master>modalityList;
	
	private String selectedmodality;
	
	private String searchText = "";
	
	private String searchInvstid = "";
	
	ArrayList<Pacs>pacsdataList;
	
	private String otnotes;
	
	private String clientname;
	
	private String clientid;
	
	ArrayList<Pacs>folderList;
	

	public String getOtnotes() {
		return otnotes;
	}

	public void setOtnotes(String otnotes) {
		this.otnotes = otnotes;
	}

	public ArrayList<Pacs> getPacsdataList() {
		return pacsdataList;
	}

	public void setPacsdataList(ArrayList<Pacs> pacsdataList) {
		this.pacsdataList = pacsdataList;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public String getSelectedmodality() {
		return selectedmodality;
	}

	public void setSelectedmodality(String selectedmodality) {
		this.selectedmodality = selectedmodality;
	}

	public ArrayList<Master> getModalityList() {
		return modalityList;
	}

	public void setModalityList(ArrayList<Master> modalityList) {
		this.modalityList = modalityList;
	}

	public String getInvstid() {
		return invstid;
	}

	public void setInvstid(String invstid) {
		this.invstid = invstid;
	}

	public ArrayList<Master> getInvstList() {
		return invstList;
	}

	public void setInvstList(ArrayList<Master> invstList) {
		this.invstList = invstList;
	}

	public File[] getFileUpload() {
		return fileUpload;
	}

	public void setFileUpload(File[] fileUpload) {
		this.fileUpload = fileUpload;
	}

	public File[] getFiles() {
		return files;
	}

	public void setFiles(File[] files) {
		this.files = files;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String[] getFileUploadFileName() {
		return fileUploadFileName;
	}

	public void setFileUploadFileName(String[] fileUploadFileName) {
		this.fileUploadFileName = fileUploadFileName;
	}

	public String[] getFilesFileName() {
		return filesFileName;
	}

	public void setFilesFileName(String[] filesFileName) {
		this.filesFileName = filesFileName;
	}

	public String getSelectedinvstid() {
		return selectedinvstid;
	}

	public void setSelectedinvstid(String selectedinvstid) {
		this.selectedinvstid = selectedinvstid;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getSearchInvstid() {
		return searchInvstid;
	}

	public void setSearchInvstid(String searchInvstid) {
		this.searchInvstid = searchInvstid;
	}

	public String getClientname() {
		return clientname;
	}

	public void setClientname(String clientname) {
		this.clientname = clientname;
	}

	public String getClientid() {
		return clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public ArrayList<Pacs> getFolderList() {
		return folderList;
	}

	public void setFolderList(ArrayList<Pacs> folderList) {
		this.folderList = folderList;
	}
	
	
}
