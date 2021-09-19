/*     */ package com.wurmonline.server.behaviours;
/*     */ 
/*     */ import com.wurmonline.server.MiscConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.items.Item;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.questions.TicketUpdateQuestion;
/*     */ import com.wurmonline.server.support.Ticket;
/*     */ import com.wurmonline.server.support.Tickets;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TicketBehaviour
/*     */   extends Behaviour
/*     */   implements MiscConstants
/*     */ {
/*     */   TicketBehaviour() {
/*  43 */     super((short)50);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, Item object, int ticketId) {
/*  54 */     return getBehavioursFor(performer, ticketId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ActionEntry> getBehavioursFor(Creature performer, int ticketId) {
/*  65 */     List<ActionEntry> toReturn = new LinkedList<>();
/*     */     
/*  67 */     Ticket ticket = Tickets.getTicket(ticketId);
/*  68 */     if (ticket == null)
/*  69 */       return toReturn; 
/*  70 */     Player player = (Player)performer;
/*  71 */     if (player.mayHearDevTalk()) {
/*     */       
/*  73 */       if (ticket.isOpen()) {
/*     */         
/*  75 */         toReturn.add(new ActionEntry((short)-4, "Forward", "forward", emptyIntArr));
/*  76 */         toReturn.add(Actions.actionEntrys[596]);
/*  77 */         toReturn.add(Actions.actionEntrys[591]);
/*  78 */         toReturn.add(Actions.actionEntrys[592]);
/*  79 */         toReturn.add(Actions.actionEntrys[593]);
/*  80 */         if (ticket.getResponderName().equalsIgnoreCase(performer.getName()))
/*  81 */           toReturn.add(Actions.actionEntrys[594]); 
/*  82 */         if (ticket.getCategoryCode() != 11)
/*  83 */           toReturn.add(Actions.actionEntrys[589]); 
/*  84 */         toReturn.add(Actions.actionEntrys[590]);
/*  85 */         if (!ticket.getResponderName().equalsIgnoreCase(performer.getName())) {
/*  86 */           toReturn.add(Actions.actionEntrys[595]);
/*     */         }
/*  88 */       } else if (ticket.hasFeedback()) {
/*  89 */         toReturn.add(Actions.actionEntrys[597]);
/*  90 */       } else if (ticket.getStateCode() == 2) {
/*  91 */         toReturn.add(Actions.actionEntrys[599]);
/*     */       } 
/*  93 */       toReturn.add(Actions.actionEntrys[587]);
/*     */     }
/*  95 */     else if (ticket.getPlayerId() == player.getWurmId()) {
/*     */ 
/*     */       
/*  98 */       if (ticket.isOpen())
/*     */       {
/* 100 */         if (player.mayHearMgmtTalk() && ticket.getLevelCode() == 1) {
/*     */           
/* 102 */           toReturn.add(new ActionEntry((short)-1, "Forward", "forward", emptyIntArr));
/* 103 */           toReturn.add(Actions.actionEntrys[591]);
/*     */         } 
/* 105 */         toReturn.add(Actions.actionEntrys[587]);
/* 106 */         toReturn.add(Actions.actionEntrys[588]);
/*     */       }
/*     */       else
/*     */       {
/* 110 */         toReturn.add(new ActionEntry((short)587, "View", "viewing", emptyIntArr));
/* 111 */         toReturn.add(Actions.actionEntrys[597]);
/*     */       }
/*     */     
/* 114 */     } else if (player.mayHearMgmtTalk()) {
/*     */       
/* 116 */       if (ticket.isOpen() && player.mayMute()) {
/*     */         
/* 118 */         toReturn.add(new ActionEntry((short)-2, "Forward", "forward", emptyIntArr));
/* 119 */         toReturn.add(Actions.actionEntrys[596]);
/* 120 */         toReturn.add(Actions.actionEntrys[591]);
/* 121 */         if (ticket.getResponderName().equalsIgnoreCase(performer.getName()))
/* 122 */           toReturn.add(Actions.actionEntrys[594]); 
/* 123 */         toReturn.add(Actions.actionEntrys[589]);
/* 124 */         toReturn.add(Actions.actionEntrys[590]);
/*     */       } 
/* 126 */       toReturn.add(Actions.actionEntrys[587]);
/*     */     } 
/*     */     
/* 129 */     return toReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, int ticketId, short action, float counter) {
/* 140 */     Ticket ticket = Tickets.getTicket(ticketId);
/* 141 */     Player player = (Player)performer;
/* 142 */     if (player.mayHearDevTalk()) {
/*     */       
/* 144 */       if (ticket.isOpen()) {
/*     */         
/* 146 */         if (action == 596) {
/* 147 */           updateTicket(performer, ticketId, action);
/* 148 */         } else if (action == 591) {
/* 149 */           updateTicket(performer, ticketId, action);
/* 150 */         } else if (action == 592) {
/* 151 */           updateTicket(performer, ticketId, action);
/* 152 */         } else if (action == 593) {
/* 153 */           updateTicket(performer, ticketId, action);
/* 154 */         } else if (ticket.getResponderName().equalsIgnoreCase(performer.getName()) && action == 594) {
/*     */           
/* 156 */           updateTicket(performer, ticketId, action);
/* 157 */         } else if (action == 589) {
/*     */           
/* 159 */           player.respondGMTab(ticket.getPlayerName(), String.valueOf(ticket.getTicketId()));
/* 160 */           if (performer.getPower() >= 2) {
/* 161 */             ticket.addNewTicketAction((byte)3, performer.getName(), "GM " + performer.getName() + " responded.", (byte)0);
/*     */           } else {
/* 163 */             ticket.addNewTicketAction((byte)2, performer.getName(), "CM " + performer.getName() + " responded.", (byte)0);
/*     */           } 
/* 165 */         } else if (action == 590) {
/* 166 */           updateTicket(performer, ticketId, action);
/* 167 */         } else if (action == 595) {
/* 168 */           ticket.addNewTicketAction((byte)11, performer.getName(), performer.getName() + " took ticket.", (byte)1);
/*     */         } 
/* 170 */       } else if (action == 597) {
/* 171 */         updateTicket(performer, ticketId, action);
/* 172 */       } else if (action == 599 && ticket.getStateCode() == 2) {
/* 173 */         updateTicket(performer, ticketId, action);
/*     */       } 
/* 175 */       if (action == 587) {
/* 176 */         updateTicket(performer, ticketId, action);
/*     */       }
/* 178 */     } else if (ticket.getPlayerId() == player.getWurmId()) {
/*     */ 
/*     */       
/* 181 */       if (player.mayHearMgmtTalk() && ticket.getLevelCode() == 1) {
/* 182 */         updateTicket(performer, ticketId, action);
/* 183 */       } else if (ticket.isOpen() && action == 588) {
/* 184 */         updateTicket(performer, ticketId, action);
/* 185 */       } else if (action == 587) {
/* 186 */         updateTicket(performer, ticketId, action);
/* 187 */       } else if (action == 597) {
/* 188 */         updateTicket(performer, ticketId, action);
/*     */       } 
/* 190 */     } else if (player.mayHearMgmtTalk()) {
/*     */       
/* 192 */       if (ticket.isOpen())
/*     */       {
/* 194 */         if (action == 596) {
/* 195 */           updateTicket(performer, ticketId, action);
/* 196 */         } else if (action == 591) {
/* 197 */           updateTicket(performer, ticketId, action);
/* 198 */         } else if (ticket.getResponderName().equalsIgnoreCase(performer.getName()) && action == 594) {
/*     */           
/* 200 */           updateTicket(performer, ticketId, action);
/* 201 */         } else if (action == 589) {
/*     */ 
/*     */           
/* 204 */           player.respondGMTab(ticket.getPlayerName(), String.valueOf(ticket.getTicketId()));
/* 205 */           ticket.addNewTicketAction((byte)2, performer.getName(), "CM " + performer.getName() + " responded.", (byte)0);
/*     */         }
/* 207 */         else if (action == 590) {
/* 208 */           updateTicket(performer, ticketId, action);
/*     */         }  } 
/* 210 */       if (action == 587)
/* 211 */         updateTicket(performer, ticketId, action); 
/*     */     } 
/* 213 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean action(Action act, Creature performer, Item source, int ticketId, short action, float counter) {
/* 225 */     return action(act, performer, ticketId, action, counter);
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateTicket(Creature performer, int ticketId, short action) {
/* 230 */     TicketUpdateQuestion tuq = new TicketUpdateQuestion(performer, ticketId, action);
/* 231 */     tuq.sendQuestion();
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\behaviours\TicketBehaviour.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */