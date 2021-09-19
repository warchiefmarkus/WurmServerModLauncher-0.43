/*    */ package com.wurmonline.server.utils;
/*    */ 
/*    */ import com.wurmonline.shared.util.StringUtilities;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NameCountList
/*    */ {
/* 33 */   final Map<String, Integer> localMap = new HashMap<>();
/*    */ 
/*    */   
/*    */   public void add(String name) {
/* 37 */     int cnt = 1;
/* 38 */     if (this.localMap.containsKey(name))
/*    */     {
/* 40 */       cnt = ((Integer)this.localMap.get(name)).intValue() + 1;
/*    */     }
/* 42 */     this.localMap.put(name, Integer.valueOf(cnt));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 47 */     return this.localMap.isEmpty();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 53 */     String line = "";
/* 54 */     int count = 0;
/* 55 */     for (Map.Entry<String, Integer> entry : this.localMap.entrySet()) {
/*    */       
/* 57 */       count++;
/* 58 */       if (line.length() > 0)
/* 59 */         if (count == this.localMap.size()) {
/* 60 */           line = line + " and ";
/*    */         } else {
/* 62 */           line = line + ", ";
/* 63 */         }   line = line + StringUtilities.getWordForNumber(((Integer)entry.getValue()).intValue()) + " " + (String)entry.getKey();
/*    */     } 
/* 65 */     return line;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\serve\\utils\NameCountList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */