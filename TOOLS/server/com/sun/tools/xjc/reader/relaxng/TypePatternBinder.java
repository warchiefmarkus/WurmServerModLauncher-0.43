/*     */ package com.sun.tools.xjc.reader.relaxng;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.Stack;
/*     */ import org.kohsuke.rngom.digested.DAttributePattern;
/*     */ import org.kohsuke.rngom.digested.DChoicePattern;
/*     */ import org.kohsuke.rngom.digested.DDefine;
/*     */ import org.kohsuke.rngom.digested.DListPattern;
/*     */ import org.kohsuke.rngom.digested.DMixedPattern;
/*     */ import org.kohsuke.rngom.digested.DOneOrMorePattern;
/*     */ import org.kohsuke.rngom.digested.DOptionalPattern;
/*     */ import org.kohsuke.rngom.digested.DPatternWalker;
/*     */ import org.kohsuke.rngom.digested.DRefPattern;
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
/*     */ final class TypePatternBinder
/*     */   extends DPatternWalker
/*     */ {
/*     */   private boolean canInherit;
/*  61 */   private final Stack<Boolean> stack = new Stack<Boolean>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   private final Set<DDefine> cannotBeInherited = new HashSet<DDefine>();
/*     */ 
/*     */   
/*     */   void reset() {
/*  70 */     this.canInherit = true;
/*  71 */     this.stack.clear();
/*     */   }
/*     */   
/*     */   public Void onRef(DRefPattern p) {
/*  75 */     if (!this.canInherit) {
/*  76 */       this.cannotBeInherited.add(p.getTarget());
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */       
/*  83 */       this.canInherit = false;
/*     */     } 
/*  85 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Void onChoice(DChoicePattern p) {
/*  95 */     push(false);
/*  96 */     super.onChoice(p);
/*  97 */     pop();
/*  98 */     return null;
/*     */   }
/*     */   
/*     */   public Void onAttribute(DAttributePattern p) {
/* 102 */     push(false);
/* 103 */     super.onAttribute(p);
/* 104 */     pop();
/* 105 */     return null;
/*     */   }
/*     */   
/*     */   public Void onList(DListPattern p) {
/* 109 */     push(false);
/* 110 */     super.onList(p);
/* 111 */     pop();
/* 112 */     return null;
/*     */   }
/*     */   
/*     */   public Void onMixed(DMixedPattern p) {
/* 116 */     push(false);
/* 117 */     super.onMixed(p);
/* 118 */     pop();
/* 119 */     return null;
/*     */   }
/*     */   
/*     */   public Void onOneOrMore(DOneOrMorePattern p) {
/* 123 */     push(false);
/* 124 */     super.onOneOrMore(p);
/* 125 */     pop();
/* 126 */     return null;
/*     */   }
/*     */   
/*     */   public Void onZeroOrMore(DZeroOrMorePattern p) {
/* 130 */     push(false);
/* 131 */     super.onZeroOrMore(p);
/* 132 */     pop();
/* 133 */     return null;
/*     */   }
/*     */   
/*     */   public Void onOptional(DOptionalPattern p) {
/* 137 */     push(false);
/* 138 */     super.onOptional(p);
/* 139 */     pop();
/* 140 */     return null;
/*     */   }
/*     */   
/*     */   private void push(boolean v) {
/* 144 */     this.stack.push(Boolean.valueOf(this.canInherit));
/* 145 */     this.canInherit = v;
/*     */   }
/*     */   
/*     */   private void pop() {
/* 149 */     this.canInherit = ((Boolean)this.stack.pop()).booleanValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\relaxng\TypePatternBinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */