/*    */ package com.wurmonline.server.loot;
/*    */ import com.wurmonline.server.Server;
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class LootTable {
/* 12 */   private List<LootPool> lootPools = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<LootPool> getLootPools() {
/* 20 */     return this.lootPools;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootTable setLootPools(List<LootPool> lootPools) {
/* 25 */     this.lootPools = lootPools;
/* 26 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootTable setLootPools(LootPool[] pools) {
/* 31 */     return setLootPools(Arrays.asList(pools));
/*    */   }
/*    */ 
/*    */   
/*    */   public LootTable addLootPools(List<LootPool> lootPools) {
/* 36 */     this.lootPools.addAll(lootPools);
/* 37 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootTable addLootPools(LootPool[] pools) {
/* 42 */     return addLootPools(Arrays.asList(pools));
/*    */   }
/*    */ 
/*    */   
/*    */   public void awardAll(Creature victim, HashSet<Creature> receivers) {
/* 47 */     this.lootPools.forEach(pool -> pool.awardLootItem(victim, receivers));
/*    */   }
/*    */ 
/*    */   
/*    */   public void awardOne(Creature victim, Creature receiver) {
/* 52 */     this.lootPools.forEach(pool -> pool.awardLootItem(victim, receiver));
/*    */   }
/*    */ 
/*    */   
/*    */   public static byte rollForRarity(Creature receiver) {
/* 57 */     Map<Integer, Byte> chances = new HashMap<>();
/* 58 */     chances.put(Integer.valueOf(10000), Byte.valueOf((byte)3));
/* 59 */     chances.put(Integer.valueOf(1000), Byte.valueOf((byte)2));
/* 60 */     chances.put(Integer.valueOf(100), Byte.valueOf((byte)1));
/* 61 */     List<Integer> keys = new ArrayList<>(chances.keySet());
/* 62 */     keys.sort(Comparator.reverseOrder());
/* 63 */     for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext(); ) { int c = ((Integer)iterator.next()).intValue();
/*    */       
/* 65 */       if (Server.rand.nextInt(c) == 0) {
/*    */         
/* 67 */         receiver.getCommunicator().sendRarityEvent();
/* 68 */         return ((Byte)chances.get(Integer.valueOf(c))).byteValue();
/*    */       }  }
/*    */     
/* 71 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\loot\LootTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */