/*    */ package com.wurmonline.server.zones;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
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
/*    */ public final class Encounter
/*    */ {
/* 28 */   private final Map<Integer, Integer> types = new HashMap<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addType(int creatureTemplateId, int nums) {
/* 37 */     this.types.put(Integer.valueOf(creatureTemplateId), Integer.valueOf(nums));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<Integer, Integer> getTypes() {
/* 46 */     return this.types;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public final String toString() {
/* 52 */     String toRet = "";
/* 53 */     for (Map.Entry<Integer, Integer> entry : this.types.entrySet())
/*    */     {
/* 55 */       toRet = toRet + "Type " + entry.getKey() + " Numbers=" + entry.getValue() + ", ";
/*    */     }
/* 57 */     return toRet;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\zones\Encounter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */