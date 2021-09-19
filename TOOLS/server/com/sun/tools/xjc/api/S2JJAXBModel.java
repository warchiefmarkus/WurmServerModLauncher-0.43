package com.sun.tools.xjc.api;

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.tools.xjc.Plugin;
import java.util.Collection;
import java.util.List;
import javax.xml.namespace.QName;

public interface S2JJAXBModel extends JAXBModel {
  Mapping get(QName paramQName);
  
  List<JClass> getAllObjectFactories();
  
  Collection<? extends Mapping> getMappings();
  
  TypeAndAnnotation getJavaType(QName paramQName);
  
  JCodeModel generateCode(Plugin[] paramArrayOfPlugin, ErrorListener paramErrorListener);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\api\S2JJAXBModel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */