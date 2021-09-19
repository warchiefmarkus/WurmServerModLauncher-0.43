/*     */ package com.sun.tools.xjc.reader.xmlschema;
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
/*     */ import com.sun.xml.xsom.XSSchemaSet;
/*     */ import com.sun.xml.xsom.XSSimpleType;
/*     */ import com.sun.xml.xsom.XSType;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.XSXPath;
/*     */ import com.sun.xml.xsom.visitor.XSVisitor;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ final class RefererFinder
/*     */   implements XSVisitor
/*     */ {
/*  73 */   private final Set<Object> visited = new HashSet();
/*     */   
/*  75 */   private final Map<XSComponent, Set<XSComponent>> referers = new HashMap<XSComponent, Set<XSComponent>>();
/*     */   
/*     */   public Set<XSComponent> getReferer(XSComponent src) {
/*  78 */     Set<XSComponent> r = this.referers.get(src);
/*  79 */     if (r == null) return Collections.emptySet(); 
/*  80 */     return r;
/*     */   }
/*     */ 
/*     */   
/*     */   public void schemaSet(XSSchemaSet xss) {
/*  85 */     if (!this.visited.add(xss))
/*     */       return; 
/*  87 */     for (XSSchema xs : xss.getSchemas()) {
/*  88 */       schema(xs);
/*     */     }
/*     */   }
/*     */   
/*     */   public void schema(XSSchema xs) {
/*  93 */     if (!this.visited.add(xs))
/*     */       return; 
/*  95 */     for (XSComplexType ct : xs.getComplexTypes().values()) {
/*  96 */       complexType(ct);
/*     */     }
/*     */     
/*  99 */     for (XSElementDecl e : xs.getElementDecls().values()) {
/* 100 */       elementDecl(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void elementDecl(XSElementDecl e) {
/* 105 */     if (!this.visited.add(e))
/*     */       return; 
/* 107 */     refer((XSComponent)e, e.getType());
/* 108 */     e.getType().visit(this);
/*     */   }
/*     */   
/*     */   public void complexType(XSComplexType ct) {
/* 112 */     if (!this.visited.add(ct))
/*     */       return; 
/* 114 */     refer((XSComponent)ct, ct.getBaseType());
/* 115 */     ct.getBaseType().visit(this);
/* 116 */     ct.getContentType().visit(this);
/*     */   }
/*     */   
/*     */   public void modelGroupDecl(XSModelGroupDecl decl) {
/* 120 */     if (!this.visited.add(decl))
/*     */       return; 
/* 122 */     modelGroup(decl.getModelGroup());
/*     */   }
/*     */   
/*     */   public void modelGroup(XSModelGroup group) {
/* 126 */     if (!this.visited.add(group))
/*     */       return; 
/* 128 */     for (XSParticle p : group.getChildren()) {
/* 129 */       particle(p);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void particle(XSParticle particle) {
/* 135 */     particle.getTerm().visit(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void simpleType(XSSimpleType simpleType) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void annotation(XSAnnotation ann) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void attGroupDecl(XSAttGroupDecl decl) {}
/*     */ 
/*     */   
/*     */   public void attributeDecl(XSAttributeDecl decl) {}
/*     */ 
/*     */   
/*     */   public void attributeUse(XSAttributeUse use) {}
/*     */ 
/*     */   
/*     */   private void refer(XSComponent source, XSType target) {
/* 158 */     Set<XSComponent> r = this.referers.get(target);
/* 159 */     if (r == null) {
/* 160 */       r = new HashSet<XSComponent>();
/* 161 */       this.referers.put(target, r);
/*     */     } 
/* 163 */     r.add(source);
/*     */   }
/*     */   
/*     */   public void facet(XSFacet facet) {}
/*     */   
/*     */   public void notation(XSNotation notation) {}
/*     */   
/*     */   public void identityConstraint(XSIdentityConstraint decl) {}
/*     */   
/*     */   public void xpath(XSXPath xp) {}
/*     */   
/*     */   public void wildcard(XSWildcard wc) {}
/*     */   
/*     */   public void empty(XSContentType empty) {}
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\RefererFinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */