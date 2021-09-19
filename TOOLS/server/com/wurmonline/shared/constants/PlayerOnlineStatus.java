/*    */ package com.wurmonline.shared.constants;
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
/*    */ public enum PlayerOnlineStatus
/*    */ {
/* 24 */   ONLINE(1, "online"),
/* 25 */   OTHER_SERVER(2, "other server"),
/* 26 */   LOST_LINK(3, "lost link"),
/* 27 */   OFFLINE(0, "offline"),
/* 28 */   DELETE_ME(4, ""),
/* 29 */   UNKNOWN(-1, "unknown");
/*    */   
/*    */   private final byte id;
/*    */   private final String name;
/*    */   private static final PlayerOnlineStatus[] types;
/*    */   
/*    */   PlayerOnlineStatus(int aId, String aName) {
/* 36 */     this.id = (byte)aId;
/* 37 */     this.name = aName;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte getId() {
/* 42 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 47 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 54 */     types = values();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static PlayerOnlineStatus playerOnlineStatusFromId(byte aId) {
/* 66 */     for (int i = 0; i < types.length; i++) {
/*    */       
/* 68 */       if (aId == types[i].getId()) {
/* 69 */         return types[i];
/*    */       }
/*    */     } 
/* 72 */     return DELETE_ME;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\shared\constants\PlayerOnlineStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */