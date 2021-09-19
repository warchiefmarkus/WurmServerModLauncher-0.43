/*    */ package com.wurmonline.server.loot;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import java.util.Optional;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ public class IncreasingChanceLootPoolChanceFunc
/*    */   implements LootPoolChanceFunc
/*    */ {
/* 11 */   protected static final Logger logger = Logger.getLogger(IncreasingChanceLootPoolChanceFunc.class.getName());
/* 12 */   private ConcurrentHashMap<Long, Double> progressiveChances = new ConcurrentHashMap<>();
/* 13 */   private double percentIncrease = 0.0010000000474974513D;
/*    */ 
/*    */   
/*    */   public IncreasingChanceLootPoolChanceFunc setPercentIncrease(double increase) {
/* 17 */     this.percentIncrease = increase;
/* 18 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean chance(Creature victim, Creature receiver, LootPool pool) {
/* 24 */     double r = pool.getRandom().nextDouble();
/* 25 */     double bonus = ((Double)Optional.<Double>ofNullable(this.progressiveChances.get(Long.valueOf(receiver.getWurmId()))).orElse(Double.valueOf(0.0D))).doubleValue();
/* 26 */     boolean success = (r < pool.getLootPoolChance() + bonus);
/* 27 */     if (!success) {
/*    */       
/* 29 */       this.progressiveChances.put(Long.valueOf(receiver.getWurmId()), Double.valueOf(bonus + this.percentIncrease));
/* 30 */       logger.info(receiver.getName() + " failed loot pool chance for " + pool.getName() + ". Increasing chance by " + this.percentIncrease + " to " + (pool
/* 31 */           .getLootPoolChance() + ((Double)this.progressiveChances.get(Long.valueOf(receiver.getWurmId()))).doubleValue()));
/*    */     
/*    */     }
/*    */     else {
/*    */       
/* 36 */       this.progressiveChances.remove(Long.valueOf(receiver.getWurmId()));
/* 37 */       logger.info(receiver.getName() + " succeeded loot pool chance for " + pool.getName() + ". Clearing increases.");
/*    */     } 
/*    */     
/* 40 */     return success;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\loot\IncreasingChanceLootPoolChanceFunc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */