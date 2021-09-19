package com.sun.tools.xjc.generator.annotation.spec;

import com.sun.codemodel.JAnnotationWriter;
import javax.xml.bind.annotation.XmlAttribute;

public interface XmlAttributeWriter extends JAnnotationWriter<XmlAttribute> {
  XmlAttributeWriter name(String paramString);
  
  XmlAttributeWriter namespace(String paramString);
  
  XmlAttributeWriter required(boolean paramBoolean);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\annotation\spec\XmlAttributeWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */