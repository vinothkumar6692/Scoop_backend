import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;


public class USA_Today_topstories {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		final Connection conn = DBConnection.createConnection();
		URL url = new URL("http://api.usatoday.com/open/articles/topnews?api_key=ftwdpg9t8evt9ar7u4yj3xgs");
		URLConnection connection = url.openConnection();
    	AWSCredentials credentials =new BasicAWSCredentials("");
    	final AmazonSQS sqs= new AmazonSQSClient(credentials);
   	 Region usEast1 = Region.getRegion(Regions.US_EAST_1);
		AmazonSNSClient snsservice = new AmazonSNSClient(credentials); //create SNS service
		CreateTopicRequest createReq = new CreateTopicRequest().withName("newssns");
		CreateTopicResult createRes = snsservice.createTopic(createReq);

	 sqs.setRegion(usEast1);
     System.out.println("Creating SQS Now-debug");
	 CreateQueueRequest createQueueRequest = new CreateQueueRequest("NewsQueue");
		final String  newsQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(connection.getInputStream());
		TransformerFactory factory1 = TransformerFactory.newInstance();
		Transformer xform = factory1.newTransformer();
		xform.transform(new DOMSource(doc), new StreamResult(System.out));

		NodeList nList = doc.getElementsByTagName("item");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				//System.out.println("title : " + eElement.getElementsByTagName("title").item(0).getTextContent());
				String title = eElement.getElementsByTagName("title").item(0).getTextContent();
				System.out.println("Title: " +title);
				String description = eElement.getElementsByTagName("description").item(0).getTextContent();
				System.out.println("Description: "+description);
				String date = eElement.getElementsByTagName("pubDate").item(0).getTextContent();
				System.out.println("Date: "+date);
		        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yy HH:mm:ss");
		        String[] parts = date.split(", ");
		        String dateinfo = parts[1];
		        String datemysql = dateinfo.replace("GMT","");
		        Date datesql = simpleDateFormat.parse(datemysql);
		        System.out.println("Date to be inserted: "+datesql);
		        Timestamp timestamp = new Timestamp(datesql.getTime());
				PreparedStatement preparedStatement = null;
				preparedStatement = conn.prepareStatement("insert into usa_today_headlines_api values(?,?,?)");
				preparedStatement.setString(1,title );
				preparedStatement.setString(2,description );
				preparedStatement.setTimestamp(3, timestamp  );
				preparedStatement.executeUpdate();
				Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
				messageAttributes.put("news", new MessageAttributeValue().withDataType("String.Name").withStringValue(String.valueOf(description)));
				messageAttributes.put("title", new MessageAttributeValue().withDataType("String.Name").withStringValue(String.valueOf(title)));
				messageAttributes.put("title", new MessageAttributeValue().withDataType("String.Name").withStringValue(String.valueOf(datesql)));
				SendMessageRequest request1 = new SendMessageRequest();
				request1.withMessageBody("USA Today");
				request1.withQueueUrl(newsQueueUrl);
				request1.withMessageAttributes(messageAttributes);
				sqs.sendMessage(request1);
				System.out.println("\nAdded to queue");
	    	    PublishRequest publishReq = new PublishRequest().withTopicArn(createRes.getTopicArn()).withMessage(title +","+description+","+datesql);
	    	    snsservice.publish(publishReq);
	            System.out.println("published");

			}

	}
	}
}
