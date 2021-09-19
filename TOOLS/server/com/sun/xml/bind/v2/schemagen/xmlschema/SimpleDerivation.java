package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlElement;

public interface SimpleDerivation extends TypedXmlWriter {
  @XmlElement
  SimpleRestriction restriction();
  
  @XmlElement
  Union union();
  
  @XmlElement
  List list();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\xmlschema\SimpleDerivation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */