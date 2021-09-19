/*     */ package javax.xml.bind;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
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
/*     */ public class JAXBException
/*     */   extends Exception
/*     */ {
/*     */   private String errorCode;
/*     */   private Throwable linkedException;
/*     */   static final long serialVersionUID = -5621384651494307979L;
/*     */   
/*     */   public JAXBException(String message) {
/*  43 */     this(message, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAXBException(String message, String errorCode) {
/*  54 */     this(message, errorCode, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAXBException(Throwable exception) {
/*  64 */     this(null, null, exception);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAXBException(String message, Throwable exception) {
/*  75 */     this(message, null, exception);
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
/*     */   public JAXBException(String message, String errorCode, Throwable exception) {
/*  87 */     super(message);
/*  88 */     this.errorCode = errorCode;
/*  89 */     this.linkedException = exception;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getErrorCode() {
/*  98 */     return this.errorCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getLinkedException() {
/* 107 */     return this.linkedException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setLinkedException(Throwable exception) {
/* 118 */     this.linkedException = exception;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 126 */     return (this.linkedException == null) ? super.toString() : (super.toString() + "\n - with linked exception:\n[" + this.linkedException.toString() + "]");
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
/*     */   public void printStackTrace(PrintStream s) {
/* 139 */     super.printStackTrace(s);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printStackTrace() {
/* 148 */     super.printStackTrace();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printStackTrace(PrintWriter s) {
/* 158 */     super.printStackTrace(s);
/*     */   }
/*     */ 
/*     */   
/*     */   public Throwable getCause() {
/* 163 */     return this.linkedException;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\JAXBException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */