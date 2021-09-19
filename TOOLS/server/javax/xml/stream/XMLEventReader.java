package javax.xml.stream;

import java.util.Iterator;
import javax.xml.stream.events.XMLEvent;

public interface XMLEventReader extends Iterator {
  XMLEvent nextEvent() throws XMLStreamException;
  
  boolean hasNext();
  
  XMLEvent peek() throws XMLStreamException;
  
  String getElementText() throws XMLStreamException;
  
  XMLEvent nextTag() throws XMLStreamException;
  
  Object getProperty(String paramString) throws IllegalArgumentException;
  
  void close() throws XMLStreamException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\stream\XMLEventReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */