package 1.0.org.apache.xml.resolver.tools;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.xml.resolver.tools.ResolvingXMLFilter;

public class ResolvingXMLReader extends ResolvingXMLFilter {
  public ResolvingXMLReader() {
    SAXParserFactory sAXParserFactory = SAXParserFactory.newInstance();
    try {
      SAXParser sAXParser = sAXParserFactory.newSAXParser();
      setParent(sAXParser.getXMLReader());
    } catch (Exception exception) {
      exception.printStackTrace();
    } 
  }
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\org\apache\xml\resolver\tools\ResolvingXMLReader.class
 * Java compiler version: 1 (45.3)
 * JD-Core Version:       1.1.3
 */