/*     */ package com.sun.tools.xjc.reader.dtd;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class ModelGroup
/*     */   extends Term
/*     */ {
/*     */   Kind kind;
/*     */   
/*     */   enum Kind
/*     */   {
/*  50 */     CHOICE, SEQUENCE;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  55 */   private final List<Term> terms = new ArrayList<Term>();
/*     */   void normalize(List<Block> r, boolean optional) {
/*     */     Block b;
/*  58 */     switch (this.kind) {
/*     */       case SEQUENCE:
/*  60 */         for (Term t : this.terms)
/*  61 */           t.normalize(r, optional); 
/*     */         return;
/*     */       case CHOICE:
/*  64 */         b = new Block((isOptional() || optional), isRepeated());
/*  65 */         addAllElements(b);
/*  66 */         r.add(b);
/*     */         return;
/*     */     } 
/*     */   }
/*     */   
/*     */   void addAllElements(Block b) {
/*  72 */     for (Term t : this.terms)
/*  73 */       t.addAllElements(b); 
/*     */   }
/*     */   
/*     */   boolean isOptional() {
/*  77 */     switch (this.kind) {
/*     */       case SEQUENCE:
/*  79 */         for (Term t : this.terms) {
/*  80 */           if (!t.isOptional())
/*  81 */             return false; 
/*  82 */         }  return true;
/*     */       case CHOICE:
/*  84 */         for (Term t : this.terms) {
/*  85 */           if (t.isOptional())
/*  86 */             return true; 
/*  87 */         }  return false;
/*     */     } 
/*  89 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   
/*     */   boolean isRepeated() {
/*  94 */     switch (this.kind) {
/*     */       case SEQUENCE:
/*  96 */         return true;
/*     */       case CHOICE:
/*  98 */         for (Term t : this.terms) {
/*  99 */           if (t.isRepeated())
/* 100 */             return true; 
/* 101 */         }  return false;
/*     */     } 
/* 103 */     throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   
/*     */   void setKind(short connectorType) {
/*     */     Kind k;
/* 109 */     switch (connectorType) {
/*     */       case 1:
/* 111 */         k = Kind.SEQUENCE;
/*     */         break;
/*     */       case 0:
/* 114 */         k = Kind.CHOICE;
/*     */         break;
/*     */       default:
/* 117 */         throw new IllegalArgumentException();
/*     */     } 
/*     */     
/* 120 */     assert this.kind == null || k == this.kind;
/* 121 */     this.kind = k;
/*     */   }
/*     */   
/*     */   void addTerm(Term t) {
/* 125 */     if (t instanceof ModelGroup) {
/* 126 */       ModelGroup mg = (ModelGroup)t;
/* 127 */       if (mg.kind == this.kind) {
/* 128 */         this.terms.addAll(mg.terms);
/*     */         return;
/*     */       } 
/*     */     } 
/* 132 */     this.terms.add(t);
/*     */   }
/*     */ 
/*     */   
/*     */   Term wrapUp() {
/* 137 */     switch (this.terms.size()) {
/*     */       case 0:
/* 139 */         return EMPTY;
/*     */       case 1:
/* 141 */         assert this.kind == null;
/* 142 */         return this.terms.get(0);
/*     */     } 
/* 144 */     assert this.kind != null;
/* 145 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\dtd\ModelGroup.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */