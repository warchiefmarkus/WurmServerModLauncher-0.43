/*    */ package com.wurmonline.server.loot;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ public class PercentChanceLootPoolChanceFunc
/*    */   implements LootPoolChanceFunc
/*    */ {
/*  9 */   protected static final Logger logger = Logger.getLogger(PercentChanceLootPoolChanceFunc.class.getName());
/*    */ 
/*    */   
/*    */   public boolean chance(Creature victim, Creature receiver, LootPool pool) {
/* 13 */     double r = pool.getRandom().nextDouble();
/* 14 */     boolean success = (r < pool.getLootPoolChance());
/* 15 */     if (!success)
/* 16 */       logger.info(receiver.getName() + " failed loot pool chance for " + pool.getName() + ": " + r + " not less than " + pool.getLootPoolChance()); 
/* 17 */     return success;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\loot\PercentChanceLootPoolChanceFunc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */