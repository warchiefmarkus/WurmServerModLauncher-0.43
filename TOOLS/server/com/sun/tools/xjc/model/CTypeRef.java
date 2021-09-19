/*     */ package com.sun.tools.xjc.model;
/*     */ 
/*     */ import com.sun.istack.Nullable;
/*     */ import com.sun.tools.xjc.model.nav.NClass;
/*     */ import com.sun.tools.xjc.model.nav.NType;
/*     */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.xml.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.core.TypeRef;
/*     */ import com.sun.xml.bind.v2.runtime.RuntimeUtil;
/*     */ import com.sun.xml.xsom.XSDeclaration;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XmlString;
/*     */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
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
/*     */ public final class CTypeRef
/*     */   implements TypeRef<NType, NClass>
/*     */ {
/*     */   @XmlJavaTypeAdapter(RuntimeUtil.ToStringAdapter.class)
/*     */   private final CNonElement type;
/*     */   private final QName elementName;
/*     */   @Nullable
/*     */   final QName typeName;
/*     */   private final boolean nillable;
/*     */   public final XmlString defaultValue;
/*     */   
/*     */   public CTypeRef(CNonElement type, XSElementDecl decl) {
/*  79 */     this(type, BGMBuilder.getName((XSDeclaration)decl), getSimpleTypeName(decl), decl.isNillable(), decl.getDefaultValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public static QName getSimpleTypeName(XSElementDecl decl) {
/*  84 */     if (decl == null) return null; 
/*  85 */     QName typeName = null;
/*  86 */     if (decl.getType().isSimpleType())
/*  87 */       typeName = BGMBuilder.getName((XSDeclaration)decl.getType()); 
/*  88 */     return typeName;
/*     */   }
/*     */   
/*     */   public CTypeRef(CNonElement type, QName elementName, QName typeName, boolean nillable, XmlString defaultValue) {
/*  92 */     assert type != null;
/*  93 */     assert elementName != null;
/*     */     
/*  95 */     this.type = type;
/*  96 */     this.elementName = elementName;
/*  97 */     this.typeName = typeName;
/*  98 */     this.nillable = nillable;
/*  99 */     this.defaultValue = defaultValue;
/*     */   }
/*     */   
/*     */   public CNonElement getTarget() {
/* 103 */     return this.type;
/*     */   }
/*     */   
/*     */   public QName getTagName() {
/* 107 */     return this.elementName;
/*     */   }
/*     */   
/*     */   public boolean isNillable() {
/* 111 */     return this.nillable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefaultValue() {
/* 121 */     if (this.defaultValue != null) {
/* 122 */       return this.defaultValue.value;
/*     */     }
/* 124 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLeaf() {
/* 129 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public PropertyInfo<NType, NClass> getSource() {
/* 134 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\CTypeRef.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */