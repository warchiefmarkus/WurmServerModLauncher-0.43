package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlElement;

public interface SimpleTypeHost extends TypeHost, TypedXmlWriter {
  @XmlElement
  SimpleType simpleType();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\xmlschema\SimpleTypeHost.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */