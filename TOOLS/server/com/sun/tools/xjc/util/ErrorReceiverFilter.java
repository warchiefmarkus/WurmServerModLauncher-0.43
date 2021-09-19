/*    */ package com.sun.tools.xjc.util;
/*    */ 
/*    */ import com.sun.tools.xjc.ErrorReceiver;
/*    */ import com.sun.tools.xjc.api.ErrorListener;
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
/*    */ 
/*    */ 
/*    */ public class ErrorReceiverFilter
/*    */   extends ErrorReceiver
/*    */ {
/*    */   private ErrorListener core;
/*    */   
/*    */   public ErrorReceiverFilter() {}
/*    */   
/*    */   public ErrorReceiverFilter(ErrorListener h) {
/* 56 */     setErrorReceiver(h);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setErrorReceiver(ErrorListener handler) {
/* 61 */     this.core = handler;
/*    */   }
/*    */   private boolean hadError = false;
/*    */   public final boolean hadError() {
/* 65 */     return this.hadError;
/*    */   }
/*    */   public void info(SAXParseException exception) {
/* 68 */     if (this.core != null) this.core.info(exception); 
/*    */   }
/*    */   
/*    */   public void warning(SAXParseException exception) {
/* 72 */     if (this.core != null) this.core.warning(exception); 
/*    */   }
/*    */   
/*    */   public void error(SAXParseException exception) {
/* 76 */     this.hadError = true;
/* 77 */     if (this.core != null) this.core.error(exception); 
/*    */   }
/*    */   
/*    */   public void fatalError(SAXParseException exception) {
/* 81 */     this.hadError = true;
/* 82 */     if (this.core != null) this.core.fatalError(exception); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xj\\util\ErrorReceiverFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */