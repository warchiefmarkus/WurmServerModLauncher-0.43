/*     */ package javax.xml.bind.helpers;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import javax.xml.bind.ValidationEvent;
/*     */ import javax.xml.bind.ValidationEventLocator;
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
/*     */ public class ValidationEventImpl
/*     */   implements ValidationEvent
/*     */ {
/*     */   private int severity;
/*     */   private String message;
/*     */   private Throwable linkedException;
/*     */   private ValidationEventLocator locator;
/*     */   
/*     */   public ValidationEventImpl(int _severity, String _message, ValidationEventLocator _locator) {
/*  44 */     this(_severity, _message, _locator, null);
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
/*     */ 
/*     */   
/*     */   public ValidationEventImpl(int _severity, String _message, ValidationEventLocator _locator, Throwable _linkedException) {
/*  63 */     setSeverity(_severity);
/*  64 */     this.message = _message;
/*  65 */     this.locator = _locator;
/*  66 */     this.linkedException = _linkedException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSeverity() {
/*  75 */     return this.severity;
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
/*     */   public void setSeverity(int _severity) {
/*  88 */     if (_severity != 0 && _severity != 1 && _severity != 2)
/*     */     {
/*     */       
/*  91 */       throw new IllegalArgumentException(Messages.format("ValidationEventImpl.IllegalSeverity"));
/*     */     }
/*     */ 
/*     */     
/*  95 */     this.severity = _severity;
/*     */   }
/*     */   
/*     */   public String getMessage() {
/*  99 */     return this.message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMessage(String _message) {
/* 107 */     this.message = _message;
/*     */   }
/*     */   
/*     */   public Throwable getLinkedException() {
/* 111 */     return this.linkedException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLinkedException(Throwable _linkedException) {
/* 119 */     this.linkedException = _linkedException;
/*     */   }
/*     */   
/*     */   public ValidationEventLocator getLocator() {
/* 123 */     return this.locator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocator(ValidationEventLocator _locator) {
/* 131 */     this.locator = _locator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 142 */     switch (getSeverity()) { case 0:
/* 143 */         s = "WARNING";
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 148 */         return MessageFormat.format("[severity={0},message={1},locator={2}]", new Object[] { s, getMessage(), getLocator() });case 1: s = "ERROR"; return MessageFormat.format("[severity={0},message={1},locator={2}]", new Object[] { s, getMessage(), getLocator() });case 2: s = "FATAL_ERROR"; return MessageFormat.format("[severity={0},message={1},locator={2}]", new Object[] { s, getMessage(), getLocator() }); }  String s = String.valueOf(getSeverity()); return MessageFormat.format("[severity={0},message={1},locator={2}]", new Object[] { s, getMessage(), getLocator() });
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\helpers\ValidationEventImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */