/*     */ package com.sun.xml.bind.api;
/*     */ 
/*     */ import com.sun.xml.bind.v2.model.nav.Navigator;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collection;
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
/*     */ public final class TypeReference
/*     */ {
/*     */   public final QName tagName;
/*     */   public final Type type;
/*     */   public final Annotation[] annotations;
/*     */   
/*     */   public TypeReference(QName tagName, Type type, Annotation... annotations) {
/*  80 */     if (tagName == null || type == null || annotations == null) {
/*  81 */       throw new IllegalArgumentException();
/*     */     }
/*  83 */     this.tagName = new QName(tagName.getNamespaceURI().intern(), tagName.getLocalPart().intern(), tagName.getPrefix());
/*  84 */     this.type = type;
/*  85 */     this.annotations = annotations;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <A extends Annotation> A get(Class<A> annotationType) {
/*  93 */     for (Annotation a : this.annotations) {
/*  94 */       if (a.annotationType() == annotationType)
/*  95 */         return annotationType.cast(a); 
/*     */     } 
/*  97 */     return null;
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
/*     */   public TypeReference toItemType() {
/* 109 */     Type base = Navigator.REFLECTION.getBaseClass(this.type, Collection.class);
/* 110 */     if (base == null) {
/* 111 */       return this;
/*     */     }
/* 113 */     return new TypeReference(this.tagName, Navigator.REFLECTION.getTypeArgument(base, 0), new Annotation[0]);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\bind\api\TypeReference.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */