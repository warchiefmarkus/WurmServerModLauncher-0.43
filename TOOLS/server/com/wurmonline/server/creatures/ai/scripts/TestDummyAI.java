/*    */ package com.wurmonline.server.creatures.ai.scripts;
/*    */ 
/*    */ import com.wurmonline.server.bodys.Wound;
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.creatures.ai.CreatureAI;
/*    */ import com.wurmonline.server.creatures.ai.CreatureAIData;
/*    */ import com.wurmonline.server.items.NoSpaceException;
/*    */ import javax.annotation.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TestDummyAI
/*    */   extends CreatureAI
/*    */ {
/*    */   protected boolean pollMovement(Creature c, long delta) {
/* 18 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean pollAttack(Creature c, long delta) {
/* 25 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean pollBreeding(Creature c, long delta) {
/* 32 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public CreatureAIData createCreatureAIData() {
/* 38 */     return new TestDummyAIData();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void creatureCreated(Creature c) {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double receivedWound(Creature c, @Nullable Creature performer, byte dmgType, int dmgPosition, float armourMod, double damage) {
/* 51 */     if (performer != null) {
/*    */       
/*    */       try {
/*    */ 
/*    */         
/* 56 */         String message = "You dealt " + String.format("%.2f", new Object[] { Double.valueOf(damage / 65535.0D * 100.0D) }) + " to " + c.getBody().getBodyPart(dmgPosition).getName() + " of type " + Wound.getName(dmgType) + ".";
/* 57 */         performer.getCommunicator().sendNormalServerMessage(message);
/*    */       }
/* 59 */       catch (NoSpaceException noSpaceException) {}
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 65 */     return 0.0D;
/*    */   }
/*    */   
/*    */   public class TestDummyAIData extends CreatureAIData {}
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\ai\scripts\TestDummyAI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */