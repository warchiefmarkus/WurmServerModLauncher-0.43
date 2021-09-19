/*    */ package com.sun.tools.jxc.apt;
/*    */ 
/*    */ import com.sun.mirror.apt.AnnotationProcessorEnvironment;
/*    */ import com.sun.mirror.apt.Messager;
/*    */ import com.sun.tools.xjc.ErrorReceiver;
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
/*    */ final class ErrorReceiverImpl
/*    */   extends ErrorReceiver
/*    */ {
/*    */   private final Messager messager;
/*    */   private final boolean debug;
/*    */   
/*    */   public ErrorReceiverImpl(Messager messager, boolean debug) {
/* 53 */     this.messager = messager;
/* 54 */     this.debug = debug;
/*    */   }
/*    */   
/*    */   public ErrorReceiverImpl(Messager messager) {
/* 58 */     this(messager, false);
/*    */   }
/*    */   
/*    */   public ErrorReceiverImpl(AnnotationProcessorEnvironment env) {
/* 62 */     this(env.getMessager());
/*    */   }
/*    */   
/*    */   public void error(SAXParseException exception) {
/* 66 */     this.messager.printError(exception.getMessage());
/* 67 */     this.messager.printError(getLocation(exception));
/* 68 */     printDetail(exception);
/*    */   }
/*    */   
/*    */   public void fatalError(SAXParseException exception) {
/* 72 */     this.messager.printError(exception.getMessage());
/* 73 */     this.messager.printError(getLocation(exception));
/* 74 */     printDetail(exception);
/*    */   }
/*    */   
/*    */   public void warning(SAXParseException exception) {
/* 78 */     this.messager.printWarning(exception.getMessage());
/* 79 */     this.messager.printWarning(getLocation(exception));
/* 80 */     printDetail(exception);
/*    */   }
/*    */   
/*    */   public void info(SAXParseException exception) {
/* 84 */     printDetail(exception);
/*    */   }
/*    */ 
/*    */   
/*    */   private String getLocation(SAXParseException e) {
/* 89 */     return "";
/*    */   }
/*    */   
/*    */   private void printDetail(SAXParseException e) {
/* 93 */     if (this.debug)
/* 94 */       e.printStackTrace(System.out); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\jxc\apt\ErrorReceiverImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */