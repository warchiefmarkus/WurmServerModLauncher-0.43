/*    */ package com.sun.tools.xjc.reader.relaxng;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import org.kohsuke.rngom.digested.DDefine;
/*    */ import org.kohsuke.rngom.digested.DGrammarPattern;
/*    */ import org.kohsuke.rngom.digested.DPatternVisitor;
/*    */ import org.kohsuke.rngom.digested.DPatternWalker;
/*    */ import org.kohsuke.rngom.digested.DRefPattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class DefineFinder
/*    */   extends DPatternWalker
/*    */ {
/* 54 */   public final Set<DDefine> defs = new HashSet<DDefine>();
/*    */   
/*    */   public Void onGrammar(DGrammarPattern p) {
/* 57 */     for (DDefine def : p) {
/* 58 */       this.defs.add(def);
/* 59 */       def.getPattern().accept((DPatternVisitor)this);
/*    */     } 
/*    */     
/* 62 */     return (Void)p.getStart().accept((DPatternVisitor)this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Void onRef(DRefPattern p) {
/* 70 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\tools\xjc\reader\relaxng\DefineFinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */