/*     */ package com.sun.codemodel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JPrimitiveType
/*     */   extends JType
/*     */ {
/*     */   private final String typeName;
/*     */   private final JCodeModel owner;
/*     */   private final JClass wrapperClass;
/*     */   private JClass arrayClass;
/*     */   
/*     */   JPrimitiveType(JCodeModel owner, String typeName, Class wrapper) {
/*  41 */     this.owner = owner;
/*  42 */     this.typeName = typeName;
/*  43 */     this.wrapperClass = owner.ref(wrapper);
/*     */   }
/*     */   public JCodeModel owner() {
/*  46 */     return this.owner;
/*     */   }
/*     */   public String fullName() {
/*  49 */     return this.typeName;
/*     */   }
/*     */   
/*     */   public String name() {
/*  53 */     return fullName();
/*     */   }
/*     */   
/*     */   public boolean isPrimitive() {
/*  57 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public JClass array() {
/*  62 */     if (this.arrayClass == null)
/*  63 */       this.arrayClass = new JArrayClass(this.owner, this); 
/*  64 */     return this.arrayClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass boxify() {
/*  73 */     return this.wrapperClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JType unboxify() {
/*  82 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass getWrapperClass() {
/*  90 */     return boxify();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JExpression wrap(JExpression exp) {
/* 101 */     return JExpr._new(boxify()).arg(exp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JExpression unwrap(JExpression exp) {
/* 112 */     return exp.invoke(this.typeName + "Value");
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 116 */     f.p(this.typeName);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JPrimitiveType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */