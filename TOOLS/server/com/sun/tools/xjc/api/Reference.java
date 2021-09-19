/*     */ package com.sun.tools.xjc.api;
/*     */ 
/*     */ import com.sun.mirror.apt.AnnotationProcessorEnvironment;
/*     */ import com.sun.mirror.declaration.Declaration;
/*     */ import com.sun.mirror.declaration.MethodDeclaration;
/*     */ import com.sun.mirror.declaration.ParameterDeclaration;
/*     */ import com.sun.mirror.declaration.TypeDeclaration;
/*     */ import com.sun.mirror.type.TypeMirror;
/*     */ import com.sun.mirror.util.SourcePosition;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Reference
/*     */ {
/*     */   public final TypeMirror type;
/*     */   public final Declaration annotations;
/*     */   
/*     */   public Reference(MethodDeclaration method) {
/*  78 */     this(method.getReturnType(), (Declaration)method);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reference(ParameterDeclaration param) {
/*  86 */     this(param.getType(), (Declaration)param);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reference(TypeDeclaration type, AnnotationProcessorEnvironment env) {
/*  93 */     this((TypeMirror)env.getTypeUtils().getDeclaredType(type, new TypeMirror[0]), (Declaration)type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Reference(TypeMirror type, Declaration annotations) {
/* 100 */     if (type == null || annotations == null)
/* 101 */       throw new IllegalArgumentException(); 
/* 102 */     this.type = type;
/* 103 */     this.annotations = annotations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SourcePosition getPosition() {
/* 111 */     return this.annotations.getPosition();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/* 115 */     if (this == o) return true; 
/* 116 */     if (!(o instanceof Reference)) return false;
/*     */     
/* 118 */     Reference that = (Reference)o;
/*     */     
/* 120 */     return (this.annotations.equals(that.annotations) && this.type.equals(that.type));
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 124 */     return 29 * this.type.hashCode() + this.annotations.hashCode();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\api\Reference.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */