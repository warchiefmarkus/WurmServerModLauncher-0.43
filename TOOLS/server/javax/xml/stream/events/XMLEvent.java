package javax.xml.stream.events;

import java.io.Writer;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

public interface XMLEvent extends XMLStreamConstants {
  int getEventType();
  
  Location getLocation();
  
  boolean isStartElement();
  
  boolean isAttribute();
  
  boolean isNamespace();
  
  boolean isEndElement();
  
  boolean isEntityReference();
  
  boolean isProcessingInstruction();
  
  boolean isCharacters();
  
  boolean isStartDocument();
  
  boolean isEndDocument();
  
  StartElement asStartElement();
  
  EndElement asEndElement();
  
  Characters asCharacters();
  
  QName getSchemaType();
  
  void writeAsEncodedUnicode(Writer paramWriter) throws XMLStreamException;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\stream\events\XMLEvent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */