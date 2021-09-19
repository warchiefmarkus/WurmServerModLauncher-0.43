/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import com.wurmonline.server.players.PlayerInfoFactory;
/*     */ import com.wurmonline.server.villages.Village;
/*     */ import com.wurmonline.server.villages.Villages;
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
/*     */ public final class TeleportQuestion
/*     */   extends Question
/*     */ {
/*  39 */   private final List<Player> playerlist = new LinkedList<>();
/*  40 */   private final List<Village> villagelist = new LinkedList<>();
/*  41 */   private String filter = "";
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean filterPlayers = false;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean filterVillages = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TeleportQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  55 */     super(aResponder, aTitle, aQuestion, 17, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*  61 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/*     */     
/*  63 */     buf.append("harray{label{text='Tile x'};input{id='data1'; text='-1'}}");
/*  64 */     buf.append("harray{label{text='Tile y'};input{id='data2'; text='-1'}}");
/*  65 */     buf.append("harray{label{text='Surfaced: '};dropdown{id='layer';options='true,false'}}");
/*     */     
/*  67 */     Player[] players = Players.getInstance().getPlayers();
/*     */ 
/*     */     
/*  70 */     Arrays.sort((Object[])players); int x;
/*  71 */     for (x = 0; x < players.length; x++) {
/*  72 */       if (!this.filterPlayers || 
/*  73 */         PlayerInfoFactory.wildCardMatch(players[x].getName().toLowerCase(), this.filter.toLowerCase()))
/*     */       {
/*     */ 
/*     */         
/*  77 */         this.playerlist.add(players[x]); } 
/*     */     } 
/*  79 */     buf.append("text{text=''};");
/*  80 */     buf.append("harray{label{text=\"Filter by: \"};input{maxchars=\"20\";id=\"filtertext\";text=\"" + this.filter + "\"; onenter='filterboth'};label{text=' (Use * as a wildcard)'};}");
/*     */     
/*  82 */     buf.append("harray{label{text='Player:    '}; dropdown{id='wurmid';options='");
/*  83 */     for (x = 0; x < this.playerlist.size(); x++) {
/*     */       
/*  85 */       if (x > 0)
/*  86 */         buf.append(","); 
/*  87 */       buf.append(((Player)this.playerlist.get(x)).getName());
/*     */     } 
/*  89 */     buf.append("'};button{text='Filter'; id='filterplayer'}}");
/*  90 */     buf.append("harray{label{text='Village:   '}; dropdown{id='villid';default='0';options=\"none,");
/*  91 */     Village[] vills = Villages.getVillages();
/*     */     
/*  93 */     Arrays.sort((Object[])vills);
/*  94 */     int lastPerm = 0; int i;
/*  95 */     for (i = 0; i < vills.length; i++) {
/*  96 */       if (!this.filterVillages || 
/*  97 */         PlayerInfoFactory.wildCardMatch(vills[i].getName().toLowerCase(), this.filter.toLowerCase()))
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 102 */         if ((vills[i]).isPermanent) {
/*     */           
/* 104 */           this.villagelist.add(lastPerm, vills[i]);
/* 105 */           lastPerm++;
/*     */         } else {
/*     */           
/* 108 */           this.villagelist.add(vills[i]);
/*     */         }  } 
/* 110 */     }  for (i = 0; i < this.villagelist.size(); i++) {
/*     */       
/* 112 */       if (i > 0)
/* 113 */         buf.append(","); 
/* 114 */       if (((Village)this.villagelist.get(i)).isPermanent)
/* 115 */         buf.append("#"); 
/* 116 */       buf.append(((Village)this.villagelist.get(i)).getName());
/*     */     } 
/* 118 */     buf.append("\"};button{text='Filter'; id='filtervillage'}}");
/* 119 */     buf.append(createAnswerButton2());
/* 120 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {
/* 126 */     setAnswer(answers);
/* 127 */     boolean filterP = false;
/* 128 */     boolean filterV = false;
/* 129 */     String val = getAnswer().getProperty("filterplayer");
/* 130 */     if (val != null && val.equals("true"))
/* 131 */       filterP = true; 
/* 132 */     val = getAnswer().getProperty("filtervillage");
/* 133 */     if (val != null && val.equals("true"))
/* 134 */       filterV = true; 
/* 135 */     val = getAnswer().getProperty("filterboth");
/* 136 */     if (val != null && val.equals("true")) {
/* 137 */       filterV = filterP = true;
/*     */     }
/* 139 */     if (filterP || filterV) {
/*     */       
/* 141 */       val = getAnswer().getProperty("filtertext");
/* 142 */       if (val == null || val.length() == 0)
/* 143 */         val = "*"; 
/* 144 */       TeleportQuestion tq = new TeleportQuestion(getResponder(), this.title, this.question, this.target);
/* 145 */       tq.filter = val;
/* 146 */       tq.filterPlayers = filterP;
/* 147 */       tq.filterVillages = filterV;
/* 148 */       tq.sendQuestion();
/*     */     } else {
/*     */       
/* 151 */       QuestionParser.parseTeleportQuestion(this);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Player getPlayer(int aPosition) {
/* 161 */     return this.playerlist.get(aPosition);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Village getVillage(int aPosition) {
/* 171 */     return this.villagelist.get(aPosition);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\TeleportQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */