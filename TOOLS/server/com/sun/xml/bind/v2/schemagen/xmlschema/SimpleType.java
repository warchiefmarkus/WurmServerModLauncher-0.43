package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
import com.sun.xml.txw2.annotation.XmlElement;

@XmlElement("simpleType")
public interface SimpleType extends Annotated, SimpleDerivation, TypedXmlWriter {
  @XmlAttribute("final")
  SimpleType _final(String paramString);
  
  @XmlAttribute("final")
  SimpleType _final(String[] paramArrayOfString);
  
  @XmlAttribute
  SimpleType name(String paramString);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\xmlschema\SimpleType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */