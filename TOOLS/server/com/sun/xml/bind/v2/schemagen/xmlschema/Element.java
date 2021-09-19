package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
import javax.xml.namespace.QName;

public interface Element extends Annotated, ComplexTypeHost, FixedOrDefault, SimpleTypeHost, TypedXmlWriter {
  @XmlAttribute
  Element type(QName paramQName);
  
  @XmlAttribute
  Element block(String paramString);
  
  @XmlAttribute
  Element block(String[] paramArrayOfString);
  
  @XmlAttribute
  Element nillable(boolean paramBoolean);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\xmlschema\Element.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */