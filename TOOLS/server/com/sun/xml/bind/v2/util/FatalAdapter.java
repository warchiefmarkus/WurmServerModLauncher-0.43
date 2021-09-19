/*    */ package com.sun.xml.bind.v2.util;
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
/*    */ public class FatalAdapter
/*    */   implements ErrorHandler
/*    */ {
/*    */   private final ErrorHandler core;
/*    */   
/*    */   public FatalAdapter(ErrorHandler handler) {
/* 52 */     this.core = handler;
/*    */   }
/*    */   
/*    */   public void warning(SAXParseException exception) throws SAXException {
/* 56 */     this.core.warning(exception);
/*    */   }
/*    */   
/*    */   public void error(SAXParseException exception) throws SAXException {
/* 60 */     this.core.fatalError(exception);
/*    */   }
/*    */   
/*    */   public void fatalError(SAXParseException exception) throws SAXException {
/* 64 */     this.core.fatalError(exception);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v\\util\FatalAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */