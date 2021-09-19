package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
import com.sun.xml.txw2.annotation.XmlElement;

@XmlElement("appinfo")
public interface Appinfo extends TypedXmlWriter {
  @XmlAttribute
  Appinfo source(String paramString);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\xmlschema\Appinfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */