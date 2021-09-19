/*     */ package com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.tools.xjc.reader.Ring;
/*     */ import com.sun.tools.xjc.reader.xmlschema.ct.ComplexTypeFieldBuilder;
/*     */ import com.sun.xml.xsom.XSAttContainer;
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
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BindGreen
/*     */   extends ColorBinder
/*     */ {
/*  61 */   private final ComplexTypeFieldBuilder ctBuilder = (ComplexTypeFieldBuilder)Ring.get(ComplexTypeFieldBuilder.class);
/*     */   
/*     */   public void attGroupDecl(XSAttGroupDecl ag) {
/*  64 */     attContainer((XSAttContainer)ag);
/*     */   }
/*     */ 
/*     */   
/*     */   public void attContainer(XSAttContainer cont) {
/*  69 */     Iterator<XSAttributeUse> itr = cont.iterateDeclaredAttributeUses();
/*  70 */     while (itr.hasNext())
/*  71 */       this.builder.ying((XSComponent)itr.next(), (XSComponent)cont); 
/*  72 */     itr = cont.iterateAttGroups();
/*  73 */     while (itr.hasNext()) {
/*  74 */       this.builder.ying((XSComponent)itr.next(), (XSComponent)cont);
/*     */     }
/*  76 */     XSWildcard w = cont.getAttributeWildcard();
/*  77 */     if (w != null)
/*  78 */       this.builder.ying((XSComponent)w, (XSComponent)cont); 
/*     */   }
/*     */   
/*     */   public void complexType(XSComplexType ct) {
/*  82 */     this.ctBuilder.build(ct);
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
/*     */   public void attributeDecl(XSAttributeDecl xsAttributeDecl) {
/*  94 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void wildcard(XSWildcard xsWildcard) {
/*  99 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void modelGroupDecl(XSModelGroupDecl xsModelGroupDecl) {
/* 104 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void modelGroup(XSModelGroup xsModelGroup) {
/* 109 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void elementDecl(XSElementDecl xsElementDecl) {
/* 114 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void particle(XSParticle xsParticle) {
/* 119 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void empty(XSContentType xsContentType) {
/* 124 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void simpleType(XSSimpleType xsSimpleType) {
/* 135 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void attributeUse(XSAttributeUse use) {
/* 140 */     throw new IllegalStateException();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\BindGreen.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */