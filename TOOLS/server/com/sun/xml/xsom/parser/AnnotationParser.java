package com.sun.xml.xsom.parser;

import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

public abstract class AnnotationParser {
  public abstract ContentHandler getContentHandler(AnnotationContext paramAnnotationContext, String paramString, ErrorHandler paramErrorHandler, EntityResolver paramEntityResolver);
  
  public abstract Object getResult(Object paramObject);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\parser\AnnotationParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */