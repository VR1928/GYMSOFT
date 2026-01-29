package com.apm.Master.eu.bi;

import java.util.ArrayList;
import java.util.List;

import com.apm.Master.eu.entity.Master;

public interface DischargeOutcomeDAO {

      	public ArrayList<Master> getAllDischargeOutcome();
      	public int addDischargeOutcome(Master master);
      	public Master getMaster(int id);
      	public int updateDischargeOutcome(Master master);
      	public int deleteDischargeOutcome(int id);
      	public ArrayList<Master> getMasterList();
		public ArrayList<Master> getNewChargeTypeList();
		public ArrayList<Master> getNewChargeTypeListProc();
}
