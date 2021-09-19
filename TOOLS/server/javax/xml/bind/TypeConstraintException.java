/*     */ package javax.xml.bind;
/*     */ 
/*     */ import java.io.PrintStream;
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
/*     */ public class TypeConstraintException
/*     */   extends RuntimeException
/*     */ {
/*     */   private String errorCode;
/*     */   private Throwable linkedException;
/*     */   
/*     */   public TypeConstraintException(String message) {
/*  51 */     this(message, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeConstraintException(String message, String errorCode) {
/*  62 */     this(message, errorCode, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeConstraintException(Throwable exception) {
/*  72 */     this(null, null, exception);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeConstraintException(String message, Throwable exception) {
/*  83 */     this(message, null, exception);
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
/*     */   public TypeConstraintException(String message, String errorCode, Throwable exception) {
/*  95 */     super(message);
/*  96 */     this.errorCode = errorCode;
/*  97 */     this.linkedException = exception;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getErrorCode() {
/* 106 */     return this.errorCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Throwable getLinkedException() {
/* 115 */     return this.linkedException;
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
/* 126 */     this.linkedException = exception;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 134 */     return (this.linkedException == null) ? super.toString() : (super.toString() + "\n - with linked exception:\n[" + this.linkedException.toString() + "]");
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
/* 147 */     if (this.linkedException != null) {
/* 148 */       this.linkedException.printStackTrace(s);
/* 149 */       s.println("--------------- linked to ------------------");
/*     */     } 
/*     */     
/* 152 */     super.printStackTrace(s);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void printStackTrace() {
/* 161 */     printStackTrace(System.err);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\TypeConstraintException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */