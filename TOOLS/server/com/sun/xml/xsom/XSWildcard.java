package com.sun.xml.xsom;

import com.sun.xml.xsom.visitor.XSWildcardFunction;
import com.sun.xml.xsom.visitor.XSWildcardVisitor;
import java.util.Collection;
import java.util.Iterator;

public interface XSWildcard extends XSComponent, XSTerm {
  public static final int LAX = 1;
  
  public static final int STRTICT = 2;
  
  public static final int SKIP = 3;
  
  int getMode();
  
  boolean acceptsNamespace(String paramString);
  
  void visit(XSWildcardVisitor paramXSWildcardVisitor);
  
  <T> T apply(XSWildcardFunction<T> paramXSWildcardFunction);
  
  public static interface Union extends XSWildcard {
    Iterator<String> iterateNamespaces();
    
    Collection<String> getNamespaces();
  }
  
  public static interface Other extends XSWildcard {
    String getOtherNamespace();
  }
  
  public static interface Any extends XSWildcard {}
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\XSWildcard.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */