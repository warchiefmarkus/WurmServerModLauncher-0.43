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
/*     */   implements XSFunction
/*     */ {
/*     */   public final boolean find(XSComponent c) {
/*  48 */     return ((Boolean)c.apply(this)).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object annotation(XSAnnotation ann) {
/*  55 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object attGroupDecl(XSAttGroupDecl decl) {
/*  62 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object attributeDecl(XSAttributeDecl decl) {
/*  69 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object attributeUse(XSAttributeUse use) {
/*  76 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object complexType(XSComplexType type) {
/*  83 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object schema(XSSchema schema) {
/*  90 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object facet(XSFacet facet) {
/*  97 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object notation(XSNotation notation) {
/* 104 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object simpleType(XSSimpleType simpleType) {
/* 111 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object particle(XSParticle particle) {
/* 118 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object empty(XSContentType empty) {
/* 125 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object wildcard(XSWildcard wc) {
/* 132 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object modelGroupDecl(XSModelGroupDecl decl) {
/* 139 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object modelGroup(XSModelGroup group) {
/* 146 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object elementDecl(XSElementDecl decl) {
/* 153 */     return Boolean.FALSE;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\1.0\com\sun\xml\xso\\util\XSFinder.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */