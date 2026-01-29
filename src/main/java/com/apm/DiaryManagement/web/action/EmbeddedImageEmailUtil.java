package com.apm.DiaryManagement.web.action;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.apm.Log.eu.bi.EmailLetterLogDAO;
import com.apm.Log.eu.blogic.jdbc.JDBCEmailLetterLogDAO;
import com.apm.Log.eu.entity.EmailLetterLog;
import com.apm.Registration.eu.bi.ClinicDAO;
import com.apm.Registration.eu.blogic.jdbc.JDBCClinicDAO;
import com.apm.Report.eu.bi.ClientReportDAO;
import com.apm.Report.eu.blogic.jdbc.JDBCClientReportDAO;
import com.apm.common.eu.blogic.jdbc.Connection_provider;
import com.apm.common.utils.DateTimeUtils;
import com.apm.common.web.action.BaseAction;
import com.apm.common.web.common.helper.LoginHelper;
import com.apm.common.web.common.helper.LoginInfo;
import com.opensymphony.xwork2.ActionContext;
 
/**
 * This utility class provides a functionality to send an HTML e-mail message
 * with embedded images.
 * @author www.codejava.net
 *
 */
public class EmbeddedImageEmailUtil{
	
	private static final Logger log = Logger.getLogger(EmbeddedImageEmailUtil.class);
	
	//static HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
//	static LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
 
    /**
     * Sends an HTML e-mail with inline images.
     * @param host SMTP host
     * @param port SMTP port
     * @param userName e-mail address of the sender's account
     * @param password password of the sender's account
     * @param toAddress e-mail address of the recipient
     * @param subject e-mail subject
     * @param htmlBody e-mail content with HTML tags
     * @param mapInlineImages
     *          key: Content-ID
     *          value: path of the image file
     * @throws AddressException
     * @throws MessagingException
     */
	
	
    public static void send(Connection connection, int id,String to, String cc,
            String subject, String htmlBody,
            Map<String, String> mapInlineImages, LoginInfo loginInfo)
                throws AddressException, MessagingException {
    	
    	StringBuffer str = new StringBuffer();
    	str.append(htmlBody);
    	
    	
        // sets SMTP server properties
    	/*final String userName = "cbstecdemotest@gmail.com";
		final String password = "cbstech1234";*/
    	final String userName = loginInfo.getEmailUserName();
		final String password = loginInfo.getEmailPassword();
        Properties properties = new Properties();
        properties.put("mail.smtp.host", loginInfo.getEmailHostName());
        properties.put("mail.smtp.port", 25);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", userName);
        properties.put("mail.password", password);
       
       // Connection connection = null;
       
        try{
        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);
        Message msg = new MimeMessage(session);
        
        Address[] toUsers = InternetAddress.parse(to);
        msg.addRecipients(Message.RecipientType.TO, toUsers);
        
        Address[] ccUsers = InternetAddress.parse(cc);
        msg.addRecipients(Message.RecipientType.CC, ccUsers);
        
 
        // creates a new e-mail message
        
 
        msg.setFrom(new InternetAddress(userName));
        InternetAddress[] toAddresses = { new InternetAddress(to) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject(subject);
        msg.setSentDate(new Date());
 
        // creates message part
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setContent(str.toString(), "text/html");
 
        // creates multi-part
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
 
        // adds inline image attachments
        if (mapInlineImages != null && mapInlineImages.size() > 0) {
            Set<String> setImageID = mapInlineImages.keySet();
             
            for (String contentId : setImageID) {
                MimeBodyPart imagePart = new MimeBodyPart();
                imagePart.setHeader("Content-ID", "<" + contentId + ">");
                imagePart.setDisposition(MimeBodyPart.INLINE);
                 
                String imageFilePath = mapInlineImages.get(contentId);
                try {
                    imagePart.attachFile(imageFilePath);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
 
                multipart.addBodyPart(imagePart);
            }
        }
 
        msg.setContent(multipart);        
		
		//connection = Connection_provider.getconnection();		
        ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
        String CheckMailToSend = clinicDAO.IsMailSend(loginInfo.getClinicid());
        if(CheckMailToSend.equals("true")){
      
        	Transport.send(msg);
        	
        	System.out.println("email sent");
        }else{
        	
        	
        	System.out.println("sorry..email not sent");
        }
		}catch(Exception e){
			e.printStackTrace();
			log.debug("*******************"+e.getMessage());
		}
        
    }
    
    
    
    public static void sendMailAttachment(Connection connection,int id,String to,String cc,String subject,String bodyText,String filename, LoginInfo loginInfo, EmailLetterLog emailLetterLog,String procurementid) throws Exception{
    	
    /*	StringBuffer str = new StringBuffer();
    	str.append("<br><br><a href='www.twitter.com'><img src='img/Entypo_f309(0)_32.png'></a>&nbsp;&nbsp;<a href='www.facebook.com'><img src='img/Entypo_f30c(0)_32.png'></a>&nbsp;&nbsp;<a href='www.gmail.com'><img src='img/Entypo_f30f(0)_32.png'></a>&nbsp;<br>");
    	bodyText.concat(str.toString());*/
    	String from = loginInfo.getEmailUserName();
    	//String from ="ruchita.ghugal@pranam.co.in";
    	
    	
    	/* String from = "cbstecdemotest@gmail.com";
         String to = "manojmishra443@gmail.com";
         String subject = "Important Message";
         String bodyText = "This is a important message with attachment";
         String filename = "c:/hello.pdf";*/
    	
    	/*final String userName = "cbstecdemotest@gmail.com";
		final String password = "cbstech1234";*/
    	
    	final String userName = loginInfo.getEmailUserName();
    	final String password =loginInfo.getEmailPassword(); //
        Properties properties = new Properties();
        properties.put("mail.smtp.host", loginInfo.getEmailHostName());
      // properties.put("mail.smtp.host", "mail.pranam.co.in");
        properties.put("mail.smtp.port", 25);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.user", userName);
        properties.put("mail.password", password);
        
        
        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
        Session session = Session.getInstance(properties, auth);
        
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(userName));
            //message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            //message.addRecipient(Message.RecipientType.TO, new InternetAddress(to)); 
           
            Address[] toUsers = InternetAddress.parse(to);
            message.addRecipients(Message.RecipientType.TO, toUsers);
            
            Address[] ccUsers = InternetAddress.parse(cc);
            message.addRecipients(Message.RecipientType.CC, ccUsers);
            
            message.setSubject(subject);
            message.setSentDate(new Date());
           
            //
            // Set the email message text.
            //
            MimeBodyPart messagePart = new MimeBodyPart();
          //  messagePart.setText(bodyText);
            messagePart.setContent(bodyText,"text/html");
            
            //
            // Set the email attachment file
            //
            MimeBodyPart attachmentPart = new MimeBodyPart();
            FileDataSource fileDataSource = new FileDataSource(filename) {
                @Override
                public String getContentType() {
                    return "application/octet-stream";
                }
            };
            attachmentPart.setDataHandler(new DataHandler(fileDataSource));
            attachmentPart.setFileName(filename);
            
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messagePart);
            multipart.addBodyPart(attachmentPart);
            
            message.setContent(multipart);
            
           // connection = Connection_provider.getconnection();
    		ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
    		EmailLetterLogDAO emailLetterLogDAO = new JDBCEmailLetterLogDAO(connection);
          	String status = "Not Delivered";
            String CheckMailToSend = clinicDAO.IsMailSend(loginInfo.getClinicid());
            emailLetterLog.setLastModified(DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
            if(CheckMailToSend.equals("true")){          
            	Transport.send(message);            	
            	System.out.println("pdf sent");
            	status = "Delivered"; 
            	
              	int result = emailLetterLogDAO.saveEmailLetterLogDetails(from, to, subject,emailLetterLog,status,procurementid);        
          	}else{
                System.out.println("sorry..pdf not sent");
               //save email letter log method
              
                int result = emailLetterLogDAO.saveEmailLetterLogDetails(from, to, subject, emailLetterLog,status,procurementid);            
            }
    		}catch(Exception e){
    			e.printStackTrace();
    			log.debug("*******************"+e.getMessage());
    		}
        
    }
    
    
    public static void main(String[] args) throws AddressException, MessagingException,Exception {
    	  Map<String, String> inlineImages = new HashMap<String, String>();
 	     inlineImages.put("image1", "c:/p-icon.png");
 	     Connection connection = null;
		try {
			connection = Connection_provider.getconnection();
			HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			int id = loginInfo.getId();
			EmailLetterLog emailLetterLog = new EmailLetterLog();
		
    	EmbeddedImageEmailUtil.sendMail(connection,id,"manojmishra443@gmail.com", "manojmishra443@gmail.com", "hi", "hello", loginInfo,emailLetterLog);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}


    

    public static void sendMail(Connection connection,int id,String to, String cc, String subject,
			String notes, LoginInfo loginInfo, EmailLetterLog emailLetterLog) throws Exception {
    	log.debug("********************hello world7");
    	
    	// Connection connection = null;
		 //String to = "destinationemail@gmail.com";
    	  // Sender's email ID needs to be mentioned
    	  String from = loginInfo.getEmailUserName();
	      final String username = loginInfo.getEmailUserName();//change accordingly
	      final String password = loginInfo.getEmailPassword();//change accordingly

    	
	    
	      // Assuming you are sending email through relay.jangosmtp.net
	      String host = loginInfo.getEmailHostName();

	      Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "true");
	      props.put("mail.smtp.host", host);
	      props.put("mail.smtp.port", "25");

	      // Get the Session object.
	      Session session = Session.getInstance(props,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(username, password);
	            }
		});

	      try {
	            // Create a default MimeMessage object.
	            Message message = new MimeMessage(session);

	   	   // Set From: header field of the header.
		   message.setFrom(new InternetAddress(from));

		   // Set To: header field of the header.
		   message.setRecipients(Message.RecipientType.TO,
	              InternetAddress.parse(to));

		   // Set Subject: header field
		   message.setSubject(subject);

		   // Send the actual HTML message, as big as you like
		   message.setContent(notes,"text/html");

		   // Send message
		 //  connection = Connection_provider.getconnection();		   
   			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
   			EmailLetterLogDAO emailLetterLogDAO = new JDBCEmailLetterLogDAO(connection);
   			String status = "Not Delivered";
   			String CheckMailToSend = clinicDAO.IsMailSend(loginInfo.getClinicid());
   		    emailLetterLog.setLastModified(DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
   			if(CheckMailToSend.equals("true")){
   				log.debug("********************hello world8");
   				Transport.send(message);           	
   				System.out.println("email sent");
   				status = "Delivered"; 
   				
   				int result = emailLetterLogDAO.saveEmailLetterLogDetails(from, to, subject, emailLetterLog,status,"0");    
      		 }else{
      			 
      			System.out.println("sorry..email not sent");
               	 //save email letter log method
      			
              	int result = emailLetterLogDAO.saveEmailLetterLogDetails(from, to, subject, emailLetterLog,status,"0");        
      		 }

	      } catch (MessagingException e) {
		   e.printStackTrace();
		   throw new RuntimeException(e);
	      } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.debug("*******************"+e.getMessage());
		}
		
	}
    
    
    public static void sendMailAdvance(Connection connection,int id,String to, String cc, String subject,
			String notes, LoginInfo loginInfo, EmailLetterLog emailLetterLog) throws Exception {
    	log.debug("********************hello world7");
    	
    	// Connection connection = null;
		 //String to = "destinationemail@gmail.com";
    	  // Sender's email ID needs to be mentioned
    	  String from = loginInfo.getEmailUserName();
	      final String username = loginInfo.getEmailUserName();//change accordingly
	      final String password = loginInfo.getEmailPassword();//change accordingly

    	
	    
	      // Assuming you are sending email through relay.jangosmtp.net
	      String host = loginInfo.getEmailHostName();

	      Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "true");
	      props.put("mail.smtp.host", host);
	      props.put("mail.smtp.port", "25");

	      // Get the Session object.
	      Session session = Session.getInstance(props,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(username, password);
	            }
		});
	      
	      ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
	      EmailLetterLogDAO emailLetterLogDAO = new JDBCEmailLetterLogDAO(connection);

	      try {
	            // Create a default MimeMessage object.
	            Message message = new MimeMessage(session);

	   	   // Set From: header field of the header.
		   message.setFrom(new InternetAddress(from));

		   // Set To: header field of the header.
		   message.setRecipients(Message.RecipientType.TO,
	              InternetAddress.parse(to));

		   // Set Subject: header field
		   message.setSubject(subject);

		   // Send the actual HTML message, as big as you like
		   message.setContent(notes,"text/html");

		   // Send message
		 //  connection = Connection_provider.getconnection();		   
   			
   			String status = "Not Delivered";
   			String CheckMailToSend = clinicDAO.IsMailSend(id);
   		    emailLetterLog.setLastModified(DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
   			if(CheckMailToSend.equals("true")){
   				log.debug("********************hello world8");
   				Transport.send(message);           	
   				System.out.println("email sent");
   				status = "Delivered"; 
   				
   				int result = emailLetterLogDAO.saveEmailLetterLogDetails(from, to, subject, emailLetterLog,status,"0");   
   				int updatestatus = emailLetterLogDAO.updateAdEmailStatus(emailLetterLog);
      		 }else{
      			 
      			System.out.println("sorry..email not sent");
               	 //save email letter log method
      			
              	int result = emailLetterLogDAO.saveEmailLetterLogDetails(from, to, subject, emailLetterLog,status,"0");        
      		 }

	      } catch (MessagingException e) {
	    	  
	    	  int result = emailLetterLogDAO.saveEmailLetterLogDetails(from, to, subject, emailLetterLog,"Not Delivered","0"); 
	    	  
	    	  e.printStackTrace();
		   throw new RuntimeException(e);
	      } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.debug("*******************"+e.getMessage());
		}
		
	}
    
    public static void sendMailForForgotPassword(String to, String cc, String subject,
			String notes){
    	
    	  String from = "care@ashwinikidneycentre.com";
	      final String username = "care@ashwinikidneycentre.com";//change accordingly
	      final String password = "K1dney@121";//change accordingly

	      // Assuming you are sending email through relay.jangosmtp.net
	      String host = "mail.ashwinikidneycentre.com";

	      Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "true");
	      props.put("mail.smtp.host", host);
	      props.put("mail.smtp.port", "25");

	      // Get the Session object.
	      Session session = Session.getInstance(props,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(username, password);
	            }
		});

	      try {
	            // Create a default MimeMessage object.
	            Message message = new MimeMessage(session);

	   	   // Set From: header field of the header.
		   message.setFrom(new InternetAddress(from));

		   // Set To: header field of the header.
		   message.setRecipients(Message.RecipientType.TO,
	              InternetAddress.parse(to));

		   // Set Subject: header field
		   message.setSubject(subject);

		   // Send the actual HTML message, as big as you like
		   message.setContent(notes,"text/html");

		   // Send message
        
          	Transport.send(message);
          	
          	System.out.println("email sent");
	      }

	      catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			log.debug("*******************"+e.getMessage());
		}
    	
    }
    
    //@ruchi forgot password with new credentials
    
    public static void sendMailForForgotPasswordNew(String to, String cc, String subject,
			String notes){
    	
    	  String from = "info@EscapeQ.com";
	      final String username = "info@EscapeQ.com";//change accordingly
	      final String password = "H1nckley&01";//change accordingly

	      // Assuming you are sending email through relay.jangosmtp.net
	      String host = "mail.EscapeQ.com";

	      Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "true");
	      props.put("mail.smtp.host", host);
	      props.put("mail.smtp.port", "25");

	      // Get the Session object.
	      Session session = Session.getInstance(props,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(username, password);
	            }
		});

	      try {
	            // Create a default MimeMessage object.
	            Message message = new MimeMessage(session);

	   	   // Set From: header field of the header.
		   message.setFrom(new InternetAddress(from));

		   // Set To: header field of the header.
		   message.setRecipients(Message.RecipientType.TO,
	              InternetAddress.parse(to));

		   // Set Subject: header field
		   message.setSubject(subject);

		   // Send the actual HTML message, as big as you like
		   message.setContent(notes,"text/html");

		   // Send message
        
          	Transport.send(message);
          	
          	System.out.println("email sent");
	      }

	      catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			log.debug("*******************"+e.getMessage());
		}
    	
    }
    
    
    public static void sendOtpMail(String to, String cc, String subject,
			String notes){
    	
    	  String from = "info@pranam.co.in";
	      final String username = "info@pranam.co.in";//change accordingly
	      final String password = "N0gpur&01";//change accordingly

    	  

    	
	      // Assuming you are sending email through relay.jangosmtp.net
	      String host = "mail.pranam.co.in";

	      Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "true");
	      props.put("mail.smtp.host", host);
	      props.put("mail.smtp.port", "25");

	      // Get the Session object.
	      Session session = Session.getInstance(props,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(username, password);
	            }
		});

	      try {
	            // Create a default MimeMessage object.
	            Message message = new MimeMessage(session);

	   	   // Set From: header field of the header.
		   message.setFrom(new InternetAddress(from));

		   // Set To: header field of the header.
		   message.setRecipients(Message.RecipientType.TO,
	              InternetAddress.parse(to));

		   // Set Subject: header field
		   message.setSubject(subject);

		   // Send the actual HTML message, as big as you like
		   message.setContent(notes,"text/html");

		   // Send message
        
          	Transport.send(message);
          	
          	System.out.println("email sent");
	      }

	      catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			log.debug("*******************"+e.getMessage());
		}
    	
    }
    
    
    public static void sendEmrLinkMail(String to, String cc, String subject,
			String notes,LoginInfo loginInfo,EmailLetterLog emailLetterLog,Connection connection){
    	
    	 String from = loginInfo.getEmailUserName();
	      final String username = loginInfo.getEmailUserName();//change accordingly
	      final String password = loginInfo.getEmailPassword();//change accordingly

   	
	    
	      // Assuming you are sending email through relay.jangosmtp.net
	      String host = loginInfo.getEmailHostName();

	      Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "true");
	      props.put("mail.smtp.host", host);
	      props.put("mail.smtp.port", "25");

	      // Get the Session object.
	      Session session = Session.getInstance(props,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(username, password);
	            }
		});

	      try {
	            // Create a default MimeMessage object.
	            Message message = new MimeMessage(session);

	   	   // Set From: header field of the header.
		   message.setFrom(new InternetAddress(from));

		   // Set To: header field of the header.
		   message.setRecipients(Message.RecipientType.TO,
	              InternetAddress.parse(to));

		   // Set Subject: header field
		   message.setSubject(subject);

		   // Send the actual HTML message, as big as you like
		   message.setContent(notes,"text/html");

		   // Send message
        
          	Transport.send(message);
          	
          	String status = "Delivered";
   			EmailLetterLogDAO emailLetterLogDAO = new JDBCEmailLetterLogDAO(connection);
   		    emailLetterLog.setLastModified(DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
   			
   				log.debug("********************hello world8");
   				Transport.send(message);           	
   				System.out.println("email sent");
   				status = "Delivered"; 
   				
   				int result = emailLetterLogDAO.saveEmailLetterLogDetails(from, to, subject, emailLetterLog,status,"0");   
   				
      		 

          	
          	
          	
          	System.out.println("email sent");
	      }

	      catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			log.debug("*******************"+e.getMessage());
		}
    	
    }
    
    public static void sendMailFromSupport(String to, String cc, String subject,
			String notes){
    	
    	  String from = "support@escapeq.com";
	      final String username = "support@escapeq.com";//change accordingly
	      final String password = "Nagpur@202";//change accordingly

    	  

    	
	      // Assuming you are sending email through relay.jangosmtp.net
	      String host = "mail.pranam.co.in";

	      Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "true");
	      props.put("mail.smtp.host", host);
	      props.put("mail.smtp.port", "25");

	      // Get the Session object.
	      Session session = Session.getInstance(props,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(username, password);
	            }
		});

	      try {
	            // Create a default MimeMessage object.
	            Message message = new MimeMessage(session);

	   	   // Set From: header field of the header.
		   message.setFrom(new InternetAddress(from));

		   // Set To: header field of the header.
		   message.setRecipients(Message.RecipientType.TO,
	              InternetAddress.parse(to));

		   // Set Subject: header field
		   message.setSubject(subject);

		   // Send the actual HTML message, as big as you like
		   message.setContent(notes,"text/html");

		   // Send message
        
          	Transport.send(message);
          	
          	System.out.println("email sent");
	      }

	      catch (Exception e) {
			e.printStackTrace();
			
			log.debug("*******************"+e.getMessage());
		}
    	
    }
    
    
    public static void sendMailFromMarketing(String to, String cc, String subject,
			String notes, LoginInfo loginInfo, String allemailds){
    	
    	  String from = loginInfo.getSms_senderid();
    	  final String username = loginInfo.getEmailUserName();
  		  final String password = loginInfo.getEmailPassword();
  		
	      // Assuming you are sending email through relay.jangosmtp.net
	     // String host = "mail.pranam.co.in";
	      String host = loginInfo.getEmailHostName();
	      Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "true");
	      props.put("mail.smtp.host", host);
	      props.put("mail.smtp.port", "25");

	      // Get the Session object.
	      Session session = Session.getInstance(props,
	         new javax.mail.Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	               return new PasswordAuthentication(username, password);
	            }
		});

	      try {
	            // Create a default MimeMessage object.
	            Message message = new MimeMessage(session);

	   	   // Set From: header field of the header.
		   message.setFrom(new InternetAddress(from));

		   // Set To: header field of the header.
		   message.setRecipients(Message.RecipientType.TO,
	              InternetAddress.parse(to));

		   // Set Subject: header field
		   message.setSubject(subject);

		   // Send the actual HTML message, as big as you like
		   message.setContent(notes,"text/html");

		   // Send message
        
          	Transport.send(message);
          	Connection connection = Connection_provider.getconnection();
          	ClientReportDAO clientReportDAO = new JDBCClientReportDAO(connection);
          	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			String todate = dateFormat.format(cal.getTime());
			int result = clientReportDAO.saveMarketingSendHistory(subject,notes,"","email",allemailds,todate,loginInfo.getUserId());
          	//System.out.println("email sent");
	      }

	      catch (Exception e) {
			e.printStackTrace();
			
			log.debug("*******************"+e.getMessage());
		}
    	
    }



	public static void sendMailPayroll(Connection connection, String to, String cc, String subject, String notes,
			LoginInfo loginInfo, EmailLetterLog emailLetterLog) throws Exception {
	    	log.debug("********************hello world7");
	    	
	    	// Connection connection = null;
			 //String to = "destinationemail@gmail.com";
	    	  // Sender's email ID needs to be mentioned
	    	  String from = loginInfo.getEmailUserName();
		      final String username = loginInfo.getEmailUserName();//change accordingly
		      final String password = loginInfo.getEmailPassword();//change accordingly

	    	
		    
		      // Assuming you are sending email through relay.jangosmtp.net
		      String host = loginInfo.getEmailHostName();

		      Properties props = new Properties();
		      props.put("mail.smtp.auth", "true");
		      props.put("mail.smtp.starttls.enable", "true");
		      props.put("mail.smtp.host", host);
		      props.put("mail.smtp.port", "25");

		      // Get the Session object.
		      Session session = Session.getInstance(props,
		         new javax.mail.Authenticator() {
		            protected PasswordAuthentication getPasswordAuthentication() {
		               return new PasswordAuthentication(username, password);
		            }
			});

		      try {
		            // Create a default MimeMessage object.
		            Message message = new MimeMessage(session);

		   	   // Set From: header field of the header.
			   message.setFrom(new InternetAddress(from));

			   // Set To: header field of the header.
			   message.setRecipients(Message.RecipientType.TO,
		              InternetAddress.parse(to));
			   if(!cc.equals("")){
			   message.addRecipient(RecipientType.CC, new InternetAddress(cc));
			   }
			   // Set Subject: header field
			   message.setSubject(subject);

			   // Send the actual HTML message, as big as you like
			   message.setContent(notes,"text/html");

			   // Send message
			 //  connection = Connection_provider.getconnection();		   
	   			ClinicDAO clinicDAO = new JDBCClinicDAO(connection);
	   			EmailLetterLogDAO emailLetterLogDAO = new JDBCEmailLetterLogDAO(connection);
	   			String status = "Not Delivered";
	   			String CheckMailToSend = clinicDAO.IsMailSend(loginInfo.getClinicid());
	   		    emailLetterLog.setLastModified(DateTimeUtils.getUKCurrentDataTime(loginInfo.getTimeZone()));
	   			if(CheckMailToSend.equals("true")){
	   				log.debug("********************hello world8");
	   				Transport.send(message);           	
	   				System.out.println("email sent");
	   				status = "Delivered"; 
	   				
	   				int result = emailLetterLogDAO.saveEmailLetterLogDetails(from, to, subject, emailLetterLog,status,"0");    
	      		 }else{
	      			 
	      			System.out.println("sorry..email not sent");
	               	 //save email letter log method
	      			
	              	int result = emailLetterLogDAO.saveEmailLetterLogDetails(from, to, subject, emailLetterLog,status,"0");        
	      		 }

		      } catch (MessagingException e) {
			   e.printStackTrace();
			   throw new RuntimeException(e);
		      } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				log.debug("*******************"+e.getMessage());
			}
		
		
	}
    
    
}




