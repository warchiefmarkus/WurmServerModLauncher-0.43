/*    */ package com.sun.xml.xsom.impl.util;
/*    */ 
/*    */ import org.xml.sax.ErrorHandler;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXParseException;
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
/*    */ public class DraconianErrorHandler
/*    */   implements ErrorHandler
/*    */ {
/*    */   public void error(SAXParseException e) throws SAXException {
/* 31 */     throw e;
/*    */   }
/*    */   public void fatalError(SAXParseException e) throws SAXException {
/* 34 */     throw e;
/*    */   }
/*    */   
/*    */   public void warning(SAXParseException e) {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xsom\imp\\util\DraconianErrorHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */