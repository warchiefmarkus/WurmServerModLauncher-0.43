/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.Servers;
/*     */ import com.wurmonline.server.WurmCalendar;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.kingdom.King;
/*     */ import com.wurmonline.server.kingdom.Kingdom;
/*     */ import com.wurmonline.server.kingdom.Kingdoms;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ public final class KingdomHistory
/*     */   extends Question
/*     */ {
/*     */   public KingdomHistory(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  43 */     super(aResponder, aTitle, aQuestion, 66, aTarget);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties answers) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendQuestion() {
/*  55 */     String lHtml = getBmlHeaderWithScroll();
/*     */     
/*  57 */     StringBuilder buf = new StringBuilder(lHtml);
/*  58 */     Map<Integer, King> kings = King.eras;
/*  59 */     Map<String, LinkedList<King>> counters = new HashMap<>();
/*  60 */     for (King k : kings.values()) {
/*     */       
/*  62 */       LinkedList<King> kinglist = counters.get(k.kingdomName);
/*     */       
/*  64 */       if (kinglist == null)
/*  65 */         kinglist = new LinkedList<>(); 
/*  66 */       kinglist.add(k);
/*  67 */       counters.put(k.kingdomName, kinglist);
/*     */     } 
/*  69 */     for (Iterator<Map.Entry<String, LinkedList<King>>> it = counters.entrySet().iterator(); it.hasNext(); ) {
/*     */       
/*  71 */       Map.Entry<String, LinkedList<King>> entry = it.next();
/*  72 */       addKing(entry.getValue(), entry.getKey(), buf);
/*     */     } 
/*  74 */     if (Servers.localServer.isChallengeServer())
/*     */     {
/*  76 */       for (Kingdom kingdom : Kingdoms.getAllKingdoms()) {
/*     */         
/*  78 */         if (kingdom.existsHere()) {
/*     */           
/*  80 */           buf.append("label{text=\"" + kingdom.getName() + " points:\"};");
/*  81 */           buf.append("label{text=\"" + kingdom.getWinpoints() + "\"};text{text=''};");
/*     */         } 
/*     */       } 
/*     */     }
/*  85 */     buf.append(createAnswerButton3());
/*     */     
/*  87 */     getResponder().getCommunicator().sendBml(500, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addKing(Collection<King> kings, String kingdomName, StringBuilder buf) {
/*  93 */     buf.append("text{type=\"bold\";text=\"History of " + kingdomName + ":\"}text{text=''}");
/*  94 */     buf.append("table{rows='" + (kings
/*  95 */         .size() + 1) + "'; cols='10';label{text='Ruler'};label{text='Capital'};label{text='Start Land'};label{text='End Land'};label{text='Land Difference'};label{text='Levels Killed'};label{text='Levels Lost'};label{text='Levels Appointed'};label{text='Start Date'};label{text='End Date'};");
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
/* 108 */     for (King k : kings) {
/*     */       
/* 110 */       buf.append("label{text=\"" + k.getFullTitle() + "\"};");
/* 111 */       buf.append("label{text=\"" + k.capital + "\"};");
/* 112 */       buf.append("label{text=\"" + String.format("%.2f%%", new Object[] { Float.valueOf(k.startLand) }) + "\"};");
/* 113 */       buf.append("label{text=\"" + String.format("%.2f%%", new Object[] { Float.valueOf(k.currentLand) }) + "\"};");
/* 114 */       buf.append("label{text=\"" + String.format("%.2f%%", new Object[] { Float.valueOf(k.currentLand - k.startLand) }) + "\"};");
/* 115 */       buf.append("label{text=\"" + k.levelskilled + "\"};");
/* 116 */       buf.append("label{text=\"" + k.levelslost + "\"};");
/* 117 */       buf.append("label{text=\"" + k.appointed + "\"};");
/* 118 */       buf.append("label{text=\"" + WurmCalendar.getDateFor(k.startWurmTime) + "\"};");
/* 119 */       if (k.endWurmTime > 0L) {
/* 120 */         buf.append("label{text=\"" + WurmCalendar.getDateFor(k.endWurmTime) + "\"};"); continue;
/*     */       } 
/* 122 */       buf.append("label{text=\"N/A\"};");
/*     */     } 
/*     */     
/* 125 */     buf.append("}");
/* 126 */     buf.append("text{text=\"\"}");
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\KingdomHistory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */