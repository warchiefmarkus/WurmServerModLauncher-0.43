/*     */ package com.sun.xml.bind.v2.model.annotation;
/*     */ 
/*     */ import com.sun.xml.bind.v2.model.core.ErrorHandler;
/*     */ import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
/*     */ import java.lang.annotation.Annotation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractInlineAnnotationReaderImpl<T, C, F, M>
/*     */   implements AnnotationReader<T, C, F, M>
/*     */ {
/*     */   private ErrorHandler errorHandler;
/*     */   
/*     */   public void setErrorHandler(ErrorHandler errorHandler) {
/*  58 */     if (errorHandler == null)
/*  59 */       throw new IllegalArgumentException(); 
/*  60 */     this.errorHandler = errorHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final ErrorHandler getErrorHandler() {
/*  67 */     assert this.errorHandler != null : "error handler must be set before use";
/*  68 */     return this.errorHandler;
/*     */   }
/*     */   
/*     */   public final <A extends Annotation> A getMethodAnnotation(Class<A> annotation, M getter, M setter, Locatable srcPos) {
/*  72 */     A a1 = (getter == null) ? null : (A)getMethodAnnotation(annotation, getter, srcPos);
/*  73 */     A a2 = (setter == null) ? null : (A)getMethodAnnotation(annotation, setter, srcPos);
/*     */     
/*  75 */     if (a1 == null) {
/*  76 */       if (a2 == null) {
/*  77 */         return null;
/*     */       }
/*  79 */       return a2;
/*     */     } 
/*  81 */     if (a2 == null) {
/*  82 */       return a1;
/*     */     }
/*     */     
/*  85 */     getErrorHandler().error(new IllegalAnnotationException(Messages.DUPLICATE_ANNOTATIONS.format(new Object[] { annotation.getName(), fullName(getter), fullName(setter) }, ), (Annotation)a1, (Annotation)a2));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  90 */     return a1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasMethodAnnotation(Class<? extends Annotation> annotation, String propertyName, M getter, M setter, Locatable srcPos) {
/*  96 */     boolean x = (getter != null && hasMethodAnnotation(annotation, getter));
/*  97 */     boolean y = (setter != null && hasMethodAnnotation(annotation, setter));
/*     */     
/*  99 */     if (x && y)
/*     */     {
/* 101 */       getMethodAnnotation(annotation, getter, setter, srcPos);
/*     */     }
/*     */     
/* 104 */     return (x || y);
/*     */   }
/*     */   
/*     */   protected abstract String fullName(M paramM);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\v2\model\annotation\AbstractInlineAnnotationReaderImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */