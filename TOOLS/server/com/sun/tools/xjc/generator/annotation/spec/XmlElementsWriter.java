package com.sun.tools.xjc.generator.annotation.spec;

import com.sun.codemodel.JAnnotationWriter;
import javax.xml.bind.annotation.XmlElements;

public interface XmlElementsWriter extends JAnnotationWriter<XmlElements> {
  XmlElementWriter value();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\annotation\spec\XmlElementsWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */