package javax.xml.stream.events;

import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

public interface StartElement extends XMLEvent {
  QName getName();
  
  Iterator getAttributes();
  
  Iterator getNamespaces();
  
  Attribute getAttributeByName(QName paramQName);
  
  NamespaceContext getNamespaceContext();
  
  String getNamespaceURI(String paramString);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\stream\events\StartElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */