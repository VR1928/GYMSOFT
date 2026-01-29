package com.apm.DiaryManagement.eu.bi;

import java.util.ArrayList;

import com.apm.DiaryManagement.eu.entity.NotAvailableSlot;
import com.apm.DiaryManagement.web.form.NotAvailableSlotForm;

public interface FinderDAO {

	ArrayList<NotAvailableSlot> getFinderList(String clientId,String date);

}
