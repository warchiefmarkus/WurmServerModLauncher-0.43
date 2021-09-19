package com.sun.tools.xjc.api;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import java.util.Collection;
import java.util.Map;
import javax.xml.namespace.QName;

public interface JavaCompiler {
  J2SJAXBModel bind(Collection<Reference> paramCollection, Map<QName, Reference> paramMap, String paramString, AnnotationProcessorEnvironment paramAnnotationProcessorEnvironment);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\api\JavaCompiler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */