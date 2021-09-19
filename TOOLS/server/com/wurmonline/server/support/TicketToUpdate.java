/*    */ package com.wurmonline.server.support;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class TicketToUpdate
/*    */ {
/*    */   public static final byte ARCHIVESTATE = 0;
/*    */   public static final byte TRELLOCARDID = 1;
/*    */   public static final byte TRELLOFEEDBACKCARDID = 2;
/*    */   public static final byte ISDIRTY = 3;
/*    */   private final Ticket ticket;
/*    */   private final byte toUpdate;
/*    */   private byte archiveState;
/*    */   private String trelloCardId;
/*    */   private boolean isDirty;
/*    */   
/*    */   public TicketToUpdate(Ticket aTicket, byte aToUpdate, byte newArchiveState) {
/* 49 */     this.ticket = aTicket;
/* 50 */     this.toUpdate = aToUpdate;
/* 51 */     this.archiveState = newArchiveState;
/*    */   }
/*    */ 
/*    */   
/*    */   public TicketToUpdate(Ticket aTicket, byte aToUpdate, String aTrelloCardId) {
/* 56 */     this.ticket = aTicket;
/* 57 */     this.toUpdate = aToUpdate;
/* 58 */     this.trelloCardId = aTrelloCardId;
/*    */   }
/*    */ 
/*    */   
/*    */   public TicketToUpdate(Ticket aTicket, byte aToUpdate, boolean aIsDirty) {
/* 63 */     this.ticket = aTicket;
/* 64 */     this.toUpdate = aToUpdate;
/* 65 */     this.isDirty = aIsDirty;
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 70 */     switch (this.toUpdate) {
/*    */       
/*    */       case 0:
/* 73 */         this.ticket.setArchiveState(this.archiveState);
/*    */         break;
/*    */       
/*    */       case 1:
/* 77 */         this.ticket.setTrelloCardId(this.trelloCardId);
/*    */         break;
/*    */       
/*    */       case 2:
/* 81 */         this.ticket.setTrelloFeedbackCardId(this.trelloCardId);
/*    */         break;
/*    */       
/*    */       case 3:
/* 85 */         this.ticket.setDirty(this.isDirty);
/* 86 */         this.ticket.dbUpdateIsDirty();
/*    */         break;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\support\TicketToUpdate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */