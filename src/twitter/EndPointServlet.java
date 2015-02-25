package twitter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;

//import cloud.TwitterPost;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.ConfirmSubscriptionRequest;
import com.amazonaws.services.sns.model.ConfirmSubscriptionResult;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Servlet implementation class EndPointServlet
 */
public class EndPointServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
    BasicAWSCredentials awsCreds = new BasicAWSCredentials("AKIAIUNZQ7TNRLTK2TJA", "X6jnLJkC+fPhfPJXZrjVKNgmPBIzhTR3NYAhzp+O");
    private final AmazonS3Client s3 = new AmazonS3Client(awsCreds);
    private final AmazonSNSClient amazonSNSClient = new AmazonSNSClient(awsCreds);
//    public AmazonSimpleDBClient sdb = new AmazonSimpleDBClient(awsCreds);; 
//    public String myDomain = "car2";	
    
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EndPointServlet() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException, SecurityException{			
		String messagetype = request.getHeader("x-amz-sns-message-type");	
		
		//If message doesn't have the message type header, don't process it.
		if (messagetype == null)
			return;

		// Parse the JSON message in the message body
		// and hydrate a Message object with its contents 
		// so that we have easy access to the name/value pairs 
		// from the JSON message.
		Scanner scan = new Scanner(request.getInputStream());
		StringBuilder builder = new StringBuilder();
		while (scan.hasNextLine()) {
			builder.append(scan.nextLine());
		}
		
        String str = builder.toString();
        str=str.replaceFirst(",\\s+\"MessageAttributes\".+$", "}");
        
		SnsMessage msg = readMessageFromJson(str);
		String str1 = msg.getMessage();
		byte[] content = str1.getBytes("UTF-8");
		s3.putObject("letstrytrytry",
              "getmsg.txt",
              new ByteArrayInputStream(content),
              new ObjectMetadata());
		// Process the message based on type.
		if (messagetype.equals("Notification")) {
			//TODO: Do something with the Message and Subject.
			//Just log the subject (if it exists) and the message.
			
			String t1 = "haobaya";
			byte[] content1 = t1.getBytes("UTF-8");
			s3.putObject("letstrytrytry",
	              "notificationa.txt",
	              new ByteArrayInputStream(content1),
	              new ObjectMetadata());
			
			String mes = "";
			String logMsgAndSubject = ">>Notification received from topic " + msg.getTopicArn();
			if (msg.getSubject() != null) {
				logMsgAndSubject += " Subject: " + msg.getSubject();
			logMsgAndSubject += " Message: " + msg.getMessage();
			mes = msg.getMessage();		
			}
			//WbHandler.broadcast(mes);
			/*
			TweetServer tweetServer = new TweetServer(10089);
		    tweetServer.start();
			tweetServer.publish("nnnnnnnnnnn");
			*/
			String t2 = mes;
			byte[] content2 = t2.getBytes("UTF-8");
			s3.putObject("letstrytrytry",
	              "done.txt",
	              new ByteArrayInputStream(content2),
	              new ObjectMetadata());
			String id, score;
			Pattern ID = Pattern.compile("\"id\":\\s+\"[^\"]+\"");
			Pattern SCORE = Pattern.compile("\"score\":\\s+\"[^\"]+\"");
			Matcher matcher = ID.matcher(mes);
			matcher.find();
			id = matcher.group().replaceFirst("\"id\":\\s+", "");
			id = id.replaceAll("\"", "");
			matcher = SCORE.matcher(mes);
			matcher.find();
			score = matcher.group().replaceFirst("\"score\":\\s+", "");
			score = score.replaceAll("\"", "");
			
//			try {
//				TwitterPost t = new TwitterPost(id, score);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			
			// insert records in database
			
//			try {
//				Thread.sleep(10000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			Region usEast1 = Region.getRegion(Regions.US_EAST_1);
//			sdb.setRegion(usEast1);	
//			try {
//				Thread.sleep(30000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			CreateDomainRequest createDomainRequest = new CreateDomainRequest("car2");
//			sdb.createDomain(createDomainRequest);
//			
//			List<ReplaceableAttribute> mylist = new ArrayList<ReplaceableAttribute>();
//			PutAttributesRequest putAttributesRequest = new PutAttributesRequest();
//	        putAttributesRequest.setDomainName("car2");
//	        
//	        mylist.add(new ReplaceableAttribute("id", id, true));
//	        mylist.add(new ReplaceableAttribute("score",score, true));
//			
//			putAttributesRequest.setAttributes(mylist);
//            sdb.putAttributes(putAttributesRequest);
//			
//            String sdbmsg = "sdbmsg";
//			byte[] content222 = sdbmsg.getBytes("UTF-8");
//			s3.putObject("letstrytrytry",
//	              "sdbmsg.txt",
//	              new ByteArrayInputStream(content222),
//	              new ObjectMetadata());
//			
//		    SelectResult selectResult = null;
//		    String query = null;
//		    
//		    query = "select id, score from " + "car2";
//		    SelectRequest selectRequest = new SelectRequest(query);
//		    selectResult = sdb.select(selectRequest);
//		    String selectres = selectResult.toString();
//			byte[] content2222 = selectres.getBytes("UTF-8");
//			s3.putObject("letstrytrytry",
//	              "queryres.txt",
//	              new ByteArrayInputStream(content2222),
//	              new ObjectMetadata());
	        
			/*
			request.setAttribute("id", id);
			request.setAttribute("score", score);
			request.getRequestDispatcher("index-5.jsp").forward(request, response);
			*/
		} 
		else if (messagetype.equals("SubscriptionConfirmation"))
		{
			//TODO: You should make sure that this subscription is from the topic you expect. Compare topicARN to your list of topics 
			//that you want to enable to add this endpoint as a subscription.

			//Confirm the subscription by going to the subscribeURL location 
			//and capture the return value (XML message body as a string)
			Scanner sc = new Scanner(new URL(msg.getSubscribeURL()).openStream());
			StringBuilder sb = new StringBuilder();
			while (sc.hasNextLine()) {
				sb.append(sc.nextLine());
			}

			//System.out.println(">>Subscription confirmation (" + msg.getSubscribeURL() +") Return value: " + sb.toString());
			//TODO: Process the return value to ensure the endpoint is subscribed.
			
//			ConfirmSubscriptionRequest confirmSubscriptionRequest = new ConfirmSubscriptionRequest()
//					.withTopicArn(msg.getTopicArn())
//					.withToken(msg.getToken());
//			ConfirmSubscriptionResult result = amazonSNSClient.confirmSubscription(confirmSubscriptionRequest);
		}
		else if (messagetype.equals("UnsubscribeConfirmation")) {
			//TODO: Handle UnsubscribeConfirmation message. 
			//For example, take action if unsubscribing should not have occurred.
			//You can read the SubscribeURL from this message and 
			//re-subscribe the endpoint.
			System.out.println(">>Unsubscribe confirmation: " + msg.getMessage());
		}
		else {
			//TODO: Handle unknown message type.
			System.out.println(">>Unknown message type.");
		}
		System.out.println(">>Done processing message: " + msg.getMessageId());
		
		//request.getRequestDispatcher("index.html").forward(request, response);
	}

	private SnsMessage readMessageFromJson(String string){
		ObjectMapper mapper = new ObjectMapper(); 
		SnsMessage message = null;
		try {
			message = mapper.readValue(string, SnsMessage.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return message;
	}

}

