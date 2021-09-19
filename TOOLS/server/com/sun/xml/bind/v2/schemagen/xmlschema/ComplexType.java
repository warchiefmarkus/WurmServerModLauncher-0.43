package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
import com.sun.xml.txw2.annotation.XmlElement;

@XmlElement("complexType")
public interface ComplexType extends Annotated, ComplexTypeModel, TypedXmlWriter {
  @XmlAttribute("final")
  ComplexType _final(String paramString);
  
  @XmlAttribute("final")
  ComplexType _final(String[] paramArrayOfString);
  
  @XmlAttribute
  ComplexType block(String paramString);
  
  @XmlAttribute
  ComplexType block(String[] paramArrayOfString);
  
  @XmlAttribute("abstract")
  ComplexType _abstract(boolean paramBoolean);
  
  @XmlAttribute
  ComplexType name(String paramString);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\xmlschema\ComplexType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */