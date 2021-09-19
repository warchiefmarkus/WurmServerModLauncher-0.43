/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Features;
/*     */ import com.wurmonline.server.ServerEntry;
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.Spawnpoint;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
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
/*     */ public final class SpawnQuestion
/*     */   extends Question
/*     */ {
/*  43 */   private final List<Spawnpoint> spawnpoints = new LinkedList<>();
/*  44 */   private final Map<Integer, Integer> servers = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SpawnQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  54 */     super(aResponder, aTitle, aQuestion, 34, aTarget);
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
/*     */   public void sendQuestion() {
/*  91 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/*     */     
/*  93 */     Set<Spawnpoint> spawnPoints = ((Player)getResponder()).spawnpoints;
/*  94 */     if (spawnPoints == null || spawnPoints.isEmpty()) {
/*     */       
/*  96 */       ((Player)getResponder()).calculateSpawnPoints();
/*  97 */       spawnPoints = ((Player)getResponder()).spawnpoints;
/*     */     } 
/*  99 */     if (spawnPoints != null && !spawnPoints.isEmpty()) {
/*     */       
/* 101 */       int x = 0;
/* 102 */       buf.append("dropdown{id='spawnpoint';options=\"");
/* 103 */       for (Spawnpoint sp : spawnPoints) {
/*     */         
/* 105 */         if (x > 0)
/* 106 */           buf.append(","); 
/* 107 */         this.spawnpoints.add(sp);
/* 108 */         buf.append(sp.description);
/* 109 */         x++;
/*     */       } 
/* 111 */       buf.append("\"};");
/*     */     }
/*     */     else {
/*     */       
/* 115 */       buf.append("label{text=\"No valid spawn points found. Wait and try again using the /respawn command or send to start at the startpoint.\"};");
/*     */     } 
/*     */     
/* 118 */     if (Servers.localServer.EPIC)
/*     */     {
/*     */       
/* 121 */       if (Servers.localServer.getKingdom() != getResponder().getKingdomId()) {
/*     */         
/* 123 */         buf.append("label{text=\"You may also select to spawn on another Epic server.\"};");
/* 124 */         int x = 0;
/* 125 */         this.servers.put(Integer.valueOf(x), Integer.valueOf(0));
/* 126 */         buf.append("dropdown{id='eserver';options=\"None");
/* 127 */         for (ServerEntry s : Servers.getAllServers()) {
/*     */           
/* 129 */           if (s.EPIC && s.isAvailable(0, getResponder().isPaying()))
/*     */           {
/* 131 */             if (s.getId() != Servers.localServer.id)
/*     */             {
/* 133 */               if (s.getKingdom() == 0 || s.getKingdom() == getResponder().getKingdomId())
/*     */               {
/* 135 */                 if (getResponder().isPaying() || !s.ISPAYMENT) {
/*     */                   
/* 137 */                   x++;
/* 138 */                   buf.append(",");
/* 139 */                   buf.append(s.getName());
/* 140 */                   this.servers.put(Integer.valueOf(x), Integer.valueOf(s.getId()));
/*     */                 } 
/*     */               }
/*     */             }
/*     */           }
/*     */         } 
/* 146 */         buf.append("\"};");
/*     */       } 
/*     */     }
/* 149 */     if (Features.Feature.FREE_ITEMS.isEnabled()) {
/*     */       
/* 151 */       buf.append("text{text=''};text{text=''};");
/* 152 */       buf.append("label{text=\"Do you require a weapon QL 40 or a rope?\"};");
/* 153 */       buf.append("dropdown{id='weapon';options=\"No");
/* 154 */       buf.append(",Long Sword + Shield,Two Handed Sword, Large Axe + Shield, Huge Axe, Medium Maul + Shield, Large Maul, Halberd, Long Spear");
/* 155 */       buf.append("\"};");
/*     */       
/* 157 */       buf.append("text{text=''};text{text=''};");
/* 158 */       buf.append("label{text=\"You may also select to spawn with some armour.\"};");
/* 159 */       buf.append("dropdown{id='armour';options=\"None");
/* 160 */       buf.append(",Chain (QL 40), Leather (QL 60), Plate (QL 20)");
/* 161 */       buf.append("\"};");
/*     */     } 
/* 163 */     buf.append(createAnswerButton2());
/* 164 */     getResponder().getCommunicator().sendBml(300, Servers.localServer.isChallengeServer() ? 500 : 300, true, true, buf
/* 165 */         .toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */   
/*     */   final Map<Integer, Integer> getServerEntries() {
/* 170 */     return this.servers;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/* 176 */     setAnswer(answers);
/* 177 */     QuestionParser.parseSpawnQuestion(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Spawnpoint getSpawnpoint(int aIndex) {
/* 187 */     if (this.spawnpoints.isEmpty())
/* 188 */       return null; 
/* 189 */     return this.spawnpoints.get(aIndex);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\SpawnQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */