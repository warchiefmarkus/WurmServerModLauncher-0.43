package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlElement;

public interface ContentModelContainer extends TypedXmlWriter {
  @XmlElement
  LocalElement element();
  
  @XmlElement
  Any any();
  
  @XmlElement
  ExplicitGroup all();
  
  @XmlElement
  ExplicitGroup sequence();
  
  @XmlElement
  ExplicitGroup choice();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\xmlschema\ContentModelContainer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */