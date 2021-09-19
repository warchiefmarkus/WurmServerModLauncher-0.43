package com.sun.tools.xjc.generator.annotation.spec;

import com.sun.codemodel.JAnnotationWriter;
import javax.xml.bind.annotation.XmlNs;

public interface XmlNsWriter extends JAnnotationWriter<XmlNs> {
  XmlNsWriter prefix(String paramString);
  
  XmlNsWriter namespaceURI(String paramString);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\annotation\spec\XmlNsWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */