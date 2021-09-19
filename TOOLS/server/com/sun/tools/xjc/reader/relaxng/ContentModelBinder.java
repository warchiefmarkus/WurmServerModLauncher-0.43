/*     */ package com.sun.tools.xjc.reader.relaxng;
/*     */ 
/*     */ import com.sun.tools.xjc.model.CAttributePropertyInfo;
/*     */ import com.sun.tools.xjc.model.CClassInfo;
/*     */ import com.sun.tools.xjc.model.CElementPropertyInfo;
/*     */ import com.sun.tools.xjc.model.CPropertyInfo;
/*     */ import com.sun.tools.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.xjc.model.Multiplicity;
/*     */ import com.sun.tools.xjc.model.TypeUse;
/*     */ import com.sun.tools.xjc.reader.RawTypeSet;
/*     */ import com.sun.xml.bind.v2.model.core.ID;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.kohsuke.rngom.digested.DAttributePattern;
/*     */ import org.kohsuke.rngom.digested.DChoicePattern;
/*     */ import org.kohsuke.rngom.digested.DMixedPattern;
/*     */ import org.kohsuke.rngom.digested.DOneOrMorePattern;
/*     */ import org.kohsuke.rngom.digested.DOptionalPattern;
/*     */ import org.kohsuke.rngom.digested.DPattern;
/*     */ import org.kohsuke.rngom.digested.DPatternWalker;
/*     */ import org.kohsuke.rngom.digested.DZeroOrMorePattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ContentModelBinder
/*     */   extends DPatternWalker
/*     */ {
/*     */   private final RELAXNGCompiler compiler;
/*     */   private final CClassInfo clazz;
/*     */   private boolean insideOptional = false;
/*  71 */   private int iota = 1;
/*     */   
/*     */   public ContentModelBinder(RELAXNGCompiler compiler, CClassInfo clazz) {
/*  74 */     this.compiler = compiler;
/*  75 */     this.clazz = clazz;
/*     */   }
/*     */   
/*     */   public Void onMixed(DMixedPattern p) {
/*  79 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Void onChoice(DChoicePattern p) {
/*  83 */     boolean old = this.insideOptional;
/*  84 */     this.insideOptional = true;
/*  85 */     super.onChoice(p);
/*  86 */     this.insideOptional = old;
/*  87 */     return null;
/*     */   }
/*     */   
/*     */   public Void onOptional(DOptionalPattern p) {
/*  91 */     boolean old = this.insideOptional;
/*  92 */     this.insideOptional = true;
/*  93 */     super.onOptional(p);
/*  94 */     this.insideOptional = old;
/*  95 */     return null;
/*     */   }
/*     */   
/*     */   public Void onZeroOrMore(DZeroOrMorePattern p) {
/*  99 */     return onRepeated((DPattern)p, true);
/*     */   }
/*     */   
/*     */   public Void onOneOrMore(DOneOrMorePattern p) {
/* 103 */     return onRepeated((DPattern)p, this.insideOptional);
/*     */   }
/*     */ 
/*     */   
/*     */   private Void onRepeated(DPattern p, boolean optional) {
/* 108 */     RawTypeSet rts = RawTypeSetBuilder.build(this.compiler, p, optional ? Multiplicity.STAR : Multiplicity.PLUS);
/* 109 */     if (rts.canBeTypeRefs == RawTypeSet.Mode.SHOULD_BE_TYPEREF) {
/* 110 */       CElementPropertyInfo prop = new CElementPropertyInfo(calcName(p), CElementPropertyInfo.CollectionMode.REPEATED_ELEMENT, ID.NONE, null, null, null, p.getLocation(), !optional);
/*     */       
/* 112 */       rts.addTo(prop);
/* 113 */       this.clazz.addProperty((CPropertyInfo)prop);
/*     */     } else {
/* 115 */       CReferencePropertyInfo prop = new CReferencePropertyInfo(calcName(p), true, false, null, null, p.getLocation());
/*     */       
/* 117 */       rts.addTo(prop);
/* 118 */       this.clazz.addProperty((CPropertyInfo)prop);
/*     */     } 
/*     */     
/* 121 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Void onAttribute(DAttributePattern p) {
/* 126 */     QName name = p.getName().listNames().iterator().next();
/*     */     
/* 128 */     CAttributePropertyInfo ap = new CAttributePropertyInfo(calcName((DPattern)p), null, null, p.getLocation(), name, (TypeUse)p.getChild().accept(this.compiler.typeUseBinder), null, !this.insideOptional);
/*     */ 
/*     */ 
/*     */     
/* 132 */     this.clazz.addProperty((CPropertyInfo)ap);
/*     */     
/* 134 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private String calcName(DPattern p) {
/* 139 */     return "field" + this.iota++;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\relaxng\ContentModelBinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */