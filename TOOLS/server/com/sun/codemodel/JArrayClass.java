/*     */ package com.sun.codemodel;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class JArrayClass
/*     */   extends JClass
/*     */ {
/*     */   private final JType componentType;
/*     */   
/*     */   JArrayClass(JCodeModel owner, JType component) {
/*  39 */     super(owner);
/*  40 */     this.componentType = component;
/*     */   }
/*     */ 
/*     */   
/*     */   public String name() {
/*  45 */     return this.componentType.name() + "[]";
/*     */   }
/*     */   
/*     */   public String fullName() {
/*  49 */     return this.componentType.fullName() + "[]";
/*     */   }
/*     */   
/*     */   public String binaryName() {
/*  53 */     return this.componentType.binaryName() + "[]";
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/*  57 */     f.g(this.componentType).p("[]");
/*     */   }
/*     */   
/*     */   public JPackage _package() {
/*  61 */     return owner().rootPackage();
/*     */   }
/*     */   
/*     */   public JClass _extends() {
/*  65 */     return owner().ref(Object.class);
/*     */   }
/*     */   
/*     */   public Iterator<JClass> _implements() {
/*  69 */     return Collections.<JClass>emptyList().iterator();
/*     */   }
/*     */   
/*     */   public boolean isInterface() {
/*  73 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isAbstract() {
/*  77 */     return false;
/*     */   }
/*     */   
/*     */   public JType elementType() {
/*  81 */     return this.componentType;
/*     */   }
/*     */   
/*     */   public boolean isArray() {
/*  85 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  94 */     if (!(obj instanceof JArrayClass)) return false;
/*     */     
/*  96 */     if (this.componentType.equals(((JArrayClass)obj).componentType)) {
/*  97 */       return true;
/*     */     }
/*  99 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 103 */     return this.componentType.hashCode();
/*     */   }
/*     */   
/*     */   protected JClass substituteParams(JTypeVar[] variables, List<JClass> bindings) {
/* 107 */     if (this.componentType.isPrimitive()) {
/* 108 */       return this;
/*     */     }
/* 110 */     JClass c = ((JClass)this.componentType).substituteParams(variables, bindings);
/* 111 */     if (c == this.componentType) {
/* 112 */       return this;
/*     */     }
/* 114 */     return new JArrayClass(owner(), c);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JArrayClass.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */