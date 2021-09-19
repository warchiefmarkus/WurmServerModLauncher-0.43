/*    */ package com.sun.xml.xsom.impl.parser;
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
/* 41 */   public static final AnnotationParser theInstance = new DefaultAnnotationParser();
/*    */ 
/*    */ 
/*    */   
/*    */   public ContentHandler getContentHandler(AnnotationContext contest, String elementName, ErrorHandler errorHandler, EntityResolver entityResolver) {
/* 46 */     return new DefaultHandler();
/*    */   }
/*    */   
/*    */   public Object getResult(Object existing) {
/* 50 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\impl\parser\DefaultAnnotationParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */