/*    */ package com.sun.istack;
/*    */ 
/*    */ import org.xml.sax.SAXException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SAXException2
/*    */   extends SAXException
/*    */ {
/*    */   public SAXException2(String message) {
/* 13 */     super(message);
/*    */   }
/*    */   
/*    */   public SAXException2(Exception e) {
/* 17 */     super(e);
/*    */   }
/*    */   
/*    */   public SAXException2(String message, Exception e) {
/* 21 */     super(message, e);
/*    */   }
/*    */   
/*    */   public Throwable getCause() {
/* 25 */     return getException();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\istack\SAXException2.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */