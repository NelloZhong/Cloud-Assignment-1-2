package twitter;

import java.io.IOException;

import com.amazonaws.auth.AWSCredentials;
//import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.sun.xml.internal.fastinfoset.sax.Properties;

public class SendEmail {
	public static void main(String[] args) throws IOException {
		AWSCredentials credentials = new PropertiesCredentials(DataFetch.class.getResourceAsStream("AwsCredentials.properties"));
		//AWSCredentials credentials = new BasicAWSCredentials( PropertyLoader.getInstance().getAccessKey(), Properties.getInstance().getSecretKey() );
		AmazonSimpleEmailServiceClient sesClient = new AmazonSimpleEmailServiceClient( credentials );
		String subjectText = "Feedback from " + "qianyi";
		Content subjectContent = new Content(subjectText);
		    	
		String bodyText = "Rating: ";
		Body messageBody = new Body(new Content(bodyText));	
		    	
		Message feedbackMessage = new Message(subjectContent,messageBody);
		    	
		String email = "qyyzhong@gmail.com";
		Destination destination = new Destination().withToAddresses("qz2198@columbia.edu");
	//	Destination destination = new Destination().withToAddresses(email);
		SendEmailRequest request = new SendEmailRequest(email,destination,feedbackMessage);
		SendEmailResult result =sesClient.sendEmail(request);
	}
}
