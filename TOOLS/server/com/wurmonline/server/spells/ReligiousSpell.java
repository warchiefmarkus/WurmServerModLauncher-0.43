/*    */ package com.wurmonline.server.spells;
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
/*    */ abstract class ReligiousSpell
/*    */   extends Spell
/*    */ {
/*    */   ReligiousSpell(String aName, int aNum, int aCastingTime, int aCost, int aDifficulty, int aLevel, long cooldown) {
/* 27 */     super(aName, aNum, aCastingTime, aCost, aDifficulty, aLevel, cooldown, true);
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\ReligiousSpell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */