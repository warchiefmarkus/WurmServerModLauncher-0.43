/*     */ package com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.tools.xjc.reader.Ring;
/*     */ import com.sun.tools.xjc.reader.xmlschema.ct.ComplexTypeFieldBuilder;
/*     */ import com.sun.xml.bind.v2.TODO;
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
/*     */ 
/*     */ 
/*     */ public final class BindRed
/*     */   extends ColorBinder
/*     */ {
/*  61 */   private final ComplexTypeFieldBuilder ctBuilder = (ComplexTypeFieldBuilder)Ring.get(ComplexTypeFieldBuilder.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void complexType(XSComplexType ct) {
/*  73 */     this.ctBuilder.build(ct);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void wildcard(XSWildcard xsWildcard) {
/*  82 */     TODO.checkSpec();
/*  83 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void elementDecl(XSElementDecl e) {
/*  87 */     SimpleTypeBuilder stb = (SimpleTypeBuilder)Ring.get(SimpleTypeBuilder.class);
/*  88 */     stb.refererStack.push(e);
/*  89 */     this.builder.ying((XSComponent)e.getType(), (XSComponent)e);
/*  90 */     stb.refererStack.pop();
/*     */   }
/*     */   
/*     */   public void simpleType(XSSimpleType type) {
/*  94 */     SimpleTypeBuilder stb = (SimpleTypeBuilder)Ring.get(SimpleTypeBuilder.class);
/*  95 */     stb.refererStack.push(type);
/*  96 */     createSimpleTypeProperty(type, "Value");
/*  97 */     stb.refererStack.pop();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void attGroupDecl(XSAttGroupDecl ag) {
/* 106 */     throw new IllegalStateException();
/*     */   }
/*     */   public void attributeDecl(XSAttributeDecl ad) {
/* 109 */     throw new IllegalStateException();
/*     */   }
/*     */   public void attributeUse(XSAttributeUse au) {
/* 112 */     throw new IllegalStateException();
/*     */   }
/*     */   public void empty(XSContentType xsContentType) {
/* 115 */     throw new IllegalStateException();
/*     */   }
/*     */   public void modelGroupDecl(XSModelGroupDecl xsModelGroupDecl) {
/* 118 */     throw new IllegalStateException();
/*     */   }
/*     */   public void modelGroup(XSModelGroup xsModelGroup) {
/* 121 */     throw new IllegalStateException();
/*     */   }
/*     */   public void particle(XSParticle p) {
/* 124 */     throw new IllegalStateException();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\BindRed.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */