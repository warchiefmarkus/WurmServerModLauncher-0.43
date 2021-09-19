package 1.0.com.sun.xml.xsom.impl.parser.state;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public interface NGCCEventReceiver {
  void enterElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes) throws SAXException;
  
  void leaveElement(String paramString1, String paramString2, String paramString3) throws SAXException;
  
  void text(String paramString) throws SAXException;
  
  void enterAttribute(String paramString1, String paramString2, String paramString3) throws SAXException;
  
  void leaveAttribute(String paramString1, String paramString2, String paramString3) throws SAXException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\NGCCEventReceiver.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */