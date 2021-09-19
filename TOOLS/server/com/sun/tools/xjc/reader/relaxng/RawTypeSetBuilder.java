/*     */ package com.sun.tools.xjc.reader.relaxng;
/*     */ 
/*     */ import com.sun.tools.xjc.model.CClassInfo;
/*     */ import com.sun.tools.xjc.model.CElementPropertyInfo;
/*     */ import com.sun.tools.xjc.model.CNonElement;
/*     */ import com.sun.tools.xjc.model.CReferencePropertyInfo;
/*     */ import com.sun.tools.xjc.model.CTypeInfo;
/*     */ import com.sun.tools.xjc.model.CTypeRef;
/*     */ import com.sun.tools.xjc.model.Multiplicity;
/*     */ import com.sun.tools.xjc.reader.RawTypeSet;
/*     */ import com.sun.xml.bind.v2.model.core.ID;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.kohsuke.rngom.digested.DAttributePattern;
/*     */ import org.kohsuke.rngom.digested.DElementPattern;
/*     */ import org.kohsuke.rngom.digested.DOneOrMorePattern;
/*     */ import org.kohsuke.rngom.digested.DPattern;
/*     */ import org.kohsuke.rngom.digested.DPatternVisitor;
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
/*     */ public final class RawTypeSetBuilder
/*     */   extends DPatternWalker
/*     */ {
/*     */   private Multiplicity mul;
/*     */   
/*     */   public static RawTypeSet build(RELAXNGCompiler compiler, DPattern contentModel, Multiplicity mul) {
/*  65 */     RawTypeSetBuilder builder = new RawTypeSetBuilder(compiler, mul);
/*  66 */     contentModel.accept((DPatternVisitor)builder);
/*  67 */     return builder.create();
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
/*  78 */   private final Set<RawTypeSet.Ref> refs = new HashSet<RawTypeSet.Ref>();
/*     */   
/*     */   private final RELAXNGCompiler compiler;
/*     */   
/*     */   public RawTypeSetBuilder(RELAXNGCompiler compiler, Multiplicity mul) {
/*  83 */     this.mul = mul;
/*  84 */     this.compiler = compiler;
/*     */   }
/*     */   
/*     */   private RawTypeSet create() {
/*  88 */     return new RawTypeSet(this.refs, this.mul);
/*     */   }
/*     */ 
/*     */   
/*     */   public Void onAttribute(DAttributePattern p) {
/*  93 */     return null;
/*     */   }
/*     */   
/*     */   public Void onElement(DElementPattern p) {
/*  97 */     CTypeInfo[] tis = this.compiler.classes.get(p);
/*  98 */     if (tis != null) {
/*  99 */       for (CTypeInfo ti : tis) {
/* 100 */         this.refs.add(new CClassInfoRef((CClassInfo)ti));
/*     */       }
/*     */     } else {
/*     */       assert false;
/*     */     } 
/* 105 */     return null;
/*     */   }
/*     */   
/*     */   public Void onZeroOrMore(DZeroOrMorePattern p) {
/* 109 */     this.mul = this.mul.makeRepeated();
/* 110 */     return super.onZeroOrMore(p);
/*     */   }
/*     */   
/*     */   public Void onOneOrMore(DOneOrMorePattern p) {
/* 114 */     this.mul = this.mul.makeRepeated();
/* 115 */     return super.onOneOrMore(p);
/*     */   }
/*     */   
/*     */   private static final class CClassInfoRef
/*     */     extends RawTypeSet.Ref
/*     */   {
/*     */     private final CClassInfo ci;
/*     */     
/*     */     CClassInfoRef(CClassInfo ci) {
/* 124 */       this.ci = ci;
/* 125 */       assert ci.isElement();
/*     */     }
/*     */     
/*     */     protected ID id() {
/* 129 */       return ID.NONE;
/*     */     }
/*     */     
/*     */     protected boolean isListOfValues() {
/* 133 */       return false;
/*     */     }
/*     */     
/*     */     protected RawTypeSet.Mode canBeType(RawTypeSet parent) {
/* 137 */       return RawTypeSet.Mode.SHOULD_BE_TYPEREF;
/*     */     }
/*     */     
/*     */     protected void toElementRef(CReferencePropertyInfo prop) {
/* 141 */       prop.getElements().add(this.ci);
/*     */     }
/*     */     
/*     */     protected CTypeRef toTypeRef(CElementPropertyInfo ep) {
/* 145 */       return new CTypeRef((CNonElement)this.ci, this.ci.getElementName(), this.ci.getTypeName(), false, null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\relaxng\RawTypeSetBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */