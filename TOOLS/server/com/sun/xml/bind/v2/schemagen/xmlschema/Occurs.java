package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;

public interface Occurs extends TypedXmlWriter {
  @XmlAttribute
  Occurs minOccurs(int paramInt);
  
  @XmlAttribute
  Occurs maxOccurs(String paramString);
  
  @XmlAttribute
  Occurs maxOccurs(int paramInt);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\xmlschema\Occurs.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */