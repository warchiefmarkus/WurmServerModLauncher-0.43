/*    */ package com.wurmonline.server.support;
/*    */ 
/*    */ import com.wurmonline.server.Players;
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
/*    */ public final class TicketToSend
/*    */ {
/*    */   private final Ticket ticket;
/*    */   private final TicketAction ticketAction;
/*    */   private final boolean sendActions;
/*    */   
/*    */   public TicketToSend(Ticket aTicket) {
/* 41 */     this.ticket = aTicket;
/* 42 */     this.sendActions = false;
/* 43 */     this.ticketAction = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public TicketToSend(Ticket aTicket, TicketAction action) {
/* 48 */     this.ticket = aTicket;
/* 49 */     this.sendActions = true;
/* 50 */     this.ticketAction = action;
/*    */   }
/*    */ 
/*    */   
/*    */   public TicketToSend(Ticket aTicket, int actionsToSend, TicketAction action) {
/* 55 */     this.ticket = aTicket;
/* 56 */     this.sendActions = (actionsToSend > 0);
/* 57 */     if (actionsToSend == 1) {
/* 58 */       this.ticketAction = action;
/*    */     } else {
/* 60 */       this.ticketAction = null;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void send() {
/* 65 */     if (this.ticket.getArchiveState() == 1) {
/*    */       
/* 67 */       Players.getInstance().removeTicket(this.ticket);
/* 68 */       this.ticket.setArchiveState((byte)2);
/*    */     }
/* 70 */     else if (!this.sendActions) {
/* 71 */       Players.getInstance().sendTicket(this.ticket);
/*    */     } else {
/* 73 */       Players.getInstance().sendTicket(this.ticket, this.ticketAction);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\support\TicketToSend.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */