package com.sun.xml.xsom;

import com.sun.xml.xsom.parser.SchemaDocument;
import com.sun.xml.xsom.visitor.XSFunction;
import com.sun.xml.xsom.visitor.XSVisitor;
import java.util.Collection;
import java.util.List;
import javax.xml.namespace.NamespaceContext;
import org.xml.sax.Locator;

public interface XSComponent {
  XSAnnotation getAnnotation();
  
  XSAnnotation getAnnotation(boolean paramBoolean);
  
  List<? extends ForeignAttributes> getForeignAttributes();
  
  String getForeignAttribute(String paramString1, String paramString2);
  
  Locator getLocator();
  
  XSSchema getOwnerSchema();
  
  XSSchemaSet getRoot();
  
  SchemaDocument getSourceDocument();
  
  Collection<XSComponent> select(String paramString, NamespaceContext paramNamespaceContext);
  
  XSComponent selectSingle(String paramString, NamespaceContext paramNamespaceContext);
  
  void visit(XSVisitor paramXSVisitor);
  
  <T> T apply(XSFunction<T> paramXSFunction);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\XSComponent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */