import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.XML;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.MessageAttributeValue;
import com.amazonaws.services.sqs.model.SendMessageRequest;




public class Newyorktimes_topstories {

	public static void main(String[] args) throws Exception  {
		// TODO Auto-generated method stub
		URL url = new URL("http://api.nytimes.com/svc/topstories/v1/home.json?api-key=a8759b9b0b79ba994111a7e4a27c977c:17:71948109");
		URLConnection connection = url.openConnection();
		org.json.JSONObject jo = (org.json.JSONObject) new JSONTokener(IOUtils.toString(new URL("http://api.nytimes.com/svc/topstories/v1/home.json?api-key=a8759b9b0b79ba994111a7e4a27c977c:17:71948109"))).nextValue();
		final Connection conn = DBConnection.createConnection();
		//System.out.println(jo);
    	AWSCredentials credentials =new BasicAWSCredentials("");
    	final AmazonSQS sqs= new AmazonSQSClient(credentials);
		 Region usEast1 = Region.getRegion(Regions.US_EAST_1);
		 sqs.setRegion(usEast1);
	     System.out.println("Creating SQS Now-debug");
		 CreateQueueRequest createQueueRequest = new CreateQueueRequest("NewsQueue");
 		final String  newsQueueUrl = sqs.createQueue(createQueueRequest).getQueueUrl();



		   //String key = (String)keys.next();
		    //System.out.println(key);

		    JSONArray arr = jo.getJSONArray("results");

		    for (int i = 0; i < arr.length(); i++)
		    {
		        String title = arr.getJSONObject(i).getString("title");
		        String news = arr.getJSONObject(i).getString("abstract");
		        String datetime = arr.getJSONObject(i).getString("created_date");
		        System.out.println("Title: "+title);
		        System.out.println("News: "+news);
		        System.out.println("Datetime: "+datetime);
		        String[] parts = datetime.split("T");
		        String date = parts[0]; // 004
		        System.out.println("Date: "+date );
		        String timeset[]=parts[1].split("-");
		        String time = timeset[0];
		        System.out.println("Time: "+time);
		        String mysqldatetime = date + " " + time;
		        //(JSONArray)
		     if( arr.getJSONObject(i).get("multimedia") != null)
		     {

			   // JSONObject arr1 = arr.getJSONObject(i).getJSONObject("multimedia");
		String multimedia  = arr.getJSONObject(i).get("multimedia").toString();
		if(multimedia!=null)
		{
	    multimedia = multimedia.replace("[", "");
	    multimedia = multimedia.replace("]", "");

	    JSONObject jsonObj = new JSONObject(multimedia);
	    System.out.println(jsonObj);
	    String imgurl = jsonObj.getString("url");

		PreparedStatement preparedStatement = null;
		preparedStatement = conn.prepareStatement("insert into new_york_time_headlines_api values(?,?,?,?)");

		preparedStatement.setString(1,title );
		preparedStatement.setString(2,news );
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date datesql = simpleDateFormat.parse(mysqldatetime);
        System.out.println("date to be inserted : "+simpleDateFormat.format(datesql));
        System.out.println(datesql.getTime());
        Timestamp timestamp = new Timestamp(datesql.getTime());
		preparedStatement.setTimestamp(3, timestamp  );
		preparedStatement.setString(4,imgurl);
		preparedStatement.executeUpdate();
		Map<String, MessageAttributeValue> messageAttributes = new HashMap<>();
		messageAttributes.put("news", new MessageAttributeValue().withDataType("String.Name").withStringValue(String.valueOf(news)));
		messageAttributes.put("title", new MessageAttributeValue().withDataType("String.Name").withStringValue(String.valueOf(title)));
		messageAttributes.put("title", new MessageAttributeValue().withDataType("String.Name").withStringValue(String.valueOf(datesql)));
		SendMessageRequest request1 = new SendMessageRequest();
		request1.withMessageBody("New York Times");
		request1.withQueueUrl(newsQueueUrl);
		request1.withMessageAttributes(messageAttributes);
		sqs.sendMessage(request1);
		System.out.println("\nAdded to queue");



		}



		     }



	/*
String xml = XML.toString(jo);
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
DocumentBuilder builder = factory.newDocumentBuilder();
Document document = builder.parse( new InputSource( new StringReader( xml ) ) );
*/

		}

	}
}
