/*     */ package com.wurmonline.server.creatures.ai;
/*     */ 
/*     */ import com.wurmonline.server.creatures.Creature;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ 
/*     */ public class CreaturePathFinderNPC
/*     */   extends TimerTask
/*     */ {
/*  37 */   private final Map<Creature, PathTile> pathTargets = new ConcurrentHashMap<>();
/*     */   
/*     */   private boolean keeprunning = true;
/*     */   
/*     */   public static final long SLEEP_TIME = 25L;
/*  42 */   private static final StaticPathFinderNPC pathFinder = new StaticPathFinderNPC();
/*     */   
/*  44 */   private static Logger logger = Logger.getLogger(CreaturePathFinderNPC.class.getName());
/*     */ 
/*     */   
/*     */   public final void addTarget(Creature c, PathTile target) {
/*  48 */     this.pathTargets.put(c, target);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void removeTarget(Creature c) {
/*  53 */     this.pathTargets.remove(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean log = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLog() {
/*  72 */     return log;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void toggleLog() {
/*  77 */     setLog(!isLog());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLog(boolean nlog) {
/*  88 */     log = nlog;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void startRunning() {
/*  93 */     Timer timer = new Timer();
/*  94 */     timer.scheduleAtFixedRate(this, 30000L, 25L);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void shutDown() {
/*  99 */     this.keeprunning = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/* 110 */     if (this.keeprunning) {
/*     */       
/* 112 */       long now = System.currentTimeMillis();
/* 113 */       if (!this.pathTargets.isEmpty()) {
/*     */         
/* 115 */         for (Iterator<Map.Entry<Creature, PathTile>> it = this.pathTargets.entrySet().iterator(); it.hasNext(); ) {
/*     */           
/* 117 */           Map.Entry<Creature, PathTile> entry = it.next();
/* 118 */           Creature creature = entry.getKey();
/* 119 */           PathTile p = entry.getValue();
/*     */           
/*     */           try {
/* 122 */             Path path = creature.findPath(p.getTileX(), p.getTileY(), pathFinder);
/* 123 */             if (path != null)
/*     */             {
/* 125 */               if (p.hasSpecificPos()) {
/*     */                 
/* 127 */                 PathTile lastTile = path.getPathTiles().getLast();
/* 128 */                 lastTile.setSpecificPos(p.getPosX(), p.getPosY());
/*     */               } 
/* 130 */               creature.getStatus().setPath(path);
/* 131 */               creature.receivedPath = true;
/* 132 */               it.remove();
/*     */             }
/*     */           
/* 135 */           } catch (NoPathException np) {
/*     */             
/* 137 */             it.remove();
/* 138 */             creature.setPathing(false, false);
/*     */           } 
/*     */         } 
/* 141 */         if (log && System.currentTimeMillis() - now > 0L) {
/* 142 */           logger.log(Level.INFO, "NPC Finding paths took " + (System.currentTimeMillis() - now) + " ms for " + this.pathTargets
/* 143 */               .size());
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/* 148 */       logger.log(Level.INFO, "Shutting down NPC pathfinder");
/* 149 */       cancel();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\ai\CreaturePathFinderNPC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */