package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
import com.sun.xml.txw2.annotation.XmlElement;
import javax.xml.namespace.QName;

@XmlElement("element")
public interface LocalElement extends Element, Occurs, TypedXmlWriter {
  @XmlAttribute
  LocalElement form(String paramString);
  
  @XmlAttribute
  LocalElement name(String paramString);
  
  @XmlAttribute
  LocalElement ref(QName paramQName);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\xmlschema\LocalElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */