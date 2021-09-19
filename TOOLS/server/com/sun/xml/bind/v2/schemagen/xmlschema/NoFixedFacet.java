package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;

public interface NoFixedFacet extends Annotated, TypedXmlWriter {
  @XmlAttribute
  NoFixedFacet value(String paramString);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\xmlschema\NoFixedFacet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */