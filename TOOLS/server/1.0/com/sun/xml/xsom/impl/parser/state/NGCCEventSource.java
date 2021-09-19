package 1.0.com.sun.xml.xsom.impl.parser.state;

import com.sun.xml.xsom.impl.parser.state.NGCCEventReceiver;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public interface NGCCEventSource {
  int replace(NGCCEventReceiver paramNGCCEventReceiver1, NGCCEventReceiver paramNGCCEventReceiver2);
  
  void sendEnterElement(int paramInt, String paramString1, String paramString2, String paramString3, Attributes paramAttributes) throws SAXException;
  
  void sendLeaveElement(int paramInt, String paramString1, String paramString2, String paramString3) throws SAXException;
  
  void sendEnterAttribute(int paramInt, String paramString1, String paramString2, String paramString3) throws SAXException;
  
  void sendLeaveAttribute(int paramInt, String paramString1, String paramString2, String paramString3) throws SAXException;
  
  void sendText(int paramInt, String paramString) throws SAXException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\state\NGCCEventSource.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */