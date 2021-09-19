/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Players;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.deities.Deities;
/*     */ import com.wurmonline.server.deities.Deity;
/*     */ import com.wurmonline.server.players.Player;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public final class SetDeityQuestion
/*     */   extends Question
/*     */ {
/*  39 */   private final List<Player> playlist = new LinkedList<>();
/*  40 */   private final Map<Integer, Integer> deityMap = new ConcurrentHashMap<>();
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
/*     */   public SetDeityQuestion(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  52 */     super(aResponder, aTitle, aQuestion, 26, aTarget);
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
/*  63 */     setAnswer(answers);
/*  64 */     QuestionParser.parseSetDeityQuestion(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*  75 */     StringBuilder buf = new StringBuilder(getBmlHeader());
/*     */     
/*  77 */     buf.append("harray{label{text='Player: '};dropdown{id='wurmid';options='");
/*  78 */     Player[] players = Players.getInstance().getPlayers();
/*     */ 
/*     */     
/*  81 */     Arrays.sort((Object[])players);
/*     */     
/*  83 */     for (int x = 0; x < players.length; x++) {
/*     */       
/*  85 */       if (x > 0)
/*  86 */         buf.append(","); 
/*  87 */       buf.append(players[x].getName());
/*  88 */       this.playlist.add(players[x]);
/*     */     } 
/*     */     
/*  91 */     buf.append("'}}");
/*  92 */     Deity[] deitys = Deities.getDeities();
/*  93 */     int counter = 0;
/*     */     
/*  95 */     buf.append("harray{label{text=\"Deity\"};dropdown{id=\"deityid\";options='None");
/*  96 */     for (Deity d : deitys) {
/*     */       
/*  98 */       counter++;
/*  99 */       this.deityMap.put(Integer.valueOf(counter), Integer.valueOf(d.getNumber()));
/* 100 */       buf.append(",");
/* 101 */       buf.append(d.getName());
/*     */     } 
/* 103 */     buf.append("'}}");
/*     */     
/* 105 */     buf.append("harray{label{text=\"Faith\"};input{maxchars=\"3\";id=\"faith\";text=\"1\"}label{text=\".\"}input{maxchars=\"6\"; id=\"faithdec\"; text=\"000000\"}}");
/*     */ 
/*     */ 
/*     */     
/* 109 */     buf.append("harray{label{text=\"Favor\"};input{maxchars='3';id=\"favor\";text=\"1\"}}");
/* 110 */     buf.append(createAnswerButton2());
/*     */     
/* 112 */     getResponder().getCommunicator().sendBml(300, 300, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */   
/*     */   public final int getDeityNumberFromArrayPos(int arrayPos) {
/* 117 */     if (arrayPos == 0)
/* 118 */       return 0; 
/* 119 */     return ((Integer)this.deityMap.get(Integer.valueOf(arrayPos))).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Player getPlayer(int aPosition) {
/* 129 */     return this.playlist.get(aPosition);
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\SetDeityQuestion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */