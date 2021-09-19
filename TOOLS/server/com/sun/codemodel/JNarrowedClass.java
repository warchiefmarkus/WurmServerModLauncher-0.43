/*     */ package com.sun.codemodel;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class JNarrowedClass
/*     */   extends JClass
/*     */ {
/*     */   final JClass basis;
/*     */   private final List<JClass> args;
/*     */   
/*     */   JNarrowedClass(JClass basis, JClass arg) {
/*  46 */     this(basis, Collections.singletonList(arg));
/*     */   }
/*     */   
/*     */   JNarrowedClass(JClass basis, List<JClass> args) {
/*  50 */     super(basis.owner());
/*  51 */     this.basis = basis;
/*  52 */     assert !(basis instanceof JNarrowedClass);
/*  53 */     this.args = args;
/*     */   }
/*     */   
/*     */   public JClass narrow(JClass clazz) {
/*  57 */     List<JClass> newArgs = new ArrayList<JClass>(this.args);
/*  58 */     newArgs.add(clazz);
/*  59 */     return new JNarrowedClass(this.basis, newArgs);
/*     */   }
/*     */   
/*     */   public JClass narrow(JClass... clazz) {
/*  63 */     List<JClass> newArgs = new ArrayList<JClass>(this.args);
/*  64 */     for (JClass c : clazz)
/*  65 */       newArgs.add(c); 
/*  66 */     return new JNarrowedClass(this.basis, newArgs);
/*     */   }
/*     */   
/*     */   public String name() {
/*  70 */     StringBuffer buf = new StringBuffer();
/*  71 */     buf.append(this.basis.name());
/*  72 */     buf.append('<');
/*  73 */     boolean first = true;
/*  74 */     for (JClass c : this.args) {
/*  75 */       if (first) {
/*  76 */         first = false;
/*     */       } else {
/*  78 */         buf.append(',');
/*  79 */       }  buf.append(c.name());
/*     */     } 
/*  81 */     buf.append('>');
/*  82 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public String fullName() {
/*  86 */     StringBuilder buf = new StringBuilder();
/*  87 */     buf.append(this.basis.fullName());
/*  88 */     buf.append('<');
/*  89 */     boolean first = true;
/*  90 */     for (JClass c : this.args) {
/*  91 */       if (first) {
/*  92 */         first = false;
/*     */       } else {
/*  94 */         buf.append(',');
/*  95 */       }  buf.append(c.fullName());
/*     */     } 
/*  97 */     buf.append('>');
/*  98 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public String binaryName() {
/* 102 */     StringBuilder buf = new StringBuilder();
/* 103 */     buf.append(this.basis.binaryName());
/* 104 */     buf.append('<');
/* 105 */     boolean first = true;
/* 106 */     for (JClass c : this.args) {
/* 107 */       if (first) {
/* 108 */         first = false;
/*     */       } else {
/* 110 */         buf.append(',');
/* 111 */       }  buf.append(c.binaryName());
/*     */     } 
/* 113 */     buf.append('>');
/* 114 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 118 */     f.t(this.basis).p('<').g((Collection)this.args).p('￿');
/*     */   }
/*     */ 
/*     */   
/*     */   void printLink(JFormatter f) {
/* 123 */     this.basis.printLink(f);
/* 124 */     f.p("{@code <}");
/* 125 */     boolean first = true;
/* 126 */     for (JClass c : this.args) {
/* 127 */       if (first) {
/* 128 */         first = false;
/*     */       } else {
/* 130 */         f.p(',');
/* 131 */       }  c.printLink(f);
/*     */     } 
/* 133 */     f.p("{@code >}");
/*     */   }
/*     */   
/*     */   public JPackage _package() {
/* 137 */     return this.basis._package();
/*     */   }
/*     */   
/*     */   public JClass _extends() {
/* 141 */     JClass base = this.basis._extends();
/* 142 */     if (base == null) return base; 
/* 143 */     return base.substituteParams(this.basis.typeParams(), this.args);
/*     */   }
/*     */   
/*     */   public Iterator<JClass> _implements() {
/* 147 */     return new Iterator<JClass>() {
/* 148 */         private final Iterator<JClass> core = JNarrowedClass.this.basis._implements();
/*     */         public void remove() {
/* 150 */           this.core.remove();
/*     */         }
/*     */         public JClass next() {
/* 153 */           return ((JClass)this.core.next()).substituteParams(JNarrowedClass.this.basis.typeParams(), JNarrowedClass.this.args);
/*     */         }
/*     */         public boolean hasNext() {
/* 156 */           return this.core.hasNext();
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public JClass erasure() {
/* 162 */     return this.basis;
/*     */   }
/*     */   
/*     */   public boolean isInterface() {
/* 166 */     return this.basis.isInterface();
/*     */   }
/*     */   
/*     */   public boolean isAbstract() {
/* 170 */     return this.basis.isAbstract();
/*     */   }
/*     */   
/*     */   public boolean isArray() {
/* 174 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 183 */     if (!(obj instanceof JNarrowedClass)) return false; 
/* 184 */     return fullName().equals(((JClass)obj).fullName());
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 188 */     return fullName().hashCode();
/*     */   }
/*     */   protected JClass substituteParams(JTypeVar[] variables, List<JClass> bindings) {
/*     */     int j;
/* 192 */     JClass b = this.basis.substituteParams(variables, bindings);
/* 193 */     boolean different = (b != this.basis);
/*     */     
/* 195 */     List<JClass> clazz = new ArrayList<JClass>(this.args.size());
/* 196 */     for (int i = 0; i < clazz.size(); i++) {
/* 197 */       JClass c = ((JClass)this.args.get(i)).substituteParams(variables, bindings);
/* 198 */       clazz.set(i, c);
/* 199 */       j = different | ((c != this.args.get(i)) ? 1 : 0);
/*     */     } 
/*     */     
/* 202 */     if (j != 0) {
/* 203 */       return new JNarrowedClass(b, clazz);
/*     */     }
/* 205 */     return this;
/*     */   }
/*     */   
/*     */   public List<JClass> getTypeParameters() {
/* 209 */     return this.args;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JNarrowedClass.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */