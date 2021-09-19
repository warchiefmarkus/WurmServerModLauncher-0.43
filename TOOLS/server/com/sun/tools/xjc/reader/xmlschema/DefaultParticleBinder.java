/*     */ package com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.tools.xjc.model.CClassInfo;
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSTerm;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.visitor.XSTermVisitor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ final class DefaultParticleBinder
/*     */   extends ParticleBinder
/*     */ {
/*     */   public void build(XSParticle p, Collection<XSParticle> forcedProps) {
/*  68 */     Checker checker = checkCollision(p, forcedProps);
/*     */     
/*  70 */     if (checker.hasNameCollision()) {
/*  71 */       CReferencePropertyInfo prop = new CReferencePropertyInfo((getCurrentBean().getBaseClass() == null) ? "Content" : "Rest", true, false, (XSComponent)p, this.builder.getBindInfo((XSComponent)p).toCustomizationList(), p.getLocator());
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  76 */       RawTypeSetBuilder.build(p, false).addTo(prop);
/*  77 */       prop.javadoc = Messages.format("DefaultParticleBinder.FallbackJavadoc", new Object[] { checker.getCollisionInfo().toString() });
/*     */ 
/*     */       
/*  80 */       getCurrentBean().addProperty((CPropertyInfo)prop);
/*     */     } else {
/*  82 */       (new Builder(checker.markedParticles)).particle(p);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkFallback(XSParticle p) {
/*  88 */     return checkCollision(p, Collections.emptyList()).hasNameCollision();
/*     */   }
/*     */ 
/*     */   
/*     */   private Checker checkCollision(XSParticle p, Collection<XSParticle> forcedProps) {
/*  93 */     Checker checker = new Checker(forcedProps);
/*     */     
/*  95 */     CClassInfo superClass = getCurrentBean().getBaseClass();
/*     */     
/*  97 */     if (superClass != null)
/*  98 */       checker.readSuperClass(superClass); 
/*  99 */     checker.particle(p);
/*     */     
/* 101 */     return checker;
/*     */   }
/*     */   private final class Checker implements XSTermVisitor { private CollisionInfo collisionInfo; private final NameCollisionChecker cchecker; private final Collection<XSParticle> forcedProps; private XSParticle outerParticle;
/*     */     public final Map<XSParticle, String> markedParticles;
/*     */     private final Map<XSParticle, String> labelCache;
/*     */     
/*     */     boolean hasNameCollision() {
/*     */       return (this.collisionInfo != null);
/*     */     }
/*     */     
/*     */     CollisionInfo getCollisionInfo() {
/*     */       return this.collisionInfo;
/*     */     }
/*     */     
/*     */     public void particle(XSParticle p) {
/*     */       if (DefaultParticleBinder.this.getLocalPropCustomization(p) != null || DefaultParticleBinder.this.builder.getLocalDomCustomization(p) != null) {
/*     */         check(p);
/*     */         mark(p);
/*     */         return;
/*     */       } 
/*     */       XSTerm t = p.getTerm();
/*     */       if (p.isRepeated() && (t.isModelGroup() || t.isModelGroupDecl())) {
/*     */         mark(p);
/*     */         return;
/*     */       } 
/*     */       if (this.forcedProps.contains(p)) {
/*     */         mark(p);
/*     */         return;
/*     */       } 
/*     */       this.outerParticle = p;
/*     */       t.visit(this);
/*     */     }
/*     */     
/* 134 */     Checker(Collection<XSParticle> forcedProps) { this.collisionInfo = null;
/*     */ 
/*     */       
/* 137 */       this.cchecker = new NameCollisionChecker();
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
/* 230 */       this.markedParticles = new HashMap<XSParticle, String>();
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
/* 303 */       this.labelCache = new Hashtable<XSParticle, String>(); this.forcedProps = forcedProps; }
/*     */     public void elementDecl(XSElementDecl decl) { check(this.outerParticle); mark(this.outerParticle); }
/*     */     public void modelGroup(XSModelGroup mg) { if (mg.getCompositor() == XSModelGroup.Compositor.CHOICE && DefaultParticleBinder.this.builder.getGlobalBinding().isChoiceContentPropertyEnabled()) { mark(this.outerParticle); return; }
/*     */        for (XSParticle child : mg.getChildren())
/*     */         particle(child);  }
/*     */     public void modelGroupDecl(XSModelGroupDecl decl) { modelGroup(decl.getModelGroup()); }
/*     */     public void wildcard(XSWildcard wc) { mark(this.outerParticle); }
/* 310 */     void readSuperClass(CClassInfo ci) { this.cchecker.readSuperClass(ci); } private String computeLabel(XSParticle p) { String label = this.labelCache.get(p);
/* 311 */       if (label == null)
/* 312 */         this.labelCache.put(p, label = DefaultParticleBinder.this.computeLabel(p)); 
/* 313 */       return label; }
/*     */     private void check(XSParticle p) { if (this.collisionInfo == null)
/*     */         this.collisionInfo = this.cchecker.check(p);  } private void mark(XSParticle p) { this.markedParticles.put(p, computeLabel(p)); } private final class NameCollisionChecker {
/*     */       CollisionInfo check(XSParticle p) { String label = DefaultParticleBinder.Checker.this.computeLabel(p);
/*     */         if (this.occupiedLabels.containsKey(label))
/*     */           return new CollisionInfo(label, p.getLocator(), ((CPropertyInfo)this.occupiedLabels.get(label)).locator); 
/*     */         for (XSParticle jp : this.particles) {
/*     */           if (!check(p, jp))
/*     */             return new CollisionInfo(label, p.getLocator(), jp.getLocator()); 
/*     */         } 
/*     */         this.particles.add(p);
/*     */         return null; } private final List<XSParticle> particles = new ArrayList<XSParticle>(); private final Map<String, CPropertyInfo> occupiedLabels = new HashMap<String, CPropertyInfo>(); private boolean check(XSParticle p1, XSParticle p2) { return !DefaultParticleBinder.Checker.this.computeLabel(p1).equals(DefaultParticleBinder.Checker.this.computeLabel(p2)); } void readSuperClass(CClassInfo base) {
/*     */         for (; base != null; base = base.getBaseClass()) {
/*     */           for (CPropertyInfo p : base.getProperties())
/*     */             this.occupiedLabels.put(p.getName(true), p); 
/*     */         } 
/*     */       } private NameCollisionChecker() {}
/*     */     } }
/*     */    private final class Builder implements XSTermVisitor {
/*     */     Builder(Map<XSParticle, String> markedParticles) {
/* 333 */       this.markedParticles = markedParticles;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final Map<XSParticle, String> markedParticles;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean insideOptionalParticle;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private boolean marked(XSParticle p) {
/* 351 */       return this.markedParticles.containsKey(p);
/*     */     }
/*     */     
/*     */     private String getLabel(XSParticle p) {
/* 355 */       return this.markedParticles.get(p);
/*     */     }
/*     */     
/*     */     public void particle(XSParticle p) {
/* 359 */       XSTerm t = p.getTerm();
/*     */       
/* 361 */       if (marked(p)) {
/* 362 */         BIProperty cust = BIProperty.getCustomization((XSComponent)p);
/* 363 */         CPropertyInfo prop = cust.createElementOrReferenceProperty(getLabel(p), false, p, RawTypeSetBuilder.build(p, this.insideOptionalParticle));
/*     */         
/* 365 */         DefaultParticleBinder.this.getCurrentBean().addProperty(prop);
/*     */       } else {
/*     */         
/* 368 */         assert !p.isRepeated();
/*     */         
/* 370 */         boolean oldIOP = this.insideOptionalParticle;
/* 371 */         this.insideOptionalParticle |= (p.getMinOccurs() == 0) ? 1 : 0;
/*     */         
/* 373 */         t.visit(this);
/* 374 */         this.insideOptionalParticle = oldIOP;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void elementDecl(XSElementDecl e) {
/*     */       assert false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void wildcard(XSWildcard wc) {
/*     */       assert false;
/*     */     }
/*     */     
/*     */     public void modelGroupDecl(XSModelGroupDecl decl) {
/* 389 */       modelGroup(decl.getModelGroup());
/*     */     }
/*     */     
/*     */     public void modelGroup(XSModelGroup mg) {
/* 393 */       boolean oldIOP = this.insideOptionalParticle;
/* 394 */       this.insideOptionalParticle |= (mg.getCompositor() == XSModelGroup.CHOICE) ? 1 : 0;
/*     */       
/* 396 */       for (XSParticle p : mg.getChildren()) {
/* 397 */         particle(p);
/*     */       }
/* 399 */       this.insideOptionalParticle = oldIOP;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\DefaultParticleBinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */