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
/*     */ public class CreaturePathFinderAgg
/*     */   extends TimerTask
/*     */ {
/*  36 */   private final Map<Creature, PathTile> pathTargets = new ConcurrentHashMap<>();
/*     */   
/*     */   private boolean keeprunning = true;
/*     */   
/*     */   public static final long SLEEP_TIME = 25L;
/*  41 */   private static final StaticPathFinderAgg pathFinder = new StaticPathFinderAgg();
/*     */   
/*  43 */   private static Logger logger = Logger.getLogger(CreaturePathFinderAgg.class.getName());
/*     */ 
/*     */   
/*     */   public final void addTarget(Creature c, PathTile target) {
/*  47 */     this.pathTargets.put(c, target);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void removeTarget(Creature c) {
/*  52 */     this.pathTargets.remove(c);
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
/*  71 */     return log;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void toggleLog() {
/*  76 */     setLog(!isLog());
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
/*  87 */     log = nlog;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void startRunning() {
/*  92 */     Timer timer = new Timer();
/*  93 */     timer.scheduleAtFixedRate(this, 30000L, 25L);
/*     */   }
/*     */ 
/*     */   
/*     */   public final void shutDown() {
/*  98 */     this.keeprunning = false;
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
/*     */   public void run() {
/* 110 */     if (this.keeprunning) {
/*     */       
/* 112 */       long now = System.currentTimeMillis();
/* 113 */       if (!this.pathTargets.isEmpty())
/*     */       {
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
/*     */       }
/* 142 */       if (log && System.currentTimeMillis() - now > 0L) {
/* 143 */         logger.log(Level.INFO, "AGG Finding paths took " + (System.currentTimeMillis() - now) + " ms for " + this.pathTargets
/* 144 */             .size());
/*     */       }
/*     */     } else {
/*     */       
/* 148 */       logger.log(Level.INFO, "Shutting down Agg pathfinder");
/* 149 */       cancel();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\leo\Desktop\server.jar!\com\wurmonline\server\creatures\ai\CreaturePathFinderAgg.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */