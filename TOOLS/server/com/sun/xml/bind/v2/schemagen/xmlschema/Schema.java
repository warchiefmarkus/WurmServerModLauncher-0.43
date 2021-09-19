package com.sun.xml.bind.v2.schemagen.xmlschema;

import com.sun.xml.txw2.TypedXmlWriter;
import com.sun.xml.txw2.annotation.XmlAttribute;
import com.sun.xml.txw2.annotation.XmlElement;

@XmlElement("schema")
public interface Schema extends SchemaTop, TypedXmlWriter {
  @XmlElement
  Annotation annotation();
  
  @XmlElement("import")
  Import _import();
  
  @XmlAttribute
  Schema targetNamespace(String paramString);
  
  @XmlAttribute(ns = "http://www.w3.org/XML/1998/namespace")
  Schema lang(String paramString);
  
  @XmlAttribute
  Schema id(String paramString);
  
  @XmlAttribute
  Schema elementFormDefault(String paramString);
  
  @XmlAttribute
  Schema attributeFormDefault(String paramString);
  
  @XmlAttribute
  Schema blockDefault(String paramString);
  
  @XmlAttribute
  Schema blockDefault(String[] paramArrayOfString);
  
  @XmlAttribute
  Schema finalDefault(String paramString);
  
  @XmlAttribute
  Schema finalDefault(String[] paramArrayOfString);
  
  @XmlAttribute
  Schema version(String paramString);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\schemagen\xmlschema\Schema.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */