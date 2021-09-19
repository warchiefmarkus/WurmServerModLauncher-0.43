package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
import com.sun.xml.txw2.annotation.XmlElement;

public interface ComplexTypeModel extends AttrDecls, TypeDefParticle, TypedXmlWriter {
  @XmlElement
  SimpleContent simpleContent();
  
  @XmlElement
  ComplexContent complexContent();
  
  @XmlAttribute
  ComplexTypeModel mixed(boolean paramBoolean);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\xmlschema\ComplexTypeModel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */