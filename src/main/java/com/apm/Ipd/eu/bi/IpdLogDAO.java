package com.apm.Ipd.eu.bi;

import java.util.ArrayList;

import com.apm.Ipd.eu.entity.Bed;

public interface IpdLogDAO {

	ArrayList<Bed> getAdmissionlogDAO(String clientId);

	ArrayList<Bed> getDischargeLog(String clientId);

	ArrayList<Bed> getBedChangeLogList(String clientId,String admissionid);

	String getDischargeBedId(String admissionid);

	boolean isBedChanged(String admissionid,String clientid);

	String getDischargeDate(int ipdid);

}
