/*     */ package com.sun.xml.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.xml.bind.v2.model.core.ErrorHandler;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.xml.bind.JAXBException;
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
/*     */ public class IllegalAnnotationsException
/*     */   extends JAXBException
/*     */ {
/*     */   private final List<IllegalAnnotationException> errors;
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public IllegalAnnotationsException(List<IllegalAnnotationException> errors) {
/*  64 */     super(errors.size() + " counts of IllegalAnnotationExceptions");
/*  65 */     assert !errors.isEmpty() : "there must be at least one error";
/*  66 */     this.errors = Collections.unmodifiableList(new ArrayList<IllegalAnnotationException>(errors));
/*     */   }
/*     */   
/*     */   public String toString() {
/*  70 */     StringBuilder sb = new StringBuilder(super.toString());
/*  71 */     sb.append('\n');
/*     */     
/*  73 */     for (IllegalAnnotationException error : this.errors) {
/*  74 */       sb.append(error.toString()).append('\n');
/*     */     }
/*  76 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<IllegalAnnotationException> getErrors() {
/*  87 */     return this.errors;
/*     */   }
/*     */   
/*     */   public static class Builder implements ErrorHandler {
/*  91 */     private final List<IllegalAnnotationException> list = new ArrayList<IllegalAnnotationException>();
/*     */     public void error(IllegalAnnotationException e) {
/*  93 */       this.list.add(e);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void check() throws IllegalAnnotationsException {
/* 100 */       if (this.list.isEmpty())
/*     */         return; 
/* 102 */       throw new IllegalAnnotationsException(this.list);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\runtime\IllegalAnnotationsException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */