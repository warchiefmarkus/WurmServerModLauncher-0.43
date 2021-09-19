package com.sun.tools.xjc.generator.annotation.spec;

import com.sun.codemodel.JAnnotationWriter;
import com.sun.codemodel.JType;
import javax.xml.bind.annotation.XmlType;

public interface XmlTypeWriter extends JAnnotationWriter<XmlType> {
  XmlTypeWriter name(String paramString);
  
  XmlTypeWriter namespace(String paramString);
  
  XmlTypeWriter propOrder(String paramString);
  
  XmlTypeWriter factoryClass(Class paramClass);
  
  XmlTypeWriter factoryClass(JType paramJType);
  
  XmlTypeWriter factoryMethod(String paramString);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\annotation\spec\XmlTypeWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */