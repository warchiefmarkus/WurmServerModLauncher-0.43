/*    */ package com.wurmonline.server.creatures;
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
/*    */ public enum AttackIdentifier
/*    */ {
/* 26 */   STRIKE(0, "strike"),
/* 27 */   BITE(1, "bite"),
/* 28 */   MAUL(2, "maul"),
/* 29 */   CLAW(3, "claw"),
/* 30 */   HEADBUTT(4, "headbutt"),
/* 31 */   KICK(5, "kick");
/*    */   
/*    */   private final int id;
/*    */   
/*    */   private final String animationString;
/*    */   
/*    */   AttackIdentifier(int _id, String animation) {
/* 38 */     this.id = _id;
/* 39 */     this.animationString = animation;
/*    */   }
/*    */ 
/*    */   
/*    */   public final int getId() {
/* 44 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public final String getAnimationString() {
/* 49 */     return this.animationString;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\AttackIdentifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */