import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;

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


public class Joy_The_Baker {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		final Connection conn = DBConnection.createConnection();
		URL url = new URL("http://joythebaker.com/feed/");
		URLConnection connection = url.openConnection();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		 factory.setNamespaceAware(true);
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
				String title =  eElement.getElementsByTagName("title").item(0).getTextContent();
				System.out.println("title: "+title);
				String description = eElement.getElementsByTagName("content:encoded").item(0).getTextContent();
				System.out.println("Description: "+description);
				
			}
		}
	}

}
