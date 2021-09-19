/*     */ package com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSAttributeUse;
/*     */ import com.sun.xml.xsom.XSComplexType;
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
/*     */ public final class BindYellow
/*     */   extends ColorBinder
/*     */ {
/*     */   public void complexType(XSComplexType ct) {}
/*     */   
/*     */   public void wildcard(XSWildcard xsWildcard) {
/*  60 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void elementDecl(XSElementDecl xsElementDecl) {
/*  65 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void simpleType(XSSimpleType xsSimpleType) {
/*  70 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void attributeDecl(XSAttributeDecl xsAttributeDecl) {
/*  75 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void attGroupDecl(XSAttGroupDecl xsAttGroupDecl) {
/*  85 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public void attributeUse(XSAttributeUse use) {
/*  89 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public void modelGroupDecl(XSModelGroupDecl xsModelGroupDecl) {
/*  93 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public void modelGroup(XSModelGroup xsModelGroup) {
/*  97 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public void particle(XSParticle xsParticle) {
/* 101 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   public void empty(XSContentType xsContentType) {
/* 105 */     throw new IllegalStateException();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\BindYellow.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */