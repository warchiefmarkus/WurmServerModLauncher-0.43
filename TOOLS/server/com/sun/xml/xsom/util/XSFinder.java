/*     */ package com.sun.xml.xsom.util;
/*     */ 
/*     */ import com.sun.xml.xsom.XSAnnotation;
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSAttributeUse;
/*     */ import com.sun.xml.xsom.XSComplexType;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSContentType;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSFacet;
/*     */ import com.sun.xml.xsom.XSIdentityConstraint;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSNotation;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSSchema;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.XSXPath;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XSFinder
/*     */   implements XSFunction<Boolean>
/*     */ {
/*     */   public final boolean find(XSComponent c) {
/*  60 */     return ((Boolean)c.apply(this)).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean annotation(XSAnnotation ann) {
/*  67 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean attGroupDecl(XSAttGroupDecl decl) {
/*  74 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean attributeDecl(XSAttributeDecl decl) {
/*  81 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean attributeUse(XSAttributeUse use) {
/*  88 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean complexType(XSComplexType type) {
/*  95 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean schema(XSSchema schema) {
/* 102 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean facet(XSFacet facet) {
/* 109 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean notation(XSNotation notation) {
/* 116 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean simpleType(XSSimpleType simpleType) {
/* 123 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean particle(XSParticle particle) {
/* 130 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean empty(XSContentType empty) {
/* 137 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean wildcard(XSWildcard wc) {
/* 144 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean modelGroupDecl(XSModelGroupDecl decl) {
/* 151 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean modelGroup(XSModelGroup group) {
/* 158 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean elementDecl(XSElementDecl decl) {
/* 165 */     return Boolean.FALSE;
/*     */   }
/*     */   
/*     */   public Boolean identityConstraint(XSIdentityConstraint decl) {
/* 169 */     return Boolean.FALSE;
/*     */   }
/*     */   
/*     */   public Boolean xpath(XSXPath xpath) {
/* 173 */     return Boolean.FALSE;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xso\\util\XSFinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */