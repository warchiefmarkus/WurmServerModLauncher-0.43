package javax.xml.stream.events;

import java.util.Iterator;
import javax.xml.namespace.QName;

public interface EndElement extends XMLEvent {
  QName getName();
  
  Iterator getNamespaces();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\stream\events\EndElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */