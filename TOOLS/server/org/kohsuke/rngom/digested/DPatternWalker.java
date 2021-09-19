/*    */ package org.kohsuke.rngom.digested;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DPatternWalker
/*    */   implements DPatternVisitor<Void>
/*    */ {
/*    */   public Void onAttribute(DAttributePattern p) {
/* 10 */     return onXmlToken(p);
/*    */   }
/*    */   
/*    */   protected Void onXmlToken(DXmlTokenPattern p) {
/* 14 */     return onUnary(p);
/*    */   }
/*    */   
/*    */   public Void onChoice(DChoicePattern p) {
/* 18 */     return onContainer(p);
/*    */   }
/*    */   
/*    */   protected Void onContainer(DContainerPattern p) {
/* 22 */     for (DPattern c = p.firstChild(); c != null; c = c.next)
/* 23 */       c.accept(this); 
/* 24 */     return null;
/*    */   }
/*    */   
/*    */   public Void onData(DDataPattern p) {
/* 28 */     return null;
/*    */   }
/*    */   
/*    */   public Void onElement(DElementPattern p) {
/* 32 */     return onXmlToken(p);
/*    */   }
/*    */   
/*    */   public Void onEmpty(DEmptyPattern p) {
/* 36 */     return null;
/*    */   }
/*    */   
/*    */   public Void onGrammar(DGrammarPattern p) {
/* 40 */     return p.getStart().<Void>accept(this);
/*    */   }
/*    */   
/*    */   public Void onGroup(DGroupPattern p) {
/* 44 */     return onContainer(p);
/*    */   }
/*    */   
/*    */   public Void onInterleave(DInterleavePattern p) {
/* 48 */     return onContainer(p);
/*    */   }
/*    */   
/*    */   public Void onList(DListPattern p) {
/* 52 */     return onUnary(p);
/*    */   }
/*    */   
/*    */   public Void onMixed(DMixedPattern p) {
/* 56 */     return onUnary(p);
/*    */   }
/*    */   
/*    */   public Void onNotAllowed(DNotAllowedPattern p) {
/* 60 */     return null;
/*    */   }
/*    */   
/*    */   public Void onOneOrMore(DOneOrMorePattern p) {
/* 64 */     return onUnary(p);
/*    */   }
/*    */   
/*    */   public Void onOptional(DOptionalPattern p) {
/* 68 */     return onUnary(p);
/*    */   }
/*    */   
/*    */   public Void onRef(DRefPattern p) {
/* 72 */     return p.getTarget().getPattern().<Void>accept(this);
/*    */   }
/*    */   
/*    */   public Void onText(DTextPattern p) {
/* 76 */     return null;
/*    */   }
/*    */   
/*    */   public Void onValue(DValuePattern p) {
/* 80 */     return null;
/*    */   }
/*    */   
/*    */   public Void onZeroOrMore(DZeroOrMorePattern p) {
/* 84 */     return onUnary(p);
/*    */   }
/*    */   
/*    */   protected Void onUnary(DUnaryPattern p) {
/* 88 */     return p.getChild().<Void>accept(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\digested\DPatternWalker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */