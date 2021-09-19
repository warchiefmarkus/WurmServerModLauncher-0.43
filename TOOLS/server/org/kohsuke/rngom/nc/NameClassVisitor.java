package org.kohsuke.rngom.nc;

import javax.xml.namespace.QName;

public interface NameClassVisitor<V> {
  V visitChoice(NameClass paramNameClass1, NameClass paramNameClass2);
  
  V visitNsName(String paramString);
  
  V visitNsNameExcept(String paramString, NameClass paramNameClass);
  
  V visitAnyName();
  
  V visitAnyNameExcept(NameClass paramNameClass);
  
  V visitName(QName paramQName);
  
  V visitNull();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\nc\NameClassVisitor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */