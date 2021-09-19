package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
import com.sun.xml.txw2.annotation.XmlElement;

@XmlElement("complexContent")
public interface ComplexContent extends Annotated, TypedXmlWriter {
  @XmlElement
  ComplexExtension extension();
  
  @XmlElement
  ComplexRestriction restriction();
  
  @XmlAttribute
  ComplexContent mixed(boolean paramBoolean);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\xmlschema\ComplexContent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */