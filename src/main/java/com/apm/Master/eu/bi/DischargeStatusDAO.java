package com.apm.Master.eu.bi;

import java.util.ArrayList;

import com.apm.Master.eu.entity.Master;
import com.apm.common.utils.Pagination;

public interface DischargeStatusDAO {

	public ArrayList<Master> getAllDischargeStatus(Pagination  pagination);
  	public int addDischargeStatus(Master master);
  	public Master getMaster(int id);
  	public int updateDischargeStatus(Master master);
  	public int deleteDischargeStatus(int id);
  	public ArrayList<Master> getMasterList();
	public int getTotalCount();
	public String getDischargeStatusById(int statusid);
}
