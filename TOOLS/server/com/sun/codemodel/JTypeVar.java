/*     */ package com.sun.codemodel;
/*     */ 
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
/*     */ public final class JTypeVar
/*     */   extends JClass
/*     */   implements JDeclaration
/*     */ {
/*     */   private final String name;
/*     */   private JClass bound;
/*     */   
/*     */   JTypeVar(JCodeModel owner, String _name) {
/*  39 */     super(owner);
/*  40 */     this.name = _name;
/*     */   }
/*     */   
/*     */   public String name() {
/*  44 */     return this.name;
/*     */   }
/*     */   
/*     */   public String fullName() {
/*  48 */     return this.name;
/*     */   }
/*     */   
/*     */   public JPackage _package() {
/*  52 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JTypeVar bound(JClass c) {
/*  61 */     if (this.bound != null)
/*  62 */       throw new IllegalArgumentException("type variable has an existing class bound " + this.bound); 
/*  63 */     this.bound = c;
/*  64 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass _extends() {
/*  74 */     if (this.bound != null) {
/*  75 */       return this.bound;
/*     */     }
/*  77 */     return owner().ref(Object.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<JClass> _implements() {
/*  84 */     return this.bound._implements();
/*     */   }
/*     */   
/*     */   public boolean isInterface() {
/*  88 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isAbstract() {
/*  92 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void declare(JFormatter f) {
/*  99 */     f.id(this.name);
/* 100 */     if (this.bound != null) {
/* 101 */       f.p("extends").g(this.bound);
/*     */     }
/*     */   }
/*     */   
/*     */   protected JClass substituteParams(JTypeVar[] variables, List<JClass> bindings) {
/* 106 */     for (int i = 0; i < variables.length; i++) {
/* 107 */       if (variables[i] == this)
/* 108 */         return bindings.get(i); 
/* 109 */     }  return this;
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 113 */     f.id(this.name);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JTypeVar.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */