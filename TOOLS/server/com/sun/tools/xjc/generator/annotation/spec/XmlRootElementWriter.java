package com.sun.tools.xjc.generator.annotation.spec;

import com.sun.codemodel.JAnnotationWriter;
import javax.xml.bind.annotation.XmlRootElement;

public interface XmlRootElementWriter extends JAnnotationWriter<XmlRootElement> {
  XmlRootElementWriter name(String paramString);
  
  XmlRootElementWriter namespace(String paramString);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\annotation\spec\XmlRootElementWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */