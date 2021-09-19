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
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum TicketGroup
/*    */ {
/* 27 */   NONE(0, ""), OPEN(-1, "Open tickets"), FORUM(-2, "Forum requests"), WATCH(-3, "Players to watch"), CLOSED(-4, "Closed tickets");
/*    */   
/*    */   private static final TicketGroup[] types;
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final byte id;
/*    */   
/*    */   static {
/* 36 */     types = values();
/*    */   }
/*    */ 
/*    */   
/*    */   TicketGroup(int id, String name) {
/* 41 */     this.id = (byte)id;
/* 42 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte getId() {
/* 47 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 52 */     return this.name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 57 */     return "G" + Math.abs(this.id);
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
/*    */   public static TicketGroup ticketGroupFromId(byte aId) {
/* 69 */     for (int i = 0; i < types.length; i++) {
/*    */       
/* 71 */       if (aId == types[i].getId()) {
/* 72 */         return types[i];
/*    */       }
/*    */     } 
/* 75 */     return NONE;
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\shared\constants\TicketGroup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */