/*     */ package 1.0.com.sun.xml.xsom.util;
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
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSNotation;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSSchema;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSWildcard;
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
/*     */ public class NameGetter
/*     */   implements XSFunction
/*     */ {
/*     */   private final Locale locale;
/*     */   
/*     */   public NameGetter(Locale _locale) {
/*  48 */     this.locale = _locale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   public static final XSFunction theInstance = new com.sun.xml.xsom.util.NameGetter(null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String get(XSComponent comp) {
/*  64 */     return (String)comp.apply(theInstance);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object annotation(XSAnnotation ann) {
/*  69 */     return localize("annotation");
/*     */   }
/*     */   
/*     */   public Object attGroupDecl(XSAttGroupDecl decl) {
/*  73 */     return localize("attGroupDecl");
/*     */   }
/*     */   
/*     */   public Object attributeUse(XSAttributeUse use) {
/*  77 */     return localize("attributeUse");
/*     */   }
/*     */   
/*     */   public Object attributeDecl(XSAttributeDecl decl) {
/*  81 */     return localize("attributeDecl");
/*     */   }
/*     */   
/*     */   public Object complexType(XSComplexType type) {
/*  85 */     return localize("complexType");
/*     */   }
/*     */   
/*     */   public Object schema(XSSchema schema) {
/*  89 */     return localize("schema");
/*     */   }
/*     */   
/*     */   public Object facet(XSFacet facet) {
/*  93 */     return localize("facet");
/*     */   }
/*     */   
/*     */   public Object simpleType(XSSimpleType simpleType) {
/*  97 */     return localize("simpleType");
/*     */   }
/*     */   
/*     */   public Object particle(XSParticle particle) {
/* 101 */     return localize("particle");
/*     */   }
/*     */   
/*     */   public Object empty(XSContentType empty) {
/* 105 */     return localize("empty");
/*     */   }
/*     */   
/*     */   public Object wildcard(XSWildcard wc) {
/* 109 */     return localize("wildcard");
/*     */   }
/*     */   
/*     */   public Object modelGroupDecl(XSModelGroupDecl decl) {
/* 113 */     return localize("modelGroupDecl");
/*     */   }
/*     */   
/*     */   public Object modelGroup(XSModelGroup group) {
/* 117 */     return localize("modelGroup");
/*     */   }
/*     */   
/*     */   public Object elementDecl(XSElementDecl decl) {
/* 121 */     return localize("elementDecl");
/*     */   }
/*     */   
/*     */   public Object notation(XSNotation n) {
/* 125 */     return localize("notation");
/*     */   }
/*     */ 
/*     */   
/*     */   private String localize(String key) {
/*     */     ResourceBundle rb;
/* 131 */     if (this.locale == null) {
/* 132 */       rb = ResourceBundle.getBundle(com.sun.xml.xsom.util.NameGetter.class.getName());
/*     */     } else {
/* 134 */       rb = ResourceBundle.getBundle(com.sun.xml.xsom.util.NameGetter.class.getName(), this.locale);
/*     */     } 
/* 136 */     return rb.getString(key);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xso\\util\NameGetter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */