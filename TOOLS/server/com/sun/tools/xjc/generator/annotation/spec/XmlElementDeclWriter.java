package com.sun.tools.xjc.generator.annotation.spec;

import com.sun.codemodel.JAnnotationWriter;
import com.sun.codemodel.JType;
import javax.xml.bind.annotation.XmlElementDecl;

public interface XmlElementDeclWriter extends JAnnotationWriter<XmlElementDecl> {
  XmlElementDeclWriter name(String paramString);
  
  XmlElementDeclWriter namespace(String paramString);
  
  XmlElementDeclWriter defaultValue(String paramString);
  
  XmlElementDeclWriter scope(Class paramClass);
  
  XmlElementDeclWriter scope(JType paramJType);
  
  XmlElementDeclWriter substitutionHeadNamespace(String paramString);
  
  XmlElementDeclWriter substitutionHeadName(String paramString);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\annotation\spec\XmlElementDeclWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */