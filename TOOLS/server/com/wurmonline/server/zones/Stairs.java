/*    */ package com.wurmonline.server.zones;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.ConcurrentHashMap;
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
/*    */ public class Stairs
/*    */ {
/* 32 */   public static final Map<Integer, Set<Integer>> stairTiles = new ConcurrentHashMap<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final void addStair(int volatileId, int floorLevel) {
/* 43 */     Set<Integer> stairSet = stairTiles.get(Integer.valueOf(volatileId));
/* 44 */     if (stairSet == null)
/* 45 */       stairSet = new HashSet<>(); 
/* 46 */     stairSet.add(Integer.valueOf(floorLevel));
/* 47 */     stairTiles.put(Integer.valueOf(volatileId), stairSet);
/*    */   }
/*    */ 
/*    */   
/*    */   public static final boolean hasStair(int volatileId, int floorLevel) {
/* 52 */     Set<Integer> stairSet = stairTiles.get(Integer.valueOf(volatileId));
/* 53 */     if (stairSet == null)
/* 54 */       return false; 
/* 55 */     return stairSet
/* 56 */       .contains(Integer.valueOf(floorLevel));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final void removeStair(int volatileId, int floorLevel) {
/* 61 */     Set<Integer> stairSet = stairTiles.get(Integer.valueOf(volatileId));
/* 62 */     if (stairSet == null)
/*    */       return; 
/* 64 */     stairSet.remove(Integer.valueOf(floorLevel));
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\Stairs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */