/*     */ package com.sun.tools.xjc.reader.xmlschema;
/*     */ 
/*     */ import com.sun.tools.xjc.model.Multiplicity;
/*     */ import com.sun.xml.xsom.XSElementDecl;
/*     */ import com.sun.xml.xsom.XSModelGroup;
/*     */ import com.sun.xml.xsom.XSModelGroupDecl;
/*     */ import com.sun.xml.xsom.XSParticle;
/*     */ import com.sun.xml.xsom.XSWildcard;
/*     */ import com.sun.xml.xsom.visitor.XSTermFunction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MultiplicityCounter
/*     */   implements XSTermFunction<Multiplicity>
/*     */ {
/*  57 */   public static final MultiplicityCounter theInstance = new MultiplicityCounter();
/*     */ 
/*     */   
/*     */   public Multiplicity particle(XSParticle p) {
/*     */     Integer max;
/*  62 */     Multiplicity m = (Multiplicity)p.getTerm().apply(this);
/*     */ 
/*     */     
/*  65 */     if (m.max == null || p.getMaxOccurs() == -1) {
/*  66 */       max = null;
/*     */     } else {
/*  68 */       max = Integer.valueOf(p.getMaxOccurs());
/*     */     } 
/*  70 */     return Multiplicity.multiply(m, Multiplicity.create(p.getMinOccurs(), max));
/*     */   }
/*     */   
/*     */   public Multiplicity wildcard(XSWildcard wc) {
/*  74 */     return Multiplicity.ONE;
/*     */   }
/*     */   
/*     */   public Multiplicity modelGroupDecl(XSModelGroupDecl decl) {
/*  78 */     return modelGroup(decl.getModelGroup());
/*     */   }
/*     */   
/*     */   public Multiplicity modelGroup(XSModelGroup group) {
/*  82 */     boolean isChoice = (group.getCompositor() == XSModelGroup.CHOICE);
/*     */     
/*  84 */     Multiplicity r = Multiplicity.ZERO;
/*     */     
/*  86 */     for (XSParticle p : group.getChildren()) {
/*  87 */       Multiplicity m = particle(p);
/*     */       
/*  89 */       if (r == null) {
/*  90 */         r = m;
/*     */       
/*     */       }
/*  93 */       else if (isChoice) {
/*  94 */         r = Multiplicity.choice(r, m);
/*     */       } else {
/*  96 */         r = Multiplicity.group(r, m);
/*     */       } 
/*     */     } 
/*  99 */     return r;
/*     */   }
/*     */   
/*     */   public Multiplicity elementDecl(XSElementDecl decl) {
/* 103 */     return Multiplicity.ONE;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\xmlschema\MultiplicityCounter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */