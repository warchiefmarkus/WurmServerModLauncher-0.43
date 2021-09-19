/*    */ package org.kohsuke.rngom.binary;
/*    */ 
/*    */ public class PatternBuilder {
/*    */   private final EmptyPattern empty;
/*    */   protected final NotAllowedPattern notAllowed;
/*    */   protected final PatternInterner interner;
/*    */   
/*    */   public PatternBuilder() {
/*  9 */     this.empty = new EmptyPattern();
/* 10 */     this.notAllowed = new NotAllowedPattern();
/* 11 */     this.interner = new PatternInterner();
/*    */   }
/*    */   
/*    */   public PatternBuilder(PatternBuilder parent) {
/* 15 */     this.empty = parent.empty;
/* 16 */     this.notAllowed = parent.notAllowed;
/* 17 */     this.interner = new PatternInterner(parent.interner);
/*    */   }
/*    */   
/*    */   Pattern makeEmpty() {
/* 21 */     return this.empty;
/*    */   }
/*    */   
/*    */   Pattern makeNotAllowed() {
/* 25 */     return this.notAllowed;
/*    */   }
/*    */   
/*    */   Pattern makeGroup(Pattern p1, Pattern p2) {
/* 29 */     if (p1 == this.empty)
/* 30 */       return p2; 
/* 31 */     if (p2 == this.empty)
/* 32 */       return p1; 
/* 33 */     if (p1 == this.notAllowed || p2 == this.notAllowed) {
/* 34 */       return this.notAllowed;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 39 */     Pattern p = new GroupPattern(p1, p2);
/* 40 */     return this.interner.intern(p);
/*    */   }
/*    */   
/*    */   Pattern makeInterleave(Pattern p1, Pattern p2) {
/* 44 */     if (p1 == this.empty)
/* 45 */       return p2; 
/* 46 */     if (p2 == this.empty)
/* 47 */       return p1; 
/* 48 */     if (p1 == this.notAllowed || p2 == this.notAllowed) {
/* 49 */       return this.notAllowed;
/*    */     }
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
/* 63 */     Pattern p = new InterleavePattern(p1, p2);
/* 64 */     return this.interner.intern(p);
/*    */   }
/*    */   
/*    */   Pattern makeChoice(Pattern p1, Pattern p2) {
/* 68 */     if (p1 == this.empty && p2.isNullable())
/* 69 */       return p2; 
/* 70 */     if (p2 == this.empty && p1.isNullable())
/* 71 */       return p1; 
/* 72 */     Pattern p = new ChoicePattern(p1, p2);
/* 73 */     return this.interner.intern(p);
/*    */   }
/*    */   
/*    */   Pattern makeOneOrMore(Pattern p) {
/* 77 */     if (p == this.empty || p == this.notAllowed || p instanceof OneOrMorePattern)
/*    */     {
/*    */       
/* 80 */       return p; } 
/* 81 */     Pattern p1 = new OneOrMorePattern(p);
/* 82 */     return this.interner.intern(p1);
/*    */   }
/*    */   
/*    */   Pattern makeOptional(Pattern p) {
/* 86 */     return makeChoice(p, this.empty);
/*    */   }
/*    */   
/*    */   Pattern makeZeroOrMore(Pattern p) {
/* 90 */     return makeOptional(makeOneOrMore(p));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\PatternBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */