/*     */ package com.sun.xml.xsom.util;
/*     */ 
/*     */ import com.sun.xml.xsom.XSAnnotation;
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSAttributeUse;
/*     */ import com.sun.xml.xsom.XSComplexType;
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
/*     */ public class ComponentNameFunction
/*     */   implements XSFunction<String>
/*     */ {
/*  31 */   private NameGetter nameGetter = new NameGetter(null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String annotation(XSAnnotation ann) {
/*  38 */     return this.nameGetter.annotation(ann);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String attGroupDecl(XSAttGroupDecl decl) {
/*  45 */     String name = decl.getName();
/*  46 */     if (name == null) name = ""; 
/*  47 */     return name + " " + this.nameGetter.attGroupDecl(decl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String attributeDecl(XSAttributeDecl decl) {
/*  54 */     String name = decl.getName();
/*  55 */     if (name == null) name = ""; 
/*  56 */     return name + " " + this.nameGetter.attributeDecl(decl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String attributeUse(XSAttributeUse use) {
/*  64 */     return this.nameGetter.attributeUse(use);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String complexType(XSComplexType type) {
/*  71 */     String name = type.getName();
/*  72 */     if (name == null) name = "anonymous"; 
/*  73 */     return name + " " + this.nameGetter.complexType(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String schema(XSSchema schema) {
/*  80 */     return this.nameGetter.schema(schema) + " \"" + schema.getTargetNamespace() + "\"";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String facet(XSFacet facet) {
/*  87 */     String name = facet.getName();
/*  88 */     if (name == null) name = ""; 
/*  89 */     return name + " " + this.nameGetter.facet(facet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String notation(XSNotation notation) {
/*  96 */     String name = notation.getName();
/*  97 */     if (name == null) name = ""; 
/*  98 */     return name + " " + this.nameGetter.notation(notation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String simpleType(XSSimpleType simpleType) {
/* 105 */     String name = simpleType.getName();
/* 106 */     if (name == null) name = "anonymous"; 
/* 107 */     return name + " " + this.nameGetter.simpleType(simpleType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String particle(XSParticle particle) {
/* 115 */     return this.nameGetter.particle(particle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String empty(XSContentType empty) {
/* 123 */     return this.nameGetter.empty(empty);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String wildcard(XSWildcard wc) {
/* 131 */     return this.nameGetter.wildcard(wc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String modelGroupDecl(XSModelGroupDecl decl) {
/* 138 */     String name = decl.getName();
/* 139 */     if (name == null) name = ""; 
/* 140 */     return name + " " + this.nameGetter.modelGroupDecl(decl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String modelGroup(XSModelGroup group) {
/* 148 */     return this.nameGetter.modelGroup(group);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String elementDecl(XSElementDecl decl) {
/* 155 */     String name = decl.getName();
/* 156 */     if (name == null) name = ""; 
/* 157 */     return name + " " + this.nameGetter.elementDecl(decl);
/*     */   }
/*     */   
/*     */   public String identityConstraint(XSIdentityConstraint decl) {
/* 161 */     return decl.getName() + " " + this.nameGetter.identityConstraint(decl);
/*     */   }
/*     */   
/*     */   public String xpath(XSXPath xpath) {
/* 165 */     return this.nameGetter.xpath(xpath);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xso\\util\ComponentNameFunction.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */