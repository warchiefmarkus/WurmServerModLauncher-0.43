/*     */ package com.sun.codemodel;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
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
/*     */ public abstract class JClass
/*     */   extends JType
/*     */ {
/*     */   private final JCodeModel _owner;
/*     */   
/*     */   protected JClass(JCodeModel _owner) {
/*  40 */     this._owner = _owner;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass outer() {
/*  64 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public final JCodeModel owner() {
/*  69 */     return this._owner;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JTypeVar[] typeParams() {
/* 104 */     return EMPTY_ARRAY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 110 */   protected static final JTypeVar[] EMPTY_ARRAY = new JTypeVar[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JClass arrayClass;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JPrimitiveType getPrimitiveType() {
/* 127 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass boxify() {
/* 134 */     return this;
/*     */   }
/*     */   public JType unboxify() {
/* 137 */     JPrimitiveType pt = getPrimitiveType();
/* 138 */     return (pt == null) ? this : pt;
/*     */   }
/*     */   
/*     */   public JClass erasure() {
/* 142 */     return this;
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
/*     */   public final boolean isAssignableFrom(JClass derived) {
/* 155 */     if (derived instanceof JNullType) return true;
/*     */     
/* 157 */     if (this == derived) return true;
/*     */ 
/*     */ 
/*     */     
/* 161 */     if (this == _package().owner().ref(Object.class)) return true;
/*     */     
/* 163 */     JClass b = derived._extends();
/* 164 */     if (b != null && isAssignableFrom(b)) {
/* 165 */       return true;
/*     */     }
/* 167 */     if (isInterface()) {
/* 168 */       Iterator<JClass> itfs = derived._implements();
/* 169 */       while (itfs.hasNext()) {
/* 170 */         if (isAssignableFrom(itfs.next()))
/* 171 */           return true; 
/*     */       } 
/*     */     } 
/* 174 */     return false;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JClass getBaseClass(JClass baseType) {
/* 202 */     if (erasure().equals(baseType)) {
/* 203 */       return this;
/*     */     }
/* 205 */     JClass b = _extends();
/* 206 */     if (b != null) {
/* 207 */       JClass bc = b.getBaseClass(baseType);
/* 208 */       if (bc != null) {
/* 209 */         return bc;
/*     */       }
/*     */     } 
/* 212 */     Iterator<JClass> itfs = _implements();
/* 213 */     while (itfs.hasNext()) {
/* 214 */       JClass bc = ((JClass)itfs.next()).getBaseClass(baseType);
/* 215 */       if (bc != null) {
/* 216 */         return bc;
/*     */       }
/*     */     } 
/* 219 */     return null;
/*     */   }
/*     */   
/*     */   public final JClass getBaseClass(Class baseType) {
/* 223 */     return getBaseClass(owner().ref(baseType));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass array() {
/* 229 */     if (this.arrayClass == null)
/* 230 */       this.arrayClass = new JArrayClass(owner(), this); 
/* 231 */     return this.arrayClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass narrow(Class clazz) {
/* 242 */     return narrow(owner().ref(clazz));
/*     */   }
/*     */   
/*     */   public JClass narrow(Class... clazz) {
/* 246 */     JClass[] r = new JClass[clazz.length];
/* 247 */     for (int i = 0; i < clazz.length; i++)
/* 248 */       r[i] = owner().ref(clazz[i]); 
/* 249 */     return narrow(r);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JClass narrow(JClass clazz) {
/* 260 */     return new JNarrowedClass(this, clazz);
/*     */   }
/*     */   
/*     */   public JClass narrow(JClass... clazz) {
/* 264 */     return new JNarrowedClass(this, Arrays.asList((Object[])clazz.clone()));
/*     */   }
/*     */   
/*     */   public JClass narrow(List<? extends JClass> clazz) {
/* 268 */     return new JNarrowedClass(this, new ArrayList<JClass>(clazz));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<JClass> getTypeParameters() {
/* 275 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isParameterized() {
/* 282 */     return (erasure() != this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JClass wildcard() {
/* 291 */     return new JTypeWildcard(this);
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
/*     */   public String toString() {
/* 309 */     return getClass().getName() + '(' + name() + ')';
/*     */   }
/*     */ 
/*     */   
/*     */   public final JExpression dotclass() {
/* 314 */     return JExpr.dotclass(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public final JInvocation staticInvoke(JMethod method) {
/* 319 */     return new JInvocation(this, method);
/*     */   }
/*     */ 
/*     */   
/*     */   public final JInvocation staticInvoke(String method) {
/* 324 */     return new JInvocation(this, method);
/*     */   }
/*     */ 
/*     */   
/*     */   public final JFieldRef staticRef(String field) {
/* 329 */     return new JFieldRef(this, field);
/*     */   }
/*     */ 
/*     */   
/*     */   public final JFieldRef staticRef(JVar field) {
/* 334 */     return new JFieldRef(this, field);
/*     */   }
/*     */   
/*     */   public void generate(JFormatter f) {
/* 338 */     f.t(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void printLink(JFormatter f) {
/* 345 */     f.p("{@link ").g(this).p('}');
/*     */   }
/*     */   
/*     */   public abstract String name();
/*     */   
/*     */   public abstract JPackage _package();
/*     */   
/*     */   public abstract JClass _extends();
/*     */   
/*     */   public abstract Iterator<JClass> _implements();
/*     */   
/*     */   public abstract boolean isInterface();
/*     */   
/*     */   public abstract boolean isAbstract();
/*     */   
/*     */   protected abstract JClass substituteParams(JTypeVar[] paramArrayOfJTypeVar, List<JClass> paramList);
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\codemodel\JClass.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */