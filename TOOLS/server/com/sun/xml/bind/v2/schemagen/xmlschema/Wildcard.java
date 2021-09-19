package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;

public interface Wildcard extends Annotated, TypedXmlWriter {
  @XmlAttribute
  Wildcard processContents(String paramString);
  
  @XmlAttribute
  Wildcard namespace(String paramString);
  
  @XmlAttribute
  Wildcard namespace(String[] paramArrayOfString);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\xmlschema\Wildcard.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */