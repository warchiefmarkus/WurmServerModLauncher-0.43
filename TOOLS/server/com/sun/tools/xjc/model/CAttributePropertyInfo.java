/*     */ package com.sun.tools.xjc.model;
/*     */ 
/*     */ import com.sun.istack.Nullable;
/*     */ import com.sun.tools.xjc.model.nav.NClass;
/*     */ import com.sun.tools.xjc.model.nav.NType;
/*     */ import com.sun.xml.bind.v2.model.core.AttributePropertyInfo;
/*     */ import com.sun.xml.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.Locator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CAttributePropertyInfo
/*     */   extends CSingleTypePropertyInfo
/*     */   implements AttributePropertyInfo<NType, NClass>
/*     */ {
/*     */   private final QName attName;
/*     */   private final boolean isRequired;
/*     */   
/*     */   public CAttributePropertyInfo(String name, XSComponent source, CCustomizations customizations, Locator locator, QName attName, TypeUse type, @Nullable QName typeName, boolean required) {
/*  69 */     super(name, type, typeName, source, customizations, locator);
/*  70 */     this.isRequired = required;
/*  71 */     this.attName = attName;
/*     */   }
/*     */   
/*     */   public boolean isRequired() {
/*  75 */     return this.isRequired;
/*     */   }
/*     */   
/*     */   public QName getXmlName() {
/*  79 */     return this.attName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isUnboxable() {
/*  87 */     if (!this.isRequired) return false; 
/*  88 */     return super.isUnboxable();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOptionalPrimitive() {
/*  93 */     return (!this.isRequired && super.isUnboxable());
/*     */   }
/*     */   
/*     */   public <V> V accept(CPropertyVisitor<V> visitor) {
/*  97 */     return visitor.onAttribute(this);
/*     */   }
/*     */   
/*     */   public final PropertyKind kind() {
/* 101 */     return PropertyKind.ATTRIBUTE;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\model\CAttributePropertyInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */