package twitter;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;

import twitter4j.FilterQuery;
import twitter4j.JSONObject;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterGet {
	public static int Key_Size;
	public static String[] ConsumeKey;
	public static String[] ConsumerSecret;
	public static String[] AccessToken;
	public static String[] AccessTokenSecret;
	
	public static String[] keywords;
	public static int Query_Size;
	public static ConfigurationBuilder cb = new ConfigurationBuilder();
    static TweetServer tweetServer;
    static String myQueueUrl;
    static int port = 11090;
    static  AmazonSQS sqs;
	static String itemName="300";
	/*
	 * Load configuration from config
	 */
	public static void Initial() 
	{
		System.out.println("==============API_KEY & Queries fetch process starts=============\n");
		Properties prop = new Properties();
        String filename = "config.properties";
		try {
			//Load
			prop.load(TwitterGet.class.getResourceAsStream(filename));
			
			ConsumeKey = prop.getProperty("ConsumerKey").split(";");
			ConsumerSecret = prop.getProperty("ConsumerSecret").split(";");
			AccessToken = prop.getProperty("AccessToken").split(";");
			AccessTokenSecret = prop.getProperty("AccessTokenSecret").split(";");
			keywords = prop.getProperty("Keywords").split(";");
		} catch (Exception e) {
			System.out.println("Get twitter API Key error");
		}finally {
			
		}
		Key_Size = ConsumeKey.length;
	}	
	
	public static void main(String[] args) throws IOException {
		Initial();
		 tweetServer = new TweetServer(port);
		 tweetServer.start();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey(ConsumeKey[0])  
		.setOAuthConsumerSecret(ConsumerSecret[0])
		.setOAuthAccessToken(AccessToken[0])
		.setOAuthAccessTokenSecret(AccessTokenSecret[0]);
//		AWSCredentials credentials = new ProfileCredentialsProvider().getCredentials();
//        

	        
	        
	        
        
		StatusListener listener = new StatusListener(){
			private int Number = 0;
			private String fileName = "";
	        private int count = 0;
	        private PrintWriter pw;
	        private FileWriter Log;
	        
			public void onStatus(Status tweet) {
				DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
				Date date = new Date(); 
				try{
					if(count == 0) {
						String newfileName = dateFormat.format(date);
						if(!newfileName.equals(fileName))
						{
							fileName = newfileName;
							Number = 0;
						}
						pw = new PrintWriter(new File(fileName+"-"+Number+".json"));
						pw.write("{\"tweets\":[");
					}
					
					if (tweet.getGeoLocation() != null
	            			|| (tweet.getUser().getLocation() != null
	            			&& !tweet.getUser().getLocation().isEmpty())) {
	            		
						Double latitude = null ;
						Double longtitude = null;
						
		            	if (tweet.getGeoLocation() != null) {
			            	System.out.println("From GeoLocation:");
			            	latitude = tweet.getGeoLocation().getLatitude();
			            	longtitude = tweet.getGeoLocation().getLongitude();
		            	}
		            	
	            	ArrayList<String> list = new ArrayList<String>();
		            	list.add("Columbia");
		            	list.add("Snow");
		            	list.add("University");
		            	list.add("Baseball");
		            	list.add("Computer");
//	            	ArrayList<String> list = new ArrayList<String>();
//		            	list.add("buy");
//		            	list.add("shop");
//		            	list.add("deal");
//		            	list.add("want to buy");
//		            	list.add("hope to get");
//		            	list.add("wish to buy");
//		            	list.add("wish to get");
//		            	list.add("want to get");
//		            	list.add("hope to buy");
//		            	list.add("taste");
//		               	list.add("brand");
//		               	list.add("gifts");
//		            	list.add("gift");
//		            	list.add("taste");
		            	
		            	
		            	
		            	String temp="";
		   
		            	
		            	if(!(latitude == null || longtitude == null || latitude.isNaN() || longtitude.isNaN()))
		            	{
			            	JSONObject jo = new JSONObject();
			            	
			            	String textString = tweet.getText();
			            	String[] singleWord = textString.split("[\\p{Punct}, \\s]+");
			            	for (String word : singleWord) {
			            		if (word == null || word.length() == 0 ) {
			            			continue;
			            		}
			            		for (int i = 0; i<=4; i++){
			            			if (word.toLowerCase().equals(list.get(i).toLowerCase())){
			            				temp = list.get(i);
			            				break;
			            			}
			            				
			            		}
			            	}			            	
			            	
			            	
			            	jo.put("Date", fileName);
			            	jo.put("Latitude", latitude);
			            	jo.put("Longitude", longtitude);
			            //	jo.put("address", tweet.getUser().getLocation());
			            	//jo.put("user", tweet.getUser().getScreenName());
			            	jo.put("text", tweet.getText());
			            	jo.put("keyword", temp);
			            	
			            	System.out.println("================="+temp);
			            	
			            	System.out.println("The "+ count + " tweets: " + dateFormat.format(date)+ ", File No: "+Number);
			            	System.out.println("latitude "+latitude+",longitude "+longtitude);
			            	System.out.println(jo+"\n");
			            	
			            	count++;
			            	if (count == 1){
			            		pw.write(jo.toString() + "]}");
			            		pw.close();
			            		count = 0;	
								Log = new FileWriter("Log",false);
			            		Log.write(dateFormat.format(date) + "\n" + Number);
			            		Log.close();
			            		System.out.println("written to file: " + fileName + ("-"+ Number++)+".json");
			            		DataFetch df = new DataFetch(fileName + ("-"+ (Number-1)) +".json", "UTF-8",tweetServer, sqs, myQueueUrl,itemName);
			    	            Integer k= Integer.parseInt(itemName)+1;
			    	            itemName=k.toString();
			            	}
			            	else
			            		pw.write(jo.toString() + ",");
		            	}
            	}
				}catch(Exception e){
					pw.close();
					e.printStackTrace();
				}
            }
			@Override
			public void onException(Exception ex) {
				pw.close();
				ex.printStackTrace();
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {}

			@Override
			public void onScrubGeo(long arg0, long arg1) {}

			@Override
			public void onStallWarning(StallWarning arg0) {}

			@Override
			public void onTrackLimitationNotice(int arg0) {}
	    };
	    
	    TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
	    twitterStream.addListener(listener);
	    FilterQuery fil = new FilterQuery();
	  //  String[] keywords = {"want to buy"};
	    
	   String[] keywords = {"Columbia", "Snow", "University", "Baseball", "Computer"};
	    fil.track(keywords);
	    twitterStream.filter(fil);
	}

}
