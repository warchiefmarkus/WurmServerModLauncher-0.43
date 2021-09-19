package com.sun.tools.xjc.outline;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassContainer;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JType;
import com.sun.tools.xjc.ErrorReceiver;
import com.sun.tools.xjc.model.CClassInfo;
import com.sun.tools.xjc.model.CClassInfoParent;
import com.sun.tools.xjc.model.CElementInfo;
import com.sun.tools.xjc.model.CEnumLeafInfo;
import com.sun.tools.xjc.model.CPropertyInfo;
import com.sun.tools.xjc.model.CTypeRef;
import com.sun.tools.xjc.model.Model;
import com.sun.tools.xjc.util.CodeModelClassFactory;
import java.util.Collection;

public interface Outline {
  Model getModel();
  
  JCodeModel getCodeModel();
  
  FieldOutline getField(CPropertyInfo paramCPropertyInfo);
  
  PackageOutline getPackageContext(JPackage paramJPackage);
  
  Collection<? extends ClassOutline> getClasses();
  
  ClassOutline getClazz(CClassInfo paramCClassInfo);
  
  ElementOutline getElement(CElementInfo paramCElementInfo);
  
  EnumOutline getEnum(CEnumLeafInfo paramCEnumLeafInfo);
  
  Collection<EnumOutline> getEnums();
  
  Iterable<? extends PackageOutline> getAllPackageContexts();
  
  CodeModelClassFactory getClassFactory();
  
  ErrorReceiver getErrorReceiver();
  
  JClassContainer getContainer(CClassInfoParent paramCClassInfoParent, Aspect paramAspect);
  
  JType resolve(CTypeRef paramCTypeRef, Aspect paramAspect);
  
  JClass addRuntime(Class paramClass);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\outline\Outline.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */