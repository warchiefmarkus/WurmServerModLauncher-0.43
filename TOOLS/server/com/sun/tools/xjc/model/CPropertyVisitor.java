package com.sun.tools.xjc.model;

public interface CPropertyVisitor<V> {
  V onElement(CElementPropertyInfo paramCElementPropertyInfo);
  
  V onAttribute(CAttributePropertyInfo paramCAttributePropertyInfo);
  
  V onValue(CValuePropertyInfo paramCValuePropertyInfo);
  
  V onReference(CReferencePropertyInfo paramCReferencePropertyInfo);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\CPropertyVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */