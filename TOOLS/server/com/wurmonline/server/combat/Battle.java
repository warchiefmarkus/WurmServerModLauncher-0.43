/*     */ package com.wurmonline.server.combat;
/*     */ 
/*     */ import com.wurmonline.server.Constants;
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Battle
/*     */ {
/*  42 */   private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/*  43 */   private final SimpleDateFormat filedf = new SimpleDateFormat("yyyy-MM-ddHHmmss");
/*     */ 
/*     */   
/*     */   private final Set<Creature> creatures;
/*     */ 
/*     */   
/*     */   private List<Creature> casualties;
/*     */ 
/*     */   
/*     */   private final List<BattleEvent> events;
/*     */ 
/*     */   
/*     */   private final long startTime;
/*     */ 
/*     */   
/*     */   private long endTime;
/*     */ 
/*     */   
/*     */   private final String name;
/*     */   
/*  63 */   private static final Logger logger = Logger.getLogger(Battle.class.getName());
/*     */   
/*     */   private static final String header = "<HTML> <HEAD><TITLE>Wurm battle log</TITLE></HEAD><BODY><BR><BR><B>";
/*     */   private static final String footer = "</BODY></HTML>";
/*     */   
/*     */   Battle(Creature attacker, Creature defender) {
/*  69 */     this.creatures = new HashSet<>();
/*  70 */     this.creatures.add(attacker);
/*  71 */     this.creatures.add(defender);
/*  72 */     this.startTime = System.currentTimeMillis();
/*  73 */     this.endTime = System.currentTimeMillis();
/*     */ 
/*     */ 
/*     */     
/*  77 */     attacker.setBattle(this);
/*  78 */     defender.setBattle(this);
/*  79 */     this.events = new LinkedList<>();
/*  80 */     this.name = "Battle_" + attacker.getName() + "_vs_" + defender.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   boolean containsCreature(Creature creature) {
/*  85 */     return this.creatures.contains(creature);
/*     */   }
/*     */ 
/*     */   
/*     */   void addCreature(Creature creature) {
/*  90 */     if (!this.creatures.contains(creature)) {
/*     */       
/*  92 */       this.creatures.add(creature);
/*  93 */       this.events.add(new BattleEvent((short)-1, creature.getName()));
/*  94 */       creature.setBattle(this);
/*     */     } 
/*  96 */     touch();
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeCreature(Creature creature) {
/* 101 */     this.creatures.remove(creature);
/* 102 */     creature.setBattle(null);
/* 103 */     this.events.add(new BattleEvent((short)-2, creature.getName()));
/* 104 */     touch();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void clearCreatures() {
/* 114 */     this.creatures.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCasualty(Creature dead) {
/* 119 */     if (this.casualties == null)
/* 120 */       this.casualties = new LinkedList<>(); 
/* 121 */     this.casualties.add(dead);
/* 122 */     this.events.add(new BattleEvent((short)-3, dead.getName()));
/* 123 */     this.creatures.remove(dead);
/* 124 */     dead.setBattle(null);
/* 125 */     touch();
/*     */   }
/*     */ 
/*     */   
/*     */   void touch() {
/* 130 */     this.endTime = System.currentTimeMillis();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addCasualty(Creature killer, Creature dead) {
/* 135 */     if (this.casualties == null)
/* 136 */       this.casualties = new LinkedList<>(); 
/* 137 */     this.casualties.add(dead);
/* 138 */     this.events.add(new BattleEvent((short)-3, dead.getName(), killer.getName()));
/* 139 */     this.creatures.remove(dead);
/* 140 */     dead.setBattle(null);
/* 141 */     touch();
/*     */   }
/*     */ 
/*     */   
/*     */   public void addEvent(BattleEvent event) {
/* 146 */     this.events.add(event);
/* 147 */     touch();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Creature[] getCreatures() {
/* 156 */     return this.creatures.<Creature>toArray(new Creature[this.creatures.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getStartTime() {
/* 165 */     return this.startTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getEndTime() {
/* 174 */     return this.endTime;
/*     */   }
/*     */ 
/*     */   
/*     */   void save() {
/* 179 */     if (this.casualties != null && this.casualties.size() > 0) {
/*     */       
/* 181 */       Writer output = null;
/*     */ 
/*     */       
/*     */       try {
/* 185 */         Date d = new Date(this.startTime);
/* 186 */         String dir = Constants.webPath;
/* 187 */         if (!dir.endsWith(File.separator))
/* 188 */           dir = dir + File.separator; 
/* 189 */         File aFile = new File(dir + this.name + "_" + this.filedf.format(d) + ".html");
/*     */ 
/*     */ 
/*     */         
/* 193 */         output = new BufferedWriter(new FileWriter(aFile));
/*     */ 
/*     */ 
/*     */         
/* 197 */         String start = this.name + "</B><BR><I>started at " + this.df.format(d) + " and ended on " + this.df.format(new Date(this.endTime)) + "</I><BR><BR>";
/*     */ 
/*     */         
/*     */         try {
/* 201 */           output.write("<HTML> <HEAD><TITLE>Wurm battle log</TITLE></HEAD><BODY><BR><BR><B>");
/* 202 */           output.write(start);
/*     */         }
/* 204 */         catch (IOException iox) {
/*     */           
/* 206 */           logger.log(Level.WARNING, iox.getMessage(), iox);
/*     */         } 
/* 208 */         for (BattleEvent lBattleEvent : this.events) {
/*     */           
/* 210 */           String ts = lBattleEvent.toString();
/*     */ 
/*     */           
/*     */           try {
/* 214 */             output.write(ts);
/*     */           }
/* 216 */           catch (IOException iox) {
/*     */             
/* 218 */             logger.log(Level.WARNING, iox.getMessage(), iox);
/*     */           } 
/*     */         } 
/* 221 */         output.write("</BODY></HTML>");
/*     */       }
/* 223 */       catch (IOException iox) {
/*     */         
/* 225 */         logger.log(Level.WARNING, "Failed to close " + this.name, iox);
/*     */       } finally {
/*     */ 
/*     */         
/*     */         try {
/*     */ 
/*     */           
/* 232 */           if (output != null) {
/* 233 */             output.close();
/*     */           }
/* 235 */         } catch (IOException iOException) {}
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 241 */     for (Creature cret : this.creatures)
/*     */     {
/* 243 */       cret.setBattle(null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\combat\Battle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */