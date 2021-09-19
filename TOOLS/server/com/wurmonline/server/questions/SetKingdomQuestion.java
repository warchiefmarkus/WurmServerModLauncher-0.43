/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.kingdom.Kingdom;
/*     */ import com.wurmonline.server.kingdom.Kingdoms;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ public final class SetKingdomQuestion
/*     */   extends Question
/*     */ {
/*  39 */   private final List<Player> playerlist = new LinkedList<>();
/*  40 */   private final LinkedList<Kingdom> availKingdoms = new LinkedList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SetKingdomQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  50 */     super(aResponder, aTitle, aQuestion, 37, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/*  61 */     setAnswer(answers);
/*  62 */     QuestionParser.parseSetKingdomQuestion(this);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/* 101 */     StringBuilder buf = new StringBuilder();
/* 102 */     buf.append(getBmlHeader());
/*     */     
/* 104 */     if (getResponder().getPower() <= 0) {
/*     */       
/* 106 */       if (Kingdoms.isCustomKingdom(getResponder().getKingdomId())) {
/*     */         
/* 108 */         if (getResponder().mayChangeKingdom(null) || Servers.isThisATestServer())
/*     */         {
/* 110 */           byte targetKingdom = getResponder().getKingdomTemplateId();
/* 111 */           if (Servers.isThisAChaosServer() && targetKingdom != 3)
/*     */           {
/* 113 */             targetKingdom = 4;
/*     */           }
/* 115 */           buf.append("text{text=\"You may select to leave this kingdom. Make sure to do this in a safe place.\"}text{text=\"\"}");
/* 116 */           buf.append("text{type=\"italic\";text=\"Do you want to leave " + 
/* 117 */               Kingdoms.getNameFor(getResponder().getKingdomId()) + " for " + 
/* 118 */               Kingdoms.getNameFor(targetKingdom) + "?\"}");
/*     */           
/* 120 */           buf.append("radio{ group='kingd'; id='true';text='Yes'}");
/* 121 */           buf.append("radio{ group='kingd'; id='false';text='No';selected='true'}");
/*     */         }
/*     */         else
/*     */         {
/* 125 */           buf.append("text{text=\"You may not select to leave this kingdom right now. Maybe you are the mayor of a settlement, or converted too recently?\"}text{text=\"\"}");
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 130 */         buf.append("text{text=\"You may not leave this kingdom.\"}text{text=\"\"}");
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 135 */       buf.append("harray{label{text='Player: '};dropdown{id='wurmid';options='");
/* 136 */       Player[] players = Players.getInstance().getPlayers();
/*     */ 
/*     */       
/* 139 */       Arrays.sort((Object[])players);
/*     */       
/* 141 */       for (int x = 0; x < players.length; x++) {
/*     */         
/* 143 */         if (x > 0)
/* 144 */           buf.append(","); 
/* 145 */         this.playerlist.add(players[x]);
/* 146 */         buf.append(players[x].getName());
/*     */       } 
/*     */       
/* 149 */       buf.append("'}}");
/* 150 */       buf.append("harray{label{text='Kingdom: '};dropdown{id='kingdomid';options=\"None");
/* 151 */       Kingdom[] kingdoms = Kingdoms.getAllKingdoms();
/* 152 */       for (int i = 0; i < kingdoms.length; i++) {
/*     */         
/* 154 */         if (kingdoms[i].getId() != 0) {
/*     */           
/* 156 */           buf.append(",");
/* 157 */           this.availKingdoms.add(kingdoms[i]);
/* 158 */           buf.append(kingdoms[i].getName());
/*     */         } 
/*     */       } 
/* 161 */       buf.append("\"}}");
/*     */     } 
/* 163 */     buf.append(createAnswerButton2());
/*     */     
/* 165 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Player getPlayer(int aPosition) {
/* 175 */     return this.playerlist.get(aPosition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<Kingdom> getAvailKingdoms() {
/* 185 */     return this.availKingdoms;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\SetKingdomQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */