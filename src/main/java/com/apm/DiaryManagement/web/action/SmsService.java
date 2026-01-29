package com.apm.DiaryManagement.web.action;







import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.apm.Log.eu.entity.EmailLetterLog;
import com.apm.Master.eu.bi.MasterDAO;
import com.apm.Master.eu.blogic.jdbc.JDBCMasterDAO;
import com.apm.common.eu.blogic.jdbc.Connection_provider;
import com.apm.common.web.action.LoginAction;
import com.apm.common.web.common.helper.LoginHelper;
import com.apm.common.web.common.helper.LoginInfo;


public final class SmsService {
	HttpServletRequest request=ServletActionContext.getRequest();
	HttpServletResponse response=ServletActionContext.getResponse();
	HttpSession session=request.getSession(false);
	LoginInfo loginInfo=LoginHelper.getLoginInfo(request);

	private static final Logger log = Logger.getLogger(SmsService.class);
	/** SMS Account User Id */
	//private final String SMS_ACC_USER_ID = "5611";
	private final String SMS_ACC_USER_ID = "GMSOFT";
	
	/** SMS Account Password */
	private final String SMS_ACC_PASSWORD = " l41P$Nv!";
	
	/** SMS Account Sender Id */
	private final String SMS_ACC_SENDER_ID = "GMSOFT";

	/** SMS URL Host */
	private final String SMS_URL_HOST = "http://www.smsjust.com/blank/sms/user/urlsms.php?";
	//private final String SMS_URL_HOST = "http://localhost:8080/api/pushsms.php?";
	
	/** SMS Sending URL Query string
	{0} - Recipients Mobile Number (multiple number can be added by separating with comma)
	{1} - Message Text  */
	/*private final String SMS_URL_QUERY = "username="+ SMS_ACC_USER_ID +
										 "&pass="+ SMS_ACC_PASSWORD +
										 "&senderid="+ SMS_ACC_SENDER_ID+
										 "&dest_mobileno={0}&message={1}&response=Y"; */
	private final String SMS_URL_QUERY = "username="+ SMS_ACC_USER_ID +
									 "&pass="+ SMS_ACC_PASSWORD +
									 "&senderid="+ SMS_ACC_SENDER_ID +
									 "&dest_mobileno={0}&message={1}&response=Y"; 
	
	/** SMS Url */
	private final String SMS_URL = SMS_URL_HOST + SMS_URL_QUERY;
	
	/** URL Encoder for white space */
	private final String ENCODE_SPACE = "%20";
	
	/** URL Encoder for comma */
	private final String ENCODE_COMMA = "%2C";
	
	/** URL Encoder for line feed (new line) */
	private final String ENCODE_LF = "%0A";
	
	
	/**
	 * Default constructor, Don't make it public or private
	 * It should be visible only within package so keep visibility default
	 */
	public SmsService(){ }
	
	/**
	 * Send sms by using sms provider
	 * 
	 * @param mobileNo mobile number to send sms
	 * @param messageText message text
	 * @return true if sms sent successfully, else false
	 */
	public synchronized boolean sendSms(String messageText, String  mobileNo, LoginInfo loginInfo, EmailLetterLog emailLetterLog){
		
		/*messageText = "Ã Â¤â€¢Ã Â¥ï¿½Ã Â¤Â¯Ã Â¤Â¾ Ã Â¤Â¹Ã Â¤Â¾Ã Â¤Â² Ã Â¤Â¹Ã Â¥Ë†";
		mobileNo = "9156248809";*/
		boolean isSmsSent = false;		// initially sms sent is false
		
		// replace query parameters with actual mobile number and message text
		String smsUrl = SMS_URL.replace("{0}", mobileNo).replace("{1}", messageText);
		
		// encode url by replacing space, comma and new line with corresponding encoding code
		smsUrl = smsUrl.replace(" ", ENCODE_SPACE).replace(",", ENCODE_COMMA).replace("\n", ENCODE_LF);
		
		try{
			
			
			URL url = new URL(smsUrl);	// get url from url string
			
			URLConnection urlConnection = url.openConnection();	// open url connection
			urlConnection.connect();	// connect to url
			
			HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;	// get http url connection object from url connection
			int previouscount = getsmsbalance();
			int responseCode = httpConnection.getResponseCode();	// get response code
			 /*String data ="";
			 if (200 == responseCode) {
				 	InputStream inputStream = httpConnection.getInputStream();
			        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
	
				    StringBuilder response = new StringBuilder();
				    String currentLine;
	
				    while ((currentLine = in.readLine()) != null) 
				        response.append(currentLine);
				    data = response.toString();
			 } */
			
			log.debug("sms@@@@@@@@@@"+ responseCode );
			log.debug("sms@@@@@@@@@@"+ urlConnection );
			
			if(responseCode == HttpURLConnection.HTTP_OK){	// if response code is HTTP+OK then message sent successfully 
				isSmsSent = true;
				/*int aftercount = getsmsbalance();*/
			   /* int finalcount= previouscount -aftercount;*/
				Connection connection=null; 
				try {
					
					connection=Connection_provider.getconnection();
					MasterDAO masterDAO=new JDBCMasterDAO(connection);
					
					/*DateFormat dateFormat = new SimpleDateFormat("MM");
				    Calendar cal = Calendar.getInstance();
				    String month = dateFormat.format(cal.getTime());
				  
				    DateFormat dateFormat1 = new SimpleDateFormat("yyyy");
				    Calendar cal1 = Calendar.getInstance();
				    String year = dateFormat1.format(cal1.getTime());*/
				    
				   /* if(finalcount>0){
				    	 int ress = masterDAO.updateSMSMonthCount(month,year,finalcount);
				    }*/
					int lastcount = masterDAO.getlastcountsms();
					
					lastcount = lastcount+1;
					
					int res=masterDAO.updateSMSCounter(lastcount,messageText);
					
					
				} catch (Exception e) {

					
					e.printStackTrace();
					
				}
				finally {
					connection.close();
				}
				
				
				
			}
			
		}catch (Exception e) {
			log.debug("sms@@@@@@@@@@"+ e.getMessage() );
			
			e.printStackTrace();
			log.debug("sms@@@@@@@@@@"+ e.getMessage() );
		}
		
		return isSmsSent;
		
	}
	
	public static void main(String[] args) {
		SmsService s = new SmsService();
		String sms="&#2310;&#2346;&#2325;&#2366;&#2354;&#2360;&#2368;&#2325;&#2352;&#2339; &#2342;&#2367;&#2344;&#2366;&#2306;&#2325; &#2407;&#2411;-&#2406;&#2415;-&#2408;&#2406;&#2407;&#2414; &#2325;&#2379; &#2344;&#2367;&#2352;&#2381;&#2343;&#2366;&#2352;&#2367;&#2340;&#2325;&#2367;&#2351;&#2366;&#2327;&#2351;&#2366; &#2361;&#2376;";
		String msg = "Appointment Cancelled on 01-10-2015 at 10:05 AM with MyClinic 3 Bond Street Bond Gate BondStreet 02476641214 ";
		String msgtext = "Thanks for registring in fitfull Gym.";
		s.sendSms(msgtext, "9579037238", new LoginInfo(),new EmailLetterLog());
		//int ss=s.testmethod();
	}
	
	private int getsmsbalance() {
		int res =1;
		try {
			String smsUrl1 = "http://www.smsjust.com/sms/user/balance_check.php?username="+SMS_ACC_USER_ID+"&pass="+SMS_ACC_PASSWORD+"";
			URL url1 = new URL(smsUrl1);	// get url from url string
			URLConnection urlConnection1 = url1.openConnection();	// open url connection
			urlConnection1.connect();	// connect to url
			HttpURLConnection httpConnection1 = (HttpURLConnection) urlConnection1;	// get http url connection object from url connection
			int responseCode1 = httpConnection1.getResponseCode();
			InputStream inputStream;
		    if (200 == responseCode1) {
		        inputStream = httpConnection1.getInputStream();
		        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

			    StringBuilder response = new StringBuilder();
			    String currentLine;

			    while ((currentLine = in.readLine()) != null) 
			        response.append(currentLine);
			    String data = response.toString().split(":")[1];
			    res = Integer.parseInt(data);
		    } 

		    
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public synchronized boolean sendvaccineSms( String messageText, String  mobileNo, LoginInfo loginInfo, EmailLetterLog emailLetterLog,Connection con){
		
		boolean isSmsSent = false;		// initially sms sent is false
		
		try{
			
			//messageText=URLEncoder.encode(messageText, "UTF-8");
			
			// replace query parameters with actual mobile number and message text
			String smsUrl = SMS_URL.replace("{0}", mobileNo).replace("{1}", messageText)+"&msgtype=UNI";
			
			// encode url by replacing space, comma and new line with corresponding encoding code
			smsUrl = smsUrl.replace(" ", ENCODE_SPACE).replace(",", ENCODE_COMMA).replace("\n", ENCODE_LF);
			
			URL url = new URL(smsUrl);	// get url from url string
			
			URLConnection urlConnection = url.openConnection();	// open url connection
			urlConnection.connect();	// connect to url
			
			HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;	// get http url connection object from url connection
			int previouscount = getsmsbalance();	
			int responseCode = httpConnection.getResponseCode();	// get response code
         	
			if(responseCode == HttpURLConnection.HTTP_OK){	// if response code is HTTP+OK then message sent successfully 
				isSmsSent = true;
				int aftercount = getsmsbalance();
				int finalcount= previouscount -aftercount;
				Connection connection=null; 
				try {
					connection=con;
					MasterDAO masterDAO=new JDBCMasterDAO(connection);
					DateFormat dateFormat = new SimpleDateFormat("MM");
					Calendar cal = Calendar.getInstance();
					String month = dateFormat.format(cal.getTime());
					  
					DateFormat dateFormat1 = new SimpleDateFormat("yyyy");
					Calendar cal1 = Calendar.getInstance();
					String year = dateFormat1.format(cal1.getTime());
					if(finalcount>0){
				    	int ress = masterDAO.updateSMSMonthCount(month,year,finalcount);
				    }
					int res=masterDAO.updateSMSCounter(1,messageText);
				
					
				} catch (Exception e) {

					e.printStackTrace();
				}
				
				
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return isSmsSent;
		
	}
	

	
}
