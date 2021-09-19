/*    */ package com.sun.istack;
/*    */ 
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.SAXParseException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SAXParseException2
/*    */   extends SAXParseException
/*    */ {
/*    */   public SAXParseException2(String message, Locator locator) {
/* 14 */     super(message, locator);
/*    */   }
/*    */   
/*    */   public SAXParseException2(String message, Locator locator, Exception e) {
/* 18 */     super(message, locator, e);
/*    */   }
/*    */   
/*    */   public SAXParseException2(String message, String publicId, String systemId, int lineNumber, int columnNumber) {
/* 22 */     super(message, publicId, systemId, lineNumber, columnNumber);
/*    */   }
/*    */   
/*    */   public SAXParseException2(String message, String publicId, String systemId, int lineNumber, int columnNumber, Exception e) {
/* 26 */     super(message, publicId, systemId, lineNumber, columnNumber, e);
/*    */   }
/*    */   
/*    */   public Throwable getCause() {
/* 30 */     return getException();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\istack\SAXParseException2.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */