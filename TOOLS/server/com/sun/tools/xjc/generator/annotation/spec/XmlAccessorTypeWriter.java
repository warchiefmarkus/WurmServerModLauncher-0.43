package com.sun.tools.xjc.generator.annotation.spec;

import com.sun.codemodel.JAnnotationWriter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

public interface XmlAccessorTypeWriter extends JAnnotationWriter<XmlAccessorType> {
  XmlAccessorTypeWriter value(XmlAccessType paramXmlAccessType);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\annotation\spec\XmlAccessorTypeWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */