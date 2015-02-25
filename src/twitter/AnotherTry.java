package twitter;
import javax.mail.*; 
import javax.mail.internet.*; 

import java.util.*;
import java.io.*; 
public class AnotherTry {
	private static final String SMTP_HOST_NAME = "smtp.gmail.com"; 
	private static final String SMTP_ANTH_USER = "qyyzhong@gmail.com";
	private static final String SMTP_AUTH_PWD ="13992853798";
	private static final String emailMsgTxt = "Body";
	private static final String emailSubjectTxt="Subject";
	private static final String emailFromAddress = "qyyzhong@gmail.com";
	
	private static final String[] emailList = {"tr2400@columbia.edu"};
	public static void main(String args[]) throws Exception {
		AnotherTry smtpMailSender = new AnotherTry();
		smtpMailSender.postMail( emailList, emailSubjectTxt, emailMsgTxt, emailFromAddress);
		
	}
	public void postMail(String recipients[], String subject, String message, String from) throws MessagingException {
		boolean debug = false;
		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "false");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.port", "465");
		Authenticator auth = new SMTPAuthenticator();
		Session session = Session.getDefaultInstance(props, auth);
		session.setDebug(debug);
		Message msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);
		InternetAddress[] addressTo = new InternetAddress[recipients.length]; 
		for (int i = 0; i < recipients.length; i++){
			addressTo[i] = new InternetAddress(recipients[i]);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo); 
		msg.setSubject(subject);
		msg.setContent(message, "text/plain");
		Transport.send(msg);
	}
	
	private class SMTPAuthenticator extends javax.mail.Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			String username = SMTP_ANTH_USER;
			String password = SMTP_AUTH_PWD;
			return new PasswordAuthentication(username, password);
		}
	}
	
}