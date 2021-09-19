/*     */ package com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.tools.xjc.model.CAttributePropertyInfo;
/*     */ import com.sun.tools.xjc.model.CClass;
/*     */ import com.sun.tools.xjc.model.CDefaultValue;
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.xjc.model.TypeUse;
/*     */ import com.sun.tools.xjc.reader.Ring;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty;
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSAttributeUse;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSContentType;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BindPurple
/*     */   extends ColorBinder
/*     */ {
/*     */   public void attGroupDecl(XSAttGroupDecl xsAttGroupDecl) {
/*  64 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void attributeDecl(XSAttributeDecl xsAttributeDecl) {
/*  69 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void attributeUse(XSAttributeUse use) {
/*  76 */     boolean hasFixedValue = (use.getFixedValue() != null);
/*  77 */     BIProperty pc = BIProperty.getCustomization((XSComponent)use);
/*     */ 
/*     */     
/*  80 */     boolean toConstant = (pc.isConstantProperty() && hasFixedValue);
/*  81 */     TypeUse attType = bindAttDecl(use.getDecl());
/*     */     
/*  83 */     CAttributePropertyInfo cAttributePropertyInfo = pc.createAttributeProperty(use, attType);
/*     */     
/*  85 */     if (toConstant) {
/*  86 */       ((CPropertyInfo)cAttributePropertyInfo).defaultValue = CDefaultValue.create(attType, use.getFixedValue());
/*  87 */       ((CPropertyInfo)cAttributePropertyInfo).realization = this.builder.fieldRendererFactory.getConst(((CPropertyInfo)cAttributePropertyInfo).realization);
/*     */     }
/*  89 */     else if (!attType.isCollection()) {
/*     */ 
/*     */       
/*  92 */       if (use.getDefaultValue() != null) {
/*     */ 
/*     */ 
/*     */         
/*  96 */         ((CPropertyInfo)cAttributePropertyInfo).defaultValue = CDefaultValue.create(attType, use.getDefaultValue());
/*     */       }
/*  98 */       else if (use.getFixedValue() != null) {
/*  99 */         ((CPropertyInfo)cAttributePropertyInfo).defaultValue = CDefaultValue.create(attType, use.getFixedValue());
/*     */       } 
/*     */     } 
/*     */     
/* 103 */     getCurrentBean().addProperty((CPropertyInfo)cAttributePropertyInfo);
/*     */   }
/*     */   
/*     */   private TypeUse bindAttDecl(XSAttributeDecl decl) {
/* 107 */     SimpleTypeBuilder stb = (SimpleTypeBuilder)Ring.get(SimpleTypeBuilder.class);
/* 108 */     stb.refererStack.push(decl);
/*     */     try {
/* 110 */       return stb.build(decl.getType());
/*     */     } finally {
/* 112 */       stb.refererStack.pop();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void complexType(XSComplexType ct) {
/* 118 */     CClass ctBean = this.selector.bindToType(ct, null, false);
/* 119 */     if (getCurrentBean() != ctBean)
/*     */     {
/*     */       
/* 122 */       getCurrentBean().setBaseClass(ctBean);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void wildcard(XSWildcard xsWildcard) {
/* 129 */     getCurrentBean().hasAttributeWildcard(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void modelGroupDecl(XSModelGroupDecl xsModelGroupDecl) {
/* 134 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void modelGroup(XSModelGroup xsModelGroup) {
/* 139 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void elementDecl(XSElementDecl xsElementDecl) {
/* 144 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void simpleType(XSSimpleType type) {
/* 148 */     createSimpleTypeProperty(type, "Value");
/*     */   }
/*     */ 
/*     */   
/*     */   public void particle(XSParticle xsParticle) {
/* 153 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void empty(XSContentType ct) {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\BindPurple.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */