package 1.0.com.sun.xml.xsom.parser;

import com.sun.xml.xsom.parser.AnnotationContext;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;

public abstract class AnnotationParser {
  public abstract ContentHandler getContentHandler(AnnotationContext paramAnnotationContext, String paramString, ErrorHandler paramErrorHandler, EntityResolver paramEntityResolver);
  
  public abstract Object getResult(Object paramObject);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\parser\AnnotationParser.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */