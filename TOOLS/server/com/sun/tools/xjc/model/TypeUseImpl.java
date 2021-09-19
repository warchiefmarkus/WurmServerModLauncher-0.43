/*     */ package com.sun.tools.xjc.model;
/*     */ 
/*     */ import com.sun.codemodel.JExpr;
/*     */ import com.sun.codemodel.JExpression;
/*     */ import com.sun.codemodel.JStringLiteral;
/*     */ import com.sun.tools.xjc.outline.Outline;
/*     */ import com.sun.xml.bind.v2.ClassFactory;
/*     */ import com.sun.xml.bind.v2.model.core.ID;
/*     */ import com.sun.xml.xsom.XmlString;
/*     */ import javax.activation.MimeType;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class TypeUseImpl
/*     */   implements TypeUse
/*     */ {
/*     */   private final CNonElement coreType;
/*     */   private final boolean collection;
/*     */   private final CAdapter adapter;
/*     */   private final ID id;
/*     */   private final MimeType expectedMimeType;
/*     */   
/*     */   public TypeUseImpl(CNonElement itemType, boolean collection, ID id, MimeType expectedMimeType, CAdapter adapter) {
/*  65 */     this.coreType = itemType;
/*  66 */     this.collection = collection;
/*  67 */     this.id = id;
/*  68 */     this.expectedMimeType = expectedMimeType;
/*  69 */     this.adapter = adapter;
/*     */   }
/*     */   
/*     */   public boolean isCollection() {
/*  73 */     return this.collection;
/*     */   }
/*     */   
/*     */   public CNonElement getInfo() {
/*  77 */     return this.coreType;
/*     */   }
/*     */   
/*     */   public CAdapter getAdapterUse() {
/*  81 */     return this.adapter;
/*     */   }
/*     */   
/*     */   public ID idUse() {
/*  85 */     return this.id;
/*     */   }
/*     */   
/*     */   public MimeType getExpectedMimeType() {
/*  89 */     return this.expectedMimeType;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o) {
/*  93 */     if (this == o) return true; 
/*  94 */     if (!(o instanceof TypeUseImpl)) return false;
/*     */     
/*  96 */     TypeUseImpl that = (TypeUseImpl)o;
/*     */     
/*  98 */     if (this.collection != that.collection) return false; 
/*  99 */     if (this.id != that.id) return false; 
/* 100 */     if ((this.adapter != null) ? !this.adapter.equals(that.adapter) : (that.adapter != null)) return false; 
/* 101 */     if ((this.coreType != null) ? !this.coreType.equals(that.coreType) : (that.coreType != null)) return false;
/*     */     
/* 103 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 108 */     int result = (this.coreType != null) ? this.coreType.hashCode() : 0;
/* 109 */     result = 29 * result + (this.collection ? 1 : 0);
/* 110 */     result = 29 * result + ((this.adapter != null) ? this.adapter.hashCode() : 0);
/* 111 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public JExpression createConstant(Outline outline, XmlString lexical) {
/* 116 */     if (isCollection()) return null;
/*     */     
/* 118 */     if (this.adapter == null) return this.coreType.createConstant(outline, lexical);
/*     */ 
/*     */     
/* 121 */     JExpression cons = this.coreType.createConstant(outline, lexical);
/* 122 */     Class<? extends XmlAdapter> atype = this.adapter.getAdapterIfKnown();
/*     */ 
/*     */     
/* 125 */     if (cons instanceof JStringLiteral && atype != null) {
/* 126 */       JStringLiteral scons = (JStringLiteral)cons;
/* 127 */       XmlAdapter a = (XmlAdapter)ClassFactory.create(atype);
/*     */       try {
/* 129 */         Object value = a.unmarshal(scons.str);
/* 130 */         if (value instanceof String) {
/* 131 */           return JExpr.lit((String)value);
/*     */         }
/* 133 */       } catch (Exception e) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 138 */     return (JExpression)JExpr._new(this.adapter.getAdapterClass(outline)).invoke("unmarshal").arg(cons);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\TypeUseImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */