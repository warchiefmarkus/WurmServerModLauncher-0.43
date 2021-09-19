/*    */ package com.wurmonline.server.loot;
/*    */ 
/*    */ import com.wurmonline.server.creatures.Creature;
/*    */ 
/*    */ 
/*    */ public class DefaultActiveFunc
/*    */   implements ActiveFunc
/*    */ {
/*    */   public boolean active(Creature victim, Creature receiver) {
/* 10 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\loot\DefaultActiveFunc.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */