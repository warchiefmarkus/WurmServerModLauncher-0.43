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
/*     */ public class XSFunctionFilter
/*     */   implements XSFunction
/*     */ {
/*     */   protected XSFunction core;
/*     */   
/*     */   public XSFunctionFilter(XSFunction _core) {
/*  46 */     this.core = _core;
/*     */   }
/*     */   
/*     */   public XSFunctionFilter() {}
/*     */   
/*     */   public Object annotation(XSAnnotation ann) {
/*  52 */     return this.core.annotation(ann);
/*     */   }
/*     */   
/*     */   public Object attGroupDecl(XSAttGroupDecl decl) {
/*  56 */     return this.core.attGroupDecl(decl);
/*     */   }
/*     */   
/*     */   public Object attributeDecl(XSAttributeDecl decl) {
/*  60 */     return this.core.attributeDecl(decl);
/*     */   }
/*     */   
/*     */   public Object attributeUse(XSAttributeUse use) {
/*  64 */     return this.core.attributeUse(use);
/*     */   }
/*     */   
/*     */   public Object complexType(XSComplexType type) {
/*  68 */     return this.core.complexType(type);
/*     */   }
/*     */   
/*     */   public Object schema(XSSchema schema) {
/*  72 */     return this.core.schema(schema);
/*     */   }
/*     */   
/*     */   public Object facet(XSFacet facet) {
/*  76 */     return this.core.facet(facet);
/*     */   }
/*     */   
/*     */   public Object notation(XSNotation notation) {
/*  80 */     return this.core.notation(notation);
/*     */   }
/*     */   
/*     */   public Object simpleType(XSSimpleType simpleType) {
/*  84 */     return this.core.simpleType(simpleType);
/*     */   }
/*     */   
/*     */   public Object particle(XSParticle particle) {
/*  88 */     return this.core.particle(particle);
/*     */   }
/*     */   
/*     */   public Object empty(XSContentType empty) {
/*  92 */     return this.core.empty(empty);
/*     */   }
/*     */   
/*     */   public Object wildcard(XSWildcard wc) {
/*  96 */     return this.core.wildcard(wc);
/*     */   }
/*     */   
/*     */   public Object modelGroupDecl(XSModelGroupDecl decl) {
/* 100 */     return this.core.modelGroupDecl(decl);
/*     */   }
/*     */   
/*     */   public Object modelGroup(XSModelGroup group) {
/* 104 */     return this.core.modelGroup(group);
/*     */   }
/*     */   
/*     */   public Object elementDecl(XSElementDecl decl) {
/* 108 */     return this.core.elementDecl(decl);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xso\\util\XSFunctionFilter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */