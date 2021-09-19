/*     */ package javax.xml.bind;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JAXBElement<T>
/*     */   implements Serializable
/*     */ {
/*     */   protected final QName name;
/*     */   protected final Class<T> declaredType;
/*     */   protected final Class scope;
/*     */   protected T value;
/*     */   protected boolean nil = false;
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public static final class GlobalScope {}
/*     */   
/*     */   public JAXBElement(QName name, Class<T> declaredType, Class<GlobalScope> scope, T value) {
/*  90 */     if (declaredType == null || name == null)
/*  91 */       throw new IllegalArgumentException(); 
/*  92 */     this.declaredType = declaredType;
/*  93 */     if (scope == null) scope = GlobalScope.class; 
/*  94 */     this.scope = scope;
/*  95 */     this.name = name;
/*  96 */     setValue(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JAXBElement(QName name, Class<T> declaredType, T value) {
/* 105 */     this(name, declaredType, GlobalScope.class, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<T> getDeclaredType() {
/* 112 */     return this.declaredType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public QName getName() {
/* 119 */     return this.name;
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
/*     */   public void setValue(T t) {
/* 131 */     this.value = t;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T getValue() {
/* 141 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class getScope() {
/* 151 */     return this.scope;
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
/*     */   public boolean isNil() {
/* 164 */     return (this.value == null || this.nil);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNil(boolean value) {
/* 173 */     this.nil = value;
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
/*     */   public boolean isGlobalScope() {
/* 185 */     return (this.scope == GlobalScope.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTypeSubstituted() {
/* 193 */     if (this.value == null) return false; 
/* 194 */     return (this.value.getClass() != this.declaredType);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\javax\xml\bind\JAXBElement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */