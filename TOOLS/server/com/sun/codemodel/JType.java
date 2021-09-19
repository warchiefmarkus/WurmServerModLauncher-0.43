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
/*     */ public abstract class JType
/*     */   implements JGenerable, Comparable
/*     */ {
/*     */   public static JPrimitiveType parse(JCodeModel codeModel, String typeName) {
/*  36 */     if (typeName.equals("void"))
/*  37 */       return codeModel.VOID; 
/*  38 */     if (typeName.equals("boolean"))
/*  39 */       return codeModel.BOOLEAN; 
/*  40 */     if (typeName.equals("byte"))
/*  41 */       return codeModel.BYTE; 
/*  42 */     if (typeName.equals("short"))
/*  43 */       return codeModel.SHORT; 
/*  44 */     if (typeName.equals("char"))
/*  45 */       return codeModel.CHAR; 
/*  46 */     if (typeName.equals("int"))
/*  47 */       return codeModel.INT; 
/*  48 */     if (typeName.equals("float"))
/*  49 */       return codeModel.FLOAT; 
/*  50 */     if (typeName.equals("long"))
/*  51 */       return codeModel.LONG; 
/*  52 */     if (typeName.equals("double")) {
/*  53 */       return codeModel.DOUBLE;
/*     */     }
/*  55 */     throw new IllegalArgumentException("Not a primitive type: " + typeName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract JCodeModel owner();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String fullName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String binaryName() {
/*  81 */     return fullName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String name();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract JClass array();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isArray() {
/* 105 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPrimitive() {
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract JClass boxify();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract JType unboxify();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JType erasure() {
/* 134 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isReference() {
/* 141 */     return !isPrimitive();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JType elementType() {
/* 149 */     throw new IllegalArgumentException("Not an array type");
/*     */   }
/*     */   
/*     */   public String toString() {
/* 153 */     return getClass().getName() + '(' + fullName() + ')';
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
/*     */   public int compareTo(Object o) {
/* 165 */     String rhs = ((JType)o).fullName();
/* 166 */     boolean p = fullName().startsWith("java");
/* 167 */     boolean q = rhs.startsWith("java");
/*     */     
/* 169 */     if (p && !q)
/* 170 */       return -1; 
/* 171 */     if (!p && q) {
/* 172 */       return 1;
/*     */     }
/* 174 */     return fullName().compareTo(rhs);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */