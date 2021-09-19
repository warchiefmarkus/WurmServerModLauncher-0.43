package com.sun.tools.xjc.generator.annotation.spec;

import com.sun.codemodel.JAnnotationWriter;
import com.sun.codemodel.JType;
import javax.xml.bind.annotation.XmlSeeAlso;

public interface XmlSeeAlsoWriter extends JAnnotationWriter<XmlSeeAlso> {
  XmlSeeAlsoWriter value(Class paramClass);
  
  XmlSeeAlsoWriter value(JType paramJType);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\annotation\spec\XmlSeeAlsoWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */