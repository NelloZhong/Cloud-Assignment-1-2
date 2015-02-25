package twitter;

import java.util.List;
import java.util.Map.Entry;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;


import java.util.List;
import java.util.Map.Entry;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;


public class QueueMessage {
    private AWSCredentials credentials;
	private String queueUrl;
	AmazonSQS sqs;
	public QueueMessage(AWSCredentials cred, String url) {
		credentials = cred;
		queueUrl = url;
		sqs = new AmazonSQSClient(credentials);
        Region usEast1 = Region.getRegion(Regions.US_EAST_1);
        sqs.setRegion(usEast1);
	}
	public void sendTweet(String id, String tweet) {
		String workRequest =
        	    "{" +
        	    "  \"id\": \"" + id + "\"," +
        	    "  \"tweet\": \"" + tweet + "\"" +
        	    "}";
        sqs.sendMessage(new SendMessageRequest()
        .withQueueUrl(queueUrl)
        .withMessageBody(workRequest));
	}
	
	public void sendTweet(String id, String tweet, Double longitude, Double latitude, String keyword, String date) {
		String workRequest =
        	    "{" +
        	    "  \"id\": \"" + id + "\"," +
        	    "  \"tweet\": \"" + tweet + "\"," +
        	    "  \"date\": \"" + date + "\"," +
        	    "  \"keyword\": \"" + keyword + "\"," +
        	    "  \"latitude\": \"" + latitude + "\"," +
        	    "  \"longitude\": \"" + longitude + "\"" +
        	    "}";
        sqs.sendMessage(new SendMessageRequest()
        .withQueueUrl(queueUrl)
        .withMessageBody(workRequest));
	}
	
	public List<Message> get() {
		System.out.println("Receiving messages from MyQueue.\n");
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
        for (Message message : messages) {
            System.out.println("  Message");
            System.out.println("    MessageId:     " + message.getMessageId());
            System.out.println("    ReceiptHandle: " + message.getReceiptHandle());
            System.out.println("    MD5OfBody:     " + message.getMD5OfBody());
            System.out.println("    Body:          " + message.getBody());
            for (Entry<String, String> entry : message.getAttributes().entrySet()) {
                System.out.println("  Attribute");
                System.out.println("    Name:  " + entry.getKey());
                System.out.println("    Value: " + entry.getValue());
            }
        }
        System.out.println();
		return messages;
	}
	
	public void deleteAll() {
		System.out.println("Deleting messages.\n");
        String messageRecieptHandle;
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
        int n = messages.size();
        for (int i = 1; i < n; i++)
        {
        	messageRecieptHandle = messages.get(i).getReceiptHandle();
            sqs.deleteMessage(new DeleteMessageRequest(queueUrl, messageRecieptHandle));
        }
        System.out.println("Done!");
	}
}
