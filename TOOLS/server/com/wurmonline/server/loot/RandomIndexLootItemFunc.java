/*    */ package com.wurmonline.server.loot;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import java.util.Optional;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RandomIndexLootItemFunc
/*    */   implements LootItemFunc
/*    */ {
/*    */   public Optional<LootItem> item(Creature victim, Creature receiver, LootPool pool) {
/* 12 */     return Optional.ofNullable(pool.getLootItems().get(pool.getRandom().nextInt(pool.getLootItems().size())));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\loot\RandomIndexLootItemFunc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */