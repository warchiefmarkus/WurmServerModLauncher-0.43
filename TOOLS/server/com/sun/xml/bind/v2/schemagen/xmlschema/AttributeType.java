package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
import javax.xml.namespace.QName;

public interface AttributeType extends SimpleTypeHost, TypedXmlWriter {
  @XmlAttribute
  AttributeType type(QName paramQName);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\xmlschema\AttributeType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */