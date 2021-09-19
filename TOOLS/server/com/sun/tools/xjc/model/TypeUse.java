package com.sun.tools.xjc.model;

import com.sun.codemodel.JExpression;
import com.sun.tools.xjc.outline.Outline;
import com.sun.xml.bind.v2.model.core.ID;
import com.sun.xml.xsom.XmlString;
import javax.activation.MimeType;

public interface TypeUse {
  boolean isCollection();
  
  CAdapter getAdapterUse();
  
  CNonElement getInfo();
  
  ID idUse();
  
  MimeType getExpectedMimeType();
  
  JExpression createConstant(Outline paramOutline, XmlString paramXmlString);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\TypeUse.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */