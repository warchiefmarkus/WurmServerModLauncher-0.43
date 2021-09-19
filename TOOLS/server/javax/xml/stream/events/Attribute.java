package javax.xml.stream.events;

import javax.xml.namespace.QName;

public interface Attribute extends XMLEvent {
  QName getName();
  
  String getValue();
  
  String getDTDType();
  
  boolean isSpecified();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\stream\events\Attribute.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */