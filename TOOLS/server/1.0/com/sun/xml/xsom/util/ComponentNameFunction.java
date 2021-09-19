/*     */ package 1.0.com.sun.xml.xsom.util;
/*     */ 
/*     */ import com.sun.xml.xsom.XSAnnotation;
/*     */ import com.sun.xml.xsom.XSAttGroupDecl;
/*     */ import com.sun.xml.xsom.XSAttributeDecl;
/*     */ import com.sun.xml.xsom.XSAttributeUse;
/*     */ import com.sun.xml.xsom.XSComplexType;
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
/*     */ import com.sun.xml.xsom.util.NameGetter;
/*     */ import com.sun.xml.xsom.visitor.XSFunction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ComponentNameFunction
/*     */   implements XSFunction
/*     */ {
/*  29 */   private NameGetter nameGetter = new NameGetter(null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object annotation(XSAnnotation ann) {
/*  36 */     return this.nameGetter.annotation(ann);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object attGroupDecl(XSAttGroupDecl decl) {
/*  43 */     String name = decl.getName();
/*  44 */     if (name == null) name = ""; 
/*  45 */     return name + " " + this.nameGetter.attGroupDecl(decl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object attributeDecl(XSAttributeDecl decl) {
/*  52 */     String name = decl.getName();
/*  53 */     if (name == null) name = ""; 
/*  54 */     return name + " " + this.nameGetter.attributeDecl(decl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object attributeUse(XSAttributeUse use) {
/*  62 */     return this.nameGetter.attributeUse(use);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object complexType(XSComplexType type) {
/*  69 */     String name = type.getName();
/*  70 */     if (name == null) name = "anonymous"; 
/*  71 */     return name + " " + this.nameGetter.complexType(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object schema(XSSchema schema) {
/*  78 */     return this.nameGetter.schema(schema) + " \"" + schema.getTargetNamespace() + "\"";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object facet(XSFacet facet) {
/*  85 */     String name = facet.getName();
/*  86 */     if (name == null) name = ""; 
/*  87 */     return name + " " + this.nameGetter.facet(facet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object notation(XSNotation notation) {
/*  94 */     String name = notation.getName();
/*  95 */     if (name == null) name = ""; 
/*  96 */     return name + " " + this.nameGetter.notation(notation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object simpleType(XSSimpleType simpleType) {
/* 103 */     String name = simpleType.getName();
/* 104 */     if (name == null) name = "anonymous"; 
/* 105 */     return name + " " + this.nameGetter.simpleType(simpleType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object particle(XSParticle particle) {
/* 113 */     return this.nameGetter.particle(particle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object empty(XSContentType empty) {
/* 121 */     return this.nameGetter.empty(empty);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object wildcard(XSWildcard wc) {
/* 129 */     return this.nameGetter.wildcard(wc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object modelGroupDecl(XSModelGroupDecl decl) {
/* 136 */     String name = decl.getName();
/* 137 */     if (name == null) name = ""; 
/* 138 */     return name + " " + this.nameGetter.modelGroupDecl(decl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object modelGroup(XSModelGroup group) {
/* 146 */     return this.nameGetter.modelGroup(group);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object elementDecl(XSElementDecl decl) {
/* 153 */     String name = decl.getName();
/* 154 */     if (name == null) name = ""; 
/* 155 */     return name + " " + this.nameGetter.elementDecl(decl);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xso\\util\ComponentNameFunction.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */