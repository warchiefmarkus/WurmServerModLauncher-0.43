package com.sun.tools.xjc.generator.annotation.spec;

import com.sun.codemodel.JAnnotationWriter;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;

public interface XmlSchemaWriter extends JAnnotationWriter<XmlSchema> {
  XmlSchemaWriter location(String paramString);
  
  XmlSchemaWriter namespace(String paramString);
  
  XmlNsWriter xmlns();
  
  XmlSchemaWriter elementFormDefault(XmlNsForm paramXmlNsForm);
  
  XmlSchemaWriter attributeFormDefault(XmlNsForm paramXmlNsForm);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\annotation\spec\XmlSchemaWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */