/*    */ package 1.0.com.sun.tools.xjc.util;
/*    */ 
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
/*    */ public class ErrorReceiverFilter
/*    */   extends ErrorReceiver
/*    */ {
/*    */   private ErrorReceiver core;
/*    */   
/*    */   public ErrorReceiverFilter() {}
/*    */   
/*    */   public ErrorReceiverFilter(ErrorReceiver h) {
/* 24 */     setErrorReceiver(h);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setErrorReceiver(ErrorReceiver handler) {
/* 29 */     this.core = handler;
/*    */   }
/*    */   public ErrorReceiver getErrorReceiver() {
/* 32 */     return this.core;
/*    */   }
/*    */   
/*    */   public final boolean hadError() {
/* 36 */     return this.hadError;
/*    */   } private boolean hadError = false;
/*    */   public void info(SAXParseException exception) {
/* 39 */     if (this.core != null) this.core.info(exception); 
/*    */   }
/*    */   
/*    */   public void warning(SAXParseException exception) {
/* 43 */     if (this.core != null) this.core.warning(exception); 
/*    */   }
/*    */   
/*    */   public void error(SAXParseException exception) {
/* 47 */     this.hadError = true;
/* 48 */     if (this.core != null) this.core.error(exception); 
/*    */   }
/*    */   
/*    */   public void fatalError(SAXParseException exception) {
/* 52 */     this.hadError = true;
/* 53 */     if (this.core != null) this.core.fatalError(exception); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xj\\util\ErrorReceiverFilter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */