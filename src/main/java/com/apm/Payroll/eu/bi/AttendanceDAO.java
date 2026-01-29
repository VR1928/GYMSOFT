package com.apm.Payroll.eu.bi;

import java.util.ArrayList;

import com.apm.Payroll.eu.entity.Attendance;
import com.apm.common.utils.Pagination;
import com.apm.common.web.common.helper.LoginInfo;

public interface AttendanceDAO {

	int addOrUpdateWorkSheet(Attendance attendance);

	ArrayList<Attendance> getAllAttendanceList(String month, int weekno,String branchid, Pagination pagination, String searchemp, LoginInfo loginInfo, String numdate, int daysInMonth);
    int getTotalHours(String month,String emp_id);

	String getSalaryForAttendence(String emp_id);

	String totalSalaryForAttendence(String emp_id);

	int getAllAttendanceListcount(String month, int weekno, String branchid, String searchemp, LoginInfo loginInfo, String numdate, int daysInMonth);

	Attendance getListOfAttendance(String today, String userid, String empid);

	int insertPunchIn(String empid, String userid, String intime, String date, int status, String remark);

	boolean checktodaysentry(String today, LoginInfo loginInfo);

	int updatePunching(String empid, String userid, String intime, String date1, int status, String columnname, String remarkcol, String remark);
	
	String differenceOftwoTimes(String dateStart,String dateStop);

	ArrayList<Attendance> getAllAttendanceListOfSelf(String attdate, String newmonth, String year, LoginInfo loginInfo, String empname);

	ArrayList<Attendance> getMonthWiseReport(String empid, String empname, String month, String year, int daysInMonth, int daysofMonth, Pagination pagination);

	int getMonthWiseReportCount(String empid, String empname, String tmp, String year, int daysInMonth, int dayOfMonth);

	int changepunchtime(String id, String val, String colname, Attendance attendance);

	Attendance getDayDetails(String id);
	
	
}
