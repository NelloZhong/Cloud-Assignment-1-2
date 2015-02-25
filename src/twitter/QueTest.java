package twitter;

import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.DeleteQueueRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

public class QueTest {
	 private static AWSCredentials credentials;
		private static String queueUrl;
		public static AmazonSQS sqs;
		
	public static void main(String args[]) throws IOException{
		credentials = new PropertiesCredentials(DataFetch.class.getResourceAsStream("AwsCredentials.properties"));
		queueUrl = "https://sqs.us-east-1.amazonaws.com/321375226505/test";
		sqs = new AmazonSQSClient(credentials);
        Region usEast1 = Region.getRegion(Regions.US_EAST_1);
        sqs.setRegion(usEast1);
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
        System.out.println(messages);
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
        System.out.println("Deleting a message.\n");
        String messageRecieptHandle = messages.get(0).getReceiptHandle();
        sqs.deleteMessage(new DeleteMessageRequest(queueUrl, messageRecieptHandle));

        // Delete a queue
        System.out.println("Deleting the test queue.\n");
     //   sqs.deleteQueue(new DeleteQueueRequest(queueUrl));

	}

}
