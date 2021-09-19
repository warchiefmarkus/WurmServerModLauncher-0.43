package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlElement;

public interface AttrDecls extends TypedXmlWriter {
  @XmlElement
  LocalAttribute attribute();
  
  @XmlElement
  Wildcard anyAttribute();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\xmlschema\AttrDecls.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */