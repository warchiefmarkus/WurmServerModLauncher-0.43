/*     */ package com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.xjc.model.Multiplicity;
/*     */ import com.sun.tools.xjc.reader.RawTypeSet;
/*     */ import com.sun.tools.xjc.reader.gbind.ConnectedComponent;
/*     */ import com.sun.tools.xjc.reader.gbind.Element;
/*     */ import com.sun.tools.xjc.reader.gbind.Expression;
/*     */ import com.sun.tools.xjc.reader.gbind.Graph;
/*     */ import com.sun.tools.xjc.reader.xmlschema.bindinfo.BIProperty;
/*     */ import com.sun.xml.bind.v2.model.core.WildcardMode;
/*     */ import com.sun.xml.xsom.XSComponent;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ExpressionParticleBinder
/*     */   extends ParticleBinder
/*     */ {
/*     */   public void build(XSParticle p, Collection<XSParticle> forcedProps) {
/*  66 */     Expression tree = ExpressionBuilder.createTree(p);
/*  67 */     Graph g = new Graph(tree);
/*  68 */     for (ConnectedComponent cc : g) {
/*  69 */       buildProperty(cc);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void buildProperty(ConnectedComponent cc) {
/*  77 */     StringBuilder propName = new StringBuilder();
/*  78 */     int nameTokenCount = 0;
/*     */     
/*  80 */     RawTypeSetBuilder rtsb = new RawTypeSetBuilder();
/*  81 */     for (Element e : cc) {
/*  82 */       GElement ge = (GElement)e;
/*     */       
/*  84 */       if (nameTokenCount < 3) {
/*  85 */         if (nameTokenCount != 0)
/*  86 */           propName.append("And"); 
/*  87 */         propName.append(makeJavaName(cc.isCollection(), ge.getPropertyNameSeed()));
/*  88 */         nameTokenCount++;
/*     */       } 
/*     */       
/*  91 */       if (e instanceof GElementImpl) {
/*  92 */         GElementImpl ei = (GElementImpl)e;
/*  93 */         rtsb.elementDecl(ei.decl);
/*     */         continue;
/*     */       } 
/*  96 */       if (e instanceof GWildcardElement) {
/*  97 */         GWildcardElement w = (GWildcardElement)e;
/*  98 */         rtsb.getRefs().add(new RawTypeSetBuilder.WildcardRef(w.isStrict() ? WildcardMode.STRICT : WildcardMode.SKIP));
/*     */         
/*     */         continue;
/*     */       } 
/* 102 */       assert false : e;
/*     */     } 
/*     */     
/* 105 */     Multiplicity m = Multiplicity.ONE;
/* 106 */     if (cc.isCollection())
/* 107 */       m = m.makeRepeated(); 
/* 108 */     if (!cc.isRequired()) {
/* 109 */       m = m.makeOptional();
/*     */     }
/* 111 */     RawTypeSet rts = new RawTypeSet(rtsb.getRefs(), m);
/*     */     
/* 113 */     XSParticle p = findSourceParticle(cc);
/*     */     
/* 115 */     BIProperty cust = BIProperty.getCustomization((XSComponent)p);
/* 116 */     CPropertyInfo prop = cust.createElementOrReferenceProperty(propName.toString(), false, p, rts);
/*     */     
/* 118 */     getCurrentBean().addProperty(prop);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private XSParticle findSourceParticle(ConnectedComponent cc) {
/* 131 */     XSParticle first = null;
/*     */     
/* 133 */     for (Element e : cc) {
/* 134 */       GElement ge = (GElement)e;
/* 135 */       for (XSParticle p : ge.particles) {
/* 136 */         if (first == null)
/* 137 */           first = p; 
/* 138 */         if (getLocalPropCustomization(p) != null) {
/* 139 */           return p;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     return first;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean checkFallback(XSParticle p) {
/* 152 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\ExpressionParticleBinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */