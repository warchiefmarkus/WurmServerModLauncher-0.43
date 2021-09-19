package com.sun.tools.xjc.generator.annotation.spec;

import com.sun.codemodel.JAnnotationWriter;
import com.sun.codemodel.JType;
import javax.xml.bind.annotation.XmlElement;

public interface XmlElementWriter extends JAnnotationWriter<XmlElement> {
  XmlElementWriter name(String paramString);
  
  XmlElementWriter type(Class paramClass);
  
  XmlElementWriter type(JType paramJType);
  
  XmlElementWriter namespace(String paramString);
  
  XmlElementWriter defaultValue(String paramString);
  
  XmlElementWriter required(boolean paramBoolean);
  
  XmlElementWriter nillable(boolean paramBoolean);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\annotation\spec\XmlElementWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */