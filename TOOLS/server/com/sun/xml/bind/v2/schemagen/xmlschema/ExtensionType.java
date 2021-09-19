package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
import javax.xml.namespace.QName;

public interface ExtensionType extends Annotated, TypedXmlWriter {
  @XmlAttribute
  ExtensionType base(QName paramQName);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\xmlschema\ExtensionType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */