package com.sun.tools.xjc.generator.annotation.spec;

import com.sun.codemodel.JAnnotationWriter;
import com.sun.codemodel.JType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

public interface XmlJavaTypeAdapterWriter extends JAnnotationWriter<XmlJavaTypeAdapter> {
  XmlJavaTypeAdapterWriter value(Class paramClass);
  
  XmlJavaTypeAdapterWriter value(JType paramJType);
  
  XmlJavaTypeAdapterWriter type(Class paramClass);
  
  XmlJavaTypeAdapterWriter type(JType paramJType);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\annotation\spec\XmlJavaTypeAdapterWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */