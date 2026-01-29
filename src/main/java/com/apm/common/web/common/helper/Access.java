package com.apm.common.web.common.helper;

public class Access {
	
	
	
	 private boolean token_display;
	 private boolean diarymanagement;
	 private boolean appointmentbooking;
	 private boolean basicfinance;
	 private boolean fullfinance;
	 private boolean medicalrecord;
	 private boolean communication;
	 private boolean report;
	 private boolean assessmentForm;
	 private boolean manageclient;
	 private boolean manageclinic;
	 private boolean managemaster;
	 private boolean manageprisc;
	 private boolean manageinvst;
	 private boolean manageipd;
	 private boolean manageopd;
	 private boolean apmtfinder;
	 private boolean manageemr;
	 private boolean expences;
	 private boolean managemis;
	 
	 private boolean payroll;
	 private boolean bloodbak;
	 private boolean inventory;
	 private boolean discharge;
	 
	 private boolean packs;
	 private boolean investigation_chart;
	 private boolean sheduler;
	 private boolean housekeeping;
	 private boolean dietery;
	 private boolean cafeteria;
	 private boolean packages;
	 private boolean ambulance;
	 private boolean bank_deposite;
	 private boolean account_reconcilation;
	
	 //new terms added for personal widget
	 private boolean ot;
		private boolean casualty;
		private boolean pharmacy;
		private boolean mrd;
		private boolean marketing;
		private boolean voice_recording;
		
		private boolean globalaccess;
	
		
		//opd access variables
		
		private boolean opd_modify;
		private boolean opd_cancel;
		private boolean opd_prescription;
		private boolean opd_investigation;
		private boolean opd_ot;
		private boolean opd_viewaccount;
		private boolean opd_apmtfinder;
		private boolean opd_advandref;
		private boolean opd_modifydiagnosis;
		private boolean opd_editpatient;
		private boolean opd_log;
		private boolean opd_emr;
		private boolean opd_assessmentform;
		private boolean opd_treatment;
		
		private boolean opd_addcharges;
		private boolean opd_createinvoice;
		private boolean opd_recordpayment;
		
		
		//ipd access variables
		
		
		private boolean ipd_admission;
		private boolean ipd_declaration;
		private boolean ipd_log;
		private boolean ipd_forms;
		private boolean ipd_discharge;
		private boolean ipd_emr;
		private boolean ipd_prescription;
		private boolean ipd_investigation;
		private boolean ipd_reqconsultant;
		private boolean ipd_nursingcare;
		private boolean ipd_reqblood;
		private boolean ipd_autocare;
		private boolean ipd_treatmentlog;
		private boolean ipd_addcharges;
		private boolean ipd_createinvoice;
		private boolean ipd_recordpayment;
		private boolean ipd_advandref;
		private boolean ipd_viewaccount;
		private boolean ipd_packages;
		
		
		//account access variables
		//, , , , , , 
		private boolean acc_createinvoice;
		private boolean acc_recordpayment;
		private boolean acc_viewcreditaccount;
		private boolean acc_advandref;
		private boolean acc_chargeinvoice;
		private boolean acc_addcharges;
		private boolean acc_chargedetails;
		
		
		//emr access variable
		//, , , , , , , , , 
		
		private boolean emr_docs;
		private boolean emr_history;
		private boolean emr_medicine;
		private boolean emr_investigation;
		private boolean emr_pacs;
		private boolean emr_media;
		private boolean emr_update;
		private boolean emr_print;
		private boolean emr_edit;
		private boolean emr_delete;
		
		//client access variable
		private boolean client_add;
		private boolean client_edit;
		private boolean client_delete;
		private boolean client_forms;
		private boolean client_discharge;
		private boolean client_emai;
		private boolean client_print;
		private boolean client_treatment;
		private boolean client_log;
		private boolean client_recordpayment;
		private boolean client_cashdesk;
		private boolean clientadvandref;
		private boolean client_addcharge;
		private boolean client_viewaccount;
		private boolean client_emr;
		private boolean indent;
		private boolean investigation_approve;
		private boolean daily_opd;
		private boolean indent_approve;
		private boolean cash_desk;
		
		private boolean tpa;
		private boolean nabh_quality;
		private boolean doctor_opd;
		private boolean refund;
		private boolean invst_collect;
		private boolean cancel_admsn;
		private boolean medicine_barcode;
		
	public boolean isMedicine_barcode() {
			return medicine_barcode;
		}
		public void setMedicine_barcode(boolean medicine_barcode) {
			this.medicine_barcode = medicine_barcode;
		}
	public boolean isCancel_admsn() {
			return cancel_admsn;
		}
		public void setCancel_admsn(boolean cancel_admsn) {
			this.cancel_admsn = cancel_admsn;
		}
		public boolean isAcc_refund() {
			return acc_refund;
		}
		public void setAcc_refund(boolean acc_refund) {
			this.acc_refund = acc_refund;
		}
	public boolean isInvst_collect() {
			return invst_collect;
		}
		public void setInvst_collect(boolean invst_collect) {
			this.invst_collect = invst_collect;
		}
	public boolean isRefund() {
			return refund;
		}
		public void setRefund(boolean refund) {
			this.refund = refund;
		}
	public boolean isDoctor_opd() {
			return doctor_opd;
		}
		public void setDoctor_opd(boolean doctor_opd) {
			this.doctor_opd = doctor_opd;
		}
	public boolean isTpa() {
			return tpa;
		}
		public void setTpa(boolean tpa) {
			this.tpa = tpa;
		}
		public boolean isNabh_quality() {
			return nabh_quality;
		}
		public void setNabh_quality(boolean nabh_quality) {
			this.nabh_quality = nabh_quality;
		}
	public boolean isIndent_approve() {
			return indent_approve;
		}
		public void setIndent_approve(boolean indent_approve) {
			this.indent_approve = indent_approve;
		}
		public boolean isCash_desk() {
			return cash_desk;
		}
		public void setCash_desk(boolean cash_desk) {
			this.cash_desk = cash_desk;
		}
	public boolean isDaily_opd() {
			return daily_opd;
		}
		public void setDaily_opd(boolean daily_opd) {
			this.daily_opd = daily_opd;
		}
	public boolean isInvestigation_approve() {
			return investigation_approve;
		}
		public void setInvestigation_approve(boolean investigation_approve) {
			this.investigation_approve = investigation_approve;
		}
	public boolean isIndent() {
			return indent;
		}
		public void setIndent(boolean indent) {
			this.indent = indent;
		}
	public boolean isAcc_createinvoice() {
			return acc_createinvoice;
		}
		public void setAcc_createinvoice(boolean acc_createinvoice) {
			this.acc_createinvoice = acc_createinvoice;
		}
		public boolean isAcc_recordpayment() {
			return acc_recordpayment;
		}
		public void setAcc_recordpayment(boolean acc_recordpayment) {
			this.acc_recordpayment = acc_recordpayment;
		}
		public boolean isAcc_viewcreditaccount() {
			return acc_viewcreditaccount;
		}
		public void setAcc_viewcreditaccount(boolean acc_viewcreditaccount) {
			this.acc_viewcreditaccount = acc_viewcreditaccount;
		}
		public boolean isAcc_advandref() {
			return acc_advandref;
		}
		public void setAcc_advandref(boolean acc_advandref) {
			this.acc_advandref = acc_advandref;
		}
		public boolean isAcc_chargeinvoice() {
			return acc_chargeinvoice;
		}
		public void setAcc_chargeinvoice(boolean acc_chargeinvoice) {
			this.acc_chargeinvoice = acc_chargeinvoice;
		}
		public boolean isAcc_addcharges() {
			return acc_addcharges;
		}
		public void setAcc_addcharges(boolean acc_addcharges) {
			this.acc_addcharges = acc_addcharges;
		}
		public boolean isAcc_chargedetails() {
			return acc_chargedetails;
		}
		public void setAcc_chargedetails(boolean acc_chargedetails) {
			this.acc_chargedetails = acc_chargedetails;
		}
	public boolean isOpd_addcharges() {
			return opd_addcharges;
		}
		public void setOpd_addcharges(boolean opd_addcharges) {
			this.opd_addcharges = opd_addcharges;
		}
		public boolean isOpd_createinvoice() {
			return opd_createinvoice;
		}
		public void setOpd_createinvoice(boolean opd_createinvoice) {
			this.opd_createinvoice = opd_createinvoice;
		}
		public boolean isOpd_recordpayment() {
			return opd_recordpayment;
		}
		public void setOpd_recordpayment(boolean opd_recordpayment) {
			this.opd_recordpayment = opd_recordpayment;
		}
	public boolean isPayroll() {
		return payroll;
	}
	public void setPayroll(boolean payroll) {
		this.payroll = payroll;
	}
	public boolean isPacks() {
		return packs;
	}
	public void setPacks(boolean packs) {
		this.packs = packs;
	}
	public boolean isInvestigation_chart() {
		return investigation_chart;
	}
	public void setInvestigation_chart(boolean investigation_chart) {
		this.investigation_chart = investigation_chart;
	}
	public boolean isSheduler() {
		return sheduler;
	}
	public void setSheduler(boolean sheduler) {
		this.sheduler = sheduler;
	}
	public boolean isHousekeeping() {
		return housekeeping;
	}
	public void setHousekeeping(boolean housekeeping) {
		this.housekeeping = housekeeping;
	}
	public boolean isDietery() {
		return dietery;
	}
	public void setDietery(boolean dietery) {
		this.dietery = dietery;
	}
	public boolean isCafeteria() {
		return cafeteria;
	}
	public void setCafeteria(boolean cafeteria) {
		this.cafeteria = cafeteria;
	}
	public boolean isPackages() {
		return packages;
	}
	public void setPackages(boolean packages) {
		this.packages = packages;
	}
	public boolean isAmbulance() {
		return ambulance;
	}
	public void setAmbulance(boolean ambulance) {
		this.ambulance = ambulance;
	}
	public boolean isBank_deposite() {
		return bank_deposite;
	}
	public void setBank_deposite(boolean bank_deposite) {
		this.bank_deposite = bank_deposite;
	}
	public boolean isAccount_reconcilation() {
		return account_reconcilation;
	}
	public void setAccount_reconcilation(boolean account_reconcilation) {
		this.account_reconcilation = account_reconcilation;
	}
	public boolean isBloodbak() {
		return bloodbak;
	}
	public void setBloodbak(boolean bloodbak) {
		this.bloodbak = bloodbak;
	}
	public boolean isInventory() {
		return inventory;
	}
	public void setInventory(boolean inventory) {
		this.inventory = inventory;
	}
	public boolean isDischarge() {
		return discharge;
	}
	public void setDischarge(boolean discharge) {
		this.discharge = discharge;
	}
	public boolean isManagemis() {
		return managemis;
	}
	public void setManagemis(boolean managemis) {
		this.managemis = managemis;
	}
	public boolean isExpences() {
		return expences;
	}
	public void setExpences(boolean expences) {
		this.expences = expences;
	}
	public boolean isManageemr() {
		return manageemr;
	}
	public void setManageemr(boolean manageemr) {
		this.manageemr = manageemr;
	}
	public boolean isApmtfinder() {
		return apmtfinder;
	}
	public void setApmtfinder(boolean apmtfinder) {
		this.apmtfinder = apmtfinder;
	}
	public boolean isManageopd() {
		return manageopd;
	}
	public void setManageopd(boolean manageopd) {
		this.manageopd = manageopd;
	}
	public boolean isDiarymanagement() {
		return diarymanagement;
	}
	public void setDiarymanagement(boolean diarymanagement) {
		this.diarymanagement = diarymanagement;
	}
	public boolean isAppointmentbooking() {
		return appointmentbooking;
	}
	public void setAppointmentbooking(boolean appointmentbooking) {
		this.appointmentbooking = appointmentbooking;
	}
	public boolean isBasicfinance() {
		return basicfinance;
	}
	public void setBasicfinance(boolean basicfinance) {
		this.basicfinance = basicfinance;
	}
	public boolean isFullfinance() {
		return fullfinance;
	}
	public void setFullfinance(boolean fullfinance) {
		this.fullfinance = fullfinance;
	}
	public boolean isMedicalrecord() {
		return medicalrecord;
	}
	public void setMedicalrecord(boolean medicalrecord) {
		this.medicalrecord = medicalrecord;
	}
	public boolean isCommunication() {
		return communication;
	}
	public void setCommunication(boolean communication) {
		this.communication = communication;
	}
	public boolean isReport() {
		return report;
	}
	public void setReport(boolean report) {
		this.report = report;
	}
	public boolean isAssessmentForm() {
		return assessmentForm;
	}
	public void setAssessmentForm(boolean assessmentForm) {
		this.assessmentForm = assessmentForm;
	}
	public boolean isManageclient() {
		return manageclient;
	}
	public void setManageclient(boolean manageclient) {
		this.manageclient = manageclient;
	}
	public boolean isManageclinic() {
		return manageclinic;
	}
	public void setManageclinic(boolean manageclinic) {
		this.manageclinic = manageclinic;
	}
	public boolean isManagemaster() {
		return managemaster;
	}
	public void setManagemaster(boolean managemaster) {
		this.managemaster = managemaster;
	}
	public boolean isManageprisc() {
		return manageprisc;
	}
	public void setManageprisc(boolean manageprisc) {
		this.manageprisc = manageprisc;
	}
	public boolean isManageinvst() {
		return manageinvst;
	}
	public void setManageinvst(boolean manageinvst) {
		this.manageinvst = manageinvst;
	}
	public boolean isManageipd() {
		return manageipd;
	}
	public void setManageipd(boolean manageipd) {
		this.manageipd = manageipd;
	}
	public boolean isOt() {
		return ot;
	}
	public void setOt(boolean ot) {
		this.ot = ot;
	}
	public boolean isCasualty() {
		return casualty;
	}
	public void setCasualty(boolean casualty) {
		this.casualty = casualty;
	}
	public boolean isPharmacy() {
		return pharmacy;
	}
	public void setPharmacy(boolean pharmacy) {
		this.pharmacy = pharmacy;
	}
	public boolean isMrd() {
		return mrd;
	}
	public void setMrd(boolean mrd) {
		this.mrd = mrd;
	}
	public boolean isMarketing() {
		return marketing;
	}
	public void setMarketing(boolean marketing) {
		this.marketing = marketing;
	}
	public boolean isVoice_recording() {
		return voice_recording;
	}
	public void setVoice_recording(boolean voice_recording) {
		this.voice_recording = voice_recording;
	}
	public boolean isOpd_modify() {
		return opd_modify;
	}
	public void setOpd_modify(boolean opd_modify) {
		this.opd_modify = opd_modify;
	}
	public boolean isOpd_cancel() {
		return opd_cancel;
	}
	public void setOpd_cancel(boolean opd_cancel) {
		this.opd_cancel = opd_cancel;
	}
	public boolean isOpd_prescription() {
		return opd_prescription;
	}
	public void setOpd_prescription(boolean opd_prescription) {
		this.opd_prescription = opd_prescription;
	}
	public boolean isOpd_investigation() {
		return opd_investigation;
	}
	public void setOpd_investigation(boolean opd_investigation) {
		this.opd_investigation = opd_investigation;
	}
	public boolean isOpd_ot() {
		return opd_ot;
	}
	public void setOpd_ot(boolean opd_ot) {
		this.opd_ot = opd_ot;
	}
	public boolean isOpd_viewaccount() {
		return opd_viewaccount;
	}
	public void setOpd_viewaccount(boolean opd_viewaccount) {
		this.opd_viewaccount = opd_viewaccount;
	}
	public boolean isOpd_apmtfinder() {
		return opd_apmtfinder;
	}
	public void setOpd_apmtfinder(boolean opd_apmtfinder) {
		this.opd_apmtfinder = opd_apmtfinder;
	}
	public boolean isOpd_advandref() {
		return opd_advandref;
	}
	public void setOpd_advandref(boolean opd_advandref) {
		this.opd_advandref = opd_advandref;
	}
	public boolean isOpd_modifydiagnosis() {
		return opd_modifydiagnosis;
	}
	public void setOpd_modifydiagnosis(boolean opd_modifydiagnosis) {
		this.opd_modifydiagnosis = opd_modifydiagnosis;
	}
	public boolean isOpd_editpatient() {
		return opd_editpatient;
	}
	public void setOpd_editpatient(boolean opd_editpatient) {
		this.opd_editpatient = opd_editpatient;
	}
	public boolean isOpd_log() {
		return opd_log;
	}
	public void setOpd_log(boolean opd_log) {
		this.opd_log = opd_log;
	}
	public boolean isOpd_emr() {
		return opd_emr;
	}
	public void setOpd_emr(boolean opd_emr) {
		this.opd_emr = opd_emr;
	}
	public boolean isOpd_assessmentform() {
		return opd_assessmentform;
	}
	public void setOpd_assessmentform(boolean opd_assessmentform) {
		this.opd_assessmentform = opd_assessmentform;
	}
	public boolean isOpd_treatment() {
		return opd_treatment;
	}
	public void setOpd_treatment(boolean opd_treatment) {
		this.opd_treatment = opd_treatment;
	}
	public boolean isIpd_admission() {
		return ipd_admission;
	}
	public void setIpd_admission(boolean ipd_admission) {
		this.ipd_admission = ipd_admission;
	}
	public boolean isIpd_declaration() {
		return ipd_declaration;
	}
	public void setIpd_declaration(boolean ipd_declaration) {
		this.ipd_declaration = ipd_declaration;
	}
	public boolean isIpd_log() {
		return ipd_log;
	}
	public void setIpd_log(boolean ipd_log) {
		this.ipd_log = ipd_log;
	}
	public boolean isIpd_forms() {
		return ipd_forms;
	}
	public void setIpd_forms(boolean ipd_forms) {
		this.ipd_forms = ipd_forms;
	}
	public boolean isIpd_discharge() {
		return ipd_discharge;
	}
	public void setIpd_discharge(boolean ipd_discharge) {
		this.ipd_discharge = ipd_discharge;
	}
	public boolean isIpd_emr() {
		return ipd_emr;
	}
	public void setIpd_emr(boolean ipd_emr) {
		this.ipd_emr = ipd_emr;
	}
	public boolean isIpd_prescription() {
		return ipd_prescription;
	}
	public void setIpd_prescription(boolean ipd_prescription) {
		this.ipd_prescription = ipd_prescription;
	}
	public boolean isIpd_investigation() {
		return ipd_investigation;
	}
	public void setIpd_investigation(boolean ipd_investigation) {
		this.ipd_investigation = ipd_investigation;
	}
	public boolean isIpd_reqconsultant() {
		return ipd_reqconsultant;
	}
	public void setIpd_reqconsultant(boolean ipd_reqconsultant) {
		this.ipd_reqconsultant = ipd_reqconsultant;
	}
	public boolean isIpd_nursingcare() {
		return ipd_nursingcare;
	}
	public void setIpd_nursingcare(boolean ipd_nursingcare) {
		this.ipd_nursingcare = ipd_nursingcare;
	}
	public boolean isIpd_reqblood() {
		return ipd_reqblood;
	}
	public void setIpd_reqblood(boolean ipd_reqblood) {
		this.ipd_reqblood = ipd_reqblood;
	}
	public boolean isIpd_autocare() {
		return ipd_autocare;
	}
	public void setIpd_autocare(boolean ipd_autocare) {
		this.ipd_autocare = ipd_autocare;
	}
	public boolean isIpd_treatmentlog() {
		return ipd_treatmentlog;
	}
	public void setIpd_treatmentlog(boolean ipd_treatmentlog) {
		this.ipd_treatmentlog = ipd_treatmentlog;
	}
	public boolean isIpd_addcharges() {
		return ipd_addcharges;
	}
	public void setIpd_addcharges(boolean ipd_addcharges) {
		this.ipd_addcharges = ipd_addcharges;
	}
	public boolean isIpd_createinvoice() {
		return ipd_createinvoice;
	}
	public void setIpd_createinvoice(boolean ipd_createinvoice) {
		this.ipd_createinvoice = ipd_createinvoice;
	}
	public boolean isIpd_recordpayment() {
		return ipd_recordpayment;
	}
	public void setIpd_recordpayment(boolean ipd_recordpayment) {
		this.ipd_recordpayment = ipd_recordpayment;
	}
	public boolean isIpd_advandref() {
		return ipd_advandref;
	}
	public void setIpd_advandref(boolean ipd_advandref) {
		this.ipd_advandref = ipd_advandref;
	}
	public boolean isIpd_viewaccount() {
		return ipd_viewaccount;
	}
	public void setIpd_viewaccount(boolean ipd_viewaccount) {
		this.ipd_viewaccount = ipd_viewaccount;
	}
	public boolean isIpd_packages() {
		return ipd_packages;
	}
	public void setIpd_packages(boolean ipd_packages) {
		this.ipd_packages = ipd_packages;
	}
	public boolean isEmr_docs() {
		return emr_docs;
	}
	public void setEmr_docs(boolean emr_docs) {
		this.emr_docs = emr_docs;
	}
	public boolean isEmr_history() {
		return emr_history;
	}
	public void setEmr_history(boolean emr_history) {
		this.emr_history = emr_history;
	}
	public boolean isEmr_medicine() {
		return emr_medicine;
	}
	public void setEmr_medicine(boolean emr_medicine) {
		this.emr_medicine = emr_medicine;
	}
	public boolean isEmr_investigation() {
		return emr_investigation;
	}
	public void setEmr_investigation(boolean emr_investigation) {
		this.emr_investigation = emr_investigation;
	}
	public boolean isEmr_pacs() {
		return emr_pacs;
	}
	public void setEmr_pacs(boolean emr_pacs) {
		this.emr_pacs = emr_pacs;
	}
	public boolean isEmr_media() {
		return emr_media;
	}
	public void setEmr_media(boolean emr_media) {
		this.emr_media = emr_media;
	}
	public boolean isEmr_update() {
		return emr_update;
	}
	public void setEmr_update(boolean emr_update) {
		this.emr_update = emr_update;
	}
	public boolean isEmr_print() {
		return emr_print;
	}
	public void setEmr_print(boolean emr_print) {
		this.emr_print = emr_print;
	}
	public boolean isEmr_edit() {
		return emr_edit;
	}
	public void setEmr_edit(boolean emr_edit) {
		this.emr_edit = emr_edit;
	}
	public boolean isEmr_delete() {
		return emr_delete;
	}
	public void setEmr_delete(boolean emr_delete) {
		this.emr_delete = emr_delete;
	}
	public boolean isClient_add() {
		return client_add;
	}
	public void setClient_add(boolean client_add) {
		this.client_add = client_add;
	}
	public boolean isClient_edit() {
		return client_edit;
	}
	public void setClient_edit(boolean client_edit) {
		this.client_edit = client_edit;
	}
	public boolean isClient_delete() {
		return client_delete;
	}
	public void setClient_delete(boolean client_delete) {
		this.client_delete = client_delete;
	}
	public boolean isClient_forms() {
		return client_forms;
	}
	public void setClient_forms(boolean client_forms) {
		this.client_forms = client_forms;
	}
	public boolean isClient_discharge() {
		return client_discharge;
	}
	public void setClient_discharge(boolean client_discharge) {
		this.client_discharge = client_discharge;
	}
	public boolean isClient_emai() {
		return client_emai;
	}
	public void setClient_emai(boolean client_emai) {
		this.client_emai = client_emai;
	}
	public boolean isClient_print() {
		return client_print;
	}
	public void setClient_print(boolean client_print) {
		this.client_print = client_print;
	}
	public boolean isClient_treatment() {
		return client_treatment;
	}
	public void setClient_treatment(boolean client_treatment) {
		this.client_treatment = client_treatment;
	}
	public boolean isClient_log() {
		return client_log;
	}
	public void setClient_log(boolean client_log) {
		this.client_log = client_log;
	}
	public boolean isClient_recordpayment() {
		return client_recordpayment;
	}
	public void setClient_recordpayment(boolean client_recordpayment) {
		this.client_recordpayment = client_recordpayment;
	}
	public boolean isClient_cashdesk() {
		return client_cashdesk;
	}
	public void setClient_cashdesk(boolean client_cashdesk) {
		this.client_cashdesk = client_cashdesk;
	}
	public boolean isClientadvandref() {
		return clientadvandref;
	}
	public void setClientadvandref(boolean clientadvandref) {
		this.clientadvandref = clientadvandref;
	}
	public boolean isClient_addcharge() {
		return client_addcharge;
	}
	public void setClient_addcharge(boolean client_addcharge) {
		this.client_addcharge = client_addcharge;
	}
	public boolean isClient_viewaccount() {
		return client_viewaccount;
	}
	public void setClient_viewaccount(boolean client_viewaccount) {
		this.client_viewaccount = client_viewaccount;
	}
	public boolean isClient_emr() {
		return client_emr;
	}
	public void setClient_emr(boolean client_emr) {
		this.client_emr = client_emr;
	}
	public boolean isGlobalaccess() {
		return globalaccess;
	}
	public void setGlobalaccess(boolean globalaccess) {
		this.globalaccess = globalaccess;
	}
	 
	 
	 
	private boolean  practioner_share, opd_practioner_share, charges, invoice, payment_report_detailed, payment_report_small, add_debtors, ca, auditors, userwise_payment, deptwise_analysis, charges_share, billing, discount, cancel_invoice, payment, kpi_dashboard, treatment_episode_list, patient_condition_list, dtr_report, patientlist, current_track_with_no_future_ampts, no_appointment_activity_record, dna_with_no_future_appointment, no_activity_record, dna_analysiis, appointment_kept_vs_dna, treatment_state_by_refferal, returning_patients, outcome_discharge, deathreport, current_patient_report, ipd_daily_report, ipd_monthly_report, bed_occupancy_report, reffered_by, mlc, report_outstandng, now_patients, total_patients_seen, dna_outstanding_action, sales_report, payment_recive, Inventory_opening, itemwise_stock, purchase_report, expiry_medicine_report, grn, indent_statement, ipd_daily_discharge, opd_ipd_cancel_refund, ipd_bill_register, service_register_details, cancel_invoice_report, KPI_report;

	public boolean isPractioner_share() {
		return practioner_share;
	}
	public void setPractioner_share(boolean practioner_share) {
		this.practioner_share = practioner_share;
	}
	public boolean isOpd_practioner_share() {
		return opd_practioner_share;
	}
	public void setOpd_practioner_share(boolean opd_practioner_share) {
		this.opd_practioner_share = opd_practioner_share;
	}
	public boolean isCharges() {
		return charges;
	}
	public void setCharges(boolean charges) {
		this.charges = charges;
	}
	public boolean isInvoice() {
		return invoice;
	}
	public void setInvoice(boolean invoice) {
		this.invoice = invoice;
	}
	public boolean isPayment_report_detailed() {
		return payment_report_detailed;
	}
	public void setPayment_report_detailed(boolean payment_report_detailed) {
		this.payment_report_detailed = payment_report_detailed;
	}
	public boolean isPayment_report_small() {
		return payment_report_small;
	}
	public void setPayment_report_small(boolean payment_report_small) {
		this.payment_report_small = payment_report_small;
	}
	public boolean isAdd_debtors() {
		return add_debtors;
	}
	public void setAdd_debtors(boolean add_debtors) {
		this.add_debtors = add_debtors;
	}
	public boolean isCa() {
		return ca;
	}
	public void setCa(boolean ca) {
		this.ca = ca;
	}
	public boolean isAuditors() {
		return auditors;
	}
	public void setAuditors(boolean auditors) {
		this.auditors = auditors;
	}
	public boolean isUserwise_payment() {
		return userwise_payment;
	}
	public void setUserwise_payment(boolean userwise_payment) {
		this.userwise_payment = userwise_payment;
	}
	public boolean isDeptwise_analysis() {
		return deptwise_analysis;
	}
	public void setDeptwise_analysis(boolean deptwise_analysis) {
		this.deptwise_analysis = deptwise_analysis;
	}
	public boolean isCharges_share() {
		return charges_share;
	}
	public void setCharges_share(boolean charges_share) {
		this.charges_share = charges_share;
	}
	public boolean isBilling() {
		return billing;
	}
	public void setBilling(boolean billing) {
		this.billing = billing;
	}
	public boolean isDiscount() {
		return discount;
	}
	public void setDiscount(boolean discount) {
		this.discount = discount;
	}
	public boolean isCancel_invoice() {
		return cancel_invoice;
	}
	public void setCancel_invoice(boolean cancel_invoice) {
		this.cancel_invoice = cancel_invoice;
	}
	public boolean isPayment() {
		return payment;
	}
	public void setPayment(boolean payment) {
		this.payment = payment;
	}
	public boolean isKpi_dashboard() {
		return kpi_dashboard;
	}
	public void setKpi_dashboard(boolean kpi_dashboard) {
		this.kpi_dashboard = kpi_dashboard;
	}
	public boolean isTreatment_episode_list() {
		return treatment_episode_list;
	}
	public void setTreatment_episode_list(boolean treatment_episode_list) {
		this.treatment_episode_list = treatment_episode_list;
	}
	public boolean isPatient_condition_list() {
		return patient_condition_list;
	}
	public void setPatient_condition_list(boolean patient_condition_list) {
		this.patient_condition_list = patient_condition_list;
	}
	public boolean isDtr_report() {
		return dtr_report;
	}
	public void setDtr_report(boolean dtr_report) {
		this.dtr_report = dtr_report;
	}
	public boolean isPatientlist() {
		return patientlist;
	}
	public void setPatientlist(boolean patientlist) {
		this.patientlist = patientlist;
	}
	public boolean isCurrent_track_with_no_future_ampts() {
		return current_track_with_no_future_ampts;
	}
	public void setCurrent_track_with_no_future_ampts(boolean current_track_with_no_future_ampts) {
		this.current_track_with_no_future_ampts = current_track_with_no_future_ampts;
	}
	public boolean isNo_appointment_activity_record() {
		return no_appointment_activity_record;
	}
	public void setNo_appointment_activity_record(boolean no_appointment_activity_record) {
		this.no_appointment_activity_record = no_appointment_activity_record;
	}
	public boolean isDna_with_no_future_appointment() {
		return dna_with_no_future_appointment;
	}
	public void setDna_with_no_future_appointment(boolean dna_with_no_future_appointment) {
		this.dna_with_no_future_appointment = dna_with_no_future_appointment;
	}
	public boolean isNo_activity_record() {
		return no_activity_record;
	}
	public void setNo_activity_record(boolean no_activity_record) {
		this.no_activity_record = no_activity_record;
	}
	public boolean isDna_analysiis() {
		return dna_analysiis;
	}
	public void setDna_analysiis(boolean dna_analysiis) {
		this.dna_analysiis = dna_analysiis;
	}
	public boolean isAppointment_kept_vs_dna() {
		return appointment_kept_vs_dna;
	}
	public void setAppointment_kept_vs_dna(boolean appointment_kept_vs_dna) {
		this.appointment_kept_vs_dna = appointment_kept_vs_dna;
	}
	public boolean isTreatment_state_by_refferal() {
		return treatment_state_by_refferal;
	}
	public void setTreatment_state_by_refferal(boolean treatment_state_by_refferal) {
		this.treatment_state_by_refferal = treatment_state_by_refferal;
	}
	public boolean isReturning_patients() {
		return returning_patients;
	}
	public void setReturning_patients(boolean returning_patients) {
		this.returning_patients = returning_patients;
	}
	public boolean isOutcome_discharge() {
		return outcome_discharge;
	}
	public void setOutcome_discharge(boolean outcome_discharge) {
		this.outcome_discharge = outcome_discharge;
	}
	public boolean isDeathreport() {
		return deathreport;
	}
	public void setDeathreport(boolean deathreport) {
		this.deathreport = deathreport;
	}
	public boolean isCurrent_patient_report() {
		return current_patient_report;
	}
	public void setCurrent_patient_report(boolean current_patient_report) {
		this.current_patient_report = current_patient_report;
	}
	public boolean isIpd_daily_report() {
		return ipd_daily_report;
	}
	public void setIpd_daily_report(boolean ipd_daily_report) {
		this.ipd_daily_report = ipd_daily_report;
	}
	public boolean isIpd_monthly_report() {
		return ipd_monthly_report;
	}
	public void setIpd_monthly_report(boolean ipd_monthly_report) {
		this.ipd_monthly_report = ipd_monthly_report;
	}
	public boolean isBed_occupancy_report() {
		return bed_occupancy_report;
	}
	public void setBed_occupancy_report(boolean bed_occupancy_report) {
		this.bed_occupancy_report = bed_occupancy_report;
	}
	public boolean isReffered_by() {
		return reffered_by;
	}
	public void setReffered_by(boolean reffered_by) {
		this.reffered_by = reffered_by;
	}
	public boolean isMlc() {
		return mlc;
	}
	public void setMlc(boolean mlc) {
		this.mlc = mlc;
	}
	public boolean isReport_outstandng() {
		return report_outstandng;
	}
	public void setReport_outstandng(boolean report_outstandng) {
		this.report_outstandng = report_outstandng;
	}
	public boolean isNow_patients() {
		return now_patients;
	}
	public void setNow_patients(boolean now_patients) {
		this.now_patients = now_patients;
	}
	public boolean isTotal_patients_seen() {
		return total_patients_seen;
	}
	public void setTotal_patients_seen(boolean total_patients_seen) {
		this.total_patients_seen = total_patients_seen;
	}
	public boolean isDna_outstanding_action() {
		return dna_outstanding_action;
	}
	public void setDna_outstanding_action(boolean dna_outstanding_action) {
		this.dna_outstanding_action = dna_outstanding_action;
	}
	public boolean isSales_report() {
		return sales_report;
	}
	public void setSales_report(boolean sales_report) {
		this.sales_report = sales_report;
	}
	public boolean isPayment_recive() {
		return payment_recive;
	}
	public void setPayment_recive(boolean payment_recive) {
		this.payment_recive = payment_recive;
	}
	public boolean isInventory_opening() {
		return Inventory_opening;
	}
	public void setInventory_opening(boolean inventory_opening) {
		Inventory_opening = inventory_opening;
	}
	public boolean isItemwise_stock() {
		return itemwise_stock;
	}
	public void setItemwise_stock(boolean itemwise_stock) {
		this.itemwise_stock = itemwise_stock;
	}
	public boolean isPurchase_report() {
		return purchase_report;
	}
	public void setPurchase_report(boolean purchase_report) {
		this.purchase_report = purchase_report;
	}
	public boolean isExpiry_medicine_report() {
		return expiry_medicine_report;
	}
	public void setExpiry_medicine_report(boolean expiry_medicine_report) {
		this.expiry_medicine_report = expiry_medicine_report;
	}
	public boolean isGrn() {
		return grn;
	}
	public void setGrn(boolean grn) {
		this.grn = grn;
	}
	public boolean isIndent_statement() {
		return indent_statement;
	}
	public void setIndent_statement(boolean indent_statement) {
		this.indent_statement = indent_statement;
	}
	public boolean isIpd_daily_discharge() {
		return ipd_daily_discharge;
	}
	public void setIpd_daily_discharge(boolean ipd_daily_discharge) {
		this.ipd_daily_discharge = ipd_daily_discharge;
	}
	public boolean isOpd_ipd_cancel_refund() {
		return opd_ipd_cancel_refund;
	}
	public void setOpd_ipd_cancel_refund(boolean opd_ipd_cancel_refund) {
		this.opd_ipd_cancel_refund = opd_ipd_cancel_refund;
	}
	public boolean isIpd_bill_register() {
		return ipd_bill_register;
	}
	public void setIpd_bill_register(boolean ipd_bill_register) {
		this.ipd_bill_register = ipd_bill_register;
	}
	public boolean isService_register_details() {
		return service_register_details;
	}
	public void setService_register_details(boolean service_register_details) {
		this.service_register_details = service_register_details;
	}
	public boolean isCancel_invoice_report() {
		return cancel_invoice_report;
	}
	public void setCancel_invoice_report(boolean cancel_invoice_report) {
		this.cancel_invoice_report = cancel_invoice_report;
	}
	public boolean isKPI_report() {
		return KPI_report;
	}
	public void setKPI_report(boolean kPI_report) {
		KPI_report = kPI_report;
	}
	public boolean isShow_master() {
		return show_master;
	}
	public void setShow_master(boolean show_master) {
		this.show_master = show_master;
	}


public boolean isToken_display() {
		return token_display;
	}
	public void setToken_display(boolean token_display) {
		this.token_display = token_display;
	}



public boolean isAdd_medicine() {
		return add_medicine;
	}
	public void setAdd_medicine(boolean add_medicine) {
		this.add_medicine = add_medicine;
	}



public boolean isPharm_print_backdate() {
		return pharm_print_backdate;
	}
	public void setPharm_print_backdate(boolean pharm_print_backdate) {
		this.pharm_print_backdate = pharm_print_backdate;
	}

	


public boolean isEditcharges() {
		return editcharges;
	}
	public void setEditcharges(boolean editcharges) {
		this.editcharges = editcharges;
	}



public String getInvestigation_newaccess() {
		return investigation_newaccess;
	}
	public void setInvestigation_newaccess(String investigation_newaccess) {
		this.investigation_newaccess = investigation_newaccess;
	}



public boolean isCathlab() {
		return cathlab;
	}
	public void setCathlab(boolean cathlab) {
		this.cathlab = cathlab;
	}



private boolean acc_refund;
	private boolean show_master;
	private boolean add_medicine;
private boolean pharm_print_backdate;
private boolean editcharges;
private String investigation_newaccess="";
private boolean cathlab;
private boolean myhr;
private boolean daycare;
private boolean emergency_lbl;

public boolean isMyhr() {
	return myhr;
}
public void setMyhr(boolean myhr) {
	this.myhr = myhr;
}
public boolean isDaycare() {
	return daycare;
}
public void setDaycare(boolean daycare) {
	this.daycare = daycare;
}
public boolean isEmergency_lbl() {
	return emergency_lbl;
}
public void setEmergency_lbl(boolean emergency_lbl) {
	this.emergency_lbl = emergency_lbl;
}
}
