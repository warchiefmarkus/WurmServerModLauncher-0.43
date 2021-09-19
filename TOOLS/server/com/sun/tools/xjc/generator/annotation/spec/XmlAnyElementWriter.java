package com.sun.tools.xjc.generator.annotation.spec;

import com.sun.codemodel.JAnnotationWriter;
import com.sun.codemodel.JType;
import javax.xml.bind.annotation.XmlAnyElement;

public interface XmlAnyElementWriter extends JAnnotationWriter<XmlAnyElement> {
  XmlAnyElementWriter value(Class paramClass);
  
  XmlAnyElementWriter value(JType paramJType);
  
  XmlAnyElementWriter lax(boolean paramBoolean);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\annotation\spec\XmlAnyElementWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */