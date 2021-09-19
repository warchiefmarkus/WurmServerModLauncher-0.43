/*    */ package com.wurmonline.server.spells;
/*    */ 
/*    */ import com.wurmonline.shared.constants.AttitudeConstants;
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
/*    */ public abstract class KarmaSpell
/*    */   extends Spell
/*    */   implements AttitudeConstants
/*    */ {
/*    */   public KarmaSpell(String aName, int aNum, int aCastingTime, int aCost, int aDifficulty, int aLevel, long cooldown) {
/* 32 */     super(aName, aNum, aCastingTime, aCost, aDifficulty, aLevel, cooldown, false);
/* 33 */     this.karmaSpell = true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\spells\KarmaSpell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */