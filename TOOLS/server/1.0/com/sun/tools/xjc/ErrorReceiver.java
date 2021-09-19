/*     */ package 1.0.com.sun.tools.xjc;
/*     */ 
/*     */ import com.sun.tools.xjc.AbortException;
/*     */ import com.sun.tools.xjc.Messages;
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
/*     */ public abstract class ErrorReceiver
/*     */   implements ErrorHandler
/*     */ {
/*     */   public final void error(Locator loc, String msg) {
/*  44 */     error(new SAXParseException(msg, loc));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void warning(Locator loc, String msg) {
/*  52 */     warning(new SAXParseException(msg, loc));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void error(SAXParseException paramSAXParseException) throws AbortException;
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
/*     */   public abstract void info(SAXParseException paramSAXParseException);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void debug(String msg) {
/*  79 */     info(new SAXParseException(msg, null));
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
/*  96 */     if (e.getLineNumber() != -1 || e.getSystemId() != null) {
/*  97 */       int line = e.getLineNumber();
/*  98 */       return Messages.format("ConsoleErrorReporter.LineXOfY", (line == -1) ? "?" : Integer.toString(line), getShortName(e.getSystemId()));
/*     */     } 
/*     */ 
/*     */     
/* 102 */     return Messages.format("ConsoleErrorReporter.UnknownLocation");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String getShortName(String url) {
/* 108 */     if (url == null) {
/* 109 */       return Messages.format("ConsoleErrorReporter.UnknownFile");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 114 */     int idx = url.lastIndexOf('/');
/* 115 */     if (idx != -1) return url.substring(idx + 1); 
/* 116 */     idx = url.lastIndexOf('\\');
/* 117 */     if (idx != -1) return url.substring(idx + 1);
/*     */     
/* 119 */     return url;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\tools\xjc\ErrorReceiver.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */