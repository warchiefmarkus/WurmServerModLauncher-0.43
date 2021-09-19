package com.sun.tools.xjc.generator.annotation.spec;

import com.sun.codemodel.JAnnotationWriter;
import javax.xml.bind.annotation.XmlElementWrapper;

public interface XmlElementWrapperWriter extends JAnnotationWriter<XmlElementWrapper> {
  XmlElementWrapperWriter name(String paramString);
  
  XmlElementWrapperWriter namespace(String paramString);
  
  XmlElementWrapperWriter required(boolean paramBoolean);
  
  XmlElementWrapperWriter nillable(boolean paramBoolean);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\annotation\spec\XmlElementWrapperWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */