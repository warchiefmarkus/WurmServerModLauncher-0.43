/*    */ package com.wurmonline.server.loot;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ public class PercentChanceLootItemFunc
/*    */   implements LootItemFunc
/*    */ {
/* 12 */   protected static final Logger logger = Logger.getLogger(PercentChanceLootItemFunc.class.getName());
/*    */ 
/*    */   
/*    */   public Optional<LootItem> item(Creature victim, Creature receiver, LootPool pool) {
/* 16 */     List<LootItem> lootItems = pool.getLootItems();
/* 17 */     lootItems.sort(Comparator.comparingDouble(LootItem::getItemChance));
/* 18 */     double r = pool.getRandom().nextDouble();
/* 19 */     return lootItems.stream().filter(i -> {
/*    */           boolean success = (r < i.getItemChance());
/*    */           if (!success)
/*    */             logger.info(receiver.getName() + " failed loot roll for " + i.getItemName() + ": " + r + " not less than " + i.getItemChance()); 
/*    */           return success;
/* 24 */         }).findFirst();
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\loot\PercentChanceLootItemFunc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */