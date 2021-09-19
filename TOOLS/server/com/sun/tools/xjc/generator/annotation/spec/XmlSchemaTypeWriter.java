package com.sun.tools.xjc.generator.annotation.spec;

import com.sun.codemodel.JAnnotationWriter;
import com.sun.codemodel.JType;
import javax.xml.bind.annotation.XmlSchemaType;

public interface XmlSchemaTypeWriter extends JAnnotationWriter<XmlSchemaType> {
  XmlSchemaTypeWriter name(String paramString);
  
  XmlSchemaTypeWriter type(Class paramClass);
  
  XmlSchemaTypeWriter type(JType paramJType);
  
  XmlSchemaTypeWriter namespace(String paramString);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\annotation\spec\XmlSchemaTypeWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */