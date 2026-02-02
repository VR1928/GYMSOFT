package com.apm.Ipd.web.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.allcolor.yahp.converter.CYaHPConverter;
import org.allcolor.yahp.converter.IHtmlToPdfTransformer;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import com.apm.Accounts.eu.bi.AccountsDAO;
import com.apm.Accounts.eu.blogic.jdbc.JDBCAccountsDAO;
import com.apm.Appointment.eu.entity.AppointmentType;
import com.apm.DiaryManagement.eu.bi.ClientDAO;
import com.apm.DiaryManagement.eu.bi.DiaryManagementDAO;
import com.apm.DiaryManagement.eu.bi.NotAvailableSlotDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCClientDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCDiaryManagentDAO;
import com.apm.DiaryManagement.eu.blogic.jdbc.JDBCNotAvailableSlotDAO;
import com.apm.DiaryManagement.eu.entity.Client;
import com.apm.DiaryManagement.eu.entity.DiaryManagement;
import com.apm.DiaryManagement.eu.entity.NotAvailableSlot;
import com.apm.DiaryManagement.eu.entity.Priscription;
import com.apm.DiaryManagement.web.action.EmbeddedImageEmailUtil;
import com.apm.DiaryManagement.web.action.SmsService;
import com.apm.Dietary.eu.entity.DietaryDetails;
import com.apm.Emr.eu.bi.EmrDAO;
import com.apm.Emr.eu.bi.InvestigationDAO;
import com.apm.Emr.eu.blogic.jdbc.JDBCEmrDAO;
import com.apm.Emr.eu.blogic.jdbc.JDBCInvestigationDAO;
import com.apm.Emr.eu.entity.Investigation;
import com.apm.Emr.eu.entity.InvstTemplate;
import com.apm.Ipd.eu.bi.BedDao;
import com.apm.Ipd.eu.bi.IpdDAO;
import com.apm.Ipd.eu.bi.IpdLogDAO;
import com.apm.Ipd.eu.blogic.jdbc.JDBCBedDao;
import com.apm.Ipd.eu.blogic.jdbc.JDBCIpdDAO;
import com.apm.Ipd.eu.blogic.jdbc.JDBCIpdLogDAO;
import com.apm.Ipd.eu.entity.Bed;
import com.apm.Ipd.eu.entity.Discharge;
import com.apm.Ipd.eu.entity.Ipd;
import com.apm.Ipd.web.form.IpdForm;
import com.apm.Log.eu.entity.EmailLetterLog;
import com.apm.Master.eu.bi.DischargeOutcomeDAO;
import com.apm.Master.eu.bi.DischargeStatusDAO;
import com.apm.Master.eu.bi.MasterDAO;
import com.apm.Master.eu.bi.TreatmentTypeDAO;
import com.apm.Master.eu.blogic.jdbc.JDBCDischargeOutcomeDAO;
import com.apm.Master.eu.blogic.jdbc.JDBCDischargeStatus;
import com.apm.Master.eu.blogic.jdbc.JDBCMasterDAO;
import com.apm.Master.eu.blogic.jdbc.JDBCTreatmentTypeDAO;
import com.apm.Master.eu.entity.Master;
import com.apm.Master.eu.entity.TreatmentType;
import com.apm.Pharmacy.eu.bi.PharmacyDAO;
import com.apm.Pharmacy.eu.blogic.jdbc.JDBCPharmacyDAO;
import com.apm.Registration.eu.bi.ClinicDAO;
import com.apm.Registration.eu.bi.UserProfileDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCClinicDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCUserProfileDAO;
import com.apm.Registration.eu.entity.Clinic;
import com.apm.Registration.eu.entity.UserProfile;
import com.apm.Support.eu.bi.UserAdministartionDAO;
import com.apm.Support.eu.blogic.jdbc.JDBCUserAdministration;
import com.apm.ThirdParties.eu.bi.ThirdPartyDAO;
import com.apm.ThirdParties.eu.blogic.jdbc.JDBCThirdPartyDAO;
import com.apm.ThirdParties.eu.entity.ThirdParty;
import com.apm.Tools.web.action.AllTemplateAction;
import com.apm.TreatmentEpisode.eu.bi.TreatmentEpisodeDAO;
import com.apm.TreatmentEpisode.eu.blogic.jdbc.JDBCTreatmentEpisode;
import com.apm.TreatmentEpisode.eu.entity.TreatmentEpisode;
import com.apm.common.eu.blogic.jdbc.Connection_provider;
import com.apm.common.utils.DateTimeUtils;
import com.apm.common.web.action.BaseAction;
import com.apm.common.web.common.helper.LoginHelper;
import com.apm.common.web.common.helper.LoginInfo;
import com.apm.common.web.utils.PopulateList;
import com.apm.main.common.constants.Constants;
import com.onbarcode.barcode.Code128;
import com.onbarcode.barcode.Code39;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;
import atg.taglib.json.util.JSONObject;


public class CommonIpdAction extends BaseAction implements ModelDriven<IpdForm>, Preparable {

	HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
	HttpServletResponse response = (HttpServletResponse) ActionContext.getContext()
			.get(ServletActionContext.HTTP_RESPONSE);
	HttpSession session = request.getSession(true);
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);

	IpdForm ipdForm = new IpdForm();

	public void prepare() throws Exception {
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			PharmacyDAO pharmacyDAO = new JDBCPharmacyDAO(connection);
			ArrayList<Master> requestlocationlist= pharmacyDAO.getAllLocationNew();
			ipdForm.setRequestlocationlist(requestlocationlist);
			if(loginInfo.isPrisc_location_list()){
				int default_location = pharmacyDAO.getByDefaultPharmacyLocation();
				ipdForm.setRequestlocationid(""+default_location);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public IpdForm getModel() {
		return ipdForm;
	}

	public String status() throws Exception {
		String treatmentepisodeid = request.getParameter("ipdtreatmentepisodeid");
		String clientid = request.getParameter("clientid");
		Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			IpdDAO ipdDAO = new JDBCIpdDAO(connection);
			ClientDAO clientDAO = new JDBCClientDAO(connection);

			Discharge discharge = ipdDAO.getDischargeDetails(treatmentepisodeid);
			Client client = clientDAO.getClientDetails(clientid);
			String fullname = client.getTitle() + " " + client.getFirstName() + " " + client.getLastName();

			String list1 = ipdDAO.getDischargeChecklistDataText(treatmentepisodeid, clientid, "1", discharge);
			String list2 = ipdDAO.getDischargeChecklistDataText(treatmentepisodeid, clientid, "2", discharge);
			String list3 = ipdDAO.getDischargeChecklistDataText(treatmentepisodeid, clientid, "3", discharge);
			String list4 = ipdDAO.getDischargeChecklistDataText(treatmentepisodeid, clientid, "4", discharge);

			String str = discharge.getDis_form_status() + "~" + discharge.getDis_form_time() + "~"
					+ discharge.getDis_mdicine_status() + "~" + discharge.getDis_mdicine_time() + "~"
					+ discharge.getDis_bill_status() + "~" + discharge.getDis_bill_time() + "~"
					+ discharge.getDis_nursing_status() + "~" + discharge.getDis_nursing_time() + "~"
					+ discharge.getDis_initiate_status() + "~" + discharge.getDis_initiate_time() + "~" + fullname;

			StringBuilder builder = new StringBuilder();
			builder.append("" + discharge.getDis_form_status() + "");
			builder.append("~");
			builder.append("" + discharge.getDis_form_time() + "");
			builder.append("~");
			builder.append("" + discharge.getDis_mdicine_status() + "");
			builder.append("~");
			builder.append("" + discharge.getDis_mdicine_time() + "");
			builder.append("~");
			builder.append("" + discharge.getDis_bill_status() + "");
			builder.append("~");
			builder.append("" + discharge.getDis_bill_time() + "");
			builder.append("~");
			builder.append("" + discharge.getDis_nursing_status() + "");
			builder.append("~");
			builder.append("" + discharge.getDis_nursing_time() + "");
			builder.append("~");
			builder.append("" + discharge.getDis_initiate_status() + "");
			builder.append("~");
			builder.append("" + discharge.getDis_initiate_time() + "");
			builder.append("~");
			builder.append("" + fullname + "");
			builder.append("~");
			builder.append("<ul>");
			builder.append("  " + list1);
			builder.append("</ul>");

			builder.append("~");
			builder.append("<ul>");
			if (discharge.getDis_mdicine_status().equals("0")) {
				builder.append(
						"<li class='setlimet'><label class='checkbox checkbox-custom-alt'><input onclick='selectAllChecklist(this.value,this.checked,"
								+ treatmentepisodeid
								+ ")' value='akashbclass' type='checkbox'><i></i>Select All</label></li>");
			} else {
				builder.append(
						"<li class='setlimet'><label class='checkbox checkbox-custom-alt'><input type='checkbox'><i></i>Select All</label></li>");
			}
			builder.append(" " + list2);
			builder.append("</ul>");

			builder.append("~");
			builder.append("<ul>");
			if (discharge.getDis_bill_status().equals("0")) {
				builder.append(
						"<li class='setlimet'><label class='checkbox checkbox-custom-alt'><input onclick='selectAllChecklist(this.value,this.checked,"
								+ treatmentepisodeid
								+ ")' value='akashcclass' type='checkbox'><i></i>Select All</label></li>");
			} else {
				builder.append(
						"<li class='setlimet'><label class='checkbox checkbox-custom-alt'><input type='checkbox'><i></i>Select All</label></li>");
			}

			builder.append(" " + list3);
			builder.append("</ul>");

			builder.append("~");
			builder.append("<ul>");
			if (discharge.getDis_nursing_status().equals("0")) {
				builder.append(
						"<li class='setlimet'><label class='checkbox checkbox-custom-alt'><input onclick='selectAllChecklist(this.value,this.checked,"
								+ treatmentepisodeid
								+ ")' value='akashdclass' type='checkbox'><i></i>Select All</label></li>");
			} else {
				builder.append(
						"<li class='setlimet'><label class='checkbox checkbox-custom-alt'><input type='checkbox'><i></i>Select All</label></li>");
			}
			builder.append(" " + list4);
			builder.append("</ul>");

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			// response.getWriter().write(""+str.toString()+"");
			response.getWriter().write("" + builder.toString() + "");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return null;
	}

	public String getipdtemplate() throws Exception {

		Connection connection = null;

		try {
			connection = Connection_provider.getconnection();
			MasterDAO masterDAO = new JDBCMasterDAO(connection);
			String id = request.getParameter("id");

			Master master = masterDAO.getIpdTemplate(id);

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("" + master.getText() + "");

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			connection.close();
		}

		return null;

	}

	public String updatedischarge() throws Exception {

		if (!verifyLogin(request)) {
			return "login";
		}
		Connection connection = null;

		try {

			connection = Connection_provider.getconnection();
			IpdDAO ipdDAO = new JDBCIpdDAO(connection);
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			BedDao bedDao = new JDBCBedDao(connection);
			MasterDAO masterDAO = new JDBCMasterDAO(connection);
			UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
			String sessionadmissionid = ipdForm.getClientip();

			System.out.println(sessionadmissionid);

			session.setAttribute("qipd", sessionadmissionid);
			Bed bed = bedDao.getEditIpdData(sessionadmissionid);
			String patientid = bed.getClientid();
			String treatmentEpisodeid = bed.getTreatmentepisodeid();
			String condition = bed.getConditionid();
			String practionerId = bed.getPractitionerid();
			String otnotesid = ipdForm.getOtNotesids();
			String priscids = ipdForm.getPriscid();

			for (String notesid : otnotesid.split(",")) {
				if (notesid.equals("0")) {
					continue;
				}
				int otnote = ipdDAO.updateotnotesid(notesid);
			}
			TreatmentTypeDAO treatmentTypeDAO = new JDBCTreatmentTypeDAO(connection);
			ArrayList<TreatmentType> conditionlist = new ArrayList<TreatmentType>();

			ipdForm.setConditionlist(conditionlist);

			session.setAttribute("sessionselectedclientid", bed.getClientid());

			// update status

			String datetime = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
			int valuetoupdate = 1;

			int update = ipdDAO.updateInitialDischargeStatus("dis_form_status", "dis_form_time", valuetoupdate,
					datetime, bed.getTreatmentepisodeid(), loginInfo.getUserId());

			/*
			 * Bed bed = new Bed();
			 * bed.setHospitalcourse(ipdForm.getHospitalcourse());
			 * 
			 * 
			 * bed.setDischargedate(dischargedate);
			 * bed.setHour(ipdForm.getHour());
			 * bed.setMinute(ipdForm.getMinute());
			 * bed.setDischargeStatus(ipdForm.getDischargeStatus());
			 * bed.setDischrgeOutcomes(ipdForm.getDischrgeOutcomes());
			 * bed.setDiscadvnotes(ipdForm.getDiscadvnotes());
			 * bed.setChkDischarge(ipdForm.isChkDischarge());
			 */

			Bed bed2 = new Bed();
			bed2.setDischrgeOutcomes(ipdForm.getDischrgeOutcomes());
			bed2.setDischargeStatus(ipdForm.getDischargeStatus());
			// bed2.setTreatmentepisodeid(ipdForm.getHospitalcourse());
			String hcourse = ipdForm.getHospitalcourse();
			bed2.setHospitalcourse(ipdForm.getHospitalcourse());
			bed2.setDiscadvnotes(ipdForm.getDiscadvnotes());
			bed2.setFindondischarge(ipdForm.getFindondischarge());
			bed2.setTreatmentgiven(ipdForm.getTreatmentgiven());
			bed2.setInvestigation(ipdForm.getInvestigation());
			bed2.setOtNotes(ipdForm.getOtNotes());
			String appt="";
			for(String s : ipdForm.getAppointmentText().split(",")){
				if(s.equals("0")){
					continue;
				}
				if(appt.equals("")){
					appt=s;
				}else{
					appt=appt+","+s;
				}
				
			}
			bed2.setAppointmentText(appt);
			bed2.setAnesthesia(ipdForm.getAnesthesia());
			bed2.setSurgeon(ipdForm.getSurgeon());
			bed2.setAnesthesiologist(ipdForm.getAnesthesiologist());
			bed2.setTreatmentepisodeid(treatmentEpisodeid);
			bed2.setExample(ipdForm.getExample());
			bed2.setDischargebyid(loginInfo.getUserId());
			bed2.setDeathnote(ipdForm.getDeathnote());
			bed2.setEmergencydetail(ipdForm.getEmergencydetail());
			
			if(loginInfo.isNew_aureus_discard()){
				String dietryDetails=ipdForm.getNewCardFields().getDietary_advice();
				int r=bedDao.saveNewFieldsData(sessionadmissionid, "dietary_advice", dietryDetails);
				 r=bedDao.saveNewFieldsData(sessionadmissionid, "local_relevant_area", ipdForm.getNewCardFields().getLocal_relevant_area());
				 r=bedDao.saveNewFieldsData(sessionadmissionid, "tubes_and_training", ipdForm.getNewCardFields().getTubes_and_training());
				 r=bedDao.saveNewFieldsData(sessionadmissionid, "line_tube_drains", ipdForm.getNewCardFields().getLine_tube_drains());
				 r=bedDao.saveNewFieldsData(sessionadmissionid, "when_to_get_help", ipdForm.getNewCardFields().getWhen_to_get_help());
				 r=bedDao.saveNewFieldsData(sessionadmissionid, "general_other", ipdForm.getNewCardFields().getGeneral_other());
				 r=bedDao.saveNewFieldsData(sessionadmissionid, "call_for_appointmant", ipdForm.getNewCardFields().getCall_for_appointmant());
				 r=bedDao.saveNewFieldsData(sessionadmissionid, "consent_sign", ipdForm.getNewCardFields().getConsent_sign());
			}
				
			 
			 
			int status = 0;
			String dischargedate = DateTimeUtils.getCommencingDate1(ipdForm.getDischargedate()) + " "
					+ ipdForm.getHour() + ":" + ipdForm.getMinute() + ":20";

			bed2.setDischargedate(dischargedate);
			int disstatus = ipdDAO.gettreatmentstatus(bed.getTreatmentepisodeid());
			if (disstatus == 1) {
				status = 1;
			}
			// if(ipdForm.isChkDischarge()){
			// status = 1;
			// }

			if (status == 1) {
				int res = bedDao.updateBedStatus(sessionadmissionid);
				int log = bedDao.saveDischargeLog(sessionadmissionid, patientid, dischargedate, "", loginInfo);
			}

			bed2.setStatus("" + status);

			if (bed2.getAppointmentText() != null) {
				if (bed2.getAppointmentText().equals("0,") || bed2.getAppointmentText().equals("0, ")) {
					bed2.setAppointmentText("0");
				}
			}
			String test = bed2.getAppointmentText();
			if (status == 1) {

				/*
				 * int upd =
				 * emrDAO.updateTreatmentEpisodeSischargeStatus(ipdForm.
				 * getDischrgeOutcomes(),
				 * ipdForm.getDischargeStatus(),status,treatmentEpisodeid,
				 * dischargedate,
				 * ipdForm.getHospitalcourse(),ipdForm.getDiscadvnotes(),ipdForm
				 * .getFindondischarge(),ipdForm.getTreatmentgiven(),
				 * ipdForm.getInvestigation(),ipdForm.getOtNotes());
				 */
				int upd = emrDAO.updateTreatmentEpisodeDischargeForm(bed2);

				AllTemplateAction allTemplateAction = new AllTemplateAction();
				allTemplateAction.sendDischargeEmail(patientid, condition, practionerId, "0", loginInfo, connection,
						treatmentEpisodeid);
			} else {
				/*
				 * emrDAO.updateTreatmentEpisodeSischargeStatus(ipdForm.
				 * getDischrgeOutcomes(),
				 * ipdForm.getDischargeStatus(),status,treatmentEpisodeid,
				 * dischargedate,
				 * ipdForm.getHospitalcourse(),ipdForm.getDiscadvnotes(),ipdForm
				 * .getExample(),ipdForm.getFindondischarge(),ipdForm.
				 * getTreatmentgiven(),ipdForm.getInvestigation(),ipdForm.
				 * getOtNotes());
				 */
				int upd = emrDAO.updateTreatmentEpisodeDischargeForm(bed2);
			}

			// update discharge ot notes and data
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			NotAvailableSlot notAvailableSlot = new NotAvailableSlot();
			notAvailableSlot.setProcedure(ipdForm.getProcedure());
			notAvailableSlot.setAnesthesia(ipdForm.getAnesthesia());
			notAvailableSlot.setSurgeon(ipdForm.getSurgeon());
			notAvailableSlot.setOtnotes(ipdForm.getOtNotes());
			notAvailableSlot.setApmttypetext(ipdForm.getAppointmentText());
			ipdForm.setAnsintime(notAvailableSlot.getAnsintime());

			int res = notAvailableSlotDAO.updateDischargeOtData(sessionadmissionid, notAvailableSlot);

			datetime = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());

			// for update delete previous conditions @jitu
			res = ipdDAO.deleteFinalConditions(sessionadmissionid, treatmentEpisodeid);
			res = ipdDAO.deleteConditionReport(sessionadmissionid, treatmentEpisodeid);

			String allconditions = "0";
			if (ipdForm.getConditions() != null) {
				for (Bed bedcondition : ipdForm.getConditions()) {

					if (bedcondition == null) {
						continue;
					}
					allconditions = allconditions + "," + bedcondition.getConditionid();
					bedcondition.setTreatmentepisodeid(treatmentEpisodeid);
					bedcondition.setLastmodified(datetime);

					boolean isexist = bedDao.isIpdExistCondition(sessionadmissionid, treatmentEpisodeid,
							bedcondition.getConditionid());
					if (!isexist) {
						int dd = bedDao.addIpdConditionReport(Integer.parseInt(sessionadmissionid), bedcondition);
					}
				}
			}
			int result = ipdDAO.savefinalConditionDischarge(sessionadmissionid, treatmentEpisodeid, datetime,
					allconditions);

			// 23 jan 18 Akash sms comment
			// autosmsDischarge(patientid);

			// Akash 06 nov 2017
			String userid = loginInfo.getUserId();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			String todate = dateFormat.format(calendar.getTime());
			boolean flag = ipdDAO.getIsDisCheckListStatus("1", bed.getTreatmentepisodeid());

			if (!flag) {
				int res99 = ipdDAO.updateCheckListStatus("1", "1", bed.getTreatmentepisodeid(), userid, todate);
				int res1 = ipdDAO.updateCheckListStatus("1", "2", bed.getTreatmentepisodeid(), userid, todate);
				int res2 = ipdDAO.updateCheckListStatus("1", "3", bed.getTreatmentepisodeid(), userid, todate);
				int res3 = ipdDAO.updateCheckListStatus("1", "4", bed.getTreatmentepisodeid(), userid, todate);
				int disdataid1 = ipdDAO.getDisDataId("1", bed.getTreatmentepisodeid());
				int disdataid2 = ipdDAO.getDisDataId("2", bed.getTreatmentepisodeid());
				int disdataid3 = ipdDAO.getDisDataId("3", bed.getTreatmentepisodeid());
				int disdataid4 = ipdDAO.getDisDataId("4", bed.getTreatmentepisodeid());
				int res6 = ipdDAO.updateDischargeCKLIndi(disdataid1);
				int res7 = ipdDAO.updateDischargeCKLIndi(disdataid2);
				int res8 = ipdDAO.updateDischargeCKLIndi(disdataid3);
				int res9 = ipdDAO.updateDischargeCKLIndi(disdataid4);
			}

			if (ipdForm.getHospitalcourse() != null) {
				if (!ipdForm.getHospitalcourse().equals("")) {
					if (!ipdForm.getHospitalcourse().equals("<br>")) {

						boolean flag2 = ipdDAO.getIsDisCheckListStatus("5", bed.getTreatmentepisodeid());
						if (!flag2) {
							int res4 = ipdDAO.updateCheckListStatus("1", "5", bed.getTreatmentepisodeid(), userid,
									todate);
							int disdataid4 = ipdDAO.getDisDataId("5", bed.getTreatmentepisodeid());
							int res8 = ipdDAO.updateDischargeCKLIndi(disdataid4);
						} else {
							int res9 = ipdDAO.updateCheckListStatusSystemModify("1", "5", bed.getTreatmentepisodeid(),
									userid, todate, "true");
						}
					}
				}
			}
			for (String priscid : priscids.split(",")) {
				if (priscid.equals("0")) {
					continue;
				}
				int prisc = ipdDAO.updateprisc(priscid);
			}
			String invstids = ipdForm.getInvstids();
			/*
			 * for(String invstid: invstids.split(",") ){
			 * if(invstid.equals("0")){ continue; } int invst1=
			 * ipdDAO.updateinvst(invstid); }
			 */

			String rmonotesids = ipdForm.getRmonotesids();
			for (String rmonotesid : rmonotesids.split(",")) {
				if (rmonotesid.equals("0")) {
					continue;
				}
				int invst1 = ipdDAO.updateRMONotesDisplayed(rmonotesid);
			}

			// 29 NOV 2017 Akash

			if (ipdForm.getDisdefaulttempname() != null) {
				if (!ipdForm.getDisdefaulttempname().equals("")) {
					String discharge_default_id = masterDAO.getIpdTemplateId("Discharge Default");
					int res5 = ipdDAO.saveIPDTemplate(ipdForm.getDisdefaulttempname(), discharge_default_id,
							ipdForm.getDepartment(), ipdForm.getExample());
				}
			}
			if (ipdForm.getHospitalcoursetempname() != null) {
				if (!ipdForm.getHospitalcoursetempname().equals("")) {
					String hospital_course_id = masterDAO.getIpdTemplateId("Hospital Course");
					int res5 = ipdDAO.saveIPDTemplate(ipdForm.getHospitalcoursetempname(), hospital_course_id,
							ipdForm.getDepartment(), ipdForm.getHospitalcourse());
				}
			}
			if (ipdForm.getNursingadvicetempname() != null) {
				if (!ipdForm.getNursingadvicetempname().equals("")) {
					String nursing_advice_id = masterDAO.getIpdTemplateId("Nursing Advice");
					int res5 = ipdDAO.saveIPDTemplate(ipdForm.getNursingadvicetempname(), nursing_advice_id,
							ipdForm.getDepartment(), ipdForm.getDiscadvnotes());
				}
			}
			if (ipdForm.getInvestigationtempname() != null) {
				if (!ipdForm.getInvestigationtempname().equals("")) {
					String invenstigations = masterDAO.getIpdTemplateId("Investigations");
					int res5 = ipdDAO.saveIPDTemplate(ipdForm.getInvestigationtempname(), invenstigations,
							ipdForm.getDepartment(), ipdForm.getInvestigation());
				}
			}
			if (ipdForm.getFindingondistempname() != null) {
				if (!ipdForm.getFindingondistempname().equals("")) {
					String finding_on_discharge = masterDAO.getIpdTemplateId("FINDING ON DISCHARGE");
					int res5 = ipdDAO.saveIPDTemplate(ipdForm.getFindingondistempname(), finding_on_discharge,
							ipdForm.getDepartment(), ipdForm.getFindondischarge());
				}
			}

			if (ipdForm.getPerinataltemp() != null) {
				if (!ipdForm.getPerinataltemp().equals("")) {
					String getPerinataltemp = masterDAO.getIpdTemplateId("Perinatal History");
					int res5 = ipdDAO.saveIPDTemplate(ipdForm.getPerinataltemp(), getPerinataltemp,
							ipdForm.getDepartment(), ipdForm.getPerinatal_history());
				}
			}
			
			if (ipdForm.getMaternalhisttemp() != null) {
				if (!ipdForm.getMaternalhisttemp().equals("")) {
					String getMaternalhisttemp = masterDAO.getIpdTemplateId("Maternal History");
					int res5 = ipdDAO.saveIPDTemplate(ipdForm.getMaternalhisttemp(), getMaternalhisttemp,
							ipdForm.getDepartment(), ipdForm.getMaternal_history());
				}
			}
			
			UserProfile userProfile = userProfileDAO
					.getUserprofileDetails(Integer.parseInt(ipdForm.getPractitionerid()));
			if (ipdForm.getOperativetempname() != null) {
				if (!ipdForm.getOperativetempname().equals("")) {
					/*
					 * Master master = new Master();
					 * master.setOther_template_text(ipdForm.getOtNotes());
					 * master.setTitle(ipdForm.getOperativetempname());
					 * master.setDiscipline_id(userProfile.getDiciplineName());
					 * int res5 = masterDAO.addOtherTemplate(master);
					 */
					String surgical_template = masterDAO.getIpdTemplateId("OT Template");
					int res5 = ipdDAO.saveIPDTemplate(ipdForm.getOperativetempname(), surgical_template,
							ipdForm.getDepartment(), ipdForm.getOtNotes());
				}
			}

			// Akash 27 Nov 2017
			String admissionfr = ipdForm.getAddmissionfor();
			String alergies = ipdForm.getAlergies();
			String chiefcomplatetempname = ipdForm.getChiefcomplatetempname();
			String presentillnesstempname = ipdForm.getPresentillnesstempname();
			Bed bed3 = new Bed();
			bed3.setAddmissionfor(ipdForm.getAddmissionfor());
			bed3.setAlergies(ipdForm.getAlergies());
			bed3.setChiefcomplains(ipdForm.getChiefcomplains());
			bed3.setPresentillness(ipdForm.getPresentillness());
			bed3.setIpdid(sessionadmissionid);
			bed3.setSuggestedtrtment(ipdForm.getSuggestedtrtment());
			// bed3.setSuggestedtrtment(ipdForm.getTreatmenthistory());
			bed3.setEarlierinvest(ipdForm.getEarlierinvest());
			bed3.setSurgicalnotes(ipdForm.getSurgicalnotes());
			bed3.setDiethist(ipdForm.getDiethist());
			bed3.setEmmunizationhist(ipdForm.getEmmunizationhist());
			bed3.setDevelopmenthist(ipdForm.getDevelopmenthist());
			bed3.setBirthhist(ipdForm.getBirthhist());
			bed3.setHeadcircumference(ipdForm.getHeadcircumference());
			bed3.setMidarmcircumference(ipdForm.getMidarmcircumference());
			bed3.setLength(ipdForm.getLength());
			bed3.setWtaddmission(ipdForm.getWtaddmission());
			bed3.setWtdischarge(ipdForm.getWtdischarge());

			// new datra
			bed3.setPersonalhist(ipdForm.getPersonalhist());
			bed3.setFamily_history(ipdForm.getFamilyhist());
			bed3.setPasthistory(ipdForm.getPasthistory());
			bed3.setSurgicalnotes(ipdForm.getSurgicalnotes());
			bed3.setOnexamination(ipdForm.getOnexamination());
			bed3.setSuggestedtrtment(ipdForm.getSuggestedtrtment());
			bed3.setEarlierinvest(ipdForm.getEarlierinvest());

			// kunal
			bed3.setKunal_final_diagnosis_text(ipdForm.getKunal_final_diagnosis_text());
			bed3.setKunal_manual_medicine_text(ipdForm.getKunal_manual_medicine_text());
			bed3.setGstureage(ipdForm.getGstureage());
			bed3.setWtonbirth(ipdForm.getWtonbirth());
			bed3.setMaternal_history(ipdForm.getMaternal_history());
			bed3.setPerinatal_history(ipdForm.getPerinatal_history());
			bed3.setReasonlamadama(ipdForm.getLamadamareason());
			int resultt = ipdDAO.updateAdmDataFromDisc(bed3);

			if (chiefcomplatetempname != null) {
				if (!chiefcomplatetempname.equals("")) {
					int res5 = ipdDAO.saveIPDTemplate(chiefcomplatetempname, "1", ipdForm.getDepartment(),
							ipdForm.getChiefcomplains());
				}
			}
			if (presentillnesstempname != null) {
				if (!presentillnesstempname.equals("")) {
					int res5 = ipdDAO.saveIPDTemplate(presentillnesstempname, "2", ipdForm.getDepartment(),
							ipdForm.getPresentillness());
				}
			}

			// Akash 01 feb 18
			// If discharge form is fillled then by default, patient to be mark
			// d as discharge Initiated.
			/*
			 * String column="dis_initiate_status"; String
			 * column2="dis_initiate_time"; String datetime1 =
			 * DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()); int
			 * valuetoupdate1 =
			 * ipdDAO.getValueToUpdate(column,bed.getTreatmentepisodeid());
			 * //beacuse it return vice versa i.e i fit return 1 that means it
			 * status 0 if(valuetoupdate1==1){ int update1 =
			 * ipdDAO.updateInitialDischargeStatus(column,column2,valuetoupdate1
			 * ,datetime1,bed.getTreatmentepisodeid()); }
			 */

			// Akash 03 April 2018
			String totalotids = ipdForm.getTotalotids();
			if (totalotids != null) {
				for (String id : totalotids.split(",")) {
					if (id.equals("0")) {
						continue;
					}
					String editotprocedure = request.getParameter("editotprocedure" + id);
					int res4 = ipdDAO.updateDischrgeOTProcedure(editotprocedure, id);
				}
			}

			// Akash 04 April 2018 prisc sr wise
			String totalchildmedids = ipdForm.getTotalchildmedids();
			if (totalchildmedids != null) {
				for (String id : totalchildmedids.split(",")) {
					if (id.equals("0")) {
						continue;
					}
					String dicpriscmedsrno = request.getParameter("dicpriscmed" + id);
					String dicpriscdose = request.getParameter("discpriscdose" + id);
					String dicpriscdays = request.getParameter("dicpriscdays" + id);
					int res5 = ipdDAO.updateDischrgePriscSrNo(dicpriscmedsrno, id, dicpriscdose, dicpriscdays);
				}
			}

			String followupdate = request.getParameter("followupdate1");
			if (followupdate == null) {
				followupdate = "";
			}
			if (!followupdate.equals("")) {
				followupdate = DateTimeUtils.getCommencingDate1(followupdate);
				DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				String date = dateFormat1.format(cal.getTime());
				Client client = new Client();

				client.setIpdid(sessionadmissionid);
				client.setClientId(bed.getClientid());
				client.setType("1");
				client.setFollowupdate(followupdate);
				client.setDate(date);
				client.setPractid(bed.getPractitionerid());
				client.setLocation("IPD Discharge");
				ClientDAO clientDAO = new JDBCClientDAO(connection);
				clientDAO.savefollowup(client);
			}
			 int rest=bedDao.updateUseridInTable(loginInfo.getUserId(), bed.getTreatmentepisodeid(), "lastformfilleruserid");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
		return "dischargesaved";

	}

	public String dynamictemplatesave() {
		try {
			Connection connection = Connection_provider.getconnection();
			StringBuilder buffer = new StringBuilder();
			BufferedReader reader = request.getReader();
			String line;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			String data = buffer.toString();
			JSONObject jsonObject = new JSONObject(data);

			String id = jsonObject.getString("id");
			String name = jsonObject.getString("name");
			String datatext = jsonObject.getString("dataa");

			IpdDAO ipdDAO = new JDBCIpdDAO(connection);
			int x = ipdDAO.saveIpdTemplates(id, datatext, name);

			JSONObject jsonobj = new JSONObject();
			jsonobj.put("noob", "" + x);
			String response1 = jsonobj.toString();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("" + response1);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String printdischarge() throws SQLException {

		if (!verifyLogin(request)) {
			return "login";
		}
		Connection connection = null;

		String clientid = request.getParameter("clientid");

		if (clientid == null) {
			clientid = (String) session.getAttribute("sessionselectedclientid");
		}

		try {

			// connection=Connection_provider.getconnection();
			connection = DriverManager.getConnection("" + Constants.DB_HOST + ":3306/" + loginInfo.getClinicUserid()
					+ "?useUnicode=true&characterEncoding=UTF-8", "pranams", "6qxi5x&)~XBZ");
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
			MasterDAO masterDAO = new JDBCMasterDAO(connection);
			String tempid = DateTimeUtils.isNull(request.getParameter("selectedid"));
			if(tempid.equals("")||tempid.equals("0")){
				
			}else{
				BedDao bedDao1 = new JDBCBedDao(connection);
				
				Bed b = bedDao1.getEditIpdData(tempid);
				clientid=DateTimeUtils.isNull(clientid);
				if(clientid.equals("")||clientid.equals("0")){
					clientid=b.getClientid();
				}
			}
			
			if (clientid != null) {
				IpdDAO ipdDAO = new JDBCIpdDAO(connection);
				BedDao bedDao = new JDBCBedDao(connection);
				String selectedid = ipdDAO.getDischargedIpdid(clientid);

				String reqselectedid = request.getParameter("selectedid");
				if (reqselectedid != null) {
					selectedid = reqselectedid;
				}

				Bed bed = bedDao.getEditIpdData(selectedid);
				if (loginInfo.getIpd_abbr_access() == 1) {
					String newipdabbr = ipdDAO.getIpdAbrivationIds(Integer.parseInt(selectedid));
					ipdForm.setNewipdabbr(newipdabbr);
					if (Integer.parseInt(bed.getIpdseqno()) > 0) {
						ipdForm.setIpdseqno(bed.getIpdseqno());
					} else {
						ipdForm.setIpdseqno(selectedid);
					}
				} else {
					if (Integer.parseInt(bed.getIpdseqno()) > 0) {
						ipdForm.setIpdseqno(bed.getIpdseqno());
						ipdForm.setNewipdabbr(bed.getIpdseqno());
					} else {
						ipdForm.setIpdseqno(selectedid);
						ipdForm.setNewipdabbr(selectedid);
					}

				}
				
				if(loginInfo.isNew_aureus_discard()){
					if(!bedDao.entryExistsInNewDischargeFields(selectedid)){
						bedDao.makeEntryToNewDischargeFields(bed.getClientid(), selectedid, bed.getTreatmentepisodeid());
					}
					ipdForm.setNewCardFields(bedDao.newDischargeCardDetails(selectedid));
					
					ArrayList<Master> vitalList=masterDAO.getallVitalMasterdata("1");
					for (Master master : vitalList) {
						master.setFinding(masterDAO.getdischargeVitalVal(""+master.getId(), selectedid));
					}
					ipdForm.setVitalList(vitalList);
				}
				
				Client client = clientDAO.getClientDetails(clientid);
				ipdForm.setAddress(client.getAddress());
				ipdForm.setAddr(client.getAddress() + "," + client.getTown() + "-" + client.getPostCode()+", "+DateTimeUtils.isNull(client.getCounty()) );
				ipdForm.setContact(client.getMobNo());
				ipdForm.setClient(clientid);

				ipdForm.setAgeonadmn(bed.getAgeonAdmn());
				UserProfile userProfile = userProfileDAO
						.getUserprofileDetails(Integer.parseInt(bed.getPractitionerid()));
				Master discipline = masterDAO.getDisciplineData(userProfile.getDiciplineName());
				ipdForm.setDiscipline(discipline.getDiscipline());
				ipdForm.setDoctor_name(
						userProfile.getInitial() + " " + userProfile.getFirstname() + " " + userProfile.getLastname());

				ipdForm.setQualification(DateTimeUtils.isNull(userProfile.getQualification()));

				ArrayList<Master> qualificationList= new ArrayList<Master>();
				
				for(String str:ipdForm.getQualification().split(",")){
					Master master= new Master();
					master.setName(str);
					qualificationList.add(master);
				}
				if(qualificationList.size()==0){
					Master master= new Master();
					master.setName(ipdForm.getQualification());
					qualificationList.add(master);
				}
				ipdForm.setQualificationList(qualificationList);
				
				String numcount = ipdDAO.getNumofAdmissionCount(clientid);
				ipdForm.setNum_admission(numcount);

				ipdForm.setKunal_final_diagnosis_text(bed.getKunal_final_diagnosis_text());
				ipdForm.setKunal_manual_medicine_text(bed.getKunal_manual_medicine_text());

				// get prescription list
				ArrayList<Priscription> dischargePriscList = ipdDAO.getDischargePrescList(selectedid);
				if (dischargePriscList.size() > 0) {
					Priscription pr = dischargePriscList.get(dischargePriscList.size() - 1);
					ipdForm.setStrengthflag(pr.isStrengthflag());
					ipdForm.setRemarkflag(pr.isRemarkflag());
					ipdForm.setQuantityflag(pr.isStrengthflag());
				}
				
				ArrayList<Priscription> treatmentivendischargePriscList = ipdDAO.getTreatmentGivenDischargePrescList(selectedid);
				ipdForm.setTreatmentivendischargePriscListt(treatmentivendischargePriscList);
				session.setAttribute("treatmentivendischargePriscList", treatmentivendischargePriscList);
				
				
				
				String discadvoice = ipdDAO.getDIscPrisc(selectedid);

				ipdForm.setAdvoice(discadvoice);

				ipdForm.setDischargePriscList(dischargePriscList);
				session.setAttribute("dischargePriscList", dischargePriscList);

				Bed bed1 = ipdDAO.getDischargeData(bed.getTreatmentepisodeid());
				ipdForm.setChkDischarge(bed1.isChkDischarge());

				ipdForm.setEmergencydetail(bed1.getEmergencydetail());
				ipdForm.setHospitalcourse(bed1.getHospitalcourse());
				ipdForm.setHospitalcourse(bed1.getHospitalcourse());
				if (ipdForm.getHospitalcourse() != null) {
					if (ipdForm.getHospitalcourse().equals("") || ipdForm.getHospitalcourse().equals("<br>")) {
						ipdForm.setHospitalcourse(null);
					}
				}

				ipdForm.setDiscadvnotes(bed1.getDiscadvnotes());
				if (ipdForm.getDiscadvnotes() != null) {
					if (ipdForm.getDiscadvnotes().equals("") || ipdForm.getDiscadvnotes().equals("<br>")) {
						ipdForm.setDiscadvnotes(null);
					}
				}

				ipdForm.setExample(bed1.getExample());

				DischargeOutcomeDAO dao = new JDBCDischargeOutcomeDAO(connection);

				String discdate = "";
				if (bed1.getDischargedate() != null) {
					if (!bed1.getDischargedate().equals("")) {

						String temp[] = bed1.getDischargedate().split(" ");
						discdate = DateTimeUtils.getCommencingDate1(temp[0]) + " " + temp[1];
						if (loginInfo.isBalgopal()) {
							String time[] = temp[1].split(":");
							int hourOfDay = (Integer.parseInt(time[0]));
							int minute = (Integer.parseInt(time[1]));
							String apmpm = ((hourOfDay > 12) ? hourOfDay % 12 : hourOfDay) + ":"
									+ (minute < 10 ? ("0" + minute) : minute) + " " + ((hourOfDay >= 12) ? "PM" : "AM");
							discdate = DateTimeUtils.getCommencingDate1(temp[0]) + " " + apmpm;
						}

					}
				}

				ipdForm.setDischargedate(discdate);

				session.setAttribute("dischargeddata", ipdForm);

				Bed bedconditions = ipdDAO.getAllFinalCondition(selectedid, bed.getTreatmentepisodeid());

				ArrayList<Bed> finalConditions = new ArrayList<Bed>();

				if (bedconditions.getConditionname() != null) {

					for (String str : bedconditions.getConditionname().split(",")) {

						if (str.equals("0")) {

							continue;
						}

						int id = Integer.parseInt(str);
						String conditionname = bedDao.getIpdConditionName(str);
						Bed bed2 = new Bed();
						bed2.setId(id);
						bed2.setConditionname(conditionname);
						finalConditions.add(bed2);

					}

				}

				ipdForm.setFinalConditions(finalConditions);

				printformdata(selectedid);
				ipdForm.setDaycare(bedDao.isDayCare(selectedid));
				NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
				// String anethesia_name =
				// notAvailableSlotDAO.getDiaryUserName(bed1.getAnesthesia());
				/*
				 * String surgeon_name =
				 * notAvailableSlotDAO.getDiaryUserName(bed1.getSurgeon());
				 * String anesthesiologist_name =
				 * notAvailableSlotDAO.getDiaryUserName(bed1.getAnesthesiologist
				 * ());
				 */
				// String surgeon_name =
				// userProfileDAO.getReferalDrName(bed1.getSurgeon());
				String surgeon_name = notAvailableSlotDAO.getDiaryUserName(bed1.getSurgeon());
				String anesthesiologist_name = userProfileDAO.getReferalDrName(bed1.getAnesthesiologist());
				String anethesia_name = null;
				if (bed1.getAnesthesia() != null) {
					if (!bed1.getAnesthesia().equals("")) {
						if (bed1.getAnesthesia().equals("1")) {
							anethesia_name = "Local";
						} else if (bed1.getAnesthesia().equals("2")) {
							anethesia_name = "General";
						} else if (bed1.getAnesthesia().equals("3")) {
							anethesia_name = "Spinal";
						}
					}
				}
				ipdForm.setAnesthesia(anethesia_name);
				ipdForm.setSurgeon(surgeon_name);
				ipdForm.setAnesthesiologist(anesthesiologist_name);

				if (bed1.getEmergencydetail() != null) {
					if (bed1.getEmergencydetail().equals("") || bed1.getEmergencydetail().equals("<br>")) {
						ipdForm.setEmergencydetail(null);
					} else {
						ipdForm.setEmergencydetail(bed1.getEmergencydetail());
					}
				} else {
					ipdForm.setEmergencydetail(bed1.getEmergencydetail());
				}

				if (ipdForm.getAppointmentText() != null) {

					if (ipdForm.getAppointmentText().equals("") || ipdForm.getAppointmentText().equals("<br>")
							|| ipdForm.getAppointmentText().equals("0")) {
						ipdForm.setAppointmentText(null);
						/*
						 * Ipd ipd = ipdDAO.getProcedureName(selectedid);
						 * 
						 * String procedureid =
						 * ipdDAO.getProcedureId(ipd.getProcedurename());
						 * ipdForm.setAppointmentText(ipd.getProcedurename());
						 */

					}

				} /*
					 * else{ Ipd ipd = ipdDAO.getProcedureName(selectedid);
					 * 
					 * String procedureid =
					 * ipdDAO.getProcedureId(ipd.getProcedurename());
					 * ipdForm.setAppointmentText(ipd.getProcedurename()); }
					 */
				if (ipdForm.getSurgeon() != null) {
					if (ipdForm.getSurgeon().equals("") || ipdForm.getSurgeon().equals("<br>")
							|| ipdForm.getSurgeon().equals("0")) {
						ipdForm.setSurgeon(null);
					}

				}
				if (ipdForm.getAnesthesia() != null) {
					if (ipdForm.getAnesthesia().equals("") || ipdForm.getAnesthesia().equals("<br>")
							|| ipdForm.getAnesthesia().equals("0")) {
						ipdForm.setAnesthesia(null);
					}
				}
				if (ipdForm.getAnesthesiologist() != null) {
					if (ipdForm.getAnesthesiologist().equals("") || ipdForm.getAnesthesiologist().equals("<br>")
							|| ipdForm.getAnesthesiologist().equals("0")) {
						ipdForm.setAnesthesiologist(null);
					}
				}
				if (userProfile.getMobile() == "0" || userProfile.getMobile().equals("0")) {
					ipdForm.setDoctor_phone(null);
				} else {
					ipdForm.setDoctor_phone(userProfile.getMobile());
				}

				if (bed1.getDischrgeOutcomes() != null) {
					Master master = dao.getMaster(Integer.parseInt(bed1.getDischrgeOutcomes()));
					if (master.getName() != null) {
						ipdForm.setDischrgeOutcomes(master.getName());
					} else {
						ipdForm.setDischrgeOutcomes("");
					}

					int selectedid1 = loginInfo.getId();

					ClinicDAO clinicListDAO = new JDBCClinicDAO(connection);
					com.apm.Registration.eu.entity.Clinic cliniclist = clinicListDAO.getCliniclistDetails(selectedid1);
					ipdForm.setClinicName(cliniclist.getClinicName());

					DischargeStatusDAO ddao = new JDBCDischargeStatus(connection);
					master = ddao.getMaster(Integer.parseInt(bed1.getDischargeStatus()));

					ipdForm.setDischargeStatusId(bed1.getDischargeStatus());

					if (master.getName() != null) {
						ipdForm.setDischargeStatus(master.getName());
					} else {
						ipdForm.setDischargeStatus("");
					}
					DischargeStatusDAO statusDAO = new JDBCDischargeStatus(connection);
					String dischargehead = statusDAO
							.getDischargeStatusById(Integer.parseInt(bed1.getDischargeStatus()));
					if (dischargehead == null) {
						dischargehead = "";
					}
					ipdForm.setDischargehead(dischargehead);
				}
				ArrayList<String> otaptid = ipdDAO.getAllOTIds(selectedid, clientid);

				// Adarsh
				/*
				 * ArrayList<Master> otnoteslist =new ArrayList<Master>(); for
				 * (String string : otaptid) { ArrayList<Master> otnoteslist1 =
				 * ipdDAO.getAllOtNotes(string);
				 * otnoteslist.addAll(otnoteslist1); }
				 * ipdForm.setOtnoteslist(otnoteslist);
				 */

				ArrayList<Master> otdatesandids = ipdDAO.getOtDatesAndIds(selectedid, clientid);
				ipdForm.setOtdatesandids(otdatesandids);
				String clinicname = userProfile.getClinicname();
				ipdForm.setClinicName(userProfile.getClinicname());

			} else {
				addActionError("Please Select Client!!");
			}

			Clinic clinic = new Clinic();
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);

			ArrayList<Clinic> locationAdressList = accountsDAO.getLocationAddress(loginInfo.getClinicid());

			clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
			ipdForm.setClinicName(clinic.getClinicName());
			ipdForm.setClinicOwner(clinic.getClinicOwner());
			ipdForm.setOwner_qualification(clinic.getOwner_qualification());
			ipdForm.setLocationAdressList(locationAdressList);
			ipdForm.setAddress(clinic.getAddress());
			ipdForm.setLandLine(clinic.getLandLine());
			ipdForm.setClinicemail(clinic.getEmail());
			ipdForm.setWebsiteUrl(clinic.getWebsiteUrl());
			ipdForm.setClinicLogo(clinic.getUserImageFileName());
			BedDao bedDao=new JDBCBedDao(connection);
			
			if(loginInfo.isNew_aureus_discard()){
				
						
				return "print_aureus_dcard";
			}
		}

		
		catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			connection.close();
		}
		return "printdischarge";
	}

	public void printformdata(String selectedid) throws SQLException {

		Connection connection = null;

		try {

			connection = Connection_provider.getconnection();
			// String selectedid = request.getParameter("selectedid");
			ipdForm.setClientip(selectedid);
			ipdForm.setJsonipdid(selectedid);
			BedDao bedDao = new JDBCBedDao(connection);
			IpdDAO ipdDAO = new JDBCIpdDAO(connection);
			Bed bed = bedDao.getEditIpdData(selectedid);
			MasterDAO masterDAO = new JDBCMasterDAO(connection);
			IpdLogDAO ipdLogDAO = new JDBCIpdLogDAO(connection);
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);

			ipdForm.setLamadamareason(bed.getReasonlamadama());
			ipdForm.setCasualtyipd(bed.getAction());
			ipdForm.setDaycare(bedDao.isDayCare(selectedid));
			String dd[] = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ");
			String printedBy = loginInfo.getUserId() + " " + DateTimeUtils.getCommencingDate1(dd[0]) + " " + dd[1];
			ipdForm.setPrintedBy(printedBy);
			if (loginInfo.getIpd_abbr_access() == 1) {
				String newipdabbr = ipdDAO.getIpdAbrivationIds(Integer.parseInt(selectedid));
				ipdForm.setNewipdabbr(newipdabbr);
				if (Integer.parseInt(bed.getIpdseqno()) > 0) {
					ipdForm.setIpdseqno(bed.getIpdseqno());
				} else {
					ipdForm.setIpdseqno(selectedid);
				}
			} else {
				if (Integer.parseInt(bed.getIpdseqno()) > 0) {
					ipdForm.setIpdseqno(bed.getIpdseqno());
					ipdForm.setNewipdabbr(bed.getIpdseqno());
				} else {
					ipdForm.setIpdseqno(selectedid);
					ipdForm.setNewipdabbr(selectedid);
				}
			}
			UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
			UserProfile userProfile = userProfileDAO.getUserprofileDetails(Integer.parseInt(bed.getPractitionerid()));
			String practitionername = userProfile.getInitial() + " " + userProfile.getFirstname() + " "
					+ userProfile.getLastname();
			String specializationId = userProfile.getDiciplineName();
			ipdForm.setDepartment(userProfile.getSpecialization());

			if (userProfile.getDoctor() != null) {

				if (!userProfile.getDoctor().equals("") && !userProfile.getDoctor().equals("0")) {

					UserProfile supportiveDoctorDetails = userProfileDAO
							.getUserprofileDetails(Integer.parseInt(userProfile.getDoctor()));
					String supportiveDoctorName = supportiveDoctorDetails.getInitial() + " "
							+ supportiveDoctorDetails.getFirstname() + " " + supportiveDoctorDetails.getLastname();
					String supportiveQualification = supportiveDoctorDetails.getQualification();
					ipdForm.setSupportiveDoctorName(supportiveDoctorName);
					ipdForm.setSupportiveQualification(supportiveQualification);
				}
			}

			boolean issystemicreview = masterDAO.isIpdFormFieldActive(specializationId, "Systemic Review");
			boolean history = masterDAO.isIpdFormFieldActive(specializationId, "History");
			boolean obstretic_history = masterDAO.isIpdFormFieldActive(specializationId, "OBSTRETIC HISTORY");
			boolean menstrual_history = masterDAO.isIpdFormFieldActive(specializationId, "MENSTRUAL HISTORY");
			boolean substance_history = masterDAO.isIpdFormFieldActive(specializationId, "SUBSTANCE HISTORY");
			boolean verification = masterDAO.isIpdFormFieldActive(specializationId, "Verification");
			boolean pediatric = masterDAO.isIpdFormFieldActive(specializationId, "Paediatric History");

			ipdForm.setNicuaccess(masterDAO.isIpdFormFieldActive(specializationId, "NICU Setting"));
			ipdForm.setNicuaccess(pediatric);
			ipdForm.setVerification(verification);
			ipdForm.setIssystemicreview(issystemicreview);
			ipdForm.setHistory(history);
			ipdForm.setObstretic_history(obstretic_history);
			ipdForm.setMenstrual_history(menstrual_history);
			ipdForm.setSubstance_history(substance_history);
			ipdForm.setPaediatrichist(pediatric);

			ipdForm.setEditclientid(bed.getClientid());
			ipdForm.setClientid(bed.getClientid());
			ipdForm.setPractitionerid(practitionername);
			ipdForm.setConditionid(bed.getConditionid());
			ipdForm.setDepartment(bed.getDepartment());
			ipdForm.setSecndryconsult(bed.getSecndryconsult());
			ipdForm.setAddmitedbyuserid(bed.getAddmitedbyuserid());

			NotAvailableSlot notAvailableSlot = notAvailableSlotDAO.getOTDataByIpd(selectedid);
			ipdForm.setProcedure(notAvailableSlot.getProcedure());
			// ipdForm.setAnesthesia(notAvailableSlot.getAnesthesia());
			// ipdForm.setSurgeon(notAvailableSlot.getSurgeon());
			ipdForm.setOtNotes(notAvailableSlot.getOtnotes());
			session.setAttribute("ipdotnotes", notAvailableSlot.getOtnotes());
			// ipdForm.setAppointmentText(notAvailableSlot.getApmttypetext());
			// ipdForm.setAnsintime(notAvailableSlot.getAnsintime());

			if (bed.getRefferedby() != null) {

				if (bed.getRefferedby().equals("") || bed.getRefferedby().equals("0")) {
					bed.setRefferedby(null);
				}
			}

			ipdForm.setRefferedby(bed.getRefferedby());

			String wardname = ipdDAO.getIpdWardName(bed.getWardid());
			String bedname = ipdDAO.getIpdBedName(bed.getBedid());

			ipdForm.setWardid(wardname);
			ipdForm.setBedid(bedname);

			ipdForm.setTpid(bed.getTpid());

			if (ipdForm.getTpid() == null) {

				ipdForm.setTpid("0");
			}

			ipdForm.setStatus(bed.getStatus());
			ipdForm.setAddmissionfor(bed.getAddmissionfor());
			ipdForm.setReimbursment(bed.getReimbursment());

			if (bed.getSecndryconsult() != null) {
				if (bed.getSecndryconsult().equals("0")) {
					bed.setSecndryconsult(null);
				}
			}

			if (bed.getSecndryconsult() != null) {
				if (!bed.getSecndryconsult().equals("")) {

					ArrayList<String> allConsultantList = ipdDAO.getAllSecondaryConsultList(selectedid);
					ArrayList<UserProfile> allconsultantlistwithdepart = ipdDAO.getSecConsWithDepartment(selectedid);
					ipdForm.setAllconsultantlistwithdepart(allconsultantlistwithdepart);
					ipdForm.setAllConsultantList(allConsultantList);
					bed.setAllConsultantList(allConsultantList);
				}
			} else {

				practitionername = "";
			}

			userProfile = userProfileDAO.getUserprofileDetails(Integer.parseInt(bed.getPractitionerid()));
			Master discipline = masterDAO.getDisciplineData(userProfile.getDiciplineName());
			ipdForm.setDiscipline(discipline.getDiscipline());
			ipdForm.setDoctor_name(
					userProfile.getInitial() + " " + userProfile.getFirstname() + " " + userProfile.getLastname());
			ipdForm.setDoctor_phone(userProfile.getMobile());
			ipdForm.setQualification(userProfile.getQualification());
			ipdForm.setUseregno(userProfile.getRegisterno());

			// Akash add mlc ref dr name
			String mlcdrname = userProfileDAO.getReferalDrName(bed.getMlcrefdoctor());
			ipdForm.setMlcrefdoctor(mlcdrname);

			ipdForm.setSecndryconsult(practitionername);
			ipdForm.setMlcno(bed.getMlcno());
			ipdForm.setAdmissiondetails(bed.getAdmissiondetails());
			String val = bed.getSuggestedtrtment();
			ipdForm.setSuggestedtrtment(bed.getSuggestedtrtment());
			ipdForm.setHosp(bed.getHosp());
			ipdForm.setPackagename(bed.getPackagename());
			// chiefcomplains, presentillness, pasthistory, personalhist,
			// familyhist, onexamination, treatmentepisodeid lokeshnew
			ipdForm.setChiefcomplains(bed.getChiefcomplains());
			ipdForm.setPresentillness(bed.getPresentillness());
			ipdForm.setPasthistory(bed.getPasthistory());
			ipdForm.setPersonalhist(bed.getPersonalhist());
			ipdForm.setFamilyhist(bed.getFamilyhist());
			ipdForm.setOnexamination(bed.getOnexamination());

			String treatmentname = bedDao.getTreatmentName(bed.getTreatmentepisodeid());

			ipdForm.setTreatmentepisodeid(treatmentname);

			ipdForm.setSuggestoper(bed.getSuggestoper());
			ipdForm.setSystdepartment(bed.getSystdepartment());
			ipdForm.setFpcondition(bed.getFpcondition());
			ipdForm.setFpnotest(bed.getFpnotest());
			ipdForm.setNauseacondition(bed.getNauseacondition());
			ipdForm.setNauseanotes(bed.getNauseanotes());
			ipdForm.setFnucondition(bed.getFnucondition());
			ipdForm.setFnunotes(bed.getFnunotes());
			ipdForm.setSmcondition(bed.getSmcondition());
			ipdForm.setSmnotes(bed.getSmnotes());
			ipdForm.setChestpaincond(bed.getChestpaincond());
			ipdForm.setChestpainnotes(bed.getChestpainnotes());
			ipdForm.setDimvisioncond(bed.getDimvisioncond());
			ipdForm.setDimvisionnotes(bed.getDimvisionnotes());

			// dimvisionnotes, hgucondition, hgunotes, hmcondition, hmnotes,
			// jointpaincond, jointpainnotes,
			ipdForm.setHgucondition(bed.getHgucondition());
			ipdForm.setHgunotes(bed.getHgunotes());
			ipdForm.setHmcondition(bed.getHmcondition());
			ipdForm.setHmnotes(bed.getHmnotes());
			ipdForm.setJointpaincond(bed.getJointpaincond());
			ipdForm.setJointpainnotes(bed.getJointpainnotes());

			// constipationcond, constpationnotes, specialnotes, edemafeetcondi,
			// edemafeetnotes, hematuriacondi,
			ipdForm.setConstipationcond(bed.getConstipationcond());
			ipdForm.setConstpationnotes(bed.getConstpationnotes());
			ipdForm.setSpecialnotes(bed.getSpecialnotes());
			ipdForm.setEdemafeetcondi(bed.getEdemafeetcondi());
			ipdForm.setEdemafeetnotes(bed.getEdemafeetnotes());
			ipdForm.setHematuriacondi(bed.getHematuriacondi());
			ipdForm.setHematurianotes(bed.getHematurianotes());

			// hematurianotes, bmcondition, bmnotes, oliguriacondi,
			// oligurianotes, pstgucondi, pstgunotes,
			ipdForm.setBmcondition(bed.getBmcondition());
			ipdForm.setBmnotes(bed.getBmnotes());
			ipdForm.setOliguriacondi(bed.getOliguriacondi());
			ipdForm.setOligurianotes(bed.getOligurianotes());
			ipdForm.setPstgucondi(bed.getPstgucondi());
			ipdForm.setPstgunotes(bed.getPstgunotes());

			// bmecondition, bmenotes, tnecondition, tnenotes, weaknesscondi,
			// weaknessnotes, ihcondition, ihnotes
			ipdForm.setBmecondition(bed.getBmecondition());
			ipdForm.setBmenotes(bed.getBmenotes());
			ipdForm.setTnecondition(bed.getTnecondition());
			ipdForm.setTnenotes(bed.getTnenotes());
			ipdForm.setWeaknesscondi(bed.getWeaknesscondi());
			ipdForm.setWeaknessnotes(bed.getWeaknessnotes());
			ipdForm.setIhcondition(bed.getIhcondition());
			ipdForm.setIhnotes(bed.getIhnotes());

			ipdForm.setAdmission_reason(bed.getAdmission_reason());
			ipdForm.setEarlierinvest(bed.getEarlierinvest());
			ipdForm.setAlergies(bed.getAlergies());

			ipdForm.setBirthhist(bed.getBirthhist());
			ipdForm.setDiethist(bed.getDiethist());
			ipdForm.setDevelopmenthist(bed.getDevelopmenthist());
			ipdForm.setEmmunizationhist(bed.getEmmunizationhist());
			// lokesh
			ipdForm.setHeadcircumference(bed.getHeadcircumference());
			ipdForm.setMidarmcircumference(bed.getMidarmcircumference());
			ipdForm.setLength(bed.getLength());
			ipdForm.setWtaddmission(bed.getWtaddmission());
			ipdForm.setWtdischarge(bed.getWtdischarge());

			// giddinesscondition,giddinessnotes,unconcondition,unconnotes
			ipdForm.setGiddinesscondition(bed.getGiddinesscondition());
			ipdForm.setGiddinessnotes(bed.getGiddinessnotes());
			ipdForm.setUnconcondition(bed.getUnconcondition());
			ipdForm.setUnconnotes(bed.getUnconnotes());

			/*
			 * if(!bed.getEmmunizationhist().equals("")||!bed.getBirthhist().
			 * equals("")||!bed.getDiethist().equals("")||!bed.
			 * getDevelopmenthist().equals("")){ bed.setPeditric(true); }
			 */

			ipdForm.setId(Integer.parseInt(selectedid));

			Bed bed1 = ipdDAO.getDischargeData(bed.getTreatmentepisodeid());
			ipdForm.setChkDischarge(bed1.isChkDischarge());
			ipdForm.setDischrgeOutcomes(bed1.getDischrgeOutcomes());
			ipdForm.setDischargeStatus(bed1.getDischargeStatus());
			ipdForm.setHospitalcourse(bed1.getHospitalcourse());
			// lokesh
			/*
			 * if(bed1.getHospitalcourse()!=null){
			 * if(bed1.getHospitalcourse().contains("<div>")){ String hscourse=
			 * bed1.getHospitalcourse(); hscourse=hscourse.replaceAll("<div>",
			 * ""); hscourse=hscourse.replaceAll("</div>", "");
			 * bed1.setHospitalcourse(hscourse); } }
			 */
			ipdForm.setDiscadvnotes(bed1.getDiscadvnotes());
			ipdForm.setTreatmentgiven(bed1.getTreatmentgiven());
			ipdForm.setFindondischarge(bed1.getFindondischarge());
			ipdForm.setInvestigation(bed1.getInvestigation());
			ipdForm.setOtNotes(bed1.getOtNotes());
			ipdForm.setAppointmentText(bed1.getAppointmentText());
			ipdForm.setAnesthesia(bed1.getAnesthesia());
			ipdForm.setSurgeon(bed1.getSurgeon());
			ipdForm.setAnesthesiologist(bed1.getAnesthesiologist());
			ipdForm.setDischargebyid(bed1.getDischargebyid());
			ipdForm.setSurgicalnotes(bed1.getSurgicalnotes());
			ipdForm.setMaternal_history(bed.getMaternal_history());
			ipdForm.setPerinatal_history(bed.getPerinatal_history());
			
			ipdForm.setDischargeEndedbyId(bedDao.getLastDischargeUserId(bed.getTreatmentepisodeid(), "endeduserid"));
			ipdForm.setDischargteLastUpdatedId(bedDao.getLastDischargeUserId(bed.getTreatmentepisodeid(), "lastformfilleruserid"));
			// peditric

			ipdForm.setEmergencydetail(bed1.getEmergencydetail());

			ipdForm.setDeathnote(bed1.getDeathnote());
			ipdForm.setTreatmenthistory(bed.getTreatmenthistory());

			if (ipdForm.getDeathnote() != null) {

				if (ipdForm.getDeathnote().equals("") || ipdForm.getDeathnote().equals("<br>")) {
					ipdForm.setDeathnote(null);
				}

			}

			if (ipdForm.getSurgicalnotes() != null) {

				if (ipdForm.getSurgicalnotes().equals("") || ipdForm.getSurgicalnotes().equals("<br>")) {
					ipdForm.setSurgicalnotes(null);
				}

			}

			if (ipdForm.getTreatmentgiven() != null) {

				if (ipdForm.getTreatmentgiven().equals("") || ipdForm.getTreatmentgiven().equals("<br>")) {
					ipdForm.setTreatmentgiven(null);
				}

			}
			if (ipdForm.getFindondischarge() != null) {

				if (ipdForm.getFindondischarge().equals("") || ipdForm.getFindondischarge().equals("<br>")) {
					ipdForm.setFindondischarge(null);
				}

			}
			if (ipdForm.getInvestigation() != null) {

				if (ipdForm.getInvestigation().equals("") || ipdForm.getInvestigation().equals("<br>")) {
					ipdForm.setInvestigation(null);
				}

			}
			if (ipdForm.getOtNotes() != null) {

				if (ipdForm.getOtNotes().equals("") || ipdForm.getOtNotes().equals("<br>")) {
					ipdForm.setOtNotes(null);
				}

			}
			//As per praful shinde requirement It has to bring complete text Date :29/11/2019 lokesh
			
			
			/*if (ipdForm.getAppointmentText() != null) {

				if (ipdForm.getAppointmentText().equals("") || ipdForm.getAppointmentText().equals("<br>")) {
					ipdForm.setAppointmentText(null);
				} else {
					String data1 = "";
					String[] data = ipdForm.getAppointmentText().split(",");
					for (int i = 0; i < data.length; i++) {
						if (i != 0) {
							if (data1.equals("")) {
								data1 = data[i];
							} else {
								data1 = data1 + data[i];
							}

						}
					}
					ipdForm.setAppointmentText(data1);
				}

			}*/
			if (ipdForm.getSurgeon() != null) {

				if (ipdForm.getSurgeon().equals("") || ipdForm.getSurgeon().equals("<br>")) {
					ipdForm.setSurgeon(null);
				}

			}
			if (ipdForm.getAnesthesia() != null) {

				if (ipdForm.getAnesthesia().equals("") || ipdForm.getAnesthesia().equals("<br>")) {
					ipdForm.setAnesthesia(null);
				}

			}
			if (ipdForm.getAnesthesiologist() != null) {

				if (ipdForm.getAnesthesiologist().equals("") || ipdForm.getAnesthesiologist().equals("<br>")) {
					ipdForm.setAnesthesiologist(null);
				}

			}

			ipdForm.setChkDischarge(bed1.isChkDischarge());
			ipdForm.setHospitalcourse(bed1.getHospitalcourse());
			ipdForm.setHospitalcourse(bed1.getHospitalcourse());
			if (ipdForm.getHospitalcourse() != null) {
				if (ipdForm.getHospitalcourse().equals("") || ipdForm.getHospitalcourse().equals("<br>")) {
					ipdForm.setHospitalcourse(null);
				}
			}

			ipdForm.setDiscadvnotes(bed1.getDiscadvnotes());
			if (ipdForm.getDiscadvnotes() != null) {
				if (ipdForm.getDiscadvnotes().equals("") || ipdForm.getDiscadvnotes().equals("<br>")) {
					ipdForm.setDiscadvnotes(null);
				}
			}
			ipdForm.setExample(bed1.getExample());
			if (ipdForm.getExample() != null) {

				if (ipdForm.getExample().equals("") || ipdForm.getExample().equals("<br>")) {
					ipdForm.setExample(null);
				}

			}

			// gynic details

			boolean issubstance = false;

			ipdForm.setAlcohal(bed.getAlcohal());
			ipdForm.setDrugs(bed.getDrugs());
			ipdForm.setOther_medication(bed.getOther_medication());
			ipdForm.setTobaco(bed.getTobaco());
			ipdForm.setTobaconotes(bed.getTobaconotes());
			ipdForm.setSmoking(bed.getSmoking());
			if (ipdForm.getAlcohal() != null) {

				if (ipdForm.getAlcohal().equals("") || ipdForm.getAlcohal().equals("No")) {
					ipdForm.setAlcohal(null);
				} else {
					issubstance = true;
				}
			}
			if (ipdForm.getDrugs() != null) {

				if (ipdForm.getDrugs().equals("") || ipdForm.getDrugs().equals("No")) {
					ipdForm.setDrugs(null);
				} else {
					issubstance = true;
				}
			}
			if (ipdForm.getOther_medication() != null) {

				if (ipdForm.getOther_medication().equals("") || ipdForm.getOther_medication().equals("No")) {
					ipdForm.setOther_medication(null);
				} else {
					issubstance = true;
				}
			}
			if (ipdForm.getTobaco() != null) {

				if (ipdForm.getTobaco().equals("") || ipdForm.getTobaco().equals("No")) {
					ipdForm.setTobaco(null);
				} else {
					issubstance = true;
				}
			}
			if (ipdForm.getTobaconotes() != null) {

				if (ipdForm.getTobaconotes().equals("") || ipdForm.getTobaconotes().equals("No")) {
					ipdForm.setTobaconotes(null);
				} else {
					issubstance = true;
				}
			}
			if (ipdForm.getSmoking() != null) {

				if (ipdForm.getSmoking().equals("") || ipdForm.getSmoking().equals("No")) {
					ipdForm.setSmoking(null);
				} else {
					issubstance = true;
				}
			}

			if (issubstance) {
				ipdForm.setSubstancehistory("");
			} else {
				ipdForm.setSubstancehistory(null);
			}

			boolean ismenstrual = false;

			ipdForm.setAge_menarche(bed.getAge_menarche());
			ipdForm.setLmp(bed.getLmp());
			ipdForm.setLlmp(bed.getLlmp());
			ipdForm.setPmc(bed.getPmc());

			if (ipdForm.getAge_menarche() != null) {
				if (ipdForm.getAge_menarche().equals("0") || ipdForm.getAge_menarche().equals("")) {
					ipdForm.setAge_menarche(null);
				} else {
					ismenstrual = true;
				}
			}
			if (ipdForm.getLmp() != null) {
				if (ipdForm.getLmp().equals("0") || ipdForm.getLmp().equals("")) {
					ipdForm.setLmp(null);
				} else {
					ismenstrual = true;
				}
			}
			if (ipdForm.getLlmp() != null) {
				if (ipdForm.getLlmp().equals("0") || ipdForm.getLlmp().equals("")) {
					ipdForm.setLlmp(null);
				} else {
					ismenstrual = true;
				}
			}
			if (ipdForm.getPmc() != null) {
				if (ipdForm.getPmc().equals("0") || ipdForm.getPmc().equals("")) {
					ipdForm.setPmc(null);
				} else {
					ismenstrual = true;
				}
			}

			ipdForm.setCycle_length(bed.getCycle_length());
			ipdForm.setDuration_flow(bed.getDuration_flow());
			ipdForm.setAmount_flow(bed.getAmount_flow());
			ipdForm.setDysmenorrhea(bed.getDysmenorrhea());
			ipdForm.setDyspareunia(bed.getDyspareunia());
			ipdForm.setHopassing_clots(bed.getHopassing_clots());
			if (ipdForm.getCycle_length() != null) {
				if (ipdForm.getCycle_length().equals("0") || ipdForm.getCycle_length().equals("")) {
					ipdForm.setCycle_length(null);
				} else {
					ismenstrual = true;
				}
			}
			if (ipdForm.getDuration_flow() != null) {
				if (ipdForm.getDuration_flow().equals("0") || ipdForm.getDuration_flow().equals("")) {
					ipdForm.setDuration_flow(null);
				} else {
					ismenstrual = true;
				}
			}
			if (ipdForm.getAmount_flow() != null) {
				if (ipdForm.getAmount_flow().equals("0") || ipdForm.getAmount_flow().equals("")) {
					ipdForm.setAmount_flow(null);
				} else {
					ismenstrual = true;
				}
			}
			if (ipdForm.getDysmenorrhea() != null) {
				if (ipdForm.getDysmenorrhea().equals("0") || ipdForm.getDysmenorrhea().equals("")) {
					ipdForm.setDysmenorrhea(null);
				} else {
					ismenstrual = true;
				}
			}
			if (ipdForm.getDyspareunia() != null) {
				if (ipdForm.getDyspareunia().equals("0") || ipdForm.getDyspareunia().equals("")) {
					ipdForm.setDyspareunia(null);
				} else {
					ismenstrual = true;
				}
			}
			if (ipdForm.getHopassing_clots() != null) {
				if (ipdForm.getHopassing_clots().equals("0") || ipdForm.getHopassing_clots().equals("")) {
					ipdForm.setHopassing_clots(null);
				} else {
					ismenstrual = true;
				}
			}

			ipdForm.setWhite_disc_itching(bed.getWhite_disc_itching());
			ipdForm.setIntercourse_freq(bed.getIntercourse_freq());
			ipdForm.setGalactorrea(bed.getGalactorrea());
			if (ipdForm.getWhite_disc_itching() != null) {
				if (ipdForm.getWhite_disc_itching().equals("0") || ipdForm.getWhite_disc_itching().equals("")) {
					ipdForm.setWhite_disc_itching(null);
				} else {
					ismenstrual = true;
				}
			}
			if (ipdForm.getIntercourse_freq() != null) {
				if (ipdForm.getIntercourse_freq().equals("0") || ipdForm.getIntercourse_freq().equals("")) {
					ipdForm.setIntercourse_freq(null);
				} else {
					ismenstrual = true;
				}
			}
			if (ipdForm.getGalactorrea() != null) {
				if (ipdForm.getGalactorrea().equals("0") || ipdForm.getGalactorrea().equals("")) {
					ipdForm.setGalactorrea(null);
				} else {
					ismenstrual = true;
				}
			}

			ipdForm.setHo_contraception(bed.getHo_contraception());
			ipdForm.setRubella_vaccine(bed.getRubella_vaccine());
			ipdForm.setMenopause(bed.getMenopause());
			if (ipdForm.getHo_contraception() != null) {
				if (ipdForm.getHo_contraception().equals("0") || ipdForm.getHo_contraception().equals("")) {
					ipdForm.setHo_contraception(null);
				} else {
					ismenstrual = true;
				}
			}
			if (ipdForm.getRubella_vaccine() != null) {
				if (ipdForm.getRubella_vaccine().equals("0") || ipdForm.getRubella_vaccine().equals("")) {
					ipdForm.setRubella_vaccine(null);
				} else {
					ismenstrual = true;
				}
			}
			if (ipdForm.getMenopause() != null) {
				if (ipdForm.getMenopause().equals("0") || ipdForm.getMenopause().equals("")) {
					ipdForm.setMenopause(null);
				} else {
					ismenstrual = true;
				}
			}

			if (ismenstrual) {
				ipdForm.setMenstraulhistory("");
			} else {
				ipdForm.setMenstraulhistory(null);
			}

			ipdForm.setLive_boys(bed.getLive_boys());
			ipdForm.setLive_girls(bed.getLive_girls());
			ipdForm.setStillbirths(bed.getStillbirths());
			ipdForm.setAbortions(bed.getAbortions());
			ipdForm.setDeath_children(bed.getDeath_children());

			ArrayList<Bed> gynicobsList = bedDao.getGynicObsList(selectedid);
			ipdForm.setGynicobsList(gynicobsList);

			// parity_aboration_notes,p,l,a,d
			ipdForm.setParity_abortion_notes(bed.getParity_abortion_notes());
			if (ipdForm.getParity_abortion_notes() != null) {

				if (ipdForm.getParity_abortion_notes().equals("") || ipdForm.getParity_abortion_notes().equals("0")) {
					ipdForm.setParity_abortion_notes("");
				}
			}
			ipdForm.setP(bed.getP());
			ipdForm.setL(bed.getL());
			ipdForm.setA(bed.getA());
			ipdForm.setD(bed.getD());

			boolean ishistory = false;

			if (bed.getAddmissionfor() != null) {

				if (bed.getAddmissionfor().equals("") || bed.getAddmissionfor().equals("<br>")) {
					bed.setAddmissionfor(null);
				}
			}
			if (bed.getAlergies() != null) {

				if (bed.getAlergies().equals("") || bed.getAlergies().equals("<br>")) {
					bed.setAlergies(null);
				}
			}
			if (bed.getPackagename() != null) {

				if (bed.getPackagename().equals("") || bed.getPackagename().equals("<br>")) {
					bed.setPackagename(null);
				}

			}
			if (bed.getAdmission_reason() != null) {

				if (bed.getAdmission_reason().equals("") || bed.getAdmission_reason().equals("<br>")) {
					bed.setAdmission_reason(null);
				}
			}
			if (bed.getChiefcomplains() != null) {

				if (bed.getChiefcomplains().equals("") || bed.getChiefcomplains().equals("<br>")) {
					bed.setChiefcomplains(null);
				}
			}
			if (bed.getPasthistory() != null) {

				if (bed.getPasthistory().equals("") || bed.getPasthistory().equals("<br>")) {
					bed.setPasthistory(null);
				} else {
					ishistory = true;
				}
			}
			if (bed.getFamilyhist() != null) {

				if (bed.getFamilyhist().equals("") || bed.getFamilyhist().equals("<br>")) {
					bed.setFamilyhist(null);
				} else {
					ishistory = true;
				}
			}
			if (bed.getPersonalhist() != null) {

				if (bed.getPersonalhist().equals("") || bed.getPersonalhist().equals("<br>")) {
					bed.setPersonalhist(null);
				} else {
					ishistory = true;
				}
			}
			if (bed.getOnexamination() != null) {

				if (bed.getOnexamination().equals("") || bed.getOnexamination().equals("<br>")) {
					bed.setOnexamination(null);
				} else {
					ishistory = true;
				}

			}
			if (bed.getSurgicalnotes() != null) {

				if (bed.getSurgicalnotes().equals("") || bed.getSurgicalnotes().equals("<br>")) {
					bed.setSurgicalnotes(null);
				} else {
					ishistory = true;
				}

			}

			// Akash 05 June 2018 to set content of History textbox data
			if (ipdForm.getExample() != null) {
				bed.setExample(ipdForm.getExample());
				ishistory = true;
			}

			if (bed.getSuggestedtrtment() != null) {

				if (bed.getSuggestedtrtment().equals("") || bed.getSuggestedtrtment().equals("<br>")) {
					bed.setSuggestedtrtment(null);
				} else {
				}
			}
			if (bed.getSpecialnotes() != null) {

				if (bed.getSpecialnotes().equals("") || bed.getSpecialnotes().equals("<br>")) {
					bed.setSpecialnotes(null);
				}

			}
			if (bed.getEarlierinvest() != null) {

				if (bed.getEarlierinvest().equals("") || bed.getEarlierinvest().equals("<br>")) {
					bed.setEarlierinvest(null);
				}
			}
			if (bed.getPresentillness() != null) {

				if (bed.getPresentillness().equals("") || bed.getPresentillness().equals("<br>")) {
					bed.setPresentillness(null);
				}
			}

			if (bed.getPresentillness() == null && bed.getChiefcomplains() == null
					&& bed.getAdmission_reason() == null) {

				bed.setSummary(null);
			} else {
				bed.setSummary("");
			}

			if (!ishistory) {
				bed.setHistory(null);
			} else {
				bed.setHistory("");
			}

			ipdForm.setSurgicalnotes(bed.getSurgicalnotes());
			// set treatment episode
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			Client client = clientDAO.getClientDetails(ipdForm.getClientid());
			String fullname = client.getTitle() + " " + client.getFirstName() + " " + client.getMiddlename() + " "
					+ client.getLastName();
			ipdForm.setClient(fullname);
			ipdForm.setRegno(client.getAbrivationid());
			String whopay = client.getWhopay();
			ipdForm.setAbrivationid(client.getAbrivationid());
			ipdForm.setPatientIdAbrivation(client.getPatientIdAbrivation());
			if (whopay == null) {
				whopay = "";
			}
			if (!whopay.equals("Client")) {
				ThirdPartyDAO thirdPartyDAO = new JDBCThirdPartyDAO(connection);
				ThirdParty thirdParty = thirdPartyDAO.getThirdPartyDetails(bed.getTpid());
				ipdForm.setThirdParty(thirdParty.getCompanyName());
			} else {
				ipdForm.setThirdParty("Self");
			}
			String birthtime=client.getBirthtime();
			 String hh1="";
			 String mm1="";
			 String apmpm1="";
			 if(!birthtime.equals("00:00:00")){
				 String time[] = birthtime.split(":");
				 hh1 = time[0];
				 mm1 = time[1];
				 int hourOfDay1=Integer.parseInt(hh1);
				 int minute1=Integer.parseInt(mm1);
				 apmpm1 =  ((hourOfDay1 > 12) ? hourOfDay1 % 12 : hourOfDay1) + ":" + (minute1 < 10 ? ("0" + minute1) : minute1) + " " + ((hourOfDay1 >= 12) ? "PM" : "AM");	 
			 }
			// lokesh
			String agegender = "";
			String dob = client.getDob();
			String age = DateTimeUtils.getAge1(client.getDob());
			/*
			 * String age1[]= age.split("(?<=\\D)(?=\\d)|(?<=\\d)(?=\\D)"); age=
			 * age1[0]; if(Integer.parseInt(age)<2){
			 * if(Integer.parseInt(age)<1){ String monthdays=
			 * DateTimeUtils.getMonthandDays(client.getDob());
			 * agegender=monthdays+" / "+client.getGender(); }else{ String
			 * monthdays= DateTimeUtils.getMonthandDays(client.getDob());
			 * agegender= age + " Years" +
			 * " "+monthdays+" / "+client.getGender(); } } else { agegender =
			 * age + "Years" + " / " + client.getGender(); }
			 */

			if(!birthtime.equals("00:00:00")){
				agegender = age +"("+apmpm1+")"+ " / " + client.getGender();
			}else{
			agegender = age + " / " + client.getGender();	
			}
			ipdForm.setAge(age);
			ipdForm.setAgegender(agegender);
			ipdForm.setRelativename(client.getEmergencyContName());
			ipdForm.setRelationcont(client.getEmergencyContNo());
			ipdForm.setRelation(client.getRelation());

			boolean isfamilyd = false;

			if (ipdForm.getRelativename() != null) {

				if (ipdForm.getRelativename().equals("")) {

					ipdForm.setRelativename(null);
				}
			}

			if (ipdForm.getRelationcont() != null) {

				if (ipdForm.getRelationcont().equals("")) {

					ipdForm.setRelationcont(null);
				}
			}

			if (ipdForm.getRelation() != null) {

				if (ipdForm.getRelation().equals("")) {

					ipdForm.setRelation(null);
				}
			}

			if (ipdForm.getRelativename() == null) {
				isfamilyd = true;
			}
			if (ipdForm.getRelationcont() == null) {
				isfamilyd = true;
			}
			if (ipdForm.getRelation() == null) {
				isfamilyd = true;
			}

			if (isfamilyd) {
				ipdForm.setFamilyDetails("");
			} else {
				ipdForm.setFamilyDetails("ee");
			}

			String numcount = ipdDAO.getNumofAdmissionCount(ipdForm.getClientid());
			ipdForm.setNum_admission(numcount);
			ipdForm.setDob(client.getDob());
			ipdForm.setAddress(client.getAddress() + ", " + client.getTown() + "-" + client.getPostCode());
			ipdForm.setContact(client.getMobNo());

			ArrayList<Bed> bedLogList = ipdLogDAO.getBedChangeLogList(bed.getClientid(), selectedid);
			ipdForm.setBedLogList(bedLogList);

			if (bedLogList.size() > 0) {
				bedLogList.get(0).setStatus("1");
			}

			String payby = client.getWhopay();
			if (client.getWhopay().equals("Self")) {
				payby = "Client";
			}
			TreatmentEpisodeDAO treatmentEpisodeDAO = new JDBCTreatmentEpisode(connection);
			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
			ArrayList<TreatmentEpisode> treatmentEpisodeList = treatmentEpisodeDAO
					.getIpdTreatmentEpisodeList(ipdForm.getClientid(), payby);
			ipdForm.setTreatmentEpisodeList(treatmentEpisodeList);

			ipdForm.setTreatmentEpisode(bed.getTreatmentepisodeid());
			String admissiondate = bed.getAdmissiondate();
			String[] data = admissiondate.split(" ");
			String data2 = DateTimeUtils.getCommencingDate1(data[0]);
			String data3 = data2 + " " + data[1];
			// ipdForm.setAdmissiondate(bed.getAdmissiondate());
			if (loginInfo.isBalgopal()) {
				String time[] = data[1].split(":");
				int hourOfDay = (Integer.parseInt(time[0]));
				int minute = (Integer.parseInt(time[1]));
				String apmpm = ((hourOfDay > 12) ? hourOfDay % 12 : hourOfDay) + ":"
						+ (minute < 10 ? ("0" + minute) : minute) + " " + ((hourOfDay >= 12) ? "PM" : "AM");
				data3 = DateTimeUtils.getCommencingDate1(data[0]) + " " + apmpm;
			}
			ipdForm.setAdmissiondate(data3);

			/*
			 * Collection<Bed>
			 * conditions=bedDao.getConditionList(bed.getConditionid());
			 * ipdForm.setConditions(conditions);
			 */

			Clinic clinic = clinicDAO.getCliniclistDetails(loginInfo.getClinicid());
			ipdForm.setClinicLogo(clinic.getUserImageFileName());

			String treatmentName = bedDao.getTreatmentName(bed.getTreatmentepisodeid());
			ipdForm.setTreatmentEpisode(treatmentName);
			ArrayList<Bed> ipdConditionids = bedDao.getIpdConditionList(selectedid);

			ArrayList<String> ipdconditionnames = bedDao.getConditionNameList(ipdConditionids);

			ArrayList<Client> ipdCondtitionList = clientDAO.getTreatmentTypeList();
			session.setAttribute("ipdConditionids", ipdConditionids);
			session.setAttribute("ipdCondtitionList", ipdconditionnames);
			session.setAttribute("bed", bed);

			ipdForm.setMlccase(bed.getMlccase());
			/*
			 * if(bed.getMlccase().equals("1")){
			 * 
			 * }
			 */
			
			ipdForm.setFathername(client.getFathername());
			ipdForm.setMothername(client.getMothername());
			ipdForm.setBirthplace(client.getBirthplace());
			
			if (ipdForm.getDischargedate() != null) {
				if (!ipdForm.getDischargedate().equals("")) {
					String c = ipdForm.getDischargedate();
					String h[] = c.split(" ");
					c = DateTimeUtils.getCommencingDate1(h[0]);
					String d = DateTimeUtils.getAge1onAddmission(ipdForm.getDob(), c);
					String w[] = ipdForm.getAgegender().split("/");

					ipdForm.setAgegender(d + "/" + w[1]);
					ipdForm.setAge(d);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String discharge() throws SQLException {
		if (!verifyLogin(request)) {
			return "login";
		}

		try {
			session.removeAttribute("finalConditions");
			if (session.getAttribute("openedb") == null) {
				session.setAttribute("openedb", "ipd");
			}

			String selectedid = request.getParameter("selectedid");
			getipddatadischarge(selectedid);
			
			if(loginInfo.isNew_aureus_discard()){
				return "new_aureus_discard";
			}
			

		}
		
		catch (Exception e) {
			e.printStackTrace();
		}

		return "discharge";
	}

	public void getipddatadischarge(String selectedid) {

		Connection connection = null;

		try {
			session.removeAttribute("finalConditions");
			if (session.getAttribute("openedb") == null) {
				session.setAttribute("openedb", "ipd");
			}

	/*		pointer*/
			connection = Connection_provider.getconnection();
			IpdDAO ipdDAO = new JDBCIpdDAO(connection);
			BedDao bedDao = new JDBCBedDao(connection);
			IpdLogDAO ipdLogDAO = new JDBCIpdLogDAO(connection);
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			MasterDAO masterDAO = new JDBCMasterDAO(connection);
			UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
			DiaryManagementDAO diaryManagementDAO = new JDBCDiaryManagentDAO(connection);
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			ipdForm.setDaycare(bedDao.isDayCare(selectedid));
			/*
			 * UserProfileDAO userProfileDAO = new
			 * JDBCUserProfileDAO(connection);
			 */

			// prepare data of IPDACTION LOKESH
			listOfExtraData();

			// for procedure
			Ipd ipd = ipdDAO.getProcedureName(selectedid);

			Bed bed = bedDao.getEditIpdData(selectedid);
			
			
			//for new Aureus Discharge Card
			
			if(!bedDao.entryExistsInNewDischargeFields(selectedid)){
				bedDao.makeEntryToNewDischargeFields(bed.getClientid(), selectedid, bed.getTreatmentepisodeid());
			}
			
			
			
			/*Bed newDischargeFields= */
			if(loginInfo.isNew_aureus_discard()){
				request.setAttribute("newDischargeFields", bedDao.newDischargeCardDetails(selectedid));
				ipdForm.setNewCardFields( bedDao.newDischargeCardDetails(selectedid));
				ArrayList<Master> vitalList=masterDAO.getallVitalMasterdata("1");
				for (Master master : vitalList) {
					master.setFinding(masterDAO.getdischargeVitalVal(""+master.getId(), selectedid));
				}
				ipdForm.setVitalList(vitalList);
				request.setAttribute("vitallist", vitalList);
				
			}
			
			
			
			ipdForm.setLamadamareason(bed.getReasonlamadama());
			ipdForm.setGstureage(bed.getGstureage());
			ipdForm.setWtonbirth(bed.getWtonbirth());
			session.setAttribute("sessionadmissionid", selectedid);
			String parentpriscid = ipdDAO.getParentPriscId(selectedid);
			ipdForm.setParentpriscid(parentpriscid);

			String procedureid = ipdDAO.getProcedureId(ipd.getProcedurename());
			ipdForm.setAppointmentText(procedureid);

			ipdForm.setNewadmndate(bed.getNewadmndate());
			ArrayList<Master> procedureList = notAvailableSlotDAO.getProcedureList(bed.getSpeciality());
			if (procedureList.size() == 0) {
				procedureList = new ArrayList<Master>();
			}

			ipdForm.setProcedureList(procedureList);

			if (loginInfo.getIpd_abbr_access() == 1) {
				String newipdabbr = ipdDAO.getIpdAbrivationIds(Integer.parseInt(selectedid));
				ipdForm.setNewipdabbr(newipdabbr);
				if (Integer.parseInt(bed.getIpdseqno()) > 0) {
					ipdForm.setIpdseqno(bed.getIpdseqno());
				} else {
					ipdForm.setIpdseqno(selectedid);
				}
			} else {
				if (Integer.parseInt(bed.getIpdseqno()) > 0) {
					ipdForm.setIpdseqno(bed.getIpdseqno());
					ipdForm.setNewipdabbr(bed.getIpdseqno());
				} else {
					ipdForm.setIpdseqno(selectedid);
					ipdForm.setNewipdabbr(selectedid);
				}
			}

			Client client = clientDAO.getClientDetails(bed.getClientid());

			UserProfile userProfile = userProfileDAO.getUserprofileDetails(Integer.parseInt(bed.getPractitionerid()));
			Master discipline = masterDAO.getDisciplineData(userProfile.getDiciplineName());
			ipdForm.setDiscipline(discipline.getDiscipline());
			ipdForm.setDoctor_name(
					userProfile.getInitial() + " " + userProfile.getFirstname() + " " + userProfile.getLastname());
			ipdForm.setDoctor_phone(userProfile.getMobile());

			String discharge_default_id = masterDAO.getIpdTemplateId("Discharge Default");
			String hospital_course_id = masterDAO.getIpdTemplateId("Hospital Course");
			String nursing_advice_id = masterDAO.getIpdTemplateId("Nursing Advice");
			String operative_notes_id = masterDAO.getIpdTemplateId("Operative Notes");
			String invenstigations = masterDAO.getIpdTemplateId("Investigations");
			String finding_on_discharge = masterDAO.getIpdTemplateId("FINDING ON DISCHARGE");
			String treatment_given_id = masterDAO.getIpdTemplateId("Discharge Treatment Given");
			String emergencydetailsid = masterDAO.getIpdTemplateId("Emergency Details");
			String treatmentgiventemplateid = masterDAO.getIpdTemplateId("Treatment Given");
			String onexami_id = masterDAO.getIpdTemplateId("On Examination");
			ArrayList<Bed> bedLogList = ipdLogDAO.getBedChangeLogList(bed.getClientid(), selectedid);
			ipdForm.setBedLogList(bedLogList);

			ipdForm.setCommonTemplateList(ipdDAO.commonTemplateList());
			
			
			String specializationId=  userProfile.getDiciplineName();
			ipdForm.setNicuaccess(masterDAO.isIpdFormFieldActive(specializationId, "NICU Setting"));
			boolean peditric = masterDAO.isIpdFormFieldActive(specializationId, "Paediatric History");
			ipdForm.setNicuaccess(peditric);
			// int size=bedLogList.size()-1;
			if (bedLogList.size() > 0) {
				bedLogList.get(0).setStatus("1");
			}

			ArrayList<Master> discharge_default_list = masterDAO.getIpdTemplateListNames(discharge_default_id);
			ArrayList<Master> hospital_course_list = masterDAO.getIpdTemplateListNames(hospital_course_id);
			ArrayList<Master> nursing_advice_list = masterDAO.getIpdTemplateListNames(nursing_advice_id);
			ArrayList<Master> operativeList = masterDAO.getIpdTemplateListNames(operative_notes_id);
			ArrayList<Master> investigationList = masterDAO.getIpdTemplateListNames(invenstigations);
			ArrayList<Master> finding_on_dischargeList = masterDAO.getIpdTemplateListNames(finding_on_discharge);
			ArrayList<Master> treatment_given_list = masterDAO.getIpdTemplateListNames(treatment_given_id);
			ArrayList<Master> emergencydetailslist = masterDAO.getIpdTemplateListNames(emergencydetailsid);
			ArrayList<Master> treatmentgiventemplist = masterDAO.getIpdTemplateListNames(treatmentgiventemplateid);
			ArrayList<Master> on_exam_list = masterDAO.getIpdTemplateListNames(onexami_id);
			
			
			String maternalHisttempid=masterDAO.getIpdTemplateId("Maternal History");
			String perinatalHistroy=masterDAO.getIpdTemplateId("Perinatal History");
			ArrayList<Master> perintal_hisry_list=masterDAO.getIpdTemplateListNames(perinatalHistroy);
			ArrayList<Master> maternal_histry_list=masterDAO.getIpdTemplateListNames(maternalHisttempid);
			ipdForm.setPerintal_hisry_list(perintal_hisry_list);
			ipdForm.setMaternal_histry_list(maternal_histry_list);
			
			
			ipdForm.setOperativeList(operativeList);
			ipdForm.setDischarge_default_list(discharge_default_list);
			ipdForm.setHospital_course_list(hospital_course_list);
			ipdForm.setNursing_advice_list(nursing_advice_list);
			ipdForm.setInvestigationList(investigationList);
			ipdForm.setFinding_on_dischargeList(finding_on_dischargeList);
			ipdForm.setTreatment_given_list(treatment_given_list);
			ipdForm.setEmergencydetailslist(emergencydetailslist);
			ipdForm.setTreatmentgiventemplatelist(treatmentgiventemplist);
			ipdForm.setOn_exam_list(on_exam_list);
			
			// Old surgical list from apm_other_template table //Akash 11 dec
			// 2017
			/*
			 * UserProfile userProfile2 =
			 * userProfileDAO.getUserprofileDetails(loginInfo.getId());
			 * ArrayList<Master> otherTemplateList =
			 * masterDAO.getEmrTemplateList(userProfile2.getDiciplineName());
			 * ipdForm.setOtherTemplateList(otherTemplateList);
			 */

			InvestigationDAO investigationDAO = new JDBCInvestigationDAO(connection);
			ArrayList<InvstTemplate> templateList = investigationDAO.getTemplateList();
			ipdForm.setInvestigationtemplatelist(templateList);

			// New from ipd_template table //the surgical notes
			String surgical_template = masterDAO.getIpdTemplateId("OT Template");
			ArrayList<Master> otherTemplateList = masterDAO.getIpdTemplateListNames(surgical_template);
			ipdForm.setOtherTemplateList(otherTemplateList);

			double balance = 0;
			
			Client clientData = clientDAO.getClientDetails(bed.getClientid());
			ipdForm.setContact(clientData.getMobNo());
			ipdForm.setAddress(clientData.getAddress());
			String numcount = ipdDAO.getNumofAdmissionCount(bed.getClientid());
			ipdForm.setNum_admission(numcount);

			if (client.getWhopay().equals(Constants.PAY_BY_CLIENT)) {
				double debit = diaryManagementDAO.getClientDebitTotal(bed.getClientid());
				double payment = diaryManagementDAO.getClientPayment(bed.getClientid());

				balance = debit - payment;

				System.out.println(balance);

				ipdForm.setWhopay("Self");
			} else {

				ThirdParty thirdParty = client.getTpDetails();
				ipdForm.setWhopay(thirdParty.getCompanyName());

			}

			ipdForm.setBalance(balance);

			// get prescription list
			ArrayList<Priscription> dischargePriscList = ipdDAO.getDischargePrescList(selectedid);
			ipdForm.setDischargePriscList(dischargePriscList);
			session.setAttribute("priscList", dischargePriscList);

			ArrayList<Priscription> treatmentivendischargePriscList = ipdDAO.getTreatmentGivenDischargePrescList(selectedid);
			ipdForm.setTreatmentivendischargePriscListt(treatmentivendischargePriscList);
			int size = dischargePriscList.size();
			if (size > 0) {
				String totalchildmedids = dischargePriscList.get(size - 1).getTotalchildmedids();
				ipdForm.setTotalchildmedids(totalchildmedids);
			} else {
				ipdForm.setTotalchildmedids("0");
			}

			Bed bed1 = ipdDAO.getDischargeData(bed.getTreatmentepisodeid());
			ipdForm.setChkDischarge(bed1.isChkDischarge());
			ipdForm.setDischrgeOutcomes(bed1.getDischrgeOutcomes());

			ipdForm.setDischargeStatus(bed1.getDischargeStatus());
			ipdForm.setHospitalcourse(bed1.getHospitalcourse());
			ipdForm.setDiscadvnotes(bed1.getDiscadvnotes());
			ipdForm.setExample(bed1.getExample());
			ipdForm.setFindondischarge(bed1.getFindondischarge());
			ipdForm.setTreatmentgiven(bed1.getTreatmentgiven());
			ipdForm.setInvestigation(bed1.getInvestigation());
			ipdForm.setOtNotes(bed1.getOtNotes());

			ipdForm.setDeathnote(bed1.getDeathnote());

			ipdForm.setAppointmentText(bed1.getAppointmentText());
			ipdForm.setAnesthesia(bed1.getAnesthesia());
			ipdForm.setSurgeon(bed1.getSurgeon());
			ipdForm.setAnesthesiologist(bed1.getAnesthesiologist());

			if (bed1.getDischargedate() != null) {
				if (!bed1.getDischargedate().equals("")) {
					String temp[] = bed1.getDischargedate().split(" ");
					String date = DateTimeUtils.getCommencingDate1(temp[0]);
					ipdForm.setDischargedate(date);
					String time[] = temp[1].split(":");
					String hh = time[0];
					String mm = time[1];
					ipdForm.setHour(hh);
					ipdForm.setMinute(mm);
				}
			}

			printformdata(selectedid);

			ipdForm.setClientid(bed.getClientid());
			ipdForm.setPractitionerid(bed.getPractitionerid());
			ipdForm.setConditionid(bed.getConditionid());

			Bed bedconditions = ipdDAO.getAllFinalCondition(selectedid, bed.getTreatmentepisodeid());

			ArrayList<Bed> finalConditions = new ArrayList<Bed>();

			if (bedconditions.getConditionname() != null) {

				for (String str : bedconditions.getConditionname().split(",")) {

					if (str == null) {
						continue;
					}
					if (str.equals("0")) {

						continue;
					}
					int id = Integer.parseInt(str);
					String conditionname = bedDao.getIpdConditionName(str);
					Bed bed2 = new Bed();
					bed2.setId(id);
					bed2.setConditionname(conditionname);
					bed2.setConditionid(str);
					finalConditions.add(bed2);

				}
			}
			ipdForm.setAbrivationid(client.getAbrivationid());

			ipdForm.setFinalConditions(finalConditions);

			ArrayList<Client> ipdCondtitionList = new ArrayList<Client>(); // clientDAO.getEmrTreatmentTypeList();
			session.setAttribute("finalConditions", finalConditions);
			session.setAttribute("ipdCondtitionmaster", ipdCondtitionList);

			ArrayList<Bed> ipdconditionlist = bedDao.getIpdConditionList(selectedid);
			ipdForm.setIpdconditionlist(ipdconditionlist);

			int selectedid1 = loginInfo.getId();

			ClinicDAO clinicListDAO = new JDBCClinicDAO(connection);
			com.apm.Registration.eu.entity.Clinic cliniclist = clinicListDAO.getCliniclistDetails(selectedid1);
			ipdForm.setClinicName(cliniclist.getClinicName());

			// 29 NOV 2017
			ipdForm.setAddmissionfor(bed.getAddmissionfor());
			ipdForm.setAlergies(bed.getAlergies());
			ipdForm.setChiefcomplains(bed.getChiefcomplains());
			ipdForm.setPresentillness(bed.getPresentillness());

			// peditric
			ipdForm.setBirthhist(bed.getBirthhist());
			ipdForm.setDiethist(bed.getDiethist());
			ipdForm.setDevelopmenthist(bed.getDevelopmenthist());
			ipdForm.setEmmunizationhist(bed.getEmmunizationhist());
			ipdForm.setHeadcircumference(bed.getHeadcircumference());
			ipdForm.setMidarmcircumference(bed.getMidarmcircumference());
			ipdForm.setLength(bed.getLength());
			ipdForm.setWtaddmission(bed.getWtaddmission());
			ipdForm.setWtdischarge(bed.getWtdischarge());
			ipdForm.setMaternal_history(bed.getMaternal_history());
			ipdForm.setPerinatal_history(bed.getPerinatal_history());

			// kunal
			ipdForm.setKunal_final_diagnosis_text(bed.getKunal_final_diagnosis_text());
			ipdForm.setKunal_manual_medicine_text(bed.getKunal_manual_medicine_text());

			String chief_comlint_id = masterDAO.getIpdTemplateId("Chief Complaints");
			String present_ill_id = masterDAO.getIpdTemplateId("Present Illness");

			ArrayList<Master> chief_complaints_list = masterDAO.getIpdTemplateListNames(chief_comlint_id);
			ArrayList<Master> present_illness_list = masterDAO.getIpdTemplateListNames(present_ill_id);
			ipdForm.setChief_complaints_list(chief_complaints_list);
			ipdForm.setPresent_illness_list(present_illness_list);

			ArrayList<String> otaptid = ipdDAO.getAllOTIds(selectedid, bed.getClientid());

			ArrayList<Master> otnoteslist = new ArrayList<Master>();
			for (String string : otaptid) {
				ArrayList<Master> otnoteslist1 = ipdDAO.getAllOtNotes(string);
				otnoteslist.addAll(otnoteslist1);
			}
			String otNotes = bed1.getOtNotes();
			if (otNotes == null) {
				otNotes = "";
			}
			String otNotesids = "0";
			for (Master master : otnoteslist) {
				otNotesids = otNotesids + "," + master.getId();
				otNotes = otNotes + master.getOtnotes();
			}
			ipdForm.setOtnoteslist(otnoteslist);
			ipdForm.setOtNotes(otNotes);
			ipdForm.setOtNotesids(otNotesids);

			ArrayList<Master> otdatesandids = ipdDAO.getOtDatesAndIdsFromdischarge(selectedid);
			ipdForm.setOtdatesandids(otdatesandids);
			int size1 = otdatesandids.size();
			if (size1 > 0) {
				String totalotids = otdatesandids.get(size1 - 1).getTotalids();
				ipdForm.setTotalotids(totalotids);
			} else {
				ipdForm.setTotalotids("0");
			}

			session.setAttribute("otdatesandids", otdatesandids);

			ArrayList<Priscription> treatmentlist = new ArrayList<Priscription>();

			// Akash
			if (loginInfo.isMedtreatgiven()) {
				treatmentlist = bedDao.gettreatmentlist(selectedid);
				ipdForm.setTreatmentlist1(treatmentlist);
			} else {
				ipdForm.setTreatmentlist1(treatmentlist);
			}

			ArrayList<Investigation> investlist = new ArrayList<Investigation>();
			/* investlist = bedDao.getinvestigationlist(selectedid); */
			ipdForm.setInvestlist(investlist);
			String treatmentgiven = "";
			String invstl = "";
			StringBuffer buffer = new StringBuffer();
			StringBuffer buffer1 = new StringBuffer();
			String priscid = "0";
			for (Priscription prisc : treatmentlist) {

				/*
				 * buffer.append(prisc.getMdicinenametxt()+" - "+prisc.getDosage
				 * ()+" - "+String.valueOf(prisc.getDays())+" <br>");
				 */
				buffer.append(prisc.getMdicinenametxt() + " <br>");
				priscid = priscid + "," + prisc.getPrischildid();
			}
			if (bed1.getTreatmentgiven() != null) {
				if (loginInfo.isMedtreatgiven()) {
					treatmentgiven = bed1.getTreatmentgiven() + "<br>" + buffer.toString();
				} else {
					treatmentgiven = bed1.getTreatmentgiven();
				}

			} else {
				if (loginInfo.isMedtreatgiven()) {
					treatmentgiven = buffer.toString();
				}

			}
			ipdForm.setPriscid(priscid);

			String invstids = "0";
			/*
			 * for(Investigation invst : investlist){ invstids =
			 * invstids+","+invst.getParentid();
			 * buffer1.append(invst.getInvsttype()+"--"+invst.getDate()+"<br>");
			 * } if(bed1.getInvestigation()!=null){ invstl=
			 * bed1.getInvestigation() +"<br>"+buffer1.toString(); }else{
			 * invstl= buffer1.toString(); }
			 */

			ipdForm.setInvstids(invstids);
			ipdForm.setTreatmentgiven(treatmentgiven);
			ipdForm.setInvestigation(bed1.getInvestigation());

			if (loginInfo.getInvestigation_newaccess().equals("1")) {
				ipdForm.setDischrgeOutcomes("9");
			}
			if (loginInfo.getClinicUserid().equals("aureus")) {
				ipdForm.setDischrgeOutcomes("9");
			}
			// Akash 24 May 2018
			ArrayList<Ipd> rmonoteslist = new ArrayList<Ipd>();
			rmonoteslist = ipdDAO.getRMONotesList(selectedid);
			StringBuffer stringBuffer = new StringBuffer();
			String rmonotesids = "0";
			String hospitalcorc = "";
			for (Ipd ipd2 : rmonoteslist) {
				rmonotesids = rmonotesids + "," + ipd2.getId();
				stringBuffer.append("Day:" + ipd2.getDay() + "<br>");
				stringBuffer.append("" + ipd2.getNotes() + "<br>");
			}

			if (bed1.getHospitalcourse() != null) {
				if (bed1.getHospitalcourse().equals("<br>")) {
					hospitalcorc = stringBuffer.toString();
				} else {
					hospitalcorc = bed1.getHospitalcourse() + "<br>" + stringBuffer.toString();
				}

			} else {
				hospitalcorc = stringBuffer.toString();
			}

			ipdForm.setHospitalcourse(hospitalcorc);
			ipdForm.setRmonotesids(rmonotesids);

			if (ipdForm.getSurgeon() == null) {
				if (ipd.getSurgeon() != null) {
					if (!ipd.getSurgeon().equals("")) {
						ipdForm.setSurgeon(ipd.getSurgeon());
					}
				}
			} else if (ipdForm.getSurgeon().equals("0")) {
				if (ipd.getSurgeon() != null) {
					if (!ipd.getSurgeon().equals("")) {
						ipdForm.setSurgeon(ipd.getSurgeon());
					}
				}
			}

			if (ipdForm.getAnesthesiologist() == null) {
				if (ipd.getAnesthesia() != null) {
					if (!ipd.getAnesthesia().equals("")) {
						ipdForm.setAnesthesiologist(ipd.getAnesthesia());
					}
				}
			} else if (ipdForm.getAnesthesiologist().equals("0")) {
				if (ipd.getAnesthesia() != null) {
					if (!ipd.getAnesthesia().equals("")) {
						ipdForm.setAnesthesiologist(ipd.getAnesthesia());
					}
				}
			}

			/*
			 * //get repeat prescription list EmrDAO emrDAO = new
			 * JDBCEmrDAO(connection); ArrayList<Priscription>parentPriscList =
			 * emrDAO.getParentPriscList(bed.getClientid(),bed.getPractitionerid
			 * (),bed.getConditionid());
			 * ipdForm.setParentPriscList(parentPriscList);
			 */

		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void listOfExtraData() {
		try {
			Connection connection = Connection_provider.getconnection();
			NotAvailableSlotDAO notAvailableSlotDAO = new JDBCNotAvailableSlotDAO(connection);
			EmrDAO emrDAO = new JDBCEmrDAO(connection);
			MasterDAO masterDAO = new JDBCMasterDAO(connection);
			UserProfileDAO userProfileDAO = new JDBCUserProfileDAO(connection);
			ClientDAO clientDAO = new JDBCClientDAO(connection);
			ArrayList<DiaryManagement> userList = notAvailableSlotDAO.getUserList(loginInfo.getId());
			ipdForm.setUserList(userList);

			// user define date time
			ipdForm.setHourList(PopulateList.hourListNew());
			ipdForm.setMinuteList(PopulateList.getMinuteList());
			String datetime = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
			String temp[] = datetime.split(" ");
			String date = DateTimeUtils.getCommencingDate1(temp[0]);
			ipdForm.setAdmissiondate(date);
			ipdForm.setDischargedate(date);
			String time[] = temp[1].split(":");
			String hh = time[0];
			String mm = time[1];
			ipdForm.setHour(hh);
			ipdForm.setMinute(mm);

			ArrayList<com.apm.Master.eu.entity.Discharge> dischargeOutcomeList = emrDAO.getDischrageOutcomeList();
			ipdForm.setDischargeOutcomeList(dischargeOutcomeList);

			ArrayList<com.apm.Master.eu.entity.Discharge> dischargeStatusList = emrDAO.getDischrageStatusList();
			ipdForm.setDischargeStatusList(dischargeStatusList);

			String dates = DateTimeUtils.getDashboardTodayDate(loginInfo.getTimeZone());
			ipdForm.setPriscdate(dates);
			ipdForm.setPriscdateandtime(DateTimeUtils.getPriscDatetime(loginInfo.getTimeZone()));

			ArrayList<Master> dosageList = emrDAO.getDosageList();
			ipdForm.setDosageList(dosageList);

			ArrayList<Master> mdicneTypeList = emrDAO.getMedicineTypeList();
			ipdForm.setMdicneTypeList(mdicneTypeList);

			ArrayList<Master> mdicinecategoryList = emrDAO.getmedicineCategoryList();
			ipdForm.setMdicinecategoryList(mdicinecategoryList);

			ArrayList<Priscription> templateNameList = emrDAO.getTemplateNameList(loginInfo);
			if (templateNameList.size() == 0) {
				templateNameList = new ArrayList<Priscription>();
			}
			ipdForm.setTemplateNameList(templateNameList);
			ipdForm.setCountry("India");

			// set staff name list of OPERATING SURGEON and OPERATIVE PROCEDURE
			UserProfile userProfile = userProfileDAO.getUserprofileDetails(loginInfo.getId());
			ArrayList<Master> procedureList = notAvailableSlotDAO.getProcedureList(userProfile.getDiciplineName());
			if (procedureList.size() == 0) {
				procedureList = new ArrayList<Master>();
			}
			ipdForm.setProcedureList(procedureList);

			/*
			 * ArrayList<UserProfile> staffList =
			 * notAvailableSlotDAO.getOTstaffList(); if(staffList.size()==0){
			 * staffList = new ArrayList<UserProfile>(); }
			 * ipdForm.setStaffList(staffList);
			 */

			ArrayList<UserProfile> staffList = userProfileDAO.getAllPractitionerList("1", null, null, null, null);
			ipdForm.setStaffList(staffList);

			if (staffList.size() == 0) {
				staffList = new ArrayList<UserProfile>();
			}
			ArrayList<Client> anesthesiaList = userProfileDAO.getAllAnethesiaList();
			ipdForm.setAnesthesiaList(anesthesiaList);

			ArrayList<UserProfile> mlcdrlist = userProfileDAO.getAllPractitionerList(null, null, null, null, "1");
			ipdForm.setMlcdrlist(mlcdrlist);

			ArrayList<Master> declerationTitleList = clientDAO.getDeclerationTitleList();
			ipdForm.setDeclerationTitleList(declerationTitleList);

			// jitu
			ArrayList<Master> specializationTemplateList = masterDAO.getMasterSpecializationList();
			ipdForm.setSpecializationTemplateList(specializationTemplateList);
			// set template name list

			ArrayList<Master> priscUnitList = masterDAO.getPriscUnitList();
			ipdForm.setPriscUnitList(priscUnitList);
			// Akash 11 April 2018
			ArrayList<Priscription> medicinetimelist = emrDAO.getMedicineTimeList();
			ipdForm.setMedicinetimelist(medicinetimelist);

			ArrayList<Master> medicineList = new ArrayList<Master>();
			ipdForm.setMedicineList(medicineList);

			ArrayList<Priscription> parentPriscList = new ArrayList<Priscription>();
			ipdForm.setParentPriscList(parentPriscList);

			ArrayList<Master> dosagenoteList = masterDAO.getDosageNoteList();
			ipdForm.setDosagenoteList(dosagenoteList);
			ArrayList<Client> thirdPartyTypeList = new ArrayList<Client>();
			thirdPartyTypeList = clientDAO.getThirdPartyType();
			ipdForm.setThirdPartyTypeList(thirdPartyTypeList);

			ArrayList<Master> nimaidoselist = masterDAO.getnimaidoselistt();
			ArrayList<Master> nimaiqtylist = masterDAO.getnimaiqtylist();
			ArrayList<Master> nimairemarklist = masterDAO.getnimairemarlist();
			
			ipdForm.setNimaidoselist(nimaidoselist);
			ipdForm.setNimaiqtylist(nimaiqtylist);
			ipdForm.setNimairemarklist(nimairemarklist);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getinvestigationlistforipd() {
		
		try {
			BufferedReader br=request.getReader();
			String line="";
			String inputjson="";
			if((line=br.readLine())!=null){
				inputjson=inputjson+line;
			}
			JSONObject jsonObject= new JSONObject(inputjson);
			String ipdid=jsonObject.getString("ipdid");
			Connection connection = Connection_provider.getconnection();
			String list = "";
			InvestigationDAO investigationDAO = new JDBCInvestigationDAO(connection);
			list = investigationDAO.getListOFIpdInvestigation(ipdid);
			JSONObject jsonobj = new JSONObject();
			jsonobj.put("textdata", list);
			
			String responsetext=jsonobj.toString();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(responsetext);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String savewardchargechangerate() {
		String clientid = request.getParameter("clientid");
		String wardid = request.getParameter("wardid");
		try {
			/*
			 * Connection connection= Connection_provider.getconnection();
			 * IpdDAO ipdDAO= new JDBCIpdDAO(connection); BedDao bedDao= new
			 * JDBCBedDao(connection); String ipdid=""; ipdid=
			 * ipdDAO.getLAstIpdIdByClient(clientid);
			 * ipdDAO.rateChangeFlagWard(ipdid,"1"); Bed bed=
			 * bedDao.getEditIpdData(ipdid);
			 * ipdDAO.rateChangeWardFromMaster(wardid, bed.getBedid());
			 */
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write("");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String newdischargeform() {
		try {
			if (!verifyLogin(request)) {
				return "login";
			}
			if (session.getAttribute("openedb") == null) {
				session.setAttribute("openedb", "ipd");
			}

			String selectedid = request.getParameter("selectedid");
			Connection connection= Connection_provider.getconnection();
			IpdDAO ipdDAO= new JDBCIpdDAO(connection);
			BedDao bedDao= new JDBCBedDao(connection);
			
			MasterDAO masterDAO= new JDBCMasterDAO(connection);
			NotAvailableSlotDAO notAvailableSlotDAO= new JDBCNotAvailableSlotDAO(connection);
			UserProfileDAO userProfileDAO= new JDBCUserProfileDAO(connection);
			ClientDAO clientDAO= new JDBCClientDAO(connection);
			IpdLogDAO ipdLogDAO= new JDBCIpdLogDAO(connection);
			DiaryManagementDAO diaryManagementDAO= new JDBCDiaryManagentDAO(connection);
			
			
			session.setAttribute("sessionadmissionid", selectedid);
			session.removeAttribute("finalConditions");
			if (session.getAttribute("openedb") == null) {
				session.setAttribute("openedb", "ipd");
			}
			
			// prepare data of IPDACTION LOKESH
						listOfExtraData();
						Ipd ipd = ipdDAO.getProcedureName(selectedid);

						Bed bed = bedDao.getEditIpdData(selectedid);
						session.setAttribute("sessionadmissionid", selectedid);
						String parentpriscid = ipdDAO.getParentPriscId(selectedid);
						ipdForm.setParentpriscid(parentpriscid);
						session.setAttribute("bed", bed);

						String procedureid = ipdDAO.getProcedureId(ipd.getProcedurename());
						ipdForm.setAppointmentText(procedureid);

						ipdForm.setNewadmndate(bed.getNewadmndate());
						ArrayList<Master> procedureList = notAvailableSlotDAO.getProcedureList(bed.getSpeciality());
						if (procedureList.size() == 0) {
							procedureList = new ArrayList<Master>();
						}

						ipdForm.setProcedureList(procedureList);

						if (loginInfo.getIpd_abbr_access() == 1) {
							String newipdabbr = ipdDAO.getIpdAbrivationIds(Integer.parseInt(selectedid));
							ipdForm.setNewipdabbr(newipdabbr);
							if (Integer.parseInt(bed.getIpdseqno()) > 0) {
								ipdForm.setIpdseqno(bed.getIpdseqno());
							} else {
								ipdForm.setIpdseqno(selectedid);
							}
						} else {
							if (Integer.parseInt(bed.getIpdseqno()) > 0) {
								ipdForm.setIpdseqno(bed.getIpdseqno());
								ipdForm.setNewipdabbr(bed.getIpdseqno());
							} else {
								ipdForm.setIpdseqno(selectedid);
								ipdForm.setNewipdabbr(selectedid);
							}
						}

						Client client = clientDAO.getClientDetails(bed.getClientid());

						UserProfile userProfile = userProfileDAO.getUserprofileDetails(Integer.parseInt(bed.getPractitionerid()));
						Master discipline = masterDAO.getDisciplineData(userProfile.getDiciplineName());
						ipdForm.setDiscipline(discipline.getDiscipline());
						ipdForm.setDoctor_name(
								userProfile.getInitial() + " " + userProfile.getFirstname() + " " + userProfile.getLastname());
						ipdForm.setDoctor_phone(userProfile.getMobile());
						ipdForm.setFullname(client.getFullname());
						ipdForm.setClient(ipdForm.getFullname());
						ipdForm.setAddress(client.getAddress());
						ipdForm.setContact(client.getMobNo());
						
						ipdForm.setAddr(client.getAddress() + "," + client.getTown() + "-" + client.getPostCode());
						ipdForm.setContact(client.getMobNo());
						
						
						ipdForm.setAgegender(client.getAge1()+"/"+client.getGender());
					
						
						String clientid= bed.getClientid();
						ipdForm.setPractitionerid(bed.getPractitionerid());
						ipdForm.setClientid(bed.getClientid());
						ipdForm.setAgeonadmn(bed.getAgeonAdmn());
						
						ipdForm.setQualification(userProfile.getQualification());

						String numcount = ipdDAO.getNumofAdmissionCount(clientid);
						ipdForm.setNum_admission(numcount);

						ipdForm.setKunal_final_diagnosis_text(bed.getKunal_final_diagnosis_text());
						ipdForm.setKunal_manual_medicine_text(bed.getKunal_manual_medicine_text());
						
						ArrayList<Bed> ipdconditionlist = bedDao.getIpdConditionList(selectedid);
						ipdForm.setIpdconditionlist(ipdconditionlist);
						
						Bed bedconditions = ipdDAO.getAllFinalCondition(selectedid, bed.getTreatmentepisodeid());

						ArrayList<Bed> finalConditions = new ArrayList<Bed>();

						if (bedconditions.getConditionname() != null) {

							for (String str : bedconditions.getConditionname().split(",")) {

								if (str == null) {
									continue;
								}
								if (str.equals("0")) {

									continue;
								}
								int id = Integer.parseInt(str);
								String conditionname = bedDao.getIpdConditionName(str);
								Bed bed2 = new Bed();
								bed2.setId(id);
								bed2.setConditionname(conditionname);
								bed2.setConditionid(str);
								finalConditions.add(bed2);

							}
						}
						ipdForm.setAbrivationid(client.getAbrivationid());

						ipdForm.setFinalConditions(finalConditions);

						ArrayList<Client> ipdCondtitionList = new ArrayList<Client>(); // clientDAO.getEmrTreatmentTypeList();
						session.setAttribute("finalConditions", finalConditions);
						session.setAttribute("ipdCondtitionmaster", ipdCondtitionList);
						
						String discharge_default_id = masterDAO.getIpdTemplateId("Discharge Default");
						String hospital_course_id = masterDAO.getIpdTemplateId("Hospital Course");
						String nursing_advice_id = masterDAO.getIpdTemplateId("Nursing Advice");
						String operative_notes_id = masterDAO.getIpdTemplateId("Operative Notes");
						String invenstigations = masterDAO.getIpdTemplateId("Investigations");
						String finding_on_discharge = masterDAO.getIpdTemplateId("FINDING ON DISCHARGE");
						String treatment_given_id = masterDAO.getIpdTemplateId("Discharge Treatment Given");
						String emergencydetailsid = masterDAO.getIpdTemplateId("Emergency Details");
						String treatmentgiventemplateid = masterDAO.getIpdTemplateId("Treatment Given");
						String onexami_id = masterDAO.getIpdTemplateId("On Examination");

						
						ArrayList<Master> discharge_default_list = masterDAO.getIpdTemplateListNames(discharge_default_id);
						ArrayList<Master> hospital_course_list = masterDAO.getIpdTemplateListNames(hospital_course_id);
						ArrayList<Master> nursing_advice_list = masterDAO.getIpdTemplateListNames(nursing_advice_id);
						ArrayList<Master> operativeList = masterDAO.getIpdTemplateListNames(operative_notes_id);
						ArrayList<Master> investigationList = masterDAO.getIpdTemplateListNames(invenstigations);
						ArrayList<Master> finding_on_dischargeList = masterDAO.getIpdTemplateListNames(finding_on_discharge);
						ArrayList<Master> treatment_given_list = masterDAO.getIpdTemplateListNames(treatment_given_id);
						ArrayList<Master> emergencydetailslist = masterDAO.getIpdTemplateListNames(emergencydetailsid);
						ArrayList<Master> treatmentgiventemplist = masterDAO.getIpdTemplateListNames(treatmentgiventemplateid);
						ArrayList<Master> on_exam_list = masterDAO.getIpdTemplateListNames(onexami_id);
						ipdForm.setOperativeList(operativeList);
						ipdForm.setDischarge_default_list(discharge_default_list);
						ipdForm.setHospital_course_list(hospital_course_list);
						ipdForm.setNursing_advice_list(nursing_advice_list);
						ipdForm.setInvestigationList(investigationList);
						ipdForm.setFinding_on_dischargeList(finding_on_dischargeList);
						ipdForm.setTreatment_given_list(treatment_given_list);
						ipdForm.setEmergencydetailslist(emergencydetailslist);
						ipdForm.setTreatmentgiventemplatelist(treatmentgiventemplist);
						ipdForm.setOn_exam_list(on_exam_list);
						ipdForm.setLamadamareason(bed.getReasonlamadama());
						
						ipdForm.setCommonTemplateList(ipdDAO.commonTemplateList());
						
						String chief_comlint_id = masterDAO.getIpdTemplateId("Chief Complaints");
						String present_ill_id = masterDAO.getIpdTemplateId("Present Illness");

						ArrayList<Master> chief_complaints_list = masterDAO.getIpdTemplateListNames(chief_comlint_id);
						ArrayList<Master> present_illness_list = masterDAO.getIpdTemplateListNames(present_ill_id);
						ipdForm.setChief_complaints_list(chief_complaints_list);
						ipdForm.setPresent_illness_list(present_illness_list);
						String surgical_template = masterDAO.getIpdTemplateId("OT Template");
						ArrayList<Master> otherTemplateList = masterDAO.getIpdTemplateListNames(surgical_template);
						ipdForm.setOtherTemplateList(otherTemplateList);
						
						InvestigationDAO investigationDAO = new JDBCInvestigationDAO(connection);
						ArrayList<InvstTemplate> templateList = investigationDAO.getTemplateList();
						ipdForm.setInvestigationtemplatelist(templateList);
						
						String specializationId = userProfile.getDiciplineName();
						
						boolean issystemicreview = masterDAO.isIpdFormFieldActive(specializationId, "Systemic Review");
						boolean history = masterDAO.isIpdFormFieldActive(specializationId, "History");
						boolean obstretic_history = masterDAO.isIpdFormFieldActive(specializationId, "OBSTRETIC HISTORY");
						boolean menstrual_history = masterDAO.isIpdFormFieldActive(specializationId, "MENSTRUAL HISTORY");
						boolean substance_history = masterDAO.isIpdFormFieldActive(specializationId, "SUBSTANCE HISTORY");
						
						boolean verification = masterDAO.isIpdFormFieldActive(specializationId, "Verification");
						boolean pediatric = masterDAO.isIpdFormFieldActive(specializationId, "Paediatric History");
						
						ipdForm.setConditionid(bed.getConditionid());
						ipdForm.setVerification(verification);
						ipdForm.setIssystemicreview(issystemicreview);
						ipdForm.setHistory(history);
						ipdForm.setObstretic_history(obstretic_history);
						ipdForm.setMenstrual_history(menstrual_history);
						ipdForm.setSubstance_history(substance_history);
						ipdForm.setPaediatrichist(pediatric);
						
						
						
						String admissiondate = bed.getAdmissiondate();
						String[] data = admissiondate.split(" ");
						String data2 = DateTimeUtils.getCommencingDate1(data[0]);
						String data3 = data2 + " " + data[1];
						// ipdForm.setAdmissiondate(bed.getAdmissiondate());
						if (loginInfo.isBalgopal()) {
							String time[] = data[1].split(":");
							int hourOfDay = (Integer.parseInt(time[0]));
							int minute = (Integer.parseInt(time[1]));
							String apmpm = ((hourOfDay > 12) ? hourOfDay % 12 : hourOfDay) + ":"
									+ (minute < 10 ? ("0" + minute) : minute) + " " + ((hourOfDay >= 12) ? "PM" : "AM");
							data3 = DateTimeUtils.getCommencingDate1(data[0]) + " " + apmpm;
						}
						ipdForm.setAdmissiondate(data3);

						
						Bed bed1 = ipdDAO.getDischargeData(bed.getTreatmentepisodeid());
						ipdForm.setChkDischarge(bed1.isChkDischarge());
						ipdForm.setDischrgeOutcomes(bed1.getDischrgeOutcomes());
						ipdForm.setDischargeStatus(bed1.getDischargeStatus());
						ipdForm.setHospitalcourse(bed1.getHospitalcourse());
						
						if (bed1.getDischargedate() != null) {
							if (!bed1.getDischargedate().equals("")) {
								String temp[] = bed1.getDischargedate().split(" ");
								String date = DateTimeUtils.getCommencingDate1(temp[0]);
								ipdForm.setDischargedate(date);
								String time[] = temp[1].split(":");
								String hh = time[0];
								String mm = time[1];
								ipdForm.setHour(hh);
								ipdForm.setMinute(mm);
							}
						}
						ArrayList<Master> otdatesandids = ipdDAO.getOtDatesAndIdsFromdischarge(selectedid);
						ipdForm.setOtdatesandids(otdatesandids);
						int size1 = otdatesandids.size();
						if (size1 > 0) {
							String totalotids = otdatesandids.get(size1 - 1).getTotalids();
							ipdForm.setTotalotids(totalotids);
						} else {
							ipdForm.setTotalotids("0");
						}

						session.setAttribute("otdatesandids", otdatesandids);
						
						
						boolean isentryExist=ipdDAO.checkIfJsonNewDischargeFormStatusExist(selectedid);
						if(!isentryExist){
							ipdDAO.insereJsonNewDischargeFormStatus(selectedid);
						}
						ipdForm.setJsonipdid(selectedid);
						ipdForm.setSurgeon(bed1.getSurgeon());
						ipdForm.setAnesthesiologist(bed1.getAnesthesiologist());
						ipdForm.setAnesthesia(bed1.getAnesthesia());
						ipdForm.setAppointmentText(bed1.getAppointmentText());
						
						ipdForm.setAdmn_summdiv(ipdDAO.chkStatusOfJsonNewDischargeForm("admn_summ",selectedid));
						ipdForm.setHistrydiv(ipdDAO.chkStatusOfJsonNewDischargeForm("histry",selectedid));
						ipdForm.setSurgical_notesdiv(ipdDAO.chkStatusOfJsonNewDischargeForm("surgical_notes",selectedid));
						ipdForm.setHospital_coursediv(ipdDAO.chkStatusOfJsonNewDischargeForm("hospital_course",selectedid));
						ipdForm.setTreatmnt_givendiv(ipdDAO.chkStatusOfJsonNewDischargeForm("treatmnt_given",selectedid));
						ipdForm.setInvst_div(ipdDAO.chkStatusOfJsonNewDischargeForm("invst",selectedid));
						ipdForm.setOtherdiv(ipdDAO.chkStatusOfJsonNewDischargeForm("other",selectedid));
						ipdForm.setEmergency_detdiv(ipdDAO.chkStatusOfJsonNewDischargeForm("emergency_det",selectedid));
						
						// get prescription list
						ArrayList<Priscription> dischargePriscList = ipdDAO.getDischargePrescList(selectedid);
						if (dischargePriscList.size() > 0) {
							Priscription pr = dischargePriscList.get(dischargePriscList.size() - 1);
							ipdForm.setStrengthflag(pr.isStrengthflag());
							ipdForm.setRemarkflag(pr.isRemarkflag());
							ipdForm.setQuantityflag(pr.isStrengthflag());
						}
						String discadvoice = ipdDAO.getDIscPrisc(selectedid);

						ipdForm.setAdvoice(discadvoice);

						ipdForm.setDischargePriscList(dischargePriscList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "newdischargeform";
	}

	
	public String getAdmissionDivDataOfDischargeForm(){
		try {
			Connection connection= Connection_provider.getconnection();
			BufferedReader br=request.getReader();
			String line="";
			String inputjson="";
			if((line=br.readLine())!=null){
				inputjson=inputjson+line;
			}
			JSONObject jsonObject= new JSONObject(inputjson);
			String ipdid=jsonObject.getString("ipdid");
			if(ipdid==null){
				ipdid="";
			}
			BedDao bedDao= new JDBCBedDao(connection);
			Bed bed = bedDao.getEditIpdData(ipdid);
			JSONObject jsonobj = new JSONObject();
			/*chiefcomplains,alergies,presentillness,headcircumference.midarmcircumference,length,wtaddmission,wtdischarge, on Next Hist   div familyhist,personalhist,earlierinvest,suggestedtrtment,suggestedtrtment,surgicalnotes,birthhist,diethist,developmenthist,emmunizationhist,example*/
			jsonobj.put("chiefcomplains", bed.getChiefcomplains());
			jsonobj.put("alergies", bed.getAlergies());
			jsonobj.put("presentillness", bed.getPresentillness());
			jsonobj.put("headcircumference", bed.getHeadcircumference());
			jsonobj.put("midarmcircumference", bed.getMidarmcircumference());
			jsonobj.put("length", bed.getLength());
			jsonobj.put("wtaddmission", bed.getWtaddmission());
			jsonobj.put("wtdischarge", bed.getWtdischarge());
			
			String responsetext=jsonobj.toString();
			response.setContentType("application/json");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(responsetext);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
public String getHistoryDivDataOfDischargeForm(){
	try {
		Connection connection= Connection_provider.getconnection();
		BufferedReader br=request.getReader();
		String line="";
		String inputjson="";
		if((line=br.readLine())!=null){
			inputjson=inputjson+line;
		}
		JSONObject jsonObject= new JSONObject(inputjson);
		String ipdid=jsonObject.getString("ipdid");
		if(ipdid==null){
			ipdid="";
		}
		IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		BedDao bedDao= new JDBCBedDao(connection);
		Bed bed = bedDao.getEditIpdData(ipdid);
		Bed bed1 = ipdDAO.getDischargeData(bed.getTreatmentepisodeid());
		JSONObject jsonobj = new JSONObject();
		/* Hist   div familyhist,personalhist,earlierinvest,suggestedtrtment,suggestedtrtment,surgicalnotes,birthhist,diethist,developmenthist,emmunizationhist,example,onexamination,otNotes*/
		jsonobj.put("familyhist", bed.getFamilyhist());
		jsonobj.put("personalhist", bed.getPersonalhist());
		jsonobj.put("earlierinvest", bed.getEarlierinvest());
		jsonobj.put("suggestedtrtment", bed.getSuggestedtrtment());
		jsonobj.put("surgicalnotes", bed.getSurgicalnotes());
		jsonobj.put("birthhist", bed.getBirthhist());
		jsonobj.put("diethist", bed.getDiethist());
		jsonobj.put("developmenthist", bed.getDevelopmenthist());
		jsonobj.put("emmunizationhist", bed.getEmmunizationhist());
		
		jsonobj.put("onexamination", bed.getOnexamination());
		jsonobj.put("otNotes", bed1.getOtNotes());
		jsonobj.put("pasthistory", bed.getPasthistory());
		jsonobj.put("example", bed1.getExample());
		
		String responsetext=jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(responsetext);
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}	
	

public String getHospitalCourseDivDataOfDischargeForm(){
	try {
		Connection connection= Connection_provider.getconnection();
		BufferedReader br=request.getReader();
		String line="";
		String inputjson="";
		if((line=br.readLine())!=null){
			inputjson=inputjson+line;
		}
		JSONObject jsonObject= new JSONObject(inputjson);
		String ipdid=jsonObject.getString("ipdid");
		if(ipdid==null){
			ipdid="";
		}
		IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		BedDao bedDao= new JDBCBedDao(connection);
		Bed bed = bedDao.getEditIpdData(ipdid);
		Bed bed1 = ipdDAO.getDischargeData(bed.getTreatmentepisodeid());
		JSONObject jsonobj = new JSONObject();
		/* Hist   div familyhist,personalhist,earlierinvest,suggestedtrtment,suggestedtrtment,surgicalnotes,birthhist,diethist,developmenthist,emmunizationhist,example,onexamination,otNotes*/
		jsonobj.put("hospitalcourse", bed1.getHospitalcourse());
		
		
		
		String responsetext=jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(responsetext);
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}

public String getTearmentGivenDivDataOfDischargeForm(){
	try {
		Connection connection= Connection_provider.getconnection();
		BufferedReader br=request.getReader();
		String line="";
		String inputjson="";
		if((line=br.readLine())!=null){
			inputjson=inputjson+line;
		}
		JSONObject jsonObject= new JSONObject(inputjson);
		String ipdid=jsonObject.getString("ipdid");
		if(ipdid==null){
			ipdid="";
		}
		IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		BedDao bedDao= new JDBCBedDao(connection);
		Bed bed = bedDao.getEditIpdData(ipdid);
		Bed bed1 = ipdDAO.getDischargeData(bed.getTreatmentepisodeid());
		JSONObject jsonobj = new JSONObject();
		/* Hist   div familyhist,personalhist,earlierinvest,suggestedtrtment,suggestedtrtment,surgicalnotes,birthhist,diethist,developmenthist,emmunizationhist,example,onexamination,otNotes*/
		jsonobj.put("treatmentgiven", bed1.getTreatmentgiven());
		
		
		
		String responsetext=jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(responsetext);
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}


public String getOtherDivDataOfDischargeForm(){
	try {
		Connection connection= Connection_provider.getconnection();
		BufferedReader br=request.getReader();
		String line="";
		String inputjson="";
		if((line=br.readLine())!=null){
			inputjson=inputjson+line;
		}
		JSONObject jsonObject= new JSONObject(inputjson);
		String ipdid=jsonObject.getString("ipdid");
		if(ipdid==null){
			ipdid="";
		}
		IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		BedDao bedDao= new JDBCBedDao(connection);
		Bed bed = bedDao.getEditIpdData(ipdid);
		Bed bed1 = ipdDAO.getDischargeData(bed.getTreatmentepisodeid());
		JSONObject jsonobj = new JSONObject();
		/*findondischarge,discadvnotes,kunal_manual_medicine_text*/
		if(bed1.getFindondischarge()==null){
			bed1.setFinaldiagnosis("");
		}
		if(bed1.getDiscadvnotes()==null){
			bed1.setDiscadvnotes("");
		}
		if(bed.getKunal_manual_medicine_text()==null){
			bed.setKunal_manual_medicine_text("");
		}
		
		jsonobj.put("findondischarge", bed1.getFindondischarge());
		jsonobj.put("discadvnotes", bed1.getDiscadvnotes());
		jsonobj.put("kunal_manual_medicine_text", bed.getKunal_manual_medicine_text());
		
		String responsetext=jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(responsetext);
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	
	
	
	return  null;
}

public String getEmergencyDetailsDivDataOfDischargeForm(){
	try {
		Connection connection= Connection_provider.getconnection();
		BufferedReader br=request.getReader();
		String line="";
		String inputjson="";
		if((line=br.readLine())!=null){
			inputjson=inputjson+line;
		}
		JSONObject jsonObject= new JSONObject(inputjson);
		String ipdid=jsonObject.getString("ipdid");
		if(ipdid==null){
			ipdid="";
		}
		IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		BedDao bedDao= new JDBCBedDao(connection);
		Bed bed = bedDao.getEditIpdData(ipdid);
		Bed bed1 = ipdDAO.getDischargeData(bed.getTreatmentepisodeid());
		JSONObject jsonobj = new JSONObject();
		/*findondischarge,discadvnotes,kunal_manual_medicine_text*/
		jsonobj.put("emergencydetail", bed1.getEmergencydetail());
		
		
		String responsetext=jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(responsetext);
		
	} catch (Exception e) {
		e.printStackTrace();
	}

	
	return null;
}


public String getInvstigationDivDataOfDischargeForm(){
	try {
		Connection connection= Connection_provider.getconnection();
		BufferedReader br=request.getReader();
		String line="";
		String inputjson="";
		if((line=br.readLine())!=null){
			inputjson=inputjson+line;
		}
		JSONObject jsonObject= new JSONObject(inputjson);
		String ipdid=jsonObject.getString("ipdid");
		if(ipdid==null){
			ipdid="";
		}
		IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		BedDao bedDao= new JDBCBedDao(connection);
		Bed bed = bedDao.getEditIpdData(ipdid);
		Bed bed1 = ipdDAO.getDischargeData(bed.getTreatmentepisodeid());
		JSONObject jsonobj = new JSONObject();
		/*findondischarge,discadvnotes,kunal_manual_medicine_text*/
		jsonobj.put("investigation", bed1.getInvestigation());
		
		
		String responsetext=jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(responsetext);
		
	} catch (Exception e) {
		e.printStackTrace();
	}

	
	return null;
}


public String saveInvstigationDivDataOfDischargeForm(){
	try {
		Connection connection= Connection_provider.getconnection();
		BufferedReader br=request.getReader();
		String line="";
		String inputjson="";
		if((line=br.readLine())!=null){
			inputjson=inputjson+line;
		}
		JSONObject jsonObject= new JSONObject(inputjson);
		String ipdid=jsonObject.getString("ipdid");
		String investigation=jsonObject.getString("investigation");
		if(ipdid==null){
			ipdid="";
		}
		IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		BedDao bedDao= new JDBCBedDao(connection);
		Bed bed = bedDao.getEditIpdData(ipdid);
		bed.setInvestigation(investigation);
	
		JSONObject jsonobj = new JSONObject();
		ipdDAO.saveInvestigationOfDischargeForm(bed);
		/*findondischarge,discadvnotes,kunal_manual_medicine_text*/
		jsonobj.put("investigation", "1");
		ipdDAO.updateJsonNewDischargeFormStatus(ipdid, "invst", "1");
		
		
		String responsetext=jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(responsetext);
		
	} catch (Exception e) {
		e.printStackTrace();
	}

	
	return null;
}


public String saveEmergencyDetailsDivDataOfDischargeForm(){
	try {
		Connection connection= Connection_provider.getconnection();
		BufferedReader br=request.getReader();
		String line="";
		String inputjson="";
		if((line=br.readLine())!=null){
			inputjson=inputjson+line;
		}
		JSONObject jsonObject= new JSONObject(inputjson);
		String ipdid=jsonObject.getString("ipdid");
		String emergencydetail=jsonObject.getString("emergencydet");
		if(ipdid==null){
			ipdid="";
		}
		IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		BedDao bedDao= new JDBCBedDao(connection);
		Bed bed = bedDao.getEditIpdData(ipdid);
		bed.setEmergencydetail(emergencydetail);
	
		JSONObject jsonobj = new JSONObject();
		ipdDAO.saveEmergencyDetailsOfDischargeForm(bed);
		/*findondischarge,discadvnotes,kunal_manual_medicine_text*/
		jsonobj.put("emergencydet", "1");
		ipdDAO.updateJsonNewDischargeFormStatus(ipdid, "emergency_det", "1");
		
		String responsetext=jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(responsetext);
		
	} catch (Exception e) {
		e.printStackTrace();
	}

	
	return null;
}


public String saveTreatmentGivenDivDataOfDischargeForm(){
	try {
		Connection connection= Connection_provider.getconnection();
		BufferedReader br=request.getReader();
		String line="";
		String inputjson="";
		if((line=br.readLine())!=null){
			inputjson=inputjson+line;
		}
		JSONObject jsonObject= new JSONObject(inputjson);
		String ipdid=jsonObject.getString("ipdid");
		String treatmentgiven=jsonObject.getString("treatmentgiven");
		if(ipdid==null){
			ipdid="";
		}
		IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		BedDao bedDao= new JDBCBedDao(connection);
		Bed bed = bedDao.getEditIpdData(ipdid);
		bed.setTreatmentgiven(treatmentgiven);
	
		JSONObject jsonobj = new JSONObject();
		ipdDAO.saveTearmentGivenDivDataOfDischargeForm(bed);
		ipdDAO.updateJsonNewDischargeFormStatus(ipdid, "treatmnt_given", "1");
		/*findondischarge,discadvnotes,kunal_manual_medicine_text*/
		jsonobj.put("treatmentgiven", "1");
		
		
		String responsetext=jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(responsetext);
		
	} catch (Exception e) {
		e.printStackTrace();
	}

	
	return null;
}


public String saveHospitalCourseDivDataOfDischargeForm(){
	try {
		Connection connection= Connection_provider.getconnection();
		BufferedReader br=request.getReader();
		String line="";
		String inputjson="";
		if((line=br.readLine())!=null){
			inputjson=inputjson+line;
		}
		JSONObject jsonObject= new JSONObject(inputjson);
		String ipdid=jsonObject.getString("ipdid");
		String hospitalcourse=jsonObject.getString("hospitalcourse");
		if(ipdid==null){
			ipdid="";
		}
		IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		BedDao bedDao= new JDBCBedDao(connection);
		Bed bed = bedDao.getEditIpdData(ipdid);
		bed.setHospitalcourse(hospitalcourse);
	
		JSONObject jsonobj = new JSONObject();
		ipdDAO.saveHospitalCourseDivDataOfDischargeForm(bed);
		/*findondischarge,discadvnotes,kunal_manual_medicine_text*/
		jsonobj.put("hospitalcourse", "1");
		ipdDAO.updateJsonNewDischargeFormStatus(ipdid, "hospital_course", "1");
		
		String responsetext=jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(responsetext);
		
	} catch (Exception e) {
		e.printStackTrace();
	}

	
	return null;
}

public String saveHistoryDivDataOfDischargeForm(){
	try {
		Connection connection= Connection_provider.getconnection();
		BufferedReader br=request.getReader();
		String line="";
		String inputjson="";
		if((line=br.readLine())!=null){
			inputjson=inputjson+line;
		}
		JSONObject jsonObject= new JSONObject(inputjson);
		String ipdid=jsonObject.getString("ipdid");
		
		
		if(ipdid==null){
			ipdid="";
		}
		IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		BedDao bedDao= new JDBCBedDao(connection);
		Bed bed = bedDao.getEditIpdData(ipdid);
		
		bed.setFamilyhist(jsonObject.getString("familyhist"));
		bed.setPersonalhist(jsonObject.getString("personalhist"));
		bed.setEarlierinvest(jsonObject.getString("earlierinvest"));
		bed.setSuggestedtrtment(jsonObject.getString("suggestedtrtment"));
		bed.setSurgicalnotes(jsonObject.getString("surgicalnotes"));
		bed.setBirthhist(jsonObject.getString("birthhist"));
		bed.setDiethist(jsonObject.getString("diethist"));
		bed.setDevelopmenthist(jsonObject.getString("developmenthist"));
		bed.setEmmunizationhist(jsonObject.getString("emmunizationhist"));
		bed.setOnexamination(jsonObject.getString("onexaminationlok"));
		bed.setPasthistory(jsonObject.getString("pasthistory"));
//		familyhist,personalhist,earlierinvest,suggestedtrtment,suggestedtrtment,surgicalnotes,birthhist,diethist,developmenthist,emmunizationhist
		JSONObject jsonobj = new JSONObject();
		Bed bed1 = ipdDAO.getDischargeData(bed.getTreatmentepisodeid());
		bed.setExample(jsonObject.getString("discharge_default"));
		bed.setOtNotes(jsonObject.getString("operation_notes"));
		ipdDAO.saveHistoryDivDataOfDischargeForm(bed);
		
		ipdDAO.updateJsonNewDischargeFormStatus(ipdid, "histry", "1");
		
		/*findondischarge,discadvnotes,kunal_manual_medicine_text*/
		jsonobj.put("hospitalcourse", "1");
		
		
		String responsetext=jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(responsetext);
		
	} catch (Exception e) {
		e.printStackTrace();
	}

	
	return null;
}



public String saveAdmissionDivDataOfDischargeForm(){
	try {
		Connection connection= Connection_provider.getconnection();
		BufferedReader br=request.getReader();
		String line="";
		String inputjson="";
		if((line=br.readLine())!=null){
			inputjson=inputjson+line;
		}
		JSONObject jsonObject= new JSONObject(inputjson);
		String ipdid=jsonObject.getString("ipdid");
		
		
		if(ipdid==null){
			ipdid="";
		}
		IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		BedDao bedDao= new JDBCBedDao(connection);
		Bed bed = bedDao.getEditIpdData(ipdid);
		
		bed.setChiefcomplains(jsonObject.getString("chiefcomplains"));
		bed.setAlergies(jsonObject.getString("alergies"));
		bed.setPresentillness(jsonObject.getString("presentillness"));
		bed.setHeadcircumference(jsonObject.getString("headcircumference"));
		bed.setMidarmcircumference(jsonObject.getString("midarmcircumference"));
		bed.setLength(jsonObject.getString("length"));
		bed.setWtaddmission(jsonObject.getString("wtaddmission"));
		bed.setWtdischarge(jsonObject.getString("wtdischarge"));
		
		//chiefcomplains,alergies,presentillness,headcircumference.midarmcircumference,length,wtaddmission,wtdischarge	
		Bed bed1 = ipdDAO.getDischargeData(bed.getTreatmentepisodeid());
		bed.setExample(bed1.getExample());
		ipdDAO.saveAdmissionDataOfDischForm(bed);
		JSONObject jsonobj= new JSONObject();
		/*findondischarge,discadvnotes,kunal_manual_medicine_text*/
		jsonobj.put("hospitalcourse", "1");
		ipdDAO.updateJsonNewDischargeFormStatus(ipdid, "admn_summ", "1");
		
		String responsetext=jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(responsetext);
		
	} catch (Exception e) {
		e.printStackTrace();
	}

	
	return null;
}

public String saveOtherDivDataOfDischargeForm(){
	try {
		Connection connection= Connection_provider.getconnection();
		BufferedReader br=request.getReader();
		String line="";
		String inputjson="";
		if((line=br.readLine())!=null){
			inputjson=inputjson+line;
		}
		JSONObject jsonObject= new JSONObject(inputjson);
		String ipdid=jsonObject.getString("ipdid");
		
		
		if(ipdid==null){
			ipdid="";
		}
		IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		BedDao bedDao= new JDBCBedDao(connection);
		Bed bed = bedDao.getEditIpdData(ipdid);
		
		String dayta=jsonObject.getString("discadvnotes");
		
		System.out.println(dayta);
		bed.setFindondischarge(jsonObject.getString("finddis"));
		bed.setDiscadvnotes(jsonObject.getString("discadvnotes"));
		bed.setKunal_manual_medicine_text(jsonObject.getString("kunal_manual_medicine_text"));
		
		//chiefcomplains,alergies,presentillness,headcircumference.midarmcircumference,length,wtaddmission,wtdischarge	
		
		ipdDAO.saveOTHERDivDataOfDischargeForm(bed);
		JSONObject jsonobj= new JSONObject();
		/*findondischarge,discadvnotes,kunal_manual_medicine_text*/
		jsonobj.put("hospitalcourse", "1");
		ipdDAO.updateJsonNewDischargeFormStatus(ipdid, "other", "1");
		
		String responsetext=jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(responsetext);
		
	} catch (Exception e) {
		e.printStackTrace();
	}

	
	return null;
}


public String saveDiagnosisOfDischargeForm(){
	try {
		Connection connection= Connection_provider.getconnection();
		BufferedReader br=request.getReader();
		String line="";
		String inputjson="";
		if((line=br.readLine())!=null){
			inputjson=inputjson+line;
		}
		JSONObject jsonObject= new JSONObject(inputjson);
		String ipdid=jsonObject.getString("ipdid");
		
		
		if(ipdid==null){
			ipdid="";
		}
		IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		BedDao bedDao= new JDBCBedDao(connection);
		Bed bed = bedDao.getEditIpdData(ipdid);
		
	
		
		String diagosis=(jsonObject.getString("diagosis"));
		String disstatusjson=(jsonObject.getString("disstatus"));
		String disOutcome=(jsonObject.getString("disOutcome"));
		String dischargedate=(jsonObject.getString("dischargedate"));
		String dishour=(jsonObject.getString("dishour"));
		String dismin=(jsonObject.getString("dismin"));
		String reasonformlama=(jsonObject.getString("reasonlamdama"));
		
		
		Bed bed1 = ipdDAO.getDischargeData(bed.getTreatmentepisodeid());

		String datetime = DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
		String allconditions = "0";
		
		if(diagosis!=null){
			for(String bedcondition:diagosis.split(",")){
				allconditions = allconditions + "," + bedcondition;
				boolean isexist = bedDao.isIpdExistCondition(ipdid, bed.getTreatmentepisodeid(),
						bedcondition);
				
				bed.setLastmodified(datetime);
				bed.setConditionid(bedcondition);
				if (!isexist) {
					int dd = bedDao.addIpdConditionReport(Integer.parseInt(ipdid), bed);
				}
			}
		}
		

		int result = ipdDAO.savefinalConditionDischarge(ipdid, bed.getTreatmentepisodeid(), datetime,
				allconditions);

		
		//chiefcomplains,alergies,presentillness,headcircumference.midarmcircumference,length,wtaddmission,wtdischarge	
		bed.setReasonlamadama(reasonformlama);
		DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	      Calendar cal1 = Calendar.getInstance();
	      String date = dateFormat1.format(cal1.getTime());  
	      bed.setDischargeDate(date);
		ipdDAO.saveOTHERDivDataOfDischargeForm(bed);
		
		 dischargedate = DateTimeUtils.getCommencingDate1(dischargedate) + " "
					+ dishour + ":" + dismin + ":20";

		 
		Bed bed2=bed1;
		bed2.setDischrgeOutcomes(disOutcome);
		bed2.setDischargeStatus(disstatusjson);
		 bed2.setDischargedate(dischargedate);
		 int status=0;
			int disstatus = ipdDAO.gettreatmentstatus(bed.getTreatmentepisodeid());
			if (disstatus == 1) {
				status = 1;
			}
			// if(ipdForm.isChkDischarge()){
			// status = 1;
			// }

			if (status == 1) {
				int res = bedDao.updateBedStatus(ipdid);
				int log = bedDao.saveDischargeLog(ipdid, bed.getClientid(), dischargedate, "", loginInfo);
			}

			bed2.setStatus("" + status);

			if (bed2.getAppointmentText() != null) {
				if (bed2.getAppointmentText().equals("0,") || bed2.getAppointmentText().equals("0, ")) {
					bed2.setAppointmentText("0");
				}
			}
			EmrDAO emrDAO=new JDBCEmrDAO(connection);
			String test = bed2.getAppointmentText();
			bed2.setTreatmentepisodeid(bed.getTreatmentepisodeid());
			if (status == 1) {

				
				
				int upd = emrDAO.updateTreatmentEpisodeDischargeForm(bed2);

				AllTemplateAction allTemplateAction = new AllTemplateAction();
				allTemplateAction.sendDischargeEmail(bed.getClientid(), bed.getConditionid(), bed.getPractitionerid(), "0", loginInfo, connection,
						bed.getTreatmentepisodeid());
			}else{
				int upd = emrDAO.updateTreatmentEpisodeDischargeForm(bed2);
			}

			String userid = loginInfo.getUserId();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar calendar = Calendar.getInstance();
			String todate = dateFormat.format(calendar.getTime());
			boolean flag = ipdDAO.getIsDisCheckListStatus("1", bed.getTreatmentepisodeid());

			if (!flag) {
				int res99 = ipdDAO.updateCheckListStatus("1", "1", bed.getTreatmentepisodeid(), userid, todate);
				int res1 = ipdDAO.updateCheckListStatus("1", "2", bed.getTreatmentepisodeid(), userid, todate);
				int res2 = ipdDAO.updateCheckListStatus("1", "3", bed.getTreatmentepisodeid(), userid, todate);
				int res3 = ipdDAO.updateCheckListStatus("1", "4", bed.getTreatmentepisodeid(), userid, todate);
				int disdataid1 = ipdDAO.getDisDataId("1", bed.getTreatmentepisodeid());
				int disdataid2 = ipdDAO.getDisDataId("2", bed.getTreatmentepisodeid());
				int disdataid3 = ipdDAO.getDisDataId("3", bed.getTreatmentepisodeid());
				int disdataid4 = ipdDAO.getDisDataId("4", bed.getTreatmentepisodeid());
				int res6 = ipdDAO.updateDischargeCKLIndi(disdataid1);
				int res7 = ipdDAO.updateDischargeCKLIndi(disdataid2);
				int res8 = ipdDAO.updateDischargeCKLIndi(disdataid3);
				int res9 = ipdDAO.updateDischargeCKLIndi(disdataid4);
			}
			



		 
		 
		 
		 
		 
		 
			 int rest=bedDao.updateUseridInTable(loginInfo.getUserId(), bed.getTreatmentepisodeid(), "lastformfilleruserid");	
		
		JSONObject jsonobj= new JSONObject();
		/*findondischarge,discadvnotes,kunal_manual_medicine_text*/
		jsonobj.put("hospitalcourse", "1");
		
		
		String responsetext=jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(responsetext);
		
	} catch (Exception e) {
		e.printStackTrace();
	}

	
	return null;
}

public String getSurgicalNotewsDivData(){
	try {
		Connection connection= Connection_provider.getconnection();
		BufferedReader reader=request.getReader();
		String ipline="";
		String line="";
		if((line=reader.readLine())!=null){
			ipline=ipline+line;
					
		}
		JSONObject jsonObject= new JSONObject(ipline);
		String ipdid=jsonObject.getString("ipdid");
		IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		BedDao bedDao= new JDBCBedDao(connection);
		Bed bed = bedDao.getEditIpdData(ipdid);
		Bed bed1 = ipdDAO.getDischargeData(bed.getTreatmentepisodeid());
		JSONObject jsonobj = new JSONObject();
		/*findondischarge,discadvnotes,kunal_manual_medicine_text*/
		jsonobj.put("operation_notes", bed1.getOtNotes());
		
		
		String responsetext=jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(responsetext);
		
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}


public String saveSurgicalNotewsDivData(){
	try {
		Connection connection= Connection_provider.getconnection();
		BufferedReader br=request.getReader();
		String line="";
		String inputjson="";
		if((line=br.readLine())!=null){
			inputjson=inputjson+line;
		}
		JSONObject jsonObject= new JSONObject(inputjson);
		String ipdid=jsonObject.getString("ipdid");
		String otnotes=jsonObject.getString("otnotes");
	
		if(ipdid==null){
			ipdid="";
		}
		IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		BedDao bedDao= new JDBCBedDao(connection);
		Bed bed = bedDao.getEditIpdData(ipdid);
		
		bed.setOtNotes(otnotes);
		bed.setSurgeon(jsonObject.getString("surgeon"));
		bed.setAnesthesiologist(jsonObject.getString("anesthesiologist"));
		bed.setAnesthesia(jsonObject.getString("anesthesia"));
		bed.setAppointmentText(jsonObject.getString("procedure"));
		//chiefcomplains,alergies,presentillness,headcircumference.midarmcircumference,length,wtaddmission,wtdischarge	
		
		ipdDAO.saveSurgicalNotesOfDiv(bed);
		JSONObject jsonobj= new JSONObject();
		/*findondischarge,discadvnotes,kunal_manual_medicine_text*/
		jsonobj.put("hospitalcourse", "1");
		ipdDAO.updateJsonNewDischargeFormStatus(ipdid, "surgical_notes", "1");
		
		String responsetext=jsonobj.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(responsetext);
		
	} catch (Exception e) {
		e.printStackTrace();
	}

	
	return null;
}


public String refreshAjaxipTemplateList(){
	try {
		String tempid=request.getParameter("reqtempid");
		Connection connection= Connection_provider.getconnection();
		IpdDAO ipdDAO = new JDBCIpdDAO(connection);
		MasterDAO masterDAO= new JDBCMasterDAO(connection);
		ArrayList<Master> treatmentgiventemplist = masterDAO.getIpdTemplateListNames(tempid);
		StringBuffer buffer= new StringBuffer();
		String function="gettreattemplate(this.value)";
		if(tempid!=null){
			if(tempid.equals("7")){
				function="gettreatmentttemplate(this.value)";
			}else if(tempid.equals("2")){
				function="getpresentIllness(this.value)";
			}else if(tempid.equals("11")){
				function="getOTSurgicaltemplate(this.value)";
			}else if(tempid.equals("9")){
				function="gethosptemplate(this.value)";
			}else if(tempid.equals("")){
				function="getInvstemplate(this.value)";
			}else if(tempid.equals("12")){
				function="getemergencytemplate(this.value)";
			}else if(tempid.equals("16")){
				function="getFindOnDischtemplate(this.value)";
			}else if(tempid.equals("10")){
				function="getnursingtemplate(this.value)";
			}else if(tempid.equals("1")){
				function="setChiefComplaints(this.value)";
			}else if(tempid.equals("8")){
				function="getdisctemplate(this.value)";
			}
		}
		buffer.append("<select onchange='"+function+"' class='form-control'  id=''>");
		buffer.append("<option value='0'>Select Template</option>");
		for(Master master:treatmentgiventemplist){
			buffer.append("<option value='"+master.getId()+"'>"+master.getName()+"</option>");
		}
		buffer.append("</select>");
		
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(buffer.toString());
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}


public String loadolddataofdiagnosis(){
	try {
		String diagnosisid=request.getParameter("diagid");
		String clientId=request.getParameter("clientid");
		Connection connection= Connection_provider.getconnection();
		IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		
		ArrayList<Bed> ipdList=ipdDAO.getAllIpdidsOfDiagnosis(diagnosisid);
		
		StringBuffer buffer= new StringBuffer();
		buffer.append("<table id='diag' class='my-table xlstable' style='width:100%;border-bottom:0px solid gray !important'>");
		buffer.append("<tr><th><b>Discharge Card Id</b></th><th><b>Name</b> </th><th><b>Sex/Age</b> </th><th><b>Discharge Status</b></th></tr>");
		for(Bed bed:ipdList){
			if(bed!=null){
				buffer.append("<tr>");
				buffer.append("<td><input type='radio' class='chk' name='diagipdid'  value='"+bed.getIpdnewid()+"'>   <b><a onclick=opencPopup('printdischargeCommonnew?selectedid="+bed.getIpdnewid()+"&clientid=')>"+bed.getIpdseqno()+"</a></b></td>");
				buffer.append("<td><b>"+bed.getClientname()+"</b></td>");
				buffer.append("<td><b>"+bed.getGender()+" / "+bed.getAge()+"</b></td>");
				buffer.append("<td><b>"+bed.getDis_status_name()+"</b></td>");
				buffer.append("</tr>");
			}
		}
		buffer.append("</table>");
		
		
		ArrayList<Bed> previousipdlist=ipdDAO.getAllIpdOfClient(clientId);
		if(previousipdlist.size()>0){
			buffer.append("<br> <h4 align='center'><b> Also Your Previous Data</b></h4>");
			buffer.append("<table id='diag2' class='my-table xlstable' style='width:100%;border-bottom:0px solid gray !important'>");
			buffer.append("<tr><th><b>Discharge Card Id</b></th><th><b>Name</b> </th><th><b>Sex/Age</b> </th><th><b>Discharge Status</b></th></tr>");
			for(Bed bed:previousipdlist){
				if(bed!=null){
					buffer.append("<tr>");
					buffer.append("<td><input type='radio' class='chk' name='diagipdid'  value='"+bed.getIpdnewid()+"'>   <b><a onclick=opencPopup('printdischargeCommonnew?selectedid="+bed.getIpdnewid()+"&clientid=')>"+bed.getIpdseqno()+"</a></b></td>");
					buffer.append("<td><b>"+bed.getClientname()+"</b></td>");
					buffer.append("<td><b>"+bed.getGender()+" / "+bed.getAge()+"</b></td>");
					buffer.append("<td><b>"+bed.getDis_status_name()+"</b></td>");
					buffer.append("</tr>");
				}
			}
			buffer.append("</table>");
	
		}
				
		
		
		
		
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(buffer.toString());
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}



public String givefollowup(){
	try {
		Connection connection=Connection_provider.getconnection();
		String ipdid=DateTimeUtils.isNull(request.getParameter("ipdid"));
		String date=DateTimeUtils.isNull(request.getParameter("date"));
		String isbookapmt=DateTimeUtils.isNull(request.getParameter("bookapmt"));
		BedDao bedDao= new JDBCBedDao(connection);
		Bed bed=bedDao.getEditIpdData(ipdid);
		ClientDAO clientDAO= new JDBCClientDAO(connection);
		Client client= new Client();
		client.setClientId(bed.getClientid());
		client.setIpdid(ipdid);
		client.setIpdid(ipdid);
		client.setType("1");
		client.setPractid(bed.getPractitionerid());
		client.setLocation("IPD");
		client.setFollowupdate(DateTimeUtils.getCommencingDate1(date));
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	     Calendar cal = Calendar.getInstance();
	     String toDaysDate = dateFormat.format(cal.getTime());
	     client.setDate(toDaysDate);
	    String practionerId=bed.getPractitionerid(); 
		
		if(isbookapmt.equals("true")){
			int followup=clientDAO.savefollowup(client);
			String newfollwupdate=date;
			newfollwupdate=DateTimeUtils.getCommencingDate1(newfollwupdate);
			AccountsDAO accountsDAO= new JDBCAccountsDAO(connection);
			String whopay=clientDAO.getWhoPayName(client.getClientId());
			AppointmentType appointmentType= accountsDAO.getFollowUpIdCharge(whopay);
			if(appointmentType.getCharges()!=null){
				NotAvailableSlotDAO notAvailableSlotDAO =  new JDBCNotAvailableSlotDAO(connection);	
				NotAvailableSlot n =notAvailableSlotDAO.getMveDiaryUserDetails(""+practionerId,newfollwupdate);
				
				
				String stime = n.getSTime();
				int slotid = n.getId();
				String duration = n.getDuration();
				boolean chkapmtexsist = notAvailableSlotDAO.chkmveapmtaxsist(""+practionerId,newfollwupdate);
				if(chkapmtexsist){
					stime = notAvailableSlotDAO.getmveapmtendtime(""+practionerId,newfollwupdate);
				}
				SimpleDateFormat df = new SimpleDateFormat("HH:mm");
				 Date d = df.parse(stime); 
				 Calendar cal1 = Calendar.getInstance();
				 cal1.setTime(d);
				 cal1.add(Calendar.MINUTE, 5);
				 String endtime = df.format(cal1.getTime());
				 
				 Client client2= clientDAO.getClientDetails(bed.getClientid());
				 
				 UserProfileDAO userProfileDAO= new JDBCUserProfileDAO(connection);
				 UserProfile user= new UserProfile();
					user=userProfileDAO.getUserprofileDetails(DateTimeUtils.convertToInteger(practionerId));
				 NotAvailableSlot notAvailableSlot= new NotAvailableSlot();
				
				 notAvailableSlot.setApmtSlotId(slotid);
				 notAvailableSlot.setCommencing(newfollwupdate);
				 notAvailableSlot.setSTime(stime);
				 notAvailableSlot.setEndTime(endtime);
				 notAvailableSlot.setDiaryUserId((DateTimeUtils.convertToInteger(practionerId)));
				 notAvailableSlot.setDiaryUser(user.getFullname());
				 notAvailableSlot.setLocation("1");
				 notAvailableSlot.setDept(user.getDiciplineName());
				 notAvailableSlot.setClient(client2.getFullname());
				 notAvailableSlot.setClientId(bed.getClientid());
				 notAvailableSlot.setApmtDuration("00:05");
				 notAvailableSlot.setApmtType(""+appointmentType.getId());
				 notAvailableSlot.setRoom("Room1");
				 notAvailableSlot.setPayBy("Client");
				 notAvailableSlot.setAddedBy(""+loginInfo.getUserId());
				 notAvailableSlot.setCondition(user.getDiciplineName());
				 notAvailableSlot.setVaccineApmt(true);
				 notAvailableSlot.setNotes("");
				 notAvailableSlot.setTreatmentEpisodeId("0");
				 notAvailableSlot.setUsedsession("0");
				 notAvailableSlot.setOtid(0);
				 notAvailableSlot.setOtplan(""+0);
				 notAvailableSlot.setProcedure(""+0);
				 notAvailableSlot.setSurgeon(""+0);
				 notAvailableSlot.setAnesthesia(""+0);
				 notAvailableSlot.setIpdno(""+0);
				 notAvailableSlot.setWardid(""+0);
				 notAvailableSlot.setAssistaffcharge(""+0);
				 notAvailableSlot.setSic(""+0);
				 notAvailableSlot.setPsurcharge(""+0);
				 notAvailableSlot.setPanetcharge(""+0);
					
				 
				 
				int x= notAvailableSlotDAO.saveAppointment(notAvailableSlot);
				 
				String message="Your Follow up is Scheduled on "+DateTimeUtils.getCommencingDate1(client.getDate())+" by "+user.getFullname()+"";
				
				SmsService s= new SmsService();
				/*s.sendSms(message, client2.getMobNo(), loginInfo, new EmailLetterLog());*/
			}
		
		}
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write("");
	} catch (Exception e) {
		// TODO: handle exception
	}
	
	return null;
}
public String patientbarcode(){
	String clientId= DateTimeUtils.isNull(request.getParameter("id"));
	String uhid=DateTimeUtils.isNull(request.getParameter("abrivation"));
	String namefull=DateTimeUtils.isNull(request.getParameter("patientNameOrig"));
	try {
	/*	Connection connection= Connection_provider.getconnection();
		ClientDAO clientDAO= new JDBCClientDAO(connection);
		Client  client= clientDAO.getClientDetails(clientId);*/
		 /*  Code128 code128 = new Code128();
		   code128.setBarcodeWidth(3);
		   String imgname = clientId + "" + uhid + ".gif";
		   code128.setShowText(false);
		   code128.setData(""+uhid);
		  
		  
		   String filePath = request.getRealPath("/mdbarcode/"+imgname);
		   code128.drawBarcode(filePath);*/
			Client client= new Client();
		ClientDAO clientDAO= new JDBCClientDAO(Connection_provider.getconnection());
		client=clientDAO.getPatient(DateTimeUtils.convertToInteger(clientId));
		   ipdForm.setClientid(clientId);
		   ipdForm.setAbrivationid(client.getAbrivationid());
		   /*ipdForm.setFullname(client.getTitle()+" "+client.getFullname());*/
		   ipdForm.setFullname(""+client.getFullname());
	} catch (Exception e) {
	e.printStackTrace();
	}
	return "patientbarcode";
}

public String savepaymentreportnotes(){
	try {
		BufferedReader br= request.getReader();
		String line="",jsonString="";
		if((line=br.readLine())!=null){
			jsonString=jsonString+line;
		}
		
		JSONObject jsonObject= new JSONObject(jsonString);
		String note=jsonObject.getString("data");
		Connection connection= Connection_provider.getconnection();
		UserProfileDAO userProfileDAO= new JDBCUserProfileDAO(connection);
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			String date = dateFormat.format(cal.getTime());
		
		if(!(userProfileDAO.ifEntryExistUserPaymentReportNotes(loginInfo.getUserId(), date))){
			int res=userProfileDAO.insertToUserPaymentReportNotes(loginInfo, note, date);
		}else{
			int id=userProfileDAO.getIdOfPaymentReportNotes(loginInfo.getUserId(), date);
			int res=userProfileDAO.updateStatusUserPaymentReportNotes(loginInfo, "0", date, ""+id, note);
		}
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write("");
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
} 


public String saveDeathNoteDivDataOfDischargeForm(){
	try {
		Connection connection= Connection_provider.getconnection();
		BufferedReader br=request.getReader();
		String line="";
		String inputjson="";
		if((line=br.readLine())!=null){
			inputjson=inputjson+line;
		}
		JSONObject jsonObject= new JSONObject(inputjson);
		String ipdid=jsonObject.getString("ipdid");
		String death_note=jsonObject.getString("death_note");
	
		if(ipdid==null){
			ipdid="";
		}
		IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		BedDao bedDao= new JDBCBedDao(connection);
		Bed bed = bedDao.getEditIpdData(ipdid);
		
		bed.setDeathnote(death_note);
		
		//chiefcomplains,alergies,presentillness,headcircumference.midarmcircumference,length,wtaddmission,wtdischarge	
		
		ipdDAO.saveSurgicalNotesOfDiv(bed);
		JSONObject jsonobj= new JSONObject();
		/*findondischarge,discadvnotes,kunal_manual_medicine_text*/
		jsonobj.put("hospitalcourse", "1");
		
		ipdDAO.saveDeathNotesDivData(bed);
		
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write("");
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}

public String getDeathNoteDivDataOfDischargeForm(){
	try {
		Connection connection= Connection_provider.getconnection();
		BufferedReader br=request.getReader();
		String line="";
		String inputjson="";
		if((line=br.readLine())!=null){
			inputjson=inputjson+line;
		}
		JSONObject jsonObject= new JSONObject(inputjson);
		String ipdid=jsonObject.getString("ipdid");
		BedDao bedDao= new JDBCBedDao(connection);
		IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		Bed bed = bedDao.getEditIpdData(ipdid);
		bed=ipdDAO.getDischargeData(bed.getTreatmentepisodeid());
		jsonObject.put("death_note", bed.getDeathnote());
		
		
		String responsetext=jsonObject.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+responsetext);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}

public String setaccessToThis(){
	try {
		Connection connection= Connection_provider.getconnection();
		ClinicDAO clinicDAO= new JDBCClinicDAO(connection);
		String colname=request.getParameter("col");
		String value=request.getParameter("value");
		int x=clinicDAO.switchAccessOfClinic(value, colname);
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write("" +x+ "");
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}
public String all_relesed_notes(){
	try {
		String fromDate=DateTimeUtils.isNull(ipdForm.getFromdate()),toDate=DateTimeUtils.isNull(ipdForm.getTodate());
		if(fromDate.equals("")){
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();
			fromDate = dateFormat.format(cal.getTime());
			toDate = dateFormat.format(cal.getTime());
		}
		Connection connection= Connection_provider.getconnection();
		UserAdministartionDAO userAdministartionDAO= new JDBCUserAdministration(connection);
		ArrayList<UserProfile> releaseList=new ArrayList<UserProfile>();
		releaseList=userAdministartionDAO.relesedNotesList(fromDate, toDate);
		ipdForm.setReleaseList(releaseList);
		ipdForm.setFromdate(fromDate);
		ipdForm.setTodate(toDate);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return "all_relesed_notes";
}

public String saverelesed_notes(){
	try {
		Connection connection=Connection_provider.getconnection();
		File uploadedFile = ipdForm.getSubuploadfiles();
		String filecontenttype = ipdForm.getSubuploadfilesContentType();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		String date = dateFormat.format(cal.getTime());
		
		String fileName= ipdForm.getSubuploadfilesFileName();
 		//String path =ServletActionContext.getre("/liveData/release_notes/");
		
		
		String path =request.getRealPath(("/liveData/release_notes/"));
		File uploadFolder = new File(path);
		if (!uploadFolder.exists()) {
			uploadFolder.mkdirs();
		}
		File fileToCreate = new File(path, fileName);
        FileUtils.copyFile(uploadedFile, fileToCreate);
        UserAdministartionDAO userAdministartionDAO= new JDBCUserAdministration(connection);
        UserProfile userProfile= new UserProfile();
        userProfile.setDatetime(date);
        userProfile.setFilename(fileName);
        userProfile.setWarname(DateTimeUtils.isNull(ipdForm.getWarname()));
        int uploaded=userAdministartionDAO.saveReleasedNotes(userProfile);
	} catch (Exception e) {
		e.printStackTrace();
	}
	return "saverelesed_notes";
}


public String saveclientfinding() throws Exception{
	   
	   Connection connection=null;
	   try {
		   connection=Connection_provider.getconnection();
		   IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		   String finding= request.getParameter("finding");
		   String ipdid= request.getParameter("ipdid");
		   String clientid= request.getParameter("clientid");
		   String vitalid= request.getParameter("vitalid");
		   String time=request.getParameter("time");
		   String date= DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()).split(" ")[0];
		   String dateTime= DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
		   Master master =new Master();
		   master.setFinding(finding);
		   master.setIpdid(ipdid);
		   master.setClientid(clientid);
		   master.setVital_master_id(vitalid);
		   master.setDate(date);
		   master.setUserid(loginInfo.getUserId());
		   master.setDatetime(dateTime);
		   master.setIsfromdcard(DateTimeUtils.isNull(request.getParameter("isfromdcard")));
		  String isfromclinical=DateTimeUtils.isNull(request.getParameter("isfromclinical"));
		  if(isfromclinical.equals("1")){
			  master.setDate(DateTimeUtils.getCommencingDate1(time.split(" ")[0]));
			  time=time.split(" ")[1];
			  master.setDatetime(master.getDate()+" "+time+":00");
		  }
		   
		   int res= ipdDAO.saveClientVitalData(master, finding, time);
		   
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(""); 
		   		   
		
	   } catch (Exception e) {
		
		   e.printStackTrace();
	   }
	   finally {
		   connection.close();
	   }
	   return null;
}



public String getnursingtasknamelist() throws Exception{
	Connection connection=null;
	try {
		connection=Connection_provider.getconnection();
		MasterDAO masterDAO = new JDBCMasterDAO(connection);
		String id = request.getParameter("id");
		ArrayList<Master>  arrayList = masterDAO.getNursingDetailsByNursingTypeid(id);
		StringBuilder str = new StringBuilder();
		str.append("<select class='form-control showToolTip chosen' name='taskname' id='taskname'>");
		str.append("<option value='0'>Select Taskname</option>");	
		for (Master master : arrayList) {
				str.append("<option value='"+master.getTaskname()+"'>"+master.getTaskname()+"</option>");
			}
		str.append("</select>");	  
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+str.toString()+"");
	} catch (Exception e) {
		e.printStackTrace();
	}finally{
		connection.close();
	}
	return null;
}

public String getclinical_notes_new_list_to_emr(){
	StringBuffer buffer= new StringBuffer();
	try {
		String clientId=DateTimeUtils.isNull(request.getParameter("cid"));
		String practioner=DateTimeUtils.isNull(request.getParameter("pid"));
		Connection connection= Connection_provider.getconnection();
		EmrDAO emrDAO= new JDBCEmrDAO(connection);
		ArrayList<DietaryDetails> dietList=emrDAO.dietryList(clientId, practioner);
		ArrayList<Master> vitaList= emrDAO.vitalList(clientId, practioner);
		ArrayList<Master> nursingList=emrDAO.nursingcareList(clientId, practioner);
		
		if(dietList.size()>0){
			int i=1;
			buffer.append("<label>Diet List</label><br>");
			buffer.append("<table class='my-table'><tr><th>Sr.</th><th>Date / Time</th><th>Print</th></tr>");
			for(DietaryDetails diet:dietList){
				buffer.append("<tr><td>"+i+"</td> <td>"+diet.getLastmodified()+"</td>  <td><a href='#'><i class='fa fa-print' onclick=openPopup('printdiethistoryDietarydetails?parentid="+diet.getId()+"')></i></a></td></tr>");
				i++;
			}
			buffer.append("</table><br>");
		}
		
		if(vitaList.size()>0){
			int i=1;
			buffer.append("<label>Vital List</label><br>");
			buffer.append("<table class='my-table'><tr><th>Sr.</th><th>Ipd Number</th><th>Print</th></tr>");
			for(Master vital:vitaList){
				buffer.append("<td>"+i+"</td> <td>"+vital.getIpd()+"</td>  <td><a href='#'><i class='fa fa-print' onclick=openPopup('vitalstatisticsIpd?clientid="+vital.getClientid()+"&ipdid="+vital.getIpd()+"')></i></a></td>");
				i++;
			}
			buffer.append("</table><br>");
		}
		
		if(nursingList.size()>0){
			int i=1;
			buffer.append("<label>Nursing List</label><br>");
			buffer.append("<table class='my-table'><tr><th>Sr.</th><th>Date / Time</th><th>Print</th></tr>");
			for(Master nursing:nursingList){
				buffer.append("<td>"+i+"</td> <td>"+nursing.getDatetime()+"</td>  <td><a href='#'><i class='fa fa-print' ></i></a></td>");
				i++;
			}
			buffer.append("</table><br>");
		}
		
		
	
		
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(""+buffer.toString()+"");
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return null;
}

public String setdichargecheckers(){
	try {
		Connection connection= Connection_provider.getconnection();
		BedDao bedDao = new JDBCBedDao(connection);
		BufferedReader br=request.getReader();
		String line="";
		String inputjson="";
		if((line=br.readLine())!=null){
			inputjson=inputjson+line;
		}
		JSONObject jsonObject= new JSONObject(inputjson);
		String ipdid=jsonObject.getString("ipdid");
		String val=jsonObject.getString("val");
		String column=jsonObject.getString("column");
		
		int res=bedDao.saveNewFieldsData(ipdid, column, val);
		
		String responsetext=jsonObject.toString();
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(responsetext);
		
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}

public String save_template_ajax(){
	try {
		Connection connection= Connection_provider.getconnection();
		IpdDAO ipdDAO= new JDBCIpdDAO(connection);
		BufferedReader br=request.getReader();
		String line="";
		String inputjson="";
		if((line=br.readLine())!=null){
			inputjson=inputjson+line;
		}
		JSONObject jsonObject= new JSONObject(inputjson);
		Master master= new Master();
		master.setName(jsonObject.getString("name"));
		master.setText(jsonObject.getString("text"));
		String boxId=jsonObject.getString("boxId");
		int res=ipdDAO.saveCommonTeplate(master);
		StringBuffer buffer= new StringBuffer();
		ArrayList<Master> list=ipdDAO.commonTemplateList();
		buffer.append("<select style='width:20%' onchange=getTemplateDataByAjax(this.value,'"+boxId+"')  class='form-control'>");
		buffer.append("<option value='0'> Select Template </option>");
		for(Master master2:list){
			buffer.append("<option value='"+master2.getId()+"'> "+master2.getName()+" </option>");
		}
		buffer.append("</select >");
		
		jsonObject.put("list", buffer.toString());
		
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(jsonObject.toString());
	} catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}


public String end()throws Exception{
	 if(!verifyLogin(request)) {
	 		return "login";
	 	}
	     Connection connection=null;
		try {
			connection=Connection_provider.getconnection();
			IpdDAO ipdDAO = new JDBCIpdDAO(connection);
			BedDao bedDao = new JDBCBedDao(connection);
			ClientDAO clientDAO= new JDBCClientDAO(connection);
			
			String sessionadmissionid = ipdForm.getClientip();
			String clietid = ipdForm.getClientid();
			String treatmentepisode = ipdForm.getTreatmentEpisode();
			String isfromdischargedashbaord = request.getParameter("isfromdischargedashbaord");
			Client client= clientDAO.getClientDetails(clietid);
			Bed bed = bedDao.getEditIpdData(sessionadmissionid);
			
			/*String dischargedate = ipdDAO.getIpdDischargeDate(treatmentepisode);
			*/
			String dischargedate= DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone());
			
			 int res = bedDao.updateBedStatus(sessionadmissionid);
			 int log = bedDao.saveDischargeLog(sessionadmissionid,clietid,dischargedate,bed.getBedid(),loginInfo);
			 
			 //set treatment sttaus
			 int update = ipdDAO.updateDischaregeStatus(treatmentepisode,dischargedate);
			 
			 //update in process charges
			 update = ipdDAO.updateInProcessCharges(sessionadmissionid);
			  int rest=bedDao.updateUseridInTable(loginInfo.getUserId(), bed.getTreatmentepisodeid(), "endeduserid");
			
			  
			  DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			    Calendar cal = Calendar.getInstance();
			    
			    String dtt = dateFormat.format(cal.getTime());
			  SmsService s = new SmsService();
			  String msg=client.getFullname()+" : You have successfully completed discharge process on :"+dtt;
			  if(loginInfo.isDischarge_msg_hs()){
				  s.sendSms(msg, client.getMobNo(), loginInfo, new EmailLetterLog());
			  }
			 
			 if(DateTimeUtils.isNull(isfromdischargedashbaord).equals("1")){
				 return "dishchargedashboard";
			 }
			  
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally{
			connection.close();
		}
		 return "ipddashboard";
}


public String email() throws Exception{
	if(!verifyLogin(request)){
		return "login";
	}
	Connection connection = null;
	
	try{
		int loginId = loginInfo.getId();
		connection = Connection_provider.getconnection();
		AccountsDAO accountsDAO = new JDBCAccountsDAO(connection);
		
		BufferedReader br=request.getReader();
		String line="";
		String inputjson="";
		if((line=br.readLine())!=null){
			inputjson=inputjson+line;
		}
		JSONObject jsonObject= new JSONObject(inputjson);
		
		String id = request.getParameter("id");
		String to = request.getParameter("to");
		String cc = request.getParameter("cc");
		String subject = request.getParameter("subject");
		String notes = request.getParameter("notes");
		
		
		id=jsonObject.getString("id");
		to=jsonObject.getString("to");
		cc=jsonObject.getString("cc");
		subject=jsonObject.getString("subject");
		notes=jsonObject.getString("notes");
		String filename1="";
		String filename = "";
		if((filename).contains("/")){
			filename = (String)session.getAttribute("pdfFileName");
			String[] temp1 = filename.split("/");
			 filename1 = temp1[1];
		}
		
		
		int invoiceid = 0;
		if(session.getAttribute("chargesInvoiceid")!=null){
			invoiceid=(Integer)session.getAttribute("chargesInvoiceid");
		}
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String d1 = dateFormat.format(date);
		String[] temp = d1.split("\\s+");
		String date1 = temp[0];
		String time = temp[1];
		String clientid = request.getParameter("clientid");
		String type = request.getParameter("type");
		 String pdffilename="";
		clientid=jsonObject.getString("clientid");
		type=jsonObject.getString("type");
		String filePath="";
		{
			  filePath = request.getRealPath("/liveData/document/");
				//String filename = "Invoice_"+clientid+"_"+invoiceid+".pdf";
			pdffilename = "Invoice_"+invoiceid+".pdf";
			 String filedata=""+jsonObject.getString("htmlContent");
			 filedata=filedata.replaceAll("[\\r\\n\\t]+", "");
			 StringBuffer buffer= new StringBuffer();
			 buffer.append("<html><body><h2>Hello</h2></body></html>");
			//s filedata=buffer.toString();
			 htmlToPdfFile(filedata, filePath, pdffilename);
			 filename=pdffilename;	
			  session.setAttribute("pdfFileName", filePath+"/"+pdffilename);
				
		}
		
		
		
		int result = accountsDAO.saveEmailLogDetails(to, cc, subject, notes, filePath+"/"+pdffilename,invoiceid,DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()),time,type);
		
		//set deliver status
		int update = accountsDAO.updateDeliverStatus(invoiceid,"2");
		String status = "Email";
		//int upPaymentStatus = accountsDAO.updatePaymentDeliverStatus(id,status);
		
		EmailLetterLog emailLetterLog = new EmailLetterLog();
		emailLetterLog.setClientId(clientid);
		emailLetterLog.setType(status);
		
		EmbeddedImageEmailUtil.sendMailAttachment(connection,loginId,to, cc, subject, notes, filePath+"/"+pdffilename,loginInfo,emailLetterLog,"0");
		response.setContentType("application/json");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write(jsonObject.toString());
		
		
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		
		connection.close();
	}
	
return null;
}
public void htmlToPdfFile(String htmlString, String filepath,
		String fileaName) throws Exception {
	
	
		try {
			

			CYaHPConverter converter = new CYaHPConverter();
			File fout = new File(filepath + "/" + fileaName);
			FileOutputStream out = new FileOutputStream(fout);
			Map properties = new HashMap();
			ArrayList headerFooterList = new ArrayList();

			properties.put(IHtmlToPdfTransformer.PDF_RENDERER_CLASS,
					IHtmlToPdfTransformer.FLYINGSAUCER_PDF_RENDERER);
			// properties.put(IHtmlToPdfTransformer.FOP_TTF_FONT_PATH, fontPath);
			converter.convertToPdf(htmlString, IHtmlToPdfTransformer.A4P,
					(java.util.List) headerFooterList, "file:///temp/", // root for
																		// relative
																		// external
																		// CSS and
																		// IMAGE
					out, properties);
			out.flush();
			out.close();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
}


public String saveToPdf(){
	try{
		String inputfile="3212_IPD.pdf";
		String  uploadDirName = request.getRealPath("/liveData/document/");
		File f=new File(inputfile);
		FileInputStream fis=null;
		FileOutputStream fo=null;
		File f1=new File(uploadDirName+"/"+f.getName());
		
		fis=new FileInputStream(f);
		fo=new FileOutputStream(f1);
		
		
		try
		{
		byte buf[] = new byte[1024*8]; /* declare a 8kB buffer */
		int len = -1;
		while((len = fis.read(buf)) != -1)
		{
		fo.write(buf, 0, len);
		}
		}
		catch(Exception e)
		{
		e.printStackTrace();
		}
		
		
		
		
		
		
	}catch (Exception e) {
		e.printStackTrace();
	}
	return null;
}


}
