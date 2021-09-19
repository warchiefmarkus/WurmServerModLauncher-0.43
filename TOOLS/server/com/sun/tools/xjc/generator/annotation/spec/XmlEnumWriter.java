package com.sun.tools.xjc.generator.annotation.spec;

import com.sun.codemodel.JAnnotationWriter;
import com.sun.codemodel.JType;
import javax.xml.bind.annotation.XmlEnum;

public interface XmlEnumWriter extends JAnnotationWriter<XmlEnum> {
  XmlEnumWriter value(Class paramClass);
  
  XmlEnumWriter value(JType paramJType);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\annotation\spec\XmlEnumWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */