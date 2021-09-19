package com.sun.tools.xjc.generator.annotation.spec;

import com.sun.codemodel.JAnnotationWriter;
import javax.xml.bind.annotation.XmlElementRefs;

public interface XmlElementRefsWriter extends JAnnotationWriter<XmlElementRefs> {
  XmlElementRefWriter value();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\annotation\spec\XmlElementRefsWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */