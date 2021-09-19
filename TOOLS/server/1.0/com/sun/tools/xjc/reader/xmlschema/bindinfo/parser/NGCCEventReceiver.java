package 1.0.com.sun.tools.xjc.reader.xmlschema.bindinfo.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public interface NGCCEventReceiver {
  void enterElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes) throws SAXException;
  
  void leaveElement(String paramString1, String paramString2, String paramString3) throws SAXException;
  
  void text(String paramString) throws SAXException;
  
  void enterAttribute(String paramString1, String paramString2, String paramString3) throws SAXException;
  
  void leaveAttribute(String paramString1, String paramString2, String paramString3) throws SAXException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\bindinfo\parser\NGCCEventReceiver.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */