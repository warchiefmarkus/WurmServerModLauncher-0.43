/*     */ package com.sun.tools.xjc;
/*     */ 
/*     */ import com.sun.istack.SAXParseException2;
/*     */ import com.sun.tools.xjc.api.ErrorListener;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ErrorReceiver
/*     */   implements ErrorHandler, ErrorListener
/*     */ {
/*     */   public final void error(Locator loc, String msg) {
/*  82 */     error((SAXParseException)new SAXParseException2(msg, loc));
/*     */   }
/*     */   
/*     */   public final void error(Locator loc, String msg, Exception e) {
/*  86 */     error((SAXParseException)new SAXParseException2(msg, loc, e));
/*     */   }
/*     */   
/*     */   public final void error(String msg, Exception e) {
/*  90 */     error((SAXParseException)new SAXParseException2(msg, null, e));
/*     */   }
/*     */   
/*     */   public void error(Exception e) {
/*  94 */     error(e.getMessage(), e);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void warning(Locator loc, String msg) {
/* 102 */     warning(new SAXParseException(msg, loc));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void error(SAXParseException paramSAXParseException) throws AbortException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void fatalError(SAXParseException paramSAXParseException) throws AbortException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void warning(SAXParseException paramSAXParseException) throws AbortException;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void pollAbort() throws AbortException {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void info(SAXParseException paramSAXParseException);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void debug(String msg) {
/* 136 */     info(new SAXParseException(msg, null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String getLocationString(SAXParseException e) {
/* 153 */     if (e.getLineNumber() != -1 || e.getSystemId() != null) {
/* 154 */       int line = e.getLineNumber();
/* 155 */       return Messages.format("ConsoleErrorReporter.LineXOfY", new Object[] { (line == -1) ? "?" : Integer.toString(line), getShortName(e.getSystemId()) });
/*     */     } 
/*     */ 
/*     */     
/* 159 */     return Messages.format("ConsoleErrorReporter.UnknownLocation", new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String getShortName(String url) {
/* 165 */     if (url == null) {
/* 166 */       return Messages.format("ConsoleErrorReporter.UnknownFile", new Object[0]);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 181 */     return url;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\ErrorReceiver.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */