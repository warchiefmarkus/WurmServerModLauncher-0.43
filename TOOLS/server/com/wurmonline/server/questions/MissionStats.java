/*     */ package com.wurmonline.server.questions;
/*     */ 
/*     */ import com.wurmonline.server.TimeConstants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import com.wurmonline.server.tutorial.Mission;
/*     */ import com.wurmonline.server.tutorial.MissionPerformed;
/*     */ import com.wurmonline.server.tutorial.MissionPerformer;
/*     */ import com.wurmonline.server.tutorial.Missions;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ public class MissionStats
/*     */   extends Question
/*     */   implements TimeConstants
/*     */ {
/*  37 */   private static final Logger logger = Logger.getLogger(MissionStats.class.getName());
/*     */   private final int targetMission;
/*  39 */   private MissionManager root = null;
/*  40 */   private final Map<Float, Integer> perfstats = new HashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String red = "color=\"255,127,127\"";
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String green = "color=\"127,255,127\"";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MissionStats(Creature aResponder, String aTitle, String aQuestion, long aTarget) {
/*  54 */     super(aResponder, aTitle, aQuestion, 93, aTarget);
/*  55 */     this.targetMission = (int)aTarget;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void answer(Properties aAnswers) {
/*  66 */     setAnswer(aAnswers);
/*  67 */     boolean update = getBooleanProp("update");
/*  68 */     if (update) {
/*     */       
/*  70 */       MissionStats ms = new MissionStats(getResponder(), this.title, this.question, this.targetMission);
/*  71 */       ms.setRoot(this.root);
/*  72 */       ms.sendQuestion();
/*     */       return;
/*     */     } 
/*  75 */     if (this.root != null)
/*     */     {
/*  77 */       this.root.reshow();
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
/*     */   public void sendQuestion() {
/*     */     try {
/*  91 */       Mission m = Missions.getMissionWithId(this.targetMission);
/*     */       
/*  93 */       MissionPerformer[] mps = MissionPerformed.getAllPerformers();
/*  94 */       StringBuilder buf = new StringBuilder(getBmlHeader());
/*  95 */       buf.append("text{text=\"\"}");
/*  96 */       this.perfstats.clear();
/*  97 */       float total = 0.0F; int x;
/*  98 */       for (x = 0; x < mps.length; x++) {
/*     */         
/* 100 */         MissionPerformed mp = mps[x].getMission(this.targetMission);
/* 101 */         if (mp != null) {
/*     */           
/* 103 */           float reached = mp.getState();
/* 104 */           Integer numbers = this.perfstats.get(Float.valueOf(reached));
/* 105 */           if (numbers == null) {
/*     */             
/* 107 */             numbers = Integer.valueOf(1);
/* 108 */             this.perfstats.put(Float.valueOf(reached), numbers);
/*     */           }
/*     */           else {
/*     */             
/* 112 */             numbers = Integer.valueOf(numbers.intValue() + 1);
/* 113 */             this.perfstats.put(Float.valueOf(reached), numbers);
/*     */           } 
/* 115 */           total++;
/*     */         } 
/*     */       } 
/* 118 */       buf.append("text{type=\"bold\";text=\"Total statistics for mission " + m.getName() + ":\"}");
/* 119 */       showStats(buf, total);
/* 120 */       this.perfstats.clear();
/* 121 */       total = 0.0F;
/* 122 */       for (x = 0; x < mps.length; x++) {
/*     */         
/* 124 */         MissionPerformed mp = mps[x].getMission(this.targetMission);
/* 125 */         if (mp != null)
/*     */         {
/* 127 */           if (System.currentTimeMillis() - mp.getStartTimeMillis() < 86400000L) {
/*     */             
/* 129 */             float reached = mp.getState();
/* 130 */             Integer numbers = this.perfstats.get(Float.valueOf(reached));
/* 131 */             if (numbers == null) {
/*     */               
/* 133 */               numbers = Integer.valueOf(1);
/* 134 */               this.perfstats.put(Float.valueOf(reached), numbers);
/*     */             }
/*     */             else {
/*     */               
/* 138 */               numbers = Integer.valueOf(numbers.intValue() + 1);
/* 139 */               this.perfstats.put(Float.valueOf(reached), numbers);
/*     */             } 
/* 141 */             total++;
/*     */           } 
/*     */         }
/*     */       } 
/* 145 */       buf.append("text{type=\"bold\";text=\"Statistics for mission " + m.getName() + " started within last 24 hours:\"}");
/* 146 */       showStats(buf, total);
/* 147 */       this.perfstats.clear();
/* 148 */       total = 0.0F;
/* 149 */       for (x = 0; x < mps.length; x++) {
/*     */         
/* 151 */         MissionPerformed mp = mps[x].getMission(this.targetMission);
/* 152 */         if (mp != null)
/*     */         {
/* 154 */           if (System.currentTimeMillis() - mp.getStartTimeMillis() < 259200000L) {
/*     */             
/* 156 */             float reached = mp.getState();
/* 157 */             Integer numbers = this.perfstats.get(Float.valueOf(reached));
/* 158 */             if (numbers == null) {
/*     */               
/* 160 */               numbers = Integer.valueOf(1);
/* 161 */               this.perfstats.put(Float.valueOf(reached), numbers);
/*     */             }
/*     */             else {
/*     */               
/* 165 */               numbers = Integer.valueOf(numbers.intValue() + 1);
/* 166 */               this.perfstats.put(Float.valueOf(reached), numbers);
/*     */             } 
/* 168 */             total++;
/*     */           } 
/*     */         }
/*     */       } 
/* 172 */       buf.append("text{type=\"bold\";text=\"Statistics for mission " + m.getName() + " started within last three days:\"}");
/* 173 */       showStats(buf, total);
/* 174 */       buf.append("harray{button{text=\"Refresh Statistics\";id=\"update\"};label{text=\"  \"};button{text=\"Back to mission list\";id=\"back\"};}");
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 179 */       buf.append("}};null;null;}");
/* 180 */       getResponder().getCommunicator().sendBml(400, 400, true, true, buf.toString(), 200, 200, 200, this.title);
/*     */     }
/* 182 */     catch (Exception ex) {
/*     */       
/* 184 */       if (logger.isLoggable(Level.FINER))
/*     */       {
/* 186 */         logger.finer("Problem sending a question about target mission: " + this.targetMission);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void showStats(StringBuilder buf, float total) {
/* 193 */     buf.append("table{rows=\"1\"; cols=\"3\";label{text=\"Percent complete\"};label{text=\"People reached\"};label{text=\"Percent of total\"}");
/*     */ 
/*     */ 
/*     */     
/* 197 */     Float[] farr = (Float[])this.perfstats.keySet().toArray((Object[])new Float[this.perfstats.size()]);
/* 198 */     Arrays.sort((Object[])farr);
/* 199 */     for (Float f : farr) {
/*     */       
/* 201 */       String perc = f + "";
/* 202 */       String colour = "";
/* 203 */       if (f.floatValue() == -1.0D) {
/*     */         
/* 205 */         perc = "Failed (-1.0)";
/* 206 */         colour = "color=\"255,127,127\"";
/*     */       }
/* 208 */       else if (f.floatValue() == 100.0D) {
/*     */         
/* 210 */         perc = "Completed (100.0)";
/* 211 */         colour = "color=\"127,255,127\"";
/*     */       } 
/* 213 */       buf.append("label{" + colour + "text=\"" + perc + "\"};");
/* 214 */       buf.append("label{text=\"" + this.perfstats.get(f) + "\"};");
/* 215 */       buf.append("label{text=\"" + (((Integer)this.perfstats.get(f)).intValue() / total * 100.0F) + "\"};");
/*     */     } 
/* 217 */     buf.append("}");
/* 218 */     if (farr.length == 0)
/* 219 */       buf.append("text{text=\"none\"}"); 
/* 220 */     buf.append("text{text=\"\"}");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void setRoot(MissionManager aRoot) {
/* 230 */     this.root = aRoot;
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\questions\MissionStats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */