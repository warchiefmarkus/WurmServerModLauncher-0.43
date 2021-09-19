/*    */ package 1.0.com.sun.tools.xjc.reader.xmlschema;
/*    */ 
/*    */ import com.sun.tools.xjc.ErrorReceiver;
/*    */ import com.sun.tools.xjc.reader.xmlschema.Messages;
/*    */ import org.xml.sax.Locator;
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
/*    */ public final class ErrorReporter
/*    */ {
/*    */   private ErrorReceiver errorReceiver;
/*    */   private boolean hadError = false;
/*    */   
/*    */   ErrorReporter(ErrorReceiver handler) {
/* 43 */     setErrorHandler(handler);
/*    */   }
/*    */   boolean hadError() {
/* 46 */     return this.hadError;
/*    */   }
/*    */   void setErrorHandler(ErrorReceiver h) {
/* 49 */     this.errorReceiver = h;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   void error(Locator loc, String prop) {
/* 59 */     error(loc, prop, new Object[0]);
/*    */   }
/*    */   void error(Locator loc, String prop, Object arg1) {
/* 62 */     error(loc, prop, new Object[] { arg1 });
/*    */   }
/*    */   void error(Locator loc, String prop, Object arg1, Object arg2) {
/* 65 */     error(loc, prop, new Object[] { arg1, arg2 });
/*    */   }
/*    */   void error(Locator loc, String prop, Object arg1, Object arg2, Object arg3) {
/* 68 */     error(loc, prop, new Object[] { arg1, arg2, arg3 });
/*    */   }
/*    */   void error(Locator loc, String prop, Object[] args) {
/* 71 */     this.errorReceiver.error(loc, Messages.format(prop, args));
/*    */   }
/*    */   
/*    */   void warning(Locator loc, String prop, Object[] args) {
/* 75 */     this.errorReceiver.warning(new SAXParseException(Messages.format(prop, args), loc));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\reader\xmlschema\ErrorReporter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */