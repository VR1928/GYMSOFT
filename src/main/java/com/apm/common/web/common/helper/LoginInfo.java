package com.apm.common.web.common.helper;






import java.util.ArrayList;



public class LoginInfo {

	/** Id of user that corresponds to primary key of database table row */
	private int id;
	
	/** name of user */
	private String userName = null;

	/** id of user */
	private String userId = null;
	private boolean inhousepatient;
	private String branchName;
	private String acaccess;
	private String loginsessionid;
	private boolean invoice_groupby;
	private boolean invoice_charge_seqno;
	private boolean opd_tp_zero_invoice;
	private int grn_to_prisc_location;
	private boolean opd_video_icon_show;
	private boolean hidecalinpoprint;
	private boolean change_indent_product;
	private boolean max_phar_discount;
	private boolean disc_approve_sms;
	private boolean ot_surgeon_sms;
	private boolean ot_patient_sms;
	public boolean isOt_surgeon_sms() {
		return ot_surgeon_sms;
	}

	public void setOt_surgeon_sms(boolean ot_surgeon_sms) {
		this.ot_surgeon_sms = ot_surgeon_sms;
	}

	public boolean isOt_patient_sms() {
		return ot_patient_sms;
	}

	public void setOt_patient_sms(boolean ot_patient_sms) {
		this.ot_patient_sms = ot_patient_sms;
	}

	public boolean isMax_phar_discount() {
		return max_phar_discount;
	}

	public void setMax_phar_discount(boolean max_phar_discount) {
		this.max_phar_discount = max_phar_discount;
	}

	public boolean isChange_indent_product() {
		return change_indent_product;
	}

	public void setChange_indent_product(boolean change_indent_product) {
		this.change_indent_product = change_indent_product;
	}

	public boolean isHidecalinpoprint() {
		return hidecalinpoprint;
	}

	public void setHidecalinpoprint(boolean hidecalinpoprint) {
		this.hidecalinpoprint = hidecalinpoprint;
	}

	public boolean isOpd_video_icon_show() {
		return opd_video_icon_show;
	}

	public void setOpd_video_icon_show(boolean opd_video_icon_show) {
		this.opd_video_icon_show = opd_video_icon_show;
	}

	public int getGrn_to_prisc_location() {
		return grn_to_prisc_location;
	}

	public void setGrn_to_prisc_location(int grn_to_prisc_location) {
		this.grn_to_prisc_location = grn_to_prisc_location;
	}

	public boolean isInvoice_groupby() {
		return invoice_groupby;
	}

	public void setInvoice_groupby(boolean invoice_groupby) {
		this.invoice_groupby = invoice_groupby;
	}

	public boolean isInvoice_charge_seqno() {
		return invoice_charge_seqno;
	}

	public void setInvoice_charge_seqno(boolean invoice_charge_seqno) {
		this.invoice_charge_seqno = invoice_charge_seqno;
	}


	private int outoprisc;
	
	private int iskunal;
	private int gymsms;
	private boolean invoice_date_modify;
	private boolean package_access;
	
	private int wardforcharge;
	private boolean prisc_location_list,prisc_print;
	private boolean direct_prisc;
	private boolean prachi_clinic;
	private boolean indv_discount;
	private boolean prisc_new_req_access;
	
	private boolean invoicemodify;
	private boolean medicine_barcode;
	private boolean additional_disc;
	private boolean opening_closeing_on;
	private String opening_locations;
	private boolean cancel_invoice_new;
	private boolean payrollaccess;
	private boolean proc_after_stock;
	private boolean phar_print_seq;
	private String sms_senderid;
	private int superadminid;
	
	public int getGymsms() {
		return gymsms;
	}

	public void setGymsms(int gymsms) {
		this.gymsms = gymsms;
	}

	public int getSuperadminid() {
		return superadminid;
	}

	public void setSuperadminid(int superadminid) {
		this.superadminid = superadminid;
	}

	public String getSms_senderid() {
		return sms_senderid;
	}

	public void setSms_senderid(String sms_senderid) {
		this.sms_senderid = sms_senderid;
	}

	public boolean isPhar_print_seq() {
		return phar_print_seq;
	}

	public void setPhar_print_seq(boolean phar_print_seq) {
		this.phar_print_seq = phar_print_seq;
	}

	public boolean isProc_after_stock() {
		return proc_after_stock;
	}

	public void setProc_after_stock(boolean proc_after_stock) {
		this.proc_after_stock = proc_after_stock;
	}

	public boolean isCancel_invoice_new() {
		return cancel_invoice_new;
	}

	public void setCancel_invoice_new(boolean cancel_invoice_new) {
		this.cancel_invoice_new = cancel_invoice_new;
	}

	public boolean isOpening_closeing_on() {
		return opening_closeing_on;
	}

	public void setOpening_closeing_on(boolean opening_closeing_on) {
		this.opening_closeing_on = opening_closeing_on;
	}

	public String getOpening_locations() {
		return opening_locations;
	}

	public void setOpening_locations(String opening_locations) {
		this.opening_locations = opening_locations;
	}

	public boolean isAdditional_disc() {
		return additional_disc;
	}

	public void setAdditional_disc(boolean additional_disc) {
		this.additional_disc = additional_disc;
	}

	public boolean isMedicine_barcode() {
		return medicine_barcode;
	}

	public void setMedicine_barcode(boolean medicine_barcode) {
		this.medicine_barcode = medicine_barcode;
	}

	public boolean isPrisc_new_req_access() {
		return prisc_new_req_access;
	}

	public void setPrisc_new_req_access(boolean prisc_new_req_access) {
		this.prisc_new_req_access = prisc_new_req_access;
	}

	public boolean isPrachi_clinic() {
		return prachi_clinic;
	}

	public void setPrachi_clinic(boolean prachi_clinic) {
		this.prachi_clinic = prachi_clinic;
	}

	public boolean isDirect_prisc() {
		return direct_prisc;
	}

	public void setDirect_prisc(boolean direct_prisc) {
		this.direct_prisc = direct_prisc;
	}

	public boolean isPrisc_location_list() {
		return prisc_location_list;
	}

	public void setPrisc_location_list(boolean prisc_location_list) {
		this.prisc_location_list = prisc_location_list;
	}

	public boolean isPrisc_print() {
		return prisc_print;
	}

	public void setPrisc_print(boolean prisc_print) {
		this.prisc_print = prisc_print;
	}

	public int getIskunal() {
		return iskunal;
	}

	public void setIskunal(int iskunal) {
		this.iskunal = iskunal;
	}

	public int getWardforcharge() {
		return wardforcharge;
	}

	public void setWardforcharge(int wardforcharge) {
		this.wardforcharge = wardforcharge;
	}

	public int getOutoprisc() {
		return outoprisc;
	}

	public void setOutoprisc(int outoprisc) {
		this.outoprisc = outoprisc;
	}

	public String getLoginsessionid() {
		return loginsessionid;
	}

	public void setLoginsessionid(String loginsessionid) {
		this.loginsessionid = loginsessionid;
	}


	private int userType;
	private boolean edit_purchaseorder;
	private boolean delete_purchaseorder;
	
	private String firstName;
	
	private String lastName;
	
	private String clinicOwner;
	private int pharmacyUserType;
	private String dbName;
	
	private String clinicName = "";
	
	private String timeZone = "";
	
	private int clinicid;
	
	private String clinicUserid;
	
	private String commencing;
	
	private String title;
	
	private String diaryUserid;
	
	private String email;
	
	private String mob;
	
	private String dob;
	
	private String gender;
	
	private String clientOtp;
	
	private int isotpconfirmd = 0;
	
	private String country;
	
	private String clinicAddress;
	
	private String regAddress = "";
	
	private String regLocation = "";
	
	private String regContactNo = "";
	
	private String regPinCode = "";
	
	private String loginType = "";
	
	private int clinicStartTime;
	
	private String jobTitle;
	
	private boolean manageopd;
	
	private boolean apmtfinder;
	 
	 private boolean manageemr;
	 
	 private boolean expences;
	 
	 private boolean managemis;
	 
	 private String loc;
	 
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
	 private int dbsize;
	 private String islogo;
	 
	//new terms added for personal widget
		private boolean ot;
		private boolean casualty;
		private boolean pharmacy;
		private boolean mrd;
		private boolean marketing;
		private boolean voice_recording;
	
		//pharmacy setting user setting term
		private boolean islogin;
		private boolean sale_bill;
		private boolean disscount;
		private boolean ledger;
		private boolean account;
		private boolean purchase_order;
		private boolean SMS_authority;
		private boolean edit_bill;
		private boolean return_medicine;
	    public boolean isReturn_medicine() {
			return return_medicine;
		}

		public void setReturn_medicine(boolean return_medicine) {
			this.return_medicine = return_medicine;
		}


		private boolean delete_bill;
	    
	    private boolean opd_addcharges;
		private boolean opd_createinvoice;
		private boolean opd_recordpayment;
		private boolean procurementType;
		
		
	    
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
		
		
		private boolean acc_createinvoice;
		private boolean acc_recordpayment;
		private boolean acc_viewcreditaccount;
		private boolean acc_advandref;
		private boolean acc_chargeinvoice;
		private boolean acc_addcharges;
		private boolean acc_chargedetails;
		
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
		
		private boolean showdiscount;
		private boolean refund_dashboard;
		private boolean showinvestigation;
		
		private String uhid;
		private boolean edit_invst_charge;
		private boolean medtreatgiven;
		
		
		private String pacsip;
		private boolean invst_collect;
		private boolean approve_po;
		private boolean cancel_admsn;
		private boolean stock_log;
		
		private double creditlimit=0;
		private boolean creditlimitaccess;
		private boolean edit_paypo;
		private boolean isledgerhosp;
		private boolean adjustmentaccess;
		private boolean direct_refund_disc;
		private boolean supplier_add;
		private boolean prisc_deliver_return;
		private boolean auto_generic_name;
		private boolean ref_dis_pay;
		
	public boolean isRef_dis_pay() {
			return ref_dis_pay;
		}

		public void setRef_dis_pay(boolean ref_dis_pay) {
			this.ref_dis_pay = ref_dis_pay;
		}

	public boolean isAuto_generic_name() {
			return auto_generic_name;
		}

		public void setAuto_generic_name(boolean auto_generic_name) {
			this.auto_generic_name = auto_generic_name;
		}

	public boolean isPrisc_deliver_return() {
			return prisc_deliver_return;
		}

		public void setPrisc_deliver_return(boolean prisc_deliver_return) {
			this.prisc_deliver_return = prisc_deliver_return;
		}

	public boolean isSupplier_add() {
			return supplier_add;
		}

		public void setSupplier_add(boolean supplier_add) {
			this.supplier_add = supplier_add;
		}

	public boolean isDirect_refund_disc() {
			return direct_refund_disc;
		}

		public void setDirect_refund_disc(boolean direct_refund_disc) {
			this.direct_refund_disc = direct_refund_disc;
		}

	public boolean isAdjustmentaccess() {
			return adjustmentaccess;
		}

		public void setAdjustmentaccess(boolean adjustmentaccess) {
			this.adjustmentaccess = adjustmentaccess;
		}

	public boolean isIsledgerhosp() {
			return isledgerhosp;
		}

		public void setIsledgerhosp(boolean isledgerhosp) {
			this.isledgerhosp = isledgerhosp;
		}

	public boolean isEdit_paypo() {
			return edit_paypo;
		}

		public void setEdit_paypo(boolean edit_paypo) {
			this.edit_paypo = edit_paypo;
		}

	public double getCreditlimit() {
			return creditlimit;
		}

		public void setCreditlimit(double creditlimit) {
			this.creditlimit = creditlimit;
		}

		public boolean isCreditlimitaccess() {
			return creditlimitaccess;
		}

		public void setCreditlimitaccess(boolean creditlimitaccess) {
			this.creditlimitaccess = creditlimitaccess;
		}

	public boolean isStock_log() {
			return stock_log;
		}

		public void setStock_log(boolean stock_log) {
			this.stock_log = stock_log;
		}

	public boolean isCancel_admsn() {
			return cancel_admsn;
		}

		public void setCancel_admsn(boolean cancel_admsn) {
			this.cancel_admsn = cancel_admsn;
		}

	public boolean isApprove_po() {
			return approve_po;
		}

		public void setApprove_po(boolean approve_po) {
			this.approve_po = approve_po;
		}

	public boolean isInvst_collect() {
			return invst_collect;
		}

		public void setInvst_collect(boolean invst_collect) {
			this.invst_collect = invst_collect;
		}

	public String getPacsip() {
			return pacsip;
		}

		public void setPacsip(String pacsip) {
			this.pacsip = pacsip;
		}

	public boolean isMedtreatgiven() {
			return medtreatgiven;
		}

		public void setMedtreatgiven(boolean medtreatgiven) {
			this.medtreatgiven = medtreatgiven;
		}

	public boolean isEdit_invst_charge() {
			return edit_invst_charge;
		}

		public void setEdit_invst_charge(boolean edit_invst_charge) {
			this.edit_invst_charge = edit_invst_charge;
		}

	public String getUhid() {
			return uhid;
		}

		public void setUhid(String uhid) {
			this.uhid = uhid;
		}

	public boolean isShowinvestigation() {
			return showinvestigation;
		}

		public void setShowinvestigation(boolean showinvestigation) {
			this.showinvestigation = showinvestigation;
		}

	public boolean isRefund_dashboard() {
			return refund_dashboard;
		}

		public void setRefund_dashboard(boolean refund_dashboard) {
			this.refund_dashboard = refund_dashboard;
		}

	public boolean isShowdiscount() {
			return showdiscount;
		}

		public void setShowdiscount(boolean showdiscount) {
			this.showdiscount = showdiscount;
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

	public boolean isDelete_bill() {
			return delete_bill;
		}

		public void setDelete_bill(boolean delete_bill) {
			this.delete_bill = delete_bill;
		}

	public String getIslogo() {
		return islogo;
	}

	public void setIslogo(String islogo) {
		this.islogo = islogo;
	}

	public int getDbsize() {
		return dbsize;
	}

	public void setDbsize(int dbsize) {
		this.dbsize = dbsize;
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

	public boolean isPayroll() {
		return payroll;
	}

	public void setPayroll(boolean payroll) {
		this.payroll = payroll;
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

	public boolean isIOS() {
		return iOS;
	}

	public void setIOS(boolean ios) {
		iOS = ios;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getClinicLogo() {
		return clinicLogo;
	}

	public void setClinicLogo(String clinicLogo) {
		this.clinicLogo = clinicLogo;
	}


	private int clinicEndTime;
	
	private String clinicLogo;
	
	
	
	

	public int getClinicStartTime() {
		return clinicStartTime;
	}

	public void setClinicStartTime(int clinicStartTime) {
		this.clinicStartTime = clinicStartTime;
	}

	public int getClinicEndTime() {
		return clinicEndTime;
	}

	public void setClinicEndTime(int clinicEndTime) {
		this.clinicEndTime = clinicEndTime;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getRegAddress() {
		return regAddress;
	}

	public void setRegAddress(String regAddress) {
		this.regAddress = regAddress;
	}

	public String getRegLocation() {
		return regLocation;
	}

	public void setRegLocation(String regLocation) {
		this.regLocation = regLocation;
	}

	public String getRegContactNo() {
		return regContactNo;
	}

	public void setRegContactNo(String regContactNo) {
		this.regContactNo = regContactNo;
	}

	public String getRegPinCode() {
		return regPinCode;
	}

	public void setRegPinCode(String regPinCode) {
		this.regPinCode = regPinCode;
	}

	public String getClinicAddress() {
		return clinicAddress;
	}

	public void setClinicAddress(String clinicAddress) {
		this.clinicAddress = clinicAddress;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getIsotpconfirmd() {
		return isotpconfirmd;
	}

	public void setIsotpconfirmd(int isotpconfirmd) {
		this.isotpconfirmd = isotpconfirmd;
	}

	public String getClientOtp() {
		return clientOtp;
	}

	public void setClientOtp(String clientOtp) {
		this.clientOtp = clientOtp;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCommencing() {
		return commencing;
	}

	public void setCommencing(String commencing) {
		this.commencing = commencing;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDiaryUserid() {
		return diaryUserid;
	}

	public void setDiaryUserid(String diaryUserid) {
		this.diaryUserid = diaryUserid;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMob() {
		return mob;
	}

	public void setMob(String mob) {
		this.mob = mob;
	}

	public String getClinicUserid() {
		return clinicUserid;
	}

	public void setClinicUserid(String clinicUserid) {
		this.clinicUserid = clinicUserid;
	}

	public int getClinicid() {
		return clinicid;
	}

	public void setClinicid(int clinicid) {
		this.clinicid = clinicid;
	}

	public String getTimeZone() {
		return timeZone;
	}

	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	public String getClinicName() {
		return clinicName;
	}

	public void setClinicName(String clinicName) {
		this.clinicName = clinicName;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getClinicOwner() {
		return clinicOwner;
	}

	public void setClinicOwner(String clinicOwner) {
		this.clinicOwner = clinicOwner;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	
    /**
     * @return the userId
     */
    public String getUserName() {
    	return userName;
    }
	
    /**
     * @param userId the userId to set
     */
    public void setUserName(String userName) {
    	this.userName = userName;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
    	return userId;
    }
	
    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
    	this.userId = userId;
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	private String emailConfigureId;
	
	private String emailUserName;
	
	private String emailPassword;
	
	private String emailConfirmPassword;
	
	private String emailHostName;
	
	public String getEmailConfigureId() {
		return emailConfigureId;
	}

	public void setEmailConfigureId(String emailConfigureId) {
		this.emailConfigureId = emailConfigureId;
	}

	public String getEmailUserName() {
		return emailUserName;
	}

	public void setEmailUserName(String emailUserName) {
		this.emailUserName = emailUserName;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	public String getEmailConfirmPassword() {
		return emailConfirmPassword;
	}

	public void setEmailConfirmPassword(String emailConfirmPassword) {
		this.emailConfirmPassword = emailConfirmPassword;
	}

	public String getEmailHostName() {
		return emailHostName;
	}

	public void setEmailHostName(String emailHostName) {
		this.emailHostName = emailHostName;
	}
	
	//Menu Settings
		private boolean DiaryManagement;
		
		public boolean isDiaryManagement() {
			return DiaryManagement;
		}



		public void setDiaryManagement(boolean diaryManagement) {
			DiaryManagement = diaryManagement;
		}



		public boolean isAppointmentBooking() {
			return AppointmentBooking;
		}



		public void setAppointmentBooking(boolean appointmentBooking) {
			AppointmentBooking = appointmentBooking;
		}



		public boolean isBasicFinance() {
			return BasicFinance;
		}



		public void setBasicFinance(boolean basicFinance) {
			BasicFinance = basicFinance;
		}



		public boolean isFullFinance() {
			return fullFinance;
		}



		public void setFullFinance(boolean fullFinance) {
			this.fullFinance = fullFinance;
		}



		public boolean isMedicalRecord() {
			return medicalRecord;
		}



		public void setMedicalRecord(boolean medicalRecord) {
			this.medicalRecord = medicalRecord;
		}



		public boolean isClinicResourceMngment() {
			return clinicResourceMngment;
		}



		public void setClinicResourceMngment(boolean clinicResourceMngment) {
			this.clinicResourceMngment = clinicResourceMngment;
		}



		public boolean isClinicPayrollMngment() {
			return clinicPayrollMngment;
		}



		public void setClinicPayrollMngment(boolean clinicPayrollMngment) {
			this.clinicPayrollMngment = clinicPayrollMngment;
		}

		private boolean AppointmentBooking;
		
		private boolean BasicFinance;
		
		private boolean fullFinance;
		
		private boolean medicalRecord;
		
		private boolean clinicResourceMngment;
		
		private boolean clinicPayrollMngment;
		
		private boolean communication;
		
		private boolean report;
		
		private boolean assessmentForms;
		
		private boolean desktop;
			
		private boolean iOS;
		
		private boolean mobile;
		
		private boolean tablet;
		
		private boolean manageclient;
		private boolean manageclinic;
		private boolean managemaster;
		private boolean manageprisc;
		private boolean manageinvst;
		private boolean manageipd;
		
		
		
		


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



		public boolean isAssessmentForms() {
			return assessmentForms;
		}



		public void setAssessmentForms(boolean assessmentForms) {
			this.assessmentForms = assessmentForms;
		}



		public boolean isDesktop() {
			return desktop;
		}



		public void setDesktop(boolean desktop) {
			this.desktop = desktop;
		}



		public boolean isiOS() {
			return iOS;
		}



		public void setiOS(boolean iOS) {
			this.iOS = iOS;
		}



		public boolean isMobile() {
			return mobile;
		}



		public void setMobile(boolean mobile) {
			this.mobile = mobile;
		}



		public boolean isTablet() {
			return tablet;
		}



		public void setTablet(boolean tablet) {
			this.tablet = tablet;
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

		public boolean isIslogin() {
			return islogin;
		}

		public void setIslogin(boolean islogin) {
			this.islogin = islogin;
		}

		public boolean isSale_bill() {
			return sale_bill;
		}

		public void setSale_bill(boolean sale_bill) {
			this.sale_bill = sale_bill;
		}

		public boolean isDisscount() {
			return disscount;
		}

		public void setDisscount(boolean disscount) {
			this.disscount = disscount;
		}

		public boolean isLedger() {
			return ledger;
		}

		public void setLedger(boolean ledger) {
			this.ledger = ledger;
		}

		public boolean isAccount() {
			return account;
		}

		public void setAccount(boolean account) {
			this.account = account;
		}

		public boolean isPurchase_order() {
			return purchase_order;
		}

		public void setPurchase_order(boolean purchase_order) {
			this.purchase_order = purchase_order;
		}

		public String getLoc() {
			return loc;
		}

		public void setLoc(String loc) {
			this.loc = loc;
		}

		public boolean isSMS_authority() {
			return SMS_authority;
		}

		public void setSMS_authority(boolean sms_authority) {
			SMS_authority = sms_authority;
		}

		public boolean isEdit_bill() {
			return edit_bill;
		}

		public void setEdit_bill(boolean edit_bill) {
			this.edit_bill = edit_bill;
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

		public boolean isEdit_purchaseorder() {
			return edit_purchaseorder;
		}

		public void setEdit_purchaseorder(boolean edit_purchaseorder) {
			this.edit_purchaseorder = edit_purchaseorder;
		}

		public boolean isDelete_purchaseorder() {
			return delete_purchaseorder;
		}

		public void setDelete_purchaseorder(boolean delete_purchaseorder) {
			this.delete_purchaseorder = delete_purchaseorder;
		}

		public boolean isInhousepatient() {
			return inhousepatient;
		}

		public void setInhousepatient(boolean inhousepatient) {
			this.inhousepatient = inhousepatient;
		}

		public boolean isProcurementType() {
			return procurementType;
		}

		public void setProcurementType(boolean procurementType) {
			this.procurementType = procurementType;
		}

		public int getPharmacyUserType() {
			return pharmacyUserType;
		}

		public void setPharmacyUserType(int pharmacyUserType) {
			this.pharmacyUserType = pharmacyUserType;
		}

		public String getAcaccess() {
			return acaccess;
		}

		public void setAcaccess(String acaccess) {
			this.acaccess = acaccess;
		}

		public boolean isCriticalvaluesms() {
			return criticalvaluesms;
		}

		public void setCriticalvaluesms(boolean criticalvaluesms) {
			this.criticalvaluesms = criticalvaluesms;
		}


		private boolean criticalvaluesms;
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

public boolean isAcc_refund() {
			return acc_refund;
		}

		public void setAcc_refund(boolean acc_refund) {
			this.acc_refund = acc_refund;
		}


public boolean isToken_display() {
			return token_display;
		}

		public void setToken_display(boolean token_display) {
			this.token_display = token_display;
		}

public boolean isPurchase_edit_pharmacy() {
			return purchase_edit_pharmacy;
		}

		public void setPurchase_edit_pharmacy(boolean purchase_edit_pharmacy) {
			this.purchase_edit_pharmacy = purchase_edit_pharmacy;
		}


public String getCancel_po() {
			return cancel_po;
		}

		public void setCancel_po(String cancel_po) {
			this.cancel_po = cancel_po;
		}


public boolean isAdd_medicine() {
			return add_medicine;
		}

		public void setAdd_medicine(boolean add_medicine) {
			this.add_medicine = add_medicine;
		}


public boolean isIsdotmatrix() {
			return isdotmatrix;
		}

		public void setIsdotmatrix(boolean isdotmatrix) {
			this.isdotmatrix = isdotmatrix;
		}


public boolean isPharm_print_backdate() {
			return pharm_print_backdate;
		}

		public void setPharm_print_backdate(boolean pharm_print_backdate) {
			this.pharm_print_backdate = pharm_print_backdate;
		}


public boolean isBdaysms() {
			return bdaysms;
		}

		public void setBdaysms(boolean bdaysms) {
			this.bdaysms = bdaysms;
		}


public boolean isImmusms() {
			return immusms;
		}

		public void setImmusms(boolean immusms) {
			this.immusms = immusms;
		}


public boolean isF_diagnosis_discharge() {
			return f_diagnosis_discharge;
		}

		public void setF_diagnosis_discharge(boolean f_diagnosis_discharge) {
			this.f_diagnosis_discharge = f_diagnosis_discharge;
		}


public boolean isSeq_no_gen() {
			return seq_no_gen;
		}

		public void setSeq_no_gen(boolean seq_no_gen) {
			this.seq_no_gen = seq_no_gen;
		}


public boolean isMisaccess() {
			return misaccess;
		}

		public void setMisaccess(boolean misaccess) {
			this.misaccess = misaccess;
		}


public boolean isRemoveprocurement() {
			return removeprocurement;
		}

		public void setRemoveprocurement(boolean removeprocurement) {
			this.removeprocurement = removeprocurement;
		}


public boolean isModify_disc() {
			return modify_disc;
		}

		public void setModify_disc(boolean modify_disc) {
			this.modify_disc = modify_disc;
		}

		
		

public boolean isSmsVisitingConslt() {
			return smsVisitingConslt;
		}

		public void setSmsVisitingConslt(boolean smsVisitingConslt) {
			this.smsVisitingConslt = smsVisitingConslt;
		}


public boolean isShow_hospital_admin() {
			return show_hospital_admin;
		}

		public void setShow_hospital_admin(boolean show_hospital_admin) {
			this.show_hospital_admin = show_hospital_admin;
		}


public boolean isDirect_ipd() {
			return direct_ipd;
		}

		public void setDirect_ipd(boolean direct_ipd) {
			this.direct_ipd = direct_ipd;
		}


public boolean isDrwise_ipd() {
			return drwise_ipd;
		}

		public void setDrwise_ipd(boolean drwise_ipd) {
			this.drwise_ipd = drwise_ipd;
		}


public boolean isJobtitlewise_investigation() {
			return jobtitlewise_investigation;
		}

		public void setJobtitlewise_investigation(boolean jobtitlewise_investigation) {
			this.jobtitlewise_investigation = jobtitlewise_investigation;
		}


public boolean isEditchargesacs() {
			return editchargesacs;
		}

		public void setEditchargesacs(boolean editchargesacs) {
			this.editchargesacs = editchargesacs;
		}


public boolean isShow_wardname() {
			return show_wardname;
		}

		public void setShow_wardname(boolean show_wardname) {
			this.show_wardname = show_wardname;
		}


public boolean isDischarge_new() {
			return discharge_new;
		}

		public void setDischarge_new(boolean discharge_new) {
			this.discharge_new = discharge_new;
		}


public boolean isShow_unpost() {
			return show_unpost;
		}

		public void setShow_unpost(boolean show_unpost) {
			this.show_unpost = show_unpost;
		}


public boolean isSms_on_bedchange() {
			return sms_on_bedchange;
		}

		public void setSms_on_bedchange(boolean sms_on_bedchange) {
			this.sms_on_bedchange = sms_on_bedchange;
		}

		public boolean isSms_on_newadm() {
			return sms_on_newadm;
		}

		public void setSms_on_newadm(boolean sms_on_newadm) {
			this.sms_on_newadm = sms_on_newadm;
		}


public String getFullname() {
			return fullname;
		}

		public void setFullname(String fullname) {
			this.fullname = fullname;
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


public int getIpd_abbr_access() {
			return ipd_abbr_access;
		}

		public void setIpd_abbr_access(int ipd_abbr_access) {
			this.ipd_abbr_access = ipd_abbr_access;
		}


public boolean isHidelogoinvst() {
			return hidelogoinvst;
		}

		public void setHidelogoinvst(boolean hidelogoinvst) {
			this.hidelogoinvst = hidelogoinvst;
		}

		public boolean isHidelogoemr() {
			return hidelogoemr;
		}

		public void setHidelogoemr(boolean hidelogoemr) {
			this.hidelogoemr = hidelogoemr;
		}

		public boolean isHidelogobillinv() {
			return hidelogobillinv;
		}

		public void setHidelogobillinv(boolean hidelogobillinv) {
			this.hidelogobillinv = hidelogobillinv;
		}


public boolean isInvst_inv_apr() {
			return invst_inv_apr;
		}

		public void setInvst_inv_apr(boolean invst_inv_apr) {
			this.invst_inv_apr = invst_inv_apr;
		}


public int getPagelimitpharmacy() {
			return pagelimitpharmacy;
		}

		public void setPagelimitpharmacy(int pagelimitpharmacy) {
			this.pagelimitpharmacy = pagelimitpharmacy;
		}


public int getDischarge_validation() {
			return discharge_validation;
		}

		public void setDischarge_validation(int discharge_validation) {
			this.discharge_validation = discharge_validation;
		}


public String getPatientname_field() {
			return patientname_field;
		}

		public void setPatientname_field(String patientname_field) {
			this.patientname_field = patientname_field;
		}


public String getPractitonername_field() {
			return practitonername_field;
		}

		public void setPractitonername_field(String practitonername_field) {
			this.practitonername_field = practitonername_field;
		}


public int getAddress_manual() {
			return address_manual;
		}

		public void setAddress_manual(int address_manual) {
			this.address_manual = address_manual;
		}


public String getWarningmsg() {
			return warningmsg;
		}

		public void setWarningmsg(String warningmsg) {
			this.warningmsg = warningmsg;
		}


public String getUserMobileNo() {
			return userMobileNo;
		}

		public void setUserMobileNo(String userMobileNo) {
			this.userMobileNo = userMobileNo;
		}


public boolean isTreatment_episode_acc() {
			return treatment_episode_acc;
		}

		public void setTreatment_episode_acc(boolean treatment_episode_acc) {
			this.treatment_episode_acc = treatment_episode_acc;
		}


public boolean isPcsAdmin() {
			return pcsAdmin;
		}

		public void setPcsAdmin(boolean pcsAdmin) {
			this.pcsAdmin = pcsAdmin;
		}


private boolean purchase_edit_pharmacy;
private boolean acc_refund;
private boolean show_master; 
private boolean token_display;
private String cancel_po;
private boolean add_medicine;
private boolean isdotmatrix;
private boolean pharm_print_backdate;
private boolean bdaysms;
private boolean immusms;
private boolean f_diagnosis_discharge;
private boolean seq_no_gen;
private boolean misaccess;
private boolean removeprocurement;
private boolean modify_disc;
private boolean smsVisitingConslt;
private boolean show_hospital_admin;
private boolean direct_ipd;
private boolean drwise_ipd;
private boolean jobtitlewise_investigation;
private boolean editchargesacs;
private boolean show_wardname;
private boolean discharge_new;
private boolean show_unpost;
private boolean sms_on_bedchange;
private boolean sms_on_newadm;
private String fullname;
private String investigation_newaccess="";
private boolean cathlab; 
private int ipd_abbr_access;
private boolean hidelogoinvst;
private boolean hidelogoemr; 
private boolean hidelogobillinv;
private boolean invst_inv_apr;
private int pagelimitpharmacy;
private int discharge_validation;
private String patientname_field;
private String practitonername_field;
private int address_manual;
private String warningmsg;
private String userMobileNo;
private boolean treatment_episode_acc;
private boolean pcsAdmin;
private boolean balgopal;
public boolean isInvest_order() {
	return invest_order;
}


public void setInvest_order(boolean invest_order) {
	this.invest_order = invest_order;
}

public boolean isDemo_access() {
	return demo_access;
}


public void setDemo_access(boolean demo_access) {
	this.demo_access = demo_access;
}

public boolean isBalgopal() {
	return balgopal;
}

public void setBalgopal(boolean balgopal) {
	this.balgopal = balgopal;
}


public boolean isJson_diagnosis() {
	return json_diagnosis;
}

public void setJson_diagnosis(boolean json_diagnosis) {
	this.json_diagnosis = json_diagnosis;
}


public boolean isSupport_Access() {
	return support_Access;
}

public void setSupport_Access(boolean support_Access) {
	this.support_Access = support_Access;
}


public boolean isPaymentReport() {
	return paymentReport;
}

public void setPaymentReport(boolean paymentReport) {
	this.paymentReport = paymentReport;
}


private boolean invest_order,demo_access;
private boolean json_diagnosis;
private boolean support_Access;
private boolean paymentReport;

private boolean ipd_payamnt_sms,opd_payamnt_sms,adv_payamnt_sms,refund_payamnt_sms,other_payamnt_sms;


public boolean isIpd_payamnt_sms() {
	return ipd_payamnt_sms;
}


public void setIpd_payamnt_sms(boolean ipd_payamnt_sms) {
	this.ipd_payamnt_sms = ipd_payamnt_sms;
}


public boolean isOpd_payamnt_sms() {
	return opd_payamnt_sms;
}


public void setOpd_payamnt_sms(boolean opd_payamnt_sms) {
	this.opd_payamnt_sms = opd_payamnt_sms;
}


public boolean isAdv_payamnt_sms() {
	return adv_payamnt_sms;
}


public void setAdv_payamnt_sms(boolean adv_payamnt_sms) {
	this.adv_payamnt_sms = adv_payamnt_sms;
}


public boolean isRefund_payamnt_sms() {
	return refund_payamnt_sms;
}


public void setRefund_payamnt_sms(boolean refund_payamnt_sms) {
	this.refund_payamnt_sms = refund_payamnt_sms;
}


public boolean isOther_payamnt_sms() {
	return other_payamnt_sms;
}


public void setOther_payamnt_sms(boolean other_payamnt_sms) {
	this.other_payamnt_sms = other_payamnt_sms;
} 
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
public boolean isAdd_manual_charge() {
	return add_manual_charge;
}

public void setAdd_manual_charge(boolean add_manual_charge) {
	this.add_manual_charge = add_manual_charge;
}


public boolean isUpdate_investigation_charge() {
	return update_investigation_charge;
}

public void setUpdate_investigation_charge(boolean update_investigation_charge) {
	this.update_investigation_charge = update_investigation_charge;
}


public int getVacinator() {
	return vacinator;
}

public void setVacinator(int vacinator) {
	this.vacinator = vacinator;
}


private boolean add_manual_charge;
private boolean update_investigation_charge;
private int vacinator;
private boolean invest_savenprint;
private boolean prisc_savenprint;




public boolean isInvest_savenprint() {
	return invest_savenprint;
}

public void setInvest_savenprint(boolean invest_savenprint) {
	this.invest_savenprint = invest_savenprint;
}

public boolean isPrisc_savenprint() {
	return prisc_savenprint;
}

public void setPrisc_savenprint(boolean prisc_savenprint) {
	this.prisc_savenprint = prisc_savenprint;
}

public boolean isInvoicemodify() {
	return invoicemodify;
}

public void setInvoicemodify(boolean invoicemodify) {
	this.invoicemodify = invoicemodify;
}

public boolean isIndv_discount() {
	return indv_discount;
}

public void setIndv_discount(boolean indv_discount) {
	this.indv_discount = indv_discount;
}

public boolean isNewdischargecard() {
	return newdischargecard;
}

public void setNewdischargecard(boolean newdischargecard) {
	this.newdischargecard = newdischargecard;
}


public boolean isPayrollaccess() {
	return payrollaccess;
}

public void setPayrollaccess(boolean payrollaccess) {
	this.payrollaccess = payrollaccess;
}


public boolean isInvoice_date_modify() {
	return invoice_date_modify;
}

public void setInvoice_date_modify(boolean invoice_date_modify) {
	this.invoice_date_modify = invoice_date_modify;
}


public boolean isPackage_access() {
	return package_access;
}

public void setPackage_access(boolean package_access) {
	this.package_access = package_access;
}


public boolean isDischarge_msg_hs() {
	return discharge_msg_hs;
}

public void setDischarge_msg_hs(boolean discharge_msg_hs) {
	this.discharge_msg_hs = discharge_msg_hs;
}


public boolean isBarcode_productname_show() {
	return barcode_productname_show;
}

public void setBarcode_productname_show(boolean barcode_productname_show) {
	this.barcode_productname_show = barcode_productname_show;
}


public boolean isOpd_tp_zero_invoice() {
	return opd_tp_zero_invoice;
}

public void setOpd_tp_zero_invoice(boolean opd_tp_zero_invoice) {
	this.opd_tp_zero_invoice = opd_tp_zero_invoice;
}


public boolean isRelease_notes_upload() {
	return release_notes_upload;
}

public void setRelease_notes_upload(boolean release_notes_upload) {
	this.release_notes_upload = release_notes_upload;
}


public boolean isNew_aureus_discard() {
	return new_aureus_discard;
}

public void setNew_aureus_discard(boolean new_aureus_discard) {
	this.new_aureus_discard = new_aureus_discard;
}


public boolean isEmr_vitals_show() {
	return emr_vitals_show;
}

public void setEmr_vitals_show(boolean emr_vitals_show) {
	this.emr_vitals_show = emr_vitals_show;
}


public boolean isDeleted_invst_charge() {
	return deleted_invst_charge;
}

public void setDeleted_invst_charge(boolean deleted_invst_charge) {
	this.deleted_invst_charge = deleted_invst_charge;
}




private boolean newdischargecard;
private boolean discharge_msg_hs;
private boolean barcode_productname_show;
private boolean release_notes_upload;
private boolean new_aureus_discard;
private boolean emr_vitals_show;
private boolean deleted_invst_charge;
private String invoice_default_note;
public String getInvoice_default_note() {
	return invoice_default_note;
}

public void setInvoice_default_note(String invoice_default_note) {
	this.invoice_default_note = invoice_default_note;
}

public boolean isDisc_approve_sms() {
	return disc_approve_sms;
}

public void setDisc_approve_sms(boolean disc_approve_sms) {
	this.disc_approve_sms = disc_approve_sms;
}


}
