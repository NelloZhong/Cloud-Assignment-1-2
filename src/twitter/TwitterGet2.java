package twitter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.joda.time.DateTime;

import au.com.bytecode.opencsv.CSVWriter;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.CreateDomainRequest;
import com.amazonaws.services.simpledb.model.PutAttributesRequest;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;

import twitter4j.FilterQuery;
import twitter4j.JSONObject;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.api.FriendsFollowersResources;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.json.DataObjectFactory;

public class TwitterGet2 {
	public static int Key_Size;
	public static String[] ConsumeKey;
	public static String[] ConsumerSecret;
	public static String[] AccessToken;
	public static String[] AccessTokenSecret;
	
	public static String[] keywords;
	public static int Query_Size;
	public static ConfigurationBuilder cb = new ConfigurationBuilder();
	public static  AmazonSimpleDBClient sdb;
	public String myDomain = "car1";
	public static String itemName ="500";
    static TweetServer tweetServer;
    public static CSVWriter writer;
    public static CSVWriter writer2;
    static int port = 11089;
	public static List<String[]> data;
	public static List<String[]> data2;
	/*
	 * Load configuration from config
	 */
	public static void Initial() 
	
	{
		Date date = new Date(); 
		System.out.println("==============API_KEY & Queries fetch process starts=============\n");
		Properties prop = new Properties();
        String filename = "config.properties";
        data = new ArrayList<String[]>();
        data2 = new ArrayList<String[]>();
		try {
			
			 AWSCredentials credentials = new PropertiesCredentials(DataFetch.class.getResourceAsStream("AwsCredentials.properties"));
				sdb = new AmazonSimpleDBClient(credentials);
				
		        //SimpleDB sdb = new SimpleDB(credentials);
		
				Region usEast1 = Region.getRegion(Regions.US_EAST_1);
				sdb.setRegion(usEast1);
				
				// Create a domain
			    System.out.println("Creating domain called " + "car1" + ".\n");
			  sdb.createDomain(new CreateDomainRequest("car1"));
	         System.out.println("flag1");
			//Load
			prop.load(TwitterGet.class.getResourceAsStream(filename));
			
			ConsumeKey = prop.getProperty("ConsumerKey").split(";");
			ConsumerSecret = prop.getProperty("ConsumerSecret").split(";");
			AccessToken = prop.getProperty("AccessToken").split(";");
			AccessTokenSecret = prop.getProperty("AccessTokenSecret").split(";");
			keywords = prop.getProperty("Keywords").split(";");
		} catch (Exception e) {
			System.out.println("Get twitter API Key error");
			// TODO: handle exception
		}finally {
			
		}
		//Record the key size and keyword size
		Key_Size = ConsumeKey.length;
		Query_Size = keywords.length;
		//Verbose informations
		System.out.println("Getting "+Key_Size+" API_KEY:"+ConsumeKey[0]);
		System.out.println(Query_Size +" Queries are:" + Arrays.toString(keywords));
		
		System.out.println("==============API_KEY & Queries fetch process Ends=============\n");
		try {
			writer = new CSVWriter(new FileWriter("/Users/qianyizhong/Documents/yourfile3.csv"));
			writer2 = new CSVWriter(new FileWriter("/Users/qianyizhong/Documents/myfile3.csv"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
//        try {
//            tweetServer = new TweetServer(port);
//            tweetServer.start();
//            System.out.println( "TweetServer started on port: " + tweetServer.getPort());
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
		Initial();
		 
		 tweetServer = new TweetServer(port);
		 tweetServer.start();
		cb.setDebugEnabled(true)
		.setOAuthConsumerKey(ConsumeKey[0])  
		.setOAuthConsumerSecret(ConsumerSecret[0])
		.setOAuthAccessToken(AccessToken[0])
		.setOAuthAccessTokenSecret(AccessTokenSecret[0]);
		
       

			//TweetsArray = obj.getJSONObject("entityinfo").getJSONArray("tweets");
			
			

			
			
		 
		 
		 
		 
		StatusListener listener = new StatusListener(){
			private int fileNo = 0;
			private String filePath = "";
	        private int count = 0;
	        private PrintWriter pw;
	        private FileWriter Log;
	        
			public  void onStatus(Status tweet) {
				DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
				Date date = new Date(); 
			
				try{

					
					
					
					    // ...
			         
			      
			       
					if(count == 0) {
						String newfileName = dateFormat.format(date);
						if(!newfileName.equals(filePath))
						{
							filePath = newfileName;
							fileNo = 0;
						}
						pw = new PrintWriter(new File(filePath+"-"+fileNo+".json"));
						pw.write("{\"tweets\":[");
					}
					
					if (tweet.getGeoLocation() != null
	            			|| (tweet.getUser().getLocation() != null
	            			&& !tweet.getUser().getLocation().isEmpty())) {
	            		
						//Avoid in case Lat Long cannot be parsed 
						Double latitude = null ;
						Double longtitude = null;
						
		            	if (tweet.getGeoLocation() != null) {
			            	System.out.println("From GeoLocation:");
			            	latitude = tweet.getGeoLocation().getLatitude();
			            	longtitude = tweet.getGeoLocation().getLongitude();
		            	}
		            	
		            	
		            //	String statusJson = DataObjectFactory.getRawJSON();
		            	
		            //	String j = DataObjectFactory.getRawJSON(tweet);
		        //    	System.out.println(j);
//		            	else{
//		            		//Get LatLng by user location
//	                    	String address = tweet.getUser().getLocation();
//			            	System.out.println("From Address: "+ address);
//	                    	Double[] LatLng =  TwitterLatLng.getLongitudeLatitude(address);
//	                    	if(LatLng !=null )
//	                    	{
//		                    	latitude = LatLng[0];
//		                    	longtitude = LatLng[1];
//	                    	}
//		            	}
		            	
		            	ArrayList<String> list = new ArrayList<String>();
		            	list.add("Columbia");
		            	list.add("Snow");
		            	list.add("University");
		            	list.add("Baseball");
		            	list.add("Computer");
//		            	list.add("A");
//		            	list.add("Is");
//		            	list.add("My");
//		            	list.add("Weather");
		            	String temp="";
		   
		            	
		            	if(!(latitude == null || longtitude == null || latitude.isNaN() || longtitude.isNaN()))
		            	{
			            	JSONObject jo = new JSONObject();
			            	
			            	String textString = tweet.getText();
//			            	   try {
//			                	      File file = new File("/Users/qianyizhong/Desktop/"+itemName+".txt");
//			                	      BufferedWriter output = new BufferedWriter(new FileWriter(file));
//			                	      output.write(textString);
//			                	      output.close();
//			                	    } catch ( IOException e ) {
//			                	       e.printStackTrace();
//			                	    }
			            	
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
			            	
			            	
			            	DateTime dt = new DateTime();  // current time
			            	//int month = dt.getMonth();     // gets the current month
			            	Integer hours = dt.getHourOfDay();
			            	 filePath = "12-06-2014";
			            	data.add(new String[] {itemName,filePath,hours.toString(),tweet.getText()});
			            	String[] entries = {itemName,filePath,hours.toString(),tweet.getText()};
			            	writer = new CSVWriter(new FileWriter("/Users/qianyizhong/Documents/yourfile3.csv"));
			            	//System.out.println("entries is:" + entries);
			            	
			        	    writer.writeAll(data);
			            	writer.close();
			            	String twitt = tweet.getText();
			            	String stringToBeTokenized = twitt.replace('#', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('?', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('<', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('>', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('[', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace(']', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('{', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('}', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('@', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('(', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace(')', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace(',', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('!', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('&', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace(':', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('?', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('/', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('\\', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('\'', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('"', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('+', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('=', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('_', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('-', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('*', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('$', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('^', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('%', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('.', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace(';', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('1', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('2', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('3', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('4', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('5', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('6', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('7', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('8', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('9', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('0', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('\n', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('\t', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('`', ' ');
			         		stringToBeTokenized = stringToBeTokenized.replace('~', ' ');
			        
			        		String stopwords="	a about above above across after afterwards again against all almost alone along already "
			        				+ "also although always am among amongst amoungst amount  an and another any anyhow anyone"
			        				+ " anything anyway anywhere are around as  at back be became because become becomes becoming"
			        				+ " been before beforehand behind being below beside besides between beyond both bottom but by "
			        				+ "call can cannot cant co con could couldnt cry de describe detail do done down due during each "
			        				+ "eg eight either eleven else elsewhere empty enough etc even ever every everyone everything"
			        				+ " everywhere except few fifteen fify fill find fire first five for former formerly forty found "
			        				+ "four from front full further get give go had has hasnt have he hence her here hereafter hereby "
			        				+ "herein hereupon hers herself him himself his how however hundred ie if in inc indeed interest into "
			        				+ "is it its itself keep last latter latterly least less ltd made many may me meanwhile might mill mine"
			        				+ " more moreover most mostly move much must my myself name namely neither never nevertheless next new nine"
			        				+ " no nobody none noone nor not nothing now nowhere of off often on once one only onto or other others "
			        				+ "otherwise our ours ourselves out over own part per perhaps please put rather re same see seem seemed seeming "
			        				+ "seems serious several she should show side since sincere six sixty so some somehow someone something sometime"
			        				+ " sometimes somewhere still such system take ten than that the their them themselves then thence there"
			        				+ " thereafter thereby therefore therein thereupon these they thickv thin third this those though three through"
			        				+ " throughout thru thus to together too top toward towards twelve twenty two un under until up upon us very via"
			        				+ " was we well were what whatever when whence whenever where whereafter whereas whereby wherein whereupon"
			        				+ " wherever whether which while whither who whoever whole whom whose why will with within without would yet"
			        				+ " you your yours yourself yourselves the 1 2 3 4 5 6 7 8 9 0 00 000 0000 00000 000000 0000000 \\ / j ya di tu " 
			        				+ "tr da q http apa uu ? fa ik nya event events";
			        		
			        		String[] tokens = stringToBeTokenized.split(" |-|,|\"|\\.|\\(|\\)|\\||&|\\?|:\n\t");
                            String adtwit =new String();
			        		for (int i = 0; i < tokens.length; i++) {
			        			String token = tokens[i].trim().toLowerCase();
			        			if (stopwords.contains(token)) {

			        			} else {
			        				adtwit = adtwit+token+" ";
			        			}
			        		}
			            	
			            	System.out.println(adtwit);
			            	
			            	data2.add(new String[]{itemName,filePath,hours.toString(),adtwit});
			            	//String[] entries = {itemName,filePath,hours.toString(),tweet.getText()};
			            	writer2 = new CSVWriter(new FileWriter("/Users/qianyizhong/Documents/myfile3.csv"));
			            //	System.out.println("entries is:" + entries);
			            	
			        	    writer2.writeAll(data2);
			            	writer2.close();
			            	
//			            	if(temp.equals("A")){
//			            		temp = "University";
//			            	} else if (temp.equals("Is")){
//			            		temp = "Snow";
//			            		
//			            	} else if (temp.equals("My")){
//			            		temp = "Computer";
//			            	} else if (temp.equals("Weather")){
//			            		temp = "Baseball";
//			            	}
			            	 //  filePath = "12-06-2014";
							   List<ReplaceableAttribute> list1 = new ArrayList<ReplaceableAttribute>();
								PutAttributesRequest putAttributesRequest = new PutAttributesRequest();
						        putAttributesRequest.setDomainName("car1");
						        putAttributesRequest.setItemName(filePath+"-"+itemName);
						        System.out.println(itemName);
						//		String s0 = obj.getJSONArray("tweets").getJSONObject(i).getString("user");
							//	String s1 = obj.getJSONArray("tweets").getJSONObject(i).getString("text");
								//String s2 = obj.getJSONArray("tweets").getJSONObject(i).getString("Date");
								//Double s3 = obj.getJSONArray("tweets").getJSONObject(i).getDouble("Latitude");
								//Double s4 = obj.getJSONArray("tweets").getJSONObject(i).getDouble("Longitude");
								//String s5 = obj.getJSONArray("tweets").getJSONObject(i).getString("keyword");
								
//								System.out.println(s0);
//								System.out.println(s1);
//								System.out.println(s2);
//								System.out.println(s3);
							//	System.out.println("json keyword is");
							//	System.out.println(s5);		
						      
								list1.add(new ReplaceableAttribute("user", tweet.getUser().getScreenName(), true));
					            list1.add(new ReplaceableAttribute("text", tweet.getText(), true));
					            list1.add(new ReplaceableAttribute("Date", filePath, true));
					            list1.add(new ReplaceableAttribute("Latitude",latitude.toString() , true));
					            list1.add(new ReplaceableAttribute("Longitude", longtitude.toString(), true));
					            list1.add(new ReplaceableAttribute("keyword", temp.toString(),true));
					            
								putAttributesRequest.setAttributes(list1);
					            sdb.putAttributes(putAttributesRequest);
					            Integer k= Integer.parseInt(itemName)+1;
					            itemName=k.toString();
					            System.out.println("??????????????????????");
					            System.out.println(itemName);
					            tweetServer.publish("bbbbb");
//			            	    if (k%500 == 0) {
//			            	    	Thread.sleep(1000*3600);
//			            	    }
			            	//jo.put("Date", tweet.getCreatedAt());
			            	jo.put("Date",filePath);
			            	jo.put("Latitude", latitude);
			            	jo.put("Longitude", longtitude);
			            	jo.put("address", tweet.getUser().getLocation());
			            	jo.put("user", tweet.getUser().getScreenName());
			            	jo.put("text", tweet.getText());
			            	jo.put("keyword", temp);
			            	
			            	System.out.println("================="+temp);
			            	
			            	System.out.println("The "+ count + " tweets: " + dateFormat.format(date)+ ", File No: "+fileNo);
			            	System.out.println("latitude "+latitude+",longitude "+longtitude);
			            	System.out.println(jo+"\n");
			            	
			            	count++;
			            	if (count == 5){
			            		pw.write(jo.toString() + "]}");
			            		pw.close();
			            		count = 0;	//initialize counter
			            		//Modify the log file, record the current file number
			            		//Once finishes writing a file, record the current fileNo and date
								Log = new FileWriter("Log",false);
			            		Log.write(dateFormat.format(date) + "\n" + fileNo);
			            		Log.close();
			            		System.out.println("written to file: " + filePath + ("-"+ fileNo++)+".json");
			            		
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
	    String[] keywords = {"event","news","events"};
	    fil.track(keywords);
	   twitterStream.filter(fil);

	//    twitterStream.sample();
	}

}
