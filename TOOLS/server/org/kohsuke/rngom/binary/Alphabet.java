/*    */ package org.kohsuke.rngom.binary;
/*    */ 
/*    */ import org.kohsuke.rngom.nc.ChoiceNameClass;
/*    */ import org.kohsuke.rngom.nc.NameClass;
/*    */ 
/*    */ class Alphabet {
/*    */   private NameClass nameClass;
/*    */   
/*    */   boolean isEmpty() {
/* 10 */     return (this.nameClass == null);
/*    */   }
/*    */   
/*    */   void addElement(NameClass nc) {
/* 14 */     if (this.nameClass == null) {
/* 15 */       this.nameClass = nc;
/* 16 */     } else if (nc != null) {
/* 17 */       this.nameClass = (NameClass)new ChoiceNameClass(this.nameClass, nc);
/*    */     } 
/*    */   }
/*    */   void addAlphabet(Alphabet a) {
/* 21 */     addElement(a.nameClass);
/*    */   }
/*    */   
/*    */   void checkOverlap(Alphabet a) throws RestrictionViolationException {
/* 25 */     if (this.nameClass != null && a.nameClass != null && this.nameClass.hasOverlapWith(a.nameClass))
/*    */     {
/*    */       
/* 28 */       throw new RestrictionViolationException("interleave_element_overlap");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\org\kohsuke\rngom\binary\Alphabet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */