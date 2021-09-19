/*    */ package 1.0.com.sun.xml.xsom.impl.parser;
/*    */ 
/*    */ import com.sun.xml.xsom.parser.AnnotationContext;
/*    */ import com.sun.xml.xsom.parser.AnnotationParser;
/*    */ import org.xml.sax.ContentHandler;
/*    */ import org.xml.sax.EntityResolver;
/*    */ import org.xml.sax.ErrorHandler;
/*    */ import org.xml.sax.helpers.DefaultHandler;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class DefaultAnnotationParser
/*    */   extends AnnotationParser
/*    */ {
/* 31 */   public static final AnnotationParser theInstance = new com.sun.xml.xsom.impl.parser.DefaultAnnotationParser();
/*    */ 
/*    */ 
/*    */   
/*    */   public ContentHandler getContentHandler(AnnotationContext contest, String elementName, ErrorHandler errorHandler, EntityResolver entityResolver) {
/* 36 */     return new DefaultHandler();
/*    */   }
/*    */   
/*    */   public Object getResult(Object existing) {
/* 40 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xsom\impl\parser\DefaultAnnotationParser.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */