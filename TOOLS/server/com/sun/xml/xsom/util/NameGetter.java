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
/*     */ import java.util.Locale;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NameGetter
/*     */   implements XSFunction<String>
/*     */ {
/*     */   private final Locale locale;
/*     */   
/*     */   public NameGetter(Locale _locale) {
/*  60 */     this.locale = _locale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  69 */   public static final XSFunction theInstance = new NameGetter(null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String get(XSComponent comp) {
/*  76 */     return (String)comp.apply(theInstance);
/*     */   }
/*     */ 
/*     */   
/*     */   public String annotation(XSAnnotation ann) {
/*  81 */     return localize("annotation");
/*     */   }
/*     */   
/*     */   public String attGroupDecl(XSAttGroupDecl decl) {
/*  85 */     return localize("attGroupDecl");
/*     */   }
/*     */   
/*     */   public String attributeUse(XSAttributeUse use) {
/*  89 */     return localize("attributeUse");
/*     */   }
/*     */   
/*     */   public String attributeDecl(XSAttributeDecl decl) {
/*  93 */     return localize("attributeDecl");
/*     */   }
/*     */   
/*     */   public String complexType(XSComplexType type) {
/*  97 */     return localize("complexType");
/*     */   }
/*     */   
/*     */   public String schema(XSSchema schema) {
/* 101 */     return localize("schema");
/*     */   }
/*     */   
/*     */   public String facet(XSFacet facet) {
/* 105 */     return localize("facet");
/*     */   }
/*     */   
/*     */   public String simpleType(XSSimpleType simpleType) {
/* 109 */     return localize("simpleType");
/*     */   }
/*     */   
/*     */   public String particle(XSParticle particle) {
/* 113 */     return localize("particle");
/*     */   }
/*     */   
/*     */   public String empty(XSContentType empty) {
/* 117 */     return localize("empty");
/*     */   }
/*     */   
/*     */   public String wildcard(XSWildcard wc) {
/* 121 */     return localize("wildcard");
/*     */   }
/*     */   
/*     */   public String modelGroupDecl(XSModelGroupDecl decl) {
/* 125 */     return localize("modelGroupDecl");
/*     */   }
/*     */   
/*     */   public String modelGroup(XSModelGroup group) {
/* 129 */     return localize("modelGroup");
/*     */   }
/*     */   
/*     */   public String elementDecl(XSElementDecl decl) {
/* 133 */     return localize("elementDecl");
/*     */   }
/*     */   
/*     */   public String notation(XSNotation n) {
/* 137 */     return localize("notation");
/*     */   }
/*     */   
/*     */   public String identityConstraint(XSIdentityConstraint decl) {
/* 141 */     return localize("idConstraint");
/*     */   }
/*     */   
/*     */   public String xpath(XSXPath xpath) {
/* 145 */     return localize("xpath");
/*     */   }
/*     */ 
/*     */   
/*     */   private String localize(String key) {
/*     */     ResourceBundle rb;
/* 151 */     if (this.locale == null) {
/* 152 */       rb = ResourceBundle.getBundle(NameGetter.class.getName());
/*     */     } else {
/* 154 */       rb = ResourceBundle.getBundle(NameGetter.class.getName(), this.locale);
/*     */     } 
/* 156 */     return rb.getString(key);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\xml\xso\\util\NameGetter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */