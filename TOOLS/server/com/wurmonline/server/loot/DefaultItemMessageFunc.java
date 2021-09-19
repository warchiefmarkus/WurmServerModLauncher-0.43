/*    */ package com.wurmonline.server.loot;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.items.Item;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultItemMessageFunc
/*    */   implements ItemMessageFunc
/*    */ {
/*    */   public void message(Creature victim, Creature receiver, Item item) {
/* 12 */     receiver.getCommunicator().sendSafeServerMessage("You loot " + item.getNameWithGenus() + " from the corpse.", (byte)2);
/* 13 */     if (receiver.getCurrentTile() != null)
/* 14 */       receiver.getCurrentTile().broadCastAction(receiver.getName() + " picks up " + item.getNameWithGenus() + " from the corpse.", receiver, false); 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\loot\DefaultItemMessageFunc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */