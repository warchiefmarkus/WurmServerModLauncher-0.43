/*     */ package com.sun.org.apache.xml.internal.resolver;
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
/*     */ public class CatalogException
/*     */   extends Exception
/*     */ {
/*     */   public static final int WRAPPER = 1;
/*     */   public static final int INVALID_ENTRY = 2;
/*     */   public static final int INVALID_ENTRY_TYPE = 3;
/*     */   public static final int NO_XML_PARSER = 4;
/*     */   public static final int UNKNOWN_FORMAT = 5;
/*     */   public static final int UNPARSEABLE = 6;
/*     */   public static final int PARSE_FAILED = 7;
/*     */   public static final int UNENDED_COMMENT = 8;
/*  56 */   private Exception exception = null;
/*  57 */   private int exceptionType = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CatalogException(int type, String message) {
/*  66 */     super(message);
/*  67 */     this.exceptionType = type;
/*  68 */     this.exception = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CatalogException(int type) {
/*  77 */     super("Catalog Exception " + type);
/*  78 */     this.exceptionType = type;
/*  79 */     this.exception = null;
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
/*     */   public CatalogException(Exception e) {
/*  93 */     this.exceptionType = 1;
/*  94 */     this.exception = e;
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
/*     */   public CatalogException(String message, Exception e) {
/* 107 */     super(message);
/* 108 */     this.exceptionType = 1;
/* 109 */     this.exception = e;
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
/*     */   public String getMessage() {
/* 123 */     String message = super.getMessage();
/*     */     
/* 125 */     if (message == null && this.exception != null) {
/* 126 */       return this.exception.getMessage();
/*     */     }
/* 128 */     return message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Exception getException() {
/* 139 */     return this.exception;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getExceptionType() {
/* 149 */     return this.exceptionType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 159 */     if (this.exception != null) {
/* 160 */       return this.exception.toString();
/*     */     }
/* 162 */     return super.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\org\apache\xml\internal\resolver\CatalogException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */