/*     */ package com.sun.tools.xjc.reader.xmlschema.ct;
/*     */ 
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.xjc.model.CValuePropertyInfo;
/*     */ import com.sun.tools.xjc.model.TypeUse;
/*     */ import com.sun.tools.xjc.reader.xmlschema.BGMBuilder;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty;
/*     */ import com.sun.xml.xsom.XSAttContainer;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSContentType;
/*     */ import com.sun.xml.xsom.XSDeclaration;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSTerm;
/*     */ import com.sun.xml.xsom.visitor.XSContentTypeVisitor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class FreshComplexTypeBuilder
/*     */   extends CTBuilder
/*     */ {
/*     */   public boolean isApplicable(XSComplexType ct) {
/*  61 */     return (ct.getBaseType() == this.schemas.getAnyType() && !ct.isMixed());
/*     */   }
/*     */ 
/*     */   
/*     */   public void build(final XSComplexType ct) {
/*  66 */     XSContentType contentType = ct.getContentType();
/*     */     
/*  68 */     contentType.visit(new XSContentTypeVisitor() {
/*     */           public void simpleType(XSSimpleType st) {
/*  70 */             FreshComplexTypeBuilder.this.builder.recordBindingMode(ct, ComplexTypeBindingMode.NORMAL);
/*     */             
/*  72 */             FreshComplexTypeBuilder.this.simpleTypeBuilder.refererStack.push(ct);
/*  73 */             TypeUse use = FreshComplexTypeBuilder.this.simpleTypeBuilder.build(st);
/*  74 */             FreshComplexTypeBuilder.this.simpleTypeBuilder.refererStack.pop();
/*     */             
/*  76 */             BIProperty prop = BIProperty.getCustomization((XSComponent)ct);
/*  77 */             CValuePropertyInfo cValuePropertyInfo = prop.createValueProperty("Value", false, (XSComponent)ct, use, BGMBuilder.getName((XSDeclaration)st));
/*  78 */             FreshComplexTypeBuilder.this.selector.getCurrentBean().addProperty((CPropertyInfo)cValuePropertyInfo);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void particle(XSParticle p) {
/*  84 */             FreshComplexTypeBuilder.this.builder.recordBindingMode(ct, FreshComplexTypeBuilder.this.bgmBuilder.getParticleBinder().checkFallback(p) ? ComplexTypeBindingMode.FALLBACK_CONTENT : ComplexTypeBindingMode.NORMAL);
/*     */ 
/*     */             
/*  87 */             FreshComplexTypeBuilder.this.bgmBuilder.getParticleBinder().build(p);
/*     */             
/*  89 */             XSTerm term = p.getTerm();
/*  90 */             if (term.isModelGroup() && term.asModelGroup().getCompositor() == XSModelGroup.ALL) {
/*  91 */               FreshComplexTypeBuilder.this.selector.getCurrentBean().setOrdered(false);
/*     */             }
/*     */           }
/*     */           
/*     */           public void empty(XSContentType e) {
/*  96 */             FreshComplexTypeBuilder.this.builder.recordBindingMode(ct, ComplexTypeBindingMode.NORMAL);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 101 */     this.green.attContainer((XSAttContainer)ct);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\ct\FreshComplexTypeBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */