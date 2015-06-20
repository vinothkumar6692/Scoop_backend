import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class The_Verge_Gadget_Review {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
				final Connection conn = DBConnection.createConnection();
				URL url = new URL("http://www.theverge.com/rss/group/review/index.xml");
				URLConnection connection = url.openConnection();
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(connection.getInputStream());
				TransformerFactory factory1 = TransformerFactory.newInstance();
				Transformer xform = factory1.newTransformer();
				xform.transform(new DOMSource(doc), new StreamResult(System.out));
				NodeList nList = doc.getElementsByTagName("entry");
				for (int temp = 0; temp < nList.getLength(); temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						String title =  eElement.getElementsByTagName("title").item(0).getTextContent();
						System.out.println("title: "+title);
						String description = eElement.getElementsByTagName("content").item(0).getTextContent();
						System.out.println("Description: "+description);
						description = description.replaceAll("\\<img.*?>","");
				        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String datetime = eElement.getElementsByTagName("published").item(0).getTextContent();
						String[] parts = datetime.split("T");
				        String date = parts[0]; // 004
				        System.out.println("Date: "+date );
				        String timeset[]=parts[1].split("-");
				        String time = timeset[0];
				        System.out.println("Time: "+time);
				        PreparedStatement preparedStatement = null;
						preparedStatement = conn.prepareStatement("insert into the_verge_gadgets_review1 values(?,?,?)");
				        String mysqldatetime = date + " " + time;
						Date datesql = simpleDateFormat.parse(mysqldatetime);
				        System.out.println("date to be inserted : "+simpleDateFormat.format(datesql));
				        System.out.println(datesql.getTime());
				        Timestamp timestamp = new Timestamp(datesql.getTime());
						preparedStatement.setString(1,title );
						preparedStatement.setString(2,description );
						preparedStatement.setTimestamp(3, timestamp  );
						preparedStatement.executeUpdate();
					}
				}
	}

}
