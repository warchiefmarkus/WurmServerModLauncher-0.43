/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.HistoryManager;
/*     */ import com.wurmonline.server.Server;
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.kingdom.Appointments;
/*     */ import com.wurmonline.server.kingdom.King;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import java.util.Properties;
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
/*     */ public final class AreaHistoryQuestion
/*     */   extends Question
/*     */   implements TimeConstants
/*     */ {
/*     */   private static final long waittime = 21600000L;
/*     */   
/*     */   public AreaHistoryQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  39 */     super(aResponder, aTitle, aQuestion, 41, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  45 */     String newevent = answers.getProperty("newevent");
/*  46 */     if (newevent != null && newevent.length() > 0) {
/*     */       
/*  48 */       Appointments app = King.getCurrentAppointments(getResponder().getKingdomId());
/*  49 */       if (app != null)
/*     */       {
/*  51 */         if (app.getOfficialForId(1510) == getResponder().getWurmId())
/*     */         {
/*  53 */           if (System.currentTimeMillis() - 21600000L < (((Player)getResponder()).getSaveFile()).lastCreatedHistoryEvent) {
/*     */             
/*  55 */             getResponder().getCommunicator().sendNormalServerMessage("You need to wait " + 
/*     */                 
/*  57 */                 Server.getTimeFor(21600000L + 
/*  58 */                   (((Player)getResponder()).getSaveFile()).lastCreatedHistoryEvent - 
/*  59 */                   System.currentTimeMillis()) + " before writing history again.");
/*     */           
/*     */           }
/*     */           else {
/*     */             
/*  64 */             newevent = newevent.replace("\"", "'");
/*  65 */             newevent = newevent.replace("\\", "");
/*  66 */             newevent = newevent.replace("/", "");
/*  67 */             newevent = newevent.replace(";", "");
/*  68 */             newevent = newevent.replace("#", "");
/*     */             
/*  70 */             getResponder().getCommunicator().sendNormalServerMessage("You write some history.");
/*  71 */             HistoryManager.addHistory(getResponder().getName() + " note:", newevent);
/*  72 */             (((Player)getResponder()).getSaveFile()).lastCreatedHistoryEvent = System.currentTimeMillis();
/*     */           } 
/*     */         }
/*     */       }
/*     */     } 
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
/*     */   public void sendQuestion() {
/* 108 */     StringBuilder buf = new StringBuilder();
/* 109 */     buf.append(getBmlHeader());
/* 110 */     buf.append("header{text='Area Events:'}text{text=''}");
/* 111 */     Appointments app = King.getCurrentAppointments(getResponder().getKingdomId());
/* 112 */     if (app != null)
/*     */     {
/* 114 */       if (app.getOfficialForId(1510) == getResponder().getWurmId()) {
/*     */         
/* 116 */         buf.append("text{text='Your office allows you to record historic events:'}");
/* 117 */         if (System.currentTimeMillis() - 21600000L < (((Player)getResponder()).getSaveFile()).lastCreatedHistoryEvent) {
/*     */           
/* 119 */           buf.append("text{text='You need to wait " + 
/* 120 */               Server.getTimeFor(21600000L + (((Player)getResponder()).getSaveFile()).lastCreatedHistoryEvent - 
/* 121 */                 System.currentTimeMillis()) + " before writing history again.'}");
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 126 */           buf.append("input{id='newevent';maxchars='200';text=''}");
/* 127 */           buf.append("text{type=\"italic\";text=\"Please note that event texts are governed by game conduct rules so make sure they are not inappropriate.\"}");
/*     */         } 
/*     */       } 
/*     */     }
/* 131 */     String[] list = HistoryManager.getHistory(200);
/* 132 */     if (list.length > 0) {
/*     */       
/* 134 */       for (int x = 0; x < list.length; x++) {
/* 135 */         buf.append("text{text=\"" + list[x] + "\"}");
/*     */       }
/*     */     } else {
/* 138 */       buf.append("text{text='No events recorded.'}");
/*     */     } 
/* 140 */     buf.append(createAnswerButton2());
/* 141 */     getResponder().getCommunicator().sendBml(400, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\AreaHistoryQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */