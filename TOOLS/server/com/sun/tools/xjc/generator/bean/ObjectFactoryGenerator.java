package com.sun.tools.xjc.generator.bean;

import com.sun.codemodel.JDefinedClass;
import com.sun.tools.xjc.model.CElementInfo;

public abstract class ObjectFactoryGenerator {
  abstract void populate(CElementInfo paramCElementInfo);
  
  abstract void populate(ClassOutlineImpl paramClassOutlineImpl);
  
  public abstract JDefinedClass getObjectFactory();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\generator\bean\ObjectFactoryGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */