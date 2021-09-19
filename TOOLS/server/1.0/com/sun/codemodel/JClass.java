/*     */ package 1.0.com.sun.codemodel;
/*     */ 
/*     */ import com.sun.codemodel.JArrayClass;
/*     */ import com.sun.codemodel.JCodeModel;
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JFieldRef;
/*     */ import com.sun.codemodel.JInvocation;
/*     */ import com.sun.codemodel.JMethod;
/*     */ import com.sun.codemodel.JPackage;
/*     */ import com.sun.codemodel.JPrimitiveType;
/*     */ import com.sun.codemodel.JType;
/*     */ import com.sun.codemodel.JVar;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public abstract class JClass extends JType {
/*     */   protected JClass(JCodeModel _owner) {
/*  18 */     this._owner = _owner;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final JCodeModel _owner;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String fullName() {
/*  35 */     JPackage p = _package();
/*  36 */     if (p.isUnnamed()) return name(); 
/*  37 */     return p.name() + '.' + name();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final JCodeModel owner() {
/*  45 */     return this._owner;
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
/*     */   public JPrimitiveType getPrimitiveType() {
/*  78 */     return null;
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
/*     */   public final boolean isAssignableFrom(com.sun.codemodel.JClass derived) {
/*  90 */     if (derived instanceof com.sun.codemodel.JNullType) return true;
/*     */     
/*  92 */     if (this == derived) return true;
/*     */ 
/*     */ 
/*     */     
/*  96 */     if (this == _package().owner().ref(Object.class)) return true;
/*     */     
/*  98 */     com.sun.codemodel.JClass b = derived._extends();
/*  99 */     if (b != null && isAssignableFrom(b)) {
/* 100 */       return true;
/*     */     }
/* 102 */     if (isInterface()) {
/* 103 */       Iterator itfs = derived._implements();
/* 104 */       while (itfs.hasNext()) {
/* 105 */         if (isAssignableFrom(itfs.next()))
/* 106 */           return true; 
/*     */       } 
/*     */     } 
/* 109 */     return false;
/*     */   }
/*     */   
/*     */   public com.sun.codemodel.JClass array() {
/* 113 */     return (com.sun.codemodel.JClass)new JArrayClass(owner(), this);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 117 */     return getClass().getName() + "(" + name() + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public final JExpression dotclass() {
/* 122 */     return JExpr.dotclass(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public final JInvocation staticInvoke(JMethod method) {
/* 127 */     return staticInvoke(method.name());
/*     */   }
/*     */ 
/*     */   
/*     */   public final JInvocation staticInvoke(String method) {
/* 132 */     return new JInvocation(this, method);
/*     */   }
/*     */ 
/*     */   
/*     */   public final JFieldRef staticRef(String field) {
/* 137 */     return new JFieldRef(this, field);
/*     */   }
/*     */ 
/*     */   
/*     */   public final JFieldRef staticRef(JVar field) {
/* 142 */     return staticRef(field.name());
/*     */   }
/*     */   
/*     */   public abstract String name();
/*     */   
/*     */   public abstract JPackage _package();
/*     */   
/*     */   public abstract com.sun.codemodel.JClass _extends();
/*     */   
/*     */   public abstract Iterator _implements();
/*     */   
/*     */   public abstract boolean isInterface();
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\codemodel\JClass.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */