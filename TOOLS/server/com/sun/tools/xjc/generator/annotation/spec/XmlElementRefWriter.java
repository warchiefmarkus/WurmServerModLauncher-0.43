package com.sun.tools.xjc.generator.annotation.spec;

import com.sun.codemodel.JAnnotationWriter;
import com.sun.codemodel.JType;
import javax.xml.bind.annotation.XmlElementRef;

public interface XmlElementRefWriter extends JAnnotationWriter<XmlElementRef> {
  XmlElementRefWriter name(String paramString);
  
  XmlElementRefWriter type(Class paramClass);
  
  XmlElementRefWriter type(JType paramJType);
  
  XmlElementRefWriter namespace(String paramString);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\annotation\spec\XmlElementRefWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */