/*    */ package com.wurmonline.server.items;
/*    */ 
/*    */ import com.wurmonline.server.Items;
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import com.wurmonline.server.zones.VolaTile;
/*    */ import com.wurmonline.server.zones.Zones;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ItemVicinityRequirement
/*    */   extends CreationRequirement
/*    */ {
/*    */   public ItemVicinityRequirement(int aNumber, int aResourceTemplateId, int aNumberNeeded, boolean aConsume, int aDistance) {
/* 38 */     super(aNumber, aResourceTemplateId, aNumberNeeded, aConsume);
/* 39 */     setDistance(aDistance);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean fill(Creature performer, Item creation) {
/* 45 */     boolean toReturn = false;
/* 46 */     VolaTile tile = performer.getCurrentTile();
/* 47 */     if (tile == null)
/* 48 */       return false; 
/* 49 */     VolaTile[] tiles = Zones.getTilesSurrounding(tile.tilex, tile.tiley, performer.isOnSurface(), getDistance());
/* 50 */     if (canBeFilled(tiles))
/*    */     {
/* 52 */       if (willBeConsumed()) {
/*    */         
/* 54 */         int found = 0;
/* 55 */         for (int x = 0; x < tiles.length; x++) {
/*    */           
/* 57 */           Item[] items = tiles[x].getItems();
/* 58 */           for (int i = 0; i < items.length; i++) {
/*    */             
/* 60 */             if (items[i].getTemplateId() == getResourceTemplateId()) {
/*    */               
/* 62 */               found++;
/* 63 */               Items.destroyItem(items[i].getWurmId());
/* 64 */               if (found == getResourceNumber()) {
/* 65 */                 return true;
/*    */               }
/*    */             } 
/*    */           } 
/*    */         } 
/*    */       } else {
/* 71 */         toReturn = true;
/*    */       }  } 
/* 73 */     return toReturn;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canBeFilled(VolaTile[] tiles) {
/* 78 */     int found = 0;
/* 79 */     for (int x = 0; x < tiles.length; x++) {
/*    */       
/* 81 */       Item[] items = tiles[x].getItems();
/* 82 */       for (int i = 0; i < items.length; i++) {
/*    */         
/* 84 */         if (items[i].getTemplateId() == getResourceTemplateId()) {
/*    */           
/* 86 */           found++;
/* 87 */           if (found == getResourceNumber())
/* 88 */             return true; 
/*    */         } 
/*    */       } 
/*    */     } 
/* 92 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\items\ItemVicinityRequirement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */