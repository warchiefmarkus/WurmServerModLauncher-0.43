/*     */ package com.sun.tools.xjc.reader.relaxng;
/*     */ 
/*     */ import com.sun.tools.xjc.model.CBuiltinLeafInfo;
/*     */ import com.sun.tools.xjc.model.TypeUse;
/*     */ import com.sun.tools.xjc.model.TypeUseFactory;
/*     */ import org.kohsuke.rngom.digested.DAttributePattern;
/*     */ import org.kohsuke.rngom.digested.DChoicePattern;
/*     */ import org.kohsuke.rngom.digested.DContainerPattern;
/*     */ import org.kohsuke.rngom.digested.DDataPattern;
/*     */ import org.kohsuke.rngom.digested.DElementPattern;
/*     */ import org.kohsuke.rngom.digested.DEmptyPattern;
/*     */ import org.kohsuke.rngom.digested.DGrammarPattern;
/*     */ import org.kohsuke.rngom.digested.DGroupPattern;
/*     */ import org.kohsuke.rngom.digested.DInterleavePattern;
/*     */ import org.kohsuke.rngom.digested.DListPattern;
/*     */ import org.kohsuke.rngom.digested.DMixedPattern;
/*     */ import org.kohsuke.rngom.digested.DNotAllowedPattern;
/*     */ import org.kohsuke.rngom.digested.DOneOrMorePattern;
/*     */ import org.kohsuke.rngom.digested.DOptionalPattern;
/*     */ import org.kohsuke.rngom.digested.DPattern;
/*     */ import org.kohsuke.rngom.digested.DPatternVisitor;
/*     */ import org.kohsuke.rngom.digested.DRefPattern;
/*     */ import org.kohsuke.rngom.digested.DTextPattern;
/*     */ import org.kohsuke.rngom.digested.DValuePattern;
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
/*     */ 
/*     */ final class TypeUseBinder
/*     */   implements DPatternVisitor<TypeUse>
/*     */ {
/*     */   private final RELAXNGCompiler compiler;
/*     */   
/*     */   public TypeUseBinder(RELAXNGCompiler compiler) {
/*  77 */     this.compiler = compiler;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeUse onGrammar(DGrammarPattern p) {
/*  82 */     return (TypeUse)CBuiltinLeafInfo.STRING;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeUse onChoice(DChoicePattern p) {
/*  87 */     return (TypeUse)CBuiltinLeafInfo.STRING;
/*     */   }
/*     */   
/*     */   public TypeUse onData(DDataPattern p) {
/*  91 */     return onDataType(p.getDatatypeLibrary(), p.getType());
/*     */   }
/*     */   
/*     */   public TypeUse onValue(DValuePattern p) {
/*  95 */     return onDataType(p.getDatatypeLibrary(), p.getType());
/*     */   }
/*     */   
/*     */   private TypeUse onDataType(String datatypeLibrary, String type) {
/*  99 */     DatatypeLib lib = this.compiler.datatypes.get(datatypeLibrary);
/* 100 */     if (lib != null) {
/* 101 */       TypeUse use = lib.get(type);
/* 102 */       if (use != null) {
/* 103 */         return use;
/*     */       }
/*     */     } 
/*     */     
/* 107 */     return (TypeUse)CBuiltinLeafInfo.STRING;
/*     */   }
/*     */   
/*     */   public TypeUse onInterleave(DInterleavePattern p) {
/* 111 */     return onContainer((DContainerPattern)p);
/*     */   }
/*     */   
/*     */   public TypeUse onGroup(DGroupPattern p) {
/* 115 */     return onContainer((DContainerPattern)p);
/*     */   }
/*     */   
/*     */   private TypeUse onContainer(DContainerPattern p) {
/* 119 */     TypeUse t = null;
/* 120 */     for (DPattern child : p) {
/* 121 */       TypeUse s = (TypeUse)child.accept(this);
/* 122 */       if (t != null && t != s)
/* 123 */         return (TypeUse)CBuiltinLeafInfo.STRING; 
/* 124 */       t = s;
/*     */     } 
/* 126 */     return t;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeUse onNotAllowed(DNotAllowedPattern p) {
/* 131 */     return error();
/*     */   }
/*     */   
/*     */   public TypeUse onEmpty(DEmptyPattern p) {
/* 135 */     return (TypeUse)CBuiltinLeafInfo.STRING;
/*     */   }
/*     */   
/*     */   public TypeUse onList(DListPattern p) {
/* 139 */     return (TypeUse)p.getChild().accept(this);
/*     */   }
/*     */   
/*     */   public TypeUse onOneOrMore(DOneOrMorePattern p) {
/* 143 */     return TypeUseFactory.makeCollection((TypeUse)p.getChild().accept(this));
/*     */   }
/*     */   
/*     */   public TypeUse onZeroOrMore(DZeroOrMorePattern p) {
/* 147 */     return TypeUseFactory.makeCollection((TypeUse)p.getChild().accept(this));
/*     */   }
/*     */   
/*     */   public TypeUse onOptional(DOptionalPattern p) {
/* 151 */     return (TypeUse)CBuiltinLeafInfo.STRING;
/*     */   }
/*     */ 
/*     */   
/*     */   public TypeUse onRef(DRefPattern p) {
/* 156 */     return (TypeUse)p.getTarget().getPattern().accept(this);
/*     */   }
/*     */   
/*     */   public TypeUse onText(DTextPattern p) {
/* 160 */     return (TypeUse)CBuiltinLeafInfo.STRING;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TypeUse onAttribute(DAttributePattern p) {
/* 169 */     return error();
/*     */   }
/*     */   
/*     */   public TypeUse onElement(DElementPattern p) {
/* 173 */     return error();
/*     */   }
/*     */   
/*     */   public TypeUse onMixed(DMixedPattern p) {
/* 177 */     return error();
/*     */   }
/*     */   
/*     */   private TypeUse error() {
/* 181 */     throw new IllegalStateException();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\relaxng\TypeUseBinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */