package com.sun.tools.xjc.outline;

import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JPackage;
import com.sun.tools.xjc.generator.bean.ObjectFactoryGenerator;
import java.util.Set;
import javax.xml.bind.annotation.XmlNsForm;

public interface PackageOutline {
  JPackage _package();
  
  JDefinedClass objectFactory();
  
  ObjectFactoryGenerator objectFactoryGenerator();
  
  Set<? extends ClassOutline> getClasses();
  
  String getMostUsedNamespaceURI();
  
  XmlNsForm getElementFormDefault();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\outline\PackageOutline.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */